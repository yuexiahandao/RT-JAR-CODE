/*      */ package com.sun.imageio.plugins.jpeg;
/*      */ 
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.color.CMMException;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.color.ICC_ColorSpace;
/*      */ import java.awt.color.ICC_Profile;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorConvertOp;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.security.AccessController;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.ImageReadParam;
/*      */ import javax.imageio.ImageReader;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
/*      */ import javax.imageio.plugins.jpeg.JPEGImageReadParam;
/*      */ import javax.imageio.plugins.jpeg.JPEGQTable;
/*      */ import javax.imageio.spi.ImageReaderSpi;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ import sun.java2d.Disposer;
/*      */ import sun.java2d.DisposerRecord;
/*      */ import sun.security.action.LoadLibraryAction;
/*      */ 
/*      */ public class JPEGImageReader extends ImageReader
/*      */ {
/*   64 */   private boolean debug = false;
/*      */ 
/*   72 */   private long structPointer = 0L;
/*      */ 
/*   75 */   private ImageInputStream iis = null;
/*      */ 
/*   81 */   private List imagePositions = null;
/*      */ 
/*   86 */   private int numImages = 0;
/*      */   protected static final int WARNING_NO_EOI = 0;
/*      */   protected static final int WARNING_NO_JFIF_IN_THUMB = 1;
/*      */   protected static final int WARNING_IGNORE_INVALID_ICC = 2;
/*      */   private static final int MAX_WARNING = 2;
/*  127 */   private int currentImage = -1;
/*      */   private int width;
/*      */   private int height;
/*      */   private int colorSpaceCode;
/*      */   private int outColorSpaceCode;
/*      */   private int numComponents;
/*  150 */   private ColorSpace iccCS = null;
/*      */ 
/*  154 */   private ColorConvertOp convert = null;
/*      */ 
/*  157 */   private BufferedImage image = null;
/*      */ 
/*  160 */   private WritableRaster raster = null;
/*      */ 
/*  163 */   private WritableRaster target = null;
/*      */ 
/*  166 */   private DataBufferByte buffer = null;
/*      */ 
/*  169 */   private Rectangle destROI = null;
/*      */ 
/*  172 */   private int[] destinationBands = null;
/*      */ 
/*  175 */   private JPEGMetadata streamMetadata = null;
/*      */ 
/*  178 */   private JPEGMetadata imageMetadata = null;
/*  179 */   private int imageMetadataIndex = -1;
/*      */ 
/*  185 */   private boolean haveSeeked = false;
/*      */ 
/*  191 */   private JPEGQTable[] abbrevQTables = null;
/*  192 */   private JPEGHuffmanTable[] abbrevDCHuffmanTables = null;
/*  193 */   private JPEGHuffmanTable[] abbrevACHuffmanTables = null;
/*      */ 
/*  195 */   private int minProgressivePass = 0;
/*  196 */   private int maxProgressivePass = 2147483647;
/*      */   private static final int UNKNOWN = -1;
/*      */   private static final int MIN_ESTIMATED_PASSES = 10;
/*  203 */   private int knownPassCount = -1;
/*  204 */   private int pass = 0;
/*  205 */   private float percentToDate = 0.0F;
/*  206 */   private float previousPassPercentage = 0.0F;
/*  207 */   private int progInterval = 0;
/*      */ 
/*  212 */   private boolean tablesOnlyChecked = false;
/*      */ 
/*  215 */   private Object disposerReferent = new Object();
/*      */   private DisposerRecord disposerRecord;
/* 1623 */   private Thread theThread = null;
/* 1624 */   private int theLockCount = 0;
/*      */ 
/* 1659 */   private CallBackLock cbLock = new CallBackLock();
/*      */ 
/*      */   private static native void initReaderIDs(Class paramClass1, Class paramClass2, Class paramClass3);
/*      */ 
/*      */   public JPEGImageReader(ImageReaderSpi paramImageReaderSpi)
/*      */   {
/*  226 */     super(paramImageReaderSpi);
/*  227 */     this.structPointer = initJPEGImageReader();
/*  228 */     this.disposerRecord = new JPEGReaderDisposerRecord(this.structPointer);
/*  229 */     Disposer.addRecord(this.disposerReferent, this.disposerRecord);
/*      */   }
/*      */ 
/*      */   private native long initJPEGImageReader();
/*      */ 
/*      */   protected void warningOccurred(int paramInt)
/*      */   {
/*  241 */     this.cbLock.lock();
/*      */     try {
/*  243 */       if ((paramInt < 0) || (paramInt > 2)) {
/*  244 */         throw new InternalError("Invalid warning index");
/*      */       }
/*  246 */       processWarningOccurred("com.sun.imageio.plugins.jpeg.JPEGImageReaderResources", Integer.toString(paramInt));
/*      */     }
/*      */     finally
/*      */     {
/*  250 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void warningWithMessage(String paramString)
/*      */   {
/*  268 */     this.cbLock.lock();
/*      */     try {
/*  270 */       processWarningOccurred(paramString);
/*      */     } finally {
/*  272 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setInput(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  280 */     setThreadLock();
/*      */     try {
/*  282 */       this.cbLock.check();
/*      */ 
/*  284 */       super.setInput(paramObject, paramBoolean1, paramBoolean2);
/*  285 */       this.ignoreMetadata = paramBoolean2;
/*  286 */       resetInternalState();
/*  287 */       this.iis = ((ImageInputStream)paramObject);
/*  288 */       setSource(this.structPointer);
/*      */     } finally {
/*  290 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private int readInputData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  305 */     this.cbLock.lock();
/*      */     try {
/*  307 */       return this.iis.read(paramArrayOfByte, paramInt1, paramInt2);
/*      */     } finally {
/*  309 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private long skipInputBytes(long paramLong)
/*      */     throws IOException
/*      */   {
/*  322 */     this.cbLock.lock();
/*      */     try {
/*  324 */       return this.iis.skipBytes(paramLong);
/*      */     } finally {
/*  326 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void setSource(long paramLong);
/*      */ 
/*      */   private void checkTablesOnly() throws IOException {
/*  333 */     if (this.debug) {
/*  334 */       System.out.println("Checking for tables-only image");
/*      */     }
/*  336 */     long l1 = this.iis.getStreamPosition();
/*  337 */     if (this.debug) {
/*  338 */       System.out.println("saved pos is " + l1);
/*  339 */       System.out.println("length is " + this.iis.length());
/*      */     }
/*      */ 
/*  342 */     boolean bool = readNativeHeader(true);
/*  343 */     if (bool)
/*      */     {
/*      */       long l2;
/*  344 */       if (this.debug) {
/*  345 */         System.out.println("tables-only image found");
/*  346 */         l2 = this.iis.getStreamPosition();
/*  347 */         System.out.println("pos after return from native is " + l2);
/*      */       }
/*      */ 
/*  351 */       if (!this.ignoreMetadata) {
/*  352 */         this.iis.seek(l1);
/*  353 */         this.haveSeeked = true;
/*  354 */         this.streamMetadata = new JPEGMetadata(true, false, this.iis, this);
/*      */ 
/*  356 */         l2 = this.iis.getStreamPosition();
/*  357 */         if (this.debug) {
/*  358 */           System.out.println("pos after constructing stream metadata is " + l2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  364 */       if (hasNextImage())
/*  365 */         this.imagePositions.add(new Long(this.iis.getStreamPosition()));
/*      */     }
/*      */     else {
/*  368 */       this.imagePositions.add(new Long(l1));
/*      */ 
/*  370 */       this.currentImage = 0;
/*      */     }
/*  372 */     if (this.seekForwardOnly) {
/*  373 */       Long localLong = (Long)this.imagePositions.get(this.imagePositions.size() - 1);
/*  374 */       this.iis.flushBefore(localLong.longValue());
/*      */     }
/*  376 */     this.tablesOnlyChecked = true;
/*      */   }
/*      */ 
/*      */   public int getNumImages(boolean paramBoolean) throws IOException {
/*  380 */     setThreadLock();
/*      */     try {
/*  382 */       this.cbLock.check();
/*      */ 
/*  384 */       return getNumImagesOnThread(paramBoolean);
/*      */     } finally {
/*  386 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private int getNumImagesOnThread(boolean paramBoolean) throws IOException
/*      */   {
/*  392 */     if (this.numImages != 0) {
/*  393 */       return this.numImages;
/*      */     }
/*  395 */     if (this.iis == null) {
/*  396 */       throw new IllegalStateException("Input not set");
/*      */     }
/*  398 */     if (paramBoolean == true) {
/*  399 */       if (this.seekForwardOnly) {
/*  400 */         throw new IllegalStateException("seekForwardOnly and allowSearch can't both be true!");
/*      */       }
/*      */ 
/*  405 */       if (!this.tablesOnlyChecked) {
/*  406 */         checkTablesOnly();
/*      */       }
/*      */ 
/*  409 */       this.iis.mark();
/*      */ 
/*  411 */       gotoImage(0);
/*      */ 
/*  413 */       JPEGBuffer localJPEGBuffer = new JPEGBuffer(this.iis);
/*  414 */       localJPEGBuffer.loadBuf(0);
/*      */ 
/*  416 */       boolean bool = false;
/*  417 */       while (!bool) {
/*  418 */         bool = localJPEGBuffer.scanForFF(this);
/*  419 */         switch (localJPEGBuffer.buf[localJPEGBuffer.bufPtr] & 0xFF) {
/*      */         case 216:
/*  421 */           this.numImages += 1;
/*      */         case 0:
/*      */         case 208:
/*      */         case 209:
/*      */         case 210:
/*      */         case 211:
/*      */         case 212:
/*      */         case 213:
/*      */         case 214:
/*      */         case 215:
/*      */         case 217:
/*  434 */           localJPEGBuffer.bufAvail -= 1;
/*  435 */           localJPEGBuffer.bufPtr += 1;
/*  436 */           break;
/*      */         default:
/*  439 */           localJPEGBuffer.bufAvail -= 1;
/*  440 */           localJPEGBuffer.bufPtr += 1;
/*  441 */           localJPEGBuffer.loadBuf(2);
/*  442 */           int i = (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr++)] & 0xFF) << 8 | localJPEGBuffer.buf[(localJPEGBuffer.bufPtr++)] & 0xFF;
/*      */ 
/*  444 */           localJPEGBuffer.bufAvail -= 2;
/*  445 */           i -= 2;
/*  446 */           localJPEGBuffer.skipData(i);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  451 */       this.iis.reset();
/*      */ 
/*  453 */       return this.numImages;
/*      */     }
/*      */ 
/*  456 */     return -1;
/*      */   }
/*      */ 
/*      */   private void gotoImage(int paramInt)
/*      */     throws IOException
/*      */   {
/*  469 */     if (this.iis == null) {
/*  470 */       throw new IllegalStateException("Input not set");
/*      */     }
/*  472 */     if (paramInt < this.minIndex) {
/*  473 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  475 */     if (!this.tablesOnlyChecked) {
/*  476 */       checkTablesOnly();
/*      */     }
/*  478 */     if (paramInt < this.imagePositions.size()) {
/*  479 */       this.iis.seek(((Long)this.imagePositions.get(paramInt)).longValue());
/*      */     }
/*      */     else
/*      */     {
/*  484 */       Long localLong = (Long)this.imagePositions.get(this.imagePositions.size() - 1);
/*  485 */       this.iis.seek(localLong.longValue());
/*  486 */       skipImage();
/*      */ 
/*  488 */       for (int i = this.imagePositions.size(); 
/*  489 */         i <= paramInt; 
/*  490 */         i++)
/*      */       {
/*  492 */         if (!hasNextImage()) {
/*  493 */           throw new IndexOutOfBoundsException();
/*      */         }
/*  495 */         localLong = new Long(this.iis.getStreamPosition());
/*  496 */         this.imagePositions.add(localLong);
/*  497 */         if (this.seekForwardOnly) {
/*  498 */           this.iis.flushBefore(localLong.longValue());
/*      */         }
/*  500 */         if (i < paramInt) {
/*  501 */           skipImage();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  506 */     if (this.seekForwardOnly) {
/*  507 */       this.minIndex = paramInt;
/*      */     }
/*      */ 
/*  510 */     this.haveSeeked = true;
/*      */   }
/*      */ 
/*      */   private void skipImage()
/*      */     throws IOException
/*      */   {
/*  522 */     if (this.debug) {
/*  523 */       System.out.println("skipImage called");
/*      */     }
/*  525 */     int i = 0;
/*  526 */     for (int j = this.iis.read(); 
/*  527 */       j != -1; 
/*  528 */       j = this.iis.read())
/*      */     {
/*  530 */       if ((i == 1) && 
/*  531 */         (j == 217)) {
/*  532 */         return;
/*      */       }
/*      */ 
/*  535 */       i = j == 255 ? 1 : 0;
/*      */     }
/*  537 */     throw new IndexOutOfBoundsException();
/*      */   }
/*      */ 
/*      */   private boolean hasNextImage()
/*      */     throws IOException
/*      */   {
/*  546 */     if (this.debug) {
/*  547 */       System.out.print("hasNextImage called; returning ");
/*      */     }
/*  549 */     this.iis.mark();
/*  550 */     int i = 0;
/*  551 */     for (int j = this.iis.read(); 
/*  552 */       j != -1; 
/*  553 */       j = this.iis.read())
/*      */     {
/*  555 */       if ((i == 1) && 
/*  556 */         (j == 216)) {
/*  557 */         this.iis.reset();
/*  558 */         if (this.debug) {
/*  559 */           System.out.println("true");
/*      */         }
/*  561 */         return true;
/*      */       }
/*      */ 
/*  564 */       i = j == 255 ? 1 : 0;
/*      */     }
/*      */ 
/*  567 */     this.iis.reset();
/*  568 */     if (this.debug) {
/*  569 */       System.out.println("false");
/*      */     }
/*  571 */     return false;
/*      */   }
/*      */ 
/*      */   private void pushBack(int paramInt)
/*      */     throws IOException
/*      */   {
/*  580 */     if (this.debug) {
/*  581 */       System.out.println("pushing back " + paramInt + " bytes");
/*      */     }
/*  583 */     this.cbLock.lock();
/*      */     try {
/*  585 */       this.iis.seek(this.iis.getStreamPosition() - paramInt);
/*      */     }
/*      */     finally {
/*  588 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readHeader(int paramInt, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  597 */     gotoImage(paramInt);
/*  598 */     readNativeHeader(paramBoolean);
/*  599 */     this.currentImage = paramInt;
/*      */   }
/*      */ 
/*      */   private boolean readNativeHeader(boolean paramBoolean) throws IOException {
/*  603 */     boolean bool = false;
/*  604 */     bool = readImageHeader(this.structPointer, this.haveSeeked, paramBoolean);
/*  605 */     this.haveSeeked = false;
/*  606 */     return bool;
/*      */   }
/*      */ 
/*      */   private native boolean readImageHeader(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws IOException;
/*      */ 
/*      */   private void setImageData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte)
/*      */   {
/*  639 */     this.width = paramInt1;
/*  640 */     this.height = paramInt2;
/*  641 */     this.colorSpaceCode = paramInt3;
/*  642 */     this.outColorSpaceCode = paramInt4;
/*  643 */     this.numComponents = paramInt5;
/*      */ 
/*  645 */     if (paramArrayOfByte == null) {
/*  646 */       this.iccCS = null;
/*  647 */       return;
/*      */     }
/*      */ 
/*  650 */     ICC_Profile localICC_Profile1 = null;
/*      */     try {
/*  652 */       localICC_Profile1 = ICC_Profile.getInstance(paramArrayOfByte);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  658 */       this.iccCS = null;
/*  659 */       warningOccurred(2);
/*      */ 
/*  661 */       return;
/*      */     }
/*  663 */     byte[] arrayOfByte1 = localICC_Profile1.getData();
/*      */ 
/*  665 */     ICC_Profile localICC_Profile2 = null;
/*  666 */     if ((this.iccCS instanceof ICC_ColorSpace)) {
/*  667 */       localICC_Profile2 = ((ICC_ColorSpace)this.iccCS).getProfile();
/*      */     }
/*  669 */     byte[] arrayOfByte2 = null;
/*  670 */     if (localICC_Profile2 != null) {
/*  671 */       arrayOfByte2 = localICC_Profile2.getData();
/*      */     }
/*      */ 
/*  683 */     if ((arrayOfByte2 == null) || (!Arrays.equals(arrayOfByte2, arrayOfByte1)))
/*      */     {
/*  686 */       this.iccCS = new ICC_ColorSpace(localICC_Profile1);
/*      */       try
/*      */       {
/*  689 */         float[] arrayOfFloat = this.iccCS.fromRGB(new float[] { 1.0F, 0.0F, 0.0F });
/*      */       }
/*      */       catch (CMMException localCMMException)
/*      */       {
/*  695 */         this.iccCS = null;
/*  696 */         this.cbLock.lock();
/*      */         try {
/*  698 */           warningOccurred(2);
/*      */         } finally {
/*  700 */           this.cbLock.unlock();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getWidth(int paramInt) throws IOException {
/*  707 */     setThreadLock();
/*      */     try {
/*  709 */       if (this.currentImage != paramInt) {
/*  710 */         this.cbLock.check();
/*  711 */         readHeader(paramInt, true);
/*      */       }
/*  713 */       return this.width;
/*      */     } finally {
/*  715 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getHeight(int paramInt) throws IOException {
/*  720 */     setThreadLock();
/*      */     try {
/*  722 */       if (this.currentImage != paramInt) {
/*  723 */         this.cbLock.check();
/*  724 */         readHeader(paramInt, true);
/*      */       }
/*  726 */       return this.height;
/*      */     } finally {
/*  728 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private ImageTypeProducer getImageType(int paramInt)
/*      */   {
/*  739 */     ImageTypeProducer localImageTypeProducer = null;
/*      */ 
/*  741 */     if ((paramInt > 0) && (paramInt < 12)) {
/*  742 */       localImageTypeProducer = ImageTypeProducer.getTypeProducer(paramInt);
/*      */     }
/*  744 */     return localImageTypeProducer;
/*      */   }
/*      */ 
/*      */   public ImageTypeSpecifier getRawImageType(int paramInt) throws IOException
/*      */   {
/*  749 */     setThreadLock();
/*      */     try {
/*  751 */       if (this.currentImage != paramInt) {
/*  752 */         this.cbLock.check();
/*      */ 
/*  754 */         readHeader(paramInt, true);
/*      */       }
/*      */ 
/*  758 */       return getImageType(this.colorSpaceCode).getType();
/*      */     } finally {
/*  760 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Iterator getImageTypes(int paramInt) throws IOException
/*      */   {
/*  766 */     setThreadLock();
/*      */     try {
/*  768 */       return getImageTypesOnThread(paramInt);
/*      */     } finally {
/*  770 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Iterator getImageTypesOnThread(int paramInt) throws IOException
/*      */   {
/*  776 */     if (this.currentImage != paramInt) {
/*  777 */       this.cbLock.check();
/*  778 */       readHeader(paramInt, true);
/*      */     }
/*      */ 
/*  793 */     ImageTypeProducer localImageTypeProducer = getImageType(this.colorSpaceCode);
/*      */ 
/*  799 */     ArrayList localArrayList = new ArrayList(1);
/*      */ 
/*  801 */     switch (this.colorSpaceCode) {
/*      */     case 1:
/*  803 */       localArrayList.add(localImageTypeProducer);
/*  804 */       localArrayList.add(getImageType(2));
/*  805 */       break;
/*      */     case 2:
/*  807 */       localArrayList.add(localImageTypeProducer);
/*  808 */       localArrayList.add(getImageType(1));
/*  809 */       localArrayList.add(getImageType(5));
/*  810 */       break;
/*      */     case 6:
/*  812 */       localArrayList.add(localImageTypeProducer);
/*  813 */       break;
/*      */     case 5:
/*  815 */       if (localImageTypeProducer != null) {
/*  816 */         localArrayList.add(localImageTypeProducer);
/*  817 */         localArrayList.add(getImageType(2)); } break;
/*      */     case 10:
/*  821 */       if (localImageTypeProducer != null)
/*  822 */         localArrayList.add(localImageTypeProducer); break;
/*      */     case 3:
/*  831 */       localArrayList.add(getImageType(2));
/*      */ 
/*  833 */       if (this.iccCS != null) {
/*  834 */         localArrayList.add(new ImageTypeProducer() {
/*      */           protected ImageTypeSpecifier produce() {
/*  836 */             return ImageTypeSpecifier.createInterleaved(JPEGImageReader.this.iccCS, JPEG.bOffsRGB, 0, false, false);
/*      */           }
/*      */ 
/*      */         });
/*      */       }
/*      */ 
/*  847 */       localArrayList.add(getImageType(1));
/*  848 */       localArrayList.add(getImageType(5));
/*  849 */       break;
/*      */     case 7:
/*  853 */       localArrayList.add(getImageType(6));
/*      */     case 4:
/*      */     case 8:
/*      */     case 9:
/*  857 */     }return new ImageTypeIterator(localArrayList.iterator());
/*      */   }
/*      */ 
/*      */   private void checkColorConversion(BufferedImage paramBufferedImage, ImageReadParam paramImageReadParam)
/*      */     throws IIOException
/*      */   {
/*  878 */     if ((paramImageReadParam != null) && (
/*  879 */       (paramImageReadParam.getSourceBands() != null) || (paramImageReadParam.getDestinationBands() != null)))
/*      */     {
/*  882 */       return;
/*      */     }
/*      */ 
/*  891 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*      */ 
/*  893 */     if ((localColorModel instanceof IndexColorModel)) {
/*  894 */       throw new IIOException("IndexColorModel not supported");
/*      */     }
/*      */ 
/*  899 */     ColorSpace localColorSpace1 = localColorModel.getColorSpace();
/*  900 */     int i = localColorSpace1.getType();
/*  901 */     this.convert = null;
/*      */     ColorSpace localColorSpace2;
/*  902 */     switch (this.outColorSpaceCode) {
/*      */     case 1:
/*  904 */       if (i == 5)
/*      */       {
/*  906 */         setOutColorSpace(this.structPointer, 2);
/*      */ 
/*  909 */         this.outColorSpaceCode = 2;
/*  910 */         this.numComponents = 3;
/*  911 */       } else if (i != 6) {
/*  912 */         throw new IIOException("Incompatible color conversion");
/*      */       }
/*      */       break;
/*      */     case 2:
/*  916 */       if (i == 6) {
/*  917 */         if (this.colorSpaceCode == 3)
/*      */         {
/*  919 */           setOutColorSpace(this.structPointer, 1);
/*      */ 
/*  922 */           this.outColorSpaceCode = 1;
/*  923 */           this.numComponents = 1;
/*      */         }
/*  925 */       } else if ((this.iccCS != null) && (localColorModel.getNumComponents() == this.numComponents) && (localColorSpace1 != this.iccCS))
/*      */       {
/*  930 */         this.convert = new ColorConvertOp(this.iccCS, localColorSpace1, null);
/*      */       }
/*  932 */       else if ((this.iccCS == null) && (!localColorSpace1.isCS_sRGB()) && (localColorModel.getNumComponents() == this.numComponents))
/*      */       {
/*  936 */         this.convert = new ColorConvertOp(JPEG.JCS.sRGB, localColorSpace1, null);
/*  937 */       } else if (i != 5) {
/*  938 */         throw new IIOException("Incompatible color conversion");
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 6:
/*  943 */       if ((i != 5) || (localColorModel.getNumComponents() != this.numComponents))
/*      */       {
/*  945 */         throw new IIOException("Incompatible color conversion");
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 5:
/*  950 */       localColorSpace2 = JPEG.JCS.getYCC();
/*  951 */       if (localColorSpace2 == null) {
/*  952 */         throw new IIOException("Incompatible color conversion");
/*      */       }
/*  954 */       if ((localColorSpace1 != localColorSpace2) && (localColorModel.getNumComponents() == this.numComponents))
/*      */       {
/*  956 */         this.convert = new ColorConvertOp(localColorSpace2, localColorSpace1, null);
/*      */       }
/*      */ 
/*  959 */       break;
/*      */     case 10:
/*  962 */       localColorSpace2 = JPEG.JCS.getYCC();
/*      */ 
/*  964 */       if ((localColorSpace2 == null) || (localColorSpace1 != localColorSpace2) || (localColorModel.getNumComponents() != this.numComponents))
/*      */       {
/*  967 */         throw new IIOException("Incompatible color conversion");
/*      */       }
/*      */ 
/*  970 */       break;
/*      */     case 3:
/*      */     case 4:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     default:
/*  973 */       throw new IIOException("Incompatible color conversion");
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void setOutColorSpace(long paramLong, int paramInt);
/*      */ 
/*      */   public ImageReadParam getDefaultReadParam()
/*      */   {
/*  986 */     return new JPEGImageReadParam();
/*      */   }
/*      */ 
/*      */   public IIOMetadata getStreamMetadata() throws IOException {
/*  990 */     setThreadLock();
/*      */     try {
/*  992 */       if (!this.tablesOnlyChecked) {
/*  993 */         this.cbLock.check();
/*  994 */         checkTablesOnly();
/*      */       }
/*  996 */       return this.streamMetadata;
/*      */     } finally {
/*  998 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public IIOMetadata getImageMetadata(int paramInt) throws IOException
/*      */   {
/* 1004 */     setThreadLock();
/*      */     try
/*      */     {
/*      */       JPEGMetadata localJPEGMetadata;
/* 1009 */       if ((this.imageMetadataIndex == paramInt) && (this.imageMetadata != null))
/*      */       {
/* 1011 */         return this.imageMetadata;
/*      */       }
/*      */ 
/* 1014 */       this.cbLock.check();
/*      */ 
/* 1016 */       gotoImage(paramInt);
/*      */ 
/* 1018 */       this.imageMetadata = new JPEGMetadata(false, false, this.iis, this);
/*      */ 
/* 1020 */       this.imageMetadataIndex = paramInt;
/*      */ 
/* 1022 */       return this.imageMetadata;
/*      */     } finally {
/* 1024 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public BufferedImage read(int paramInt, ImageReadParam paramImageReadParam) throws IOException
/*      */   {
/* 1030 */     setThreadLock();
/*      */     try {
/* 1032 */       this.cbLock.check();
/*      */       try {
/* 1034 */         readInternal(paramInt, paramImageReadParam, false);
/*      */       } catch (RuntimeException localRuntimeException) {
/* 1036 */         resetLibraryState(this.structPointer);
/* 1037 */         throw localRuntimeException;
/*      */       } catch (IOException localIOException) {
/* 1039 */         resetLibraryState(this.structPointer);
/* 1040 */         throw localIOException;
/*      */       }
/*      */ 
/* 1043 */       BufferedImage localBufferedImage1 = this.image;
/* 1044 */       this.image = null;
/* 1045 */       return localBufferedImage1;
/*      */     } finally {
/* 1047 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private Raster readInternal(int paramInt, ImageReadParam paramImageReadParam, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1054 */     readHeader(paramInt, false);
/*      */ 
/* 1056 */     WritableRaster localWritableRaster = null;
/* 1057 */     int i = 0;
/*      */ 
/* 1059 */     if (!paramBoolean)
/*      */     {
/* 1061 */       localObject1 = getImageTypes(paramInt);
/* 1062 */       if (!((Iterator)localObject1).hasNext()) {
/* 1063 */         throw new IIOException("Unsupported Image Type");
/*      */       }
/*      */ 
/* 1066 */       this.image = getDestination(paramImageReadParam, (Iterator)localObject1, this.width, this.height);
/* 1067 */       localWritableRaster = this.image.getRaster();
/*      */ 
/* 1071 */       i = this.image.getSampleModel().getNumBands();
/*      */ 
/* 1078 */       checkColorConversion(this.image, paramImageReadParam);
/*      */ 
/* 1081 */       checkReadParamBandSettings(paramImageReadParam, this.numComponents, i);
/*      */     }
/*      */     else
/*      */     {
/* 1085 */       setOutColorSpace(this.structPointer, this.colorSpaceCode);
/* 1086 */       this.image = null;
/*      */     }
/*      */ 
/* 1096 */     Object localObject1 = JPEG.bandOffsets[(this.numComponents - 1)];
/* 1097 */     int j = paramBoolean ? this.numComponents : i;
/* 1098 */     this.destinationBands = null;
/*      */ 
/* 1100 */     Rectangle localRectangle = new Rectangle(0, 0, 0, 0);
/* 1101 */     this.destROI = new Rectangle(0, 0, 0, 0);
/* 1102 */     computeRegions(paramImageReadParam, this.width, this.height, this.image, localRectangle, this.destROI);
/*      */ 
/* 1104 */     int k = 1;
/* 1105 */     int m = 1;
/*      */ 
/* 1107 */     this.minProgressivePass = 0;
/* 1108 */     this.maxProgressivePass = 2147483647;
/*      */ 
/* 1110 */     if (paramImageReadParam != null) {
/* 1111 */       k = paramImageReadParam.getSourceXSubsampling();
/* 1112 */       m = paramImageReadParam.getSourceYSubsampling();
/*      */ 
/* 1114 */       int[] arrayOfInt1 = paramImageReadParam.getSourceBands();
/* 1115 */       if (arrayOfInt1 != null) {
/* 1116 */         localObject1 = arrayOfInt1;
/* 1117 */         j = localObject1.length;
/*      */       }
/* 1119 */       if (!paramBoolean) {
/* 1120 */         this.destinationBands = paramImageReadParam.getDestinationBands();
/*      */       }
/*      */ 
/* 1123 */       this.minProgressivePass = paramImageReadParam.getSourceMinProgressivePass();
/* 1124 */       this.maxProgressivePass = paramImageReadParam.getSourceMaxProgressivePass();
/*      */ 
/* 1126 */       if ((paramImageReadParam instanceof JPEGImageReadParam)) {
/* 1127 */         localObject2 = (JPEGImageReadParam)paramImageReadParam;
/* 1128 */         if (((JPEGImageReadParam)localObject2).areTablesSet()) {
/* 1129 */           this.abbrevQTables = ((JPEGImageReadParam)localObject2).getQTables();
/* 1130 */           this.abbrevDCHuffmanTables = ((JPEGImageReadParam)localObject2).getDCHuffmanTables();
/* 1131 */           this.abbrevACHuffmanTables = ((JPEGImageReadParam)localObject2).getACHuffmanTables();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1136 */     int n = this.destROI.width * j;
/*      */ 
/* 1138 */     this.buffer = new DataBufferByte(n);
/*      */ 
/* 1140 */     Object localObject2 = JPEG.bandOffsets[(j - 1)];
/*      */ 
/* 1142 */     this.raster = Raster.createInterleavedRaster(this.buffer, this.destROI.width, 1, n, j, (int[])localObject2, null);
/*      */ 
/* 1151 */     if (paramBoolean) {
/* 1152 */       this.target = Raster.createInterleavedRaster(0, this.destROI.width, this.destROI.height, n, j, (int[])localObject2, null);
/*      */     }
/*      */     else
/*      */     {
/* 1160 */       this.target = localWritableRaster;
/*      */     }
/* 1162 */     int[] arrayOfInt2 = this.target.getSampleModel().getSampleSize();
/* 1163 */     for (int i1 = 0; i1 < arrayOfInt2.length; i1++) {
/* 1164 */       if ((arrayOfInt2[i1] <= 0) || (arrayOfInt2[i1] > 8)) {
/* 1165 */         throw new IIOException("Illegal band size: should be 0 < size <= 8");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1176 */     i1 = (this.updateListeners != null) || (this.progressListeners != null) ? 1 : 0;
/*      */ 
/* 1180 */     initProgressData();
/*      */ 
/* 1183 */     if (paramInt == this.imageMetadataIndex) {
/* 1184 */       this.knownPassCount = 0;
/* 1185 */       Iterator localIterator = this.imageMetadata.markerSequence.iterator();
/* 1186 */       while (localIterator.hasNext()) {
/* 1187 */         if ((localIterator.next() instanceof SOSMarkerSegment)) {
/* 1188 */           this.knownPassCount += 1;
/*      */         }
/*      */       }
/*      */     }
/* 1192 */     this.progInterval = Math.max((this.target.getHeight() - 1) / 20, 1);
/* 1193 */     if (this.knownPassCount > 0)
/* 1194 */       this.progInterval *= this.knownPassCount;
/* 1195 */     else if (this.maxProgressivePass != 2147483647) {
/* 1196 */       this.progInterval *= (this.maxProgressivePass - this.minProgressivePass + 1);
/*      */     }
/*      */ 
/* 1199 */     if (this.debug) {
/* 1200 */       System.out.println("**** Read Data *****");
/* 1201 */       System.out.println("numRasterBands is " + j);
/* 1202 */       System.out.print("srcBands:");
/* 1203 */       for (i2 = 0; i2 < localObject1.length; i2++)
/* 1204 */         System.out.print(" " + localObject1[i2]);
/* 1205 */       System.out.println();
/* 1206 */       System.out.println("destination bands is " + this.destinationBands);
/* 1207 */       if (this.destinationBands != null) {
/* 1208 */         for (i2 = 0; i2 < this.destinationBands.length; i2++) {
/* 1209 */           System.out.print(" " + this.destinationBands[i2]);
/*      */         }
/* 1211 */         System.out.println();
/*      */       }
/* 1213 */       System.out.println("sourceROI is " + localRectangle);
/* 1214 */       System.out.println("destROI is " + this.destROI);
/* 1215 */       System.out.println("periodX is " + k);
/* 1216 */       System.out.println("periodY is " + m);
/* 1217 */       System.out.println("minProgressivePass is " + this.minProgressivePass);
/* 1218 */       System.out.println("maxProgressivePass is " + this.maxProgressivePass);
/* 1219 */       System.out.println("callbackUpdates is " + i1);
/*      */     }
/*      */ 
/* 1224 */     processImageStarted(this.currentImage);
/*      */ 
/* 1226 */     int i2 = 0;
/*      */ 
/* 1231 */     boolean bool = readImage(this.structPointer, this.buffer.getData(), j, (int[])localObject1, arrayOfInt2, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, k, m, this.abbrevQTables, this.abbrevDCHuffmanTables, this.abbrevACHuffmanTables, this.minProgressivePass, this.maxProgressivePass, i1);
/*      */ 
/* 1245 */     if (bool)
/* 1246 */       processReadAborted();
/*      */     else {
/* 1248 */       processImageComplete();
/*      */     }
/*      */ 
/* 1251 */     return this.target;
/*      */   }
/*      */ 
/*      */   private void acceptPixels(int paramInt, boolean paramBoolean)
/*      */   {
/* 1262 */     if (this.convert != null) {
/* 1263 */       this.convert.filter(this.raster, this.raster);
/*      */     }
/* 1265 */     this.target.setRect(this.destROI.x, this.destROI.y + paramInt, this.raster);
/*      */ 
/* 1267 */     this.cbLock.lock();
/*      */     try {
/* 1269 */       processImageUpdate(this.image, this.destROI.x, this.destROI.y + paramInt, this.raster.getWidth(), 1, 1, 1, this.destinationBands);
/*      */ 
/* 1274 */       if ((paramInt > 0) && (paramInt % this.progInterval == 0)) {
/* 1275 */         int i = this.target.getHeight() - 1;
/* 1276 */         float f = paramInt / i;
/* 1277 */         if (paramBoolean) {
/* 1278 */           if (this.knownPassCount != -1) {
/* 1279 */             processImageProgress((this.pass + f) * 100.0F / this.knownPassCount);
/*      */           }
/* 1281 */           else if (this.maxProgressivePass != 2147483647)
/*      */           {
/* 1283 */             processImageProgress((this.pass + f) * 100.0F / (this.maxProgressivePass - this.minProgressivePass + 1));
/*      */           }
/*      */           else
/*      */           {
/* 1294 */             int j = Math.max(2, 10 - this.pass);
/*      */ 
/* 1296 */             int k = this.pass + j - 1;
/* 1297 */             this.progInterval = Math.max(i / 20 * k, k);
/*      */ 
/* 1299 */             if (paramInt % this.progInterval == 0) {
/* 1300 */               this.percentToDate = (this.previousPassPercentage + (1.0F - this.previousPassPercentage) * f / j);
/*      */ 
/* 1303 */               if (this.debug) {
/* 1304 */                 System.out.print("pass= " + this.pass);
/* 1305 */                 System.out.print(", y= " + paramInt);
/* 1306 */                 System.out.print(", progInt= " + this.progInterval);
/* 1307 */                 System.out.print(", % of pass: " + f);
/* 1308 */                 System.out.print(", rem. passes: " + j);
/*      */ 
/* 1310 */                 System.out.print(", prev%: " + this.previousPassPercentage);
/*      */ 
/* 1312 */                 System.out.print(", %ToDate: " + this.percentToDate);
/* 1313 */                 System.out.print(" ");
/*      */               }
/* 1315 */               processImageProgress(this.percentToDate * 100.0F);
/*      */             }
/*      */           }
/*      */         }
/* 1319 */         else processImageProgress(f * 100.0F); 
/*      */       }
/*      */     }
/*      */     finally
/*      */     {
/* 1323 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initProgressData() {
/* 1328 */     this.knownPassCount = -1;
/* 1329 */     this.pass = 0;
/* 1330 */     this.percentToDate = 0.0F;
/* 1331 */     this.previousPassPercentage = 0.0F;
/* 1332 */     this.progInterval = 0;
/*      */   }
/*      */ 
/*      */   private void passStarted(int paramInt) {
/* 1336 */     this.cbLock.lock();
/*      */     try {
/* 1338 */       this.pass = paramInt;
/* 1339 */       this.previousPassPercentage = this.percentToDate;
/* 1340 */       processPassStarted(this.image, paramInt, this.minProgressivePass, this.maxProgressivePass, 0, 0, 1, 1, this.destinationBands);
/*      */     }
/*      */     finally
/*      */     {
/* 1348 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void passComplete() {
/* 1353 */     this.cbLock.lock();
/*      */     try {
/* 1355 */       processPassComplete(this.image);
/*      */     } finally {
/* 1357 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void thumbnailStarted(int paramInt) {
/* 1362 */     this.cbLock.lock();
/*      */     try {
/* 1364 */       processThumbnailStarted(this.currentImage, paramInt);
/*      */     } finally {
/* 1366 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void thumbnailProgress(float paramFloat)
/*      */   {
/* 1372 */     this.cbLock.lock();
/*      */     try {
/* 1374 */       processThumbnailProgress(paramFloat);
/*      */     } finally {
/* 1376 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void thumbnailComplete()
/*      */   {
/* 1382 */     this.cbLock.lock();
/*      */     try {
/* 1384 */       processThumbnailComplete();
/*      */     } finally {
/* 1386 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private native boolean readImage(long paramLong, byte[] paramArrayOfByte, int paramInt1, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, JPEGQTable[] paramArrayOfJPEGQTable, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable1, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable2, int paramInt8, int paramInt9, boolean paramBoolean);
/*      */ 
/*      */   public void abort()
/*      */   {
/* 1409 */     setThreadLock();
/*      */     try
/*      */     {
/* 1416 */       super.abort();
/* 1417 */       abortRead(this.structPointer);
/*      */     } finally {
/* 1419 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void abortRead(long paramLong);
/*      */ 
/*      */   private native void resetLibraryState(long paramLong);
/*      */ 
/*      */   public boolean canReadRaster()
/*      */   {
/* 1430 */     return true;
/*      */   }
/*      */ 
/*      */   public Raster readRaster(int paramInt, ImageReadParam paramImageReadParam) throws IOException
/*      */   {
/* 1435 */     setThreadLock();
/* 1436 */     Raster localRaster = null;
/*      */     try {
/* 1438 */       this.cbLock.check();
/*      */ 
/* 1448 */       Point localPoint = null;
/* 1449 */       if (paramImageReadParam != null) {
/* 1450 */         localPoint = paramImageReadParam.getDestinationOffset();
/* 1451 */         paramImageReadParam.setDestinationOffset(new Point(0, 0));
/*      */       }
/* 1453 */       localRaster = readInternal(paramInt, paramImageReadParam, true);
/*      */ 
/* 1455 */       if (localPoint != null)
/* 1456 */         this.target = this.target.createWritableTranslatedChild(localPoint.x, localPoint.y);
/*      */     }
/*      */     catch (RuntimeException localRuntimeException)
/*      */     {
/* 1460 */       resetLibraryState(this.structPointer);
/* 1461 */       throw localRuntimeException;
/*      */     } catch (IOException localIOException) {
/* 1463 */       resetLibraryState(this.structPointer);
/* 1464 */       throw localIOException;
/*      */     } finally {
/* 1466 */       clearThreadLock();
/*      */     }
/* 1468 */     return localRaster;
/*      */   }
/*      */ 
/*      */   public boolean readerSupportsThumbnails() {
/* 1472 */     return true;
/*      */   }
/*      */ 
/*      */   public int getNumThumbnails(int paramInt) throws IOException {
/* 1476 */     setThreadLock();
/*      */     try {
/* 1478 */       this.cbLock.check();
/*      */ 
/* 1480 */       getImageMetadata(paramInt);
/*      */ 
/* 1482 */       JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)this.imageMetadata.findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 1485 */       int i = 0;
/* 1486 */       if (localJFIFMarkerSegment != null) {
/* 1487 */         i = localJFIFMarkerSegment.thumb == null ? 0 : 1;
/* 1488 */         i += localJFIFMarkerSegment.extSegments.size();
/*      */       }
/* 1490 */       return i;
/*      */     } finally {
/* 1492 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getThumbnailWidth(int paramInt1, int paramInt2) throws IOException
/*      */   {
/* 1498 */     setThreadLock();
/*      */     try {
/* 1500 */       this.cbLock.check();
/*      */ 
/* 1502 */       if ((paramInt2 < 0) || (paramInt2 >= getNumThumbnails(paramInt1)))
/*      */       {
/* 1504 */         throw new IndexOutOfBoundsException("No such thumbnail");
/*      */       }
/*      */ 
/* 1507 */       JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)this.imageMetadata.findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 1510 */       return localJFIFMarkerSegment.getThumbnailWidth(paramInt2);
/*      */     } finally {
/* 1512 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getThumbnailHeight(int paramInt1, int paramInt2) throws IOException
/*      */   {
/* 1518 */     setThreadLock();
/*      */     try {
/* 1520 */       this.cbLock.check();
/*      */ 
/* 1522 */       if ((paramInt2 < 0) || (paramInt2 >= getNumThumbnails(paramInt1)))
/*      */       {
/* 1524 */         throw new IndexOutOfBoundsException("No such thumbnail");
/*      */       }
/*      */ 
/* 1527 */       JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)this.imageMetadata.findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 1530 */       return localJFIFMarkerSegment.getThumbnailHeight(paramInt2);
/*      */     } finally {
/* 1532 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public BufferedImage readThumbnail(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1539 */     setThreadLock();
/*      */     try {
/* 1541 */       this.cbLock.check();
/*      */ 
/* 1543 */       if ((paramInt2 < 0) || (paramInt2 >= getNumThumbnails(paramInt1)))
/*      */       {
/* 1545 */         throw new IndexOutOfBoundsException("No such thumbnail");
/*      */       }
/*      */ 
/* 1548 */       JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)this.imageMetadata.findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 1551 */       return localJFIFMarkerSegment.getThumbnail(this.iis, paramInt2, this);
/*      */     } finally {
/* 1553 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resetInternalState()
/*      */   {
/* 1559 */     resetReader(this.structPointer);
/*      */ 
/* 1562 */     this.numImages = 0;
/* 1563 */     this.imagePositions = new ArrayList();
/* 1564 */     this.currentImage = -1;
/* 1565 */     this.image = null;
/* 1566 */     this.raster = null;
/* 1567 */     this.target = null;
/* 1568 */     this.buffer = null;
/* 1569 */     this.destROI = null;
/* 1570 */     this.destinationBands = null;
/* 1571 */     this.streamMetadata = null;
/* 1572 */     this.imageMetadata = null;
/* 1573 */     this.imageMetadataIndex = -1;
/* 1574 */     this.haveSeeked = false;
/* 1575 */     this.tablesOnlyChecked = false;
/* 1576 */     this.iccCS = null;
/* 1577 */     initProgressData();
/*      */   }
/*      */ 
/*      */   public void reset() {
/* 1581 */     setThreadLock();
/*      */     try {
/* 1583 */       this.cbLock.check();
/* 1584 */       super.reset();
/*      */     } finally {
/* 1586 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void resetReader(long paramLong);
/*      */ 
/*      */   public void dispose() {
/* 1593 */     setThreadLock();
/*      */     try {
/* 1595 */       this.cbLock.check();
/*      */ 
/* 1597 */       if (this.structPointer != 0L) {
/* 1598 */         this.disposerRecord.dispose();
/* 1599 */         this.structPointer = 0L;
/*      */       }
/*      */     } finally {
/* 1602 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void disposeReader(long paramLong);
/*      */ 
/*      */   private synchronized void setThreadLock()
/*      */   {
/* 1627 */     Thread localThread = Thread.currentThread();
/* 1628 */     if (this.theThread != null) {
/* 1629 */       if (this.theThread != localThread)
/*      */       {
/* 1632 */         throw new IllegalStateException("Attempt to use instance of " + this + " locked on thread " + this.theThread + " from thread " + localThread);
/*      */       }
/*      */ 
/* 1637 */       this.theLockCount += 1;
/*      */     }
/*      */     else {
/* 1640 */       this.theThread = localThread;
/* 1641 */       this.theLockCount = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void clearThreadLock() {
/* 1646 */     Thread localThread = Thread.currentThread();
/* 1647 */     if ((this.theThread == null) || (this.theThread != localThread)) {
/* 1648 */       throw new IllegalStateException("Attempt to clear thread lock  form wrong thread. Locked thread: " + this.theThread + "; current thread: " + localThread);
/*      */     }
/*      */ 
/* 1653 */     this.theLockCount -= 1;
/* 1654 */     if (this.theLockCount == 0)
/* 1655 */       this.theThread = null;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   89 */     AccessController.doPrivileged(new LoadLibraryAction("jpeg"));
/*      */ 
/*   91 */     initReaderIDs(ImageInputStream.class, JPEGQTable.class, JPEGHuffmanTable.class);
/*      */   }
/*      */ 
/*      */   private static class CallBackLock
/*      */   {
/*      */     private State lockState;
/*      */ 
/*      */     CallBackLock()
/*      */     {
/* 1666 */       this.lockState = State.Unlocked;
/*      */     }
/*      */ 
/*      */     void check() {
/* 1670 */       if (this.lockState != State.Unlocked)
/* 1671 */         throw new IllegalStateException("Access to the reader is not allowed");
/*      */     }
/*      */ 
/*      */     private void lock()
/*      */     {
/* 1676 */       this.lockState = State.Locked;
/*      */     }
/*      */ 
/*      */     private void unlock() {
/* 1680 */       this.lockState = State.Unlocked;
/*      */     }
/*      */ 
/*      */     private static enum State {
/* 1684 */       Unlocked, 
/* 1685 */       Locked;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class JPEGReaderDisposerRecord
/*      */     implements DisposerRecord
/*      */   {
/*      */     private long pData;
/*      */ 
/*      */     public JPEGReaderDisposerRecord(long paramLong)
/*      */     {
/* 1612 */       this.pData = paramLong;
/*      */     }
/*      */ 
/*      */     public synchronized void dispose() {
/* 1616 */       if (this.pData != 0L) {
/* 1617 */         JPEGImageReader.disposeReader(this.pData);
/* 1618 */         this.pData = 0L;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGImageReader
 * JD-Core Version:    0.6.2
 */
/*      */ package com.sun.imageio.plugins.jpeg;
/*      */ 
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.color.ICC_ColorSpace;
/*      */ import java.awt.color.ICC_Profile;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorConvertOp;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.security.AccessController;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.IIOImage;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.ImageWriteParam;
/*      */ import javax.imageio.ImageWriter;
/*      */ import javax.imageio.metadata.IIOInvalidTreeException;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
/*      */ import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
/*      */ import javax.imageio.plugins.jpeg.JPEGQTable;
/*      */ import javax.imageio.spi.ImageWriterSpi;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ import org.w3c.dom.Node;
/*      */ import sun.java2d.Disposer;
/*      */ import sun.java2d.DisposerRecord;
/*      */ import sun.security.action.LoadLibraryAction;
/*      */ 
/*      */ public class JPEGImageWriter extends ImageWriter
/*      */ {
/*   74 */   private boolean debug = false;
/*      */ 
/*   82 */   private long structPointer = 0L;
/*      */ 
/*   86 */   private ImageOutputStream ios = null;
/*      */ 
/*   89 */   private Raster srcRas = null;
/*      */ 
/*   92 */   private WritableRaster raster = null;
/*      */ 
/*   98 */   private boolean indexed = false;
/*   99 */   private IndexColorModel indexCM = null;
/*      */ 
/*  101 */   private boolean convertTosRGB = false;
/*  102 */   private WritableRaster converted = null;
/*      */ 
/*  104 */   private boolean isAlphaPremultiplied = false;
/*  105 */   private ColorModel srcCM = null;
/*      */ 
/*  110 */   private List thumbnails = null;
/*      */ 
/*  115 */   private ICC_Profile iccProfile = null;
/*      */ 
/*  117 */   private int sourceXOffset = 0;
/*  118 */   private int sourceYOffset = 0;
/*  119 */   private int sourceWidth = 0;
/*  120 */   private int[] srcBands = null;
/*  121 */   private int sourceHeight = 0;
/*      */ 
/*  124 */   private int currentImage = 0;
/*      */ 
/*  126 */   private ColorConvertOp convertOp = null;
/*      */ 
/*  128 */   private JPEGQTable[] streamQTables = null;
/*  129 */   private JPEGHuffmanTable[] streamDCHuffmanTables = null;
/*  130 */   private JPEGHuffmanTable[] streamACHuffmanTables = null;
/*      */ 
/*  133 */   private boolean ignoreJFIF = false;
/*  134 */   private boolean forceJFIF = false;
/*  135 */   private boolean ignoreAdobe = false;
/*  136 */   private int newAdobeTransform = -1;
/*  137 */   private boolean writeDefaultJFIF = false;
/*  138 */   private boolean writeAdobe = false;
/*  139 */   private JPEGMetadata metadata = null;
/*      */ 
/*  141 */   private boolean sequencePrepared = false;
/*      */ 
/*  143 */   private int numScans = 0;
/*      */ 
/*  146 */   private Object disposerReferent = new Object();
/*      */   private DisposerRecord disposerRecord;
/*      */   protected static final int WARNING_DEST_IGNORED = 0;
/*      */   protected static final int WARNING_STREAM_METADATA_IGNORED = 1;
/*      */   protected static final int WARNING_DEST_METADATA_COMP_MISMATCH = 2;
/*      */   protected static final int WARNING_DEST_METADATA_JFIF_MISMATCH = 3;
/*      */   protected static final int WARNING_DEST_METADATA_ADOBE_MISMATCH = 4;
/*      */   protected static final int WARNING_IMAGE_METADATA_JFIF_MISMATCH = 5;
/*      */   protected static final int WARNING_IMAGE_METADATA_ADOBE_MISMATCH = 6;
/*      */   protected static final int WARNING_METADATA_NOT_JPEG_FOR_RASTER = 7;
/*      */   protected static final int WARNING_NO_BANDS_ON_INDEXED = 8;
/*      */   protected static final int WARNING_ILLEGAL_THUMBNAIL = 9;
/*      */   protected static final int WARNING_IGNORING_THUMBS = 10;
/*      */   protected static final int WARNING_FORCING_JFIF = 11;
/*      */   protected static final int WARNING_THUMB_CLIPPED = 12;
/*      */   protected static final int WARNING_METADATA_ADJUSTED_FOR_THUMB = 13;
/*      */   protected static final int WARNING_NO_RGB_THUMB_AS_INDEXED = 14;
/*      */   protected static final int WARNING_NO_GRAY_THUMB_AS_INDEXED = 15;
/*      */   private static final int MAX_WARNING = 15;
/*  309 */   static final Dimension[] preferredThumbSizes = { new Dimension(1, 1), new Dimension(255, 255) };
/*      */ 
/* 1844 */   private Thread theThread = null;
/* 1845 */   private int theLockCount = 0;
/*      */ 
/* 1879 */   private CallBackLock cbLock = new CallBackLock();
/*      */ 
/*      */   public JPEGImageWriter(ImageWriterSpi paramImageWriterSpi)
/*      */   {
/*  188 */     super(paramImageWriterSpi);
/*  189 */     this.structPointer = initJPEGImageWriter();
/*  190 */     this.disposerRecord = new JPEGWriterDisposerRecord(this.structPointer);
/*  191 */     Disposer.addRecord(this.disposerReferent, this.disposerRecord);
/*      */   }
/*      */ 
/*      */   public void setOutput(Object paramObject) {
/*  195 */     setThreadLock();
/*      */     try {
/*  197 */       this.cbLock.check();
/*      */ 
/*  199 */       super.setOutput(paramObject);
/*  200 */       resetInternalState();
/*  201 */       this.ios = ((ImageOutputStream)paramObject);
/*      */ 
/*  203 */       setDest(this.structPointer);
/*      */     } finally {
/*  205 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ImageWriteParam getDefaultWriteParam() {
/*  210 */     return new JPEGImageWriteParam(null);
/*      */   }
/*      */ 
/*      */   public IIOMetadata getDefaultStreamMetadata(ImageWriteParam paramImageWriteParam) {
/*  214 */     setThreadLock();
/*      */     try {
/*  216 */       return new JPEGMetadata(paramImageWriteParam, this);
/*      */     } finally {
/*  218 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  225 */     setThreadLock();
/*      */     try {
/*  227 */       return new JPEGMetadata(paramImageTypeSpecifier, paramImageWriteParam, this);
/*      */     } finally {
/*  229 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public IIOMetadata convertStreamMetadata(IIOMetadata paramIIOMetadata, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  239 */     if ((paramIIOMetadata instanceof JPEGMetadata)) {
/*  240 */       JPEGMetadata localJPEGMetadata = (JPEGMetadata)paramIIOMetadata;
/*  241 */       if (localJPEGMetadata.isStream) {
/*  242 */         return paramIIOMetadata;
/*      */       }
/*      */     }
/*  245 */     return null;
/*      */   }
/*      */ 
/*      */   public IIOMetadata convertImageMetadata(IIOMetadata paramIIOMetadata, ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  252 */     setThreadLock();
/*      */     try {
/*  254 */       return convertImageMetadataOnThread(paramIIOMetadata, paramImageTypeSpecifier, paramImageWriteParam);
/*      */     } finally {
/*  256 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private IIOMetadata convertImageMetadataOnThread(IIOMetadata paramIIOMetadata, ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*      */     Object localObject;
/*  265 */     if ((paramIIOMetadata instanceof JPEGMetadata)) {
/*  266 */       localObject = (JPEGMetadata)paramIIOMetadata;
/*  267 */       if (!((JPEGMetadata)localObject).isStream) {
/*  268 */         return paramIIOMetadata;
/*      */       }
/*      */ 
/*  272 */       return null;
/*      */     }
/*      */ 
/*  277 */     if (paramIIOMetadata.isStandardMetadataFormatSupported()) {
/*  278 */       localObject = "javax_imageio_1.0";
/*      */ 
/*  280 */       Node localNode = paramIIOMetadata.getAsTree((String)localObject);
/*  281 */       if (localNode != null) {
/*  282 */         JPEGMetadata localJPEGMetadata = new JPEGMetadata(paramImageTypeSpecifier, paramImageWriteParam, this);
/*      */         try
/*      */         {
/*  286 */           localJPEGMetadata.setFromTree((String)localObject, localNode);
/*      */         }
/*      */         catch (IIOInvalidTreeException localIIOInvalidTreeException)
/*      */         {
/*  290 */           return null;
/*      */         }
/*      */ 
/*  293 */         return localJPEGMetadata;
/*      */       }
/*      */     }
/*  296 */     return null;
/*      */   }
/*      */ 
/*      */   public int getNumThumbnailsSupported(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam, IIOMetadata paramIIOMetadata1, IIOMetadata paramIIOMetadata2)
/*      */   {
/*  303 */     if (jfifOK(paramImageTypeSpecifier, paramImageWriteParam, paramIIOMetadata1, paramIIOMetadata2)) {
/*  304 */       return 2147483647;
/*      */     }
/*  306 */     return 0;
/*      */   }
/*      */ 
/*      */   public Dimension[] getPreferredThumbnailSizes(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam, IIOMetadata paramIIOMetadata1, IIOMetadata paramIIOMetadata2)
/*      */   {
/*  316 */     if (jfifOK(paramImageTypeSpecifier, paramImageWriteParam, paramIIOMetadata1, paramIIOMetadata2)) {
/*  317 */       return (Dimension[])preferredThumbSizes.clone();
/*      */     }
/*  319 */     return null;
/*      */   }
/*      */ 
/*      */   private boolean jfifOK(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam, IIOMetadata paramIIOMetadata1, IIOMetadata paramIIOMetadata2)
/*      */   {
/*  327 */     if ((paramImageTypeSpecifier != null) && (!JPEG.isJFIFcompliant(paramImageTypeSpecifier, true)))
/*      */     {
/*  329 */       return false;
/*      */     }
/*  331 */     if (paramIIOMetadata2 != null) {
/*  332 */       JPEGMetadata localJPEGMetadata = null;
/*  333 */       if ((paramIIOMetadata2 instanceof JPEGMetadata))
/*  334 */         localJPEGMetadata = (JPEGMetadata)paramIIOMetadata2;
/*      */       else {
/*  336 */         localJPEGMetadata = (JPEGMetadata)convertImageMetadata(paramIIOMetadata2, paramImageTypeSpecifier, paramImageWriteParam);
/*      */       }
/*      */ 
/*  341 */       if (localJPEGMetadata.findMarkerSegment(JFIFMarkerSegment.class, true) == null)
/*      */       {
/*  343 */         return false;
/*      */       }
/*      */     }
/*  346 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean canWriteRasters() {
/*  350 */     return true;
/*      */   }
/*      */ 
/*      */   public void write(IIOMetadata paramIIOMetadata, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/*  356 */     setThreadLock();
/*      */     try {
/*  358 */       this.cbLock.check();
/*      */ 
/*  360 */       writeOnThread(paramIIOMetadata, paramIIOImage, paramImageWriteParam);
/*      */     } finally {
/*  362 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeOnThread(IIOMetadata paramIIOMetadata, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/*  370 */     if (this.ios == null) {
/*  371 */       throw new IllegalStateException("Output has not been set!");
/*      */     }
/*      */ 
/*  374 */     if (paramIIOImage == null) {
/*  375 */       throw new IllegalArgumentException("image is null!");
/*      */     }
/*      */ 
/*  379 */     if (paramIIOMetadata != null) {
/*  380 */       warningOccurred(1);
/*      */     }
/*      */ 
/*  384 */     boolean bool1 = paramIIOImage.hasRaster();
/*      */ 
/*  386 */     RenderedImage localRenderedImage = null;
/*  387 */     if (bool1) {
/*  388 */       this.srcRas = paramIIOImage.getRaster();
/*      */     } else {
/*  390 */       localRenderedImage = paramIIOImage.getRenderedImage();
/*  391 */       if ((localRenderedImage instanceof BufferedImage))
/*      */       {
/*  393 */         this.srcRas = ((BufferedImage)localRenderedImage).getRaster();
/*  394 */       } else if ((localRenderedImage.getNumXTiles() == 1) && (localRenderedImage.getNumYTiles() == 1))
/*      */       {
/*  398 */         this.srcRas = localRenderedImage.getTile(localRenderedImage.getMinTileX(), localRenderedImage.getMinTileY());
/*      */ 
/*  403 */         if ((this.srcRas.getWidth() != localRenderedImage.getWidth()) || (this.srcRas.getHeight() != localRenderedImage.getHeight()))
/*      */         {
/*  406 */           this.srcRas = this.srcRas.createChild(this.srcRas.getMinX(), this.srcRas.getMinY(), localRenderedImage.getWidth(), localRenderedImage.getHeight(), this.srcRas.getMinX(), this.srcRas.getMinY(), null);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  416 */         this.srcRas = localRenderedImage.getData();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  423 */     int i = this.srcRas.getNumBands();
/*  424 */     this.indexed = false;
/*  425 */     this.indexCM = null;
/*  426 */     ColorModel localColorModel = null;
/*  427 */     ColorSpace localColorSpace = null;
/*  428 */     this.isAlphaPremultiplied = false;
/*  429 */     this.srcCM = null;
/*  430 */     if (!bool1) {
/*  431 */       localColorModel = localRenderedImage.getColorModel();
/*  432 */       if (localColorModel != null) {
/*  433 */         localColorSpace = localColorModel.getColorSpace();
/*  434 */         if ((localColorModel instanceof IndexColorModel)) {
/*  435 */           this.indexed = true;
/*  436 */           this.indexCM = ((IndexColorModel)localColorModel);
/*  437 */           i = localColorModel.getNumComponents();
/*      */         }
/*  439 */         if (localColorModel.isAlphaPremultiplied()) {
/*  440 */           this.isAlphaPremultiplied = true;
/*  441 */           this.srcCM = localColorModel;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  446 */     this.srcBands = JPEG.bandOffsets[(i - 1)];
/*  447 */     int j = i;
/*      */ 
/*  450 */     if (paramImageWriteParam != null) {
/*  451 */       int[] arrayOfInt1 = paramImageWriteParam.getSourceBands();
/*  452 */       if (arrayOfInt1 != null) {
/*  453 */         if (this.indexed) {
/*  454 */           warningOccurred(8);
/*      */         } else {
/*  456 */           this.srcBands = arrayOfInt1;
/*  457 */           j = this.srcBands.length;
/*  458 */           if (j > i) {
/*  459 */             throw new IIOException("ImageWriteParam specifies too many source bands");
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  466 */     boolean bool2 = j != i;
/*  467 */     boolean bool3 = (!bool1) && (!bool2);
/*      */ 
/*  469 */     Object localObject1 = null;
/*      */     int[] arrayOfInt2;
/*  470 */     if (!this.indexed) {
/*  471 */       localObject1 = this.srcRas.getSampleModel().getSampleSize();
/*      */ 
/*  473 */       if (bool2) {
/*  474 */         arrayOfInt2 = new int[j];
/*  475 */         for (m = 0; m < j; m++) {
/*  476 */           arrayOfInt2[m] = localObject1[this.srcBands[m]];
/*      */         }
/*  478 */         localObject1 = arrayOfInt2;
/*      */       }
/*      */     } else {
/*  481 */       arrayOfInt2 = this.srcRas.getSampleModel().getSampleSize();
/*  482 */       localObject1 = new int[i];
/*  483 */       for (m = 0; m < i; m++) {
/*  484 */         localObject1[m] = arrayOfInt2[0];
/*      */       }
/*      */     }
/*      */ 
/*  488 */     for (int k = 0; k < localObject1.length; k++)
/*      */     {
/*  493 */       if ((localObject1[k] <= 0) || (localObject1[k] > 8)) {
/*  494 */         throw new IIOException("Illegal band size: should be 0 < size <= 8");
/*      */       }
/*      */ 
/*  501 */       if (this.indexed) {
/*  502 */         localObject1[k] = 8;
/*      */       }
/*      */     }
/*      */ 
/*  506 */     if (this.debug) {
/*  507 */       System.out.println("numSrcBands is " + i);
/*  508 */       System.out.println("numBandsUsed is " + j);
/*  509 */       System.out.println("usingBandSubset is " + bool2);
/*  510 */       System.out.println("fullImage is " + bool3);
/*  511 */       System.out.print("Band sizes:");
/*  512 */       for (k = 0; k < localObject1.length; k++) {
/*  513 */         System.out.print(" " + localObject1[k]);
/*      */       }
/*  515 */       System.out.println();
/*      */     }
/*      */ 
/*  519 */     ImageTypeSpecifier localImageTypeSpecifier1 = null;
/*  520 */     if (paramImageWriteParam != null) {
/*  521 */       localImageTypeSpecifier1 = paramImageWriteParam.getDestinationType();
/*      */ 
/*  523 */       if ((bool3) && (localImageTypeSpecifier1 != null)) {
/*  524 */         warningOccurred(0);
/*  525 */         localImageTypeSpecifier1 = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  531 */     this.sourceXOffset = this.srcRas.getMinX();
/*  532 */     this.sourceYOffset = this.srcRas.getMinY();
/*  533 */     int m = this.srcRas.getWidth();
/*  534 */     int n = this.srcRas.getHeight();
/*  535 */     this.sourceWidth = m;
/*  536 */     this.sourceHeight = n;
/*  537 */     int i1 = 1;
/*  538 */     int i2 = 1;
/*  539 */     int i3 = 0;
/*  540 */     int i4 = 0;
/*  541 */     JPEGQTable[] arrayOfJPEGQTable = null;
/*  542 */     JPEGHuffmanTable[] arrayOfJPEGHuffmanTable1 = null;
/*  543 */     JPEGHuffmanTable[] arrayOfJPEGHuffmanTable2 = null;
/*  544 */     boolean bool4 = false;
/*  545 */     JPEGImageWriteParam localJPEGImageWriteParam = null;
/*  546 */     int i5 = 0;
/*      */ 
/*  548 */     if (paramImageWriteParam != null)
/*      */     {
/*  550 */       localObject2 = paramImageWriteParam.getSourceRegion();
/*  551 */       if (localObject2 != null) {
/*  552 */         Rectangle localRectangle = new Rectangle(this.sourceXOffset, this.sourceYOffset, this.sourceWidth, this.sourceHeight);
/*      */ 
/*  556 */         localObject2 = ((Rectangle)localObject2).intersection(localRectangle);
/*  557 */         this.sourceXOffset = ((Rectangle)localObject2).x;
/*  558 */         this.sourceYOffset = ((Rectangle)localObject2).y;
/*  559 */         this.sourceWidth = ((Rectangle)localObject2).width;
/*  560 */         this.sourceHeight = ((Rectangle)localObject2).height;
/*      */       }
/*      */ 
/*  563 */       if (this.sourceWidth + this.sourceXOffset > m) {
/*  564 */         this.sourceWidth = (m - this.sourceXOffset);
/*      */       }
/*  566 */       if (this.sourceHeight + this.sourceYOffset > n) {
/*  567 */         this.sourceHeight = (n - this.sourceYOffset);
/*      */       }
/*      */ 
/*  570 */       i1 = paramImageWriteParam.getSourceXSubsampling();
/*  571 */       i2 = paramImageWriteParam.getSourceYSubsampling();
/*  572 */       i3 = paramImageWriteParam.getSubsamplingXOffset();
/*  573 */       i4 = paramImageWriteParam.getSubsamplingYOffset();
/*      */ 
/*  575 */       switch (paramImageWriteParam.getCompressionMode()) {
/*      */       case 0:
/*  577 */         throw new IIOException("JPEG compression cannot be disabled");
/*      */       case 2:
/*  579 */         float f = paramImageWriteParam.getCompressionQuality();
/*  580 */         f = JPEG.convertToLinearQuality(f);
/*  581 */         arrayOfJPEGQTable = new JPEGQTable[2];
/*  582 */         arrayOfJPEGQTable[0] = JPEGQTable.K1Luminance.getScaledInstance(f, true);
/*      */ 
/*  584 */         arrayOfJPEGQTable[1] = JPEGQTable.K2Chrominance.getScaledInstance(f, true);
/*      */ 
/*  586 */         break;
/*      */       case 1:
/*  588 */         arrayOfJPEGQTable = new JPEGQTable[2];
/*  589 */         arrayOfJPEGQTable[0] = JPEGQTable.K1Div2Luminance;
/*  590 */         arrayOfJPEGQTable[1] = JPEGQTable.K2Div2Chrominance;
/*      */       }
/*      */ 
/*  595 */       i5 = paramImageWriteParam.getProgressiveMode();
/*      */ 
/*  597 */       if ((paramImageWriteParam instanceof JPEGImageWriteParam)) {
/*  598 */         localJPEGImageWriteParam = (JPEGImageWriteParam)paramImageWriteParam;
/*  599 */         bool4 = localJPEGImageWriteParam.getOptimizeHuffmanTables();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  604 */     Object localObject2 = paramIIOImage.getMetadata();
/*  605 */     if (localObject2 != null) {
/*  606 */       if ((localObject2 instanceof JPEGMetadata)) {
/*  607 */         this.metadata = ((JPEGMetadata)localObject2);
/*  608 */         if (this.debug) {
/*  609 */           System.out.println("We have metadata, and it's JPEG metadata");
/*      */         }
/*      */ 
/*      */       }
/*  613 */       else if (!bool1) {
/*  614 */         ImageTypeSpecifier localImageTypeSpecifier2 = localImageTypeSpecifier1;
/*  615 */         if (localImageTypeSpecifier2 == null) {
/*  616 */           localImageTypeSpecifier2 = new ImageTypeSpecifier(localRenderedImage);
/*      */         }
/*  618 */         this.metadata = ((JPEGMetadata)convertImageMetadata((IIOMetadata)localObject2, localImageTypeSpecifier2, paramImageWriteParam));
/*      */       }
/*      */       else
/*      */       {
/*  622 */         warningOccurred(7);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  629 */     this.ignoreJFIF = false;
/*  630 */     this.ignoreAdobe = false;
/*  631 */     this.newAdobeTransform = -1;
/*  632 */     this.writeDefaultJFIF = false;
/*  633 */     this.writeAdobe = false;
/*      */ 
/*  636 */     int i6 = 0;
/*  637 */     int i7 = 0;
/*      */ 
/*  639 */     JFIFMarkerSegment localJFIFMarkerSegment = null;
/*  640 */     AdobeMarkerSegment localAdobeMarkerSegment = null;
/*  641 */     SOFMarkerSegment localSOFMarkerSegment = null;
/*      */ 
/*  643 */     if (this.metadata != null) {
/*  644 */       localJFIFMarkerSegment = (JFIFMarkerSegment)this.metadata.findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/*  646 */       localAdobeMarkerSegment = (AdobeMarkerSegment)this.metadata.findMarkerSegment(AdobeMarkerSegment.class, true);
/*      */ 
/*  648 */       localSOFMarkerSegment = (SOFMarkerSegment)this.metadata.findMarkerSegment(SOFMarkerSegment.class, true);
/*      */     }
/*      */ 
/*  652 */     this.iccProfile = null;
/*  653 */     this.convertTosRGB = false;
/*  654 */     this.converted = null;
/*      */ 
/*  656 */     if (localImageTypeSpecifier1 != null) {
/*  657 */       if (j != localImageTypeSpecifier1.getNumBands()) {
/*  658 */         throw new IIOException("Number of source bands != number of destination bands");
/*      */       }
/*      */ 
/*  661 */       localColorSpace = localImageTypeSpecifier1.getColorModel().getColorSpace();
/*      */ 
/*  663 */       if (this.metadata != null) {
/*  664 */         checkSOFBands(localSOFMarkerSegment, j);
/*      */ 
/*  666 */         checkJFIF(localJFIFMarkerSegment, localImageTypeSpecifier1, false);
/*      */ 
/*  668 */         if ((localJFIFMarkerSegment != null) && (!this.ignoreJFIF) && 
/*  669 */           (JPEG.isNonStandardICC(localColorSpace))) {
/*  670 */           this.iccProfile = ((ICC_ColorSpace)localColorSpace).getProfile();
/*      */         }
/*      */ 
/*  673 */         checkAdobe(localAdobeMarkerSegment, localImageTypeSpecifier1, false);
/*      */       }
/*      */       else
/*      */       {
/*  677 */         if (JPEG.isJFIFcompliant(localImageTypeSpecifier1, false)) {
/*  678 */           this.writeDefaultJFIF = true;
/*      */ 
/*  680 */           if (JPEG.isNonStandardICC(localColorSpace))
/*  681 */             this.iccProfile = ((ICC_ColorSpace)localColorSpace).getProfile();
/*      */         }
/*      */         else {
/*  684 */           int i8 = JPEG.transformForType(localImageTypeSpecifier1, false);
/*  685 */           if (i8 != -1) {
/*  686 */             this.writeAdobe = true;
/*  687 */             this.newAdobeTransform = i8;
/*      */           }
/*      */         }
/*      */ 
/*  691 */         this.metadata = new JPEGMetadata(localImageTypeSpecifier1, null, this);
/*      */       }
/*  693 */       i6 = getSrcCSType(localImageTypeSpecifier1);
/*  694 */       i7 = getDefaultDestCSType(localImageTypeSpecifier1);
/*      */     }
/*  696 */     else if (this.metadata == null) {
/*  697 */       if (bool3)
/*      */       {
/*  699 */         this.metadata = new JPEGMetadata(new ImageTypeSpecifier(localRenderedImage), paramImageWriteParam, this);
/*      */ 
/*  701 */         if (this.metadata.findMarkerSegment(JFIFMarkerSegment.class, true) != null)
/*      */         {
/*  703 */           localColorSpace = localRenderedImage.getColorModel().getColorSpace();
/*  704 */           if (JPEG.isNonStandardICC(localColorSpace)) {
/*  705 */             this.iccProfile = ((ICC_ColorSpace)localColorSpace).getProfile();
/*      */           }
/*      */         }
/*      */ 
/*  709 */         i6 = getSrcCSType(localRenderedImage);
/*  710 */         i7 = getDefaultDestCSType(localRenderedImage);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  715 */       checkSOFBands(localSOFMarkerSegment, j);
/*  716 */       if (bool3)
/*      */       {
/*  719 */         ImageTypeSpecifier localImageTypeSpecifier3 = new ImageTypeSpecifier(localRenderedImage);
/*      */ 
/*  722 */         i6 = getSrcCSType(localRenderedImage);
/*      */ 
/*  724 */         if (localColorModel != null) {
/*  725 */           boolean bool5 = localColorModel.hasAlpha();
/*  726 */           switch (localColorSpace.getType()) {
/*      */           case 6:
/*  728 */             if (!bool5) {
/*  729 */               i7 = 1;
/*      */             }
/*  731 */             else if (localJFIFMarkerSegment != null) {
/*  732 */               this.ignoreJFIF = true;
/*  733 */               warningOccurred(5);
/*      */             }
/*      */ 
/*  738 */             if ((localAdobeMarkerSegment != null) && (localAdobeMarkerSegment.transform != 0))
/*      */             {
/*  740 */               this.newAdobeTransform = 0;
/*  741 */               warningOccurred(6); } break;
/*      */           case 5:
/*  746 */             if (!bool5) {
/*  747 */               if (localJFIFMarkerSegment != null) {
/*  748 */                 i7 = 3;
/*  749 */                 if ((JPEG.isNonStandardICC(localColorSpace)) || (((localColorSpace instanceof ICC_ColorSpace)) && (localJFIFMarkerSegment.iccSegment != null)))
/*      */                 {
/*  752 */                   this.iccProfile = ((ICC_ColorSpace)localColorSpace).getProfile();
/*      */                 }
/*      */               }
/*  755 */               else if (localAdobeMarkerSegment != null) {
/*  756 */                 switch (localAdobeMarkerSegment.transform) {
/*      */                 case 0:
/*  758 */                   i7 = 2;
/*  759 */                   break;
/*      */                 case 1:
/*  761 */                   i7 = 3;
/*  762 */                   break;
/*      */                 default:
/*  764 */                   warningOccurred(6);
/*      */ 
/*  766 */                   this.newAdobeTransform = 0;
/*  767 */                   i7 = 2;
/*  768 */                   break;
/*      */                 }
/*      */               }
/*      */               else {
/*  772 */                 i10 = localSOFMarkerSegment.getIDencodedCSType();
/*      */ 
/*  775 */                 if (i10 != 0) {
/*  776 */                   i7 = i10;
/*      */                 } else {
/*  778 */                   bool6 = isSubsampled(localSOFMarkerSegment.componentSpecs);
/*      */ 
/*  780 */                   if (bool6)
/*  781 */                     i7 = 3;
/*      */                   else
/*  783 */                     i7 = 2;
/*      */                 }
/*      */               }
/*      */             }
/*      */             else {
/*  788 */               if (localJFIFMarkerSegment != null) {
/*  789 */                 this.ignoreJFIF = true;
/*  790 */                 warningOccurred(5);
/*      */               }
/*      */ 
/*  793 */               if (localAdobeMarkerSegment != null) {
/*  794 */                 if (localAdobeMarkerSegment.transform != 0)
/*      */                 {
/*  796 */                   this.newAdobeTransform = 0;
/*  797 */                   warningOccurred(6);
/*      */                 }
/*      */ 
/*  800 */                 i7 = 6;
/*      */               }
/*      */               else {
/*  803 */                 i10 = localSOFMarkerSegment.getIDencodedCSType();
/*      */ 
/*  806 */                 if (i10 != 0) {
/*  807 */                   i7 = i10;
/*      */                 } else {
/*  809 */                   bool6 = isSubsampled(localSOFMarkerSegment.componentSpecs);
/*      */ 
/*  811 */                   i7 = bool6 ? 7 : 6;
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*  816 */             break;
/*      */           case 13:
/*  818 */             if (localColorSpace == JPEG.JCS.getYCC()) {
/*  819 */               if (!bool5) {
/*  820 */                 if (localJFIFMarkerSegment != null) {
/*  821 */                   this.convertTosRGB = true;
/*  822 */                   this.convertOp = new ColorConvertOp(localColorSpace, JPEG.JCS.sRGB, null);
/*      */ 
/*  826 */                   i7 = 3;
/*  827 */                 } else if (localAdobeMarkerSegment != null) {
/*  828 */                   if (localAdobeMarkerSegment.transform != 1)
/*      */                   {
/*  830 */                     this.newAdobeTransform = 1;
/*  831 */                     warningOccurred(6);
/*      */                   }
/*      */ 
/*  834 */                   i7 = 5;
/*      */                 } else {
/*  836 */                   i7 = 5;
/*      */                 }
/*      */               } else {
/*  839 */                 if (localJFIFMarkerSegment != null) {
/*  840 */                   this.ignoreJFIF = true;
/*  841 */                   warningOccurred(5);
/*      */                 }
/*  843 */                 else if ((localAdobeMarkerSegment != null) && 
/*  844 */                   (localAdobeMarkerSegment.transform != 0))
/*      */                 {
/*  846 */                   this.newAdobeTransform = 0;
/*      */ 
/*  848 */                   warningOccurred(6);
/*      */                 }
/*      */ 
/*  852 */                 i7 = 10;
/*      */               }
/*      */             }
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  861 */     int i9 = 0;
/*  862 */     int[] arrayOfInt3 = null;
/*      */ 
/*  864 */     if (this.metadata != null) {
/*  865 */       if (localSOFMarkerSegment == null) {
/*  866 */         localSOFMarkerSegment = (SOFMarkerSegment)this.metadata.findMarkerSegment(SOFMarkerSegment.class, true);
/*      */       }
/*      */ 
/*  869 */       if ((localSOFMarkerSegment != null) && (localSOFMarkerSegment.tag == 194)) {
/*  870 */         i9 = 1;
/*  871 */         if (i5 == 3)
/*  872 */           arrayOfInt3 = collectScans(this.metadata, localSOFMarkerSegment);
/*      */         else {
/*  874 */           this.numScans = 0;
/*      */         }
/*      */       }
/*  877 */       if (localJFIFMarkerSegment == null) {
/*  878 */         localJFIFMarkerSegment = (JFIFMarkerSegment)this.metadata.findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  883 */     this.thumbnails = paramIIOImage.getThumbnails();
/*  884 */     int i10 = paramIIOImage.getNumThumbnails();
/*  885 */     this.forceJFIF = false;
/*      */ 
/*  889 */     if (!this.writeDefaultJFIF)
/*      */     {
/*  891 */       if (this.metadata == null) {
/*  892 */         this.thumbnails = null;
/*  893 */         if (i10 != 0) {
/*  894 */           warningOccurred(10);
/*      */         }
/*      */ 
/*      */       }
/*  900 */       else if (!bool3) {
/*  901 */         if (localJFIFMarkerSegment == null) {
/*  902 */           this.thumbnails = null;
/*  903 */           if (i10 != 0) {
/*  904 */             warningOccurred(10);
/*      */           }
/*      */         }
/*      */       }
/*  908 */       else if (localJFIFMarkerSegment == null)
/*      */       {
/*  910 */         if ((i7 == 1) || (i7 == 3))
/*      */         {
/*  912 */           if (i10 != 0) {
/*  913 */             this.forceJFIF = true;
/*  914 */             warningOccurred(11);
/*      */           }
/*      */         } else {
/*  917 */           this.thumbnails = null;
/*  918 */           if (i10 != 0) {
/*  919 */             warningOccurred(10);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  929 */     boolean bool6 = (this.metadata != null) || (this.writeDefaultJFIF) || (this.writeAdobe);
/*      */ 
/*  935 */     boolean bool7 = true;
/*  936 */     boolean bool8 = true;
/*      */ 
/*  939 */     DQTMarkerSegment localDQTMarkerSegment = null;
/*  940 */     DHTMarkerSegment localDHTMarkerSegment = null;
/*      */ 
/*  942 */     int i11 = 0;
/*      */ 
/*  944 */     if (this.metadata != null) {
/*  945 */       localDQTMarkerSegment = (DQTMarkerSegment)this.metadata.findMarkerSegment(DQTMarkerSegment.class, true);
/*      */ 
/*  947 */       localDHTMarkerSegment = (DHTMarkerSegment)this.metadata.findMarkerSegment(DHTMarkerSegment.class, true);
/*      */ 
/*  949 */       localObject3 = (DRIMarkerSegment)this.metadata.findMarkerSegment(DRIMarkerSegment.class, true);
/*      */ 
/*  952 */       if (localObject3 != null) {
/*  953 */         i11 = ((DRIMarkerSegment)localObject3).restartInterval;
/*      */       }
/*      */ 
/*  956 */       if (localDQTMarkerSegment == null) {
/*  957 */         bool7 = false;
/*      */       }
/*  959 */       if (localDHTMarkerSegment == null) {
/*  960 */         bool8 = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  966 */     if (arrayOfJPEGQTable == null) {
/*  967 */       if (localDQTMarkerSegment != null)
/*  968 */         arrayOfJPEGQTable = collectQTablesFromMetadata(this.metadata);
/*  969 */       else if (this.streamQTables != null)
/*  970 */         arrayOfJPEGQTable = this.streamQTables;
/*  971 */       else if ((localJPEGImageWriteParam != null) && (localJPEGImageWriteParam.areTablesSet()))
/*  972 */         arrayOfJPEGQTable = localJPEGImageWriteParam.getQTables();
/*      */       else {
/*  974 */         arrayOfJPEGQTable = JPEG.getDefaultQTables();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  980 */     if (!bool4)
/*      */     {
/*  982 */       if ((localDHTMarkerSegment != null) && (i9 == 0)) {
/*  983 */         arrayOfJPEGHuffmanTable1 = collectHTablesFromMetadata(this.metadata, true);
/*  984 */         arrayOfJPEGHuffmanTable2 = collectHTablesFromMetadata(this.metadata, false);
/*  985 */       } else if (this.streamDCHuffmanTables != null) {
/*  986 */         arrayOfJPEGHuffmanTable1 = this.streamDCHuffmanTables;
/*  987 */         arrayOfJPEGHuffmanTable2 = this.streamACHuffmanTables;
/*  988 */       } else if ((localJPEGImageWriteParam != null) && (localJPEGImageWriteParam.areTablesSet())) {
/*  989 */         arrayOfJPEGHuffmanTable1 = localJPEGImageWriteParam.getDCHuffmanTables();
/*  990 */         arrayOfJPEGHuffmanTable2 = localJPEGImageWriteParam.getACHuffmanTables();
/*      */       } else {
/*  992 */         arrayOfJPEGHuffmanTable1 = JPEG.getDefaultHuffmanTables(true);
/*  993 */         arrayOfJPEGHuffmanTable2 = JPEG.getDefaultHuffmanTables(false);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  998 */     Object localObject3 = new int[j];
/*  999 */     int[] arrayOfInt4 = new int[j];
/* 1000 */     int[] arrayOfInt5 = new int[j];
/* 1001 */     int[] arrayOfInt6 = new int[j];
/* 1002 */     for (int i12 = 0; i12 < j; i12++) {
/* 1003 */       localObject3[i12] = (i12 + 1);
/* 1004 */       arrayOfInt4[i12] = 1;
/* 1005 */       arrayOfInt5[i12] = 1;
/* 1006 */       arrayOfInt6[i12] = 0;
/*      */     }
/*      */ 
/* 1010 */     if (localSOFMarkerSegment != null) {
/* 1011 */       for (i12 = 0; i12 < j; i12++) {
/* 1012 */         if (!this.forceJFIF) {
/* 1013 */           localObject3[i12] = localSOFMarkerSegment.componentSpecs[i12].componentId;
/*      */         }
/* 1015 */         arrayOfInt4[i12] = localSOFMarkerSegment.componentSpecs[i12].HsamplingFactor;
/* 1016 */         arrayOfInt5[i12] = localSOFMarkerSegment.componentSpecs[i12].VsamplingFactor;
/* 1017 */         arrayOfInt6[i12] = localSOFMarkerSegment.componentSpecs[i12].QtableSelector;
/*      */       }
/*      */     }
/*      */ 
/* 1021 */     this.sourceXOffset += i3;
/* 1022 */     this.sourceWidth -= i3;
/* 1023 */     this.sourceYOffset += i4;
/* 1024 */     this.sourceHeight -= i4;
/*      */ 
/* 1026 */     i12 = (this.sourceWidth + i1 - 1) / i1;
/* 1027 */     int i13 = (this.sourceHeight + i2 - 1) / i2;
/*      */ 
/* 1030 */     int i14 = this.sourceWidth * j;
/*      */ 
/* 1032 */     DataBufferByte localDataBufferByte = new DataBufferByte(i14);
/*      */ 
/* 1035 */     int[] arrayOfInt7 = JPEG.bandOffsets[(j - 1)];
/*      */ 
/* 1037 */     this.raster = Raster.createInterleavedRaster(localDataBufferByte, this.sourceWidth, 1, i14, j, arrayOfInt7, null);
/*      */ 
/* 1046 */     processImageStarted(this.currentImage);
/*      */ 
/* 1048 */     boolean bool9 = false;
/*      */ 
/* 1050 */     if (this.debug) {
/* 1051 */       System.out.println("inCsType: " + i6);
/* 1052 */       System.out.println("outCsType: " + i7);
/*      */     }
/*      */ 
/* 1058 */     bool9 = writeImage(this.structPointer, localDataBufferByte.getData(), i6, i7, j, (int[])localObject1, this.sourceWidth, i12, i13, i1, i2, arrayOfJPEGQTable, bool7, arrayOfJPEGHuffmanTable1, arrayOfJPEGHuffmanTable2, bool8, bool4, i5 != 0, this.numScans, arrayOfInt3, (int[])localObject3, arrayOfInt4, arrayOfInt5, arrayOfInt6, bool6, i11);
/*      */ 
/* 1083 */     this.cbLock.lock();
/*      */     try {
/* 1085 */       if (bool9)
/* 1086 */         processWriteAborted();
/*      */       else {
/* 1088 */         processImageComplete();
/*      */       }
/*      */ 
/* 1091 */       this.ios.flush();
/*      */     } finally {
/* 1093 */       this.cbLock.unlock();
/*      */     }
/* 1095 */     this.currentImage += 1;
/*      */   }
/*      */ 
/*      */   public void prepareWriteSequence(IIOMetadata paramIIOMetadata) throws IOException
/*      */   {
/* 1100 */     setThreadLock();
/*      */     try {
/* 1102 */       this.cbLock.check();
/*      */ 
/* 1104 */       prepareWriteSequenceOnThread(paramIIOMetadata);
/*      */     } finally {
/* 1106 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void prepareWriteSequenceOnThread(IIOMetadata paramIIOMetadata) throws IOException
/*      */   {
/* 1112 */     if (this.ios == null) {
/* 1113 */       throw new IllegalStateException("Output has not been set!");
/*      */     }
/*      */ 
/* 1125 */     if (paramIIOMetadata != null) {
/* 1126 */       if ((paramIIOMetadata instanceof JPEGMetadata))
/*      */       {
/* 1129 */         JPEGMetadata localJPEGMetadata = (JPEGMetadata)paramIIOMetadata;
/* 1130 */         if (!localJPEGMetadata.isStream) {
/* 1131 */           throw new IllegalArgumentException("Invalid stream metadata object.");
/*      */         }
/*      */ 
/* 1137 */         if (this.currentImage != 0) {
/* 1138 */           throw new IIOException("JPEG Stream metadata must precede all images");
/*      */         }
/*      */ 
/* 1141 */         if (this.sequencePrepared == true) {
/* 1142 */           throw new IIOException("Stream metadata already written!");
/*      */         }
/*      */ 
/* 1147 */         this.streamQTables = collectQTablesFromMetadata(localJPEGMetadata);
/* 1148 */         if (this.debug) {
/* 1149 */           System.out.println("after collecting from stream metadata, streamQTables.length is " + this.streamQTables.length);
/*      */         }
/*      */ 
/* 1153 */         if (this.streamQTables == null) {
/* 1154 */           this.streamQTables = JPEG.getDefaultQTables();
/*      */         }
/* 1156 */         this.streamDCHuffmanTables = collectHTablesFromMetadata(localJPEGMetadata, true);
/*      */ 
/* 1158 */         if (this.streamDCHuffmanTables == null) {
/* 1159 */           this.streamDCHuffmanTables = JPEG.getDefaultHuffmanTables(true);
/*      */         }
/* 1161 */         this.streamACHuffmanTables = collectHTablesFromMetadata(localJPEGMetadata, false);
/*      */ 
/* 1163 */         if (this.streamACHuffmanTables == null) {
/* 1164 */           this.streamACHuffmanTables = JPEG.getDefaultHuffmanTables(false);
/*      */         }
/*      */ 
/* 1168 */         writeTables(this.structPointer, this.streamQTables, this.streamDCHuffmanTables, this.streamACHuffmanTables);
/*      */       }
/*      */       else
/*      */       {
/* 1173 */         throw new IIOException("Stream metadata must be JPEG metadata");
/*      */       }
/*      */     }
/* 1176 */     this.sequencePrepared = true;
/*      */   }
/*      */ 
/*      */   public void writeToSequence(IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam) throws IOException
/*      */   {
/* 1181 */     setThreadLock();
/*      */     try {
/* 1183 */       this.cbLock.check();
/*      */ 
/* 1185 */       if (!this.sequencePrepared) {
/* 1186 */         throw new IllegalStateException("sequencePrepared not called!");
/*      */       }
/*      */ 
/* 1189 */       write(null, paramIIOImage, paramImageWriteParam);
/*      */     } finally {
/* 1191 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endWriteSequence() throws IOException {
/* 1196 */     setThreadLock();
/*      */     try {
/* 1198 */       this.cbLock.check();
/*      */ 
/* 1200 */       if (!this.sequencePrepared) {
/* 1201 */         throw new IllegalStateException("sequencePrepared not called!");
/*      */       }
/* 1203 */       this.sequencePrepared = false;
/*      */     } finally {
/* 1205 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void abort() {
/* 1210 */     setThreadLock();
/*      */     try
/*      */     {
/* 1216 */       super.abort();
/* 1217 */       abortWrite(this.structPointer);
/*      */     } finally {
/* 1219 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resetInternalState()
/*      */   {
/* 1225 */     resetWriter(this.structPointer);
/*      */ 
/* 1228 */     this.srcRas = null;
/* 1229 */     this.raster = null;
/* 1230 */     this.convertTosRGB = false;
/* 1231 */     this.currentImage = 0;
/* 1232 */     this.numScans = 0;
/* 1233 */     this.metadata = null;
/*      */   }
/*      */ 
/*      */   public void reset() {
/* 1237 */     setThreadLock();
/*      */     try {
/* 1239 */       this.cbLock.check();
/*      */ 
/* 1241 */       super.reset();
/*      */     } finally {
/* 1243 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void dispose() {
/* 1248 */     setThreadLock();
/*      */     try {
/* 1250 */       this.cbLock.check();
/*      */ 
/* 1252 */       if (this.structPointer != 0L) {
/* 1253 */         this.disposerRecord.dispose();
/* 1254 */         this.structPointer = 0L;
/*      */       }
/*      */     } finally {
/* 1257 */       clearThreadLock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void warningOccurred(int paramInt)
/*      */   {
/* 1271 */     this.cbLock.lock();
/*      */     try {
/* 1273 */       if ((paramInt < 0) || (paramInt > 15)) {
/* 1274 */         throw new InternalError("Invalid warning index");
/*      */       }
/* 1276 */       processWarningOccurred(this.currentImage, "com.sun.imageio.plugins.jpeg.JPEGImageWriterResources", Integer.toString(paramInt));
/*      */     }
/*      */     finally
/*      */     {
/* 1281 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void warningWithMessage(String paramString)
/*      */   {
/* 1299 */     this.cbLock.lock();
/*      */     try {
/* 1301 */       processWarningOccurred(this.currentImage, paramString);
/*      */     } finally {
/* 1303 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void thumbnailStarted(int paramInt) {
/* 1308 */     this.cbLock.lock();
/*      */     try {
/* 1310 */       processThumbnailStarted(this.currentImage, paramInt);
/*      */     } finally {
/* 1312 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void thumbnailProgress(float paramFloat)
/*      */   {
/* 1318 */     this.cbLock.lock();
/*      */     try {
/* 1320 */       processThumbnailProgress(paramFloat);
/*      */     } finally {
/* 1322 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   void thumbnailComplete()
/*      */   {
/* 1328 */     this.cbLock.lock();
/*      */     try {
/* 1330 */       processThumbnailComplete();
/*      */     } finally {
/* 1332 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkSOFBands(SOFMarkerSegment paramSOFMarkerSegment, int paramInt)
/*      */     throws IIOException
/*      */   {
/* 1345 */     if ((paramSOFMarkerSegment != null) && 
/* 1346 */       (paramSOFMarkerSegment.componentSpecs.length != paramInt))
/* 1347 */       throw new IIOException("Metadata components != number of destination bands");
/*      */   }
/*      */ 
/*      */   private void checkJFIF(JFIFMarkerSegment paramJFIFMarkerSegment, ImageTypeSpecifier paramImageTypeSpecifier, boolean paramBoolean)
/*      */   {
/* 1356 */     if ((paramJFIFMarkerSegment != null) && 
/* 1357 */       (!JPEG.isJFIFcompliant(paramImageTypeSpecifier, paramBoolean))) {
/* 1358 */       this.ignoreJFIF = true;
/* 1359 */       warningOccurred(paramBoolean ? 5 : 3);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkAdobe(AdobeMarkerSegment paramAdobeMarkerSegment, ImageTypeSpecifier paramImageTypeSpecifier, boolean paramBoolean)
/*      */   {
/* 1369 */     if (paramAdobeMarkerSegment != null) {
/* 1370 */       int i = JPEG.transformForType(paramImageTypeSpecifier, paramBoolean);
/* 1371 */       if (paramAdobeMarkerSegment.transform != i) {
/* 1372 */         warningOccurred(paramBoolean ? 6 : 4);
/*      */ 
/* 1375 */         if (i == -1)
/* 1376 */           this.ignoreAdobe = true;
/*      */         else
/* 1378 */           this.newAdobeTransform = i;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private int[] collectScans(JPEGMetadata paramJPEGMetadata, SOFMarkerSegment paramSOFMarkerSegment)
/*      */   {
/* 1392 */     ArrayList localArrayList = new ArrayList();
/* 1393 */     int i = 9;
/* 1394 */     int j = 4;
/* 1395 */     Object localObject = paramJPEGMetadata.markerSequence.iterator();
/* 1396 */     while (((Iterator)localObject).hasNext()) {
/* 1397 */       MarkerSegment localMarkerSegment = (MarkerSegment)((Iterator)localObject).next();
/* 1398 */       if ((localMarkerSegment instanceof SOSMarkerSegment)) {
/* 1399 */         localArrayList.add(localMarkerSegment);
/*      */       }
/*      */     }
/* 1402 */     localObject = null;
/* 1403 */     this.numScans = 0;
/* 1404 */     if (!localArrayList.isEmpty()) {
/* 1405 */       this.numScans = localArrayList.size();
/* 1406 */       localObject = new int[this.numScans * i];
/* 1407 */       int k = 0;
/* 1408 */       for (int m = 0; m < this.numScans; m++) {
/* 1409 */         SOSMarkerSegment localSOSMarkerSegment = (SOSMarkerSegment)localArrayList.get(m);
/* 1410 */         localObject[(k++)] = localSOSMarkerSegment.componentSpecs.length;
/* 1411 */         for (int n = 0; n < j; n++) {
/* 1412 */           if (n < localSOSMarkerSegment.componentSpecs.length) {
/* 1413 */             int i1 = localSOSMarkerSegment.componentSpecs[n].componentSelector;
/* 1414 */             for (int i2 = 0; i2 < paramSOFMarkerSegment.componentSpecs.length; i2++)
/* 1415 */               if (i1 == paramSOFMarkerSegment.componentSpecs[i2].componentId) {
/* 1416 */                 localObject[(k++)] = i2;
/* 1417 */                 break;
/*      */               }
/*      */           }
/*      */           else {
/* 1421 */             localObject[(k++)] = 0;
/*      */           }
/*      */         }
/* 1424 */         localObject[(k++)] = localSOSMarkerSegment.startSpectralSelection;
/* 1425 */         localObject[(k++)] = localSOSMarkerSegment.endSpectralSelection;
/* 1426 */         localObject[(k++)] = localSOSMarkerSegment.approxHigh;
/* 1427 */         localObject[(k++)] = localSOSMarkerSegment.approxLow;
/*      */       }
/*      */     }
/* 1430 */     return localObject;
/*      */   }
/*      */ 
/*      */   private JPEGQTable[] collectQTablesFromMetadata(JPEGMetadata paramJPEGMetadata)
/*      */   {
/* 1439 */     ArrayList localArrayList = new ArrayList();
/* 1440 */     Iterator localIterator = paramJPEGMetadata.markerSequence.iterator();
/* 1441 */     while (localIterator.hasNext()) {
/* 1442 */       localObject = (MarkerSegment)localIterator.next();
/* 1443 */       if ((localObject instanceof DQTMarkerSegment)) {
/* 1444 */         DQTMarkerSegment localDQTMarkerSegment = (DQTMarkerSegment)localObject;
/*      */ 
/* 1446 */         localArrayList.addAll(localDQTMarkerSegment.tables);
/*      */       }
/*      */     }
/* 1449 */     Object localObject = null;
/* 1450 */     if (localArrayList.size() != 0) {
/* 1451 */       localObject = new JPEGQTable[localArrayList.size()];
/* 1452 */       for (int i = 0; i < localObject.length; i++) {
/* 1453 */         localObject[i] = new JPEGQTable(((DQTMarkerSegment.Qtable)localArrayList.get(i)).data);
/*      */       }
/*      */     }
/*      */ 
/* 1457 */     return localObject;
/*      */   }
/*      */ 
/*      */   private JPEGHuffmanTable[] collectHTablesFromMetadata(JPEGMetadata paramJPEGMetadata, boolean paramBoolean)
/*      */     throws IIOException
/*      */   {
/* 1469 */     ArrayList localArrayList = new ArrayList();
/* 1470 */     Iterator localIterator = paramJPEGMetadata.markerSequence.iterator();
/*      */     Object localObject2;
/*      */     int i;
/* 1471 */     while (localIterator.hasNext()) {
/* 1472 */       localObject1 = (MarkerSegment)localIterator.next();
/* 1473 */       if ((localObject1 instanceof DHTMarkerSegment)) {
/* 1474 */         localObject2 = (DHTMarkerSegment)localObject1;
/*      */ 
/* 1476 */         for (i = 0; i < ((DHTMarkerSegment)localObject2).tables.size(); i++) {
/* 1477 */           DHTMarkerSegment.Htable localHtable = (DHTMarkerSegment.Htable)((DHTMarkerSegment)localObject2).tables.get(i);
/*      */ 
/* 1479 */           if (localHtable.tableClass == (paramBoolean ? 0 : 1)) {
/* 1480 */             localArrayList.add(localHtable);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1485 */     Object localObject1 = null;
/* 1486 */     if (localArrayList.size() != 0) {
/* 1487 */       localObject2 = new DHTMarkerSegment.Htable[localArrayList.size()];
/*      */ 
/* 1489 */       localArrayList.toArray((Object[])localObject2);
/* 1490 */       localObject1 = new JPEGHuffmanTable[localArrayList.size()];
/* 1491 */       for (i = 0; i < localObject1.length; i++) {
/* 1492 */         localObject1[i] = null;
/* 1493 */         for (int j = 0; j < localArrayList.size(); j++) {
/* 1494 */           if (localObject2[j].tableID == i) {
/* 1495 */             if (localObject1[i] != null) {
/* 1496 */               throw new IIOException("Metadata has duplicate Htables!");
/*      */             }
/* 1498 */             localObject1[i] = new JPEGHuffmanTable(localObject2[j].numCodes, localObject2[j].values);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1505 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private int getSrcCSType(ImageTypeSpecifier paramImageTypeSpecifier)
/*      */   {
/* 1513 */     return getSrcCSType(paramImageTypeSpecifier.getColorModel());
/*      */   }
/*      */ 
/*      */   private int getSrcCSType(RenderedImage paramRenderedImage) {
/* 1517 */     return getSrcCSType(paramRenderedImage.getColorModel());
/*      */   }
/*      */ 
/*      */   private int getSrcCSType(ColorModel paramColorModel) {
/* 1521 */     int i = 0;
/* 1522 */     if (paramColorModel != null) {
/* 1523 */       boolean bool = paramColorModel.hasAlpha();
/* 1524 */       ColorSpace localColorSpace = paramColorModel.getColorSpace();
/* 1525 */       switch (localColorSpace.getType()) {
/*      */       case 6:
/* 1527 */         i = 1;
/* 1528 */         break;
/*      */       case 5:
/* 1530 */         if (bool)
/* 1531 */           i = 6;
/*      */         else {
/* 1533 */           i = 2;
/*      */         }
/* 1535 */         break;
/*      */       case 3:
/* 1537 */         if (bool)
/* 1538 */           i = 7;
/*      */         else {
/* 1540 */           i = 3;
/*      */         }
/* 1542 */         break;
/*      */       case 13:
/* 1544 */         if (localColorSpace == JPEG.JCS.getYCC()) {
/* 1545 */           if (bool)
/* 1546 */             i = 10;
/*      */           else {
/* 1548 */             i = 5;
/*      */           }
/*      */         }
/*      */       case 9:
/* 1552 */         i = 4;
/*      */       case 4:
/*      */       case 7:
/*      */       case 8:
/*      */       case 10:
/*      */       case 11:
/* 1556 */       case 12: }  } return i;
/*      */   }
/*      */ 
/*      */   private int getDestCSType(ImageTypeSpecifier paramImageTypeSpecifier) {
/* 1560 */     ColorModel localColorModel = paramImageTypeSpecifier.getColorModel();
/* 1561 */     boolean bool = localColorModel.hasAlpha();
/* 1562 */     ColorSpace localColorSpace = localColorModel.getColorSpace();
/* 1563 */     int i = 0;
/* 1564 */     switch (localColorSpace.getType()) {
/*      */     case 6:
/* 1566 */       i = 1;
/* 1567 */       break;
/*      */     case 5:
/* 1569 */       if (bool)
/* 1570 */         i = 6;
/*      */       else {
/* 1572 */         i = 2;
/*      */       }
/* 1574 */       break;
/*      */     case 3:
/* 1576 */       if (bool)
/* 1577 */         i = 7;
/*      */       else {
/* 1579 */         i = 3;
/*      */       }
/* 1581 */       break;
/*      */     case 13:
/* 1583 */       if (localColorSpace == JPEG.JCS.getYCC()) {
/* 1584 */         if (bool)
/* 1585 */           i = 10;
/*      */         else {
/* 1587 */           i = 5;
/*      */         }
/*      */       }
/*      */     case 9:
/* 1591 */       i = 4;
/*      */     case 4:
/*      */     case 7:
/*      */     case 8:
/*      */     case 10:
/*      */     case 11:
/* 1594 */     case 12: } return i;
/*      */   }
/*      */ 
/*      */   private int getDefaultDestCSType(ImageTypeSpecifier paramImageTypeSpecifier) {
/* 1598 */     return getDefaultDestCSType(paramImageTypeSpecifier.getColorModel());
/*      */   }
/*      */ 
/*      */   private int getDefaultDestCSType(RenderedImage paramRenderedImage) {
/* 1602 */     return getDefaultDestCSType(paramRenderedImage.getColorModel());
/*      */   }
/*      */ 
/*      */   private int getDefaultDestCSType(ColorModel paramColorModel) {
/* 1606 */     int i = 0;
/* 1607 */     if (paramColorModel != null) {
/* 1608 */       boolean bool = paramColorModel.hasAlpha();
/* 1609 */       ColorSpace localColorSpace = paramColorModel.getColorSpace();
/* 1610 */       switch (localColorSpace.getType()) {
/*      */       case 6:
/* 1612 */         i = 1;
/* 1613 */         break;
/*      */       case 5:
/* 1615 */         if (bool)
/* 1616 */           i = 7;
/*      */         else {
/* 1618 */           i = 3;
/*      */         }
/* 1620 */         break;
/*      */       case 3:
/* 1622 */         if (bool)
/* 1623 */           i = 7;
/*      */         else {
/* 1625 */           i = 3;
/*      */         }
/* 1627 */         break;
/*      */       case 13:
/* 1629 */         if (localColorSpace == JPEG.JCS.getYCC()) {
/* 1630 */           if (bool)
/* 1631 */             i = 10;
/*      */           else {
/* 1633 */             i = 5;
/*      */           }
/*      */         }
/*      */       case 9:
/* 1637 */         i = 11;
/*      */       case 4:
/*      */       case 7:
/*      */       case 8:
/*      */       case 10:
/*      */       case 11:
/* 1641 */       case 12: }  } return i;
/*      */   }
/*      */ 
/*      */   private boolean isSubsampled(SOFMarkerSegment.ComponentSpec[] paramArrayOfComponentSpec) {
/* 1645 */     int i = paramArrayOfComponentSpec[0].HsamplingFactor;
/* 1646 */     int j = paramArrayOfComponentSpec[0].VsamplingFactor;
/* 1647 */     for (int k = 1; k < paramArrayOfComponentSpec.length; k++)
/* 1648 */       if ((paramArrayOfComponentSpec[k].HsamplingFactor != i) || (paramArrayOfComponentSpec[k].HsamplingFactor != i))
/*      */       {
/* 1650 */         return true;
/*      */       }
/* 1652 */     return false;
/*      */   }
/*      */ 
/*      */   private static native void initWriterIDs(Class paramClass1, Class paramClass2);
/*      */ 
/*      */   private native long initJPEGImageWriter();
/*      */ 
/*      */   private native void setDest(long paramLong);
/*      */ 
/*      */   private native boolean writeImage(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, JPEGQTable[] paramArrayOfJPEGQTable, boolean paramBoolean1, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable1, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable2, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt9, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, int[] paramArrayOfInt5, int[] paramArrayOfInt6, boolean paramBoolean5, int paramInt10);
/*      */ 
/*      */   private void writeMetadata()
/*      */     throws IOException
/*      */   {
/* 1704 */     if (this.metadata == null) {
/* 1705 */       if (this.writeDefaultJFIF) {
/* 1706 */         JFIFMarkerSegment.writeDefaultJFIF(this.ios, this.thumbnails, this.iccProfile, this);
/*      */       }
/*      */ 
/* 1711 */       if (this.writeAdobe)
/* 1712 */         AdobeMarkerSegment.writeAdobeSegment(this.ios, this.newAdobeTransform);
/*      */     }
/*      */     else {
/* 1715 */       this.metadata.writeToStream(this.ios, this.ignoreJFIF, this.forceJFIF, this.thumbnails, this.iccProfile, this.ignoreAdobe, this.newAdobeTransform, this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void writeTables(long paramLong, JPEGQTable[] paramArrayOfJPEGQTable, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable1, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable2);
/*      */ 
/*      */   private void grabPixels(int paramInt)
/*      */   {
/* 1743 */     Object localObject1 = null;
/*      */     Object localObject2;
/* 1744 */     if (this.indexed) {
/* 1745 */       localObject1 = this.srcRas.createChild(this.sourceXOffset, this.sourceYOffset + paramInt, this.sourceWidth, 1, 0, 0, new int[] { 0 });
/*      */ 
/* 1753 */       boolean bool = this.indexCM.getTransparency() != 1;
/*      */ 
/* 1755 */       localObject2 = this.indexCM.convertToIntDiscrete((Raster)localObject1, bool);
/*      */ 
/* 1757 */       localObject1 = ((BufferedImage)localObject2).getRaster();
/*      */     } else {
/* 1759 */       localObject1 = this.srcRas.createChild(this.sourceXOffset, this.sourceYOffset + paramInt, this.sourceWidth, 1, 0, 0, this.srcBands);
/*      */     }
/*      */ 
/* 1765 */     if (this.convertTosRGB) {
/* 1766 */       if (this.debug) {
/* 1767 */         System.out.println("Converting to sRGB");
/*      */       }
/*      */ 
/* 1772 */       this.converted = this.convertOp.filter((Raster)localObject1, this.converted);
/* 1773 */       localObject1 = this.converted;
/*      */     }
/* 1775 */     if (this.isAlphaPremultiplied) {
/* 1776 */       WritableRaster localWritableRaster = ((Raster)localObject1).createCompatibleWritableRaster();
/* 1777 */       localObject2 = null;
/* 1778 */       localObject2 = ((Raster)localObject1).getPixels(((Raster)localObject1).getMinX(), ((Raster)localObject1).getMinY(), ((Raster)localObject1).getWidth(), ((Raster)localObject1).getHeight(), (int[])localObject2);
/*      */ 
/* 1781 */       localWritableRaster.setPixels(((Raster)localObject1).getMinX(), ((Raster)localObject1).getMinY(), ((Raster)localObject1).getWidth(), ((Raster)localObject1).getHeight(), (int[])localObject2);
/*      */ 
/* 1784 */       this.srcCM.coerceData(localWritableRaster, false);
/* 1785 */       localObject1 = localWritableRaster.createChild(localWritableRaster.getMinX(), localWritableRaster.getMinY(), localWritableRaster.getWidth(), localWritableRaster.getHeight(), 0, 0, this.srcBands);
/*      */     }
/*      */ 
/* 1790 */     this.raster.setRect((Raster)localObject1);
/* 1791 */     if ((paramInt > 7) && (paramInt % 8 == 0)) {
/* 1792 */       this.cbLock.lock();
/*      */       try {
/* 1794 */         processImageProgress(paramInt / this.sourceHeight * 100.0F);
/*      */       } finally {
/* 1796 */         this.cbLock.unlock();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private native void abortWrite(long paramLong);
/*      */ 
/*      */   private native void resetWriter(long paramLong);
/*      */ 
/*      */   private static native void disposeWriter(long paramLong);
/*      */ 
/*      */   private void writeOutputData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1836 */     this.cbLock.lock();
/*      */     try {
/* 1838 */       this.ios.write(paramArrayOfByte, paramInt1, paramInt2);
/*      */     } finally {
/* 1840 */       this.cbLock.unlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void setThreadLock()
/*      */   {
/* 1848 */     Thread localThread = Thread.currentThread();
/* 1849 */     if (this.theThread != null) {
/* 1850 */       if (this.theThread != localThread)
/*      */       {
/* 1853 */         throw new IllegalStateException("Attempt to use instance of " + this + " locked on thread " + this.theThread + " from thread " + localThread);
/*      */       }
/*      */ 
/* 1858 */       this.theLockCount += 1;
/*      */     }
/*      */     else {
/* 1861 */       this.theThread = localThread;
/* 1862 */       this.theLockCount = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void clearThreadLock() {
/* 1867 */     Thread localThread = Thread.currentThread();
/* 1868 */     if ((this.theThread == null) || (this.theThread != localThread)) {
/* 1869 */       throw new IllegalStateException("Attempt to clear thread lock form wrong thread. Locked thread: " + this.theThread + "; current thread: " + localThread);
/*      */     }
/*      */ 
/* 1873 */     this.theLockCount -= 1;
/* 1874 */     if (this.theLockCount == 0)
/* 1875 */       this.theThread = null;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  179 */     AccessController.doPrivileged(new LoadLibraryAction("jpeg"));
/*      */ 
/*  181 */     initWriterIDs(JPEGQTable.class, JPEGHuffmanTable.class);
/*      */   }
/*      */ 
/*      */   private static class CallBackLock
/*      */   {
/*      */     private State lockState;
/*      */ 
/*      */     CallBackLock()
/*      */     {
/* 1886 */       this.lockState = State.Unlocked;
/*      */     }
/*      */ 
/*      */     void check() {
/* 1890 */       if (this.lockState != State.Unlocked)
/* 1891 */         throw new IllegalStateException("Access to the writer is not allowed");
/*      */     }
/*      */ 
/*      */     private void lock()
/*      */     {
/* 1896 */       this.lockState = State.Locked;
/*      */     }
/*      */ 
/*      */     private void unlock() {
/* 1900 */       this.lockState = State.Unlocked;
/*      */     }
/*      */ 
/*      */     private static enum State {
/* 1904 */       Unlocked, 
/* 1905 */       Locked;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class JPEGWriterDisposerRecord
/*      */     implements DisposerRecord
/*      */   {
/*      */     private long pData;
/*      */ 
/*      */     public JPEGWriterDisposerRecord(long paramLong)
/*      */     {
/* 1814 */       this.pData = paramLong;
/*      */     }
/*      */ 
/*      */     public synchronized void dispose() {
/* 1818 */       if (this.pData != 0L) {
/* 1819 */         JPEGImageWriter.disposeWriter(this.pData);
/* 1820 */         this.pData = 0L;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGImageWriter
 * JD-Core Version:    0.6.2
 */
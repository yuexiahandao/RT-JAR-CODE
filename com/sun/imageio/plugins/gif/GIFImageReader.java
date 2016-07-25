/*      */ package com.sun.imageio.plugins.gif;
/*      */ 
/*      */ import com.sun.imageio.plugins.common.ReaderUtil;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteOrder;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.ImageReadParam;
/*      */ import javax.imageio.ImageReader;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.spi.ImageReaderSpi;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ 
/*      */ public class GIFImageReader extends ImageReader
/*      */ {
/*   55 */   ImageInputStream stream = null;
/*      */ 
/*   60 */   boolean gotHeader = false;
/*      */ 
/*   63 */   GIFStreamMetadata streamMetadata = null;
/*      */ 
/*   66 */   int currIndex = -1;
/*      */ 
/*   69 */   GIFImageMetadata imageMetadata = null;
/*      */ 
/*   74 */   List imageStartPosition = new ArrayList();
/*      */   int imageMetadataLength;
/*   81 */   int numImages = -1;
/*      */ 
/*   84 */   byte[] block = new byte['ÿ'];
/*   85 */   int blockLength = 0;
/*   86 */   int bitPos = 0;
/*   87 */   int nextByte = 0;
/*      */   int initCodeSize;
/*      */   int clearCode;
/*      */   int eofCode;
/*   93 */   int next32Bits = 0;
/*      */ 
/*   97 */   boolean lastBlockFound = false;
/*      */ 
/*  100 */   BufferedImage theImage = null;
/*      */ 
/*  103 */   WritableRaster theTile = null;
/*      */ 
/*  106 */   int width = -1; int height = -1;
/*      */ 
/*  109 */   int streamX = -1; int streamY = -1;
/*      */ 
/*  112 */   int rowsDone = 0;
/*      */ 
/*  115 */   int interlacePass = 0;
/*      */ 
/*  120 */   static final int[] interlaceIncrement = { 8, 8, 4, 2, -1 };
/*  121 */   static final int[] interlaceOffset = { 0, 4, 2, 1, -1 };
/*      */   Rectangle sourceRegion;
/*      */   int sourceXSubsampling;
/*      */   int sourceYSubsampling;
/*      */   int sourceMinProgressivePass;
/*      */   int sourceMaxProgressivePass;
/*      */   Point destinationOffset;
/*      */   Rectangle destinationRegion;
/*      */   int updateMinY;
/*      */   int updateYStep;
/*  372 */   boolean decodeThisRow = true;
/*  373 */   int destY = 0;
/*      */   byte[] rowBuf;
/*      */ 
/*      */   public GIFImageReader(ImageReaderSpi paramImageReaderSpi)
/*      */   {
/*  124 */     super(paramImageReaderSpi);
/*      */   }
/*      */ 
/*      */   public void setInput(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  131 */     super.setInput(paramObject, paramBoolean1, paramBoolean2);
/*  132 */     if (paramObject != null) {
/*  133 */       if (!(paramObject instanceof ImageInputStream)) {
/*  134 */         throw new IllegalArgumentException("input not an ImageInputStream!");
/*      */       }
/*      */ 
/*  137 */       this.stream = ((ImageInputStream)paramObject);
/*      */     } else {
/*  139 */       this.stream = null;
/*      */     }
/*      */ 
/*  143 */     resetStreamSettings();
/*      */   }
/*      */ 
/*      */   public int getNumImages(boolean paramBoolean) throws IIOException {
/*  147 */     if (this.stream == null) {
/*  148 */       throw new IllegalStateException("Input not set!");
/*      */     }
/*  150 */     if ((this.seekForwardOnly) && (paramBoolean)) {
/*  151 */       throw new IllegalStateException("seekForwardOnly and allowSearch can't both be true!");
/*      */     }
/*      */ 
/*  155 */     if (this.numImages > 0) {
/*  156 */       return this.numImages;
/*      */     }
/*  158 */     if (paramBoolean) {
/*  159 */       this.numImages = (locateImage(2147483647) + 1);
/*      */     }
/*  161 */     return this.numImages;
/*      */   }
/*      */ 
/*      */   private void checkIndex(int paramInt)
/*      */   {
/*  167 */     if (paramInt < this.minIndex) {
/*  168 */       throw new IndexOutOfBoundsException("imageIndex < minIndex!");
/*      */     }
/*  170 */     if (this.seekForwardOnly)
/*  171 */       this.minIndex = paramInt;
/*      */   }
/*      */ 
/*      */   public int getWidth(int paramInt) throws IIOException
/*      */   {
/*  176 */     checkIndex(paramInt);
/*      */ 
/*  178 */     int i = locateImage(paramInt);
/*  179 */     if (i != paramInt) {
/*  180 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  182 */     readMetadata();
/*  183 */     return this.imageMetadata.imageWidth;
/*      */   }
/*      */ 
/*      */   public int getHeight(int paramInt) throws IIOException {
/*  187 */     checkIndex(paramInt);
/*      */ 
/*  189 */     int i = locateImage(paramInt);
/*  190 */     if (i != paramInt) {
/*  191 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  193 */     readMetadata();
/*  194 */     return this.imageMetadata.imageHeight;
/*      */   }
/*      */ 
/*      */   public Iterator getImageTypes(int paramInt) throws IIOException {
/*  198 */     checkIndex(paramInt);
/*      */ 
/*  200 */     int i = locateImage(paramInt);
/*  201 */     if (i != paramInt) {
/*  202 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  204 */     readMetadata();
/*      */ 
/*  206 */     ArrayList localArrayList = new ArrayList(1);
/*      */     byte[] arrayOfByte1;
/*  209 */     if (this.imageMetadata.localColorTable != null)
/*  210 */       arrayOfByte1 = this.imageMetadata.localColorTable;
/*      */     else {
/*  212 */       arrayOfByte1 = this.streamMetadata.globalColorTable;
/*      */     }
/*      */ 
/*  216 */     int j = arrayOfByte1.length / 3;
/*      */     int k;
/*  218 */     if (j == 2)
/*  219 */       k = 1;
/*  220 */     else if (j == 4)
/*  221 */       k = 2;
/*  222 */     else if ((j == 8) || (j == 16))
/*      */     {
/*  224 */       k = 4;
/*      */     }
/*      */     else {
/*  227 */       k = 8;
/*      */     }
/*  229 */     int m = 1 << k;
/*  230 */     byte[] arrayOfByte2 = new byte[m];
/*  231 */     byte[] arrayOfByte3 = new byte[m];
/*  232 */     byte[] arrayOfByte4 = new byte[m];
/*      */ 
/*  235 */     int n = 0;
/*  236 */     for (int i1 = 0; i1 < j; i1++) {
/*  237 */       arrayOfByte2[i1] = arrayOfByte1[(n++)];
/*  238 */       arrayOfByte3[i1] = arrayOfByte1[(n++)];
/*  239 */       arrayOfByte4[i1] = arrayOfByte1[(n++)];
/*      */     }
/*      */ 
/*  242 */     byte[] arrayOfByte5 = null;
/*  243 */     if (this.imageMetadata.transparentColorFlag) {
/*  244 */       arrayOfByte5 = new byte[m];
/*  245 */       Arrays.fill(arrayOfByte5, (byte)-1);
/*      */ 
/*  249 */       int i2 = Math.min(this.imageMetadata.transparentColorIndex, m - 1);
/*      */ 
/*  251 */       arrayOfByte5[i2] = 0;
/*      */     }
/*      */ 
/*  254 */     int[] arrayOfInt = new int[1];
/*  255 */     arrayOfInt[0] = k;
/*  256 */     localArrayList.add(ImageTypeSpecifier.createIndexed(arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte5, k, 0));
/*      */ 
/*  258 */     return localArrayList.iterator();
/*      */   }
/*      */ 
/*      */   public ImageReadParam getDefaultReadParam() {
/*  262 */     return new ImageReadParam();
/*      */   }
/*      */ 
/*      */   public IIOMetadata getStreamMetadata() throws IIOException {
/*  266 */     readHeader();
/*  267 */     return this.streamMetadata;
/*      */   }
/*      */ 
/*      */   public IIOMetadata getImageMetadata(int paramInt) throws IIOException {
/*  271 */     checkIndex(paramInt);
/*      */ 
/*  273 */     int i = locateImage(paramInt);
/*  274 */     if (i != paramInt) {
/*  275 */       throw new IndexOutOfBoundsException("Bad image index!");
/*      */     }
/*  277 */     readMetadata();
/*  278 */     return this.imageMetadata;
/*      */   }
/*      */ 
/*      */   private void initNext32Bits()
/*      */   {
/*  284 */     this.next32Bits = (this.block[0] & 0xFF);
/*  285 */     this.next32Bits |= (this.block[1] & 0xFF) << 8;
/*  286 */     this.next32Bits |= (this.block[2] & 0xFF) << 16;
/*  287 */     this.next32Bits |= this.block[3] << 24;
/*  288 */     this.nextByte = 4; } 
/*      */   private int getCode(int paramInt1, int paramInt2) throws IOException { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 480	com/sun/imageio/plugins/gif/GIFImageReader:bitPos	I
/*      */     //   4: iload_1
/*      */     //   5: iadd
/*      */     //   6: bipush 32
/*      */     //   8: if_icmple +8 -> 16
/*      */     //   11: aload_0
/*      */     //   12: getfield 485	com/sun/imageio/plugins/gif/GIFImageReader:eofCode	I
/*      */     //   15: ireturn
/*      */     //   16: aload_0
/*      */     //   17: getfield 491	com/sun/imageio/plugins/gif/GIFImageReader:next32Bits	I
/*      */     //   20: aload_0
/*      */     //   21: getfield 480	com/sun/imageio/plugins/gif/GIFImageReader:bitPos	I
/*      */     //   24: ishr
/*      */     //   25: iload_2
/*      */     //   26: iand
/*      */     //   27: istore_3
/*      */     //   28: aload_0
/*      */     //   29: dup
/*      */     //   30: getfield 480	com/sun/imageio/plugins/gif/GIFImageReader:bitPos	I
/*      */     //   33: iload_1
/*      */     //   34: iadd
/*      */     //   35: putfield 480	com/sun/imageio/plugins/gif/GIFImageReader:bitPos	I
/*      */     //   38: aload_0
/*      */     //   39: getfield 480	com/sun/imageio/plugins/gif/GIFImageReader:bitPos	I
/*      */     //   42: bipush 8
/*      */     //   44: if_icmplt +156 -> 200
/*      */     //   47: aload_0
/*      */     //   48: getfield 506	com/sun/imageio/plugins/gif/GIFImageReader:lastBlockFound	Z
/*      */     //   51: ifne +149 -> 200
/*      */     //   54: aload_0
/*      */     //   55: dup
/*      */     //   56: getfield 491	com/sun/imageio/plugins/gif/GIFImageReader:next32Bits	I
/*      */     //   59: bipush 8
/*      */     //   61: iushr
/*      */     //   62: putfield 491	com/sun/imageio/plugins/gif/GIFImageReader:next32Bits	I
/*      */     //   65: aload_0
/*      */     //   66: dup
/*      */     //   67: getfield 480	com/sun/imageio/plugins/gif/GIFImageReader:bitPos	I
/*      */     //   70: bipush 8
/*      */     //   72: isub
/*      */     //   73: putfield 480	com/sun/imageio/plugins/gif/GIFImageReader:bitPos	I
/*      */     //   76: aload_0
/*      */     //   77: getfield 492	com/sun/imageio/plugins/gif/GIFImageReader:nextByte	I
/*      */     //   80: aload_0
/*      */     //   81: getfield 481	com/sun/imageio/plugins/gif/GIFImageReader:blockLength	I
/*      */     //   84: if_icmplt +85 -> 169
/*      */     //   87: aload_0
/*      */     //   88: aload_0
/*      */     //   89: getfield 521	com/sun/imageio/plugins/gif/GIFImageReader:stream	Ljavax/imageio/stream/ImageInputStream;
/*      */     //   92: invokeinterface 608 1 0
/*      */     //   97: putfield 481	com/sun/imageio/plugins/gif/GIFImageReader:blockLength	I
/*      */     //   100: aload_0
/*      */     //   101: getfield 481	com/sun/imageio/plugins/gif/GIFImageReader:blockLength	I
/*      */     //   104: ifne +10 -> 114
/*      */     //   107: aload_0
/*      */     //   108: iconst_1
/*      */     //   109: putfield 506	com/sun/imageio/plugins/gif/GIFImageReader:lastBlockFound	Z
/*      */     //   112: iload_3
/*      */     //   113: ireturn
/*      */     //   114: aload_0
/*      */     //   115: getfield 481	com/sun/imageio/plugins/gif/GIFImageReader:blockLength	I
/*      */     //   118: istore 4
/*      */     //   120: iconst_0
/*      */     //   121: istore 5
/*      */     //   123: iload 4
/*      */     //   125: ifle +39 -> 164
/*      */     //   128: aload_0
/*      */     //   129: getfield 521	com/sun/imageio/plugins/gif/GIFImageReader:stream	Ljavax/imageio/stream/ImageInputStream;
/*      */     //   132: aload_0
/*      */     //   133: getfield 508	com/sun/imageio/plugins/gif/GIFImageReader:block	[B
/*      */     //   136: iload 5
/*      */     //   138: iload 4
/*      */     //   140: invokeinterface 614 4 0
/*      */     //   145: istore 6
/*      */     //   147: iload 5
/*      */     //   149: iload 6
/*      */     //   151: iadd
/*      */     //   152: istore 5
/*      */     //   154: iload 4
/*      */     //   156: iload 6
/*      */     //   158: isub
/*      */     //   159: istore 4
/*      */     //   161: goto -38 -> 123
/*      */     //   164: aload_0
/*      */     //   165: iconst_0
/*      */     //   166: putfield 492	com/sun/imageio/plugins/gif/GIFImageReader:nextByte	I
/*      */     //   169: aload_0
/*      */     //   170: dup
/*      */     //   171: getfield 491	com/sun/imageio/plugins/gif/GIFImageReader:next32Bits	I
/*      */     //   174: aload_0
/*      */     //   175: getfield 508	com/sun/imageio/plugins/gif/GIFImageReader:block	[B
/*      */     //   178: aload_0
/*      */     //   179: dup
/*      */     //   180: getfield 492	com/sun/imageio/plugins/gif/GIFImageReader:nextByte	I
/*      */     //   183: dup_x1
/*      */     //   184: iconst_1
/*      */     //   185: iadd
/*      */     //   186: putfield 492	com/sun/imageio/plugins/gif/GIFImageReader:nextByte	I
/*      */     //   189: baload
/*      */     //   190: bipush 24
/*      */     //   192: ishl
/*      */     //   193: ior
/*      */     //   194: putfield 491	com/sun/imageio/plugins/gif/GIFImageReader:next32Bits	I
/*      */     //   197: goto -159 -> 38
/*      */     //   200: iload_3
/*      */     //   201: ireturn } 
/*  339 */   public void initializeStringTable(int[] paramArrayOfInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int[] paramArrayOfInt2) { int i = 1 << this.initCodeSize;
/*  340 */     for (int j = 0; j < i; j++) {
/*  341 */       paramArrayOfInt1[j] = -1;
/*  342 */       paramArrayOfByte1[j] = ((byte)j);
/*  343 */       paramArrayOfByte2[j] = ((byte)j);
/*  344 */       paramArrayOfInt2[j] = 1;
/*      */     }
/*      */ 
/*  349 */     for (j = i; j < 4096; j++) {
/*  350 */       paramArrayOfInt1[j] = -1;
/*  351 */       paramArrayOfInt2[j] = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void outputRow()
/*      */   {
/*  379 */     int i = Math.min(this.sourceRegion.width, this.destinationRegion.width * this.sourceXSubsampling);
/*      */ 
/*  381 */     int j = this.destinationRegion.x;
/*      */ 
/*  383 */     if (this.sourceXSubsampling == 1)
/*  384 */       this.theTile.setDataElements(j, this.destY, i, 1, this.rowBuf);
/*      */     else {
/*  386 */       for (int k = 0; k < i; j++) {
/*  387 */         this.theTile.setSample(j, this.destY, 0, this.rowBuf[k] & 0xFF);
/*      */ 
/*  386 */         k += this.sourceXSubsampling;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  392 */     if (this.updateListeners != null) {
/*  393 */       int[] arrayOfInt = { 0 };
/*      */ 
/*  396 */       processImageUpdate(this.theImage, j, this.destY, i, 1, 1, this.updateYStep, arrayOfInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void computeDecodeThisRow()
/*      */   {
/*  404 */     this.decodeThisRow = ((this.destY < this.destinationRegion.y + this.destinationRegion.height) && (this.streamY >= this.sourceRegion.y) && (this.streamY < this.sourceRegion.y + this.sourceRegion.height) && ((this.streamY - this.sourceRegion.y) % this.sourceYSubsampling == 0));
/*      */   }
/*      */ 
/*      */   private void outputPixels(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/*  412 */     if ((this.interlacePass < this.sourceMinProgressivePass) || (this.interlacePass > this.sourceMaxProgressivePass))
/*      */     {
/*  414 */       return;
/*      */     }
/*      */ 
/*  417 */     for (int i = 0; i < paramInt; i++) {
/*  418 */       if (this.streamX >= this.sourceRegion.x) {
/*  419 */         this.rowBuf[(this.streamX - this.sourceRegion.x)] = paramArrayOfByte[i];
/*      */       }
/*      */ 
/*  423 */       this.streamX += 1;
/*  424 */       if (this.streamX == this.width)
/*      */       {
/*  426 */         this.rowsDone += 1;
/*  427 */         processImageProgress(100.0F * this.rowsDone / this.height);
/*      */ 
/*  429 */         if (this.decodeThisRow) {
/*  430 */           outputRow();
/*      */         }
/*      */ 
/*  433 */         this.streamX = 0;
/*  434 */         if (this.imageMetadata.interlaceFlag) {
/*  435 */           this.streamY += interlaceIncrement[this.interlacePass];
/*  436 */           if (this.streamY >= this.height)
/*      */           {
/*  438 */             if (this.updateListeners != null) {
/*  439 */               processPassComplete(this.theImage);
/*      */             }
/*      */ 
/*  442 */             this.interlacePass += 1;
/*  443 */             if (this.interlacePass > this.sourceMaxProgressivePass) {
/*  444 */               return;
/*      */             }
/*  446 */             this.streamY = interlaceOffset[this.interlacePass];
/*  447 */             startPass(this.interlacePass);
/*      */           }
/*      */         } else {
/*  450 */           this.streamY += 1;
/*      */         }
/*      */ 
/*  455 */         this.destY = (this.destinationRegion.y + (this.streamY - this.sourceRegion.y) / this.sourceYSubsampling);
/*      */ 
/*  457 */         computeDecodeThisRow();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readHeader()
/*      */     throws IIOException
/*      */   {
/*  465 */     if (this.gotHeader) {
/*  466 */       return;
/*      */     }
/*  468 */     if (this.stream == null) {
/*  469 */       throw new IllegalStateException("Input not set!");
/*      */     }
/*      */ 
/*  473 */     this.streamMetadata = new GIFStreamMetadata();
/*      */     try
/*      */     {
/*  476 */       this.stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
/*      */ 
/*  478 */       byte[] arrayOfByte = new byte[6];
/*  479 */       this.stream.readFully(arrayOfByte);
/*      */ 
/*  481 */       StringBuffer localStringBuffer = new StringBuffer(3);
/*  482 */       localStringBuffer.append((char)arrayOfByte[3]);
/*  483 */       localStringBuffer.append((char)arrayOfByte[4]);
/*  484 */       localStringBuffer.append((char)arrayOfByte[5]);
/*  485 */       this.streamMetadata.version = localStringBuffer.toString();
/*      */ 
/*  487 */       this.streamMetadata.logicalScreenWidth = this.stream.readUnsignedShort();
/*  488 */       this.streamMetadata.logicalScreenHeight = this.stream.readUnsignedShort();
/*      */ 
/*  490 */       int i = this.stream.readUnsignedByte();
/*  491 */       int j = (i & 0x80) != 0 ? 1 : 0;
/*  492 */       this.streamMetadata.colorResolution = ((i >> 4 & 0x7) + 1);
/*  493 */       this.streamMetadata.sortFlag = ((i & 0x8) != 0);
/*  494 */       int k = 1 << (i & 0x7) + 1;
/*      */ 
/*  496 */       this.streamMetadata.backgroundColorIndex = this.stream.readUnsignedByte();
/*  497 */       this.streamMetadata.pixelAspectRatio = this.stream.readUnsignedByte();
/*      */ 
/*  499 */       if (j != 0) {
/*  500 */         this.streamMetadata.globalColorTable = new byte[3 * k];
/*  501 */         this.stream.readFully(this.streamMetadata.globalColorTable);
/*      */       } else {
/*  503 */         this.streamMetadata.globalColorTable = null;
/*      */       }
/*      */ 
/*  507 */       this.imageStartPosition.add(Long.valueOf(this.stream.getStreamPosition()));
/*      */     } catch (IOException localIOException) {
/*  509 */       throw new IIOException("I/O error reading header!", localIOException);
/*      */     }
/*      */ 
/*  512 */     this.gotHeader = true;
/*      */   }
/*      */ 
/*      */   private boolean skipImage() throws IIOException
/*      */   {
/*      */     try
/*      */     {
/*      */       while (true)
/*      */       {
/*  521 */         int i = this.stream.readUnsignedByte();
/*      */         int j;
/*      */         int k;
/*  523 */         if (i == 44) {
/*  524 */           this.stream.skipBytes(8);
/*      */ 
/*  526 */           j = this.stream.readUnsignedByte();
/*  527 */           if ((j & 0x80) != 0)
/*      */           {
/*  529 */             k = (j & 0x7) + 1;
/*  530 */             this.stream.skipBytes(3 * (1 << k));
/*      */           }
/*      */ 
/*  533 */           this.stream.skipBytes(1);
/*      */ 
/*  535 */           k = 0;
/*      */           do {
/*  537 */             k = this.stream.readUnsignedByte();
/*  538 */             this.stream.skipBytes(k);
/*  539 */           }while (k > 0);
/*      */ 
/*  541 */           return true;
/*  542 */         }if (i == 59)
/*  543 */           return false;
/*  544 */         if (i == 33) {
/*  545 */           j = this.stream.readUnsignedByte();
/*      */ 
/*  547 */           k = 0;
/*      */           do {
/*  549 */             k = this.stream.readUnsignedByte();
/*  550 */             this.stream.skipBytes(k);
/*  551 */           }while (k > 0); } else {
/*  552 */           if (i == 0)
/*      */           {
/*  554 */             return false;
/*      */           }
/*  556 */           j = 0;
/*      */           do {
/*  558 */             j = this.stream.readUnsignedByte();
/*  559 */             this.stream.skipBytes(j);
/*  560 */           }while (j > 0);
/*      */         }
/*      */       }
/*      */     } catch (EOFException localEOFException) {
/*  564 */       return false;
/*      */     } catch (IOException localIOException) {
/*  566 */       throw new IIOException("I/O error locating image!", localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int locateImage(int paramInt) throws IIOException {
/*  571 */     readHeader();
/*      */     try
/*      */     {
/*  575 */       int i = Math.min(paramInt, this.imageStartPosition.size() - 1);
/*      */ 
/*  578 */       Long localLong1 = (Long)this.imageStartPosition.get(i);
/*  579 */       this.stream.seek(localLong1.longValue());
/*      */ 
/*  582 */       while (i < paramInt) {
/*  583 */         if (!skipImage()) {
/*  584 */           i--;
/*  585 */           return i;
/*      */         }
/*      */ 
/*  588 */         Long localLong2 = new Long(this.stream.getStreamPosition());
/*  589 */         this.imageStartPosition.add(localLong2);
/*  590 */         i++;
/*      */       }
/*      */     } catch (IOException localIOException) {
/*  593 */       throw new IIOException("Couldn't seek!", localIOException);
/*      */     }
/*      */ 
/*  596 */     if (this.currIndex != paramInt) {
/*  597 */       this.imageMetadata = null;
/*      */     }
/*  599 */     this.currIndex = paramInt;
/*  600 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private byte[] concatenateBlocks() throws IOException
/*      */   {
/*  605 */     Object localObject = new byte[0];
/*      */     while (true) {
/*  607 */       int i = this.stream.readUnsignedByte();
/*  608 */       if (i == 0) {
/*      */         break;
/*      */       }
/*  611 */       byte[] arrayOfByte = new byte[localObject.length + i];
/*  612 */       System.arraycopy(localObject, 0, arrayOfByte, 0, localObject.length);
/*  613 */       this.stream.readFully(arrayOfByte, localObject.length, i);
/*  614 */       localObject = arrayOfByte;
/*      */     }
/*      */ 
/*  617 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void readMetadata() throws IIOException
/*      */   {
/*  622 */     if (this.stream == null) {
/*  623 */       throw new IllegalStateException("Input not set!");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  628 */       this.imageMetadata = new GIFImageMetadata();
/*      */ 
/*  630 */       long l = this.stream.getStreamPosition();
/*      */       while (true) {
/*  632 */         int i = this.stream.readUnsignedByte();
/*      */         int j;
/*      */         int k;
/*      */         int n;
/*  633 */         if (i == 44) {
/*  634 */           this.imageMetadata.imageLeftPosition = this.stream.readUnsignedShort();
/*      */ 
/*  636 */           this.imageMetadata.imageTopPosition = this.stream.readUnsignedShort();
/*      */ 
/*  638 */           this.imageMetadata.imageWidth = this.stream.readUnsignedShort();
/*  639 */           this.imageMetadata.imageHeight = this.stream.readUnsignedShort();
/*      */ 
/*  641 */           j = this.stream.readUnsignedByte();
/*  642 */           k = (j & 0x80) != 0 ? 1 : 0;
/*      */ 
/*  644 */           this.imageMetadata.interlaceFlag = ((j & 0x40) != 0);
/*  645 */           this.imageMetadata.sortFlag = ((j & 0x20) != 0);
/*  646 */           n = 1 << (j & 0x7) + 1;
/*      */ 
/*  648 */           if (k != 0)
/*      */           {
/*  650 */             this.imageMetadata.localColorTable = new byte[3 * n];
/*      */ 
/*  652 */             this.stream.readFully(this.imageMetadata.localColorTable);
/*      */           } else {
/*  654 */             this.imageMetadata.localColorTable = null;
/*      */           }
/*      */ 
/*  658 */           this.imageMetadataLength = ((int)(this.stream.getStreamPosition() - l));
/*      */ 
/*  662 */           return;
/*  663 */         }if (i == 33) {
/*  664 */           j = this.stream.readUnsignedByte();
/*      */ 
/*  666 */           if (j == 249) {
/*  667 */             k = this.stream.readUnsignedByte();
/*  668 */             n = this.stream.readUnsignedByte();
/*  669 */             this.imageMetadata.disposalMethod = (n >> 2 & 0x3);
/*      */ 
/*  671 */             this.imageMetadata.userInputFlag = ((n & 0x2) != 0);
/*      */ 
/*  673 */             this.imageMetadata.transparentColorFlag = ((n & 0x1) != 0);
/*      */ 
/*  676 */             this.imageMetadata.delayTime = this.stream.readUnsignedShort();
/*  677 */             this.imageMetadata.transparentColorIndex = this.stream.readUnsignedByte();
/*      */ 
/*  680 */             int i1 = this.stream.readUnsignedByte();
/*  681 */           } else if (j == 1) {
/*  682 */             k = this.stream.readUnsignedByte();
/*  683 */             this.imageMetadata.hasPlainTextExtension = true;
/*  684 */             this.imageMetadata.textGridLeft = this.stream.readUnsignedShort();
/*      */ 
/*  686 */             this.imageMetadata.textGridTop = this.stream.readUnsignedShort();
/*      */ 
/*  688 */             this.imageMetadata.textGridWidth = this.stream.readUnsignedShort();
/*      */ 
/*  690 */             this.imageMetadata.textGridHeight = this.stream.readUnsignedShort();
/*      */ 
/*  692 */             this.imageMetadata.characterCellWidth = this.stream.readUnsignedByte();
/*      */ 
/*  694 */             this.imageMetadata.characterCellHeight = this.stream.readUnsignedByte();
/*      */ 
/*  696 */             this.imageMetadata.textForegroundColor = this.stream.readUnsignedByte();
/*      */ 
/*  698 */             this.imageMetadata.textBackgroundColor = this.stream.readUnsignedByte();
/*      */ 
/*  700 */             this.imageMetadata.text = concatenateBlocks();
/*  701 */           } else if (j == 254) {
/*  702 */             byte[] arrayOfByte1 = concatenateBlocks();
/*  703 */             if (this.imageMetadata.comments == null) {
/*  704 */               this.imageMetadata.comments = new ArrayList();
/*      */             }
/*  706 */             this.imageMetadata.comments.add(arrayOfByte1);
/*      */           }
/*      */           else
/*      */           {
/*      */             int m;
/*  707 */             if (j == 255) {
/*  708 */               m = this.stream.readUnsignedByte();
/*  709 */               byte[] arrayOfByte2 = new byte[8];
/*  710 */               byte[] arrayOfByte3 = new byte[3];
/*      */ 
/*  713 */               byte[] arrayOfByte4 = new byte[m];
/*  714 */               this.stream.readFully(arrayOfByte4);
/*      */ 
/*  716 */               int i2 = copyData(arrayOfByte4, 0, arrayOfByte2);
/*  717 */               i2 = copyData(arrayOfByte4, i2, arrayOfByte3);
/*      */ 
/*  719 */               Object localObject = concatenateBlocks();
/*      */ 
/*  721 */               if (i2 < m) {
/*  722 */                 int i3 = m - i2;
/*  723 */                 byte[] arrayOfByte5 = new byte[i3 + localObject.length];
/*      */ 
/*  726 */                 System.arraycopy(arrayOfByte4, i2, arrayOfByte5, 0, i3);
/*  727 */                 System.arraycopy(localObject, 0, arrayOfByte5, i3, localObject.length);
/*      */ 
/*  730 */                 localObject = arrayOfByte5;
/*      */               }
/*      */ 
/*  734 */               if (this.imageMetadata.applicationIDs == null) {
/*  735 */                 this.imageMetadata.applicationIDs = new ArrayList();
/*  736 */                 this.imageMetadata.authenticationCodes = new ArrayList();
/*      */ 
/*  738 */                 this.imageMetadata.applicationData = new ArrayList();
/*      */               }
/*  740 */               this.imageMetadata.applicationIDs.add(arrayOfByte2);
/*  741 */               this.imageMetadata.authenticationCodes.add(arrayOfByte3);
/*  742 */               this.imageMetadata.applicationData.add(localObject);
/*      */             }
/*      */             else {
/*  745 */               m = 0;
/*      */               do {
/*  747 */                 m = this.stream.readUnsignedByte();
/*  748 */                 this.stream.skipBytes(m);
/*  749 */               }while (m > 0);
/*      */             }
/*      */           } } else { if (i == 59) {
/*  752 */             throw new IndexOutOfBoundsException("Attempt to read past end of image sequence!");
/*      */           }
/*      */ 
/*  755 */           throw new IIOException("Unexpected block type " + i + "!"); }
/*      */       }
/*      */     }
/*      */     catch (IIOException localIIOException)
/*      */     {
/*  760 */       throw localIIOException;
/*      */     } catch (IOException localIOException) {
/*  762 */       throw new IIOException("I/O error reading image metadata!", localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int copyData(byte[] paramArrayOfByte1, int paramInt, byte[] paramArrayOfByte2) {
/*  767 */     int i = paramArrayOfByte2.length;
/*  768 */     int j = paramArrayOfByte1.length - paramInt;
/*  769 */     if (i > j) {
/*  770 */       i = j;
/*      */     }
/*  772 */     System.arraycopy(paramArrayOfByte1, paramInt, paramArrayOfByte2, 0, i);
/*  773 */     return paramInt + i;
/*      */   }
/*      */ 
/*      */   private void startPass(int paramInt) {
/*  777 */     if (this.updateListeners == null) {
/*  778 */       return;
/*      */     }
/*      */ 
/*  781 */     int i = 0;
/*  782 */     int j = 1;
/*  783 */     if (this.imageMetadata.interlaceFlag) {
/*  784 */       i = interlaceOffset[this.interlacePass];
/*  785 */       j = interlaceIncrement[this.interlacePass];
/*      */     }
/*      */ 
/*  788 */     int[] arrayOfInt1 = ReaderUtil.computeUpdatedPixels(this.sourceRegion, this.destinationOffset, this.destinationRegion.x, this.destinationRegion.y, this.destinationRegion.x + this.destinationRegion.width - 1, this.destinationRegion.y + this.destinationRegion.height - 1, this.sourceXSubsampling, this.sourceYSubsampling, 0, i, this.destinationRegion.width, (this.destinationRegion.height + j - 1) / j, 1, j);
/*      */ 
/*  807 */     this.updateMinY = arrayOfInt1[1];
/*  808 */     this.updateYStep = arrayOfInt1[5];
/*      */ 
/*  811 */     int[] arrayOfInt2 = { 0 };
/*      */ 
/*  813 */     processPassStarted(this.theImage, this.interlacePass, this.sourceMinProgressivePass, this.sourceMaxProgressivePass, 0, this.updateMinY, 1, this.updateYStep, arrayOfInt2);
/*      */   }
/*      */ 
/*      */   public BufferedImage read(int paramInt, ImageReadParam paramImageReadParam)
/*      */     throws IIOException
/*      */   {
/*  826 */     if (this.stream == null) {
/*  827 */       throw new IllegalStateException("Input not set!");
/*      */     }
/*  829 */     checkIndex(paramInt);
/*      */ 
/*  831 */     int i = locateImage(paramInt);
/*  832 */     if (i != paramInt) {
/*  833 */       throw new IndexOutOfBoundsException("imageIndex out of bounds!");
/*      */     }
/*      */ 
/*  836 */     clearAbortRequest();
/*  837 */     readMetadata();
/*      */ 
/*  840 */     if (paramImageReadParam == null) {
/*  841 */       paramImageReadParam = getDefaultReadParam();
/*      */     }
/*      */ 
/*  845 */     Iterator localIterator = getImageTypes(paramInt);
/*  846 */     this.theImage = getDestination(paramImageReadParam, localIterator, this.imageMetadata.imageWidth, this.imageMetadata.imageHeight);
/*      */ 
/*  850 */     this.theTile = this.theImage.getWritableTile(0, 0);
/*  851 */     this.width = this.imageMetadata.imageWidth;
/*  852 */     this.height = this.imageMetadata.imageHeight;
/*  853 */     this.streamX = 0;
/*  854 */     this.streamY = 0;
/*  855 */     this.rowsDone = 0;
/*  856 */     this.interlacePass = 0;
/*      */ 
/*  861 */     this.sourceRegion = new Rectangle(0, 0, 0, 0);
/*  862 */     this.destinationRegion = new Rectangle(0, 0, 0, 0);
/*  863 */     computeRegions(paramImageReadParam, this.width, this.height, this.theImage, this.sourceRegion, this.destinationRegion);
/*      */ 
/*  865 */     this.destinationOffset = new Point(this.destinationRegion.x, this.destinationRegion.y);
/*      */ 
/*  868 */     this.sourceXSubsampling = paramImageReadParam.getSourceXSubsampling();
/*  869 */     this.sourceYSubsampling = paramImageReadParam.getSourceYSubsampling();
/*  870 */     this.sourceMinProgressivePass = Math.max(paramImageReadParam.getSourceMinProgressivePass(), 0);
/*      */ 
/*  872 */     this.sourceMaxProgressivePass = Math.min(paramImageReadParam.getSourceMaxProgressivePass(), 3);
/*      */ 
/*  875 */     this.destY = (this.destinationRegion.y + (this.streamY - this.sourceRegion.y) / this.sourceYSubsampling);
/*      */ 
/*  877 */     computeDecodeThisRow();
/*      */ 
/*  880 */     processImageStarted(paramInt);
/*  881 */     startPass(0);
/*      */ 
/*  883 */     this.rowBuf = new byte[this.width];
/*      */     try
/*      */     {
/*  887 */       this.initCodeSize = this.stream.readUnsignedByte();
/*      */ 
/*  890 */       this.blockLength = this.stream.readUnsignedByte();
/*  891 */       int j = this.blockLength;
/*  892 */       int k = 0;
/*      */       int m;
/*  893 */       while (j > 0) {
/*  894 */         m = this.stream.read(this.block, k, j);
/*  895 */         j -= m;
/*  896 */         k += m;
/*      */       }
/*      */ 
/*  899 */       this.bitPos = 0;
/*  900 */       this.nextByte = 0;
/*  901 */       this.lastBlockFound = false;
/*  902 */       this.interlacePass = 0;
/*      */ 
/*  905 */       initNext32Bits();
/*      */ 
/*  907 */       this.clearCode = (1 << this.initCodeSize);
/*  908 */       this.eofCode = (this.clearCode + 1);
/*      */ 
/*  910 */       int n = 0;
/*      */ 
/*  912 */       int[] arrayOfInt1 = new int[4096];
/*  913 */       byte[] arrayOfByte1 = new byte[4096];
/*  914 */       byte[] arrayOfByte2 = new byte[4096];
/*  915 */       int[] arrayOfInt2 = new int[4096];
/*  916 */       byte[] arrayOfByte3 = new byte[4096];
/*      */ 
/*  918 */       initializeStringTable(arrayOfInt1, arrayOfByte1, arrayOfByte2, arrayOfInt2);
/*  919 */       int i1 = (1 << this.initCodeSize) + 2;
/*  920 */       int i2 = this.initCodeSize + 1;
/*  921 */       int i3 = (1 << i2) - 1;
/*      */ 
/*  923 */       while (!abortRequested()) {
/*  924 */         m = getCode(i2, i3);
/*      */ 
/*  926 */         if (m == this.clearCode) {
/*  927 */           initializeStringTable(arrayOfInt1, arrayOfByte1, arrayOfByte2, arrayOfInt2);
/*  928 */           i1 = (1 << this.initCodeSize) + 2;
/*  929 */           i2 = this.initCodeSize + 1;
/*  930 */           i3 = (1 << i2) - 1;
/*      */ 
/*  932 */           m = getCode(i2, i3);
/*  933 */           if (m == this.eofCode)
/*      */           {
/*  935 */             processImageComplete();
/*  936 */             return this.theImage;
/*      */           }
/*      */         } else { if (m == this.eofCode)
/*      */           {
/*  940 */             processImageComplete();
/*  941 */             return this.theImage;
/*      */           }
/*      */ 
/*  944 */           if (m < i1) {
/*  945 */             i4 = m;
/*      */           } else {
/*  947 */             i4 = n;
/*  948 */             if (m != i1)
/*      */             {
/*  951 */               processWarningOccurred("Out-of-sequence code!");
/*      */             }
/*      */           }
/*      */ 
/*  955 */           i5 = i1;
/*  956 */           i6 = n;
/*      */ 
/*  958 */           arrayOfInt1[i5] = i6;
/*  959 */           arrayOfByte1[i5] = arrayOfByte2[i4];
/*  960 */           arrayOfByte2[i5] = arrayOfByte2[i6];
/*  961 */           arrayOfInt2[i6] += 1;
/*      */ 
/*  963 */           i1++;
/*  964 */           if ((i1 == 1 << i2) && (i1 < 4096))
/*      */           {
/*  966 */             i2++;
/*  967 */             i3 = (1 << i2) - 1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  972 */         int i4 = m;
/*  973 */         int i5 = arrayOfInt2[i4];
/*  974 */         for (int i6 = i5 - 1; i6 >= 0; i6--) {
/*  975 */           arrayOfByte3[i6] = arrayOfByte1[i4];
/*  976 */           i4 = arrayOfInt1[i4];
/*      */         }
/*      */ 
/*  979 */         outputPixels(arrayOfByte3, i5);
/*  980 */         n = m;
/*      */       }
/*      */ 
/*  983 */       processReadAborted();
/*  984 */       return this.theImage;
/*      */     } catch (IOException localIOException) {
/*  986 */       localIOException.printStackTrace();
/*  987 */       throw new IIOException("I/O error reading image!", localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  996 */     super.reset();
/*  997 */     resetStreamSettings();
/*      */   }
/*      */ 
/*      */   private void resetStreamSettings()
/*      */   {
/* 1004 */     this.gotHeader = false;
/* 1005 */     this.streamMetadata = null;
/* 1006 */     this.currIndex = -1;
/* 1007 */     this.imageMetadata = null;
/* 1008 */     this.imageStartPosition = new ArrayList();
/* 1009 */     this.numImages = -1;
/*      */ 
/* 1012 */     this.blockLength = 0;
/* 1013 */     this.bitPos = 0;
/* 1014 */     this.nextByte = 0;
/*      */ 
/* 1016 */     this.next32Bits = 0;
/* 1017 */     this.lastBlockFound = false;
/*      */ 
/* 1019 */     this.theImage = null;
/* 1020 */     this.theTile = null;
/* 1021 */     this.width = -1;
/* 1022 */     this.height = -1;
/* 1023 */     this.streamX = -1;
/* 1024 */     this.streamY = -1;
/* 1025 */     this.rowsDone = 0;
/* 1026 */     this.interlacePass = 0;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFImageReader
 * JD-Core Version:    0.6.2
 */
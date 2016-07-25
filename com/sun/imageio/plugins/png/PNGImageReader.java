/*      */ package com.sun.imageio.plugins.png;
/*      */ 
/*      */ import com.sun.imageio.plugins.common.ReaderUtil;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.DataBuffer;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.DataBufferUShort;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.SequenceInputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.InflaterInputStream;
/*      */ import java.util.zip.ZipException;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.ImageReadParam;
/*      */ import javax.imageio.ImageReader;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.spi.ImageReaderSpi;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ import sun.awt.image.ByteInterleavedRaster;
/*      */ 
/*      */ public class PNGImageReader extends ImageReader
/*      */ {
/*      */   static final int IHDR_TYPE = 1229472850;
/*      */   static final int PLTE_TYPE = 1347179589;
/*      */   static final int IDAT_TYPE = 1229209940;
/*      */   static final int IEND_TYPE = 1229278788;
/*      */   static final int bKGD_TYPE = 1649100612;
/*      */   static final int cHRM_TYPE = 1665684045;
/*      */   static final int gAMA_TYPE = 1732332865;
/*      */   static final int hIST_TYPE = 1749635924;
/*      */   static final int iCCP_TYPE = 1766015824;
/*      */   static final int iTXt_TYPE = 1767135348;
/*      */   static final int pHYs_TYPE = 1883789683;
/*      */   static final int sBIT_TYPE = 1933723988;
/*      */   static final int sPLT_TYPE = 1934642260;
/*      */   static final int sRGB_TYPE = 1934772034;
/*      */   static final int tEXt_TYPE = 1950701684;
/*      */   static final int tIME_TYPE = 1950960965;
/*      */   static final int tRNS_TYPE = 1951551059;
/*      */   static final int zTXt_TYPE = 2052348020;
/*      */   static final int PNG_COLOR_GRAY = 0;
/*      */   static final int PNG_COLOR_RGB = 2;
/*      */   static final int PNG_COLOR_PALETTE = 3;
/*      */   static final int PNG_COLOR_GRAY_ALPHA = 4;
/*      */   static final int PNG_COLOR_RGB_ALPHA = 6;
/*  144 */   static final int[] inputBandsForColorType = { 1, -1, 3, 1, 2, -1, 4 };
/*      */   static final int PNG_FILTER_NONE = 0;
/*      */   static final int PNG_FILTER_SUB = 1;
/*      */   static final int PNG_FILTER_UP = 2;
/*      */   static final int PNG_FILTER_AVERAGE = 3;
/*      */   static final int PNG_FILTER_PAETH = 4;
/*  160 */   static final int[] adam7XOffset = { 0, 4, 0, 2, 0, 1, 0 };
/*  161 */   static final int[] adam7YOffset = { 0, 0, 4, 0, 2, 0, 1 };
/*  162 */   static final int[] adam7XSubsampling = { 8, 8, 4, 4, 2, 2, 1, 1 };
/*  163 */   static final int[] adam7YSubsampling = { 8, 8, 8, 4, 4, 2, 2, 1 };
/*      */   private static final boolean debug = true;
/*  167 */   ImageInputStream stream = null;
/*      */ 
/*  169 */   boolean gotHeader = false;
/*  170 */   boolean gotMetadata = false;
/*      */ 
/*  172 */   ImageReadParam lastParam = null;
/*      */ 
/*  174 */   long imageStartPosition = -1L;
/*      */ 
/*  176 */   Rectangle sourceRegion = null;
/*  177 */   int sourceXSubsampling = -1;
/*  178 */   int sourceYSubsampling = -1;
/*  179 */   int sourceMinProgressivePass = 0;
/*  180 */   int sourceMaxProgressivePass = 6;
/*  181 */   int[] sourceBands = null;
/*  182 */   int[] destinationBands = null;
/*  183 */   Point destinationOffset = new Point(0, 0);
/*      */ 
/*  185 */   PNGMetadata metadata = new PNGMetadata();
/*      */ 
/*  187 */   DataInputStream pixelStream = null;
/*      */ 
/*  189 */   BufferedImage theImage = null;
/*      */ 
/*  192 */   int pixelsDone = 0;
/*      */   int totalPixels;
/*  860 */   private static final int[][] bandOffsets = { null, { 0 }, { 0, 1 }, { 0, 1, 2 }, { 0, 1, 2, 3 } };
/*      */ 
/*      */   public PNGImageReader(ImageReaderSpi paramImageReaderSpi)
/*      */   {
/*  198 */     super(paramImageReaderSpi);
/*      */   }
/*      */ 
/*      */   public void setInput(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  204 */     super.setInput(paramObject, paramBoolean1, paramBoolean2);
/*  205 */     this.stream = ((ImageInputStream)paramObject);
/*      */ 
/*  208 */     resetStreamSettings();
/*      */   }
/*      */ 
/*      */   private String readNullTerminatedString(String paramString, int paramInt) throws IOException {
/*  212 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*      */ 
/*  214 */     int j = 0;
/*      */     int i;
/*  215 */     while ((paramInt > j++) && ((i = this.stream.read()) != 0)) {
/*  216 */       if (i == -1) throw new EOFException();
/*  217 */       localByteArrayOutputStream.write(i);
/*      */     }
/*  219 */     return new String(localByteArrayOutputStream.toByteArray(), paramString);
/*      */   }
/*      */ 
/*      */   private void readHeader() throws IIOException {
/*  223 */     if (this.gotHeader) {
/*  224 */       return;
/*      */     }
/*  226 */     if (this.stream == null) {
/*  227 */       throw new IllegalStateException("Input source not set!");
/*      */     }
/*      */     try
/*      */     {
/*  231 */       byte[] arrayOfByte = new byte[8];
/*  232 */       this.stream.readFully(arrayOfByte);
/*      */ 
/*  234 */       if ((arrayOfByte[0] != -119) || (arrayOfByte[1] != 80) || (arrayOfByte[2] != 78) || (arrayOfByte[3] != 71) || (arrayOfByte[4] != 13) || (arrayOfByte[5] != 10) || (arrayOfByte[6] != 26) || (arrayOfByte[7] != 10))
/*      */       {
/*  242 */         throw new IIOException("Bad PNG signature!");
/*      */       }
/*      */ 
/*  245 */       int i = this.stream.readInt();
/*  246 */       if (i != 13) {
/*  247 */         throw new IIOException("Bad length for IHDR chunk!");
/*      */       }
/*  249 */       int j = this.stream.readInt();
/*  250 */       if (j != 1229472850) {
/*  251 */         throw new IIOException("Bad type for IHDR chunk!");
/*      */       }
/*      */ 
/*  254 */       this.metadata = new PNGMetadata();
/*      */ 
/*  256 */       int k = this.stream.readInt();
/*  257 */       int m = this.stream.readInt();
/*      */ 
/*  260 */       this.stream.readFully(arrayOfByte, 0, 5);
/*  261 */       int n = arrayOfByte[0] & 0xFF;
/*  262 */       int i1 = arrayOfByte[1] & 0xFF;
/*  263 */       int i2 = arrayOfByte[2] & 0xFF;
/*  264 */       int i3 = arrayOfByte[3] & 0xFF;
/*  265 */       int i4 = arrayOfByte[4] & 0xFF;
/*      */ 
/*  268 */       this.stream.skipBytes(4);
/*      */ 
/*  270 */       this.stream.flushBefore(this.stream.getStreamPosition());
/*      */ 
/*  272 */       if (k == 0) {
/*  273 */         throw new IIOException("Image width == 0!");
/*      */       }
/*  275 */       if (m == 0) {
/*  276 */         throw new IIOException("Image height == 0!");
/*      */       }
/*  278 */       if ((n != 1) && (n != 2) && (n != 4) && (n != 8) && (n != 16))
/*      */       {
/*  280 */         throw new IIOException("Bit depth must be 1, 2, 4, 8, or 16!");
/*      */       }
/*  282 */       if ((i1 != 0) && (i1 != 2) && (i1 != 3) && (i1 != 4) && (i1 != 6))
/*      */       {
/*  284 */         throw new IIOException("Color type must be 0, 2, 3, 4, or 6!");
/*      */       }
/*  286 */       if ((i1 == 3) && (n == 16)) {
/*  287 */         throw new IIOException("Bad color type/bit depth combination!");
/*      */       }
/*  289 */       if (((i1 == 2) || (i1 == 6) || (i1 == 4)) && (n != 8) && (n != 16))
/*      */       {
/*  293 */         throw new IIOException("Bad color type/bit depth combination!");
/*      */       }
/*  295 */       if (i2 != 0) {
/*  296 */         throw new IIOException("Unknown compression method (not 0)!");
/*      */       }
/*  298 */       if (i3 != 0) {
/*  299 */         throw new IIOException("Unknown filter method (not 0)!");
/*      */       }
/*  301 */       if ((i4 != 0) && (i4 != 1)) {
/*  302 */         throw new IIOException("Unknown interlace method (not 0 or 1)!");
/*      */       }
/*      */ 
/*  305 */       this.metadata.IHDR_present = true;
/*  306 */       this.metadata.IHDR_width = k;
/*  307 */       this.metadata.IHDR_height = m;
/*  308 */       this.metadata.IHDR_bitDepth = n;
/*  309 */       this.metadata.IHDR_colorType = i1;
/*  310 */       this.metadata.IHDR_compressionMethod = i2;
/*  311 */       this.metadata.IHDR_filterMethod = i3;
/*  312 */       this.metadata.IHDR_interlaceMethod = i4;
/*  313 */       this.gotHeader = true;
/*      */     } catch (IOException localIOException) {
/*  315 */       throw new IIOException("I/O error reading PNG header!", localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void parse_PLTE_chunk(int paramInt) throws IOException {
/*  320 */     if (this.metadata.PLTE_present) {
/*  321 */       processWarningOccurred("A PNG image may not contain more than one PLTE chunk.\nThe chunk wil be ignored.");
/*      */ 
/*  324 */       return;
/*  325 */     }if ((this.metadata.IHDR_colorType == 0) || (this.metadata.IHDR_colorType == 4))
/*      */     {
/*  327 */       processWarningOccurred("A PNG gray or gray alpha image cannot have a PLTE chunk.\nThe chunk wil be ignored.");
/*      */ 
/*  330 */       return;
/*      */     }
/*      */ 
/*  333 */     byte[] arrayOfByte = new byte[paramInt];
/*  334 */     this.stream.readFully(arrayOfByte);
/*      */ 
/*  336 */     int i = paramInt / 3;
/*      */     int j;
/*  337 */     if (this.metadata.IHDR_colorType == 3) {
/*  338 */       j = 1 << this.metadata.IHDR_bitDepth;
/*  339 */       if (i > j) {
/*  340 */         processWarningOccurred("PLTE chunk contains too many entries for bit depth, ignoring extras.");
/*      */ 
/*  342 */         i = j;
/*      */       }
/*  344 */       i = Math.min(i, j);
/*      */     }
/*      */ 
/*  349 */     if (i > 16)
/*  350 */       j = 256;
/*  351 */     else if (i > 4)
/*  352 */       j = 16;
/*  353 */     else if (i > 2)
/*  354 */       j = 4;
/*      */     else {
/*  356 */       j = 2;
/*      */     }
/*      */ 
/*  359 */     this.metadata.PLTE_present = true;
/*  360 */     this.metadata.PLTE_red = new byte[j];
/*  361 */     this.metadata.PLTE_green = new byte[j];
/*  362 */     this.metadata.PLTE_blue = new byte[j];
/*      */ 
/*  364 */     int k = 0;
/*  365 */     for (int m = 0; m < i; m++) {
/*  366 */       this.metadata.PLTE_red[m] = arrayOfByte[(k++)];
/*  367 */       this.metadata.PLTE_green[m] = arrayOfByte[(k++)];
/*  368 */       this.metadata.PLTE_blue[m] = arrayOfByte[(k++)];
/*      */     }
/*      */   }
/*      */ 
/*      */   private void parse_bKGD_chunk() throws IOException {
/*  373 */     if (this.metadata.IHDR_colorType == 3) {
/*  374 */       this.metadata.bKGD_colorType = 3;
/*  375 */       this.metadata.bKGD_index = this.stream.readUnsignedByte();
/*  376 */     } else if ((this.metadata.IHDR_colorType == 0) || (this.metadata.IHDR_colorType == 4))
/*      */     {
/*  378 */       this.metadata.bKGD_colorType = 0;
/*  379 */       this.metadata.bKGD_gray = this.stream.readUnsignedShort();
/*      */     } else {
/*  381 */       this.metadata.bKGD_colorType = 2;
/*  382 */       this.metadata.bKGD_red = this.stream.readUnsignedShort();
/*  383 */       this.metadata.bKGD_green = this.stream.readUnsignedShort();
/*  384 */       this.metadata.bKGD_blue = this.stream.readUnsignedShort();
/*      */     }
/*      */ 
/*  387 */     this.metadata.bKGD_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_cHRM_chunk() throws IOException {
/*  391 */     this.metadata.cHRM_whitePointX = this.stream.readInt();
/*  392 */     this.metadata.cHRM_whitePointY = this.stream.readInt();
/*  393 */     this.metadata.cHRM_redX = this.stream.readInt();
/*  394 */     this.metadata.cHRM_redY = this.stream.readInt();
/*  395 */     this.metadata.cHRM_greenX = this.stream.readInt();
/*  396 */     this.metadata.cHRM_greenY = this.stream.readInt();
/*  397 */     this.metadata.cHRM_blueX = this.stream.readInt();
/*  398 */     this.metadata.cHRM_blueY = this.stream.readInt();
/*      */ 
/*  400 */     this.metadata.cHRM_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_gAMA_chunk() throws IOException {
/*  404 */     int i = this.stream.readInt();
/*  405 */     this.metadata.gAMA_gamma = i;
/*      */ 
/*  407 */     this.metadata.gAMA_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_hIST_chunk(int paramInt)
/*      */     throws IOException, IIOException
/*      */   {
/*  413 */     if (!this.metadata.PLTE_present) {
/*  414 */       throw new IIOException("hIST chunk without prior PLTE chunk!");
/*      */     }
/*      */ 
/*  422 */     this.metadata.hIST_histogram = new char[paramInt / 2];
/*  423 */     this.stream.readFully(this.metadata.hIST_histogram, 0, this.metadata.hIST_histogram.length);
/*      */ 
/*  426 */     this.metadata.hIST_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_iCCP_chunk(int paramInt) throws IOException {
/*  430 */     String str = readNullTerminatedString("ISO-8859-1", 80);
/*  431 */     this.metadata.iCCP_profileName = str;
/*      */ 
/*  433 */     this.metadata.iCCP_compressionMethod = this.stream.readUnsignedByte();
/*      */ 
/*  435 */     byte[] arrayOfByte = new byte[paramInt - str.length() - 2];
/*      */ 
/*  437 */     this.stream.readFully(arrayOfByte);
/*  438 */     this.metadata.iCCP_compressedProfile = arrayOfByte;
/*      */ 
/*  440 */     this.metadata.iCCP_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_iTXt_chunk(int paramInt) throws IOException {
/*  444 */     long l1 = this.stream.getStreamPosition();
/*      */ 
/*  446 */     String str1 = readNullTerminatedString("ISO-8859-1", 80);
/*  447 */     this.metadata.iTXt_keyword.add(str1);
/*      */ 
/*  449 */     int i = this.stream.readUnsignedByte();
/*  450 */     this.metadata.iTXt_compressionFlag.add(Boolean.valueOf(i == 1));
/*      */ 
/*  452 */     int j = this.stream.readUnsignedByte();
/*  453 */     this.metadata.iTXt_compressionMethod.add(Integer.valueOf(j));
/*      */ 
/*  455 */     String str2 = readNullTerminatedString("UTF8", 80);
/*  456 */     this.metadata.iTXt_languageTag.add(str2);
/*      */ 
/*  458 */     long l2 = this.stream.getStreamPosition();
/*  459 */     int k = (int)(l1 + paramInt - l2);
/*  460 */     String str3 = readNullTerminatedString("UTF8", k);
/*      */ 
/*  462 */     this.metadata.iTXt_translatedKeyword.add(str3);
/*      */ 
/*  465 */     l2 = this.stream.getStreamPosition();
/*  466 */     byte[] arrayOfByte = new byte[(int)(l1 + paramInt - l2)];
/*  467 */     this.stream.readFully(arrayOfByte);
/*      */     String str4;
/*  469 */     if (i == 1)
/*  470 */       str4 = new String(inflate(arrayOfByte), "UTF8");
/*      */     else {
/*  472 */       str4 = new String(arrayOfByte, "UTF8");
/*      */     }
/*  474 */     this.metadata.iTXt_text.add(str4);
/*      */   }
/*      */ 
/*      */   private void parse_pHYs_chunk() throws IOException {
/*  478 */     this.metadata.pHYs_pixelsPerUnitXAxis = this.stream.readInt();
/*  479 */     this.metadata.pHYs_pixelsPerUnitYAxis = this.stream.readInt();
/*  480 */     this.metadata.pHYs_unitSpecifier = this.stream.readUnsignedByte();
/*      */ 
/*  482 */     this.metadata.pHYs_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_sBIT_chunk() throws IOException {
/*  486 */     int i = this.metadata.IHDR_colorType;
/*  487 */     if ((i == 0) || (i == 4))
/*      */     {
/*  489 */       this.metadata.sBIT_grayBits = this.stream.readUnsignedByte();
/*  490 */     } else if ((i == 2) || (i == 3) || (i == 6))
/*      */     {
/*  493 */       this.metadata.sBIT_redBits = this.stream.readUnsignedByte();
/*  494 */       this.metadata.sBIT_greenBits = this.stream.readUnsignedByte();
/*  495 */       this.metadata.sBIT_blueBits = this.stream.readUnsignedByte();
/*      */     }
/*      */ 
/*  498 */     if ((i == 4) || (i == 6))
/*      */     {
/*  500 */       this.metadata.sBIT_alphaBits = this.stream.readUnsignedByte();
/*      */     }
/*      */ 
/*  503 */     this.metadata.sBIT_colorType = i;
/*  504 */     this.metadata.sBIT_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_sPLT_chunk(int paramInt) throws IOException, IIOException
/*      */   {
/*  509 */     this.metadata.sPLT_paletteName = readNullTerminatedString("ISO-8859-1", 80);
/*  510 */     paramInt -= this.metadata.sPLT_paletteName.length() + 1;
/*      */ 
/*  512 */     int i = this.stream.readUnsignedByte();
/*  513 */     this.metadata.sPLT_sampleDepth = i;
/*      */ 
/*  515 */     int j = paramInt / (4 * (i / 8) + 2);
/*  516 */     this.metadata.sPLT_red = new int[j];
/*  517 */     this.metadata.sPLT_green = new int[j];
/*  518 */     this.metadata.sPLT_blue = new int[j];
/*  519 */     this.metadata.sPLT_alpha = new int[j];
/*  520 */     this.metadata.sPLT_frequency = new int[j];
/*      */     int k;
/*  522 */     if (i == 8)
/*  523 */       for (k = 0; k < j; k++) {
/*  524 */         this.metadata.sPLT_red[k] = this.stream.readUnsignedByte();
/*  525 */         this.metadata.sPLT_green[k] = this.stream.readUnsignedByte();
/*  526 */         this.metadata.sPLT_blue[k] = this.stream.readUnsignedByte();
/*  527 */         this.metadata.sPLT_alpha[k] = this.stream.readUnsignedByte();
/*  528 */         this.metadata.sPLT_frequency[k] = this.stream.readUnsignedShort();
/*      */       }
/*  530 */     else if (i == 16)
/*  531 */       for (k = 0; k < j; k++) {
/*  532 */         this.metadata.sPLT_red[k] = this.stream.readUnsignedShort();
/*  533 */         this.metadata.sPLT_green[k] = this.stream.readUnsignedShort();
/*  534 */         this.metadata.sPLT_blue[k] = this.stream.readUnsignedShort();
/*  535 */         this.metadata.sPLT_alpha[k] = this.stream.readUnsignedShort();
/*  536 */         this.metadata.sPLT_frequency[k] = this.stream.readUnsignedShort();
/*      */       }
/*      */     else {
/*  539 */       throw new IIOException("sPLT sample depth not 8 or 16!");
/*      */     }
/*      */ 
/*  542 */     this.metadata.sPLT_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_sRGB_chunk() throws IOException {
/*  546 */     this.metadata.sRGB_renderingIntent = this.stream.readUnsignedByte();
/*      */ 
/*  548 */     this.metadata.sRGB_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_tEXt_chunk(int paramInt) throws IOException {
/*  552 */     String str = readNullTerminatedString("ISO-8859-1", 80);
/*  553 */     this.metadata.tEXt_keyword.add(str);
/*      */ 
/*  555 */     byte[] arrayOfByte = new byte[paramInt - str.length() - 1];
/*  556 */     this.stream.readFully(arrayOfByte);
/*  557 */     this.metadata.tEXt_text.add(new String(arrayOfByte, "ISO-8859-1"));
/*      */   }
/*      */ 
/*      */   private void parse_tIME_chunk() throws IOException {
/*  561 */     this.metadata.tIME_year = this.stream.readUnsignedShort();
/*  562 */     this.metadata.tIME_month = this.stream.readUnsignedByte();
/*  563 */     this.metadata.tIME_day = this.stream.readUnsignedByte();
/*  564 */     this.metadata.tIME_hour = this.stream.readUnsignedByte();
/*  565 */     this.metadata.tIME_minute = this.stream.readUnsignedByte();
/*  566 */     this.metadata.tIME_second = this.stream.readUnsignedByte();
/*      */ 
/*  568 */     this.metadata.tIME_present = true;
/*      */   }
/*      */ 
/*      */   private void parse_tRNS_chunk(int paramInt) throws IOException {
/*  572 */     int i = this.metadata.IHDR_colorType;
/*  573 */     if (i == 3) {
/*  574 */       if (!this.metadata.PLTE_present) {
/*  575 */         processWarningOccurred("tRNS chunk without prior PLTE chunk, ignoring it.");
/*      */ 
/*  577 */         return;
/*      */       }
/*      */ 
/*  581 */       int j = this.metadata.PLTE_red.length;
/*  582 */       int k = paramInt;
/*  583 */       if (k > j) {
/*  584 */         processWarningOccurred("tRNS chunk has more entries than prior PLTE chunk, ignoring extras.");
/*      */ 
/*  586 */         k = j;
/*      */       }
/*  588 */       this.metadata.tRNS_alpha = new byte[k];
/*  589 */       this.metadata.tRNS_colorType = 3;
/*  590 */       this.stream.read(this.metadata.tRNS_alpha, 0, k);
/*  591 */       this.stream.skipBytes(paramInt - k);
/*  592 */     } else if (i == 0) {
/*  593 */       if (paramInt != 2) {
/*  594 */         processWarningOccurred("tRNS chunk for gray image must have length 2, ignoring chunk.");
/*      */ 
/*  596 */         this.stream.skipBytes(paramInt);
/*  597 */         return;
/*      */       }
/*  599 */       this.metadata.tRNS_gray = this.stream.readUnsignedShort();
/*  600 */       this.metadata.tRNS_colorType = 0;
/*  601 */     } else if (i == 2) {
/*  602 */       if (paramInt != 6) {
/*  603 */         processWarningOccurred("tRNS chunk for RGB image must have length 6, ignoring chunk.");
/*      */ 
/*  605 */         this.stream.skipBytes(paramInt);
/*  606 */         return;
/*      */       }
/*  608 */       this.metadata.tRNS_red = this.stream.readUnsignedShort();
/*  609 */       this.metadata.tRNS_green = this.stream.readUnsignedShort();
/*  610 */       this.metadata.tRNS_blue = this.stream.readUnsignedShort();
/*  611 */       this.metadata.tRNS_colorType = 2;
/*      */     } else {
/*  613 */       processWarningOccurred("Gray+Alpha and RGBS images may not have a tRNS chunk, ignoring it.");
/*      */ 
/*  615 */       return;
/*      */     }
/*      */ 
/*  618 */     this.metadata.tRNS_present = true;
/*      */   }
/*      */ 
/*      */   private static byte[] inflate(byte[] paramArrayOfByte) throws IOException {
/*  622 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*  623 */     InflaterInputStream localInflaterInputStream = new InflaterInputStream(localByteArrayInputStream);
/*  624 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*      */     try
/*      */     {
/*      */       int i;
/*  628 */       while ((i = localInflaterInputStream.read()) != -1)
/*  629 */         localByteArrayOutputStream.write(i);
/*      */     }
/*      */     finally {
/*  632 */       localInflaterInputStream.close();
/*      */     }
/*  634 */     return localByteArrayOutputStream.toByteArray();
/*      */   }
/*      */ 
/*      */   private void parse_zTXt_chunk(int paramInt) throws IOException {
/*  638 */     String str = readNullTerminatedString("ISO-8859-1", 80);
/*  639 */     this.metadata.zTXt_keyword.add(str);
/*      */ 
/*  641 */     int i = this.stream.readUnsignedByte();
/*  642 */     this.metadata.zTXt_compressionMethod.add(new Integer(i));
/*      */ 
/*  644 */     byte[] arrayOfByte = new byte[paramInt - str.length() - 2];
/*  645 */     this.stream.readFully(arrayOfByte);
/*  646 */     this.metadata.zTXt_text.add(new String(inflate(arrayOfByte), "ISO-8859-1"));
/*      */   }
/*      */ 
/*      */   private void readMetadata() throws IIOException {
/*  650 */     if (this.gotMetadata) {
/*  651 */       return;
/*      */     }
/*      */ 
/*  654 */     readHeader();
/*      */ 
/*  662 */     int i = this.metadata.IHDR_colorType;
/*      */     int m;
/*  663 */     if ((this.ignoreMetadata) && (i != 3)) {
/*      */       try {
/*      */         while (true) {
/*  666 */           int j = this.stream.readInt();
/*  667 */           m = this.stream.readInt();
/*      */ 
/*  669 */           if (m == 1229209940)
/*      */           {
/*  671 */             this.stream.skipBytes(-8);
/*  672 */             this.imageStartPosition = this.stream.getStreamPosition();
/*  673 */             break;
/*      */           }
/*      */ 
/*  676 */           this.stream.skipBytes(j + 4);
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException1) {
/*  680 */         throw new IIOException("Error skipping PNG metadata", localIOException1);
/*      */       }
/*      */ 
/*  683 */       this.gotMetadata = true;
/*  684 */       return;
/*      */     }
/*      */     try
/*      */     {
/*      */       while (true) {
/*  689 */         int k = this.stream.readInt();
/*  690 */         m = this.stream.readInt();
/*      */ 
/*  692 */         switch (m)
/*      */         {
/*      */         case 1229209940:
/*  695 */           this.stream.skipBytes(-8);
/*  696 */           this.imageStartPosition = this.stream.getStreamPosition();
/*  697 */           break;
/*      */         case 1347179589:
/*  699 */           parse_PLTE_chunk(k);
/*  700 */           break;
/*      */         case 1649100612:
/*  702 */           parse_bKGD_chunk();
/*  703 */           break;
/*      */         case 1665684045:
/*  705 */           parse_cHRM_chunk();
/*  706 */           break;
/*      */         case 1732332865:
/*  708 */           parse_gAMA_chunk();
/*  709 */           break;
/*      */         case 1749635924:
/*  711 */           parse_hIST_chunk(k);
/*  712 */           break;
/*      */         case 1766015824:
/*  714 */           parse_iCCP_chunk(k);
/*  715 */           break;
/*      */         case 1767135348:
/*  717 */           parse_iTXt_chunk(k);
/*  718 */           break;
/*      */         case 1883789683:
/*  720 */           parse_pHYs_chunk();
/*  721 */           break;
/*      */         case 1933723988:
/*  723 */           parse_sBIT_chunk();
/*  724 */           break;
/*      */         case 1934642260:
/*  726 */           parse_sPLT_chunk(k);
/*  727 */           break;
/*      */         case 1934772034:
/*  729 */           parse_sRGB_chunk();
/*  730 */           break;
/*      */         case 1950701684:
/*  732 */           parse_tEXt_chunk(k);
/*  733 */           break;
/*      */         case 1950960965:
/*  735 */           parse_tIME_chunk();
/*  736 */           break;
/*      */         case 1951551059:
/*  738 */           parse_tRNS_chunk(k);
/*  739 */           break;
/*      */         case 2052348020:
/*  741 */           parse_zTXt_chunk(k);
/*  742 */           break;
/*      */         default:
/*  745 */           byte[] arrayOfByte = new byte[k];
/*  746 */           this.stream.readFully(arrayOfByte);
/*      */ 
/*  748 */           StringBuilder localStringBuilder = new StringBuilder(4);
/*  749 */           localStringBuilder.append((char)(m >>> 24));
/*  750 */           localStringBuilder.append((char)(m >> 16 & 0xFF));
/*  751 */           localStringBuilder.append((char)(m >> 8 & 0xFF));
/*  752 */           localStringBuilder.append((char)(m & 0xFF));
/*      */ 
/*  754 */           int i1 = m >>> 28;
/*  755 */           if (i1 == 0) {
/*  756 */             processWarningOccurred("Encountered unknown chunk with critical bit set!");
/*      */           }
/*      */ 
/*  760 */           this.metadata.unknownChunkType.add(localStringBuilder.toString());
/*  761 */           this.metadata.unknownChunkData.add(arrayOfByte);
/*      */         }
/*      */ 
/*  765 */         int n = this.stream.readInt();
/*  766 */         this.stream.flushBefore(this.stream.getStreamPosition());
/*      */       }
/*      */     } catch (IOException localIOException2) {
/*  769 */       throw new IIOException("Error reading PNG metadata", localIOException2);
/*      */     }
/*      */ 
/*  772 */     this.gotMetadata = true;
/*      */   }
/*      */ 
/*      */   private static void decodeSubFilter(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  779 */     for (int i = paramInt3; i < paramInt2; i++)
/*      */     {
/*  782 */       int j = paramArrayOfByte[(i + paramInt1)] & 0xFF;
/*  783 */       j += (paramArrayOfByte[(i + paramInt1 - paramInt3)] & 0xFF);
/*      */ 
/*  785 */       paramArrayOfByte[(i + paramInt1)] = ((byte)j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void decodeUpFilter(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
/*      */   {
/*  792 */     for (int i = 0; i < paramInt3; i++) {
/*  793 */       int j = paramArrayOfByte1[(i + paramInt1)] & 0xFF;
/*  794 */       int k = paramArrayOfByte2[(i + paramInt2)] & 0xFF;
/*      */ 
/*  796 */       paramArrayOfByte1[(i + paramInt1)] = ((byte)(j + k));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void decodeAverageFilter(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*      */     int i;
/*      */     int k;
/*  805 */     for (int m = 0; m < paramInt4; m++) {
/*  806 */       i = paramArrayOfByte1[(m + paramInt1)] & 0xFF;
/*  807 */       k = paramArrayOfByte2[(m + paramInt2)] & 0xFF;
/*      */ 
/*  809 */       paramArrayOfByte1[(m + paramInt1)] = ((byte)(i + k / 2));
/*      */     }
/*      */ 
/*  812 */     for (m = paramInt4; m < paramInt3; m++) {
/*  813 */       i = paramArrayOfByte1[(m + paramInt1)] & 0xFF;
/*  814 */       int j = paramArrayOfByte1[(m + paramInt1 - paramInt4)] & 0xFF;
/*  815 */       k = paramArrayOfByte2[(m + paramInt2)] & 0xFF;
/*      */ 
/*  817 */       paramArrayOfByte1[(m + paramInt1)] = ((byte)(i + (j + k) / 2));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int paethPredictor(int paramInt1, int paramInt2, int paramInt3) {
/*  822 */     int i = paramInt1 + paramInt2 - paramInt3;
/*  823 */     int j = Math.abs(i - paramInt1);
/*  824 */     int k = Math.abs(i - paramInt2);
/*  825 */     int m = Math.abs(i - paramInt3);
/*      */ 
/*  827 */     if ((j <= k) && (j <= m))
/*  828 */       return paramInt1;
/*  829 */     if (k <= m) {
/*  830 */       return paramInt2;
/*      */     }
/*  832 */     return paramInt3;
/*      */   }
/*      */ 
/*      */   private static void decodePaethFilter(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*      */     int i;
/*      */     int k;
/*  841 */     for (int n = 0; n < paramInt4; n++) {
/*  842 */       i = paramArrayOfByte1[(n + paramInt1)] & 0xFF;
/*  843 */       k = paramArrayOfByte2[(n + paramInt2)] & 0xFF;
/*      */ 
/*  845 */       paramArrayOfByte1[(n + paramInt1)] = ((byte)(i + k));
/*      */     }
/*      */ 
/*  848 */     for (n = paramInt4; n < paramInt3; n++) {
/*  849 */       i = paramArrayOfByte1[(n + paramInt1)] & 0xFF;
/*  850 */       int j = paramArrayOfByte1[(n + paramInt1 - paramInt4)] & 0xFF;
/*  851 */       k = paramArrayOfByte2[(n + paramInt2)] & 0xFF;
/*  852 */       int m = paramArrayOfByte2[(n + paramInt2 - paramInt4)] & 0xFF;
/*      */ 
/*  854 */       paramArrayOfByte1[(n + paramInt1)] = ((byte)(i + paethPredictor(j, k, m)));
/*      */     }
/*      */   }
/*      */ 
/*      */   private WritableRaster createRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  873 */     WritableRaster localWritableRaster = null;
/*  874 */     Point localPoint = new Point(0, 0);
/*      */     Object localObject;
/*  875 */     if ((paramInt5 < 8) && (paramInt3 == 1)) {
/*  876 */       localObject = new DataBufferByte(paramInt2 * paramInt4);
/*  877 */       localWritableRaster = Raster.createPackedRaster((DataBuffer)localObject, paramInt1, paramInt2, paramInt5, localPoint);
/*      */     }
/*  881 */     else if (paramInt5 <= 8) {
/*  882 */       localObject = new DataBufferByte(paramInt2 * paramInt4);
/*  883 */       localWritableRaster = Raster.createInterleavedRaster((DataBuffer)localObject, paramInt1, paramInt2, paramInt4, paramInt3, bandOffsets[paramInt3], localPoint);
/*      */     }
/*      */     else
/*      */     {
/*  890 */       localObject = new DataBufferUShort(paramInt2 * paramInt4);
/*  891 */       localWritableRaster = Raster.createInterleavedRaster((DataBuffer)localObject, paramInt1, paramInt2, paramInt4, paramInt3, bandOffsets[paramInt3], localPoint);
/*      */     }
/*      */ 
/*  899 */     return localWritableRaster;
/*      */   }
/*      */ 
/*      */   private void skipPass(int paramInt1, int paramInt2) throws IOException, IIOException
/*      */   {
/*  904 */     if ((paramInt1 == 0) || (paramInt2 == 0)) {
/*  905 */       return;
/*      */     }
/*      */ 
/*  908 */     int i = inputBandsForColorType[this.metadata.IHDR_colorType];
/*  909 */     int j = (i * paramInt1 * this.metadata.IHDR_bitDepth + 7) / 8;
/*      */ 
/*  912 */     for (int k = 0; k < paramInt2; k++)
/*      */     {
/*  914 */       this.pixelStream.skipBytes(1 + j);
/*      */ 
/*  918 */       if (abortRequested())
/*  919 */         return;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateImageProgress(int paramInt)
/*      */   {
/*  925 */     this.pixelsDone += paramInt;
/*  926 */     processImageProgress(100.0F * this.pixelsDone / this.totalPixels);
/*      */   }
/*      */ 
/*      */   private void decodePass(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*      */     throws IOException
/*      */   {
/*  934 */     if ((paramInt6 == 0) || (paramInt7 == 0)) {
/*  935 */       return;
/*      */     }
/*      */ 
/*  938 */     WritableRaster localWritableRaster1 = this.theImage.getWritableTile(0, 0);
/*  939 */     int i = localWritableRaster1.getMinX();
/*  940 */     int j = i + localWritableRaster1.getWidth() - 1;
/*  941 */     int k = localWritableRaster1.getMinY();
/*  942 */     int m = k + localWritableRaster1.getHeight() - 1;
/*      */ 
/*  945 */     int[] arrayOfInt1 = ReaderUtil.computeUpdatedPixels(this.sourceRegion, this.destinationOffset, i, k, j, m, this.sourceXSubsampling, this.sourceYSubsampling, paramInt2, paramInt3, paramInt6, paramInt7, paramInt4, paramInt5);
/*      */ 
/*  955 */     int n = arrayOfInt1[0];
/*  956 */     int i1 = arrayOfInt1[1];
/*  957 */     int i2 = arrayOfInt1[2];
/*  958 */     int i3 = arrayOfInt1[4];
/*  959 */     int i4 = arrayOfInt1[5];
/*      */ 
/*  961 */     int i5 = this.metadata.IHDR_bitDepth;
/*  962 */     int i6 = inputBandsForColorType[this.metadata.IHDR_colorType];
/*  963 */     int i7 = i5 == 16 ? 2 : 1;
/*  964 */     i7 *= i6;
/*      */ 
/*  966 */     int i8 = (i6 * paramInt6 * i5 + 7) / 8;
/*  967 */     int i9 = i5 == 16 ? i8 / 2 : i8;
/*      */ 
/*  970 */     if (i2 == 0) {
/*  971 */       for (i10 = 0; i10 < paramInt7; i10++)
/*      */       {
/*  973 */         updateImageProgress(paramInt6);
/*      */ 
/*  975 */         this.pixelStream.skipBytes(1 + i8);
/*      */       }
/*  977 */       return;
/*      */     }
/*      */ 
/*  984 */     int i10 = (n - this.destinationOffset.x) * this.sourceXSubsampling + this.sourceRegion.x;
/*      */ 
/*  987 */     int i11 = (i10 - paramInt2) / paramInt4;
/*      */ 
/*  990 */     int i12 = i3 * this.sourceXSubsampling / paramInt4;
/*      */ 
/*  992 */     byte[] arrayOfByte = null;
/*  993 */     short[] arrayOfShort = null;
/*  994 */     Object localObject1 = new byte[i8];
/*  995 */     Object localObject2 = new byte[i8];
/*      */ 
/*  998 */     WritableRaster localWritableRaster2 = createRaster(paramInt6, 1, i6, i9, i5);
/*      */ 
/* 1003 */     int[] arrayOfInt2 = localWritableRaster2.getPixel(0, 0, (int[])null);
/*      */ 
/* 1005 */     DataBuffer localDataBuffer = localWritableRaster2.getDataBuffer();
/* 1006 */     int i13 = localDataBuffer.getDataType();
/* 1007 */     if (i13 == 0)
/* 1008 */       arrayOfByte = ((DataBufferByte)localDataBuffer).getData();
/*      */     else {
/* 1010 */       arrayOfShort = ((DataBufferUShort)localDataBuffer).getData();
/*      */     }
/*      */ 
/* 1013 */     processPassStarted(this.theImage, paramInt1, this.sourceMinProgressivePass, this.sourceMaxProgressivePass, n, i1, i3, i4, this.destinationBands);
/*      */ 
/* 1022 */     if (this.sourceBands != null) {
/* 1023 */       localWritableRaster2 = localWritableRaster2.createWritableChild(0, 0, localWritableRaster2.getWidth(), 1, 0, 0, this.sourceBands);
/*      */     }
/*      */ 
/* 1028 */     if (this.destinationBands != null) {
/* 1029 */       localWritableRaster1 = localWritableRaster1.createWritableChild(0, 0, localWritableRaster1.getWidth(), localWritableRaster1.getHeight(), 0, 0, this.destinationBands);
/*      */     }
/*      */ 
/* 1038 */     int i14 = 0;
/* 1039 */     int[] arrayOfInt3 = localWritableRaster1.getSampleModel().getSampleSize();
/* 1040 */     int i15 = arrayOfInt3.length;
/* 1041 */     for (int i16 = 0; i16 < i15; i16++) {
/* 1042 */       if (arrayOfInt3[i16] != i5) {
/* 1043 */         i14 = 1;
/* 1044 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1050 */     Object localObject3 = (int[][])null;
/*      */     int i19;
/*      */     int i22;
/* 1051 */     if (i14 != 0) {
/* 1052 */       i17 = (1 << i5) - 1;
/* 1053 */       i18 = i17 / 2;
/* 1054 */       localObject3 = new int[i15][];
/* 1055 */       for (i19 = 0; i19 < i15; i19++) {
/* 1056 */         int i20 = (1 << arrayOfInt3[i19]) - 1;
/* 1057 */         localObject3[i19] = new int[i17 + 1];
/* 1058 */         for (i22 = 0; i22 <= i17; i22++) {
/* 1059 */           localObject3[i19][i22] = ((i22 * i20 + i18) / i17);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1067 */     int i17 = (i12 == 1) && (i3 == 1) && (i14 == 0) && ((localWritableRaster1 instanceof ByteInterleavedRaster)) ? 1 : 0;
/*      */ 
/* 1072 */     if (i17 != 0) {
/* 1073 */       localWritableRaster2 = localWritableRaster2.createWritableChild(i11, 0, i2, 1, 0, 0, null);
/*      */     }
/*      */ 
/* 1080 */     for (int i18 = 0; i18 < paramInt7; i18++)
/*      */     {
/* 1082 */       updateImageProgress(paramInt6);
/*      */ 
/* 1085 */       i19 = this.pixelStream.read();
/*      */       try
/*      */       {
/* 1088 */         Object localObject4 = localObject2;
/* 1089 */         localObject2 = localObject1;
/* 1090 */         localObject1 = localObject4;
/*      */ 
/* 1092 */         this.pixelStream.readFully((byte[])localObject1, 0, i8);
/*      */       }
/*      */       catch (ZipException localZipException) {
/* 1095 */         throw localZipException;
/*      */       }
/*      */ 
/* 1098 */       switch (i19) {
/*      */       case 0:
/* 1100 */         break;
/*      */       case 1:
/* 1102 */         decodeSubFilter((byte[])localObject1, 0, i8, i7);
/* 1103 */         break;
/*      */       case 2:
/* 1105 */         decodeUpFilter((byte[])localObject1, 0, (byte[])localObject2, 0, i8);
/* 1106 */         break;
/*      */       case 3:
/* 1108 */         decodeAverageFilter((byte[])localObject1, 0, (byte[])localObject2, 0, i8, i7);
/*      */ 
/* 1110 */         break;
/*      */       case 4:
/* 1112 */         decodePaethFilter((byte[])localObject1, 0, (byte[])localObject2, 0, i8, i7);
/*      */ 
/* 1114 */         break;
/*      */       default:
/* 1116 */         throw new IIOException("Unknown row filter type (= " + i19 + ")!");
/*      */       }
/*      */ 
/* 1121 */       if (i5 < 16) {
/* 1122 */         System.arraycopy(localObject1, 0, arrayOfByte, 0, i8);
/*      */       } else {
/* 1124 */         i21 = 0;
/* 1125 */         for (i22 = 0; i22 < i9; i22++) {
/* 1126 */           arrayOfShort[i22] = ((short)(localObject1[i21] << 8 | localObject1[(i21 + 1)] & 0xFF));
/*      */ 
/* 1128 */           i21 += 2;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1133 */       int i21 = i18 * paramInt5 + paramInt3;
/* 1134 */       if ((i21 >= this.sourceRegion.y) && (i21 < this.sourceRegion.y + this.sourceRegion.height) && ((i21 - this.sourceRegion.y) % this.sourceYSubsampling == 0))
/*      */       {
/* 1139 */         i22 = this.destinationOffset.y + (i21 - this.sourceRegion.y) / this.sourceYSubsampling;
/*      */ 
/* 1141 */         if (i22 >= k)
/*      */         {
/* 1144 */           if (i22 > m)
/*      */           {
/*      */             break;
/*      */           }
/* 1148 */           if (i17 != 0) {
/* 1149 */             localWritableRaster1.setRect(n, i22, localWritableRaster2);
/*      */           } else {
/* 1151 */             int i23 = i11;
/*      */ 
/* 1153 */             int i24 = n;
/*      */ 
/* 1155 */             for (; i24 < n + i2; 
/* 1155 */               i24 += i3)
/*      */             {
/* 1157 */               localWritableRaster2.getPixel(i23, 0, arrayOfInt2);
/* 1158 */               if (i14 != 0) {
/* 1159 */                 for (int i25 = 0; i25 < i15; i25++) {
/* 1160 */                   arrayOfInt2[i25] = localObject3[i25][arrayOfInt2[i25]];
/*      */                 }
/*      */               }
/* 1163 */               localWritableRaster1.setPixel(i24, i22, arrayOfInt2);
/* 1164 */               i23 += i12;
/*      */             }
/*      */           }
/*      */ 
/* 1168 */           processImageUpdate(this.theImage, n, i22, i2, 1, i3, i4, this.destinationBands);
/*      */ 
/* 1176 */           if (abortRequested()) {
/* 1177 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1182 */     processPassComplete(this.theImage);
/*      */   }
/*      */ 
/*      */   private void decodeImage() throws IOException, IIOException
/*      */   {
/* 1187 */     int i = this.metadata.IHDR_width;
/* 1188 */     int j = this.metadata.IHDR_height;
/*      */ 
/* 1190 */     this.pixelsDone = 0;
/* 1191 */     this.totalPixels = (i * j);
/*      */ 
/* 1193 */     clearAbortRequest();
/*      */ 
/* 1195 */     if (this.metadata.IHDR_interlaceMethod == 0)
/* 1196 */       decodePass(0, 0, 0, 1, 1, i, j);
/*      */     else
/* 1198 */       for (int k = 0; k <= this.sourceMaxProgressivePass; k++) {
/* 1199 */         int m = adam7XOffset[k];
/* 1200 */         int n = adam7YOffset[k];
/* 1201 */         int i1 = adam7XSubsampling[k];
/* 1202 */         int i2 = adam7YSubsampling[k];
/* 1203 */         int i3 = adam7XSubsampling[(k + 1)] - 1;
/* 1204 */         int i4 = adam7YSubsampling[(k + 1)] - 1;
/*      */ 
/* 1206 */         if (k >= this.sourceMinProgressivePass) {
/* 1207 */           decodePass(k, m, n, i1, i2, (i + i3) / i1, (j + i4) / i2);
/*      */         }
/*      */         else
/*      */         {
/* 1215 */           skipPass((i + i3) / i1, (j + i4) / i2);
/*      */         }
/*      */ 
/* 1221 */         if (abortRequested())
/* 1222 */           return;
/*      */       }
/*      */   }
/*      */ 
/*      */   private void readImage(ImageReadParam paramImageReadParam)
/*      */     throws IIOException
/*      */   {
/* 1229 */     readMetadata();
/*      */ 
/* 1231 */     int i = this.metadata.IHDR_width;
/* 1232 */     int j = this.metadata.IHDR_height;
/*      */ 
/* 1235 */     this.sourceXSubsampling = 1;
/* 1236 */     this.sourceYSubsampling = 1;
/* 1237 */     this.sourceMinProgressivePass = 0;
/* 1238 */     this.sourceMaxProgressivePass = 6;
/* 1239 */     this.sourceBands = null;
/* 1240 */     this.destinationBands = null;
/* 1241 */     this.destinationOffset = new Point(0, 0);
/*      */ 
/* 1244 */     if (paramImageReadParam != null) {
/* 1245 */       this.sourceXSubsampling = paramImageReadParam.getSourceXSubsampling();
/* 1246 */       this.sourceYSubsampling = paramImageReadParam.getSourceYSubsampling();
/*      */ 
/* 1248 */       this.sourceMinProgressivePass = Math.max(paramImageReadParam.getSourceMinProgressivePass(), 0);
/*      */ 
/* 1250 */       this.sourceMaxProgressivePass = Math.min(paramImageReadParam.getSourceMaxProgressivePass(), 6);
/*      */ 
/* 1253 */       this.sourceBands = paramImageReadParam.getSourceBands();
/* 1254 */       this.destinationBands = paramImageReadParam.getDestinationBands();
/* 1255 */       this.destinationOffset = paramImageReadParam.getDestinationOffset();
/*      */     }
/* 1257 */     Inflater localInflater = null;
/*      */     try {
/* 1259 */       this.stream.seek(this.imageStartPosition);
/*      */ 
/* 1261 */       PNGImageDataEnumeration localPNGImageDataEnumeration = new PNGImageDataEnumeration(this.stream);
/* 1262 */       Object localObject1 = new SequenceInputStream(localPNGImageDataEnumeration);
/*      */ 
/* 1275 */       localInflater = new Inflater();
/* 1276 */       localObject1 = new InflaterInputStream((InputStream)localObject1, localInflater);
/* 1277 */       localObject1 = new BufferedInputStream((InputStream)localObject1);
/* 1278 */       this.pixelStream = new DataInputStream((InputStream)localObject1);
/*      */ 
/* 1280 */       this.theImage = getDestination(paramImageReadParam, getImageTypes(0), i, j);
/*      */ 
/* 1285 */       Rectangle localRectangle = new Rectangle(0, 0, 0, 0);
/* 1286 */       this.sourceRegion = new Rectangle(0, 0, 0, 0);
/* 1287 */       computeRegions(paramImageReadParam, i, j, this.theImage, this.sourceRegion, localRectangle);
/*      */ 
/* 1290 */       this.destinationOffset.setLocation(localRectangle.getLocation());
/*      */ 
/* 1295 */       int k = this.metadata.IHDR_colorType;
/* 1296 */       checkReadParamBandSettings(paramImageReadParam, inputBandsForColorType[k], this.theImage.getSampleModel().getNumBands());
/*      */ 
/* 1300 */       processImageStarted(0);
/* 1301 */       decodeImage();
/* 1302 */       if (abortRequested())
/* 1303 */         processReadAborted();
/*      */       else
/* 1305 */         processImageComplete();
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1308 */       throw new IIOException("Error reading PNG image data", localIOException);
/*      */     } finally {
/* 1310 */       if (localInflater != null)
/* 1311 */         localInflater.end();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getNumImages(boolean paramBoolean) throws IIOException
/*      */   {
/* 1317 */     if (this.stream == null) {
/* 1318 */       throw new IllegalStateException("No input source set!");
/*      */     }
/* 1320 */     if ((this.seekForwardOnly) && (paramBoolean)) {
/* 1321 */       throw new IllegalStateException("seekForwardOnly and allowSearch can't both be true!");
/*      */     }
/*      */ 
/* 1324 */     return 1;
/*      */   }
/*      */ 
/*      */   public int getWidth(int paramInt) throws IIOException {
/* 1328 */     if (paramInt != 0) {
/* 1329 */       throw new IndexOutOfBoundsException("imageIndex != 0!");
/*      */     }
/*      */ 
/* 1332 */     readHeader();
/*      */ 
/* 1334 */     return this.metadata.IHDR_width;
/*      */   }
/*      */ 
/*      */   public int getHeight(int paramInt) throws IIOException {
/* 1338 */     if (paramInt != 0) {
/* 1339 */       throw new IndexOutOfBoundsException("imageIndex != 0!");
/*      */     }
/*      */ 
/* 1342 */     readHeader();
/*      */ 
/* 1344 */     return this.metadata.IHDR_height;
/*      */   }
/*      */ 
/*      */   public Iterator<ImageTypeSpecifier> getImageTypes(int paramInt)
/*      */     throws IIOException
/*      */   {
/* 1350 */     if (paramInt != 0) {
/* 1351 */       throw new IndexOutOfBoundsException("imageIndex != 0!");
/*      */     }
/*      */ 
/* 1354 */     readHeader();
/*      */ 
/* 1356 */     ArrayList localArrayList = new ArrayList(1);
/*      */ 
/* 1363 */     int i = this.metadata.IHDR_bitDepth;
/* 1364 */     int j = this.metadata.IHDR_colorType;
/*      */     int k;
/* 1367 */     if (i <= 8)
/* 1368 */       k = 0;
/*      */     else
/* 1370 */       k = 1;
/*      */     ColorSpace localColorSpace1;
/*      */     int[] arrayOfInt;
/* 1373 */     switch (j)
/*      */     {
/*      */     case 0:
/* 1376 */       localArrayList.add(ImageTypeSpecifier.createGrayscale(i, k, false));
/*      */ 
/* 1379 */       break;
/*      */     case 2:
/* 1382 */       if (i == 8)
/*      */       {
/* 1385 */         localArrayList.add(ImageTypeSpecifier.createFromBufferedImageType(5));
/*      */ 
/* 1388 */         localArrayList.add(ImageTypeSpecifier.createFromBufferedImageType(1));
/*      */ 
/* 1391 */         localArrayList.add(ImageTypeSpecifier.createFromBufferedImageType(4));
/*      */       }
/*      */ 
/* 1396 */       localColorSpace1 = ColorSpace.getInstance(1000);
/* 1397 */       arrayOfInt = new int[3];
/* 1398 */       arrayOfInt[0] = 0;
/* 1399 */       arrayOfInt[1] = 1;
/* 1400 */       arrayOfInt[2] = 2;
/* 1401 */       localArrayList.add(ImageTypeSpecifier.createInterleaved(localColorSpace1, arrayOfInt, k, false, false));
/*      */ 
/* 1406 */       break;
/*      */     case 3:
/* 1409 */       readMetadata();
/*      */ 
/* 1435 */       int m = 1 << i;
/*      */ 
/* 1437 */       byte[] arrayOfByte1 = this.metadata.PLTE_red;
/* 1438 */       byte[] arrayOfByte2 = this.metadata.PLTE_green;
/* 1439 */       byte[] arrayOfByte3 = this.metadata.PLTE_blue;
/*      */ 
/* 1441 */       if (this.metadata.PLTE_red.length < m) {
/* 1442 */         arrayOfByte1 = Arrays.copyOf(this.metadata.PLTE_red, m);
/* 1443 */         Arrays.fill(arrayOfByte1, this.metadata.PLTE_red.length, m, this.metadata.PLTE_red[(this.metadata.PLTE_red.length - 1)]);
/*      */ 
/* 1446 */         arrayOfByte2 = Arrays.copyOf(this.metadata.PLTE_green, m);
/* 1447 */         Arrays.fill(arrayOfByte2, this.metadata.PLTE_green.length, m, this.metadata.PLTE_green[(this.metadata.PLTE_green.length - 1)]);
/*      */ 
/* 1450 */         arrayOfByte3 = Arrays.copyOf(this.metadata.PLTE_blue, m);
/* 1451 */         Arrays.fill(arrayOfByte3, this.metadata.PLTE_blue.length, m, this.metadata.PLTE_blue[(this.metadata.PLTE_blue.length - 1)]);
/*      */       }
/*      */ 
/* 1459 */       byte[] arrayOfByte4 = null;
/* 1460 */       if ((this.metadata.tRNS_present) && (this.metadata.tRNS_alpha != null)) {
/* 1461 */         if (this.metadata.tRNS_alpha.length == arrayOfByte1.length) {
/* 1462 */           arrayOfByte4 = this.metadata.tRNS_alpha;
/*      */         } else {
/* 1464 */           arrayOfByte4 = Arrays.copyOf(this.metadata.tRNS_alpha, arrayOfByte1.length);
/* 1465 */           Arrays.fill(arrayOfByte4, this.metadata.tRNS_alpha.length, arrayOfByte1.length, (byte)-1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1471 */       localArrayList.add(ImageTypeSpecifier.createIndexed(arrayOfByte1, arrayOfByte2, arrayOfByte3, arrayOfByte4, i, 0));
/*      */ 
/* 1475 */       break;
/*      */     case 4:
/* 1479 */       ColorSpace localColorSpace2 = ColorSpace.getInstance(1003);
/* 1480 */       arrayOfInt = new int[2];
/* 1481 */       arrayOfInt[0] = 0;
/* 1482 */       arrayOfInt[1] = 1;
/* 1483 */       localArrayList.add(ImageTypeSpecifier.createInterleaved(localColorSpace2, arrayOfInt, k, true, false));
/*      */ 
/* 1488 */       break;
/*      */     case 6:
/* 1491 */       if (i == 8)
/*      */       {
/* 1494 */         localArrayList.add(ImageTypeSpecifier.createFromBufferedImageType(6));
/*      */ 
/* 1497 */         localArrayList.add(ImageTypeSpecifier.createFromBufferedImageType(2));
/*      */       }
/*      */ 
/* 1502 */       localColorSpace1 = ColorSpace.getInstance(1000);
/* 1503 */       arrayOfInt = new int[4];
/* 1504 */       arrayOfInt[0] = 0;
/* 1505 */       arrayOfInt[1] = 1;
/* 1506 */       arrayOfInt[2] = 2;
/* 1507 */       arrayOfInt[3] = 3;
/*      */ 
/* 1509 */       localArrayList.add(ImageTypeSpecifier.createInterleaved(localColorSpace1, arrayOfInt, k, true, false));
/*      */ 
/* 1514 */       break;
/*      */     case 1:
/*      */     case 5:
/*      */     }
/*      */ 
/* 1520 */     return localArrayList.iterator();
/*      */   }
/*      */ 
/*      */   public ImageTypeSpecifier getRawImageType(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1546 */     Iterator localIterator = getImageTypes(paramInt);
/* 1547 */     ImageTypeSpecifier localImageTypeSpecifier = null;
/*      */     do
/* 1549 */       localImageTypeSpecifier = (ImageTypeSpecifier)localIterator.next();
/* 1550 */     while (localIterator.hasNext());
/* 1551 */     return localImageTypeSpecifier;
/*      */   }
/*      */ 
/*      */   public ImageReadParam getDefaultReadParam() {
/* 1555 */     return new ImageReadParam();
/*      */   }
/*      */ 
/*      */   public IIOMetadata getStreamMetadata() throws IIOException
/*      */   {
/* 1560 */     return null;
/*      */   }
/*      */ 
/*      */   public IIOMetadata getImageMetadata(int paramInt) throws IIOException {
/* 1564 */     if (paramInt != 0) {
/* 1565 */       throw new IndexOutOfBoundsException("imageIndex != 0!");
/*      */     }
/* 1567 */     readMetadata();
/* 1568 */     return this.metadata;
/*      */   }
/*      */ 
/*      */   public BufferedImage read(int paramInt, ImageReadParam paramImageReadParam) throws IIOException
/*      */   {
/* 1573 */     if (paramInt != 0) {
/* 1574 */       throw new IndexOutOfBoundsException("imageIndex != 0!");
/*      */     }
/*      */ 
/* 1577 */     readImage(paramImageReadParam);
/* 1578 */     return this.theImage;
/*      */   }
/*      */ 
/*      */   public void reset() {
/* 1582 */     super.reset();
/* 1583 */     resetStreamSettings();
/*      */   }
/*      */ 
/*      */   private void resetStreamSettings() {
/* 1587 */     this.gotHeader = false;
/* 1588 */     this.gotMetadata = false;
/* 1589 */     this.metadata = null;
/* 1590 */     this.pixelStream = null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.PNGImageReader
 * JD-Core Version:    0.6.2
 */
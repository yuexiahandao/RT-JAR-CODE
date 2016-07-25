/*      */ package com.sun.imageio.plugins.png;
/*      */ 
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.zip.DeflaterOutputStream;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.IIOImage;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.ImageWriteParam;
/*      */ import javax.imageio.ImageWriter;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.spi.ImageWriterSpi;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ 
/*      */ public class PNGImageWriter extends ImageWriter
/*      */ {
/*  280 */   ImageOutputStream stream = null;
/*      */ 
/*  282 */   PNGMetadata metadata = null;
/*      */ 
/*  285 */   int sourceXOffset = 0;
/*  286 */   int sourceYOffset = 0;
/*  287 */   int sourceWidth = 0;
/*  288 */   int sourceHeight = 0;
/*  289 */   int[] sourceBands = null;
/*  290 */   int periodX = 1;
/*  291 */   int periodY = 1;
/*      */   int numBands;
/*      */   int bpp;
/*  296 */   RowFilter rowFilter = new RowFilter();
/*  297 */   byte[] prevRow = null;
/*  298 */   byte[] currRow = null;
/*  299 */   byte[][] filteredRows = (byte[][])null;
/*      */ 
/*  309 */   int[] sampleSize = null;
/*  310 */   int scalingBitDepth = -1;
/*      */ 
/*  313 */   byte[][] scale = (byte[][])null;
/*  314 */   byte[] scale0 = null;
/*      */ 
/*  317 */   byte[][] scaleh = (byte[][])null;
/*  318 */   byte[][] scalel = (byte[][])null;
/*      */   int totalPixels;
/*      */   int pixelsDone;
/*  339 */   private static int[] allowedProgressivePasses = { 1, 7 };
/*      */ 
/*      */   public PNGImageWriter(ImageWriterSpi paramImageWriterSpi)
/*      */   {
/*  324 */     super(paramImageWriterSpi);
/*      */   }
/*      */ 
/*      */   public void setOutput(Object paramObject) {
/*  328 */     super.setOutput(paramObject);
/*  329 */     if (paramObject != null) {
/*  330 */       if (!(paramObject instanceof ImageOutputStream)) {
/*  331 */         throw new IllegalArgumentException("output not an ImageOutputStream!");
/*      */       }
/*  333 */       this.stream = ((ImageOutputStream)paramObject);
/*      */     } else {
/*  335 */       this.stream = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public ImageWriteParam getDefaultWriteParam()
/*      */   {
/*  342 */     return new PNGImageWriteParam(getLocale());
/*      */   }
/*      */ 
/*      */   public IIOMetadata getDefaultStreamMetadata(ImageWriteParam paramImageWriteParam) {
/*  346 */     return null;
/*      */   }
/*      */ 
/*      */   public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  351 */     PNGMetadata localPNGMetadata = new PNGMetadata();
/*  352 */     localPNGMetadata.initialize(paramImageTypeSpecifier, paramImageTypeSpecifier.getSampleModel().getNumBands());
/*  353 */     return localPNGMetadata;
/*      */   }
/*      */ 
/*      */   public IIOMetadata convertStreamMetadata(IIOMetadata paramIIOMetadata, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  358 */     return null;
/*      */   }
/*      */ 
/*      */   public IIOMetadata convertImageMetadata(IIOMetadata paramIIOMetadata, ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  365 */     if ((paramIIOMetadata instanceof PNGMetadata)) {
/*  366 */       return (PNGMetadata)((PNGMetadata)paramIIOMetadata).clone();
/*      */     }
/*  368 */     return new PNGMetadata(paramIIOMetadata);
/*      */   }
/*      */ 
/*      */   private void write_magic()
/*      */     throws IOException
/*      */   {
/*  374 */     byte[] arrayOfByte = { -119, 80, 78, 71, 13, 10, 26, 10 };
/*  375 */     this.stream.write(arrayOfByte);
/*      */   }
/*      */ 
/*      */   private void write_IHDR() throws IOException
/*      */   {
/*  380 */     ChunkStream localChunkStream = new ChunkStream(1229472850, this.stream);
/*  381 */     localChunkStream.writeInt(this.metadata.IHDR_width);
/*  382 */     localChunkStream.writeInt(this.metadata.IHDR_height);
/*  383 */     localChunkStream.writeByte(this.metadata.IHDR_bitDepth);
/*  384 */     localChunkStream.writeByte(this.metadata.IHDR_colorType);
/*  385 */     if (this.metadata.IHDR_compressionMethod != 0) {
/*  386 */       throw new IIOException("Only compression method 0 is defined in PNG 1.1");
/*      */     }
/*      */ 
/*  389 */     localChunkStream.writeByte(this.metadata.IHDR_compressionMethod);
/*  390 */     if (this.metadata.IHDR_filterMethod != 0) {
/*  391 */       throw new IIOException("Only filter method 0 is defined in PNG 1.1");
/*      */     }
/*      */ 
/*  394 */     localChunkStream.writeByte(this.metadata.IHDR_filterMethod);
/*  395 */     if ((this.metadata.IHDR_interlaceMethod < 0) || (this.metadata.IHDR_interlaceMethod > 1))
/*      */     {
/*  397 */       throw new IIOException("Only interlace methods 0 (node) and 1 (adam7) are defined in PNG 1.1");
/*      */     }
/*      */ 
/*  400 */     localChunkStream.writeByte(this.metadata.IHDR_interlaceMethod);
/*  401 */     localChunkStream.finish();
/*      */   }
/*      */ 
/*      */   private void write_cHRM() throws IOException {
/*  405 */     if (this.metadata.cHRM_present) {
/*  406 */       ChunkStream localChunkStream = new ChunkStream(1665684045, this.stream);
/*  407 */       localChunkStream.writeInt(this.metadata.cHRM_whitePointX);
/*  408 */       localChunkStream.writeInt(this.metadata.cHRM_whitePointY);
/*  409 */       localChunkStream.writeInt(this.metadata.cHRM_redX);
/*  410 */       localChunkStream.writeInt(this.metadata.cHRM_redY);
/*  411 */       localChunkStream.writeInt(this.metadata.cHRM_greenX);
/*  412 */       localChunkStream.writeInt(this.metadata.cHRM_greenY);
/*  413 */       localChunkStream.writeInt(this.metadata.cHRM_blueX);
/*  414 */       localChunkStream.writeInt(this.metadata.cHRM_blueY);
/*  415 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_gAMA() throws IOException {
/*  420 */     if (this.metadata.gAMA_present) {
/*  421 */       ChunkStream localChunkStream = new ChunkStream(1732332865, this.stream);
/*  422 */       localChunkStream.writeInt(this.metadata.gAMA_gamma);
/*  423 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_iCCP() throws IOException {
/*  428 */     if (this.metadata.iCCP_present) {
/*  429 */       ChunkStream localChunkStream = new ChunkStream(1766015824, this.stream);
/*  430 */       localChunkStream.writeBytes(this.metadata.iCCP_profileName);
/*  431 */       localChunkStream.writeByte(0);
/*      */ 
/*  433 */       localChunkStream.writeByte(this.metadata.iCCP_compressionMethod);
/*  434 */       localChunkStream.write(this.metadata.iCCP_compressedProfile);
/*  435 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_sBIT() throws IOException {
/*  440 */     if (this.metadata.sBIT_present) {
/*  441 */       ChunkStream localChunkStream = new ChunkStream(1933723988, this.stream);
/*  442 */       int i = this.metadata.IHDR_colorType;
/*  443 */       if (this.metadata.sBIT_colorType != i) {
/*  444 */         processWarningOccurred(0, "sBIT metadata has wrong color type.\nThe chunk will not be written.");
/*      */ 
/*  447 */         return;
/*      */       }
/*      */ 
/*  450 */       if ((i == 0) || (i == 4))
/*      */       {
/*  452 */         localChunkStream.writeByte(this.metadata.sBIT_grayBits);
/*  453 */       } else if ((i == 2) || (i == 3) || (i == 6))
/*      */       {
/*  456 */         localChunkStream.writeByte(this.metadata.sBIT_redBits);
/*  457 */         localChunkStream.writeByte(this.metadata.sBIT_greenBits);
/*  458 */         localChunkStream.writeByte(this.metadata.sBIT_blueBits);
/*      */       }
/*      */ 
/*  461 */       if ((i == 4) || (i == 6))
/*      */       {
/*  463 */         localChunkStream.writeByte(this.metadata.sBIT_alphaBits);
/*      */       }
/*  465 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_sRGB() throws IOException {
/*  470 */     if (this.metadata.sRGB_present) {
/*  471 */       ChunkStream localChunkStream = new ChunkStream(1934772034, this.stream);
/*  472 */       localChunkStream.writeByte(this.metadata.sRGB_renderingIntent);
/*  473 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_PLTE() throws IOException {
/*  478 */     if (this.metadata.PLTE_present) {
/*  479 */       if ((this.metadata.IHDR_colorType == 0) || (this.metadata.IHDR_colorType == 4))
/*      */       {
/*  483 */         processWarningOccurred(0, "A PLTE chunk may not appear in a gray or gray alpha image.\nThe chunk will not be written");
/*      */ 
/*  486 */         return;
/*      */       }
/*      */ 
/*  489 */       ChunkStream localChunkStream = new ChunkStream(1347179589, this.stream);
/*      */ 
/*  491 */       int i = this.metadata.PLTE_red.length;
/*  492 */       byte[] arrayOfByte = new byte[i * 3];
/*  493 */       int j = 0;
/*  494 */       for (int k = 0; k < i; k++) {
/*  495 */         arrayOfByte[(j++)] = this.metadata.PLTE_red[k];
/*  496 */         arrayOfByte[(j++)] = this.metadata.PLTE_green[k];
/*  497 */         arrayOfByte[(j++)] = this.metadata.PLTE_blue[k];
/*      */       }
/*      */ 
/*  500 */       localChunkStream.write(arrayOfByte);
/*  501 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_hIST() throws IOException, IIOException {
/*  506 */     if (this.metadata.hIST_present) {
/*  507 */       ChunkStream localChunkStream = new ChunkStream(1749635924, this.stream);
/*      */ 
/*  509 */       if (!this.metadata.PLTE_present) {
/*  510 */         throw new IIOException("hIST chunk without PLTE chunk!");
/*      */       }
/*      */ 
/*  513 */       localChunkStream.writeChars(this.metadata.hIST_histogram, 0, this.metadata.hIST_histogram.length);
/*      */ 
/*  515 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_tRNS() throws IOException, IIOException {
/*  520 */     if (this.metadata.tRNS_present) {
/*  521 */       ChunkStream localChunkStream = new ChunkStream(1951551059, this.stream);
/*  522 */       int i = this.metadata.IHDR_colorType;
/*  523 */       int j = this.metadata.tRNS_colorType;
/*      */ 
/*  527 */       int k = this.metadata.tRNS_red;
/*  528 */       int m = this.metadata.tRNS_green;
/*  529 */       int n = this.metadata.tRNS_blue;
/*  530 */       if ((i == 2) && (j == 0))
/*      */       {
/*  532 */         j = i;
/*  533 */         k = m = n = this.metadata.tRNS_gray;
/*      */       }
/*      */ 
/*  537 */       if (j != i) {
/*  538 */         processWarningOccurred(0, "tRNS metadata has incompatible color type.\nThe chunk will not be written.");
/*      */ 
/*  541 */         return;
/*      */       }
/*      */ 
/*  544 */       if (i == 3) {
/*  545 */         if (!this.metadata.PLTE_present) {
/*  546 */           throw new IIOException("tRNS chunk without PLTE chunk!");
/*      */         }
/*  548 */         localChunkStream.write(this.metadata.tRNS_alpha);
/*  549 */       } else if (i == 0) {
/*  550 */         localChunkStream.writeShort(this.metadata.tRNS_gray);
/*  551 */       } else if (i == 2) {
/*  552 */         localChunkStream.writeShort(k);
/*  553 */         localChunkStream.writeShort(m);
/*  554 */         localChunkStream.writeShort(n);
/*      */       } else {
/*  556 */         throw new IIOException("tRNS chunk for color type 4 or 6!");
/*      */       }
/*  558 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_bKGD() throws IOException {
/*  563 */     if (this.metadata.bKGD_present) {
/*  564 */       ChunkStream localChunkStream = new ChunkStream(1649100612, this.stream);
/*  565 */       int i = this.metadata.IHDR_colorType & 0x3;
/*  566 */       int j = this.metadata.bKGD_colorType;
/*      */ 
/*  570 */       int k = this.metadata.bKGD_red;
/*  571 */       int m = this.metadata.bKGD_red;
/*  572 */       int n = this.metadata.bKGD_red;
/*  573 */       if ((i == 2) && (j == 0))
/*      */       {
/*  576 */         j = i;
/*  577 */         k = m = n = this.metadata.bKGD_gray;
/*      */       }
/*      */ 
/*  582 */       if (j != i) {
/*  583 */         processWarningOccurred(0, "bKGD metadata has incompatible color type.\nThe chunk will not be written.");
/*      */ 
/*  586 */         return;
/*      */       }
/*      */ 
/*  589 */       if (i == 3) {
/*  590 */         localChunkStream.writeByte(this.metadata.bKGD_index);
/*  591 */       } else if ((i == 0) || (i == 4))
/*      */       {
/*  593 */         localChunkStream.writeShort(this.metadata.bKGD_gray);
/*      */       }
/*      */       else {
/*  596 */         localChunkStream.writeShort(k);
/*  597 */         localChunkStream.writeShort(m);
/*  598 */         localChunkStream.writeShort(n);
/*      */       }
/*  600 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_pHYs() throws IOException {
/*  605 */     if (this.metadata.pHYs_present) {
/*  606 */       ChunkStream localChunkStream = new ChunkStream(1883789683, this.stream);
/*  607 */       localChunkStream.writeInt(this.metadata.pHYs_pixelsPerUnitXAxis);
/*  608 */       localChunkStream.writeInt(this.metadata.pHYs_pixelsPerUnitYAxis);
/*  609 */       localChunkStream.writeByte(this.metadata.pHYs_unitSpecifier);
/*  610 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_sPLT() throws IOException {
/*  615 */     if (this.metadata.sPLT_present) {
/*  616 */       ChunkStream localChunkStream = new ChunkStream(1934642260, this.stream);
/*      */ 
/*  618 */       localChunkStream.writeBytes(this.metadata.sPLT_paletteName);
/*  619 */       localChunkStream.writeByte(0);
/*      */ 
/*  621 */       localChunkStream.writeByte(this.metadata.sPLT_sampleDepth);
/*  622 */       int i = this.metadata.sPLT_red.length;
/*      */       int j;
/*  624 */       if (this.metadata.sPLT_sampleDepth == 8)
/*  625 */         for (j = 0; j < i; j++) {
/*  626 */           localChunkStream.writeByte(this.metadata.sPLT_red[j]);
/*  627 */           localChunkStream.writeByte(this.metadata.sPLT_green[j]);
/*  628 */           localChunkStream.writeByte(this.metadata.sPLT_blue[j]);
/*  629 */           localChunkStream.writeByte(this.metadata.sPLT_alpha[j]);
/*  630 */           localChunkStream.writeShort(this.metadata.sPLT_frequency[j]);
/*      */         }
/*      */       else {
/*  633 */         for (j = 0; j < i; j++) {
/*  634 */           localChunkStream.writeShort(this.metadata.sPLT_red[j]);
/*  635 */           localChunkStream.writeShort(this.metadata.sPLT_green[j]);
/*  636 */           localChunkStream.writeShort(this.metadata.sPLT_blue[j]);
/*  637 */           localChunkStream.writeShort(this.metadata.sPLT_alpha[j]);
/*  638 */           localChunkStream.writeShort(this.metadata.sPLT_frequency[j]);
/*      */         }
/*      */       }
/*  641 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_tIME() throws IOException {
/*  646 */     if (this.metadata.tIME_present) {
/*  647 */       ChunkStream localChunkStream = new ChunkStream(1950960965, this.stream);
/*  648 */       localChunkStream.writeShort(this.metadata.tIME_year);
/*  649 */       localChunkStream.writeByte(this.metadata.tIME_month);
/*  650 */       localChunkStream.writeByte(this.metadata.tIME_day);
/*  651 */       localChunkStream.writeByte(this.metadata.tIME_hour);
/*  652 */       localChunkStream.writeByte(this.metadata.tIME_minute);
/*  653 */       localChunkStream.writeByte(this.metadata.tIME_second);
/*  654 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_tEXt() throws IOException {
/*  659 */     Iterator localIterator1 = this.metadata.tEXt_keyword.iterator();
/*  660 */     Iterator localIterator2 = this.metadata.tEXt_text.iterator();
/*      */ 
/*  662 */     while (localIterator1.hasNext()) {
/*  663 */       ChunkStream localChunkStream = new ChunkStream(1950701684, this.stream);
/*  664 */       String str1 = (String)localIterator1.next();
/*  665 */       localChunkStream.writeBytes(str1);
/*  666 */       localChunkStream.writeByte(0);
/*      */ 
/*  668 */       String str2 = (String)localIterator2.next();
/*  669 */       localChunkStream.writeBytes(str2);
/*  670 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private byte[] deflate(byte[] paramArrayOfByte) throws IOException {
/*  675 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*  676 */     DeflaterOutputStream localDeflaterOutputStream = new DeflaterOutputStream(localByteArrayOutputStream);
/*  677 */     localDeflaterOutputStream.write(paramArrayOfByte);
/*  678 */     localDeflaterOutputStream.close();
/*  679 */     return localByteArrayOutputStream.toByteArray();
/*      */   }
/*      */ 
/*      */   private void write_iTXt() throws IOException {
/*  683 */     Iterator localIterator1 = this.metadata.iTXt_keyword.iterator();
/*  684 */     Iterator localIterator2 = this.metadata.iTXt_compressionFlag.iterator();
/*  685 */     Iterator localIterator3 = this.metadata.iTXt_compressionMethod.iterator();
/*  686 */     Iterator localIterator4 = this.metadata.iTXt_languageTag.iterator();
/*  687 */     Iterator localIterator5 = this.metadata.iTXt_translatedKeyword.iterator();
/*      */ 
/*  689 */     Iterator localIterator6 = this.metadata.iTXt_text.iterator();
/*      */ 
/*  691 */     while (localIterator1.hasNext()) {
/*  692 */       ChunkStream localChunkStream = new ChunkStream(1767135348, this.stream);
/*      */ 
/*  694 */       localChunkStream.writeBytes((String)localIterator1.next());
/*  695 */       localChunkStream.writeByte(0);
/*      */ 
/*  697 */       Boolean localBoolean = (Boolean)localIterator2.next();
/*  698 */       localChunkStream.writeByte(localBoolean.booleanValue() ? 1 : 0);
/*      */ 
/*  700 */       localChunkStream.writeByte(((Integer)localIterator3.next()).intValue());
/*      */ 
/*  702 */       localChunkStream.writeBytes((String)localIterator4.next());
/*  703 */       localChunkStream.writeByte(0);
/*      */ 
/*  706 */       localChunkStream.write(((String)localIterator5.next()).getBytes("UTF8"));
/*  707 */       localChunkStream.writeByte(0);
/*      */ 
/*  709 */       String str = (String)localIterator6.next();
/*  710 */       if (localBoolean.booleanValue())
/*  711 */         localChunkStream.write(deflate(str.getBytes("UTF8")));
/*      */       else {
/*  713 */         localChunkStream.write(str.getBytes("UTF8"));
/*      */       }
/*  715 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_zTXt() throws IOException {
/*  720 */     Iterator localIterator1 = this.metadata.zTXt_keyword.iterator();
/*  721 */     Iterator localIterator2 = this.metadata.zTXt_compressionMethod.iterator();
/*  722 */     Iterator localIterator3 = this.metadata.zTXt_text.iterator();
/*      */ 
/*  724 */     while (localIterator1.hasNext()) {
/*  725 */       ChunkStream localChunkStream = new ChunkStream(2052348020, this.stream);
/*  726 */       String str1 = (String)localIterator1.next();
/*  727 */       localChunkStream.writeBytes(str1);
/*  728 */       localChunkStream.writeByte(0);
/*      */ 
/*  730 */       int i = ((Integer)localIterator2.next()).intValue();
/*  731 */       localChunkStream.writeByte(i);
/*      */ 
/*  733 */       String str2 = (String)localIterator3.next();
/*  734 */       localChunkStream.write(deflate(str2.getBytes("ISO-8859-1")));
/*  735 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeUnknownChunks() throws IOException {
/*  740 */     Iterator localIterator1 = this.metadata.unknownChunkType.iterator();
/*  741 */     Iterator localIterator2 = this.metadata.unknownChunkData.iterator();
/*      */ 
/*  743 */     while ((localIterator1.hasNext()) && (localIterator2.hasNext())) {
/*  744 */       String str = (String)localIterator1.next();
/*  745 */       ChunkStream localChunkStream = new ChunkStream(chunkType(str), this.stream);
/*  746 */       byte[] arrayOfByte = (byte[])localIterator2.next();
/*  747 */       localChunkStream.write(arrayOfByte);
/*  748 */       localChunkStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int chunkType(String paramString) {
/*  753 */     int i = paramString.charAt(0);
/*  754 */     int j = paramString.charAt(1);
/*  755 */     int k = paramString.charAt(2);
/*  756 */     int m = paramString.charAt(3);
/*      */ 
/*  758 */     int n = i << 24 | j << 16 | k << 8 | m;
/*  759 */     return n;
/*      */   }
/*      */ 
/*      */   private void encodePass(ImageOutputStream paramImageOutputStream, RenderedImage paramRenderedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     throws IOException
/*      */   {
/*  766 */     int i = this.sourceXOffset;
/*  767 */     int j = this.sourceYOffset;
/*  768 */     int k = this.sourceWidth;
/*  769 */     int m = this.sourceHeight;
/*      */ 
/*  772 */     paramInt1 *= this.periodX;
/*  773 */     paramInt3 *= this.periodX;
/*  774 */     paramInt2 *= this.periodY;
/*  775 */     paramInt4 *= this.periodY;
/*      */ 
/*  778 */     int n = (k - paramInt1 + paramInt3 - 1) / paramInt3;
/*  779 */     int i1 = (m - paramInt2 + paramInt4 - 1) / paramInt4;
/*  780 */     if ((n == 0) || (i1 == 0)) {
/*  781 */       return;
/*      */     }
/*      */ 
/*  785 */     paramInt1 *= this.numBands;
/*  786 */     paramInt3 *= this.numBands;
/*      */ 
/*  789 */     int i2 = 8 / this.metadata.IHDR_bitDepth;
/*  790 */     int i3 = k * this.numBands;
/*  791 */     int[] arrayOfInt = new int[i3];
/*      */ 
/*  793 */     int i4 = n * this.numBands;
/*  794 */     if (this.metadata.IHDR_bitDepth < 8)
/*  795 */       i4 = (i4 + i2 - 1) / i2;
/*  796 */     else if (this.metadata.IHDR_bitDepth == 16) {
/*  797 */       i4 *= 2;
/*      */     }
/*      */ 
/*  800 */     IndexColorModel localIndexColorModel = null;
/*  801 */     if ((this.metadata.IHDR_colorType == 4) && ((paramRenderedImage.getColorModel() instanceof IndexColorModel)))
/*      */     {
/*  805 */       i4 *= 2;
/*      */ 
/*  808 */       localIndexColorModel = (IndexColorModel)paramRenderedImage.getColorModel();
/*      */     }
/*      */ 
/*  811 */     this.currRow = new byte[i4 + this.bpp];
/*  812 */     this.prevRow = new byte[i4 + this.bpp];
/*  813 */     this.filteredRows = new byte[5][i4 + this.bpp];
/*      */ 
/*  815 */     int i5 = this.metadata.IHDR_bitDepth;
/*  816 */     for (int i6 = j + paramInt2; i6 < j + m; i6 += paramInt4) {
/*  817 */       Rectangle localRectangle = new Rectangle(i, i6, k, 1);
/*  818 */       Raster localRaster = paramRenderedImage.getData(localRectangle);
/*  819 */       if (this.sourceBands != null) {
/*  820 */         localRaster = localRaster.createChild(i, i6, k, 1, i, i6, this.sourceBands);
/*      */       }
/*      */ 
/*  824 */       localRaster.getPixels(i, i6, k, 1, arrayOfInt);
/*      */ 
/*  826 */       if (paramRenderedImage.getColorModel().isAlphaPremultiplied()) {
/*  827 */         localObject = localRaster.createCompatibleWritableRaster();
/*  828 */         ((WritableRaster)localObject).setPixels(((WritableRaster)localObject).getMinX(), ((WritableRaster)localObject).getMinY(), ((WritableRaster)localObject).getWidth(), ((WritableRaster)localObject).getHeight(), arrayOfInt);
/*      */ 
/*  832 */         paramRenderedImage.getColorModel().coerceData((WritableRaster)localObject, false);
/*  833 */         ((WritableRaster)localObject).getPixels(((WritableRaster)localObject).getMinX(), ((WritableRaster)localObject).getMinY(), ((WritableRaster)localObject).getWidth(), ((WritableRaster)localObject).getHeight(), arrayOfInt);
/*      */       }
/*      */ 
/*  839 */       Object localObject = this.metadata.PLTE_order;
/*  840 */       if (localObject != null) {
/*  841 */         for (i7 = 0; i7 < i3; i7++) {
/*  842 */           arrayOfInt[i7] = localObject[arrayOfInt[i7]];
/*      */         }
/*      */       }
/*      */ 
/*  846 */       int i7 = this.bpp;
/*  847 */       int i8 = 0;
/*  848 */       int i9 = 0;
/*      */       int i11;
/*      */       int i12;
/*  850 */       switch (i5) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 4:
/*  854 */         i10 = i2 - 1;
/*  855 */         for (i11 = paramInt1; i11 < i3; i11 += paramInt3) {
/*  856 */           i12 = this.scale0[arrayOfInt[i11]];
/*  857 */           i9 = i9 << i5 | i12;
/*      */ 
/*  859 */           if ((i8++ & i10) == i10) {
/*  860 */             this.currRow[(i7++)] = ((byte)i9);
/*  861 */             i9 = 0;
/*  862 */             i8 = 0;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  867 */         if ((i8 & i10) != 0) {
/*  868 */           i9 <<= (8 / i5 - i8) * i5;
/*  869 */           this.currRow[(i7++)] = ((byte)i9); } break;
/*      */       case 8:
/*  874 */         if (this.numBands == 1) {
/*  875 */           for (i11 = paramInt1; i11 < i3; i11 += paramInt3) {
/*  876 */             this.currRow[(i7++)] = this.scale0[arrayOfInt[i11]];
/*  877 */             if (localIndexColorModel != null) {
/*  878 */               this.currRow[(i7++)] = this.scale0[localIndexColorModel.getAlpha(0xFF & arrayOfInt[i11])];
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  883 */           for (i11 = paramInt1; i11 < i3; i11 += paramInt3) {
/*  884 */             for (i12 = 0; i12 < this.numBands; i12++) {
/*  885 */               this.currRow[(i7++)] = this.scale[i12][arrayOfInt[(i11 + i12)]];
/*      */             }
/*      */           }
/*      */         }
/*  889 */         break;
/*      */       case 16:
/*  892 */         for (i11 = paramInt1; i11 < i3; i11 += paramInt3) {
/*  893 */           for (i12 = 0; i12 < this.numBands; i12++) {
/*  894 */             this.currRow[(i7++)] = this.scaleh[i12][arrayOfInt[(i11 + i12)]];
/*  895 */             this.currRow[(i7++)] = this.scalel[i12][arrayOfInt[(i11 + i12)]];
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  902 */       int i10 = this.rowFilter.filterRow(this.metadata.IHDR_colorType, this.currRow, this.prevRow, this.filteredRows, i4, this.bpp);
/*      */ 
/*  907 */       paramImageOutputStream.write(i10);
/*  908 */       paramImageOutputStream.write(this.filteredRows[i10], this.bpp, i4);
/*      */ 
/*  911 */       byte[] arrayOfByte = this.currRow;
/*  912 */       this.currRow = this.prevRow;
/*  913 */       this.prevRow = arrayOfByte;
/*      */ 
/*  915 */       this.pixelsDone += n;
/*  916 */       processImageProgress(100.0F * this.pixelsDone / this.totalPixels);
/*      */ 
/*  920 */       if (abortRequested())
/*  921 */         return;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write_IDAT(RenderedImage paramRenderedImage)
/*      */     throws IOException
/*      */   {
/*  928 */     IDATOutputStream localIDATOutputStream = new IDATOutputStream(this.stream, 32768);
/*      */     try {
/*  930 */       if (this.metadata.IHDR_interlaceMethod == 1) {
/*  931 */         for (int i = 0; i < 7; i++) {
/*  932 */           encodePass(localIDATOutputStream, paramRenderedImage, PNGImageReader.adam7XOffset[i], PNGImageReader.adam7YOffset[i], PNGImageReader.adam7XSubsampling[i], PNGImageReader.adam7YSubsampling[i]);
/*      */ 
/*  937 */           if (abortRequested())
/*      */             break;
/*      */         }
/*      */       }
/*      */       else
/*  942 */         encodePass(localIDATOutputStream, paramRenderedImage, 0, 0, 1, 1);
/*      */     }
/*      */     finally {
/*  945 */       localIDATOutputStream.finish();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeIEND() throws IOException {
/*  950 */     ChunkStream localChunkStream = new ChunkStream(1229278788, this.stream);
/*  951 */     localChunkStream.finish();
/*      */   }
/*      */ 
/*      */   private boolean equals(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  957 */     if ((paramArrayOfInt1 == null) || (paramArrayOfInt2 == null)) {
/*  958 */       return false;
/*      */     }
/*  960 */     if (paramArrayOfInt1.length != paramArrayOfInt2.length) {
/*  961 */       return false;
/*      */     }
/*  963 */     for (int i = 0; i < paramArrayOfInt1.length; i++) {
/*  964 */       if (paramArrayOfInt1[i] != paramArrayOfInt2[i]) {
/*  965 */         return false;
/*      */       }
/*      */     }
/*  968 */     return true;
/*      */   }
/*      */ 
/*      */   private void initializeScaleTables(int[] paramArrayOfInt)
/*      */   {
/*  975 */     int i = this.metadata.IHDR_bitDepth;
/*      */ 
/*  978 */     if ((i == this.scalingBitDepth) && (equals(paramArrayOfInt, this.sampleSize)))
/*      */     {
/*  980 */       return;
/*      */     }
/*      */ 
/*  984 */     this.sampleSize = paramArrayOfInt;
/*  985 */     this.scalingBitDepth = i;
/*  986 */     int j = (1 << i) - 1;
/*      */     int k;
/*      */     int m;
/*      */     int n;
/*      */     int i1;
/*  987 */     if (i <= 8) {
/*  988 */       this.scale = new byte[this.numBands][];
/*  989 */       for (k = 0; k < this.numBands; k++) {
/*  990 */         m = (1 << paramArrayOfInt[k]) - 1;
/*  991 */         n = m / 2;
/*  992 */         this.scale[k] = new byte[m + 1];
/*  993 */         for (i1 = 0; i1 <= m; i1++) {
/*  994 */           this.scale[k][i1] = ((byte)((i1 * j + n) / m));
/*      */         }
/*      */       }
/*      */ 
/*  998 */       this.scale0 = this.scale[0];
/*  999 */       this.scaleh = (this.scalel = (byte[][])null);
/*      */     }
/*      */     else {
/* 1002 */       this.scaleh = new byte[this.numBands][];
/* 1003 */       this.scalel = new byte[this.numBands][];
/*      */ 
/* 1005 */       for (k = 0; k < this.numBands; k++) {
/* 1006 */         m = (1 << paramArrayOfInt[k]) - 1;
/* 1007 */         n = m / 2;
/* 1008 */         this.scaleh[k] = new byte[m + 1];
/* 1009 */         this.scalel[k] = new byte[m + 1];
/* 1010 */         for (i1 = 0; i1 <= m; i1++) {
/* 1011 */           int i2 = (i1 * j + n) / m;
/* 1012 */           this.scaleh[k][i1] = ((byte)(i2 >> 8));
/* 1013 */           this.scalel[k][i1] = ((byte)(i2 & 0xFF));
/*      */         }
/*      */       }
/* 1016 */       this.scale = ((byte[][])null);
/* 1017 */       this.scale0 = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write(IIOMetadata paramIIOMetadata, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IIOException
/*      */   {
/* 1024 */     if (this.stream == null) {
/* 1025 */       throw new IllegalStateException("output == null!");
/*      */     }
/* 1027 */     if (paramIIOImage == null) {
/* 1028 */       throw new IllegalArgumentException("image == null!");
/*      */     }
/* 1030 */     if (paramIIOImage.hasRaster()) {
/* 1031 */       throw new UnsupportedOperationException("image has a Raster!");
/*      */     }
/*      */ 
/* 1034 */     RenderedImage localRenderedImage = paramIIOImage.getRenderedImage();
/* 1035 */     SampleModel localSampleModel = localRenderedImage.getSampleModel();
/* 1036 */     this.numBands = localSampleModel.getNumBands();
/*      */ 
/* 1039 */     this.sourceXOffset = localRenderedImage.getMinX();
/* 1040 */     this.sourceYOffset = localRenderedImage.getMinY();
/* 1041 */     this.sourceWidth = localRenderedImage.getWidth();
/* 1042 */     this.sourceHeight = localRenderedImage.getHeight();
/* 1043 */     this.sourceBands = null;
/* 1044 */     this.periodX = 1;
/* 1045 */     this.periodY = 1;
/*      */ 
/* 1047 */     if (paramImageWriteParam != null)
/*      */     {
/* 1049 */       Rectangle localRectangle1 = paramImageWriteParam.getSourceRegion();
/* 1050 */       if (localRectangle1 != null) {
/* 1051 */         Rectangle localRectangle2 = new Rectangle(localRenderedImage.getMinX(), localRenderedImage.getMinY(), localRenderedImage.getWidth(), localRenderedImage.getHeight());
/*      */ 
/* 1056 */         localRectangle1 = localRectangle1.intersection(localRectangle2);
/* 1057 */         this.sourceXOffset = localRectangle1.x;
/* 1058 */         this.sourceYOffset = localRectangle1.y;
/* 1059 */         this.sourceWidth = localRectangle1.width;
/* 1060 */         this.sourceHeight = localRectangle1.height;
/*      */       }
/*      */ 
/* 1064 */       j = paramImageWriteParam.getSubsamplingXOffset();
/* 1065 */       int k = paramImageWriteParam.getSubsamplingYOffset();
/* 1066 */       this.sourceXOffset += j;
/* 1067 */       this.sourceYOffset += k;
/* 1068 */       this.sourceWidth -= j;
/* 1069 */       this.sourceHeight -= k;
/*      */ 
/* 1072 */       this.periodX = paramImageWriteParam.getSourceXSubsampling();
/* 1073 */       this.periodY = paramImageWriteParam.getSourceYSubsampling();
/*      */ 
/* 1075 */       int[] arrayOfInt = paramImageWriteParam.getSourceBands();
/* 1076 */       if (arrayOfInt != null) {
/* 1077 */         this.sourceBands = arrayOfInt;
/* 1078 */         this.numBands = this.sourceBands.length;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1083 */     int i = (this.sourceWidth + this.periodX - 1) / this.periodX;
/* 1084 */     int j = (this.sourceHeight + this.periodY - 1) / this.periodY;
/* 1085 */     if ((i <= 0) || (j <= 0)) {
/* 1086 */       throw new IllegalArgumentException("Empty source region!");
/*      */     }
/*      */ 
/* 1090 */     this.totalPixels = (i * j);
/* 1091 */     this.pixelsDone = 0;
/*      */ 
/* 1094 */     IIOMetadata localIIOMetadata = paramIIOImage.getMetadata();
/* 1095 */     if (localIIOMetadata != null) {
/* 1096 */       this.metadata = ((PNGMetadata)convertImageMetadata(localIIOMetadata, ImageTypeSpecifier.createFromRenderedImage(localRenderedImage), null));
/*      */     }
/*      */     else
/*      */     {
/* 1100 */       this.metadata = new PNGMetadata();
/*      */     }
/*      */ 
/* 1103 */     if (paramImageWriteParam != null)
/*      */     {
/* 1105 */       switch (paramImageWriteParam.getProgressiveMode()) {
/*      */       case 1:
/* 1107 */         this.metadata.IHDR_interlaceMethod = 1;
/* 1108 */         break;
/*      */       case 0:
/* 1110 */         this.metadata.IHDR_interlaceMethod = 0;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1118 */     this.metadata.initialize(new ImageTypeSpecifier(localRenderedImage), this.numBands);
/*      */ 
/* 1121 */     this.metadata.IHDR_width = i;
/* 1122 */     this.metadata.IHDR_height = j;
/*      */ 
/* 1124 */     this.bpp = (this.numBands * (this.metadata.IHDR_bitDepth == 16 ? 2 : 1));
/*      */ 
/* 1127 */     initializeScaleTables(localSampleModel.getSampleSize());
/*      */ 
/* 1129 */     clearAbortRequest();
/*      */ 
/* 1131 */     processImageStarted(0);
/*      */     try
/*      */     {
/* 1134 */       write_magic();
/* 1135 */       write_IHDR();
/*      */ 
/* 1137 */       write_cHRM();
/* 1138 */       write_gAMA();
/* 1139 */       write_iCCP();
/* 1140 */       write_sBIT();
/* 1141 */       write_sRGB();
/*      */ 
/* 1143 */       write_PLTE();
/*      */ 
/* 1145 */       write_hIST();
/* 1146 */       write_tRNS();
/* 1147 */       write_bKGD();
/*      */ 
/* 1149 */       write_pHYs();
/* 1150 */       write_sPLT();
/* 1151 */       write_tIME();
/* 1152 */       write_tEXt();
/* 1153 */       write_iTXt();
/* 1154 */       write_zTXt();
/*      */ 
/* 1156 */       writeUnknownChunks();
/*      */ 
/* 1158 */       write_IDAT(localRenderedImage);
/*      */ 
/* 1160 */       if (abortRequested()) {
/* 1161 */         processWriteAborted();
/*      */       }
/*      */       else {
/* 1164 */         writeIEND();
/* 1165 */         processImageComplete();
/*      */       }
/*      */     } catch (IOException localIOException) {
/* 1168 */       throw new IIOException("I/O error writing PNG file!", localIOException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.PNGImageWriter
 * JD-Core Version:    0.6.2
 */
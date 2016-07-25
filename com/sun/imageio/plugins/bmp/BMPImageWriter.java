/*      */ package com.sun.imageio.plugins.bmp;
/*      */ 
/*      */ import com.sun.imageio.plugins.common.I18N;
/*      */ import com.sun.imageio.plugins.common.ImageUtil;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.BandedSampleModel;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ComponentSampleModel;
/*      */ import java.awt.image.DataBuffer;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.DataBufferInt;
/*      */ import java.awt.image.DataBufferShort;
/*      */ import java.awt.image.DataBufferUShort;
/*      */ import java.awt.image.DirectColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.MultiPixelPackedSampleModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.SinglePixelPackedSampleModel;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteOrder;
/*      */ import java.util.Iterator;
/*      */ import javax.imageio.IIOImage;
/*      */ import javax.imageio.ImageIO;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.ImageWriteParam;
/*      */ import javax.imageio.ImageWriter;
/*      */ import javax.imageio.event.IIOWriteProgressListener;
/*      */ import javax.imageio.event.IIOWriteWarningListener;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.plugins.bmp.BMPImageWriteParam;
/*      */ import javax.imageio.spi.ImageWriterSpi;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ 
/*      */ public class BMPImageWriter extends ImageWriter
/*      */   implements BMPConstants
/*      */ {
/*   86 */   private ImageOutputStream stream = null;
/*   87 */   private ByteArrayOutputStream embedded_stream = null;
/*      */   private int version;
/*      */   private int compressionType;
/*      */   private boolean isTopDown;
/*      */   private int w;
/*      */   private int h;
/*   92 */   private int compImageSize = 0;
/*      */   private int[] bitMasks;
/*      */   private int[] bitPos;
/*      */   private byte[] bpixels;
/*      */   private short[] spixels;
/*      */   private int[] ipixels;
/*      */ 
/*      */   public BMPImageWriter(ImageWriterSpi paramImageWriterSpi)
/*      */   {
/*  103 */     super(paramImageWriterSpi);
/*      */   }
/*      */ 
/*      */   public void setOutput(Object paramObject) {
/*  107 */     super.setOutput(paramObject);
/*  108 */     if (paramObject != null) {
/*  109 */       if (!(paramObject instanceof ImageOutputStream))
/*  110 */         throw new IllegalArgumentException(I18N.getString("BMPImageWriter0"));
/*  111 */       this.stream = ((ImageOutputStream)paramObject);
/*  112 */       this.stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
/*      */     } else {
/*  114 */       this.stream = null;
/*      */     }
/*      */   }
/*      */ 
/*  118 */   public ImageWriteParam getDefaultWriteParam() { return new BMPImageWriteParam(); }
/*      */ 
/*      */   public IIOMetadata getDefaultStreamMetadata(ImageWriteParam paramImageWriteParam)
/*      */   {
/*  122 */     return null;
/*      */   }
/*      */ 
/*      */   public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  127 */     BMPMetadata localBMPMetadata = new BMPMetadata();
/*  128 */     localBMPMetadata.bmpVersion = "BMP v. 3.x";
/*  129 */     localBMPMetadata.compression = getPreferredCompressionType(paramImageTypeSpecifier);
/*  130 */     if ((paramImageWriteParam != null) && (paramImageWriteParam.getCompressionMode() == 2))
/*      */     {
/*  132 */       localBMPMetadata.compression = getCompressionType(paramImageWriteParam.getCompressionType());
/*      */     }
/*  134 */     localBMPMetadata.bitsPerPixel = ((short)paramImageTypeSpecifier.getColorModel().getPixelSize());
/*  135 */     return localBMPMetadata;
/*      */   }
/*      */ 
/*      */   public IIOMetadata convertStreamMetadata(IIOMetadata paramIIOMetadata, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  140 */     return null;
/*      */   }
/*      */ 
/*      */   public IIOMetadata convertImageMetadata(IIOMetadata paramIIOMetadata, ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  146 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean canWriteRasters() {
/*  150 */     return true;
/*      */   }
/*      */ 
/*      */   public void write(IIOMetadata paramIIOMetadata, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/*  157 */     if (this.stream == null) {
/*  158 */       throw new IllegalStateException(I18N.getString("BMPImageWriter7"));
/*      */     }
/*      */ 
/*  161 */     if (paramIIOImage == null) {
/*  162 */       throw new IllegalArgumentException(I18N.getString("BMPImageWriter8"));
/*      */     }
/*      */ 
/*  165 */     clearAbortRequest();
/*  166 */     processImageStarted(0);
/*  167 */     if (paramImageWriteParam == null) {
/*  168 */       paramImageWriteParam = getDefaultWriteParam();
/*      */     }
/*  170 */     BMPImageWriteParam localBMPImageWriteParam = (BMPImageWriteParam)paramImageWriteParam;
/*      */ 
/*  173 */     int i = 24;
/*  174 */     int j = 0;
/*  175 */     int k = 0;
/*  176 */     IndexColorModel localIndexColorModel = null;
/*      */ 
/*  178 */     RenderedImage localRenderedImage = null;
/*  179 */     Raster localRaster1 = null;
/*  180 */     boolean bool1 = paramIIOImage.hasRaster();
/*  181 */     Object localObject1 = paramImageWriteParam.getSourceRegion();
/*  182 */     SampleModel localSampleModel1 = null;
/*  183 */     ColorModel localColorModel = null;
/*      */ 
/*  185 */     this.compImageSize = 0;
/*      */ 
/*  187 */     if (bool1) {
/*  188 */       localRaster1 = paramIIOImage.getRaster();
/*  189 */       localSampleModel1 = localRaster1.getSampleModel();
/*  190 */       localColorModel = ImageUtil.createColorModel(null, localSampleModel1);
/*  191 */       if (localObject1 == null)
/*  192 */         localObject1 = localRaster1.getBounds();
/*      */       else
/*  194 */         localObject1 = ((Rectangle)localObject1).intersection(localRaster1.getBounds());
/*      */     } else {
/*  196 */       localRenderedImage = paramIIOImage.getRenderedImage();
/*  197 */       localSampleModel1 = localRenderedImage.getSampleModel();
/*  198 */       localColorModel = localRenderedImage.getColorModel();
/*  199 */       localObject2 = new Rectangle(localRenderedImage.getMinX(), localRenderedImage.getMinY(), localRenderedImage.getWidth(), localRenderedImage.getHeight());
/*      */ 
/*  201 */       if (localObject1 == null)
/*  202 */         localObject1 = localObject2;
/*      */       else {
/*  204 */         localObject1 = ((Rectangle)localObject1).intersection((Rectangle)localObject2);
/*      */       }
/*      */     }
/*  207 */     Object localObject2 = paramIIOImage.getMetadata();
/*  208 */     BMPMetadata localBMPMetadata = null;
/*  209 */     if ((localObject2 != null) && ((localObject2 instanceof BMPMetadata)))
/*      */     {
/*  212 */       localBMPMetadata = (BMPMetadata)localObject2;
/*      */     } else {
/*  214 */       ImageTypeSpecifier localImageTypeSpecifier = new ImageTypeSpecifier(localColorModel, localSampleModel1);
/*      */ 
/*  217 */       localBMPMetadata = (BMPMetadata)getDefaultImageMetadata(localImageTypeSpecifier, paramImageWriteParam);
/*      */     }
/*      */ 
/*  221 */     if (((Rectangle)localObject1).isEmpty()) {
/*  222 */       throw new RuntimeException(I18N.getString("BMPImageWrite0"));
/*      */     }
/*  224 */     int m = paramImageWriteParam.getSourceXSubsampling();
/*  225 */     int n = paramImageWriteParam.getSourceYSubsampling();
/*  226 */     int i1 = paramImageWriteParam.getSubsamplingXOffset();
/*  227 */     int i2 = paramImageWriteParam.getSubsamplingYOffset();
/*      */ 
/*  230 */     int i3 = localSampleModel1.getDataType();
/*      */ 
/*  232 */     ((Rectangle)localObject1).translate(i1, i2);
/*  233 */     localObject1.width -= i1;
/*  234 */     localObject1.height -= i2;
/*      */ 
/*  236 */     int i4 = ((Rectangle)localObject1).x / m;
/*  237 */     int i5 = ((Rectangle)localObject1).y / n;
/*  238 */     this.w = ((((Rectangle)localObject1).width + m - 1) / m);
/*  239 */     this.h = ((((Rectangle)localObject1).height + n - 1) / n);
/*  240 */     i1 = ((Rectangle)localObject1).x % m;
/*  241 */     i2 = ((Rectangle)localObject1).y % n;
/*      */ 
/*  243 */     Rectangle localRectangle1 = new Rectangle(i4, i5, this.w, this.h);
/*  244 */     boolean bool2 = localRectangle1.equals(localObject1);
/*      */ 
/*  247 */     int[] arrayOfInt1 = paramImageWriteParam.getSourceBands();
/*  248 */     int i6 = 1;
/*  249 */     int i7 = localSampleModel1.getNumBands();
/*      */ 
/*  251 */     if (arrayOfInt1 != null) {
/*  252 */       localSampleModel1 = localSampleModel1.createSubsetSampleModel(arrayOfInt1);
/*  253 */       localColorModel = null;
/*  254 */       i6 = 0;
/*  255 */       i7 = localSampleModel1.getNumBands();
/*      */     } else {
/*  257 */       arrayOfInt1 = new int[i7];
/*  258 */       for (int i8 = 0; i8 < i7; i8++) {
/*  259 */         arrayOfInt1[i8] = i8;
/*      */       }
/*      */     }
/*  262 */     int[] arrayOfInt2 = null;
/*  263 */     boolean bool3 = true;
/*      */ 
/*  265 */     if ((localSampleModel1 instanceof ComponentSampleModel)) {
/*  266 */       arrayOfInt2 = ((ComponentSampleModel)localSampleModel1).getBandOffsets();
/*  267 */       if ((localSampleModel1 instanceof BandedSampleModel))
/*      */       {
/*  270 */         bool3 = false;
/*      */       }
/*      */       else
/*      */       {
/*  275 */         for (int i9 = 0; i9 < arrayOfInt2.length; i9++) {
/*  276 */           bool3 &= arrayOfInt2[i9] == arrayOfInt2.length - i9 - 1;
/*      */         }
/*      */       }
/*      */     }
/*  280 */     else if ((localSampleModel1 instanceof SinglePixelPackedSampleModel))
/*      */     {
/*  285 */       int[] arrayOfInt3 = ((SinglePixelPackedSampleModel)localSampleModel1).getBitOffsets();
/*  286 */       for (i11 = 0; i11 < arrayOfInt3.length - 1; i11++) {
/*  287 */         bool3 &= arrayOfInt3[i11] > arrayOfInt3[(i11 + 1)];
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  292 */     if (arrayOfInt2 == null)
/*      */     {
/*  295 */       arrayOfInt2 = new int[i7];
/*  296 */       for (int i10 = 0; i10 < i7; i10++) {
/*  297 */         arrayOfInt2[i10] = i10;
/*      */       }
/*      */     }
/*  300 */     bool2 &= bool3;
/*      */ 
/*  302 */     int[] arrayOfInt4 = localSampleModel1.getSampleSize();
/*      */ 
/*  307 */     int i11 = this.w * i7;
/*      */ 
/*  309 */     switch (localBMPImageWriteParam.getCompressionMode()) {
/*      */     case 2:
/*  311 */       this.compressionType = getCompressionType(localBMPImageWriteParam.getCompressionType());
/*  312 */       break;
/*      */     case 3:
/*  314 */       this.compressionType = localBMPMetadata.compression;
/*  315 */       break;
/*      */     case 1:
/*  317 */       this.compressionType = getPreferredCompressionType(localColorModel, localSampleModel1);
/*  318 */       break;
/*      */     default:
/*  321 */       this.compressionType = 0;
/*      */     }
/*      */ 
/*  324 */     if (!canEncodeImage(this.compressionType, localColorModel, localSampleModel1)) {
/*  325 */       throw new IOException("Image can not be encoded with compression type " + compressionTypeNames[this.compressionType]);
/*      */     }
/*      */ 
/*  329 */     byte[] arrayOfByte1 = null; byte[] arrayOfByte2 = null; byte[] arrayOfByte3 = null; byte[] arrayOfByte4 = null;
/*      */     int i12;
/*  331 */     if (this.compressionType == 3) {
/*  332 */       i = DataBuffer.getDataTypeSize(localSampleModel1.getDataType());
/*      */ 
/*  335 */       if ((i != 16) && (i != 32))
/*      */       {
/*  338 */         i = 32;
/*      */ 
/*  342 */         bool2 = false;
/*      */       }
/*      */ 
/*  345 */       i11 = this.w * i + 7 >> 3;
/*      */ 
/*  347 */       j = 1;
/*  348 */       k = 3;
/*  349 */       arrayOfByte1 = new byte[k];
/*  350 */       arrayOfByte2 = new byte[k];
/*  351 */       arrayOfByte3 = new byte[k];
/*  352 */       arrayOfByte4 = new byte[k];
/*      */ 
/*  354 */       i12 = 16711680;
/*  355 */       int i14 = 65280;
/*  356 */       i16 = 255;
/*      */ 
/*  358 */       if (i == 16)
/*      */       {
/*  365 */         if ((localColorModel instanceof DirectColorModel)) {
/*  366 */           DirectColorModel localDirectColorModel = (DirectColorModel)localColorModel;
/*  367 */           i12 = localDirectColorModel.getRedMask();
/*  368 */           i14 = localDirectColorModel.getGreenMask();
/*  369 */           i16 = localDirectColorModel.getBlueMask();
/*      */         }
/*      */         else
/*      */         {
/*  373 */           throw new IOException("Image can not be encoded with compression type " + compressionTypeNames[this.compressionType]);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  378 */       writeMaskToPalette(i12, 0, arrayOfByte1, arrayOfByte2, arrayOfByte3, arrayOfByte4);
/*  379 */       writeMaskToPalette(i14, 1, arrayOfByte1, arrayOfByte2, arrayOfByte3, arrayOfByte4);
/*  380 */       writeMaskToPalette(i16, 2, arrayOfByte1, arrayOfByte2, arrayOfByte3, arrayOfByte4);
/*      */ 
/*  382 */       if (!bool2)
/*      */       {
/*  384 */         this.bitMasks = new int[3];
/*  385 */         this.bitMasks[0] = i12;
/*  386 */         this.bitMasks[1] = i14;
/*  387 */         this.bitMasks[2] = i16;
/*      */ 
/*  389 */         this.bitPos = new int[3];
/*  390 */         this.bitPos[0] = firstLowBit(i12);
/*  391 */         this.bitPos[1] = firstLowBit(i14);
/*  392 */         this.bitPos[2] = firstLowBit(i16);
/*      */       }
/*      */ 
/*  395 */       if ((localColorModel instanceof IndexColorModel)) {
/*  396 */         localIndexColorModel = (IndexColorModel)localColorModel;
/*      */       }
/*      */     }
/*  399 */     else if ((localColorModel instanceof IndexColorModel)) {
/*  400 */       j = 1;
/*  401 */       localIndexColorModel = (IndexColorModel)localColorModel;
/*  402 */       k = localIndexColorModel.getMapSize();
/*      */ 
/*  404 */       if (k <= 2) {
/*  405 */         i = 1;
/*  406 */         i11 = this.w + 7 >> 3;
/*  407 */       } else if (k <= 16) {
/*  408 */         i = 4;
/*  409 */         i11 = this.w + 1 >> 1;
/*  410 */       } else if (k <= 256) {
/*  411 */         i = 8;
/*      */       }
/*      */       else
/*      */       {
/*  415 */         i = 24;
/*  416 */         j = 0;
/*  417 */         k = 0;
/*  418 */         i11 = this.w * 3;
/*      */       }
/*      */ 
/*  421 */       if (j == 1) {
/*  422 */         arrayOfByte1 = new byte[k];
/*  423 */         arrayOfByte2 = new byte[k];
/*  424 */         arrayOfByte3 = new byte[k];
/*  425 */         arrayOfByte4 = new byte[k];
/*      */ 
/*  427 */         localIndexColorModel.getAlphas(arrayOfByte4);
/*  428 */         localIndexColorModel.getReds(arrayOfByte1);
/*  429 */         localIndexColorModel.getGreens(arrayOfByte2);
/*  430 */         localIndexColorModel.getBlues(arrayOfByte3);
/*      */       }
/*      */ 
/*      */     }
/*  435 */     else if (i7 == 1)
/*      */     {
/*  437 */       j = 1;
/*  438 */       k = 256;
/*  439 */       i = arrayOfInt4[0];
/*      */ 
/*  441 */       i11 = this.w * i + 7 >> 3;
/*      */ 
/*  443 */       arrayOfByte1 = new byte[256];
/*  444 */       arrayOfByte2 = new byte[256];
/*  445 */       arrayOfByte3 = new byte[256];
/*  446 */       arrayOfByte4 = new byte[256];
/*      */ 
/*  448 */       for (i12 = 0; i12 < 256; i12++) {
/*  449 */         arrayOfByte1[i12] = ((byte)i12);
/*  450 */         arrayOfByte2[i12] = ((byte)i12);
/*  451 */         arrayOfByte3[i12] = ((byte)i12);
/*  452 */         arrayOfByte4[i12] = -1;
/*      */       }
/*      */ 
/*      */     }
/*  456 */     else if (((localSampleModel1 instanceof SinglePixelPackedSampleModel)) && (i6 != 0))
/*      */     {
/*  465 */       int[] arrayOfInt5 = localSampleModel1.getSampleSize();
/*  466 */       i = 0;
/*  467 */       for (i18 : arrayOfInt5) {
/*  468 */         i += i18;
/*      */       }
/*  470 */       i = roundBpp(i);
/*  471 */       if (i != DataBuffer.getDataTypeSize(localSampleModel1.getDataType())) {
/*  472 */         bool2 = false;
/*      */       }
/*  474 */       i11 = this.w * i + 7 >> 3;
/*      */     }
/*      */ 
/*  481 */     int i13 = 0;
/*  482 */     int i15 = 0;
/*  483 */     int i16 = 0;
/*  484 */     ??? = 0;
/*  485 */     int i18 = 0;
/*  486 */     int i19 = 0;
/*  487 */     int i20 = 0;
/*  488 */     int i21 = k;
/*      */ 
/*  491 */     int i22 = i11 % 4;
/*  492 */     if (i22 != 0) {
/*  493 */       i22 = 4 - i22;
/*      */     }
/*      */ 
/*  499 */     i15 = 54 + k * 4;
/*      */ 
/*  501 */     ??? = (i11 + i22) * this.h;
/*  502 */     i13 = ??? + i15;
/*  503 */     i16 = 40;
/*      */ 
/*  505 */     long l1 = this.stream.getStreamPosition();
/*      */ 
/*  507 */     writeFileHeader(i13, i15);
/*      */ 
/*  514 */     if ((this.compressionType == 0) || (this.compressionType == 3))
/*      */     {
/*  517 */       this.isTopDown = localBMPImageWriteParam.isTopDown();
/*      */     }
/*  519 */     else this.isTopDown = false;
/*      */ 
/*  522 */     writeInfoHeader(i16, i);
/*      */ 
/*  525 */     this.stream.writeInt(this.compressionType);
/*      */ 
/*  528 */     this.stream.writeInt(???);
/*      */ 
/*  531 */     this.stream.writeInt(i18);
/*      */ 
/*  534 */     this.stream.writeInt(i19);
/*      */ 
/*  537 */     this.stream.writeInt(i20);
/*      */ 
/*  540 */     this.stream.writeInt(i21);
/*      */ 
/*  543 */     if (j == 1)
/*      */     {
/*  546 */       if (this.compressionType == 3)
/*      */       {
/*  548 */         for (i23 = 0; i23 < 3; i23++) {
/*  549 */           int i24 = (arrayOfByte4[i23] & 0xFF) + (arrayOfByte1[i23] & 0xFF) * 256 + (arrayOfByte2[i23] & 0xFF) * 65536 + (arrayOfByte3[i23] & 0xFF) * 16777216;
/*  550 */           this.stream.writeInt(i24);
/*      */         }
/*      */       }
/*  553 */       else for (i23 = 0; i23 < k; i23++) {
/*  554 */           this.stream.writeByte(arrayOfByte3[i23]);
/*  555 */           this.stream.writeByte(arrayOfByte2[i23]);
/*  556 */           this.stream.writeByte(arrayOfByte1[i23]);
/*  557 */           this.stream.writeByte(arrayOfByte4[i23]);
/*      */         }
/*      */ 
/*      */ 
/*      */     }
/*      */ 
/*  563 */     int i23 = this.w * i7;
/*      */ 
/*  566 */     int[] arrayOfInt7 = new int[i23 * m];
/*      */ 
/*  570 */     this.bpixels = new byte[i11];
/*      */ 
/*  574 */     if ((this.compressionType == 4) || (this.compressionType == 5))
/*      */     {
/*  578 */       this.embedded_stream = new ByteArrayOutputStream();
/*  579 */       writeEmbedded(paramIIOImage, localBMPImageWriteParam);
/*      */ 
/*  581 */       this.embedded_stream.flush();
/*  582 */       ??? = this.embedded_stream.size();
/*      */ 
/*  584 */       long l2 = this.stream.getStreamPosition();
/*  585 */       i13 = i15 + ???;
/*  586 */       this.stream.seek(l1);
/*  587 */       writeSize(i13, 2);
/*  588 */       this.stream.seek(l1);
/*  589 */       writeSize(???, 34);
/*  590 */       this.stream.seek(l2);
/*  591 */       this.stream.write(this.embedded_stream.toByteArray());
/*  592 */       this.embedded_stream = null;
/*      */ 
/*  594 */       if (abortRequested()) {
/*  595 */         processWriteAborted();
/*      */       } else {
/*  597 */         processImageComplete();
/*  598 */         this.stream.flushBefore(this.stream.getStreamPosition());
/*      */       }
/*      */ 
/*  601 */       return;
/*      */     }
/*      */ 
/*  604 */     int i25 = arrayOfInt2[0];
/*  605 */     for (int i26 = 1; i26 < arrayOfInt2.length; i26++) {
/*  606 */       if (arrayOfInt2[i26] > i25)
/*  607 */         i25 = arrayOfInt2[i26];
/*      */     }
/*  609 */     int[] arrayOfInt8 = new int[i25 + 1];
/*      */ 
/*  611 */     int i27 = i11;
/*      */ 
/*  613 */     if ((bool2) && (i6 != 0)) {
/*  614 */       i27 = i11 / (DataBuffer.getDataTypeSize(i3) >> 3);
/*      */     }
/*  616 */     for (int i28 = 0; (i28 < this.h) && 
/*  617 */       (!abortRequested()); i28++)
/*      */     {
/*  621 */       int i29 = i5 + i28;
/*      */ 
/*  623 */       if (!this.isTopDown) {
/*  624 */         i29 = i5 + this.h - i28 - 1;
/*      */       }
/*      */ 
/*  627 */       Raster localRaster2 = localRaster1;
/*      */ 
/*  629 */       Rectangle localRectangle2 = new Rectangle(i4 * m + i1, i29 * n + i2, (this.w - 1) * m + 1, 1);
/*      */ 
/*  634 */       if (!bool1)
/*  635 */         localRaster2 = localRenderedImage.getData(localRectangle2);
/*      */       int i31;
/*      */       int i32;
/*      */       int i33;
/*  637 */       if ((bool2) && (i6 != 0)) {
/*  638 */         SampleModel localSampleModel2 = localRaster2.getSampleModel();
/*  639 */         i31 = 0;
/*  640 */         i32 = localRectangle2.x - localRaster2.getSampleModelTranslateX();
/*  641 */         i33 = localRectangle2.y - localRaster2.getSampleModelTranslateY();
/*      */         Object localObject3;
/*  642 */         if ((localSampleModel2 instanceof ComponentSampleModel)) {
/*  643 */           localObject3 = (ComponentSampleModel)localSampleModel2;
/*  644 */           i31 = ((ComponentSampleModel)localObject3).getOffset(i32, i33, 0);
/*  645 */           for (int i35 = 1; i35 < ((ComponentSampleModel)localObject3).getNumBands(); i35++) {
/*  646 */             if (i31 > ((ComponentSampleModel)localObject3).getOffset(i32, i33, i35))
/*  647 */               i31 = ((ComponentSampleModel)localObject3).getOffset(i32, i33, i35);
/*      */           }
/*      */         }
/*  650 */         else if ((localSampleModel2 instanceof MultiPixelPackedSampleModel)) {
/*  651 */           localObject3 = (MultiPixelPackedSampleModel)localSampleModel2;
/*      */ 
/*  653 */           i31 = ((MultiPixelPackedSampleModel)localObject3).getOffset(i32, i33);
/*  654 */         } else if ((localSampleModel2 instanceof SinglePixelPackedSampleModel)) {
/*  655 */           localObject3 = (SinglePixelPackedSampleModel)localSampleModel2;
/*      */ 
/*  657 */           i31 = ((SinglePixelPackedSampleModel)localObject3).getOffset(i32, i33);
/*      */         }
/*      */         int i34;
/*  660 */         if ((this.compressionType == 0) || (this.compressionType == 3)) {
/*  661 */           switch (i3) {
/*      */           case 0:
/*  663 */             localObject3 = ((DataBufferByte)localRaster2.getDataBuffer()).getData();
/*      */ 
/*  665 */             this.stream.write((byte[])localObject3, i31, i27);
/*  666 */             break;
/*      */           case 2:
/*  669 */             short[] arrayOfShort1 = ((DataBufferShort)localRaster2.getDataBuffer()).getData();
/*      */ 
/*  671 */             this.stream.writeShorts(arrayOfShort1, i31, i27);
/*  672 */             break;
/*      */           case 1:
/*  675 */             short[] arrayOfShort2 = ((DataBufferUShort)localRaster2.getDataBuffer()).getData();
/*      */ 
/*  677 */             this.stream.writeShorts(arrayOfShort2, i31, i27);
/*  678 */             break;
/*      */           case 3:
/*  681 */             int[] arrayOfInt9 = ((DataBufferInt)localRaster2.getDataBuffer()).getData();
/*      */ 
/*  683 */             this.stream.writeInts(arrayOfInt9, i31, i27);
/*      */           }
/*      */ 
/*  687 */           for (i34 = 0; i34 < i22; i34++)
/*  688 */             this.stream.writeByte(0);
/*      */         }
/*  690 */         else if (this.compressionType == 2) {
/*  691 */           if ((this.bpixels == null) || (this.bpixels.length < i23))
/*  692 */             this.bpixels = new byte[i23];
/*  693 */           localRaster2.getPixels(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, arrayOfInt7);
/*      */ 
/*  695 */           for (i34 = 0; i34 < i23; i34++) {
/*  696 */             this.bpixels[i34] = ((byte)arrayOfInt7[i34]);
/*      */           }
/*  698 */           encodeRLE4(this.bpixels, i23);
/*  699 */         } else if (this.compressionType == 1)
/*      */         {
/*  704 */           if ((this.bpixels == null) || (this.bpixels.length < i23))
/*  705 */             this.bpixels = new byte[i23];
/*  706 */           localRaster2.getPixels(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, arrayOfInt7);
/*      */ 
/*  708 */           for (i34 = 0; i34 < i23; i34++) {
/*  709 */             this.bpixels[i34] = ((byte)arrayOfInt7[i34]);
/*      */           }
/*      */ 
/*  712 */           encodeRLE8(this.bpixels, i23);
/*      */         }
/*      */       } else {
/*  715 */         localRaster2.getPixels(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, arrayOfInt7);
/*      */ 
/*  718 */         if ((m != 1) || (i25 != i7 - 1)) {
/*  719 */           int i30 = 0; i31 = 0; for (i32 = 0; i30 < this.w; 
/*  720 */             i32 += i7)
/*      */           {
/*  722 */             System.arraycopy(arrayOfInt7, i31, arrayOfInt8, 0, arrayOfInt8.length);
/*      */ 
/*  724 */             for (i33 = 0; i33 < i7; i33++)
/*      */             {
/*  726 */               arrayOfInt7[(i32 + i33)] = arrayOfInt8[arrayOfInt1[i33]];
/*      */             }
/*  720 */             i30++; i31 += m * i7;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  730 */         writePixels(0, i23, i, arrayOfInt7, i22, i7, localIndexColorModel);
/*      */       }
/*      */ 
/*  734 */       processImageProgress(100.0F * (i28 / this.h));
/*      */     }
/*      */ 
/*  737 */     if ((this.compressionType == 2) || (this.compressionType == 1))
/*      */     {
/*  740 */       this.stream.writeByte(0);
/*  741 */       this.stream.writeByte(1);
/*  742 */       incCompImageSize(2);
/*      */ 
/*  744 */       ??? = this.compImageSize;
/*  745 */       i13 = this.compImageSize + i15;
/*  746 */       long l3 = this.stream.getStreamPosition();
/*  747 */       this.stream.seek(l1);
/*  748 */       writeSize(i13, 2);
/*  749 */       this.stream.seek(l1);
/*  750 */       writeSize(???, 34);
/*  751 */       this.stream.seek(l3);
/*      */     }
/*      */ 
/*  754 */     if (abortRequested()) {
/*  755 */       processWriteAborted();
/*      */     } else {
/*  757 */       processImageComplete();
/*  758 */       this.stream.flushBefore(this.stream.getStreamPosition());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writePixels(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt, int paramInt4, int paramInt5, IndexColorModel paramIndexColorModel)
/*      */     throws IOException
/*      */   {
/*  766 */     int i = 0;
/*  767 */     int j = 0;
/*      */     int n;
/*      */     int m;
/*  768 */     switch (paramInt3)
/*      */     {
/*      */     case 1:
/*  772 */       for (int k = 0; k < paramInt2 / 8; k++) {
/*  773 */         this.bpixels[(j++)] = ((byte)(paramArrayOfInt[(paramInt1++)] << 7 | paramArrayOfInt[(paramInt1++)] << 6 | paramArrayOfInt[(paramInt1++)] << 5 | paramArrayOfInt[(paramInt1++)] << 4 | paramArrayOfInt[(paramInt1++)] << 3 | paramArrayOfInt[(paramInt1++)] << 2 | paramArrayOfInt[(paramInt1++)] << 1 | paramArrayOfInt[(paramInt1++)]));
/*      */       }
/*      */ 
/*  784 */       if (paramInt2 % 8 > 0) {
/*  785 */         i = 0;
/*  786 */         for (k = 0; k < paramInt2 % 8; k++) {
/*  787 */           i |= paramArrayOfInt[(paramInt1++)] << 7 - k;
/*      */         }
/*  789 */         this.bpixels[(j++)] = ((byte)i);
/*      */       }
/*  791 */       this.stream.write(this.bpixels, 0, (paramInt2 + 7) / 8);
/*      */ 
/*  793 */       break;
/*      */     case 4:
/*  796 */       if (this.compressionType == 2) {
/*  797 */         byte[] arrayOfByte1 = new byte[paramInt2];
/*  798 */         for (n = 0; n < paramInt2; n++) {
/*  799 */           arrayOfByte1[n] = ((byte)paramArrayOfInt[(paramInt1++)]);
/*      */         }
/*  801 */         encodeRLE4(arrayOfByte1, paramInt2);
/*      */       } else {
/*  803 */         for (m = 0; m < paramInt2 / 2; m++) {
/*  804 */           i = paramArrayOfInt[(paramInt1++)] << 4 | paramArrayOfInt[(paramInt1++)];
/*  805 */           this.bpixels[(j++)] = ((byte)i);
/*      */         }
/*      */ 
/*  808 */         if (paramInt2 % 2 == 1) {
/*  809 */           i = paramArrayOfInt[paramInt1] << 4;
/*  810 */           this.bpixels[(j++)] = ((byte)i);
/*      */         }
/*  812 */         this.stream.write(this.bpixels, 0, (paramInt2 + 1) / 2);
/*      */       }
/*  814 */       break;
/*      */     case 8:
/*  817 */       if (this.compressionType == 1) {
/*  818 */         for (m = 0; m < paramInt2; m++) {
/*  819 */           this.bpixels[m] = ((byte)paramArrayOfInt[(paramInt1++)]);
/*      */         }
/*  821 */         encodeRLE8(this.bpixels, paramInt2);
/*      */       } else {
/*  823 */         for (m = 0; m < paramInt2; m++) {
/*  824 */           this.bpixels[m] = ((byte)paramArrayOfInt[(paramInt1++)]);
/*      */         }
/*  826 */         this.stream.write(this.bpixels, 0, paramInt2);
/*      */       }
/*  828 */       break;
/*      */     case 16:
/*  831 */       if (this.spixels == null) {
/*  832 */         this.spixels = new short[paramInt2 / paramInt5];
/*      */       }
/*      */ 
/*  842 */       m = 0; for (n = 0; m < paramInt2; n++) {
/*  843 */         this.spixels[n] = 0;
/*  844 */         if (this.compressionType == 0)
/*      */         {
/*  849 */           this.spixels[n] = ((short)((0x1F & paramArrayOfInt[m]) << 10 | (0x1F & paramArrayOfInt[(m + 1)]) << 5 | 0x1F & paramArrayOfInt[(m + 2)]));
/*      */ 
/*  853 */           m += 3;
/*      */         } else {
/*  855 */           for (int i2 = 0; i2 < paramInt5; m++)
/*      */           {
/*      */             int tmp612_610 = n;
/*      */             short[] tmp612_607 = this.spixels; tmp612_607[tmp612_610] = ((short)(tmp612_607[tmp612_610] | paramArrayOfInt[m] << this.bitPos[i2] & this.bitMasks[i2]));
/*      */ 
/*  855 */             i2++;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  861 */       this.stream.writeShorts(this.spixels, 0, this.spixels.length);
/*  862 */       break;
/*      */     case 24:
/*  865 */       if (paramInt5 == 3) {
/*  866 */         for (m = 0; m < paramInt2; m += 3)
/*      */         {
/*  868 */           this.bpixels[(j++)] = ((byte)paramArrayOfInt[(paramInt1 + 2)]);
/*  869 */           this.bpixels[(j++)] = ((byte)paramArrayOfInt[(paramInt1 + 1)]);
/*  870 */           this.bpixels[(j++)] = ((byte)paramArrayOfInt[paramInt1]);
/*  871 */           paramInt1 += 3;
/*      */         }
/*  873 */         this.stream.write(this.bpixels, 0, paramInt2);
/*      */       }
/*      */       else {
/*  876 */         m = paramIndexColorModel.getMapSize();
/*      */ 
/*  878 */         byte[] arrayOfByte2 = new byte[m];
/*  879 */         byte[] arrayOfByte3 = new byte[m];
/*  880 */         byte[] arrayOfByte4 = new byte[m];
/*      */ 
/*  882 */         paramIndexColorModel.getReds(arrayOfByte2);
/*  883 */         paramIndexColorModel.getGreens(arrayOfByte3);
/*  884 */         paramIndexColorModel.getBlues(arrayOfByte4);
/*      */ 
/*  887 */         for (int i5 = 0; i5 < paramInt2; i5++) {
/*  888 */           int i4 = paramArrayOfInt[paramInt1];
/*  889 */           this.bpixels[(j++)] = arrayOfByte4[i4];
/*  890 */           this.bpixels[(j++)] = arrayOfByte3[i4];
/*  891 */           this.bpixels[(j++)] = arrayOfByte4[i4];
/*  892 */           paramInt1++;
/*      */         }
/*  894 */         this.stream.write(this.bpixels, 0, paramInt2 * 3);
/*      */       }
/*  896 */       break;
/*      */     case 32:
/*  899 */       if (this.ipixels == null)
/*  900 */         this.ipixels = new int[paramInt2 / paramInt5];
/*  901 */       if (paramInt5 == 3)
/*      */       {
/*  911 */         m = 0; for (int i1 = 0; m < paramInt2; i1++) {
/*  912 */           this.ipixels[i1] = 0;
/*  913 */           if (this.compressionType == 0) {
/*  914 */             this.ipixels[i1] = ((0xFF & paramArrayOfInt[(m + 2)]) << 16 | (0xFF & paramArrayOfInt[(m + 1)]) << 8 | 0xFF & paramArrayOfInt[m]);
/*      */ 
/*  918 */             m += 3;
/*      */           } else {
/*  920 */             for (int i3 = 0; i3 < paramInt5; m++) {
/*  921 */               this.ipixels[i1] |= paramArrayOfInt[m] << this.bitPos[i3] & this.bitMasks[i3];
/*      */ 
/*  920 */               i3++;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  934 */         for (m = 0; m < paramInt2; m++) {
/*  935 */           if (paramIndexColorModel != null)
/*  936 */             this.ipixels[m] = paramIndexColorModel.getRGB(paramArrayOfInt[m]);
/*      */           else {
/*  938 */             this.ipixels[m] = (paramArrayOfInt[m] << 16 | paramArrayOfInt[m] << 8 | paramArrayOfInt[m]);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  943 */       this.stream.writeInts(this.ipixels, 0, this.ipixels.length);
/*      */     }
/*      */ 
/*  948 */     if ((this.compressionType == 0) || (this.compressionType == 3))
/*      */     {
/*  951 */       for (j = 0; j < paramInt4; j++)
/*  952 */         this.stream.writeByte(0);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void encodeRLE8(byte[] paramArrayOfByte, int paramInt)
/*      */     throws IOException
/*      */   {
/*  960 */     int i = 1; int j = -1; int k = -1;
/*  961 */     int m = 0; int n = 0;
/*      */ 
/*  963 */     m = paramArrayOfByte[(++k)];
/*  964 */     byte[] arrayOfByte = new byte[256];
/*      */ 
/*  966 */     while (k < paramInt - 1) {
/*  967 */       n = paramArrayOfByte[(++k)];
/*      */       int i1;
/*  968 */       if (n == m) {
/*  969 */         if (j >= 3)
/*      */         {
/*  971 */           this.stream.writeByte(0);
/*  972 */           this.stream.writeByte(j);
/*  973 */           incCompImageSize(2);
/*  974 */           for (i1 = 0; i1 < j; i1++) {
/*  975 */             this.stream.writeByte(arrayOfByte[i1]);
/*  976 */             incCompImageSize(1);
/*      */           }
/*  978 */           if (!isEven(j))
/*      */           {
/*  980 */             this.stream.writeByte(0);
/*  981 */             incCompImageSize(1);
/*      */           }
/*      */         }
/*  984 */         else if (j > -1)
/*      */         {
/*  989 */           for (i1 = 0; i1 < j; i1++) {
/*  990 */             this.stream.writeByte(1);
/*  991 */             this.stream.writeByte(arrayOfByte[i1]);
/*  992 */             incCompImageSize(2);
/*      */           }
/*      */         }
/*  995 */         j = -1;
/*  996 */         i++;
/*  997 */         if (i == 256)
/*      */         {
/*  999 */           this.stream.writeByte(i - 1);
/* 1000 */           this.stream.writeByte(m);
/* 1001 */           incCompImageSize(2);
/* 1002 */           i = 1;
/*      */         }
/*      */       }
/*      */       else {
/* 1006 */         if (i > 1)
/*      */         {
/* 1008 */           this.stream.writeByte(i);
/* 1009 */           this.stream.writeByte(m);
/* 1010 */           incCompImageSize(2);
/* 1011 */         } else if (j < 0)
/*      */         {
/* 1013 */           arrayOfByte[(++j)] = m;
/* 1014 */           arrayOfByte[(++j)] = n;
/* 1015 */         } else if (j < 254)
/*      */         {
/* 1017 */           arrayOfByte[(++j)] = n;
/*      */         } else {
/* 1019 */           this.stream.writeByte(0);
/* 1020 */           this.stream.writeByte(j + 1);
/* 1021 */           incCompImageSize(2);
/* 1022 */           for (i1 = 0; i1 <= j; i1++) {
/* 1023 */             this.stream.writeByte(arrayOfByte[i1]);
/* 1024 */             incCompImageSize(1);
/*      */           }
/*      */ 
/* 1027 */           this.stream.writeByte(0);
/* 1028 */           incCompImageSize(1);
/* 1029 */           j = -1;
/*      */         }
/* 1031 */         m = n;
/* 1032 */         i = 1;
/*      */       }
/*      */ 
/* 1035 */       if (k == paramInt - 1)
/*      */       {
/* 1037 */         if (j == -1) {
/* 1038 */           this.stream.writeByte(i);
/* 1039 */           this.stream.writeByte(m);
/* 1040 */           incCompImageSize(2);
/* 1041 */           i = 1;
/*      */         }
/* 1045 */         else if (j >= 2) {
/* 1046 */           this.stream.writeByte(0);
/* 1047 */           this.stream.writeByte(j + 1);
/* 1048 */           incCompImageSize(2);
/* 1049 */           for (i1 = 0; i1 <= j; i1++) {
/* 1050 */             this.stream.writeByte(arrayOfByte[i1]);
/* 1051 */             incCompImageSize(1);
/*      */           }
/* 1053 */           if (!isEven(j + 1))
/*      */           {
/* 1055 */             this.stream.writeByte(0);
/* 1056 */             incCompImageSize(1);
/*      */           }
/*      */ 
/*      */         }
/* 1060 */         else if (j > -1) {
/* 1061 */           for (i1 = 0; i1 <= j; i1++) {
/* 1062 */             this.stream.writeByte(1);
/* 1063 */             this.stream.writeByte(arrayOfByte[i1]);
/* 1064 */             incCompImageSize(2);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1070 */         this.stream.writeByte(0);
/* 1071 */         this.stream.writeByte(0);
/* 1072 */         incCompImageSize(2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void encodeRLE4(byte[] paramArrayOfByte, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1080 */     int i = 2; int j = -1; int k = -1; int m = 0; int n = 0;
/* 1081 */     int i1 = 0; int i2 = 0; int i3 = 0; int i4 = 0;
/* 1082 */     byte[] arrayOfByte = new byte[256];
/*      */ 
/* 1085 */     i1 = paramArrayOfByte[(++k)];
/* 1086 */     i2 = paramArrayOfByte[(++k)];
/*      */ 
/* 1088 */     while (k < paramInt - 2) {
/* 1089 */       i3 = paramArrayOfByte[(++k)];
/* 1090 */       i4 = paramArrayOfByte[(++k)];
/*      */       int i5;
/* 1092 */       if (i3 == i1)
/*      */       {
/* 1095 */         if (j >= 4) {
/* 1096 */           this.stream.writeByte(0);
/* 1097 */           this.stream.writeByte(j - 1);
/* 1098 */           incCompImageSize(2);
/*      */ 
/* 1101 */           for (i5 = 0; i5 < j - 2; i5 += 2) {
/* 1102 */             m = arrayOfByte[i5] << 4 | arrayOfByte[(i5 + 1)];
/* 1103 */             this.stream.writeByte((byte)m);
/* 1104 */             incCompImageSize(1);
/*      */           }
/*      */ 
/* 1107 */           if (!isEven(j - 1)) {
/* 1108 */             n = arrayOfByte[(j - 2)] << 4 | 0x0;
/* 1109 */             this.stream.writeByte(n);
/* 1110 */             incCompImageSize(1);
/*      */           }
/*      */ 
/* 1113 */           if (!isEven((int)Math.ceil((j - 1) / 2))) {
/* 1114 */             this.stream.writeByte(0);
/* 1115 */             incCompImageSize(1);
/*      */           }
/* 1117 */         } else if (j > -1) {
/* 1118 */           this.stream.writeByte(2);
/* 1119 */           m = arrayOfByte[0] << 4 | arrayOfByte[1];
/* 1120 */           this.stream.writeByte(m);
/* 1121 */           incCompImageSize(2);
/*      */         }
/* 1123 */         j = -1;
/*      */ 
/* 1125 */         if (i4 == i2)
/*      */         {
/* 1127 */           i += 2;
/* 1128 */           if (i == 256) {
/* 1129 */             this.stream.writeByte(i - 1);
/* 1130 */             m = i1 << 4 | i2;
/* 1131 */             this.stream.writeByte(m);
/* 1132 */             incCompImageSize(2);
/* 1133 */             i = 2;
/* 1134 */             if (k < paramInt - 1) {
/* 1135 */               i1 = i2;
/* 1136 */               i2 = paramArrayOfByte[(++k)];
/*      */             } else {
/* 1138 */               this.stream.writeByte(1);
/* 1139 */               i5 = i2 << 4 | 0x0;
/* 1140 */               this.stream.writeByte(i5);
/* 1141 */               incCompImageSize(2);
/* 1142 */               i = -1;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1149 */           i++;
/* 1150 */           m = i1 << 4 | i2;
/* 1151 */           this.stream.writeByte(i);
/* 1152 */           this.stream.writeByte(m);
/* 1153 */           incCompImageSize(2);
/* 1154 */           i = 2;
/* 1155 */           i1 = i4;
/*      */ 
/* 1157 */           if (k < paramInt - 1) {
/* 1158 */             i2 = paramArrayOfByte[(++k)];
/*      */           } else {
/* 1160 */             this.stream.writeByte(1);
/* 1161 */             i5 = i4 << 4 | 0x0;
/* 1162 */             this.stream.writeByte(i5);
/* 1163 */             incCompImageSize(2);
/* 1164 */             i = -1;
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1170 */         if (i > 2) {
/* 1171 */           m = i1 << 4 | i2;
/* 1172 */           this.stream.writeByte(i);
/* 1173 */           this.stream.writeByte(m);
/* 1174 */           incCompImageSize(2);
/* 1175 */         } else if (j < 0) {
/* 1176 */           arrayOfByte[(++j)] = i1;
/* 1177 */           arrayOfByte[(++j)] = i2;
/* 1178 */           arrayOfByte[(++j)] = i3;
/* 1179 */           arrayOfByte[(++j)] = i4;
/* 1180 */         } else if (j < 253) {
/* 1181 */           arrayOfByte[(++j)] = i3;
/* 1182 */           arrayOfByte[(++j)] = i4;
/*      */         } else {
/* 1184 */           this.stream.writeByte(0);
/* 1185 */           this.stream.writeByte(j + 1);
/* 1186 */           incCompImageSize(2);
/* 1187 */           for (i5 = 0; i5 < j; i5 += 2) {
/* 1188 */             m = arrayOfByte[i5] << 4 | arrayOfByte[(i5 + 1)];
/* 1189 */             this.stream.writeByte((byte)m);
/* 1190 */             incCompImageSize(1);
/*      */           }
/*      */ 
/* 1194 */           this.stream.writeByte(0);
/* 1195 */           incCompImageSize(1);
/* 1196 */           j = -1;
/*      */         }
/*      */ 
/* 1199 */         i1 = i3;
/* 1200 */         i2 = i4;
/* 1201 */         i = 2;
/*      */       }
/*      */ 
/* 1204 */       if (k >= paramInt - 2) {
/* 1205 */         if ((j == -1) && (i >= 2)) {
/* 1206 */           if (k == paramInt - 2) {
/* 1207 */             if (paramArrayOfByte[(++k)] == i1) {
/* 1208 */               i++;
/* 1209 */               m = i1 << 4 | i2;
/* 1210 */               this.stream.writeByte(i);
/* 1211 */               this.stream.writeByte(m);
/* 1212 */               incCompImageSize(2);
/*      */             } else {
/* 1214 */               m = i1 << 4 | i2;
/* 1215 */               this.stream.writeByte(i);
/* 1216 */               this.stream.writeByte(m);
/* 1217 */               this.stream.writeByte(1);
/* 1218 */               m = paramArrayOfByte[k] << 4 | 0x0;
/* 1219 */               this.stream.writeByte(m);
/* 1220 */               i5 = paramArrayOfByte[k] << 4 | 0x0;
/* 1221 */               incCompImageSize(4);
/*      */             }
/*      */           } else {
/* 1224 */             this.stream.writeByte(i);
/* 1225 */             m = i1 << 4 | i2;
/* 1226 */             this.stream.writeByte(m);
/* 1227 */             incCompImageSize(2);
/*      */           }
/* 1229 */         } else if (j > -1) {
/* 1230 */           if (k == paramInt - 2) {
/* 1231 */             arrayOfByte[(++j)] = paramArrayOfByte[(++k)];
/*      */           }
/* 1233 */           if (j >= 2) {
/* 1234 */             this.stream.writeByte(0);
/* 1235 */             this.stream.writeByte(j + 1);
/* 1236 */             incCompImageSize(2);
/* 1237 */             for (i5 = 0; i5 < j; i5 += 2) {
/* 1238 */               m = arrayOfByte[i5] << 4 | arrayOfByte[(i5 + 1)];
/* 1239 */               this.stream.writeByte((byte)m);
/* 1240 */               incCompImageSize(1);
/*      */             }
/* 1242 */             if (!isEven(j + 1)) {
/* 1243 */               n = arrayOfByte[j] << 4 | 0x0;
/* 1244 */               this.stream.writeByte(n);
/* 1245 */               incCompImageSize(1);
/*      */             }
/*      */ 
/* 1249 */             if (!isEven((int)Math.ceil((j + 1) / 2))) {
/* 1250 */               this.stream.writeByte(0);
/* 1251 */               incCompImageSize(1);
/*      */             }
/*      */           }
/*      */           else {
/* 1255 */             switch (j) {
/*      */             case 0:
/* 1257 */               this.stream.writeByte(1);
/* 1258 */               i5 = arrayOfByte[0] << 4 | 0x0;
/* 1259 */               this.stream.writeByte(i5);
/* 1260 */               incCompImageSize(2);
/* 1261 */               break;
/*      */             case 1:
/* 1263 */               this.stream.writeByte(2);
/* 1264 */               m = arrayOfByte[0] << 4 | arrayOfByte[1];
/* 1265 */               this.stream.writeByte(m);
/* 1266 */               incCompImageSize(2);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1272 */         this.stream.writeByte(0);
/* 1273 */         this.stream.writeByte(0);
/* 1274 */         incCompImageSize(2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void incCompImageSize(int paramInt)
/*      */   {
/* 1281 */     this.compImageSize += paramInt;
/*      */   }
/*      */ 
/*      */   private boolean isEven(int paramInt) {
/* 1285 */     return paramInt % 2 == 0;
/*      */   }
/*      */ 
/*      */   private void writeFileHeader(int paramInt1, int paramInt2) throws IOException
/*      */   {
/* 1290 */     this.stream.writeByte(66);
/* 1291 */     this.stream.writeByte(77);
/*      */ 
/* 1294 */     this.stream.writeInt(paramInt1);
/*      */ 
/* 1297 */     this.stream.writeInt(0);
/*      */ 
/* 1300 */     this.stream.writeInt(paramInt2);
/*      */   }
/*      */ 
/*      */   private void writeInfoHeader(int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1307 */     this.stream.writeInt(paramInt1);
/*      */ 
/* 1310 */     this.stream.writeInt(this.w);
/*      */ 
/* 1313 */     this.stream.writeInt(this.isTopDown ? -this.h : this.h);
/*      */ 
/* 1316 */     this.stream.writeShort(1);
/*      */ 
/* 1319 */     this.stream.writeShort(paramInt2);
/*      */   }
/*      */ 
/*      */   private void writeSize(int paramInt1, int paramInt2) throws IOException {
/* 1323 */     this.stream.skipBytes(paramInt2);
/* 1324 */     this.stream.writeInt(paramInt1);
/*      */   }
/*      */ 
/*      */   public void reset() {
/* 1328 */     super.reset();
/* 1329 */     this.stream = null;
/*      */   }
/*      */ 
/*      */   private int getCompressionType(String paramString) {
/* 1333 */     for (int i = 0; i < BMPConstants.compressionTypeNames.length; i++)
/* 1334 */       if (BMPConstants.compressionTypeNames[i].equals(paramString))
/* 1335 */         return i;
/* 1336 */     return 0; } 
/*      */   private void writeEmbedded(IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam) throws IOException { // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 485	com/sun/imageio/plugins/bmp/BMPImageWriter:compressionType	I
/*      */     //   4: iconst_4
/*      */     //   5: if_icmpne +8 -> 13
/*      */     //   8: ldc 15
/*      */     //   10: goto +5 -> 15
/*      */     //   13: ldc 16
/*      */     //   15: astore_3
/*      */     //   16: aload_3
/*      */     //   17: invokestatic 592	javax/imageio/ImageIO:getImageWritersByFormatName	(Ljava/lang/String;)Ljava/util/Iterator;
/*      */     //   20: astore 4
/*      */     //   22: aconst_null
/*      */     //   23: astore 5
/*      */     //   25: aload 4
/*      */     //   27: invokeinterface 630 1 0
/*      */     //   32: ifeq +15 -> 47
/*      */     //   35: aload 4
/*      */     //   37: invokeinterface 631 1 0
/*      */     //   42: checkcast 290	javax/imageio/ImageWriter
/*      */     //   45: astore 5
/*      */     //   47: aload 5
/*      */     //   49: ifnull +125 -> 174
/*      */     //   52: aload_0
/*      */     //   53: getfield 494	com/sun/imageio/plugins/bmp/BMPImageWriter:embedded_stream	Ljava/io/ByteArrayOutputStream;
/*      */     //   56: ifnonnull +13 -> 69
/*      */     //   59: new 280	java/lang/RuntimeException
/*      */     //   62: dup
/*      */     //   63: ldc 14
/*      */     //   65: invokespecial 582	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
/*      */     //   68: athrow
/*      */     //   69: aload 5
/*      */     //   71: new 253	com/sun/imageio/plugins/bmp/BMPImageWriter$1
/*      */     //   74: dup
/*      */     //   75: aload_0
/*      */     //   76: invokespecial 532	com/sun/imageio/plugins/bmp/BMPImageWriter$1:<init>	(Lcom/sun/imageio/plugins/bmp/BMPImageWriter;)V
/*      */     //   79: invokevirtual 614	javax/imageio/ImageWriter:addIIOWriteProgressListener	(Ljavax/imageio/event/IIOWriteProgressListener;)V
/*      */     //   82: aload 5
/*      */     //   84: new 254	com/sun/imageio/plugins/bmp/BMPImageWriter$2
/*      */     //   87: dup
/*      */     //   88: aload_0
/*      */     //   89: invokespecial 533	com/sun/imageio/plugins/bmp/BMPImageWriter$2:<init>	(Lcom/sun/imageio/plugins/bmp/BMPImageWriter;)V
/*      */     //   92: invokevirtual 615	javax/imageio/ImageWriter:addIIOWriteWarningListener	(Ljavax/imageio/event/IIOWriteWarningListener;)V
/*      */     //   95: aload 5
/*      */     //   97: aload_0
/*      */     //   98: getfield 494	com/sun/imageio/plugins/bmp/BMPImageWriter:embedded_stream	Ljava/io/ByteArrayOutputStream;
/*      */     //   101: invokestatic 593	javax/imageio/ImageIO:createImageOutputStream	(Ljava/lang/Object;)Ljavax/imageio/stream/ImageOutputStream;
/*      */     //   104: invokevirtual 612	javax/imageio/ImageWriter:setOutput	(Ljava/lang/Object;)V
/*      */     //   107: aload 5
/*      */     //   109: invokevirtual 613	javax/imageio/ImageWriter:getDefaultWriteParam	()Ljavax/imageio/ImageWriteParam;
/*      */     //   112: astore 6
/*      */     //   114: aload 6
/*      */     //   116: aload_2
/*      */     //   117: invokevirtual 606	javax/imageio/ImageWriteParam:getDestinationOffset	()Ljava/awt/Point;
/*      */     //   120: invokevirtual 607	javax/imageio/ImageWriteParam:setDestinationOffset	(Ljava/awt/Point;)V
/*      */     //   123: aload 6
/*      */     //   125: aload_2
/*      */     //   126: invokevirtual 603	javax/imageio/ImageWriteParam:getSourceBands	()[I
/*      */     //   129: invokevirtual 605	javax/imageio/ImageWriteParam:setSourceBands	([I)V
/*      */     //   132: aload 6
/*      */     //   134: aload_2
/*      */     //   135: invokevirtual 608	javax/imageio/ImageWriteParam:getSourceRegion	()Ljava/awt/Rectangle;
/*      */     //   138: invokevirtual 609	javax/imageio/ImageWriteParam:setSourceRegion	(Ljava/awt/Rectangle;)V
/*      */     //   141: aload 6
/*      */     //   143: aload_2
/*      */     //   144: invokevirtual 599	javax/imageio/ImageWriteParam:getSourceXSubsampling	()I
/*      */     //   147: aload_2
/*      */     //   148: invokevirtual 600	javax/imageio/ImageWriteParam:getSourceYSubsampling	()I
/*      */     //   151: aload_2
/*      */     //   152: invokevirtual 601	javax/imageio/ImageWriteParam:getSubsamplingXOffset	()I
/*      */     //   155: aload_2
/*      */     //   156: invokevirtual 602	javax/imageio/ImageWriteParam:getSubsamplingYOffset	()I
/*      */     //   159: invokevirtual 604	javax/imageio/ImageWriteParam:setSourceSubsampling	(IIII)V
/*      */     //   162: aload 5
/*      */     //   164: aconst_null
/*      */     //   165: aload_1
/*      */     //   166: aload 6
/*      */     //   168: invokevirtual 617	javax/imageio/ImageWriter:write	(Ljavax/imageio/metadata/IIOMetadata;Ljavax/imageio/IIOImage;Ljavax/imageio/ImageWriteParam;)V
/*      */     //   171: goto +38 -> 209
/*      */     //   174: new 280	java/lang/RuntimeException
/*      */     //   177: dup
/*      */     //   178: new 282	java/lang/StringBuilder
/*      */     //   181: dup
/*      */     //   182: invokespecial 584	java/lang/StringBuilder:<init>	()V
/*      */     //   185: ldc 9
/*      */     //   187: invokestatic 535	com/sun/imageio/plugins/common/I18N:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   190: invokevirtual 586	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   193: ldc 6
/*      */     //   195: invokevirtual 586	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   198: aload_3
/*      */     //   199: invokevirtual 586	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   202: invokevirtual 585	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   205: invokespecial 582	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
/*      */     //   208: athrow
/*      */     //   209: return } 
/* 1381 */   private int firstLowBit(int paramInt) { int i = 0;
/* 1382 */     while ((paramInt & 0x1) == 0) {
/* 1383 */       i++;
/* 1384 */       paramInt >>>= 1;
/*      */     }
/* 1386 */     return i;
/*      */   }
/*      */ 
/*      */   protected int getPreferredCompressionType(ColorModel paramColorModel, SampleModel paramSampleModel)
/*      */   {
/* 1424 */     ImageTypeSpecifier localImageTypeSpecifier = new ImageTypeSpecifier(paramColorModel, paramSampleModel);
/* 1425 */     return getPreferredCompressionType(localImageTypeSpecifier);
/*      */   }
/*      */ 
/*      */   protected int getPreferredCompressionType(ImageTypeSpecifier paramImageTypeSpecifier) {
/* 1429 */     if (paramImageTypeSpecifier.getBufferedImageType() == 8) {
/* 1430 */       return 3;
/*      */     }
/* 1432 */     return 0;
/*      */   }
/*      */ 
/*      */   protected boolean canEncodeImage(int paramInt, ColorModel paramColorModel, SampleModel paramSampleModel)
/*      */   {
/* 1444 */     ImageTypeSpecifier localImageTypeSpecifier = new ImageTypeSpecifier(paramColorModel, paramSampleModel);
/* 1445 */     return canEncodeImage(paramInt, localImageTypeSpecifier);
/*      */   }
/*      */ 
/*      */   protected boolean canEncodeImage(int paramInt, ImageTypeSpecifier paramImageTypeSpecifier) {
/* 1449 */     ImageWriterSpi localImageWriterSpi = getOriginatingProvider();
/* 1450 */     if (!localImageWriterSpi.canEncodeImage(paramImageTypeSpecifier)) {
/* 1451 */       return false;
/*      */     }
/* 1453 */     int i = paramImageTypeSpecifier.getBufferedImageType();
/* 1454 */     int j = paramImageTypeSpecifier.getColorModel().getPixelSize();
/* 1455 */     if ((this.compressionType == 2) && (j != 4))
/*      */     {
/* 1457 */       return false;
/*      */     }
/* 1459 */     if ((this.compressionType == 1) && (j != 8))
/*      */     {
/* 1461 */       return false;
/*      */     }
/* 1463 */     if (j == 16)
/*      */     {
/* 1490 */       int k = 0;
/* 1491 */       int m = 0;
/*      */ 
/* 1493 */       SampleModel localSampleModel = paramImageTypeSpecifier.getSampleModel();
/* 1494 */       if ((localSampleModel instanceof SinglePixelPackedSampleModel)) {
/* 1495 */         int[] arrayOfInt = ((SinglePixelPackedSampleModel)localSampleModel).getSampleSize();
/*      */ 
/* 1498 */         k = 1;
/* 1499 */         m = 1;
/* 1500 */         for (int n = 0; n < arrayOfInt.length; n++) {
/* 1501 */           k &= (arrayOfInt[n] == 5 ? 1 : 0);
/* 1502 */           m &= ((arrayOfInt[n] == 5) || ((n == 1) && (arrayOfInt[n] == 6)) ? 1 : 0);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1507 */       return ((this.compressionType == 0) && (k != 0)) || ((this.compressionType == 3) && (m != 0));
/*      */     }
/*      */ 
/* 1510 */     return true;
/*      */   }
/*      */ 
/*      */   protected void writeMaskToPalette(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4)
/*      */   {
/* 1515 */     paramArrayOfByte3[paramInt2] = ((byte)(0xFF & paramInt1 >> 24));
/* 1516 */     paramArrayOfByte2[paramInt2] = ((byte)(0xFF & paramInt1 >> 16));
/* 1517 */     paramArrayOfByte1[paramInt2] = ((byte)(0xFF & paramInt1 >> 8));
/* 1518 */     paramArrayOfByte4[paramInt2] = ((byte)(0xFF & paramInt1));
/*      */   }
/*      */ 
/*      */   private int roundBpp(int paramInt) {
/* 1522 */     if (paramInt <= 8)
/* 1523 */       return 8;
/* 1524 */     if (paramInt <= 16)
/* 1525 */       return 16;
/* 1526 */     if (paramInt <= 24) {
/* 1527 */       return 24;
/*      */     }
/* 1529 */     return 32;
/*      */   }
/*      */ 
/*      */   private class IIOWriteProgressAdapter
/*      */     implements IIOWriteProgressListener
/*      */   {
/*      */     private IIOWriteProgressAdapter()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void imageComplete(ImageWriter paramImageWriter)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void imageProgress(ImageWriter paramImageWriter, float paramFloat)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void imageStarted(ImageWriter paramImageWriter, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void thumbnailComplete(ImageWriter paramImageWriter)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void thumbnailProgress(ImageWriter paramImageWriter, float paramFloat)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void thumbnailStarted(ImageWriter paramImageWriter, int paramInt1, int paramInt2)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void writeAborted(ImageWriter paramImageWriter)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.bmp.BMPImageWriter
 * JD-Core Version:    0.6.2
 */
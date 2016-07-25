/*      */ package com.sun.imageio.plugins.gif;
/*      */ 
/*      */ import com.sun.imageio.plugins.common.LZWCompressor;
/*      */ import com.sun.imageio.plugins.common.PaletteBuilder;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ComponentSampleModel;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteOrder;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.IIOImage;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.ImageWriteParam;
/*      */ import javax.imageio.ImageWriter;
/*      */ import javax.imageio.metadata.IIOInvalidTreeException;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.metadata.IIOMetadataNode;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ import sun.awt.image.ByteComponentRaster;
/*      */ 
/*      */ public class GIFImageWriter extends ImageWriter
/*      */ {
/*      */   private static final boolean DEBUG = false;
/*      */   static final String STANDARD_METADATA_NAME = "javax_imageio_1.0";
/*      */   static final String STREAM_METADATA_NAME = "javax_imageio_gif_stream_1.0";
/*      */   static final String IMAGE_METADATA_NAME = "javax_imageio_gif_image_1.0";
/*   75 */   private ImageOutputStream stream = null;
/*      */ 
/*   80 */   private boolean isWritingSequence = false;
/*      */ 
/*   85 */   private boolean wroteSequenceHeader = false;
/*      */ 
/*   90 */   private GIFWritableStreamMetadata theStreamMetadata = null;
/*      */ 
/*   95 */   private int imageIndex = 0;
/*      */ 
/*      */   private static int getNumBits(int paramInt)
/*      */     throws IOException
/*      */   {
/*      */     int i;
/*  103 */     switch (paramInt) {
/*      */     case 2:
/*  105 */       i = 1;
/*  106 */       break;
/*      */     case 4:
/*  108 */       i = 2;
/*  109 */       break;
/*      */     case 8:
/*  111 */       i = 3;
/*  112 */       break;
/*      */     case 16:
/*  114 */       i = 4;
/*  115 */       break;
/*      */     case 32:
/*  117 */       i = 5;
/*  118 */       break;
/*      */     case 64:
/*  120 */       i = 6;
/*  121 */       break;
/*      */     case 128:
/*  123 */       i = 7;
/*  124 */       break;
/*      */     case 256:
/*  126 */       i = 8;
/*  127 */       break;
/*      */     default:
/*  129 */       throw new IOException("Bad palette length: " + paramInt + "!");
/*      */     }
/*      */ 
/*  132 */     return i;
/*      */   }
/*      */ 
/*      */   private static void computeRegions(Rectangle paramRectangle, Dimension paramDimension, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  143 */     int i = 1;
/*  144 */     int j = 1;
/*  145 */     if (paramImageWriteParam != null) {
/*  146 */       int[] arrayOfInt = paramImageWriteParam.getSourceBands();
/*  147 */       if ((arrayOfInt != null) && ((arrayOfInt.length != 1) || (arrayOfInt[0] != 0)))
/*      */       {
/*  150 */         throw new IllegalArgumentException("Cannot sub-band image!");
/*      */       }
/*      */ 
/*  154 */       Rectangle localRectangle = paramImageWriteParam.getSourceRegion();
/*  155 */       if (localRectangle != null)
/*      */       {
/*  157 */         localRectangle = localRectangle.intersection(paramRectangle);
/*  158 */         paramRectangle.setBounds(localRectangle);
/*      */       }
/*      */ 
/*  162 */       int k = paramImageWriteParam.getSubsamplingXOffset();
/*  163 */       int m = paramImageWriteParam.getSubsamplingYOffset();
/*  164 */       paramRectangle.x += k;
/*  165 */       paramRectangle.y += m;
/*  166 */       paramRectangle.width -= k;
/*  167 */       paramRectangle.height -= m;
/*      */ 
/*  170 */       i = paramImageWriteParam.getSourceXSubsampling();
/*  171 */       j = paramImageWriteParam.getSourceYSubsampling();
/*      */     }
/*      */ 
/*  175 */     paramDimension.setSize((paramRectangle.width + i - 1) / i, (paramRectangle.height + j - 1) / j);
/*      */ 
/*  177 */     if ((paramDimension.width <= 0) || (paramDimension.height <= 0))
/*  178 */       throw new IllegalArgumentException("Empty source region!");
/*      */   }
/*      */ 
/*      */   private static byte[] createColorTable(ColorModel paramColorModel, SampleModel paramSampleModel)
/*      */   {
/*      */     int j;
/*      */     int k;
/*      */     byte[] arrayOfByte1;
/*  189 */     if ((paramColorModel instanceof IndexColorModel)) {
/*  190 */       IndexColorModel localIndexColorModel = (IndexColorModel)paramColorModel;
/*  191 */       j = localIndexColorModel.getMapSize();
/*      */ 
/*  198 */       k = getGifPaletteSize(j);
/*      */ 
/*  200 */       byte[] arrayOfByte2 = new byte[k];
/*  201 */       byte[] arrayOfByte3 = new byte[k];
/*  202 */       byte[] arrayOfByte4 = new byte[k];
/*  203 */       localIndexColorModel.getReds(arrayOfByte2);
/*  204 */       localIndexColorModel.getGreens(arrayOfByte3);
/*  205 */       localIndexColorModel.getBlues(arrayOfByte4);
/*      */ 
/*  211 */       for (int m = j; m < k; m++) {
/*  212 */         arrayOfByte2[m] = arrayOfByte2[0];
/*  213 */         arrayOfByte3[m] = arrayOfByte3[0];
/*  214 */         arrayOfByte4[m] = arrayOfByte4[0];
/*      */       }
/*      */ 
/*  217 */       arrayOfByte1 = new byte[3 * k];
/*  218 */       m = 0;
/*  219 */       for (int n = 0; n < k; n++) {
/*  220 */         arrayOfByte1[(m++)] = arrayOfByte2[n];
/*  221 */         arrayOfByte1[(m++)] = arrayOfByte3[n];
/*  222 */         arrayOfByte1[(m++)] = arrayOfByte4[n];
/*      */       }
/*  224 */     } else if (paramSampleModel.getNumBands() == 1)
/*      */     {
/*  226 */       int i = paramSampleModel.getSampleSize()[0];
/*  227 */       if (i > 8) {
/*  228 */         i = 8;
/*      */       }
/*  230 */       j = 3 * (1 << i);
/*  231 */       arrayOfByte1 = new byte[j];
/*  232 */       for (k = 0; k < j; k++) {
/*  233 */         arrayOfByte1[k] = ((byte)(k / 3));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  238 */       arrayOfByte1 = null;
/*      */     }
/*      */ 
/*  241 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   private static int getGifPaletteSize(int paramInt)
/*      */   {
/*  249 */     if (paramInt <= 2) {
/*  250 */       return 2;
/*      */     }
/*  252 */     paramInt -= 1;
/*  253 */     paramInt |= paramInt >> 1;
/*  254 */     paramInt |= paramInt >> 2;
/*  255 */     paramInt |= paramInt >> 4;
/*  256 */     paramInt |= paramInt >> 8;
/*  257 */     paramInt |= paramInt >> 16;
/*  258 */     return paramInt + 1;
/*      */   }
/*      */ 
/*      */   public GIFImageWriter(GIFImageWriterSpi paramGIFImageWriterSpi)
/*      */   {
/*  264 */     super(paramGIFImageWriterSpi);
/*      */   }
/*      */ 
/*      */   public boolean canWriteSequence()
/*      */   {
/*  271 */     return true;
/*      */   }
/*      */ 
/*      */   private void convertMetadata(String paramString, IIOMetadata paramIIOMetadata1, IIOMetadata paramIIOMetadata2)
/*      */   {
/*  282 */     String str1 = null;
/*      */ 
/*  284 */     String str2 = paramIIOMetadata1.getNativeMetadataFormatName();
/*      */     Object localObject;
/*  285 */     if ((str2 != null) && (str2.equals(paramString)))
/*      */     {
/*  287 */       str1 = paramString;
/*      */     } else {
/*  289 */       localObject = paramIIOMetadata1.getExtraMetadataFormatNames();
/*      */ 
/*  291 */       if (localObject != null) {
/*  292 */         for (int i = 0; i < localObject.length; i++) {
/*  293 */           if (localObject[i].equals(paramString)) {
/*  294 */             str1 = paramString;
/*  295 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  301 */     if ((str1 == null) && (paramIIOMetadata1.isStandardMetadataFormatSupported()))
/*      */     {
/*  303 */       str1 = "javax_imageio_1.0";
/*      */     }
/*      */ 
/*  306 */     if (str1 != null)
/*      */       try {
/*  308 */         localObject = paramIIOMetadata1.getAsTree(str1);
/*  309 */         paramIIOMetadata2.mergeTree(str1, (Node)localObject);
/*      */       }
/*      */       catch (IIOInvalidTreeException localIIOInvalidTreeException)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   public IIOMetadata convertStreamMetadata(IIOMetadata paramIIOMetadata, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  322 */     if (paramIIOMetadata == null) {
/*  323 */       throw new IllegalArgumentException("inData == null!");
/*      */     }
/*      */ 
/*  326 */     IIOMetadata localIIOMetadata = getDefaultStreamMetadata(paramImageWriteParam);
/*      */ 
/*  328 */     convertMetadata("javax_imageio_gif_stream_1.0", paramIIOMetadata, localIIOMetadata);
/*      */ 
/*  330 */     return localIIOMetadata;
/*      */   }
/*      */ 
/*      */   public IIOMetadata convertImageMetadata(IIOMetadata paramIIOMetadata, ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  340 */     if (paramIIOMetadata == null) {
/*  341 */       throw new IllegalArgumentException("inData == null!");
/*      */     }
/*  343 */     if (paramImageTypeSpecifier == null) {
/*  344 */       throw new IllegalArgumentException("imageType == null!");
/*      */     }
/*      */ 
/*  347 */     GIFWritableImageMetadata localGIFWritableImageMetadata = (GIFWritableImageMetadata)getDefaultImageMetadata(paramImageTypeSpecifier, paramImageWriteParam);
/*      */ 
/*  353 */     boolean bool = localGIFWritableImageMetadata.interlaceFlag;
/*      */ 
/*  355 */     convertMetadata("javax_imageio_gif_image_1.0", paramIIOMetadata, localGIFWritableImageMetadata);
/*      */ 
/*  359 */     if ((paramImageWriteParam != null) && (paramImageWriteParam.canWriteProgressive()) && (paramImageWriteParam.getProgressiveMode() != 3))
/*      */     {
/*  361 */       localGIFWritableImageMetadata.interlaceFlag = bool;
/*      */     }
/*      */ 
/*  364 */     return localGIFWritableImageMetadata;
/*      */   }
/*      */ 
/*      */   public void endWriteSequence() throws IOException {
/*  368 */     if (this.stream == null) {
/*  369 */       throw new IllegalStateException("output == null!");
/*      */     }
/*  371 */     if (!this.isWritingSequence) {
/*  372 */       throw new IllegalStateException("prepareWriteSequence() was not invoked!");
/*      */     }
/*  374 */     writeTrailer();
/*  375 */     resetLocal();
/*      */   }
/*      */ 
/*      */   public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam)
/*      */   {
/*  380 */     GIFWritableImageMetadata localGIFWritableImageMetadata = new GIFWritableImageMetadata();
/*      */ 
/*  385 */     SampleModel localSampleModel = paramImageTypeSpecifier.getSampleModel();
/*      */ 
/*  387 */     Rectangle localRectangle = new Rectangle(localSampleModel.getWidth(), localSampleModel.getHeight());
/*      */ 
/*  389 */     Dimension localDimension = new Dimension();
/*  390 */     computeRegions(localRectangle, localDimension, paramImageWriteParam);
/*      */ 
/*  392 */     localGIFWritableImageMetadata.imageWidth = localDimension.width;
/*  393 */     localGIFWritableImageMetadata.imageHeight = localDimension.height;
/*      */ 
/*  397 */     if ((paramImageWriteParam != null) && (paramImageWriteParam.canWriteProgressive()) && (paramImageWriteParam.getProgressiveMode() == 0))
/*      */     {
/*  399 */       localGIFWritableImageMetadata.interlaceFlag = false;
/*      */     }
/*  401 */     else localGIFWritableImageMetadata.interlaceFlag = true;
/*      */ 
/*  406 */     ColorModel localColorModel = paramImageTypeSpecifier.getColorModel();
/*      */ 
/*  408 */     localGIFWritableImageMetadata.localColorTable = createColorTable(localColorModel, localSampleModel);
/*      */ 
/*  413 */     if ((localColorModel instanceof IndexColorModel)) {
/*  414 */       int i = ((IndexColorModel)localColorModel).getTransparentPixel();
/*      */ 
/*  416 */       if (i != -1) {
/*  417 */         localGIFWritableImageMetadata.transparentColorFlag = true;
/*  418 */         localGIFWritableImageMetadata.transparentColorIndex = i;
/*      */       }
/*      */     }
/*      */ 
/*  422 */     return localGIFWritableImageMetadata;
/*      */   }
/*      */ 
/*      */   public IIOMetadata getDefaultStreamMetadata(ImageWriteParam paramImageWriteParam) {
/*  426 */     GIFWritableStreamMetadata localGIFWritableStreamMetadata = new GIFWritableStreamMetadata();
/*      */ 
/*  428 */     localGIFWritableStreamMetadata.version = "89a";
/*  429 */     return localGIFWritableStreamMetadata;
/*      */   }
/*      */ 
/*      */   public ImageWriteParam getDefaultWriteParam() {
/*  433 */     return new GIFImageWriteParam(getLocale());
/*      */   }
/*      */ 
/*      */   public void prepareWriteSequence(IIOMetadata paramIIOMetadata)
/*      */     throws IOException
/*      */   {
/*  439 */     if (this.stream == null) {
/*  440 */       throw new IllegalStateException("Output is not set.");
/*      */     }
/*      */ 
/*  443 */     resetLocal();
/*      */ 
/*  446 */     if (paramIIOMetadata == null) {
/*  447 */       this.theStreamMetadata = ((GIFWritableStreamMetadata)getDefaultStreamMetadata(null));
/*      */     }
/*      */     else {
/*  450 */       this.theStreamMetadata = new GIFWritableStreamMetadata();
/*  451 */       convertMetadata("javax_imageio_gif_stream_1.0", paramIIOMetadata, this.theStreamMetadata);
/*      */     }
/*      */ 
/*  455 */     this.isWritingSequence = true;
/*      */   }
/*      */ 
/*      */   public void reset() {
/*  459 */     super.reset();
/*  460 */     resetLocal();
/*      */   }
/*      */ 
/*      */   private void resetLocal()
/*      */   {
/*  467 */     this.isWritingSequence = false;
/*  468 */     this.wroteSequenceHeader = false;
/*  469 */     this.theStreamMetadata = null;
/*  470 */     this.imageIndex = 0;
/*      */   }
/*      */ 
/*      */   public void setOutput(Object paramObject) {
/*  474 */     super.setOutput(paramObject);
/*  475 */     if (paramObject != null) {
/*  476 */       if (!(paramObject instanceof ImageOutputStream)) {
/*  477 */         throw new IllegalArgumentException("output is not an ImageOutputStream");
/*      */       }
/*      */ 
/*  480 */       this.stream = ((ImageOutputStream)paramObject);
/*  481 */       this.stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
/*      */     } else {
/*  483 */       this.stream = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write(IIOMetadata paramIIOMetadata, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/*  490 */     if (this.stream == null) {
/*  491 */       throw new IllegalStateException("output == null!");
/*      */     }
/*  493 */     if (paramIIOImage == null) {
/*  494 */       throw new IllegalArgumentException("iioimage == null!");
/*      */     }
/*  496 */     if (paramIIOImage.hasRaster()) {
/*  497 */       throw new UnsupportedOperationException("canWriteRasters() == false!");
/*      */     }
/*      */ 
/*  500 */     resetLocal();
/*      */     GIFWritableStreamMetadata localGIFWritableStreamMetadata;
/*  503 */     if (paramIIOMetadata == null) {
/*  504 */       localGIFWritableStreamMetadata = (GIFWritableStreamMetadata)getDefaultStreamMetadata(paramImageWriteParam);
/*      */     }
/*      */     else {
/*  507 */       localGIFWritableStreamMetadata = (GIFWritableStreamMetadata)convertStreamMetadata(paramIIOMetadata, paramImageWriteParam);
/*      */     }
/*      */ 
/*  511 */     write(true, true, localGIFWritableStreamMetadata, paramIIOImage, paramImageWriteParam);
/*      */   }
/*      */ 
/*      */   public void writeToSequence(IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam) throws IOException
/*      */   {
/*  516 */     if (this.stream == null) {
/*  517 */       throw new IllegalStateException("output == null!");
/*      */     }
/*  519 */     if (paramIIOImage == null) {
/*  520 */       throw new IllegalArgumentException("image == null!");
/*      */     }
/*  522 */     if (paramIIOImage.hasRaster()) {
/*  523 */       throw new UnsupportedOperationException("canWriteRasters() == false!");
/*      */     }
/*  525 */     if (!this.isWritingSequence) {
/*  526 */       throw new IllegalStateException("prepareWriteSequence() was not invoked!");
/*      */     }
/*      */ 
/*  529 */     write(!this.wroteSequenceHeader, false, this.theStreamMetadata, paramIIOImage, paramImageWriteParam);
/*      */ 
/*  532 */     if (!this.wroteSequenceHeader) {
/*  533 */       this.wroteSequenceHeader = true;
/*      */     }
/*      */ 
/*  536 */     this.imageIndex += 1;
/*      */   }
/*      */ 
/*      */   private boolean needToCreateIndex(RenderedImage paramRenderedImage)
/*      */   {
/*  542 */     SampleModel localSampleModel = paramRenderedImage.getSampleModel();
/*  543 */     ColorModel localColorModel = paramRenderedImage.getColorModel();
/*      */ 
/*  545 */     return (localSampleModel.getNumBands() != 1) || (localSampleModel.getSampleSize()[0] > 8) || (localColorModel.getComponentSize()[0] > 8);
/*      */   }
/*      */ 
/*      */   private void write(boolean paramBoolean1, boolean paramBoolean2, IIOMetadata paramIIOMetadata, IIOImage paramIIOImage, ImageWriteParam paramImageWriteParam)
/*      */     throws IOException
/*      */   {
/*  577 */     clearAbortRequest();
/*      */ 
/*  579 */     RenderedImage localRenderedImage = paramIIOImage.getRenderedImage();
/*      */ 
/*  582 */     if (needToCreateIndex(localRenderedImage)) {
/*  583 */       localRenderedImage = PaletteBuilder.createIndexedImage(localRenderedImage);
/*  584 */       paramIIOImage.setRenderedImage(localRenderedImage);
/*      */     }
/*      */ 
/*  587 */     ColorModel localColorModel = localRenderedImage.getColorModel();
/*  588 */     SampleModel localSampleModel = localRenderedImage.getSampleModel();
/*      */ 
/*  591 */     Rectangle localRectangle = new Rectangle(localRenderedImage.getMinX(), localRenderedImage.getMinY(), localRenderedImage.getWidth(), localRenderedImage.getHeight());
/*      */ 
/*  595 */     Dimension localDimension = new Dimension();
/*  596 */     computeRegions(localRectangle, localDimension, paramImageWriteParam);
/*      */ 
/*  599 */     GIFWritableImageMetadata localGIFWritableImageMetadata = null;
/*  600 */     if (paramIIOImage.getMetadata() != null) {
/*  601 */       localGIFWritableImageMetadata = new GIFWritableImageMetadata();
/*  602 */       convertMetadata("javax_imageio_gif_image_1.0", paramIIOImage.getMetadata(), localGIFWritableImageMetadata);
/*      */ 
/*  611 */       if (localGIFWritableImageMetadata.localColorTable == null) {
/*  612 */         localGIFWritableImageMetadata.localColorTable = createColorTable(localColorModel, localSampleModel);
/*      */ 
/*  617 */         if ((localColorModel instanceof IndexColorModel)) {
/*  618 */           localObject = (IndexColorModel)localColorModel;
/*      */ 
/*  620 */           int i = ((IndexColorModel)localObject).getTransparentPixel();
/*  621 */           localGIFWritableImageMetadata.transparentColorFlag = (i != -1);
/*  622 */           if (localGIFWritableImageMetadata.transparentColorFlag) {
/*  623 */             localGIFWritableImageMetadata.transparentColorIndex = i;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  636 */     Object localObject = null;
/*      */ 
/*  640 */     if (paramBoolean1) {
/*  641 */       if (paramIIOMetadata == null) {
/*  642 */         throw new IllegalArgumentException("Cannot write null header!");
/*      */       }
/*      */ 
/*  645 */       GIFWritableStreamMetadata localGIFWritableStreamMetadata = (GIFWritableStreamMetadata)paramIIOMetadata;
/*      */ 
/*  649 */       if (localGIFWritableStreamMetadata.version == null) {
/*  650 */         localGIFWritableStreamMetadata.version = "89a";
/*      */       }
/*      */ 
/*  654 */       if (localGIFWritableStreamMetadata.logicalScreenWidth == -1)
/*      */       {
/*  657 */         localGIFWritableStreamMetadata.logicalScreenWidth = localDimension.width;
/*      */       }
/*      */ 
/*  660 */       if (localGIFWritableStreamMetadata.logicalScreenHeight == -1)
/*      */       {
/*  663 */         localGIFWritableStreamMetadata.logicalScreenHeight = localDimension.height;
/*      */       }
/*      */ 
/*  666 */       if (localGIFWritableStreamMetadata.colorResolution == -1)
/*      */       {
/*  669 */         localGIFWritableStreamMetadata.colorResolution = (localColorModel != null ? localColorModel.getComponentSize()[0] : localSampleModel.getSampleSize()[0]);
/*      */       }
/*      */ 
/*  676 */       if (localGIFWritableStreamMetadata.globalColorTable == null) {
/*  677 */         if ((this.isWritingSequence) && (localGIFWritableImageMetadata != null) && (localGIFWritableImageMetadata.localColorTable != null))
/*      */         {
/*  681 */           localGIFWritableStreamMetadata.globalColorTable = localGIFWritableImageMetadata.localColorTable;
/*      */         }
/*  683 */         else if ((localGIFWritableImageMetadata == null) || (localGIFWritableImageMetadata.localColorTable == null))
/*      */         {
/*  686 */           localGIFWritableStreamMetadata.globalColorTable = createColorTable(localColorModel, localSampleModel);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  700 */       localObject = localGIFWritableStreamMetadata.globalColorTable;
/*      */       int j;
/*  704 */       if (localObject != null)
/*  705 */         j = getNumBits(localObject.length / 3);
/*  706 */       else if ((localGIFWritableImageMetadata != null) && (localGIFWritableImageMetadata.localColorTable != null))
/*      */       {
/*  708 */         j = getNumBits(localGIFWritableImageMetadata.localColorTable.length / 3);
/*      */       }
/*      */       else {
/*  711 */         j = localSampleModel.getSampleSize(0);
/*      */       }
/*  713 */       writeHeader(localGIFWritableStreamMetadata, j);
/*  714 */     } else if (this.isWritingSequence) {
/*  715 */       localObject = this.theStreamMetadata.globalColorTable;
/*      */     } else {
/*  717 */       throw new IllegalArgumentException("Must write header for single image!");
/*      */     }
/*      */ 
/*  721 */     writeImage(paramIIOImage.getRenderedImage(), localGIFWritableImageMetadata, paramImageWriteParam, (byte[])localObject, localRectangle, localDimension);
/*      */ 
/*  725 */     if (paramBoolean2)
/*  726 */       writeTrailer();
/*      */   }
/*      */ 
/*      */   private void writeImage(RenderedImage paramRenderedImage, GIFWritableImageMetadata paramGIFWritableImageMetadata, ImageWriteParam paramImageWriteParam, byte[] paramArrayOfByte, Rectangle paramRectangle, Dimension paramDimension)
/*      */     throws IOException
/*      */   {
/*  744 */     ColorModel localColorModel = paramRenderedImage.getColorModel();
/*  745 */     SampleModel localSampleModel = paramRenderedImage.getSampleModel();
/*      */     boolean bool;
/*  748 */     if (paramGIFWritableImageMetadata == null)
/*      */     {
/*  750 */       paramGIFWritableImageMetadata = (GIFWritableImageMetadata)getDefaultImageMetadata(new ImageTypeSpecifier(paramRenderedImage), paramImageWriteParam);
/*      */ 
/*  755 */       bool = paramGIFWritableImageMetadata.transparentColorFlag;
/*      */     }
/*      */     else {
/*  758 */       NodeList localNodeList = null;
/*      */       try {
/*  760 */         IIOMetadataNode localIIOMetadataNode = (IIOMetadataNode)paramGIFWritableImageMetadata.getAsTree("javax_imageio_gif_image_1.0");
/*      */ 
/*  762 */         localNodeList = localIIOMetadataNode.getElementsByTagName("GraphicControlExtension");
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException)
/*      */       {
/*      */       }
/*      */ 
/*  768 */       bool = (localNodeList != null) && (localNodeList.getLength() > 0);
/*      */ 
/*  773 */       if ((paramImageWriteParam != null) && (paramImageWriteParam.canWriteProgressive())) {
/*  774 */         if (paramImageWriteParam.getProgressiveMode() == 0)
/*      */         {
/*  776 */           paramGIFWritableImageMetadata.interlaceFlag = false;
/*  777 */         } else if (paramImageWriteParam.getProgressiveMode() == 1)
/*      */         {
/*  779 */           paramGIFWritableImageMetadata.interlaceFlag = true;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  785 */     if (Arrays.equals(paramArrayOfByte, paramGIFWritableImageMetadata.localColorTable)) {
/*  786 */       paramGIFWritableImageMetadata.localColorTable = null;
/*      */     }
/*      */ 
/*  790 */     paramGIFWritableImageMetadata.imageWidth = paramDimension.width;
/*  791 */     paramGIFWritableImageMetadata.imageHeight = paramDimension.height;
/*      */ 
/*  794 */     if (bool) {
/*  795 */       writeGraphicControlExtension(paramGIFWritableImageMetadata);
/*      */     }
/*      */ 
/*  799 */     writePlainTextExtension(paramGIFWritableImageMetadata);
/*  800 */     writeApplicationExtension(paramGIFWritableImageMetadata);
/*  801 */     writeCommentExtension(paramGIFWritableImageMetadata);
/*      */ 
/*  804 */     int i = getNumBits(paramGIFWritableImageMetadata.localColorTable == null ? paramArrayOfByte.length / 3 : paramArrayOfByte == null ? localSampleModel.getSampleSize(0) : paramGIFWritableImageMetadata.localColorTable.length / 3);
/*      */ 
/*  810 */     writeImageDescriptor(paramGIFWritableImageMetadata, i);
/*      */ 
/*  813 */     writeRasterData(paramRenderedImage, paramRectangle, paramDimension, paramImageWriteParam, paramGIFWritableImageMetadata.interlaceFlag);
/*      */   }
/*      */ 
/*      */   private void writeRows(RenderedImage paramRenderedImage, LZWCompressor paramLZWCompressor, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11)
/*      */     throws IOException
/*      */   {
/*  824 */     int[] arrayOfInt = new int[paramInt5];
/*  825 */     byte[] arrayOfByte = new byte[paramInt8];
/*      */ 
/*  827 */     Raster localRaster = (paramRenderedImage.getNumXTiles() == 1) && (paramRenderedImage.getNumYTiles() == 1) ? paramRenderedImage.getTile(0, 0) : paramRenderedImage.getData();
/*      */ 
/*  830 */     for (int i = paramInt6; i < paramInt9; i += paramInt7) {
/*  831 */       if (paramInt10 % paramInt11 == 0) {
/*  832 */         if (abortRequested()) {
/*  833 */           processWriteAborted();
/*  834 */           return;
/*      */         }
/*  836 */         processImageProgress(paramInt10 * 100.0F / paramInt9);
/*      */       }
/*      */ 
/*  839 */       localRaster.getSamples(paramInt1, paramInt3, paramInt5, 1, 0, arrayOfInt);
/*  840 */       int j = 0; for (int k = 0; j < paramInt8; k += paramInt2) {
/*  841 */         arrayOfByte[j] = ((byte)arrayOfInt[k]);
/*      */ 
/*  840 */         j++;
/*      */       }
/*      */ 
/*  843 */       paramLZWCompressor.compress(arrayOfByte, 0, paramInt8);
/*  844 */       paramInt10++;
/*  845 */       paramInt3 += paramInt4;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeRowsOpt(byte[] paramArrayOfByte, int paramInt1, int paramInt2, LZWCompressor paramLZWCompressor, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
/*      */     throws IOException
/*      */   {
/*  856 */     paramInt1 += paramInt3 * paramInt2;
/*  857 */     paramInt2 *= paramInt4;
/*  858 */     for (int i = paramInt3; i < paramInt6; i += paramInt4) {
/*  859 */       if (paramInt7 % paramInt8 == 0) {
/*  860 */         if (abortRequested()) {
/*  861 */           processWriteAborted();
/*  862 */           return;
/*      */         }
/*  864 */         processImageProgress(paramInt7 * 100.0F / paramInt6);
/*      */       }
/*      */ 
/*  867 */       paramLZWCompressor.compress(paramArrayOfByte, paramInt1, paramInt5);
/*  868 */       paramInt7++;
/*  869 */       paramInt1 += paramInt2;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeRasterData(RenderedImage paramRenderedImage, Rectangle paramRectangle, Dimension paramDimension, ImageWriteParam paramImageWriteParam, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  879 */     int i = paramRectangle.x;
/*  880 */     int j = paramRectangle.y;
/*  881 */     int k = paramRectangle.width;
/*  882 */     int m = paramRectangle.height;
/*      */ 
/*  884 */     int n = paramDimension.width;
/*  885 */     int i1 = paramDimension.height;
/*      */     int i2;
/*      */     int i3;
/*  889 */     if (paramImageWriteParam == null) {
/*  890 */       i2 = 1;
/*  891 */       i3 = 1;
/*      */     } else {
/*  893 */       i2 = paramImageWriteParam.getSourceXSubsampling();
/*  894 */       i3 = paramImageWriteParam.getSourceYSubsampling();
/*      */     }
/*      */ 
/*  897 */     SampleModel localSampleModel = paramRenderedImage.getSampleModel();
/*  898 */     int i4 = localSampleModel.getSampleSize()[0];
/*      */ 
/*  900 */     int i5 = i4;
/*  901 */     if (i5 == 1) {
/*  902 */       i5++;
/*      */     }
/*  904 */     this.stream.write(i5);
/*      */ 
/*  906 */     LZWCompressor localLZWCompressor = new LZWCompressor(this.stream, i5, false);
/*      */ 
/*  916 */     int i6 = (i2 == 1) && (i3 == 1) && (paramRenderedImage.getNumXTiles() == 1) && (paramRenderedImage.getNumYTiles() == 1) && ((localSampleModel instanceof ComponentSampleModel)) && ((paramRenderedImage.getTile(0, 0) instanceof ByteComponentRaster)) && ((paramRenderedImage.getTile(0, 0).getDataBuffer() instanceof DataBufferByte)) ? 1 : 0;
/*      */ 
/*  923 */     int i7 = 0;
/*      */ 
/*  925 */     int i8 = Math.max(i1 / 20, 1);
/*      */ 
/*  927 */     processImageStarted(this.imageIndex);
/*      */     Object localObject;
/*      */     byte[] arrayOfByte;
/*      */     ComponentSampleModel localComponentSampleModel;
/*      */     int i9;
/*      */     int i10;
/*  929 */     if (paramBoolean)
/*      */     {
/*  932 */       if (i6 != 0) {
/*  933 */         localObject = (ByteComponentRaster)paramRenderedImage.getTile(0, 0);
/*      */ 
/*  935 */         arrayOfByte = ((DataBufferByte)((ByteComponentRaster)localObject).getDataBuffer()).getData();
/*  936 */         localComponentSampleModel = (ComponentSampleModel)((ByteComponentRaster)localObject).getSampleModel();
/*      */ 
/*  938 */         i9 = localComponentSampleModel.getOffset(i, j, 0);
/*      */ 
/*  940 */         i9 += ((ByteComponentRaster)localObject).getDataOffset(0);
/*  941 */         i10 = localComponentSampleModel.getScanlineStride();
/*      */ 
/*  943 */         writeRowsOpt(arrayOfByte, i9, i10, localLZWCompressor, 0, 8, n, i1, i7, i8);
/*      */ 
/*  947 */         if (abortRequested()) {
/*  948 */           return;
/*      */         }
/*      */ 
/*  951 */         i7 += i1 / 8;
/*      */ 
/*  953 */         writeRowsOpt(arrayOfByte, i9, i10, localLZWCompressor, 4, 8, n, i1, i7, i8);
/*      */ 
/*  957 */         if (abortRequested()) {
/*  958 */           return;
/*      */         }
/*      */ 
/*  961 */         i7 += (i1 - 4) / 8;
/*      */ 
/*  963 */         writeRowsOpt(arrayOfByte, i9, i10, localLZWCompressor, 2, 4, n, i1, i7, i8);
/*      */ 
/*  967 */         if (abortRequested()) {
/*  968 */           return;
/*      */         }
/*      */ 
/*  971 */         i7 += (i1 - 2) / 4;
/*      */ 
/*  973 */         writeRowsOpt(arrayOfByte, i9, i10, localLZWCompressor, 1, 2, n, i1, i7, i8);
/*      */       }
/*      */       else
/*      */       {
/*  977 */         writeRows(paramRenderedImage, localLZWCompressor, i, i2, j, 8 * i3, k, 0, 8, n, i1, i7, i8);
/*      */ 
/*  984 */         if (abortRequested()) {
/*  985 */           return;
/*      */         }
/*      */ 
/*  988 */         i7 += i1 / 8;
/*      */ 
/*  990 */         writeRows(paramRenderedImage, localLZWCompressor, i, i2, j + 4 * i3, 8 * i3, k, 4, 8, n, i1, i7, i8);
/*      */ 
/*  996 */         if (abortRequested()) {
/*  997 */           return;
/*      */         }
/*      */ 
/* 1000 */         i7 += (i1 - 4) / 8;
/*      */ 
/* 1002 */         writeRows(paramRenderedImage, localLZWCompressor, i, i2, j + 2 * i3, 4 * i3, k, 2, 4, n, i1, i7, i8);
/*      */ 
/* 1008 */         if (abortRequested()) {
/* 1009 */           return;
/*      */         }
/*      */ 
/* 1012 */         i7 += (i1 - 2) / 4;
/*      */ 
/* 1014 */         writeRows(paramRenderedImage, localLZWCompressor, i, i2, j + i3, 2 * i3, k, 1, 2, n, i1, i7, i8);
/*      */       }
/*      */ 
/*      */     }
/* 1023 */     else if (i6 != 0) {
/* 1024 */       localObject = paramRenderedImage.getTile(0, 0);
/* 1025 */       arrayOfByte = ((DataBufferByte)((Raster)localObject).getDataBuffer()).getData();
/* 1026 */       localComponentSampleModel = (ComponentSampleModel)((Raster)localObject).getSampleModel();
/*      */ 
/* 1028 */       i9 = localComponentSampleModel.getOffset(i, j, 0);
/* 1029 */       i10 = localComponentSampleModel.getScanlineStride();
/*      */ 
/* 1031 */       writeRowsOpt(arrayOfByte, i9, i10, localLZWCompressor, 0, 1, n, i1, i7, i8);
/*      */     }
/*      */     else
/*      */     {
/* 1035 */       writeRows(paramRenderedImage, localLZWCompressor, i, i2, j, i3, k, 0, 1, n, i1, i7, i8);
/*      */     }
/*      */ 
/* 1044 */     if (abortRequested()) {
/* 1045 */       return;
/*      */     }
/*      */ 
/* 1048 */     processImageProgress(100.0F);
/*      */ 
/* 1050 */     localLZWCompressor.flush();
/*      */ 
/* 1052 */     this.stream.write(0);
/*      */ 
/* 1054 */     processImageComplete();
/*      */   }
/*      */ 
/*      */   private void writeHeader(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, int paramInt6, byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1068 */       this.stream.writeBytes("GIF" + paramString);
/*      */ 
/* 1072 */       this.stream.writeShort((short)paramInt1);
/*      */ 
/* 1075 */       this.stream.writeShort((short)paramInt2);
/*      */ 
/* 1079 */       int i = paramArrayOfByte != null ? 128 : 0;
/* 1080 */       i |= (paramInt3 - 1 & 0x7) << 4;
/* 1081 */       if (paramBoolean) {
/* 1082 */         i |= 8;
/*      */       }
/* 1084 */       i |= paramInt6 - 1;
/* 1085 */       this.stream.write(i);
/*      */ 
/* 1088 */       this.stream.write(paramInt5);
/*      */ 
/* 1091 */       this.stream.write(paramInt4);
/*      */ 
/* 1094 */       if (paramArrayOfByte != null)
/* 1095 */         this.stream.write(paramArrayOfByte);
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1098 */       throw new IIOException("I/O error writing header!", localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeHeader(IIOMetadata paramIIOMetadata, int paramInt)
/*      */     throws IOException
/*      */   {
/*      */     GIFWritableStreamMetadata localGIFWritableStreamMetadata;
/* 1106 */     if ((paramIIOMetadata instanceof GIFWritableStreamMetadata)) {
/* 1107 */       localGIFWritableStreamMetadata = (GIFWritableStreamMetadata)paramIIOMetadata;
/*      */     } else {
/* 1109 */       localGIFWritableStreamMetadata = new GIFWritableStreamMetadata();
/* 1110 */       Node localNode = paramIIOMetadata.getAsTree("javax_imageio_gif_stream_1.0");
/*      */ 
/* 1112 */       localGIFWritableStreamMetadata.setFromTree("javax_imageio_gif_stream_1.0", localNode);
/*      */     }
/*      */ 
/* 1115 */     writeHeader(localGIFWritableStreamMetadata.version, localGIFWritableStreamMetadata.logicalScreenWidth, localGIFWritableStreamMetadata.logicalScreenHeight, localGIFWritableStreamMetadata.colorResolution, localGIFWritableStreamMetadata.pixelAspectRatio, localGIFWritableStreamMetadata.backgroundColorIndex, localGIFWritableStreamMetadata.sortFlag, paramInt, localGIFWritableStreamMetadata.globalColorTable);
/*      */   }
/*      */ 
/*      */   private void writeGraphicControlExtension(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1133 */       this.stream.write(33);
/* 1134 */       this.stream.write(249);
/*      */ 
/* 1136 */       this.stream.write(4);
/*      */ 
/* 1138 */       int i = (paramInt1 & 0x3) << 2;
/* 1139 */       if (paramBoolean1) {
/* 1140 */         i |= 2;
/*      */       }
/* 1142 */       if (paramBoolean2) {
/* 1143 */         i |= 1;
/*      */       }
/* 1145 */       this.stream.write(i);
/*      */ 
/* 1147 */       this.stream.writeShort((short)paramInt2);
/*      */ 
/* 1149 */       this.stream.write(paramInt3);
/* 1150 */       this.stream.write(0);
/*      */     } catch (IOException localIOException) {
/* 1152 */       throw new IIOException("I/O error writing Graphic Control Extension!", localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeGraphicControlExtension(GIFWritableImageMetadata paramGIFWritableImageMetadata) throws IOException
/*      */   {
/* 1158 */     writeGraphicControlExtension(paramGIFWritableImageMetadata.disposalMethod, paramGIFWritableImageMetadata.userInputFlag, paramGIFWritableImageMetadata.transparentColorFlag, paramGIFWritableImageMetadata.delayTime, paramGIFWritableImageMetadata.transparentColorIndex);
/*      */   }
/*      */ 
/*      */   private void writeBlocks(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1166 */     if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
/* 1167 */       int i = 0;
/* 1168 */       while (i < paramArrayOfByte.length) {
/* 1169 */         int j = Math.min(paramArrayOfByte.length - i, 255);
/* 1170 */         this.stream.write(j);
/* 1171 */         this.stream.write(paramArrayOfByte, i, j);
/* 1172 */         i += j;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writePlainTextExtension(GIFWritableImageMetadata paramGIFWritableImageMetadata) throws IOException
/*      */   {
/* 1179 */     if (paramGIFWritableImageMetadata.hasPlainTextExtension)
/*      */       try {
/* 1181 */         this.stream.write(33);
/* 1182 */         this.stream.write(1);
/*      */ 
/* 1184 */         this.stream.write(12);
/*      */ 
/* 1186 */         this.stream.writeShort(paramGIFWritableImageMetadata.textGridLeft);
/* 1187 */         this.stream.writeShort(paramGIFWritableImageMetadata.textGridTop);
/* 1188 */         this.stream.writeShort(paramGIFWritableImageMetadata.textGridWidth);
/* 1189 */         this.stream.writeShort(paramGIFWritableImageMetadata.textGridHeight);
/* 1190 */         this.stream.write(paramGIFWritableImageMetadata.characterCellWidth);
/* 1191 */         this.stream.write(paramGIFWritableImageMetadata.characterCellHeight);
/* 1192 */         this.stream.write(paramGIFWritableImageMetadata.textForegroundColor);
/* 1193 */         this.stream.write(paramGIFWritableImageMetadata.textBackgroundColor);
/*      */ 
/* 1195 */         writeBlocks(paramGIFWritableImageMetadata.text);
/*      */ 
/* 1197 */         this.stream.write(0);
/*      */       } catch (IOException localIOException) {
/* 1199 */         throw new IIOException("I/O error writing Plain Text Extension!", localIOException);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void writeApplicationExtension(GIFWritableImageMetadata paramGIFWritableImageMetadata)
/*      */     throws IOException
/*      */   {
/* 1206 */     if (paramGIFWritableImageMetadata.applicationIDs != null) {
/* 1207 */       Iterator localIterator1 = paramGIFWritableImageMetadata.applicationIDs.iterator();
/* 1208 */       Iterator localIterator2 = paramGIFWritableImageMetadata.authenticationCodes.iterator();
/* 1209 */       Iterator localIterator3 = paramGIFWritableImageMetadata.applicationData.iterator();
/*      */ 
/* 1211 */       while (localIterator1.hasNext())
/*      */         try {
/* 1213 */           this.stream.write(33);
/* 1214 */           this.stream.write(255);
/*      */ 
/* 1216 */           this.stream.write(11);
/* 1217 */           this.stream.write((byte[])localIterator1.next(), 0, 8);
/* 1218 */           this.stream.write((byte[])localIterator2.next(), 0, 3);
/*      */ 
/* 1220 */           writeBlocks((byte[])localIterator3.next());
/*      */ 
/* 1222 */           this.stream.write(0);
/*      */         } catch (IOException localIOException) {
/* 1224 */           throw new IIOException("I/O error writing Application Extension!", localIOException);
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeCommentExtension(GIFWritableImageMetadata paramGIFWritableImageMetadata)
/*      */     throws IOException
/*      */   {
/* 1232 */     if (paramGIFWritableImageMetadata.comments != null)
/*      */       try {
/* 1234 */         Iterator localIterator = paramGIFWritableImageMetadata.comments.iterator();
/* 1235 */         while (localIterator.hasNext()) {
/* 1236 */           this.stream.write(33);
/* 1237 */           this.stream.write(254);
/* 1238 */           writeBlocks((byte[])localIterator.next());
/* 1239 */           this.stream.write(0);
/*      */         }
/*      */       } catch (IOException localIOException) {
/* 1242 */         throw new IIOException("I/O error writing Comment Extension!", localIOException);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void writeImageDescriptor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, int paramInt5, byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/* 1258 */       this.stream.write(44);
/*      */ 
/* 1260 */       this.stream.writeShort((short)paramInt1);
/* 1261 */       this.stream.writeShort((short)paramInt2);
/* 1262 */       this.stream.writeShort((short)paramInt3);
/* 1263 */       this.stream.writeShort((short)paramInt4);
/*      */ 
/* 1265 */       int i = paramArrayOfByte != null ? 128 : 0;
/* 1266 */       if (paramBoolean1) {
/* 1267 */         i |= 64;
/*      */       }
/* 1269 */       if (paramBoolean2) {
/* 1270 */         i |= 8;
/*      */       }
/* 1272 */       i |= paramInt5 - 1;
/* 1273 */       this.stream.write(i);
/*      */ 
/* 1275 */       if (paramArrayOfByte != null)
/* 1276 */         this.stream.write(paramArrayOfByte);
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1279 */       throw new IIOException("I/O error writing Image Descriptor!", localIOException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeImageDescriptor(GIFWritableImageMetadata paramGIFWritableImageMetadata, int paramInt)
/*      */     throws IOException
/*      */   {
/* 1287 */     writeImageDescriptor(paramGIFWritableImageMetadata.imageLeftPosition, paramGIFWritableImageMetadata.imageTopPosition, paramGIFWritableImageMetadata.imageWidth, paramGIFWritableImageMetadata.imageHeight, paramGIFWritableImageMetadata.interlaceFlag, paramGIFWritableImageMetadata.sortFlag, paramInt, paramGIFWritableImageMetadata.localColorTable);
/*      */   }
/*      */ 
/*      */   private void writeTrailer()
/*      */     throws IOException
/*      */   {
/* 1298 */     this.stream.write(59);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFImageWriter
 * JD-Core Version:    0.6.2
 */
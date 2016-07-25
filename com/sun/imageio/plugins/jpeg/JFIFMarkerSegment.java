/*      */ package com.sun.imageio.plugins.jpeg;
/*      */ 
/*      */ import java.awt.Graphics;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.color.ICC_ColorSpace;
/*      */ import java.awt.color.ICC_Profile;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ComponentColorModel;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.IIOImage;
/*      */ import javax.imageio.ImageReader;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.event.IIOReadProgressListener;
/*      */ import javax.imageio.metadata.IIOInvalidTreeException;
/*      */ import javax.imageio.metadata.IIOMetadataNode;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ import javax.imageio.stream.MemoryCacheImageOutputStream;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ class JFIFMarkerSegment extends MarkerSegment
/*      */ {
/*      */   int majorVersion;
/*      */   int minorVersion;
/*      */   int resUnits;
/*      */   int Xdensity;
/*      */   int Ydensity;
/*      */   int thumbWidth;
/*      */   int thumbHeight;
/*   78 */   JFIFThumbRGB thumb = null;
/*   79 */   ArrayList extSegments = new ArrayList();
/*   80 */   ICCMarkerSegment iccSegment = null;
/*      */   private static final int THUMB_JPEG = 16;
/*      */   private static final int THUMB_PALETTE = 17;
/*      */   private static final int THUMB_UNASSIGNED = 18;
/*      */   private static final int THUMB_RGB = 19;
/*      */   private static final int DATA_SIZE = 14;
/*      */   private static final int ID_SIZE = 5;
/*   87 */   private final int MAX_THUMB_WIDTH = 255;
/*   88 */   private final int MAX_THUMB_HEIGHT = 255;
/*      */ 
/*   90 */   private final boolean debug = false;
/*      */ 
/*   99 */   private boolean inICC = false;
/*      */ 
/*  106 */   private ICCMarkerSegment tempICCSegment = null;
/*      */ 
/*      */   JFIFMarkerSegment()
/*      */   {
/*  113 */     super(224);
/*  114 */     this.majorVersion = 1;
/*  115 */     this.minorVersion = 2;
/*  116 */     this.resUnits = 0;
/*  117 */     this.Xdensity = 1;
/*  118 */     this.Ydensity = 1;
/*  119 */     this.thumbWidth = 0;
/*  120 */     this.thumbHeight = 0;
/*      */   }
/*      */ 
/*      */   JFIFMarkerSegment(JPEGBuffer paramJPEGBuffer)
/*      */     throws IOException
/*      */   {
/*  128 */     super(paramJPEGBuffer);
/*  129 */     paramJPEGBuffer.bufPtr += 5;
/*      */ 
/*  131 */     this.majorVersion = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)];
/*  132 */     this.minorVersion = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)];
/*  133 */     this.resUnits = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)];
/*  134 */     this.Xdensity = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/*  135 */     this.Xdensity |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/*  136 */     this.Ydensity = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/*  137 */     this.Ydensity |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/*  138 */     this.thumbWidth = (paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF);
/*  139 */     this.thumbHeight = (paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF);
/*  140 */     paramJPEGBuffer.bufAvail -= 14;
/*  141 */     if (this.thumbWidth > 0)
/*  142 */       this.thumb = new JFIFThumbRGB(paramJPEGBuffer, this.thumbWidth, this.thumbHeight);
/*      */   }
/*      */ 
/*      */   JFIFMarkerSegment(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/*  150 */     this();
/*  151 */     updateFromNativeNode(paramNode, true);
/*      */   }
/*      */ 
/*      */   protected Object clone()
/*      */   {
/*  158 */     JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)super.clone();
/*      */     Iterator localIterator;
/*  159 */     if (!this.extSegments.isEmpty()) {
/*  160 */       localJFIFMarkerSegment.extSegments = new ArrayList();
/*  161 */       for (localIterator = this.extSegments.iterator(); localIterator.hasNext(); ) {
/*  162 */         JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)localIterator.next();
/*      */ 
/*  164 */         localJFIFMarkerSegment.extSegments.add(localJFIFExtensionMarkerSegment.clone());
/*      */       }
/*      */     }
/*  167 */     if (this.iccSegment != null) {
/*  168 */       localJFIFMarkerSegment.iccSegment = ((ICCMarkerSegment)this.iccSegment.clone());
/*      */     }
/*  170 */     return localJFIFMarkerSegment;
/*      */   }
/*      */ 
/*      */   void addJFXX(JPEGBuffer paramJPEGBuffer, JPEGImageReader paramJPEGImageReader)
/*      */     throws IOException
/*      */   {
/*  179 */     this.extSegments.add(new JFIFExtensionMarkerSegment(paramJPEGBuffer, paramJPEGImageReader));
/*      */   }
/*      */ 
/*      */   void addICC(JPEGBuffer paramJPEGBuffer)
/*      */     throws IOException
/*      */   {
/*  187 */     if (!this.inICC) {
/*  188 */       if (this.iccSegment != null) {
/*  189 */         throw new IIOException("> 1 ICC APP2 Marker Segment not supported");
/*      */       }
/*      */ 
/*  192 */       this.tempICCSegment = new ICCMarkerSegment(paramJPEGBuffer);
/*  193 */       if (!this.inICC) {
/*  194 */         this.iccSegment = this.tempICCSegment;
/*  195 */         this.tempICCSegment = null;
/*      */       }
/*      */     }
/*  198 */     else if (this.tempICCSegment.addData(paramJPEGBuffer) == true) {
/*  199 */       this.iccSegment = this.tempICCSegment;
/*  200 */       this.tempICCSegment = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   void addICC(ICC_ColorSpace paramICC_ColorSpace)
/*      */     throws IOException
/*      */   {
/*  210 */     if (this.iccSegment != null) {
/*  211 */       throw new IIOException("> 1 ICC APP2 Marker Segment not supported");
/*      */     }
/*      */ 
/*  214 */     this.iccSegment = new ICCMarkerSegment(paramICC_ColorSpace);
/*      */   }
/*      */ 
/*      */   IIOMetadataNode getNativeNode()
/*      */   {
/*  222 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("app0JFIF");
/*  223 */     localIIOMetadataNode1.setAttribute("majorVersion", Integer.toString(this.majorVersion));
/*  224 */     localIIOMetadataNode1.setAttribute("minorVersion", Integer.toString(this.minorVersion));
/*  225 */     localIIOMetadataNode1.setAttribute("resUnits", Integer.toString(this.resUnits));
/*  226 */     localIIOMetadataNode1.setAttribute("Xdensity", Integer.toString(this.Xdensity));
/*  227 */     localIIOMetadataNode1.setAttribute("Ydensity", Integer.toString(this.Ydensity));
/*  228 */     localIIOMetadataNode1.setAttribute("thumbWidth", Integer.toString(this.thumbWidth));
/*  229 */     localIIOMetadataNode1.setAttribute("thumbHeight", Integer.toString(this.thumbHeight));
/*      */     IIOMetadataNode localIIOMetadataNode2;
/*      */     Iterator localIterator;
/*  230 */     if (!this.extSegments.isEmpty()) {
/*  231 */       localIIOMetadataNode2 = new IIOMetadataNode("JFXX");
/*  232 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*  233 */       for (localIterator = this.extSegments.iterator(); localIterator.hasNext(); ) {
/*  234 */         JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)localIterator.next();
/*      */ 
/*  236 */         localIIOMetadataNode2.appendChild(localJFIFExtensionMarkerSegment.getNativeNode());
/*      */       }
/*      */     }
/*  239 */     if (this.iccSegment != null) {
/*  240 */       localIIOMetadataNode1.appendChild(this.iccSegment.getNativeNode());
/*      */     }
/*      */ 
/*  243 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   void updateFromNativeNode(Node paramNode, boolean paramBoolean)
/*      */     throws IIOInvalidTreeException
/*      */   {
/*  256 */     NamedNodeMap localNamedNodeMap = paramNode.getAttributes();
/*  257 */     if (localNamedNodeMap.getLength() > 0) {
/*  258 */       int i = getAttributeValue(paramNode, localNamedNodeMap, "majorVersion", 0, 255, false);
/*      */ 
/*  260 */       this.majorVersion = (i != -1 ? i : this.majorVersion);
/*  261 */       i = getAttributeValue(paramNode, localNamedNodeMap, "minorVersion", 0, 255, false);
/*      */ 
/*  263 */       this.minorVersion = (i != -1 ? i : this.minorVersion);
/*  264 */       i = getAttributeValue(paramNode, localNamedNodeMap, "resUnits", 0, 2, false);
/*  265 */       this.resUnits = (i != -1 ? i : this.resUnits);
/*  266 */       i = getAttributeValue(paramNode, localNamedNodeMap, "Xdensity", 1, 65535, false);
/*  267 */       this.Xdensity = (i != -1 ? i : this.Xdensity);
/*  268 */       i = getAttributeValue(paramNode, localNamedNodeMap, "Ydensity", 1, 65535, false);
/*  269 */       this.Ydensity = (i != -1 ? i : this.Ydensity);
/*  270 */       i = getAttributeValue(paramNode, localNamedNodeMap, "thumbWidth", 0, 255, false);
/*  271 */       this.thumbWidth = (i != -1 ? i : this.thumbWidth);
/*  272 */       i = getAttributeValue(paramNode, localNamedNodeMap, "thumbHeight", 0, 255, false);
/*  273 */       this.thumbHeight = (i != -1 ? i : this.thumbHeight);
/*      */     }
/*  275 */     if (paramNode.hasChildNodes()) {
/*  276 */       NodeList localNodeList1 = paramNode.getChildNodes();
/*  277 */       int j = localNodeList1.getLength();
/*  278 */       if (j > 2) {
/*  279 */         throw new IIOInvalidTreeException("app0JFIF node cannot have > 2 children", paramNode);
/*      */       }
/*      */ 
/*  282 */       for (int k = 0; k < j; k++) {
/*  283 */         Node localNode1 = localNodeList1.item(k);
/*  284 */         String str = localNode1.getNodeName();
/*  285 */         if (str.equals("JFXX")) {
/*  286 */           if ((!this.extSegments.isEmpty()) && (paramBoolean)) {
/*  287 */             throw new IIOInvalidTreeException("app0JFIF node cannot have > 1 JFXX node", paramNode);
/*      */           }
/*      */ 
/*  290 */           NodeList localNodeList2 = localNode1.getChildNodes();
/*  291 */           int m = localNodeList2.getLength();
/*  292 */           for (int n = 0; n < m; n++) {
/*  293 */             Node localNode2 = localNodeList2.item(n);
/*  294 */             this.extSegments.add(new JFIFExtensionMarkerSegment(localNode2));
/*      */           }
/*      */         }
/*  297 */         if (str.equals("app2ICC")) {
/*  298 */           if ((this.iccSegment != null) && (paramBoolean)) {
/*  299 */             throw new IIOInvalidTreeException("> 1 ICC APP2 Marker Segment not supported", paramNode);
/*      */           }
/*      */ 
/*  302 */           this.iccSegment = new ICCMarkerSegment(localNode1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   int getThumbnailWidth(int paramInt) {
/*  309 */     if (this.thumb != null) {
/*  310 */       if (paramInt == 0) {
/*  311 */         return this.thumb.getWidth();
/*      */       }
/*  313 */       paramInt--;
/*      */     }
/*  315 */     JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)this.extSegments.get(paramInt);
/*      */ 
/*  317 */     return localJFIFExtensionMarkerSegment.thumb.getWidth();
/*      */   }
/*      */ 
/*      */   int getThumbnailHeight(int paramInt) {
/*  321 */     if (this.thumb != null) {
/*  322 */       if (paramInt == 0) {
/*  323 */         return this.thumb.getHeight();
/*      */       }
/*  325 */       paramInt--;
/*      */     }
/*  327 */     JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)this.extSegments.get(paramInt);
/*      */ 
/*  329 */     return localJFIFExtensionMarkerSegment.thumb.getHeight();
/*      */   }
/*      */ 
/*      */   BufferedImage getThumbnail(ImageInputStream paramImageInputStream, int paramInt, JPEGImageReader paramJPEGImageReader)
/*      */     throws IOException
/*      */   {
/*  335 */     paramJPEGImageReader.thumbnailStarted(paramInt);
/*  336 */     BufferedImage localBufferedImage = null;
/*  337 */     if ((this.thumb != null) && (paramInt == 0)) {
/*  338 */       localBufferedImage = this.thumb.getThumbnail(paramImageInputStream, paramJPEGImageReader);
/*      */     } else {
/*  340 */       if (this.thumb != null) {
/*  341 */         paramInt--;
/*      */       }
/*  343 */       JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)this.extSegments.get(paramInt);
/*      */ 
/*  345 */       localBufferedImage = localJFIFExtensionMarkerSegment.thumb.getThumbnail(paramImageInputStream, paramJPEGImageReader);
/*      */     }
/*  347 */     paramJPEGImageReader.thumbnailComplete();
/*  348 */     return localBufferedImage;
/*      */   }
/*      */ 
/*      */   void write(ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter)
/*      */     throws IOException
/*      */   {
/*  359 */     write(paramImageOutputStream, null, paramJPEGImageWriter);
/*      */   }
/*      */ 
/*      */   void write(ImageOutputStream paramImageOutputStream, BufferedImage paramBufferedImage, JPEGImageWriter paramJPEGImageWriter)
/*      */     throws IOException
/*      */   {
/*  372 */     int i = 0;
/*  373 */     int j = 0;
/*  374 */     int k = 0;
/*  375 */     int[] arrayOfInt = null;
/*  376 */     if (paramBufferedImage != null)
/*      */     {
/*  378 */       i = paramBufferedImage.getWidth();
/*  379 */       j = paramBufferedImage.getHeight();
/*  380 */       if ((i > 255) || (j > 255))
/*      */       {
/*  382 */         paramJPEGImageWriter.warningOccurred(12);
/*      */       }
/*  384 */       i = Math.min(i, 255);
/*  385 */       j = Math.min(j, 255);
/*  386 */       arrayOfInt = paramBufferedImage.getRaster().getPixels(0, 0, i, j, (int[])null);
/*      */ 
/*  389 */       k = arrayOfInt.length;
/*      */     }
/*  391 */     this.length = (16 + k);
/*  392 */     writeTag(paramImageOutputStream);
/*  393 */     byte[] arrayOfByte = { 74, 70, 73, 70, 0 };
/*  394 */     paramImageOutputStream.write(arrayOfByte);
/*  395 */     paramImageOutputStream.write(this.majorVersion);
/*  396 */     paramImageOutputStream.write(this.minorVersion);
/*  397 */     paramImageOutputStream.write(this.resUnits);
/*  398 */     write2bytes(paramImageOutputStream, this.Xdensity);
/*  399 */     write2bytes(paramImageOutputStream, this.Ydensity);
/*  400 */     paramImageOutputStream.write(i);
/*  401 */     paramImageOutputStream.write(j);
/*  402 */     if (arrayOfInt != null) {
/*  403 */       paramJPEGImageWriter.thumbnailStarted(0);
/*  404 */       writeThumbnailData(paramImageOutputStream, arrayOfInt, paramJPEGImageWriter);
/*  405 */       paramJPEGImageWriter.thumbnailComplete();
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeThumbnailData(ImageOutputStream paramImageOutputStream, int[] paramArrayOfInt, JPEGImageWriter paramJPEGImageWriter)
/*      */     throws IOException
/*      */   {
/*  416 */     int i = paramArrayOfInt.length / 20;
/*  417 */     if (i == 0) {
/*  418 */       i = 1;
/*      */     }
/*  420 */     for (int j = 0; j < paramArrayOfInt.length; j++) {
/*  421 */       paramImageOutputStream.write(paramArrayOfInt[j]);
/*  422 */       if ((j > i) && (j % i == 0))
/*  423 */         paramJPEGImageWriter.thumbnailProgress(j * 100.0F / paramArrayOfInt.length);
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeWithThumbs(ImageOutputStream paramImageOutputStream, List paramList, JPEGImageWriter paramJPEGImageWriter)
/*      */     throws IOException
/*      */   {
/*  441 */     if (paramList != null) {
/*  442 */       JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = null;
/*  443 */       if (paramList.size() == 1) {
/*  444 */         if (!this.extSegments.isEmpty()) {
/*  445 */           localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)this.extSegments.get(0);
/*      */         }
/*  447 */         writeThumb(paramImageOutputStream, (BufferedImage)paramList.get(0), localJFIFExtensionMarkerSegment, 0, true, paramJPEGImageWriter);
/*      */       }
/*      */       else
/*      */       {
/*  455 */         write(paramImageOutputStream, paramJPEGImageWriter);
/*  456 */         for (int i = 0; i < paramList.size(); i++) {
/*  457 */           localJFIFExtensionMarkerSegment = null;
/*  458 */           if (i < this.extSegments.size()) {
/*  459 */             localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)this.extSegments.get(i);
/*      */           }
/*  461 */           writeThumb(paramImageOutputStream, (BufferedImage)paramList.get(i), localJFIFExtensionMarkerSegment, i, false, paramJPEGImageWriter);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  470 */       write(paramImageOutputStream, paramJPEGImageWriter);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeThumb(ImageOutputStream paramImageOutputStream, BufferedImage paramBufferedImage, JFIFExtensionMarkerSegment paramJFIFExtensionMarkerSegment, int paramInt, boolean paramBoolean, JPEGImageWriter paramJPEGImageWriter)
/*      */     throws IOException
/*      */   {
/*  481 */     ColorModel localColorModel = paramBufferedImage.getColorModel();
/*  482 */     ColorSpace localColorSpace = localColorModel.getColorSpace();
/*      */     BufferedImage localBufferedImage;
/*  484 */     if ((localColorModel instanceof IndexColorModel))
/*      */     {
/*  487 */       if (paramBoolean) {
/*  488 */         write(paramImageOutputStream, paramJPEGImageWriter);
/*      */       }
/*  490 */       if ((paramJFIFExtensionMarkerSegment == null) || (paramJFIFExtensionMarkerSegment.code == 17))
/*      */       {
/*  492 */         writeJFXXSegment(paramInt, paramBufferedImage, paramImageOutputStream, paramJPEGImageWriter);
/*      */       }
/*      */       else {
/*  495 */         localBufferedImage = ((IndexColorModel)localColorModel).convertToIntDiscrete(paramBufferedImage.getRaster(), false);
/*      */ 
/*  498 */         paramJFIFExtensionMarkerSegment.setThumbnail(localBufferedImage);
/*  499 */         paramJPEGImageWriter.thumbnailStarted(paramInt);
/*  500 */         paramJFIFExtensionMarkerSegment.write(paramImageOutputStream, paramJPEGImageWriter);
/*  501 */         paramJPEGImageWriter.thumbnailComplete();
/*      */       }
/*  503 */     } else if (localColorSpace.getType() == 5) {
/*  504 */       if (paramJFIFExtensionMarkerSegment == null) {
/*  505 */         if (paramBoolean)
/*  506 */           write(paramImageOutputStream, paramBufferedImage, paramJPEGImageWriter);
/*      */         else
/*  508 */           writeJFXXSegment(paramInt, paramBufferedImage, paramImageOutputStream, paramJPEGImageWriter);
/*      */       }
/*      */       else
/*      */       {
/*  512 */         if (paramBoolean) {
/*  513 */           write(paramImageOutputStream, paramJPEGImageWriter);
/*      */         }
/*  515 */         if (paramJFIFExtensionMarkerSegment.code == 17) {
/*  516 */           writeJFXXSegment(paramInt, paramBufferedImage, paramImageOutputStream, paramJPEGImageWriter);
/*  517 */           paramJPEGImageWriter.warningOccurred(14);
/*      */         }
/*      */         else {
/*  520 */           paramJFIFExtensionMarkerSegment.setThumbnail(paramBufferedImage);
/*  521 */           paramJPEGImageWriter.thumbnailStarted(paramInt);
/*  522 */           paramJFIFExtensionMarkerSegment.write(paramImageOutputStream, paramJPEGImageWriter);
/*  523 */           paramJPEGImageWriter.thumbnailComplete();
/*      */         }
/*      */       }
/*  526 */     } else if (localColorSpace.getType() == 6) {
/*  527 */       if (paramJFIFExtensionMarkerSegment == null) {
/*  528 */         if (paramBoolean) {
/*  529 */           localBufferedImage = expandGrayThumb(paramBufferedImage);
/*  530 */           write(paramImageOutputStream, localBufferedImage, paramJPEGImageWriter);
/*      */         } else {
/*  532 */           writeJFXXSegment(paramInt, paramBufferedImage, paramImageOutputStream, paramJPEGImageWriter);
/*      */         }
/*      */       }
/*      */       else {
/*  536 */         if (paramBoolean) {
/*  537 */           write(paramImageOutputStream, paramJPEGImageWriter);
/*      */         }
/*  539 */         if (paramJFIFExtensionMarkerSegment.code == 19) {
/*  540 */           localBufferedImage = expandGrayThumb(paramBufferedImage);
/*  541 */           writeJFXXSegment(paramInt, localBufferedImage, paramImageOutputStream, paramJPEGImageWriter);
/*  542 */         } else if (paramJFIFExtensionMarkerSegment.code == 16) {
/*  543 */           paramJFIFExtensionMarkerSegment.setThumbnail(paramBufferedImage);
/*  544 */           paramJPEGImageWriter.thumbnailStarted(paramInt);
/*  545 */           paramJFIFExtensionMarkerSegment.write(paramImageOutputStream, paramJPEGImageWriter);
/*  546 */           paramJPEGImageWriter.thumbnailComplete();
/*  547 */         } else if (paramJFIFExtensionMarkerSegment.code == 17) {
/*  548 */           writeJFXXSegment(paramInt, paramBufferedImage, paramImageOutputStream, paramJPEGImageWriter);
/*  549 */           paramJPEGImageWriter.warningOccurred(15);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  554 */       paramJPEGImageWriter.warningOccurred(9);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeJFXXSegment(int paramInt, BufferedImage paramBufferedImage, ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter)
/*      */     throws IOException
/*      */   {
/*  570 */     JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = null;
/*      */     try {
/*  572 */       localJFIFExtensionMarkerSegment = new JFIFExtensionMarkerSegment(paramBufferedImage);
/*      */     } catch (IllegalThumbException localIllegalThumbException) {
/*  574 */       paramJPEGImageWriter.warningOccurred(9);
/*      */ 
/*  576 */       return;
/*      */     }
/*  578 */     paramJPEGImageWriter.thumbnailStarted(paramInt);
/*  579 */     localJFIFExtensionMarkerSegment.write(paramImageOutputStream, paramJPEGImageWriter);
/*  580 */     paramJPEGImageWriter.thumbnailComplete();
/*      */   }
/*      */ 
/*      */   private static BufferedImage expandGrayThumb(BufferedImage paramBufferedImage)
/*      */   {
/*  589 */     BufferedImage localBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 1);
/*      */ 
/*  592 */     Graphics localGraphics = localBufferedImage.getGraphics();
/*  593 */     localGraphics.drawImage(paramBufferedImage, 0, 0, null);
/*  594 */     return localBufferedImage;
/*      */   }
/*      */ 
/*      */   static void writeDefaultJFIF(ImageOutputStream paramImageOutputStream, List paramList, ICC_Profile paramICC_Profile, JPEGImageWriter paramJPEGImageWriter)
/*      */     throws IOException
/*      */   {
/*  612 */     JFIFMarkerSegment localJFIFMarkerSegment = new JFIFMarkerSegment();
/*  613 */     localJFIFMarkerSegment.writeWithThumbs(paramImageOutputStream, paramList, paramJPEGImageWriter);
/*  614 */     if (paramICC_Profile != null)
/*  615 */       writeICC(paramICC_Profile, paramImageOutputStream);
/*      */   }
/*      */ 
/*      */   void print()
/*      */   {
/*  623 */     printTag("JFIF");
/*  624 */     System.out.print("Version ");
/*  625 */     System.out.print(this.majorVersion);
/*  626 */     System.out.println(".0" + Integer.toString(this.minorVersion));
/*      */ 
/*  628 */     System.out.print("Resolution units: ");
/*  629 */     System.out.println(this.resUnits);
/*  630 */     System.out.print("X density: ");
/*  631 */     System.out.println(this.Xdensity);
/*  632 */     System.out.print("Y density: ");
/*  633 */     System.out.println(this.Ydensity);
/*  634 */     System.out.print("Thumbnail Width: ");
/*  635 */     System.out.println(this.thumbWidth);
/*  636 */     System.out.print("Thumbnail Height: ");
/*  637 */     System.out.println(this.thumbHeight);
/*      */     Iterator localIterator;
/*  638 */     if (!this.extSegments.isEmpty()) {
/*  639 */       for (localIterator = this.extSegments.iterator(); localIterator.hasNext(); ) {
/*  640 */         JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)localIterator.next();
/*      */ 
/*  642 */         localJFIFExtensionMarkerSegment.print();
/*      */       }
/*      */     }
/*  645 */     if (this.iccSegment != null)
/*  646 */       this.iccSegment.print();
/*      */   }
/*      */ 
/*      */   static void writeICC(ICC_Profile paramICC_Profile, ImageOutputStream paramImageOutputStream)
/*      */     throws IOException
/*      */   {
/* 1338 */     int i = 2;
/*      */ 
/* 1340 */     int j = "ICC_PROFILE".length() + 1;
/* 1341 */     int k = 2;
/* 1342 */     int m = 65535 - i - j - k;
/*      */ 
/* 1345 */     byte[] arrayOfByte1 = paramICC_Profile.getData();
/* 1346 */     int n = arrayOfByte1.length / m;
/* 1347 */     if (arrayOfByte1.length % m != 0) {
/* 1348 */       n++;
/*      */     }
/* 1350 */     int i1 = 1;
/* 1351 */     int i2 = 0;
/* 1352 */     for (int i3 = 0; i3 < n; i3++) {
/* 1353 */       int i4 = Math.min(arrayOfByte1.length - i2, m);
/* 1354 */       int i5 = i4 + k + j + i;
/* 1355 */       paramImageOutputStream.write(255);
/* 1356 */       paramImageOutputStream.write(226);
/* 1357 */       MarkerSegment.write2bytes(paramImageOutputStream, i5);
/* 1358 */       byte[] arrayOfByte2 = "ICC_PROFILE".getBytes("US-ASCII");
/* 1359 */       paramImageOutputStream.write(arrayOfByte2);
/* 1360 */       paramImageOutputStream.write(0);
/* 1361 */       paramImageOutputStream.write(i1++);
/* 1362 */       paramImageOutputStream.write(n);
/* 1363 */       paramImageOutputStream.write(arrayOfByte1, i2, i4);
/* 1364 */       i2 += i4;
/*      */     }
/*      */   }
/*      */ 
/*      */   class ICCMarkerSegment extends MarkerSegment
/*      */   {
/* 1375 */     ArrayList chunks = null;
/* 1376 */     byte[] profile = null;
/*      */     private static final int ID_SIZE = 12;
/*      */     int chunksRead;
/*      */     int numChunks;
/*      */ 
/*      */     ICCMarkerSegment(ICC_ColorSpace arg2)
/*      */     {
/* 1383 */       super();
/* 1384 */       this.chunks = null;
/* 1385 */       this.chunksRead = 0;
/* 1386 */       this.numChunks = 0;
/*      */       Object localObject;
/* 1387 */       this.profile = localObject.getProfile().getData();
/*      */     }
/*      */ 
/*      */     ICCMarkerSegment(JPEGBuffer arg2) throws IOException {
/* 1391 */       super();
/*      */ 
/* 1395 */       localJPEGBuffer.bufPtr += 12;
/* 1396 */       localJPEGBuffer.bufAvail -= 12;
/*      */ 
/* 1402 */       this.length -= 12;
/*      */ 
/* 1405 */       int i = localJPEGBuffer.buf[localJPEGBuffer.bufPtr] & 0xFF;
/*      */ 
/* 1407 */       this.numChunks = (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 1)] & 0xFF);
/*      */ 
/* 1409 */       if (i > this.numChunks) {
/* 1410 */         throw new IIOException("Image format Error; chunk num > num chunks");
/*      */       }
/*      */ 
/* 1415 */       if (this.numChunks == 1)
/*      */       {
/* 1417 */         this.length -= 2;
/* 1418 */         this.profile = new byte[this.length];
/* 1419 */         localJPEGBuffer.bufPtr += 2;
/* 1420 */         localJPEGBuffer.bufAvail -= 2;
/* 1421 */         localJPEGBuffer.readData(this.profile);
/* 1422 */         JFIFMarkerSegment.this.inICC = false;
/*      */       }
/*      */       else {
/* 1425 */         byte[] arrayOfByte = new byte[this.length];
/*      */ 
/* 1428 */         this.length -= 2;
/* 1429 */         localJPEGBuffer.readData(arrayOfByte);
/* 1430 */         this.chunks = new ArrayList();
/* 1431 */         this.chunks.add(arrayOfByte);
/* 1432 */         this.chunksRead = 1;
/* 1433 */         JFIFMarkerSegment.this.inICC = true;
/*      */       }
/*      */     }
/*      */ 
/*      */     ICCMarkerSegment(Node arg2) throws IIOInvalidTreeException {
/* 1438 */       super();
/*      */       Object localObject;
/* 1439 */       if ((localObject instanceof IIOMetadataNode)) {
/* 1440 */         IIOMetadataNode localIIOMetadataNode = (IIOMetadataNode)localObject;
/* 1441 */         ICC_Profile localICC_Profile = (ICC_Profile)localIIOMetadataNode.getUserObject();
/* 1442 */         if (localICC_Profile != null)
/* 1443 */           this.profile = localICC_Profile.getData();
/*      */       }
/*      */     }
/*      */ 
/*      */     protected Object clone()
/*      */     {
/* 1449 */       ICCMarkerSegment localICCMarkerSegment = (ICCMarkerSegment)super.clone();
/* 1450 */       if (this.profile != null) {
/* 1451 */         localICCMarkerSegment.profile = ((byte[])this.profile.clone());
/*      */       }
/* 1453 */       return localICCMarkerSegment;
/*      */     }
/*      */ 
/*      */     boolean addData(JPEGBuffer paramJPEGBuffer)
/*      */       throws IOException
/*      */     {
/* 1461 */       paramJPEGBuffer.bufPtr += 1;
/* 1462 */       paramJPEGBuffer.bufAvail -= 1;
/*      */ 
/* 1464 */       int i = (paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8;
/* 1465 */       i |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/* 1466 */       paramJPEGBuffer.bufAvail -= 2;
/*      */ 
/* 1468 */       i -= 2;
/*      */ 
/* 1470 */       paramJPEGBuffer.bufPtr += 12;
/* 1471 */       paramJPEGBuffer.bufAvail -= 12;
/*      */ 
/* 1477 */       i -= 12;
/*      */ 
/* 1480 */       int j = paramJPEGBuffer.buf[paramJPEGBuffer.bufPtr] & 0xFF;
/* 1481 */       if (j > this.numChunks) {
/* 1482 */         throw new IIOException("Image format Error; chunk num > num chunks");
/*      */       }
/*      */ 
/* 1487 */       int k = paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr + 1)] & 0xFF;
/* 1488 */       if (this.numChunks != k) {
/* 1489 */         throw new IIOException("Image format Error; icc num chunks mismatch");
/*      */       }
/*      */ 
/* 1492 */       i -= 2;
/*      */ 
/* 1498 */       boolean bool = false;
/* 1499 */       byte[] arrayOfByte1 = new byte[i];
/* 1500 */       paramJPEGBuffer.readData(arrayOfByte1);
/* 1501 */       this.chunks.add(arrayOfByte1);
/* 1502 */       this.length += i;
/* 1503 */       this.chunksRead += 1;
/* 1504 */       if (this.chunksRead < this.numChunks) {
/* 1505 */         JFIFMarkerSegment.this.inICC = true;
/*      */       }
/*      */       else
/*      */       {
/* 1512 */         this.profile = new byte[this.length];
/*      */ 
/* 1516 */         int m = 0;
/* 1517 */         for (int n = 1; n <= this.numChunks; n++) {
/* 1518 */           int i1 = 0;
/* 1519 */           for (int i2 = 0; i2 < this.chunks.size(); i2++) {
/* 1520 */             byte[] arrayOfByte2 = (byte[])this.chunks.get(i2);
/* 1521 */             if (arrayOfByte2[0] == n) {
/* 1522 */               System.arraycopy(arrayOfByte2, 2, this.profile, m, arrayOfByte2.length - 2);
/*      */ 
/* 1525 */               m += arrayOfByte2.length - 2;
/* 1526 */               i1 = 1;
/*      */             }
/*      */           }
/* 1529 */           if (i1 == 0) {
/* 1530 */             throw new IIOException("Image Format Error: Missing ICC chunk num " + n);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1535 */         this.chunks = null;
/* 1536 */         this.chunksRead = 0;
/* 1537 */         this.numChunks = 0;
/* 1538 */         JFIFMarkerSegment.this.inICC = false;
/* 1539 */         bool = true;
/*      */       }
/* 1541 */       return bool;
/*      */     }
/*      */ 
/*      */     IIOMetadataNode getNativeNode() {
/* 1545 */       IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("app2ICC");
/* 1546 */       if (this.profile != null) {
/* 1547 */         localIIOMetadataNode.setUserObject(ICC_Profile.getInstance(this.profile));
/*      */       }
/* 1549 */       return localIIOMetadataNode;
/*      */     }
/*      */ 
/*      */     void write(ImageOutputStream paramImageOutputStream)
/*      */       throws IOException
/*      */     {
/*      */     }
/*      */ 
/*      */     void print()
/*      */     {
/* 1561 */       printTag("ICC Profile APP2");
/*      */     }
/*      */   }
/*      */ 
/*      */   private class IllegalThumbException extends Exception
/*      */   {
/*      */     private IllegalThumbException()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   class JFIFExtensionMarkerSegment extends MarkerSegment
/*      */   {
/*      */     int code;
/*      */     JFIFMarkerSegment.JFIFThumb thumb;
/*      */     private static final int DATA_SIZE = 6;
/*      */     private static final int ID_SIZE = 5;
/*      */ 
/*      */     JFIFExtensionMarkerSegment(JPEGBuffer paramJPEGImageReader, JPEGImageReader arg3)
/*      */       throws IOException
/*      */     {
/*  662 */       super();
/*  663 */       paramJPEGImageReader.bufPtr += 5;
/*      */ 
/*  665 */       this.code = (paramJPEGImageReader.buf[(paramJPEGImageReader.bufPtr++)] & 0xFF);
/*  666 */       paramJPEGImageReader.bufAvail -= 6;
/*  667 */       if (this.code == 16)
/*      */       {
/*      */         JPEGImageReader localJPEGImageReader;
/*  668 */         this.thumb = new JFIFMarkerSegment.JFIFThumbJPEG(JFIFMarkerSegment.this, paramJPEGImageReader, this.length, localJPEGImageReader);
/*      */       } else {
/*  670 */         paramJPEGImageReader.loadBuf(2);
/*  671 */         int i = paramJPEGImageReader.buf[(paramJPEGImageReader.bufPtr++)] & 0xFF;
/*  672 */         int j = paramJPEGImageReader.buf[(paramJPEGImageReader.bufPtr++)] & 0xFF;
/*  673 */         paramJPEGImageReader.bufAvail -= 2;
/*      */ 
/*  675 */         if (this.code == 17)
/*  676 */           this.thumb = new JFIFMarkerSegment.JFIFThumbPalette(JFIFMarkerSegment.this, paramJPEGImageReader, i, j);
/*      */         else
/*  678 */           this.thumb = new JFIFMarkerSegment.JFIFThumbRGB(JFIFMarkerSegment.this, paramJPEGImageReader, i, j);
/*      */       }
/*      */     }
/*      */ 
/*      */     JFIFExtensionMarkerSegment(Node arg2) throws IIOInvalidTreeException
/*      */     {
/*  684 */       super();
/*      */       Node localNode1;
/*  685 */       NamedNodeMap localNamedNodeMap = localNode1.getAttributes();
/*  686 */       if (localNamedNodeMap.getLength() > 0) {
/*  687 */         this.code = getAttributeValue(localNode1, localNamedNodeMap, "extensionCode", 16, 19, false);
/*      */ 
/*  693 */         if (this.code == 18)
/*  694 */           throw new IIOInvalidTreeException("invalid extensionCode attribute value", localNode1);
/*      */       }
/*      */       else
/*      */       {
/*  698 */         this.code = 18;
/*      */       }
/*      */ 
/*  701 */       if (localNode1.getChildNodes().getLength() != 1) {
/*  702 */         throw new IIOInvalidTreeException("app0JFXX node must have exactly 1 child", localNode1);
/*      */       }
/*      */ 
/*  705 */       Node localNode2 = localNode1.getFirstChild();
/*  706 */       String str = localNode2.getNodeName();
/*  707 */       if (str.equals("JFIFthumbJPEG")) {
/*  708 */         if (this.code == 18) {
/*  709 */           this.code = 16;
/*      */         }
/*  711 */         this.thumb = new JFIFMarkerSegment.JFIFThumbJPEG(JFIFMarkerSegment.this, localNode2);
/*  712 */       } else if (str.equals("JFIFthumbPalette")) {
/*  713 */         if (this.code == 18) {
/*  714 */           this.code = 17;
/*      */         }
/*  716 */         this.thumb = new JFIFMarkerSegment.JFIFThumbPalette(JFIFMarkerSegment.this, localNode2);
/*  717 */       } else if (str.equals("JFIFthumbRGB")) {
/*  718 */         if (this.code == 18) {
/*  719 */           this.code = 19;
/*      */         }
/*  721 */         this.thumb = new JFIFMarkerSegment.JFIFThumbRGB(JFIFMarkerSegment.this, localNode2);
/*      */       } else {
/*  723 */         throw new IIOInvalidTreeException("unrecognized app0JFXX child node", localNode1);
/*      */       }
/*      */     }
/*      */ 
/*      */     JFIFExtensionMarkerSegment(BufferedImage arg2)
/*      */       throws JFIFMarkerSegment.IllegalThumbException
/*      */     {
/*  731 */       super();
/*      */       BufferedImage localBufferedImage;
/*  732 */       ColorModel localColorModel = localBufferedImage.getColorModel();
/*  733 */       int i = localColorModel.getColorSpace().getType();
/*  734 */       if (localColorModel.hasAlpha()) {
/*  735 */         throw new JFIFMarkerSegment.IllegalThumbException(JFIFMarkerSegment.this, null);
/*      */       }
/*  737 */       if ((localColorModel instanceof IndexColorModel)) {
/*  738 */         this.code = 17;
/*  739 */         this.thumb = new JFIFMarkerSegment.JFIFThumbPalette(JFIFMarkerSegment.this, localBufferedImage);
/*  740 */       } else if (i == 5) {
/*  741 */         this.code = 19;
/*  742 */         this.thumb = new JFIFMarkerSegment.JFIFThumbRGB(JFIFMarkerSegment.this, localBufferedImage);
/*  743 */       } else if (i == 6) {
/*  744 */         this.code = 16;
/*  745 */         this.thumb = new JFIFMarkerSegment.JFIFThumbJPEG(JFIFMarkerSegment.this, localBufferedImage);
/*      */       } else {
/*  747 */         throw new JFIFMarkerSegment.IllegalThumbException(JFIFMarkerSegment.this, null);
/*      */       }
/*      */     }
/*      */ 
/*      */     void setThumbnail(BufferedImage paramBufferedImage) {
/*      */       try {
/*  753 */         switch (this.code) {
/*      */         case 17:
/*  755 */           this.thumb = new JFIFMarkerSegment.JFIFThumbPalette(JFIFMarkerSegment.this, paramBufferedImage);
/*  756 */           break;
/*      */         case 19:
/*  758 */           this.thumb = new JFIFMarkerSegment.JFIFThumbRGB(JFIFMarkerSegment.this, paramBufferedImage);
/*  759 */           break;
/*      */         case 16:
/*  761 */           this.thumb = new JFIFMarkerSegment.JFIFThumbJPEG(JFIFMarkerSegment.this, paramBufferedImage);
/*      */         case 18:
/*      */         }
/*      */       }
/*      */       catch (JFIFMarkerSegment.IllegalThumbException localIllegalThumbException) {
/*  766 */         throw new InternalError("Illegal thumb in setThumbnail!");
/*      */       }
/*      */     }
/*      */ 
/*      */     protected Object clone() {
/*  771 */       JFIFExtensionMarkerSegment localJFIFExtensionMarkerSegment = (JFIFExtensionMarkerSegment)super.clone();
/*      */ 
/*  773 */       if (this.thumb != null) {
/*  774 */         localJFIFExtensionMarkerSegment.thumb = ((JFIFMarkerSegment.JFIFThumb)this.thumb.clone());
/*      */       }
/*  776 */       return localJFIFExtensionMarkerSegment;
/*      */     }
/*      */ 
/*      */     IIOMetadataNode getNativeNode() {
/*  780 */       IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("app0JFXX");
/*  781 */       localIIOMetadataNode.setAttribute("extensionCode", Integer.toString(this.code));
/*  782 */       localIIOMetadataNode.appendChild(this.thumb.getNativeNode());
/*  783 */       return localIIOMetadataNode;
/*      */     }
/*      */ 
/*      */     void write(ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter) throws IOException
/*      */     {
/*  788 */       this.length = (8 + this.thumb.getLength());
/*  789 */       writeTag(paramImageOutputStream);
/*  790 */       byte[] arrayOfByte = { 74, 70, 88, 88, 0 };
/*  791 */       paramImageOutputStream.write(arrayOfByte);
/*  792 */       paramImageOutputStream.write(this.code);
/*  793 */       this.thumb.write(paramImageOutputStream, paramJPEGImageWriter);
/*      */     }
/*      */ 
/*      */     void print() {
/*  797 */       printTag("JFXX");
/*  798 */       this.thumb.print();
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class JFIFThumb
/*      */     implements Cloneable
/*      */   {
/*  807 */     long streamPos = -1L;
/*      */ 
/*      */     abstract int getLength();
/*      */ 
/*      */     abstract int getWidth();
/*      */ 
/*      */     abstract int getHeight();
/*      */ 
/*      */     abstract BufferedImage getThumbnail(ImageInputStream paramImageInputStream, JPEGImageReader paramJPEGImageReader)
/*      */       throws IOException;
/*      */ 
/*      */     protected JFIFThumb()
/*      */     {
/*      */     }
/*      */ 
/*      */     protected JFIFThumb(JPEGBuffer arg2)
/*      */       throws IOException
/*      */     {
/*      */       Object localObject;
/*  819 */       this.streamPos = localObject.getStreamPosition();
/*      */     }
/*      */ 
/*      */     abstract void print();
/*      */ 
/*      */     abstract IIOMetadataNode getNativeNode();
/*      */ 
/*      */     abstract void write(ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter) throws IOException;
/*      */ 
/*      */     protected Object clone()
/*      */     {
/*      */       try {
/*  831 */         return super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */       }
/*  833 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   class JFIFThumbJPEG extends JFIFMarkerSegment.JFIFThumb
/*      */   {
/* 1124 */     JPEGMetadata thumbMetadata = null;
/* 1125 */     byte[] data = null;
/*      */     private static final int PREAMBLE_SIZE = 6;
/*      */ 
/*      */     JFIFThumbJPEG(JPEGBuffer paramInt, int paramJPEGImageReader, JPEGImageReader arg4)
/*      */       throws IOException
/*      */     {
/* 1131 */       super(paramInt);
/*      */ 
/* 1133 */       long l = this.streamPos + (paramJPEGImageReader - 6);
/*      */ 
/* 1136 */       paramInt.iis.seek(this.streamPos);
/*      */       JPEGImageReader localJPEGImageReader;
/* 1137 */       this.thumbMetadata = new JPEGMetadata(false, true, paramInt.iis, localJPEGImageReader);
/*      */ 
/* 1139 */       paramInt.iis.seek(l);
/*      */ 
/* 1141 */       paramInt.bufAvail = 0;
/* 1142 */       paramInt.bufPtr = 0;
/*      */     }
/*      */     JFIFThumbJPEG(Node arg2) throws IIOInvalidTreeException {
/* 1145 */       super();
/*      */       Node localNode1;
/* 1146 */       if (localNode1.getChildNodes().getLength() > 1) {
/* 1147 */         throw new IIOInvalidTreeException("JFIFThumbJPEG node must have 0 or 1 child", localNode1);
/*      */       }
/*      */ 
/* 1150 */       Node localNode2 = localNode1.getFirstChild();
/* 1151 */       if (localNode2 != null) {
/* 1152 */         String str = localNode2.getNodeName();
/* 1153 */         if (!str.equals("markerSequence")) {
/* 1154 */           throw new IIOInvalidTreeException("JFIFThumbJPEG child must be a markerSequence node", localNode1);
/*      */         }
/*      */ 
/* 1158 */         this.thumbMetadata = new JPEGMetadata(false, true);
/* 1159 */         this.thumbMetadata.setFromMarkerSequenceNode(localNode2);
/*      */       }
/*      */     }
/*      */ 
/* 1163 */     JFIFThumbJPEG(BufferedImage arg2) throws JFIFMarkerSegment.IllegalThumbException { super();
/* 1164 */       int i = 4096;
/* 1165 */       int j = 65527;
/*      */       try {
/* 1167 */         ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(i);
/*      */ 
/* 1169 */         MemoryCacheImageOutputStream localMemoryCacheImageOutputStream = new MemoryCacheImageOutputStream(localByteArrayOutputStream);
/*      */ 
/* 1172 */         JPEGImageWriter localJPEGImageWriter = new JPEGImageWriter(null);
/*      */ 
/* 1174 */         localJPEGImageWriter.setOutput(localMemoryCacheImageOutputStream);
/*      */         RenderedImage localRenderedImage;
/* 1177 */         JPEGMetadata localJPEGMetadata = (JPEGMetadata)localJPEGImageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(localRenderedImage), null);
/*      */ 
/* 1182 */         MarkerSegment localMarkerSegment = localJPEGMetadata.findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 1184 */         if (localMarkerSegment == null) {
/* 1185 */           throw new JFIFMarkerSegment.IllegalThumbException(JFIFMarkerSegment.this, null);
/*      */         }
/*      */ 
/* 1188 */         localJPEGMetadata.markerSequence.remove(localMarkerSegment);
/*      */ 
/* 1211 */         localJPEGImageWriter.write(new IIOImage(localRenderedImage, null, localJPEGMetadata));
/*      */ 
/* 1213 */         localJPEGImageWriter.dispose();
/*      */ 
/* 1215 */         if (localByteArrayOutputStream.size() > j) {
/* 1216 */           throw new JFIFMarkerSegment.IllegalThumbException(JFIFMarkerSegment.this, null);
/*      */         }
/* 1218 */         this.data = localByteArrayOutputStream.toByteArray();
/*      */       } catch (IOException localIOException) {
/* 1220 */         throw new JFIFMarkerSegment.IllegalThumbException(JFIFMarkerSegment.this, null);
/*      */       } }
/*      */ 
/*      */     int getWidth()
/*      */     {
/* 1225 */       int i = 0;
/* 1226 */       SOFMarkerSegment localSOFMarkerSegment = (SOFMarkerSegment)this.thumbMetadata.findMarkerSegment(SOFMarkerSegment.class, true);
/*      */ 
/* 1229 */       if (localSOFMarkerSegment != null) {
/* 1230 */         i = localSOFMarkerSegment.samplesPerLine;
/*      */       }
/* 1232 */       return i;
/*      */     }
/*      */ 
/*      */     int getHeight() {
/* 1236 */       int i = 0;
/* 1237 */       SOFMarkerSegment localSOFMarkerSegment = (SOFMarkerSegment)this.thumbMetadata.findMarkerSegment(SOFMarkerSegment.class, true);
/*      */ 
/* 1240 */       if (localSOFMarkerSegment != null) {
/* 1241 */         i = localSOFMarkerSegment.numLines;
/*      */       }
/* 1243 */       return i;
/*      */     }
/*      */ 
/*      */     BufferedImage getThumbnail(ImageInputStream paramImageInputStream, JPEGImageReader paramJPEGImageReader)
/*      */       throws IOException
/*      */     {
/* 1270 */       paramImageInputStream.mark();
/* 1271 */       paramImageInputStream.seek(this.streamPos);
/* 1272 */       JPEGImageReader localJPEGImageReader = new JPEGImageReader(null);
/* 1273 */       localJPEGImageReader.setInput(paramImageInputStream);
/* 1274 */       localJPEGImageReader.addIIOReadProgressListener(new ThumbnailReadListener(paramJPEGImageReader));
/*      */ 
/* 1276 */       BufferedImage localBufferedImage = localJPEGImageReader.read(0, null);
/* 1277 */       localJPEGImageReader.dispose();
/* 1278 */       paramImageInputStream.reset();
/* 1279 */       return localBufferedImage;
/*      */     }
/*      */ 
/*      */     protected Object clone() {
/* 1283 */       JFIFThumbJPEG localJFIFThumbJPEG = (JFIFThumbJPEG)super.clone();
/* 1284 */       if (this.thumbMetadata != null) {
/* 1285 */         localJFIFThumbJPEG.thumbMetadata = ((JPEGMetadata)this.thumbMetadata.clone());
/*      */       }
/* 1287 */       return localJFIFThumbJPEG;
/*      */     }
/*      */ 
/*      */     IIOMetadataNode getNativeNode() {
/* 1291 */       IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("JFIFthumbJPEG");
/* 1292 */       if (this.thumbMetadata != null) {
/* 1293 */         localIIOMetadataNode.appendChild(this.thumbMetadata.getNativeTree());
/*      */       }
/* 1295 */       return localIIOMetadataNode;
/*      */     }
/*      */ 
/*      */     int getLength() {
/* 1299 */       if (this.data == null) {
/* 1300 */         return 0;
/*      */       }
/* 1302 */       return this.data.length;
/*      */     }
/*      */ 
/*      */     void write(ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter)
/*      */       throws IOException
/*      */     {
/* 1308 */       int i = this.data.length / 20;
/* 1309 */       if (i == 0) {
/* 1310 */         i = 1;
/*      */       }
/* 1312 */       int j = 0;
/* 1313 */       while (j < this.data.length) {
/* 1314 */         int k = Math.min(i, this.data.length - j);
/* 1315 */         paramImageOutputStream.write(this.data, j, k);
/* 1316 */         j += i;
/* 1317 */         float f = j * 100.0F / this.data.length;
/* 1318 */         if (f > 100.0F) {
/* 1319 */           f = 100.0F;
/*      */         }
/* 1321 */         paramJPEGImageWriter.thumbnailProgress(f);
/*      */       }
/*      */     }
/*      */ 
/*      */     void print() {
/* 1326 */       System.out.println("JFIF thumbnail stored as JPEG");
/*      */     }
/*      */ 
/*      */     private class ThumbnailReadListener
/*      */       implements IIOReadProgressListener
/*      */     {
/* 1248 */       JPEGImageReader reader = null;
/*      */ 
/*      */       ThumbnailReadListener(JPEGImageReader arg2)
/*      */       {
/*      */         Object localObject;
/* 1250 */         this.reader = localObject;
/*      */       }
/*      */       public void sequenceStarted(ImageReader paramImageReader, int paramInt) {
/*      */       }
/*      */       public void sequenceComplete(ImageReader paramImageReader) {
/*      */       }
/*      */       public void imageStarted(ImageReader paramImageReader, int paramInt) {  } 
/* 1257 */       public void imageProgress(ImageReader paramImageReader, float paramFloat) { this.reader.thumbnailProgress(paramFloat); }
/*      */ 
/*      */ 
/*      */       public void imageComplete(ImageReader paramImageReader)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void thumbnailStarted(ImageReader paramImageReader, int paramInt1, int paramInt2)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void thumbnailProgress(ImageReader paramImageReader, float paramFloat)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void thumbnailComplete(ImageReader paramImageReader)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void readAborted(ImageReader paramImageReader)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   class JFIFThumbPalette extends JFIFMarkerSegment.JFIFThumbUncompressed
/*      */   {
/*      */     private static final int PALETTE_SIZE = 768;
/*      */ 
/*      */     JFIFThumbPalette(JPEGBuffer paramInt1, int paramInt2, int arg4)
/*      */       throws IOException
/*      */     {
/* 1034 */       super(paramInt1, paramInt2, i, 768 + paramInt2 * i, "JFIFThumbPalette");
/*      */     }
/*      */ 
/*      */     JFIFThumbPalette(Node arg2)
/*      */       throws IIOInvalidTreeException
/*      */     {
/* 1042 */       super(localNode, "JFIFThumbPalette");
/*      */     }
/*      */ 
/*      */     JFIFThumbPalette(BufferedImage arg2) throws JFIFMarkerSegment.IllegalThumbException {
/* 1046 */       super(localBufferedImage);
/* 1047 */       IndexColorModel localIndexColorModel = (IndexColorModel)this.thumbnail.getColorModel();
/* 1048 */       if (localIndexColorModel.getMapSize() > 256)
/* 1049 */         throw new JFIFMarkerSegment.IllegalThumbException(JFIFMarkerSegment.this, null);
/*      */     }
/*      */ 
/*      */     int getLength()
/*      */     {
/* 1054 */       return this.thumbWidth * this.thumbHeight + 768;
/*      */     }
/*      */ 
/*      */     BufferedImage getThumbnail(ImageInputStream paramImageInputStream, JPEGImageReader paramJPEGImageReader)
/*      */       throws IOException
/*      */     {
/* 1060 */       paramImageInputStream.mark();
/* 1061 */       paramImageInputStream.seek(this.streamPos);
/*      */ 
/* 1063 */       byte[] arrayOfByte = new byte[768];
/* 1064 */       float f = 768.0F / getLength();
/* 1065 */       readByteBuffer(paramImageInputStream, arrayOfByte, paramJPEGImageReader, f, 0.0F);
/*      */ 
/* 1070 */       DataBufferByte localDataBufferByte = new DataBufferByte(this.thumbWidth * this.thumbHeight);
/* 1071 */       readByteBuffer(paramImageInputStream, localDataBufferByte.getData(), paramJPEGImageReader, 1.0F - f, f);
/*      */ 
/* 1076 */       paramImageInputStream.read();
/* 1077 */       paramImageInputStream.reset();
/*      */ 
/* 1079 */       IndexColorModel localIndexColorModel = new IndexColorModel(8, 256, arrayOfByte, 0, false);
/*      */ 
/* 1084 */       SampleModel localSampleModel = localIndexColorModel.createCompatibleSampleModel(this.thumbWidth, this.thumbHeight);
/*      */ 
/* 1086 */       WritableRaster localWritableRaster = Raster.createWritableRaster(localSampleModel, localDataBufferByte, null);
/*      */ 
/* 1088 */       return new BufferedImage(localIndexColorModel, localWritableRaster, false, null);
/*      */     }
/*      */ 
/*      */     void write(ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter)
/*      */       throws IOException
/*      */     {
/* 1096 */       super.write(paramImageOutputStream, paramJPEGImageWriter);
/*      */ 
/* 1098 */       byte[] arrayOfByte1 = new byte[768];
/* 1099 */       IndexColorModel localIndexColorModel = (IndexColorModel)this.thumbnail.getColorModel();
/* 1100 */       byte[] arrayOfByte2 = new byte[256];
/* 1101 */       byte[] arrayOfByte3 = new byte[256];
/* 1102 */       byte[] arrayOfByte4 = new byte[256];
/* 1103 */       localIndexColorModel.getReds(arrayOfByte2);
/* 1104 */       localIndexColorModel.getGreens(arrayOfByte3);
/* 1105 */       localIndexColorModel.getBlues(arrayOfByte4);
/* 1106 */       for (int i = 0; i < 256; i++) {
/* 1107 */         arrayOfByte1[(i * 3)] = arrayOfByte2[i];
/* 1108 */         arrayOfByte1[(i * 3 + 1)] = arrayOfByte3[i];
/* 1109 */         arrayOfByte1[(i * 3 + 2)] = arrayOfByte4[i];
/*      */       }
/* 1111 */       paramImageOutputStream.write(arrayOfByte1);
/* 1112 */       writePixels(paramImageOutputStream, paramJPEGImageWriter);
/*      */     }
/*      */   }
/*      */ 
/*      */   class JFIFThumbRGB extends JFIFMarkerSegment.JFIFThumbUncompressed
/*      */   {
/*      */     JFIFThumbRGB(JPEGBuffer paramInt1, int paramInt2, int arg4)
/*      */       throws IOException
/*      */     {
/*  970 */       super(paramInt1, paramInt2, i, paramInt2 * i * 3, "JFIFthumbRGB");
/*      */     }
/*      */ 
/*      */     JFIFThumbRGB(Node arg2) throws IIOInvalidTreeException {
/*  974 */       super(localNode, "JFIFthumbRGB");
/*      */     }
/*      */ 
/*      */     JFIFThumbRGB(BufferedImage arg2) throws JFIFMarkerSegment.IllegalThumbException {
/*  978 */       super(localBufferedImage);
/*      */     }
/*      */ 
/*      */     int getLength() {
/*  982 */       return this.thumbWidth * this.thumbHeight * 3;
/*      */     }
/*      */ 
/*      */     BufferedImage getThumbnail(ImageInputStream paramImageInputStream, JPEGImageReader paramJPEGImageReader)
/*      */       throws IOException
/*      */     {
/*  988 */       paramImageInputStream.mark();
/*  989 */       paramImageInputStream.seek(this.streamPos);
/*  990 */       DataBufferByte localDataBufferByte = new DataBufferByte(getLength());
/*  991 */       readByteBuffer(paramImageInputStream, localDataBufferByte.getData(), paramJPEGImageReader, 1.0F, 0.0F);
/*      */ 
/*  996 */       paramImageInputStream.reset();
/*      */ 
/*  998 */       WritableRaster localWritableRaster = Raster.createInterleavedRaster(localDataBufferByte, this.thumbWidth, this.thumbHeight, this.thumbWidth * 3, 3, new int[] { 0, 1, 2 }, null);
/*      */ 
/* 1006 */       ComponentColorModel localComponentColorModel = new ComponentColorModel(JPEG.JCS.sRGB, false, false, 1, 0);
/*      */ 
/* 1011 */       return new BufferedImage(localComponentColorModel, localWritableRaster, false, null);
/*      */     }
/*      */ 
/*      */     void write(ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter)
/*      */       throws IOException
/*      */     {
/* 1019 */       super.write(paramImageOutputStream, paramJPEGImageWriter);
/* 1020 */       writePixels(paramImageOutputStream, paramJPEGImageWriter);
/*      */     }
/*      */   }
/*      */ 
/*      */   abstract class JFIFThumbUncompressed extends JFIFMarkerSegment.JFIFThumb
/*      */   {
/*  839 */     BufferedImage thumbnail = null;
/*      */     int thumbWidth;
/*      */     int thumbHeight;
/*      */     String name;
/*      */ 
/*      */     JFIFThumbUncompressed(JPEGBuffer paramInt1, int paramInt2, int paramInt3, int paramString, String arg6)
/*      */       throws IOException
/*      */     {
/*  850 */       super(paramInt1);
/*  851 */       this.thumbWidth = paramInt2;
/*  852 */       this.thumbHeight = paramInt3;
/*      */ 
/*  854 */       paramInt1.skipData(paramString);
/*      */       Object localObject;
/*  855 */       this.name = localObject;
/*      */     }
/*      */ 
/*      */     JFIFThumbUncompressed(Node paramString, String arg3) throws IIOInvalidTreeException {
/*  859 */       super();
/*      */ 
/*  861 */       this.thumbWidth = 0;
/*  862 */       this.thumbHeight = 0;
/*      */       String str;
/*  863 */       this.name = str;
/*  864 */       NamedNodeMap localNamedNodeMap = paramString.getAttributes();
/*  865 */       int i = localNamedNodeMap.getLength();
/*  866 */       if (i > 2) {
/*  867 */         throw new IIOInvalidTreeException(str + " node cannot have > 2 attributes", paramString);
/*      */       }
/*      */ 
/*  870 */       if (i != 0) {
/*  871 */         int j = MarkerSegment.getAttributeValue(paramString, localNamedNodeMap, "thumbWidth", 0, 255, false);
/*      */ 
/*  873 */         this.thumbWidth = (j != -1 ? j : this.thumbWidth);
/*  874 */         j = MarkerSegment.getAttributeValue(paramString, localNamedNodeMap, "thumbHeight", 0, 255, false);
/*      */ 
/*  876 */         this.thumbHeight = (j != -1 ? j : this.thumbHeight);
/*      */       }
/*      */     }
/*      */ 
/*  880 */     JFIFThumbUncompressed(BufferedImage arg2) { super();
/*      */       Object localObject;
/*  881 */       this.thumbnail = localObject;
/*  882 */       this.thumbWidth = localObject.getWidth();
/*  883 */       this.thumbHeight = localObject.getHeight();
/*  884 */       this.name = null;
/*      */     }
/*      */ 
/*      */     void readByteBuffer(ImageInputStream paramImageInputStream, byte[] paramArrayOfByte, JPEGImageReader paramJPEGImageReader, float paramFloat1, float paramFloat2)
/*      */       throws IOException
/*      */     {
/*  892 */       int i = Math.max((int)(paramArrayOfByte.length / 20 / paramFloat1), 1);
/*      */ 
/*  894 */       int j = 0;
/*  895 */       while (j < paramArrayOfByte.length) {
/*  896 */         int k = Math.min(i, paramArrayOfByte.length - j);
/*  897 */         paramImageInputStream.read(paramArrayOfByte, j, k);
/*  898 */         j += i;
/*  899 */         float f = j * 100.0F / paramArrayOfByte.length * paramFloat1 + paramFloat2;
/*      */ 
/*  902 */         if (f > 100.0F) {
/*  903 */           f = 100.0F;
/*      */         }
/*  905 */         paramJPEGImageReader.thumbnailProgress(f);
/*      */       }
/*      */     }
/*      */ 
/*      */     int getWidth()
/*      */     {
/*  911 */       return this.thumbWidth;
/*      */     }
/*      */ 
/*      */     int getHeight() {
/*  915 */       return this.thumbHeight;
/*      */     }
/*      */ 
/*      */     IIOMetadataNode getNativeNode() {
/*  919 */       IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode(this.name);
/*  920 */       localIIOMetadataNode.setAttribute("thumbWidth", Integer.toString(this.thumbWidth));
/*  921 */       localIIOMetadataNode.setAttribute("thumbHeight", Integer.toString(this.thumbHeight));
/*  922 */       return localIIOMetadataNode;
/*      */     }
/*      */ 
/*      */     void write(ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter) throws IOException
/*      */     {
/*  927 */       if ((this.thumbWidth > 255) || (this.thumbHeight > 255))
/*      */       {
/*  929 */         paramJPEGImageWriter.warningOccurred(12);
/*      */       }
/*  931 */       this.thumbWidth = Math.min(this.thumbWidth, 255);
/*  932 */       this.thumbHeight = Math.min(this.thumbHeight, 255);
/*  933 */       paramImageOutputStream.write(this.thumbWidth);
/*  934 */       paramImageOutputStream.write(this.thumbHeight);
/*      */     }
/*      */ 
/*      */     void writePixels(ImageOutputStream paramImageOutputStream, JPEGImageWriter paramJPEGImageWriter) throws IOException
/*      */     {
/*  939 */       if ((this.thumbWidth > 255) || (this.thumbHeight > 255))
/*      */       {
/*  941 */         paramJPEGImageWriter.warningOccurred(12);
/*      */       }
/*  943 */       this.thumbWidth = Math.min(this.thumbWidth, 255);
/*  944 */       this.thumbHeight = Math.min(this.thumbHeight, 255);
/*  945 */       int[] arrayOfInt = this.thumbnail.getRaster().getPixels(0, 0, this.thumbWidth, this.thumbHeight, (int[])null);
/*      */ 
/*  949 */       JFIFMarkerSegment.this.writeThumbnailData(paramImageOutputStream, arrayOfInt, paramJPEGImageWriter);
/*      */     }
/*      */ 
/*      */     void print() {
/*  953 */       System.out.print(this.name + " width: ");
/*  954 */       System.out.println(this.thumbWidth);
/*  955 */       System.out.print(this.name + " height: ");
/*  956 */       System.out.println(this.thumbHeight);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JFIFMarkerSegment
 * JD-Core Version:    0.6.2
 */
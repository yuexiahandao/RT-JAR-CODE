/*      */ package com.sun.imageio.plugins.jpeg;
/*      */ 
/*      */ import java.awt.Point;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.color.ICC_ColorSpace;
/*      */ import java.awt.color.ICC_Profile;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import javax.imageio.IIOException;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.ImageWriteParam;
/*      */ import javax.imageio.metadata.IIOInvalidTreeException;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.metadata.IIOMetadataNode;
/*      */ import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
/*      */ import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ import javax.imageio.stream.ImageOutputStream;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.NodeList;
/*      */ 
/*      */ public class JPEGMetadata extends IIOMetadata
/*      */   implements Cloneable
/*      */ {
/*      */   private static final boolean debug = false;
/*   72 */   private List resetSequence = null;
/*      */ 
/*   80 */   private boolean inThumb = false;
/*      */   private boolean hasAlpha;
/*  103 */   List markerSequence = new ArrayList();
/*      */   final boolean isStream;
/*      */   private boolean transparencyDone;
/*      */ 
/*      */   JPEGMetadata(boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  119 */     super(true, "javax_imageio_jpeg_image_1.0", "com.sun.imageio.plugins.jpeg.JPEGImageMetadataFormat", null, null);
/*      */ 
/*  123 */     this.inThumb = paramBoolean2;
/*      */ 
/*  125 */     this.isStream = paramBoolean1;
/*  126 */     if (paramBoolean1) {
/*  127 */       this.nativeMetadataFormatName = "javax_imageio_jpeg_stream_1.0";
/*  128 */       this.nativeMetadataFormatClassName = "com.sun.imageio.plugins.jpeg.JPEGStreamMetadataFormat";
/*      */     }
/*      */   }
/*      */ 
/*      */   JPEGMetadata(boolean paramBoolean1, boolean paramBoolean2, ImageInputStream paramImageInputStream, JPEGImageReader paramJPEGImageReader)
/*      */     throws IOException
/*      */   {
/*  151 */     this(paramBoolean1, paramBoolean2);
/*      */ 
/*  153 */     JPEGBuffer localJPEGBuffer = new JPEGBuffer(paramImageInputStream);
/*      */ 
/*  155 */     localJPEGBuffer.loadBuf(0);
/*      */ 
/*  158 */     if (((localJPEGBuffer.buf[0] & 0xFF) != 255) || ((localJPEGBuffer.buf[1] & 0xFF) != 216) || ((localJPEGBuffer.buf[2] & 0xFF) != 255))
/*      */     {
/*  161 */       throw new IIOException("Image format error");
/*      */     }
/*      */ 
/*  164 */     int i = 0;
/*  165 */     localJPEGBuffer.bufAvail -= 2;
/*  166 */     localJPEGBuffer.bufPtr = 2;
/*  167 */     Object localObject = null;
/*  168 */     while (i == 0)
/*      */     {
/*  171 */       localJPEGBuffer.loadBuf(1);
/*      */ 
/*  176 */       localJPEGBuffer.scanForFF(paramJPEGImageReader);
/*      */       JFIFMarkerSegment localJFIFMarkerSegment;
/*  177 */       switch (localJPEGBuffer.buf[localJPEGBuffer.bufPtr] & 0xFF)
/*      */       {
/*      */       case 0:
/*  182 */         localJPEGBuffer.bufAvail -= 1;
/*  183 */         localJPEGBuffer.bufPtr += 1;
/*  184 */         break;
/*      */       case 192:
/*      */       case 193:
/*      */       case 194:
/*  188 */         if (paramBoolean1) {
/*  189 */           throw new IIOException("SOF not permitted in stream metadata");
/*      */         }
/*      */ 
/*  192 */         localObject = new SOFMarkerSegment(localJPEGBuffer);
/*  193 */         break;
/*      */       case 219:
/*  195 */         localObject = new DQTMarkerSegment(localJPEGBuffer);
/*  196 */         break;
/*      */       case 196:
/*  198 */         localObject = new DHTMarkerSegment(localJPEGBuffer);
/*  199 */         break;
/*      */       case 221:
/*  201 */         localObject = new DRIMarkerSegment(localJPEGBuffer);
/*  202 */         break;
/*      */       case 224:
/*  205 */         localJPEGBuffer.loadBuf(8);
/*  206 */         byte[] arrayOfByte = localJPEGBuffer.buf;
/*  207 */         int j = localJPEGBuffer.bufPtr;
/*  208 */         if ((arrayOfByte[(j + 3)] == 74) && (arrayOfByte[(j + 4)] == 70) && (arrayOfByte[(j + 5)] == 73) && (arrayOfByte[(j + 6)] == 70) && (arrayOfByte[(j + 7)] == 0))
/*      */         {
/*  213 */           if (this.inThumb) {
/*  214 */             paramJPEGImageReader.warningOccurred(1);
/*      */ 
/*  218 */             localJFIFMarkerSegment = new JFIFMarkerSegment(localJPEGBuffer);
/*      */           } else {
/*  220 */             if (paramBoolean1) {
/*  221 */               throw new IIOException("JFIF not permitted in stream metadata");
/*      */             }
/*  223 */             if (!this.markerSequence.isEmpty()) {
/*  224 */               throw new IIOException("JFIF APP0 must be first marker after SOI");
/*      */             }
/*      */ 
/*  227 */             localObject = new JFIFMarkerSegment(localJPEGBuffer);
/*      */           }
/*  229 */         } else if ((arrayOfByte[(j + 3)] == 74) && (arrayOfByte[(j + 4)] == 70) && (arrayOfByte[(j + 5)] == 88) && (arrayOfByte[(j + 6)] == 88) && (arrayOfByte[(j + 7)] == 0))
/*      */         {
/*  234 */           if (paramBoolean1) {
/*  235 */             throw new IIOException("JFXX not permitted in stream metadata");
/*      */           }
/*      */ 
/*  238 */           if (this.inThumb) {
/*  239 */             throw new IIOException("JFXX markers not allowed in JFIF JPEG thumbnail");
/*      */           }
/*      */ 
/*  242 */           localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/*  245 */           if (localJFIFMarkerSegment == null) {
/*  246 */             throw new IIOException("JFXX encountered without prior JFIF!");
/*      */           }
/*      */ 
/*  249 */           localJFIFMarkerSegment.addJFXX(localJPEGBuffer, paramJPEGImageReader);
/*      */         }
/*      */         else {
/*  252 */           localObject = new MarkerSegment(localJPEGBuffer);
/*  253 */           ((MarkerSegment)localObject).loadData(localJPEGBuffer);
/*      */         }
/*  255 */         break;
/*      */       case 226:
/*  258 */         localJPEGBuffer.loadBuf(15);
/*  259 */         if ((localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 3)] == 73) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 4)] == 67) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 5)] == 67) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 6)] == 95) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 7)] == 80) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 8)] == 82) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 9)] == 79) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 10)] == 70) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 11)] == 73) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 12)] == 76) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 13)] == 69) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 14)] == 0))
/*      */         {
/*  272 */           if (paramBoolean1) {
/*  273 */             throw new IIOException("ICC profiles not permitted in stream metadata");
/*      */           }
/*      */ 
/*  277 */           localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/*  280 */           if (localJFIFMarkerSegment == null) {
/*  281 */             localObject = new MarkerSegment(localJPEGBuffer);
/*  282 */             ((MarkerSegment)localObject).loadData(localJPEGBuffer);
/*      */           } else {
/*  284 */             localJFIFMarkerSegment.addICC(localJPEGBuffer);
/*      */           }
/*      */         }
/*      */         else {
/*  288 */           localObject = new MarkerSegment(localJPEGBuffer);
/*  289 */           ((MarkerSegment)localObject).loadData(localJPEGBuffer);
/*      */         }
/*  291 */         break;
/*      */       case 238:
/*  294 */         localJPEGBuffer.loadBuf(8);
/*  295 */         if ((localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 3)] == 65) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 4)] == 100) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 5)] == 111) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 6)] == 98) && (localJPEGBuffer.buf[(localJPEGBuffer.bufPtr + 7)] == 101))
/*      */         {
/*  300 */           if (paramBoolean1) {
/*  301 */             throw new IIOException("Adobe APP14 markers not permitted in stream metadata");
/*      */           }
/*      */ 
/*  304 */           localObject = new AdobeMarkerSegment(localJPEGBuffer);
/*      */         } else {
/*  306 */           localObject = new MarkerSegment(localJPEGBuffer);
/*  307 */           ((MarkerSegment)localObject).loadData(localJPEGBuffer);
/*      */         }
/*      */ 
/*  310 */         break;
/*      */       case 254:
/*  312 */         localObject = new COMMarkerSegment(localJPEGBuffer);
/*  313 */         break;
/*      */       case 218:
/*  315 */         if (paramBoolean1) {
/*  316 */           throw new IIOException("SOS not permitted in stream metadata");
/*      */         }
/*      */ 
/*  319 */         localObject = new SOSMarkerSegment(localJPEGBuffer);
/*  320 */         break;
/*      */       case 208:
/*      */       case 209:
/*      */       case 210:
/*      */       case 211:
/*      */       case 212:
/*      */       case 213:
/*      */       case 214:
/*      */       case 215:
/*  332 */         localJPEGBuffer.bufPtr += 1;
/*  333 */         localJPEGBuffer.bufAvail -= 1;
/*  334 */         break;
/*      */       case 217:
/*  336 */         i = 1;
/*  337 */         localJPEGBuffer.bufPtr += 1;
/*  338 */         localJPEGBuffer.bufAvail -= 1;
/*  339 */         break;
/*      */       default:
/*  341 */         localObject = new MarkerSegment(localJPEGBuffer);
/*  342 */         ((MarkerSegment)localObject).loadData(localJPEGBuffer);
/*  343 */         ((MarkerSegment)localObject).unknown = true;
/*      */       }
/*      */ 
/*  346 */       if (localObject != null) {
/*  347 */         this.markerSequence.add(localObject);
/*      */ 
/*  351 */         localObject = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  359 */     localJPEGBuffer.pushBack();
/*      */ 
/*  361 */     if (!isConsistent())
/*  362 */       throw new IIOException("Inconsistent metadata read from stream");
/*      */   }
/*      */ 
/*      */   JPEGMetadata(ImageWriteParam paramImageWriteParam, JPEGImageWriter paramJPEGImageWriter)
/*      */   {
/*  371 */     this(true, false);
/*      */ 
/*  373 */     JPEGImageWriteParam localJPEGImageWriteParam = null;
/*      */ 
/*  375 */     if ((paramImageWriteParam != null) && ((paramImageWriteParam instanceof JPEGImageWriteParam))) {
/*  376 */       localJPEGImageWriteParam = (JPEGImageWriteParam)paramImageWriteParam;
/*  377 */       if (!localJPEGImageWriteParam.areTablesSet()) {
/*  378 */         localJPEGImageWriteParam = null;
/*      */       }
/*      */     }
/*  381 */     if (localJPEGImageWriteParam != null) {
/*  382 */       this.markerSequence.add(new DQTMarkerSegment(localJPEGImageWriteParam.getQTables()));
/*  383 */       this.markerSequence.add(new DHTMarkerSegment(localJPEGImageWriteParam.getDCHuffmanTables(), localJPEGImageWriteParam.getACHuffmanTables()));
/*      */     }
/*      */     else
/*      */     {
/*  388 */       this.markerSequence.add(new DQTMarkerSegment(JPEG.getDefaultQTables()));
/*  389 */       this.markerSequence.add(new DHTMarkerSegment(JPEG.getDefaultHuffmanTables(true), JPEG.getDefaultHuffmanTables(false)));
/*      */     }
/*      */ 
/*  394 */     if (!isConsistent())
/*  395 */       throw new InternalError("Default stream metadata is inconsistent");
/*      */   }
/*      */ 
/*      */   JPEGMetadata(ImageTypeSpecifier paramImageTypeSpecifier, ImageWriteParam paramImageWriteParam, JPEGImageWriter paramJPEGImageWriter)
/*      */   {
/*  406 */     this(false, false);
/*      */ 
/*  408 */     int i = 1;
/*  409 */     int j = 0;
/*  410 */     int k = 0;
/*  411 */     boolean bool1 = true;
/*  412 */     int m = 0;
/*  413 */     boolean bool2 = false;
/*  414 */     boolean bool3 = false;
/*  415 */     boolean bool4 = false;
/*  416 */     int n = 1;
/*  417 */     int i1 = 1;
/*  418 */     float f = 0.75F;
/*  419 */     byte[] arrayOfByte = { 1, 2, 3, 4 };
/*  420 */     int i2 = 0;
/*      */ 
/*  422 */     ImageTypeSpecifier localImageTypeSpecifier = null;
/*      */ 
/*  424 */     if (paramImageWriteParam != null) {
/*  425 */       localImageTypeSpecifier = paramImageWriteParam.getDestinationType();
/*  426 */       if ((localImageTypeSpecifier != null) && 
/*  427 */         (paramImageTypeSpecifier != null))
/*      */       {
/*  429 */         paramJPEGImageWriter.warningOccurred(0);
/*      */ 
/*  431 */         localImageTypeSpecifier = null;
/*      */       }
/*      */ 
/*  435 */       if (paramImageWriteParam.canWriteProgressive())
/*      */       {
/*  438 */         if (paramImageWriteParam.getProgressiveMode() == 1) {
/*  439 */           bool2 = true;
/*  440 */           bool3 = true;
/*  441 */           i1 = 0;
/*      */         }
/*      */       }
/*      */ 
/*  445 */       if ((paramImageWriteParam instanceof JPEGImageWriteParam)) {
/*  446 */         localObject1 = (JPEGImageWriteParam)paramImageWriteParam;
/*  447 */         if (((JPEGImageWriteParam)localObject1).areTablesSet()) {
/*  448 */           n = 0;
/*  449 */           i1 = 0;
/*  450 */           if ((((JPEGImageWriteParam)localObject1).getDCHuffmanTables().length > 2) || (((JPEGImageWriteParam)localObject1).getACHuffmanTables().length > 2))
/*      */           {
/*  452 */             bool4 = true;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  457 */         if (!bool2) {
/*  458 */           bool3 = ((JPEGImageWriteParam)localObject1).getOptimizeHuffmanTables();
/*  459 */           if (bool3) {
/*  460 */             i1 = 0;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  469 */       if ((paramImageWriteParam.canWriteCompressed()) && 
/*  470 */         (paramImageWriteParam.getCompressionMode() == 2)) {
/*  471 */         f = paramImageWriteParam.getCompressionQuality();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  478 */     Object localObject1 = null;
/*      */     Object localObject2;
/*      */     int i3;
/*      */     boolean bool5;
/*      */     int i4;
/*  479 */     if (localImageTypeSpecifier != null) {
/*  480 */       localObject2 = localImageTypeSpecifier.getColorModel();
/*  481 */       i2 = ((ColorModel)localObject2).getNumComponents();
/*  482 */       i3 = ((ColorModel)localObject2).getNumColorComponents() != i2 ? 1 : 0;
/*  483 */       bool5 = ((ColorModel)localObject2).hasAlpha();
/*  484 */       localObject1 = ((ColorModel)localObject2).getColorSpace();
/*  485 */       i4 = ((ColorSpace)localObject1).getType();
/*  486 */       switch (i4) {
/*      */       case 6:
/*  488 */         bool1 = false;
/*  489 */         if (i3 != 0)
/*  490 */           i = 0; break;
/*      */       case 13:
/*  494 */         if (localObject1 == JPEG.JCS.getYCC()) {
/*  495 */           i = 0;
/*  496 */           arrayOfByte[0] = 89;
/*  497 */           arrayOfByte[1] = 67;
/*  498 */           arrayOfByte[2] = 99;
/*  499 */           if (bool5)
/*  500 */             arrayOfByte[3] = 65;  } break;
/*      */       case 3:
/*  505 */         if (i3 != 0) {
/*  506 */           i = 0;
/*  507 */           if (!bool5) {
/*  508 */             j = 1;
/*  509 */             k = 2; }  } break;
/*      */       case 5:
/*  514 */         i = 0;
/*  515 */         j = 1;
/*  516 */         bool1 = false;
/*  517 */         arrayOfByte[0] = 82;
/*  518 */         arrayOfByte[1] = 71;
/*  519 */         arrayOfByte[2] = 66;
/*  520 */         if (bool5)
/*  521 */           arrayOfByte[3] = 65; break;
/*      */       default:
/*  527 */         i = 0;
/*  528 */         bool1 = false;
/*      */       }
/*  530 */     } else if (paramImageTypeSpecifier != null) {
/*  531 */       localObject2 = paramImageTypeSpecifier.getColorModel();
/*  532 */       i2 = ((ColorModel)localObject2).getNumComponents();
/*  533 */       i3 = ((ColorModel)localObject2).getNumColorComponents() != i2 ? 1 : 0;
/*  534 */       bool5 = ((ColorModel)localObject2).hasAlpha();
/*  535 */       localObject1 = ((ColorModel)localObject2).getColorSpace();
/*  536 */       i4 = ((ColorSpace)localObject1).getType();
/*  537 */       switch (i4) {
/*      */       case 6:
/*  539 */         bool1 = false;
/*  540 */         if (i3 != 0)
/*  541 */           i = 0; break;
/*      */       case 5:
/*  546 */         if (bool5)
/*  547 */           i = 0; break;
/*      */       case 13:
/*  551 */         i = 0;
/*  552 */         bool1 = false;
/*  553 */         if (localObject1.equals(ColorSpace.getInstance(1002))) {
/*  554 */           bool1 = true;
/*  555 */           j = 1;
/*  556 */           arrayOfByte[0] = 89;
/*  557 */           arrayOfByte[1] = 67;
/*  558 */           arrayOfByte[2] = 99;
/*  559 */           if (bool5)
/*  560 */             arrayOfByte[3] = 65;  } break;
/*      */       case 3:
/*  565 */         if (i3 != 0) {
/*  566 */           i = 0;
/*  567 */           if (!bool5) {
/*  568 */             j = 1;
/*  569 */             k = 2; }  } break;
/*      */       case 9:
/*  574 */         i = 0;
/*  575 */         j = 1;
/*  576 */         k = 2;
/*  577 */         break;
/*      */       case 4:
/*      */       case 7:
/*      */       case 8:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       default:
/*  582 */         i = 0;
/*  583 */         bool1 = false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  589 */     if ((i != 0) && (JPEG.isNonStandardICC((ColorSpace)localObject1))) {
/*  590 */       m = 1;
/*      */     }
/*      */ 
/*  594 */     if (i != 0) {
/*  595 */       localObject2 = new JFIFMarkerSegment();
/*  596 */       this.markerSequence.add(localObject2);
/*  597 */       if (m != 0)
/*      */         try {
/*  599 */           ((JFIFMarkerSegment)localObject2).addICC((ICC_ColorSpace)localObject1);
/*      */         }
/*      */         catch (IOException localIOException) {
/*      */         }
/*      */     }
/*  604 */     if (j != 0) {
/*  605 */       this.markerSequence.add(new AdobeMarkerSegment(k));
/*      */     }
/*      */ 
/*  609 */     if (n != 0) {
/*  610 */       this.markerSequence.add(new DQTMarkerSegment(f, bool1));
/*      */     }
/*      */ 
/*  614 */     if (i1 != 0) {
/*  615 */       this.markerSequence.add(new DHTMarkerSegment(bool1));
/*      */     }
/*      */ 
/*  619 */     this.markerSequence.add(new SOFMarkerSegment(bool2, bool4, bool1, arrayOfByte, i2));
/*      */ 
/*  626 */     if (!bool2) {
/*  627 */       this.markerSequence.add(new SOSMarkerSegment(bool1, arrayOfByte, i2));
/*      */     }
/*      */ 
/*  633 */     if (!isConsistent())
/*  634 */       throw new InternalError("Default image metadata is inconsistent");
/*      */   }
/*      */ 
/*      */   MarkerSegment findMarkerSegment(int paramInt)
/*      */   {
/*  648 */     Iterator localIterator = this.markerSequence.iterator();
/*  649 */     while (localIterator.hasNext()) {
/*  650 */       MarkerSegment localMarkerSegment = (MarkerSegment)localIterator.next();
/*  651 */       if (localMarkerSegment.tag == paramInt) {
/*  652 */         return localMarkerSegment;
/*      */       }
/*      */     }
/*  655 */     return null;
/*      */   }
/*      */ 
/*      */   MarkerSegment findMarkerSegment(Class paramClass, boolean paramBoolean)
/*      */   {
/*      */     Object localObject;
/*      */     MarkerSegment localMarkerSegment;
/*  663 */     if (paramBoolean) {
/*  664 */       localObject = this.markerSequence.iterator();
/*  665 */       while (((Iterator)localObject).hasNext()) {
/*  666 */         localMarkerSegment = (MarkerSegment)((Iterator)localObject).next();
/*  667 */         if (paramClass.isInstance(localMarkerSegment))
/*  668 */           return localMarkerSegment;
/*      */       }
/*      */     }
/*      */     else {
/*  672 */       localObject = this.markerSequence.listIterator(this.markerSequence.size());
/*  673 */       while (((ListIterator)localObject).hasPrevious()) {
/*  674 */         localMarkerSegment = (MarkerSegment)((ListIterator)localObject).previous();
/*  675 */         if (paramClass.isInstance(localMarkerSegment)) {
/*  676 */           return localMarkerSegment;
/*      */         }
/*      */       }
/*      */     }
/*  680 */     return null;
/*      */   }
/*      */ 
/*      */   private int findMarkerSegmentPosition(Class paramClass, boolean paramBoolean)
/*      */   {
/*      */     ListIterator localListIterator;
/*      */     int i;
/*      */     MarkerSegment localMarkerSegment;
/*  688 */     if (paramBoolean) {
/*  689 */       localListIterator = this.markerSequence.listIterator();
/*  690 */       for (i = 0; localListIterator.hasNext(); i++) {
/*  691 */         localMarkerSegment = (MarkerSegment)localListIterator.next();
/*  692 */         if (paramClass.isInstance(localMarkerSegment))
/*  693 */           return i;
/*      */       }
/*      */     }
/*      */     else {
/*  697 */       localListIterator = this.markerSequence.listIterator(this.markerSequence.size());
/*  698 */       for (i = this.markerSequence.size() - 1; localListIterator.hasPrevious(); i--) {
/*  699 */         localMarkerSegment = (MarkerSegment)localListIterator.previous();
/*  700 */         if (paramClass.isInstance(localMarkerSegment)) {
/*  701 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*  705 */     return -1;
/*      */   }
/*      */ 
/*      */   private int findLastUnknownMarkerSegmentPosition() {
/*  709 */     ListIterator localListIterator = this.markerSequence.listIterator(this.markerSequence.size());
/*  710 */     for (int i = this.markerSequence.size() - 1; localListIterator.hasPrevious(); i--) {
/*  711 */       MarkerSegment localMarkerSegment = (MarkerSegment)localListIterator.previous();
/*  712 */       if (localMarkerSegment.unknown == true) {
/*  713 */         return i;
/*      */       }
/*      */     }
/*  716 */     return -1;
/*      */   }
/*      */ 
/*      */   protected Object clone()
/*      */   {
/*  722 */     JPEGMetadata localJPEGMetadata = null;
/*      */     try {
/*  724 */       localJPEGMetadata = (JPEGMetadata)super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  726 */     if (this.markerSequence != null) {
/*  727 */       localJPEGMetadata.markerSequence = cloneSequence();
/*      */     }
/*  729 */     localJPEGMetadata.resetSequence = null;
/*  730 */     return localJPEGMetadata;
/*      */   }
/*      */ 
/*      */   private List cloneSequence()
/*      */   {
/*  737 */     if (this.markerSequence == null) {
/*  738 */       return null;
/*      */     }
/*  740 */     ArrayList localArrayList = new ArrayList(this.markerSequence.size());
/*  741 */     Iterator localIterator = this.markerSequence.iterator();
/*  742 */     while (localIterator.hasNext()) {
/*  743 */       MarkerSegment localMarkerSegment = (MarkerSegment)localIterator.next();
/*  744 */       localArrayList.add(localMarkerSegment.clone());
/*      */     }
/*      */ 
/*  747 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public Node getAsTree(String paramString)
/*      */   {
/*  754 */     if (paramString == null) {
/*  755 */       throw new IllegalArgumentException("null formatName!");
/*      */     }
/*  757 */     if (this.isStream) {
/*  758 */       if (paramString.equals("javax_imageio_jpeg_stream_1.0"))
/*  759 */         return getNativeTree();
/*      */     }
/*      */     else {
/*  762 */       if (paramString.equals("javax_imageio_jpeg_image_1.0")) {
/*  763 */         return getNativeTree();
/*      */       }
/*  765 */       if (paramString.equals("javax_imageio_1.0"))
/*      */       {
/*  767 */         return getStandardTree();
/*      */       }
/*      */     }
/*  770 */     throw new IllegalArgumentException("Unsupported format name: " + paramString);
/*      */   }
/*      */ 
/*      */   IIOMetadataNode getNativeTree()
/*      */   {
/*  777 */     Iterator localIterator = this.markerSequence.iterator();
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     Object localObject3;
/*  778 */     if (this.isStream) {
/*  779 */       localObject1 = new IIOMetadataNode("javax_imageio_jpeg_stream_1.0");
/*  780 */       localObject2 = localObject1;
/*      */     } else {
/*  782 */       localObject3 = new IIOMetadataNode("markerSequence");
/*  783 */       if (!this.inThumb) {
/*  784 */         localObject1 = new IIOMetadataNode("javax_imageio_jpeg_image_1.0");
/*  785 */         IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("JPEGvariety");
/*  786 */         ((IIOMetadataNode)localObject1).appendChild(localIIOMetadataNode);
/*  787 */         JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/*  789 */         if (localJFIFMarkerSegment != null) {
/*  790 */           localIterator.next();
/*  791 */           localIIOMetadataNode.appendChild(localJFIFMarkerSegment.getNativeNode());
/*      */         }
/*  793 */         ((IIOMetadataNode)localObject1).appendChild((Node)localObject3);
/*      */       } else {
/*  795 */         localObject1 = localObject3;
/*      */       }
/*  797 */       localObject2 = localObject3;
/*      */     }
/*  799 */     while (localIterator.hasNext()) {
/*  800 */       localObject3 = (MarkerSegment)localIterator.next();
/*  801 */       localObject2.appendChild(((MarkerSegment)localObject3).getNativeNode());
/*      */     }
/*  803 */     return localObject1;
/*      */   }
/*      */ 
/*      */   protected IIOMetadataNode getStandardChromaNode()
/*      */   {
/*  809 */     this.hasAlpha = false;
/*      */ 
/*  813 */     SOFMarkerSegment localSOFMarkerSegment = (SOFMarkerSegment)findMarkerSegment(SOFMarkerSegment.class, true);
/*      */ 
/*  815 */     if (localSOFMarkerSegment == null)
/*      */     {
/*  817 */       return null;
/*      */     }
/*      */ 
/*  820 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Chroma");
/*  821 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("ColorSpaceType");
/*  822 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  825 */     int i = localSOFMarkerSegment.componentSpecs.length;
/*      */ 
/*  827 */     IIOMetadataNode localIIOMetadataNode3 = new IIOMetadataNode("NumChannels");
/*  828 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode3);
/*  829 */     localIIOMetadataNode3.setAttribute("value", Integer.toString(i));
/*      */ 
/*  832 */     if (findMarkerSegment(JFIFMarkerSegment.class, true) != null) {
/*  833 */       if (i == 1)
/*  834 */         localIIOMetadataNode2.setAttribute("name", "GRAY");
/*      */       else {
/*  836 */         localIIOMetadataNode2.setAttribute("name", "YCbCr");
/*      */       }
/*  838 */       return localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  842 */     AdobeMarkerSegment localAdobeMarkerSegment = (AdobeMarkerSegment)findMarkerSegment(AdobeMarkerSegment.class, true);
/*      */ 
/*  844 */     if (localAdobeMarkerSegment != null) {
/*  845 */       switch (localAdobeMarkerSegment.transform) {
/*      */       case 2:
/*  847 */         localIIOMetadataNode2.setAttribute("name", "YCCK");
/*  848 */         break;
/*      */       case 1:
/*  850 */         localIIOMetadataNode2.setAttribute("name", "YCbCr");
/*  851 */         break;
/*      */       case 0:
/*  853 */         if (i == 3)
/*  854 */           localIIOMetadataNode2.setAttribute("name", "RGB");
/*  855 */         else if (i == 4) {
/*  856 */           localIIOMetadataNode2.setAttribute("name", "CMYK");
/*      */         }
/*      */         break;
/*      */       }
/*  860 */       return localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  864 */     if (i < 3) {
/*  865 */       localIIOMetadataNode2.setAttribute("name", "GRAY");
/*  866 */       if (i == 2) {
/*  867 */         this.hasAlpha = true;
/*      */       }
/*  869 */       return localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  872 */     int j = 1;
/*      */ 
/*  874 */     for (int k = 0; k < localSOFMarkerSegment.componentSpecs.length; k++) {
/*  875 */       m = localSOFMarkerSegment.componentSpecs[k].componentId;
/*  876 */       if ((m < 1) || (m >= localSOFMarkerSegment.componentSpecs.length)) {
/*  877 */         j = 0;
/*      */       }
/*      */     }
/*      */ 
/*  881 */     if (j != 0) {
/*  882 */       localIIOMetadataNode2.setAttribute("name", "YCbCr");
/*  883 */       if (i == 4) {
/*  884 */         this.hasAlpha = true;
/*      */       }
/*  886 */       return localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  890 */     if ((localSOFMarkerSegment.componentSpecs[0].componentId == 82) && (localSOFMarkerSegment.componentSpecs[1].componentId == 71) && (localSOFMarkerSegment.componentSpecs[2].componentId == 66))
/*      */     {
/*  894 */       localIIOMetadataNode2.setAttribute("name", "RGB");
/*  895 */       if ((i == 4) && (localSOFMarkerSegment.componentSpecs[3].componentId == 65))
/*      */       {
/*  897 */         this.hasAlpha = true;
/*      */       }
/*  899 */       return localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  902 */     if ((localSOFMarkerSegment.componentSpecs[0].componentId == 89) && (localSOFMarkerSegment.componentSpecs[1].componentId == 67) && (localSOFMarkerSegment.componentSpecs[2].componentId == 99))
/*      */     {
/*  906 */       localIIOMetadataNode2.setAttribute("name", "PhotoYCC");
/*  907 */       if ((i == 4) && (localSOFMarkerSegment.componentSpecs[3].componentId == 65))
/*      */       {
/*  909 */         this.hasAlpha = true;
/*      */       }
/*  911 */       return localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  917 */     k = 0;
/*      */ 
/*  919 */     int m = localSOFMarkerSegment.componentSpecs[0].HsamplingFactor;
/*  920 */     int n = localSOFMarkerSegment.componentSpecs[0].VsamplingFactor;
/*      */ 
/*  922 */     for (int i1 = 1; i1 < localSOFMarkerSegment.componentSpecs.length; i1++) {
/*  923 */       if ((localSOFMarkerSegment.componentSpecs[i1].HsamplingFactor != m) || (localSOFMarkerSegment.componentSpecs[i1].VsamplingFactor != n))
/*      */       {
/*  925 */         k = 1;
/*  926 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  930 */     if (k != 0) {
/*  931 */       localIIOMetadataNode2.setAttribute("name", "YCbCr");
/*  932 */       if (i == 4) {
/*  933 */         this.hasAlpha = true;
/*      */       }
/*  935 */       return localIIOMetadataNode1;
/*      */     }
/*      */ 
/*  939 */     if (i == 3)
/*  940 */       localIIOMetadataNode2.setAttribute("name", "RGB");
/*      */     else {
/*  942 */       localIIOMetadataNode2.setAttribute("name", "CMYK");
/*      */     }
/*      */ 
/*  945 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   protected IIOMetadataNode getStandardCompressionNode()
/*      */   {
/*  950 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Compression");
/*      */ 
/*  953 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
/*  954 */     localIIOMetadataNode2.setAttribute("value", "JPEG");
/*  955 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  958 */     IIOMetadataNode localIIOMetadataNode3 = new IIOMetadataNode("Lossless");
/*  959 */     localIIOMetadataNode3.setAttribute("value", "FALSE");
/*  960 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode3);
/*      */ 
/*  963 */     int i = 0;
/*  964 */     Iterator localIterator = this.markerSequence.iterator();
/*      */     Object localObject;
/*  965 */     while (localIterator.hasNext()) {
/*  966 */       localObject = (MarkerSegment)localIterator.next();
/*  967 */       if (((MarkerSegment)localObject).tag == 218) {
/*  968 */         i++;
/*      */       }
/*      */     }
/*  971 */     if (i != 0) {
/*  972 */       localObject = new IIOMetadataNode("NumProgressiveScans");
/*  973 */       ((IIOMetadataNode)localObject).setAttribute("value", Integer.toString(i));
/*  974 */       localIIOMetadataNode1.appendChild((Node)localObject);
/*      */     }
/*      */ 
/*  977 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   protected IIOMetadataNode getStandardDimensionNode()
/*      */   {
/*  983 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Dimension");
/*  984 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("ImageOrientation");
/*  985 */     localIIOMetadataNode2.setAttribute("value", "normal");
/*  986 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  988 */     JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/*  990 */     if (localJFIFMarkerSegment != null)
/*      */     {
/*      */       float f1;
/*  994 */       if (localJFIFMarkerSegment.resUnits == 0)
/*      */       {
/*  996 */         f1 = localJFIFMarkerSegment.Xdensity / localJFIFMarkerSegment.Ydensity;
/*      */       }
/*      */       else {
/*  999 */         f1 = localJFIFMarkerSegment.Ydensity / localJFIFMarkerSegment.Xdensity;
/*      */       }
/* 1001 */       IIOMetadataNode localIIOMetadataNode3 = new IIOMetadataNode("PixelAspectRatio");
/* 1002 */       localIIOMetadataNode3.setAttribute("value", Float.toString(f1));
/* 1003 */       localIIOMetadataNode1.insertBefore(localIIOMetadataNode3, localIIOMetadataNode2);
/*      */ 
/* 1006 */       if (localJFIFMarkerSegment.resUnits != 0)
/*      */       {
/* 1008 */         float f2 = localJFIFMarkerSegment.resUnits == 1 ? 25.4F : 10.0F;
/*      */ 
/* 1010 */         IIOMetadataNode localIIOMetadataNode4 = new IIOMetadataNode("HorizontalPixelSize");
/*      */ 
/* 1012 */         localIIOMetadataNode4.setAttribute("value", Float.toString(f2 / localJFIFMarkerSegment.Xdensity));
/*      */ 
/* 1014 */         localIIOMetadataNode1.appendChild(localIIOMetadataNode4);
/*      */ 
/* 1016 */         IIOMetadataNode localIIOMetadataNode5 = new IIOMetadataNode("VerticalPixelSize");
/*      */ 
/* 1018 */         localIIOMetadataNode5.setAttribute("value", Float.toString(f2 / localJFIFMarkerSegment.Ydensity));
/*      */ 
/* 1020 */         localIIOMetadataNode1.appendChild(localIIOMetadataNode5);
/*      */       }
/*      */     }
/* 1023 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   protected IIOMetadataNode getStandardTextNode() {
/* 1027 */     IIOMetadataNode localIIOMetadataNode1 = null;
/*      */ 
/* 1029 */     if (findMarkerSegment(254) != null) {
/* 1030 */       localIIOMetadataNode1 = new IIOMetadataNode("Text");
/* 1031 */       Iterator localIterator = this.markerSequence.iterator();
/* 1032 */       while (localIterator.hasNext()) {
/* 1033 */         MarkerSegment localMarkerSegment = (MarkerSegment)localIterator.next();
/* 1034 */         if (localMarkerSegment.tag == 254) {
/* 1035 */           COMMarkerSegment localCOMMarkerSegment = (COMMarkerSegment)localMarkerSegment;
/* 1036 */           IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("TextEntry");
/* 1037 */           localIIOMetadataNode2.setAttribute("keyword", "comment");
/* 1038 */           localIIOMetadataNode2.setAttribute("value", localCOMMarkerSegment.getComment());
/* 1039 */           localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */         }
/*      */       }
/*      */     }
/* 1043 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   protected IIOMetadataNode getStandardTransparencyNode() {
/* 1047 */     IIOMetadataNode localIIOMetadataNode1 = null;
/* 1048 */     if (this.hasAlpha == true) {
/* 1049 */       localIIOMetadataNode1 = new IIOMetadataNode("Transparency");
/* 1050 */       IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("Alpha");
/* 1051 */       localIIOMetadataNode2.setAttribute("value", "nonpremultiplied");
/* 1052 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/* 1054 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly()
/*      */   {
/* 1060 */     return false;
/*      */   }
/*      */ 
/*      */   public void mergeTree(String paramString, Node paramNode) throws IIOInvalidTreeException
/*      */   {
/* 1065 */     if (paramString == null) {
/* 1066 */       throw new IllegalArgumentException("null formatName!");
/*      */     }
/* 1068 */     if (paramNode == null) {
/* 1069 */       throw new IllegalArgumentException("null root!");
/*      */     }
/* 1071 */     List localList = null;
/* 1072 */     if (this.resetSequence == null) {
/* 1073 */       this.resetSequence = cloneSequence();
/* 1074 */       localList = this.resetSequence;
/*      */     } else {
/* 1076 */       localList = cloneSequence();
/*      */     }
/* 1078 */     if ((this.isStream) && (paramString.equals("javax_imageio_jpeg_stream_1.0")))
/*      */     {
/* 1080 */       mergeNativeTree(paramNode);
/* 1081 */     } else if ((!this.isStream) && (paramString.equals("javax_imageio_jpeg_image_1.0")))
/*      */     {
/* 1083 */       mergeNativeTree(paramNode);
/* 1084 */     } else if ((!this.isStream) && (paramString.equals("javax_imageio_1.0")))
/*      */     {
/* 1087 */       mergeStandardTree(paramNode);
/*      */     }
/* 1089 */     else throw new IllegalArgumentException("Unsupported format name: " + paramString);
/*      */ 
/* 1092 */     if (!isConsistent()) {
/* 1093 */       this.markerSequence = localList;
/* 1094 */       throw new IIOInvalidTreeException("Merged tree is invalid; original restored", paramNode);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeNativeTree(Node paramNode) throws IIOInvalidTreeException
/*      */   {
/* 1100 */     String str = paramNode.getNodeName();
/* 1101 */     if (str != (this.isStream ? "javax_imageio_jpeg_stream_1.0" : "javax_imageio_jpeg_image_1.0"))
/*      */     {
/* 1103 */       throw new IIOInvalidTreeException("Invalid root node name: " + str, paramNode);
/*      */     }
/*      */ 
/* 1106 */     if (paramNode.getChildNodes().getLength() != 2) {
/* 1107 */       throw new IIOInvalidTreeException("JPEGvariety and markerSequence nodes must be present", paramNode);
/*      */     }
/*      */ 
/* 1110 */     mergeJFIFsubtree(paramNode.getFirstChild());
/* 1111 */     mergeSequenceSubtree(paramNode.getLastChild());
/*      */   }
/*      */ 
/*      */   private void mergeJFIFsubtree(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1123 */     if (paramNode.getChildNodes().getLength() != 0) {
/* 1124 */       Node localNode = paramNode.getFirstChild();
/*      */ 
/* 1126 */       JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 1128 */       if (localJFIFMarkerSegment != null) {
/* 1129 */         localJFIFMarkerSegment.updateFromNativeNode(localNode, false);
/*      */       }
/*      */       else
/* 1132 */         this.markerSequence.add(0, new JFIFMarkerSegment(localNode));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeSequenceSubtree(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1139 */     NodeList localNodeList = paramNode.getChildNodes();
/* 1140 */     for (int i = 0; i < localNodeList.getLength(); i++) {
/* 1141 */       Node localNode = localNodeList.item(i);
/* 1142 */       String str = localNode.getNodeName();
/* 1143 */       if (str.equals("dqt"))
/* 1144 */         mergeDQTNode(localNode);
/* 1145 */       else if (str.equals("dht"))
/* 1146 */         mergeDHTNode(localNode);
/* 1147 */       else if (str.equals("dri"))
/* 1148 */         mergeDRINode(localNode);
/* 1149 */       else if (str.equals("com"))
/* 1150 */         mergeCOMNode(localNode);
/* 1151 */       else if (str.equals("app14Adobe"))
/* 1152 */         mergeAdobeNode(localNode);
/* 1153 */       else if (str.equals("unknown"))
/* 1154 */         mergeUnknownNode(localNode);
/* 1155 */       else if (str.equals("sof"))
/* 1156 */         mergeSOFNode(localNode);
/* 1157 */       else if (str.equals("sos"))
/* 1158 */         mergeSOSNode(localNode);
/*      */       else
/* 1160 */         throw new IIOInvalidTreeException("Invalid node: " + str, localNode);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeDQTNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1184 */     ArrayList localArrayList = new ArrayList();
/* 1185 */     Iterator localIterator = this.markerSequence.iterator();
/*      */     Object localObject1;
/* 1186 */     while (localIterator.hasNext()) {
/* 1187 */       localObject1 = (MarkerSegment)localIterator.next();
/* 1188 */       if ((localObject1 instanceof DQTMarkerSegment))
/* 1189 */         localArrayList.add(localObject1);
/*      */     }
/*      */     int i;
/*      */     int k;
/* 1192 */     if (!localArrayList.isEmpty()) {
/* 1193 */       localObject1 = paramNode.getChildNodes();
/* 1194 */       for (i = 0; i < ((NodeList)localObject1).getLength(); i++) {
/* 1195 */         Node localNode = ((NodeList)localObject1).item(i);
/* 1196 */         k = MarkerSegment.getAttributeValue(localNode, null, "qtableId", 0, 3, true);
/*      */ 
/* 1201 */         Object localObject2 = null;
/* 1202 */         int m = -1;
/* 1203 */         for (int n = 0; n < localArrayList.size(); n++) {
/* 1204 */           DQTMarkerSegment localDQTMarkerSegment = (DQTMarkerSegment)localArrayList.get(n);
/* 1205 */           for (int i1 = 0; i1 < localDQTMarkerSegment.tables.size(); i1++) {
/* 1206 */             DQTMarkerSegment.Qtable localQtable = (DQTMarkerSegment.Qtable)localDQTMarkerSegment.tables.get(i1);
/*      */ 
/* 1208 */             if (k == localQtable.tableID) {
/* 1209 */               localObject2 = localDQTMarkerSegment;
/* 1210 */               m = i1;
/* 1211 */               break;
/*      */             }
/*      */           }
/* 1214 */           if (localObject2 != null) break;
/*      */         }
/* 1216 */         if (localObject2 != null) {
/* 1217 */           ((DQTMarkerSegment)localObject2).tables.set(m, ((DQTMarkerSegment)localObject2).getQtableFromNode(localNode));
/*      */         } else {
/* 1219 */           localObject2 = (DQTMarkerSegment)localArrayList.get(localArrayList.size() - 1);
/* 1220 */           ((DQTMarkerSegment)localObject2).tables.add(((DQTMarkerSegment)localObject2).getQtableFromNode(localNode));
/*      */         }
/*      */       }
/*      */     } else {
/* 1224 */       localObject1 = new DQTMarkerSegment(paramNode);
/* 1225 */       i = findMarkerSegmentPosition(DHTMarkerSegment.class, true);
/* 1226 */       int j = findMarkerSegmentPosition(SOFMarkerSegment.class, true);
/* 1227 */       k = findMarkerSegmentPosition(SOSMarkerSegment.class, true);
/* 1228 */       if (i != -1)
/* 1229 */         this.markerSequence.add(i, localObject1);
/* 1230 */       else if (j != -1)
/* 1231 */         this.markerSequence.add(j, localObject1);
/* 1232 */       else if (k != -1)
/* 1233 */         this.markerSequence.add(k, localObject1);
/*      */       else
/* 1235 */         this.markerSequence.add(localObject1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeDHTNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1260 */     ArrayList localArrayList = new ArrayList();
/* 1261 */     Iterator localIterator = this.markerSequence.iterator();
/*      */     Object localObject1;
/* 1262 */     while (localIterator.hasNext()) {
/* 1263 */       localObject1 = (MarkerSegment)localIterator.next();
/* 1264 */       if ((localObject1 instanceof DHTMarkerSegment))
/* 1265 */         localArrayList.add(localObject1);
/*      */     }
/*      */     int i;
/* 1268 */     if (!localArrayList.isEmpty()) {
/* 1269 */       localObject1 = paramNode.getChildNodes();
/* 1270 */       for (i = 0; i < ((NodeList)localObject1).getLength(); i++) {
/* 1271 */         Node localNode = ((NodeList)localObject1).item(i);
/* 1272 */         NamedNodeMap localNamedNodeMap = localNode.getAttributes();
/* 1273 */         int m = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "htableId", 0, 3, true);
/*      */ 
/* 1278 */         int n = MarkerSegment.getAttributeValue(localNode, localNamedNodeMap, "class", 0, 1, true);
/*      */ 
/* 1283 */         Object localObject2 = null;
/* 1284 */         int i1 = -1;
/* 1285 */         for (int i2 = 0; i2 < localArrayList.size(); i2++) {
/* 1286 */           DHTMarkerSegment localDHTMarkerSegment = (DHTMarkerSegment)localArrayList.get(i2);
/* 1287 */           for (int i3 = 0; i3 < localDHTMarkerSegment.tables.size(); i3++) {
/* 1288 */             DHTMarkerSegment.Htable localHtable = (DHTMarkerSegment.Htable)localDHTMarkerSegment.tables.get(i3);
/*      */ 
/* 1290 */             if ((m == localHtable.tableID) && (n == localHtable.tableClass))
/*      */             {
/* 1292 */               localObject2 = localDHTMarkerSegment;
/* 1293 */               i1 = i3;
/* 1294 */               break;
/*      */             }
/*      */           }
/* 1297 */           if (localObject2 != null) break;
/*      */         }
/* 1299 */         if (localObject2 != null) {
/* 1300 */           ((DHTMarkerSegment)localObject2).tables.set(i1, ((DHTMarkerSegment)localObject2).getHtableFromNode(localNode));
/*      */         } else {
/* 1302 */           localObject2 = (DHTMarkerSegment)localArrayList.get(localArrayList.size() - 1);
/* 1303 */           ((DHTMarkerSegment)localObject2).tables.add(((DHTMarkerSegment)localObject2).getHtableFromNode(localNode));
/*      */         }
/*      */       }
/*      */     } else {
/* 1307 */       localObject1 = new DHTMarkerSegment(paramNode);
/* 1308 */       i = findMarkerSegmentPosition(DQTMarkerSegment.class, false);
/* 1309 */       int j = findMarkerSegmentPosition(SOFMarkerSegment.class, true);
/* 1310 */       int k = findMarkerSegmentPosition(SOSMarkerSegment.class, true);
/* 1311 */       if (i != -1)
/* 1312 */         this.markerSequence.add(i + 1, localObject1);
/* 1313 */       else if (j != -1)
/* 1314 */         this.markerSequence.add(j, localObject1);
/* 1315 */       else if (k != -1)
/* 1316 */         this.markerSequence.add(k, localObject1);
/*      */       else
/* 1318 */         this.markerSequence.add(localObject1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeDRINode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1337 */     DRIMarkerSegment localDRIMarkerSegment1 = (DRIMarkerSegment)findMarkerSegment(DRIMarkerSegment.class, true);
/*      */ 
/* 1339 */     if (localDRIMarkerSegment1 != null) {
/* 1340 */       localDRIMarkerSegment1.updateFromNativeNode(paramNode, false);
/*      */     } else {
/* 1342 */       DRIMarkerSegment localDRIMarkerSegment2 = new DRIMarkerSegment(paramNode);
/* 1343 */       int i = findMarkerSegmentPosition(SOFMarkerSegment.class, true);
/* 1344 */       int j = findMarkerSegmentPosition(SOSMarkerSegment.class, true);
/* 1345 */       if (i != -1)
/* 1346 */         this.markerSequence.add(i, localDRIMarkerSegment2);
/* 1347 */       else if (j != -1)
/* 1348 */         this.markerSequence.add(j, localDRIMarkerSegment2);
/*      */       else
/* 1350 */         this.markerSequence.add(localDRIMarkerSegment2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeCOMNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1361 */     COMMarkerSegment localCOMMarkerSegment = new COMMarkerSegment(paramNode);
/* 1362 */     insertCOMMarkerSegment(localCOMMarkerSegment);
/*      */   }
/*      */ 
/*      */   private void insertCOMMarkerSegment(COMMarkerSegment paramCOMMarkerSegment)
/*      */   {
/* 1378 */     int i = findMarkerSegmentPosition(COMMarkerSegment.class, false);
/* 1379 */     int j = findMarkerSegment(JFIFMarkerSegment.class, true) != null ? 1 : 0;
/* 1380 */     int k = findMarkerSegmentPosition(AdobeMarkerSegment.class, true);
/* 1381 */     if (i != -1)
/* 1382 */       this.markerSequence.add(i + 1, paramCOMMarkerSegment);
/* 1383 */     else if (j != 0)
/* 1384 */       this.markerSequence.add(1, paramCOMMarkerSegment);
/* 1385 */     else if (k != -1)
/* 1386 */       this.markerSequence.add(k + 1, paramCOMMarkerSegment);
/*      */     else
/* 1388 */       this.markerSequence.add(0, paramCOMMarkerSegment);
/*      */   }
/*      */ 
/*      */   private void mergeAdobeNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1400 */     AdobeMarkerSegment localAdobeMarkerSegment1 = (AdobeMarkerSegment)findMarkerSegment(AdobeMarkerSegment.class, true);
/*      */ 
/* 1402 */     if (localAdobeMarkerSegment1 != null) {
/* 1403 */       localAdobeMarkerSegment1.updateFromNativeNode(paramNode, false);
/*      */     } else {
/* 1405 */       AdobeMarkerSegment localAdobeMarkerSegment2 = new AdobeMarkerSegment(paramNode);
/* 1406 */       insertAdobeMarkerSegment(localAdobeMarkerSegment2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void insertAdobeMarkerSegment(AdobeMarkerSegment paramAdobeMarkerSegment)
/*      */   {
/* 1421 */     int i = findMarkerSegment(JFIFMarkerSegment.class, true) != null ? 1 : 0;
/*      */ 
/* 1423 */     int j = findLastUnknownMarkerSegmentPosition();
/* 1424 */     if (i != 0)
/* 1425 */       this.markerSequence.add(1, paramAdobeMarkerSegment);
/* 1426 */     else if (j != -1)
/* 1427 */       this.markerSequence.add(j + 1, paramAdobeMarkerSegment);
/*      */     else
/* 1429 */       this.markerSequence.add(0, paramAdobeMarkerSegment);
/*      */   }
/*      */ 
/*      */   private void mergeUnknownNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1447 */     MarkerSegment localMarkerSegment = new MarkerSegment(paramNode);
/* 1448 */     int i = findLastUnknownMarkerSegmentPosition();
/* 1449 */     int j = findMarkerSegment(JFIFMarkerSegment.class, true) != null ? 1 : 0;
/* 1450 */     int k = findMarkerSegmentPosition(AdobeMarkerSegment.class, true);
/* 1451 */     if (i != -1)
/* 1452 */       this.markerSequence.add(i + 1, localMarkerSegment);
/* 1453 */     else if (j != 0)
/* 1454 */       this.markerSequence.add(1, localMarkerSegment);
/* 1455 */     if (k != -1)
/* 1456 */       this.markerSequence.add(k, localMarkerSegment);
/*      */     else
/* 1458 */       this.markerSequence.add(0, localMarkerSegment);
/*      */   }
/*      */ 
/*      */   private void mergeSOFNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1475 */     SOFMarkerSegment localSOFMarkerSegment1 = (SOFMarkerSegment)findMarkerSegment(SOFMarkerSegment.class, true);
/*      */ 
/* 1477 */     if (localSOFMarkerSegment1 != null) {
/* 1478 */       localSOFMarkerSegment1.updateFromNativeNode(paramNode, false);
/*      */     } else {
/* 1480 */       SOFMarkerSegment localSOFMarkerSegment2 = new SOFMarkerSegment(paramNode);
/* 1481 */       int i = findMarkerSegmentPosition(SOSMarkerSegment.class, true);
/* 1482 */       if (i != -1)
/* 1483 */         this.markerSequence.add(i, localSOFMarkerSegment2);
/*      */       else
/* 1485 */         this.markerSequence.add(localSOFMarkerSegment2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeSOSNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1501 */     SOSMarkerSegment localSOSMarkerSegment1 = (SOSMarkerSegment)findMarkerSegment(SOSMarkerSegment.class, true);
/*      */ 
/* 1503 */     SOSMarkerSegment localSOSMarkerSegment2 = (SOSMarkerSegment)findMarkerSegment(SOSMarkerSegment.class, false);
/*      */ 
/* 1505 */     if (localSOSMarkerSegment1 != null) {
/* 1506 */       if (localSOSMarkerSegment1 != localSOSMarkerSegment2) {
/* 1507 */         throw new IIOInvalidTreeException("Can't merge SOS node into a tree with > 1 SOS node", paramNode);
/*      */       }
/*      */ 
/* 1510 */       localSOSMarkerSegment1.updateFromNativeNode(paramNode, false);
/*      */     } else {
/* 1512 */       this.markerSequence.add(new SOSMarkerSegment(paramNode));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeStandardTree(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1519 */     this.transparencyDone = false;
/* 1520 */     NodeList localNodeList = paramNode.getChildNodes();
/* 1521 */     for (int i = 0; i < localNodeList.getLength(); i++) {
/* 1522 */       Node localNode = localNodeList.item(i);
/* 1523 */       String str = localNode.getNodeName();
/* 1524 */       if (str.equals("Chroma"))
/* 1525 */         mergeStandardChromaNode(localNode, localNodeList);
/* 1526 */       else if (str.equals("Compression"))
/* 1527 */         mergeStandardCompressionNode(localNode);
/* 1528 */       else if (str.equals("Data"))
/* 1529 */         mergeStandardDataNode(localNode);
/* 1530 */       else if (str.equals("Dimension"))
/* 1531 */         mergeStandardDimensionNode(localNode);
/* 1532 */       else if (str.equals("Document"))
/* 1533 */         mergeStandardDocumentNode(localNode);
/* 1534 */       else if (str.equals("Text"))
/* 1535 */         mergeStandardTextNode(localNode);
/* 1536 */       else if (str.equals("Transparency"))
/* 1537 */         mergeStandardTransparencyNode(localNode);
/*      */       else
/* 1539 */         throw new IIOInvalidTreeException("Invalid node: " + str, localNode);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeStandardChromaNode(Node paramNode, NodeList paramNodeList)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1563 */     if (this.transparencyDone) {
/* 1564 */       throw new IIOInvalidTreeException("Transparency node must follow Chroma node", paramNode);
/*      */     }
/*      */ 
/* 1568 */     Node localNode = paramNode.getFirstChild();
/* 1569 */     if ((localNode == null) || (!localNode.getNodeName().equals("ColorSpaceType")))
/*      */     {
/* 1571 */       return;
/*      */     }
/*      */ 
/* 1574 */     String str = localNode.getAttributes().getNamedItem("name").getNodeValue();
/*      */ 
/* 1576 */     int i = 0;
/* 1577 */     int j = 0;
/* 1578 */     int k = 0;
/* 1579 */     int m = 0;
/* 1580 */     boolean bool1 = false;
/* 1581 */     byte[] arrayOfByte = { 1, 2, 3, 4 };
/* 1582 */     if (str.equals("GRAY")) {
/* 1583 */       i = 1;
/* 1584 */       j = 1;
/* 1585 */     } else if (str.equals("YCbCr")) {
/* 1586 */       i = 3;
/* 1587 */       j = 1;
/* 1588 */       bool1 = true;
/* 1589 */     } else if (str.equals("PhotoYCC")) {
/* 1590 */       i = 3;
/* 1591 */       k = 1;
/* 1592 */       m = 1;
/* 1593 */       arrayOfByte[0] = 89;
/* 1594 */       arrayOfByte[1] = 67;
/* 1595 */       arrayOfByte[2] = 99;
/* 1596 */     } else if (str.equals("RGB")) {
/* 1597 */       i = 3;
/* 1598 */       k = 1;
/* 1599 */       m = 0;
/* 1600 */       arrayOfByte[0] = 82;
/* 1601 */       arrayOfByte[1] = 71;
/* 1602 */       arrayOfByte[2] = 66;
/* 1603 */     } else if ((str.equals("XYZ")) || (str.equals("Lab")) || (str.equals("Luv")) || (str.equals("YxY")) || (str.equals("HSV")) || (str.equals("HLS")) || (str.equals("CMY")) || (str.equals("3CLR")))
/*      */     {
/* 1611 */       i = 3;
/* 1612 */     } else if (str.equals("YCCK")) {
/* 1613 */       i = 4;
/* 1614 */       k = 1;
/* 1615 */       m = 2;
/* 1616 */       bool1 = true;
/* 1617 */     } else if (str.equals("CMYK")) {
/* 1618 */       i = 4;
/* 1619 */       k = 1;
/* 1620 */       m = 0;
/* 1621 */     } else if (str.equals("4CLR")) {
/* 1622 */       i = 4;
/*      */     } else {
/* 1624 */       return;
/*      */     }
/*      */ 
/* 1627 */     boolean bool2 = false;
/* 1628 */     for (int n = 0; n < paramNodeList.getLength(); n++) {
/* 1629 */       localObject1 = paramNodeList.item(n);
/* 1630 */       if (((Node)localObject1).getNodeName().equals("Transparency")) {
/* 1631 */         bool2 = wantAlpha((Node)localObject1);
/* 1632 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1636 */     if (bool2) {
/* 1637 */       i++;
/* 1638 */       j = 0;
/* 1639 */       if (arrayOfByte[0] == 82) {
/* 1640 */         arrayOfByte[3] = 65;
/* 1641 */         k = 0;
/*      */       }
/*      */     }
/*      */ 
/* 1645 */     JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 1647 */     Object localObject1 = (AdobeMarkerSegment)findMarkerSegment(AdobeMarkerSegment.class, true);
/*      */ 
/* 1649 */     SOFMarkerSegment localSOFMarkerSegment = (SOFMarkerSegment)findMarkerSegment(SOFMarkerSegment.class, true);
/*      */ 
/* 1651 */     SOSMarkerSegment localSOSMarkerSegment = (SOSMarkerSegment)findMarkerSegment(SOSMarkerSegment.class, true);
/*      */ 
/* 1660 */     if ((localSOFMarkerSegment != null) && (localSOFMarkerSegment.tag == 194) && 
/* 1661 */       (localSOFMarkerSegment.componentSpecs.length != i) && (localSOSMarkerSegment != null)) {
/* 1662 */       return;
/*      */     }
/*      */ 
/* 1667 */     if ((j == 0) && (localJFIFMarkerSegment != null)) {
/* 1668 */       this.markerSequence.remove(localJFIFMarkerSegment);
/*      */     }
/*      */ 
/* 1672 */     if ((j != 0) && (!this.isStream)) {
/* 1673 */       this.markerSequence.add(0, new JFIFMarkerSegment());
/*      */     }
/*      */ 
/* 1678 */     if (k != 0) {
/* 1679 */       if ((localObject1 == null) && (!this.isStream)) {
/* 1680 */         localObject1 = new AdobeMarkerSegment(m);
/* 1681 */         insertAdobeMarkerSegment((AdobeMarkerSegment)localObject1);
/*      */       } else {
/* 1683 */         ((AdobeMarkerSegment)localObject1).transform = m;
/*      */       }
/* 1685 */     } else if (localObject1 != null) {
/* 1686 */       this.markerSequence.remove(localObject1);
/*      */     }
/*      */ 
/* 1689 */     int i1 = 0;
/* 1690 */     int i2 = 0;
/*      */ 
/* 1692 */     boolean bool3 = false;
/*      */ 
/* 1694 */     int[] arrayOfInt1 = { 0, 1, 1, 0 };
/* 1695 */     int[] arrayOfInt2 = { 0, 0, 0, 0 };
/*      */ 
/* 1697 */     int[] arrayOfInt3 = bool1 ? arrayOfInt1 : arrayOfInt2;
/*      */ 
/* 1702 */     SOFMarkerSegment.ComponentSpec[] arrayOfComponentSpec = null;
/*      */     Iterator localIterator1;
/*      */     Object localObject2;
/*      */     Object localObject3;
/* 1704 */     if (localSOFMarkerSegment != null) {
/* 1705 */       arrayOfComponentSpec = localSOFMarkerSegment.componentSpecs;
/* 1706 */       bool3 = localSOFMarkerSegment.tag == 194;
/*      */ 
/* 1709 */       this.markerSequence.set(this.markerSequence.indexOf(localSOFMarkerSegment), new SOFMarkerSegment(bool3, false, bool1, arrayOfByte, i));
/*      */ 
/* 1721 */       for (int i3 = 0; i3 < arrayOfComponentSpec.length; i3++) {
/* 1722 */         if (arrayOfComponentSpec[i3].QtableSelector != arrayOfInt3[i3]) {
/* 1723 */           i1 = 1;
/*      */         }
/*      */       }
/*      */ 
/* 1727 */       if (bool3)
/*      */       {
/* 1730 */         i3 = 0;
/* 1731 */         for (int i4 = 0; i4 < arrayOfComponentSpec.length; i4++) {
/* 1732 */           if (arrayOfByte[i4] != arrayOfComponentSpec[i4].componentId) {
/* 1733 */             i3 = 1;
/*      */           }
/*      */         }
/* 1736 */         if (i3 != 0)
/*      */         {
/* 1738 */           for (localIterator1 = this.markerSequence.iterator(); localIterator1.hasNext(); ) {
/* 1739 */             localObject2 = (MarkerSegment)localIterator1.next();
/* 1740 */             if ((localObject2 instanceof SOSMarkerSegment)) {
/* 1741 */               localObject3 = (SOSMarkerSegment)localObject2;
/* 1742 */               for (int i7 = 0; i7 < ((SOSMarkerSegment)localObject3).componentSpecs.length; i7++) {
/* 1743 */                 int i8 = localObject3.componentSpecs[i7].componentSelector;
/*      */ 
/* 1751 */                 for (int i9 = 0; i9 < arrayOfComponentSpec.length; i9++) {
/* 1752 */                   if (arrayOfComponentSpec[i9].componentId == i8) {
/* 1753 */                     localObject3.componentSpecs[i7].componentSelector = arrayOfByte[i9];
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/* 1762 */       else if (localSOSMarkerSegment != null)
/*      */       {
/* 1765 */         for (i3 = 0; i3 < localSOSMarkerSegment.componentSpecs.length; i3++) {
/* 1766 */           if ((localSOSMarkerSegment.componentSpecs[i3].dcHuffTable != arrayOfInt3[i3]) || (localSOSMarkerSegment.componentSpecs[i3].acHuffTable != arrayOfInt3[i3]))
/*      */           {
/* 1770 */             i2 = 1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1775 */         this.markerSequence.set(this.markerSequence.indexOf(localSOSMarkerSegment), new SOSMarkerSegment(bool1, arrayOfByte, i));
/*      */       }
/*      */ 
/*      */     }
/* 1783 */     else if (this.isStream)
/*      */     {
/* 1785 */       i1 = 1;
/* 1786 */       i2 = 1;
/*      */     }
/*      */     ArrayList localArrayList;
/*      */     Object localObject4;
/*      */     Object localObject5;
/* 1790 */     if (i1 != 0) {
/* 1791 */       localArrayList = new ArrayList();
/* 1792 */       for (localIterator1 = this.markerSequence.iterator(); localIterator1.hasNext(); ) {
/* 1793 */         localObject2 = (MarkerSegment)localIterator1.next();
/* 1794 */         if ((localObject2 instanceof DQTMarkerSegment)) {
/* 1795 */           localArrayList.add(localObject2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1801 */       if ((!localArrayList.isEmpty()) && (bool1))
/*      */       {
/* 1808 */         int i5 = 0;
/* 1809 */         for (localObject2 = localArrayList.iterator(); ((Iterator)localObject2).hasNext(); ) {
/* 1810 */           localObject3 = (DQTMarkerSegment)((Iterator)localObject2).next();
/* 1811 */           localObject4 = ((DQTMarkerSegment)localObject3).tables.iterator();
/* 1812 */           while (((Iterator)localObject4).hasNext()) {
/* 1813 */             localObject5 = (DQTMarkerSegment.Qtable)((Iterator)localObject4).next();
/*      */ 
/* 1815 */             if (((DQTMarkerSegment.Qtable)localObject5).tableID == 1) {
/* 1816 */               i5 = 1;
/*      */             }
/*      */           }
/*      */         }
/* 1820 */         if (i5 == 0)
/*      */         {
/* 1822 */           localObject2 = null;
/* 1823 */           for (localObject3 = localArrayList.iterator(); ((Iterator)localObject3).hasNext(); ) {
/* 1824 */             localObject4 = (DQTMarkerSegment)((Iterator)localObject3).next();
/* 1825 */             localObject5 = ((DQTMarkerSegment)localObject4).tables.iterator();
/* 1826 */             while (((Iterator)localObject5).hasNext()) {
/* 1827 */               DQTMarkerSegment.Qtable localQtable = (DQTMarkerSegment.Qtable)((Iterator)localObject5).next();
/*      */ 
/* 1829 */               if (localQtable.tableID == 0) {
/* 1830 */                 localObject2 = localQtable;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1838 */           localObject3 = (DQTMarkerSegment)localArrayList.get(localArrayList.size() - 1);
/*      */ 
/* 1840 */           ((DQTMarkerSegment)localObject3).tables.add(((DQTMarkerSegment)localObject3).getChromaForLuma((DQTMarkerSegment.Qtable)localObject2));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1845 */     if (i2 != 0) {
/* 1846 */       localArrayList = new ArrayList();
/* 1847 */       for (Iterator localIterator2 = this.markerSequence.iterator(); localIterator2.hasNext(); ) {
/* 1848 */         localObject2 = (MarkerSegment)localIterator2.next();
/* 1849 */         if ((localObject2 instanceof DHTMarkerSegment)) {
/* 1850 */           localArrayList.add(localObject2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1856 */       if ((!localArrayList.isEmpty()) && (bool1))
/*      */       {
/* 1862 */         int i6 = 0;
/* 1863 */         for (localObject2 = localArrayList.iterator(); ((Iterator)localObject2).hasNext(); ) {
/* 1864 */           localObject3 = (DHTMarkerSegment)((Iterator)localObject2).next();
/* 1865 */           localObject4 = ((DHTMarkerSegment)localObject3).tables.iterator();
/* 1866 */           while (((Iterator)localObject4).hasNext()) {
/* 1867 */             localObject5 = (DHTMarkerSegment.Htable)((Iterator)localObject4).next();
/*      */ 
/* 1869 */             if (((DHTMarkerSegment.Htable)localObject5).tableID == 1) {
/* 1870 */               i6 = 1;
/*      */             }
/*      */           }
/*      */         }
/* 1874 */         if (i6 == 0)
/*      */         {
/* 1877 */           localObject2 = (DHTMarkerSegment)localArrayList.get(localArrayList.size() - 1);
/*      */ 
/* 1879 */           ((DHTMarkerSegment)localObject2).addHtable(JPEGHuffmanTable.StdDCLuminance, true, 1);
/* 1880 */           ((DHTMarkerSegment)localObject2).addHtable(JPEGHuffmanTable.StdACLuminance, true, 1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean wantAlpha(Node paramNode) {
/* 1887 */     boolean bool = false;
/* 1888 */     Node localNode = paramNode.getFirstChild();
/* 1889 */     if ((localNode.getNodeName().equals("Alpha")) && 
/* 1890 */       (localNode.hasAttributes())) {
/* 1891 */       String str = localNode.getAttributes().getNamedItem("value").getNodeValue();
/*      */ 
/* 1893 */       if (!str.equals("none")) {
/* 1894 */         bool = true;
/*      */       }
/*      */     }
/*      */ 
/* 1898 */     this.transparencyDone = true;
/* 1899 */     return bool;
/*      */   }
/*      */ 
/*      */   private void mergeStandardCompressionNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/*      */   }
/*      */ 
/*      */   private void mergeStandardDataNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/*      */   }
/*      */ 
/*      */   private void mergeStandardDimensionNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1918 */     JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */     Object localObject;
/* 1920 */     if (localJFIFMarkerSegment == null)
/*      */     {
/* 1925 */       int i = 0;
/* 1926 */       SOFMarkerSegment localSOFMarkerSegment = (SOFMarkerSegment)findMarkerSegment(SOFMarkerSegment.class, true);
/*      */ 
/* 1928 */       if (localSOFMarkerSegment != null) {
/* 1929 */         int k = localSOFMarkerSegment.componentSpecs.length;
/* 1930 */         if ((k == 1) || (k == 3)) {
/* 1931 */           i = 1;
/* 1932 */           for (int m = 0; m < localSOFMarkerSegment.componentSpecs.length; m++) {
/* 1933 */             if (localSOFMarkerSegment.componentSpecs[m].componentId != m + 1) {
/* 1934 */               i = 0;
/*      */             }
/*      */           }
/*      */ 
/* 1938 */           localObject = (AdobeMarkerSegment)findMarkerSegment(AdobeMarkerSegment.class, true);
/*      */ 
/* 1941 */           if (localObject != null) {
/* 1942 */             if (((AdobeMarkerSegment)localObject).transform != (k == 1 ? 0 : 1))
/*      */             {
/* 1945 */               i = 0;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1952 */       if (i != 0) {
/* 1953 */         localJFIFMarkerSegment = new JFIFMarkerSegment();
/* 1954 */         this.markerSequence.add(0, localJFIFMarkerSegment);
/*      */       }
/*      */     }
/* 1957 */     if (localJFIFMarkerSegment != null) {
/* 1958 */       NodeList localNodeList = paramNode.getChildNodes();
/* 1959 */       for (int j = 0; j < localNodeList.getLength(); j++) {
/* 1960 */         Node localNode = localNodeList.item(j);
/* 1961 */         localObject = localNode.getAttributes();
/* 1962 */         String str1 = localNode.getNodeName();
/*      */         String str2;
/*      */         float f;
/* 1963 */         if (str1.equals("PixelAspectRatio")) {
/* 1964 */           str2 = ((NamedNodeMap)localObject).getNamedItem("value").getNodeValue();
/* 1965 */           f = Float.parseFloat(str2);
/* 1966 */           Point localPoint = findIntegerRatio(f);
/* 1967 */           localJFIFMarkerSegment.resUnits = 0;
/* 1968 */           localJFIFMarkerSegment.Xdensity = localPoint.x;
/* 1969 */           localJFIFMarkerSegment.Xdensity = localPoint.y;
/*      */         }
/*      */         else
/*      */         {
/*      */           int n;
/* 1970 */           if (str1.equals("HorizontalPixelSize")) {
/* 1971 */             str2 = ((NamedNodeMap)localObject).getNamedItem("value").getNodeValue();
/* 1972 */             f = Float.parseFloat(str2);
/*      */ 
/* 1974 */             n = (int)Math.round(1.0D / (f * 10.0D));
/* 1975 */             localJFIFMarkerSegment.resUnits = 2;
/* 1976 */             localJFIFMarkerSegment.Xdensity = n;
/* 1977 */           } else if (str1.equals("VerticalPixelSize")) {
/* 1978 */             str2 = ((NamedNodeMap)localObject).getNamedItem("value").getNodeValue();
/* 1979 */             f = Float.parseFloat(str2);
/*      */ 
/* 1981 */             n = (int)Math.round(1.0D / (f * 10.0D));
/* 1982 */             localJFIFMarkerSegment.resUnits = 2;
/* 1983 */             localJFIFMarkerSegment.Ydensity = n;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Point findIntegerRatio(float paramFloat)
/*      */   {
/* 1995 */     float f1 = 0.005F;
/*      */ 
/* 1998 */     paramFloat = Math.abs(paramFloat);
/*      */ 
/* 2001 */     if (paramFloat <= f1) {
/* 2002 */       return new Point(1, 255);
/*      */     }
/*      */ 
/* 2006 */     if (paramFloat >= 255.0F) {
/* 2007 */       return new Point(255, 1);
/*      */     }
/*      */ 
/* 2011 */     int i = 0;
/* 2012 */     if (paramFloat < 1.0D) {
/* 2013 */       paramFloat = 1.0F / paramFloat;
/* 2014 */       i = 1;
/*      */     }
/*      */ 
/* 2018 */     int j = 1;
/* 2019 */     int k = Math.round(paramFloat);
/*      */ 
/* 2021 */     float f2 = k;
/* 2022 */     float f3 = Math.abs(paramFloat - f2);
/* 2023 */     while (f3 > f1)
/*      */     {
/* 2025 */       j++;
/* 2026 */       k = Math.round(j * paramFloat);
/* 2027 */       f2 = k / j;
/* 2028 */       f3 = Math.abs(paramFloat - f2);
/*      */     }
/* 2030 */     return i != 0 ? new Point(j, k) : new Point(k, j);
/*      */   }
/*      */ 
/*      */   private void mergeStandardDocumentNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/*      */   }
/*      */ 
/*      */   private void mergeStandardTextNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 2043 */     NodeList localNodeList = paramNode.getChildNodes();
/* 2044 */     for (int i = 0; i < localNodeList.getLength(); i++) {
/* 2045 */       Node localNode1 = localNodeList.item(i);
/* 2046 */       NamedNodeMap localNamedNodeMap = localNode1.getAttributes();
/* 2047 */       Node localNode2 = localNamedNodeMap.getNamedItem("compression");
/* 2048 */       int j = 1;
/*      */       String str;
/* 2049 */       if (localNode2 != null) {
/* 2050 */         str = localNode2.getNodeValue();
/* 2051 */         if (!str.equals("none")) {
/* 2052 */           j = 0;
/*      */         }
/*      */       }
/* 2055 */       if (j != 0) {
/* 2056 */         str = localNamedNodeMap.getNamedItem("value").getNodeValue();
/* 2057 */         COMMarkerSegment localCOMMarkerSegment = new COMMarkerSegment(str);
/* 2058 */         insertCOMMarkerSegment(localCOMMarkerSegment);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeStandardTransparencyNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 2069 */     if ((!this.transparencyDone) && (!this.isStream)) {
/* 2070 */       boolean bool1 = wantAlpha(paramNode);
/*      */ 
/* 2074 */       JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 2076 */       AdobeMarkerSegment localAdobeMarkerSegment = (AdobeMarkerSegment)findMarkerSegment(AdobeMarkerSegment.class, true);
/*      */ 
/* 2078 */       SOFMarkerSegment localSOFMarkerSegment = (SOFMarkerSegment)findMarkerSegment(SOFMarkerSegment.class, true);
/*      */ 
/* 2080 */       SOSMarkerSegment localSOSMarkerSegment = (SOSMarkerSegment)findMarkerSegment(SOSMarkerSegment.class, true);
/*      */ 
/* 2085 */       if ((localSOFMarkerSegment != null) && (localSOFMarkerSegment.tag == 194)) {
/* 2086 */         return;
/*      */       }
/*      */ 
/* 2091 */       if (localSOFMarkerSegment != null) {
/* 2092 */         int i = localSOFMarkerSegment.componentSpecs.length;
/* 2093 */         boolean bool2 = (i == 2) || (i == 4);
/*      */ 
/* 2095 */         if (bool2 != bool1)
/*      */         {
/*      */           SOFMarkerSegment.ComponentSpec[] arrayOfComponentSpec;
/*      */           int j;
/*      */           int k;
/* 2096 */           if (bool1) {
/* 2097 */             i++;
/* 2098 */             if (localJFIFMarkerSegment != null) {
/* 2099 */               this.markerSequence.remove(localJFIFMarkerSegment);
/*      */             }
/*      */ 
/* 2103 */             if (localAdobeMarkerSegment != null) {
/* 2104 */               localAdobeMarkerSegment.transform = 0;
/*      */             }
/*      */ 
/* 2108 */             arrayOfComponentSpec = new SOFMarkerSegment.ComponentSpec[i];
/*      */ 
/* 2110 */             for (j = 0; j < localSOFMarkerSegment.componentSpecs.length; j++) {
/* 2111 */               arrayOfComponentSpec[j] = localSOFMarkerSegment.componentSpecs[j];
/*      */             }
/* 2113 */             j = (byte)localSOFMarkerSegment.componentSpecs[0].componentId;
/* 2114 */             k = (byte)(j > 1 ? 65 : 4);
/* 2115 */             arrayOfComponentSpec[(i - 1)] = localSOFMarkerSegment.getComponentSpec(k, localSOFMarkerSegment.componentSpecs[0].HsamplingFactor, localSOFMarkerSegment.componentSpecs[0].QtableSelector);
/*      */ 
/* 2120 */             localSOFMarkerSegment.componentSpecs = arrayOfComponentSpec;
/*      */ 
/* 2123 */             SOSMarkerSegment.ScanComponentSpec[] arrayOfScanComponentSpec2 = new SOSMarkerSegment.ScanComponentSpec[i];
/*      */ 
/* 2125 */             for (int m = 0; m < localSOSMarkerSegment.componentSpecs.length; m++) {
/* 2126 */               arrayOfScanComponentSpec2[m] = localSOSMarkerSegment.componentSpecs[m];
/*      */             }
/* 2128 */             arrayOfScanComponentSpec2[(i - 1)] = localSOSMarkerSegment.getScanComponentSpec(k, 0);
/*      */ 
/* 2130 */             localSOSMarkerSegment.componentSpecs = arrayOfScanComponentSpec2;
/*      */           } else {
/* 2132 */             i--;
/*      */ 
/* 2134 */             arrayOfComponentSpec = new SOFMarkerSegment.ComponentSpec[i];
/*      */ 
/* 2136 */             for (j = 0; j < i; j++) {
/* 2137 */               arrayOfComponentSpec[j] = localSOFMarkerSegment.componentSpecs[j];
/*      */             }
/* 2139 */             localSOFMarkerSegment.componentSpecs = arrayOfComponentSpec;
/*      */ 
/* 2142 */             SOSMarkerSegment.ScanComponentSpec[] arrayOfScanComponentSpec1 = new SOSMarkerSegment.ScanComponentSpec[i];
/*      */ 
/* 2144 */             for (k = 0; k < i; k++) {
/* 2145 */               arrayOfScanComponentSpec1[k] = localSOSMarkerSegment.componentSpecs[k];
/*      */             }
/* 2147 */             localSOSMarkerSegment.componentSpecs = arrayOfScanComponentSpec1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setFromTree(String paramString, Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 2157 */     if (paramString == null) {
/* 2158 */       throw new IllegalArgumentException("null formatName!");
/*      */     }
/* 2160 */     if (paramNode == null) {
/* 2161 */       throw new IllegalArgumentException("null root!");
/*      */     }
/* 2163 */     if ((this.isStream) && (paramString.equals("javax_imageio_jpeg_stream_1.0")))
/*      */     {
/* 2165 */       setFromNativeTree(paramNode);
/* 2166 */     } else if ((!this.isStream) && (paramString.equals("javax_imageio_jpeg_image_1.0")))
/*      */     {
/* 2168 */       setFromNativeTree(paramNode);
/* 2169 */     } else if ((!this.isStream) && (paramString.equals("javax_imageio_1.0")))
/*      */     {
/* 2173 */       super.setFromTree(paramString, paramNode);
/*      */     }
/* 2175 */     else throw new IllegalArgumentException("Unsupported format name: " + paramString);
/*      */   }
/*      */ 
/*      */   private void setFromNativeTree(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 2181 */     if (this.resetSequence == null) {
/* 2182 */       this.resetSequence = this.markerSequence;
/*      */     }
/* 2184 */     this.markerSequence = new ArrayList();
/*      */ 
/* 2188 */     String str = paramNode.getNodeName();
/* 2189 */     if (str != (this.isStream ? "javax_imageio_jpeg_stream_1.0" : "javax_imageio_jpeg_image_1.0"))
/*      */     {
/* 2191 */       throw new IIOInvalidTreeException("Invalid root node name: " + str, paramNode);
/*      */     }
/*      */ 
/* 2194 */     if (!this.isStream) {
/* 2195 */       if (paramNode.getChildNodes().getLength() != 2) {
/* 2196 */         throw new IIOInvalidTreeException("JPEGvariety and markerSequence nodes must be present", paramNode);
/*      */       }
/*      */ 
/* 2200 */       localNode = paramNode.getFirstChild();
/*      */ 
/* 2202 */       if (localNode.getChildNodes().getLength() != 0) {
/* 2203 */         this.markerSequence.add(new JFIFMarkerSegment(localNode.getFirstChild()));
/*      */       }
/*      */     }
/*      */ 
/* 2207 */     Node localNode = this.isStream ? paramNode : paramNode.getLastChild();
/* 2208 */     setFromMarkerSequenceNode(localNode);
/*      */   }
/*      */ 
/*      */   void setFromMarkerSequenceNode(Node paramNode)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 2215 */     NodeList localNodeList = paramNode.getChildNodes();
/*      */ 
/* 2217 */     for (int i = 0; i < localNodeList.getLength(); i++) {
/* 2218 */       Node localNode = localNodeList.item(i);
/* 2219 */       String str = localNode.getNodeName();
/* 2220 */       if (str.equals("dqt"))
/* 2221 */         this.markerSequence.add(new DQTMarkerSegment(localNode));
/* 2222 */       else if (str.equals("dht"))
/* 2223 */         this.markerSequence.add(new DHTMarkerSegment(localNode));
/* 2224 */       else if (str.equals("dri"))
/* 2225 */         this.markerSequence.add(new DRIMarkerSegment(localNode));
/* 2226 */       else if (str.equals("com"))
/* 2227 */         this.markerSequence.add(new COMMarkerSegment(localNode));
/* 2228 */       else if (str.equals("app14Adobe"))
/* 2229 */         this.markerSequence.add(new AdobeMarkerSegment(localNode));
/* 2230 */       else if (str.equals("unknown"))
/* 2231 */         this.markerSequence.add(new MarkerSegment(localNode));
/* 2232 */       else if (str.equals("sof"))
/* 2233 */         this.markerSequence.add(new SOFMarkerSegment(localNode));
/* 2234 */       else if (str.equals("sos"))
/* 2235 */         this.markerSequence.add(new SOSMarkerSegment(localNode));
/*      */       else
/* 2237 */         throw new IIOInvalidTreeException("Invalid " + (this.isStream ? "stream " : "image ") + "child: " + str, localNode);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isConsistent()
/*      */   {
/* 2252 */     SOFMarkerSegment localSOFMarkerSegment = (SOFMarkerSegment)findMarkerSegment(SOFMarkerSegment.class, true);
/*      */ 
/* 2255 */     JFIFMarkerSegment localJFIFMarkerSegment = (JFIFMarkerSegment)findMarkerSegment(JFIFMarkerSegment.class, true);
/*      */ 
/* 2258 */     AdobeMarkerSegment localAdobeMarkerSegment = (AdobeMarkerSegment)findMarkerSegment(AdobeMarkerSegment.class, true);
/*      */ 
/* 2261 */     boolean bool = true;
/* 2262 */     if (!this.isStream) {
/* 2263 */       if (localSOFMarkerSegment != null)
/*      */       {
/* 2265 */         int i = localSOFMarkerSegment.componentSpecs.length;
/* 2266 */         int j = countScanBands();
/* 2267 */         if ((j != 0) && 
/* 2268 */           (j != i)) {
/* 2269 */           bool = false;
/*      */         }
/*      */ 
/* 2273 */         if (localJFIFMarkerSegment != null) {
/* 2274 */           if ((i != 1) && (i != 3)) {
/* 2275 */             bool = false;
/*      */           }
/* 2277 */           for (int k = 0; k < i; k++) {
/* 2278 */             if (localSOFMarkerSegment.componentSpecs[k].componentId != k + 1) {
/* 2279 */               bool = false;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 2286 */           if ((localAdobeMarkerSegment != null) && (((i == 1) && (localAdobeMarkerSegment.transform != 0)) || ((i == 3) && (localAdobeMarkerSegment.transform != 1))))
/*      */           {
/* 2291 */             bool = false;
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/* 2296 */         SOSMarkerSegment localSOSMarkerSegment = (SOSMarkerSegment)findMarkerSegment(SOSMarkerSegment.class, true);
/*      */ 
/* 2299 */         if ((localJFIFMarkerSegment != null) || (localAdobeMarkerSegment != null) || (localSOFMarkerSegment != null) || (localSOSMarkerSegment != null))
/*      */         {
/* 2301 */           bool = false;
/*      */         }
/*      */       }
/*      */     }
/* 2305 */     return bool;
/*      */   }
/*      */ 
/*      */   private int countScanBands()
/*      */   {
/* 2313 */     ArrayList localArrayList = new ArrayList();
/* 2314 */     Iterator localIterator = this.markerSequence.iterator();
/* 2315 */     while (localIterator.hasNext()) {
/* 2316 */       MarkerSegment localMarkerSegment = (MarkerSegment)localIterator.next();
/* 2317 */       if ((localMarkerSegment instanceof SOSMarkerSegment)) {
/* 2318 */         SOSMarkerSegment localSOSMarkerSegment = (SOSMarkerSegment)localMarkerSegment;
/* 2319 */         SOSMarkerSegment.ScanComponentSpec[] arrayOfScanComponentSpec = localSOSMarkerSegment.componentSpecs;
/* 2320 */         for (int i = 0; i < arrayOfScanComponentSpec.length; i++) {
/* 2321 */           Integer localInteger = new Integer(arrayOfScanComponentSpec[i].componentSelector);
/* 2322 */           if (!localArrayList.contains(localInteger)) {
/* 2323 */             localArrayList.add(localInteger);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2329 */     return localArrayList.size();
/*      */   }
/*      */ 
/*      */   void writeToStream(ImageOutputStream paramImageOutputStream, boolean paramBoolean1, boolean paramBoolean2, List paramList, ICC_Profile paramICC_Profile, boolean paramBoolean3, int paramInt, JPEGImageWriter paramJPEGImageWriter)
/*      */     throws IOException
/*      */   {
/* 2343 */     if (paramBoolean2)
/*      */     {
/* 2347 */       JFIFMarkerSegment.writeDefaultJFIF(paramImageOutputStream, paramList, paramICC_Profile, paramJPEGImageWriter);
/*      */ 
/* 2351 */       if ((!paramBoolean3) && (paramInt != -1))
/*      */       {
/* 2353 */         if ((paramInt != 0) && (paramInt != 1))
/*      */         {
/* 2356 */           paramBoolean3 = true;
/* 2357 */           paramJPEGImageWriter.warningOccurred(13);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2363 */     Iterator localIterator = this.markerSequence.iterator();
/* 2364 */     while (localIterator.hasNext()) {
/* 2365 */       MarkerSegment localMarkerSegment = (MarkerSegment)localIterator.next();
/*      */       Object localObject;
/* 2366 */       if ((localMarkerSegment instanceof JFIFMarkerSegment)) {
/* 2367 */         if (!paramBoolean1) {
/* 2368 */           localObject = (JFIFMarkerSegment)localMarkerSegment;
/* 2369 */           ((JFIFMarkerSegment)localObject).writeWithThumbs(paramImageOutputStream, paramList, paramJPEGImageWriter);
/* 2370 */           if (paramICC_Profile != null)
/* 2371 */             JFIFMarkerSegment.writeICC(paramICC_Profile, paramImageOutputStream);
/*      */         }
/*      */       }
/* 2374 */       else if ((localMarkerSegment instanceof AdobeMarkerSegment)) {
/* 2375 */         if (!paramBoolean3)
/* 2376 */           if (paramInt != -1) {
/* 2377 */             localObject = (AdobeMarkerSegment)localMarkerSegment.clone();
/*      */ 
/* 2379 */             ((AdobeMarkerSegment)localObject).transform = paramInt;
/* 2380 */             ((AdobeMarkerSegment)localObject).write(paramImageOutputStream);
/* 2381 */           } else if (paramBoolean2)
/*      */           {
/* 2383 */             localObject = (AdobeMarkerSegment)localMarkerSegment;
/* 2384 */             if ((((AdobeMarkerSegment)localObject).transform == 0) || (((AdobeMarkerSegment)localObject).transform == 1))
/*      */             {
/* 2386 */               ((AdobeMarkerSegment)localObject).write(paramImageOutputStream);
/*      */             }
/* 2388 */             else paramJPEGImageWriter.warningOccurred(13);
/*      */           }
/*      */           else
/*      */           {
/* 2392 */             localMarkerSegment.write(paramImageOutputStream);
/*      */           }
/*      */       }
/*      */       else
/* 2396 */         localMarkerSegment.write(paramImageOutputStream);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/* 2404 */     if (this.resetSequence != null) {
/* 2405 */       this.markerSequence = this.resetSequence;
/* 2406 */       this.resetSequence = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void print() {
/* 2411 */     for (int i = 0; i < this.markerSequence.size(); i++) {
/* 2412 */       MarkerSegment localMarkerSegment = (MarkerSegment)this.markerSequence.get(i);
/* 2413 */       localMarkerSegment.print();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGMetadata
 * JD-Core Version:    0.6.2
 */
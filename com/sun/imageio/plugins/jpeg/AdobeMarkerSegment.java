/*     */ package com.sun.imageio.plugins.jpeg;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ class AdobeMarkerSegment extends MarkerSegment
/*     */ {
/*     */   int version;
/*     */   int flags0;
/*     */   int flags1;
/*     */   int transform;
/*     */   private static final int ID_SIZE = 5;
/*     */ 
/*     */   AdobeMarkerSegment(int paramInt)
/*     */   {
/*  49 */     super(238);
/*  50 */     this.version = 101;
/*  51 */     this.flags0 = 0;
/*  52 */     this.flags1 = 0;
/*  53 */     this.transform = paramInt;
/*     */   }
/*     */ 
/*     */   AdobeMarkerSegment(JPEGBuffer paramJPEGBuffer) throws IOException {
/*  57 */     super(paramJPEGBuffer);
/*  58 */     paramJPEGBuffer.bufPtr += 5;
/*  59 */     this.version = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/*  60 */     this.version |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/*  61 */     this.flags0 = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/*  62 */     this.flags0 |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/*  63 */     this.flags1 = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/*  64 */     this.flags1 |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/*  65 */     this.transform = (paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF);
/*  66 */     paramJPEGBuffer.bufAvail -= this.length;
/*     */   }
/*     */ 
/*     */   AdobeMarkerSegment(Node paramNode) throws IIOInvalidTreeException {
/*  70 */     this(0);
/*  71 */     updateFromNativeNode(paramNode, true);
/*     */   }
/*     */ 
/*     */   IIOMetadataNode getNativeNode() {
/*  75 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("app14Adobe");
/*  76 */     localIIOMetadataNode.setAttribute("version", Integer.toString(this.version));
/*  77 */     localIIOMetadataNode.setAttribute("flags0", Integer.toString(this.flags0));
/*  78 */     localIIOMetadataNode.setAttribute("flags1", Integer.toString(this.flags1));
/*  79 */     localIIOMetadataNode.setAttribute("transform", Integer.toString(this.transform));
/*     */ 
/*  81 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   void updateFromNativeNode(Node paramNode, boolean paramBoolean)
/*     */     throws IIOInvalidTreeException
/*     */   {
/*  87 */     NamedNodeMap localNamedNodeMap = paramNode.getAttributes();
/*  88 */     this.transform = getAttributeValue(paramNode, localNamedNodeMap, "transform", 0, 2, true);
/*  89 */     int i = localNamedNodeMap.getLength();
/*  90 */     if (i > 4) {
/*  91 */       throw new IIOInvalidTreeException("Adobe APP14 node cannot have > 4 attributes", paramNode);
/*     */     }
/*     */ 
/*  94 */     if (i > 1) {
/*  95 */       int j = getAttributeValue(paramNode, localNamedNodeMap, "version", 100, 255, false);
/*     */ 
/*  97 */       this.version = (j != -1 ? j : this.version);
/*  98 */       j = getAttributeValue(paramNode, localNamedNodeMap, "flags0", 0, 65535, false);
/*  99 */       this.flags0 = (j != -1 ? j : this.flags0);
/* 100 */       j = getAttributeValue(paramNode, localNamedNodeMap, "flags1", 0, 65535, false);
/* 101 */       this.flags1 = (j != -1 ? j : this.flags1);
/*     */     }
/*     */   }
/*     */ 
/*     */   void write(ImageOutputStream paramImageOutputStream)
/*     */     throws IOException
/*     */   {
/* 110 */     this.length = 14;
/* 111 */     writeTag(paramImageOutputStream);
/* 112 */     byte[] arrayOfByte = { 65, 100, 111, 98, 101 };
/* 113 */     paramImageOutputStream.write(arrayOfByte);
/* 114 */     write2bytes(paramImageOutputStream, this.version);
/* 115 */     write2bytes(paramImageOutputStream, this.flags0);
/* 116 */     write2bytes(paramImageOutputStream, this.flags1);
/* 117 */     paramImageOutputStream.write(this.transform);
/*     */   }
/*     */ 
/*     */   static void writeAdobeSegment(ImageOutputStream paramImageOutputStream, int paramInt) throws IOException
/*     */   {
/* 122 */     new AdobeMarkerSegment(paramInt).write(paramImageOutputStream);
/*     */   }
/*     */ 
/*     */   void print() {
/* 126 */     printTag("Adobe APP14");
/* 127 */     System.out.print("Version: ");
/* 128 */     System.out.println(this.version);
/* 129 */     System.out.print("Flags0: 0x");
/* 130 */     System.out.println(Integer.toHexString(this.flags0));
/* 131 */     System.out.print("Flags1: 0x");
/* 132 */     System.out.println(Integer.toHexString(this.flags1));
/* 133 */     System.out.print("Transform: ");
/* 134 */     System.out.println(this.transform);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.AdobeMarkerSegment
 * JD-Core Version:    0.6.2
 */
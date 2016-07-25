/*     */ package com.sun.imageio.plugins.jpeg;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadataNode;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ class COMMarkerSegment extends MarkerSegment
/*     */ {
/*     */   private static final String ENCODING = "ISO-8859-1";
/*     */ 
/*     */   COMMarkerSegment(JPEGBuffer paramJPEGBuffer)
/*     */     throws IOException
/*     */   {
/*  56 */     super(paramJPEGBuffer);
/*  57 */     loadData(paramJPEGBuffer);
/*     */   }
/*     */ 
/*     */   COMMarkerSegment(String paramString)
/*     */   {
/*  66 */     super(254);
/*  67 */     this.data = paramString.getBytes();
/*     */   }
/*     */ 
/*     */   COMMarkerSegment(Node paramNode)
/*     */     throws IIOInvalidTreeException
/*     */   {
/*  77 */     super(254);
/*     */     Object localObject;
/*  78 */     if ((paramNode instanceof IIOMetadataNode)) {
/*  79 */       localObject = (IIOMetadataNode)paramNode;
/*  80 */       this.data = ((byte[])((IIOMetadataNode)localObject).getUserObject());
/*     */     }
/*  82 */     if (this.data == null) {
/*  83 */       localObject = paramNode.getAttributes().getNamedItem("comment").getNodeValue();
/*     */ 
/*  85 */       if (localObject != null)
/*  86 */         this.data = ((String)localObject).getBytes();
/*     */       else
/*  88 */         throw new IIOInvalidTreeException("Empty comment node!", paramNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getComment()
/*     */   {
/*     */     try
/*     */     {
/* 100 */       return new String(this.data, "ISO-8859-1"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   IIOMetadataNode getNativeNode()
/*     */   {
/* 111 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("com");
/* 112 */     localIIOMetadataNode.setAttribute("comment", getComment());
/* 113 */     if (this.data != null) {
/* 114 */       localIIOMetadataNode.setUserObject(this.data.clone());
/*     */     }
/* 116 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   void write(ImageOutputStream paramImageOutputStream)
/*     */     throws IOException
/*     */   {
/* 124 */     this.length = (2 + this.data.length);
/* 125 */     writeTag(paramImageOutputStream);
/* 126 */     paramImageOutputStream.write(this.data);
/*     */   }
/*     */ 
/*     */   void print() {
/* 130 */     printTag("COM");
/* 131 */     System.out.println("<" + getComment() + ">");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.COMMarkerSegment
 * JD-Core Version:    0.6.2
 */
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
/*     */ class MarkerSegment
/*     */   implements Cloneable
/*     */ {
/*     */   protected static final int LENGTH_SIZE = 2;
/*     */   int tag;
/*     */   int length;
/*  49 */   byte[] data = null;
/*  50 */   boolean unknown = false;
/*     */ 
/*     */   MarkerSegment(JPEGBuffer paramJPEGBuffer)
/*     */     throws IOException
/*     */   {
/*  58 */     paramJPEGBuffer.loadBuf(3);
/*  59 */     this.tag = (paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF);
/*  60 */     this.length = ((paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF) << 8);
/*  61 */     this.length |= paramJPEGBuffer.buf[(paramJPEGBuffer.bufPtr++)] & 0xFF;
/*  62 */     this.length -= 2;
/*  63 */     paramJPEGBuffer.bufAvail -= 3;
/*     */ 
/*  66 */     paramJPEGBuffer.loadBuf(this.length);
/*     */   }
/*     */ 
/*     */   MarkerSegment(int paramInt)
/*     */   {
/*  74 */     this.tag = paramInt;
/*  75 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   MarkerSegment(Node paramNode)
/*     */     throws IIOInvalidTreeException
/*     */   {
/*  84 */     this.tag = getAttributeValue(paramNode, null, "MarkerTag", 0, 255, true);
/*     */ 
/*  89 */     this.length = 0;
/*     */ 
/*  91 */     if ((paramNode instanceof IIOMetadataNode)) {
/*  92 */       IIOMetadataNode localIIOMetadataNode = (IIOMetadataNode)paramNode;
/*     */       try {
/*  94 */         this.data = ((byte[])localIIOMetadataNode.getUserObject());
/*     */       } catch (Exception localException) {
/*  96 */         IIOInvalidTreeException localIIOInvalidTreeException = new IIOInvalidTreeException("Can't get User Object", paramNode);
/*     */ 
/*  99 */         localIIOInvalidTreeException.initCause(localException);
/* 100 */         throw localIIOInvalidTreeException;
/*     */       }
/*     */     } else {
/* 103 */       throw new IIOInvalidTreeException("Node must have User Object", paramNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object clone()
/*     */   {
/* 112 */     MarkerSegment localMarkerSegment = null;
/*     */     try {
/* 114 */       localMarkerSegment = (MarkerSegment)super.clone(); } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 116 */     if (this.data != null) {
/* 117 */       localMarkerSegment.data = ((byte[])this.data.clone());
/*     */     }
/* 119 */     return localMarkerSegment;
/*     */   }
/*     */ 
/*     */   void loadData(JPEGBuffer paramJPEGBuffer)
/*     */     throws IOException
/*     */   {
/* 127 */     this.data = new byte[this.length];
/* 128 */     paramJPEGBuffer.readData(this.data);
/*     */   }
/*     */ 
/*     */   IIOMetadataNode getNativeNode() {
/* 132 */     IIOMetadataNode localIIOMetadataNode = new IIOMetadataNode("unknown");
/* 133 */     localIIOMetadataNode.setAttribute("MarkerTag", Integer.toString(this.tag));
/* 134 */     localIIOMetadataNode.setUserObject(this.data);
/*     */ 
/* 136 */     return localIIOMetadataNode;
/*     */   }
/*     */ 
/*     */   static int getAttributeValue(Node paramNode, NamedNodeMap paramNamedNodeMap, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 146 */     if (paramNamedNodeMap == null) {
/* 147 */       paramNamedNodeMap = paramNode.getAttributes();
/*     */     }
/* 149 */     String str = paramNamedNodeMap.getNamedItem(paramString).getNodeValue();
/* 150 */     int i = -1;
/* 151 */     if (str == null) {
/* 152 */       if (paramBoolean)
/* 153 */         throw new IIOInvalidTreeException(paramString + " attribute not found", paramNode);
/*     */     }
/*     */     else
/*     */     {
/* 157 */       i = Integer.parseInt(str);
/* 158 */       if ((i < paramInt1) || (i > paramInt2)) {
/* 159 */         throw new IIOInvalidTreeException(paramString + " attribute out of range", paramNode);
/*     */       }
/*     */     }
/*     */ 
/* 163 */     return i;
/*     */   }
/*     */ 
/*     */   void writeTag(ImageOutputStream paramImageOutputStream)
/*     */     throws IOException
/*     */   {
/* 172 */     paramImageOutputStream.write(255);
/* 173 */     paramImageOutputStream.write(this.tag);
/* 174 */     write2bytes(paramImageOutputStream, this.length);
/*     */   }
/*     */ 
/*     */   void write(ImageOutputStream paramImageOutputStream)
/*     */     throws IOException
/*     */   {
/* 182 */     this.length = (2 + (this.data != null ? this.data.length : 0));
/* 183 */     writeTag(paramImageOutputStream);
/* 184 */     if (this.data != null)
/* 185 */       paramImageOutputStream.write(this.data);
/*     */   }
/*     */ 
/*     */   static void write2bytes(ImageOutputStream paramImageOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 191 */     paramImageOutputStream.write(paramInt >> 8 & 0xFF);
/* 192 */     paramImageOutputStream.write(paramInt & 0xFF);
/*     */   }
/*     */ 
/*     */   void printTag(String paramString)
/*     */   {
/* 197 */     System.out.println(paramString + " marker segment - marker = 0x" + Integer.toHexString(this.tag));
/*     */ 
/* 199 */     System.out.println("length: " + this.length);
/*     */   }
/*     */ 
/*     */   void print() {
/* 203 */     printTag("Unknown");
/*     */     int i;
/* 204 */     if (this.length > 10) {
/* 205 */       System.out.print("First 5 bytes:");
/* 206 */       for (i = 0; i < 5; i++) {
/* 207 */         System.out.print(" Ox" + Integer.toHexString(this.data[i]));
/*     */       }
/*     */ 
/* 210 */       System.out.print("\nLast 5 bytes:");
/* 211 */       for (i = this.data.length - 5; i < this.data.length; i++)
/* 212 */         System.out.print(" Ox" + Integer.toHexString(this.data[i]));
/*     */     }
/*     */     else
/*     */     {
/* 216 */       System.out.print("Data:");
/* 217 */       for (i = 0; i < this.data.length; i++) {
/* 218 */         System.out.print(" Ox" + Integer.toHexString(this.data[i]));
/*     */       }
/*     */     }
/*     */ 
/* 222 */     System.out.println();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.MarkerSegment
 * JD-Core Version:    0.6.2
 */
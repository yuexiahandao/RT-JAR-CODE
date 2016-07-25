/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import org.w3c.dom.ls.LSOutput;
/*     */ 
/*     */ public class DOMOutputImpl
/*     */   implements LSOutput
/*     */ {
/*  59 */   protected Writer fCharStream = null;
/*  60 */   protected OutputStream fByteStream = null;
/*  61 */   protected String fSystemId = null;
/*  62 */   protected String fEncoding = null;
/*     */ 
/*     */   public Writer getCharacterStream()
/*     */   {
/*  78 */     return this.fCharStream;
/*     */   }
/*     */ 
/*     */   public void setCharacterStream(Writer characterStream)
/*     */   {
/*  90 */     this.fCharStream = characterStream;
/*     */   }
/*     */ 
/*     */   public OutputStream getByteStream()
/*     */   {
/* 102 */     return this.fByteStream;
/*     */   }
/*     */ 
/*     */   public void setByteStream(OutputStream byteStream)
/*     */   {
/* 114 */     this.fByteStream = byteStream;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 127 */     return this.fSystemId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 140 */     this.fSystemId = systemId;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 155 */     return this.fEncoding;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 170 */     this.fEncoding = encoding;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMOutputImpl
 * JD-Core Version:    0.6.2
 */
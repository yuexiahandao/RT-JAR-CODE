/*     */ package com.sun.xml.internal.stream.buffer;
/*     */ 
/*     */ import com.sun.xml.internal.stream.buffer.sax.SAXBufferCreator;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ 
/*     */ public class XMLStreamBufferResult extends SAXResult
/*     */ {
/*     */   protected MutableXMLStreamBuffer _buffer;
/*     */   protected SAXBufferCreator _bufferCreator;
/*     */ 
/*     */   public XMLStreamBufferResult()
/*     */   {
/*  66 */     setXMLStreamBuffer(new MutableXMLStreamBuffer());
/*     */   }
/*     */ 
/*     */   public XMLStreamBufferResult(MutableXMLStreamBuffer buffer)
/*     */   {
/*  75 */     setXMLStreamBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public MutableXMLStreamBuffer getXMLStreamBuffer()
/*     */   {
/*  84 */     return this._buffer;
/*     */   }
/*     */ 
/*     */   public void setXMLStreamBuffer(MutableXMLStreamBuffer buffer)
/*     */   {
/*  93 */     if (buffer == null) {
/*  94 */       throw new NullPointerException("buffer cannot be null");
/*     */     }
/*  96 */     this._buffer = buffer;
/*  97 */     setSystemId(this._buffer.getSystemId());
/*     */ 
/*  99 */     if (this._bufferCreator != null)
/* 100 */       this._bufferCreator.setXMLStreamBuffer(this._buffer);
/*     */   }
/*     */ 
/*     */   public ContentHandler getHandler()
/*     */   {
/* 105 */     if (this._bufferCreator == null) {
/* 106 */       this._bufferCreator = new SAXBufferCreator(this._buffer);
/* 107 */       setHandler(this._bufferCreator);
/* 108 */     } else if (super.getHandler() == null) {
/* 109 */       setHandler(this._bufferCreator);
/*     */     }
/*     */ 
/* 112 */     return this._bufferCreator;
/*     */   }
/*     */ 
/*     */   public LexicalHandler getLexicalHandler() {
/* 116 */     return (LexicalHandler)getHandler();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.XMLStreamBufferResult
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.xml.internal.stream.buffer;
/*     */ 
/*     */ import com.sun.xml.internal.stream.buffer.sax.SAXBufferProcessor;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class XMLStreamBufferSource extends SAXSource
/*     */ {
/*     */   protected XMLStreamBuffer _buffer;
/*     */   protected SAXBufferProcessor _bufferProcessor;
/*     */ 
/*     */   public XMLStreamBufferSource(XMLStreamBuffer buffer)
/*     */   {
/*  63 */     super(new InputSource(new ByteArrayInputStream(new byte[0])));
/*     */ 
/*  65 */     setXMLStreamBuffer(buffer);
/*     */   }
/*     */ 
/*     */   public XMLStreamBuffer getXMLStreamBuffer()
/*     */   {
/*  74 */     return this._buffer;
/*     */   }
/*     */ 
/*     */   public void setXMLStreamBuffer(XMLStreamBuffer buffer)
/*     */   {
/*  83 */     if (buffer == null) {
/*  84 */       throw new NullPointerException("buffer cannot be null");
/*     */     }
/*  86 */     this._buffer = buffer;
/*     */ 
/*  88 */     if (this._bufferProcessor != null)
/*  89 */       this._bufferProcessor.setBuffer(this._buffer, false);
/*     */   }
/*     */ 
/*     */   public XMLReader getXMLReader()
/*     */   {
/*  94 */     if (this._bufferProcessor == null) {
/*  95 */       this._bufferProcessor = new SAXBufferProcessor(this._buffer, false);
/*  96 */       setXMLReader(this._bufferProcessor);
/*  97 */     } else if (super.getXMLReader() == null) {
/*  98 */       setXMLReader(this._bufferProcessor);
/*     */     }
/*     */ 
/* 101 */     return this._bufferProcessor;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.XMLStreamBufferSource
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public final class SAXInputSource extends XMLInputSource
/*     */ {
/*     */   private XMLReader fXMLReader;
/*     */   private InputSource fInputSource;
/*     */ 
/*     */   public SAXInputSource()
/*     */   {
/*  40 */     this(null);
/*     */   }
/*     */ 
/*     */   public SAXInputSource(InputSource inputSource) {
/*  44 */     this(null, inputSource);
/*     */   }
/*     */ 
/*     */   public SAXInputSource(XMLReader reader, InputSource inputSource) {
/*  48 */     super(inputSource != null ? inputSource.getPublicId() : null, inputSource != null ? inputSource.getSystemId() : null, null);
/*     */ 
/*  50 */     if (inputSource != null) {
/*  51 */       setByteStream(inputSource.getByteStream());
/*  52 */       setCharacterStream(inputSource.getCharacterStream());
/*  53 */       setEncoding(inputSource.getEncoding());
/*     */     }
/*  55 */     this.fInputSource = inputSource;
/*  56 */     this.fXMLReader = reader;
/*     */   }
/*     */ 
/*     */   public void setXMLReader(XMLReader reader) {
/*  60 */     this.fXMLReader = reader;
/*     */   }
/*     */ 
/*     */   public XMLReader getXMLReader() {
/*  64 */     return this.fXMLReader;
/*     */   }
/*     */ 
/*     */   public void setInputSource(InputSource inputSource) {
/*  68 */     if (inputSource != null) {
/*  69 */       setPublicId(inputSource.getPublicId());
/*  70 */       setSystemId(inputSource.getSystemId());
/*  71 */       setByteStream(inputSource.getByteStream());
/*  72 */       setCharacterStream(inputSource.getCharacterStream());
/*  73 */       setEncoding(inputSource.getEncoding());
/*     */     }
/*     */     else {
/*  76 */       setPublicId(null);
/*  77 */       setSystemId(null);
/*  78 */       setByteStream(null);
/*  79 */       setCharacterStream(null);
/*  80 */       setEncoding(null);
/*     */     }
/*  82 */     this.fInputSource = inputSource;
/*     */   }
/*     */ 
/*     */   public InputSource getInputSource() {
/*  86 */     return this.fInputSource;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/*  95 */     super.setPublicId(publicId);
/*  96 */     if (this.fInputSource == null) {
/*  97 */       this.fInputSource = new InputSource();
/*     */     }
/*  99 */     this.fInputSource.setPublicId(publicId);
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 108 */     super.setSystemId(systemId);
/* 109 */     if (this.fInputSource == null) {
/* 110 */       this.fInputSource = new InputSource();
/*     */     }
/* 112 */     this.fInputSource.setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public void setByteStream(InputStream byteStream)
/*     */   {
/* 125 */     super.setByteStream(byteStream);
/* 126 */     if (this.fInputSource == null) {
/* 127 */       this.fInputSource = new InputSource();
/*     */     }
/* 129 */     this.fInputSource.setByteStream(byteStream);
/*     */   }
/*     */ 
/*     */   public void setCharacterStream(Reader charStream)
/*     */   {
/* 144 */     super.setCharacterStream(charStream);
/* 145 */     if (this.fInputSource == null) {
/* 146 */       this.fInputSource = new InputSource();
/*     */     }
/* 148 */     this.fInputSource.setCharacterStream(charStream);
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 157 */     super.setEncoding(encoding);
/* 158 */     if (this.fInputSource == null) {
/* 159 */       this.fInputSource = new InputSource();
/*     */     }
/* 161 */     this.fInputSource.setEncoding(encoding);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.SAXInputSource
 * JD-Core Version:    0.6.2
 */
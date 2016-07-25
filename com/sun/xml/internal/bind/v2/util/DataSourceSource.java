/*     */ package com.sun.xml.internal.bind.v2.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.activation.DataSource;
/*     */ import javax.activation.MimeType;
/*     */ import javax.activation.MimeTypeParseException;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ 
/*     */ public final class DataSourceSource extends StreamSource
/*     */ {
/*     */   private final DataSource source;
/*     */   private final String charset;
/*     */   private Reader r;
/*     */   private InputStream is;
/*     */ 
/*     */   public DataSourceSource(DataHandler dh)
/*     */     throws MimeTypeParseException
/*     */   {
/*  69 */     this(dh.getDataSource());
/*     */   }
/*     */ 
/*     */   public DataSourceSource(DataSource source) throws MimeTypeParseException {
/*  73 */     this.source = source;
/*     */ 
/*  75 */     String ct = source.getContentType();
/*  76 */     if (ct == null) {
/*  77 */       this.charset = null;
/*     */     } else {
/*  79 */       MimeType mimeType = new MimeType(ct);
/*  80 */       this.charset = mimeType.getParameter("charset");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setReader(Reader reader)
/*     */   {
/*  86 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void setInputStream(InputStream inputStream)
/*     */   {
/*  91 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Reader getReader()
/*     */   {
/*     */     try {
/*  97 */       if (this.charset == null) return null;
/*  98 */       if (this.r == null)
/*  99 */         this.r = new InputStreamReader(this.source.getInputStream(), this.charset);
/* 100 */       return this.r;
/*     */     }
/*     */     catch (IOException e) {
/* 103 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */   {
/*     */     try {
/* 110 */       if (this.charset != null) return null;
/* 111 */       if (this.is == null)
/* 112 */         this.is = this.source.getInputStream();
/* 113 */       return this.is;
/*     */     }
/*     */     catch (IOException e) {
/* 116 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DataSource getDataSource() {
/* 121 */     return this.source;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.util.DataSourceSource
 * JD-Core Version:    0.6.2
 */
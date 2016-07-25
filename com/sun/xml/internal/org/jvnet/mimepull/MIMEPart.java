/*     */ package com.sun.xml.internal.org.jvnet.mimepull;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ 
/*     */ public class MIMEPart
/*     */ {
/*     */   private volatile InternetHeaders headers;
/*     */   private volatile String contentId;
/*     */   private String contentType;
/*     */   volatile boolean parsed;
/*     */   final MIMEMessage msg;
/*     */   private final DataHead dataHead;
/*     */ 
/*     */   MIMEPart(MIMEMessage msg)
/*     */   {
/*  53 */     this.msg = msg;
/*  54 */     this.dataHead = new DataHead(this);
/*     */   }
/*     */ 
/*     */   MIMEPart(MIMEMessage msg, String contentId) {
/*  58 */     this(msg);
/*  59 */     this.contentId = contentId;
/*     */   }
/*     */ 
/*     */   public InputStream read()
/*     */   {
/*  72 */     return this.dataHead.read();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  81 */     this.dataHead.close();
/*     */   }
/*     */ 
/*     */   public InputStream readOnce()
/*     */   {
/*  98 */     return this.dataHead.readOnce();
/*     */   }
/*     */ 
/*     */   public void moveTo(File f) {
/* 102 */     this.dataHead.moveTo(f);
/*     */   }
/*     */ 
/*     */   public String getContentId()
/*     */   {
/* 111 */     if (this.contentId == null) {
/* 112 */       getHeaders();
/*     */     }
/* 114 */     return this.contentId;
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 123 */     if (this.contentType == null) {
/* 124 */       getHeaders();
/*     */     }
/* 126 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   private void getHeaders()
/*     */   {
/* 131 */     while (this.headers == null)
/* 132 */       if ((!this.msg.makeProgress()) && 
/* 133 */         (this.headers == null))
/* 134 */         throw new IllegalStateException("Internal Error. Didn't get Headers even after complete parsing.");
/*     */   }
/*     */ 
/*     */   public List<String> getHeader(String name)
/*     */   {
/* 149 */     getHeaders();
/* 150 */     assert (this.headers != null);
/* 151 */     return this.headers.getHeader(name);
/*     */   }
/*     */ 
/*     */   public List<? extends Header> getAllHeaders()
/*     */   {
/* 160 */     getHeaders();
/* 161 */     assert (this.headers != null);
/* 162 */     return this.headers.getAllHeaders();
/*     */   }
/*     */ 
/*     */   void setHeaders(InternetHeaders headers)
/*     */   {
/* 171 */     this.headers = headers;
/* 172 */     List ct = getHeader("Content-Type");
/* 173 */     this.contentType = (ct == null ? "application/octet-stream" : (String)ct.get(0));
/*     */   }
/*     */ 
/*     */   void addBody(ByteBuffer buf)
/*     */   {
/* 182 */     this.dataHead.addBody(buf);
/*     */   }
/*     */ 
/*     */   void doneParsing()
/*     */   {
/* 190 */     this.parsed = true;
/* 191 */     this.dataHead.doneParsing();
/*     */   }
/*     */ 
/*     */   void setContentId(String cid)
/*     */   {
/* 199 */     this.contentId = cid;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 204 */     return "Part=" + this.contentId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.mimepull.MIMEPart
 * JD-Core Version:    0.6.2
 */
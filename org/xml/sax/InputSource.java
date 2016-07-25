/*     */ package org.xml.sax;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ 
/*     */ public class InputSource
/*     */ {
/*     */   private String publicId;
/*     */   private String systemId;
/*     */   private InputStream byteStream;
/*     */   private String encoding;
/*     */   private Reader characterStream;
/*     */ 
/*     */   public InputSource()
/*     */   {
/*     */   }
/*     */ 
/*     */   public InputSource(String systemId)
/*     */   {
/* 119 */     setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public InputSource(InputStream byteStream)
/*     */   {
/* 140 */     setByteStream(byteStream);
/*     */   }
/*     */ 
/*     */   public InputSource(Reader characterStream)
/*     */   {
/* 160 */     setCharacterStream(characterStream);
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/* 178 */     this.publicId = publicId;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 190 */     return this.publicId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 219 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 237 */     return this.systemId;
/*     */   }
/*     */ 
/*     */   public void setByteStream(InputStream byteStream)
/*     */   {
/* 260 */     this.byteStream = byteStream;
/*     */   }
/*     */ 
/*     */   public InputStream getByteStream()
/*     */   {
/* 276 */     return this.byteStream;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 297 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 313 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   public void setCharacterStream(Reader characterStream)
/*     */   {
/* 331 */     this.characterStream = characterStream;
/*     */   }
/*     */ 
/*     */   public Reader getCharacterStream()
/*     */   {
/* 343 */     return this.characterStream;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.InputSource
 * JD-Core Version:    0.6.2
 */
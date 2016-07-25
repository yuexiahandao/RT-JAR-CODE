/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ 
/*     */ public class DOMInputImpl
/*     */   implements LSInput
/*     */ {
/*  69 */   protected String fPublicId = null;
/*  70 */   protected String fSystemId = null;
/*  71 */   protected String fBaseSystemId = null;
/*     */ 
/*  73 */   protected InputStream fByteStream = null;
/*  74 */   protected Reader fCharStream = null;
/*  75 */   protected String fData = null;
/*     */ 
/*  77 */   protected String fEncoding = null;
/*     */ 
/*  79 */   protected boolean fCertifiedText = false;
/*     */ 
/*     */   public DOMInputImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DOMInputImpl(String publicId, String systemId, String baseSystemId)
/*     */   {
/* 107 */     this.fPublicId = publicId;
/* 108 */     this.fSystemId = systemId;
/* 109 */     this.fBaseSystemId = baseSystemId;
/*     */   }
/*     */ 
/*     */   public DOMInputImpl(String publicId, String systemId, String baseSystemId, InputStream byteStream, String encoding)
/*     */   {
/* 133 */     this.fPublicId = publicId;
/* 134 */     this.fSystemId = systemId;
/* 135 */     this.fBaseSystemId = baseSystemId;
/* 136 */     this.fByteStream = byteStream;
/* 137 */     this.fEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public DOMInputImpl(String publicId, String systemId, String baseSystemId, Reader charStream, String encoding)
/*     */   {
/* 162 */     this.fPublicId = publicId;
/* 163 */     this.fSystemId = systemId;
/* 164 */     this.fBaseSystemId = baseSystemId;
/* 165 */     this.fCharStream = charStream;
/* 166 */     this.fEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public DOMInputImpl(String publicId, String systemId, String baseSystemId, String data, String encoding)
/*     */   {
/* 190 */     this.fPublicId = publicId;
/* 191 */     this.fSystemId = systemId;
/* 192 */     this.fBaseSystemId = baseSystemId;
/* 193 */     this.fData = data;
/* 194 */     this.fEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public InputStream getByteStream()
/*     */   {
/* 209 */     return this.fByteStream;
/*     */   }
/*     */ 
/*     */   public void setByteStream(InputStream byteStream)
/*     */   {
/* 224 */     this.fByteStream = byteStream;
/*     */   }
/*     */ 
/*     */   public Reader getCharacterStream()
/*     */   {
/* 236 */     return this.fCharStream;
/*     */   }
/*     */ 
/*     */   public void setCharacterStream(Reader characterStream)
/*     */   {
/* 248 */     this.fCharStream = characterStream;
/*     */   }
/*     */ 
/*     */   public String getStringData()
/*     */   {
/* 259 */     return this.fData;
/*     */   }
/*     */ 
/*     */   public void setStringData(String stringData)
/*     */   {
/* 271 */     this.fData = stringData;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 286 */     return this.fEncoding;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 300 */     this.fEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 309 */     return this.fPublicId;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/* 317 */     this.fPublicId = publicId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 335 */     return this.fSystemId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 352 */     this.fSystemId = systemId;
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 361 */     return this.fBaseSystemId;
/*     */   }
/*     */ 
/*     */   public void setBaseURI(String baseURI)
/*     */   {
/* 369 */     this.fBaseSystemId = baseURI;
/*     */   }
/*     */ 
/*     */   public boolean getCertifiedText()
/*     */   {
/* 378 */     return this.fCertifiedText;
/*     */   }
/*     */ 
/*     */   public void setCertifiedText(boolean certifiedText)
/*     */   {
/* 388 */     this.fCertifiedText = certifiedText;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DOMInputImpl
 * JD-Core Version:    0.6.2
 */
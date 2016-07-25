/*     */ package com.sun.org.apache.xerces.internal.xni.parser;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ 
/*     */ public class XMLInputSource
/*     */ {
/*     */   protected String fPublicId;
/*     */   protected String fSystemId;
/*     */   protected String fBaseSystemId;
/*     */   protected InputStream fByteStream;
/*     */   protected Reader fCharStream;
/*     */   protected String fEncoding;
/*     */ 
/*     */   public XMLInputSource(String publicId, String systemId, String baseSystemId)
/*     */   {
/*  86 */     this.fPublicId = publicId;
/*  87 */     this.fSystemId = systemId;
/*  88 */     this.fBaseSystemId = baseSystemId;
/*     */   }
/*     */ 
/*     */   public XMLInputSource(XMLResourceIdentifier resourceIdentifier)
/*     */   {
/* 100 */     this.fPublicId = resourceIdentifier.getPublicId();
/* 101 */     this.fSystemId = resourceIdentifier.getLiteralSystemId();
/* 102 */     this.fBaseSystemId = resourceIdentifier.getBaseSystemId();
/*     */   }
/*     */ 
/*     */   public XMLInputSource(String publicId, String systemId, String baseSystemId, InputStream byteStream, String encoding)
/*     */   {
/* 123 */     this.fPublicId = publicId;
/* 124 */     this.fSystemId = systemId;
/* 125 */     this.fBaseSystemId = baseSystemId;
/* 126 */     this.fByteStream = byteStream;
/* 127 */     this.fEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public XMLInputSource(String publicId, String systemId, String baseSystemId, Reader charStream, String encoding)
/*     */   {
/* 149 */     this.fPublicId = publicId;
/* 150 */     this.fSystemId = systemId;
/* 151 */     this.fBaseSystemId = baseSystemId;
/* 152 */     this.fCharStream = charStream;
/* 153 */     this.fEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/* 166 */     this.fPublicId = publicId;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 171 */     return this.fPublicId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 180 */     this.fSystemId = systemId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 185 */     return this.fSystemId;
/*     */   }
/*     */ 
/*     */   public void setBaseSystemId(String baseSystemId)
/*     */   {
/* 194 */     this.fBaseSystemId = baseSystemId;
/*     */   }
/*     */ 
/*     */   public String getBaseSystemId()
/*     */   {
/* 199 */     return this.fBaseSystemId;
/*     */   }
/*     */ 
/*     */   public void setByteStream(InputStream byteStream)
/*     */   {
/* 212 */     this.fByteStream = byteStream;
/*     */   }
/*     */ 
/*     */   public InputStream getByteStream()
/*     */   {
/* 217 */     return this.fByteStream;
/*     */   }
/*     */ 
/*     */   public void setCharacterStream(Reader charStream)
/*     */   {
/* 232 */     this.fCharStream = charStream;
/*     */   }
/*     */ 
/*     */   public Reader getCharacterStream()
/*     */   {
/* 237 */     return this.fCharStream;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 246 */     this.fEncoding = encoding;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 251 */     return this.fEncoding;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource
 * JD-Core Version:    0.6.2
 */
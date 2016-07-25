/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class HTTPInputSource extends XMLInputSource
/*     */ {
/*  51 */   protected boolean fFollowRedirects = true;
/*     */ 
/*  54 */   protected Map fHTTPRequestProperties = new HashMap();
/*     */ 
/*     */   public HTTPInputSource(String publicId, String systemId, String baseSystemId)
/*     */   {
/*  76 */     super(publicId, systemId, baseSystemId);
/*     */   }
/*     */ 
/*     */   public HTTPInputSource(XMLResourceIdentifier resourceIdentifier)
/*     */   {
/*  87 */     super(resourceIdentifier);
/*     */   }
/*     */ 
/*     */   public HTTPInputSource(String publicId, String systemId, String baseSystemId, InputStream byteStream, String encoding)
/*     */   {
/* 107 */     super(publicId, systemId, baseSystemId, byteStream, encoding);
/*     */   }
/*     */ 
/*     */   public HTTPInputSource(String publicId, String systemId, String baseSystemId, Reader charStream, String encoding)
/*     */   {
/* 128 */     super(publicId, systemId, baseSystemId, charStream, encoding);
/*     */   }
/*     */ 
/*     */   public boolean getFollowHTTPRedirects()
/*     */   {
/* 140 */     return this.fFollowRedirects;
/*     */   }
/*     */ 
/*     */   public void setFollowHTTPRedirects(boolean followRedirects)
/*     */   {
/* 149 */     this.fFollowRedirects = followRedirects;
/*     */   }
/*     */ 
/*     */   public String getHTTPRequestProperty(String key)
/*     */   {
/* 162 */     return (String)this.fHTTPRequestProperties.get(key);
/*     */   }
/*     */ 
/*     */   public Iterator getHTTPRequestProperties()
/*     */   {
/* 176 */     return this.fHTTPRequestProperties.entrySet().iterator();
/*     */   }
/*     */ 
/*     */   public void setHTTPRequestProperty(String key, String value)
/*     */   {
/* 187 */     if (value != null) {
/* 188 */       this.fHTTPRequestProperties.put(key, value);
/*     */     }
/*     */     else
/* 191 */       this.fHTTPRequestProperties.remove(key);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.HTTPInputSource
 * JD-Core Version:    0.6.2
 */
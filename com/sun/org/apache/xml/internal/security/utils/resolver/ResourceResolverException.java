/*     */ package com.sun.org.apache.xml.internal.security.utils.resolver;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;
/*     */ import org.w3c.dom.Attr;
/*     */ 
/*     */ public class ResourceResolverException extends XMLSecurityException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/* 110 */   Attr _uri = null;
/*     */   String _BaseURI;
/*     */ 
/*     */   public ResourceResolverException(String paramString1, Attr paramAttr, String paramString2)
/*     */   {
/*  50 */     super(paramString1);
/*     */ 
/*  52 */     this._uri = paramAttr;
/*  53 */     this._BaseURI = paramString2;
/*     */   }
/*     */ 
/*     */   public ResourceResolverException(String paramString1, Object[] paramArrayOfObject, Attr paramAttr, String paramString2)
/*     */   {
/*  67 */     super(paramString1, paramArrayOfObject);
/*     */ 
/*  69 */     this._uri = paramAttr;
/*  70 */     this._BaseURI = paramString2;
/*     */   }
/*     */ 
/*     */   public ResourceResolverException(String paramString1, Exception paramException, Attr paramAttr, String paramString2)
/*     */   {
/*  84 */     super(paramString1, paramException);
/*     */ 
/*  86 */     this._uri = paramAttr;
/*  87 */     this._BaseURI = paramString2;
/*     */   }
/*     */ 
/*     */   public ResourceResolverException(String paramString1, Object[] paramArrayOfObject, Exception paramException, Attr paramAttr, String paramString2)
/*     */   {
/* 103 */     super(paramString1, paramArrayOfObject, paramException);
/*     */ 
/* 105 */     this._uri = paramAttr;
/* 106 */     this._BaseURI = paramString2;
/*     */   }
/*     */ 
/*     */   public void setURI(Attr paramAttr)
/*     */   {
/* 116 */     this._uri = paramAttr;
/*     */   }
/*     */ 
/*     */   public Attr getURI()
/*     */   {
/* 124 */     return this._uri;
/*     */   }
/*     */ 
/*     */   public void setBaseURI(String paramString)
/*     */   {
/* 134 */     this._BaseURI = paramString;
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 142 */     return this._BaseURI;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException
 * JD-Core Version:    0.6.2
 */
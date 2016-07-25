/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class EntityResolverWrapper
/*     */   implements XMLEntityResolver
/*     */ {
/*     */   protected EntityResolver fEntityResolver;
/*     */ 
/*     */   public EntityResolverWrapper()
/*     */   {
/*     */   }
/*     */ 
/*     */   public EntityResolverWrapper(EntityResolver entityResolver)
/*     */   {
/*  63 */     setEntityResolver(entityResolver);
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver entityResolver)
/*     */   {
/*  72 */     this.fEntityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */   public EntityResolver getEntityResolver()
/*     */   {
/*  77 */     return this.fEntityResolver;
/*     */   }
/*     */ 
/*     */   public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
/*     */     throws XNIException, IOException
/*     */   {
/* 102 */     String pubId = resourceIdentifier.getPublicId();
/* 103 */     String sysId = resourceIdentifier.getExpandedSystemId();
/* 104 */     if ((pubId == null) && (sysId == null)) {
/* 105 */       return null;
/*     */     }
/*     */ 
/* 108 */     if ((this.fEntityResolver != null) && (resourceIdentifier != null)) {
/*     */       try {
/* 110 */         InputSource inputSource = this.fEntityResolver.resolveEntity(pubId, sysId);
/* 111 */         if (inputSource != null) {
/* 112 */           String publicId = inputSource.getPublicId();
/* 113 */           String systemId = inputSource.getSystemId();
/* 114 */           String baseSystemId = resourceIdentifier.getBaseSystemId();
/* 115 */           InputStream byteStream = inputSource.getByteStream();
/* 116 */           Reader charStream = inputSource.getCharacterStream();
/* 117 */           String encoding = inputSource.getEncoding();
/* 118 */           XMLInputSource xmlInputSource = new XMLInputSource(publicId, systemId, baseSystemId);
/*     */ 
/* 120 */           xmlInputSource.setByteStream(byteStream);
/* 121 */           xmlInputSource.setCharacterStream(charStream);
/* 122 */           xmlInputSource.setEncoding(encoding);
/* 123 */           return xmlInputSource;
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (SAXException e)
/*     */       {
/* 129 */         Exception ex = e.getException();
/* 130 */         if (ex == null) {
/* 131 */           ex = e;
/*     */         }
/* 133 */         throw new XNIException(ex);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 138 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.EntityResolverWrapper
 * JD-Core Version:    0.6.2
 */
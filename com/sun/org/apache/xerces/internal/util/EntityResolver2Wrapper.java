/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.ExternalSubsetResolver;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLDTDDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.EntityResolver2;
/*     */ 
/*     */ public class EntityResolver2Wrapper
/*     */   implements ExternalSubsetResolver
/*     */ {
/*     */   protected EntityResolver2 fEntityResolver;
/*     */ 
/*     */   public EntityResolver2Wrapper()
/*     */   {
/*     */   }
/*     */ 
/*     */   public EntityResolver2Wrapper(EntityResolver2 entityResolver)
/*     */   {
/*  67 */     setEntityResolver(entityResolver);
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver2 entityResolver)
/*     */   {
/*  80 */     this.fEntityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */   public EntityResolver2 getEntityResolver()
/*     */   {
/*  89 */     return this.fEntityResolver;
/*     */   }
/*     */ 
/*     */   public XMLInputSource getExternalSubset(XMLDTDDescription grammarDescription)
/*     */     throws XNIException, IOException
/*     */   {
/* 110 */     if (this.fEntityResolver != null)
/*     */     {
/* 112 */       String name = grammarDescription.getRootName();
/* 113 */       String baseURI = grammarDescription.getBaseSystemId();
/*     */       try
/*     */       {
/* 117 */         InputSource inputSource = this.fEntityResolver.getExternalSubset(name, baseURI);
/* 118 */         return inputSource != null ? createXMLInputSource(inputSource, baseURI) : null;
/*     */       }
/*     */       catch (SAXException e)
/*     */       {
/* 122 */         Exception ex = e.getException();
/* 123 */         if (ex == null) {
/* 124 */           ex = e;
/*     */         }
/* 126 */         throw new XNIException(ex);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
/*     */     throws XNIException, IOException
/*     */   {
/* 152 */     if (this.fEntityResolver != null)
/*     */     {
/* 154 */       String pubId = resourceIdentifier.getPublicId();
/* 155 */       String sysId = resourceIdentifier.getLiteralSystemId();
/* 156 */       String baseURI = resourceIdentifier.getBaseSystemId();
/* 157 */       String name = null;
/* 158 */       if ((resourceIdentifier instanceof XMLDTDDescription)) {
/* 159 */         name = "[dtd]";
/*     */       }
/* 161 */       else if ((resourceIdentifier instanceof XMLEntityDescription)) {
/* 162 */         name = ((XMLEntityDescription)resourceIdentifier).getEntityName();
/*     */       }
/*     */ 
/* 170 */       if ((pubId == null) && (sysId == null)) {
/* 171 */         return null;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 176 */         InputSource inputSource = this.fEntityResolver.resolveEntity(name, pubId, baseURI, sysId);
/*     */ 
/* 178 */         return inputSource != null ? createXMLInputSource(inputSource, baseURI) : null;
/*     */       }
/*     */       catch (SAXException e)
/*     */       {
/* 182 */         Exception ex = e.getException();
/* 183 */         if (ex == null) {
/* 184 */           ex = e;
/*     */         }
/* 186 */         throw new XNIException(ex);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */   private XMLInputSource createXMLInputSource(InputSource source, String baseURI)
/*     */   {
/* 200 */     String publicId = source.getPublicId();
/* 201 */     String systemId = source.getSystemId();
/* 202 */     String baseSystemId = baseURI;
/* 203 */     InputStream byteStream = source.getByteStream();
/* 204 */     Reader charStream = source.getCharacterStream();
/* 205 */     String encoding = source.getEncoding();
/* 206 */     XMLInputSource xmlInputSource = new XMLInputSource(publicId, systemId, baseSystemId);
/*     */ 
/* 208 */     xmlInputSource.setByteStream(byteStream);
/* 209 */     xmlInputSource.setCharacterStream(charStream);
/* 210 */     xmlInputSource.setEncoding(encoding);
/* 211 */     return xmlInputSource;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.EntityResolver2Wrapper
 * JD-Core Version:    0.6.2
 */
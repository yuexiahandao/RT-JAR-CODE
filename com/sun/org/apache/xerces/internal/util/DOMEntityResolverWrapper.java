/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ 
/*     */ public class DOMEntityResolverWrapper
/*     */   implements XMLEntityResolver
/*     */ {
/*     */   private static final String XML_TYPE = "http://www.w3.org/TR/REC-xml";
/*     */   private static final String XSD_TYPE = "http://www.w3.org/2001/XMLSchema";
/*     */   protected LSResourceResolver fEntityResolver;
/*     */ 
/*     */   public DOMEntityResolverWrapper()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DOMEntityResolverWrapper(LSResourceResolver entityResolver)
/*     */   {
/*  73 */     setEntityResolver(entityResolver);
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(LSResourceResolver entityResolver)
/*     */   {
/*  82 */     this.fEntityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */   public LSResourceResolver getEntityResolver()
/*     */   {
/*  87 */     return this.fEntityResolver;
/*     */   }
/*     */ 
/*     */   public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
/*     */     throws XNIException, IOException
/*     */   {
/* 106 */     if (this.fEntityResolver != null)
/*     */     {
/* 109 */       LSInput inputSource = resourceIdentifier == null ? this.fEntityResolver.resolveResource(null, null, null, null, null) : this.fEntityResolver.resolveResource(getType(resourceIdentifier), resourceIdentifier.getNamespace(), resourceIdentifier.getPublicId(), resourceIdentifier.getLiteralSystemId(), resourceIdentifier.getBaseSystemId());
/*     */ 
/* 123 */       if (inputSource != null) {
/* 124 */         String publicId = inputSource.getPublicId();
/* 125 */         String systemId = inputSource.getSystemId();
/* 126 */         String baseSystemId = inputSource.getBaseURI();
/* 127 */         InputStream byteStream = inputSource.getByteStream();
/* 128 */         Reader charStream = inputSource.getCharacterStream();
/* 129 */         String encoding = inputSource.getEncoding();
/* 130 */         String data = inputSource.getStringData();
/*     */ 
/* 137 */         XMLInputSource xmlInputSource = new XMLInputSource(publicId, systemId, baseSystemId);
/*     */ 
/* 140 */         if (charStream != null) {
/* 141 */           xmlInputSource.setCharacterStream(charStream);
/*     */         }
/* 143 */         else if (byteStream != null) {
/* 144 */           xmlInputSource.setByteStream(byteStream);
/*     */         }
/* 146 */         else if ((data != null) && (data.length() != 0)) {
/* 147 */           xmlInputSource.setCharacterStream(new StringReader(data));
/*     */         }
/* 149 */         xmlInputSource.setEncoding(encoding);
/* 150 */         return xmlInputSource;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */   private String getType(XMLResourceIdentifier resourceIdentifier)
/*     */   {
/* 161 */     if ((resourceIdentifier instanceof XMLGrammarDescription)) {
/* 162 */       XMLGrammarDescription desc = (XMLGrammarDescription)resourceIdentifier;
/* 163 */       if ("http://www.w3.org/2001/XMLSchema".equals(desc.getGrammarType())) {
/* 164 */         return "http://www.w3.org/2001/XMLSchema";
/*     */       }
/*     */     }
/* 167 */     return "http://www.w3.org/TR/REC-xml";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.DOMEntityResolverWrapper
 * JD-Core Version:    0.6.2
 */
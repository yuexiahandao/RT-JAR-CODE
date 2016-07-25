/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*     */ import java.io.IOException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public abstract class XMLParser
/*     */ {
/*     */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*     */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*  71 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/error-handler" };
/*     */   protected XMLParserConfiguration fConfiguration;
/*     */   XMLSecurityManager securityManager;
/*     */   XMLSecurityPropertyManager securityPropertyManager;
/*     */ 
/*     */   public boolean getFeature(String featureId)
/*     */     throws SAXNotSupportedException, SAXNotRecognizedException
/*     */   {
/*  99 */     return this.fConfiguration.getFeature(featureId);
/*     */   }
/*     */ 
/*     */   protected XMLParser(XMLParserConfiguration config)
/*     */   {
/* 109 */     this.fConfiguration = config;
/*     */ 
/* 112 */     this.fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
/*     */   }
/*     */ 
/*     */   public void parse(XMLInputSource inputSource)
/*     */     throws XNIException, IOException
/*     */   {
/* 131 */     if (this.securityManager == null) {
/* 132 */       this.securityManager = new XMLSecurityManager(true);
/* 133 */       this.fConfiguration.setProperty("http://apache.org/xml/properties/security-manager", this.securityManager);
/*     */     }
/* 135 */     if (this.securityPropertyManager == null) {
/* 136 */       this.securityPropertyManager = new XMLSecurityPropertyManager();
/* 137 */       this.fConfiguration.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.securityPropertyManager);
/*     */     }
/*     */ 
/* 140 */     reset();
/* 141 */     this.fConfiguration.parse(inputSource);
/*     */   }
/*     */ 
/*     */   protected void reset()
/*     */     throws XNIException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XMLParser
 * JD-Core Version:    0.6.2
 */
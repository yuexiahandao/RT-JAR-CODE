/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.Limit;
/*     */ import java.io.IOException;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.TransformerFactoryConfigurationError;
/*     */ import javax.xml.transform.sax.SAXResult;
/*     */ import javax.xml.transform.sax.SAXTransformerFactory;
/*     */ import javax.xml.transform.sax.TransformerHandler;
/*     */ import javax.xml.transform.stax.StAXResult;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class StAXValidatorHelper
/*     */   implements ValidatorHelper
/*     */ {
/*     */   private static final String DEFAULT_TRANSFORMER_IMPL = "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";
/*     */   private XMLSchemaValidatorComponentManager fComponentManager;
/*  58 */   private Transformer identityTransformer1 = null;
/*  59 */   private TransformerHandler identityTransformer2 = null;
/*  60 */   private ValidatorHandlerImpl handler = null;
/*     */ 
/*     */   public StAXValidatorHelper(XMLSchemaValidatorComponentManager componentManager)
/*     */   {
/*  64 */     this.fComponentManager = componentManager;
/*     */   }
/*     */ 
/*     */   public void validate(Source source, Result result)
/*     */     throws SAXException, IOException
/*     */   {
/*  70 */     if ((result == null) || ((result instanceof StAXResult)))
/*     */     {
/*  72 */       if (this.identityTransformer1 == null) {
/*     */         try {
/*  74 */           SAXTransformerFactory tf = this.fComponentManager.getFeature("http://www.oracle.com/feature/use-service-mechanism") ? (SAXTransformerFactory)SAXTransformerFactory.newInstance() : (SAXTransformerFactory)TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", StAXValidatorHelper.class.getClassLoader());
/*     */ 
/*  77 */           XMLSecurityManager securityManager = (XMLSecurityManager)this.fComponentManager.getProperty("http://apache.org/xml/properties/security-manager");
/*  78 */           if (securityManager != null) {
/*  79 */             for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
/*  80 */               if (securityManager.isSet(limit.ordinal())) {
/*  81 */                 tf.setAttribute(limit.apiProperty(), securityManager.getLimitValueAsString(limit));
/*     */               }
/*     */             }
/*     */ 
/*  85 */             if (securityManager.printEntityCountInfo()) {
/*  86 */               tf.setAttribute("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo", "yes");
/*     */             }
/*     */           }
/*     */ 
/*  90 */           this.identityTransformer1 = tf.newTransformer();
/*  91 */           this.identityTransformer2 = tf.newTransformerHandler();
/*     */         }
/*     */         catch (TransformerConfigurationException e) {
/*  94 */           throw new TransformerFactoryConfigurationError(e);
/*     */         }
/*     */       }
/*     */ 
/*  98 */       this.handler = new ValidatorHandlerImpl(this.fComponentManager);
/*  99 */       if (result != null) {
/* 100 */         this.handler.setContentHandler(this.identityTransformer2);
/* 101 */         this.identityTransformer2.setResult(result);
/*     */       }
/*     */       try
/*     */       {
/* 105 */         this.identityTransformer1.transform(source, new SAXResult(this.handler));
/*     */       } catch (TransformerException e) {
/* 107 */         if ((e.getException() instanceof SAXException))
/* 108 */           throw ((SAXException)e.getException());
/* 109 */         throw new SAXException(e);
/*     */       } finally {
/* 111 */         this.handler.setContentHandler(null);
/*     */       }
/* 113 */       return;
/*     */     }
/* 115 */     throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[] { source.getClass().getName(), result.getClass().getName() }));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.StAXValidatorHelper
 * JD-Core Version:    0.6.2
 */
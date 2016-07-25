/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator;
/*     */ import com.sun.org.apache.xerces.internal.parsers.XML11Configuration;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.SoftReference;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.TransformerFactoryConfigurationError;
/*     */ import javax.xml.transform.sax.SAXTransformerFactory;
/*     */ import javax.xml.transform.sax.TransformerHandler;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class StreamValidatorHelper
/*     */   implements ValidatorHelper
/*     */ {
/*     */   private static final String PARSER_SETTINGS = "http://apache.org/xml/features/internal/parser-settings";
/*     */   private static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*     */   private static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*     */   private static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   private static final String SCHEMA_VALIDATOR = "http://apache.org/xml/properties/internal/validator/schema";
/*     */   private static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   private static final String VALIDATION_MANAGER = "http://apache.org/xml/properties/internal/validation-manager";
/*     */   private static final String DEFAULT_TRANSFORMER_IMPL = "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*  98 */   private SoftReference fConfiguration = new SoftReference(null);
/*     */   private XMLSchemaValidator fSchemaValidator;
/*     */   private XMLSchemaValidatorComponentManager fComponentManager;
/* 106 */   private ValidatorHandlerImpl handler = null;
/*     */ 
/*     */   public StreamValidatorHelper(XMLSchemaValidatorComponentManager componentManager) {
/* 109 */     this.fComponentManager = componentManager;
/* 110 */     this.fSchemaValidator = ((XMLSchemaValidator)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validator/schema"));
/*     */   }
/*     */ 
/*     */   public void validate(Source source, Result result) throws SAXException, IOException
/*     */   {
/* 115 */     if ((result == null) || ((result instanceof StreamResult))) {
/* 116 */       StreamSource streamSource = (StreamSource)source;
/*     */ 
/* 119 */       if (result != null) {
/*     */         TransformerHandler identityTransformerHandler;
/*     */         try { SAXTransformerFactory tf = this.fComponentManager.getFeature("http://www.oracle.com/feature/use-service-mechanism") ? (SAXTransformerFactory)SAXTransformerFactory.newInstance() : (SAXTransformerFactory)TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", StreamValidatorHelper.class.getClassLoader());
/*     */ 
/* 124 */           identityTransformerHandler = tf.newTransformerHandler();
/*     */         } catch (TransformerConfigurationException e) {
/* 126 */           throw new TransformerFactoryConfigurationError(e);
/*     */         }
/*     */ 
/* 129 */         this.handler = new ValidatorHandlerImpl(this.fComponentManager);
/* 130 */         this.handler.setContentHandler(identityTransformerHandler);
/* 131 */         identityTransformerHandler.setResult(result);
/*     */       }
/*     */ 
/* 134 */       XMLInputSource input = new XMLInputSource(streamSource.getPublicId(), streamSource.getSystemId(), null);
/* 135 */       input.setByteStream(streamSource.getInputStream());
/* 136 */       input.setCharacterStream(streamSource.getReader());
/*     */ 
/* 140 */       XMLParserConfiguration config = (XMLParserConfiguration)this.fConfiguration.get();
/* 141 */       if (config == null) {
/* 142 */         config = initialize();
/*     */       }
/* 145 */       else if (this.fComponentManager.getFeature("http://apache.org/xml/features/internal/parser-settings")) {
/* 146 */         config.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver"));
/* 147 */         config.setProperty("http://apache.org/xml/properties/internal/error-handler", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-handler"));
/*     */       }
/*     */ 
/* 151 */       this.fComponentManager.reset();
/* 152 */       this.fSchemaValidator.setDocumentHandler(this.handler);
/*     */       try
/*     */       {
/* 155 */         config.parse(input);
/*     */       }
/*     */       catch (XMLParseException e) {
/* 158 */         throw Util.toSAXParseException(e);
/*     */       }
/*     */       catch (XNIException e) {
/* 161 */         throw Util.toSAXException(e);
/*     */       }
/* 163 */       return;
/*     */     }
/* 165 */     throw new IllegalArgumentException(JAXPValidationMessageFormatter.formatMessage(this.fComponentManager.getLocale(), "SourceResultMismatch", new Object[] { source.getClass().getName(), result.getClass().getName() }));
/*     */   }
/*     */ 
/*     */   private XMLParserConfiguration initialize()
/*     */   {
/* 171 */     XML11Configuration config = new XML11Configuration();
/* 172 */     if (this.fComponentManager.getFeature("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 173 */       config.setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager());
/*     */     }
/* 175 */     config.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/entity-resolver"));
/* 176 */     config.setProperty("http://apache.org/xml/properties/internal/error-handler", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-handler"));
/* 177 */     XMLErrorReporter errorReporter = (XMLErrorReporter)this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/error-reporter");
/* 178 */     config.setProperty("http://apache.org/xml/properties/internal/error-reporter", errorReporter);
/*     */ 
/* 180 */     if (errorReporter.getMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210") == null) {
/* 181 */       XMLMessageFormatter xmft = new XMLMessageFormatter();
/* 182 */       errorReporter.putMessageFormatter("http://www.w3.org/TR/1998/REC-xml-19980210", xmft);
/* 183 */       errorReporter.putMessageFormatter("http://www.w3.org/TR/1999/REC-xml-names-19990114", xmft);
/*     */     }
/* 185 */     config.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table"));
/* 186 */     config.setProperty("http://apache.org/xml/properties/internal/validation-manager", this.fComponentManager.getProperty("http://apache.org/xml/properties/internal/validation-manager"));
/* 187 */     config.setDocumentHandler(this.fSchemaValidator);
/* 188 */     config.setDTDHandler(null);
/* 189 */     config.setDTDContentModelHandler(null);
/* 190 */     config.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fComponentManager.getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"));
/*     */ 
/* 192 */     config.setProperty("http://apache.org/xml/properties/security-manager", this.fComponentManager.getProperty("http://apache.org/xml/properties/security-manager"));
/*     */ 
/* 194 */     this.fConfiguration = new SoftReference(config);
/* 195 */     return config;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.StreamValidatorHelper
 * JD-Core Version:    0.6.2
 */
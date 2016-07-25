/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.DTDGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDLoader;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.XSMessageFormatter;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class XMLGrammarCachingConfiguration extends XIncludeAwareParserConfiguration
/*     */ {
/*     */   public static final int BIG_PRIME = 2039;
/*  81 */   protected static final SynchronizedSymbolTable fStaticSymbolTable = new SynchronizedSymbolTable(2039);
/*     */ 
/*  85 */   protected static final XMLGrammarPoolImpl fStaticGrammarPool = new XMLGrammarPoolImpl();
/*     */   protected static final String SCHEMA_FULL_CHECKING = "http://apache.org/xml/features/validation/schema-full-checking";
/*     */   protected XMLSchemaLoader fSchemaLoader;
/*     */   protected XMLDTDLoader fDTDLoader;
/*     */ 
/*     */   public XMLGrammarCachingConfiguration()
/*     */   {
/* 106 */     this(fStaticSymbolTable, fStaticGrammarPool, null);
/*     */   }
/*     */ 
/*     */   public XMLGrammarCachingConfiguration(SymbolTable symbolTable)
/*     */   {
/* 115 */     this(symbolTable, fStaticGrammarPool, null);
/*     */   }
/*     */ 
/*     */   public XMLGrammarCachingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 131 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public XMLGrammarCachingConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 149 */     super(symbolTable, grammarPool, parentSettings);
/*     */ 
/* 158 */     this.fSchemaLoader = new XMLSchemaLoader(this.fSymbolTable);
/* 159 */     this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*     */ 
/* 162 */     this.fDTDLoader = new XMLDTDLoader(this.fSymbolTable, this.fGrammarPool);
/*     */   }
/*     */ 
/*     */   public void lockGrammarPool()
/*     */   {
/* 174 */     this.fGrammarPool.lockPool();
/*     */   }
/*     */ 
/*     */   public void clearGrammarPool()
/*     */   {
/* 182 */     this.fGrammarPool.clear();
/*     */   }
/*     */ 
/*     */   public void unlockGrammarPool()
/*     */   {
/* 190 */     this.fGrammarPool.unlockPool();
/*     */   }
/*     */ 
/*     */   public Grammar parseGrammar(String type, String uri)
/*     */     throws XNIException, IOException
/*     */   {
/* 209 */     XMLInputSource source = new XMLInputSource(null, uri, null);
/* 210 */     return parseGrammar(type, source);
/*     */   }
/*     */ 
/*     */   public Grammar parseGrammar(String type, XMLInputSource is)
/*     */     throws XNIException, IOException
/*     */   {
/* 232 */     if (type.equals("http://www.w3.org/2001/XMLSchema"))
/*     */     {
/* 234 */       return parseXMLSchema(is);
/* 235 */     }if (type.equals("http://www.w3.org/TR/REC-xml")) {
/* 236 */       return parseDTD(is);
/*     */     }
/*     */ 
/* 239 */     return null;
/*     */   }
/*     */ 
/*     */   SchemaGrammar parseXMLSchema(XMLInputSource is)
/*     */     throws IOException
/*     */   {
/* 255 */     XMLEntityResolver resolver = getEntityResolver();
/* 256 */     if (resolver != null) {
/* 257 */       this.fSchemaLoader.setEntityResolver(resolver);
/*     */     }
/* 259 */     if (this.fErrorReporter.getMessageFormatter("http://www.w3.org/TR/xml-schema-1") == null) {
/* 260 */       this.fErrorReporter.putMessageFormatter("http://www.w3.org/TR/xml-schema-1", new XSMessageFormatter());
/*     */     }
/* 262 */     this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*     */ 
/* 264 */     String propPrefix = "http://apache.org/xml/properties/";
/* 265 */     String propName = propPrefix + "schema/external-schemaLocation";
/* 266 */     this.fSchemaLoader.setProperty(propName, getProperty(propName));
/* 267 */     propName = propPrefix + "schema/external-noNamespaceSchemaLocation";
/* 268 */     this.fSchemaLoader.setProperty(propName, getProperty(propName));
/* 269 */     propName = "http://java.sun.com/xml/jaxp/properties/schemaSource";
/* 270 */     this.fSchemaLoader.setProperty(propName, getProperty(propName));
/* 271 */     this.fSchemaLoader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", getFeature("http://apache.org/xml/features/validation/schema-full-checking"));
/*     */ 
/* 276 */     SchemaGrammar grammar = (SchemaGrammar)this.fSchemaLoader.loadGrammar(is);
/*     */ 
/* 278 */     if (grammar != null) {
/* 279 */       this.fGrammarPool.cacheGrammars("http://www.w3.org/2001/XMLSchema", new Grammar[] { grammar });
/*     */     }
/*     */ 
/* 283 */     return grammar;
/*     */   }
/*     */ 
/*     */   DTDGrammar parseDTD(XMLInputSource is)
/*     */     throws IOException
/*     */   {
/* 291 */     XMLEntityResolver resolver = getEntityResolver();
/* 292 */     if (resolver != null) {
/* 293 */       this.fDTDLoader.setEntityResolver(resolver);
/*     */     }
/* 295 */     this.fDTDLoader.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*     */ 
/* 300 */     DTDGrammar grammar = (DTDGrammar)this.fDTDLoader.loadGrammar(is);
/*     */ 
/* 302 */     if (grammar != null) {
/* 303 */       this.fGrammarPool.cacheGrammars("http://www.w3.org/TR/REC-xml", new Grammar[] { grammar });
/*     */     }
/*     */ 
/* 307 */     return grammar;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XMLGrammarCachingConfiguration
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class XMLGrammarPreparser
/*     */ {
/*     */   private static final String CONTINUE_AFTER_FATAL_ERROR = "http://apache.org/xml/features/continue-after-fatal-error";
/*     */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   protected static final String ERROR_REPORTER = "http://apache.org/xml/properties/internal/error-reporter";
/*     */   protected static final String ERROR_HANDLER = "http://apache.org/xml/properties/internal/error-handler";
/*     */   protected static final String ENTITY_RESOLVER = "http://apache.org/xml/properties/internal/entity-resolver";
/*     */   protected static final String GRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*  85 */   private static final Hashtable KNOWN_LOADERS = new Hashtable();
/*     */ 
/*  95 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/error-reporter", "http://apache.org/xml/properties/internal/error-handler", "http://apache.org/xml/properties/internal/entity-resolver", "http://apache.org/xml/properties/internal/grammar-pool" };
/*     */   protected SymbolTable fSymbolTable;
/*     */   protected XMLErrorReporter fErrorReporter;
/*     */   protected XMLEntityResolver fEntityResolver;
/*     */   protected XMLGrammarPool fGrammarPool;
/*     */   protected Locale fLocale;
/*     */   private Hashtable fLoaders;
/*     */ 
/*     */   public XMLGrammarPreparser()
/*     */   {
/* 120 */     this(new SymbolTable());
/*     */   }
/*     */ 
/*     */   public XMLGrammarPreparser(SymbolTable symbolTable)
/*     */   {
/* 129 */     this.fSymbolTable = symbolTable;
/*     */ 
/* 131 */     this.fLoaders = new Hashtable();
/* 132 */     this.fErrorReporter = new XMLErrorReporter();
/* 133 */     setLocale(Locale.getDefault());
/* 134 */     this.fEntityResolver = new XMLEntityManager();
/*     */   }
/*     */ 
/*     */   public boolean registerPreparser(String grammarType, XMLGrammarLoader loader)
/*     */   {
/* 154 */     if (loader == null) {
/* 155 */       if (KNOWN_LOADERS.containsKey(grammarType))
/*     */       {
/* 157 */         String loaderName = (String)KNOWN_LOADERS.get(grammarType);
/*     */         try {
/* 159 */           XMLGrammarLoader gl = (XMLGrammarLoader)ObjectFactory.newInstance(loaderName, true);
/* 160 */           this.fLoaders.put(grammarType, gl);
/*     */         } catch (Exception e) {
/* 162 */           return false;
/*     */         }
/* 164 */         return true;
/*     */       }
/* 166 */       return false;
/*     */     }
/*     */ 
/* 169 */     this.fLoaders.put(grammarType, loader);
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */   public Grammar preparseGrammar(String type, XMLInputSource is)
/*     */     throws XNIException, IOException
/*     */   {
/* 191 */     if (this.fLoaders.containsKey(type)) {
/* 192 */       XMLGrammarLoader gl = (XMLGrammarLoader)this.fLoaders.get(type);
/*     */ 
/* 194 */       gl.setProperty("http://apache.org/xml/properties/internal/symbol-table", this.fSymbolTable);
/* 195 */       gl.setProperty("http://apache.org/xml/properties/internal/entity-resolver", this.fEntityResolver);
/* 196 */       gl.setProperty("http://apache.org/xml/properties/internal/error-reporter", this.fErrorReporter);
/*     */ 
/* 198 */       if (this.fGrammarPool != null)
/*     */         try {
/* 200 */           gl.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/*     */         }
/* 205 */       return gl.loadGrammar(is);
/*     */     }
/* 207 */     return null;
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/* 219 */     this.fLocale = locale;
/* 220 */     this.fErrorReporter.setLocale(locale);
/*     */   }
/*     */ 
/*     */   public Locale getLocale()
/*     */   {
/* 225 */     return this.fLocale;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(XMLErrorHandler errorHandler)
/*     */   {
/* 235 */     this.fErrorReporter.setProperty("http://apache.org/xml/properties/internal/error-handler", errorHandler);
/*     */   }
/*     */ 
/*     */   public XMLErrorHandler getErrorHandler()
/*     */   {
/* 240 */     return this.fErrorReporter.getErrorHandler();
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(XMLEntityResolver entityResolver)
/*     */   {
/* 249 */     this.fEntityResolver = entityResolver;
/*     */   }
/*     */ 
/*     */   public XMLEntityResolver getEntityResolver()
/*     */   {
/* 254 */     return this.fEntityResolver;
/*     */   }
/*     */ 
/*     */   public void setGrammarPool(XMLGrammarPool grammarPool)
/*     */   {
/* 263 */     this.fGrammarPool = grammarPool;
/*     */   }
/*     */ 
/*     */   public XMLGrammarPool getGrammarPool()
/*     */   {
/* 268 */     return this.fGrammarPool;
/*     */   }
/*     */ 
/*     */   public XMLGrammarLoader getLoader(String type)
/*     */   {
/* 274 */     return (XMLGrammarLoader)this.fLoaders.get(type);
/*     */   }
/*     */ 
/*     */   public void setFeature(String featureId, boolean value)
/*     */   {
/* 283 */     Enumeration loaders = this.fLoaders.elements();
/* 284 */     while (loaders.hasMoreElements()) {
/* 285 */       XMLGrammarLoader gl = (XMLGrammarLoader)loaders.nextElement();
/*     */       try {
/* 287 */         gl.setFeature(featureId, value);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 294 */     if (featureId.equals("http://apache.org/xml/features/continue-after-fatal-error"))
/* 295 */       this.fErrorReporter.setFeature("http://apache.org/xml/features/continue-after-fatal-error", value);
/*     */   }
/*     */ 
/*     */   public void setProperty(String propId, Object value)
/*     */   {
/* 307 */     Enumeration loaders = this.fLoaders.elements();
/* 308 */     while (loaders.hasMoreElements()) {
/* 309 */       XMLGrammarLoader gl = (XMLGrammarLoader)loaders.nextElement();
/*     */       try {
/* 311 */         gl.setProperty(propId, value);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String type, String featureId)
/*     */   {
/* 325 */     XMLGrammarLoader gl = (XMLGrammarLoader)this.fLoaders.get(type);
/* 326 */     return gl.getFeature(featureId);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String type, String propertyId)
/*     */   {
/* 338 */     XMLGrammarLoader gl = (XMLGrammarLoader)this.fLoaders.get(type);
/* 339 */     return gl.getProperty(propertyId);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  88 */     KNOWN_LOADERS.put("http://www.w3.org/2001/XMLSchema", "com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader");
/*     */ 
/*  90 */     KNOWN_LOADERS.put("http://www.w3.org/TR/REC-xml", "com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDLoader");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.XMLGrammarPreparser
 * JD-Core Version:    0.6.2
 */
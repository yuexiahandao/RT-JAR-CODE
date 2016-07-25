/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.State;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.State;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLParserConfiguration;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public class SAXParser extends AbstractSAXParser
/*     */ {
/*     */   protected static final String NOTIFY_BUILTIN_REFS = "http://apache.org/xml/features/scanner/notify-builtin-refs";
/*     */   protected static final String REPORT_WHITESPACE = "http://java.sun.com/xml/schema/features/report-ignored-element-content-whitespace";
/*  59 */   private static final String[] RECOGNIZED_FEATURES = { "http://apache.org/xml/features/scanner/notify-builtin-refs", "http://java.sun.com/xml/schema/features/report-ignored-element-content-whitespace" };
/*     */   protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
/*     */   protected static final String XMLGRAMMAR_POOL = "http://apache.org/xml/properties/internal/grammar-pool";
/*  75 */   private static final String[] RECOGNIZED_PROPERTIES = { "http://apache.org/xml/properties/internal/symbol-table", "http://apache.org/xml/properties/internal/grammar-pool" };
/*     */ 
/*     */   public SAXParser(XMLParserConfiguration config)
/*     */   {
/*  89 */     super(config);
/*     */   }
/*     */ 
/*     */   public SAXParser()
/*     */   {
/*  96 */     this(null, null);
/*     */   }
/*     */ 
/*     */   public SAXParser(SymbolTable symbolTable)
/*     */   {
/* 103 */     this(symbolTable, null);
/*     */   }
/*     */ 
/*     */   public SAXParser(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/* 111 */     super(new XIncludeAwareParserConfiguration());
/*     */ 
/* 114 */     this.fConfiguration.addRecognizedFeatures(RECOGNIZED_FEATURES);
/* 115 */     this.fConfiguration.setFeature("http://apache.org/xml/features/scanner/notify-builtin-refs", true);
/*     */ 
/* 118 */     this.fConfiguration.addRecognizedProperties(RECOGNIZED_PROPERTIES);
/* 119 */     if (symbolTable != null) {
/* 120 */       this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/symbol-table", symbolTable);
/*     */     }
/* 122 */     if (grammarPool != null)
/* 123 */       this.fConfiguration.setProperty("http://apache.org/xml/properties/internal/grammar-pool", grammarPool);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 138 */     if (name.equals("http://apache.org/xml/properties/security-manager")) {
/* 139 */       this.securityManager = XMLSecurityManager.convert(value, this.securityManager);
/* 140 */       super.setProperty("http://apache.org/xml/properties/security-manager", this.securityManager);
/* 141 */       return;
/*     */     }
/* 143 */     if (name.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
/* 144 */       if (value == null)
/* 145 */         this.securityPropertyManager = new XMLSecurityPropertyManager();
/*     */       else {
/* 147 */         this.securityPropertyManager = ((XMLSecurityPropertyManager)value);
/*     */       }
/* 149 */       super.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.securityPropertyManager);
/* 150 */       return;
/*     */     }
/*     */ 
/* 153 */     if (this.securityManager == null) {
/* 154 */       this.securityManager = new XMLSecurityManager(true);
/* 155 */       super.setProperty("http://apache.org/xml/properties/security-manager", this.securityManager);
/*     */     }
/*     */ 
/* 158 */     if (this.securityPropertyManager == null) {
/* 159 */       this.securityPropertyManager = new XMLSecurityPropertyManager();
/* 160 */       super.setProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.securityPropertyManager);
/*     */     }
/*     */ 
/* 163 */     int index = this.securityPropertyManager.getIndex(name);
/* 164 */     if (index > -1)
/*     */     {
/* 170 */       this.securityPropertyManager.setValue(index, XMLSecurityPropertyManager.State.APIPROPERTY, (String)value);
/*     */     }
/* 173 */     else if (!this.securityManager.setLimit(name, XMLSecurityManager.State.APIPROPERTY, value))
/*     */     {
/* 175 */       super.setProperty(name, value);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.SAXParser
 * JD-Core Version:    0.6.2
 */
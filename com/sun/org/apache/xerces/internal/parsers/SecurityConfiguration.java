/*     */ package com.sun.org.apache.xerces.internal.parsers;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
/*     */ 
/*     */ public class SecurityConfiguration extends XIncludeAwareParserConfiguration
/*     */ {
/*     */   protected static final String SECURITY_MANAGER_PROPERTY = "http://apache.org/xml/properties/security-manager";
/*     */ 
/*     */   public SecurityConfiguration()
/*     */   {
/*  65 */     this(null, null, null);
/*     */   }
/*     */ 
/*     */   public SecurityConfiguration(SymbolTable symbolTable)
/*     */   {
/*  74 */     this(symbolTable, null, null);
/*     */   }
/*     */ 
/*     */   public SecurityConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool)
/*     */   {
/*  90 */     this(symbolTable, grammarPool, null);
/*     */   }
/*     */ 
/*     */   public SecurityConfiguration(SymbolTable symbolTable, XMLGrammarPool grammarPool, XMLComponentManager parentSettings)
/*     */   {
/* 108 */     super(symbolTable, grammarPool, parentSettings);
/*     */ 
/* 111 */     setProperty("http://apache.org/xml/properties/security-manager", new XMLSecurityManager(true));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.parsers.SecurityConfiguration
 * JD-Core Version:    0.6.2
 */
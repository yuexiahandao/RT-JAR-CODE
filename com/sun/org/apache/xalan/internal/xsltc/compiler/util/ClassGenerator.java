/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Method;
/*     */ import com.sun.org.apache.bcel.internal.generic.ALOAD;
/*     */ import com.sun.org.apache.bcel.internal.generic.ClassGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.Stylesheet;
/*     */ 
/*     */ public class ClassGenerator extends ClassGen
/*     */ {
/*     */   protected static final int TRANSLET_INDEX = 0;
/*  52 */   protected static int INVALID_INDEX = -1;
/*     */   private Stylesheet _stylesheet;
/*     */   private final Parser _parser;
/*     */   private final Instruction _aloadTranslet;
/*     */   private final String _domClass;
/*     */   private final String _domClassSig;
/*     */   private final String _applyTemplatesSig;
/*     */   private final String _applyTemplatesSigForImport;
/*     */ 
/*     */   public ClassGenerator(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces, Stylesheet stylesheet)
/*     */   {
/*  67 */     super(class_name, super_class_name, file_name, access_flags, interfaces);
/*     */ 
/*  69 */     this._stylesheet = stylesheet;
/*  70 */     this._parser = stylesheet.getParser();
/*  71 */     this._aloadTranslet = new ALOAD(0);
/*     */ 
/*  73 */     if (stylesheet.isMultiDocument()) {
/*  74 */       this._domClass = "com.sun.org.apache.xalan.internal.xsltc.dom.MultiDOM";
/*  75 */       this._domClassSig = "Lcom/sun/org/apache/xalan/internal/xsltc/dom/MultiDOM;";
/*     */     }
/*     */     else {
/*  78 */       this._domClass = "com.sun.org.apache.xalan.internal.xsltc.dom.DOMAdapter";
/*  79 */       this._domClassSig = "Lcom/sun/org/apache/xalan/internal/xsltc/dom/DOMAdapter;";
/*     */     }
/*  81 */     this._applyTemplatesSig = "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;)V";
/*     */ 
/*  87 */     this._applyTemplatesSigForImport = "(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;I)V";
/*     */   }
/*     */ 
/*     */   public final Parser getParser()
/*     */   {
/*  96 */     return this._parser;
/*     */   }
/*     */ 
/*     */   public final Stylesheet getStylesheet() {
/* 100 */     return this._stylesheet;
/*     */   }
/*     */ 
/*     */   public final String getClassName()
/*     */   {
/* 108 */     return this._stylesheet.getClassName();
/*     */   }
/*     */ 
/*     */   public Instruction loadTranslet() {
/* 112 */     return this._aloadTranslet;
/*     */   }
/*     */ 
/*     */   public final String getDOMClass() {
/* 116 */     return this._domClass;
/*     */   }
/*     */ 
/*     */   public final String getDOMClassSig() {
/* 120 */     return this._domClassSig;
/*     */   }
/*     */ 
/*     */   public final String getApplyTemplatesSig() {
/* 124 */     return this._applyTemplatesSig;
/*     */   }
/*     */ 
/*     */   public final String getApplyTemplatesSigForImport() {
/* 128 */     return this._applyTemplatesSigForImport;
/*     */   }
/*     */ 
/*     */   public boolean isExternal()
/*     */   {
/* 136 */     return false;
/*     */   }
/*     */   public void addMethod(MethodGenerator methodGen) {
/* 139 */     Method[] methodsToAdd = methodGen.getGeneratedMethods(this);
/* 140 */     for (int i = 0; i < methodsToAdd.length; i++)
/* 141 */       addMethod(methodsToAdd[i]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator
 * JD-Core Version:    0.6.2
 */
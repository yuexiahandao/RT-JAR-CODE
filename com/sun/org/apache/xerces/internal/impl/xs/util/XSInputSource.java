/*    */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*    */ import com.sun.org.apache.xerces.internal.xs.XSObject;
/*    */ 
/*    */ public final class XSInputSource extends XMLInputSource
/*    */ {
/*    */   private SchemaGrammar[] fGrammars;
/*    */   private XSObject[] fComponents;
/*    */ 
/*    */   public XSInputSource(SchemaGrammar[] grammars)
/*    */   {
/* 40 */     super(null, null, null);
/* 41 */     this.fGrammars = grammars;
/* 42 */     this.fComponents = null;
/*    */   }
/*    */ 
/*    */   public XSInputSource(XSObject[] component) {
/* 46 */     super(null, null, null);
/* 47 */     this.fGrammars = null;
/* 48 */     this.fComponents = component;
/*    */   }
/*    */ 
/*    */   public SchemaGrammar[] getGrammars() {
/* 52 */     return this.fGrammars;
/*    */   }
/*    */ 
/*    */   public void setGrammars(SchemaGrammar[] grammars) {
/* 56 */     this.fGrammars = grammars;
/*    */   }
/*    */ 
/*    */   public XSObject[] getComponents() {
/* 60 */     return this.fComponents;
/*    */   }
/*    */ 
/*    */   public void setComponents(XSObject[] components) {
/* 64 */     this.fComponents = components;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.XSInputSource
 * JD-Core Version:    0.6.2
 */
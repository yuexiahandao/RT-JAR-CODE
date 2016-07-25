/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ 
/*    */ final class NamespaceAlias extends TopLevelElement
/*    */ {
/*    */   private String sPrefix;
/*    */   private String rPrefix;
/*    */ 
/*    */   public void parseContents(Parser parser)
/*    */   {
/* 45 */     this.sPrefix = getAttribute("stylesheet-prefix");
/* 46 */     this.rPrefix = getAttribute("result-prefix");
/* 47 */     parser.getSymbolTable().addPrefixAlias(this.sPrefix, this.rPrefix);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 51 */     return Type.Void;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.NamespaceAlias
 * JD-Core Version:    0.6.2
 */
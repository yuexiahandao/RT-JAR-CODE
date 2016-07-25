/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ final class ArgumentList
/*    */ {
/*    */   private final Expression _arg;
/*    */   private final ArgumentList _rest;
/*    */ 
/*    */   public ArgumentList(Expression arg, ArgumentList rest)
/*    */   {
/* 35 */     this._arg = arg;
/* 36 */     this._rest = rest;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 40 */     return this._arg.toString() + ", " + this._rest.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ArgumentList
 * JD-Core Version:    0.6.2
 */
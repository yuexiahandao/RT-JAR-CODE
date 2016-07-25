/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode;
/*    */ 
/*    */ public class TypeCheckError extends Exception
/*    */ {
/*    */   static final long serialVersionUID = 3246224233917854640L;
/* 34 */   ErrorMsg _error = null;
/* 35 */   SyntaxTreeNode _node = null;
/*    */ 
/*    */   public TypeCheckError(SyntaxTreeNode node)
/*    */   {
/* 39 */     this._node = node;
/*    */   }
/*    */ 
/*    */   public TypeCheckError(ErrorMsg error)
/*    */   {
/* 44 */     this._error = error;
/*    */   }
/*    */ 
/*    */   public TypeCheckError(String code, Object param)
/*    */   {
/* 49 */     this._error = new ErrorMsg(code, param);
/*    */   }
/*    */ 
/*    */   public TypeCheckError(String code, Object param1, Object param2)
/*    */   {
/* 54 */     this._error = new ErrorMsg(code, param1, param2);
/*    */   }
/*    */ 
/*    */   public ErrorMsg getErrorMsg() {
/* 58 */     return this._error;
/*    */   }
/*    */ 
/*    */   public String getMessage() {
/* 62 */     return toString();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 68 */     if (this._error == null) {
/* 69 */       if (this._node != null) {
/* 70 */         this._error = new ErrorMsg("TYPE_CHECK_ERR", this._node.toString());
/*    */       }
/*    */       else {
/* 73 */         this._error = new ErrorMsg("TYPE_CHECK_UNK_LOC_ERR");
/*    */       }
/*    */     }
/*    */ 
/* 77 */     return this._error.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError
 * JD-Core Version:    0.6.2
 */
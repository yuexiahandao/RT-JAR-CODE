/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class BooleanCall extends FunctionCall
/*    */ {
/* 39 */   private Expression _arg = null;
/*    */ 
/*    */   public BooleanCall(QName fname, Vector arguments) {
/* 42 */     super(fname, arguments);
/* 43 */     this._arg = argument(0);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 47 */     this._arg.typeCheck(stable);
/* 48 */     return this._type = Type.Boolean;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 52 */     this._arg.translate(classGen, methodGen);
/* 53 */     Type targ = this._arg.getType();
/* 54 */     if (!targ.identicalTo(Type.Boolean)) {
/* 55 */       this._arg.startIterator(classGen, methodGen);
/* 56 */       targ.translateTo(classGen, methodGen, Type.Boolean);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.BooleanCall
 * JD-Core Version:    0.6.2
 */
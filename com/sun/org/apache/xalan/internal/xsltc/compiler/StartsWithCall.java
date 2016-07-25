/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class StartsWithCall extends FunctionCall
/*    */ {
/* 44 */   private Expression _base = null;
/* 45 */   private Expression _token = null;
/*    */ 
/*    */   public StartsWithCall(QName fname, Vector arguments)
/*    */   {
/* 51 */     super(fname, arguments);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable)
/*    */     throws TypeCheckError
/*    */   {
/* 60 */     if (argumentCount() != 2) {
/* 61 */       ErrorMsg err = new ErrorMsg("ILLEGAL_ARG_ERR", getName(), this);
/*    */ 
/* 63 */       throw new TypeCheckError(err);
/*    */     }
/*    */ 
/* 67 */     this._base = argument(0);
/* 68 */     Type baseType = this._base.typeCheck(stable);
/* 69 */     if (baseType != Type.String) {
/* 70 */       this._base = new CastExpr(this._base, Type.String);
/*    */     }
/*    */ 
/* 73 */     this._token = argument(1);
/* 74 */     Type tokenType = this._token.typeCheck(stable);
/* 75 */     if (tokenType != Type.String) {
/* 76 */       this._token = new CastExpr(this._token, Type.String);
/*    */     }
/* 78 */     return this._type = Type.Boolean;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 85 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 86 */     InstructionList il = methodGen.getInstructionList();
/* 87 */     this._base.translate(classGen, methodGen);
/* 88 */     this._token.translate(classGen, methodGen);
/* 89 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.String", "startsWith", "(Ljava/lang/String;)Z")));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.StartsWithCall
 * JD-Core Version:    0.6.2
 */
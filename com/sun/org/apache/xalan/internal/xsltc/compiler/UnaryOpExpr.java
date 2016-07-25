/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class UnaryOpExpr extends Expression
/*    */ {
/*    */   private Expression _left;
/*    */ 
/*    */   public UnaryOpExpr(Expression left)
/*    */   {
/* 41 */     (this._left = left).setParent(this);
/*    */   }
/*    */ 
/*    */   public boolean hasPositionCall()
/*    */   {
/* 49 */     return this._left.hasPositionCall();
/*    */   }
/*    */ 
/*    */   public boolean hasLastCall()
/*    */   {
/* 56 */     return this._left.hasLastCall();
/*    */   }
/*    */ 
/*    */   public void setParser(Parser parser) {
/* 60 */     super.setParser(parser);
/* 61 */     this._left.setParser(parser);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 65 */     Type tleft = this._left.typeCheck(stable);
/* 66 */     MethodType ptype = lookupPrimop(stable, "u-", new MethodType(Type.Void, tleft));
/*    */ 
/* 70 */     if (ptype != null) {
/* 71 */       Type arg1 = (Type)ptype.argsType().elementAt(0);
/* 72 */       if (!arg1.identicalTo(tleft)) {
/* 73 */         this._left = new CastExpr(this._left, arg1);
/*    */       }
/* 75 */       return this._type = ptype.resultType();
/*    */     }
/*    */ 
/* 78 */     throw new TypeCheckError(this);
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 82 */     return "u-(" + this._left + ')';
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 86 */     InstructionList il = methodGen.getInstructionList();
/* 87 */     this._left.translate(classGen, methodGen);
/* 88 */     il.append(this._type.NEG());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.UnaryOpExpr
 * JD-Core Version:    0.6.2
 */
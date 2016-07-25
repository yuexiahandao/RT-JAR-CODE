/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class NumberCall extends FunctionCall
/*    */ {
/*    */   public NumberCall(QName fname, Vector arguments)
/*    */   {
/* 41 */     super(fname, arguments);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 45 */     if (argumentCount() > 0) {
/* 46 */       argument().typeCheck(stable);
/*    */     }
/* 48 */     return this._type = Type.Real;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 52 */     InstructionList il = methodGen.getInstructionList();
/*    */     Type targ;
/*    */     Type targ;
/* 55 */     if (argumentCount() == 0) {
/* 56 */       il.append(methodGen.loadContextNode());
/* 57 */       targ = Type.Node;
/*    */     }
/*    */     else {
/* 60 */       Expression arg = argument();
/* 61 */       arg.translate(classGen, methodGen);
/* 62 */       arg.startIterator(classGen, methodGen);
/* 63 */       targ = arg.getType();
/*    */     }
/*    */ 
/* 66 */     if (!targ.identicalTo(Type.Real))
/* 67 */       targ.translateTo(classGen, methodGen, Type.Real);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.NumberCall
 * JD-Core Version:    0.6.2
 */
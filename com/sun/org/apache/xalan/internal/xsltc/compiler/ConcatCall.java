/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*    */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.bcel.internal.generic.NEW;
/*    */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class ConcatCall extends FunctionCall
/*    */ {
/*    */   public ConcatCall(QName fname, Vector arguments)
/*    */   {
/* 46 */     super(fname, arguments);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/* 50 */     for (int i = 0; i < argumentCount(); i++) {
/* 51 */       Expression exp = argument(i);
/* 52 */       if (!exp.typeCheck(stable).identicalTo(Type.String)) {
/* 53 */         setArgument(i, new CastExpr(exp, Type.String));
/*    */       }
/*    */     }
/* 56 */     return this._type = Type.String;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 61 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 62 */     InstructionList il = methodGen.getInstructionList();
/* 63 */     int nArgs = argumentCount();
/*    */ 
/* 65 */     switch (nArgs) {
/*    */     case 0:
/* 67 */       il.append(new PUSH(cpg, ""));
/* 68 */       break;
/*    */     case 1:
/* 71 */       argument().translate(classGen, methodGen);
/* 72 */       break;
/*    */     default:
/* 75 */       int initBuffer = cpg.addMethodref("java.lang.StringBuffer", "<init>", "()V");
/*    */ 
/* 77 */       Instruction append = new INVOKEVIRTUAL(cpg.addMethodref("java.lang.StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
/*    */ 
/* 83 */       int toString = cpg.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;");
/*    */ 
/* 87 */       il.append(new NEW(cpg.addClass("java.lang.StringBuffer")));
/* 88 */       il.append(DUP);
/* 89 */       il.append(new INVOKESPECIAL(initBuffer));
/* 90 */       for (int i = 0; i < nArgs; i++) {
/* 91 */         argument(i).translate(classGen, methodGen);
/* 92 */         il.append(append);
/*    */       }
/* 94 */       il.append(new INVOKEVIRTUAL(toString));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ConcatCall
 * JD-Core Version:    0.6.2
 */
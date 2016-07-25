/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.ILOAD;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.FilterGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*    */ import java.util.Vector;
/*    */ 
/*    */ final class LangCall extends FunctionCall
/*    */ {
/*    */   private Expression _lang;
/*    */   private Type _langType;
/*    */ 
/*    */   public LangCall(QName fname, Vector arguments)
/*    */   {
/* 51 */     super(fname, arguments);
/* 52 */     this._lang = argument(0);
/*    */   }
/*    */ 
/*    */   public Type typeCheck(SymbolTable stable)
/*    */     throws TypeCheckError
/*    */   {
/* 59 */     this._langType = this._lang.typeCheck(stable);
/* 60 */     if (!(this._langType instanceof StringType)) {
/* 61 */       this._lang = new CastExpr(this._lang, Type.String);
/*    */     }
/* 63 */     return Type.Boolean;
/*    */   }
/*    */ 
/*    */   public Type getType()
/*    */   {
/* 70 */     return Type.Boolean;
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*    */   {
/* 79 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 80 */     InstructionList il = methodGen.getInstructionList();
/*    */ 
/* 82 */     int tst = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "testLanguage", "(Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;I)Z");
/*    */ 
/* 85 */     this._lang.translate(classGen, methodGen);
/* 86 */     il.append(methodGen.loadDOM());
/* 87 */     if ((classGen instanceof FilterGenerator))
/* 88 */       il.append(new ILOAD(1));
/*    */     else
/* 90 */       il.append(methodGen.loadContextNode());
/* 91 */     il.append(new INVOKESTATIC(tst));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.LangCall
 * JD-Core Version:    0.6.2
 */
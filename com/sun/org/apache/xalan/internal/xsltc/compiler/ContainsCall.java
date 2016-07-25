/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFLT;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class ContainsCall extends FunctionCall
/*     */ {
/*  45 */   private Expression _base = null;
/*  46 */   private Expression _token = null;
/*     */ 
/*     */   public ContainsCall(QName fname, Vector arguments)
/*     */   {
/*  52 */     super(fname, arguments);
/*     */   }
/*     */ 
/*     */   public boolean isBoolean()
/*     */   {
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  68 */     if (argumentCount() != 2) {
/*  69 */       throw new TypeCheckError("ILLEGAL_ARG_ERR", getName(), this);
/*     */     }
/*     */ 
/*  73 */     this._base = argument(0);
/*  74 */     Type baseType = this._base.typeCheck(stable);
/*  75 */     if (baseType != Type.String) {
/*  76 */       this._base = new CastExpr(this._base, Type.String);
/*     */     }
/*     */ 
/*  79 */     this._token = argument(1);
/*  80 */     Type tokenType = this._token.typeCheck(stable);
/*  81 */     if (tokenType != Type.String) {
/*  82 */       this._token = new CastExpr(this._token, Type.String);
/*     */     }
/*  84 */     return this._type = Type.Boolean;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*  91 */     translateDesynthesized(classGen, methodGen);
/*  92 */     synthesize(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 100 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 101 */     InstructionList il = methodGen.getInstructionList();
/* 102 */     this._base.translate(classGen, methodGen);
/* 103 */     this._token.translate(classGen, methodGen);
/* 104 */     il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.String", "indexOf", "(Ljava/lang/String;)I")));
/*     */ 
/* 107 */     this._falseList.add(il.append(new IFLT(null)));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ContainsCall
 * JD-Core Version:    0.6.2
 */
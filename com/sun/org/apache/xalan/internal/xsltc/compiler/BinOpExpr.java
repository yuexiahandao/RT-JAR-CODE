/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class BinOpExpr extends Expression
/*     */ {
/*     */   public static final int PLUS = 0;
/*     */   public static final int MINUS = 1;
/*     */   public static final int TIMES = 2;
/*     */   public static final int DIV = 3;
/*     */   public static final int MOD = 4;
/*  45 */   private static final String[] Ops = { "+", "-", "*", "/", "%" };
/*     */   private int _op;
/*     */   private Expression _left;
/*     */   private Expression _right;
/*     */ 
/*     */   public BinOpExpr(int op, Expression left, Expression right)
/*     */   {
/*  53 */     this._op = op;
/*  54 */     (this._left = left).setParent(this);
/*  55 */     (this._right = right).setParent(this);
/*     */   }
/*     */ 
/*     */   public boolean hasPositionCall()
/*     */   {
/*  63 */     if (this._left.hasPositionCall()) return true;
/*  64 */     if (this._right.hasPositionCall()) return true;
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasLastCall()
/*     */   {
/*  72 */     return (this._left.hasLastCall()) || (this._right.hasLastCall());
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  76 */     super.setParser(parser);
/*  77 */     this._left.setParser(parser);
/*  78 */     this._right.setParser(parser);
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
/*  82 */     Type tleft = this._left.typeCheck(stable);
/*  83 */     Type tright = this._right.typeCheck(stable);
/*  84 */     MethodType ptype = lookupPrimop(stable, Ops[this._op], new MethodType(Type.Void, tleft, tright));
/*     */ 
/*  87 */     if (ptype != null) {
/*  88 */       Type arg1 = (Type)ptype.argsType().elementAt(0);
/*  89 */       if (!arg1.identicalTo(tleft)) {
/*  90 */         this._left = new CastExpr(this._left, arg1);
/*     */       }
/*  92 */       Type arg2 = (Type)ptype.argsType().elementAt(1);
/*  93 */       if (!arg2.identicalTo(tright)) {
/*  94 */         this._right = new CastExpr(this._right, arg1);
/*     */       }
/*  96 */       return this._type = ptype.resultType();
/*     */     }
/*  98 */     throw new TypeCheckError(this);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 102 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 104 */     this._left.translate(classGen, methodGen);
/* 105 */     this._right.translate(classGen, methodGen);
/*     */ 
/* 107 */     switch (this._op) {
/*     */     case 0:
/* 109 */       il.append(this._type.ADD());
/* 110 */       break;
/*     */     case 1:
/* 112 */       il.append(this._type.SUB());
/* 113 */       break;
/*     */     case 2:
/* 115 */       il.append(this._type.MUL());
/* 116 */       break;
/*     */     case 3:
/* 118 */       il.append(this._type.DIV());
/* 119 */       break;
/*     */     case 4:
/* 121 */       il.append(this._type.REM());
/* 122 */       break;
/*     */     default:
/* 124 */       ErrorMsg msg = new ErrorMsg("ILLEGAL_BINARY_OP_ERR", this);
/* 125 */       getParser().reportError(3, msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 130 */     return Ops[this._op] + '(' + this._left + ", " + this._right + ')';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.BinOpExpr
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class LogicalExpr extends Expression
/*     */ {
/*     */   public static final int OR = 0;
/*     */   public static final int AND = 1;
/*     */   private final int _op;
/*     */   private Expression _left;
/*     */   private Expression _right;
/*  49 */   private static final String[] Ops = { "or", "and" };
/*     */ 
/*     */   public LogicalExpr(int op, Expression left, Expression right)
/*     */   {
/*  58 */     this._op = op;
/*  59 */     (this._left = left).setParent(this);
/*  60 */     (this._right = right).setParent(this);
/*     */   }
/*     */ 
/*     */   public boolean hasPositionCall()
/*     */   {
/*  68 */     return (this._left.hasPositionCall()) || (this._right.hasPositionCall());
/*     */   }
/*     */ 
/*     */   public boolean hasLastCall()
/*     */   {
/*  75 */     return (this._left.hasLastCall()) || (this._right.hasLastCall());
/*     */   }
/*     */ 
/*     */   public Object evaluateAtCompileTime()
/*     */   {
/*  84 */     Object leftb = this._left.evaluateAtCompileTime();
/*  85 */     Object rightb = this._right.evaluateAtCompileTime();
/*     */ 
/*  88 */     if ((leftb == null) || (rightb == null)) {
/*  89 */       return null;
/*     */     }
/*     */ 
/*  92 */     if (this._op == 1) {
/*  93 */       return (leftb == Boolean.TRUE) && (rightb == Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE;
/*     */     }
/*     */ 
/*  97 */     return (leftb == Boolean.TRUE) || (rightb == Boolean.TRUE) ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   public int getOp()
/*     */   {
/* 107 */     return this._op;
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser)
/*     */   {
/* 115 */     super.setParser(parser);
/* 116 */     this._left.setParser(parser);
/* 117 */     this._right.setParser(parser);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 124 */     return Ops[this._op] + '(' + this._left + ", " + this._right + ')';
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 132 */     Type tleft = this._left.typeCheck(stable);
/* 133 */     Type tright = this._right.typeCheck(stable);
/*     */ 
/* 136 */     MethodType wantType = new MethodType(Type.Void, tleft, tright);
/* 137 */     MethodType haveType = lookupPrimop(stable, Ops[this._op], wantType);
/*     */ 
/* 140 */     if (haveType != null)
/*     */     {
/* 142 */       Type arg1 = (Type)haveType.argsType().elementAt(0);
/* 143 */       if (!arg1.identicalTo(tleft)) {
/* 144 */         this._left = new CastExpr(this._left, arg1);
/*     */       }
/* 146 */       Type arg2 = (Type)haveType.argsType().elementAt(1);
/* 147 */       if (!arg2.identicalTo(tright)) {
/* 148 */         this._right = new CastExpr(this._right, arg1);
/*     */       }
/* 150 */       return this._type = haveType.resultType();
/*     */     }
/* 152 */     throw new TypeCheckError(this);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 159 */     translateDesynthesized(classGen, methodGen);
/* 160 */     synthesize(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 169 */     InstructionList il = methodGen.getInstructionList();
/* 170 */     SyntaxTreeNode parent = getParent();
/*     */ 
/* 173 */     if (this._op == 1)
/*     */     {
/* 176 */       this._left.translateDesynthesized(classGen, methodGen);
/*     */ 
/* 179 */       InstructionHandle middle = il.append(NOP);
/*     */ 
/* 182 */       this._right.translateDesynthesized(classGen, methodGen);
/*     */ 
/* 185 */       InstructionHandle after = il.append(NOP);
/*     */ 
/* 188 */       this._falseList.append(this._right._falseList.append(this._left._falseList));
/*     */ 
/* 192 */       if (((this._left instanceof LogicalExpr)) && (((LogicalExpr)this._left).getOp() == 0))
/*     */       {
/* 194 */         this._left.backPatchTrueList(middle);
/*     */       }
/* 196 */       else if ((this._left instanceof NotCall)) {
/* 197 */         this._left.backPatchTrueList(middle);
/*     */       }
/*     */       else {
/* 200 */         this._trueList.append(this._left._trueList);
/*     */       }
/*     */ 
/* 205 */       if (((this._right instanceof LogicalExpr)) && (((LogicalExpr)this._right).getOp() == 0))
/*     */       {
/* 207 */         this._right.backPatchTrueList(after);
/*     */       }
/* 209 */       else if ((this._right instanceof NotCall)) {
/* 210 */         this._right.backPatchTrueList(after);
/*     */       }
/*     */       else {
/* 213 */         this._trueList.append(this._right._trueList);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 219 */       this._left.translateDesynthesized(classGen, methodGen);
/*     */ 
/* 223 */       InstructionHandle ih = il.append(new GOTO(null));
/*     */ 
/* 226 */       this._right.translateDesynthesized(classGen, methodGen);
/*     */ 
/* 228 */       this._left._trueList.backPatch(ih);
/* 229 */       this._left._falseList.backPatch(ih.getNext());
/*     */ 
/* 231 */       this._falseList.append(this._right._falseList);
/* 232 */       this._trueList.add(ih).append(this._right._trueList);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.LogicalExpr
 * JD-Core Version:    0.6.2
 */
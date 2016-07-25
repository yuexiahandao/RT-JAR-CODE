/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNE;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPNE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NumberType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;
/*     */ 
/*     */ final class EqualityExpr extends Expression
/*     */ {
/*     */   private final int _op;
/*     */   private Expression _left;
/*     */   private Expression _right;
/*     */ 
/*     */   public EqualityExpr(int op, Expression left, Expression right)
/*     */   {
/*  66 */     this._op = op;
/*  67 */     (this._left = left).setParent(this);
/*  68 */     (this._right = right).setParent(this);
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  72 */     super.setParser(parser);
/*  73 */     this._left.setParser(parser);
/*  74 */     this._right.setParser(parser);
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  78 */     return Operators.getOpNames(this._op) + '(' + this._left + ", " + this._right + ')';
/*     */   }
/*     */ 
/*     */   public Expression getLeft() {
/*  82 */     return this._left;
/*     */   }
/*     */ 
/*     */   public Expression getRight() {
/*  86 */     return this._right;
/*     */   }
/*     */ 
/*     */   public boolean getOp() {
/*  90 */     return this._op != 1;
/*     */   }
/*     */ 
/*     */   public boolean hasPositionCall()
/*     */   {
/*  98 */     if (this._left.hasPositionCall()) return true;
/*  99 */     if (this._right.hasPositionCall()) return true;
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasLastCall() {
/* 104 */     if (this._left.hasLastCall()) return true;
/* 105 */     if (this._right.hasLastCall()) return true;
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   private void swapArguments() {
/* 110 */     Expression temp = this._left;
/* 111 */     this._left = this._right;
/* 112 */     this._right = temp;
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 119 */     Type tleft = this._left.typeCheck(stable);
/* 120 */     Type tright = this._right.typeCheck(stable);
/*     */ 
/* 122 */     if ((tleft.isSimple()) && (tright.isSimple())) {
/* 123 */       if (tleft != tright) {
/* 124 */         if ((tleft instanceof BooleanType)) {
/* 125 */           this._right = new CastExpr(this._right, Type.Boolean);
/*     */         }
/* 127 */         else if ((tright instanceof BooleanType)) {
/* 128 */           this._left = new CastExpr(this._left, Type.Boolean);
/*     */         }
/* 130 */         else if (((tleft instanceof NumberType)) || ((tright instanceof NumberType)))
/*     */         {
/* 132 */           this._left = new CastExpr(this._left, Type.Real);
/* 133 */           this._right = new CastExpr(this._right, Type.Real);
/*     */         }
/*     */         else {
/* 136 */           this._left = new CastExpr(this._left, Type.String);
/* 137 */           this._right = new CastExpr(this._right, Type.String);
/*     */         }
/*     */       }
/*     */     }
/* 141 */     else if ((tleft instanceof ReferenceType)) {
/* 142 */       this._right = new CastExpr(this._right, Type.Reference);
/*     */     }
/* 144 */     else if ((tright instanceof ReferenceType)) {
/* 145 */       this._left = new CastExpr(this._left, Type.Reference);
/*     */     }
/* 148 */     else if (((tleft instanceof NodeType)) && (tright == Type.String)) {
/* 149 */       this._left = new CastExpr(this._left, Type.String);
/*     */     }
/* 151 */     else if ((tleft == Type.String) && ((tright instanceof NodeType))) {
/* 152 */       this._right = new CastExpr(this._right, Type.String);
/*     */     }
/* 155 */     else if (((tleft instanceof NodeType)) && ((tright instanceof NodeType))) {
/* 156 */       this._left = new CastExpr(this._left, Type.String);
/* 157 */       this._right = new CastExpr(this._right, Type.String);
/*     */     }
/* 159 */     else if ((!(tleft instanceof NodeType)) || (!(tright instanceof NodeSetType)))
/*     */     {
/* 162 */       if (((tleft instanceof NodeSetType)) && ((tright instanceof NodeType))) {
/* 163 */         swapArguments();
/*     */       }
/*     */       else
/*     */       {
/* 169 */         if ((tleft instanceof NodeType)) {
/* 170 */           this._left = new CastExpr(this._left, Type.NodeSet);
/*     */         }
/* 172 */         if ((tright instanceof NodeType)) {
/* 173 */           this._right = new CastExpr(this._right, Type.NodeSet);
/*     */         }
/*     */ 
/* 177 */         if ((tleft.isSimple()) || (((tleft instanceof ResultTreeType)) && ((tright instanceof NodeSetType))))
/*     */         {
/* 180 */           swapArguments();
/*     */         }
/*     */ 
/* 184 */         if ((this._right.getType() instanceof IntType))
/* 185 */           this._right = new CastExpr(this._right, Type.Real);
/*     */       }
/*     */     }
/* 188 */     return this._type = Type.Boolean;
/*     */   }
/*     */ 
/*     */   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 193 */     Type tleft = this._left.getType();
/* 194 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 196 */     if ((tleft instanceof BooleanType)) {
/* 197 */       this._left.translate(classGen, methodGen);
/* 198 */       this._right.translate(classGen, methodGen);
/* 199 */       this._falseList.add(il.append(this._op == 0 ? new IF_ICMPNE(null) : new IF_ICMPEQ(null)));
/*     */     }
/* 203 */     else if ((tleft instanceof NumberType)) {
/* 204 */       this._left.translate(classGen, methodGen);
/* 205 */       this._right.translate(classGen, methodGen);
/*     */ 
/* 207 */       if ((tleft instanceof RealType)) {
/* 208 */         il.append(DCMPG);
/* 209 */         this._falseList.add(il.append(this._op == 0 ? new IFNE(null) : new IFEQ(null)));
/*     */       }
/*     */       else
/*     */       {
/* 214 */         this._falseList.add(il.append(this._op == 0 ? new IF_ICMPNE(null) : new IF_ICMPEQ(null)));
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 220 */       translate(classGen, methodGen);
/* 221 */       desynthesize(classGen, methodGen);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 226 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 227 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 229 */     Type tleft = this._left.getType();
/* 230 */     Type tright = this._right.getType();
/*     */ 
/* 232 */     if (((tleft instanceof BooleanType)) || ((tleft instanceof NumberType))) {
/* 233 */       translateDesynthesized(classGen, methodGen);
/* 234 */       synthesize(classGen, methodGen);
/* 235 */       return;
/*     */     }
/*     */ 
/* 238 */     if ((tleft instanceof StringType)) {
/* 239 */       int equals = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
/*     */ 
/* 242 */       this._left.translate(classGen, methodGen);
/* 243 */       this._right.translate(classGen, methodGen);
/* 244 */       il.append(new INVOKEVIRTUAL(equals));
/*     */ 
/* 246 */       if (this._op == 1) {
/* 247 */         il.append(ICONST_1);
/* 248 */         il.append(IXOR);
/*     */       }
/* 250 */       return;
/*     */     }
/*     */ 
/* 255 */     if ((tleft instanceof ResultTreeType)) {
/* 256 */       if ((tright instanceof BooleanType)) {
/* 257 */         this._right.translate(classGen, methodGen);
/* 258 */         if (this._op == 1) {
/* 259 */           il.append(ICONST_1);
/* 260 */           il.append(IXOR);
/*     */         }
/* 262 */         return;
/*     */       }
/*     */ 
/* 265 */       if ((tright instanceof RealType)) {
/* 266 */         this._left.translate(classGen, methodGen);
/* 267 */         tleft.translateTo(classGen, methodGen, Type.Real);
/* 268 */         this._right.translate(classGen, methodGen);
/*     */ 
/* 270 */         il.append(DCMPG);
/* 271 */         BranchHandle falsec = il.append(this._op == 0 ? new IFNE(null) : new IFEQ(null));
/*     */ 
/* 274 */         il.append(ICONST_1);
/* 275 */         BranchHandle truec = il.append(new GOTO(null));
/* 276 */         falsec.setTarget(il.append(ICONST_0));
/* 277 */         truec.setTarget(il.append(NOP));
/* 278 */         return;
/*     */       }
/*     */ 
/* 283 */       this._left.translate(classGen, methodGen);
/* 284 */       tleft.translateTo(classGen, methodGen, Type.String);
/* 285 */       this._right.translate(classGen, methodGen);
/*     */ 
/* 287 */       if ((tright instanceof ResultTreeType)) {
/* 288 */         tright.translateTo(classGen, methodGen, Type.String);
/*     */       }
/*     */ 
/* 291 */       int equals = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
/*     */ 
/* 294 */       il.append(new INVOKEVIRTUAL(equals));
/*     */ 
/* 296 */       if (this._op == 1) {
/* 297 */         il.append(ICONST_1);
/* 298 */         il.append(IXOR);
/*     */       }
/* 300 */       return;
/*     */     }
/*     */ 
/* 303 */     if (((tleft instanceof NodeSetType)) && ((tright instanceof BooleanType))) {
/* 304 */       this._left.translate(classGen, methodGen);
/* 305 */       this._left.startIterator(classGen, methodGen);
/* 306 */       Type.NodeSet.translateTo(classGen, methodGen, Type.Boolean);
/* 307 */       this._right.translate(classGen, methodGen);
/*     */ 
/* 309 */       il.append(IXOR);
/* 310 */       if (this._op == 0) {
/* 311 */         il.append(ICONST_1);
/* 312 */         il.append(IXOR);
/*     */       }
/* 314 */       return;
/*     */     }
/*     */ 
/* 317 */     if (((tleft instanceof NodeSetType)) && ((tright instanceof StringType))) {
/* 318 */       this._left.translate(classGen, methodGen);
/* 319 */       this._left.startIterator(classGen, methodGen);
/* 320 */       this._right.translate(classGen, methodGen);
/* 321 */       il.append(new PUSH(cpg, this._op));
/* 322 */       il.append(methodGen.loadDOM());
/* 323 */       int cmp = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "compare", "(" + tleft.toSignature() + tright.toSignature() + "I" + "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;" + ")Z");
/*     */ 
/* 331 */       il.append(new INVOKESTATIC(cmp));
/* 332 */       return;
/*     */     }
/*     */ 
/* 336 */     this._left.translate(classGen, methodGen);
/* 337 */     this._left.startIterator(classGen, methodGen);
/* 338 */     this._right.translate(classGen, methodGen);
/* 339 */     this._right.startIterator(classGen, methodGen);
/*     */ 
/* 342 */     if ((tright instanceof ResultTreeType)) {
/* 343 */       tright.translateTo(classGen, methodGen, Type.String);
/* 344 */       tright = Type.String;
/*     */     }
/*     */ 
/* 348 */     il.append(new PUSH(cpg, this._op));
/* 349 */     il.append(methodGen.loadDOM());
/*     */ 
/* 351 */     int compare = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "compare", "(" + tleft.toSignature() + tright.toSignature() + "I" + "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;" + ")Z");
/*     */ 
/* 359 */     il.append(new INVOKESTATIC(compare));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.EqualityExpr
 * JD-Core Version:    0.6.2
 */
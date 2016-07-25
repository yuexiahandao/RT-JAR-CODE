/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class RelationalExpr extends Expression
/*     */ {
/*     */   private int _op;
/*     */   private Expression _left;
/*     */   private Expression _right;
/*     */ 
/*     */   public RelationalExpr(int op, Expression left, Expression right)
/*     */   {
/*  56 */     this._op = op;
/*  57 */     (this._left = left).setParent(this);
/*  58 */     (this._right = right).setParent(this);
/*     */   }
/*     */ 
/*     */   public void setParser(Parser parser) {
/*  62 */     super.setParser(parser);
/*  63 */     this._left.setParser(parser);
/*  64 */     this._right.setParser(parser);
/*     */   }
/*     */ 
/*     */   public boolean hasPositionCall()
/*     */   {
/*  72 */     if (this._left.hasPositionCall()) return true;
/*  73 */     if (this._right.hasPositionCall()) return true;
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasLastCall()
/*     */   {
/*  81 */     return (this._left.hasLastCall()) || (this._right.hasLastCall());
/*     */   }
/*     */ 
/*     */   public boolean hasReferenceArgs() {
/*  85 */     return ((this._left.getType() instanceof ReferenceType)) || ((this._right.getType() instanceof ReferenceType));
/*     */   }
/*     */ 
/*     */   public boolean hasNodeArgs()
/*     */   {
/*  90 */     return ((this._left.getType() instanceof NodeType)) || ((this._right.getType() instanceof NodeType));
/*     */   }
/*     */ 
/*     */   public boolean hasNodeSetArgs()
/*     */   {
/*  95 */     return ((this._left.getType() instanceof NodeSetType)) || ((this._right.getType() instanceof NodeSetType));
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable) throws TypeCheckError
/*     */   {
/* 100 */     Type tleft = this._left.typeCheck(stable);
/* 101 */     Type tright = this._right.typeCheck(stable);
/*     */ 
/* 104 */     if (((tleft instanceof ResultTreeType)) && ((tright instanceof ResultTreeType)))
/*     */     {
/* 107 */       this._right = new CastExpr(this._right, Type.Real);
/* 108 */       this._left = new CastExpr(this._left, Type.Real);
/* 109 */       return this._type = Type.Boolean;
/*     */     }
/*     */ 
/* 113 */     if (hasReferenceArgs()) {
/* 114 */       Type type = null;
/* 115 */       Type typeL = null;
/* 116 */       Type typeR = null;
/* 117 */       if (((tleft instanceof ReferenceType)) && 
/* 118 */         ((this._left instanceof VariableRefBase))) {
/* 119 */         VariableRefBase ref = (VariableRefBase)this._left;
/* 120 */         VariableBase var = ref.getVariable();
/* 121 */         typeL = var.getType();
/*     */       }
/*     */ 
/* 124 */       if (((tright instanceof ReferenceType)) && 
/* 125 */         ((this._right instanceof VariableRefBase))) {
/* 126 */         VariableRefBase ref = (VariableRefBase)this._right;
/* 127 */         VariableBase var = ref.getVariable();
/* 128 */         typeR = var.getType();
/*     */       }
/*     */ 
/* 132 */       if (typeL == null)
/* 133 */         type = typeR;
/* 134 */       else if (typeR == null)
/* 135 */         type = typeL;
/*     */       else {
/* 137 */         type = Type.Real;
/*     */       }
/* 139 */       if (type == null) type = Type.Real;
/*     */ 
/* 141 */       this._right = new CastExpr(this._right, type);
/* 142 */       this._left = new CastExpr(this._left, type);
/* 143 */       return this._type = Type.Boolean;
/*     */     }
/*     */ 
/* 146 */     if (hasNodeSetArgs())
/*     */     {
/* 148 */       if ((tright instanceof NodeSetType)) {
/* 149 */         Expression temp = this._right; this._right = this._left; this._left = temp;
/* 150 */         this._op = (this._op == 4 ? 5 : this._op == 3 ? 2 : this._op == 2 ? 3 : 4);
/*     */ 
/* 153 */         tright = this._right.getType();
/*     */       }
/*     */ 
/* 157 */       if ((tright instanceof NodeType)) {
/* 158 */         this._right = new CastExpr(this._right, Type.NodeSet);
/*     */       }
/*     */ 
/* 161 */       if ((tright instanceof IntType)) {
/* 162 */         this._right = new CastExpr(this._right, Type.Real);
/*     */       }
/*     */ 
/* 165 */       if ((tright instanceof ResultTreeType)) {
/* 166 */         this._right = new CastExpr(this._right, Type.String);
/*     */       }
/* 168 */       return this._type = Type.Boolean;
/*     */     }
/*     */ 
/* 172 */     if (hasNodeArgs()) {
/* 173 */       if ((tleft instanceof BooleanType)) {
/* 174 */         this._right = new CastExpr(this._right, Type.Boolean);
/* 175 */         tright = Type.Boolean;
/*     */       }
/* 177 */       if ((tright instanceof BooleanType)) {
/* 178 */         this._left = new CastExpr(this._left, Type.Boolean);
/* 179 */         tleft = Type.Boolean;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 184 */     MethodType ptype = lookupPrimop(stable, Operators.getOpNames(this._op), new MethodType(Type.Void, tleft, tright));
/*     */ 
/* 187 */     if (ptype != null) {
/* 188 */       Type arg1 = (Type)ptype.argsType().elementAt(0);
/* 189 */       if (!arg1.identicalTo(tleft)) {
/* 190 */         this._left = new CastExpr(this._left, arg1);
/*     */       }
/* 192 */       Type arg2 = (Type)ptype.argsType().elementAt(1);
/* 193 */       if (!arg2.identicalTo(tright)) {
/* 194 */         this._right = new CastExpr(this._right, arg1);
/*     */       }
/* 196 */       return this._type = ptype.resultType();
/*     */     }
/* 198 */     throw new TypeCheckError(this);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 202 */     if ((hasNodeSetArgs()) || (hasReferenceArgs())) {
/* 203 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 204 */       InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 207 */       this._left.translate(classGen, methodGen);
/* 208 */       this._left.startIterator(classGen, methodGen);
/* 209 */       this._right.translate(classGen, methodGen);
/* 210 */       this._right.startIterator(classGen, methodGen);
/*     */ 
/* 212 */       il.append(new PUSH(cpg, this._op));
/* 213 */       il.append(methodGen.loadDOM());
/*     */ 
/* 215 */       int index = cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary", "compare", "(" + this._left.getType().toSignature() + this._right.getType().toSignature() + "I" + "Lcom/sun/org/apache/xalan/internal/xsltc/DOM;" + ")Z");
/*     */ 
/* 222 */       il.append(new INVOKESTATIC(index));
/*     */     }
/*     */     else {
/* 225 */       translateDesynthesized(classGen, methodGen);
/* 226 */       synthesize(classGen, methodGen);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 232 */     if ((hasNodeSetArgs()) || (hasReferenceArgs())) {
/* 233 */       translate(classGen, methodGen);
/* 234 */       desynthesize(classGen, methodGen);
/*     */     }
/*     */     else {
/* 237 */       BranchInstruction bi = null;
/* 238 */       InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 240 */       this._left.translate(classGen, methodGen);
/* 241 */       this._right.translate(classGen, methodGen);
/*     */ 
/* 245 */       boolean tozero = false;
/* 246 */       Type tleft = this._left.getType();
/*     */ 
/* 248 */       if ((tleft instanceof RealType)) {
/* 249 */         il.append(tleft.CMP((this._op == 3) || (this._op == 5)));
/* 250 */         tleft = Type.Int;
/* 251 */         tozero = true;
/*     */       }
/*     */ 
/* 254 */       switch (this._op) {
/*     */       case 3:
/* 256 */         bi = tleft.GE(tozero);
/* 257 */         break;
/*     */       case 2:
/* 260 */         bi = tleft.LE(tozero);
/* 261 */         break;
/*     */       case 5:
/* 264 */         bi = tleft.GT(tozero);
/* 265 */         break;
/*     */       case 4:
/* 268 */         bi = tleft.LT(tozero);
/* 269 */         break;
/*     */       default:
/* 272 */         ErrorMsg msg = new ErrorMsg("ILLEGAL_RELAT_OP_ERR", this);
/* 273 */         getParser().reportError(2, msg);
/*     */       }
/*     */ 
/* 276 */       this._falseList.add(il.append(bi));
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 281 */     return Operators.getOpNames(this._op) + '(' + this._left + ", " + this._right + ')';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.RelationalExpr
 * JD-Core Version:    0.6.2
 */
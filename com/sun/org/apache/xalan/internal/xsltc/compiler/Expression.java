/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO_W;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ abstract class Expression extends SyntaxTreeNode
/*     */ {
/*     */   protected Type _type;
/*  59 */   protected FlowList _trueList = new FlowList();
/*     */ 
/*  64 */   protected FlowList _falseList = new FlowList();
/*     */ 
/*     */   public Type getType() {
/*  67 */     return this._type;
/*     */   }
/*     */ 
/*     */   public abstract String toString();
/*     */ 
/*     */   public boolean hasPositionCall() {
/*  73 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasLastCall() {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   public Object evaluateAtCompileTime()
/*     */   {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/*  93 */     return typeCheckContents(stable);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 100 */     ErrorMsg msg = new ErrorMsg("NOT_IMPLEMENTED_ERR", getClass(), this);
/*     */ 
/* 102 */     getParser().reportError(2, msg);
/*     */   }
/*     */ 
/*     */   public final InstructionList compile(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 111 */     InstructionList save = methodGen.getInstructionList();
/*     */     InstructionList result;
/* 112 */     methodGen.setInstructionList(result = new InstructionList());
/* 113 */     translate(classGen, methodGen);
/* 114 */     methodGen.setInstructionList(save);
/* 115 */     return result;
/*     */   }
/*     */ 
/*     */   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 123 */     translate(classGen, methodGen);
/* 124 */     if ((this._type instanceof BooleanType))
/* 125 */       desynthesize(classGen, methodGen);
/*     */   }
/*     */ 
/*     */   public void startIterator(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 136 */     if (!(this._type instanceof NodeSetType)) {
/* 137 */       return;
/*     */     }
/*     */ 
/* 141 */     Expression expr = this;
/* 142 */     if ((expr instanceof CastExpr)) {
/* 143 */       expr = ((CastExpr)expr).getExpr();
/*     */     }
/* 145 */     if (!(expr instanceof VariableRefBase)) {
/* 146 */       InstructionList il = methodGen.getInstructionList();
/* 147 */       il.append(methodGen.loadContextNode());
/* 148 */       il.append(methodGen.setStartNode());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void synthesize(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 158 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 159 */     InstructionList il = methodGen.getInstructionList();
/* 160 */     this._trueList.backPatch(il.append(ICONST_1));
/* 161 */     BranchHandle truec = il.append(new GOTO_W(null));
/* 162 */     this._falseList.backPatch(il.append(ICONST_0));
/* 163 */     truec.setTarget(il.append(NOP));
/*     */   }
/*     */ 
/*     */   public void desynthesize(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 168 */     InstructionList il = methodGen.getInstructionList();
/* 169 */     this._falseList.add(il.append(new IFEQ(null)));
/*     */   }
/*     */ 
/*     */   public FlowList getFalseList() {
/* 173 */     return this._falseList;
/*     */   }
/*     */ 
/*     */   public FlowList getTrueList() {
/* 177 */     return this._trueList;
/*     */   }
/*     */ 
/*     */   public void backPatchFalseList(InstructionHandle ih) {
/* 181 */     this._falseList.backPatch(ih);
/*     */   }
/*     */ 
/*     */   public void backPatchTrueList(InstructionHandle ih) {
/* 185 */     this._trueList.backPatch(ih);
/*     */   }
/*     */ 
/*     */   public MethodType lookupPrimop(SymbolTable stable, String op, MethodType ctype)
/*     */   {
/* 197 */     MethodType result = null;
/* 198 */     Vector primop = stable.lookupPrimop(op);
/* 199 */     if (primop != null) {
/* 200 */       int n = primop.size();
/* 201 */       int minDistance = 2147483647;
/* 202 */       for (int i = 0; i < n; i++) {
/* 203 */         MethodType ptype = (MethodType)primop.elementAt(i);
/*     */ 
/* 205 */         if (ptype.argsCount() == ctype.argsCount())
/*     */         {
/* 210 */           if (result == null) {
/* 211 */             result = ptype;
/*     */           }
/*     */ 
/* 215 */           int distance = ctype.distanceTo(ptype);
/* 216 */           if (distance < minDistance) {
/* 217 */             minDistance = distance;
/* 218 */             result = ptype;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 222 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.IF_ICMPNE;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.SIPUSH;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MultiHashtable;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ 
/*     */ final class CastExpr extends Expression
/*     */ {
/*     */   private final Expression _left;
/*  54 */   private static MultiHashtable InternalTypeMap = new MultiHashtable();
/*     */ 
/* 123 */   private boolean _typeTest = false;
/*     */ 
/*     */   public CastExpr(Expression left, Type type)
/*     */     throws TypeCheckError
/*     */   {
/* 130 */     this._left = left;
/* 131 */     this._type = type;
/*     */ 
/* 133 */     if (((this._left instanceof Step)) && (this._type == Type.Boolean)) {
/* 134 */       Step step = (Step)this._left;
/* 135 */       if ((step.getAxis() == 13) && (step.getNodeType() != -1)) {
/* 136 */         this._typeTest = true;
/*     */       }
/*     */     }
/*     */ 
/* 140 */     setParser(left.getParser());
/* 141 */     setParent(left.getParent());
/* 142 */     left.setParent(this);
/* 143 */     typeCheck(left.getParser().getSymbolTable());
/*     */   }
/*     */ 
/*     */   public Expression getExpr() {
/* 147 */     return this._left;
/*     */   }
/*     */ 
/*     */   public boolean hasPositionCall()
/*     */   {
/* 155 */     return this._left.hasPositionCall();
/*     */   }
/*     */ 
/*     */   public boolean hasLastCall() {
/* 159 */     return this._left.hasLastCall();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 163 */     return "cast(" + this._left + ", " + this._type + ")";
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 173 */     Type tleft = this._left.getType();
/* 174 */     if (tleft == null) {
/* 175 */       tleft = this._left.typeCheck(stable);
/*     */     }
/* 177 */     if ((tleft instanceof NodeType)) {
/* 178 */       tleft = Type.Node;
/*     */     }
/* 180 */     else if ((tleft instanceof ResultTreeType)) {
/* 181 */       tleft = Type.ResultTree;
/*     */     }
/* 183 */     if (InternalTypeMap.maps(tleft, this._type) != null) {
/* 184 */       return this._type;
/*     */     }
/*     */ 
/* 187 */     throw new TypeCheckError(new ErrorMsg("DATA_CONVERSION_ERR", tleft.toString(), this._type.toString()));
/*     */   }
/*     */ 
/*     */   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 194 */     Type ltype = this._left.getType();
/*     */ 
/* 199 */     if (this._typeTest) {
/* 200 */       ConstantPoolGen cpg = classGen.getConstantPool();
/* 201 */       InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 203 */       int idx = cpg.addInterfaceMethodref("com.sun.org.apache.xalan.internal.xsltc.DOM", "getExpandedTypeID", "(I)I");
/*     */ 
/* 206 */       il.append(new SIPUSH((short)((Step)this._left).getNodeType()));
/* 207 */       il.append(methodGen.loadDOM());
/* 208 */       il.append(methodGen.loadContextNode());
/* 209 */       il.append(new INVOKEINTERFACE(idx, 2));
/* 210 */       this._falseList.add(il.append(new IF_ICMPNE(null)));
/*     */     }
/*     */     else
/*     */     {
/* 214 */       this._left.translate(classGen, methodGen);
/* 215 */       if (this._type != ltype) {
/* 216 */         this._left.startIterator(classGen, methodGen);
/* 217 */         if ((this._type instanceof BooleanType)) {
/* 218 */           FlowList fl = ltype.translateToDesynthesized(classGen, methodGen, this._type);
/*     */ 
/* 220 */           if (fl != null)
/* 221 */             this._falseList.append(fl);
/*     */         }
/*     */         else
/*     */         {
/* 225 */           ltype.translateTo(classGen, methodGen, this._type);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 232 */     Type ltype = this._left.getType();
/* 233 */     this._left.translate(classGen, methodGen);
/* 234 */     if (!this._type.identicalTo(ltype)) {
/* 235 */       this._left.startIterator(classGen, methodGen);
/* 236 */       ltype.translateTo(classGen, methodGen, this._type);
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  58 */     InternalTypeMap.put(Type.Boolean, Type.Boolean);
/*  59 */     InternalTypeMap.put(Type.Boolean, Type.Real);
/*  60 */     InternalTypeMap.put(Type.Boolean, Type.String);
/*  61 */     InternalTypeMap.put(Type.Boolean, Type.Reference);
/*  62 */     InternalTypeMap.put(Type.Boolean, Type.Object);
/*     */ 
/*  64 */     InternalTypeMap.put(Type.Real, Type.Real);
/*  65 */     InternalTypeMap.put(Type.Real, Type.Int);
/*  66 */     InternalTypeMap.put(Type.Real, Type.Boolean);
/*  67 */     InternalTypeMap.put(Type.Real, Type.String);
/*  68 */     InternalTypeMap.put(Type.Real, Type.Reference);
/*  69 */     InternalTypeMap.put(Type.Real, Type.Object);
/*     */ 
/*  71 */     InternalTypeMap.put(Type.Int, Type.Int);
/*  72 */     InternalTypeMap.put(Type.Int, Type.Real);
/*  73 */     InternalTypeMap.put(Type.Int, Type.Boolean);
/*  74 */     InternalTypeMap.put(Type.Int, Type.String);
/*  75 */     InternalTypeMap.put(Type.Int, Type.Reference);
/*  76 */     InternalTypeMap.put(Type.Int, Type.Object);
/*     */ 
/*  78 */     InternalTypeMap.put(Type.String, Type.String);
/*  79 */     InternalTypeMap.put(Type.String, Type.Boolean);
/*  80 */     InternalTypeMap.put(Type.String, Type.Real);
/*  81 */     InternalTypeMap.put(Type.String, Type.Reference);
/*  82 */     InternalTypeMap.put(Type.String, Type.Object);
/*     */ 
/*  84 */     InternalTypeMap.put(Type.NodeSet, Type.NodeSet);
/*  85 */     InternalTypeMap.put(Type.NodeSet, Type.Boolean);
/*  86 */     InternalTypeMap.put(Type.NodeSet, Type.Real);
/*  87 */     InternalTypeMap.put(Type.NodeSet, Type.String);
/*  88 */     InternalTypeMap.put(Type.NodeSet, Type.Node);
/*  89 */     InternalTypeMap.put(Type.NodeSet, Type.Reference);
/*  90 */     InternalTypeMap.put(Type.NodeSet, Type.Object);
/*     */ 
/*  92 */     InternalTypeMap.put(Type.Node, Type.Node);
/*  93 */     InternalTypeMap.put(Type.Node, Type.Boolean);
/*  94 */     InternalTypeMap.put(Type.Node, Type.Real);
/*  95 */     InternalTypeMap.put(Type.Node, Type.String);
/*  96 */     InternalTypeMap.put(Type.Node, Type.NodeSet);
/*  97 */     InternalTypeMap.put(Type.Node, Type.Reference);
/*  98 */     InternalTypeMap.put(Type.Node, Type.Object);
/*     */ 
/* 100 */     InternalTypeMap.put(Type.ResultTree, Type.ResultTree);
/* 101 */     InternalTypeMap.put(Type.ResultTree, Type.Boolean);
/* 102 */     InternalTypeMap.put(Type.ResultTree, Type.Real);
/* 103 */     InternalTypeMap.put(Type.ResultTree, Type.String);
/* 104 */     InternalTypeMap.put(Type.ResultTree, Type.NodeSet);
/* 105 */     InternalTypeMap.put(Type.ResultTree, Type.Reference);
/* 106 */     InternalTypeMap.put(Type.ResultTree, Type.Object);
/*     */ 
/* 108 */     InternalTypeMap.put(Type.Reference, Type.Reference);
/* 109 */     InternalTypeMap.put(Type.Reference, Type.Boolean);
/* 110 */     InternalTypeMap.put(Type.Reference, Type.Int);
/* 111 */     InternalTypeMap.put(Type.Reference, Type.Real);
/* 112 */     InternalTypeMap.put(Type.Reference, Type.String);
/* 113 */     InternalTypeMap.put(Type.Reference, Type.Node);
/* 114 */     InternalTypeMap.put(Type.Reference, Type.NodeSet);
/* 115 */     InternalTypeMap.put(Type.Reference, Type.ResultTree);
/* 116 */     InternalTypeMap.put(Type.Reference, Type.Object);
/*     */ 
/* 118 */     InternalTypeMap.put(Type.Object, Type.String);
/*     */ 
/* 120 */     InternalTypeMap.put(Type.Void, Type.String);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.CastExpr
 * JD-Core Version:    0.6.2
 */
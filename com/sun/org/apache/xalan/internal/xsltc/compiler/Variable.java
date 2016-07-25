/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.DCONST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ICONST;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class Variable extends VariableBase
/*     */ {
/*     */   public int getIndex()
/*     */   {
/*  47 */     return this._local != null ? this._local.getIndex() : -1;
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/*  55 */     super.parseContents(parser);
/*     */ 
/*  58 */     SyntaxTreeNode parent = getParent();
/*  59 */     if ((parent instanceof Stylesheet))
/*     */     {
/*  61 */       this._isLocal = false;
/*     */ 
/*  63 */       Variable var = parser.getSymbolTable().lookupVariable(this._name);
/*     */ 
/*  65 */       if (var != null) {
/*  66 */         int us = getImportPrecedence();
/*  67 */         int them = var.getImportPrecedence();
/*     */ 
/*  69 */         if (us == them) {
/*  70 */           String name = this._name.toString();
/*  71 */           reportError(this, parser, "VARIABLE_REDEF_ERR", name);
/*     */         }
/*     */         else {
/*  74 */           if (them > us) {
/*  75 */             this._ignore = true;
/*  76 */             copyReferences(var);
/*  77 */             return;
/*     */           }
/*     */ 
/*  80 */           var.copyReferences(this);
/*  81 */           var.disable();
/*     */         }
/*     */       }
/*     */ 
/*  85 */       ((Stylesheet)parent).addVariable(this);
/*  86 */       parser.getSymbolTable().addVariable(this);
/*     */     }
/*     */     else {
/*  89 */       this._isLocal = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 100 */     if (this._select != null) {
/* 101 */       this._type = this._select.typeCheck(stable);
/*     */     }
/* 104 */     else if (hasContents()) {
/* 105 */       typeCheckContents(stable);
/* 106 */       this._type = Type.ResultTree;
/*     */     }
/*     */     else {
/* 109 */       this._type = Type.Reference;
/*     */     }
/*     */ 
/* 114 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void initialize(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 123 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 124 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 127 */     if ((isLocal()) && (!this._refs.isEmpty()))
/*     */     {
/* 129 */       if (this._local == null) {
/* 130 */         this._local = methodGen.addLocalVariable2(getEscapedName(), this._type.toJCType(), null);
/*     */       }
/*     */ 
/* 135 */       if (((this._type instanceof IntType)) || ((this._type instanceof NodeType)) || ((this._type instanceof BooleanType)))
/*     */       {
/* 138 */         il.append(new ICONST(0));
/* 139 */       } else if ((this._type instanceof RealType))
/* 140 */         il.append(new DCONST(0.0D));
/*     */       else {
/* 142 */         il.append(new ACONST_NULL());
/*     */       }
/*     */ 
/* 145 */       this._local.setStart(il.append(this._type.STORE(this._local.getIndex())));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/* 151 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 152 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 155 */     if (this._refs.isEmpty()) {
/* 156 */       this._ignore = true;
/*     */     }
/*     */ 
/* 160 */     if (this._ignore) return;
/* 161 */     this._ignore = true;
/*     */ 
/* 163 */     String name = getEscapedName();
/*     */ 
/* 165 */     if (isLocal())
/*     */     {
/* 167 */       translateValue(classGen, methodGen);
/*     */ 
/* 170 */       boolean createLocal = this._local == null;
/* 171 */       if (createLocal) {
/* 172 */         mapRegister(methodGen);
/*     */       }
/* 174 */       InstructionHandle storeInst = il.append(this._type.STORE(this._local.getIndex()));
/*     */ 
/* 181 */       if (createLocal)
/* 182 */         this._local.setStart(storeInst);
/*     */     }
/*     */     else
/*     */     {
/* 186 */       String signature = this._type.toSignature();
/*     */ 
/* 189 */       if (classGen.containsField(name) == null) {
/* 190 */         classGen.addField(new Field(1, cpg.addUtf8(name), cpg.addUtf8(signature), null, cpg.getConstantPool()));
/*     */ 
/* 196 */         il.append(classGen.loadTranslet());
/*     */ 
/* 198 */         translateValue(classGen, methodGen);
/*     */ 
/* 200 */         il.append(new PUTFIELD(cpg.addFieldref(classGen.getClassName(), name, signature)));
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Variable
 * JD-Core Version:    0.6.2
 */
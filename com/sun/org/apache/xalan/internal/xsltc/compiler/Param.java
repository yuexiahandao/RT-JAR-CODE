/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.classfile.Field;
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFNONNULL;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
/*     */ import com.sun.org.apache.bcel.internal.generic.Instruction;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUSH;
/*     */ import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ObjectType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class Param extends VariableBase
/*     */ {
/*  59 */   private boolean _isInSimpleNamedTemplate = false;
/*     */ 
/*     */   public String toString()
/*     */   {
/*  65 */     return "param(" + this._name + ")";
/*     */   }
/*     */ 
/*     */   public Instruction setLoadInstruction(Instruction instruction)
/*     */   {
/*  73 */     Instruction tmp = this._loadInstruction;
/*  74 */     this._loadInstruction = instruction;
/*  75 */     return tmp;
/*     */   }
/*     */ 
/*     */   public Instruction setStoreInstruction(Instruction instruction)
/*     */   {
/*  83 */     Instruction tmp = this._storeInstruction;
/*  84 */     this._storeInstruction = instruction;
/*  85 */     return tmp;
/*     */   }
/*     */ 
/*     */   public void display(int indent)
/*     */   {
/*  92 */     indent(indent);
/*  93 */     System.out.println("param " + this._name);
/*  94 */     if (this._select != null) {
/*  95 */       indent(indent + 4);
/*  96 */       System.out.println("select " + this._select.toString());
/*     */     }
/*  98 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public void parseContents(Parser parser)
/*     */   {
/* 108 */     super.parseContents(parser);
/*     */ 
/* 111 */     SyntaxTreeNode parent = getParent();
/* 112 */     if ((parent instanceof Stylesheet))
/*     */     {
/* 114 */       this._isLocal = false;
/*     */ 
/* 116 */       Param param = parser.getSymbolTable().lookupParam(this._name);
/*     */ 
/* 118 */       if (param != null) {
/* 119 */         int us = getImportPrecedence();
/* 120 */         int them = param.getImportPrecedence();
/*     */ 
/* 122 */         if (us == them) {
/* 123 */           String name = this._name.toString();
/* 124 */           reportError(this, parser, "VARIABLE_REDEF_ERR", name);
/*     */         }
/*     */         else {
/* 127 */           if (them > us) {
/* 128 */             this._ignore = true;
/* 129 */             copyReferences(param);
/* 130 */             return;
/*     */           }
/*     */ 
/* 133 */           param.copyReferences(this);
/* 134 */           param.disable();
/*     */         }
/*     */       }
/*     */ 
/* 138 */       ((Stylesheet)parent).addParam(this);
/* 139 */       parser.getSymbolTable().addParam(this);
/*     */     }
/* 141 */     else if ((parent instanceof Template)) {
/* 142 */       Template template = (Template)parent;
/* 143 */       this._isLocal = true;
/* 144 */       template.addParameter(this);
/* 145 */       if (template.isSimpleNamedTemplate())
/* 146 */         this._isInSimpleNamedTemplate = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Type typeCheck(SymbolTable stable)
/*     */     throws TypeCheckError
/*     */   {
/* 157 */     if (this._select != null) {
/* 158 */       this._type = this._select.typeCheck(stable);
/* 159 */       if ((!(this._type instanceof ReferenceType)) && (!(this._type instanceof ObjectType))) {
/* 160 */         this._select = new CastExpr(this._select, Type.Reference);
/*     */       }
/*     */     }
/* 163 */     else if (hasContents()) {
/* 164 */       typeCheckContents(stable);
/*     */     }
/* 166 */     this._type = Type.Reference;
/*     */ 
/* 170 */     return Type.Void;
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 174 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 175 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 177 */     if (this._ignore) return;
/* 178 */     this._ignore = true;
/*     */ 
/* 185 */     String name = BasisLibrary.mapQNameToJavaName(this._name.toString());
/* 186 */     String signature = this._type.toSignature();
/* 187 */     String className = this._type.getClassName();
/*     */ 
/* 189 */     if (isLocal())
/*     */     {
/* 195 */       if (this._isInSimpleNamedTemplate) {
/* 196 */         il.append(loadInstruction());
/* 197 */         BranchHandle ifBlock = il.append(new IFNONNULL(null));
/* 198 */         translateValue(classGen, methodGen);
/* 199 */         il.append(storeInstruction());
/* 200 */         ifBlock.setTarget(il.append(NOP));
/* 201 */         return;
/*     */       }
/*     */ 
/* 204 */       il.append(classGen.loadTranslet());
/* 205 */       il.append(new PUSH(cpg, name));
/* 206 */       translateValue(classGen, methodGen);
/* 207 */       il.append(new PUSH(cpg, true));
/*     */ 
/* 210 */       il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "addParameter", "(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/Object;")));
/*     */ 
/* 213 */       if (className != "") {
/* 214 */         il.append(new CHECKCAST(cpg.addClass(className)));
/*     */       }
/*     */ 
/* 217 */       this._type.translateUnBox(classGen, methodGen);
/*     */ 
/* 219 */       if (this._refs.isEmpty()) {
/* 220 */         il.append(this._type.POP());
/* 221 */         this._local = null;
/*     */       }
/*     */       else {
/* 224 */         this._local = methodGen.addLocalVariable2(name, this._type.toJCType(), il.getEnd());
/*     */ 
/* 228 */         il.append(this._type.STORE(this._local.getIndex()));
/*     */       }
/*     */ 
/*     */     }
/* 232 */     else if (classGen.containsField(name) == null) {
/* 233 */       classGen.addField(new Field(1, cpg.addUtf8(name), cpg.addUtf8(signature), null, cpg.getConstantPool()));
/*     */ 
/* 236 */       il.append(classGen.loadTranslet());
/* 237 */       il.append(DUP);
/* 238 */       il.append(new PUSH(cpg, name));
/* 239 */       translateValue(classGen, methodGen);
/* 240 */       il.append(new PUSH(cpg, true));
/*     */ 
/* 243 */       il.append(new INVOKEVIRTUAL(cpg.addMethodref("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet", "addParameter", "(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/Object;")));
/*     */ 
/* 247 */       this._type.translateUnBox(classGen, methodGen);
/*     */ 
/* 250 */       if (className != "") {
/* 251 */         il.append(new CHECKCAST(cpg.addClass(className)));
/*     */       }
/* 253 */       il.append(new PUTFIELD(cpg.addFieldref(classGen.getClassName(), name, signature)));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Param
 * JD-Core Version:    0.6.2
 */
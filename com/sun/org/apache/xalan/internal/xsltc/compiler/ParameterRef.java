/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*     */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*     */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*     */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ 
/*     */ final class ParameterRef extends VariableRefBase
/*     */ {
/*  47 */   QName _name = null;
/*     */ 
/*     */   public ParameterRef(Param param) {
/*  50 */     super(param);
/*  51 */     this._name = param._name;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  56 */     return "parameter-ref(" + this._variable.getName() + '/' + this._variable.getType() + ')';
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/*  60 */     ConstantPoolGen cpg = classGen.getConstantPool();
/*  61 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/*  68 */     String name = BasisLibrary.mapQNameToJavaName(this._name.toString());
/*  69 */     String signature = this._type.toSignature();
/*     */ 
/*  71 */     if (this._variable.isLocal()) {
/*  72 */       if (classGen.isExternal()) {
/*  73 */         Closure variableClosure = this._closure;
/*  74 */         while ((variableClosure != null) && 
/*  75 */           (!variableClosure.inInnerClass())) {
/*  76 */           variableClosure = variableClosure.getParentClosure();
/*     */         }
/*     */ 
/*  79 */         if (variableClosure != null) {
/*  80 */           il.append(ALOAD_0);
/*  81 */           il.append(new GETFIELD(cpg.addFieldref(variableClosure.getInnerClassName(), name, signature)));
/*     */         }
/*     */         else
/*     */         {
/*  86 */           il.append(this._variable.loadInstruction());
/*     */         }
/*     */       }
/*     */       else {
/*  90 */         il.append(this._variable.loadInstruction());
/*     */       }
/*     */     }
/*     */     else {
/*  94 */       String className = classGen.getClassName();
/*  95 */       il.append(classGen.loadTranslet());
/*  96 */       if (classGen.isExternal()) {
/*  97 */         il.append(new CHECKCAST(cpg.addClass(className)));
/*     */       }
/*  99 */       il.append(new GETFIELD(cpg.addFieldref(className, name, signature)));
/*     */     }
/*     */ 
/* 102 */     if ((this._variable.getType() instanceof NodeSetType))
/*     */     {
/* 104 */       int clone = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "cloneIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*     */ 
/* 108 */       il.append(new INVOKEINTERFACE(clone, 1));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.ParameterRef
 * JD-Core Version:    0.6.2
 */
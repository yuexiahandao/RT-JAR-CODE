/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
/*    */ import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
/*    */ import com.sun.org.apache.bcel.internal.generic.GETFIELD;
/*    */ import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
/*    */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
/*    */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*    */ 
/*    */ final class VariableRef extends VariableRefBase
/*    */ {
/*    */   public VariableRef(Variable variable)
/*    */   {
/* 44 */     super(variable);
/*    */   }
/*    */ 
/*    */   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
/* 48 */     ConstantPoolGen cpg = classGen.getConstantPool();
/* 49 */     InstructionList il = methodGen.getInstructionList();
/*    */ 
/* 52 */     if (this._type.implementedAsMethod()) return;
/*    */ 
/* 54 */     String name = this._variable.getEscapedName();
/* 55 */     String signature = this._type.toSignature();
/*    */ 
/* 57 */     if (this._variable.isLocal()) {
/* 58 */       if (classGen.isExternal()) {
/* 59 */         Closure variableClosure = this._closure;
/* 60 */         while ((variableClosure != null) && 
/* 61 */           (!variableClosure.inInnerClass())) {
/* 62 */           variableClosure = variableClosure.getParentClosure();
/*    */         }
/*    */ 
/* 65 */         if (variableClosure != null) {
/* 66 */           il.append(ALOAD_0);
/* 67 */           il.append(new GETFIELD(cpg.addFieldref(variableClosure.getInnerClassName(), name, signature)));
/*    */         }
/*    */         else
/*    */         {
/* 72 */           il.append(this._variable.loadInstruction());
/*    */         }
/*    */       }
/*    */       else {
/* 76 */         il.append(this._variable.loadInstruction());
/*    */       }
/*    */     }
/*    */     else {
/* 80 */       String className = classGen.getClassName();
/* 81 */       il.append(classGen.loadTranslet());
/* 82 */       if (classGen.isExternal()) {
/* 83 */         il.append(new CHECKCAST(cpg.addClass(className)));
/*    */       }
/* 85 */       il.append(new GETFIELD(cpg.addFieldref(className, name, signature)));
/*    */     }
/*    */ 
/* 88 */     if ((this._variable.getType() instanceof NodeSetType))
/*    */     {
/* 90 */       int clone = cpg.addInterfaceMethodref("com.sun.org.apache.xml.internal.dtm.DTMAxisIterator", "cloneIterator", "()Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
/*    */ 
/* 94 */       il.append(new INVOKEINTERFACE(clone, 1));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.VariableRef
 * JD-Core Version:    0.6.2
 */
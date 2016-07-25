/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.GOTO;
/*     */ import com.sun.org.apache.bcel.internal.generic.IFEQ;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class Choose extends Instruction
/*     */ {
/*     */   public void display(int indent)
/*     */   {
/*  52 */     indent(indent);
/*  53 */     Util.println("Choose");
/*  54 */     indent(indent + 4);
/*  55 */     displayContents(indent + 4);
/*     */   }
/*     */ 
/*     */   public void translate(ClassGenerator classGen, MethodGenerator methodGen)
/*     */   {
/*  63 */     Vector whenElements = new Vector();
/*  64 */     Otherwise otherwise = null;
/*  65 */     Enumeration elements = elements();
/*     */ 
/*  68 */     ErrorMsg error = null;
/*  69 */     int line = getLineNumber();
/*     */ 
/*  72 */     while (elements.hasMoreElements()) {
/*  73 */       Object element = elements.nextElement();
/*     */ 
/*  75 */       if ((element instanceof When)) {
/*  76 */         whenElements.addElement(element);
/*     */       }
/*  79 */       else if ((element instanceof Otherwise)) {
/*  80 */         if (otherwise == null) {
/*  81 */           otherwise = (Otherwise)element;
/*     */         }
/*     */         else {
/*  84 */           error = new ErrorMsg("MULTIPLE_OTHERWISE_ERR", this);
/*  85 */           getParser().reportError(3, error);
/*     */         }
/*     */       }
/*  88 */       else if ((element instanceof Text)) {
/*  89 */         ((Text)element).ignore();
/*     */       }
/*     */       else
/*     */       {
/*  93 */         error = new ErrorMsg("WHEN_ELEMENT_ERR", this);
/*  94 */         getParser().reportError(3, error);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  99 */     if (whenElements.size() == 0) {
/* 100 */       error = new ErrorMsg("MISSING_WHEN_ERR", this);
/* 101 */       getParser().reportError(3, error);
/* 102 */       return;
/*     */     }
/*     */ 
/* 105 */     InstructionList il = methodGen.getInstructionList();
/*     */ 
/* 109 */     BranchHandle nextElement = null;
/* 110 */     Vector exitHandles = new Vector();
/* 111 */     InstructionHandle exit = null;
/*     */ 
/* 113 */     Enumeration whens = whenElements.elements();
/* 114 */     while (whens.hasMoreElements()) {
/* 115 */       When when = (When)whens.nextElement();
/* 116 */       Expression test = when.getTest();
/*     */ 
/* 118 */       InstructionHandle truec = il.getEnd();
/*     */ 
/* 120 */       if (nextElement != null)
/* 121 */         nextElement.setTarget(il.append(NOP));
/* 122 */       test.translateDesynthesized(classGen, methodGen);
/*     */ 
/* 124 */       if ((test instanceof FunctionCall)) {
/* 125 */         FunctionCall call = (FunctionCall)test;
/*     */         try {
/* 127 */           Type type = call.typeCheck(getParser().getSymbolTable());
/* 128 */           if (type != Type.Boolean) {
/* 129 */             test._falseList.add(il.append(new IFEQ(null)));
/*     */           }
/*     */         }
/*     */         catch (TypeCheckError e)
/*     */         {
/*     */         }
/*     */       }
/*     */ 
/* 137 */       truec = il.getEnd();
/*     */ 
/* 141 */       if (!when.ignore()) when.translateContents(classGen, methodGen);
/*     */ 
/* 144 */       exitHandles.addElement(il.append(new GOTO(null)));
/* 145 */       if ((whens.hasMoreElements()) || (otherwise != null)) {
/* 146 */         nextElement = il.append(new GOTO(null));
/* 147 */         test.backPatchFalseList(nextElement);
/*     */       }
/*     */       else {
/* 150 */         test.backPatchFalseList(exit = il.append(NOP));
/* 151 */       }test.backPatchTrueList(truec.getNext());
/*     */     }
/*     */ 
/* 155 */     if (otherwise != null) {
/* 156 */       nextElement.setTarget(il.append(NOP));
/* 157 */       otherwise.translateContents(classGen, methodGen);
/* 158 */       exit = il.append(NOP);
/*     */     }
/*     */ 
/* 162 */     Enumeration exitGotos = exitHandles.elements();
/* 163 */     while (exitGotos.hasMoreElements()) {
/* 164 */       BranchHandle gotoExit = (BranchHandle)exitGotos.nextElement();
/* 165 */       gotoExit.setTarget(exit);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.Choose
 * JD-Core Version:    0.6.2
 */
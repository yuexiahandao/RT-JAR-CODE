/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.BranchHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
/*     */ import com.sun.org.apache.bcel.internal.generic.InstructionList;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class FlowList
/*     */ {
/*     */   private Vector _elements;
/*     */ 
/*     */   public FlowList()
/*     */   {
/*  41 */     this._elements = null;
/*     */   }
/*     */ 
/*     */   public FlowList(InstructionHandle bh) {
/*  45 */     this._elements = new Vector();
/*  46 */     this._elements.addElement(bh);
/*     */   }
/*     */ 
/*     */   public FlowList(FlowList list) {
/*  50 */     this._elements = list._elements;
/*     */   }
/*     */ 
/*     */   public FlowList add(InstructionHandle bh) {
/*  54 */     if (this._elements == null) {
/*  55 */       this._elements = new Vector();
/*     */     }
/*  57 */     this._elements.addElement(bh);
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */   public FlowList append(FlowList right) {
/*  62 */     if (this._elements == null) {
/*  63 */       this._elements = right._elements;
/*     */     }
/*     */     else {
/*  66 */       Vector temp = right._elements;
/*  67 */       if (temp != null) {
/*  68 */         int n = temp.size();
/*  69 */         for (int i = 0; i < n; i++) {
/*  70 */           this._elements.addElement(temp.elementAt(i));
/*     */         }
/*     */       }
/*     */     }
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   public void backPatch(InstructionHandle target)
/*     */   {
/*  81 */     if (this._elements != null) {
/*  82 */       int n = this._elements.size();
/*  83 */       for (int i = 0; i < n; i++) {
/*  84 */         BranchHandle bh = (BranchHandle)this._elements.elementAt(i);
/*  85 */         bh.setTarget(target);
/*     */       }
/*  87 */       this._elements.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   public FlowList copyAndRedirect(InstructionList oldList, InstructionList newList)
/*     */   {
/*  98 */     FlowList result = new FlowList();
/*  99 */     if (this._elements == null) {
/* 100 */       return result;
/*     */     }
/*     */ 
/* 103 */     int n = this._elements.size();
/* 104 */     Iterator oldIter = oldList.iterator();
/* 105 */     Iterator newIter = newList.iterator();
/*     */ 
/* 107 */     while (oldIter.hasNext()) {
/* 108 */       InstructionHandle oldIh = (InstructionHandle)oldIter.next();
/* 109 */       InstructionHandle newIh = (InstructionHandle)newIter.next();
/*     */ 
/* 111 */       for (int i = 0; i < n; i++) {
/* 112 */         if (this._elements.elementAt(i) == oldIh) {
/* 113 */           result.add(newIh);
/*     */         }
/*     */       }
/*     */     }
/* 117 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList
 * JD-Core Version:    0.6.2
 */
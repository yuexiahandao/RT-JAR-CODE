/*     */ package com.sun.org.apache.xerces.internal.impl.xs.models;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMStateSet;
/*     */ 
/*     */ public class XSCMBinOp extends CMNode
/*     */ {
/*     */   private CMNode fLeftChild;
/*     */   private CMNode fRightChild;
/*     */ 
/*     */   public XSCMBinOp(int type, CMNode leftNode, CMNode rightNode)
/*     */   {
/*  41 */     super(type);
/*     */ 
/*  44 */     if ((type() != 101) && (type() != 102))
/*     */     {
/*  46 */       throw new RuntimeException("ImplementationMessages.VAL_BST");
/*     */     }
/*     */ 
/*  50 */     this.fLeftChild = leftNode;
/*  51 */     this.fRightChild = rightNode;
/*     */   }
/*     */ 
/*     */   final CMNode getLeft()
/*     */   {
/*  59 */     return this.fLeftChild;
/*     */   }
/*     */ 
/*     */   final CMNode getRight() {
/*  63 */     return this.fRightChild;
/*     */   }
/*     */ 
/*     */   public boolean isNullable()
/*     */   {
/*  76 */     if (type() == 101)
/*  77 */       return (this.fLeftChild.isNullable()) || (this.fRightChild.isNullable());
/*  78 */     if (type() == 102) {
/*  79 */       return (this.fLeftChild.isNullable()) && (this.fRightChild.isNullable());
/*     */     }
/*  81 */     throw new RuntimeException("ImplementationMessages.VAL_BST");
/*     */   }
/*     */ 
/*     */   protected void calcFirstPos(CMStateSet toSet)
/*     */   {
/*  89 */     if (type() == 101)
/*     */     {
/*  91 */       toSet.setTo(this.fLeftChild.firstPos());
/*  92 */       toSet.union(this.fRightChild.firstPos());
/*     */     }
/*  94 */     else if (type() == 102)
/*     */     {
/* 100 */       toSet.setTo(this.fLeftChild.firstPos());
/* 101 */       if (this.fLeftChild.isNullable())
/* 102 */         toSet.union(this.fRightChild.firstPos());
/*     */     }
/*     */     else {
/* 105 */       throw new RuntimeException("ImplementationMessages.VAL_BST");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void calcLastPos(CMStateSet toSet) {
/* 110 */     if (type() == 101)
/*     */     {
/* 112 */       toSet.setTo(this.fLeftChild.lastPos());
/* 113 */       toSet.union(this.fRightChild.lastPos());
/*     */     }
/* 115 */     else if (type() == 102)
/*     */     {
/* 121 */       toSet.setTo(this.fRightChild.lastPos());
/* 122 */       if (this.fRightChild.isNullable())
/* 123 */         toSet.union(this.fLeftChild.lastPos());
/*     */     }
/*     */     else {
/* 126 */       throw new RuntimeException("ImplementationMessages.VAL_BST");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.models.XSCMBinOp
 * JD-Core Version:    0.6.2
 */
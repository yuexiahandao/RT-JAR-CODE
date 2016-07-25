/*     */ package com.sun.org.apache.xerces.internal.impl.dtd.models;
/*     */ 
/*     */ public class CMBinOp extends CMNode
/*     */ {
/*     */   private CMNode fLeftChild;
/*     */   private CMNode fRightChild;
/*     */ 
/*     */   public CMBinOp(int type, CMNode leftNode, CMNode rightNode)
/*     */   {
/*  79 */     super(type);
/*     */ 
/*  82 */     if ((type() != 4) && (type() != 5))
/*     */     {
/*  85 */       throw new RuntimeException("ImplementationMessages.VAL_BST");
/*     */     }
/*     */ 
/*  89 */     this.fLeftChild = leftNode;
/*  90 */     this.fRightChild = rightNode;
/*     */   }
/*     */ 
/*     */   final CMNode getLeft()
/*     */   {
/*  99 */     return this.fLeftChild;
/*     */   }
/*     */ 
/*     */   final CMNode getRight()
/*     */   {
/* 104 */     return this.fRightChild;
/*     */   }
/*     */ 
/*     */   public boolean isNullable()
/*     */   {
/* 118 */     if (type() == 4)
/* 119 */       return (this.fLeftChild.isNullable()) || (this.fRightChild.isNullable());
/* 120 */     if (type() == 5) {
/* 121 */       return (this.fLeftChild.isNullable()) && (this.fRightChild.isNullable());
/*     */     }
/* 123 */     throw new RuntimeException("ImplementationMessages.VAL_BST");
/*     */   }
/*     */ 
/*     */   protected void calcFirstPos(CMStateSet toSet)
/*     */   {
/* 132 */     if (type() == 4)
/*     */     {
/* 135 */       toSet.setTo(this.fLeftChild.firstPos());
/* 136 */       toSet.union(this.fRightChild.firstPos());
/*     */     }
/* 138 */     else if (type() == 5)
/*     */     {
/* 145 */       toSet.setTo(this.fLeftChild.firstPos());
/* 146 */       if (this.fLeftChild.isNullable())
/* 147 */         toSet.union(this.fRightChild.firstPos());
/*     */     }
/*     */     else
/*     */     {
/* 151 */       throw new RuntimeException("ImplementationMessages.VAL_BST");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void calcLastPos(CMStateSet toSet)
/*     */   {
/* 157 */     if (type() == 4)
/*     */     {
/* 160 */       toSet.setTo(this.fLeftChild.lastPos());
/* 161 */       toSet.union(this.fRightChild.lastPos());
/*     */     }
/* 163 */     else if (type() == 5)
/*     */     {
/* 170 */       toSet.setTo(this.fRightChild.lastPos());
/* 171 */       if (this.fRightChild.isNullable())
/* 172 */         toSet.union(this.fLeftChild.lastPos());
/*     */     }
/*     */     else
/*     */     {
/* 176 */       throw new RuntimeException("ImplementationMessages.VAL_BST");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.models.CMBinOp
 * JD-Core Version:    0.6.2
 */
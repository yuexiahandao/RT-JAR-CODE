/*     */ package com.sun.org.apache.xerces.internal.impl.xs.models;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
/*     */ import com.sun.org.apache.xerces.internal.impl.dtd.models.CMStateSet;
/*     */ 
/*     */ public class XSCMLeaf extends CMNode
/*     */ {
/*  41 */   private Object fLeaf = null;
/*     */ 
/*  46 */   private int fParticleId = -1;
/*     */ 
/*  53 */   private int fPosition = -1;
/*     */ 
/*     */   public XSCMLeaf(int type, Object leaf, int id, int position)
/*     */   {
/*  61 */     super(type);
/*     */ 
/*  64 */     this.fLeaf = leaf;
/*  65 */     this.fParticleId = id;
/*  66 */     this.fPosition = position;
/*     */   }
/*     */ 
/*     */   final Object getLeaf()
/*     */   {
/*  74 */     return this.fLeaf;
/*     */   }
/*     */ 
/*     */   final int getParticleId() {
/*  78 */     return this.fParticleId;
/*     */   }
/*     */ 
/*     */   final int getPosition() {
/*  82 */     return this.fPosition;
/*     */   }
/*     */ 
/*     */   final void setPosition(int newPosition) {
/*  86 */     this.fPosition = newPosition;
/*     */   }
/*     */ 
/*     */   public boolean isNullable()
/*     */   {
/*  97 */     return this.fPosition == -1;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 101 */     StringBuffer strRet = new StringBuffer(this.fLeaf.toString());
/* 102 */     if (this.fPosition >= 0) {
/* 103 */       strRet.append(" (Pos:" + Integer.toString(this.fPosition) + ")");
/*     */     }
/*     */ 
/* 110 */     return strRet.toString();
/*     */   }
/*     */ 
/*     */   protected void calcFirstPos(CMStateSet toSet)
/*     */   {
/* 117 */     if (this.fPosition == -1) {
/* 118 */       toSet.zeroBits();
/*     */     }
/*     */     else
/*     */     {
/* 122 */       toSet.setBit(this.fPosition);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void calcLastPos(CMStateSet toSet) {
/* 127 */     if (this.fPosition == -1) {
/* 128 */       toSet.zeroBits();
/*     */     }
/*     */     else
/*     */     {
/* 132 */       toSet.setBit(this.fPosition);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.models.XSCMLeaf
 * JD-Core Version:    0.6.2
 */
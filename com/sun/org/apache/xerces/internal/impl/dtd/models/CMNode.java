/*     */ package com.sun.org.apache.xerces.internal.impl.dtd.models;
/*     */ 
/*     */ public abstract class CMNode
/*     */ {
/*     */   private int fType;
/* 185 */   private CMStateSet fFirstPos = null;
/* 186 */   private CMStateSet fFollowPos = null;
/* 187 */   private CMStateSet fLastPos = null;
/* 188 */   private int fMaxStates = -1;
/* 189 */   private Object fUserData = null;
/*     */ 
/*     */   public CMNode(int type)
/*     */   {
/*  77 */     this.fType = type;
/*     */   }
/*     */ 
/*     */   public abstract boolean isNullable();
/*     */ 
/*     */   public final int type()
/*     */   {
/*  93 */     return this.fType;
/*     */   }
/*     */ 
/*     */   public final CMStateSet firstPos()
/*     */   {
/*  99 */     if (this.fFirstPos == null)
/*     */     {
/* 101 */       this.fFirstPos = new CMStateSet(this.fMaxStates);
/* 102 */       calcFirstPos(this.fFirstPos);
/*     */     }
/* 104 */     return this.fFirstPos;
/*     */   }
/*     */ 
/*     */   public final CMStateSet lastPos()
/*     */   {
/* 110 */     if (this.fLastPos == null)
/*     */     {
/* 112 */       this.fLastPos = new CMStateSet(this.fMaxStates);
/* 113 */       calcLastPos(this.fLastPos);
/*     */     }
/* 115 */     return this.fLastPos;
/*     */   }
/*     */ 
/*     */   final void setFollowPos(CMStateSet setToAdopt)
/*     */   {
/* 120 */     this.fFollowPos = setToAdopt;
/*     */   }
/*     */ 
/*     */   public final void setMaxStates(int maxStates)
/*     */   {
/* 125 */     this.fMaxStates = maxStates;
/*     */   }
/*     */ 
/*     */   public void setUserData(Object userData)
/*     */   {
/* 134 */     this.fUserData = userData;
/*     */   }
/*     */ 
/*     */   public Object getUserData()
/*     */   {
/* 143 */     return this.fUserData;
/*     */   }
/*     */ 
/*     */   protected abstract void calcFirstPos(CMStateSet paramCMStateSet);
/*     */ 
/*     */   protected abstract void calcLastPos(CMStateSet paramCMStateSet);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode
 * JD-Core Version:    0.6.2
 */
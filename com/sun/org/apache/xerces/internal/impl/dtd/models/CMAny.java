/*     */ package com.sun.org.apache.xerces.internal.impl.dtd.models;
/*     */ 
/*     */ public class CMAny extends CMNode
/*     */ {
/*     */   private int fType;
/*     */   private String fURI;
/*  99 */   private int fPosition = -1;
/*     */ 
/*     */   public CMAny(int type, String uri, int position)
/*     */   {
/* 107 */     super(type);
/*     */ 
/* 110 */     this.fType = type;
/* 111 */     this.fURI = uri;
/* 112 */     this.fPosition = position;
/*     */   }
/*     */ 
/*     */   final int getType()
/*     */   {
/* 120 */     return this.fType;
/*     */   }
/*     */ 
/*     */   final String getURI() {
/* 124 */     return this.fURI;
/*     */   }
/*     */ 
/*     */   final int getPosition()
/*     */   {
/* 129 */     return this.fPosition;
/*     */   }
/*     */ 
/*     */   final void setPosition(int newPosition)
/*     */   {
/* 134 */     this.fPosition = newPosition;
/*     */   }
/*     */ 
/*     */   public boolean isNullable()
/*     */   {
/* 146 */     return this.fPosition == -1;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 151 */     StringBuffer strRet = new StringBuffer();
/* 152 */     strRet.append("(");
/* 153 */     strRet.append("##any:uri=");
/* 154 */     strRet.append(this.fURI);
/* 155 */     strRet.append(')');
/* 156 */     if (this.fPosition >= 0)
/*     */     {
/* 158 */       strRet.append(" (Pos:" + new Integer(this.fPosition).toString() + ")");
/*     */     }
/*     */ 
/* 165 */     return strRet.toString();
/*     */   }
/*     */ 
/*     */   protected void calcFirstPos(CMStateSet toSet)
/*     */   {
/* 173 */     if (this.fPosition == -1) {
/* 174 */       toSet.zeroBits();
/*     */     }
/*     */     else
/*     */     {
/* 178 */       toSet.setBit(this.fPosition);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void calcLastPos(CMStateSet toSet)
/*     */   {
/* 184 */     if (this.fPosition == -1) {
/* 185 */       toSet.zeroBits();
/*     */     }
/*     */     else
/*     */     {
/* 189 */       toSet.setBit(this.fPosition);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.models.CMAny
 * JD-Core Version:    0.6.2
 */
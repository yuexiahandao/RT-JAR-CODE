/*     */ package com.sun.org.apache.xerces.internal.impl.dtd.models;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ 
/*     */ public class CMLeaf extends CMNode
/*     */ {
/*  81 */   private QName fElement = new QName();
/*     */ 
/*  88 */   private int fPosition = -1;
/*     */ 
/*     */   public CMLeaf(QName element, int position)
/*     */   {
/*  96 */     super(0);
/*     */ 
/*  99 */     this.fElement.setValues(element);
/* 100 */     this.fPosition = position;
/*     */   }
/*     */ 
/*     */   public CMLeaf(QName element)
/*     */   {
/* 105 */     super(0);
/*     */ 
/* 108 */     this.fElement.setValues(element);
/*     */   }
/*     */ 
/*     */   final QName getElement()
/*     */   {
/* 117 */     return this.fElement;
/*     */   }
/*     */ 
/*     */   final int getPosition()
/*     */   {
/* 122 */     return this.fPosition;
/*     */   }
/*     */ 
/*     */   final void setPosition(int newPosition)
/*     */   {
/* 127 */     this.fPosition = newPosition;
/*     */   }
/*     */ 
/*     */   public boolean isNullable()
/*     */   {
/* 139 */     return this.fPosition == -1;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 144 */     StringBuffer strRet = new StringBuffer(this.fElement.toString());
/* 145 */     strRet.append(" (");
/* 146 */     strRet.append(this.fElement.uri);
/* 147 */     strRet.append(',');
/* 148 */     strRet.append(this.fElement.localpart);
/* 149 */     strRet.append(')');
/* 150 */     if (this.fPosition >= 0)
/*     */     {
/* 152 */       strRet.append(" (Pos:" + new Integer(this.fPosition).toString() + ")");
/*     */     }
/*     */ 
/* 159 */     return strRet.toString();
/*     */   }
/*     */ 
/*     */   protected void calcFirstPos(CMStateSet toSet)
/*     */   {
/* 167 */     if (this.fPosition == -1) {
/* 168 */       toSet.zeroBits();
/*     */     }
/*     */     else
/*     */     {
/* 172 */       toSet.setBit(this.fPosition);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void calcLastPos(CMStateSet toSet)
/*     */   {
/* 178 */     if (this.fPosition == -1) {
/* 179 */       toSet.zeroBits();
/*     */     }
/*     */     else
/*     */     {
/* 183 */       toSet.setBit(this.fPosition);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.models.CMLeaf
 * JD-Core Version:    0.6.2
 */
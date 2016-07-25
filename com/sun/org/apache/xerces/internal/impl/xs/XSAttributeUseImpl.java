/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ 
/*     */ public class XSAttributeUseImpl
/*     */   implements XSAttributeUse
/*     */ {
/*  44 */   public XSAttributeDecl fAttrDecl = null;
/*     */ 
/*  46 */   public short fUse = 0;
/*     */ 
/*  48 */   public short fConstraintType = 0;
/*     */ 
/*  50 */   public ValidatedInfo fDefault = null;
/*     */ 
/*  52 */   public XSObjectList fAnnotations = null;
/*     */ 
/*     */   public void reset() {
/*  55 */     this.fDefault = null;
/*  56 */     this.fAttrDecl = null;
/*  57 */     this.fUse = 0;
/*  58 */     this.fConstraintType = 0;
/*  59 */     this.fAnnotations = null;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/*  66 */     return 4;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean getRequired()
/*     */   {
/*  92 */     return this.fUse == 1;
/*     */   }
/*     */ 
/*     */   public XSAttributeDeclaration getAttrDeclaration()
/*     */   {
/* 100 */     return this.fAttrDecl;
/*     */   }
/*     */ 
/*     */   public short getConstraintType()
/*     */   {
/* 107 */     return this.fConstraintType;
/*     */   }
/*     */ 
/*     */   public String getConstraintValue()
/*     */   {
/* 116 */     return getConstraintType() == 0 ? null : this.fDefault.stringValue();
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getActualVC() {
/* 129 */     return getConstraintType() == 0 ? null : this.fDefault.actualValue;
/*     */   }
/*     */ 
/*     */   public short getActualVCType()
/*     */   {
/* 135 */     return getConstraintType() == 0 ? 45 : this.fDefault.actualValueType;
/*     */   }
/*     */ 
/*     */   public ShortList getItemValueTypes()
/*     */   {
/* 141 */     return getConstraintType() == 0 ? null : this.fDefault.itemValueTypes;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 150 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSAttributeUseImpl
 * JD-Core Version:    0.6.2
 */
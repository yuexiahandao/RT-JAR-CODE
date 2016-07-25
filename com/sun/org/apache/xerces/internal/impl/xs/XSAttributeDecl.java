/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
/*     */ 
/*     */ public class XSAttributeDecl
/*     */   implements XSAttributeDeclaration
/*     */ {
/*     */   public static final short SCOPE_ABSENT = 0;
/*     */   public static final short SCOPE_GLOBAL = 1;
/*     */   public static final short SCOPE_LOCAL = 2;
/*  54 */   String fName = null;
/*     */ 
/*  56 */   String fTargetNamespace = null;
/*     */ 
/*  58 */   XSSimpleType fType = null;
/*  59 */   public QName fUnresolvedTypeName = null;
/*     */ 
/*  61 */   short fConstraintType = 0;
/*     */ 
/*  63 */   short fScope = 0;
/*     */ 
/*  65 */   XSComplexTypeDecl fEnclosingCT = null;
/*     */ 
/*  67 */   XSObjectList fAnnotations = null;
/*     */ 
/*  69 */   ValidatedInfo fDefault = null;
/*     */ 
/*  72 */   private XSNamespaceItem fNamespaceItem = null;
/*     */ 
/*     */   public void setValues(String name, String targetNamespace, XSSimpleType simpleType, short constraintType, short scope, ValidatedInfo valInfo, XSComplexTypeDecl enclosingCT, XSObjectList annotations)
/*     */   {
/*  78 */     this.fName = name;
/*  79 */     this.fTargetNamespace = targetNamespace;
/*  80 */     this.fType = simpleType;
/*  81 */     this.fConstraintType = constraintType;
/*  82 */     this.fScope = scope;
/*  83 */     this.fDefault = valInfo;
/*  84 */     this.fEnclosingCT = enclosingCT;
/*  85 */     this.fAnnotations = annotations;
/*     */   }
/*     */ 
/*     */   public void reset() {
/*  89 */     this.fName = null;
/*  90 */     this.fTargetNamespace = null;
/*  91 */     this.fType = null;
/*  92 */     this.fUnresolvedTypeName = null;
/*  93 */     this.fConstraintType = 0;
/*  94 */     this.fScope = 0;
/*  95 */     this.fDefault = null;
/*  96 */     this.fAnnotations = null;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 103 */     return 1;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 111 */     return this.fName;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 120 */     return this.fTargetNamespace;
/*     */   }
/*     */ 
/*     */   public XSSimpleTypeDefinition getTypeDefinition()
/*     */   {
/* 127 */     return this.fType;
/*     */   }
/*     */ 
/*     */   public short getScope()
/*     */   {
/* 138 */     return this.fScope;
/*     */   }
/*     */ 
/*     */   public XSComplexTypeDefinition getEnclosingCTDefinition()
/*     */   {
/* 147 */     return this.fEnclosingCT;
/*     */   }
/*     */ 
/*     */   public short getConstraintType()
/*     */   {
/* 154 */     return this.fConstraintType;
/*     */   }
/*     */ 
/*     */   public String getConstraintValue()
/*     */   {
/* 163 */     return getConstraintType() == 0 ? null : this.fDefault.stringValue();
/*     */   }
/*     */ 
/*     */   public XSAnnotation getAnnotation()
/*     */   {
/* 172 */     return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 179 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public ValidatedInfo getValInfo() {
/* 183 */     return this.fDefault;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 190 */     return this.fNamespaceItem;
/*     */   }
/*     */ 
/*     */   void setNamespaceItem(XSNamespaceItem namespaceItem) {
/* 194 */     this.fNamespaceItem = namespaceItem;
/*     */   }
/*     */ 
/*     */   public Object getActualVC() {
/* 198 */     return getConstraintType() == 0 ? null : this.fDefault.actualValue;
/*     */   }
/*     */ 
/*     */   public short getActualVCType()
/*     */   {
/* 204 */     return getConstraintType() == 0 ? 45 : this.fDefault.actualValueType;
/*     */   }
/*     */ 
/*     */   public ShortList getItemValueTypes()
/*     */   {
/* 210 */     return getConstraintType() == 0 ? null : this.fDefault.itemValueTypes;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl
 * JD-Core Version:    0.6.2
 */
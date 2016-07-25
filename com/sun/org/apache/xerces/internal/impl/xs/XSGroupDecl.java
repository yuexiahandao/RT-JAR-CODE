/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSModelGroup;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ 
/*     */ public class XSGroupDecl
/*     */   implements XSModelGroupDefinition
/*     */ {
/*  44 */   public String fName = null;
/*     */ 
/*  46 */   public String fTargetNamespace = null;
/*     */ 
/*  48 */   public XSModelGroupImpl fModelGroup = null;
/*     */ 
/*  50 */   public XSObjectList fAnnotations = null;
/*     */ 
/*  53 */   private XSNamespaceItem fNamespaceItem = null;
/*     */ 
/*     */   public short getType()
/*     */   {
/*  59 */     return 6;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  67 */     return this.fName;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/*  76 */     return this.fTargetNamespace;
/*     */   }
/*     */ 
/*     */   public XSModelGroup getModelGroup()
/*     */   {
/*  83 */     return this.fModelGroup;
/*     */   }
/*     */ 
/*     */   public XSAnnotation getAnnotation()
/*     */   {
/*  90 */     return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/*  97 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 104 */     return this.fNamespaceItem;
/*     */   }
/*     */ 
/*     */   void setNamespaceItem(XSNamespaceItem namespaceItem) {
/* 108 */     this.fNamespaceItem = namespaceItem;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSGroupDecl
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ 
/*     */ public class XSNotationDecl
/*     */   implements XSNotationDeclaration
/*     */ {
/*  42 */   public String fName = null;
/*     */ 
/*  44 */   public String fTargetNamespace = null;
/*     */ 
/*  46 */   public String fPublicId = null;
/*     */ 
/*  48 */   public String fSystemId = null;
/*     */ 
/*  51 */   public XSObjectList fAnnotations = null;
/*     */ 
/*  55 */   private XSNamespaceItem fNamespaceItem = null;
/*     */ 
/*     */   public short getType()
/*     */   {
/*  61 */     return 11;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  69 */     return this.fName;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/*  78 */     return this.fTargetNamespace;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/*  85 */     return this.fSystemId;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/*  93 */     return this.fPublicId;
/*     */   }
/*     */ 
/*     */   public XSAnnotation getAnnotation()
/*     */   {
/* 100 */     return this.fAnnotations != null ? (XSAnnotation)this.fAnnotations.item(0) : null;
/*     */   }
/*     */ 
/*     */   public XSObjectList getAnnotations()
/*     */   {
/* 107 */     return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 114 */     return this.fNamespaceItem;
/*     */   }
/*     */ 
/*     */   void setNamespaceItem(XSNamespaceItem namespaceItem) {
/* 118 */     this.fNamespaceItem = namespaceItem;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSNotationDecl
 * JD-Core Version:    0.6.2
 */
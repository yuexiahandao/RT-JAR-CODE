/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ public class DeferredEntityReferenceImpl extends EntityReferenceImpl
/*     */   implements DeferredNode
/*     */ {
/*     */   static final long serialVersionUID = 390319091370032223L;
/*     */   protected transient int fNodeIndex;
/*     */ 
/*     */   DeferredEntityReferenceImpl(DeferredDocumentImpl ownerDocument, int nodeIndex)
/*     */   {
/* 103 */     super(ownerDocument, null);
/*     */ 
/* 105 */     this.fNodeIndex = nodeIndex;
/* 106 */     needsSyncData(true);
/*     */   }
/*     */ 
/*     */   public int getNodeIndex()
/*     */   {
/* 116 */     return this.fNodeIndex;
/*     */   }
/*     */ 
/*     */   protected void synchronizeData()
/*     */   {
/* 130 */     needsSyncData(false);
/*     */ 
/* 133 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)this.ownerDocument;
/*     */ 
/* 135 */     this.name = ownerDocument.getNodeName(this.fNodeIndex);
/* 136 */     this.baseURI = ownerDocument.getNodeValue(this.fNodeIndex);
/*     */   }
/*     */ 
/*     */   protected void synchronizeChildren()
/*     */   {
/* 144 */     needsSyncChildren(false);
/*     */ 
/* 147 */     isReadOnly(false);
/* 148 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)ownerDocument();
/*     */ 
/* 150 */     ownerDocument.synchronizeChildren(this, this.fNodeIndex);
/* 151 */     setReadOnly(true, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DeferredEntityReferenceImpl
 * JD-Core Version:    0.6.2
 */
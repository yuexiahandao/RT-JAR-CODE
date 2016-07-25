/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ public class DeferredEntityImpl extends EntityImpl
/*     */   implements DeferredNode
/*     */ {
/*     */   static final long serialVersionUID = 4760180431078941638L;
/*     */   protected transient int fNodeIndex;
/*     */ 
/*     */   DeferredEntityImpl(DeferredDocumentImpl ownerDocument, int nodeIndex)
/*     */   {
/*  85 */     super(ownerDocument, null);
/*     */ 
/*  87 */     this.fNodeIndex = nodeIndex;
/*  88 */     needsSyncData(true);
/*  89 */     needsSyncChildren(true);
/*     */   }
/*     */ 
/*     */   public int getNodeIndex()
/*     */   {
/*  99 */     return this.fNodeIndex;
/*     */   }
/*     */ 
/*     */   protected void synchronizeData()
/*     */   {
/* 113 */     needsSyncData(false);
/*     */ 
/* 116 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)this.ownerDocument;
/*     */ 
/* 118 */     this.name = ownerDocument.getNodeName(this.fNodeIndex);
/*     */ 
/* 121 */     this.publicId = ownerDocument.getNodeValue(this.fNodeIndex);
/* 122 */     this.systemId = ownerDocument.getNodeURI(this.fNodeIndex);
/* 123 */     int extraDataIndex = ownerDocument.getNodeExtra(this.fNodeIndex);
/* 124 */     ownerDocument.getNodeType(extraDataIndex);
/*     */ 
/* 126 */     this.notationName = ownerDocument.getNodeName(extraDataIndex);
/*     */ 
/* 129 */     this.version = ownerDocument.getNodeValue(extraDataIndex);
/* 130 */     this.encoding = ownerDocument.getNodeURI(extraDataIndex);
/*     */ 
/* 133 */     int extraIndex2 = ownerDocument.getNodeExtra(extraDataIndex);
/* 134 */     this.baseURI = ownerDocument.getNodeName(extraIndex2);
/* 135 */     this.inputEncoding = ownerDocument.getNodeValue(extraIndex2);
/*     */   }
/*     */ 
/*     */   protected void synchronizeChildren()
/*     */   {
/* 143 */     needsSyncChildren(false);
/*     */ 
/* 145 */     isReadOnly(false);
/* 146 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)ownerDocument();
/*     */ 
/* 148 */     ownerDocument.synchronizeChildren(this, this.fNodeIndex);
/* 149 */     setReadOnly(true, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DeferredEntityImpl
 * JD-Core Version:    0.6.2
 */
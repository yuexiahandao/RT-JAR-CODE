/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class DeferredElementDefinitionImpl extends ElementDefinitionImpl
/*     */   implements DeferredNode
/*     */ {
/*     */   static final long serialVersionUID = 6703238199538041591L;
/*     */   protected transient int fNodeIndex;
/*     */ 
/*     */   DeferredElementDefinitionImpl(DeferredDocumentImpl ownerDocument, int nodeIndex)
/*     */   {
/*  63 */     super(ownerDocument, null);
/*     */ 
/*  65 */     this.fNodeIndex = nodeIndex;
/*  66 */     needsSyncData(true);
/*  67 */     needsSyncChildren(true);
/*     */   }
/*     */ 
/*     */   public int getNodeIndex()
/*     */   {
/*  77 */     return this.fNodeIndex;
/*     */   }
/*     */ 
/*     */   protected void synchronizeData()
/*     */   {
/*  88 */     needsSyncData(false);
/*     */ 
/*  91 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)this.ownerDocument;
/*     */ 
/*  93 */     this.name = ownerDocument.getNodeName(this.fNodeIndex);
/*     */   }
/*     */ 
/*     */   protected void synchronizeChildren()
/*     */   {
/* 101 */     boolean orig = this.ownerDocument.getMutationEvents();
/* 102 */     this.ownerDocument.setMutationEvents(false);
/*     */ 
/* 105 */     needsSyncChildren(false);
/*     */ 
/* 108 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)this.ownerDocument;
/*     */ 
/* 110 */     this.attributes = new NamedNodeMapImpl(ownerDocument);
/*     */ 
/* 114 */     for (int nodeIndex = ownerDocument.getLastChild(this.fNodeIndex); 
/* 115 */       nodeIndex != -1; 
/* 116 */       nodeIndex = ownerDocument.getPrevSibling(nodeIndex)) {
/* 117 */       Node attr = ownerDocument.getNodeObject(nodeIndex);
/* 118 */       this.attributes.setNamedItem(attr);
/*     */     }
/*     */ 
/* 122 */     ownerDocument.setMutationEvents(orig);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DeferredElementDefinitionImpl
 * JD-Core Version:    0.6.2
 */
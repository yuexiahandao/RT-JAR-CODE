/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ public class DeferredTextImpl extends TextImpl
/*     */   implements DeferredNode
/*     */ {
/*     */   static final long serialVersionUID = 2310613872100393425L;
/*     */   protected transient int fNodeIndex;
/*     */ 
/*     */   DeferredTextImpl(DeferredDocumentImpl ownerDocument, int nodeIndex)
/*     */   {
/*  67 */     super(ownerDocument, null);
/*     */ 
/*  69 */     this.fNodeIndex = nodeIndex;
/*  70 */     needsSyncData(true);
/*     */   }
/*     */ 
/*     */   public int getNodeIndex()
/*     */   {
/*  80 */     return this.fNodeIndex;
/*     */   }
/*     */ 
/*     */   protected void synchronizeData()
/*     */   {
/*  91 */     needsSyncData(false);
/*     */ 
/*  94 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)ownerDocument();
/*     */ 
/*  96 */     this.data = ownerDocument.getNodeValueString(this.fNodeIndex);
/*     */ 
/* 103 */     isIgnorableWhitespace(ownerDocument.getNodeExtra(this.fNodeIndex) == 1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DeferredTextImpl
 * JD-Core Version:    0.6.2
 */
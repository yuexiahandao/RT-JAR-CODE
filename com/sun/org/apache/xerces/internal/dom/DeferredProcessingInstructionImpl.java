/*    */ package com.sun.org.apache.xerces.internal.dom;
/*    */ 
/*    */ public class DeferredProcessingInstructionImpl extends ProcessingInstructionImpl
/*    */   implements DeferredNode
/*    */ {
/*    */   static final long serialVersionUID = -4643577954293565388L;
/*    */   protected transient int fNodeIndex;
/*    */ 
/*    */   DeferredProcessingInstructionImpl(DeferredDocumentImpl ownerDocument, int nodeIndex)
/*    */   {
/* 60 */     super(ownerDocument, null, null);
/*    */ 
/* 62 */     this.fNodeIndex = nodeIndex;
/* 63 */     needsSyncData(true);
/*    */   }
/*    */ 
/*    */   public int getNodeIndex()
/*    */   {
/* 73 */     return this.fNodeIndex;
/*    */   }
/*    */ 
/*    */   protected void synchronizeData()
/*    */   {
/* 84 */     needsSyncData(false);
/*    */ 
/* 87 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)ownerDocument();
/*    */ 
/* 89 */     this.target = ownerDocument.getNodeName(this.fNodeIndex);
/* 90 */     this.data = ownerDocument.getNodeValueString(this.fNodeIndex);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DeferredProcessingInstructionImpl
 * JD-Core Version:    0.6.2
 */
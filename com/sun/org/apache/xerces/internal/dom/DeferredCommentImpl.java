/*    */ package com.sun.org.apache.xerces.internal.dom;
/*    */ 
/*    */ public class DeferredCommentImpl extends CommentImpl
/*    */   implements DeferredNode
/*    */ {
/*    */   static final long serialVersionUID = 6498796371083589338L;
/*    */   protected transient int fNodeIndex;
/*    */ 
/*    */   DeferredCommentImpl(DeferredDocumentImpl ownerDocument, int nodeIndex)
/*    */   {
/* 57 */     super(ownerDocument, null);
/*    */ 
/* 59 */     this.fNodeIndex = nodeIndex;
/* 60 */     needsSyncData(true);
/*    */   }
/*    */ 
/*    */   public int getNodeIndex()
/*    */   {
/* 70 */     return this.fNodeIndex;
/*    */   }
/*    */ 
/*    */   protected void synchronizeData()
/*    */   {
/* 81 */     needsSyncData(false);
/*    */ 
/* 84 */     DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl)ownerDocument();
/*    */ 
/* 86 */     this.data = ownerDocument.getNodeValueString(this.fNodeIndex);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DeferredCommentImpl
 * JD-Core Version:    0.6.2
 */
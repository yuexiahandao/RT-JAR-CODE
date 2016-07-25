/*    */ package com.sun.xml.internal.txw2;
/*    */ 
/*    */ abstract class Content
/*    */ {
/*    */   private Content next;
/*    */ 
/*    */   final Content getNext()
/*    */   {
/* 38 */     return this.next;
/*    */   }
/*    */ 
/*    */   final void setNext(Document doc, Content next)
/*    */   {
/* 50 */     assert (next != null);
/* 51 */     assert (this.next == null) : ("next of " + this + " is already set to " + this.next);
/* 52 */     this.next = next;
/* 53 */     doc.run();
/*    */   }
/*    */ 
/*    */   boolean isReadyToCommit()
/*    */   {
/* 60 */     return true;
/*    */   }
/*    */ 
/*    */   abstract boolean concludesPendingStartTag();
/*    */ 
/*    */   abstract void accept(ContentVisitor paramContentVisitor);
/*    */ 
/*    */   public void written()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.Content
 * JD-Core Version:    0.6.2
 */
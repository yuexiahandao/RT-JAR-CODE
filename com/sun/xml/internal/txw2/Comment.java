/*    */ package com.sun.xml.internal.txw2;
/*    */ 
/*    */ final class Comment extends Content
/*    */ {
/* 37 */   private final StringBuilder buffer = new StringBuilder();
/*    */ 
/*    */   public Comment(Document document, NamespaceResolver nsResolver, Object obj) {
/* 40 */     document.writeValue(obj, nsResolver, this.buffer);
/*    */   }
/*    */ 
/*    */   boolean concludesPendingStartTag() {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   void accept(ContentVisitor visitor) {
/* 48 */     visitor.onComment(this.buffer);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.Comment
 * JD-Core Version:    0.6.2
 */
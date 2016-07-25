/*    */ package com.sun.xml.internal.txw2;
/*    */ 
/*    */ final class EndDocument extends Content
/*    */ {
/*    */   boolean concludesPendingStartTag()
/*    */   {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   void accept(ContentVisitor visitor) {
/* 37 */     visitor.onEndDocument();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.EndDocument
 * JD-Core Version:    0.6.2
 */
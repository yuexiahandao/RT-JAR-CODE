/*    */ package com.sun.xml.internal.txw2;
/*    */ 
/*    */ final class StartDocument extends Content
/*    */ {
/*    */   boolean concludesPendingStartTag()
/*    */   {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   void accept(ContentVisitor visitor) {
/* 37 */     visitor.onStartDocument();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.StartDocument
 * JD-Core Version:    0.6.2
 */
/*    */ package com.sun.xml.internal.txw2;
/*    */ 
/*    */ final class Pcdata extends Text
/*    */ {
/*    */   Pcdata(Document document, NamespaceResolver nsResolver, Object obj)
/*    */   {
/* 35 */     super(document, nsResolver, obj);
/*    */   }
/*    */ 
/*    */   void accept(ContentVisitor visitor) {
/* 39 */     visitor.onPcdata(this.buffer);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.Pcdata
 * JD-Core Version:    0.6.2
 */
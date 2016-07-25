/*    */ package com.sun.beans.decoder;
/*    */ 
/*    */ class NullElementHandler extends ElementHandler
/*    */   implements ValueObject
/*    */ {
/*    */   protected final ValueObject getValueObject()
/*    */   {
/* 53 */     return this;
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   public final boolean isVoid()
/*    */   {
/* 74 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.NullElementHandler
 * JD-Core Version:    0.6.2
 */
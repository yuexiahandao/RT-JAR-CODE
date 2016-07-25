/*    */ package com.sun.beans.decoder;
/*    */ 
/*    */ final class DoubleElementHandler extends StringElementHandler
/*    */ {
/*    */   public Object getValue(String paramString)
/*    */   {
/* 61 */     return Double.valueOf(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.DoubleElementHandler
 * JD-Core Version:    0.6.2
 */
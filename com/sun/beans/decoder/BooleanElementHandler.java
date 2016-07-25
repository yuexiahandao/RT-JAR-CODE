/*    */ package com.sun.beans.decoder;
/*    */ 
/*    */ final class BooleanElementHandler extends StringElementHandler
/*    */ {
/*    */   public Object getValue(String paramString)
/*    */   {
/* 61 */     if (Boolean.TRUE.toString().equalsIgnoreCase(paramString)) {
/* 62 */       return Boolean.TRUE;
/*    */     }
/* 64 */     if (Boolean.FALSE.toString().equalsIgnoreCase(paramString)) {
/* 65 */       return Boolean.FALSE;
/*    */     }
/* 67 */     throw new IllegalArgumentException("Unsupported boolean argument: " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.BooleanElementHandler
 * JD-Core Version:    0.6.2
 */
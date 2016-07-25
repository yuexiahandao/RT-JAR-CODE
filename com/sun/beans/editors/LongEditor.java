/*    */ package com.sun.beans.editors;
/*    */ 
/*    */ public class LongEditor extends NumberEditor
/*    */ {
/*    */   public String getJavaInitializationString()
/*    */   {
/* 38 */     Object localObject = getValue();
/* 39 */     return localObject != null ? localObject + "L" : "null";
/*    */   }
/*    */ 
/*    */   public void setAsText(String paramString)
/*    */     throws IllegalArgumentException
/*    */   {
/* 45 */     setValue(paramString == null ? null : Long.decode(paramString));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.editors.LongEditor
 * JD-Core Version:    0.6.2
 */
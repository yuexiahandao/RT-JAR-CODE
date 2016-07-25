/*    */ package com.sun.istack.internal.localization;
/*    */ 
/*    */ public abstract interface Localizable
/*    */ {
/* 62 */   public static final String NOT_LOCALIZABLE = new String("");
/*    */ 
/*    */   public abstract String getKey();
/*    */ 
/*    */   public abstract Object[] getArguments();
/*    */ 
/*    */   public abstract String getResourceBundleName();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.istack.internal.localization.Localizable
 * JD-Core Version:    0.6.2
 */
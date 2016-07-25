/*    */ package com.sun.xml.internal.ws.util.localization;
/*    */ 
/*    */ public final class NullLocalizable
/*    */   implements Localizable
/*    */ {
/*    */   private final String msg;
/*    */ 
/*    */   public NullLocalizable(String msg)
/*    */   {
/* 37 */     if (msg == null)
/* 38 */       throw new IllegalArgumentException();
/* 39 */     this.msg = msg;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 43 */     return Localizable.NOT_LOCALIZABLE;
/*    */   }
/*    */   public Object[] getArguments() {
/* 46 */     return new Object[] { this.msg };
/*    */   }
/*    */   public String getResourceBundleName() {
/* 49 */     return "";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.localization.NullLocalizable
 * JD-Core Version:    0.6.2
 */
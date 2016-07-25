/*    */ package com.sun.org.glassfish.gmbal;
/*    */ 
/*    */ public class GmbalException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -7478444176079980162L;
/*    */ 
/*    */   public GmbalException(String msg)
/*    */   {
/* 41 */     super(msg);
/*    */   }
/*    */ 
/*    */   public GmbalException(String msg, Throwable thr) {
/* 45 */     super(msg, thr);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.glassfish.gmbal.GmbalException
 * JD-Core Version:    0.6.2
 */
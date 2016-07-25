/*    */ package com.sun.xml.internal.txw2;
/*    */ 
/*    */ public class TxwException extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */ 
/*    */   public TxwException(String message)
/*    */   {
/* 33 */     super(message);
/*    */   }
/*    */ 
/*    */   public TxwException(Throwable cause) {
/* 37 */     super(cause);
/*    */   }
/*    */ 
/*    */   public TxwException(String message, Throwable cause) {
/* 41 */     super(message, cause);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.TxwException
 * JD-Core Version:    0.6.2
 */
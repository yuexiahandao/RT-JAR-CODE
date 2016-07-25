/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class TRANSACTION_UNAVAILABLE extends SystemException
/*    */ {
/*    */   public TRANSACTION_UNAVAILABLE()
/*    */   {
/* 49 */     this("");
/*    */   }
/*    */ 
/*    */   public TRANSACTION_UNAVAILABLE(String paramString)
/*    */   {
/* 59 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public TRANSACTION_UNAVAILABLE(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 69 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public TRANSACTION_UNAVAILABLE(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 81 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.TRANSACTION_UNAVAILABLE
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class TRANSACTION_ROLLEDBACK extends SystemException
/*    */ {
/*    */   public TRANSACTION_ROLLEDBACK()
/*    */   {
/* 50 */     this("");
/*    */   }
/*    */ 
/*    */   public TRANSACTION_ROLLEDBACK(String paramString)
/*    */   {
/* 60 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public TRANSACTION_ROLLEDBACK(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 70 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public TRANSACTION_ROLLEDBACK(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 81 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.TRANSACTION_ROLLEDBACK
 * JD-Core Version:    0.6.2
 */
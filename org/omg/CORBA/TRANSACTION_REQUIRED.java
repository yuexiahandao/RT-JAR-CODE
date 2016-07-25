/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class TRANSACTION_REQUIRED extends SystemException
/*    */ {
/*    */   public TRANSACTION_REQUIRED()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public TRANSACTION_REQUIRED(String paramString)
/*    */   {
/* 56 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public TRANSACTION_REQUIRED(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 66 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public TRANSACTION_REQUIRED(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 77 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.TRANSACTION_REQUIRED
 * JD-Core Version:    0.6.2
 */
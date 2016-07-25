/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class COMM_FAILURE extends SystemException
/*    */ {
/*    */   public COMM_FAILURE()
/*    */   {
/* 51 */     this("");
/*    */   }
/*    */ 
/*    */   public COMM_FAILURE(String paramString)
/*    */   {
/* 62 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public COMM_FAILURE(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 74 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public COMM_FAILURE(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 88 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.COMM_FAILURE
 * JD-Core Version:    0.6.2
 */
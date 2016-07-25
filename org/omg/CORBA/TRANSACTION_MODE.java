/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class TRANSACTION_MODE extends SystemException
/*    */ {
/*    */   public TRANSACTION_MODE()
/*    */   {
/* 49 */     this("");
/*    */   }
/*    */ 
/*    */   public TRANSACTION_MODE(String paramString)
/*    */   {
/* 59 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public TRANSACTION_MODE(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 69 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public TRANSACTION_MODE(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 80 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.TRANSACTION_MODE
 * JD-Core Version:    0.6.2
 */
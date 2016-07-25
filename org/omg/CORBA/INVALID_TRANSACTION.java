/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INVALID_TRANSACTION extends SystemException
/*    */ {
/*    */   public INVALID_TRANSACTION()
/*    */   {
/* 49 */     this("");
/*    */   }
/*    */ 
/*    */   public INVALID_TRANSACTION(String paramString)
/*    */   {
/* 58 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INVALID_TRANSACTION(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 68 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INVALID_TRANSACTION(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 80 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INVALID_TRANSACTION
 * JD-Core Version:    0.6.2
 */
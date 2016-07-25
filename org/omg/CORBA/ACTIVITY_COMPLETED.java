/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class ACTIVITY_COMPLETED extends SystemException
/*    */ {
/*    */   public ACTIVITY_COMPLETED()
/*    */   {
/* 48 */     this("");
/*    */   }
/*    */ 
/*    */   public ACTIVITY_COMPLETED(String paramString)
/*    */   {
/* 58 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public ACTIVITY_COMPLETED(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 70 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public ACTIVITY_COMPLETED(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 84 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ACTIVITY_COMPLETED
 * JD-Core Version:    0.6.2
 */
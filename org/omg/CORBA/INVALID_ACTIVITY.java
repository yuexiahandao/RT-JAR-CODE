/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INVALID_ACTIVITY extends SystemException
/*    */ {
/*    */   public INVALID_ACTIVITY()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public INVALID_ACTIVITY(String paramString)
/*    */   {
/* 57 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INVALID_ACTIVITY(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 69 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INVALID_ACTIVITY(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 83 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INVALID_ACTIVITY
 * JD-Core Version:    0.6.2
 */
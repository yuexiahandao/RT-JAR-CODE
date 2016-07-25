/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class ACTIVITY_REQUIRED extends SystemException
/*    */ {
/*    */   public ACTIVITY_REQUIRED()
/*    */   {
/* 46 */     this("");
/*    */   }
/*    */ 
/*    */   public ACTIVITY_REQUIRED(String paramString)
/*    */   {
/* 56 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public ACTIVITY_REQUIRED(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 68 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public ACTIVITY_REQUIRED(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 82 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ACTIVITY_REQUIRED
 * JD-Core Version:    0.6.2
 */
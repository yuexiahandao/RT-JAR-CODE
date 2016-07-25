/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class NO_RESPONSE extends SystemException
/*    */ {
/*    */   public NO_RESPONSE()
/*    */   {
/* 48 */     this("");
/*    */   }
/*    */ 
/*    */   public NO_RESPONSE(String paramString)
/*    */   {
/* 57 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public NO_RESPONSE(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 67 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public NO_RESPONSE(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 78 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.NO_RESPONSE
 * JD-Core Version:    0.6.2
 */
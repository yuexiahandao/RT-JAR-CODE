/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class NO_IMPLEMENT extends SystemException
/*    */ {
/*    */   public NO_IMPLEMENT()
/*    */   {
/* 52 */     this("");
/*    */   }
/*    */ 
/*    */   public NO_IMPLEMENT(String paramString)
/*    */   {
/* 61 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public NO_IMPLEMENT(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 72 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public NO_IMPLEMENT(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 84 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.NO_IMPLEMENT
 * JD-Core Version:    0.6.2
 */
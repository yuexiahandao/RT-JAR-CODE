/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class NO_PERMISSION extends SystemException
/*    */ {
/*    */   public NO_PERMISSION()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public NO_PERMISSION(String paramString)
/*    */   {
/* 56 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public NO_PERMISSION(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 66 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public NO_PERMISSION(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 77 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.NO_PERMISSION
 * JD-Core Version:    0.6.2
 */
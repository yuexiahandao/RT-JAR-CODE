/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class BAD_PARAM extends SystemException
/*    */ {
/*    */   public BAD_PARAM()
/*    */   {
/* 54 */     this("");
/*    */   }
/*    */ 
/*    */   public BAD_PARAM(String paramString)
/*    */   {
/* 65 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public BAD_PARAM(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 75 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public BAD_PARAM(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 89 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.BAD_PARAM
 * JD-Core Version:    0.6.2
 */
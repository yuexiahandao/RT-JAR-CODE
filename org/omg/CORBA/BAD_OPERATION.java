/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class BAD_OPERATION extends SystemException
/*    */ {
/*    */   public BAD_OPERATION()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public BAD_OPERATION(String paramString)
/*    */   {
/* 56 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public BAD_OPERATION(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 67 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public BAD_OPERATION(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 80 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.BAD_OPERATION
 * JD-Core Version:    0.6.2
 */
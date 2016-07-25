/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class FREE_MEM extends SystemException
/*    */ {
/*    */   public FREE_MEM()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public FREE_MEM(String paramString)
/*    */   {
/* 57 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public FREE_MEM(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 67 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public FREE_MEM(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 79 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.FREE_MEM
 * JD-Core Version:    0.6.2
 */
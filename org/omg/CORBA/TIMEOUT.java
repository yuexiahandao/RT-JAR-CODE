/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class TIMEOUT extends SystemException
/*    */ {
/*    */   public TIMEOUT()
/*    */   {
/* 45 */     this("");
/*    */   }
/*    */ 
/*    */   public TIMEOUT(String paramString)
/*    */   {
/* 55 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public TIMEOUT(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 67 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public TIMEOUT(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 81 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.TIMEOUT
 * JD-Core Version:    0.6.2
 */
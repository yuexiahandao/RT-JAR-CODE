/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INV_POLICY extends SystemException
/*    */ {
/*    */   public INV_POLICY()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public INV_POLICY(String paramString)
/*    */   {
/* 57 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INV_POLICY(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 67 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INV_POLICY(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 78 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INV_POLICY
 * JD-Core Version:    0.6.2
 */
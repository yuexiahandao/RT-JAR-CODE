/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INV_FLAG extends SystemException
/*    */ {
/*    */   public INV_FLAG()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public INV_FLAG(String paramString)
/*    */   {
/* 56 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INV_FLAG(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 67 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INV_FLAG(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 80 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INV_FLAG
 * JD-Core Version:    0.6.2
 */
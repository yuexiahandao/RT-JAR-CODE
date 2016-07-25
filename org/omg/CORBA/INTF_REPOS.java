/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INTF_REPOS extends SystemException
/*    */ {
/*    */   public INTF_REPOS()
/*    */   {
/* 48 */     this("");
/*    */   }
/*    */ 
/*    */   public INTF_REPOS(String paramString)
/*    */   {
/* 56 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INTF_REPOS(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 66 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INTF_REPOS(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 78 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INTF_REPOS
 * JD-Core Version:    0.6.2
 */
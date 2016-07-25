/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class BAD_QOS extends SystemException
/*    */ {
/*    */   public BAD_QOS()
/*    */   {
/* 45 */     this("");
/*    */   }
/*    */ 
/*    */   public BAD_QOS(String paramString)
/*    */   {
/* 55 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public BAD_QOS(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 67 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public BAD_QOS(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 81 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.BAD_QOS
 * JD-Core Version:    0.6.2
 */
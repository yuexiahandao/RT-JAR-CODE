/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class IMP_LIMIT extends SystemException
/*    */ {
/*    */   public IMP_LIMIT()
/*    */   {
/* 51 */     this("");
/*    */   }
/*    */ 
/*    */   public IMP_LIMIT(String paramString)
/*    */   {
/* 61 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public IMP_LIMIT(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 71 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public IMP_LIMIT(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 83 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.IMP_LIMIT
 * JD-Core Version:    0.6.2
 */
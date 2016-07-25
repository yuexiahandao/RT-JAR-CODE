/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class NO_MEMORY extends SystemException
/*    */ {
/*    */   public NO_MEMORY()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public NO_MEMORY(String paramString)
/*    */   {
/* 56 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public NO_MEMORY(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 66 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public NO_MEMORY(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 77 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.NO_MEMORY
 * JD-Core Version:    0.6.2
 */
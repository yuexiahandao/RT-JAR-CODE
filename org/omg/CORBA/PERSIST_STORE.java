/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class PERSIST_STORE extends SystemException
/*    */ {
/*    */   public PERSIST_STORE()
/*    */   {
/* 47 */     this("");
/*    */   }
/*    */ 
/*    */   public PERSIST_STORE(String paramString)
/*    */   {
/* 56 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public PERSIST_STORE(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 66 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public PERSIST_STORE(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 77 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PERSIST_STORE
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INITIALIZE extends SystemException
/*    */ {
/*    */   public INITIALIZE()
/*    */   {
/* 49 */     this("");
/*    */   }
/*    */ 
/*    */   public INITIALIZE(String paramString)
/*    */   {
/* 59 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INITIALIZE(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 71 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INITIALIZE(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 85 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INITIALIZE
 * JD-Core Version:    0.6.2
 */
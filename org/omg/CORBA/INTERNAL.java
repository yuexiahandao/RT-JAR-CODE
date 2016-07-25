/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INTERNAL extends SystemException
/*    */ {
/*    */   public INTERNAL()
/*    */   {
/* 50 */     this("");
/*    */   }
/*    */ 
/*    */   public INTERNAL(String paramString)
/*    */   {
/* 59 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INTERNAL(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 71 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INTERNAL(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 85 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INTERNAL
 * JD-Core Version:    0.6.2
 */
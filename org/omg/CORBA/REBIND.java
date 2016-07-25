/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class REBIND extends SystemException
/*    */ {
/*    */   public REBIND()
/*    */   {
/* 48 */     this("");
/*    */   }
/*    */ 
/*    */   public REBIND(String paramString)
/*    */   {
/* 58 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public REBIND(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 70 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public REBIND(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 84 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.REBIND
 * JD-Core Version:    0.6.2
 */
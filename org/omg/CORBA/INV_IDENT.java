/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INV_IDENT extends SystemException
/*    */ {
/*    */   public INV_IDENT()
/*    */   {
/* 48 */     this("");
/*    */   }
/*    */ 
/*    */   public INV_IDENT(String paramString)
/*    */   {
/* 57 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INV_IDENT(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 68 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INV_IDENT(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 81 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INV_IDENT
 * JD-Core Version:    0.6.2
 */
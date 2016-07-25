/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class INV_OBJREF extends SystemException
/*    */ {
/*    */   public INV_OBJREF()
/*    */   {
/* 54 */     this("");
/*    */   }
/*    */ 
/*    */   public INV_OBJREF(String paramString)
/*    */   {
/* 63 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public INV_OBJREF(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 74 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public INV_OBJREF(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 87 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.INV_OBJREF
 * JD-Core Version:    0.6.2
 */
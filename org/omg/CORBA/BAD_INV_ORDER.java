/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class BAD_INV_ORDER extends SystemException
/*    */ {
/*    */   public BAD_INV_ORDER()
/*    */   {
/* 49 */     this("");
/*    */   }
/*    */ 
/*    */   public BAD_INV_ORDER(String paramString)
/*    */   {
/* 59 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public BAD_INV_ORDER(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 70 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public BAD_INV_ORDER(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 82 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.BAD_INV_ORDER
 * JD-Core Version:    0.6.2
 */
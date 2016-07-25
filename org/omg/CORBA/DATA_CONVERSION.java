/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class DATA_CONVERSION extends SystemException
/*    */ {
/*    */   public DATA_CONVERSION()
/*    */   {
/* 53 */     this("");
/*    */   }
/*    */ 
/*    */   public DATA_CONVERSION(String paramString)
/*    */   {
/* 61 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public DATA_CONVERSION(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 71 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public DATA_CONVERSION(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 83 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DATA_CONVERSION
 * JD-Core Version:    0.6.2
 */
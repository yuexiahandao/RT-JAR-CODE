/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class CODESET_INCOMPATIBLE extends SystemException
/*    */ {
/*    */   public CODESET_INCOMPATIBLE()
/*    */   {
/* 44 */     this("");
/*    */   }
/*    */ 
/*    */   public CODESET_INCOMPATIBLE(String paramString)
/*    */   {
/* 54 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public CODESET_INCOMPATIBLE(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 66 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public CODESET_INCOMPATIBLE(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 80 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.CODESET_INCOMPATIBLE
 * JD-Core Version:    0.6.2
 */
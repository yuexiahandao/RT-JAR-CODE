/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class OBJECT_NOT_EXIST extends SystemException
/*    */ {
/*    */   public OBJECT_NOT_EXIST()
/*    */   {
/* 57 */     this("");
/*    */   }
/*    */ 
/*    */   public OBJECT_NOT_EXIST(String paramString)
/*    */   {
/* 66 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public OBJECT_NOT_EXIST(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 76 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public OBJECT_NOT_EXIST(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 87 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.OBJECT_NOT_EXIST
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class OBJ_ADAPTER extends SystemException
/*    */ {
/*    */   public OBJ_ADAPTER()
/*    */   {
/* 52 */     this("");
/*    */   }
/*    */ 
/*    */   public OBJ_ADAPTER(String paramString)
/*    */   {
/* 61 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public OBJ_ADAPTER(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 71 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public OBJ_ADAPTER(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 82 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.OBJ_ADAPTER
 * JD-Core Version:    0.6.2
 */
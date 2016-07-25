/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class MARSHAL extends SystemException
/*    */ {
/*    */   public MARSHAL()
/*    */   {
/* 55 */     this("");
/*    */   }
/*    */ 
/*    */   public MARSHAL(String paramString)
/*    */   {
/* 64 */     this(paramString, 0, CompletionStatus.COMPLETED_NO);
/*    */   }
/*    */ 
/*    */   public MARSHAL(int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 74 */     this("", paramInt, paramCompletionStatus);
/*    */   }
/*    */ 
/*    */   public MARSHAL(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*    */   {
/* 85 */     super(paramString, paramInt, paramCompletionStatus);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.MARSHAL
 * JD-Core Version:    0.6.2
 */
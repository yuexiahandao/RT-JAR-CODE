/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class PolicyError extends UserException
/*    */ {
/*    */   public short reason;
/*    */ 
/*    */   public PolicyError()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PolicyError(short paramShort)
/*    */   {
/* 58 */     this.reason = paramShort;
/*    */   }
/*    */ 
/*    */   public PolicyError(String paramString, short paramShort)
/*    */   {
/* 68 */     super(paramString);
/* 69 */     this.reason = paramShort;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyError
 * JD-Core Version:    0.6.2
 */
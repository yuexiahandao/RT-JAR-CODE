/*    */ package org.omg.CORBA;
/*    */ 
/*    */ public final class UnknownUserException extends UserException
/*    */ {
/*    */   public Any except;
/*    */ 
/*    */   public UnknownUserException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public UnknownUserException(Any paramAny)
/*    */   {
/* 64 */     this.except = paramAny;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.UnknownUserException
 * JD-Core Version:    0.6.2
 */
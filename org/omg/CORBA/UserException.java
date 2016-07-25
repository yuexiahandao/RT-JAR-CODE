/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.IDLEntity;
/*    */ 
/*    */ public abstract class UserException extends Exception
/*    */   implements IDLEntity
/*    */ {
/*    */   protected UserException()
/*    */   {
/*    */   }
/*    */ 
/*    */   protected UserException(String paramString)
/*    */   {
/* 55 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.UserException
 * JD-Core Version:    0.6.2
 */
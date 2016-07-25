/*    */ package org.omg.PortableServer.POAPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class ServantAlreadyActive extends UserException
/*    */ {
/*    */   public ServantAlreadyActive()
/*    */   {
/* 16 */     super(ServantAlreadyActiveHelper.id());
/*    */   }
/*    */ 
/*    */   public ServantAlreadyActive(String paramString)
/*    */   {
/* 22 */     super(ServantAlreadyActiveHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.POAPackage.ServantAlreadyActive
 * JD-Core Version:    0.6.2
 */
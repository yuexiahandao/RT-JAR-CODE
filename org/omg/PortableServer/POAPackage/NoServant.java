/*    */ package org.omg.PortableServer.POAPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class NoServant extends UserException
/*    */ {
/*    */   public NoServant()
/*    */   {
/* 16 */     super(NoServantHelper.id());
/*    */   }
/*    */ 
/*    */   public NoServant(String paramString)
/*    */   {
/* 22 */     super(NoServantHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.POAPackage.NoServant
 * JD-Core Version:    0.6.2
 */
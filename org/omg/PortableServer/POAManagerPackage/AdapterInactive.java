/*    */ package org.omg.PortableServer.POAManagerPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class AdapterInactive extends UserException
/*    */ {
/*    */   public AdapterInactive()
/*    */   {
/* 16 */     super(AdapterInactiveHelper.id());
/*    */   }
/*    */ 
/*    */   public AdapterInactive(String paramString)
/*    */   {
/* 22 */     super(AdapterInactiveHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.POAManagerPackage.AdapterInactive
 * JD-Core Version:    0.6.2
 */
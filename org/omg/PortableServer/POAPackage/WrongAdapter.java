/*    */ package org.omg.PortableServer.POAPackage;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class WrongAdapter extends UserException
/*    */ {
/*    */   public WrongAdapter()
/*    */   {
/* 16 */     super(WrongAdapterHelper.id());
/*    */   }
/*    */ 
/*    */   public WrongAdapter(String paramString)
/*    */   {
/* 22 */     super(WrongAdapterHelper.id() + "  " + paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.POAPackage.WrongAdapter
 * JD-Core Version:    0.6.2
 */
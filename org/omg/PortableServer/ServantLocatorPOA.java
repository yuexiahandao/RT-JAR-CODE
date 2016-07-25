/*    */ package org.omg.PortableServer;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import org.omg.CORBA.BAD_OPERATION;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.InvokeHandler;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.ResponseHandler;
/*    */ 
/*    */ public abstract class ServantLocatorPOA extends Servant
/*    */   implements ServantLocatorOperations, InvokeHandler
/*    */ {
/* 36 */   private static Hashtable _methods = new Hashtable();
/*    */ 
/* 51 */   private static String[] __ids = { "IDL:omg.org/PortableServer/ServantLocator:1.0", "IDL:omg.org/PortableServer/ServantManager:1.0" };
/*    */ 
/*    */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*    */   {
/* 47 */     throw new BAD_OPERATION();
/*    */   }
/*    */ 
/*    */   public String[] _all_interfaces(POA paramPOA, byte[] paramArrayOfByte)
/*    */   {
/* 57 */     return (String[])__ids.clone();
/*    */   }
/*    */ 
/*    */   public ServantLocator _this()
/*    */   {
/* 62 */     return ServantLocatorHelper.narrow(super._this_object());
/*    */   }
/*    */ 
/*    */   public ServantLocator _this(ORB paramORB)
/*    */   {
/* 68 */     return ServantLocatorHelper.narrow(super._this_object(paramORB));
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 39 */     _methods.put("preinvoke", new Integer(0));
/* 40 */     _methods.put("postinvoke", new Integer(1));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.ServantLocatorPOA
 * JD-Core Version:    0.6.2
 */
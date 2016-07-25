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
/*    */ public abstract class ServantActivatorPOA extends Servant
/*    */   implements ServantActivatorOperations, InvokeHandler
/*    */ {
/* 22 */   private static Hashtable _methods = new Hashtable();
/*    */ 
/* 37 */   private static String[] __ids = { "IDL:omg.org/PortableServer/ServantActivator:2.3", "IDL:omg.org/PortableServer/ServantManager:1.0" };
/*    */ 
/*    */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*    */   {
/* 33 */     throw new BAD_OPERATION();
/*    */   }
/*    */ 
/*    */   public String[] _all_interfaces(POA paramPOA, byte[] paramArrayOfByte)
/*    */   {
/* 43 */     return (String[])__ids.clone();
/*    */   }
/*    */ 
/*    */   public ServantActivator _this()
/*    */   {
/* 48 */     return ServantActivatorHelper.narrow(super._this_object());
/*    */   }
/*    */ 
/*    */   public ServantActivator _this(ORB paramORB)
/*    */   {
/* 54 */     return ServantActivatorHelper.narrow(super._this_object(paramORB));
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 25 */     _methods.put("incarnate", new Integer(0));
/* 26 */     _methods.put("etherealize", new Integer(1));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.ServantActivatorPOA
 * JD-Core Version:    0.6.2
 */
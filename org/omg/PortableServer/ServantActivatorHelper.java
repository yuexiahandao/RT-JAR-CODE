/*    */ package org.omg.PortableServer;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.MARSHAL;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.Delegate;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.ObjectImpl;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ServantActivatorHelper
/*    */ {
/* 18 */   private static String _id = "IDL:omg.org/PortableServer/ServantActivator:2.3";
/*    */ 
/* 33 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, ServantActivator paramServantActivator)
/*    */   {
/* 22 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 23 */     paramAny.type(type());
/* 24 */     write(localOutputStream, paramServantActivator);
/* 25 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static ServantActivator extract(Any paramAny)
/*    */   {
/* 30 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 36 */     if (__typeCode == null)
/*    */     {
/* 38 */       __typeCode = ORB.init().create_interface_tc(id(), "ServantActivator");
/*    */     }
/* 40 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 45 */     return _id;
/*    */   }
/*    */ 
/*    */   public static ServantActivator read(InputStream paramInputStream)
/*    */   {
/* 50 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, ServantActivator paramServantActivator)
/*    */   {
/* 55 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static ServantActivator narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 60 */     if (paramObject == null)
/* 61 */       return null;
/* 62 */     if ((paramObject instanceof ServantActivator))
/* 63 */       return (ServantActivator)paramObject;
/* 64 */     if (!paramObject._is_a(id())) {
/* 65 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 68 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 69 */     _ServantActivatorStub local_ServantActivatorStub = new _ServantActivatorStub();
/* 70 */     local_ServantActivatorStub._set_delegate(localDelegate);
/* 71 */     return local_ServantActivatorStub;
/*    */   }
/*    */ 
/*    */   public static ServantActivator unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 77 */     if (paramObject == null)
/* 78 */       return null;
/* 79 */     if ((paramObject instanceof ServantActivator)) {
/* 80 */       return (ServantActivator)paramObject;
/*    */     }
/*    */ 
/* 83 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 84 */     _ServantActivatorStub local_ServantActivatorStub = new _ServantActivatorStub();
/* 85 */     local_ServantActivatorStub._set_delegate(localDelegate);
/* 86 */     return local_ServantActivatorStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer.ServantActivatorHelper
 * JD-Core Version:    0.6.2
 */
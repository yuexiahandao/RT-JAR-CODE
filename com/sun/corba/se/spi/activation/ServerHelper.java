/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.Delegate;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.ObjectImpl;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ServerHelper
/*    */ {
/* 16 */   private static String _id = "IDL:activation/Server:1.0";
/*    */ 
/* 31 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Server paramServer)
/*    */   {
/* 20 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 21 */     paramAny.type(type());
/* 22 */     write(localOutputStream, paramServer);
/* 23 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Server extract(Any paramAny)
/*    */   {
/* 28 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 34 */     if (__typeCode == null)
/*    */     {
/* 36 */       __typeCode = ORB.init().create_interface_tc(id(), "Server");
/*    */     }
/* 38 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 43 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Server read(InputStream paramInputStream)
/*    */   {
/* 48 */     return narrow(paramInputStream.read_Object(_ServerStub.class));
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Server paramServer)
/*    */   {
/* 53 */     paramOutputStream.write_Object(paramServer);
/*    */   }
/*    */ 
/*    */   public static Server narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 58 */     if (paramObject == null)
/* 59 */       return null;
/* 60 */     if ((paramObject instanceof Server))
/* 61 */       return (Server)paramObject;
/* 62 */     if (!paramObject._is_a(id())) {
/* 63 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 66 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 67 */     _ServerStub local_ServerStub = new _ServerStub();
/* 68 */     local_ServerStub._set_delegate(localDelegate);
/* 69 */     return local_ServerStub;
/*    */   }
/*    */ 
/*    */   public static Server unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 75 */     if (paramObject == null)
/* 76 */       return null;
/* 77 */     if ((paramObject instanceof Server)) {
/* 78 */       return (Server)paramObject;
/*    */     }
/*    */ 
/* 81 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 82 */     _ServerStub local_ServerStub = new _ServerStub();
/* 83 */     local_ServerStub._set_delegate(localDelegate);
/* 84 */     return local_ServerStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerHelper
 * JD-Core Version:    0.6.2
 */
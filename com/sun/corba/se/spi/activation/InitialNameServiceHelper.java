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
/*    */ public abstract class InitialNameServiceHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/InitialNameService:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, InitialNameService paramInitialNameService)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramInitialNameService);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static InitialNameService extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = ORB.init().create_interface_tc(id(), "InitialNameService");
/*    */     }
/* 35 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 40 */     return _id;
/*    */   }
/*    */ 
/*    */   public static InitialNameService read(InputStream paramInputStream)
/*    */   {
/* 45 */     return narrow(paramInputStream.read_Object(_InitialNameServiceStub.class));
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, InitialNameService paramInitialNameService)
/*    */   {
/* 50 */     paramOutputStream.write_Object(paramInitialNameService);
/*    */   }
/*    */ 
/*    */   public static InitialNameService narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 55 */     if (paramObject == null)
/* 56 */       return null;
/* 57 */     if ((paramObject instanceof InitialNameService))
/* 58 */       return (InitialNameService)paramObject;
/* 59 */     if (!paramObject._is_a(id())) {
/* 60 */       throw new BAD_PARAM();
/*    */     }
/*    */ 
/* 63 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 64 */     _InitialNameServiceStub local_InitialNameServiceStub = new _InitialNameServiceStub();
/* 65 */     local_InitialNameServiceStub._set_delegate(localDelegate);
/* 66 */     return local_InitialNameServiceStub;
/*    */   }
/*    */ 
/*    */   public static InitialNameService unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 72 */     if (paramObject == null)
/* 73 */       return null;
/* 74 */     if ((paramObject instanceof InitialNameService)) {
/* 75 */       return (InitialNameService)paramObject;
/*    */     }
/*    */ 
/* 78 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 79 */     _InitialNameServiceStub local_InitialNameServiceStub = new _InitialNameServiceStub();
/* 80 */     local_InitialNameServiceStub._set_delegate(localDelegate);
/* 81 */     return local_InitialNameServiceStub;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.InitialNameServiceHelper
 * JD-Core Version:    0.6.2
 */
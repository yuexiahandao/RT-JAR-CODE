/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.BAD_PARAM;
/*    */ import org.omg.CORBA.MARSHAL;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class IORInterceptor_3_0Helper
/*    */ {
/* 13 */   private static String _id = "IDL:omg.org/PortableInterceptor/IORInterceptor_3_0:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, IORInterceptor_3_0 paramIORInterceptor_3_0)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramIORInterceptor_3_0);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static IORInterceptor_3_0 extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = ORB.init().create_interface_tc(id(), "IORInterceptor_3_0");
/*    */     }
/* 35 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 40 */     return _id;
/*    */   }
/*    */ 
/*    */   public static IORInterceptor_3_0 read(InputStream paramInputStream)
/*    */   {
/* 45 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, IORInterceptor_3_0 paramIORInterceptor_3_0)
/*    */   {
/* 50 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static IORInterceptor_3_0 narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 55 */     if (paramObject == null)
/* 56 */       return null;
/* 57 */     if ((paramObject instanceof IORInterceptor_3_0)) {
/* 58 */       return (IORInterceptor_3_0)paramObject;
/*    */     }
/* 60 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   public static IORInterceptor_3_0 unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 65 */     if (paramObject == null)
/* 66 */       return null;
/* 67 */     if ((paramObject instanceof IORInterceptor_3_0)) {
/* 68 */       return (IORInterceptor_3_0)paramObject;
/*    */     }
/* 70 */     throw new BAD_PARAM();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.IORInterceptor_3_0Helper
 * JD-Core Version:    0.6.2
 */
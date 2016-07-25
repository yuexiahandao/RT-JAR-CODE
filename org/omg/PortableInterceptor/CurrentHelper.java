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
/*    */ public abstract class CurrentHelper
/*    */ {
/* 31 */   private static String _id = "IDL:omg.org/PortableInterceptor/Current:1.0";
/*    */ 
/* 46 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Current paramCurrent)
/*    */   {
/* 35 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 36 */     paramAny.type(type());
/* 37 */     write(localOutputStream, paramCurrent);
/* 38 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Current extract(Any paramAny)
/*    */   {
/* 43 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 49 */     if (__typeCode == null)
/*    */     {
/* 51 */       __typeCode = ORB.init().create_interface_tc(id(), "Current");
/*    */     }
/* 53 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 58 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Current read(InputStream paramInputStream)
/*    */   {
/* 63 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Current paramCurrent)
/*    */   {
/* 68 */     throw new MARSHAL();
/*    */   }
/*    */ 
/*    */   public static Current narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 73 */     if (paramObject == null)
/* 74 */       return null;
/* 75 */     if ((paramObject instanceof Current)) {
/* 76 */       return (Current)paramObject;
/*    */     }
/* 78 */     throw new BAD_PARAM();
/*    */   }
/*    */ 
/*    */   public static Current unchecked_narrow(org.omg.CORBA.Object paramObject)
/*    */   {
/* 83 */     if (paramObject == null)
/* 84 */       return null;
/* 85 */     if ((paramObject instanceof Current)) {
/* 86 */       return (Current)paramObject;
/*    */     }
/* 88 */     throw new BAD_PARAM();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.CurrentHelper
 * JD-Core Version:    0.6.2
 */
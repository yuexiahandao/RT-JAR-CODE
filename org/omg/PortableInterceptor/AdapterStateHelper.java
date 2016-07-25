/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class AdapterStateHelper
/*    */ {
/* 17 */   private static String _id = "IDL:omg.org/PortableInterceptor/AdapterState:1.0";
/*    */ 
/* 32 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, short paramShort)
/*    */   {
/* 21 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 22 */     paramAny.type(type());
/* 23 */     write(localOutputStream, paramShort);
/* 24 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static short extract(Any paramAny)
/*    */   {
/* 29 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 35 */     if (__typeCode == null)
/*    */     {
/* 37 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_short);
/* 38 */       __typeCode = ORB.init().create_alias_tc(id(), "AdapterState", __typeCode);
/*    */     }
/* 40 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 45 */     return _id;
/*    */   }
/*    */ 
/*    */   public static short read(InputStream paramInputStream)
/*    */   {
/* 50 */     short s = 0;
/* 51 */     s = paramInputStream.read_short();
/* 52 */     return s;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, short paramShort)
/*    */   {
/* 57 */     paramOutputStream.write_short(paramShort);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.AdapterStateHelper
 * JD-Core Version:    0.6.2
 */
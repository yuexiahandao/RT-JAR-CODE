/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class VisibilityHelper
/*    */ {
/* 39 */   private static String _id = "IDL:omg.org/CORBA/Visibility:1.0";
/*    */ 
/* 58 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, short paramShort)
/*    */   {
/* 47 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 48 */     paramAny.type(type());
/* 49 */     write(localOutputStream, paramShort);
/* 50 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static short extract(Any paramAny)
/*    */   {
/* 55 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 61 */     if (__typeCode == null)
/*    */     {
/* 63 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_short);
/* 64 */       __typeCode = ORB.init().create_alias_tc(id(), "Visibility", __typeCode);
/*    */     }
/* 66 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 71 */     return _id;
/*    */   }
/*    */ 
/*    */   public static short read(InputStream paramInputStream)
/*    */   {
/* 76 */     short s = 0;
/* 77 */     s = paramInputStream.read_short();
/* 78 */     return s;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, short paramShort)
/*    */   {
/* 83 */     paramOutputStream.write_short(paramShort);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.VisibilityHelper
 * JD-Core Version:    0.6.2
 */
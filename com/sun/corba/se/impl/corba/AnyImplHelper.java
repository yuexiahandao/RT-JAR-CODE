/*    */ package com.sun.corba.se.impl.corba;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class AnyImplHelper
/*    */ {
/* 38 */   private static String _id = "IDL:omg.org/CORBA/Any:1.0";
/*    */ 
/* 53 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny1, Any paramAny2)
/*    */   {
/* 42 */     OutputStream localOutputStream = paramAny1.create_output_stream();
/* 43 */     paramAny1.type(type());
/* 44 */     write(localOutputStream, paramAny2);
/* 45 */     paramAny1.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Any extract(Any paramAny)
/*    */   {
/* 50 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 56 */     if (__typeCode == null)
/*    */     {
/* 58 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_any);
/*    */     }
/* 60 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 65 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Any read(InputStream paramInputStream)
/*    */   {
/* 70 */     return paramInputStream.read_any();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Any paramAny)
/*    */   {
/* 75 */     paramOutputStream.write_any(paramAny);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.AnyImplHelper
 * JD-Core Version:    0.6.2
 */
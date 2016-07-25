/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public abstract class ValueBaseHelper
/*    */ {
/* 45 */   private static String _id = "IDL:omg.org/CORBA/ValueBase:1.0";
/*    */ 
/* 60 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Serializable paramSerializable)
/*    */   {
/* 49 */     org.omg.CORBA.portable.OutputStream localOutputStream = paramAny.create_output_stream();
/* 50 */     paramAny.type(type());
/* 51 */     write(localOutputStream, paramSerializable);
/* 52 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Serializable extract(Any paramAny)
/*    */   {
/* 57 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 63 */     if (__typeCode == null)
/*    */     {
/* 65 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_value);
/*    */     }
/* 67 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 72 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Serializable read(org.omg.CORBA.portable.InputStream paramInputStream)
/*    */   {
/* 77 */     return ((org.omg.CORBA_2_3.portable.InputStream)paramInputStream).read_value();
/*    */   }
/*    */ 
/*    */   public static void write(org.omg.CORBA.portable.OutputStream paramOutputStream, Serializable paramSerializable)
/*    */   {
/* 82 */     ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_value(paramSerializable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ValueBaseHelper
 * JD-Core Version:    0.6.2
 */
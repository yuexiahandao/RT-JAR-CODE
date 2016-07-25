/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ParameterModeHelper
/*    */ {
/* 22 */   private static String _id = "IDL:omg.org/CORBA/ParameterMode:1.0";
/*    */ 
/* 37 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, ParameterMode paramParameterMode)
/*    */   {
/* 26 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 27 */     paramAny.type(type());
/* 28 */     write(localOutputStream, paramParameterMode);
/* 29 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static ParameterMode extract(Any paramAny)
/*    */   {
/* 34 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 40 */     if (__typeCode == null)
/*    */     {
/* 42 */       __typeCode = ORB.init().create_enum_tc(id(), "ParameterMode", new String[] { "PARAM_IN", "PARAM_OUT", "PARAM_INOUT" });
/*    */     }
/* 44 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 49 */     return _id;
/*    */   }
/*    */ 
/*    */   public static ParameterMode read(InputStream paramInputStream)
/*    */   {
/* 54 */     return ParameterMode.from_int(paramInputStream.read_long());
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, ParameterMode paramParameterMode)
/*    */   {
/* 59 */     paramOutputStream.write_long(paramParameterMode.value());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ParameterModeHelper
 * JD-Core Version:    0.6.2
 */
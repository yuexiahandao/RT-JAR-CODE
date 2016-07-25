/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ObjectHelper
/*    */ {
/* 43 */   private static String _id = "";
/*    */ 
/* 58 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Object paramObject)
/*    */   {
/* 47 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 48 */     paramAny.type(type());
/* 49 */     write(localOutputStream, paramObject);
/* 50 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Object extract(Any paramAny)
/*    */   {
/* 55 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 61 */     if (__typeCode == null)
/*    */     {
/* 63 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_objref);
/*    */     }
/* 65 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 70 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Object read(InputStream paramInputStream)
/*    */   {
/* 75 */     return paramInputStream.read_Object();
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Object paramObject)
/*    */   {
/* 80 */     paramOutputStream.write_Object(paramObject);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ObjectHelper
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class FieldNameHelper
/*    */ {
/* 39 */   private static String _id = "IDL:omg.org/CORBA/FieldName:1.0";
/*    */ 
/* 54 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, String paramString)
/*    */   {
/* 43 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 44 */     paramAny.type(type());
/* 45 */     write(localOutputStream, paramString);
/* 46 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static String extract(Any paramAny)
/*    */   {
/* 51 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 57 */     if (__typeCode == null)
/*    */     {
/* 59 */       __typeCode = ORB.init().create_string_tc(0);
/* 60 */       __typeCode = ORB.init().create_alias_tc(id(), "FieldName", __typeCode);
/*    */     }
/* 62 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 67 */     return _id;
/*    */   }
/*    */ 
/*    */   public static String read(InputStream paramInputStream)
/*    */   {
/* 72 */     String str = null;
/* 73 */     str = paramInputStream.read_string();
/* 74 */     return str;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, String paramString)
/*    */   {
/* 79 */     paramOutputStream.write_string(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.FieldNameHelper
 * JD-Core Version:    0.6.2
 */
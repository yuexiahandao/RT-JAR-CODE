/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class AttributeModeHelper
/*    */ {
/* 37 */   private static String _id = "IDL:omg.org/CORBA/AttributeMode:1.0";
/*    */ 
/* 56 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, AttributeMode paramAttributeMode)
/*    */   {
/* 45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 46 */     paramAny.type(type());
/* 47 */     write(localOutputStream, paramAttributeMode);
/* 48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static AttributeMode extract(Any paramAny)
/*    */   {
/* 53 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 59 */     if (__typeCode == null)
/*    */     {
/* 61 */       __typeCode = ORB.init().create_enum_tc(id(), "AttributeMode", new String[] { "ATTR_NORMAL", "ATTR_READONLY" });
/*    */     }
/* 63 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 68 */     return _id;
/*    */   }
/*    */ 
/*    */   public static AttributeMode read(InputStream paramInputStream)
/*    */   {
/* 73 */     return AttributeMode.from_int(paramInputStream.read_long());
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, AttributeMode paramAttributeMode)
/*    */   {
/* 78 */     paramOutputStream.write_long(paramAttributeMode.value());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.AttributeModeHelper
 * JD-Core Version:    0.6.2
 */
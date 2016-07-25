/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class RepositoryHelper
/*    */ {
/* 29 */   private static String _id = "IDL:com.sun.omg.org/CORBA/Repository:3.0";
/*    */ 
/* 48 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Repository paramRepository)
/*    */   {
/* 37 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 38 */     paramAny.type(type());
/* 39 */     write(localOutputStream, paramRepository);
/* 40 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Repository extract(Any paramAny)
/*    */   {
/* 45 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 51 */     if (__typeCode == null)
/*    */     {
/* 53 */       __typeCode = ORB.init().create_string_tc(0);
/* 54 */       __typeCode = ORB.init().create_alias_tc(id(), "Repository", __typeCode);
/*    */     }
/* 56 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 61 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Repository read(InputStream paramInputStream)
/*    */   {
/* 66 */     String str = null;
/* 67 */     str = paramInputStream.read_string();
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Repository paramRepository)
/*    */   {
/* 73 */     paramOutputStream.write_string(null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.RepositoryHelper
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.CosNaming.NamingContextExtPackage;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class AddressHelper
/*    */ {
/* 17 */   private static String _id = "IDL:omg.org/CosNaming/NamingContextExt/Address:1.0";
/*    */ 
/* 32 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, String paramString)
/*    */   {
/* 21 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 22 */     paramAny.type(type());
/* 23 */     write(localOutputStream, paramString);
/* 24 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static String extract(Any paramAny)
/*    */   {
/* 29 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 35 */     if (__typeCode == null)
/*    */     {
/* 37 */       __typeCode = ORB.init().create_string_tc(0);
/* 38 */       __typeCode = ORB.init().create_alias_tc(id(), "Address", __typeCode);
/*    */     }
/* 40 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 45 */     return _id;
/*    */   }
/*    */ 
/*    */   public static String read(InputStream paramInputStream)
/*    */   {
/* 50 */     String str = null;
/* 51 */     str = paramInputStream.read_string();
/* 52 */     return str;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, String paramString)
/*    */   {
/* 57 */     paramOutputStream.write_string(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextExtPackage.AddressHelper
 * JD-Core Version:    0.6.2
 */
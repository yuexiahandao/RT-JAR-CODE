/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class NotFoundReasonHelper
/*    */ {
/* 17 */   private static String _id = "IDL:omg.org/CosNaming/NamingContext/NotFoundReason:1.0";
/*    */ 
/* 32 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, NotFoundReason paramNotFoundReason)
/*    */   {
/* 21 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 22 */     paramAny.type(type());
/* 23 */     write(localOutputStream, paramNotFoundReason);
/* 24 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static NotFoundReason extract(Any paramAny)
/*    */   {
/* 29 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 35 */     if (__typeCode == null)
/*    */     {
/* 37 */       __typeCode = ORB.init().create_enum_tc(id(), "NotFoundReason", new String[] { "missing_node", "not_context", "not_object" });
/*    */     }
/* 39 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 44 */     return _id;
/*    */   }
/*    */ 
/*    */   public static NotFoundReason read(InputStream paramInputStream)
/*    */   {
/* 49 */     return NotFoundReason.from_int(paramInputStream.read_long());
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, NotFoundReason paramNotFoundReason)
/*    */   {
/* 54 */     paramOutputStream.write_long(paramNotFoundReason.value());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.NotFoundReasonHelper
 * JD-Core Version:    0.6.2
 */
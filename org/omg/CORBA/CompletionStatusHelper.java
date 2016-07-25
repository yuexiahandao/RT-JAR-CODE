/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class CompletionStatusHelper
/*    */ {
/* 39 */   private static String _id = "IDL:omg.org/CORBA/CompletionStatus:1.0";
/*    */ 
/* 54 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, CompletionStatus paramCompletionStatus)
/*    */   {
/* 43 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 44 */     paramAny.type(type());
/* 45 */     write(localOutputStream, paramCompletionStatus);
/* 46 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static CompletionStatus extract(Any paramAny)
/*    */   {
/* 51 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 57 */     if (__typeCode == null)
/*    */     {
/* 59 */       __typeCode = ORB.init().create_enum_tc(id(), "CompletionStatus", new String[] { "COMPLETED_YES", "COMPLETED_NO", "COMPLETED_MAYBE" });
/*    */     }
/* 61 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 66 */     return _id;
/*    */   }
/*    */ 
/*    */   public static CompletionStatus read(InputStream paramInputStream)
/*    */   {
/* 71 */     return CompletionStatus.from_int(paramInputStream.read_long());
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, CompletionStatus paramCompletionStatus)
/*    */   {
/* 76 */     paramOutputStream.write_long(paramCompletionStatus.value());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.CompletionStatusHelper
 * JD-Core Version:    0.6.2
 */
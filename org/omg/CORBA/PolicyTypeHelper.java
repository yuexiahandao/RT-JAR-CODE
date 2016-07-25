/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class PolicyTypeHelper
/*    */ {
/* 42 */   private static String _id = "IDL:omg.org/CORBA/PolicyType:1.0";
/*    */ 
/* 57 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, int paramInt)
/*    */   {
/* 46 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 47 */     paramAny.type(type());
/* 48 */     write(localOutputStream, paramInt);
/* 49 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static int extract(Any paramAny)
/*    */   {
/* 54 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 60 */     if (__typeCode == null)
/*    */     {
/* 62 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_ulong);
/* 63 */       __typeCode = ORB.init().create_alias_tc(id(), "PolicyType", __typeCode);
/*    */     }
/* 65 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 70 */     return _id;
/*    */   }
/*    */ 
/*    */   public static int read(InputStream paramInputStream)
/*    */   {
/* 75 */     int i = 0;
/* 76 */     i = paramInputStream.read_ulong();
/* 77 */     return i;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, int paramInt)
/*    */   {
/* 82 */     paramOutputStream.write_ulong(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyTypeHelper
 * JD-Core Version:    0.6.2
 */
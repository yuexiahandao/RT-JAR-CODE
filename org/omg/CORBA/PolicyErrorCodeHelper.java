/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class PolicyErrorCodeHelper
/*    */ {
/* 19 */   private static String _id = "IDL:omg.org/CORBA/PolicyErrorCode:1.0";
/*    */ 
/* 34 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, short paramShort)
/*    */   {
/* 23 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 24 */     paramAny.type(type());
/* 25 */     write(localOutputStream, paramShort);
/* 26 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static short extract(Any paramAny)
/*    */   {
/* 31 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 37 */     if (__typeCode == null)
/*    */     {
/* 39 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_short);
/* 40 */       __typeCode = ORB.init().create_alias_tc(id(), "PolicyErrorCode", __typeCode);
/*    */     }
/* 42 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 47 */     return _id;
/*    */   }
/*    */ 
/*    */   public static short read(InputStream paramInputStream)
/*    */   {
/* 52 */     short s = 0;
/* 53 */     s = paramInputStream.read_short();
/* 54 */     return s;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, short paramShort)
/*    */   {
/* 59 */     paramOutputStream.write_short(paramShort);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyErrorCodeHelper
 * JD-Core Version:    0.6.2
 */
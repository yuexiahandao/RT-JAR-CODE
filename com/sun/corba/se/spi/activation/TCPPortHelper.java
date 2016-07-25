/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class TCPPortHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/TCPPort:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, int paramInt)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramInt);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static int extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_long);
/* 34 */       __typeCode = ORB.init().create_alias_tc(id(), "TCPPort", __typeCode);
/*    */     }
/* 36 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 41 */     return _id;
/*    */   }
/*    */ 
/*    */   public static int read(InputStream paramInputStream)
/*    */   {
/* 46 */     int i = 0;
/* 47 */     i = paramInputStream.read_long();
/* 48 */     return i;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, int paramInt)
/*    */   {
/* 53 */     paramOutputStream.write_long(paramInt);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.TCPPortHelper
 * JD-Core Version:    0.6.2
 */
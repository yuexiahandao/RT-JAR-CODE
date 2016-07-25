/*    */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class AddressingDispositionHelper
/*    */ {
/* 38 */   private static String _id = "IDL:messages/AddressingDisposition:1.0";
/*    */ 
/* 53 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, short paramShort)
/*    */   {
/* 42 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 43 */     paramAny.type(type());
/* 44 */     write(localOutputStream, paramShort);
/* 45 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static short extract(Any paramAny)
/*    */   {
/* 50 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 56 */     if (__typeCode == null)
/*    */     {
/* 58 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_short);
/* 59 */       __typeCode = ORB.init().create_alias_tc(id(), "AddressingDisposition", __typeCode);
/*    */     }
/* 61 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 66 */     return _id;
/*    */   }
/*    */ 
/*    */   public static short read(InputStream paramInputStream)
/*    */   {
/* 71 */     short s = 0;
/* 72 */     s = paramInputStream.read_short();
/* 73 */     return s;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, short paramShort)
/*    */   {
/* 78 */     paramOutputStream.write_short(paramShort);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.AddressingDispositionHelper
 * JD-Core Version:    0.6.2
 */
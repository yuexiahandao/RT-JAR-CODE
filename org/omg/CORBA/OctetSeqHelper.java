/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class OctetSeqHelper
/*    */ {
/* 52 */   private static String _id = "IDL:omg.org/CORBA/OctetSeq:1.0";
/*    */ 
/* 67 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, byte[] paramArrayOfByte)
/*    */   {
/* 56 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 57 */     paramAny.type(type());
/* 58 */     write(localOutputStream, paramArrayOfByte);
/* 59 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static byte[] extract(Any paramAny)
/*    */   {
/* 64 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 70 */     if (__typeCode == null)
/*    */     {
/* 72 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_octet);
/* 73 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 74 */       __typeCode = ORB.init().create_alias_tc(id(), "OctetSeq", __typeCode);
/*    */     }
/* 76 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 81 */     return _id;
/*    */   }
/*    */ 
/*    */   public static byte[] read(InputStream paramInputStream)
/*    */   {
/* 86 */     byte[] arrayOfByte = null;
/* 87 */     int i = paramInputStream.read_long();
/* 88 */     arrayOfByte = new byte[i];
/* 89 */     paramInputStream.read_octet_array(arrayOfByte, 0, i);
/* 90 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, byte[] paramArrayOfByte)
/*    */   {
/* 95 */     paramOutputStream.write_long(paramArrayOfByte.length);
/* 96 */     paramOutputStream.write_octet_array(paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.OctetSeqHelper
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.OctetSeqHelper;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ObjectIdHelper
/*    */ {
/* 17 */   private static String _id = "IDL:omg.org/PortableInterceptor/ObjectId:1.0";
/*    */ 
/* 32 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, byte[] paramArrayOfByte)
/*    */   {
/* 21 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 22 */     paramAny.type(type());
/* 23 */     write(localOutputStream, paramArrayOfByte);
/* 24 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static byte[] extract(Any paramAny)
/*    */   {
/* 29 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 35 */     if (__typeCode == null)
/*    */     {
/* 37 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_octet);
/* 38 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 39 */       __typeCode = ORB.init().create_alias_tc(OctetSeqHelper.id(), "OctetSeq", __typeCode);
/* 40 */       __typeCode = ORB.init().create_alias_tc(id(), "ObjectId", __typeCode);
/*    */     }
/* 42 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 47 */     return _id;
/*    */   }
/*    */ 
/*    */   public static byte[] read(InputStream paramInputStream)
/*    */   {
/* 52 */     byte[] arrayOfByte = null;
/* 53 */     arrayOfByte = OctetSeqHelper.read(paramInputStream);
/* 54 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, byte[] paramArrayOfByte)
/*    */   {
/* 59 */     OctetSeqHelper.write(paramOutputStream, paramArrayOfByte);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ObjectIdHelper
 * JD-Core Version:    0.6.2
 */
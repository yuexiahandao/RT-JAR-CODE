/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ServiceDetailHelper
/*    */ {
/*    */   private static TypeCode _tc;
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, ServiceDetail paramServiceDetail)
/*    */   {
/* 38 */     paramOutputStream.write_ulong(paramServiceDetail.service_detail_type);
/*    */ 
/* 40 */     paramOutputStream.write_long(paramServiceDetail.service_detail.length);
/* 41 */     paramOutputStream.write_octet_array(paramServiceDetail.service_detail, 0, paramServiceDetail.service_detail.length);
/*    */   }
/*    */ 
/*    */   public static ServiceDetail read(InputStream paramInputStream) {
/* 45 */     ServiceDetail localServiceDetail = new ServiceDetail();
/* 46 */     localServiceDetail.service_detail_type = paramInputStream.read_ulong();
/*    */ 
/* 48 */     int i = paramInputStream.read_long();
/* 49 */     localServiceDetail.service_detail = new byte[i];
/* 50 */     paramInputStream.read_octet_array(localServiceDetail.service_detail, 0, localServiceDetail.service_detail.length);
/*    */ 
/* 52 */     return localServiceDetail;
/*    */   }
/*    */   public static ServiceDetail extract(Any paramAny) {
/* 55 */     InputStream localInputStream = paramAny.create_input_stream();
/* 56 */     return read(localInputStream);
/*    */   }
/*    */   public static void insert(Any paramAny, ServiceDetail paramServiceDetail) {
/* 59 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 60 */     write(localOutputStream, paramServiceDetail);
/* 61 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type() {
/* 65 */     int i = 2;
/* 66 */     StructMember[] arrayOfStructMember = null;
/* 67 */     if (_tc == null) {
/* 68 */       arrayOfStructMember = new StructMember[2];
/* 69 */       arrayOfStructMember[0] = new StructMember("service_detail_type", ORB.init().get_primitive_tc(TCKind.tk_ulong), null);
/*    */ 
/* 74 */       arrayOfStructMember[1] = new StructMember("service_detail", ORB.init().create_sequence_tc(0, ORB.init().get_primitive_tc(TCKind.tk_octet)), null);
/*    */ 
/* 78 */       _tc = ORB.init().create_struct_tc(id(), "ServiceDetail", arrayOfStructMember);
/*    */     }
/* 80 */     return _tc;
/*    */   }
/*    */   public static String id() {
/* 83 */     return "IDL:omg.org/CORBA/ServiceDetail:1.0";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ServiceDetailHelper
 * JD-Core Version:    0.6.2
 */
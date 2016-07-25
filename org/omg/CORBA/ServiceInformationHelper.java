/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ServiceInformationHelper
/*    */ {
/*    */   private static TypeCode _tc;
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, ServiceInformation paramServiceInformation)
/*    */   {
/* 39 */     paramOutputStream.write_long(paramServiceInformation.service_options.length);
/* 40 */     paramOutputStream.write_ulong_array(paramServiceInformation.service_options, 0, paramServiceInformation.service_options.length);
/* 41 */     paramOutputStream.write_long(paramServiceInformation.service_details.length);
/* 42 */     for (int i = 0; i < paramServiceInformation.service_details.length; i++)
/* 43 */       ServiceDetailHelper.write(paramOutputStream, paramServiceInformation.service_details[i]);
/*    */   }
/*    */ 
/*    */   public static ServiceInformation read(InputStream paramInputStream)
/*    */   {
/* 48 */     ServiceInformation localServiceInformation = new ServiceInformation();
/*    */ 
/* 50 */     int i = paramInputStream.read_long();
/* 51 */     localServiceInformation.service_options = new int[i];
/* 52 */     paramInputStream.read_ulong_array(localServiceInformation.service_options, 0, localServiceInformation.service_options.length);
/*    */ 
/* 55 */     i = paramInputStream.read_long();
/* 56 */     localServiceInformation.service_details = new ServiceDetail[i];
/* 57 */     for (int j = 0; j < localServiceInformation.service_details.length; j++) {
/* 58 */       localServiceInformation.service_details[j] = ServiceDetailHelper.read(paramInputStream);
/*    */     }
/*    */ 
/* 61 */     return localServiceInformation;
/*    */   }
/*    */   public static ServiceInformation extract(Any paramAny) {
/* 64 */     InputStream localInputStream = paramAny.create_input_stream();
/* 65 */     return read(localInputStream);
/*    */   }
/*    */   public static void insert(Any paramAny, ServiceInformation paramServiceInformation) {
/* 68 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 69 */     write(localOutputStream, paramServiceInformation);
/* 70 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type() {
/* 74 */     int i = 2;
/* 75 */     StructMember[] arrayOfStructMember = null;
/* 76 */     if (_tc == null) {
/* 77 */       arrayOfStructMember = new StructMember[2];
/* 78 */       arrayOfStructMember[0] = new StructMember("service_options", ORB.init().create_sequence_tc(0, ORB.init().get_primitive_tc(TCKind.tk_ulong)), null);
/*    */ 
/* 83 */       arrayOfStructMember[1] = new StructMember("service_details", ORB.init().create_sequence_tc(0, ServiceDetailHelper.type()), null);
/*    */ 
/* 87 */       _tc = ORB.init().create_struct_tc(id(), "ServiceInformation", arrayOfStructMember);
/*    */     }
/* 89 */     return _tc;
/*    */   }
/*    */   public static String id() {
/* 92 */     return "IDL:omg.org/CORBA/ServiceInformation:1.0";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ServiceInformationHelper
 * JD-Core Version:    0.6.2
 */
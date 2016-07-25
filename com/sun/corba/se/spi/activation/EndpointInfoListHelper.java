/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class EndpointInfoListHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/EndpointInfoList:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, EndPointInfo[] paramArrayOfEndPointInfo)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramArrayOfEndPointInfo);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static EndPointInfo[] extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = EndPointInfoHelper.type();
/* 34 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 35 */       __typeCode = ORB.init().create_alias_tc(id(), "EndpointInfoList", __typeCode);
/*    */     }
/* 37 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 42 */     return _id;
/*    */   }
/*    */ 
/*    */   public static EndPointInfo[] read(InputStream paramInputStream)
/*    */   {
/* 47 */     EndPointInfo[] arrayOfEndPointInfo = null;
/* 48 */     int i = paramInputStream.read_long();
/* 49 */     arrayOfEndPointInfo = new EndPointInfo[i];
/* 50 */     for (int j = 0; j < arrayOfEndPointInfo.length; j++)
/* 51 */       arrayOfEndPointInfo[j] = EndPointInfoHelper.read(paramInputStream);
/* 52 */     return arrayOfEndPointInfo;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, EndPointInfo[] paramArrayOfEndPointInfo)
/*    */   {
/* 57 */     paramOutputStream.write_long(paramArrayOfEndPointInfo.length);
/* 58 */     for (int i = 0; i < paramArrayOfEndPointInfo.length; i++)
/* 59 */       EndPointInfoHelper.write(paramOutputStream, paramArrayOfEndPointInfo[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.EndpointInfoListHelper
 * JD-Core Version:    0.6.2
 */
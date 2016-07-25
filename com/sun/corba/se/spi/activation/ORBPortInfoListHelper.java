/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ORBPortInfoListHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/ORBPortInfoList:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, ORBPortInfo[] paramArrayOfORBPortInfo)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramArrayOfORBPortInfo);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static ORBPortInfo[] extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = ORBPortInfoHelper.type();
/* 34 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 35 */       __typeCode = ORB.init().create_alias_tc(id(), "ORBPortInfoList", __typeCode);
/*    */     }
/* 37 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 42 */     return _id;
/*    */   }
/*    */ 
/*    */   public static ORBPortInfo[] read(InputStream paramInputStream)
/*    */   {
/* 47 */     ORBPortInfo[] arrayOfORBPortInfo = null;
/* 48 */     int i = paramInputStream.read_long();
/* 49 */     arrayOfORBPortInfo = new ORBPortInfo[i];
/* 50 */     for (int j = 0; j < arrayOfORBPortInfo.length; j++)
/* 51 */       arrayOfORBPortInfo[j] = ORBPortInfoHelper.read(paramInputStream);
/* 52 */     return arrayOfORBPortInfo;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, ORBPortInfo[] paramArrayOfORBPortInfo)
/*    */   {
/* 57 */     paramOutputStream.write_long(paramArrayOfORBPortInfo.length);
/* 58 */     for (int i = 0; i < paramArrayOfORBPortInfo.length; i++)
/* 59 */       ORBPortInfoHelper.write(paramOutputStream, paramArrayOfORBPortInfo[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ORBPortInfoListHelper
 * JD-Core Version:    0.6.2
 */
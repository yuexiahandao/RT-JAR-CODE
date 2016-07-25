/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ServerIdsHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/ServerIds:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, int[] paramArrayOfInt)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramArrayOfInt);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static int[] extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_long);
/* 34 */       __typeCode = ORB.init().create_alias_tc(ServerIdHelper.id(), "ServerId", __typeCode);
/* 35 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 36 */       __typeCode = ORB.init().create_alias_tc(id(), "ServerIds", __typeCode);
/*    */     }
/* 38 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 43 */     return _id;
/*    */   }
/*    */ 
/*    */   public static int[] read(InputStream paramInputStream)
/*    */   {
/* 48 */     int[] arrayOfInt = null;
/* 49 */     int i = paramInputStream.read_long();
/* 50 */     arrayOfInt = new int[i];
/* 51 */     for (int j = 0; j < arrayOfInt.length; j++)
/* 52 */       arrayOfInt[j] = ServerIdHelper.read(paramInputStream);
/* 53 */     return arrayOfInt;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, int[] paramArrayOfInt)
/*    */   {
/* 58 */     paramOutputStream.write_long(paramArrayOfInt.length);
/* 59 */     for (int i = 0; i < paramArrayOfInt.length; i++)
/* 60 */       ServerIdHelper.write(paramOutputStream, paramArrayOfInt[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerIdsHelper
 * JD-Core Version:    0.6.2
 */
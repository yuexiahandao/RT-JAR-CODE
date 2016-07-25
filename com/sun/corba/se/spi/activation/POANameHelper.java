/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class POANameHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/POAName:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, String[] paramArrayOfString)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramArrayOfString);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static String[] extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = ORB.init().create_string_tc(0);
/* 34 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 35 */       __typeCode = ORB.init().create_alias_tc(id(), "POAName", __typeCode);
/*    */     }
/* 37 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 42 */     return _id;
/*    */   }
/*    */ 
/*    */   public static String[] read(InputStream paramInputStream)
/*    */   {
/* 47 */     String[] arrayOfString = null;
/* 48 */     int i = paramInputStream.read_long();
/* 49 */     arrayOfString = new String[i];
/* 50 */     for (int j = 0; j < arrayOfString.length; j++)
/* 51 */       arrayOfString[j] = paramInputStream.read_string();
/* 52 */     return arrayOfString;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, String[] paramArrayOfString)
/*    */   {
/* 57 */     paramOutputStream.write_long(paramArrayOfString.length);
/* 58 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 59 */       paramOutputStream.write_string(paramArrayOfString[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.POANameHelper
 * JD-Core Version:    0.6.2
 */
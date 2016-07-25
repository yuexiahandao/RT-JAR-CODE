/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class InitializerSeqHelper
/*    */ {
/* 37 */   private static String _id = "IDL:omg.org/CORBA/InitializerSeq:1.0";
/*    */ 
/* 56 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Initializer[] paramArrayOfInitializer)
/*    */   {
/* 45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 46 */     paramAny.type(type());
/* 47 */     write(localOutputStream, paramArrayOfInitializer);
/* 48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Initializer[] extract(Any paramAny)
/*    */   {
/* 53 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 59 */     if (__typeCode == null)
/*    */     {
/* 61 */       __typeCode = InitializerHelper.type();
/* 62 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 63 */       __typeCode = ORB.init().create_alias_tc(id(), "InitializerSeq", __typeCode);
/*    */     }
/* 65 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 70 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Initializer[] read(InputStream paramInputStream)
/*    */   {
/* 75 */     Initializer[] arrayOfInitializer = null;
/* 76 */     int i = paramInputStream.read_long();
/* 77 */     arrayOfInitializer = new Initializer[i];
/* 78 */     for (int j = 0; j < arrayOfInitializer.length; j++)
/* 79 */       arrayOfInitializer[j] = InitializerHelper.read(paramInputStream);
/* 80 */     return arrayOfInitializer;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Initializer[] paramArrayOfInitializer)
/*    */   {
/* 85 */     paramOutputStream.write_long(paramArrayOfInitializer.length);
/* 86 */     for (int i = 0; i < paramArrayOfInitializer.length; i++)
/* 87 */       InitializerHelper.write(paramOutputStream, paramArrayOfInitializer[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.InitializerSeqHelper
 * JD-Core Version:    0.6.2
 */
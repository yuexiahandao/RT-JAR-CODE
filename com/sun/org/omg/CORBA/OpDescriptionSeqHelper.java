/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class OpDescriptionSeqHelper
/*    */ {
/* 37 */   private static String _id = "IDL:omg.org/CORBA/OpDescriptionSeq:1.0";
/*    */ 
/* 56 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, OperationDescription[] paramArrayOfOperationDescription)
/*    */   {
/* 45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 46 */     paramAny.type(type());
/* 47 */     write(localOutputStream, paramArrayOfOperationDescription);
/* 48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static OperationDescription[] extract(Any paramAny)
/*    */   {
/* 53 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 59 */     if (__typeCode == null)
/*    */     {
/* 61 */       __typeCode = OperationDescriptionHelper.type();
/* 62 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 63 */       __typeCode = ORB.init().create_alias_tc(id(), "OpDescriptionSeq", __typeCode);
/*    */     }
/* 65 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 70 */     return _id;
/*    */   }
/*    */ 
/*    */   public static OperationDescription[] read(InputStream paramInputStream)
/*    */   {
/* 75 */     OperationDescription[] arrayOfOperationDescription = null;
/* 76 */     int i = paramInputStream.read_long();
/* 77 */     arrayOfOperationDescription = new OperationDescription[i];
/* 78 */     for (int j = 0; j < arrayOfOperationDescription.length; j++)
/* 79 */       arrayOfOperationDescription[j] = OperationDescriptionHelper.read(paramInputStream);
/* 80 */     return arrayOfOperationDescription;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, OperationDescription[] paramArrayOfOperationDescription)
/*    */   {
/* 85 */     paramOutputStream.write_long(paramArrayOfOperationDescription.length);
/* 86 */     for (int i = 0; i < paramArrayOfOperationDescription.length; i++)
/* 87 */       OperationDescriptionHelper.write(paramOutputStream, paramArrayOfOperationDescription[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.OpDescriptionSeqHelper
 * JD-Core Version:    0.6.2
 */
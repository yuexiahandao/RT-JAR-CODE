/*    */ package com.sun.org.omg.SendingContext.CodeBasePackage;
/*    */ 
/*    */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
/*    */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescriptionHelper;
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class ValueDescSeqHelper
/*    */ {
/* 37 */   private static String _id = "IDL:omg.org/SendingContext/CodeBase/ValueDescSeq:1.0";
/*    */ 
/* 56 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, FullValueDescription[] paramArrayOfFullValueDescription)
/*    */   {
/* 45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 46 */     paramAny.type(type());
/* 47 */     write(localOutputStream, paramArrayOfFullValueDescription);
/* 48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static FullValueDescription[] extract(Any paramAny)
/*    */   {
/* 53 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 59 */     if (__typeCode == null)
/*    */     {
/* 61 */       __typeCode = FullValueDescriptionHelper.type();
/* 62 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 63 */       __typeCode = ORB.init().create_alias_tc(id(), "ValueDescSeq", __typeCode);
/*    */     }
/* 65 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 70 */     return _id;
/*    */   }
/*    */ 
/*    */   public static FullValueDescription[] read(InputStream paramInputStream)
/*    */   {
/* 75 */     FullValueDescription[] arrayOfFullValueDescription = null;
/* 76 */     int i = paramInputStream.read_long();
/* 77 */     arrayOfFullValueDescription = new FullValueDescription[i];
/* 78 */     for (int j = 0; j < arrayOfFullValueDescription.length; j++)
/* 79 */       arrayOfFullValueDescription[j] = FullValueDescriptionHelper.read(paramInputStream);
/* 80 */     return arrayOfFullValueDescription;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, FullValueDescription[] paramArrayOfFullValueDescription)
/*    */   {
/* 85 */     paramOutputStream.write_long(paramArrayOfFullValueDescription.length);
/* 86 */     for (int i = 0; i < paramArrayOfFullValueDescription.length; i++)
/* 87 */       FullValueDescriptionHelper.write(paramOutputStream, paramArrayOfFullValueDescription[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.SendingContext.CodeBasePackage.ValueDescSeqHelper
 * JD-Core Version:    0.6.2
 */
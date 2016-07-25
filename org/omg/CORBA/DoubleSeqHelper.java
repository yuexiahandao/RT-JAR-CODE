/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class DoubleSeqHelper
/*    */ {
/* 52 */   private static String _id = "IDL:omg.org/CORBA/DoubleSeq:1.0";
/*    */ 
/* 67 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, double[] paramArrayOfDouble)
/*    */   {
/* 56 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 57 */     paramAny.type(type());
/* 58 */     write(localOutputStream, paramArrayOfDouble);
/* 59 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static double[] extract(Any paramAny)
/*    */   {
/* 64 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 70 */     if (__typeCode == null)
/*    */     {
/* 72 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_double);
/* 73 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 74 */       __typeCode = ORB.init().create_alias_tc(id(), "DoubleSeq", __typeCode);
/*    */     }
/* 76 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 81 */     return _id;
/*    */   }
/*    */ 
/*    */   public static double[] read(InputStream paramInputStream)
/*    */   {
/* 86 */     double[] arrayOfDouble = null;
/* 87 */     int i = paramInputStream.read_long();
/* 88 */     arrayOfDouble = new double[i];
/* 89 */     paramInputStream.read_double_array(arrayOfDouble, 0, i);
/* 90 */     return arrayOfDouble;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, double[] paramArrayOfDouble)
/*    */   {
/* 95 */     paramOutputStream.write_long(paramArrayOfDouble.length);
/* 96 */     paramOutputStream.write_double_array(paramArrayOfDouble, 0, paramArrayOfDouble.length);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DoubleSeqHelper
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.DynamicAny;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class DynAnySeqHelper
/*    */ {
/* 13 */   private static String _id = "IDL:omg.org/DynamicAny/DynAnySeq:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, DynAny[] paramArrayOfDynAny)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramArrayOfDynAny);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static DynAny[] extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = DynAnyHelper.type();
/* 34 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 35 */       __typeCode = ORB.init().create_alias_tc(id(), "DynAnySeq", __typeCode);
/*    */     }
/* 37 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 42 */     return _id;
/*    */   }
/*    */ 
/*    */   public static DynAny[] read(InputStream paramInputStream)
/*    */   {
/* 47 */     DynAny[] arrayOfDynAny = null;
/* 48 */     int i = paramInputStream.read_long();
/* 49 */     arrayOfDynAny = new DynAny[i];
/* 50 */     for (int j = 0; j < arrayOfDynAny.length; j++)
/* 51 */       arrayOfDynAny[j] = DynAnyHelper.read(paramInputStream);
/* 52 */     return arrayOfDynAny;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, DynAny[] paramArrayOfDynAny)
/*    */   {
/* 57 */     paramOutputStream.write_long(paramArrayOfDynAny.length);
/* 58 */     for (int i = 0; i < paramArrayOfDynAny.length; i++)
/* 59 */       DynAnyHelper.write(paramOutputStream, paramArrayOfDynAny[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynAnySeqHelper
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.DynamicAny;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class AnySeqHelper
/*    */ {
/* 13 */   private static String _id = "IDL:omg.org/DynamicAny/AnySeq:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Any[] paramArrayOfAny)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramArrayOfAny);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Any[] extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 31 */     if (__typeCode == null)
/*    */     {
/* 33 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_any);
/* 34 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 35 */       __typeCode = ORB.init().create_alias_tc(id(), "AnySeq", __typeCode);
/*    */     }
/* 37 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 42 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Any[] read(InputStream paramInputStream)
/*    */   {
/* 47 */     Any[] arrayOfAny = null;
/* 48 */     int i = paramInputStream.read_long();
/* 49 */     arrayOfAny = new Any[i];
/* 50 */     for (int j = 0; j < arrayOfAny.length; j++)
/* 51 */       arrayOfAny[j] = paramInputStream.read_any();
/* 52 */     return arrayOfAny;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Any[] paramArrayOfAny)
/*    */   {
/* 57 */     paramOutputStream.write_long(paramArrayOfAny.length);
/* 58 */     for (int i = 0; i < paramArrayOfAny.length; i++)
/* 59 */       paramOutputStream.write_any(paramArrayOfAny[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.AnySeqHelper
 * JD-Core Version:    0.6.2
 */
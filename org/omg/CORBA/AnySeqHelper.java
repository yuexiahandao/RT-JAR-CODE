/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class AnySeqHelper
/*    */ {
/* 52 */   private static String _id = "IDL:omg.org/CORBA/AnySeq:1.0";
/*    */ 
/* 67 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, Any[] paramArrayOfAny)
/*    */   {
/* 56 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 57 */     paramAny.type(type());
/* 58 */     write(localOutputStream, paramArrayOfAny);
/* 59 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Any[] extract(Any paramAny)
/*    */   {
/* 64 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 70 */     if (__typeCode == null)
/*    */     {
/* 72 */       __typeCode = ORB.init().get_primitive_tc(TCKind.tk_any);
/* 73 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 74 */       __typeCode = ORB.init().create_alias_tc(id(), "AnySeq", __typeCode);
/*    */     }
/* 76 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 81 */     return _id;
/*    */   }
/*    */ 
/*    */   public static Any[] read(InputStream paramInputStream)
/*    */   {
/* 86 */     Any[] arrayOfAny = null;
/* 87 */     int i = paramInputStream.read_long();
/* 88 */     arrayOfAny = new Any[i];
/* 89 */     for (int j = 0; j < arrayOfAny.length; j++)
/* 90 */       arrayOfAny[j] = paramInputStream.read_any();
/* 91 */     return arrayOfAny;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Any[] paramArrayOfAny)
/*    */   {
/* 96 */     paramOutputStream.write_long(paramArrayOfAny.length);
/* 97 */     for (int i = 0; i < paramArrayOfAny.length; i++)
/* 98 */       paramOutputStream.write_any(paramArrayOfAny[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.AnySeqHelper
 * JD-Core Version:    0.6.2
 */
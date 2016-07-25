/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class WStringSeqHelper
/*    */ {
/* 15 */   private static String _id = "IDL:omg.org/CORBA/WStringSeq:1.0";
/*    */ 
/* 30 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, String[] paramArrayOfString)
/*    */   {
/* 19 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 20 */     paramAny.type(type());
/* 21 */     write(localOutputStream, paramArrayOfString);
/* 22 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static String[] extract(Any paramAny)
/*    */   {
/* 27 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 33 */     if (__typeCode == null)
/*    */     {
/* 35 */       __typeCode = ORB.init().create_wstring_tc(0);
/* 36 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 37 */       __typeCode = ORB.init().create_alias_tc(id(), "WStringSeq", __typeCode);
/*    */     }
/* 39 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 44 */     return _id;
/*    */   }
/*    */ 
/*    */   public static String[] read(InputStream paramInputStream)
/*    */   {
/* 49 */     String[] arrayOfString = null;
/* 50 */     int i = paramInputStream.read_long();
/* 51 */     arrayOfString = new String[i];
/* 52 */     for (int j = 0; j < arrayOfString.length; j++)
/* 53 */       arrayOfString[j] = paramInputStream.read_wstring();
/* 54 */     return arrayOfString;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, String[] paramArrayOfString)
/*    */   {
/* 59 */     paramOutputStream.write_long(paramArrayOfString.length);
/* 60 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 61 */       paramOutputStream.write_wstring(paramArrayOfString[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.WStringSeqHelper
 * JD-Core Version:    0.6.2
 */
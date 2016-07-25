/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class ContextIdSeqHelper
/*    */ {
/* 37 */   private static String _id = "IDL:omg.org/CORBA/ContextIdSeq:1.0";
/*    */ 
/* 56 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, String[] paramArrayOfString)
/*    */   {
/* 45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 46 */     paramAny.type(type());
/* 47 */     write(localOutputStream, paramArrayOfString);
/* 48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static String[] extract(Any paramAny)
/*    */   {
/* 53 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 59 */     if (__typeCode == null)
/*    */     {
/* 61 */       __typeCode = ORB.init().create_string_tc(0);
/* 62 */       __typeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", __typeCode);
/* 63 */       __typeCode = ORB.init().create_alias_tc(ContextIdentifierHelper.id(), "ContextIdentifier", __typeCode);
/* 64 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 65 */       __typeCode = ORB.init().create_alias_tc(id(), "ContextIdSeq", __typeCode);
/*    */     }
/* 67 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 72 */     return _id;
/*    */   }
/*    */ 
/*    */   public static String[] read(InputStream paramInputStream)
/*    */   {
/* 77 */     String[] arrayOfString = null;
/* 78 */     int i = paramInputStream.read_long();
/* 79 */     arrayOfString = new String[i];
/* 80 */     for (int j = 0; j < arrayOfString.length; j++)
/* 81 */       arrayOfString[j] = ContextIdentifierHelper.read(paramInputStream);
/* 82 */     return arrayOfString;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, String[] paramArrayOfString)
/*    */   {
/* 87 */     paramOutputStream.write_long(paramArrayOfString.length);
/* 88 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 89 */       ContextIdentifierHelper.write(paramOutputStream, paramArrayOfString[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ContextIdSeqHelper
 * JD-Core Version:    0.6.2
 */
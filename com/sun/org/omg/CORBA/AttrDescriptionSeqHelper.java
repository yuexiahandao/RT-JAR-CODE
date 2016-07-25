/*    */ package com.sun.org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public final class AttrDescriptionSeqHelper
/*    */ {
/* 37 */   private static String _id = "IDL:omg.org/CORBA/AttrDescriptionSeq:1.0";
/*    */ 
/* 56 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, AttributeDescription[] paramArrayOfAttributeDescription)
/*    */   {
/* 45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 46 */     paramAny.type(type());
/* 47 */     write(localOutputStream, paramArrayOfAttributeDescription);
/* 48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static AttributeDescription[] extract(Any paramAny)
/*    */   {
/* 53 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 59 */     if (__typeCode == null)
/*    */     {
/* 61 */       __typeCode = AttributeDescriptionHelper.type();
/* 62 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 63 */       __typeCode = ORB.init().create_alias_tc(id(), "AttrDescriptionSeq", __typeCode);
/*    */     }
/* 65 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 70 */     return _id;
/*    */   }
/*    */ 
/*    */   public static AttributeDescription[] read(InputStream paramInputStream)
/*    */   {
/* 75 */     AttributeDescription[] arrayOfAttributeDescription = null;
/* 76 */     int i = paramInputStream.read_long();
/* 77 */     arrayOfAttributeDescription = new AttributeDescription[i];
/* 78 */     for (int j = 0; j < arrayOfAttributeDescription.length; j++)
/* 79 */       arrayOfAttributeDescription[j] = AttributeDescriptionHelper.read(paramInputStream);
/* 80 */     return arrayOfAttributeDescription;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, AttributeDescription[] paramArrayOfAttributeDescription)
/*    */   {
/* 85 */     paramOutputStream.write_long(paramArrayOfAttributeDescription.length);
/* 86 */     for (int i = 0; i < paramArrayOfAttributeDescription.length; i++)
/* 87 */       AttributeDescriptionHelper.write(paramOutputStream, paramArrayOfAttributeDescription[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.AttrDescriptionSeqHelper
 * JD-Core Version:    0.6.2
 */
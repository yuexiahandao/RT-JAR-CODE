/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ObjectReferenceTemplateSeqHelper
/*    */ {
/* 17 */   private static String _id = "IDL:omg.org/PortableInterceptor/ObjectReferenceTemplateSeq:1.0";
/*    */ 
/* 32 */   private static TypeCode __typeCode = null;
/*    */ 
/*    */   public static void insert(Any paramAny, ObjectReferenceTemplate[] paramArrayOfObjectReferenceTemplate)
/*    */   {
/* 21 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 22 */     paramAny.type(type());
/* 23 */     write(localOutputStream, paramArrayOfObjectReferenceTemplate);
/* 24 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static ObjectReferenceTemplate[] extract(Any paramAny)
/*    */   {
/* 29 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 35 */     if (__typeCode == null)
/*    */     {
/* 37 */       __typeCode = ObjectReferenceTemplateHelper.type();
/* 38 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/* 39 */       __typeCode = ORB.init().create_alias_tc(id(), "ObjectReferenceTemplateSeq", __typeCode);
/*    */     }
/* 41 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 46 */     return _id;
/*    */   }
/*    */ 
/*    */   public static ObjectReferenceTemplate[] read(InputStream paramInputStream)
/*    */   {
/* 51 */     ObjectReferenceTemplate[] arrayOfObjectReferenceTemplate = null;
/* 52 */     int i = paramInputStream.read_long();
/* 53 */     arrayOfObjectReferenceTemplate = new ObjectReferenceTemplate[i];
/* 54 */     for (int j = 0; j < arrayOfObjectReferenceTemplate.length; j++)
/* 55 */       arrayOfObjectReferenceTemplate[j] = ObjectReferenceTemplateHelper.read(paramInputStream);
/* 56 */     return arrayOfObjectReferenceTemplate;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, ObjectReferenceTemplate[] paramArrayOfObjectReferenceTemplate)
/*    */   {
/* 61 */     paramOutputStream.write_long(paramArrayOfObjectReferenceTemplate.length);
/* 62 */     for (int i = 0; i < paramArrayOfObjectReferenceTemplate.length; i++)
/* 63 */       ObjectReferenceTemplateHelper.write(paramOutputStream, paramArrayOfObjectReferenceTemplate[i]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ObjectReferenceTemplateSeqHelper
 * JD-Core Version:    0.6.2
 */
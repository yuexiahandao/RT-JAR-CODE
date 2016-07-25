/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.ValueMember;
/*    */ 
/*    */ public abstract class ObjectReferenceTemplateHelper
/*    */ {
/* 21 */   private static String _id = "IDL:omg.org/PortableInterceptor/ObjectReferenceTemplate:1.0";
/*    */ 
/* 37 */   private static TypeCode __typeCode = null;
/* 38 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, ObjectReferenceTemplate paramObjectReferenceTemplate)
/*    */   {
/* 26 */     org.omg.CORBA.portable.OutputStream localOutputStream = paramAny.create_output_stream();
/* 27 */     paramAny.type(type());
/* 28 */     write(localOutputStream, paramObjectReferenceTemplate);
/* 29 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static ObjectReferenceTemplate extract(Any paramAny)
/*    */   {
/* 34 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 41 */     if (__typeCode == null)
/*    */     {
/* 43 */       synchronized (TypeCode.class)
/*    */       {
/* 45 */         if (__typeCode == null)
/*    */         {
/* 47 */           if (__active)
/*    */           {
/* 49 */             return ORB.init().create_recursive_tc(_id);
/*    */           }
/* 51 */           __active = true;
/* 52 */           ValueMember[] arrayOfValueMember = new ValueMember[0];
/* 53 */           Object localObject1 = null;
/* 54 */           __typeCode = ORB.init().create_value_tc(_id, "ObjectReferenceTemplate", (short)2, null, arrayOfValueMember);
/* 55 */           __active = false;
/*    */         }
/*    */       }
/*    */     }
/* 59 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 64 */     return _id;
/*    */   }
/*    */ 
/*    */   public static ObjectReferenceTemplate read(org.omg.CORBA.portable.InputStream paramInputStream)
/*    */   {
/* 69 */     return (ObjectReferenceTemplate)((org.omg.CORBA_2_3.portable.InputStream)paramInputStream).read_value(id());
/*    */   }
/*    */ 
/*    */   public static void write(org.omg.CORBA.portable.OutputStream paramOutputStream, ObjectReferenceTemplate paramObjectReferenceTemplate)
/*    */   {
/* 74 */     ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_value(paramObjectReferenceTemplate, id());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ObjectReferenceTemplateHelper
 * JD-Core Version:    0.6.2
 */
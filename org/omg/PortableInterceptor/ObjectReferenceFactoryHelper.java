/*    */ package org.omg.PortableInterceptor;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.ValueMember;
/*    */ 
/*    */ public abstract class ObjectReferenceFactoryHelper
/*    */ {
/* 17 */   private static String _id = "IDL:omg.org/PortableInterceptor/ObjectReferenceFactory:1.0";
/*    */ 
/* 33 */   private static TypeCode __typeCode = null;
/* 34 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, ObjectReferenceFactory paramObjectReferenceFactory)
/*    */   {
/* 22 */     org.omg.CORBA.portable.OutputStream localOutputStream = paramAny.create_output_stream();
/* 23 */     paramAny.type(type());
/* 24 */     write(localOutputStream, paramObjectReferenceFactory);
/* 25 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static ObjectReferenceFactory extract(Any paramAny)
/*    */   {
/* 30 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 37 */     if (__typeCode == null)
/*    */     {
/* 39 */       synchronized (TypeCode.class)
/*    */       {
/* 41 */         if (__typeCode == null)
/*    */         {
/* 43 */           if (__active)
/*    */           {
/* 45 */             return ORB.init().create_recursive_tc(_id);
/*    */           }
/* 47 */           __active = true;
/* 48 */           ValueMember[] arrayOfValueMember = new ValueMember[0];
/* 49 */           Object localObject1 = null;
/* 50 */           __typeCode = ORB.init().create_value_tc(_id, "ObjectReferenceFactory", (short)2, null, arrayOfValueMember);
/* 51 */           __active = false;
/*    */         }
/*    */       }
/*    */     }
/* 55 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 60 */     return _id;
/*    */   }
/*    */ 
/*    */   public static ObjectReferenceFactory read(org.omg.CORBA.portable.InputStream paramInputStream)
/*    */   {
/* 65 */     return (ObjectReferenceFactory)((org.omg.CORBA_2_3.portable.InputStream)paramInputStream).read_value(id());
/*    */   }
/*    */ 
/*    */   public static void write(org.omg.CORBA.portable.OutputStream paramOutputStream, ObjectReferenceFactory paramObjectReferenceFactory)
/*    */   {
/* 70 */     ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_value(paramObjectReferenceFactory, id());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ObjectReferenceFactoryHelper
 * JD-Core Version:    0.6.2
 */
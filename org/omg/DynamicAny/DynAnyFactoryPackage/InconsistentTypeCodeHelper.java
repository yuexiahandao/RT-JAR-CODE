/*    */ package org.omg.DynamicAny.DynAnyFactoryPackage;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.StructMember;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class InconsistentTypeCodeHelper
/*    */ {
/* 13 */   private static String _id = "IDL:omg.org/DynamicAny/DynAnyFactory/InconsistentTypeCode:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/* 29 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, InconsistentTypeCode paramInconsistentTypeCode)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramInconsistentTypeCode);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static InconsistentTypeCode extract(Any paramAny)
/*    */   {
/* 25 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 32 */     if (__typeCode == null)
/*    */     {
/* 34 */       synchronized (TypeCode.class)
/*    */       {
/* 36 */         if (__typeCode == null)
/*    */         {
/* 38 */           if (__active)
/*    */           {
/* 40 */             return ORB.init().create_recursive_tc(_id);
/*    */           }
/* 42 */           __active = true;
/* 43 */           StructMember[] arrayOfStructMember = new StructMember[0];
/* 44 */           Object localObject1 = null;
/* 45 */           __typeCode = ORB.init().create_exception_tc(id(), "InconsistentTypeCode", arrayOfStructMember);
/* 46 */           __active = false;
/*    */         }
/*    */       }
/*    */     }
/* 50 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 55 */     return _id;
/*    */   }
/*    */ 
/*    */   public static InconsistentTypeCode read(InputStream paramInputStream)
/*    */   {
/* 60 */     InconsistentTypeCode localInconsistentTypeCode = new InconsistentTypeCode();
/*    */ 
/* 62 */     paramInputStream.read_string();
/* 63 */     return localInconsistentTypeCode;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, InconsistentTypeCode paramInconsistentTypeCode)
/*    */   {
/* 69 */     paramOutputStream.write_string(id());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCodeHelper
 * JD-Core Version:    0.6.2
 */
/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.StructMember;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class NameComponentHelper
/*    */ {
/* 13 */   private static String _id = "IDL:omg.org/CosNaming/NameComponent:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/* 29 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, NameComponent paramNameComponent)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramNameComponent);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static NameComponent extract(Any paramAny)
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
/* 43 */           StructMember[] arrayOfStructMember = new StructMember[2];
/* 44 */           TypeCode localTypeCode = null;
/* 45 */           localTypeCode = ORB.init().create_string_tc(0);
/* 46 */           localTypeCode = ORB.init().create_alias_tc(IstringHelper.id(), "Istring", localTypeCode);
/* 47 */           arrayOfStructMember[0] = new StructMember("id", localTypeCode, null);
/*    */ 
/* 51 */           localTypeCode = ORB.init().create_string_tc(0);
/* 52 */           localTypeCode = ORB.init().create_alias_tc(IstringHelper.id(), "Istring", localTypeCode);
/* 53 */           arrayOfStructMember[1] = new StructMember("kind", localTypeCode, null);
/*    */ 
/* 57 */           __typeCode = ORB.init().create_struct_tc(id(), "NameComponent", arrayOfStructMember);
/* 58 */           __active = false;
/*    */         }
/*    */       }
/*    */     }
/* 62 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 67 */     return _id;
/*    */   }
/*    */ 
/*    */   public static NameComponent read(InputStream paramInputStream)
/*    */   {
/* 72 */     NameComponent localNameComponent = new NameComponent();
/* 73 */     localNameComponent.id = paramInputStream.read_string();
/* 74 */     localNameComponent.kind = paramInputStream.read_string();
/* 75 */     return localNameComponent;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, NameComponent paramNameComponent)
/*    */   {
/* 80 */     paramOutputStream.write_string(paramNameComponent.id);
/* 81 */     paramOutputStream.write_string(paramNameComponent.kind);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NameComponentHelper
 * JD-Core Version:    0.6.2
 */
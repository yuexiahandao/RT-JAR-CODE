/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.StructMember;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class BindingHelper
/*    */ {
/* 13 */   private static String _id = "IDL:omg.org/CosNaming/Binding:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/* 29 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, Binding paramBinding)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramBinding);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static Binding extract(Any paramAny)
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
/* 45 */           localTypeCode = NameComponentHelper.type();
/* 46 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 47 */           localTypeCode = ORB.init().create_alias_tc(NameHelper.id(), "Name", localTypeCode);
/* 48 */           arrayOfStructMember[0] = new StructMember("binding_name", localTypeCode, null);
/*    */ 
/* 52 */           localTypeCode = BindingTypeHelper.type();
/* 53 */           arrayOfStructMember[1] = new StructMember("binding_type", localTypeCode, null);
/*    */ 
/* 57 */           __typeCode = ORB.init().create_struct_tc(id(), "Binding", arrayOfStructMember);
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
/*    */   public static Binding read(InputStream paramInputStream)
/*    */   {
/* 72 */     Binding localBinding = new Binding();
/* 73 */     localBinding.binding_name = NameHelper.read(paramInputStream);
/* 74 */     localBinding.binding_type = BindingTypeHelper.read(paramInputStream);
/* 75 */     return localBinding;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, Binding paramBinding)
/*    */   {
/* 80 */     NameHelper.write(paramOutputStream, paramBinding.binding_name);
/* 81 */     BindingTypeHelper.write(paramOutputStream, paramBinding.binding_type);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.BindingHelper
 * JD-Core Version:    0.6.2
 */
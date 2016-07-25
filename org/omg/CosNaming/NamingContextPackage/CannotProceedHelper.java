/*    */ package org.omg.CosNaming.NamingContextPackage;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.StructMember;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CosNaming.NameComponentHelper;
/*    */ import org.omg.CosNaming.NameHelper;
/*    */ import org.omg.CosNaming.NamingContextHelper;
/*    */ 
/*    */ public abstract class CannotProceedHelper
/*    */ {
/* 13 */   private static String _id = "IDL:omg.org/CosNaming/NamingContext/CannotProceed:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/* 29 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, CannotProceed paramCannotProceed)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramCannotProceed);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static CannotProceed extract(Any paramAny)
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
/* 45 */           localTypeCode = NamingContextHelper.type();
/* 46 */           arrayOfStructMember[0] = new StructMember("cxt", localTypeCode, null);
/*    */ 
/* 50 */           localTypeCode = NameComponentHelper.type();
/* 51 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 52 */           localTypeCode = ORB.init().create_alias_tc(NameHelper.id(), "Name", localTypeCode);
/* 53 */           arrayOfStructMember[1] = new StructMember("rest_of_name", localTypeCode, null);
/*    */ 
/* 57 */           __typeCode = ORB.init().create_exception_tc(id(), "CannotProceed", arrayOfStructMember);
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
/*    */   public static CannotProceed read(InputStream paramInputStream)
/*    */   {
/* 72 */     CannotProceed localCannotProceed = new CannotProceed();
/*    */ 
/* 74 */     paramInputStream.read_string();
/* 75 */     localCannotProceed.cxt = NamingContextHelper.read(paramInputStream);
/* 76 */     localCannotProceed.rest_of_name = NameHelper.read(paramInputStream);
/* 77 */     return localCannotProceed;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, CannotProceed paramCannotProceed)
/*    */   {
/* 83 */     paramOutputStream.write_string(id());
/* 84 */     NamingContextHelper.write(paramOutputStream, paramCannotProceed.cxt);
/* 85 */     NameHelper.write(paramOutputStream, paramCannotProceed.rest_of_name);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextPackage.CannotProceedHelper
 * JD-Core Version:    0.6.2
 */
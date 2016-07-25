/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class WrongTransactionHelper
/*    */ {
/* 39 */   private static String _id = "IDL:omg.org/CORBA/WrongTransaction:1.0";
/*    */ 
/* 54 */   private static TypeCode __typeCode = null;
/* 55 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, WrongTransaction paramWrongTransaction)
/*    */   {
/* 43 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 44 */     paramAny.type(type());
/* 45 */     write(localOutputStream, paramWrongTransaction);
/* 46 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static WrongTransaction extract(Any paramAny)
/*    */   {
/* 51 */     return read(paramAny.create_input_stream());
/*    */   }
/*    */ 
/*    */   public static synchronized TypeCode type()
/*    */   {
/* 58 */     if (__typeCode == null)
/*    */     {
/* 60 */       synchronized (TypeCode.class)
/*    */       {
/* 62 */         if (__typeCode == null)
/*    */         {
/* 64 */           if (__active)
/*    */           {
/* 66 */             return ORB.init().create_recursive_tc(_id);
/*    */           }
/* 68 */           __active = true;
/* 69 */           StructMember[] arrayOfStructMember = new StructMember[0];
/* 70 */           Object localObject1 = null;
/* 71 */           __typeCode = ORB.init().create_exception_tc(id(), "WrongTransaction", arrayOfStructMember);
/* 72 */           __active = false;
/*    */         }
/*    */       }
/*    */     }
/* 76 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 81 */     return _id;
/*    */   }
/*    */ 
/*    */   public static WrongTransaction read(InputStream paramInputStream)
/*    */   {
/* 86 */     WrongTransaction localWrongTransaction = new WrongTransaction();
/*    */ 
/* 88 */     paramInputStream.read_string();
/* 89 */     return localWrongTransaction;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, WrongTransaction paramWrongTransaction)
/*    */   {
/* 95 */     paramOutputStream.write_string(id());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.WrongTransactionHelper
 * JD-Core Version:    0.6.2
 */
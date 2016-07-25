/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.StructMember;
/*    */ import org.omg.CORBA.TCKind;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ServerAlreadyInstalledHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/ServerAlreadyInstalled:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/* 29 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, ServerAlreadyInstalled paramServerAlreadyInstalled)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramServerAlreadyInstalled);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static ServerAlreadyInstalled extract(Any paramAny)
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
/* 43 */           StructMember[] arrayOfStructMember = new StructMember[1];
/* 44 */           TypeCode localTypeCode = null;
/* 45 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_long);
/* 46 */           localTypeCode = ORB.init().create_alias_tc(ServerIdHelper.id(), "ServerId", localTypeCode);
/* 47 */           arrayOfStructMember[0] = new StructMember("serverId", localTypeCode, null);
/*    */ 
/* 51 */           __typeCode = ORB.init().create_exception_tc(id(), "ServerAlreadyInstalled", arrayOfStructMember);
/* 52 */           __active = false;
/*    */         }
/*    */       }
/*    */     }
/* 56 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 61 */     return _id;
/*    */   }
/*    */ 
/*    */   public static ServerAlreadyInstalled read(InputStream paramInputStream)
/*    */   {
/* 66 */     ServerAlreadyInstalled localServerAlreadyInstalled = new ServerAlreadyInstalled();
/*    */ 
/* 68 */     paramInputStream.read_string();
/* 69 */     localServerAlreadyInstalled.serverId = paramInputStream.read_long();
/* 70 */     return localServerAlreadyInstalled;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, ServerAlreadyInstalled paramServerAlreadyInstalled)
/*    */   {
/* 76 */     paramOutputStream.write_string(id());
/* 77 */     paramOutputStream.write_long(paramServerAlreadyInstalled.serverId);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.ServerAlreadyInstalledHelper
 * JD-Core Version:    0.6.2
 */
/*    */ package com.sun.corba.se.spi.activation.LocatorPackage;
/*    */ 
/*    */ import com.sun.corba.se.spi.activation.ORBPortInfoHelper;
/*    */ import com.sun.corba.se.spi.activation.ORBPortInfoListHelper;
/*    */ import org.omg.CORBA.Any;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.StructMember;
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ 
/*    */ public abstract class ServerLocationHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/Locator/ServerLocation:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/* 29 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, ServerLocation paramServerLocation)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramServerLocation);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static ServerLocation extract(Any paramAny)
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
/* 46 */           arrayOfStructMember[0] = new StructMember("hostname", localTypeCode, null);
/*    */ 
/* 50 */           localTypeCode = ORBPortInfoHelper.type();
/* 51 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 52 */           localTypeCode = ORB.init().create_alias_tc(ORBPortInfoListHelper.id(), "ORBPortInfoList", localTypeCode);
/* 53 */           arrayOfStructMember[1] = new StructMember("ports", localTypeCode, null);
/*    */ 
/* 57 */           __typeCode = ORB.init().create_struct_tc(id(), "ServerLocation", arrayOfStructMember);
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
/*    */   public static ServerLocation read(InputStream paramInputStream)
/*    */   {
/* 72 */     ServerLocation localServerLocation = new ServerLocation();
/* 73 */     localServerLocation.hostname = paramInputStream.read_string();
/* 74 */     localServerLocation.ports = ORBPortInfoListHelper.read(paramInputStream);
/* 75 */     return localServerLocation;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, ServerLocation paramServerLocation)
/*    */   {
/* 80 */     paramOutputStream.write_string(paramServerLocation.hostname);
/* 81 */     ORBPortInfoListHelper.write(paramOutputStream, paramServerLocation.ports);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper
 * JD-Core Version:    0.6.2
 */
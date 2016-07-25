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
/*    */ public abstract class EndPointInfoHelper
/*    */ {
/* 13 */   private static String _id = "IDL:activation/EndPointInfo:1.0";
/*    */ 
/* 28 */   private static TypeCode __typeCode = null;
/* 29 */   private static boolean __active = false;
/*    */ 
/*    */   public static void insert(Any paramAny, EndPointInfo paramEndPointInfo)
/*    */   {
/* 17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 18 */     paramAny.type(type());
/* 19 */     write(localOutputStream, paramEndPointInfo);
/* 20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*    */   }
/*    */ 
/*    */   public static EndPointInfo extract(Any paramAny)
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
/* 46 */           arrayOfStructMember[0] = new StructMember("endpointType", localTypeCode, null);
/*    */ 
/* 50 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_long);
/* 51 */           localTypeCode = ORB.init().create_alias_tc(TCPPortHelper.id(), "TCPPort", localTypeCode);
/* 52 */           arrayOfStructMember[1] = new StructMember("port", localTypeCode, null);
/*    */ 
/* 56 */           __typeCode = ORB.init().create_struct_tc(id(), "EndPointInfo", arrayOfStructMember);
/* 57 */           __active = false;
/*    */         }
/*    */       }
/*    */     }
/* 61 */     return __typeCode;
/*    */   }
/*    */ 
/*    */   public static String id()
/*    */   {
/* 66 */     return _id;
/*    */   }
/*    */ 
/*    */   public static EndPointInfo read(InputStream paramInputStream)
/*    */   {
/* 71 */     EndPointInfo localEndPointInfo = new EndPointInfo();
/* 72 */     localEndPointInfo.endpointType = paramInputStream.read_string();
/* 73 */     localEndPointInfo.port = paramInputStream.read_long();
/* 74 */     return localEndPointInfo;
/*    */   }
/*    */ 
/*    */   public static void write(OutputStream paramOutputStream, EndPointInfo paramEndPointInfo)
/*    */   {
/* 79 */     paramOutputStream.write_string(paramEndPointInfo.endpointType);
/* 80 */     paramOutputStream.write_long(paramEndPointInfo.port);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.EndPointInfoHelper
 * JD-Core Version:    0.6.2
 */
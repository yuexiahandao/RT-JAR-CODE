/*     */ package com.sun.corba.se.spi.activation.RepositoryPackage;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.StructMember;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public abstract class ServerDefHelper
/*     */ {
/*  13 */   private static String _id = "IDL:activation/Repository/ServerDef:1.0";
/*     */ 
/*  28 */   private static TypeCode __typeCode = null;
/*  29 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, ServerDef paramServerDef)
/*     */   {
/*  17 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  18 */     paramAny.type(type());
/*  19 */     write(localOutputStream, paramServerDef);
/*  20 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static ServerDef extract(Any paramAny)
/*     */   {
/*  25 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  32 */     if (__typeCode == null)
/*     */     {
/*  34 */       synchronized (TypeCode.class)
/*     */       {
/*  36 */         if (__typeCode == null)
/*     */         {
/*  38 */           if (__active)
/*     */           {
/*  40 */             return ORB.init().create_recursive_tc(_id);
/*     */           }
/*  42 */           __active = true;
/*  43 */           StructMember[] arrayOfStructMember = new StructMember[5];
/*  44 */           TypeCode localTypeCode = null;
/*  45 */           localTypeCode = ORB.init().create_string_tc(0);
/*  46 */           arrayOfStructMember[0] = new StructMember("applicationName", localTypeCode, null);
/*     */ 
/*  50 */           localTypeCode = ORB.init().create_string_tc(0);
/*  51 */           arrayOfStructMember[1] = new StructMember("serverName", localTypeCode, null);
/*     */ 
/*  55 */           localTypeCode = ORB.init().create_string_tc(0);
/*  56 */           arrayOfStructMember[2] = new StructMember("serverClassPath", localTypeCode, null);
/*     */ 
/*  60 */           localTypeCode = ORB.init().create_string_tc(0);
/*  61 */           arrayOfStructMember[3] = new StructMember("serverArgs", localTypeCode, null);
/*     */ 
/*  65 */           localTypeCode = ORB.init().create_string_tc(0);
/*  66 */           arrayOfStructMember[4] = new StructMember("serverVmArgs", localTypeCode, null);
/*     */ 
/*  70 */           __typeCode = ORB.init().create_struct_tc(id(), "ServerDef", arrayOfStructMember);
/*  71 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/*  75 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  80 */     return _id;
/*     */   }
/*     */ 
/*     */   public static ServerDef read(InputStream paramInputStream)
/*     */   {
/*  85 */     ServerDef localServerDef = new ServerDef();
/*  86 */     localServerDef.applicationName = paramInputStream.read_string();
/*  87 */     localServerDef.serverName = paramInputStream.read_string();
/*  88 */     localServerDef.serverClassPath = paramInputStream.read_string();
/*  89 */     localServerDef.serverArgs = paramInputStream.read_string();
/*  90 */     localServerDef.serverVmArgs = paramInputStream.read_string();
/*  91 */     return localServerDef;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, ServerDef paramServerDef)
/*     */   {
/*  96 */     paramOutputStream.write_string(paramServerDef.applicationName);
/*  97 */     paramOutputStream.write_string(paramServerDef.serverName);
/*  98 */     paramOutputStream.write_string(paramServerDef.serverClassPath);
/*  99 */     paramOutputStream.write_string(paramServerDef.serverArgs);
/* 100 */     paramOutputStream.write_string(paramServerDef.serverVmArgs);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.RepositoryPackage.ServerDefHelper
 * JD-Core Version:    0.6.2
 */
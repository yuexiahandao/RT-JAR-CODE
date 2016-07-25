/*     */ package com.sun.org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.StructMember;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class InitializerHelper
/*     */ {
/*  37 */   private static String _id = "IDL:omg.org/CORBA/Initializer:1.0";
/*     */ 
/*  56 */   private static TypeCode __typeCode = null;
/*  57 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, Initializer paramInitializer)
/*     */   {
/*  45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  46 */     paramAny.type(type());
/*  47 */     write(localOutputStream, paramInitializer);
/*  48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static Initializer extract(Any paramAny)
/*     */   {
/*  53 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  60 */     if (__typeCode == null)
/*     */     {
/*  62 */       synchronized (TypeCode.class)
/*     */       {
/*  64 */         if (__typeCode == null)
/*     */         {
/*  66 */           if (__active)
/*     */           {
/*  68 */             return ORB.init().create_recursive_tc(_id);
/*     */           }
/*  70 */           __active = true;
/*  71 */           StructMember[] arrayOfStructMember = new StructMember[2];
/*  72 */           TypeCode localTypeCode = null;
/*  73 */           localTypeCode = StructMemberHelper.type();
/*  74 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/*  75 */           localTypeCode = ORB.init().create_alias_tc(StructMemberSeqHelper.id(), "StructMemberSeq", localTypeCode);
/*  76 */           arrayOfStructMember[0] = new StructMember("members", localTypeCode, null);
/*     */ 
/*  80 */           localTypeCode = ORB.init().create_string_tc(0);
/*  81 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  82 */           arrayOfStructMember[1] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  86 */           __typeCode = ORB.init().create_struct_tc(id(), "Initializer", arrayOfStructMember);
/*  87 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/*  91 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  96 */     return _id;
/*     */   }
/*     */ 
/*     */   public static Initializer read(InputStream paramInputStream)
/*     */   {
/* 101 */     Initializer localInitializer = new Initializer();
/* 102 */     localInitializer.members = StructMemberSeqHelper.read(paramInputStream);
/* 103 */     localInitializer.name = paramInputStream.read_string();
/* 104 */     return localInitializer;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, Initializer paramInitializer)
/*     */   {
/* 109 */     StructMemberSeqHelper.write(paramOutputStream, paramInitializer.members);
/* 110 */     paramOutputStream.write_string(paramInitializer.name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.InitializerHelper
 * JD-Core Version:    0.6.2
 */
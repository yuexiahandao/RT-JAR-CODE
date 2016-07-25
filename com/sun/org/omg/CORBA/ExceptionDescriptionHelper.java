/*     */ package com.sun.org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.StructMember;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class ExceptionDescriptionHelper
/*     */ {
/*  37 */   private static String _id = "IDL:omg.org/CORBA/ExceptionDescription:1.0";
/*     */ 
/*  56 */   private static TypeCode __typeCode = null;
/*  57 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, ExceptionDescription paramExceptionDescription)
/*     */   {
/*  45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  46 */     paramAny.type(type());
/*  47 */     write(localOutputStream, paramExceptionDescription);
/*  48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static ExceptionDescription extract(Any paramAny)
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
/*  71 */           StructMember[] arrayOfStructMember = new StructMember[5];
/*  72 */           TypeCode localTypeCode = null;
/*  73 */           localTypeCode = ORB.init().create_string_tc(0);
/*  74 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  75 */           arrayOfStructMember[0] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  79 */           localTypeCode = ORB.init().create_string_tc(0);
/*  80 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/*  81 */           arrayOfStructMember[1] = new StructMember("id", localTypeCode, null);
/*     */ 
/*  85 */           localTypeCode = ORB.init().create_string_tc(0);
/*  86 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/*  87 */           arrayOfStructMember[2] = new StructMember("defined_in", localTypeCode, null);
/*     */ 
/*  91 */           localTypeCode = ORB.init().create_string_tc(0);
/*  92 */           localTypeCode = ORB.init().create_alias_tc(VersionSpecHelper.id(), "VersionSpec", localTypeCode);
/*  93 */           arrayOfStructMember[3] = new StructMember("version", localTypeCode, null);
/*     */ 
/*  97 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/*  98 */           arrayOfStructMember[4] = new StructMember("type", localTypeCode, null);
/*     */ 
/* 102 */           __typeCode = ORB.init().create_struct_tc(id(), "ExceptionDescription", arrayOfStructMember);
/* 103 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/* 107 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 112 */     return _id;
/*     */   }
/*     */ 
/*     */   public static ExceptionDescription read(InputStream paramInputStream)
/*     */   {
/* 117 */     ExceptionDescription localExceptionDescription = new ExceptionDescription();
/* 118 */     localExceptionDescription.name = paramInputStream.read_string();
/* 119 */     localExceptionDescription.id = paramInputStream.read_string();
/* 120 */     localExceptionDescription.defined_in = paramInputStream.read_string();
/* 121 */     localExceptionDescription.version = paramInputStream.read_string();
/* 122 */     localExceptionDescription.type = paramInputStream.read_TypeCode();
/* 123 */     return localExceptionDescription;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, ExceptionDescription paramExceptionDescription)
/*     */   {
/* 128 */     paramOutputStream.write_string(paramExceptionDescription.name);
/* 129 */     paramOutputStream.write_string(paramExceptionDescription.id);
/* 130 */     paramOutputStream.write_string(paramExceptionDescription.defined_in);
/* 131 */     paramOutputStream.write_string(paramExceptionDescription.version);
/* 132 */     paramOutputStream.write_TypeCode(paramExceptionDescription.type);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ExceptionDescriptionHelper
 * JD-Core Version:    0.6.2
 */
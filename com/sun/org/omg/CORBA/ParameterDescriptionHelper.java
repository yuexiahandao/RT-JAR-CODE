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
/*     */ public final class ParameterDescriptionHelper
/*     */ {
/*  37 */   private static String _id = "IDL:omg.org/CORBA/ParameterDescription:1.0";
/*     */ 
/*  56 */   private static TypeCode __typeCode = null;
/*  57 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, ParameterDescription paramParameterDescription)
/*     */   {
/*  45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  46 */     paramAny.type(type());
/*  47 */     write(localOutputStream, paramParameterDescription);
/*  48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static ParameterDescription extract(Any paramAny)
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
/*  71 */           StructMember[] arrayOfStructMember = new StructMember[4];
/*  72 */           TypeCode localTypeCode = null;
/*  73 */           localTypeCode = ORB.init().create_string_tc(0);
/*  74 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  75 */           arrayOfStructMember[0] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  79 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/*  80 */           arrayOfStructMember[1] = new StructMember("type", localTypeCode, null);
/*     */ 
/*  84 */           localTypeCode = IDLTypeHelper.type();
/*  85 */           arrayOfStructMember[2] = new StructMember("type_def", localTypeCode, null);
/*     */ 
/*  89 */           localTypeCode = ParameterModeHelper.type();
/*  90 */           arrayOfStructMember[3] = new StructMember("mode", localTypeCode, null);
/*     */ 
/*  94 */           __typeCode = ORB.init().create_struct_tc(id(), "ParameterDescription", arrayOfStructMember);
/*  95 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/*  99 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 104 */     return _id;
/*     */   }
/*     */ 
/*     */   public static ParameterDescription read(InputStream paramInputStream)
/*     */   {
/* 109 */     ParameterDescription localParameterDescription = new ParameterDescription();
/* 110 */     localParameterDescription.name = paramInputStream.read_string();
/* 111 */     localParameterDescription.type = paramInputStream.read_TypeCode();
/* 112 */     localParameterDescription.type_def = IDLTypeHelper.read(paramInputStream);
/* 113 */     localParameterDescription.mode = ParameterModeHelper.read(paramInputStream);
/* 114 */     return localParameterDescription;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, ParameterDescription paramParameterDescription)
/*     */   {
/* 119 */     paramOutputStream.write_string(paramParameterDescription.name);
/* 120 */     paramOutputStream.write_TypeCode(paramParameterDescription.type);
/* 121 */     IDLTypeHelper.write(paramOutputStream, paramParameterDescription.type_def);
/* 122 */     ParameterModeHelper.write(paramOutputStream, paramParameterDescription.mode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ParameterDescriptionHelper
 * JD-Core Version:    0.6.2
 */
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
/*     */ public final class OperationDescriptionHelper
/*     */ {
/*  37 */   private static String _id = "IDL:omg.org/CORBA/OperationDescription:1.0";
/*     */ 
/*  56 */   private static TypeCode __typeCode = null;
/*  57 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, OperationDescription paramOperationDescription)
/*     */   {
/*  45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  46 */     paramAny.type(type());
/*  47 */     write(localOutputStream, paramOperationDescription);
/*  48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static OperationDescription extract(Any paramAny)
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
/*  71 */           StructMember[] arrayOfStructMember = new StructMember[9];
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
/*  98 */           arrayOfStructMember[4] = new StructMember("result", localTypeCode, null);
/*     */ 
/* 102 */           localTypeCode = OperationModeHelper.type();
/* 103 */           arrayOfStructMember[5] = new StructMember("mode", localTypeCode, null);
/*     */ 
/* 107 */           localTypeCode = ORB.init().create_string_tc(0);
/* 108 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/* 109 */           localTypeCode = ORB.init().create_alias_tc(ContextIdentifierHelper.id(), "ContextIdentifier", localTypeCode);
/* 110 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 111 */           localTypeCode = ORB.init().create_alias_tc(ContextIdSeqHelper.id(), "ContextIdSeq", localTypeCode);
/* 112 */           arrayOfStructMember[6] = new StructMember("contexts", localTypeCode, null);
/*     */ 
/* 116 */           localTypeCode = ParameterDescriptionHelper.type();
/* 117 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 118 */           localTypeCode = ORB.init().create_alias_tc(ParDescriptionSeqHelper.id(), "ParDescriptionSeq", localTypeCode);
/* 119 */           arrayOfStructMember[7] = new StructMember("parameters", localTypeCode, null);
/*     */ 
/* 123 */           localTypeCode = ExceptionDescriptionHelper.type();
/* 124 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 125 */           localTypeCode = ORB.init().create_alias_tc(ExcDescriptionSeqHelper.id(), "ExcDescriptionSeq", localTypeCode);
/* 126 */           arrayOfStructMember[8] = new StructMember("exceptions", localTypeCode, null);
/*     */ 
/* 130 */           __typeCode = ORB.init().create_struct_tc(id(), "OperationDescription", arrayOfStructMember);
/* 131 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/* 135 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 140 */     return _id;
/*     */   }
/*     */ 
/*     */   public static OperationDescription read(InputStream paramInputStream)
/*     */   {
/* 145 */     OperationDescription localOperationDescription = new OperationDescription();
/* 146 */     localOperationDescription.name = paramInputStream.read_string();
/* 147 */     localOperationDescription.id = paramInputStream.read_string();
/* 148 */     localOperationDescription.defined_in = paramInputStream.read_string();
/* 149 */     localOperationDescription.version = paramInputStream.read_string();
/* 150 */     localOperationDescription.result = paramInputStream.read_TypeCode();
/* 151 */     localOperationDescription.mode = OperationModeHelper.read(paramInputStream);
/* 152 */     localOperationDescription.contexts = ContextIdSeqHelper.read(paramInputStream);
/* 153 */     localOperationDescription.parameters = ParDescriptionSeqHelper.read(paramInputStream);
/* 154 */     localOperationDescription.exceptions = ExcDescriptionSeqHelper.read(paramInputStream);
/* 155 */     return localOperationDescription;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, OperationDescription paramOperationDescription)
/*     */   {
/* 160 */     paramOutputStream.write_string(paramOperationDescription.name);
/* 161 */     paramOutputStream.write_string(paramOperationDescription.id);
/* 162 */     paramOutputStream.write_string(paramOperationDescription.defined_in);
/* 163 */     paramOutputStream.write_string(paramOperationDescription.version);
/* 164 */     paramOutputStream.write_TypeCode(paramOperationDescription.result);
/* 165 */     OperationModeHelper.write(paramOutputStream, paramOperationDescription.mode);
/* 166 */     ContextIdSeqHelper.write(paramOutputStream, paramOperationDescription.contexts);
/* 167 */     ParDescriptionSeqHelper.write(paramOutputStream, paramOperationDescription.parameters);
/* 168 */     ExcDescriptionSeqHelper.write(paramOutputStream, paramOperationDescription.exceptions);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.OperationDescriptionHelper
 * JD-Core Version:    0.6.2
 */
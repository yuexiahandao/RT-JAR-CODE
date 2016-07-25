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
/*     */ public final class AttributeDescriptionHelper
/*     */ {
/*  37 */   private static String _id = "IDL:omg.org/CORBA/AttributeDescription:1.0";
/*     */ 
/*  56 */   private static TypeCode __typeCode = null;
/*  57 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, AttributeDescription paramAttributeDescription)
/*     */   {
/*  45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  46 */     paramAny.type(type());
/*  47 */     write(localOutputStream, paramAttributeDescription);
/*  48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static AttributeDescription extract(Any paramAny)
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
/*  71 */           StructMember[] arrayOfStructMember = new StructMember[6];
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
/* 102 */           localTypeCode = AttributeModeHelper.type();
/* 103 */           arrayOfStructMember[5] = new StructMember("mode", localTypeCode, null);
/*     */ 
/* 107 */           __typeCode = ORB.init().create_struct_tc(id(), "AttributeDescription", arrayOfStructMember);
/* 108 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/* 112 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 117 */     return _id;
/*     */   }
/*     */ 
/*     */   public static AttributeDescription read(InputStream paramInputStream)
/*     */   {
/* 122 */     AttributeDescription localAttributeDescription = new AttributeDescription();
/* 123 */     localAttributeDescription.name = paramInputStream.read_string();
/* 124 */     localAttributeDescription.id = paramInputStream.read_string();
/* 125 */     localAttributeDescription.defined_in = paramInputStream.read_string();
/* 126 */     localAttributeDescription.version = paramInputStream.read_string();
/* 127 */     localAttributeDescription.type = paramInputStream.read_TypeCode();
/* 128 */     localAttributeDescription.mode = AttributeModeHelper.read(paramInputStream);
/* 129 */     return localAttributeDescription;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, AttributeDescription paramAttributeDescription)
/*     */   {
/* 134 */     paramOutputStream.write_string(paramAttributeDescription.name);
/* 135 */     paramOutputStream.write_string(paramAttributeDescription.id);
/* 136 */     paramOutputStream.write_string(paramAttributeDescription.defined_in);
/* 137 */     paramOutputStream.write_string(paramAttributeDescription.version);
/* 138 */     paramOutputStream.write_TypeCode(paramAttributeDescription.type);
/* 139 */     AttributeModeHelper.write(paramOutputStream, paramAttributeDescription.mode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.AttributeDescriptionHelper
 * JD-Core Version:    0.6.2
 */
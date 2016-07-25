/*     */ package com.sun.org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.StructMember;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.ValueMember;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class ValueMemberHelper
/*     */ {
/*  39 */   private static String _id = "IDL:omg.org/CORBA/ValueMember:1.0";
/*     */ 
/*  62 */   private static TypeCode __typeCode = null;
/*  63 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, ValueMember paramValueMember)
/*     */   {
/*  49 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  50 */     paramAny.type(type());
/*  51 */     write(localOutputStream, paramValueMember);
/*  52 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static ValueMember extract(Any paramAny)
/*     */   {
/*  59 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  66 */     if (__typeCode == null)
/*     */     {
/*  68 */       synchronized (TypeCode.class)
/*     */       {
/*  70 */         if (__typeCode == null)
/*     */         {
/*  72 */           if (__active)
/*     */           {
/*  74 */             return ORB.init().create_recursive_tc(_id);
/*     */           }
/*  76 */           __active = true;
/*  77 */           StructMember[] arrayOfStructMember = new StructMember[7];
/*  78 */           TypeCode localTypeCode = null;
/*  79 */           localTypeCode = ORB.init().create_string_tc(0);
/*  80 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  81 */           arrayOfStructMember[0] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  85 */           localTypeCode = ORB.init().create_string_tc(0);
/*  86 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/*  87 */           arrayOfStructMember[1] = new StructMember("id", localTypeCode, null);
/*     */ 
/*  91 */           localTypeCode = ORB.init().create_string_tc(0);
/*  92 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/*  93 */           arrayOfStructMember[2] = new StructMember("defined_in", localTypeCode, null);
/*     */ 
/*  97 */           localTypeCode = ORB.init().create_string_tc(0);
/*  98 */           localTypeCode = ORB.init().create_alias_tc(VersionSpecHelper.id(), "VersionSpec", localTypeCode);
/*  99 */           arrayOfStructMember[3] = new StructMember("version", localTypeCode, null);
/*     */ 
/* 103 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/* 104 */           arrayOfStructMember[4] = new StructMember("type", localTypeCode, null);
/*     */ 
/* 108 */           localTypeCode = IDLTypeHelper.type();
/* 109 */           arrayOfStructMember[5] = new StructMember("type_def", localTypeCode, null);
/*     */ 
/* 113 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_short);
/* 114 */           localTypeCode = ORB.init().create_alias_tc(VisibilityHelper.id(), "Visibility", localTypeCode);
/* 115 */           arrayOfStructMember[6] = new StructMember("access", localTypeCode, null);
/*     */ 
/* 119 */           __typeCode = ORB.init().create_struct_tc(id(), "ValueMember", arrayOfStructMember);
/* 120 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/* 124 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 129 */     return _id;
/*     */   }
/*     */ 
/*     */   public static ValueMember read(InputStream paramInputStream)
/*     */   {
/* 138 */     ValueMember localValueMember = new ValueMember();
/* 139 */     localValueMember.name = paramInputStream.read_string();
/* 140 */     localValueMember.id = paramInputStream.read_string();
/* 141 */     localValueMember.defined_in = paramInputStream.read_string();
/* 142 */     localValueMember.version = paramInputStream.read_string();
/* 143 */     localValueMember.type = paramInputStream.read_TypeCode();
/* 144 */     localValueMember.type_def = IDLTypeHelper.read(paramInputStream);
/* 145 */     localValueMember.access = paramInputStream.read_short();
/* 146 */     return localValueMember;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, ValueMember paramValueMember)
/*     */   {
/* 153 */     paramOutputStream.write_string(paramValueMember.name);
/* 154 */     paramOutputStream.write_string(paramValueMember.id);
/* 155 */     paramOutputStream.write_string(paramValueMember.defined_in);
/* 156 */     paramOutputStream.write_string(paramValueMember.version);
/* 157 */     paramOutputStream.write_TypeCode(paramValueMember.type);
/* 158 */     IDLTypeHelper.write(paramOutputStream, paramValueMember.type_def);
/* 159 */     paramOutputStream.write_short(paramValueMember.access);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ValueMemberHelper
 * JD-Core Version:    0.6.2
 */
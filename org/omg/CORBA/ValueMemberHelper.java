/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public abstract class ValueMemberHelper
/*     */ {
/*  40 */   private static String _id = "IDL:omg.org/CORBA/ValueMember:1.0";
/*     */ 
/*  55 */   private static TypeCode __typeCode = null;
/*  56 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, ValueMember paramValueMember)
/*     */   {
/*  44 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  45 */     paramAny.type(type());
/*  46 */     write(localOutputStream, paramValueMember);
/*  47 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static ValueMember extract(Any paramAny)
/*     */   {
/*  52 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  59 */     if (__typeCode == null)
/*     */     {
/*  61 */       synchronized (TypeCode.class)
/*     */       {
/*  63 */         if (__typeCode == null)
/*     */         {
/*  65 */           if (__active)
/*     */           {
/*  67 */             return ORB.init().create_recursive_tc(_id);
/*     */           }
/*  69 */           __active = true;
/*  70 */           StructMember[] arrayOfStructMember = new StructMember[7];
/*  71 */           TypeCode localTypeCode = null;
/*  72 */           localTypeCode = ORB.init().create_string_tc(0);
/*  73 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  74 */           arrayOfStructMember[0] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  78 */           localTypeCode = ORB.init().create_string_tc(0);
/*  79 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/*  80 */           arrayOfStructMember[1] = new StructMember("id", localTypeCode, null);
/*     */ 
/*  84 */           localTypeCode = ORB.init().create_string_tc(0);
/*  85 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/*  86 */           arrayOfStructMember[2] = new StructMember("defined_in", localTypeCode, null);
/*     */ 
/*  90 */           localTypeCode = ORB.init().create_string_tc(0);
/*  91 */           localTypeCode = ORB.init().create_alias_tc(VersionSpecHelper.id(), "VersionSpec", localTypeCode);
/*  92 */           arrayOfStructMember[3] = new StructMember("version", localTypeCode, null);
/*     */ 
/*  96 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/*  97 */           arrayOfStructMember[4] = new StructMember("type", localTypeCode, null);
/*     */ 
/* 101 */           localTypeCode = IDLTypeHelper.type();
/* 102 */           arrayOfStructMember[5] = new StructMember("type_def", localTypeCode, null);
/*     */ 
/* 106 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_short);
/* 107 */           localTypeCode = ORB.init().create_alias_tc(VisibilityHelper.id(), "Visibility", localTypeCode);
/* 108 */           arrayOfStructMember[6] = new StructMember("access", localTypeCode, null);
/*     */ 
/* 112 */           __typeCode = ORB.init().create_struct_tc(id(), "ValueMember", arrayOfStructMember);
/* 113 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/* 117 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 122 */     return _id;
/*     */   }
/*     */ 
/*     */   public static ValueMember read(InputStream paramInputStream)
/*     */   {
/* 127 */     ValueMember localValueMember = new ValueMember();
/* 128 */     localValueMember.name = paramInputStream.read_string();
/* 129 */     localValueMember.id = paramInputStream.read_string();
/* 130 */     localValueMember.defined_in = paramInputStream.read_string();
/* 131 */     localValueMember.version = paramInputStream.read_string();
/* 132 */     localValueMember.type = paramInputStream.read_TypeCode();
/* 133 */     localValueMember.type_def = IDLTypeHelper.read(paramInputStream);
/* 134 */     localValueMember.access = paramInputStream.read_short();
/* 135 */     return localValueMember;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, ValueMember paramValueMember)
/*     */   {
/* 140 */     paramOutputStream.write_string(paramValueMember.name);
/* 141 */     paramOutputStream.write_string(paramValueMember.id);
/* 142 */     paramOutputStream.write_string(paramValueMember.defined_in);
/* 143 */     paramOutputStream.write_string(paramValueMember.version);
/* 144 */     paramOutputStream.write_TypeCode(paramValueMember.type);
/* 145 */     IDLTypeHelper.write(paramOutputStream, paramValueMember.type_def);
/* 146 */     paramOutputStream.write_short(paramValueMember.access);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ValueMemberHelper
 * JD-Core Version:    0.6.2
 */
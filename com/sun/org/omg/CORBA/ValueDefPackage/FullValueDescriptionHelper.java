/*     */ package com.sun.org.omg.CORBA.ValueDefPackage;
/*     */ 
/*     */ import com.sun.org.omg.CORBA.AttrDescriptionSeqHelper;
/*     */ import com.sun.org.omg.CORBA.AttributeDescriptionHelper;
/*     */ import com.sun.org.omg.CORBA.IdentifierHelper;
/*     */ import com.sun.org.omg.CORBA.InitializerHelper;
/*     */ import com.sun.org.omg.CORBA.InitializerSeqHelper;
/*     */ import com.sun.org.omg.CORBA.OpDescriptionSeqHelper;
/*     */ import com.sun.org.omg.CORBA.OperationDescriptionHelper;
/*     */ import com.sun.org.omg.CORBA.RepositoryIdHelper;
/*     */ import com.sun.org.omg.CORBA.RepositoryIdSeqHelper;
/*     */ import com.sun.org.omg.CORBA.ValueMemberHelper;
/*     */ import com.sun.org.omg.CORBA.ValueMemberSeqHelper;
/*     */ import com.sun.org.omg.CORBA.VersionSpecHelper;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.StructMember;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class FullValueDescriptionHelper
/*     */ {
/*  37 */   private static String _id = "IDL:omg.org/CORBA/ValueDef/FullValueDescription:1.0";
/*     */ 
/*  56 */   private static TypeCode __typeCode = null;
/*  57 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, FullValueDescription paramFullValueDescription)
/*     */   {
/*  45 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  46 */     paramAny.type(type());
/*  47 */     write(localOutputStream, paramFullValueDescription);
/*  48 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static FullValueDescription extract(Any paramAny)
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
/*  71 */           StructMember[] arrayOfStructMember = new StructMember[15];
/*  72 */           TypeCode localTypeCode = null;
/*  73 */           localTypeCode = ORB.init().create_string_tc(0);
/*  74 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  75 */           arrayOfStructMember[0] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  79 */           localTypeCode = ORB.init().create_string_tc(0);
/*  80 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/*  81 */           arrayOfStructMember[1] = new StructMember("id", localTypeCode, null);
/*     */ 
/*  85 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_boolean);
/*  86 */           arrayOfStructMember[2] = new StructMember("is_abstract", localTypeCode, null);
/*     */ 
/*  90 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_boolean);
/*  91 */           arrayOfStructMember[3] = new StructMember("is_custom", localTypeCode, null);
/*     */ 
/*  95 */           localTypeCode = ORB.init().create_string_tc(0);
/*  96 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/*  97 */           arrayOfStructMember[4] = new StructMember("defined_in", localTypeCode, null);
/*     */ 
/* 101 */           localTypeCode = ORB.init().create_string_tc(0);
/* 102 */           localTypeCode = ORB.init().create_alias_tc(VersionSpecHelper.id(), "VersionSpec", localTypeCode);
/* 103 */           arrayOfStructMember[5] = new StructMember("version", localTypeCode, null);
/*     */ 
/* 107 */           localTypeCode = OperationDescriptionHelper.type();
/* 108 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 109 */           localTypeCode = ORB.init().create_alias_tc(OpDescriptionSeqHelper.id(), "OpDescriptionSeq", localTypeCode);
/* 110 */           arrayOfStructMember[6] = new StructMember("operations", localTypeCode, null);
/*     */ 
/* 114 */           localTypeCode = AttributeDescriptionHelper.type();
/* 115 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 116 */           localTypeCode = ORB.init().create_alias_tc(AttrDescriptionSeqHelper.id(), "AttrDescriptionSeq", localTypeCode);
/* 117 */           arrayOfStructMember[7] = new StructMember("attributes", localTypeCode, null);
/*     */ 
/* 121 */           localTypeCode = ValueMemberHelper.type();
/* 122 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 123 */           localTypeCode = ORB.init().create_alias_tc(ValueMemberSeqHelper.id(), "ValueMemberSeq", localTypeCode);
/* 124 */           arrayOfStructMember[8] = new StructMember("members", localTypeCode, null);
/*     */ 
/* 128 */           localTypeCode = InitializerHelper.type();
/* 129 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 130 */           localTypeCode = ORB.init().create_alias_tc(InitializerSeqHelper.id(), "InitializerSeq", localTypeCode);
/* 131 */           arrayOfStructMember[9] = new StructMember("initializers", localTypeCode, null);
/*     */ 
/* 135 */           localTypeCode = ORB.init().create_string_tc(0);
/* 136 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/* 137 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 138 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdSeqHelper.id(), "RepositoryIdSeq", localTypeCode);
/* 139 */           arrayOfStructMember[10] = new StructMember("supported_interfaces", localTypeCode, null);
/*     */ 
/* 143 */           localTypeCode = ORB.init().create_string_tc(0);
/* 144 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/* 145 */           localTypeCode = ORB.init().create_sequence_tc(0, localTypeCode);
/* 146 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdSeqHelper.id(), "RepositoryIdSeq", localTypeCode);
/* 147 */           arrayOfStructMember[11] = new StructMember("abstract_base_values", localTypeCode, null);
/*     */ 
/* 151 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_boolean);
/* 152 */           arrayOfStructMember[12] = new StructMember("is_truncatable", localTypeCode, null);
/*     */ 
/* 156 */           localTypeCode = ORB.init().create_string_tc(0);
/* 157 */           localTypeCode = ORB.init().create_alias_tc(RepositoryIdHelper.id(), "RepositoryId", localTypeCode);
/* 158 */           arrayOfStructMember[13] = new StructMember("base_value", localTypeCode, null);
/*     */ 
/* 162 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/* 163 */           arrayOfStructMember[14] = new StructMember("type", localTypeCode, null);
/*     */ 
/* 167 */           __typeCode = ORB.init().create_struct_tc(id(), "FullValueDescription", arrayOfStructMember);
/* 168 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/* 172 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 177 */     return _id;
/*     */   }
/*     */ 
/*     */   public static FullValueDescription read(InputStream paramInputStream)
/*     */   {
/* 182 */     FullValueDescription localFullValueDescription = new FullValueDescription();
/* 183 */     localFullValueDescription.name = paramInputStream.read_string();
/* 184 */     localFullValueDescription.id = paramInputStream.read_string();
/* 185 */     localFullValueDescription.is_abstract = paramInputStream.read_boolean();
/* 186 */     localFullValueDescription.is_custom = paramInputStream.read_boolean();
/* 187 */     localFullValueDescription.defined_in = paramInputStream.read_string();
/* 188 */     localFullValueDescription.version = paramInputStream.read_string();
/* 189 */     localFullValueDescription.operations = OpDescriptionSeqHelper.read(paramInputStream);
/* 190 */     localFullValueDescription.attributes = AttrDescriptionSeqHelper.read(paramInputStream);
/* 191 */     localFullValueDescription.members = ValueMemberSeqHelper.read(paramInputStream);
/* 192 */     localFullValueDescription.initializers = InitializerSeqHelper.read(paramInputStream);
/* 193 */     localFullValueDescription.supported_interfaces = RepositoryIdSeqHelper.read(paramInputStream);
/* 194 */     localFullValueDescription.abstract_base_values = RepositoryIdSeqHelper.read(paramInputStream);
/* 195 */     localFullValueDescription.is_truncatable = paramInputStream.read_boolean();
/* 196 */     localFullValueDescription.base_value = paramInputStream.read_string();
/* 197 */     localFullValueDescription.type = paramInputStream.read_TypeCode();
/* 198 */     return localFullValueDescription;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, FullValueDescription paramFullValueDescription)
/*     */   {
/* 203 */     paramOutputStream.write_string(paramFullValueDescription.name);
/* 204 */     paramOutputStream.write_string(paramFullValueDescription.id);
/* 205 */     paramOutputStream.write_boolean(paramFullValueDescription.is_abstract);
/* 206 */     paramOutputStream.write_boolean(paramFullValueDescription.is_custom);
/* 207 */     paramOutputStream.write_string(paramFullValueDescription.defined_in);
/* 208 */     paramOutputStream.write_string(paramFullValueDescription.version);
/* 209 */     OpDescriptionSeqHelper.write(paramOutputStream, paramFullValueDescription.operations);
/* 210 */     AttrDescriptionSeqHelper.write(paramOutputStream, paramFullValueDescription.attributes);
/* 211 */     ValueMemberSeqHelper.write(paramOutputStream, paramFullValueDescription.members);
/* 212 */     InitializerSeqHelper.write(paramOutputStream, paramFullValueDescription.initializers);
/* 213 */     RepositoryIdSeqHelper.write(paramOutputStream, paramFullValueDescription.supported_interfaces);
/* 214 */     RepositoryIdSeqHelper.write(paramOutputStream, paramFullValueDescription.abstract_base_values);
/* 215 */     paramOutputStream.write_boolean(paramFullValueDescription.is_truncatable);
/* 216 */     paramOutputStream.write_string(paramFullValueDescription.base_value);
/* 217 */     paramOutputStream.write_TypeCode(paramFullValueDescription.type);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescriptionHelper
 * JD-Core Version:    0.6.2
 */
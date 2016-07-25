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
/*     */ public final class StructMemberHelper
/*     */ {
/*  39 */   private static String _id = "IDL:omg.org/CORBA/StructMember:1.0";
/*     */ 
/*  62 */   private static TypeCode __typeCode = null;
/*  63 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, StructMember paramStructMember)
/*     */   {
/*  49 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  50 */     paramAny.type(type());
/*  51 */     write(localOutputStream, paramStructMember);
/*  52 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static StructMember extract(Any paramAny)
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
/*  77 */           StructMember[] arrayOfStructMember = new StructMember[3];
/*  78 */           TypeCode localTypeCode = null;
/*  79 */           localTypeCode = ORB.init().create_string_tc(0);
/*  80 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  81 */           arrayOfStructMember[0] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  85 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/*  86 */           arrayOfStructMember[1] = new StructMember("type", localTypeCode, null);
/*     */ 
/*  90 */           localTypeCode = IDLTypeHelper.type();
/*  91 */           arrayOfStructMember[2] = new StructMember("type_def", localTypeCode, null);
/*     */ 
/*  95 */           __typeCode = ORB.init().create_struct_tc(id(), "StructMember", arrayOfStructMember);
/*  96 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/* 100 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 105 */     return _id;
/*     */   }
/*     */ 
/*     */   public static StructMember read(InputStream paramInputStream)
/*     */   {
/* 114 */     StructMember localStructMember = new StructMember();
/* 115 */     localStructMember.name = paramInputStream.read_string();
/* 116 */     localStructMember.type = paramInputStream.read_TypeCode();
/* 117 */     localStructMember.type_def = IDLTypeHelper.read(paramInputStream);
/* 118 */     return localStructMember;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, StructMember paramStructMember)
/*     */   {
/* 125 */     paramOutputStream.write_string(paramStructMember.name);
/* 126 */     paramOutputStream.write_TypeCode(paramStructMember.type);
/* 127 */     IDLTypeHelper.write(paramOutputStream, paramStructMember.type_def);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.StructMemberHelper
 * JD-Core Version:    0.6.2
 */
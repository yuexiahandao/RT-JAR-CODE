/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public abstract class UnionMemberHelper
/*     */ {
/*  40 */   private static String _id = "IDL:omg.org/CORBA/UnionMember:1.0";
/*     */ 
/*  55 */   private static TypeCode __typeCode = null;
/*  56 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, UnionMember paramUnionMember)
/*     */   {
/*  44 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  45 */     paramAny.type(type());
/*  46 */     write(localOutputStream, paramUnionMember);
/*  47 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static UnionMember extract(Any paramAny)
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
/*  70 */           StructMember[] arrayOfStructMember = new StructMember[4];
/*  71 */           TypeCode localTypeCode = null;
/*  72 */           localTypeCode = ORB.init().create_string_tc(0);
/*  73 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  74 */           arrayOfStructMember[0] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  78 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_any);
/*  79 */           arrayOfStructMember[1] = new StructMember("label", localTypeCode, null);
/*     */ 
/*  83 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/*  84 */           arrayOfStructMember[2] = new StructMember("type", localTypeCode, null);
/*     */ 
/*  88 */           localTypeCode = IDLTypeHelper.type();
/*  89 */           arrayOfStructMember[3] = new StructMember("type_def", localTypeCode, null);
/*     */ 
/*  93 */           __typeCode = ORB.init().create_struct_tc(id(), "UnionMember", arrayOfStructMember);
/*  94 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/*  98 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 103 */     return _id;
/*     */   }
/*     */ 
/*     */   public static UnionMember read(InputStream paramInputStream)
/*     */   {
/* 108 */     UnionMember localUnionMember = new UnionMember();
/* 109 */     localUnionMember.name = paramInputStream.read_string();
/* 110 */     localUnionMember.label = paramInputStream.read_any();
/* 111 */     localUnionMember.type = paramInputStream.read_TypeCode();
/* 112 */     localUnionMember.type_def = IDLTypeHelper.read(paramInputStream);
/* 113 */     return localUnionMember;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, UnionMember paramUnionMember)
/*     */   {
/* 118 */     paramOutputStream.write_string(paramUnionMember.name);
/* 119 */     paramOutputStream.write_any(paramUnionMember.label);
/* 120 */     paramOutputStream.write_TypeCode(paramUnionMember.type);
/* 121 */     IDLTypeHelper.write(paramOutputStream, paramUnionMember.type_def);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.UnionMemberHelper
 * JD-Core Version:    0.6.2
 */
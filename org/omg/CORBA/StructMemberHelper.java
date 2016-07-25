/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public abstract class StructMemberHelper
/*     */ {
/*  40 */   private static String _id = "IDL:omg.org/CORBA/StructMember:1.0";
/*     */ 
/*  55 */   private static TypeCode __typeCode = null;
/*  56 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, StructMember paramStructMember)
/*     */   {
/*  44 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  45 */     paramAny.type(type());
/*  46 */     write(localOutputStream, paramStructMember);
/*  47 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static StructMember extract(Any paramAny)
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
/*  70 */           StructMember[] arrayOfStructMember = new StructMember[3];
/*  71 */           TypeCode localTypeCode = null;
/*  72 */           localTypeCode = ORB.init().create_string_tc(0);
/*  73 */           localTypeCode = ORB.init().create_alias_tc(IdentifierHelper.id(), "Identifier", localTypeCode);
/*  74 */           arrayOfStructMember[0] = new StructMember("name", localTypeCode, null);
/*     */ 
/*  78 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/*  79 */           arrayOfStructMember[1] = new StructMember("type", localTypeCode, null);
/*     */ 
/*  83 */           localTypeCode = IDLTypeHelper.type();
/*  84 */           arrayOfStructMember[2] = new StructMember("type_def", localTypeCode, null);
/*     */ 
/*  88 */           __typeCode = ORB.init().create_struct_tc(id(), "StructMember", arrayOfStructMember);
/*  89 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/*  93 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  98 */     return _id;
/*     */   }
/*     */ 
/*     */   public static StructMember read(InputStream paramInputStream)
/*     */   {
/* 103 */     StructMember localStructMember = new StructMember();
/* 104 */     localStructMember.name = paramInputStream.read_string();
/* 105 */     localStructMember.type = paramInputStream.read_TypeCode();
/* 106 */     localStructMember.type_def = IDLTypeHelper.read(paramInputStream);
/* 107 */     return localStructMember;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, StructMember paramStructMember)
/*     */   {
/* 112 */     paramOutputStream.write_string(paramStructMember.name);
/* 113 */     paramOutputStream.write_TypeCode(paramStructMember.type);
/* 114 */     IDLTypeHelper.write(paramOutputStream, paramStructMember.type_def);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.StructMemberHelper
 * JD-Core Version:    0.6.2
 */
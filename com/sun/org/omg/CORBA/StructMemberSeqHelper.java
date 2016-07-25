/*     */ package com.sun.org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.StructMember;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class StructMemberSeqHelper
/*     */ {
/*  39 */   private static String _id = "IDL:omg.org/CORBA/StructMemberSeq:1.0";
/*     */ 
/*  62 */   private static TypeCode __typeCode = null;
/*     */ 
/*     */   public static void insert(Any paramAny, StructMember[] paramArrayOfStructMember)
/*     */   {
/*  49 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  50 */     paramAny.type(type());
/*  51 */     write(localOutputStream, paramArrayOfStructMember);
/*  52 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static StructMember[] extract(Any paramAny)
/*     */   {
/*  59 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  65 */     if (__typeCode == null)
/*     */     {
/*  67 */       __typeCode = StructMemberHelper.type();
/*  68 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/*  69 */       __typeCode = ORB.init().create_alias_tc(id(), "StructMemberSeq", __typeCode);
/*     */     }
/*  71 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  76 */     return _id;
/*     */   }
/*     */ 
/*     */   public static StructMember[] read(InputStream paramInputStream)
/*     */   {
/*  85 */     StructMember[] arrayOfStructMember = null;
/*  86 */     int i = paramInputStream.read_long();
/*     */ 
/*  89 */     arrayOfStructMember = new StructMember[i];
/*  90 */     for (int j = 0; j < arrayOfStructMember.length; j++)
/*  91 */       arrayOfStructMember[j] = StructMemberHelper.read(paramInputStream);
/*  92 */     return arrayOfStructMember;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, StructMember[] paramArrayOfStructMember)
/*     */   {
/*  99 */     paramOutputStream.write_long(paramArrayOfStructMember.length);
/* 100 */     for (int i = 0; i < paramArrayOfStructMember.length; i++)
/* 101 */       StructMemberHelper.write(paramOutputStream, paramArrayOfStructMember[i]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.StructMemberSeqHelper
 * JD-Core Version:    0.6.2
 */
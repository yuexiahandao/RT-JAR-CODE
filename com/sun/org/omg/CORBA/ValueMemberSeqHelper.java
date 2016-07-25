/*     */ package com.sun.org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.ValueMember;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class ValueMemberSeqHelper
/*     */ {
/*  39 */   private static String _id = "IDL:omg.org/CORBA/ValueMemberSeq:1.0";
/*     */ 
/*  62 */   private static TypeCode __typeCode = null;
/*     */ 
/*     */   public static void insert(Any paramAny, ValueMember[] paramArrayOfValueMember)
/*     */   {
/*  49 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  50 */     paramAny.type(type());
/*  51 */     write(localOutputStream, paramArrayOfValueMember);
/*  52 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static ValueMember[] extract(Any paramAny)
/*     */   {
/*  59 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  65 */     if (__typeCode == null)
/*     */     {
/*  67 */       __typeCode = ValueMemberHelper.type();
/*  68 */       __typeCode = ORB.init().create_sequence_tc(0, __typeCode);
/*  69 */       __typeCode = ORB.init().create_alias_tc(id(), "ValueMemberSeq", __typeCode);
/*     */     }
/*  71 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  76 */     return _id;
/*     */   }
/*     */ 
/*     */   public static ValueMember[] read(InputStream paramInputStream)
/*     */   {
/*  85 */     ValueMember[] arrayOfValueMember = null;
/*  86 */     int i = paramInputStream.read_long();
/*     */ 
/*  89 */     arrayOfValueMember = new ValueMember[i];
/*  90 */     for (int j = 0; j < arrayOfValueMember.length; j++)
/*  91 */       arrayOfValueMember[j] = ValueMemberHelper.read(paramInputStream);
/*  92 */     return arrayOfValueMember;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, ValueMember[] paramArrayOfValueMember)
/*     */   {
/*  99 */     paramOutputStream.write_long(paramArrayOfValueMember.length);
/* 100 */     for (int i = 0; i < paramArrayOfValueMember.length; i++)
/* 101 */       ValueMemberHelper.write(paramOutputStream, paramArrayOfValueMember[i]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.ValueMemberSeqHelper
 * JD-Core Version:    0.6.2
 */
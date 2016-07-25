/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public abstract class NameValuePairHelper
/*     */ {
/*  39 */   private static String _id = "IDL:omg.org/CORBA/NameValuePair:1.0";
/*     */ 
/*  54 */   private static TypeCode __typeCode = null;
/*  55 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, NameValuePair paramNameValuePair)
/*     */   {
/*  43 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  44 */     paramAny.type(type());
/*  45 */     write(localOutputStream, paramNameValuePair);
/*  46 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static NameValuePair extract(Any paramAny)
/*     */   {
/*  51 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  58 */     if (__typeCode == null)
/*     */     {
/*  60 */       synchronized (TypeCode.class)
/*     */       {
/*  62 */         if (__typeCode == null)
/*     */         {
/*  64 */           if (__active)
/*     */           {
/*  66 */             return ORB.init().create_recursive_tc(_id);
/*     */           }
/*  68 */           __active = true;
/*  69 */           StructMember[] arrayOfStructMember = new StructMember[2];
/*  70 */           TypeCode localTypeCode = null;
/*  71 */           localTypeCode = ORB.init().create_string_tc(0);
/*  72 */           localTypeCode = ORB.init().create_alias_tc(FieldNameHelper.id(), "FieldName", localTypeCode);
/*  73 */           arrayOfStructMember[0] = new StructMember("id", localTypeCode, null);
/*     */ 
/*  77 */           localTypeCode = ORB.init().get_primitive_tc(TCKind.tk_any);
/*  78 */           arrayOfStructMember[1] = new StructMember("value", localTypeCode, null);
/*     */ 
/*  82 */           __typeCode = ORB.init().create_struct_tc(id(), "NameValuePair", arrayOfStructMember);
/*  83 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/*  87 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  92 */     return _id;
/*     */   }
/*     */ 
/*     */   public static NameValuePair read(InputStream paramInputStream)
/*     */   {
/*  97 */     NameValuePair localNameValuePair = new NameValuePair();
/*  98 */     localNameValuePair.id = paramInputStream.read_string();
/*  99 */     localNameValuePair.value = paramInputStream.read_any();
/* 100 */     return localNameValuePair;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, NameValuePair paramNameValuePair)
/*     */   {
/* 105 */     paramOutputStream.write_string(paramNameValuePair.id);
/* 106 */     paramOutputStream.write_any(paramNameValuePair.value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.NameValuePairHelper
 * JD-Core Version:    0.6.2
 */
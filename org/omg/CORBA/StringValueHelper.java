/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.omg.CORBA.portable.BoxedValueHelper;
/*     */ 
/*     */ public class StringValueHelper
/*     */   implements BoxedValueHelper
/*     */ {
/*  58 */   private static String _id = "IDL:omg.org/CORBA/StringValue:1.0";
/*     */ 
/*  60 */   private static StringValueHelper _instance = new StringValueHelper();
/*     */ 
/*  75 */   private static TypeCode __typeCode = null;
/*  76 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, String paramString)
/*     */   {
/*  64 */     org.omg.CORBA.portable.OutputStream localOutputStream = paramAny.create_output_stream();
/*  65 */     paramAny.type(type());
/*  66 */     write(localOutputStream, paramString);
/*  67 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static String extract(Any paramAny)
/*     */   {
/*  72 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  79 */     if (__typeCode == null)
/*     */     {
/*  81 */       synchronized (TypeCode.class)
/*     */       {
/*  83 */         if (__typeCode == null)
/*     */         {
/*  85 */           if (__active)
/*     */           {
/*  87 */             return ORB.init().create_recursive_tc(_id);
/*     */           }
/*  89 */           __active = true;
/*  90 */           __typeCode = ORB.init().create_string_tc(0);
/*  91 */           __typeCode = ORB.init().create_value_box_tc(_id, "StringValue", __typeCode);
/*  92 */           __active = false;
/*     */         }
/*     */       }
/*     */     }
/*  96 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 101 */     return _id;
/*     */   }
/*     */ 
/*     */   public static String read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 106 */     if (!(paramInputStream instanceof org.omg.CORBA_2_3.portable.InputStream))
/* 107 */       throw new BAD_PARAM();
/* 108 */     return (String)((org.omg.CORBA_2_3.portable.InputStream)paramInputStream).read_value(_instance);
/*     */   }
/*     */ 
/*     */   public Serializable read_value(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 114 */     String str = paramInputStream.read_string();
/* 115 */     return str;
/*     */   }
/*     */ 
/*     */   public static void write(org.omg.CORBA.portable.OutputStream paramOutputStream, String paramString)
/*     */   {
/* 120 */     if (!(paramOutputStream instanceof org.omg.CORBA_2_3.portable.OutputStream))
/* 121 */       throw new BAD_PARAM();
/* 122 */     ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_value(paramString, _instance);
/*     */   }
/*     */ 
/*     */   public void write_value(org.omg.CORBA.portable.OutputStream paramOutputStream, Serializable paramSerializable)
/*     */   {
/* 127 */     if (!(paramSerializable instanceof String))
/* 128 */       throw new MARSHAL();
/* 129 */     String str = (String)paramSerializable;
/* 130 */     paramOutputStream.write_string(str);
/*     */   }
/*     */ 
/*     */   public String get_id()
/*     */   {
/* 135 */     return _id;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.StringValueHelper
 * JD-Core Version:    0.6.2
 */
/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.omg.CORBA.portable.BoxedValueHelper;
/*     */ 
/*     */ public class WStringValueHelper
/*     */   implements BoxedValueHelper
/*     */ {
/*  62 */   private static String _id = "IDL:omg.org/CORBA/WStringValue:1.0";
/*     */ 
/*  64 */   private static WStringValueHelper _instance = new WStringValueHelper();
/*     */ 
/*  79 */   private static TypeCode __typeCode = null;
/*  80 */   private static boolean __active = false;
/*     */ 
/*     */   public static void insert(Any paramAny, String paramString)
/*     */   {
/*  68 */     org.omg.CORBA.portable.OutputStream localOutputStream = paramAny.create_output_stream();
/*  69 */     paramAny.type(type());
/*  70 */     write(localOutputStream, paramString);
/*  71 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static String extract(Any paramAny)
/*     */   {
/*  76 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  83 */     if (__typeCode == null)
/*     */     {
/*  85 */       synchronized (TypeCode.class)
/*     */       {
/*  87 */         if (__typeCode == null)
/*     */         {
/*  89 */           if (__active)
/*     */           {
/*  91 */             return ORB.init().create_recursive_tc(_id);
/*     */           }
/*  93 */           __active = true;
/*  94 */           __typeCode = ORB.init().create_wstring_tc(0);
/*  95 */           __typeCode = ORB.init().create_value_box_tc(_id, "WStringValue", __typeCode);
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
/*     */   public static String read(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 110 */     if (!(paramInputStream instanceof org.omg.CORBA_2_3.portable.InputStream))
/* 111 */       throw new BAD_PARAM();
/* 112 */     return (String)((org.omg.CORBA_2_3.portable.InputStream)paramInputStream).read_value(_instance);
/*     */   }
/*     */ 
/*     */   public Serializable read_value(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 118 */     String str = paramInputStream.read_wstring();
/* 119 */     return str;
/*     */   }
/*     */ 
/*     */   public static void write(org.omg.CORBA.portable.OutputStream paramOutputStream, String paramString)
/*     */   {
/* 124 */     if (!(paramOutputStream instanceof org.omg.CORBA_2_3.portable.OutputStream))
/* 125 */       throw new BAD_PARAM();
/* 126 */     ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_value(paramString, _instance);
/*     */   }
/*     */ 
/*     */   public void write_value(org.omg.CORBA.portable.OutputStream paramOutputStream, Serializable paramSerializable)
/*     */   {
/* 131 */     if (!(paramSerializable instanceof String))
/* 132 */       throw new MARSHAL();
/* 133 */     String str = (String)paramSerializable;
/* 134 */     paramOutputStream.write_wstring(str);
/*     */   }
/*     */ 
/*     */   public String get_id()
/*     */   {
/* 139 */     return _id;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.WStringValueHelper
 * JD-Core Version:    0.6.2
 */
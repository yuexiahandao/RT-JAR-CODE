/*     */ package org.omg.DynamicAny;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public abstract class DynAnyHelper
/*     */ {
/*  81 */   private static String _id = "IDL:omg.org/DynamicAny/DynAny:1.0";
/*     */ 
/*  96 */   private static TypeCode __typeCode = null;
/*     */ 
/*     */   public static void insert(Any paramAny, DynAny paramDynAny)
/*     */   {
/*  85 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  86 */     paramAny.type(type());
/*  87 */     write(localOutputStream, paramDynAny);
/*  88 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static DynAny extract(Any paramAny)
/*     */   {
/*  93 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  99 */     if (__typeCode == null)
/*     */     {
/* 101 */       __typeCode = ORB.init().create_interface_tc(id(), "DynAny");
/*     */     }
/* 103 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 108 */     return _id;
/*     */   }
/*     */ 
/*     */   public static DynAny read(InputStream paramInputStream)
/*     */   {
/* 113 */     throw new MARSHAL();
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, DynAny paramDynAny)
/*     */   {
/* 118 */     throw new MARSHAL();
/*     */   }
/*     */ 
/*     */   public static DynAny narrow(org.omg.CORBA.Object paramObject)
/*     */   {
/* 123 */     if (paramObject == null)
/* 124 */       return null;
/* 125 */     if ((paramObject instanceof DynAny))
/* 126 */       return (DynAny)paramObject;
/* 127 */     if (!paramObject._is_a(id())) {
/* 128 */       throw new BAD_PARAM();
/*     */     }
/*     */ 
/* 131 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 132 */     _DynAnyStub local_DynAnyStub = new _DynAnyStub();
/* 133 */     local_DynAnyStub._set_delegate(localDelegate);
/* 134 */     return local_DynAnyStub;
/*     */   }
/*     */ 
/*     */   public static DynAny unchecked_narrow(org.omg.CORBA.Object paramObject)
/*     */   {
/* 140 */     if (paramObject == null)
/* 141 */       return null;
/* 142 */     if ((paramObject instanceof DynAny)) {
/* 143 */       return (DynAny)paramObject;
/*     */     }
/*     */ 
/* 146 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 147 */     _DynAnyStub local_DynAnyStub = new _DynAnyStub();
/* 148 */     local_DynAnyStub._set_delegate(localDelegate);
/* 149 */     return local_DynAnyStub;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynAnyHelper
 * JD-Core Version:    0.6.2
 */
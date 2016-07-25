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
/*     */ public abstract class DynAnyFactoryHelper
/*     */ {
/*  35 */   private static String _id = "IDL:omg.org/DynamicAny/DynAnyFactory:1.0";
/*     */ 
/*  50 */   private static TypeCode __typeCode = null;
/*     */ 
/*     */   public static void insert(Any paramAny, DynAnyFactory paramDynAnyFactory)
/*     */   {
/*  39 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  40 */     paramAny.type(type());
/*  41 */     write(localOutputStream, paramDynAnyFactory);
/*  42 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static DynAnyFactory extract(Any paramAny)
/*     */   {
/*  47 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  53 */     if (__typeCode == null)
/*     */     {
/*  55 */       __typeCode = ORB.init().create_interface_tc(id(), "DynAnyFactory");
/*     */     }
/*  57 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  62 */     return _id;
/*     */   }
/*     */ 
/*     */   public static DynAnyFactory read(InputStream paramInputStream)
/*     */   {
/*  67 */     throw new MARSHAL();
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, DynAnyFactory paramDynAnyFactory)
/*     */   {
/*  72 */     throw new MARSHAL();
/*     */   }
/*     */ 
/*     */   public static DynAnyFactory narrow(org.omg.CORBA.Object paramObject)
/*     */   {
/*  77 */     if (paramObject == null)
/*  78 */       return null;
/*  79 */     if ((paramObject instanceof DynAnyFactory))
/*  80 */       return (DynAnyFactory)paramObject;
/*  81 */     if (!paramObject._is_a(id())) {
/*  82 */       throw new BAD_PARAM();
/*     */     }
/*     */ 
/*  85 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/*  86 */     _DynAnyFactoryStub local_DynAnyFactoryStub = new _DynAnyFactoryStub();
/*  87 */     local_DynAnyFactoryStub._set_delegate(localDelegate);
/*  88 */     return local_DynAnyFactoryStub;
/*     */   }
/*     */ 
/*     */   public static DynAnyFactory unchecked_narrow(org.omg.CORBA.Object paramObject)
/*     */   {
/*  94 */     if (paramObject == null)
/*  95 */       return null;
/*  96 */     if ((paramObject instanceof DynAnyFactory)) {
/*  97 */       return (DynAnyFactory)paramObject;
/*     */     }
/*     */ 
/* 100 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 101 */     _DynAnyFactoryStub local_DynAnyFactoryStub = new _DynAnyFactoryStub();
/* 102 */     local_DynAnyFactoryStub._set_delegate(localDelegate);
/* 103 */     return local_DynAnyFactoryStub;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynAnyFactoryHelper
 * JD-Core Version:    0.6.2
 */
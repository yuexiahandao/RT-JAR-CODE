/*     */ package com.sun.org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.IDLType;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ 
/*     */ public final class IDLTypeHelper
/*     */ {
/*  39 */   private static String _id = "IDL:omg.org/CORBA/IDLType:1.0";
/*     */ 
/*  62 */   private static TypeCode __typeCode = null;
/*     */ 
/*     */   public static void insert(Any paramAny, IDLType paramIDLType)
/*     */   {
/*  49 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  50 */     paramAny.type(type());
/*  51 */     write(localOutputStream, paramIDLType);
/*  52 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static IDLType extract(Any paramAny)
/*     */   {
/*  59 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  65 */     if (__typeCode == null)
/*     */     {
/*  67 */       __typeCode = ORB.init().create_interface_tc(id(), "IDLType");
/*     */     }
/*  69 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/*  74 */     return _id;
/*     */   }
/*     */ 
/*     */   public static IDLType read(InputStream paramInputStream)
/*     */   {
/*  81 */     return narrow(paramInputStream.read_Object(_IDLTypeStub.class));
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, IDLType paramIDLType)
/*     */   {
/*  88 */     paramOutputStream.write_Object(paramIDLType);
/*     */   }
/*     */ 
/*     */   public static IDLType narrow(org.omg.CORBA.Object paramObject)
/*     */   {
/*  95 */     if (paramObject == null) {
/*  96 */       return null;
/*     */     }
/*     */ 
/*  99 */     if ((paramObject instanceof IDLType))
/*     */     {
/* 102 */       return (IDLType)paramObject;
/* 103 */     }if (!paramObject._is_a(id())) {
/* 104 */       throw new BAD_PARAM();
/*     */     }
/*     */ 
/* 107 */     Delegate localDelegate = ((ObjectImpl)paramObject)._get_delegate();
/* 108 */     return new _IDLTypeStub(localDelegate);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.IDLTypeHelper
 * JD-Core Version:    0.6.2
 */
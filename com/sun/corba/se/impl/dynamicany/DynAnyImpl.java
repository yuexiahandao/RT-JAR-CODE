/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.CORBA.ORBPackage.InvalidName;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.DynamicAny.DynAny;
/*     */ import org.omg.DynamicAny.DynAnyFactory;
/*     */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*     */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*     */ 
/*     */ abstract class DynAnyImpl extends LocalObject
/*     */   implements DynAny
/*     */ {
/*     */   protected static final int NO_INDEX = -1;
/*     */   protected static final byte STATUS_DESTROYABLE = 0;
/*     */   protected static final byte STATUS_UNDESTROYABLE = 1;
/*     */   protected static final byte STATUS_DESTROYED = 2;
/*  59 */   protected ORB orb = null;
/*     */   protected ORBUtilSystemException wrapper;
/*  66 */   protected Any any = null;
/*     */ 
/*  68 */   protected byte status = 0;
/*  69 */   protected int index = -1;
/*     */ 
/* 195 */   private String[] __ids = { "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*     */ 
/*     */   protected DynAnyImpl()
/*     */   {
/*  76 */     this.wrapper = ORBUtilSystemException.get("rpc.presentation");
/*     */   }
/*     */ 
/*     */   protected DynAnyImpl(ORB paramORB, Any paramAny, boolean paramBoolean)
/*     */   {
/*  81 */     this.orb = paramORB;
/*  82 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.presentation");
/*     */ 
/*  84 */     if (paramBoolean)
/*  85 */       this.any = DynAnyUtil.copy(paramAny, paramORB);
/*     */     else {
/*  87 */       this.any = paramAny;
/*     */     }
/*  89 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   protected DynAnyImpl(ORB paramORB, TypeCode paramTypeCode) {
/*  93 */     this.orb = paramORB;
/*  94 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.presentation");
/*     */ 
/*  96 */     this.any = DynAnyUtil.createDefaultAnyOfType(paramTypeCode, paramORB);
/*     */   }
/*     */ 
/*     */   protected DynAnyFactory factory() {
/*     */     try {
/* 101 */       return (DynAnyFactory)this.orb.resolve_initial_references("DynAnyFactory");
/*     */     } catch (InvalidName localInvalidName) {
/*     */     }
/* 104 */     throw new RuntimeException("Unable to find DynAnyFactory");
/*     */   }
/*     */ 
/*     */   protected Any getAny()
/*     */   {
/* 109 */     return this.any;
/*     */   }
/*     */ 
/*     */   protected Any getAny(DynAny paramDynAny)
/*     */   {
/* 115 */     if ((paramDynAny instanceof DynAnyImpl)) {
/* 116 */       return ((DynAnyImpl)paramDynAny).getAny();
/*     */     }
/*     */ 
/* 122 */     return paramDynAny.to_any();
/*     */   }
/*     */ 
/*     */   protected void writeAny(OutputStream paramOutputStream)
/*     */   {
/* 127 */     this.any.write_value(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected void setStatus(byte paramByte) {
/* 131 */     this.status = paramByte;
/*     */   }
/*     */ 
/*     */   protected void clearData()
/*     */   {
/* 136 */     this.any.type(this.any.type());
/*     */   }
/*     */ 
/*     */   public TypeCode type()
/*     */   {
/* 144 */     if (this.status == 2) {
/* 145 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 147 */     return this.any.type();
/*     */   }
/*     */ 
/*     */   public void assign(DynAny paramDynAny)
/*     */     throws TypeMismatch
/*     */   {
/* 154 */     if (this.status == 2) {
/* 155 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 157 */     if ((this.any != null) && (!this.any.type().equal(paramDynAny.type()))) {
/* 158 */       throw new TypeMismatch();
/*     */     }
/* 160 */     this.any = paramDynAny.to_any();
/*     */   }
/*     */ 
/*     */   public void from_any(Any paramAny)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 168 */     if (this.status == 2) {
/* 169 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 171 */     if ((this.any != null) && (!this.any.type().equal(paramAny.type()))) {
/* 172 */       throw new TypeMismatch();
/*     */     }
/*     */ 
/* 176 */     Any localAny = null;
/*     */     try {
/* 178 */       localAny = DynAnyUtil.copy(paramAny, this.orb);
/*     */     } catch (Exception localException) {
/* 180 */       throw new InvalidValue();
/*     */     }
/* 182 */     if (!DynAnyUtil.isInitialized(localAny)) {
/* 183 */       throw new InvalidValue();
/*     */     }
/* 185 */     this.any = localAny;
/*     */   }
/*     */ 
/*     */   public abstract Any to_any();
/*     */ 
/*     */   public abstract boolean equal(DynAny paramDynAny);
/*     */ 
/*     */   public abstract void destroy();
/*     */ 
/*     */   public abstract DynAny copy();
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 198 */     return (String[])this.__ids.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynAnyImpl
 * JD-Core Version:    0.6.2
 */
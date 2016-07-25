/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*     */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*     */ import org.omg.DynamicAny.DynValueCommon;
/*     */ import org.omg.DynamicAny.NameDynAnyPair;
/*     */ import org.omg.DynamicAny.NameValuePair;
/*     */ 
/*     */ abstract class DynValueCommonImpl extends DynAnyComplexImpl
/*     */   implements DynValueCommon
/*     */ {
/*     */   protected boolean isNull;
/*     */ 
/*     */   private DynValueCommonImpl()
/*     */   {
/*  51 */     this(null, (Any)null, false);
/*  52 */     this.isNull = true;
/*     */   }
/*     */ 
/*     */   protected DynValueCommonImpl(ORB paramORB, Any paramAny, boolean paramBoolean) {
/*  56 */     super(paramORB, paramAny, paramBoolean);
/*  57 */     this.isNull = checkInitComponents();
/*     */   }
/*     */ 
/*     */   protected DynValueCommonImpl(ORB paramORB, TypeCode paramTypeCode) {
/*  61 */     super(paramORB, paramTypeCode);
/*  62 */     this.isNull = true;
/*     */   }
/*     */ 
/*     */   public boolean is_null()
/*     */   {
/*  71 */     return this.isNull;
/*     */   }
/*     */ 
/*     */   public void set_to_null()
/*     */   {
/*  76 */     this.isNull = true;
/*  77 */     clearData();
/*     */   }
/*     */ 
/*     */   public void set_to_value()
/*     */   {
/*  85 */     if (this.isNull)
/*  86 */       this.isNull = false;
/*     */   }
/*     */ 
/*     */   public NameValuePair[] get_members()
/*     */     throws InvalidValue
/*     */   {
/* 100 */     if (this.status == 2) {
/* 101 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 103 */     if (this.isNull) {
/* 104 */       throw new InvalidValue();
/*     */     }
/* 106 */     checkInitComponents();
/* 107 */     return this.nameValuePairs;
/*     */   }
/*     */ 
/*     */   public NameDynAnyPair[] get_members_as_dyn_any()
/*     */     throws InvalidValue
/*     */   {
/* 114 */     if (this.status == 2) {
/* 115 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 117 */     if (this.isNull) {
/* 118 */       throw new InvalidValue();
/*     */     }
/* 120 */     checkInitComponents();
/* 121 */     return this.nameDynAnyPairs;
/*     */   }
/*     */ 
/*     */   public void set_members(NameValuePair[] paramArrayOfNameValuePair)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 133 */     super.set_members(paramArrayOfNameValuePair);
/*     */ 
/* 135 */     this.isNull = false;
/*     */   }
/*     */ 
/*     */   public void set_members_as_dyn_any(NameDynAnyPair[] paramArrayOfNameDynAnyPair)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 143 */     super.set_members_as_dyn_any(paramArrayOfNameDynAnyPair);
/*     */ 
/* 145 */     this.isNull = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynValueCommonImpl
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.DynamicAny.DynAny;
/*     */ import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
/*     */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*     */ import org.omg.DynamicAny.DynArray;
/*     */ 
/*     */ public class DynArrayImpl extends DynAnyCollectionImpl
/*     */   implements DynArray
/*     */ {
/*     */   private DynArrayImpl()
/*     */   {
/*  50 */     this(null, (Any)null, false);
/*     */   }
/*     */ 
/*     */   protected DynArrayImpl(ORB paramORB, Any paramAny, boolean paramBoolean) {
/*  54 */     super(paramORB, paramAny, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected DynArrayImpl(ORB paramORB, TypeCode paramTypeCode) {
/*  58 */     super(paramORB, paramTypeCode);
/*     */   }
/*     */ 
/*     */   protected boolean initializeComponentsFromAny()
/*     */   {
/*  65 */     TypeCode localTypeCode1 = this.any.type();
/*  66 */     int i = getBound();
/*  67 */     TypeCode localTypeCode2 = getContentType();
/*     */     InputStream localInputStream;
/*     */     try {
/*  71 */       localInputStream = this.any.create_input_stream();
/*     */     } catch (BAD_OPERATION localBAD_OPERATION) {
/*  73 */       return false;
/*     */     }
/*     */ 
/*  76 */     this.components = new DynAny[i];
/*  77 */     this.anys = new Any[i];
/*     */ 
/*  79 */     for (int j = 0; j < i; j++)
/*     */     {
/*  82 */       this.anys[j] = DynAnyUtil.extractAnyFromStream(localTypeCode2, localInputStream, this.orb);
/*     */       try
/*     */       {
/*  85 */         this.components[j] = DynAnyUtil.createMostDerivedDynAny(this.anys[j], this.orb, false);
/*     */       } catch (InconsistentTypeCode localInconsistentTypeCode) {
/*     */       }
/*     */     }
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean initializeComponentsFromTypeCode()
/*     */   {
/*  98 */     TypeCode localTypeCode1 = this.any.type();
/*  99 */     int i = getBound();
/* 100 */     TypeCode localTypeCode2 = getContentType();
/*     */ 
/* 102 */     this.components = new DynAny[i];
/* 103 */     this.anys = new Any[i];
/*     */ 
/* 105 */     for (int j = 0; j < i; j++) {
/* 106 */       createDefaultComponentAt(j, localTypeCode2);
/*     */     }
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   protected void checkValue(Object[] paramArrayOfObject)
/*     */     throws InvalidValue
/*     */   {
/* 134 */     if ((paramArrayOfObject == null) || (paramArrayOfObject.length != getBound()))
/* 135 */       throw new InvalidValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynArrayImpl
 * JD-Core Version:    0.6.2
 */
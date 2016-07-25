/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*     */ import org.omg.CORBA.TypeCodePackage.Bounds;
/*     */ import org.omg.DynamicAny.DynAny;
/*     */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*     */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*     */ import org.omg.DynamicAny.DynEnum;
/*     */ 
/*     */ public class DynEnumImpl extends DynAnyBasicImpl
/*     */   implements DynEnum
/*     */ {
/*  48 */   int currentEnumeratorIndex = -1;
/*     */ 
/*     */   private DynEnumImpl()
/*     */   {
/*  55 */     this(null, (Any)null, false);
/*     */   }
/*     */ 
/*     */   protected DynEnumImpl(ORB paramORB, Any paramAny, boolean paramBoolean)
/*     */   {
/*  60 */     super(paramORB, paramAny, paramBoolean);
/*  61 */     this.index = -1;
/*     */     try
/*     */     {
/*  64 */       this.currentEnumeratorIndex = this.any.extract_long();
/*     */     }
/*     */     catch (BAD_OPERATION localBAD_OPERATION) {
/*  67 */       this.currentEnumeratorIndex = 0;
/*  68 */       this.any.type(this.any.type());
/*  69 */       this.any.insert_long(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected DynEnumImpl(ORB paramORB, TypeCode paramTypeCode)
/*     */   {
/*  76 */     super(paramORB, paramTypeCode);
/*  77 */     this.index = -1;
/*  78 */     this.currentEnumeratorIndex = 0;
/*  79 */     this.any.insert_long(0);
/*     */   }
/*     */ 
/*     */   private int memberCount()
/*     */   {
/*  87 */     int i = 0;
/*     */     try {
/*  89 */       i = this.any.type().member_count();
/*     */     } catch (BadKind localBadKind) {
/*     */     }
/*  92 */     return i;
/*     */   }
/*     */ 
/*     */   private String memberName(int paramInt) {
/*  96 */     String str = null;
/*     */     try {
/*  98 */       str = this.any.type().member_name(paramInt);
/*     */     } catch (BadKind localBadKind) {
/*     */     } catch (Bounds localBounds) {
/*     */     }
/* 102 */     return str;
/*     */   }
/*     */ 
/*     */   private int computeCurrentEnumeratorIndex(String paramString) {
/* 106 */     int i = memberCount();
/* 107 */     for (int j = 0; j < i; j++) {
/* 108 */       if (memberName(j).equals(paramString)) {
/* 109 */         return j;
/*     */       }
/*     */     }
/* 112 */     return -1;
/*     */   }
/*     */ 
/*     */   public int component_count()
/*     */   {
/* 121 */     return 0;
/*     */   }
/*     */ 
/*     */   public DynAny current_component()
/*     */     throws TypeMismatch
/*     */   {
/* 129 */     if (this.status == 2) {
/* 130 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 132 */     throw new TypeMismatch();
/*     */   }
/*     */ 
/*     */   public String get_as_string()
/*     */   {
/* 141 */     if (this.status == 2) {
/* 142 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 144 */     return memberName(this.currentEnumeratorIndex);
/*     */   }
/*     */ 
/*     */   public void set_as_string(String paramString)
/*     */     throws InvalidValue
/*     */   {
/* 154 */     if (this.status == 2) {
/* 155 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 157 */     int i = computeCurrentEnumeratorIndex(paramString);
/* 158 */     if (i == -1) {
/* 159 */       throw new InvalidValue();
/*     */     }
/* 161 */     this.currentEnumeratorIndex = i;
/* 162 */     this.any.insert_long(i);
/*     */   }
/*     */ 
/*     */   public int get_as_ulong()
/*     */   {
/* 169 */     if (this.status == 2) {
/* 170 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 172 */     return this.currentEnumeratorIndex;
/*     */   }
/*     */ 
/*     */   public void set_as_ulong(int paramInt)
/*     */     throws InvalidValue
/*     */   {
/* 181 */     if (this.status == 2) {
/* 182 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 184 */     if ((paramInt < 0) || (paramInt >= memberCount())) {
/* 185 */       throw new InvalidValue();
/*     */     }
/* 187 */     this.currentEnumeratorIndex = paramInt;
/* 188 */     this.any.insert_long(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynEnumImpl
 * JD-Core Version:    0.6.2
 */
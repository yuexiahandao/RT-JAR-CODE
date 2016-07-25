/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*     */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*     */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*     */ import org.omg.DynamicAny.DynFixed;
/*     */ 
/*     */ public class DynFixedImpl extends DynAnyBasicImpl
/*     */   implements DynFixed
/*     */ {
/*     */   private DynFixedImpl()
/*     */   {
/*  48 */     this(null, (Any)null, false);
/*     */   }
/*     */ 
/*     */   protected DynFixedImpl(ORB paramORB, Any paramAny, boolean paramBoolean) {
/*  52 */     super(paramORB, paramAny, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected DynFixedImpl(ORB paramORB, TypeCode paramTypeCode)
/*     */   {
/*  57 */     super(paramORB, paramTypeCode);
/*  58 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   public String get_value()
/*     */   {
/*  74 */     if (this.status == 2) {
/*  75 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/*  77 */     return this.any.extract_fixed().toString();
/*     */   }
/*     */ 
/*     */   public boolean set_value(String paramString)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 101 */     if (this.status == 2) {
/* 102 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 104 */     int i = 0;
/* 105 */     int j = 0;
/* 106 */     boolean bool = true;
/*     */     try {
/* 108 */       i = this.any.type().fixed_digits();
/* 109 */       j = this.any.type().fixed_scale();
/*     */     }
/*     */     catch (BadKind localBadKind) {
/*     */     }
/* 113 */     String str1 = paramString.trim();
/* 114 */     if (str1.length() == 0) {
/* 115 */       throw new TypeMismatch();
/*     */     }
/* 117 */     String str2 = "";
/* 118 */     if (str1.charAt(0) == '-') {
/* 119 */       str2 = "-";
/* 120 */       str1 = str1.substring(1);
/* 121 */     } else if (str1.charAt(0) == '+') {
/* 122 */       str2 = "+";
/* 123 */       str1 = str1.substring(1);
/*     */     }
/*     */ 
/* 126 */     int k = str1.indexOf('d');
/* 127 */     if (k == -1) {
/* 128 */       k = str1.indexOf('D');
/*     */     }
/* 130 */     if (k != -1) {
/* 131 */       str1 = str1.substring(0, k);
/*     */     }
/*     */ 
/* 134 */     if (str1.length() == 0) {
/* 135 */       throw new TypeMismatch();
/*     */     }
/*     */ 
/* 141 */     int i1 = str1.indexOf('.');
/*     */     String str3;
/*     */     String str4;
/*     */     int m;
/*     */     int n;
/* 142 */     if (i1 == -1) {
/* 143 */       str3 = str1;
/* 144 */       str4 = null;
/* 145 */       m = 0;
/* 146 */       n = str3.length();
/* 147 */     } else if (i1 == 0) {
/* 148 */       str3 = null;
/* 149 */       str4 = str1;
/* 150 */       m = str4.length();
/* 151 */       n = m;
/*     */     } else {
/* 153 */       str3 = str1.substring(0, i1);
/* 154 */       str4 = str1.substring(i1 + 1);
/* 155 */       m = str4.length();
/* 156 */       n = str3.length() + m;
/*     */     }
/*     */ 
/* 159 */     if (n > i) {
/* 160 */       bool = false;
/*     */ 
/* 162 */       if (str3.length() < i)
/* 163 */         str4 = str4.substring(0, i - str3.length());
/* 164 */       else if (str3.length() == i)
/*     */       {
/* 167 */         str4 = null;
/*     */       }
/*     */       else
/*     */       {
/* 171 */         throw new InvalidValue();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     BigDecimal localBigDecimal;
/*     */     try
/*     */     {
/* 186 */       new BigInteger(str3);
/* 187 */       if (str4 == null) {
/* 188 */         localBigDecimal = new BigDecimal(str2 + str3);
/*     */       } else {
/* 190 */         new BigInteger(str4);
/* 191 */         localBigDecimal = new BigDecimal(str2 + str3 + "." + str4);
/*     */       }
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 194 */       throw new TypeMismatch();
/*     */     }
/* 196 */     this.any.insert_fixed(localBigDecimal, this.any.type());
/* 197 */     return bool;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 201 */     int i = 0;
/* 202 */     int j = 0;
/*     */     try {
/* 204 */       i = this.any.type().fixed_digits();
/* 205 */       j = this.any.type().fixed_scale();
/*     */     } catch (BadKind localBadKind) {
/*     */     }
/* 208 */     return "DynFixed with value=" + get_value() + ", digits=" + i + ", scale=" + j;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynFixedImpl
 * JD-Core Version:    0.6.2
 */
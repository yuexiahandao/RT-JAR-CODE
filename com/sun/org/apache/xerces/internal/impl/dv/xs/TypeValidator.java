/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ 
/*     */ public abstract class TypeValidator
/*     */ {
/*     */   public static final short LESS_THAN = -1;
/*     */   public static final short EQUAL = 0;
/*     */   public static final short GREATER_THAN = 1;
/*     */   public static final short INDETERMINATE = 2;
/*     */ 
/*     */   public abstract short getAllowedFacets();
/*     */ 
/*     */   public abstract Object getActualValue(String paramString, ValidationContext paramValidationContext)
/*     */     throws InvalidDatatypeValueException;
/*     */ 
/*     */   public void checkExtraRules(Object value, ValidationContext context)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isIdentical(Object value1, Object value2)
/*     */   {
/*  72 */     return value1.equals(value2);
/*     */   }
/*     */ 
/*     */   public int compare(Object value1, Object value2)
/*     */   {
/*  78 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getDataLength(Object value)
/*     */   {
/*  84 */     return (value instanceof String) ? ((String)value).length() : -1;
/*     */   }
/*     */ 
/*     */   public int getTotalDigits(Object value)
/*     */   {
/*  90 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getFractionDigits(Object value)
/*     */   {
/*  96 */     return -1;
/*     */   }
/*     */ 
/*     */   public static final boolean isDigit(char ch)
/*     */   {
/* 101 */     return (ch >= '0') && (ch <= '9');
/*     */   }
/*     */ 
/*     */   public static final int getDigit(char ch)
/*     */   {
/* 107 */     return isDigit(ch) ? ch - '0' : -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator
 * JD-Core Version:    0.6.2
 */
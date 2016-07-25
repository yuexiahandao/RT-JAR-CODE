/*     */ package com.sun.beans;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ final class WildcardTypeImpl
/*     */   implements WildcardType
/*     */ {
/*     */   private final Type[] upperBounds;
/*     */   private final Type[] lowerBounds;
/*     */ 
/*     */   WildcardTypeImpl(Type[] paramArrayOfType1, Type[] paramArrayOfType2)
/*     */   {
/*  63 */     this.upperBounds = paramArrayOfType1;
/*  64 */     this.lowerBounds = paramArrayOfType2;
/*     */   }
/*     */ 
/*     */   public Type[] getUpperBounds()
/*     */   {
/*  77 */     return (Type[])this.upperBounds.clone();
/*     */   }
/*     */ 
/*     */   public Type[] getLowerBounds()
/*     */   {
/*  91 */     return (Type[])this.lowerBounds.clone();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 106 */     if ((paramObject instanceof WildcardType)) {
/* 107 */       WildcardType localWildcardType = (WildcardType)paramObject;
/* 108 */       return (Arrays.equals(this.upperBounds, localWildcardType.getUpperBounds())) && (Arrays.equals(this.lowerBounds, localWildcardType.getLowerBounds()));
/*     */     }
/*     */ 
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 124 */     return Arrays.hashCode(this.upperBounds) ^ Arrays.hashCode(this.lowerBounds);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     Type[] arrayOfType;
/*     */     StringBuilder localStringBuilder;
/* 140 */     if (this.lowerBounds.length == 0) {
/* 141 */       if ((this.upperBounds.length == 0) || (Object.class == this.upperBounds[0])) {
/* 142 */         return "?";
/*     */       }
/* 144 */       arrayOfType = this.upperBounds;
/* 145 */       localStringBuilder = new StringBuilder("? extends ");
/*     */     }
/*     */     else {
/* 148 */       arrayOfType = this.lowerBounds;
/* 149 */       localStringBuilder = new StringBuilder("? super ");
/*     */     }
/* 151 */     for (int i = 0; i < arrayOfType.length; i++) {
/* 152 */       if (i > 0) {
/* 153 */         localStringBuilder.append(" & ");
/*     */       }
/* 155 */       localStringBuilder.append((arrayOfType[i] instanceof Class) ? ((Class)arrayOfType[i]).getName() : arrayOfType[i].toString());
/*     */     }
/*     */ 
/* 159 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.WildcardTypeImpl
 * JD-Core Version:    0.6.2
 */
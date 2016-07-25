/*     */ package com.sun.xml.internal.bind.v2.model.nav;
/*     */ 
/*     */ import java.lang.reflect.MalformedParameterizedTypeException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class ParameterizedTypeImpl
/*     */   implements ParameterizedType
/*     */ {
/*     */   private Type[] actualTypeArguments;
/*     */   private Class<?> rawType;
/*     */   private Type ownerType;
/*     */ 
/*     */   ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType)
/*     */   {
/*  46 */     this.actualTypeArguments = actualTypeArguments;
/*  47 */     this.rawType = rawType;
/*  48 */     if (ownerType != null)
/*  49 */       this.ownerType = ownerType;
/*     */     else {
/*  51 */       this.ownerType = rawType.getDeclaringClass();
/*     */     }
/*  53 */     validateConstructorArguments();
/*     */   }
/*     */ 
/*     */   private void validateConstructorArguments() {
/*  57 */     TypeVariable[] formals = this.rawType.getTypeParameters();
/*     */ 
/*  59 */     if (formals.length != this.actualTypeArguments.length) {
/*  60 */       throw new MalformedParameterizedTypeException();
/*     */     }
/*  62 */     for (int i = 0; i < this.actualTypeArguments.length; i++);
/*     */   }
/*     */ 
/*     */   public Type[] getActualTypeArguments()
/*     */   {
/*  69 */     return (Type[])this.actualTypeArguments.clone();
/*     */   }
/*     */ 
/*     */   public Class<?> getRawType() {
/*  73 */     return this.rawType;
/*     */   }
/*     */ 
/*     */   public Type getOwnerType() {
/*  77 */     return this.ownerType;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  89 */     if ((o instanceof ParameterizedType))
/*     */     {
/*  91 */       ParameterizedType that = (ParameterizedType)o;
/*     */ 
/*  93 */       if (this == that) {
/*  94 */         return true;
/*     */       }
/*  96 */       Type thatOwner = that.getOwnerType();
/*  97 */       Type thatRawType = that.getRawType();
/*     */ 
/* 119 */       return (this.ownerType == null ? thatOwner == null : this.ownerType.equals(thatOwner)) && (this.rawType == null ? thatRawType == null : this.rawType.equals(thatRawType)) && (Arrays.equals(this.actualTypeArguments, that.getActualTypeArguments()));
/*     */     }
/*     */ 
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 134 */     return Arrays.hashCode(this.actualTypeArguments) ^ (this.ownerType == null ? 0 : this.ownerType.hashCode()) ^ (this.rawType == null ? 0 : this.rawType.hashCode());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 140 */     StringBuilder sb = new StringBuilder();
/*     */ 
/* 142 */     if (this.ownerType != null) {
/* 143 */       if ((this.ownerType instanceof Class))
/* 144 */         sb.append(((Class)this.ownerType).getName());
/*     */       else {
/* 146 */         sb.append(this.ownerType.toString());
/*     */       }
/* 148 */       sb.append(".");
/*     */ 
/* 150 */       if ((this.ownerType instanceof ParameterizedTypeImpl))
/*     */       {
/* 153 */         sb.append(this.rawType.getName().replace(((ParameterizedTypeImpl)this.ownerType).rawType.getName() + "$", ""));
/*     */       }
/*     */       else
/* 156 */         sb.append(this.rawType.getName());
/*     */     } else {
/* 158 */       sb.append(this.rawType.getName());
/*     */     }
/* 160 */     if ((this.actualTypeArguments != null) && (this.actualTypeArguments.length > 0))
/*     */     {
/* 162 */       sb.append("<");
/* 163 */       boolean first = true;
/* 164 */       for (Type t : this.actualTypeArguments) {
/* 165 */         if (!first)
/* 166 */           sb.append(", ");
/* 167 */         if ((t instanceof Class))
/* 168 */           sb.append(((Class)t).getName());
/*     */         else
/* 170 */           sb.append(t.toString());
/* 171 */         first = false;
/*     */       }
/* 173 */       sb.append(">");
/*     */     }
/*     */ 
/* 176 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.nav.ParameterizedTypeImpl
 * JD-Core Version:    0.6.2
 */
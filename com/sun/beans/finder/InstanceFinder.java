/*     */ package com.sun.beans.finder;
/*     */ 
/*     */ class InstanceFinder<T>
/*     */ {
/*  37 */   private static final String[] EMPTY = new String[0];
/*     */   private final Class<? extends T> type;
/*     */   private final boolean allow;
/*     */   private final String suffix;
/*     */   private volatile String[] packages;
/*     */ 
/*     */   InstanceFinder(Class<? extends T> paramClass, boolean paramBoolean, String paramString, String[] paramArrayOfString)
/*     */   {
/*  45 */     this.type = paramClass;
/*  46 */     this.allow = paramBoolean;
/*  47 */     this.suffix = paramString;
/*  48 */     this.packages = ((String[])paramArrayOfString.clone());
/*     */   }
/*     */ 
/*     */   public String[] getPackages() {
/*  52 */     return (String[])this.packages.clone();
/*     */   }
/*     */ 
/*     */   public void setPackages(String[] paramArrayOfString) {
/*  56 */     this.packages = ((paramArrayOfString != null) && (paramArrayOfString.length > 0) ? (String[])paramArrayOfString.clone() : EMPTY);
/*     */   }
/*     */ 
/*     */   public T find(Class<?> paramClass)
/*     */   {
/*  62 */     if (paramClass == null) {
/*  63 */       return null;
/*     */     }
/*  65 */     String str1 = paramClass.getName() + this.suffix;
/*  66 */     Object localObject = instantiate(paramClass, str1);
/*  67 */     if (localObject != null) {
/*  68 */       return localObject;
/*     */     }
/*  70 */     if (this.allow) {
/*  71 */       localObject = instantiate(paramClass, null);
/*  72 */       if (localObject != null) {
/*  73 */         return localObject;
/*     */       }
/*     */     }
/*  76 */     int i = str1.lastIndexOf('.') + 1;
/*  77 */     if (i > 0) {
/*  78 */       str1 = str1.substring(i);
/*     */     }
/*  80 */     for (String str2 : this.packages) {
/*  81 */       localObject = instantiate(paramClass, str2, str1);
/*  82 */       if (localObject != null) {
/*  83 */         return localObject;
/*     */       }
/*     */     }
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   protected T instantiate(Class<?> paramClass, String paramString) {
/*  90 */     if (paramClass != null) {
/*     */       try {
/*  92 */         if (paramString != null) {
/*  93 */           paramClass = ClassFinder.findClass(paramString, paramClass.getClassLoader());
/*     */         }
/*  95 */         if (this.type.isAssignableFrom(paramClass)) {
/*  96 */           return paramClass.newInstance();
/*     */         }
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */     }
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */   protected T instantiate(Class<?> paramClass, String paramString1, String paramString2) {
/* 107 */     return instantiate(paramClass, paramString1 + '.' + paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.InstanceFinder
 * JD-Core Version:    0.6.2
 */
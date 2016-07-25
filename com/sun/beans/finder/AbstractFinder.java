/*     */ package com.sun.beans.finder;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ abstract class AbstractFinder<T extends Member>
/*     */ {
/*     */   private final Class<?>[] args;
/*     */ 
/*     */   protected AbstractFinder(Class<?>[] paramArrayOfClass)
/*     */   {
/*  55 */     this.args = paramArrayOfClass;
/*     */   }
/*     */ 
/*     */   protected abstract Class<?>[] getParameters(T paramT);
/*     */ 
/*     */   protected abstract boolean isVarArgs(T paramT);
/*     */ 
/*     */   protected boolean isValid(T paramT)
/*     */   {
/*  88 */     return Modifier.isPublic(paramT.getModifiers());
/*     */   }
/*     */ 
/*     */   final T find(T[] paramArrayOfT)
/*     */     throws NoSuchMethodException
/*     */   {
/* 109 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 111 */     Object localObject1 = null;
/* 112 */     Object localObject2 = null;
/* 113 */     int i = 0;
/*     */     T ?;
/*     */     Class[] arrayOfClass1;
/* 115 */     for (? : paramArrayOfT) {
/* 116 */       if (isValid(?)) {
/* 117 */         arrayOfClass1 = getParameters(?);
/* 118 */         if (arrayOfClass1.length == this.args.length) {
/* 119 */           PrimitiveWrapperMap.replacePrimitivesWithWrappers(arrayOfClass1);
/* 120 */           if (isAssignable(arrayOfClass1, this.args)) {
/* 121 */             if (localObject1 == null) {
/* 122 */               localObject1 = ?;
/* 123 */               localObject2 = arrayOfClass1;
/*     */             } else {
/* 125 */               boolean bool1 = isAssignable(localObject2, arrayOfClass1);
/* 126 */               boolean bool3 = isAssignable(arrayOfClass1, localObject2);
/*     */ 
/* 128 */               if ((bool3) && (bool1))
/*     */               {
/* 130 */                 bool1 = !?.isSynthetic();
/* 131 */                 bool3 = !localObject1.isSynthetic();
/*     */               }
/* 133 */               if (bool3 == bool1) {
/* 134 */                 i = 1;
/* 135 */               } else if (bool1) {
/* 136 */                 localObject1 = ?;
/* 137 */                 localObject2 = arrayOfClass1;
/* 138 */                 i = 0;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 143 */         if (isVarArgs(?)) {
/* 144 */           int m = arrayOfClass1.length - 1;
/* 145 */           if (m <= this.args.length) {
/* 146 */             Class[] arrayOfClass2 = new Class[this.args.length];
/* 147 */             System.arraycopy(arrayOfClass1, 0, arrayOfClass2, 0, m);
/* 148 */             if (m < this.args.length) {
/* 149 */               Class localClass = arrayOfClass1[m].getComponentType();
/* 150 */               if (localClass.isPrimitive()) {
/* 151 */                 localClass = PrimitiveWrapperMap.getType(localClass.getName());
/*     */               }
/* 153 */               for (int n = m; n < this.args.length; n++) {
/* 154 */                 arrayOfClass2[n] = localClass;
/*     */               }
/*     */             }
/* 157 */             localHashMap.put(?, arrayOfClass2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 162 */     for (? : paramArrayOfT) {
/* 163 */       arrayOfClass1 = (Class[])localHashMap.get(?);
/* 164 */       if ((arrayOfClass1 != null) && 
/* 165 */         (isAssignable(arrayOfClass1, this.args))) {
/* 166 */         if (localObject1 == null) {
/* 167 */           localObject1 = ?;
/* 168 */           localObject2 = arrayOfClass1;
/*     */         } else {
/* 170 */           boolean bool2 = isAssignable(localObject2, arrayOfClass1);
/* 171 */           boolean bool4 = isAssignable(arrayOfClass1, localObject2);
/*     */ 
/* 173 */           if ((bool4) && (bool2))
/*     */           {
/* 175 */             bool2 = !?.isSynthetic();
/* 176 */             bool4 = !localObject1.isSynthetic();
/*     */           }
/* 178 */           if (bool4 == bool2) {
/* 179 */             if (localObject2 == localHashMap.get(localObject1))
/* 180 */               i = 1;
/*     */           }
/* 182 */           else if (bool2) {
/* 183 */             localObject1 = ?;
/* 184 */             localObject2 = arrayOfClass1;
/* 185 */             i = 0;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 192 */     if (i != 0) {
/* 193 */       throw new NoSuchMethodException("Ambiguous methods are found");
/*     */     }
/* 195 */     if (localObject1 == null) {
/* 196 */       throw new NoSuchMethodException("Method is not found");
/*     */     }
/* 198 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private boolean isAssignable(Class<?>[] paramArrayOfClass1, Class<?>[] paramArrayOfClass2)
/*     */   {
/* 219 */     for (int i = 0; i < this.args.length; i++) {
/* 220 */       if ((null != this.args[i]) && 
/* 221 */         (!paramArrayOfClass1[i].isAssignableFrom(paramArrayOfClass2[i]))) {
/* 222 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 226 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.AbstractFinder
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.beans;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;
/*     */ import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
/*     */ 
/*     */ public final class TypeResolver
/*     */ {
/*  49 */   private static final WeakCache<Type, Map<Type, Type>> CACHE = new WeakCache();
/*     */ 
/*     */   public static Type resolveInClass(Class<?> paramClass, Type paramType)
/*     */   {
/*  81 */     return resolve(getActualType(paramClass), paramType);
/*     */   }
/*     */ 
/*     */   public static Type[] resolveInClass(Class<?> paramClass, Type[] paramArrayOfType)
/*     */   {
/*  96 */     return resolve(getActualType(paramClass), paramArrayOfType);
/*     */   }
/*     */ 
/*     */   public static Type resolve(Type paramType1, Type paramType2)
/*     */   {
/* 157 */     if ((paramType2 instanceof Class))
/* 158 */       return paramType2;
/*     */     Object localObject1;
/* 160 */     if ((paramType2 instanceof GenericArrayType)) {
/* 161 */       localObject1 = ((GenericArrayType)paramType2).getGenericComponentType();
/* 162 */       localObject1 = resolve(paramType1, (Type)localObject1);
/* 163 */       return (localObject1 instanceof Class) ? Array.newInstance((Class)localObject1, 0).getClass() : GenericArrayTypeImpl.make((Type)localObject1);
/*     */     }
/*     */     Type[] arrayOfType1;
/* 167 */     if ((paramType2 instanceof ParameterizedType)) {
/* 168 */       localObject1 = (ParameterizedType)paramType2;
/* 169 */       arrayOfType1 = resolve(paramType1, ((ParameterizedType)localObject1).getActualTypeArguments());
/* 170 */       return ParameterizedTypeImpl.make((Class)((ParameterizedType)localObject1).getRawType(), arrayOfType1, ((ParameterizedType)localObject1).getOwnerType());
/*     */     }
/*     */ 
/* 173 */     if ((paramType2 instanceof WildcardType)) {
/* 174 */       localObject1 = (WildcardType)paramType2;
/* 175 */       arrayOfType1 = resolve(paramType1, ((WildcardType)localObject1).getUpperBounds());
/* 176 */       Type[] arrayOfType2 = resolve(paramType1, ((WildcardType)localObject1).getLowerBounds());
/* 177 */       return new WildcardTypeImpl(arrayOfType1, arrayOfType2);
/*     */     }
/* 179 */     if ((paramType2 instanceof TypeVariable))
/*     */     {
/* 181 */       synchronized (CACHE) {
/* 182 */         localObject1 = (Map)CACHE.get(paramType1);
/* 183 */         if (localObject1 == null) {
/* 184 */           localObject1 = new HashMap();
/* 185 */           prepare((Map)localObject1, paramType1);
/* 186 */           CACHE.put(paramType1, localObject1);
/*     */         }
/*     */       }
/* 189 */       ??? = (Type)((Map)localObject1).get(paramType2);
/* 190 */       if ((??? == null) || (???.equals(paramType2))) {
/* 191 */         return paramType2;
/*     */       }
/* 193 */       ??? = fixGenericArray((Type)???);
/*     */ 
/* 201 */       return resolve(paramType1, (Type)???);
/*     */     }
/* 203 */     throw new IllegalArgumentException("Bad Type kind: " + paramType2.getClass());
/*     */   }
/*     */ 
/*     */   public static Type[] resolve(Type paramType, Type[] paramArrayOfType)
/*     */   {
/* 215 */     int i = paramArrayOfType.length;
/* 216 */     Type[] arrayOfType = new Type[i];
/* 217 */     for (int j = 0; j < i; j++) {
/* 218 */       arrayOfType[j] = resolve(paramType, paramArrayOfType[j]);
/*     */     }
/* 220 */     return arrayOfType;
/*     */   }
/*     */ 
/*     */   public static Class<?> erase(Type paramType)
/*     */   {
/* 233 */     if ((paramType instanceof Class))
/* 234 */       return (Class)paramType;
/*     */     Object localObject;
/* 236 */     if ((paramType instanceof ParameterizedType)) {
/* 237 */       localObject = (ParameterizedType)paramType;
/* 238 */       return (Class)((ParameterizedType)localObject).getRawType();
/*     */     }
/*     */     Type[] arrayOfType;
/* 240 */     if ((paramType instanceof TypeVariable)) {
/* 241 */       localObject = (TypeVariable)paramType;
/* 242 */       arrayOfType = ((TypeVariable)localObject).getBounds();
/* 243 */       return 0 < arrayOfType.length ? erase(arrayOfType[0]) : Object.class;
/*     */     }
/*     */ 
/* 247 */     if ((paramType instanceof WildcardType)) {
/* 248 */       localObject = (WildcardType)paramType;
/* 249 */       arrayOfType = ((WildcardType)localObject).getUpperBounds();
/* 250 */       return 0 < arrayOfType.length ? erase(arrayOfType[0]) : Object.class;
/*     */     }
/*     */ 
/* 254 */     if ((paramType instanceof GenericArrayType)) {
/* 255 */       localObject = (GenericArrayType)paramType;
/* 256 */       return Array.newInstance(erase(((GenericArrayType)localObject).getGenericComponentType()), 0).getClass();
/*     */     }
/* 258 */     throw new IllegalArgumentException("Unknown Type kind: " + paramType.getClass());
/*     */   }
/*     */ 
/*     */   public static Class[] erase(Type[] paramArrayOfType)
/*     */   {
/* 271 */     int i = paramArrayOfType.length;
/* 272 */     Class[] arrayOfClass = new Class[i];
/* 273 */     for (int j = 0; j < i; j++) {
/* 274 */       arrayOfClass[j] = erase(paramArrayOfType[j]);
/*     */     }
/* 276 */     return arrayOfClass;
/*     */   }
/*     */ 
/*     */   private static void prepare(Map<Type, Type> paramMap, Type paramType)
/*     */   {
/* 294 */     Class localClass = (Class)((paramType instanceof Class) ? paramType : ((ParameterizedType)paramType).getRawType());
/*     */ 
/* 298 */     TypeVariable[] arrayOfTypeVariable = localClass.getTypeParameters();
/*     */ 
/* 300 */     Type[] arrayOfType = (paramType instanceof Class) ? arrayOfTypeVariable : ((ParameterizedType)paramType).getActualTypeArguments();
/*     */ 
/* 304 */     assert (arrayOfTypeVariable.length == arrayOfType.length);
/* 305 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 306 */       paramMap.put(arrayOfTypeVariable[i], arrayOfType[i]);
/*     */     }
/* 308 */     Type localType1 = localClass.getGenericSuperclass();
/* 309 */     if (localType1 != null) {
/* 310 */       prepare(paramMap, localType1);
/*     */     }
/* 312 */     for (Type localType2 : localClass.getGenericInterfaces()) {
/* 313 */       prepare(paramMap, localType2);
/*     */     }
/*     */ 
/* 317 */     if (((paramType instanceof Class)) && (arrayOfTypeVariable.length > 0))
/* 318 */       for (??? = paramMap.entrySet().iterator(); ((Iterator)???).hasNext(); ) { Map.Entry localEntry = (Map.Entry)((Iterator)???).next();
/* 319 */         localEntry.setValue(erase((Type)localEntry.getValue()));
/*     */       }
/*     */   }
/*     */ 
/*     */   private static Type fixGenericArray(Type paramType)
/*     */   {
/* 343 */     if ((paramType instanceof GenericArrayType)) {
/* 344 */       Type localType = ((GenericArrayType)paramType).getGenericComponentType();
/* 345 */       localType = fixGenericArray(localType);
/* 346 */       if ((localType instanceof Class)) {
/* 347 */         return Array.newInstance((Class)localType, 0).getClass();
/*     */       }
/*     */     }
/* 350 */     return paramType;
/*     */   }
/*     */ 
/*     */   private static Type getActualType(Class<?> paramClass)
/*     */   {
/* 372 */     TypeVariable[] arrayOfTypeVariable = paramClass.getTypeParameters();
/* 373 */     return arrayOfTypeVariable.length == 0 ? paramClass : ParameterizedTypeImpl.make(paramClass, arrayOfTypeVariable, paramClass.getEnclosingClass());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.TypeResolver
 * JD-Core Version:    0.6.2
 */
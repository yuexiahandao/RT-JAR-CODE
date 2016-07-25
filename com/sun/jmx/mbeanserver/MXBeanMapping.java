/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.io.InvalidObjectException;
/*     */ import java.lang.reflect.Type;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ import javax.management.openmbean.OpenType;
/*     */ 
/*     */ public abstract class MXBeanMapping
/*     */ {
/*     */   private final Type javaType;
/*     */   private final OpenType<?> openType;
/*     */   private final Class<?> openClass;
/*     */ 
/*     */   protected MXBeanMapping(Type paramType, OpenType<?> paramOpenType)
/*     */   {
/* 133 */     if ((paramType == null) || (paramOpenType == null))
/* 134 */       throw new NullPointerException("Null argument");
/* 135 */     this.javaType = paramType;
/* 136 */     this.openType = paramOpenType;
/* 137 */     this.openClass = makeOpenClass(paramType, paramOpenType);
/*     */   }
/*     */ 
/*     */   public final Type getJavaType()
/*     */   {
/* 145 */     return this.javaType;
/*     */   }
/*     */ 
/*     */   public final OpenType<?> getOpenType()
/*     */   {
/* 153 */     return this.openType;
/*     */   }
/*     */ 
/*     */   public final Class<?> getOpenClass()
/*     */   {
/* 164 */     return this.openClass;
/*     */   }
/*     */ 
/*     */   private static Class<?> makeOpenClass(Type paramType, OpenType<?> paramOpenType) {
/* 168 */     if (((paramType instanceof Class)) && (((Class)paramType).isPrimitive()))
/* 169 */       return (Class)paramType;
/*     */     try {
/* 171 */       String str = paramOpenType.getClassName();
/* 172 */       return Class.forName(str, false, null);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 174 */       throw new RuntimeException(localClassNotFoundException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract Object fromOpenValue(Object paramObject)
/*     */     throws InvalidObjectException;
/*     */ 
/*     */   public abstract Object toOpenValue(Object paramObject)
/*     */     throws OpenDataException;
/*     */ 
/*     */   public void checkReconstructible()
/*     */     throws InvalidObjectException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MXBeanMapping
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.beans.finder;
/*     */ 
/*     */ import java.beans.BeanDescriptor;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.MethodDescriptor;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public final class BeanInfoFinder extends InstanceFinder<BeanInfo>
/*     */ {
/*     */   private static final String DEFAULT = "sun.beans.infos";
/*     */   private static final String DEFAULT_NEW = "com.sun.beans.infos";
/*     */ 
/*     */   public BeanInfoFinder()
/*     */   {
/*  48 */     super(BeanInfo.class, true, "BeanInfo", new String[] { "sun.beans.infos" });
/*     */   }
/*     */ 
/*     */   private static boolean isValid(Class<?> paramClass, Method paramMethod) {
/*  52 */     return (paramMethod != null) && (paramMethod.getDeclaringClass().isAssignableFrom(paramClass));
/*     */   }
/*     */ 
/*     */   protected BeanInfo instantiate(Class<?> paramClass, String paramString1, String paramString2)
/*     */   {
/*  57 */     if ("sun.beans.infos".equals(paramString1)) {
/*  58 */       paramString1 = "com.sun.beans.infos";
/*     */     }
/*     */ 
/*  63 */     BeanInfo localBeanInfo = (!"com.sun.beans.infos".equals(paramString1)) || ("ComponentBeanInfo".equals(paramString2)) ? (BeanInfo)super.instantiate(paramClass, paramString1, paramString2) : null;
/*     */ 
/*  67 */     if (localBeanInfo != null)
/*     */     {
/*  69 */       BeanDescriptor localBeanDescriptor = localBeanInfo.getBeanDescriptor();
/*  70 */       if (localBeanDescriptor != null) {
/*  71 */         if (paramClass.equals(localBeanDescriptor.getBeanClass()))
/*  72 */           return localBeanInfo;
/*     */       }
/*     */       else
/*     */       {
/*  76 */         PropertyDescriptor[] arrayOfPropertyDescriptor = localBeanInfo.getPropertyDescriptors();
/*     */         Method localMethod;
/*  77 */         if (arrayOfPropertyDescriptor != null) {
/*  78 */           for (Object localObject3 : arrayOfPropertyDescriptor) {
/*  79 */             localMethod = localObject3.getReadMethod();
/*  80 */             if (localMethod == null) {
/*  81 */               localMethod = localObject3.getWriteMethod();
/*     */             }
/*  83 */             if (isValid(paramClass, localMethod))
/*  84 */               return localBeanInfo;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  89 */           ??? = localBeanInfo.getMethodDescriptors();
/*  90 */           if (??? != null) {
/*  91 */             for (localMethod : ???) {
/*  92 */               if (isValid(paramClass, localMethod.getMethod())) {
/*  93 */                 return localBeanInfo;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 100 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.BeanInfoFinder
 * JD-Core Version:    0.6.2
 */
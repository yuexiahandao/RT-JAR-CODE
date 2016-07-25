/*     */ package com.sun.xml.internal.bind.v2.model.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class RuntimeInlineAnnotationReader extends AbstractInlineAnnotationReaderImpl<Type, Class, Field, Method>
/*     */   implements RuntimeAnnotationReader
/*     */ {
/*  98 */   private final Map<Class<? extends Annotation>, Map<Package, Annotation>> packageCache = new HashMap();
/*     */ 
/*     */   public <A extends Annotation> A getFieldAnnotation(Class<A> annotation, Field field, Locatable srcPos)
/*     */   {
/*  46 */     return LocatableAnnotation.create(field.getAnnotation(annotation), srcPos);
/*     */   }
/*     */ 
/*     */   public boolean hasFieldAnnotation(Class<? extends Annotation> annotationType, Field field) {
/*  50 */     return field.isAnnotationPresent(annotationType);
/*     */   }
/*     */ 
/*     */   public boolean hasClassAnnotation(Class clazz, Class<? extends Annotation> annotationType) {
/*  54 */     return clazz.isAnnotationPresent(annotationType);
/*     */   }
/*     */ 
/*     */   public Annotation[] getAllFieldAnnotations(Field field, Locatable srcPos) {
/*  58 */     Annotation[] r = field.getAnnotations();
/*  59 */     for (int i = 0; i < r.length; i++) {
/*  60 */       r[i] = LocatableAnnotation.create(r[i], srcPos);
/*     */     }
/*  62 */     return r;
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotation, Method method, Locatable srcPos) {
/*  66 */     return LocatableAnnotation.create(method.getAnnotation(annotation), srcPos);
/*     */   }
/*     */ 
/*     */   public boolean hasMethodAnnotation(Class<? extends Annotation> annotation, Method method) {
/*  70 */     return method.isAnnotationPresent(annotation);
/*     */   }
/*     */ 
/*     */   public Annotation[] getAllMethodAnnotations(Method method, Locatable srcPos) {
/*  74 */     Annotation[] r = method.getAnnotations();
/*  75 */     for (int i = 0; i < r.length; i++) {
/*  76 */       r[i] = LocatableAnnotation.create(r[i], srcPos);
/*     */     }
/*  78 */     return r;
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getMethodParameterAnnotation(Class<A> annotation, Method method, int paramIndex, Locatable srcPos) {
/*  82 */     Annotation[] pa = method.getParameterAnnotations()[paramIndex];
/*  83 */     for (Annotation a : pa) {
/*  84 */       if (a.annotationType() == annotation)
/*  85 */         return LocatableAnnotation.create(a, srcPos);
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getClassAnnotation(Class<A> a, Class clazz, Locatable srcPos) {
/*  91 */     return LocatableAnnotation.create(clazz.getAnnotation(a), srcPos);
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getPackageAnnotation(Class<A> a, Class clazz, Locatable srcPos)
/*     */   {
/* 102 */     Package p = clazz.getPackage();
/* 103 */     if (p == null) return null;
/*     */ 
/* 105 */     Map cache = (Map)this.packageCache.get(a);
/* 106 */     if (cache == null) {
/* 107 */       cache = new HashMap();
/* 108 */       this.packageCache.put(a, cache);
/*     */     }
/*     */ 
/* 111 */     if (cache.containsKey(p)) {
/* 112 */       return (Annotation)cache.get(p);
/*     */     }
/* 114 */     Annotation ann = LocatableAnnotation.create(p.getAnnotation(a), srcPos);
/* 115 */     cache.put(p, ann);
/* 116 */     return ann;
/*     */   }
/*     */ 
/*     */   public Class getClassValue(Annotation a, String name)
/*     */   {
/*     */     try {
/* 122 */       return (Class)a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 125 */       throw new IllegalAccessError(e.getMessage());
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 128 */       throw new InternalError(Messages.CLASS_NOT_FOUND.format(new Object[] { a.annotationType(), e.getMessage() }));
/*     */     } catch (NoSuchMethodException e) {
/* 130 */       throw new NoSuchMethodError(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Class[] getClassArrayValue(Annotation a, String name) {
/*     */     try {
/* 136 */       return (Class[])a.annotationType().getMethod(name, new Class[0]).invoke(a, new Object[0]);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 139 */       throw new IllegalAccessError(e.getMessage());
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 142 */       throw new InternalError(e.getMessage());
/*     */     } catch (NoSuchMethodException e) {
/* 144 */       throw new NoSuchMethodError(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String fullName(Method m) {
/* 149 */     return m.getDeclaringClass().getName() + '#' + m.getName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader
 * JD-Core Version:    0.6.2
 */
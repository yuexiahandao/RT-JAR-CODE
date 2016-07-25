/*     */ package com.sun.xml.internal.bind.v2.model.annotation;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class LocatableAnnotation
/*     */   implements InvocationHandler, Locatable, Location
/*     */ {
/*     */   private final Annotation core;
/*     */   private final Locatable upstream;
/* 123 */   private static final Map<Class, Quick> quicks = new HashMap();
/*     */ 
/*     */   public static <A extends Annotation> A create(A annotation, Locatable parentSourcePos)
/*     */   {
/*  54 */     if (annotation == null) return null;
/*  55 */     Class type = annotation.annotationType();
/*  56 */     if (quicks.containsKey(type))
/*     */     {
/*  58 */       return ((Quick)quicks.get(type)).newInstance(parentSourcePos, annotation);
/*     */     }
/*     */ 
/*  63 */     ClassLoader cl = LocatableAnnotation.class.getClassLoader();
/*     */     try
/*     */     {
/*  66 */       Class loadableT = Class.forName(type.getName(), false, cl);
/*  67 */       if (loadableT != type) {
/*  68 */         return annotation;
/*     */       }
/*  70 */       return (Annotation)Proxy.newProxyInstance(cl, new Class[] { type, Locatable.class }, new LocatableAnnotation(annotation, parentSourcePos));
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/*  75 */       return annotation;
/*     */     }
/*     */     catch (IllegalArgumentException e) {
/*     */     }
/*  79 */     return annotation;
/*     */   }
/*     */ 
/*     */   LocatableAnnotation(Annotation core, Locatable upstream)
/*     */   {
/*  85 */     this.core = core;
/*  86 */     this.upstream = upstream;
/*     */   }
/*     */ 
/*     */   public Locatable getUpstream() {
/*  90 */     return this.upstream;
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*     */     try {
/*  99 */       if (method.getDeclaringClass() == Locatable.class)
/* 100 */         return method.invoke(this, args);
/* 101 */       if (Modifier.isStatic(method.getModifiers()))
/*     */       {
/* 105 */         throw new IllegalArgumentException();
/*     */       }
/* 107 */       return method.invoke(this.core, args);
/*     */     } catch (InvocationTargetException e) {
/* 109 */       if (e.getTargetException() != null)
/* 110 */         throw e.getTargetException();
/* 111 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 116 */     return this.core.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 126 */     for (Quick q : Init.getAll())
/* 127 */       quicks.put(q.annotationType(), q);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.LocatableAnnotation
 * JD-Core Version:    0.6.2
 */
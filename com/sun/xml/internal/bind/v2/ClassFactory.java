/*     */ package com.sun.xml.internal.bind.v2;
/*     */ 
/*     */ import com.sun.xml.internal.bind.Util;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public final class ClassFactory
/*     */ {
/*  51 */   private static final Class[] emptyClass = new Class[0];
/*  52 */   private static final Object[] emptyObject = new Object[0];
/*     */ 
/*  54 */   private static final Logger logger = Util.getClassLogger();
/*     */ 
/*  61 */   private static final ThreadLocal<Map<Class, WeakReference<Constructor>>> tls = new ThreadLocal()
/*     */   {
/*     */     public Map<Class, WeakReference<Constructor>> initialValue() {
/*  64 */       return new WeakHashMap();
/*     */     }
/*  61 */   };
/*     */ 
/*     */   public static void cleanCache()
/*     */   {
/*  69 */     if (tls != null)
/*     */       try {
/*  71 */         tls.remove();
/*     */       } catch (Exception e) {
/*  73 */         logger.log(Level.WARNING, "Unable to clean Thread Local cache of classes used in Unmarshaller: {0}", e.getLocalizedMessage());
/*     */       }
/*     */   }
/*     */ 
/*     */   public static <T> T create0(Class<T> clazz)
/*     */     throws IllegalAccessException, InvocationTargetException, InstantiationException
/*     */   {
/*  82 */     Map m = (Map)tls.get();
/*  83 */     Constructor cons = null;
/*  84 */     WeakReference consRef = (WeakReference)m.get(clazz);
/*  85 */     if (consRef != null)
/*  86 */       cons = (Constructor)consRef.get();
/*  87 */     if (cons == null) {
/*     */       try {
/*  89 */         cons = clazz.getDeclaredConstructor(emptyClass);
/*     */       } catch (NoSuchMethodException e) {
/*  91 */         logger.log(Level.INFO, "No default constructor found on " + clazz, e);
/*     */         NoSuchMethodError exp;
/*     */         NoSuchMethodError exp;
/*  93 */         if ((clazz.getDeclaringClass() != null) && (!Modifier.isStatic(clazz.getModifiers())))
/*  94 */           exp = new NoSuchMethodError(Messages.NO_DEFAULT_CONSTRUCTOR_IN_INNER_CLASS.format(new Object[] { clazz.getName() }));
/*     */         else {
/*  96 */           exp = new NoSuchMethodError(e.getMessage());
/*     */         }
/*  98 */         exp.initCause(e);
/*  99 */         throw exp;
/*     */       }
/*     */ 
/* 102 */       int classMod = clazz.getModifiers();
/*     */ 
/* 104 */       if ((!Modifier.isPublic(classMod)) || (!Modifier.isPublic(cons.getModifiers()))) {
/*     */         try
/*     */         {
/* 107 */           cons.setAccessible(true);
/*     */         }
/*     */         catch (SecurityException e) {
/* 110 */           logger.log(Level.FINE, "Unable to make the constructor of " + clazz + " accessible", e);
/* 111 */           throw e;
/*     */         }
/*     */       }
/*     */ 
/* 115 */       m.put(clazz, new WeakReference(cons));
/*     */     }
/*     */ 
/* 118 */     return cons.newInstance(emptyObject);
/*     */   }
/*     */ 
/*     */   public static <T> T create(Class<T> clazz)
/*     */   {
/*     */     try
/*     */     {
/* 127 */       return create0(clazz);
/*     */     } catch (InstantiationException e) {
/* 129 */       logger.log(Level.INFO, "failed to create a new instance of " + clazz, e);
/* 130 */       throw new InstantiationError(e.toString());
/*     */     } catch (IllegalAccessException e) {
/* 132 */       logger.log(Level.INFO, "failed to create a new instance of " + clazz, e);
/* 133 */       throw new IllegalAccessError(e.toString());
/*     */     } catch (InvocationTargetException e) {
/* 135 */       Throwable target = e.getTargetException();
/*     */ 
/* 139 */       if ((target instanceof RuntimeException)) {
/* 140 */         throw ((RuntimeException)target);
/*     */       }
/*     */ 
/* 143 */       if ((target instanceof Error)) {
/* 144 */         throw ((Error)target);
/*     */       }
/*     */ 
/* 149 */       throw new IllegalStateException(target);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object create(Method method)
/*     */   {
/*     */     Throwable errorMsg;
/*     */     try
/*     */     {
/* 159 */       return method.invoke(null, emptyObject);
/*     */     } catch (InvocationTargetException ive) {
/* 161 */       Throwable target = ive.getTargetException();
/*     */ 
/* 163 */       if ((target instanceof RuntimeException)) {
/* 164 */         throw ((RuntimeException)target);
/*     */       }
/* 166 */       if ((target instanceof Error)) {
/* 167 */         throw ((Error)target);
/*     */       }
/* 169 */       throw new IllegalStateException(target);
/*     */     } catch (IllegalAccessException e) {
/* 171 */       logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), e);
/* 172 */       throw new IllegalAccessError(e.toString());
/*     */     } catch (IllegalArgumentException iae) {
/* 174 */       logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), iae);
/* 175 */       errorMsg = iae;
/*     */     } catch (NullPointerException npe) {
/* 177 */       logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), npe);
/* 178 */       errorMsg = npe;
/*     */     } catch (ExceptionInInitializerError eie) {
/* 180 */       logger.log(Level.INFO, "failed to create a new instance of " + method.getReturnType().getName(), eie);
/* 181 */       errorMsg = eie;
/*     */     }
/*     */ 
/* 185 */     NoSuchMethodError exp = new NoSuchMethodError(errorMsg.getMessage());
/* 186 */     exp.initCause(errorMsg);
/* 187 */     throw exp;
/*     */   }
/*     */ 
/*     */   public static <T> Class<? extends T> inferImplClass(Class<T> fieldType, Class[] knownImplClasses)
/*     */   {
/* 197 */     if (!fieldType.isInterface()) {
/* 198 */       return fieldType;
/*     */     }
/* 200 */     for (Class impl : knownImplClasses) {
/* 201 */       if (fieldType.isAssignableFrom(impl)) {
/* 202 */         return impl.asSubclass(fieldType);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 208 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.ClassFactory
 * JD-Core Version:    0.6.2
 */
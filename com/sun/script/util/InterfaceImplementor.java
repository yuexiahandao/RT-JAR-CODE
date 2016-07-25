/*     */ package com.sun.script.util;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import javax.script.Invocable;
/*     */ import javax.script.ScriptException;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class InterfaceImplementor
/*     */ {
/*     */   private Invocable engine;
/*     */ 
/*     */   public InterfaceImplementor(Invocable paramInvocable)
/*     */   {
/*  46 */     this.engine = paramInvocable;
/*     */   }
/*     */ 
/*     */   public <T> T getInterface(Object paramObject, Class<T> paramClass)
/*     */     throws ScriptException
/*     */   {
/*  83 */     if ((paramClass == null) || (!paramClass.isInterface())) {
/*  84 */       throw new IllegalArgumentException("interface Class expected");
/*     */     }
/*  86 */     if (!isImplemented(paramObject, paramClass)) {
/*  87 */       return null;
/*     */     }
/*     */ 
/*  90 */     if ((System.getSecurityManager() != null) && (!Modifier.isPublic(paramClass.getModifiers())))
/*     */     {
/*  92 */       throw new SecurityException("attempt to implement non-public interface");
/*     */     }
/*     */ 
/*  96 */     ReflectUtil.checkPackageAccess(paramClass.getName());
/*     */ 
/*  98 */     AccessControlContext localAccessControlContext = AccessController.getContext();
/*  99 */     return paramClass.cast(Proxy.newProxyInstance(getLoaderForProxy(paramClass), new Class[] { paramClass }, new InterfaceImplementorInvocationHandler(paramObject, localAccessControlContext)));
/*     */   }
/*     */ 
/*     */   protected boolean isImplemented(Object paramObject, Class<?> paramClass)
/*     */   {
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */   protected Object convertResult(Method paramMethod, Object paramObject)
/*     */     throws ScriptException
/*     */   {
/* 113 */     return paramObject;
/*     */   }
/*     */ 
/*     */   protected Object[] convertArguments(Method paramMethod, Object[] paramArrayOfObject)
/*     */     throws ScriptException
/*     */   {
/* 120 */     return paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   private static ClassLoader getLoaderForProxy(Class<?> paramClass)
/*     */   {
/* 125 */     ClassLoader localClassLoader = paramClass.getClassLoader();
/*     */ 
/* 128 */     if (localClassLoader == null) {
/* 129 */       localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */     }
/*     */ 
/* 133 */     if (localClassLoader == null) {
/* 134 */       localClassLoader = ClassLoader.getSystemClassLoader();
/*     */     }
/* 136 */     return localClassLoader;
/*     */   }
/*     */ 
/*     */   private final class InterfaceImplementorInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private Object thiz;
/*     */     private AccessControlContext accCtxt;
/*     */ 
/*     */     public InterfaceImplementorInvocationHandler(Object paramAccessControlContext, AccessControlContext arg3)
/*     */     {
/*  56 */       this.thiz = paramAccessControlContext;
/*     */       Object localObject;
/*  57 */       this.accCtxt = localObject;
/*     */     }
/*     */ 
/*     */     public Object invoke(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*     */       throws Throwable
/*     */     {
/*  63 */       paramArrayOfObject = InterfaceImplementor.this.convertArguments(paramMethod, paramArrayOfObject);
/*     */ 
/*  65 */       final Method localMethod = paramMethod;
/*  66 */       final Object[] arrayOfObject = paramArrayOfObject;
/*  67 */       Object localObject = AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Object run() throws Exception {
/*  69 */           if (InterfaceImplementor.InterfaceImplementorInvocationHandler.this.thiz == null) {
/*  70 */             return InterfaceImplementor.this.engine.invokeFunction(localMethod.getName(), arrayOfObject);
/*     */           }
/*  72 */           return InterfaceImplementor.this.engine.invokeMethod(InterfaceImplementor.InterfaceImplementorInvocationHandler.this.thiz, localMethod.getName(), arrayOfObject);
/*     */         }
/*     */       }
/*     */       , this.accCtxt);
/*     */ 
/*  77 */       return InterfaceImplementor.this.convertResult(paramMethod, localObject);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.util.InterfaceImplementor
 * JD-Core Version:    0.6.2
 */
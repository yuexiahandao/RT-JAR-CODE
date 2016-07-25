/*     */ package com.sun.script.javascript;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.org.mozilla.javascript.internal.ClassShutter;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.NativeJavaObject;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ import sun.org.mozilla.javascript.internal.WrapFactory;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ final class RhinoWrapFactory extends WrapFactory
/*     */ {
/*     */   private static RhinoWrapFactory theInstance;
/*     */ 
/*     */   static synchronized WrapFactory getInstance()
/*     */   {
/*  50 */     if (theInstance == null) {
/*  51 */       theInstance = new RhinoWrapFactory();
/*     */     }
/*  53 */     return theInstance;
/*     */   }
/*     */ 
/*     */   public Scriptable wrapAsJavaObject(Context paramContext, Scriptable paramScriptable, Object paramObject, Class paramClass)
/*     */   {
/*  73 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  74 */     ClassShutter localClassShutter = RhinoClassShutter.getInstance();
/*  75 */     if ((paramObject instanceof ClassLoader))
/*     */     {
/*  78 */       if (localSecurityManager != null) {
/*  79 */         localSecurityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
/*     */       }
/*     */ 
/*  82 */       return super.wrapAsJavaObject(paramContext, paramScriptable, paramObject, paramClass);
/*     */     }
/*  84 */     Object localObject1 = null;
/*  85 */     if ((paramObject instanceof Class)) {
/*  86 */       localObject1 = ((Class)paramObject).getName();
/*  87 */     } else if ((paramObject instanceof Member)) {
/*  88 */       localObject2 = (Member)paramObject;
/*     */ 
/*  92 */       if ((localSecurityManager != null) && (!Modifier.isPublic(((Member)localObject2).getModifiers()))) {
/*  93 */         return null;
/*     */       }
/*  95 */       localObject1 = ((Member)localObject2).getDeclaringClass().getName();
/*     */     }
/*     */ 
/* 100 */     if (localObject1 != null) {
/* 101 */       if (!localClassShutter.visibleToScripts((String)localObject1)) {
/* 102 */         return null;
/*     */       }
/* 104 */       return super.wrapAsJavaObject(paramContext, paramScriptable, paramObject, paramClass);
/*     */     }
/*     */ 
/* 110 */     localObject1 = paramObject.getClass();
/* 111 */     Object localObject2 = ((Class)localObject1).getName();
/* 112 */     if (!localClassShutter.visibleToScripts((String)localObject2))
/*     */     {
/* 118 */       Object localObject3 = null;
/*     */ 
/* 125 */       if ((paramClass != null) && (paramClass.isInterface())) {
/* 126 */         localObject3 = paramClass;
/*     */       }
/*     */       else
/*     */       {
/* 130 */         while (localObject1 != null) {
/* 131 */           localObject1 = ((Class)localObject1).getSuperclass();
/* 132 */           localObject2 = ((Class)localObject1).getName();
/* 133 */           if (localClassShutter.visibleToScripts((String)localObject2)) {
/* 134 */             localObject3 = localObject1;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 140 */         assert (localObject3 != null) : "even java.lang.Object is not accessible?";
/*     */       }
/*     */ 
/* 143 */       return new RhinoJavaObject(paramScriptable, paramObject, (Class)localObject3);
/*     */     }
/* 145 */     return super.wrapAsJavaObject(paramContext, paramScriptable, paramObject, paramClass);
/*     */   }
/*     */ 
/*     */   private static class RhinoJavaObject extends NativeJavaObject
/*     */   {
/*     */     RhinoJavaObject(Scriptable paramScriptable, Object paramObject, Class paramClass)
/*     */     {
/*  63 */       super(null, paramClass);
/*     */ 
/*  67 */       this.javaObject = paramObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.javascript.RhinoWrapFactory
 * JD-Core Version:    0.6.2
 */
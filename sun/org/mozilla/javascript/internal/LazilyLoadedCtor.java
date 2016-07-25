/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public final class LazilyLoadedCtor
/*     */ {
/*     */   private static final int STATE_BEFORE_INIT = 0;
/*     */   private static final int STATE_INITIALIZING = 1;
/*     */   private static final int STATE_WITH_VALUE = 2;
/*     */   private final ScriptableObject scope;
/*     */   private final String propertyName;
/*     */   private final String className;
/*     */   private final boolean sealed;
/*     */   private final boolean privileged;
/*     */   private Object initializedValue;
/*     */   private int state;
/*     */ 
/*     */   public LazilyLoadedCtor(ScriptableObject paramScriptableObject, String paramString1, String paramString2, boolean paramBoolean)
/*     */   {
/*  67 */     this(paramScriptableObject, paramString1, paramString2, paramBoolean, false);
/*     */   }
/*     */ 
/*     */   LazilyLoadedCtor(ScriptableObject paramScriptableObject, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  74 */     this.scope = paramScriptableObject;
/*  75 */     this.propertyName = paramString1;
/*  76 */     this.className = paramString2;
/*  77 */     this.sealed = paramBoolean1;
/*  78 */     this.privileged = paramBoolean2;
/*  79 */     this.state = 0;
/*     */ 
/*  81 */     paramScriptableObject.addLazilyInitializedValue(paramString1, 0, this, 2);
/*     */   }
/*     */ 
/*     */   void init()
/*     */   {
/*  87 */     synchronized (this) {
/*  88 */       if (this.state == 1) {
/*  89 */         throw new IllegalStateException("Recursive initialization for " + this.propertyName);
/*     */       }
/*  91 */       if (this.state == 0) {
/*  92 */         this.state = 1;
/*     */ 
/*  95 */         Object localObject1 = Scriptable.NOT_FOUND;
/*     */         try {
/*  97 */           localObject1 = buildValue();
/*     */         } finally {
/*  99 */           this.initializedValue = localObject1;
/* 100 */           this.state = 2;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   Object getValue()
/*     */   {
/* 108 */     if (this.state != 2)
/* 109 */       throw new IllegalStateException(this.propertyName);
/* 110 */     return this.initializedValue;
/*     */   }
/*     */ 
/*     */   private Object buildValue()
/*     */   {
/* 115 */     if (this.privileged)
/*     */     {
/* 117 */       return AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run()
/*     */         {
/* 121 */           return LazilyLoadedCtor.this.buildValue0();
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */ 
/* 127 */     return buildValue0();
/*     */   }
/*     */ 
/*     */   private Object buildValue0()
/*     */   {
/* 133 */     Class localClass = cast(Kit.classOrNull(this.className));
/* 134 */     if (localClass != null)
/*     */       try {
/* 136 */         Object localObject = ScriptableObject.buildClassCtor(this.scope, localClass, this.sealed, false);
/*     */ 
/* 138 */         if (localObject != null) {
/* 139 */           return localObject;
/*     */         }
/*     */ 
/* 144 */         localObject = this.scope.get(this.propertyName, this.scope);
/* 145 */         if (localObject != Scriptable.NOT_FOUND)
/* 146 */           return localObject;
/*     */       }
/*     */       catch (InvocationTargetException localInvocationTargetException) {
/* 149 */         Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 150 */         if ((localThrowable instanceof RuntimeException))
/* 151 */           throw ((RuntimeException)localThrowable);
/*     */       } catch (RhinoException localRhinoException) {
/*     */       } catch (InstantiationException localInstantiationException) {
/*     */       }
/*     */       catch (IllegalAccessException localIllegalAccessException) {
/*     */       }
/*     */       catch (SecurityException localSecurityException) {
/*     */       }
/* 159 */     return Scriptable.NOT_FOUND;
/*     */   }
/*     */ 
/*     */   private Class<? extends Scriptable> cast(Class<?> paramClass)
/*     */   {
/* 164 */     return paramClass;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.LazilyLoadedCtor
 * JD-Core Version:    0.6.2
 */
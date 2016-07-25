/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public abstract class SecurityController
/*     */ {
/*     */   private static SecurityController global;
/*     */ 
/*     */   static SecurityController global()
/*     */   {
/*  75 */     return global;
/*     */   }
/*     */ 
/*     */   public static boolean hasGlobal()
/*     */   {
/*  84 */     return global != null;
/*     */   }
/*     */ 
/*     */   public static void initGlobal(SecurityController paramSecurityController)
/*     */   {
/* 101 */     if (paramSecurityController == null) throw new IllegalArgumentException();
/* 102 */     if (global != null) {
/* 103 */       throw new SecurityException("Cannot overwrite already installed global SecurityController");
/*     */     }
/* 105 */     global = paramSecurityController;
/*     */   }
/*     */ 
/*     */   public abstract GeneratedClassLoader createClassLoader(ClassLoader paramClassLoader, Object paramObject);
/*     */ 
/*     */   public static GeneratedClassLoader createLoader(ClassLoader paramClassLoader, Object paramObject)
/*     */   {
/* 136 */     Context localContext = Context.getContext();
/* 137 */     if (paramClassLoader == null) {
/* 138 */       paramClassLoader = localContext.getApplicationClassLoader();
/*     */     }
/* 140 */     SecurityController localSecurityController = localContext.getSecurityController();
/*     */     GeneratedClassLoader localGeneratedClassLoader;
/* 142 */     if (localSecurityController == null) {
/* 143 */       localGeneratedClassLoader = localContext.createClassLoader(paramClassLoader);
/*     */     } else {
/* 145 */       Object localObject = localSecurityController.getDynamicSecurityDomain(paramObject);
/* 146 */       localGeneratedClassLoader = localSecurityController.createClassLoader(paramClassLoader, localObject);
/*     */     }
/* 148 */     return localGeneratedClassLoader;
/*     */   }
/*     */ 
/*     */   public static Class<?> getStaticSecurityDomainClass() {
/* 152 */     SecurityController localSecurityController = Context.getContext().getSecurityController();
/* 153 */     return localSecurityController == null ? null : localSecurityController.getStaticSecurityDomainClassInternal();
/*     */   }
/*     */ 
/*     */   public Class<?> getStaticSecurityDomainClassInternal()
/*     */   {
/* 158 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract Object getDynamicSecurityDomain(Object paramObject);
/*     */ 
/*     */   public Object callWithDomain(Object paramObject, Context paramContext, final Callable paramCallable, Scriptable paramScriptable1, final Scriptable paramScriptable2, final Object[] paramArrayOfObject)
/*     */   {
/* 189 */     return execWithDomain(paramContext, paramScriptable1, new Script()
/*     */     {
/*     */       public Object exec(Context paramAnonymousContext, Scriptable paramAnonymousScriptable)
/*     */       {
/* 193 */         return paramCallable.call(paramAnonymousContext, paramAnonymousScriptable, paramScriptable2, paramArrayOfObject);
/*     */       }
/*     */     }
/*     */     , paramObject);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Object execWithDomain(Context paramContext, Scriptable paramScriptable, Script paramScript, Object paramObject)
/*     */   {
/* 207 */     throw new IllegalStateException("callWithDomain should be overridden");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.SecurityController
 * JD-Core Version:    0.6.2
 */
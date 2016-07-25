/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.xml.XMLLib.Factory;
/*     */ 
/*     */ public class ContextFactory
/*     */ {
/*     */   private static volatile boolean hasCustomGlobal;
/* 145 */   private static ContextFactory global = new ContextFactory();
/*     */   private volatile boolean sealed;
/* 149 */   private final Object listenersLock = new Object();
/*     */   private volatile Object listeners;
/*     */   private boolean disabledListening;
/*     */   private ClassLoader applicationClassLoader;
/*     */ 
/*     */   protected ContextFactory()
/*     */   {
/* 172 */     Context.checkRhinoDisabled();
/*     */   }
/*     */ 
/*     */   public static ContextFactory getGlobal()
/*     */   {
/* 183 */     return global;
/*     */   }
/*     */ 
/*     */   public static boolean hasExplicitGlobal()
/*     */   {
/* 197 */     return hasCustomGlobal;
/*     */   }
/*     */ 
/*     */   public static synchronized void initGlobal(ContextFactory paramContextFactory)
/*     */   {
/* 209 */     if (paramContextFactory == null) {
/* 210 */       throw new IllegalArgumentException();
/*     */     }
/* 212 */     if (hasCustomGlobal) {
/* 213 */       throw new IllegalStateException();
/*     */     }
/* 215 */     hasCustomGlobal = true;
/* 216 */     global = paramContextFactory;
/*     */   }
/*     */ 
/*     */   public static synchronized GlobalSetter getGlobalSetter()
/*     */   {
/* 225 */     if (hasCustomGlobal) {
/* 226 */       throw new IllegalStateException();
/*     */     }
/* 228 */     hasCustomGlobal = true;
/*     */ 
/* 237 */     return new GlobalSetter()
/*     */     {
/*     */       public void setContextFactoryGlobal(ContextFactory paramAnonymousContextFactory)
/*     */       {
/* 231 */         ContextFactory.access$002(paramAnonymousContextFactory == null ? new ContextFactory() : paramAnonymousContextFactory);
/*     */       }
/*     */       public ContextFactory getContextFactoryGlobal() {
/* 234 */         return ContextFactory.global;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected Context makeContext()
/*     */   {
/* 251 */     return new Context(this);
/*     */   }
/*     */ 
/*     */   protected boolean hasFeature(Context paramContext, int paramInt)
/*     */   {
/*     */     int i;
/* 262 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/* 275 */       i = paramContext.getLanguageVersion();
/* 276 */       return (i == 100) || (i == 110) || (i == 120);
/*     */     case 2:
/* 281 */       return false;
/*     */     case 3:
/* 284 */       return false;
/*     */     case 4:
/* 287 */       i = paramContext.getLanguageVersion();
/* 288 */       return i == 120;
/*     */     case 5:
/* 291 */       return true;
/*     */     case 6:
/* 294 */       i = paramContext.getLanguageVersion();
/* 295 */       return (i == 0) || (i >= 160);
/*     */     case 7:
/* 299 */       return false;
/*     */     case 8:
/* 302 */       return false;
/*     */     case 9:
/* 305 */       return false;
/*     */     case 10:
/* 308 */       return false;
/*     */     case 11:
/* 311 */       return false;
/*     */     case 12:
/* 314 */       return false;
/*     */     case 13:
/* 317 */       return false;
/*     */     }
/*     */ 
/* 320 */     throw new IllegalArgumentException(String.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   private boolean isDom3Present() {
/* 324 */     Class localClass = Kit.classOrNull("org.w3c.dom.Node");
/* 325 */     if (localClass == null) return false;
/*     */ 
/*     */     try
/*     */     {
/* 329 */       localClass.getMethod("getUserData", new Class[] { String.class });
/* 330 */       return true; } catch (NoSuchMethodException localNoSuchMethodException) {
/*     */     }
/* 332 */     return false;
/*     */   }
/*     */ 
/*     */   protected XMLLib.Factory getE4xImplementationFactory()
/*     */   {
/* 356 */     if (isDom3Present()) {
/* 357 */       return XMLLib.Factory.create("sun.org.mozilla.javascript.internal.xmlimpl.XMLLibImpl");
/*     */     }
/*     */ 
/* 360 */     if (Kit.classOrNull("org.apache.xmlbeans.XmlCursor") != null) {
/* 361 */       return XMLLib.Factory.create("sun.org.mozilla.javascript.internal.xml.impl.xmlbeans.XMLLibImpl");
/*     */     }
/*     */ 
/* 365 */     return null;
/*     */   }
/*     */ 
/*     */   protected GeneratedClassLoader createClassLoader(ClassLoader paramClassLoader)
/*     */   {
/* 381 */     return null;
/*     */   }
/*     */ 
/*     */   public final ClassLoader getApplicationClassLoader()
/*     */   {
/* 392 */     return this.applicationClassLoader;
/*     */   }
/*     */ 
/*     */   public final void initApplicationClassLoader(ClassLoader paramClassLoader)
/*     */   {
/* 402 */     if (paramClassLoader == null) {
/* 403 */       throw new IllegalArgumentException("loader is null");
/*     */     }
/*     */ 
/* 411 */     if (this.applicationClassLoader != null) {
/* 412 */       throw new IllegalStateException("applicationClassLoader can only be set once");
/*     */     }
/* 414 */     checkNotSealed();
/*     */ 
/* 416 */     this.applicationClassLoader = paramClassLoader;
/*     */   }
/*     */ 
/*     */   protected Object doTopCall(Callable paramCallable, Context paramContext, Scriptable paramScriptable1, Scriptable paramScriptable2, Object[] paramArrayOfObject)
/*     */   {
/* 430 */     return paramCallable.call(paramContext, paramScriptable1, paramScriptable2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   protected void observeInstructionCount(Context paramContext, int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void onContextCreated(Context paramContext)
/*     */   {
/* 444 */     Object localObject = this.listeners;
/* 445 */     for (int i = 0; ; i++) {
/* 446 */       Listener localListener = (Listener)Kit.getListener(localObject, i);
/* 447 */       if (localListener == null)
/*     */         break;
/* 449 */       localListener.contextCreated(paramContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void onContextReleased(Context paramContext)
/*     */   {
/* 455 */     Object localObject = this.listeners;
/* 456 */     for (int i = 0; ; i++) {
/* 457 */       Listener localListener = (Listener)Kit.getListener(localObject, i);
/* 458 */       if (localListener == null)
/*     */         break;
/* 460 */       localListener.contextReleased(paramContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void addListener(Listener paramListener)
/*     */   {
/* 466 */     checkNotSealed();
/* 467 */     synchronized (this.listenersLock) {
/* 468 */       if (this.disabledListening) {
/* 469 */         throw new IllegalStateException();
/*     */       }
/* 471 */       this.listeners = Kit.addListener(this.listeners, paramListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void removeListener(Listener paramListener)
/*     */   {
/* 477 */     checkNotSealed();
/* 478 */     synchronized (this.listenersLock) {
/* 479 */       if (this.disabledListening) {
/* 480 */         throw new IllegalStateException();
/*     */       }
/* 482 */       this.listeners = Kit.removeListener(this.listeners, paramListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   final void disableContextListening()
/*     */   {
/* 492 */     checkNotSealed();
/* 493 */     synchronized (this.listenersLock) {
/* 494 */       this.disabledListening = true;
/* 495 */       this.listeners = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean isSealed()
/*     */   {
/* 505 */     return this.sealed;
/*     */   }
/*     */ 
/*     */   public final void seal()
/*     */   {
/* 515 */     checkNotSealed();
/* 516 */     this.sealed = true;
/*     */   }
/*     */ 
/*     */   protected final void checkNotSealed()
/*     */   {
/* 521 */     if (this.sealed) throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public final Object call(ContextAction paramContextAction)
/*     */   {
/* 539 */     return Context.call(this, paramContextAction);
/*     */   }
/*     */ 
/*     */   public Context enterContext()
/*     */   {
/* 583 */     return enterContext(null);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final Context enter()
/*     */   {
/* 592 */     return enterContext(null);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void exit()
/*     */   {
/* 600 */     Context.exit();
/*     */   }
/*     */ 
/*     */   public final Context enterContext(Context paramContext)
/*     */   {
/* 620 */     return Context.enter(paramContext, this);
/*     */   }
/*     */ 
/*     */   public static abstract interface GlobalSetter
/*     */   {
/*     */     public abstract void setContextFactoryGlobal(ContextFactory paramContextFactory);
/*     */ 
/*     */     public abstract ContextFactory getContextFactoryGlobal();
/*     */   }
/*     */ 
/*     */   public static abstract interface Listener
/*     */   {
/*     */     public abstract void contextCreated(Context paramContext);
/*     */ 
/*     */     public abstract void contextReleased(Context paramContext);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ContextFactory
 * JD-Core Version:    0.6.2
 */
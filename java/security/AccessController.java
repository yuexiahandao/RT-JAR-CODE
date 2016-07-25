/*     */ package java.security;
/*     */ 
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public final class AccessController
/*     */ {
/*     */   @CallerSensitive
/*     */   public static native <T> T doPrivileged(PrivilegedAction<T> paramPrivilegedAction);
/*     */ 
/*     */   @CallerSensitive
/*     */   public static <T> T doPrivilegedWithCombiner(PrivilegedAction<T> paramPrivilegedAction)
/*     */   {
/* 296 */     AccessControlContext localAccessControlContext = getStackAccessControlContext();
/* 297 */     if (localAccessControlContext == null) {
/* 298 */       return doPrivileged(paramPrivilegedAction);
/*     */     }
/* 300 */     DomainCombiner localDomainCombiner = localAccessControlContext.getAssignedCombiner();
/* 301 */     return doPrivileged(paramPrivilegedAction, preserveCombiner(localDomainCombiner, Reflection.getCallerClass()));
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static native <T> T doPrivileged(PrivilegedAction<T> paramPrivilegedAction, AccessControlContext paramAccessControlContext);
/*     */ 
/*     */   @CallerSensitive
/*     */   public static native <T> T doPrivileged(PrivilegedExceptionAction<T> paramPrivilegedExceptionAction)
/*     */     throws PrivilegedActionException;
/*     */ 
/*     */   @CallerSensitive
/*     */   public static <T> T doPrivilegedWithCombiner(PrivilegedExceptionAction<T> paramPrivilegedExceptionAction)
/*     */     throws PrivilegedActionException
/*     */   {
/* 395 */     AccessControlContext localAccessControlContext = getStackAccessControlContext();
/* 396 */     if (localAccessControlContext == null) {
/* 397 */       return doPrivileged(paramPrivilegedExceptionAction);
/*     */     }
/* 399 */     DomainCombiner localDomainCombiner = localAccessControlContext.getAssignedCombiner();
/* 400 */     return doPrivileged(paramPrivilegedExceptionAction, preserveCombiner(localDomainCombiner, Reflection.getCallerClass()));
/*     */   }
/*     */ 
/*     */   private static AccessControlContext preserveCombiner(DomainCombiner paramDomainCombiner, Class<?> paramClass)
/*     */   {
/* 408 */     ProtectionDomain localProtectionDomain = (ProtectionDomain)doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ProtectionDomain run() {
/* 411 */         return this.val$caller.getProtectionDomain();
/*     */       }
/*     */     });
/* 417 */     ProtectionDomain[] arrayOfProtectionDomain = { localProtectionDomain };
/* 418 */     if (paramDomainCombiner == null) {
/* 419 */       return new AccessControlContext(arrayOfProtectionDomain);
/*     */     }
/* 421 */     return new AccessControlContext(paramDomainCombiner.combine(arrayOfProtectionDomain, null), paramDomainCombiner);
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static native <T> T doPrivileged(PrivilegedExceptionAction<T> paramPrivilegedExceptionAction, AccessControlContext paramAccessControlContext)
/*     */     throws PrivilegedActionException;
/*     */ 
/*     */   private static native AccessControlContext getStackAccessControlContext();
/*     */ 
/*     */   static native AccessControlContext getInheritedAccessControlContext();
/*     */ 
/*     */   public static AccessControlContext getContext()
/*     */   {
/* 495 */     AccessControlContext localAccessControlContext = getStackAccessControlContext();
/* 496 */     if (localAccessControlContext == null)
/*     */     {
/* 499 */       return new AccessControlContext(null, true);
/*     */     }
/* 501 */     return localAccessControlContext.optimize();
/*     */   }
/*     */ 
/*     */   public static void checkPermission(Permission paramPermission)
/*     */     throws AccessControlException
/*     */   {
/* 529 */     if (paramPermission == null) {
/* 530 */       throw new NullPointerException("permission can't be null");
/*     */     }
/*     */ 
/* 533 */     AccessControlContext localAccessControlContext = getStackAccessControlContext();
/*     */ 
/* 535 */     if (localAccessControlContext == null) {
/* 536 */       localObject = AccessControlContext.getDebug();
/* 537 */       int i = 0;
/* 538 */       if (localObject != null) {
/* 539 */         i = !Debug.isOn("codebase=") ? 1 : 0;
/* 540 */         i &= ((!Debug.isOn("permission=")) || (Debug.isOn("permission=" + paramPermission.getClass().getCanonicalName())) ? 1 : 0);
/*     */       }
/*     */ 
/* 544 */       if ((i != 0) && (Debug.isOn("stack"))) {
/* 545 */         Thread.currentThread(); Thread.dumpStack();
/*     */       }
/*     */ 
/* 548 */       if ((i != 0) && (Debug.isOn("domain"))) {
/* 549 */         ((Debug)localObject).println("domain (context is null)");
/*     */       }
/*     */ 
/* 552 */       if (i != 0) {
/* 553 */         ((Debug)localObject).println("access allowed " + paramPermission);
/*     */       }
/* 555 */       return;
/*     */     }
/*     */ 
/* 558 */     Object localObject = localAccessControlContext.optimize();
/* 559 */     ((AccessControlContext)localObject).checkPermission(paramPermission);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AccessController
 * JD-Core Version:    0.6.2
 */
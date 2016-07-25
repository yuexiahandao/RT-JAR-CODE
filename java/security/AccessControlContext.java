/*     */ package java.security;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public final class AccessControlContext
/*     */ {
/*     */   private ProtectionDomain[] context;
/*     */   private boolean isPrivileged;
/*  85 */   private boolean isAuthorized = false;
/*     */   private AccessControlContext privilegedContext;
/*  91 */   private DomainCombiner combiner = null;
/*     */ 
/*  93 */   private static boolean debugInit = false;
/*  94 */   private static Debug debug = null;
/*     */ 
/*     */   static Debug getDebug()
/*     */   {
/*  98 */     if (debugInit) {
/*  99 */       return debug;
/*     */     }
/* 101 */     if (Policy.isSet()) {
/* 102 */       debug = Debug.getInstance("access");
/* 103 */       debugInit = true;
/*     */     }
/* 105 */     return debug;
/*     */   }
/*     */ 
/*     */   public AccessControlContext(ProtectionDomain[] paramArrayOfProtectionDomain)
/*     */   {
/* 121 */     if (paramArrayOfProtectionDomain.length == 0) {
/* 122 */       this.context = null;
/* 123 */     } else if (paramArrayOfProtectionDomain.length == 1) {
/* 124 */       if (paramArrayOfProtectionDomain[0] != null)
/* 125 */         this.context = ((ProtectionDomain[])paramArrayOfProtectionDomain.clone());
/*     */       else
/* 127 */         this.context = null;
/*     */     }
/*     */     else {
/* 130 */       ArrayList localArrayList = new ArrayList(paramArrayOfProtectionDomain.length);
/* 131 */       for (int i = 0; i < paramArrayOfProtectionDomain.length; i++) {
/* 132 */         if ((paramArrayOfProtectionDomain[i] != null) && (!localArrayList.contains(paramArrayOfProtectionDomain[i])))
/* 133 */           localArrayList.add(paramArrayOfProtectionDomain[i]);
/*     */       }
/* 135 */       if (!localArrayList.isEmpty()) {
/* 136 */         this.context = new ProtectionDomain[localArrayList.size()];
/* 137 */         this.context = ((ProtectionDomain[])localArrayList.toArray(this.context));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public AccessControlContext(AccessControlContext paramAccessControlContext, DomainCombiner paramDomainCombiner)
/*     */   {
/* 168 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 169 */     if (localSecurityManager != null) {
/* 170 */       localSecurityManager.checkPermission(SecurityConstants.CREATE_ACC_PERMISSION);
/* 171 */       this.isAuthorized = true;
/*     */     }
/*     */ 
/* 174 */     this.context = paramAccessControlContext.context;
/*     */ 
/* 182 */     this.combiner = paramDomainCombiner;
/*     */   }
/*     */ 
/*     */   AccessControlContext(ProtectionDomain[] paramArrayOfProtectionDomain, DomainCombiner paramDomainCombiner)
/*     */   {
/* 189 */     if (paramArrayOfProtectionDomain != null) {
/* 190 */       this.context = ((ProtectionDomain[])paramArrayOfProtectionDomain.clone());
/*     */     }
/* 192 */     this.combiner = paramDomainCombiner;
/* 193 */     this.isAuthorized = true;
/*     */   }
/*     */ 
/*     */   AccessControlContext(ProtectionDomain[] paramArrayOfProtectionDomain, boolean paramBoolean)
/*     */   {
/* 203 */     this.context = paramArrayOfProtectionDomain;
/* 204 */     this.isPrivileged = paramBoolean;
/* 205 */     this.isAuthorized = true;
/*     */   }
/*     */ 
/*     */   AccessControlContext(ProtectionDomain[] paramArrayOfProtectionDomain, AccessControlContext paramAccessControlContext)
/*     */   {
/* 214 */     this.context = paramArrayOfProtectionDomain;
/* 215 */     this.privilegedContext = paramAccessControlContext;
/* 216 */     this.isPrivileged = true;
/*     */   }
/*     */ 
/*     */   ProtectionDomain[] getContext()
/*     */   {
/* 223 */     return this.context;
/*     */   }
/*     */ 
/*     */   boolean isPrivileged()
/*     */   {
/* 231 */     return this.isPrivileged;
/*     */   }
/*     */ 
/*     */   DomainCombiner getAssignedCombiner()
/*     */   {
/*     */     AccessControlContext localAccessControlContext;
/* 239 */     if (this.isPrivileged)
/* 240 */       localAccessControlContext = this.privilegedContext;
/*     */     else {
/* 242 */       localAccessControlContext = AccessController.getInheritedAccessControlContext();
/*     */     }
/* 244 */     if (localAccessControlContext != null) {
/* 245 */       return localAccessControlContext.combiner;
/*     */     }
/* 247 */     return null;
/*     */   }
/*     */ 
/*     */   public DomainCombiner getDomainCombiner()
/*     */   {
/* 267 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 268 */     if (localSecurityManager != null) {
/* 269 */       localSecurityManager.checkPermission(SecurityConstants.GET_COMBINER_PERMISSION);
/*     */     }
/* 271 */     return this.combiner;
/*     */   }
/*     */ 
/*     */   public void checkPermission(Permission paramPermission)
/*     */     throws AccessControlException
/*     */   {
/* 296 */     int i = 0;
/*     */ 
/* 298 */     if (paramPermission == null) {
/* 299 */       throw new NullPointerException("permission can't be null");
/*     */     }
/* 301 */     if (getDebug() != null)
/*     */     {
/* 303 */       i = !Debug.isOn("codebase=") ? 1 : 0;
/* 304 */       if (i == 0)
/*     */       {
/* 307 */         for (j = 0; (this.context != null) && (j < this.context.length); j++) {
/* 308 */           if ((this.context[j].getCodeSource() != null) && (this.context[j].getCodeSource().getLocation() != null) && (Debug.isOn("codebase=" + this.context[j].getCodeSource().getLocation().toString())))
/*     */           {
/* 311 */             i = 1;
/* 312 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 317 */       i &= ((!Debug.isOn("permission=")) || (Debug.isOn("permission=" + paramPermission.getClass().getCanonicalName())) ? 1 : 0);
/*     */ 
/* 320 */       if ((i != 0) && (Debug.isOn("stack"))) {
/* 321 */         Thread.currentThread(); Thread.dumpStack();
/*     */       }
/*     */ 
/* 324 */       if ((i != 0) && (Debug.isOn("domain"))) {
/* 325 */         if (this.context == null)
/* 326 */           debug.println("domain (context is null)");
/*     */         else {
/* 328 */           for (j = 0; j < this.context.length; j++) {
/* 329 */             debug.println("domain " + j + " " + this.context[j]);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 346 */     if (this.context == null) {
/* 347 */       return;
/*     */     }
/* 349 */     for (int j = 0; j < this.context.length; j++) {
/* 350 */       if ((this.context[j] != null) && (!this.context[j].implies(paramPermission))) {
/* 351 */         if (i != 0) {
/* 352 */           debug.println("access denied " + paramPermission);
/*     */         }
/*     */ 
/* 355 */         if ((Debug.isOn("failure")) && (debug != null))
/*     */         {
/* 359 */           if (i == 0) {
/* 360 */             debug.println("access denied " + paramPermission);
/*     */           }
/* 362 */           Thread.currentThread(); Thread.dumpStack();
/* 363 */           final ProtectionDomain localProtectionDomain = this.context[j];
/* 364 */           final Debug localDebug = debug;
/* 365 */           AccessController.doPrivileged(new PrivilegedAction() {
/*     */             public Void run() {
/* 367 */               localDebug.println("domain that failed " + localProtectionDomain);
/* 368 */               return null;
/*     */             }
/*     */           });
/*     */         }
/* 372 */         throw new AccessControlException("access denied " + paramPermission, paramPermission);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 377 */     if (i != 0)
/* 378 */       debug.println("access allowed " + paramPermission);
/*     */   }
/*     */ 
/*     */   AccessControlContext optimize()
/*     */   {
/*     */     AccessControlContext localAccessControlContext;
/* 391 */     if (this.isPrivileged)
/* 392 */       localAccessControlContext = this.privilegedContext;
/*     */     else {
/* 394 */       localAccessControlContext = AccessController.getInheritedAccessControlContext();
/*     */     }
/*     */ 
/* 399 */     int i = this.context == null ? 1 : 0;
/*     */ 
/* 403 */     int j = (localAccessControlContext == null) || (localAccessControlContext.context == null) ? 1 : 0;
/*     */ 
/* 405 */     if ((localAccessControlContext != null) && (localAccessControlContext.combiner != null))
/*     */     {
/* 407 */       return goCombiner(this.context, localAccessControlContext);
/*     */     }
/*     */ 
/* 412 */     if ((j != 0) && (i != 0)) {
/* 413 */       return this;
/*     */     }
/*     */ 
/* 418 */     if (i != 0) {
/* 419 */       return localAccessControlContext;
/*     */     }
/*     */ 
/* 422 */     int k = this.context.length;
/*     */ 
/* 427 */     if ((j != 0) && (k <= 2)) {
/* 428 */       return this;
/*     */     }
/*     */ 
/* 433 */     if ((k == 1) && (this.context[0] == localAccessControlContext.context[0])) {
/* 434 */       return localAccessControlContext;
/*     */     }
/*     */ 
/* 437 */     int m = j != 0 ? 0 : localAccessControlContext.context.length;
/*     */ 
/* 440 */     Object localObject = new ProtectionDomain[k + m];
/*     */ 
/* 443 */     if (j == 0) {
/* 444 */       System.arraycopy(localAccessControlContext.context, 0, localObject, 0, m);
/*     */     }
/*     */ 
/* 449 */     label236: for (int n = 0; n < this.context.length; n++) {
/* 450 */       ProtectionDomain localProtectionDomain = this.context[n];
/* 451 */       if (localProtectionDomain != null) {
/* 452 */         for (int i1 = 0; i1 < m; i1++) {
/* 453 */           if (localProtectionDomain == localObject[i1]) {
/*     */             break label236;
/*     */           }
/*     */         }
/* 457 */         localObject[(m++)] = localProtectionDomain;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 462 */     if (m != localObject.length)
/*     */     {
/* 464 */       if ((j == 0) && (m == localAccessControlContext.context.length))
/* 465 */         return localAccessControlContext;
/* 466 */       if ((j != 0) && (m == k)) {
/* 467 */         return this;
/*     */       }
/* 469 */       ProtectionDomain[] arrayOfProtectionDomain = new ProtectionDomain[m];
/* 470 */       System.arraycopy(localObject, 0, arrayOfProtectionDomain, 0, m);
/* 471 */       localObject = arrayOfProtectionDomain;
/*     */     }
/*     */ 
/* 478 */     this.context = ((ProtectionDomain[])localObject);
/* 479 */     this.combiner = null;
/* 480 */     this.isPrivileged = false;
/*     */ 
/* 482 */     return this;
/*     */   }
/*     */ 
/*     */   private AccessControlContext goCombiner(ProtectionDomain[] paramArrayOfProtectionDomain, AccessControlContext paramAccessControlContext)
/*     */   {
/* 493 */     if (getDebug() != null) {
/* 494 */       debug.println("AccessControlContext invoking the Combiner");
/*     */     }
/*     */ 
/* 499 */     ProtectionDomain[] arrayOfProtectionDomain = paramAccessControlContext.combiner.combine(paramArrayOfProtectionDomain, paramAccessControlContext.context);
/*     */ 
/* 505 */     this.context = arrayOfProtectionDomain;
/* 506 */     this.combiner = paramAccessControlContext.combiner;
/* 507 */     this.isPrivileged = false;
/* 508 */     this.isAuthorized = paramAccessControlContext.isAuthorized;
/*     */ 
/* 510 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 524 */     if (paramObject == this) {
/* 525 */       return true;
/*     */     }
/* 527 */     if (!(paramObject instanceof AccessControlContext)) {
/* 528 */       return false;
/*     */     }
/* 530 */     AccessControlContext localAccessControlContext = (AccessControlContext)paramObject;
/*     */ 
/* 533 */     if (this.context == null) {
/* 534 */       return localAccessControlContext.context == null;
/*     */     }
/*     */ 
/* 537 */     if (localAccessControlContext.context == null) {
/* 538 */       return false;
/*     */     }
/* 540 */     if ((!containsAllPDs(localAccessControlContext)) || (!localAccessControlContext.containsAllPDs(this))) {
/* 541 */       return false;
/*     */     }
/* 543 */     if (this.combiner == null) {
/* 544 */       return localAccessControlContext.combiner == null;
/*     */     }
/* 546 */     if (localAccessControlContext.combiner == null) {
/* 547 */       return false;
/*     */     }
/* 549 */     if (!this.combiner.equals(localAccessControlContext.combiner)) {
/* 550 */       return false;
/*     */     }
/* 552 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean containsAllPDs(AccessControlContext paramAccessControlContext) {
/* 556 */     boolean bool = false;
/*     */ 
/* 564 */     for (int i = 0; i < this.context.length; i++) {
/* 565 */       bool = false;
/*     */       ProtectionDomain localProtectionDomain1;
/* 566 */       if ((localProtectionDomain1 = this.context[i]) == null) {
/* 567 */         for (int j = 0; (j < paramAccessControlContext.context.length) && (!bool); j++)
/* 568 */           bool = paramAccessControlContext.context[j] == null;
/*     */       }
/*     */       else {
/* 571 */         Class localClass = localProtectionDomain1.getClass();
/*     */ 
/* 573 */         for (int k = 0; (k < paramAccessControlContext.context.length) && (!bool); k++) {
/* 574 */           ProtectionDomain localProtectionDomain2 = paramAccessControlContext.context[k];
/*     */ 
/* 577 */           bool = (localProtectionDomain2 != null) && (localClass == localProtectionDomain2.getClass()) && (localProtectionDomain1.equals(localProtectionDomain2));
/*     */         }
/*     */       }
/*     */ 
/* 581 */       if (!bool) return false;
/*     */     }
/* 583 */     return bool;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 594 */     int i = 0;
/*     */ 
/* 596 */     if (this.context == null) {
/* 597 */       return i;
/*     */     }
/* 599 */     for (int j = 0; j < this.context.length; j++) {
/* 600 */       if (this.context[j] != null)
/* 601 */         i ^= this.context[j].hashCode();
/*     */     }
/* 603 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.AccessControlContext
 * JD-Core Version:    0.6.2
 */
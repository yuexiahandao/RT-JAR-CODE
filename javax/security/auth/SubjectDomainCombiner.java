/*     */ package javax.security.auth;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.CodeSource;
/*     */ import java.security.DomainCombiner;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.Permissions;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.security.Security;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public class SubjectDomainCombiner
/*     */   implements DomainCombiner
/*     */ {
/*     */   private Subject subject;
/*  50 */   private WeakKeyValueMap<ProtectionDomain, ProtectionDomain> cachedPDs = new WeakKeyValueMap(null);
/*     */   private Set<Principal> principalSet;
/*     */   private Principal[] principals;
/*  55 */   private static final Debug debug = Debug.getInstance("combiner", "\t[SubjectDomainCombiner]");
/*     */ 
/*  60 */   private static final boolean useJavaxPolicy = Policy.isCustomPolicySet(debug);
/*     */ 
/*  64 */   private static final boolean allowCaching = (useJavaxPolicy) && (cachePolicy());
/*     */ 
/*     */   public SubjectDomainCombiner(Subject paramSubject)
/*     */   {
/*  77 */     this.subject = paramSubject;
/*     */ 
/*  79 */     if (paramSubject.isReadOnly()) {
/*  80 */       this.principalSet = paramSubject.getPrincipals();
/*  81 */       this.principals = ((Principal[])this.principalSet.toArray(new Principal[this.principalSet.size()]));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Subject getSubject()
/*     */   {
/* 102 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 103 */     if (localSecurityManager != null) {
/* 104 */       localSecurityManager.checkPermission(new AuthPermission("getSubjectFromDomainCombiner"));
/*     */     }
/*     */ 
/* 107 */     return this.subject;
/*     */   }
/*     */ 
/*     */   public ProtectionDomain[] combine(ProtectionDomain[] paramArrayOfProtectionDomain1, ProtectionDomain[] paramArrayOfProtectionDomain2)
/*     */   {
/* 158 */     if (debug != null) {
/* 159 */       if (this.subject == null) {
/* 160 */         debug.println("null subject");
/*     */       } else {
/* 162 */         final Subject localSubject = this.subject;
/* 163 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run() {
/* 166 */             SubjectDomainCombiner.debug.println(localSubject.toString());
/* 167 */             return null;
/*     */           }
/*     */         });
/*     */       }
/* 171 */       printInputDomains(paramArrayOfProtectionDomain1, paramArrayOfProtectionDomain2);
/*     */     }
/*     */ 
/* 174 */     if ((paramArrayOfProtectionDomain1 == null) || (paramArrayOfProtectionDomain1.length == 0))
/*     */     {
/* 184 */       return paramArrayOfProtectionDomain2;
/*     */     }
/*     */ 
/* 192 */     paramArrayOfProtectionDomain1 = optimize(paramArrayOfProtectionDomain1);
/* 193 */     if (debug != null) {
/* 194 */       debug.println("after optimize");
/* 195 */       printInputDomains(paramArrayOfProtectionDomain1, paramArrayOfProtectionDomain2);
/*     */     }
/*     */ 
/* 198 */     if ((paramArrayOfProtectionDomain1 == null) && (paramArrayOfProtectionDomain2 == null)) {
/* 199 */       return null;
/*     */     }
/*     */ 
/* 204 */     if (useJavaxPolicy) {
/* 205 */       return combineJavaxPolicy(paramArrayOfProtectionDomain1, paramArrayOfProtectionDomain2);
/*     */     }
/*     */ 
/* 208 */     Object localObject1 = paramArrayOfProtectionDomain1 == null ? 0 : paramArrayOfProtectionDomain1.length;
/* 209 */     Object localObject2 = paramArrayOfProtectionDomain2 == null ? 0 : paramArrayOfProtectionDomain2.length;
/*     */ 
/* 213 */     ProtectionDomain[] arrayOfProtectionDomain = new ProtectionDomain[localObject1 + localObject2];
/*     */ 
/* 215 */     int i = 1;
/* 216 */     synchronized (this.cachedPDs)
/*     */     {
/*     */       Object localObject3;
/* 217 */       if ((!this.subject.isReadOnly()) && (!this.subject.getPrincipals().equals(this.principalSet)))
/*     */       {
/* 221 */         localObject3 = this.subject.getPrincipals();
/* 222 */         synchronized (localObject3) {
/* 223 */           this.principalSet = new HashSet((Collection)localObject3);
/*     */         }
/* 225 */         this.principals = ((Principal[])this.principalSet.toArray(new Principal[this.principalSet.size()]));
/*     */ 
/* 227 */         this.cachedPDs.clear();
/*     */ 
/* 229 */         if (debug != null) {
/* 230 */           debug.println("Subject mutated - clearing cache");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 235 */       for (??? = 0; ??? < localObject1; ???++) {
/* 236 */         ProtectionDomain localProtectionDomain = paramArrayOfProtectionDomain1[???];
/*     */ 
/* 238 */         localObject3 = (ProtectionDomain)this.cachedPDs.getValue(localProtectionDomain);
/*     */ 
/* 240 */         if (localObject3 == null) {
/* 241 */           localObject3 = new ProtectionDomain(localProtectionDomain.getCodeSource(), localProtectionDomain.getPermissions(), localProtectionDomain.getClassLoader(), this.principals);
/*     */ 
/* 245 */           this.cachedPDs.putValue(localProtectionDomain, localObject3);
/*     */         } else {
/* 247 */           i = 0;
/*     */         }
/* 249 */         arrayOfProtectionDomain[???] = localObject3;
/*     */       }
/*     */     }
/*     */ 
/* 253 */     if (debug != null) {
/* 254 */       debug.println("updated current: ");
/* 255 */       for (??? = 0; ??? < localObject1; ???++) {
/* 256 */         debug.println("\tupdated[" + ??? + "] = " + printDomain(arrayOfProtectionDomain[???]));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 262 */     if (localObject2 > 0) {
/* 263 */       System.arraycopy(paramArrayOfProtectionDomain2, 0, arrayOfProtectionDomain, localObject1, localObject2);
/*     */ 
/* 266 */       if (i == 0) {
/* 267 */         arrayOfProtectionDomain = optimize(arrayOfProtectionDomain);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 273 */     if (debug != null) {
/* 274 */       if ((arrayOfProtectionDomain == null) || (arrayOfProtectionDomain.length == 0)) {
/* 275 */         debug.println("returning null");
/*     */       } else {
/* 277 */         debug.println("combinedDomains: ");
/* 278 */         for (int j = 0; j < arrayOfProtectionDomain.length; j++) {
/* 279 */           debug.println("newDomain " + j + ": " + printDomain(arrayOfProtectionDomain[j]));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 286 */     if ((arrayOfProtectionDomain == null) || (arrayOfProtectionDomain.length == 0)) {
/* 287 */       return null;
/*     */     }
/* 289 */     return arrayOfProtectionDomain;
/*     */   }
/*     */ 
/*     */   private ProtectionDomain[] combineJavaxPolicy(ProtectionDomain[] paramArrayOfProtectionDomain1, ProtectionDomain[] paramArrayOfProtectionDomain2)
/*     */   {
/* 300 */     if (!allowCaching) {
/* 301 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Void run()
/*     */         {
/* 305 */           Policy.getPolicy().refresh();
/* 306 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 311 */     Object localObject1 = paramArrayOfProtectionDomain1 == null ? 0 : paramArrayOfProtectionDomain1.length;
/* 312 */     Object localObject2 = paramArrayOfProtectionDomain2 == null ? 0 : paramArrayOfProtectionDomain2.length;
/*     */ 
/* 316 */     ProtectionDomain[] arrayOfProtectionDomain = new ProtectionDomain[localObject1 + localObject2];
/*     */ 
/* 318 */     synchronized (this.cachedPDs) {
/* 319 */       if ((!this.subject.isReadOnly()) && (!this.subject.getPrincipals().equals(this.principalSet)))
/*     */       {
/* 323 */         Set localSet = this.subject.getPrincipals();
/* 324 */         synchronized (localSet) {
/* 325 */           this.principalSet = new HashSet(localSet);
/*     */         }
/* 327 */         this.principals = ((Principal[])this.principalSet.toArray(new Principal[this.principalSet.size()]));
/*     */ 
/* 329 */         this.cachedPDs.clear();
/*     */ 
/* 331 */         if (debug != null) {
/* 332 */           debug.println("Subject mutated - clearing cache");
/*     */         }
/*     */       }
/*     */ 
/* 336 */       for (Object localObject3 = 0; localObject3 < localObject1; localObject3++) {
/* 337 */         ??? = paramArrayOfProtectionDomain1[localObject3];
/* 338 */         ProtectionDomain localProtectionDomain = (ProtectionDomain)this.cachedPDs.getValue(???);
/*     */ 
/* 340 */         if (localProtectionDomain == null)
/*     */         {
/* 349 */           Permissions localPermissions = new Permissions();
/* 350 */           PermissionCollection localPermissionCollection1 = ((ProtectionDomain)???).getPermissions();
/*     */           Enumeration localEnumeration;
/* 352 */           if (localPermissionCollection1 != null) {
/* 353 */             synchronized (localPermissionCollection1) {
/* 354 */               localEnumeration = localPermissionCollection1.elements();
/* 355 */               while (localEnumeration.hasMoreElements()) {
/* 356 */                 localObject5 = (Permission)localEnumeration.nextElement();
/*     */ 
/* 358 */                 localPermissions.add((Permission)localObject5);
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 365 */           ??? = ((ProtectionDomain)???).getCodeSource();
/* 366 */           Object localObject5 = this.subject;
/* 367 */           PermissionCollection localPermissionCollection2 = (PermissionCollection)AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public PermissionCollection run()
/*     */             {
/* 371 */               return Policy.getPolicy().getPermissions(this.val$finalS, this.val$finalCs);
/*     */             }
/*     */           });
/* 379 */           synchronized (localPermissionCollection2) {
/* 380 */             localEnumeration = localPermissionCollection2.elements();
/* 381 */             while (localEnumeration.hasMoreElements()) {
/* 382 */               Permission localPermission = (Permission)localEnumeration.nextElement();
/* 383 */               if (!localPermissions.implies(localPermission)) {
/* 384 */                 localPermissions.add(localPermission);
/* 385 */                 if (debug != null) {
/* 386 */                   debug.println("Adding perm " + localPermission + "\n");
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 391 */           localProtectionDomain = new ProtectionDomain((CodeSource)???, localPermissions, ((ProtectionDomain)???).getClassLoader(), this.principals);
/*     */ 
/* 394 */           if (allowCaching)
/* 395 */             this.cachedPDs.putValue(???, localProtectionDomain);
/*     */         }
/* 397 */         arrayOfProtectionDomain[localObject3] = localProtectionDomain;
/*     */       }
/*     */     }
/*     */ 
/* 401 */     if (debug != null) {
/* 402 */       debug.println("updated current: ");
/* 403 */       for (??? = 0; ??? < localObject1; ???++) {
/* 404 */         debug.println("\tupdated[" + ??? + "] = " + arrayOfProtectionDomain[???]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 409 */     if (localObject2 > 0) {
/* 410 */       System.arraycopy(paramArrayOfProtectionDomain2, 0, arrayOfProtectionDomain, localObject1, localObject2);
/*     */     }
/*     */ 
/* 413 */     if (debug != null) {
/* 414 */       if ((arrayOfProtectionDomain == null) || (arrayOfProtectionDomain.length == 0)) {
/* 415 */         debug.println("returning null");
/*     */       } else {
/* 417 */         debug.println("combinedDomains: ");
/* 418 */         for (int i = 0; i < arrayOfProtectionDomain.length; i++) {
/* 419 */           debug.println("newDomain " + i + ": " + arrayOfProtectionDomain[i].toString());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 426 */     if ((arrayOfProtectionDomain == null) || (arrayOfProtectionDomain.length == 0)) {
/* 427 */       return null;
/*     */     }
/* 429 */     return arrayOfProtectionDomain;
/*     */   }
/*     */ 
/*     */   private static ProtectionDomain[] optimize(ProtectionDomain[] paramArrayOfProtectionDomain)
/*     */   {
/* 434 */     if ((paramArrayOfProtectionDomain == null) || (paramArrayOfProtectionDomain.length == 0)) {
/* 435 */       return null;
/*     */     }
/* 437 */     Object localObject = new ProtectionDomain[paramArrayOfProtectionDomain.length];
/*     */ 
/* 439 */     int i = 0;
/* 440 */     for (int j = 0; j < paramArrayOfProtectionDomain.length; j++)
/*     */     {
/*     */       ProtectionDomain localProtectionDomain;
/* 449 */       if ((localProtectionDomain = paramArrayOfProtectionDomain[j]) != null)
/*     */       {
/* 452 */         int k = 0;
/* 453 */         for (int m = 0; (m < i) && (k == 0); m++) {
/* 454 */           k = localObject[m] == localProtectionDomain ? 1 : 0;
/*     */         }
/* 456 */         if (k == 0) {
/* 457 */           localObject[(i++)] = localProtectionDomain;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 463 */     if ((i > 0) && (i < paramArrayOfProtectionDomain.length)) {
/* 464 */       ProtectionDomain[] arrayOfProtectionDomain = new ProtectionDomain[i];
/* 465 */       System.arraycopy(localObject, 0, arrayOfProtectionDomain, 0, arrayOfProtectionDomain.length);
/* 466 */       localObject = arrayOfProtectionDomain;
/*     */     }
/*     */ 
/* 469 */     return (i == 0) || (localObject.length == 0) ? null : localObject;
/*     */   }
/*     */ 
/*     */   private static boolean cachePolicy() {
/* 473 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run() {
/* 476 */         return Security.getProperty("cache.auth.policy");
/*     */       }
/*     */     });
/* 479 */     if (str != null) {
/* 480 */       return Boolean.parseBoolean(str);
/*     */     }
/*     */ 
/* 484 */     return true;
/*     */   }
/*     */ 
/*     */   private static void printInputDomains(ProtectionDomain[] paramArrayOfProtectionDomain1, ProtectionDomain[] paramArrayOfProtectionDomain2)
/*     */   {
/*     */     int i;
/* 489 */     if ((paramArrayOfProtectionDomain1 == null) || (paramArrayOfProtectionDomain1.length == 0))
/* 490 */       debug.println("currentDomains null or 0 length");
/*     */     else {
/* 492 */       for (i = 0; (paramArrayOfProtectionDomain1 != null) && (i < paramArrayOfProtectionDomain1.length); 
/* 493 */         i++) {
/* 494 */         if (paramArrayOfProtectionDomain1[i] == null)
/* 495 */           debug.println("currentDomain " + i + ": SystemDomain");
/*     */         else {
/* 497 */           debug.println("currentDomain " + i + ": " + printDomain(paramArrayOfProtectionDomain1[i]));
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 503 */     if ((paramArrayOfProtectionDomain2 == null) || (paramArrayOfProtectionDomain2.length == 0)) {
/* 504 */       debug.println("assignedDomains null or 0 length");
/*     */     } else {
/* 506 */       debug.println("assignedDomains = ");
/* 507 */       for (i = 0; (paramArrayOfProtectionDomain2 != null) && (i < paramArrayOfProtectionDomain2.length); 
/* 508 */         i++)
/* 509 */         if (paramArrayOfProtectionDomain2[i] == null)
/* 510 */           debug.println("assignedDomain " + i + ": SystemDomain");
/*     */         else
/* 512 */           debug.println("assignedDomain " + i + ": " + printDomain(paramArrayOfProtectionDomain2[i]));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String printDomain(ProtectionDomain paramProtectionDomain)
/*     */   {
/* 520 */     if (paramProtectionDomain == null) {
/* 521 */       return "null";
/*     */     }
/* 523 */     return (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/* 525 */         return this.val$pd.toString();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static class WeakKeyValueMap<K, V> extends WeakHashMap<K, WeakReference<V>>
/*     */   {
/*     */     public V getValue(K paramK)
/*     */     {
/* 550 */       WeakReference localWeakReference = (WeakReference)super.get(paramK);
/* 551 */       if (localWeakReference != null) {
/* 552 */         return localWeakReference.get();
/*     */       }
/* 554 */       return null;
/*     */     }
/*     */ 
/*     */     public V putValue(K paramK, V paramV) {
/* 558 */       WeakReference localWeakReference = (WeakReference)super.put(paramK, new WeakReference(paramV));
/* 559 */       if (localWeakReference != null) {
/* 560 */         return localWeakReference.get();
/*     */       }
/* 562 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.SubjectDomainCombiner
 * JD-Core Version:    0.6.2
 */
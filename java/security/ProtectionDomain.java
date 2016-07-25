/*     */ package java.security;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import sun.misc.JavaSecurityAccess;
/*     */ import sun.misc.JavaSecurityProtectionDomainAccess;
/*     */ import sun.misc.JavaSecurityProtectionDomainAccess.ProtectionDomainCache;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public class ProtectionDomain
/*     */ {
/*     */   private CodeSource codesource;
/*     */   private ClassLoader classloader;
/*     */   private Principal[] principals;
/*     */   private PermissionCollection permissions;
/* 107 */   private boolean hasAllPerm = false;
/*     */   private boolean staticPermissions;
/* 116 */   final Key key = new Key();
/*     */   private static final Debug debug;
/*     */ 
/*     */   public ProtectionDomain(CodeSource paramCodeSource, PermissionCollection paramPermissionCollection)
/*     */   {
/* 132 */     this.codesource = paramCodeSource;
/* 133 */     if (paramPermissionCollection != null) {
/* 134 */       this.permissions = paramPermissionCollection;
/* 135 */       this.permissions.setReadOnly();
/* 136 */       if (((paramPermissionCollection instanceof Permissions)) && (((Permissions)paramPermissionCollection).allPermission != null))
/*     */       {
/* 138 */         this.hasAllPerm = true;
/*     */       }
/*     */     }
/* 141 */     this.classloader = null;
/* 142 */     this.principals = new Principal[0];
/* 143 */     this.staticPermissions = true;
/*     */   }
/*     */ 
/*     */   public ProtectionDomain(CodeSource paramCodeSource, PermissionCollection paramPermissionCollection, ClassLoader paramClassLoader, Principal[] paramArrayOfPrincipal)
/*     */   {
/* 179 */     this.codesource = paramCodeSource;
/* 180 */     if (paramPermissionCollection != null) {
/* 181 */       this.permissions = paramPermissionCollection;
/* 182 */       this.permissions.setReadOnly();
/* 183 */       if (((paramPermissionCollection instanceof Permissions)) && (((Permissions)paramPermissionCollection).allPermission != null))
/*     */       {
/* 185 */         this.hasAllPerm = true;
/*     */       }
/*     */     }
/* 188 */     this.classloader = paramClassLoader;
/* 189 */     this.principals = (paramArrayOfPrincipal != null ? (Principal[])paramArrayOfPrincipal.clone() : new Principal[0]);
/*     */ 
/* 191 */     this.staticPermissions = false;
/*     */   }
/*     */ 
/*     */   public final CodeSource getCodeSource()
/*     */   {
/* 200 */     return this.codesource;
/*     */   }
/*     */ 
/*     */   public final ClassLoader getClassLoader()
/*     */   {
/* 211 */     return this.classloader;
/*     */   }
/*     */ 
/*     */   public final Principal[] getPrincipals()
/*     */   {
/* 223 */     return (Principal[])this.principals.clone();
/*     */   }
/*     */ 
/*     */   public final PermissionCollection getPermissions()
/*     */   {
/* 234 */     return this.permissions;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 266 */     if (this.hasAllPerm)
/*     */     {
/* 269 */       return true;
/*     */     }
/*     */ 
/* 272 */     if ((!this.staticPermissions) && (Policy.getPolicyNoCheck().implies(this, paramPermission)))
/*     */     {
/* 274 */       return true;
/* 275 */     }if (this.permissions != null) {
/* 276 */       return this.permissions.implies(paramPermission);
/*     */     }
/* 278 */     return false;
/*     */   }
/*     */ 
/*     */   boolean impliesCreateAccessControlContext()
/*     */   {
/* 283 */     return implies(SecurityConstants.CREATE_ACC_PERMISSION);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 290 */     String str = "<no principals>";
/* 291 */     if ((this.principals != null) && (this.principals.length > 0)) {
/* 292 */       localObject = new StringBuilder("(principals ");
/*     */ 
/* 294 */       for (int i = 0; i < this.principals.length; i++) {
/* 295 */         ((StringBuilder)localObject).append(this.principals[i].getClass().getName() + " \"" + this.principals[i].getName() + "\"");
/*     */ 
/* 298 */         if (i < this.principals.length - 1)
/* 299 */           ((StringBuilder)localObject).append(",\n");
/*     */         else
/* 301 */           ((StringBuilder)localObject).append(")\n");
/*     */       }
/* 303 */       str = ((StringBuilder)localObject).toString();
/*     */     }
/*     */ 
/* 308 */     Object localObject = (Policy.isSet()) && (seeAllp()) ? mergePermissions() : getPermissions();
/*     */ 
/* 312 */     return "ProtectionDomain  " + this.codesource + "\n" + " " + this.classloader + "\n" + " " + str + "\n" + " " + localObject + "\n";
/*     */   }
/*     */ 
/*     */   private static boolean seeAllp()
/*     */   {
/* 335 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*     */ 
/* 337 */     if (localSecurityManager == null) {
/* 338 */       return true;
/*     */     }
/* 340 */     if (debug != null) {
/* 341 */       if ((localSecurityManager.getClass().getClassLoader() == null) && (Policy.getPolicyNoCheck().getClass().getClassLoader() == null))
/*     */       {
/* 344 */         return true;
/*     */       }
/*     */     }
/*     */     else try {
/* 348 */         localSecurityManager.checkPermission(SecurityConstants.GET_POLICY_PERMISSION);
/* 349 */         return true;
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/*     */ 
/*     */ 
/* 356 */     return false;
/*     */   }
/*     */ 
/*     */   private PermissionCollection mergePermissions() {
/* 360 */     if (this.staticPermissions) {
/* 361 */       return this.permissions;
/*     */     }
/* 363 */     PermissionCollection localPermissionCollection = (PermissionCollection)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public PermissionCollection run()
/*     */       {
/* 367 */         Policy localPolicy = Policy.getPolicyNoCheck();
/* 368 */         return localPolicy.getPermissions(ProtectionDomain.this);
/*     */       }
/*     */     });
/* 372 */     Permissions localPermissions = new Permissions();
/* 373 */     int i = 32;
/* 374 */     int j = 8;
/*     */ 
/* 376 */     ArrayList localArrayList1 = new ArrayList(j);
/* 377 */     ArrayList localArrayList2 = new ArrayList(i);
/*     */     Enumeration localEnumeration;
/* 381 */     if (this.permissions != null) {
/* 382 */       synchronized (this.permissions) {
/* 383 */         localEnumeration = this.permissions.elements();
/* 384 */         while (localEnumeration.hasMoreElements()) {
/* 385 */           localArrayList1.add(localEnumeration.nextElement());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 392 */     if (localPermissionCollection != null) {
/* 393 */       synchronized (localPermissionCollection) {
/* 394 */         localEnumeration = localPermissionCollection.elements();
/* 395 */         while (localEnumeration.hasMoreElements()) {
/* 396 */           localArrayList2.add(localEnumeration.nextElement());
/* 397 */           j++;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 402 */     if ((localPermissionCollection != null) && (this.permissions != null))
/*     */     {
/* 407 */       synchronized (this.permissions) {
/* 408 */         localEnumeration = this.permissions.elements();
/* 409 */         while (localEnumeration.hasMoreElements()) {
/* 410 */           Permission localPermission1 = (Permission)localEnumeration.nextElement();
/* 411 */           Class localClass = localPermission1.getClass();
/* 412 */           String str1 = localPermission1.getActions();
/* 413 */           String str2 = localPermission1.getName();
/* 414 */           for (int m = 0; m < localArrayList2.size(); m++) {
/* 415 */             Permission localPermission2 = (Permission)localArrayList2.get(m);
/* 416 */             if (localClass.isInstance(localPermission2))
/*     */             {
/* 420 */               if ((str2.equals(localPermission2.getName())) && (str1.equals(localPermission2.getActions())))
/*     */               {
/* 422 */                 localArrayList2.remove(m);
/* 423 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     int k;
/* 431 */     if (localPermissionCollection != null)
/*     */     {
/* 435 */       for (k = localArrayList2.size() - 1; k >= 0; k--) {
/* 436 */         localPermissions.add((Permission)localArrayList2.get(k));
/*     */       }
/*     */     }
/* 439 */     if (this.permissions != null) {
/* 440 */       for (k = localArrayList1.size() - 1; k >= 0; k--) {
/* 441 */         localPermissions.add((Permission)localArrayList1.get(k));
/*     */       }
/*     */     }
/*     */ 
/* 445 */     return localPermissions;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  66 */     SharedSecrets.setJavaSecurityAccess(new JavaSecurityAccess()
/*     */     {
/*     */       public <T> T doIntersectionPrivilege(PrivilegedAction<T> paramAnonymousPrivilegedAction, AccessControlContext paramAnonymousAccessControlContext1, AccessControlContext paramAnonymousAccessControlContext2)
/*     */       {
/*  73 */         if (paramAnonymousPrivilegedAction == null) {
/*  74 */           throw new NullPointerException();
/*     */         }
/*  76 */         return AccessController.doPrivileged(paramAnonymousPrivilegedAction, new AccessControlContext(paramAnonymousAccessControlContext1.getContext(), paramAnonymousAccessControlContext2).optimize());
/*     */       }
/*     */ 
/*     */       public <T> T doIntersectionPrivilege(PrivilegedAction<T> paramAnonymousPrivilegedAction, AccessControlContext paramAnonymousAccessControlContext)
/*     */       {
/*  87 */         return doIntersectionPrivilege(paramAnonymousPrivilegedAction, AccessController.getContext(), paramAnonymousAccessControlContext);
/*     */       }
/*     */     });
/* 118 */     debug = Debug.getInstance("domain");
/*     */ 
/* 454 */     SharedSecrets.setJavaSecurityProtectionDomainAccess(new JavaSecurityProtectionDomainAccess()
/*     */     {
/*     */       public JavaSecurityProtectionDomainAccess.ProtectionDomainCache getProtectionDomainCache() {
/* 457 */         return new JavaSecurityProtectionDomainAccess.ProtectionDomainCache() {
/* 458 */           private final Map<ProtectionDomain.Key, PermissionCollection> map = Collections.synchronizedMap(new WeakHashMap());
/*     */ 
/*     */           public void put(ProtectionDomain paramAnonymous2ProtectionDomain, PermissionCollection paramAnonymous2PermissionCollection)
/*     */           {
/* 463 */             this.map.put(paramAnonymous2ProtectionDomain == null ? null : paramAnonymous2ProtectionDomain.key, paramAnonymous2PermissionCollection);
/*     */           }
/*     */           public PermissionCollection get(ProtectionDomain paramAnonymous2ProtectionDomain) {
/* 466 */             return paramAnonymous2ProtectionDomain == null ? (PermissionCollection)this.map.get(null) : (PermissionCollection)this.map.get(paramAnonymous2ProtectionDomain.key);
/*     */           }
/*     */         };
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   final class Key
/*     */   {
/*     */     Key()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.ProtectionDomain
 * JD-Core Version:    0.6.2
 */
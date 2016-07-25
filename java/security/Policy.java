/*     */ package java.security;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ import sun.security.provider.PolicyFile;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.SecurityConstants;
/*     */ 
/*     */ public abstract class Policy
/*     */ {
/* 104 */   public static final PermissionCollection UNSUPPORTED_EMPTY_COLLECTION = new UnsupportedEmptyCollection();
/*     */ 
/* 121 */   private static AtomicReference<PolicyInfo> policy = new AtomicReference(new PolicyInfo(null, false));
/*     */ 
/* 124 */   private static final Debug debug = Debug.getInstance("policy");
/*     */   private WeakHashMap<ProtectionDomain.Key, PermissionCollection> pdMapping;
/*     */ 
/*     */   static boolean isSet()
/*     */   {
/* 132 */     PolicyInfo localPolicyInfo = (PolicyInfo)policy.get();
/* 133 */     return (localPolicyInfo.policy != null) && (localPolicyInfo.initialized == true);
/*     */   }
/*     */ 
/*     */   private static void checkPermission(String paramString) {
/* 137 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 138 */     if (localSecurityManager != null)
/* 139 */       localSecurityManager.checkPermission(new SecurityPermission("createPolicy." + paramString));
/*     */   }
/*     */ 
/*     */   public static Policy getPolicy()
/*     */   {
/* 163 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 164 */     if (localSecurityManager != null)
/* 165 */       localSecurityManager.checkPermission(SecurityConstants.GET_POLICY_PERMISSION);
/* 166 */     return getPolicyNoCheck();
/*     */   }
/*     */ 
/*     */   static Policy getPolicyNoCheck()
/*     */   {
/* 177 */     PolicyInfo localPolicyInfo1 = (PolicyInfo)policy.get();
/*     */ 
/* 180 */     if ((!localPolicyInfo1.initialized) || (localPolicyInfo1.policy == null)) {
/* 181 */       synchronized (Policy.class) {
/* 182 */         PolicyInfo localPolicyInfo2 = (PolicyInfo)policy.get();
/* 183 */         if (localPolicyInfo2.policy == null) {
/* 184 */           String str1 = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public String run() {
/* 187 */               return Security.getProperty("policy.provider");
/*     */             }
/*     */           });
/* 190 */           if (str1 == null) {
/* 191 */             str1 = "sun.security.provider.PolicyFile";
/*     */           }
/*     */           try
/*     */           {
/* 195 */             localPolicyInfo2 = new PolicyInfo((Policy)Class.forName(str1).newInstance(), true);
/*     */           }
/*     */           catch (Exception localException)
/*     */           {
/* 208 */             PolicyFile localPolicyFile = new PolicyFile();
/* 209 */             localPolicyInfo2 = new PolicyInfo(localPolicyFile, false);
/* 210 */             policy.set(localPolicyInfo2);
/*     */ 
/* 212 */             String str2 = str1;
/* 213 */             Policy localPolicy = (Policy)AccessController.doPrivileged(new PrivilegedAction()
/*     */             {
/*     */               public Policy run() {
/*     */                 try {
/* 217 */                   ClassLoader localClassLoader1 = ClassLoader.getSystemClassLoader();
/*     */ 
/* 220 */                   ClassLoader localClassLoader2 = null;
/* 221 */                   while (localClassLoader1 != null) {
/* 222 */                     localClassLoader2 = localClassLoader1;
/* 223 */                     localClassLoader1 = localClassLoader1.getParent();
/*     */                   }
/* 225 */                   return localClassLoader2 != null ? (Policy)Class.forName(this.val$pc, true, localClassLoader2).newInstance() : null;
/*     */                 }
/*     */                 catch (Exception localException) {
/* 228 */                   if (Policy.debug != null) {
/* 229 */                     Policy.debug.println("policy provider " + this.val$pc + " not available");
/*     */ 
/* 232 */                     localException.printStackTrace();
/*     */                   }
/*     */                 }
/* 234 */                 return null;
/*     */               }
/*     */             });
/* 242 */             if (localPolicy != null) {
/* 243 */               localPolicyInfo2 = new PolicyInfo(localPolicy, true);
/*     */             } else {
/* 245 */               if (debug != null) {
/* 246 */                 debug.println("using sun.security.provider.PolicyFile");
/*     */               }
/* 248 */               localPolicyInfo2 = new PolicyInfo(localPolicyFile, true);
/*     */             }
/*     */           }
/* 251 */           policy.set(localPolicyInfo2);
/*     */         }
/* 253 */         return localPolicyInfo2.policy;
/*     */       }
/*     */     }
/* 256 */     return localPolicyInfo1.policy;
/*     */   }
/*     */ 
/*     */   public static void setPolicy(Policy paramPolicy)
/*     */   {
/* 278 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 279 */     if (localSecurityManager != null) localSecurityManager.checkPermission(new SecurityPermission("setPolicy"));
/*     */ 
/* 281 */     if (paramPolicy != null) {
/* 282 */       initPolicy(paramPolicy);
/*     */     }
/* 284 */     synchronized (Policy.class) {
/* 285 */       policy.set(new PolicyInfo(paramPolicy, paramPolicy != null));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void initPolicy(Policy paramPolicy)
/*     */   {
/* 317 */     ProtectionDomain localProtectionDomain = (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ProtectionDomain run() {
/* 320 */         return this.val$p.getClass().getProtectionDomain();
/*     */       }
/*     */     });
/* 329 */     Object localObject1 = null;
/* 330 */     synchronized (paramPolicy) {
/* 331 */       if (paramPolicy.pdMapping == null) {
/* 332 */         paramPolicy.pdMapping = new WeakHashMap();
/*     */       }
/*     */     }
/*     */ 
/* 336 */     if (localProtectionDomain.getCodeSource() != null) {
/* 337 */       ??? = ((PolicyInfo)policy.get()).policy;
/* 338 */       if (??? != null) {
/* 339 */         localObject1 = ((Policy)???).getPermissions(localProtectionDomain);
/*     */       }
/*     */ 
/* 342 */       if (localObject1 == null) {
/* 343 */         localObject1 = new Permissions();
/* 344 */         ((PermissionCollection)localObject1).add(SecurityConstants.ALL_PERMISSION);
/*     */       }
/*     */ 
/* 347 */       synchronized (paramPolicy.pdMapping)
/*     */       {
/* 349 */         paramPolicy.pdMapping.put(localProtectionDomain.key, localObject1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Policy getInstance(String paramString, Parameters paramParameters)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 396 */     checkPermission(paramString);
/*     */     try {
/* 398 */       GetInstance.Instance localInstance = GetInstance.getInstance("Policy", PolicySpi.class, paramString, paramParameters);
/*     */ 
/* 402 */       return new PolicyDelegate((PolicySpi)localInstance.impl, localInstance.provider, paramString, paramParameters, null);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 407 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Policy getInstance(String paramString1, Parameters paramParameters, String paramString2)
/*     */     throws NoSuchProviderException, NoSuchAlgorithmException
/*     */   {
/* 458 */     if ((paramString2 == null) || (paramString2.length() == 0)) {
/* 459 */       throw new IllegalArgumentException("missing provider");
/*     */     }
/*     */ 
/* 462 */     checkPermission(paramString1);
/*     */     try {
/* 464 */       GetInstance.Instance localInstance = GetInstance.getInstance("Policy", PolicySpi.class, paramString1, paramParameters, paramString2);
/*     */ 
/* 469 */       return new PolicyDelegate((PolicySpi)localInstance.impl, localInstance.provider, paramString1, paramParameters, null);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 474 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Policy getInstance(String paramString, Parameters paramParameters, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 518 */     if (paramProvider == null) {
/* 519 */       throw new IllegalArgumentException("missing provider");
/*     */     }
/*     */ 
/* 522 */     checkPermission(paramString);
/*     */     try {
/* 524 */       GetInstance.Instance localInstance = GetInstance.getInstance("Policy", PolicySpi.class, paramString, paramParameters, paramProvider);
/*     */ 
/* 529 */       return new PolicyDelegate((PolicySpi)localInstance.impl, localInstance.provider, paramString, paramParameters, null);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/* 534 */       return handleException(localNoSuchAlgorithmException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Policy handleException(NoSuchAlgorithmException paramNoSuchAlgorithmException) throws NoSuchAlgorithmException
/*     */   {
/* 540 */     Throwable localThrowable = paramNoSuchAlgorithmException.getCause();
/* 541 */     if ((localThrowable instanceof IllegalArgumentException)) {
/* 542 */       throw ((IllegalArgumentException)localThrowable);
/*     */     }
/* 544 */     throw paramNoSuchAlgorithmException;
/*     */   }
/*     */ 
/*     */   public Provider getProvider()
/*     */   {
/* 559 */     return null;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 574 */     return null;
/*     */   }
/*     */ 
/*     */   public Parameters getParameters()
/*     */   {
/* 589 */     return null;
/*     */   }
/*     */ 
/*     */   public PermissionCollection getPermissions(CodeSource paramCodeSource)
/*     */   {
/* 619 */     return UNSUPPORTED_EMPTY_COLLECTION;
/*     */   }
/*     */ 
/*     */   public PermissionCollection getPermissions(ProtectionDomain paramProtectionDomain)
/*     */   {
/* 657 */     Object localObject1 = null;
/*     */ 
/* 659 */     if (paramProtectionDomain == null) {
/* 660 */       return new Permissions();
/*     */     }
/* 662 */     if (this.pdMapping == null) {
/* 663 */       initPolicy(this);
/*     */     }
/*     */ 
/* 666 */     synchronized (this.pdMapping) {
/* 667 */       localObject1 = (PermissionCollection)this.pdMapping.get(paramProtectionDomain.key);
/*     */     }
/*     */ 
/* 670 */     if (localObject1 != null) {
/* 671 */       ??? = new Permissions();
/*     */       Enumeration localEnumeration;
/* 672 */       synchronized (localObject1) {
/* 673 */         for (localEnumeration = ((PermissionCollection)localObject1).elements(); localEnumeration.hasMoreElements(); ) {
/* 674 */           ((Permissions)???).add((Permission)localEnumeration.nextElement());
/*     */         }
/*     */       }
/* 677 */       return ???;
/*     */     }
/*     */ 
/* 680 */     localObject1 = getPermissions(paramProtectionDomain.getCodeSource());
/* 681 */     if ((localObject1 == null) || (localObject1 == UNSUPPORTED_EMPTY_COLLECTION)) {
/* 682 */       localObject1 = new Permissions();
/*     */     }
/*     */ 
/* 685 */     addStaticPerms((PermissionCollection)localObject1, paramProtectionDomain.getPermissions());
/* 686 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void addStaticPerms(PermissionCollection paramPermissionCollection1, PermissionCollection paramPermissionCollection2)
/*     */   {
/* 694 */     if (paramPermissionCollection2 != null)
/* 695 */       synchronized (paramPermissionCollection2) {
/* 696 */         Enumeration localEnumeration = paramPermissionCollection2.elements();
/* 697 */         while (localEnumeration.hasMoreElements())
/* 698 */           paramPermissionCollection1.add((Permission)localEnumeration.nextElement());
/*     */       }
/*     */   }
/*     */ 
/*     */   public boolean implies(ProtectionDomain paramProtectionDomain, Permission paramPermission)
/*     */   {
/* 721 */     if (this.pdMapping == null) {
/* 722 */       initPolicy(this);
/*     */     }
/*     */ 
/* 725 */     synchronized (this.pdMapping) {
/* 726 */       localPermissionCollection = (PermissionCollection)this.pdMapping.get(paramProtectionDomain.key);
/*     */     }
/*     */ 
/* 729 */     if (localPermissionCollection != null) {
/* 730 */       return localPermissionCollection.implies(paramPermission);
/*     */     }
/*     */ 
/* 733 */     PermissionCollection localPermissionCollection = getPermissions(paramProtectionDomain);
/* 734 */     if (localPermissionCollection == null) {
/* 735 */       return false;
/*     */     }
/*     */ 
/* 738 */     synchronized (this.pdMapping)
/*     */     {
/* 740 */       this.pdMapping.put(paramProtectionDomain.key, localPermissionCollection);
/*     */     }
/*     */ 
/* 743 */     return localPermissionCollection.implies(paramPermission);
/*     */   }
/*     */ 
/*     */   public void refresh()
/*     */   {
/*     */   }
/*     */ 
/*     */   public static abstract interface Parameters
/*     */   {
/*     */   }
/*     */ 
/*     */   private static class PolicyDelegate extends Policy
/*     */   {
/*     */     private PolicySpi spi;
/*     */     private Provider p;
/*     */     private String type;
/*     */     private Policy.Parameters params;
/*     */ 
/*     */     private PolicyDelegate(PolicySpi paramPolicySpi, Provider paramProvider, String paramString, Policy.Parameters paramParameters)
/*     */     {
/* 770 */       this.spi = paramPolicySpi;
/* 771 */       this.p = paramProvider;
/* 772 */       this.type = paramString;
/* 773 */       this.params = paramParameters;
/*     */     }
/*     */     public String getType() {
/* 776 */       return this.type;
/*     */     }
/* 778 */     public Policy.Parameters getParameters() { return this.params; } 
/*     */     public Provider getProvider() {
/* 780 */       return this.p;
/*     */     }
/*     */ 
/*     */     public PermissionCollection getPermissions(CodeSource paramCodeSource) {
/* 784 */       return this.spi.engineGetPermissions(paramCodeSource);
/*     */     }
/*     */ 
/*     */     public PermissionCollection getPermissions(ProtectionDomain paramProtectionDomain) {
/* 788 */       return this.spi.engineGetPermissions(paramProtectionDomain);
/*     */     }
/*     */ 
/*     */     public boolean implies(ProtectionDomain paramProtectionDomain, Permission paramPermission) {
/* 792 */       return this.spi.engineImplies(paramProtectionDomain, paramPermission);
/*     */     }
/*     */ 
/*     */     public void refresh() {
/* 796 */       this.spi.engineRefresh();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class PolicyInfo
/*     */   {
/*     */     final Policy policy;
/*     */     final boolean initialized;
/*     */ 
/*     */     PolicyInfo(Policy paramPolicy, boolean paramBoolean)
/*     */     {
/* 115 */       this.policy = paramPolicy;
/* 116 */       this.initialized = paramBoolean;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class UnsupportedEmptyCollection extends PermissionCollection
/*     */   {
/*     */     private Permissions perms;
/*     */ 
/*     */     public UnsupportedEmptyCollection()
/*     */     {
/* 823 */       this.perms = new Permissions();
/* 824 */       this.perms.setReadOnly();
/*     */     }
/*     */ 
/*     */     public void add(Permission paramPermission)
/*     */     {
/* 837 */       this.perms.add(paramPermission);
/*     */     }
/*     */ 
/*     */     public boolean implies(Permission paramPermission)
/*     */     {
/* 850 */       return this.perms.implies(paramPermission);
/*     */     }
/*     */ 
/*     */     public Enumeration<Permission> elements()
/*     */     {
/* 860 */       return this.perms.elements();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Policy
 * JD-Core Version:    0.6.2
 */
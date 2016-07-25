/*      */ package java.security;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import sun.security.util.Debug;
/*      */ 
/*      */ public abstract class Provider extends Properties
/*      */ {
/*      */   static final long serialVersionUID = -4298000515446427739L;
/*   92 */   private static final Debug debug = Debug.getInstance("provider", "Provider");
/*      */   private String name;
/*      */   private String info;
/*      */   private double version;
/*  118 */   private transient Set<Map.Entry<Object, Object>> entrySet = null;
/*  119 */   private transient int entrySetCallCount = 0;
/*      */   private transient boolean initialized;
/*      */   private transient boolean legacyChanged;
/*      */   private transient boolean servicesChanged;
/*      */   private transient Map<String, String> legacyStrings;
/*      */   private transient Map<ServiceKey, Service> serviceMap;
/*      */   private transient Map<ServiceKey, Service> legacyMap;
/*      */   private transient Set<Service> serviceSet;
/*      */   private static final String ALIAS_PREFIX = "Alg.Alias.";
/*      */   private static final String ALIAS_PREFIX_LOWER = "alg.alias.";
/*  590 */   private static final int ALIAS_LENGTH = "Alg.Alias.".length();
/*      */ 
/*  703 */   private static volatile ServiceKey previousKey = new ServiceKey("", "", false, null);
/*      */ 
/*  949 */   private static final Map<String, EngineDescription> knownEngines = new HashMap();
/*      */ 
/*      */   protected Provider(String paramString1, double paramDouble, String paramString2)
/*      */   {
/*  134 */     this.name = paramString1;
/*  135 */     this.version = paramDouble;
/*  136 */     this.info = paramString2;
/*  137 */     putId();
/*  138 */     this.initialized = true;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  147 */     return this.name;
/*      */   }
/*      */ 
/*      */   public double getVersion()
/*      */   {
/*  156 */     return this.version;
/*      */   }
/*      */ 
/*      */   public String getInfo()
/*      */   {
/*  166 */     return this.info;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  177 */     return this.name + " version " + this.version;
/*      */   }
/*      */ 
/*      */   public synchronized void clear()
/*      */   {
/*  208 */     check("clearProviderProperties." + this.name);
/*  209 */     if (debug != null) {
/*  210 */       debug.println("Remove " + this.name + " provider properties");
/*      */     }
/*  212 */     implClear();
/*      */   }
/*      */ 
/*      */   public synchronized void load(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*  224 */     check("putProviderProperty." + this.name);
/*  225 */     if (debug != null) {
/*  226 */       debug.println("Load " + this.name + " provider properties");
/*      */     }
/*  228 */     Properties localProperties = new Properties();
/*  229 */     localProperties.load(paramInputStream);
/*  230 */     implPutAll(localProperties);
/*      */   }
/*      */ 
/*      */   public synchronized void putAll(Map<?, ?> paramMap)
/*      */   {
/*  241 */     check("putProviderProperty." + this.name);
/*  242 */     if (debug != null) {
/*  243 */       debug.println("Put all " + this.name + " provider properties");
/*      */     }
/*  245 */     implPutAll(paramMap);
/*      */   }
/*      */ 
/*      */   public synchronized Set<Map.Entry<Object, Object>> entrySet()
/*      */   {
/*  256 */     checkInitialized();
/*  257 */     if (this.entrySet == null) {
/*  258 */       if (this.entrySetCallCount++ == 0)
/*  259 */         this.entrySet = Collections.unmodifiableMap(this).entrySet();
/*      */       else {
/*  261 */         return super.entrySet();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  269 */     if (this.entrySetCallCount != 2) {
/*  270 */       throw new RuntimeException("Internal error.");
/*      */     }
/*  272 */     return this.entrySet;
/*      */   }
/*      */ 
/*      */   public Set<Object> keySet()
/*      */   {
/*  282 */     checkInitialized();
/*  283 */     return Collections.unmodifiableSet(super.keySet());
/*      */   }
/*      */ 
/*      */   public Collection<Object> values()
/*      */   {
/*  293 */     checkInitialized();
/*  294 */     return Collections.unmodifiableCollection(super.values());
/*      */   }
/*      */ 
/*      */   public synchronized Object put(Object paramObject1, Object paramObject2)
/*      */   {
/*  326 */     check("putProviderProperty." + this.name);
/*  327 */     if (debug != null) {
/*  328 */       debug.println("Set " + this.name + " provider property [" + paramObject1 + "/" + paramObject2 + "]");
/*      */     }
/*      */ 
/*  331 */     return implPut(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public synchronized Object remove(Object paramObject)
/*      */   {
/*  362 */     check("removeProviderProperty." + this.name);
/*  363 */     if (debug != null) {
/*  364 */       debug.println("Remove " + this.name + " provider property " + paramObject);
/*      */     }
/*  366 */     return implRemove(paramObject);
/*      */   }
/*      */ 
/*      */   public Object get(Object paramObject)
/*      */   {
/*  371 */     checkInitialized();
/*  372 */     return super.get(paramObject);
/*      */   }
/*      */ 
/*      */   public Enumeration<Object> keys()
/*      */   {
/*  377 */     checkInitialized();
/*  378 */     return super.keys();
/*      */   }
/*      */ 
/*      */   public Enumeration<Object> elements()
/*      */   {
/*  383 */     checkInitialized();
/*  384 */     return super.elements();
/*      */   }
/*      */ 
/*      */   public String getProperty(String paramString)
/*      */   {
/*  389 */     checkInitialized();
/*  390 */     return super.getProperty(paramString);
/*      */   }
/*      */ 
/*      */   private void checkInitialized() {
/*  394 */     if (!this.initialized)
/*  395 */       throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   private void check(String paramString)
/*      */   {
/*  400 */     checkInitialized();
/*  401 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  402 */     if (localSecurityManager != null)
/*  403 */       localSecurityManager.checkSecurityAccess(paramString);
/*      */   }
/*      */ 
/*      */   private void putId()
/*      */   {
/*  432 */     super.put("Provider.id name", String.valueOf(this.name));
/*  433 */     super.put("Provider.id version", String.valueOf(this.version));
/*  434 */     super.put("Provider.id info", String.valueOf(this.info));
/*  435 */     super.put("Provider.id className", getClass().getName());
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*      */   {
/*  440 */     HashMap localHashMap = new HashMap();
/*  441 */     for (Map.Entry localEntry : super.entrySet()) {
/*  442 */       localHashMap.put(localEntry.getKey(), localEntry.getValue());
/*      */     }
/*  444 */     this.defaults = null;
/*  445 */     paramObjectInputStream.defaultReadObject();
/*  446 */     implClear();
/*  447 */     this.initialized = true;
/*  448 */     putAll(localHashMap);
/*      */   }
/*      */ 
/*      */   private void implPutAll(Map paramMap)
/*      */   {
/*  457 */     for (Map.Entry localEntry : paramMap.entrySet())
/*  458 */       implPut(localEntry.getKey(), localEntry.getValue());
/*      */   }
/*      */ 
/*      */   private Object implRemove(Object paramObject)
/*      */   {
/*  463 */     if ((paramObject instanceof String)) {
/*  464 */       String str = (String)paramObject;
/*  465 */       if (str.startsWith("Provider.")) {
/*  466 */         return null;
/*      */       }
/*  468 */       this.legacyChanged = true;
/*  469 */       if (this.legacyStrings == null) {
/*  470 */         this.legacyStrings = new LinkedHashMap();
/*      */       }
/*  472 */       this.legacyStrings.remove(str);
/*      */     }
/*  474 */     return super.remove(paramObject);
/*      */   }
/*      */ 
/*      */   private Object implPut(Object paramObject1, Object paramObject2) {
/*  478 */     if (((paramObject1 instanceof String)) && ((paramObject2 instanceof String))) {
/*  479 */       String str = (String)paramObject1;
/*  480 */       if (str.startsWith("Provider.")) {
/*  481 */         return null;
/*      */       }
/*  483 */       this.legacyChanged = true;
/*  484 */       if (this.legacyStrings == null) {
/*  485 */         this.legacyStrings = new LinkedHashMap();
/*      */       }
/*  487 */       this.legacyStrings.put(str, (String)paramObject2);
/*      */     }
/*  489 */     return super.put(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   private void implClear() {
/*  493 */     if (this.legacyStrings != null) {
/*  494 */       this.legacyStrings.clear();
/*      */     }
/*  496 */     if (this.legacyMap != null) {
/*  497 */       this.legacyMap.clear();
/*      */     }
/*  499 */     if (this.serviceMap != null) {
/*  500 */       this.serviceMap.clear();
/*      */     }
/*  502 */     this.legacyChanged = false;
/*  503 */     this.servicesChanged = false;
/*  504 */     this.serviceSet = null;
/*  505 */     super.clear();
/*  506 */     putId();
/*      */   }
/*      */ 
/*      */   private void ensureLegacyParsed()
/*      */   {
/*  544 */     if ((!this.legacyChanged) || (this.legacyStrings == null)) {
/*  545 */       return;
/*      */     }
/*  547 */     this.serviceSet = null;
/*  548 */     if (this.legacyMap == null)
/*  549 */       this.legacyMap = new LinkedHashMap();
/*      */     else {
/*  551 */       this.legacyMap.clear();
/*      */     }
/*  553 */     for (Map.Entry localEntry : this.legacyStrings.entrySet()) {
/*  554 */       parseLegacyPut((String)localEntry.getKey(), (String)localEntry.getValue());
/*      */     }
/*  556 */     removeInvalidServices(this.legacyMap);
/*  557 */     this.legacyChanged = false;
/*      */   }
/*      */ 
/*      */   private void removeInvalidServices(Map<ServiceKey, Service> paramMap)
/*      */   {
/*  565 */     for (Iterator localIterator = paramMap.entrySet().iterator(); localIterator.hasNext(); ) {
/*  566 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/*  567 */       Service localService = (Service)localEntry.getValue();
/*  568 */       if (!localService.isValid())
/*  569 */         localIterator.remove();
/*      */     }
/*      */   }
/*      */ 
/*      */   private String[] getTypeAndAlgorithm(String paramString)
/*      */   {
/*  575 */     int i = paramString.indexOf(".");
/*  576 */     if (i < 1) {
/*  577 */       if (debug != null) {
/*  578 */         debug.println("Ignoring invalid entry in provider " + this.name + ":" + paramString);
/*      */       }
/*      */ 
/*  581 */       return null;
/*      */     }
/*  583 */     String str1 = paramString.substring(0, i);
/*  584 */     String str2 = paramString.substring(i + 1);
/*  585 */     return new String[] { str1, str2 };
/*      */   }
/*      */ 
/*      */   private void parseLegacyPut(String paramString1, String paramString2)
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*      */     String str2;
/*      */     String str3;
/*      */     Object localObject3;
/*      */     Object localObject4;
/*  593 */     if (paramString1.toLowerCase(Locale.ENGLISH).startsWith("alg.alias."))
/*      */     {
/*  596 */       localObject1 = paramString2;
/*  597 */       String str1 = paramString1.substring(ALIAS_LENGTH);
/*  598 */       localObject2 = getTypeAndAlgorithm(str1);
/*  599 */       if (localObject2 == null) {
/*  600 */         return;
/*      */       }
/*  602 */       str2 = getEngineName(localObject2[0]);
/*  603 */       str3 = localObject2[1].intern();
/*  604 */       localObject3 = new ServiceKey(str2, (String)localObject1, true, null);
/*  605 */       localObject4 = (Service)this.legacyMap.get(localObject3);
/*  606 */       if (localObject4 == null) {
/*  607 */         localObject4 = new Service(this, null);
/*  608 */         ((Service)localObject4).type = str2;
/*  609 */         ((Service)localObject4).algorithm = ((String)localObject1);
/*  610 */         this.legacyMap.put(localObject3, localObject4);
/*      */       }
/*  612 */       this.legacyMap.put(new ServiceKey(str2, str3, true, null), localObject4);
/*  613 */       ((Service)localObject4).addAlias(str3);
/*      */     } else {
/*  615 */       localObject1 = getTypeAndAlgorithm(paramString1);
/*  616 */       if (localObject1 == null) {
/*  617 */         return;
/*      */       }
/*  619 */       int i = localObject1[1].indexOf(' ');
/*  620 */       if (i == -1)
/*      */       {
/*  622 */         localObject2 = getEngineName(localObject1[0]);
/*  623 */         str2 = localObject1[1].intern();
/*  624 */         str3 = paramString2;
/*  625 */         localObject3 = new ServiceKey((String)localObject2, str2, true, null);
/*  626 */         localObject4 = (Service)this.legacyMap.get(localObject3);
/*  627 */         if (localObject4 == null) {
/*  628 */           localObject4 = new Service(this, null);
/*  629 */           ((Service)localObject4).type = ((String)localObject2);
/*  630 */           ((Service)localObject4).algorithm = str2;
/*  631 */           this.legacyMap.put(localObject3, localObject4);
/*      */         }
/*  633 */         ((Service)localObject4).className = str3;
/*      */       }
/*      */       else {
/*  636 */         localObject2 = paramString2;
/*  637 */         str2 = getEngineName(localObject1[0]);
/*  638 */         str3 = localObject1[1];
/*  639 */         localObject3 = str3.substring(0, i).intern();
/*  640 */         localObject4 = str3.substring(i + 1);
/*      */ 
/*  642 */         while (((String)localObject4).startsWith(" ")) {
/*  643 */           localObject4 = ((String)localObject4).substring(1);
/*      */         }
/*  645 */         localObject4 = ((String)localObject4).intern();
/*  646 */         ServiceKey localServiceKey = new ServiceKey(str2, (String)localObject3, true, null);
/*  647 */         Service localService = (Service)this.legacyMap.get(localServiceKey);
/*  648 */         if (localService == null) {
/*  649 */           localService = new Service(this, null);
/*  650 */           localService.type = str2;
/*  651 */           localService.algorithm = ((String)localObject3);
/*  652 */           this.legacyMap.put(localServiceKey, localService);
/*      */         }
/*  654 */         localService.addAttribute((String)localObject4, (String)localObject2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized Service getService(String paramString1, String paramString2)
/*      */   {
/*  680 */     checkInitialized();
/*      */ 
/*  682 */     ServiceKey localServiceKey = previousKey;
/*  683 */     if (!localServiceKey.matches(paramString1, paramString2)) {
/*  684 */       localServiceKey = new ServiceKey(paramString1, paramString2, false, null);
/*  685 */       previousKey = localServiceKey;
/*      */     }
/*  687 */     if (this.serviceMap != null) {
/*  688 */       Service localService = (Service)this.serviceMap.get(localServiceKey);
/*  689 */       if (localService != null) {
/*  690 */         return localService;
/*      */       }
/*      */     }
/*  693 */     ensureLegacyParsed();
/*  694 */     return this.legacyMap != null ? (Service)this.legacyMap.get(localServiceKey) : null;
/*      */   }
/*      */ 
/*      */   public synchronized Set<Service> getServices()
/*      */   {
/*  716 */     checkInitialized();
/*  717 */     if ((this.legacyChanged) || (this.servicesChanged)) {
/*  718 */       this.serviceSet = null;
/*      */     }
/*  720 */     if (this.serviceSet == null) {
/*  721 */       ensureLegacyParsed();
/*  722 */       LinkedHashSet localLinkedHashSet = new LinkedHashSet();
/*  723 */       if (this.serviceMap != null) {
/*  724 */         localLinkedHashSet.addAll(this.serviceMap.values());
/*      */       }
/*  726 */       if (this.legacyMap != null) {
/*  727 */         localLinkedHashSet.addAll(this.legacyMap.values());
/*      */       }
/*  729 */       this.serviceSet = Collections.unmodifiableSet(localLinkedHashSet);
/*  730 */       this.servicesChanged = false;
/*      */     }
/*  732 */     return this.serviceSet;
/*      */   }
/*      */ 
/*      */   protected synchronized void putService(Service paramService)
/*      */   {
/*  765 */     check("putProviderProperty." + this.name);
/*  766 */     if (debug != null) {
/*  767 */       debug.println(this.name + ".putService(): " + paramService);
/*      */     }
/*  769 */     if (paramService == null) {
/*  770 */       throw new NullPointerException();
/*      */     }
/*  772 */     if (paramService.getProvider() != this) {
/*  773 */       throw new IllegalArgumentException("service.getProvider() must match this Provider object");
/*      */     }
/*      */ 
/*  776 */     if (this.serviceMap == null) {
/*  777 */       this.serviceMap = new LinkedHashMap();
/*      */     }
/*  779 */     this.servicesChanged = true;
/*  780 */     String str1 = paramService.getType();
/*  781 */     String str2 = paramService.getAlgorithm();
/*  782 */     ServiceKey localServiceKey = new ServiceKey(str1, str2, true, null);
/*      */ 
/*  784 */     implRemoveService((Service)this.serviceMap.get(localServiceKey));
/*  785 */     this.serviceMap.put(localServiceKey, paramService);
/*  786 */     for (String str3 : paramService.getAliases()) {
/*  787 */       this.serviceMap.put(new ServiceKey(str1, str3, true, null), paramService);
/*      */     }
/*  789 */     putPropertyStrings(paramService);
/*      */   }
/*      */ 
/*      */   private void putPropertyStrings(Service paramService)
/*      */   {
/*  797 */     String str1 = paramService.getType();
/*  798 */     String str2 = paramService.getAlgorithm();
/*      */ 
/*  800 */     super.put(str1 + "." + str2, paramService.getClassName());
/*  801 */     for (Iterator localIterator = paramService.getAliases().iterator(); localIterator.hasNext(); ) { localObject = (String)localIterator.next();
/*  802 */       super.put("Alg.Alias." + str1 + "." + (String)localObject, str2);
/*      */     }
/*  804 */     Object localObject;
/*  804 */     for (localIterator = paramService.attributes.entrySet().iterator(); localIterator.hasNext(); ) { localObject = (Map.Entry)localIterator.next();
/*  805 */       String str3 = str1 + "." + str2 + " " + ((Map.Entry)localObject).getKey();
/*  806 */       super.put(str3, ((Map.Entry)localObject).getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removePropertyStrings(Service paramService)
/*      */   {
/*  815 */     String str1 = paramService.getType();
/*  816 */     String str2 = paramService.getAlgorithm();
/*      */ 
/*  818 */     super.remove(str1 + "." + str2);
/*  819 */     for (Iterator localIterator = paramService.getAliases().iterator(); localIterator.hasNext(); ) { localObject = (String)localIterator.next();
/*  820 */       super.remove("Alg.Alias." + str1 + "." + (String)localObject);
/*      */     }
/*  822 */     Object localObject;
/*  822 */     for (localIterator = paramService.attributes.entrySet().iterator(); localIterator.hasNext(); ) { localObject = (Map.Entry)localIterator.next();
/*  823 */       String str3 = str1 + "." + str2 + " " + ((Map.Entry)localObject).getKey();
/*  824 */       super.remove(str3);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected synchronized void removeService(Service paramService)
/*      */   {
/*  857 */     check("removeProviderProperty." + this.name);
/*  858 */     if (debug != null) {
/*  859 */       debug.println(this.name + ".removeService(): " + paramService);
/*      */     }
/*  861 */     if (paramService == null) {
/*  862 */       throw new NullPointerException();
/*      */     }
/*  864 */     implRemoveService(paramService);
/*      */   }
/*      */ 
/*      */   private void implRemoveService(Service paramService) {
/*  868 */     if ((paramService == null) || (this.serviceMap == null)) {
/*  869 */       return;
/*      */     }
/*  871 */     String str1 = paramService.getType();
/*  872 */     String str2 = paramService.getAlgorithm();
/*  873 */     ServiceKey localServiceKey = new ServiceKey(str1, str2, false, null);
/*  874 */     Service localService = (Service)this.serviceMap.get(localServiceKey);
/*  875 */     if (paramService != localService) {
/*  876 */       return;
/*      */     }
/*  878 */     this.servicesChanged = true;
/*  879 */     this.serviceMap.remove(localServiceKey);
/*  880 */     for (String str3 : paramService.getAliases()) {
/*  881 */       this.serviceMap.remove(new ServiceKey(str1, str3, false, null));
/*      */     }
/*  883 */     removePropertyStrings(paramService);
/*      */   }
/*      */ 
/*      */   private static void addEngine(String paramString1, boolean paramBoolean, String paramString2)
/*      */   {
/*  942 */     EngineDescription localEngineDescription = new EngineDescription(paramString1, paramBoolean, paramString2);
/*      */ 
/*  944 */     knownEngines.put(paramString1.toLowerCase(Locale.ENGLISH), localEngineDescription);
/*  945 */     knownEngines.put(paramString1, localEngineDescription);
/*      */   }
/*      */ 
/*      */   private static String getEngineName(String paramString)
/*      */   {
/*  999 */     EngineDescription localEngineDescription = (EngineDescription)knownEngines.get(paramString);
/* 1000 */     if (localEngineDescription == null) {
/* 1001 */       localEngineDescription = (EngineDescription)knownEngines.get(paramString.toLowerCase(Locale.ENGLISH));
/*      */     }
/* 1003 */     return localEngineDescription == null ? paramString : localEngineDescription.name;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  951 */     addEngine("AlgorithmParameterGenerator", false, null);
/*  952 */     addEngine("AlgorithmParameters", false, null);
/*  953 */     addEngine("KeyFactory", false, null);
/*  954 */     addEngine("KeyPairGenerator", false, null);
/*  955 */     addEngine("KeyStore", false, null);
/*  956 */     addEngine("MessageDigest", false, null);
/*  957 */     addEngine("SecureRandom", false, null);
/*  958 */     addEngine("Signature", true, null);
/*  959 */     addEngine("CertificateFactory", false, null);
/*  960 */     addEngine("CertPathBuilder", false, null);
/*  961 */     addEngine("CertPathValidator", false, null);
/*  962 */     addEngine("CertStore", false, "java.security.cert.CertStoreParameters");
/*      */ 
/*  965 */     addEngine("Cipher", true, null);
/*  966 */     addEngine("ExemptionMechanism", false, null);
/*  967 */     addEngine("Mac", true, null);
/*  968 */     addEngine("KeyAgreement", true, null);
/*  969 */     addEngine("KeyGenerator", false, null);
/*  970 */     addEngine("SecretKeyFactory", false, null);
/*      */ 
/*  972 */     addEngine("KeyManagerFactory", false, null);
/*  973 */     addEngine("SSLContext", false, null);
/*  974 */     addEngine("TrustManagerFactory", false, null);
/*      */ 
/*  976 */     addEngine("GssApiMechanism", false, null);
/*      */ 
/*  978 */     addEngine("SaslClientFactory", false, null);
/*  979 */     addEngine("SaslServerFactory", false, null);
/*      */ 
/*  981 */     addEngine("Policy", false, "java.security.Policy$Parameters");
/*      */ 
/*  984 */     addEngine("Configuration", false, "javax.security.auth.login.Configuration$Parameters");
/*      */ 
/*  987 */     addEngine("XMLSignatureFactory", false, null);
/*  988 */     addEngine("KeyInfoFactory", false, null);
/*  989 */     addEngine("TransformService", false, null);
/*      */ 
/*  991 */     addEngine("TerminalFactory", false, "java.lang.Object");
/*      */   }
/*      */ 
/*      */   private static class EngineDescription
/*      */   {
/*      */     final String name;
/*      */     final boolean supportsParameter;
/*      */     final String constructorParameterClassName;
/*      */     private volatile Class constructorParameterClass;
/*      */ 
/*      */     EngineDescription(String paramString1, boolean paramBoolean, String paramString2)
/*      */     {
/*  924 */       this.name = paramString1;
/*  925 */       this.supportsParameter = paramBoolean;
/*  926 */       this.constructorParameterClassName = paramString2;
/*      */     }
/*      */     Class getConstructorParameterClass() throws ClassNotFoundException {
/*  929 */       Class localClass = this.constructorParameterClass;
/*  930 */       if (localClass == null) {
/*  931 */         localClass = Class.forName(this.constructorParameterClassName);
/*  932 */         this.constructorParameterClass = localClass;
/*      */       }
/*  934 */       return localClass;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class Service
/*      */   {
/*      */     private String type;
/*      */     private String algorithm;
/*      */     private String className;
/*      */     private final Provider provider;
/*      */     private List<String> aliases;
/*      */     private Map<Provider.UString, String> attributes;
/*      */     private volatile Reference<Class> classRef;
/*      */     private volatile Boolean hasKeyAttributes;
/*      */     private String[] supportedFormats;
/*      */     private Class[] supportedClasses;
/*      */     private boolean registered;
/* 1058 */     private static final Class[] CLASS0 = new Class[0];
/*      */ 
/*      */     private Service(Provider paramProvider)
/*      */     {
/* 1064 */       this.provider = paramProvider;
/* 1065 */       this.aliases = Collections.emptyList();
/* 1066 */       this.attributes = Collections.emptyMap();
/*      */     }
/*      */ 
/*      */     private boolean isValid() {
/* 1070 */       return (this.type != null) && (this.algorithm != null) && (this.className != null);
/*      */     }
/*      */ 
/*      */     private void addAlias(String paramString) {
/* 1074 */       if (this.aliases.isEmpty()) {
/* 1075 */         this.aliases = new ArrayList(2);
/*      */       }
/* 1077 */       this.aliases.add(paramString);
/*      */     }
/*      */ 
/*      */     void addAttribute(String paramString1, String paramString2) {
/* 1081 */       if (this.attributes.isEmpty()) {
/* 1082 */         this.attributes = new HashMap(8);
/*      */       }
/* 1084 */       this.attributes.put(new Provider.UString(paramString1), paramString2);
/*      */     }
/*      */ 
/*      */     public Service(Provider paramProvider, String paramString1, String paramString2, String paramString3, List<String> paramList, Map<String, String> paramMap)
/*      */     {
/* 1104 */       if ((paramProvider == null) || (paramString1 == null) || (paramString2 == null) || (paramString3 == null))
/*      */       {
/* 1106 */         throw new NullPointerException();
/*      */       }
/* 1108 */       this.provider = paramProvider;
/* 1109 */       this.type = Provider.getEngineName(paramString1);
/* 1110 */       this.algorithm = paramString2;
/* 1111 */       this.className = paramString3;
/* 1112 */       if (paramList == null)
/* 1113 */         this.aliases = Collections.emptyList();
/*      */       else {
/* 1115 */         this.aliases = new ArrayList(paramList);
/*      */       }
/* 1117 */       if (paramMap == null) {
/* 1118 */         this.attributes = Collections.emptyMap();
/*      */       } else {
/* 1120 */         this.attributes = new HashMap();
/* 1121 */         for (Map.Entry localEntry : paramMap.entrySet())
/* 1122 */           this.attributes.put(new Provider.UString((String)localEntry.getKey()), localEntry.getValue());
/*      */       }
/*      */     }
/*      */ 
/*      */     public final String getType()
/*      */     {
/* 1133 */       return this.type;
/*      */     }
/*      */ 
/*      */     public final String getAlgorithm()
/*      */     {
/* 1143 */       return this.algorithm;
/*      */     }
/*      */ 
/*      */     public final Provider getProvider()
/*      */     {
/* 1152 */       return this.provider;
/*      */     }
/*      */ 
/*      */     public final String getClassName()
/*      */     {
/* 1161 */       return this.className;
/*      */     }
/*      */ 
/*      */     private final List<String> getAliases()
/*      */     {
/* 1166 */       return this.aliases;
/*      */     }
/*      */ 
/*      */     public final String getAttribute(String paramString)
/*      */     {
/* 1181 */       if (paramString == null) {
/* 1182 */         throw new NullPointerException();
/*      */       }
/* 1184 */       return (String)this.attributes.get(new Provider.UString(paramString));
/*      */     }
/*      */ 
/*      */     public Object newInstance(Object paramObject)
/*      */       throws NoSuchAlgorithmException
/*      */     {
/* 1215 */       if (!this.registered) {
/* 1216 */         if (this.provider.getService(this.type, this.algorithm) != this) {
/* 1217 */           throw new NoSuchAlgorithmException("Service not registered with Provider " + this.provider.getName() + ": " + this);
/*      */         }
/*      */ 
/* 1221 */         this.registered = true;
/*      */       }
/*      */       try {
/* 1224 */         Provider.EngineDescription localEngineDescription = (Provider.EngineDescription)Provider.knownEngines.get(this.type);
/* 1225 */         if (localEngineDescription == null)
/*      */         {
/* 1229 */           return newInstanceGeneric(paramObject);
/*      */         }
/* 1231 */         if (localEngineDescription.constructorParameterClassName == null) {
/* 1232 */           if (paramObject != null) {
/* 1233 */             throw new InvalidParameterException("constructorParameter not used with " + this.type + " engines");
/*      */           }
/*      */ 
/* 1237 */           localClass = getImplClass();
/* 1238 */           localObject = new Class[0];
/* 1239 */           localConstructor = localClass.getConstructor((Class[])localObject);
/* 1240 */           return localConstructor.newInstance(new Object[0]);
/*      */         }
/* 1242 */         Class localClass = localEngineDescription.getConstructorParameterClass();
/* 1243 */         if (paramObject != null) {
/* 1244 */           localObject = paramObject.getClass();
/* 1245 */           if (!localClass.isAssignableFrom((Class)localObject)) {
/* 1246 */             throw new InvalidParameterException("constructorParameter must be instanceof " + localEngineDescription.constructorParameterClassName.replace('$', '.') + " for engine type " + this.type);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1252 */         Object localObject = getImplClass();
/* 1253 */         Constructor localConstructor = ((Class)localObject).getConstructor(new Class[] { localClass });
/* 1254 */         return localConstructor.newInstance(new Object[] { paramObject });
/*      */       }
/*      */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1257 */         throw localNoSuchAlgorithmException;
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/* 1259 */         throw new NoSuchAlgorithmException("Error constructing implementation (algorithm: " + this.algorithm + ", provider: " + this.provider.getName() + ", class: " + this.className + ")", localInvocationTargetException.getCause());
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 1264 */         throw new NoSuchAlgorithmException("Error constructing implementation (algorithm: " + this.algorithm + ", provider: " + this.provider.getName() + ", class: " + this.className + ")", localException);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Class getImplClass()
/*      */       throws NoSuchAlgorithmException
/*      */     {
/*      */       try
/*      */       {
/* 1274 */         Reference localReference = this.classRef;
/* 1275 */         Class localClass = localReference == null ? null : (Class)localReference.get();
/* 1276 */         if (localClass == null) {
/* 1277 */           ClassLoader localClassLoader = this.provider.getClass().getClassLoader();
/* 1278 */           if (localClassLoader == null)
/* 1279 */             localClass = Class.forName(this.className);
/*      */           else {
/* 1281 */             localClass = localClassLoader.loadClass(this.className);
/*      */           }
/* 1283 */           if (!Modifier.isPublic(localClass.getModifiers())) {
/* 1284 */             throw new NoSuchAlgorithmException("class configured for " + this.type + " (provider: " + this.provider.getName() + ") is not public.");
/*      */           }
/*      */ 
/* 1288 */           this.classRef = new WeakReference(localClass);
/*      */         }
/* 1290 */         return localClass;
/*      */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 1292 */         throw new NoSuchAlgorithmException("class configured for " + this.type + "(provider: " + this.provider.getName() + ") cannot be found.", localClassNotFoundException);
/*      */       }
/*      */     }
/*      */ 
/*      */     private Object newInstanceGeneric(Object paramObject)
/*      */       throws Exception
/*      */     {
/* 1305 */       Class localClass1 = getImplClass();
/* 1306 */       if (paramObject == null) {
/*      */         try
/*      */         {
/* 1309 */           Class[] arrayOfClass1 = new Class[0];
/* 1310 */           localObject1 = localClass1.getConstructor(arrayOfClass1);
/* 1311 */           return ((Constructor)localObject1).newInstance(new Object[0]);
/*      */         } catch (NoSuchMethodException localNoSuchMethodException) {
/* 1313 */           throw new NoSuchAlgorithmException("No public no-arg constructor found in class " + this.className);
/*      */         }
/*      */       }
/*      */ 
/* 1317 */       Class localClass2 = paramObject.getClass();
/* 1318 */       Object localObject1 = localClass1.getConstructors();
/*      */ 
/* 1321 */       for (Object localObject3 : localObject1) {
/* 1322 */         Class[] arrayOfClass2 = localObject3.getParameterTypes();
/* 1323 */         if (arrayOfClass2.length == 1)
/*      */         {
/* 1326 */           if (arrayOfClass2[0].isAssignableFrom(localClass2))
/*      */           {
/* 1329 */             return localObject3.newInstance(new Object[] { paramObject });
/*      */           }
/*      */         }
/*      */       }
/* 1331 */       throw new NoSuchAlgorithmException("No public constructor matching " + localClass2.getName() + " found in class " + this.className);
/*      */     }
/*      */ 
/*      */     public boolean supportsParameter(Object paramObject)
/*      */     {
/* 1363 */       Provider.EngineDescription localEngineDescription = (Provider.EngineDescription)Provider.knownEngines.get(this.type);
/* 1364 */       if (localEngineDescription == null)
/*      */       {
/* 1366 */         return true;
/*      */       }
/* 1368 */       if (!localEngineDescription.supportsParameter) {
/* 1369 */         throw new InvalidParameterException("supportsParameter() not used with " + this.type + " engines");
/*      */       }
/*      */ 
/* 1373 */       if ((paramObject != null) && (!(paramObject instanceof Key))) {
/* 1374 */         throw new InvalidParameterException("Parameter must be instanceof Key for engine " + this.type);
/*      */       }
/*      */ 
/* 1377 */       if (!hasKeyAttributes()) {
/* 1378 */         return true;
/*      */       }
/* 1380 */       if (paramObject == null) {
/* 1381 */         return false;
/*      */       }
/* 1383 */       Key localKey = (Key)paramObject;
/* 1384 */       if (supportsKeyFormat(localKey)) {
/* 1385 */         return true;
/*      */       }
/* 1387 */       if (supportsKeyClass(localKey)) {
/* 1388 */         return true;
/*      */       }
/* 1390 */       return false;
/*      */     }
/*      */ 
/*      */     private boolean hasKeyAttributes()
/*      */     {
/* 1398 */       Boolean localBoolean = this.hasKeyAttributes;
/* 1399 */       if (localBoolean == null) {
/* 1400 */         synchronized (this)
/*      */         {
/* 1402 */           String str1 = getAttribute("SupportedKeyFormats");
/* 1403 */           if (str1 != null) {
/* 1404 */             this.supportedFormats = str1.split("\\|");
/*      */           }
/* 1406 */           str1 = getAttribute("SupportedKeyClasses");
/* 1407 */           if (str1 != null) {
/* 1408 */             String[] arrayOfString1 = str1.split("\\|");
/* 1409 */             ArrayList localArrayList = new ArrayList(arrayOfString1.length);
/*      */ 
/* 1411 */             for (String str2 : arrayOfString1) {
/* 1412 */               Class localClass = getKeyClass(str2);
/* 1413 */               if (localClass != null) {
/* 1414 */                 localArrayList.add(localClass);
/*      */               }
/*      */             }
/* 1417 */             this.supportedClasses = ((Class[])localArrayList.toArray(CLASS0));
/*      */           }
/* 1419 */           boolean bool = (this.supportedFormats != null) || (this.supportedClasses != null);
/*      */ 
/* 1421 */           localBoolean = Boolean.valueOf(bool);
/* 1422 */           this.hasKeyAttributes = localBoolean;
/*      */         }
/*      */       }
/* 1425 */       return localBoolean.booleanValue();
/*      */     }
/*      */ 
/*      */     private Class getKeyClass(String paramString)
/*      */     {
/*      */       try {
/* 1431 */         return Class.forName(paramString);
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException1)
/*      */       {
/*      */         try {
/* 1436 */           ClassLoader localClassLoader = this.provider.getClass().getClassLoader();
/* 1437 */           if (localClassLoader != null)
/* 1438 */             return localClassLoader.loadClass(paramString);
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException2) {
/*      */         }
/*      */       }
/* 1443 */       return null;
/*      */     }
/*      */ 
/*      */     private boolean supportsKeyFormat(Key paramKey) {
/* 1447 */       if (this.supportedFormats == null) {
/* 1448 */         return false;
/*      */       }
/* 1450 */       String str1 = paramKey.getFormat();
/* 1451 */       if (str1 == null) {
/* 1452 */         return false;
/*      */       }
/* 1454 */       for (String str2 : this.supportedFormats) {
/* 1455 */         if (str2.equals(str1)) {
/* 1456 */           return true;
/*      */         }
/*      */       }
/* 1459 */       return false;
/*      */     }
/*      */ 
/*      */     private boolean supportsKeyClass(Key paramKey) {
/* 1463 */       if (this.supportedClasses == null) {
/* 1464 */         return false;
/*      */       }
/* 1466 */       Class localClass1 = paramKey.getClass();
/* 1467 */       for (Class localClass2 : this.supportedClasses) {
/* 1468 */         if (localClass2.isAssignableFrom(localClass1)) {
/* 1469 */           return true;
/*      */         }
/*      */       }
/* 1472 */       return false;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1481 */       String str1 = "\r\n  aliases: " + this.aliases.toString();
/*      */ 
/* 1483 */       String str2 = "\r\n  attributes: " + this.attributes.toString();
/*      */ 
/* 1485 */       return this.provider.getName() + ": " + this.type + "." + this.algorithm + " -> " + this.className + str1 + str2 + "\r\n";
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ServiceKey
/*      */   {
/*      */     private final String type;
/*      */     private final String algorithm;
/*      */     private final String originalAlgorithm;
/*      */ 
/*      */     private ServiceKey(String paramString1, String paramString2, boolean paramBoolean)
/*      */     {
/*  515 */       this.type = paramString1;
/*  516 */       this.originalAlgorithm = paramString2;
/*  517 */       paramString2 = paramString2.toUpperCase(Locale.ENGLISH);
/*  518 */       this.algorithm = (paramBoolean ? paramString2.intern() : paramString2);
/*      */     }
/*      */     public int hashCode() {
/*  521 */       return this.type.hashCode() + this.algorithm.hashCode();
/*      */     }
/*      */     public boolean equals(Object paramObject) {
/*  524 */       if (this == paramObject) {
/*  525 */         return true;
/*      */       }
/*  527 */       if (!(paramObject instanceof ServiceKey)) {
/*  528 */         return false;
/*      */       }
/*  530 */       ServiceKey localServiceKey = (ServiceKey)paramObject;
/*  531 */       return (this.type.equals(localServiceKey.type)) && (this.algorithm.equals(localServiceKey.algorithm));
/*      */     }
/*      */ 
/*      */     boolean matches(String paramString1, String paramString2) {
/*  535 */       return (this.type == paramString1) && (this.originalAlgorithm == paramString2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class UString
/*      */   {
/*      */     final String string;
/*      */     final String lowerString;
/*      */ 
/*      */     UString(String paramString)
/*      */     {
/*  892 */       this.string = paramString;
/*  893 */       this.lowerString = paramString.toLowerCase(Locale.ENGLISH);
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/*  897 */       return this.lowerString.hashCode();
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/*  901 */       if (this == paramObject) {
/*  902 */         return true;
/*      */       }
/*  904 */       if (!(paramObject instanceof UString)) {
/*  905 */         return false;
/*      */       }
/*  907 */       UString localUString = (UString)paramObject;
/*  908 */       return this.lowerString.equals(localUString.lowerString);
/*      */     }
/*      */ 
/*      */     public String toString() {
/*  912 */       return this.string;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Provider
 * JD-Core Version:    0.6.2
 */
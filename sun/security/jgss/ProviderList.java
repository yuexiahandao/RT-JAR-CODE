/*     */ package sun.security.jgss;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.AccessController;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.security.jgss.spi.MechanismFactory;
/*     */ import sun.security.jgss.wrapper.NativeGSSFactory;
/*     */ import sun.security.jgss.wrapper.SunNativeProvider;
/*     */ 
/*     */ public final class ProviderList
/*     */ {
/*     */   private static final String PROV_PROP_PREFIX = "GssApiMechanism.";
/*  90 */   private static final int PROV_PROP_PREFIX_LEN = "GssApiMechanism.".length();
/*     */   private static final String SPI_MECH_FACTORY_TYPE = "sun.security.jgss.spi.MechanismFactory";
/*     */   private static final String DEFAULT_MECH_PROP = "sun.security.jgss.mechanism";
/* 114 */   public static final Oid DEFAULT_MECH_OID = localOid == null ? GSSUtil.GSS_KRB5_MECH_OID : localOid;
/*     */ 
/* 118 */   private ArrayList<PreferencesEntry> preferences = new ArrayList(5);
/*     */ 
/* 120 */   private HashMap<PreferencesEntry, MechanismFactory> factories = new HashMap(5);
/*     */ 
/* 122 */   private HashSet<Oid> mechs = new HashSet(5);
/*     */   private final GSSCaller caller;
/*     */ 
/*     */   public ProviderList(GSSCaller paramGSSCaller, boolean paramBoolean)
/*     */   {
/* 127 */     this.caller = paramGSSCaller;
/*     */     Provider[] arrayOfProvider;
/* 129 */     if (paramBoolean) {
/* 130 */       arrayOfProvider = new Provider[1];
/* 131 */       arrayOfProvider[0] = new SunNativeProvider();
/*     */     } else {
/* 133 */       arrayOfProvider = Security.getProviders();
/*     */     }
/*     */ 
/* 136 */     for (int i = 0; i < arrayOfProvider.length; i++) {
/* 137 */       Provider localProvider = arrayOfProvider[i];
/*     */       try {
/* 139 */         addProviderAtEnd(localProvider, null);
/*     */       }
/*     */       catch (GSSException localGSSException) {
/* 142 */         GSSUtil.debug("Error in adding provider " + localProvider.getName() + ": " + localGSSException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isMechFactoryProperty(String paramString)
/*     */   {
/* 154 */     return (paramString.startsWith("GssApiMechanism.")) || (paramString.regionMatches(true, 0, "GssApiMechanism.", 0, PROV_PROP_PREFIX_LEN));
/*     */   }
/*     */ 
/*     */   private Oid getOidFromMechFactoryProperty(String paramString)
/*     */     throws GSSException
/*     */   {
/* 163 */     String str = paramString.substring(PROV_PROP_PREFIX_LEN);
/* 164 */     return new Oid(str);
/*     */   }
/*     */ 
/*     */   public synchronized MechanismFactory getMechFactory(Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 170 */     if (paramOid == null) paramOid = DEFAULT_MECH_OID;
/* 171 */     return getMechFactory(paramOid, null);
/*     */   }
/*     */ 
/*     */   public synchronized MechanismFactory getMechFactory(Oid paramOid, Provider paramProvider)
/*     */     throws GSSException
/*     */   {
/* 189 */     if (paramOid == null) paramOid = DEFAULT_MECH_OID;
/*     */ 
/* 191 */     if (paramProvider == null)
/*     */     {
/* 196 */       Iterator localIterator = this.preferences.iterator();
/* 197 */       while (localIterator.hasNext()) {
/* 198 */         PreferencesEntry localPreferencesEntry2 = (PreferencesEntry)localIterator.next();
/* 199 */         if (localPreferencesEntry2.impliesMechanism(paramOid)) {
/* 200 */           MechanismFactory localMechanismFactory = getMechFactory(localPreferencesEntry2, paramOid);
/* 201 */           if (localMechanismFactory != null) return localMechanismFactory;
/*     */         }
/*     */       }
/* 204 */       throw new GSSExceptionImpl(2, paramOid);
/*     */     }
/*     */ 
/* 208 */     PreferencesEntry localPreferencesEntry1 = new PreferencesEntry(paramProvider, paramOid);
/* 209 */     return getMechFactory(localPreferencesEntry1, paramOid);
/*     */   }
/*     */ 
/*     */   private MechanismFactory getMechFactory(PreferencesEntry paramPreferencesEntry, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 226 */     Provider localProvider = paramPreferencesEntry.getProvider();
/*     */ 
/* 232 */     PreferencesEntry localPreferencesEntry = new PreferencesEntry(localProvider, paramOid);
/* 233 */     MechanismFactory localMechanismFactory = (MechanismFactory)this.factories.get(localPreferencesEntry);
/* 234 */     if (localMechanismFactory == null)
/*     */     {
/* 239 */       String str1 = "GssApiMechanism." + paramOid.toString();
/* 240 */       String str2 = localProvider.getProperty(str1);
/* 241 */       if (str2 != null) {
/* 242 */         localMechanismFactory = getMechFactoryImpl(localProvider, str2, paramOid, this.caller);
/* 243 */         this.factories.put(localPreferencesEntry, localMechanismFactory);
/*     */       }
/* 251 */       else if (paramPreferencesEntry.getOid() != null) {
/* 252 */         throw new GSSExceptionImpl(2, "Provider " + localProvider.getName() + " does not support mechanism " + paramOid);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 258 */     return localMechanismFactory;
/*     */   }
/*     */ 
/*     */   private static MechanismFactory getMechFactoryImpl(Provider paramProvider, String paramString, Oid paramOid, GSSCaller paramGSSCaller)
/*     */     throws GSSException
/*     */   {
/*     */     try
/*     */     {
/* 278 */       Class localClass1 = Class.forName("sun.security.jgss.spi.MechanismFactory");
/*     */ 
/* 290 */       ClassLoader localClassLoader = paramProvider.getClass().getClassLoader();
/*     */       Class localClass2;
/* 292 */       if (localClassLoader != null)
/* 293 */         localClass2 = localClassLoader.loadClass(paramString);
/*     */       else {
/* 295 */         localClass2 = Class.forName(paramString);
/*     */       }
/*     */ 
/* 298 */       if (localClass1.isAssignableFrom(localClass2))
/*     */       {
/* 300 */         Constructor localConstructor = localClass2.getConstructor(new Class[] { GSSCaller.class });
/*     */ 
/* 302 */         MechanismFactory localMechanismFactory = (MechanismFactory)localConstructor.newInstance(new Object[] { paramGSSCaller });
/*     */ 
/* 304 */         if ((localMechanismFactory instanceof NativeGSSFactory)) {
/* 305 */           ((NativeGSSFactory)localMechanismFactory).setMech(paramOid);
/*     */         }
/* 307 */         return localMechanismFactory;
/*     */       }
/* 309 */       throw createGSSException(paramProvider, paramString, "is not a sun.security.jgss.spi.MechanismFactory", null);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 313 */       throw createGSSException(paramProvider, paramString, "cannot be created", localClassNotFoundException);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 315 */       throw createGSSException(paramProvider, paramString, "cannot be created", localNoSuchMethodException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 317 */       throw createGSSException(paramProvider, paramString, "cannot be created", localInvocationTargetException);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 319 */       throw createGSSException(paramProvider, paramString, "cannot be created", localInstantiationException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 321 */       throw createGSSException(paramProvider, paramString, "cannot be created", localIllegalAccessException);
/*     */     } catch (SecurityException localSecurityException) {
/* 323 */       throw createGSSException(paramProvider, paramString, "cannot be created", localSecurityException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static GSSException createGSSException(Provider paramProvider, String paramString1, String paramString2, Exception paramException)
/*     */   {
/* 332 */     String str = paramString1 + " configured by " + paramProvider.getName() + " for GSS-API Mechanism Factory ";
/*     */ 
/* 334 */     return new GSSExceptionImpl(2, str + paramString2, paramException);
/*     */   }
/*     */ 
/*     */   public Oid[] getMechs()
/*     */   {
/* 340 */     return (Oid[])this.mechs.toArray(new Oid[0]);
/*     */   }
/*     */ 
/*     */   public synchronized void addProviderAtFront(Provider paramProvider, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 346 */     PreferencesEntry localPreferencesEntry1 = new PreferencesEntry(paramProvider, paramOid);
/*     */ 
/* 350 */     Iterator localIterator = this.preferences.iterator();
/* 351 */     while (localIterator.hasNext()) {
/* 352 */       PreferencesEntry localPreferencesEntry2 = (PreferencesEntry)localIterator.next();
/* 353 */       if (localPreferencesEntry1.implies(localPreferencesEntry2))
/* 354 */         localIterator.remove();
/*     */     }
/*     */     boolean bool;
/* 357 */     if (paramOid == null) {
/* 358 */       bool = addAllMechsFromProvider(paramProvider);
/*     */     } else {
/* 360 */       String str = paramOid.toString();
/* 361 */       if (paramProvider.getProperty("GssApiMechanism." + str) == null) {
/* 362 */         throw new GSSExceptionImpl(2, "Provider " + paramProvider.getName() + " does not support " + str);
/*     */       }
/*     */ 
/* 366 */       this.mechs.add(paramOid);
/* 367 */       bool = true;
/*     */     }
/*     */ 
/* 370 */     if (bool)
/* 371 */       this.preferences.add(0, localPreferencesEntry1);
/*     */   }
/*     */ 
/*     */   public synchronized void addProviderAtEnd(Provider paramProvider, Oid paramOid)
/*     */     throws GSSException
/*     */   {
/* 378 */     PreferencesEntry localPreferencesEntry1 = new PreferencesEntry(paramProvider, paramOid);
/*     */ 
/* 382 */     Iterator localIterator = this.preferences.iterator();
/* 383 */     while (localIterator.hasNext()) {
/* 384 */       PreferencesEntry localPreferencesEntry2 = (PreferencesEntry)localIterator.next();
/* 385 */       if (localPreferencesEntry2.implies(localPreferencesEntry1))
/*     */         return;
/*     */     }
/*     */     boolean bool;
/* 391 */     if (paramOid == null) {
/* 392 */       bool = addAllMechsFromProvider(paramProvider);
/*     */     } else {
/* 394 */       String str = paramOid.toString();
/* 395 */       if (paramProvider.getProperty("GssApiMechanism." + str) == null) {
/* 396 */         throw new GSSExceptionImpl(2, "Provider " + paramProvider.getName() + " does not support " + str);
/*     */       }
/*     */ 
/* 400 */       this.mechs.add(paramOid);
/* 401 */       bool = true;
/*     */     }
/*     */ 
/* 404 */     if (bool)
/* 405 */       this.preferences.add(localPreferencesEntry1);
/*     */   }
/*     */ 
/*     */   private boolean addAllMechsFromProvider(Provider paramProvider)
/*     */   {
/* 421 */     boolean bool = false;
/*     */ 
/* 424 */     Enumeration localEnumeration = paramProvider.keys();
/*     */ 
/* 427 */     while (localEnumeration.hasMoreElements()) {
/* 428 */       String str = (String)localEnumeration.nextElement();
/* 429 */       if (isMechFactoryProperty(str)) {
/*     */         try
/*     */         {
/* 432 */           Oid localOid = getOidFromMechFactoryProperty(str);
/* 433 */           this.mechs.add(localOid);
/* 434 */           bool = true;
/*     */         }
/*     */         catch (GSSException localGSSException) {
/* 437 */           GSSUtil.debug("Ignore the invalid property " + str + " from provider " + paramProvider.getName());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 443 */     return bool;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 108 */     Oid localOid = null;
/* 109 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.security.jgss.mechanism"));
/*     */ 
/* 111 */     if (str != null)
/* 112 */       localOid = GSSUtil.createOid(str);
/*     */   }
/*     */ 
/*     */   private static final class PreferencesEntry
/*     */   {
/*     */     private Provider p;
/*     */     private Oid oid;
/*     */ 
/*     */     PreferencesEntry(Provider paramProvider, Oid paramOid)
/*     */     {
/* 461 */       this.p = paramProvider;
/* 462 */       this.oid = paramOid;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 466 */       if (this == paramObject) {
/* 467 */         return true;
/*     */       }
/*     */ 
/* 470 */       if (!(paramObject instanceof PreferencesEntry)) {
/* 471 */         return false;
/*     */       }
/*     */ 
/* 474 */       PreferencesEntry localPreferencesEntry = (PreferencesEntry)paramObject;
/* 475 */       if (this.p.getName().equals(localPreferencesEntry.p.getName())) {
/* 476 */         if ((this.oid != null) && (localPreferencesEntry.oid != null)) {
/* 477 */           return this.oid.equals(localPreferencesEntry.oid);
/*     */         }
/* 479 */         return (this.oid == null) && (localPreferencesEntry.oid == null);
/*     */       }
/*     */ 
/* 483 */       return false;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 487 */       int i = 17;
/*     */ 
/* 489 */       i = 37 * i + this.p.getName().hashCode();
/* 490 */       if (this.oid != null) {
/* 491 */         i = 37 * i + this.oid.hashCode();
/*     */       }
/*     */ 
/* 494 */       return i;
/*     */     }
/*     */ 
/*     */     boolean implies(Object paramObject)
/*     */     {
/* 506 */       if ((paramObject instanceof PreferencesEntry)) {
/* 507 */         PreferencesEntry localPreferencesEntry = (PreferencesEntry)paramObject;
/* 508 */         return (equals(localPreferencesEntry)) || ((this.p.getName().equals(localPreferencesEntry.p.getName())) && (this.oid == null));
/*     */       }
/*     */ 
/* 512 */       return false;
/*     */     }
/*     */ 
/*     */     Provider getProvider()
/*     */     {
/* 517 */       return this.p;
/*     */     }
/*     */ 
/*     */     Oid getOid() {
/* 521 */       return this.oid;
/*     */     }
/*     */ 
/*     */     boolean impliesMechanism(Oid paramOid)
/*     */     {
/* 534 */       return (this.oid == null) || (this.oid.equals(paramOid));
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 539 */       StringBuffer localStringBuffer = new StringBuffer("<");
/* 540 */       localStringBuffer.append(this.p.getName());
/* 541 */       localStringBuffer.append(", ");
/* 542 */       localStringBuffer.append(this.oid);
/* 543 */       localStringBuffer.append(">");
/* 544 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.ProviderList
 * JD-Core Version:    0.6.2
 */
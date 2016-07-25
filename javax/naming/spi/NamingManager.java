/*     */ package javax.naming.spi;
/*     */ 
/*     */ import com.sun.naming.internal.FactoryEnumeration;
/*     */ import com.sun.naming.internal.ResourceManager;
/*     */ import com.sun.naming.internal.VersionHelper;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CannotProceedException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NoInitialContextException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.naming.StringRefAddr;
/*     */ 
/*     */ public class NamingManager
/*     */ {
/*  74 */   static final VersionHelper helper = VersionHelper.getVersionHelper();
/*     */ 
/*  81 */   private static ObjectFactoryBuilder object_factory_builder = null;
/*     */   private static final String defaultPkgPrefix = "com.sun.jndi.url";
/* 614 */   private static InitialContextFactoryBuilder initctx_factory_builder = null;
/*     */   public static final String CPE = "java.naming.spi.CannotProceedException";
/*     */ 
/*     */   public static synchronized void setObjectFactoryBuilder(ObjectFactoryBuilder paramObjectFactoryBuilder)
/*     */     throws NamingException
/*     */   {
/* 110 */     if (object_factory_builder != null) {
/* 111 */       throw new IllegalStateException("ObjectFactoryBuilder already set");
/*     */     }
/* 113 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 114 */     if (localSecurityManager != null) {
/* 115 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 117 */     object_factory_builder = paramObjectFactoryBuilder;
/*     */   }
/*     */ 
/*     */   static synchronized ObjectFactoryBuilder getObjectFactoryBuilder()
/*     */   {
/* 124 */     return object_factory_builder;
/*     */   }
/*     */ 
/*     */   static ObjectFactory getObjectFactoryFromReference(Reference paramReference, String paramString)
/*     */     throws IllegalAccessException, InstantiationException, MalformedURLException
/*     */   {
/* 142 */     Class localClass = null;
/*     */     try
/*     */     {
/* 146 */       localClass = helper.loadClass(paramString);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1)
/*     */     {
/*     */     }
/*     */     String str;
/* 155 */     if ((localClass == null) && ((str = paramReference.getFactoryClassLocation()) != null))
/*     */       try
/*     */       {
/* 158 */         localClass = helper.loadClass(paramString, str);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException2)
/*     */       {
/*     */       }
/* 163 */     return localClass != null ? (ObjectFactory)localClass.newInstance() : null;
/*     */   }
/*     */ 
/*     */   private static Object createObjectFromFactories(Object paramObject, Name paramName, Context paramContext, Hashtable paramHashtable)
/*     */     throws Exception
/*     */   {
/* 177 */     FactoryEnumeration localFactoryEnumeration = ResourceManager.getFactories("java.naming.factory.object", paramHashtable, paramContext);
/*     */ 
/* 180 */     if (localFactoryEnumeration == null) {
/* 181 */       return null;
/*     */     }
/*     */ 
/* 185 */     Object localObject = null;
/* 186 */     while ((localObject == null) && (localFactoryEnumeration.hasMore())) {
/* 187 */       ObjectFactory localObjectFactory = (ObjectFactory)localFactoryEnumeration.next();
/* 188 */       localObject = localObjectFactory.getObjectInstance(paramObject, paramName, paramContext, paramHashtable);
/*     */     }
/* 190 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static String getURLScheme(String paramString) {
/* 194 */     int i = paramString.indexOf(':');
/* 195 */     int j = paramString.indexOf('/');
/*     */ 
/* 197 */     if ((i > 0) && ((j == -1) || (i < j)))
/* 198 */       return paramString.substring(0, i);
/* 199 */     return null;
/*     */   }
/*     */ 
/*     */   public static Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*     */     throws Exception
/*     */   {
/* 296 */     ObjectFactoryBuilder localObjectFactoryBuilder = getObjectFactoryBuilder();
/*     */     ObjectFactory localObjectFactory;
/* 297 */     if (localObjectFactoryBuilder != null)
/*     */     {
/* 299 */       localObjectFactory = localObjectFactoryBuilder.createObjectFactory(paramObject, paramHashtable);
/* 300 */       return localObjectFactory.getObjectInstance(paramObject, paramName, paramContext, paramHashtable);
/*     */     }
/*     */ 
/* 305 */     Reference localReference = null;
/* 306 */     if ((paramObject instanceof Reference))
/* 307 */       localReference = (Reference)paramObject;
/* 308 */     else if ((paramObject instanceof Referenceable)) {
/* 309 */       localReference = ((Referenceable)paramObject).getReference();
/*     */     }
/*     */ 
/* 314 */     if (localReference != null) {
/* 315 */       String str = localReference.getFactoryClassName();
/* 316 */       if (str != null)
/*     */       {
/* 319 */         localObjectFactory = getObjectFactoryFromReference(localReference, str);
/* 320 */         if (localObjectFactory != null) {
/* 321 */           return localObjectFactory.getObjectInstance(localReference, paramName, paramContext, paramHashtable);
/*     */         }
/*     */ 
/* 327 */         return paramObject;
/*     */       }
/*     */ 
/* 333 */       localObject = processURLAddrs(localReference, paramName, paramContext, paramHashtable);
/* 334 */       if (localObject != null) {
/* 335 */         return localObject;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 341 */     Object localObject = createObjectFromFactories(paramObject, paramName, paramContext, paramHashtable);
/*     */ 
/* 343 */     return localObject != null ? localObject : paramObject;
/*     */   }
/*     */ 
/*     */   static Object processURLAddrs(Reference paramReference, Name paramName, Context paramContext, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 355 */     for (int i = 0; i < paramReference.size(); i++) {
/* 356 */       RefAddr localRefAddr = paramReference.get(i);
/* 357 */       if (((localRefAddr instanceof StringRefAddr)) && (localRefAddr.getType().equalsIgnoreCase("URL")))
/*     */       {
/* 360 */         String str = (String)localRefAddr.getContent();
/* 361 */         Object localObject = processURL(str, paramName, paramContext, paramHashtable);
/* 362 */         if (localObject != null) {
/* 363 */           return localObject;
/*     */         }
/*     */       }
/*     */     }
/* 367 */     return null;
/*     */   }
/*     */ 
/*     */   private static Object processURL(Object paramObject, Name paramName, Context paramContext, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/*     */     Object localObject2;
/*     */     Object localObject1;
/* 377 */     if ((paramObject instanceof String)) {
/* 378 */       localObject2 = (String)paramObject;
/* 379 */       String str1 = getURLScheme((String)localObject2);
/* 380 */       if (str1 != null) {
/* 381 */         localObject1 = getURLObject(str1, paramObject, paramName, paramContext, paramHashtable);
/*     */ 
/* 383 */         if (localObject1 != null) {
/* 384 */           return localObject1;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 392 */     if ((paramObject instanceof String[])) {
/* 393 */       localObject2 = (String[])paramObject;
/* 394 */       for (int i = 0; i < localObject2.length; i++) {
/* 395 */         String str2 = getURLScheme(localObject2[i]);
/* 396 */         if (str2 != null) {
/* 397 */           localObject1 = getURLObject(str2, paramObject, paramName, paramContext, paramHashtable);
/*     */ 
/* 399 */           if (localObject1 != null)
/* 400 */             return localObject1;
/*     */         }
/*     */       }
/*     */     }
/* 404 */     return null;
/*     */   }
/*     */ 
/*     */   static Context getContext(Object paramObject, Name paramName, Context paramContext, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 433 */     if ((paramObject instanceof Context))
/*     */     {
/* 435 */       return (Context)paramObject;
/*     */     }
/*     */     Object localObject;
/*     */     try {
/* 439 */       localObject = getObjectInstance(paramObject, paramName, paramContext, paramHashtable);
/*     */     } catch (NamingException localNamingException1) {
/* 441 */       throw localNamingException1;
/*     */     } catch (Exception localException) {
/* 443 */       NamingException localNamingException2 = new NamingException();
/* 444 */       localNamingException2.setRootCause(localException);
/* 445 */       throw localNamingException2;
/*     */     }
/*     */ 
/* 448 */     return (localObject instanceof Context) ? (Context)localObject : null;
/*     */   }
/*     */ 
/*     */   static Resolver getResolver(Object paramObject, Name paramName, Context paramContext, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 458 */     if ((paramObject instanceof Resolver))
/*     */     {
/* 460 */       return (Resolver)paramObject;
/*     */     }
/*     */     Object localObject;
/*     */     try {
/* 464 */       localObject = getObjectInstance(paramObject, paramName, paramContext, paramHashtable);
/*     */     } catch (NamingException localNamingException1) {
/* 466 */       throw localNamingException1;
/*     */     } catch (Exception localException) {
/* 468 */       NamingException localNamingException2 = new NamingException();
/* 469 */       localNamingException2.setRootCause(localException);
/* 470 */       throw localNamingException2;
/*     */     }
/*     */ 
/* 473 */     return (localObject instanceof Resolver) ? (Resolver)localObject : null;
/*     */   }
/*     */ 
/*     */   public static Context getURLContext(String paramString, Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 550 */     Object localObject = getURLObject(paramString, null, null, null, paramHashtable);
/* 551 */     if ((localObject instanceof Context)) {
/* 552 */       return (Context)localObject;
/*     */     }
/* 554 */     return null;
/*     */   }
/*     */ 
/*     */   private static Object getURLObject(String paramString, Object paramObject, Name paramName, Context paramContext, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 592 */     ObjectFactory localObjectFactory = (ObjectFactory)ResourceManager.getFactory("java.naming.factory.url.pkgs", paramHashtable, paramContext, "." + paramString + "." + paramString + "URLContextFactory", "com.sun.jndi.url");
/*     */ 
/* 596 */     if (localObjectFactory == null) {
/* 597 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 601 */       return localObjectFactory.getObjectInstance(paramObject, paramName, paramContext, paramHashtable);
/*     */     } catch (NamingException localNamingException1) {
/* 603 */       throw localNamingException1;
/*     */     } catch (Exception localException) {
/* 605 */       NamingException localNamingException2 = new NamingException();
/* 606 */       localNamingException2.setRootCause(localException);
/* 607 */       throw localNamingException2;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized InitialContextFactoryBuilder getInitialContextFactoryBuilder()
/*     */   {
/* 622 */     return initctx_factory_builder;
/*     */   }
/*     */ 
/*     */   public static Context getInitialContext(Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 654 */     InitialContextFactoryBuilder localInitialContextFactoryBuilder = getInitialContextFactoryBuilder();
/*     */     InitialContextFactory localInitialContextFactory;
/* 655 */     if (localInitialContextFactoryBuilder == null)
/*     */     {
/* 659 */       String str = paramHashtable != null ? (String)paramHashtable.get("java.naming.factory.initial") : null;
/*     */ 
/* 661 */       if (str == null) {
/* 662 */         NoInitialContextException localNoInitialContextException1 = new NoInitialContextException("Need to specify class name in environment or system property, or as an applet parameter, or in an application resource file:  java.naming.factory.initial");
/*     */ 
/* 667 */         throw localNoInitialContextException1;
/*     */       }
/*     */       try
/*     */       {
/* 671 */         localInitialContextFactory = (InitialContextFactory)helper.loadClass(str).newInstance();
/*     */       }
/*     */       catch (Exception localException) {
/* 674 */         NoInitialContextException localNoInitialContextException2 = new NoInitialContextException("Cannot instantiate class: " + str);
/*     */ 
/* 677 */         localNoInitialContextException2.setRootCause(localException);
/* 678 */         throw localNoInitialContextException2;
/*     */       }
/*     */     } else {
/* 681 */       localInitialContextFactory = localInitialContextFactoryBuilder.createInitialContextFactory(paramHashtable);
/*     */     }
/*     */ 
/* 684 */     return localInitialContextFactory.getInitialContext(paramHashtable);
/*     */   }
/*     */ 
/*     */   public static synchronized void setInitialContextFactoryBuilder(InitialContextFactoryBuilder paramInitialContextFactoryBuilder)
/*     */     throws NamingException
/*     */   {
/* 708 */     if (initctx_factory_builder != null) {
/* 709 */       throw new IllegalStateException("InitialContextFactoryBuilder already set");
/*     */     }
/*     */ 
/* 712 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 713 */     if (localSecurityManager != null) {
/* 714 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 716 */     initctx_factory_builder = paramInitialContextFactoryBuilder;
/*     */   }
/*     */ 
/*     */   public static boolean hasInitialContextFactoryBuilder()
/*     */   {
/* 727 */     return getInitialContextFactoryBuilder() != null;
/*     */   }
/*     */ 
/*     */   public static Context getContinuationContext(CannotProceedException paramCannotProceedException)
/*     */     throws NamingException
/*     */   {
/* 777 */     Hashtable localHashtable = paramCannotProceedException.getEnvironment();
/* 778 */     if (localHashtable == null) {
/* 779 */       localHashtable = new Hashtable(7);
/*     */     }
/*     */     else {
/* 782 */       localHashtable = (Hashtable)localHashtable.clone();
/*     */     }
/* 784 */     localHashtable.put("java.naming.spi.CannotProceedException", paramCannotProceedException);
/*     */ 
/* 786 */     ContinuationContext localContinuationContext = new ContinuationContext(paramCannotProceedException, localHashtable);
/* 787 */     return localContinuationContext.getTargetContext();
/*     */   }
/*     */ 
/*     */   public static Object getStateToBind(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 857 */     FactoryEnumeration localFactoryEnumeration = ResourceManager.getFactories("java.naming.factory.state", paramHashtable, paramContext);
/*     */ 
/* 860 */     if (localFactoryEnumeration == null) {
/* 861 */       return paramObject;
/*     */     }
/*     */ 
/* 866 */     Object localObject = null;
/* 867 */     while ((localObject == null) && (localFactoryEnumeration.hasMore())) {
/* 868 */       StateFactory localStateFactory = (StateFactory)localFactoryEnumeration.next();
/* 869 */       localObject = localStateFactory.getStateToBind(paramObject, paramName, paramContext, paramHashtable);
/*     */     }
/*     */ 
/* 872 */     return localObject != null ? localObject : paramObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.spi.NamingManager
 * JD-Core Version:    0.6.2
 */
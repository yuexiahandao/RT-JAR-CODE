/*     */ package javax.sql.rowset.spi;
/*     */ 
/*     */ import com.sun.rowset.providers.RIOptimisticProvider;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.security.AccessControlException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.sql.SQLPermission;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NotContextException;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class SyncFactory
/*     */ {
/*     */   public static final String ROWSET_SYNC_PROVIDER = "rowset.provider.classname";
/*     */   public static final String ROWSET_SYNC_VENDOR = "rowset.provider.vendor";
/*     */   public static final String ROWSET_SYNC_PROVIDER_VERSION = "rowset.provider.version";
/* 236 */   private static String ROWSET_PROPERTIES = "rowset.properties";
/*     */ 
/* 240 */   private static String default_provider = "com.sun.rowset.providers.RIOptimisticProvider";
/*     */ 
/* 245 */   private static final SQLPermission SET_SYNCFACTORY_PERMISSION = new SQLPermission("setSyncFactory");
/*     */   private static Context ic;
/*     */   private static volatile Logger rsLogger;
/*     */   private static Level rsLevel;
/*     */   private static Hashtable implementations;
/* 269 */   private static Object logSync = new Object();
/*     */ 
/* 273 */   private static PrintWriter logWriter = null;
/*     */ 
/* 341 */   private static String colon = ":";
/* 342 */   private static String strFileSep = "/";
/*     */ 
/* 472 */   private static boolean debug = false;
/*     */ 
/* 477 */   private static int providerImplIndex = 0;
/*     */ 
/* 783 */   private static boolean lazyJNDICtxRefresh = false;
/*     */ 
/*     */   public static synchronized void registerProvider(String paramString)
/*     */     throws SyncFactoryException
/*     */   {
/* 305 */     ProviderImpl localProviderImpl = new ProviderImpl();
/* 306 */     localProviderImpl.setClassname(paramString);
/* 307 */     initMapIfNecessary();
/* 308 */     implementations.put(paramString, localProviderImpl);
/*     */   }
/*     */ 
/*     */   public static SyncFactory getSyncFactory()
/*     */   {
/* 323 */     return SyncFactoryHolder.factory;
/*     */   }
/*     */ 
/*     */   public static synchronized void unregisterProvider(String paramString)
/*     */     throws SyncFactoryException
/*     */   {
/* 336 */     initMapIfNecessary();
/* 337 */     if (implementations.containsKey(paramString))
/* 338 */       implementations.remove(paramString);
/*     */   }
/*     */ 
/*     */   private static synchronized void initMapIfNecessary()
/*     */     throws SyncFactoryException
/*     */   {
/* 349 */     final Properties localProperties = new Properties();
/*     */ 
/* 351 */     if (implementations == null) {
/* 352 */       implementations = new Hashtable();
/*     */       try
/*     */       {
/*     */         String str1;
/*     */         try
/*     */         {
/* 372 */           str1 = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */             public String run() {
/* 374 */               return System.getProperty("rowset.properties");
/*     */             } } );
/*     */         }
/*     */         catch (Exception localException1) {
/* 378 */           System.out.println("errorget rowset.properties: " + localException1);
/* 379 */           str1 = null;
/*     */         }
/* 381 */         if (str1 != null)
/*     */         {
/* 384 */           ROWSET_PROPERTIES = str1;
/* 385 */           localObject1 = new FileInputStream(ROWSET_PROPERTIES); Object localObject2 = null;
/*     */           try { localProperties.load((InputStream)localObject1); }
/*     */           catch (Throwable localThrowable2)
/*     */           {
/* 385 */             localObject2 = localThrowable2; throw localThrowable2;
/*     */           } finally {
/* 387 */             if (localObject1 != null) if (localObject2 != null) try { ((FileInputStream)localObject1).close(); } catch (Throwable localThrowable3) { localObject2.addSuppressed(localThrowable3); } else ((FileInputStream)localObject1).close(); 
/*     */           }
/* 388 */           parseProperties(localProperties);
/*     */         }
/*     */ 
/* 394 */         ROWSET_PROPERTIES = "javax" + strFileSep + "sql" + strFileSep + "rowset" + strFileSep + "rowset.properties";
/*     */ 
/* 398 */         Object localObject1 = Thread.currentThread().getContextClassLoader();
/*     */         try
/*     */         {
/* 401 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */           {
/*     */             public Void run() throws SyncFactoryException, IOException, FileNotFoundException {
/* 404 */               InputStream localInputStream = this.val$cl == null ? ClassLoader.getSystemResourceAsStream(SyncFactory.ROWSET_PROPERTIES) : this.val$cl.getResourceAsStream(SyncFactory.ROWSET_PROPERTIES); Object localObject1 = null;
/*     */               try
/*     */               {
/* 407 */                 if (localInputStream == null) {
/* 408 */                   throw new SyncFactoryException("Resource " + SyncFactory.ROWSET_PROPERTIES + " not found");
/*     */                 }
/* 410 */                 localProperties.load(localInputStream);
/*     */               }
/*     */               catch (Throwable localThrowable2)
/*     */               {
/* 404 */                 localObject1 = localThrowable2; throw localThrowable2;
/*     */               }
/*     */               finally
/*     */               {
/* 411 */                 if (localInputStream != null) if (localObject1 != null) try { localInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localInputStream.close(); 
/*     */               }
/* 412 */               return null;
/*     */             }
/*     */           });
/*     */         }
/*     */         catch (PrivilegedActionException localPrivilegedActionException) {
/* 417 */           Exception localException3 = localPrivilegedActionException.getException();
/* 418 */           if ((localException3 instanceof SyncFactoryException)) {
/* 419 */             throw ((SyncFactoryException)localException3);
/*     */           }
/* 421 */           SyncFactoryException localSyncFactoryException = new SyncFactoryException();
/* 422 */           localSyncFactoryException.initCause(localPrivilegedActionException.getException());
/* 423 */           throw localSyncFactoryException;
/*     */         }
/*     */ 
/* 427 */         parseProperties(localProperties);
/*     */       }
/*     */       catch (FileNotFoundException localFileNotFoundException)
/*     */       {
/* 432 */         throw new SyncFactoryException("Cannot locate properties file: " + localFileNotFoundException);
/*     */       } catch (IOException localIOException) {
/* 434 */         throw new SyncFactoryException("IOException: " + localIOException);
/*     */       }
/*     */ 
/* 441 */       localProperties.clear();
/*     */       String str2;
/*     */       try
/*     */       {
/* 444 */         str2 = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public String run() {
/* 446 */             return System.getProperty("rowset.provider.classname");
/*     */           } } );
/*     */       }
/*     */       catch (Exception localException2) {
/* 450 */         str2 = null;
/*     */       }
/*     */ 
/* 453 */       if (str2 != null) {
/* 454 */         int i = 0;
/* 455 */         if (str2.indexOf(colon) > 0) {
/* 456 */           StringTokenizer localStringTokenizer = new StringTokenizer(str2, colon);
/* 457 */           while (localStringTokenizer.hasMoreElements()) {
/* 458 */             localProperties.put("rowset.provider.classname." + i, localStringTokenizer.nextToken());
/* 459 */             i++;
/*     */           }
/*     */         } else {
/* 462 */           localProperties.put("rowset.provider.classname", str2);
/*     */         }
/* 464 */         parseProperties(localProperties);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void parseProperties(Properties paramProperties)
/*     */   {
/* 485 */     ProviderImpl localProviderImpl = null;
/* 486 */     String str1 = null;
/* 487 */     String[] arrayOfString = null;
/*     */ 
/* 489 */     for (Enumeration localEnumeration = paramProperties.propertyNames(); localEnumeration.hasMoreElements(); )
/*     */     {
/* 491 */       String str2 = (String)localEnumeration.nextElement();
/*     */ 
/* 493 */       int i = str2.length();
/*     */ 
/* 495 */       if (str2.startsWith("rowset.provider.classname"))
/*     */       {
/* 497 */         localProviderImpl = new ProviderImpl();
/* 498 */         localProviderImpl.setIndex(providerImplIndex++);
/*     */ 
/* 500 */         if (i == "rowset.provider.classname".length())
/*     */         {
/* 502 */           arrayOfString = getPropertyNames(false);
/*     */         }
/*     */         else {
/* 505 */           arrayOfString = getPropertyNames(true, str2.substring(i - 1));
/*     */         }
/*     */ 
/* 508 */         str1 = paramProperties.getProperty(arrayOfString[0]);
/* 509 */         localProviderImpl.setClassname(str1);
/* 510 */         localProviderImpl.setVendor(paramProperties.getProperty(arrayOfString[1]));
/* 511 */         localProviderImpl.setVersion(paramProperties.getProperty(arrayOfString[2]));
/* 512 */         implementations.put(str1, localProviderImpl);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String[] getPropertyNames(boolean paramBoolean)
/*     */   {
/* 521 */     return getPropertyNames(paramBoolean, null);
/*     */   }
/*     */ 
/*     */   private static String[] getPropertyNames(boolean paramBoolean, String paramString)
/*     */   {
/* 530 */     String str = ".";
/* 531 */     String[] arrayOfString = { "rowset.provider.classname", "rowset.provider.vendor", "rowset.provider.version" };
/*     */ 
/* 535 */     if (paramBoolean) {
/* 536 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 537 */         arrayOfString[i] = (arrayOfString[i] + str + paramString);
/*     */       }
/*     */ 
/* 541 */       return arrayOfString;
/*     */     }
/* 543 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static void showImpl(ProviderImpl paramProviderImpl)
/*     */   {
/* 551 */     System.out.println("Provider implementation:");
/* 552 */     System.out.println("Classname: " + paramProviderImpl.getClassname());
/* 553 */     System.out.println("Vendor: " + paramProviderImpl.getVendor());
/* 554 */     System.out.println("Version: " + paramProviderImpl.getVersion());
/* 555 */     System.out.println("Impl index: " + paramProviderImpl.getIndex());
/*     */   }
/*     */ 
/*     */   public static SyncProvider getInstance(String paramString)
/*     */     throws SyncFactoryException
/*     */   {
/* 570 */     if (paramString == null) {
/* 571 */       throw new SyncFactoryException("The providerID cannot be null");
/*     */     }
/*     */ 
/* 574 */     initMapIfNecessary();
/* 575 */     initJNDIContext();
/*     */ 
/* 577 */     ProviderImpl localProviderImpl = (ProviderImpl)implementations.get(paramString);
/*     */ 
/* 579 */     if (localProviderImpl == null)
/*     */     {
/* 581 */       return new RIOptimisticProvider();
/*     */     }
/*     */     Object localObject;
/*     */     try {
/* 585 */       ReflectUtil.checkPackageAccess(paramString);
/*     */     } catch (AccessControlException localAccessControlException) {
/* 587 */       localObject = new SyncFactoryException();
/* 588 */       ((SyncFactoryException)localObject).initCause(localAccessControlException);
/* 589 */       throw ((Throwable)localObject);
/*     */     }
/*     */ 
/* 592 */     Class localClass = null;
/*     */     try {
/* 594 */       localObject = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 602 */       localClass = Class.forName(paramString, true, (ClassLoader)localObject);
/*     */ 
/* 604 */       if (localClass != null) {
/* 605 */         return (SyncProvider)localClass.newInstance();
/*     */       }
/* 607 */       return new RIOptimisticProvider();
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException)
/*     */     {
/* 611 */       throw new SyncFactoryException("IllegalAccessException: " + localIllegalAccessException.getMessage());
/*     */     } catch (InstantiationException localInstantiationException) {
/* 613 */       throw new SyncFactoryException("InstantiationException: " + localInstantiationException.getMessage());
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 615 */       throw new SyncFactoryException("ClassNotFoundException: " + localClassNotFoundException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Enumeration<SyncProvider> getRegisteredProviders()
/*     */     throws SyncFactoryException
/*     */   {
/* 633 */     initMapIfNecessary();
/*     */ 
/* 636 */     return implementations.elements();
/*     */   }
/*     */ 
/*     */   public static void setLogger(Logger paramLogger)
/*     */   {
/* 662 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 663 */     if (localSecurityManager != null) {
/* 664 */       localSecurityManager.checkPermission(SET_SYNCFACTORY_PERMISSION);
/*     */     }
/*     */ 
/* 667 */     if (paramLogger == null) {
/* 668 */       throw new NullPointerException("You must provide a Logger");
/*     */     }
/* 670 */     rsLogger = paramLogger;
/*     */   }
/*     */ 
/*     */   public static void setLogger(Logger paramLogger, Level paramLevel)
/*     */   {
/* 701 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 702 */     if (localSecurityManager != null) {
/* 703 */       localSecurityManager.checkPermission(SET_SYNCFACTORY_PERMISSION);
/*     */     }
/*     */ 
/* 706 */     if (paramLogger == null) {
/* 707 */       throw new NullPointerException("You must provide a Logger");
/*     */     }
/* 709 */     paramLogger.setLevel(paramLevel);
/* 710 */     rsLogger = paramLogger;
/*     */   }
/*     */ 
/*     */   public static Logger getLogger()
/*     */     throws SyncFactoryException
/*     */   {
/* 721 */     Logger localLogger = rsLogger;
/*     */ 
/* 723 */     if (localLogger == null) {
/* 724 */       throw new SyncFactoryException("(SyncFactory) : No logger has been set");
/*     */     }
/*     */ 
/* 727 */     return localLogger;
/*     */   }
/*     */ 
/*     */   public static synchronized void setJNDIContext(Context paramContext)
/*     */     throws SyncFactoryException
/*     */   {
/* 750 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 751 */     if (localSecurityManager != null) {
/* 752 */       localSecurityManager.checkPermission(SET_SYNCFACTORY_PERMISSION);
/*     */     }
/* 754 */     if (paramContext == null) {
/* 755 */       throw new SyncFactoryException("Invalid JNDI context supplied");
/*     */     }
/* 757 */     ic = paramContext;
/*     */   }
/*     */ 
/*     */   private static synchronized void initJNDIContext()
/*     */     throws SyncFactoryException
/*     */   {
/* 767 */     if ((ic != null) && (!lazyJNDICtxRefresh))
/*     */       try {
/* 769 */         parseProperties(parseJNDIContext());
/* 770 */         lazyJNDICtxRefresh = true;
/*     */       } catch (NamingException localNamingException) {
/* 772 */         localNamingException.printStackTrace();
/* 773 */         throw new SyncFactoryException("SPI: NamingException: " + localNamingException.getExplanation());
/*     */       } catch (Exception localException) {
/* 775 */         localException.printStackTrace();
/* 776 */         throw new SyncFactoryException("SPI: Exception: " + localException.getMessage());
/*     */       }
/*     */   }
/*     */ 
/*     */   private static Properties parseJNDIContext()
/*     */     throws NamingException
/*     */   {
/* 791 */     NamingEnumeration localNamingEnumeration = ic.listBindings("");
/* 792 */     Properties localProperties = new Properties();
/*     */ 
/* 795 */     enumerateBindings(localNamingEnumeration, localProperties);
/*     */ 
/* 797 */     return localProperties;
/*     */   }
/*     */ 
/*     */   private static void enumerateBindings(NamingEnumeration paramNamingEnumeration, Properties paramProperties)
/*     */     throws NamingException
/*     */   {
/* 809 */     int i = 0;
/*     */     try
/*     */     {
/* 812 */       Binding localBinding = null;
/* 813 */       Object localObject = null;
/* 814 */       String str = null;
/* 815 */       while (paramNamingEnumeration.hasMore()) {
/* 816 */         localBinding = (Binding)paramNamingEnumeration.next();
/* 817 */         str = localBinding.getName();
/* 818 */         localObject = localBinding.getObject();
/*     */ 
/* 820 */         if (!(ic.lookup(str) instanceof Context))
/*     */         {
/* 822 */           if ((ic.lookup(str) instanceof SyncProvider)) {
/* 823 */             i = 1;
/*     */           }
/*     */         }
/*     */ 
/* 827 */         if (i != 0) {
/* 828 */           SyncProvider localSyncProvider = (SyncProvider)localObject;
/* 829 */           paramProperties.put("rowset.provider.classname", localSyncProvider.getProviderID());
/*     */ 
/* 831 */           i = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (NotContextException localNotContextException) {
/* 836 */       paramNamingEnumeration.next();
/*     */ 
/* 838 */       enumerateBindings(paramNamingEnumeration, paramProperties);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SyncFactoryHolder
/*     */   {
/* 846 */     static final SyncFactory factory = new SyncFactory(null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sql.rowset.spi.SyncFactory
 * JD-Core Version:    0.6.2
 */
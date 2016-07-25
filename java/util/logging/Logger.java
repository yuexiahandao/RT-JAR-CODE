/*      */ package java.util.logging;
/*      */ 
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.Reflection;
/*      */ 
/*      */ public class Logger
/*      */ {
/*  175 */   private static final Handler[] emptyHandlers = new Handler[0];
/*  176 */   private static final int offValue = Level.OFF.intValue();
/*      */   private volatile LogManager manager;
/*      */   private String name;
/*  179 */   private final CopyOnWriteArrayList<Handler> handlers = new CopyOnWriteArrayList();
/*      */   private volatile String resourceBundleName;
/*  182 */   private volatile boolean useParentHandlers = true;
/*      */   private volatile Filter filter;
/*      */   private boolean anonymous;
/*      */   private ResourceBundle catalog;
/*      */   private String catalogName;
/*      */   private Locale catalogLocale;
/*  192 */   private static final Object treeLock = new Object();
/*      */   private volatile Logger parent;
/*      */   private ArrayList<LogManager.LoggerWeakRef> kids;
/*      */   private volatile Level levelObject;
/*      */   private volatile int levelValue;
/*      */   private WeakReference<ClassLoader> callersClassLoaderRef;
/*      */   private final boolean isSystemLogger;
/*      */   public static final String GLOBAL_LOGGER_NAME = "global";
/*      */ 
/*      */   @Deprecated
/*  241 */   public static final Logger global = new Logger("global");
/*      */   static final String SYSTEM_LOGGER_RB_NAME = "sun.util.logging.resources.logging";
/*      */ 
/*      */   public static final Logger getGlobal()
/*      */   {
/*  216 */     return global;
/*      */   }
/*      */ 
/*      */   protected Logger(String paramString1, String paramString2)
/*      */   {
/*  261 */     this(paramString1, paramString2, null, false);
/*      */   }
/*      */ 
/*      */   Logger(String paramString1, String paramString2, Class<?> paramClass, boolean paramBoolean) {
/*  265 */     this.manager = LogManager.getLogManager();
/*  266 */     this.isSystemLogger = paramBoolean;
/*  267 */     setupResourceInfo(paramString2, paramClass);
/*  268 */     this.name = paramString1;
/*  269 */     this.levelValue = Level.INFO.intValue();
/*      */   }
/*      */ 
/*      */   private void setCallersClassLoaderRef(Class<?> paramClass) {
/*  273 */     Object localObject = paramClass != null ? paramClass.getClassLoader() : null;
/*      */ 
/*  276 */     if (localObject != null)
/*  277 */       this.callersClassLoaderRef = new WeakReference(localObject);
/*      */   }
/*      */ 
/*      */   private ClassLoader getCallersClassLoader()
/*      */   {
/*  282 */     return this.callersClassLoaderRef != null ? (ClassLoader)this.callersClassLoaderRef.get() : null;
/*      */   }
/*      */ 
/*      */   private Logger(String paramString)
/*      */   {
/*  292 */     this.name = paramString;
/*  293 */     this.isSystemLogger = true;
/*  294 */     this.levelValue = Level.INFO.intValue();
/*      */   }
/*      */ 
/*      */   void setLogManager(LogManager paramLogManager)
/*      */   {
/*  300 */     this.manager = paramLogManager;
/*      */   }
/*      */ 
/*      */   private void checkPermission() throws SecurityException {
/*  304 */     if (!this.anonymous) {
/*  305 */       if (this.manager == null)
/*      */       {
/*  307 */         this.manager = LogManager.getLogManager();
/*      */       }
/*  309 */       this.manager.checkPermission();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Logger demandLogger(String paramString1, String paramString2, Class<?> paramClass)
/*      */   {
/*  339 */     LogManager localLogManager = LogManager.getLogManager();
/*  340 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  341 */     if ((localSecurityManager != null) && (!LoggerHelper.disableCallerCheck) && 
/*  342 */       (paramClass.getClassLoader() == null)) {
/*  343 */       return localLogManager.demandSystemLogger(paramString1, paramString2);
/*      */     }
/*      */ 
/*  346 */     return localLogManager.demandLogger(paramString1, paramString2, paramClass);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static Logger getLogger(String paramString)
/*      */   {
/*  393 */     return demandLogger(paramString, null, Reflection.getCallerClass());
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static Logger getLogger(String paramString1, String paramString2)
/*      */   {
/*  441 */     Class localClass = Reflection.getCallerClass();
/*  442 */     Logger localLogger = demandLogger(paramString1, paramString2, localClass);
/*      */ 
/*  444 */     if (localLogger.resourceBundleName == null)
/*      */     {
/*  455 */       localLogger.setupResourceInfo(paramString2, localClass);
/*  456 */     } else if (!localLogger.resourceBundleName.equals(paramString2))
/*      */     {
/*  459 */       throw new IllegalArgumentException(localLogger.resourceBundleName + " != " + paramString2);
/*      */     }
/*      */ 
/*  462 */     return localLogger;
/*      */   }
/*      */ 
/*      */   static Logger getPlatformLogger(String paramString)
/*      */   {
/*  469 */     LogManager localLogManager = LogManager.getLogManager();
/*      */ 
/*  473 */     Logger localLogger = localLogManager.demandSystemLogger(paramString, "sun.util.logging.resources.logging");
/*  474 */     return localLogger;
/*      */   }
/*      */ 
/*      */   public static Logger getAnonymousLogger()
/*      */   {
/*  498 */     return getAnonymousLogger(null);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public static Logger getAnonymousLogger(String paramString)
/*      */   {
/*  530 */     LogManager localLogManager = LogManager.getLogManager();
/*      */ 
/*  532 */     localLogManager.drainLoggerRefQueueBounded();
/*  533 */     Logger localLogger1 = new Logger(null, paramString, Reflection.getCallerClass(), false);
/*      */ 
/*  535 */     localLogger1.anonymous = true;
/*  536 */     Logger localLogger2 = localLogManager.getLogger("");
/*  537 */     localLogger1.doSetParent(localLogger2);
/*  538 */     return localLogger1;
/*      */   }
/*      */ 
/*      */   public ResourceBundle getResourceBundle()
/*      */   {
/*  550 */     return findResourceBundle(getResourceBundleName(), true);
/*      */   }
/*      */ 
/*      */   public String getResourceBundleName()
/*      */   {
/*  561 */     return this.resourceBundleName;
/*      */   }
/*      */ 
/*      */   public void setFilter(Filter paramFilter)
/*      */     throws SecurityException
/*      */   {
/*  576 */     checkPermission();
/*  577 */     this.filter = paramFilter;
/*      */   }
/*      */ 
/*      */   public Filter getFilter()
/*      */   {
/*  586 */     return this.filter;
/*      */   }
/*      */ 
/*      */   public void log(LogRecord paramLogRecord)
/*      */   {
/*  599 */     if ((paramLogRecord.getLevel().intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  600 */       return;
/*      */     }
/*  602 */     Filter localFilter = this.filter;
/*  603 */     if ((localFilter != null) && (!localFilter.isLoggable(paramLogRecord))) {
/*  604 */       return;
/*      */     }
/*      */ 
/*  610 */     Logger localLogger = this;
/*  611 */     while (localLogger != null) {
/*  612 */       Handler[] arrayOfHandler1 = this.isSystemLogger ? localLogger.accessCheckedHandlers() : localLogger.getHandlers();
/*      */ 
/*  615 */       for (Handler localHandler : arrayOfHandler1) {
/*  616 */         localHandler.publish(paramLogRecord);
/*      */       }
/*      */ 
/*  619 */       boolean bool = this.isSystemLogger ? localLogger.useParentHandlers : localLogger.getUseParentHandlers();
/*      */ 
/*  623 */       if (!bool)
/*      */       {
/*      */         break;
/*      */       }
/*  627 */       localLogger = this.isSystemLogger ? localLogger.parent : localLogger.getParent();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doLog(LogRecord paramLogRecord)
/*      */   {
/*  635 */     paramLogRecord.setLoggerName(this.name);
/*  636 */     String str = getEffectiveResourceBundleName();
/*  637 */     if ((str != null) && (!str.equals("sun.util.logging.resources.logging"))) {
/*  638 */       paramLogRecord.setResourceBundleName(str);
/*  639 */       paramLogRecord.setResourceBundle(findResourceBundle(str, true));
/*      */     }
/*  641 */     log(paramLogRecord);
/*      */   }
/*      */ 
/*      */   public void log(Level paramLevel, String paramString)
/*      */   {
/*  660 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  661 */       return;
/*      */     }
/*  663 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString);
/*  664 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   public void log(Level paramLevel, String paramString, Object paramObject)
/*      */   {
/*  679 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  680 */       return;
/*      */     }
/*  682 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString);
/*  683 */     Object[] arrayOfObject = { paramObject };
/*  684 */     localLogRecord.setParameters(arrayOfObject);
/*  685 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   public void log(Level paramLevel, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  700 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  701 */       return;
/*      */     }
/*  703 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString);
/*  704 */     localLogRecord.setParameters(paramArrayOfObject);
/*  705 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   public void log(Level paramLevel, String paramString, Throwable paramThrowable)
/*      */   {
/*  725 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  726 */       return;
/*      */     }
/*  728 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString);
/*  729 */     localLogRecord.setThrown(paramThrowable);
/*  730 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   public void logp(Level paramLevel, String paramString1, String paramString2, String paramString3)
/*      */   {
/*  751 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  752 */       return;
/*      */     }
/*  754 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString3);
/*  755 */     localLogRecord.setSourceClassName(paramString1);
/*  756 */     localLogRecord.setSourceMethodName(paramString2);
/*  757 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   public void logp(Level paramLevel, String paramString1, String paramString2, String paramString3, Object paramObject)
/*      */   {
/*  776 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  777 */       return;
/*      */     }
/*  779 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString3);
/*  780 */     localLogRecord.setSourceClassName(paramString1);
/*  781 */     localLogRecord.setSourceMethodName(paramString2);
/*  782 */     Object[] arrayOfObject = { paramObject };
/*  783 */     localLogRecord.setParameters(arrayOfObject);
/*  784 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   public void logp(Level paramLevel, String paramString1, String paramString2, String paramString3, Object[] paramArrayOfObject)
/*      */   {
/*  803 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  804 */       return;
/*      */     }
/*  806 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString3);
/*  807 */     localLogRecord.setSourceClassName(paramString1);
/*  808 */     localLogRecord.setSourceMethodName(paramString2);
/*  809 */     localLogRecord.setParameters(paramArrayOfObject);
/*  810 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   public void logp(Level paramLevel, String paramString1, String paramString2, String paramString3, Throwable paramThrowable)
/*      */   {
/*  834 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  835 */       return;
/*      */     }
/*  837 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString3);
/*  838 */     localLogRecord.setSourceClassName(paramString1);
/*  839 */     localLogRecord.setSourceMethodName(paramString2);
/*  840 */     localLogRecord.setThrown(paramThrowable);
/*  841 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   private void doLog(LogRecord paramLogRecord, String paramString)
/*      */   {
/*  853 */     paramLogRecord.setLoggerName(this.name);
/*  854 */     if (paramString != null) {
/*  855 */       paramLogRecord.setResourceBundleName(paramString);
/*  856 */       paramLogRecord.setResourceBundle(findResourceBundle(paramString, false));
/*      */     }
/*  858 */     log(paramLogRecord);
/*      */   }
/*      */ 
/*      */   public void logrb(Level paramLevel, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*  882 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  883 */       return;
/*      */     }
/*  885 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString4);
/*  886 */     localLogRecord.setSourceClassName(paramString1);
/*  887 */     localLogRecord.setSourceMethodName(paramString2);
/*  888 */     doLog(localLogRecord, paramString3);
/*      */   }
/*      */ 
/*      */   public void logrb(Level paramLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object paramObject)
/*      */   {
/*  913 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  914 */       return;
/*      */     }
/*  916 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString4);
/*  917 */     localLogRecord.setSourceClassName(paramString1);
/*  918 */     localLogRecord.setSourceMethodName(paramString2);
/*  919 */     Object[] arrayOfObject = { paramObject };
/*  920 */     localLogRecord.setParameters(arrayOfObject);
/*  921 */     doLog(localLogRecord, paramString3);
/*      */   }
/*      */ 
/*      */   public void logrb(Level paramLevel, String paramString1, String paramString2, String paramString3, String paramString4, Object[] paramArrayOfObject)
/*      */   {
/*  946 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  947 */       return;
/*      */     }
/*  949 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString4);
/*  950 */     localLogRecord.setSourceClassName(paramString1);
/*  951 */     localLogRecord.setSourceMethodName(paramString2);
/*  952 */     localLogRecord.setParameters(paramArrayOfObject);
/*  953 */     doLog(localLogRecord, paramString3);
/*      */   }
/*      */ 
/*      */   public void logrb(Level paramLevel, String paramString1, String paramString2, String paramString3, String paramString4, Throwable paramThrowable)
/*      */   {
/*  983 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/*  984 */       return;
/*      */     }
/*  986 */     LogRecord localLogRecord = new LogRecord(paramLevel, paramString4);
/*  987 */     localLogRecord.setSourceClassName(paramString1);
/*  988 */     localLogRecord.setSourceMethodName(paramString2);
/*  989 */     localLogRecord.setThrown(paramThrowable);
/*  990 */     doLog(localLogRecord, paramString3);
/*      */   }
/*      */ 
/*      */   public void entering(String paramString1, String paramString2)
/*      */   {
/* 1009 */     if (Level.FINER.intValue() < this.levelValue) {
/* 1010 */       return;
/*      */     }
/* 1012 */     logp(Level.FINER, paramString1, paramString2, "ENTRY");
/*      */   }
/*      */ 
/*      */   public void entering(String paramString1, String paramString2, Object paramObject)
/*      */   {
/* 1028 */     if (Level.FINER.intValue() < this.levelValue) {
/* 1029 */       return;
/*      */     }
/* 1031 */     Object[] arrayOfObject = { paramObject };
/* 1032 */     logp(Level.FINER, paramString1, paramString2, "ENTRY {0}", arrayOfObject);
/*      */   }
/*      */ 
/*      */   public void entering(String paramString1, String paramString2, Object[] paramArrayOfObject)
/*      */   {
/* 1049 */     if (Level.FINER.intValue() < this.levelValue) {
/* 1050 */       return;
/*      */     }
/* 1052 */     String str = "ENTRY";
/* 1053 */     if (paramArrayOfObject == null) {
/* 1054 */       logp(Level.FINER, paramString1, paramString2, str);
/* 1055 */       return;
/*      */     }
/* 1057 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 1058 */       str = str + " {" + i + "}";
/*      */     }
/* 1060 */     logp(Level.FINER, paramString1, paramString2, str, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public void exiting(String paramString1, String paramString2)
/*      */   {
/* 1074 */     if (Level.FINER.intValue() < this.levelValue) {
/* 1075 */       return;
/*      */     }
/* 1077 */     logp(Level.FINER, paramString1, paramString2, "RETURN");
/*      */   }
/*      */ 
/*      */   public void exiting(String paramString1, String paramString2, Object paramObject)
/*      */   {
/* 1094 */     if (Level.FINER.intValue() < this.levelValue) {
/* 1095 */       return;
/*      */     }
/* 1097 */     Object[] arrayOfObject = { paramObject };
/* 1098 */     logp(Level.FINER, paramString1, paramString2, "RETURN {0}", paramObject);
/*      */   }
/*      */ 
/*      */   public void throwing(String paramString1, String paramString2, Throwable paramThrowable)
/*      */   {
/* 1123 */     if ((Level.FINER.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/* 1124 */       return;
/*      */     }
/* 1126 */     LogRecord localLogRecord = new LogRecord(Level.FINER, "THROW");
/* 1127 */     localLogRecord.setSourceClassName(paramString1);
/* 1128 */     localLogRecord.setSourceMethodName(paramString2);
/* 1129 */     localLogRecord.setThrown(paramThrowable);
/* 1130 */     doLog(localLogRecord);
/*      */   }
/*      */ 
/*      */   public void severe(String paramString)
/*      */   {
/* 1147 */     if (Level.SEVERE.intValue() < this.levelValue) {
/* 1148 */       return;
/*      */     }
/* 1150 */     log(Level.SEVERE, paramString);
/*      */   }
/*      */ 
/*      */   public void warning(String paramString)
/*      */   {
/* 1163 */     if (Level.WARNING.intValue() < this.levelValue) {
/* 1164 */       return;
/*      */     }
/* 1166 */     log(Level.WARNING, paramString);
/*      */   }
/*      */ 
/*      */   public void info(String paramString)
/*      */   {
/* 1179 */     if (Level.INFO.intValue() < this.levelValue) {
/* 1180 */       return;
/*      */     }
/* 1182 */     log(Level.INFO, paramString);
/*      */   }
/*      */ 
/*      */   public void config(String paramString)
/*      */   {
/* 1195 */     if (Level.CONFIG.intValue() < this.levelValue) {
/* 1196 */       return;
/*      */     }
/* 1198 */     log(Level.CONFIG, paramString);
/*      */   }
/*      */ 
/*      */   public void fine(String paramString)
/*      */   {
/* 1211 */     if (Level.FINE.intValue() < this.levelValue) {
/* 1212 */       return;
/*      */     }
/* 1214 */     log(Level.FINE, paramString);
/*      */   }
/*      */ 
/*      */   public void finer(String paramString)
/*      */   {
/* 1227 */     if (Level.FINER.intValue() < this.levelValue) {
/* 1228 */       return;
/*      */     }
/* 1230 */     log(Level.FINER, paramString);
/*      */   }
/*      */ 
/*      */   public void finest(String paramString)
/*      */   {
/* 1243 */     if (Level.FINEST.intValue() < this.levelValue) {
/* 1244 */       return;
/*      */     }
/* 1246 */     log(Level.FINEST, paramString);
/*      */   }
/*      */ 
/*      */   public void setLevel(Level paramLevel)
/*      */     throws SecurityException
/*      */   {
/* 1268 */     checkPermission();
/* 1269 */     synchronized (treeLock) {
/* 1270 */       this.levelObject = paramLevel;
/* 1271 */       updateEffectiveLevel();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Level getLevel()
/*      */   {
/* 1283 */     return this.levelObject;
/*      */   }
/*      */ 
/*      */   public boolean isLoggable(Level paramLevel)
/*      */   {
/* 1295 */     if ((paramLevel.intValue() < this.levelValue) || (this.levelValue == offValue)) {
/* 1296 */       return false;
/*      */     }
/* 1298 */     return true;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/* 1306 */     return this.name;
/*      */   }
/*      */ 
/*      */   public void addHandler(Handler paramHandler)
/*      */     throws SecurityException
/*      */   {
/* 1322 */     paramHandler.getClass();
/* 1323 */     checkPermission();
/* 1324 */     this.handlers.add(paramHandler);
/*      */   }
/*      */ 
/*      */   public void removeHandler(Handler paramHandler)
/*      */     throws SecurityException
/*      */   {
/* 1337 */     checkPermission();
/* 1338 */     if (paramHandler == null) {
/* 1339 */       return;
/*      */     }
/* 1341 */     this.handlers.remove(paramHandler);
/*      */   }
/*      */ 
/*      */   public Handler[] getHandlers()
/*      */   {
/* 1350 */     return accessCheckedHandlers();
/*      */   }
/*      */ 
/*      */   Handler[] accessCheckedHandlers()
/*      */   {
/* 1356 */     return (Handler[])this.handlers.toArray(emptyHandlers);
/*      */   }
/*      */ 
/*      */   public void setUseParentHandlers(boolean paramBoolean)
/*      */   {
/* 1371 */     checkPermission();
/* 1372 */     this.useParentHandlers = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getUseParentHandlers()
/*      */   {
/* 1382 */     return this.useParentHandlers;
/*      */   }
/*      */ 
/*      */   private static ResourceBundle findSystemResourceBundle(Locale paramLocale)
/*      */   {
/* 1389 */     return (ResourceBundle)AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public ResourceBundle run() {
/*      */         try {
/* 1392 */           return ResourceBundle.getBundle("sun.util.logging.resources.logging", this.val$locale, ClassLoader.getSystemClassLoader());
/*      */         }
/*      */         catch (MissingResourceException localMissingResourceException)
/*      */         {
/* 1396 */           throw new InternalError(localMissingResourceException.toString());
/*      */         }
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private synchronized ResourceBundle findResourceBundle(String paramString, boolean paramBoolean)
/*      */   {
/* 1423 */     if (paramString == null) {
/* 1424 */       return null;
/*      */     }
/*      */ 
/* 1427 */     Locale localLocale = Locale.getDefault();
/*      */ 
/* 1430 */     if ((this.catalog != null) && (localLocale.equals(this.catalogLocale)) && (paramString.equals(this.catalogName)))
/*      */     {
/* 1432 */       return this.catalog;
/*      */     }
/*      */ 
/* 1435 */     if (paramString.equals("sun.util.logging.resources.logging")) {
/* 1436 */       this.catalog = findSystemResourceBundle(localLocale);
/* 1437 */       this.catalogName = paramString;
/* 1438 */       this.catalogLocale = localLocale;
/* 1439 */       return this.catalog;
/*      */     }
/*      */ 
/* 1444 */     ClassLoader localClassLoader1 = Thread.currentThread().getContextClassLoader();
/* 1445 */     if (localClassLoader1 == null)
/* 1446 */       localClassLoader1 = ClassLoader.getSystemClassLoader();
/*      */     try
/*      */     {
/* 1449 */       this.catalog = ResourceBundle.getBundle(paramString, localLocale, localClassLoader1);
/* 1450 */       this.catalogName = paramString;
/* 1451 */       this.catalogLocale = localLocale;
/* 1452 */       return this.catalog;
/*      */     }
/*      */     catch (MissingResourceException localMissingResourceException1)
/*      */     {
/* 1458 */       if (paramBoolean)
/*      */       {
/* 1460 */         ClassLoader localClassLoader2 = getCallersClassLoader();
/* 1461 */         if ((localClassLoader2 != null) && (localClassLoader2 != localClassLoader1)) {
/*      */           try {
/* 1463 */             this.catalog = ResourceBundle.getBundle(paramString, localLocale, localClassLoader2);
/*      */ 
/* 1465 */             this.catalogName = paramString;
/* 1466 */             this.catalogLocale = localLocale;
/* 1467 */             return this.catalog;
/*      */           }
/*      */           catch (MissingResourceException localMissingResourceException2)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1475 */       if (LoggerHelper.allowStackWalkSearch)
/* 1476 */         return findResourceBundleFromStack(paramString, localLocale, localClassLoader1);
/*      */     }
/* 1478 */     return null;
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   private synchronized ResourceBundle findResourceBundleFromStack(String paramString, Locale paramLocale, ClassLoader paramClassLoader)
/*      */   {
/* 1491 */     for (int i = 0; ; i++) {
/* 1492 */       Class localClass = Reflection.getCallerClass(i);
/* 1493 */       if (localClass == null) {
/*      */         break;
/*      */       }
/* 1496 */       ClassLoader localClassLoader = localClass.getClassLoader();
/* 1497 */       if (localClassLoader == null) {
/* 1498 */         localClassLoader = ClassLoader.getSystemClassLoader();
/*      */       }
/* 1500 */       if (paramClassLoader != localClassLoader)
/*      */       {
/* 1504 */         paramClassLoader = localClassLoader;
/*      */         try {
/* 1506 */           this.catalog = ResourceBundle.getBundle(paramString, paramLocale, paramClassLoader);
/* 1507 */           this.catalogName = paramString;
/* 1508 */           this.catalogLocale = paramLocale;
/* 1509 */           return this.catalog; } catch (MissingResourceException localMissingResourceException) {
/*      */         }
/*      */       }
/*      */     }
/* 1513 */     return null;
/*      */   }
/*      */ 
/*      */   private synchronized void setupResourceInfo(String paramString, Class<?> paramClass)
/*      */   {
/* 1524 */     if (paramString == null) {
/* 1525 */       return;
/*      */     }
/*      */ 
/* 1528 */     setCallersClassLoaderRef(paramClass);
/* 1529 */     if ((this.isSystemLogger) && (getCallersClassLoader() != null)) {
/* 1530 */       checkPermission();
/*      */     }
/* 1532 */     if (findResourceBundle(paramString, true) == null)
/*      */     {
/* 1536 */       this.callersClassLoaderRef = null;
/* 1537 */       throw new MissingResourceException("Can't find " + paramString + " bundle", paramString, "");
/*      */     }
/*      */ 
/* 1540 */     this.resourceBundleName = paramString;
/*      */   }
/*      */ 
/*      */   public Logger getParent()
/*      */   {
/* 1562 */     return this.parent;
/*      */   }
/*      */ 
/*      */   public void setParent(Logger paramLogger)
/*      */   {
/* 1576 */     if (paramLogger == null) {
/* 1577 */       throw new NullPointerException();
/*      */     }
/* 1579 */     if (this.manager == null) {
/* 1580 */       this.manager = LogManager.getLogManager();
/*      */     }
/* 1582 */     this.manager.checkPermission();
/* 1583 */     doSetParent(paramLogger);
/*      */   }
/*      */ 
/*      */   private void doSetParent(Logger paramLogger)
/*      */   {
/* 1593 */     synchronized (treeLock)
/*      */     {
/* 1596 */       LogManager.LoggerWeakRef localLoggerWeakRef = null;
/*      */       Iterator localIterator;
/* 1597 */       if (this.parent != null)
/*      */       {
/* 1599 */         for (localIterator = this.parent.kids.iterator(); localIterator.hasNext(); ) {
/* 1600 */           localLoggerWeakRef = (LogManager.LoggerWeakRef)localIterator.next();
/* 1601 */           Logger localLogger = (Logger)localLoggerWeakRef.get();
/* 1602 */           if (localLogger == this)
/*      */           {
/* 1604 */             localIterator.remove();
/* 1605 */             break;
/*      */           }
/* 1607 */           localLoggerWeakRef = null;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1614 */       this.parent = paramLogger;
/* 1615 */       if (this.parent.kids == null) {
/* 1616 */         this.parent.kids = new ArrayList(2);
/*      */       }
/* 1618 */       if (localLoggerWeakRef == null)
/*      */       {
/*      */         LogManager tmp120_117 = this.manager; tmp120_117.getClass(); localLoggerWeakRef = new LogManager.LoggerWeakRef(tmp120_117, this);
/*      */       }
/* 1622 */       localLoggerWeakRef.setParentRef(new WeakReference(this.parent));
/* 1623 */       this.parent.kids.add(localLoggerWeakRef);
/*      */ 
/* 1627 */       updateEffectiveLevel();
/*      */     }
/*      */   }
/*      */ 
/*      */   final void removeChildLogger(LogManager.LoggerWeakRef paramLoggerWeakRef)
/*      */   {
/*      */     Iterator localIterator;
/* 1636 */     synchronized (treeLock) {
/* 1637 */       for (localIterator = this.kids.iterator(); localIterator.hasNext(); ) {
/* 1638 */         LogManager.LoggerWeakRef localLoggerWeakRef = (LogManager.LoggerWeakRef)localIterator.next();
/* 1639 */         if (localLoggerWeakRef == paramLoggerWeakRef) {
/* 1640 */           localIterator.remove();
/* 1641 */           return;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateEffectiveLevel()
/*      */   {
/*      */     int i;
/* 1655 */     if (this.levelObject != null) {
/* 1656 */       i = this.levelObject.intValue();
/*      */     }
/* 1658 */     else if (this.parent != null) {
/* 1659 */       i = this.parent.levelValue;
/*      */     }
/*      */     else {
/* 1662 */       i = Level.INFO.intValue();
/*      */     }
/*      */ 
/* 1667 */     if (this.levelValue == i) {
/* 1668 */       return;
/*      */     }
/*      */ 
/* 1671 */     this.levelValue = i;
/*      */ 
/* 1676 */     if (this.kids != null)
/* 1677 */       for (int j = 0; j < this.kids.size(); j++) {
/* 1678 */         LogManager.LoggerWeakRef localLoggerWeakRef = (LogManager.LoggerWeakRef)this.kids.get(j);
/* 1679 */         Logger localLogger = (Logger)localLoggerWeakRef.get();
/* 1680 */         if (localLogger != null)
/* 1681 */           localLogger.updateEffectiveLevel();
/*      */       }
/*      */   }
/*      */ 
/*      */   private String getEffectiveResourceBundleName()
/*      */   {
/* 1692 */     Logger localLogger = this;
/* 1693 */     while (localLogger != null) {
/* 1694 */       String str = this.isSystemLogger ? null : localLogger.isSystemLogger ? localLogger.resourceBundleName : localLogger.getResourceBundleName();
/*      */ 
/* 1699 */       if (str != null) {
/* 1700 */         return str;
/*      */       }
/* 1702 */       localLogger = this.isSystemLogger ? localLogger.parent : localLogger.getParent();
/*      */     }
/* 1704 */     return null;
/*      */   }
/*      */ 
/*      */   private static class LoggerHelper
/*      */   {
/*  322 */     static boolean disableCallerCheck = getBooleanProperty("sun.util.logging.disableCallerCheck");
/*      */ 
/*  326 */     static boolean allowStackWalkSearch = getBooleanProperty("jdk.logging.allowStackWalkSearch");
/*      */ 
/*      */     private static boolean getBooleanProperty(String paramString) {
/*  329 */       String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public String run() {
/*  331 */           return System.getProperty(this.val$key);
/*      */         }
/*      */       });
/*  334 */       return Boolean.valueOf(str).booleanValue();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.Logger
 * JD-Core Version:    0.6.2
 */
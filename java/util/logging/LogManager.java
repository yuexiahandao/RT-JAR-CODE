/*      */ package java.util.logging;
/*      */ 
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.security.AccessController;
/*      */ import java.security.Permission;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.Vector;
/*      */ import java.util.WeakHashMap;
/*      */ import sun.misc.JavaAWTAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ public class LogManager
/*      */ {
/*      */   private static LogManager manager;
/*  159 */   private volatile Properties props = new Properties();
/*      */ 
/*  161 */   private PropertyChangeSupport changes = new PropertyChangeSupport(LogManager.class);
/*      */ 
/*  163 */   private static final Level defaultLevel = Level.INFO;
/*      */ 
/*  166 */   private final LoggerContext systemContext = new SystemLoggerContext();
/*  167 */   private final LoggerContext userContext = new LoggerContext(null);
/*      */   private Logger rootLogger;
/*      */   private volatile boolean readPrimordialConfiguration;
/*  176 */   private boolean initializedGlobalHandlers = true;
/*      */   private boolean deathImminent;
/*  375 */   private static WeakHashMap<Object, LoggerContext> contextsMap = null;
/*      */ 
/*  838 */   private final ReferenceQueue<Logger> loggerRefQueue = new ReferenceQueue();
/*      */   private static final int MAX_ITERATIONS = 400;
/* 1409 */   private final Permission controlPermission = new LoggingPermission("control", null);
/*      */ 
/* 1527 */   private static LoggingMXBean loggingMXBean = null;
/*      */   public static final String LOGGING_MXBEAN_NAME = "java.util.logging:type=Logging";
/*      */ 
/*      */   protected LogManager()
/*      */   {
/*  268 */     this(checkSubclassPermissions());
/*      */   }
/*      */ 
/*      */   private LogManager(Void paramVoid)
/*      */   {
/*      */     try
/*      */     {
/*  275 */       Runtime.getRuntime().addShutdownHook(new Cleaner(null));
/*      */     }
/*      */     catch (IllegalStateException localIllegalStateException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Void checkSubclassPermissions() {
/*  283 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  284 */     if (localSecurityManager != null)
/*      */     {
/*  289 */       localSecurityManager.checkPermission(new RuntimePermission("shutdownHooks"));
/*  290 */       localSecurityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
/*      */     }
/*  292 */     return null;
/*      */   }
/*      */ 
/*      */   public static LogManager getLogManager()
/*      */   {
/*  299 */     if (manager != null) {
/*  300 */       manager.readPrimordialConfiguration();
/*      */     }
/*  302 */     return manager;
/*      */   }
/*      */ 
/*      */   private void readPrimordialConfiguration() {
/*  306 */     if (!this.readPrimordialConfiguration)
/*  307 */       synchronized (this) {
/*  308 */         if (!this.readPrimordialConfiguration)
/*      */         {
/*  312 */           if (System.out == null) {
/*  313 */             return;
/*      */           }
/*  315 */           this.readPrimordialConfiguration = true;
/*      */           try
/*      */           {
/*  318 */             AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*      */               public Void run() throws Exception {
/*  320 */                 LogManager.this.readConfiguration();
/*      */ 
/*  323 */                 PlatformLogger.redirectPlatformLoggers();
/*  324 */                 return null;
/*      */               }
/*      */             });
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     throws SecurityException
/*      */   {
/*  348 */     if (paramPropertyChangeListener == null) {
/*  349 */       throw new NullPointerException();
/*      */     }
/*  351 */     checkPermission();
/*  352 */     this.changes.addPropertyChangeListener(paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     throws SecurityException
/*      */   {
/*  370 */     checkPermission();
/*  371 */     this.changes.removePropertyChangeListener(paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   private LoggerContext getUserContext()
/*      */   {
/*  380 */     LoggerContext localLoggerContext = null;
/*      */ 
/*  382 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  383 */     JavaAWTAccess localJavaAWTAccess = SharedSecrets.getJavaAWTAccess();
/*  384 */     if ((localSecurityManager != null) && (localJavaAWTAccess != null))
/*      */     {
/*  386 */       Object localObject1 = localJavaAWTAccess.getAppletContext();
/*  387 */       if (localObject1 != null) {
/*  388 */         synchronized (localJavaAWTAccess)
/*      */         {
/*  391 */           if (contextsMap == null) {
/*  392 */             contextsMap = new WeakHashMap();
/*      */           }
/*  394 */           localLoggerContext = (LoggerContext)contextsMap.get(localObject1);
/*  395 */           if (localLoggerContext == null)
/*      */           {
/*  400 */             localLoggerContext = new LoggerContext(true, null);
/*  401 */             contextsMap.put(localObject1, localLoggerContext);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  407 */     return localLoggerContext != null ? localLoggerContext : this.userContext;
/*      */   }
/*      */ 
/*      */   private List<LoggerContext> contexts() {
/*  411 */     ArrayList localArrayList = new ArrayList();
/*  412 */     localArrayList.add(this.systemContext);
/*  413 */     localArrayList.add(getUserContext());
/*  414 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   Logger demandLogger(String paramString1, String paramString2, Class<?> paramClass)
/*      */   {
/*  430 */     Logger localLogger1 = getLogger(paramString1);
/*  431 */     if (localLogger1 == null)
/*      */     {
/*  433 */       Logger localLogger2 = new Logger(paramString1, paramString2, paramClass, false);
/*      */       do {
/*  435 */         if (addLogger(localLogger2))
/*      */         {
/*  438 */           return localLogger2;
/*      */         }
/*      */ 
/*  452 */         localLogger1 = getLogger(paramString1);
/*  453 */       }while (localLogger1 == null);
/*      */     }
/*  455 */     return localLogger1;
/*      */   }
/*      */ 
/*      */   Logger demandSystemLogger(String paramString1, String paramString2)
/*      */   {
/*  460 */     final Logger localLogger1 = this.systemContext.demandLogger(paramString1, paramString2);
/*      */     Logger localLogger2;
/*      */     do {
/*  471 */       if (addLogger(localLogger1))
/*      */       {
/*  473 */         localLogger2 = localLogger1;
/*      */       }
/*  475 */       else localLogger2 = getLogger(paramString1);
/*      */     }
/*  477 */     while (localLogger2 == null);
/*      */ 
/*  480 */     if ((localLogger2 != localLogger1) && (localLogger1.accessCheckedHandlers().length == 0))
/*      */     {
/*  482 */       final Logger localLogger3 = localLogger2;
/*  483 */       AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Void run() {
/*  485 */           for (Handler localHandler : localLogger3.accessCheckedHandlers()) {
/*  486 */             localLogger1.addHandler(localHandler);
/*      */           }
/*  488 */           return null;
/*      */         }
/*      */       });
/*      */     }
/*  492 */     return localLogger1;
/*      */   }
/*      */ 
/*      */   private void loadLoggerHandlers(final Logger paramLogger, String paramString1, final String paramString2)
/*      */   {
/*  802 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/*  804 */         String[] arrayOfString = LogManager.this.parseClassNames(paramString2);
/*  805 */         for (int i = 0; i < arrayOfString.length; i++) {
/*  806 */           String str1 = arrayOfString[i];
/*      */           try {
/*  808 */             Class localClass = ClassLoader.getSystemClassLoader().loadClass(str1);
/*  809 */             Handler localHandler = (Handler)localClass.newInstance();
/*      */ 
/*  812 */             String str2 = LogManager.this.getProperty(str1 + ".level");
/*  813 */             if (str2 != null) {
/*  814 */               Level localLevel = Level.findLevel(str2);
/*  815 */               if (localLevel != null) {
/*  816 */                 localHandler.setLevel(localLevel);
/*      */               }
/*      */               else {
/*  819 */                 System.err.println("Can't set level for " + str1);
/*      */               }
/*      */             }
/*      */ 
/*  823 */             paramLogger.addHandler(localHandler);
/*      */           } catch (Exception localException) {
/*  825 */             System.err.println("Can't load log handler \"" + str1 + "\"");
/*  826 */             System.err.println("" + localException);
/*  827 */             localException.printStackTrace();
/*      */           }
/*      */         }
/*  830 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   final void drainLoggerRefQueueBounded()
/*      */   {
/*  967 */     for (int i = 0; (i < 400) && 
/*  968 */       (this.loggerRefQueue != null); i++)
/*      */     {
/*  973 */       LoggerWeakRef localLoggerWeakRef = (LoggerWeakRef)this.loggerRefQueue.poll();
/*  974 */       if (localLoggerWeakRef == null)
/*      */       {
/*      */         break;
/*      */       }
/*  978 */       localLoggerWeakRef.dispose();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean addLogger(Logger paramLogger)
/*      */   {
/*  999 */     String str = paramLogger.getName();
/* 1000 */     if (str == null) {
/* 1001 */       throw new NullPointerException();
/*      */     }
/* 1003 */     drainLoggerRefQueueBounded();
/* 1004 */     LoggerContext localLoggerContext = getUserContext();
/* 1005 */     if (localLoggerContext.addLocalLogger(paramLogger))
/*      */     {
/* 1008 */       loadLoggerHandlers(paramLogger, str, str + ".handlers");
/* 1009 */       return true;
/*      */     }
/* 1011 */     return false;
/*      */   }
/*      */ 
/*      */   private static void doSetLevel(Logger paramLogger, final Level paramLevel)
/*      */   {
/* 1018 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1019 */     if (localSecurityManager == null)
/*      */     {
/* 1021 */       paramLogger.setLevel(paramLevel);
/* 1022 */       return;
/*      */     }
/*      */ 
/* 1026 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/* 1028 */         this.val$logger.setLevel(paramLevel);
/* 1029 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static void doSetParent(Logger paramLogger1, final Logger paramLogger2)
/*      */   {
/* 1036 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1037 */     if (localSecurityManager == null)
/*      */     {
/* 1039 */       paramLogger1.setParent(paramLogger2);
/* 1040 */       return;
/*      */     }
/*      */ 
/* 1044 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/* 1046 */         this.val$logger.setParent(paramLogger2);
/* 1047 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public Logger getLogger(String paramString)
/*      */   {
/* 1067 */     return getUserContext().findLogger(paramString);
/*      */   }
/*      */ 
/*      */   public Enumeration<String> getLoggerNames()
/*      */   {
/* 1087 */     return getUserContext().getLoggerNames();
/*      */   }
/*      */ 
/*      */   public void readConfiguration()
/*      */     throws IOException, SecurityException
/*      */   {
/* 1107 */     checkPermission();
/*      */ 
/* 1110 */     String str1 = System.getProperty("java.util.logging.config.class");
/* 1111 */     if (str1 != null)
/*      */     {
/*      */       try
/*      */       {
/* 1117 */         Class localClass = ClassLoader.getSystemClassLoader().loadClass(str1);
/* 1118 */         localClass.newInstance();
/* 1119 */         return;
/*      */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 1121 */         localObject1 = Thread.currentThread().getContextClassLoader().loadClass(str1);
/* 1122 */         ((Class)localObject1).newInstance();
/* 1123 */         return;
/*      */       }
/*      */       catch (Exception localException) {
/* 1126 */         System.err.println("Logging configuration class \"" + str1 + "\" failed");
/* 1127 */         System.err.println("" + localException);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1132 */     String str2 = System.getProperty("java.util.logging.config.file");
/* 1133 */     if (str2 == null) {
/* 1134 */       str2 = System.getProperty("java.home");
/* 1135 */       if (str2 == null) {
/* 1136 */         throw new Error("Can't find java.home ??");
/*      */       }
/* 1138 */       localObject1 = new File(str2, "lib");
/* 1139 */       localObject1 = new File((File)localObject1, "logging.properties");
/* 1140 */       str2 = ((File)localObject1).getCanonicalPath();
/*      */     }
/* 1142 */     Object localObject1 = new FileInputStream(str2);
/* 1143 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream((InputStream)localObject1);
/*      */     try {
/* 1145 */       readConfiguration(localBufferedInputStream);
/*      */     } finally {
/* 1147 */       if (localObject1 != null)
/* 1148 */         ((InputStream)localObject1).close();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */     throws SecurityException
/*      */   {
/* 1165 */     checkPermission();
/* 1166 */     synchronized (this) {
/* 1167 */       this.props = new Properties();
/*      */ 
/* 1170 */       this.initializedGlobalHandlers = true;
/*      */     }
/* 1172 */     for (??? = contexts().iterator(); ((Iterator)???).hasNext(); ) { LoggerContext localLoggerContext = (LoggerContext)((Iterator)???).next();
/* 1173 */       Enumeration localEnumeration = localLoggerContext.getLoggerNames();
/* 1174 */       while (localEnumeration.hasMoreElements()) {
/* 1175 */         String str = (String)localEnumeration.nextElement();
/* 1176 */         Logger localLogger = localLoggerContext.findLogger(str);
/* 1177 */         if (localLogger != null)
/* 1178 */           resetLogger(localLogger);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void resetLogger(Logger paramLogger)
/*      */   {
/* 1187 */     Handler[] arrayOfHandler = paramLogger.getHandlers();
/* 1188 */     for (int i = 0; i < arrayOfHandler.length; i++) {
/* 1189 */       Handler localHandler = arrayOfHandler[i];
/* 1190 */       paramLogger.removeHandler(localHandler);
/*      */       try {
/* 1192 */         localHandler.close();
/*      */       }
/*      */       catch (Exception localException) {
/*      */       }
/*      */     }
/* 1197 */     String str = paramLogger.getName();
/* 1198 */     if ((str != null) && (str.equals("")))
/*      */     {
/* 1200 */       paramLogger.setLevel(defaultLevel);
/*      */     }
/* 1202 */     else paramLogger.setLevel(null);
/*      */   }
/*      */ 
/*      */   private String[] parseClassNames(String paramString)
/*      */   {
/* 1208 */     String str1 = getProperty(paramString);
/* 1209 */     if (str1 == null) {
/* 1210 */       return new String[0];
/*      */     }
/* 1212 */     str1 = str1.trim();
/* 1213 */     int i = 0;
/* 1214 */     Vector localVector = new Vector();
/* 1215 */     while (i < str1.length()) {
/* 1216 */       int j = i;
/* 1217 */       while ((j < str1.length()) && 
/* 1218 */         (!Character.isWhitespace(str1.charAt(j))))
/*      */       {
/* 1221 */         if (str1.charAt(j) == ',') {
/*      */           break;
/*      */         }
/* 1224 */         j++;
/*      */       }
/* 1226 */       String str2 = str1.substring(i, j);
/* 1227 */       i = j + 1;
/* 1228 */       str2 = str2.trim();
/* 1229 */       if (str2.length() != 0)
/*      */       {
/* 1232 */         localVector.add(str2);
/*      */       }
/*      */     }
/* 1234 */     return (String[])localVector.toArray(new String[localVector.size()]);
/*      */   }
/*      */ 
/*      */   public void readConfiguration(InputStream paramInputStream)
/*      */     throws IOException, SecurityException
/*      */   {
/* 1251 */     checkPermission();
/* 1252 */     reset();
/*      */ 
/* 1255 */     this.props.load(paramInputStream);
/*      */ 
/* 1257 */     String[] arrayOfString = parseClassNames("config");
/*      */ 
/* 1259 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 1260 */       String str = arrayOfString[i];
/*      */       try {
/* 1262 */         Class localClass = ClassLoader.getSystemClassLoader().loadClass(str);
/* 1263 */         localClass.newInstance();
/*      */       } catch (Exception localException) {
/* 1265 */         System.err.println("Can't load config class \"" + str + "\"");
/* 1266 */         System.err.println("" + localException);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1272 */     setLevelsOnExistingLoggers();
/*      */ 
/* 1275 */     this.changes.firePropertyChange(null, null, null);
/*      */ 
/* 1279 */     synchronized (this) {
/* 1280 */       this.initializedGlobalHandlers = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getProperty(String paramString)
/*      */   {
/* 1291 */     return this.props.getProperty(paramString);
/*      */   }
/*      */ 
/*      */   String getStringProperty(String paramString1, String paramString2)
/*      */   {
/* 1298 */     String str = getProperty(paramString1);
/* 1299 */     if (str == null) {
/* 1300 */       return paramString2;
/*      */     }
/* 1302 */     return str.trim();
/*      */   }
/*      */ 
/*      */   int getIntProperty(String paramString, int paramInt)
/*      */   {
/* 1309 */     String str = getProperty(paramString);
/* 1310 */     if (str == null)
/* 1311 */       return paramInt;
/*      */     try
/*      */     {
/* 1314 */       return Integer.parseInt(str.trim()); } catch (Exception localException) {
/*      */     }
/* 1316 */     return paramInt;
/*      */   }
/*      */ 
/*      */   boolean getBooleanProperty(String paramString, boolean paramBoolean)
/*      */   {
/* 1324 */     String str = getProperty(paramString);
/* 1325 */     if (str == null) {
/* 1326 */       return paramBoolean;
/*      */     }
/* 1328 */     str = str.toLowerCase();
/* 1329 */     if ((str.equals("true")) || (str.equals("1")))
/* 1330 */       return true;
/* 1331 */     if ((str.equals("false")) || (str.equals("0"))) {
/* 1332 */       return false;
/*      */     }
/* 1334 */     return paramBoolean;
/*      */   }
/*      */ 
/*      */   Level getLevelProperty(String paramString, Level paramLevel)
/*      */   {
/* 1341 */     String str = getProperty(paramString);
/* 1342 */     if (str == null) {
/* 1343 */       return paramLevel;
/*      */     }
/* 1345 */     Level localLevel = Level.findLevel(str.trim());
/* 1346 */     return localLevel != null ? localLevel : paramLevel;
/*      */   }
/*      */ 
/*      */   Filter getFilterProperty(String paramString, Filter paramFilter)
/*      */   {
/* 1354 */     String str = getProperty(paramString);
/*      */     try {
/* 1356 */       if (str != null) {
/* 1357 */         Class localClass = ClassLoader.getSystemClassLoader().loadClass(str);
/* 1358 */         return (Filter)localClass.newInstance();
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1366 */     return paramFilter;
/*      */   }
/*      */ 
/*      */   Formatter getFormatterProperty(String paramString, Formatter paramFormatter)
/*      */   {
/* 1375 */     String str = getProperty(paramString);
/*      */     try {
/* 1377 */       if (str != null) {
/* 1378 */         Class localClass = ClassLoader.getSystemClassLoader().loadClass(str);
/* 1379 */         return (Formatter)localClass.newInstance();
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/* 1387 */     return paramFormatter;
/*      */   }
/*      */ 
/*      */   private synchronized void initializeGlobalHandlers()
/*      */   {
/* 1394 */     if (this.initializedGlobalHandlers) {
/* 1395 */       return;
/*      */     }
/*      */ 
/* 1398 */     this.initializedGlobalHandlers = true;
/*      */ 
/* 1400 */     if (this.deathImminent)
/*      */     {
/* 1404 */       return;
/*      */     }
/* 1406 */     loadLoggerHandlers(this.rootLogger, null, "handlers");
/*      */   }
/*      */ 
/*      */   void checkPermission()
/*      */   {
/* 1412 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1413 */     if (localSecurityManager != null)
/* 1414 */       localSecurityManager.checkPermission(this.controlPermission);
/*      */   }
/*      */ 
/*      */   public void checkAccess()
/*      */     throws SecurityException
/*      */   {
/* 1428 */     checkPermission();
/*      */   }
/*      */ 
/*      */   private synchronized void setLevelsOnExistingLoggers()
/*      */   {
/* 1502 */     Enumeration localEnumeration = this.props.propertyNames();
/*      */     String str2;
/*      */     Level localLevel;
/* 1503 */     while (localEnumeration.hasMoreElements()) {
/* 1504 */       String str1 = (String)localEnumeration.nextElement();
/* 1505 */       if (str1.endsWith(".level"))
/*      */       {
/* 1509 */         int i = str1.length() - 6;
/* 1510 */         str2 = str1.substring(0, i);
/* 1511 */         localLevel = getLevelProperty(str1, null);
/* 1512 */         if (localLevel == null) {
/* 1513 */           System.err.println("Bad level value for property: " + str1);
/*      */         }
/*      */         else
/* 1516 */           for (LoggerContext localLoggerContext : contexts()) {
/* 1517 */             Logger localLogger = localLoggerContext.findLogger(str2);
/* 1518 */             if (localLogger != null)
/*      */             {
/* 1521 */               localLogger.setLevel(localLevel);
/*      */             }
/*      */           }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static synchronized LoggingMXBean getLoggingMXBean()
/*      */   {
/* 1557 */     if (loggingMXBean == null) {
/* 1558 */       loggingMXBean = new Logging();
/*      */     }
/* 1560 */     return loggingMXBean;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  181 */     AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/*  183 */         String str = null;
/*      */         try {
/*  185 */           str = System.getProperty("java.util.logging.manager");
/*  186 */           if (str != null)
/*      */             try {
/*  188 */               Class localClass1 = ClassLoader.getSystemClassLoader().loadClass(str);
/*  189 */               LogManager.access$102((LogManager)localClass1.newInstance());
/*      */             } catch (ClassNotFoundException localClassNotFoundException) {
/*  191 */               Class localClass2 = Thread.currentThread().getContextClassLoader().loadClass(str);
/*  192 */               LogManager.access$102((LogManager)localClass2.newInstance());
/*      */             }
/*      */         }
/*      */         catch (Exception localException) {
/*  196 */           System.err.println("Could not load Logmanager \"" + str + "\"");
/*  197 */           localException.printStackTrace();
/*      */         }
/*  199 */         if (LogManager.manager == null)
/*  200 */           LogManager.access$102(new LogManager());
/*      */         LogManager tmp122_119 = LogManager.manager; tmp122_119.getClass(); LogManager.manager.rootLogger = new LogManager.RootLogger(tmp122_119, null);
/*      */ 
/*  209 */         LogManager.manager.addLogger(LogManager.manager.rootLogger);
/*  210 */         LogManager.manager.systemContext.addLocalLogger(LogManager.manager.rootLogger, false);
/*  211 */         LogManager.manager.userContext.addLocalLogger(LogManager.manager.rootLogger, false);
/*      */ 
/*  215 */         Logger.global.setLogManager(LogManager.manager);
/*      */ 
/*  218 */         LogManager.manager.addLogger(Logger.global);
/*  219 */         LogManager.manager.systemContext.addLocalLogger(Logger.global, false);
/*  220 */         LogManager.manager.userContext.addLocalLogger(Logger.global, false);
/*      */ 
/*  225 */         return null;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private class Cleaner extends Thread
/*      */   {
/*      */     private Cleaner()
/*      */     {
/*  239 */       setContextClassLoader(null);
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/*  245 */       LogManager localLogManager = LogManager.manager;
/*      */ 
/*  249 */       synchronized (LogManager.this)
/*      */       {
/*  251 */         LogManager.this.deathImminent = true;
/*  252 */         LogManager.this.initializedGlobalHandlers = true;
/*      */       }
/*      */ 
/*  256 */       LogManager.this.reset();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LogNode
/*      */   {
/*      */     HashMap<String, LogNode> children;
/*      */     LogManager.LoggerWeakRef loggerRef;
/*      */     LogNode parent;
/*      */     final LogManager.LoggerContext context;
/*      */ 
/*      */     LogNode(LogNode paramLogNode, LogManager.LoggerContext paramLoggerContext)
/*      */     {
/* 1439 */       this.parent = paramLogNode;
/* 1440 */       this.context = paramLoggerContext;
/*      */     }
/*      */ 
/*      */     void walkAndSetParent(Logger paramLogger)
/*      */     {
/* 1446 */       if (this.children == null) {
/* 1447 */         return;
/*      */       }
/* 1449 */       Iterator localIterator = this.children.values().iterator();
/* 1450 */       while (localIterator.hasNext()) {
/* 1451 */         LogNode localLogNode = (LogNode)localIterator.next();
/* 1452 */         LogManager.LoggerWeakRef localLoggerWeakRef = localLogNode.loggerRef;
/* 1453 */         Logger localLogger = localLoggerWeakRef == null ? null : (Logger)localLoggerWeakRef.get();
/* 1454 */         if (localLogger == null)
/* 1455 */           localLogNode.walkAndSetParent(paramLogger);
/*      */         else
/* 1457 */           LogManager.doSetParent(localLogger, paramLogger);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LoggerContext
/*      */   {
/*  504 */     private final Hashtable<String, LogManager.LoggerWeakRef> namedLoggers = new Hashtable();
/*      */     private final LogManager.LogNode root;
/*      */     private final boolean requiresDefaultLoggers;
/*      */ 
/*      */     private LoggerContext()
/*      */     {
/*  509 */       this(false);
/*      */     }
/*      */     private LoggerContext(boolean paramBoolean) {
/*  512 */       this.root = new LogManager.LogNode(null, this);
/*  513 */       this.requiresDefaultLoggers = paramBoolean;
/*      */     }
/*      */ 
/*      */     Logger demandLogger(String paramString1, String paramString2)
/*      */     {
/*  519 */       return LogManager.manager.demandLogger(paramString1, paramString2, null);
/*      */     }
/*      */ 
/*      */     private void ensureInitialized()
/*      */     {
/*  531 */       if (this.requiresDefaultLoggers)
/*      */       {
/*  533 */         ensureDefaultLogger(LogManager.manager.rootLogger);
/*  534 */         ensureDefaultLogger(Logger.global);
/*      */       }
/*      */     }
/*      */ 
/*      */     synchronized Logger findLogger(String paramString)
/*      */     {
/*  542 */       ensureInitialized();
/*  543 */       LogManager.LoggerWeakRef localLoggerWeakRef = (LogManager.LoggerWeakRef)this.namedLoggers.get(paramString);
/*  544 */       if (localLoggerWeakRef == null) {
/*  545 */         return null;
/*      */       }
/*  547 */       Logger localLogger = (Logger)localLoggerWeakRef.get();
/*  548 */       if (localLogger == null)
/*      */       {
/*  551 */         localLoggerWeakRef.dispose();
/*      */       }
/*  553 */       return localLogger;
/*      */     }
/*      */ 
/*      */     private void ensureAllDefaultLoggers(Logger paramLogger)
/*      */     {
/*  563 */       if (this.requiresDefaultLoggers) {
/*  564 */         String str = paramLogger.getName();
/*  565 */         if (!str.isEmpty()) {
/*  566 */           ensureDefaultLogger(LogManager.manager.rootLogger);
/*      */         }
/*  568 */         if (!"global".equals(str))
/*  569 */           ensureDefaultLogger(Logger.global);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void ensureDefaultLogger(Logger paramLogger)
/*      */     {
/*  581 */       if ((!this.requiresDefaultLoggers) || (paramLogger == null) || ((paramLogger != Logger.global) && (paramLogger != LogManager.manager.rootLogger)))
/*      */       {
/*  589 */         assert (paramLogger == null);
/*      */ 
/*  591 */         return;
/*      */       }
/*      */ 
/*  595 */       if (!this.namedLoggers.containsKey(paramLogger.getName()))
/*      */       {
/*  602 */         addLocalLogger(paramLogger, false);
/*      */       }
/*      */     }
/*      */ 
/*      */     boolean addLocalLogger(Logger paramLogger)
/*      */     {
/*  608 */       return addLocalLogger(paramLogger, this.requiresDefaultLoggers);
/*      */     }
/*      */ 
/*      */     synchronized boolean addLocalLogger(Logger paramLogger, boolean paramBoolean)
/*      */     {
/*  623 */       if (paramBoolean) {
/*  624 */         ensureAllDefaultLoggers(paramLogger);
/*      */       }
/*      */ 
/*  627 */       String str = paramLogger.getName();
/*  628 */       if (str == null) {
/*  629 */         throw new NullPointerException();
/*      */       }
/*      */ 
/*  632 */       LogManager.LoggerWeakRef localLoggerWeakRef1 = (LogManager.LoggerWeakRef)this.namedLoggers.get(str);
/*  633 */       if (localLoggerWeakRef1 != null)
/*  634 */         if (localLoggerWeakRef1.get() == null)
/*      */         {
/*  638 */           localLoggerWeakRef1.dispose();
/*      */         }
/*      */         else
/*  641 */           return false;
/*      */       LogManager tmp69_66 = LogManager.manager; tmp69_66.getClass(); localLoggerWeakRef1 = new LogManager.LoggerWeakRef(tmp69_66, paramLogger);
/*  648 */       this.namedLoggers.put(str, localLoggerWeakRef1);
/*      */ 
/*  651 */       Level localLevel = LogManager.manager.getLevelProperty(str + ".level", null);
/*  652 */       if (localLevel != null) {
/*  653 */         LogManager.doSetLevel(paramLogger, localLevel);
/*      */       }
/*      */ 
/*  656 */       processParentHandlers(paramLogger, str);
/*      */ 
/*  659 */       LogManager.LogNode localLogNode1 = getNode(str);
/*  660 */       localLogNode1.loggerRef = localLoggerWeakRef1;
/*  661 */       Logger localLogger = null;
/*  662 */       LogManager.LogNode localLogNode2 = localLogNode1.parent;
/*  663 */       while (localLogNode2 != null) {
/*  664 */         LogManager.LoggerWeakRef localLoggerWeakRef2 = localLogNode2.loggerRef;
/*  665 */         if (localLoggerWeakRef2 != null) {
/*  666 */           localLogger = (Logger)localLoggerWeakRef2.get();
/*  667 */           if (localLogger != null) {
/*      */             break;
/*      */           }
/*      */         }
/*  671 */         localLogNode2 = localLogNode2.parent;
/*      */       }
/*      */ 
/*  674 */       if (localLogger != null) {
/*  675 */         LogManager.doSetParent(paramLogger, localLogger);
/*      */       }
/*      */ 
/*  678 */       localLogNode1.walkAndSetParent(paramLogger);
/*      */ 
/*  680 */       localLoggerWeakRef1.setNode(localLogNode1);
/*  681 */       return true;
/*      */     }
/*      */ 
/*      */     synchronized void removeLoggerRef(String paramString, LogManager.LoggerWeakRef paramLoggerWeakRef) {
/*  685 */       if (this.namedLoggers.get(paramString) == paramLoggerWeakRef)
/*  686 */         this.namedLoggers.remove(paramString);
/*      */     }
/*      */ 
/*      */     synchronized Enumeration<String> getLoggerNames()
/*      */     {
/*  693 */       ensureInitialized();
/*  694 */       return this.namedLoggers.keys();
/*      */     }
/*      */ 
/*      */     private void processParentHandlers(final Logger paramLogger, final String paramString)
/*      */     {
/*  700 */       AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Void run() {
/*  702 */           if (paramLogger != LogManager.manager.rootLogger) {
/*  703 */             boolean bool = LogManager.manager.getBooleanProperty(paramString + ".useParentHandlers", true);
/*  704 */             if (!bool) {
/*  705 */               paramLogger.setUseParentHandlers(false);
/*      */             }
/*      */           }
/*  708 */           return null;
/*      */         }
/*      */       });
/*  712 */       int i = 1;
/*      */       while (true) {
/*  714 */         int j = paramString.indexOf(".", i);
/*  715 */         if (j < 0) {
/*      */           break;
/*      */         }
/*  718 */         String str = paramString.substring(0, j);
/*  719 */         if ((LogManager.manager.getProperty(str + ".level") != null) || (LogManager.manager.getProperty(str + ".handlers") != null))
/*      */         {
/*  723 */           demandLogger(str, null);
/*      */         }
/*  725 */         i = j + 1;
/*      */       }
/*      */     }
/*      */ 
/*      */     LogManager.LogNode getNode(String paramString)
/*      */     {
/*  732 */       if ((paramString == null) || (paramString.equals(""))) {
/*  733 */         return this.root;
/*      */       }
/*  735 */       Object localObject = this.root;
/*  736 */       while (paramString.length() > 0) {
/*  737 */         int i = paramString.indexOf(".");
/*      */         String str;
/*  739 */         if (i > 0) {
/*  740 */           str = paramString.substring(0, i);
/*  741 */           paramString = paramString.substring(i + 1);
/*      */         } else {
/*  743 */           str = paramString;
/*  744 */           paramString = "";
/*      */         }
/*  746 */         if (((LogManager.LogNode)localObject).children == null) {
/*  747 */           ((LogManager.LogNode)localObject).children = new HashMap();
/*      */         }
/*  749 */         LogManager.LogNode localLogNode = (LogManager.LogNode)((LogManager.LogNode)localObject).children.get(str);
/*  750 */         if (localLogNode == null) {
/*  751 */           localLogNode = new LogManager.LogNode((LogManager.LogNode)localObject, this);
/*  752 */           ((LogManager.LogNode)localObject).children.put(str, localLogNode);
/*      */         }
/*  754 */         localObject = localLogNode;
/*      */       }
/*  756 */       return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   final class LoggerWeakRef extends WeakReference<Logger>
/*      */   {
/*      */     private String name;
/*      */     private LogManager.LogNode node;
/*      */     private WeakReference<Logger> parentRef;
/*  865 */     private boolean disposed = false;
/*      */ 
/*      */     LoggerWeakRef(Logger arg2) {
/*  868 */       super(LogManager.this.loggerRefQueue);
/*      */ 
/*  870 */       this.name = localObject.getName();
/*      */     }
/*      */ 
/*      */     void dispose()
/*      */     {
/*  884 */       synchronized (this)
/*      */       {
/*  891 */         if (this.disposed) return;
/*  892 */         this.disposed = true;
/*      */       }
/*      */ 
/*  895 */       ??? = this.node;
/*  896 */       if (??? != null)
/*      */       {
/*  901 */         synchronized (((LogManager.LogNode)???).context)
/*      */         {
/*  904 */           ((LogManager.LogNode)???).context.removeLoggerRef(this.name, this);
/*  905 */           this.name = null;
/*      */ 
/*  909 */           if (((LogManager.LogNode)???).loggerRef == this) {
/*  910 */             ((LogManager.LogNode)???).loggerRef = null;
/*      */           }
/*  912 */           this.node = null;
/*      */         }
/*      */       }
/*      */ 
/*  916 */       if (this.parentRef != null)
/*      */       {
/*  918 */         ??? = (Logger)this.parentRef.get();
/*  919 */         if (??? != null)
/*      */         {
/*  922 */           ((Logger)???).removeChildLogger(this);
/*      */         }
/*  924 */         this.parentRef = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     void setNode(LogManager.LogNode paramLogNode)
/*      */     {
/*  930 */       this.node = paramLogNode;
/*      */     }
/*      */ 
/*      */     void setParentRef(WeakReference<Logger> paramWeakReference)
/*      */     {
/*  935 */       this.parentRef = paramWeakReference;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class RootLogger extends Logger
/*      */   {
/*      */     private RootLogger()
/*      */     {
/* 1468 */       super(null, null, true);
/* 1469 */       setLevel(LogManager.defaultLevel);
/*      */     }
/*      */ 
/*      */     public void log(LogRecord paramLogRecord)
/*      */     {
/* 1475 */       LogManager.this.initializeGlobalHandlers();
/* 1476 */       super.log(paramLogRecord);
/*      */     }
/*      */ 
/*      */     public void addHandler(Handler paramHandler)
/*      */     {
/* 1481 */       LogManager.this.initializeGlobalHandlers();
/* 1482 */       super.addHandler(paramHandler);
/*      */     }
/*      */ 
/*      */     public void removeHandler(Handler paramHandler)
/*      */     {
/* 1487 */       LogManager.this.initializeGlobalHandlers();
/* 1488 */       super.removeHandler(paramHandler);
/*      */     }
/*      */ 
/*      */     Handler[] accessCheckedHandlers()
/*      */     {
/* 1493 */       LogManager.this.initializeGlobalHandlers();
/* 1494 */       return super.accessCheckedHandlers();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class SystemLoggerContext extends LogManager.LoggerContext
/*      */   {
/*      */     SystemLoggerContext()
/*      */     {
/*  760 */       super();
/*      */     }
/*      */ 
/*      */     Logger demandLogger(String paramString1, String paramString2)
/*      */     {
/*  766 */       Object localObject = findLogger(paramString1);
/*  767 */       if (localObject == null)
/*      */       {
/*  769 */         Logger localLogger = new Logger(paramString1, paramString2, null, true);
/*      */         do
/*  771 */           if (addLocalLogger(localLogger))
/*      */           {
/*  774 */             localObject = localLogger;
/*      */           }
/*      */           else
/*      */           {
/*  787 */             localObject = findLogger(paramString1);
/*      */           }
/*  789 */         while (localObject == null);
/*      */       }
/*  791 */       return localObject;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.LogManager
 * JD-Core Version:    0.6.2
 */
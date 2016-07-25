/*     */ package java.sql;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ 
/*     */ public class DriverManager
/*     */ {
/*  84 */   private static final CopyOnWriteArrayList<DriverInfo> registeredDrivers = new CopyOnWriteArrayList();
/*  85 */   private static volatile int loginTimeout = 0;
/*  86 */   private static volatile PrintWriter logWriter = null;
/*  87 */   private static volatile PrintStream logStream = null;
/*     */ 
/*  89 */   private static final Object logSync = new Object();
/*     */ 
/* 109 */   static final SQLPermission SET_LOG_PERMISSION = new SQLPermission("setLog");
/*     */ 
/*     */   public static PrintWriter getLogWriter()
/*     */   {
/* 125 */     return logWriter;
/*     */   }
/*     */ 
/*     */   public static void setLogWriter(PrintWriter paramPrintWriter)
/*     */   {
/* 160 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 161 */     if (localSecurityManager != null) {
/* 162 */       localSecurityManager.checkPermission(SET_LOG_PERMISSION);
/*     */     }
/* 164 */     logStream = null;
/* 165 */     logWriter = paramPrintWriter;
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static Connection getConnection(String paramString, Properties paramProperties)
/*     */     throws SQLException
/*     */   {
/* 187 */     return getConnection(paramString, paramProperties, Reflection.getCallerClass());
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static Connection getConnection(String paramString1, String paramString2, String paramString3)
/*     */     throws SQLException
/*     */   {
/* 206 */     Properties localProperties = new Properties();
/*     */ 
/* 208 */     if (paramString2 != null) {
/* 209 */       localProperties.put("user", paramString2);
/*     */     }
/* 211 */     if (paramString3 != null) {
/* 212 */       localProperties.put("password", paramString3);
/*     */     }
/*     */ 
/* 215 */     return getConnection(paramString1, localProperties, Reflection.getCallerClass());
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static Connection getConnection(String paramString)
/*     */     throws SQLException
/*     */   {
/* 232 */     Properties localProperties = new Properties();
/* 233 */     return getConnection(paramString, localProperties, Reflection.getCallerClass());
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static Driver getDriver(String paramString)
/*     */     throws SQLException
/*     */   {
/* 251 */     println("DriverManager.getDriver(\"" + paramString + "\")");
/*     */ 
/* 253 */     Class localClass = Reflection.getCallerClass();
/*     */ 
/* 257 */     for (DriverInfo localDriverInfo : registeredDrivers)
/*     */     {
/* 260 */       if (isDriverAllowed(localDriverInfo.driver, localClass))
/*     */         try {
/* 262 */           if (localDriverInfo.driver.acceptsURL(paramString))
/*     */           {
/* 264 */             println("getDriver returning " + localDriverInfo.driver.getClass().getName());
/* 265 */             return localDriverInfo.driver;
/*     */           }
/*     */         }
/*     */         catch (SQLException localSQLException)
/*     */         {
/*     */         }
/*     */       else {
/* 272 */         println("    skipping: " + localDriverInfo.driver.getClass().getName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 277 */     println("getDriver: no suitable driver");
/* 278 */     throw new SQLException("No suitable driver", "08001");
/*     */   }
/*     */ 
/*     */   public static synchronized void registerDriver(Driver paramDriver)
/*     */     throws SQLException
/*     */   {
/* 296 */     if (paramDriver != null) {
/* 297 */       registeredDrivers.addIfAbsent(new DriverInfo(paramDriver));
/*     */     }
/*     */     else {
/* 300 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 303 */     println("registerDriver: " + paramDriver);
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static synchronized void deregisterDriver(Driver paramDriver)
/*     */     throws SQLException
/*     */   {
/* 317 */     if (paramDriver == null) {
/* 318 */       return;
/*     */     }
/*     */ 
/* 321 */     println("DriverManager.deregisterDriver: " + paramDriver);
/*     */ 
/* 323 */     DriverInfo localDriverInfo = new DriverInfo(paramDriver);
/* 324 */     if (registeredDrivers.contains(localDriverInfo)) {
/* 325 */       if (isDriverAllowed(paramDriver, Reflection.getCallerClass())) {
/* 326 */         registeredDrivers.remove(localDriverInfo);
/*     */       }
/*     */       else
/*     */       {
/* 330 */         throw new SecurityException();
/*     */       }
/*     */     }
/* 333 */     else println("    couldn't find driver to unload");
/*     */   }
/*     */ 
/*     */   @CallerSensitive
/*     */   public static Enumeration<Driver> getDrivers()
/*     */   {
/* 348 */     Vector localVector = new Vector();
/*     */ 
/* 350 */     Class localClass = Reflection.getCallerClass();
/*     */ 
/* 353 */     for (DriverInfo localDriverInfo : registeredDrivers)
/*     */     {
/* 356 */       if (isDriverAllowed(localDriverInfo.driver, localClass))
/* 357 */         localVector.addElement(localDriverInfo.driver);
/*     */       else {
/* 359 */         println("    skipping: " + localDriverInfo.getClass().getName());
/*     */       }
/*     */     }
/* 362 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   public static void setLoginTimeout(int paramInt)
/*     */   {
/* 374 */     loginTimeout = paramInt;
/*     */   }
/*     */ 
/*     */   public static int getLoginTimeout()
/*     */   {
/* 385 */     return loginTimeout;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static void setLogStream(PrintStream paramPrintStream)
/*     */   {
/* 409 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 410 */     if (localSecurityManager != null) {
/* 411 */       localSecurityManager.checkPermission(SET_LOG_PERMISSION);
/*     */     }
/*     */ 
/* 414 */     logStream = paramPrintStream;
/* 415 */     if (paramPrintStream != null)
/* 416 */       logWriter = new PrintWriter(paramPrintStream);
/*     */     else
/* 418 */       logWriter = null;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static PrintStream getLogStream()
/*     */   {
/* 430 */     return logStream;
/*     */   }
/*     */ 
/*     */   public static void println(String paramString)
/*     */   {
/* 439 */     synchronized (logSync) {
/* 440 */       if (logWriter != null) {
/* 441 */         logWriter.println(paramString);
/*     */ 
/* 444 */         logWriter.flush();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isDriverAllowed(Driver paramDriver, Class<?> paramClass)
/*     */   {
/* 454 */     ClassLoader localClassLoader = paramClass != null ? paramClass.getClassLoader() : null;
/* 455 */     return isDriverAllowed(paramDriver, localClassLoader);
/*     */   }
/*     */ 
/*     */   private static boolean isDriverAllowed(Driver paramDriver, ClassLoader paramClassLoader) {
/* 459 */     boolean bool = false;
/* 460 */     if (paramDriver != null) {
/* 461 */       Class localClass = null;
/*     */       try {
/* 463 */         localClass = Class.forName(paramDriver.getClass().getName(), true, paramClassLoader);
/*     */       } catch (Exception localException) {
/* 465 */         bool = false;
/*     */       }
/*     */ 
/* 468 */       bool = localClass == paramDriver.getClass();
/*     */     }
/*     */ 
/* 471 */     return bool;
/*     */   }
/*     */ 
/*     */   private static void loadInitialDrivers() {
/*     */     String str1;
/*     */     try {
/* 477 */       str1 = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public String run() {
/* 479 */           return System.getProperty("jdbc.drivers");
/*     */         } } );
/*     */     }
/*     */     catch (Exception localException1) {
/* 483 */       str1 = null;
/*     */     }
/*     */ 
/* 490 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/* 493 */         ServiceLoader localServiceLoader = ServiceLoader.load(Driver.class);
/* 494 */         Iterator localIterator = localServiceLoader.iterator();
/*     */         try
/*     */         {
/* 509 */           while (localIterator.hasNext())
/* 510 */             localIterator.next();
/*     */         }
/*     */         catch (Throwable localThrowable)
/*     */         {
/*     */         }
/* 515 */         return null;
/*     */       }
/*     */     });
/* 519 */     println("DriverManager.initialize: jdbc.drivers = " + str1);
/*     */ 
/* 521 */     if ((str1 == null) || (str1.equals(""))) {
/* 522 */       return;
/*     */     }
/* 524 */     String[] arrayOfString1 = str1.split(":");
/* 525 */     println("number of Drivers:" + arrayOfString1.length);
/* 526 */     for (String str2 : arrayOfString1)
/*     */       try {
/* 528 */         println("DriverManager.Initialize: loading " + str2);
/* 529 */         Class.forName(str2, true, ClassLoader.getSystemClassLoader());
/*     */       }
/*     */       catch (Exception localException2) {
/* 532 */         println("DriverManager.Initialize: load failed: " + localException2);
/*     */       }
/*     */   }
/*     */ 
/*     */   private static Connection getConnection(String paramString, Properties paramProperties, Class<?> paramClass)
/*     */     throws SQLException
/*     */   {
/* 547 */     ClassLoader localClassLoader = paramClass != null ? paramClass.getClassLoader() : null;
/* 548 */     synchronized (DriverManager.class)
/*     */     {
/* 550 */       if (localClassLoader == null) {
/* 551 */         localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */       }
/*     */     }
/*     */ 
/* 555 */     if (paramString == null) {
/* 556 */       throw new SQLException("The url cannot be null", "08001");
/*     */     }
/*     */ 
/* 559 */     println("DriverManager.getConnection(\"" + paramString + "\")");
/*     */ 
/* 563 */     ??? = null;
/*     */ 
/* 565 */     for (DriverInfo localDriverInfo : registeredDrivers)
/*     */     {
/* 568 */       if (isDriverAllowed(localDriverInfo.driver, localClassLoader)) {
/*     */         try {
/* 570 */           println("    trying " + localDriverInfo.driver.getClass().getName());
/* 571 */           Connection localConnection = localDriverInfo.driver.connect(paramString, paramProperties);
/* 572 */           if (localConnection != null)
/*     */           {
/* 574 */             println("getConnection returning " + localDriverInfo.driver.getClass().getName());
/* 575 */             return localConnection;
/*     */           }
/*     */         } catch (SQLException localSQLException) {
/* 578 */           if (??? == null) {
/* 579 */             ??? = localSQLException;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 584 */         println("    skipping: " + localDriverInfo.getClass().getName());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 590 */     if (??? != null) {
/* 591 */       println("getConnection failed: " + ???);
/* 592 */       throw ???;
/*     */     }
/*     */ 
/* 595 */     println("getConnection: no suitable driver found for " + paramString);
/* 596 */     throw new SQLException("No suitable driver found for " + paramString, "08001");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 100 */     loadInitialDrivers();
/* 101 */     println("JDBC DriverManager initialized");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.DriverManager
 * JD-Core Version:    0.6.2
 */
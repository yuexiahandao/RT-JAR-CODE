/*     */ package sun.util.logging;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public class PlatformLogger
/*     */ {
/*     */   public static final int OFF = 2147483647;
/*     */   public static final int SEVERE = 1000;
/*     */   public static final int WARNING = 900;
/*     */   public static final int INFO = 800;
/*     */   public static final int CONFIG = 700;
/*     */   public static final int FINE = 500;
/*     */   public static final int FINER = 400;
/*     */   public static final int FINEST = 300;
/*     */   public static final int ALL = -2147483648;
/* 161 */   private static final Level DEFAULT_LEVEL = Level.INFO;
/*     */ 
/* 164 */   private static boolean loggingEnabled = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Boolean run() {
/* 167 */       String str1 = System.getProperty("java.util.logging.config.class");
/* 168 */       String str2 = System.getProperty("java.util.logging.config.file");
/* 169 */       return Boolean.valueOf((str1 != null) || (str2 != null));
/*     */     }
/*     */   })).booleanValue();
/*     */ 
/* 189 */   private static Map<String, WeakReference<PlatformLogger>> loggers = new HashMap();
/*     */   private volatile LoggerProxy loggerProxy;
/*     */   private volatile JavaLoggerProxy javaLoggerProxy;
/*     */ 
/*     */   public static synchronized PlatformLogger getLogger(String paramString)
/*     */   {
/* 196 */     PlatformLogger localPlatformLogger = null;
/* 197 */     WeakReference localWeakReference = (WeakReference)loggers.get(paramString);
/* 198 */     if (localWeakReference != null) {
/* 199 */       localPlatformLogger = (PlatformLogger)localWeakReference.get();
/*     */     }
/* 201 */     if (localPlatformLogger == null) {
/* 202 */       localPlatformLogger = new PlatformLogger(paramString);
/* 203 */       loggers.put(paramString, new WeakReference(localPlatformLogger));
/*     */     }
/* 205 */     return localPlatformLogger;
/*     */   }
/*     */ 
/*     */   public static synchronized void redirectPlatformLoggers()
/*     */   {
/* 213 */     if ((loggingEnabled) || (!LoggingSupport.isAvailable())) return;
/*     */ 
/* 215 */     loggingEnabled = true;
/* 216 */     for (Map.Entry localEntry : loggers.entrySet()) {
/* 217 */       WeakReference localWeakReference = (WeakReference)localEntry.getValue();
/* 218 */       PlatformLogger localPlatformLogger = (PlatformLogger)localWeakReference.get();
/* 219 */       if (localPlatformLogger != null)
/* 220 */         localPlatformLogger.redirectToJavaLoggerProxy();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void redirectToJavaLoggerProxy()
/*     */   {
/* 229 */     DefaultLoggerProxy localDefaultLoggerProxy = (DefaultLoggerProxy)DefaultLoggerProxy.class.cast(this.loggerProxy);
/* 230 */     JavaLoggerProxy localJavaLoggerProxy = new JavaLoggerProxy(localDefaultLoggerProxy.name, localDefaultLoggerProxy.level);
/*     */ 
/* 232 */     this.javaLoggerProxy = localJavaLoggerProxy;
/* 233 */     this.loggerProxy = localJavaLoggerProxy;
/*     */   }
/*     */ 
/*     */   private PlatformLogger(String paramString)
/*     */   {
/* 242 */     if (loggingEnabled)
/* 243 */       this.loggerProxy = (this.javaLoggerProxy = new JavaLoggerProxy(paramString));
/*     */     else
/* 245 */       this.loggerProxy = new DefaultLoggerProxy(paramString);
/*     */   }
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/* 254 */     return this.loggerProxy.isEnabled();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 261 */     return this.loggerProxy.name;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isLoggable(int paramInt)
/*     */   {
/* 272 */     return isLoggable(Level.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int getLevel()
/*     */   {
/* 283 */     Level localLevel = this.loggerProxy.getLevel();
/* 284 */     return localLevel != null ? localLevel.intValue() : 0;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setLevel(int paramInt)
/*     */   {
/* 294 */     this.loggerProxy.setLevel(paramInt == 0 ? null : Level.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public boolean isLoggable(Level paramLevel)
/*     */   {
/* 302 */     if (paramLevel == null) {
/* 303 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 306 */     JavaLoggerProxy localJavaLoggerProxy = this.javaLoggerProxy;
/* 307 */     return localJavaLoggerProxy != null ? localJavaLoggerProxy.isLoggable(paramLevel) : this.loggerProxy.isLoggable(paramLevel);
/*     */   }
/*     */ 
/*     */   public Level level()
/*     */   {
/* 318 */     return this.loggerProxy.getLevel();
/*     */   }
/*     */ 
/*     */   public void setLevel(Level paramLevel)
/*     */   {
/* 334 */     this.loggerProxy.setLevel(paramLevel);
/*     */   }
/*     */ 
/*     */   public void severe(String paramString)
/*     */   {
/* 341 */     this.loggerProxy.doLog(Level.SEVERE, paramString);
/*     */   }
/*     */ 
/*     */   public void severe(String paramString, Throwable paramThrowable) {
/* 345 */     this.loggerProxy.doLog(Level.SEVERE, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void severe(String paramString, Object[] paramArrayOfObject) {
/* 349 */     this.loggerProxy.doLog(Level.SEVERE, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void warning(String paramString)
/*     */   {
/* 356 */     this.loggerProxy.doLog(Level.WARNING, paramString);
/*     */   }
/*     */ 
/*     */   public void warning(String paramString, Throwable paramThrowable) {
/* 360 */     this.loggerProxy.doLog(Level.WARNING, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void warning(String paramString, Object[] paramArrayOfObject) {
/* 364 */     this.loggerProxy.doLog(Level.WARNING, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void info(String paramString)
/*     */   {
/* 371 */     this.loggerProxy.doLog(Level.INFO, paramString);
/*     */   }
/*     */ 
/*     */   public void info(String paramString, Throwable paramThrowable) {
/* 375 */     this.loggerProxy.doLog(Level.INFO, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void info(String paramString, Object[] paramArrayOfObject) {
/* 379 */     this.loggerProxy.doLog(Level.INFO, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void config(String paramString)
/*     */   {
/* 386 */     this.loggerProxy.doLog(Level.CONFIG, paramString);
/*     */   }
/*     */ 
/*     */   public void config(String paramString, Throwable paramThrowable) {
/* 390 */     this.loggerProxy.doLog(Level.CONFIG, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void config(String paramString, Object[] paramArrayOfObject) {
/* 394 */     this.loggerProxy.doLog(Level.CONFIG, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void fine(String paramString)
/*     */   {
/* 401 */     this.loggerProxy.doLog(Level.FINE, paramString);
/*     */   }
/*     */ 
/*     */   public void fine(String paramString, Throwable paramThrowable) {
/* 405 */     this.loggerProxy.doLog(Level.FINE, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void fine(String paramString, Object[] paramArrayOfObject) {
/* 409 */     this.loggerProxy.doLog(Level.FINE, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void finer(String paramString)
/*     */   {
/* 416 */     this.loggerProxy.doLog(Level.FINER, paramString);
/*     */   }
/*     */ 
/*     */   public void finer(String paramString, Throwable paramThrowable) {
/* 420 */     this.loggerProxy.doLog(Level.FINER, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void finer(String paramString, Object[] paramArrayOfObject) {
/* 424 */     this.loggerProxy.doLog(Level.FINER, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public void finest(String paramString)
/*     */   {
/* 431 */     this.loggerProxy.doLog(Level.FINEST, paramString);
/*     */   }
/*     */ 
/*     */   public void finest(String paramString, Throwable paramThrowable) {
/* 435 */     this.loggerProxy.doLog(Level.FINEST, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void finest(String paramString, Object[] paramArrayOfObject) {
/* 439 */     this.loggerProxy.doLog(Level.FINEST, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 177 */       Class.forName("sun.util.logging.PlatformLogger$DefaultLoggerProxy", false, PlatformLogger.class.getClassLoader());
/*     */ 
/* 180 */       Class.forName("sun.util.logging.PlatformLogger$JavaLoggerProxy", false, PlatformLogger.class.getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 184 */       throw new InternalError(localClassNotFoundException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class DefaultLoggerProxy extends PlatformLogger.LoggerProxy
/*     */   {
/*     */     volatile PlatformLogger.Level effectiveLevel;
/*     */     volatile PlatformLogger.Level level;
/* 552 */     private static final String formatString = LoggingSupport.getSimpleFormat(false);
/*     */ 
/* 556 */     private Date date = new Date();
/*     */ 
/*     */     private static PrintStream outputStream()
/*     */     {
/* 471 */       return System.err;
/*     */     }
/*     */ 
/*     */     DefaultLoggerProxy(String paramString)
/*     */     {
/* 478 */       super();
/* 479 */       this.effectiveLevel = deriveEffectiveLevel(null);
/* 480 */       this.level = null;
/*     */     }
/*     */ 
/*     */     boolean isEnabled() {
/* 484 */       return this.effectiveLevel != PlatformLogger.Level.OFF;
/*     */     }
/*     */ 
/*     */     PlatformLogger.Level getLevel() {
/* 488 */       return this.level;
/*     */     }
/*     */ 
/*     */     void setLevel(PlatformLogger.Level paramLevel) {
/* 492 */       PlatformLogger.Level localLevel = this.level;
/* 493 */       if (localLevel != paramLevel) {
/* 494 */         this.level = paramLevel;
/* 495 */         this.effectiveLevel = deriveEffectiveLevel(paramLevel);
/*     */       }
/*     */     }
/*     */ 
/*     */     void doLog(PlatformLogger.Level paramLevel, String paramString) {
/* 500 */       if (isLoggable(paramLevel))
/* 501 */         outputStream().print(format(paramLevel, paramString, null));
/*     */     }
/*     */ 
/*     */     void doLog(PlatformLogger.Level paramLevel, String paramString, Throwable paramThrowable)
/*     */     {
/* 506 */       if (isLoggable(paramLevel))
/* 507 */         outputStream().print(format(paramLevel, paramString, paramThrowable));
/*     */     }
/*     */ 
/*     */     void doLog(PlatformLogger.Level paramLevel, String paramString, Object[] paramArrayOfObject)
/*     */     {
/* 512 */       if (isLoggable(paramLevel)) {
/* 513 */         String str = formatMessage(paramString, paramArrayOfObject);
/* 514 */         outputStream().print(format(paramLevel, str, null));
/*     */       }
/*     */     }
/*     */ 
/*     */     boolean isLoggable(PlatformLogger.Level paramLevel) {
/* 519 */       PlatformLogger.Level localLevel = this.effectiveLevel;
/* 520 */       return (paramLevel.intValue() >= localLevel.intValue()) && (localLevel != PlatformLogger.Level.OFF);
/*     */     }
/*     */ 
/*     */     private PlatformLogger.Level deriveEffectiveLevel(PlatformLogger.Level paramLevel)
/*     */     {
/* 525 */       return paramLevel == null ? PlatformLogger.DEFAULT_LEVEL : paramLevel;
/*     */     }
/*     */ 
/*     */     private String formatMessage(String paramString, Object[] paramArrayOfObject)
/*     */     {
/*     */       try
/*     */       {
/* 532 */         if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0))
/*     */         {
/* 534 */           return paramString;
/*     */         }
/*     */ 
/* 541 */         if ((paramString.indexOf("{0") >= 0) || (paramString.indexOf("{1") >= 0) || (paramString.indexOf("{2") >= 0) || (paramString.indexOf("{3") >= 0))
/*     */         {
/* 543 */           return MessageFormat.format(paramString, paramArrayOfObject);
/*     */         }
/* 545 */         return paramString;
/*     */       } catch (Exception localException) {
/*     */       }
/* 548 */       return paramString;
/*     */     }
/*     */ 
/*     */     private synchronized String format(PlatformLogger.Level paramLevel, String paramString, Throwable paramThrowable)
/*     */     {
/* 558 */       this.date.setTime(System.currentTimeMillis());
/* 559 */       String str = "";
/* 560 */       if (paramThrowable != null) {
/* 561 */         StringWriter localStringWriter = new StringWriter();
/* 562 */         PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
/* 563 */         localPrintWriter.println();
/* 564 */         paramThrowable.printStackTrace(localPrintWriter);
/* 565 */         localPrintWriter.close();
/* 566 */         str = localStringWriter.toString();
/*     */       }
/*     */ 
/* 569 */       return String.format(formatString, new Object[] { this.date, getCallerInfo(), this.name, paramLevel.name(), paramString, str });
/*     */     }
/*     */ 
/*     */     private String getCallerInfo()
/*     */     {
/* 581 */       Object localObject = null;
/* 582 */       String str1 = null;
/*     */ 
/* 584 */       JavaLangAccess localJavaLangAccess = SharedSecrets.getJavaLangAccess();
/* 585 */       Throwable localThrowable = new Throwable();
/* 586 */       int i = localJavaLangAccess.getStackTraceDepth(localThrowable);
/*     */ 
/* 588 */       String str2 = "sun.util.logging.PlatformLogger";
/* 589 */       int j = 1;
/* 590 */       for (int k = 0; k < i; k++)
/*     */       {
/* 593 */         StackTraceElement localStackTraceElement = localJavaLangAccess.getStackTraceElement(localThrowable, k);
/*     */ 
/* 595 */         String str3 = localStackTraceElement.getClassName();
/* 596 */         if (j != 0)
/*     */         {
/* 598 */           if (str3.equals(str2)) {
/* 599 */             j = 0;
/*     */           }
/*     */         }
/* 602 */         else if (!str3.equals(str2))
/*     */         {
/* 604 */           localObject = str3;
/* 605 */           str1 = localStackTraceElement.getMethodName();
/* 606 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 611 */       if (localObject != null) {
/* 612 */         return localObject + " " + str1;
/*     */       }
/* 614 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class JavaLoggerProxy extends PlatformLogger.LoggerProxy
/*     */   {
/*     */     private final Object javaLogger;
/*     */ 
/*     */     JavaLoggerProxy(String paramString)
/*     */     {
/* 634 */       this(paramString, null);
/*     */     }
/*     */ 
/*     */     JavaLoggerProxy(String paramString, PlatformLogger.Level paramLevel) {
/* 638 */       super();
/* 639 */       this.javaLogger = LoggingSupport.getLogger(paramString);
/* 640 */       if (paramLevel != null)
/*     */       {
/* 642 */         LoggingSupport.setLevel(this.javaLogger, paramLevel.javaLevel);
/*     */       }
/*     */     }
/*     */ 
/*     */     void doLog(PlatformLogger.Level paramLevel, String paramString) {
/* 647 */       LoggingSupport.log(this.javaLogger, paramLevel.javaLevel, paramString);
/*     */     }
/*     */ 
/*     */     void doLog(PlatformLogger.Level paramLevel, String paramString, Throwable paramThrowable) {
/* 651 */       LoggingSupport.log(this.javaLogger, paramLevel.javaLevel, paramString, paramThrowable);
/*     */     }
/*     */ 
/*     */     void doLog(PlatformLogger.Level paramLevel, String paramString, Object[] paramArrayOfObject) {
/* 655 */       if (!isLoggable(paramLevel)) {
/* 656 */         return;
/*     */       }
/*     */ 
/* 660 */       int i = paramArrayOfObject != null ? paramArrayOfObject.length : 0;
/* 661 */       String[] arrayOfString = new String[i];
/* 662 */       for (int j = 0; j < i; j++) {
/* 663 */         arrayOfString[j] = String.valueOf(paramArrayOfObject[j]);
/*     */       }
/* 665 */       LoggingSupport.log(this.javaLogger, paramLevel.javaLevel, paramString, arrayOfString);
/*     */     }
/*     */ 
/*     */     boolean isEnabled() {
/* 669 */       return LoggingSupport.isLoggable(this.javaLogger, PlatformLogger.Level.OFF.javaLevel);
/*     */     }
/*     */ 
/*     */     PlatformLogger.Level getLevel()
/*     */     {
/* 678 */       Object localObject = LoggingSupport.getLevel(this.javaLogger);
/* 679 */       if (localObject == null) return null;
/*     */       try
/*     */       {
/* 682 */         return PlatformLogger.Level.valueOf(LoggingSupport.getLevelName(localObject)); } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */       }
/* 684 */       return PlatformLogger.Level.valueOf(LoggingSupport.getLevelValue(localObject));
/*     */     }
/*     */ 
/*     */     void setLevel(PlatformLogger.Level paramLevel)
/*     */     {
/* 689 */       LoggingSupport.setLevel(this.javaLogger, paramLevel == null ? null : paramLevel.javaLevel);
/*     */     }
/*     */ 
/*     */     boolean isLoggable(PlatformLogger.Level paramLevel) {
/* 693 */       return LoggingSupport.isLoggable(this.javaLogger, paramLevel.javaLevel);
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/* 626 */       for (PlatformLogger.Level localLevel : PlatformLogger.Level.values())
/* 627 */         localLevel.javaLevel = LoggingSupport.parseLevel(localLevel.name());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum Level
/*     */   {
/* 111 */     ALL, 
/* 112 */     FINEST, 
/* 113 */     FINER, 
/* 114 */     FINE, 
/* 115 */     CONFIG, 
/* 116 */     INFO, 
/* 117 */     WARNING, 
/* 118 */     SEVERE, 
/* 119 */     OFF;
/*     */ 
/*     */     Object javaLevel;
/* 130 */     private static final int[] levelValues = { -2147483648, 300, 400, 500, 700, 800, 900, 1000, 2147483647 };
/*     */ 
/*     */     public int intValue()
/*     */     {
/* 137 */       return levelValues[ordinal()];
/*     */     }
/*     */ 
/*     */     static Level valueOf(int paramInt) {
/* 141 */       switch (paramInt)
/*     */       {
/*     */       case 300:
/* 144 */         return FINEST;
/*     */       case 500:
/* 145 */         return FINE;
/*     */       case 400:
/* 146 */         return FINER;
/*     */       case 800:
/* 147 */         return INFO;
/*     */       case 900:
/* 148 */         return WARNING;
/*     */       case 700:
/* 149 */         return CONFIG;
/*     */       case 1000:
/* 150 */         return SEVERE;
/*     */       case 2147483647:
/* 151 */         return OFF;
/*     */       case -2147483648:
/* 152 */         return ALL;
/*     */       }
/*     */ 
/* 156 */       int i = Arrays.binarySearch(levelValues, 0, levelValues.length - 2, paramInt);
/* 157 */       return values()[(-i - 1)];
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class LoggerProxy
/*     */   {
/*     */     final String name;
/*     */ 
/*     */     protected LoggerProxy(String paramString)
/*     */     {
/* 449 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     abstract boolean isEnabled();
/*     */ 
/*     */     abstract PlatformLogger.Level getLevel();
/*     */ 
/*     */     abstract void setLevel(PlatformLogger.Level paramLevel);
/*     */ 
/*     */     abstract void doLog(PlatformLogger.Level paramLevel, String paramString);
/*     */ 
/*     */     abstract void doLog(PlatformLogger.Level paramLevel, String paramString, Throwable paramThrowable);
/*     */ 
/*     */     abstract void doLog(PlatformLogger.Level paramLevel, String paramString, Object[] paramArrayOfObject);
/*     */ 
/*     */     abstract boolean isLoggable(PlatformLogger.Level paramLevel);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.logging.PlatformLogger
 * JD-Core Version:    0.6.2
 */
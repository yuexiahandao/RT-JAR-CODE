/*     */ package sun.management.snmp.util;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class MibLogger
/*     */ {
/*     */   final Logger logger;
/*     */   final String className;
/*     */ 
/*     */   static String getClassName(Class paramClass)
/*     */   {
/*  36 */     if (paramClass == null) return null;
/*  37 */     if (paramClass.isArray())
/*  38 */       return getClassName(paramClass.getComponentType()) + "[]";
/*  39 */     String str = paramClass.getName();
/*  40 */     int i = str.lastIndexOf('.');
/*  41 */     int j = str.length();
/*  42 */     if ((i < 0) || (i >= j))
/*  43 */       return str;
/*  44 */     return str.substring(i + 1, j);
/*     */   }
/*     */ 
/*     */   static String getLoggerName(Class paramClass) {
/*  48 */     if (paramClass == null) return "sun.management.snmp.jvminstr";
/*  49 */     Package localPackage = paramClass.getPackage();
/*  50 */     if (localPackage == null) return "sun.management.snmp.jvminstr";
/*  51 */     String str = localPackage.getName();
/*  52 */     if (str == null) return "sun.management.snmp.jvminstr";
/*  53 */     return str;
/*     */   }
/*     */ 
/*     */   public MibLogger(Class paramClass) {
/*  57 */     this(getLoggerName(paramClass), getClassName(paramClass));
/*     */   }
/*     */ 
/*     */   public MibLogger(Class paramClass, String paramString) {
/*  61 */     this(getLoggerName(paramClass) + (paramString == null ? "" : new StringBuilder().append(".").append(paramString).toString()), getClassName(paramClass));
/*     */   }
/*     */ 
/*     */   public MibLogger(String paramString)
/*     */   {
/*  66 */     this("sun.management.snmp.jvminstr", paramString);
/*     */   }
/*     */ 
/*     */   public MibLogger(String paramString1, String paramString2) {
/*  70 */     Logger localLogger = null;
/*     */     try {
/*  72 */       localLogger = Logger.getLogger(paramString1);
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*  76 */     this.logger = localLogger;
/*  77 */     this.className = paramString2;
/*     */   }
/*     */ 
/*     */   protected Logger getLogger() {
/*  81 */     return this.logger;
/*     */   }
/*     */ 
/*     */   public boolean isTraceOn() {
/*  85 */     Logger localLogger = getLogger();
/*  86 */     if (localLogger == null) return false;
/*  87 */     return localLogger.isLoggable(Level.FINE);
/*     */   }
/*     */ 
/*     */   public boolean isDebugOn() {
/*  91 */     Logger localLogger = getLogger();
/*  92 */     if (localLogger == null) return false;
/*  93 */     return localLogger.isLoggable(Level.FINEST);
/*     */   }
/*     */ 
/*     */   public boolean isInfoOn() {
/*  97 */     Logger localLogger = getLogger();
/*  98 */     if (localLogger == null) return false;
/*  99 */     return localLogger.isLoggable(Level.INFO);
/*     */   }
/*     */ 
/*     */   public boolean isConfigOn() {
/* 103 */     Logger localLogger = getLogger();
/* 104 */     if (localLogger == null) return false;
/* 105 */     return localLogger.isLoggable(Level.CONFIG);
/*     */   }
/*     */ 
/*     */   public void config(String paramString1, String paramString2) {
/* 109 */     Logger localLogger = getLogger();
/* 110 */     if (localLogger != null) localLogger.logp(Level.CONFIG, this.className, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void config(String paramString, Throwable paramThrowable)
/*     */   {
/* 115 */     Logger localLogger = getLogger();
/* 116 */     if (localLogger != null) localLogger.logp(Level.CONFIG, this.className, paramString, paramThrowable.toString(), paramThrowable);
/*     */   }
/*     */ 
/*     */   public void config(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 121 */     Logger localLogger = getLogger();
/* 122 */     if (localLogger != null) localLogger.logp(Level.CONFIG, this.className, paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void error(String paramString1, String paramString2)
/*     */   {
/* 127 */     Logger localLogger = getLogger();
/* 128 */     if (localLogger != null) localLogger.logp(Level.SEVERE, this.className, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void info(String paramString1, String paramString2)
/*     */   {
/* 133 */     Logger localLogger = getLogger();
/* 134 */     if (localLogger != null) localLogger.logp(Level.INFO, this.className, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void info(String paramString, Throwable paramThrowable)
/*     */   {
/* 139 */     Logger localLogger = getLogger();
/* 140 */     if (localLogger != null) localLogger.logp(Level.INFO, this.className, paramString, paramThrowable.toString(), paramThrowable);
/*     */   }
/*     */ 
/*     */   public void info(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 145 */     Logger localLogger = getLogger();
/* 146 */     if (localLogger != null) localLogger.logp(Level.INFO, this.className, paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void warning(String paramString1, String paramString2)
/*     */   {
/* 151 */     Logger localLogger = getLogger();
/* 152 */     if (localLogger != null) localLogger.logp(Level.WARNING, this.className, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void warning(String paramString, Throwable paramThrowable)
/*     */   {
/* 157 */     Logger localLogger = getLogger();
/* 158 */     if (localLogger != null) localLogger.logp(Level.WARNING, this.className, paramString, paramThrowable.toString(), paramThrowable);
/*     */   }
/*     */ 
/*     */   public void warning(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 163 */     Logger localLogger = getLogger();
/* 164 */     if (localLogger != null) localLogger.logp(Level.WARNING, this.className, paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void trace(String paramString1, String paramString2)
/*     */   {
/* 169 */     Logger localLogger = getLogger();
/* 170 */     if (localLogger != null) localLogger.logp(Level.FINE, this.className, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void trace(String paramString, Throwable paramThrowable)
/*     */   {
/* 175 */     Logger localLogger = getLogger();
/* 176 */     if (localLogger != null) localLogger.logp(Level.FINE, this.className, paramString, paramThrowable.toString(), paramThrowable);
/*     */   }
/*     */ 
/*     */   public void trace(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 181 */     Logger localLogger = getLogger();
/* 182 */     if (localLogger != null) localLogger.logp(Level.FINE, this.className, paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void debug(String paramString1, String paramString2)
/*     */   {
/* 187 */     Logger localLogger = getLogger();
/* 188 */     if (localLogger != null) localLogger.logp(Level.FINEST, this.className, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void debug(String paramString, Throwable paramThrowable)
/*     */   {
/* 193 */     Logger localLogger = getLogger();
/* 194 */     if (localLogger != null) localLogger.logp(Level.FINEST, this.className, paramString, paramThrowable.toString(), paramThrowable);
/*     */   }
/*     */ 
/*     */   public void debug(String paramString1, String paramString2, Throwable paramThrowable)
/*     */   {
/* 199 */     Logger localLogger = getLogger();
/* 200 */     if (localLogger != null) localLogger.logp(Level.FINEST, this.className, paramString1, paramString2, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.util.MibLogger
 * JD-Core Version:    0.6.2
 */
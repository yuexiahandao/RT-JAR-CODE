/*     */ package sun.util.logging;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ public class LoggingSupport
/*     */ {
/*  48 */   private static final LoggingProxy proxy = (LoggingProxy)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public LoggingProxy run()
/*     */     {
/*     */       try
/*     */       {
/*  54 */         Class localClass = Class.forName("java.util.logging.LoggingProxyImpl", true, null);
/*  55 */         Field localField = localClass.getDeclaredField("INSTANCE");
/*  56 */         localField.setAccessible(true);
/*  57 */         return (LoggingProxy)localField.get(null);
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/*  59 */         return null;
/*     */       } catch (NoSuchFieldException localNoSuchFieldException) {
/*  61 */         throw new AssertionError(localNoSuchFieldException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/*  63 */         throw new AssertionError(localIllegalAccessException);
/*     */       }
/*     */     } } );
/*     */   private static final String DEFAULT_FORMAT = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s%n%4$s: %5$s%6$s%n";
/*     */   private static final String FORMAT_PROP_KEY = "java.util.logging.SimpleFormatter.format";
/*     */ 
/*  71 */   public static boolean isAvailable() { return proxy != null; }
/*     */ 
/*     */   private static void ensureAvailable()
/*     */   {
/*  75 */     if (proxy == null)
/*  76 */       throw new AssertionError("Should not here");
/*     */   }
/*     */ 
/*     */   public static List<String> getLoggerNames() {
/*  80 */     ensureAvailable();
/*  81 */     return proxy.getLoggerNames();
/*     */   }
/*     */   public static String getLoggerLevel(String paramString) {
/*  84 */     ensureAvailable();
/*  85 */     return proxy.getLoggerLevel(paramString);
/*     */   }
/*     */ 
/*     */   public static void setLoggerLevel(String paramString1, String paramString2) {
/*  89 */     ensureAvailable();
/*  90 */     proxy.setLoggerLevel(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public static String getParentLoggerName(String paramString) {
/*  94 */     ensureAvailable();
/*  95 */     return proxy.getParentLoggerName(paramString);
/*     */   }
/*     */ 
/*     */   public static Object getLogger(String paramString) {
/*  99 */     ensureAvailable();
/* 100 */     return proxy.getLogger(paramString);
/*     */   }
/*     */ 
/*     */   public static Object getLevel(Object paramObject) {
/* 104 */     ensureAvailable();
/* 105 */     return proxy.getLevel(paramObject);
/*     */   }
/*     */ 
/*     */   public static void setLevel(Object paramObject1, Object paramObject2) {
/* 109 */     ensureAvailable();
/* 110 */     proxy.setLevel(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public static boolean isLoggable(Object paramObject1, Object paramObject2) {
/* 114 */     ensureAvailable();
/* 115 */     return proxy.isLoggable(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public static void log(Object paramObject1, Object paramObject2, String paramString) {
/* 119 */     ensureAvailable();
/* 120 */     proxy.log(paramObject1, paramObject2, paramString);
/*     */   }
/*     */ 
/*     */   public static void log(Object paramObject1, Object paramObject2, String paramString, Throwable paramThrowable) {
/* 124 */     ensureAvailable();
/* 125 */     proxy.log(paramObject1, paramObject2, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public static void log(Object paramObject1, Object paramObject2, String paramString, Object[] paramArrayOfObject) {
/* 129 */     ensureAvailable();
/* 130 */     proxy.log(paramObject1, paramObject2, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public static Object parseLevel(String paramString) {
/* 134 */     ensureAvailable();
/* 135 */     return proxy.parseLevel(paramString);
/*     */   }
/*     */ 
/*     */   public static String getLevelName(Object paramObject) {
/* 139 */     ensureAvailable();
/* 140 */     return proxy.getLevelName(paramObject);
/*     */   }
/*     */ 
/*     */   public static int getLevelValue(Object paramObject) {
/* 144 */     ensureAvailable();
/* 145 */     return proxy.getLevelValue(paramObject);
/*     */   }
/*     */ 
/*     */   public static String getSimpleFormat()
/*     */   {
/* 153 */     return getSimpleFormat(true);
/*     */   }
/*     */ 
/*     */   static String getSimpleFormat(boolean paramBoolean)
/*     */   {
/* 159 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public String run()
/*     */       {
/* 163 */         return System.getProperty("java.util.logging.SimpleFormatter.format");
/*     */       }
/*     */     });
/* 167 */     if ((paramBoolean) && (proxy != null) && (str == null)) {
/* 168 */       str = proxy.getProperty("java.util.logging.SimpleFormatter.format");
/*     */     }
/*     */ 
/* 171 */     if (str != null)
/*     */       try
/*     */       {
/* 174 */         String.format(str, new Object[] { new Date(), "", "", "", "", "" });
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {
/* 177 */         str = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s%n%4$s: %5$s%6$s%n";
/*     */       }
/*     */     else {
/* 180 */       str = "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s%n%4$s: %5$s%6$s%n";
/*     */     }
/* 182 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.logging.LoggingSupport
 * JD-Core Version:    0.6.2
 */
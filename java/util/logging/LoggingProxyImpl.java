/*     */ package java.util.logging;
/*     */ 
/*     */ import java.util.List;
/*     */ import sun.util.logging.LoggingProxy;
/*     */ 
/*     */ class LoggingProxyImpl
/*     */   implements LoggingProxy
/*     */ {
/*  34 */   static final LoggingProxy INSTANCE = new LoggingProxyImpl();
/*     */ 
/*     */   public Object getLogger(String paramString)
/*     */   {
/*  41 */     return Logger.getPlatformLogger(paramString);
/*     */   }
/*     */ 
/*     */   public Object getLevel(Object paramObject)
/*     */   {
/*  46 */     return ((Logger)paramObject).getLevel();
/*     */   }
/*     */ 
/*     */   public void setLevel(Object paramObject1, Object paramObject2)
/*     */   {
/*  51 */     ((Logger)paramObject1).setLevel((Level)paramObject2);
/*     */   }
/*     */ 
/*     */   public boolean isLoggable(Object paramObject1, Object paramObject2)
/*     */   {
/*  56 */     return ((Logger)paramObject1).isLoggable((Level)paramObject2);
/*     */   }
/*     */ 
/*     */   public void log(Object paramObject1, Object paramObject2, String paramString)
/*     */   {
/*  61 */     ((Logger)paramObject1).log((Level)paramObject2, paramString);
/*     */   }
/*     */ 
/*     */   public void log(Object paramObject1, Object paramObject2, String paramString, Throwable paramThrowable)
/*     */   {
/*  66 */     ((Logger)paramObject1).log((Level)paramObject2, paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   public void log(Object paramObject1, Object paramObject2, String paramString, Object[] paramArrayOfObject)
/*     */   {
/*  71 */     ((Logger)paramObject1).log((Level)paramObject2, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public List<String> getLoggerNames()
/*     */   {
/*  76 */     return LogManager.getLoggingMXBean().getLoggerNames();
/*     */   }
/*     */ 
/*     */   public String getLoggerLevel(String paramString)
/*     */   {
/*  81 */     return LogManager.getLoggingMXBean().getLoggerLevel(paramString);
/*     */   }
/*     */ 
/*     */   public void setLoggerLevel(String paramString1, String paramString2)
/*     */   {
/*  86 */     LogManager.getLoggingMXBean().setLoggerLevel(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public String getParentLoggerName(String paramString)
/*     */   {
/*  91 */     return LogManager.getLoggingMXBean().getParentLoggerName(paramString);
/*     */   }
/*     */ 
/*     */   public Object parseLevel(String paramString)
/*     */   {
/*  96 */     Level localLevel = Level.findLevel(paramString);
/*  97 */     if (localLevel == null) {
/*  98 */       throw new IllegalArgumentException("Unknown level \"" + paramString + "\"");
/*     */     }
/* 100 */     return localLevel;
/*     */   }
/*     */ 
/*     */   public String getLevelName(Object paramObject)
/*     */   {
/* 105 */     return ((Level)paramObject).getLevelName();
/*     */   }
/*     */ 
/*     */   public int getLevelValue(Object paramObject)
/*     */   {
/* 110 */     return ((Level)paramObject).intValue();
/*     */   }
/*     */ 
/*     */   public String getProperty(String paramString)
/*     */   {
/* 115 */     return LogManager.getLogManager().getProperty(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.LoggingProxyImpl
 * JD-Core Version:    0.6.2
 */
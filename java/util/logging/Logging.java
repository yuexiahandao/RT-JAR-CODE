/*     */ package java.util.logging;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ 
/*     */ class Logging
/*     */   implements LoggingMXBean
/*     */ {
/*  49 */   private static LogManager logManager = LogManager.getLogManager();
/*     */ 
/*  67 */   private static String EMPTY_STRING = "";
/*     */ 
/*     */   public List<String> getLoggerNames()
/*     */   {
/*  58 */     Enumeration localEnumeration = logManager.getLoggerNames();
/*  59 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/*  61 */     while (localEnumeration.hasMoreElements()) {
/*  62 */       localArrayList.add((String)localEnumeration.nextElement());
/*     */     }
/*  64 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public String getLoggerLevel(String paramString)
/*     */   {
/*  69 */     Logger localLogger = logManager.getLogger(paramString);
/*  70 */     if (localLogger == null) {
/*  71 */       return null;
/*     */     }
/*     */ 
/*  74 */     Level localLevel = localLogger.getLevel();
/*  75 */     if (localLevel == null) {
/*  76 */       return EMPTY_STRING;
/*     */     }
/*  78 */     return localLevel.getLevelName();
/*     */   }
/*     */ 
/*     */   public void setLoggerLevel(String paramString1, String paramString2)
/*     */   {
/*  83 */     if (paramString1 == null) {
/*  84 */       throw new NullPointerException("loggerName is null");
/*     */     }
/*     */ 
/*  87 */     Logger localLogger = logManager.getLogger(paramString1);
/*  88 */     if (localLogger == null) {
/*  89 */       throw new IllegalArgumentException("Logger " + paramString1 + "does not exist");
/*     */     }
/*     */ 
/*  93 */     Level localLevel = null;
/*  94 */     if (paramString2 != null)
/*     */     {
/*  96 */       localLevel = Level.findLevel(paramString2);
/*  97 */       if (localLevel == null) {
/*  98 */         throw new IllegalArgumentException("Unknown level \"" + paramString2 + "\"");
/*     */       }
/*     */     }
/*     */ 
/* 102 */     localLogger.setLevel(localLevel);
/*     */   }
/*     */ 
/*     */   public String getParentLoggerName(String paramString) {
/* 106 */     Logger localLogger1 = logManager.getLogger(paramString);
/* 107 */     if (localLogger1 == null) {
/* 108 */       return null;
/*     */     }
/*     */ 
/* 111 */     Logger localLogger2 = localLogger1.getParent();
/* 112 */     if (localLogger2 == null)
/*     */     {
/* 114 */       return EMPTY_STRING;
/*     */     }
/* 116 */     return localLogger2.getName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.Logging
 * JD-Core Version:    0.6.2
 */
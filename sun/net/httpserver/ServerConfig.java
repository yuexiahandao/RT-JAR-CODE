/*     */ package sun.net.httpserver;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class ServerConfig
/*     */ {
/*     */   static int clockTick;
/*     */   static final int DEFAULT_CLOCK_TICK = 10000;
/*     */   static final long DEFAULT_IDLE_INTERVAL = 30L;
/*     */   static final int DEFAULT_MAX_IDLE_CONNECTIONS = 200;
/*     */   static final long DEFAULT_MAX_REQ_TIME = -1L;
/*     */   static final long DEFAULT_MAX_RSP_TIME = -1L;
/*     */   static final long DEFAULT_TIMER_MILLIS = 1000L;
/*     */   static final int DEFAULT_MAX_REQ_HEADERS = 200;
/*     */   static final long DEFAULT_DRAIN_AMOUNT = 65536L;
/*     */   static long idleInterval;
/*     */   static long drainAmount;
/*     */   static int maxIdleConnections;
/*     */   private static int maxReqHeaders;
/*     */   static long maxReqTime;
/*     */   static long maxRspTime;
/*     */   static long timerMillis;
/*     */   static boolean debug;
/*     */   static boolean noDelay;
/*     */ 
/*     */   static void checkLegacyProperties(Logger paramLogger)
/*     */   {
/* 113 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/* 116 */         if (System.getProperty("sun.net.httpserver.readTimeout") != null)
/*     */         {
/* 119 */           this.val$logger.warning("sun.net.httpserver.readTimeout property is no longer used. Use sun.net.httpserver.maxReqTime instead.");
/*     */         }
/*     */ 
/* 124 */         if (System.getProperty("sun.net.httpserver.writeTimeout") != null)
/*     */         {
/* 127 */           this.val$logger.warning("sun.net.httpserver.writeTimeout property is no longer used. Use sun.net.httpserver.maxRspTime instead.");
/*     */         }
/*     */ 
/* 132 */         if (System.getProperty("sun.net.httpserver.selCacheTimeout") != null)
/*     */         {
/* 135 */           this.val$logger.warning("sun.net.httpserver.selCacheTimeout property is no longer used.");
/*     */         }
/*     */ 
/* 139 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static boolean debugEnabled()
/*     */   {
/* 146 */     return debug;
/*     */   }
/*     */ 
/*     */   static long getIdleInterval() {
/* 150 */     return idleInterval;
/*     */   }
/*     */ 
/*     */   static int getClockTick() {
/* 154 */     return clockTick;
/*     */   }
/*     */ 
/*     */   static int getMaxIdleConnections() {
/* 158 */     return maxIdleConnections;
/*     */   }
/*     */ 
/*     */   static long getDrainAmount() {
/* 162 */     return drainAmount;
/*     */   }
/*     */ 
/*     */   static int getMaxReqHeaders() {
/* 166 */     return maxReqHeaders;
/*     */   }
/*     */ 
/*     */   static long getMaxReqTime() {
/* 170 */     return maxReqTime;
/*     */   }
/*     */ 
/*     */   static long getMaxRspTime() {
/* 174 */     return maxRspTime;
/*     */   }
/*     */ 
/*     */   static long getTimerMillis() {
/* 178 */     return timerMillis;
/*     */   }
/*     */ 
/*     */   static boolean noDelay() {
/* 182 */     return noDelay;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  67 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run()
/*     */       {
/*  71 */         ServerConfig.idleInterval = Long.getLong("sun.net.httpserver.idleInterval", 30L).longValue() * 1000L;
/*     */ 
/*  74 */         ServerConfig.clockTick = Integer.getInteger("sun.net.httpserver.clockTick", 10000).intValue();
/*     */ 
/*  77 */         ServerConfig.maxIdleConnections = Integer.getInteger("sun.net.httpserver.maxIdleConnections", 200).intValue();
/*     */ 
/*  81 */         ServerConfig.drainAmount = Long.getLong("sun.net.httpserver.drainAmount", 65536L).longValue();
/*     */ 
/*  84 */         ServerConfig.access$002(Integer.getInteger("sun.net.httpserver.maxReqHeaders", 200).intValue());
/*     */ 
/*  88 */         ServerConfig.maxReqTime = Long.getLong("sun.net.httpserver.maxReqTime", -1L).longValue();
/*     */ 
/*  91 */         ServerConfig.maxRspTime = Long.getLong("sun.net.httpserver.maxRspTime", -1L).longValue();
/*     */ 
/*  94 */         ServerConfig.timerMillis = Long.getLong("sun.net.httpserver.timerMillis", 1000L).longValue();
/*     */ 
/*  97 */         ServerConfig.debug = Boolean.getBoolean("sun.net.httpserver.debug");
/*     */ 
/*  99 */         ServerConfig.noDelay = Boolean.getBoolean("sun.net.httpserver.nodelay");
/*     */ 
/* 101 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.httpserver.ServerConfig
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Writer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Vector;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class PerformanceLogger
/*     */ {
/*     */   private static final int START_INDEX = 0;
/*     */   private static final int LAST_RESERVED = 0;
/*  79 */   private static boolean perfLoggingOn = false;
/*  80 */   private static boolean useNanoTime = false;
/*     */   private static Vector<TimeData> times;
/*  82 */   private static String logFileName = null;
/*  83 */   private static Writer logWriter = null;
/*     */   private static long baseTime;
/*     */ 
/*     */   public static boolean loggingEnabled()
/*     */   {
/* 142 */     return perfLoggingOn;
/*     */   }
/*     */ 
/*     */   private static long getCurrentTime()
/*     */   {
/* 171 */     if (useNanoTime) {
/* 172 */       return System.nanoTime();
/*     */     }
/* 174 */     return System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public static void setStartTime(String paramString)
/*     */   {
/* 185 */     if (loggingEnabled()) {
/* 186 */       long l = getCurrentTime();
/* 187 */       setStartTime(paramString, l);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void setBaseTime(long paramLong)
/*     */   {
/* 196 */     if (loggingEnabled())
/* 197 */       baseTime = paramLong;
/*     */   }
/*     */ 
/*     */   public static void setStartTime(String paramString, long paramLong)
/*     */   {
/* 209 */     if (loggingEnabled())
/* 210 */       times.set(0, new TimeData(paramString, paramLong));
/*     */   }
/*     */ 
/*     */   public static long getStartTime()
/*     */   {
/* 220 */     if (loggingEnabled()) {
/* 221 */       return ((TimeData)times.get(0)).getTime();
/*     */     }
/* 223 */     return 0L;
/*     */   }
/*     */ 
/*     */   public static int setTime(String paramString)
/*     */   {
/* 232 */     if (loggingEnabled()) {
/* 233 */       long l = getCurrentTime();
/* 234 */       return setTime(paramString, l);
/*     */     }
/* 236 */     return 0;
/*     */   }
/*     */ 
/*     */   public static int setTime(String paramString, long paramLong)
/*     */   {
/* 249 */     if (loggingEnabled())
/*     */     {
/* 253 */       synchronized (times) {
/* 254 */         times.add(new TimeData(paramString, paramLong));
/* 255 */         return times.size() - 1;
/*     */       }
/*     */     }
/* 258 */     return 0;
/*     */   }
/*     */ 
/*     */   public static long getTimeAtIndex(int paramInt)
/*     */   {
/* 266 */     if (loggingEnabled()) {
/* 267 */       return ((TimeData)times.get(paramInt)).getTime();
/*     */     }
/* 269 */     return 0L;
/*     */   }
/*     */ 
/*     */   public static String getMessageAtIndex(int paramInt)
/*     */   {
/* 277 */     if (loggingEnabled()) {
/* 278 */       return ((TimeData)times.get(paramInt)).getMessage();
/*     */     }
/* 280 */     return null;
/*     */   }
/*     */ 
/*     */   public static void outputLog(Writer paramWriter)
/*     */   {
/* 288 */     if (loggingEnabled())
/*     */       try {
/* 290 */         synchronized (times) {
/* 291 */           for (int i = 0; i < times.size(); i++) {
/* 292 */             TimeData localTimeData = (TimeData)times.get(i);
/* 293 */             if (localTimeData != null) {
/* 294 */               paramWriter.write(i + " " + localTimeData.getMessage() + ": " + (localTimeData.getTime() - baseTime) + "\n");
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 300 */         paramWriter.flush();
/*     */       } catch (Exception localException) {
/* 302 */         System.out.println(localException + ": Writing performance log to " + paramWriter);
/*     */       }
/*     */   }
/*     */ 
/*     */   public static void outputLog()
/*     */   {
/* 313 */     outputLog(logWriter);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  87 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.perflog"));
/*     */ 
/*  90 */     if (str1 != null) {
/*  91 */       perfLoggingOn = true;
/*     */ 
/*  94 */       String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.perflog.nano"));
/*     */ 
/*  97 */       if (str2 != null) {
/*  98 */         useNanoTime = true;
/*     */       }
/*     */ 
/* 102 */       if (str1.regionMatches(true, 0, "file:", 0, 5)) {
/* 103 */         logFileName = str1.substring(5);
/*     */       }
/* 105 */       if ((logFileName != null) && 
/* 106 */         (logWriter == null)) {
/* 107 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run() {
/*     */             try {
/* 111 */               File localFile = new File(PerformanceLogger.logFileName);
/* 112 */               localFile.createNewFile();
/* 113 */               PerformanceLogger.access$102(new FileWriter(localFile));
/*     */             } catch (Exception localException) {
/* 115 */               System.out.println(localException + ": Creating logfile " + PerformanceLogger.logFileName + ".  Log to console");
/*     */             }
/*     */ 
/* 119 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */ 
/* 124 */       if (logWriter == null) {
/* 125 */         logWriter = new OutputStreamWriter(System.out);
/*     */       }
/*     */     }
/* 128 */     times = new Vector(10);
/*     */ 
/* 130 */     for (int i = 0; i <= 0; i++)
/* 131 */       times.add(new TimeData("Time " + i + " not set", 0L));
/*     */   }
/*     */ 
/*     */   static class TimeData
/*     */   {
/*     */     String message;
/*     */     long time;
/*     */ 
/*     */     TimeData(String paramString, long paramLong)
/*     */     {
/* 154 */       this.message = paramString;
/* 155 */       this.time = paramLong;
/*     */     }
/*     */ 
/*     */     String getMessage() {
/* 159 */       return this.message;
/*     */     }
/*     */ 
/*     */     long getTime() {
/* 163 */       return this.time;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.PerformanceLogger
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.rmi.runtime;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.rmi.server.LogStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.logging.Handler;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.logging.SimpleFormatter;
/*     */ import java.util.logging.StreamHandler;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class Log
/*     */ {
/*  67 */   public static final Level BRIEF = Level.FINE;
/*  68 */   public static final Level VERBOSE = Level.FINER;
/*     */ 
/*  79 */   private static final LogFactory logFactory = bool ? new LogStreamLogFactory() : new LoggerLogFactory();
/*     */ 
/*     */   public abstract boolean isLoggable(Level paramLevel);
/*     */ 
/*     */   public abstract void log(Level paramLevel, String paramString);
/*     */ 
/*     */   public abstract void log(Level paramLevel, String paramString, Throwable paramThrowable);
/*     */ 
/*     */   public abstract void setOutputStream(OutputStream paramOutputStream);
/*     */ 
/*     */   public abstract PrintStream getPrintStream();
/*     */ 
/*     */   public static Log getLog(String paramString1, String paramString2, int paramInt)
/*     */   {
/*     */     Level localLevel;
/* 124 */     if (paramInt < 0)
/* 125 */       localLevel = null;
/* 126 */     else if (paramInt == 0)
/* 127 */       localLevel = Level.OFF;
/* 128 */     else if ((paramInt > 0) && (paramInt <= 10))
/*     */     {
/* 130 */       localLevel = BRIEF;
/* 131 */     } else if ((paramInt > 10) && (paramInt <= 20))
/*     */     {
/* 134 */       localLevel = VERBOSE;
/*     */     }
/* 136 */     else localLevel = Level.FINEST;
/*     */ 
/* 138 */     return logFactory.createLog(paramString1, paramString2, localLevel);
/*     */   }
/*     */ 
/*     */   public static Log getLog(String paramString1, String paramString2, boolean paramBoolean)
/*     */   {
/* 151 */     Level localLevel = paramBoolean ? VERBOSE : null;
/* 152 */     return logFactory.createLog(paramString1, paramString2, localLevel);
/*     */   }
/*     */ 
/*     */   private static String[] getSource()
/*     */   {
/* 449 */     StackTraceElement[] arrayOfStackTraceElement = new Exception().getStackTrace();
/* 450 */     return new String[] { arrayOfStackTraceElement[3].getClassName(), arrayOfStackTraceElement[3].getMethodName() };
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  73 */     boolean bool = Boolean.valueOf((String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.log.useOld"))).booleanValue();
/*     */   }
/*     */ 
/*     */   private static class InternalStreamHandler extends StreamHandler
/*     */   {
/*     */     InternalStreamHandler(OutputStream paramOutputStream)
/*     */     {
/* 279 */       super(new SimpleFormatter());
/*     */     }
/*     */ 
/*     */     public void publish(LogRecord paramLogRecord) {
/* 283 */       super.publish(paramLogRecord);
/* 284 */       flush();
/*     */     }
/*     */ 
/*     */     public void close() {
/* 288 */       flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract interface LogFactory
/*     */   {
/*     */     public abstract Log createLog(String paramString1, String paramString2, Level paramLevel);
/*     */   }
/*     */ 
/*     */   private static class LogStreamLog extends Log
/*     */   {
/*     */     private final LogStream stream;
/* 377 */     private int levelValue = Level.OFF.intValue();
/*     */ 
/*     */     private LogStreamLog(LogStream paramLogStream, Level paramLevel) {
/* 380 */       if ((paramLogStream != null) && (paramLevel != null))
/*     */       {
/* 384 */         this.levelValue = paramLevel.intValue();
/*     */       }
/* 386 */       this.stream = paramLogStream;
/*     */     }
/*     */ 
/*     */     public synchronized boolean isLoggable(Level paramLevel) {
/* 390 */       return paramLevel.intValue() >= this.levelValue;
/*     */     }
/*     */ 
/*     */     public void log(Level paramLevel, String paramString) {
/* 394 */       if (isLoggable(paramLevel)) {
/* 395 */         String[] arrayOfString = Log.access$200();
/* 396 */         this.stream.println(unqualifiedName(arrayOfString[0]) + "." + arrayOfString[1] + ": " + paramString);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void log(Level paramLevel, String paramString, Throwable paramThrowable)
/*     */     {
/* 402 */       if (isLoggable(paramLevel))
/*     */       {
/* 407 */         synchronized (this.stream) {
/* 408 */           String[] arrayOfString = Log.access$200();
/* 409 */           this.stream.println(unqualifiedName(arrayOfString[0]) + "." + arrayOfString[1] + ": " + paramString);
/*     */ 
/* 411 */           paramThrowable.printStackTrace(this.stream);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public PrintStream getPrintStream() {
/* 417 */       return this.stream;
/*     */     }
/*     */ 
/*     */     public synchronized void setOutputStream(OutputStream paramOutputStream) {
/* 421 */       if (paramOutputStream != null) {
/* 422 */         if (VERBOSE.intValue() < this.levelValue) {
/* 423 */           this.levelValue = VERBOSE.intValue();
/*     */         }
/* 425 */         this.stream.setOutputStream(paramOutputStream);
/*     */       }
/*     */       else {
/* 428 */         this.levelValue = Level.OFF.intValue();
/*     */       }
/*     */     }
/*     */ 
/*     */     private static String unqualifiedName(String paramString)
/*     */     {
/* 436 */       int i = paramString.lastIndexOf(".");
/* 437 */       if (i >= 0) {
/* 438 */         paramString = paramString.substring(i + 1);
/*     */       }
/* 440 */       paramString = paramString.replace('$', '.');
/* 441 */       return paramString;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LogStreamLogFactory
/*     */     implements Log.LogFactory
/*     */   {
/*     */     public Log createLog(String paramString1, String paramString2, Level paramLevel)
/*     */     {
/* 360 */       LogStream localLogStream = null;
/* 361 */       if (paramString2 != null) {
/* 362 */         localLogStream = LogStream.log(paramString2);
/*     */       }
/* 364 */       return new Log.LogStreamLog(localLogStream, paramLevel, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LoggerLog extends Log
/*     */   {
/* 182 */     private static final Handler alternateConsole = (Handler)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Handler run()
/*     */       {
/* 186 */         Log.InternalStreamHandler localInternalStreamHandler = new Log.InternalStreamHandler(System.err);
/*     */ 
/* 188 */         localInternalStreamHandler.setLevel(Level.ALL);
/* 189 */         return localInternalStreamHandler;
/*     */       }
/*     */     });
/*     */ 
/* 194 */     private Log.InternalStreamHandler copyHandler = null;
/*     */     private final Logger logger;
/*     */     private Log.LoggerPrintStream loggerSandwich;
/*     */ 
/*     */     private LoggerLog(final Logger paramLogger, final Level paramLevel)
/*     */     {
/* 204 */       this.logger = paramLogger;
/*     */ 
/* 206 */       if (paramLevel != null)
/* 207 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run() {
/* 210 */             if (!paramLogger.isLoggable(paramLevel)) {
/* 211 */               paramLogger.setLevel(paramLevel);
/*     */             }
/* 213 */             paramLogger.addHandler(Log.LoggerLog.alternateConsole);
/* 214 */             return null;
/*     */           }
/*     */         });
/*     */     }
/*     */ 
/*     */     public boolean isLoggable(Level paramLevel)
/*     */     {
/* 222 */       return this.logger.isLoggable(paramLevel);
/*     */     }
/*     */ 
/*     */     public void log(Level paramLevel, String paramString) {
/* 226 */       if (isLoggable(paramLevel)) {
/* 227 */         String[] arrayOfString = Log.access$200();
/* 228 */         this.logger.logp(paramLevel, arrayOfString[0], arrayOfString[1], Thread.currentThread().getName() + ": " + paramString);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void log(Level paramLevel, String paramString, Throwable paramThrowable)
/*     */     {
/* 234 */       if (isLoggable(paramLevel)) {
/* 235 */         String[] arrayOfString = Log.access$200();
/* 236 */         this.logger.logp(paramLevel, arrayOfString[0], arrayOfString[1], Thread.currentThread().getName() + ": " + paramString, paramThrowable);
/*     */       }
/*     */     }
/*     */ 
/*     */     public synchronized void setOutputStream(OutputStream paramOutputStream)
/*     */     {
/* 249 */       if (paramOutputStream != null) {
/* 250 */         if (!this.logger.isLoggable(VERBOSE)) {
/* 251 */           this.logger.setLevel(VERBOSE);
/*     */         }
/* 253 */         this.copyHandler = new Log.InternalStreamHandler(paramOutputStream);
/* 254 */         this.copyHandler.setLevel(Log.VERBOSE);
/* 255 */         this.logger.addHandler(this.copyHandler);
/*     */       }
/*     */       else {
/* 258 */         if (this.copyHandler != null) {
/* 259 */           this.logger.removeHandler(this.copyHandler);
/*     */         }
/* 261 */         this.copyHandler = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     public synchronized PrintStream getPrintStream() {
/* 266 */       if (this.loggerSandwich == null) {
/* 267 */         this.loggerSandwich = new Log.LoggerPrintStream(this.logger, null);
/*     */       }
/* 269 */       return this.loggerSandwich;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LoggerLogFactory
/*     */     implements Log.LogFactory
/*     */   {
/*     */     public Log createLog(String paramString1, String paramString2, Level paramLevel)
/*     */     {
/* 171 */       Logger localLogger = Logger.getLogger(paramString1);
/* 172 */       return new Log.LoggerLog(localLogger, paramLevel, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class LoggerPrintStream extends PrintStream
/*     */   {
/*     */     private final Logger logger;
/* 303 */     private int last = -1;
/*     */     private final ByteArrayOutputStream bufOut;
/*     */ 
/*     */     private LoggerPrintStream(Logger paramLogger)
/*     */     {
/* 310 */       super();
/* 311 */       this.bufOut = ((ByteArrayOutputStream)this.out);
/* 312 */       this.logger = paramLogger;
/*     */     }
/*     */ 
/*     */     public void write(int paramInt) {
/* 316 */       if ((this.last == 13) && (paramInt == 10)) {
/* 317 */         this.last = -1;
/* 318 */         return;
/* 319 */       }if ((paramInt == 10) || (paramInt == 13))
/*     */         try
/*     */         {
/* 322 */           String str = Thread.currentThread().getName() + ": " + this.bufOut.toString();
/*     */ 
/* 325 */           this.logger.logp(Level.INFO, "LogStream", "print", str);
/*     */         } finally {
/* 327 */           this.bufOut.reset();
/*     */         }
/*     */       else {
/* 330 */         super.write(paramInt);
/*     */       }
/* 332 */       this.last = paramInt;
/*     */     }
/*     */ 
/*     */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 336 */       if (paramInt2 < 0) {
/* 337 */         throw new ArrayIndexOutOfBoundsException(paramInt2);
/*     */       }
/* 339 */       for (int i = 0; i < paramInt2; i++)
/* 340 */         write(paramArrayOfByte[(paramInt1 + i)]);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 345 */       return "RMI";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.runtime.Log
 * JD-Core Version:    0.6.2
 */
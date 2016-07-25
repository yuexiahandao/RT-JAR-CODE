/*     */ package java.util.logging;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ 
/*     */ public abstract class Handler
/*     */ {
/*  49 */   private static final int offValue = Level.OFF.intValue();
/*  50 */   private LogManager manager = LogManager.getLogManager();
/*     */   private Filter filter;
/*     */   private Formatter formatter;
/*  53 */   private Level logLevel = Level.ALL;
/*  54 */   private ErrorManager errorManager = new ErrorManager();
/*     */   private String encoding;
/*  59 */   boolean sealed = true;
/*     */ 
/*     */   public abstract void publish(LogRecord paramLogRecord);
/*     */ 
/*     */   public abstract void flush();
/*     */ 
/*     */   public abstract void close()
/*     */     throws SecurityException;
/*     */ 
/*     */   public void setFormatter(Formatter paramFormatter)
/*     */     throws SecurityException
/*     */   {
/* 114 */     checkPermission();
/*     */ 
/* 116 */     paramFormatter.getClass();
/* 117 */     this.formatter = paramFormatter;
/*     */   }
/*     */ 
/*     */   public Formatter getFormatter()
/*     */   {
/* 125 */     return this.formatter;
/*     */   }
/*     */ 
/*     */   public void setEncoding(String paramString)
/*     */     throws SecurityException, UnsupportedEncodingException
/*     */   {
/* 143 */     checkPermission();
/* 144 */     if (paramString != null) {
/*     */       try {
/* 146 */         if (!Charset.isSupported(paramString))
/* 147 */           throw new UnsupportedEncodingException(paramString);
/*     */       }
/*     */       catch (IllegalCharsetNameException localIllegalCharsetNameException) {
/* 150 */         throw new UnsupportedEncodingException(paramString);
/*     */       }
/*     */     }
/* 153 */     this.encoding = paramString;
/*     */   }
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/* 163 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   public void setFilter(Filter paramFilter)
/*     */     throws SecurityException
/*     */   {
/* 178 */     checkPermission();
/* 179 */     this.filter = paramFilter;
/*     */   }
/*     */ 
/*     */   public Filter getFilter()
/*     */   {
/* 188 */     return this.filter;
/*     */   }
/*     */ 
/*     */   public void setErrorManager(ErrorManager paramErrorManager)
/*     */   {
/* 202 */     checkPermission();
/* 203 */     if (paramErrorManager == null) {
/* 204 */       throw new NullPointerException();
/*     */     }
/* 206 */     this.errorManager = paramErrorManager;
/*     */   }
/*     */ 
/*     */   public ErrorManager getErrorManager()
/*     */   {
/* 216 */     checkPermission();
/* 217 */     return this.errorManager;
/*     */   }
/*     */ 
/*     */   protected void reportError(String paramString, Exception paramException, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 232 */       this.errorManager.error(paramString, paramException, paramInt);
/*     */     } catch (Exception localException) {
/* 234 */       System.err.println("Handler.reportError caught:");
/* 235 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void setLevel(Level paramLevel)
/*     */     throws SecurityException
/*     */   {
/* 253 */     if (paramLevel == null) {
/* 254 */       throw new NullPointerException();
/*     */     }
/* 256 */     checkPermission();
/* 257 */     this.logLevel = paramLevel;
/*     */   }
/*     */ 
/*     */   public synchronized Level getLevel()
/*     */   {
/* 267 */     return this.logLevel;
/*     */   }
/*     */ 
/*     */   public boolean isLoggable(LogRecord paramLogRecord)
/*     */   {
/* 284 */     int i = getLevel().intValue();
/* 285 */     if ((paramLogRecord.getLevel().intValue() < i) || (i == offValue)) {
/* 286 */       return false;
/*     */     }
/* 288 */     Filter localFilter = getFilter();
/* 289 */     if (localFilter == null) {
/* 290 */       return true;
/*     */     }
/* 292 */     return localFilter.isLoggable(paramLogRecord);
/*     */   }
/*     */ 
/*     */   void checkPermission()
/*     */     throws SecurityException
/*     */   {
/* 300 */     if (this.sealed)
/* 301 */       this.manager.checkPermission();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.Handler
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.istack.internal.logging;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
/*     */ 
/*     */ public class Logger
/*     */ {
/*     */   private static final String WS_LOGGING_SUBSYSTEM_NAME_ROOT = "com.sun.metro";
/*     */   private static final String ROOT_WS_PACKAGE = "com.sun.xml.internal.ws.";
/*  48 */   private static final Level METHOD_CALL_LEVEL_VALUE = Level.FINEST;
/*     */   private final String componentClassName;
/*     */   private final java.util.logging.Logger logger;
/*     */ 
/*     */   protected Logger(String systemLoggerName, String componentName)
/*     */   {
/*  57 */     this.componentClassName = ("[" + componentName + "] ");
/*  58 */     this.logger = java.util.logging.Logger.getLogger(systemLoggerName);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static Logger getLogger(@NotNull Class<?> componentClass)
/*     */   {
/*  77 */     return new Logger(getSystemLoggerName(componentClass), componentClass.getName());
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static Logger getLogger(@NotNull String customLoggerName, @NotNull Class<?> componentClass)
/*     */   {
/*  97 */     return new Logger(customLoggerName, componentClass.getName());
/*     */   }
/*     */ 
/*     */   static final String getSystemLoggerName(@NotNull Class<?> componentClass)
/*     */   {
/* 106 */     StringBuilder sb = new StringBuilder(componentClass.getPackage().getName());
/* 107 */     int lastIndexOfWsPackage = sb.lastIndexOf("com.sun.xml.internal.ws.");
/* 108 */     if (lastIndexOfWsPackage > -1) {
/* 109 */       sb.replace(0, lastIndexOfWsPackage + "com.sun.xml.internal.ws.".length(), "");
/*     */ 
/* 111 */       StringTokenizer st = new StringTokenizer(sb.toString(), ".");
/* 112 */       sb = new StringBuilder("com.sun.metro").append(".");
/* 113 */       if (st.hasMoreTokens()) {
/* 114 */         String token = st.nextToken();
/* 115 */         if ("api".equals(token)) {
/* 116 */           token = st.nextToken();
/*     */         }
/* 118 */         sb.append(token);
/*     */       }
/*     */     }
/*     */ 
/* 122 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public void log(Level level, String message) {
/* 126 */     if (!this.logger.isLoggable(level)) {
/* 127 */       return;
/*     */     }
/* 129 */     this.logger.logp(level, this.componentClassName, getCallerMethodName(), message);
/*     */   }
/*     */ 
/*     */   public void log(Level level, String message, Throwable thrown) {
/* 133 */     if (!this.logger.isLoggable(level)) {
/* 134 */       return;
/*     */     }
/* 136 */     this.logger.logp(level, this.componentClassName, getCallerMethodName(), message, thrown);
/*     */   }
/*     */ 
/*     */   public void finest(String message) {
/* 140 */     if (!this.logger.isLoggable(Level.FINEST)) {
/* 141 */       return;
/*     */     }
/* 143 */     this.logger.logp(Level.FINEST, this.componentClassName, getCallerMethodName(), message);
/*     */   }
/*     */ 
/*     */   public void finest(String message, Throwable thrown) {
/* 147 */     if (!this.logger.isLoggable(Level.FINEST)) {
/* 148 */       return;
/*     */     }
/* 150 */     this.logger.logp(Level.FINEST, this.componentClassName, getCallerMethodName(), message, thrown);
/*     */   }
/*     */ 
/*     */   public void finer(String message) {
/* 154 */     if (!this.logger.isLoggable(Level.FINER)) {
/* 155 */       return;
/*     */     }
/* 157 */     this.logger.logp(Level.FINER, this.componentClassName, getCallerMethodName(), message);
/*     */   }
/*     */ 
/*     */   public void finer(String message, Throwable thrown) {
/* 161 */     if (!this.logger.isLoggable(Level.FINER)) {
/* 162 */       return;
/*     */     }
/* 164 */     this.logger.logp(Level.FINER, this.componentClassName, getCallerMethodName(), message, thrown);
/*     */   }
/*     */ 
/*     */   public void fine(String message) {
/* 168 */     if (!this.logger.isLoggable(Level.FINE)) {
/* 169 */       return;
/*     */     }
/* 171 */     this.logger.logp(Level.FINE, this.componentClassName, getCallerMethodName(), message);
/*     */   }
/*     */ 
/*     */   public void fine(String message, Throwable thrown) {
/* 175 */     if (!this.logger.isLoggable(Level.FINE)) {
/* 176 */       return;
/*     */     }
/* 178 */     this.logger.logp(Level.FINE, this.componentClassName, getCallerMethodName(), message, thrown);
/*     */   }
/*     */ 
/*     */   public void info(String message) {
/* 182 */     if (!this.logger.isLoggable(Level.INFO)) {
/* 183 */       return;
/*     */     }
/* 185 */     this.logger.logp(Level.INFO, this.componentClassName, getCallerMethodName(), message);
/*     */   }
/*     */ 
/*     */   public void info(String message, Throwable thrown) {
/* 189 */     if (!this.logger.isLoggable(Level.INFO)) {
/* 190 */       return;
/*     */     }
/* 192 */     this.logger.logp(Level.INFO, this.componentClassName, getCallerMethodName(), message, thrown);
/*     */   }
/*     */ 
/*     */   public void config(String message) {
/* 196 */     if (!this.logger.isLoggable(Level.CONFIG)) {
/* 197 */       return;
/*     */     }
/* 199 */     this.logger.logp(Level.CONFIG, this.componentClassName, getCallerMethodName(), message);
/*     */   }
/*     */ 
/*     */   public void config(String message, Throwable thrown) {
/* 203 */     if (!this.logger.isLoggable(Level.CONFIG)) {
/* 204 */       return;
/*     */     }
/* 206 */     this.logger.logp(Level.CONFIG, this.componentClassName, getCallerMethodName(), message, thrown);
/*     */   }
/*     */ 
/*     */   public void warning(String message) {
/* 210 */     if (!this.logger.isLoggable(Level.WARNING)) {
/* 211 */       return;
/*     */     }
/* 213 */     this.logger.logp(Level.WARNING, this.componentClassName, getCallerMethodName(), message);
/*     */   }
/*     */ 
/*     */   public void warning(String message, Throwable thrown) {
/* 217 */     if (!this.logger.isLoggable(Level.WARNING)) {
/* 218 */       return;
/*     */     }
/* 220 */     this.logger.logp(Level.WARNING, this.componentClassName, getCallerMethodName(), message, thrown);
/*     */   }
/*     */ 
/*     */   public void severe(String message) {
/* 224 */     if (!this.logger.isLoggable(Level.SEVERE)) {
/* 225 */       return;
/*     */     }
/* 227 */     this.logger.logp(Level.SEVERE, this.componentClassName, getCallerMethodName(), message);
/*     */   }
/*     */ 
/*     */   public void severe(String message, Throwable thrown) {
/* 231 */     if (!this.logger.isLoggable(Level.SEVERE)) {
/* 232 */       return;
/*     */     }
/* 234 */     this.logger.logp(Level.SEVERE, this.componentClassName, getCallerMethodName(), message, thrown);
/*     */   }
/*     */ 
/*     */   public boolean isMethodCallLoggable() {
/* 238 */     return this.logger.isLoggable(METHOD_CALL_LEVEL_VALUE);
/*     */   }
/*     */ 
/*     */   public boolean isLoggable(Level level) {
/* 242 */     return this.logger.isLoggable(level);
/*     */   }
/*     */ 
/*     */   public void setLevel(Level level) {
/* 246 */     this.logger.setLevel(level);
/*     */   }
/*     */ 
/*     */   public void entering() {
/* 250 */     if (!this.logger.isLoggable(METHOD_CALL_LEVEL_VALUE)) {
/* 251 */       return;
/*     */     }
/*     */ 
/* 254 */     this.logger.entering(this.componentClassName, getCallerMethodName());
/*     */   }
/*     */ 
/*     */   public void entering(Object[] parameters) {
/* 258 */     if (!this.logger.isLoggable(METHOD_CALL_LEVEL_VALUE)) {
/* 259 */       return;
/*     */     }
/*     */ 
/* 262 */     this.logger.entering(this.componentClassName, getCallerMethodName(), parameters);
/*     */   }
/*     */ 
/*     */   public void exiting() {
/* 266 */     if (!this.logger.isLoggable(METHOD_CALL_LEVEL_VALUE)) {
/* 267 */       return;
/*     */     }
/* 269 */     this.logger.exiting(this.componentClassName, getCallerMethodName());
/*     */   }
/*     */ 
/*     */   public void exiting(Object result) {
/* 273 */     if (!this.logger.isLoggable(METHOD_CALL_LEVEL_VALUE)) {
/* 274 */       return;
/*     */     }
/* 276 */     this.logger.exiting(this.componentClassName, getCallerMethodName(), result);
/*     */   }
/*     */ 
/*     */   public <T extends Throwable> T logSevereException(T exception, Throwable cause)
/*     */   {
/* 295 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 296 */       if (cause == null) {
/* 297 */         this.logger.logp(Level.SEVERE, this.componentClassName, getCallerMethodName(), exception.getMessage());
/*     */       } else {
/* 299 */         exception.initCause(cause);
/* 300 */         this.logger.logp(Level.SEVERE, this.componentClassName, getCallerMethodName(), exception.getMessage(), cause);
/*     */       }
/*     */     }
/*     */ 
/* 304 */     return exception;
/*     */   }
/*     */ 
/*     */   public <T extends Throwable> T logSevereException(T exception, boolean logCause)
/*     */   {
/* 326 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 327 */       if ((logCause) && (exception.getCause() != null))
/* 328 */         this.logger.logp(Level.SEVERE, this.componentClassName, getCallerMethodName(), exception.getMessage(), exception.getCause());
/*     */       else {
/* 330 */         this.logger.logp(Level.SEVERE, this.componentClassName, getCallerMethodName(), exception.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 334 */     return exception;
/*     */   }
/*     */ 
/*     */   public <T extends Throwable> T logSevereException(T exception)
/*     */   {
/* 341 */     if (this.logger.isLoggable(Level.SEVERE)) {
/* 342 */       if (exception.getCause() == null)
/* 343 */         this.logger.logp(Level.SEVERE, this.componentClassName, getCallerMethodName(), exception.getMessage());
/*     */       else {
/* 345 */         this.logger.logp(Level.SEVERE, this.componentClassName, getCallerMethodName(), exception.getMessage(), exception.getCause());
/*     */       }
/*     */     }
/*     */ 
/* 349 */     return exception;
/*     */   }
/*     */ 
/*     */   public <T extends Throwable> T logException(T exception, Throwable cause, Level level)
/*     */   {
/* 369 */     if (this.logger.isLoggable(level)) {
/* 370 */       if (cause == null) {
/* 371 */         this.logger.logp(level, this.componentClassName, getCallerMethodName(), exception.getMessage());
/*     */       } else {
/* 373 */         exception.initCause(cause);
/* 374 */         this.logger.logp(level, this.componentClassName, getCallerMethodName(), exception.getMessage(), cause);
/*     */       }
/*     */     }
/*     */ 
/* 378 */     return exception;
/*     */   }
/*     */ 
/*     */   public <T extends Throwable> T logException(T exception, boolean logCause, Level level)
/*     */   {
/* 401 */     if (this.logger.isLoggable(level)) {
/* 402 */       if ((logCause) && (exception.getCause() != null))
/* 403 */         this.logger.logp(level, this.componentClassName, getCallerMethodName(), exception.getMessage(), exception.getCause());
/*     */       else {
/* 405 */         this.logger.logp(level, this.componentClassName, getCallerMethodName(), exception.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 409 */     return exception;
/*     */   }
/*     */ 
/*     */   public <T extends Throwable> T logException(T exception, Level level)
/*     */   {
/* 417 */     if (this.logger.isLoggable(level)) {
/* 418 */       if (exception.getCause() == null)
/* 419 */         this.logger.logp(level, this.componentClassName, getCallerMethodName(), exception.getMessage());
/*     */       else {
/* 421 */         this.logger.logp(level, this.componentClassName, getCallerMethodName(), exception.getMessage(), exception.getCause());
/*     */       }
/*     */     }
/*     */ 
/* 425 */     return exception;
/*     */   }
/*     */ 
/*     */   private static String getCallerMethodName()
/*     */   {
/* 435 */     return getStackMethodName(5);
/*     */   }
/*     */ 
/*     */   private static String getStackMethodName(int methodIndexInStack)
/*     */   {
/* 449 */     StackTraceElement[] stack = Thread.currentThread().getStackTrace();
/*     */     String methodName;
/*     */     String methodName;
/* 450 */     if (stack.length > methodIndexInStack + 1)
/* 451 */       methodName = stack[methodIndexInStack].getMethodName();
/*     */     else {
/* 453 */       methodName = "UNKNOWN METHOD";
/*     */     }
/*     */ 
/* 456 */     return methodName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.istack.internal.logging.Logger
 * JD-Core Version:    0.6.2
 */
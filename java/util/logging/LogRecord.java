/*     */ package java.util.logging;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Locale;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public class LogRecord
/*     */   implements Serializable
/*     */ {
/*  72 */   private static final AtomicLong globalSequenceNumber = new AtomicLong(0L);
/*     */   private static final int MIN_SEQUENTIAL_THREAD_ID = 1073741823;
/*  85 */   private static final AtomicInteger nextThreadId = new AtomicInteger(1073741823);
/*     */ 
/*  88 */   private static final ThreadLocal<Integer> threadIds = new ThreadLocal();
/*     */   private Level level;
/*     */   private long sequenceNumber;
/*     */   private String sourceClassName;
/*     */   private String sourceMethodName;
/*     */   private String message;
/*     */   private int threadID;
/*     */   private long millis;
/*     */   private Throwable thrown;
/*     */   private String loggerName;
/*     */   private String resourceBundleName;
/*     */   private transient boolean needToInferCaller;
/*     */   private transient Object[] parameters;
/*     */   private transient ResourceBundle resourceBundle;
/*     */   private static final long serialVersionUID = 5372048053134512534L;
/*     */ 
/*     */   private int defaultThreadID()
/*     */   {
/* 148 */     long l = Thread.currentThread().getId();
/* 149 */     if (l < 1073741823L) {
/* 150 */       return (int)l;
/*     */     }
/* 152 */     Integer localInteger = (Integer)threadIds.get();
/* 153 */     if (localInteger == null) {
/* 154 */       localInteger = Integer.valueOf(nextThreadId.getAndIncrement());
/* 155 */       threadIds.set(localInteger);
/*     */     }
/* 157 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public LogRecord(Level paramLevel, String paramString)
/*     */   {
/* 179 */     paramLevel.getClass();
/* 180 */     this.level = paramLevel;
/* 181 */     this.message = paramString;
/*     */ 
/* 183 */     this.sequenceNumber = globalSequenceNumber.getAndIncrement();
/* 184 */     this.threadID = defaultThreadID();
/* 185 */     this.millis = System.currentTimeMillis();
/* 186 */     this.needToInferCaller = true;
/*     */   }
/*     */ 
/*     */   public String getLoggerName()
/*     */   {
/* 195 */     return this.loggerName;
/*     */   }
/*     */ 
/*     */   public void setLoggerName(String paramString)
/*     */   {
/* 204 */     this.loggerName = paramString;
/*     */   }
/*     */ 
/*     */   public ResourceBundle getResourceBundle()
/*     */   {
/* 216 */     return this.resourceBundle;
/*     */   }
/*     */ 
/*     */   public void setResourceBundle(ResourceBundle paramResourceBundle)
/*     */   {
/* 225 */     this.resourceBundle = paramResourceBundle;
/*     */   }
/*     */ 
/*     */   public String getResourceBundleName()
/*     */   {
/* 236 */     return this.resourceBundleName;
/*     */   }
/*     */ 
/*     */   public void setResourceBundleName(String paramString)
/*     */   {
/* 245 */     this.resourceBundleName = paramString;
/*     */   }
/*     */ 
/*     */   public Level getLevel()
/*     */   {
/* 253 */     return this.level;
/*     */   }
/*     */ 
/*     */   public void setLevel(Level paramLevel)
/*     */   {
/* 261 */     if (paramLevel == null) {
/* 262 */       throw new NullPointerException();
/*     */     }
/* 264 */     this.level = paramLevel;
/*     */   }
/*     */ 
/*     */   public long getSequenceNumber()
/*     */   {
/* 276 */     return this.sequenceNumber;
/*     */   }
/*     */ 
/*     */   public void setSequenceNumber(long paramLong)
/*     */   {
/* 286 */     this.sequenceNumber = paramLong;
/*     */   }
/*     */ 
/*     */   public String getSourceClassName()
/*     */   {
/* 304 */     if (this.needToInferCaller) {
/* 305 */       inferCaller();
/*     */     }
/* 307 */     return this.sourceClassName;
/*     */   }
/*     */ 
/*     */   public void setSourceClassName(String paramString)
/*     */   {
/* 316 */     this.sourceClassName = paramString;
/* 317 */     this.needToInferCaller = false;
/*     */   }
/*     */ 
/*     */   public String getSourceMethodName()
/*     */   {
/* 335 */     if (this.needToInferCaller) {
/* 336 */       inferCaller();
/*     */     }
/* 338 */     return this.sourceMethodName;
/*     */   }
/*     */ 
/*     */   public void setSourceMethodName(String paramString)
/*     */   {
/* 347 */     this.sourceMethodName = paramString;
/* 348 */     this.needToInferCaller = false;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 366 */     return this.message;
/*     */   }
/*     */ 
/*     */   public void setMessage(String paramString)
/*     */   {
/* 375 */     this.message = paramString;
/*     */   }
/*     */ 
/*     */   public Object[] getParameters()
/*     */   {
/* 385 */     return this.parameters;
/*     */   }
/*     */ 
/*     */   public void setParameters(Object[] paramArrayOfObject)
/*     */   {
/* 394 */     this.parameters = paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   public int getThreadID()
/*     */   {
/* 406 */     return this.threadID;
/*     */   }
/*     */ 
/*     */   public void setThreadID(int paramInt)
/*     */   {
/* 414 */     this.threadID = paramInt;
/*     */   }
/*     */ 
/*     */   public long getMillis()
/*     */   {
/* 423 */     return this.millis;
/*     */   }
/*     */ 
/*     */   public void setMillis(long paramLong)
/*     */   {
/* 432 */     this.millis = paramLong;
/*     */   }
/*     */ 
/*     */   public Throwable getThrown()
/*     */   {
/* 444 */     return this.thrown;
/*     */   }
/*     */ 
/*     */   public void setThrown(Throwable paramThrowable)
/*     */   {
/* 453 */     this.thrown = paramThrowable;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 470 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 473 */     paramObjectOutputStream.writeByte(1);
/* 474 */     paramObjectOutputStream.writeByte(0);
/* 475 */     if (this.parameters == null) {
/* 476 */       paramObjectOutputStream.writeInt(-1);
/* 477 */       return;
/*     */     }
/* 479 */     paramObjectOutputStream.writeInt(this.parameters.length);
/*     */ 
/* 481 */     for (int i = 0; i < this.parameters.length; i++)
/* 482 */       if (this.parameters[i] == null)
/* 483 */         paramObjectOutputStream.writeObject(null);
/*     */       else
/* 485 */         paramObjectOutputStream.writeObject(this.parameters[i].toString());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 493 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 496 */     int i = paramObjectInputStream.readByte();
/* 497 */     int j = paramObjectInputStream.readByte();
/* 498 */     if (i != 1) {
/* 499 */       throw new IOException("LogRecord: bad version: " + i + "." + j);
/*     */     }
/* 501 */     int k = paramObjectInputStream.readInt();
/* 502 */     if (k == -1) {
/* 503 */       this.parameters = null;
/*     */     } else {
/* 505 */       this.parameters = new Object[k];
/* 506 */       for (int m = 0; m < this.parameters.length; m++) {
/* 507 */         this.parameters[m] = paramObjectInputStream.readObject();
/*     */       }
/*     */     }
/*     */ 
/* 511 */     if (this.resourceBundleName != null)
/*     */     {
/*     */       try
/*     */       {
/* 515 */         ResourceBundle localResourceBundle = ResourceBundle.getBundle(this.resourceBundleName, Locale.getDefault(), ClassLoader.getSystemClassLoader());
/*     */ 
/* 519 */         this.resourceBundle = localResourceBundle;
/*     */       }
/*     */       catch (MissingResourceException localMissingResourceException)
/*     */       {
/* 523 */         this.resourceBundle = null;
/*     */       }
/*     */     }
/*     */ 
/* 527 */     this.needToInferCaller = false;
/*     */   }
/*     */ 
/*     */   private void inferCaller()
/*     */   {
/* 532 */     this.needToInferCaller = false;
/* 533 */     JavaLangAccess localJavaLangAccess = SharedSecrets.getJavaLangAccess();
/* 534 */     Throwable localThrowable = new Throwable();
/* 535 */     int i = localJavaLangAccess.getStackTraceDepth(localThrowable);
/*     */ 
/* 537 */     int j = 1;
/* 538 */     for (int k = 0; k < i; k++)
/*     */     {
/* 541 */       StackTraceElement localStackTraceElement = localJavaLangAccess.getStackTraceElement(localThrowable, k);
/*     */ 
/* 543 */       String str = localStackTraceElement.getClassName();
/* 544 */       boolean bool = isLoggerImplFrame(str);
/* 545 */       if (j != 0)
/*     */       {
/* 547 */         if (bool) {
/* 548 */           j = 0;
/*     */         }
/*     */       }
/* 551 */       else if (!bool)
/*     */       {
/* 553 */         if ((!str.startsWith("java.lang.reflect.")) && (!str.startsWith("sun.reflect.")))
/*     */         {
/* 555 */           setSourceClassName(str);
/* 556 */           setSourceMethodName(localStackTraceElement.getMethodName());
/* 557 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isLoggerImplFrame(String paramString)
/*     */   {
/* 568 */     return (paramString.equals("java.util.logging.Logger")) || (paramString.startsWith("java.util.logging.LoggingProxyImpl")) || (paramString.startsWith("sun.util.logging."));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.LogRecord
 * JD-Core Version:    0.6.2
 */
/*     */ package java.util.logging;
/*     */ 
/*     */ public class MemoryHandler extends Handler
/*     */ {
/*     */   private static final int DEFAULT_SIZE = 1000;
/*     */   private Level pushLevel;
/*     */   private int size;
/*     */   private Handler target;
/*     */   private LogRecord[] buffer;
/*     */   int start;
/*     */   int count;
/*     */ 
/*     */   private void configure()
/*     */   {
/*  87 */     LogManager localLogManager = LogManager.getLogManager();
/*  88 */     String str = getClass().getName();
/*     */ 
/*  90 */     this.pushLevel = localLogManager.getLevelProperty(str + ".push", Level.SEVERE);
/*  91 */     this.size = localLogManager.getIntProperty(str + ".size", 1000);
/*  92 */     if (this.size <= 0) {
/*  93 */       this.size = 1000;
/*     */     }
/*  95 */     setLevel(localLogManager.getLevelProperty(str + ".level", Level.ALL));
/*  96 */     setFilter(localLogManager.getFilterProperty(str + ".filter", null));
/*  97 */     setFormatter(localLogManager.getFormatterProperty(str + ".formatter", new SimpleFormatter()));
/*     */   }
/*     */ 
/*     */   public MemoryHandler()
/*     */   {
/* 105 */     this.sealed = false;
/* 106 */     configure();
/* 107 */     this.sealed = true;
/*     */ 
/* 109 */     String str = "???";
/*     */     try {
/* 111 */       LogManager localLogManager = LogManager.getLogManager();
/* 112 */       str = localLogManager.getProperty("java.util.logging.MemoryHandler.target");
/* 113 */       Class localClass = ClassLoader.getSystemClassLoader().loadClass(str);
/* 114 */       this.target = ((Handler)localClass.newInstance());
/*     */     } catch (Exception localException) {
/* 116 */       throw new RuntimeException("MemoryHandler can't load handler \"" + str + "\"", localException);
/*     */     }
/* 118 */     init();
/*     */   }
/*     */ 
/*     */   private void init()
/*     */   {
/* 123 */     this.buffer = new LogRecord[this.size];
/* 124 */     this.start = 0;
/* 125 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public MemoryHandler(Handler paramHandler, int paramInt, Level paramLevel)
/*     */   {
/* 142 */     if ((paramHandler == null) || (paramLevel == null)) {
/* 143 */       throw new NullPointerException();
/*     */     }
/* 145 */     if (paramInt <= 0) {
/* 146 */       throw new IllegalArgumentException();
/*     */     }
/* 148 */     this.sealed = false;
/* 149 */     configure();
/* 150 */     this.sealed = true;
/* 151 */     this.target = paramHandler;
/* 152 */     this.pushLevel = paramLevel;
/* 153 */     this.size = paramInt;
/* 154 */     init();
/*     */   }
/*     */ 
/*     */   public synchronized void publish(LogRecord paramLogRecord)
/*     */   {
/* 173 */     if (!isLoggable(paramLogRecord)) {
/* 174 */       return;
/*     */     }
/* 176 */     int i = (this.start + this.count) % this.buffer.length;
/* 177 */     this.buffer[i] = paramLogRecord;
/* 178 */     if (this.count < this.buffer.length) {
/* 179 */       this.count += 1;
/*     */     } else {
/* 181 */       this.start += 1;
/* 182 */       this.start %= this.buffer.length;
/*     */     }
/* 184 */     if (paramLogRecord.getLevel().intValue() >= this.pushLevel.intValue())
/* 185 */       push();
/*     */   }
/*     */ 
/*     */   public synchronized void push()
/*     */   {
/* 195 */     for (int i = 0; i < this.count; i++) {
/* 196 */       int j = (this.start + i) % this.buffer.length;
/* 197 */       LogRecord localLogRecord = this.buffer[j];
/* 198 */       this.target.publish(localLogRecord);
/*     */     }
/*     */ 
/* 201 */     this.start = 0;
/* 202 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 212 */     this.target.flush();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws SecurityException
/*     */   {
/* 223 */     this.target.close();
/* 224 */     setLevel(Level.OFF);
/*     */   }
/*     */ 
/*     */   public void setPushLevel(Level paramLevel)
/*     */     throws SecurityException
/*     */   {
/* 237 */     if (paramLevel == null) {
/* 238 */       throw new NullPointerException();
/*     */     }
/* 240 */     LogManager localLogManager = LogManager.getLogManager();
/* 241 */     checkPermission();
/* 242 */     this.pushLevel = paramLevel;
/*     */   }
/*     */ 
/*     */   public synchronized Level getPushLevel()
/*     */   {
/* 251 */     return this.pushLevel;
/*     */   }
/*     */ 
/*     */   public boolean isLoggable(LogRecord paramLogRecord)
/*     */   {
/* 268 */     return super.isLoggable(paramLogRecord);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.MemoryHandler
 * JD-Core Version:    0.6.2
 */
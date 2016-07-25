/*     */ package sun.net;
/*     */ 
/*     */ import java.net.URL;
/*     */ 
/*     */ public class ProgressSource
/*     */ {
/*     */   private URL url;
/*     */   private String method;
/*     */   private String contentType;
/*  45 */   private long progress = 0L;
/*     */ 
/*  47 */   private long lastProgress = 0L;
/*     */ 
/*  49 */   private long expected = -1L;
/*     */   private State state;
/*  53 */   private boolean connected = false;
/*     */ 
/*  55 */   private int threshold = 8192;
/*     */   private ProgressMonitor progressMonitor;
/*     */ 
/*     */   public ProgressSource(URL paramURL, String paramString)
/*     */   {
/*  63 */     this(paramURL, paramString, -1L);
/*     */   }
/*     */ 
/*     */   public ProgressSource(URL paramURL, String paramString, long paramLong)
/*     */   {
/*  70 */     this.url = paramURL;
/*  71 */     this.method = paramString;
/*  72 */     this.contentType = "content/unknown";
/*  73 */     this.progress = 0L;
/*  74 */     this.lastProgress = 0L;
/*  75 */     this.expected = paramLong;
/*  76 */     this.state = State.NEW;
/*  77 */     this.progressMonitor = ProgressMonitor.getDefault();
/*  78 */     this.threshold = this.progressMonitor.getProgressUpdateThreshold();
/*     */   }
/*     */ 
/*     */   public boolean connected() {
/*  82 */     if (!this.connected) {
/*  83 */       this.connected = true;
/*  84 */       this.state = State.CONNECTED;
/*  85 */       return false;
/*     */     }
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*  94 */     this.state = State.DELETE;
/*     */   }
/*     */ 
/*     */   public URL getURL()
/*     */   {
/* 101 */     return this.url;
/*     */   }
/*     */ 
/*     */   public String getMethod()
/*     */   {
/* 108 */     return this.method;
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 115 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   public void setContentType(String paramString)
/*     */   {
/* 120 */     this.contentType = paramString;
/*     */   }
/*     */ 
/*     */   public long getProgress()
/*     */   {
/* 127 */     return this.progress;
/*     */   }
/*     */ 
/*     */   public long getExpected()
/*     */   {
/* 134 */     return this.expected;
/*     */   }
/*     */ 
/*     */   public State getState()
/*     */   {
/* 141 */     return this.state;
/*     */   }
/*     */ 
/*     */   public void beginTracking()
/*     */   {
/* 148 */     this.progressMonitor.registerSource(this);
/*     */   }
/*     */ 
/*     */   public void finishTracking()
/*     */   {
/* 155 */     this.progressMonitor.unregisterSource(this);
/*     */   }
/*     */ 
/*     */   public void updateProgress(long paramLong1, long paramLong2)
/*     */   {
/* 162 */     this.lastProgress = this.progress;
/* 163 */     this.progress = paramLong1;
/* 164 */     this.expected = paramLong2;
/*     */ 
/* 166 */     if (!connected())
/* 167 */       this.state = State.CONNECTED;
/*     */     else {
/* 169 */       this.state = State.UPDATE;
/*     */     }
/*     */ 
/* 191 */     if (this.lastProgress / this.threshold != this.progress / this.threshold) {
/* 192 */       this.progressMonitor.updateProgress(this);
/*     */     }
/*     */ 
/* 196 */     if ((this.expected != -1L) && 
/* 197 */       (this.progress >= this.expected) && (this.progress != 0L))
/* 198 */       close();
/*     */   }
/*     */ 
/*     */   public Object clone() throws CloneNotSupportedException
/*     */   {
/* 203 */     return super.clone();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 207 */     return getClass().getName() + "[url=" + this.url + ", method=" + this.method + ", state=" + this.state + ", content-type=" + this.contentType + ", progress=" + this.progress + ", expected=" + this.expected + "]";
/*     */   }
/*     */ 
/*     */   public static enum State
/*     */   {
/*  36 */     NEW, CONNECTED, UPDATE, DELETE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ProgressSource
 * JD-Core Version:    0.6.2
 */
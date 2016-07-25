/*     */ package sun.net;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class ProgressEvent extends EventObject
/*     */ {
/*     */   private URL url;
/*     */   private String contentType;
/*     */   private String method;
/*     */   private long progress;
/*     */   private long expected;
/*     */   private ProgressSource.State state;
/*     */ 
/*     */   public ProgressEvent(ProgressSource paramProgressSource, URL paramURL, String paramString1, String paramString2, ProgressSource.State paramState, long paramLong1, long paramLong2)
/*     */   {
/*  54 */     super(paramProgressSource);
/*  55 */     this.url = paramURL;
/*  56 */     this.method = paramString1;
/*  57 */     this.contentType = paramString2;
/*  58 */     this.progress = paramLong1;
/*  59 */     this.expected = paramLong2;
/*  60 */     this.state = paramState;
/*     */   }
/*     */ 
/*     */   public URL getURL()
/*     */   {
/*  68 */     return this.url;
/*     */   }
/*     */ 
/*     */   public String getMethod()
/*     */   {
/*  76 */     return this.method;
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/*  84 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   public long getProgress()
/*     */   {
/*  92 */     return this.progress;
/*     */   }
/*     */ 
/*     */   public long getExpected()
/*     */   {
/*  99 */     return this.expected;
/*     */   }
/*     */ 
/*     */   public ProgressSource.State getState()
/*     */   {
/* 106 */     return this.state;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 110 */     return getClass().getName() + "[url=" + this.url + ", method=" + this.method + ", state=" + this.state + ", content-type=" + this.contentType + ", progress=" + this.progress + ", expected=" + this.expected + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ProgressEvent
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.net.www.http;
/*     */ 
/*     */ class KeepAliveCleanerEntry
/*     */ {
/*     */   KeepAliveStream kas;
/*     */   HttpClient hc;
/*     */ 
/*     */   public KeepAliveCleanerEntry(KeepAliveStream paramKeepAliveStream, HttpClient paramHttpClient)
/*     */   {
/* 217 */     this.kas = paramKeepAliveStream;
/* 218 */     this.hc = paramHttpClient;
/*     */   }
/*     */ 
/*     */   protected KeepAliveStream getKeepAliveStream() {
/* 222 */     return this.kas;
/*     */   }
/*     */ 
/*     */   protected HttpClient getHttpClient() {
/* 226 */     return this.hc;
/*     */   }
/*     */ 
/*     */   protected void setQueuedForCleanup() {
/* 230 */     this.kas.queuedForCleanup = true;
/*     */   }
/*     */ 
/*     */   protected boolean getQueuedForCleanup() {
/* 234 */     return this.kas.queuedForCleanup;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.KeepAliveCleanerEntry
 * JD-Core Version:    0.6.2
 */
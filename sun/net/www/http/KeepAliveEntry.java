/*     */ package sun.net.www.http;
/*     */ 
/*     */ class KeepAliveEntry
/*     */ {
/*     */   HttpClient hc;
/*     */   long idleStartTime;
/*     */ 
/*     */   KeepAliveEntry(HttpClient paramHttpClient, long paramLong)
/*     */   {
/* 344 */     this.hc = paramHttpClient;
/* 345 */     this.idleStartTime = paramLong;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.KeepAliveEntry
 * JD-Core Version:    0.6.2
 */
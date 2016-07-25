/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.net.URL;
/*     */ 
/*     */ class KeepAliveKey
/*     */ {
/* 296 */   private String protocol = null;
/* 297 */   private String host = null;
/* 298 */   private int port = 0;
/* 299 */   private Object obj = null;
/*     */ 
/*     */   public KeepAliveKey(URL paramURL, Object paramObject)
/*     */   {
/* 307 */     this.protocol = paramURL.getProtocol();
/* 308 */     this.host = paramURL.getHost();
/* 309 */     this.port = paramURL.getPort();
/* 310 */     this.obj = paramObject;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 318 */     if (!(paramObject instanceof KeepAliveKey))
/* 319 */       return false;
/* 320 */     KeepAliveKey localKeepAliveKey = (KeepAliveKey)paramObject;
/* 321 */     return (this.host.equals(localKeepAliveKey.host)) && (this.port == localKeepAliveKey.port) && (this.protocol.equals(localKeepAliveKey.protocol)) && (this.obj == localKeepAliveKey.obj);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 333 */     String str = this.protocol + this.host + this.port;
/* 334 */     return this.obj == null ? str.hashCode() : str.hashCode() + this.obj.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.KeepAliveKey
 * JD-Core Version:    0.6.2
 */
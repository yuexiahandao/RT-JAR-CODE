/*     */ package sun.net.www.protocol.gopher;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import sun.net.NetworkClient;
/*     */ 
/*     */ class GopherInputStream extends FilterInputStream
/*     */ {
/*     */   NetworkClient parent;
/*     */ 
/*     */   GopherInputStream(NetworkClient paramNetworkClient, InputStream paramInputStream)
/*     */   {
/* 346 */     super(paramInputStream);
/* 347 */     this.parent = paramNetworkClient;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 352 */       this.parent.closeServer();
/* 353 */       super.close();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.protocol.gopher.GopherInputStream
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Stack;
/*     */ 
/*     */ class ClientVector extends Stack<KeepAliveEntry>
/*     */ {
/*     */   private static final long serialVersionUID = -8680532108106489459L;
/*     */   int nap;
/*     */ 
/*     */   ClientVector(int paramInt)
/*     */   {
/* 249 */     this.nap = paramInt;
/*     */   }
/*     */ 
/*     */   synchronized HttpClient get() {
/* 253 */     if (empty()) {
/* 254 */       return null;
/*     */     }
/*     */ 
/* 257 */     HttpClient localHttpClient = null;
/* 258 */     long l = System.currentTimeMillis();
/*     */     do {
/* 260 */       KeepAliveEntry localKeepAliveEntry = (KeepAliveEntry)pop();
/* 261 */       if (l - localKeepAliveEntry.idleStartTime > this.nap)
/* 262 */         localKeepAliveEntry.hc.closeServer();
/*     */       else
/* 264 */         localHttpClient = localKeepAliveEntry.hc;
/*     */     }
/* 266 */     while ((localHttpClient == null) && (!empty()));
/* 267 */     return localHttpClient;
/*     */   }
/*     */ 
/*     */   synchronized void put(HttpClient paramHttpClient)
/*     */   {
/* 273 */     if (size() >= KeepAliveCache.getMaxConnections())
/* 274 */       paramHttpClient.closeServer();
/*     */     else
/* 276 */       push(new KeepAliveEntry(paramHttpClient, System.currentTimeMillis()));
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 285 */     throw new NotSerializableException();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 290 */     throw new NotSerializableException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.ClientVector
 * JD-Core Version:    0.6.2
 */
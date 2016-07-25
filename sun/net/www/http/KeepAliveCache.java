/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ 
/*     */ public class KeepAliveCache extends HashMap<KeepAliveKey, ClientVector>
/*     */   implements Runnable
/*     */ {
/*     */   private static final long serialVersionUID = -2937172892064557949L;
/*     */   static final int MAX_CONNECTIONS = 5;
/*  52 */   static int result = -1;
/*     */   static final int LIFETIME = 5000;
/*  67 */   private Thread keepAliveTimer = null;
/*     */ 
/*     */   static int getMaxConnections()
/*     */   {
/*  54 */     if (result == -1) {
/*  55 */       result = ((Integer)AccessController.doPrivileged(new GetIntegerAction("http.maxConnections", 5))).intValue();
/*     */ 
/*  59 */       if (result <= 0)
/*  60 */         result = 5;
/*     */     }
/*  62 */     return result;
/*     */   }
/*     */ 
/*     */   public synchronized void put(URL paramURL, Object paramObject, HttpClient paramHttpClient)
/*     */   {
/*  80 */     int i = this.keepAliveTimer == null ? 1 : 0;
/*  81 */     if ((i == 0) && 
/*  82 */       (!this.keepAliveTimer.isAlive())) {
/*  83 */       i = 1;
/*     */     }
/*     */ 
/*  86 */     if (i != 0) {
/*  87 */       clear();
/*     */ 
/*  94 */       localObject = this;
/*  95 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Void run()
/*     */         {
/* 100 */           Object localObject = Thread.currentThread().getThreadGroup();
/* 101 */           ThreadGroup localThreadGroup = null;
/* 102 */           while ((localThreadGroup = ((ThreadGroup)localObject).getParent()) != null) {
/* 103 */             localObject = localThreadGroup;
/*     */           }
/*     */ 
/* 106 */           KeepAliveCache.this.keepAliveTimer = new Thread((ThreadGroup)localObject, this.val$cache, "Keep-Alive-Timer");
/* 107 */           KeepAliveCache.this.keepAliveTimer.setDaemon(true);
/* 108 */           KeepAliveCache.this.keepAliveTimer.setPriority(8);
/*     */ 
/* 111 */           KeepAliveCache.this.keepAliveTimer.setContextClassLoader(null);
/* 112 */           KeepAliveCache.this.keepAliveTimer.start();
/* 113 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/* 118 */     Object localObject = new KeepAliveKey(paramURL, paramObject);
/* 119 */     ClientVector localClientVector = (ClientVector)super.get(localObject);
/*     */ 
/* 121 */     if (localClientVector == null) {
/* 122 */       int j = paramHttpClient.getKeepAliveTimeout();
/* 123 */       localClientVector = new ClientVector(j > 0 ? j * 1000 : 5000);
/*     */ 
/* 125 */       localClientVector.put(paramHttpClient);
/* 126 */       super.put(localObject, localClientVector);
/*     */     } else {
/* 128 */       localClientVector.put(paramHttpClient);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void remove(HttpClient paramHttpClient, Object paramObject)
/*     */   {
/* 134 */     KeepAliveKey localKeepAliveKey = new KeepAliveKey(paramHttpClient.url, paramObject);
/* 135 */     ClientVector localClientVector = (ClientVector)super.get(localKeepAliveKey);
/* 136 */     if (localClientVector != null) {
/* 137 */       localClientVector.remove(paramHttpClient);
/* 138 */       if (localClientVector.empty())
/* 139 */         removeVector(localKeepAliveKey);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void removeVector(KeepAliveKey paramKeepAliveKey)
/*     */   {
/* 148 */     super.remove(paramKeepAliveKey);
/*     */   }
/*     */ 
/*     */   public synchronized HttpClient get(URL paramURL, Object paramObject)
/*     */   {
/* 156 */     KeepAliveKey localKeepAliveKey = new KeepAliveKey(paramURL, paramObject);
/* 157 */     ClientVector localClientVector = (ClientVector)super.get(localKeepAliveKey);
/* 158 */     if (localClientVector == null) {
/* 159 */       return null;
/*     */     }
/* 161 */     return localClientVector.get();
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     do
/*     */     {
/*     */       try
/*     */       {
/* 172 */         Thread.sleep(5000L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException)
/*     */       {
/*     */       }
/*     */       Iterator localIterator;
/*     */       KeepAliveKey localKeepAliveKey;
/* 174 */       synchronized (this)
/*     */       {
/* 185 */         long l = System.currentTimeMillis();
/*     */ 
/* 187 */         ArrayList localArrayList = new ArrayList();
/*     */ 
/* 190 */         for (localIterator = keySet().iterator(); localIterator.hasNext(); ) { localKeepAliveKey = (KeepAliveKey)localIterator.next();
/* 191 */           ClientVector localClientVector = (ClientVector)get(localKeepAliveKey);
/* 192 */           synchronized (localClientVector)
/*     */           {
/* 195 */             for (int i = 0; i < localClientVector.size(); i++) {
/* 196 */               KeepAliveEntry localKeepAliveEntry = (KeepAliveEntry)localClientVector.elementAt(i);
/* 197 */               if (l - localKeepAliveEntry.idleStartTime <= localClientVector.nap) break;
/* 198 */               HttpClient localHttpClient = localKeepAliveEntry.hc;
/* 199 */               localHttpClient.closeServer();
/*     */             }
/*     */ 
/* 204 */             localClientVector.subList(0, i).clear();
/*     */ 
/* 206 */             if (localClientVector.size() == 0) {
/* 207 */               localArrayList.add(localKeepAliveKey);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/* 212 */         for (localIterator = localArrayList.iterator(); localIterator.hasNext(); ) { localKeepAliveKey = (KeepAliveKey)localIterator.next();
/* 213 */           removeVector(localKeepAliveKey); }
/*     */       }
/*     */     }
/* 216 */     while (size() > 0);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 226 */     throw new NotSerializableException();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 231 */     throw new NotSerializableException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.KeepAliveCache
 * JD-Core Version:    0.6.2
 */
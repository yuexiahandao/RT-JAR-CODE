/*     */ package sun.net.www.http;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.net.ProgressSource;
/*     */ import sun.net.www.MeteredStream;
/*     */ 
/*     */ public class KeepAliveStream extends MeteredStream
/*     */   implements Hurryable
/*     */ {
/*     */   HttpClient hc;
/*     */   boolean hurried;
/*  48 */   protected boolean queuedForCleanup = false;
/*     */ 
/*  50 */   private static final KeepAliveStreamCleaner queue = new KeepAliveStreamCleaner();
/*     */   private static Thread cleanerThread;
/*     */ 
/*     */   public KeepAliveStream(InputStream paramInputStream, ProgressSource paramProgressSource, long paramLong, HttpClient paramHttpClient)
/*     */   {
/*  57 */     super(paramInputStream, paramProgressSource, paramLong);
/*  58 */     this.hc = paramHttpClient;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  66 */     if (this.closed) {
/*  67 */       return;
/*     */     }
/*     */ 
/*  71 */     if (this.queuedForCleanup) {
/*  72 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  83 */       if (this.expected > this.count) {
/*  84 */         long l1 = this.expected - this.count;
/*  85 */         if (l1 <= available()) {
/*  86 */           long l2 = 0L;
/*  87 */           while (l2 < l1) {
/*  88 */             l1 -= l2;
/*  89 */             l2 = skip(l1);
/*     */           }
/*  91 */         } else if ((this.expected <= KeepAliveStreamCleaner.MAX_DATA_REMAINING) && (!this.hurried))
/*     */         {
/*  94 */           queueForCleanup(new KeepAliveCleanerEntry(this, this.hc));
/*     */         } else {
/*  96 */           this.hc.closeServer();
/*     */         }
/*     */       }
/*  99 */       if ((!this.closed) && (!this.hurried) && (!this.queuedForCleanup))
/* 100 */         this.hc.finished();
/*     */     }
/*     */     finally {
/* 103 */       if (this.pi != null) {
/* 104 */         this.pi.finishTracking();
/*     */       }
/* 106 */       if (!this.queuedForCleanup)
/*     */       {
/* 109 */         this.in = null;
/* 110 */         this.hc = null;
/* 111 */         this.closed = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 119 */     return false;
/*     */   }
/*     */   public void mark(int paramInt) {
/*     */   }
/*     */ 
/*     */   public void reset() throws IOException {
/* 125 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ 
/*     */   public synchronized boolean hurry()
/*     */   {
/*     */     try {
/* 131 */       if ((this.closed) || (this.count >= this.expected))
/* 132 */         return false;
/* 133 */       if (this.in.available() < this.expected - this.count)
/*     */       {
/* 135 */         return false;
/*     */       }
/*     */ 
/* 140 */       int i = (int)(this.expected - this.count);
/* 141 */       byte[] arrayOfByte = new byte[i];
/* 142 */       DataInputStream localDataInputStream = new DataInputStream(this.in);
/* 143 */       localDataInputStream.readFully(arrayOfByte);
/* 144 */       this.in = new ByteArrayInputStream(arrayOfByte);
/* 145 */       this.hurried = true;
/* 146 */       return true;
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   private static void queueForCleanup(KeepAliveCleanerEntry paramKeepAliveCleanerEntry)
/*     */   {
/* 155 */     synchronized (queue) {
/* 156 */       if (!paramKeepAliveCleanerEntry.getQueuedForCleanup()) {
/* 157 */         if (!queue.offer(paramKeepAliveCleanerEntry)) {
/* 158 */           paramKeepAliveCleanerEntry.getHttpClient().closeServer();
/* 159 */           return;
/*     */         }
/*     */ 
/* 162 */         paramKeepAliveCleanerEntry.setQueuedForCleanup();
/* 163 */         queue.notifyAll();
/*     */       }
/*     */ 
/* 166 */       int i = cleanerThread == null ? 1 : 0;
/* 167 */       if ((i == 0) && 
/* 168 */         (!cleanerThread.isAlive())) {
/* 169 */         i = 1;
/*     */       }
/*     */ 
/* 173 */       if (i != 0)
/* 174 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Void run()
/*     */           {
/* 179 */             Object localObject = Thread.currentThread().getThreadGroup();
/* 180 */             ThreadGroup localThreadGroup = null;
/* 181 */             while ((localThreadGroup = ((ThreadGroup)localObject).getParent()) != null) {
/* 182 */               localObject = localThreadGroup;
/*     */             }
/*     */ 
/* 185 */             KeepAliveStream.access$002(new Thread((ThreadGroup)localObject, KeepAliveStream.queue, "Keep-Alive-SocketCleaner"));
/* 186 */             KeepAliveStream.cleanerThread.setDaemon(true);
/* 187 */             KeepAliveStream.cleanerThread.setPriority(8);
/*     */ 
/* 190 */             KeepAliveStream.cleanerThread.setContextClassLoader(null);
/* 191 */             KeepAliveStream.cleanerThread.start();
/* 192 */             return null;
/*     */           }
/*     */         });
/*     */     }
/*     */   }
/*     */ 
/*     */   protected long remainingToRead()
/*     */   {
/* 200 */     return this.expected - this.count;
/*     */   }
/*     */ 
/*     */   protected void setClosed() {
/* 204 */     this.in = null;
/* 205 */     this.hc = null;
/* 206 */     this.closed = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.http.KeepAliveStream
 * JD-Core Version:    0.6.2
 */
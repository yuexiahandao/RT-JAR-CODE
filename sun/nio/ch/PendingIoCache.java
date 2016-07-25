/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.nio.channels.AsynchronousCloseException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class PendingIoCache
/*     */ {
/*  38 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*  39 */   private static final int addressSize = unsafe.addressSize();
/*     */ 
/*  54 */   private static final int SIZEOF_OVERLAPPED = dependsArch(20, 32);
/*     */   private boolean closed;
/*     */   private boolean closePending;
/*  63 */   private final Map<Long, PendingFuture> pendingIoMap = new HashMap();
/*     */ 
/*  67 */   private long[] overlappedCache = new long[4];
/*  68 */   private int overlappedCacheCount = 0;
/*     */ 
/*     */   private static int dependsArch(int paramInt1, int paramInt2)
/*     */   {
/*  42 */     return addressSize == 4 ? paramInt1 : paramInt2;
/*     */   }
/*     */ 
/*     */   long add(PendingFuture<?, ?> paramPendingFuture)
/*     */   {
/*  74 */     synchronized (this) {
/*  75 */       if (this.closed)
/*  76 */         throw new AssertionError("Should not get here");
/*     */       long l;
/*  78 */       if (this.overlappedCacheCount > 0)
/*  79 */         l = this.overlappedCache[(--this.overlappedCacheCount)];
/*     */       else {
/*  81 */         l = unsafe.allocateMemory(SIZEOF_OVERLAPPED);
/*     */       }
/*  83 */       this.pendingIoMap.put(Long.valueOf(l), paramPendingFuture);
/*  84 */       return l;
/*     */     }
/*     */   }
/*     */ 
/*     */   <V, A> PendingFuture<V, A> remove(long paramLong)
/*     */   {
/*  90 */     synchronized (this) {
/*  91 */       PendingFuture localPendingFuture = (PendingFuture)this.pendingIoMap.remove(Long.valueOf(paramLong));
/*  92 */       if (localPendingFuture != null) {
/*  93 */         if (this.overlappedCacheCount < this.overlappedCache.length) {
/*  94 */           this.overlappedCache[(this.overlappedCacheCount++)] = paramLong;
/*     */         }
/*     */         else {
/*  97 */           unsafe.freeMemory(paramLong);
/*     */         }
/*     */ 
/* 100 */         if (this.closePending) {
/* 101 */           notifyAll();
/*     */         }
/*     */       }
/* 104 */       return localPendingFuture;
/*     */     }
/*     */   }
/*     */ 
/*     */   void close() {
/* 109 */     synchronized (this) {
/* 110 */       if (this.closed) {
/* 111 */         return;
/*     */       }
/*     */ 
/* 114 */       if (!this.pendingIoMap.isEmpty()) {
/* 115 */         clearPendingIoMap();
/*     */       }
/*     */ 
/* 118 */       while (this.overlappedCacheCount > 0) {
/* 119 */         unsafe.freeMemory(this.overlappedCache[(--this.overlappedCacheCount)]);
/*     */       }
/*     */ 
/* 123 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void clearPendingIoMap() {
/* 128 */     assert (Thread.holdsLock(this));
/*     */ 
/* 131 */     this.closePending = true;
/*     */     try {
/* 133 */       wait(50L);
/*     */     } catch (InterruptedException localInterruptedException) {
/* 135 */       Thread.currentThread().interrupt();
/*     */     }
/* 137 */     this.closePending = false;
/* 138 */     if (this.pendingIoMap.isEmpty()) {
/* 139 */       return;
/*     */     }
/*     */ 
/* 143 */     for (Long localLong : this.pendingIoMap.keySet()) {
/* 144 */       PendingFuture localPendingFuture = (PendingFuture)this.pendingIoMap.get(localLong);
/* 145 */       assert (!localPendingFuture.isDone());
/*     */ 
/* 148 */       Iocp localIocp = (Iocp)((Groupable)localPendingFuture.channel()).group();
/* 149 */       localIocp.makeStale(localLong);
/*     */ 
/* 152 */       final Iocp.ResultHandler localResultHandler = (Iocp.ResultHandler)localPendingFuture.getContext();
/* 153 */       Runnable local1 = new Runnable() {
/*     */         public void run() {
/* 155 */           localResultHandler.failed(-1, new AsynchronousCloseException());
/*     */         }
/*     */       };
/* 158 */       localIocp.executeOnPooledThread(local1);
/*     */     }
/* 160 */     this.pendingIoMap.clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.PendingIoCache
 * JD-Core Version:    0.6.2
 */
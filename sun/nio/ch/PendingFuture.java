/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.AsynchronousChannel;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ final class PendingFuture<V, A>
/*     */   implements Future<V>
/*     */ {
/*  38 */   private static final CancellationException CANCELLED = new CancellationException();
/*     */   private final AsynchronousChannel channel;
/*     */   private final CompletionHandler<V, ? super A> handler;
/*     */   private final A attachment;
/*     */   private volatile boolean haveResult;
/*     */   private volatile V result;
/*     */   private volatile Throwable exc;
/*     */   private CountDownLatch latch;
/*     */   private Future<?> timeoutTask;
/*     */   private volatile Object context;
/*     */ 
/*     */   PendingFuture(AsynchronousChannel paramAsynchronousChannel, CompletionHandler<V, ? super A> paramCompletionHandler, A paramA, Object paramObject)
/*     */   {
/*  64 */     this.channel = paramAsynchronousChannel;
/*  65 */     this.handler = paramCompletionHandler;
/*  66 */     this.attachment = paramA;
/*  67 */     this.context = paramObject;
/*     */   }
/*     */ 
/*     */   PendingFuture(AsynchronousChannel paramAsynchronousChannel, CompletionHandler<V, ? super A> paramCompletionHandler, A paramA)
/*     */   {
/*  74 */     this.channel = paramAsynchronousChannel;
/*  75 */     this.handler = paramCompletionHandler;
/*  76 */     this.attachment = paramA;
/*     */   }
/*     */ 
/*     */   PendingFuture(AsynchronousChannel paramAsynchronousChannel) {
/*  80 */     this(paramAsynchronousChannel, null, null);
/*     */   }
/*     */ 
/*     */   PendingFuture(AsynchronousChannel paramAsynchronousChannel, Object paramObject) {
/*  84 */     this(paramAsynchronousChannel, null, null, paramObject);
/*     */   }
/*     */ 
/*     */   AsynchronousChannel channel() {
/*  88 */     return this.channel;
/*     */   }
/*     */ 
/*     */   CompletionHandler<V, ? super A> handler() {
/*  92 */     return this.handler;
/*     */   }
/*     */ 
/*     */   A attachment() {
/*  96 */     return this.attachment;
/*     */   }
/*     */ 
/*     */   void setContext(Object paramObject) {
/* 100 */     this.context = paramObject;
/*     */   }
/*     */ 
/*     */   Object getContext() {
/* 104 */     return this.context;
/*     */   }
/*     */ 
/*     */   void setTimeoutTask(Future<?> paramFuture) {
/* 108 */     synchronized (this) {
/* 109 */       if (this.haveResult)
/* 110 */         paramFuture.cancel(false);
/*     */       else
/* 112 */         this.timeoutTask = paramFuture;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean prepareForWait()
/*     */   {
/* 119 */     synchronized (this) {
/* 120 */       if (this.haveResult) {
/* 121 */         return false;
/*     */       }
/* 123 */       if (this.latch == null)
/* 124 */         this.latch = new CountDownLatch(1);
/* 125 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   void setResult(V paramV)
/*     */   {
/* 134 */     synchronized (this) {
/* 135 */       if (this.haveResult)
/* 136 */         return;
/* 137 */       this.result = paramV;
/* 138 */       this.haveResult = true;
/* 139 */       if (this.timeoutTask != null)
/* 140 */         this.timeoutTask.cancel(false);
/* 141 */       if (this.latch != null)
/* 142 */         this.latch.countDown();
/*     */     }
/*     */   }
/*     */ 
/*     */   void setFailure(Throwable paramThrowable)
/*     */   {
/* 150 */     if ((!(paramThrowable instanceof IOException)) && (!(paramThrowable instanceof SecurityException)))
/* 151 */       paramThrowable = new IOException(paramThrowable);
/* 152 */     synchronized (this) {
/* 153 */       if (this.haveResult)
/* 154 */         return;
/* 155 */       this.exc = paramThrowable;
/* 156 */       this.haveResult = true;
/* 157 */       if (this.timeoutTask != null)
/* 158 */         this.timeoutTask.cancel(false);
/* 159 */       if (this.latch != null)
/* 160 */         this.latch.countDown();
/*     */     }
/*     */   }
/*     */ 
/*     */   void setResult(V paramV, Throwable paramThrowable)
/*     */   {
/* 168 */     if (paramThrowable == null)
/* 169 */       setResult(paramV);
/*     */     else
/* 171 */       setFailure(paramThrowable);
/*     */   }
/*     */ 
/*     */   public V get()
/*     */     throws ExecutionException, InterruptedException
/*     */   {
/* 177 */     if (!this.haveResult) {
/* 178 */       boolean bool = prepareForWait();
/* 179 */       if (bool)
/* 180 */         this.latch.await();
/*     */     }
/* 182 */     if (this.exc != null) {
/* 183 */       if (this.exc == CANCELLED)
/* 184 */         throw new CancellationException();
/* 185 */       throw new ExecutionException(this.exc);
/*     */     }
/* 187 */     return this.result;
/*     */   }
/*     */ 
/*     */   public V get(long paramLong, TimeUnit paramTimeUnit)
/*     */     throws ExecutionException, InterruptedException, TimeoutException
/*     */   {
/* 194 */     if (!this.haveResult) {
/* 195 */       boolean bool = prepareForWait();
/* 196 */       if ((bool) && 
/* 197 */         (!this.latch.await(paramLong, paramTimeUnit))) throw new TimeoutException();
/*     */     }
/* 199 */     if (this.exc != null) {
/* 200 */       if (this.exc == CANCELLED)
/* 201 */         throw new CancellationException();
/* 202 */       throw new ExecutionException(this.exc);
/*     */     }
/* 204 */     return this.result;
/*     */   }
/*     */ 
/*     */   Throwable exception() {
/* 208 */     return this.exc != CANCELLED ? this.exc : null;
/*     */   }
/*     */ 
/*     */   V value() {
/* 212 */     return this.result;
/*     */   }
/*     */ 
/*     */   public boolean isCancelled()
/*     */   {
/* 217 */     return this.exc == CANCELLED;
/*     */   }
/*     */ 
/*     */   public boolean isDone()
/*     */   {
/* 222 */     return this.haveResult;
/*     */   }
/*     */ 
/*     */   public boolean cancel(boolean paramBoolean)
/*     */   {
/* 227 */     synchronized (this) {
/* 228 */       if (this.haveResult) {
/* 229 */         return false;
/*     */       }
/*     */ 
/* 232 */       if ((channel() instanceof Cancellable)) {
/* 233 */         ((Cancellable)channel()).onCancel(this);
/*     */       }
/*     */ 
/* 236 */       this.exc = CANCELLED;
/* 237 */       this.haveResult = true;
/* 238 */       if (this.timeoutTask != null) {
/* 239 */         this.timeoutTask.cancel(false);
/*     */       }
/*     */     }
/*     */ 
/* 243 */     if (paramBoolean)
/*     */       try {
/* 245 */         channel().close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/* 250 */     if (this.latch != null)
/* 251 */       this.latch.countDown();
/* 252 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.PendingFuture
 * JD-Core Version:    0.6.2
 */
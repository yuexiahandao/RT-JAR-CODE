/*     */ package java.util.concurrent;
/*     */ 
/*     */ public class ExecutorCompletionService<V>
/*     */   implements CompletionService<V>
/*     */ {
/*     */   private final Executor executor;
/*     */   private final AbstractExecutorService aes;
/*     */   private final BlockingQueue<Future<V>> completionQueue;
/*     */ 
/*     */   private RunnableFuture<V> newTaskFor(Callable<V> paramCallable)
/*     */   {
/* 125 */     if (this.aes == null) {
/* 126 */       return new FutureTask(paramCallable);
/*     */     }
/* 128 */     return this.aes.newTaskFor(paramCallable);
/*     */   }
/*     */ 
/*     */   private RunnableFuture<V> newTaskFor(Runnable paramRunnable, V paramV) {
/* 132 */     if (this.aes == null) {
/* 133 */       return new FutureTask(paramRunnable, paramV);
/*     */     }
/* 135 */     return this.aes.newTaskFor(paramRunnable, paramV);
/*     */   }
/*     */ 
/*     */   public ExecutorCompletionService(Executor paramExecutor)
/*     */   {
/* 147 */     if (paramExecutor == null)
/* 148 */       throw new NullPointerException();
/* 149 */     this.executor = paramExecutor;
/* 150 */     this.aes = ((paramExecutor instanceof AbstractExecutorService) ? (AbstractExecutorService)paramExecutor : null);
/*     */ 
/* 152 */     this.completionQueue = new LinkedBlockingQueue();
/*     */   }
/*     */ 
/*     */   public ExecutorCompletionService(Executor paramExecutor, BlockingQueue<Future<V>> paramBlockingQueue)
/*     */   {
/* 170 */     if ((paramExecutor == null) || (paramBlockingQueue == null))
/* 171 */       throw new NullPointerException();
/* 172 */     this.executor = paramExecutor;
/* 173 */     this.aes = ((paramExecutor instanceof AbstractExecutorService) ? (AbstractExecutorService)paramExecutor : null);
/*     */ 
/* 175 */     this.completionQueue = paramBlockingQueue;
/*     */   }
/*     */ 
/*     */   public Future<V> submit(Callable<V> paramCallable) {
/* 179 */     if (paramCallable == null) throw new NullPointerException();
/* 180 */     RunnableFuture localRunnableFuture = newTaskFor(paramCallable);
/* 181 */     this.executor.execute(new QueueingFuture(localRunnableFuture));
/* 182 */     return localRunnableFuture;
/*     */   }
/*     */ 
/*     */   public Future<V> submit(Runnable paramRunnable, V paramV) {
/* 186 */     if (paramRunnable == null) throw new NullPointerException();
/* 187 */     RunnableFuture localRunnableFuture = newTaskFor(paramRunnable, paramV);
/* 188 */     this.executor.execute(new QueueingFuture(localRunnableFuture));
/* 189 */     return localRunnableFuture;
/*     */   }
/*     */ 
/*     */   public Future<V> take() throws InterruptedException {
/* 193 */     return (Future)this.completionQueue.take();
/*     */   }
/*     */ 
/*     */   public Future<V> poll() {
/* 197 */     return (Future)this.completionQueue.poll();
/*     */   }
/*     */ 
/*     */   public Future<V> poll(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException
/*     */   {
/* 202 */     return (Future)this.completionQueue.poll(paramLong, paramTimeUnit);
/*     */   }
/*     */ 
/*     */   private class QueueingFuture extends FutureTask<Void>
/*     */   {
/*     */     private final Future<V> task;
/*     */ 
/*     */     QueueingFuture()
/*     */     {
/* 117 */       super(null);
/* 118 */       this.task = localRunnable;
/*     */     }
/* 120 */     protected void done() { ExecutorCompletionService.this.completionQueue.add(this.task); }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ExecutorCompletionService
 * JD-Core Version:    0.6.2
 */
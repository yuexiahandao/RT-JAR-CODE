/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract class AbstractExecutorService
/*     */   implements ExecutorService
/*     */ {
/*     */   protected <T> RunnableFuture<T> newTaskFor(Runnable paramRunnable, T paramT)
/*     */   {
/*  86 */     return new FutureTask(paramRunnable, paramT);
/*     */   }
/*     */ 
/*     */   protected <T> RunnableFuture<T> newTaskFor(Callable<T> paramCallable)
/*     */   {
/* 100 */     return new FutureTask(paramCallable);
/*     */   }
/*     */ 
/*     */   public Future<?> submit(Runnable paramRunnable)
/*     */   {
/* 108 */     if (paramRunnable == null) throw new NullPointerException();
/* 109 */     RunnableFuture localRunnableFuture = newTaskFor(paramRunnable, null);
/* 110 */     execute(localRunnableFuture);
/* 111 */     return localRunnableFuture;
/*     */   }
/*     */ 
/*     */   public <T> Future<T> submit(Runnable paramRunnable, T paramT)
/*     */   {
/* 119 */     if (paramRunnable == null) throw new NullPointerException();
/* 120 */     RunnableFuture localRunnableFuture = newTaskFor(paramRunnable, paramT);
/* 121 */     execute(localRunnableFuture);
/* 122 */     return localRunnableFuture;
/*     */   }
/*     */ 
/*     */   public <T> Future<T> submit(Callable<T> paramCallable)
/*     */   {
/* 130 */     if (paramCallable == null) throw new NullPointerException();
/* 131 */     RunnableFuture localRunnableFuture = newTaskFor(paramCallable);
/* 132 */     execute(localRunnableFuture);
/* 133 */     return localRunnableFuture;
/*     */   }
/*     */ 
/*     */   private <T> T doInvokeAny(Collection<? extends Callable<T>> paramCollection, boolean paramBoolean, long paramLong)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/* 142 */     if (paramCollection == null)
/* 143 */       throw new NullPointerException();
/* 144 */     int i = paramCollection.size();
/* 145 */     if (i == 0)
/* 146 */       throw new IllegalArgumentException();
/* 147 */     ArrayList localArrayList = new ArrayList(i);
/* 148 */     ExecutorCompletionService localExecutorCompletionService = new ExecutorCompletionService(this);
/*     */     try
/*     */     {
/* 160 */       Object localObject1 = null;
/* 161 */       long l1 = paramBoolean ? System.nanoTime() : 0L;
/* 162 */       Iterator localIterator1 = paramCollection.iterator();
/*     */ 
/* 165 */       localArrayList.add(localExecutorCompletionService.submit((Callable)localIterator1.next()));
/* 166 */       i--;
/* 167 */       int j = 1;
/*     */       while (true)
/*     */       {
/* 170 */         Future localFuture1 = localExecutorCompletionService.poll();
/* 171 */         if (localFuture1 == null)
/* 172 */           if (i > 0) {
/* 173 */             i--;
/* 174 */             localArrayList.add(localExecutorCompletionService.submit((Callable)localIterator1.next()));
/* 175 */             j++;
/*     */           } else {
/* 177 */             if (j == 0)
/*     */               break;
/* 179 */             if (paramBoolean) {
/* 180 */               localFuture1 = localExecutorCompletionService.poll(paramLong, TimeUnit.NANOSECONDS);
/* 181 */               if (localFuture1 == null)
/* 182 */                 throw new TimeoutException();
/* 183 */               long l2 = System.nanoTime();
/* 184 */               paramLong -= l2 - l1;
/* 185 */               l1 = l2;
/*     */             }
/*     */             else {
/* 188 */               localFuture1 = localExecutorCompletionService.take();
/*     */             }
/*     */           }
/* 190 */         if (localFuture1 != null) {
/* 191 */           j--;
/*     */           try
/*     */           {
/*     */             Iterator localIterator2;
/*     */             Future localFuture2;
/* 193 */             return localFuture1.get();
/*     */           } catch (ExecutionException localExecutionException) {
/* 195 */             localObject1 = localExecutionException;
/*     */           } catch (RuntimeException localRuntimeException) {
/* 197 */             localObject1 = new ExecutionException(localRuntimeException);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 202 */       if (localObject1 == null)
/* 203 */         localObject1 = new ExecutionException();
/* 204 */       throw ((Throwable)localObject1);
/*     */     }
/*     */     finally {
/* 207 */       for (Future localFuture3 : localArrayList)
/* 208 */         localFuture3.cancel(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection) throws InterruptedException, ExecutionException
/*     */   {
/*     */     try {
/* 215 */       return doInvokeAny(paramCollection, false, 0L);
/*     */     } catch (TimeoutException localTimeoutException) {
/* 217 */       if (!$assertionsDisabled) throw new AssertionError(); 
/*     */     }
/* 218 */     return null;
/*     */   }
/*     */ 
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException, ExecutionException, TimeoutException
/*     */   {
/* 225 */     return doInvokeAny(paramCollection, true, paramTimeUnit.toNanos(paramLong));
/*     */   }
/*     */ 
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection) throws InterruptedException
/*     */   {
/* 230 */     if (paramCollection == null)
/* 231 */       throw new NullPointerException();
/* 232 */     ArrayList localArrayList = new ArrayList(paramCollection.size());
/* 233 */     int i = 0;
/*     */     try {
/* 235 */       for (Object localObject1 = paramCollection.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Callable)((Iterator)localObject1).next();
/* 236 */         RunnableFuture localRunnableFuture = newTaskFor((Callable)localObject2);
/* 237 */         localArrayList.add(localRunnableFuture);
/* 238 */         execute(localRunnableFuture);
/*     */       }
/* 240 */       Object localObject2;
/* 240 */       for (localObject1 = localArrayList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Future)((Iterator)localObject1).next();
/* 241 */         if (!((Future)localObject2).isDone())
/*     */           try {
/* 243 */             ((Future)localObject2).get();
/*     */           }
/*     */           catch (CancellationException localCancellationException) {
/*     */           }
/*     */           catch (ExecutionException localExecutionException) {
/*     */           } }
/* 249 */       i = 1;
/* 250 */       localObject1 = localArrayList;
/*     */ 
/* 252 */       if (i == 0)
/* 253 */         for (localObject2 = localArrayList.iterator(); ((Iterator)localObject2).hasNext(); ) { Future localFuture1 = (Future)((Iterator)localObject2).next();
/* 254 */           localFuture1.cancel(true); }  return localObject1;
/*     */     }
/*     */     finally
/*     */     {
/* 252 */       if (i == 0)
/* 253 */         for (Future localFuture2 : localArrayList)
/* 254 */           localFuture2.cancel(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> paramCollection, long paramLong, TimeUnit paramTimeUnit)
/*     */     throws InterruptedException
/*     */   {
/* 261 */     if ((paramCollection == null) || (paramTimeUnit == null))
/* 262 */       throw new NullPointerException();
/* 263 */     long l1 = paramTimeUnit.toNanos(paramLong);
/* 264 */     ArrayList localArrayList1 = new ArrayList(paramCollection.size());
/* 265 */     int i = 0;
/*     */     try {
/* 267 */       for (Callable localCallable : paramCollection) {
/* 268 */         localArrayList1.add(newTaskFor(localCallable));
/*     */       }
/* 270 */       long l2 = System.nanoTime();
/*     */ 
/* 274 */       Iterator localIterator2 = localArrayList1.iterator();
/*     */       ArrayList localArrayList2;
/*     */       Object localObject3;
/*     */       Object localObject4;
/* 275 */       while (localIterator2.hasNext()) {
/* 276 */         execute((Runnable)localIterator2.next());
/* 277 */         long l3 = System.nanoTime();
/* 278 */         l1 -= l3 - l2;
/* 279 */         l2 = l3;
/* 280 */         if (l1 <= 0L) {
/* 281 */           localArrayList2 = localArrayList1;
/*     */ 
/* 303 */           if (i == 0)
/* 304 */             for (localObject3 = localArrayList1.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (Future)((Iterator)localObject3).next();
/* 305 */               ((Future)localObject4).cancel(true); }  return localArrayList2;
/*     */         }
/*     */       }
/* 284 */       for (Object localObject1 = localArrayList1.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Future)((Iterator)localObject1).next();
/* 285 */         if (!((Future)localObject2).isDone()) {
/* 286 */           if (l1 <= 0L) {
/* 287 */             localArrayList2 = localArrayList1;
/*     */ 
/* 303 */             if (i == 0)
/* 304 */               for (localObject3 = localArrayList1.iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (Future)((Iterator)localObject3).next();
/* 305 */                 ((Future)localObject4).cancel(true); }  return localArrayList2;
/*     */           }
/*     */           try
/*     */           {
/* 289 */             ((Future)localObject2).get(l1, TimeUnit.NANOSECONDS);
/*     */           } catch (CancellationException localCancellationException) {
/*     */           } catch (ExecutionException localExecutionException) {
/*     */           } catch (TimeoutException localTimeoutException) {
/* 293 */             localObject3 = localArrayList1;
/*     */ 
/* 303 */             if (i == 0)
/* 304 */               for (localObject4 = localArrayList1.iterator(); ((Iterator)localObject4).hasNext(); ) { Future localFuture2 = (Future)((Iterator)localObject4).next();
/* 305 */                 localFuture2.cancel(true); }  return localObject3;
/*     */           }
/* 295 */           long l4 = System.nanoTime();
/* 296 */           l1 -= l4 - l2;
/* 297 */           l2 = l4;
/*     */         }
/*     */       }
/*     */       Object localObject2;
/* 300 */       i = 1;
/* 301 */       localObject1 = localArrayList1;
/*     */ 
/* 303 */       if (i == 0)
/* 304 */         for (localObject2 = localArrayList1.iterator(); ((Iterator)localObject2).hasNext(); ) { Future localFuture1 = (Future)((Iterator)localObject2).next();
/* 305 */           localFuture1.cancel(true); }  return localObject1;
/*     */     }
/*     */     finally
/*     */     {
/* 303 */       if (i == 0)
/* 304 */         for (Future localFuture3 : localArrayList1)
/* 305 */           localFuture3.cancel(true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.AbstractExecutorService
 * JD-Core Version:    0.6.2
 */
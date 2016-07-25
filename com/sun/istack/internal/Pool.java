/*     */ package com.sun.istack.internal;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ 
/*     */ public abstract interface Pool<T>
/*     */ {
/*     */   @NotNull
/*     */   public abstract T take();
/*     */ 
/*     */   public abstract void recycle(@NotNull T paramT);
/*     */ 
/*     */   public static abstract class Impl<T>
/*     */     implements Pool<T>
/*     */   {
/*     */     private volatile WeakReference<ConcurrentLinkedQueue<T>> queue;
/*     */ 
/*     */     @NotNull
/*     */     public final T take()
/*     */     {
/*  74 */       Object t = getQueue().poll();
/*  75 */       if (t == null) {
/*  76 */         return create();
/*     */       }
/*  78 */       return t;
/*     */     }
/*     */ 
/*     */     public final void recycle(T t)
/*     */     {
/*  85 */       getQueue().offer(t);
/*     */     }
/*     */ 
/*     */     private ConcurrentLinkedQueue<T> getQueue() {
/*  89 */       WeakReference q = this.queue;
/*  90 */       if (q != null) {
/*  91 */         ConcurrentLinkedQueue d = (ConcurrentLinkedQueue)q.get();
/*  92 */         if (d != null) {
/*  93 */           return d;
/*     */         }
/*     */       }
/*     */ 
/*  97 */       ConcurrentLinkedQueue d = new ConcurrentLinkedQueue();
/*  98 */       this.queue = new WeakReference(d);
/*     */ 
/* 100 */       return d;
/*     */     }
/*     */ 
/*     */     @NotNull
/*     */     protected abstract T create();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.istack.internal.Pool
 * JD-Core Version:    0.6.2
 */
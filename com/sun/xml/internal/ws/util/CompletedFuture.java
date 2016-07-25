/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ public class CompletedFuture<T>
/*    */   implements Future<T>
/*    */ {
/*    */   private final T v;
/*    */   private final Throwable re;
/*    */ 
/*    */   public CompletedFuture(T v, Throwable re)
/*    */   {
/* 43 */     this.v = v;
/* 44 */     this.re = re;
/*    */   }
/*    */ 
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean isCancelled() {
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean isDone() {
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */   public T get() throws ExecutionException {
/* 60 */     if (this.re != null) {
/* 61 */       throw new ExecutionException(this.re);
/*    */     }
/* 63 */     return this.v;
/*    */   }
/*    */ 
/*    */   public T get(long timeout, TimeUnit unit) throws ExecutionException {
/* 67 */     return get();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.CompletedFuture
 * JD-Core Version:    0.6.2
 */
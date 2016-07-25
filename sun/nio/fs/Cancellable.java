/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ abstract class Cancellable
/*     */   implements Runnable
/*     */ {
/*  39 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private final long pollingAddress;
/*  42 */   private final Object lock = new Object();
/*     */   private boolean completed;
/*     */   private Throwable exception;
/*     */ 
/*     */   protected Cancellable()
/*     */   {
/*  49 */     this.pollingAddress = unsafe.allocateMemory(4L);
/*  50 */     unsafe.putIntVolatile(null, this.pollingAddress, 0);
/*     */   }
/*     */ 
/*     */   protected long addressToPollForCancel()
/*     */   {
/*  58 */     return this.pollingAddress;
/*     */   }
/*     */ 
/*     */   protected int cancelValue()
/*     */   {
/*  67 */     return 2147483647;
/*     */   }
/*     */ 
/*     */   final void cancel()
/*     */   {
/*  75 */     synchronized (this.lock) {
/*  76 */       if (!this.completed)
/*  77 */         unsafe.putIntVolatile(null, this.pollingAddress, cancelValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   private Throwable exception()
/*     */   {
/*  87 */     synchronized (this.lock) {
/*  88 */       return this.exception;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void run()
/*     */   {
/*     */     try {
/*  95 */       implRun();
/*     */     } catch (Throwable ) {
/*  97 */       synchronized (this.lock) {
/*  98 */         this.exception = ???;
/*     */       }
/*     */     } finally {
/* 101 */       synchronized (this.lock) {
/* 102 */         this.completed = true;
/* 103 */         unsafe.freeMemory(this.pollingAddress);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract void implRun()
/*     */     throws Throwable;
/*     */ 
/*     */   static void runInterruptibly(Cancellable paramCancellable)
/*     */     throws ExecutionException
/*     */   {
/* 120 */     Thread localThread = new Thread(paramCancellable);
/* 121 */     localThread.start();
/* 122 */     int i = 0;
/* 123 */     while (localThread.isAlive()) {
/*     */       try {
/* 125 */         localThread.join();
/*     */       } catch (InterruptedException localInterruptedException) {
/* 127 */         i = 1;
/* 128 */         paramCancellable.cancel();
/*     */       }
/*     */     }
/* 131 */     if (i != 0)
/* 132 */       Thread.currentThread().interrupt();
/* 133 */     Throwable localThrowable = paramCancellable.exception();
/* 134 */     if (localThrowable != null)
/* 135 */       throw new ExecutionException(localThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.Cancellable
 * JD-Core Version:    0.6.2
 */
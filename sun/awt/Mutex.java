/*    */ package sun.awt;
/*    */ 
/*    */ public class Mutex
/*    */ {
/*    */   private boolean locked;
/*    */   private Thread owner;
/*    */ 
/*    */   public synchronized void lock()
/*    */   {
/* 33 */     if ((this.locked) && (Thread.currentThread() == this.owner)) {
/* 34 */       throw new IllegalMonitorStateException();
/*    */     }
/*    */     do
/* 37 */       if (!this.locked) {
/* 38 */         this.locked = true;
/* 39 */         this.owner = Thread.currentThread();
/*    */       } else {
/*    */         try {
/* 42 */           wait();
/*    */         }
/*    */         catch (InterruptedException localInterruptedException) {
/*    */         }
/*    */       }
/* 47 */     while (this.owner != Thread.currentThread());
/*    */   }
/*    */ 
/*    */   public synchronized void unlock() {
/* 51 */     if (Thread.currentThread() != this.owner) {
/* 52 */       throw new IllegalMonitorStateException();
/*    */     }
/* 54 */     this.owner = null;
/* 55 */     this.locked = false;
/* 56 */     notify();
/*    */   }
/*    */ 
/*    */   protected boolean isOwned() {
/* 60 */     return (this.locked) && (Thread.currentThread() == this.owner);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.Mutex
 * JD-Core Version:    0.6.2
 */
/*    */ package sun.misc;
/*    */ 
/*    */ public final class ConditionLock extends Lock
/*    */ {
/* 43 */   private int state = 0;
/*    */ 
/*    */   public ConditionLock()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ConditionLock(int paramInt)
/*    */   {
/* 55 */     this.state = paramInt;
/*    */   }
/*    */ 
/*    */   public synchronized void lockWhen(int paramInt)
/*    */     throws InterruptedException
/*    */   {
/* 68 */     while (this.state != paramInt) {
/* 69 */       wait();
/*    */     }
/* 71 */     lock();
/*    */   }
/*    */ 
/*    */   public synchronized void unlockWith(int paramInt)
/*    */   {
/* 79 */     this.state = paramInt;
/* 80 */     unlock();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.ConditionLock
 * JD-Core Version:    0.6.2
 */
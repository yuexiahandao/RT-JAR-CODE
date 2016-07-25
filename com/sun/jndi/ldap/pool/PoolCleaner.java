/*    */ package com.sun.jndi.ldap.pool;
/*    */ 
/*    */ public final class PoolCleaner extends Thread
/*    */ {
/*    */   private final Pool[] pools;
/*    */   private final long period;
/*    */ 
/*    */   public PoolCleaner(long paramLong, Pool[] paramArrayOfPool)
/*    */   {
/* 43 */     this.period = paramLong;
/* 44 */     this.pools = ((Pool[])paramArrayOfPool.clone());
/* 45 */     setDaemon(true);
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     while (true)
/* 51 */       synchronized (this)
/*    */       {
/*    */         try {
/* 54 */           wait(this.period);
/*    */         }
/*    */         catch (InterruptedException localInterruptedException)
/*    */         {
/*    */         }
/* 59 */         long l = System.currentTimeMillis() - this.period;
/* 60 */         int i = 0; if (i < this.pools.length) {
/* 61 */           if (this.pools[i] != null)
/* 62 */             this.pools[i].expire(l);
/* 60 */           i++;
/*    */         }
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.pool.PoolCleaner
 * JD-Core Version:    0.6.2
 */
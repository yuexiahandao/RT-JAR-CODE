/*    */ package sun.jdbc.odbc.ee;
/*    */ 
/*    */ import sun.jdbc.odbc.JdbcOdbcTracer;
/*    */ 
/*    */ public class PoolWorker extends Thread
/*    */ {
/*    */   private ObjectPool pool;
/* 19 */   private final int NOTSTARTED = 0;
/* 20 */   private final int STARTED = 1;
/* 21 */   private final int STOPPED = 2;
/*    */ 
/* 24 */   private int state = 0;
/*    */ 
/*    */   public PoolWorker(ObjectPool paramObjectPool)
/*    */   {
/* 34 */     this.pool = paramObjectPool;
/*    */   }
/*    */ 
/*    */   public void start()
/*    */   {
/* 41 */     if (this.state == 0) {
/* 42 */       this.state = 1;
/* 43 */       super.start();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void release()
/*    */   {
/* 51 */     this.state = 2;
/* 52 */     this.pool.markError("Pool maintenance stopped. Pool is either shutdown or there is an error!");
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/* 59 */     while (this.state == 1)
/*    */       try {
/* 61 */         this.pool.getTracer().trace("Worker Thread : Maintenance of " + this.pool.getName() + "started");
/* 62 */         this.pool.maintain();
/*    */ 
/* 64 */         if (this.pool.getCurrentSize() == 0) {
/* 65 */           this.pool.shutDown(true);
/*    */         }
/* 67 */         this.pool.getTracer().trace("Worker Thread : Maintenance of " + this.pool.getName() + "completed");
/* 68 */         sleep(this.pool.getMaintenanceInterval() * 1000);
/*    */       } catch (InterruptedException localInterruptedException) {
/* 70 */         this.pool.markError("Maintenance Thread Interrupted : " + localInterruptedException.getMessage());
/*    */       } catch (Exception localException) {
/* 72 */         this.pool.markError("Maintenance Thread Error : " + localException.getMessage());
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.ee.PoolWorker
 * JD-Core Version:    0.6.2
 */
/*    */ package java.util.concurrent.locks;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public abstract class AbstractOwnableSynchronizer
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 3737899427754241961L;
/*    */   private transient Thread exclusiveOwnerThread;
/*    */ 
/*    */   protected final void setExclusiveOwnerThread(Thread paramThread)
/*    */   {
/* 73 */     this.exclusiveOwnerThread = paramThread;
/*    */   }
/*    */ 
/*    */   protected final Thread getExclusiveOwnerThread()
/*    */   {
/* 84 */     return this.exclusiveOwnerThread;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.AbstractOwnableSynchronizer
 * JD-Core Version:    0.6.2
 */
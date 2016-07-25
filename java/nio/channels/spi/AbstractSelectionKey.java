/*    */ package java.nio.channels.spi;
/*    */ 
/*    */ import java.nio.channels.SelectionKey;
/*    */ 
/*    */ public abstract class AbstractSelectionKey extends SelectionKey
/*    */ {
/* 50 */   private volatile boolean valid = true;
/*    */ 
/*    */   public final boolean isValid() {
/* 53 */     return this.valid;
/*    */   }
/*    */ 
/*    */   void invalidate() {
/* 57 */     this.valid = false;
/*    */   }
/*    */ 
/*    */   public final void cancel()
/*    */   {
/* 70 */     synchronized (this) {
/* 71 */       if (this.valid) {
/* 72 */         this.valid = false;
/* 73 */         ((AbstractSelector)selector()).cancel(this);
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.channels.spi.AbstractSelectionKey
 * JD-Core Version:    0.6.2
 */
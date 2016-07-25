/*     */ package sun.awt.windows;
/*     */ 
/*     */ import sun.awt.Mutex;
/*     */ import sun.awt.datatransfer.ToolkitThreadBlockedHandler;
/*     */ 
/*     */ final class WToolkitThreadBlockedHandler extends Mutex
/*     */   implements ToolkitThreadBlockedHandler
/*     */ {
/*     */   public void enter()
/*     */   {
/* 469 */     if (!isOwned()) {
/* 470 */       throw new IllegalMonitorStateException();
/*     */     }
/* 472 */     unlock();
/* 473 */     startSecondaryEventLoop();
/* 474 */     lock();
/*     */   }
/*     */ 
/*     */   public void exit() {
/* 478 */     if (!isOwned()) {
/* 479 */       throw new IllegalMonitorStateException();
/*     */     }
/* 481 */     WToolkit.quitSecondaryEventLoop();
/*     */   }
/*     */ 
/*     */   private native void startSecondaryEventLoop();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WToolkitThreadBlockedHandler
 * JD-Core Version:    0.6.2
 */
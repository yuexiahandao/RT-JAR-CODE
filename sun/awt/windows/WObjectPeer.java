/*    */ package sun.awt.windows;
/*    */ 
/*    */ abstract class WObjectPeer
/*    */ {
/*    */   long pData;
/* 36 */   boolean destroyed = false;
/*    */   Object target;
/*    */   private volatile boolean disposed;
/* 43 */   protected Error createError = null;
/*    */ 
/* 46 */   private final Object stateLock = new Object();
/*    */ 
/*    */   public static WObjectPeer getPeerForTarget(Object paramObject) {
/* 49 */     WObjectPeer localWObjectPeer = (WObjectPeer)WToolkit.targetToPeer(paramObject);
/* 50 */     return localWObjectPeer;
/*    */   }
/*    */ 
/*    */   public long getData() {
/* 54 */     return this.pData;
/*    */   }
/*    */ 
/*    */   public Object getTarget() {
/* 58 */     return this.target;
/*    */   }
/*    */ 
/*    */   public final Object getStateLock() {
/* 62 */     return this.stateLock;
/*    */   }
/*    */ 
/*    */   protected abstract void disposeImpl();
/*    */ 
/*    */   public final void dispose()
/*    */   {
/* 71 */     int i = 0;
/*    */ 
/* 73 */     synchronized (this) {
/* 74 */       if (!this.disposed) {
/* 75 */         this.disposed = (i = 1);
/*    */       }
/*    */     }
/*    */ 
/* 79 */     if (i != 0)
/* 80 */       disposeImpl();
/*    */   }
/*    */ 
/*    */   protected final boolean isDisposed() {
/* 84 */     return this.disposed;
/*    */   }
/*    */ 
/*    */   private static native void initIDs();
/*    */ 
/*    */   static
/*    */   {
/* 30 */     initIDs();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WObjectPeer
 * JD-Core Version:    0.6.2
 */
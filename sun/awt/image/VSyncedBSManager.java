/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.BufferStrategy;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public abstract class VSyncedBSManager
/*     */ {
/*     */   private static VSyncedBSManager theInstance;
/*  38 */   private static final boolean vSyncLimit = Boolean.valueOf((String)AccessController.doPrivileged(new GetPropertyAction("sun.java2d.vsynclimit", "true"))).booleanValue();
/*     */ 
/*     */   private static VSyncedBSManager getInstance(boolean paramBoolean)
/*     */   {
/*  44 */     if ((theInstance == null) && (paramBoolean)) {
/*  45 */       theInstance = vSyncLimit ? new SingleVSyncedBSMgr(null) : new NoLimitVSyncBSMgr(null);
/*     */     }
/*     */ 
/*  48 */     return theInstance;
/*     */   }
/*     */ 
/*     */   abstract boolean checkAllowed(BufferStrategy paramBufferStrategy);
/*     */ 
/*     */   abstract void relinquishVsync(BufferStrategy paramBufferStrategy);
/*     */ 
/*     */   public static boolean vsyncAllowed(BufferStrategy paramBufferStrategy)
/*     */   {
/*  61 */     VSyncedBSManager localVSyncedBSManager = getInstance(true);
/*  62 */     return localVSyncedBSManager.checkAllowed(paramBufferStrategy);
/*     */   }
/*     */ 
/*     */   public static synchronized void releaseVsync(BufferStrategy paramBufferStrategy)
/*     */   {
/*  70 */     VSyncedBSManager localVSyncedBSManager = getInstance(false);
/*  71 */     if (localVSyncedBSManager != null)
/*  72 */       localVSyncedBSManager.relinquishVsync(paramBufferStrategy);
/*     */   }
/*     */ 
/*     */   private static final class NoLimitVSyncBSMgr extends VSyncedBSManager
/*     */   {
/*     */     boolean checkAllowed(BufferStrategy paramBufferStrategy)
/*     */     {
/*  83 */       return true;
/*     */     }
/*     */ 
/*     */     void relinquishVsync(BufferStrategy paramBufferStrategy)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class SingleVSyncedBSMgr extends VSyncedBSManager
/*     */   {
/*     */     private WeakReference<BufferStrategy> strategy;
/*     */ 
/*     */     public synchronized boolean checkAllowed(BufferStrategy paramBufferStrategy)
/*     */     {
/* 100 */       if (this.strategy != null) {
/* 101 */         BufferStrategy localBufferStrategy = (BufferStrategy)this.strategy.get();
/* 102 */         if (localBufferStrategy != null) {
/* 103 */           return localBufferStrategy == paramBufferStrategy;
/*     */         }
/*     */       }
/* 106 */       this.strategy = new WeakReference(paramBufferStrategy);
/* 107 */       return true;
/*     */     }
/*     */ 
/*     */     public synchronized void relinquishVsync(BufferStrategy paramBufferStrategy)
/*     */     {
/* 112 */       if (this.strategy != null) {
/* 113 */         BufferStrategy localBufferStrategy = (BufferStrategy)this.strategy.get();
/* 114 */         if (localBufferStrategy == paramBufferStrategy) {
/* 115 */           this.strategy.clear();
/* 116 */           this.strategy = null;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.VSyncedBSManager
 * JD-Core Version:    0.6.2
 */
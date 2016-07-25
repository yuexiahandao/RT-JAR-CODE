/*     */ package sun.java2d.d3d;
/*     */ 
/*     */ import java.util.Set;
/*     */ import sun.java2d.ScreenUpdateManager;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ import sun.java2d.pipe.RenderQueue;
/*     */ 
/*     */ public class D3DRenderQueue extends RenderQueue
/*     */ {
/*     */   private static D3DRenderQueue theInstance;
/*     */   private static Thread rqThread;
/*     */ 
/*     */   public static synchronized D3DRenderQueue getInstance()
/*     */   {
/*  50 */     if (theInstance == null) {
/*  51 */       theInstance = new D3DRenderQueue();
/*     */ 
/*  53 */       theInstance.flushAndInvokeNow(new Runnable() {
/*     */         public void run() {
/*  55 */           D3DRenderQueue.access$002(Thread.currentThread());
/*     */         }
/*     */       });
/*     */     }
/*  59 */     return theInstance;
/*     */   }
/*     */ 
/*     */   public static void sync()
/*     */   {
/*  72 */     if (theInstance != null)
/*     */     {
/*  75 */       D3DScreenUpdateManager localD3DScreenUpdateManager = (D3DScreenUpdateManager)ScreenUpdateManager.getInstance();
/*     */ 
/*  77 */       localD3DScreenUpdateManager.runUpdateNow();
/*     */ 
/*  79 */       theInstance.lock();
/*     */       try {
/*  81 */         theInstance.ensureCapacity(4);
/*  82 */         theInstance.getBuffer().putInt(76);
/*  83 */         theInstance.flushNow();
/*     */       } finally {
/*  85 */         theInstance.unlock();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void restoreDevices()
/*     */   {
/*  95 */     D3DRenderQueue localD3DRenderQueue = getInstance();
/*  96 */     localD3DRenderQueue.lock();
/*     */     try {
/*  98 */       localD3DRenderQueue.ensureCapacity(4);
/*  99 */       localD3DRenderQueue.getBuffer().putInt(77);
/* 100 */       localD3DRenderQueue.flushNow();
/*     */     } finally {
/* 102 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isRenderQueueThread()
/*     */   {
/* 111 */     return Thread.currentThread() == rqThread;
/*     */   }
/*     */ 
/*     */   public static void disposeGraphicsConfig(long paramLong)
/*     */   {
/* 119 */     D3DRenderQueue localD3DRenderQueue = getInstance();
/* 120 */     localD3DRenderQueue.lock();
/*     */     try
/*     */     {
/* 123 */       RenderBuffer localRenderBuffer = localD3DRenderQueue.getBuffer();
/* 124 */       localD3DRenderQueue.ensureCapacityAndAlignment(12, 4);
/* 125 */       localRenderBuffer.putInt(74);
/* 126 */       localRenderBuffer.putLong(paramLong);
/*     */ 
/* 129 */       localD3DRenderQueue.flushNow();
/*     */     } finally {
/* 131 */       localD3DRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flushNow()
/*     */   {
/* 137 */     flushBuffer(null);
/*     */   }
/*     */ 
/*     */   public void flushAndInvokeNow(Runnable paramRunnable)
/*     */   {
/* 142 */     flushBuffer(paramRunnable);
/*     */   }
/*     */ 
/*     */   private native void flushBuffer(long paramLong, int paramInt, Runnable paramRunnable);
/*     */ 
/*     */   private void flushBuffer(Runnable paramRunnable)
/*     */   {
/* 149 */     int i = this.buf.position();
/* 150 */     if ((i > 0) || (paramRunnable != null))
/*     */     {
/* 152 */       flushBuffer(this.buf.getAddress(), i, paramRunnable);
/*     */     }
/*     */ 
/* 155 */     this.buf.clear();
/*     */ 
/* 157 */     this.refSet.clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.d3d.D3DRenderQueue
 * JD-Core Version:    0.6.2
 */
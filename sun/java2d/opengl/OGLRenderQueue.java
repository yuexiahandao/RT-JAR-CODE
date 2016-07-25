/*     */ package sun.java2d.opengl;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Set;
/*     */ import sun.java2d.pipe.RenderBuffer;
/*     */ import sun.java2d.pipe.RenderQueue;
/*     */ import sun.misc.ThreadGroupUtils;
/*     */ 
/*     */ public class OGLRenderQueue extends RenderQueue
/*     */ {
/*     */   private static OGLRenderQueue theInstance;
/*     */   private final QueueFlusher flusher;
/*     */ 
/*     */   private OGLRenderQueue()
/*     */   {
/*  51 */     this.flusher = ((QueueFlusher)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public OGLRenderQueue.QueueFlusher run() {
/*  54 */         return new OGLRenderQueue.QueueFlusher(OGLRenderQueue.this, ThreadGroupUtils.getRootThreadGroup());
/*     */       }
/*     */     }));
/*     */   }
/*     */ 
/*     */   public static synchronized OGLRenderQueue getInstance()
/*     */   {
/*  65 */     if (theInstance == null) {
/*  66 */       theInstance = new OGLRenderQueue();
/*     */     }
/*  68 */     return theInstance;
/*     */   }
/*     */ 
/*     */   public static void sync()
/*     */   {
/*  81 */     if (theInstance != null) {
/*  82 */       theInstance.lock();
/*     */       try {
/*  84 */         theInstance.ensureCapacity(4);
/*  85 */         theInstance.getBuffer().putInt(76);
/*  86 */         theInstance.flushNow();
/*     */       } finally {
/*  88 */         theInstance.unlock();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void disposeGraphicsConfig(long paramLong)
/*     */   {
/*  98 */     OGLRenderQueue localOGLRenderQueue = getInstance();
/*  99 */     localOGLRenderQueue.lock();
/*     */     try
/*     */     {
/* 103 */       OGLContext.setScratchSurface(paramLong);
/*     */ 
/* 105 */       RenderBuffer localRenderBuffer = localOGLRenderQueue.getBuffer();
/* 106 */       localOGLRenderQueue.ensureCapacityAndAlignment(12, 4);
/* 107 */       localRenderBuffer.putInt(74);
/* 108 */       localRenderBuffer.putLong(paramLong);
/*     */ 
/* 111 */       localOGLRenderQueue.flushNow();
/*     */     } finally {
/* 113 */       localOGLRenderQueue.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isQueueFlusherThread()
/*     */   {
/* 121 */     return Thread.currentThread() == getInstance().flusher;
/*     */   }
/*     */ 
/*     */   public void flushNow()
/*     */   {
/*     */     try {
/* 127 */       this.flusher.flushNow();
/*     */     } catch (Exception localException) {
/* 129 */       System.err.println("exception in flushNow:");
/* 130 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flushAndInvokeNow(Runnable paramRunnable)
/*     */   {
/*     */     try {
/* 137 */       this.flusher.flushAndInvokeNow(paramRunnable);
/*     */     } catch (Exception localException) {
/* 139 */       System.err.println("exception in flushAndInvokeNow:");
/* 140 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private native void flushBuffer(long paramLong, int paramInt);
/*     */ 
/*     */   private void flushBuffer()
/*     */   {
/* 148 */     int i = this.buf.position();
/* 149 */     if (i > 0)
/*     */     {
/* 151 */       flushBuffer(this.buf.getAddress(), i);
/*     */     }
/*     */ 
/* 154 */     this.buf.clear();
/*     */ 
/* 156 */     this.refSet.clear();
/*     */   }
/*     */   private class QueueFlusher extends Thread {
/*     */     private boolean needsFlush;
/*     */     private Runnable task;
/*     */     private Error error;
/*     */ 
/* 165 */     public QueueFlusher(ThreadGroup arg2) { super("Java2D Queue Flusher");
/* 166 */       setDaemon(true);
/* 167 */       setPriority(10);
/* 168 */       start();
/*     */     }
/*     */ 
/*     */     public synchronized void flushNow()
/*     */     {
/* 173 */       this.needsFlush = true;
/* 174 */       notify();
/*     */ 
/* 177 */       while (this.needsFlush) {
/*     */         try {
/* 179 */           wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException)
/*     */         {
/*     */         }
/*     */       }
/* 185 */       if (this.error != null)
/* 186 */         throw this.error;
/*     */     }
/*     */ 
/*     */     public synchronized void flushAndInvokeNow(Runnable paramRunnable)
/*     */     {
/* 191 */       this.task = paramRunnable;
/* 192 */       flushNow();
/*     */     }
/*     */ 
/*     */     public synchronized void run() {
/* 196 */       boolean bool = false;
/*     */       while (true)
/* 198 */         if (!this.needsFlush)
/*     */           try {
/* 200 */             bool = false;
/*     */ 
/* 206 */             wait(100L);
/*     */ 
/* 215 */             if ((!this.needsFlush) && ((bool = OGLRenderQueue.this.tryLock())))
/* 216 */               if (OGLRenderQueue.this.buf.position() > 0)
/* 217 */                 this.needsFlush = true;
/*     */               else
/* 219 */                 OGLRenderQueue.this.unlock();
/*     */           }
/*     */           catch (InterruptedException localInterruptedException)
/*     */           {
/*     */           }
/*     */         else
/*     */           try
/*     */           {
/* 227 */             this.error = null;
/*     */ 
/* 229 */             OGLRenderQueue.this.flushBuffer();
/*     */ 
/* 231 */             if (this.task != null)
/* 232 */               this.task.run();
/*     */           }
/*     */           catch (Error localError) {
/* 235 */             this.error = localError;
/*     */           } catch (Exception localException) {
/* 237 */             System.err.println("exception in QueueFlusher:");
/* 238 */             localException.printStackTrace();
/*     */           } finally {
/* 240 */             if (bool) {
/* 241 */               OGLRenderQueue.this.unlock();
/*     */             }
/* 243 */             this.task = null;
/*     */ 
/* 245 */             this.needsFlush = false;
/* 246 */             notify();
/*     */           }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.opengl.OGLRenderQueue
 * JD-Core Version:    0.6.2
 */
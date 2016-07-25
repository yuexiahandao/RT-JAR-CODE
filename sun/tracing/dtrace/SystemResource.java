/*     */ package sun.tracing.dtrace;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ class SystemResource extends WeakReference<Activation>
/*     */ {
/*     */   private long handle;
/*  81 */   private static ReferenceQueue<Activation> referenceQueue = SystemResource.referenceQueue = new ReferenceQueue();
/*     */ 
/*  83 */   static HashSet<SystemResource> resources = new HashSet();
/*     */ 
/*     */   SystemResource(Activation paramActivation, long paramLong) {
/*  86 */     super(paramActivation, referenceQueue);
/*  87 */     this.handle = paramLong;
/*  88 */     flush();
/*  89 */     resources.add(this);
/*     */   }
/*     */ 
/*     */   void dispose() {
/*  93 */     JVM.dispose(this.handle);
/*  94 */     resources.remove(this);
/*  95 */     this.handle = 0L;
/*     */   }
/*     */ 
/*     */   static void flush() {
/*  99 */     SystemResource localSystemResource = null;
/* 100 */     while ((localSystemResource = (SystemResource)referenceQueue.poll()) != null)
/* 101 */       if (localSystemResource.handle != 0L)
/* 102 */         localSystemResource.dispose();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tracing.dtrace.SystemResource
 * JD-Core Version:    0.6.2
 */
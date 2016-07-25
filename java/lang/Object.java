/*     */ package java.lang;
/*     */ 
/*     */ public class Object
/*     */ {
/*     */   private static native void registerNatives();
/*     */ 
/*     */   public final native Class<?> getClass();
/*     */ 
/*     */   public native int hashCode();
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 150 */     return this == paramObject;
/*     */   }
/*     */ 
/*     */   protected native Object clone()
/*     */     throws CloneNotSupportedException;
/*     */ 
/*     */   public String toString()
/*     */   {
/* 237 */     return getClass().getName() + "@" + Integer.toHexString(hashCode());
/*     */   }
/*     */ 
/*     */   public final native void notify();
/*     */ 
/*     */   public final native void notifyAll();
/*     */ 
/*     */   public final native void wait(long paramLong)
/*     */     throws InterruptedException;
/*     */ 
/*     */   public final void wait(long paramLong, int paramInt)
/*     */     throws InterruptedException
/*     */   {
/* 448 */     if (paramLong < 0L) {
/* 449 */       throw new IllegalArgumentException("timeout value is negative");
/*     */     }
/*     */ 
/* 452 */     if ((paramInt < 0) || (paramInt > 999999)) {
/* 453 */       throw new IllegalArgumentException("nanosecond timeout value out of range");
/*     */     }
/*     */ 
/* 457 */     if ((paramInt >= 500000) || ((paramInt != 0) && (paramLong == 0L))) {
/* 458 */       paramLong += 1L;
/*     */     }
/*     */ 
/* 461 */     wait(paramLong);
/*     */   }
/*     */ 
/*     */   public final void wait()
/*     */     throws InterruptedException
/*     */   {
/* 503 */     wait(0L);
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  41 */     registerNatives();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Object
 * JD-Core Version:    0.6.2
 */
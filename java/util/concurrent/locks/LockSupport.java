/*     */ package java.util.concurrent.locks;
/*     */ 
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class LockSupport
/*     */ {
/* 124 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final long parkBlockerOffset;
/*     */ 
/*     */   private static void setBlocker(Thread paramThread, Object paramObject)
/*     */   {
/* 136 */     unsafe.putObject(paramThread, parkBlockerOffset, paramObject);
/*     */   }
/*     */ 
/*     */   public static void unpark(Thread paramThread)
/*     */   {
/* 151 */     if (paramThread != null)
/* 152 */       unsafe.unpark(paramThread);
/*     */   }
/*     */ 
/*     */   public static void park(Object paramObject)
/*     */   {
/* 184 */     Thread localThread = Thread.currentThread();
/* 185 */     setBlocker(localThread, paramObject);
/* 186 */     unsafe.park(false, 0L);
/* 187 */     setBlocker(localThread, null);
/*     */   }
/*     */ 
/*     */   public static void parkNanos(Object paramObject, long paramLong)
/*     */   {
/* 223 */     if (paramLong > 0L) {
/* 224 */       Thread localThread = Thread.currentThread();
/* 225 */       setBlocker(localThread, paramObject);
/* 226 */       unsafe.park(false, paramLong);
/* 227 */       setBlocker(localThread, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void parkUntil(Object paramObject, long paramLong)
/*     */   {
/* 265 */     Thread localThread = Thread.currentThread();
/* 266 */     setBlocker(localThread, paramObject);
/* 267 */     unsafe.park(true, paramLong);
/* 268 */     setBlocker(localThread, null);
/*     */   }
/*     */ 
/*     */   public static Object getBlocker(Thread paramThread)
/*     */   {
/* 284 */     if (paramThread == null)
/* 285 */       throw new NullPointerException();
/* 286 */     return unsafe.getObjectVolatile(paramThread, parkBlockerOffset);
/*     */   }
/*     */ 
/*     */   public static void park()
/*     */   {
/* 315 */     unsafe.park(false, 0L);
/*     */   }
/*     */ 
/*     */   public static void parkNanos(long paramLong)
/*     */   {
/* 348 */     if (paramLong > 0L)
/* 349 */       unsafe.park(false, paramLong);
/*     */   }
/*     */ 
/*     */   public static void parkUntil(long paramLong)
/*     */   {
/* 383 */     unsafe.park(true, paramLong);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 129 */       parkBlockerOffset = unsafe.objectFieldOffset(Thread.class.getDeclaredField("parkBlocker"));
/*     */     } catch (Exception localException) {
/* 131 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.locks.LockSupport
 * JD-Core Version:    0.6.2
 */
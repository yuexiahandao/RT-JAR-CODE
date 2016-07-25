/*     */ package java.util.concurrent;
/*     */ 
/*     */ public enum TimeUnit
/*     */ {
/*  72 */   NANOSECONDS, 
/*     */ 
/*  83 */   MICROSECONDS, 
/*     */ 
/*  94 */   MILLISECONDS, 
/*     */ 
/* 105 */   SECONDS, 
/*     */ 
/* 116 */   MINUTES, 
/*     */ 
/* 127 */   HOURS, 
/*     */ 
/* 138 */   DAYS;
/*     */ 
/*     */   static final long C0 = 1L;
/*     */   static final long C1 = 1000L;
/*     */   static final long C2 = 1000000L;
/*     */   static final long C3 = 1000000000L;
/*     */   static final long C4 = 60000000000L;
/*     */   static final long C5 = 3600000000000L;
/*     */   static final long C6 = 86400000000000L;
/*     */   static final long MAX = 9223372036854775807L;
/*     */ 
/*     */   static long x(long paramLong1, long paramLong2, long paramLong3)
/*     */   {
/* 166 */     if (paramLong1 > paramLong3) return 9223372036854775807L;
/* 167 */     if (paramLong1 < -paramLong3) return -9223372036854775808L;
/* 168 */     return paramLong1 * paramLong2;
/*     */   }
/*     */ 
/*     */   public long convert(long paramLong, TimeUnit paramTimeUnit)
/*     */   {
/* 196 */     throw new AbstractMethodError();
/*     */   }
/*     */ 
/*     */   public long toNanos(long paramLong)
/*     */   {
/* 208 */     throw new AbstractMethodError();
/*     */   }
/*     */ 
/*     */   public long toMicros(long paramLong)
/*     */   {
/* 220 */     throw new AbstractMethodError();
/*     */   }
/*     */ 
/*     */   public long toMillis(long paramLong)
/*     */   {
/* 232 */     throw new AbstractMethodError();
/*     */   }
/*     */ 
/*     */   public long toSeconds(long paramLong)
/*     */   {
/* 244 */     throw new AbstractMethodError();
/*     */   }
/*     */ 
/*     */   public long toMinutes(long paramLong)
/*     */   {
/* 257 */     throw new AbstractMethodError();
/*     */   }
/*     */ 
/*     */   public long toHours(long paramLong)
/*     */   {
/* 270 */     throw new AbstractMethodError();
/*     */   }
/*     */ 
/*     */   public long toDays(long paramLong)
/*     */   {
/* 281 */     throw new AbstractMethodError();
/*     */   }
/*     */ 
/*     */   abstract int excessNanos(long paramLong1, long paramLong2);
/*     */ 
/*     */   public void timedWait(Object paramObject, long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 319 */     if (paramLong > 0L) {
/* 320 */       long l = toMillis(paramLong);
/* 321 */       int i = excessNanos(paramLong, l);
/* 322 */       paramObject.wait(l, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void timedJoin(Thread paramThread, long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 339 */     if (paramLong > 0L) {
/* 340 */       long l = toMillis(paramLong);
/* 341 */       int i = excessNanos(paramLong, l);
/* 342 */       paramThread.join(l, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void sleep(long paramLong)
/*     */     throws InterruptedException
/*     */   {
/* 357 */     if (paramLong > 0L) {
/* 358 */       long l = toMillis(paramLong);
/* 359 */       int i = excessNanos(paramLong, l);
/* 360 */       Thread.sleep(l, i);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.TimeUnit
 * JD-Core Version:    0.6.2
 */
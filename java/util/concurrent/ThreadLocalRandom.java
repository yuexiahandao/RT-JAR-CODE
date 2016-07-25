/*     */ package java.util.concurrent;
/*     */ 
/*     */ import java.util.Random;
/*     */ 
/*     */ public class ThreadLocalRandom extends Random
/*     */ {
/*     */   private static final long multiplier = 25214903917L;
/*     */   private static final long addend = 11L;
/*     */   private static final long mask = 281474976710655L;
/*     */   private long rnd;
/*     */   boolean initialized;
/*     */   private long pad0;
/*     */   private long pad1;
/*     */   private long pad2;
/*     */   private long pad3;
/*     */   private long pad4;
/*     */   private long pad5;
/*     */   private long pad6;
/*     */   private long pad7;
/*  91 */   private static final ThreadLocal<ThreadLocalRandom> localRandom = new ThreadLocal()
/*     */   {
/*     */     protected ThreadLocalRandom initialValue() {
/*  94 */       return new ThreadLocalRandom();
/*     */     }
/*  91 */   };
/*     */   private static final long serialVersionUID = -5851777807851030925L;
/*     */ 
/*     */   ThreadLocalRandom()
/*     */   {
/* 104 */     this.initialized = true;
/*     */   }
/*     */ 
/*     */   public static ThreadLocalRandom current()
/*     */   {
/* 113 */     return (ThreadLocalRandom)localRandom.get();
/*     */   }
/*     */ 
/*     */   public void setSeed(long paramLong)
/*     */   {
/* 123 */     if (this.initialized)
/* 124 */       throw new UnsupportedOperationException();
/* 125 */     this.rnd = ((paramLong ^ 0xDEECE66D) & 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   protected int next(int paramInt) {
/* 129 */     this.rnd = (this.rnd * 25214903917L + 11L & 0xFFFFFFFF);
/* 130 */     return (int)(this.rnd >>> 48 - paramInt);
/*     */   }
/*     */ 
/*     */   public int nextInt(int paramInt1, int paramInt2)
/*     */   {
/* 144 */     if (paramInt1 >= paramInt2)
/* 145 */       throw new IllegalArgumentException();
/* 146 */     return nextInt(paramInt2 - paramInt1) + paramInt1;
/*     */   }
/*     */ 
/*     */   public long nextLong(long paramLong)
/*     */   {
/* 159 */     if (paramLong <= 0L) {
/* 160 */       throw new IllegalArgumentException("n must be positive");
/*     */     }
/*     */ 
/* 166 */     long l1 = 0L;
/* 167 */     while (paramLong >= 2147483647L) {
/* 168 */       int i = next(2);
/* 169 */       long l2 = paramLong >>> 1;
/* 170 */       long l3 = (i & 0x2) == 0 ? l2 : paramLong - l2;
/* 171 */       if ((i & 0x1) == 0)
/* 172 */         l1 += paramLong - l3;
/* 173 */       paramLong = l3;
/*     */     }
/* 175 */     return l1 + nextInt((int)paramLong);
/*     */   }
/*     */ 
/*     */   public long nextLong(long paramLong1, long paramLong2)
/*     */   {
/* 189 */     if (paramLong1 >= paramLong2)
/* 190 */       throw new IllegalArgumentException();
/* 191 */     return nextLong(paramLong2 - paramLong1) + paramLong1;
/*     */   }
/*     */ 
/*     */   public double nextDouble(double paramDouble)
/*     */   {
/* 204 */     if (paramDouble <= 0.0D)
/* 205 */       throw new IllegalArgumentException("n must be positive");
/* 206 */     return nextDouble() * paramDouble;
/*     */   }
/*     */ 
/*     */   public double nextDouble(double paramDouble1, double paramDouble2)
/*     */   {
/* 220 */     if (paramDouble1 >= paramDouble2)
/* 221 */       throw new IllegalArgumentException();
/* 222 */     return nextDouble() * (paramDouble2 - paramDouble1) + paramDouble1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.ThreadLocalRandom
 * JD-Core Version:    0.6.2
 */
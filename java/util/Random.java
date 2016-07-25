/*     */ package java.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.io.Serializable;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class Random
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 3905348978240129619L;
/*     */   private final AtomicLong seed;
/*     */   private static final long multiplier = 25214903917L;
/*     */   private static final long addend = 11L;
/*     */   private static final long mask = 281474976710655L;
/* 104 */   private static final AtomicLong seedUniquifier = new AtomicLong(8682522807148012L);
/*     */   private double nextNextGaussian;
/* 449 */   private boolean haveNextNextGaussian = false;
/*     */ 
/* 525 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("seed", Long.TYPE), new ObjectStreamField("nextNextGaussian", Double.TYPE), new ObjectStreamField("haveNextNextGaussian", Boolean.TYPE) };
/*     */ 
/* 570 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final long seedOffset;
/*     */ 
/*     */   public Random()
/*     */   {
/*  90 */     this(seedUniquifier() ^ System.nanoTime());
/*     */   }
/*     */ 
/*     */   private static long seedUniquifier()
/*     */   {
/*     */     while (true)
/*     */     {
/*  97 */       long l1 = seedUniquifier.get();
/*  98 */       long l2 = l1 * 181783497276652981L;
/*  99 */       if (seedUniquifier.compareAndSet(l1, l2))
/* 100 */         return l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Random(long paramLong)
/*     */   {
/* 121 */     if (getClass() == Random.class) {
/* 122 */       this.seed = new AtomicLong(initialScramble(paramLong));
/*     */     }
/*     */     else {
/* 125 */       this.seed = new AtomicLong();
/* 126 */       setSeed(paramLong);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static long initialScramble(long paramLong) {
/* 131 */     return (paramLong ^ 0xDEECE66D) & 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   public synchronized void setSeed(long paramLong)
/*     */   {
/* 154 */     this.seed.set(initialScramble(paramLong));
/* 155 */     this.haveNextNextGaussian = false;
/*     */   }
/*     */ 
/*     */   protected int next(int paramInt)
/*     */   {
/* 185 */     AtomicLong localAtomicLong = this.seed;
/*     */     long l1;
/*     */     long l2;
/*     */     do
/*     */     {
/* 187 */       l1 = localAtomicLong.get();
/* 188 */       l2 = l1 * 25214903917L + 11L & 0xFFFFFFFF;
/* 189 */     }while (!localAtomicLong.compareAndSet(l1, l2));
/* 190 */     return (int)(l2 >>> 48 - paramInt);
/*     */   }
/*     */ 
/*     */   public void nextBytes(byte[] paramArrayOfByte)
/*     */   {
/* 213 */     int i = 0; for (int j = paramArrayOfByte.length; i < j; ) {
/* 214 */       int k = nextInt();
/* 215 */       int m = Math.min(j - i, 4);
/* 216 */       for (; m-- > 0; k >>= 8)
/* 217 */         paramArrayOfByte[(i++)] = ((byte)k);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int nextInt()
/*     */   {
/* 239 */     return next(32);
/*     */   }
/*     */ 
/*     */   public int nextInt(int paramInt)
/*     */   {
/* 299 */     if (paramInt <= 0) {
/* 300 */       throw new IllegalArgumentException("n must be positive");
/*     */     }
/* 302 */     if ((paramInt & -paramInt) == paramInt)
/* 303 */       return (int)(paramInt * next(31) >> 31); int i;
/*     */     int j;
/*     */     do {
/* 307 */       i = next(31);
/* 308 */       j = i % paramInt;
/* 309 */     }while (i - j + (paramInt - 1) < 0);
/* 310 */     return j;
/*     */   }
/*     */ 
/*     */   public long nextLong()
/*     */   {
/* 334 */     return (next(32) << 32) + next(32);
/*     */   }
/*     */ 
/*     */   public boolean nextBoolean()
/*     */   {
/* 358 */     return next(1) != 0;
/*     */   }
/*     */ 
/*     */   public float nextFloat()
/*     */   {
/* 401 */     return next(24) / 16777216.0F;
/*     */   }
/*     */ 
/*     */   public double nextDouble()
/*     */   {
/* 444 */     return ((next(26) << 27) + next(27)) / 9007199254740992.0D;
/*     */   }
/*     */ 
/*     */   public synchronized double nextGaussian()
/*     */   {
/* 498 */     if (this.haveNextNextGaussian) {
/* 499 */       this.haveNextNextGaussian = false;
/* 500 */       return this.nextNextGaussian; } double d1;
/*     */     double d2;
/*     */     double d3;
/*     */     do { d1 = 2.0D * nextDouble() - 1.0D;
/* 505 */       d2 = 2.0D * nextDouble() - 1.0D;
/* 506 */       d3 = d1 * d1 + d2 * d2; }
/* 507 */     while ((d3 >= 1.0D) || (d3 == 0.0D));
/* 508 */     double d4 = StrictMath.sqrt(-2.0D * StrictMath.log(d3) / d3);
/* 509 */     this.nextNextGaussian = (d2 * d4);
/* 510 */     this.haveNextNextGaussian = true;
/* 511 */     return d1 * d4;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 538 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */ 
/* 542 */     long l = localGetField.get("seed", -1L);
/* 543 */     if (l < 0L) {
/* 544 */       throw new StreamCorruptedException("Random: invalid seed");
/*     */     }
/* 546 */     resetSeed(l);
/* 547 */     this.nextNextGaussian = localGetField.get("nextNextGaussian", 0.0D);
/* 548 */     this.haveNextNextGaussian = localGetField.get("haveNextNextGaussian", false);
/*     */   }
/*     */ 
/*     */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 558 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/*     */ 
/* 561 */     localPutField.put("seed", this.seed.get());
/* 562 */     localPutField.put("nextNextGaussian", this.nextNextGaussian);
/* 563 */     localPutField.put("haveNextNextGaussian", this.haveNextNextGaussian);
/*     */ 
/* 566 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void resetSeed(long paramLong)
/*     */   {
/* 579 */     unsafe.putObjectVolatile(this, seedOffset, new AtomicLong(paramLong));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 574 */       seedOffset = unsafe.objectFieldOffset(Random.class.getDeclaredField("seed"));
/*     */     } catch (Exception localException) {
/* 576 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Random
 * JD-Core Version:    0.6.2
 */
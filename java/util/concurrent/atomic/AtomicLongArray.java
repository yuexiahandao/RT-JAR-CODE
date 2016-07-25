/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AtomicLongArray
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2308431214976778248L;
/*  50 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*  51 */   private static final int base = unsafe.arrayBaseOffset([J.class);
/*     */ 
/*  59 */   private static final int shift = 31 - Integer.numberOfLeadingZeros(i);
/*     */   private final long[] array;
/*     */ 
/*     */   private long checkedByteOffset(int paramInt)
/*     */   {
/*  63 */     if ((paramInt < 0) || (paramInt >= this.array.length)) {
/*  64 */       throw new IndexOutOfBoundsException("index " + paramInt);
/*     */     }
/*  66 */     return byteOffset(paramInt);
/*     */   }
/*     */ 
/*     */   private static long byteOffset(int paramInt) {
/*  70 */     return (paramInt << shift) + base;
/*     */   }
/*     */ 
/*     */   public AtomicLongArray(int paramInt)
/*     */   {
/*  80 */     this.array = new long[paramInt];
/*     */   }
/*     */ 
/*     */   public AtomicLongArray(long[] paramArrayOfLong)
/*     */   {
/*  92 */     this.array = ((long[])paramArrayOfLong.clone());
/*     */   }
/*     */ 
/*     */   public final int length()
/*     */   {
/* 101 */     return this.array.length;
/*     */   }
/*     */ 
/*     */   public final long get(int paramInt)
/*     */   {
/* 111 */     return getRaw(checkedByteOffset(paramInt));
/*     */   }
/*     */ 
/*     */   private long getRaw(long paramLong) {
/* 115 */     return unsafe.getLongVolatile(this.array, paramLong);
/*     */   }
/*     */ 
/*     */   public final void set(int paramInt, long paramLong)
/*     */   {
/* 125 */     unsafe.putLongVolatile(this.array, checkedByteOffset(paramInt), paramLong);
/*     */   }
/*     */ 
/*     */   public final void lazySet(int paramInt, long paramLong)
/*     */   {
/* 136 */     unsafe.putOrderedLong(this.array, checkedByteOffset(paramInt), paramLong);
/*     */   }
/*     */ 
/*     */   public final long getAndSet(int paramInt, long paramLong)
/*     */   {
/* 149 */     long l1 = checkedByteOffset(paramInt);
/*     */     while (true) {
/* 151 */       long l2 = getRaw(l1);
/* 152 */       if (compareAndSetRaw(l1, l2, paramLong))
/* 153 */         return l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean compareAndSet(int paramInt, long paramLong1, long paramLong2)
/*     */   {
/* 168 */     return compareAndSetRaw(checkedByteOffset(paramInt), paramLong1, paramLong2);
/*     */   }
/*     */ 
/*     */   private boolean compareAndSetRaw(long paramLong1, long paramLong2, long paramLong3) {
/* 172 */     return unsafe.compareAndSwapLong(this.array, paramLong1, paramLong2, paramLong3);
/*     */   }
/*     */ 
/*     */   public final boolean weakCompareAndSet(int paramInt, long paramLong1, long paramLong2)
/*     */   {
/* 189 */     return compareAndSet(paramInt, paramLong1, paramLong2);
/*     */   }
/*     */ 
/*     */   public final long getAndIncrement(int paramInt)
/*     */   {
/* 199 */     return getAndAdd(paramInt, 1L);
/*     */   }
/*     */ 
/*     */   public final long getAndDecrement(int paramInt)
/*     */   {
/* 209 */     return getAndAdd(paramInt, -1L);
/*     */   }
/*     */ 
/*     */   public final long getAndAdd(int paramInt, long paramLong)
/*     */   {
/* 220 */     long l1 = checkedByteOffset(paramInt);
/*     */     while (true) {
/* 222 */       long l2 = getRaw(l1);
/* 223 */       if (compareAndSetRaw(l1, l2, l2 + paramLong))
/* 224 */         return l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final long incrementAndGet(int paramInt)
/*     */   {
/* 235 */     return addAndGet(paramInt, 1L);
/*     */   }
/*     */ 
/*     */   public final long decrementAndGet(int paramInt)
/*     */   {
/* 245 */     return addAndGet(paramInt, -1L);
/*     */   }
/*     */ 
/*     */   public long addAndGet(int paramInt, long paramLong)
/*     */   {
/* 256 */     long l1 = checkedByteOffset(paramInt);
/*     */     while (true) {
/* 258 */       long l2 = getRaw(l1);
/* 259 */       long l3 = l2 + paramLong;
/* 260 */       if (compareAndSetRaw(l1, l2, l3))
/* 261 */         return l3;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 270 */     int i = this.array.length - 1;
/* 271 */     if (i == -1) {
/* 272 */       return "[]";
/*     */     }
/* 274 */     StringBuilder localStringBuilder = new StringBuilder();
/* 275 */     localStringBuilder.append('[');
/* 276 */     for (int j = 0; ; j++) {
/* 277 */       localStringBuilder.append(getRaw(byteOffset(j)));
/* 278 */       if (j == i)
/* 279 */         return ']';
/* 280 */       localStringBuilder.append(',').append(' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  56 */     int i = unsafe.arrayIndexScale([J.class);
/*  57 */     if ((i & i - 1) != 0)
/*  58 */       throw new Error("data type scale not a power of two");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicLongArray
 * JD-Core Version:    0.6.2
 */
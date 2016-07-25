/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AtomicIntegerArray
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2862133569453604235L;
/*  51 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*  52 */   private static final int base = unsafe.arrayBaseOffset([I.class);
/*     */ 
/*  60 */   private static final int shift = 31 - Integer.numberOfLeadingZeros(i);
/*     */   private final int[] array;
/*     */ 
/*     */   private long checkedByteOffset(int paramInt)
/*     */   {
/*  64 */     if ((paramInt < 0) || (paramInt >= this.array.length)) {
/*  65 */       throw new IndexOutOfBoundsException("index " + paramInt);
/*     */     }
/*  67 */     return byteOffset(paramInt);
/*     */   }
/*     */ 
/*     */   private static long byteOffset(int paramInt) {
/*  71 */     return (paramInt << shift) + base;
/*     */   }
/*     */ 
/*     */   public AtomicIntegerArray(int paramInt)
/*     */   {
/*  81 */     this.array = new int[paramInt];
/*     */   }
/*     */ 
/*     */   public AtomicIntegerArray(int[] paramArrayOfInt)
/*     */   {
/*  93 */     this.array = ((int[])paramArrayOfInt.clone());
/*     */   }
/*     */ 
/*     */   public final int length()
/*     */   {
/* 102 */     return this.array.length;
/*     */   }
/*     */ 
/*     */   public final int get(int paramInt)
/*     */   {
/* 112 */     return getRaw(checkedByteOffset(paramInt));
/*     */   }
/*     */ 
/*     */   private int getRaw(long paramLong) {
/* 116 */     return unsafe.getIntVolatile(this.array, paramLong);
/*     */   }
/*     */ 
/*     */   public final void set(int paramInt1, int paramInt2)
/*     */   {
/* 126 */     unsafe.putIntVolatile(this.array, checkedByteOffset(paramInt1), paramInt2);
/*     */   }
/*     */ 
/*     */   public final void lazySet(int paramInt1, int paramInt2)
/*     */   {
/* 137 */     unsafe.putOrderedInt(this.array, checkedByteOffset(paramInt1), paramInt2);
/*     */   }
/*     */ 
/*     */   public final int getAndSet(int paramInt1, int paramInt2)
/*     */   {
/* 149 */     long l = checkedByteOffset(paramInt1);
/*     */     while (true) {
/* 151 */       int i = getRaw(l);
/* 152 */       if (compareAndSetRaw(l, i, paramInt2))
/* 153 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean compareAndSet(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 168 */     return compareAndSetRaw(checkedByteOffset(paramInt1), paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   private boolean compareAndSetRaw(long paramLong, int paramInt1, int paramInt2) {
/* 172 */     return unsafe.compareAndSwapInt(this.array, paramLong, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final boolean weakCompareAndSet(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 189 */     return compareAndSet(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public final int getAndIncrement(int paramInt)
/*     */   {
/* 199 */     return getAndAdd(paramInt, 1);
/*     */   }
/*     */ 
/*     */   public final int getAndDecrement(int paramInt)
/*     */   {
/* 209 */     return getAndAdd(paramInt, -1);
/*     */   }
/*     */ 
/*     */   public final int getAndAdd(int paramInt1, int paramInt2)
/*     */   {
/* 220 */     long l = checkedByteOffset(paramInt1);
/*     */     while (true) {
/* 222 */       int i = getRaw(l);
/* 223 */       if (compareAndSetRaw(l, i, i + paramInt2))
/* 224 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int incrementAndGet(int paramInt)
/*     */   {
/* 235 */     return addAndGet(paramInt, 1);
/*     */   }
/*     */ 
/*     */   public final int decrementAndGet(int paramInt)
/*     */   {
/* 245 */     return addAndGet(paramInt, -1);
/*     */   }
/*     */ 
/*     */   public final int addAndGet(int paramInt1, int paramInt2)
/*     */   {
/* 256 */     long l = checkedByteOffset(paramInt1);
/*     */     while (true) {
/* 258 */       int i = getRaw(l);
/* 259 */       int j = i + paramInt2;
/* 260 */       if (compareAndSetRaw(l, i, j))
/* 261 */         return j;
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
/*  57 */     int i = unsafe.arrayIndexScale([I.class);
/*  58 */     if ((i & i - 1) != 0)
/*  59 */       throw new Error("data type scale not a power of two");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicIntegerArray
 * JD-Core Version:    0.6.2
 */
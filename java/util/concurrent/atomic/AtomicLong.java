/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AtomicLong extends Number
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1927816293512124184L;
/*  56 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final long valueOffset;
/*  65 */   static final boolean VM_SUPPORTS_LONG_CAS = VMSupportsCS8();
/*     */   private volatile long value;
/*     */ 
/*     */   private static native boolean VMSupportsCS8();
/*     */ 
/*     */   public AtomicLong(long paramLong)
/*     */   {
/*  88 */     this.value = paramLong;
/*     */   }
/*     */ 
/*     */   public AtomicLong()
/*     */   {
/*     */   }
/*     */ 
/*     */   public final long get()
/*     */   {
/* 103 */     return this.value;
/*     */   }
/*     */ 
/*     */   public final void set(long paramLong)
/*     */   {
/* 112 */     this.value = paramLong;
/*     */   }
/*     */ 
/*     */   public final void lazySet(long paramLong)
/*     */   {
/* 122 */     unsafe.putOrderedLong(this, valueOffset, paramLong);
/*     */   }
/*     */ 
/*     */   public final long getAndSet(long paramLong)
/*     */   {
/*     */     while (true)
/*     */     {
/* 133 */       long l = get();
/* 134 */       if (compareAndSet(l, paramLong))
/* 135 */         return l;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean compareAndSet(long paramLong1, long paramLong2)
/*     */   {
/* 149 */     return unsafe.compareAndSwapLong(this, valueOffset, paramLong1, paramLong2);
/*     */   }
/*     */ 
/*     */   public final boolean weakCompareAndSet(long paramLong1, long paramLong2)
/*     */   {
/* 165 */     return unsafe.compareAndSwapLong(this, valueOffset, paramLong1, paramLong2);
/*     */   }
/*     */ 
/*     */   public final long getAndIncrement()
/*     */   {
/*     */     while (true)
/*     */     {
/* 175 */       long l1 = get();
/* 176 */       long l2 = l1 + 1L;
/* 177 */       if (compareAndSet(l1, l2))
/* 178 */         return l1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final long getAndDecrement()
/*     */   {
/*     */     while (true)
/*     */     {
/* 189 */       long l1 = get();
/* 190 */       long l2 = l1 - 1L;
/* 191 */       if (compareAndSet(l1, l2))
/* 192 */         return l1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final long getAndAdd(long paramLong)
/*     */   {
/*     */     while (true)
/*     */     {
/* 204 */       long l1 = get();
/* 205 */       long l2 = l1 + paramLong;
/* 206 */       if (compareAndSet(l1, l2))
/* 207 */         return l1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final long incrementAndGet()
/*     */   {
/*     */     while (true)
/*     */     {
/* 218 */       long l1 = get();
/* 219 */       long l2 = l1 + 1L;
/* 220 */       if (compareAndSet(l1, l2))
/* 221 */         return l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final long decrementAndGet()
/*     */   {
/*     */     while (true)
/*     */     {
/* 232 */       long l1 = get();
/* 233 */       long l2 = l1 - 1L;
/* 234 */       if (compareAndSet(l1, l2))
/* 235 */         return l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final long addAndGet(long paramLong)
/*     */   {
/*     */     while (true)
/*     */     {
/* 247 */       long l1 = get();
/* 248 */       long l2 = l1 + paramLong;
/* 249 */       if (compareAndSet(l1, l2))
/* 250 */         return l2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 259 */     return Long.toString(get());
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 264 */     return (int)get();
/*     */   }
/*     */ 
/*     */   public long longValue() {
/* 268 */     return get();
/*     */   }
/*     */ 
/*     */   public float floatValue() {
/* 272 */     return (float)get();
/*     */   }
/*     */ 
/*     */   public double doubleValue() {
/* 276 */     return get();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  75 */       valueOffset = unsafe.objectFieldOffset(AtomicLong.class.getDeclaredField("value"));
/*     */     } catch (Exception localException) {
/*  77 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicLong
 * JD-Core Version:    0.6.2
 */
/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AtomicInteger extends Number
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6214790243416807050L;
/*  56 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final long valueOffset;
/*     */   private volatile int value;
/*     */ 
/*     */   public AtomicInteger(int paramInt)
/*     */   {
/*  74 */     this.value = paramInt;
/*     */   }
/*     */ 
/*     */   public AtomicInteger()
/*     */   {
/*     */   }
/*     */ 
/*     */   public final int get()
/*     */   {
/*  89 */     return this.value;
/*     */   }
/*     */ 
/*     */   public final void set(int paramInt)
/*     */   {
/*  98 */     this.value = paramInt;
/*     */   }
/*     */ 
/*     */   public final void lazySet(int paramInt)
/*     */   {
/* 108 */     unsafe.putOrderedInt(this, valueOffset, paramInt);
/*     */   }
/*     */ 
/*     */   public final int getAndSet(int paramInt)
/*     */   {
/*     */     while (true)
/*     */     {
/* 119 */       int i = get();
/* 120 */       if (compareAndSet(i, paramInt))
/* 121 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean compareAndSet(int paramInt1, int paramInt2)
/*     */   {
/* 135 */     return unsafe.compareAndSwapInt(this, valueOffset, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final boolean weakCompareAndSet(int paramInt1, int paramInt2)
/*     */   {
/* 151 */     return unsafe.compareAndSwapInt(this, valueOffset, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final int getAndIncrement()
/*     */   {
/*     */     while (true)
/*     */     {
/* 161 */       int i = get();
/* 162 */       int j = i + 1;
/* 163 */       if (compareAndSet(i, j))
/* 164 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getAndDecrement()
/*     */   {
/*     */     while (true)
/*     */     {
/* 175 */       int i = get();
/* 176 */       int j = i - 1;
/* 177 */       if (compareAndSet(i, j))
/* 178 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getAndAdd(int paramInt)
/*     */   {
/*     */     while (true)
/*     */     {
/* 190 */       int i = get();
/* 191 */       int j = i + paramInt;
/* 192 */       if (compareAndSet(i, j))
/* 193 */         return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int incrementAndGet()
/*     */   {
/*     */     while (true)
/*     */     {
/* 204 */       int i = get();
/* 205 */       int j = i + 1;
/* 206 */       if (compareAndSet(i, j))
/* 207 */         return j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int decrementAndGet()
/*     */   {
/*     */     while (true)
/*     */     {
/* 218 */       int i = get();
/* 219 */       int j = i - 1;
/* 220 */       if (compareAndSet(i, j))
/* 221 */         return j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int addAndGet(int paramInt)
/*     */   {
/*     */     while (true)
/*     */     {
/* 233 */       int i = get();
/* 234 */       int j = i + paramInt;
/* 235 */       if (compareAndSet(i, j))
/* 236 */         return j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 245 */     return Integer.toString(get());
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 250 */     return get();
/*     */   }
/*     */ 
/*     */   public long longValue() {
/* 254 */     return get();
/*     */   }
/*     */ 
/*     */   public float floatValue() {
/* 258 */     return get();
/*     */   }
/*     */ 
/*     */   public double doubleValue() {
/* 262 */     return get();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  61 */       valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
/*     */     } catch (Exception localException) {
/*  63 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicInteger
 * JD-Core Version:    0.6.2
 */
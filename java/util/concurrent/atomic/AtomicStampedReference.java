/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AtomicStampedReference<V>
/*     */ {
/*     */   private volatile Pair<V> pair;
/* 194 */   private static final Unsafe UNSAFE = Unsafe.getUnsafe();
/* 195 */   private static final long pairOffset = objectFieldOffset(UNSAFE, "pair", AtomicStampedReference.class);
/*     */ 
/*     */   public AtomicStampedReference(V paramV, int paramInt)
/*     */   {
/*  74 */     this.pair = Pair.of(paramV, paramInt);
/*     */   }
/*     */ 
/*     */   public V getReference()
/*     */   {
/*  83 */     return this.pair.reference;
/*     */   }
/*     */ 
/*     */   public int getStamp()
/*     */   {
/*  92 */     return this.pair.stamp;
/*     */   }
/*     */ 
/*     */   public V get(int[] paramArrayOfInt)
/*     */   {
/* 104 */     Pair localPair = this.pair;
/* 105 */     paramArrayOfInt[0] = localPair.stamp;
/* 106 */     return localPair.reference;
/*     */   }
/*     */ 
/*     */   public boolean weakCompareAndSet(V paramV1, V paramV2, int paramInt1, int paramInt2)
/*     */   {
/* 129 */     return compareAndSet(paramV1, paramV2, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public boolean compareAndSet(V paramV1, V paramV2, int paramInt1, int paramInt2)
/*     */   {
/* 149 */     Pair localPair = this.pair;
/* 150 */     return (paramV1 == localPair.reference) && (paramInt1 == localPair.stamp) && (((paramV2 == localPair.reference) && (paramInt2 == localPair.stamp)) || (casPair(localPair, Pair.of(paramV2, paramInt2))));
/*     */   }
/*     */ 
/*     */   public void set(V paramV, int paramInt)
/*     */   {
/* 166 */     Pair localPair = this.pair;
/* 167 */     if ((paramV != localPair.reference) || (paramInt != localPair.stamp))
/* 168 */       this.pair = Pair.of(paramV, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean attemptStamp(V paramV, int paramInt)
/*     */   {
/* 185 */     Pair localPair = this.pair;
/* 186 */     return (paramV == localPair.reference) && ((paramInt == localPair.stamp) || (casPair(localPair, Pair.of(paramV, paramInt))));
/*     */   }
/*     */ 
/*     */   private boolean casPair(Pair<V> paramPair1, Pair<V> paramPair2)
/*     */   {
/* 199 */     return UNSAFE.compareAndSwapObject(this, pairOffset, paramPair1, paramPair2);
/*     */   }
/*     */ 
/*     */   static long objectFieldOffset(Unsafe paramUnsafe, String paramString, Class<?> paramClass)
/*     */   {
/*     */     try {
/* 205 */       return paramUnsafe.objectFieldOffset(paramClass.getDeclaredField(paramString));
/*     */     }
/*     */     catch (NoSuchFieldException localNoSuchFieldException) {
/* 208 */       NoSuchFieldError localNoSuchFieldError = new NoSuchFieldError(paramString);
/* 209 */       localNoSuchFieldError.initCause(localNoSuchFieldException);
/* 210 */       throw localNoSuchFieldError;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Pair<T>
/*     */   {
/*     */     final T reference;
/*     */     final int stamp;
/*     */ 
/*     */     private Pair(T paramT, int paramInt)
/*     */     {
/*  56 */       this.reference = paramT;
/*  57 */       this.stamp = paramInt;
/*     */     }
/*     */     static <T> Pair<T> of(T paramT, int paramInt) {
/*  60 */       return new Pair(paramT, paramInt);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicStampedReference
 * JD-Core Version:    0.6.2
 */
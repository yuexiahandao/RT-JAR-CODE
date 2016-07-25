/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AtomicMarkableReference<V>
/*     */ {
/*     */   private volatile Pair<V> pair;
/* 193 */   private static final Unsafe UNSAFE = Unsafe.getUnsafe();
/* 194 */   private static final long pairOffset = objectFieldOffset(UNSAFE, "pair", AtomicMarkableReference.class);
/*     */ 
/*     */   public AtomicMarkableReference(V paramV, boolean paramBoolean)
/*     */   {
/*  74 */     this.pair = Pair.of(paramV, paramBoolean);
/*     */   }
/*     */ 
/*     */   public V getReference()
/*     */   {
/*  83 */     return this.pair.reference;
/*     */   }
/*     */ 
/*     */   public boolean isMarked()
/*     */   {
/*  92 */     return this.pair.mark;
/*     */   }
/*     */ 
/*     */   public V get(boolean[] paramArrayOfBoolean)
/*     */   {
/* 104 */     Pair localPair = this.pair;
/* 105 */     paramArrayOfBoolean[0] = localPair.mark;
/* 106 */     return localPair.reference;
/*     */   }
/*     */ 
/*     */   public boolean weakCompareAndSet(V paramV1, V paramV2, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 129 */     return compareAndSet(paramV1, paramV2, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   public boolean compareAndSet(V paramV1, V paramV2, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 149 */     Pair localPair = this.pair;
/* 150 */     return (paramV1 == localPair.reference) && (paramBoolean1 == localPair.mark) && (((paramV2 == localPair.reference) && (paramBoolean2 == localPair.mark)) || (casPair(localPair, Pair.of(paramV2, paramBoolean2))));
/*     */   }
/*     */ 
/*     */   public void set(V paramV, boolean paramBoolean)
/*     */   {
/* 165 */     Pair localPair = this.pair;
/* 166 */     if ((paramV != localPair.reference) || (paramBoolean != localPair.mark))
/* 167 */       this.pair = Pair.of(paramV, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean attemptMark(V paramV, boolean paramBoolean)
/*     */   {
/* 184 */     Pair localPair = this.pair;
/* 185 */     return (paramV == localPair.reference) && ((paramBoolean == localPair.mark) || (casPair(localPair, Pair.of(paramV, paramBoolean))));
/*     */   }
/*     */ 
/*     */   private boolean casPair(Pair<V> paramPair1, Pair<V> paramPair2)
/*     */   {
/* 198 */     return UNSAFE.compareAndSwapObject(this, pairOffset, paramPair1, paramPair2);
/*     */   }
/*     */ 
/*     */   static long objectFieldOffset(Unsafe paramUnsafe, String paramString, Class<?> paramClass)
/*     */   {
/*     */     try {
/* 204 */       return paramUnsafe.objectFieldOffset(paramClass.getDeclaredField(paramString));
/*     */     }
/*     */     catch (NoSuchFieldException localNoSuchFieldException) {
/* 207 */       NoSuchFieldError localNoSuchFieldError = new NoSuchFieldError(paramString);
/* 208 */       localNoSuchFieldError.initCause(localNoSuchFieldException);
/* 209 */       throw localNoSuchFieldError;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Pair<T>
/*     */   {
/*     */     final T reference;
/*     */     final boolean mark;
/*     */ 
/*     */     private Pair(T paramT, boolean paramBoolean)
/*     */     {
/*  56 */       this.reference = paramT;
/*  57 */       this.mark = paramBoolean;
/*     */     }
/*     */     static <T> Pair<T> of(T paramT, boolean paramBoolean) {
/*  60 */       return new Pair(paramT, paramBoolean);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicMarkableReference
 * JD-Core Version:    0.6.2
 */
/*     */ package java.util.concurrent.atomic;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class AtomicReference<V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1848883965231344442L;
/*  50 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final long valueOffset;
/*     */   private volatile V value;
/*     */ 
/*     */   public AtomicReference(V paramV)
/*     */   {
/*  68 */     this.value = paramV;
/*     */   }
/*     */ 
/*     */   public AtomicReference()
/*     */   {
/*     */   }
/*     */ 
/*     */   public final V get()
/*     */   {
/*  83 */     return this.value;
/*     */   }
/*     */ 
/*     */   public final void set(V paramV)
/*     */   {
/*  92 */     this.value = paramV;
/*     */   }
/*     */ 
/*     */   public final void lazySet(V paramV)
/*     */   {
/* 102 */     unsafe.putOrderedObject(this, valueOffset, paramV);
/*     */   }
/*     */ 
/*     */   public final boolean compareAndSet(V paramV1, V paramV2)
/*     */   {
/* 114 */     return unsafe.compareAndSwapObject(this, valueOffset, paramV1, paramV2);
/*     */   }
/*     */ 
/*     */   public final boolean weakCompareAndSet(V paramV1, V paramV2)
/*     */   {
/* 130 */     return unsafe.compareAndSwapObject(this, valueOffset, paramV1, paramV2);
/*     */   }
/*     */ 
/*     */   public final V getAndSet(V paramV)
/*     */   {
/*     */     while (true)
/*     */     {
/* 141 */       Object localObject = get();
/* 142 */       if (compareAndSet(localObject, paramV))
/* 143 */         return localObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 152 */     return String.valueOf(get());
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  55 */       valueOffset = unsafe.objectFieldOffset(AtomicReference.class.getDeclaredField("value"));
/*     */     } catch (Exception localException) {
/*  57 */       throw new Error(localException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.concurrent.atomic.AtomicReference
 * JD-Core Version:    0.6.2
 */
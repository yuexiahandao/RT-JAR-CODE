/*     */ package java.lang.ref;
/*     */ 
/*     */ public class SoftReference<T> extends Reference<T>
/*     */ {
/*     */   private static long clock;
/*     */   private long timestamp;
/*     */ 
/*     */   public SoftReference(T paramT)
/*     */   {
/*  85 */     super(paramT);
/*  86 */     this.timestamp = clock;
/*     */   }
/*     */ 
/*     */   public SoftReference(T paramT, ReferenceQueue<? super T> paramReferenceQueue)
/*     */   {
/*  99 */     super(paramT, paramReferenceQueue);
/* 100 */     this.timestamp = clock;
/*     */   }
/*     */ 
/*     */   public T get()
/*     */   {
/* 112 */     Object localObject = super.get();
/* 113 */     if ((localObject != null) && (this.timestamp != clock))
/* 114 */       this.timestamp = clock;
/* 115 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ref.SoftReference
 * JD-Core Version:    0.6.2
 */
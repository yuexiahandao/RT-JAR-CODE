/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Map;
/*     */ 
/*     */ class WeakIdentityHashMap<K, V>
/*     */ {
/* 135 */   private Map<WeakReference<K>, V> map = Util.newMap();
/* 136 */   private ReferenceQueue<K> refQueue = new ReferenceQueue();
/*     */ 
/*     */   static <K, V> WeakIdentityHashMap<K, V> make()
/*     */   {
/*  63 */     return new WeakIdentityHashMap();
/*     */   }
/*     */ 
/*     */   V get(K paramK) {
/*  67 */     expunge();
/*  68 */     WeakReference localWeakReference = makeReference(paramK);
/*  69 */     return this.map.get(localWeakReference);
/*     */   }
/*     */ 
/*     */   public V put(K paramK, V paramV) {
/*  73 */     expunge();
/*  74 */     if (paramK == null)
/*  75 */       throw new IllegalArgumentException("Null key");
/*  76 */     WeakReference localWeakReference = makeReference(paramK, this.refQueue);
/*  77 */     return this.map.put(localWeakReference, paramV);
/*     */   }
/*     */ 
/*     */   public V remove(K paramK) {
/*  81 */     expunge();
/*  82 */     WeakReference localWeakReference = makeReference(paramK);
/*  83 */     return this.map.remove(localWeakReference);
/*     */   }
/*     */ 
/*     */   private void expunge()
/*     */   {
/*     */     Reference localReference;
/*  88 */     while ((localReference = this.refQueue.poll()) != null)
/*  89 */       this.map.remove(localReference);
/*     */   }
/*     */ 
/*     */   private WeakReference<K> makeReference(K paramK) {
/*  93 */     return new IdentityWeakReference(paramK);
/*     */   }
/*     */ 
/*     */   private WeakReference<K> makeReference(K paramK, ReferenceQueue<K> paramReferenceQueue) {
/*  97 */     return new IdentityWeakReference(paramK, paramReferenceQueue);
/*     */   }
/*     */ 
/*     */   private static class IdentityWeakReference<T> extends WeakReference<T>
/*     */   {
/*     */     private final int hashCode;
/*     */ 
/*     */     IdentityWeakReference(T paramT)
/*     */     {
/* 110 */       this(paramT, null);
/*     */     }
/*     */ 
/*     */     IdentityWeakReference(T paramT, ReferenceQueue<T> paramReferenceQueue) {
/* 114 */       super(paramReferenceQueue);
/* 115 */       this.hashCode = (paramT == null ? 0 : System.identityHashCode(paramT));
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 119 */       if (this == paramObject)
/* 120 */         return true;
/* 121 */       if (!(paramObject instanceof IdentityWeakReference))
/* 122 */         return false;
/* 123 */       IdentityWeakReference localIdentityWeakReference = (IdentityWeakReference)paramObject;
/* 124 */       Object localObject = get();
/* 125 */       return (localObject != null) && (localObject == localIdentityWeakReference.get());
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 129 */       return this.hashCode;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.WeakIdentityHashMap
 * JD-Core Version:    0.6.2
 */
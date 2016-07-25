/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import sun.rmi.runtime.Log;
/*     */ 
/*     */ class WeakRef extends WeakReference<Object>
/*     */ {
/*     */   private int hashValue;
/*  50 */   private Object strongRef = null;
/*     */ 
/*     */   public WeakRef(Object paramObject)
/*     */   {
/*  56 */     super(paramObject);
/*  57 */     setHashValue(paramObject);
/*     */   }
/*     */ 
/*     */   public WeakRef(Object paramObject, ReferenceQueue<Object> paramReferenceQueue)
/*     */   {
/*  64 */     super(paramObject, paramReferenceQueue);
/*  65 */     setHashValue(paramObject);
/*     */   }
/*     */ 
/*     */   public synchronized void pin()
/*     */   {
/*  72 */     if (this.strongRef == null) {
/*  73 */       this.strongRef = get();
/*     */ 
/*  75 */       if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE))
/*  76 */         DGCImpl.dgcLog.log(Log.VERBOSE, "strongRef = " + this.strongRef);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void unpin()
/*     */   {
/*  86 */     if (this.strongRef != null) {
/*  87 */       if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
/*  88 */         DGCImpl.dgcLog.log(Log.VERBOSE, "strongRef = " + this.strongRef);
/*     */       }
/*     */ 
/*  92 */       this.strongRef = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setHashValue(Object paramObject)
/*     */   {
/* 109 */     if (paramObject != null)
/* 110 */       this.hashValue = System.identityHashCode(paramObject);
/*     */     else
/* 112 */       this.hashValue = 0;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 120 */     return this.hashValue;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 129 */     if ((paramObject instanceof WeakRef)) {
/* 130 */       if (paramObject == this) {
/* 131 */         return true;
/*     */       }
/* 133 */       Object localObject = get();
/* 134 */       return (localObject != null) && (localObject == ((WeakRef)paramObject).get());
/*     */     }
/* 136 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.WeakRef
 * JD-Core Version:    0.6.2
 */
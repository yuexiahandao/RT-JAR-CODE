/*     */ package sun.misc;
/*     */ 
/*     */ class QueueElement
/*     */ {
/* 201 */   QueueElement next = null;
/* 202 */   QueueElement prev = null;
/*     */ 
/* 204 */   Object obj = null;
/*     */ 
/*     */   QueueElement(Object paramObject) {
/* 207 */     this.obj = paramObject;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 211 */     return "QueueElement[obj=" + this.obj + (this.prev == null ? " null" : " prev") + (this.next == null ? " null" : " next") + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.QueueElement
 * JD-Core Version:    0.6.2
 */
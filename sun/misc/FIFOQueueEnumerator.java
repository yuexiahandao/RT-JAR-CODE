/*     */ package sun.misc;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ final class FIFOQueueEnumerator
/*     */   implements Enumeration
/*     */ {
/*     */   Queue queue;
/*     */   QueueElement cursor;
/*     */ 
/*     */   FIFOQueueEnumerator(Queue paramQueue)
/*     */   {
/* 155 */     this.queue = paramQueue;
/* 156 */     this.cursor = paramQueue.tail;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements() {
/* 160 */     return this.cursor != null;
/*     */   }
/*     */ 
/*     */   public Object nextElement() {
/* 164 */     synchronized (this.queue) {
/* 165 */       if (this.cursor != null) {
/* 166 */         QueueElement localQueueElement = this.cursor;
/* 167 */         this.cursor = this.cursor.prev;
/* 168 */         return localQueueElement.obj;
/*     */       }
/*     */     }
/* 171 */     throw new NoSuchElementException("FIFOQueueEnumerator");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.FIFOQueueEnumerator
 * JD-Core Version:    0.6.2
 */
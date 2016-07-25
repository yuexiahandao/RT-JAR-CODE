/*     */ package sun.misc;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ final class LIFOQueueEnumerator
/*     */   implements Enumeration
/*     */ {
/*     */   Queue queue;
/*     */   QueueElement cursor;
/*     */ 
/*     */   LIFOQueueEnumerator(Queue paramQueue)
/*     */   {
/* 180 */     this.queue = paramQueue;
/* 181 */     this.cursor = paramQueue.head;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements() {
/* 185 */     return this.cursor != null;
/*     */   }
/*     */ 
/*     */   public Object nextElement() {
/* 189 */     synchronized (this.queue) {
/* 190 */       if (this.cursor != null) {
/* 191 */         QueueElement localQueueElement = this.cursor;
/* 192 */         this.cursor = this.cursor.next;
/* 193 */         return localQueueElement.obj;
/*     */       }
/*     */     }
/* 196 */     throw new NoSuchElementException("LIFOQueueEnumerator");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.LIFOQueueEnumerator
 * JD-Core Version:    0.6.2
 */
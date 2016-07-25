/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class Queue
/*     */ {
/*  40 */   int length = 0;
/*     */ 
/*  42 */   QueueElement head = null;
/*  43 */   QueueElement tail = null;
/*     */ 
/*     */   public synchronized void enqueue(Object paramObject)
/*     */   {
/*  53 */     QueueElement localQueueElement = new QueueElement(paramObject);
/*     */ 
/*  55 */     if (this.head == null) {
/*  56 */       this.head = localQueueElement;
/*  57 */       this.tail = localQueueElement;
/*  58 */       this.length = 1;
/*     */     } else {
/*  60 */       localQueueElement.next = this.head;
/*  61 */       this.head.prev = localQueueElement;
/*  62 */       this.head = localQueueElement;
/*  63 */       this.length += 1;
/*     */     }
/*  65 */     notify();
/*     */   }
/*     */ 
/*     */   public Object dequeue()
/*     */     throws InterruptedException
/*     */   {
/*  76 */     return dequeue(0L);
/*     */   }
/*     */ 
/*     */   public synchronized Object dequeue(long paramLong)
/*     */     throws InterruptedException
/*     */   {
/*  91 */     while (this.tail == null) {
/*  92 */       wait(paramLong);
/*     */     }
/*  94 */     QueueElement localQueueElement = this.tail;
/*  95 */     this.tail = localQueueElement.prev;
/*  96 */     if (this.tail == null)
/*  97 */       this.head = null;
/*     */     else {
/*  99 */       this.tail.next = null;
/*     */     }
/* 101 */     this.length -= 1;
/* 102 */     return localQueueElement.obj;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isEmpty()
/*     */   {
/* 110 */     return this.tail == null;
/*     */   }
/*     */ 
/*     */   public final synchronized Enumeration elements()
/*     */   {
/* 119 */     return new LIFOQueueEnumerator(this);
/*     */   }
/*     */ 
/*     */   public final synchronized Enumeration reverseElements()
/*     */   {
/* 128 */     return new FIFOQueueEnumerator(this);
/*     */   }
/*     */ 
/*     */   public synchronized void dump(String paramString) {
/* 132 */     System.err.println(">> " + paramString);
/* 133 */     System.err.println("[" + this.length + " elt(s); head = " + (this.head == null ? "null" : new StringBuilder().append(this.head.obj).append("").toString()) + " tail = " + (this.tail == null ? "null" : new StringBuilder().append(this.tail.obj).append("").toString()));
/*     */ 
/* 136 */     QueueElement localQueueElement1 = this.head;
/* 137 */     QueueElement localQueueElement2 = null;
/* 138 */     while (localQueueElement1 != null) {
/* 139 */       System.err.println("  " + localQueueElement1);
/* 140 */       localQueueElement2 = localQueueElement1;
/* 141 */       localQueueElement1 = localQueueElement1.next;
/*     */     }
/* 143 */     if (localQueueElement2 != this.tail) {
/* 144 */       System.err.println("  tail != last: " + this.tail + ", " + localQueueElement2);
/*     */     }
/* 146 */     System.err.println("]");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Queue
 * JD-Core Version:    0.6.2
 */
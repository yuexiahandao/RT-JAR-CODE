/*     */ package java.util;
/*     */ 
/*     */ class TaskQueue
/*     */ {
/* 578 */   private TimerTask[] queue = new TimerTask[''];
/*     */ 
/* 584 */   private int size = 0;
/*     */ 
/*     */   int size()
/*     */   {
/* 590 */     return this.size;
/*     */   }
/*     */ 
/*     */   void add(TimerTask paramTimerTask)
/*     */   {
/* 598 */     if (this.size + 1 == this.queue.length) {
/* 599 */       this.queue = ((TimerTask[])Arrays.copyOf(this.queue, 2 * this.queue.length));
/*     */     }
/* 601 */     this.queue[(++this.size)] = paramTimerTask;
/* 602 */     fixUp(this.size);
/*     */   }
/*     */ 
/*     */   TimerTask getMin()
/*     */   {
/* 610 */     return this.queue[1];
/*     */   }
/*     */ 
/*     */   TimerTask get(int paramInt)
/*     */   {
/* 619 */     return this.queue[paramInt];
/*     */   }
/*     */ 
/*     */   void removeMin()
/*     */   {
/* 626 */     this.queue[1] = this.queue[this.size];
/* 627 */     this.queue[(this.size--)] = null;
/* 628 */     fixDown(1);
/*     */   }
/*     */ 
/*     */   void quickRemove(int paramInt)
/*     */   {
/* 637 */     assert (paramInt <= this.size);
/*     */ 
/* 639 */     this.queue[paramInt] = this.queue[this.size];
/* 640 */     this.queue[(this.size--)] = null;
/*     */   }
/*     */ 
/*     */   void rescheduleMin(long paramLong)
/*     */   {
/* 648 */     this.queue[1].nextExecutionTime = paramLong;
/* 649 */     fixDown(1);
/*     */   }
/*     */ 
/*     */   boolean isEmpty()
/*     */   {
/* 656 */     return this.size == 0;
/*     */   }
/*     */ 
/*     */   void clear()
/*     */   {
/* 664 */     for (int i = 1; i <= this.size; i++) {
/* 665 */       this.queue[i] = null;
/*     */     }
/* 667 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   private void fixUp(int paramInt)
/*     */   {
/* 680 */     while (paramInt > 1) {
/* 681 */       int i = paramInt >> 1;
/* 682 */       if (this.queue[i].nextExecutionTime <= this.queue[paramInt].nextExecutionTime)
/*     */         break;
/* 684 */       TimerTask localTimerTask = this.queue[i]; this.queue[i] = this.queue[paramInt]; this.queue[paramInt] = localTimerTask;
/* 685 */       paramInt = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void fixDown(int paramInt)
/*     */   {
/*     */     int i;
/* 701 */     while (((i = paramInt << 1) <= this.size) && (i > 0)) {
/* 702 */       if ((i < this.size) && (this.queue[i].nextExecutionTime > this.queue[(i + 1)].nextExecutionTime))
/*     */       {
/* 704 */         i++;
/* 705 */       }if (this.queue[paramInt].nextExecutionTime <= this.queue[i].nextExecutionTime)
/*     */         break;
/* 707 */       TimerTask localTimerTask = this.queue[i]; this.queue[i] = this.queue[paramInt]; this.queue[paramInt] = localTimerTask;
/* 708 */       paramInt = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   void heapify()
/*     */   {
/* 717 */     for (int i = this.size / 2; i >= 1; i--)
/* 718 */       fixDown(i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.TaskQueue
 * JD-Core Version:    0.6.2
 */
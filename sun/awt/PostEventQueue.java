/*      */ package sun.awt;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.EventQueue;
/*      */ 
/*      */ class PostEventQueue
/*      */ {
/* 2026 */   private EventQueueItem queueHead = null;
/* 2027 */   private EventQueueItem queueTail = null;
/*      */   private final EventQueue eventQueue;
/* 2031 */   private volatile boolean isFlushing = false;
/*      */ 
/*      */   PostEventQueue(EventQueue paramEventQueue) {
/* 2034 */     this.eventQueue = paramEventQueue;
/*      */   }
/*      */ 
/*      */   public synchronized boolean noEvents() {
/* 2038 */     return (this.queueHead == null) && (!this.isFlushing);
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */   {
/*      */     EventQueueItem localEventQueueItem;
/* 2052 */     synchronized (this) {
/* 2053 */       localEventQueueItem = this.queueHead;
/* 2054 */       this.queueHead = (this.queueTail = null);
/* 2055 */       this.isFlushing = (localEventQueueItem != null);
/*      */     }
/*      */     try {
/* 2058 */       while (localEventQueueItem != null) {
/* 2059 */         this.eventQueue.postEvent(localEventQueueItem.event);
/* 2060 */         localEventQueueItem = localEventQueueItem.next;
/*      */       }
/*      */     }
/*      */     finally {
/* 2064 */       this.isFlushing = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   void postEvent(AWTEvent paramAWTEvent)
/*      */   {
/* 2072 */     EventQueueItem localEventQueueItem = new EventQueueItem(paramAWTEvent);
/*      */ 
/* 2074 */     synchronized (this) {
/* 2075 */       if (this.queueHead == null) {
/* 2076 */         this.queueHead = (this.queueTail = localEventQueueItem);
/*      */       } else {
/* 2078 */         this.queueTail.next = localEventQueueItem;
/* 2079 */         this.queueTail = localEventQueueItem;
/*      */       }
/*      */     }
/* 2082 */     SunToolkit.wakeupEventQueue(this.eventQueue, paramAWTEvent.getSource() == AWTAutoShutdown.getInstance());
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.PostEventQueue
 * JD-Core Version:    0.6.2
 */
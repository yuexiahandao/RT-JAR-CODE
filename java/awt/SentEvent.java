/*    */ package java.awt;
/*    */ 
/*    */ import sun.awt.AppContext;
/*    */ import sun.awt.SunToolkit;
/*    */ 
/*    */ class SentEvent extends AWTEvent
/*    */   implements ActiveEvent
/*    */ {
/*    */   private static final long serialVersionUID = -383615247028828931L;
/*    */   static final int ID = 1007;
/*    */   boolean dispatched;
/*    */   private AWTEvent nested;
/*    */   private AppContext toNotify;
/*    */ 
/*    */   SentEvent()
/*    */   {
/* 53 */     this(null);
/*    */   }
/*    */   SentEvent(AWTEvent paramAWTEvent) {
/* 56 */     this(paramAWTEvent, null);
/*    */   }
/*    */   SentEvent(AWTEvent paramAWTEvent, AppContext paramAppContext) {
/* 59 */     super(paramAWTEvent != null ? paramAWTEvent.getSource() : Toolkit.getDefaultToolkit(), 1007);
/*    */ 
/* 63 */     this.nested = paramAWTEvent;
/* 64 */     this.toNotify = paramAppContext;
/*    */   }
/*    */ 
/*    */   public void dispatch() {
/*    */     try {
/* 69 */       if (this.nested != null)
/* 70 */         Toolkit.getEventQueue().dispatchEvent(this.nested);
/*    */     }
/*    */     finally {
/* 73 */       this.dispatched = true;
/* 74 */       if (this.toNotify != null) {
/* 75 */         SunToolkit.postEvent(this.toNotify, new SentEvent());
/*    */       }
/* 77 */       synchronized (this) {
/* 78 */         notifyAll();
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/* 83 */   final void dispose() { this.dispatched = true;
/* 84 */     if (this.toNotify != null) {
/* 85 */       SunToolkit.postEvent(this.toNotify, new SentEvent());
/*    */     }
/* 87 */     synchronized (this) {
/* 88 */       notifyAll();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.SentEvent
 * JD-Core Version:    0.6.2
 */
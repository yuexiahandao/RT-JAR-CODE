/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Scrollbar;
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.peer.ScrollbarPeer;
/*     */ 
/*     */ class WScrollbarPeer extends WComponentPeer
/*     */   implements ScrollbarPeer
/*     */ {
/* 111 */   private boolean dragInProgress = false;
/*     */ 
/*     */   static native int getScrollbarSize(int paramInt);
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/*  40 */     if (((Scrollbar)this.target).getOrientation() == 1) {
/*  41 */       return new Dimension(getScrollbarSize(1), 50);
/*     */     }
/*     */ 
/*  44 */     return new Dimension(50, getScrollbarSize(0));
/*     */   }
/*     */ 
/*     */   public native void setValues(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public native void setLineIncrement(int paramInt);
/*     */ 
/*     */   public native void setPageIncrement(int paramInt);
/*     */ 
/*     */   WScrollbarPeer(Scrollbar paramScrollbar)
/*     */   {
/*  59 */     super(paramScrollbar);
/*     */   }
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   void initialize() {
/*  65 */     Scrollbar localScrollbar = (Scrollbar)this.target;
/*  66 */     setValues(localScrollbar.getValue(), localScrollbar.getVisibleAmount(), localScrollbar.getMinimum(), localScrollbar.getMaximum());
/*     */ 
/*  68 */     super.initialize();
/*     */   }
/*     */ 
/*     */   private void postAdjustmentEvent(final int paramInt1, final int paramInt2, final boolean paramBoolean)
/*     */   {
/*  78 */     final Scrollbar localScrollbar = (Scrollbar)this.target;
/*  79 */     WToolkit.executeOnEventHandlerThread(localScrollbar, new Runnable() {
/*     */       public void run() {
/*  81 */         localScrollbar.setValueIsAdjusting(paramBoolean);
/*  82 */         localScrollbar.setValue(paramInt2);
/*  83 */         WScrollbarPeer.this.postEvent(new AdjustmentEvent(localScrollbar, 601, paramInt1, paramInt2, paramBoolean));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   void lineUp(int paramInt)
/*     */   {
/*  91 */     postAdjustmentEvent(2, paramInt, false);
/*     */   }
/*     */ 
/*     */   void lineDown(int paramInt) {
/*  95 */     postAdjustmentEvent(1, paramInt, false);
/*     */   }
/*     */ 
/*     */   void pageUp(int paramInt) {
/*  99 */     postAdjustmentEvent(3, paramInt, false);
/*     */   }
/*     */ 
/*     */   void pageDown(int paramInt) {
/* 103 */     postAdjustmentEvent(4, paramInt, false);
/*     */   }
/*     */ 
/*     */   void warp(int paramInt)
/*     */   {
/* 108 */     postAdjustmentEvent(5, paramInt, false);
/*     */   }
/*     */ 
/*     */   void drag(int paramInt)
/*     */   {
/* 114 */     if (!this.dragInProgress) {
/* 115 */       this.dragInProgress = true;
/*     */     }
/* 117 */     postAdjustmentEvent(5, paramInt, true);
/*     */   }
/*     */ 
/*     */   void dragEnd(final int paramInt) {
/* 121 */     final Scrollbar localScrollbar = (Scrollbar)this.target;
/*     */ 
/* 123 */     if (!this.dragInProgress) {
/* 124 */       return;
/*     */     }
/*     */ 
/* 127 */     this.dragInProgress = false;
/* 128 */     WToolkit.executeOnEventHandlerThread(localScrollbar, new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 132 */         localScrollbar.setValueIsAdjusting(false);
/* 133 */         WScrollbarPeer.this.postEvent(new AdjustmentEvent(localScrollbar, 601, 5, paramInt, false));
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   public boolean shouldClearRectBeforePaint()
/*     */   {
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   public Dimension minimumSize()
/*     */   {
/* 148 */     return getMinimumSize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WScrollbarPeer
 * JD-Core Version:    0.6.2
 */
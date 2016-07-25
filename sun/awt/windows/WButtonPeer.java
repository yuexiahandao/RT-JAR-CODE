/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Button;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.peer.ButtonPeer;
/*     */ 
/*     */ class WButtonPeer extends WComponentPeer
/*     */   implements ButtonPeer
/*     */ {
/*     */   public Dimension getMinimumSize()
/*     */   {
/*  41 */     FontMetrics localFontMetrics = getFontMetrics(((Button)this.target).getFont());
/*  42 */     String str = ((Button)this.target).getLabel();
/*  43 */     if (str == null) {
/*  44 */       str = "";
/*     */     }
/*  46 */     return new Dimension(localFontMetrics.stringWidth(str) + 14, localFontMetrics.getHeight() + 8);
/*     */   }
/*     */ 
/*     */   public boolean isFocusable() {
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */   public native void setLabel(String paramString);
/*     */ 
/*     */   WButtonPeer(Button paramButton)
/*     */   {
/*  60 */     super(paramButton);
/*     */   }
/*     */ 
/*     */   native void create(WComponentPeer paramWComponentPeer);
/*     */ 
/*     */   public void handleAction(final long paramLong, int paramInt)
/*     */   {
/*  72 */     WToolkit.executeOnEventHandlerThread(this.target, new Runnable() {
/*     */       public void run() {
/*  74 */         WButtonPeer.this.postEvent(new ActionEvent(WButtonPeer.this.target, 1001, ((Button)WButtonPeer.this.target).getActionCommand(), paramLong, this.val$modifiers));
/*     */       }
/*     */     }
/*     */     , paramLong);
/*     */   }
/*     */ 
/*     */   public boolean shouldClearRectBeforePaint()
/*     */   {
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */   public Dimension minimumSize()
/*     */   {
/*  90 */     return getMinimumSize();
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public boolean handleJavaKeyEvent(KeyEvent paramKeyEvent)
/*     */   {
/*  99 */     switch (paramKeyEvent.getID()) {
/*     */     case 402:
/* 101 */       if (paramKeyEvent.getKeyCode() == 32) {
/* 102 */         handleAction(paramKeyEvent.getWhen(), paramKeyEvent.getModifiers());
/*     */       }
/*     */       break;
/*     */     }
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  35 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WButtonPeer
 * JD-Core Version:    0.6.2
 */
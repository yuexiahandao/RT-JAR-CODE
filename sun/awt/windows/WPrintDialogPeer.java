/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Event;
/*     */ import java.awt.Font;
/*     */ import java.awt.Window;
/*     */ import java.awt.dnd.DropTarget;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.peer.ComponentPeer;
/*     */ import java.awt.peer.DialogPeer;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.ComponentAccessor;
/*     */ import sun.awt.CausedFocusEvent.Cause;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class WPrintDialogPeer extends WWindowPeer
/*     */   implements DialogPeer
/*     */ {
/*     */   private WComponentPeer parent;
/*  45 */   private Vector<WWindowPeer> blockedWindows = new Vector();
/*     */ 
/*     */   WPrintDialogPeer(WPrintDialog paramWPrintDialog) {
/*  48 */     super(paramWPrintDialog);
/*     */   }
/*     */ 
/*     */   void create(WComponentPeer paramWComponentPeer) {
/*  52 */     this.parent = paramWComponentPeer;
/*     */   }
/*     */ 
/*     */   protected void checkCreation()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void disposeImpl()
/*     */   {
/*  61 */     WToolkit.targetDisposedPeer(this.target, this);
/*     */   }
/*     */ 
/*     */   private native boolean _show();
/*     */ 
/*     */   public void show() {
/*  67 */     new Thread(new Runnable() {
/*     */       public void run() {
/*     */         try {
/*  70 */           ((WPrintDialog)WPrintDialogPeer.this.target).setRetVal(WPrintDialogPeer.this._show());
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */ 
/*  76 */         ((WPrintDialog)WPrintDialogPeer.this.target).hide();
/*     */       }
/*     */     }).start();
/*     */   }
/*     */ 
/*     */   synchronized void setHWnd(long paramLong)
/*     */   {
/*  82 */     this.hwnd = paramLong;
/*  83 */     for (WWindowPeer localWWindowPeer : this.blockedWindows)
/*  84 */       if (paramLong != 0L)
/*  85 */         localWWindowPeer.modalDisable((Dialog)this.target, paramLong);
/*     */       else
/*  87 */         localWWindowPeer.modalEnable((Dialog)this.target);
/*     */   }
/*     */ 
/*     */   synchronized void blockWindow(WWindowPeer paramWWindowPeer)
/*     */   {
/*  93 */     this.blockedWindows.add(paramWWindowPeer);
/*  94 */     if (this.hwnd != 0L)
/*  95 */       paramWWindowPeer.modalDisable((Dialog)this.target, this.hwnd);
/*     */   }
/*     */ 
/*     */   synchronized void unblockWindow(WWindowPeer paramWWindowPeer) {
/*  99 */     this.blockedWindows.remove(paramWWindowPeer);
/* 100 */     if (this.hwnd != 0L)
/* 101 */       paramWWindowPeer.modalEnable((Dialog)this.target);
/*     */   }
/*     */ 
/*     */   public void blockWindows(List<Window> paramList)
/*     */   {
/* 106 */     for (Window localWindow : paramList) {
/* 107 */       WWindowPeer localWWindowPeer = (WWindowPeer)AWTAccessor.getComponentAccessor().getPeer(localWindow);
/* 108 */       if (localWWindowPeer != null)
/* 109 */         blockWindow(localWWindowPeer); 
/*     */     }
/*     */   }
/*     */   public native void toFront();
/*     */ 
/*     */   public native void toBack();
/*     */ 
/*     */   void initialize() {
/*     */   }
/*     */   public void updateAlwaysOnTopState() {
/*     */   }
/*     */   public void setResizable(boolean paramBoolean) {
/*     */   }
/*     */   public void hide() {  }
/*     */ 
/*     */   public void enable() {  }
/*     */ 
/*     */   public void disable() {  } 
/*     */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {  } 
/* 126 */   public boolean handleEvent(Event paramEvent) { return false; } 
/*     */   public void setForeground(Color paramColor) {
/*     */   }
/*     */   public void setBackground(Color paramColor) {  } 
/*     */   public void setFont(Font paramFont) {  } 
/*     */   public void updateMinimumSize() {  } 
/*     */   public void updateIconImages() {  } 
/* 133 */   public boolean requestFocus(boolean paramBoolean1, boolean paramBoolean2) { return false; }
/*     */ 
/*     */ 
/*     */   public boolean requestFocus(Component paramComponent, boolean paramBoolean1, boolean paramBoolean2, long paramLong, CausedFocusEvent.Cause paramCause)
/*     */   {
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   public void updateFocusableWindowState()
/*     */   {
/*     */   }
/*     */ 
/*     */   void start()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void beginValidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endValidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   void invalidate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addDropTarget(DropTarget paramDropTarget)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removeDropTarget(DropTarget paramDropTarget)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setZOrder(ComponentPeer paramComponentPeer)
/*     */   {
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public void applyShape(Region paramRegion)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setOpacity(float paramFloat)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setOpaque(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void updateWindow(BufferedImage paramBufferedImage)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void createScreenSurface(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void replaceSurfaceData()
/*     */   {
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  40 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WPrintDialogPeer
 * JD-Core Version:    0.6.2
 */
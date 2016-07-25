/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Dialog;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Rectangle;
/*    */ import sun.awt.EmbeddedFrame;
/*    */ import sun.awt.Win32GraphicsEnvironment;
/*    */ 
/*    */ public class WEmbeddedFramePeer extends WFramePeer
/*    */ {
/*    */   public WEmbeddedFramePeer(EmbeddedFrame paramEmbeddedFrame)
/*    */   {
/* 38 */     super(paramEmbeddedFrame);
/*    */   }
/*    */ 
/*    */   native void create(WComponentPeer paramWComponentPeer);
/*    */ 
/*    */   public void print(Graphics paramGraphics)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void updateMinimumSize()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void modalDisable(Dialog paramDialog, long paramLong) {
/* 52 */     super.modalDisable(paramDialog, paramLong);
/* 53 */     ((EmbeddedFrame)this.target).notifyModalBlocked(paramDialog, true);
/*    */   }
/*    */ 
/*    */   public void modalEnable(Dialog paramDialog)
/*    */   {
/* 58 */     super.modalEnable(paramDialog);
/* 59 */     ((EmbeddedFrame)this.target).notifyModalBlocked(paramDialog, false);
/*    */   }
/*    */ 
/*    */   public void setBoundsPrivate(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 63 */     setBounds(paramInt1, paramInt2, paramInt3, paramInt4, 16387);
/*    */   }
/*    */ 
/*    */   public native Rectangle getBoundsPrivate();
/*    */ 
/*    */   public native void synthesizeWmActivate(boolean paramBoolean);
/*    */ 
/*    */   public boolean isAccelCapable()
/*    */   {
/* 76 */     return !Win32GraphicsEnvironment.isDWMCompositionEnabled();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WEmbeddedFramePeer
 * JD-Core Version:    0.6.2
 */
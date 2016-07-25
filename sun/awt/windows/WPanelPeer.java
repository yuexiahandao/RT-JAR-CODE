/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Insets;
/*    */ import java.awt.peer.PanelPeer;
/*    */ import sun.awt.SunGraphicsCallback.PaintHeavyweightComponentsCallback;
/*    */ import sun.awt.SunGraphicsCallback.PrintHeavyweightComponentsCallback;
/*    */ 
/*    */ class WPanelPeer extends WCanvasPeer
/*    */   implements PanelPeer
/*    */ {
/*    */   Insets insets_;
/*    */ 
/*    */   public void paint(Graphics paramGraphics)
/*    */   {
/* 39 */     super.paint(paramGraphics);
/* 40 */     SunGraphicsCallback.PaintHeavyweightComponentsCallback.getInstance().runComponents(((Container)this.target).getComponents(), paramGraphics, 3);
/*    */   }
/*    */ 
/*    */   public void print(Graphics paramGraphics)
/*    */   {
/* 46 */     super.print(paramGraphics);
/* 47 */     SunGraphicsCallback.PrintHeavyweightComponentsCallback.getInstance().runComponents(((Container)this.target).getComponents(), paramGraphics, 3);
/*    */   }
/*    */ 
/*    */   public Insets getInsets()
/*    */   {
/* 56 */     return this.insets_;
/*    */   }
/*    */ 
/*    */   private static native void initIDs();
/*    */ 
/*    */   WPanelPeer(Component paramComponent)
/*    */   {
/* 73 */     super(paramComponent);
/*    */   }
/*    */ 
/*    */   void initialize() {
/* 77 */     super.initialize();
/* 78 */     this.insets_ = new Insets(0, 0, 0, 0);
/*    */ 
/* 80 */     Color localColor = ((Component)this.target).getBackground();
/* 81 */     if (localColor == null) {
/* 82 */       localColor = WColor.getDefaultColor(1);
/* 83 */       ((Component)this.target).setBackground(localColor);
/* 84 */       setBackground(localColor);
/*    */     }
/* 86 */     localColor = ((Component)this.target).getForeground();
/* 87 */     if (localColor == null) {
/* 88 */       localColor = WColor.getDefaultColor(2);
/* 89 */       ((Component)this.target).setForeground(localColor);
/* 90 */       setForeground(localColor);
/*    */     }
/*    */   }
/*    */ 
/*    */   public Insets insets()
/*    */   {
/* 98 */     return getInsets();
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 64 */     initIDs();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WPanelPeer
 * JD-Core Version:    0.6.2
 */
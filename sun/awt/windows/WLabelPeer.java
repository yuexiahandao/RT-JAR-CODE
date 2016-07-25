/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Component;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.FontMetrics;
/*    */ import java.awt.Label;
/*    */ import java.awt.peer.LabelPeer;
/*    */ 
/*    */ class WLabelPeer extends WComponentPeer
/*    */   implements LabelPeer
/*    */ {
/*    */   public Dimension getMinimumSize()
/*    */   {
/* 35 */     FontMetrics localFontMetrics = getFontMetrics(((Label)this.target).getFont());
/* 36 */     String str = ((Label)this.target).getText();
/* 37 */     if (str == null)
/* 38 */       str = "";
/* 39 */     return new Dimension(localFontMetrics.stringWidth(str) + 14, localFontMetrics.getHeight() + 8);
/*    */   }
/*    */   native void lazyPaint();
/*    */ 
/*    */   synchronized void start() {
/* 44 */     super.start();
/*    */ 
/* 46 */     lazyPaint();
/*    */   }
/*    */ 
/*    */   public boolean shouldClearRectBeforePaint()
/*    */   {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   public native void setText(String paramString);
/*    */ 
/*    */   public native void setAlignment(int paramInt);
/*    */ 
/*    */   WLabelPeer(Label paramLabel)
/*    */   {
/* 60 */     super(paramLabel);
/*    */   }
/*    */ 
/*    */   native void create(WComponentPeer paramWComponentPeer);
/*    */ 
/*    */   void initialize() {
/* 66 */     Label localLabel = (Label)this.target;
/*    */ 
/* 68 */     String str = localLabel.getText();
/* 69 */     if (str != null) {
/* 70 */       setText(str);
/*    */     }
/*    */ 
/* 73 */     int i = localLabel.getAlignment();
/* 74 */     if (i != 0) {
/* 75 */       setAlignment(i);
/*    */     }
/*    */ 
/* 78 */     Color localColor = ((Component)this.target).getBackground();
/* 79 */     if (localColor != null) {
/* 80 */       setBackground(localColor);
/*    */     }
/*    */ 
/* 83 */     super.initialize();
/*    */   }
/*    */ 
/*    */   public Dimension minimumSize()
/*    */   {
/* 90 */     return getMinimumSize();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WLabelPeer
 * JD-Core Version:    0.6.2
 */
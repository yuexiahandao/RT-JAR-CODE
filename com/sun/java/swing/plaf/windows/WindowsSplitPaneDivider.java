/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JSplitPane;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.basic.BasicSplitPaneDivider;
/*    */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*    */ 
/*    */ public class WindowsSplitPaneDivider extends BasicSplitPaneDivider
/*    */ {
/*    */   public WindowsSplitPaneDivider(BasicSplitPaneUI paramBasicSplitPaneUI)
/*    */   {
/* 54 */     super(paramBasicSplitPaneUI);
/*    */   }
/*    */ 
/*    */   public void paint(Graphics paramGraphics)
/*    */   {
/* 61 */     Color localColor = this.splitPane.hasFocus() ? UIManager.getColor("SplitPane.shadow") : getBackground();
/*    */ 
/* 64 */     Dimension localDimension = getSize();
/*    */ 
/* 66 */     if (localColor != null) {
/* 67 */       paramGraphics.setColor(localColor);
/* 68 */       paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);
/*    */     }
/* 70 */     super.paint(paramGraphics);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsSplitPaneDivider
 * JD-Core Version:    0.6.2
 */
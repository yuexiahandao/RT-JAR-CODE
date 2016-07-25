/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Font;
/*    */ import java.awt.FontMetrics;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicPopupMenuSeparatorUI;
/*    */ 
/*    */ public class WindowsPopupMenuSeparatorUI extends BasicPopupMenuSeparatorUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 48 */     return new WindowsPopupMenuSeparatorUI();
/*    */   }
/*    */ 
/*    */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/* 52 */     Dimension localDimension = paramJComponent.getSize();
/* 53 */     XPStyle localXPStyle = XPStyle.getXP();
/*    */     int i;
/* 54 */     if (WindowsMenuItemUI.isVistaPainting(localXPStyle)) {
/* 55 */       i = 1;
/* 56 */       Container localContainer = paramJComponent.getParent();
/* 57 */       if ((localContainer instanceof JComponent)) {
/* 58 */         localObject = ((JComponent)localContainer).getClientProperty(WindowsPopupMenuUI.GUTTER_OFFSET_KEY);
/*    */ 
/* 61 */         if ((localObject instanceof Integer))
/*    */         {
/* 67 */           i = ((Integer)localObject).intValue() - paramJComponent.getX();
/* 68 */           i += WindowsPopupMenuUI.getGutterWidth();
/*    */         }
/*    */       }
/* 71 */       Object localObject = localXPStyle.getSkin(paramJComponent, TMSchema.Part.MP_POPUPSEPARATOR);
/* 72 */       int j = ((XPStyle.Skin)localObject).getHeight();
/* 73 */       int k = (localDimension.height - j) / 2;
/* 74 */       ((XPStyle.Skin)localObject).paintSkin(paramGraphics, i, k, localDimension.width - i - 1, j, TMSchema.State.NORMAL);
/*    */     } else {
/* 76 */       i = localDimension.height / 2;
/* 77 */       paramGraphics.setColor(paramJComponent.getForeground());
/* 78 */       paramGraphics.drawLine(1, i - 1, localDimension.width - 2, i - 1);
/*    */ 
/* 80 */       paramGraphics.setColor(paramJComponent.getBackground());
/* 81 */       paramGraphics.drawLine(1, i, localDimension.width - 2, i);
/*    */     }
/*    */   }
/*    */ 
/*    */   public Dimension getPreferredSize(JComponent paramJComponent) {
/* 86 */     int i = 0;
/* 87 */     Font localFont = paramJComponent.getFont();
/* 88 */     if (localFont != null) {
/* 89 */       i = paramJComponent.getFontMetrics(localFont).getHeight();
/*    */     }
/*    */ 
/* 92 */     return new Dimension(0, i / 2 + 2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsPopupMenuSeparatorUI
 * JD-Core Version:    0.6.2
 */
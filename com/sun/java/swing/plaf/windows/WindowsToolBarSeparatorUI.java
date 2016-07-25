/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JSeparator;
/*    */ import javax.swing.JToolBar.Separator;
/*    */ import javax.swing.UIDefaults;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicToolBarSeparatorUI;
/*    */ 
/*    */ public class WindowsToolBarSeparatorUI extends BasicToolBarSeparatorUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 47 */     return new WindowsToolBarSeparatorUI();
/*    */   }
/*    */ 
/*    */   public Dimension getPreferredSize(JComponent paramJComponent) {
/* 51 */     Dimension localDimension = ((JToolBar.Separator)paramJComponent).getSeparatorSize();
/*    */ 
/* 53 */     if (localDimension != null) {
/* 54 */       localDimension = localDimension.getSize();
/*    */     } else {
/* 56 */       localDimension = new Dimension(6, 6);
/* 57 */       XPStyle localXPStyle = XPStyle.getXP();
/* 58 */       if (localXPStyle != null) {
/* 59 */         int i = ((JSeparator)paramJComponent).getOrientation() == 1 ? 1 : 0;
/* 60 */         TMSchema.Part localPart = i != 0 ? TMSchema.Part.TP_SEPARATOR : TMSchema.Part.TP_SEPARATORVERT;
/* 61 */         XPStyle.Skin localSkin = localXPStyle.getSkin(paramJComponent, localPart);
/* 62 */         localDimension.width = localSkin.getWidth();
/* 63 */         localDimension.height = localSkin.getHeight();
/*    */       }
/*    */ 
/* 66 */       if (((JSeparator)paramJComponent).getOrientation() == 1)
/* 67 */         localDimension.height = 0;
/*    */       else {
/* 69 */         localDimension.width = 0;
/*    */       }
/*    */     }
/* 72 */     return localDimension;
/*    */   }
/*    */ 
/*    */   public Dimension getMaximumSize(JComponent paramJComponent) {
/* 76 */     Dimension localDimension = getPreferredSize(paramJComponent);
/* 77 */     if (((JSeparator)paramJComponent).getOrientation() == 1) {
/* 78 */       return new Dimension(localDimension.width, 32767);
/*    */     }
/* 80 */     return new Dimension(32767, localDimension.height);
/*    */   }
/*    */ 
/*    */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*    */   {
/* 85 */     int i = ((JSeparator)paramJComponent).getOrientation() == 1 ? 1 : 0;
/* 86 */     Dimension localDimension = paramJComponent.getSize();
/*    */ 
/* 88 */     XPStyle localXPStyle = XPStyle.getXP();
/*    */     Object localObject1;
/*    */     Object localObject2;
/*    */     int m;
/* 89 */     if (localXPStyle != null) {
/* 90 */       localObject1 = i != 0 ? TMSchema.Part.TP_SEPARATOR : TMSchema.Part.TP_SEPARATORVERT;
/* 91 */       localObject2 = localXPStyle.getSkin(paramJComponent, (TMSchema.Part)localObject1);
/*    */ 
/* 93 */       int j = i != 0 ? (localDimension.width - ((XPStyle.Skin)localObject2).getWidth()) / 2 : 0;
/* 94 */       int k = i != 0 ? 0 : (localDimension.height - ((XPStyle.Skin)localObject2).getHeight()) / 2;
/* 95 */       m = i != 0 ? ((XPStyle.Skin)localObject2).getWidth() : localDimension.width;
/* 96 */       int n = i != 0 ? localDimension.height : ((XPStyle.Skin)localObject2).getHeight();
/* 97 */       ((XPStyle.Skin)localObject2).paintSkin(paramGraphics, j, k, m, n, null);
/*    */     }
/*    */     else {
/* 100 */       localObject1 = paramGraphics.getColor();
/*    */ 
/* 102 */       localObject2 = UIManager.getLookAndFeelDefaults();
/*    */ 
/* 104 */       Color localColor1 = ((UIDefaults)localObject2).getColor("ToolBar.shadow");
/* 105 */       Color localColor2 = ((UIDefaults)localObject2).getColor("ToolBar.highlight");
/*    */ 
/* 107 */       if (i != 0) {
/* 108 */         m = localDimension.width / 2 - 1;
/* 109 */         paramGraphics.setColor(localColor1);
/* 110 */         paramGraphics.drawLine(m, 2, m, localDimension.height - 2);
/*    */ 
/* 112 */         paramGraphics.setColor(localColor2);
/* 113 */         paramGraphics.drawLine(m + 1, 2, m + 1, localDimension.height - 2);
/*    */       } else {
/* 115 */         m = localDimension.height / 2 - 1;
/* 116 */         paramGraphics.setColor(localColor1);
/* 117 */         paramGraphics.drawLine(2, m, localDimension.width - 2, m);
/* 118 */         paramGraphics.setColor(localColor2);
/* 119 */         paramGraphics.drawLine(2, m + 1, localDimension.width - 2, m + 1);
/*    */       }
/* 121 */       paramGraphics.setColor((Color)localObject1);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsToolBarSeparatorUI
 * JD-Core Version:    0.6.2
 */
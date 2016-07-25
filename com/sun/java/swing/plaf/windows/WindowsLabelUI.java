/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicLabelUI;
/*    */ import sun.awt.AppContext;
/*    */ import sun.swing.SwingUtilities2;
/*    */ 
/*    */ public class WindowsLabelUI extends BasicLabelUI
/*    */ {
/* 56 */   private static final Object WINDOWS_LABEL_UI_KEY = new Object();
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 62 */     AppContext localAppContext = AppContext.getAppContext();
/* 63 */     WindowsLabelUI localWindowsLabelUI = (WindowsLabelUI)localAppContext.get(WINDOWS_LABEL_UI_KEY);
/*    */ 
/* 65 */     if (localWindowsLabelUI == null) {
/* 66 */       localWindowsLabelUI = new WindowsLabelUI();
/* 67 */       localAppContext.put(WINDOWS_LABEL_UI_KEY, localWindowsLabelUI);
/*    */     }
/* 69 */     return localWindowsLabelUI;
/*    */   }
/*    */ 
/*    */   protected void paintEnabledText(JLabel paramJLabel, Graphics paramGraphics, String paramString, int paramInt1, int paramInt2)
/*    */   {
/* 74 */     int i = paramJLabel.getDisplayedMnemonicIndex();
/*    */ 
/* 76 */     if (WindowsLookAndFeel.isMnemonicHidden() == true) {
/* 77 */       i = -1;
/*    */     }
/*    */ 
/* 80 */     paramGraphics.setColor(paramJLabel.getForeground());
/* 81 */     SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   protected void paintDisabledText(JLabel paramJLabel, Graphics paramGraphics, String paramString, int paramInt1, int paramInt2)
/*    */   {
/* 87 */     int i = paramJLabel.getDisplayedMnemonicIndex();
/*    */ 
/* 89 */     if (WindowsLookAndFeel.isMnemonicHidden() == true) {
/* 90 */       i = -1;
/*    */     }
/* 92 */     if (((UIManager.getColor("Label.disabledForeground") instanceof Color)) && ((UIManager.getColor("Label.disabledShadow") instanceof Color)))
/*    */     {
/* 94 */       paramGraphics.setColor(UIManager.getColor("Label.disabledShadow"));
/* 95 */       SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1 + 1, paramInt2 + 1);
/*    */ 
/* 98 */       paramGraphics.setColor(UIManager.getColor("Label.disabledForeground"));
/* 99 */       SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1, paramInt2);
/*    */     }
/*    */     else
/*    */     {
/* 103 */       Color localColor = paramJLabel.getBackground();
/* 104 */       paramGraphics.setColor(localColor.brighter());
/* 105 */       SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1 + 1, paramInt2 + 1);
/*    */ 
/* 107 */       paramGraphics.setColor(localColor.darker());
/* 108 */       SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1, paramInt2);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsLabelUI
 * JD-Core Version:    0.6.2
 */
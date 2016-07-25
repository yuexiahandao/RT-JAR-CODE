/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JInternalFrame.JDesktopIcon;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicDesktopIconUI;
/*    */ 
/*    */ public class WindowsDesktopIconUI extends BasicDesktopIconUI
/*    */ {
/*    */   private int width;
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 50 */     return new WindowsDesktopIconUI();
/*    */   }
/*    */ 
/*    */   public void installDefaults() {
/* 54 */     super.installDefaults();
/* 55 */     this.width = UIManager.getInt("DesktopIcon.width");
/*    */   }
/*    */ 
/*    */   public void installUI(JComponent paramJComponent) {
/* 59 */     super.installUI(paramJComponent);
/*    */ 
/* 61 */     paramJComponent.setOpaque(XPStyle.getXP() == null);
/*    */   }
/*    */ 
/*    */   public void uninstallUI(JComponent paramJComponent)
/*    */   {
/* 66 */     WindowsInternalFrameTitlePane localWindowsInternalFrameTitlePane = (WindowsInternalFrameTitlePane)this.iconPane;
/*    */ 
/* 68 */     super.uninstallUI(paramJComponent);
/* 69 */     localWindowsInternalFrameTitlePane.uninstallListeners();
/*    */   }
/*    */ 
/*    */   protected void installComponents() {
/* 73 */     this.iconPane = new WindowsInternalFrameTitlePane(this.frame);
/* 74 */     this.desktopIcon.setLayout(new BorderLayout());
/* 75 */     this.desktopIcon.add(this.iconPane, "Center");
/*    */ 
/* 77 */     if (XPStyle.getXP() != null)
/* 78 */       this.desktopIcon.setBorder(null);
/*    */   }
/*    */ 
/*    */   public Dimension getPreferredSize(JComponent paramJComponent)
/*    */   {
/* 86 */     return getMinimumSize(paramJComponent);
/*    */   }
/*    */ 
/*    */   public Dimension getMinimumSize(JComponent paramJComponent)
/*    */   {
/* 94 */     Dimension localDimension = super.getMinimumSize(paramJComponent);
/* 95 */     localDimension.width = this.width;
/* 96 */     return localDimension;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsDesktopIconUI
 * JD-Core Version:    0.6.2
 */
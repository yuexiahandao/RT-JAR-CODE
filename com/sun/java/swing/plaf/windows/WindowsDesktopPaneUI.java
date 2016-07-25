/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JDesktopPane;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicDesktopPaneUI;
/*    */ 
/*    */ public class WindowsDesktopPaneUI extends BasicDesktopPaneUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 48 */     return new WindowsDesktopPaneUI();
/*    */   }
/*    */ 
/*    */   protected void installDesktopManager() {
/* 52 */     this.desktopManager = this.desktop.getDesktopManager();
/* 53 */     if (this.desktopManager == null) {
/* 54 */       this.desktopManager = new WindowsDesktopManager();
/* 55 */       this.desktop.setDesktopManager(this.desktopManager);
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void installDefaults() {
/* 60 */     super.installDefaults();
/*    */   }
/*    */ 
/*    */   protected void installKeyboardActions() {
/* 64 */     super.installKeyboardActions();
/*    */ 
/* 67 */     if (!this.desktop.requestDefaultFocus())
/* 68 */       this.desktop.requestFocus();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsDesktopPaneUI
 * JD-Core Version:    0.6.2
 */
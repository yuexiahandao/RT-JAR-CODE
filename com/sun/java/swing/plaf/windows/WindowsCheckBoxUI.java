/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import sun.awt.AppContext;
/*    */ 
/*    */ public class WindowsCheckBoxUI extends WindowsRadioButtonUI
/*    */ {
/* 54 */   private static final Object WINDOWS_CHECK_BOX_UI_KEY = new Object();
/*    */   private static final String propertyPrefix = "CheckBox.";
/* 58 */   private boolean defaults_initialized = false;
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 64 */     AppContext localAppContext = AppContext.getAppContext();
/* 65 */     WindowsCheckBoxUI localWindowsCheckBoxUI = (WindowsCheckBoxUI)localAppContext.get(WINDOWS_CHECK_BOX_UI_KEY);
/*    */ 
/* 67 */     if (localWindowsCheckBoxUI == null) {
/* 68 */       localWindowsCheckBoxUI = new WindowsCheckBoxUI();
/* 69 */       localAppContext.put(WINDOWS_CHECK_BOX_UI_KEY, localWindowsCheckBoxUI);
/*    */     }
/* 71 */     return localWindowsCheckBoxUI;
/*    */   }
/*    */ 
/*    */   public String getPropertyPrefix()
/*    */   {
/* 76 */     return "CheckBox.";
/*    */   }
/*    */ 
/*    */   public void installDefaults(AbstractButton paramAbstractButton)
/*    */   {
/* 83 */     super.installDefaults(paramAbstractButton);
/* 84 */     if (!this.defaults_initialized) {
/* 85 */       this.icon = UIManager.getIcon(getPropertyPrefix() + "icon");
/* 86 */       this.defaults_initialized = true;
/*    */     }
/*    */   }
/*    */ 
/*    */   public void uninstallDefaults(AbstractButton paramAbstractButton) {
/* 91 */     super.uninstallDefaults(paramAbstractButton);
/* 92 */     this.defaults_initialized = false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsCheckBoxUI
 * JD-Core Version:    0.6.2
 */
/*    */ package com.sun.java.swing.plaf.motif;
/*    */ 
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import sun.awt.AppContext;
/*    */ 
/*    */ public class MotifCheckBoxUI extends MotifRadioButtonUI
/*    */ {
/* 50 */   private static final Object MOTIF_CHECK_BOX_UI_KEY = new Object();
/*    */   private static final String propertyPrefix = "CheckBox.";
/* 54 */   private boolean defaults_initialized = false;
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 61 */     AppContext localAppContext = AppContext.getAppContext();
/* 62 */     MotifCheckBoxUI localMotifCheckBoxUI = (MotifCheckBoxUI)localAppContext.get(MOTIF_CHECK_BOX_UI_KEY);
/*    */ 
/* 64 */     if (localMotifCheckBoxUI == null) {
/* 65 */       localMotifCheckBoxUI = new MotifCheckBoxUI();
/* 66 */       localAppContext.put(MOTIF_CHECK_BOX_UI_KEY, localMotifCheckBoxUI);
/*    */     }
/* 68 */     return localMotifCheckBoxUI;
/*    */   }
/*    */ 
/*    */   public String getPropertyPrefix() {
/* 72 */     return "CheckBox.";
/*    */   }
/*    */ 
/*    */   public void installDefaults(AbstractButton paramAbstractButton)
/*    */   {
/* 79 */     super.installDefaults(paramAbstractButton);
/* 80 */     if (!this.defaults_initialized) {
/* 81 */       this.icon = UIManager.getIcon(getPropertyPrefix() + "icon");
/* 82 */       this.defaults_initialized = true;
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/* 87 */     super.uninstallDefaults(paramAbstractButton);
/* 88 */     this.defaults_initialized = false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifCheckBoxUI
 * JD-Core Version:    0.6.2
 */
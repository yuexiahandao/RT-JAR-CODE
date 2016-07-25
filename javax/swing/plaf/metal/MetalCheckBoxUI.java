/*    */ package javax.swing.plaf.metal;
/*    */ 
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import sun.awt.AppContext;
/*    */ 
/*    */ public class MetalCheckBoxUI extends MetalRadioButtonUI
/*    */ {
/* 60 */   private static final Object METAL_CHECK_BOX_UI_KEY = new Object();
/*    */   private static final String propertyPrefix = "CheckBox.";
/* 64 */   private boolean defaults_initialized = false;
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 70 */     AppContext localAppContext = AppContext.getAppContext();
/* 71 */     MetalCheckBoxUI localMetalCheckBoxUI = (MetalCheckBoxUI)localAppContext.get(METAL_CHECK_BOX_UI_KEY);
/*    */ 
/* 73 */     if (localMetalCheckBoxUI == null) {
/* 74 */       localMetalCheckBoxUI = new MetalCheckBoxUI();
/* 75 */       localAppContext.put(METAL_CHECK_BOX_UI_KEY, localMetalCheckBoxUI);
/*    */     }
/* 77 */     return localMetalCheckBoxUI;
/*    */   }
/*    */ 
/*    */   public String getPropertyPrefix() {
/* 81 */     return "CheckBox.";
/*    */   }
/*    */ 
/*    */   public void installDefaults(AbstractButton paramAbstractButton)
/*    */   {
/* 88 */     super.installDefaults(paramAbstractButton);
/* 89 */     if (!this.defaults_initialized) {
/* 90 */       this.icon = UIManager.getIcon(getPropertyPrefix() + "icon");
/* 91 */       this.defaults_initialized = true;
/*    */     }
/*    */   }
/*    */ 
/*    */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/* 96 */     super.uninstallDefaults(paramAbstractButton);
/* 97 */     this.defaults_initialized = false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalCheckBoxUI
 * JD-Core Version:    0.6.2
 */
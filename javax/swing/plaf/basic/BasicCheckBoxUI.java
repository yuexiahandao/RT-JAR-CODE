/*    */ package javax.swing.plaf.basic;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import sun.awt.AppContext;
/*    */ 
/*    */ public class BasicCheckBoxUI extends BasicRadioButtonUI
/*    */ {
/* 54 */   private static final Object BASIC_CHECK_BOX_UI_KEY = new Object();
/*    */   private static final String propertyPrefix = "CheckBox.";
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 62 */     AppContext localAppContext = AppContext.getAppContext();
/* 63 */     BasicCheckBoxUI localBasicCheckBoxUI = (BasicCheckBoxUI)localAppContext.get(BASIC_CHECK_BOX_UI_KEY);
/*    */ 
/* 65 */     if (localBasicCheckBoxUI == null) {
/* 66 */       localBasicCheckBoxUI = new BasicCheckBoxUI();
/* 67 */       localAppContext.put(BASIC_CHECK_BOX_UI_KEY, localBasicCheckBoxUI);
/*    */     }
/* 69 */     return localBasicCheckBoxUI;
/*    */   }
/*    */ 
/*    */   public String getPropertyPrefix() {
/* 73 */     return "CheckBox.";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicCheckBoxUI
 * JD-Core Version:    0.6.2
 */
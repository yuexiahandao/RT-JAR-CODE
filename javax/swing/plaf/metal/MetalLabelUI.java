/*    */ package javax.swing.plaf.metal;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicLabelUI;
/*    */ import sun.awt.AppContext;
/*    */ import sun.swing.SwingUtilities2;
/*    */ 
/*    */ public class MetalLabelUI extends BasicLabelUI
/*    */ {
/* 55 */   protected static MetalLabelUI metalLabelUI = new MetalLabelUI();
/*    */ 
/* 57 */   private static final Object METAL_LABEL_UI_KEY = new Object();
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent) {
/* 60 */     if (System.getSecurityManager() != null) {
/* 61 */       AppContext localAppContext = AppContext.getAppContext();
/* 62 */       MetalLabelUI localMetalLabelUI = (MetalLabelUI)localAppContext.get(METAL_LABEL_UI_KEY);
/*    */ 
/* 64 */       if (localMetalLabelUI == null) {
/* 65 */         localMetalLabelUI = new MetalLabelUI();
/* 66 */         localAppContext.put(METAL_LABEL_UI_KEY, localMetalLabelUI);
/*    */       }
/* 68 */       return localMetalLabelUI;
/*    */     }
/* 70 */     return metalLabelUI;
/*    */   }
/*    */ 
/*    */   protected void paintDisabledText(JLabel paramJLabel, Graphics paramGraphics, String paramString, int paramInt1, int paramInt2)
/*    */   {
/* 82 */     int i = paramJLabel.getDisplayedMnemonicIndex();
/* 83 */     paramGraphics.setColor(UIManager.getColor("Label.disabledForeground"));
/* 84 */     SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1, paramInt2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalLabelUI
 * JD-Core Version:    0.6.2
 */
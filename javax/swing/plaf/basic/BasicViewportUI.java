/*    */ package javax.swing.plaf.basic;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.ViewportUI;
/*    */ 
/*    */ public class BasicViewportUI extends ViewportUI
/*    */ {
/*    */   private static ViewportUI viewportUI;
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 47 */     if (viewportUI == null) {
/* 48 */       viewportUI = new BasicViewportUI();
/*    */     }
/* 50 */     return viewportUI;
/*    */   }
/*    */ 
/*    */   public void installUI(JComponent paramJComponent) {
/* 54 */     super.installUI(paramJComponent);
/* 55 */     installDefaults(paramJComponent);
/*    */   }
/*    */ 
/*    */   public void uninstallUI(JComponent paramJComponent) {
/* 59 */     uninstallDefaults(paramJComponent);
/* 60 */     super.uninstallUI(paramJComponent);
/*    */   }
/*    */ 
/*    */   protected void installDefaults(JComponent paramJComponent) {
/* 64 */     LookAndFeel.installColorsAndFont(paramJComponent, "Viewport.background", "Viewport.foreground", "Viewport.font");
/*    */ 
/* 68 */     LookAndFeel.installProperty(paramJComponent, "opaque", Boolean.TRUE);
/*    */   }
/*    */ 
/*    */   protected void uninstallDefaults(JComponent paramJComponent)
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicViewportUI
 * JD-Core Version:    0.6.2
 */
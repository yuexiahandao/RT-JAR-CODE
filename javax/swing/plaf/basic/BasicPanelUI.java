/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Component.BaselineResizeBehavior;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.PanelUI;
/*     */ 
/*     */ public class BasicPanelUI extends PanelUI
/*     */ {
/*     */   private static PanelUI panelUI;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  47 */     if (panelUI == null) {
/*  48 */       panelUI = new BasicPanelUI();
/*     */     }
/*  50 */     return panelUI;
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/*  54 */     JPanel localJPanel = (JPanel)paramJComponent;
/*  55 */     super.installUI(localJPanel);
/*  56 */     installDefaults(localJPanel);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent) {
/*  60 */     JPanel localJPanel = (JPanel)paramJComponent;
/*  61 */     uninstallDefaults(localJPanel);
/*  62 */     super.uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JPanel paramJPanel) {
/*  66 */     LookAndFeel.installColorsAndFont(paramJPanel, "Panel.background", "Panel.foreground", "Panel.font");
/*     */ 
/*  70 */     LookAndFeel.installBorder(paramJPanel, "Panel.border");
/*  71 */     LookAndFeel.installProperty(paramJPanel, "opaque", Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JPanel paramJPanel) {
/*  75 */     LookAndFeel.uninstallBorder(paramJPanel);
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/*  88 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*  89 */     Border localBorder = paramJComponent.getBorder();
/*  90 */     if ((localBorder instanceof AbstractBorder)) {
/*  91 */       return ((AbstractBorder)localBorder).getBaseline(paramJComponent, paramInt1, paramInt2);
/*     */     }
/*  93 */     return -1;
/*     */   }
/*     */ 
/*     */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*     */   {
/* 106 */     super.getBaselineResizeBehavior(paramJComponent);
/* 107 */     Border localBorder = paramJComponent.getBorder();
/* 108 */     if ((localBorder instanceof AbstractBorder)) {
/* 109 */       return ((AbstractBorder)localBorder).getBaselineResizeBehavior(paramJComponent);
/*     */     }
/* 111 */     return Component.BaselineResizeBehavior.OTHER;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicPanelUI
 * JD-Core Version:    0.6.2
 */
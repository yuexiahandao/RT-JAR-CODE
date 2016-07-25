/*    */ package javax.swing.plaf.synth;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ 
/*    */ public class SynthToggleButtonUI extends SynthButtonUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 52 */     return new SynthToggleButtonUI();
/*    */   }
/*    */ 
/*    */   protected String getPropertyPrefix()
/*    */   {
/* 60 */     return "ToggleButton.";
/*    */   }
/*    */ 
/*    */   void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent)
/*    */   {
/* 65 */     if (((AbstractButton)paramJComponent).isContentAreaFilled()) {
/* 66 */       int i = 0; int j = 0; int k = paramJComponent.getWidth(); int m = paramJComponent.getHeight();
/* 67 */       SynthPainter localSynthPainter = paramSynthContext.getPainter();
/* 68 */       localSynthPainter.paintToggleButtonBackground(paramSynthContext, paramGraphics, i, j, k, m);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 78 */     paramSynthContext.getPainter().paintToggleButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthToggleButtonUI
 * JD-Core Version:    0.6.2
 */
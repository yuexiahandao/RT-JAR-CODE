/*    */ package javax.swing.plaf.synth;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.AbstractButton;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ 
/*    */ public class SynthRadioButtonUI extends SynthToggleButtonUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 51 */     return new SynthRadioButtonUI();
/*    */   }
/*    */ 
/*    */   protected String getPropertyPrefix()
/*    */   {
/* 59 */     return "RadioButton.";
/*    */   }
/*    */ 
/*    */   protected Icon getSizingIcon(AbstractButton paramAbstractButton)
/*    */   {
/* 68 */     return getIcon(paramAbstractButton);
/*    */   }
/*    */ 
/*    */   void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent)
/*    */   {
/* 73 */     paramSynthContext.getPainter().paintRadioButtonBackground(paramSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*    */   }
/*    */ 
/*    */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 83 */     paramSynthContext.getPainter().paintRadioButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthRadioButtonUI
 * JD-Core Version:    0.6.2
 */
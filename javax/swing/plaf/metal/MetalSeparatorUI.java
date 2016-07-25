/*    */ package javax.swing.plaf.metal;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JSeparator;
/*    */ import javax.swing.LookAndFeel;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicSeparatorUI;
/*    */ 
/*    */ public class MetalSeparatorUI extends BasicSeparatorUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 58 */     return new MetalSeparatorUI();
/*    */   }
/*    */ 
/*    */   protected void installDefaults(JSeparator paramJSeparator)
/*    */   {
/* 63 */     LookAndFeel.installColors(paramJSeparator, "Separator.background", "Separator.foreground");
/*    */   }
/*    */ 
/*    */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*    */   {
/* 68 */     Dimension localDimension = paramJComponent.getSize();
/*    */ 
/* 70 */     if (((JSeparator)paramJComponent).getOrientation() == 1)
/*    */     {
/* 72 */       paramGraphics.setColor(paramJComponent.getForeground());
/* 73 */       paramGraphics.drawLine(0, 0, 0, localDimension.height);
/*    */ 
/* 75 */       paramGraphics.setColor(paramJComponent.getBackground());
/* 76 */       paramGraphics.drawLine(1, 0, 1, localDimension.height);
/*    */     }
/*    */     else
/*    */     {
/* 80 */       paramGraphics.setColor(paramJComponent.getForeground());
/* 81 */       paramGraphics.drawLine(0, 0, localDimension.width, 0);
/*    */ 
/* 83 */       paramGraphics.setColor(paramJComponent.getBackground());
/* 84 */       paramGraphics.drawLine(0, 1, localDimension.width, 1);
/*    */     }
/*    */   }
/*    */ 
/*    */   public Dimension getPreferredSize(JComponent paramJComponent)
/*    */   {
/* 90 */     if (((JSeparator)paramJComponent).getOrientation() == 1) {
/* 91 */       return new Dimension(2, 0);
/*    */     }
/* 93 */     return new Dimension(0, 2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalSeparatorUI
 * JD-Core Version:    0.6.2
 */
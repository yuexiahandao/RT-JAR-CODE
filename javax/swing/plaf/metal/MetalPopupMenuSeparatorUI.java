/*    */ package javax.swing.plaf.metal;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ 
/*    */ public class MetalPopupMenuSeparatorUI extends MetalSeparatorUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 48 */     return new MetalPopupMenuSeparatorUI();
/*    */   }
/*    */ 
/*    */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*    */   {
/* 53 */     Dimension localDimension = paramJComponent.getSize();
/*    */ 
/* 55 */     paramGraphics.setColor(paramJComponent.getForeground());
/* 56 */     paramGraphics.drawLine(0, 1, localDimension.width, 1);
/*    */ 
/* 58 */     paramGraphics.setColor(paramJComponent.getBackground());
/* 59 */     paramGraphics.drawLine(0, 2, localDimension.width, 2);
/* 60 */     paramGraphics.drawLine(0, 0, 0, 0);
/* 61 */     paramGraphics.drawLine(0, 3, 0, 3);
/*    */   }
/*    */ 
/*    */   public Dimension getPreferredSize(JComponent paramJComponent)
/*    */   {
/* 66 */     return new Dimension(0, 4);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalPopupMenuSeparatorUI
 * JD-Core Version:    0.6.2
 */
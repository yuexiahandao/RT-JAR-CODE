/*    */ package javax.swing.plaf.basic;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JSeparator;
/*    */ import javax.swing.JToolBar.Separator;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.UIResource;
/*    */ 
/*    */ public class BasicToolBarSeparatorUI extends BasicSeparatorUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 51 */     return new BasicToolBarSeparatorUI();
/*    */   }
/*    */ 
/*    */   protected void installDefaults(JSeparator paramJSeparator)
/*    */   {
/* 56 */     Dimension localDimension = ((JToolBar.Separator)paramJSeparator).getSeparatorSize();
/*    */ 
/* 58 */     if ((localDimension == null) || ((localDimension instanceof UIResource)))
/*    */     {
/* 60 */       JToolBar.Separator localSeparator = (JToolBar.Separator)paramJSeparator;
/* 61 */       localDimension = (Dimension)UIManager.get("ToolBar.separatorSize");
/* 62 */       if (localDimension != null) {
/* 63 */         if (localSeparator.getOrientation() == 0) {
/* 64 */           localDimension = new Dimension(localDimension.height, localDimension.width);
/*    */         }
/* 66 */         localSeparator.setSeparatorSize(localDimension);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Dimension getPreferredSize(JComponent paramJComponent)
/*    */   {
/* 77 */     Dimension localDimension = ((JToolBar.Separator)paramJComponent).getSeparatorSize();
/*    */ 
/* 79 */     if (localDimension != null)
/*    */     {
/* 81 */       return localDimension.getSize();
/*    */     }
/*    */ 
/* 85 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicToolBarSeparatorUI
 * JD-Core Version:    0.6.2
 */
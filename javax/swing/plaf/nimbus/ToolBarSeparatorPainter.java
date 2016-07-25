/*    */ package javax.swing.plaf.nimbus;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ final class ToolBarSeparatorPainter extends AbstractRegionPainter
/*    */ {
/*    */   private static final int SPACE = 3;
/*    */   private static final int INSET = 2;
/*    */ 
/*    */   protected AbstractRegionPainter.PaintContext getPaintContext()
/*    */   {
/* 54 */     return new AbstractRegionPainter.PaintContext(new Insets(1, 0, 1, 0), new Dimension(38, 7), false, AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING, 1.0D, 1.0D);
/*    */   }
/*    */ 
/*    */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*    */   {
/* 65 */     paramGraphics2D.setColor(paramJComponent.getForeground());
/* 66 */     int i = paramInt2 / 2;
/* 67 */     for (int j = 2; j <= paramInt1 - 2; j += 3)
/* 68 */       paramGraphics2D.fillRect(j, i, 1, 1);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ToolBarSeparatorPainter
 * JD-Core Version:    0.6.2
 */
/*    */ package javax.swing.plaf.nimbus;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.geom.Ellipse2D;
/*    */ import java.awt.geom.Ellipse2D.Float;
/*    */ import java.awt.geom.Path2D;
/*    */ import java.awt.geom.Path2D.Float;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import java.awt.geom.Rectangle2D.Float;
/*    */ import java.awt.geom.RoundRectangle2D;
/*    */ import java.awt.geom.RoundRectangle2D.Float;
/*    */ import javax.swing.JComponent;
/*    */ 
/*    */ final class SeparatorPainter extends AbstractRegionPainter
/*    */ {
/*    */   static final int BACKGROUND_ENABLED = 1;
/*    */   private int state;
/*    */   private AbstractRegionPainter.PaintContext ctx;
/* 46 */   private Path2D path = new Path2D.Float();
/* 47 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/* 48 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/* 49 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*    */ 
/* 54 */   private Color color1 = decodeColor("nimbusBlueGrey", -0.00854701F, -0.0383041F, -0.03921568F, 0);
/*    */   private Object[] componentColors;
/*    */ 
/*    */   public SeparatorPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*    */   {
/* 62 */     this.state = paramInt;
/* 63 */     this.ctx = paramPaintContext;
/*    */   }
/*    */ 
/*    */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*    */   {
/* 69 */     this.componentColors = paramArrayOfObject;
/*    */ 
/* 72 */     switch (this.state) { case 1:
/* 73 */       paintBackgroundEnabled(paramGraphics2D);
/*    */     }
/*    */   }
/*    */ 
/*    */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*    */   {
/* 82 */     return this.ctx;
/*    */   }
/*    */ 
/*    */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D) {
/* 86 */     this.rect = decodeRect1();
/* 87 */     paramGraphics2D.setPaint(this.color1);
/* 88 */     paramGraphics2D.fill(this.rect);
/*    */   }
/*    */ 
/*    */   private Rectangle2D decodeRect1()
/*    */   {
/* 95 */     this.rect.setRect(decodeX(0.0F), decodeY(1.333333F), decodeX(3.0F) - decodeX(0.0F), decodeY(1.666667F) - decodeY(1.333333F));
/*    */ 
/* 99 */     return this.rect;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.SeparatorPainter
 * JD-Core Version:    0.6.2
 */
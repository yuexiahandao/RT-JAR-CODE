/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Ellipse2D;
/*     */ import java.awt.geom.Ellipse2D.Float;
/*     */ import java.awt.geom.Path2D;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.awt.geom.RoundRectangle2D.Float;
/*     */ import javax.swing.JComponent;
/*     */ 
/*     */ final class DesktopIconPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  46 */   private Path2D path = new Path2D.Float();
/*  47 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  48 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  49 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  54 */   private Color color1 = decodeColor("nimbusBase", 0.025515F, -0.4788516F, -0.3490197F, 0);
/*  55 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.1022619F, 0.2039216F, 0);
/*  56 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.0F, -0.0682728F, 0.09019607F, 0);
/*  57 */   private Color color4 = decodeColor("nimbusBlueGrey", -0.0111111F, -0.0889746F, 0.1647059F, 0);
/*  58 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.0F, -0.02944524F, -0.01960784F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public DesktopIconPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  66 */     this.state = paramInt;
/*  67 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  73 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  76 */     switch (this.state) { case 1:
/*  77 */       paintBackgroundEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  86 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D) {
/*  90 */     this.roundRect = decodeRoundRect1();
/*  91 */     paramGraphics2D.setPaint(this.color1);
/*  92 */     paramGraphics2D.fill(this.roundRect);
/*  93 */     this.roundRect = decodeRoundRect2();
/*  94 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/*  95 */     paramGraphics2D.fill(this.roundRect);
/*  96 */     this.rect = decodeRect1();
/*  97 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/*  98 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect1()
/*     */   {
/* 105 */     this.roundRect.setRoundRect(decodeX(0.4F), decodeY(0.0F), decodeX(2.8F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.0F), 4.833333492279053D, 4.833333492279053D);
/*     */ 
/* 110 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect2() {
/* 114 */     this.roundRect.setRoundRect(decodeX(0.6F), decodeY(0.2F), decodeX(2.8F) - decodeX(0.6F), decodeY(2.4F) - decodeY(0.2F), 3.099999904632568D, 3.099999904632568D);
/*     */ 
/* 119 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1() {
/* 123 */     this.rect.setRect(decodeX(0.8F), decodeY(0.4F), decodeX(2.4F) - decodeX(0.8F), decodeY(2.2F) - decodeY(0.4F));
/*     */ 
/* 127 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 133 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 134 */     float f1 = (float)localRectangle2D.getX();
/* 135 */     float f2 = (float)localRectangle2D.getY();
/* 136 */     float f3 = (float)localRectangle2D.getWidth();
/* 137 */     float f4 = (float)localRectangle2D.getHeight();
/* 138 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 146 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 147 */     float f1 = (float)localRectangle2D.getX();
/* 148 */     float f2 = (float)localRectangle2D.getY();
/* 149 */     float f3 = (float)localRectangle2D.getWidth();
/* 150 */     float f4 = (float)localRectangle2D.getHeight();
/* 151 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.24F, 1.0F }, new Color[] { this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.DesktopIconPainter
 * JD-Core Version:    0.6.2
 */
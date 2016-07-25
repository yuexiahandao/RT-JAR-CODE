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
/*     */ final class MenuBarPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int BORDER_ENABLED = 2;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  47 */   private Path2D path = new Path2D.Float();
/*  48 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  49 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  50 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  55 */   private Color color1 = decodeColor("nimbusBlueGrey", 0.0F, -0.07016757F, 0.1294118F, 0);
/*  56 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.1025582F, 0.2392157F, 0);
/*  57 */   private Color color3 = decodeColor("nimbusBlueGrey", -0.111111F, -0.1065423F, 0.2392157F, -29);
/*  58 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -255);
/*  59 */   private Color color5 = decodeColor("nimbusBorder", 0.0F, 0.0F, 0.0F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public MenuBarPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  67 */     this.state = paramInt;
/*  68 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  74 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  77 */     switch (this.state) { case 1:
/*  78 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 2:
/*  79 */       paintBorderEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  88 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D) {
/*  92 */     this.rect = decodeRect1();
/*  93 */     paramGraphics2D.setPaint(this.color1);
/*  94 */     paramGraphics2D.fill(this.rect);
/*  95 */     this.rect = decodeRect2();
/*  96 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/*  97 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBorderEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 102 */     this.rect = decodeRect3();
/* 103 */     paramGraphics2D.setPaint(this.color5);
/* 104 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 111 */     this.rect.setRect(decodeX(1.0F), decodeY(0.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(1.952381F) - decodeY(0.0F));
/*     */ 
/* 115 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 119 */     this.rect.setRect(decodeX(1.0F), decodeY(0.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(2.0F) - decodeY(0.0F));
/*     */ 
/* 123 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 127 */     this.rect.setRect(decodeX(1.0F), decodeY(2.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(3.0F) - decodeY(2.0F));
/*     */ 
/* 131 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 137 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 138 */     float f1 = (float)localRectangle2D.getX();
/* 139 */     float f2 = (float)localRectangle2D.getY();
/* 140 */     float f3 = (float)localRectangle2D.getWidth();
/* 141 */     float f4 = (float)localRectangle2D.getHeight();
/* 142 */     return decodeGradient(1.0F * f3 + f1, 0.0F * f4 + f2, 1.0F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.015F, 0.03F, 0.2335445F, 0.7569444F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.MenuBarPainter
 * JD-Core Version:    0.6.2
 */
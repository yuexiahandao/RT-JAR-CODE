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
/*     */ final class PopupMenuPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  47 */   private Path2D path = new Path2D.Float();
/*  48 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  49 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  50 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  55 */   private Color color1 = decodeColor("nimbusBlueGrey", -0.611111F, -0.1105263F, -0.3960784F, 0);
/*  56 */   private Color color2 = decodeColor("nimbusBase", 0.0F, -0.6357143F, 0.4509804F, 0);
/*  57 */   private Color color3 = decodeColor("nimbusBase", 0.0213483F, -0.6150531F, 0.4F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public PopupMenuPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  65 */     this.state = paramInt;
/*  66 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  72 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  75 */     switch (this.state) { case 1:
/*  76 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/*  77 */       paintBackgroundEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  86 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/*  90 */     this.rect = decodeRect1();
/*  91 */     paramGraphics2D.setPaint(this.color1);
/*  92 */     paramGraphics2D.fill(this.rect);
/*  93 */     this.rect = decodeRect2();
/*  94 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/*  95 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 100 */     this.rect = decodeRect3();
/* 101 */     paramGraphics2D.setPaint(this.color1);
/* 102 */     paramGraphics2D.fill(this.rect);
/* 103 */     this.rect = decodeRect4();
/* 104 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 105 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 112 */     this.rect.setRect(decodeX(1.0F), decodeY(0.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 116 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 120 */     this.rect.setRect(decodeX(1.004546F), decodeY(0.1111111F), decodeX(1.995455F) - decodeX(1.004546F), decodeY(2.909091F) - decodeY(0.1111111F));
/*     */ 
/* 124 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 128 */     this.rect.setRect(decodeX(0.0F), decodeY(0.0F), decodeX(3.0F) - decodeX(0.0F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 132 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect4() {
/* 136 */     this.rect.setRect(decodeX(0.5F), decodeY(0.09090909F), decodeX(2.5F) - decodeX(0.5F), decodeY(2.909091F) - decodeY(0.09090909F));
/*     */ 
/* 140 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 146 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 147 */     float f1 = (float)localRectangle2D.getX();
/* 148 */     float f2 = (float)localRectangle2D.getY();
/* 149 */     float f3 = (float)localRectangle2D.getWidth();
/* 150 */     float f4 = (float)localRectangle2D.getHeight();
/* 151 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.003F, 0.02F, 0.5F, 0.98F, 0.996F, 1.0F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.PopupMenuPainter
 * JD-Core Version:    0.6.2
 */
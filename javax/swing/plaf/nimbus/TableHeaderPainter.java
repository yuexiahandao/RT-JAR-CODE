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
/*     */ final class TableHeaderPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int ASCENDINGSORTICON_ENABLED = 1;
/*     */   static final int DESCENDINGSORTICON_ENABLED = 2;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  47 */   private Path2D path = new Path2D.Float();
/*  48 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  49 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  50 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  55 */   private Color color1 = decodeColor("nimbusBase", 0.005792797F, -0.2190476F, 0.1568627F, 0);
/*  56 */   private Color color2 = decodeColor("nimbusBase", 0.003856599F, 0.02012986F, 0.05490196F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public TableHeaderPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  64 */     this.state = paramInt;
/*  65 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  71 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  74 */     switch (this.state) { case 1:
/*  75 */       paintascendingSortIconEnabled(paramGraphics2D); break;
/*     */     case 2:
/*  76 */       paintdescendingSortIconEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  85 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintascendingSortIconEnabled(Graphics2D paramGraphics2D) {
/*  89 */     this.path = decodePath1();
/*  90 */     paramGraphics2D.setPaint(decodeGradient1(this.path));
/*  91 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintdescendingSortIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/*  96 */     this.path = decodePath2();
/*  97 */     paramGraphics2D.setPaint(decodeGradient1(this.path));
/*  98 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 105 */     this.path.reset();
/* 106 */     this.path.moveTo(decodeX(1.0F), decodeY(2.0F));
/* 107 */     this.path.lineTo(decodeX(1.707018F), decodeY(0.0F));
/* 108 */     this.path.lineTo(decodeX(3.0F), decodeY(2.0F));
/* 109 */     this.path.lineTo(decodeX(1.0F), decodeY(2.0F));
/* 110 */     this.path.closePath();
/* 111 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 115 */     this.path.reset();
/* 116 */     this.path.moveTo(decodeX(1.0F), decodeY(1.0F));
/* 117 */     this.path.lineTo(decodeX(2.0F), decodeY(1.0F));
/* 118 */     this.path.lineTo(decodeX(1.502506F), decodeY(2.0F));
/* 119 */     this.path.lineTo(decodeX(1.0F), decodeY(1.0F));
/* 120 */     this.path.closePath();
/* 121 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 127 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 128 */     float f1 = (float)localRectangle2D.getX();
/* 129 */     float f2 = (float)localRectangle2D.getY();
/* 130 */     float f3 = (float)localRectangle2D.getWidth();
/* 131 */     float f4 = (float)localRectangle2D.getHeight();
/* 132 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TableHeaderPainter
 * JD-Core Version:    0.6.2
 */
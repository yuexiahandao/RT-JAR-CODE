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
/*     */ final class ScrollBarTrackPainter extends AbstractRegionPainter
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
/*  55 */   private Color color1 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.1001636F, 0.01176471F, 0);
/*  56 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.1004761F, 0.03529412F, 0);
/*  57 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.106062F, 0.1333333F, 0);
/*  58 */   private Color color4 = decodeColor("nimbusBlueGrey", -0.611111F, -0.1105263F, 0.2470588F, 0);
/*  59 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.02222228F, -0.06465475F, -0.3176471F, 0);
/*  60 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.0F, -0.06766917F, -0.1960784F, 0);
/*  61 */   private Color color7 = decodeColor("nimbusBlueGrey", -0.00694442F, -0.0655825F, -0.04705882F, 0);
/*  62 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.0138889F, -0.07111745F, 0.05098039F, 0);
/*  63 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.0F, -0.07016757F, 0.1294118F, 0);
/*  64 */   private Color color10 = decodeColor("nimbusBlueGrey", 0.0F, -0.0596789F, -0.5137255F, 0);
/*  65 */   private Color color11 = decodeColor("nimbusBlueGrey", 0.0F, -0.0596789F, -0.5137255F, -255);
/*  66 */   private Color color12 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.07826825F, -0.5019608F, -255);
/*  67 */   private Color color13 = decodeColor("nimbusBlueGrey", -0.01587296F, -0.06731644F, -0.1098039F, 0);
/*  68 */   private Color color14 = decodeColor("nimbusBlueGrey", 0.0F, -0.06924191F, 0.1098039F, 0);
/*  69 */   private Color color15 = decodeColor("nimbusBlueGrey", -0.01587296F, -0.06861015F, -0.09019607F, 0);
/*  70 */   private Color color16 = decodeColor("nimbusBlueGrey", 0.0F, -0.06766917F, 0.07843137F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ScrollBarTrackPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  78 */     this.state = paramInt;
/*  79 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  85 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  88 */     switch (this.state) { case 1:
/*  89 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/*  90 */       paintBackgroundEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  99 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/* 103 */     this.rect = decodeRect1();
/* 104 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 105 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 110 */     this.rect = decodeRect1();
/* 111 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 112 */     paramGraphics2D.fill(this.rect);
/* 113 */     this.path = decodePath1();
/* 114 */     paramGraphics2D.setPaint(decodeGradient3(this.path));
/* 115 */     paramGraphics2D.fill(this.path);
/* 116 */     this.path = decodePath2();
/* 117 */     paramGraphics2D.setPaint(decodeGradient4(this.path));
/* 118 */     paramGraphics2D.fill(this.path);
/* 119 */     this.path = decodePath3();
/* 120 */     paramGraphics2D.setPaint(decodeGradient5(this.path));
/* 121 */     paramGraphics2D.fill(this.path);
/* 122 */     this.path = decodePath4();
/* 123 */     paramGraphics2D.setPaint(decodeGradient6(this.path));
/* 124 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 131 */     this.rect.setRect(decodeX(0.0F), decodeY(0.0F), decodeX(3.0F) - decodeX(0.0F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 135 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 139 */     this.path.reset();
/* 140 */     this.path.moveTo(decodeX(0.7F), decodeY(0.0F));
/* 141 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 142 */     this.path.lineTo(decodeX(0.0F), decodeY(1.2F));
/* 143 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(1.2F, 0.0F), decodeAnchorX(0.3F, -1.0F), decodeAnchorY(2.2F, -1.0F), decodeX(0.3F), decodeY(2.2F));
/* 144 */     this.path.curveTo(decodeAnchorX(0.3F, 1.0F), decodeAnchorY(2.2F, 1.0F), decodeAnchorX(0.678571F, 0.0F), decodeAnchorY(2.8F, 0.0F), decodeX(0.678571F), decodeY(2.8F));
/* 145 */     this.path.lineTo(decodeX(0.7F), decodeY(0.0F));
/* 146 */     this.path.closePath();
/* 147 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 151 */     this.path.reset();
/* 152 */     this.path.moveTo(decodeX(3.0F), decodeY(0.0F));
/* 153 */     this.path.lineTo(decodeX(2.222222F), decodeY(0.0F));
/* 154 */     this.path.lineTo(decodeX(2.222222F), decodeY(2.8F));
/* 155 */     this.path.curveTo(decodeAnchorX(2.222222F, 0.0F), decodeAnchorY(2.8F, 0.0F), decodeAnchorX(2.674603F, -1.0F), decodeAnchorY(2.185714F, 1.0F), decodeX(2.674603F), decodeY(2.185714F));
/* 156 */     this.path.curveTo(decodeAnchorX(2.674603F, 1.0F), decodeAnchorY(2.185714F, -1.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(1.2F, 0.0F), decodeX(3.0F), decodeY(1.2F));
/* 157 */     this.path.lineTo(decodeX(3.0F), decodeY(0.0F));
/* 158 */     this.path.closePath();
/* 159 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 163 */     this.path.reset();
/* 164 */     this.path.moveTo(decodeX(0.1142857F), decodeY(1.371429F));
/* 165 */     this.path.curveTo(decodeAnchorX(0.1142857F, 0.7857143F), decodeAnchorY(1.371429F, -0.571429F), decodeAnchorX(0.464286F, -1.357143F), decodeAnchorY(2.071429F, -1.571429F), decodeX(0.464286F), decodeY(2.071429F));
/* 166 */     this.path.curveTo(decodeAnchorX(0.464286F, 1.357143F), decodeAnchorY(2.071429F, 1.571429F), decodeAnchorX(0.8714286F, 0.2142857F), decodeAnchorY(2.728571F, -1.0F), decodeX(0.8714286F), decodeY(2.728571F));
/* 167 */     this.path.curveTo(decodeAnchorX(0.8714286F, -0.2142857F), decodeAnchorY(2.728571F, 1.0F), decodeAnchorX(0.3571429F, 1.5F), decodeAnchorY(2.314286F, 1.642857F), decodeX(0.3571429F), decodeY(2.314286F));
/* 168 */     this.path.curveTo(decodeAnchorX(0.3571429F, -1.5F), decodeAnchorY(2.314286F, -1.642857F), decodeAnchorX(0.1142857F, -0.7857143F), decodeAnchorY(1.371429F, 0.571429F), decodeX(0.1142857F), decodeY(1.371429F));
/* 169 */     this.path.closePath();
/* 170 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 174 */     this.path.reset();
/* 175 */     this.path.moveTo(decodeX(2.111111F), decodeY(2.7F));
/* 176 */     this.path.curveTo(decodeAnchorX(2.111111F, 0.4285714F), decodeAnchorY(2.7F, 0.6428571F), decodeAnchorX(2.626984F, -1.571429F), decodeAnchorY(2.2F, 1.642857F), decodeX(2.626984F), decodeY(2.2F));
/* 177 */     this.path.curveTo(decodeAnchorX(2.626984F, 1.571429F), decodeAnchorY(2.2F, -1.642857F), decodeAnchorX(2.84127F, 0.7142857F), decodeAnchorY(1.385714F, 0.6428571F), decodeX(2.84127F), decodeY(1.385714F));
/* 178 */     this.path.curveTo(decodeAnchorX(2.84127F, -0.7142857F), decodeAnchorY(1.385714F, -0.6428571F), decodeAnchorX(2.523809F, 0.7142857F), decodeAnchorY(2.057143F, -0.8571429F), decodeX(2.523809F), decodeY(2.057143F));
/* 179 */     this.path.curveTo(decodeAnchorX(2.523809F, -0.7142857F), decodeAnchorY(2.057143F, 0.8571429F), decodeAnchorX(2.111111F, -0.4285714F), decodeAnchorY(2.7F, -0.6428571F), decodeX(2.111111F), decodeY(2.7F));
/* 180 */     this.path.closePath();
/* 181 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 187 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 188 */     float f1 = (float)localRectangle2D.getX();
/* 189 */     float f2 = (float)localRectangle2D.getY();
/* 190 */     float f3 = (float)localRectangle2D.getWidth();
/* 191 */     float f4 = (float)localRectangle2D.getHeight();
/* 192 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.01612903F, 0.03870968F, 0.06129032F, 0.1609108F, 0.2645161F, 0.4378071F, 0.883871F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 204 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 205 */     float f1 = (float)localRectangle2D.getX();
/* 206 */     float f2 = (float)localRectangle2D.getY();
/* 207 */     float f3 = (float)localRectangle2D.getWidth();
/* 208 */     float f4 = (float)localRectangle2D.getHeight();
/* 209 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.03064516F, 0.06129032F, 0.09677419F, 0.1322581F, 0.2209677F, 0.3096774F, 0.4743463F, 0.8225806F }, new Color[] { this.color5, decodeColor(this.color5, this.color6, 0.5F), this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 223 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 224 */     float f1 = (float)localRectangle2D.getX();
/* 225 */     float f2 = (float)localRectangle2D.getY();
/* 226 */     float f3 = (float)localRectangle2D.getWidth();
/* 227 */     float f4 = (float)localRectangle2D.getHeight();
/* 228 */     return decodeGradient(0.0F * f3 + f1, 0.0F * f4 + f2, 0.928571F * f3 + f1, 0.122449F * f4 + f2, new float[] { 0.0F, 0.1F, 1.0F }, new Color[] { this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 236 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 237 */     float f1 = (float)localRectangle2D.getX();
/* 238 */     float f2 = (float)localRectangle2D.getY();
/* 239 */     float f3 = (float)localRectangle2D.getWidth();
/* 240 */     float f4 = (float)localRectangle2D.getHeight();
/* 241 */     return decodeGradient(-0.04591837F * f3 + f1, 0.1833643F * f4 + f2, 0.872449F * f3 + f1, 0.04050711F * f4 + f2, new float[] { 0.0F, 0.8709678F, 1.0F }, new Color[] { this.color12, decodeColor(this.color12, this.color10, 0.5F), this.color10 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 249 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 250 */     float f1 = (float)localRectangle2D.getX();
/* 251 */     float f2 = (float)localRectangle2D.getY();
/* 252 */     float f3 = (float)localRectangle2D.getWidth();
/* 253 */     float f4 = (float)localRectangle2D.getHeight();
/* 254 */     return decodeGradient(0.127193F * f3 + f1, 0.1315789F * f4 + f2, 0.9078947F * f3 + f1, 0.877193F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 262 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 263 */     float f1 = (float)localRectangle2D.getX();
/* 264 */     float f2 = (float)localRectangle2D.getY();
/* 265 */     float f3 = (float)localRectangle2D.getWidth();
/* 266 */     float f4 = (float)localRectangle2D.getHeight();
/* 267 */     return decodeGradient(0.8645834F * f3 + f1, 0.2095238F * f4 + f2, 0.02083319F * f3 + f1, 0.952381F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ScrollBarTrackPainter
 * JD-Core Version:    0.6.2
 */
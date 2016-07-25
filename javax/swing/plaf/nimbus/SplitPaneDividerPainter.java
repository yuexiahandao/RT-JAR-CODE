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
/*     */ final class SplitPaneDividerPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int BACKGROUND_FOCUSED = 2;
/*     */   static final int FOREGROUND_ENABLED = 3;
/*     */   static final int FOREGROUND_ENABLED_VERTICAL = 4;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  49 */   private Path2D path = new Path2D.Float();
/*  50 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  51 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  52 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  57 */   private Color color1 = decodeColor("nimbusBlueGrey", 0.0F, -0.01735862F, -0.1137255F, 0);
/*  58 */   private Color color2 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.1023962F, 0.2196078F, 0);
/*  59 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.0F, -0.07016757F, 0.1294118F, 0);
/*  60 */   private Color color4 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  61 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  62 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.0F, -0.04802632F, 0.007843137F, 0);
/*  63 */   private Color color7 = decodeColor("nimbusBlueGrey", 0.005555511F, -0.06970999F, 0.2156863F, 0);
/*  64 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.0F, -0.06704806F, 0.06666666F, 0);
/*  65 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.0F, -0.01961722F, -0.0980392F, 0);
/*  66 */   private Color color10 = decodeColor("nimbusBlueGrey", 0.004273474F, -0.03790062F, -0.04313725F, 0);
/*  67 */   private Color color11 = decodeColor("nimbusBlueGrey", -0.111111F, -0.1065738F, 0.2470588F, 0);
/*  68 */   private Color color12 = decodeColor("nimbusBlueGrey", 0.0F, -0.04930183F, 0.0235294F, 0);
/*  69 */   private Color color13 = decodeColor("nimbusBlueGrey", -0.00694442F, -0.07399663F, 0.1137255F, 0);
/*  70 */   private Color color14 = decodeColor("nimbusBlueGrey", -0.01851857F, -0.06998578F, 0.1254902F, 0);
/*  71 */   private Color color15 = decodeColor("nimbusBlueGrey", 0.0F, -0.05052632F, 0.03921568F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public SplitPaneDividerPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  79 */     this.state = paramInt;
/*  80 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  86 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  89 */     switch (this.state) { case 1:
/*  90 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 2:
/*  91 */       paintBackgroundFocused(paramGraphics2D); break;
/*     */     case 3:
/*  92 */       paintForegroundEnabled(paramGraphics2D); break;
/*     */     case 4:
/*  93 */       paintForegroundEnabledAndVertical(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 102 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D) {
/* 106 */     this.rect = decodeRect1();
/* 107 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 108 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 113 */     this.rect = decodeRect1();
/* 114 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 115 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 120 */     this.roundRect = decodeRoundRect1();
/* 121 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 122 */     paramGraphics2D.fill(this.roundRect);
/* 123 */     this.roundRect = decodeRoundRect2();
/* 124 */     paramGraphics2D.setPaint(decodeGradient4(this.roundRect));
/* 125 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundEnabledAndVertical(Graphics2D paramGraphics2D)
/*     */   {
/* 130 */     this.roundRect = decodeRoundRect3();
/* 131 */     paramGraphics2D.setPaint(decodeGradient5(this.roundRect));
/* 132 */     paramGraphics2D.fill(this.roundRect);
/* 133 */     this.rect = decodeRect2();
/* 134 */     paramGraphics2D.setPaint(decodeGradient6(this.rect));
/* 135 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 142 */     this.rect.setRect(decodeX(1.0F), decodeY(0.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 146 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect1() {
/* 150 */     this.roundRect.setRoundRect(decodeX(1.05F), decodeY(1.3F), decodeX(1.95F) - decodeX(1.05F), decodeY(1.8F) - decodeY(1.3F), 3.666666746139526D, 3.666666746139526D);
/*     */ 
/* 155 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect2() {
/* 159 */     this.roundRect.setRoundRect(decodeX(1.1F), decodeY(1.4F), decodeX(1.9F) - decodeX(1.1F), decodeY(1.7F) - decodeY(1.4F), 4.0D, 4.0D);
/*     */ 
/* 164 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect3() {
/* 168 */     this.roundRect.setRoundRect(decodeX(1.3F), decodeY(1.142857F), decodeX(1.7F) - decodeX(1.3F), decodeY(1.821429F) - decodeY(1.142857F), 4.0D, 4.0D);
/*     */ 
/* 173 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 177 */     this.rect.setRect(decodeX(1.4F), decodeY(1.178572F), decodeX(1.6F) - decodeX(1.4F), decodeY(1.767857F) - decodeY(1.178572F));
/*     */ 
/* 181 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 187 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 188 */     float f1 = (float)localRectangle2D.getX();
/* 189 */     float f2 = (float)localRectangle2D.getY();
/* 190 */     float f3 = (float)localRectangle2D.getWidth();
/* 191 */     float f4 = (float)localRectangle2D.getHeight();
/* 192 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.05806452F, 0.08064516F, 0.103226F, 0.116129F, 0.1290323F, 0.433871F, 0.7387097F, 0.7790322F, 0.8193548F, 0.8580645F, 0.8967742F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color2, 0.5F), this.color2, decodeColor(this.color2, this.color1, 0.5F), this.color1 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 208 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 209 */     float f1 = (float)localRectangle2D.getX();
/* 210 */     float f2 = (float)localRectangle2D.getY();
/* 211 */     float f3 = (float)localRectangle2D.getWidth();
/* 212 */     float f4 = (float)localRectangle2D.getHeight();
/* 213 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.05806452F, 0.08064516F, 0.103226F, 0.116613F, 0.13F, 0.43F, 0.73F, 0.774677F, 0.8193548F, 0.8580645F, 0.8967742F }, new Color[] { this.color1, decodeColor(this.color1, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color1, 0.5F), this.color1 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 229 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 230 */     float f1 = (float)localRectangle2D.getX();
/* 231 */     float f2 = (float)localRectangle2D.getY();
/* 232 */     float f3 = (float)localRectangle2D.getWidth();
/* 233 */     float f4 = (float)localRectangle2D.getHeight();
/* 234 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.2064516F, 0.5F, 0.7935484F }, new Color[] { this.color1, decodeColor(this.color1, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 242 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 243 */     float f1 = (float)localRectangle2D.getX();
/* 244 */     float f2 = (float)localRectangle2D.getY();
/* 245 */     float f3 = (float)localRectangle2D.getWidth();
/* 246 */     float f4 = (float)localRectangle2D.getHeight();
/* 247 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.09032258F, 0.2951613F, 0.5F, 0.5822581F, 0.6645162F }, new Color[] { this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 257 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 258 */     float f1 = (float)localRectangle2D.getX();
/* 259 */     float f2 = (float)localRectangle2D.getY();
/* 260 */     float f3 = (float)localRectangle2D.getWidth();
/* 261 */     float f4 = (float)localRectangle2D.getHeight();
/* 262 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.75F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.4209677F, 0.8419355F, 0.8951613F, 0.9483871F }, new Color[] { this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 272 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 273 */     float f1 = (float)localRectangle2D.getX();
/* 274 */     float f2 = (float)localRectangle2D.getY();
/* 275 */     float f3 = (float)localRectangle2D.getWidth();
/* 276 */     float f4 = (float)localRectangle2D.getHeight();
/* 277 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.08064516F, 0.1612903F, 0.5129032F, 0.8645161F, 0.8854839F, 0.9064516F }, new Color[] { this.color12, decodeColor(this.color12, this.color13, 0.5F), this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14, decodeColor(this.color14, this.color15, 0.5F), this.color15 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.SplitPaneDividerPainter
 * JD-Core Version:    0.6.2
 */
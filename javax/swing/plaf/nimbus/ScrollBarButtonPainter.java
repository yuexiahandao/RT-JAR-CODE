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
/*     */ final class ScrollBarButtonPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int FOREGROUND_ENABLED = 1;
/*     */   static final int FOREGROUND_DISABLED = 2;
/*     */   static final int FOREGROUND_MOUSEOVER = 3;
/*     */   static final int FOREGROUND_PRESSED = 4;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  49 */   private Path2D path = new Path2D.Float();
/*  50 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  51 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  52 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  57 */   private Color color1 = new Color(255, 200, 0, 255);
/*  58 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.0111111F, -0.07763158F, -0.14902F, 0);
/*  59 */   private Color color3 = decodeColor("nimbusBlueGrey", -0.111111F, -0.1058093F, 0.08627451F, 0);
/*  60 */   private Color color4 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.1022619F, 0.2039216F, 0);
/*  61 */   private Color color5 = decodeColor("nimbusBlueGrey", -0.03968257F, -0.07927632F, 0.1333333F, 0);
/*  62 */   private Color color6 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.0738291F, 0.1098039F, 0);
/*  63 */   private Color color7 = decodeColor("nimbusBlueGrey", -0.03968257F, -0.08241387F, 0.2313725F, 0);
/*  64 */   private Color color8 = decodeColor("nimbusBlueGrey", -0.05555552F, -0.0844394F, -0.2941177F, -136);
/*  65 */   private Color color9 = decodeColor("nimbusBlueGrey", -0.05555552F, -0.09876161F, 0.254902F, -178);
/*  66 */   private Color color10 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.08878718F, -0.5647059F, 0);
/*  67 */   private Color color11 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.08022329F, -0.4862745F, 0);
/*  68 */   private Color color12 = decodeColor("nimbusBlueGrey", -0.111111F, -0.09525914F, -0.2313725F, 0);
/*  69 */   private Color color13 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -165);
/*  70 */   private Color color14 = decodeColor("nimbusBlueGrey", -0.04444444F, -0.08022329F, -0.0980392F, 0);
/*  71 */   private Color color15 = decodeColor("nimbusBlueGrey", -0.611111F, -0.1105263F, 0.1058824F, 0);
/*  72 */   private Color color16 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  73 */   private Color color17 = decodeColor("nimbusBlueGrey", -0.03968257F, -0.08171973F, 0.2078431F, 0);
/*  74 */   private Color color18 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.07677104F, 0.1843137F, 0);
/*  75 */   private Color color19 = decodeColor("nimbusBlueGrey", -0.04444444F, -0.08022329F, -0.0980392F, -69);
/*  76 */   private Color color20 = decodeColor("nimbusBlueGrey", -0.05555552F, -0.09876161F, 0.254902F, -39);
/*  77 */   private Color color21 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.0951417F, -0.4901961F, 0);
/*  78 */   private Color color22 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.08699691F, -0.4117647F, 0);
/*  79 */   private Color color23 = decodeColor("nimbusBlueGrey", -0.111111F, -0.097193F, -0.1568627F, 0);
/*  80 */   private Color color24 = decodeColor("nimbusBlueGrey", -0.03703702F, -0.04385965F, -0.2156863F, 0);
/*  81 */   private Color color25 = decodeColor("nimbusBlueGrey", -0.0634921F, -0.07309316F, -0.01176471F, 0);
/*  82 */   private Color color26 = decodeColor("nimbusBlueGrey", -0.0486111F, -0.07296763F, 0.09019607F, 0);
/*  83 */   private Color color27 = decodeColor("nimbusBlueGrey", -0.03535354F, -0.0549708F, 0.03137255F, 0);
/*  84 */   private Color color28 = decodeColor("nimbusBlueGrey", -0.03418803F, -0.04316881F, 0.01176471F, 0);
/*  85 */   private Color color29 = decodeColor("nimbusBlueGrey", -0.03535354F, -0.0600676F, 0.1098039F, 0);
/*  86 */   private Color color30 = decodeColor("nimbusBlueGrey", -0.03703702F, -0.04385965F, -0.2156863F, -44);
/*  87 */   private Color color31 = decodeColor("nimbusBlueGrey", -0.611111F, -0.1105263F, -0.7450981F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ScrollBarButtonPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  95 */     this.state = paramInt;
/*  96 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 102 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 105 */     switch (this.state) { case 1:
/* 106 */       paintForegroundEnabled(paramGraphics2D); break;
/*     */     case 2:
/* 107 */       paintForegroundDisabled(paramGraphics2D); break;
/*     */     case 3:
/* 108 */       paintForegroundMouseOver(paramGraphics2D); break;
/*     */     case 4:
/* 109 */       paintForegroundPressed(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 118 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintForegroundEnabled(Graphics2D paramGraphics2D) {
/* 122 */     this.path = decodePath1();
/* 123 */     paramGraphics2D.setPaint(this.color1);
/* 124 */     paramGraphics2D.fill(this.path);
/* 125 */     this.path = decodePath2();
/* 126 */     paramGraphics2D.setPaint(decodeGradient1(this.path));
/* 127 */     paramGraphics2D.fill(this.path);
/* 128 */     this.path = decodePath3();
/* 129 */     paramGraphics2D.setPaint(decodeGradient2(this.path));
/* 130 */     paramGraphics2D.fill(this.path);
/* 131 */     this.path = decodePath4();
/* 132 */     paramGraphics2D.setPaint(decodeGradient3(this.path));
/* 133 */     paramGraphics2D.fill(this.path);
/* 134 */     this.path = decodePath5();
/* 135 */     paramGraphics2D.setPaint(this.color13);
/* 136 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundDisabled(Graphics2D paramGraphics2D)
/*     */   {
/* 141 */     this.path = decodePath1();
/* 142 */     paramGraphics2D.setPaint(this.color1);
/* 143 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 148 */     this.path = decodePath1();
/* 149 */     paramGraphics2D.setPaint(this.color1);
/* 150 */     paramGraphics2D.fill(this.path);
/* 151 */     this.path = decodePath2();
/* 152 */     paramGraphics2D.setPaint(decodeGradient4(this.path));
/* 153 */     paramGraphics2D.fill(this.path);
/* 154 */     this.path = decodePath3();
/* 155 */     paramGraphics2D.setPaint(decodeGradient5(this.path));
/* 156 */     paramGraphics2D.fill(this.path);
/* 157 */     this.path = decodePath4();
/* 158 */     paramGraphics2D.setPaint(decodeGradient6(this.path));
/* 159 */     paramGraphics2D.fill(this.path);
/* 160 */     this.path = decodePath5();
/* 161 */     paramGraphics2D.setPaint(this.color13);
/* 162 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 167 */     this.path = decodePath1();
/* 168 */     paramGraphics2D.setPaint(this.color1);
/* 169 */     paramGraphics2D.fill(this.path);
/* 170 */     this.path = decodePath2();
/* 171 */     paramGraphics2D.setPaint(decodeGradient7(this.path));
/* 172 */     paramGraphics2D.fill(this.path);
/* 173 */     this.path = decodePath3();
/* 174 */     paramGraphics2D.setPaint(decodeGradient8(this.path));
/* 175 */     paramGraphics2D.fill(this.path);
/* 176 */     this.path = decodePath4();
/* 177 */     paramGraphics2D.setPaint(this.color31);
/* 178 */     paramGraphics2D.fill(this.path);
/* 179 */     this.path = decodePath5();
/* 180 */     paramGraphics2D.setPaint(this.color13);
/* 181 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 188 */     this.path.reset();
/* 189 */     this.path.moveTo(decodeX(3.0F), decodeY(3.0F));
/* 190 */     this.path.lineTo(decodeX(3.0F), decodeY(3.0F));
/* 191 */     this.path.closePath();
/* 192 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 196 */     this.path.reset();
/* 197 */     this.path.moveTo(decodeX(0.0F), decodeY(0.0F));
/* 198 */     this.path.lineTo(decodeX(1.695652F), decodeY(0.0F));
/* 199 */     this.path.curveTo(decodeAnchorX(1.695652F, 0.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(1.695652F, -0.7058824F), decodeAnchorY(1.307692F, -3.029412F), decodeX(1.695652F), decodeY(1.307692F));
/* 200 */     this.path.curveTo(decodeAnchorX(1.695652F, 0.7058824F), decodeAnchorY(1.307692F, 3.029412F), decodeAnchorX(1.826087F, -2.0F), decodeAnchorY(1.769231F, -1.941176F), decodeX(1.826087F), decodeY(1.769231F));
/* 201 */     this.path.curveTo(decodeAnchorX(1.826087F, 2.0F), decodeAnchorY(1.769231F, 1.941176F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(3.0F), decodeY(2.0F));
/* 202 */     this.path.lineTo(decodeX(3.0F), decodeY(3.0F));
/* 203 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 204 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 205 */     this.path.closePath();
/* 206 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 210 */     this.path.reset();
/* 211 */     this.path.moveTo(decodeX(0.0F), decodeY(1.002263F));
/* 212 */     this.path.lineTo(decodeX(0.9705882F), decodeY(1.038462F));
/* 213 */     this.path.lineTo(decodeX(1.040921F), decodeY(1.079186F));
/* 214 */     this.path.lineTo(decodeX(1.040921F), decodeY(3.0F));
/* 215 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 216 */     this.path.lineTo(decodeX(0.0F), decodeY(1.002263F));
/* 217 */     this.path.closePath();
/* 218 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 222 */     this.path.reset();
/* 223 */     this.path.moveTo(decodeX(1.478261F), decodeY(1.230769F));
/* 224 */     this.path.lineTo(decodeX(1.478261F), decodeY(1.769231F));
/* 225 */     this.path.lineTo(decodeX(1.171356F), decodeY(1.5F));
/* 226 */     this.path.lineTo(decodeX(1.478261F), decodeY(1.230769F));
/* 227 */     this.path.closePath();
/* 228 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath5() {
/* 232 */     this.path.reset();
/* 233 */     this.path.moveTo(decodeX(1.671356F), decodeY(1.076923F));
/* 234 */     this.path.curveTo(decodeAnchorX(1.671356F, 0.735294F), decodeAnchorY(1.076923F, 0.0F), decodeAnchorX(1.71867F, -0.9117647F), decodeAnchorY(1.409502F, -2.205882F), decodeX(1.71867F), decodeY(1.409502F));
/* 235 */     this.path.curveTo(decodeAnchorX(1.71867F, 0.9117647F), decodeAnchorY(1.409502F, 2.205882F), decodeAnchorX(1.84399F, -2.352941F), decodeAnchorY(1.794118F, -1.852941F), decodeX(1.84399F), decodeY(1.794118F));
/* 236 */     this.path.curveTo(decodeAnchorX(1.84399F, 2.352941F), decodeAnchorY(1.794118F, 1.852941F), decodeAnchorX(2.5F, 0.0F), decodeAnchorY(2.235294F, 0.0F), decodeX(2.5F), decodeY(2.235294F));
/* 237 */     this.path.lineTo(decodeX(2.352942F), decodeY(2.823529F));
/* 238 */     this.path.curveTo(decodeAnchorX(2.352942F, 0.0F), decodeAnchorY(2.823529F, 0.0F), decodeAnchorX(1.818414F, 1.558824F), decodeAnchorY(1.843891F, 1.382353F), decodeX(1.818414F), decodeY(1.843891F));
/* 239 */     this.path.curveTo(decodeAnchorX(1.818414F, -1.558824F), decodeAnchorY(1.843891F, -1.382353F), decodeAnchorX(1.694373F, 0.7941176F), decodeAnchorY(1.484163F, 2.0F), decodeX(1.694373F), decodeY(1.484163F));
/* 240 */     this.path.curveTo(decodeAnchorX(1.694373F, -0.7941176F), decodeAnchorY(1.484163F, -2.0F), decodeAnchorX(1.671356F, -0.735294F), decodeAnchorY(1.076923F, 0.0F), decodeX(1.671356F), decodeY(1.076923F));
/* 241 */     this.path.closePath();
/* 242 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 248 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 249 */     float f1 = (float)localRectangle2D.getX();
/* 250 */     float f2 = (float)localRectangle2D.getY();
/* 251 */     float f3 = (float)localRectangle2D.getWidth();
/* 252 */     float f4 = (float)localRectangle2D.getHeight();
/* 253 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.03293413F, 0.06586827F, 0.08982036F, 0.1137725F, 0.2305389F, 0.3473054F, 0.494012F, 0.6407186F, 0.7844312F, 0.9281437F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5, decodeColor(this.color5, this.color6, 0.5F), this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 269 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 270 */     float f1 = (float)localRectangle2D.getX();
/* 271 */     float f2 = (float)localRectangle2D.getY();
/* 272 */     float f3 = (float)localRectangle2D.getWidth();
/* 273 */     float f4 = (float)localRectangle2D.getHeight();
/* 274 */     return decodeGradient(0.0F * f3 + f1, 0.5F * f4 + f2, 0.5735294F * f3 + f1, 0.5F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 282 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 283 */     float f1 = (float)localRectangle2D.getX();
/* 284 */     float f2 = (float)localRectangle2D.getY();
/* 285 */     float f3 = (float)localRectangle2D.getWidth();
/* 286 */     float f4 = (float)localRectangle2D.getHeight();
/* 287 */     return decodeGradient(0.925F * f3 + f1, 0.928571F * f4 + f2, 0.925F * f3 + f1, 0.004201681F * f4 + f2, new float[] { 0.0F, 0.2964072F, 0.5928144F, 0.7934132F, 0.994012F }, new Color[] { this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11, decodeColor(this.color11, this.color12, 0.5F), this.color12 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 297 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 298 */     float f1 = (float)localRectangle2D.getX();
/* 299 */     float f2 = (float)localRectangle2D.getY();
/* 300 */     float f3 = (float)localRectangle2D.getWidth();
/* 301 */     float f4 = (float)localRectangle2D.getHeight();
/* 302 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.03293413F, 0.06586827F, 0.08982036F, 0.1137725F, 0.2305389F, 0.3473054F, 0.494012F, 0.6407186F, 0.7844312F, 0.9281437F }, new Color[] { this.color14, decodeColor(this.color14, this.color15, 0.5F), this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18, decodeColor(this.color18, this.color16, 0.5F), this.color16 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 318 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 319 */     float f1 = (float)localRectangle2D.getX();
/* 320 */     float f2 = (float)localRectangle2D.getY();
/* 321 */     float f3 = (float)localRectangle2D.getWidth();
/* 322 */     float f4 = (float)localRectangle2D.getHeight();
/* 323 */     return decodeGradient(0.0F * f3 + f1, 0.5F * f4 + f2, 0.5735294F * f3 + f1, 0.5F * f4 + f2, new float[] { 0.1951872F, 0.5975936F, 1.0F }, new Color[] { this.color19, decodeColor(this.color19, this.color20, 0.5F), this.color20 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 331 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 332 */     float f1 = (float)localRectangle2D.getX();
/* 333 */     float f2 = (float)localRectangle2D.getY();
/* 334 */     float f3 = (float)localRectangle2D.getWidth();
/* 335 */     float f4 = (float)localRectangle2D.getHeight();
/* 336 */     return decodeGradient(0.925F * f3 + f1, 0.928571F * f4 + f2, 0.925F * f3 + f1, 0.004201681F * f4 + f2, new float[] { 0.0F, 0.2964072F, 0.5928144F, 0.7934132F, 0.994012F }, new Color[] { this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22, decodeColor(this.color22, this.color23, 0.5F), this.color23 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 346 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 347 */     float f1 = (float)localRectangle2D.getX();
/* 348 */     float f2 = (float)localRectangle2D.getY();
/* 349 */     float f3 = (float)localRectangle2D.getWidth();
/* 350 */     float f4 = (float)localRectangle2D.getHeight();
/* 351 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.03293413F, 0.06586827F, 0.08982036F, 0.1137725F, 0.2305389F, 0.3473054F, 0.494012F, 0.6407186F, 0.7844312F, 0.9281437F }, new Color[] { this.color24, decodeColor(this.color24, this.color25, 0.5F), this.color25, decodeColor(this.color25, this.color26, 0.5F), this.color26, decodeColor(this.color26, this.color27, 0.5F), this.color27, decodeColor(this.color27, this.color28, 0.5F), this.color28, decodeColor(this.color28, this.color29, 0.5F), this.color29 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 367 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 368 */     float f1 = (float)localRectangle2D.getX();
/* 369 */     float f2 = (float)localRectangle2D.getY();
/* 370 */     float f3 = (float)localRectangle2D.getWidth();
/* 371 */     float f4 = (float)localRectangle2D.getHeight();
/* 372 */     return decodeGradient(0.0F * f3 + f1, 0.5F * f4 + f2, 0.5735294F * f3 + f1, 0.5F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color30, decodeColor(this.color30, this.color9, 0.5F), this.color9 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ScrollBarButtonPainter
 * JD-Core Version:    0.6.2
 */
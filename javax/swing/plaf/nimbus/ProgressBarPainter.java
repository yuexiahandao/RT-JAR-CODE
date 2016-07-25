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
/*     */ final class ProgressBarPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int BACKGROUND_DISABLED = 2;
/*     */   static final int FOREGROUND_ENABLED = 3;
/*     */   static final int FOREGROUND_ENABLED_FINISHED = 4;
/*     */   static final int FOREGROUND_ENABLED_INDETERMINATE = 5;
/*     */   static final int FOREGROUND_DISABLED = 6;
/*     */   static final int FOREGROUND_DISABLED_FINISHED = 7;
/*     */   static final int FOREGROUND_DISABLED_INDETERMINATE = 8;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  53 */   private Path2D path = new Path2D.Float();
/*  54 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  55 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  56 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  61 */   private Color color1 = decodeColor("nimbusBlueGrey", 0.0F, -0.04845735F, -0.1764706F, 0);
/*  62 */   private Color color2 = decodeColor("nimbusBlueGrey", 0.0F, -0.06134599F, -0.02745098F, 0);
/*  63 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  64 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.0F, -0.09792128F, 0.1882353F, 0);
/*  65 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.0138889F, -0.0925083F, 0.1254902F, 0);
/*  66 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.0F, -0.08222443F, 0.08627451F, 0);
/*  67 */   private Color color7 = decodeColor("nimbusBlueGrey", 0.0F, -0.08477524F, 0.1686274F, 0);
/*  68 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.0F, -0.08699691F, 0.254902F, 0);
/*  69 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.0F, -0.06161327F, -0.0235294F, 0);
/*  70 */   private Color color10 = decodeColor("nimbusBlueGrey", -0.0111111F, -0.06126523F, 0.05098039F, 0);
/*  71 */   private Color color11 = decodeColor("nimbusBlueGrey", 0.0138889F, -0.09378991F, 0.1921569F, 0);
/*  72 */   private Color color12 = decodeColor("nimbusBlueGrey", 0.0F, -0.08455229F, 0.160784F, 0);
/*  73 */   private Color color13 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.08362049F, 0.1294118F, 0);
/*  74 */   private Color color14 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07826825F, 0.1058824F, 0);
/*  75 */   private Color color15 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07982456F, 0.14902F, 0);
/*  76 */   private Color color16 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.08099045F, 0.1843137F, 0);
/*  77 */   private Color color17 = decodeColor("nimbusOrange", 0.0F, 0.0F, 0.0F, -156);
/*  78 */   private Color color18 = decodeColor("nimbusOrange", -0.01579651F, 0.0209424F, -0.1529412F, 0);
/*  79 */   private Color color19 = decodeColor("nimbusOrange", -0.00432161F, 0.0209424F, -0.0745098F, 0);
/*  80 */   private Color color20 = decodeColor("nimbusOrange", -0.008021399F, 0.0209424F, -0.1019608F, 0);
/*  81 */   private Color color21 = decodeColor("nimbusOrange", -0.0117069F, -0.179058F, -0.0235294F, 0);
/*  82 */   private Color color22 = decodeColor("nimbusOrange", -0.04869125F, 0.0209424F, -0.301961F, 0);
/*  83 */   private Color color23 = decodeColor("nimbusOrange", 0.00394033F, -0.737532F, 0.1764706F, 0);
/*  84 */   private Color color24 = decodeColor("nimbusOrange", 0.00550674F, -0.4676421F, 0.1098039F, 0);
/*  85 */   private Color color25 = decodeColor("nimbusOrange", 0.004212745F, -0.1859542F, 0.04705882F, 0);
/*  86 */   private Color color26 = decodeColor("nimbusOrange", 0.004762694F, 0.0209424F, 0.003921568F, 0);
/*  87 */   private Color color27 = decodeColor("nimbusOrange", 0.004762694F, -0.1514714F, 0.160784F, 0);
/*  88 */   private Color color28 = decodeColor("nimbusOrange", 0.01066548F, -0.2731752F, 0.2509804F, 0);
/*  89 */   private Color color29 = decodeColor("nimbusBlueGrey", -0.5444444F, -0.08748484F, 0.1058824F, 0);
/*  90 */   private Color color30 = decodeColor("nimbusOrange", 0.004762694F, -0.2171528F, 0.2392157F, 0);
/*  91 */   private Color color31 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -173);
/*  92 */   private Color color32 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -170);
/*  93 */   private Color color33 = decodeColor("nimbusOrange", 0.02455436F, -0.887315F, 0.1058824F, -156);
/*  94 */   private Color color34 = decodeColor("nimbusOrange", -0.02359379F, -0.796317F, 0.0235294F, 0);
/*  95 */   private Color color35 = decodeColor("nimbusOrange", -0.01060824F, -0.7760873F, 0.04313725F, 0);
/*  96 */   private Color color36 = decodeColor("nimbusOrange", -0.01540291F, -0.7840576F, 0.03529412F, 0);
/*  97 */   private Color color37 = decodeColor("nimbusOrange", -0.01711231F, -0.8091547F, 0.05882353F, 0);
/*  98 */   private Color color38 = decodeColor("nimbusOrange", -0.07044564F, -0.844649F, -0.01960784F, 0);
/*  99 */   private Color color39 = decodeColor("nimbusOrange", -0.009704903F, -0.938149F, 0.1137255F, 0);
/* 100 */   private Color color40 = decodeColor("nimbusOrange", -0.0004456341F, -0.8674297F, 0.09411764F, 0);
/* 101 */   private Color color41 = decodeColor("nimbusOrange", -0.0004456341F, -0.7989628F, 0.07843137F, 0);
/* 102 */   private Color color42 = decodeColor("nimbusOrange", 0.00132741F, -0.753096F, 0.06666666F, 0);
/* 103 */   private Color color43 = decodeColor("nimbusOrange", 0.00132741F, -0.7644457F, 0.1098039F, 0);
/* 104 */   private Color color44 = decodeColor("nimbusOrange", 0.00924429F, -0.7879465F, 0.1333333F, 0);
/* 105 */   private Color color45 = decodeColor("nimbusBlueGrey", -0.01587296F, -0.0803539F, 0.1647059F, 0);
/* 106 */   private Color color46 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07968931F, 0.145098F, 0);
/* 107 */   private Color color47 = decodeColor("nimbusBlueGrey", 0.02222228F, -0.08779904F, 0.1176471F, 0);
/* 108 */   private Color color48 = decodeColor("nimbusBlueGrey", 0.0138889F, -0.07512809F, 0.1411765F, 0);
/* 109 */   private Color color49 = decodeColor("nimbusBlueGrey", 0.0138889F, -0.07604356F, 0.1647059F, 0);
/* 110 */   private Color color50 = decodeColor("nimbusOrange", 0.001406223F, -0.7781647F, 0.1294118F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ProgressBarPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/* 118 */     this.state = paramInt;
/* 119 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 125 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 128 */     switch (this.state) { case 1:
/* 129 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 2:
/* 130 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 3:
/* 131 */       paintForegroundEnabled(paramGraphics2D); break;
/*     */     case 4:
/* 132 */       paintForegroundEnabledAndFinished(paramGraphics2D); break;
/*     */     case 5:
/* 133 */       paintForegroundEnabledAndIndeterminate(paramGraphics2D); break;
/*     */     case 6:
/* 134 */       paintForegroundDisabled(paramGraphics2D); break;
/*     */     case 7:
/* 135 */       paintForegroundDisabledAndFinished(paramGraphics2D); break;
/*     */     case 8:
/* 136 */       paintForegroundDisabledAndIndeterminate(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 145 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D) {
/* 149 */     this.rect = decodeRect1();
/* 150 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 151 */     paramGraphics2D.fill(this.rect);
/* 152 */     this.rect = decodeRect2();
/* 153 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 154 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D)
/*     */   {
/* 159 */     this.rect = decodeRect1();
/* 160 */     paramGraphics2D.setPaint(decodeGradient3(this.rect));
/* 161 */     paramGraphics2D.fill(this.rect);
/* 162 */     this.rect = decodeRect2();
/* 163 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 164 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 169 */     this.path = decodePath1();
/* 170 */     paramGraphics2D.setPaint(this.color17);
/* 171 */     paramGraphics2D.fill(this.path);
/* 172 */     this.rect = decodeRect3();
/* 173 */     paramGraphics2D.setPaint(decodeGradient5(this.rect));
/* 174 */     paramGraphics2D.fill(this.rect);
/* 175 */     this.rect = decodeRect4();
/* 176 */     paramGraphics2D.setPaint(decodeGradient6(this.rect));
/* 177 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundEnabledAndFinished(Graphics2D paramGraphics2D)
/*     */   {
/* 182 */     this.path = decodePath2();
/* 183 */     paramGraphics2D.setPaint(this.color17);
/* 184 */     paramGraphics2D.fill(this.path);
/* 185 */     this.rect = decodeRect1();
/* 186 */     paramGraphics2D.setPaint(decodeGradient5(this.rect));
/* 187 */     paramGraphics2D.fill(this.rect);
/* 188 */     this.rect = decodeRect2();
/* 189 */     paramGraphics2D.setPaint(decodeGradient6(this.rect));
/* 190 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundEnabledAndIndeterminate(Graphics2D paramGraphics2D)
/*     */   {
/* 195 */     this.rect = decodeRect5();
/* 196 */     paramGraphics2D.setPaint(decodeGradient7(this.rect));
/* 197 */     paramGraphics2D.fill(this.rect);
/* 198 */     this.path = decodePath3();
/* 199 */     paramGraphics2D.setPaint(decodeGradient8(this.path));
/* 200 */     paramGraphics2D.fill(this.path);
/* 201 */     this.rect = decodeRect6();
/* 202 */     paramGraphics2D.setPaint(this.color31);
/* 203 */     paramGraphics2D.fill(this.rect);
/* 204 */     this.rect = decodeRect7();
/* 205 */     paramGraphics2D.setPaint(this.color32);
/* 206 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundDisabled(Graphics2D paramGraphics2D)
/*     */   {
/* 211 */     this.path = decodePath1();
/* 212 */     paramGraphics2D.setPaint(this.color33);
/* 213 */     paramGraphics2D.fill(this.path);
/* 214 */     this.rect = decodeRect3();
/* 215 */     paramGraphics2D.setPaint(decodeGradient9(this.rect));
/* 216 */     paramGraphics2D.fill(this.rect);
/* 217 */     this.rect = decodeRect4();
/* 218 */     paramGraphics2D.setPaint(decodeGradient10(this.rect));
/* 219 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundDisabledAndFinished(Graphics2D paramGraphics2D)
/*     */   {
/* 224 */     this.path = decodePath4();
/* 225 */     paramGraphics2D.setPaint(this.color33);
/* 226 */     paramGraphics2D.fill(this.path);
/* 227 */     this.rect = decodeRect1();
/* 228 */     paramGraphics2D.setPaint(decodeGradient9(this.rect));
/* 229 */     paramGraphics2D.fill(this.rect);
/* 230 */     this.rect = decodeRect2();
/* 231 */     paramGraphics2D.setPaint(decodeGradient10(this.rect));
/* 232 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundDisabledAndIndeterminate(Graphics2D paramGraphics2D)
/*     */   {
/* 237 */     this.rect = decodeRect5();
/* 238 */     paramGraphics2D.setPaint(decodeGradient11(this.rect));
/* 239 */     paramGraphics2D.fill(this.rect);
/* 240 */     this.path = decodePath5();
/* 241 */     paramGraphics2D.setPaint(decodeGradient12(this.path));
/* 242 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 249 */     this.rect.setRect(decodeX(0.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.4F));
/*     */ 
/* 253 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 257 */     this.rect.setRect(decodeX(0.6F), decodeY(0.6F), decodeX(2.4F) - decodeX(0.6F), decodeY(2.4F) - decodeY(0.6F));
/*     */ 
/* 261 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 265 */     this.path.reset();
/* 266 */     this.path.moveTo(decodeX(0.6F), decodeY(0.1266667F));
/* 267 */     this.path.curveTo(decodeAnchorX(0.6F, -2.0F), decodeAnchorY(0.1266667F, 0.0F), decodeAnchorX(0.1266667F, 0.0F), decodeAnchorY(0.6F, -2.0F), decodeX(0.1266667F), decodeY(0.6F));
/* 268 */     this.path.curveTo(decodeAnchorX(0.1266667F, 0.0F), decodeAnchorY(0.6F, 2.0F), decodeAnchorX(0.1266667F, 0.0F), decodeAnchorY(2.4F, -2.0F), decodeX(0.1266667F), decodeY(2.4F));
/* 269 */     this.path.curveTo(decodeAnchorX(0.1266667F, 0.0F), decodeAnchorY(2.4F, 2.0F), decodeAnchorX(0.6F, -2.0F), decodeAnchorY(2.893333F, 0.0F), decodeX(0.6F), decodeY(2.893333F));
/* 270 */     this.path.curveTo(decodeAnchorX(0.6F, 2.0F), decodeAnchorY(2.893333F, 0.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(2.893333F, 0.0F), decodeX(3.0F), decodeY(2.893333F));
/* 271 */     this.path.lineTo(decodeX(3.0F), decodeY(2.6F));
/* 272 */     this.path.lineTo(decodeX(0.4F), decodeY(2.6F));
/* 273 */     this.path.lineTo(decodeX(0.4F), decodeY(0.4F));
/* 274 */     this.path.lineTo(decodeX(3.0F), decodeY(0.4F));
/* 275 */     this.path.lineTo(decodeX(3.0F), decodeY(0.12F));
/* 276 */     this.path.curveTo(decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.12F, 0.0F), decodeAnchorX(0.6F, 2.0F), decodeAnchorY(0.1266667F, 0.0F), decodeX(0.6F), decodeY(0.1266667F));
/* 277 */     this.path.closePath();
/* 278 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 282 */     this.rect.setRect(decodeX(0.4F), decodeY(0.4F), decodeX(3.0F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.4F));
/*     */ 
/* 286 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect4() {
/* 290 */     this.rect.setRect(decodeX(0.6F), decodeY(0.6F), decodeX(2.8F) - decodeX(0.6F), decodeY(2.4F) - decodeY(0.6F));
/*     */ 
/* 294 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 298 */     this.path.reset();
/* 299 */     this.path.moveTo(decodeX(0.5466667F), decodeY(0.1266667F));
/* 300 */     this.path.curveTo(decodeAnchorX(0.5466667F, -2.0F), decodeAnchorY(0.1266667F, 0.0F), decodeAnchorX(0.12F, 0.0F), decodeAnchorY(0.6066667F, -2.0F), decodeX(0.12F), decodeY(0.6066667F));
/* 301 */     this.path.lineTo(decodeX(0.12F), decodeY(2.426667F));
/* 302 */     this.path.curveTo(decodeAnchorX(0.12F, 0.0F), decodeAnchorY(2.426667F, 2.0F), decodeAnchorX(0.58F, -2.0F), decodeAnchorY(2.88F, 0.0F), decodeX(0.58F), decodeY(2.88F));
/* 303 */     this.path.lineTo(decodeX(2.4F), decodeY(2.873334F));
/* 304 */     this.path.curveTo(decodeAnchorX(2.4F, 1.970929F), decodeAnchorY(2.873334F, 0.019857F), decodeAnchorX(2.866667F, -0.03333334F), decodeAnchorY(2.433333F, 1.933333F), decodeX(2.866667F), decodeY(2.433333F));
/* 305 */     this.path.lineTo(decodeX(2.873334F), decodeY(1.940741F));
/* 306 */     this.path.lineTo(decodeX(2.866667F), decodeY(1.181482F));
/* 307 */     this.path.lineTo(decodeX(2.866667F), decodeY(0.6066667F));
/* 308 */     this.path.curveTo(decodeAnchorX(2.866667F, 0.00421733F), decodeAnchorY(0.6066667F, -1.950338F), decodeAnchorX(2.46F, 1.965946F), decodeAnchorY(0.1333333F, 0.01712227F), decodeX(2.46F), decodeY(0.1333333F));
/* 309 */     this.path.lineTo(decodeX(0.5466667F), decodeY(0.1266667F));
/* 310 */     this.path.closePath();
/* 311 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect5() {
/* 315 */     this.rect.setRect(decodeX(0.0F), decodeY(0.0F), decodeX(3.0F) - decodeX(0.0F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 319 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 323 */     this.path.reset();
/* 324 */     this.path.moveTo(decodeX(0.0F), decodeY(1.333333F));
/* 325 */     this.path.curveTo(decodeAnchorX(0.0F, 2.678572F), decodeAnchorY(1.333333F, 8.881784E-016F), decodeAnchorX(1.367857F, -6.214286F), decodeAnchorY(0.2071429F, -0.03571429F), decodeX(1.367857F), decodeY(0.2071429F));
/* 326 */     this.path.lineTo(decodeX(1.564286F), decodeY(0.2071429F));
/* 327 */     this.path.curveTo(decodeAnchorX(1.564286F, 8.32967F), decodeAnchorY(0.2071429F, 0.002747253F), decodeAnchorX(2.6F, -5.285714F), decodeAnchorY(1.333333F, 0.03571429F), decodeX(2.6F), decodeY(1.333333F));
/* 328 */     this.path.lineTo(decodeX(3.0F), decodeY(1.333333F));
/* 329 */     this.path.lineTo(decodeX(3.0F), decodeY(1.666667F));
/* 330 */     this.path.lineTo(decodeX(2.6F), decodeY(1.666667F));
/* 331 */     this.path.curveTo(decodeAnchorX(2.6F, -5.321429F), decodeAnchorY(1.666667F, 0.03571429F), decodeAnchorX(1.564286F, 8.983517F), decodeAnchorY(2.8F, 0.0384615F), decodeX(1.564286F), decodeY(2.8F));
/* 332 */     this.path.lineTo(decodeX(1.389286F), decodeY(2.8F));
/* 333 */     this.path.curveTo(decodeAnchorX(1.389286F, -6.714286F), decodeAnchorY(2.8F, 0.0F), decodeAnchorX(0.0F, 2.607143F), decodeAnchorY(1.666667F, 0.03571429F), decodeX(0.0F), decodeY(1.666667F));
/* 334 */     this.path.lineTo(decodeX(0.0F), decodeY(1.333333F));
/* 335 */     this.path.closePath();
/* 336 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect6() {
/* 340 */     this.rect.setRect(decodeX(1.25F), decodeY(0.0F), decodeX(1.3F) - decodeX(1.25F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 344 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect7() {
/* 348 */     this.rect.setRect(decodeX(1.75F), decodeY(0.0F), decodeX(1.8F) - decodeX(1.75F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 352 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 356 */     this.path.reset();
/* 357 */     this.path.moveTo(decodeX(0.5933334F), decodeY(0.12F));
/* 358 */     this.path.curveTo(decodeAnchorX(0.5933334F, -2.0F), decodeAnchorY(0.12F, 0.0F), decodeAnchorX(0.12F, 0.0F), decodeAnchorY(0.5933334F, -2.0F), decodeX(0.12F), decodeY(0.5933334F));
/* 359 */     this.path.curveTo(decodeAnchorX(0.12F, 0.0F), decodeAnchorY(0.5933334F, 2.0F), decodeAnchorX(0.12F, 0.0F), decodeAnchorY(2.393333F, -2.0F), decodeX(0.12F), decodeY(2.393333F));
/* 360 */     this.path.curveTo(decodeAnchorX(0.12F, 0.0F), decodeAnchorY(2.393333F, 2.0F), decodeAnchorX(0.5933334F, -2.0F), decodeAnchorY(2.886667F, 0.0F), decodeX(0.5933334F), decodeY(2.886667F));
/* 361 */     this.path.curveTo(decodeAnchorX(0.5933334F, 2.0F), decodeAnchorY(2.886667F, 0.0F), decodeAnchorX(2.7F, 0.0F), decodeAnchorY(2.88F, 0.0F), decodeX(2.7F), decodeY(2.88F));
/* 362 */     this.path.lineTo(decodeX(2.846667F), decodeY(2.693333F));
/* 363 */     this.path.lineTo(decodeX(2.853333F), decodeY(1.614815F));
/* 364 */     this.path.lineTo(decodeX(2.86F), decodeY(1.407407F));
/* 365 */     this.path.lineTo(decodeX(2.86F), decodeY(0.3733333F));
/* 366 */     this.path.lineTo(decodeX(2.76F), decodeY(0.1333333F));
/* 367 */     this.path.curveTo(decodeAnchorX(2.76F, 0.0F), decodeAnchorY(0.1333333F, 0.0F), decodeAnchorX(0.5933334F, 2.0F), decodeAnchorY(0.12F, 0.0F), decodeX(0.5933334F), decodeY(0.12F));
/* 368 */     this.path.closePath();
/* 369 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath5() {
/* 373 */     this.path.reset();
/* 374 */     this.path.moveTo(decodeX(0.0F), decodeY(1.333333F));
/* 375 */     this.path.curveTo(decodeAnchorX(0.0F, 2.678572F), decodeAnchorY(1.333333F, 8.881784E-016F), decodeAnchorX(1.367857F, -6.357143F), decodeAnchorY(0.2071429F, -0.03571429F), decodeX(1.367857F), decodeY(0.2071429F));
/* 376 */     this.path.lineTo(decodeX(1.564286F), decodeY(0.2071429F));
/* 377 */     this.path.curveTo(decodeAnchorX(1.564286F, 4.0F), decodeAnchorY(0.2071429F, 0.0F), decodeAnchorX(2.6F, -5.285714F), decodeAnchorY(1.333333F, 0.03571429F), decodeX(2.6F), decodeY(1.333333F));
/* 378 */     this.path.lineTo(decodeX(3.0F), decodeY(1.333333F));
/* 379 */     this.path.lineTo(decodeX(3.0F), decodeY(1.666667F));
/* 380 */     this.path.lineTo(decodeX(2.6F), decodeY(1.666667F));
/* 381 */     this.path.curveTo(decodeAnchorX(2.6F, -5.321429F), decodeAnchorY(1.666667F, 0.03571429F), decodeAnchorX(1.564286F, 4.0F), decodeAnchorY(2.8F, 0.0F), decodeX(1.564286F), decodeY(2.8F));
/* 382 */     this.path.lineTo(decodeX(1.389286F), decodeY(2.8F));
/* 383 */     this.path.curveTo(decodeAnchorX(1.389286F, -6.571429F), decodeAnchorY(2.8F, -0.03571429F), decodeAnchorX(0.0F, 2.607143F), decodeAnchorY(1.666667F, 0.03571429F), decodeX(0.0F), decodeY(1.666667F));
/* 384 */     this.path.lineTo(decodeX(0.0F), decodeY(1.333333F));
/* 385 */     this.path.closePath();
/* 386 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 392 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 393 */     float f1 = (float)localRectangle2D.getX();
/* 394 */     float f2 = (float)localRectangle2D.getY();
/* 395 */     float f3 = (float)localRectangle2D.getWidth();
/* 396 */     float f4 = (float)localRectangle2D.getHeight();
/* 397 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 405 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 406 */     float f1 = (float)localRectangle2D.getX();
/* 407 */     float f2 = (float)localRectangle2D.getY();
/* 408 */     float f3 = (float)localRectangle2D.getWidth();
/* 409 */     float f4 = (float)localRectangle2D.getHeight();
/* 410 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.03870968F, 0.05967742F, 0.08064516F, 0.2370968F, 0.393548F, 0.4161291F, 0.4387097F, 0.6741936F, 0.9096775F, 0.9145162F, 0.9193549F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5, decodeColor(this.color5, this.color6, 0.5F), this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 426 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 427 */     float f1 = (float)localRectangle2D.getX();
/* 428 */     float f2 = (float)localRectangle2D.getY();
/* 429 */     float f3 = (float)localRectangle2D.getWidth();
/* 430 */     float f4 = (float)localRectangle2D.getHeight();
/* 431 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0548387F, 0.503226F, 0.9516129F }, new Color[] { this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 439 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 440 */     float f1 = (float)localRectangle2D.getX();
/* 441 */     float f2 = (float)localRectangle2D.getY();
/* 442 */     float f3 = (float)localRectangle2D.getWidth();
/* 443 */     float f4 = (float)localRectangle2D.getHeight();
/* 444 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.03870968F, 0.05967742F, 0.08064516F, 0.2370968F, 0.393548F, 0.4161291F, 0.4387097F, 0.6741936F, 0.9096775F, 0.9161291F, 0.9225807F }, new Color[] { this.color11, decodeColor(this.color11, this.color12, 0.5F), this.color12, decodeColor(this.color12, this.color13, 0.5F), this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14, decodeColor(this.color14, this.color15, 0.5F), this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 460 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 461 */     float f1 = (float)localRectangle2D.getX();
/* 462 */     float f2 = (float)localRectangle2D.getY();
/* 463 */     float f3 = (float)localRectangle2D.getWidth();
/* 464 */     float f4 = (float)localRectangle2D.getHeight();
/* 465 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.03870968F, 0.0548387F, 0.07096774F, 0.2806452F, 0.4903226F, 0.6967742F, 0.9032258F, 0.924194F, 0.9451613F }, new Color[] { this.color18, decodeColor(this.color18, this.color19, 0.5F), this.color19, decodeColor(this.color19, this.color20, 0.5F), this.color20, decodeColor(this.color20, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 479 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 480 */     float f1 = (float)localRectangle2D.getX();
/* 481 */     float f2 = (float)localRectangle2D.getY();
/* 482 */     float f3 = (float)localRectangle2D.getWidth();
/* 483 */     float f4 = (float)localRectangle2D.getHeight();
/* 484 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.03870968F, 0.06129032F, 0.083871F, 0.2725807F, 0.4612903F, 0.4903226F, 0.5193548F, 0.717742F, 0.9161291F, 0.9241936F, 0.9322581F }, new Color[] { this.color23, decodeColor(this.color23, this.color24, 0.5F), this.color24, decodeColor(this.color24, this.color25, 0.5F), this.color25, decodeColor(this.color25, this.color26, 0.5F), this.color26, decodeColor(this.color26, this.color27, 0.5F), this.color27, decodeColor(this.color27, this.color28, 0.5F), this.color28 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 500 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 501 */     float f1 = (float)localRectangle2D.getX();
/* 502 */     float f2 = (float)localRectangle2D.getY();
/* 503 */     float f3 = (float)localRectangle2D.getWidth();
/* 504 */     float f4 = (float)localRectangle2D.getHeight();
/* 505 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0516129F, 0.06612903F, 0.08064516F, 0.2935484F, 0.5064516F, 0.6903226F, 0.8741936F, 0.8887097F, 0.9032258F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color29, 0.5F), this.color29, decodeColor(this.color29, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 519 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 520 */     float f1 = (float)localRectangle2D.getX();
/* 521 */     float f2 = (float)localRectangle2D.getY();
/* 522 */     float f3 = (float)localRectangle2D.getWidth();
/* 523 */     float f4 = (float)localRectangle2D.getHeight();
/* 524 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2064516F, 0.4129032F, 0.4419355F, 0.4709677F, 0.7354839F, 1.0F }, new Color[] { this.color24, decodeColor(this.color24, this.color25, 0.5F), this.color25, decodeColor(this.color25, this.color26, 0.5F), this.color26, decodeColor(this.color26, this.color30, 0.5F), this.color30 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient9(Shape paramShape)
/*     */   {
/* 536 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 537 */     float f1 = (float)localRectangle2D.getX();
/* 538 */     float f2 = (float)localRectangle2D.getY();
/* 539 */     float f3 = (float)localRectangle2D.getWidth();
/* 540 */     float f4 = (float)localRectangle2D.getHeight();
/* 541 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.03870968F, 0.0548387F, 0.07096774F, 0.2806452F, 0.4903226F, 0.6967742F, 0.9032258F, 0.924194F, 0.9451613F }, new Color[] { this.color34, decodeColor(this.color34, this.color35, 0.5F), this.color35, decodeColor(this.color35, this.color36, 0.5F), this.color36, decodeColor(this.color36, this.color37, 0.5F), this.color37, decodeColor(this.color37, this.color38, 0.5F), this.color38 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient10(Shape paramShape)
/*     */   {
/* 555 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 556 */     float f1 = (float)localRectangle2D.getX();
/* 557 */     float f2 = (float)localRectangle2D.getY();
/* 558 */     float f3 = (float)localRectangle2D.getWidth();
/* 559 */     float f4 = (float)localRectangle2D.getHeight();
/* 560 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.03870968F, 0.06129032F, 0.083871F, 0.2725807F, 0.4612903F, 0.4903226F, 0.5193548F, 0.717742F, 0.9161291F, 0.9241936F, 0.9322581F }, new Color[] { this.color39, decodeColor(this.color39, this.color40, 0.5F), this.color40, decodeColor(this.color40, this.color41, 0.5F), this.color41, decodeColor(this.color41, this.color42, 0.5F), this.color42, decodeColor(this.color42, this.color43, 0.5F), this.color43, decodeColor(this.color43, this.color44, 0.5F), this.color44 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient11(Shape paramShape)
/*     */   {
/* 576 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 577 */     float f1 = (float)localRectangle2D.getX();
/* 578 */     float f2 = (float)localRectangle2D.getY();
/* 579 */     float f3 = (float)localRectangle2D.getWidth();
/* 580 */     float f4 = (float)localRectangle2D.getHeight();
/* 581 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0516129F, 0.06612903F, 0.08064516F, 0.2935484F, 0.5064516F, 0.6903226F, 0.8741936F, 0.8887097F, 0.9032258F }, new Color[] { this.color45, decodeColor(this.color45, this.color46, 0.5F), this.color46, decodeColor(this.color46, this.color47, 0.5F), this.color47, decodeColor(this.color47, this.color48, 0.5F), this.color48, decodeColor(this.color48, this.color49, 0.5F), this.color49 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient12(Shape paramShape)
/*     */   {
/* 595 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 596 */     float f1 = (float)localRectangle2D.getX();
/* 597 */     float f2 = (float)localRectangle2D.getY();
/* 598 */     float f3 = (float)localRectangle2D.getWidth();
/* 599 */     float f4 = (float)localRectangle2D.getHeight();
/* 600 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2064516F, 0.4129032F, 0.4419355F, 0.4709677F, 0.7354839F, 1.0F }, new Color[] { this.color40, decodeColor(this.color40, this.color41, 0.5F), this.color41, decodeColor(this.color41, this.color42, 0.5F), this.color42, decodeColor(this.color42, this.color50, 0.5F), this.color50 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ProgressBarPainter
 * JD-Core Version:    0.6.2
 */
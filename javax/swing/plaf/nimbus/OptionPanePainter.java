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
/*     */ final class OptionPanePainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int ERRORICON_ENABLED = 2;
/*     */   static final int INFORMATIONICON_ENABLED = 3;
/*     */   static final int QUESTIONICON_ENABLED = 4;
/*     */   static final int WARNINGICON_ENABLED = 5;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  50 */   private Path2D path = new Path2D.Float();
/*  51 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  52 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  53 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  58 */   private Color color1 = decodeColor("nimbusRed", -0.01481481F, 0.1838424F, 0.01568627F, 0);
/*  59 */   private Color color2 = decodeColor("nimbusRed", -0.01481481F, -0.403261F, 0.2196078F, 0);
/*  60 */   private Color color3 = decodeColor("nimbusRed", -0.01481481F, -0.07154381F, 0.1137255F, 0);
/*  61 */   private Color color4 = decodeColor("nimbusRed", -0.01481481F, 0.1102744F, 0.07058823F, 0);
/*  62 */   private Color color5 = decodeColor("nimbusRed", -0.01481481F, -0.0541357F, 0.2588235F, 0);
/*  63 */   private Color color6 = new Color(250, 250, 250, 255);
/*  64 */   private Color color7 = decodeColor("nimbusRed", 0.0F, -0.7988166F, 0.3372549F, -187);
/*  65 */   private Color color8 = new Color(255, 200, 0, 255);
/*  66 */   private Color color9 = decodeColor("nimbusInfoBlue", 0.0F, 0.06231594F, -0.05490196F, 0);
/*  67 */   private Color color10 = decodeColor("nimbusInfoBlue", 0.0003162026F, 0.07790506F, -0.1921569F, 0);
/*  68 */   private Color color11 = decodeColor("nimbusInfoBlue", -0.0008229613F, -0.4463124F, 0.1921569F, 0);
/*  69 */   private Color color12 = decodeColor("nimbusInfoBlue", 0.001272917F, -0.0739674F, 0.04313725F, 0);
/*  70 */   private Color color13 = decodeColor("nimbusInfoBlue", 0.000835419F, -0.1414863F, 0.2F, 0);
/*  71 */   private Color color14 = decodeColor("nimbusInfoBlue", -0.001479387F, -0.4145646F, 0.1647059F, 0);
/*  72 */   private Color color15 = decodeColor("nimbusInfoBlue", 0.0003437996F, -0.1472659F, 0.04313725F, 0);
/*  73 */   private Color color16 = decodeColor("nimbusInfoBlue", -0.0004271865F, -0.005555511F, 0.0F, 0);
/*  74 */   private Color color17 = decodeColor("nimbusInfoBlue", 0.0F, 0.0F, 0.0F, 0);
/*  75 */   private Color color18 = decodeColor("nimbusInfoBlue", -0.000786662F, -0.1272817F, 0.172549F, 0);
/*  76 */   private Color color19 = new Color(115, 120, 126, 255);
/*  77 */   private Color color20 = new Color(26, 34, 43, 255);
/*  78 */   private Color color21 = new Color(168, 173, 178, 255);
/*  79 */   private Color color22 = new Color(101, 109, 118, 255);
/*  80 */   private Color color23 = new Color(159, 163, 168, 255);
/*  81 */   private Color color24 = new Color(116, 122, 130, 255);
/*  82 */   private Color color25 = new Color(96, 104, 112, 255);
/*  83 */   private Color color26 = new Color(118, 128, 138, 255);
/*  84 */   private Color color27 = new Color(255, 255, 255, 255);
/*  85 */   private Color color28 = decodeColor("nimbusAlertYellow", -0.0004910231F, 0.1372549F, -0.1529412F, 0);
/*  86 */   private Color color29 = decodeColor("nimbusAlertYellow", -0.0015973F, 0.1372549F, -0.3490196F, 0);
/*  87 */   private Color color30 = decodeColor("nimbusAlertYellow", 0.0006530881F, -0.4078431F, 0.0F, 0);
/*  88 */   private Color color31 = decodeColor("nimbusAlertYellow", -0.0003945679F, -0.1098039F, 0.0F, 0);
/*  89 */   private Color color32 = decodeColor("nimbusAlertYellow", 0.0F, 0.0F, 0.0F, 0);
/*  90 */   private Color color33 = decodeColor("nimbusAlertYellow", 0.008085668F, -0.04705882F, 0.0F, 0);
/*  91 */   private Color color34 = decodeColor("nimbusAlertYellow", 0.02651516F, -0.1843137F, 0.0F, 0);
/*  92 */   private Color color35 = new Color(69, 69, 69, 255);
/*  93 */   private Color color36 = new Color(0, 0, 0, 255);
/*  94 */   private Color color37 = new Color(16, 16, 16, 255);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public OptionPanePainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/* 102 */     this.state = paramInt;
/* 103 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 109 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 112 */     switch (this.state) { case 2:
/* 113 */       painterrorIconEnabled(paramGraphics2D); break;
/*     */     case 3:
/* 114 */       paintinformationIconEnabled(paramGraphics2D); break;
/*     */     case 4:
/* 115 */       paintquestionIconEnabled(paramGraphics2D); break;
/*     */     case 5:
/* 116 */       paintwarningIconEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 125 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void painterrorIconEnabled(Graphics2D paramGraphics2D) {
/* 129 */     this.path = decodePath1();
/* 130 */     paramGraphics2D.setPaint(this.color1);
/* 131 */     paramGraphics2D.fill(this.path);
/* 132 */     this.path = decodePath2();
/* 133 */     paramGraphics2D.setPaint(decodeGradient1(this.path));
/* 134 */     paramGraphics2D.fill(this.path);
/* 135 */     this.path = decodePath3();
/* 136 */     paramGraphics2D.setPaint(this.color6);
/* 137 */     paramGraphics2D.fill(this.path);
/* 138 */     this.ellipse = decodeEllipse1();
/* 139 */     paramGraphics2D.setPaint(this.color6);
/* 140 */     paramGraphics2D.fill(this.ellipse);
/* 141 */     this.path = decodePath4();
/* 142 */     paramGraphics2D.setPaint(this.color7);
/* 143 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintinformationIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 148 */     this.ellipse = decodeEllipse2();
/* 149 */     paramGraphics2D.setPaint(this.color8);
/* 150 */     paramGraphics2D.fill(this.ellipse);
/* 151 */     this.ellipse = decodeEllipse2();
/* 152 */     paramGraphics2D.setPaint(this.color8);
/* 153 */     paramGraphics2D.fill(this.ellipse);
/* 154 */     this.ellipse = decodeEllipse2();
/* 155 */     paramGraphics2D.setPaint(this.color8);
/* 156 */     paramGraphics2D.fill(this.ellipse);
/* 157 */     this.ellipse = decodeEllipse3();
/* 158 */     paramGraphics2D.setPaint(decodeGradient2(this.ellipse));
/* 159 */     paramGraphics2D.fill(this.ellipse);
/* 160 */     this.ellipse = decodeEllipse4();
/* 161 */     paramGraphics2D.setPaint(decodeGradient3(this.ellipse));
/* 162 */     paramGraphics2D.fill(this.ellipse);
/* 163 */     this.ellipse = decodeEllipse5();
/* 164 */     paramGraphics2D.setPaint(decodeGradient4(this.ellipse));
/* 165 */     paramGraphics2D.fill(this.ellipse);
/* 166 */     this.path = decodePath5();
/* 167 */     paramGraphics2D.setPaint(this.color6);
/* 168 */     paramGraphics2D.fill(this.path);
/* 169 */     this.ellipse = decodeEllipse6();
/* 170 */     paramGraphics2D.setPaint(this.color6);
/* 171 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintquestionIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 176 */     this.ellipse = decodeEllipse3();
/* 177 */     paramGraphics2D.setPaint(decodeGradient5(this.ellipse));
/* 178 */     paramGraphics2D.fill(this.ellipse);
/* 179 */     this.ellipse = decodeEllipse4();
/* 180 */     paramGraphics2D.setPaint(decodeGradient6(this.ellipse));
/* 181 */     paramGraphics2D.fill(this.ellipse);
/* 182 */     this.ellipse = decodeEllipse5();
/* 183 */     paramGraphics2D.setPaint(decodeGradient7(this.ellipse));
/* 184 */     paramGraphics2D.fill(this.ellipse);
/* 185 */     this.path = decodePath6();
/* 186 */     paramGraphics2D.setPaint(this.color27);
/* 187 */     paramGraphics2D.fill(this.path);
/* 188 */     this.ellipse = decodeEllipse1();
/* 189 */     paramGraphics2D.setPaint(this.color27);
/* 190 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintwarningIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 195 */     this.rect = decodeRect1();
/* 196 */     paramGraphics2D.setPaint(this.color8);
/* 197 */     paramGraphics2D.fill(this.rect);
/* 198 */     this.path = decodePath7();
/* 199 */     paramGraphics2D.setPaint(decodeGradient8(this.path));
/* 200 */     paramGraphics2D.fill(this.path);
/* 201 */     this.path = decodePath8();
/* 202 */     paramGraphics2D.setPaint(decodeGradient9(this.path));
/* 203 */     paramGraphics2D.fill(this.path);
/* 204 */     this.path = decodePath9();
/* 205 */     paramGraphics2D.setPaint(decodeGradient10(this.path));
/* 206 */     paramGraphics2D.fill(this.path);
/* 207 */     this.ellipse = decodeEllipse7();
/* 208 */     paramGraphics2D.setPaint(this.color37);
/* 209 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 216 */     this.path.reset();
/* 217 */     this.path.moveTo(decodeX(1.0F), decodeY(1.270833F));
/* 218 */     this.path.lineTo(decodeX(1.270833F), decodeY(1.0F));
/* 219 */     this.path.lineTo(decodeX(1.6875F), decodeY(1.0F));
/* 220 */     this.path.lineTo(decodeX(1.958333F), decodeY(1.270833F));
/* 221 */     this.path.lineTo(decodeX(1.958333F), decodeY(1.6875F));
/* 222 */     this.path.lineTo(decodeX(1.6875F), decodeY(1.958333F));
/* 223 */     this.path.lineTo(decodeX(1.270833F), decodeY(1.958333F));
/* 224 */     this.path.lineTo(decodeX(1.0F), decodeY(1.6875F));
/* 225 */     this.path.lineTo(decodeX(1.0F), decodeY(1.270833F));
/* 226 */     this.path.closePath();
/* 227 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 231 */     this.path.reset();
/* 232 */     this.path.moveTo(decodeX(1.020833F), decodeY(1.291667F));
/* 233 */     this.path.lineTo(decodeX(1.291667F), decodeY(1.020833F));
/* 234 */     this.path.lineTo(decodeX(1.666667F), decodeY(1.020833F));
/* 235 */     this.path.lineTo(decodeX(1.9375F), decodeY(1.291667F));
/* 236 */     this.path.lineTo(decodeX(1.9375F), decodeY(1.666667F));
/* 237 */     this.path.lineTo(decodeX(1.666667F), decodeY(1.9375F));
/* 238 */     this.path.lineTo(decodeX(1.291667F), decodeY(1.9375F));
/* 239 */     this.path.lineTo(decodeX(1.020833F), decodeY(1.666667F));
/* 240 */     this.path.lineTo(decodeX(1.020833F), decodeY(1.291667F));
/* 241 */     this.path.closePath();
/* 242 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 246 */     this.path.reset();
/* 247 */     this.path.moveTo(decodeX(1.416667F), decodeY(1.229167F));
/* 248 */     this.path.curveTo(decodeAnchorX(1.416667F, 0.0F), decodeAnchorY(1.229167F, -2.0F), decodeAnchorX(1.479167F, -2.0F), decodeAnchorY(1.166667F, 0.0F), decodeX(1.479167F), decodeY(1.166667F));
/* 249 */     this.path.curveTo(decodeAnchorX(1.479167F, 2.0F), decodeAnchorY(1.166667F, 0.0F), decodeAnchorX(1.541667F, 0.0F), decodeAnchorY(1.229167F, -2.0F), decodeX(1.541667F), decodeY(1.229167F));
/* 250 */     this.path.curveTo(decodeAnchorX(1.541667F, 0.0F), decodeAnchorY(1.229167F, 2.0F), decodeAnchorX(1.5F, 0.0F), decodeAnchorY(1.604167F, 0.0F), decodeX(1.5F), decodeY(1.604167F));
/* 251 */     this.path.lineTo(decodeX(1.458333F), decodeY(1.604167F));
/* 252 */     this.path.curveTo(decodeAnchorX(1.458333F, 0.0F), decodeAnchorY(1.604167F, 0.0F), decodeAnchorX(1.416667F, 0.0F), decodeAnchorY(1.229167F, 2.0F), decodeX(1.416667F), decodeY(1.229167F));
/* 253 */     this.path.closePath();
/* 254 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse1() {
/* 258 */     this.ellipse.setFrame(decodeX(1.416667F), decodeY(1.666667F), decodeX(1.541667F) - decodeX(1.416667F), decodeY(1.791667F) - decodeY(1.666667F));
/*     */ 
/* 262 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 266 */     this.path.reset();
/* 267 */     this.path.moveTo(decodeX(1.020833F), decodeY(1.285156F));
/* 268 */     this.path.lineTo(decodeX(1.279948F), decodeY(1.020833F));
/* 269 */     this.path.lineTo(decodeX(1.678386F), decodeY(1.020833F));
/* 270 */     this.path.lineTo(decodeX(1.9375F), decodeY(1.28125F));
/* 271 */     this.path.lineTo(decodeX(1.9375F), decodeY(1.666667F));
/* 272 */     this.path.lineTo(decodeX(1.666667F), decodeY(1.9375F));
/* 273 */     this.path.lineTo(decodeX(1.285156F), decodeY(1.936198F));
/* 274 */     this.path.lineTo(decodeX(1.022135F), decodeY(1.673177F));
/* 275 */     this.path.lineTo(decodeX(1.020833F), decodeY(1.5F));
/* 276 */     this.path.lineTo(decodeX(1.041667F), decodeY(1.5F));
/* 277 */     this.path.lineTo(decodeX(1.041667F), decodeY(1.666667F));
/* 278 */     this.path.lineTo(decodeX(1.291667F), decodeY(1.916667F));
/* 279 */     this.path.lineTo(decodeX(1.666667F), decodeY(1.916667F));
/* 280 */     this.path.lineTo(decodeX(1.916667F), decodeY(1.666667F));
/* 281 */     this.path.lineTo(decodeX(1.916667F), decodeY(1.291667F));
/* 282 */     this.path.lineTo(decodeX(1.666667F), decodeY(1.041667F));
/* 283 */     this.path.lineTo(decodeX(1.291667F), decodeY(1.041667F));
/* 284 */     this.path.lineTo(decodeX(1.041667F), decodeY(1.291667F));
/* 285 */     this.path.lineTo(decodeX(1.041667F), decodeY(1.5F));
/* 286 */     this.path.lineTo(decodeX(1.020833F), decodeY(1.5F));
/* 287 */     this.path.lineTo(decodeX(1.020833F), decodeY(1.285156F));
/* 288 */     this.path.closePath();
/* 289 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse2() {
/* 293 */     this.ellipse.setFrame(decodeX(1.0F), decodeY(1.0F), decodeX(1.0F) - decodeX(1.0F), decodeY(1.0F) - decodeY(1.0F));
/*     */ 
/* 297 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse3() {
/* 301 */     this.ellipse.setFrame(decodeX(1.0F), decodeY(1.0F), decodeX(1.958333F) - decodeX(1.0F), decodeY(1.958333F) - decodeY(1.0F));
/*     */ 
/* 305 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse4() {
/* 309 */     this.ellipse.setFrame(decodeX(1.020833F), decodeY(1.020833F), decodeX(1.9375F) - decodeX(1.020833F), decodeY(1.9375F) - decodeY(1.020833F));
/*     */ 
/* 313 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse5() {
/* 317 */     this.ellipse.setFrame(decodeX(1.041667F), decodeY(1.041667F), decodeX(1.916667F) - decodeX(1.041667F), decodeY(1.916667F) - decodeY(1.041667F));
/*     */ 
/* 321 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath5() {
/* 325 */     this.path.reset();
/* 326 */     this.path.moveTo(decodeX(1.375F), decodeY(1.375F));
/* 327 */     this.path.curveTo(decodeAnchorX(1.375F, 2.5F), decodeAnchorY(1.375F, 0.0F), decodeAnchorX(1.5F, -1.1875F), decodeAnchorY(1.375F, 0.0F), decodeX(1.5F), decodeY(1.375F));
/* 328 */     this.path.curveTo(decodeAnchorX(1.5F, 1.1875F), decodeAnchorY(1.375F, 0.0F), decodeAnchorX(1.541667F, 0.0F), decodeAnchorY(1.4375F, -2.0F), decodeX(1.541667F), decodeY(1.4375F));
/* 329 */     this.path.curveTo(decodeAnchorX(1.541667F, 0.0F), decodeAnchorY(1.4375F, 2.0F), decodeAnchorX(1.541667F, 0.0F), decodeAnchorY(1.6875F, 0.0F), decodeX(1.541667F), decodeY(1.6875F));
/* 330 */     this.path.curveTo(decodeAnchorX(1.541667F, 0.0F), decodeAnchorY(1.6875F, 0.0F), decodeAnchorX(1.602865F, -2.5625F), decodeAnchorY(1.6875F, 0.0625F), decodeX(1.602865F), decodeY(1.6875F));
/* 331 */     this.path.curveTo(decodeAnchorX(1.602865F, 2.5625F), decodeAnchorY(1.6875F, -0.0625F), decodeAnchorX(1.604167F, 2.5625F), decodeAnchorY(1.770833F, 0.0F), decodeX(1.604167F), decodeY(1.770833F));
/* 332 */     this.path.curveTo(decodeAnchorX(1.604167F, -2.5625F), decodeAnchorY(1.770833F, 0.0F), decodeAnchorX(1.356771F, 2.5F), decodeAnchorY(1.770833F, 0.0625F), decodeX(1.356771F), decodeY(1.770833F));
/* 333 */     this.path.curveTo(decodeAnchorX(1.356771F, -2.5F), decodeAnchorY(1.770833F, -0.0625F), decodeAnchorX(1.354167F, -2.4375F), decodeAnchorY(1.6875F, 0.0F), decodeX(1.354167F), decodeY(1.6875F));
/* 334 */     this.path.curveTo(decodeAnchorX(1.354167F, 2.4375F), decodeAnchorY(1.6875F, 0.0F), decodeAnchorX(1.416667F, 0.0F), decodeAnchorY(1.6875F, 0.0F), decodeX(1.416667F), decodeY(1.6875F));
/* 335 */     this.path.lineTo(decodeX(1.416667F), decodeY(1.458333F));
/* 336 */     this.path.curveTo(decodeAnchorX(1.416667F, 0.0F), decodeAnchorY(1.458333F, 0.0F), decodeAnchorX(1.375F, 2.75F), decodeAnchorY(1.458333F, 0.0F), decodeX(1.375F), decodeY(1.458333F));
/* 337 */     this.path.curveTo(decodeAnchorX(1.375F, -2.75F), decodeAnchorY(1.458333F, 0.0F), decodeAnchorX(1.375F, -2.5F), decodeAnchorY(1.375F, 0.0F), decodeX(1.375F), decodeY(1.375F));
/* 338 */     this.path.closePath();
/* 339 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse6() {
/* 343 */     this.ellipse.setFrame(decodeX(1.416667F), decodeY(1.166667F), decodeX(1.541667F) - decodeX(1.416667F), decodeY(1.291667F) - decodeY(1.166667F));
/*     */ 
/* 347 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath6() {
/* 351 */     this.path.reset();
/* 352 */     this.path.moveTo(decodeX(1.3125F), decodeY(1.372396F));
/* 353 */     this.path.curveTo(decodeAnchorX(1.3125F, 1.5F), decodeAnchorY(1.372396F, 1.375F), decodeAnchorX(1.39974F, -0.75F), decodeAnchorY(1.358073F, 1.1875F), decodeX(1.39974F), decodeY(1.358073F));
/* 354 */     this.path.curveTo(decodeAnchorX(1.39974F, 0.75F), decodeAnchorY(1.358073F, -1.1875F), decodeAnchorX(1.46875F, -1.8125F), decodeAnchorY(1.290365F, 0.0F), decodeX(1.46875F), decodeY(1.290365F));
/* 355 */     this.path.curveTo(decodeAnchorX(1.46875F, 1.8125F), decodeAnchorY(1.290365F, 0.0F), decodeAnchorX(1.535156F, 0.0F), decodeAnchorY(1.35026F, -1.5625F), decodeX(1.535156F), decodeY(1.35026F));
/* 356 */     this.path.curveTo(decodeAnchorX(1.535156F, 0.0F), decodeAnchorY(1.35026F, 1.5625F), decodeAnchorX(1.470052F, 1.25F), decodeAnchorY(1.428385F, -1.1875F), decodeX(1.470052F), decodeY(1.428385F));
/* 357 */     this.path.curveTo(decodeAnchorX(1.470052F, -1.25F), decodeAnchorY(1.428385F, 1.1875F), decodeAnchorX(1.417969F, -0.0625F), decodeAnchorY(1.544271F, -1.5F), decodeX(1.417969F), decodeY(1.544271F));
/* 358 */     this.path.curveTo(decodeAnchorX(1.417969F, 0.0625F), decodeAnchorY(1.544271F, 1.5F), decodeAnchorX(1.476563F, -1.3125F), decodeAnchorY(1.602865F, 0.0F), decodeX(1.476563F), decodeY(1.602865F));
/* 359 */     this.path.curveTo(decodeAnchorX(1.476563F, 1.3125F), decodeAnchorY(1.602865F, 0.0F), decodeAnchorX(1.540365F, 0.0F), decodeAnchorY(1.546875F, 1.625F), decodeX(1.540365F), decodeY(1.546875F));
/* 360 */     this.path.curveTo(decodeAnchorX(1.540365F, 0.0F), decodeAnchorY(1.546875F, -1.625F), decodeAnchorX(1.613281F, -1.1875F), decodeAnchorY(1.464844F, 1.25F), decodeX(1.613281F), decodeY(1.464844F));
/* 361 */     this.path.curveTo(decodeAnchorX(1.613281F, 1.1875F), decodeAnchorY(1.464844F, -1.25F), decodeAnchorX(1.666667F, 0.0625F), decodeAnchorY(1.346354F, 3.3125F), decodeX(1.666667F), decodeY(1.346354F));
/* 362 */     this.path.curveTo(decodeAnchorX(1.666667F, -0.0625F), decodeAnchorY(1.346354F, -3.3125F), decodeAnchorX(1.483073F, 6.125F), decodeAnchorY(1.167969F, -0.0625F), decodeX(1.483073F), decodeY(1.167969F));
/* 363 */     this.path.curveTo(decodeAnchorX(1.483073F, -6.125F), decodeAnchorY(1.167969F, 0.0625F), decodeAnchorX(1.304688F, 0.4375F), decodeAnchorY(1.289063F, -1.25F), decodeX(1.304688F), decodeY(1.289063F));
/* 364 */     this.path.curveTo(decodeAnchorX(1.304688F, -0.4375F), decodeAnchorY(1.289063F, 1.25F), decodeAnchorX(1.3125F, -1.5F), decodeAnchorY(1.372396F, -1.375F), decodeX(1.3125F), decodeY(1.372396F));
/* 365 */     this.path.closePath();
/* 366 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1() {
/* 370 */     this.rect.setRect(decodeX(1.0F), decodeY(1.0F), decodeX(1.0F) - decodeX(1.0F), decodeY(1.0F) - decodeY(1.0F));
/*     */ 
/* 374 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath7() {
/* 378 */     this.path.reset();
/* 379 */     this.path.moveTo(decodeX(1.5F), decodeY(1.020833F));
/* 380 */     this.path.curveTo(decodeAnchorX(1.5F, 2.0F), decodeAnchorY(1.020833F, 0.0F), decodeAnchorX(1.566406F, 0.0F), decodeAnchorY(1.082031F, 0.0F), decodeX(1.566406F), decodeY(1.082031F));
/* 381 */     this.path.lineTo(decodeX(1.942708F), decodeY(1.779948F));
/* 382 */     this.path.curveTo(decodeAnchorX(1.942708F, 0.0F), decodeAnchorY(1.779948F, 0.0F), decodeAnchorX(1.975261F, 0.0F), decodeAnchorY(1.880208F, -2.375F), decodeX(1.975261F), decodeY(1.880208F));
/* 383 */     this.path.curveTo(decodeAnchorX(1.975261F, 0.0F), decodeAnchorY(1.880208F, 2.375F), decodeAnchorX(1.916667F, 0.0F), decodeAnchorY(1.9375F, 0.0F), decodeX(1.916667F), decodeY(1.9375F));
/* 384 */     this.path.lineTo(decodeX(1.083333F), decodeY(1.9375F));
/* 385 */     this.path.curveTo(decodeAnchorX(1.083333F, 0.0F), decodeAnchorY(1.9375F, 0.0F), decodeAnchorX(1.02474F, 0.125F), decodeAnchorY(1.881511F, 2.25F), decodeX(1.02474F), decodeY(1.881511F));
/* 386 */     this.path.curveTo(decodeAnchorX(1.02474F, -0.125F), decodeAnchorY(1.881511F, -2.25F), decodeAnchorX(1.059896F, 0.0F), decodeAnchorY(1.78125F, 0.0F), decodeX(1.059896F), decodeY(1.78125F));
/* 387 */     this.path.lineTo(decodeX(1.4375F), decodeY(1.083333F));
/* 388 */     this.path.curveTo(decodeAnchorX(1.4375F, 0.0F), decodeAnchorY(1.083333F, 0.0F), decodeAnchorX(1.5F, -2.0F), decodeAnchorY(1.020833F, 0.0F), decodeX(1.5F), decodeY(1.020833F));
/* 389 */     this.path.closePath();
/* 390 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath8() {
/* 394 */     this.path.reset();
/* 395 */     this.path.moveTo(decodeX(1.498698F), decodeY(1.042969F));
/* 396 */     this.path.curveTo(decodeAnchorX(1.498698F, 1.75F), decodeAnchorY(1.042969F, 0.0F), decodeAnchorX(1.554688F, 0.0F), decodeAnchorY(1.095052F, 0.0F), decodeX(1.554688F), decodeY(1.095052F));
/* 397 */     this.path.lineTo(decodeX(1.932292F), decodeY(1.800781F));
/* 398 */     this.path.curveTo(decodeAnchorX(1.932292F, 0.0F), decodeAnchorY(1.800781F, 0.0F), decodeAnchorX(1.957031F, 0.0F), decodeAnchorY(1.875F, -1.4375F), decodeX(1.957031F), decodeY(1.875F));
/* 399 */     this.path.curveTo(decodeAnchorX(1.957031F, 0.0F), decodeAnchorY(1.875F, 1.4375F), decodeAnchorX(1.884115F, 0.0F), decodeAnchorY(1.916667F, 0.0F), decodeX(1.884115F), decodeY(1.916667F));
/* 400 */     this.path.lineTo(decodeX(1.10026F), decodeY(1.916667F));
/* 401 */     this.path.curveTo(decodeAnchorX(1.10026F, 0.0F), decodeAnchorY(1.916667F, 0.0F), decodeAnchorX(1.045573F, 0.0625F), decodeAnchorY(1.872396F, 1.625F), decodeX(1.045573F), decodeY(1.872396F));
/* 402 */     this.path.curveTo(decodeAnchorX(1.045573F, -0.0625F), decodeAnchorY(1.872396F, -1.625F), decodeAnchorX(1.075521F, 0.0F), decodeAnchorY(1.790365F, 0.0F), decodeX(1.075521F), decodeY(1.790365F));
/* 403 */     this.path.lineTo(decodeX(1.441406F), decodeY(1.102865F));
/* 404 */     this.path.curveTo(decodeAnchorX(1.441406F, 0.0F), decodeAnchorY(1.102865F, 0.0F), decodeAnchorX(1.498698F, -1.75F), decodeAnchorY(1.042969F, 0.0F), decodeX(1.498698F), decodeY(1.042969F));
/* 405 */     this.path.closePath();
/* 406 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath9() {
/* 410 */     this.path.reset();
/* 411 */     this.path.moveTo(decodeX(1.5F), decodeY(1.229167F));
/* 412 */     this.path.curveTo(decodeAnchorX(1.5F, 2.0F), decodeAnchorY(1.229167F, 0.0F), decodeAnchorX(1.5625F, 0.0F), decodeAnchorY(1.3125F, -2.0F), decodeX(1.5625F), decodeY(1.3125F));
/* 413 */     this.path.curveTo(decodeAnchorX(1.5625F, 0.0F), decodeAnchorY(1.3125F, 2.0F), decodeAnchorX(1.5F, 1.3125F), decodeAnchorY(1.666667F, 0.0F), decodeX(1.5F), decodeY(1.666667F));
/* 414 */     this.path.curveTo(decodeAnchorX(1.5F, -1.3125F), decodeAnchorY(1.666667F, 0.0F), decodeAnchorX(1.4375F, 0.0F), decodeAnchorY(1.3125F, 2.0F), decodeX(1.4375F), decodeY(1.3125F));
/* 415 */     this.path.curveTo(decodeAnchorX(1.4375F, 0.0F), decodeAnchorY(1.3125F, -2.0F), decodeAnchorX(1.5F, -2.0F), decodeAnchorY(1.229167F, 0.0F), decodeX(1.5F), decodeY(1.229167F));
/* 416 */     this.path.closePath();
/* 417 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse7() {
/* 421 */     this.ellipse.setFrame(decodeX(1.4375F), decodeY(1.729167F), decodeX(1.5625F) - decodeX(1.4375F), decodeY(1.854167F) - decodeY(1.729167F));
/*     */ 
/* 425 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 431 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 432 */     float f1 = (float)localRectangle2D.getX();
/* 433 */     float f2 = (float)localRectangle2D.getY();
/* 434 */     float f3 = (float)localRectangle2D.getWidth();
/* 435 */     float f4 = (float)localRectangle2D.getHeight();
/* 436 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1725806F, 0.3451613F, 0.5145161F, 0.683871F, 0.9F, 1.0F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 448 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 449 */     float f1 = (float)localRectangle2D.getX();
/* 450 */     float f2 = (float)localRectangle2D.getY();
/* 451 */     float f3 = (float)localRectangle2D.getWidth();
/* 452 */     float f4 = (float)localRectangle2D.getHeight();
/* 453 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 461 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 462 */     float f1 = (float)localRectangle2D.getX();
/* 463 */     float f2 = (float)localRectangle2D.getY();
/* 464 */     float f3 = (float)localRectangle2D.getWidth();
/* 465 */     float f4 = (float)localRectangle2D.getHeight();
/* 466 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2414384F, 0.4828767F, 0.7414384F, 1.0F }, new Color[] { this.color11, decodeColor(this.color11, this.color12, 0.5F), this.color12, decodeColor(this.color12, this.color13, 0.5F), this.color13 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 476 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 477 */     float f1 = (float)localRectangle2D.getX();
/* 478 */     float f2 = (float)localRectangle2D.getY();
/* 479 */     float f3 = (float)localRectangle2D.getWidth();
/* 480 */     float f4 = (float)localRectangle2D.getHeight();
/* 481 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1421233F, 0.2842466F, 0.3921233F, 0.5F, 0.609589F, 0.7191781F, 0.859589F, 1.0F }, new Color[] { this.color14, decodeColor(this.color14, this.color15, 0.5F), this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 495 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 496 */     float f1 = (float)localRectangle2D.getX();
/* 497 */     float f2 = (float)localRectangle2D.getY();
/* 498 */     float f3 = (float)localRectangle2D.getWidth();
/* 499 */     float f4 = (float)localRectangle2D.getHeight();
/* 500 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color19, decodeColor(this.color19, this.color20, 0.5F), this.color20 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 508 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 509 */     float f1 = (float)localRectangle2D.getX();
/* 510 */     float f2 = (float)localRectangle2D.getY();
/* 511 */     float f3 = (float)localRectangle2D.getWidth();
/* 512 */     float f4 = (float)localRectangle2D.getHeight();
/* 513 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 521 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 522 */     float f1 = (float)localRectangle2D.getX();
/* 523 */     float f2 = (float)localRectangle2D.getY();
/* 524 */     float f3 = (float)localRectangle2D.getWidth();
/* 525 */     float f4 = (float)localRectangle2D.getHeight();
/* 526 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1523973F, 0.3047945F, 0.4794521F, 0.65411F, 0.827055F, 1.0F }, new Color[] { this.color23, decodeColor(this.color23, this.color24, 0.5F), this.color24, decodeColor(this.color24, this.color25, 0.5F), this.color25, decodeColor(this.color25, this.color26, 0.5F), this.color26 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 538 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 539 */     float f1 = (float)localRectangle2D.getX();
/* 540 */     float f2 = (float)localRectangle2D.getY();
/* 541 */     float f3 = (float)localRectangle2D.getWidth();
/* 542 */     float f4 = (float)localRectangle2D.getHeight();
/* 543 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color28, decodeColor(this.color28, this.color29, 0.5F), this.color29 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient9(Shape paramShape)
/*     */   {
/* 551 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 552 */     float f1 = (float)localRectangle2D.getX();
/* 553 */     float f2 = (float)localRectangle2D.getY();
/* 554 */     float f3 = (float)localRectangle2D.getWidth();
/* 555 */     float f4 = (float)localRectangle2D.getHeight();
/* 556 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.172945F, 0.34589F, 0.4931507F, 0.640411F, 0.7328767F, 0.8253425F, 0.9126712F, 1.0F }, new Color[] { this.color30, decodeColor(this.color30, this.color31, 0.5F), this.color31, decodeColor(this.color31, this.color32, 0.5F), this.color32, decodeColor(this.color32, this.color33, 0.5F), this.color33, decodeColor(this.color33, this.color34, 0.5F), this.color34 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient10(Shape paramShape)
/*     */   {
/* 570 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 571 */     float f1 = (float)localRectangle2D.getX();
/* 572 */     float f2 = (float)localRectangle2D.getY();
/* 573 */     float f3 = (float)localRectangle2D.getWidth();
/* 574 */     float f4 = (float)localRectangle2D.getHeight();
/* 575 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color35, decodeColor(this.color35, this.color36, 0.5F), this.color36 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.OptionPanePainter
 * JD-Core Version:    0.6.2
 */
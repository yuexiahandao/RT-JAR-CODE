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
/*     */ final class SpinnerNextButtonPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_FOCUSED = 3;
/*     */   static final int BACKGROUND_MOUSEOVER_FOCUSED = 4;
/*     */   static final int BACKGROUND_PRESSED_FOCUSED = 5;
/*     */   static final int BACKGROUND_MOUSEOVER = 6;
/*     */   static final int BACKGROUND_PRESSED = 7;
/*     */   static final int FOREGROUND_DISABLED = 8;
/*     */   static final int FOREGROUND_ENABLED = 9;
/*     */   static final int FOREGROUND_FOCUSED = 10;
/*     */   static final int FOREGROUND_MOUSEOVER_FOCUSED = 11;
/*     */   static final int FOREGROUND_PRESSED_FOCUSED = 12;
/*     */   static final int FOREGROUND_MOUSEOVER = 13;
/*     */   static final int FOREGROUND_PRESSED = 14;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  59 */   private Path2D path = new Path2D.Float();
/*  60 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  61 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  62 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  67 */   private Color color1 = decodeColor("nimbusBase", 0.0213483F, -0.5628988F, 0.2588235F, 0);
/*  68 */   private Color color2 = decodeColor("nimbusBase", 0.01023722F, -0.560714F, 0.235294F, 0);
/*  69 */   private Color color3 = decodeColor("nimbusBase", 0.0213483F, -0.592236F, 0.3529412F, 0);
/*  70 */   private Color color4 = decodeColor("nimbusBase", 0.01658648F, -0.5723659F, 0.317647F, 0);
/*  71 */   private Color color5 = decodeColor("nimbusBase", 0.0213483F, -0.5618227F, 0.2470588F, 0);
/*  72 */   private Color color6 = decodeColor("nimbusBase", 0.0005149841F, -0.3458592F, -0.007843137F, 0);
/*  73 */   private Color color7 = decodeColor("nimbusBase", 0.0005149841F, -0.2720779F, -0.1176471F, 0);
/*  74 */   private Color color8 = decodeColor("nimbusBase", 0.004681647F, -0.6197143F, 0.4313725F, 0);
/*  75 */   private Color color9 = decodeColor("nimbusBase", -0.001270711F, -0.5078604F, 0.309804F, 0);
/*  76 */   private Color color10 = decodeColor("nimbusBase", -0.002894104F, -0.480054F, 0.2823529F, 0);
/*  77 */   private Color color11 = decodeColor("nimbusBase", 0.002300739F, -0.3622768F, -0.04705882F, 0);
/*  78 */   private Color color12 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  79 */   private Color color13 = decodeColor("nimbusBase", 0.001348317F, -0.176999F, -0.1215687F, 0);
/*  80 */   private Color color14 = decodeColor("nimbusBase", 0.001348317F, 0.0399614F, -0.2588235F, 0);
/*  81 */   private Color color15 = decodeColor("nimbusBase", 0.004681647F, -0.6198413F, 0.4392157F, 0);
/*  82 */   private Color color16 = decodeColor("nimbusBase", -0.001270711F, -0.5150247F, 0.3607843F, 0);
/*  83 */   private Color color17 = decodeColor("nimbusBase", 0.002156436F, -0.4909775F, 0.345098F, 0);
/*  84 */   private Color color18 = decodeColor("nimbusBase", 5.203486E-005F, -0.3874384F, 0.01960784F, 0);
/*  85 */   private Color color19 = decodeColor("nimbusBase", -0.5786517F, -0.6357143F, -0.5490196F, 0);
/*  86 */   private Color color20 = decodeColor("nimbusBase", 0.088015F, 0.3642857F, -0.454902F, 0);
/*  87 */   private Color color21 = decodeColor("nimbusBase", -0.000420332F, -0.380506F, 0.2039216F, 0);
/*  88 */   private Color color22 = decodeColor("nimbusBase", 0.0002956986F, -0.1547016F, 0.07058823F, 0);
/*  89 */   private Color color23 = decodeColor("nimbusBase", -0.0004623532F, -0.09571427F, 0.03921568F, 0);
/*  90 */   private Color color24 = decodeColor("nimbusBase", 0.01836324F, 0.1813589F, -0.227451F, 0);
/*  91 */   private Color color25 = new Color(255, 200, 0, 255);
/*  92 */   private Color color26 = decodeColor("nimbusBase", 0.0213483F, -0.5810695F, 0.1686274F, 0);
/*  93 */   private Color color27 = decodeColor("nimbusBase", -0.5786517F, -0.6357143F, -0.04313725F, 0);
/*  94 */   private Color color28 = decodeColor("nimbusBase", -0.5786517F, -0.6357143F, -0.2431373F, 0);
/*  95 */   private Color color29 = decodeColor("nimbusBase", 0.0F, -0.6357143F, 0.4509804F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public SpinnerNextButtonPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/* 103 */     this.state = paramInt;
/* 104 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 110 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 113 */     switch (this.state) { case 1:
/* 114 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/* 115 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 3:
/* 116 */       paintBackgroundFocused(paramGraphics2D); break;
/*     */     case 4:
/* 117 */       paintBackgroundMouseOverAndFocused(paramGraphics2D); break;
/*     */     case 5:
/* 118 */       paintBackgroundPressedAndFocused(paramGraphics2D); break;
/*     */     case 6:
/* 119 */       paintBackgroundMouseOver(paramGraphics2D); break;
/*     */     case 7:
/* 120 */       paintBackgroundPressed(paramGraphics2D); break;
/*     */     case 8:
/* 121 */       paintForegroundDisabled(paramGraphics2D); break;
/*     */     case 9:
/* 122 */       paintForegroundEnabled(paramGraphics2D); break;
/*     */     case 10:
/* 123 */       paintForegroundFocused(paramGraphics2D); break;
/*     */     case 11:
/* 124 */       paintForegroundMouseOverAndFocused(paramGraphics2D); break;
/*     */     case 12:
/* 125 */       paintForegroundPressedAndFocused(paramGraphics2D); break;
/*     */     case 13:
/* 126 */       paintForegroundMouseOver(paramGraphics2D); break;
/*     */     case 14:
/* 127 */       paintForegroundPressed(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 136 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/* 140 */     this.path = decodePath1();
/* 141 */     paramGraphics2D.setPaint(decodeGradient1(this.path));
/* 142 */     paramGraphics2D.fill(this.path);
/* 143 */     this.path = decodePath2();
/* 144 */     paramGraphics2D.setPaint(decodeGradient2(this.path));
/* 145 */     paramGraphics2D.fill(this.path);
/* 146 */     this.rect = decodeRect1();
/* 147 */     paramGraphics2D.setPaint(this.color5);
/* 148 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 153 */     this.path = decodePath3();
/* 154 */     paramGraphics2D.setPaint(decodeGradient3(this.path));
/* 155 */     paramGraphics2D.fill(this.path);
/* 156 */     this.path = decodePath4();
/* 157 */     paramGraphics2D.setPaint(decodeGradient4(this.path));
/* 158 */     paramGraphics2D.fill(this.path);
/* 159 */     this.rect = decodeRect1();
/* 160 */     paramGraphics2D.setPaint(this.color11);
/* 161 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 166 */     this.path = decodePath5();
/* 167 */     paramGraphics2D.setPaint(this.color12);
/* 168 */     paramGraphics2D.fill(this.path);
/* 169 */     this.path = decodePath3();
/* 170 */     paramGraphics2D.setPaint(decodeGradient3(this.path));
/* 171 */     paramGraphics2D.fill(this.path);
/* 172 */     this.path = decodePath4();
/* 173 */     paramGraphics2D.setPaint(decodeGradient5(this.path));
/* 174 */     paramGraphics2D.fill(this.path);
/* 175 */     this.rect = decodeRect1();
/* 176 */     paramGraphics2D.setPaint(this.color11);
/* 177 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOverAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 182 */     this.path = decodePath5();
/* 183 */     paramGraphics2D.setPaint(this.color12);
/* 184 */     paramGraphics2D.fill(this.path);
/* 185 */     this.path = decodePath3();
/* 186 */     paramGraphics2D.setPaint(decodeGradient6(this.path));
/* 187 */     paramGraphics2D.fill(this.path);
/* 188 */     this.path = decodePath4();
/* 189 */     paramGraphics2D.setPaint(decodeGradient7(this.path));
/* 190 */     paramGraphics2D.fill(this.path);
/* 191 */     this.rect = decodeRect1();
/* 192 */     paramGraphics2D.setPaint(this.color18);
/* 193 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 198 */     this.path = decodePath5();
/* 199 */     paramGraphics2D.setPaint(this.color12);
/* 200 */     paramGraphics2D.fill(this.path);
/* 201 */     this.path = decodePath6();
/* 202 */     paramGraphics2D.setPaint(decodeGradient8(this.path));
/* 203 */     paramGraphics2D.fill(this.path);
/* 204 */     this.path = decodePath4();
/* 205 */     paramGraphics2D.setPaint(decodeGradient9(this.path));
/* 206 */     paramGraphics2D.fill(this.path);
/* 207 */     this.rect = decodeRect1();
/* 208 */     paramGraphics2D.setPaint(this.color24);
/* 209 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 214 */     this.path = decodePath3();
/* 215 */     paramGraphics2D.setPaint(decodeGradient6(this.path));
/* 216 */     paramGraphics2D.fill(this.path);
/* 217 */     this.path = decodePath4();
/* 218 */     paramGraphics2D.setPaint(decodeGradient10(this.path));
/* 219 */     paramGraphics2D.fill(this.path);
/* 220 */     this.rect = decodeRect1();
/* 221 */     paramGraphics2D.setPaint(this.color18);
/* 222 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 227 */     this.path = decodePath6();
/* 228 */     paramGraphics2D.setPaint(decodeGradient8(this.path));
/* 229 */     paramGraphics2D.fill(this.path);
/* 230 */     this.path = decodePath4();
/* 231 */     paramGraphics2D.setPaint(decodeGradient11(this.path));
/* 232 */     paramGraphics2D.fill(this.path);
/* 233 */     this.rect = decodeRect1();
/* 234 */     paramGraphics2D.setPaint(this.color24);
/* 235 */     paramGraphics2D.fill(this.rect);
/* 236 */     this.rect = decodeRect2();
/* 237 */     paramGraphics2D.setPaint(this.color25);
/* 238 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintForegroundDisabled(Graphics2D paramGraphics2D)
/*     */   {
/* 243 */     this.path = decodePath7();
/* 244 */     paramGraphics2D.setPaint(this.color26);
/* 245 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 250 */     this.path = decodePath7();
/* 251 */     paramGraphics2D.setPaint(decodeGradient12(this.path));
/* 252 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 257 */     this.path = decodePath8();
/* 258 */     paramGraphics2D.setPaint(decodeGradient12(this.path));
/* 259 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundMouseOverAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 264 */     this.path = decodePath8();
/* 265 */     paramGraphics2D.setPaint(decodeGradient12(this.path));
/* 266 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundPressedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 271 */     this.path = decodePath9();
/* 272 */     paramGraphics2D.setPaint(this.color29);
/* 273 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 278 */     this.path = decodePath7();
/* 279 */     paramGraphics2D.setPaint(decodeGradient12(this.path));
/* 280 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 285 */     this.path = decodePath9();
/* 286 */     paramGraphics2D.setPaint(this.color29);
/* 287 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 294 */     this.path.reset();
/* 295 */     this.path.moveTo(decodeX(0.0F), decodeY(3.0F));
/* 296 */     this.path.lineTo(decodeX(0.0F), decodeY(0.285714F));
/* 297 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.285714F, 0.0F), decodeAnchorX(2.0F, -3.636364F), decodeAnchorY(0.285714F, 0.0F), decodeX(2.0F), decodeY(0.285714F));
/* 298 */     this.path.curveTo(decodeAnchorX(2.0F, 3.636364F), decodeAnchorY(0.285714F, 0.0F), decodeAnchorX(2.714286F, -0.02272727F), decodeAnchorY(1.0F, -3.75F), decodeX(2.714286F), decodeY(1.0F));
/* 299 */     this.path.curveTo(decodeAnchorX(2.714286F, 0.02272727F), decodeAnchorY(1.0F, 3.75F), decodeAnchorX(2.714286F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeX(2.714286F), decodeY(3.0F));
/* 300 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 301 */     this.path.closePath();
/* 302 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 306 */     this.path.reset();
/* 307 */     this.path.moveTo(decodeX(1.0F), decodeY(2.0F));
/* 308 */     this.path.lineTo(decodeX(1.0F), decodeY(0.4285714F));
/* 309 */     this.path.curveTo(decodeAnchorX(1.0F, 0.0F), decodeAnchorY(0.4285714F, 0.0F), decodeAnchorX(2.0F, -3.0F), decodeAnchorY(0.4285714F, 0.0F), decodeX(2.0F), decodeY(0.4285714F));
/* 310 */     this.path.curveTo(decodeAnchorX(2.0F, 3.0F), decodeAnchorY(0.4285714F, 0.0F), decodeAnchorX(2.571429F, 0.0F), decodeAnchorY(1.0F, -2.0F), decodeX(2.571429F), decodeY(1.0F));
/* 311 */     this.path.curveTo(decodeAnchorX(2.571429F, 0.0F), decodeAnchorY(1.0F, 2.0F), decodeAnchorX(2.571429F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(2.571429F), decodeY(2.0F));
/* 312 */     this.path.lineTo(decodeX(1.0F), decodeY(2.0F));
/* 313 */     this.path.closePath();
/* 314 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1() {
/* 318 */     this.rect.setRect(decodeX(1.0F), decodeY(2.0F), decodeX(2.571429F) - decodeX(1.0F), decodeY(3.0F) - decodeY(2.0F));
/*     */ 
/* 322 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 326 */     this.path.reset();
/* 327 */     this.path.moveTo(decodeX(0.0F), decodeY(3.0F));
/* 328 */     this.path.lineTo(decodeX(0.0F), decodeY(0.285714F));
/* 329 */     this.path.lineTo(decodeX(2.0F), decodeY(0.285714F));
/* 330 */     this.path.curveTo(decodeAnchorX(2.0F, 3.636364F), decodeAnchorY(0.285714F, 0.0F), decodeAnchorX(2.714286F, -0.02272727F), decodeAnchorY(1.0F, -3.75F), decodeX(2.714286F), decodeY(1.0F));
/* 331 */     this.path.lineTo(decodeX(2.714286F), decodeY(3.0F));
/* 332 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 333 */     this.path.closePath();
/* 334 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 338 */     this.path.reset();
/* 339 */     this.path.moveTo(decodeX(1.0F), decodeY(2.0F));
/* 340 */     this.path.lineTo(decodeX(1.0F), decodeY(0.4285714F));
/* 341 */     this.path.lineTo(decodeX(2.0F), decodeY(0.4285714F));
/* 342 */     this.path.curveTo(decodeAnchorX(2.0F, 3.0F), decodeAnchorY(0.4285714F, 0.0F), decodeAnchorX(2.571429F, 0.0F), decodeAnchorY(1.0F, -2.0F), decodeX(2.571429F), decodeY(1.0F));
/* 343 */     this.path.lineTo(decodeX(2.571429F), decodeY(2.0F));
/* 344 */     this.path.lineTo(decodeX(1.0F), decodeY(2.0F));
/* 345 */     this.path.closePath();
/* 346 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath5() {
/* 350 */     this.path.reset();
/* 351 */     this.path.moveTo(decodeX(0.0F), decodeY(3.0F));
/* 352 */     this.path.lineTo(decodeX(0.0F), decodeY(0.08571429F));
/* 353 */     this.path.lineTo(decodeX(2.142857F), decodeY(0.08571429F));
/* 354 */     this.path.curveTo(decodeAnchorX(2.142857F, 3.4F), decodeAnchorY(0.08571429F, 0.0F), decodeAnchorX(2.914286F, 0.0F), decodeAnchorY(1.0F, -3.4F), decodeX(2.914286F), decodeY(1.0F));
/* 355 */     this.path.lineTo(decodeX(2.914286F), decodeY(3.0F));
/* 356 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 357 */     this.path.closePath();
/* 358 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath6() {
/* 362 */     this.path.reset();
/* 363 */     this.path.moveTo(decodeX(0.0F), decodeY(3.0F));
/* 364 */     this.path.lineTo(decodeX(0.0F), decodeY(0.285714F));
/* 365 */     this.path.lineTo(decodeX(2.0F), decodeY(0.285714F));
/* 366 */     this.path.curveTo(decodeAnchorX(2.0F, 3.454546F), decodeAnchorY(0.285714F, 0.0F), decodeAnchorX(2.714286F, -0.02272727F), decodeAnchorY(1.0F, -3.477273F), decodeX(2.714286F), decodeY(1.0F));
/* 367 */     this.path.lineTo(decodeX(2.714286F), decodeY(3.0F));
/* 368 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 369 */     this.path.closePath();
/* 370 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 374 */     this.rect.setRect(decodeX(0.0F), decodeY(0.0F), decodeX(0.0F) - decodeX(0.0F), decodeY(0.0F) - decodeY(0.0F));
/*     */ 
/* 378 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath7() {
/* 382 */     this.path.reset();
/* 383 */     this.path.moveTo(decodeX(1.0F), decodeY(2.0F));
/* 384 */     this.path.lineTo(decodeX(1.490909F), decodeY(1.028409F));
/* 385 */     this.path.lineTo(decodeX(2.0F), decodeY(2.0F));
/* 386 */     this.path.lineTo(decodeX(1.0F), decodeY(2.0F));
/* 387 */     this.path.closePath();
/* 388 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath8() {
/* 392 */     this.path.reset();
/* 393 */     this.path.moveTo(decodeX(1.0F), decodeY(2.0F));
/* 394 */     this.path.lineTo(decodeX(1.490909F), decodeY(1.352273F));
/* 395 */     this.path.lineTo(decodeX(2.0F), decodeY(2.0F));
/* 396 */     this.path.lineTo(decodeX(1.0F), decodeY(2.0F));
/* 397 */     this.path.closePath();
/* 398 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath9() {
/* 402 */     this.path.reset();
/* 403 */     this.path.moveTo(decodeX(1.0F), decodeY(2.0F));
/* 404 */     this.path.lineTo(decodeX(1.504546F), decodeY(1.079546F));
/* 405 */     this.path.lineTo(decodeX(2.0F), decodeY(2.0F));
/* 406 */     this.path.lineTo(decodeX(1.0F), decodeY(2.0F));
/* 407 */     this.path.closePath();
/* 408 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 414 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 415 */     float f1 = (float)localRectangle2D.getX();
/* 416 */     float f2 = (float)localRectangle2D.getY();
/* 417 */     float f3 = (float)localRectangle2D.getWidth();
/* 418 */     float f4 = (float)localRectangle2D.getHeight();
/* 419 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 427 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 428 */     float f1 = (float)localRectangle2D.getX();
/* 429 */     float f2 = (float)localRectangle2D.getY();
/* 430 */     float f3 = (float)localRectangle2D.getWidth();
/* 431 */     float f4 = (float)localRectangle2D.getHeight();
/* 432 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 440 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 441 */     float f1 = (float)localRectangle2D.getX();
/* 442 */     float f2 = (float)localRectangle2D.getY();
/* 443 */     float f3 = (float)localRectangle2D.getWidth();
/* 444 */     float f4 = (float)localRectangle2D.getHeight();
/* 445 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 453 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 454 */     float f1 = (float)localRectangle2D.getX();
/* 455 */     float f2 = (float)localRectangle2D.getY();
/* 456 */     float f3 = (float)localRectangle2D.getWidth();
/* 457 */     float f4 = (float)localRectangle2D.getHeight();
/* 458 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.3649733F, 0.7299466F, 0.8649733F, 1.0F }, new Color[] { this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 468 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 469 */     float f1 = (float)localRectangle2D.getX();
/* 470 */     float f2 = (float)localRectangle2D.getY();
/* 471 */     float f3 = (float)localRectangle2D.getWidth();
/* 472 */     float f4 = (float)localRectangle2D.getHeight();
/* 473 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.3756684F, 0.7513369F, 0.8756684F, 1.0F }, new Color[] { this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 483 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 484 */     float f1 = (float)localRectangle2D.getX();
/* 485 */     float f2 = (float)localRectangle2D.getY();
/* 486 */     float f3 = (float)localRectangle2D.getWidth();
/* 487 */     float f4 = (float)localRectangle2D.getHeight();
/* 488 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 496 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 497 */     float f1 = (float)localRectangle2D.getX();
/* 498 */     float f2 = (float)localRectangle2D.getY();
/* 499 */     float f3 = (float)localRectangle2D.getWidth();
/* 500 */     float f4 = (float)localRectangle2D.getHeight();
/* 501 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.3796791F, 0.7593583F, 0.8796791F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 511 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 512 */     float f1 = (float)localRectangle2D.getX();
/* 513 */     float f2 = (float)localRectangle2D.getY();
/* 514 */     float f3 = (float)localRectangle2D.getWidth();
/* 515 */     float f4 = (float)localRectangle2D.getHeight();
/* 516 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color19, decodeColor(this.color19, this.color20, 0.5F), this.color20 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient9(Shape paramShape)
/*     */   {
/* 524 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 525 */     float f1 = (float)localRectangle2D.getX();
/* 526 */     float f2 = (float)localRectangle2D.getY();
/* 527 */     float f3 = (float)localRectangle2D.getWidth();
/* 528 */     float f4 = (float)localRectangle2D.getHeight();
/* 529 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.3716578F, 0.7433155F, 0.8716577F, 1.0F }, new Color[] { this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22, decodeColor(this.color22, this.color23, 0.5F), this.color23 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient10(Shape paramShape)
/*     */   {
/* 539 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 540 */     float f1 = (float)localRectangle2D.getX();
/* 541 */     float f2 = (float)localRectangle2D.getY();
/* 542 */     float f3 = (float)localRectangle2D.getWidth();
/* 543 */     float f4 = (float)localRectangle2D.getHeight();
/* 544 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.3970588F, 0.7941176F, 0.8970588F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient11(Shape paramShape)
/*     */   {
/* 554 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 555 */     float f1 = (float)localRectangle2D.getX();
/* 556 */     float f2 = (float)localRectangle2D.getY();
/* 557 */     float f3 = (float)localRectangle2D.getWidth();
/* 558 */     float f4 = (float)localRectangle2D.getHeight();
/* 559 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.4318182F, 0.8636364F, 0.9318182F, 1.0F }, new Color[] { this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22, decodeColor(this.color22, this.color23, 0.5F), this.color23 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient12(Shape paramShape)
/*     */   {
/* 569 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 570 */     float f1 = (float)localRectangle2D.getX();
/* 571 */     float f2 = (float)localRectangle2D.getY();
/* 572 */     float f3 = (float)localRectangle2D.getWidth();
/* 573 */     float f4 = (float)localRectangle2D.getHeight();
/* 574 */     return decodeGradient(0.4863637F * f3 + f1, 0.01169591F * f4 + f2, 0.490909F * f3 + f1, 0.888889F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color27, decodeColor(this.color27, this.color28, 0.5F), this.color28 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.SpinnerNextButtonPainter
 * JD-Core Version:    0.6.2
 */
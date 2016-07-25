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
/*     */ final class TabbedPaneTabPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int BACKGROUND_ENABLED_MOUSEOVER = 2;
/*     */   static final int BACKGROUND_ENABLED_PRESSED = 3;
/*     */   static final int BACKGROUND_DISABLED = 4;
/*     */   static final int BACKGROUND_SELECTED_DISABLED = 5;
/*     */   static final int BACKGROUND_SELECTED = 6;
/*     */   static final int BACKGROUND_SELECTED_MOUSEOVER = 7;
/*     */   static final int BACKGROUND_SELECTED_PRESSED = 8;
/*     */   static final int BACKGROUND_SELECTED_FOCUSED = 9;
/*     */   static final int BACKGROUND_SELECTED_MOUSEOVER_FOCUSED = 10;
/*     */   static final int BACKGROUND_SELECTED_PRESSED_FOCUSED = 11;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  56 */   private Path2D path = new Path2D.Float();
/*  57 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  58 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  59 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  64 */   private Color color1 = decodeColor("nimbusBase", 0.03245944F, -0.5553572F, -0.1098039F, 0);
/*  65 */   private Color color2 = decodeColor("nimbusBase", 0.088015F, 0.3642857F, -0.478431F, 0);
/*  66 */   private Color color3 = decodeColor("nimbusBase", 0.088015F, -0.6317461F, 0.4392157F, 0);
/*  67 */   private Color color4 = decodeColor("nimbusBase", 0.05468172F, -0.6145278F, 0.3764706F, 0);
/*  68 */   private Color color5 = decodeColor("nimbusBase", 0.03245944F, -0.5953556F, 0.3254902F, 0);
/*  69 */   private Color color6 = decodeColor("nimbusBase", 0.03245944F, -0.5461621F, -0.0235294F, 0);
/*  70 */   private Color color7 = decodeColor("nimbusBase", 0.088015F, -0.6317773F, 0.447059F, 0);
/*  71 */   private Color color8 = decodeColor("nimbusBase", 0.0213483F, -0.6154714F, 0.4196078F, 0);
/*  72 */   private Color color9 = decodeColor("nimbusBase", 0.03245944F, -0.5985242F, 0.4F, 0);
/*  73 */   private Color color10 = decodeColor("nimbusBase", 0.088015F, 0.3642857F, -0.5215687F, 0);
/*  74 */   private Color color11 = decodeColor("nimbusBase", 0.0274089F, -0.5847884F, 0.298039F, 0);
/*  75 */   private Color color12 = decodeColor("nimbusBase", 0.03593165F, -0.5553123F, 0.2313725F, 0);
/*  76 */   private Color color13 = decodeColor("nimbusBase", 0.02968168F, -0.5281874F, 0.1803922F, 0);
/*  77 */   private Color color14 = decodeColor("nimbusBase", 0.03801495F, -0.545624F, 0.3215686F, 0);
/*  78 */   private Color color15 = decodeColor("nimbusBase", 0.03245944F, -0.5918118F, 0.254902F, 0);
/*  79 */   private Color color16 = decodeColor("nimbusBase", 0.05468172F, -0.5830827F, 0.1960784F, 0);
/*  80 */   private Color color17 = decodeColor("nimbusBase", 0.04634833F, -0.6006266F, 0.345098F, 0);
/*  81 */   private Color color18 = decodeColor("nimbusBase", 0.04634833F, -0.6001588F, 0.3333333F, 0);
/*  82 */   private Color color19 = decodeColor("nimbusBase", 0.004681647F, -0.6197143F, 0.4313725F, 0);
/*  83 */   private Color color20 = decodeColor("nimbusBase", 0.000713408F, -0.543609F, 0.345098F, 0);
/*  84 */   private Color color21 = decodeColor("nimbusBase", -0.002075136F, -0.4561026F, 0.2588235F, 0);
/*  85 */   private Color color22 = decodeColor("nimbusBase", 0.0005149841F, -0.43867F, 0.2470588F, 0);
/*  86 */   private Color color23 = decodeColor("nimbusBase", 0.0005149841F, -0.4487984F, 0.2901961F, 0);
/*  87 */   private Color color24 = decodeColor("nimbusBase", 0.0005149841F, -0.08776909F, -0.2627451F, 0);
/*  88 */   private Color color25 = decodeColor("nimbusBase", 0.06332368F, 0.3642857F, -0.4431373F, 0);
/*  89 */   private Color color26 = decodeColor("nimbusBase", 0.004681647F, -0.6198413F, 0.4392157F, 0);
/*  90 */   private Color color27 = decodeColor("nimbusBase", -0.002262771F, -0.5335866F, 0.372549F, 0);
/*  91 */   private Color color28 = decodeColor("nimbusBase", -0.001728594F, -0.460826F, 0.3254902F, 0);
/*  92 */   private Color color29 = decodeColor("nimbusBase", 0.0005149841F, -0.455534F, 0.3215686F, 0);
/*  93 */   private Color color30 = decodeColor("nimbusBase", 0.0005149841F, -0.4640405F, 0.3647059F, 0);
/*  94 */   private Color color31 = decodeColor("nimbusBase", -0.5786517F, -0.6357143F, -0.5490196F, 0);
/*  95 */   private Color color32 = decodeColor("nimbusBase", -0.000420332F, -0.380506F, 0.2039216F, 0);
/*  96 */   private Color color33 = decodeColor("nimbusBase", 0.001348317F, -0.1640162F, 0.0745098F, 0);
/*  97 */   private Color color34 = decodeColor("nimbusBase", -0.001000166F, -0.01599598F, 0.007843137F, 0);
/*  98 */   private Color color35 = decodeColor("nimbusBase", 0.0F, 0.0F, 0.0F, 0);
/*  99 */   private Color color36 = decodeColor("nimbusBase", 0.001872718F, -0.03839886F, 0.03529412F, 0);
/* 100 */   private Color color37 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public TabbedPaneTabPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/* 108 */     this.state = paramInt;
/* 109 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 115 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 118 */     switch (this.state) { case 1:
/* 119 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 2:
/* 120 */       paintBackgroundEnabledAndMouseOver(paramGraphics2D); break;
/*     */     case 3:
/* 121 */       paintBackgroundEnabledAndPressed(paramGraphics2D); break;
/*     */     case 4:
/* 122 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 5:
/* 123 */       paintBackgroundSelectedAndDisabled(paramGraphics2D); break;
/*     */     case 6:
/* 124 */       paintBackgroundSelected(paramGraphics2D); break;
/*     */     case 7:
/* 125 */       paintBackgroundSelectedAndMouseOver(paramGraphics2D); break;
/*     */     case 8:
/* 126 */       paintBackgroundSelectedAndPressed(paramGraphics2D); break;
/*     */     case 9:
/* 127 */       paintBackgroundSelectedAndFocused(paramGraphics2D); break;
/*     */     case 10:
/* 128 */       paintBackgroundSelectedAndMouseOverAndFocused(paramGraphics2D); break;
/*     */     case 11:
/* 129 */       paintBackgroundSelectedAndPressedAndFocused(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 138 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D) {
/* 142 */     this.path = decodePath1();
/* 143 */     paramGraphics2D.setPaint(decodeGradient1(this.path));
/* 144 */     paramGraphics2D.fill(this.path);
/* 145 */     this.path = decodePath2();
/* 146 */     paramGraphics2D.setPaint(decodeGradient2(this.path));
/* 147 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 152 */     this.path = decodePath1();
/* 153 */     paramGraphics2D.setPaint(decodeGradient3(this.path));
/* 154 */     paramGraphics2D.fill(this.path);
/* 155 */     this.path = decodePath2();
/* 156 */     paramGraphics2D.setPaint(decodeGradient4(this.path));
/* 157 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 162 */     this.path = decodePath3();
/* 163 */     paramGraphics2D.setPaint(decodeGradient5(this.path));
/* 164 */     paramGraphics2D.fill(this.path);
/* 165 */     this.path = decodePath4();
/* 166 */     paramGraphics2D.setPaint(decodeGradient6(this.path));
/* 167 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D)
/*     */   {
/* 172 */     this.path = decodePath5();
/* 173 */     paramGraphics2D.setPaint(decodeGradient7(this.path));
/* 174 */     paramGraphics2D.fill(this.path);
/* 175 */     this.path = decodePath6();
/* 176 */     paramGraphics2D.setPaint(decodeGradient8(this.path));
/* 177 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndDisabled(Graphics2D paramGraphics2D)
/*     */   {
/* 182 */     this.path = decodePath7();
/* 183 */     paramGraphics2D.setPaint(decodeGradient7(this.path));
/* 184 */     paramGraphics2D.fill(this.path);
/* 185 */     this.path = decodePath2();
/* 186 */     paramGraphics2D.setPaint(decodeGradient9(this.path));
/* 187 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 192 */     this.path = decodePath7();
/* 193 */     paramGraphics2D.setPaint(decodeGradient10(this.path));
/* 194 */     paramGraphics2D.fill(this.path);
/* 195 */     this.path = decodePath2();
/* 196 */     paramGraphics2D.setPaint(decodeGradient9(this.path));
/* 197 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 202 */     this.path = decodePath8();
/* 203 */     paramGraphics2D.setPaint(decodeGradient11(this.path));
/* 204 */     paramGraphics2D.fill(this.path);
/* 205 */     this.path = decodePath9();
/* 206 */     paramGraphics2D.setPaint(decodeGradient12(this.path));
/* 207 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 212 */     this.path = decodePath8();
/* 213 */     paramGraphics2D.setPaint(decodeGradient13(this.path));
/* 214 */     paramGraphics2D.fill(this.path);
/* 215 */     this.path = decodePath9();
/* 216 */     paramGraphics2D.setPaint(decodeGradient14(this.path));
/* 217 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 222 */     this.path = decodePath1();
/* 223 */     paramGraphics2D.setPaint(decodeGradient10(this.path));
/* 224 */     paramGraphics2D.fill(this.path);
/* 225 */     this.path = decodePath10();
/* 226 */     paramGraphics2D.setPaint(decodeGradient9(this.path));
/* 227 */     paramGraphics2D.fill(this.path);
/* 228 */     this.path = decodePath11();
/* 229 */     paramGraphics2D.setPaint(this.color37);
/* 230 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndMouseOverAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 235 */     this.path = decodePath12();
/* 236 */     paramGraphics2D.setPaint(decodeGradient11(this.path));
/* 237 */     paramGraphics2D.fill(this.path);
/* 238 */     this.path = decodePath13();
/* 239 */     paramGraphics2D.setPaint(decodeGradient12(this.path));
/* 240 */     paramGraphics2D.fill(this.path);
/* 241 */     this.path = decodePath14();
/* 242 */     paramGraphics2D.setPaint(this.color37);
/* 243 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndPressedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 248 */     this.path = decodePath12();
/* 249 */     paramGraphics2D.setPaint(decodeGradient13(this.path));
/* 250 */     paramGraphics2D.fill(this.path);
/* 251 */     this.path = decodePath13();
/* 252 */     paramGraphics2D.setPaint(decodeGradient14(this.path));
/* 253 */     paramGraphics2D.fill(this.path);
/* 254 */     this.path = decodePath14();
/* 255 */     paramGraphics2D.setPaint(this.color37);
/* 256 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 263 */     this.path.reset();
/* 264 */     this.path.moveTo(decodeX(0.0F), decodeY(0.7142857F));
/* 265 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.7142857F, -3.0F), decodeAnchorX(0.7142857F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(0.7142857F), decodeY(0.0F));
/* 266 */     this.path.curveTo(decodeAnchorX(0.7142857F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(2.285714F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(2.285714F), decodeY(0.0F));
/* 267 */     this.path.curveTo(decodeAnchorX(2.285714F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.7142857F, -3.0F), decodeX(3.0F), decodeY(0.7142857F));
/* 268 */     this.path.curveTo(decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.7142857F, 3.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeX(3.0F), decodeY(3.0F));
/* 269 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 270 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.7142857F, 3.0F), decodeX(0.0F), decodeY(0.7142857F));
/* 271 */     this.path.closePath();
/* 272 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 276 */     this.path.reset();
/* 277 */     this.path.moveTo(decodeX(0.1428572F), decodeY(2.0F));
/* 278 */     this.path.curveTo(decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(0.8571429F, 3.555556F), decodeX(0.1428572F), decodeY(0.8571429F));
/* 279 */     this.path.curveTo(decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(0.8571429F, -3.555556F), decodeAnchorX(0.8571429F, -3.444444F), decodeAnchorY(0.1428572F, 0.0F), decodeX(0.8571429F), decodeY(0.1428572F));
/* 280 */     this.path.curveTo(decodeAnchorX(0.8571429F, 3.444444F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.142857F, -3.333333F), decodeAnchorY(0.1428572F, 0.0F), decodeX(2.142857F), decodeY(0.1428572F));
/* 281 */     this.path.curveTo(decodeAnchorX(2.142857F, 3.333333F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(0.8571429F, -3.277778F), decodeX(2.857143F), decodeY(0.8571429F));
/* 282 */     this.path.curveTo(decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(0.8571429F, 3.277778F), decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(2.857143F), decodeY(2.0F));
/* 283 */     this.path.lineTo(decodeX(0.1428572F), decodeY(2.0F));
/* 284 */     this.path.closePath();
/* 285 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 289 */     this.path.reset();
/* 290 */     this.path.moveTo(decodeX(0.0F), decodeY(0.7142857F));
/* 291 */     this.path.curveTo(decodeAnchorX(0.0F, 0.05555556F), decodeAnchorY(0.7142857F, 2.611111F), decodeAnchorX(0.8333333F, -2.5F), decodeAnchorY(0.0F, 0.0F), decodeX(0.8333333F), decodeY(0.0F));
/* 292 */     this.path.curveTo(decodeAnchorX(0.8333333F, 2.5F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(2.285714F, -2.722222F), decodeAnchorY(0.0F, 0.0F), decodeX(2.285714F), decodeY(0.0F));
/* 293 */     this.path.curveTo(decodeAnchorX(2.285714F, 2.722222F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(3.0F, -0.05555556F), decodeAnchorY(0.7142857F, -2.722222F), decodeX(3.0F), decodeY(0.7142857F));
/* 294 */     this.path.curveTo(decodeAnchorX(3.0F, 0.05555556F), decodeAnchorY(0.7142857F, 2.722222F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeX(3.0F), decodeY(3.0F));
/* 295 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 296 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeAnchorX(0.0F, -0.05555556F), decodeAnchorY(0.7142857F, -2.611111F), decodeX(0.0F), decodeY(0.7142857F));
/* 297 */     this.path.closePath();
/* 298 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 302 */     this.path.reset();
/* 303 */     this.path.moveTo(decodeX(0.1666667F), decodeY(2.0F));
/* 304 */     this.path.curveTo(decodeAnchorX(0.1666667F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(0.1666667F, 0.0F), decodeAnchorY(0.8571429F, 3.666667F), decodeX(0.1666667F), decodeY(0.8571429F));
/* 305 */     this.path.curveTo(decodeAnchorX(0.1666667F, 0.0F), decodeAnchorY(0.8571429F, -3.666667F), decodeAnchorX(1.0F, -3.555556F), decodeAnchorY(0.1428572F, 0.0F), decodeX(1.0F), decodeY(0.1428572F));
/* 306 */     this.path.curveTo(decodeAnchorX(1.0F, 3.555556F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.142857F, -3.5F), decodeAnchorY(0.1428572F, 0.05555556F), decodeX(2.142857F), decodeY(0.1428572F));
/* 307 */     this.path.curveTo(decodeAnchorX(2.142857F, 3.5F), decodeAnchorY(0.1428572F, -0.05555556F), decodeAnchorX(2.857143F, 0.05555556F), decodeAnchorY(0.8571429F, -3.666667F), decodeX(2.857143F), decodeY(0.8571429F));
/* 308 */     this.path.curveTo(decodeAnchorX(2.857143F, -0.05555556F), decodeAnchorY(0.8571429F, 3.666667F), decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(2.857143F), decodeY(2.0F));
/* 309 */     this.path.lineTo(decodeX(0.1666667F), decodeY(2.0F));
/* 310 */     this.path.closePath();
/* 311 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath5() {
/* 315 */     this.path.reset();
/* 316 */     this.path.moveTo(decodeX(0.0F), decodeY(0.8333333F));
/* 317 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.8333333F, -3.0F), decodeAnchorX(0.7142857F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(0.7142857F), decodeY(0.0F));
/* 318 */     this.path.curveTo(decodeAnchorX(0.7142857F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(2.285714F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(2.285714F), decodeY(0.0F));
/* 319 */     this.path.curveTo(decodeAnchorX(2.285714F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.8333333F, -3.0F), decodeX(3.0F), decodeY(0.8333333F));
/* 320 */     this.path.curveTo(decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.8333333F, 3.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeX(3.0F), decodeY(3.0F));
/* 321 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 322 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.8333333F, 3.0F), decodeX(0.0F), decodeY(0.8333333F));
/* 323 */     this.path.closePath();
/* 324 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath6() {
/* 328 */     this.path.reset();
/* 329 */     this.path.moveTo(decodeX(0.1428572F), decodeY(2.0F));
/* 330 */     this.path.curveTo(decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(1.0F, 3.555556F), decodeX(0.1428572F), decodeY(1.0F));
/* 331 */     this.path.curveTo(decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(1.0F, -3.555556F), decodeAnchorX(0.8571429F, -3.444444F), decodeAnchorY(0.1666667F, 0.0F), decodeX(0.8571429F), decodeY(0.1666667F));
/* 332 */     this.path.curveTo(decodeAnchorX(0.8571429F, 3.444444F), decodeAnchorY(0.1666667F, 0.0F), decodeAnchorX(2.142857F, -3.333333F), decodeAnchorY(0.1666667F, 0.0F), decodeX(2.142857F), decodeY(0.1666667F));
/* 333 */     this.path.curveTo(decodeAnchorX(2.142857F, 3.333333F), decodeAnchorY(0.1666667F, 0.0F), decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(1.0F, -3.277778F), decodeX(2.857143F), decodeY(1.0F));
/* 334 */     this.path.curveTo(decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(1.0F, 3.277778F), decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(2.857143F), decodeY(2.0F));
/* 335 */     this.path.lineTo(decodeX(0.1428572F), decodeY(2.0F));
/* 336 */     this.path.closePath();
/* 337 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath7() {
/* 341 */     this.path.reset();
/* 342 */     this.path.moveTo(decodeX(0.0F), decodeY(0.7142857F));
/* 343 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.7142857F, -3.0F), decodeAnchorX(0.7142857F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(0.7142857F), decodeY(0.0F));
/* 344 */     this.path.curveTo(decodeAnchorX(0.7142857F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(2.285714F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(2.285714F), decodeY(0.0F));
/* 345 */     this.path.curveTo(decodeAnchorX(2.285714F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.7142857F, -3.0F), decodeX(3.0F), decodeY(0.7142857F));
/* 346 */     this.path.curveTo(decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.7142857F, 3.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(3.0F), decodeY(2.0F));
/* 347 */     this.path.lineTo(decodeX(0.0F), decodeY(2.0F));
/* 348 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.7142857F, 3.0F), decodeX(0.0F), decodeY(0.7142857F));
/* 349 */     this.path.closePath();
/* 350 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath8() {
/* 354 */     this.path.reset();
/* 355 */     this.path.moveTo(decodeX(0.0F), decodeY(0.7142857F));
/* 356 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.7142857F, -3.0F), decodeAnchorX(0.5555556F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(0.5555556F), decodeY(0.0F));
/* 357 */     this.path.curveTo(decodeAnchorX(0.5555556F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(2.444444F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(2.444444F), decodeY(0.0F));
/* 358 */     this.path.curveTo(decodeAnchorX(2.444444F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.7142857F, -3.0F), decodeX(3.0F), decodeY(0.7142857F));
/* 359 */     this.path.curveTo(decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.7142857F, 3.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(3.0F), decodeY(2.0F));
/* 360 */     this.path.lineTo(decodeX(0.0F), decodeY(2.0F));
/* 361 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.7142857F, 3.0F), decodeX(0.0F), decodeY(0.7142857F));
/* 362 */     this.path.closePath();
/* 363 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath9() {
/* 367 */     this.path.reset();
/* 368 */     this.path.moveTo(decodeX(0.1111111F), decodeY(2.0F));
/* 369 */     this.path.curveTo(decodeAnchorX(0.1111111F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeAnchorX(0.1111111F, 0.0F), decodeAnchorY(0.8571429F, 3.555556F), decodeX(0.1111111F), decodeY(0.8571429F));
/* 370 */     this.path.curveTo(decodeAnchorX(0.1111111F, 0.0F), decodeAnchorY(0.8571429F, -3.555556F), decodeAnchorX(0.6666667F, -3.444444F), decodeAnchorY(0.1428572F, 0.0F), decodeX(0.6666667F), decodeY(0.1428572F));
/* 371 */     this.path.curveTo(decodeAnchorX(0.6666667F, 3.444444F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.333333F, -3.333333F), decodeAnchorY(0.1428572F, 0.0F), decodeX(2.333333F), decodeY(0.1428572F));
/* 372 */     this.path.curveTo(decodeAnchorX(2.333333F, 3.333333F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.888889F, 0.0F), decodeAnchorY(0.8571429F, -3.277778F), decodeX(2.888889F), decodeY(0.8571429F));
/* 373 */     this.path.curveTo(decodeAnchorX(2.888889F, 0.0F), decodeAnchorY(0.8571429F, 3.277778F), decodeAnchorX(2.888889F, 0.0F), decodeAnchorY(2.0F, 0.0F), decodeX(2.888889F), decodeY(2.0F));
/* 374 */     this.path.lineTo(decodeX(0.1111111F), decodeY(2.0F));
/* 375 */     this.path.closePath();
/* 376 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath10() {
/* 380 */     this.path.reset();
/* 381 */     this.path.moveTo(decodeX(0.1428572F), decodeY(3.0F));
/* 382 */     this.path.curveTo(decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(0.8571429F, 3.555556F), decodeX(0.1428572F), decodeY(0.8571429F));
/* 383 */     this.path.curveTo(decodeAnchorX(0.1428572F, 0.0F), decodeAnchorY(0.8571429F, -3.555556F), decodeAnchorX(0.8571429F, -3.444444F), decodeAnchorY(0.1428572F, 0.0F), decodeX(0.8571429F), decodeY(0.1428572F));
/* 384 */     this.path.curveTo(decodeAnchorX(0.8571429F, 3.444444F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.142857F, -3.333333F), decodeAnchorY(0.1428572F, 0.0F), decodeX(2.142857F), decodeY(0.1428572F));
/* 385 */     this.path.curveTo(decodeAnchorX(2.142857F, 3.333333F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(0.8571429F, -3.277778F), decodeX(2.857143F), decodeY(0.8571429F));
/* 386 */     this.path.curveTo(decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(0.8571429F, 3.277778F), decodeAnchorX(2.857143F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeX(2.857143F), decodeY(3.0F));
/* 387 */     this.path.lineTo(decodeX(0.1428572F), decodeY(3.0F));
/* 388 */     this.path.closePath();
/* 389 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath11() {
/* 393 */     this.path.reset();
/* 394 */     this.path.moveTo(decodeX(1.463889F), decodeY(2.25F));
/* 395 */     this.path.lineTo(decodeX(1.465278F), decodeY(2.777778F));
/* 396 */     this.path.lineTo(decodeX(0.3809524F), decodeY(2.777778F));
/* 397 */     this.path.lineTo(decodeX(0.375F), decodeY(0.8809524F));
/* 398 */     this.path.curveTo(decodeAnchorX(0.375F, 0.0F), decodeAnchorY(0.8809524F, -2.25F), decodeAnchorX(0.8452381F, -1.916667F), decodeAnchorY(0.3809524F, 0.0F), decodeX(0.8452381F), decodeY(0.3809524F));
/* 399 */     this.path.lineTo(decodeX(2.10119F), decodeY(0.3809524F));
/* 400 */     this.path.curveTo(decodeAnchorX(2.10119F, 2.125F), decodeAnchorY(0.3809524F, 0.0F), decodeAnchorX(2.630953F, 0.0F), decodeAnchorY(0.8630952F, -2.583333F), decodeX(2.630953F), decodeY(0.8630952F));
/* 401 */     this.path.lineTo(decodeX(2.625F), decodeY(2.763889F));
/* 402 */     this.path.lineTo(decodeX(1.466667F), decodeY(2.777778F));
/* 403 */     this.path.lineTo(decodeX(1.463889F), decodeY(2.236111F));
/* 404 */     this.path.lineTo(decodeX(2.386905F), decodeY(2.222222F));
/* 405 */     this.path.lineTo(decodeX(2.375F), decodeY(0.8690476F));
/* 406 */     this.path.curveTo(decodeAnchorX(2.375F, -7.105427E-015F), decodeAnchorY(0.8690476F, -0.9166667F), decodeAnchorX(2.095238F, 1.083333F), decodeAnchorY(0.6071429F, -1.776357E-015F), decodeX(2.095238F), decodeY(0.6071429F));
/* 407 */     this.path.lineTo(decodeX(0.8333334F), decodeY(0.6130952F));
/* 408 */     this.path.curveTo(decodeAnchorX(0.8333334F, -1.0F), decodeAnchorY(0.6130952F, 0.0F), decodeAnchorX(0.625F, 0.04166667F), decodeAnchorY(0.8690476F, -0.9583333F), decodeX(0.625F), decodeY(0.8690476F));
/* 409 */     this.path.lineTo(decodeX(0.6130952F), decodeY(2.236111F));
/* 410 */     this.path.lineTo(decodeX(1.463889F), decodeY(2.25F));
/* 411 */     this.path.closePath();
/* 412 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath12() {
/* 416 */     this.path.reset();
/* 417 */     this.path.moveTo(decodeX(0.0F), decodeY(0.7142857F));
/* 418 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.7142857F, -3.0F), decodeAnchorX(0.5555556F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(0.5555556F), decodeY(0.0F));
/* 419 */     this.path.curveTo(decodeAnchorX(0.5555556F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(2.444444F, -3.0F), decodeAnchorY(0.0F, 0.0F), decodeX(2.444444F), decodeY(0.0F));
/* 420 */     this.path.curveTo(decodeAnchorX(2.444444F, 3.0F), decodeAnchorY(0.0F, 0.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.7142857F, -3.0F), decodeX(3.0F), decodeY(0.7142857F));
/* 421 */     this.path.curveTo(decodeAnchorX(3.0F, 0.0F), decodeAnchorY(0.7142857F, 3.0F), decodeAnchorX(3.0F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeX(3.0F), decodeY(3.0F));
/* 422 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 423 */     this.path.curveTo(decodeAnchorX(0.0F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeAnchorX(0.0F, 0.0F), decodeAnchorY(0.7142857F, 3.0F), decodeX(0.0F), decodeY(0.7142857F));
/* 424 */     this.path.closePath();
/* 425 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath13() {
/* 429 */     this.path.reset();
/* 430 */     this.path.moveTo(decodeX(0.1111111F), decodeY(3.0F));
/* 431 */     this.path.curveTo(decodeAnchorX(0.1111111F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeAnchorX(0.1111111F, 0.0F), decodeAnchorY(0.8571429F, 3.555556F), decodeX(0.1111111F), decodeY(0.8571429F));
/* 432 */     this.path.curveTo(decodeAnchorX(0.1111111F, 0.0F), decodeAnchorY(0.8571429F, -3.555556F), decodeAnchorX(0.6666667F, -3.444444F), decodeAnchorY(0.1428572F, 0.0F), decodeX(0.6666667F), decodeY(0.1428572F));
/* 433 */     this.path.curveTo(decodeAnchorX(0.6666667F, 3.444444F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.333333F, -3.333333F), decodeAnchorY(0.1428572F, 0.0F), decodeX(2.333333F), decodeY(0.1428572F));
/* 434 */     this.path.curveTo(decodeAnchorX(2.333333F, 3.333333F), decodeAnchorY(0.1428572F, 0.0F), decodeAnchorX(2.888889F, 0.0F), decodeAnchorY(0.8571429F, -3.277778F), decodeX(2.888889F), decodeY(0.8571429F));
/* 435 */     this.path.curveTo(decodeAnchorX(2.888889F, 0.0F), decodeAnchorY(0.8571429F, 3.277778F), decodeAnchorX(2.888889F, 0.0F), decodeAnchorY(3.0F, 0.0F), decodeX(2.888889F), decodeY(3.0F));
/* 436 */     this.path.lineTo(decodeX(0.1111111F), decodeY(3.0F));
/* 437 */     this.path.closePath();
/* 438 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath14() {
/* 442 */     this.path.reset();
/* 443 */     this.path.moveTo(decodeX(1.458333F), decodeY(2.25F));
/* 444 */     this.path.lineTo(decodeX(1.459936F), decodeY(2.777778F));
/* 445 */     this.path.lineTo(decodeX(0.296296F), decodeY(2.777778F));
/* 446 */     this.path.lineTo(decodeX(0.2916667F), decodeY(0.8809524F));
/* 447 */     this.path.curveTo(decodeAnchorX(0.2916667F, 0.0F), decodeAnchorY(0.8809524F, -2.25F), decodeAnchorX(0.657407F, -1.916667F), decodeAnchorY(0.3809524F, 0.0F), decodeX(0.657407F), decodeY(0.3809524F));
/* 448 */     this.path.lineTo(decodeX(2.300926F), decodeY(0.3809524F));
/* 449 */     this.path.curveTo(decodeAnchorX(2.300926F, 2.125F), decodeAnchorY(0.3809524F, 0.0F), decodeAnchorX(2.712963F, 0.0F), decodeAnchorY(0.8630952F, -2.583333F), decodeX(2.712963F), decodeY(0.8630952F));
/* 450 */     this.path.lineTo(decodeX(2.708333F), decodeY(2.763889F));
/* 451 */     this.path.lineTo(decodeX(1.461538F), decodeY(2.777778F));
/* 452 */     this.path.lineTo(decodeX(1.458333F), decodeY(2.236111F));
/* 453 */     this.path.lineTo(decodeX(2.523148F), decodeY(2.222222F));
/* 454 */     this.path.lineTo(decodeX(2.513889F), decodeY(0.8690476F));
/* 455 */     this.path.curveTo(decodeAnchorX(2.513889F, -7.105427E-015F), decodeAnchorY(0.8690476F, -0.9166667F), decodeAnchorX(2.296296F, 1.083333F), decodeAnchorY(0.6071429F, -1.776357E-015F), decodeX(2.296296F), decodeY(0.6071429F));
/* 456 */     this.path.lineTo(decodeX(0.6481482F), decodeY(0.6130952F));
/* 457 */     this.path.curveTo(decodeAnchorX(0.6481482F, -1.0F), decodeAnchorY(0.6130952F, 0.0F), decodeAnchorX(0.486111F, 0.04166667F), decodeAnchorY(0.8690476F, -0.9583333F), decodeX(0.486111F), decodeY(0.8690476F));
/* 458 */     this.path.lineTo(decodeX(0.4768518F), decodeY(2.236111F));
/* 459 */     this.path.lineTo(decodeX(1.458333F), decodeY(2.25F));
/* 460 */     this.path.closePath();
/* 461 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 467 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 468 */     float f1 = (float)localRectangle2D.getX();
/* 469 */     float f2 = (float)localRectangle2D.getY();
/* 470 */     float f3 = (float)localRectangle2D.getWidth();
/* 471 */     float f4 = (float)localRectangle2D.getHeight();
/* 472 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 480 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 481 */     float f1 = (float)localRectangle2D.getX();
/* 482 */     float f2 = (float)localRectangle2D.getY();
/* 483 */     float f3 = (float)localRectangle2D.getWidth();
/* 484 */     float f4 = (float)localRectangle2D.getHeight();
/* 485 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1F, 0.2F, 0.6F, 1.0F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 495 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 496 */     float f1 = (float)localRectangle2D.getX();
/* 497 */     float f2 = (float)localRectangle2D.getY();
/* 498 */     float f3 = (float)localRectangle2D.getWidth();
/* 499 */     float f4 = (float)localRectangle2D.getHeight();
/* 500 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color6, decodeColor(this.color6, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 508 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 509 */     float f1 = (float)localRectangle2D.getX();
/* 510 */     float f2 = (float)localRectangle2D.getY();
/* 511 */     float f3 = (float)localRectangle2D.getWidth();
/* 512 */     float f4 = (float)localRectangle2D.getHeight();
/* 513 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1F, 0.2F, 0.6F, 1.0F }, new Color[] { this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 523 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 524 */     float f1 = (float)localRectangle2D.getX();
/* 525 */     float f2 = (float)localRectangle2D.getY();
/* 526 */     float f3 = (float)localRectangle2D.getWidth();
/* 527 */     float f4 = (float)localRectangle2D.getHeight();
/* 528 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color10, decodeColor(this.color10, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 536 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 537 */     float f1 = (float)localRectangle2D.getX();
/* 538 */     float f2 = (float)localRectangle2D.getY();
/* 539 */     float f3 = (float)localRectangle2D.getWidth();
/* 540 */     float f4 = (float)localRectangle2D.getHeight();
/* 541 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1F, 0.2F, 0.4209678F, 0.6419355F, 0.8209677F, 1.0F }, new Color[] { this.color11, decodeColor(this.color11, this.color12, 0.5F), this.color12, decodeColor(this.color12, this.color13, 0.5F), this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 553 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 554 */     float f1 = (float)localRectangle2D.getX();
/* 555 */     float f2 = (float)localRectangle2D.getY();
/* 556 */     float f3 = (float)localRectangle2D.getWidth();
/* 557 */     float f4 = (float)localRectangle2D.getHeight();
/* 558 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 566 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 567 */     float f1 = (float)localRectangle2D.getX();
/* 568 */     float f2 = (float)localRectangle2D.getY();
/* 569 */     float f3 = (float)localRectangle2D.getWidth();
/* 570 */     float f4 = (float)localRectangle2D.getHeight();
/* 571 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1F, 0.2F, 0.6F, 1.0F }, new Color[] { this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18, decodeColor(this.color18, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient9(Shape paramShape)
/*     */   {
/* 581 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 582 */     float f1 = (float)localRectangle2D.getX();
/* 583 */     float f2 = (float)localRectangle2D.getY();
/* 584 */     float f3 = (float)localRectangle2D.getWidth();
/* 585 */     float f4 = (float)localRectangle2D.getHeight();
/* 586 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1241936F, 0.248387F, 0.4258065F, 0.6032258F, 0.6854839F, 0.7677419F, 0.883871F, 1.0F }, new Color[] { this.color19, decodeColor(this.color19, this.color20, 0.5F), this.color20, decodeColor(this.color20, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22, decodeColor(this.color22, this.color23, 0.5F), this.color23 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient10(Shape paramShape)
/*     */   {
/* 600 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 601 */     float f1 = (float)localRectangle2D.getX();
/* 602 */     float f2 = (float)localRectangle2D.getY();
/* 603 */     float f3 = (float)localRectangle2D.getWidth();
/* 604 */     float f4 = (float)localRectangle2D.getHeight();
/* 605 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color24, decodeColor(this.color24, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient11(Shape paramShape)
/*     */   {
/* 613 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 614 */     float f1 = (float)localRectangle2D.getX();
/* 615 */     float f2 = (float)localRectangle2D.getY();
/* 616 */     float f3 = (float)localRectangle2D.getWidth();
/* 617 */     float f4 = (float)localRectangle2D.getHeight();
/* 618 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color25, decodeColor(this.color25, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient12(Shape paramShape)
/*     */   {
/* 626 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 627 */     float f1 = (float)localRectangle2D.getX();
/* 628 */     float f2 = (float)localRectangle2D.getY();
/* 629 */     float f3 = (float)localRectangle2D.getWidth();
/* 630 */     float f4 = (float)localRectangle2D.getHeight();
/* 631 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1241936F, 0.248387F, 0.4258065F, 0.6032258F, 0.6854839F, 0.7677419F, 0.8677419F, 0.9677419F }, new Color[] { this.color26, decodeColor(this.color26, this.color27, 0.5F), this.color27, decodeColor(this.color27, this.color28, 0.5F), this.color28, decodeColor(this.color28, this.color29, 0.5F), this.color29, decodeColor(this.color29, this.color30, 0.5F), this.color30 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient13(Shape paramShape)
/*     */   {
/* 645 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 646 */     float f1 = (float)localRectangle2D.getX();
/* 647 */     float f2 = (float)localRectangle2D.getY();
/* 648 */     float f3 = (float)localRectangle2D.getWidth();
/* 649 */     float f4 = (float)localRectangle2D.getHeight();
/* 650 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color25, decodeColor(this.color25, this.color31, 0.5F), this.color31 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient14(Shape paramShape)
/*     */   {
/* 658 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 659 */     float f1 = (float)localRectangle2D.getX();
/* 660 */     float f2 = (float)localRectangle2D.getY();
/* 661 */     float f3 = (float)localRectangle2D.getWidth();
/* 662 */     float f4 = (float)localRectangle2D.getHeight();
/* 663 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1241936F, 0.248387F, 0.4258065F, 0.6032258F, 0.6854839F, 0.7677419F, 0.8548387F, 0.9419355F }, new Color[] { this.color32, decodeColor(this.color32, this.color33, 0.5F), this.color33, decodeColor(this.color33, this.color34, 0.5F), this.color34, decodeColor(this.color34, this.color35, 0.5F), this.color35, decodeColor(this.color35, this.color36, 0.5F), this.color36 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TabbedPaneTabPainter
 * JD-Core Version:    0.6.2
 */
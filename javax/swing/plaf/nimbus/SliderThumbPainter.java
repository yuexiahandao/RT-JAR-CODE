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
/*     */ final class SliderThumbPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_FOCUSED = 3;
/*     */   static final int BACKGROUND_FOCUSED_MOUSEOVER = 4;
/*     */   static final int BACKGROUND_FOCUSED_PRESSED = 5;
/*     */   static final int BACKGROUND_MOUSEOVER = 6;
/*     */   static final int BACKGROUND_PRESSED = 7;
/*     */   static final int BACKGROUND_ENABLED_ARROWSHAPE = 8;
/*     */   static final int BACKGROUND_DISABLED_ARROWSHAPE = 9;
/*     */   static final int BACKGROUND_MOUSEOVER_ARROWSHAPE = 10;
/*     */   static final int BACKGROUND_PRESSED_ARROWSHAPE = 11;
/*     */   static final int BACKGROUND_FOCUSED_ARROWSHAPE = 12;
/*     */   static final int BACKGROUND_FOCUSED_MOUSEOVER_ARROWSHAPE = 13;
/*     */   static final int BACKGROUND_FOCUSED_PRESSED_ARROWSHAPE = 14;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  59 */   private Path2D path = new Path2D.Float();
/*  60 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  61 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  62 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  67 */   private Color color1 = decodeColor("nimbusBase", 0.0213483F, -0.5625436F, 0.254902F, 0);
/*  68 */   private Color color2 = decodeColor("nimbusBase", 0.01509833F, -0.5510582F, 0.1921569F, 0);
/*  69 */   private Color color3 = decodeColor("nimbusBase", 0.0213483F, -0.5924243F, 0.3568627F, 0);
/*  70 */   private Color color4 = decodeColor("nimbusBase", 0.0213483F, -0.5672212F, 0.309804F, 0);
/*  71 */   private Color color5 = decodeColor("nimbusBase", 0.0213483F, -0.5684497F, 0.3254902F, 0);
/*  72 */   private Color color6 = decodeColor("nimbusBlueGrey", -0.003968239F, 0.001473688F, -0.254902F, -156);
/*  73 */   private Color color7 = decodeColor("nimbusBase", 0.0005149841F, -0.3458592F, -0.007843137F, 0);
/*  74 */   private Color color8 = decodeColor("nimbusBase", -0.001728594F, -0.1157143F, -0.254902F, 0);
/*  75 */   private Color color9 = decodeColor("nimbusBase", -0.02309609F, -0.6238095F, 0.4392157F, 0);
/*  76 */   private Color color10 = decodeColor("nimbusBase", 0.0005149841F, -0.43867F, 0.2470588F, 0);
/*  77 */   private Color color11 = decodeColor("nimbusBase", 0.0005149841F, -0.4571429F, 0.3294118F, 0);
/*  78 */   private Color color12 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  79 */   private Color color13 = decodeColor("nimbusBase", -0.00382179F, -0.1553221F, -0.1490196F, 0);
/*  80 */   private Color color14 = decodeColor("nimbusBase", -0.5786517F, -0.6357143F, -0.5450981F, 0);
/*  81 */   private Color color15 = decodeColor("nimbusBase", 0.004681647F, -0.6278092F, 0.4431372F, 0);
/*  82 */   private Color color16 = decodeColor("nimbusBase", 0.0002956986F, -0.4653107F, 0.3254902F, 0);
/*  83 */   private Color color17 = decodeColor("nimbusBase", 0.0005149841F, -0.456342F, 0.3254902F, 0);
/*  84 */   private Color color18 = decodeColor("nimbusBase", -0.001728594F, -0.473214F, 0.3921568F, 0);
/*  85 */   private Color color19 = decodeColor("nimbusBase", 0.001595259F, -0.04875779F, -0.1882353F, 0);
/*  86 */   private Color color20 = decodeColor("nimbusBase", 0.0002956986F, -0.4494398F, 0.2509804F, 0);
/*  87 */   private Color color21 = decodeColor("nimbusBase", 0.0F, 0.0F, 0.0F, 0);
/*  88 */   private Color color22 = decodeColor("nimbusBase", 0.0008937717F, -0.1210944F, 0.1215686F, 0);
/*  89 */   private Color color23 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -121);
/*  90 */   private Color color24 = new Color(150, 156, 168, 146);
/*  91 */   private Color color25 = decodeColor("nimbusBase", -0.003382862F, -0.4060847F, -0.01960784F, 0);
/*  92 */   private Color color26 = decodeColor("nimbusBase", 0.0005149841F, -0.1759442F, -0.2078432F, 0);
/*  93 */   private Color color27 = decodeColor("nimbusBase", 0.002300739F, -0.1133263F, -0.2862745F, 0);
/*  94 */   private Color color28 = decodeColor("nimbusBase", -0.02309609F, -0.6237621F, 0.4352941F, 0);
/*  95 */   private Color color29 = decodeColor("nimbusBase", 0.004681647F, -0.594392F, 0.4F, 0);
/*  96 */   private Color color30 = decodeColor("nimbusBase", -0.001728594F, -0.4454704F, 0.254902F, 0);
/*  97 */   private Color color31 = decodeColor("nimbusBase", 0.0005149841F, -0.462554F, 0.3568627F, 0);
/*  98 */   private Color color32 = decodeColor("nimbusBase", 0.0005149841F, -0.474424F, 0.4235294F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public SliderThumbPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/* 106 */     this.state = paramInt;
/* 107 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 113 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 116 */     switch (this.state) { case 1:
/* 117 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/* 118 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 3:
/* 119 */       paintBackgroundFocused(paramGraphics2D); break;
/*     */     case 4:
/* 120 */       paintBackgroundFocusedAndMouseOver(paramGraphics2D); break;
/*     */     case 5:
/* 121 */       paintBackgroundFocusedAndPressed(paramGraphics2D); break;
/*     */     case 6:
/* 122 */       paintBackgroundMouseOver(paramGraphics2D); break;
/*     */     case 7:
/* 123 */       paintBackgroundPressed(paramGraphics2D); break;
/*     */     case 8:
/* 124 */       paintBackgroundEnabledAndArrowShape(paramGraphics2D); break;
/*     */     case 9:
/* 125 */       paintBackgroundDisabledAndArrowShape(paramGraphics2D); break;
/*     */     case 10:
/* 126 */       paintBackgroundMouseOverAndArrowShape(paramGraphics2D); break;
/*     */     case 11:
/* 127 */       paintBackgroundPressedAndArrowShape(paramGraphics2D); break;
/*     */     case 12:
/* 128 */       paintBackgroundFocusedAndArrowShape(paramGraphics2D); break;
/*     */     case 13:
/* 129 */       paintBackgroundFocusedAndMouseOverAndArrowShape(paramGraphics2D); break;
/*     */     case 14:
/* 130 */       paintBackgroundFocusedAndPressedAndArrowShape(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 139 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/* 143 */     this.ellipse = decodeEllipse1();
/* 144 */     paramGraphics2D.setPaint(decodeGradient1(this.ellipse));
/* 145 */     paramGraphics2D.fill(this.ellipse);
/* 146 */     this.ellipse = decodeEllipse2();
/* 147 */     paramGraphics2D.setPaint(decodeGradient2(this.ellipse));
/* 148 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 153 */     this.ellipse = decodeEllipse3();
/* 154 */     paramGraphics2D.setPaint(this.color6);
/* 155 */     paramGraphics2D.fill(this.ellipse);
/* 156 */     this.ellipse = decodeEllipse1();
/* 157 */     paramGraphics2D.setPaint(decodeGradient3(this.ellipse));
/* 158 */     paramGraphics2D.fill(this.ellipse);
/* 159 */     this.ellipse = decodeEllipse2();
/* 160 */     paramGraphics2D.setPaint(decodeGradient4(this.ellipse));
/* 161 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 166 */     this.ellipse = decodeEllipse4();
/* 167 */     paramGraphics2D.setPaint(this.color12);
/* 168 */     paramGraphics2D.fill(this.ellipse);
/* 169 */     this.ellipse = decodeEllipse1();
/* 170 */     paramGraphics2D.setPaint(decodeGradient3(this.ellipse));
/* 171 */     paramGraphics2D.fill(this.ellipse);
/* 172 */     this.ellipse = decodeEllipse2();
/* 173 */     paramGraphics2D.setPaint(decodeGradient4(this.ellipse));
/* 174 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocusedAndMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 179 */     this.ellipse = decodeEllipse4();
/* 180 */     paramGraphics2D.setPaint(this.color12);
/* 181 */     paramGraphics2D.fill(this.ellipse);
/* 182 */     this.ellipse = decodeEllipse1();
/* 183 */     paramGraphics2D.setPaint(decodeGradient5(this.ellipse));
/* 184 */     paramGraphics2D.fill(this.ellipse);
/* 185 */     this.ellipse = decodeEllipse2();
/* 186 */     paramGraphics2D.setPaint(decodeGradient6(this.ellipse));
/* 187 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocusedAndPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 192 */     this.ellipse = decodeEllipse4();
/* 193 */     paramGraphics2D.setPaint(this.color12);
/* 194 */     paramGraphics2D.fill(this.ellipse);
/* 195 */     this.ellipse = decodeEllipse1();
/* 196 */     paramGraphics2D.setPaint(decodeGradient7(this.ellipse));
/* 197 */     paramGraphics2D.fill(this.ellipse);
/* 198 */     this.ellipse = decodeEllipse2();
/* 199 */     paramGraphics2D.setPaint(decodeGradient8(this.ellipse));
/* 200 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 205 */     this.ellipse = decodeEllipse3();
/* 206 */     paramGraphics2D.setPaint(this.color6);
/* 207 */     paramGraphics2D.fill(this.ellipse);
/* 208 */     this.ellipse = decodeEllipse1();
/* 209 */     paramGraphics2D.setPaint(decodeGradient5(this.ellipse));
/* 210 */     paramGraphics2D.fill(this.ellipse);
/* 211 */     this.ellipse = decodeEllipse2();
/* 212 */     paramGraphics2D.setPaint(decodeGradient6(this.ellipse));
/* 213 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 218 */     this.ellipse = decodeEllipse3();
/* 219 */     paramGraphics2D.setPaint(this.color23);
/* 220 */     paramGraphics2D.fill(this.ellipse);
/* 221 */     this.ellipse = decodeEllipse1();
/* 222 */     paramGraphics2D.setPaint(decodeGradient7(this.ellipse));
/* 223 */     paramGraphics2D.fill(this.ellipse);
/* 224 */     this.ellipse = decodeEllipse2();
/* 225 */     paramGraphics2D.setPaint(decodeGradient8(this.ellipse));
/* 226 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndArrowShape(Graphics2D paramGraphics2D)
/*     */   {
/* 231 */     this.path = decodePath1();
/* 232 */     paramGraphics2D.setPaint(this.color24);
/* 233 */     paramGraphics2D.fill(this.path);
/* 234 */     this.path = decodePath2();
/* 235 */     paramGraphics2D.setPaint(decodeGradient9(this.path));
/* 236 */     paramGraphics2D.fill(this.path);
/* 237 */     this.path = decodePath3();
/* 238 */     paramGraphics2D.setPaint(decodeGradient10(this.path));
/* 239 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabledAndArrowShape(Graphics2D paramGraphics2D)
/*     */   {
/* 244 */     this.path = decodePath2();
/* 245 */     paramGraphics2D.setPaint(decodeGradient11(this.path));
/* 246 */     paramGraphics2D.fill(this.path);
/* 247 */     this.path = decodePath3();
/* 248 */     paramGraphics2D.setPaint(decodeGradient12(this.path));
/* 249 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOverAndArrowShape(Graphics2D paramGraphics2D)
/*     */   {
/* 254 */     this.path = decodePath1();
/* 255 */     paramGraphics2D.setPaint(this.color24);
/* 256 */     paramGraphics2D.fill(this.path);
/* 257 */     this.path = decodePath2();
/* 258 */     paramGraphics2D.setPaint(decodeGradient13(this.path));
/* 259 */     paramGraphics2D.fill(this.path);
/* 260 */     this.path = decodePath3();
/* 261 */     paramGraphics2D.setPaint(decodeGradient14(this.path));
/* 262 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressedAndArrowShape(Graphics2D paramGraphics2D)
/*     */   {
/* 267 */     this.path = decodePath1();
/* 268 */     paramGraphics2D.setPaint(this.color24);
/* 269 */     paramGraphics2D.fill(this.path);
/* 270 */     this.path = decodePath2();
/* 271 */     paramGraphics2D.setPaint(decodeGradient15(this.path));
/* 272 */     paramGraphics2D.fill(this.path);
/* 273 */     this.path = decodePath3();
/* 274 */     paramGraphics2D.setPaint(decodeGradient16(this.path));
/* 275 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocusedAndArrowShape(Graphics2D paramGraphics2D)
/*     */   {
/* 280 */     this.path = decodePath4();
/* 281 */     paramGraphics2D.setPaint(this.color12);
/* 282 */     paramGraphics2D.fill(this.path);
/* 283 */     this.path = decodePath2();
/* 284 */     paramGraphics2D.setPaint(decodeGradient9(this.path));
/* 285 */     paramGraphics2D.fill(this.path);
/* 286 */     this.path = decodePath3();
/* 287 */     paramGraphics2D.setPaint(decodeGradient17(this.path));
/* 288 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocusedAndMouseOverAndArrowShape(Graphics2D paramGraphics2D)
/*     */   {
/* 293 */     this.path = decodePath4();
/* 294 */     paramGraphics2D.setPaint(this.color12);
/* 295 */     paramGraphics2D.fill(this.path);
/* 296 */     this.path = decodePath2();
/* 297 */     paramGraphics2D.setPaint(decodeGradient13(this.path));
/* 298 */     paramGraphics2D.fill(this.path);
/* 299 */     this.path = decodePath3();
/* 300 */     paramGraphics2D.setPaint(decodeGradient14(this.path));
/* 301 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocusedAndPressedAndArrowShape(Graphics2D paramGraphics2D)
/*     */   {
/* 306 */     this.path = decodePath4();
/* 307 */     paramGraphics2D.setPaint(this.color12);
/* 308 */     paramGraphics2D.fill(this.path);
/* 309 */     this.path = decodePath2();
/* 310 */     paramGraphics2D.setPaint(decodeGradient15(this.path));
/* 311 */     paramGraphics2D.fill(this.path);
/* 312 */     this.path = decodePath3();
/* 313 */     paramGraphics2D.setPaint(decodeGradient16(this.path));
/* 314 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse1()
/*     */   {
/* 321 */     this.ellipse.setFrame(decodeX(0.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.4F));
/*     */ 
/* 325 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse2() {
/* 329 */     this.ellipse.setFrame(decodeX(0.6F), decodeY(0.6F), decodeX(2.4F) - decodeX(0.6F), decodeY(2.4F) - decodeY(0.6F));
/*     */ 
/* 333 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse3() {
/* 337 */     this.ellipse.setFrame(decodeX(0.4F), decodeY(0.6F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.8F) - decodeY(0.6F));
/*     */ 
/* 341 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse4() {
/* 345 */     this.ellipse.setFrame(decodeX(0.12F), decodeY(0.12F), decodeX(2.88F) - decodeX(0.12F), decodeY(2.88F) - decodeY(0.12F));
/*     */ 
/* 349 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 353 */     this.path.reset();
/* 354 */     this.path.moveTo(decodeX(0.8166667F), decodeY(0.5007576F));
/* 355 */     this.path.curveTo(decodeAnchorX(0.8166667F, 1.564327F), decodeAnchorY(0.5007576F, -0.309751F), decodeAnchorX(2.792546F, 0.05817359F), decodeAnchorY(1.611688F, -0.4647635F), decodeX(2.792546F), decodeY(1.611688F));
/* 356 */     this.path.curveTo(decodeAnchorX(2.792546F, -0.3408686F), decodeAnchorY(1.611688F, 2.723285F), decodeAnchorX(0.7006364F, 4.568128F), decodeAnchorY(2.769364F, -0.006014915F), decodeX(0.7006364F), decodeY(2.769364F));
/* 357 */     this.path.curveTo(decodeAnchorX(0.7006364F, -3.523396F), decodeAnchorY(2.769364F, 0.004639302F), decodeAnchorX(0.8166667F, -1.863526F), decodeAnchorY(0.5007576F, 0.3689954F), decodeX(0.8166667F), decodeY(0.5007576F));
/* 358 */     this.path.closePath();
/* 359 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 363 */     this.path.reset();
/* 364 */     this.path.moveTo(decodeX(0.6155303F), decodeY(2.595455F));
/* 365 */     this.path.curveTo(decodeAnchorX(0.6155303F, 0.9098089F), decodeAnchorY(2.595455F, 1.315424F), decodeAnchorX(2.615152F, 0.01458881F), decodeAnchorY(1.611201F, 0.9295521F), decodeX(2.615152F), decodeY(1.611201F));
/* 366 */     this.path.curveTo(decodeAnchorX(2.615152F, -0.0136552F), decodeAnchorY(1.611201F, -0.8700643F), decodeAnchorX(0.6092392F, 0.9729935F), decodeAnchorY(0.407164F, -1.424864F), decodeX(0.6092392F), decodeY(0.407164F));
/* 367 */     this.path.curveTo(decodeAnchorX(0.6092392F, -0.7485209F), decodeAnchorY(0.407164F, 1.096144F), decodeAnchorX(0.6155303F, -0.749988F), decodeAnchorY(2.595455F, -1.084351F), decodeX(0.6155303F), decodeY(2.595455F));
/* 368 */     this.path.closePath();
/* 369 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 373 */     this.path.reset();
/* 374 */     this.path.moveTo(decodeX(0.8055606F), decodeY(0.6009697F));
/* 375 */     this.path.curveTo(decodeAnchorX(0.8055606F, 0.5082089F), decodeAnchorY(0.6009697F, -0.8490881F), decodeAnchorX(2.369273F, 0.003184607F), decodeAnchorY(1.613117F, -0.6066883F), decodeX(2.369273F), decodeY(1.613117F));
/* 376 */     this.path.curveTo(decodeAnchorX(2.369273F, -0.003890196F), decodeAnchorY(1.613117F, 0.7411076F), decodeAnchorX(0.7945455F, 0.3870974F), decodeAnchorY(2.393273F, 1.240782F), decodeX(0.7945455F), decodeY(2.393273F));
/* 377 */     this.path.curveTo(decodeAnchorX(0.7945455F, -0.3863658F), decodeAnchorY(2.393273F, -1.238437F), decodeAnchorX(0.8055606F, -0.995154F), decodeAnchorY(0.6009697F, 1.66265F), decodeX(0.8055606F), decodeY(0.6009697F));
/* 378 */     this.path.closePath();
/* 379 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 383 */     this.path.reset();
/* 384 */     this.path.moveTo(decodeX(0.6005952F), decodeY(0.1172754F));
/* 385 */     this.path.curveTo(decodeAnchorX(0.6005952F, 1.564327F), decodeAnchorY(0.1172754F, -0.309751F), decodeAnchorX(2.792546F, 0.00440584F), decodeAnchorY(1.611688F, -1.188116F), decodeX(2.792546F), decodeY(1.611688F));
/* 386 */     this.path.curveTo(decodeAnchorX(2.792546F, -0.007364541F), decodeAnchorY(1.611688F, 1.985983F), decodeAnchorX(0.7006364F, 2.771686F), decodeAnchorY(2.869364F, -0.00897458F), decodeX(0.7006364F), decodeY(2.869364F));
/* 387 */     this.path.curveTo(decodeAnchorX(0.7006364F, -3.754899F), decodeAnchorY(2.869364F, 0.01215818F), decodeAnchorX(0.6005952F, -1.863526F), decodeAnchorY(0.1172754F, 0.3689954F), decodeX(0.6005952F), decodeY(0.1172754F));
/* 388 */     this.path.closePath();
/* 389 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 395 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 396 */     float f1 = (float)localRectangle2D.getX();
/* 397 */     float f2 = (float)localRectangle2D.getY();
/* 398 */     float f3 = (float)localRectangle2D.getWidth();
/* 399 */     float f4 = (float)localRectangle2D.getHeight();
/* 400 */     return decodeGradient(0.51061F * f3 + f1, -4.553649E-018F * f4 + f2, 0.4993369F * f3 + f1, 1.003979F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 408 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 409 */     float f1 = (float)localRectangle2D.getX();
/* 410 */     float f2 = (float)localRectangle2D.getY();
/* 411 */     float f3 = (float)localRectangle2D.getWidth();
/* 412 */     float f4 = (float)localRectangle2D.getHeight();
/* 413 */     return decodeGradient(0.5023511F * f3 + f1, 0.001567398F * f4 + f2, 0.5023511F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2125668F, 0.4251337F, 0.7125669F, 1.0F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 423 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 424 */     float f1 = (float)localRectangle2D.getX();
/* 425 */     float f2 = (float)localRectangle2D.getY();
/* 426 */     float f3 = (float)localRectangle2D.getWidth();
/* 427 */     float f4 = (float)localRectangle2D.getHeight();
/* 428 */     return decodeGradient(0.51F * f3 + f1, -4.553649E-018F * f4 + f2, 0.51F * f3 + f1, 1.003979F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 436 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 437 */     float f1 = (float)localRectangle2D.getX();
/* 438 */     float f2 = (float)localRectangle2D.getY();
/* 439 */     float f3 = (float)localRectangle2D.getWidth();
/* 440 */     float f4 = (float)localRectangle2D.getHeight();
/* 441 */     return decodeGradient(0.5F * f3 + f1, 0.001567398F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2125668F, 0.4251337F, 0.5614973F, 0.697861F, 0.8489305F, 1.0F }, new Color[] { this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10, decodeColor(this.color10, this.color10, 0.5F), this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 453 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 454 */     float f1 = (float)localRectangle2D.getX();
/* 455 */     float f2 = (float)localRectangle2D.getY();
/* 456 */     float f3 = (float)localRectangle2D.getWidth();
/* 457 */     float f4 = (float)localRectangle2D.getHeight();
/* 458 */     return decodeGradient(0.51061F * f3 + f1, -4.553649E-018F * f4 + f2, 0.4993369F * f3 + f1, 1.003979F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 466 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 467 */     float f1 = (float)localRectangle2D.getX();
/* 468 */     float f2 = (float)localRectangle2D.getY();
/* 469 */     float f3 = (float)localRectangle2D.getWidth();
/* 470 */     float f4 = (float)localRectangle2D.getHeight();
/* 471 */     return decodeGradient(0.5023511F * f3 + f1, 0.001567398F * f4 + f2, 0.5023511F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2125668F, 0.4251337F, 0.5614973F, 0.697861F, 0.8489305F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 483 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 484 */     float f1 = (float)localRectangle2D.getX();
/* 485 */     float f2 = (float)localRectangle2D.getY();
/* 486 */     float f3 = (float)localRectangle2D.getWidth();
/* 487 */     float f4 = (float)localRectangle2D.getHeight();
/* 488 */     return decodeGradient(0.51061F * f3 + f1, -4.553649E-018F * f4 + f2, 0.4993369F * f3 + f1, 1.003979F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color14, decodeColor(this.color14, this.color19, 0.5F), this.color19 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 496 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 497 */     float f1 = (float)localRectangle2D.getX();
/* 498 */     float f2 = (float)localRectangle2D.getY();
/* 499 */     float f3 = (float)localRectangle2D.getWidth();
/* 500 */     float f4 = (float)localRectangle2D.getHeight();
/* 501 */     return decodeGradient(0.5023511F * f3 + f1, 0.001567398F * f4 + f2, 0.5023511F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2379679F, 0.4759358F, 0.5360962F, 0.5962567F, 0.7981284F, 1.0F }, new Color[] { this.color20, decodeColor(this.color20, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient9(Shape paramShape)
/*     */   {
/* 513 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 514 */     float f1 = (float)localRectangle2D.getX();
/* 515 */     float f2 = (float)localRectangle2D.getY();
/* 516 */     float f3 = (float)localRectangle2D.getWidth();
/* 517 */     float f4 = (float)localRectangle2D.getHeight();
/* 518 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2403226F, 0.4806452F, 0.7403226F, 1.0F }, new Color[] { this.color25, decodeColor(this.color25, this.color26, 0.5F), this.color26, decodeColor(this.color26, this.color27, 0.5F), this.color27 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient10(Shape paramShape)
/*     */   {
/* 528 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 529 */     float f1 = (float)localRectangle2D.getX();
/* 530 */     float f2 = (float)localRectangle2D.getY();
/* 531 */     float f3 = (float)localRectangle2D.getWidth();
/* 532 */     float f4 = (float)localRectangle2D.getHeight();
/* 533 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.06129032F, 0.101613F, 0.1419355F, 0.3016129F, 0.4612903F, 0.5983871F, 0.7354839F, 0.7935484F, 0.8516129F }, new Color[] { this.color28, decodeColor(this.color28, this.color29, 0.5F), this.color29, decodeColor(this.color29, this.color30, 0.5F), this.color30, decodeColor(this.color30, this.color31, 0.5F), this.color31, decodeColor(this.color31, this.color32, 0.5F), this.color32 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient11(Shape paramShape)
/*     */   {
/* 547 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 548 */     float f1 = (float)localRectangle2D.getX();
/* 549 */     float f2 = (float)localRectangle2D.getY();
/* 550 */     float f3 = (float)localRectangle2D.getWidth();
/* 551 */     float f4 = (float)localRectangle2D.getHeight();
/* 552 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient12(Shape paramShape)
/*     */   {
/* 560 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 561 */     float f1 = (float)localRectangle2D.getX();
/* 562 */     float f2 = (float)localRectangle2D.getY();
/* 563 */     float f3 = (float)localRectangle2D.getWidth();
/* 564 */     float f4 = (float)localRectangle2D.getHeight();
/* 565 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2125668F, 0.4251337F, 0.7125669F, 1.0F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient13(Shape paramShape)
/*     */   {
/* 575 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 576 */     float f1 = (float)localRectangle2D.getX();
/* 577 */     float f2 = (float)localRectangle2D.getY();
/* 578 */     float f3 = (float)localRectangle2D.getWidth();
/* 579 */     float f4 = (float)localRectangle2D.getHeight();
/* 580 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient14(Shape paramShape)
/*     */   {
/* 588 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 589 */     float f1 = (float)localRectangle2D.getX();
/* 590 */     float f2 = (float)localRectangle2D.getY();
/* 591 */     float f3 = (float)localRectangle2D.getWidth();
/* 592 */     float f4 = (float)localRectangle2D.getHeight();
/* 593 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2125668F, 0.4251337F, 0.5614973F, 0.697861F, 0.8489305F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient15(Shape paramShape)
/*     */   {
/* 605 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 606 */     float f1 = (float)localRectangle2D.getX();
/* 607 */     float f2 = (float)localRectangle2D.getY();
/* 608 */     float f3 = (float)localRectangle2D.getWidth();
/* 609 */     float f4 = (float)localRectangle2D.getHeight();
/* 610 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color14, decodeColor(this.color14, this.color19, 0.5F), this.color19 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient16(Shape paramShape)
/*     */   {
/* 618 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 619 */     float f1 = (float)localRectangle2D.getX();
/* 620 */     float f2 = (float)localRectangle2D.getY();
/* 621 */     float f3 = (float)localRectangle2D.getWidth();
/* 622 */     float f4 = (float)localRectangle2D.getHeight();
/* 623 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2379679F, 0.4759358F, 0.5360962F, 0.5962567F, 0.7981284F, 1.0F }, new Color[] { this.color20, decodeColor(this.color20, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient17(Shape paramShape)
/*     */   {
/* 635 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 636 */     float f1 = (float)localRectangle2D.getX();
/* 637 */     float f2 = (float)localRectangle2D.getY();
/* 638 */     float f3 = (float)localRectangle2D.getWidth();
/* 639 */     float f4 = (float)localRectangle2D.getHeight();
/* 640 */     return decodeGradient(0.4925773F * f3 + f1, 0.08201987F * f4 + f2, 0.4925773F * f3 + f1, 0.9179801F * f4 + f2, new float[] { 0.06129032F, 0.101613F, 0.1419355F, 0.3016129F, 0.4612903F, 0.5983871F, 0.7354839F, 0.7935484F, 0.8516129F }, new Color[] { this.color28, decodeColor(this.color28, this.color29, 0.5F), this.color29, decodeColor(this.color29, this.color30, 0.5F), this.color30, decodeColor(this.color30, this.color31, 0.5F), this.color31, decodeColor(this.color31, this.color32, 0.5F), this.color32 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.SliderThumbPainter
 * JD-Core Version:    0.6.2
 */
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
/*     */ final class TreePainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_ENABLED_SELECTED = 3;
/*     */   static final int LEAFICON_ENABLED = 4;
/*     */   static final int CLOSEDICON_ENABLED = 5;
/*     */   static final int OPENICON_ENABLED = 6;
/*     */   static final int COLLAPSEDICON_ENABLED = 7;
/*     */   static final int COLLAPSEDICON_ENABLED_SELECTED = 8;
/*     */   static final int EXPANDEDICON_ENABLED = 9;
/*     */   static final int EXPANDEDICON_ENABLED_SELECTED = 10;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  55 */   private Path2D path = new Path2D.Float();
/*  56 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  57 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  58 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  63 */   private Color color1 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.06565452F, -0.1333333F, 0);
/*  64 */   private Color color2 = new Color(97, 98, 102, 255);
/*  65 */   private Color color3 = decodeColor("nimbusBlueGrey", -0.03267974F, -0.04333264F, 0.2470588F, 0);
/*  66 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  67 */   private Color color5 = decodeColor("nimbusBase", 0.007768095F, -0.5178103F, 0.3490196F, 0);
/*  68 */   private Color color6 = decodeColor("nimbusBase", 0.01394087F, -0.599277F, 0.4196078F, 0);
/*  69 */   private Color color7 = decodeColor("nimbusBase", 0.004681647F, -0.419805F, 0.1411765F, 0);
/*  70 */   private Color color8 = decodeColor("nimbusBase", 0.0F, -0.6357143F, 0.4509804F, -127);
/*  71 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.0F, 0.0F, -0.21F, -99);
/*  72 */   private Color color10 = decodeColor("nimbusBase", 0.0002956986F, -0.4597884F, 0.298039F, 0);
/*  73 */   private Color color11 = decodeColor("nimbusBase", 0.001595259F, -0.3484803F, 0.1882353F, 0);
/*  74 */   private Color color12 = decodeColor("nimbusBase", 0.001595259F, -0.3084416F, 0.0980392F, 0);
/*  75 */   private Color color13 = decodeColor("nimbusBase", 0.001595259F, -0.2732982F, 0.03529412F, 0);
/*  76 */   private Color color14 = decodeColor("nimbusBase", 0.004681647F, -0.6198413F, 0.4392157F, 0);
/*  77 */   private Color color15 = decodeColor("nimbusBase", 0.0F, -0.6357143F, 0.4509804F, -125);
/*  78 */   private Color color16 = decodeColor("nimbusBase", 0.0F, -0.6357143F, 0.4509804F, -50);
/*  79 */   private Color color17 = decodeColor("nimbusBase", 0.0F, -0.6357143F, 0.4509804F, -100);
/*  80 */   private Color color18 = decodeColor("nimbusBase", 0.001209438F, -0.2357143F, -0.0784314F, 0);
/*  81 */   private Color color19 = decodeColor("nimbusBase", 0.0002956986F, -0.1151664F, -0.2627451F, 0);
/*  82 */   private Color color20 = decodeColor("nimbusBase", 0.002743661F, -0.335015F, 0.01176471F, 0);
/*  83 */   private Color color21 = decodeColor("nimbusBase", 0.002429426F, -0.3857143F, 0.03137255F, 0);
/*  84 */   private Color color22 = decodeColor("nimbusBase", 0.001808107F, -0.359524F, -0.1372549F, 0);
/*  85 */   private Color color23 = new Color(255, 200, 0, 255);
/*  86 */   private Color color24 = decodeColor("nimbusBase", 0.004681647F, -0.3349624F, -0.02745098F, 0);
/*  87 */   private Color color25 = decodeColor("nimbusBase", 0.001993477F, -0.361378F, -0.1058824F, 0);
/*  88 */   private Color color26 = decodeColor("nimbusBlueGrey", -0.611111F, -0.1105263F, -0.3450981F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public TreePainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  96 */     this.state = paramInt;
/*  97 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 103 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 106 */     switch (this.state) { case 4:
/* 107 */       paintleafIconEnabled(paramGraphics2D); break;
/*     */     case 5:
/* 108 */       paintclosedIconEnabled(paramGraphics2D); break;
/*     */     case 6:
/* 109 */       paintopenIconEnabled(paramGraphics2D); break;
/*     */     case 7:
/* 110 */       paintcollapsedIconEnabled(paramGraphics2D); break;
/*     */     case 8:
/* 111 */       paintcollapsedIconEnabledAndSelected(paramGraphics2D); break;
/*     */     case 9:
/* 112 */       paintexpandedIconEnabled(paramGraphics2D); break;
/*     */     case 10:
/* 113 */       paintexpandedIconEnabledAndSelected(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 122 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintleafIconEnabled(Graphics2D paramGraphics2D) {
/* 126 */     this.path = decodePath1();
/* 127 */     paramGraphics2D.setPaint(this.color1);
/* 128 */     paramGraphics2D.fill(this.path);
/* 129 */     this.rect = decodeRect1();
/* 130 */     paramGraphics2D.setPaint(this.color2);
/* 131 */     paramGraphics2D.fill(this.rect);
/* 132 */     this.path = decodePath2();
/* 133 */     paramGraphics2D.setPaint(decodeGradient1(this.path));
/* 134 */     paramGraphics2D.fill(this.path);
/* 135 */     this.path = decodePath3();
/* 136 */     paramGraphics2D.setPaint(decodeGradient2(this.path));
/* 137 */     paramGraphics2D.fill(this.path);
/* 138 */     this.path = decodePath4();
/* 139 */     paramGraphics2D.setPaint(this.color7);
/* 140 */     paramGraphics2D.fill(this.path);
/* 141 */     this.path = decodePath5();
/* 142 */     paramGraphics2D.setPaint(this.color8);
/* 143 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintclosedIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 148 */     this.path = decodePath6();
/* 149 */     paramGraphics2D.setPaint(this.color9);
/* 150 */     paramGraphics2D.fill(this.path);
/* 151 */     this.path = decodePath7();
/* 152 */     paramGraphics2D.setPaint(decodeGradient3(this.path));
/* 153 */     paramGraphics2D.fill(this.path);
/* 154 */     this.path = decodePath8();
/* 155 */     paramGraphics2D.setPaint(decodeGradient4(this.path));
/* 156 */     paramGraphics2D.fill(this.path);
/* 157 */     this.rect = decodeRect2();
/* 158 */     paramGraphics2D.setPaint(this.color15);
/* 159 */     paramGraphics2D.fill(this.rect);
/* 160 */     this.rect = decodeRect3();
/* 161 */     paramGraphics2D.setPaint(this.color16);
/* 162 */     paramGraphics2D.fill(this.rect);
/* 163 */     this.rect = decodeRect4();
/* 164 */     paramGraphics2D.setPaint(this.color17);
/* 165 */     paramGraphics2D.fill(this.rect);
/* 166 */     this.path = decodePath9();
/* 167 */     paramGraphics2D.setPaint(decodeGradient5(this.path));
/* 168 */     paramGraphics2D.fill(this.path);
/* 169 */     this.path = decodePath10();
/* 170 */     paramGraphics2D.setPaint(decodeGradient6(this.path));
/* 171 */     paramGraphics2D.fill(this.path);
/* 172 */     this.path = decodePath11();
/* 173 */     paramGraphics2D.setPaint(this.color23);
/* 174 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintopenIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 179 */     this.path = decodePath6();
/* 180 */     paramGraphics2D.setPaint(this.color9);
/* 181 */     paramGraphics2D.fill(this.path);
/* 182 */     this.path = decodePath12();
/* 183 */     paramGraphics2D.setPaint(decodeGradient3(this.path));
/* 184 */     paramGraphics2D.fill(this.path);
/* 185 */     this.path = decodePath13();
/* 186 */     paramGraphics2D.setPaint(decodeGradient4(this.path));
/* 187 */     paramGraphics2D.fill(this.path);
/* 188 */     this.rect = decodeRect2();
/* 189 */     paramGraphics2D.setPaint(this.color15);
/* 190 */     paramGraphics2D.fill(this.rect);
/* 191 */     this.rect = decodeRect3();
/* 192 */     paramGraphics2D.setPaint(this.color16);
/* 193 */     paramGraphics2D.fill(this.rect);
/* 194 */     this.rect = decodeRect4();
/* 195 */     paramGraphics2D.setPaint(this.color17);
/* 196 */     paramGraphics2D.fill(this.rect);
/* 197 */     this.path = decodePath14();
/* 198 */     paramGraphics2D.setPaint(decodeGradient5(this.path));
/* 199 */     paramGraphics2D.fill(this.path);
/* 200 */     this.path = decodePath15();
/* 201 */     paramGraphics2D.setPaint(decodeGradient7(this.path));
/* 202 */     paramGraphics2D.fill(this.path);
/* 203 */     this.path = decodePath11();
/* 204 */     paramGraphics2D.setPaint(this.color23);
/* 205 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintcollapsedIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 210 */     this.path = decodePath16();
/* 211 */     paramGraphics2D.setPaint(this.color26);
/* 212 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintcollapsedIconEnabledAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 217 */     this.path = decodePath16();
/* 218 */     paramGraphics2D.setPaint(this.color4);
/* 219 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintexpandedIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 224 */     this.path = decodePath17();
/* 225 */     paramGraphics2D.setPaint(this.color26);
/* 226 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintexpandedIconEnabledAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 231 */     this.path = decodePath17();
/* 232 */     paramGraphics2D.setPaint(this.color4);
/* 233 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 240 */     this.path.reset();
/* 241 */     this.path.moveTo(decodeX(0.2F), decodeY(0.0F));
/* 242 */     this.path.lineTo(decodeX(0.2F), decodeY(3.0F));
/* 243 */     this.path.lineTo(decodeX(0.4F), decodeY(3.0F));
/* 244 */     this.path.lineTo(decodeX(0.4F), decodeY(0.2F));
/* 245 */     this.path.lineTo(decodeX(1.919753F), decodeY(0.2F));
/* 246 */     this.path.lineTo(decodeX(2.6F), decodeY(0.9F));
/* 247 */     this.path.lineTo(decodeX(2.6F), decodeY(3.0F));
/* 248 */     this.path.lineTo(decodeX(2.8F), decodeY(3.0F));
/* 249 */     this.path.lineTo(decodeX(2.8F), decodeY(0.888889F));
/* 250 */     this.path.lineTo(decodeX(1.953704F), decodeY(0.0F));
/* 251 */     this.path.lineTo(decodeX(0.2F), decodeY(0.0F));
/* 252 */     this.path.closePath();
/* 253 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1() {
/* 257 */     this.rect.setRect(decodeX(0.4F), decodeY(2.8F), decodeX(2.6F) - decodeX(0.4F), decodeY(3.0F) - decodeY(2.8F));
/*     */ 
/* 261 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 265 */     this.path.reset();
/* 266 */     this.path.moveTo(decodeX(1.833333F), decodeY(0.2F));
/* 267 */     this.path.lineTo(decodeX(1.833333F), decodeY(1.0F));
/* 268 */     this.path.lineTo(decodeX(2.6F), decodeY(1.0F));
/* 269 */     this.path.lineTo(decodeX(1.833333F), decodeY(0.2F));
/* 270 */     this.path.closePath();
/* 271 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath3() {
/* 275 */     this.path.reset();
/* 276 */     this.path.moveTo(decodeX(1.833333F), decodeY(0.2F));
/* 277 */     this.path.lineTo(decodeX(0.4F), decodeY(0.2F));
/* 278 */     this.path.lineTo(decodeX(0.4F), decodeY(2.8F));
/* 279 */     this.path.lineTo(decodeX(2.6F), decodeY(2.8F));
/* 280 */     this.path.lineTo(decodeX(2.6F), decodeY(1.0F));
/* 281 */     this.path.lineTo(decodeX(1.833333F), decodeY(1.0F));
/* 282 */     this.path.lineTo(decodeX(1.833333F), decodeY(0.2F));
/* 283 */     this.path.closePath();
/* 284 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath4() {
/* 288 */     this.path.reset();
/* 289 */     this.path.moveTo(decodeX(1.833333F), decodeY(0.2F));
/* 290 */     this.path.lineTo(decodeX(1.623457F), decodeY(0.2F));
/* 291 */     this.path.lineTo(decodeX(1.62963F), decodeY(1.203704F));
/* 292 */     this.path.lineTo(decodeX(2.6F), decodeY(1.200617F));
/* 293 */     this.path.lineTo(decodeX(2.6F), decodeY(1.0F));
/* 294 */     this.path.lineTo(decodeX(1.833333F), decodeY(1.0F));
/* 295 */     this.path.lineTo(decodeX(1.833333F), decodeY(0.2F));
/* 296 */     this.path.closePath();
/* 297 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath5() {
/* 301 */     this.path.reset();
/* 302 */     this.path.moveTo(decodeX(1.833333F), decodeY(0.4F));
/* 303 */     this.path.lineTo(decodeX(1.833333F), decodeY(0.2F));
/* 304 */     this.path.lineTo(decodeX(0.4F), decodeY(0.2F));
/* 305 */     this.path.lineTo(decodeX(0.4F), decodeY(2.8F));
/* 306 */     this.path.lineTo(decodeX(2.6F), decodeY(2.8F));
/* 307 */     this.path.lineTo(decodeX(2.6F), decodeY(1.0F));
/* 308 */     this.path.lineTo(decodeX(2.4F), decodeY(1.0F));
/* 309 */     this.path.lineTo(decodeX(2.4F), decodeY(2.6F));
/* 310 */     this.path.lineTo(decodeX(0.6F), decodeY(2.6F));
/* 311 */     this.path.lineTo(decodeX(0.6F), decodeY(0.4F));
/* 312 */     this.path.lineTo(decodeX(1.833333F), decodeY(0.4F));
/* 313 */     this.path.closePath();
/* 314 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath6() {
/* 318 */     this.path.reset();
/* 319 */     this.path.moveTo(decodeX(0.0F), decodeY(2.4F));
/* 320 */     this.path.lineTo(decodeX(0.0F), decodeY(2.6F));
/* 321 */     this.path.lineTo(decodeX(0.2F), decodeY(3.0F));
/* 322 */     this.path.lineTo(decodeX(2.6F), decodeY(3.0F));
/* 323 */     this.path.lineTo(decodeX(2.8F), decodeY(2.6F));
/* 324 */     this.path.lineTo(decodeX(2.8F), decodeY(2.4F));
/* 325 */     this.path.lineTo(decodeX(0.0F), decodeY(2.4F));
/* 326 */     this.path.closePath();
/* 327 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath7() {
/* 331 */     this.path.reset();
/* 332 */     this.path.moveTo(decodeX(0.6F), decodeY(2.6F));
/* 333 */     this.path.lineTo(decodeX(0.6037037F), decodeY(1.842593F));
/* 334 */     this.path.lineTo(decodeX(0.8F), decodeY(1.0F));
/* 335 */     this.path.lineTo(decodeX(2.8F), decodeY(1.0F));
/* 336 */     this.path.lineTo(decodeX(2.8F), decodeY(1.333333F));
/* 337 */     this.path.lineTo(decodeX(2.6F), decodeY(2.6F));
/* 338 */     this.path.lineTo(decodeX(0.6F), decodeY(2.6F));
/* 339 */     this.path.closePath();
/* 340 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath8() {
/* 344 */     this.path.reset();
/* 345 */     this.path.moveTo(decodeX(0.2F), decodeY(2.6F));
/* 346 */     this.path.lineTo(decodeX(0.4F), decodeY(2.6F));
/* 347 */     this.path.lineTo(decodeX(0.4083334F), decodeY(1.864583F));
/* 348 */     this.path.lineTo(decodeX(0.7958334F), decodeY(0.8F));
/* 349 */     this.path.lineTo(decodeX(2.4F), decodeY(0.8F));
/* 350 */     this.path.lineTo(decodeX(2.4F), decodeY(0.6F));
/* 351 */     this.path.lineTo(decodeX(1.5F), decodeY(0.6F));
/* 352 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.4F));
/* 353 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.2F));
/* 354 */     this.path.lineTo(decodeX(0.6F), decodeY(0.2F));
/* 355 */     this.path.lineTo(decodeX(0.6F), decodeY(0.4F));
/* 356 */     this.path.lineTo(decodeX(0.4F), decodeY(0.6F));
/* 357 */     this.path.lineTo(decodeX(0.2F), decodeY(0.6F));
/* 358 */     this.path.lineTo(decodeX(0.2F), decodeY(2.6F));
/* 359 */     this.path.closePath();
/* 360 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 364 */     this.rect.setRect(decodeX(0.2F), decodeY(0.6F), decodeX(0.4F) - decodeX(0.2F), decodeY(0.8F) - decodeY(0.6F));
/*     */ 
/* 368 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 372 */     this.rect.setRect(decodeX(0.6F), decodeY(0.2F), decodeX(1.333333F) - decodeX(0.6F), decodeY(0.4F) - decodeY(0.2F));
/*     */ 
/* 376 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect4() {
/* 380 */     this.rect.setRect(decodeX(1.5F), decodeY(0.6F), decodeX(2.4F) - decodeX(1.5F), decodeY(0.8F) - decodeY(0.6F));
/*     */ 
/* 384 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath9() {
/* 388 */     this.path.reset();
/* 389 */     this.path.moveTo(decodeX(3.0F), decodeY(0.8F));
/* 390 */     this.path.lineTo(decodeX(3.0F), decodeY(1.0F));
/* 391 */     this.path.lineTo(decodeX(2.4F), decodeY(1.0F));
/* 392 */     this.path.lineTo(decodeX(2.4F), decodeY(0.6F));
/* 393 */     this.path.lineTo(decodeX(1.5F), decodeY(0.6F));
/* 394 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.4F));
/* 395 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.2F));
/* 396 */     this.path.lineTo(decodeX(0.5888889F), decodeY(0.2037037F));
/* 397 */     this.path.lineTo(decodeX(0.5962963F), decodeY(0.3481482F));
/* 398 */     this.path.lineTo(decodeX(0.3481482F), decodeY(0.6F));
/* 399 */     this.path.lineTo(decodeX(0.2F), decodeY(0.6F));
/* 400 */     this.path.lineTo(decodeX(0.2F), decodeY(2.6F));
/* 401 */     this.path.lineTo(decodeX(2.6F), decodeY(2.6F));
/* 402 */     this.path.lineTo(decodeX(2.6F), decodeY(1.333333F));
/* 403 */     this.path.lineTo(decodeX(2.774074F), decodeY(1.160494F));
/* 404 */     this.path.lineTo(decodeX(2.8F), decodeY(1.0F));
/* 405 */     this.path.lineTo(decodeX(3.0F), decodeY(1.0F));
/* 406 */     this.path.lineTo(decodeX(2.892593F), decodeY(1.188272F));
/* 407 */     this.path.lineTo(decodeX(2.8F), decodeY(1.333333F));
/* 408 */     this.path.lineTo(decodeX(2.8F), decodeY(2.6F));
/* 409 */     this.path.lineTo(decodeX(2.6F), decodeY(2.8F));
/* 410 */     this.path.lineTo(decodeX(0.2F), decodeY(2.8F));
/* 411 */     this.path.lineTo(decodeX(0.0F), decodeY(2.6F));
/* 412 */     this.path.lineTo(decodeX(0.0F), decodeY(0.6518518F));
/* 413 */     this.path.lineTo(decodeX(0.637037F), decodeY(0.0F));
/* 414 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.0F));
/* 415 */     this.path.lineTo(decodeX(1.592593F), decodeY(0.4F));
/* 416 */     this.path.lineTo(decodeX(2.4F), decodeY(0.4F));
/* 417 */     this.path.lineTo(decodeX(2.6F), decodeY(0.6F));
/* 418 */     this.path.lineTo(decodeX(2.6F), decodeY(0.8F));
/* 419 */     this.path.lineTo(decodeX(3.0F), decodeY(0.8F));
/* 420 */     this.path.closePath();
/* 421 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath10() {
/* 425 */     this.path.reset();
/* 426 */     this.path.moveTo(decodeX(2.4F), decodeY(1.0F));
/* 427 */     this.path.lineTo(decodeX(2.4F), decodeY(0.8F));
/* 428 */     this.path.lineTo(decodeX(0.7481481F), decodeY(0.8F));
/* 429 */     this.path.lineTo(decodeX(0.4037037F), decodeY(1.842593F));
/* 430 */     this.path.lineTo(decodeX(0.4F), decodeY(2.6F));
/* 431 */     this.path.lineTo(decodeX(0.6F), decodeY(2.6F));
/* 432 */     this.path.lineTo(decodeX(0.592593F), decodeY(2.225926F));
/* 433 */     this.path.lineTo(decodeX(0.916F), decodeY(0.996F));
/* 434 */     this.path.lineTo(decodeX(2.4F), decodeY(1.0F));
/* 435 */     this.path.closePath();
/* 436 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath11() {
/* 440 */     this.path.reset();
/* 441 */     this.path.moveTo(decodeX(2.2F), decodeY(2.2F));
/* 442 */     this.path.lineTo(decodeX(2.2F), decodeY(2.2F));
/* 443 */     this.path.closePath();
/* 444 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath12() {
/* 448 */     this.path.reset();
/* 449 */     this.path.moveTo(decodeX(0.6F), decodeY(2.6F));
/* 450 */     this.path.lineTo(decodeX(0.6F), decodeY(2.2F));
/* 451 */     this.path.lineTo(decodeX(0.8F), decodeY(1.333333F));
/* 452 */     this.path.lineTo(decodeX(2.8F), decodeY(1.333333F));
/* 453 */     this.path.lineTo(decodeX(2.8F), decodeY(1.666667F));
/* 454 */     this.path.lineTo(decodeX(2.6F), decodeY(2.6F));
/* 455 */     this.path.lineTo(decodeX(0.6F), decodeY(2.6F));
/* 456 */     this.path.closePath();
/* 457 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath13() {
/* 461 */     this.path.reset();
/* 462 */     this.path.moveTo(decodeX(0.2F), decodeY(2.6F));
/* 463 */     this.path.lineTo(decodeX(0.4F), decodeY(2.6F));
/* 464 */     this.path.lineTo(decodeX(0.4F), decodeY(2.0F));
/* 465 */     this.path.lineTo(decodeX(0.8F), decodeY(1.166667F));
/* 466 */     this.path.lineTo(decodeX(2.4F), decodeY(1.166667F));
/* 467 */     this.path.lineTo(decodeX(2.4F), decodeY(0.6F));
/* 468 */     this.path.lineTo(decodeX(1.5F), decodeY(0.6F));
/* 469 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.4F));
/* 470 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.2F));
/* 471 */     this.path.lineTo(decodeX(0.6F), decodeY(0.2F));
/* 472 */     this.path.lineTo(decodeX(0.6F), decodeY(0.4F));
/* 473 */     this.path.lineTo(decodeX(0.4F), decodeY(0.6F));
/* 474 */     this.path.lineTo(decodeX(0.2F), decodeY(0.6F));
/* 475 */     this.path.lineTo(decodeX(0.2F), decodeY(2.6F));
/* 476 */     this.path.closePath();
/* 477 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath14() {
/* 481 */     this.path.reset();
/* 482 */     this.path.moveTo(decodeX(3.0F), decodeY(1.166667F));
/* 483 */     this.path.lineTo(decodeX(3.0F), decodeY(1.333333F));
/* 484 */     this.path.lineTo(decodeX(2.4F), decodeY(1.333333F));
/* 485 */     this.path.lineTo(decodeX(2.4F), decodeY(0.6F));
/* 486 */     this.path.lineTo(decodeX(1.5F), decodeY(0.6F));
/* 487 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.4F));
/* 488 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.2F));
/* 489 */     this.path.lineTo(decodeX(0.5888889F), decodeY(0.2037037F));
/* 490 */     this.path.lineTo(decodeX(0.5962963F), decodeY(0.3481482F));
/* 491 */     this.path.lineTo(decodeX(0.3481482F), decodeY(0.6F));
/* 492 */     this.path.lineTo(decodeX(0.2F), decodeY(0.6F));
/* 493 */     this.path.lineTo(decodeX(0.2F), decodeY(2.6F));
/* 494 */     this.path.lineTo(decodeX(2.6F), decodeY(2.6F));
/* 495 */     this.path.lineTo(decodeX(2.6F), decodeY(2.0F));
/* 496 */     this.path.lineTo(decodeX(2.6F), decodeY(1.833333F));
/* 497 */     this.path.lineTo(decodeX(2.916F), decodeY(1.353333F));
/* 498 */     this.path.lineTo(decodeX(2.98F), decodeY(1.376667F));
/* 499 */     this.path.lineTo(decodeX(2.8F), decodeY(1.833333F));
/* 500 */     this.path.lineTo(decodeX(2.8F), decodeY(2.0F));
/* 501 */     this.path.lineTo(decodeX(2.8F), decodeY(2.6F));
/* 502 */     this.path.lineTo(decodeX(2.6F), decodeY(2.8F));
/* 503 */     this.path.lineTo(decodeX(0.2F), decodeY(2.8F));
/* 504 */     this.path.lineTo(decodeX(0.0F), decodeY(2.6F));
/* 505 */     this.path.lineTo(decodeX(0.0F), decodeY(0.6518518F));
/* 506 */     this.path.lineTo(decodeX(0.637037F), decodeY(0.0F));
/* 507 */     this.path.lineTo(decodeX(1.333333F), decodeY(0.0F));
/* 508 */     this.path.lineTo(decodeX(1.592593F), decodeY(0.4F));
/* 509 */     this.path.lineTo(decodeX(2.4F), decodeY(0.4F));
/* 510 */     this.path.lineTo(decodeX(2.6F), decodeY(0.6F));
/* 511 */     this.path.lineTo(decodeX(2.6F), decodeY(1.166667F));
/* 512 */     this.path.lineTo(decodeX(3.0F), decodeY(1.166667F));
/* 513 */     this.path.closePath();
/* 514 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath15() {
/* 518 */     this.path.reset();
/* 519 */     this.path.moveTo(decodeX(2.4F), decodeY(1.333333F));
/* 520 */     this.path.lineTo(decodeX(2.4F), decodeY(1.166667F));
/* 521 */     this.path.lineTo(decodeX(0.74F), decodeY(1.166667F));
/* 522 */     this.path.lineTo(decodeX(0.4F), decodeY(2.0F));
/* 523 */     this.path.lineTo(decodeX(0.4F), decodeY(2.6F));
/* 524 */     this.path.lineTo(decodeX(0.6F), decodeY(2.6F));
/* 525 */     this.path.lineTo(decodeX(0.592593F), decodeY(2.225926F));
/* 526 */     this.path.lineTo(decodeX(0.8F), decodeY(1.333333F));
/* 527 */     this.path.lineTo(decodeX(2.4F), decodeY(1.333333F));
/* 528 */     this.path.closePath();
/* 529 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath16() {
/* 533 */     this.path.reset();
/* 534 */     this.path.moveTo(decodeX(0.0F), decodeY(0.0F));
/* 535 */     this.path.lineTo(decodeX(1.239754F), decodeY(0.7016394F));
/* 536 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 537 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 538 */     this.path.closePath();
/* 539 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath17() {
/* 543 */     this.path.reset();
/* 544 */     this.path.moveTo(decodeX(0.0F), decodeY(0.0F));
/* 545 */     this.path.lineTo(decodeX(1.25F), decodeY(0.0F));
/* 546 */     this.path.lineTo(decodeX(0.7081968F), decodeY(2.990164F));
/* 547 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 548 */     this.path.closePath();
/* 549 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 555 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 556 */     float f1 = (float)localRectangle2D.getX();
/* 557 */     float f2 = (float)localRectangle2D.getY();
/* 558 */     float f3 = (float)localRectangle2D.getWidth();
/* 559 */     float f4 = (float)localRectangle2D.getHeight();
/* 560 */     return decodeGradient(0.0462963F * f3 + f1, 0.967593F * f4 + f2, 0.486111F * f3 + f1, 0.532407F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 568 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 569 */     float f1 = (float)localRectangle2D.getX();
/* 570 */     float f2 = (float)localRectangle2D.getY();
/* 571 */     float f3 = (float)localRectangle2D.getWidth();
/* 572 */     float f4 = (float)localRectangle2D.getHeight();
/* 573 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color5, decodeColor(this.color5, this.color6, 0.5F), this.color6 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 581 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 582 */     float f1 = (float)localRectangle2D.getX();
/* 583 */     float f2 = (float)localRectangle2D.getY();
/* 584 */     float f3 = (float)localRectangle2D.getWidth();
/* 585 */     float f4 = (float)localRectangle2D.getHeight();
/* 586 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.04191617F, 0.1032934F, 0.1646707F, 0.245509F, 0.3263473F, 0.6631737F, 1.0F }, new Color[] { this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11, decodeColor(this.color11, this.color12, 0.5F), this.color12, decodeColor(this.color12, this.color13, 0.5F), this.color13 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 598 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 599 */     float f1 = (float)localRectangle2D.getX();
/* 600 */     float f2 = (float)localRectangle2D.getY();
/* 601 */     float f3 = (float)localRectangle2D.getWidth();
/* 602 */     float f4 = (float)localRectangle2D.getHeight();
/* 603 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color5, decodeColor(this.color5, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 611 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 612 */     float f1 = (float)localRectangle2D.getX();
/* 613 */     float f2 = (float)localRectangle2D.getY();
/* 614 */     float f3 = (float)localRectangle2D.getWidth();
/* 615 */     float f4 = (float)localRectangle2D.getHeight();
/* 616 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color18, decodeColor(this.color18, this.color19, 0.5F), this.color19 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 624 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 625 */     float f1 = (float)localRectangle2D.getX();
/* 626 */     float f2 = (float)localRectangle2D.getY();
/* 627 */     float f3 = (float)localRectangle2D.getWidth();
/* 628 */     float f4 = (float)localRectangle2D.getHeight();
/* 629 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1272455F, 0.254491F, 0.6272456F, 1.0F }, new Color[] { this.color20, decodeColor(this.color20, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 639 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 640 */     float f1 = (float)localRectangle2D.getX();
/* 641 */     float f2 = (float)localRectangle2D.getY();
/* 642 */     float f3 = (float)localRectangle2D.getWidth();
/* 643 */     float f4 = (float)localRectangle2D.getHeight();
/* 644 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color24, decodeColor(this.color24, this.color25, 0.5F), this.color25 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TreePainter
 * JD-Core Version:    0.6.2
 */
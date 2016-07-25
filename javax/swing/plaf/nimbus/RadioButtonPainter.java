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
/*     */ final class RadioButtonPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int ICON_DISABLED = 3;
/*     */   static final int ICON_ENABLED = 4;
/*     */   static final int ICON_FOCUSED = 5;
/*     */   static final int ICON_MOUSEOVER = 6;
/*     */   static final int ICON_MOUSEOVER_FOCUSED = 7;
/*     */   static final int ICON_PRESSED = 8;
/*     */   static final int ICON_PRESSED_FOCUSED = 9;
/*     */   static final int ICON_SELECTED = 10;
/*     */   static final int ICON_SELECTED_FOCUSED = 11;
/*     */   static final int ICON_PRESSED_SELECTED = 12;
/*     */   static final int ICON_PRESSED_SELECTED_FOCUSED = 13;
/*     */   static final int ICON_MOUSEOVER_SELECTED = 14;
/*     */   static final int ICON_MOUSEOVER_SELECTED_FOCUSED = 15;
/*     */   static final int ICON_DISABLED_SELECTED = 16;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  61 */   private Path2D path = new Path2D.Float();
/*  62 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  63 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  64 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  69 */   private Color color1 = decodeColor("nimbusBlueGrey", 0.0F, -0.06766917F, 0.07843137F, 0);
/*  70 */   private Color color2 = decodeColor("nimbusBlueGrey", 0.0F, -0.06413457F, 0.01568627F, 0);
/*  71 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.0F, -0.08466425F, 0.1647059F, 0);
/*  72 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.0F, -0.07016757F, 0.1294118F, 0);
/*  73 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.0F, -0.07070331F, 0.1411765F, 0);
/*  74 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.0F, -0.07052632F, 0.1372549F, 0);
/*  75 */   private Color color7 = decodeColor("nimbusBlueGrey", 0.0F, 0.0F, 0.0F, -112);
/*  76 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.0F, -0.05320147F, -0.1294118F, 0);
/*  77 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.0F, 0.006356798F, -0.4431373F, 0);
/*  78 */   private Color color10 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.1065423F, 0.2392157F, 0);
/*  79 */   private Color color11 = decodeColor("nimbusBlueGrey", 0.0F, -0.07206477F, 0.172549F, 0);
/*  80 */   private Color color12 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  81 */   private Color color13 = decodeColor("nimbusBlueGrey", -0.0050505F, -0.02781955F, -0.223529F, 0);
/*  82 */   private Color color14 = decodeColor("nimbusBlueGrey", 0.0F, 0.2424149F, -0.6117647F, 0);
/*  83 */   private Color color15 = decodeColor("nimbusBlueGrey", -0.111111F, -0.1065581F, 0.2431372F, 0);
/*  84 */   private Color color16 = decodeColor("nimbusBlueGrey", 0.0F, -0.07333623F, 0.2039216F, 0);
/*  85 */   private Color color17 = decodeColor("nimbusBlueGrey", 0.08585858F, -0.06738906F, 0.254902F, 0);
/*  86 */   private Color color18 = decodeColor("nimbusBlueGrey", -0.111111F, -0.106289F, 0.1803922F, 0);
/*  87 */   private Color color19 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  88 */   private Color color20 = decodeColor("nimbusBlueGrey", 0.05555558F, 0.2394737F, -0.6666667F, 0);
/*  89 */   private Color color21 = decodeColor("nimbusBlueGrey", -0.0777778F, -0.06815343F, -0.282353F, 0);
/*  90 */   private Color color22 = decodeColor("nimbusBlueGrey", 0.0F, -0.06866585F, 0.0980392F, 0);
/*  91 */   private Color color23 = decodeColor("nimbusBlueGrey", -0.002777755F, -0.001830667F, -0.0235294F, 0);
/*  92 */   private Color color24 = decodeColor("nimbusBlueGrey", 0.002924025F, -0.02047892F, 0.08235294F, 0);
/*  93 */   private Color color25 = decodeColor("nimbusBase", 0.0002956986F, -0.360352F, -0.007843137F, 0);
/*  94 */   private Color color26 = decodeColor("nimbusBase", 0.0002956986F, 0.01945812F, -0.3215687F, 0);
/*  95 */   private Color color27 = decodeColor("nimbusBase", 0.004681647F, -0.6195853F, 0.4235294F, 0);
/*  96 */   private Color color28 = decodeColor("nimbusBase", 0.004681647F, -0.5670447F, 0.3647059F, 0);
/*  97 */   private Color color29 = decodeColor("nimbusBase", 0.0005149841F, -0.43867F, 0.2470588F, 0);
/*  98 */   private Color color30 = decodeColor("nimbusBase", 0.0005149841F, -0.4487984F, 0.2901961F, 0);
/*  99 */   private Color color31 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.07243107F, -0.3333333F, 0);
/* 100 */   private Color color32 = decodeColor("nimbusBlueGrey", -0.611111F, -0.1105263F, -0.7450981F, 0);
/* 101 */   private Color color33 = decodeColor("nimbusBlueGrey", -0.02777779F, 0.07129187F, -0.615686F, 0);
/* 102 */   private Color color34 = decodeColor("nimbusBase", -0.5786517F, -0.6357143F, -0.4980393F, 0);
/* 103 */   private Color color35 = decodeColor("nimbusBase", 0.003047705F, -0.125714F, -0.1568628F, 0);
/* 104 */   private Color color36 = decodeColor("nimbusBase", -0.001728594F, -0.4367347F, 0.2196078F, 0);
/* 105 */   private Color color37 = decodeColor("nimbusBase", -0.001065493F, -0.3134921F, 0.1568627F, 0);
/* 106 */   private Color color38 = decodeColor("nimbusBase", 0.0F, 0.0F, 0.0F, 0);
/* 107 */   private Color color39 = decodeColor("nimbusBase", 0.000805676F, -0.1238095F, 0.1098039F, 0);
/* 108 */   private Color color40 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.08022329F, -0.4862745F, 0);
/* 109 */   private Color color41 = decodeColor("nimbusBase", -0.0006374717F, -0.2045216F, -0.1215687F, 0);
/* 110 */   private Color color42 = decodeColor("nimbusBase", -0.5786517F, -0.6357143F, -0.5058824F, 0);
/* 111 */   private Color color43 = decodeColor("nimbusBase", -0.011985F, -0.6157143F, 0.4313725F, 0);
/* 112 */   private Color color44 = decodeColor("nimbusBase", 0.004681647F, -0.5693243F, 0.3960784F, 0);
/* 113 */   private Color color45 = decodeColor("nimbusBase", 0.0005149841F, -0.455534F, 0.3215686F, 0);
/* 114 */   private Color color46 = decodeColor("nimbusBase", 0.0005149841F, -0.4655016F, 0.372549F, 0);
/* 115 */   private Color color47 = decodeColor("nimbusBase", 0.002429426F, -0.4727187F, 0.3411765F, 0);
/* 116 */   private Color color48 = decodeColor("nimbusBase", 0.01023722F, -0.5628988F, 0.2588235F, 0);
/* 117 */   private Color color49 = decodeColor("nimbusBase", 0.01658648F, -0.5620301F, 0.1960784F, 0);
/* 118 */   private Color color50 = decodeColor("nimbusBase", 0.0274089F, -0.5878882F, 0.3529412F, 0);
/* 119 */   private Color color51 = decodeColor("nimbusBase", 0.0213483F, -0.5672212F, 0.309804F, 0);
/* 120 */   private Color color52 = decodeColor("nimbusBase", 0.0213483F, -0.567841F, 0.317647F, 0);
/* 121 */   private Color color53 = decodeColor("nimbusBlueGrey", -0.0111111F, -0.0581703F, 0.003921568F, 0);
/* 122 */   private Color color54 = decodeColor("nimbusBlueGrey", -0.01388884F, -0.0419549F, -0.05882353F, 0);
/* 123 */   private Color color55 = decodeColor("nimbusBlueGrey", 0.009259284F, -0.01478163F, -0.007843137F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public RadioButtonPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/* 131 */     this.state = paramInt;
/* 132 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 138 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 141 */     switch (this.state) { case 3:
/* 142 */       painticonDisabled(paramGraphics2D); break;
/*     */     case 4:
/* 143 */       painticonEnabled(paramGraphics2D); break;
/*     */     case 5:
/* 144 */       painticonFocused(paramGraphics2D); break;
/*     */     case 6:
/* 145 */       painticonMouseOver(paramGraphics2D); break;
/*     */     case 7:
/* 146 */       painticonMouseOverAndFocused(paramGraphics2D); break;
/*     */     case 8:
/* 147 */       painticonPressed(paramGraphics2D); break;
/*     */     case 9:
/* 148 */       painticonPressedAndFocused(paramGraphics2D); break;
/*     */     case 10:
/* 149 */       painticonSelected(paramGraphics2D); break;
/*     */     case 11:
/* 150 */       painticonSelectedAndFocused(paramGraphics2D); break;
/*     */     case 12:
/* 151 */       painticonPressedAndSelected(paramGraphics2D); break;
/*     */     case 13:
/* 152 */       painticonPressedAndSelectedAndFocused(paramGraphics2D); break;
/*     */     case 14:
/* 153 */       painticonMouseOverAndSelected(paramGraphics2D); break;
/*     */     case 15:
/* 154 */       painticonMouseOverAndSelectedAndFocused(paramGraphics2D); break;
/*     */     case 16:
/* 155 */       painticonDisabledAndSelected(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 164 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void painticonDisabled(Graphics2D paramGraphics2D) {
/* 168 */     this.ellipse = decodeEllipse1();
/* 169 */     paramGraphics2D.setPaint(decodeGradient1(this.ellipse));
/* 170 */     paramGraphics2D.fill(this.ellipse);
/* 171 */     this.ellipse = decodeEllipse2();
/* 172 */     paramGraphics2D.setPaint(decodeGradient2(this.ellipse));
/* 173 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 178 */     this.ellipse = decodeEllipse3();
/* 179 */     paramGraphics2D.setPaint(this.color7);
/* 180 */     paramGraphics2D.fill(this.ellipse);
/* 181 */     this.ellipse = decodeEllipse1();
/* 182 */     paramGraphics2D.setPaint(decodeGradient3(this.ellipse));
/* 183 */     paramGraphics2D.fill(this.ellipse);
/* 184 */     this.ellipse = decodeEllipse2();
/* 185 */     paramGraphics2D.setPaint(decodeGradient4(this.ellipse));
/* 186 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 191 */     this.ellipse = decodeEllipse4();
/* 192 */     paramGraphics2D.setPaint(this.color12);
/* 193 */     paramGraphics2D.fill(this.ellipse);
/* 194 */     this.ellipse = decodeEllipse1();
/* 195 */     paramGraphics2D.setPaint(decodeGradient3(this.ellipse));
/* 196 */     paramGraphics2D.fill(this.ellipse);
/* 197 */     this.ellipse = decodeEllipse2();
/* 198 */     paramGraphics2D.setPaint(decodeGradient4(this.ellipse));
/* 199 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 204 */     this.ellipse = decodeEllipse3();
/* 205 */     paramGraphics2D.setPaint(this.color7);
/* 206 */     paramGraphics2D.fill(this.ellipse);
/* 207 */     this.ellipse = decodeEllipse1();
/* 208 */     paramGraphics2D.setPaint(decodeGradient5(this.ellipse));
/* 209 */     paramGraphics2D.fill(this.ellipse);
/* 210 */     this.ellipse = decodeEllipse2();
/* 211 */     paramGraphics2D.setPaint(decodeGradient6(this.ellipse));
/* 212 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonMouseOverAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 217 */     this.ellipse = decodeEllipse4();
/* 218 */     paramGraphics2D.setPaint(this.color12);
/* 219 */     paramGraphics2D.fill(this.ellipse);
/* 220 */     this.ellipse = decodeEllipse1();
/* 221 */     paramGraphics2D.setPaint(decodeGradient5(this.ellipse));
/* 222 */     paramGraphics2D.fill(this.ellipse);
/* 223 */     this.ellipse = decodeEllipse2();
/* 224 */     paramGraphics2D.setPaint(decodeGradient6(this.ellipse));
/* 225 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 230 */     this.ellipse = decodeEllipse3();
/* 231 */     paramGraphics2D.setPaint(this.color19);
/* 232 */     paramGraphics2D.fill(this.ellipse);
/* 233 */     this.ellipse = decodeEllipse1();
/* 234 */     paramGraphics2D.setPaint(decodeGradient7(this.ellipse));
/* 235 */     paramGraphics2D.fill(this.ellipse);
/* 236 */     this.ellipse = decodeEllipse2();
/* 237 */     paramGraphics2D.setPaint(decodeGradient8(this.ellipse));
/* 238 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonPressedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 243 */     this.ellipse = decodeEllipse4();
/* 244 */     paramGraphics2D.setPaint(this.color12);
/* 245 */     paramGraphics2D.fill(this.ellipse);
/* 246 */     this.ellipse = decodeEllipse1();
/* 247 */     paramGraphics2D.setPaint(decodeGradient7(this.ellipse));
/* 248 */     paramGraphics2D.fill(this.ellipse);
/* 249 */     this.ellipse = decodeEllipse2();
/* 250 */     paramGraphics2D.setPaint(decodeGradient8(this.ellipse));
/* 251 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 256 */     this.ellipse = decodeEllipse3();
/* 257 */     paramGraphics2D.setPaint(this.color7);
/* 258 */     paramGraphics2D.fill(this.ellipse);
/* 259 */     this.ellipse = decodeEllipse1();
/* 260 */     paramGraphics2D.setPaint(decodeGradient9(this.ellipse));
/* 261 */     paramGraphics2D.fill(this.ellipse);
/* 262 */     this.ellipse = decodeEllipse2();
/* 263 */     paramGraphics2D.setPaint(decodeGradient10(this.ellipse));
/* 264 */     paramGraphics2D.fill(this.ellipse);
/* 265 */     this.ellipse = decodeEllipse5();
/* 266 */     paramGraphics2D.setPaint(decodeGradient11(this.ellipse));
/* 267 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 272 */     this.ellipse = decodeEllipse4();
/* 273 */     paramGraphics2D.setPaint(this.color12);
/* 274 */     paramGraphics2D.fill(this.ellipse);
/* 275 */     this.ellipse = decodeEllipse1();
/* 276 */     paramGraphics2D.setPaint(decodeGradient9(this.ellipse));
/* 277 */     paramGraphics2D.fill(this.ellipse);
/* 278 */     this.ellipse = decodeEllipse2();
/* 279 */     paramGraphics2D.setPaint(decodeGradient10(this.ellipse));
/* 280 */     paramGraphics2D.fill(this.ellipse);
/* 281 */     this.ellipse = decodeEllipse5();
/* 282 */     paramGraphics2D.setPaint(decodeGradient11(this.ellipse));
/* 283 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonPressedAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 288 */     this.ellipse = decodeEllipse3();
/* 289 */     paramGraphics2D.setPaint(this.color19);
/* 290 */     paramGraphics2D.fill(this.ellipse);
/* 291 */     this.ellipse = decodeEllipse1();
/* 292 */     paramGraphics2D.setPaint(decodeGradient12(this.ellipse));
/* 293 */     paramGraphics2D.fill(this.ellipse);
/* 294 */     this.ellipse = decodeEllipse2();
/* 295 */     paramGraphics2D.setPaint(decodeGradient13(this.ellipse));
/* 296 */     paramGraphics2D.fill(this.ellipse);
/* 297 */     this.ellipse = decodeEllipse5();
/* 298 */     paramGraphics2D.setPaint(decodeGradient14(this.ellipse));
/* 299 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonPressedAndSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 304 */     this.ellipse = decodeEllipse4();
/* 305 */     paramGraphics2D.setPaint(this.color12);
/* 306 */     paramGraphics2D.fill(this.ellipse);
/* 307 */     this.ellipse = decodeEllipse1();
/* 308 */     paramGraphics2D.setPaint(decodeGradient12(this.ellipse));
/* 309 */     paramGraphics2D.fill(this.ellipse);
/* 310 */     this.ellipse = decodeEllipse2();
/* 311 */     paramGraphics2D.setPaint(decodeGradient13(this.ellipse));
/* 312 */     paramGraphics2D.fill(this.ellipse);
/* 313 */     this.ellipse = decodeEllipse5();
/* 314 */     paramGraphics2D.setPaint(decodeGradient14(this.ellipse));
/* 315 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonMouseOverAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 320 */     this.ellipse = decodeEllipse3();
/* 321 */     paramGraphics2D.setPaint(this.color7);
/* 322 */     paramGraphics2D.fill(this.ellipse);
/* 323 */     this.ellipse = decodeEllipse1();
/* 324 */     paramGraphics2D.setPaint(decodeGradient15(this.ellipse));
/* 325 */     paramGraphics2D.fill(this.ellipse);
/* 326 */     this.ellipse = decodeEllipse2();
/* 327 */     paramGraphics2D.setPaint(decodeGradient16(this.ellipse));
/* 328 */     paramGraphics2D.fill(this.ellipse);
/* 329 */     this.ellipse = decodeEllipse5();
/* 330 */     paramGraphics2D.setPaint(decodeGradient11(this.ellipse));
/* 331 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonMouseOverAndSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 336 */     this.ellipse = decodeEllipse4();
/* 337 */     paramGraphics2D.setPaint(this.color12);
/* 338 */     paramGraphics2D.fill(this.ellipse);
/* 339 */     this.ellipse = decodeEllipse1();
/* 340 */     paramGraphics2D.setPaint(decodeGradient15(this.ellipse));
/* 341 */     paramGraphics2D.fill(this.ellipse);
/* 342 */     this.ellipse = decodeEllipse2();
/* 343 */     paramGraphics2D.setPaint(decodeGradient16(this.ellipse));
/* 344 */     paramGraphics2D.fill(this.ellipse);
/* 345 */     this.ellipse = decodeEllipse5();
/* 346 */     paramGraphics2D.setPaint(decodeGradient11(this.ellipse));
/* 347 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private void painticonDisabledAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 352 */     this.ellipse = decodeEllipse1();
/* 353 */     paramGraphics2D.setPaint(decodeGradient17(this.ellipse));
/* 354 */     paramGraphics2D.fill(this.ellipse);
/* 355 */     this.ellipse = decodeEllipse2();
/* 356 */     paramGraphics2D.setPaint(decodeGradient18(this.ellipse));
/* 357 */     paramGraphics2D.fill(this.ellipse);
/* 358 */     this.ellipse = decodeEllipse5();
/* 359 */     paramGraphics2D.setPaint(decodeGradient19(this.ellipse));
/* 360 */     paramGraphics2D.fill(this.ellipse);
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse1()
/*     */   {
/* 367 */     this.ellipse.setFrame(decodeX(0.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.4F));
/*     */ 
/* 371 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse2() {
/* 375 */     this.ellipse.setFrame(decodeX(0.6F), decodeY(0.6F), decodeX(2.4F) - decodeX(0.6F), decodeY(2.4F) - decodeY(0.6F));
/*     */ 
/* 379 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse3() {
/* 383 */     this.ellipse.setFrame(decodeX(0.4F), decodeY(0.6F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.8F) - decodeY(0.6F));
/*     */ 
/* 387 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse4() {
/* 391 */     this.ellipse.setFrame(decodeX(0.12F), decodeY(0.12F), decodeX(2.88F) - decodeX(0.12F), decodeY(2.88F) - decodeY(0.12F));
/*     */ 
/* 395 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Ellipse2D decodeEllipse5() {
/* 399 */     this.ellipse.setFrame(decodeX(1.125F), decodeY(1.125F), decodeX(1.875F) - decodeX(1.125F), decodeY(1.875F) - decodeY(1.125F));
/*     */ 
/* 403 */     return this.ellipse;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 409 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 410 */     float f1 = (float)localRectangle2D.getX();
/* 411 */     float f2 = (float)localRectangle2D.getY();
/* 412 */     float f3 = (float)localRectangle2D.getWidth();
/* 413 */     float f4 = (float)localRectangle2D.getHeight();
/* 414 */     return decodeGradient(0.4978991F * f3 + f1, -0.004201681F * f4 + f2, 0.5F * f3 + f1, 0.9978992F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 422 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 423 */     float f1 = (float)localRectangle2D.getX();
/* 424 */     float f2 = (float)localRectangle2D.getY();
/* 425 */     float f3 = (float)localRectangle2D.getWidth();
/* 426 */     float f4 = (float)localRectangle2D.getHeight();
/* 427 */     return decodeGradient(0.497549F * f3 + f1, 0.00490196F * f4 + f2, 0.507353F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.06344411F, 0.2160121F, 0.3685801F, 0.5483384F, 0.7280967F, 0.7749245F, 0.8217523F, 0.9108762F, 1.0F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5, decodeColor(this.color5, this.color6, 0.5F), this.color6 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 441 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 442 */     float f1 = (float)localRectangle2D.getX();
/* 443 */     float f2 = (float)localRectangle2D.getY();
/* 444 */     float f3 = (float)localRectangle2D.getWidth();
/* 445 */     float f4 = (float)localRectangle2D.getHeight();
/* 446 */     return decodeGradient(0.4978991F * f3 + f1, -0.004201681F * f4 + f2, 0.5F * f3 + f1, 0.9978992F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 454 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 455 */     float f1 = (float)localRectangle2D.getX();
/* 456 */     float f2 = (float)localRectangle2D.getY();
/* 457 */     float f3 = (float)localRectangle2D.getWidth();
/* 458 */     float f4 = (float)localRectangle2D.getHeight();
/* 459 */     return decodeGradient(0.497549F * f3 + f1, 0.00490196F * f4 + f2, 0.507353F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.06344411F, 0.2500956F, 0.436747F, 0.4804217F, 0.5240964F, 0.7048193F, 0.8855422F }, new Color[] { this.color10, decodeColor(this.color10, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 471 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 472 */     float f1 = (float)localRectangle2D.getX();
/* 473 */     float f2 = (float)localRectangle2D.getY();
/* 474 */     float f3 = (float)localRectangle2D.getWidth();
/* 475 */     float f4 = (float)localRectangle2D.getHeight();
/* 476 */     return decodeGradient(0.4978991F * f3 + f1, -0.004201681F * f4 + f2, 0.5F * f3 + f1, 0.9978992F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 484 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 485 */     float f1 = (float)localRectangle2D.getX();
/* 486 */     float f2 = (float)localRectangle2D.getY();
/* 487 */     float f3 = (float)localRectangle2D.getWidth();
/* 488 */     float f4 = (float)localRectangle2D.getHeight();
/* 489 */     return decodeGradient(0.497549F * f3 + f1, 0.00490196F * f4 + f2, 0.507353F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.06344411F, 0.2160121F, 0.3685801F, 0.5483384F, 0.7280967F, 0.7749245F, 0.8217523F, 0.9108762F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 503 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 504 */     float f1 = (float)localRectangle2D.getX();
/* 505 */     float f2 = (float)localRectangle2D.getY();
/* 506 */     float f3 = (float)localRectangle2D.getWidth();
/* 507 */     float f4 = (float)localRectangle2D.getHeight();
/* 508 */     return decodeGradient(0.4978991F * f3 + f1, -0.004201681F * f4 + f2, 0.5F * f3 + f1, 0.9978992F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color20, decodeColor(this.color20, this.color21, 0.5F), this.color21 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 516 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 517 */     float f1 = (float)localRectangle2D.getX();
/* 518 */     float f2 = (float)localRectangle2D.getY();
/* 519 */     float f3 = (float)localRectangle2D.getWidth();
/* 520 */     float f4 = (float)localRectangle2D.getHeight();
/* 521 */     return decodeGradient(0.497549F * f3 + f1, 0.00490196F * f4 + f2, 0.507353F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.06344411F, 0.2079269F, 0.3524096F, 0.4503012F, 0.548193F, 0.748494F, 0.948795F }, new Color[] { this.color22, decodeColor(this.color22, this.color23, 0.5F), this.color23, decodeColor(this.color23, this.color23, 0.5F), this.color23, decodeColor(this.color23, this.color24, 0.5F), this.color24 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient9(Shape paramShape)
/*     */   {
/* 533 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 534 */     float f1 = (float)localRectangle2D.getX();
/* 535 */     float f2 = (float)localRectangle2D.getY();
/* 536 */     float f3 = (float)localRectangle2D.getWidth();
/* 537 */     float f4 = (float)localRectangle2D.getHeight();
/* 538 */     return decodeGradient(0.4978991F * f3 + f1, -0.004201681F * f4 + f2, 0.5F * f3 + f1, 0.9978992F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color25, decodeColor(this.color25, this.color26, 0.5F), this.color26 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient10(Shape paramShape)
/*     */   {
/* 546 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 547 */     float f1 = (float)localRectangle2D.getX();
/* 548 */     float f2 = (float)localRectangle2D.getY();
/* 549 */     float f3 = (float)localRectangle2D.getWidth();
/* 550 */     float f4 = (float)localRectangle2D.getHeight();
/* 551 */     return decodeGradient(0.497549F * f3 + f1, 0.00490196F * f4 + f2, 0.507353F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.081325F, 0.1009036F, 0.1204819F, 0.2891566F, 0.4578313F, 0.6159638F, 0.7740964F, 0.8298193F, 0.8855422F }, new Color[] { this.color27, decodeColor(this.color27, this.color28, 0.5F), this.color28, decodeColor(this.color28, this.color29, 0.5F), this.color29, decodeColor(this.color29, this.color29, 0.5F), this.color29, decodeColor(this.color29, this.color30, 0.5F), this.color30 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient11(Shape paramShape)
/*     */   {
/* 565 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 566 */     float f1 = (float)localRectangle2D.getX();
/* 567 */     float f2 = (float)localRectangle2D.getY();
/* 568 */     float f3 = (float)localRectangle2D.getWidth();
/* 569 */     float f4 = (float)localRectangle2D.getHeight();
/* 570 */     return decodeGradient(0.504902F * f3 + f1, 0.0F * f4 + f2, 0.495098F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2319277F, 0.4638554F, 0.7319278F, 1.0F }, new Color[] { this.color31, decodeColor(this.color31, this.color32, 0.5F), this.color32, decodeColor(this.color32, this.color33, 0.5F), this.color33 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient12(Shape paramShape)
/*     */   {
/* 580 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 581 */     float f1 = (float)localRectangle2D.getX();
/* 582 */     float f2 = (float)localRectangle2D.getY();
/* 583 */     float f3 = (float)localRectangle2D.getWidth();
/* 584 */     float f4 = (float)localRectangle2D.getHeight();
/* 585 */     return decodeGradient(0.4978991F * f3 + f1, -0.004201681F * f4 + f2, 0.5F * f3 + f1, 0.9978992F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color34, decodeColor(this.color34, this.color26, 0.5F), this.color26 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient13(Shape paramShape)
/*     */   {
/* 593 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 594 */     float f1 = (float)localRectangle2D.getX();
/* 595 */     float f2 = (float)localRectangle2D.getY();
/* 596 */     float f3 = (float)localRectangle2D.getWidth();
/* 597 */     float f4 = (float)localRectangle2D.getHeight();
/* 598 */     return decodeGradient(0.497549F * f3 + f1, 0.00490196F * f4 + f2, 0.507353F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.03915663F, 0.07831325F, 0.1174699F, 0.2876506F, 0.4578313F, 0.561747F, 0.6656627F, 0.775602F, 0.8855422F }, new Color[] { this.color36, decodeColor(this.color36, this.color37, 0.5F), this.color37, decodeColor(this.color37, this.color38, 0.5F), this.color38, decodeColor(this.color38, this.color38, 0.5F), this.color38, decodeColor(this.color38, this.color39, 0.5F), this.color39 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient14(Shape paramShape)
/*     */   {
/* 612 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 613 */     float f1 = (float)localRectangle2D.getX();
/* 614 */     float f2 = (float)localRectangle2D.getY();
/* 615 */     float f3 = (float)localRectangle2D.getWidth();
/* 616 */     float f4 = (float)localRectangle2D.getHeight();
/* 617 */     return decodeGradient(0.504902F * f3 + f1, 0.0F * f4 + f2, 0.495098F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2319277F, 0.4638554F, 0.7319278F, 1.0F }, new Color[] { this.color40, decodeColor(this.color40, this.color32, 0.5F), this.color32, decodeColor(this.color32, this.color33, 0.5F), this.color33 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient15(Shape paramShape)
/*     */   {
/* 627 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 628 */     float f1 = (float)localRectangle2D.getX();
/* 629 */     float f2 = (float)localRectangle2D.getY();
/* 630 */     float f3 = (float)localRectangle2D.getWidth();
/* 631 */     float f4 = (float)localRectangle2D.getHeight();
/* 632 */     return decodeGradient(0.4978991F * f3 + f1, -0.004201681F * f4 + f2, 0.5F * f3 + f1, 0.9978992F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color41, decodeColor(this.color41, this.color42, 0.5F), this.color42 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient16(Shape paramShape)
/*     */   {
/* 640 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 641 */     float f1 = (float)localRectangle2D.getX();
/* 642 */     float f2 = (float)localRectangle2D.getY();
/* 643 */     float f3 = (float)localRectangle2D.getWidth();
/* 644 */     float f4 = (float)localRectangle2D.getHeight();
/* 645 */     return decodeGradient(0.497549F * f3 + f1, 0.00490196F * f4 + f2, 0.507353F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.081325F, 0.1009036F, 0.1204819F, 0.2018072F, 0.2831325F, 0.4924699F, 0.701807F, 0.7560241F, 0.810241F, 0.8478916F, 0.8855422F }, new Color[] { this.color43, decodeColor(this.color43, this.color44, 0.5F), this.color44, decodeColor(this.color44, this.color45, 0.5F), this.color45, decodeColor(this.color45, this.color45, 0.5F), this.color45, decodeColor(this.color45, this.color46, 0.5F), this.color46, decodeColor(this.color46, this.color47, 0.5F), this.color47 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient17(Shape paramShape)
/*     */   {
/* 661 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 662 */     float f1 = (float)localRectangle2D.getX();
/* 663 */     float f2 = (float)localRectangle2D.getY();
/* 664 */     float f3 = (float)localRectangle2D.getWidth();
/* 665 */     float f4 = (float)localRectangle2D.getHeight();
/* 666 */     return decodeGradient(0.4978991F * f3 + f1, -0.004201681F * f4 + f2, 0.5F * f3 + f1, 0.9978992F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color48, decodeColor(this.color48, this.color49, 0.5F), this.color49 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient18(Shape paramShape)
/*     */   {
/* 674 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 675 */     float f1 = (float)localRectangle2D.getX();
/* 676 */     float f2 = (float)localRectangle2D.getY();
/* 677 */     float f3 = (float)localRectangle2D.getWidth();
/* 678 */     float f4 = (float)localRectangle2D.getHeight();
/* 679 */     return decodeGradient(0.497549F * f3 + f1, 0.00490196F * f4 + f2, 0.507353F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.081325F, 0.2695783F, 0.4578313F, 0.6716868F, 0.8855422F }, new Color[] { this.color50, decodeColor(this.color50, this.color51, 0.5F), this.color51, decodeColor(this.color51, this.color52, 0.5F), this.color52 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient19(Shape paramShape)
/*     */   {
/* 689 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 690 */     float f1 = (float)localRectangle2D.getX();
/* 691 */     float f2 = (float)localRectangle2D.getY();
/* 692 */     float f3 = (float)localRectangle2D.getWidth();
/* 693 */     float f4 = (float)localRectangle2D.getHeight();
/* 694 */     return decodeGradient(0.504902F * f3 + f1, 0.0F * f4 + f2, 0.495098F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.2319277F, 0.4638554F, 0.7319278F, 1.0F }, new Color[] { this.color53, decodeColor(this.color53, this.color54, 0.5F), this.color54, decodeColor(this.color54, this.color55, 0.5F), this.color55 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.RadioButtonPainter
 * JD-Core Version:    0.6.2
 */
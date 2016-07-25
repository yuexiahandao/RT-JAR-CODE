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
/*     */ final class ToolBarToggleButtonPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int BACKGROUND_FOCUSED = 2;
/*     */   static final int BACKGROUND_MOUSEOVER = 3;
/*     */   static final int BACKGROUND_MOUSEOVER_FOCUSED = 4;
/*     */   static final int BACKGROUND_PRESSED = 5;
/*     */   static final int BACKGROUND_PRESSED_FOCUSED = 6;
/*     */   static final int BACKGROUND_SELECTED = 7;
/*     */   static final int BACKGROUND_SELECTED_FOCUSED = 8;
/*     */   static final int BACKGROUND_PRESSED_SELECTED = 9;
/*     */   static final int BACKGROUND_PRESSED_SELECTED_FOCUSED = 10;
/*     */   static final int BACKGROUND_MOUSEOVER_SELECTED = 11;
/*     */   static final int BACKGROUND_MOUSEOVER_SELECTED_FOCUSED = 12;
/*     */   static final int BACKGROUND_DISABLED_SELECTED = 13;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  58 */   private Path2D path = new Path2D.Float();
/*  59 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  60 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  61 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  66 */   private Color color1 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  67 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.06885965F, -0.3686275F, -153);
/*  68 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.0F, -0.02097408F, -0.2196078F, 0);
/*  69 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.0F, 0.1116959F, -0.5333334F, 0);
/*  70 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.1065893F, 0.2509804F, 0);
/*  71 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.0F, -0.09852631F, 0.235294F, 0);
/*  72 */   private Color color7 = decodeColor("nimbusBlueGrey", 0.0F, -0.07333623F, 0.2039216F, 0);
/*  73 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  74 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -86);
/*  75 */   private Color color10 = decodeColor("nimbusBlueGrey", -0.0111111F, -0.06052632F, -0.3529412F, 0);
/*  76 */   private Color color11 = decodeColor("nimbusBlueGrey", 0.0F, -0.06437247F, -0.235294F, 0);
/*  77 */   private Color color12 = decodeColor("nimbusBlueGrey", -0.00694442F, -0.0595709F, -0.1294118F, 0);
/*  78 */   private Color color13 = decodeColor("nimbusBlueGrey", 0.0F, -0.06107577F, -0.03137255F, 0);
/*  79 */   private Color color14 = decodeColor("nimbusBlueGrey", 0.0F, -0.0608026F, -0.03529412F, 0);
/*  80 */   private Color color15 = decodeColor("nimbusBlueGrey", 0.0F, -0.06472479F, -0.2313725F, 0);
/*  81 */   private Color color16 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.06959064F, -0.0745098F, 0);
/*  82 */   private Color color17 = decodeColor("nimbusBlueGrey", 0.0138889F, -0.06401469F, -0.07058823F, 0);
/*  83 */   private Color color18 = decodeColor("nimbusBlueGrey", 0.0F, -0.06530018F, 0.03529412F, 0);
/*  84 */   private Color color19 = decodeColor("nimbusBlueGrey", 0.0F, -0.06507177F, 0.03137255F, 0);
/*  85 */   private Color color20 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.05338346F, -0.4705882F, 0);
/*  86 */   private Color color21 = decodeColor("nimbusBlueGrey", 0.0F, -0.04930183F, -0.3607843F, 0);
/*  87 */   private Color color22 = decodeColor("nimbusBlueGrey", -0.01851857F, -0.03909774F, -0.2509804F, 0);
/*  88 */   private Color color23 = decodeColor("nimbusBlueGrey", -0.0050505F, -0.04001349F, -0.1333333F, 0);
/*  89 */   private Color color24 = decodeColor("nimbusBlueGrey", 0.0101011F, -0.03955858F, -0.1372549F, 0);
/*  90 */   private Color color25 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -220);
/*  91 */   private Color color26 = decodeColor("nimbusBlueGrey", 0.0F, -0.06640866F, 0.05490196F, 0);
/*  92 */   private Color color27 = decodeColor("nimbusBlueGrey", 0.0F, -0.06807348F, 0.08627451F, 0);
/*  93 */   private Color color28 = decodeColor("nimbusBlueGrey", 0.0F, -0.06924191F, 0.1098039F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ToolBarToggleButtonPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/* 101 */     this.state = paramInt;
/* 102 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 108 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 111 */     switch (this.state) { case 2:
/* 112 */       paintBackgroundFocused(paramGraphics2D); break;
/*     */     case 3:
/* 113 */       paintBackgroundMouseOver(paramGraphics2D); break;
/*     */     case 4:
/* 114 */       paintBackgroundMouseOverAndFocused(paramGraphics2D); break;
/*     */     case 5:
/* 115 */       paintBackgroundPressed(paramGraphics2D); break;
/*     */     case 6:
/* 116 */       paintBackgroundPressedAndFocused(paramGraphics2D); break;
/*     */     case 7:
/* 117 */       paintBackgroundSelected(paramGraphics2D); break;
/*     */     case 8:
/* 118 */       paintBackgroundSelectedAndFocused(paramGraphics2D); break;
/*     */     case 9:
/* 119 */       paintBackgroundPressedAndSelected(paramGraphics2D); break;
/*     */     case 10:
/* 120 */       paintBackgroundPressedAndSelectedAndFocused(paramGraphics2D); break;
/*     */     case 11:
/* 121 */       paintBackgroundMouseOverAndSelected(paramGraphics2D); break;
/*     */     case 12:
/* 122 */       paintBackgroundMouseOverAndSelectedAndFocused(paramGraphics2D); break;
/*     */     case 13:
/* 123 */       paintBackgroundDisabledAndSelected(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 132 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocused(Graphics2D paramGraphics2D) {
/* 136 */     this.path = decodePath1();
/* 137 */     paramGraphics2D.setPaint(this.color1);
/* 138 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 143 */     this.roundRect = decodeRoundRect1();
/* 144 */     paramGraphics2D.setPaint(this.color2);
/* 145 */     paramGraphics2D.fill(this.roundRect);
/* 146 */     this.roundRect = decodeRoundRect2();
/* 147 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/* 148 */     paramGraphics2D.fill(this.roundRect);
/* 149 */     this.roundRect = decodeRoundRect3();
/* 150 */     paramGraphics2D.setPaint(decodeGradient2(this.roundRect));
/* 151 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOverAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 156 */     this.roundRect = decodeRoundRect4();
/* 157 */     paramGraphics2D.setPaint(this.color1);
/* 158 */     paramGraphics2D.fill(this.roundRect);
/* 159 */     this.roundRect = decodeRoundRect2();
/* 160 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/* 161 */     paramGraphics2D.fill(this.roundRect);
/* 162 */     this.roundRect = decodeRoundRect3();
/* 163 */     paramGraphics2D.setPaint(decodeGradient2(this.roundRect));
/* 164 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 169 */     this.roundRect = decodeRoundRect5();
/* 170 */     paramGraphics2D.setPaint(this.color9);
/* 171 */     paramGraphics2D.fill(this.roundRect);
/* 172 */     this.roundRect = decodeRoundRect6();
/* 173 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 174 */     paramGraphics2D.fill(this.roundRect);
/* 175 */     this.roundRect = decodeRoundRect7();
/* 176 */     paramGraphics2D.setPaint(decodeGradient4(this.roundRect));
/* 177 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 182 */     this.roundRect = decodeRoundRect8();
/* 183 */     paramGraphics2D.setPaint(this.color1);
/* 184 */     paramGraphics2D.fill(this.roundRect);
/* 185 */     this.roundRect = decodeRoundRect6();
/* 186 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 187 */     paramGraphics2D.fill(this.roundRect);
/* 188 */     this.roundRect = decodeRoundRect7();
/* 189 */     paramGraphics2D.setPaint(decodeGradient4(this.roundRect));
/* 190 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 195 */     this.roundRect = decodeRoundRect5();
/* 196 */     paramGraphics2D.setPaint(this.color9);
/* 197 */     paramGraphics2D.fill(this.roundRect);
/* 198 */     this.roundRect = decodeRoundRect6();
/* 199 */     paramGraphics2D.setPaint(decodeGradient5(this.roundRect));
/* 200 */     paramGraphics2D.fill(this.roundRect);
/* 201 */     this.roundRect = decodeRoundRect7();
/* 202 */     paramGraphics2D.setPaint(decodeGradient6(this.roundRect));
/* 203 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 208 */     this.roundRect = decodeRoundRect8();
/* 209 */     paramGraphics2D.setPaint(this.color1);
/* 210 */     paramGraphics2D.fill(this.roundRect);
/* 211 */     this.roundRect = decodeRoundRect6();
/* 212 */     paramGraphics2D.setPaint(decodeGradient5(this.roundRect));
/* 213 */     paramGraphics2D.fill(this.roundRect);
/* 214 */     this.roundRect = decodeRoundRect7();
/* 215 */     paramGraphics2D.setPaint(decodeGradient6(this.roundRect));
/* 216 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressedAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 221 */     this.roundRect = decodeRoundRect5();
/* 222 */     paramGraphics2D.setPaint(this.color9);
/* 223 */     paramGraphics2D.fill(this.roundRect);
/* 224 */     this.roundRect = decodeRoundRect6();
/* 225 */     paramGraphics2D.setPaint(decodeGradient7(this.roundRect));
/* 226 */     paramGraphics2D.fill(this.roundRect);
/* 227 */     this.roundRect = decodeRoundRect7();
/* 228 */     paramGraphics2D.setPaint(decodeGradient8(this.roundRect));
/* 229 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressedAndSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 234 */     this.roundRect = decodeRoundRect8();
/* 235 */     paramGraphics2D.setPaint(this.color1);
/* 236 */     paramGraphics2D.fill(this.roundRect);
/* 237 */     this.roundRect = decodeRoundRect6();
/* 238 */     paramGraphics2D.setPaint(decodeGradient7(this.roundRect));
/* 239 */     paramGraphics2D.fill(this.roundRect);
/* 240 */     this.roundRect = decodeRoundRect7();
/* 241 */     paramGraphics2D.setPaint(decodeGradient8(this.roundRect));
/* 242 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOverAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 247 */     this.roundRect = decodeRoundRect5();
/* 248 */     paramGraphics2D.setPaint(this.color9);
/* 249 */     paramGraphics2D.fill(this.roundRect);
/* 250 */     this.roundRect = decodeRoundRect6();
/* 251 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 252 */     paramGraphics2D.fill(this.roundRect);
/* 253 */     this.roundRect = decodeRoundRect7();
/* 254 */     paramGraphics2D.setPaint(decodeGradient4(this.roundRect));
/* 255 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOverAndSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 260 */     this.roundRect = decodeRoundRect8();
/* 261 */     paramGraphics2D.setPaint(this.color1);
/* 262 */     paramGraphics2D.fill(this.roundRect);
/* 263 */     this.roundRect = decodeRoundRect6();
/* 264 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 265 */     paramGraphics2D.fill(this.roundRect);
/* 266 */     this.roundRect = decodeRoundRect7();
/* 267 */     paramGraphics2D.setPaint(decodeGradient4(this.roundRect));
/* 268 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabledAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 273 */     this.roundRect = decodeRoundRect5();
/* 274 */     paramGraphics2D.setPaint(this.color25);
/* 275 */     paramGraphics2D.fill(this.roundRect);
/* 276 */     this.roundRect = decodeRoundRect6();
/* 277 */     paramGraphics2D.setPaint(decodeGradient9(this.roundRect));
/* 278 */     paramGraphics2D.fill(this.roundRect);
/* 279 */     this.roundRect = decodeRoundRect7();
/* 280 */     paramGraphics2D.setPaint(decodeGradient10(this.roundRect));
/* 281 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 288 */     this.path.reset();
/* 289 */     this.path.moveTo(decodeX(1.413374F), decodeY(0.12F));
/* 290 */     this.path.lineTo(decodeX(1.989362F), decodeY(0.12F));
/* 291 */     this.path.curveTo(decodeAnchorX(1.989362F, 3.0F), decodeAnchorY(0.12F, 0.0F), decodeAnchorX(2.885715F, 0.0F), decodeAnchorY(1.041667F, -3.0F), decodeX(2.885715F), decodeY(1.041667F));
/* 292 */     this.path.lineTo(decodeX(2.9F), decodeY(1.916667F));
/* 293 */     this.path.curveTo(decodeAnchorX(2.9F, 0.0F), decodeAnchorY(1.916667F, 3.0F), decodeAnchorX(1.989362F, 3.0F), decodeAnchorY(2.671429F, 0.0F), decodeX(1.989362F), decodeY(2.671429F));
/* 294 */     this.path.lineTo(decodeX(1.010638F), decodeY(2.671429F));
/* 295 */     this.path.curveTo(decodeAnchorX(1.010638F, -3.0F), decodeAnchorY(2.671429F, 0.0F), decodeAnchorX(0.12F, 0.0F), decodeAnchorY(1.916667F, 3.0F), decodeX(0.12F), decodeY(1.916667F));
/* 296 */     this.path.lineTo(decodeX(0.12F), decodeY(1.044643F));
/* 297 */     this.path.curveTo(decodeAnchorX(0.12F, 0.0F), decodeAnchorY(1.044643F, -3.0F), decodeAnchorX(1.010638F, -3.0F), decodeAnchorY(0.12F, 0.0F), decodeX(1.010638F), decodeY(0.12F));
/* 298 */     this.path.lineTo(decodeX(1.414894F), decodeY(0.12F));
/* 299 */     this.path.lineTo(decodeX(1.414894F), decodeY(0.4857143F));
/* 300 */     this.path.lineTo(decodeX(1.010638F), decodeY(0.4857143F));
/* 301 */     this.path.curveTo(decodeAnchorX(1.010638F, -1.928572F), decodeAnchorY(0.4857143F, 0.0F), decodeAnchorX(0.4714286F, -0.04427948F), decodeAnchorY(1.03869F, -2.429218F), decodeX(0.4714286F), decodeY(1.03869F));
/* 302 */     this.path.lineTo(decodeX(0.4714286F), decodeY(1.916667F));
/* 303 */     this.path.curveTo(decodeAnchorX(0.4714286F, 0.0F), decodeAnchorY(1.916667F, 2.214286F), decodeAnchorX(1.010638F, -1.785714F), decodeAnchorY(2.314286F, 0.0F), decodeX(1.010638F), decodeY(2.314286F));
/* 304 */     this.path.lineTo(decodeX(1.989362F), decodeY(2.314286F));
/* 305 */     this.path.curveTo(decodeAnchorX(1.989362F, 2.071429F), decodeAnchorY(2.314286F, 0.0F), decodeAnchorX(2.5F, 0.0F), decodeAnchorY(1.916667F, 2.214286F), decodeX(2.5F), decodeY(1.916667F));
/* 306 */     this.path.lineTo(decodeX(2.514285F), decodeY(1.041667F));
/* 307 */     this.path.curveTo(decodeAnchorX(2.514285F, 0.0F), decodeAnchorY(1.041667F, -2.142857F), decodeAnchorX(1.990122F, 2.142857F), decodeAnchorY(0.4714286F, 0.0F), decodeX(1.990122F), decodeY(0.4714286F));
/* 308 */     this.path.lineTo(decodeX(1.414894F), decodeY(0.4857143F));
/* 309 */     this.path.lineTo(decodeX(1.413374F), decodeY(0.12F));
/* 310 */     this.path.closePath();
/* 311 */     return this.path;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect1() {
/* 315 */     this.roundRect.setRoundRect(decodeX(0.4F), decodeY(0.6F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.6F), 12.0D, 12.0D);
/*     */ 
/* 320 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect2() {
/* 324 */     this.roundRect.setRoundRect(decodeX(0.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.4F) - decodeY(0.4F), 12.0D, 12.0D);
/*     */ 
/* 329 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect3() {
/* 333 */     this.roundRect.setRoundRect(decodeX(0.6F), decodeY(0.6F), decodeX(2.4F) - decodeX(0.6F), decodeY(2.2F) - decodeY(0.6F), 9.0D, 9.0D);
/*     */ 
/* 338 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect4() {
/* 342 */     this.roundRect.setRoundRect(decodeX(0.12F), decodeY(0.12F), decodeX(2.88F) - decodeX(0.12F), decodeY(2.68F) - decodeY(0.12F), 13.0D, 13.0D);
/*     */ 
/* 347 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect5() {
/* 351 */     this.roundRect.setRoundRect(decodeX(0.4F), decodeY(0.6F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.6F), 10.0D, 10.0D);
/*     */ 
/* 356 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect6() {
/* 360 */     this.roundRect.setRoundRect(decodeX(0.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.4F) - decodeY(0.4F), 10.0D, 10.0D);
/*     */ 
/* 365 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect7() {
/* 369 */     this.roundRect.setRoundRect(decodeX(0.6F), decodeY(0.6F), decodeX(2.4F) - decodeX(0.6F), decodeY(2.2F) - decodeY(0.6F), 8.0D, 8.0D);
/*     */ 
/* 374 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect8() {
/* 378 */     this.roundRect.setRoundRect(decodeX(0.12F), decodeY(0.12F), decodeX(2.88F) - decodeX(0.12F), decodeY(2.68F) - decodeY(0.12F), 13.0D, 13.0D);
/*     */ 
/* 383 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 389 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 390 */     float f1 = (float)localRectangle2D.getX();
/* 391 */     float f2 = (float)localRectangle2D.getY();
/* 392 */     float f3 = (float)localRectangle2D.getWidth();
/* 393 */     float f4 = (float)localRectangle2D.getHeight();
/* 394 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.09F, 0.52F, 0.95F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 402 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 403 */     float f1 = (float)localRectangle2D.getX();
/* 404 */     float f2 = (float)localRectangle2D.getY();
/* 405 */     float f3 = (float)localRectangle2D.getWidth();
/* 406 */     float f4 = (float)localRectangle2D.getHeight();
/* 407 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.03F, 0.06F, 0.33F, 0.6F, 0.65F, 0.7F, 0.825F, 0.95F, 0.975F, 1.0F }, new Color[] { this.color5, decodeColor(this.color5, this.color6, 0.5F), this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8, decodeColor(this.color8, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 423 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 424 */     float f1 = (float)localRectangle2D.getX();
/* 425 */     float f2 = (float)localRectangle2D.getY();
/* 426 */     float f3 = (float)localRectangle2D.getWidth();
/* 427 */     float f4 = (float)localRectangle2D.getHeight();
/* 428 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.25F * f3 + f1, 1.004167F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 436 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 437 */     float f1 = (float)localRectangle2D.getX();
/* 438 */     float f2 = (float)localRectangle2D.getY();
/* 439 */     float f3 = (float)localRectangle2D.getWidth();
/* 440 */     float f4 = (float)localRectangle2D.getHeight();
/* 441 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.2512626F * f3 + f1, 1.009259F * f4 + f2, new float[] { 0.0F, 0.06684492F, 0.1336898F, 0.5668449F, 1.0F }, new Color[] { this.color12, decodeColor(this.color12, this.color13, 0.5F), this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 451 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 452 */     float f1 = (float)localRectangle2D.getX();
/* 453 */     float f2 = (float)localRectangle2D.getY();
/* 454 */     float f3 = (float)localRectangle2D.getWidth();
/* 455 */     float f4 = (float)localRectangle2D.getHeight();
/* 456 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.25F * f3 + f1, 1.004167F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 464 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 465 */     float f1 = (float)localRectangle2D.getX();
/* 466 */     float f2 = (float)localRectangle2D.getY();
/* 467 */     float f3 = (float)localRectangle2D.getWidth();
/* 468 */     float f4 = (float)localRectangle2D.getHeight();
/* 469 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.2512626F * f3 + f1, 1.009259F * f4 + f2, new float[] { 0.0F, 0.06684492F, 0.1336898F, 0.5668449F, 1.0F }, new Color[] { this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18, decodeColor(this.color18, this.color19, 0.5F), this.color19 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 479 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 480 */     float f1 = (float)localRectangle2D.getX();
/* 481 */     float f2 = (float)localRectangle2D.getY();
/* 482 */     float f3 = (float)localRectangle2D.getWidth();
/* 483 */     float f4 = (float)localRectangle2D.getHeight();
/* 484 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.25F * f3 + f1, 1.004167F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color20, decodeColor(this.color20, this.color21, 0.5F), this.color21 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 492 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 493 */     float f1 = (float)localRectangle2D.getX();
/* 494 */     float f2 = (float)localRectangle2D.getY();
/* 495 */     float f3 = (float)localRectangle2D.getWidth();
/* 496 */     float f4 = (float)localRectangle2D.getHeight();
/* 497 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.2512626F * f3 + f1, 1.009259F * f4 + f2, new float[] { 0.0F, 0.06684492F, 0.1336898F, 0.5668449F, 1.0F }, new Color[] { this.color22, decodeColor(this.color22, this.color23, 0.5F), this.color23, decodeColor(this.color23, this.color24, 0.5F), this.color24 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient9(Shape paramShape)
/*     */   {
/* 507 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 508 */     float f1 = (float)localRectangle2D.getX();
/* 509 */     float f2 = (float)localRectangle2D.getY();
/* 510 */     float f3 = (float)localRectangle2D.getWidth();
/* 511 */     float f4 = (float)localRectangle2D.getHeight();
/* 512 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.25F * f3 + f1, 1.004167F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color26, decodeColor(this.color26, this.color27, 0.5F), this.color27 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient10(Shape paramShape)
/*     */   {
/* 520 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 521 */     float f1 = (float)localRectangle2D.getX();
/* 522 */     float f2 = (float)localRectangle2D.getY();
/* 523 */     float f3 = (float)localRectangle2D.getWidth();
/* 524 */     float f4 = (float)localRectangle2D.getHeight();
/* 525 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.2512626F * f3 + f1, 1.009259F * f4 + f2, new float[] { 0.0F, 0.06684492F, 0.1336898F, 0.5668449F, 1.0F }, new Color[] { this.color27, decodeColor(this.color27, this.color28, 0.5F), this.color28, decodeColor(this.color28, this.color28, 0.5F), this.color28 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ToolBarToggleButtonPainter
 * JD-Core Version:    0.6.2
 */
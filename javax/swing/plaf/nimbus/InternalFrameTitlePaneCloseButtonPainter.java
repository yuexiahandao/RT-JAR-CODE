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
/*     */ final class InternalFrameTitlePaneCloseButtonPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_MOUSEOVER = 3;
/*     */   static final int BACKGROUND_PRESSED = 4;
/*     */   static final int BACKGROUND_ENABLED_WINDOWNOTFOCUSED = 5;
/*     */   static final int BACKGROUND_MOUSEOVER_WINDOWNOTFOCUSED = 6;
/*     */   static final int BACKGROUND_PRESSED_WINDOWNOTFOCUSED = 7;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  52 */   private Path2D path = new Path2D.Float();
/*  53 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  54 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  55 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  60 */   private Color color1 = decodeColor("nimbusRed", 0.5893519F, -0.7573658F, 0.09411764F, 0);
/*  61 */   private Color color2 = decodeColor("nimbusRed", 0.5962963F, -0.7100592F, 0.0F, 0);
/*  62 */   private Color color3 = decodeColor("nimbusRed", 0.6005698F, -0.720029F, -0.01568627F, -122);
/*  63 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.005555511F, -0.06244939F, 0.07058823F, 0);
/*  64 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.005555511F, -0.002999432F, -0.3803922F, -185);
/*  65 */   private Color color6 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, -0.4431373F, 0);
/*  66 */   private Color color7 = decodeColor("nimbusRed", -0.0002734261F, 0.1382904F, -0.03921568F, 0);
/*  67 */   private Color color8 = decodeColor("nimbusRed", 0.00068906F, -0.3666558F, 0.1176471F, 0);
/*  68 */   private Color color9 = decodeColor("nimbusRed", -0.00102171F, 0.1018046F, -0.03137255F, 0);
/*  69 */   private Color color10 = decodeColor("nimbusRed", -0.0002734261F, 0.1324334F, -0.03529412F, 0);
/*  70 */   private Color color11 = decodeColor("nimbusRed", -0.0002734261F, 0.00225872F, 0.06666666F, 0);
/*  71 */   private Color color12 = decodeColor("nimbusRed", 0.005653025F, 0.004000366F, -0.3843137F, -122);
/*  72 */   private Color color13 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  73 */   private Color color14 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, -0.388235F, 0);
/*  74 */   private Color color15 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, -0.1333333F, 0);
/*  75 */   private Color color16 = decodeColor("nimbusRed", 0.00068906F, -0.3892928F, 0.160784F, 0);
/*  76 */   private Color color17 = decodeColor("nimbusRed", 2.537202E-005F, 0.01229453F, 0.04313725F, 0);
/*  77 */   private Color color18 = decodeColor("nimbusRed", -0.0002734261F, 0.03358567F, 0.03921568F, 0);
/*  78 */   private Color color19 = decodeColor("nimbusRed", -0.0002734261F, -0.07198727F, 0.1411765F, 0);
/*  79 */   private Color color20 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, 0.003921568F, -122);
/*  80 */   private Color color21 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -140);
/*  81 */   private Color color22 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, -0.4941177F, 0);
/*  82 */   private Color color23 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, -0.2039216F, 0);
/*  83 */   private Color color24 = decodeColor("nimbusRed", -0.01481481F, -0.2126097F, 0.01960784F, 0);
/*  84 */   private Color color25 = decodeColor("nimbusRed", -0.01481481F, 0.1734057F, -0.0980392F, 0);
/*  85 */   private Color color26 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, -0.1058824F, 0);
/*  86 */   private Color color27 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, -0.04705882F, 0);
/*  87 */   private Color color28 = decodeColor("nimbusRed", -0.01481481F, 0.2011834F, -0.3176471F, -122);
/*  88 */   private Color color29 = decodeColor("nimbusRed", 0.5962963F, -0.6994788F, -0.07058823F, 0);
/*  89 */   private Color color30 = decodeColor("nimbusRed", 0.5962963F, -0.6624529F, -0.2313726F, 0);
/*  90 */   private Color color31 = decodeColor("nimbusRed", 0.5851852F, -0.7764952F, 0.2156863F, 0);
/*  91 */   private Color color32 = decodeColor("nimbusRed", 0.5962963F, -0.737278F, 0.1019608F, 0);
/*  92 */   private Color color33 = decodeColor("nimbusRed", 0.5962963F, -0.7391151F, 0.1254902F, 0);
/*  93 */   private Color color34 = decodeColor("nimbusBlueGrey", 0.0F, -0.02795751F, -0.3176471F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public InternalFrameTitlePaneCloseButtonPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/* 101 */     this.state = paramInt;
/* 102 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 108 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 111 */     switch (this.state) { case 1:
/* 112 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/* 113 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 3:
/* 114 */       paintBackgroundMouseOver(paramGraphics2D); break;
/*     */     case 4:
/* 115 */       paintBackgroundPressed(paramGraphics2D); break;
/*     */     case 5:
/* 116 */       paintBackgroundEnabledAndWindowNotFocused(paramGraphics2D); break;
/*     */     case 6:
/* 117 */       paintBackgroundMouseOverAndWindowNotFocused(paramGraphics2D); break;
/*     */     case 7:
/* 118 */       paintBackgroundPressedAndWindowNotFocused(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 127 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/* 131 */     this.roundRect = decodeRoundRect1();
/* 132 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/* 133 */     paramGraphics2D.fill(this.roundRect);
/* 134 */     this.path = decodePath1();
/* 135 */     paramGraphics2D.setPaint(this.color3);
/* 136 */     paramGraphics2D.fill(this.path);
/* 137 */     this.path = decodePath2();
/* 138 */     paramGraphics2D.setPaint(this.color4);
/* 139 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 144 */     this.roundRect = decodeRoundRect2();
/* 145 */     paramGraphics2D.setPaint(this.color5);
/* 146 */     paramGraphics2D.fill(this.roundRect);
/* 147 */     this.roundRect = decodeRoundRect1();
/* 148 */     paramGraphics2D.setPaint(decodeGradient2(this.roundRect));
/* 149 */     paramGraphics2D.fill(this.roundRect);
/* 150 */     this.roundRect = decodeRoundRect3();
/* 151 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 152 */     paramGraphics2D.fill(this.roundRect);
/* 153 */     this.path = decodePath1();
/* 154 */     paramGraphics2D.setPaint(this.color12);
/* 155 */     paramGraphics2D.fill(this.path);
/* 156 */     this.path = decodePath2();
/* 157 */     paramGraphics2D.setPaint(this.color13);
/* 158 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 163 */     this.roundRect = decodeRoundRect2();
/* 164 */     paramGraphics2D.setPaint(this.color5);
/* 165 */     paramGraphics2D.fill(this.roundRect);
/* 166 */     this.roundRect = decodeRoundRect1();
/* 167 */     paramGraphics2D.setPaint(decodeGradient4(this.roundRect));
/* 168 */     paramGraphics2D.fill(this.roundRect);
/* 169 */     this.roundRect = decodeRoundRect4();
/* 170 */     paramGraphics2D.setPaint(decodeGradient5(this.roundRect));
/* 171 */     paramGraphics2D.fill(this.roundRect);
/* 172 */     this.path = decodePath1();
/* 173 */     paramGraphics2D.setPaint(this.color20);
/* 174 */     paramGraphics2D.fill(this.path);
/* 175 */     this.path = decodePath2();
/* 176 */     paramGraphics2D.setPaint(this.color13);
/* 177 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 182 */     this.roundRect = decodeRoundRect2();
/* 183 */     paramGraphics2D.setPaint(this.color21);
/* 184 */     paramGraphics2D.fill(this.roundRect);
/* 185 */     this.roundRect = decodeRoundRect1();
/* 186 */     paramGraphics2D.setPaint(decodeGradient6(this.roundRect));
/* 187 */     paramGraphics2D.fill(this.roundRect);
/* 188 */     this.roundRect = decodeRoundRect3();
/* 189 */     paramGraphics2D.setPaint(decodeGradient7(this.roundRect));
/* 190 */     paramGraphics2D.fill(this.roundRect);
/* 191 */     this.path = decodePath1();
/* 192 */     paramGraphics2D.setPaint(this.color28);
/* 193 */     paramGraphics2D.fill(this.path);
/* 194 */     this.path = decodePath2();
/* 195 */     paramGraphics2D.setPaint(this.color13);
/* 196 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndWindowNotFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 201 */     this.roundRect = decodeRoundRect1();
/* 202 */     paramGraphics2D.setPaint(decodeGradient8(this.roundRect));
/* 203 */     paramGraphics2D.fill(this.roundRect);
/* 204 */     this.roundRect = decodeRoundRect3();
/* 205 */     paramGraphics2D.setPaint(decodeGradient9(this.roundRect));
/* 206 */     paramGraphics2D.fill(this.roundRect);
/* 207 */     this.path = decodePath2();
/* 208 */     paramGraphics2D.setPaint(this.color34);
/* 209 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOverAndWindowNotFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 214 */     this.roundRect = decodeRoundRect2();
/* 215 */     paramGraphics2D.setPaint(this.color5);
/* 216 */     paramGraphics2D.fill(this.roundRect);
/* 217 */     this.roundRect = decodeRoundRect1();
/* 218 */     paramGraphics2D.setPaint(decodeGradient4(this.roundRect));
/* 219 */     paramGraphics2D.fill(this.roundRect);
/* 220 */     this.roundRect = decodeRoundRect4();
/* 221 */     paramGraphics2D.setPaint(decodeGradient5(this.roundRect));
/* 222 */     paramGraphics2D.fill(this.roundRect);
/* 223 */     this.path = decodePath1();
/* 224 */     paramGraphics2D.setPaint(this.color20);
/* 225 */     paramGraphics2D.fill(this.path);
/* 226 */     this.path = decodePath2();
/* 227 */     paramGraphics2D.setPaint(this.color13);
/* 228 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressedAndWindowNotFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 233 */     this.roundRect = decodeRoundRect2();
/* 234 */     paramGraphics2D.setPaint(this.color21);
/* 235 */     paramGraphics2D.fill(this.roundRect);
/* 236 */     this.roundRect = decodeRoundRect1();
/* 237 */     paramGraphics2D.setPaint(decodeGradient6(this.roundRect));
/* 238 */     paramGraphics2D.fill(this.roundRect);
/* 239 */     this.roundRect = decodeRoundRect3();
/* 240 */     paramGraphics2D.setPaint(decodeGradient7(this.roundRect));
/* 241 */     paramGraphics2D.fill(this.roundRect);
/* 242 */     this.path = decodePath1();
/* 243 */     paramGraphics2D.setPaint(this.color28);
/* 244 */     paramGraphics2D.fill(this.path);
/* 245 */     this.path = decodePath2();
/* 246 */     paramGraphics2D.setPaint(this.color13);
/* 247 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect1()
/*     */   {
/* 254 */     this.roundRect.setRoundRect(decodeX(1.0F), decodeY(1.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(1.944444F) - decodeY(1.0F), 8.600000381469727D, 8.600000381469727D);
/*     */ 
/* 259 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 263 */     this.path.reset();
/* 264 */     this.path.moveTo(decodeX(1.25F), decodeY(1.737374F));
/* 265 */     this.path.lineTo(decodeX(1.300239F), decodeY(1.794192F));
/* 266 */     this.path.lineTo(decodeX(1.504785F), decodeY(1.590909F));
/* 267 */     this.path.lineTo(decodeX(1.684211F), decodeY(1.795455F));
/* 268 */     this.path.lineTo(decodeX(1.759569F), decodeY(1.719697F));
/* 269 */     this.path.lineTo(decodeX(1.595694F), decodeY(1.52399F));
/* 270 */     this.path.lineTo(decodeX(1.753588F), decodeY(1.340909F));
/* 271 */     this.path.lineTo(decodeX(1.683014F), decodeY(1.253788F));
/* 272 */     this.path.lineTo(decodeX(1.508373F), decodeY(1.440657F));
/* 273 */     this.path.lineTo(decodeX(1.330144F), decodeY(1.256313F));
/* 274 */     this.path.lineTo(decodeX(1.257177F), decodeY(1.332071F));
/* 275 */     this.path.lineTo(decodeX(1.427033F), decodeY(1.525253F));
/* 276 */     this.path.lineTo(decodeX(1.25F), decodeY(1.737374F));
/* 277 */     this.path.closePath();
/* 278 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 282 */     this.path.reset();
/* 283 */     this.path.moveTo(decodeX(1.257177F), decodeY(1.282828F));
/* 284 */     this.path.lineTo(decodeX(1.32177F), decodeY(1.213384F));
/* 285 */     this.path.lineTo(decodeX(1.5F), decodeY(1.404041F));
/* 286 */     this.path.lineTo(decodeX(1.673445F), decodeY(1.210859F));
/* 287 */     this.path.lineTo(decodeX(1.744019F), decodeY(1.285354F));
/* 288 */     this.path.lineTo(decodeX(1.566986F), decodeY(1.47096F));
/* 289 */     this.path.lineTo(decodeX(1.748804F), decodeY(1.652778F));
/* 290 */     this.path.lineTo(decodeX(1.673445F), decodeY(1.739899F));
/* 291 */     this.path.lineTo(decodeX(1.498804F), decodeY(1.541667F));
/* 292 */     this.path.lineTo(decodeX(1.33134F), decodeY(1.742424F));
/* 293 */     this.path.lineTo(decodeX(1.252392F), decodeY(1.656566F));
/* 294 */     this.path.lineTo(decodeX(1.436603F), decodeY(1.472222F));
/* 295 */     this.path.lineTo(decodeX(1.257177F), decodeY(1.282828F));
/* 296 */     this.path.closePath();
/* 297 */     return this.path;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect2() {
/* 301 */     this.roundRect.setRoundRect(decodeX(1.0F), decodeY(1.611111F), decodeX(2.0F) - decodeX(1.0F), decodeY(2.0F) - decodeY(1.611111F), 6.0D, 6.0D);
/*     */ 
/* 306 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect3() {
/* 310 */     this.roundRect.setRoundRect(decodeX(1.052632F), decodeY(1.05303F), decodeX(1.947368F) - decodeX(1.052632F), decodeY(1.886364F) - decodeY(1.05303F), 6.75D, 6.75D);
/*     */ 
/* 315 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect4() {
/* 319 */     this.roundRect.setRoundRect(decodeX(1.052632F), decodeY(1.051768F), decodeX(1.947368F) - decodeX(1.052632F), decodeY(1.885101F) - decodeY(1.051768F), 6.75D, 6.75D);
/*     */ 
/* 324 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 330 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 331 */     float f1 = (float)localRectangle2D.getX();
/* 332 */     float f2 = (float)localRectangle2D.getY();
/* 333 */     float f3 = (float)localRectangle2D.getWidth();
/* 334 */     float f4 = (float)localRectangle2D.getHeight();
/* 335 */     return decodeGradient(0.2486842F * f3 + f1, 0.001470588F * f4 + f2, 0.2486842F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color1, decodeColor(this.color1, this.color2, 0.5F), this.color2 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 343 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 344 */     float f1 = (float)localRectangle2D.getX();
/* 345 */     float f2 = (float)localRectangle2D.getY();
/* 346 */     float f3 = (float)localRectangle2D.getWidth();
/* 347 */     float f4 = (float)localRectangle2D.getHeight();
/* 348 */     return decodeGradient(0.2486842F * f3 + f1, 0.001470588F * f4 + f2, 0.2486842F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 356 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 357 */     float f1 = (float)localRectangle2D.getX();
/* 358 */     float f2 = (float)localRectangle2D.getY();
/* 359 */     float f3 = (float)localRectangle2D.getWidth();
/* 360 */     float f4 = (float)localRectangle2D.getHeight();
/* 361 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.2544118F * f3 + f1, 1.001667F * f4 + f2, new float[] { 0.0F, 0.2698864F, 0.5397728F, 0.595171F, 0.6505682F, 0.8252841F, 1.0F }, new Color[] { this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 373 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 374 */     float f1 = (float)localRectangle2D.getX();
/* 375 */     float f2 = (float)localRectangle2D.getY();
/* 376 */     float f3 = (float)localRectangle2D.getWidth();
/* 377 */     float f4 = (float)localRectangle2D.getHeight();
/* 378 */     return decodeGradient(0.2486842F * f3 + f1, 0.001470588F * f4 + f2, 0.2486842F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color14, decodeColor(this.color14, this.color15, 0.5F), this.color15 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 386 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 387 */     float f1 = (float)localRectangle2D.getX();
/* 388 */     float f2 = (float)localRectangle2D.getY();
/* 389 */     float f3 = (float)localRectangle2D.getWidth();
/* 390 */     float f4 = (float)localRectangle2D.getHeight();
/* 391 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.2544118F * f3 + f1, 1.001667F * f4 + f2, new float[] { 0.0F, 0.2698864F, 0.5397728F, 0.595171F, 0.6505682F, 0.814805F, 0.9790419F }, new Color[] { this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18, decodeColor(this.color18, this.color19, 0.5F), this.color19 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 403 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 404 */     float f1 = (float)localRectangle2D.getX();
/* 405 */     float f2 = (float)localRectangle2D.getY();
/* 406 */     float f3 = (float)localRectangle2D.getWidth();
/* 407 */     float f4 = (float)localRectangle2D.getHeight();
/* 408 */     return decodeGradient(0.2486842F * f3 + f1, 0.001470588F * f4 + f2, 0.2486842F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color22, decodeColor(this.color22, this.color23, 0.5F), this.color23 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient7(Shape paramShape)
/*     */   {
/* 416 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 417 */     float f1 = (float)localRectangle2D.getX();
/* 418 */     float f2 = (float)localRectangle2D.getY();
/* 419 */     float f3 = (float)localRectangle2D.getWidth();
/* 420 */     float f4 = (float)localRectangle2D.getHeight();
/* 421 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.2544118F * f3 + f1, 1.001667F * f4 + f2, new float[] { 0.0F, 0.2698864F, 0.5397728F, 0.595171F, 0.6505682F, 0.8163021F, 0.9820359F }, new Color[] { this.color24, decodeColor(this.color24, this.color25, 0.5F), this.color25, decodeColor(this.color25, this.color26, 0.5F), this.color26, decodeColor(this.color26, this.color27, 0.5F), this.color27 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient8(Shape paramShape)
/*     */   {
/* 433 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 434 */     float f1 = (float)localRectangle2D.getX();
/* 435 */     float f2 = (float)localRectangle2D.getY();
/* 436 */     float f3 = (float)localRectangle2D.getWidth();
/* 437 */     float f4 = (float)localRectangle2D.getHeight();
/* 438 */     return decodeGradient(0.2486842F * f3 + f1, 0.001470588F * f4 + f2, 0.2486842F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color29, decodeColor(this.color29, this.color30, 0.5F), this.color30 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient9(Shape paramShape)
/*     */   {
/* 446 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 447 */     float f1 = (float)localRectangle2D.getX();
/* 448 */     float f2 = (float)localRectangle2D.getY();
/* 449 */     float f3 = (float)localRectangle2D.getWidth();
/* 450 */     float f4 = (float)localRectangle2D.getHeight();
/* 451 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.2544118F * f3 + f1, 1.001667F * f4 + f2, new float[] { 0.0F, 0.241018F, 0.4820359F, 0.5838324F, 0.6856288F, 0.8428144F, 1.0F }, new Color[] { this.color31, decodeColor(this.color31, this.color32, 0.5F), this.color32, decodeColor(this.color32, this.color32, 0.5F), this.color32, decodeColor(this.color32, this.color33, 0.5F), this.color33 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.InternalFrameTitlePaneCloseButtonPainter
 * JD-Core Version:    0.6.2
 */
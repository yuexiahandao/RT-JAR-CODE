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
/*     */ final class TableHeaderRendererPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_ENABLED_FOCUSED = 3;
/*     */   static final int BACKGROUND_MOUSEOVER = 4;
/*     */   static final int BACKGROUND_PRESSED = 5;
/*     */   static final int BACKGROUND_ENABLED_SORTED = 6;
/*     */   static final int BACKGROUND_ENABLED_FOCUSED_SORTED = 7;
/*     */   static final int BACKGROUND_DISABLED_SORTED = 8;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  53 */   private Path2D path = new Path2D.Float();
/*  54 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  55 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  56 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  61 */   private Color color1 = decodeColor("nimbusBorder", -0.01388884F, 0.0005823001F, -0.1294118F, 0);
/*  62 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.0111111F, -0.0862545F, 0.06274509F, 0);
/*  63 */   private Color color3 = decodeColor("nimbusBlueGrey", -0.01388884F, -0.02833454F, -0.172549F, 0);
/*  64 */   private Color color4 = decodeColor("nimbusBlueGrey", -0.01388884F, -0.02944524F, -0.1647059F, 0);
/*  65 */   private Color color5 = decodeColor("nimbusBlueGrey", -0.02020204F, -0.0535315F, 0.01176471F, 0);
/*  66 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.1065581F, 0.2431372F, 0);
/*  67 */   private Color color7 = decodeColor("nimbusBlueGrey", 0.0F, -0.08455229F, 0.160784F, 0);
/*  68 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.0F, -0.07016757F, 0.1294118F, 0);
/*  69 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.0F, -0.07466974F, 0.2392157F, 0);
/*  70 */   private Color color10 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  71 */   private Color color11 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.1065893F, 0.2509804F, 0);
/*  72 */   private Color color12 = decodeColor("nimbusBlueGrey", 0.0F, -0.08613607F, 0.2196078F, 0);
/*  73 */   private Color color13 = decodeColor("nimbusBlueGrey", 0.0F, -0.07333623F, 0.2039216F, 0);
/*  74 */   private Color color14 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  75 */   private Color color15 = decodeColor("nimbusBlueGrey", -0.0050505F, -0.0596004F, 0.1019608F, 0);
/*  76 */   private Color color16 = decodeColor("nimbusBlueGrey", 0.0F, -0.01774281F, 0.01568627F, 0);
/*  77 */   private Color color17 = decodeColor("nimbusBlueGrey", -0.002777755F, -0.001830667F, -0.0235294F, 0);
/*  78 */   private Color color18 = decodeColor("nimbusBlueGrey", 0.005555511F, -0.02043623F, 0.1254902F, 0);
/*  79 */   private Color color19 = decodeColor("nimbusBase", -0.02309609F, -0.6237621F, 0.4352941F, 0);
/*  80 */   private Color color20 = decodeColor("nimbusBase", -0.001270711F, -0.5090175F, 0.317647F, 0);
/*  81 */   private Color color21 = decodeColor("nimbusBase", -0.0024612F, -0.4713951F, 0.2862745F, 0);
/*  82 */   private Color color22 = decodeColor("nimbusBase", -0.005122244F, -0.4910334F, 0.372549F, 0);
/*  83 */   private Color color23 = decodeColor("nimbusBase", -0.000873864F, -0.498728F, 0.309804F, 0);
/*  84 */   private Color color24 = decodeColor("nimbusBase", -0.0002202988F, -0.491647F, 0.3764706F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public TableHeaderRendererPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  92 */     this.state = paramInt;
/*  93 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  99 */     this.componentColors = paramArrayOfObject;
/*     */ 
/* 102 */     switch (this.state) { case 1:
/* 103 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/* 104 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 3:
/* 105 */       paintBackgroundEnabledAndFocused(paramGraphics2D); break;
/*     */     case 4:
/* 106 */       paintBackgroundMouseOver(paramGraphics2D); break;
/*     */     case 5:
/* 107 */       paintBackgroundPressed(paramGraphics2D); break;
/*     */     case 6:
/* 108 */       paintBackgroundEnabledAndSorted(paramGraphics2D); break;
/*     */     case 7:
/* 109 */       paintBackgroundEnabledAndFocusedAndSorted(paramGraphics2D); break;
/*     */     case 8:
/* 110 */       paintBackgroundDisabledAndSorted(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 119 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/* 123 */     this.rect = decodeRect1();
/* 124 */     paramGraphics2D.setPaint(this.color1);
/* 125 */     paramGraphics2D.fill(this.rect);
/* 126 */     this.rect = decodeRect2();
/* 127 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 128 */     paramGraphics2D.fill(this.rect);
/* 129 */     this.rect = decodeRect3();
/* 130 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 131 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 136 */     this.rect = decodeRect1();
/* 137 */     paramGraphics2D.setPaint(this.color1);
/* 138 */     paramGraphics2D.fill(this.rect);
/* 139 */     this.rect = decodeRect2();
/* 140 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 141 */     paramGraphics2D.fill(this.rect);
/* 142 */     this.rect = decodeRect3();
/* 143 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 144 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 149 */     this.rect = decodeRect1();
/* 150 */     paramGraphics2D.setPaint(this.color1);
/* 151 */     paramGraphics2D.fill(this.rect);
/* 152 */     this.rect = decodeRect2();
/* 153 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 154 */     paramGraphics2D.fill(this.rect);
/* 155 */     this.rect = decodeRect3();
/* 156 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 157 */     paramGraphics2D.fill(this.rect);
/* 158 */     this.path = decodePath1();
/* 159 */     paramGraphics2D.setPaint(this.color10);
/* 160 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 165 */     this.rect = decodeRect1();
/* 166 */     paramGraphics2D.setPaint(this.color1);
/* 167 */     paramGraphics2D.fill(this.rect);
/* 168 */     this.rect = decodeRect2();
/* 169 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 170 */     paramGraphics2D.fill(this.rect);
/* 171 */     this.rect = decodeRect3();
/* 172 */     paramGraphics2D.setPaint(decodeGradient3(this.rect));
/* 173 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 178 */     this.rect = decodeRect1();
/* 179 */     paramGraphics2D.setPaint(this.color1);
/* 180 */     paramGraphics2D.fill(this.rect);
/* 181 */     this.rect = decodeRect2();
/* 182 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 183 */     paramGraphics2D.fill(this.rect);
/* 184 */     this.rect = decodeRect3();
/* 185 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 186 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndSorted(Graphics2D paramGraphics2D)
/*     */   {
/* 191 */     this.rect = decodeRect1();
/* 192 */     paramGraphics2D.setPaint(this.color1);
/* 193 */     paramGraphics2D.fill(this.rect);
/* 194 */     this.rect = decodeRect2();
/* 195 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 196 */     paramGraphics2D.fill(this.rect);
/* 197 */     this.rect = decodeRect3();
/* 198 */     paramGraphics2D.setPaint(decodeGradient5(this.rect));
/* 199 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndFocusedAndSorted(Graphics2D paramGraphics2D)
/*     */   {
/* 204 */     this.rect = decodeRect1();
/* 205 */     paramGraphics2D.setPaint(this.color1);
/* 206 */     paramGraphics2D.fill(this.rect);
/* 207 */     this.rect = decodeRect2();
/* 208 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 209 */     paramGraphics2D.fill(this.rect);
/* 210 */     this.rect = decodeRect3();
/* 211 */     paramGraphics2D.setPaint(decodeGradient6(this.rect));
/* 212 */     paramGraphics2D.fill(this.rect);
/* 213 */     this.path = decodePath1();
/* 214 */     paramGraphics2D.setPaint(this.color10);
/* 215 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabledAndSorted(Graphics2D paramGraphics2D)
/*     */   {
/* 220 */     this.rect = decodeRect1();
/* 221 */     paramGraphics2D.setPaint(this.color1);
/* 222 */     paramGraphics2D.fill(this.rect);
/* 223 */     this.rect = decodeRect2();
/* 224 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 225 */     paramGraphics2D.fill(this.rect);
/* 226 */     this.rect = decodeRect3();
/* 227 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 228 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 235 */     this.rect.setRect(decodeX(0.0F), decodeY(2.8F), decodeX(3.0F) - decodeX(0.0F), decodeY(3.0F) - decodeY(2.8F));
/*     */ 
/* 239 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 243 */     this.rect.setRect(decodeX(2.8F), decodeY(0.0F), decodeX(3.0F) - decodeX(2.8F), decodeY(2.8F) - decodeY(0.0F));
/*     */ 
/* 247 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 251 */     this.rect.setRect(decodeX(0.0F), decodeY(0.0F), decodeX(2.8F) - decodeX(0.0F), decodeY(2.8F) - decodeY(0.0F));
/*     */ 
/* 255 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 259 */     this.path.reset();
/* 260 */     this.path.moveTo(decodeX(0.0F), decodeY(0.0F));
/* 261 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 262 */     this.path.lineTo(decodeX(3.0F), decodeY(3.0F));
/* 263 */     this.path.lineTo(decodeX(3.0F), decodeY(0.0F));
/* 264 */     this.path.lineTo(decodeX(0.24F), decodeY(0.0F));
/* 265 */     this.path.lineTo(decodeX(0.24F), decodeY(0.24F));
/* 266 */     this.path.lineTo(decodeX(2.76F), decodeY(0.24F));
/* 267 */     this.path.lineTo(decodeX(2.76F), decodeY(2.76F));
/* 268 */     this.path.lineTo(decodeX(0.24F), decodeY(2.76F));
/* 269 */     this.path.lineTo(decodeX(0.24F), decodeY(0.0F));
/* 270 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 271 */     this.path.closePath();
/* 272 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 278 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 279 */     float f1 = (float)localRectangle2D.getX();
/* 280 */     float f2 = (float)localRectangle2D.getY();
/* 281 */     float f3 = (float)localRectangle2D.getWidth();
/* 282 */     float f4 = (float)localRectangle2D.getHeight();
/* 283 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1444122F, 0.4370371F, 0.5944445F, 0.7518519F, 0.875926F, 1.0F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 295 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 296 */     float f1 = (float)localRectangle2D.getX();
/* 297 */     float f2 = (float)localRectangle2D.getY();
/* 298 */     float f3 = (float)localRectangle2D.getWidth();
/* 299 */     float f4 = (float)localRectangle2D.getHeight();
/* 300 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.07147767F, 0.288889F, 0.5490909F, 0.703704F, 0.8518518F, 1.0F }, new Color[] { this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 312 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 313 */     float f1 = (float)localRectangle2D.getX();
/* 314 */     float f2 = (float)localRectangle2D.getY();
/* 315 */     float f3 = (float)localRectangle2D.getWidth();
/* 316 */     float f4 = (float)localRectangle2D.getHeight();
/* 317 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.07147767F, 0.288889F, 0.5490909F, 0.703704F, 0.79192F, 0.880137F }, new Color[] { this.color11, decodeColor(this.color11, this.color12, 0.5F), this.color12, decodeColor(this.color12, this.color13, 0.5F), this.color13, decodeColor(this.color13, this.color14, 0.5F), this.color14 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 329 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 330 */     float f1 = (float)localRectangle2D.getX();
/* 331 */     float f2 = (float)localRectangle2D.getY();
/* 332 */     float f3 = (float)localRectangle2D.getWidth();
/* 333 */     float f4 = (float)localRectangle2D.getHeight();
/* 334 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.07147767F, 0.288889F, 0.5490909F, 0.703704F, 0.8518518F, 1.0F }, new Color[] { this.color15, decodeColor(this.color15, this.color16, 0.5F), this.color16, decodeColor(this.color16, this.color17, 0.5F), this.color17, decodeColor(this.color17, this.color18, 0.5F), this.color18 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 346 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 347 */     float f1 = (float)localRectangle2D.getX();
/* 348 */     float f2 = (float)localRectangle2D.getY();
/* 349 */     float f3 = (float)localRectangle2D.getWidth();
/* 350 */     float f4 = (float)localRectangle2D.getHeight();
/* 351 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.08049711F, 0.3253425F, 0.5626782F, 0.703704F, 0.8398656F, 0.9760274F }, new Color[] { this.color19, decodeColor(this.color19, this.color20, 0.5F), this.color20, decodeColor(this.color20, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color22, 0.5F), this.color22 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient6(Shape paramShape)
/*     */   {
/* 363 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 364 */     float f1 = (float)localRectangle2D.getX();
/* 365 */     float f2 = (float)localRectangle2D.getY();
/* 366 */     float f3 = (float)localRectangle2D.getWidth();
/* 367 */     float f4 = (float)localRectangle2D.getHeight();
/* 368 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.07147767F, 0.288889F, 0.5490909F, 0.703704F, 0.8518518F, 1.0F }, new Color[] { this.color19, decodeColor(this.color19, this.color23, 0.5F), this.color23, decodeColor(this.color23, this.color21, 0.5F), this.color21, decodeColor(this.color21, this.color24, 0.5F), this.color24 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TableHeaderRendererPainter
 * JD-Core Version:    0.6.2
 */
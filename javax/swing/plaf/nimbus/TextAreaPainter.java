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
/*     */ final class TextAreaPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_DISABLED_NOTINSCROLLPANE = 3;
/*     */   static final int BACKGROUND_ENABLED_NOTINSCROLLPANE = 4;
/*     */   static final int BACKGROUND_SELECTED = 5;
/*     */   static final int BORDER_DISABLED_NOTINSCROLLPANE = 6;
/*     */   static final int BORDER_FOCUSED_NOTINSCROLLPANE = 7;
/*     */   static final int BORDER_ENABLED_NOTINSCROLLPANE = 8;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  53 */   private Path2D path = new Path2D.Float();
/*  54 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  55 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  56 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  61 */   private Color color1 = decodeColor("nimbusBlueGrey", -0.01587296F, -0.07995863F, 0.1529412F, 0);
/*  62 */   private Color color2 = decodeColor("nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  63 */   private Color color3 = decodeColor("nimbusBlueGrey", -0.00694442F, -0.071879F, 0.06666666F, 0);
/*  64 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07826825F, 0.1058824F, 0);
/*  65 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07856284F, 0.1137255F, 0);
/*  66 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.0779682F, 0.0980392F, 0);
/*  67 */   private Color color7 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.0965403F, -0.1843137F, 0);
/*  68 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.104877F, -0.05098039F, 0);
/*  69 */   private Color color9 = decodeColor("nimbusLightBackground", 0.6666667F, 0.00490196F, -0.2F, 0);
/*  70 */   private Color color10 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.1051209F, -0.01960784F, 0);
/*  71 */   private Color color11 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.105345F, 0.01176471F, 0);
/*  72 */   private Color color12 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public TextAreaPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  80 */     this.state = paramInt;
/*  81 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  87 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  90 */     switch (this.state) { case 1:
/*  91 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/*  92 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 3:
/*  93 */       paintBackgroundDisabledAndNotInScrollPane(paramGraphics2D); break;
/*     */     case 4:
/*  94 */       paintBackgroundEnabledAndNotInScrollPane(paramGraphics2D); break;
/*     */     case 5:
/*  95 */       paintBackgroundSelected(paramGraphics2D); break;
/*     */     case 6:
/*  96 */       paintBorderDisabledAndNotInScrollPane(paramGraphics2D); break;
/*     */     case 7:
/*  97 */       paintBorderFocusedAndNotInScrollPane(paramGraphics2D); break;
/*     */     case 8:
/*  98 */       paintBorderEnabledAndNotInScrollPane(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Object[] getExtendedCacheKeys(JComponent paramJComponent)
/*     */   {
/* 104 */     Object[] arrayOfObject = null;
/* 105 */     switch (this.state) {
/*     */     case 2:
/* 107 */       arrayOfObject = new Object[] { getComponentColor(paramJComponent, "background", this.color2, 0.0F, 0.0F, 0) };
/*     */ 
/* 109 */       break;
/*     */     case 4:
/* 111 */       arrayOfObject = new Object[] { getComponentColor(paramJComponent, "background", this.color2, 0.0F, 0.0F, 0) };
/*     */ 
/* 113 */       break;
/*     */     case 7:
/* 115 */       arrayOfObject = new Object[] { getComponentColor(paramJComponent, "background", this.color9, 0.00490196F, -0.2F, 0), getComponentColor(paramJComponent, "background", this.color2, 0.0F, 0.0F, 0) };
/*     */ 
/* 118 */       break;
/*     */     case 8:
/* 120 */       arrayOfObject = new Object[] { getComponentColor(paramJComponent, "background", this.color9, 0.00490196F, -0.2F, 0), getComponentColor(paramJComponent, "background", this.color2, 0.0F, 0.0F, 0) };
/*     */     case 3:
/*     */     case 5:
/*     */     case 6:
/*     */     }
/* 125 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 130 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/* 134 */     this.rect = decodeRect1();
/* 135 */     paramGraphics2D.setPaint(this.color1);
/* 136 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 141 */     this.rect = decodeRect1();
/* 142 */     paramGraphics2D.setPaint((Color)this.componentColors[0]);
/* 143 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabledAndNotInScrollPane(Graphics2D paramGraphics2D)
/*     */   {
/* 148 */     this.rect = decodeRect2();
/* 149 */     paramGraphics2D.setPaint(this.color1);
/* 150 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndNotInScrollPane(Graphics2D paramGraphics2D)
/*     */   {
/* 155 */     this.rect = decodeRect2();
/* 156 */     paramGraphics2D.setPaint((Color)this.componentColors[0]);
/* 157 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 162 */     this.rect = decodeRect2();
/* 163 */     paramGraphics2D.setPaint(this.color2);
/* 164 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBorderDisabledAndNotInScrollPane(Graphics2D paramGraphics2D)
/*     */   {
/* 169 */     this.rect = decodeRect3();
/* 170 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 171 */     paramGraphics2D.fill(this.rect);
/* 172 */     this.rect = decodeRect4();
/* 173 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 174 */     paramGraphics2D.fill(this.rect);
/* 175 */     this.rect = decodeRect5();
/* 176 */     paramGraphics2D.setPaint(this.color6);
/* 177 */     paramGraphics2D.fill(this.rect);
/* 178 */     this.rect = decodeRect6();
/* 179 */     paramGraphics2D.setPaint(this.color4);
/* 180 */     paramGraphics2D.fill(this.rect);
/* 181 */     this.rect = decodeRect7();
/* 182 */     paramGraphics2D.setPaint(this.color4);
/* 183 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBorderFocusedAndNotInScrollPane(Graphics2D paramGraphics2D)
/*     */   {
/* 188 */     this.rect = decodeRect8();
/* 189 */     paramGraphics2D.setPaint(decodeGradient3(this.rect));
/* 190 */     paramGraphics2D.fill(this.rect);
/* 191 */     this.rect = decodeRect9();
/* 192 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 193 */     paramGraphics2D.fill(this.rect);
/* 194 */     this.rect = decodeRect10();
/* 195 */     paramGraphics2D.setPaint(this.color10);
/* 196 */     paramGraphics2D.fill(this.rect);
/* 197 */     this.rect = decodeRect11();
/* 198 */     paramGraphics2D.setPaint(this.color10);
/* 199 */     paramGraphics2D.fill(this.rect);
/* 200 */     this.rect = decodeRect12();
/* 201 */     paramGraphics2D.setPaint(this.color11);
/* 202 */     paramGraphics2D.fill(this.rect);
/* 203 */     this.path = decodePath1();
/* 204 */     paramGraphics2D.setPaint(this.color12);
/* 205 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBorderEnabledAndNotInScrollPane(Graphics2D paramGraphics2D)
/*     */   {
/* 210 */     this.rect = decodeRect8();
/* 211 */     paramGraphics2D.setPaint(decodeGradient5(this.rect));
/* 212 */     paramGraphics2D.fill(this.rect);
/* 213 */     this.rect = decodeRect9();
/* 214 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 215 */     paramGraphics2D.fill(this.rect);
/* 216 */     this.rect = decodeRect10();
/* 217 */     paramGraphics2D.setPaint(this.color10);
/* 218 */     paramGraphics2D.fill(this.rect);
/* 219 */     this.rect = decodeRect11();
/* 220 */     paramGraphics2D.setPaint(this.color10);
/* 221 */     paramGraphics2D.fill(this.rect);
/* 222 */     this.rect = decodeRect12();
/* 223 */     paramGraphics2D.setPaint(this.color11);
/* 224 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 231 */     this.rect.setRect(decodeX(0.0F), decodeY(0.0F), decodeX(3.0F) - decodeX(0.0F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 235 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 239 */     this.rect.setRect(decodeX(0.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.4F));
/*     */ 
/* 243 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 247 */     this.rect.setRect(decodeX(0.6666667F), decodeY(0.4F), decodeX(2.333333F) - decodeX(0.6666667F), decodeY(1.0F) - decodeY(0.4F));
/*     */ 
/* 251 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect4() {
/* 255 */     this.rect.setRect(decodeX(1.0F), decodeY(0.6F), decodeX(2.0F) - decodeX(1.0F), decodeY(1.0F) - decodeY(0.6F));
/*     */ 
/* 259 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect5() {
/* 263 */     this.rect.setRect(decodeX(0.6666667F), decodeY(1.0F), decodeX(1.0F) - decodeX(0.6666667F), decodeY(2.0F) - decodeY(1.0F));
/*     */ 
/* 267 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect6() {
/* 271 */     this.rect.setRect(decodeX(0.6666667F), decodeY(2.333333F), decodeX(2.333333F) - decodeX(0.6666667F), decodeY(2.0F) - decodeY(2.333333F));
/*     */ 
/* 275 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect7() {
/* 279 */     this.rect.setRect(decodeX(2.0F), decodeY(1.0F), decodeX(2.333333F) - decodeX(2.0F), decodeY(2.0F) - decodeY(1.0F));
/*     */ 
/* 283 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect8() {
/* 287 */     this.rect.setRect(decodeX(0.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(0.4F), decodeY(1.0F) - decodeY(0.4F));
/*     */ 
/* 291 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect9() {
/* 295 */     this.rect.setRect(decodeX(0.6F), decodeY(0.6F), decodeX(2.4F) - decodeX(0.6F), decodeY(1.0F) - decodeY(0.6F));
/*     */ 
/* 299 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect10() {
/* 303 */     this.rect.setRect(decodeX(0.4F), decodeY(1.0F), decodeX(0.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(1.0F));
/*     */ 
/* 307 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect11() {
/* 311 */     this.rect.setRect(decodeX(2.4F), decodeY(1.0F), decodeX(2.6F) - decodeX(2.4F), decodeY(2.6F) - decodeY(1.0F));
/*     */ 
/* 315 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect12() {
/* 319 */     this.rect.setRect(decodeX(0.6F), decodeY(2.4F), decodeX(2.4F) - decodeX(0.6F), decodeY(2.6F) - decodeY(2.4F));
/*     */ 
/* 323 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 327 */     this.path.reset();
/* 328 */     this.path.moveTo(decodeX(0.4F), decodeY(0.4F));
/* 329 */     this.path.lineTo(decodeX(0.4F), decodeY(2.6F));
/* 330 */     this.path.lineTo(decodeX(2.6F), decodeY(2.6F));
/* 331 */     this.path.lineTo(decodeX(2.6F), decodeY(0.4F));
/* 332 */     this.path.curveTo(decodeAnchorX(2.6F, 0.0F), decodeAnchorY(0.4F, 0.0F), decodeAnchorX(2.88F, 0.1F), decodeAnchorY(0.4F, 0.0F), decodeX(2.88F), decodeY(0.4F));
/* 333 */     this.path.curveTo(decodeAnchorX(2.88F, 0.1F), decodeAnchorY(0.4F, 0.0F), decodeAnchorX(2.88F, 0.0F), decodeAnchorY(2.88F, 0.0F), decodeX(2.88F), decodeY(2.88F));
/* 334 */     this.path.lineTo(decodeX(0.12F), decodeY(2.88F));
/* 335 */     this.path.lineTo(decodeX(0.12F), decodeY(0.12F));
/* 336 */     this.path.lineTo(decodeX(2.88F), decodeY(0.12F));
/* 337 */     this.path.lineTo(decodeX(2.88F), decodeY(0.4F));
/* 338 */     this.path.lineTo(decodeX(0.4F), decodeY(0.4F));
/* 339 */     this.path.closePath();
/* 340 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 346 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 347 */     float f1 = (float)localRectangle2D.getX();
/* 348 */     float f2 = (float)localRectangle2D.getY();
/* 349 */     float f3 = (float)localRectangle2D.getWidth();
/* 350 */     float f4 = (float)localRectangle2D.getHeight();
/* 351 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 359 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 360 */     float f1 = (float)localRectangle2D.getX();
/* 361 */     float f2 = (float)localRectangle2D.getY();
/* 362 */     float f3 = (float)localRectangle2D.getWidth();
/* 363 */     float f4 = (float)localRectangle2D.getHeight();
/* 364 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color5, decodeColor(this.color5, this.color1, 0.5F), this.color1 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 372 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 373 */     float f1 = (float)localRectangle2D.getX();
/* 374 */     float f2 = (float)localRectangle2D.getY();
/* 375 */     float f3 = (float)localRectangle2D.getWidth();
/* 376 */     float f4 = (float)localRectangle2D.getHeight();
/* 377 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.25F * f3 + f1, 0.1625F * f4 + f2, new float[] { 0.1F, 0.5F, 0.9F }, new Color[] { this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 385 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 386 */     float f1 = (float)localRectangle2D.getX();
/* 387 */     float f2 = (float)localRectangle2D.getY();
/* 388 */     float f3 = (float)localRectangle2D.getWidth();
/* 389 */     float f4 = (float)localRectangle2D.getHeight();
/* 390 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.1F, 0.5F, 0.9F }, new Color[] { (Color)this.componentColors[0], decodeColor((Color)this.componentColors[0], (Color)this.componentColors[1], 0.5F), (Color)this.componentColors[1] });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 398 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 399 */     float f1 = (float)localRectangle2D.getX();
/* 400 */     float f2 = (float)localRectangle2D.getY();
/* 401 */     float f3 = (float)localRectangle2D.getWidth();
/* 402 */     float f4 = (float)localRectangle2D.getHeight();
/* 403 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.1F, 0.5F, 0.9F }, new Color[] { this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TextAreaPainter
 * JD-Core Version:    0.6.2
 */
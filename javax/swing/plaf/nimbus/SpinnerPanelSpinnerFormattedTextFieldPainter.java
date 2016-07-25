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
/*     */ final class SpinnerPanelSpinnerFormattedTextFieldPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_FOCUSED = 3;
/*     */   static final int BACKGROUND_SELECTED = 4;
/*     */   static final int BACKGROUND_SELECTED_FOCUSED = 5;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  50 */   private Path2D path = new Path2D.Float();
/*  51 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  52 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  53 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  58 */   private Color color1 = decodeColor("nimbusBlueGrey", -0.611111F, -0.1105263F, -0.7450981F, -237);
/*  59 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.00694442F, -0.071879F, 0.06666666F, 0);
/*  60 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.0770335F, 0.0745098F, 0);
/*  61 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07968931F, 0.145098F, 0);
/*  62 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07856284F, 0.1137255F, 0);
/*  63 */   private Color color6 = decodeColor("nimbusBase", 0.04039598F, -0.6031562F, 0.2941176F, 0);
/*  64 */   private Color color7 = decodeColor("nimbusBase", 0.01658648F, -0.6051466F, 0.3490196F, 0);
/*  65 */   private Color color8 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.0965403F, -0.1843137F, 0);
/*  66 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.104877F, -0.08F, 0);
/*  67 */   private Color color10 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.1056244F, 0.05490196F, 0);
/*  68 */   private Color color11 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  69 */   private Color color12 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.105345F, 0.01176471F, 0);
/*  70 */   private Color color13 = decodeColor("nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  71 */   private Color color14 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  72 */   private Color color15 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.104877F, -0.05098039F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public SpinnerPanelSpinnerFormattedTextFieldPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
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
/*  93 */       paintBackgroundFocused(paramGraphics2D); break;
/*     */     case 4:
/*  94 */       paintBackgroundSelected(paramGraphics2D); break;
/*     */     case 5:
/*  95 */       paintBackgroundSelectedAndFocused(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 104 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/* 108 */     this.rect = decodeRect1();
/* 109 */     paramGraphics2D.setPaint(this.color1);
/* 110 */     paramGraphics2D.fill(this.rect);
/* 111 */     this.rect = decodeRect2();
/* 112 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 113 */     paramGraphics2D.fill(this.rect);
/* 114 */     this.rect = decodeRect3();
/* 115 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 116 */     paramGraphics2D.fill(this.rect);
/* 117 */     this.rect = decodeRect4();
/* 118 */     paramGraphics2D.setPaint(this.color6);
/* 119 */     paramGraphics2D.fill(this.rect);
/* 120 */     this.rect = decodeRect5();
/* 121 */     paramGraphics2D.setPaint(this.color7);
/* 122 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 127 */     this.rect = decodeRect1();
/* 128 */     paramGraphics2D.setPaint(this.color1);
/* 129 */     paramGraphics2D.fill(this.rect);
/* 130 */     this.rect = decodeRect2();
/* 131 */     paramGraphics2D.setPaint(decodeGradient3(this.rect));
/* 132 */     paramGraphics2D.fill(this.rect);
/* 133 */     this.rect = decodeRect3();
/* 134 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 135 */     paramGraphics2D.fill(this.rect);
/* 136 */     this.rect = decodeRect4();
/* 137 */     paramGraphics2D.setPaint(this.color12);
/* 138 */     paramGraphics2D.fill(this.rect);
/* 139 */     this.rect = decodeRect5();
/* 140 */     paramGraphics2D.setPaint(this.color13);
/* 141 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 146 */     this.rect = decodeRect6();
/* 147 */     paramGraphics2D.setPaint(this.color14);
/* 148 */     paramGraphics2D.fill(this.rect);
/* 149 */     this.rect = decodeRect2();
/* 150 */     paramGraphics2D.setPaint(decodeGradient5(this.rect));
/* 151 */     paramGraphics2D.fill(this.rect);
/* 152 */     this.rect = decodeRect3();
/* 153 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 154 */     paramGraphics2D.fill(this.rect);
/* 155 */     this.rect = decodeRect4();
/* 156 */     paramGraphics2D.setPaint(this.color12);
/* 157 */     paramGraphics2D.fill(this.rect);
/* 158 */     this.rect = decodeRect5();
/* 159 */     paramGraphics2D.setPaint(this.color13);
/* 160 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 165 */     this.rect = decodeRect1();
/* 166 */     paramGraphics2D.setPaint(this.color1);
/* 167 */     paramGraphics2D.fill(this.rect);
/* 168 */     this.rect = decodeRect2();
/* 169 */     paramGraphics2D.setPaint(decodeGradient3(this.rect));
/* 170 */     paramGraphics2D.fill(this.rect);
/* 171 */     this.rect = decodeRect3();
/* 172 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 173 */     paramGraphics2D.fill(this.rect);
/* 174 */     this.rect = decodeRect4();
/* 175 */     paramGraphics2D.setPaint(this.color12);
/* 176 */     paramGraphics2D.fill(this.rect);
/* 177 */     this.rect = decodeRect5();
/* 178 */     paramGraphics2D.setPaint(this.color13);
/* 179 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 184 */     this.rect = decodeRect6();
/* 185 */     paramGraphics2D.setPaint(this.color14);
/* 186 */     paramGraphics2D.fill(this.rect);
/* 187 */     this.rect = decodeRect2();
/* 188 */     paramGraphics2D.setPaint(decodeGradient5(this.rect));
/* 189 */     paramGraphics2D.fill(this.rect);
/* 190 */     this.rect = decodeRect3();
/* 191 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 192 */     paramGraphics2D.fill(this.rect);
/* 193 */     this.rect = decodeRect4();
/* 194 */     paramGraphics2D.setPaint(this.color12);
/* 195 */     paramGraphics2D.fill(this.rect);
/* 196 */     this.rect = decodeRect5();
/* 197 */     paramGraphics2D.setPaint(this.color13);
/* 198 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 205 */     this.rect.setRect(decodeX(0.6666667F), decodeY(2.333333F), decodeX(3.0F) - decodeX(0.6666667F), decodeY(2.666667F) - decodeY(2.333333F));
/*     */ 
/* 209 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 213 */     this.rect.setRect(decodeX(0.6666667F), decodeY(0.4F), decodeX(3.0F) - decodeX(0.6666667F), decodeY(1.0F) - decodeY(0.4F));
/*     */ 
/* 217 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 221 */     this.rect.setRect(decodeX(1.0F), decodeY(0.6F), decodeX(3.0F) - decodeX(1.0F), decodeY(1.0F) - decodeY(0.6F));
/*     */ 
/* 225 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect4() {
/* 229 */     this.rect.setRect(decodeX(0.6666667F), decodeY(1.0F), decodeX(3.0F) - decodeX(0.6666667F), decodeY(2.333333F) - decodeY(1.0F));
/*     */ 
/* 233 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect5() {
/* 237 */     this.rect.setRect(decodeX(1.0F), decodeY(1.0F), decodeX(3.0F) - decodeX(1.0F), decodeY(2.0F) - decodeY(1.0F));
/*     */ 
/* 241 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect6() {
/* 245 */     this.rect.setRect(decodeX(0.2222222F), decodeY(0.1333333F), decodeX(2.916668F) - decodeX(0.2222222F), decodeY(2.75F) - decodeY(0.1333333F));
/*     */ 
/* 249 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 255 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 256 */     float f1 = (float)localRectangle2D.getX();
/* 257 */     float f2 = (float)localRectangle2D.getY();
/* 258 */     float f3 = (float)localRectangle2D.getWidth();
/* 259 */     float f4 = (float)localRectangle2D.getHeight();
/* 260 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 268 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 269 */     float f1 = (float)localRectangle2D.getX();
/* 270 */     float f2 = (float)localRectangle2D.getY();
/* 271 */     float f3 = (float)localRectangle2D.getWidth();
/* 272 */     float f4 = (float)localRectangle2D.getHeight();
/* 273 */     return decodeGradient(0.5F * f3 + f1, 1.0F * f4 + f2, 0.5F * f3 + f1, 0.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 281 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 282 */     float f1 = (float)localRectangle2D.getX();
/* 283 */     float f2 = (float)localRectangle2D.getY();
/* 284 */     float f3 = (float)localRectangle2D.getWidth();
/* 285 */     float f4 = (float)localRectangle2D.getHeight();
/* 286 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.4957386F, 0.9914773F }, new Color[] { this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 294 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 295 */     float f1 = (float)localRectangle2D.getX();
/* 296 */     float f2 = (float)localRectangle2D.getY();
/* 297 */     float f3 = (float)localRectangle2D.getWidth();
/* 298 */     float f4 = (float)localRectangle2D.getHeight();
/* 299 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1684492F, 1.0F }, new Color[] { this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient5(Shape paramShape)
/*     */   {
/* 307 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 308 */     float f1 = (float)localRectangle2D.getX();
/* 309 */     float f2 = (float)localRectangle2D.getY();
/* 310 */     float f3 = (float)localRectangle2D.getWidth();
/* 311 */     float f4 = (float)localRectangle2D.getHeight();
/* 312 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.4957386F, 0.9914773F }, new Color[] { this.color8, decodeColor(this.color8, this.color15, 0.5F), this.color15 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.SpinnerPanelSpinnerFormattedTextFieldPainter
 * JD-Core Version:    0.6.2
 */
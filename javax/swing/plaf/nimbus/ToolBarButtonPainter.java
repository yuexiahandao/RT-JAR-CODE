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
/*     */ final class ToolBarButtonPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int BACKGROUND_FOCUSED = 2;
/*     */   static final int BACKGROUND_MOUSEOVER = 3;
/*     */   static final int BACKGROUND_MOUSEOVER_FOCUSED = 4;
/*     */   static final int BACKGROUND_PRESSED = 5;
/*     */   static final int BACKGROUND_PRESSED_FOCUSED = 6;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  51 */   private Path2D path = new Path2D.Float();
/*  52 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  53 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  54 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  59 */   private Color color1 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  60 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.06885965F, -0.3686275F, -153);
/*  61 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.0F, -0.02097408F, -0.2196078F, 0);
/*  62 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.0F, 0.1116959F, -0.5333334F, 0);
/*  63 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.1065893F, 0.2509804F, 0);
/*  64 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.0F, -0.09852631F, 0.235294F, 0);
/*  65 */   private Color color7 = decodeColor("nimbusBlueGrey", 0.0F, -0.07333623F, 0.2039216F, 0);
/*  66 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  67 */   private Color color9 = decodeColor("nimbusBlueGrey", -0.0050505F, -0.0596004F, 0.1019608F, 0);
/*  68 */   private Color color10 = decodeColor("nimbusBlueGrey", -0.00854701F, -0.04772438F, 0.06666666F, 0);
/*  69 */   private Color color11 = decodeColor("nimbusBlueGrey", -0.002777755F, -0.001830667F, -0.0235294F, 0);
/*  70 */   private Color color12 = decodeColor("nimbusBlueGrey", -0.002777755F, -0.0212406F, 0.1333333F, 0);
/*  71 */   private Color color13 = decodeColor("nimbusBlueGrey", 0.005555511F, -0.03084504F, 0.2392157F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ToolBarButtonPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  79 */     this.state = paramInt;
/*  80 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  86 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  89 */     switch (this.state) { case 2:
/*  90 */       paintBackgroundFocused(paramGraphics2D); break;
/*     */     case 3:
/*  91 */       paintBackgroundMouseOver(paramGraphics2D); break;
/*     */     case 4:
/*  92 */       paintBackgroundMouseOverAndFocused(paramGraphics2D); break;
/*     */     case 5:
/*  93 */       paintBackgroundPressed(paramGraphics2D); break;
/*     */     case 6:
/*  94 */       paintBackgroundPressedAndFocused(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 103 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundFocused(Graphics2D paramGraphics2D) {
/* 107 */     this.path = decodePath1();
/* 108 */     paramGraphics2D.setPaint(this.color1);
/* 109 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 114 */     this.roundRect = decodeRoundRect1();
/* 115 */     paramGraphics2D.setPaint(this.color2);
/* 116 */     paramGraphics2D.fill(this.roundRect);
/* 117 */     this.roundRect = decodeRoundRect2();
/* 118 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/* 119 */     paramGraphics2D.fill(this.roundRect);
/* 120 */     this.roundRect = decodeRoundRect3();
/* 121 */     paramGraphics2D.setPaint(decodeGradient2(this.roundRect));
/* 122 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOverAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 127 */     this.roundRect = decodeRoundRect4();
/* 128 */     paramGraphics2D.setPaint(this.color1);
/* 129 */     paramGraphics2D.fill(this.roundRect);
/* 130 */     this.roundRect = decodeRoundRect2();
/* 131 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/* 132 */     paramGraphics2D.fill(this.roundRect);
/* 133 */     this.roundRect = decodeRoundRect3();
/* 134 */     paramGraphics2D.setPaint(decodeGradient2(this.roundRect));
/* 135 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressed(Graphics2D paramGraphics2D)
/*     */   {
/* 140 */     this.roundRect = decodeRoundRect1();
/* 141 */     paramGraphics2D.setPaint(this.color2);
/* 142 */     paramGraphics2D.fill(this.roundRect);
/* 143 */     this.roundRect = decodeRoundRect2();
/* 144 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/* 145 */     paramGraphics2D.fill(this.roundRect);
/* 146 */     this.roundRect = decodeRoundRect3();
/* 147 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 148 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundPressedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 153 */     this.roundRect = decodeRoundRect4();
/* 154 */     paramGraphics2D.setPaint(this.color1);
/* 155 */     paramGraphics2D.fill(this.roundRect);
/* 156 */     this.roundRect = decodeRoundRect2();
/* 157 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/* 158 */     paramGraphics2D.fill(this.roundRect);
/* 159 */     this.roundRect = decodeRoundRect3();
/* 160 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 161 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 168 */     this.path.reset();
/* 169 */     this.path.moveTo(decodeX(1.413374F), decodeY(0.12F));
/* 170 */     this.path.lineTo(decodeX(1.989362F), decodeY(0.12F));
/* 171 */     this.path.curveTo(decodeAnchorX(1.989362F, 3.0F), decodeAnchorY(0.12F, 0.0F), decodeAnchorX(2.885715F, 0.0F), decodeAnchorY(1.043478F, -3.0F), decodeX(2.885715F), decodeY(1.043478F));
/* 172 */     this.path.lineTo(decodeX(2.9F), decodeY(1.956522F));
/* 173 */     this.path.curveTo(decodeAnchorX(2.9F, 0.0F), decodeAnchorY(1.956522F, 3.0F), decodeAnchorX(1.989362F, 3.0F), decodeAnchorY(2.871429F, 0.0F), decodeX(1.989362F), decodeY(2.871429F));
/* 174 */     this.path.lineTo(decodeX(1.010638F), decodeY(2.871429F));
/* 175 */     this.path.curveTo(decodeAnchorX(1.010638F, -3.0F), decodeAnchorY(2.871429F, 0.0F), decodeAnchorX(0.12F, 0.0F), decodeAnchorY(1.956522F, 3.0F), decodeX(0.12F), decodeY(1.956522F));
/* 176 */     this.path.lineTo(decodeX(0.12F), decodeY(1.046584F));
/* 177 */     this.path.curveTo(decodeAnchorX(0.12F, 0.0F), decodeAnchorY(1.046584F, -3.0F), decodeAnchorX(1.010638F, -3.0F), decodeAnchorY(0.12F, 0.0F), decodeX(1.010638F), decodeY(0.12F));
/* 178 */     this.path.lineTo(decodeX(1.414894F), decodeY(0.12F));
/* 179 */     this.path.lineTo(decodeX(1.414894F), decodeY(0.4857143F));
/* 180 */     this.path.lineTo(decodeX(1.010638F), decodeY(0.4857143F));
/* 181 */     this.path.curveTo(decodeAnchorX(1.010638F, -1.928572F), decodeAnchorY(0.4857143F, 0.0F), decodeAnchorX(0.4714286F, -0.04427948F), decodeAnchorY(1.040373F, -2.429218F), decodeX(0.4714286F), decodeY(1.040373F));
/* 182 */     this.path.lineTo(decodeX(0.4714286F), decodeY(1.956522F));
/* 183 */     this.path.curveTo(decodeAnchorX(0.4714286F, 0.0F), decodeAnchorY(1.956522F, 2.214286F), decodeAnchorX(1.010638F, -1.785714F), decodeAnchorY(2.514286F, 0.0F), decodeX(1.010638F), decodeY(2.514286F));
/* 184 */     this.path.lineTo(decodeX(1.989362F), decodeY(2.514286F));
/* 185 */     this.path.curveTo(decodeAnchorX(1.989362F, 2.071429F), decodeAnchorY(2.514286F, 0.0F), decodeAnchorX(2.5F, 0.0F), decodeAnchorY(1.956522F, 2.214286F), decodeX(2.5F), decodeY(1.956522F));
/* 186 */     this.path.lineTo(decodeX(2.514285F), decodeY(1.043478F));
/* 187 */     this.path.curveTo(decodeAnchorX(2.514285F, 0.0F), decodeAnchorY(1.043478F, -2.142857F), decodeAnchorX(1.990122F, 2.142857F), decodeAnchorY(0.4714286F, 0.0F), decodeX(1.990122F), decodeY(0.4714286F));
/* 188 */     this.path.lineTo(decodeX(1.414894F), decodeY(0.4857143F));
/* 189 */     this.path.lineTo(decodeX(1.413374F), decodeY(0.12F));
/* 190 */     this.path.closePath();
/* 191 */     return this.path;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect1() {
/* 195 */     this.roundRect.setRoundRect(decodeX(0.4F), decodeY(0.6F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.8F) - decodeY(0.6F), 12.0D, 12.0D);
/*     */ 
/* 200 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect2() {
/* 204 */     this.roundRect.setRoundRect(decodeX(0.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.4F), 12.0D, 12.0D);
/*     */ 
/* 209 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect3() {
/* 213 */     this.roundRect.setRoundRect(decodeX(0.6F), decodeY(0.6F), decodeX(2.4F) - decodeX(0.6F), decodeY(2.4F) - decodeY(0.6F), 9.0D, 9.0D);
/*     */ 
/* 218 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect4() {
/* 222 */     this.roundRect.setRoundRect(decodeX(0.12F), decodeY(0.12F), decodeX(2.88F) - decodeX(0.12F), decodeY(2.88F) - decodeY(0.12F), 13.0D, 13.0D);
/*     */ 
/* 227 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 233 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 234 */     float f1 = (float)localRectangle2D.getX();
/* 235 */     float f2 = (float)localRectangle2D.getY();
/* 236 */     float f3 = (float)localRectangle2D.getWidth();
/* 237 */     float f4 = (float)localRectangle2D.getHeight();
/* 238 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.09F, 0.52F, 0.95F }, new Color[] { this.color3, decodeColor(this.color3, this.color4, 0.5F), this.color4 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 246 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 247 */     float f1 = (float)localRectangle2D.getX();
/* 248 */     float f2 = (float)localRectangle2D.getY();
/* 249 */     float f3 = (float)localRectangle2D.getWidth();
/* 250 */     float f4 = (float)localRectangle2D.getHeight();
/* 251 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.03F, 0.06F, 0.33F, 0.6F, 0.65F, 0.7F, 0.825F, 0.95F, 0.975F, 1.0F }, new Color[] { this.color5, decodeColor(this.color5, this.color6, 0.5F), this.color6, decodeColor(this.color6, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color7, 0.5F), this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8, decodeColor(this.color8, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 267 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 268 */     float f1 = (float)localRectangle2D.getX();
/* 269 */     float f2 = (float)localRectangle2D.getY();
/* 270 */     float f3 = (float)localRectangle2D.getWidth();
/* 271 */     float f4 = (float)localRectangle2D.getHeight();
/* 272 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.03F, 0.06F, 0.33F, 0.6F, 0.65F, 0.7F, 0.825F, 0.95F, 0.975F, 1.0F }, new Color[] { this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11, decodeColor(this.color11, this.color11, 0.5F), this.color11, decodeColor(this.color11, this.color12, 0.5F), this.color12, decodeColor(this.color12, this.color13, 0.5F), this.color13 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ToolBarButtonPainter
 * JD-Core Version:    0.6.2
 */
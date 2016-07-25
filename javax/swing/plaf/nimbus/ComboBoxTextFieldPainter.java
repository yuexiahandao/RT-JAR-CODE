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
/*     */ final class ComboBoxTextFieldPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_SELECTED = 3;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  48 */   private Path2D path = new Path2D.Float();
/*  49 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  50 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  51 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  56 */   private Color color1 = decodeColor("nimbusBlueGrey", -0.611111F, -0.1105263F, -0.7450981F, -237);
/*  57 */   private Color color2 = decodeColor("nimbusBlueGrey", -0.00694442F, -0.071879F, 0.06666666F, 0);
/*  58 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.0770335F, 0.0745098F, 0);
/*  59 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07968931F, 0.145098F, 0);
/*  60 */   private Color color5 = decodeColor("nimbusBlueGrey", 0.007936537F, -0.07856284F, 0.1137255F, 0);
/*  61 */   private Color color6 = decodeColor("nimbusBase", 0.04039598F, -0.6031562F, 0.2941176F, 0);
/*  62 */   private Color color7 = decodeColor("nimbusBase", 0.01658648F, -0.6051466F, 0.3490196F, 0);
/*  63 */   private Color color8 = decodeColor("nimbusBlueGrey", -0.02777779F, -0.0965403F, -0.1843137F, 0);
/*  64 */   private Color color9 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.104877F, -0.05098039F, 0);
/*  65 */   private Color color10 = decodeColor("nimbusLightBackground", 0.6666667F, 0.00490196F, -0.2F, 0);
/*  66 */   private Color color11 = decodeColor("nimbusLightBackground", 0.0F, 0.0F, 0.0F, 0);
/*  67 */   private Color color12 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.105345F, 0.01176471F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ComboBoxTextFieldPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  75 */     this.state = paramInt;
/*  76 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  82 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  85 */     switch (this.state) { case 1:
/*  86 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/*  87 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 3:
/*  88 */       paintBackgroundSelected(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  97 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/* 101 */     this.rect = decodeRect1();
/* 102 */     paramGraphics2D.setPaint(this.color1);
/* 103 */     paramGraphics2D.fill(this.rect);
/* 104 */     this.rect = decodeRect2();
/* 105 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 106 */     paramGraphics2D.fill(this.rect);
/* 107 */     this.rect = decodeRect3();
/* 108 */     paramGraphics2D.setPaint(decodeGradient2(this.rect));
/* 109 */     paramGraphics2D.fill(this.rect);
/* 110 */     this.rect = decodeRect4();
/* 111 */     paramGraphics2D.setPaint(this.color6);
/* 112 */     paramGraphics2D.fill(this.rect);
/* 113 */     this.rect = decodeRect5();
/* 114 */     paramGraphics2D.setPaint(this.color7);
/* 115 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 120 */     this.rect = decodeRect1();
/* 121 */     paramGraphics2D.setPaint(this.color1);
/* 122 */     paramGraphics2D.fill(this.rect);
/* 123 */     this.rect = decodeRect2();
/* 124 */     paramGraphics2D.setPaint(decodeGradient3(this.rect));
/* 125 */     paramGraphics2D.fill(this.rect);
/* 126 */     this.rect = decodeRect3();
/* 127 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 128 */     paramGraphics2D.fill(this.rect);
/* 129 */     this.rect = decodeRect4();
/* 130 */     paramGraphics2D.setPaint(this.color12);
/* 131 */     paramGraphics2D.fill(this.rect);
/* 132 */     this.rect = decodeRect5();
/* 133 */     paramGraphics2D.setPaint(this.color11);
/* 134 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 139 */     this.rect = decodeRect1();
/* 140 */     paramGraphics2D.setPaint(this.color1);
/* 141 */     paramGraphics2D.fill(this.rect);
/* 142 */     this.rect = decodeRect2();
/* 143 */     paramGraphics2D.setPaint(decodeGradient3(this.rect));
/* 144 */     paramGraphics2D.fill(this.rect);
/* 145 */     this.rect = decodeRect3();
/* 146 */     paramGraphics2D.setPaint(decodeGradient4(this.rect));
/* 147 */     paramGraphics2D.fill(this.rect);
/* 148 */     this.rect = decodeRect4();
/* 149 */     paramGraphics2D.setPaint(this.color12);
/* 150 */     paramGraphics2D.fill(this.rect);
/* 151 */     this.rect = decodeRect5();
/* 152 */     paramGraphics2D.setPaint(this.color11);
/* 153 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 160 */     this.rect.setRect(decodeX(0.6666667F), decodeY(2.333333F), decodeX(3.0F) - decodeX(0.6666667F), decodeY(2.666667F) - decodeY(2.333333F));
/*     */ 
/* 164 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 168 */     this.rect.setRect(decodeX(0.6666667F), decodeY(0.4F), decodeX(3.0F) - decodeX(0.6666667F), decodeY(1.0F) - decodeY(0.4F));
/*     */ 
/* 172 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 176 */     this.rect.setRect(decodeX(1.0F), decodeY(0.6F), decodeX(3.0F) - decodeX(1.0F), decodeY(1.0F) - decodeY(0.6F));
/*     */ 
/* 180 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect4() {
/* 184 */     this.rect.setRect(decodeX(0.6666667F), decodeY(1.0F), decodeX(3.0F) - decodeX(0.6666667F), decodeY(2.333333F) - decodeY(1.0F));
/*     */ 
/* 188 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect5() {
/* 192 */     this.rect.setRect(decodeX(1.0F), decodeY(1.0F), decodeX(3.0F) - decodeX(1.0F), decodeY(2.0F) - decodeY(1.0F));
/*     */ 
/* 196 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 202 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 203 */     float f1 = (float)localRectangle2D.getX();
/* 204 */     float f2 = (float)localRectangle2D.getY();
/* 205 */     float f3 = (float)localRectangle2D.getWidth();
/* 206 */     float f4 = (float)localRectangle2D.getHeight();
/* 207 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 215 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 216 */     float f1 = (float)localRectangle2D.getX();
/* 217 */     float f2 = (float)localRectangle2D.getY();
/* 218 */     float f3 = (float)localRectangle2D.getWidth();
/* 219 */     float f4 = (float)localRectangle2D.getHeight();
/* 220 */     return decodeGradient(0.5F * f3 + f1, 1.0F * f4 + f2, 0.5F * f3 + f1, 0.0F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 228 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 229 */     float f1 = (float)localRectangle2D.getX();
/* 230 */     float f2 = (float)localRectangle2D.getY();
/* 231 */     float f3 = (float)localRectangle2D.getWidth();
/* 232 */     float f4 = (float)localRectangle2D.getHeight();
/* 233 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.4957386F, 0.9914773F }, new Color[] { this.color8, decodeColor(this.color8, this.color9, 0.5F), this.color9 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 241 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 242 */     float f1 = (float)localRectangle2D.getX();
/* 243 */     float f2 = (float)localRectangle2D.getY();
/* 244 */     float f3 = (float)localRectangle2D.getWidth();
/* 245 */     float f4 = (float)localRectangle2D.getHeight();
/* 246 */     return decodeGradient(0.5F * f3 + f1, 0.0F * f4 + f2, 0.5F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.1F, 0.5F, 0.9F }, new Color[] { this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ComboBoxTextFieldPainter
 * JD-Core Version:    0.6.2
 */
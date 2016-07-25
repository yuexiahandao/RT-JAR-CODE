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
/*     */ final class SliderTrackPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  47 */   private Path2D path = new Path2D.Float();
/*  48 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  49 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  50 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  55 */   private Color color1 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -245);
/*  56 */   private Color color2 = decodeColor("nimbusBlueGrey", 0.005555511F, -0.06126523F, 0.05098039F, 0);
/*  57 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.0101011F, -0.05983507F, 0.1058824F, 0);
/*  58 */   private Color color4 = decodeColor("nimbusBlueGrey", -0.0111111F, -0.06198263F, 0.06274509F, 0);
/*  59 */   private Color color5 = decodeColor("nimbusBlueGrey", -0.0050505F, -0.05863952F, 0.08627451F, 0);
/*  60 */   private Color color6 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, -111);
/*  61 */   private Color color7 = decodeColor("nimbusBlueGrey", 0.0F, -0.03409319F, -0.1294118F, 0);
/*  62 */   private Color color8 = decodeColor("nimbusBlueGrey", 0.0111111F, -0.02382112F, -0.06666666F, 0);
/*  63 */   private Color color9 = decodeColor("nimbusBlueGrey", -0.00854701F, -0.03314536F, -0.08627451F, 0);
/*  64 */   private Color color10 = decodeColor("nimbusBlueGrey", 0.004273474F, -0.04025605F, -0.01960784F, 0);
/*  65 */   private Color color11 = decodeColor("nimbusBlueGrey", 0.0F, -0.0362689F, 0.04705882F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public SliderTrackPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  73 */     this.state = paramInt;
/*  74 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  80 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  83 */     switch (this.state) { case 1:
/*  84 */       paintBackgroundDisabled(paramGraphics2D); break;
/*     */     case 2:
/*  85 */       paintBackgroundEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  94 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundDisabled(Graphics2D paramGraphics2D) {
/*  98 */     this.roundRect = decodeRoundRect1();
/*  99 */     paramGraphics2D.setPaint(this.color1);
/* 100 */     paramGraphics2D.fill(this.roundRect);
/* 101 */     this.roundRect = decodeRoundRect2();
/* 102 */     paramGraphics2D.setPaint(decodeGradient1(this.roundRect));
/* 103 */     paramGraphics2D.fill(this.roundRect);
/* 104 */     this.roundRect = decodeRoundRect3();
/* 105 */     paramGraphics2D.setPaint(decodeGradient2(this.roundRect));
/* 106 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 111 */     this.roundRect = decodeRoundRect4();
/* 112 */     paramGraphics2D.setPaint(this.color6);
/* 113 */     paramGraphics2D.fill(this.roundRect);
/* 114 */     this.roundRect = decodeRoundRect2();
/* 115 */     paramGraphics2D.setPaint(decodeGradient3(this.roundRect));
/* 116 */     paramGraphics2D.fill(this.roundRect);
/* 117 */     this.roundRect = decodeRoundRect5();
/* 118 */     paramGraphics2D.setPaint(decodeGradient4(this.roundRect));
/* 119 */     paramGraphics2D.fill(this.roundRect);
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect1()
/*     */   {
/* 126 */     this.roundRect.setRoundRect(decodeX(0.2F), decodeY(1.6F), decodeX(2.8F) - decodeX(0.2F), decodeY(2.833333F) - decodeY(1.6F), 8.705882072448731D, 8.705882072448731D);
/*     */ 
/* 131 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect2() {
/* 135 */     this.roundRect.setRoundRect(decodeX(0.0F), decodeY(1.0F), decodeX(3.0F) - decodeX(0.0F), decodeY(2.0F) - decodeY(1.0F), 4.941176414489746D, 4.941176414489746D);
/*     */ 
/* 140 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect3() {
/* 144 */     this.roundRect.setRoundRect(decodeX(0.2941176F), decodeY(1.2F), decodeX(2.705882F) - decodeX(0.2941176F), decodeY(2.0F) - decodeY(1.2F), 4.0D, 4.0D);
/*     */ 
/* 149 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect4() {
/* 153 */     this.roundRect.setRoundRect(decodeX(0.2F), decodeY(1.6F), decodeX(2.8F) - decodeX(0.2F), decodeY(2.166667F) - decodeY(1.6F), 8.705882072448731D, 8.705882072448731D);
/*     */ 
/* 158 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private RoundRectangle2D decodeRoundRect5() {
/* 162 */     this.roundRect.setRoundRect(decodeX(0.2882353F), decodeY(1.2F), decodeX(2.7F) - decodeX(0.2882353F), decodeY(2.0F) - decodeY(1.2F), 4.0D, 4.0D);
/*     */ 
/* 167 */     return this.roundRect;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 173 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 174 */     float f1 = (float)localRectangle2D.getX();
/* 175 */     float f2 = (float)localRectangle2D.getY();
/* 176 */     float f3 = (float)localRectangle2D.getWidth();
/* 177 */     float f4 = (float)localRectangle2D.getHeight();
/* 178 */     return decodeGradient(0.25F * f3 + f1, 0.07647059F * f4 + f2, 0.25F * f3 + f1, 0.9117647F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient2(Shape paramShape)
/*     */   {
/* 186 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 187 */     float f1 = (float)localRectangle2D.getX();
/* 188 */     float f2 = (float)localRectangle2D.getY();
/* 189 */     float f3 = (float)localRectangle2D.getWidth();
/* 190 */     float f4 = (float)localRectangle2D.getHeight();
/* 191 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.25F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1377005F, 0.2754011F, 0.6377006F, 1.0F }, new Color[] { this.color4, decodeColor(this.color4, this.color5, 0.5F), this.color5, decodeColor(this.color5, this.color3, 0.5F), this.color3 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient3(Shape paramShape)
/*     */   {
/* 201 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 202 */     float f1 = (float)localRectangle2D.getX();
/* 203 */     float f2 = (float)localRectangle2D.getY();
/* 204 */     float f3 = (float)localRectangle2D.getWidth();
/* 205 */     float f4 = (float)localRectangle2D.getHeight();
/* 206 */     return decodeGradient(0.25F * f3 + f1, 0.07647059F * f4 + f2, 0.25F * f3 + f1, 0.9117647F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color7, decodeColor(this.color7, this.color8, 0.5F), this.color8 });
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient4(Shape paramShape)
/*     */   {
/* 214 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 215 */     float f1 = (float)localRectangle2D.getX();
/* 216 */     float f2 = (float)localRectangle2D.getY();
/* 217 */     float f3 = (float)localRectangle2D.getWidth();
/* 218 */     float f4 = (float)localRectangle2D.getHeight();
/* 219 */     return decodeGradient(0.25F * f3 + f1, 0.0F * f4 + f2, 0.25F * f3 + f1, 1.0F * f4 + f2, new float[] { 0.0F, 0.1377005F, 0.2754011F, 0.4906417F, 0.7058824F }, new Color[] { this.color9, decodeColor(this.color9, this.color10, 0.5F), this.color10, decodeColor(this.color10, this.color11, 0.5F), this.color11 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.SliderTrackPainter
 * JD-Core Version:    0.6.2
 */
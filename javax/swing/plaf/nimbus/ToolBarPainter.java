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
/*     */ final class ToolBarPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BORDER_NORTH = 1;
/*     */   static final int BORDER_SOUTH = 2;
/*     */   static final int BORDER_EAST = 3;
/*     */   static final int BORDER_WEST = 4;
/*     */   static final int HANDLEICON_ENABLED = 5;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  50 */   private Path2D path = new Path2D.Float();
/*  51 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  52 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  53 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  58 */   private Color color1 = decodeColor("nimbusBorder", 0.0F, 0.0F, 0.0F, 0);
/*  59 */   private Color color2 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*  60 */   private Color color3 = decodeColor("nimbusBlueGrey", -0.00694442F, -0.07399663F, 0.1137255F, 0);
/*  61 */   private Color color4 = decodeColor("nimbusBorder", 0.0F, -0.02967563F, 0.1098039F, 0);
/*  62 */   private Color color5 = decodeColor("nimbusBlueGrey", -0.00854701F, -0.03494492F, -0.07058823F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ToolBarPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  70 */     this.state = paramInt;
/*  71 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  77 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  80 */     switch (this.state) { case 1:
/*  81 */       paintBorderNorth(paramGraphics2D); break;
/*     */     case 2:
/*  82 */       paintBorderSouth(paramGraphics2D); break;
/*     */     case 3:
/*  83 */       paintBorderEast(paramGraphics2D); break;
/*     */     case 4:
/*  84 */       paintBorderWest(paramGraphics2D); break;
/*     */     case 5:
/*  85 */       painthandleIconEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  94 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBorderNorth(Graphics2D paramGraphics2D) {
/*  98 */     this.rect = decodeRect1();
/*  99 */     paramGraphics2D.setPaint(this.color1);
/* 100 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBorderSouth(Graphics2D paramGraphics2D)
/*     */   {
/* 105 */     this.rect = decodeRect2();
/* 106 */     paramGraphics2D.setPaint(this.color1);
/* 107 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBorderEast(Graphics2D paramGraphics2D)
/*     */   {
/* 112 */     this.rect = decodeRect2();
/* 113 */     paramGraphics2D.setPaint(this.color1);
/* 114 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBorderWest(Graphics2D paramGraphics2D)
/*     */   {
/* 119 */     this.rect = decodeRect1();
/* 120 */     paramGraphics2D.setPaint(this.color1);
/* 121 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void painthandleIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 126 */     this.rect = decodeRect3();
/* 127 */     paramGraphics2D.setPaint(decodeGradient1(this.rect));
/* 128 */     paramGraphics2D.fill(this.rect);
/* 129 */     this.rect = decodeRect4();
/* 130 */     paramGraphics2D.setPaint(this.color4);
/* 131 */     paramGraphics2D.fill(this.rect);
/* 132 */     this.path = decodePath1();
/* 133 */     paramGraphics2D.setPaint(this.color5);
/* 134 */     paramGraphics2D.fill(this.path);
/* 135 */     this.path = decodePath2();
/* 136 */     paramGraphics2D.setPaint(this.color5);
/* 137 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 144 */     this.rect.setRect(decodeX(1.0F), decodeY(2.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(3.0F) - decodeY(2.0F));
/*     */ 
/* 148 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 152 */     this.rect.setRect(decodeX(1.0F), decodeY(0.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(1.0F) - decodeY(0.0F));
/*     */ 
/* 156 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 160 */     this.rect.setRect(decodeX(0.0F), decodeY(0.0F), decodeX(2.8F) - decodeX(0.0F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 164 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect4() {
/* 168 */     this.rect.setRect(decodeX(2.8F), decodeY(0.0F), decodeX(3.0F) - decodeX(2.8F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 172 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 176 */     this.path.reset();
/* 177 */     this.path.moveTo(decodeX(0.0F), decodeY(0.0F));
/* 178 */     this.path.lineTo(decodeX(0.0F), decodeY(0.4F));
/* 179 */     this.path.lineTo(decodeX(0.4F), decodeY(0.0F));
/* 180 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 181 */     this.path.closePath();
/* 182 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 186 */     this.path.reset();
/* 187 */     this.path.moveTo(decodeX(0.0F), decodeY(3.0F));
/* 188 */     this.path.lineTo(decodeX(0.0F), decodeY(2.6F));
/* 189 */     this.path.lineTo(decodeX(0.4F), decodeY(3.0F));
/* 190 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 191 */     this.path.closePath();
/* 192 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Paint decodeGradient1(Shape paramShape)
/*     */   {
/* 198 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 199 */     float f1 = (float)localRectangle2D.getX();
/* 200 */     float f2 = (float)localRectangle2D.getY();
/* 201 */     float f3 = (float)localRectangle2D.getWidth();
/* 202 */     float f4 = (float)localRectangle2D.getHeight();
/* 203 */     return decodeGradient(0.0F * f3 + f1, 0.5F * f4 + f2, 1.0F * f3 + f1, 0.5F * f4 + f2, new float[] { 0.0F, 0.5F, 1.0F }, new Color[] { this.color2, decodeColor(this.color2, this.color3, 0.5F), this.color3 });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ToolBarPainter
 * JD-Core Version:    0.6.2
 */
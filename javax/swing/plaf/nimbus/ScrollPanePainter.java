/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
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
/*     */ final class ScrollPanePainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int BORDER_ENABLED_FOCUSED = 2;
/*     */   static final int BORDER_ENABLED = 3;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  48 */   private Path2D path = new Path2D.Float();
/*  49 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  50 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  51 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  56 */   private Color color1 = decodeColor("nimbusBorder", 0.0F, 0.0F, 0.0F, 0);
/*  57 */   private Color color2 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ScrollPanePainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  65 */     this.state = paramInt;
/*  66 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  72 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  75 */     switch (this.state) { case 2:
/*  76 */       paintBorderEnabledAndFocused(paramGraphics2D); break;
/*     */     case 3:
/*  77 */       paintBorderEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  86 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBorderEnabledAndFocused(Graphics2D paramGraphics2D) {
/*  90 */     this.rect = decodeRect1();
/*  91 */     paramGraphics2D.setPaint(this.color1);
/*  92 */     paramGraphics2D.fill(this.rect);
/*  93 */     this.rect = decodeRect2();
/*  94 */     paramGraphics2D.setPaint(this.color1);
/*  95 */     paramGraphics2D.fill(this.rect);
/*  96 */     this.rect = decodeRect3();
/*  97 */     paramGraphics2D.setPaint(this.color1);
/*  98 */     paramGraphics2D.fill(this.rect);
/*  99 */     this.rect = decodeRect4();
/* 100 */     paramGraphics2D.setPaint(this.color1);
/* 101 */     paramGraphics2D.fill(this.rect);
/* 102 */     this.path = decodePath1();
/* 103 */     paramGraphics2D.setPaint(this.color2);
/* 104 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBorderEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 109 */     this.rect = decodeRect1();
/* 110 */     paramGraphics2D.setPaint(this.color1);
/* 111 */     paramGraphics2D.fill(this.rect);
/* 112 */     this.rect = decodeRect2();
/* 113 */     paramGraphics2D.setPaint(this.color1);
/* 114 */     paramGraphics2D.fill(this.rect);
/* 115 */     this.rect = decodeRect3();
/* 116 */     paramGraphics2D.setPaint(this.color1);
/* 117 */     paramGraphics2D.fill(this.rect);
/* 118 */     this.rect = decodeRect4();
/* 119 */     paramGraphics2D.setPaint(this.color1);
/* 120 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 127 */     this.rect.setRect(decodeX(0.6F), decodeY(0.4F), decodeX(2.4F) - decodeX(0.6F), decodeY(0.6F) - decodeY(0.4F));
/*     */ 
/* 131 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect2() {
/* 135 */     this.rect.setRect(decodeX(0.4F), decodeY(0.4F), decodeX(0.6F) - decodeX(0.4F), decodeY(2.6F) - decodeY(0.4F));
/*     */ 
/* 139 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect3() {
/* 143 */     this.rect.setRect(decodeX(2.4F), decodeY(0.4F), decodeX(2.6F) - decodeX(2.4F), decodeY(2.6F) - decodeY(0.4F));
/*     */ 
/* 147 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect4() {
/* 151 */     this.rect.setRect(decodeX(0.6F), decodeY(2.4F), decodeX(2.4F) - decodeX(0.6F), decodeY(2.6F) - decodeY(2.4F));
/*     */ 
/* 155 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 159 */     this.path.reset();
/* 160 */     this.path.moveTo(decodeX(0.4F), decodeY(0.4F));
/* 161 */     this.path.lineTo(decodeX(0.4F), decodeY(2.6F));
/* 162 */     this.path.lineTo(decodeX(2.6F), decodeY(2.6F));
/* 163 */     this.path.lineTo(decodeX(2.6F), decodeY(0.4F));
/* 164 */     this.path.curveTo(decodeAnchorX(2.6F, 0.0F), decodeAnchorY(0.4F, 0.0F), decodeAnchorX(2.88F, 0.1F), decodeAnchorY(0.4F, 0.0F), decodeX(2.88F), decodeY(0.4F));
/* 165 */     this.path.curveTo(decodeAnchorX(2.88F, 0.1F), decodeAnchorY(0.4F, 0.0F), decodeAnchorX(2.88F, 0.0F), decodeAnchorY(2.88F, 0.0F), decodeX(2.88F), decodeY(2.88F));
/* 166 */     this.path.lineTo(decodeX(0.12F), decodeY(2.88F));
/* 167 */     this.path.lineTo(decodeX(0.12F), decodeY(0.12F));
/* 168 */     this.path.lineTo(decodeX(2.88F), decodeY(0.12F));
/* 169 */     this.path.lineTo(decodeX(2.88F), decodeY(0.4F));
/* 170 */     this.path.lineTo(decodeX(0.4F), decodeY(0.4F));
/* 171 */     this.path.closePath();
/* 172 */     return this.path;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ScrollPanePainter
 * JD-Core Version:    0.6.2
 */
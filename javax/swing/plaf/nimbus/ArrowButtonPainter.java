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
/*     */ final class ArrowButtonPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int FOREGROUND_DISABLED = 2;
/*     */   static final int FOREGROUND_ENABLED = 3;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  48 */   private Path2D path = new Path2D.Float();
/*  49 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  50 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  51 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  56 */   private Color color1 = decodeColor("nimbusBase", 0.0274089F, -0.5739166F, 0.14902F, 0);
/*  57 */   private Color color2 = decodeColor("nimbusBase", -0.5786517F, -0.6357143F, -0.3725491F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public ArrowButtonPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
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
/*  76 */       paintForegroundDisabled(paramGraphics2D); break;
/*     */     case 3:
/*  77 */       paintForegroundEnabled(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  86 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintForegroundDisabled(Graphics2D paramGraphics2D) {
/*  90 */     this.path = decodePath1();
/*  91 */     paramGraphics2D.setPaint(this.color1);
/*  92 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintForegroundEnabled(Graphics2D paramGraphics2D)
/*     */   {
/*  97 */     this.path = decodePath1();
/*  98 */     paramGraphics2D.setPaint(this.color2);
/*  99 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 106 */     this.path.reset();
/* 107 */     this.path.moveTo(decodeX(1.8F), decodeY(1.2F));
/* 108 */     this.path.lineTo(decodeX(1.2F), decodeY(1.5F));
/* 109 */     this.path.lineTo(decodeX(1.8F), decodeY(1.8F));
/* 110 */     this.path.lineTo(decodeX(1.8F), decodeY(1.2F));
/* 111 */     this.path.closePath();
/* 112 */     return this.path;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ArrowButtonPainter
 * JD-Core Version:    0.6.2
 */
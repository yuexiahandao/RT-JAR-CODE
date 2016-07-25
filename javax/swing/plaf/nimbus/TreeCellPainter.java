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
/*     */ final class TreeCellPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_ENABLED = 1;
/*     */   static final int BACKGROUND_ENABLED_FOCUSED = 2;
/*     */   static final int BACKGROUND_ENABLED_SELECTED = 3;
/*     */   static final int BACKGROUND_SELECTED_FOCUSED = 4;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  49 */   private Path2D path = new Path2D.Float();
/*  50 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  51 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  52 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  57 */   private Color color1 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*  58 */   private Color color2 = decodeColor("nimbusSelectionBackground", 0.0F, 0.0F, 0.0F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public TreeCellPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  66 */     this.state = paramInt;
/*  67 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  73 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  76 */     switch (this.state) { case 2:
/*  77 */       paintBackgroundEnabledAndFocused(paramGraphics2D); break;
/*     */     case 3:
/*  78 */       paintBackgroundEnabledAndSelected(paramGraphics2D); break;
/*     */     case 4:
/*  79 */       paintBackgroundSelectedAndFocused(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  88 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndFocused(Graphics2D paramGraphics2D) {
/*  92 */     this.path = decodePath1();
/*  93 */     paramGraphics2D.setPaint(this.color1);
/*  94 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/*  99 */     this.rect = decodeRect1();
/* 100 */     paramGraphics2D.setPaint(this.color2);
/* 101 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/* 106 */     this.rect = decodeRect1();
/* 107 */     paramGraphics2D.setPaint(this.color2);
/* 108 */     paramGraphics2D.fill(this.rect);
/* 109 */     this.path = decodePath1();
/* 110 */     paramGraphics2D.setPaint(this.color1);
/* 111 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 118 */     this.path.reset();
/* 119 */     this.path.moveTo(decodeX(0.0F), decodeY(0.0F));
/* 120 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 121 */     this.path.lineTo(decodeX(3.0F), decodeY(3.0F));
/* 122 */     this.path.lineTo(decodeX(3.0F), decodeY(0.0F));
/* 123 */     this.path.lineTo(decodeX(0.24F), decodeY(0.0F));
/* 124 */     this.path.lineTo(decodeX(0.24F), decodeY(0.24F));
/* 125 */     this.path.lineTo(decodeX(2.760001F), decodeY(0.24F));
/* 126 */     this.path.lineTo(decodeX(2.760001F), decodeY(2.76F));
/* 127 */     this.path.lineTo(decodeX(0.24F), decodeY(2.76F));
/* 128 */     this.path.lineTo(decodeX(0.24F), decodeY(0.0F));
/* 129 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 130 */     this.path.closePath();
/* 131 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1() {
/* 135 */     this.rect.setRect(decodeX(0.0F), decodeY(0.0F), decodeX(3.0F) - decodeX(0.0F), decodeY(3.0F) - decodeY(0.0F));
/*     */ 
/* 139 */     return this.rect;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TreeCellPainter
 * JD-Core Version:    0.6.2
 */
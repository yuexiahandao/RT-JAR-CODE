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
/*     */ final class TreeCellEditorPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_ENABLED_FOCUSED = 3;
/*     */   static final int BACKGROUND_SELECTED = 4;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  49 */   private Path2D path = new Path2D.Float();
/*  50 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  51 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  52 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  57 */   private Color color1 = decodeColor("nimbusBlueGrey", 0.0F, -0.01735862F, -0.1137255F, 0);
/*  58 */   private Color color2 = decodeColor("nimbusFocus", 0.0F, 0.0F, 0.0F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public TreeCellEditorPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
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
/*  77 */       paintBackgroundEnabled(paramGraphics2D); break;
/*     */     case 3:
/*  78 */       paintBackgroundEnabledAndFocused(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  87 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabled(Graphics2D paramGraphics2D) {
/*  91 */     this.path = decodePath1();
/*  92 */     paramGraphics2D.setPaint(this.color1);
/*  93 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndFocused(Graphics2D paramGraphics2D)
/*     */   {
/*  98 */     this.path = decodePath2();
/*  99 */     paramGraphics2D.setPaint(this.color2);
/* 100 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1()
/*     */   {
/* 107 */     this.path.reset();
/* 108 */     this.path.moveTo(decodeX(0.0F), decodeY(0.0F));
/* 109 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 110 */     this.path.lineTo(decodeX(3.0F), decodeY(3.0F));
/* 111 */     this.path.lineTo(decodeX(3.0F), decodeY(0.0F));
/* 112 */     this.path.lineTo(decodeX(0.2F), decodeY(0.0F));
/* 113 */     this.path.lineTo(decodeX(0.2F), decodeY(0.2F));
/* 114 */     this.path.lineTo(decodeX(2.8F), decodeY(0.2F));
/* 115 */     this.path.lineTo(decodeX(2.8F), decodeY(2.8F));
/* 116 */     this.path.lineTo(decodeX(0.2F), decodeY(2.8F));
/* 117 */     this.path.lineTo(decodeX(0.2F), decodeY(0.0F));
/* 118 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 119 */     this.path.closePath();
/* 120 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 124 */     this.path.reset();
/* 125 */     this.path.moveTo(decodeX(0.0F), decodeY(0.0F));
/* 126 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 127 */     this.path.lineTo(decodeX(3.0F), decodeY(3.0F));
/* 128 */     this.path.lineTo(decodeX(3.0F), decodeY(0.0F));
/* 129 */     this.path.lineTo(decodeX(0.24F), decodeY(0.0F));
/* 130 */     this.path.lineTo(decodeX(0.24F), decodeY(0.24F));
/* 131 */     this.path.lineTo(decodeX(2.760001F), decodeY(0.24F));
/* 132 */     this.path.lineTo(decodeX(2.760001F), decodeY(2.76F));
/* 133 */     this.path.lineTo(decodeX(0.24F), decodeY(2.76F));
/* 134 */     this.path.lineTo(decodeX(0.24F), decodeY(0.0F));
/* 135 */     this.path.lineTo(decodeX(0.0F), decodeY(0.0F));
/* 136 */     this.path.closePath();
/* 137 */     return this.path;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.TreeCellEditorPainter
 * JD-Core Version:    0.6.2
 */
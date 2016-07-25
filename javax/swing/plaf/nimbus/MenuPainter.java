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
/*     */ final class MenuPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_ENABLED_SELECTED = 3;
/*     */   static final int ARROWICON_DISABLED = 4;
/*     */   static final int ARROWICON_ENABLED = 5;
/*     */   static final int ARROWICON_ENABLED_SELECTED = 6;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  51 */   private Path2D path = new Path2D.Float();
/*  52 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  53 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  54 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  59 */   private Color color1 = decodeColor("nimbusSelection", 0.0F, 0.0F, 0.0F, 0);
/*  60 */   private Color color2 = decodeColor("nimbusBlueGrey", 0.0F, -0.08983666F, -0.1764706F, 0);
/*  61 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.09663743F, -0.462745F, 0);
/*  62 */   private Color color4 = new Color(255, 255, 255, 255);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public MenuPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  70 */     this.state = paramInt;
/*  71 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  77 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  80 */     switch (this.state) { case 3:
/*  81 */       paintBackgroundEnabledAndSelected(paramGraphics2D); break;
/*     */     case 4:
/*  82 */       paintarrowIconDisabled(paramGraphics2D); break;
/*     */     case 5:
/*  83 */       paintarrowIconEnabled(paramGraphics2D); break;
/*     */     case 6:
/*  84 */       paintarrowIconEnabledAndSelected(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  93 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundEnabledAndSelected(Graphics2D paramGraphics2D) {
/*  97 */     this.rect = decodeRect1();
/*  98 */     paramGraphics2D.setPaint(this.color1);
/*  99 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintarrowIconDisabled(Graphics2D paramGraphics2D)
/*     */   {
/* 104 */     this.path = decodePath1();
/* 105 */     paramGraphics2D.setPaint(this.color2);
/* 106 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintarrowIconEnabled(Graphics2D paramGraphics2D)
/*     */   {
/* 111 */     this.path = decodePath1();
/* 112 */     paramGraphics2D.setPaint(this.color3);
/* 113 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintarrowIconEnabledAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 118 */     this.path = decodePath2();
/* 119 */     paramGraphics2D.setPaint(this.color4);
/* 120 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 127 */     this.rect.setRect(decodeX(1.0F), decodeY(1.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(2.0F) - decodeY(1.0F));
/*     */ 
/* 131 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 135 */     this.path.reset();
/* 136 */     this.path.moveTo(decodeX(0.0F), decodeY(0.2F));
/* 137 */     this.path.lineTo(decodeX(2.75122F), decodeY(2.102439F));
/* 138 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 139 */     this.path.lineTo(decodeX(0.0F), decodeY(0.2F));
/* 140 */     this.path.closePath();
/* 141 */     return this.path;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath2() {
/* 145 */     this.path.reset();
/* 146 */     this.path.moveTo(decodeX(0.0F), decodeY(1.0F));
/* 147 */     this.path.lineTo(decodeX(1.952962F), decodeY(1.5625F));
/* 148 */     this.path.lineTo(decodeX(0.0F), decodeY(3.0F));
/* 149 */     this.path.lineTo(decodeX(0.0F), decodeY(1.0F));
/* 150 */     this.path.closePath();
/* 151 */     return this.path;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.MenuPainter
 * JD-Core Version:    0.6.2
 */
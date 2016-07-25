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
/*     */ final class CheckBoxMenuItemPainter extends AbstractRegionPainter
/*     */ {
/*     */   static final int BACKGROUND_DISABLED = 1;
/*     */   static final int BACKGROUND_ENABLED = 2;
/*     */   static final int BACKGROUND_MOUSEOVER = 3;
/*     */   static final int BACKGROUND_SELECTED_MOUSEOVER = 4;
/*     */   static final int CHECKICON_DISABLED_SELECTED = 5;
/*     */   static final int CHECKICON_ENABLED_SELECTED = 6;
/*     */   static final int CHECKICON_SELECTED_MOUSEOVER = 7;
/*     */   private int state;
/*     */   private AbstractRegionPainter.PaintContext ctx;
/*  52 */   private Path2D path = new Path2D.Float();
/*  53 */   private Rectangle2D rect = new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*  54 */   private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*  55 */   private Ellipse2D ellipse = new Ellipse2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */ 
/*  60 */   private Color color1 = decodeColor("nimbusSelection", 0.0F, 0.0F, 0.0F, 0);
/*  61 */   private Color color2 = decodeColor("nimbusBlueGrey", 0.0F, -0.08983666F, -0.1764706F, 0);
/*  62 */   private Color color3 = decodeColor("nimbusBlueGrey", 0.05555558F, -0.09682769F, -0.4588235F, 0);
/*  63 */   private Color color4 = decodeColor("nimbusBlueGrey", 0.0F, -0.1105263F, 0.254902F, 0);
/*     */   private Object[] componentColors;
/*     */ 
/*     */   public CheckBoxMenuItemPainter(AbstractRegionPainter.PaintContext paramPaintContext, int paramInt)
/*     */   {
/*  71 */     this.state = paramInt;
/*  72 */     this.ctx = paramPaintContext;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  78 */     this.componentColors = paramArrayOfObject;
/*     */ 
/*  81 */     switch (this.state) { case 3:
/*  82 */       paintBackgroundMouseOver(paramGraphics2D); break;
/*     */     case 4:
/*  83 */       paintBackgroundSelectedAndMouseOver(paramGraphics2D); break;
/*     */     case 5:
/*  84 */       paintcheckIconDisabledAndSelected(paramGraphics2D); break;
/*     */     case 6:
/*  85 */       paintcheckIconEnabledAndSelected(paramGraphics2D); break;
/*     */     case 7:
/*  86 */       paintcheckIconSelectedAndMouseOver(paramGraphics2D);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/*  95 */     return this.ctx;
/*     */   }
/*     */ 
/*     */   private void paintBackgroundMouseOver(Graphics2D paramGraphics2D) {
/*  99 */     this.rect = decodeRect1();
/* 100 */     paramGraphics2D.setPaint(this.color1);
/* 101 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintBackgroundSelectedAndMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 106 */     this.rect = decodeRect1();
/* 107 */     paramGraphics2D.setPaint(this.color1);
/* 108 */     paramGraphics2D.fill(this.rect);
/*     */   }
/*     */ 
/*     */   private void paintcheckIconDisabledAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 113 */     this.path = decodePath1();
/* 114 */     paramGraphics2D.setPaint(this.color2);
/* 115 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintcheckIconEnabledAndSelected(Graphics2D paramGraphics2D)
/*     */   {
/* 120 */     this.path = decodePath1();
/* 121 */     paramGraphics2D.setPaint(this.color3);
/* 122 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private void paintcheckIconSelectedAndMouseOver(Graphics2D paramGraphics2D)
/*     */   {
/* 127 */     this.path = decodePath1();
/* 128 */     paramGraphics2D.setPaint(this.color4);
/* 129 */     paramGraphics2D.fill(this.path);
/*     */   }
/*     */ 
/*     */   private Rectangle2D decodeRect1()
/*     */   {
/* 136 */     this.rect.setRect(decodeX(1.0F), decodeY(1.0F), decodeX(2.0F) - decodeX(1.0F), decodeY(2.0F) - decodeY(1.0F));
/*     */ 
/* 140 */     return this.rect;
/*     */   }
/*     */ 
/*     */   private Path2D decodePath1() {
/* 144 */     this.path.reset();
/* 145 */     this.path.moveTo(decodeX(0.0F), decodeY(1.5F));
/* 146 */     this.path.lineTo(decodeX(0.429268F), decodeY(1.5F));
/* 147 */     this.path.lineTo(decodeX(0.712195F), decodeY(2.478049F));
/* 148 */     this.path.lineTo(decodeX(2.592683F), decodeY(0.0F));
/* 149 */     this.path.lineTo(decodeX(3.0F), decodeY(0.0F));
/* 150 */     this.path.lineTo(decodeX(3.0F), decodeY(0.2F));
/* 151 */     this.path.lineTo(decodeX(2.831708F), decodeY(0.395122F));
/* 152 */     this.path.lineTo(decodeX(0.8F), decodeY(3.0F));
/* 153 */     this.path.lineTo(decodeX(0.5731707F), decodeY(3.0F));
/* 154 */     this.path.lineTo(decodeX(0.0F), decodeY(1.5F));
/* 155 */     this.path.closePath();
/* 156 */     return this.path;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.CheckBoxMenuItemPainter
 * JD-Core Version:    0.6.2
 */
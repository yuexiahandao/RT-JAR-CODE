/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.BufferedImage;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.border.Border;
/*     */ 
/*     */ class LoweredBorder extends AbstractRegionPainter
/*     */   implements Border
/*     */ {
/*     */   private static final int IMG_SIZE = 30;
/*     */   private static final int RADIUS = 13;
/*  48 */   private static final Insets INSETS = new Insets(10, 10, 10, 10);
/*  49 */   private static final AbstractRegionPainter.PaintContext PAINT_CONTEXT = new AbstractRegionPainter.PaintContext(INSETS, new Dimension(30, 30), false, AbstractRegionPainter.PaintContext.CacheMode.NINE_SQUARE_SCALE, 2147483647.0D, 2147483647.0D);
/*     */ 
/*     */   protected Object[] getExtendedCacheKeys(JComponent paramJComponent)
/*     */   {
/*  59 */     return paramJComponent != null ? new Object[] { paramJComponent.getBackground() } : null;
/*     */   }
/*     */ 
/*     */   protected void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/*  88 */     Color localColor = paramJComponent == null ? Color.BLACK : paramJComponent.getBackground();
/*  89 */     BufferedImage localBufferedImage1 = new BufferedImage(30, 30, 2);
/*     */ 
/*  91 */     BufferedImage localBufferedImage2 = new BufferedImage(30, 30, 2);
/*     */ 
/*  94 */     Graphics2D localGraphics2D = (Graphics2D)localBufferedImage1.getGraphics();
/*  95 */     localGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */ 
/*  97 */     localGraphics2D.setColor(localColor);
/*  98 */     localGraphics2D.fillRoundRect(2, 0, 26, 26, 13, 13);
/*  99 */     localGraphics2D.dispose();
/*     */ 
/* 101 */     InnerShadowEffect localInnerShadowEffect = new InnerShadowEffect();
/* 102 */     localInnerShadowEffect.setDistance(1);
/* 103 */     localInnerShadowEffect.setSize(3);
/* 104 */     localInnerShadowEffect.setColor(getLighter(localColor, 2.1F));
/* 105 */     localInnerShadowEffect.setAngle(90);
/* 106 */     localInnerShadowEffect.applyEffect(localBufferedImage1, localBufferedImage2, 30, 30);
/*     */ 
/* 108 */     localGraphics2D = (Graphics2D)localBufferedImage2.getGraphics();
/* 109 */     localGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */ 
/* 111 */     localGraphics2D.setClip(0, 28, 30, 1);
/* 112 */     localGraphics2D.setColor(getLighter(localColor, 0.9F));
/* 113 */     localGraphics2D.drawRoundRect(2, 1, 25, 25, 13, 13);
/* 114 */     localGraphics2D.dispose();
/*     */ 
/* 116 */     if ((paramInt1 != 30) || (paramInt2 != 30)) {
/* 117 */       ImageScalingHelper.paint(paramGraphics2D, 0, 0, paramInt1, paramInt2, localBufferedImage2, INSETS, INSETS, ImageScalingHelper.PaintType.PAINT9_STRETCH, 512);
/*     */     }
/*     */     else
/*     */     {
/* 121 */       paramGraphics2D.drawImage(localBufferedImage2, 0, 0, paramJComponent);
/*     */     }
/* 123 */     localBufferedImage1 = null;
/* 124 */     localBufferedImage2 = null;
/*     */   }
/*     */ 
/*     */   protected AbstractRegionPainter.PaintContext getPaintContext()
/*     */   {
/* 142 */     return PAINT_CONTEXT;
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent)
/*     */   {
/* 154 */     return (Insets)INSETS.clone();
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 162 */     return false;
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 178 */     JComponent localJComponent = (paramComponent instanceof JComponent) ? (JComponent)paramComponent : null;
/*     */     Object localObject;
/* 179 */     if ((paramGraphics instanceof Graphics2D)) {
/* 180 */       localObject = (Graphics2D)paramGraphics;
/* 181 */       ((Graphics2D)localObject).translate(paramInt1, paramInt2);
/* 182 */       paint((Graphics2D)localObject, localJComponent, paramInt3, paramInt4);
/* 183 */       ((Graphics2D)localObject).translate(-paramInt1, -paramInt2);
/*     */     } else {
/* 185 */       localObject = new BufferedImage(30, 30, 2);
/*     */ 
/* 187 */       Graphics2D localGraphics2D = (Graphics2D)((BufferedImage)localObject).getGraphics();
/* 188 */       paint(localGraphics2D, localJComponent, paramInt3, paramInt4);
/* 189 */       localGraphics2D.dispose();
/* 190 */       ImageScalingHelper.paint(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, (Image)localObject, INSETS, INSETS, ImageScalingHelper.PaintType.PAINT9_STRETCH, 512);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Color getLighter(Color paramColor, float paramFloat)
/*     */   {
/* 197 */     return new Color(Math.min((int)(paramColor.getRed() / paramFloat), 255), Math.min((int)(paramColor.getGreen() / paramFloat), 255), Math.min((int)(paramColor.getBlue() / paramFloat), 255));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.LoweredBorder
 * JD-Core Version:    0.6.2
 */
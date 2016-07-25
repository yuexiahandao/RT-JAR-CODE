/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LinearGradientPaint;
/*     */ import java.awt.RadialGradientPaint;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.image.VolatileImage;
/*     */ import java.awt.print.PrinterGraphics;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.Painter;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ public abstract class AbstractRegionPainter
/*     */   implements Painter<JComponent>
/*     */ {
/*     */   private PaintContext ctx;
/*     */   private float f;
/*     */   private float leftWidth;
/*     */   private float topHeight;
/*     */   private float centerWidth;
/*     */   private float centerHeight;
/*     */   private float rightWidth;
/*     */   private float bottomHeight;
/*     */   private float leftScale;
/*     */   private float topScale;
/*     */   private float centerHScale;
/*     */   private float centerVScale;
/*     */   private float rightScale;
/*     */   private float bottomScale;
/*     */ 
/*     */   public final void paint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 145 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) return;
/*     */ 
/* 147 */     Object[] arrayOfObject = getExtendedCacheKeys(paramJComponent);
/* 148 */     this.ctx = getPaintContext();
/* 149 */     AbstractRegionPainter.PaintContext.CacheMode localCacheMode = this.ctx == null ? AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING : this.ctx.cacheMode;
/* 150 */     if ((localCacheMode == AbstractRegionPainter.PaintContext.CacheMode.NO_CACHING) || (!ImageCache.getInstance().isImageCachable(paramInt1, paramInt2)) || ((paramGraphics2D instanceof PrinterGraphics)))
/*     */     {
/* 154 */       paint0(paramGraphics2D, paramJComponent, paramInt1, paramInt2, arrayOfObject);
/* 155 */     } else if (localCacheMode == AbstractRegionPainter.PaintContext.CacheMode.FIXED_SIZES) {
/* 156 */       paintWithFixedSizeCaching(paramGraphics2D, paramJComponent, paramInt1, paramInt2, arrayOfObject);
/*     */     }
/*     */     else
/* 159 */       paintWith9SquareCaching(paramGraphics2D, this.ctx, paramJComponent, paramInt1, paramInt2, arrayOfObject);
/*     */   }
/*     */ 
/*     */   protected Object[] getExtendedCacheKeys(JComponent paramJComponent)
/*     */   {
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */   protected abstract PaintContext getPaintContext();
/*     */ 
/*     */   protected void configureGraphics(Graphics2D paramGraphics2D)
/*     */   {
/* 199 */     paramGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*     */   }
/*     */ 
/*     */   protected abstract void doPaint(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject);
/*     */ 
/*     */   protected final float decodeX(float paramFloat)
/*     */   {
/* 234 */     if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F))
/* 235 */       return paramFloat * this.leftWidth;
/* 236 */     if ((paramFloat > 1.0F) && (paramFloat < 2.0F))
/* 237 */       return (paramFloat - 1.0F) * this.centerWidth + this.leftWidth;
/* 238 */     if ((paramFloat >= 2.0F) && (paramFloat <= 3.0F)) {
/* 239 */       return (paramFloat - 2.0F) * this.rightWidth + this.leftWidth + this.centerWidth;
/*     */     }
/* 241 */     throw new IllegalArgumentException("Invalid x");
/*     */   }
/*     */ 
/*     */   protected final float decodeY(float paramFloat)
/*     */   {
/* 255 */     if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F))
/* 256 */       return paramFloat * this.topHeight;
/* 257 */     if ((paramFloat > 1.0F) && (paramFloat < 2.0F))
/* 258 */       return (paramFloat - 1.0F) * this.centerHeight + this.topHeight;
/* 259 */     if ((paramFloat >= 2.0F) && (paramFloat <= 3.0F)) {
/* 260 */       return (paramFloat - 2.0F) * this.bottomHeight + this.topHeight + this.centerHeight;
/*     */     }
/* 262 */     throw new IllegalArgumentException("Invalid y");
/*     */   }
/*     */ 
/*     */   protected final float decodeAnchorX(float paramFloat1, float paramFloat2)
/*     */   {
/* 278 */     if ((paramFloat1 >= 0.0F) && (paramFloat1 <= 1.0F))
/* 279 */       return decodeX(paramFloat1) + paramFloat2 * this.leftScale;
/* 280 */     if ((paramFloat1 > 1.0F) && (paramFloat1 < 2.0F))
/* 281 */       return decodeX(paramFloat1) + paramFloat2 * this.centerHScale;
/* 282 */     if ((paramFloat1 >= 2.0F) && (paramFloat1 <= 3.0F)) {
/* 283 */       return decodeX(paramFloat1) + paramFloat2 * this.rightScale;
/*     */     }
/* 285 */     throw new IllegalArgumentException("Invalid x");
/*     */   }
/*     */ 
/*     */   protected final float decodeAnchorY(float paramFloat1, float paramFloat2)
/*     */   {
/* 301 */     if ((paramFloat1 >= 0.0F) && (paramFloat1 <= 1.0F))
/* 302 */       return decodeY(paramFloat1) + paramFloat2 * this.topScale;
/* 303 */     if ((paramFloat1 > 1.0F) && (paramFloat1 < 2.0F))
/* 304 */       return decodeY(paramFloat1) + paramFloat2 * this.centerVScale;
/* 305 */     if ((paramFloat1 >= 2.0F) && (paramFloat1 <= 3.0F)) {
/* 306 */       return decodeY(paramFloat1) + paramFloat2 * this.bottomScale;
/*     */     }
/* 308 */     throw new IllegalArgumentException("Invalid y");
/*     */   }
/*     */ 
/*     */   protected final Color decodeColor(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt)
/*     */   {
/* 327 */     if ((UIManager.getLookAndFeel() instanceof NimbusLookAndFeel)) {
/* 328 */       NimbusLookAndFeel localNimbusLookAndFeel = (NimbusLookAndFeel)UIManager.getLookAndFeel();
/* 329 */       return localNimbusLookAndFeel.getDerivedColor(paramString, paramFloat1, paramFloat2, paramFloat3, paramInt, true);
/*     */     }
/*     */ 
/* 333 */     return Color.getHSBColor(paramFloat1, paramFloat2, paramFloat3);
/*     */   }
/*     */ 
/*     */   protected final Color decodeColor(Color paramColor1, Color paramColor2, float paramFloat)
/*     */   {
/* 349 */     return new Color(NimbusLookAndFeel.deriveARGB(paramColor1, paramColor2, paramFloat));
/*     */   }
/*     */ 
/*     */   protected final LinearGradientPaint decodeGradient(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOfFloat, Color[] paramArrayOfColor)
/*     */   {
/* 377 */     if ((paramFloat1 == paramFloat3) && (paramFloat2 == paramFloat4)) {
/* 378 */       paramFloat4 += 1.0E-005F;
/*     */     }
/* 380 */     return new LinearGradientPaint(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramArrayOfFloat, paramArrayOfColor);
/*     */   }
/*     */ 
/*     */   protected final RadialGradientPaint decodeRadialGradient(float paramFloat1, float paramFloat2, float paramFloat3, float[] paramArrayOfFloat, Color[] paramArrayOfColor)
/*     */   {
/* 407 */     if (paramFloat3 == 0.0F) {
/* 408 */       paramFloat3 = 1.0E-005F;
/*     */     }
/* 410 */     return new RadialGradientPaint(paramFloat1, paramFloat2, paramFloat3, paramArrayOfFloat, paramArrayOfColor);
/*     */   }
/*     */ 
/*     */   protected final Color getComponentColor(JComponent paramJComponent, String paramString, Color paramColor, float paramFloat1, float paramFloat2, int paramInt)
/*     */   {
/* 430 */     Color localColor = null;
/*     */     Object localObject1;
/* 431 */     if (paramJComponent != null)
/*     */     {
/* 433 */       if ("background".equals(paramString)) {
/* 434 */         localColor = paramJComponent.getBackground();
/* 435 */       } else if ("foreground".equals(paramString)) {
/* 436 */         localColor = paramJComponent.getForeground();
/* 437 */       } else if (((paramJComponent instanceof JList)) && ("selectionForeground".equals(paramString))) {
/* 438 */         localColor = ((JList)paramJComponent).getSelectionForeground();
/* 439 */       } else if (((paramJComponent instanceof JList)) && ("selectionBackground".equals(paramString))) {
/* 440 */         localColor = ((JList)paramJComponent).getSelectionBackground();
/* 441 */       } else if (((paramJComponent instanceof JTable)) && ("selectionForeground".equals(paramString))) {
/* 442 */         localColor = ((JTable)paramJComponent).getSelectionForeground();
/* 443 */       } else if (((paramJComponent instanceof JTable)) && ("selectionBackground".equals(paramString))) {
/* 444 */         localColor = ((JTable)paramJComponent).getSelectionBackground();
/*     */       } else {
/* 446 */         localObject1 = "get" + Character.toUpperCase(paramString.charAt(0)) + paramString.substring(1);
/*     */         try {
/* 448 */           Method localMethod = paramJComponent.getClass().getMethod((String)localObject1, new Class[0]);
/* 449 */           localColor = (Color)localMethod.invoke(paramJComponent, new Object[0]);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */ 
/* 456 */         if (localColor == null) {
/* 457 */           Object localObject2 = paramJComponent.getClientProperty(paramString);
/* 458 */           if ((localObject2 instanceof Color)) {
/* 459 */             localColor = (Color)localObject2;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 470 */     if ((localColor == null) || ((localColor instanceof UIResource)))
/* 471 */       return paramColor;
/* 472 */     if ((paramFloat1 != 0.0F) || (paramFloat2 != 0.0F) || (paramInt != 0)) {
/* 473 */       localObject1 = Color.RGBtoHSB(localColor.getRed(), localColor.getGreen(), localColor.getBlue(), null);
/* 474 */       localObject1[1] = clamp(localObject1[1] + paramFloat1);
/* 475 */       localObject1[2] = clamp(localObject1[2] + paramFloat2);
/* 476 */       int i = clamp(localColor.getAlpha() + paramInt);
/* 477 */       return new Color(Color.HSBtoRGB(localObject1[0], localObject1[1], localObject1[2]) & 0xFFFFFF | i << 24);
/*     */     }
/* 479 */     return localColor;
/*     */   }
/*     */ 
/*     */   private void prepare(float paramFloat1, float paramFloat2)
/*     */   {
/* 582 */     if ((this.ctx == null) || (this.ctx.canvasSize == null)) {
/* 583 */       this.f = 1.0F;
/* 584 */       this.leftWidth = (this.centerWidth = this.rightWidth = 0.0F);
/* 585 */       this.topHeight = (this.centerHeight = this.bottomHeight = 0.0F);
/* 586 */       this.leftScale = (this.centerHScale = this.rightScale = 0.0F);
/* 587 */       this.topScale = (this.centerVScale = this.bottomScale = 0.0F);
/* 588 */       return;
/*     */     }
/*     */ 
/* 592 */     Number localNumber = (Number)UIManager.get("scale");
/* 593 */     this.f = (localNumber == null ? 1.0F : localNumber.floatValue());
/*     */ 
/* 595 */     if (this.ctx.inverted) {
/* 596 */       this.centerWidth = ((this.ctx.b - this.ctx.a) * this.f);
/* 597 */       float f1 = paramFloat1 - this.centerWidth;
/* 598 */       this.leftWidth = (f1 * this.ctx.aPercent);
/* 599 */       this.rightWidth = (f1 * this.ctx.bPercent);
/* 600 */       this.centerHeight = ((this.ctx.d - this.ctx.c) * this.f);
/* 601 */       f1 = paramFloat2 - this.centerHeight;
/* 602 */       this.topHeight = (f1 * this.ctx.cPercent);
/* 603 */       this.bottomHeight = (f1 * this.ctx.dPercent);
/*     */     } else {
/* 605 */       this.leftWidth = (this.ctx.a * this.f);
/* 606 */       this.rightWidth = ((float)(this.ctx.canvasSize.getWidth() - this.ctx.b) * this.f);
/* 607 */       this.centerWidth = (paramFloat1 - this.leftWidth - this.rightWidth);
/* 608 */       this.topHeight = (this.ctx.c * this.f);
/* 609 */       this.bottomHeight = ((float)(this.ctx.canvasSize.getHeight() - this.ctx.d) * this.f);
/* 610 */       this.centerHeight = (paramFloat2 - this.topHeight - this.bottomHeight);
/*     */     }
/*     */ 
/* 613 */     this.leftScale = (this.ctx.a == 0.0F ? 0.0F : this.leftWidth / this.ctx.a);
/* 614 */     this.centerHScale = (this.ctx.b - this.ctx.a == 0.0F ? 0.0F : this.centerWidth / (this.ctx.b - this.ctx.a));
/* 615 */     this.rightScale = (this.ctx.canvasSize.width - this.ctx.b == 0.0F ? 0.0F : this.rightWidth / (this.ctx.canvasSize.width - this.ctx.b));
/* 616 */     this.topScale = (this.ctx.c == 0.0F ? 0.0F : this.topHeight / this.ctx.c);
/* 617 */     this.centerVScale = (this.ctx.d - this.ctx.c == 0.0F ? 0.0F : this.centerHeight / (this.ctx.d - this.ctx.c));
/* 618 */     this.bottomScale = (this.ctx.canvasSize.height - this.ctx.d == 0.0F ? 0.0F : this.bottomHeight / (this.ctx.canvasSize.height - this.ctx.d));
/*     */   }
/*     */ 
/*     */   private void paintWith9SquareCaching(Graphics2D paramGraphics2D, PaintContext paramPaintContext, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 625 */     Dimension localDimension = paramPaintContext.canvasSize;
/* 626 */     Insets localInsets1 = paramPaintContext.stretchingInsets;
/*     */ 
/* 628 */     if ((paramInt1 <= localDimension.width * paramPaintContext.maxHorizontalScaleFactor) && (paramInt2 <= localDimension.height * paramPaintContext.maxVerticalScaleFactor))
/*     */     {
/* 630 */       VolatileImage localVolatileImage = getImage(paramGraphics2D.getDeviceConfiguration(), paramJComponent, localDimension.width, localDimension.height, paramArrayOfObject);
/* 631 */       if (localVolatileImage != null)
/*     */       {
/*     */         Insets localInsets2;
/* 635 */         if (paramPaintContext.inverted) {
/* 636 */           int i = (paramInt1 - (localDimension.width - (localInsets1.left + localInsets1.right))) / 2;
/* 637 */           int j = (paramInt2 - (localDimension.height - (localInsets1.top + localInsets1.bottom))) / 2;
/* 638 */           localInsets2 = new Insets(j, i, j, i);
/*     */         } else {
/* 640 */           localInsets2 = localInsets1;
/*     */         }
/*     */ 
/* 643 */         Object localObject = paramGraphics2D.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
/* 644 */         paramGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
/* 645 */         ImageScalingHelper.paint(paramGraphics2D, 0, 0, paramInt1, paramInt2, localVolatileImage, localInsets1, localInsets2, ImageScalingHelper.PaintType.PAINT9_STRETCH, 512);
/*     */ 
/* 647 */         paramGraphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, localObject != null ? localObject : RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
/*     */       }
/*     */       else
/*     */       {
/* 651 */         paint0(paramGraphics2D, paramJComponent, paramInt1, paramInt2, paramArrayOfObject);
/*     */       }
/*     */     }
/*     */     else {
/* 655 */       paint0(paramGraphics2D, paramJComponent, paramInt1, paramInt2, paramArrayOfObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void paintWithFixedSizeCaching(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 661 */     VolatileImage localVolatileImage = getImage(paramGraphics2D.getDeviceConfiguration(), paramJComponent, paramInt1, paramInt2, paramArrayOfObject);
/* 662 */     if (localVolatileImage != null)
/*     */     {
/* 664 */       paramGraphics2D.drawImage(localVolatileImage, 0, 0, null);
/*     */     }
/*     */     else
/* 667 */       paint0(paramGraphics2D, paramJComponent, paramInt1, paramInt2, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   private VolatileImage getImage(GraphicsConfiguration paramGraphicsConfiguration, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 674 */     ImageCache localImageCache = ImageCache.getInstance();
/*     */ 
/* 676 */     VolatileImage localVolatileImage = (VolatileImage)localImageCache.getImage(paramGraphicsConfiguration, paramInt1, paramInt2, new Object[] { this, paramArrayOfObject });
/*     */ 
/* 678 */     int i = 0;
/*     */     do
/*     */     {
/* 681 */       int j = 2;
/* 682 */       if (localVolatileImage != null) {
/* 683 */         j = localVolatileImage.validate(paramGraphicsConfiguration);
/*     */       }
/*     */ 
/* 687 */       if ((j == 2) || (j == 1))
/*     */       {
/* 690 */         if ((localVolatileImage == null) || (localVolatileImage.getWidth() != paramInt1) || (localVolatileImage.getHeight() != paramInt2) || (j == 2))
/*     */         {
/* 693 */           if (localVolatileImage != null) {
/* 694 */             localVolatileImage.flush();
/* 695 */             localVolatileImage = null;
/*     */           }
/*     */ 
/* 698 */           localVolatileImage = paramGraphicsConfiguration.createCompatibleVolatileImage(paramInt1, paramInt2, 3);
/*     */ 
/* 701 */           localImageCache.setImage(localVolatileImage, paramGraphicsConfiguration, paramInt1, paramInt2, new Object[] { this, paramArrayOfObject });
/*     */         }
/*     */ 
/* 704 */         Graphics2D localGraphics2D = localVolatileImage.createGraphics();
/*     */ 
/* 706 */         localGraphics2D.setComposite(AlphaComposite.Clear);
/* 707 */         localGraphics2D.fillRect(0, 0, paramInt1, paramInt2);
/* 708 */         localGraphics2D.setComposite(AlphaComposite.SrcOver);
/* 709 */         configureGraphics(localGraphics2D);
/*     */ 
/* 711 */         paint0(localGraphics2D, paramJComponent, paramInt1, paramInt2, paramArrayOfObject);
/*     */ 
/* 713 */         localGraphics2D.dispose();
/*     */       }
/*     */     }
/* 715 */     while ((localVolatileImage.contentsLost()) && (i++ < 3));
/*     */ 
/* 717 */     if (i == 3) return null;
/*     */ 
/* 719 */     return localVolatileImage;
/*     */   }
/*     */ 
/*     */   private void paint0(Graphics2D paramGraphics2D, JComponent paramJComponent, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 728 */     prepare(paramInt1, paramInt2);
/* 729 */     paramGraphics2D = (Graphics2D)paramGraphics2D.create();
/* 730 */     configureGraphics(paramGraphics2D);
/* 731 */     doPaint(paramGraphics2D, paramJComponent, paramInt1, paramInt2, paramArrayOfObject);
/* 732 */     paramGraphics2D.dispose();
/*     */   }
/*     */ 
/*     */   private float clamp(float paramFloat) {
/* 736 */     if (paramFloat < 0.0F)
/* 737 */       paramFloat = 0.0F;
/* 738 */     else if (paramFloat > 1.0F) {
/* 739 */       paramFloat = 1.0F;
/*     */     }
/* 741 */     return paramFloat;
/*     */   }
/*     */ 
/*     */   private int clamp(int paramInt) {
/* 745 */     if (paramInt < 0)
/* 746 */       paramInt = 0;
/* 747 */     else if (paramInt > 255) {
/* 748 */       paramInt = 255;
/*     */     }
/* 750 */     return paramInt;
/*     */   }
/*     */ 
/*     */   protected static class PaintContext
/*     */   {
/* 494 */     private static Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
/*     */     private Insets stretchingInsets;
/*     */     private Dimension canvasSize;
/*     */     private boolean inverted;
/*     */     private CacheMode cacheMode;
/*     */     private double maxHorizontalScaleFactor;
/*     */     private double maxVerticalScaleFactor;
/*     */     private float a;
/*     */     private float b;
/*     */     private float c;
/*     */     private float d;
/*     */     private float aPercent;
/*     */     private float bPercent;
/*     */     private float cPercent;
/*     */     private float dPercent;
/*     */ 
/*     */     public PaintContext(Insets paramInsets, Dimension paramDimension, boolean paramBoolean)
/*     */     {
/* 523 */       this(paramInsets, paramDimension, paramBoolean, null, 1.0D, 1.0D);
/*     */     }
/*     */ 
/*     */     public PaintContext(Insets paramInsets, Dimension paramDimension, boolean paramBoolean, CacheMode paramCacheMode, double paramDouble1, double paramDouble2)
/*     */     {
/* 546 */       if ((paramDouble1 < 1.0D) || (paramDouble1 < 1.0D)) {
/* 547 */         throw new IllegalArgumentException("Both maxH and maxV must be >= 1");
/*     */       }
/*     */ 
/* 550 */       this.stretchingInsets = (paramInsets == null ? EMPTY_INSETS : paramInsets);
/* 551 */       this.canvasSize = paramDimension;
/* 552 */       this.inverted = paramBoolean;
/* 553 */       this.cacheMode = (paramCacheMode == null ? CacheMode.NO_CACHING : paramCacheMode);
/* 554 */       this.maxHorizontalScaleFactor = paramDouble1;
/* 555 */       this.maxVerticalScaleFactor = paramDouble2;
/*     */ 
/* 557 */       if (paramDimension != null) {
/* 558 */         this.a = this.stretchingInsets.left;
/* 559 */         this.b = (paramDimension.width - this.stretchingInsets.right);
/* 560 */         this.c = this.stretchingInsets.top;
/* 561 */         this.d = (paramDimension.height - this.stretchingInsets.bottom);
/* 562 */         this.canvasSize = paramDimension;
/* 563 */         this.inverted = paramBoolean;
/* 564 */         if (paramBoolean) {
/* 565 */           float f = paramDimension.width - (this.b - this.a);
/* 566 */           this.aPercent = (f > 0.0F ? this.a / f : 0.0F);
/* 567 */           this.bPercent = (f > 0.0F ? this.b / f : 0.0F);
/* 568 */           f = paramDimension.height - (this.d - this.c);
/* 569 */           this.cPercent = (f > 0.0F ? this.c / f : 0.0F);
/* 570 */           this.dPercent = (f > 0.0F ? this.d / f : 0.0F);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     protected static enum CacheMode
/*     */     {
/* 491 */       NO_CACHING, FIXED_SIZES, NINE_SQUARE_SCALE;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.AbstractRegionPainter
 * JD-Core Version:    0.6.2
 */
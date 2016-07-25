/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.RGBImageFilter;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.UIManager;
/*     */ import sun.swing.CachedPainter;
/*     */ import sun.swing.ImageIconUIResource;
/*     */ 
/*     */ class MetalUtils
/*     */ {
/*     */   static void drawFlush3DBorder(Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/*  46 */     drawFlush3DBorder(paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */   }
/*     */ 
/*     */   static void drawFlush3DBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  53 */     paramGraphics.translate(paramInt1, paramInt2);
/*  54 */     paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/*  55 */     paramGraphics.drawRect(0, 0, paramInt3 - 2, paramInt4 - 2);
/*  56 */     paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/*  57 */     paramGraphics.drawRect(1, 1, paramInt3 - 2, paramInt4 - 2);
/*  58 */     paramGraphics.setColor(MetalLookAndFeel.getControl());
/*  59 */     paramGraphics.drawLine(0, paramInt4 - 1, 1, paramInt4 - 2);
/*  60 */     paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 2, 1);
/*  61 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*     */   }
/*     */ 
/*     */   static void drawPressed3DBorder(Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/*  69 */     drawPressed3DBorder(paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */   }
/*     */ 
/*     */   static void drawDisabledBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  73 */     paramGraphics.translate(paramInt1, paramInt2);
/*  74 */     paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/*  75 */     paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/*  76 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*     */   }
/*     */ 
/*     */   static void drawPressed3DBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  84 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/*  86 */     drawFlush3DBorder(paramGraphics, 0, 0, paramInt3, paramInt4);
/*     */ 
/*  88 */     paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/*  89 */     paramGraphics.drawLine(1, 1, 1, paramInt4 - 2);
/*  90 */     paramGraphics.drawLine(1, 1, paramInt3 - 2, 1);
/*  91 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*     */   }
/*     */ 
/*     */   static void drawDark3DBorder(Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/* 100 */     drawDark3DBorder(paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */   }
/*     */ 
/*     */   static void drawDark3DBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 109 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 111 */     drawFlush3DBorder(paramGraphics, 0, 0, paramInt3, paramInt4);
/*     */ 
/* 113 */     paramGraphics.setColor(MetalLookAndFeel.getControl());
/* 114 */     paramGraphics.drawLine(1, 1, 1, paramInt4 - 2);
/* 115 */     paramGraphics.drawLine(1, 1, paramInt3 - 2, 1);
/* 116 */     paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/* 117 */     paramGraphics.drawLine(1, paramInt4 - 2, 1, paramInt4 - 2);
/* 118 */     paramGraphics.drawLine(paramInt3 - 2, 1, paramInt3 - 2, 1);
/* 119 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*     */   }
/*     */ 
/*     */   static void drawButtonBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 123 */     if (paramBoolean)
/* 124 */       drawActiveButtonBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     else
/* 126 */       drawFlush3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   static void drawActiveButtonBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 131 */     drawFlush3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/* 132 */     paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 133 */     paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 1, paramInt1 + 1, paramInt4 - 3);
/* 134 */     paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 1, paramInt3 - 3, paramInt1 + 1);
/* 135 */     paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 136 */     paramGraphics.drawLine(paramInt1 + 2, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/* 137 */     paramGraphics.drawLine(paramInt3 - 2, paramInt2 + 2, paramInt3 - 2, paramInt4 - 2);
/*     */   }
/*     */ 
/*     */   static void drawDefaultButtonBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
/* 141 */     drawButtonBorder(paramGraphics, paramInt1 + 1, paramInt2 + 1, paramInt3 - 1, paramInt4 - 1, paramBoolean);
/* 142 */     paramGraphics.translate(paramInt1, paramInt2);
/* 143 */     paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 144 */     paramGraphics.drawRect(0, 0, paramInt3 - 3, paramInt4 - 3);
/* 145 */     paramGraphics.drawLine(paramInt3 - 2, 0, paramInt3 - 2, 0);
/* 146 */     paramGraphics.drawLine(0, paramInt4 - 2, 0, paramInt4 - 2);
/* 147 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*     */   }
/*     */ 
/*     */   static void drawDefaultButtonPressedBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 151 */     drawPressed3DBorder(paramGraphics, paramInt1 + 1, paramInt2 + 1, paramInt3 - 1, paramInt4 - 1);
/* 152 */     paramGraphics.translate(paramInt1, paramInt2);
/* 153 */     paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 154 */     paramGraphics.drawRect(0, 0, paramInt3 - 3, paramInt4 - 3);
/* 155 */     paramGraphics.drawLine(paramInt3 - 2, 0, paramInt3 - 2, 0);
/* 156 */     paramGraphics.drawLine(0, paramInt4 - 2, 0, paramInt4 - 2);
/* 157 */     paramGraphics.setColor(MetalLookAndFeel.getControl());
/* 158 */     paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 1, 0);
/* 159 */     paramGraphics.drawLine(0, paramInt4 - 1, 0, paramInt4 - 1);
/* 160 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*     */   }
/*     */ 
/*     */   static boolean isLeftToRight(Component paramComponent)
/*     */   {
/* 168 */     return paramComponent.getComponentOrientation().isLeftToRight();
/*     */   }
/*     */ 
/*     */   static int getInt(Object paramObject, int paramInt) {
/* 172 */     Object localObject = UIManager.get(paramObject);
/*     */ 
/* 174 */     if ((localObject instanceof Integer)) {
/* 175 */       return ((Integer)localObject).intValue();
/*     */     }
/* 177 */     if ((localObject instanceof String))
/*     */       try {
/* 179 */         return Integer.parseInt((String)localObject);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 182 */     return paramInt;
/*     */   }
/*     */ 
/*     */   static boolean drawGradient(Component paramComponent, Graphics paramGraphics, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 213 */     List localList = (List)UIManager.get(paramString);
/* 214 */     if ((localList == null) || (!(paramGraphics instanceof Graphics2D))) {
/* 215 */       return false;
/*     */     }
/*     */ 
/* 218 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 219 */       return true;
/*     */     }
/*     */ 
/* 222 */     GradientPainter.INSTANCE.paint(paramComponent, (Graphics2D)paramGraphics, localList, paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*     */ 
/* 224 */     return true;
/*     */   }
/*     */ 
/*     */   static boolean isToolBarButton(JComponent paramJComponent)
/*     */   {
/* 385 */     return paramJComponent.getParent() instanceof JToolBar;
/*     */   }
/*     */ 
/*     */   static Icon getOceanToolBarIcon(Image paramImage) {
/* 389 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(paramImage.getSource(), new OceanToolBarImageFilter());
/*     */ 
/* 391 */     return new ImageIconUIResource(Toolkit.getDefaultToolkit().createImage(localFilteredImageSource));
/*     */   }
/*     */ 
/*     */   static Icon getOceanDisabledButtonIcon(Image paramImage) {
/* 395 */     Object[] arrayOfObject = (Object[])UIManager.get("Button.disabledGrayRange");
/* 396 */     int i = 180;
/* 397 */     int j = 215;
/* 398 */     if (arrayOfObject != null) {
/* 399 */       i = ((Integer)arrayOfObject[0]).intValue();
/* 400 */       j = ((Integer)arrayOfObject[1]).intValue();
/*     */     }
/* 402 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(paramImage.getSource(), new OceanDisabledButtonImageFilter(i, j));
/*     */ 
/* 404 */     return new ImageIconUIResource(Toolkit.getDefaultToolkit().createImage(localFilteredImageSource));
/*     */   }
/*     */ 
/*     */   private static class GradientPainter extends CachedPainter
/*     */   {
/* 233 */     public static final GradientPainter INSTANCE = new GradientPainter(8);
/*     */     private static final int IMAGE_SIZE = 64;
/*     */     private int w;
/*     */     private int h;
/*     */ 
/*     */     GradientPainter(int paramInt)
/*     */     {
/* 250 */       super();
/*     */     }
/*     */ 
/*     */     public void paint(Component paramComponent, Graphics2D paramGraphics2D, List paramList, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */     {
/*     */       int i;
/*     */       int j;
/* 258 */       if (paramBoolean) {
/* 259 */         i = 64;
/* 260 */         j = paramInt4;
/*     */       }
/*     */       else {
/* 263 */         i = paramInt3;
/* 264 */         j = 64;
/*     */       }
/* 266 */       synchronized (paramComponent.getTreeLock()) {
/* 267 */         this.w = paramInt3;
/* 268 */         this.h = paramInt4;
/* 269 */         paint(paramComponent, paramGraphics2D, paramInt1, paramInt2, i, j, new Object[] { paramList, Boolean.valueOf(paramBoolean) });
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void paintToImage(Component paramComponent, Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */     {
/* 276 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/* 277 */       List localList = (List)paramArrayOfObject[0];
/* 278 */       boolean bool = ((Boolean)paramArrayOfObject[1]).booleanValue();
/*     */ 
/* 280 */       if (bool) {
/* 281 */         drawVerticalGradient(localGraphics2D, ((Number)localList.get(0)).floatValue(), ((Number)localList.get(1)).floatValue(), (Color)localList.get(2), (Color)localList.get(3), (Color)localList.get(4), paramInt1, paramInt2);
/*     */       }
/*     */       else
/*     */       {
/* 289 */         drawHorizontalGradient(localGraphics2D, ((Number)localList.get(0)).floatValue(), ((Number)localList.get(1)).floatValue(), (Color)localList.get(2), (Color)localList.get(3), (Color)localList.get(4), paramInt1, paramInt2);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void paintImage(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Image paramImage, Object[] paramArrayOfObject)
/*     */     {
/* 301 */       boolean bool = ((Boolean)paramArrayOfObject[1]).booleanValue();
/*     */ 
/* 303 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */       int i;
/*     */       int j;
/* 304 */       if (bool) {
/* 305 */         for (i = 0; i < this.w; i += 64) {
/* 306 */           j = Math.min(64, this.w - i);
/* 307 */           paramGraphics.drawImage(paramImage, i, 0, i + j, this.h, 0, 0, j, this.h, null);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 312 */         for (i = 0; i < this.h; i += 64) {
/* 313 */           j = Math.min(64, this.h - i);
/* 314 */           paramGraphics.drawImage(paramImage, 0, i, this.w, i + j, 0, 0, this.w, j, null);
/*     */         }
/*     */       }
/*     */ 
/* 318 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     private void drawVerticalGradient(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2, Color paramColor1, Color paramColor2, Color paramColor3, int paramInt1, int paramInt2)
/*     */     {
/* 324 */       int i = (int)(paramFloat1 * paramInt2);
/* 325 */       int j = (int)(paramFloat2 * paramInt2);
/* 326 */       if (i > 0) {
/* 327 */         paramGraphics2D.setPaint(getGradient(0.0F, 0.0F, paramColor1, 0.0F, i, paramColor2));
/*     */ 
/* 329 */         paramGraphics2D.fillRect(0, 0, paramInt1, i);
/*     */       }
/* 331 */       if (j > 0) {
/* 332 */         paramGraphics2D.setColor(paramColor2);
/* 333 */         paramGraphics2D.fillRect(0, i, paramInt1, j);
/*     */       }
/* 335 */       if (i > 0) {
/* 336 */         paramGraphics2D.setPaint(getGradient(0.0F, i + j, paramColor2, 0.0F, i * 2.0F + j, paramColor1));
/*     */ 
/* 338 */         paramGraphics2D.fillRect(0, i + j, paramInt1, i);
/*     */       }
/* 340 */       if (paramInt2 - i * 2 - j > 0) {
/* 341 */         paramGraphics2D.setPaint(getGradient(0.0F, i * 2.0F + j, paramColor1, 0.0F, paramInt2, paramColor3));
/*     */ 
/* 343 */         paramGraphics2D.fillRect(0, i * 2 + j, paramInt1, paramInt2 - i * 2 - j);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void drawHorizontalGradient(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2, Color paramColor1, Color paramColor2, Color paramColor3, int paramInt1, int paramInt2)
/*     */     {
/* 350 */       int i = (int)(paramFloat1 * paramInt1);
/* 351 */       int j = (int)(paramFloat2 * paramInt1);
/* 352 */       if (i > 0) {
/* 353 */         paramGraphics2D.setPaint(getGradient(0.0F, 0.0F, paramColor1, i, 0.0F, paramColor2));
/*     */ 
/* 355 */         paramGraphics2D.fillRect(0, 0, i, paramInt2);
/*     */       }
/* 357 */       if (j > 0) {
/* 358 */         paramGraphics2D.setColor(paramColor2);
/* 359 */         paramGraphics2D.fillRect(i, 0, j, paramInt2);
/*     */       }
/* 361 */       if (i > 0) {
/* 362 */         paramGraphics2D.setPaint(getGradient(i + j, 0.0F, paramColor2, i * 2.0F + j, 0.0F, paramColor1));
/*     */ 
/* 364 */         paramGraphics2D.fillRect(i + j, 0, i, paramInt2);
/*     */       }
/* 366 */       if (paramInt1 - i * 2 - j > 0) {
/* 367 */         paramGraphics2D.setPaint(getGradient(i * 2.0F + j, 0.0F, paramColor1, paramInt1, 0.0F, paramColor3));
/*     */ 
/* 369 */         paramGraphics2D.fillRect(i * 2 + j, 0, paramInt1 - i * 2 - j, paramInt2);
/*     */       }
/*     */     }
/*     */ 
/*     */     private GradientPaint getGradient(float paramFloat1, float paramFloat2, Color paramColor1, float paramFloat3, float paramFloat4, Color paramColor2)
/*     */     {
/* 376 */       return new GradientPaint(paramFloat1, paramFloat2, paramColor1, paramFloat3, paramFloat4, paramColor2, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OceanDisabledButtonImageFilter extends RGBImageFilter
/*     */   {
/*     */     private float min;
/*     */     private float factor;
/*     */ 
/*     */     OceanDisabledButtonImageFilter(int paramInt1, int paramInt2)
/*     */     {
/* 418 */       this.canFilterIndexColorModel = true;
/* 419 */       this.min = paramInt1;
/* 420 */       this.factor = ((paramInt2 - paramInt1) / 255.0F);
/*     */     }
/*     */ 
/*     */     public int filterRGB(int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 425 */       int i = Math.min(255, (int)((0.2125F * (paramInt3 >> 16 & 0xFF) + 0.7154F * (paramInt3 >> 8 & 0xFF) + 0.0721F * (paramInt3 & 0xFF) + 0.5F) * this.factor + this.min));
/*     */ 
/* 429 */       return paramInt3 & 0xFF000000 | i << 16 | i << 8 | i << 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class OceanToolBarImageFilter extends RGBImageFilter
/*     */   {
/*     */     OceanToolBarImageFilter()
/*     */     {
/* 440 */       this.canFilterIndexColorModel = true;
/*     */     }
/*     */ 
/*     */     public int filterRGB(int paramInt1, int paramInt2, int paramInt3) {
/* 444 */       int i = paramInt3 >> 16 & 0xFF;
/* 445 */       int j = paramInt3 >> 8 & 0xFF;
/* 446 */       int k = paramInt3 & 0xFF;
/* 447 */       int m = Math.max(Math.max(i, j), k);
/* 448 */       return paramInt3 & 0xFF000000 | m << 16 | m << 8 | m << 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalUtils
 * JD-Core Version:    0.6.2
 */
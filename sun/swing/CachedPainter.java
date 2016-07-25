/*     */ package sun.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.VolatileImage;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class CachedPainter
/*     */ {
/*  56 */   private static final Map<Object, ImageCache> cacheMap = new HashMap();
/*     */ 
/*     */   private static ImageCache getCache(Object paramObject)
/*     */   {
/*  61 */     synchronized (CachedPainter.class) {
/*  62 */       ImageCache localImageCache = (ImageCache)cacheMap.get(paramObject);
/*  63 */       if (localImageCache == null) {
/*  64 */         localImageCache = new ImageCache(1);
/*  65 */         cacheMap.put(paramObject, localImageCache);
/*     */       }
/*  67 */       return localImageCache;
/*     */     }
/*     */   }
/*     */ 
/*     */   public CachedPainter(int paramInt)
/*     */   {
/*  78 */     getCache(getClass()).setMaxCount(paramInt);
/*     */   }
/*     */ 
/*     */   public void paint(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object[] paramArrayOfObject)
/*     */   {
/*  96 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/*  97 */       return;
/*     */     }
/*  99 */     if (paramComponent != null) {
/* 100 */       synchronized (paramComponent.getTreeLock()) {
/* 101 */         synchronized (CachedPainter.class)
/*     */         {
/* 105 */           paint0(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfObject);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/* 110 */       synchronized (CachedPainter.class) {
/* 111 */         paint0(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfObject);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void paint0(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object[] paramArrayOfObject)
/*     */   {
/* 118 */     Class localClass = getClass();
/* 119 */     GraphicsConfiguration localGraphicsConfiguration = getGraphicsConfiguration(paramComponent);
/* 120 */     ImageCache localImageCache = getCache(localClass);
/* 121 */     Image localImage = localImageCache.getImage(localClass, localGraphicsConfiguration, paramInt3, paramInt4, paramArrayOfObject);
/* 122 */     int i = 0;
/*     */     do {
/* 124 */       int j = 0;
/* 125 */       if ((localImage instanceof VolatileImage))
/*     */       {
/* 127 */         switch (((VolatileImage)localImage).validate(localGraphicsConfiguration)) {
/*     */         case 2:
/* 129 */           ((VolatileImage)localImage).flush();
/* 130 */           localImage = null;
/* 131 */           break;
/*     */         case 1:
/* 133 */           j = 1;
/*     */         }
/*     */       }
/*     */ 
/* 137 */       if (localImage == null)
/*     */       {
/* 139 */         localImage = createImage(paramComponent, paramInt3, paramInt4, localGraphicsConfiguration, paramArrayOfObject);
/* 140 */         localImageCache.setImage(localClass, localGraphicsConfiguration, paramInt3, paramInt4, paramArrayOfObject, localImage);
/* 141 */         j = 1;
/*     */       }
/* 143 */       if (j != 0)
/*     */       {
/* 145 */         Graphics localGraphics = localImage.getGraphics();
/* 146 */         paintToImage(paramComponent, localImage, localGraphics, paramInt3, paramInt4, paramArrayOfObject);
/* 147 */         localGraphics.dispose();
/*     */       }
/*     */ 
/* 151 */       paintImage(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, localImage, paramArrayOfObject);
/*     */ 
/* 157 */       if ((!(localImage instanceof VolatileImage)) || (!((VolatileImage)localImage).contentsLost())) break; i++; } while (i < 3);
/*     */   }
/*     */ 
/*     */   protected abstract void paintToImage(Component paramComponent, Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, Object[] paramArrayOfObject);
/*     */ 
/*     */   protected void paintImage(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Image paramImage, Object[] paramArrayOfObject)
/*     */   {
/* 189 */     paramGraphics.drawImage(paramImage, paramInt1, paramInt2, null);
/*     */   }
/*     */ 
/*     */   protected Image createImage(Component paramComponent, int paramInt1, int paramInt2, GraphicsConfiguration paramGraphicsConfiguration, Object[] paramArrayOfObject)
/*     */   {
/* 206 */     if (paramGraphicsConfiguration == null) {
/* 207 */       return new BufferedImage(paramInt1, paramInt2, 1);
/*     */     }
/* 209 */     return paramGraphicsConfiguration.createCompatibleVolatileImage(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void flush()
/*     */   {
/* 216 */     synchronized (CachedPainter.class) {
/* 217 */       getCache(getClass()).flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   private GraphicsConfiguration getGraphicsConfiguration(Component paramComponent) {
/* 222 */     if (paramComponent == null) {
/* 223 */       return null;
/*     */     }
/* 225 */     return paramComponent.getGraphicsConfiguration();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.CachedPainter
 * JD-Core Version:    0.6.2
 */
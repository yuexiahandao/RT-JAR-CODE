/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Image;
/*     */ import java.awt.ImageCapabilities;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.VolatileImage;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.SurfaceDataProxy;
/*     */ 
/*     */ public abstract class SurfaceManager
/*     */ {
/*     */   private static ImageAccessor imgaccessor;
/*     */   private ConcurrentHashMap cacheMap;
/*     */ 
/*     */   public static void setImageAccessor(ImageAccessor paramImageAccessor)
/*     */   {
/*  62 */     if (imgaccessor != null) {
/*  63 */       throw new InternalError("Attempt to set ImageAccessor twice");
/*     */     }
/*  65 */     imgaccessor = paramImageAccessor;
/*     */   }
/*     */ 
/*     */   public static SurfaceManager getManager(Image paramImage)
/*     */   {
/*  72 */     Object localObject = imgaccessor.getSurfaceManager(paramImage);
/*  73 */     if (localObject == null)
/*     */     {
/*     */       try
/*     */       {
/*  78 */         BufferedImage localBufferedImage = (BufferedImage)paramImage;
/*  79 */         localObject = new BufImgSurfaceManager(localBufferedImage);
/*  80 */         setManager(localBufferedImage, (SurfaceManager)localObject);
/*     */       } catch (ClassCastException localClassCastException) {
/*  82 */         throw new IllegalArgumentException("Invalid Image variant");
/*     */       }
/*     */     }
/*  85 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static void setManager(Image paramImage, SurfaceManager paramSurfaceManager) {
/*  89 */     imgaccessor.setSurfaceManager(paramImage, paramSurfaceManager);
/*     */   }
/*     */ 
/*     */   public Object getCacheData(Object paramObject)
/*     */   {
/* 115 */     return this.cacheMap == null ? null : this.cacheMap.get(paramObject);
/*     */   }
/*     */ 
/*     */   public void setCacheData(Object paramObject1, Object paramObject2)
/*     */   {
/* 124 */     if (this.cacheMap == null) {
/* 125 */       synchronized (this) {
/* 126 */         if (this.cacheMap == null) {
/* 127 */           this.cacheMap = new ConcurrentHashMap(2);
/*     */         }
/*     */       }
/*     */     }
/* 131 */     this.cacheMap.put(paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public abstract SurfaceData getPrimarySurfaceData();
/*     */ 
/*     */   public abstract SurfaceData restoreContents();
/*     */ 
/*     */   public void acceleratedSurfaceLost()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ImageCapabilities getCapabilities(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 185 */     return new ImageCapabilitiesGc(paramGraphicsConfiguration);
/*     */   }
/*     */ 
/*     */   public synchronized void flush()
/*     */   {
/* 244 */     flush(false);
/*     */   }
/*     */ 
/*     */   synchronized void flush(boolean paramBoolean) {
/* 248 */     if (this.cacheMap != null) {
/* 249 */       Iterator localIterator = this.cacheMap.values().iterator();
/* 250 */       while (localIterator.hasNext()) {
/* 251 */         Object localObject = localIterator.next();
/* 252 */         if (((localObject instanceof FlushableCacheData)) && 
/* 253 */           (((FlushableCacheData)localObject).flush(paramBoolean)))
/* 254 */           localIterator.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAccelerationPriority(float paramFloat)
/*     */   {
/* 287 */     if (paramFloat == 0.0F)
/* 288 */       flush(true);
/*     */   }
/*     */ 
/*     */   public static int getImageScale(Image paramImage)
/*     */   {
/* 299 */     if (!(paramImage instanceof VolatileImage)) {
/* 300 */       return 1;
/*     */     }
/* 302 */     SurfaceManager localSurfaceManager = getManager(paramImage);
/* 303 */     return localSurfaceManager.getPrimarySurfaceData().getDefaultScale();
/*     */   }
/*     */ 
/*     */   public static abstract interface FlushableCacheData
/*     */   {
/*     */     public abstract boolean flush(boolean paramBoolean);
/*     */   }
/*     */ 
/*     */   public static abstract class ImageAccessor
/*     */   {
/*     */     public abstract SurfaceManager getSurfaceManager(Image paramImage);
/*     */ 
/*     */     public abstract void setSurfaceManager(Image paramImage, SurfaceManager paramSurfaceManager);
/*     */   }
/*     */ 
/*     */   class ImageCapabilitiesGc extends ImageCapabilities
/*     */   {
/*     */     GraphicsConfiguration gc;
/*     */ 
/*     */     public ImageCapabilitiesGc(GraphicsConfiguration arg2)
/*     */     {
/* 192 */       super();
/*     */       Object localObject;
/* 193 */       this.gc = localObject;
/*     */     }
/*     */ 
/*     */     public boolean isAccelerated()
/*     */     {
/* 200 */       GraphicsConfiguration localGraphicsConfiguration = this.gc;
/* 201 */       if (localGraphicsConfiguration == null) {
/* 202 */         localGraphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
/*     */       }
/*     */ 
/* 205 */       if ((localGraphicsConfiguration instanceof SurfaceManager.ProxiedGraphicsConfig)) {
/* 206 */         Object localObject = ((SurfaceManager.ProxiedGraphicsConfig)localGraphicsConfiguration).getProxyKey();
/*     */ 
/* 208 */         if (localObject != null) {
/* 209 */           SurfaceDataProxy localSurfaceDataProxy = (SurfaceDataProxy)SurfaceManager.this.getCacheData(localObject);
/*     */ 
/* 211 */           return (localSurfaceDataProxy != null) && (localSurfaceDataProxy.isAccelerated());
/*     */         }
/*     */       }
/* 214 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface ProxiedGraphicsConfig
/*     */   {
/*     */     public abstract Object getProxyKey();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.SurfaceManager
 * JD-Core Version:    0.6.2
 */
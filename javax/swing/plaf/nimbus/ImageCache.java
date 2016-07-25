/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ 
/*     */ class ImageCache
/*     */ {
/*  47 */   private final LinkedHashMap<Integer, PixelCountSoftReference> map = new LinkedHashMap(16, 0.75F, true);
/*     */   private final int maxPixelCount;
/*     */   private final int maxSingleImagePixelSize;
/*  54 */   private int currentPixelCount = 0;
/*     */ 
/*  56 */   private ReadWriteLock lock = new ReentrantReadWriteLock();
/*     */ 
/*  58 */   private ReferenceQueue<Image> referenceQueue = new ReferenceQueue();
/*     */ 
/*  60 */   private static final ImageCache instance = new ImageCache();
/*     */ 
/*     */   static ImageCache getInstance()
/*     */   {
/*  65 */     return instance;
/*     */   }
/*     */ 
/*     */   public ImageCache() {
/*  69 */     this.maxPixelCount = 2097152;
/*  70 */     this.maxSingleImagePixelSize = 90000;
/*     */   }
/*     */ 
/*     */   public ImageCache(int paramInt1, int paramInt2) {
/*  74 */     this.maxPixelCount = paramInt1;
/*  75 */     this.maxSingleImagePixelSize = paramInt2;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/*  80 */     this.lock.readLock().lock();
/*     */     try {
/*  82 */       this.map.clear();
/*     */     } finally {
/*  84 */       this.lock.readLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isImageCachable(int paramInt1, int paramInt2)
/*     */   {
/*  96 */     return paramInt1 * paramInt2 < this.maxSingleImagePixelSize;
/*     */   }
/*     */ 
/*     */   public Image getImage(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 109 */     this.lock.readLock().lock();
/*     */     try {
/* 111 */       PixelCountSoftReference localPixelCountSoftReference = (PixelCountSoftReference)this.map.get(Integer.valueOf(hash(paramGraphicsConfiguration, paramInt1, paramInt2, paramArrayOfObject)));
/*     */       Image localImage;
/* 113 */       if ((localPixelCountSoftReference != null) && (localPixelCountSoftReference.equals(paramGraphicsConfiguration, paramInt1, paramInt2, paramArrayOfObject))) {
/* 114 */         return (Image)localPixelCountSoftReference.get();
/*     */       }
/* 116 */       return null;
/*     */     }
/*     */     finally {
/* 119 */       this.lock.readLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean setImage(Image paramImage, GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 134 */     if (!isImageCachable(paramInt1, paramInt2)) return false;
/* 135 */     int i = hash(paramGraphicsConfiguration, paramInt1, paramInt2, paramArrayOfObject);
/* 136 */     this.lock.writeLock().lock();
/*     */     try {
/* 138 */       PixelCountSoftReference localPixelCountSoftReference = (PixelCountSoftReference)this.map.get(Integer.valueOf(i));
/*     */ 
/* 140 */       if ((localPixelCountSoftReference != null) && (localPixelCountSoftReference.get() == paramImage)) {
/* 141 */         return true;
/*     */       }
/*     */ 
/* 144 */       if (localPixelCountSoftReference != null) {
/* 145 */         this.currentPixelCount -= localPixelCountSoftReference.pixelCount;
/* 146 */         this.map.remove(Integer.valueOf(i));
/*     */       }
/*     */ 
/* 149 */       int j = paramImage.getWidth(null) * paramImage.getHeight(null);
/* 150 */       this.currentPixelCount += j;
/*     */ 
/* 152 */       if (this.currentPixelCount > this.maxPixelCount) {
/* 153 */         while ((localPixelCountSoftReference = (PixelCountSoftReference)this.referenceQueue.poll()) != null)
/*     */         {
/* 155 */           this.map.remove(Integer.valueOf(localPixelCountSoftReference.hash));
/* 156 */           this.currentPixelCount -= localPixelCountSoftReference.pixelCount;
/*     */         }
/*     */       }
/*     */ 
/* 160 */       if (this.currentPixelCount > this.maxPixelCount) {
/* 161 */         Iterator localIterator = this.map.entrySet().iterator();
/* 162 */         while ((this.currentPixelCount > this.maxPixelCount) && (localIterator.hasNext())) {
/* 163 */           Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 164 */           localIterator.remove();
/* 165 */           Image localImage = (Image)((PixelCountSoftReference)localEntry.getValue()).get();
/* 166 */           if (localImage != null) localImage.flush();
/* 167 */           this.currentPixelCount -= ((PixelCountSoftReference)localEntry.getValue()).pixelCount;
/*     */         }
/*     */       }
/*     */ 
/* 171 */       this.map.put(Integer.valueOf(i), new PixelCountSoftReference(paramImage, this.referenceQueue, j, i, paramGraphicsConfiguration, paramInt1, paramInt2, paramArrayOfObject));
/* 172 */       return true;
/*     */     } finally {
/* 174 */       this.lock.writeLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   private int hash(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 181 */     int i = paramGraphicsConfiguration != null ? paramGraphicsConfiguration.hashCode() : 0;
/* 182 */     i = 31 * i + paramInt1;
/* 183 */     i = 31 * i + paramInt2;
/* 184 */     i = 31 * i + Arrays.deepHashCode(paramArrayOfObject);
/* 185 */     return i;
/*     */   }
/*     */   private static class PixelCountSoftReference extends SoftReference<Image> {
/*     */     private final int pixelCount;
/*     */     private final int hash;
/*     */     private final GraphicsConfiguration config;
/*     */     private final int w;
/*     */     private final int h;
/*     */     private final Object[] args;
/*     */ 
/*     */     public PixelCountSoftReference(Image paramImage, ReferenceQueue<? super Image> paramReferenceQueue, int paramInt1, int paramInt2, GraphicsConfiguration paramGraphicsConfiguration, int paramInt3, int paramInt4, Object[] paramArrayOfObject) {
/* 201 */       super(paramReferenceQueue);
/* 202 */       this.pixelCount = paramInt1;
/* 203 */       this.hash = paramInt2;
/* 204 */       this.config = paramGraphicsConfiguration;
/* 205 */       this.w = paramInt3;
/* 206 */       this.h = paramInt4;
/* 207 */       this.args = paramArrayOfObject;
/*     */     }
/*     */ 
/*     */     public boolean equals(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object[] paramArrayOfObject) {
/* 211 */       return (paramGraphicsConfiguration == this.config) && (paramInt1 == this.w) && (paramInt2 == this.h) && (Arrays.equals(paramArrayOfObject, this.args));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ImageCache
 * JD-Core Version:    0.6.2
 */
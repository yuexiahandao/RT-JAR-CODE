/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.image.AreaAveragingScaleFilter;
/*     */ import java.awt.image.FilteredImageSource;
/*     */ import java.awt.image.ImageFilter;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.image.ReplicateScaleFilter;
/*     */ import sun.awt.image.SurfaceManager;
/*     */ import sun.awt.image.SurfaceManager.ImageAccessor;
/*     */ 
/*     */ public abstract class Image
/*     */ {
/*  52 */   private static ImageCapabilities defaultImageCaps = new ImageCapabilities(false);
/*     */ 
/*  62 */   protected float accelerationPriority = 0.5F;
/*     */ 
/* 136 */   public static final Object UndefinedProperty = new Object();
/*     */   public static final int SCALE_DEFAULT = 1;
/*     */   public static final int SCALE_FAST = 2;
/*     */   public static final int SCALE_SMOOTH = 4;
/*     */   public static final int SCALE_REPLICATE = 8;
/*     */   public static final int SCALE_AREA_AVERAGING = 16;
/*     */   SurfaceManager surfaceManager;
/*     */ 
/*     */   public abstract int getWidth(ImageObserver paramImageObserver);
/*     */ 
/*     */   public abstract int getHeight(ImageObserver paramImageObserver);
/*     */ 
/*     */   public abstract ImageProducer getSource();
/*     */ 
/*     */   public abstract Graphics getGraphics();
/*     */ 
/*     */   public abstract Object getProperty(String paramString, ImageObserver paramImageObserver);
/*     */ 
/*     */   public Image getScaledInstance(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*     */     Object localObject;
/* 170 */     if ((paramInt3 & 0x14) != 0)
/* 171 */       localObject = new AreaAveragingScaleFilter(paramInt1, paramInt2);
/*     */     else {
/* 173 */       localObject = new ReplicateScaleFilter(paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 176 */     FilteredImageSource localFilteredImageSource = new FilteredImageSource(getSource(), (ImageFilter)localObject);
/* 177 */     return Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 259 */     if (this.surfaceManager != null)
/* 260 */       this.surfaceManager.flush();
/*     */   }
/*     */ 
/*     */   public ImageCapabilities getCapabilities(GraphicsConfiguration paramGraphicsConfiguration)
/*     */   {
/* 287 */     if (this.surfaceManager != null) {
/* 288 */       return this.surfaceManager.getCapabilities(paramGraphicsConfiguration);
/*     */     }
/*     */ 
/* 296 */     return defaultImageCaps;
/*     */   }
/*     */ 
/*     */   public void setAccelerationPriority(float paramFloat)
/*     */   {
/* 320 */     if ((paramFloat < 0.0F) || (paramFloat > 1.0F)) {
/* 321 */       throw new IllegalArgumentException("Priority must be a value between 0 and 1, inclusive");
/*     */     }
/*     */ 
/* 324 */     this.accelerationPriority = paramFloat;
/* 325 */     if (this.surfaceManager != null)
/* 326 */       this.surfaceManager.setAccelerationPriority(this.accelerationPriority);
/*     */   }
/*     */ 
/*     */   public float getAccelerationPriority()
/*     */   {
/* 338 */     return this.accelerationPriority;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 344 */     SurfaceManager.setImageAccessor(new SurfaceManager.ImageAccessor() {
/*     */       public SurfaceManager getSurfaceManager(Image paramAnonymousImage) {
/* 346 */         return paramAnonymousImage.surfaceManager;
/*     */       }
/*     */       public void setSurfaceManager(Image paramAnonymousImage, SurfaceManager paramAnonymousSurfaceManager) {
/* 349 */         paramAnonymousImage.surfaceManager = paramAnonymousSurfaceManager;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Image
 * JD-Core Version:    0.6.2
 */
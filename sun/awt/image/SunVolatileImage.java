/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.ImageCapabilities;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.awt.image.VolatileImage;
/*     */ import sun.java2d.DestSurfaceProvider;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.Surface;
/*     */ import sun.java2d.SurfaceManagerFactory;
/*     */ import sun.print.PrinterGraphicsConfig;
/*     */ 
/*     */ public class SunVolatileImage extends VolatileImage
/*     */   implements DestSurfaceProvider
/*     */ {
/*     */   protected VolatileSurfaceManager volSurfaceManager;
/*     */   protected Component comp;
/*     */   private GraphicsConfiguration graphicsConfig;
/*     */   private Font defaultFont;
/*     */   private int width;
/*     */   private int height;
/*     */   private int forcedAccelSurfaceType;
/*     */ 
/*     */   protected SunVolatileImage(Component paramComponent, GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object paramObject, int paramInt3, ImageCapabilities paramImageCapabilities, int paramInt4)
/*     */   {
/*  71 */     this.comp = paramComponent;
/*  72 */     this.graphicsConfig = paramGraphicsConfiguration;
/*  73 */     this.width = paramInt1;
/*  74 */     this.height = paramInt2;
/*  75 */     this.forcedAccelSurfaceType = paramInt4;
/*  76 */     if ((paramInt3 != 1) && (paramInt3 != 2) && (paramInt3 != 3))
/*     */     {
/*  80 */       throw new IllegalArgumentException("Unknown transparency type:" + paramInt3);
/*     */     }
/*     */ 
/*  83 */     this.transparency = paramInt3;
/*  84 */     this.volSurfaceManager = createSurfaceManager(paramObject, paramImageCapabilities);
/*  85 */     SurfaceManager.setManager(this, this.volSurfaceManager);
/*     */ 
/*  88 */     this.volSurfaceManager.initialize();
/*     */ 
/*  90 */     this.volSurfaceManager.initContents();
/*     */   }
/*     */ 
/*     */   private SunVolatileImage(Component paramComponent, GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, Object paramObject, ImageCapabilities paramImageCapabilities)
/*     */   {
/*  98 */     this(paramComponent, paramGraphicsConfiguration, paramInt1, paramInt2, paramObject, 1, paramImageCapabilities, 0);
/*     */   }
/*     */ 
/*     */   public SunVolatileImage(Component paramComponent, int paramInt1, int paramInt2)
/*     */   {
/* 103 */     this(paramComponent, paramInt1, paramInt2, null);
/*     */   }
/*     */ 
/*     */   public SunVolatileImage(Component paramComponent, int paramInt1, int paramInt2, Object paramObject)
/*     */   {
/* 109 */     this(paramComponent, paramComponent.getGraphicsConfiguration(), paramInt1, paramInt2, paramObject, null);
/*     */   }
/*     */ 
/*     */   public SunVolatileImage(GraphicsConfiguration paramGraphicsConfiguration, int paramInt1, int paramInt2, int paramInt3, ImageCapabilities paramImageCapabilities)
/*     */   {
/* 117 */     this(null, paramGraphicsConfiguration, paramInt1, paramInt2, null, paramInt3, paramImageCapabilities, 0);
/*     */   }
/*     */ 
/*     */   public int getWidth()
/*     */   {
/* 122 */     return this.width;
/*     */   }
/*     */ 
/*     */   public int getHeight() {
/* 126 */     return this.height;
/*     */   }
/*     */ 
/*     */   public GraphicsConfiguration getGraphicsConfig() {
/* 130 */     return this.graphicsConfig;
/*     */   }
/*     */ 
/*     */   public void updateGraphicsConfig()
/*     */   {
/* 137 */     if (this.comp != null) {
/* 138 */       GraphicsConfiguration localGraphicsConfiguration = this.comp.getGraphicsConfiguration();
/* 139 */       if (localGraphicsConfiguration != null)
/*     */       {
/* 143 */         this.graphicsConfig = localGraphicsConfiguration;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Component getComponent() {
/* 149 */     return this.comp;
/*     */   }
/*     */ 
/*     */   public int getForcedAccelSurfaceType() {
/* 153 */     return this.forcedAccelSurfaceType;
/*     */   }
/*     */ 
/*     */   protected VolatileSurfaceManager createSurfaceManager(Object paramObject, ImageCapabilities paramImageCapabilities)
/*     */   {
/* 171 */     if (((this.graphicsConfig instanceof BufferedImageGraphicsConfig)) || ((this.graphicsConfig instanceof PrinterGraphicsConfig)) || ((paramImageCapabilities != null) && (!paramImageCapabilities.isAccelerated())))
/*     */     {
/* 175 */       return new BufImgVolatileSurfaceManager(this, paramObject);
/*     */     }
/* 177 */     SurfaceManagerFactory localSurfaceManagerFactory = SurfaceManagerFactory.getInstance();
/* 178 */     return localSurfaceManagerFactory.createVolatileManager(this, paramObject);
/*     */   }
/*     */ 
/*     */   private Color getForeground() {
/* 182 */     if (this.comp != null) {
/* 183 */       return this.comp.getForeground();
/*     */     }
/* 185 */     return Color.black;
/*     */   }
/*     */ 
/*     */   private Color getBackground()
/*     */   {
/* 190 */     if (this.comp != null) {
/* 191 */       return this.comp.getBackground();
/*     */     }
/* 193 */     return Color.white;
/*     */   }
/*     */ 
/*     */   private Font getFont()
/*     */   {
/* 198 */     if (this.comp != null) {
/* 199 */       return this.comp.getFont();
/*     */     }
/* 201 */     if (this.defaultFont == null) {
/* 202 */       this.defaultFont = new Font("Dialog", 0, 12);
/*     */     }
/* 204 */     return this.defaultFont;
/*     */   }
/*     */ 
/*     */   public Graphics2D createGraphics()
/*     */   {
/* 209 */     return new SunGraphics2D(this.volSurfaceManager.getPrimarySurfaceData(), getForeground(), getBackground(), getFont());
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString, ImageObserver paramImageObserver)
/*     */   {
/* 217 */     if (paramString == null) {
/* 218 */       throw new NullPointerException("null property name is not allowed");
/*     */     }
/* 220 */     return Image.UndefinedProperty;
/*     */   }
/*     */ 
/*     */   public int getWidth(ImageObserver paramImageObserver) {
/* 224 */     return getWidth();
/*     */   }
/*     */ 
/*     */   public int getHeight(ImageObserver paramImageObserver) {
/* 228 */     return getHeight();
/*     */   }
/*     */ 
/*     */   public BufferedImage getBackupImage()
/*     */   {
/* 236 */     return this.graphicsConfig.createCompatibleImage(getWidth(), getHeight(), getTransparency());
/*     */   }
/*     */ 
/*     */   public BufferedImage getSnapshot()
/*     */   {
/* 241 */     BufferedImage localBufferedImage = getBackupImage();
/* 242 */     Graphics2D localGraphics2D = localBufferedImage.createGraphics();
/* 243 */     localGraphics2D.setComposite(AlphaComposite.Src);
/* 244 */     localGraphics2D.drawImage(this, 0, 0, null);
/* 245 */     localGraphics2D.dispose();
/* 246 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   public int validate(GraphicsConfiguration paramGraphicsConfiguration) {
/* 250 */     return this.volSurfaceManager.validate(paramGraphicsConfiguration);
/*     */   }
/*     */ 
/*     */   public boolean contentsLost() {
/* 254 */     return this.volSurfaceManager.contentsLost();
/*     */   }
/*     */ 
/*     */   public ImageCapabilities getCapabilities() {
/* 258 */     return this.volSurfaceManager.getCapabilities(this.graphicsConfig);
/*     */   }
/*     */ 
/*     */   public Surface getDestSurface()
/*     */   {
/* 268 */     return this.volSurfaceManager.getPrimarySurfaceData();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.SunVolatileImage
 * JD-Core Version:    0.6.2
 */
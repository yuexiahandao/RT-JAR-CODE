/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.SystemColor;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ 
/*     */ public class OffScreenImage extends BufferedImage
/*     */ {
/*     */   protected Component c;
/*     */   private OffScreenImageSource osis;
/*     */   private Font defaultFont;
/*     */ 
/*     */   public OffScreenImage(Component paramComponent, ColorModel paramColorModel, WritableRaster paramWritableRaster, boolean paramBoolean)
/*     */   {
/*  62 */     super(paramColorModel, paramWritableRaster, paramBoolean, null);
/*  63 */     this.c = paramComponent;
/*  64 */     initSurface(paramWritableRaster.getWidth(), paramWritableRaster.getHeight());
/*     */   }
/*     */ 
/*     */   public Graphics getGraphics() {
/*  68 */     return createGraphics();
/*     */   }
/*     */ 
/*     */   public Graphics2D createGraphics() {
/*  72 */     if (this.c == null) {
/*  73 */       localObject1 = GraphicsEnvironment.getLocalGraphicsEnvironment();
/*     */ 
/*  75 */       return ((GraphicsEnvironment)localObject1).createGraphics(this);
/*     */     }
/*     */ 
/*  78 */     Object localObject1 = this.c.getBackground();
/*  79 */     if (localObject1 == null) {
/*  80 */       localObject1 = SystemColor.window;
/*     */     }
/*     */ 
/*  83 */     Object localObject2 = this.c.getForeground();
/*  84 */     if (localObject2 == null) {
/*  85 */       localObject2 = SystemColor.windowText;
/*     */     }
/*     */ 
/*  88 */     Font localFont = this.c.getFont();
/*  89 */     if (localFont == null) {
/*  90 */       if (this.defaultFont == null) {
/*  91 */         this.defaultFont = new Font("Dialog", 0, 12);
/*     */       }
/*  93 */       localFont = this.defaultFont;
/*     */     }
/*     */ 
/*  96 */     return new SunGraphics2D(SurfaceData.getPrimarySurfaceData(this), (Color)localObject2, (Color)localObject1, localFont);
/*     */   }
/*     */ 
/*     */   private void initSurface(int paramInt1, int paramInt2)
/*     */   {
/* 101 */     Graphics2D localGraphics2D = createGraphics();
/*     */     try {
/* 103 */       localGraphics2D.clearRect(0, 0, paramInt1, paramInt2);
/*     */     } finally {
/* 105 */       localGraphics2D.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ImageProducer getSource() {
/* 110 */     if (this.osis == null) {
/* 111 */       this.osis = new OffScreenImageSource(this);
/*     */     }
/* 113 */     return this.osis;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.OffScreenImage
 * JD-Core Version:    0.6.2
 */
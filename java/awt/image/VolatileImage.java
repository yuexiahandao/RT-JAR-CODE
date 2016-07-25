/*     */ package java.awt.image;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.ImageCapabilities;
/*     */ import java.awt.Transparency;
/*     */ 
/*     */ public abstract class VolatileImage extends Image
/*     */   implements Transparency
/*     */ {
/*     */   public static final int IMAGE_OK = 0;
/*     */   public static final int IMAGE_RESTORED = 1;
/*     */   public static final int IMAGE_INCOMPATIBLE = 2;
/* 328 */   protected int transparency = 3;
/*     */ 
/*     */   public abstract BufferedImage getSnapshot();
/*     */ 
/*     */   public abstract int getWidth();
/*     */ 
/*     */   public abstract int getHeight();
/*     */ 
/*     */   public ImageProducer getSource()
/*     */   {
/* 218 */     return getSnapshot().getSource();
/*     */   }
/*     */ 
/*     */   public Graphics getGraphics()
/*     */   {
/* 233 */     return createGraphics();
/*     */   }
/*     */ 
/*     */   public abstract Graphics2D createGraphics();
/*     */ 
/*     */   public abstract int validate(GraphicsConfiguration paramGraphicsConfiguration);
/*     */ 
/*     */   public abstract boolean contentsLost();
/*     */ 
/*     */   public abstract ImageCapabilities getCapabilities();
/*     */ 
/*     */   public int getTransparency()
/*     */   {
/* 340 */     return this.transparency;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.VolatileImage
 * JD-Core Version:    0.6.2
 */
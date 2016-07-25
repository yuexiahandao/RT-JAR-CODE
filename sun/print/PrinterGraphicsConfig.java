/*     */ package sun.print;
/*     */ 
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.DirectColorModel;
/*     */ 
/*     */ public class PrinterGraphicsConfig extends GraphicsConfiguration
/*     */ {
/*     */   static ColorModel theModel;
/*     */   GraphicsDevice gd;
/*     */   int pageWidth;
/*     */   int pageHeight;
/*     */   AffineTransform deviceTransform;
/*     */ 
/*     */   public PrinterGraphicsConfig(String paramString, AffineTransform paramAffineTransform, int paramInt1, int paramInt2)
/*     */   {
/*  48 */     this.pageWidth = paramInt1;
/*  49 */     this.pageHeight = paramInt2;
/*  50 */     this.deviceTransform = paramAffineTransform;
/*  51 */     this.gd = new PrinterGraphicsDevice(this, paramString);
/*     */   }
/*     */ 
/*     */   public GraphicsDevice getDevice()
/*     */   {
/*  58 */     return this.gd;
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel()
/*     */   {
/*  65 */     if (theModel == null) {
/*  66 */       BufferedImage localBufferedImage = new BufferedImage(1, 1, 5);
/*     */ 
/*  68 */       theModel = localBufferedImage.getColorModel();
/*     */     }
/*     */ 
/*  71 */     return theModel;
/*     */   }
/*     */ 
/*     */   public ColorModel getColorModel(int paramInt)
/*     */   {
/*  79 */     switch (paramInt) {
/*     */     case 1:
/*  81 */       return getColorModel();
/*     */     case 2:
/*  83 */       return new DirectColorModel(25, 16711680, 65280, 255, 16777216);
/*     */     case 3:
/*  85 */       return ColorModel.getRGBdefault();
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   public AffineTransform getDefaultTransform()
/*     */   {
/* 101 */     return new AffineTransform(this.deviceTransform);
/*     */   }
/*     */ 
/*     */   public AffineTransform getNormalizingTransform()
/*     */   {
/* 124 */     return new AffineTransform();
/*     */   }
/*     */ 
/*     */   public Rectangle getBounds() {
/* 128 */     return new Rectangle(0, 0, this.pageWidth, this.pageHeight);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PrinterGraphicsConfig
 * JD-Core Version:    0.6.2
 */
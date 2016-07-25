/*     */ package com.sun.imageio.plugins.bmp;
/*     */ 
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.util.Locale;
/*     */ import javax.imageio.IIOException;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.spi.ImageWriterSpi;
/*     */ import javax.imageio.spi.ServiceRegistry;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ 
/*     */ public class BMPImageWriterSpi extends ImageWriterSpi
/*     */ {
/*  44 */   private static String[] readerSpiNames = { "com.sun.imageio.plugins.bmp.BMPImageReaderSpi" };
/*     */ 
/*  46 */   private static String[] formatNames = { "bmp", "BMP" };
/*  47 */   private static String[] entensions = { "bmp" };
/*  48 */   private static String[] mimeType = { "image/bmp" };
/*     */ 
/*  50 */   private boolean registered = false;
/*     */ 
/*     */   public BMPImageWriterSpi() {
/*  53 */     super("Oracle Corporation", "1.0", formatNames, entensions, mimeType, "com.sun.imageio.plugins.bmp.BMPImageWriter", new Class[] { ImageOutputStream.class }, readerSpiNames, false, null, null, null, null, true, "javax_imageio_bmp_1.0", "com.sun.imageio.plugins.bmp.BMPMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public String getDescription(Locale paramLocale)
/*     */   {
/*  70 */     return "Standard BMP Image Writer";
/*     */   }
/*     */ 
/*     */   public void onRegistration(ServiceRegistry paramServiceRegistry, Class<?> paramClass)
/*     */   {
/*  75 */     if (this.registered) {
/*  76 */       return;
/*     */     }
/*     */ 
/*  79 */     this.registered = true;
/*     */   }
/*     */ 
/*     */   public boolean canEncodeImage(ImageTypeSpecifier paramImageTypeSpecifier) {
/*  83 */     int i = paramImageTypeSpecifier.getSampleModel().getDataType();
/*  84 */     if ((i < 0) || (i > 3)) {
/*  85 */       return false;
/*     */     }
/*  87 */     SampleModel localSampleModel = paramImageTypeSpecifier.getSampleModel();
/*  88 */     int j = localSampleModel.getNumBands();
/*  89 */     if ((j != 1) && (j != 3)) {
/*  90 */       return false;
/*     */     }
/*  92 */     if ((j == 1) && (i != 0)) {
/*  93 */       return false;
/*     */     }
/*  95 */     if ((i > 0) && (!(localSampleModel instanceof SinglePixelPackedSampleModel)))
/*     */     {
/*  97 */       return false;
/*     */     }
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */   public ImageWriter createWriterInstance(Object paramObject) throws IIOException
/*     */   {
/* 104 */     return new BMPImageWriter(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.bmp.BMPImageWriterSpi
 * JD-Core Version:    0.6.2
 */
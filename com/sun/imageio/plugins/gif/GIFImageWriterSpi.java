/*     */ package com.sun.imageio.plugins.gif;
/*     */ 
/*     */ import com.sun.imageio.plugins.common.PaletteBuilder;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.util.Locale;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.ImageWriter;
/*     */ import javax.imageio.spi.ImageWriterSpi;
/*     */ import javax.imageio.stream.ImageOutputStream;
/*     */ 
/*     */ public class GIFImageWriterSpi extends ImageWriterSpi
/*     */ {
/*     */   private static final String vendorName = "Oracle Corporation";
/*     */   private static final String version = "1.0";
/*  43 */   private static final String[] names = { "gif", "GIF" };
/*     */ 
/*  45 */   private static final String[] suffixes = { "gif" };
/*     */ 
/*  47 */   private static final String[] MIMETypes = { "image/gif" };
/*     */   private static final String writerClassName = "com.sun.imageio.plugins.gif.GIFImageWriter";
/*  52 */   private static final String[] readerSpiNames = { "com.sun.imageio.plugins.gif.GIFImageReaderSpi" };
/*     */ 
/*     */   public GIFImageWriterSpi()
/*     */   {
/*  57 */     super("Oracle Corporation", "1.0", names, suffixes, MIMETypes, "com.sun.imageio.plugins.gif.GIFImageWriter", new Class[] { ImageOutputStream.class }, readerSpiNames, true, "javax_imageio_gif_stream_1.0", "com.sun.imageio.plugins.gif.GIFStreamMetadataFormat", null, null, true, "javax_imageio_gif_image_1.0", "com.sun.imageio.plugins.gif.GIFImageMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public boolean canEncodeImage(ImageTypeSpecifier paramImageTypeSpecifier)
/*     */   {
/*  77 */     if (paramImageTypeSpecifier == null) {
/*  78 */       throw new IllegalArgumentException("type == null!");
/*     */     }
/*     */ 
/*  81 */     SampleModel localSampleModel = paramImageTypeSpecifier.getSampleModel();
/*  82 */     ColorModel localColorModel = paramImageTypeSpecifier.getColorModel();
/*     */ 
/*  84 */     int i = (localSampleModel.getNumBands() == 1) && (localSampleModel.getSampleSize(0) <= 8) && (localSampleModel.getWidth() <= 65535) && (localSampleModel.getHeight() <= 65535) && ((localColorModel == null) || (localColorModel.getComponentSize()[0] <= 8)) ? 1 : 0;
/*     */ 
/*  90 */     if (i != 0) {
/*  91 */       return true;
/*     */     }
/*  93 */     return PaletteBuilder.canCreatePalette(paramImageTypeSpecifier);
/*     */   }
/*     */ 
/*     */   public String getDescription(Locale paramLocale)
/*     */   {
/*  98 */     return "Standard GIF image writer";
/*     */   }
/*     */ 
/*     */   public ImageWriter createWriterInstance(Object paramObject) {
/* 102 */     return new GIFImageWriter(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFImageWriterSpi
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.imageio.plugins.png;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import javax.imageio.ImageReader;
/*     */ import javax.imageio.spi.ImageReaderSpi;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ 
/*     */ public class PNGImageReaderSpi extends ImageReaderSpi
/*     */ {
/*     */   private static final String vendorName = "Oracle Corporation";
/*     */   private static final String version = "1.0";
/*  43 */   private static final String[] names = { "png", "PNG" };
/*     */ 
/*  45 */   private static final String[] suffixes = { "png" };
/*     */ 
/*  47 */   private static final String[] MIMETypes = { "image/png", "image/x-png" };
/*     */   private static final String readerClassName = "com.sun.imageio.plugins.png.PNGImageReader";
/*  52 */   private static final String[] writerSpiNames = { "com.sun.imageio.plugins.png.PNGImageWriterSpi" };
/*     */ 
/*     */   public PNGImageReaderSpi()
/*     */   {
/*  57 */     super("Oracle Corporation", "1.0", names, suffixes, MIMETypes, "com.sun.imageio.plugins.png.PNGImageReader", new Class[] { ImageInputStream.class }, writerSpiNames, false, null, null, null, null, true, "javax_imageio_png_1.0", "com.sun.imageio.plugins.png.PNGMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public String getDescription(Locale paramLocale)
/*     */   {
/*  76 */     return "Standard PNG image reader";
/*     */   }
/*     */ 
/*     */   public boolean canDecodeInput(Object paramObject) throws IOException {
/*  80 */     if (!(paramObject instanceof ImageInputStream)) {
/*  81 */       return false;
/*     */     }
/*     */ 
/*  84 */     ImageInputStream localImageInputStream = (ImageInputStream)paramObject;
/*  85 */     byte[] arrayOfByte = new byte[8];
/*  86 */     localImageInputStream.mark();
/*  87 */     localImageInputStream.readFully(arrayOfByte);
/*  88 */     localImageInputStream.reset();
/*     */ 
/*  90 */     return (arrayOfByte[0] == -119) && (arrayOfByte[1] == 80) && (arrayOfByte[2] == 78) && (arrayOfByte[3] == 71) && (arrayOfByte[4] == 13) && (arrayOfByte[5] == 10) && (arrayOfByte[6] == 26) && (arrayOfByte[7] == 10);
/*     */   }
/*     */ 
/*     */   public ImageReader createReaderInstance(Object paramObject)
/*     */   {
/* 101 */     return new PNGImageReader(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.PNGImageReaderSpi
 * JD-Core Version:    0.6.2
 */
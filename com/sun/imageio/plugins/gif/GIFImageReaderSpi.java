/*    */ package com.sun.imageio.plugins.gif;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import javax.imageio.ImageReader;
/*    */ import javax.imageio.spi.ImageReaderSpi;
/*    */ import javax.imageio.stream.ImageInputStream;
/*    */ 
/*    */ public class GIFImageReaderSpi extends ImageReaderSpi
/*    */ {
/*    */   private static final String vendorName = "Oracle Corporation";
/*    */   private static final String version = "1.0";
/* 43 */   private static final String[] names = { "gif", "GIF" };
/*    */ 
/* 45 */   private static final String[] suffixes = { "gif" };
/*    */ 
/* 47 */   private static final String[] MIMETypes = { "image/gif" };
/*    */   private static final String readerClassName = "com.sun.imageio.plugins.gif.GIFImageReader";
/* 52 */   private static final String[] writerSpiNames = { "com.sun.imageio.plugins.gif.GIFImageWriterSpi" };
/*    */ 
/*    */   public GIFImageReaderSpi()
/*    */   {
/* 57 */     super("Oracle Corporation", "1.0", names, suffixes, MIMETypes, "com.sun.imageio.plugins.gif.GIFImageReader", new Class[] { ImageInputStream.class }, writerSpiNames, true, "javax_imageio_gif_stream_1.0", "com.sun.imageio.plugins.gif.GIFStreamMetadataFormat", null, null, true, "javax_imageio_gif_image_1.0", "com.sun.imageio.plugins.gif.GIFImageMetadataFormat", null, null);
/*    */   }
/*    */ 
/*    */   public String getDescription(Locale paramLocale)
/*    */   {
/* 77 */     return "Standard GIF image reader";
/*    */   }
/*    */ 
/*    */   public boolean canDecodeInput(Object paramObject) throws IOException {
/* 81 */     if (!(paramObject instanceof ImageInputStream)) {
/* 82 */       return false;
/*    */     }
/*    */ 
/* 85 */     ImageInputStream localImageInputStream = (ImageInputStream)paramObject;
/* 86 */     byte[] arrayOfByte = new byte[6];
/* 87 */     localImageInputStream.mark();
/* 88 */     localImageInputStream.readFully(arrayOfByte);
/* 89 */     localImageInputStream.reset();
/*    */ 
/* 91 */     return (arrayOfByte[0] == 71) && (arrayOfByte[1] == 73) && (arrayOfByte[2] == 70) && (arrayOfByte[3] == 56) && ((arrayOfByte[4] == 55) || (arrayOfByte[4] == 57)) && (arrayOfByte[5] == 97);
/*    */   }
/*    */ 
/*    */   public ImageReader createReaderInstance(Object paramObject)
/*    */   {
/* 96 */     return new GIFImageReader(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFImageReaderSpi
 * JD-Core Version:    0.6.2
 */
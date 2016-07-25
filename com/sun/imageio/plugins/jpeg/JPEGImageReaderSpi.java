/*    */ package com.sun.imageio.plugins.jpeg;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import javax.imageio.IIOException;
/*    */ import javax.imageio.ImageReader;
/*    */ import javax.imageio.spi.ImageReaderSpi;
/*    */ import javax.imageio.stream.ImageInputStream;
/*    */ 
/*    */ public class JPEGImageReaderSpi extends ImageReaderSpi
/*    */ {
/* 39 */   private static String[] writerSpiNames = { "com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi" };
/*    */ 
/*    */   public JPEGImageReaderSpi()
/*    */   {
/* 43 */     super("Oracle Corporation", "0.5", JPEG.names, JPEG.suffixes, JPEG.MIMETypes, "com.sun.imageio.plugins.jpeg.JPEGImageReader", new Class[] { ImageInputStream.class }, writerSpiNames, true, "javax_imageio_jpeg_stream_1.0", "com.sun.imageio.plugins.jpeg.JPEGStreamMetadataFormat", null, null, true, "javax_imageio_jpeg_image_1.0", "com.sun.imageio.plugins.jpeg.JPEGImageMetadataFormat", null, null);
/*    */   }
/*    */ 
/*    */   public String getDescription(Locale paramLocale)
/*    */   {
/* 63 */     return "Standard JPEG Image Reader";
/*    */   }
/*    */ 
/*    */   public boolean canDecodeInput(Object paramObject) throws IOException {
/* 67 */     if (!(paramObject instanceof ImageInputStream)) {
/* 68 */       return false;
/*    */     }
/* 70 */     ImageInputStream localImageInputStream = (ImageInputStream)paramObject;
/* 71 */     localImageInputStream.mark();
/*    */ 
/* 74 */     int i = localImageInputStream.read();
/* 75 */     int j = localImageInputStream.read();
/* 76 */     localImageInputStream.reset();
/* 77 */     if ((i == 255) && (j == 216)) {
/* 78 */       return true;
/*    */     }
/* 80 */     return false;
/*    */   }
/*    */ 
/*    */   public ImageReader createReaderInstance(Object paramObject) throws IIOException
/*    */   {
/* 85 */     return new JPEGImageReader(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi
 * JD-Core Version:    0.6.2
 */
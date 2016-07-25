/*    */ package com.sun.imageio.plugins.bmp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Locale;
/*    */ import javax.imageio.IIOException;
/*    */ import javax.imageio.ImageReader;
/*    */ import javax.imageio.spi.ImageReaderSpi;
/*    */ import javax.imageio.spi.ServiceRegistry;
/*    */ import javax.imageio.stream.ImageInputStream;
/*    */ 
/*    */ public class BMPImageReaderSpi extends ImageReaderSpi
/*    */ {
/* 39 */   private static String[] writerSpiNames = { "com.sun.imageio.plugins.bmp.BMPImageWriterSpi" };
/*    */ 
/* 41 */   private static String[] formatNames = { "bmp", "BMP" };
/* 42 */   private static String[] entensions = { "bmp" };
/* 43 */   private static String[] mimeType = { "image/bmp" };
/*    */ 
/* 45 */   private boolean registered = false;
/*    */ 
/*    */   public BMPImageReaderSpi() {
/* 48 */     super("Oracle Corporation", "1.0", formatNames, entensions, mimeType, "com.sun.imageio.plugins.bmp.BMPImageReader", new Class[] { ImageInputStream.class }, writerSpiNames, false, null, null, null, null, true, "javax_imageio_bmp_1.0", "com.sun.imageio.plugins.bmp.BMPMetadataFormat", null, null);
/*    */   }
/*    */ 
/*    */   public void onRegistration(ServiceRegistry paramServiceRegistry, Class<?> paramClass)
/*    */   {
/* 66 */     if (this.registered) {
/* 67 */       return;
/*    */     }
/* 69 */     this.registered = true;
/*    */   }
/*    */ 
/*    */   public String getDescription(Locale paramLocale) {
/* 73 */     return "Standard BMP Image Reader";
/*    */   }
/*    */ 
/*    */   public boolean canDecodeInput(Object paramObject) throws IOException {
/* 77 */     if (!(paramObject instanceof ImageInputStream)) {
/* 78 */       return false;
/*    */     }
/*    */ 
/* 81 */     ImageInputStream localImageInputStream = (ImageInputStream)paramObject;
/* 82 */     byte[] arrayOfByte = new byte[2];
/* 83 */     localImageInputStream.mark();
/* 84 */     localImageInputStream.readFully(arrayOfByte);
/* 85 */     localImageInputStream.reset();
/*    */ 
/* 87 */     return (arrayOfByte[0] == 66) && (arrayOfByte[1] == 77);
/*    */   }
/*    */ 
/*    */   public ImageReader createReaderInstance(Object paramObject) throws IIOException
/*    */   {
/* 92 */     return new BMPImageReader(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.bmp.BMPImageReaderSpi
 * JD-Core Version:    0.6.2
 */
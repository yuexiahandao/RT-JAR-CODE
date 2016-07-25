/*    */ package com.sun.imageio.plugins.wbmp;
/*    */ 
/*    */ import java.awt.image.MultiPixelPackedSampleModel;
/*    */ import java.awt.image.SampleModel;
/*    */ import java.util.Locale;
/*    */ import javax.imageio.IIOException;
/*    */ import javax.imageio.ImageTypeSpecifier;
/*    */ import javax.imageio.ImageWriter;
/*    */ import javax.imageio.spi.ImageWriterSpi;
/*    */ import javax.imageio.spi.ServiceRegistry;
/*    */ import javax.imageio.stream.ImageOutputStream;
/*    */ 
/*    */ public class WBMPImageWriterSpi extends ImageWriterSpi
/*    */ {
/* 43 */   private static String[] readerSpiNames = { "com.sun.imageio.plugins.wbmp.WBMPImageReaderSpi" };
/*    */ 
/* 45 */   private static String[] formatNames = { "wbmp", "WBMP" };
/* 46 */   private static String[] entensions = { "wbmp" };
/* 47 */   private static String[] mimeType = { "image/vnd.wap.wbmp" };
/*    */ 
/* 49 */   private boolean registered = false;
/*    */ 
/*    */   public WBMPImageWriterSpi() {
/* 52 */     super("Oracle Corporation", "1.0", formatNames, entensions, mimeType, "com.sun.imageio.plugins.wbmp.WBMPImageWriter", new Class[] { ImageOutputStream.class }, readerSpiNames, true, null, null, null, null, true, null, null, null, null);
/*    */   }
/*    */ 
/*    */   public String getDescription(Locale paramLocale)
/*    */   {
/* 67 */     return "Standard WBMP Image Writer";
/*    */   }
/*    */ 
/*    */   public void onRegistration(ServiceRegistry paramServiceRegistry, Class<?> paramClass)
/*    */   {
/* 72 */     if (this.registered) {
/* 73 */       return;
/*    */     }
/*    */ 
/* 76 */     this.registered = true;
/*    */   }
/*    */ 
/*    */   public boolean canEncodeImage(ImageTypeSpecifier paramImageTypeSpecifier) {
/* 80 */     SampleModel localSampleModel = paramImageTypeSpecifier.getSampleModel();
/* 81 */     if (!(localSampleModel instanceof MultiPixelPackedSampleModel))
/* 82 */       return false;
/* 83 */     if (localSampleModel.getSampleSize(0) != 1) {
/* 84 */       return false;
/*    */     }
/* 86 */     return true;
/*    */   }
/*    */ 
/*    */   public ImageWriter createWriterInstance(Object paramObject) throws IIOException
/*    */   {
/* 91 */     return new WBMPImageWriter(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.wbmp.WBMPImageWriterSpi
 * JD-Core Version:    0.6.2
 */
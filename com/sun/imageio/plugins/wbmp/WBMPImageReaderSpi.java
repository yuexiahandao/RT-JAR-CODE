/*     */ package com.sun.imageio.plugins.wbmp;
/*     */ 
/*     */ import com.sun.imageio.plugins.common.ReaderUtil;
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import javax.imageio.IIOException;
/*     */ import javax.imageio.ImageReader;
/*     */ import javax.imageio.spi.ImageReaderSpi;
/*     */ import javax.imageio.spi.ServiceRegistry;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ 
/*     */ public class WBMPImageReaderSpi extends ImageReaderSpi
/*     */ {
/*     */   private static final int MAX_WBMP_WIDTH = 1024;
/*     */   private static final int MAX_WBMP_HEIGHT = 768;
/*  43 */   private static String[] writerSpiNames = { "com.sun.imageio.plugins.wbmp.WBMPImageWriterSpi" };
/*     */ 
/*  45 */   private static String[] formatNames = { "wbmp", "WBMP" };
/*  46 */   private static String[] entensions = { "wbmp" };
/*  47 */   private static String[] mimeType = { "image/vnd.wap.wbmp" };
/*     */ 
/*  49 */   private boolean registered = false;
/*     */ 
/*     */   public WBMPImageReaderSpi() {
/*  52 */     super("Oracle Corporation", "1.0", formatNames, entensions, mimeType, "com.sun.imageio.plugins.wbmp.WBMPImageReader", new Class[] { ImageInputStream.class }, writerSpiNames, true, null, null, null, null, true, "javax_imageio_wbmp_1.0", "com.sun.imageio.plugins.wbmp.WBMPMetadataFormat", null, null);
/*     */   }
/*     */ 
/*     */   public void onRegistration(ServiceRegistry paramServiceRegistry, Class<?> paramClass)
/*     */   {
/*  70 */     if (this.registered) {
/*  71 */       return;
/*     */     }
/*  73 */     this.registered = true;
/*     */   }
/*     */ 
/*     */   public String getDescription(Locale paramLocale) {
/*  77 */     return "Standard WBMP Image Reader";
/*     */   }
/*     */ 
/*     */   public boolean canDecodeInput(Object paramObject) throws IOException {
/*  81 */     if (!(paramObject instanceof ImageInputStream)) {
/*  82 */       return false;
/*     */     }
/*     */ 
/*  85 */     ImageInputStream localImageInputStream = (ImageInputStream)paramObject;
/*     */ 
/*  87 */     localImageInputStream.mark();
/*  88 */     int i = localImageInputStream.readByte();
/*  89 */     int j = localImageInputStream.readByte();
/*     */ 
/*  91 */     if ((i != 0) || (j != 0))
/*     */     {
/*  93 */       localImageInputStream.reset();
/*  94 */       return false;
/*     */     }
/*     */ 
/*  97 */     int k = ReaderUtil.readMultiByteInteger(localImageInputStream);
/*  98 */     int m = ReaderUtil.readMultiByteInteger(localImageInputStream);
/*     */ 
/* 100 */     if ((k <= 0) || (m <= 0)) {
/* 101 */       localImageInputStream.reset();
/* 102 */       return false;
/*     */     }
/*     */ 
/* 105 */     long l1 = localImageInputStream.length();
/* 106 */     if (l1 == -1L)
/*     */     {
/* 114 */       localImageInputStream.reset();
/* 115 */       return (k < 1024) && (m < 768);
/*     */     }
/*     */ 
/* 118 */     l1 -= localImageInputStream.getStreamPosition();
/* 119 */     localImageInputStream.reset();
/*     */ 
/* 121 */     long l2 = k / 8 + (k % 8 == 0 ? 0 : 1);
/*     */ 
/* 123 */     return l1 == l2 * m;
/*     */   }
/*     */ 
/*     */   public ImageReader createReaderInstance(Object paramObject) throws IIOException
/*     */   {
/* 128 */     return new WBMPImageReader(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.wbmp.WBMPImageReaderSpi
 * JD-Core Version:    0.6.2
 */
/*    */ package com.sun.imageio.spi;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Locale;
/*    */ import javax.imageio.spi.ImageInputStreamSpi;
/*    */ import javax.imageio.stream.FileCacheImageInputStream;
/*    */ import javax.imageio.stream.ImageInputStream;
/*    */ import javax.imageio.stream.MemoryCacheImageInputStream;
/*    */ 
/*    */ public class InputStreamImageInputStreamSpi extends ImageInputStreamSpi
/*    */ {
/*    */   private static final String vendorName = "Oracle Corporation";
/*    */   private static final String version = "1.0";
/* 43 */   private static final Class inputClass = InputStream.class;
/*    */ 
/*    */   public InputStreamImageInputStreamSpi() {
/* 46 */     super("Oracle Corporation", "1.0", inputClass);
/*    */   }
/*    */ 
/*    */   public String getDescription(Locale paramLocale) {
/* 50 */     return "Service provider that instantiates a FileCacheImageInputStream or MemoryCacheImageInputStream from an InputStream";
/*    */   }
/*    */ 
/*    */   public boolean canUseCacheFile() {
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   public boolean needsCacheFile() {
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   public ImageInputStream createInputStreamInstance(Object paramObject, boolean paramBoolean, File paramFile)
/*    */     throws IOException
/*    */   {
/* 65 */     if ((paramObject instanceof InputStream)) {
/* 66 */       InputStream localInputStream = (InputStream)paramObject;
/*    */ 
/* 68 */       if (paramBoolean) {
/* 69 */         return new FileCacheImageInputStream(localInputStream, paramFile);
/*    */       }
/* 71 */       return new MemoryCacheImageInputStream(localInputStream);
/*    */     }
/*    */ 
/* 74 */     throw new IllegalArgumentException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.spi.InputStreamImageInputStreamSpi
 * JD-Core Version:    0.6.2
 */
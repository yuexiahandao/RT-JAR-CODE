/*    */ package com.sun.imageio.spi;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Locale;
/*    */ import javax.imageio.spi.ImageInputStreamSpi;
/*    */ import javax.imageio.stream.FileImageInputStream;
/*    */ import javax.imageio.stream.ImageInputStream;
/*    */ 
/*    */ public class FileImageInputStreamSpi extends ImageInputStreamSpi
/*    */ {
/*    */   private static final String vendorName = "Oracle Corporation";
/*    */   private static final String version = "1.0";
/* 40 */   private static final Class inputClass = File.class;
/*    */ 
/*    */   public FileImageInputStreamSpi() {
/* 43 */     super("Oracle Corporation", "1.0", inputClass);
/*    */   }
/*    */ 
/*    */   public String getDescription(Locale paramLocale) {
/* 47 */     return "Service provider that instantiates a FileImageInputStream from a File";
/*    */   }
/*    */ 
/*    */   public ImageInputStream createInputStreamInstance(Object paramObject, boolean paramBoolean, File paramFile)
/*    */   {
/* 53 */     if ((paramObject instanceof File)) {
/*    */       try {
/* 55 */         return new FileImageInputStream((File)paramObject);
/*    */       } catch (Exception localException) {
/* 57 */         return null;
/*    */       }
/*    */     }
/* 60 */     throw new IllegalArgumentException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.spi.FileImageInputStreamSpi
 * JD-Core Version:    0.6.2
 */
/*    */ package com.sun.image.codec.jpeg;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.Raster;
/*    */ 
/*    */ public class TruncatedFileException extends RuntimeException
/*    */ {
/* 41 */   private Raster ras = null;
/* 42 */   private BufferedImage bi = null;
/*    */ 
/*    */   public TruncatedFileException(BufferedImage paramBufferedImage)
/*    */   {
/* 53 */     super("Premature end of input file");
/* 54 */     this.bi = paramBufferedImage;
/* 55 */     this.ras = paramBufferedImage.getData();
/*    */   }
/*    */ 
/*    */   public TruncatedFileException(Raster paramRaster)
/*    */   {
/* 66 */     super("Premature end of input file");
/* 67 */     this.ras = paramRaster;
/*    */   }
/*    */ 
/*    */   public Raster getRaster()
/*    */   {
/* 75 */     return this.ras;
/*    */   }
/*    */ 
/*    */   public BufferedImage getBufferedImage()
/*    */   {
/* 82 */     return this.bi;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.image.codec.jpeg.TruncatedFileException
 * JD-Core Version:    0.6.2
 */
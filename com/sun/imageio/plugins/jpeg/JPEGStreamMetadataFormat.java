/*    */ package com.sun.imageio.plugins.jpeg;
/*    */ 
/*    */ import javax.imageio.metadata.IIOMetadataFormat;
/*    */ 
/*    */ public class JPEGStreamMetadataFormat extends JPEGMetadataFormat
/*    */ {
/* 33 */   private static JPEGStreamMetadataFormat theInstance = null;
/*    */ 
/*    */   private JPEGStreamMetadataFormat() {
/* 36 */     super("javax_imageio_jpeg_stream_1.0", 4);
/*    */ 
/* 38 */     addStreamElements(getRootName());
/*    */   }
/*    */ 
/*    */   public static synchronized IIOMetadataFormat getInstance() {
/* 42 */     if (theInstance == null) {
/* 43 */       theInstance = new JPEGStreamMetadataFormat();
/*    */     }
/* 45 */     return theInstance;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGStreamMetadataFormat
 * JD-Core Version:    0.6.2
 */
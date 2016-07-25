/*    */ package com.sun.imageio.plugins.jpeg;
/*    */ 
/*    */ public class JPEGStreamMetadataFormatResources extends JPEGMetadataFormatResources
/*    */ {
/*    */   protected Object[][] getContents()
/*    */   {
/* 39 */     Object[][] arrayOfObject = new Object[commonContents.length][2];
/* 40 */     for (int i = 0; i < commonContents.length; i++) {
/* 41 */       arrayOfObject[i][0] = commonContents[i][0];
/* 42 */       arrayOfObject[i][1] = commonContents[i][1];
/*    */     }
/* 44 */     return arrayOfObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGStreamMetadataFormatResources
 * JD-Core Version:    0.6.2
 */
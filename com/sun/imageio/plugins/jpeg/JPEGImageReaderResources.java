/*    */ package com.sun.imageio.plugins.jpeg;
/*    */ 
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ public class JPEGImageReaderResources extends ListResourceBundle
/*    */ {
/*    */   protected Object[][] getContents()
/*    */   {
/* 35 */     return new Object[][] { { Integer.toString(0), "Truncated File - Missing EOI marker" }, { Integer.toString(1), "JFIF markers not allowed in JFIF JPEG thumbnail; ignored" }, { Integer.toString(2), "Embedded color profile is invalid; ignored" } };
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGImageReaderResources
 * JD-Core Version:    0.6.2
 */
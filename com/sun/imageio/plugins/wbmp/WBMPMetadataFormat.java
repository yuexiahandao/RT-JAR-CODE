/*    */ package com.sun.imageio.plugins.wbmp;
/*    */ 
/*    */ import javax.imageio.ImageTypeSpecifier;
/*    */ import javax.imageio.metadata.IIOMetadataFormat;
/*    */ import javax.imageio.metadata.IIOMetadataFormatImpl;
/*    */ 
/*    */ public class WBMPMetadataFormat extends IIOMetadataFormatImpl
/*    */ {
/* 35 */   private static IIOMetadataFormat instance = null;
/*    */ 
/*    */   private WBMPMetadataFormat() {
/* 38 */     super("javax_imageio_wbmp_1.0", 2);
/*    */ 
/* 42 */     addElement("ImageDescriptor", "javax_imageio_wbmp_1.0", 0);
/*    */ 
/* 46 */     addAttribute("ImageDescriptor", "WBMPType", 2, true, "0");
/*    */ 
/* 49 */     addAttribute("ImageDescriptor", "Width", 2, true, null, "0", "65535", true, true);
/*    */ 
/* 52 */     addAttribute("ImageDescriptor", "Height", 2, true, null, "1", "65535", true, true);
/*    */   }
/*    */ 
/*    */   public boolean canNodeAppear(String paramString, ImageTypeSpecifier paramImageTypeSpecifier)
/*    */   {
/* 61 */     return true;
/*    */   }
/*    */ 
/*    */   public static synchronized IIOMetadataFormat getInstance() {
/* 65 */     if (instance == null) {
/* 66 */       instance = new WBMPMetadataFormat();
/*    */     }
/* 68 */     return instance;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.wbmp.WBMPMetadataFormat
 * JD-Core Version:    0.6.2
 */
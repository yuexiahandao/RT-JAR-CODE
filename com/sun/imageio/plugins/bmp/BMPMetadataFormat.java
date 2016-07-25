/*     */ package com.sun.imageio.plugins.bmp;
/*     */ 
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.metadata.IIOMetadataFormat;
/*     */ import javax.imageio.metadata.IIOMetadataFormatImpl;
/*     */ 
/*     */ public class BMPMetadataFormat extends IIOMetadataFormatImpl
/*     */ {
/*  35 */   private static IIOMetadataFormat instance = null;
/*     */ 
/*     */   private BMPMetadataFormat() {
/*  38 */     super("javax_imageio_bmp_1.0", 2);
/*     */ 
/*  42 */     addElement("ImageDescriptor", "javax_imageio_bmp_1.0", 0);
/*     */ 
/*  45 */     addAttribute("ImageDescriptor", "bmpVersion", 0, true, null);
/*     */ 
/*  47 */     addAttribute("ImageDescriptor", "width", 2, true, null, "0", "65535", true, true);
/*     */ 
/*  50 */     addAttribute("ImageDescriptor", "height", 2, true, null, "1", "65535", true, true);
/*     */ 
/*  53 */     addAttribute("ImageDescriptor", "bitsPerPixel", 2, true, null, "1", "65535", true, true);
/*     */ 
/*  56 */     addAttribute("ImageDescriptor", "compression", 2, false, null);
/*     */ 
/*  58 */     addAttribute("ImageDescriptor", "imageSize", 2, true, null, "1", "65535", true, true);
/*     */ 
/*  62 */     addElement("PixelsPerMeter", "javax_imageio_bmp_1.0", 0);
/*     */ 
/*  65 */     addAttribute("PixelsPerMeter", "X", 2, false, null, "1", "65535", true, true);
/*     */ 
/*  68 */     addAttribute("PixelsPerMeter", "Y", 2, false, null, "1", "65535", true, true);
/*     */ 
/*  72 */     addElement("ColorsUsed", "javax_imageio_bmp_1.0", 0);
/*     */ 
/*  75 */     addAttribute("ColorsUsed", "value", 2, true, null, "0", "65535", true, true);
/*     */ 
/*  79 */     addElement("ColorsImportant", "javax_imageio_bmp_1.0", 0);
/*     */ 
/*  82 */     addAttribute("ColorsImportant", "value", 2, false, null, "0", "65535", true, true);
/*     */ 
/*  86 */     addElement("BI_BITFIELDS_Mask", "javax_imageio_bmp_1.0", 0);
/*     */ 
/*  89 */     addAttribute("BI_BITFIELDS_Mask", "red", 2, false, null, "0", "65535", true, true);
/*     */ 
/*  92 */     addAttribute("BI_BITFIELDS_Mask", "green", 2, false, null, "0", "65535", true, true);
/*     */ 
/*  95 */     addAttribute("BI_BITFIELDS_Mask", "blue", 2, false, null, "0", "65535", true, true);
/*     */ 
/*  99 */     addElement("ColorSpace", "javax_imageio_bmp_1.0", 0);
/*     */ 
/* 102 */     addAttribute("ColorSpace", "value", 2, false, null, "0", "65535", true, true);
/*     */ 
/* 106 */     addElement("LCS_CALIBRATED_RGB", "javax_imageio_bmp_1.0", 0);
/*     */ 
/* 111 */     addAttribute("LCS_CALIBRATED_RGB", "redX", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 114 */     addAttribute("LCS_CALIBRATED_RGB", "redY", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 117 */     addAttribute("LCS_CALIBRATED_RGB", "redZ", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 120 */     addAttribute("LCS_CALIBRATED_RGB", "greenX", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 123 */     addAttribute("LCS_CALIBRATED_RGB", "greenY", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 126 */     addAttribute("LCS_CALIBRATED_RGB", "greenZ", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 129 */     addAttribute("LCS_CALIBRATED_RGB", "blueX", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 132 */     addAttribute("LCS_CALIBRATED_RGB", "blueY", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 135 */     addAttribute("LCS_CALIBRATED_RGB", "blueZ", 4, false, null, "0", "65535", true, true);
/*     */ 
/* 139 */     addElement("LCS_CALIBRATED_RGB_GAMMA", "javax_imageio_bmp_1.0", 0);
/*     */ 
/* 142 */     addAttribute("LCS_CALIBRATED_RGB_GAMMA", "red", 2, false, null, "0", "65535", true, true);
/*     */ 
/* 145 */     addAttribute("LCS_CALIBRATED_RGB_GAMMA", "green", 2, false, null, "0", "65535", true, true);
/*     */ 
/* 148 */     addAttribute("LCS_CALIBRATED_RGB_GAMMA", "blue", 2, false, null, "0", "65535", true, true);
/*     */ 
/* 152 */     addElement("Intent", "javax_imageio_bmp_1.0", 0);
/*     */ 
/* 155 */     addAttribute("Intent", "value", 2, false, null, "0", "65535", true, true);
/*     */ 
/* 160 */     addElement("Palette", "javax_imageio_bmp_1.0", 2, 256);
/*     */ 
/* 163 */     addAttribute("Palette", "sizeOfPalette", 2, true, null);
/*     */ 
/* 165 */     addBooleanAttribute("Palette", "sortFlag", false, false);
/*     */ 
/* 169 */     addElement("PaletteEntry", "Palette", 0);
/*     */ 
/* 171 */     addAttribute("PaletteEntry", "index", 2, true, null, "0", "255", true, true);
/*     */ 
/* 174 */     addAttribute("PaletteEntry", "red", 2, true, null, "0", "255", true, true);
/*     */ 
/* 177 */     addAttribute("PaletteEntry", "green", 2, true, null, "0", "255", true, true);
/*     */ 
/* 180 */     addAttribute("PaletteEntry", "blue", 2, true, null, "0", "255", true, true);
/*     */ 
/* 186 */     addElement("CommentExtensions", "javax_imageio_bmp_1.0", 1, 2147483647);
/*     */ 
/* 191 */     addElement("CommentExtension", "CommentExtensions", 0);
/*     */ 
/* 193 */     addAttribute("CommentExtension", "value", 0, true, null);
/*     */   }
/*     */ 
/*     */   public boolean canNodeAppear(String paramString, ImageTypeSpecifier paramImageTypeSpecifier)
/*     */   {
/* 199 */     return true;
/*     */   }
/*     */ 
/*     */   public static synchronized IIOMetadataFormat getInstance() {
/* 203 */     if (instance == null) {
/* 204 */       instance = new BMPMetadataFormat();
/*     */     }
/* 206 */     return instance;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.bmp.BMPMetadataFormat
 * JD-Core Version:    0.6.2
 */
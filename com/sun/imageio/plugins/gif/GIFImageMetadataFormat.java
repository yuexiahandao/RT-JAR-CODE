/*     */ package com.sun.imageio.plugins.gif;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.metadata.IIOMetadataFormat;
/*     */ import javax.imageio.metadata.IIOMetadataFormatImpl;
/*     */ 
/*     */ public class GIFImageMetadataFormat extends IIOMetadataFormatImpl
/*     */ {
/*  35 */   private static IIOMetadataFormat instance = null;
/*     */ 
/*     */   private GIFImageMetadataFormat() {
/*  38 */     super("javax_imageio_gif_image_1.0", 2);
/*     */ 
/*  42 */     addElement("ImageDescriptor", "javax_imageio_gif_image_1.0", 0);
/*     */ 
/*  45 */     addAttribute("ImageDescriptor", "imageLeftPosition", 2, true, null, "0", "65535", true, true);
/*     */ 
/*  48 */     addAttribute("ImageDescriptor", "imageTopPosition", 2, true, null, "0", "65535", true, true);
/*     */ 
/*  51 */     addAttribute("ImageDescriptor", "imageWidth", 2, true, null, "1", "65535", true, true);
/*     */ 
/*  54 */     addAttribute("ImageDescriptor", "imageHeight", 2, true, null, "1", "65535", true, true);
/*     */ 
/*  57 */     addBooleanAttribute("ImageDescriptor", "interlaceFlag", false, false);
/*     */ 
/*  61 */     addElement("LocalColorTable", "javax_imageio_gif_image_1.0", 2, 256);
/*     */ 
/*  64 */     addAttribute("LocalColorTable", "sizeOfLocalColorTable", 2, true, null, Arrays.asList(GIFStreamMetadata.colorTableSizes));
/*     */ 
/*  67 */     addBooleanAttribute("LocalColorTable", "sortFlag", false, false);
/*     */ 
/*  71 */     addElement("ColorTableEntry", "LocalColorTable", 0);
/*     */ 
/*  73 */     addAttribute("ColorTableEntry", "index", 2, true, null, "0", "255", true, true);
/*     */ 
/*  76 */     addAttribute("ColorTableEntry", "red", 2, true, null, "0", "255", true, true);
/*     */ 
/*  79 */     addAttribute("ColorTableEntry", "green", 2, true, null, "0", "255", true, true);
/*     */ 
/*  82 */     addAttribute("ColorTableEntry", "blue", 2, true, null, "0", "255", true, true);
/*     */ 
/*  87 */     addElement("GraphicControlExtension", "javax_imageio_gif_image_1.0", 0);
/*     */ 
/*  90 */     addAttribute("GraphicControlExtension", "disposalMethod", 0, true, null, Arrays.asList(GIFImageMetadata.disposalMethodNames));
/*     */ 
/*  93 */     addBooleanAttribute("GraphicControlExtension", "userInputFlag", false, false);
/*     */ 
/*  95 */     addBooleanAttribute("GraphicControlExtension", "transparentColorFlag", false, false);
/*     */ 
/*  97 */     addAttribute("GraphicControlExtension", "delayTime", 2, true, null, "0", "65535", true, true);
/*     */ 
/* 100 */     addAttribute("GraphicControlExtension", "transparentColorIndex", 2, true, null, "0", "255", true, true);
/*     */ 
/* 105 */     addElement("PlainTextExtension", "javax_imageio_gif_image_1.0", 0);
/*     */ 
/* 108 */     addAttribute("PlainTextExtension", "textGridLeft", 2, true, null, "0", "65535", true, true);
/*     */ 
/* 111 */     addAttribute("PlainTextExtension", "textGridTop", 2, true, null, "0", "65535", true, true);
/*     */ 
/* 114 */     addAttribute("PlainTextExtension", "textGridWidth", 2, true, null, "1", "65535", true, true);
/*     */ 
/* 117 */     addAttribute("PlainTextExtension", "textGridHeight", 2, true, null, "1", "65535", true, true);
/*     */ 
/* 120 */     addAttribute("PlainTextExtension", "characterCellWidth", 2, true, null, "1", "65535", true, true);
/*     */ 
/* 123 */     addAttribute("PlainTextExtension", "characterCellHeight", 2, true, null, "1", "65535", true, true);
/*     */ 
/* 126 */     addAttribute("PlainTextExtension", "textForegroundColor", 2, true, null, "0", "255", true, true);
/*     */ 
/* 129 */     addAttribute("PlainTextExtension", "textBackgroundColor", 2, true, null, "0", "255", true, true);
/*     */ 
/* 134 */     addElement("ApplicationExtensions", "javax_imageio_gif_image_1.0", 1, 2147483647);
/*     */ 
/* 139 */     addElement("ApplicationExtension", "ApplicationExtensions", 0);
/*     */ 
/* 141 */     addAttribute("ApplicationExtension", "applicationID", 0, true, null);
/*     */ 
/* 143 */     addAttribute("ApplicationExtension", "authenticationCode", 0, true, null);
/*     */ 
/* 145 */     addObjectValue("ApplicationExtension", Byte.TYPE, 0, 2147483647);
/*     */ 
/* 149 */     addElement("CommentExtensions", "javax_imageio_gif_image_1.0", 1, 2147483647);
/*     */ 
/* 154 */     addElement("CommentExtension", "CommentExtensions", 0);
/*     */ 
/* 156 */     addAttribute("CommentExtension", "value", 0, true, null);
/*     */   }
/*     */ 
/*     */   public boolean canNodeAppear(String paramString, ImageTypeSpecifier paramImageTypeSpecifier)
/*     */   {
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */   public static synchronized IIOMetadataFormat getInstance() {
/* 166 */     if (instance == null) {
/* 167 */       instance = new GIFImageMetadataFormat();
/*     */     }
/* 169 */     return instance;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFImageMetadataFormat
 * JD-Core Version:    0.6.2
 */
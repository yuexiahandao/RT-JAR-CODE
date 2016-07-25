/*     */ package com.sun.imageio.plugins.png;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.metadata.IIOMetadataFormat;
/*     */ import javax.imageio.metadata.IIOMetadataFormatImpl;
/*     */ 
/*     */ public class PNGMetadataFormat extends IIOMetadataFormatImpl
/*     */ {
/*  38 */   private static IIOMetadataFormat instance = null;
/*     */ 
/*  40 */   private static String VALUE_0 = "0";
/*  41 */   private static String VALUE_1 = "1";
/*  42 */   private static String VALUE_12 = "12";
/*  43 */   private static String VALUE_23 = "23";
/*  44 */   private static String VALUE_31 = "31";
/*  45 */   private static String VALUE_59 = "59";
/*  46 */   private static String VALUE_60 = "60";
/*  47 */   private static String VALUE_255 = "255";
/*  48 */   private static String VALUE_MAX_16 = "65535";
/*  49 */   private static String VALUE_MAX_32 = "2147483647";
/*     */ 
/*     */   private PNGMetadataFormat() {
/*  52 */     super("javax_imageio_png_1.0", 2);
/*     */ 
/*  56 */     addElement("IHDR", "javax_imageio_png_1.0", 0);
/*     */ 
/*  59 */     addAttribute("IHDR", "width", 2, true, null, VALUE_1, VALUE_MAX_32, true, true);
/*     */ 
/*  63 */     addAttribute("IHDR", "height", 2, true, null, VALUE_1, VALUE_MAX_32, true, true);
/*     */ 
/*  67 */     addAttribute("IHDR", "bitDepth", 2, true, null, Arrays.asList(PNGMetadata.IHDR_bitDepths));
/*     */ 
/*  71 */     String[] arrayOfString = { "Grayscale", "RGB", "Palette", "GrayAlpha", "RGBAlpha" };
/*     */ 
/*  74 */     addAttribute("IHDR", "colorType", 0, true, null, Arrays.asList(arrayOfString));
/*     */ 
/*  78 */     addAttribute("IHDR", "compressionMethod", 0, true, null, Arrays.asList(PNGMetadata.IHDR_compressionMethodNames));
/*     */ 
/*  82 */     addAttribute("IHDR", "filterMethod", 0, true, null, Arrays.asList(PNGMetadata.IHDR_filterMethodNames));
/*     */ 
/*  86 */     addAttribute("IHDR", "interlaceMethod", 0, true, null, Arrays.asList(PNGMetadata.IHDR_interlaceMethodNames));
/*     */ 
/*  91 */     addElement("PLTE", "javax_imageio_png_1.0", 1, 256);
/*     */ 
/*  95 */     addElement("PLTEEntry", "PLTE", 0);
/*     */ 
/*  98 */     addAttribute("PLTEEntry", "index", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 102 */     addAttribute("PLTEEntry", "red", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 106 */     addAttribute("PLTEEntry", "green", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 110 */     addAttribute("PLTEEntry", "blue", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 115 */     addElement("bKGD", "javax_imageio_png_1.0", 3);
/*     */ 
/* 119 */     addElement("bKGD_Grayscale", "bKGD", 0);
/*     */ 
/* 122 */     addAttribute("bKGD_Grayscale", "gray", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 127 */     addElement("bKGD_RGB", "bKGD", 0);
/*     */ 
/* 130 */     addAttribute("bKGD_RGB", "red", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 134 */     addAttribute("bKGD_RGB", "green", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 138 */     addAttribute("bKGD_RGB", "blue", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 143 */     addElement("bKGD_Palette", "bKGD", 0);
/*     */ 
/* 146 */     addAttribute("bKGD_Palette", "index", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 151 */     addElement("cHRM", "javax_imageio_png_1.0", 0);
/*     */ 
/* 154 */     addAttribute("cHRM", "whitePointX", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 158 */     addAttribute("cHRM", "whitePointY", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 162 */     addAttribute("cHRM", "redX", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 166 */     addAttribute("cHRM", "redY", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 170 */     addAttribute("cHRM", "greenX", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 174 */     addAttribute("cHRM", "greenY", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 178 */     addAttribute("cHRM", "blueX", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 182 */     addAttribute("cHRM", "blueY", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 187 */     addElement("gAMA", "javax_imageio_png_1.0", 0);
/*     */ 
/* 190 */     addAttribute("gAMA", "value", 2, true, null, VALUE_0, VALUE_MAX_32, true, true);
/*     */ 
/* 195 */     addElement("hIST", "javax_imageio_png_1.0", 1, 256);
/*     */ 
/* 199 */     addElement("hISTEntry", "hIST", 0);
/*     */ 
/* 202 */     addAttribute("hISTEntry", "index", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 206 */     addAttribute("hISTEntry", "value", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 211 */     addElement("iCCP", "javax_imageio_png_1.0", 0);
/*     */ 
/* 214 */     addAttribute("iCCP", "profileName", 0, true, null);
/*     */ 
/* 217 */     addAttribute("iCCP", "compressionMethod", 0, true, null, Arrays.asList(PNGMetadata.iCCP_compressionMethodNames));
/*     */ 
/* 221 */     addObjectValue("iCCP", Byte.TYPE, 0, 2147483647);
/*     */ 
/* 224 */     addElement("iTXt", "javax_imageio_png_1.0", 1, 2147483647);
/*     */ 
/* 228 */     addElement("iTXtEntry", "iTXt", 0);
/*     */ 
/* 231 */     addAttribute("iTXtEntry", "keyword", 0, true, null);
/*     */ 
/* 234 */     addBooleanAttribute("iTXtEntry", "compressionFlag", false, false);
/*     */ 
/* 237 */     addAttribute("iTXtEntry", "compressionMethod", 0, true, null);
/*     */ 
/* 240 */     addAttribute("iTXtEntry", "languageTag", 0, true, null);
/*     */ 
/* 243 */     addAttribute("iTXtEntry", "translatedKeyword", 0, true, null);
/*     */ 
/* 246 */     addAttribute("iTXtEntry", "text", 0, true, null);
/*     */ 
/* 250 */     addElement("pHYS", "javax_imageio_png_1.0", 0);
/*     */ 
/* 253 */     addAttribute("pHYS", "pixelsPerUnitXAxis", 2, true, null, VALUE_0, VALUE_MAX_32, true, true);
/*     */ 
/* 256 */     addAttribute("pHYS", "pixelsPerUnitYAxis", 2, true, null, VALUE_0, VALUE_MAX_32, true, true);
/*     */ 
/* 259 */     addAttribute("pHYS", "unitSpecifier", 0, true, null, Arrays.asList(PNGMetadata.unitSpecifierNames));
/*     */ 
/* 264 */     addElement("sBIT", "javax_imageio_png_1.0", 3);
/*     */ 
/* 268 */     addElement("sBIT_Grayscale", "sBIT", 0);
/*     */ 
/* 271 */     addAttribute("sBIT_Grayscale", "gray", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 276 */     addElement("sBIT_GrayAlpha", "sBIT", 0);
/*     */ 
/* 279 */     addAttribute("sBIT_GrayAlpha", "gray", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 283 */     addAttribute("sBIT_GrayAlpha", "alpha", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 288 */     addElement("sBIT_RGB", "sBIT", 0);
/*     */ 
/* 291 */     addAttribute("sBIT_RGB", "red", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 295 */     addAttribute("sBIT_RGB", "green", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 299 */     addAttribute("sBIT_RGB", "blue", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 304 */     addElement("sBIT_RGBAlpha", "sBIT", 0);
/*     */ 
/* 307 */     addAttribute("sBIT_RGBAlpha", "red", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 311 */     addAttribute("sBIT_RGBAlpha", "green", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 315 */     addAttribute("sBIT_RGBAlpha", "blue", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 319 */     addAttribute("sBIT_RGBAlpha", "alpha", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 324 */     addElement("sBIT_Palette", "sBIT", 0);
/*     */ 
/* 327 */     addAttribute("sBIT_Palette", "red", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 331 */     addAttribute("sBIT_Palette", "green", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 335 */     addAttribute("sBIT_Palette", "blue", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 340 */     addElement("sPLT", "javax_imageio_png_1.0", 1, 256);
/*     */ 
/* 344 */     addElement("sPLTEntry", "sPLT", 0);
/*     */ 
/* 347 */     addAttribute("sPLTEntry", "index", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 351 */     addAttribute("sPLTEntry", "red", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 355 */     addAttribute("sPLTEntry", "green", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 359 */     addAttribute("sPLTEntry", "blue", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 363 */     addAttribute("sPLTEntry", "alpha", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 368 */     addElement("sRGB", "javax_imageio_png_1.0", 0);
/*     */ 
/* 371 */     addAttribute("sRGB", "renderingIntent", 0, true, null, Arrays.asList(PNGMetadata.renderingIntentNames));
/*     */ 
/* 376 */     addElement("tEXt", "javax_imageio_png_1.0", 1, 2147483647);
/*     */ 
/* 380 */     addElement("tEXtEntry", "tEXt", 0);
/*     */ 
/* 383 */     addAttribute("tEXtEntry", "keyword", 0, true, null);
/*     */ 
/* 386 */     addAttribute("tEXtEntry", "value", 0, true, null);
/*     */ 
/* 390 */     addElement("tIME", "javax_imageio_png_1.0", 0);
/*     */ 
/* 393 */     addAttribute("tIME", "year", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 397 */     addAttribute("tIME", "month", 2, true, null, VALUE_1, VALUE_12, true, true);
/*     */ 
/* 401 */     addAttribute("tIME", "day", 2, true, null, VALUE_1, VALUE_31, true, true);
/*     */ 
/* 405 */     addAttribute("tIME", "hour", 2, true, null, VALUE_0, VALUE_23, true, true);
/*     */ 
/* 409 */     addAttribute("tIME", "minute", 2, true, null, VALUE_0, VALUE_59, true, true);
/*     */ 
/* 413 */     addAttribute("tIME", "second", 2, true, null, VALUE_0, VALUE_60, true, true);
/*     */ 
/* 418 */     addElement("tRNS", "javax_imageio_png_1.0", 3);
/*     */ 
/* 422 */     addElement("tRNS_Grayscale", "tRNS", 0);
/*     */ 
/* 425 */     addAttribute("tRNS_Grayscale", "gray", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 430 */     addElement("tRNS_RGB", "tRNS", 0);
/*     */ 
/* 433 */     addAttribute("tRNS_RGB", "red", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 437 */     addAttribute("tRNS_RGB", "green", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 441 */     addAttribute("tRNS_RGB", "blue", 2, true, null, VALUE_0, VALUE_MAX_16, true, true);
/*     */ 
/* 446 */     addElement("tRNS_Palette", "tRNS", 0);
/*     */ 
/* 449 */     addAttribute("tRNS_Palette", "index", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 453 */     addAttribute("tRNS_Palette", "alpha", 2, true, null, VALUE_0, VALUE_255, true, true);
/*     */ 
/* 458 */     addElement("zTXt", "javax_imageio_png_1.0", 1, 2147483647);
/*     */ 
/* 462 */     addElement("zTXtEntry", "zTXt", 0);
/*     */ 
/* 465 */     addAttribute("zTXtEntry", "keyword", 0, true, null);
/*     */ 
/* 468 */     addAttribute("zTXtEntry", "compressionMethod", 0, true, null, Arrays.asList(PNGMetadata.zTXt_compressionMethodNames));
/*     */ 
/* 472 */     addAttribute("zTXtEntry", "text", 0, true, null);
/*     */ 
/* 476 */     addElement("UnknownChunks", "javax_imageio_png_1.0", 1, 2147483647);
/*     */ 
/* 480 */     addElement("UnknownChunk", "UnknownChunks", 0);
/*     */ 
/* 483 */     addAttribute("UnknownChunk", "type", 0, true, null);
/*     */ 
/* 486 */     addObjectValue("UnknownChunk", Byte.TYPE, 0, 2147483647);
/*     */   }
/*     */ 
/*     */   public boolean canNodeAppear(String paramString, ImageTypeSpecifier paramImageTypeSpecifier)
/*     */   {
/* 491 */     return true;
/*     */   }
/*     */ 
/*     */   public static synchronized IIOMetadataFormat getInstance() {
/* 495 */     if (instance == null) {
/* 496 */       instance = new PNGMetadataFormat();
/*     */     }
/* 498 */     return instance;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.PNGMetadataFormat
 * JD-Core Version:    0.6.2
 */
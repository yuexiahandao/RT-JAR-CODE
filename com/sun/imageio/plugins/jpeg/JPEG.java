/*     */ package com.sun.imageio.plugins.jpeg;
/*     */ 
/*     */ import java.awt.color.ColorSpace;
/*     */ import java.awt.color.ICC_ColorSpace;
/*     */ import java.awt.image.ColorModel;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
/*     */ import javax.imageio.plugins.jpeg.JPEGQTable;
/*     */ 
/*     */ public class JPEG
/*     */ {
/*     */   public static final int TEM = 1;
/*     */   public static final int SOF0 = 192;
/*     */   public static final int SOF1 = 193;
/*     */   public static final int SOF2 = 194;
/*     */   public static final int SOF3 = 195;
/*     */   public static final int DHT = 196;
/*     */   public static final int SOF5 = 197;
/*     */   public static final int SOF6 = 198;
/*     */   public static final int SOF7 = 199;
/*     */   public static final int JPG = 200;
/*     */   public static final int SOF9 = 201;
/*     */   public static final int SOF10 = 202;
/*     */   public static final int SOF11 = 203;
/*     */   public static final int DAC = 204;
/*     */   public static final int SOF13 = 205;
/*     */   public static final int SOF14 = 206;
/*     */   public static final int SOF15 = 207;
/*     */   public static final int RST0 = 208;
/*     */   public static final int RST1 = 209;
/*     */   public static final int RST2 = 210;
/*     */   public static final int RST3 = 211;
/*     */   public static final int RST4 = 212;
/*     */   public static final int RST5 = 213;
/*     */   public static final int RST6 = 214;
/*     */   public static final int RST7 = 215;
/*     */   public static final int RESTART_RANGE = 8;
/*     */   public static final int SOI = 216;
/*     */   public static final int EOI = 217;
/*     */   public static final int SOS = 218;
/*     */   public static final int DQT = 219;
/*     */   public static final int DNL = 220;
/*     */   public static final int DRI = 221;
/*     */   public static final int DHP = 222;
/*     */   public static final int EXP = 223;
/*     */   public static final int APP0 = 224;
/*     */   public static final int APP1 = 225;
/*     */   public static final int APP2 = 226;
/*     */   public static final int APP3 = 227;
/*     */   public static final int APP4 = 228;
/*     */   public static final int APP5 = 229;
/*     */   public static final int APP6 = 230;
/*     */   public static final int APP7 = 231;
/*     */   public static final int APP8 = 232;
/*     */   public static final int APP9 = 233;
/*     */   public static final int APP10 = 234;
/*     */   public static final int APP11 = 235;
/*     */   public static final int APP12 = 236;
/*     */   public static final int APP13 = 237;
/*     */   public static final int APP14 = 238;
/*     */   public static final int APP15 = 239;
/*     */   public static final int COM = 254;
/*     */   public static final int DENSITY_UNIT_ASPECT_RATIO = 0;
/*     */   public static final int DENSITY_UNIT_DOTS_INCH = 1;
/*     */   public static final int DENSITY_UNIT_DOTS_CM = 2;
/*     */   public static final int NUM_DENSITY_UNIT = 3;
/*     */   public static final int ADOBE_IMPOSSIBLE = -1;
/*     */   public static final int ADOBE_UNKNOWN = 0;
/*     */   public static final int ADOBE_YCC = 1;
/*     */   public static final int ADOBE_YCCK = 2;
/*     */   public static final String vendor = "Oracle Corporation";
/*     */   public static final String version = "0.5";
/* 175 */   public static final String[] names = { "JPEG", "jpeg", "JPG", "jpg" };
/* 176 */   public static final String[] suffixes = { "jpg", "jpeg" };
/* 177 */   public static final String[] MIMETypes = { "image/jpeg" };
/*     */   public static final String nativeImageMetadataFormatName = "javax_imageio_jpeg_image_1.0";
/*     */   public static final String nativeImageMetadataFormatClassName = "com.sun.imageio.plugins.jpeg.JPEGImageMetadataFormat";
/*     */   public static final String nativeStreamMetadataFormatName = "javax_imageio_jpeg_stream_1.0";
/*     */   public static final String nativeStreamMetadataFormatClassName = "com.sun.imageio.plugins.jpeg.JPEGStreamMetadataFormat";
/*     */   public static final int JCS_UNKNOWN = 0;
/*     */   public static final int JCS_GRAYSCALE = 1;
/*     */   public static final int JCS_RGB = 2;
/*     */   public static final int JCS_YCbCr = 3;
/*     */   public static final int JCS_CMYK = 4;
/*     */   public static final int JCS_YCC = 5;
/*     */   public static final int JCS_RGBA = 6;
/*     */   public static final int JCS_YCbCrA = 7;
/*     */   public static final int JCS_YCCA = 10;
/*     */   public static final int JCS_YCCK = 11;
/*     */   public static final int NUM_JCS_CODES = 12;
/* 204 */   public static final int[][] bandOffsets = { { 0 }, { 0, 1 }, { 0, 1, 2 }, { 0, 1, 2, 3 } };
/*     */ 
/* 209 */   public static final int[] bOffsRGB = { 2, 1, 0 };
/*     */   public static final float DEFAULT_QUALITY = 0.75F;
/*     */ 
/*     */   static boolean isNonStandardICC(ColorSpace paramColorSpace)
/*     */   {
/* 246 */     boolean bool = false;
/* 247 */     if (((paramColorSpace instanceof ICC_ColorSpace)) && (!paramColorSpace.isCS_sRGB()) && (!paramColorSpace.equals(ColorSpace.getInstance(1001))) && (!paramColorSpace.equals(ColorSpace.getInstance(1003))) && (!paramColorSpace.equals(ColorSpace.getInstance(1004))) && (!paramColorSpace.equals(ColorSpace.getInstance(1002))))
/*     */     {
/* 254 */       bool = true;
/*     */     }
/* 256 */     return bool;
/*     */   }
/*     */ 
/*     */   static boolean isJFIFcompliant(ImageTypeSpecifier paramImageTypeSpecifier, boolean paramBoolean)
/*     */   {
/* 267 */     ColorModel localColorModel = paramImageTypeSpecifier.getColorModel();
/*     */ 
/* 269 */     if (localColorModel.hasAlpha()) {
/* 270 */       return false;
/*     */     }
/*     */ 
/* 273 */     int i = paramImageTypeSpecifier.getNumComponents();
/* 274 */     if (i == 1) {
/* 275 */       return true;
/*     */     }
/*     */ 
/* 279 */     if (i != 3) {
/* 280 */       return false;
/*     */     }
/*     */ 
/* 283 */     if (paramBoolean)
/*     */     {
/* 285 */       if (localColorModel.getColorSpace().getType() == 5) {
/* 286 */         return true;
/*     */       }
/*     */ 
/*     */     }
/* 290 */     else if (localColorModel.getColorSpace().getType() == 3) {
/* 291 */       return true;
/*     */     }
/*     */ 
/* 295 */     return false;
/*     */   }
/*     */ 
/*     */   static int transformForType(ImageTypeSpecifier paramImageTypeSpecifier, boolean paramBoolean)
/*     */   {
/* 305 */     int i = -1;
/* 306 */     ColorModel localColorModel = paramImageTypeSpecifier.getColorModel();
/* 307 */     switch (localColorModel.getColorSpace().getType()) {
/*     */     case 6:
/* 309 */       i = 0;
/* 310 */       break;
/*     */     case 5:
/* 312 */       i = paramBoolean ? 1 : 0;
/* 313 */       break;
/*     */     case 3:
/* 315 */       i = 1;
/* 316 */       break;
/*     */     case 9:
/* 318 */       i = paramBoolean ? 2 : -1;
/*     */     case 4:
/*     */     case 7:
/* 320 */     case 8: } return i;
/*     */   }
/*     */ 
/*     */   static float convertToLinearQuality(float paramFloat)
/*     */   {
/* 329 */     if (paramFloat <= 0.0F) {
/* 330 */       paramFloat = 0.01F;
/*     */     }
/*     */ 
/* 333 */     if (paramFloat > 1.0F) {
/* 334 */       paramFloat = 1.0F;
/*     */     }
/*     */ 
/* 337 */     if (paramFloat < 0.5F)
/* 338 */       paramFloat = 0.5F / paramFloat;
/*     */     else {
/* 340 */       paramFloat = 2.0F - paramFloat * 2.0F;
/*     */     }
/*     */ 
/* 343 */     return paramFloat;
/*     */   }
/*     */ 
/*     */   static JPEGQTable[] getDefaultQTables()
/*     */   {
/* 350 */     JPEGQTable[] arrayOfJPEGQTable = new JPEGQTable[2];
/* 351 */     arrayOfJPEGQTable[0] = JPEGQTable.K1Div2Luminance;
/* 352 */     arrayOfJPEGQTable[1] = JPEGQTable.K2Div2Chrominance;
/* 353 */     return arrayOfJPEGQTable;
/*     */   }
/*     */ 
/*     */   static JPEGHuffmanTable[] getDefaultHuffmanTables(boolean paramBoolean)
/*     */   {
/* 360 */     JPEGHuffmanTable[] arrayOfJPEGHuffmanTable = new JPEGHuffmanTable[2];
/* 361 */     if (paramBoolean) {
/* 362 */       arrayOfJPEGHuffmanTable[0] = JPEGHuffmanTable.StdDCLuminance;
/* 363 */       arrayOfJPEGHuffmanTable[1] = JPEGHuffmanTable.StdDCChrominance;
/*     */     } else {
/* 365 */       arrayOfJPEGHuffmanTable[0] = JPEGHuffmanTable.StdACLuminance;
/* 366 */       arrayOfJPEGHuffmanTable[1] = JPEGHuffmanTable.StdACChrominance;
/*     */     }
/* 368 */     return arrayOfJPEGHuffmanTable;
/*     */   }
/*     */ 
/*     */   public static class JCS
/*     */   {
/* 216 */     public static final ColorSpace sRGB = ColorSpace.getInstance(1000);
/*     */ 
/* 219 */     private static ColorSpace YCC = null;
/* 220 */     private static boolean yccInited = false;
/*     */ 
/*     */     public static ColorSpace getYCC() {
/* 223 */       if (!yccInited) {
/*     */         try {
/* 225 */           YCC = ColorSpace.getInstance(1002);
/*     */         } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */         }
/*     */         finally {
/* 229 */           yccInited = true;
/*     */         }
/*     */       }
/* 232 */       return YCC;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEG
 * JD-Core Version:    0.6.2
 */
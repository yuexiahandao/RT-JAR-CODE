/*    */ package com.sun.imageio.plugins.bmp;
/*    */ 
/*    */ public abstract interface BMPConstants
/*    */ {
/*    */   public static final String VERSION_2 = "BMP v. 2.x";
/*    */   public static final String VERSION_3 = "BMP v. 3.x";
/*    */   public static final String VERSION_3_NT = "BMP v. 3.x NT";
/*    */   public static final String VERSION_4 = "BMP v. 4.x";
/*    */   public static final String VERSION_5 = "BMP v. 5.x";
/*    */   public static final int LCS_CALIBRATED_RGB = 0;
/*    */   public static final int LCS_sRGB = 1;
/*    */   public static final int LCS_WINDOWS_COLOR_SPACE = 2;
/*    */   public static final int PROFILE_LINKED = 3;
/*    */   public static final int PROFILE_EMBEDDED = 4;
/*    */   public static final int BI_RGB = 0;
/*    */   public static final int BI_RLE8 = 1;
/*    */   public static final int BI_RLE4 = 2;
/*    */   public static final int BI_BITFIELDS = 3;
/*    */   public static final int BI_JPEG = 4;
/*    */   public static final int BI_PNG = 5;
/* 51 */   public static final String[] compressionTypeNames = { "BI_RGB", "BI_RLE8", "BI_RLE4", "BI_BITFIELDS", "BI_JPEG", "BI_PNG" };
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.bmp.BMPConstants
 * JD-Core Version:    0.6.2
 */
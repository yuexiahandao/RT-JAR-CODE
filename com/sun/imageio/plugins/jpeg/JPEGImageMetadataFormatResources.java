/*     */ package com.sun.imageio.plugins.jpeg;
/*     */ 
/*     */ public class JPEGImageMetadataFormatResources extends JPEGMetadataFormatResources
/*     */ {
/*  33 */   static final Object[][] imageContents = { { "JPEGvariety", "A node grouping all marker segments specific to the variety of stream being read/written (e.g. JFIF) - may be empty" }, { "markerSequence", "A node grouping all non-jfif marker segments" }, { "app0jfif", "A JFIF APP0 marker segment" }, { "app14Adobe", "An Adobe APP14 marker segment" }, { "sof", "A Start Of Frame marker segment" }, { "sos", "A Start Of Scan marker segment" }, { "app0JFXX", "A JFIF extension marker segment" }, { "app2ICC", "An ICC profile APP2 marker segment" }, { "JFIFthumbJPEG", "A JFIF thumbnail in JPEG format (no JFIF segments permitted)" }, { "JFIFthumbPalette", "A JFIF thumbnail as an RGB indexed image" }, { "JFIFthumbRGB", "A JFIF thumbnail as an RGB image" }, { "componentSpec", "A component specification for a frame" }, { "scanComponentSpec", "A component specification for a scan" }, { "app0JFIF/majorVersion", "The major JFIF version number" }, { "app0JFIF/minorVersion", "The minor JFIF version number" }, { "app0JFIF/resUnits", "The resolution units for Xdensity and Ydensity (0 = no units, just aspect ratio; 1 = dots/inch; 2 = dots/cm)" }, { "app0JFIF/Xdensity", "The horizontal density or aspect ratio numerator" }, { "app0JFIF/Ydensity", "The vertical density or aspect ratio denominator" }, { "app0JFIF/thumbWidth", "The width of the thumbnail, or 0 if there isn't one" }, { "app0JFIF/thumbHeight", "The height of the thumbnail, or 0 if there isn't one" }, { "app0JFXX/extensionCode", "The JFXX extension code identifying thumbnail type: (16 = JPEG, 17 = indexed, 19 = RGB" }, { "JFIFthumbPalette/thumbWidth", "The width of the thumbnail" }, { "JFIFthumbPalette/thumbHeight", "The height of the thumbnail" }, { "JFIFthumbRGB/thumbWidth", "The width of the thumbnail" }, { "JFIFthumbRGB/thumbHeight", "The height of the thumbnail" }, { "app14Adobe/version", "The version of Adobe APP14 marker segment" }, { "app14Adobe/flags0", "The flags0 variable of an APP14 marker segment" }, { "app14Adobe/flags1", "The flags1 variable of an APP14 marker segment" }, { "app14Adobe/transform", "The color transform applied to the image (0 = Unknown, 1 = YCbCr, 2 = YCCK)" }, { "sof/process", "The JPEG process (0 = Baseline sequential, 1 = Extended sequential, 2 = Progressive)" }, { "sof/samplePrecision", "The number of bits per sample" }, { "sof/numLines", "The number of lines in the image" }, { "sof/samplesPerLine", "The number of samples per line" }, { "sof/numFrameComponents", "The number of components in the image" }, { "componentSpec/componentId", "The id for this component" }, { "componentSpec/HsamplingFactor", "The horizontal sampling factor for this component" }, { "componentSpec/VsamplingFactor", "The vertical sampling factor for this component" }, { "componentSpec/QtableSelector", "The quantization table to use for this component" }, { "sos/numScanComponents", "The number of components in the scan" }, { "sos/startSpectralSelection", "The first spectral band included in this scan" }, { "sos/endSpectralSelection", "The last spectral band included in this scan" }, { "sos/approxHigh", "The highest bit position included in this scan" }, { "sos/approxLow", "The lowest bit position included in this scan" }, { "scanComponentSpec/componentSelector", "The id of this component" }, { "scanComponentSpec/dcHuffTable", "The huffman table to use for encoding DC coefficients" }, { "scanComponentSpec/acHuffTable", "The huffman table to use for encoding AC coefficients" } };
/*     */ 
/*     */   protected Object[][] getContents()
/*     */   {
/* 130 */     Object[][] arrayOfObject = new Object[commonContents.length + imageContents.length][2];
/*     */ 
/* 132 */     int i = 0;
/* 133 */     for (int j = 0; j < commonContents.length; i++) {
/* 134 */       arrayOfObject[i][0] = commonContents[j][0];
/* 135 */       arrayOfObject[i][1] = commonContents[j][1];
/*     */ 
/* 133 */       j++;
/*     */     }
/*     */ 
/* 137 */     for (j = 0; j < imageContents.length; i++) {
/* 138 */       arrayOfObject[i][0] = imageContents[j][0];
/* 139 */       arrayOfObject[i][1] = imageContents[j][1];
/*     */ 
/* 137 */       j++;
/*     */     }
/*     */ 
/* 141 */     return arrayOfObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.jpeg.JPEGImageMetadataFormatResources
 * JD-Core Version:    0.6.2
 */
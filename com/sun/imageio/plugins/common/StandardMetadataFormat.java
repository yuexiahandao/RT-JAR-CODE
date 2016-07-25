/*     */ package com.sun.imageio.plugins.common;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ import javax.imageio.metadata.IIOMetadataFormatImpl;
/*     */ 
/*     */ public class StandardMetadataFormat extends IIOMetadataFormatImpl
/*     */ {
/*     */   private void addSingleAttributeElement(String paramString1, String paramString2, int paramInt)
/*     */   {
/*  39 */     addElement(paramString1, paramString2, 0);
/*  40 */     addAttribute(paramString1, "value", paramInt, true, null);
/*     */   }
/*     */ 
/*     */   public StandardMetadataFormat() {
/*  44 */     super("javax_imageio_1.0", 2);
/*     */ 
/*  48 */     addElement("Chroma", "javax_imageio_1.0", 2);
/*     */ 
/*  52 */     addElement("ColorSpaceType", "Chroma", 0);
/*     */ 
/*  55 */     ArrayList localArrayList = new ArrayList();
/*  56 */     localArrayList.add("XYZ");
/*  57 */     localArrayList.add("Lab");
/*  58 */     localArrayList.add("Luv");
/*  59 */     localArrayList.add("YCbCr");
/*  60 */     localArrayList.add("Yxy");
/*  61 */     localArrayList.add("YCCK");
/*  62 */     localArrayList.add("PhotoYCC");
/*  63 */     localArrayList.add("RGB");
/*  64 */     localArrayList.add("GRAY");
/*  65 */     localArrayList.add("HSV");
/*  66 */     localArrayList.add("HLS");
/*  67 */     localArrayList.add("CMYK");
/*  68 */     localArrayList.add("CMY");
/*  69 */     localArrayList.add("2CLR");
/*  70 */     localArrayList.add("3CLR");
/*  71 */     localArrayList.add("4CLR");
/*  72 */     localArrayList.add("5CLR");
/*  73 */     localArrayList.add("6CLR");
/*  74 */     localArrayList.add("7CLR");
/*  75 */     localArrayList.add("8CLR");
/*  76 */     localArrayList.add("9CLR");
/*  77 */     localArrayList.add("ACLR");
/*  78 */     localArrayList.add("BCLR");
/*  79 */     localArrayList.add("CCLR");
/*  80 */     localArrayList.add("DCLR");
/*  81 */     localArrayList.add("ECLR");
/*  82 */     localArrayList.add("FCLR");
/*  83 */     addAttribute("ColorSpaceType", "name", 0, true, null, localArrayList);
/*     */ 
/*  91 */     addElement("NumChannels", "Chroma", 0);
/*     */ 
/*  93 */     addAttribute("NumChannels", "value", 2, true, 0, 2147483647);
/*     */ 
/*  99 */     addElement("Gamma", "Chroma", 0);
/* 100 */     addAttribute("Gamma", "value", 3, true, null);
/*     */ 
/* 104 */     addElement("BlackIsZero", "Chroma", 0);
/* 105 */     addBooleanAttribute("BlackIsZero", "value", true, true);
/*     */ 
/* 108 */     addElement("Palette", "Chroma", 0, 2147483647);
/*     */ 
/* 111 */     addElement("PaletteEntry", "Palette", 0);
/* 112 */     addAttribute("PaletteEntry", "index", 2, true, null);
/*     */ 
/* 114 */     addAttribute("PaletteEntry", "red", 2, true, null);
/*     */ 
/* 116 */     addAttribute("PaletteEntry", "green", 2, true, null);
/*     */ 
/* 118 */     addAttribute("PaletteEntry", "blue", 2, true, null);
/*     */ 
/* 120 */     addAttribute("PaletteEntry", "alpha", 2, false, "255");
/*     */ 
/* 124 */     addElement("BackgroundIndex", "Chroma", 0);
/* 125 */     addAttribute("BackgroundIndex", "value", 2, true, null);
/*     */ 
/* 129 */     addElement("BackgroundColor", "Chroma", 0);
/* 130 */     addAttribute("BackgroundColor", "red", 2, true, null);
/*     */ 
/* 132 */     addAttribute("BackgroundColor", "green", 2, true, null);
/*     */ 
/* 134 */     addAttribute("BackgroundColor", "blue", 2, true, null);
/*     */ 
/* 138 */     addElement("Compression", "javax_imageio_1.0", 2);
/*     */ 
/* 142 */     addSingleAttributeElement("CompressionTypeName", "Compression", 0);
/*     */ 
/* 147 */     addElement("Lossless", "Compression", 0);
/* 148 */     addBooleanAttribute("Lossless", "value", true, true);
/*     */ 
/* 151 */     addSingleAttributeElement("NumProgressiveScans", "Compression", 2);
/*     */ 
/* 156 */     addSingleAttributeElement("BitRate", "Compression", 3);
/*     */ 
/* 161 */     addElement("Data", "javax_imageio_1.0", 2);
/*     */ 
/* 165 */     addElement("PlanarConfiguration", "Data", 0);
/*     */ 
/* 167 */     localArrayList = new ArrayList();
/* 168 */     localArrayList.add("PixelInterleaved");
/* 169 */     localArrayList.add("PlaneInterleaved");
/* 170 */     localArrayList.add("LineInterleaved");
/* 171 */     localArrayList.add("TileInterleaved");
/* 172 */     addAttribute("PlanarConfiguration", "value", 0, true, null, localArrayList);
/*     */ 
/* 179 */     addElement("SampleFormat", "Data", 0);
/*     */ 
/* 181 */     localArrayList = new ArrayList();
/* 182 */     localArrayList.add("SignedIntegral");
/* 183 */     localArrayList.add("UnsignedIntegral");
/* 184 */     localArrayList.add("Real");
/* 185 */     localArrayList.add("Index");
/* 186 */     addAttribute("SampleFormat", "value", 0, true, null, localArrayList);
/*     */ 
/* 193 */     addElement("BitsPerSample", "Data", 0);
/*     */ 
/* 195 */     addAttribute("BitsPerSample", "value", 2, true, 1, 2147483647);
/*     */ 
/* 201 */     addElement("SignificantBitsPerSample", "Data", 0);
/* 202 */     addAttribute("SignificantBitsPerSample", "value", 2, true, 1, 2147483647);
/*     */ 
/* 208 */     addElement("SampleMSB", "Data", 0);
/*     */ 
/* 210 */     addAttribute("SampleMSB", "value", 2, true, 1, 2147483647);
/*     */ 
/* 216 */     addElement("Dimension", "javax_imageio_1.0", 2);
/*     */ 
/* 220 */     addSingleAttributeElement("PixelAspectRatio", "Dimension", 3);
/*     */ 
/* 225 */     addElement("ImageOrientation", "Dimension", 0);
/*     */ 
/* 228 */     localArrayList = new ArrayList();
/* 229 */     localArrayList.add("Normal");
/* 230 */     localArrayList.add("Rotate90");
/* 231 */     localArrayList.add("Rotate180");
/* 232 */     localArrayList.add("Rotate270");
/* 233 */     localArrayList.add("FlipH");
/* 234 */     localArrayList.add("FlipV");
/* 235 */     localArrayList.add("FlipHRotate90");
/* 236 */     localArrayList.add("FlipVRotate90");
/* 237 */     addAttribute("ImageOrientation", "value", 0, true, null, localArrayList);
/*     */ 
/* 244 */     addSingleAttributeElement("HorizontalPixelSize", "Dimension", 3);
/*     */ 
/* 249 */     addSingleAttributeElement("VerticalPixelSize", "Dimension", 3);
/*     */ 
/* 254 */     addSingleAttributeElement("HorizontalPhysicalPixelSpacing", "Dimension", 3);
/*     */ 
/* 259 */     addSingleAttributeElement("VerticalPhysicalPixelSpacing", "Dimension", 3);
/*     */ 
/* 264 */     addSingleAttributeElement("HorizontalPosition", "Dimension", 3);
/*     */ 
/* 269 */     addSingleAttributeElement("VerticalPosition", "Dimension", 3);
/*     */ 
/* 274 */     addSingleAttributeElement("HorizontalPixelOffset", "Dimension", 2);
/*     */ 
/* 279 */     addSingleAttributeElement("VerticalPixelOffset", "Dimension", 2);
/*     */ 
/* 284 */     addSingleAttributeElement("HorizontalScreenSize", "Dimension", 2);
/*     */ 
/* 289 */     addSingleAttributeElement("VerticalScreenSize", "Dimension", 2);
/*     */ 
/* 295 */     addElement("Document", "javax_imageio_1.0", 2);
/*     */ 
/* 299 */     addElement("FormatVersion", "Document", 0);
/*     */ 
/* 301 */     addAttribute("FormatVersion", "value", 0, true, null);
/*     */ 
/* 307 */     addElement("SubimageInterpretation", "Document", 0);
/*     */ 
/* 309 */     localArrayList = new ArrayList();
/* 310 */     localArrayList.add("Standalone");
/* 311 */     localArrayList.add("SinglePage");
/* 312 */     localArrayList.add("FullResolution");
/* 313 */     localArrayList.add("ReducedResolution");
/* 314 */     localArrayList.add("PyramidLayer");
/* 315 */     localArrayList.add("Preview");
/* 316 */     localArrayList.add("VolumeSlice");
/* 317 */     localArrayList.add("ObjectView");
/* 318 */     localArrayList.add("Panorama");
/* 319 */     localArrayList.add("AnimationFrame");
/* 320 */     localArrayList.add("TransparencyMask");
/* 321 */     localArrayList.add("CompositingLayer");
/* 322 */     localArrayList.add("SpectralSlice");
/* 323 */     localArrayList.add("Unknown");
/* 324 */     addAttribute("SubimageInterpretation", "value", 0, true, null, localArrayList);
/*     */ 
/* 331 */     addElement("ImageCreationTime", "Document", 0);
/*     */ 
/* 333 */     addAttribute("ImageCreationTime", "year", 2, true, null);
/*     */ 
/* 337 */     addAttribute("ImageCreationTime", "month", 2, true, null, "1", "12", true, true);
/*     */ 
/* 342 */     addAttribute("ImageCreationTime", "day", 2, true, null, "1", "31", true, true);
/*     */ 
/* 347 */     addAttribute("ImageCreationTime", "hour", 2, false, "0", "0", "23", true, true);
/*     */ 
/* 352 */     addAttribute("ImageCreationTime", "minute", 2, false, "0", "0", "59", true, true);
/*     */ 
/* 358 */     addAttribute("ImageCreationTime", "second", 2, false, "0", "0", "60", true, true);
/*     */ 
/* 365 */     addElement("ImageModificationTime", "Document", 0);
/*     */ 
/* 367 */     addAttribute("ImageModificationTime", "year", 2, true, null);
/*     */ 
/* 371 */     addAttribute("ImageModificationTime", "month", 2, true, null, "1", "12", true, true);
/*     */ 
/* 376 */     addAttribute("ImageModificationTime", "day", 2, true, null, "1", "31", true, true);
/*     */ 
/* 381 */     addAttribute("ImageModificationTime", "hour", 2, false, "0", "0", "23", true, true);
/*     */ 
/* 386 */     addAttribute("ImageModificationTime", "minute", 2, false, "0", "0", "59", true, true);
/*     */ 
/* 392 */     addAttribute("ImageModificationTime", "second", 2, false, "0", "0", "60", true, true);
/*     */ 
/* 399 */     addElement("Text", "javax_imageio_1.0", 0, 2147483647);
/*     */ 
/* 403 */     addElement("TextEntry", "Text", 0);
/* 404 */     addAttribute("TextEntry", "keyword", 0, false, null);
/*     */ 
/* 408 */     addAttribute("TextEntry", "value", 0, true, null);
/*     */ 
/* 412 */     addAttribute("TextEntry", "language", 0, false, null);
/*     */ 
/* 416 */     addAttribute("TextEntry", "encoding", 0, false, null);
/*     */ 
/* 421 */     localArrayList = new ArrayList();
/* 422 */     localArrayList.add("none");
/* 423 */     localArrayList.add("lzw");
/* 424 */     localArrayList.add("zip");
/* 425 */     localArrayList.add("bzip");
/* 426 */     localArrayList.add("other");
/* 427 */     addAttribute("TextEntry", "compression", 0, false, "none", localArrayList);
/*     */ 
/* 434 */     addElement("Transparency", "javax_imageio_1.0", 2);
/*     */ 
/* 438 */     addElement("Alpha", "Transparency", 0);
/*     */ 
/* 440 */     localArrayList = new ArrayList();
/* 441 */     localArrayList.add("none");
/* 442 */     localArrayList.add("premultiplied");
/* 443 */     localArrayList.add("nonpremultiplied");
/* 444 */     addAttribute("Alpha", "value", 0, false, "none", localArrayList);
/*     */ 
/* 451 */     addSingleAttributeElement("TransparentIndex", "Transparency", 2);
/*     */ 
/* 455 */     addElement("TransparentColor", "Transparency", 0);
/*     */ 
/* 457 */     addAttribute("TransparentColor", "value", 2, true, 0, 2147483647);
/*     */ 
/* 463 */     addElement("TileTransparencies", "Transparency", 0, 2147483647);
/*     */ 
/* 467 */     addElement("TransparentTile", "TileTransparencies", 0);
/*     */ 
/* 469 */     addAttribute("TransparentTile", "x", 2, true, null);
/*     */ 
/* 473 */     addAttribute("TransparentTile", "y", 2, true, null);
/*     */ 
/* 479 */     addElement("TileOpacities", "Transparency", 0, 2147483647);
/*     */ 
/* 483 */     addElement("OpaqueTile", "TileOpacities", 0);
/*     */ 
/* 485 */     addAttribute("OpaqueTile", "x", 2, true, null);
/*     */ 
/* 489 */     addAttribute("OpaqueTile", "y", 2, true, null);
/*     */   }
/*     */ 
/*     */   public boolean canNodeAppear(String paramString, ImageTypeSpecifier paramImageTypeSpecifier)
/*     */   {
/* 497 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.common.StandardMetadataFormat
 * JD-Core Version:    0.6.2
 */
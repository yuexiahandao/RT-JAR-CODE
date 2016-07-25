/*      */ package com.sun.imageio.plugins.png;
/*      */ 
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.util.ArrayList;
/*      */ import java.util.StringTokenizer;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.metadata.IIOInvalidTreeException;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.metadata.IIOMetadataNode;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ 
/*      */ public class PNGMetadata extends IIOMetadata
/*      */   implements Cloneable
/*      */ {
/*      */   public static final String nativeMetadataFormatName = "javax_imageio_png_1.0";
/*      */   protected static final String nativeMetadataFormatClassName = "com.sun.imageio.plugins.png.PNGMetadataFormat";
/*   52 */   public static final String[] IHDR_colorTypeNames = { "Grayscale", null, "RGB", "Palette", "GrayAlpha", null, "RGBAlpha" };
/*      */ 
/*   57 */   public static final int[] IHDR_numChannels = { 1, 0, 3, 3, 2, 0, 4 };
/*      */ 
/*   62 */   public static final String[] IHDR_bitDepths = { "1", "2", "4", "8", "16" };
/*      */ 
/*   67 */   public static final String[] IHDR_compressionMethodNames = { "deflate" };
/*      */ 
/*   72 */   public static final String[] IHDR_filterMethodNames = { "adaptive" };
/*      */ 
/*   77 */   public static final String[] IHDR_interlaceMethodNames = { "none", "adam7" };
/*      */ 
/*   82 */   public static final String[] iCCP_compressionMethodNames = { "deflate" };
/*      */ 
/*   87 */   public static final String[] zTXt_compressionMethodNames = { "deflate" };
/*      */   public static final int PHYS_UNIT_UNKNOWN = 0;
/*      */   public static final int PHYS_UNIT_METER = 1;
/*   98 */   public static final String[] unitSpecifierNames = { "unknown", "meter" };
/*      */ 
/*  103 */   public static final String[] renderingIntentNames = { "Perceptual", "Relative colorimetric", "Saturation", "Absolute colorimetric" };
/*      */ 
/*  112 */   public static final String[] colorSpaceTypeNames = { "GRAY", null, "RGB", "RGB", "GRAY", null, "RGB" };
/*      */   public boolean IHDR_present;
/*      */   public int IHDR_width;
/*      */   public int IHDR_height;
/*      */   public int IHDR_bitDepth;
/*      */   public int IHDR_colorType;
/*      */   public int IHDR_compressionMethod;
/*      */   public int IHDR_filterMethod;
/*      */   public int IHDR_interlaceMethod;
/*      */   public boolean PLTE_present;
/*      */   public byte[] PLTE_red;
/*      */   public byte[] PLTE_green;
/*      */   public byte[] PLTE_blue;
/*  138 */   public int[] PLTE_order = null;
/*      */   public boolean bKGD_present;
/*      */   public int bKGD_colorType;
/*      */   public int bKGD_index;
/*      */   public int bKGD_gray;
/*      */   public int bKGD_red;
/*      */   public int bKGD_green;
/*      */   public int bKGD_blue;
/*      */   public boolean cHRM_present;
/*      */   public int cHRM_whitePointX;
/*      */   public int cHRM_whitePointY;
/*      */   public int cHRM_redX;
/*      */   public int cHRM_redY;
/*      */   public int cHRM_greenX;
/*      */   public int cHRM_greenY;
/*      */   public int cHRM_blueX;
/*      */   public int cHRM_blueY;
/*      */   public boolean gAMA_present;
/*      */   public int gAMA_gamma;
/*      */   public boolean hIST_present;
/*      */   public char[] hIST_histogram;
/*      */   public boolean iCCP_present;
/*      */   public String iCCP_profileName;
/*      */   public int iCCP_compressionMethod;
/*      */   public byte[] iCCP_compressedProfile;
/*  177 */   public ArrayList<String> iTXt_keyword = new ArrayList();
/*  178 */   public ArrayList<Boolean> iTXt_compressionFlag = new ArrayList();
/*  179 */   public ArrayList<Integer> iTXt_compressionMethod = new ArrayList();
/*  180 */   public ArrayList<String> iTXt_languageTag = new ArrayList();
/*  181 */   public ArrayList<String> iTXt_translatedKeyword = new ArrayList();
/*  182 */   public ArrayList<String> iTXt_text = new ArrayList();
/*      */   public boolean pHYs_present;
/*      */   public int pHYs_pixelsPerUnitXAxis;
/*      */   public int pHYs_pixelsPerUnitYAxis;
/*      */   public int pHYs_unitSpecifier;
/*      */   public boolean sBIT_present;
/*      */   public int sBIT_colorType;
/*      */   public int sBIT_grayBits;
/*      */   public int sBIT_redBits;
/*      */   public int sBIT_greenBits;
/*      */   public int sBIT_blueBits;
/*      */   public int sBIT_alphaBits;
/*      */   public boolean sPLT_present;
/*      */   public String sPLT_paletteName;
/*      */   public int sPLT_sampleDepth;
/*      */   public int[] sPLT_red;
/*      */   public int[] sPLT_green;
/*      */   public int[] sPLT_blue;
/*      */   public int[] sPLT_alpha;
/*      */   public int[] sPLT_frequency;
/*      */   public boolean sRGB_present;
/*      */   public int sRGB_renderingIntent;
/*  214 */   public ArrayList<String> tEXt_keyword = new ArrayList();
/*  215 */   public ArrayList<String> tEXt_text = new ArrayList();
/*      */   public boolean tIME_present;
/*      */   public int tIME_year;
/*      */   public int tIME_month;
/*      */   public int tIME_day;
/*      */   public int tIME_hour;
/*      */   public int tIME_minute;
/*      */   public int tIME_second;
/*      */   public boolean tRNS_present;
/*      */   public int tRNS_colorType;
/*      */   public byte[] tRNS_alpha;
/*      */   public int tRNS_gray;
/*      */   public int tRNS_red;
/*      */   public int tRNS_green;
/*      */   public int tRNS_blue;
/*  238 */   public ArrayList<String> zTXt_keyword = new ArrayList();
/*  239 */   public ArrayList<Integer> zTXt_compressionMethod = new ArrayList();
/*  240 */   public ArrayList<String> zTXt_text = new ArrayList();
/*      */ 
/*  243 */   public ArrayList<String> unknownChunkType = new ArrayList();
/*  244 */   public ArrayList<byte[]> unknownChunkData = new ArrayList();
/*      */ 
/*      */   public PNGMetadata() {
/*  247 */     super(true, "javax_imageio_png_1.0", "com.sun.imageio.plugins.png.PNGMetadataFormat", null, null);
/*      */   }
/*      */ 
/*      */   public PNGMetadata(IIOMetadata paramIIOMetadata)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void initialize(ImageTypeSpecifier paramImageTypeSpecifier, int paramInt)
/*      */   {
/*  263 */     ColorModel localColorModel = paramImageTypeSpecifier.getColorModel();
/*  264 */     SampleModel localSampleModel = paramImageTypeSpecifier.getSampleModel();
/*      */ 
/*  267 */     int[] arrayOfInt = localSampleModel.getSampleSize();
/*  268 */     int i = arrayOfInt[0];
/*      */ 
/*  271 */     for (int j = 1; j < arrayOfInt.length; j++) {
/*  272 */       if (arrayOfInt[j] > i) {
/*  273 */         i = arrayOfInt[j];
/*      */       }
/*      */     }
/*      */ 
/*  277 */     if ((arrayOfInt.length > 1) && (i < 8)) {
/*  278 */       i = 8;
/*      */     }
/*      */ 
/*  282 */     if ((i > 2) && (i < 4))
/*  283 */       i = 4;
/*  284 */     else if ((i > 4) && (i < 8))
/*  285 */       i = 8;
/*  286 */     else if ((i > 8) && (i < 16))
/*  287 */       i = 16;
/*  288 */     else if (i > 16) {
/*  289 */       throw new RuntimeException("bitDepth > 16!");
/*      */     }
/*  291 */     this.IHDR_bitDepth = i;
/*      */ 
/*  294 */     if ((localColorModel instanceof IndexColorModel)) {
/*  295 */       IndexColorModel localIndexColorModel = (IndexColorModel)localColorModel;
/*  296 */       int k = localIndexColorModel.getMapSize();
/*      */ 
/*  298 */       byte[] arrayOfByte1 = new byte[k];
/*  299 */       localIndexColorModel.getReds(arrayOfByte1);
/*  300 */       byte[] arrayOfByte2 = new byte[k];
/*  301 */       localIndexColorModel.getGreens(arrayOfByte2);
/*  302 */       byte[] arrayOfByte3 = new byte[k];
/*  303 */       localIndexColorModel.getBlues(arrayOfByte3);
/*      */ 
/*  307 */       int m = 0;
/*  308 */       if ((!this.IHDR_present) || (this.IHDR_colorType != 3))
/*      */       {
/*  310 */         m = 1;
/*  311 */         int n = 255 / ((1 << this.IHDR_bitDepth) - 1);
/*  312 */         for (int i1 = 0; i1 < k; i1++) {
/*  313 */           int i2 = arrayOfByte1[i1];
/*  314 */           if ((i2 != (byte)(i1 * n)) || (i2 != arrayOfByte2[i1]) || (i2 != arrayOfByte3[i1]))
/*      */           {
/*  317 */             m = 0;
/*  318 */             break;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  324 */       boolean bool = localColorModel.hasAlpha();
/*      */ 
/*  326 */       byte[] arrayOfByte4 = null;
/*  327 */       if (bool) {
/*  328 */         arrayOfByte4 = new byte[k];
/*  329 */         localIndexColorModel.getAlphas(arrayOfByte4);
/*      */       }
/*      */ 
/*  341 */       if ((m != 0) && (bool) && ((i == 8) || (i == 16))) {
/*  342 */         this.IHDR_colorType = 4;
/*  343 */       } else if ((m != 0) && (!bool)) {
/*  344 */         this.IHDR_colorType = 0;
/*      */       } else {
/*  346 */         this.IHDR_colorType = 3;
/*  347 */         this.PLTE_present = true;
/*  348 */         this.PLTE_order = null;
/*  349 */         this.PLTE_red = ((byte[])arrayOfByte1.clone());
/*  350 */         this.PLTE_green = ((byte[])arrayOfByte2.clone());
/*  351 */         this.PLTE_blue = ((byte[])arrayOfByte3.clone());
/*      */ 
/*  353 */         if (bool) {
/*  354 */           this.tRNS_present = true;
/*  355 */           this.tRNS_colorType = 3;
/*      */ 
/*  357 */           this.PLTE_order = new int[arrayOfByte4.length];
/*      */ 
/*  366 */           byte[] arrayOfByte5 = new byte[arrayOfByte4.length];
/*      */ 
/*  370 */           int i3 = 0;
/*  371 */           for (int i4 = 0; i4 < arrayOfByte4.length; i4++) {
/*  372 */             if (arrayOfByte4[i4] != -1) {
/*  373 */               this.PLTE_order[i4] = i3;
/*  374 */               arrayOfByte5[i3] = arrayOfByte4[i4];
/*  375 */               i3++;
/*      */             }
/*      */           }
/*  378 */           i4 = i3;
/*      */ 
/*  382 */           for (int i5 = 0; i5 < arrayOfByte4.length; i5++) {
/*  383 */             if (arrayOfByte4[i5] == -1) {
/*  384 */               this.PLTE_order[i5] = (i3++);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  389 */           byte[] arrayOfByte6 = this.PLTE_red;
/*  390 */           byte[] arrayOfByte7 = this.PLTE_green;
/*  391 */           byte[] arrayOfByte8 = this.PLTE_blue;
/*  392 */           int i6 = arrayOfByte6.length;
/*  393 */           this.PLTE_red = new byte[i6];
/*  394 */           this.PLTE_green = new byte[i6];
/*  395 */           this.PLTE_blue = new byte[i6];
/*  396 */           for (int i7 = 0; i7 < i6; i7++) {
/*  397 */             this.PLTE_red[this.PLTE_order[i7]] = arrayOfByte6[i7];
/*  398 */             this.PLTE_green[this.PLTE_order[i7]] = arrayOfByte7[i7];
/*  399 */             this.PLTE_blue[this.PLTE_order[i7]] = arrayOfByte8[i7];
/*      */           }
/*      */ 
/*  403 */           this.tRNS_alpha = new byte[i4];
/*  404 */           System.arraycopy(arrayOfByte5, 0, this.tRNS_alpha, 0, i4);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*  409 */     else if (paramInt == 1) {
/*  410 */       this.IHDR_colorType = 0;
/*  411 */     } else if (paramInt == 2) {
/*  412 */       this.IHDR_colorType = 4;
/*  413 */     } else if (paramInt == 3) {
/*  414 */       this.IHDR_colorType = 2;
/*  415 */     } else if (paramInt == 4) {
/*  416 */       this.IHDR_colorType = 6;
/*      */     } else {
/*  418 */       throw new RuntimeException("Number of bands not 1-4!");
/*      */     }
/*      */ 
/*  422 */     this.IHDR_present = true;
/*      */   }
/*      */ 
/*      */   public boolean isReadOnly() {
/*  426 */     return false;
/*      */   }
/*      */ 
/*      */   private ArrayList<byte[]> cloneBytesArrayList(ArrayList<byte[]> paramArrayList) {
/*  430 */     if (paramArrayList == null) {
/*  431 */       return null;
/*      */     }
/*  433 */     ArrayList localArrayList = new ArrayList(paramArrayList.size());
/*  434 */     for (byte[] arrayOfByte : paramArrayList) {
/*  435 */       localArrayList.add(arrayOfByte == null ? null : (byte[])arrayOfByte.clone());
/*      */     }
/*  437 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     PNGMetadata localPNGMetadata;
/*      */     try
/*      */     {
/*  445 */       localPNGMetadata = (PNGMetadata)super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  447 */       return null;
/*      */     }
/*      */ 
/*  451 */     localPNGMetadata.unknownChunkData = cloneBytesArrayList(this.unknownChunkData);
/*      */ 
/*  454 */     return localPNGMetadata;
/*      */   }
/*      */ 
/*      */   public Node getAsTree(String paramString) {
/*  458 */     if (paramString.equals("javax_imageio_png_1.0"))
/*  459 */       return getNativeTree();
/*  460 */     if (paramString.equals("javax_imageio_1.0"))
/*      */     {
/*  462 */       return getStandardTree();
/*      */     }
/*  464 */     throw new IllegalArgumentException("Not a recognized format!");
/*      */   }
/*      */ 
/*      */   private Node getNativeTree()
/*      */   {
/*  469 */     IIOMetadataNode localIIOMetadataNode1 = null;
/*  470 */     IIOMetadataNode localIIOMetadataNode2 = new IIOMetadataNode("javax_imageio_png_1.0");
/*      */     IIOMetadataNode localIIOMetadataNode3;
/*  473 */     if (this.IHDR_present) {
/*  474 */       localIIOMetadataNode3 = new IIOMetadataNode("IHDR");
/*  475 */       localIIOMetadataNode3.setAttribute("width", Integer.toString(this.IHDR_width));
/*  476 */       localIIOMetadataNode3.setAttribute("height", Integer.toString(this.IHDR_height));
/*  477 */       localIIOMetadataNode3.setAttribute("bitDepth", Integer.toString(this.IHDR_bitDepth));
/*      */ 
/*  479 */       localIIOMetadataNode3.setAttribute("colorType", IHDR_colorTypeNames[this.IHDR_colorType]);
/*      */ 
/*  482 */       localIIOMetadataNode3.setAttribute("compressionMethod", IHDR_compressionMethodNames[this.IHDR_compressionMethod]);
/*      */ 
/*  485 */       localIIOMetadataNode3.setAttribute("filterMethod", IHDR_filterMethodNames[this.IHDR_filterMethod]);
/*      */ 
/*  487 */       localIIOMetadataNode3.setAttribute("interlaceMethod", IHDR_interlaceMethodNames[this.IHDR_interlaceMethod]);
/*      */ 
/*  489 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */     int i;
/*      */     IIOMetadataNode localIIOMetadataNode6;
/*  493 */     if (this.PLTE_present) {
/*  494 */       localIIOMetadataNode3 = new IIOMetadataNode("PLTE");
/*  495 */       i = this.PLTE_red.length;
/*  496 */       for (int k = 0; k < i; k++) {
/*  497 */         localIIOMetadataNode6 = new IIOMetadataNode("PLTEEntry");
/*  498 */         localIIOMetadataNode6.setAttribute("index", Integer.toString(k));
/*  499 */         localIIOMetadataNode6.setAttribute("red", Integer.toString(this.PLTE_red[k] & 0xFF));
/*      */ 
/*  501 */         localIIOMetadataNode6.setAttribute("green", Integer.toString(this.PLTE_green[k] & 0xFF));
/*      */ 
/*  503 */         localIIOMetadataNode6.setAttribute("blue", Integer.toString(this.PLTE_blue[k] & 0xFF));
/*      */ 
/*  505 */         localIIOMetadataNode3.appendChild(localIIOMetadataNode6);
/*      */       }
/*      */ 
/*  508 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  512 */     if (this.bKGD_present) {
/*  513 */       localIIOMetadataNode3 = new IIOMetadataNode("bKGD");
/*      */ 
/*  515 */       if (this.bKGD_colorType == 3) {
/*  516 */         localIIOMetadataNode1 = new IIOMetadataNode("bKGD_Palette");
/*  517 */         localIIOMetadataNode1.setAttribute("index", Integer.toString(this.bKGD_index));
/*  518 */       } else if (this.bKGD_colorType == 0) {
/*  519 */         localIIOMetadataNode1 = new IIOMetadataNode("bKGD_Grayscale");
/*  520 */         localIIOMetadataNode1.setAttribute("gray", Integer.toString(this.bKGD_gray));
/*  521 */       } else if (this.bKGD_colorType == 2) {
/*  522 */         localIIOMetadataNode1 = new IIOMetadataNode("bKGD_RGB");
/*  523 */         localIIOMetadataNode1.setAttribute("red", Integer.toString(this.bKGD_red));
/*  524 */         localIIOMetadataNode1.setAttribute("green", Integer.toString(this.bKGD_green));
/*  525 */         localIIOMetadataNode1.setAttribute("blue", Integer.toString(this.bKGD_blue));
/*      */       }
/*  527 */       localIIOMetadataNode3.appendChild(localIIOMetadataNode1);
/*      */ 
/*  529 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  533 */     if (this.cHRM_present) {
/*  534 */       localIIOMetadataNode3 = new IIOMetadataNode("cHRM");
/*  535 */       localIIOMetadataNode3.setAttribute("whitePointX", Integer.toString(this.cHRM_whitePointX));
/*      */ 
/*  537 */       localIIOMetadataNode3.setAttribute("whitePointY", Integer.toString(this.cHRM_whitePointY));
/*      */ 
/*  539 */       localIIOMetadataNode3.setAttribute("redX", Integer.toString(this.cHRM_redX));
/*  540 */       localIIOMetadataNode3.setAttribute("redY", Integer.toString(this.cHRM_redY));
/*  541 */       localIIOMetadataNode3.setAttribute("greenX", Integer.toString(this.cHRM_greenX));
/*  542 */       localIIOMetadataNode3.setAttribute("greenY", Integer.toString(this.cHRM_greenY));
/*  543 */       localIIOMetadataNode3.setAttribute("blueX", Integer.toString(this.cHRM_blueX));
/*  544 */       localIIOMetadataNode3.setAttribute("blueY", Integer.toString(this.cHRM_blueY));
/*      */ 
/*  546 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  550 */     if (this.gAMA_present) {
/*  551 */       localIIOMetadataNode3 = new IIOMetadataNode("gAMA");
/*  552 */       localIIOMetadataNode3.setAttribute("value", Integer.toString(this.gAMA_gamma));
/*      */ 
/*  554 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */     IIOMetadataNode localIIOMetadataNode4;
/*  558 */     if (this.hIST_present) {
/*  559 */       localIIOMetadataNode3 = new IIOMetadataNode("hIST");
/*      */ 
/*  561 */       for (i = 0; i < this.hIST_histogram.length; i++) {
/*  562 */         localIIOMetadataNode4 = new IIOMetadataNode("hISTEntry");
/*      */ 
/*  564 */         localIIOMetadataNode4.setAttribute("index", Integer.toString(i));
/*  565 */         localIIOMetadataNode4.setAttribute("value", Integer.toString(this.hIST_histogram[i]));
/*      */ 
/*  567 */         localIIOMetadataNode3.appendChild(localIIOMetadataNode4);
/*      */       }
/*      */ 
/*  570 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  574 */     if (this.iCCP_present) {
/*  575 */       localIIOMetadataNode3 = new IIOMetadataNode("iCCP");
/*  576 */       localIIOMetadataNode3.setAttribute("profileName", this.iCCP_profileName);
/*  577 */       localIIOMetadataNode3.setAttribute("compressionMethod", iCCP_compressionMethodNames[this.iCCP_compressionMethod]);
/*      */ 
/*  580 */       Object localObject = this.iCCP_compressedProfile;
/*  581 */       if (localObject != null) {
/*  582 */         localObject = ((byte[])localObject).clone();
/*      */       }
/*  584 */       localIIOMetadataNode3.setUserObject(localObject);
/*      */ 
/*  586 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */     int j;
/*  590 */     if (this.iTXt_keyword.size() > 0) {
/*  591 */       localIIOMetadataNode3 = new IIOMetadataNode("iTXt");
/*  592 */       for (j = 0; j < this.iTXt_keyword.size(); j++) {
/*  593 */         localIIOMetadataNode4 = new IIOMetadataNode("iTXtEntry");
/*  594 */         localIIOMetadataNode4.setAttribute("keyword", (String)this.iTXt_keyword.get(j));
/*  595 */         localIIOMetadataNode4.setAttribute("compressionFlag", ((Boolean)this.iTXt_compressionFlag.get(j)).booleanValue() ? "TRUE" : "FALSE");
/*      */ 
/*  597 */         localIIOMetadataNode4.setAttribute("compressionMethod", ((Integer)this.iTXt_compressionMethod.get(j)).toString());
/*      */ 
/*  599 */         localIIOMetadataNode4.setAttribute("languageTag", (String)this.iTXt_languageTag.get(j));
/*      */ 
/*  601 */         localIIOMetadataNode4.setAttribute("translatedKeyword", (String)this.iTXt_translatedKeyword.get(j));
/*      */ 
/*  603 */         localIIOMetadataNode4.setAttribute("text", (String)this.iTXt_text.get(j));
/*      */ 
/*  605 */         localIIOMetadataNode3.appendChild(localIIOMetadataNode4);
/*      */       }
/*      */ 
/*  608 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  612 */     if (this.pHYs_present) {
/*  613 */       localIIOMetadataNode3 = new IIOMetadataNode("pHYs");
/*  614 */       localIIOMetadataNode3.setAttribute("pixelsPerUnitXAxis", Integer.toString(this.pHYs_pixelsPerUnitXAxis));
/*      */ 
/*  616 */       localIIOMetadataNode3.setAttribute("pixelsPerUnitYAxis", Integer.toString(this.pHYs_pixelsPerUnitYAxis));
/*      */ 
/*  618 */       localIIOMetadataNode3.setAttribute("unitSpecifier", unitSpecifierNames[this.pHYs_unitSpecifier]);
/*      */ 
/*  621 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  625 */     if (this.sBIT_present) {
/*  626 */       localIIOMetadataNode3 = new IIOMetadataNode("sBIT");
/*      */ 
/*  628 */       if (this.sBIT_colorType == 0) {
/*  629 */         localIIOMetadataNode1 = new IIOMetadataNode("sBIT_Grayscale");
/*  630 */         localIIOMetadataNode1.setAttribute("gray", Integer.toString(this.sBIT_grayBits));
/*      */       }
/*  632 */       else if (this.sBIT_colorType == 4) {
/*  633 */         localIIOMetadataNode1 = new IIOMetadataNode("sBIT_GrayAlpha");
/*  634 */         localIIOMetadataNode1.setAttribute("gray", Integer.toString(this.sBIT_grayBits));
/*      */ 
/*  636 */         localIIOMetadataNode1.setAttribute("alpha", Integer.toString(this.sBIT_alphaBits));
/*      */       }
/*  638 */       else if (this.sBIT_colorType == 2) {
/*  639 */         localIIOMetadataNode1 = new IIOMetadataNode("sBIT_RGB");
/*  640 */         localIIOMetadataNode1.setAttribute("red", Integer.toString(this.sBIT_redBits));
/*      */ 
/*  642 */         localIIOMetadataNode1.setAttribute("green", Integer.toString(this.sBIT_greenBits));
/*      */ 
/*  644 */         localIIOMetadataNode1.setAttribute("blue", Integer.toString(this.sBIT_blueBits));
/*      */       }
/*  646 */       else if (this.sBIT_colorType == 6) {
/*  647 */         localIIOMetadataNode1 = new IIOMetadataNode("sBIT_RGBAlpha");
/*  648 */         localIIOMetadataNode1.setAttribute("red", Integer.toString(this.sBIT_redBits));
/*      */ 
/*  650 */         localIIOMetadataNode1.setAttribute("green", Integer.toString(this.sBIT_greenBits));
/*      */ 
/*  652 */         localIIOMetadataNode1.setAttribute("blue", Integer.toString(this.sBIT_blueBits));
/*      */ 
/*  654 */         localIIOMetadataNode1.setAttribute("alpha", Integer.toString(this.sBIT_alphaBits));
/*      */       }
/*  656 */       else if (this.sBIT_colorType == 3) {
/*  657 */         localIIOMetadataNode1 = new IIOMetadataNode("sBIT_Palette");
/*  658 */         localIIOMetadataNode1.setAttribute("red", Integer.toString(this.sBIT_redBits));
/*      */ 
/*  660 */         localIIOMetadataNode1.setAttribute("green", Integer.toString(this.sBIT_greenBits));
/*      */ 
/*  662 */         localIIOMetadataNode1.setAttribute("blue", Integer.toString(this.sBIT_blueBits));
/*      */       }
/*      */ 
/*  665 */       localIIOMetadataNode3.appendChild(localIIOMetadataNode1);
/*      */ 
/*  667 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  671 */     if (this.sPLT_present) {
/*  672 */       localIIOMetadataNode3 = new IIOMetadataNode("sPLT");
/*      */ 
/*  674 */       localIIOMetadataNode3.setAttribute("name", this.sPLT_paletteName);
/*  675 */       localIIOMetadataNode3.setAttribute("sampleDepth", Integer.toString(this.sPLT_sampleDepth));
/*      */ 
/*  678 */       j = this.sPLT_red.length;
/*  679 */       for (int m = 0; m < j; m++) {
/*  680 */         localIIOMetadataNode6 = new IIOMetadataNode("sPLTEntry");
/*  681 */         localIIOMetadataNode6.setAttribute("index", Integer.toString(m));
/*  682 */         localIIOMetadataNode6.setAttribute("red", Integer.toString(this.sPLT_red[m]));
/*  683 */         localIIOMetadataNode6.setAttribute("green", Integer.toString(this.sPLT_green[m]));
/*  684 */         localIIOMetadataNode6.setAttribute("blue", Integer.toString(this.sPLT_blue[m]));
/*  685 */         localIIOMetadataNode6.setAttribute("alpha", Integer.toString(this.sPLT_alpha[m]));
/*  686 */         localIIOMetadataNode6.setAttribute("frequency", Integer.toString(this.sPLT_frequency[m]));
/*      */ 
/*  688 */         localIIOMetadataNode3.appendChild(localIIOMetadataNode6);
/*      */       }
/*      */ 
/*  691 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  695 */     if (this.sRGB_present) {
/*  696 */       localIIOMetadataNode3 = new IIOMetadataNode("sRGB");
/*  697 */       localIIOMetadataNode3.setAttribute("renderingIntent", renderingIntentNames[this.sRGB_renderingIntent]);
/*      */ 
/*  700 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */     IIOMetadataNode localIIOMetadataNode5;
/*  704 */     if (this.tEXt_keyword.size() > 0) {
/*  705 */       localIIOMetadataNode3 = new IIOMetadataNode("tEXt");
/*  706 */       for (j = 0; j < this.tEXt_keyword.size(); j++) {
/*  707 */         localIIOMetadataNode5 = new IIOMetadataNode("tEXtEntry");
/*  708 */         localIIOMetadataNode5.setAttribute("keyword", (String)this.tEXt_keyword.get(j));
/*  709 */         localIIOMetadataNode5.setAttribute("value", (String)this.tEXt_text.get(j));
/*      */ 
/*  711 */         localIIOMetadataNode3.appendChild(localIIOMetadataNode5);
/*      */       }
/*      */ 
/*  714 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  718 */     if (this.tIME_present) {
/*  719 */       localIIOMetadataNode3 = new IIOMetadataNode("tIME");
/*  720 */       localIIOMetadataNode3.setAttribute("year", Integer.toString(this.tIME_year));
/*  721 */       localIIOMetadataNode3.setAttribute("month", Integer.toString(this.tIME_month));
/*  722 */       localIIOMetadataNode3.setAttribute("day", Integer.toString(this.tIME_day));
/*  723 */       localIIOMetadataNode3.setAttribute("hour", Integer.toString(this.tIME_hour));
/*  724 */       localIIOMetadataNode3.setAttribute("minute", Integer.toString(this.tIME_minute));
/*  725 */       localIIOMetadataNode3.setAttribute("second", Integer.toString(this.tIME_second));
/*      */ 
/*  727 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  731 */     if (this.tRNS_present) {
/*  732 */       localIIOMetadataNode3 = new IIOMetadataNode("tRNS");
/*      */ 
/*  734 */       if (this.tRNS_colorType == 3) {
/*  735 */         localIIOMetadataNode1 = new IIOMetadataNode("tRNS_Palette");
/*      */ 
/*  737 */         for (j = 0; j < this.tRNS_alpha.length; j++) {
/*  738 */           localIIOMetadataNode5 = new IIOMetadataNode("tRNS_PaletteEntry");
/*      */ 
/*  740 */           localIIOMetadataNode5.setAttribute("index", Integer.toString(j));
/*  741 */           localIIOMetadataNode5.setAttribute("alpha", Integer.toString(this.tRNS_alpha[j] & 0xFF));
/*      */ 
/*  743 */           localIIOMetadataNode1.appendChild(localIIOMetadataNode5);
/*      */         }
/*  745 */       } else if (this.tRNS_colorType == 0) {
/*  746 */         localIIOMetadataNode1 = new IIOMetadataNode("tRNS_Grayscale");
/*  747 */         localIIOMetadataNode1.setAttribute("gray", Integer.toString(this.tRNS_gray));
/*  748 */       } else if (this.tRNS_colorType == 2) {
/*  749 */         localIIOMetadataNode1 = new IIOMetadataNode("tRNS_RGB");
/*  750 */         localIIOMetadataNode1.setAttribute("red", Integer.toString(this.tRNS_red));
/*  751 */         localIIOMetadataNode1.setAttribute("green", Integer.toString(this.tRNS_green));
/*  752 */         localIIOMetadataNode1.setAttribute("blue", Integer.toString(this.tRNS_blue));
/*      */       }
/*  754 */       localIIOMetadataNode3.appendChild(localIIOMetadataNode1);
/*      */ 
/*  756 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  760 */     if (this.zTXt_keyword.size() > 0) {
/*  761 */       localIIOMetadataNode3 = new IIOMetadataNode("zTXt");
/*  762 */       for (j = 0; j < this.zTXt_keyword.size(); j++) {
/*  763 */         localIIOMetadataNode5 = new IIOMetadataNode("zTXtEntry");
/*  764 */         localIIOMetadataNode5.setAttribute("keyword", (String)this.zTXt_keyword.get(j));
/*      */ 
/*  766 */         int n = ((Integer)this.zTXt_compressionMethod.get(j)).intValue();
/*  767 */         localIIOMetadataNode5.setAttribute("compressionMethod", zTXt_compressionMethodNames[n]);
/*      */ 
/*  770 */         localIIOMetadataNode5.setAttribute("text", (String)this.zTXt_text.get(j));
/*      */ 
/*  772 */         localIIOMetadataNode3.appendChild(localIIOMetadataNode5);
/*      */       }
/*      */ 
/*  775 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  779 */     if (this.unknownChunkType.size() > 0) {
/*  780 */       localIIOMetadataNode3 = new IIOMetadataNode("UnknownChunks");
/*      */ 
/*  782 */       for (j = 0; j < this.unknownChunkType.size(); j++) {
/*  783 */         localIIOMetadataNode5 = new IIOMetadataNode("UnknownChunk");
/*      */ 
/*  785 */         localIIOMetadataNode5.setAttribute("type", (String)this.unknownChunkType.get(j));
/*      */ 
/*  787 */         localIIOMetadataNode5.setUserObject((byte[])this.unknownChunkData.get(j));
/*      */ 
/*  789 */         localIIOMetadataNode3.appendChild(localIIOMetadataNode5);
/*      */       }
/*      */ 
/*  792 */       localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */     }
/*      */ 
/*  795 */     return localIIOMetadataNode2;
/*      */   }
/*      */ 
/*      */   private int getNumChannels()
/*      */   {
/*  801 */     int i = IHDR_numChannels[this.IHDR_colorType];
/*  802 */     if ((this.IHDR_colorType == 3) && (this.tRNS_present) && (this.tRNS_colorType == this.IHDR_colorType))
/*      */     {
/*  804 */       i = 4;
/*      */     }
/*  806 */     return i;
/*      */   }
/*      */ 
/*      */   public IIOMetadataNode getStandardChromaNode() {
/*  810 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Chroma");
/*  811 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*      */ 
/*  813 */     localIIOMetadataNode2 = new IIOMetadataNode("ColorSpaceType");
/*  814 */     localIIOMetadataNode2.setAttribute("name", colorSpaceTypeNames[this.IHDR_colorType]);
/*  815 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  817 */     localIIOMetadataNode2 = new IIOMetadataNode("NumChannels");
/*  818 */     localIIOMetadataNode2.setAttribute("value", Integer.toString(getNumChannels()));
/*  819 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  821 */     if (this.gAMA_present) {
/*  822 */       localIIOMetadataNode2 = new IIOMetadataNode("Gamma");
/*  823 */       localIIOMetadataNode2.setAttribute("value", Float.toString(this.gAMA_gamma * 1.0E-005F));
/*  824 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/*  827 */     localIIOMetadataNode2 = new IIOMetadataNode("BlackIsZero");
/*  828 */     localIIOMetadataNode2.setAttribute("value", "TRUE");
/*  829 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     int i;
/*      */     int j;
/*  831 */     if (this.PLTE_present) {
/*  832 */       i = (this.tRNS_present) && (this.tRNS_colorType == 3) ? 1 : 0;
/*      */ 
/*  835 */       localIIOMetadataNode2 = new IIOMetadataNode("Palette");
/*  836 */       for (j = 0; j < this.PLTE_red.length; j++) {
/*  837 */         IIOMetadataNode localIIOMetadataNode3 = new IIOMetadataNode("PaletteEntry");
/*      */ 
/*  839 */         localIIOMetadataNode3.setAttribute("index", Integer.toString(j));
/*  840 */         localIIOMetadataNode3.setAttribute("red", Integer.toString(this.PLTE_red[j] & 0xFF));
/*      */ 
/*  842 */         localIIOMetadataNode3.setAttribute("green", Integer.toString(this.PLTE_green[j] & 0xFF));
/*      */ 
/*  844 */         localIIOMetadataNode3.setAttribute("blue", Integer.toString(this.PLTE_blue[j] & 0xFF));
/*      */ 
/*  846 */         if (i != 0) {
/*  847 */           int m = j < this.tRNS_alpha.length ? this.tRNS_alpha[j] & 0xFF : 255;
/*      */ 
/*  849 */           localIIOMetadataNode3.setAttribute("alpha", Integer.toString(m));
/*      */         }
/*  851 */         localIIOMetadataNode2.appendChild(localIIOMetadataNode3);
/*      */       }
/*  853 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/*  856 */     if (this.bKGD_present) {
/*  857 */       if (this.bKGD_colorType == 3) {
/*  858 */         localIIOMetadataNode2 = new IIOMetadataNode("BackgroundIndex");
/*  859 */         localIIOMetadataNode2.setAttribute("value", Integer.toString(this.bKGD_index));
/*      */       } else {
/*  861 */         localIIOMetadataNode2 = new IIOMetadataNode("BackgroundColor");
/*      */         int k;
/*  864 */         if (this.bKGD_colorType == 0) {
/*  865 */           i = j = k = this.bKGD_gray;
/*      */         } else {
/*  867 */           i = this.bKGD_red;
/*  868 */           j = this.bKGD_green;
/*  869 */           k = this.bKGD_blue;
/*      */         }
/*  871 */         localIIOMetadataNode2.setAttribute("red", Integer.toString(i));
/*  872 */         localIIOMetadataNode2.setAttribute("green", Integer.toString(j));
/*  873 */         localIIOMetadataNode2.setAttribute("blue", Integer.toString(k));
/*      */       }
/*  875 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/*  878 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public IIOMetadataNode getStandardCompressionNode() {
/*  882 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Compression");
/*  883 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*      */ 
/*  885 */     localIIOMetadataNode2 = new IIOMetadataNode("CompressionTypeName");
/*  886 */     localIIOMetadataNode2.setAttribute("value", "deflate");
/*  887 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  889 */     localIIOMetadataNode2 = new IIOMetadataNode("Lossless");
/*  890 */     localIIOMetadataNode2.setAttribute("value", "TRUE");
/*  891 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  893 */     localIIOMetadataNode2 = new IIOMetadataNode("NumProgressiveScans");
/*  894 */     localIIOMetadataNode2.setAttribute("value", this.IHDR_interlaceMethod == 0 ? "1" : "7");
/*      */ 
/*  896 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  898 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   private String repeat(String paramString, int paramInt) {
/*  902 */     if (paramInt == 1) {
/*  903 */       return paramString;
/*      */     }
/*  905 */     StringBuffer localStringBuffer = new StringBuffer((paramString.length() + 1) * paramInt - 1);
/*  906 */     localStringBuffer.append(paramString);
/*  907 */     for (int i = 1; i < paramInt; i++) {
/*  908 */       localStringBuffer.append(" ");
/*  909 */       localStringBuffer.append(paramString);
/*      */     }
/*  911 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public IIOMetadataNode getStandardDataNode() {
/*  915 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Data");
/*  916 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*      */ 
/*  918 */     localIIOMetadataNode2 = new IIOMetadataNode("PlanarConfiguration");
/*  919 */     localIIOMetadataNode2.setAttribute("value", "PixelInterleaved");
/*  920 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  922 */     localIIOMetadataNode2 = new IIOMetadataNode("SampleFormat");
/*  923 */     localIIOMetadataNode2.setAttribute("value", this.IHDR_colorType == 3 ? "Index" : "UnsignedIntegral");
/*      */ 
/*  926 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  928 */     String str1 = Integer.toString(this.IHDR_bitDepth);
/*  929 */     localIIOMetadataNode2 = new IIOMetadataNode("BitsPerSample");
/*  930 */     localIIOMetadataNode2.setAttribute("value", repeat(str1, getNumChannels()));
/*  931 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  933 */     if (this.sBIT_present) {
/*  934 */       localIIOMetadataNode2 = new IIOMetadataNode("SignificantBitsPerSample");
/*      */       String str2;
/*  936 */       if ((this.sBIT_colorType == 0) || (this.sBIT_colorType == 4))
/*      */       {
/*  938 */         str2 = Integer.toString(this.sBIT_grayBits);
/*      */       }
/*      */       else {
/*  941 */         str2 = Integer.toString(this.sBIT_redBits) + " " + Integer.toString(this.sBIT_greenBits) + " " + Integer.toString(this.sBIT_blueBits);
/*      */       }
/*      */ 
/*  946 */       if ((this.sBIT_colorType == 4) || (this.sBIT_colorType == 6))
/*      */       {
/*  948 */         str2 = str2 + " " + Integer.toString(this.sBIT_alphaBits);
/*      */       }
/*      */ 
/*  951 */       localIIOMetadataNode2.setAttribute("value", str2);
/*  952 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/*  957 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public IIOMetadataNode getStandardDimensionNode() {
/*  961 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Dimension");
/*  962 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*      */ 
/*  964 */     localIIOMetadataNode2 = new IIOMetadataNode("PixelAspectRatio");
/*  965 */     float f = this.pHYs_present ? this.pHYs_pixelsPerUnitXAxis / this.pHYs_pixelsPerUnitYAxis : 1.0F;
/*      */ 
/*  967 */     localIIOMetadataNode2.setAttribute("value", Float.toString(f));
/*  968 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  970 */     localIIOMetadataNode2 = new IIOMetadataNode("ImageOrientation");
/*  971 */     localIIOMetadataNode2.setAttribute("value", "Normal");
/*  972 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  974 */     if ((this.pHYs_present) && (this.pHYs_unitSpecifier == 1)) {
/*  975 */       localIIOMetadataNode2 = new IIOMetadataNode("HorizontalPixelSize");
/*  976 */       localIIOMetadataNode2.setAttribute("value", Float.toString(1000.0F / this.pHYs_pixelsPerUnitXAxis));
/*      */ 
/*  978 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/*  980 */       localIIOMetadataNode2 = new IIOMetadataNode("VerticalPixelSize");
/*  981 */       localIIOMetadataNode2.setAttribute("value", Float.toString(1000.0F / this.pHYs_pixelsPerUnitYAxis));
/*      */ 
/*  983 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/*  986 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public IIOMetadataNode getStandardDocumentNode() {
/*  990 */     if (!this.tIME_present) {
/*  991 */       return null;
/*      */     }
/*      */ 
/*  994 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Document");
/*  995 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*      */ 
/*  997 */     localIIOMetadataNode2 = new IIOMetadataNode("ImageModificationTime");
/*  998 */     localIIOMetadataNode2.setAttribute("year", Integer.toString(this.tIME_year));
/*  999 */     localIIOMetadataNode2.setAttribute("month", Integer.toString(this.tIME_month));
/* 1000 */     localIIOMetadataNode2.setAttribute("day", Integer.toString(this.tIME_day));
/* 1001 */     localIIOMetadataNode2.setAttribute("hour", Integer.toString(this.tIME_hour));
/* 1002 */     localIIOMetadataNode2.setAttribute("minute", Integer.toString(this.tIME_minute));
/* 1003 */     localIIOMetadataNode2.setAttribute("second", Integer.toString(this.tIME_second));
/* 1004 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/* 1006 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public IIOMetadataNode getStandardTextNode() {
/* 1010 */     int i = this.tEXt_keyword.size() + this.iTXt_keyword.size() + this.zTXt_keyword.size();
/*      */ 
/* 1012 */     if (i == 0) {
/* 1013 */       return null;
/*      */     }
/*      */ 
/* 1016 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Text");
/* 1017 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*      */ 
/* 1019 */     for (int j = 0; j < this.tEXt_keyword.size(); j++) {
/* 1020 */       localIIOMetadataNode2 = new IIOMetadataNode("TextEntry");
/* 1021 */       localIIOMetadataNode2.setAttribute("keyword", (String)this.tEXt_keyword.get(j));
/* 1022 */       localIIOMetadataNode2.setAttribute("value", (String)this.tEXt_text.get(j));
/* 1023 */       localIIOMetadataNode2.setAttribute("encoding", "ISO-8859-1");
/* 1024 */       localIIOMetadataNode2.setAttribute("compression", "none");
/*      */ 
/* 1026 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/* 1029 */     for (j = 0; j < this.iTXt_keyword.size(); j++) {
/* 1030 */       localIIOMetadataNode2 = new IIOMetadataNode("TextEntry");
/* 1031 */       localIIOMetadataNode2.setAttribute("keyword", (String)this.iTXt_keyword.get(j));
/* 1032 */       localIIOMetadataNode2.setAttribute("value", (String)this.iTXt_text.get(j));
/* 1033 */       localIIOMetadataNode2.setAttribute("language", (String)this.iTXt_languageTag.get(j));
/*      */ 
/* 1035 */       if (((Boolean)this.iTXt_compressionFlag.get(j)).booleanValue())
/* 1036 */         localIIOMetadataNode2.setAttribute("compression", "zip");
/*      */       else {
/* 1038 */         localIIOMetadataNode2.setAttribute("compression", "none");
/*      */       }
/*      */ 
/* 1041 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/* 1044 */     for (j = 0; j < this.zTXt_keyword.size(); j++) {
/* 1045 */       localIIOMetadataNode2 = new IIOMetadataNode("TextEntry");
/* 1046 */       localIIOMetadataNode2.setAttribute("keyword", (String)this.zTXt_keyword.get(j));
/* 1047 */       localIIOMetadataNode2.setAttribute("value", (String)this.zTXt_text.get(j));
/* 1048 */       localIIOMetadataNode2.setAttribute("compression", "zip");
/*      */ 
/* 1050 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/* 1053 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   public IIOMetadataNode getStandardTransparencyNode() {
/* 1057 */     IIOMetadataNode localIIOMetadataNode1 = new IIOMetadataNode("Transparency");
/*      */ 
/* 1059 */     IIOMetadataNode localIIOMetadataNode2 = null;
/*      */ 
/* 1061 */     localIIOMetadataNode2 = new IIOMetadataNode("Alpha");
/* 1062 */     int i = (this.IHDR_colorType == 6) || (this.IHDR_colorType == 4) || ((this.IHDR_colorType == 3) && (this.tRNS_present) && (this.tRNS_colorType == this.IHDR_colorType) && (this.tRNS_alpha != null)) ? 1 : 0;
/*      */ 
/* 1069 */     localIIOMetadataNode2.setAttribute("value", i != 0 ? "nonpremultipled" : "none");
/* 1070 */     localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */ 
/* 1072 */     if (this.tRNS_present) {
/* 1073 */       localIIOMetadataNode2 = new IIOMetadataNode("TransparentColor");
/* 1074 */       if (this.tRNS_colorType == 2) {
/* 1075 */         localIIOMetadataNode2.setAttribute("value", Integer.toString(this.tRNS_red) + " " + Integer.toString(this.tRNS_green) + " " + Integer.toString(this.tRNS_blue));
/*      */       }
/* 1079 */       else if (this.tRNS_colorType == 0) {
/* 1080 */         localIIOMetadataNode2.setAttribute("value", Integer.toString(this.tRNS_gray));
/*      */       }
/* 1082 */       localIIOMetadataNode1.appendChild(localIIOMetadataNode2);
/*      */     }
/*      */ 
/* 1085 */     return localIIOMetadataNode1;
/*      */   }
/*      */ 
/*      */   private void fatal(Node paramNode, String paramString)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1091 */     throw new IIOInvalidTreeException(paramString, paramNode);
/*      */   }
/*      */ 
/*      */   private String getStringAttribute(Node paramNode, String paramString1, String paramString2, boolean paramBoolean)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1098 */     Node localNode = paramNode.getAttributes().getNamedItem(paramString1);
/* 1099 */     if (localNode == null) {
/* 1100 */       if (!paramBoolean) {
/* 1101 */         return paramString2;
/*      */       }
/* 1103 */       fatal(paramNode, "Required attribute " + paramString1 + " not present!");
/*      */     }
/*      */ 
/* 1106 */     return localNode.getNodeValue();
/*      */   }
/*      */ 
/*      */   private int getIntAttribute(Node paramNode, String paramString, int paramInt, boolean paramBoolean)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1114 */     String str = getStringAttribute(paramNode, paramString, null, paramBoolean);
/* 1115 */     if (str == null) {
/* 1116 */       return paramInt;
/*      */     }
/* 1118 */     return Integer.parseInt(str);
/*      */   }
/*      */ 
/*      */   private float getFloatAttribute(Node paramNode, String paramString, float paramFloat, boolean paramBoolean)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1125 */     String str = getStringAttribute(paramNode, paramString, null, paramBoolean);
/* 1126 */     if (str == null) {
/* 1127 */       return paramFloat;
/*      */     }
/* 1129 */     return Float.parseFloat(str);
/*      */   }
/*      */ 
/*      */   private int getIntAttribute(Node paramNode, String paramString)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1135 */     return getIntAttribute(paramNode, paramString, -1, true);
/*      */   }
/*      */ 
/*      */   private float getFloatAttribute(Node paramNode, String paramString)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1141 */     return getFloatAttribute(paramNode, paramString, -1.0F, true);
/*      */   }
/*      */ 
/*      */   private boolean getBooleanAttribute(Node paramNode, String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1149 */     Node localNode = paramNode.getAttributes().getNamedItem(paramString);
/* 1150 */     if (localNode == null) {
/* 1151 */       if (!paramBoolean2) {
/* 1152 */         return paramBoolean1;
/*      */       }
/* 1154 */       fatal(paramNode, "Required attribute " + paramString + " not present!");
/*      */     }
/*      */ 
/* 1157 */     String str = localNode.getNodeValue();
/*      */ 
/* 1159 */     if ((str.equals("TRUE")) || (str.equals("true")))
/* 1160 */       return true;
/* 1161 */     if ((str.equals("FALSE")) || (str.equals("false"))) {
/* 1162 */       return false;
/*      */     }
/* 1164 */     fatal(paramNode, "Attribute " + paramString + " must be 'TRUE' or 'FALSE'!");
/* 1165 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean getBooleanAttribute(Node paramNode, String paramString)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1172 */     return getBooleanAttribute(paramNode, paramString, false, true);
/*      */   }
/*      */ 
/*      */   private int getEnumeratedAttribute(Node paramNode, String paramString, String[] paramArrayOfString, int paramInt, boolean paramBoolean)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1180 */     Node localNode = paramNode.getAttributes().getNamedItem(paramString);
/* 1181 */     if (localNode == null) {
/* 1182 */       if (!paramBoolean) {
/* 1183 */         return paramInt;
/*      */       }
/* 1185 */       fatal(paramNode, "Required attribute " + paramString + " not present!");
/*      */     }
/*      */ 
/* 1188 */     String str = localNode.getNodeValue();
/* 1189 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 1190 */       if (str.equals(paramArrayOfString[i])) {
/* 1191 */         return i;
/*      */       }
/*      */     }
/*      */ 
/* 1195 */     fatal(paramNode, "Illegal value for attribute " + paramString + "!");
/* 1196 */     return -1;
/*      */   }
/*      */ 
/*      */   private int getEnumeratedAttribute(Node paramNode, String paramString, String[] paramArrayOfString)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1203 */     return getEnumeratedAttribute(paramNode, paramString, paramArrayOfString, -1, true);
/*      */   }
/*      */ 
/*      */   private String getAttribute(Node paramNode, String paramString1, String paramString2, boolean paramBoolean)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1210 */     Node localNode = paramNode.getAttributes().getNamedItem(paramString1);
/* 1211 */     if (localNode == null) {
/* 1212 */       if (!paramBoolean) {
/* 1213 */         return paramString2;
/*      */       }
/* 1215 */       fatal(paramNode, "Required attribute " + paramString1 + " not present!");
/*      */     }
/*      */ 
/* 1218 */     return localNode.getNodeValue();
/*      */   }
/*      */ 
/*      */   private String getAttribute(Node paramNode, String paramString)
/*      */     throws IIOInvalidTreeException
/*      */   {
/* 1224 */     return getAttribute(paramNode, paramString, null, true);
/*      */   }
/*      */ 
/*      */   public void mergeTree(String paramString, Node paramNode) throws IIOInvalidTreeException
/*      */   {
/* 1229 */     if (paramString.equals("javax_imageio_png_1.0")) {
/* 1230 */       if (paramNode == null) {
/* 1231 */         throw new IllegalArgumentException("root == null!");
/*      */       }
/* 1233 */       mergeNativeTree(paramNode);
/* 1234 */     } else if (paramString.equals("javax_imageio_1.0"))
/*      */     {
/* 1236 */       if (paramNode == null) {
/* 1237 */         throw new IllegalArgumentException("root == null!");
/*      */       }
/* 1239 */       mergeStandardTree(paramNode);
/*      */     } else {
/* 1241 */       throw new IllegalArgumentException("Not a recognized format!");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void mergeNativeTree(Node paramNode) throws IIOInvalidTreeException
/*      */   {
/* 1247 */     Node localNode = paramNode;
/* 1248 */     if (!localNode.getNodeName().equals("javax_imageio_png_1.0")) {
/* 1249 */       fatal(localNode, "Root must be javax_imageio_png_1.0");
/*      */     }
/*      */ 
/* 1252 */     localNode = localNode.getFirstChild();
/* 1253 */     while (localNode != null) {
/* 1254 */       String str1 = localNode.getNodeName();
/*      */ 
/* 1256 */       if (str1.equals("IHDR")) {
/* 1257 */         this.IHDR_width = getIntAttribute(localNode, "width");
/* 1258 */         this.IHDR_height = getIntAttribute(localNode, "height");
/* 1259 */         this.IHDR_bitDepth = Integer.valueOf(IHDR_bitDepths[getEnumeratedAttribute(localNode, "bitDepth", IHDR_bitDepths)]).intValue();
/*      */ 
/* 1264 */         this.IHDR_colorType = getEnumeratedAttribute(localNode, "colorType", IHDR_colorTypeNames);
/*      */ 
/* 1266 */         this.IHDR_compressionMethod = getEnumeratedAttribute(localNode, "compressionMethod", IHDR_compressionMethodNames);
/*      */ 
/* 1269 */         this.IHDR_filterMethod = getEnumeratedAttribute(localNode, "filterMethod", IHDR_filterMethodNames);
/*      */ 
/* 1273 */         this.IHDR_interlaceMethod = getEnumeratedAttribute(localNode, "interlaceMethod", IHDR_interlaceMethodNames);
/*      */ 
/* 1276 */         this.IHDR_present = true;
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject1;
/*      */         Object localObject2;
/*      */         Object localObject4;
/*      */         int k;
/*      */         Object localObject8;
/* 1277 */         if (str1.equals("PLTE")) {
/* 1278 */           localObject1 = new byte[256];
/* 1279 */           localObject2 = new byte[256];
/* 1280 */           localObject4 = new byte[256];
/* 1281 */           k = -1;
/*      */ 
/* 1283 */           localObject8 = localNode.getFirstChild();
/* 1284 */           if (localObject8 == null) {
/* 1285 */             fatal(localNode, "Palette has no entries!");
/*      */           }
/*      */ 
/* 1288 */           while (localObject8 != null) {
/* 1289 */             if (!((Node)localObject8).getNodeName().equals("PLTEEntry")) {
/* 1290 */               fatal(localNode, "Only a PLTEEntry may be a child of a PLTE!");
/*      */             }
/*      */ 
/* 1294 */             n = getIntAttribute((Node)localObject8, "index");
/* 1295 */             if ((n < 0) || (n > 255)) {
/* 1296 */               fatal(localNode, "Bad value for PLTEEntry attribute index!");
/*      */             }
/*      */ 
/* 1299 */             if (n > k) {
/* 1300 */               k = n;
/*      */             }
/* 1302 */             localObject1[n] = ((byte)getIntAttribute((Node)localObject8, "red"));
/*      */ 
/* 1304 */             localObject2[n] = ((byte)getIntAttribute((Node)localObject8, "green"));
/*      */ 
/* 1306 */             localObject4[n] = ((byte)getIntAttribute((Node)localObject8, "blue"));
/*      */ 
/* 1309 */             localObject8 = ((Node)localObject8).getNextSibling();
/*      */           }
/*      */ 
/* 1312 */           int n = k + 1;
/* 1313 */           this.PLTE_red = new byte[n];
/* 1314 */           this.PLTE_green = new byte[n];
/* 1315 */           this.PLTE_blue = new byte[n];
/* 1316 */           System.arraycopy(localObject1, 0, this.PLTE_red, 0, n);
/* 1317 */           System.arraycopy(localObject2, 0, this.PLTE_green, 0, n);
/* 1318 */           System.arraycopy(localObject4, 0, this.PLTE_blue, 0, n);
/* 1319 */           this.PLTE_present = true;
/* 1320 */         } else if (str1.equals("bKGD")) {
/* 1321 */           this.bKGD_present = false;
/* 1322 */           localObject1 = localNode.getFirstChild();
/* 1323 */           if (localObject1 == null) {
/* 1324 */             fatal(localNode, "bKGD node has no children!");
/*      */           }
/* 1326 */           localObject2 = ((Node)localObject1).getNodeName();
/* 1327 */           if (((String)localObject2).equals("bKGD_Palette")) {
/* 1328 */             this.bKGD_index = getIntAttribute((Node)localObject1, "index");
/* 1329 */             this.bKGD_colorType = 3;
/* 1330 */           } else if (((String)localObject2).equals("bKGD_Grayscale")) {
/* 1331 */             this.bKGD_gray = getIntAttribute((Node)localObject1, "gray");
/* 1332 */             this.bKGD_colorType = 0;
/* 1333 */           } else if (((String)localObject2).equals("bKGD_RGB")) {
/* 1334 */             this.bKGD_red = getIntAttribute((Node)localObject1, "red");
/* 1335 */             this.bKGD_green = getIntAttribute((Node)localObject1, "green");
/* 1336 */             this.bKGD_blue = getIntAttribute((Node)localObject1, "blue");
/* 1337 */             this.bKGD_colorType = 2;
/*      */           } else {
/* 1339 */             fatal(localNode, "Bad child of a bKGD node!");
/*      */           }
/* 1341 */           if (((Node)localObject1).getNextSibling() != null) {
/* 1342 */             fatal(localNode, "bKGD node has more than one child!");
/*      */           }
/*      */ 
/* 1345 */           this.bKGD_present = true;
/* 1346 */         } else if (str1.equals("cHRM")) {
/* 1347 */           this.cHRM_whitePointX = getIntAttribute(localNode, "whitePointX");
/* 1348 */           this.cHRM_whitePointY = getIntAttribute(localNode, "whitePointY");
/* 1349 */           this.cHRM_redX = getIntAttribute(localNode, "redX");
/* 1350 */           this.cHRM_redY = getIntAttribute(localNode, "redY");
/* 1351 */           this.cHRM_greenX = getIntAttribute(localNode, "greenX");
/* 1352 */           this.cHRM_greenY = getIntAttribute(localNode, "greenY");
/* 1353 */           this.cHRM_blueX = getIntAttribute(localNode, "blueX");
/* 1354 */           this.cHRM_blueY = getIntAttribute(localNode, "blueY");
/*      */ 
/* 1356 */           this.cHRM_present = true;
/* 1357 */         } else if (str1.equals("gAMA")) {
/* 1358 */           this.gAMA_gamma = getIntAttribute(localNode, "value");
/* 1359 */           this.gAMA_present = true;
/* 1360 */         } else if (str1.equals("hIST")) {
/* 1361 */           localObject1 = new char[256];
/* 1362 */           int i = -1;
/*      */ 
/* 1364 */           localObject4 = localNode.getFirstChild();
/* 1365 */           if (localObject4 == null) {
/* 1366 */             fatal(localNode, "hIST node has no children!");
/*      */           }
/*      */ 
/* 1369 */           while (localObject4 != null) {
/* 1370 */             if (!((Node)localObject4).getNodeName().equals("hISTEntry")) {
/* 1371 */               fatal(localNode, "Only a hISTEntry may be a child of a hIST!");
/*      */             }
/*      */ 
/* 1375 */             k = getIntAttribute((Node)localObject4, "index");
/* 1376 */             if ((k < 0) || (k > 255)) {
/* 1377 */               fatal(localNode, "Bad value for histEntry attribute index!");
/*      */             }
/*      */ 
/* 1380 */             if (k > i) {
/* 1381 */               i = k;
/*      */             }
/* 1383 */             localObject1[k] = ((char)getIntAttribute((Node)localObject4, "value"));
/*      */ 
/* 1386 */             localObject4 = ((Node)localObject4).getNextSibling();
/*      */           }
/*      */ 
/* 1389 */           k = i + 1;
/* 1390 */           this.hIST_histogram = new char[k];
/* 1391 */           System.arraycopy(localObject1, 0, this.hIST_histogram, 0, k);
/*      */ 
/* 1393 */           this.hIST_present = true;
/* 1394 */         } else if (str1.equals("iCCP")) {
/* 1395 */           this.iCCP_profileName = getAttribute(localNode, "profileName");
/* 1396 */           this.iCCP_compressionMethod = getEnumeratedAttribute(localNode, "compressionMethod", iCCP_compressionMethodNames);
/*      */ 
/* 1399 */           localObject1 = ((IIOMetadataNode)localNode).getUserObject();
/*      */ 
/* 1401 */           if (localObject1 == null) {
/* 1402 */             fatal(localNode, "No ICCP profile present in user object!");
/*      */           }
/* 1404 */           if (!(localObject1 instanceof byte[])) {
/* 1405 */             fatal(localNode, "User object not a byte array!");
/*      */           }
/*      */ 
/* 1408 */           this.iCCP_compressedProfile = ((byte[])((byte[])localObject1).clone());
/*      */ 
/* 1411 */           this.iCCP_present = true;
/*      */         }
/*      */         else
/*      */         {
/*      */           Object localObject3;
/*      */           Object localObject7;
/*      */           Object localObject9;
/* 1412 */           if (str1.equals("iTXt")) {
/* 1413 */             localObject1 = localNode.getFirstChild();
/* 1414 */             while (localObject1 != null) {
/* 1415 */               if (!((Node)localObject1).getNodeName().equals("iTXtEntry")) {
/* 1416 */                 fatal(localNode, "Only an iTXtEntry may be a child of an iTXt!");
/*      */               }
/*      */ 
/* 1420 */               localObject3 = getAttribute((Node)localObject1, "keyword");
/* 1421 */               if (isValidKeyword((String)localObject3)) {
/* 1422 */                 this.iTXt_keyword.add(localObject3);
/*      */ 
/* 1424 */                 boolean bool = getBooleanAttribute((Node)localObject1, "compressionFlag");
/*      */ 
/* 1426 */                 this.iTXt_compressionFlag.add(Boolean.valueOf(bool));
/*      */ 
/* 1428 */                 localObject7 = getAttribute((Node)localObject1, "compressionMethod");
/*      */ 
/* 1430 */                 this.iTXt_compressionMethod.add(Integer.valueOf((String)localObject7));
/*      */ 
/* 1432 */                 localObject8 = getAttribute((Node)localObject1, "languageTag");
/*      */ 
/* 1434 */                 this.iTXt_languageTag.add(localObject8);
/*      */ 
/* 1436 */                 String str3 = getAttribute((Node)localObject1, "translatedKeyword");
/*      */ 
/* 1438 */                 this.iTXt_translatedKeyword.add(str3);
/*      */ 
/* 1440 */                 localObject9 = getAttribute((Node)localObject1, "text");
/* 1441 */                 this.iTXt_text.add(localObject9);
/*      */               }
/*      */ 
/* 1446 */               localObject1 = ((Node)localObject1).getNextSibling();
/*      */             }
/* 1448 */           } else if (str1.equals("pHYs")) {
/* 1449 */             this.pHYs_pixelsPerUnitXAxis = getIntAttribute(localNode, "pixelsPerUnitXAxis");
/*      */ 
/* 1451 */             this.pHYs_pixelsPerUnitYAxis = getIntAttribute(localNode, "pixelsPerUnitYAxis");
/*      */ 
/* 1453 */             this.pHYs_unitSpecifier = getEnumeratedAttribute(localNode, "unitSpecifier", unitSpecifierNames);
/*      */ 
/* 1457 */             this.pHYs_present = true;
/* 1458 */           } else if (str1.equals("sBIT")) {
/* 1459 */             this.sBIT_present = false;
/* 1460 */             localObject1 = localNode.getFirstChild();
/* 1461 */             if (localObject1 == null) {
/* 1462 */               fatal(localNode, "sBIT node has no children!");
/*      */             }
/* 1464 */             localObject3 = ((Node)localObject1).getNodeName();
/* 1465 */             if (((String)localObject3).equals("sBIT_Grayscale")) {
/* 1466 */               this.sBIT_grayBits = getIntAttribute((Node)localObject1, "gray");
/* 1467 */               this.sBIT_colorType = 0;
/* 1468 */             } else if (((String)localObject3).equals("sBIT_GrayAlpha")) {
/* 1469 */               this.sBIT_grayBits = getIntAttribute((Node)localObject1, "gray");
/* 1470 */               this.sBIT_alphaBits = getIntAttribute((Node)localObject1, "alpha");
/* 1471 */               this.sBIT_colorType = 4;
/* 1472 */             } else if (((String)localObject3).equals("sBIT_RGB")) {
/* 1473 */               this.sBIT_redBits = getIntAttribute((Node)localObject1, "red");
/* 1474 */               this.sBIT_greenBits = getIntAttribute((Node)localObject1, "green");
/* 1475 */               this.sBIT_blueBits = getIntAttribute((Node)localObject1, "blue");
/* 1476 */               this.sBIT_colorType = 2;
/* 1477 */             } else if (((String)localObject3).equals("sBIT_RGBAlpha")) {
/* 1478 */               this.sBIT_redBits = getIntAttribute((Node)localObject1, "red");
/* 1479 */               this.sBIT_greenBits = getIntAttribute((Node)localObject1, "green");
/* 1480 */               this.sBIT_blueBits = getIntAttribute((Node)localObject1, "blue");
/* 1481 */               this.sBIT_alphaBits = getIntAttribute((Node)localObject1, "alpha");
/* 1482 */               this.sBIT_colorType = 6;
/* 1483 */             } else if (((String)localObject3).equals("sBIT_Palette")) {
/* 1484 */               this.sBIT_redBits = getIntAttribute((Node)localObject1, "red");
/* 1485 */               this.sBIT_greenBits = getIntAttribute((Node)localObject1, "green");
/* 1486 */               this.sBIT_blueBits = getIntAttribute((Node)localObject1, "blue");
/* 1487 */               this.sBIT_colorType = 3;
/*      */             } else {
/* 1489 */               fatal(localNode, "Bad child of an sBIT node!");
/*      */             }
/* 1491 */             if (((Node)localObject1).getNextSibling() != null) {
/* 1492 */               fatal(localNode, "sBIT node has more than one child!");
/*      */             }
/*      */ 
/* 1495 */             this.sBIT_present = true;
/*      */           }
/*      */           else
/*      */           {
/*      */             Object localObject5;
/*      */             int i1;
/* 1496 */             if (str1.equals("sPLT")) {
/* 1497 */               this.sPLT_paletteName = getAttribute(localNode, "name");
/* 1498 */               this.sPLT_sampleDepth = getIntAttribute(localNode, "sampleDepth");
/*      */ 
/* 1500 */               localObject1 = new int[256];
/* 1501 */               localObject3 = new int[256];
/* 1502 */               localObject5 = new int[256];
/* 1503 */               localObject7 = new int[256];
/* 1504 */               localObject8 = new int[256];
/* 1505 */               i1 = -1;
/*      */ 
/* 1507 */               localObject9 = localNode.getFirstChild();
/* 1508 */               if (localObject9 == null) {
/* 1509 */                 fatal(localNode, "sPLT node has no children!");
/*      */               }
/*      */ 
/* 1512 */               while (localObject9 != null) {
/* 1513 */                 if (!((Node)localObject9).getNodeName().equals("sPLTEntry")) {
/* 1514 */                   fatal(localNode, "Only an sPLTEntry may be a child of an sPLT!");
/*      */                 }
/*      */ 
/* 1518 */                 i2 = getIntAttribute((Node)localObject9, "index");
/* 1519 */                 if ((i2 < 0) || (i2 > 255)) {
/* 1520 */                   fatal(localNode, "Bad value for PLTEEntry attribute index!");
/*      */                 }
/*      */ 
/* 1523 */                 if (i2 > i1) {
/* 1524 */                   i1 = i2;
/*      */                 }
/* 1526 */                 localObject1[i2] = getIntAttribute((Node)localObject9, "red");
/* 1527 */                 localObject3[i2] = getIntAttribute((Node)localObject9, "green");
/* 1528 */                 localObject5[i2] = getIntAttribute((Node)localObject9, "blue");
/* 1529 */                 localObject7[i2] = getIntAttribute((Node)localObject9, "alpha");
/* 1530 */                 localObject8[i2] = getIntAttribute((Node)localObject9, "frequency");
/*      */ 
/* 1533 */                 localObject9 = ((Node)localObject9).getNextSibling();
/*      */               }
/*      */ 
/* 1536 */               int i2 = i1 + 1;
/* 1537 */               this.sPLT_red = new int[i2];
/* 1538 */               this.sPLT_green = new int[i2];
/* 1539 */               this.sPLT_blue = new int[i2];
/* 1540 */               this.sPLT_alpha = new int[i2];
/* 1541 */               this.sPLT_frequency = new int[i2];
/* 1542 */               System.arraycopy(localObject1, 0, this.sPLT_red, 0, i2);
/* 1543 */               System.arraycopy(localObject3, 0, this.sPLT_green, 0, i2);
/* 1544 */               System.arraycopy(localObject5, 0, this.sPLT_blue, 0, i2);
/* 1545 */               System.arraycopy(localObject7, 0, this.sPLT_alpha, 0, i2);
/* 1546 */               System.arraycopy(localObject8, 0, this.sPLT_frequency, 0, i2);
/*      */ 
/* 1549 */               this.sPLT_present = true;
/* 1550 */             } else if (str1.equals("sRGB")) {
/* 1551 */               this.sRGB_renderingIntent = getEnumeratedAttribute(localNode, "renderingIntent", renderingIntentNames);
/*      */ 
/* 1555 */               this.sRGB_present = true;
/* 1556 */             } else if (str1.equals("tEXt")) {
/* 1557 */               localObject1 = localNode.getFirstChild();
/* 1558 */               while (localObject1 != null) {
/* 1559 */                 if (!((Node)localObject1).getNodeName().equals("tEXtEntry")) {
/* 1560 */                   fatal(localNode, "Only an tEXtEntry may be a child of an tEXt!");
/*      */                 }
/*      */ 
/* 1564 */                 localObject3 = getAttribute((Node)localObject1, "keyword");
/* 1565 */                 this.tEXt_keyword.add(localObject3);
/*      */ 
/* 1567 */                 localObject5 = getAttribute((Node)localObject1, "value");
/* 1568 */                 this.tEXt_text.add(localObject5);
/*      */ 
/* 1570 */                 localObject1 = ((Node)localObject1).getNextSibling();
/*      */               }
/* 1572 */             } else if (str1.equals("tIME")) {
/* 1573 */               this.tIME_year = getIntAttribute(localNode, "year");
/* 1574 */               this.tIME_month = getIntAttribute(localNode, "month");
/* 1575 */               this.tIME_day = getIntAttribute(localNode, "day");
/* 1576 */               this.tIME_hour = getIntAttribute(localNode, "hour");
/* 1577 */               this.tIME_minute = getIntAttribute(localNode, "minute");
/* 1578 */               this.tIME_second = getIntAttribute(localNode, "second");
/*      */ 
/* 1580 */               this.tIME_present = true;
/* 1581 */             } else if (str1.equals("tRNS")) {
/* 1582 */               this.tRNS_present = false;
/* 1583 */               localObject1 = localNode.getFirstChild();
/* 1584 */               if (localObject1 == null) {
/* 1585 */                 fatal(localNode, "tRNS node has no children!");
/*      */               }
/* 1587 */               localObject3 = ((Node)localObject1).getNodeName();
/* 1588 */               if (((String)localObject3).equals("tRNS_Palette")) {
/* 1589 */                 localObject5 = new byte[256];
/* 1590 */                 int m = -1;
/*      */ 
/* 1592 */                 localObject8 = ((Node)localObject1).getFirstChild();
/* 1593 */                 if (localObject8 == null) {
/* 1594 */                   fatal(localNode, "tRNS_Palette node has no children!");
/*      */                 }
/* 1596 */                 while (localObject8 != null) {
/* 1597 */                   if (!((Node)localObject8).getNodeName().equals("tRNS_PaletteEntry"))
/*      */                   {
/* 1599 */                     fatal(localNode, "Only a tRNS_PaletteEntry may be a child of a tRNS_Palette!");
/*      */                   }
/*      */ 
/* 1602 */                   i1 = getIntAttribute((Node)localObject8, "index");
/*      */ 
/* 1604 */                   if ((i1 < 0) || (i1 > 255)) {
/* 1605 */                     fatal(localNode, "Bad value for tRNS_PaletteEntry attribute index!");
/*      */                   }
/*      */ 
/* 1608 */                   if (i1 > m) {
/* 1609 */                     m = i1;
/*      */                   }
/* 1611 */                   localObject5[i1] = ((byte)getIntAttribute((Node)localObject8, "alpha"));
/*      */ 
/* 1615 */                   localObject8 = ((Node)localObject8).getNextSibling();
/*      */                 }
/*      */ 
/* 1619 */                 i1 = m + 1;
/* 1620 */                 this.tRNS_alpha = new byte[i1];
/* 1621 */                 this.tRNS_colorType = 3;
/* 1622 */                 System.arraycopy(localObject5, 0, this.tRNS_alpha, 0, i1);
/* 1623 */               } else if (((String)localObject3).equals("tRNS_Grayscale")) {
/* 1624 */                 this.tRNS_gray = getIntAttribute((Node)localObject1, "gray");
/* 1625 */                 this.tRNS_colorType = 0;
/* 1626 */               } else if (((String)localObject3).equals("tRNS_RGB")) {
/* 1627 */                 this.tRNS_red = getIntAttribute((Node)localObject1, "red");
/* 1628 */                 this.tRNS_green = getIntAttribute((Node)localObject1, "green");
/* 1629 */                 this.tRNS_blue = getIntAttribute((Node)localObject1, "blue");
/* 1630 */                 this.tRNS_colorType = 2;
/*      */               } else {
/* 1632 */                 fatal(localNode, "Bad child of a tRNS node!");
/*      */               }
/* 1634 */               if (((Node)localObject1).getNextSibling() != null) {
/* 1635 */                 fatal(localNode, "tRNS node has more than one child!");
/*      */               }
/*      */ 
/* 1638 */               this.tRNS_present = true;
/* 1639 */             } else if (str1.equals("zTXt")) {
/* 1640 */               localObject1 = localNode.getFirstChild();
/* 1641 */               while (localObject1 != null) {
/* 1642 */                 if (!((Node)localObject1).getNodeName().equals("zTXtEntry")) {
/* 1643 */                   fatal(localNode, "Only an zTXtEntry may be a child of an zTXt!");
/*      */                 }
/*      */ 
/* 1647 */                 localObject3 = getAttribute((Node)localObject1, "keyword");
/* 1648 */                 this.zTXt_keyword.add(localObject3);
/*      */ 
/* 1650 */                 int j = getEnumeratedAttribute((Node)localObject1, "compressionMethod", zTXt_compressionMethodNames);
/*      */ 
/* 1653 */                 this.zTXt_compressionMethod.add(new Integer(j));
/*      */ 
/* 1655 */                 String str2 = getAttribute((Node)localObject1, "text");
/* 1656 */                 this.zTXt_text.add(str2);
/*      */ 
/* 1658 */                 localObject1 = ((Node)localObject1).getNextSibling();
/*      */               }
/* 1660 */             } else if (str1.equals("UnknownChunks")) {
/* 1661 */               localObject1 = localNode.getFirstChild();
/* 1662 */               while (localObject1 != null) {
/* 1663 */                 if (!((Node)localObject1).getNodeName().equals("UnknownChunk")) {
/* 1664 */                   fatal(localNode, "Only an UnknownChunk may be a child of an UnknownChunks!");
/*      */                 }
/*      */ 
/* 1667 */                 localObject3 = getAttribute((Node)localObject1, "type");
/* 1668 */                 Object localObject6 = ((IIOMetadataNode)localObject1).getUserObject();
/*      */ 
/* 1671 */                 if (((String)localObject3).length() != 4) {
/* 1672 */                   fatal((Node)localObject1, "Chunk type must be 4 characters!");
/*      */                 }
/*      */ 
/* 1675 */                 if (localObject6 == null) {
/* 1676 */                   fatal((Node)localObject1, "No chunk data present in user object!");
/*      */                 }
/*      */ 
/* 1679 */                 if (!(localObject6 instanceof byte[])) {
/* 1680 */                   fatal((Node)localObject1, "User object not a byte array!");
/*      */                 }
/*      */ 
/* 1683 */                 this.unknownChunkType.add(localObject3);
/* 1684 */                 this.unknownChunkData.add(((byte[])localObject6).clone());
/*      */ 
/* 1686 */                 localObject1 = ((Node)localObject1).getNextSibling();
/*      */               }
/*      */             } else {
/* 1689 */               fatal(localNode, "Unknown child of root node!");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1692 */       localNode = localNode.getNextSibling();
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isValidKeyword(String paramString)
/*      */   {
/* 1706 */     int i = paramString.length();
/* 1707 */     if ((i < 1) || (i >= 80)) {
/* 1708 */       return false;
/*      */     }
/* 1710 */     if ((paramString.startsWith(" ")) || (paramString.endsWith(" ")) || (paramString.contains("  "))) {
/* 1711 */       return false;
/*      */     }
/* 1713 */     return isISOLatin(paramString, false);
/*      */   }
/*      */ 
/*      */   private boolean isISOLatin(String paramString, boolean paramBoolean)
/*      */   {
/* 1726 */     int i = paramString.length();
/* 1727 */     for (int j = 0; j < i; j++) {
/* 1728 */       int k = paramString.charAt(j);
/* 1729 */       if ((k < 32) || (k > 255) || ((k > 126) && (k < 161)))
/*      */       {
/* 1732 */         if ((!paramBoolean) || (k != 16)) {
/* 1733 */           return false;
/*      */         }
/*      */       }
/*      */     }
/* 1737 */     return true;
/*      */   }
/*      */ 
/*      */   private void mergeStandardTree(Node paramNode) throws IIOInvalidTreeException
/*      */   {
/* 1742 */     Node localNode1 = paramNode;
/* 1743 */     if (!localNode1.getNodeName().equals("javax_imageio_1.0"))
/*      */     {
/* 1745 */       fatal(localNode1, "Root must be javax_imageio_1.0");
/*      */     }
/*      */ 
/* 1749 */     localNode1 = localNode1.getFirstChild();
/* 1750 */     while (localNode1 != null) {
/* 1751 */       String str1 = localNode1.getNodeName();
/*      */       Node localNode2;
/*      */       String str2;
/*      */       int i2;
/*      */       Node localNode4;
/*      */       int k;
/*      */       int i1;
/* 1753 */       if (str1.equals("Chroma")) {
/* 1754 */         localNode2 = localNode1.getFirstChild();
/* 1755 */         while (localNode2 != null) {
/* 1756 */           str2 = localNode2.getNodeName();
/* 1757 */           if (str2.equals("Gamma")) {
/* 1758 */             float f1 = getFloatAttribute(localNode2, "value");
/* 1759 */             this.gAMA_present = true;
/* 1760 */             this.gAMA_gamma = ((int)(f1 * 100000.0F + 0.5D));
/* 1761 */           } else if (str2.equals("Palette")) {
/* 1762 */             byte[] arrayOfByte1 = new byte[256];
/* 1763 */             byte[] arrayOfByte2 = new byte[256];
/* 1764 */             byte[] arrayOfByte3 = new byte[256];
/* 1765 */             i2 = -1;
/*      */ 
/* 1767 */             localNode4 = localNode2.getFirstChild();
/* 1768 */             while (localNode4 != null) {
/* 1769 */               i3 = getIntAttribute(localNode4, "index");
/* 1770 */               if ((i3 >= 0) && (i3 <= 255)) {
/* 1771 */                 arrayOfByte1[i3] = ((byte)getIntAttribute(localNode4, "red"));
/*      */ 
/* 1773 */                 arrayOfByte2[i3] = ((byte)getIntAttribute(localNode4, "green"));
/*      */ 
/* 1775 */                 arrayOfByte3[i3] = ((byte)getIntAttribute(localNode4, "blue"));
/*      */ 
/* 1777 */                 if (i3 > i2) {
/* 1778 */                   i2 = i3;
/*      */                 }
/*      */               }
/* 1781 */               localNode4 = localNode4.getNextSibling();
/*      */             }
/*      */ 
/* 1784 */             int i3 = i2 + 1;
/* 1785 */             this.PLTE_red = new byte[i3];
/* 1786 */             this.PLTE_green = new byte[i3];
/* 1787 */             this.PLTE_blue = new byte[i3];
/* 1788 */             System.arraycopy(arrayOfByte1, 0, this.PLTE_red, 0, i3);
/* 1789 */             System.arraycopy(arrayOfByte2, 0, this.PLTE_green, 0, i3);
/* 1790 */             System.arraycopy(arrayOfByte3, 0, this.PLTE_blue, 0, i3);
/* 1791 */             this.PLTE_present = true;
/* 1792 */           } else if (str2.equals("BackgroundIndex")) {
/* 1793 */             this.bKGD_present = true;
/* 1794 */             this.bKGD_colorType = 3;
/* 1795 */             this.bKGD_index = getIntAttribute(localNode2, "value");
/* 1796 */           } else if (str2.equals("BackgroundColor")) {
/* 1797 */             k = getIntAttribute(localNode2, "red");
/* 1798 */             int n = getIntAttribute(localNode2, "green");
/* 1799 */             i1 = getIntAttribute(localNode2, "blue");
/* 1800 */             if ((k == n) && (k == i1)) {
/* 1801 */               this.bKGD_colorType = 0;
/* 1802 */               this.bKGD_gray = k;
/*      */             } else {
/* 1804 */               this.bKGD_red = k;
/* 1805 */               this.bKGD_green = n;
/* 1806 */               this.bKGD_blue = i1;
/*      */             }
/* 1808 */             this.bKGD_present = true;
/*      */           }
/*      */ 
/* 1813 */           localNode2 = localNode2.getNextSibling();
/*      */         }
/* 1815 */       } else if (str1.equals("Compression")) {
/* 1816 */         localNode2 = localNode1.getFirstChild();
/* 1817 */         while (localNode2 != null) {
/* 1818 */           str2 = localNode2.getNodeName();
/* 1819 */           if (str2.equals("NumProgressiveScans"))
/*      */           {
/* 1821 */             k = getIntAttribute(localNode2, "value");
/* 1822 */             this.IHDR_interlaceMethod = (k > 1 ? 1 : 0);
/*      */           }
/*      */ 
/* 1827 */           localNode2 = localNode2.getNextSibling();
/*      */         }
/* 1829 */       } else if (str1.equals("Data")) {
/* 1830 */         localNode2 = localNode1.getFirstChild();
/* 1831 */         while (localNode2 != null) {
/* 1832 */           str2 = localNode2.getNodeName();
/*      */           String str4;
/*      */           StringTokenizer localStringTokenizer;
/* 1833 */           if (str2.equals("BitsPerSample")) {
/* 1834 */             str4 = getAttribute(localNode2, "value");
/* 1835 */             localStringTokenizer = new StringTokenizer(str4);
/* 1836 */             i1 = -1;
/* 1837 */             while (localStringTokenizer.hasMoreTokens()) {
/* 1838 */               i2 = Integer.parseInt(localStringTokenizer.nextToken());
/* 1839 */               if (i2 > i1) {
/* 1840 */                 i1 = i2;
/*      */               }
/*      */             }
/* 1843 */             if (i1 < 1) {
/* 1844 */               i1 = 1;
/*      */             }
/* 1846 */             if (i1 == 3) i1 = 4;
/* 1847 */             if ((i1 > 4) || (i1 < 8)) {
/* 1848 */               i1 = 8;
/*      */             }
/* 1850 */             if (i1 > 8) {
/* 1851 */               i1 = 16;
/*      */             }
/* 1853 */             this.IHDR_bitDepth = i1;
/* 1854 */           } else if (str2.equals("SignificantBitsPerSample")) {
/* 1855 */             str4 = getAttribute(localNode2, "value");
/* 1856 */             localStringTokenizer = new StringTokenizer(str4);
/* 1857 */             i1 = localStringTokenizer.countTokens();
/* 1858 */             if (i1 == 1) {
/* 1859 */               this.sBIT_colorType = 0;
/* 1860 */               this.sBIT_grayBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1861 */             } else if (i1 == 2) {
/* 1862 */               this.sBIT_colorType = 4;
/*      */ 
/* 1864 */               this.sBIT_grayBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1865 */               this.sBIT_alphaBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1866 */             } else if (i1 == 3) {
/* 1867 */               this.sBIT_colorType = 2;
/* 1868 */               this.sBIT_redBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1869 */               this.sBIT_greenBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1870 */               this.sBIT_blueBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1871 */             } else if (i1 == 4) {
/* 1872 */               this.sBIT_colorType = 6;
/*      */ 
/* 1874 */               this.sBIT_redBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1875 */               this.sBIT_greenBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1876 */               this.sBIT_blueBits = Integer.parseInt(localStringTokenizer.nextToken());
/* 1877 */               this.sBIT_alphaBits = Integer.parseInt(localStringTokenizer.nextToken());
/*      */             }
/* 1879 */             if ((i1 >= 1) && (i1 <= 4)) {
/* 1880 */               this.sBIT_present = true;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1886 */           localNode2 = localNode2.getNextSibling();
/*      */         }
/* 1888 */       } else if (str1.equals("Dimension")) {
/* 1889 */         int i = 0;
/* 1890 */         int j = 0;
/* 1891 */         int m = 0;
/*      */ 
/* 1893 */         float f2 = -1.0F;
/* 1894 */         float f3 = -1.0F;
/* 1895 */         float f4 = -1.0F;
/*      */ 
/* 1897 */         localNode4 = localNode1.getFirstChild();
/* 1898 */         while (localNode4 != null) {
/* 1899 */           String str9 = localNode4.getNodeName();
/* 1900 */           if (str9.equals("PixelAspectRatio")) {
/* 1901 */             f4 = getFloatAttribute(localNode4, "value");
/* 1902 */             m = 1;
/* 1903 */           } else if (str9.equals("HorizontalPixelSize")) {
/* 1904 */             f2 = getFloatAttribute(localNode4, "value");
/* 1905 */             i = 1;
/* 1906 */           } else if (str9.equals("VerticalPixelSize")) {
/* 1907 */             f3 = getFloatAttribute(localNode4, "value");
/* 1908 */             j = 1;
/*      */           }
/*      */ 
/* 1919 */           localNode4 = localNode4.getNextSibling();
/*      */         }
/*      */ 
/* 1922 */         if ((i != 0) && (j != 0)) {
/* 1923 */           this.pHYs_present = true;
/* 1924 */           this.pHYs_unitSpecifier = 1;
/* 1925 */           this.pHYs_pixelsPerUnitXAxis = ((int)(f2 * 1000.0F + 0.5F));
/* 1926 */           this.pHYs_pixelsPerUnitYAxis = ((int)(f3 * 1000.0F + 0.5F));
/* 1927 */         } else if (m != 0) {
/* 1928 */           this.pHYs_present = true;
/* 1929 */           this.pHYs_unitSpecifier = 0;
/*      */ 
/* 1932 */           for (int i4 = 1; 
/* 1933 */             i4 < 100; i4++) {
/* 1934 */             int i5 = (int)(f4 * i4);
/* 1935 */             if (Math.abs(i5 / i4 - f4) < 0.001D) {
/*      */               break;
/*      */             }
/*      */           }
/* 1939 */           this.pHYs_pixelsPerUnitXAxis = ((int)(f4 * i4));
/* 1940 */           this.pHYs_pixelsPerUnitYAxis = i4;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         Node localNode3;
/*      */         String str3;
/* 1942 */         if (str1.equals("Document")) {
/* 1943 */           localNode3 = localNode1.getFirstChild();
/* 1944 */           while (localNode3 != null) {
/* 1945 */             str3 = localNode3.getNodeName();
/* 1946 */             if (str3.equals("ImageModificationTime")) {
/* 1947 */               this.tIME_present = true;
/* 1948 */               this.tIME_year = getIntAttribute(localNode3, "year");
/* 1949 */               this.tIME_month = getIntAttribute(localNode3, "month");
/* 1950 */               this.tIME_day = getIntAttribute(localNode3, "day");
/* 1951 */               this.tIME_hour = getIntAttribute(localNode3, "hour", 0, false);
/*      */ 
/* 1953 */               this.tIME_minute = getIntAttribute(localNode3, "minute", 0, false);
/*      */ 
/* 1955 */               this.tIME_second = getIntAttribute(localNode3, "second", 0, false);
/*      */             }
/*      */ 
/* 1960 */             localNode3 = localNode3.getNextSibling();
/*      */           }
/* 1962 */         } else if (str1.equals("Text")) {
/* 1963 */           localNode3 = localNode1.getFirstChild();
/* 1964 */           while (localNode3 != null) {
/* 1965 */             str3 = localNode3.getNodeName();
/* 1966 */             if (str3.equals("TextEntry")) {
/* 1967 */               String str5 = getAttribute(localNode3, "keyword", "", false);
/*      */ 
/* 1969 */               String str6 = getAttribute(localNode3, "value");
/* 1970 */               String str7 = getAttribute(localNode3, "language", "", false);
/*      */ 
/* 1972 */               String str8 = getAttribute(localNode3, "compression", "none", false);
/*      */ 
/* 1975 */               if (isValidKeyword(str5))
/*      */               {
/* 1977 */                 if (isISOLatin(str6, true)) {
/* 1978 */                   if (str8.equals("zip"))
/*      */                   {
/* 1980 */                     this.zTXt_keyword.add(str5);
/* 1981 */                     this.zTXt_text.add(str6);
/* 1982 */                     this.zTXt_compressionMethod.add(Integer.valueOf(0));
/*      */                   }
/*      */                   else {
/* 1985 */                     this.tEXt_keyword.add(str5);
/* 1986 */                     this.tEXt_text.add(str6);
/*      */                   }
/*      */                 }
/*      */                 else {
/* 1990 */                   this.iTXt_keyword.add(str5);
/* 1991 */                   this.iTXt_compressionFlag.add(Boolean.valueOf(str8.equals("zip")));
/* 1992 */                   this.iTXt_compressionMethod.add(Integer.valueOf(0));
/* 1993 */                   this.iTXt_languageTag.add(str7);
/* 1994 */                   this.iTXt_translatedKeyword.add(str5);
/* 1995 */                   this.iTXt_text.add(str6);
/*      */                 }
/*      */               }
/*      */             }
/* 1998 */             localNode3 = localNode3.getNextSibling();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2016 */       localNode1 = localNode1.getNextSibling();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/* 2022 */     this.IHDR_present = false;
/* 2023 */     this.PLTE_present = false;
/* 2024 */     this.bKGD_present = false;
/* 2025 */     this.cHRM_present = false;
/* 2026 */     this.gAMA_present = false;
/* 2027 */     this.hIST_present = false;
/* 2028 */     this.iCCP_present = false;
/* 2029 */     this.iTXt_keyword = new ArrayList();
/* 2030 */     this.iTXt_compressionFlag = new ArrayList();
/* 2031 */     this.iTXt_compressionMethod = new ArrayList();
/* 2032 */     this.iTXt_languageTag = new ArrayList();
/* 2033 */     this.iTXt_translatedKeyword = new ArrayList();
/* 2034 */     this.iTXt_text = new ArrayList();
/* 2035 */     this.pHYs_present = false;
/* 2036 */     this.sBIT_present = false;
/* 2037 */     this.sPLT_present = false;
/* 2038 */     this.sRGB_present = false;
/* 2039 */     this.tEXt_keyword = new ArrayList();
/* 2040 */     this.tEXt_text = new ArrayList();
/* 2041 */     this.tIME_present = false;
/* 2042 */     this.tRNS_present = false;
/* 2043 */     this.zTXt_keyword = new ArrayList();
/* 2044 */     this.zTXt_compressionMethod = new ArrayList();
/* 2045 */     this.zTXt_text = new ArrayList();
/* 2046 */     this.unknownChunkType = new ArrayList();
/* 2047 */     this.unknownChunkData = new ArrayList();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.PNGMetadata
 * JD-Core Version:    0.6.2
 */
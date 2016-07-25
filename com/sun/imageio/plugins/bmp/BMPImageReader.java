/*      */ package com.sun.imageio.plugins.bmp;
/*      */ 
/*      */ import com.sun.imageio.plugins.common.I18N;
/*      */ import com.sun.imageio.plugins.common.ImageUtil;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.color.ICC_ColorSpace;
/*      */ import java.awt.color.ICC_Profile;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ComponentSampleModel;
/*      */ import java.awt.image.DataBufferByte;
/*      */ import java.awt.image.DataBufferInt;
/*      */ import java.awt.image.DataBufferUShort;
/*      */ import java.awt.image.DirectColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.MultiPixelPackedSampleModel;
/*      */ import java.awt.image.PixelInterleavedSampleModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.SampleModel;
/*      */ import java.awt.image.SinglePixelPackedSampleModel;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteOrder;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import javax.imageio.ImageIO;
/*      */ import javax.imageio.ImageReadParam;
/*      */ import javax.imageio.ImageReader;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ import javax.imageio.event.IIOReadProgressListener;
/*      */ import javax.imageio.event.IIOReadUpdateListener;
/*      */ import javax.imageio.event.IIOReadWarningListener;
/*      */ import javax.imageio.metadata.IIOMetadata;
/*      */ import javax.imageio.spi.ImageReaderSpi;
/*      */ import javax.imageio.stream.ImageInputStream;
/*      */ 
/*      */ public class BMPImageReader extends ImageReader
/*      */   implements BMPConstants
/*      */ {
/*      */   private static final int VERSION_2_1_BIT = 0;
/*      */   private static final int VERSION_2_4_BIT = 1;
/*      */   private static final int VERSION_2_8_BIT = 2;
/*      */   private static final int VERSION_2_24_BIT = 3;
/*      */   private static final int VERSION_3_1_BIT = 4;
/*      */   private static final int VERSION_3_4_BIT = 5;
/*      */   private static final int VERSION_3_8_BIT = 6;
/*      */   private static final int VERSION_3_24_BIT = 7;
/*      */   private static final int VERSION_3_NT_16_BIT = 8;
/*      */   private static final int VERSION_3_NT_32_BIT = 9;
/*      */   private static final int VERSION_4_1_BIT = 10;
/*      */   private static final int VERSION_4_4_BIT = 11;
/*      */   private static final int VERSION_4_8_BIT = 12;
/*      */   private static final int VERSION_4_16_BIT = 13;
/*      */   private static final int VERSION_4_24_BIT = 14;
/*      */   private static final int VERSION_4_32_BIT = 15;
/*      */   private static final int VERSION_3_XP_EMBEDDED = 16;
/*      */   private static final int VERSION_4_XP_EMBEDDED = 17;
/*      */   private static final int VERSION_5_XP_EMBEDDED = 18;
/*      */   private long bitmapFileSize;
/*      */   private long bitmapOffset;
/*      */   private long compression;
/*      */   private long imageSize;
/*      */   private byte[] palette;
/*      */   private int imageType;
/*      */   private int numBands;
/*      */   private boolean isBottomUp;
/*      */   private int bitsPerPixel;
/*      */   private int redMask;
/*      */   private int greenMask;
/*      */   private int blueMask;
/*      */   private int alphaMask;
/*      */   private SampleModel sampleModel;
/*      */   private SampleModel originalSampleModel;
/*      */   private ColorModel colorModel;
/*      */   private ColorModel originalColorModel;
/*  124 */   private ImageInputStream iis = null;
/*      */ 
/*  127 */   private boolean gotHeader = false;
/*      */   private int width;
/*      */   private int height;
/*      */   private Rectangle destinationRegion;
/*      */   private Rectangle sourceRegion;
/*      */   private BMPMetadata metadata;
/*      */   private BufferedImage bi;
/*  150 */   private boolean noTransform = true;
/*      */ 
/*  153 */   private boolean seleBand = false;
/*      */   private int scaleX;
/*      */   private int scaleY;
/*      */   private int[] sourceBands;
/*      */   private int[] destBands;
/* 1757 */   private static Boolean isLinkedProfileDisabled = null;
/*      */ 
/* 1771 */   private static Boolean isWindowsPlatform = null;
/*      */ 
/*      */   public BMPImageReader(ImageReaderSpi paramImageReaderSpi)
/*      */   {
/*  165 */     super(paramImageReaderSpi);
/*      */   }
/*      */ 
/*      */   public void setInput(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  172 */     super.setInput(paramObject, paramBoolean1, paramBoolean2);
/*  173 */     this.iis = ((ImageInputStream)paramObject);
/*  174 */     if (this.iis != null)
/*  175 */       this.iis.setByteOrder(ByteOrder.LITTLE_ENDIAN);
/*  176 */     resetHeaderInfo();
/*      */   }
/*      */ 
/*      */   public int getNumImages(boolean paramBoolean) throws IOException
/*      */   {
/*  181 */     if (this.iis == null) {
/*  182 */       throw new IllegalStateException(I18N.getString("GetNumImages0"));
/*      */     }
/*  184 */     if ((this.seekForwardOnly) && (paramBoolean)) {
/*  185 */       throw new IllegalStateException(I18N.getString("GetNumImages1"));
/*      */     }
/*  187 */     return 1;
/*      */   }
/*      */ 
/*      */   public int getWidth(int paramInt) throws IOException {
/*  191 */     checkIndex(paramInt);
/*  192 */     readHeader();
/*  193 */     return this.width;
/*      */   }
/*      */ 
/*      */   public int getHeight(int paramInt) throws IOException {
/*  197 */     checkIndex(paramInt);
/*  198 */     readHeader();
/*  199 */     return this.height;
/*      */   }
/*      */ 
/*      */   private void checkIndex(int paramInt) {
/*  203 */     if (paramInt != 0)
/*  204 */       throw new IndexOutOfBoundsException(I18N.getString("BMPImageReader0"));
/*      */   }
/*      */ 
/*      */   public void readHeader() throws IOException
/*      */   {
/*  209 */     if (this.gotHeader) {
/*  210 */       return;
/*      */     }
/*  212 */     if (this.iis == null) {
/*  213 */       throw new IllegalStateException("Input source not set!");
/*      */     }
/*  215 */     int i = 0; int j = 0;
/*      */ 
/*  217 */     this.metadata = new BMPMetadata();
/*  218 */     this.iis.mark();
/*      */ 
/*  221 */     byte[] arrayOfByte1 = new byte[2];
/*  222 */     this.iis.read(arrayOfByte1);
/*  223 */     if ((arrayOfByte1[0] != 66) || (arrayOfByte1[1] != 77)) {
/*  224 */       throw new IllegalArgumentException(I18N.getString("BMPImageReader1"));
/*      */     }
/*      */ 
/*  227 */     this.bitmapFileSize = this.iis.readUnsignedInt();
/*      */ 
/*  229 */     this.iis.skipBytes(4);
/*      */ 
/*  232 */     this.bitmapOffset = this.iis.readUnsignedInt();
/*      */ 
/*  236 */     long l1 = this.iis.readUnsignedInt();
/*      */ 
/*  238 */     if (l1 == 12L) {
/*  239 */       this.width = this.iis.readShort();
/*  240 */       this.height = this.iis.readShort();
/*      */     } else {
/*  242 */       this.width = this.iis.readInt();
/*  243 */       this.height = this.iis.readInt();
/*      */     }
/*      */ 
/*  246 */     this.metadata.width = this.width;
/*  247 */     this.metadata.height = this.height;
/*      */ 
/*  249 */     int k = this.iis.readUnsignedShort();
/*  250 */     this.bitsPerPixel = this.iis.readUnsignedShort();
/*      */ 
/*  253 */     this.metadata.bitsPerPixel = ((short)this.bitsPerPixel);
/*      */ 
/*  257 */     this.numBands = 3;
/*      */ 
/*  259 */     if (l1 == 12L)
/*      */     {
/*  261 */       this.metadata.bmpVersion = "BMP v. 2.x";
/*      */ 
/*  264 */       if (this.bitsPerPixel == 1)
/*  265 */         this.imageType = 0;
/*  266 */       else if (this.bitsPerPixel == 4)
/*  267 */         this.imageType = 1;
/*  268 */       else if (this.bitsPerPixel == 8)
/*  269 */         this.imageType = 2;
/*  270 */       else if (this.bitsPerPixel == 24) {
/*  271 */         this.imageType = 3;
/*      */       }
/*      */ 
/*  275 */       int m = (int)((this.bitmapOffset - 14L - l1) / 3L);
/*  276 */       int n = m * 3;
/*  277 */       this.palette = new byte[n];
/*  278 */       this.iis.readFully(this.palette, 0, n);
/*  279 */       this.metadata.palette = this.palette;
/*  280 */       this.metadata.paletteSize = m;
/*      */     } else {
/*  282 */       this.compression = this.iis.readUnsignedInt();
/*  283 */       this.imageSize = this.iis.readUnsignedInt();
/*  284 */       long l2 = this.iis.readInt();
/*  285 */       long l3 = this.iis.readInt();
/*  286 */       long l4 = this.iis.readUnsignedInt();
/*  287 */       long l5 = this.iis.readUnsignedInt();
/*      */ 
/*  289 */       this.metadata.compression = ((int)this.compression);
/*  290 */       this.metadata.xPixelsPerMeter = ((int)l2);
/*  291 */       this.metadata.yPixelsPerMeter = ((int)l3);
/*  292 */       this.metadata.colorsUsed = ((int)l4);
/*  293 */       this.metadata.colorsImportant = ((int)l5);
/*      */ 
/*  295 */       if (l1 == 40L)
/*      */       {
/*      */         int i6;
/*  297 */         switch ((int)this.compression)
/*      */         {
/*      */         case 4:
/*      */         case 5:
/*  301 */           this.metadata.bmpVersion = "BMP v. 3.x";
/*  302 */           this.imageType = 16;
/*  303 */           break;
/*      */         case 0:
/*      */         case 1:
/*      */         case 2:
/*  310 */           int i5 = (int)((this.bitmapOffset - 14L - l1) / 4L);
/*  311 */           i6 = i5 * 4;
/*  312 */           this.palette = new byte[i6];
/*  313 */           this.iis.readFully(this.palette, 0, i6);
/*      */ 
/*  315 */           this.metadata.palette = this.palette;
/*  316 */           this.metadata.paletteSize = i5;
/*      */ 
/*  318 */           if (this.bitsPerPixel == 1) {
/*  319 */             this.imageType = 4;
/*  320 */           } else if (this.bitsPerPixel == 4) {
/*  321 */             this.imageType = 5;
/*  322 */           } else if (this.bitsPerPixel == 8) {
/*  323 */             this.imageType = 6;
/*  324 */           } else if (this.bitsPerPixel == 24) {
/*  325 */             this.imageType = 7;
/*  326 */           } else if (this.bitsPerPixel == 16) {
/*  327 */             this.imageType = 8;
/*      */ 
/*  329 */             this.redMask = 31744;
/*  330 */             this.greenMask = 992;
/*  331 */             this.blueMask = 31;
/*  332 */             this.metadata.redMask = this.redMask;
/*  333 */             this.metadata.greenMask = this.greenMask;
/*  334 */             this.metadata.blueMask = this.blueMask;
/*  335 */           } else if (this.bitsPerPixel == 32) {
/*  336 */             this.imageType = 9;
/*  337 */             this.redMask = 16711680;
/*  338 */             this.greenMask = 65280;
/*  339 */             this.blueMask = 255;
/*  340 */             this.metadata.redMask = this.redMask;
/*  341 */             this.metadata.greenMask = this.greenMask;
/*  342 */             this.metadata.blueMask = this.blueMask;
/*      */           }
/*      */ 
/*  345 */           this.metadata.bmpVersion = "BMP v. 3.x";
/*  346 */           break;
/*      */         case 3:
/*  350 */           if (this.bitsPerPixel == 16)
/*  351 */             this.imageType = 8;
/*  352 */           else if (this.bitsPerPixel == 32) {
/*  353 */             this.imageType = 9;
/*      */           }
/*      */ 
/*  357 */           this.redMask = ((int)this.iis.readUnsignedInt());
/*  358 */           this.greenMask = ((int)this.iis.readUnsignedInt());
/*  359 */           this.blueMask = ((int)this.iis.readUnsignedInt());
/*  360 */           this.metadata.redMask = this.redMask;
/*  361 */           this.metadata.greenMask = this.greenMask;
/*  362 */           this.metadata.blueMask = this.blueMask;
/*      */ 
/*  364 */           if (l4 != 0L)
/*      */           {
/*  366 */             i6 = (int)l4 * 4;
/*  367 */             this.palette = new byte[i6];
/*  368 */             this.iis.readFully(this.palette, 0, i6);
/*      */ 
/*  370 */             this.metadata.palette = this.palette;
/*  371 */             this.metadata.paletteSize = ((int)l4);
/*      */           }
/*  373 */           this.metadata.bmpVersion = "BMP v. 3.x NT";
/*      */ 
/*  375 */           break;
/*      */         default:
/*  377 */           throw new RuntimeException(I18N.getString("BMPImageReader2"));
/*      */         }
/*      */       }
/*  380 */       else if ((l1 == 108L) || (l1 == 124L))
/*      */       {
/*  382 */         if (l1 == 108L)
/*  383 */           this.metadata.bmpVersion = "BMP v. 4.x";
/*  384 */         else if (l1 == 124L) {
/*  385 */           this.metadata.bmpVersion = "BMP v. 5.x";
/*      */         }
/*      */ 
/*  388 */         this.redMask = ((int)this.iis.readUnsignedInt());
/*  389 */         this.greenMask = ((int)this.iis.readUnsignedInt());
/*  390 */         this.blueMask = ((int)this.iis.readUnsignedInt());
/*      */ 
/*  392 */         this.alphaMask = ((int)this.iis.readUnsignedInt());
/*  393 */         long l6 = this.iis.readUnsignedInt();
/*  394 */         int i7 = this.iis.readInt();
/*  395 */         int i8 = this.iis.readInt();
/*  396 */         int i9 = this.iis.readInt();
/*  397 */         int i10 = this.iis.readInt();
/*  398 */         int i11 = this.iis.readInt();
/*  399 */         int i12 = this.iis.readInt();
/*  400 */         int i13 = this.iis.readInt();
/*  401 */         int i14 = this.iis.readInt();
/*  402 */         int i15 = this.iis.readInt();
/*  403 */         long l7 = this.iis.readUnsignedInt();
/*  404 */         long l8 = this.iis.readUnsignedInt();
/*  405 */         long l9 = this.iis.readUnsignedInt();
/*      */ 
/*  407 */         if (l1 == 124L) {
/*  408 */           this.metadata.intent = this.iis.readInt();
/*  409 */           i = this.iis.readInt();
/*  410 */           j = this.iis.readInt();
/*  411 */           this.iis.skipBytes(4);
/*      */         }
/*      */ 
/*  414 */         this.metadata.colorSpace = ((int)l6);
/*      */ 
/*  416 */         if (l6 == 0L)
/*      */         {
/*  418 */           this.metadata.redX = i7;
/*  419 */           this.metadata.redY = i8;
/*  420 */           this.metadata.redZ = i9;
/*  421 */           this.metadata.greenX = i10;
/*  422 */           this.metadata.greenY = i11;
/*  423 */           this.metadata.greenZ = i12;
/*  424 */           this.metadata.blueX = i13;
/*  425 */           this.metadata.blueY = i14;
/*  426 */           this.metadata.blueZ = i15;
/*  427 */           this.metadata.gammaRed = ((int)l7);
/*  428 */           this.metadata.gammaGreen = ((int)l8);
/*  429 */           this.metadata.gammaBlue = ((int)l9);
/*      */         }
/*      */ 
/*  433 */         int i16 = (int)((this.bitmapOffset - 14L - l1) / 4L);
/*  434 */         int i17 = i16 * 4;
/*  435 */         this.palette = new byte[i17];
/*  436 */         this.iis.readFully(this.palette, 0, i17);
/*  437 */         this.metadata.palette = this.palette;
/*  438 */         this.metadata.paletteSize = i16;
/*      */ 
/*  440 */         switch ((int)this.compression) {
/*      */         case 4:
/*      */         case 5:
/*  443 */           if (l1 == 108L)
/*  444 */             this.imageType = 17;
/*  445 */           else if (l1 == 124L)
/*  446 */             this.imageType = 18; break;
/*      */         default:
/*  450 */           if (this.bitsPerPixel == 1) {
/*  451 */             this.imageType = 10;
/*  452 */           } else if (this.bitsPerPixel == 4) {
/*  453 */             this.imageType = 11;
/*  454 */           } else if (this.bitsPerPixel == 8) {
/*  455 */             this.imageType = 12;
/*  456 */           } else if (this.bitsPerPixel == 16) {
/*  457 */             this.imageType = 13;
/*  458 */             if ((int)this.compression == 0) {
/*  459 */               this.redMask = 31744;
/*  460 */               this.greenMask = 992;
/*  461 */               this.blueMask = 31;
/*      */             }
/*  463 */           } else if (this.bitsPerPixel == 24) {
/*  464 */             this.imageType = 14;
/*  465 */           } else if (this.bitsPerPixel == 32) {
/*  466 */             this.imageType = 15;
/*  467 */             if ((int)this.compression == 0) {
/*  468 */               this.redMask = 16711680;
/*  469 */               this.greenMask = 65280;
/*  470 */               this.blueMask = 255;
/*      */             }
/*      */           }
/*      */ 
/*  474 */           this.metadata.redMask = this.redMask;
/*  475 */           this.metadata.greenMask = this.greenMask;
/*  476 */           this.metadata.blueMask = this.blueMask;
/*  477 */           this.metadata.alphaMask = this.alphaMask;
/*      */         }
/*      */       } else {
/*  480 */         throw new RuntimeException(I18N.getString("BMPImageReader3"));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  485 */     if (this.height > 0)
/*      */     {
/*  487 */       this.isBottomUp = true;
/*      */     }
/*      */     else {
/*  490 */       this.isBottomUp = false;
/*  491 */       this.height = Math.abs(this.height);
/*      */     }
/*      */ 
/*  496 */     Object localObject1 = ColorSpace.getInstance(1000);
/*      */     Object localObject2;
/*  497 */     if ((this.metadata.colorSpace == 3) || (this.metadata.colorSpace == 4))
/*      */     {
/*  500 */       this.iis.mark();
/*  501 */       this.iis.skipBytes(i - l1);
/*  502 */       localObject2 = new byte[j];
/*  503 */       this.iis.readFully((byte[])localObject2, 0, j);
/*  504 */       this.iis.reset();
/*      */       try
/*      */       {
/*  507 */         if ((this.metadata.colorSpace == 3) && (isLinkedProfileAllowed()) && (!isUncOrDevicePath((byte[])localObject2)))
/*      */         {
/*  511 */           String str = new String((byte[])localObject2, "windows-1252");
/*      */ 
/*  513 */           localObject1 = new ICC_ColorSpace(ICC_Profile.getInstance(str));
/*      */         }
/*      */         else {
/*  516 */           localObject1 = new ICC_ColorSpace(ICC_Profile.getInstance((byte[])localObject2));
/*      */         }
/*      */       }
/*      */       catch (Exception localException) {
/*  520 */         localObject1 = ColorSpace.getInstance(1000);
/*      */       }
/*      */     }
/*      */ 
/*  524 */     if ((this.bitsPerPixel == 0) || (this.compression == 4L) || (this.compression == 5L))
/*      */     {
/*  529 */       this.colorModel = null;
/*  530 */       this.sampleModel = null;
/*  531 */     } else if ((this.bitsPerPixel == 1) || (this.bitsPerPixel == 4) || (this.bitsPerPixel == 8))
/*      */     {
/*  533 */       this.numBands = 1;
/*      */ 
/*  535 */       if (this.bitsPerPixel == 8) {
/*  536 */         localObject2 = new int[this.numBands];
/*  537 */         for (int i1 = 0; i1 < this.numBands; i1++) {
/*  538 */           localObject2[i1] = (this.numBands - 1 - i1);
/*      */         }
/*  540 */         this.sampleModel = new PixelInterleavedSampleModel(0, this.width, this.height, this.numBands, this.numBands * this.width, (int[])localObject2);
/*      */       }
/*      */       else
/*      */       {
/*  548 */         this.sampleModel = new MultiPixelPackedSampleModel(0, this.width, this.height, this.bitsPerPixel);
/*      */       }
/*      */       byte[] arrayOfByte2;
/*      */       byte[] arrayOfByte3;
/*      */       int i4;
/*      */       int i3;
/*  556 */       if ((this.imageType == 0) || (this.imageType == 1) || (this.imageType == 2))
/*      */       {
/*  561 */         l1 = this.palette.length / 3;
/*      */ 
/*  563 */         if (l1 > 256L) {
/*  564 */           l1 = 256L;
/*      */         }
/*      */ 
/*  568 */         localObject2 = new byte[(int)l1];
/*  569 */         arrayOfByte2 = new byte[(int)l1];
/*  570 */         arrayOfByte3 = new byte[(int)l1];
/*  571 */         for (i4 = 0; i4 < (int)l1; i4++) {
/*  572 */           i3 = 3 * i4;
/*  573 */           arrayOfByte3[i4] = this.palette[i3];
/*  574 */           arrayOfByte2[i4] = this.palette[(i3 + 1)];
/*  575 */           localObject2[i4] = this.palette[(i3 + 2)];
/*      */         }
/*      */       } else {
/*  578 */         l1 = this.palette.length / 4;
/*      */ 
/*  580 */         if (l1 > 256L) {
/*  581 */           l1 = 256L;
/*      */         }
/*      */ 
/*  585 */         localObject2 = new byte[(int)l1];
/*  586 */         arrayOfByte2 = new byte[(int)l1];
/*  587 */         arrayOfByte3 = new byte[(int)l1];
/*  588 */         for (i4 = 0; i4 < l1; i4++) {
/*  589 */           i3 = 4 * i4;
/*  590 */           arrayOfByte3[i4] = this.palette[i3];
/*  591 */           arrayOfByte2[i4] = this.palette[(i3 + 1)];
/*  592 */           localObject2[i4] = this.palette[(i3 + 2)];
/*      */         }
/*      */       }
/*      */ 
/*  596 */       if (ImageUtil.isIndicesForGrayscale((byte[])localObject2, arrayOfByte2, arrayOfByte3)) {
/*  597 */         this.colorModel = ImageUtil.createColorModel(null, this.sampleModel);
/*      */       }
/*      */       else
/*  600 */         this.colorModel = new IndexColorModel(this.bitsPerPixel, (int)l1, (byte[])localObject2, arrayOfByte2, arrayOfByte3);
/*  601 */     } else if (this.bitsPerPixel == 16) {
/*  602 */       this.numBands = 3;
/*  603 */       this.sampleModel = new SinglePixelPackedSampleModel(1, this.width, this.height, new int[] { this.redMask, this.greenMask, this.blueMask });
/*      */ 
/*  608 */       this.colorModel = new DirectColorModel((ColorSpace)localObject1, 16, this.redMask, this.greenMask, this.blueMask, 0, false, 1);
/*      */     }
/*  613 */     else if (this.bitsPerPixel == 32) {
/*  614 */       this.numBands = (this.alphaMask == 0 ? 3 : 4);
/*      */ 
/*  618 */       localObject2 = new int[] { this.redMask, this.greenMask, this.blueMask, this.numBands == 3 ? new int[] { this.redMask, this.greenMask, this.blueMask } : this.alphaMask };
/*      */ 
/*  622 */       this.sampleModel = new SinglePixelPackedSampleModel(3, this.width, this.height, (int[])localObject2);
/*      */ 
/*  627 */       this.colorModel = new DirectColorModel((ColorSpace)localObject1, 32, this.redMask, this.greenMask, this.blueMask, this.alphaMask, false, 3);
/*      */     }
/*      */     else
/*      */     {
/*  632 */       this.numBands = 3;
/*      */ 
/*  634 */       localObject2 = new int[this.numBands];
/*  635 */       for (int i2 = 0; i2 < this.numBands; i2++) {
/*  636 */         localObject2[i2] = (this.numBands - 1 - i2);
/*      */       }
/*      */ 
/*  639 */       this.sampleModel = new PixelInterleavedSampleModel(0, this.width, this.height, this.numBands, this.numBands * this.width, (int[])localObject2);
/*      */ 
/*  646 */       this.colorModel = ImageUtil.createColorModel((ColorSpace)localObject1, this.sampleModel);
/*      */     }
/*      */ 
/*  650 */     this.originalSampleModel = this.sampleModel;
/*  651 */     this.originalColorModel = this.colorModel;
/*      */ 
/*  655 */     this.iis.reset();
/*  656 */     this.iis.skipBytes(this.bitmapOffset);
/*  657 */     this.gotHeader = true;
/*      */   }
/*      */ 
/*      */   public Iterator getImageTypes(int paramInt) throws IOException
/*      */   {
/*  662 */     checkIndex(paramInt);
/*  663 */     readHeader();
/*  664 */     ArrayList localArrayList = new ArrayList(1);
/*  665 */     localArrayList.add(new ImageTypeSpecifier(this.originalColorModel, this.originalSampleModel));
/*      */ 
/*  667 */     return localArrayList.iterator();
/*      */   }
/*      */ 
/*      */   public ImageReadParam getDefaultReadParam() {
/*  671 */     return new ImageReadParam();
/*      */   }
/*      */ 
/*      */   public IIOMetadata getImageMetadata(int paramInt) throws IOException
/*      */   {
/*  676 */     checkIndex(paramInt);
/*  677 */     if (this.metadata == null) {
/*  678 */       readHeader();
/*      */     }
/*  680 */     return this.metadata;
/*      */   }
/*      */ 
/*      */   public IIOMetadata getStreamMetadata() throws IOException {
/*  684 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isRandomAccessEasy(int paramInt) throws IOException {
/*  688 */     checkIndex(paramInt);
/*  689 */     readHeader();
/*  690 */     return this.metadata.compression == 0;
/*      */   }
/*      */ 
/*      */   public BufferedImage read(int paramInt, ImageReadParam paramImageReadParam)
/*      */     throws IOException
/*      */   {
/*  696 */     if (this.iis == null) {
/*  697 */       throw new IllegalStateException(I18N.getString("BMPImageReader5"));
/*      */     }
/*      */ 
/*  700 */     checkIndex(paramInt);
/*  701 */     clearAbortRequest();
/*  702 */     processImageStarted(paramInt);
/*      */ 
/*  704 */     if (paramImageReadParam == null) {
/*  705 */       paramImageReadParam = getDefaultReadParam();
/*      */     }
/*      */ 
/*  708 */     readHeader();
/*      */ 
/*  710 */     this.sourceRegion = new Rectangle(0, 0, 0, 0);
/*  711 */     this.destinationRegion = new Rectangle(0, 0, 0, 0);
/*      */ 
/*  713 */     computeRegions(paramImageReadParam, this.width, this.height, paramImageReadParam.getDestination(), this.sourceRegion, this.destinationRegion);
/*      */ 
/*  718 */     this.scaleX = paramImageReadParam.getSourceXSubsampling();
/*  719 */     this.scaleY = paramImageReadParam.getSourceYSubsampling();
/*      */ 
/*  722 */     this.sourceBands = paramImageReadParam.getSourceBands();
/*  723 */     this.destBands = paramImageReadParam.getDestinationBands();
/*      */ 
/*  725 */     this.seleBand = ((this.sourceBands != null) && (this.destBands != null));
/*  726 */     this.noTransform = ((this.destinationRegion.equals(new Rectangle(0, 0, this.width, this.height))) || (this.seleBand));
/*      */ 
/*  730 */     if (!this.seleBand) {
/*  731 */       this.sourceBands = new int[this.numBands];
/*  732 */       this.destBands = new int[this.numBands];
/*  733 */       for (int i = 0; i < this.numBands; i++)
/*      */       {
/*      */         int tmp247_246 = i; this.sourceBands[i] = tmp247_246; this.destBands[i] = tmp247_246;
/*      */       }
/*      */     }
/*      */ 
/*  738 */     this.bi = paramImageReadParam.getDestination();
/*      */ 
/*  741 */     WritableRaster localWritableRaster = null;
/*      */ 
/*  743 */     if (this.bi == null) {
/*  744 */       if ((this.sampleModel != null) && (this.colorModel != null)) {
/*  745 */         this.sampleModel = this.sampleModel.createCompatibleSampleModel(this.destinationRegion.x + this.destinationRegion.width, this.destinationRegion.y + this.destinationRegion.height);
/*      */ 
/*  750 */         if (this.seleBand)
/*  751 */           this.sampleModel = this.sampleModel.createSubsetSampleModel(this.sourceBands);
/*  752 */         localWritableRaster = Raster.createWritableRaster(this.sampleModel, new Point());
/*  753 */         this.bi = new BufferedImage(this.colorModel, localWritableRaster, false, null);
/*      */       }
/*      */     } else {
/*  756 */       localWritableRaster = this.bi.getWritableTile(0, 0);
/*  757 */       this.sampleModel = this.bi.getSampleModel();
/*  758 */       this.colorModel = this.bi.getColorModel();
/*      */ 
/*  760 */       this.noTransform &= this.destinationRegion.equals(localWritableRaster.getBounds());
/*      */     }
/*      */ 
/*  763 */     byte[] arrayOfByte = null;
/*  764 */     short[] arrayOfShort = null;
/*  765 */     int[] arrayOfInt = null;
/*      */ 
/*  768 */     if (this.sampleModel != null) {
/*  769 */       if (this.sampleModel.getDataType() == 0) {
/*  770 */         arrayOfByte = (byte[])((DataBufferByte)localWritableRaster.getDataBuffer()).getData();
/*      */       }
/*  772 */       else if (this.sampleModel.getDataType() == 1) {
/*  773 */         arrayOfShort = (short[])((DataBufferUShort)localWritableRaster.getDataBuffer()).getData();
/*      */       }
/*  775 */       else if (this.sampleModel.getDataType() == 3) {
/*  776 */         arrayOfInt = (int[])((DataBufferInt)localWritableRaster.getDataBuffer()).getData();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  781 */     switch (this.imageType)
/*      */     {
/*      */     case 0:
/*  785 */       read1Bit(arrayOfByte);
/*  786 */       break;
/*      */     case 1:
/*  790 */       read4Bit(arrayOfByte);
/*  791 */       break;
/*      */     case 2:
/*  795 */       read8Bit(arrayOfByte);
/*  796 */       break;
/*      */     case 3:
/*  800 */       read24Bit(arrayOfByte);
/*  801 */       break;
/*      */     case 4:
/*  805 */       read1Bit(arrayOfByte);
/*  806 */       break;
/*      */     case 5:
/*  809 */       switch ((int)this.compression) {
/*      */       case 0:
/*  811 */         read4Bit(arrayOfByte);
/*  812 */         break;
/*      */       case 2:
/*  815 */         readRLE4(arrayOfByte);
/*  816 */         break;
/*      */       default:
/*  819 */         throw new RuntimeException(I18N.getString("BMPImageReader1"));
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 6:
/*  825 */       switch ((int)this.compression) {
/*      */       case 0:
/*  827 */         read8Bit(arrayOfByte);
/*  828 */         break;
/*      */       case 1:
/*  831 */         readRLE8(arrayOfByte);
/*  832 */         break;
/*      */       default:
/*  835 */         throw new RuntimeException(I18N.getString("BMPImageReader1"));
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 7:
/*  843 */       read24Bit(arrayOfByte);
/*  844 */       break;
/*      */     case 8:
/*  847 */       read16Bit(arrayOfShort);
/*  848 */       break;
/*      */     case 9:
/*  851 */       read32Bit(arrayOfInt);
/*  852 */       break;
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*  857 */       this.bi = readEmbedded((int)this.compression, this.bi, paramImageReadParam);
/*  858 */       break;
/*      */     case 10:
/*  861 */       read1Bit(arrayOfByte);
/*  862 */       break;
/*      */     case 11:
/*  865 */       switch ((int)this.compression)
/*      */       {
/*      */       case 0:
/*  868 */         read4Bit(arrayOfByte);
/*  869 */         break;
/*      */       case 2:
/*  872 */         readRLE4(arrayOfByte);
/*  873 */         break;
/*      */       default:
/*  876 */         throw new RuntimeException(I18N.getString("BMPImageReader1"));
/*      */       }
/*      */ 
/*      */     case 12:
/*  881 */       switch ((int)this.compression)
/*      */       {
/*      */       case 0:
/*  884 */         read8Bit(arrayOfByte);
/*  885 */         break;
/*      */       case 1:
/*  888 */         readRLE8(arrayOfByte);
/*  889 */         break;
/*      */       default:
/*  892 */         throw new RuntimeException(I18N.getString("BMPImageReader1"));
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 13:
/*  898 */       read16Bit(arrayOfShort);
/*  899 */       break;
/*      */     case 14:
/*  902 */       read24Bit(arrayOfByte);
/*  903 */       break;
/*      */     case 15:
/*  906 */       read32Bit(arrayOfInt);
/*      */     }
/*      */ 
/*  910 */     if (abortRequested())
/*  911 */       processReadAborted();
/*      */     else {
/*  913 */       processImageComplete();
/*      */     }
/*  915 */     return this.bi;
/*      */   }
/*      */ 
/*      */   public boolean canReadRaster() {
/*  919 */     return true;
/*      */   }
/*      */ 
/*      */   public Raster readRaster(int paramInt, ImageReadParam paramImageReadParam) throws IOException
/*      */   {
/*  924 */     BufferedImage localBufferedImage = read(paramInt, paramImageReadParam);
/*  925 */     return localBufferedImage.getData();
/*      */   }
/*      */ 
/*      */   private void resetHeaderInfo() {
/*  929 */     this.gotHeader = false;
/*  930 */     this.bi = null;
/*  931 */     this.sampleModel = (this.originalSampleModel = null);
/*  932 */     this.colorModel = (this.originalColorModel = null);
/*      */   }
/*      */ 
/*      */   public void reset() {
/*  936 */     super.reset();
/*  937 */     this.iis = null;
/*  938 */     resetHeaderInfo();
/*      */   }
/*      */ 
/*      */   private void read1Bit(byte[] paramArrayOfByte) throws IOException
/*      */   {
/*  943 */     int i = (this.width + 7) / 8;
/*  944 */     int j = i % 4;
/*  945 */     if (j != 0) {
/*  946 */       j = 4 - j;
/*      */     }
/*      */ 
/*  949 */     int k = i + j;
/*      */     int n;
/*  951 */     if (this.noTransform) {
/*  952 */       int m = this.isBottomUp ? (this.height - 1) * i : 0;
/*      */ 
/*  954 */       for (n = 0; (n < this.height) && 
/*  955 */         (!abortRequested()); n++)
/*      */       {
/*  958 */         this.iis.readFully(paramArrayOfByte, m, i);
/*  959 */         this.iis.skipBytes(j);
/*  960 */         m += (this.isBottomUp ? -i : i);
/*  961 */         processImageUpdate(this.bi, 0, n, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/*  964 */         processImageProgress(100.0F * n / this.destinationRegion.height);
/*      */       }
/*      */     } else {
/*  967 */       byte[] arrayOfByte = new byte[k];
/*  968 */       n = ((MultiPixelPackedSampleModel)this.sampleModel).getScanlineStride();
/*      */ 
/*  971 */       if (this.isBottomUp) {
/*  972 */         i1 = this.sourceRegion.y + (this.destinationRegion.height - 1) * this.scaleY;
/*      */ 
/*  974 */         this.iis.skipBytes(k * (this.height - 1 - i1));
/*      */       } else {
/*  976 */         this.iis.skipBytes(k * this.sourceRegion.y);
/*      */       }
/*  978 */       int i1 = k * (this.scaleY - 1);
/*      */ 
/*  981 */       int[] arrayOfInt1 = new int[this.destinationRegion.width];
/*  982 */       int[] arrayOfInt2 = new int[this.destinationRegion.width];
/*  983 */       int[] arrayOfInt3 = new int[this.destinationRegion.width];
/*  984 */       int[] arrayOfInt4 = new int[this.destinationRegion.width];
/*      */ 
/*  986 */       int i2 = this.destinationRegion.x; int i3 = this.sourceRegion.x; int i4 = 0;
/*      */ 
/*  988 */       for (; i2 < this.destinationRegion.x + this.destinationRegion.width; 
/*  988 */         i3 += this.scaleX) {
/*  989 */         arrayOfInt3[i4] = (i3 >> 3);
/*  990 */         arrayOfInt1[i4] = (7 - (i3 & 0x7));
/*  991 */         arrayOfInt4[i4] = (i2 >> 3);
/*  992 */         arrayOfInt2[i4] = (7 - (i2 & 0x7));
/*      */ 
/*  988 */         i2++; i4++;
/*      */       }
/*      */ 
/*  995 */       i2 = this.destinationRegion.y * n;
/*  996 */       if (this.isBottomUp) {
/*  997 */         i2 += (this.destinationRegion.height - 1) * n;
/*      */       }
/*  999 */       i3 = 0; i4 = this.sourceRegion.y;
/* 1000 */       for (; i3 < this.destinationRegion.height; i4 += this.scaleY)
/*      */       {
/* 1002 */         if (abortRequested())
/*      */           break;
/* 1004 */         this.iis.read(arrayOfByte, 0, k);
/* 1005 */         for (int i5 = 0; i5 < this.destinationRegion.width; i5++)
/*      */         {
/* 1007 */           int i6 = arrayOfByte[arrayOfInt3[i5]] >> arrayOfInt1[i5] & 0x1;
/*      */           int tmp555_554 = (i2 + arrayOfInt4[i5]);
/*      */           byte[] tmp555_546 = paramArrayOfByte; tmp555_546[tmp555_554] = ((byte)(tmp555_546[tmp555_554] | i6 << arrayOfInt2[i5]));
/*      */         }
/*      */ 
/* 1011 */         i2 += (this.isBottomUp ? -n : n);
/* 1012 */         this.iis.skipBytes(i1);
/* 1013 */         processImageUpdate(this.bi, 0, i3, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1016 */         processImageProgress(100.0F * i3 / this.destinationRegion.height);
/*      */ 
/* 1000 */         i3++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void read4Bit(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1024 */     int i = (this.width + 1) / 2;
/*      */ 
/* 1027 */     int j = i % 4;
/* 1028 */     if (j != 0) {
/* 1029 */       j = 4 - j;
/*      */     }
/* 1031 */     int k = i + j;
/*      */     int n;
/* 1033 */     if (this.noTransform) {
/* 1034 */       int m = this.isBottomUp ? (this.height - 1) * i : 0;
/*      */ 
/* 1036 */       for (n = 0; (n < this.height) && 
/* 1037 */         (!abortRequested()); n++)
/*      */       {
/* 1040 */         this.iis.readFully(paramArrayOfByte, m, i);
/* 1041 */         this.iis.skipBytes(j);
/* 1042 */         m += (this.isBottomUp ? -i : i);
/* 1043 */         processImageUpdate(this.bi, 0, n, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1046 */         processImageProgress(100.0F * n / this.destinationRegion.height);
/*      */       }
/*      */     } else {
/* 1049 */       byte[] arrayOfByte = new byte[k];
/* 1050 */       n = ((MultiPixelPackedSampleModel)this.sampleModel).getScanlineStride();
/*      */ 
/* 1053 */       if (this.isBottomUp) {
/* 1054 */         i1 = this.sourceRegion.y + (this.destinationRegion.height - 1) * this.scaleY;
/*      */ 
/* 1056 */         this.iis.skipBytes(k * (this.height - 1 - i1));
/*      */       } else {
/* 1058 */         this.iis.skipBytes(k * this.sourceRegion.y);
/*      */       }
/* 1060 */       int i1 = k * (this.scaleY - 1);
/*      */ 
/* 1063 */       int[] arrayOfInt1 = new int[this.destinationRegion.width];
/* 1064 */       int[] arrayOfInt2 = new int[this.destinationRegion.width];
/* 1065 */       int[] arrayOfInt3 = new int[this.destinationRegion.width];
/* 1066 */       int[] arrayOfInt4 = new int[this.destinationRegion.width];
/*      */ 
/* 1068 */       int i2 = this.destinationRegion.x; int i3 = this.sourceRegion.x; int i4 = 0;
/*      */ 
/* 1070 */       for (; i2 < this.destinationRegion.x + this.destinationRegion.width; 
/* 1070 */         i3 += this.scaleX) {
/* 1071 */         arrayOfInt3[i4] = (i3 >> 1);
/* 1072 */         arrayOfInt1[i4] = (1 - (i3 & 0x1) << 2);
/* 1073 */         arrayOfInt4[i4] = (i2 >> 1);
/* 1074 */         arrayOfInt2[i4] = (1 - (i2 & 0x1) << 2);
/*      */ 
/* 1070 */         i2++; i4++;
/*      */       }
/*      */ 
/* 1077 */       i2 = this.destinationRegion.y * n;
/* 1078 */       if (this.isBottomUp) {
/* 1079 */         i2 += (this.destinationRegion.height - 1) * n;
/*      */       }
/* 1081 */       i3 = 0; i4 = this.sourceRegion.y;
/* 1082 */       for (; i3 < this.destinationRegion.height; i4 += this.scaleY)
/*      */       {
/* 1084 */         if (abortRequested())
/*      */           break;
/* 1086 */         this.iis.read(arrayOfByte, 0, k);
/* 1087 */         for (int i5 = 0; i5 < this.destinationRegion.width; i5++)
/*      */         {
/* 1089 */           int i6 = arrayOfByte[arrayOfInt3[i5]] >> arrayOfInt1[i5] & 0xF;
/*      */           int tmp554_553 = (i2 + arrayOfInt4[i5]);
/*      */           byte[] tmp554_545 = paramArrayOfByte; tmp554_545[tmp554_553] = ((byte)(tmp554_545[tmp554_553] | i6 << arrayOfInt2[i5]));
/*      */         }
/*      */ 
/* 1093 */         i2 += (this.isBottomUp ? -n : n);
/* 1094 */         this.iis.skipBytes(i1);
/* 1095 */         processImageUpdate(this.bi, 0, i3, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1098 */         processImageProgress(100.0F * i3 / this.destinationRegion.height);
/*      */ 
/* 1082 */         i3++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void read8Bit(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1107 */     int i = this.width % 4;
/* 1108 */     if (i != 0) {
/* 1109 */       i = 4 - i;
/*      */     }
/*      */ 
/* 1112 */     int j = this.width + i;
/*      */     int m;
/* 1114 */     if (this.noTransform) {
/* 1115 */       int k = this.isBottomUp ? (this.height - 1) * this.width : 0;
/*      */ 
/* 1117 */       for (m = 0; (m < this.height) && 
/* 1118 */         (!abortRequested()); m++)
/*      */       {
/* 1121 */         this.iis.readFully(paramArrayOfByte, k, this.width);
/* 1122 */         this.iis.skipBytes(i);
/* 1123 */         k += (this.isBottomUp ? -this.width : this.width);
/* 1124 */         processImageUpdate(this.bi, 0, m, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1127 */         processImageProgress(100.0F * m / this.destinationRegion.height);
/*      */       }
/*      */     } else {
/* 1130 */       byte[] arrayOfByte = new byte[j];
/* 1131 */       m = ((ComponentSampleModel)this.sampleModel).getScanlineStride();
/*      */ 
/* 1134 */       if (this.isBottomUp) {
/* 1135 */         n = this.sourceRegion.y + (this.destinationRegion.height - 1) * this.scaleY;
/*      */ 
/* 1137 */         this.iis.skipBytes(j * (this.height - 1 - n));
/*      */       } else {
/* 1139 */         this.iis.skipBytes(j * this.sourceRegion.y);
/*      */       }
/* 1141 */       int n = j * (this.scaleY - 1);
/*      */ 
/* 1143 */       int i1 = this.destinationRegion.y * m;
/* 1144 */       if (this.isBottomUp)
/* 1145 */         i1 += (this.destinationRegion.height - 1) * m;
/* 1146 */       i1 += this.destinationRegion.x;
/*      */ 
/* 1148 */       int i2 = 0; int i3 = this.sourceRegion.y;
/* 1149 */       for (; i2 < this.destinationRegion.height; i3 += this.scaleY)
/*      */       {
/* 1151 */         if (abortRequested())
/*      */           break;
/* 1153 */         this.iis.read(arrayOfByte, 0, j);
/* 1154 */         int i4 = 0; int i5 = this.sourceRegion.x;
/* 1155 */         for (; i4 < this.destinationRegion.width; i5 += this.scaleX)
/*      */         {
/* 1157 */           paramArrayOfByte[(i1 + i4)] = arrayOfByte[i5];
/*      */ 
/* 1155 */           i4++;
/*      */         }
/*      */ 
/* 1160 */         i1 += (this.isBottomUp ? -m : m);
/* 1161 */         this.iis.skipBytes(n);
/* 1162 */         processImageUpdate(this.bi, 0, i2, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1165 */         processImageProgress(100.0F * i2 / this.destinationRegion.height);
/*      */ 
/* 1149 */         i2++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void read24Bit(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1174 */     int i = this.width * 3 % 4;
/* 1175 */     if (i != 0) {
/* 1176 */       i = 4 - i;
/*      */     }
/* 1178 */     int j = this.width * 3;
/* 1179 */     int k = j + i;
/*      */     int n;
/* 1181 */     if (this.noTransform) {
/* 1182 */       int m = this.isBottomUp ? (this.height - 1) * this.width * 3 : 0;
/*      */ 
/* 1184 */       for (n = 0; (n < this.height) && 
/* 1185 */         (!abortRequested()); n++)
/*      */       {
/* 1188 */         this.iis.readFully(paramArrayOfByte, m, j);
/* 1189 */         this.iis.skipBytes(i);
/* 1190 */         m += (this.isBottomUp ? -j : j);
/* 1191 */         processImageUpdate(this.bi, 0, n, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1194 */         processImageProgress(100.0F * n / this.destinationRegion.height);
/*      */       }
/*      */     } else {
/* 1197 */       byte[] arrayOfByte = new byte[k];
/* 1198 */       j = ((ComponentSampleModel)this.sampleModel).getScanlineStride();
/*      */ 
/* 1201 */       if (this.isBottomUp) {
/* 1202 */         n = this.sourceRegion.y + (this.destinationRegion.height - 1) * this.scaleY;
/*      */ 
/* 1204 */         this.iis.skipBytes(k * (this.height - 1 - n));
/*      */       } else {
/* 1206 */         this.iis.skipBytes(k * this.sourceRegion.y);
/*      */       }
/* 1208 */       n = k * (this.scaleY - 1);
/*      */ 
/* 1210 */       int i1 = this.destinationRegion.y * j;
/* 1211 */       if (this.isBottomUp)
/* 1212 */         i1 += (this.destinationRegion.height - 1) * j;
/* 1213 */       i1 += this.destinationRegion.x * 3;
/*      */ 
/* 1215 */       int i2 = 0; int i3 = this.sourceRegion.y;
/* 1216 */       for (; i2 < this.destinationRegion.height; i3 += this.scaleY)
/*      */       {
/* 1218 */         if (abortRequested())
/*      */           break;
/* 1220 */         this.iis.read(arrayOfByte, 0, k);
/* 1221 */         int i4 = 0; int i5 = 3 * this.sourceRegion.x;
/* 1222 */         for (; i4 < this.destinationRegion.width; i5 += 3 * this.scaleX)
/*      */         {
/* 1224 */           int i6 = 3 * i4 + i1;
/* 1225 */           for (int i7 = 0; i7 < this.destBands.length; i7++)
/* 1226 */             paramArrayOfByte[(i6 + this.destBands[i7])] = arrayOfByte[(i5 + this.sourceBands[i7])];
/* 1222 */           i4++;
/*      */         }
/*      */ 
/* 1229 */         i1 += (this.isBottomUp ? -j : j);
/* 1230 */         this.iis.skipBytes(n);
/* 1231 */         processImageUpdate(this.bi, 0, i2, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1234 */         processImageProgress(100.0F * i2 / this.destinationRegion.height);
/*      */ 
/* 1216 */         i2++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void read16Bit(short[] paramArrayOfShort)
/*      */     throws IOException
/*      */   {
/* 1242 */     int i = this.width * 2 % 4;
/*      */ 
/* 1244 */     if (i != 0) {
/* 1245 */       i = 4 - i;
/*      */     }
/* 1247 */     int j = this.width + i / 2;
/*      */     int m;
/* 1249 */     if (this.noTransform) {
/* 1250 */       int k = this.isBottomUp ? (this.height - 1) * this.width : 0;
/* 1251 */       for (m = 0; (m < this.height) && 
/* 1252 */         (!abortRequested()); m++)
/*      */       {
/* 1256 */         this.iis.readFully(paramArrayOfShort, k, this.width);
/* 1257 */         this.iis.skipBytes(i);
/*      */ 
/* 1259 */         k += (this.isBottomUp ? -this.width : this.width);
/* 1260 */         processImageUpdate(this.bi, 0, m, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1263 */         processImageProgress(100.0F * m / this.destinationRegion.height);
/*      */       }
/*      */     } else {
/* 1266 */       short[] arrayOfShort = new short[j];
/* 1267 */       m = ((SinglePixelPackedSampleModel)this.sampleModel).getScanlineStride();
/*      */ 
/* 1270 */       if (this.isBottomUp) {
/* 1271 */         n = this.sourceRegion.y + (this.destinationRegion.height - 1) * this.scaleY;
/*      */ 
/* 1273 */         this.iis.skipBytes(j * (this.height - 1 - n) << 1);
/*      */       } else {
/* 1275 */         this.iis.skipBytes(j * this.sourceRegion.y << 1);
/*      */       }
/* 1277 */       int n = j * (this.scaleY - 1) << 1;
/*      */ 
/* 1279 */       int i1 = this.destinationRegion.y * m;
/* 1280 */       if (this.isBottomUp)
/* 1281 */         i1 += (this.destinationRegion.height - 1) * m;
/* 1282 */       i1 += this.destinationRegion.x;
/*      */ 
/* 1284 */       int i2 = 0; int i3 = this.sourceRegion.y;
/* 1285 */       for (; i2 < this.destinationRegion.height; i3 += this.scaleY)
/*      */       {
/* 1287 */         if (abortRequested())
/*      */           break;
/* 1289 */         this.iis.readFully(arrayOfShort, 0, j);
/* 1290 */         int i4 = 0; int i5 = this.sourceRegion.x;
/* 1291 */         for (; i4 < this.destinationRegion.width; i5 += this.scaleX)
/*      */         {
/* 1293 */           paramArrayOfShort[(i1 + i4)] = arrayOfShort[i5];
/*      */ 
/* 1291 */           i4++;
/*      */         }
/*      */ 
/* 1296 */         i1 += (this.isBottomUp ? -m : m);
/* 1297 */         this.iis.skipBytes(n);
/* 1298 */         processImageUpdate(this.bi, 0, i2, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1301 */         processImageProgress(100.0F * i2 / this.destinationRegion.height);
/*      */ 
/* 1285 */         i2++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void read32Bit(int[] paramArrayOfInt)
/*      */     throws IOException
/*      */   {
/*      */     int j;
/* 1307 */     if (this.noTransform) {
/* 1308 */       int i = this.isBottomUp ? (this.height - 1) * this.width : 0;
/*      */ 
/* 1310 */       for (j = 0; (j < this.height) && 
/* 1311 */         (!abortRequested()); j++)
/*      */       {
/* 1314 */         this.iis.readFully(paramArrayOfInt, i, this.width);
/* 1315 */         i += (this.isBottomUp ? -this.width : this.width);
/* 1316 */         processImageUpdate(this.bi, 0, j, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1319 */         processImageProgress(100.0F * j / this.destinationRegion.height);
/*      */       }
/*      */     } else {
/* 1322 */       int[] arrayOfInt = new int[this.width];
/* 1323 */       j = ((SinglePixelPackedSampleModel)this.sampleModel).getScanlineStride();
/*      */ 
/* 1326 */       if (this.isBottomUp) {
/* 1327 */         k = this.sourceRegion.y + (this.destinationRegion.height - 1) * this.scaleY;
/*      */ 
/* 1329 */         this.iis.skipBytes(this.width * (this.height - 1 - k) << 2);
/*      */       } else {
/* 1331 */         this.iis.skipBytes(this.width * this.sourceRegion.y << 2);
/*      */       }
/* 1333 */       int k = this.width * (this.scaleY - 1) << 2;
/*      */ 
/* 1335 */       int m = this.destinationRegion.y * j;
/* 1336 */       if (this.isBottomUp)
/* 1337 */         m += (this.destinationRegion.height - 1) * j;
/* 1338 */       m += this.destinationRegion.x;
/*      */ 
/* 1340 */       int n = 0; int i1 = this.sourceRegion.y;
/* 1341 */       for (; n < this.destinationRegion.height; i1 += this.scaleY)
/*      */       {
/* 1343 */         if (abortRequested())
/*      */           break;
/* 1345 */         this.iis.readFully(arrayOfInt, 0, this.width);
/* 1346 */         int i2 = 0; int i3 = this.sourceRegion.x;
/* 1347 */         for (; i2 < this.destinationRegion.width; i3 += this.scaleX)
/*      */         {
/* 1349 */           paramArrayOfInt[(m + i2)] = arrayOfInt[i3];
/*      */ 
/* 1347 */           i2++;
/*      */         }
/*      */ 
/* 1352 */         m += (this.isBottomUp ? -j : j);
/* 1353 */         this.iis.skipBytes(k);
/* 1354 */         processImageUpdate(this.bi, 0, n, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1357 */         processImageProgress(100.0F * n / this.destinationRegion.height);
/*      */ 
/* 1341 */         n++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readRLE8(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1364 */     int i = (int)this.imageSize;
/* 1365 */     if (i == 0) {
/* 1366 */       i = (int)(this.bitmapFileSize - this.bitmapOffset);
/*      */     }
/*      */ 
/* 1369 */     int j = 0;
/*      */ 
/* 1372 */     int k = this.width % 4;
/* 1373 */     if (k != 0) {
/* 1374 */       j = 4 - k;
/*      */     }
/*      */ 
/* 1378 */     byte[] arrayOfByte = new byte[i];
/* 1379 */     int m = 0;
/* 1380 */     this.iis.readFully(arrayOfByte, 0, i);
/*      */ 
/* 1383 */     decodeRLE8(i, j, arrayOfByte, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   private void decodeRLE8(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*      */     throws IOException
/*      */   {
/* 1391 */     byte[] arrayOfByte = new byte[this.width * this.height];
/* 1392 */     int i = 0; int j = 0;
/*      */ 
/* 1394 */     int m = 0;
/* 1395 */     int n = this.isBottomUp ? this.height - 1 : 0;
/* 1396 */     int i1 = ((ComponentSampleModel)this.sampleModel).getScanlineStride();
/*      */ 
/* 1398 */     int i2 = 0;
/*      */ 
/* 1400 */     while (i != paramInt1) {
/* 1401 */       int k = paramArrayOfByte1[(i++)] & 0xFF;
/*      */       int i3;
/* 1402 */       if (k == 0)
/*      */       {
/*      */         int i4;
/*      */         int i5;
/* 1403 */         switch (paramArrayOfByte1[(i++)] & 0xFF)
/*      */         {
/*      */         case 0:
/* 1407 */           if ((n >= this.sourceRegion.y) && (n < this.sourceRegion.y + this.sourceRegion.height))
/*      */           {
/* 1409 */             if (this.noTransform) {
/* 1410 */               i3 = n * this.width;
/* 1411 */               for (i4 = 0; i4 < this.width; i4++)
/* 1412 */                 paramArrayOfByte2[(i3++)] = arrayOfByte[i4];
/* 1413 */               processImageUpdate(this.bi, 0, n, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1416 */               i2++;
/* 1417 */             } else if ((n - this.sourceRegion.y) % this.scaleY == 0) {
/* 1418 */               i3 = (n - this.sourceRegion.y) / this.scaleY + this.destinationRegion.y;
/*      */ 
/* 1420 */               i4 = i3 * i1;
/* 1421 */               i4 += this.destinationRegion.x;
/* 1422 */               i5 = this.sourceRegion.x;
/*      */ 
/* 1424 */               for (; i5 < this.sourceRegion.x + this.sourceRegion.width; 
/* 1424 */                 i5 += this.scaleX)
/* 1425 */                 paramArrayOfByte2[(i4++)] = arrayOfByte[i5];
/* 1426 */               processImageUpdate(this.bi, 0, i3, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1429 */               i2++;
/*      */             }
/*      */           }
/* 1432 */           processImageProgress(100.0F * i2 / this.destinationRegion.height);
/* 1433 */           n += (this.isBottomUp ? -1 : 1);
/* 1434 */           j = 0;
/*      */ 
/* 1436 */           if (abortRequested())
/* 1437 */             m = 1; break;
/*      */         case 1:
/* 1444 */           m = 1;
/* 1445 */           break;
/*      */         case 2:
/* 1449 */           i3 = paramArrayOfByte1[(i++)] & 0xFF;
/* 1450 */           i4 = paramArrayOfByte1[i] & 0xFF;
/*      */ 
/* 1452 */           j += i3 + i4 * this.width;
/* 1453 */           break;
/*      */         default:
/* 1456 */           i5 = paramArrayOfByte1[(i - 1)] & 0xFF;
/* 1457 */           for (int i6 = 0; i6 < i5; i6++) {
/* 1458 */             arrayOfByte[(j++)] = ((byte)(paramArrayOfByte1[(i++)] & 0xFF));
/*      */           }
/*      */ 
/* 1463 */           if ((i5 & 0x1) == 1)
/* 1464 */             i++;
/*      */           break;
/*      */         }
/*      */       } else {
/* 1468 */         for (i3 = 0; i3 < k; i3++) {
/* 1469 */           arrayOfByte[(j++)] = ((byte)(paramArrayOfByte1[i] & 0xFF));
/*      */         }
/*      */ 
/* 1472 */         i++;
/*      */       }
/*      */ 
/* 1476 */       if (m != 0)
/* 1477 */         break;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readRLE4(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1485 */     int i = (int)this.imageSize;
/* 1486 */     if (i == 0) {
/* 1487 */       i = (int)(this.bitmapFileSize - this.bitmapOffset);
/*      */     }
/*      */ 
/* 1490 */     int j = 0;
/*      */ 
/* 1493 */     int k = this.width % 4;
/* 1494 */     if (k != 0) {
/* 1495 */       j = 4 - k;
/*      */     }
/*      */ 
/* 1499 */     byte[] arrayOfByte = new byte[i];
/* 1500 */     this.iis.readFully(arrayOfByte, 0, i);
/*      */ 
/* 1503 */     decodeRLE4(i, j, arrayOfByte, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   private void decodeRLE4(int paramInt1, int paramInt2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*      */     throws IOException
/*      */   {
/* 1510 */     byte[] arrayOfByte = new byte[this.width];
/* 1511 */     int i = 0; int j = 0;
/*      */ 
/* 1513 */     int m = 0;
/* 1514 */     int n = this.isBottomUp ? this.height - 1 : 0;
/* 1515 */     int i1 = ((MultiPixelPackedSampleModel)this.sampleModel).getScanlineStride();
/*      */ 
/* 1517 */     int i2 = 0;
/*      */ 
/* 1519 */     while (i != paramInt1)
/*      */     {
/* 1521 */       int k = paramArrayOfByte1[(i++)] & 0xFF;
/*      */       int i4;
/* 1522 */       if (k == 0)
/*      */       {
/*      */         int i3;
/*      */         int i5;
/*      */         int i6;
/* 1526 */         switch (paramArrayOfByte1[(i++)] & 0xFF)
/*      */         {
/*      */         case 0:
/* 1531 */           if ((n >= this.sourceRegion.y) && (n < this.sourceRegion.y + this.sourceRegion.height))
/*      */           {
/* 1533 */             if (this.noTransform) {
/* 1534 */               i3 = n * (this.width + 1 >> 1);
/* 1535 */               i4 = 0; for (i5 = 0; i4 < this.width >> 1; i4++) {
/* 1536 */                 paramArrayOfByte2[(i3++)] = ((byte)(arrayOfByte[(i5++)] << 4 | arrayOfByte[(i5++)]));
/*      */               }
/* 1538 */               if ((this.width & 0x1) == 1)
/*      */               {
/*      */                 int tmp229_227 = i3;
/*      */                 byte[] tmp229_225 = paramArrayOfByte2; tmp229_225[tmp229_227] = ((byte)(tmp229_225[tmp229_227] | arrayOfByte[(this.width - 1)] << 4));
/*      */               }
/* 1541 */               processImageUpdate(this.bi, 0, n, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1544 */               i2++;
/* 1545 */             } else if ((n - this.sourceRegion.y) % this.scaleY == 0) {
/* 1546 */               i3 = (n - this.sourceRegion.y) / this.scaleY + this.destinationRegion.y;
/*      */ 
/* 1548 */               i4 = i3 * i1;
/* 1549 */               i4 += (this.destinationRegion.x >> 1);
/* 1550 */               i5 = 1 - (this.destinationRegion.x & 0x1) << 2;
/* 1551 */               i6 = this.sourceRegion.x;
/*      */ 
/* 1553 */               for (; i6 < this.sourceRegion.x + this.sourceRegion.width; 
/* 1553 */                 i6 += this.scaleX)
/*      */               {
/*      */                 int tmp391_389 = i4;
/*      */                 byte[] tmp391_387 = paramArrayOfByte2; tmp391_387[tmp391_389] = ((byte)(tmp391_387[tmp391_389] | arrayOfByte[i6] << i5));
/* 1555 */                 i5 += 4;
/* 1556 */                 if (i5 == 4) {
/* 1557 */                   i4++;
/*      */                 }
/* 1559 */                 i5 &= 7;
/*      */               }
/* 1561 */               processImageUpdate(this.bi, 0, i3, this.destinationRegion.width, 1, 1, 1, new int[] { 0 });
/*      */ 
/* 1564 */               i2++;
/*      */             }
/*      */           }
/* 1567 */           processImageProgress(100.0F * i2 / this.destinationRegion.height);
/* 1568 */           n += (this.isBottomUp ? -1 : 1);
/* 1569 */           j = 0;
/*      */ 
/* 1571 */           if (abortRequested())
/* 1572 */             m = 1; break;
/*      */         case 1:
/* 1579 */           m = 1;
/* 1580 */           break;
/*      */         case 2:
/* 1584 */           i3 = paramArrayOfByte1[(i++)] & 0xFF;
/* 1585 */           i4 = paramArrayOfByte1[i] & 0xFF;
/*      */ 
/* 1587 */           j += i3 + i4 * this.width;
/* 1588 */           break;
/*      */         default:
/* 1591 */           i5 = paramArrayOfByte1[(i - 1)] & 0xFF;
/* 1592 */           for (i6 = 0; i6 < i5; i6++) {
/* 1593 */             arrayOfByte[(j++)] = ((byte)((i6 & 0x1) == 0 ? (paramArrayOfByte1[i] & 0xF0) >> 4 : paramArrayOfByte1[(i++)] & 0xF));
/*      */           }
/*      */ 
/* 1599 */           if ((i5 & 0x1) == 1) {
/* 1600 */             i++;
/*      */           }
/*      */ 
/* 1605 */           if (((int)Math.ceil(i5 / 2) & 0x1) == 1)
/* 1606 */             i++;
/*      */           break;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1612 */         int[] arrayOfInt = { (paramArrayOfByte1[i] & 0xF0) >> 4, paramArrayOfByte1[i] & 0xF };
/*      */ 
/* 1614 */         for (i4 = 0; (i4 < k) && (j < this.width); i4++) {
/* 1615 */           arrayOfByte[(j++)] = ((byte)arrayOfInt[(i4 & 0x1)]);
/*      */         }
/*      */ 
/* 1618 */         i++;
/*      */       }
/*      */ 
/* 1622 */       if (m != 0)
/* 1623 */         break;  }  } 
/*      */   private BufferedImage readEmbedded(int paramInt, BufferedImage paramBufferedImage, ImageReadParam paramImageReadParam) throws IOException { // Byte code:
/*      */     //   0: iload_1
/*      */     //   1: lookupswitch	default:+41->42, 4:+27->28, 5:+34->35
/*      */     //   29: ldc_w 14852
/*      */     //   32: goto +37 -> 69
/*      */     //   35: ldc 20
/*      */     //   37: astore 4
/*      */     //   39: goto +30 -> 69
/*      */     //   42: new 388	java/io/IOException
/*      */     //   45: dup
/*      */     //   46: new 397	java/lang/StringBuilder
/*      */     //   49: dup
/*      */     //   50: invokespecial 786	java/lang/StringBuilder:<init>	()V
/*      */     //   53: ldc 21
/*      */     //   55: invokevirtual 789	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   58: iload_1
/*      */     //   59: invokevirtual 788	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*      */     //   62: invokevirtual 787	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   65: invokespecial 777	java/io/IOException:<init>	(Ljava/lang/String;)V
/*      */     //   68: athrow
/*      */     //   69: aload 4
/*      */     //   71: invokestatic 794	javax/imageio/ImageIO:getImageReadersByFormatName	(Ljava/lang/String;)Ljava/util/Iterator;
/*      */     //   74: invokeinterface 824 1 0
/*      */     //   79: checkcast 404	javax/imageio/ImageReader
/*      */     //   82: astore 5
/*      */     //   84: aload 5
/*      */     //   86: ifnonnull +39 -> 125
/*      */     //   89: new 395	java/lang/RuntimeException
/*      */     //   92: dup
/*      */     //   93: new 397	java/lang/StringBuilder
/*      */     //   96: dup
/*      */     //   97: invokespecial 786	java/lang/StringBuilder:<init>	()V
/*      */     //   100: ldc 14
/*      */     //   102: invokestatic 744	com/sun/imageio/plugins/common/I18N:getString	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   105: invokevirtual 789	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   108: ldc 4
/*      */     //   110: invokevirtual 789	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   113: aload 4
/*      */     //   115: invokevirtual 789	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   118: invokevirtual 787	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   121: invokespecial 784	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
/*      */     //   124: athrow
/*      */     //   125: aload_0
/*      */     //   126: getfield 655	com/sun/imageio/plugins/bmp/BMPImageReader:imageSize	J
/*      */     //   129: l2i
/*      */     //   130: newarray byte
/*      */     //   132: astore 6
/*      */     //   134: aload_0
/*      */     //   135: getfield 674	com/sun/imageio/plugins/bmp/BMPImageReader:iis	Ljavax/imageio/stream/ImageInputStream;
/*      */     //   138: aload 6
/*      */     //   140: invokeinterface 833 2 0
/*      */     //   145: pop
/*      */     //   146: aload 5
/*      */     //   148: new 387	java/io/ByteArrayInputStream
/*      */     //   151: dup
/*      */     //   152: aload 6
/*      */     //   154: invokespecial 776	java/io/ByteArrayInputStream:<init>	([B)V
/*      */     //   157: invokestatic 795	javax/imageio/ImageIO:createImageInputStream	(Ljava/lang/Object;)Ljavax/imageio/stream/ImageInputStream;
/*      */     //   160: invokevirtual 813	javax/imageio/ImageReader:setInput	(Ljava/lang/Object;)V
/*      */     //   163: aload_2
/*      */     //   164: ifnonnull +55 -> 219
/*      */     //   167: aload 5
/*      */     //   169: iconst_0
/*      */     //   170: invokevirtual 815	javax/imageio/ImageReader:getImageTypes	(I)Ljava/util/Iterator;
/*      */     //   173: invokeinterface 824 1 0
/*      */     //   178: checkcast 405	javax/imageio/ImageTypeSpecifier
/*      */     //   181: astore 7
/*      */     //   183: aload 7
/*      */     //   185: aload_0
/*      */     //   186: getfield 665	com/sun/imageio/plugins/bmp/BMPImageReader:destinationRegion	Ljava/awt/Rectangle;
/*      */     //   189: getfield 706	java/awt/Rectangle:x	I
/*      */     //   192: aload_0
/*      */     //   193: getfield 665	com/sun/imageio/plugins/bmp/BMPImageReader:destinationRegion	Ljava/awt/Rectangle;
/*      */     //   196: getfield 705	java/awt/Rectangle:width	I
/*      */     //   199: iadd
/*      */     //   200: aload_0
/*      */     //   201: getfield 665	com/sun/imageio/plugins/bmp/BMPImageReader:destinationRegion	Ljava/awt/Rectangle;
/*      */     //   204: getfield 707	java/awt/Rectangle:y	I
/*      */     //   207: aload_0
/*      */     //   208: getfield 665	com/sun/imageio/plugins/bmp/BMPImageReader:destinationRegion	Ljava/awt/Rectangle;
/*      */     //   211: getfield 704	java/awt/Rectangle:height	I
/*      */     //   214: iadd
/*      */     //   215: invokevirtual 822	javax/imageio/ImageTypeSpecifier:createBufferedImage	(II)Ljava/awt/image/BufferedImage;
/*      */     //   218: astore_2
/*      */     //   219: aload 5
/*      */     //   221: new 360	com/sun/imageio/plugins/bmp/BMPImageReader$1
/*      */     //   224: dup
/*      */     //   225: aload_0
/*      */     //   226: invokespecial 738	com/sun/imageio/plugins/bmp/BMPImageReader$1:<init>	(Lcom/sun/imageio/plugins/bmp/BMPImageReader;)V
/*      */     //   229: invokevirtual 817	javax/imageio/ImageReader:addIIOReadProgressListener	(Ljavax/imageio/event/IIOReadProgressListener;)V
/*      */     //   232: aload 5
/*      */     //   234: new 361	com/sun/imageio/plugins/bmp/BMPImageReader$2
/*      */     //   237: dup
/*      */     //   238: aload_0
/*      */     //   239: invokespecial 739	com/sun/imageio/plugins/bmp/BMPImageReader$2:<init>	(Lcom/sun/imageio/plugins/bmp/BMPImageReader;)V
/*      */     //   242: invokevirtual 818	javax/imageio/ImageReader:addIIOReadUpdateListener	(Ljavax/imageio/event/IIOReadUpdateListener;)V
/*      */     //   245: aload 5
/*      */     //   247: new 362	com/sun/imageio/plugins/bmp/BMPImageReader$3
/*      */     //   250: dup
/*      */     //   251: aload_0
/*      */     //   252: invokespecial 740	com/sun/imageio/plugins/bmp/BMPImageReader$3:<init>	(Lcom/sun/imageio/plugins/bmp/BMPImageReader;)V
/*      */     //   255: invokevirtual 819	javax/imageio/ImageReader:addIIOReadWarningListener	(Ljavax/imageio/event/IIOReadWarningListener;)V
/*      */     //   258: aload 5
/*      */     //   260: invokevirtual 816	javax/imageio/ImageReader:getDefaultReadParam	()Ljavax/imageio/ImageReadParam;
/*      */     //   263: astore 7
/*      */     //   265: aload 7
/*      */     //   267: aload_2
/*      */     //   268: invokevirtual 811	javax/imageio/ImageReadParam:setDestination	(Ljava/awt/image/BufferedImage;)V
/*      */     //   271: aload 7
/*      */     //   273: aload_3
/*      */     //   274: invokevirtual 801	javax/imageio/ImageReadParam:getDestinationBands	()[I
/*      */     //   277: invokevirtual 804	javax/imageio/ImageReadParam:setDestinationBands	([I)V
/*      */     //   280: aload 7
/*      */     //   282: aload_3
/*      */     //   283: invokevirtual 806	javax/imageio/ImageReadParam:getDestinationOffset	()Ljava/awt/Point;
/*      */     //   286: invokevirtual 807	javax/imageio/ImageReadParam:setDestinationOffset	(Ljava/awt/Point;)V
/*      */     //   289: aload 7
/*      */     //   291: aload_3
/*      */     //   292: invokevirtual 802	javax/imageio/ImageReadParam:getSourceBands	()[I
/*      */     //   295: invokevirtual 805	javax/imageio/ImageReadParam:setSourceBands	([I)V
/*      */     //   298: aload 7
/*      */     //   300: aload_3
/*      */     //   301: invokevirtual 808	javax/imageio/ImageReadParam:getSourceRegion	()Ljava/awt/Rectangle;
/*      */     //   304: invokevirtual 809	javax/imageio/ImageReadParam:setSourceRegion	(Ljava/awt/Rectangle;)V
/*      */     //   307: aload 7
/*      */     //   309: aload_3
/*      */     //   310: invokevirtual 796	javax/imageio/ImageReadParam:getSourceXSubsampling	()I
/*      */     //   313: aload_3
/*      */     //   314: invokevirtual 797	javax/imageio/ImageReadParam:getSourceYSubsampling	()I
/*      */     //   317: aload_3
/*      */     //   318: invokevirtual 798	javax/imageio/ImageReadParam:getSubsamplingXOffset	()I
/*      */     //   321: aload_3
/*      */     //   322: invokevirtual 799	javax/imageio/ImageReadParam:getSubsamplingYOffset	()I
/*      */     //   325: invokevirtual 803	javax/imageio/ImageReadParam:setSourceSubsampling	(IIII)V
/*      */     //   328: aload 5
/*      */     //   330: iconst_0
/*      */     //   331: aload 7
/*      */     //   333: invokevirtual 821	javax/imageio/ImageReader:read	(ILjavax/imageio/ImageReadParam;)Ljava/awt/image/BufferedImage;
/*      */     //   336: pop
/*      */     //   337: aload_2
/*      */     //   338: areturn } 
/* 1760 */   private static boolean isLinkedProfileAllowed() { if (isLinkedProfileDisabled == null) {
/* 1761 */       PrivilegedAction local4 = new PrivilegedAction() {
/*      */         public Boolean run() {
/* 1763 */           return Boolean.valueOf(Boolean.getBoolean("sun.imageio.plugins.bmp.disableLinkedProfiles"));
/*      */         }
/*      */       };
/* 1766 */       isLinkedProfileDisabled = (Boolean)AccessController.doPrivileged(local4);
/*      */     }
/* 1768 */     return !isLinkedProfileDisabled.booleanValue();
/*      */   }
/*      */ 
/*      */   private static boolean isUncOrDevicePath(byte[] paramArrayOfByte)
/*      */   {
/* 1785 */     if (isWindowsPlatform == null) {
/* 1786 */       PrivilegedAction local5 = new PrivilegedAction() {
/*      */         public Boolean run() {
/* 1788 */           String str = System.getProperty("os.name");
/* 1789 */           return Boolean.valueOf((str != null) && (str.toLowerCase().startsWith("win")));
/*      */         }
/*      */       };
/* 1793 */       isWindowsPlatform = (Boolean)AccessController.doPrivileged(local5);
/*      */     }
/*      */ 
/* 1796 */     if (!isWindowsPlatform.booleanValue())
/*      */     {
/* 1798 */       return false;
/*      */     }
/*      */ 
/* 1802 */     if (paramArrayOfByte[0] == 47) paramArrayOfByte[0] = 92;
/* 1803 */     if (paramArrayOfByte[1] == 47) paramArrayOfByte[1] = 92;
/* 1804 */     if (paramArrayOfByte[3] == 47) paramArrayOfByte[3] = 92;
/*      */ 
/* 1807 */     if ((paramArrayOfByte[0] == 92) && (paramArrayOfByte[1] == 92)) {
/* 1808 */       if ((paramArrayOfByte[2] == 63) && (paramArrayOfByte[3] == 92))
/*      */       {
/* 1810 */         return ((paramArrayOfByte[4] == 85) || (paramArrayOfByte[4] == 117)) && ((paramArrayOfByte[5] == 78) || (paramArrayOfByte[5] == 110)) && ((paramArrayOfByte[6] == 67) || (paramArrayOfByte[6] == 99));
/*      */       }
/*      */ 
/* 1815 */       return true;
/*      */     }
/*      */ 
/* 1818 */     return false;
/*      */   }
/*      */ 
/*      */   private class EmbeddedProgressAdapter
/*      */     implements IIOReadProgressListener
/*      */   {
/*      */     private EmbeddedProgressAdapter()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void imageComplete(ImageReader paramImageReader)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void imageProgress(ImageReader paramImageReader, float paramFloat)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void imageStarted(ImageReader paramImageReader, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void thumbnailComplete(ImageReader paramImageReader)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void thumbnailProgress(ImageReader paramImageReader, float paramFloat)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void thumbnailStarted(ImageReader paramImageReader, int paramInt1, int paramInt2)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void sequenceComplete(ImageReader paramImageReader)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void sequenceStarted(ImageReader paramImageReader, int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void readAborted(ImageReader paramImageReader)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.bmp.BMPImageReader
 * JD-Core Version:    0.6.2
 */
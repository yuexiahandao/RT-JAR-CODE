/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class GifImageDecoder extends ImageDecoder
/*     */ {
/*     */   private static final boolean verbose = false;
/*     */   private static final int IMAGESEP = 44;
/*     */   private static final int EXBLOCK = 33;
/*     */   private static final int EX_GRAPHICS_CONTROL = 249;
/*     */   private static final int EX_COMMENT = 254;
/*     */   private static final int EX_APPLICATION = 255;
/*     */   private static final int TERMINATOR = 59;
/*     */   private static final int TRANSPARENCYMASK = 1;
/*     */   private static final int INTERLACEMASK = 64;
/*     */   private static final int COLORMAPMASK = 128;
/*     */   int num_global_colors;
/*     */   byte[] global_colormap;
/*  61 */   int trans_pixel = -1;
/*     */   IndexColorModel global_model;
/*  64 */   Hashtable props = new Hashtable();
/*     */   byte[] saved_image;
/*     */   IndexColorModel saved_model;
/*     */   int global_width;
/*     */   int global_height;
/*     */   int global_bgpixel;
/*     */   GifFrame curframe;
/*     */   private static final int normalflags = 30;
/*     */   private static final int interlaceflags = 29;
/* 342 */   private short[] prefix = new short[4096];
/* 343 */   private byte[] suffix = new byte[4096];
/* 344 */   private byte[] outCode = new byte[4097];
/*     */ 
/*     */   public GifImageDecoder(InputStreamImageSource paramInputStreamImageSource, InputStream paramInputStream)
/*     */   {
/*  76 */     super(paramInputStreamImageSource, paramInputStream);
/*     */   }
/*     */ 
/*     */   private static void error(String paramString)
/*     */     throws ImageFormatException
/*     */   {
/*  83 */     throw new ImageFormatException(paramString);
/*     */   }
/*     */ 
/*     */   private int readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*     */     while (true)
/*  91 */       if (paramInt2 > 0)
/*     */         try {
/*  93 */           int i = this.input.read(paramArrayOfByte, paramInt1, paramInt2);
/*  94 */           if (i >= 0)
/*     */           {
/*  97 */             paramInt1 += i;
/*  98 */             paramInt2 -= i;
/*     */           }
/*     */         }
/*     */         catch (IOException localIOException) {
/*     */         }
/* 103 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   private static final int ExtractByte(byte[] paramArrayOfByte, int paramInt) {
/* 107 */     return paramArrayOfByte[paramInt] & 0xFF;
/*     */   }
/*     */ 
/*     */   private static final int ExtractWord(byte[] paramArrayOfByte, int paramInt) {
/* 111 */     return paramArrayOfByte[paramInt] & 0xFF | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 8;
/*     */   }
/*     */ 
/*     */   public void produceImage()
/*     */     throws IOException, ImageFormatException
/*     */   {
/*     */     try
/*     */     {
/* 119 */       readHeader();
/*     */ 
/* 121 */       int i = 0;
/* 122 */       int j = 0;
/* 123 */       int k = -1;
/* 124 */       int m = 0;
/* 125 */       int n = -1;
/* 126 */       int i1 = 0;
/* 127 */       int i2 = 0;
/*     */ 
/* 129 */       while (!this.aborted)
/*     */       {
/*     */         int i3;
/* 132 */         switch (i3 = this.input.read()) {
/*     */         case 33:
/* 134 */           switch (i3 = this.input.read()) {
/*     */           case 249:
/* 136 */             byte[] arrayOfByte1 = new byte[6];
/* 137 */             if (readBytes(arrayOfByte1, 0, 6) != 0) {
/*     */               return;
/*     */             }
/* 140 */             if ((arrayOfByte1[0] != 4) || (arrayOfByte1[5] != 0))
/*     */             {
/*     */               return;
/*     */             }
/* 144 */             n = ExtractWord(arrayOfByte1, 2) * 10;
/* 145 */             if ((n > 0) && (i2 == 0)) {
/* 146 */               i2 = 1;
/* 147 */               ImageFetcher.startingAnimation();
/*     */             }
/* 149 */             m = arrayOfByte1[1] >> 2 & 0x7;
/* 150 */             if ((arrayOfByte1[1] & 0x1) != 0)
/* 151 */               this.trans_pixel = ExtractByte(arrayOfByte1, 4);
/*     */             else {
/* 153 */               this.trans_pixel = -1;
/*     */             }
/* 155 */             break;
/*     */           case 254:
/*     */           case 255:
/*     */           default:
/* 161 */             int i4 = 0;
/* 162 */             String str = "";
/*     */             while (true) {
/* 164 */               int i5 = this.input.read();
/* 165 */               if (i5 <= 0) {
/*     */                 break;
/*     */               }
/* 168 */               byte[] arrayOfByte2 = new byte[i5];
/* 169 */               if (readBytes(arrayOfByte2, 0, i5) != 0) {
/*     */                 return;
/*     */               }
/* 172 */               if (i3 == 254) {
/* 173 */                 str = str + new String(arrayOfByte2, 0);
/* 174 */               } else if (i3 == 255) {
/* 175 */                 if (i4 != 0) {
/* 176 */                   if ((i5 == 3) && (arrayOfByte2[0] == 1)) {
/* 177 */                     if (i1 != 0) {
/* 178 */                       ExtractWord(arrayOfByte2, 1);
/*     */                     }
/*     */                     else {
/* 181 */                       k = ExtractWord(arrayOfByte2, 1);
/* 182 */                       i1 = 1;
/*     */                     }
/*     */                   }
/* 185 */                   else i4 = 0;
/*     */                 }
/*     */ 
/* 188 */                 if ("NETSCAPE2.0".equals(new String(arrayOfByte2, 0))) {
/* 189 */                   i4 = 1;
/*     */                 }
/*     */               }
/*     */             }
/* 193 */             if (i3 == 254) {
/* 194 */               this.props.put("comment", str);
/*     */             }
/* 196 */             if ((i4 != 0) && (i2 == 0)) {
/* 197 */               i2 = 1;
/* 198 */               ImageFetcher.startingAnimation(); } break;
/*     */           case -1:
/*     */             return;
/*     */           }
/*     */ 
/* 205 */           break;
/*     */         case 44:
/* 208 */           if (i2 == 0)
/* 209 */             this.input.mark(0);
/*     */           try
/*     */           {
/* 212 */             if (!readImage(i == 0, m, n))
/*     */             {
/*     */               return;
/*     */             }
/*     */ 
/*     */           }
/*     */           catch (Exception localException)
/*     */           {
/*     */             return;
/*     */           }
/*     */ 
/* 223 */           j++;
/* 224 */           i++;
/* 225 */           break;
/*     */         case -1:
/*     */         default:
/* 238 */           if (j == 0) {
/*     */             return;
/*     */           }
/*     */           break;
/*     */         case 59:
/*     */         }
/* 244 */         if ((k == 0) || (k-- >= 0)) {
/*     */           try {
/* 246 */             if (this.curframe != null) {
/* 247 */               this.curframe.dispose();
/* 248 */               this.curframe = null;
/*     */             }
/* 250 */             this.input.reset();
/* 251 */             this.saved_image = null;
/* 252 */             this.saved_model = null;
/* 253 */             j = 0;
/*     */           }
/*     */           catch (IOException localIOException)
/*     */           {
/*     */             return;
/*     */           }
/*     */ 
/*     */         }
/*     */         else {
/* 264 */           imageComplete(3, true);
/*     */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     finally {
/* 269 */       close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readHeader()
/*     */     throws IOException, ImageFormatException
/*     */   {
/* 278 */     byte[] arrayOfByte = new byte[13];
/*     */ 
/* 281 */     if (readBytes(arrayOfByte, 0, 13) != 0) {
/* 282 */       throw new IOException();
/*     */     }
/*     */ 
/* 286 */     if ((arrayOfByte[0] != 71) || (arrayOfByte[1] != 73) || (arrayOfByte[2] != 70)) {
/* 287 */       error("not a GIF file.");
/*     */     }
/*     */ 
/* 291 */     this.global_width = ExtractWord(arrayOfByte, 6);
/* 292 */     this.global_height = ExtractWord(arrayOfByte, 8);
/*     */ 
/* 295 */     int i = ExtractByte(arrayOfByte, 10);
/* 296 */     if ((i & 0x80) == 0)
/*     */     {
/* 303 */       this.num_global_colors = 2;
/* 304 */       this.global_bgpixel = 0;
/* 305 */       this.global_colormap = new byte[6];
/*     */       int tmp124_123 = (this.global_colormap[2] = 0); this.global_colormap[1] = tmp124_123; this.global_colormap[0] = tmp124_123;
/*     */       byte tmp145_144 = (this.global_colormap[5] = -1); this.global_colormap[4] = tmp145_144; this.global_colormap[3] = tmp145_144;
/*     */     }
/*     */     else
/*     */     {
/* 311 */       this.num_global_colors = (1 << (i & 0x7) + 1);
/*     */ 
/* 313 */       this.global_bgpixel = ExtractByte(arrayOfByte, 11);
/*     */ 
/* 315 */       if (arrayOfByte[12] != 0) {
/* 316 */         this.props.put("aspectratio", "" + (ExtractByte(arrayOfByte, 12) + 15) / 64.0D);
/*     */       }
/*     */ 
/* 320 */       this.global_colormap = new byte[this.num_global_colors * 3];
/* 321 */       if (readBytes(this.global_colormap, 0, this.num_global_colors * 3) != 0) {
/* 322 */         throw new IOException();
/*     */       }
/*     */     }
/* 325 */     this.input.mark(2147483647);
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   private native boolean parseImage(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean, int paramInt5, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, IndexColorModel paramIndexColorModel);
/*     */ 
/*     */   private int sendPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, ColorModel paramColorModel)
/*     */   {
/* 362 */     if (paramInt2 < 0) {
/* 363 */       paramInt4 += paramInt2;
/* 364 */       paramInt2 = 0;
/*     */     }
/* 366 */     if (paramInt2 + paramInt4 > this.global_height) {
/* 367 */       paramInt4 = this.global_height - paramInt2;
/*     */     }
/* 369 */     if (paramInt4 <= 0)
/* 370 */       return 1;
/*     */     int i;
/*     */     int k;
/* 374 */     if (paramInt1 < 0) {
/* 375 */       i = -paramInt1;
/* 376 */       paramInt3 += paramInt1;
/* 377 */       k = 0;
/*     */     } else {
/* 379 */       i = 0;
/*     */ 
/* 381 */       k = paramInt1;
/*     */     }
/*     */ 
/* 386 */     if (k + paramInt3 > this.global_width) {
/* 387 */       paramInt3 = this.global_width - k;
/*     */     }
/* 389 */     if (paramInt3 <= 0) {
/* 390 */       return 1;
/*     */     }
/* 392 */     int j = i + paramInt3;
/*     */ 
/* 395 */     int m = paramInt2 * this.global_width + k;
/* 396 */     int n = this.curframe.disposal_method == 1 ? 1 : 0;
/* 397 */     if ((this.trans_pixel >= 0) && (!this.curframe.initialframe))
/*     */     {
/*     */       int i2;
/* 398 */       if ((this.saved_image != null) && (paramColorModel.equals(this.saved_model))) {
/* 399 */         for (i1 = i; i1 < j; m++) {
/* 400 */           i2 = paramArrayOfByte[i1];
/* 401 */           if ((i2 & 0xFF) == this.trans_pixel)
/* 402 */             paramArrayOfByte[i1] = this.saved_image[m];
/* 403 */           else if (n != 0)
/* 404 */             this.saved_image[m] = i2;
/* 399 */           i1++;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 412 */         i1 = -1;
/* 413 */         i2 = 1;
/* 414 */         for (int i3 = i; i3 < j; m++) {
/* 415 */           int i4 = paramArrayOfByte[i3];
/* 416 */           if ((i4 & 0xFF) == this.trans_pixel) {
/* 417 */             if (i1 >= 0) {
/* 418 */               i2 = setPixels(paramInt1 + i1, paramInt2, i3 - i1, 1, paramColorModel, paramArrayOfByte, i1, 0);
/*     */ 
/* 422 */               if (i2 == 0) {
/*     */                 break;
/*     */               }
/*     */             }
/* 426 */             i1 = -1;
/*     */           } else {
/* 428 */             if (i1 < 0) {
/* 429 */               i1 = i3;
/*     */             }
/* 431 */             if (n != 0)
/* 432 */               this.saved_image[m] = i4;
/*     */           }
/* 414 */           i3++;
/*     */         }
/*     */ 
/* 436 */         if (i1 >= 0) {
/* 437 */           i2 = setPixels(paramInt1 + i1, paramInt2, j - i1, 1, paramColorModel, paramArrayOfByte, i1, 0);
/*     */         }
/*     */ 
/* 442 */         return i2;
/*     */       }
/* 444 */     } else if (n != 0) {
/* 445 */       System.arraycopy(paramArrayOfByte, i, this.saved_image, m, paramInt3);
/*     */     }
/* 447 */     int i1 = setPixels(k, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, i, 0);
/*     */ 
/* 449 */     return i1;
/*     */   }
/*     */ 
/*     */   private boolean readImage(boolean paramBoolean, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 458 */     if ((this.curframe != null) && (!this.curframe.dispose())) {
/* 459 */       abort();
/* 460 */       return false;
/*     */     }
/*     */ 
/* 463 */     long l = 0L;
/*     */ 
/* 470 */     byte[] arrayOfByte1 = new byte[259];
/*     */ 
/* 473 */     if (readBytes(arrayOfByte1, 0, 10) != 0) {
/* 474 */       throw new IOException();
/*     */     }
/* 476 */     int i = ExtractWord(arrayOfByte1, 0);
/* 477 */     int j = ExtractWord(arrayOfByte1, 2);
/* 478 */     int k = ExtractWord(arrayOfByte1, 4);
/* 479 */     int m = ExtractWord(arrayOfByte1, 6);
/*     */ 
/* 492 */     if ((k == 0) && (this.global_width != 0)) {
/* 493 */       k = this.global_width - i;
/*     */     }
/* 495 */     if ((m == 0) && (this.global_height != 0)) {
/* 496 */       m = this.global_height - j;
/*     */     }
/*     */ 
/* 499 */     boolean bool1 = (arrayOfByte1[8] & 0x40) != 0;
/*     */ 
/* 501 */     IndexColorModel localIndexColorModel = this.global_model;
/*     */ 
/* 503 */     if ((arrayOfByte1[8] & 0x80) != 0)
/*     */     {
/* 507 */       n = 1 << (arrayOfByte1[8] & 0x7) + 1;
/*     */ 
/* 510 */       arrayOfByte2 = new byte[n * 3];
/* 511 */       arrayOfByte2[0] = arrayOfByte1[9];
/* 512 */       if (readBytes(arrayOfByte2, 1, n * 3 - 1) != 0) {
/* 513 */         throw new IOException();
/*     */       }
/*     */ 
/* 518 */       if (readBytes(arrayOfByte1, 9, 1) != 0) {
/* 519 */         throw new IOException();
/*     */       }
/* 521 */       if (this.trans_pixel >= n)
/*     */       {
/* 523 */         n = this.trans_pixel + 1;
/* 524 */         arrayOfByte2 = grow_colormap(arrayOfByte2, n);
/*     */       }
/* 526 */       localIndexColorModel = new IndexColorModel(8, n, arrayOfByte2, 0, false, this.trans_pixel);
/*     */     }
/* 528 */     else if ((localIndexColorModel == null) || (this.trans_pixel != localIndexColorModel.getTransparentPixel()))
/*     */     {
/* 530 */       if (this.trans_pixel >= this.num_global_colors)
/*     */       {
/* 532 */         this.num_global_colors = (this.trans_pixel + 1);
/* 533 */         this.global_colormap = grow_colormap(this.global_colormap, this.num_global_colors);
/*     */       }
/* 535 */       localIndexColorModel = new IndexColorModel(8, this.num_global_colors, this.global_colormap, 0, false, this.trans_pixel);
/*     */ 
/* 537 */       this.global_model = localIndexColorModel;
/*     */     }
/*     */ 
/* 541 */     if (paramBoolean) {
/* 542 */       if (this.global_width == 0) this.global_width = k;
/* 543 */       if (this.global_height == 0) this.global_height = m;
/*     */ 
/* 545 */       setDimensions(this.global_width, this.global_height);
/* 546 */       setProperties(this.props);
/* 547 */       setColorModel(localIndexColorModel);
/* 548 */       headerComplete();
/*     */     }
/*     */ 
/* 551 */     if ((paramInt1 == 1) && (this.saved_image == null)) {
/* 552 */       this.saved_image = new byte[this.global_width * this.global_height];
/*     */ 
/* 557 */       if ((m < this.global_height) && (localIndexColorModel != null)) {
/* 558 */         n = (byte)localIndexColorModel.getTransparentPixel();
/* 559 */         if (n >= 0) {
/* 560 */           arrayOfByte2 = new byte[this.global_width];
/* 561 */           for (i1 = 0; i1 < this.global_width; i1++) {
/* 562 */             arrayOfByte2[i1] = n;
/*     */           }
/*     */ 
/* 565 */           setPixels(0, 0, this.global_width, j, localIndexColorModel, arrayOfByte2, 0, 0);
/*     */ 
/* 567 */           setPixels(0, j + m, this.global_width, this.global_height - m - j, localIndexColorModel, arrayOfByte2, 0, 0);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 574 */     int n = bool1 ? 29 : 30;
/* 575 */     setHints(n);
/*     */ 
/* 577 */     this.curframe = new GifFrame(this, paramInt1, paramInt2, this.curframe == null, localIndexColorModel, i, j, k, m);
/*     */ 
/* 582 */     byte[] arrayOfByte2 = new byte[k];
/*     */ 
/* 588 */     int i1 = ExtractByte(arrayOfByte1, 9);
/* 589 */     if (i1 >= 12)
/*     */     {
/* 594 */       return false;
/*     */     }
/* 596 */     boolean bool2 = parseImage(i, j, k, m, bool1, i1, arrayOfByte1, arrayOfByte2, localIndexColorModel);
/*     */ 
/* 600 */     if (!bool2) {
/* 601 */       abort();
/*     */     }
/*     */ 
/* 610 */     return bool2;
/*     */   }
/*     */ 
/*     */   public static byte[] grow_colormap(byte[] paramArrayOfByte, int paramInt) {
/* 614 */     byte[] arrayOfByte = new byte[paramInt * 3];
/* 615 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
/* 616 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 350 */     NativeLibLoader.loadLibraries();
/* 351 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.GifImageDecoder
 * JD-Core Version:    0.6.2
 */
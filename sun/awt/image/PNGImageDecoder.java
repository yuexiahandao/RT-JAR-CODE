/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Hashtable;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ 
/*     */ public class PNGImageDecoder extends ImageDecoder
/*     */ {
/*     */   private static final int GRAY = 0;
/*     */   private static final int PALETTE = 1;
/*     */   private static final int COLOR = 2;
/*     */   private static final int ALPHA = 4;
/*     */   private static final int bKGDChunk = 1649100612;
/*     */   private static final int cHRMChunk = 1665684045;
/*     */   private static final int gAMAChunk = 1732332865;
/*     */   private static final int hISTChunk = 1749635924;
/*     */   private static final int IDATChunk = 1229209940;
/*     */   private static final int IENDChunk = 1229278788;
/*     */   private static final int IHDRChunk = 1229472850;
/*     */   private static final int PLTEChunk = 1347179589;
/*     */   private static final int pHYsChunk = 1883789683;
/*     */   private static final int sBITChunk = 1933723988;
/*     */   private static final int tEXtChunk = 1950701684;
/*     */   private static final int tIMEChunk = 1950960965;
/*     */   private static final int tRNSChunk = 1951551059;
/*     */   private static final int zTXtChunk = 2052348020;
/*     */   private int width;
/*     */   private int height;
/*     */   private int bitDepth;
/*     */   private int colorType;
/*     */   private int compressionMethod;
/*     */   private int filterMethod;
/*     */   private int interlaceMethod;
/*  70 */   private int gamma = 100000;
/*     */   private Hashtable properties;
/*     */   private ColorModel cm;
/*     */   private byte[] red_map;
/*     */   private byte[] green_map;
/*     */   private byte[] blue_map;
/*     */   private byte[] alpha_map;
/*  77 */   private int transparentPixel = -1;
/*  78 */   private byte[] transparentPixel_16 = null;
/*  79 */   private static ColorModel[] greyModels = new ColorModel[4];
/*     */ 
/* 615 */   private static final byte[] startingRow = { 0, 0, 0, 4, 0, 2, 0, 1 };
/* 616 */   private static final byte[] startingCol = { 0, 0, 4, 0, 2, 0, 1, 0 };
/* 617 */   private static final byte[] rowIncrement = { 1, 8, 8, 8, 4, 4, 2, 2 };
/* 618 */   private static final byte[] colIncrement = { 1, 8, 8, 4, 4, 2, 2, 1 };
/* 619 */   private static final byte[] blockHeight = { 1, 8, 8, 4, 4, 2, 2, 1 };
/* 620 */   private static final byte[] blockWidth = { 1, 8, 4, 4, 2, 2, 1, 1 };
/*     */   int pos;
/*     */   int limit;
/*     */   int chunkStart;
/*     */   int chunkKey;
/*     */   int chunkLength;
/*     */   int chunkCRC;
/*     */   boolean seenEOF;
/* 628 */   private static final byte[] signature = { -119, 80, 78, 71, 13, 10, 26, 10 };
/*     */   PNGFilterInputStream inputStream;
/*     */   InputStream underlyingInputStream;
/* 663 */   byte[] inbuf = new byte[4096];
/*     */ 
/* 732 */   private static boolean checkCRC = true;
/*     */ 
/* 753 */   private static final int[] crc_table = new int[256];
/*     */ 
/*     */   private void property(String paramString, Object paramObject)
/*     */   {
/*  85 */     if (paramObject == null) return;
/*  86 */     if (this.properties == null) this.properties = new Hashtable();
/*  87 */     this.properties.put(paramString, paramObject);
/*     */   }
/*     */   private void property(String paramString, float paramFloat) {
/*  90 */     property(paramString, new Float(paramFloat));
/*     */   }
/*     */   private final void pngassert(boolean paramBoolean) throws IOException {
/*  93 */     if (!paramBoolean) {
/*  94 */       PNGException localPNGException = new PNGException("Broken file");
/*  95 */       localPNGException.printStackTrace();
/*  96 */       throw localPNGException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean handleChunk(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */     throws IOException
/*     */   {
/*     */     int i;
/*     */     int j;
/* 101 */     switch (paramInt1) {
/*     */     case 1649100612:
/* 103 */       Color localColor = null;
/* 104 */       switch (this.colorType) {
/*     */       case 2:
/*     */       case 6:
/* 107 */         pngassert(paramInt3 == 6);
/* 108 */         localColor = new Color(paramArrayOfByte[paramInt2] & 0xFF, paramArrayOfByte[(paramInt2 + 2)] & 0xFF, paramArrayOfByte[(paramInt2 + 4)] & 0xFF);
/* 109 */         break;
/*     */       case 3:
/*     */       case 7:
/* 112 */         pngassert(paramInt3 == 1);
/* 113 */         i = paramArrayOfByte[paramInt2] & 0xFF;
/* 114 */         pngassert((this.red_map != null) && (i < this.red_map.length));
/* 115 */         localColor = new Color(this.red_map[i] & 0xFF, this.green_map[i] & 0xFF, this.blue_map[i] & 0xFF);
/* 116 */         break;
/*     */       case 0:
/*     */       case 4:
/* 119 */         pngassert(paramInt3 == 2);
/* 120 */         j = paramArrayOfByte[paramInt2] & 0xFF;
/* 121 */         localColor = new Color(j, j, j);
/*     */       case 1:
/*     */       case 5:
/* 124 */       }if (localColor != null) property("background", localColor); break;
/*     */     case 1665684045:
/* 127 */       property("chromaticities", new Chromaticities(getInt(paramInt2), getInt(paramInt2 + 4), getInt(paramInt2 + 8), getInt(paramInt2 + 12), getInt(paramInt2 + 16), getInt(paramInt2 + 20), getInt(paramInt2 + 24), getInt(paramInt2 + 28)));
/*     */ 
/* 137 */       break;
/*     */     case 1732332865:
/* 139 */       if (paramInt3 != 4) throw new PNGException("bogus gAMA");
/* 140 */       this.gamma = getInt(paramInt2);
/* 141 */       if (this.gamma != 100000) property("gamma", this.gamma / 100000.0F); break;
/*     */     case 1749635924:
/* 143 */       break;
/*     */     case 1229209940:
/* 144 */       return false;
/*     */     case 1229278788:
/* 145 */       break;
/*     */     case 1229472850:
/* 147 */       if ((paramInt3 != 13) || ((this.width = getInt(paramInt2)) == 0) || ((this.height = getInt(paramInt2 + 4)) == 0))
/*     */       {
/* 150 */         throw new PNGException("bogus IHDR");
/* 151 */       }this.bitDepth = getByte(paramInt2 + 8);
/* 152 */       this.colorType = getByte(paramInt2 + 9);
/* 153 */       this.compressionMethod = getByte(paramInt2 + 10);
/* 154 */       this.filterMethod = getByte(paramInt2 + 11);
/* 155 */       this.interlaceMethod = getByte(paramInt2 + 12);
/*     */ 
/* 159 */       break;
/*     */     case 1347179589:
/* 161 */       i = paramInt3 / 3;
/* 162 */       this.red_map = new byte[i];
/* 163 */       this.green_map = new byte[i];
/* 164 */       this.blue_map = new byte[i];
/* 165 */       j = 0; for (int m = paramInt2; j < i; m += 3) {
/* 166 */         this.red_map[j] = paramArrayOfByte[m];
/* 167 */         this.green_map[j] = paramArrayOfByte[(m + 1)];
/* 168 */         this.blue_map[j] = paramArrayOfByte[(m + 2)];
/*     */ 
/* 165 */         j++;
/*     */       }
/*     */ 
/* 171 */       break;
/*     */     case 1883789683:
/* 172 */       break;
/*     */     case 1933723988:
/* 173 */       break;
/*     */     case 1950701684:
/* 175 */       i = 0;
/* 176 */       while ((i < paramInt3) && (paramArrayOfByte[(paramInt2 + i)] != 0)) i++;
/* 177 */       if (i < paramInt3) {
/* 178 */         String str1 = new String(paramArrayOfByte, paramInt2, i);
/* 179 */         String str2 = new String(paramArrayOfByte, paramInt2 + i + 1, paramInt3 - i - 1);
/* 180 */         property(str1, str2);
/* 181 */       }break;
/*     */     case 1950960965:
/* 184 */       property("modtime", new GregorianCalendar(getShort(paramInt2 + 0), getByte(paramInt2 + 2) - 1, getByte(paramInt2 + 3), getByte(paramInt2 + 4), getByte(paramInt2 + 5), getByte(paramInt2 + 6)).getTime());
/*     */ 
/* 191 */       break;
/*     */     case 1951551059:
/*     */       int n;
/* 193 */       switch (this.colorType) {
/*     */       case 3:
/*     */       case 7:
/* 196 */         int k = paramInt3;
/* 197 */         if (this.red_map != null) k = this.red_map.length;
/* 198 */         this.alpha_map = new byte[k];
/* 199 */         System.arraycopy(paramArrayOfByte, paramInt2, this.alpha_map, 0, paramInt3 < k ? paramInt3 : k);
/*     */         while (true) { k--; if (k < paramInt3) break; this.alpha_map[k] = -1;
/*     */         }
/*     */       case 2:
/*     */       case 6:
/* 204 */         pngassert(paramInt3 == 6);
/* 205 */         if (this.bitDepth == 16) {
/* 206 */           this.transparentPixel_16 = new byte[6];
/* 207 */           for (n = 0; n < 6; n++)
/* 208 */             this.transparentPixel_16[n] = ((byte)getByte(paramInt2 + n));
/*     */         }
/*     */         else {
/* 211 */           this.transparentPixel = ((getShort(paramInt2 + 0) & 0xFF) << 16 | (getShort(paramInt2 + 2) & 0xFF) << 8 | getShort(paramInt2 + 4) & 0xFF);
/*     */         }
/*     */ 
/* 216 */         break;
/*     */       case 0:
/*     */       case 4:
/* 219 */         pngassert(paramInt3 == 2);
/*     */ 
/* 224 */         n = getShort(paramInt2);
/* 225 */         n = 0xFF & (this.bitDepth == 16 ? n >> 8 : n);
/* 226 */         this.transparentPixel = (n << 16 | n << 8 | n);
/*     */       case 1:
/*     */       case 5:
/* 229 */       }break;
/*     */     case 2052348020:
/*     */     }
/* 232 */     return true;
/*     */   }
/*     */ 
/*     */   public void produceImage()
/*     */     throws IOException, ImageFormatException
/*     */   {
/*     */     try
/*     */     {
/* 246 */       for (int i = 0; i < signature.length; i++) {
/* 247 */         if ((signature[i] & 0xFF) != this.underlyingInputStream.read())
/* 248 */           throw new PNGException("Chunk signature mismatch");
/*     */       }
/* 250 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream(new InflaterInputStream(this.inputStream, new Inflater()));
/*     */ 
/* 252 */       getData();
/*     */ 
/* 254 */       byte[] arrayOfByte1 = null;
/* 255 */       int[] arrayOfInt = null;
/* 256 */       int j = this.width;
/*     */ 
/* 258 */       int m = 0;
/* 259 */       switch (this.bitDepth) { case 1:
/* 260 */         m = 0; break;
/*     */       case 2:
/* 261 */         m = 1; break;
/*     */       case 4:
/* 262 */         m = 2; break;
/*     */       case 8:
/* 263 */         m = 3; break;
/*     */       case 16:
/* 264 */         m = 4; break;
/*     */       default:
/* 265 */         throw new PNGException("invalid depth");
/*     */       }
/* 267 */       int k;
/* 267 */       if (this.interlaceMethod != 0) { j *= this.height; k = this.width; } else {
/* 268 */         k = 0;
/* 269 */       }int n = this.colorType | this.bitDepth << 3;
/* 270 */       int i1 = (1 << (this.bitDepth >= 8 ? 8 : this.bitDepth)) - 1;
/*     */ 
/* 272 */       switch (this.colorType) {
/*     */       case 3:
/*     */       case 7:
/* 275 */         if (this.red_map == null) throw new PNGException("palette expected");
/* 276 */         if (this.alpha_map == null) {
/* 277 */           this.cm = new IndexColorModel(this.bitDepth, this.red_map.length, this.red_map, this.green_map, this.blue_map);
/*     */         }
/*     */         else {
/* 280 */           this.cm = new IndexColorModel(this.bitDepth, this.red_map.length, this.red_map, this.green_map, this.blue_map, this.alpha_map);
/*     */         }
/* 282 */         arrayOfByte1 = new byte[j];
/* 283 */         break;
/*     */       case 0:
/* 285 */         i2 = m >= 4 ? 3 : m;
/* 286 */         if ((this.cm = greyModels[i2]) == null) {
/* 287 */           i3 = 1 << (1 << i2);
/*     */ 
/* 289 */           byte[] arrayOfByte2 = new byte[i3];
/* 290 */           for (i5 = 0; i5 < i3; i5++) arrayOfByte2[i5] = ((byte)(255 * i5 / (i3 - 1)));
/*     */ 
/* 292 */           if (this.transparentPixel == -1)
/* 293 */             this.cm = new IndexColorModel(this.bitDepth, arrayOfByte2.length, arrayOfByte2, arrayOfByte2, arrayOfByte2);
/*     */           else {
/* 295 */             this.cm = new IndexColorModel(this.bitDepth, arrayOfByte2.length, arrayOfByte2, arrayOfByte2, arrayOfByte2, this.transparentPixel & 0xFF);
/*     */           }
/*     */ 
/* 298 */           greyModels[i2] = this.cm;
/*     */         }
/*     */ 
/* 301 */         arrayOfByte1 = new byte[j];
/* 302 */         break;
/*     */       case 2:
/*     */       case 4:
/*     */       case 6:
/* 306 */         this.cm = ColorModel.getRGBdefault();
/* 307 */         arrayOfInt = new int[j];
/* 308 */         break;
/*     */       case 1:
/*     */       case 5:
/*     */       default:
/* 310 */         throw new PNGException("invalid color type");
/*     */       }
/*     */ 
/* 320 */       setDimensions(this.width, this.height);
/* 321 */       setColorModel(this.cm);
/* 322 */       int i2 = this.interlaceMethod != 0 ? 6 : 30;
/*     */ 
/* 326 */       setHints(i2);
/* 327 */       headerComplete();
/*     */ 
/* 330 */       int i3 = (this.colorType & 0x1) != 0 ? 1 : ((this.colorType & 0x2) != 0 ? 3 : 1) + ((this.colorType & 0x4) != 0 ? 1 : 0);
/*     */ 
/* 332 */       int i4 = i3 * this.bitDepth;
/* 333 */       int i5 = i4 + 7 >> 3;
/*     */       int i6;
/*     */       int i7;
/* 335 */       if (this.interlaceMethod == 0) { i6 = -1; i7 = 0; } else {
/* 336 */         i6 = 0; i7 = 7;
/*     */       }
/*     */ 
/*     */       while (true)
/*     */       {
/* 342 */         i6++; if (i6 > i7) break;
/* 343 */         int i8 = startingRow[i6];
/* 344 */         int i9 = rowIncrement[i6];
/* 345 */         int i10 = colIncrement[i6];
/* 346 */         int i11 = blockWidth[i6];
/* 347 */         int i12 = blockHeight[i6];
/* 348 */         int i13 = startingCol[i6];
/* 349 */         int i14 = (this.width - i13 + (i10 - 1)) / i10;
/* 350 */         int i15 = i14 * i4 + 7 >> 3;
/* 351 */         if (i15 != 0) {
/* 352 */           int i16 = this.interlaceMethod == 0 ? i9 * this.width : 0;
/* 353 */           int i17 = k * i8;
/* 354 */           int i18 = 1;
/*     */ 
/* 356 */           Object localObject1 = new byte[i15];
/* 357 */           Object localObject2 = new byte[i15];
/*     */ 
/* 361 */           while (i8 < this.height) {
/* 362 */             int i19 = localBufferedInputStream.read();
/* 363 */             for (int i20 = 0; i20 < i15; ) {
/* 364 */               i21 = localBufferedInputStream.read((byte[])localObject1, i20, i15 - i20);
/* 365 */               if (i21 <= 0) throw new PNGException("missing data");
/* 366 */               i20 += i21;
/*     */             }
/* 368 */             filterRow((byte[])localObject1, i18 != 0 ? null : (byte[])localObject2, i19, i15, i5);
/*     */ 
/* 371 */             i20 = i13;
/* 372 */             int i21 = 0;
/* 373 */             int i22 = 0;
/* 374 */             while (i20 < this.width) {
/* 375 */               if (arrayOfInt != null)
/*     */               {
/*     */                 int i24;
/* 376 */                 switch (n) {
/*     */                 case 70:
/* 378 */                   arrayOfInt[(i20 + i17)] = ((localObject1[i21] & 0xFF) << 16 | (localObject1[(i21 + 1)] & 0xFF) << 8 | localObject1[(i21 + 2)] & 0xFF | (localObject1[(i21 + 3)] & 0xFF) << 24);
/*     */ 
/* 383 */                   i21 += 4;
/* 384 */                   break;
/*     */                 case 134:
/* 386 */                   arrayOfInt[(i20 + i17)] = ((localObject1[i21] & 0xFF) << 16 | (localObject1[(i21 + 2)] & 0xFF) << 8 | localObject1[(i21 + 4)] & 0xFF | (localObject1[(i21 + 6)] & 0xFF) << 24);
/*     */ 
/* 391 */                   i21 += 8;
/* 392 */                   break;
/*     */                 case 66:
/* 394 */                   i22 = (localObject1[i21] & 0xFF) << 16 | (localObject1[(i21 + 1)] & 0xFF) << 8 | localObject1[(i21 + 2)] & 0xFF;
/*     */ 
/* 398 */                   if (i22 != this.transparentPixel) {
/* 399 */                     i22 |= -16777216;
/*     */                   }
/* 401 */                   arrayOfInt[(i20 + i17)] = i22;
/* 402 */                   i21 += 3;
/* 403 */                   break;
/*     */                 case 130:
/* 405 */                   i22 = (localObject1[i21] & 0xFF) << 16 | (localObject1[(i21 + 2)] & 0xFF) << 8 | localObject1[(i21 + 4)] & 0xFF;
/*     */ 
/* 410 */                   int i23 = this.transparentPixel_16 != null ? 1 : 0;
/* 411 */                   for (i24 = 0; (i23 != 0) && (i24 < 6); i24++) {
/* 412 */                     i23 &= ((localObject1[(i21 + i24)] & 0xFF) == (this.transparentPixel_16[i24] & 0xFF) ? 1 : 0);
/*     */                   }
/*     */ 
/* 415 */                   if (i23 == 0) {
/* 416 */                     i22 |= -16777216;
/*     */                   }
/* 418 */                   arrayOfInt[(i20 + i17)] = i22;
/* 419 */                   i21 += 6;
/* 420 */                   break;
/*     */                 case 68:
/* 422 */                   i24 = localObject1[i21] & 0xFF;
/* 423 */                   arrayOfInt[(i20 + i17)] = (i24 << 16 | i24 << 8 | i24 | (localObject1[(i21 + 1)] & 0xFF) << 24);
/*     */ 
/* 426 */                   i21 += 2;
/* 427 */                   break;
/*     */                 case 132:
/* 429 */                   i24 = localObject1[i21] & 0xFF;
/* 430 */                   arrayOfInt[(i20 + i17)] = (i24 << 16 | i24 << 8 | i24 | (localObject1[(i21 + 2)] & 0xFF) << 24);
/*     */ 
/* 433 */                   i21 += 4;
/* 434 */                   break;
/*     */                 default:
/* 435 */                   throw new PNGException("illegal type/depth");
/*     */                 }
/*     */               } else { switch (this.bitDepth) {
/*     */                 case 1:
/* 439 */                   arrayOfByte1[(i20 + i17)] = ((byte)(localObject1[(i21 >> 3)] >> 7 - (i21 & 0x7) & 0x1));
/*     */ 
/* 441 */                   i21++;
/* 442 */                   break;
/*     */                 case 2:
/* 444 */                   arrayOfByte1[(i20 + i17)] = ((byte)(localObject1[(i21 >> 2)] >> (3 - (i21 & 0x3)) * 2 & 0x3));
/*     */ 
/* 446 */                   i21++;
/* 447 */                   break;
/*     */                 case 4:
/* 449 */                   arrayOfByte1[(i20 + i17)] = ((byte)(localObject1[(i21 >> 1)] >> (1 - (i21 & 0x1)) * 4 & 0xF));
/*     */ 
/* 451 */                   i21++;
/* 452 */                   break;
/*     */                 case 8:
/* 453 */                   arrayOfByte1[(i20 + i17)] = localObject1[(i21++)];
/* 454 */                   break;
/*     */                 case 16:
/* 455 */                   arrayOfByte1[(i20 + i17)] = localObject1[i21]; i21 += 2;
/* 456 */                   break;
/*     */                 default:
/* 457 */                   throw new PNGException("illegal type/depth");
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/* 462 */               i20 += i10;
/*     */             }
/* 464 */             if (this.interlaceMethod == 0) {
/* 465 */               if (arrayOfInt != null)
/*     */               {
/* 470 */                 sendPixels(0, i8, this.width, 1, arrayOfInt, 0, this.width);
/*     */               }
/*     */               else
/*     */               {
/* 478 */                 sendPixels(0, i8, this.width, 1, arrayOfByte1, 0, this.width);
/*     */               }
/*     */             }
/* 481 */             i8 += i9;
/* 482 */             i17 += i9 * k;
/* 483 */             Object localObject3 = localObject1;
/* 484 */             localObject1 = localObject2;
/* 485 */             localObject2 = localObject3;
/* 486 */             i18 = 0;
/*     */           }
/* 488 */           if (this.interlaceMethod != 0) {
/* 489 */             if (arrayOfInt != null)
/*     */             {
/* 494 */               sendPixels(0, 0, this.width, this.height, arrayOfInt, 0, this.width);
/*     */             }
/*     */             else
/*     */             {
/* 502 */               sendPixels(0, 0, this.width, this.height, arrayOfByte1, 0, this.width);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 519 */       imageComplete(3, true);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 525 */       if (!this.aborted)
/*     */       {
/* 530 */         property("error", localIOException);
/*     */ 
/* 535 */         imageComplete(3, true);
/* 536 */         throw localIOException;
/*     */       }
/*     */     } finally {
/*     */       try { close(); }
/*     */       catch (Throwable localThrowable3)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean sendPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 549 */     int i = setPixels(paramInt1, paramInt2, paramInt3, paramInt4, this.cm, paramArrayOfInt, paramInt5, paramInt6);
/*     */ 
/* 551 */     if (i <= 0) {
/* 552 */       this.aborted = true;
/*     */     }
/* 554 */     return !this.aborted;
/*     */   }
/*     */ 
/*     */   private boolean sendPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6) {
/* 558 */     int i = setPixels(paramInt1, paramInt2, paramInt3, paramInt4, this.cm, paramArrayOfByte, paramInt5, paramInt6);
/*     */ 
/* 560 */     if (i <= 0) {
/* 561 */       this.aborted = true;
/*     */     }
/* 563 */     return !this.aborted;
/*     */   }
/*     */ 
/*     */   private void filterRow(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException
/*     */   {
/* 569 */     int i = 0;
/* 570 */     switch (paramInt1) {
/*     */     case 0:
/* 572 */       break;
/*     */     case 1:
/* 574 */       for (i = paramInt3; i < paramInt2; )
/*     */       {
/*     */         int tmp57_55 = i;
/*     */         byte[] tmp57_54 = paramArrayOfByte1; tmp57_54[tmp57_55] = ((byte)(tmp57_54[tmp57_55] + paramArrayOfByte1[(i - paramInt3)]));
/*     */ 
/* 574 */         i++; continue;
/*     */ 
/* 578 */         if (paramArrayOfByte2 != null)
/* 579 */           while (i < paramInt2)
/*     */           {
/*     */             int tmp89_87 = i;
/*     */             byte[] tmp89_86 = paramArrayOfByte1; tmp89_86[tmp89_87] = ((byte)(tmp89_86[tmp89_87] + paramArrayOfByte2[i]));
/*     */ 
/* 579 */             i++; continue;
/*     */ 
/* 583 */             if (paramArrayOfByte2 != null) {
/* 584 */               for (; i < paramInt3; i++)
/*     */               {
/*     */                 int tmp118_116 = i;
/*     */                 byte[] tmp118_115 = paramArrayOfByte1; tmp118_115[tmp118_116] = ((byte)(tmp118_115[tmp118_116] + ((0xFF & paramArrayOfByte2[i]) >> 1)));
/* 586 */               }for (; i < paramInt2; i++)
/*     */               {
/*     */                 int tmp149_147 = i;
/*     */                 byte[] tmp149_146 = paramArrayOfByte1; tmp149_146[tmp149_147] = ((byte)(tmp149_146[tmp149_147] + ((paramArrayOfByte2[i] & 0xFF) + (paramArrayOfByte1[(i - paramInt3)] & 0xFF) >> 1)));
/*     */               }
/*     */             }
/* 589 */             for (i = paramInt3; i < paramInt2; )
/*     */             {
/*     */               int tmp196_194 = i;
/*     */               byte[] tmp196_193 = paramArrayOfByte1; tmp196_193[tmp196_194] = ((byte)(tmp196_193[tmp196_194] + ((paramArrayOfByte1[(i - paramInt3)] & 0xFF) >> 1)));
/*     */ 
/* 589 */               i++; continue;
/*     */ 
/* 593 */               if (paramArrayOfByte2 != null) {
/* 594 */                 for (; i < paramInt3; i++)
/*     */                 {
/*     */                   int tmp234_232 = i;
/*     */                   byte[] tmp234_231 = paramArrayOfByte1; tmp234_231[tmp234_232] = ((byte)(tmp234_231[tmp234_232] + paramArrayOfByte2[i]));
/* 596 */                 }for (; i < paramInt2; i++)
/*     */                 {
/* 598 */                   int j = paramArrayOfByte1[(i - paramInt3)] & 0xFF;
/* 599 */                   int k = paramArrayOfByte2[i] & 0xFF;
/* 600 */                   int m = paramArrayOfByte2[(i - paramInt3)] & 0xFF;
/* 601 */                   int n = j + k - m;
/* 602 */                   int i1 = n > j ? n - j : j - n;
/* 603 */                   int i2 = n > k ? n - k : k - n;
/* 604 */                   int i3 = n > m ? n - m : m - n;
/*     */                   int tmp371_369 = i;
/*     */                   byte[] tmp371_368 = paramArrayOfByte1; tmp371_368[tmp371_369] = ((byte)(tmp371_368[tmp371_369] + (i2 <= i3 ? k : (i1 <= i2) && (i1 <= i3) ? j : m)));
/*     */                 }
/*     */               }
/* 608 */               for (i = paramInt3; i < paramInt2; )
/*     */               {
/*     */                 int tmp429_427 = i;
/*     */                 byte[] tmp429_426 = paramArrayOfByte1; tmp429_426[tmp429_427] = ((byte)(tmp429_426[tmp429_427] + paramArrayOfByte1[(i - paramInt3)]));
/*     */ 
/* 608 */                 i++; continue;
/*     */ 
/* 612 */                 throw new PNGException("Illegal filter");
/*     */               }
/*     */             }
/*     */           }
/*     */       }
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     }
/*     */   }
/*     */ 
/*     */   public PNGImageDecoder(InputStreamImageSource paramInputStreamImageSource, InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 639 */     super(paramInputStreamImageSource, paramInputStream);
/* 640 */     this.inputStream = new PNGFilterInputStream(this, paramInputStream);
/* 641 */     this.underlyingInputStream = this.inputStream.underlyingInputStream;
/*     */   }
/*     */ 
/*     */   private void fill()
/*     */     throws IOException
/*     */   {
/* 665 */     if (!this.seenEOF) {
/* 666 */       if ((this.pos > 0) && (this.pos < this.limit)) {
/* 667 */         System.arraycopy(this.inbuf, this.pos, this.inbuf, 0, this.limit - this.pos);
/* 668 */         this.limit -= this.pos;
/* 669 */         this.pos = 0;
/* 670 */       } else if (this.pos >= this.limit) {
/* 671 */         this.pos = 0; this.limit = 0;
/*     */       }
/* 673 */       int i = this.inbuf.length;
/* 674 */       while (this.limit < i) {
/* 675 */         int j = this.underlyingInputStream.read(this.inbuf, this.limit, i - this.limit);
/* 676 */         if (j <= 0) { this.seenEOF = true; break; }
/* 677 */         this.limit += j;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 682 */   private boolean need(int paramInt) throws IOException { if (this.limit - this.pos >= paramInt) return true;
/* 683 */     fill();
/* 684 */     if (this.limit - this.pos >= paramInt) return true;
/* 685 */     if (this.seenEOF) return false;
/* 686 */     byte[] arrayOfByte = new byte[paramInt + 100];
/* 687 */     System.arraycopy(this.inbuf, this.pos, arrayOfByte, 0, this.limit - this.pos);
/* 688 */     this.limit -= this.pos;
/* 689 */     this.pos = 0;
/* 690 */     this.inbuf = arrayOfByte;
/* 691 */     fill();
/* 692 */     return this.limit - this.pos >= paramInt; }
/*     */ 
/*     */   private final int getInt(int paramInt) {
/* 695 */     return (this.inbuf[paramInt] & 0xFF) << 24 | (this.inbuf[(paramInt + 1)] & 0xFF) << 16 | (this.inbuf[(paramInt + 2)] & 0xFF) << 8 | this.inbuf[(paramInt + 3)] & 0xFF;
/*     */   }
/*     */ 
/*     */   private final int getShort(int paramInt)
/*     */   {
/* 701 */     return (short)((this.inbuf[paramInt] & 0xFF) << 8 | this.inbuf[(paramInt + 1)] & 0xFF);
/*     */   }
/*     */ 
/*     */   private final int getByte(int paramInt) {
/* 705 */     return this.inbuf[paramInt] & 0xFF;
/*     */   }
/*     */   private final boolean getChunk() throws IOException {
/* 708 */     this.chunkLength = 0;
/* 709 */     if (!need(8)) return false;
/* 710 */     this.chunkLength = getInt(this.pos);
/* 711 */     this.chunkKey = getInt(this.pos + 4);
/* 712 */     if (this.chunkLength < 0) throw new PNGException("bogus length: " + this.chunkLength);
/* 713 */     if (!need(this.chunkLength + 12)) return false;
/* 714 */     this.chunkCRC = getInt(this.pos + 8 + this.chunkLength);
/* 715 */     this.chunkStart = (this.pos + 8);
/* 716 */     int i = crc(this.inbuf, this.pos + 4, this.chunkLength + 4);
/* 717 */     if ((this.chunkCRC != i) && (checkCRC)) throw new PNGException("crc corruption");
/* 718 */     this.pos += this.chunkLength + 12;
/* 719 */     return true;
/*     */   }
/*     */   private void readAll() throws IOException {
/* 722 */     while (getChunk()) handleChunk(this.chunkKey, this.inbuf, this.chunkStart, this.chunkLength); 
/*     */   }
/*     */ 
/* 725 */   boolean getData() throws IOException { while ((this.chunkLength == 0) && (getChunk()))
/* 726 */       if (handleChunk(this.chunkKey, this.inbuf, this.chunkStart, this.chunkLength))
/* 727 */         this.chunkLength = 0;
/* 728 */     return this.chunkLength > 0;
/*     */   }
/*     */ 
/*     */   public static boolean getCheckCRC()
/*     */   {
/* 733 */     return checkCRC; } 
/* 734 */   public static void setCheckCRC(boolean paramBoolean) { checkCRC = paramBoolean; }
/*     */ 
/*     */   protected void wrc(int paramInt) {
/* 737 */     paramInt &= 255;
/* 738 */     if ((paramInt <= 32) || (paramInt > 122)) paramInt = 63;
/* 739 */     System.out.write(paramInt);
/*     */   }
/*     */   protected void wrk(int paramInt) {
/* 742 */     wrc(paramInt >> 24);
/* 743 */     wrc(paramInt >> 16);
/* 744 */     wrc(paramInt >> 8);
/* 745 */     wrc(paramInt);
/*     */   }
/*     */   public void print() {
/* 748 */     wrk(this.chunkKey);
/* 749 */     System.out.print(" " + this.chunkLength + "\n");
/*     */   }
/*     */ 
/*     */   private static int update_crc(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 774 */     int i = paramInt1;
/*     */     while (true) { paramInt3--; if (paramInt3 < 0) break;
/* 776 */       i = crc_table[((i ^ paramArrayOfByte[(paramInt2++)]) & 0xFF)] ^ i >>> 8; }
/* 777 */     return i;
/*     */   }
/*     */ 
/*     */   private static int crc(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 782 */     return update_crc(-1, paramArrayOfByte, paramInt1, paramInt2) ^ 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 757 */     for (int i = 0; i < 256; i++) {
/* 758 */       int j = i;
/* 759 */       for (int k = 0; k < 8; k++)
/* 760 */         if ((j & 0x1) != 0)
/* 761 */           j = 0xEDB88320 ^ j >>> 1;
/*     */         else
/* 763 */           j >>>= 1;
/* 764 */       crc_table[i] = j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Chromaticities
/*     */   {
/*     */     public float whiteX;
/*     */     public float whiteY;
/*     */     public float redX;
/*     */     public float redY;
/*     */     public float greenX;
/*     */     public float greenY;
/*     */     public float blueX;
/*     */     public float blueY;
/*     */ 
/*     */     Chromaticities(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
/* 787 */       this.whiteX = (paramInt1 / 100000.0F);
/* 788 */       this.whiteY = (paramInt2 / 100000.0F);
/* 789 */       this.redX = (paramInt3 / 100000.0F);
/* 790 */       this.redY = (paramInt4 / 100000.0F);
/* 791 */       this.greenX = (paramInt5 / 100000.0F);
/* 792 */       this.greenY = (paramInt6 / 100000.0F);
/* 793 */       this.blueX = (paramInt7 / 100000.0F);
/* 794 */       this.blueY = (paramInt8 / 100000.0F);
/*     */     }
/*     */     public String toString() {
/* 797 */       return "Chromaticities(white=" + this.whiteX + "," + this.whiteY + ";red=" + this.redX + "," + this.redY + ";green=" + this.greenX + "," + this.greenY + ";blue=" + this.blueX + "," + this.blueY + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   public class PNGException extends IOException
/*     */   {
/*     */     PNGException(String arg2)
/*     */     {
/* 235 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.PNGImageDecoder
 * JD-Core Version:    0.6.2
 */
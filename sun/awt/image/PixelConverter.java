/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ 
/*     */ public class PixelConverter
/*     */ {
/*  49 */   public static final PixelConverter instance = new PixelConverter();
/*     */ 
/*  52 */   protected int alphaMask = 0;
/*     */ 
/*     */   public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */   {
/*  57 */     Object localObject = paramColorModel.getDataElements(paramInt, null);
/*  58 */     switch (paramColorModel.getTransferType()) {
/*     */     case 0:
/*  60 */       byte[] arrayOfByte = (byte[])localObject;
/*  61 */       int i = 0;
/*     */ 
/*  63 */       switch (arrayOfByte.length) {
/*     */       default:
/*  65 */         i = arrayOfByte[3] << 24;
/*     */       case 3:
/*  68 */         i |= (arrayOfByte[2] & 0xFF) << 16;
/*     */       case 2:
/*  71 */         i |= (arrayOfByte[1] & 0xFF) << 8;
/*     */       case 1:
/*     */       }
/*  74 */       i |= arrayOfByte[0] & 0xFF;
/*     */ 
/*  77 */       return i;
/*     */     case 1:
/*     */     case 2:
/*  80 */       short[] arrayOfShort = (short[])localObject;
/*     */ 
/*  82 */       return (arrayOfShort.length > 1 ? arrayOfShort[1] << 16 : 0) | arrayOfShort[0] & 0xFFFF;
/*     */     case 3:
/*  85 */       return ((int[])(int[])localObject)[0];
/*     */     }
/*  87 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */   {
/*  93 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public final int getAlphaMask() {
/*  97 */     return this.alphaMask;
/*     */   }
/*     */ 
/*     */   public static class Argb extends PixelConverter
/*     */   {
/* 151 */     public static final PixelConverter instance = new Argb();
/*     */ 
/*     */     private Argb() {
/* 154 */       this.alphaMask = -16777216;
/*     */     }
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel) {
/* 158 */       return paramInt;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel) {
/* 162 */       return paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ArgbBm extends PixelConverter
/*     */   {
/* 389 */     public static final PixelConverter instance = new ArgbBm();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 394 */       return paramInt | paramInt >> 31 << 24;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel) {
/* 398 */       return paramInt << 7 >> 7;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ArgbPre extends PixelConverter
/*     */   {
/* 353 */     public static final PixelConverter instance = new ArgbPre();
/*     */ 
/*     */     private ArgbPre() {
/* 356 */       this.alphaMask = -16777216;
/*     */     }
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel) {
/* 360 */       if (paramInt >> 24 == -1) {
/* 361 */         return paramInt;
/*     */       }
/* 363 */       int i = paramInt >>> 24;
/* 364 */       int j = paramInt >> 16 & 0xFF;
/* 365 */       int k = paramInt >> 8 & 0xFF;
/* 366 */       int m = paramInt & 0xFF;
/* 367 */       int n = i + (i >> 7);
/* 368 */       j = j * n >> 8;
/* 369 */       k = k * n >> 8;
/* 370 */       m = m * n >> 8;
/* 371 */       return i << 24 | j << 16 | k << 8 | m;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel) {
/* 375 */       int i = paramInt >>> 24;
/* 376 */       if ((i == 255) || (i == 0)) {
/* 377 */         return paramInt;
/*     */       }
/* 379 */       int j = paramInt >> 16 & 0xFF;
/* 380 */       int k = paramInt >> 8 & 0xFF;
/* 381 */       int m = paramInt & 0xFF;
/* 382 */       j = ((j << 8) - j) / i;
/* 383 */       k = ((k << 8) - k) / i;
/* 384 */       m = ((m << 8) - m) / i;
/* 385 */       return i << 24 | j << 16 | k << 8 | m;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Bgrx extends PixelConverter
/*     */   {
/* 284 */     public static final PixelConverter instance = new Bgrx();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 289 */       return paramInt << 24 | (paramInt & 0xFF00) << 8 | paramInt >> 8 & 0xFF00;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 295 */       return 0xFF000000 | (paramInt & 0xFF00) << 8 | paramInt >> 8 & 0xFF00 | paramInt >>> 24;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ByteGray extends PixelConverter
/*     */   {
/*     */     static final double RED_MULT = 0.299D;
/*     */     static final double GRN_MULT = 0.587D;
/*     */     static final double BLU_MULT = 0.114D;
/* 405 */     public static final PixelConverter instance = new ByteGray();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 410 */       int i = paramInt >> 16 & 0xFF;
/* 411 */       int j = paramInt >> 8 & 0xFF;
/* 412 */       int k = paramInt & 0xFF;
/* 413 */       return (int)(i * 0.299D + j * 0.587D + k * 0.114D + 0.5D);
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 420 */       return ((0xFF00 | paramInt) << 8 | paramInt) << 8 | paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Rgba extends PixelConverter
/*     */   {
/* 302 */     public static final PixelConverter instance = new Rgba();
/*     */ 
/*     */     private Rgba() {
/* 305 */       this.alphaMask = 255;
/*     */     }
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel) {
/* 309 */       return paramInt << 8 | paramInt >>> 24;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel) {
/* 313 */       return paramInt << 24 | paramInt >>> 8;
/*     */     }
/*     */   }
/*     */ 
/* 317 */   public static class RgbaPre extends PixelConverter { public static final PixelConverter instance = new RgbaPre();
/*     */ 
/*     */     private RgbaPre() {
/* 320 */       this.alphaMask = 255;
/*     */     }
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel) {
/* 324 */       if (paramInt >> 24 == -1) {
/* 325 */         return paramInt << 8 | paramInt >>> 24;
/*     */       }
/* 327 */       int i = paramInt >>> 24;
/* 328 */       int j = paramInt >> 16 & 0xFF;
/* 329 */       int k = paramInt >> 8 & 0xFF;
/* 330 */       int m = paramInt & 0xFF;
/* 331 */       int n = i + (i >> 7);
/* 332 */       j = j * n >> 8;
/* 333 */       k = k * n >> 8;
/* 334 */       m = m * n >> 8;
/* 335 */       return j << 24 | k << 16 | m << 8 | i;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel) {
/* 339 */       int i = paramInt & 0xFF;
/* 340 */       if ((i == 255) || (i == 0)) {
/* 341 */         return paramInt >>> 8 | paramInt << 24;
/*     */       }
/* 343 */       int j = paramInt >>> 24;
/* 344 */       int k = paramInt >> 16 & 0xFF;
/* 345 */       int m = paramInt >> 8 & 0xFF;
/* 346 */       j = ((j << 8) - j) / i;
/* 347 */       k = ((k << 8) - k) / i;
/* 348 */       m = ((m << 8) - m) / i;
/* 349 */       return j << 24 | k << 16 | m << 8 | i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Rgbx extends PixelConverter
/*     */   {
/* 125 */     public static final PixelConverter instance = new Rgbx();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 130 */       return paramInt << 8;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel) {
/* 134 */       return 0xFF000000 | paramInt >> 8;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Ushort4444Argb extends PixelConverter
/*     */   {
/* 232 */     public static final PixelConverter instance = new Ushort4444Argb();
/*     */ 
/*     */     private Ushort4444Argb() {
/* 235 */       this.alphaMask = 61440;
/*     */     }
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 241 */       int i = paramInt >> 16 & 0xF000;
/* 242 */       int j = paramInt >> 12 & 0xF00;
/* 243 */       int k = paramInt >> 8 & 0xF0;
/* 244 */       int m = paramInt >> 4 & 0xF;
/*     */ 
/* 246 */       return i | j | k | m;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 253 */       int i = paramInt & 0xF000;
/* 254 */       i = (paramInt << 16 | paramInt << 12) & 0xFF000000;
/* 255 */       int j = paramInt & 0xF00;
/* 256 */       j = (paramInt << 12 | paramInt << 8) & 0xFF0000;
/* 257 */       int k = paramInt & 0xF0;
/* 258 */       k = (paramInt << 8 | paramInt << 4) & 0xFF00;
/* 259 */       int m = paramInt & 0xF;
/* 260 */       m = (paramInt << 4 | paramInt << 0) & 0xFF;
/*     */ 
/* 262 */       return i | j | k | m;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Ushort555Rgb extends PixelConverter
/*     */   {
/* 210 */     public static final PixelConverter instance = new Ushort555Rgb();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 215 */       return paramInt >> 9 & 0x7C00 | paramInt >> 6 & 0x3E0 | paramInt >> 3 & 0x1F;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 222 */       int i = paramInt >> 10 & 0x1F;
/* 223 */       i = i << 3 | i >> 2;
/* 224 */       int j = paramInt >> 5 & 0x1F;
/* 225 */       j = j << 3 | j >> 2;
/* 226 */       int k = paramInt & 0x1F;
/* 227 */       k = k << 3 | k >> 2;
/* 228 */       return 0xFF000000 | i << 16 | j << 8 | k;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Ushort555Rgbx extends PixelConverter
/*     */   {
/* 188 */     public static final PixelConverter instance = new Ushort555Rgbx();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 193 */       return paramInt >> 8 & 0xF800 | paramInt >> 5 & 0x7C0 | paramInt >> 2 & 0x3E;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 200 */       int i = paramInt >> 11 & 0x1F;
/* 201 */       i = i << 3 | i >> 2;
/* 202 */       int j = paramInt >> 6 & 0x1F;
/* 203 */       j = j << 3 | j >> 2;
/* 204 */       int k = paramInt >> 1 & 0x1F;
/* 205 */       k = k << 3 | k >> 2;
/* 206 */       return 0xFF000000 | i << 16 | j << 8 | k;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Ushort565Rgb extends PixelConverter
/*     */   {
/* 166 */     public static final PixelConverter instance = new Ushort565Rgb();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 171 */       return paramInt >> 8 & 0xF800 | paramInt >> 5 & 0x7E0 | paramInt >> 3 & 0x1F;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 178 */       int i = paramInt >> 11 & 0x1F;
/* 179 */       i = i << 3 | i >> 2;
/* 180 */       int j = paramInt >> 5 & 0x3F;
/* 181 */       j = j << 2 | j >> 4;
/* 182 */       int k = paramInt & 0x1F;
/* 183 */       k = k << 3 | k >> 2;
/* 184 */       return 0xFF000000 | i << 16 | j << 8 | k;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class UshortGray extends PixelConverter.ByteGray
/*     */   {
/*     */     static final double SHORT_MULT = 257.0D;
/*     */     static final double USHORT_RED_MULT = 76.843000000000004D;
/*     */     static final double USHORT_GRN_MULT = 150.85899999999998D;
/*     */     static final double USHORT_BLU_MULT = 29.298000000000002D;
/* 428 */     public static final PixelConverter instance = new UshortGray();
/*     */ 
/* 430 */     private UshortGray() { super(); }
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel) {
/* 433 */       int i = paramInt >> 16 & 0xFF;
/* 434 */       int j = paramInt >> 8 & 0xFF;
/* 435 */       int k = paramInt & 0xFF;
/* 436 */       return (int)(i * 76.843000000000004D + j * 150.85899999999998D + k * 29.298000000000002D + 0.5D);
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 443 */       paramInt >>= 8;
/* 444 */       return ((0xFF00 | paramInt) << 8 | paramInt) << 8 | paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Xbgr extends PixelConverter
/*     */   {
/* 266 */     public static final PixelConverter instance = new Xbgr();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 271 */       return (paramInt & 0xFF) << 16 | paramInt & 0xFF00 | paramInt >> 16 & 0xFF;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 277 */       return 0xFF000000 | (paramInt & 0xFF) << 16 | paramInt & 0xFF00 | paramInt >> 16 & 0xFF;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Xrgb extends PixelConverter
/*     */   {
/* 138 */     public static final PixelConverter instance = new Xrgb();
/*     */ 
/*     */     public int rgbToPixel(int paramInt, ColorModel paramColorModel)
/*     */     {
/* 143 */       return paramInt;
/*     */     }
/*     */ 
/*     */     public int pixelToRgb(int paramInt, ColorModel paramColorModel) {
/* 147 */       return 0xFF000000 | paramInt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.PixelConverter
 * JD-Core Version:    0.6.2
 */
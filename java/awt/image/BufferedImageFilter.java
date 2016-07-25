/*     */ package java.awt.image;
/*     */ 
/*     */ public class BufferedImageFilter extends ImageFilter
/*     */   implements Cloneable
/*     */ {
/*     */   BufferedImageOp bufferedImageOp;
/*     */   ColorModel model;
/*     */   int width;
/*     */   int height;
/*     */   byte[] bytePixels;
/*     */   int[] intPixels;
/*     */ 
/*     */   public BufferedImageFilter(BufferedImageOp paramBufferedImageOp)
/*     */   {
/*  63 */     if (paramBufferedImageOp == null) {
/*  64 */       throw new NullPointerException("Operation cannot be null");
/*     */     }
/*  66 */     this.bufferedImageOp = paramBufferedImageOp;
/*     */   }
/*     */ 
/*     */   public BufferedImageOp getBufferedImageOp()
/*     */   {
/*  74 */     return this.bufferedImageOp;
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  96 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/*  97 */       imageComplete(3);
/*  98 */       return;
/*     */     }
/* 100 */     this.width = paramInt1;
/* 101 */     this.height = paramInt2;
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/* 125 */     this.model = paramColorModel;
/*     */   }
/*     */ 
/*     */   private void convertToRGB() {
/* 129 */     int i = this.width * this.height;
/* 130 */     int[] arrayOfInt = new int[i];
/*     */     int j;
/* 131 */     if (this.bytePixels != null) {
/* 132 */       for (j = 0; j < i; j++)
/* 133 */         arrayOfInt[j] = this.model.getRGB(this.bytePixels[j] & 0xFF);
/*     */     }
/* 135 */     else if (this.intPixels != null) {
/* 136 */       for (j = 0; j < i; j++) {
/* 137 */         arrayOfInt[j] = this.model.getRGB(this.intPixels[j]);
/*     */       }
/*     */     }
/* 140 */     this.bytePixels = null;
/* 141 */     this.intPixels = arrayOfInt;
/* 142 */     this.model = ColorModel.getRGBdefault();
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 165 */     if ((paramInt3 < 0) || (paramInt4 < 0)) {
/* 166 */       throw new IllegalArgumentException("Width (" + paramInt3 + ") and height (" + paramInt4 + ") must be > 0");
/*     */     }
/*     */ 
/* 171 */     if ((paramInt3 == 0) || (paramInt4 == 0)) {
/* 172 */       return;
/*     */     }
/* 174 */     if (paramInt2 < 0) {
/* 175 */       i = -paramInt2;
/* 176 */       if (i >= paramInt4) {
/* 177 */         return;
/*     */       }
/* 179 */       paramInt5 += paramInt6 * i;
/* 180 */       paramInt2 += i;
/* 181 */       paramInt4 -= i;
/*     */     }
/* 183 */     if (paramInt2 + paramInt4 > this.height) {
/* 184 */       paramInt4 = this.height - paramInt2;
/* 185 */       if (paramInt4 <= 0) {
/* 186 */         return;
/*     */       }
/*     */     }
/* 189 */     if (paramInt1 < 0) {
/* 190 */       i = -paramInt1;
/* 191 */       if (i >= paramInt3) {
/* 192 */         return;
/*     */       }
/* 194 */       paramInt5 += i;
/* 195 */       paramInt1 += i;
/* 196 */       paramInt3 -= i;
/*     */     }
/* 198 */     if (paramInt1 + paramInt3 > this.width) {
/* 199 */       paramInt3 = this.width - paramInt1;
/* 200 */       if (paramInt3 <= 0) {
/* 201 */         return;
/*     */       }
/*     */     }
/* 204 */     int i = paramInt2 * this.width + paramInt1;
/*     */     int j;
/* 205 */     if (this.intPixels == null) {
/* 206 */       if (this.bytePixels == null) {
/* 207 */         this.bytePixels = new byte[this.width * this.height];
/* 208 */         this.model = paramColorModel;
/* 209 */       } else if (this.model != paramColorModel) {
/* 210 */         convertToRGB();
/*     */       }
/* 212 */       if (this.bytePixels != null) {
/* 213 */         for (j = paramInt4; j > 0; j--) {
/* 214 */           System.arraycopy(paramArrayOfByte, paramInt5, this.bytePixels, i, paramInt3);
/* 215 */           paramInt5 += paramInt6;
/* 216 */           i += this.width;
/*     */         }
/*     */       }
/*     */     }
/* 220 */     if (this.intPixels != null) {
/* 221 */       j = this.width - paramInt3;
/* 222 */       int k = paramInt6 - paramInt3;
/* 223 */       for (int m = paramInt4; m > 0; m--) {
/* 224 */         for (int n = paramInt3; n > 0; n--) {
/* 225 */           this.intPixels[(i++)] = paramColorModel.getRGB(paramArrayOfByte[(paramInt5++)] & 0xFF);
/*     */         }
/* 227 */         paramInt5 += k;
/* 228 */         i += j;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 252 */     if ((paramInt3 < 0) || (paramInt4 < 0)) {
/* 253 */       throw new IllegalArgumentException("Width (" + paramInt3 + ") and height (" + paramInt4 + ") must be > 0");
/*     */     }
/*     */ 
/* 258 */     if ((paramInt3 == 0) || (paramInt4 == 0)) {
/* 259 */       return;
/*     */     }
/* 261 */     if (paramInt2 < 0) {
/* 262 */       i = -paramInt2;
/* 263 */       if (i >= paramInt4) {
/* 264 */         return;
/*     */       }
/* 266 */       paramInt5 += paramInt6 * i;
/* 267 */       paramInt2 += i;
/* 268 */       paramInt4 -= i;
/*     */     }
/* 270 */     if (paramInt2 + paramInt4 > this.height) {
/* 271 */       paramInt4 = this.height - paramInt2;
/* 272 */       if (paramInt4 <= 0) {
/* 273 */         return;
/*     */       }
/*     */     }
/* 276 */     if (paramInt1 < 0) {
/* 277 */       i = -paramInt1;
/* 278 */       if (i >= paramInt3) {
/* 279 */         return;
/*     */       }
/* 281 */       paramInt5 += i;
/* 282 */       paramInt1 += i;
/* 283 */       paramInt3 -= i;
/*     */     }
/* 285 */     if (paramInt1 + paramInt3 > this.width) {
/* 286 */       paramInt3 = this.width - paramInt1;
/* 287 */       if (paramInt3 <= 0) {
/* 288 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 292 */     if (this.intPixels == null) {
/* 293 */       if (this.bytePixels == null) {
/* 294 */         this.intPixels = new int[this.width * this.height];
/* 295 */         this.model = paramColorModel;
/*     */       } else {
/* 297 */         convertToRGB();
/*     */       }
/*     */     }
/* 300 */     int i = paramInt2 * this.width + paramInt1;
/*     */     int j;
/* 301 */     if (this.model == paramColorModel) {
/* 302 */       for (j = paramInt4; j > 0; j--) {
/* 303 */         System.arraycopy(paramArrayOfInt, paramInt5, this.intPixels, i, paramInt3);
/* 304 */         paramInt5 += paramInt6;
/* 305 */         i += this.width;
/*     */       }
/*     */     } else {
/* 308 */       if (this.model != ColorModel.getRGBdefault()) {
/* 309 */         convertToRGB();
/*     */       }
/* 311 */       j = this.width - paramInt3;
/* 312 */       int k = paramInt6 - paramInt3;
/* 313 */       for (int m = paramInt4; m > 0; m--) {
/* 314 */         for (int n = paramInt3; n > 0; n--) {
/* 315 */           this.intPixels[(i++)] = paramColorModel.getRGB(paramArrayOfInt[(paramInt5++)]);
/*     */         }
/* 317 */         paramInt5 += k;
/* 318 */         i += j;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void imageComplete(int paramInt)
/*     */   {
/* 341 */     switch (paramInt)
/*     */     {
/*     */     case 1:
/*     */     case 4:
/* 345 */       this.model = null;
/* 346 */       this.width = -1;
/* 347 */       this.height = -1;
/* 348 */       this.intPixels = null;
/* 349 */       this.bytePixels = null;
/* 350 */       break;
/*     */     case 2:
/*     */     case 3:
/* 354 */       if ((this.width > 0) && (this.height > 0))
/*     */       {
/*     */         WritableRaster localWritableRaster;
/* 355 */         if ((this.model instanceof DirectColorModel)) {
/* 356 */           if (this.intPixels == null) break;
/* 357 */           localWritableRaster = createDCMraster();
/*     */         }
/* 359 */         else if ((this.model instanceof IndexColorModel)) {
/* 360 */           localObject1 = new int[] { 0 };
/* 361 */           if (this.bytePixels == null) break;
/* 362 */           localObject2 = new DataBufferByte(this.bytePixels, this.width * this.height);
/*     */ 
/* 364 */           localWritableRaster = Raster.createInterleavedRaster((DataBuffer)localObject2, this.width, this.height, this.width, 1, (int[])localObject1, null);
/*     */         }
/*     */         else
/*     */         {
/* 368 */           convertToRGB();
/* 369 */           if (this.intPixels == null) break;
/* 370 */           localWritableRaster = createDCMraster();
/*     */         }
/* 372 */         Object localObject1 = new BufferedImage(this.model, localWritableRaster, this.model.isAlphaPremultiplied(), null);
/*     */ 
/* 375 */         localObject1 = this.bufferedImageOp.filter((BufferedImage)localObject1, null);
/* 376 */         Object localObject2 = ((BufferedImage)localObject1).getRaster();
/* 377 */         ColorModel localColorModel = ((BufferedImage)localObject1).getColorModel();
/* 378 */         int i = ((WritableRaster)localObject2).getWidth();
/* 379 */         int j = ((WritableRaster)localObject2).getHeight();
/* 380 */         this.consumer.setDimensions(i, j);
/* 381 */         this.consumer.setColorModel(localColorModel);
/*     */         Object localObject3;
/* 382 */         if ((localColorModel instanceof DirectColorModel)) {
/* 383 */           localObject3 = (DataBufferInt)((WritableRaster)localObject2).getDataBuffer();
/* 384 */           this.consumer.setPixels(0, 0, i, j, localColorModel, ((DataBufferInt)localObject3).getData(), 0, i);
/*     */         }
/* 387 */         else if ((localColorModel instanceof IndexColorModel)) {
/* 388 */           localObject3 = (DataBufferByte)((WritableRaster)localObject2).getDataBuffer();
/* 389 */           this.consumer.setPixels(0, 0, i, j, localColorModel, ((DataBufferByte)localObject3).getData(), 0, i);
/*     */         }
/*     */         else
/*     */         {
/* 393 */           throw new InternalError("Unknown color model " + localColorModel);
/*     */         }
/*     */       }
/*     */       break;
/*     */     }
/* 397 */     this.consumer.imageComplete(paramInt);
/*     */   }
/*     */ 
/*     */   private final WritableRaster createDCMraster()
/*     */   {
/* 402 */     DirectColorModel localDirectColorModel = (DirectColorModel)this.model;
/* 403 */     boolean bool = this.model.hasAlpha();
/* 404 */     int[] arrayOfInt = new int[3 + (bool ? 1 : 0)];
/* 405 */     arrayOfInt[0] = localDirectColorModel.getRedMask();
/* 406 */     arrayOfInt[1] = localDirectColorModel.getGreenMask();
/* 407 */     arrayOfInt[2] = localDirectColorModel.getBlueMask();
/* 408 */     if (bool) {
/* 409 */       arrayOfInt[3] = localDirectColorModel.getAlphaMask();
/*     */     }
/* 411 */     DataBufferInt localDataBufferInt = new DataBufferInt(this.intPixels, this.width * this.height);
/* 412 */     WritableRaster localWritableRaster = Raster.createPackedRaster(localDataBufferInt, this.width, this.height, this.width, arrayOfInt, null);
/*     */ 
/* 414 */     return localWritableRaster;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.BufferedImageFilter
 * JD-Core Version:    0.6.2
 */
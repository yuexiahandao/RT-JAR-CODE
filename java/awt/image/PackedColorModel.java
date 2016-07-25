/*     */ package java.awt.image;
/*     */ 
/*     */ import java.awt.color.ColorSpace;
/*     */ 
/*     */ public abstract class PackedColorModel extends ColorModel
/*     */ {
/*     */   int[] maskArray;
/*     */   int[] maskOffsets;
/*     */   float[] scaleFactors;
/*     */ 
/*     */   public PackedColorModel(ColorSpace paramColorSpace, int paramInt1, int[] paramArrayOfInt, int paramInt2, boolean paramBoolean, int paramInt3, int paramInt4)
/*     */   {
/* 129 */     super(paramInt1, createBitsArray(paramArrayOfInt, paramInt2), paramColorSpace, paramInt2 != 0, paramBoolean, paramInt3, paramInt4);
/*     */ 
/* 133 */     if ((paramInt1 < 1) || (paramInt1 > 32)) {
/* 134 */       throw new IllegalArgumentException("Number of bits must be between 1 and 32.");
/*     */     }
/*     */ 
/* 137 */     this.maskArray = new int[this.numComponents];
/* 138 */     this.maskOffsets = new int[this.numComponents];
/* 139 */     this.scaleFactors = new float[this.numComponents];
/*     */ 
/* 141 */     for (int i = 0; i < this.numColorComponents; i++)
/*     */     {
/* 143 */       DecomposeMask(paramArrayOfInt[i], i, paramColorSpace.getName(i));
/*     */     }
/* 145 */     if (paramInt2 != 0) {
/* 146 */       DecomposeMask(paramInt2, this.numColorComponents, "alpha");
/* 147 */       if (this.nBits[(this.numComponents - 1)] == 1)
/* 148 */         this.transparency = 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public PackedColorModel(ColorSpace paramColorSpace, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, int paramInt6, int paramInt7)
/*     */   {
/* 197 */     super(paramInt1, createBitsArray(paramInt2, paramInt3, paramInt4, paramInt5), paramColorSpace, paramInt5 != 0, paramBoolean, paramInt6, paramInt7);
/*     */ 
/* 202 */     if (paramColorSpace.getType() != 5) {
/* 203 */       throw new IllegalArgumentException("ColorSpace must be TYPE_RGB.");
/*     */     }
/* 205 */     this.maskArray = new int[this.numComponents];
/* 206 */     this.maskOffsets = new int[this.numComponents];
/* 207 */     this.scaleFactors = new float[this.numComponents];
/*     */ 
/* 209 */     DecomposeMask(paramInt2, 0, "red");
/*     */ 
/* 211 */     DecomposeMask(paramInt3, 1, "green");
/*     */ 
/* 213 */     DecomposeMask(paramInt4, 2, "blue");
/*     */ 
/* 215 */     if (paramInt5 != 0) {
/* 216 */       DecomposeMask(paramInt5, 3, "alpha");
/* 217 */       if (this.nBits[3] == 1)
/* 218 */         this.transparency = 2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getMask(int paramInt)
/*     */   {
/* 244 */     return this.maskArray[paramInt];
/*     */   }
/*     */ 
/*     */   public final int[] getMasks()
/*     */   {
/* 255 */     return (int[])this.maskArray.clone();
/*     */   }
/*     */ 
/*     */   private void DecomposeMask(int paramInt1, int paramInt2, String paramString)
/*     */   {
/* 264 */     int i = 0;
/* 265 */     int j = this.nBits[paramInt2];
/*     */ 
/* 268 */     this.maskArray[paramInt2] = paramInt1;
/*     */ 
/* 271 */     if (paramInt1 != 0) {
/* 272 */       while ((paramInt1 & 0x1) == 0) {
/* 273 */         paramInt1 >>>= 1;
/* 274 */         i++;
/*     */       }
/*     */     }
/*     */ 
/* 278 */     if (i + j > this.pixel_bits) {
/* 279 */       throw new IllegalArgumentException(paramString + " mask " + Integer.toHexString(this.maskArray[paramInt2]) + " overflows pixel (expecting " + this.pixel_bits + " bits");
/*     */     }
/*     */ 
/* 285 */     this.maskOffsets[paramInt2] = i;
/* 286 */     if (j == 0)
/*     */     {
/* 289 */       this.scaleFactors[paramInt2] = 256.0F;
/*     */     }
/* 291 */     else this.scaleFactors[paramInt2] = (255.0F / ((1 << j) - 1));
/*     */   }
/*     */ 
/*     */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*     */   {
/* 310 */     return new SinglePixelPackedSampleModel(this.transferType, paramInt1, paramInt2, this.maskArray);
/*     */   }
/*     */ 
/*     */   public boolean isCompatibleSampleModel(SampleModel paramSampleModel)
/*     */   {
/* 326 */     if (!(paramSampleModel instanceof SinglePixelPackedSampleModel)) {
/* 327 */       return false;
/*     */     }
/*     */ 
/* 331 */     if (this.numComponents != paramSampleModel.getNumBands()) {
/* 332 */       return false;
/*     */     }
/*     */ 
/* 336 */     if (paramSampleModel.getTransferType() != this.transferType) {
/* 337 */       return false;
/*     */     }
/*     */ 
/* 340 */     SinglePixelPackedSampleModel localSinglePixelPackedSampleModel = (SinglePixelPackedSampleModel)paramSampleModel;
/*     */ 
/* 342 */     int[] arrayOfInt = localSinglePixelPackedSampleModel.getBitMasks();
/* 343 */     if (arrayOfInt.length != this.maskArray.length) {
/* 344 */       return false;
/*     */     }
/*     */ 
/* 350 */     int i = (int)((1L << DataBuffer.getDataTypeSize(this.transferType)) - 1L);
/* 351 */     for (int j = 0; j < arrayOfInt.length; j++) {
/* 352 */       if ((i & arrayOfInt[j]) != (i & this.maskArray[j])) {
/* 353 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 357 */     return true;
/*     */   }
/*     */ 
/*     */   public WritableRaster getAlphaRaster(WritableRaster paramWritableRaster)
/*     */   {
/* 374 */     if (!hasAlpha()) {
/* 375 */       return null;
/*     */     }
/*     */ 
/* 378 */     int i = paramWritableRaster.getMinX();
/* 379 */     int j = paramWritableRaster.getMinY();
/* 380 */     int[] arrayOfInt = new int[1];
/* 381 */     arrayOfInt[0] = (paramWritableRaster.getNumBands() - 1);
/* 382 */     return paramWritableRaster.createWritableChild(i, j, paramWritableRaster.getWidth(), paramWritableRaster.getHeight(), i, j, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 397 */     if (!(paramObject instanceof PackedColorModel)) {
/* 398 */       return false;
/*     */     }
/*     */ 
/* 401 */     if (!super.equals(paramObject)) {
/* 402 */       return false;
/*     */     }
/*     */ 
/* 405 */     PackedColorModel localPackedColorModel = (PackedColorModel)paramObject;
/* 406 */     int i = localPackedColorModel.getNumComponents();
/* 407 */     if (i != this.numComponents) {
/* 408 */       return false;
/*     */     }
/* 410 */     for (int j = 0; j < i; j++) {
/* 411 */       if (this.maskArray[j] != localPackedColorModel.getMask(j)) {
/* 412 */         return false;
/*     */       }
/*     */     }
/* 415 */     return true;
/*     */   }
/*     */ 
/*     */   private static final int[] createBitsArray(int[] paramArrayOfInt, int paramInt)
/*     */   {
/* 420 */     int i = paramArrayOfInt.length;
/* 421 */     int j = paramInt == 0 ? 0 : 1;
/* 422 */     int[] arrayOfInt = new int[i + j];
/* 423 */     for (int k = 0; k < i; k++) {
/* 424 */       arrayOfInt[k] = countBits(paramArrayOfInt[k]);
/* 425 */       if (arrayOfInt[k] < 0) {
/* 426 */         throw new IllegalArgumentException("Noncontiguous color mask (" + Integer.toHexString(paramArrayOfInt[k]) + "at index " + k);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 431 */     if (paramInt != 0) {
/* 432 */       arrayOfInt[i] = countBits(paramInt);
/* 433 */       if (arrayOfInt[i] < 0) {
/* 434 */         throw new IllegalArgumentException("Noncontiguous alpha mask (" + Integer.toHexString(paramInt));
/*     */       }
/*     */     }
/*     */ 
/* 438 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static final int[] createBitsArray(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 443 */     int[] arrayOfInt = new int[3 + (paramInt4 == 0 ? 0 : 1)];
/* 444 */     arrayOfInt[0] = countBits(paramInt1);
/* 445 */     arrayOfInt[1] = countBits(paramInt2);
/* 446 */     arrayOfInt[2] = countBits(paramInt3);
/* 447 */     if (arrayOfInt[0] < 0) {
/* 448 */       throw new IllegalArgumentException("Noncontiguous red mask (" + Integer.toHexString(paramInt1));
/*     */     }
/*     */ 
/* 451 */     if (arrayOfInt[1] < 0) {
/* 452 */       throw new IllegalArgumentException("Noncontiguous green mask (" + Integer.toHexString(paramInt2));
/*     */     }
/*     */ 
/* 455 */     if (arrayOfInt[2] < 0) {
/* 456 */       throw new IllegalArgumentException("Noncontiguous blue mask (" + Integer.toHexString(paramInt3));
/*     */     }
/*     */ 
/* 459 */     if (paramInt4 != 0) {
/* 460 */       arrayOfInt[3] = countBits(paramInt4);
/* 461 */       if (arrayOfInt[3] < 0) {
/* 462 */         throw new IllegalArgumentException("Noncontiguous alpha mask (" + Integer.toHexString(paramInt4));
/*     */       }
/*     */     }
/*     */ 
/* 466 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private static final int countBits(int paramInt) {
/* 470 */     int i = 0;
/* 471 */     if (paramInt != 0) {
/* 472 */       while ((paramInt & 0x1) == 0) {
/* 473 */         paramInt >>>= 1;
/*     */       }
/* 475 */       while ((paramInt & 0x1) == 1) {
/* 476 */         paramInt >>>= 1;
/* 477 */         i++;
/*     */       }
/*     */     }
/* 480 */     if (paramInt != 0) {
/* 481 */       return -1;
/*     */     }
/* 483 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.PackedColorModel
 * JD-Core Version:    0.6.2
 */
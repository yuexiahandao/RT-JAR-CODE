/*     */ package java.awt.image;
/*     */ 
/*     */ public class PixelInterleavedSampleModel extends ComponentSampleModel
/*     */ {
/*     */   public PixelInterleavedSampleModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int[] paramArrayOfInt)
/*     */   {
/*  87 */     super(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramArrayOfInt);
/*  88 */     int i = this.bandOffsets[0];
/*  89 */     int j = this.bandOffsets[0];
/*  90 */     for (int k = 1; k < this.bandOffsets.length; k++) {
/*  91 */       i = Math.min(i, this.bandOffsets[k]);
/*  92 */       j = Math.max(j, this.bandOffsets[k]);
/*     */     }
/*  94 */     j -= i;
/*  95 */     if (j > paramInt5) {
/*  96 */       throw new IllegalArgumentException("Offsets between bands must be less than the scanline  stride");
/*     */     }
/*     */ 
/* 100 */     if (paramInt4 * paramInt2 > paramInt5) {
/* 101 */       throw new IllegalArgumentException("Pixel stride times width must be less than or equal to the scanline stride");
/*     */     }
/*     */ 
/* 106 */     if (paramInt4 < j)
/* 107 */       throw new IllegalArgumentException("Pixel stride must be greater than or equal to the offsets between bands");
/*     */   }
/*     */ 
/*     */   public SampleModel createCompatibleSampleModel(int paramInt1, int paramInt2)
/*     */   {
/* 127 */     int i = this.bandOffsets[0];
/* 128 */     int j = this.bandOffsets.length;
/* 129 */     for (int k = 1; k < j; k++)
/* 130 */       if (this.bandOffsets[k] < i)
/* 131 */         i = this.bandOffsets[k];
/*     */     int[] arrayOfInt;
/* 135 */     if (i > 0) {
/* 136 */       arrayOfInt = new int[j];
/* 137 */       for (int m = 0; m < j; m++)
/* 138 */         arrayOfInt[m] = (this.bandOffsets[m] - i);
/*     */     }
/*     */     else
/*     */     {
/* 142 */       arrayOfInt = this.bandOffsets;
/*     */     }
/* 144 */     return new PixelInterleavedSampleModel(this.dataType, paramInt1, paramInt2, this.pixelStride, this.pixelStride * paramInt1, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public SampleModel createSubsetSampleModel(int[] paramArrayOfInt)
/*     */   {
/* 158 */     int[] arrayOfInt = new int[paramArrayOfInt.length];
/* 159 */     for (int i = 0; i < paramArrayOfInt.length; i++) {
/* 160 */       arrayOfInt[i] = this.bandOffsets[paramArrayOfInt[i]];
/*     */     }
/* 162 */     return new PixelInterleavedSampleModel(this.dataType, this.width, this.height, this.pixelStride, this.scanlineStride, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 169 */     return super.hashCode() ^ 0x1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.PixelInterleavedSampleModel
 * JD-Core Version:    0.6.2
 */
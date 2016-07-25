/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class DropShadowEffect extends ShadowEffect
/*     */ {
/*     */   Effect.EffectType getEffectType()
/*     */   {
/*  52 */     return Effect.EffectType.UNDER;
/*     */   }
/*     */ 
/*     */   BufferedImage applyEffect(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2, int paramInt1, int paramInt2)
/*     */   {
/*  69 */     if ((paramBufferedImage1 == null) || (paramBufferedImage1.getType() != 2)) {
/*  70 */       throw new IllegalArgumentException("Effect only works with source images of type BufferedImage.TYPE_INT_ARGB.");
/*     */     }
/*     */ 
/*  73 */     if ((paramBufferedImage2 != null) && (paramBufferedImage2.getType() != 2)) {
/*  74 */       throw new IllegalArgumentException("Effect only works with destination images of type BufferedImage.TYPE_INT_ARGB.");
/*     */     }
/*     */ 
/*  78 */     double d = Math.toRadians(this.angle - 90);
/*  79 */     int i = (int)(Math.sin(d) * this.distance);
/*  80 */     int j = (int)(Math.cos(d) * this.distance);
/*     */ 
/*  82 */     int k = i + this.size;
/*  83 */     int m = i + this.size;
/*  84 */     int n = paramInt1 + i + this.size + this.size;
/*  85 */     int i1 = paramInt2 + i + this.size;
/*     */ 
/*  87 */     int[] arrayOfInt = getArrayCache().getTmpIntArray(paramInt1);
/*  88 */     byte[] arrayOfByte1 = getArrayCache().getTmpByteArray1(n * i1);
/*  89 */     Arrays.fill(arrayOfByte1, (byte)0);
/*  90 */     byte[] arrayOfByte2 = getArrayCache().getTmpByteArray2(n * i1);
/*     */ 
/*  92 */     WritableRaster localWritableRaster1 = paramBufferedImage1.getRaster();
/*  93 */     for (int i2 = 0; i2 < paramInt2; i2++) {
/*  94 */       int i3 = i2 + m;
/*  95 */       i4 = i3 * n;
/*  96 */       localWritableRaster1.getDataElements(0, i2, paramInt1, 1, arrayOfInt);
/*  97 */       for (i5 = 0; i5 < paramInt1; i5++) {
/*  98 */         i6 = i5 + k;
/*  99 */         arrayOfByte1[(i4 + i6)] = ((byte)((arrayOfInt[i5] & 0xFF000000) >>> 24));
/*     */       }
/*     */     }
/*     */ 
/* 103 */     float[] arrayOfFloat = EffectUtils.createGaussianKernel(this.size);
/* 104 */     EffectUtils.blur(arrayOfByte1, arrayOfByte2, n, i1, arrayOfFloat, this.size);
/* 105 */     EffectUtils.blur(arrayOfByte2, arrayOfByte1, i1, n, arrayOfFloat, this.size);
/*     */ 
/* 107 */     float f = Math.min(1.0F / (1.0F - 0.01F * this.spread), 255.0F);
/* 108 */     for (int i4 = 0; i4 < arrayOfByte1.length; i4++) {
/* 109 */       i5 = (int)((arrayOfByte1[i4] & 0xFF) * f);
/* 110 */       arrayOfByte1[i4] = (i5 > 255 ? -1 : (byte)i5);
/*     */     }
/*     */ 
/* 113 */     if (paramBufferedImage2 == null) paramBufferedImage2 = new BufferedImage(paramInt1, paramInt2, 2);
/*     */ 
/* 115 */     WritableRaster localWritableRaster2 = paramBufferedImage2.getRaster();
/* 116 */     int i5 = this.color.getRed(); int i6 = this.color.getGreen(); int i7 = this.color.getBlue();
/* 117 */     for (int i8 = 0; i8 < paramInt2; i8++) {
/* 118 */       int i9 = i8 + m;
/* 119 */       int i10 = (i9 - j) * n;
/* 120 */       for (int i11 = 0; i11 < paramInt1; i11++) {
/* 121 */         int i12 = i11 + k;
/* 122 */         arrayOfInt[i11] = (arrayOfByte1[(i10 + (i12 - i))] << 24 | i5 << 16 | i6 << 8 | i7);
/*     */       }
/* 124 */       localWritableRaster2.setDataElements(0, i8, paramInt1, 1, arrayOfInt);
/*     */     }
/* 126 */     return paramBufferedImage2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.DropShadowEffect
 * JD-Core Version:    0.6.2
 */
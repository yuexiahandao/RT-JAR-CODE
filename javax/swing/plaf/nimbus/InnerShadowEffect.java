/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class InnerShadowEffect extends ShadowEffect
/*     */ {
/*     */   Effect.EffectType getEffectType()
/*     */   {
/*  51 */     return Effect.EffectType.OVER;
/*     */   }
/*     */ 
/*     */   BufferedImage applyEffect(BufferedImage paramBufferedImage1, BufferedImage paramBufferedImage2, int paramInt1, int paramInt2)
/*     */   {
/*  67 */     if ((paramBufferedImage1 == null) || (paramBufferedImage1.getType() != 2)) {
/*  68 */       throw new IllegalArgumentException("Effect only works with source images of type BufferedImage.TYPE_INT_ARGB.");
/*     */     }
/*     */ 
/*  71 */     if ((paramBufferedImage2 != null) && (paramBufferedImage2.getType() != 2)) {
/*  72 */       throw new IllegalArgumentException("Effect only works with destination images of type BufferedImage.TYPE_INT_ARGB.");
/*     */     }
/*     */ 
/*  76 */     double d = Math.toRadians(this.angle - 90);
/*  77 */     int i = (int)(Math.sin(d) * this.distance);
/*  78 */     int j = (int)(Math.cos(d) * this.distance);
/*     */ 
/*  80 */     int k = i + this.size;
/*  81 */     int m = i + this.size;
/*  82 */     int n = paramInt1 + i + this.size + this.size;
/*  83 */     int i1 = paramInt2 + i + this.size;
/*     */ 
/*  85 */     int[] arrayOfInt = getArrayCache().getTmpIntArray(paramInt1);
/*  86 */     byte[] arrayOfByte1 = getArrayCache().getTmpByteArray1(n * i1);
/*  87 */     Arrays.fill(arrayOfByte1, (byte)-1);
/*  88 */     byte[] arrayOfByte2 = getArrayCache().getTmpByteArray2(n * i1);
/*  89 */     byte[] arrayOfByte3 = getArrayCache().getTmpByteArray3(n * i1);
/*     */ 
/*  91 */     WritableRaster localWritableRaster1 = paramBufferedImage1.getRaster();
/*  92 */     for (int i2 = 0; i2 < paramInt2; i2++) {
/*  93 */       int i3 = i2 + m;
/*  94 */       i4 = i3 * n;
/*  95 */       localWritableRaster1.getDataElements(0, i2, paramInt1, 1, arrayOfInt);
/*  96 */       for (i5 = 0; i5 < paramInt1; i5++) {
/*  97 */         i6 = i5 + k;
/*  98 */         arrayOfByte1[(i4 + i6)] = ((byte)(255 - ((arrayOfInt[i5] & 0xFF000000) >>> 24) & 0xFF));
/*     */       }
/*     */     }
/*     */ 
/* 102 */     float[] arrayOfFloat = EffectUtils.createGaussianKernel(this.size * 2);
/* 103 */     EffectUtils.blur(arrayOfByte1, arrayOfByte3, n, i1, arrayOfFloat, this.size * 2);
/* 104 */     EffectUtils.blur(arrayOfByte3, arrayOfByte2, i1, n, arrayOfFloat, this.size * 2);
/*     */ 
/* 106 */     float f = Math.min(1.0F / (1.0F - 0.01F * this.spread), 255.0F);
/* 107 */     for (int i4 = 0; i4 < arrayOfByte2.length; i4++) {
/* 108 */       i5 = (int)((arrayOfByte2[i4] & 0xFF) * f);
/* 109 */       arrayOfByte2[i4] = (i5 > 255 ? -1 : (byte)i5);
/*     */     }
/*     */ 
/* 112 */     if (paramBufferedImage2 == null) paramBufferedImage2 = new BufferedImage(paramInt1, paramInt2, 2);
/*     */ 
/* 114 */     WritableRaster localWritableRaster2 = paramBufferedImage2.getRaster();
/* 115 */     int i5 = this.color.getRed(); int i6 = this.color.getGreen(); int i7 = this.color.getBlue();
/* 116 */     for (int i8 = 0; i8 < paramInt2; i8++) {
/* 117 */       int i9 = i8 + m;
/* 118 */       int i10 = i9 * n;
/* 119 */       int i11 = (i9 - j) * n;
/* 120 */       for (int i12 = 0; i12 < paramInt1; i12++) {
/* 121 */         int i13 = i12 + k;
/* 122 */         int i14 = 255 - (arrayOfByte1[(i10 + i13)] & 0xFF);
/* 123 */         int i15 = arrayOfByte2[(i11 + (i13 - i))] & 0xFF;
/* 124 */         int i16 = Math.min(i14, i15);
/* 125 */         arrayOfInt[i12] = (((byte)i16 & 0xFF) << 24 | i5 << 16 | i6 << 8 | i7);
/*     */       }
/* 127 */       localWritableRaster2.setDataElements(0, i8, paramInt1, 1, arrayOfInt);
/*     */     }
/* 129 */     return paramBufferedImage2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.InnerShadowEffect
 * JD-Core Version:    0.6.2
 */
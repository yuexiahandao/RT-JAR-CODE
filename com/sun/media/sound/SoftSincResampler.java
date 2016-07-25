/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class SoftSincResampler extends SoftAbstractResampler
/*     */ {
/*     */   float[][][] sinc_table;
/*  37 */   int sinc_scale_size = 100;
/*  38 */   int sinc_table_fsize = 800;
/*  39 */   int sinc_table_size = 30;
/*  40 */   int sinc_table_center = this.sinc_table_size / 2;
/*     */ 
/*     */   public SoftSincResampler()
/*     */   {
/*  44 */     this.sinc_table = new float[this.sinc_scale_size][this.sinc_table_fsize];
/*  45 */     for (int i = 0; i < this.sinc_scale_size; i++) {
/*  46 */       float f = (float)(1.0D / (1.0D + Math.pow(i, 1.1D) / 10.0D));
/*  47 */       for (int j = 0; j < this.sinc_table_fsize; j++)
/*  48 */         this.sinc_table[i][j] = sincTable(this.sinc_table_size, -j / this.sinc_table_fsize, f);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static double sinc(double paramDouble)
/*     */   {
/*  56 */     return paramDouble == 0.0D ? 1.0D : Math.sin(3.141592653589793D * paramDouble) / (3.141592653589793D * paramDouble);
/*     */   }
/*     */ 
/*     */   public static float[] wHanning(int paramInt, float paramFloat)
/*     */   {
/*  61 */     float[] arrayOfFloat = new float[paramInt];
/*  62 */     for (int i = 0; i < paramInt; i++) {
/*  63 */       arrayOfFloat[i] = ((float)(-0.5D * Math.cos(6.283185307179586D * (i + paramFloat) / paramInt) + 0.5D));
/*     */     }
/*     */ 
/*  67 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public static float[] sincTable(int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/*  72 */     int i = paramInt / 2;
/*  73 */     float[] arrayOfFloat = wHanning(paramInt, paramFloat1);
/*  74 */     for (int j = 0; j < paramInt; j++)
/*     */     {
/*     */       int tmp24_22 = j;
/*     */       float[] tmp24_20 = arrayOfFloat; tmp24_20[tmp24_22] = ((float)(tmp24_20[tmp24_22] * (sinc((-i + j + paramFloat1) * paramFloat2) * paramFloat2)));
/*  76 */     }return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public int getPadding()
/*     */   {
/*  81 */     return this.sinc_table_size / 2 + 2;
/*     */   }
/*     */ 
/*     */   public void interpolate(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float paramFloat1, float[] paramArrayOfFloat3, float paramFloat2, float[] paramArrayOfFloat4, int[] paramArrayOfInt, int paramInt)
/*     */   {
/*  87 */     float f1 = paramArrayOfFloat3[0];
/*  88 */     float f2 = paramArrayOfFloat2[0];
/*  89 */     int i = paramArrayOfInt[0];
/*  90 */     float f3 = paramFloat1;
/*  91 */     int j = paramInt;
/*  92 */     int k = this.sinc_scale_size - 1;
/*     */     int m;
/*     */     float[] arrayOfFloat;
/*     */     int i2;
/*     */     float f4;
/*     */     int i3;
/*  93 */     if (paramFloat2 == 0.0F)
/*     */     {
/*  95 */       m = (int)((f1 - 1.0F) * 10.0F);
/*  96 */       if (m < 0)
/*  97 */         m = 0;
/*  98 */       else if (m > k)
/*  99 */         m = k;
/* 100 */       float[][] arrayOfFloat1 = this.sinc_table[m];
/* 101 */       while ((f2 < f3) && (i < j)) {
/* 102 */         int i1 = (int)f2;
/* 103 */         arrayOfFloat = arrayOfFloat1[((int)((f2 - i1) * this.sinc_table_fsize))];
/*     */ 
/* 105 */         i2 = i1 - this.sinc_table_center;
/* 106 */         f4 = 0.0F;
/* 107 */         for (i3 = 0; i3 < this.sinc_table_size; i2++) {
/* 108 */           f4 += paramArrayOfFloat1[i2] * arrayOfFloat[i3];
/*     */ 
/* 107 */           i3++;
/*     */         }
/* 109 */         paramArrayOfFloat4[(i++)] = f4;
/* 110 */         f2 += f1;
/*     */       }
/*     */     } else {
/* 113 */       while ((f2 < f3) && (i < j)) {
/* 114 */         m = (int)f2;
/* 115 */         int n = (int)((f1 - 1.0F) * 10.0F);
/* 116 */         if (n < 0)
/* 117 */           n = 0;
/* 118 */         else if (n > k)
/* 119 */           n = k;
/* 120 */         float[][] arrayOfFloat2 = this.sinc_table[n];
/*     */ 
/* 122 */         arrayOfFloat = arrayOfFloat2[((int)((f2 - m) * this.sinc_table_fsize))];
/*     */ 
/* 124 */         i2 = m - this.sinc_table_center;
/* 125 */         f4 = 0.0F;
/* 126 */         for (i3 = 0; i3 < this.sinc_table_size; i2++) {
/* 127 */           f4 += paramArrayOfFloat1[i2] * arrayOfFloat[i3];
/*     */ 
/* 126 */           i3++;
/*     */         }
/* 128 */         paramArrayOfFloat4[(i++)] = f4;
/*     */ 
/* 130 */         f2 += f1;
/* 131 */         f1 += paramFloat2;
/*     */       }
/*     */     }
/* 134 */     paramArrayOfFloat2[0] = f2;
/* 135 */     paramArrayOfInt[0] = i;
/* 136 */     paramArrayOfFloat3[0] = f1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftSincResampler
 * JD-Core Version:    0.6.2
 */
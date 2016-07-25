/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class SoftLanczosResampler extends SoftAbstractResampler
/*     */ {
/*     */   float[][] sinc_table;
/*  35 */   int sinc_table_fsize = 2000;
/*  36 */   int sinc_table_size = 5;
/*  37 */   int sinc_table_center = this.sinc_table_size / 2;
/*     */ 
/*     */   public SoftLanczosResampler()
/*     */   {
/*  41 */     this.sinc_table = new float[this.sinc_table_fsize][];
/*  42 */     for (int i = 0; i < this.sinc_table_fsize; i++)
/*  43 */       this.sinc_table[i] = sincTable(this.sinc_table_size, -i / this.sinc_table_fsize);
/*     */   }
/*     */ 
/*     */   public static double sinc(double paramDouble)
/*     */   {
/*  50 */     return paramDouble == 0.0D ? 1.0D : Math.sin(3.141592653589793D * paramDouble) / (3.141592653589793D * paramDouble);
/*     */   }
/*     */ 
/*     */   public static float[] sincTable(int paramInt, float paramFloat)
/*     */   {
/*  55 */     int i = paramInt / 2;
/*  56 */     float[] arrayOfFloat = new float[paramInt];
/*  57 */     for (int j = 0; j < paramInt; j++) {
/*  58 */       float f = -i + j + paramFloat;
/*  59 */       if ((f < -2.0F) || (f > 2.0F))
/*  60 */         arrayOfFloat[j] = 0.0F;
/*  61 */       else if (f == 0.0F)
/*  62 */         arrayOfFloat[j] = 1.0F;
/*     */       else {
/*  64 */         arrayOfFloat[j] = ((float)(2.0D * Math.sin(3.141592653589793D * f) * Math.sin(3.141592653589793D * f / 2.0D) / (3.141592653589793D * f * (3.141592653589793D * f))));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  69 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public int getPadding()
/*     */   {
/*  74 */     return this.sinc_table_size / 2 + 2;
/*     */   }
/*     */ 
/*     */   public void interpolate(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float paramFloat1, float[] paramArrayOfFloat3, float paramFloat2, float[] paramArrayOfFloat4, int[] paramArrayOfInt, int paramInt)
/*     */   {
/*  80 */     float f1 = paramArrayOfFloat3[0];
/*  81 */     float f2 = paramArrayOfFloat2[0];
/*  82 */     int i = paramArrayOfInt[0];
/*  83 */     float f3 = paramFloat1;
/*  84 */     int j = paramInt;
/*     */     int k;
/*     */     float[] arrayOfFloat;
/*     */     int m;
/*     */     float f4;
/*     */     int n;
/*  86 */     if (paramFloat2 == 0.0F) {
/*  87 */       while ((f2 < f3) && (i < j)) {
/*  88 */         k = (int)f2;
/*  89 */         arrayOfFloat = this.sinc_table[((int)((f2 - k) * this.sinc_table_fsize))];
/*     */ 
/*  91 */         m = k - this.sinc_table_center;
/*  92 */         f4 = 0.0F;
/*  93 */         for (n = 0; n < this.sinc_table_size; m++) {
/*  94 */           f4 += paramArrayOfFloat1[m] * arrayOfFloat[n];
/*     */ 
/*  93 */           n++;
/*     */         }
/*  95 */         paramArrayOfFloat4[(i++)] = f4;
/*  96 */         f2 += f1;
/*     */       }
/*     */     }
/*  99 */     while ((f2 < f3) && (i < j)) {
/* 100 */       k = (int)f2;
/* 101 */       arrayOfFloat = this.sinc_table[((int)((f2 - k) * this.sinc_table_fsize))];
/*     */ 
/* 103 */       m = k - this.sinc_table_center;
/* 104 */       f4 = 0.0F;
/* 105 */       for (n = 0; n < this.sinc_table_size; m++) {
/* 106 */         f4 += paramArrayOfFloat1[m] * arrayOfFloat[n];
/*     */ 
/* 105 */         n++;
/*     */       }
/* 107 */       paramArrayOfFloat4[(i++)] = f4;
/*     */ 
/* 109 */       f2 += f1;
/* 110 */       f1 += paramFloat2;
/*     */     }
/*     */ 
/* 113 */     paramArrayOfFloat2[0] = f2;
/* 114 */     paramArrayOfInt[0] = i;
/* 115 */     paramArrayOfFloat3[0] = f1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftLanczosResampler
 * JD-Core Version:    0.6.2
 */
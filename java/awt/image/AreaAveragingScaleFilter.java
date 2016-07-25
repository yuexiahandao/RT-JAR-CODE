/*     */ package java.awt.image;
/*     */ 
/*     */ public class AreaAveragingScaleFilter extends ReplicateScaleFilter
/*     */ {
/*  61 */   private static final ColorModel rgbmodel = ColorModel.getRGBdefault();
/*     */   private static final int neededHints = 6;
/*     */   private boolean passthrough;
/*     */   private float[] reds;
/*     */   private float[] greens;
/*     */   private float[] blues;
/*     */   private float[] alphas;
/*     */   private int savedy;
/*     */   private int savedyrem;
/*     */ 
/*     */   public AreaAveragingScaleFilter(int paramInt1, int paramInt2)
/*     */   {
/*  77 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/*  93 */     this.passthrough = ((paramInt & 0x6) != 6);
/*  94 */     super.setHints(paramInt);
/*     */   }
/*     */ 
/*     */   private void makeAccumBuffers() {
/*  98 */     this.reds = new float[this.destWidth];
/*  99 */     this.greens = new float[this.destWidth];
/* 100 */     this.blues = new float[this.destWidth];
/* 101 */     this.alphas = new float[this.destWidth];
/*     */   }
/*     */ 
/*     */   private int[] calcRow() {
/* 105 */     float f1 = this.srcWidth * this.srcHeight;
/* 106 */     if ((this.outpixbuf == null) || (!(this.outpixbuf instanceof int[]))) {
/* 107 */       this.outpixbuf = new int[this.destWidth];
/*     */     }
/* 109 */     int[] arrayOfInt = (int[])this.outpixbuf;
/* 110 */     for (int i = 0; i < this.destWidth; i++) {
/* 111 */       float f2 = f1;
/* 112 */       int j = Math.round(this.alphas[i] / f2);
/* 113 */       if (j <= 0)
/* 114 */         j = 0;
/* 115 */       else if (j >= 255) {
/* 116 */         j = 255;
/*     */       }
/*     */       else
/*     */       {
/* 121 */         f2 = this.alphas[i] / 255.0F;
/*     */       }
/* 123 */       int k = Math.round(this.reds[i] / f2);
/* 124 */       int m = Math.round(this.greens[i] / f2);
/* 125 */       int n = Math.round(this.blues[i] / f2);
/* 126 */       if (k < 0) k = 0; else if (k > 255) k = 255;
/* 127 */       if (m < 0) m = 0; else if (m > 255) m = 255;
/* 128 */       if (n < 0) n = 0; else if (n > 255) n = 255;
/* 129 */       arrayOfInt[i] = (j << 24 | k << 16 | m << 8 | n);
/*     */     }
/* 131 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private void accumPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, Object paramObject, int paramInt5, int paramInt6)
/*     */   {
/* 137 */     if (this.reds == null) {
/* 138 */       makeAccumBuffers();
/*     */     }
/* 140 */     int i = paramInt2;
/* 141 */     int j = this.destHeight;
/*     */     int k;
/*     */     int m;
/* 143 */     if (i == 0) {
/* 144 */       k = 0;
/* 145 */       m = 0;
/*     */     } else {
/* 147 */       k = this.savedy;
/* 148 */       m = this.savedyrem;
/*     */     }
/* 150 */     while (i < paramInt2 + paramInt4)
/*     */     {
/* 152 */       if (m == 0) {
/* 153 */         for (i1 = 0; i1 < this.destWidth; i1++)
/*     */         {
/*     */           float tmp101_100 = (this.greens[i1] = this.blues[i1] = 0.0F); this.reds[i1] = tmp101_100; this.alphas[i1] = tmp101_100;
/*     */         }
/* 156 */         m = this.srcHeight;
/*     */       }
/*     */       int n;
/* 158 */       if (j < m)
/* 159 */         n = j;
/*     */       else {
/* 161 */         n = m;
/*     */       }
/* 163 */       int i1 = 0;
/* 164 */       int i2 = 0;
/* 165 */       int i3 = 0;
/* 166 */       int i4 = this.srcWidth;
/* 167 */       float f1 = 0.0F; float f2 = 0.0F; float f3 = 0.0F; float f4 = 0.0F;
/* 168 */       while (i1 < paramInt3)
/*     */       {
/*     */         int i5;
/* 169 */         if (i3 == 0) {
/* 170 */           i3 = this.destWidth;
/*     */ 
/* 172 */           if ((paramObject instanceof byte[]))
/* 173 */             i5 = ((byte[])(byte[])paramObject)[(paramInt5 + i1)] & 0xFF;
/*     */           else {
/* 175 */             i5 = ((int[])(int[])paramObject)[(paramInt5 + i1)];
/*     */           }
/*     */ 
/* 178 */           i5 = paramColorModel.getRGB(i5);
/* 179 */           f1 = i5 >>> 24;
/* 180 */           f2 = i5 >> 16 & 0xFF;
/* 181 */           f3 = i5 >> 8 & 0xFF;
/* 182 */           f4 = i5 & 0xFF;
/*     */ 
/* 184 */           if (f1 != 255.0F) {
/* 185 */             f5 = f1 / 255.0F;
/* 186 */             f2 *= f5;
/* 187 */             f3 *= f5;
/* 188 */             f4 *= f5;
/*     */           }
/*     */         }
/*     */ 
/* 192 */         if (i3 < i4)
/* 193 */           i5 = i3;
/*     */         else {
/* 195 */           i5 = i4;
/*     */         }
/* 197 */         float f5 = i5 * n;
/* 198 */         this.alphas[i2] += f5 * f1;
/* 199 */         this.reds[i2] += f5 * f2;
/* 200 */         this.greens[i2] += f5 * f3;
/* 201 */         this.blues[i2] += f5 * f4;
/* 202 */         if (i3 -= i5 == 0) {
/* 203 */           i1++;
/*     */         }
/* 205 */         if (i4 -= i5 == 0) {
/* 206 */           i2++;
/* 207 */           i4 = this.srcWidth;
/*     */         }
/*     */       }
/* 210 */       if (m -= n == 0) {
/* 211 */         int[] arrayOfInt = calcRow();
/*     */         do {
/* 213 */           this.consumer.setPixels(0, k, this.destWidth, 1, rgbmodel, arrayOfInt, 0, this.destWidth);
/*     */ 
/* 215 */           k++;
/* 216 */         }while ((j -= n >= n) && (n == this.srcHeight));
/*     */       } else {
/* 218 */         j -= n;
/*     */       }
/* 220 */       if (j == 0) {
/* 221 */         j = this.destHeight;
/* 222 */         i++;
/* 223 */         paramInt5 += paramInt6;
/*     */       }
/*     */     }
/* 226 */     this.savedyrem = m;
/* 227 */     this.savedy = k;
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 249 */     if (this.passthrough)
/* 250 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */     else
/* 252 */       accumPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 275 */     if (this.passthrough)
/* 276 */       super.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */     else
/* 278 */       accumPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.AreaAveragingScaleFilter
 * JD-Core Version:    0.6.2
 */
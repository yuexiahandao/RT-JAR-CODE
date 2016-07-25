/*     */ package com.sun.imageio.plugins.png;
/*     */ 
/*     */ public class RowFilter
/*     */ {
/*     */   private static final int abs(int paramInt)
/*     */   {
/*  31 */     return paramInt < 0 ? -paramInt : paramInt;
/*     */   }
/*     */ 
/*     */   protected static int subFilter(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2)
/*     */   {
/*  39 */     int i = 0;
/*  40 */     for (int j = paramInt1; j < paramInt2 + paramInt1; j++) {
/*  41 */       int k = paramArrayOfByte1[j] & 0xFF;
/*  42 */       int m = paramArrayOfByte1[(j - paramInt1)] & 0xFF;
/*  43 */       int n = k - m;
/*  44 */       paramArrayOfByte2[j] = ((byte)n);
/*     */ 
/*  46 */       i += abs(n);
/*     */     }
/*     */ 
/*  49 */     return i;
/*     */   }
/*     */ 
/*     */   protected static int upFilter(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt1, int paramInt2)
/*     */   {
/*  58 */     int i = 0;
/*  59 */     for (int j = paramInt1; j < paramInt2 + paramInt1; j++) {
/*  60 */       int k = paramArrayOfByte1[j] & 0xFF;
/*  61 */       int m = paramArrayOfByte2[j] & 0xFF;
/*  62 */       int n = k - m;
/*  63 */       paramArrayOfByte3[j] = ((byte)n);
/*     */ 
/*  65 */       i += abs(n);
/*     */     }
/*     */ 
/*  68 */     return i;
/*     */   }
/*     */ 
/*     */   protected final int paethPredictor(int paramInt1, int paramInt2, int paramInt3) {
/*  72 */     int i = paramInt1 + paramInt2 - paramInt3;
/*  73 */     int j = abs(i - paramInt1);
/*  74 */     int k = abs(i - paramInt2);
/*  75 */     int m = abs(i - paramInt3);
/*     */ 
/*  77 */     if ((j <= k) && (j <= m))
/*  78 */       return paramInt1;
/*  79 */     if (k <= m) {
/*  80 */       return paramInt2;
/*     */     }
/*  82 */     return paramInt3;
/*     */   }
/*     */ 
/*     */   public int filterRow(int paramInt1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[][] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/*  94 */     if (paramInt1 != 3) {
/*  95 */       System.arraycopy(paramArrayOfByte1, paramInt3, paramArrayOfByte[0], paramInt3, paramInt2);
/*     */ 
/*  98 */       return 0;
/*     */     }
/*     */ 
/* 101 */     int[] arrayOfInt = new int[5];
/* 102 */     for (int i = 0; i < 5; i++) {
/* 103 */       arrayOfInt[i] = 2147483647;
/*     */     }
/*     */ 
/* 107 */     i = 0;
/*     */ 
/* 109 */     for (int k = paramInt3; k < paramInt2 + paramInt3; k++) {
/* 110 */       m = paramArrayOfByte1[k] & 0xFF;
/* 111 */       i += m;
/*     */     }
/*     */ 
/* 114 */     arrayOfInt[0] = i;
/*     */ 
/* 118 */     byte[] arrayOfByte = paramArrayOfByte[1];
/* 119 */     k = subFilter(paramArrayOfByte1, arrayOfByte, paramInt3, paramInt2);
/*     */ 
/* 124 */     arrayOfInt[1] = k;
/*     */ 
/* 128 */     arrayOfByte = paramArrayOfByte[2];
/* 129 */     k = upFilter(paramArrayOfByte1, paramArrayOfByte2, arrayOfByte, paramInt3, paramInt2);
/*     */ 
/* 135 */     arrayOfInt[2] = k;
/*     */ 
/* 139 */     arrayOfByte = paramArrayOfByte[3];
/* 140 */     k = 0;
/*     */     int n;
/*     */     int i1;
/*     */     int i2;
/*     */     int i3;
/* 142 */     for (int m = paramInt3; m < paramInt2 + paramInt3; m++) {
/* 143 */       n = paramArrayOfByte1[m] & 0xFF;
/* 144 */       i1 = paramArrayOfByte1[(m - paramInt3)] & 0xFF;
/* 145 */       i2 = paramArrayOfByte2[m] & 0xFF;
/* 146 */       i3 = n - (i1 + i2) / 2;
/* 147 */       arrayOfByte[m] = ((byte)i3);
/*     */ 
/* 149 */       k += abs(i3);
/*     */     }
/*     */ 
/* 152 */     arrayOfInt[3] = k;
/*     */ 
/* 156 */     arrayOfByte = paramArrayOfByte[4];
/* 157 */     k = 0;
/*     */ 
/* 159 */     for (m = paramInt3; m < paramInt2 + paramInt3; m++) {
/* 160 */       n = paramArrayOfByte1[m] & 0xFF;
/* 161 */       i1 = paramArrayOfByte1[(m - paramInt3)] & 0xFF;
/* 162 */       i2 = paramArrayOfByte2[m] & 0xFF;
/* 163 */       i3 = paramArrayOfByte2[(m - paramInt3)] & 0xFF;
/* 164 */       int i4 = paethPredictor(i1, i2, i3);
/* 165 */       int i5 = n - i4;
/* 166 */       arrayOfByte[m] = ((byte)i5);
/*     */ 
/* 168 */       k += abs(i5);
/*     */     }
/*     */ 
/* 171 */     arrayOfInt[4] = k;
/*     */ 
/* 174 */     int j = arrayOfInt[0];
/* 175 */     k = 0;
/*     */ 
/* 177 */     for (m = 1; m < 5; m++) {
/* 178 */       if (arrayOfInt[m] < j) {
/* 179 */         j = arrayOfInt[m];
/* 180 */         k = m;
/*     */       }
/*     */     }
/*     */ 
/* 184 */     if (k == 0) {
/* 185 */       System.arraycopy(paramArrayOfByte1, paramInt3, paramArrayOfByte[0], paramInt3, paramInt2);
/*     */     }
/*     */ 
/* 190 */     return k;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.RowFilter
 * JD-Core Version:    0.6.2
 */
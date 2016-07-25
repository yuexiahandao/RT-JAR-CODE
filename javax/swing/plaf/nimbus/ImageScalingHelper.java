/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ 
/*     */ class ImageScalingHelper
/*     */ {
/*  67 */   private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
/*     */   static final int PAINT_TOP_LEFT = 1;
/*     */   static final int PAINT_TOP = 2;
/*     */   static final int PAINT_TOP_RIGHT = 4;
/*     */   static final int PAINT_LEFT = 8;
/*     */   static final int PAINT_CENTER = 16;
/*     */   static final int PAINT_RIGHT = 32;
/*     */   static final int PAINT_BOTTOM_RIGHT = 64;
/*     */   static final int PAINT_BOTTOM = 128;
/*     */   static final int PAINT_BOTTOM_LEFT = 256;
/*     */   static final int PAINT_ALL = 512;
/*     */ 
/*     */   public static void paint(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Image paramImage, Insets paramInsets1, Insets paramInsets2, PaintType paramPaintType, int paramInt5)
/*     */   {
/* 105 */     if ((paramImage == null) || (paramImage.getWidth(null) <= 0) || (paramImage.getHeight(null) <= 0)) {
/* 106 */       return;
/*     */     }
/* 108 */     if (paramInsets1 == null) {
/* 109 */       paramInsets1 = EMPTY_INSETS;
/*     */     }
/* 111 */     if (paramInsets2 == null) {
/* 112 */       paramInsets2 = EMPTY_INSETS;
/*     */     }
/* 114 */     int i = paramImage.getWidth(null);
/* 115 */     int j = paramImage.getHeight(null);
/*     */ 
/* 117 */     if (paramPaintType == PaintType.CENTER)
/*     */     {
/* 119 */       paramGraphics.drawImage(paramImage, paramInt1 + (paramInt3 - i) / 2, paramInt2 + (paramInt4 - j) / 2, null);
/*     */     }
/*     */     else
/*     */     {
/*     */       int k;
/*     */       int m;
/*     */       int n;
/*     */       int i1;
/*     */       int i2;
/*     */       int i3;
/*     */       int i4;
/*     */       int i5;
/* 121 */       if (paramPaintType == PaintType.TILE)
/*     */       {
/* 123 */         k = 0;
/* 124 */         m = paramInt2; for (n = paramInt2 + paramInt4; m < n; 
/* 125 */           k = 0) {
/* 126 */           i1 = 0;
/* 127 */           i2 = paramInt1; for (i3 = paramInt1 + paramInt3; i2 < i3; 
/* 128 */             i1 = 0) {
/* 129 */             i4 = Math.min(i3, i2 + i - i1);
/* 130 */             i5 = Math.min(n, m + j - k);
/* 131 */             paramGraphics.drawImage(paramImage, i2, m, i4, i5, i1, k, i1 + i4 - i2, k + i5 - m, null);
/*     */ 
/* 128 */             i2 += i - i1;
/*     */           }
/* 125 */           m += j - k;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 137 */         k = paramInsets1.top;
/* 138 */         m = paramInsets1.left;
/* 139 */         n = paramInsets1.bottom;
/* 140 */         i1 = paramInsets1.right;
/*     */ 
/* 142 */         i2 = paramInsets2.top;
/* 143 */         i3 = paramInsets2.left;
/* 144 */         i4 = paramInsets2.bottom;
/* 145 */         i5 = paramInsets2.right;
/*     */ 
/* 148 */         if (k + n > j) {
/* 149 */           i4 = i2 = n = k = Math.max(0, j / 2);
/*     */         }
/* 151 */         if (m + i1 > i) {
/* 152 */           i3 = i5 = m = i1 = Math.max(0, i / 2);
/*     */         }
/*     */ 
/* 157 */         if (i2 + i4 > paramInt4) {
/* 158 */           i2 = i4 = Math.max(0, paramInt4 / 2 - 1);
/*     */         }
/* 160 */         if (i3 + i5 > paramInt3) {
/* 161 */           i3 = i5 = Math.max(0, paramInt3 / 2 - 1);
/*     */         }
/*     */ 
/* 164 */         boolean bool = paramPaintType == PaintType.PAINT9_STRETCH;
/* 165 */         if ((paramInt5 & 0x200) != 0) {
/* 166 */           paramInt5 = 0x1FF & (paramInt5 ^ 0xFFFFFFFF);
/*     */         }
/*     */ 
/* 169 */         if ((paramInt5 & 0x8) != 0) {
/* 170 */           drawChunk(paramImage, paramGraphics, bool, paramInt1, paramInt2 + i2, paramInt1 + i3, paramInt2 + paramInt4 - i4, 0, k, m, j - n, false);
/*     */         }
/*     */ 
/* 173 */         if ((paramInt5 & 0x1) != 0) {
/* 174 */           drawImage(paramImage, paramGraphics, paramInt1, paramInt2, paramInt1 + i3, paramInt2 + i2, 0, 0, m, k);
/*     */         }
/*     */ 
/* 177 */         if ((paramInt5 & 0x2) != 0) {
/* 178 */           drawChunk(paramImage, paramGraphics, bool, paramInt1 + i3, paramInt2, paramInt1 + paramInt3 - i5, paramInt2 + i2, m, 0, i - i1, k, true);
/*     */         }
/*     */ 
/* 181 */         if ((paramInt5 & 0x4) != 0) {
/* 182 */           drawImage(paramImage, paramGraphics, paramInt1 + paramInt3 - i5, paramInt2, paramInt1 + paramInt3, paramInt2 + i2, i - i1, 0, i, k);
/*     */         }
/*     */ 
/* 185 */         if ((paramInt5 & 0x20) != 0) {
/* 186 */           drawChunk(paramImage, paramGraphics, bool, paramInt1 + paramInt3 - i5, paramInt2 + i2, paramInt1 + paramInt3, paramInt2 + paramInt4 - i4, i - i1, k, i, j - n, false);
/*     */         }
/*     */ 
/* 190 */         if ((paramInt5 & 0x40) != 0) {
/* 191 */           drawImage(paramImage, paramGraphics, paramInt1 + paramInt3 - i5, paramInt2 + paramInt4 - i4, paramInt1 + paramInt3, paramInt2 + paramInt4, i - i1, j - n, i, j);
/*     */         }
/*     */ 
/* 194 */         if ((paramInt5 & 0x80) != 0) {
/* 195 */           drawChunk(paramImage, paramGraphics, bool, paramInt1 + i3, paramInt2 + paramInt4 - i4, paramInt1 + paramInt3 - i5, paramInt2 + paramInt4, m, j - n, i - i1, j, true);
/*     */         }
/*     */ 
/* 199 */         if ((paramInt5 & 0x100) != 0) {
/* 200 */           drawImage(paramImage, paramGraphics, paramInt1, paramInt2 + paramInt4 - i4, paramInt1 + i3, paramInt2 + paramInt4, 0, j - n, m, j);
/*     */         }
/*     */ 
/* 203 */         if ((paramInt5 & 0x10) != 0)
/* 204 */           drawImage(paramImage, paramGraphics, paramInt1 + i3, paramInt2 + i2, paramInt1 + paramInt3 - i5, paramInt2 + paramInt4 - i4, m, k, i - i1, j - n);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void drawChunk(Image paramImage, Graphics paramGraphics, boolean paramBoolean1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean2)
/*     */   {
/* 232 */     if ((paramInt3 - paramInt1 <= 0) || (paramInt4 - paramInt2 <= 0) || (paramInt7 - paramInt5 <= 0) || (paramInt8 - paramInt6 <= 0))
/*     */     {
/* 235 */       return;
/*     */     }
/* 237 */     if (paramBoolean1) {
/* 238 */       paramGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, null);
/*     */     }
/*     */     else {
/* 241 */       int i = paramInt7 - paramInt5;
/* 242 */       int j = paramInt8 - paramInt6;
/*     */       int k;
/*     */       int m;
/* 246 */       if (paramBoolean2) {
/* 247 */         k = i;
/* 248 */         m = 0;
/*     */       }
/*     */       else {
/* 251 */         k = 0;
/* 252 */         m = j;
/*     */       }
/* 254 */       while ((paramInt1 < paramInt3) && (paramInt2 < paramInt4)) {
/* 255 */         int n = Math.min(paramInt3, paramInt1 + i);
/* 256 */         int i1 = Math.min(paramInt4, paramInt2 + j);
/*     */ 
/* 258 */         paramGraphics.drawImage(paramImage, paramInt1, paramInt2, n, i1, paramInt5, paramInt6, paramInt5 + n - paramInt1, paramInt6 + i1 - paramInt2, null);
/*     */ 
/* 261 */         paramInt1 += k;
/* 262 */         paramInt2 += m;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void drawImage(Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
/*     */   {
/* 271 */     if ((paramInt3 - paramInt1 <= 0) || (paramInt4 - paramInt2 <= 0) || (paramInt7 - paramInt5 <= 0) || (paramInt8 - paramInt6 <= 0))
/*     */     {
/* 274 */       return;
/*     */     }
/* 276 */     paramGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, null);
/*     */   }
/*     */ 
/*     */   static enum PaintType
/*     */   {
/*  44 */     CENTER, 
/*     */ 
/*  50 */     TILE, 
/*     */ 
/*  56 */     PAINT9_STRETCH, 
/*     */ 
/*  62 */     PAINT9_TILE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ImageScalingHelper
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.image.BufferedImage;
/*     */ import sun.swing.CachedPainter;
/*     */ 
/*     */ public class Paint9Painter extends CachedPainter
/*     */ {
/*  67 */   private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
/*     */   public static final int PAINT_TOP_LEFT = 1;
/*     */   public static final int PAINT_TOP = 2;
/*     */   public static final int PAINT_TOP_RIGHT = 4;
/*     */   public static final int PAINT_LEFT = 8;
/*     */   public static final int PAINT_CENTER = 16;
/*     */   public static final int PAINT_RIGHT = 32;
/*     */   public static final int PAINT_BOTTOM_RIGHT = 64;
/*     */   public static final int PAINT_BOTTOM = 128;
/*     */   public static final int PAINT_BOTTOM_LEFT = 256;
/*     */   public static final int PAINT_ALL = 512;
/*     */ 
/*     */   public static boolean validImage(Image paramImage)
/*     */   {
/*  93 */     return (paramImage != null) && (paramImage.getWidth(null) > 0) && (paramImage.getHeight(null) > 0);
/*     */   }
/*     */ 
/*     */   public Paint9Painter(int paramInt)
/*     */   {
/*  99 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public void paint(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Image paramImage, Insets paramInsets1, Insets paramInsets2, PaintType paramPaintType, int paramInt5)
/*     */   {
/* 131 */     if (paramImage == null) {
/* 132 */       return;
/*     */     }
/* 134 */     super.paint(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, new Object[] { paramImage, paramInsets1, paramInsets2, paramPaintType, Integer.valueOf(paramInt5) });
/*     */   }
/*     */ 
/*     */   protected void paintToImage(Component paramComponent, Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */   {
/* 139 */     int i = 0;
/* 140 */     while (i < paramArrayOfObject.length) {
/* 141 */       Image localImage = (Image)paramArrayOfObject[(i++)];
/* 142 */       Insets localInsets1 = (Insets)paramArrayOfObject[(i++)];
/* 143 */       Insets localInsets2 = (Insets)paramArrayOfObject[(i++)];
/* 144 */       PaintType localPaintType = (PaintType)paramArrayOfObject[(i++)];
/* 145 */       int j = ((Integer)paramArrayOfObject[(i++)]).intValue();
/* 146 */       paint9(paramGraphics, 0, 0, paramInt1, paramInt2, localImage, localInsets1, localInsets2, localPaintType, j);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paint9(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Image paramImage, Insets paramInsets1, Insets paramInsets2, PaintType paramPaintType, int paramInt5)
/*     */   {
/* 153 */     if (!validImage(paramImage)) {
/* 154 */       return;
/*     */     }
/* 156 */     if (paramInsets1 == null) {
/* 157 */       paramInsets1 = EMPTY_INSETS;
/*     */     }
/* 159 */     if (paramInsets2 == null) {
/* 160 */       paramInsets2 = EMPTY_INSETS;
/*     */     }
/* 162 */     int i = paramImage.getWidth(null);
/* 163 */     int j = paramImage.getHeight(null);
/*     */ 
/* 165 */     if (paramPaintType == PaintType.CENTER)
/*     */     {
/* 167 */       paramGraphics.drawImage(paramImage, paramInt1 + (paramInt3 - i) / 2, paramInt2 + (paramInt4 - j) / 2, null);
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
/* 170 */       if (paramPaintType == PaintType.TILE)
/*     */       {
/* 172 */         k = 0;
/* 173 */         m = paramInt2; for (n = paramInt2 + paramInt4; m < n; 
/* 174 */           k = 0) {
/* 175 */           i1 = 0;
/* 176 */           i2 = paramInt1; for (i3 = paramInt1 + paramInt3; i2 < i3; 
/* 177 */             i1 = 0) {
/* 178 */             i4 = Math.min(i3, i2 + i - i1);
/* 179 */             i5 = Math.min(n, m + j - k);
/* 180 */             paramGraphics.drawImage(paramImage, i2, m, i4, i5, i1, k, i1 + i4 - i2, k + i5 - m, null);
/*     */ 
/* 177 */             i2 += i - i1;
/*     */           }
/* 174 */           m += j - k;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 187 */         k = paramInsets1.top;
/* 188 */         m = paramInsets1.left;
/* 189 */         n = paramInsets1.bottom;
/* 190 */         i1 = paramInsets1.right;
/*     */ 
/* 192 */         i2 = paramInsets2.top;
/* 193 */         i3 = paramInsets2.left;
/* 194 */         i4 = paramInsets2.bottom;
/* 195 */         i5 = paramInsets2.right;
/*     */ 
/* 198 */         if (k + n > j) {
/* 199 */           i4 = i2 = n = k = Math.max(0, j / 2);
/*     */         }
/* 201 */         if (m + i1 > i) {
/* 202 */           i3 = i5 = m = i1 = Math.max(0, i / 2);
/*     */         }
/*     */ 
/* 207 */         if (i2 + i4 > paramInt4) {
/* 208 */           i2 = i4 = Math.max(0, paramInt4 / 2 - 1);
/*     */         }
/* 210 */         if (i3 + i5 > paramInt3) {
/* 211 */           i3 = i5 = Math.max(0, paramInt3 / 2 - 1);
/*     */         }
/*     */ 
/* 214 */         boolean bool = paramPaintType == PaintType.PAINT9_STRETCH;
/* 215 */         if ((paramInt5 & 0x200) != 0) {
/* 216 */           paramInt5 = 0x1FF & (paramInt5 ^ 0xFFFFFFFF);
/*     */         }
/*     */ 
/* 219 */         if ((paramInt5 & 0x8) != 0) {
/* 220 */           drawChunk(paramImage, paramGraphics, bool, paramInt1, paramInt2 + i2, paramInt1 + i3, paramInt2 + paramInt4 - i4, 0, k, m, j - n, false);
/*     */         }
/*     */ 
/* 223 */         if ((paramInt5 & 0x1) != 0) {
/* 224 */           drawImage(paramImage, paramGraphics, paramInt1, paramInt2, paramInt1 + i3, paramInt2 + i2, 0, 0, m, k);
/*     */         }
/*     */ 
/* 227 */         if ((paramInt5 & 0x2) != 0) {
/* 228 */           drawChunk(paramImage, paramGraphics, bool, paramInt1 + i3, paramInt2, paramInt1 + paramInt3 - i5, paramInt2 + i2, m, 0, i - i1, k, true);
/*     */         }
/*     */ 
/* 231 */         if ((paramInt5 & 0x4) != 0) {
/* 232 */           drawImage(paramImage, paramGraphics, paramInt1 + paramInt3 - i5, paramInt2, paramInt1 + paramInt3, paramInt2 + i2, i - i1, 0, i, k);
/*     */         }
/*     */ 
/* 235 */         if ((paramInt5 & 0x20) != 0) {
/* 236 */           drawChunk(paramImage, paramGraphics, bool, paramInt1 + paramInt3 - i5, paramInt2 + i2, paramInt1 + paramInt3, paramInt2 + paramInt4 - i4, i - i1, k, i, j - n, false);
/*     */         }
/*     */ 
/* 240 */         if ((paramInt5 & 0x40) != 0) {
/* 241 */           drawImage(paramImage, paramGraphics, paramInt1 + paramInt3 - i5, paramInt2 + paramInt4 - i4, paramInt1 + paramInt3, paramInt2 + paramInt4, i - i1, j - n, i, j);
/*     */         }
/*     */ 
/* 244 */         if ((paramInt5 & 0x80) != 0) {
/* 245 */           drawChunk(paramImage, paramGraphics, bool, paramInt1 + i3, paramInt2 + paramInt4 - i4, paramInt1 + paramInt3 - i5, paramInt2 + paramInt4, m, j - n, i - i1, j, true);
/*     */         }
/*     */ 
/* 249 */         if ((paramInt5 & 0x100) != 0) {
/* 250 */           drawImage(paramImage, paramGraphics, paramInt1, paramInt2 + paramInt4 - i4, paramInt1 + i3, paramInt2 + paramInt4, 0, j - n, m, j);
/*     */         }
/*     */ 
/* 253 */         if ((paramInt5 & 0x10) != 0)
/* 254 */           drawImage(paramImage, paramGraphics, paramInt1 + i3, paramInt2 + i2, paramInt1 + paramInt3 - i5, paramInt2 + paramInt4 - i4, m, k, i - i1, j - n);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawImage(Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
/*     */   {
/* 264 */     if ((paramInt3 - paramInt1 <= 0) || (paramInt4 - paramInt2 <= 0) || (paramInt7 - paramInt5 <= 0) || (paramInt8 - paramInt6 <= 0))
/*     */     {
/* 267 */       return;
/*     */     }
/* 269 */     paramGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, null);
/*     */   }
/*     */ 
/*     */   private void drawChunk(Image paramImage, Graphics paramGraphics, boolean paramBoolean1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean2)
/*     */   {
/* 294 */     if ((paramInt3 - paramInt1 <= 0) || (paramInt4 - paramInt2 <= 0) || (paramInt7 - paramInt5 <= 0) || (paramInt8 - paramInt6 <= 0))
/*     */     {
/* 297 */       return;
/*     */     }
/* 299 */     if (paramBoolean1) {
/* 300 */       paramGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, null);
/*     */     }
/*     */     else {
/* 303 */       int i = paramInt7 - paramInt5;
/* 304 */       int j = paramInt8 - paramInt6;
/*     */       int k;
/*     */       int m;
/* 308 */       if (paramBoolean2) {
/* 309 */         k = i;
/* 310 */         m = 0;
/*     */       }
/*     */       else {
/* 313 */         k = 0;
/* 314 */         m = j;
/*     */       }
/* 316 */       while ((paramInt1 < paramInt3) && (paramInt2 < paramInt4)) {
/* 317 */         int n = Math.min(paramInt3, paramInt1 + i);
/* 318 */         int i1 = Math.min(paramInt4, paramInt2 + j);
/*     */ 
/* 320 */         paramGraphics.drawImage(paramImage, paramInt1, paramInt2, n, i1, paramInt5, paramInt6, paramInt5 + n - paramInt1, paramInt6 + i1 - paramInt2, null);
/*     */ 
/* 323 */         paramInt1 += k;
/* 324 */         paramInt2 += m;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Image createImage(Component paramComponent, int paramInt1, int paramInt2, GraphicsConfiguration paramGraphicsConfiguration, Object[] paramArrayOfObject)
/*     */   {
/* 335 */     if (paramGraphicsConfiguration == null) {
/* 336 */       return new BufferedImage(paramInt1, paramInt2, 2);
/*     */     }
/* 338 */     return paramGraphicsConfiguration.createCompatibleImage(paramInt1, paramInt2, 3);
/*     */   }
/*     */ 
/*     */   public static enum PaintType
/*     */   {
/*  45 */     CENTER, 
/*     */ 
/*  52 */     TILE, 
/*     */ 
/*  58 */     PAINT9_STRETCH, 
/*     */ 
/*  64 */     PAINT9_TILE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.plaf.synth.Paint9Painter
 * JD-Core Version:    0.6.2
 */
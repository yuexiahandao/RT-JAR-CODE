/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class SizeRequirements
/*     */   implements Serializable
/*     */ {
/*     */   public int minimum;
/*     */   public int preferred;
/*     */   public int maximum;
/*     */   public float alignment;
/*     */ 
/*     */   public SizeRequirements()
/*     */   {
/* 135 */     this.minimum = 0;
/* 136 */     this.preferred = 0;
/* 137 */     this.maximum = 0;
/* 138 */     this.alignment = 0.5F;
/*     */   }
/*     */ 
/*     */   public SizeRequirements(int paramInt1, int paramInt2, int paramInt3, float paramFloat)
/*     */   {
/* 151 */     this.minimum = paramInt1;
/* 152 */     this.preferred = paramInt2;
/* 153 */     this.maximum = paramInt3;
/* 154 */     this.alignment = (paramFloat < 0.0F ? 0.0F : paramFloat > 1.0F ? 1.0F : paramFloat);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 164 */     return "[" + this.minimum + "," + this.preferred + "," + this.maximum + "]@" + this.alignment;
/*     */   }
/*     */ 
/*     */   public static SizeRequirements getTiledSizeRequirements(SizeRequirements[] paramArrayOfSizeRequirements)
/*     */   {
/* 183 */     SizeRequirements localSizeRequirements1 = new SizeRequirements();
/* 184 */     for (int i = 0; i < paramArrayOfSizeRequirements.length; i++) {
/* 185 */       SizeRequirements localSizeRequirements2 = paramArrayOfSizeRequirements[i];
/* 186 */       localSizeRequirements1.minimum = ((int)Math.min(localSizeRequirements1.minimum + localSizeRequirements2.minimum, 2147483647L));
/* 187 */       localSizeRequirements1.preferred = ((int)Math.min(localSizeRequirements1.preferred + localSizeRequirements2.preferred, 2147483647L));
/* 188 */       localSizeRequirements1.maximum = ((int)Math.min(localSizeRequirements1.maximum + localSizeRequirements2.maximum, 2147483647L));
/*     */     }
/* 190 */     return localSizeRequirements1;
/*     */   }
/*     */ 
/*     */   public static SizeRequirements getAlignedSizeRequirements(SizeRequirements[] paramArrayOfSizeRequirements)
/*     */   {
/* 206 */     SizeRequirements localSizeRequirements1 = new SizeRequirements();
/* 207 */     SizeRequirements localSizeRequirements2 = new SizeRequirements();
/* 208 */     for (int i = 0; i < paramArrayOfSizeRequirements.length; i++) {
/* 209 */       SizeRequirements localSizeRequirements3 = paramArrayOfSizeRequirements[i];
/*     */ 
/* 211 */       k = (int)(localSizeRequirements3.alignment * localSizeRequirements3.minimum);
/* 212 */       int m = localSizeRequirements3.minimum - k;
/* 213 */       localSizeRequirements1.minimum = Math.max(k, localSizeRequirements1.minimum);
/* 214 */       localSizeRequirements2.minimum = Math.max(m, localSizeRequirements2.minimum);
/*     */ 
/* 216 */       k = (int)(localSizeRequirements3.alignment * localSizeRequirements3.preferred);
/* 217 */       m = localSizeRequirements3.preferred - k;
/* 218 */       localSizeRequirements1.preferred = Math.max(k, localSizeRequirements1.preferred);
/* 219 */       localSizeRequirements2.preferred = Math.max(m, localSizeRequirements2.preferred);
/*     */ 
/* 221 */       k = (int)(localSizeRequirements3.alignment * localSizeRequirements3.maximum);
/* 222 */       m = localSizeRequirements3.maximum - k;
/* 223 */       localSizeRequirements1.maximum = Math.max(k, localSizeRequirements1.maximum);
/* 224 */       localSizeRequirements2.maximum = Math.max(m, localSizeRequirements2.maximum);
/*     */     }
/* 226 */     i = (int)Math.min(localSizeRequirements1.minimum + localSizeRequirements2.minimum, 2147483647L);
/* 227 */     int j = (int)Math.min(localSizeRequirements1.preferred + localSizeRequirements2.preferred, 2147483647L);
/* 228 */     int k = (int)Math.min(localSizeRequirements1.maximum + localSizeRequirements2.maximum, 2147483647L);
/* 229 */     float f = 0.0F;
/* 230 */     if (i > 0) {
/* 231 */       f = localSizeRequirements1.minimum / i;
/* 232 */       f = f < 0.0F ? 0.0F : f > 1.0F ? 1.0F : f;
/*     */     }
/* 234 */     return new SizeRequirements(i, j, k, f);
/*     */   }
/*     */ 
/*     */   public static void calculateTiledPositions(int paramInt, SizeRequirements paramSizeRequirements, SizeRequirements[] paramArrayOfSizeRequirements, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 263 */     calculateTiledPositions(paramInt, paramSizeRequirements, paramArrayOfSizeRequirements, paramArrayOfInt1, paramArrayOfInt2, true);
/*     */   }
/*     */ 
/*     */   public static void calculateTiledPositions(int paramInt, SizeRequirements paramSizeRequirements, SizeRequirements[] paramArrayOfSizeRequirements, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean)
/*     */   {
/* 308 */     long l1 = 0L;
/* 309 */     long l2 = 0L;
/* 310 */     long l3 = 0L;
/* 311 */     for (int i = 0; i < paramArrayOfSizeRequirements.length; i++) {
/* 312 */       l1 += paramArrayOfSizeRequirements[i].minimum;
/* 313 */       l2 += paramArrayOfSizeRequirements[i].preferred;
/* 314 */       l3 += paramArrayOfSizeRequirements[i].maximum;
/*     */     }
/* 316 */     if (paramInt >= l2)
/* 317 */       expandedTile(paramInt, l1, l2, l3, paramArrayOfSizeRequirements, paramArrayOfInt1, paramArrayOfInt2, paramBoolean);
/*     */     else
/* 319 */       compressedTile(paramInt, l1, l2, l3, paramArrayOfSizeRequirements, paramArrayOfInt1, paramArrayOfInt2, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static void compressedTile(int paramInt, long paramLong1, long paramLong2, long paramLong3, SizeRequirements[] paramArrayOfSizeRequirements, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean)
/*     */   {
/* 329 */     float f1 = (float)Math.min(paramLong2 - paramInt, paramLong2 - paramLong1);
/* 330 */     float f2 = paramLong2 - paramLong1 == 0L ? 0.0F : f1 / (float)(paramLong2 - paramLong1);
/*     */     int i;
/*     */     int j;
/*     */     SizeRequirements localSizeRequirements;
/*     */     float f3;
/* 334 */     if (paramBoolean)
/*     */     {
/* 336 */       i = 0;
/* 337 */       for (j = 0; j < paramArrayOfInt2.length; j++) {
/* 338 */         paramArrayOfInt1[j] = i;
/* 339 */         localSizeRequirements = paramArrayOfSizeRequirements[j];
/* 340 */         f3 = f2 * (localSizeRequirements.preferred - localSizeRequirements.minimum);
/* 341 */         paramArrayOfInt2[j] = ((int)(localSizeRequirements.preferred - f3));
/* 342 */         i = (int)Math.min(i + paramArrayOfInt2[j], 2147483647L);
/*     */       }
/*     */     }
/*     */     else {
/* 346 */       i = paramInt;
/* 347 */       for (j = 0; j < paramArrayOfInt2.length; j++) {
/* 348 */         localSizeRequirements = paramArrayOfSizeRequirements[j];
/* 349 */         f3 = f2 * (localSizeRequirements.preferred - localSizeRequirements.minimum);
/* 350 */         paramArrayOfInt2[j] = ((int)(localSizeRequirements.preferred - f3));
/* 351 */         paramArrayOfInt1[j] = (i - paramArrayOfInt2[j]);
/* 352 */         i = (int)Math.max(i - paramArrayOfInt2[j], 0L);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void expandedTile(int paramInt, long paramLong1, long paramLong2, long paramLong3, SizeRequirements[] paramArrayOfSizeRequirements, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean)
/*     */   {
/* 363 */     float f1 = (float)Math.min(paramInt - paramLong2, paramLong3 - paramLong2);
/* 364 */     float f2 = paramLong3 - paramLong2 == 0L ? 0.0F : f1 / (float)(paramLong3 - paramLong2);
/*     */     int i;
/*     */     int j;
/*     */     SizeRequirements localSizeRequirements;
/*     */     int k;
/* 368 */     if (paramBoolean)
/*     */     {
/* 370 */       i = 0;
/* 371 */       for (j = 0; j < paramArrayOfInt2.length; j++) {
/* 372 */         paramArrayOfInt1[j] = i;
/* 373 */         localSizeRequirements = paramArrayOfSizeRequirements[j];
/* 374 */         k = (int)(f2 * (localSizeRequirements.maximum - localSizeRequirements.preferred));
/* 375 */         paramArrayOfInt2[j] = ((int)Math.min(localSizeRequirements.preferred + k, 2147483647L));
/* 376 */         i = (int)Math.min(i + paramArrayOfInt2[j], 2147483647L);
/*     */       }
/*     */     }
/*     */     else {
/* 380 */       i = paramInt;
/* 381 */       for (j = 0; j < paramArrayOfInt2.length; j++) {
/* 382 */         localSizeRequirements = paramArrayOfSizeRequirements[j];
/* 383 */         k = (int)(f2 * (localSizeRequirements.maximum - localSizeRequirements.preferred));
/* 384 */         paramArrayOfInt2[j] = ((int)Math.min(localSizeRequirements.preferred + k, 2147483647L));
/* 385 */         paramArrayOfInt1[j] = (i - paramArrayOfInt2[j]);
/* 386 */         i = (int)Math.max(i - paramArrayOfInt2[j], 0L);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void calculateAlignedPositions(int paramInt, SizeRequirements paramSizeRequirements, SizeRequirements[] paramArrayOfSizeRequirements, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 421 */     calculateAlignedPositions(paramInt, paramSizeRequirements, paramArrayOfSizeRequirements, paramArrayOfInt1, paramArrayOfInt2, true);
/*     */   }
/*     */ 
/*     */   public static void calculateAlignedPositions(int paramInt, SizeRequirements paramSizeRequirements, SizeRequirements[] paramArrayOfSizeRequirements, int[] paramArrayOfInt1, int[] paramArrayOfInt2, boolean paramBoolean)
/*     */   {
/* 461 */     float f1 = paramBoolean ? paramSizeRequirements.alignment : 1.0F - paramSizeRequirements.alignment;
/* 462 */     int i = (int)(paramInt * f1);
/* 463 */     int j = paramInt - i;
/* 464 */     for (int k = 0; k < paramArrayOfSizeRequirements.length; k++) {
/* 465 */       SizeRequirements localSizeRequirements = paramArrayOfSizeRequirements[k];
/* 466 */       float f2 = paramBoolean ? localSizeRequirements.alignment : 1.0F - localSizeRequirements.alignment;
/* 467 */       int m = (int)(localSizeRequirements.maximum * f2);
/* 468 */       int n = localSizeRequirements.maximum - m;
/* 469 */       int i1 = Math.min(i, m);
/* 470 */       int i2 = Math.min(j, n);
/*     */ 
/* 472 */       paramArrayOfInt1[k] = (i - i1);
/* 473 */       paramArrayOfInt2[k] = ((int)Math.min(i1 + i2, 2147483647L));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int[] adjustSizes(int paramInt, SizeRequirements[] paramArrayOfSizeRequirements)
/*     */   {
/* 486 */     return new int[0];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SizeRequirements
 * JD-Core Version:    0.6.2
 */
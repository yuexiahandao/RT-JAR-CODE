/*     */ package javax.imageio;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.image.BufferedImage;
/*     */ 
/*     */ public class ImageReadParam extends IIOParam
/*     */ {
/* 142 */   protected boolean canSetSourceRenderSize = false;
/*     */ 
/* 152 */   protected Dimension sourceRenderSize = null;
/*     */ 
/* 159 */   protected BufferedImage destination = null;
/*     */ 
/* 166 */   protected int[] destinationBands = null;
/*     */ 
/* 177 */   protected int minProgressivePass = 0;
/*     */ 
/* 191 */   protected int numProgressivePasses = 2147483647;
/*     */ 
/*     */   public void setDestinationType(ImageTypeSpecifier paramImageTypeSpecifier)
/*     */   {
/* 200 */     super.setDestinationType(paramImageTypeSpecifier);
/* 201 */     setDestination(null);
/*     */   }
/*     */ 
/*     */   public void setDestination(BufferedImage paramBufferedImage)
/*     */   {
/* 232 */     this.destination = paramBufferedImage;
/*     */   }
/*     */ 
/*     */   public BufferedImage getDestination()
/*     */   {
/* 245 */     return this.destination;
/*     */   }
/*     */ 
/*     */   public void setDestinationBands(int[] paramArrayOfInt)
/*     */   {
/* 282 */     if (paramArrayOfInt == null) {
/* 283 */       this.destinationBands = null;
/*     */     } else {
/* 285 */       int i = paramArrayOfInt.length;
/* 286 */       for (int j = 0; j < i; j++) {
/* 287 */         int k = paramArrayOfInt[j];
/* 288 */         if (k < 0) {
/* 289 */           throw new IllegalArgumentException("Band value < 0!");
/*     */         }
/* 291 */         for (int m = j + 1; m < i; m++) {
/* 292 */           if (k == paramArrayOfInt[m]) {
/* 293 */             throw new IllegalArgumentException("Duplicate band value!");
/*     */           }
/*     */         }
/*     */       }
/* 297 */       this.destinationBands = ((int[])paramArrayOfInt.clone());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] getDestinationBands()
/*     */   {
/* 312 */     if (this.destinationBands == null) {
/* 313 */       return null;
/*     */     }
/* 315 */     return (int[])this.destinationBands.clone();
/*     */   }
/*     */ 
/*     */   public boolean canSetSourceRenderSize()
/*     */   {
/* 334 */     return this.canSetSourceRenderSize;
/*     */   }
/*     */ 
/*     */   public void setSourceRenderSize(Dimension paramDimension)
/*     */     throws UnsupportedOperationException
/*     */   {
/* 374 */     if (!canSetSourceRenderSize()) {
/* 375 */       throw new UnsupportedOperationException("Can't set source render size!");
/*     */     }
/*     */ 
/* 379 */     if (paramDimension == null) {
/* 380 */       this.sourceRenderSize = null;
/*     */     } else {
/* 382 */       if ((paramDimension.width <= 0) || (paramDimension.height <= 0)) {
/* 383 */         throw new IllegalArgumentException("width or height <= 0!");
/*     */       }
/* 385 */       this.sourceRenderSize = ((Dimension)paramDimension.clone());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getSourceRenderSize()
/*     */   {
/* 401 */     return this.sourceRenderSize == null ? null : (Dimension)this.sourceRenderSize.clone();
/*     */   }
/*     */ 
/*     */   public void setSourceProgressivePasses(int paramInt1, int paramInt2)
/*     */   {
/* 446 */     if (paramInt1 < 0) {
/* 447 */       throw new IllegalArgumentException("minPass < 0!");
/*     */     }
/* 449 */     if (paramInt2 <= 0) {
/* 450 */       throw new IllegalArgumentException("numPasses <= 0!");
/*     */     }
/* 452 */     if ((paramInt2 != 2147483647) && ((paramInt1 + paramInt2 - 1 & 0x80000000) != 0))
/*     */     {
/* 454 */       throw new IllegalArgumentException("minPass + numPasses - 1 > INTEGER.MAX_VALUE!");
/*     */     }
/*     */ 
/* 458 */     this.minProgressivePass = paramInt1;
/* 459 */     this.numProgressivePasses = paramInt2;
/*     */   }
/*     */ 
/*     */   public int getSourceMinProgressivePass()
/*     */   {
/* 473 */     return this.minProgressivePass;
/*     */   }
/*     */ 
/*     */   public int getSourceMaxProgressivePass()
/*     */   {
/* 487 */     if (this.numProgressivePasses == 2147483647) {
/* 488 */       return 2147483647;
/*     */     }
/* 490 */     return this.minProgressivePass + this.numProgressivePasses - 1;
/*     */   }
/*     */ 
/*     */   public int getSourceNumProgressivePasses()
/*     */   {
/* 506 */     return this.numProgressivePasses;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.ImageReadParam
 * JD-Core Version:    0.6.2
 */
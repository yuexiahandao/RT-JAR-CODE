/*     */ package javax.imageio;
/*     */ 
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ 
/*     */ public abstract class IIOParam
/*     */ {
/*  54 */   protected Rectangle sourceRegion = null;
/*     */ 
/*  61 */   protected int sourceXSubsampling = 1;
/*     */ 
/*  68 */   protected int sourceYSubsampling = 1;
/*     */ 
/*  76 */   protected int subsamplingXOffset = 0;
/*     */ 
/*  84 */   protected int subsamplingYOffset = 0;
/*     */ 
/*  93 */   protected int[] sourceBands = null;
/*     */ 
/* 101 */   protected ImageTypeSpecifier destinationType = null;
/*     */ 
/* 107 */   protected Point destinationOffset = new Point(0, 0);
/*     */ 
/* 121 */   protected IIOParamController defaultController = null;
/*     */ 
/* 135 */   protected IIOParamController controller = null;
/*     */ 
/*     */   protected IIOParam()
/*     */   {
/* 141 */     this.controller = this.defaultController;
/*     */   }
/*     */ 
/*     */   public void setSourceRegion(Rectangle paramRectangle)
/*     */   {
/* 182 */     if (paramRectangle == null) {
/* 183 */       this.sourceRegion = null;
/* 184 */       return;
/*     */     }
/*     */ 
/* 187 */     if (paramRectangle.x < 0) {
/* 188 */       throw new IllegalArgumentException("sourceRegion.x < 0!");
/*     */     }
/* 190 */     if (paramRectangle.y < 0) {
/* 191 */       throw new IllegalArgumentException("sourceRegion.y < 0!");
/*     */     }
/* 193 */     if (paramRectangle.width <= 0) {
/* 194 */       throw new IllegalArgumentException("sourceRegion.width <= 0!");
/*     */     }
/* 196 */     if (paramRectangle.height <= 0) {
/* 197 */       throw new IllegalArgumentException("sourceRegion.height <= 0!");
/*     */     }
/*     */ 
/* 201 */     if (paramRectangle.width <= this.subsamplingXOffset) {
/* 202 */       throw new IllegalStateException("sourceRegion.width <= subsamplingXOffset!");
/*     */     }
/*     */ 
/* 205 */     if (paramRectangle.height <= this.subsamplingYOffset) {
/* 206 */       throw new IllegalStateException("sourceRegion.height <= subsamplingYOffset!");
/*     */     }
/*     */ 
/* 210 */     this.sourceRegion = ((Rectangle)paramRectangle.clone());
/*     */   }
/*     */ 
/*     */   public Rectangle getSourceRegion()
/*     */   {
/* 225 */     if (this.sourceRegion == null) {
/* 226 */       return null;
/*     */     }
/* 228 */     return (Rectangle)this.sourceRegion.clone();
/*     */   }
/*     */ 
/*     */   public void setSourceSubsampling(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 303 */     if (paramInt1 <= 0) {
/* 304 */       throw new IllegalArgumentException("sourceXSubsampling <= 0!");
/*     */     }
/* 306 */     if (paramInt2 <= 0) {
/* 307 */       throw new IllegalArgumentException("sourceYSubsampling <= 0!");
/*     */     }
/* 309 */     if ((paramInt3 < 0) || (paramInt3 >= paramInt1))
/*     */     {
/* 311 */       throw new IllegalArgumentException("subsamplingXOffset out of range!");
/*     */     }
/*     */ 
/* 314 */     if ((paramInt4 < 0) || (paramInt4 >= paramInt2))
/*     */     {
/* 316 */       throw new IllegalArgumentException("subsamplingYOffset out of range!");
/*     */     }
/*     */ 
/* 321 */     if ((this.sourceRegion != null) && (
/* 322 */       (paramInt3 >= this.sourceRegion.width) || (paramInt4 >= this.sourceRegion.height)))
/*     */     {
/* 324 */       throw new IllegalStateException("region contains no pixels!");
/*     */     }
/*     */ 
/* 328 */     this.sourceXSubsampling = paramInt1;
/* 329 */     this.sourceYSubsampling = paramInt2;
/* 330 */     this.subsamplingXOffset = paramInt3;
/* 331 */     this.subsamplingYOffset = paramInt4;
/*     */   }
/*     */ 
/*     */   public int getSourceXSubsampling()
/*     */   {
/* 346 */     return this.sourceXSubsampling;
/*     */   }
/*     */ 
/*     */   public int getSourceYSubsampling()
/*     */   {
/* 361 */     return this.sourceYSubsampling;
/*     */   }
/*     */ 
/*     */   public int getSubsamplingXOffset()
/*     */   {
/* 376 */     return this.subsamplingXOffset;
/*     */   }
/*     */ 
/*     */   public int getSubsamplingYOffset()
/*     */   {
/* 391 */     return this.subsamplingYOffset;
/*     */   }
/*     */ 
/*     */   public void setSourceBands(int[] paramArrayOfInt)
/*     */   {
/* 424 */     if (paramArrayOfInt == null) {
/* 425 */       this.sourceBands = null;
/*     */     } else {
/* 427 */       int i = paramArrayOfInt.length;
/* 428 */       for (int j = 0; j < i; j++) {
/* 429 */         int k = paramArrayOfInt[j];
/* 430 */         if (k < 0) {
/* 431 */           throw new IllegalArgumentException("Band value < 0!");
/*     */         }
/* 433 */         for (int m = j + 1; m < i; m++) {
/* 434 */           if (k == paramArrayOfInt[m]) {
/* 435 */             throw new IllegalArgumentException("Duplicate band value!");
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 440 */       this.sourceBands = ((int[])paramArrayOfInt.clone());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] getSourceBands()
/*     */   {
/* 460 */     if (this.sourceBands == null) {
/* 461 */       return null;
/*     */     }
/* 463 */     return (int[])this.sourceBands.clone();
/*     */   }
/*     */ 
/*     */   public void setDestinationType(ImageTypeSpecifier paramImageTypeSpecifier)
/*     */   {
/* 498 */     this.destinationType = paramImageTypeSpecifier;
/*     */   }
/*     */ 
/*     */   public ImageTypeSpecifier getDestinationType()
/*     */   {
/* 514 */     return this.destinationType;
/*     */   }
/*     */ 
/*     */   public void setDestinationOffset(Point paramPoint)
/*     */   {
/* 547 */     if (paramPoint == null) {
/* 548 */       throw new IllegalArgumentException("destinationOffset == null!");
/*     */     }
/* 550 */     this.destinationOffset = ((Point)paramPoint.clone());
/*     */   }
/*     */ 
/*     */   public Point getDestinationOffset()
/*     */   {
/* 566 */     return (Point)this.destinationOffset.clone();
/*     */   }
/*     */ 
/*     */   public void setController(IIOParamController paramIIOParamController)
/*     */   {
/* 588 */     this.controller = paramIIOParamController;
/*     */   }
/*     */ 
/*     */   public IIOParamController getController()
/*     */   {
/* 607 */     return this.controller;
/*     */   }
/*     */ 
/*     */   public IIOParamController getDefaultController()
/*     */   {
/* 625 */     return this.defaultController;
/*     */   }
/*     */ 
/*     */   public boolean hasController()
/*     */   {
/* 643 */     return this.controller != null;
/*     */   }
/*     */ 
/*     */   public boolean activateController()
/*     */   {
/* 671 */     if (!hasController()) {
/* 672 */       throw new IllegalStateException("hasController() == false!");
/*     */     }
/* 674 */     return getController().activate(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.IIOParam
 * JD-Core Version:    0.6.2
 */
/*     */ package java.awt;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class GridLayout
/*     */   implements LayoutManager, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7411804673224730901L;
/*     */   int hgap;
/*     */   int vgap;
/*     */   int rows;
/*     */   int cols;
/*     */ 
/*     */   public GridLayout()
/*     */   {
/* 150 */     this(1, 0, 0, 0);
/*     */   }
/*     */ 
/*     */   public GridLayout(int paramInt1, int paramInt2)
/*     */   {
/* 166 */     this(paramInt1, paramInt2, 0, 0);
/*     */   }
/*     */ 
/*     */   public GridLayout(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 194 */     if ((paramInt1 == 0) && (paramInt2 == 0)) {
/* 195 */       throw new IllegalArgumentException("rows and cols cannot both be zero");
/*     */     }
/* 197 */     this.rows = paramInt1;
/* 198 */     this.cols = paramInt2;
/* 199 */     this.hgap = paramInt3;
/* 200 */     this.vgap = paramInt4;
/*     */   }
/*     */ 
/*     */   public int getRows()
/*     */   {
/* 209 */     return this.rows;
/*     */   }
/*     */ 
/*     */   public void setRows(int paramInt)
/*     */   {
/* 220 */     if ((paramInt == 0) && (this.cols == 0)) {
/* 221 */       throw new IllegalArgumentException("rows and cols cannot both be zero");
/*     */     }
/* 223 */     this.rows = paramInt;
/*     */   }
/*     */ 
/*     */   public int getColumns()
/*     */   {
/* 232 */     return this.cols;
/*     */   }
/*     */ 
/*     */   public void setColumns(int paramInt)
/*     */   {
/* 248 */     if ((paramInt == 0) && (this.rows == 0)) {
/* 249 */       throw new IllegalArgumentException("rows and cols cannot both be zero");
/*     */     }
/* 251 */     this.cols = paramInt;
/*     */   }
/*     */ 
/*     */   public int getHgap()
/*     */   {
/* 260 */     return this.hgap;
/*     */   }
/*     */ 
/*     */   public void setHgap(int paramInt)
/*     */   {
/* 269 */     this.hgap = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVgap()
/*     */   {
/* 278 */     return this.vgap;
/*     */   }
/*     */ 
/*     */   public void setVgap(int paramInt)
/*     */   {
/* 287 */     this.vgap = paramInt;
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/* 326 */     synchronized (paramContainer.getTreeLock()) {
/* 327 */       Insets localInsets = paramContainer.getInsets();
/* 328 */       int i = paramContainer.getComponentCount();
/* 329 */       int j = this.rows;
/* 330 */       int k = this.cols;
/*     */ 
/* 332 */       if (j > 0)
/* 333 */         k = (i + j - 1) / j;
/*     */       else {
/* 335 */         j = (i + k - 1) / k;
/*     */       }
/* 337 */       int m = 0;
/* 338 */       int n = 0;
/* 339 */       for (int i1 = 0; i1 < i; i1++) {
/* 340 */         Component localComponent = paramContainer.getComponent(i1);
/* 341 */         Dimension localDimension = localComponent.getPreferredSize();
/* 342 */         if (m < localDimension.width) {
/* 343 */           m = localDimension.width;
/*     */         }
/* 345 */         if (n < localDimension.height) {
/* 346 */           n = localDimension.height;
/*     */         }
/*     */       }
/* 349 */       return new Dimension(localInsets.left + localInsets.right + k * m + (k - 1) * this.hgap, localInsets.top + localInsets.bottom + j * n + (j - 1) * this.vgap);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 375 */     synchronized (paramContainer.getTreeLock()) {
/* 376 */       Insets localInsets = paramContainer.getInsets();
/* 377 */       int i = paramContainer.getComponentCount();
/* 378 */       int j = this.rows;
/* 379 */       int k = this.cols;
/*     */ 
/* 381 */       if (j > 0)
/* 382 */         k = (i + j - 1) / j;
/*     */       else {
/* 384 */         j = (i + k - 1) / k;
/*     */       }
/* 386 */       int m = 0;
/* 387 */       int n = 0;
/* 388 */       for (int i1 = 0; i1 < i; i1++) {
/* 389 */         Component localComponent = paramContainer.getComponent(i1);
/* 390 */         Dimension localDimension = localComponent.getMinimumSize();
/* 391 */         if (m < localDimension.width) {
/* 392 */           m = localDimension.width;
/*     */         }
/* 394 */         if (n < localDimension.height) {
/* 395 */           n = localDimension.height;
/*     */         }
/*     */       }
/* 398 */       return new Dimension(localInsets.left + localInsets.right + k * m + (k - 1) * this.hgap, localInsets.top + localInsets.bottom + j * n + (j - 1) * this.vgap);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 422 */     synchronized (paramContainer.getTreeLock()) {
/* 423 */       Insets localInsets = paramContainer.getInsets();
/* 424 */       int i = paramContainer.getComponentCount();
/* 425 */       int j = this.rows;
/* 426 */       int k = this.cols;
/* 427 */       boolean bool = paramContainer.getComponentOrientation().isLeftToRight();
/*     */ 
/* 429 */       if (i == 0) {
/* 430 */         return;
/*     */       }
/* 432 */       if (j > 0)
/* 433 */         k = (i + j - 1) / j;
/*     */       else {
/* 435 */         j = (i + k - 1) / k;
/*     */       }
/*     */ 
/* 442 */       int m = (k - 1) * this.hgap;
/* 443 */       int n = paramContainer.width - (localInsets.left + localInsets.right);
/* 444 */       int i1 = (n - m) / k;
/* 445 */       int i2 = (n - (i1 * k + m)) / 2;
/*     */ 
/* 447 */       int i3 = (j - 1) * this.vgap;
/* 448 */       int i4 = paramContainer.height - (localInsets.top + localInsets.bottom);
/* 449 */       int i5 = (i4 - i3) / j;
/* 450 */       int i6 = (i4 - (i5 * j + i3)) / 2;
/*     */       int i7;
/*     */       int i8;
/*     */       int i9;
/*     */       int i10;
/*     */       int i11;
/* 451 */       if (bool) {
/* 452 */         i7 = 0; for (i8 = localInsets.left + i2; i7 < k; i8 += i1 + this.hgap) {
/* 453 */           i9 = 0; for (i10 = localInsets.top + i6; i9 < j; i10 += i5 + this.vgap) {
/* 454 */             i11 = i9 * k + i7;
/* 455 */             if (i11 < i)
/* 456 */               paramContainer.getComponent(i11).setBounds(i8, i10, i1, i5);
/* 453 */             i9++;
/*     */           }
/* 452 */           i7++;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 461 */         i7 = 0; for (i8 = paramContainer.width - localInsets.right - i1 - i2; i7 < k; i8 -= i1 + this.hgap) {
/* 462 */           i9 = 0; for (i10 = localInsets.top + i6; i9 < j; i10 += i5 + this.vgap) {
/* 463 */             i11 = i9 * k + i7;
/* 464 */             if (i11 < i)
/* 465 */               paramContainer.getComponent(i11).setBounds(i8, i10, i1, i5);
/* 462 */             i9++;
/*     */           }
/* 461 */           i7++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 478 */     return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + ",rows=" + this.rows + ",cols=" + this.cols + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.GridLayout
 * JD-Core Version:    0.6.2
 */
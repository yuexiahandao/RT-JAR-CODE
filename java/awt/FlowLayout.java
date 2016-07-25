/*     */ package java.awt;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class FlowLayout
/*     */   implements LayoutManager, Serializable
/*     */ {
/*     */   public static final int LEFT = 0;
/*     */   public static final int CENTER = 1;
/*     */   public static final int RIGHT = 2;
/*     */   public static final int LEADING = 3;
/*     */   public static final int TRAILING = 4;
/*     */   int align;
/*     */   int newAlign;
/*     */   int hgap;
/*     */   int vgap;
/*     */   private boolean alignOnBaseline;
/*     */   private static final long serialVersionUID = -7262534875583282631L;
/*     */   private static final int currentSerialVersion = 1;
/* 659 */   private int serialVersionOnStream = 1;
/*     */ 
/*     */   public FlowLayout()
/*     */   {
/* 203 */     this(1, 5, 5);
/*     */   }
/*     */ 
/*     */   public FlowLayout(int paramInt)
/*     */   {
/* 216 */     this(paramInt, 5, 5);
/*     */   }
/*     */ 
/*     */   public FlowLayout(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 236 */     this.hgap = paramInt2;
/* 237 */     this.vgap = paramInt3;
/* 238 */     setAlignment(paramInt1);
/*     */   }
/*     */ 
/*     */   public int getAlignment()
/*     */   {
/* 252 */     return this.newAlign;
/*     */   }
/*     */ 
/*     */   public void setAlignment(int paramInt)
/*     */   {
/* 270 */     this.newAlign = paramInt;
/*     */ 
/* 276 */     switch (paramInt) {
/*     */     case 3:
/* 278 */       this.align = 0;
/* 279 */       break;
/*     */     case 4:
/* 281 */       this.align = 2;
/* 282 */       break;
/*     */     default:
/* 284 */       this.align = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getHgap()
/*     */   {
/* 301 */     return this.hgap;
/*     */   }
/*     */ 
/*     */   public void setHgap(int paramInt)
/*     */   {
/* 316 */     this.hgap = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVgap()
/*     */   {
/* 331 */     return this.vgap;
/*     */   }
/*     */ 
/*     */   public void setVgap(int paramInt)
/*     */   {
/* 345 */     this.vgap = paramInt;
/*     */   }
/*     */ 
/*     */   public void setAlignOnBaseline(boolean paramBoolean)
/*     */   {
/* 358 */     this.alignOnBaseline = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getAlignOnBaseline()
/*     */   {
/* 370 */     return this.alignOnBaseline;
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
/* 403 */     synchronized (paramContainer.getTreeLock()) {
/* 404 */       Dimension localDimension1 = new Dimension(0, 0);
/* 405 */       int i = paramContainer.getComponentCount();
/* 406 */       int j = 1;
/* 407 */       boolean bool = getAlignOnBaseline();
/* 408 */       int k = 0;
/* 409 */       int m = 0;
/*     */ 
/* 411 */       for (int n = 0; n < i; n++) {
/* 412 */         Component localComponent = paramContainer.getComponent(n);
/* 413 */         if (localComponent.isVisible()) {
/* 414 */           Dimension localDimension2 = localComponent.getPreferredSize();
/* 415 */           localDimension1.height = Math.max(localDimension1.height, localDimension2.height);
/* 416 */           if (j != 0)
/* 417 */             j = 0;
/*     */           else {
/* 419 */             localDimension1.width += this.hgap;
/*     */           }
/* 421 */           localDimension1.width += localDimension2.width;
/* 422 */           if (bool) {
/* 423 */             int i1 = localComponent.getBaseline(localDimension2.width, localDimension2.height);
/* 424 */             if (i1 >= 0) {
/* 425 */               k = Math.max(k, i1);
/* 426 */               m = Math.max(m, localDimension2.height - i1);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 431 */       if (bool) {
/* 432 */         localDimension1.height = Math.max(k + m, localDimension1.height);
/*     */       }
/* 434 */       Insets localInsets = paramContainer.getInsets();
/* 435 */       localDimension1.width += localInsets.left + localInsets.right + this.hgap * 2;
/* 436 */       localDimension1.height += localInsets.top + localInsets.bottom + this.vgap * 2;
/* 437 */       return localDimension1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 452 */     synchronized (paramContainer.getTreeLock()) {
/* 453 */       boolean bool = getAlignOnBaseline();
/* 454 */       Dimension localDimension1 = new Dimension(0, 0);
/* 455 */       int i = paramContainer.getComponentCount();
/* 456 */       int j = 0;
/* 457 */       int k = 0;
/* 458 */       int m = 1;
/*     */ 
/* 460 */       for (int n = 0; n < i; n++) {
/* 461 */         Component localComponent = paramContainer.getComponent(n);
/* 462 */         if (localComponent.visible) {
/* 463 */           Dimension localDimension2 = localComponent.getMinimumSize();
/* 464 */           localDimension1.height = Math.max(localDimension1.height, localDimension2.height);
/* 465 */           if (m != 0)
/* 466 */             m = 0;
/*     */           else {
/* 468 */             localDimension1.width += this.hgap;
/*     */           }
/* 470 */           localDimension1.width += localDimension2.width;
/* 471 */           if (bool) {
/* 472 */             int i1 = localComponent.getBaseline(localDimension2.width, localDimension2.height);
/* 473 */             if (i1 >= 0) {
/* 474 */               j = Math.max(j, i1);
/* 475 */               k = Math.max(k, localDimension1.height - i1);
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 482 */       if (bool) {
/* 483 */         localDimension1.height = Math.max(j + k, localDimension1.height);
/*     */       }
/*     */ 
/* 486 */       Insets localInsets = paramContainer.getInsets();
/* 487 */       localDimension1.width += localInsets.left + localInsets.right + this.hgap * 2;
/* 488 */       localDimension1.height += localInsets.top + localInsets.bottom + this.vgap * 2;
/* 489 */       return localDimension1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private int moveComponents(Container paramContainer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1, boolean paramBoolean2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 518 */     switch (this.newAlign) {
/*     */     case 0:
/* 520 */       paramInt1 += (paramBoolean1 ? 0 : paramInt3);
/* 521 */       break;
/*     */     case 1:
/* 523 */       paramInt1 += paramInt3 / 2;
/* 524 */       break;
/*     */     case 2:
/* 526 */       paramInt1 += (paramBoolean1 ? paramInt3 : 0);
/* 527 */       break;
/*     */     case 3:
/* 529 */       break;
/*     */     case 4:
/* 531 */       paramInt1 += paramInt3;
/*     */     }
/*     */ 
/* 534 */     int i = 0;
/* 535 */     int j = 0;
/* 536 */     int k = 0;
/* 537 */     if (paramBoolean2) {
/* 538 */       m = 0;
/* 539 */       for (int n = paramInt5; n < paramInt6; n++) {
/* 540 */         Component localComponent2 = paramContainer.getComponent(n);
/* 541 */         if (localComponent2.visible) {
/* 542 */           if (paramArrayOfInt1[n] >= 0) {
/* 543 */             i = Math.max(i, paramArrayOfInt1[n]);
/* 544 */             m = Math.max(m, paramArrayOfInt2[n]);
/*     */           }
/*     */           else {
/* 547 */             j = Math.max(localComponent2.getHeight(), j);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 552 */       paramInt4 = Math.max(i + m, j);
/* 553 */       k = (paramInt4 - i - m) / 2;
/*     */     }
/* 555 */     for (int m = paramInt5; m < paramInt6; m++) {
/* 556 */       Component localComponent1 = paramContainer.getComponent(m);
/* 557 */       if (localComponent1.isVisible())
/*     */       {
/*     */         int i1;
/* 559 */         if ((paramBoolean2) && (paramArrayOfInt1[m] >= 0)) {
/* 560 */           i1 = paramInt2 + k + i - paramArrayOfInt1[m];
/*     */         }
/*     */         else {
/* 563 */           i1 = paramInt2 + (paramInt4 - localComponent1.height) / 2;
/*     */         }
/* 565 */         if (paramBoolean1)
/* 566 */           localComponent1.setLocation(paramInt1, i1);
/*     */         else {
/* 568 */           localComponent1.setLocation(paramContainer.width - paramInt1 - localComponent1.width, i1);
/*     */         }
/* 570 */         paramInt1 += localComponent1.width + this.hgap;
/*     */       }
/*     */     }
/* 573 */     return paramInt4;
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 588 */     synchronized (paramContainer.getTreeLock()) {
/* 589 */       Insets localInsets = paramContainer.getInsets();
/* 590 */       int i = paramContainer.width - (localInsets.left + localInsets.right + this.hgap * 2);
/* 591 */       int j = paramContainer.getComponentCount();
/* 592 */       int k = 0; int m = localInsets.top + this.vgap;
/* 593 */       int n = 0; int i1 = 0;
/*     */ 
/* 595 */       boolean bool1 = paramContainer.getComponentOrientation().isLeftToRight();
/*     */ 
/* 597 */       boolean bool2 = getAlignOnBaseline();
/* 598 */       int[] arrayOfInt1 = null;
/* 599 */       int[] arrayOfInt2 = null;
/*     */ 
/* 601 */       if (bool2) {
/* 602 */         arrayOfInt1 = new int[j];
/* 603 */         arrayOfInt2 = new int[j];
/*     */       }
/*     */ 
/* 606 */       for (int i2 = 0; i2 < j; i2++) {
/* 607 */         Component localComponent = paramContainer.getComponent(i2);
/* 608 */         if (localComponent.isVisible()) {
/* 609 */           Dimension localDimension = localComponent.getPreferredSize();
/* 610 */           localComponent.setSize(localDimension.width, localDimension.height);
/*     */ 
/* 612 */           if (bool2) {
/* 613 */             int i3 = localComponent.getBaseline(localDimension.width, localDimension.height);
/* 614 */             if (i3 >= 0) {
/* 615 */               arrayOfInt1[i2] = i3;
/* 616 */               arrayOfInt2[i2] = (localDimension.height - i3);
/*     */             }
/*     */             else {
/* 619 */               arrayOfInt1[i2] = -1;
/*     */             }
/*     */           }
/* 622 */           if ((k == 0) || (k + localDimension.width <= i)) {
/* 623 */             if (k > 0) {
/* 624 */               k += this.hgap;
/*     */             }
/* 626 */             k += localDimension.width;
/* 627 */             n = Math.max(n, localDimension.height);
/*     */           } else {
/* 629 */             n = moveComponents(paramContainer, localInsets.left + this.hgap, m, i - k, n, i1, i2, bool1, bool2, arrayOfInt1, arrayOfInt2);
/*     */ 
/* 632 */             k = localDimension.width;
/* 633 */             m += this.vgap + n;
/* 634 */             n = localDimension.height;
/* 635 */             i1 = i2;
/*     */           }
/*     */         }
/*     */       }
/* 639 */       moveComponents(paramContainer, localInsets.left + this.hgap, m, i - k, n, i1, j, bool1, bool2, arrayOfInt1, arrayOfInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 669 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 671 */     if (this.serialVersionOnStream < 1)
/*     */     {
/* 673 */       setAlignment(this.align);
/*     */     }
/* 675 */     this.serialVersionOnStream = 1;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 684 */     String str = "";
/* 685 */     switch (this.align) { case 0:
/* 686 */       str = ",align=left"; break;
/*     */     case 1:
/* 687 */       str = ",align=center"; break;
/*     */     case 2:
/* 688 */       str = ",align=right"; break;
/*     */     case 3:
/* 689 */       str = ",align=leading"; break;
/*     */     case 4:
/* 690 */       str = ",align=trailing";
/*     */     }
/* 692 */     return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + str + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.FlowLayout
 * JD-Core Version:    0.6.2
 */
/*     */ package java.awt;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class BorderLayout
/*     */   implements LayoutManager2, Serializable
/*     */ {
/*     */   int hgap;
/*     */   int vgap;
/*     */   Component north;
/*     */   Component west;
/*     */   Component east;
/*     */   Component south;
/*     */   Component center;
/*     */   Component firstLine;
/*     */   Component lastLine;
/*     */   Component firstItem;
/*     */   Component lastItem;
/*     */   public static final String NORTH = "North";
/*     */   public static final String SOUTH = "South";
/*     */   public static final String EAST = "East";
/*     */   public static final String WEST = "West";
/*     */   public static final String CENTER = "Center";
/*     */   public static final String BEFORE_FIRST_LINE = "First";
/*     */   public static final String AFTER_LAST_LINE = "Last";
/*     */   public static final String BEFORE_LINE_BEGINS = "Before";
/*     */   public static final String AFTER_LINE_ENDS = "After";
/*     */   public static final String PAGE_START = "First";
/*     */   public static final String PAGE_END = "Last";
/*     */   public static final String LINE_START = "Before";
/*     */   public static final String LINE_END = "After";
/*     */   private static final long serialVersionUID = -8658291919501921765L;
/*     */ 
/*     */   public BorderLayout()
/*     */   {
/* 352 */     this(0, 0);
/*     */   }
/*     */ 
/*     */   public BorderLayout(int paramInt1, int paramInt2)
/*     */   {
/* 364 */     this.hgap = paramInt1;
/* 365 */     this.vgap = paramInt2;
/*     */   }
/*     */ 
/*     */   public int getHgap()
/*     */   {
/* 373 */     return this.hgap;
/*     */   }
/*     */ 
/*     */   public void setHgap(int paramInt)
/*     */   {
/* 382 */     this.hgap = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVgap()
/*     */   {
/* 390 */     return this.vgap;
/*     */   }
/*     */ 
/*     */   public void setVgap(int paramInt)
/*     */   {
/* 399 */     this.vgap = paramInt;
/*     */   }
/*     */ 
/*     */   public void addLayoutComponent(Component paramComponent, Object paramObject)
/*     */   {
/* 422 */     synchronized (paramComponent.getTreeLock()) {
/* 423 */       if ((paramObject == null) || ((paramObject instanceof String)))
/* 424 */         addLayoutComponent((String)paramObject, paramComponent);
/*     */       else
/* 426 */         throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void addLayoutComponent(String paramString, Component paramComponent)
/*     */   {
/* 436 */     synchronized (paramComponent.getTreeLock())
/*     */     {
/* 438 */       if (paramString == null) {
/* 439 */         paramString = "Center";
/*     */       }
/*     */ 
/* 444 */       if ("Center".equals(paramString))
/* 445 */         this.center = paramComponent;
/* 446 */       else if ("North".equals(paramString))
/* 447 */         this.north = paramComponent;
/* 448 */       else if ("South".equals(paramString))
/* 449 */         this.south = paramComponent;
/* 450 */       else if ("East".equals(paramString))
/* 451 */         this.east = paramComponent;
/* 452 */       else if ("West".equals(paramString))
/* 453 */         this.west = paramComponent;
/* 454 */       else if ("First".equals(paramString))
/* 455 */         this.firstLine = paramComponent;
/* 456 */       else if ("Last".equals(paramString))
/* 457 */         this.lastLine = paramComponent;
/* 458 */       else if ("Before".equals(paramString))
/* 459 */         this.firstItem = paramComponent;
/* 460 */       else if ("After".equals(paramString))
/* 461 */         this.lastItem = paramComponent;
/*     */       else
/* 463 */         throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeLayoutComponent(Component paramComponent)
/*     */   {
/* 478 */     synchronized (paramComponent.getTreeLock()) {
/* 479 */       if (paramComponent == this.center)
/* 480 */         this.center = null;
/* 481 */       else if (paramComponent == this.north)
/* 482 */         this.north = null;
/* 483 */       else if (paramComponent == this.south)
/* 484 */         this.south = null;
/* 485 */       else if (paramComponent == this.east)
/* 486 */         this.east = null;
/* 487 */       else if (paramComponent == this.west) {
/* 488 */         this.west = null;
/*     */       }
/* 490 */       if (paramComponent == this.firstLine)
/* 491 */         this.firstLine = null;
/* 492 */       else if (paramComponent == this.lastLine)
/* 493 */         this.lastLine = null;
/* 494 */       else if (paramComponent == this.firstItem)
/* 495 */         this.firstItem = null;
/* 496 */       else if (paramComponent == this.lastItem)
/* 497 */         this.lastItem = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Component getLayoutComponent(Object paramObject)
/*     */   {
/* 518 */     if ("Center".equals(paramObject))
/* 519 */       return this.center;
/* 520 */     if ("North".equals(paramObject))
/* 521 */       return this.north;
/* 522 */     if ("South".equals(paramObject))
/* 523 */       return this.south;
/* 524 */     if ("West".equals(paramObject))
/* 525 */       return this.west;
/* 526 */     if ("East".equals(paramObject))
/* 527 */       return this.east;
/* 528 */     if ("First".equals(paramObject))
/* 529 */       return this.firstLine;
/* 530 */     if ("Last".equals(paramObject))
/* 531 */       return this.lastLine;
/* 532 */     if ("Before".equals(paramObject))
/* 533 */       return this.firstItem;
/* 534 */     if ("After".equals(paramObject)) {
/* 535 */       return this.lastItem;
/*     */     }
/* 537 */     throw new IllegalArgumentException("cannot get component: unknown constraint: " + paramObject);
/*     */   }
/*     */ 
/*     */   public Component getLayoutComponent(Container paramContainer, Object paramObject)
/*     */   {
/* 567 */     boolean bool = paramContainer.getComponentOrientation().isLeftToRight();
/* 568 */     Component localComponent = null;
/*     */ 
/* 570 */     if ("North".equals(paramObject)) {
/* 571 */       localComponent = this.firstLine != null ? this.firstLine : this.north;
/* 572 */     } else if ("South".equals(paramObject)) {
/* 573 */       localComponent = this.lastLine != null ? this.lastLine : this.south;
/* 574 */     } else if ("West".equals(paramObject)) {
/* 575 */       localComponent = bool ? this.firstItem : this.lastItem;
/* 576 */       if (localComponent == null)
/* 577 */         localComponent = this.west;
/*     */     }
/* 579 */     else if ("East".equals(paramObject)) {
/* 580 */       localComponent = bool ? this.lastItem : this.firstItem;
/* 581 */       if (localComponent == null)
/* 582 */         localComponent = this.east;
/*     */     }
/* 584 */     else if ("Center".equals(paramObject)) {
/* 585 */       localComponent = this.center;
/*     */     } else {
/* 587 */       throw new IllegalArgumentException("cannot get component: invalid constraint: " + paramObject);
/*     */     }
/*     */ 
/* 590 */     return localComponent;
/*     */   }
/*     */ 
/*     */   public Object getConstraints(Component paramComponent)
/*     */   {
/* 606 */     if (paramComponent == null) {
/* 607 */       return null;
/*     */     }
/* 609 */     if (paramComponent == this.center)
/* 610 */       return "Center";
/* 611 */     if (paramComponent == this.north)
/* 612 */       return "North";
/* 613 */     if (paramComponent == this.south)
/* 614 */       return "South";
/* 615 */     if (paramComponent == this.west)
/* 616 */       return "West";
/* 617 */     if (paramComponent == this.east)
/* 618 */       return "East";
/* 619 */     if (paramComponent == this.firstLine)
/* 620 */       return "First";
/* 621 */     if (paramComponent == this.lastLine)
/* 622 */       return "Last";
/* 623 */     if (paramComponent == this.firstItem)
/* 624 */       return "Before";
/* 625 */     if (paramComponent == this.lastItem) {
/* 626 */       return "After";
/*     */     }
/* 628 */     return null;
/*     */   }
/*     */ 
/*     */   public Dimension minimumLayoutSize(Container paramContainer)
/*     */   {
/* 646 */     synchronized (paramContainer.getTreeLock()) {
/* 647 */       Dimension localDimension = new Dimension(0, 0);
/*     */ 
/* 649 */       boolean bool = paramContainer.getComponentOrientation().isLeftToRight();
/* 650 */       Component localComponent = null;
/*     */ 
/* 652 */       if ((localComponent = getChild("East", bool)) != null) {
/* 653 */         localObject1 = localComponent.getMinimumSize();
/* 654 */         localDimension.width += ((Dimension)localObject1).width + this.hgap;
/* 655 */         localDimension.height = Math.max(((Dimension)localObject1).height, localDimension.height);
/*     */       }
/* 657 */       if ((localComponent = getChild("West", bool)) != null) {
/* 658 */         localObject1 = localComponent.getMinimumSize();
/* 659 */         localDimension.width += ((Dimension)localObject1).width + this.hgap;
/* 660 */         localDimension.height = Math.max(((Dimension)localObject1).height, localDimension.height);
/*     */       }
/* 662 */       if ((localComponent = getChild("Center", bool)) != null) {
/* 663 */         localObject1 = localComponent.getMinimumSize();
/* 664 */         localDimension.width += ((Dimension)localObject1).width;
/* 665 */         localDimension.height = Math.max(((Dimension)localObject1).height, localDimension.height);
/*     */       }
/* 667 */       if ((localComponent = getChild("North", bool)) != null) {
/* 668 */         localObject1 = localComponent.getMinimumSize();
/* 669 */         localDimension.width = Math.max(((Dimension)localObject1).width, localDimension.width);
/* 670 */         localDimension.height += ((Dimension)localObject1).height + this.vgap;
/*     */       }
/* 672 */       if ((localComponent = getChild("South", bool)) != null) {
/* 673 */         localObject1 = localComponent.getMinimumSize();
/* 674 */         localDimension.width = Math.max(((Dimension)localObject1).width, localDimension.width);
/* 675 */         localDimension.height += ((Dimension)localObject1).height + this.vgap;
/*     */       }
/*     */ 
/* 678 */       Object localObject1 = paramContainer.getInsets();
/* 679 */       localDimension.width += ((Insets)localObject1).left + ((Insets)localObject1).right;
/* 680 */       localDimension.height += ((Insets)localObject1).top + ((Insets)localObject1).bottom;
/*     */ 
/* 682 */       return localDimension;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension preferredLayoutSize(Container paramContainer)
/*     */   {
/* 702 */     synchronized (paramContainer.getTreeLock()) {
/* 703 */       Dimension localDimension = new Dimension(0, 0);
/*     */ 
/* 705 */       boolean bool = paramContainer.getComponentOrientation().isLeftToRight();
/* 706 */       Component localComponent = null;
/*     */ 
/* 708 */       if ((localComponent = getChild("East", bool)) != null) {
/* 709 */         localObject1 = localComponent.getPreferredSize();
/* 710 */         localDimension.width += ((Dimension)localObject1).width + this.hgap;
/* 711 */         localDimension.height = Math.max(((Dimension)localObject1).height, localDimension.height);
/*     */       }
/* 713 */       if ((localComponent = getChild("West", bool)) != null) {
/* 714 */         localObject1 = localComponent.getPreferredSize();
/* 715 */         localDimension.width += ((Dimension)localObject1).width + this.hgap;
/* 716 */         localDimension.height = Math.max(((Dimension)localObject1).height, localDimension.height);
/*     */       }
/* 718 */       if ((localComponent = getChild("Center", bool)) != null) {
/* 719 */         localObject1 = localComponent.getPreferredSize();
/* 720 */         localDimension.width += ((Dimension)localObject1).width;
/* 721 */         localDimension.height = Math.max(((Dimension)localObject1).height, localDimension.height);
/*     */       }
/* 723 */       if ((localComponent = getChild("North", bool)) != null) {
/* 724 */         localObject1 = localComponent.getPreferredSize();
/* 725 */         localDimension.width = Math.max(((Dimension)localObject1).width, localDimension.width);
/* 726 */         localDimension.height += ((Dimension)localObject1).height + this.vgap;
/*     */       }
/* 728 */       if ((localComponent = getChild("South", bool)) != null) {
/* 729 */         localObject1 = localComponent.getPreferredSize();
/* 730 */         localDimension.width = Math.max(((Dimension)localObject1).width, localDimension.width);
/* 731 */         localDimension.height += ((Dimension)localObject1).height + this.vgap;
/*     */       }
/*     */ 
/* 734 */       Object localObject1 = paramContainer.getInsets();
/* 735 */       localDimension.width += ((Insets)localObject1).left + ((Insets)localObject1).right;
/* 736 */       localDimension.height += ((Insets)localObject1).top + ((Insets)localObject1).bottom;
/*     */ 
/* 738 */       return localDimension;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension maximumLayoutSize(Container paramContainer)
/*     */   {
/* 751 */     return new Dimension(2147483647, 2147483647);
/*     */   }
/*     */ 
/*     */   public float getLayoutAlignmentX(Container paramContainer)
/*     */   {
/* 762 */     return 0.5F;
/*     */   }
/*     */ 
/*     */   public float getLayoutAlignmentY(Container paramContainer)
/*     */   {
/* 773 */     return 0.5F;
/*     */   }
/*     */ 
/*     */   public void invalidateLayout(Container paramContainer)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void layoutContainer(Container paramContainer)
/*     */   {
/* 803 */     synchronized (paramContainer.getTreeLock()) {
/* 804 */       Insets localInsets = paramContainer.getInsets();
/* 805 */       int i = localInsets.top;
/* 806 */       int j = paramContainer.height - localInsets.bottom;
/* 807 */       int k = localInsets.left;
/* 808 */       int m = paramContainer.width - localInsets.right;
/*     */ 
/* 810 */       boolean bool = paramContainer.getComponentOrientation().isLeftToRight();
/* 811 */       Component localComponent = null;
/*     */       Dimension localDimension;
/* 813 */       if ((localComponent = getChild("North", bool)) != null) {
/* 814 */         localComponent.setSize(m - k, localComponent.height);
/* 815 */         localDimension = localComponent.getPreferredSize();
/* 816 */         localComponent.setBounds(k, i, m - k, localDimension.height);
/* 817 */         i += localDimension.height + this.vgap;
/*     */       }
/* 819 */       if ((localComponent = getChild("South", bool)) != null) {
/* 820 */         localComponent.setSize(m - k, localComponent.height);
/* 821 */         localDimension = localComponent.getPreferredSize();
/* 822 */         localComponent.setBounds(k, j - localDimension.height, m - k, localDimension.height);
/* 823 */         j -= localDimension.height + this.vgap;
/*     */       }
/* 825 */       if ((localComponent = getChild("East", bool)) != null) {
/* 826 */         localComponent.setSize(localComponent.width, j - i);
/* 827 */         localDimension = localComponent.getPreferredSize();
/* 828 */         localComponent.setBounds(m - localDimension.width, i, localDimension.width, j - i);
/* 829 */         m -= localDimension.width + this.hgap;
/*     */       }
/* 831 */       if ((localComponent = getChild("West", bool)) != null) {
/* 832 */         localComponent.setSize(localComponent.width, j - i);
/* 833 */         localDimension = localComponent.getPreferredSize();
/* 834 */         localComponent.setBounds(k, i, localDimension.width, j - i);
/* 835 */         k += localDimension.width + this.hgap;
/*     */       }
/* 837 */       if ((localComponent = getChild("Center", bool)) != null)
/* 838 */         localComponent.setBounds(k, i, m - k, j - i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Component getChild(String paramString, boolean paramBoolean)
/*     */   {
/* 851 */     Component localComponent = null;
/*     */ 
/* 853 */     if (paramString == "North") {
/* 854 */       localComponent = this.firstLine != null ? this.firstLine : this.north;
/*     */     }
/* 856 */     else if (paramString == "South") {
/* 857 */       localComponent = this.lastLine != null ? this.lastLine : this.south;
/*     */     }
/* 859 */     else if (paramString == "West") {
/* 860 */       localComponent = paramBoolean ? this.firstItem : this.lastItem;
/* 861 */       if (localComponent == null) {
/* 862 */         localComponent = this.west;
/*     */       }
/*     */     }
/* 865 */     else if (paramString == "East") {
/* 866 */       localComponent = paramBoolean ? this.lastItem : this.firstItem;
/* 867 */       if (localComponent == null) {
/* 868 */         localComponent = this.east;
/*     */       }
/*     */     }
/* 871 */     else if (paramString == "Center") {
/* 872 */       localComponent = this.center;
/*     */     }
/* 874 */     if ((localComponent != null) && (!localComponent.visible)) {
/* 875 */       localComponent = null;
/*     */     }
/* 877 */     return localComponent;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 885 */     return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.BorderLayout
 * JD-Core Version:    0.6.2
 */
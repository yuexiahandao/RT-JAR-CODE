/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import javax.swing.SizeRequirements;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.ElementChange;
/*      */ 
/*      */ public class BoxView extends CompositeView
/*      */ {
/*      */   int majorAxis;
/*      */   int majorSpan;
/*      */   int minorSpan;
/*      */   boolean majorReqValid;
/*      */   boolean minorReqValid;
/*      */   SizeRequirements majorRequest;
/*      */   SizeRequirements minorRequest;
/*      */   boolean majorAllocValid;
/*      */   int[] majorOffsets;
/*      */   int[] majorSpans;
/*      */   boolean minorAllocValid;
/*      */   int[] minorOffsets;
/*      */   int[] minorSpans;
/*      */   Rectangle tempRect;
/*      */ 
/*      */   public BoxView(Element paramElement, int paramInt)
/*      */   {
/*   70 */     super(paramElement);
/*   71 */     this.tempRect = new Rectangle();
/*   72 */     this.majorAxis = paramInt;
/*      */ 
/*   74 */     this.majorOffsets = new int[0];
/*   75 */     this.majorSpans = new int[0];
/*   76 */     this.majorReqValid = false;
/*   77 */     this.majorAllocValid = false;
/*   78 */     this.minorOffsets = new int[0];
/*   79 */     this.minorSpans = new int[0];
/*   80 */     this.minorReqValid = false;
/*   81 */     this.minorAllocValid = false;
/*      */   }
/*      */ 
/*      */   public int getAxis()
/*      */   {
/*   94 */     return this.majorAxis;
/*      */   }
/*      */ 
/*      */   public void setAxis(int paramInt)
/*      */   {
/*  106 */     int i = paramInt != this.majorAxis ? 1 : 0;
/*  107 */     this.majorAxis = paramInt;
/*  108 */     if (i != 0)
/*  109 */       preferenceChanged(null, true, true);
/*      */   }
/*      */ 
/*      */   public void layoutChanged(int paramInt)
/*      */   {
/*  128 */     if (paramInt == this.majorAxis)
/*  129 */       this.majorAllocValid = false;
/*      */     else
/*  131 */       this.minorAllocValid = false;
/*      */   }
/*      */ 
/*      */   protected boolean isLayoutValid(int paramInt)
/*      */   {
/*  143 */     if (paramInt == this.majorAxis) {
/*  144 */       return this.majorAllocValid;
/*      */     }
/*  146 */     return this.minorAllocValid;
/*      */   }
/*      */ 
/*      */   protected void paintChild(Graphics paramGraphics, Rectangle paramRectangle, int paramInt)
/*      */   {
/*  160 */     View localView = getView(paramInt);
/*  161 */     localView.paint(paramGraphics, paramRectangle);
/*      */   }
/*      */ 
/*      */   public void replace(int paramInt1, int paramInt2, View[] paramArrayOfView)
/*      */   {
/*  181 */     super.replace(paramInt1, paramInt2, paramArrayOfView);
/*      */ 
/*  184 */     int i = paramArrayOfView != null ? paramArrayOfView.length : 0;
/*  185 */     this.majorOffsets = updateLayoutArray(this.majorOffsets, paramInt1, i);
/*  186 */     this.majorSpans = updateLayoutArray(this.majorSpans, paramInt1, i);
/*  187 */     this.majorReqValid = false;
/*  188 */     this.majorAllocValid = false;
/*  189 */     this.minorOffsets = updateLayoutArray(this.minorOffsets, paramInt1, i);
/*  190 */     this.minorSpans = updateLayoutArray(this.minorSpans, paramInt1, i);
/*  191 */     this.minorReqValid = false;
/*  192 */     this.minorAllocValid = false;
/*      */   }
/*      */ 
/*      */   int[] updateLayoutArray(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  210 */     int i = getViewCount();
/*  211 */     int[] arrayOfInt = new int[i];
/*      */ 
/*  213 */     System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramInt1);
/*  214 */     System.arraycopy(paramArrayOfInt, paramInt1, arrayOfInt, paramInt1 + paramInt2, i - paramInt2 - paramInt1);
/*      */ 
/*  216 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   protected void forwardUpdate(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  239 */     boolean bool = isLayoutValid(this.majorAxis);
/*  240 */     super.forwardUpdate(paramElementChange, paramDocumentEvent, paramShape, paramViewFactory);
/*      */ 
/*  243 */     if ((bool) && (!isLayoutValid(this.majorAxis)))
/*      */     {
/*  247 */       Container localContainer = getContainer();
/*  248 */       if ((paramShape != null) && (localContainer != null)) {
/*  249 */         int i = paramDocumentEvent.getOffset();
/*  250 */         int j = getViewIndexAtPosition(i);
/*  251 */         Rectangle localRectangle = getInsideAllocation(paramShape);
/*  252 */         if (this.majorAxis == 0) {
/*  253 */           localRectangle.x += this.majorOffsets[j];
/*  254 */           localRectangle.width -= this.majorOffsets[j];
/*      */         } else {
/*  256 */           localRectangle.y += this.minorOffsets[j];
/*  257 */           localRectangle.height -= this.minorOffsets[j];
/*      */         }
/*  259 */         localContainer.repaint(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void preferenceChanged(View paramView, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  276 */     boolean bool1 = this.majorAxis == 0 ? paramBoolean1 : paramBoolean2;
/*  277 */     boolean bool2 = this.majorAxis == 0 ? paramBoolean2 : paramBoolean1;
/*  278 */     if (bool1) {
/*  279 */       this.majorReqValid = false;
/*  280 */       this.majorAllocValid = false;
/*      */     }
/*  282 */     if (bool2) {
/*  283 */       this.minorReqValid = false;
/*  284 */       this.minorAllocValid = false;
/*      */     }
/*  286 */     super.preferenceChanged(paramView, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public int getResizeWeight(int paramInt)
/*      */   {
/*  298 */     checkRequests(paramInt);
/*  299 */     if (paramInt == this.majorAxis) {
/*  300 */       if ((this.majorRequest.preferred != this.majorRequest.minimum) || (this.majorRequest.preferred != this.majorRequest.maximum))
/*      */       {
/*  302 */         return 1;
/*      */       }
/*      */     }
/*  305 */     else if ((this.minorRequest.preferred != this.minorRequest.minimum) || (this.minorRequest.preferred != this.minorRequest.maximum))
/*      */     {
/*  307 */       return 1;
/*      */     }
/*      */ 
/*  310 */     return 0;
/*      */   }
/*      */ 
/*      */   void setSpanOnAxis(int paramInt, float paramFloat)
/*      */   {
/*  322 */     if (paramInt == this.majorAxis) {
/*  323 */       if (this.majorSpan != (int)paramFloat) {
/*  324 */         this.majorAllocValid = false;
/*      */       }
/*  326 */       if (!this.majorAllocValid)
/*      */       {
/*  328 */         this.majorSpan = ((int)paramFloat);
/*  329 */         checkRequests(this.majorAxis);
/*  330 */         layoutMajorAxis(this.majorSpan, paramInt, this.majorOffsets, this.majorSpans);
/*  331 */         this.majorAllocValid = true;
/*      */ 
/*  334 */         updateChildSizes();
/*      */       }
/*      */     } else {
/*  337 */       if ((int)paramFloat != this.minorSpan) {
/*  338 */         this.minorAllocValid = false;
/*      */       }
/*  340 */       if (!this.minorAllocValid)
/*      */       {
/*  342 */         this.minorSpan = ((int)paramFloat);
/*  343 */         checkRequests(paramInt);
/*  344 */         layoutMinorAxis(this.minorSpan, paramInt, this.minorOffsets, this.minorSpans);
/*  345 */         this.minorAllocValid = true;
/*      */ 
/*  348 */         updateChildSizes();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void updateChildSizes()
/*      */   {
/*  357 */     int i = getViewCount();
/*      */     int j;
/*      */     View localView;
/*  358 */     if (this.majorAxis == 0)
/*  359 */       for (j = 0; j < i; j++) {
/*  360 */         localView = getView(j);
/*  361 */         localView.setSize(this.majorSpans[j], this.minorSpans[j]);
/*      */       }
/*      */     else
/*  364 */       for (j = 0; j < i; j++) {
/*  365 */         localView = getView(j);
/*  366 */         localView.setSize(this.minorSpans[j], this.majorSpans[j]);
/*      */       }
/*      */   }
/*      */ 
/*      */   float getSpanOnAxis(int paramInt)
/*      */   {
/*  380 */     if (paramInt == this.majorAxis) {
/*  381 */       return this.majorSpan;
/*      */     }
/*  383 */     return this.minorSpan;
/*      */   }
/*      */ 
/*      */   public void setSize(float paramFloat1, float paramFloat2)
/*      */   {
/*  397 */     layout(Math.max(0, (int)(paramFloat1 - getLeftInset() - getRightInset())), Math.max(0, (int)(paramFloat2 - getTopInset() - getBottomInset())));
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, Shape paramShape)
/*      */   {
/*  413 */     Rectangle localRectangle1 = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*      */ 
/*  415 */     int i = getViewCount();
/*  416 */     int j = localRectangle1.x + getLeftInset();
/*  417 */     int k = localRectangle1.y + getTopInset();
/*  418 */     Rectangle localRectangle2 = paramGraphics.getClipBounds();
/*  419 */     for (int m = 0; m < i; m++) {
/*  420 */       this.tempRect.x = (j + getOffset(0, m));
/*  421 */       this.tempRect.y = (k + getOffset(1, m));
/*  422 */       this.tempRect.width = getSpan(0, m);
/*  423 */       this.tempRect.height = getSpan(1, m);
/*  424 */       int n = this.tempRect.x; int i1 = n + this.tempRect.width;
/*  425 */       int i2 = this.tempRect.y; int i3 = i2 + this.tempRect.height;
/*  426 */       int i4 = localRectangle2.x; int i5 = i4 + localRectangle2.width;
/*  427 */       int i6 = localRectangle2.y; int i7 = i6 + localRectangle2.height;
/*      */ 
/*  432 */       if ((i1 >= i4) && (i3 >= i6) && (i5 >= n) && (i7 >= i2))
/*  433 */         paintChild(paramGraphics, this.tempRect, m);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Shape getChildAllocation(int paramInt, Shape paramShape)
/*      */   {
/*  452 */     if (paramShape != null) {
/*  453 */       Shape localShape = super.getChildAllocation(paramInt, paramShape);
/*  454 */       if ((localShape != null) && (!isAllocationValid()))
/*      */       {
/*  456 */         Rectangle localRectangle = (localShape instanceof Rectangle) ? (Rectangle)localShape : localShape.getBounds();
/*      */ 
/*  458 */         if ((localRectangle.width == 0) && (localRectangle.height == 0)) {
/*  459 */           return null;
/*      */         }
/*      */       }
/*  462 */       return localShape;
/*      */     }
/*  464 */     return null;
/*      */   }
/*      */ 
/*      */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*      */     throws BadLocationException
/*      */   {
/*  480 */     if (!isAllocationValid()) {
/*  481 */       Rectangle localRectangle = paramShape.getBounds();
/*  482 */       setSize(localRectangle.width, localRectangle.height);
/*      */     }
/*  484 */     return super.modelToView(paramInt, paramShape, paramBias);
/*      */   }
/*      */ 
/*      */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*      */   {
/*  499 */     if (!isAllocationValid()) {
/*  500 */       Rectangle localRectangle = paramShape.getBounds();
/*  501 */       setSize(localRectangle.width, localRectangle.height);
/*      */     }
/*  503 */     return super.viewToModel(paramFloat1, paramFloat2, paramShape, paramArrayOfBias);
/*      */   }
/*      */ 
/*      */   public float getAlignment(int paramInt)
/*      */   {
/*  524 */     checkRequests(paramInt);
/*  525 */     if (paramInt == this.majorAxis) {
/*  526 */       return this.majorRequest.alignment;
/*      */     }
/*  528 */     return this.minorRequest.alignment;
/*      */   }
/*      */ 
/*      */   public float getPreferredSpan(int paramInt)
/*      */   {
/*  545 */     checkRequests(paramInt);
/*  546 */     float f = paramInt == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
/*      */ 
/*  548 */     if (paramInt == this.majorAxis) {
/*  549 */       return this.majorRequest.preferred + f;
/*      */     }
/*  551 */     return this.minorRequest.preferred + f;
/*      */   }
/*      */ 
/*      */   public float getMinimumSpan(int paramInt)
/*      */   {
/*  568 */     checkRequests(paramInt);
/*  569 */     float f = paramInt == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
/*      */ 
/*  571 */     if (paramInt == this.majorAxis) {
/*  572 */       return this.majorRequest.minimum + f;
/*      */     }
/*  574 */     return this.minorRequest.minimum + f;
/*      */   }
/*      */ 
/*      */   public float getMaximumSpan(int paramInt)
/*      */   {
/*  591 */     checkRequests(paramInt);
/*  592 */     float f = paramInt == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
/*      */ 
/*  594 */     if (paramInt == this.majorAxis) {
/*  595 */       return this.majorRequest.maximum + f;
/*      */     }
/*  597 */     return this.minorRequest.maximum + f;
/*      */   }
/*      */ 
/*      */   protected boolean isAllocationValid()
/*      */   {
/*  610 */     return (this.majorAllocValid) && (this.minorAllocValid);
/*      */   }
/*      */ 
/*      */   protected boolean isBefore(int paramInt1, int paramInt2, Rectangle paramRectangle)
/*      */   {
/*  623 */     if (this.majorAxis == 0) {
/*  624 */       return paramInt1 < paramRectangle.x;
/*      */     }
/*  626 */     return paramInt2 < paramRectangle.y;
/*      */   }
/*      */ 
/*      */   protected boolean isAfter(int paramInt1, int paramInt2, Rectangle paramRectangle)
/*      */   {
/*  640 */     if (this.majorAxis == 0) {
/*  641 */       return paramInt1 > paramRectangle.width + paramRectangle.x;
/*      */     }
/*  643 */     return paramInt2 > paramRectangle.height + paramRectangle.y;
/*      */   }
/*      */ 
/*      */   protected View getViewAtPoint(int paramInt1, int paramInt2, Rectangle paramRectangle)
/*      */   {
/*  657 */     int i = getViewCount();
/*  658 */     if (this.majorAxis == 0) {
/*  659 */       if (paramInt1 < paramRectangle.x + this.majorOffsets[0]) {
/*  660 */         childAllocation(0, paramRectangle);
/*  661 */         return getView(0);
/*      */       }
/*  663 */       for (j = 0; j < i; j++) {
/*  664 */         if (paramInt1 < paramRectangle.x + this.majorOffsets[j]) {
/*  665 */           childAllocation(j - 1, paramRectangle);
/*  666 */           return getView(j - 1);
/*      */         }
/*      */       }
/*  669 */       childAllocation(i - 1, paramRectangle);
/*  670 */       return getView(i - 1);
/*      */     }
/*  672 */     if (paramInt2 < paramRectangle.y + this.majorOffsets[0]) {
/*  673 */       childAllocation(0, paramRectangle);
/*  674 */       return getView(0);
/*      */     }
/*  676 */     for (int j = 0; j < i; j++) {
/*  677 */       if (paramInt2 < paramRectangle.y + this.majorOffsets[j]) {
/*  678 */         childAllocation(j - 1, paramRectangle);
/*  679 */         return getView(j - 1);
/*      */       }
/*      */     }
/*  682 */     childAllocation(i - 1, paramRectangle);
/*  683 */     return getView(i - 1);
/*      */   }
/*      */ 
/*      */   protected void childAllocation(int paramInt, Rectangle paramRectangle)
/*      */   {
/*  695 */     paramRectangle.x += getOffset(0, paramInt);
/*  696 */     paramRectangle.y += getOffset(1, paramInt);
/*  697 */     paramRectangle.width = getSpan(0, paramInt);
/*  698 */     paramRectangle.height = getSpan(1, paramInt);
/*      */   }
/*      */ 
/*      */   protected void layout(int paramInt1, int paramInt2)
/*      */   {
/*  708 */     setSpanOnAxis(0, paramInt1);
/*  709 */     setSpanOnAxis(1, paramInt2);
/*      */   }
/*      */ 
/*      */   public int getWidth()
/*      */   {
/*      */     int i;
/*  719 */     if (this.majorAxis == 0)
/*  720 */       i = this.majorSpan;
/*      */     else {
/*  722 */       i = this.minorSpan;
/*      */     }
/*  724 */     i += getLeftInset() - getRightInset();
/*  725 */     return i;
/*      */   }
/*      */ 
/*      */   public int getHeight()
/*      */   {
/*      */     int i;
/*  735 */     if (this.majorAxis == 1)
/*  736 */       i = this.majorSpan;
/*      */     else {
/*  738 */       i = this.minorSpan;
/*      */     }
/*  740 */     i += getTopInset() - getBottomInset();
/*  741 */     return i;
/*      */   }
/*      */ 
/*      */   protected void layoutMajorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  765 */     long l1 = 0L;
/*  766 */     int i = getViewCount();
/*  767 */     for (int j = 0; j < i; j++) {
/*  768 */       View localView1 = getView(j);
/*  769 */       paramArrayOfInt2[j] = ((int)localView1.getPreferredSpan(paramInt2));
/*  770 */       l1 += paramArrayOfInt2[j];
/*      */     }
/*      */ 
/*  779 */     long l2 = paramInt1 - l1;
/*  780 */     float f1 = 0.0F;
/*  781 */     int[] arrayOfInt = null;
/*      */     float f2;
/*  783 */     if (l2 != 0L) {
/*  784 */       long l3 = 0L;
/*  785 */       arrayOfInt = new int[i];
/*  786 */       for (int n = 0; n < i; n++) {
/*  787 */         View localView2 = getView(n);
/*      */         int i1;
/*  789 */         if (l2 < 0L) {
/*  790 */           i1 = (int)localView2.getMinimumSpan(paramInt2);
/*  791 */           paramArrayOfInt2[n] -= i1;
/*      */         } else {
/*  793 */           i1 = (int)localView2.getMaximumSpan(paramInt2);
/*  794 */           arrayOfInt[n] = (i1 - paramArrayOfInt2[n]);
/*      */         }
/*  796 */         l3 += i1;
/*      */       }
/*      */ 
/*  799 */       f2 = (float)Math.abs(l3 - l1);
/*  800 */       f1 = (float)l2 / f2;
/*  801 */       f1 = Math.min(f1, 1.0F);
/*  802 */       f1 = Math.max(f1, -1.0F);
/*      */     }
/*      */ 
/*  806 */     int k = 0;
/*  807 */     for (int m = 0; m < i; m++) {
/*  808 */       paramArrayOfInt1[m] = k;
/*  809 */       if (l2 != 0L) {
/*  810 */         f2 = f1 * arrayOfInt[m];
/*  811 */         paramArrayOfInt2[m] += Math.round(f2);
/*      */       }
/*  813 */       k = (int)Math.min(k + paramArrayOfInt2[m], 2147483647L);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void layoutMinorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  834 */     int i = getViewCount();
/*  835 */     for (int j = 0; j < i; j++) {
/*  836 */       View localView = getView(j);
/*  837 */       int k = (int)localView.getMaximumSpan(paramInt2);
/*  838 */       if (k < paramInt1)
/*      */       {
/*  840 */         float f = localView.getAlignment(paramInt2);
/*  841 */         paramArrayOfInt1[j] = ((int)((paramInt1 - k) * f));
/*  842 */         paramArrayOfInt2[j] = k;
/*      */       }
/*      */       else {
/*  845 */         int m = (int)localView.getMinimumSpan(paramInt2);
/*  846 */         paramArrayOfInt1[j] = 0;
/*  847 */         paramArrayOfInt2[j] = Math.max(m, paramInt1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected SizeRequirements calculateMajorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */   {
/*  864 */     float f1 = 0.0F;
/*  865 */     float f2 = 0.0F;
/*  866 */     float f3 = 0.0F;
/*      */ 
/*  868 */     int i = getViewCount();
/*  869 */     for (int j = 0; j < i; j++) {
/*  870 */       View localView = getView(j);
/*  871 */       f1 += localView.getMinimumSpan(paramInt);
/*  872 */       f2 += localView.getPreferredSpan(paramInt);
/*  873 */       f3 += localView.getMaximumSpan(paramInt);
/*      */     }
/*      */ 
/*  876 */     if (paramSizeRequirements == null) {
/*  877 */       paramSizeRequirements = new SizeRequirements();
/*      */     }
/*  879 */     paramSizeRequirements.alignment = 0.5F;
/*  880 */     paramSizeRequirements.minimum = ((int)f1);
/*  881 */     paramSizeRequirements.preferred = ((int)f2);
/*  882 */     paramSizeRequirements.maximum = ((int)f3);
/*  883 */     return paramSizeRequirements;
/*      */   }
/*      */ 
/*      */   protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */   {
/*  897 */     int i = 0;
/*  898 */     long l = 0L;
/*  899 */     int j = 2147483647;
/*  900 */     int k = getViewCount();
/*  901 */     for (int m = 0; m < k; m++) {
/*  902 */       View localView = getView(m);
/*  903 */       i = Math.max((int)localView.getMinimumSpan(paramInt), i);
/*  904 */       l = Math.max((int)localView.getPreferredSpan(paramInt), l);
/*  905 */       j = Math.max((int)localView.getMaximumSpan(paramInt), j);
/*      */     }
/*      */ 
/*  908 */     if (paramSizeRequirements == null) {
/*  909 */       paramSizeRequirements = new SizeRequirements();
/*  910 */       paramSizeRequirements.alignment = 0.5F;
/*      */     }
/*  912 */     paramSizeRequirements.preferred = ((int)l);
/*  913 */     paramSizeRequirements.minimum = i;
/*  914 */     paramSizeRequirements.maximum = j;
/*  915 */     return paramSizeRequirements;
/*      */   }
/*      */ 
/*      */   void checkRequests(int paramInt)
/*      */   {
/*  925 */     if ((paramInt != 0) && (paramInt != 1)) {
/*  926 */       throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*      */     }
/*  928 */     if (paramInt == this.majorAxis) {
/*  929 */       if (!this.majorReqValid) {
/*  930 */         this.majorRequest = calculateMajorAxisRequirements(paramInt, this.majorRequest);
/*      */ 
/*  932 */         this.majorReqValid = true;
/*      */       }
/*  934 */     } else if (!this.minorReqValid) {
/*  935 */       this.minorRequest = calculateMinorAxisRequirements(paramInt, this.minorRequest);
/*  936 */       this.minorReqValid = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void baselineLayout(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*      */   {
/*  956 */     int i = (int)(paramInt1 * getAlignment(paramInt2));
/*  957 */     int j = paramInt1 - i;
/*      */ 
/*  959 */     int k = getViewCount();
/*      */ 
/*  961 */     for (int m = 0; m < k; m++) {
/*  962 */       View localView = getView(m);
/*  963 */       float f1 = localView.getAlignment(paramInt2);
/*      */       float f2;
/*  966 */       if (localView.getResizeWeight(paramInt2) > 0)
/*      */       {
/*  970 */         float f3 = localView.getMinimumSpan(paramInt2);
/*      */ 
/*  972 */         float f4 = localView.getMaximumSpan(paramInt2);
/*      */ 
/*  974 */         if (f1 == 0.0F)
/*      */         {
/*  976 */           f2 = Math.max(Math.min(f4, j), f3);
/*  977 */         } else if (f1 == 1.0F)
/*      */         {
/*  979 */           f2 = Math.max(Math.min(f4, i), f3);
/*      */         }
/*      */         else {
/*  982 */           float f5 = Math.min(i / f1, j / (1.0F - f1));
/*      */ 
/*  985 */           f2 = Math.max(Math.min(f4, f5), f3);
/*      */         }
/*      */       }
/*      */       else {
/*  989 */         f2 = localView.getPreferredSpan(paramInt2);
/*      */       }
/*      */ 
/*  992 */       paramArrayOfInt1[m] = (i - (int)(f2 * f1));
/*  993 */       paramArrayOfInt2[m] = ((int)f2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected SizeRequirements baselineRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*      */   {
/* 1007 */     SizeRequirements localSizeRequirements1 = new SizeRequirements();
/* 1008 */     SizeRequirements localSizeRequirements2 = new SizeRequirements();
/*      */ 
/* 1010 */     if (paramSizeRequirements == null) {
/* 1011 */       paramSizeRequirements = new SizeRequirements();
/*      */     }
/*      */ 
/* 1014 */     paramSizeRequirements.alignment = 0.5F;
/*      */ 
/* 1016 */     int i = getViewCount();
/*      */ 
/* 1020 */     for (int j = 0; j < i; j++) {
/* 1021 */       View localView = getView(j);
/* 1022 */       float f1 = localView.getAlignment(paramInt);
/*      */ 
/* 1028 */       float f2 = localView.getPreferredSpan(paramInt);
/* 1029 */       int k = (int)(f1 * f2);
/* 1030 */       int m = (int)(f2 - k);
/* 1031 */       localSizeRequirements1.preferred = Math.max(k, localSizeRequirements1.preferred);
/* 1032 */       localSizeRequirements2.preferred = Math.max(m, localSizeRequirements2.preferred);
/*      */ 
/* 1034 */       if (localView.getResizeWeight(paramInt) > 0)
/*      */       {
/* 1037 */         f2 = localView.getMinimumSpan(paramInt);
/* 1038 */         k = (int)(f1 * f2);
/* 1039 */         m = (int)(f2 - k);
/* 1040 */         localSizeRequirements1.minimum = Math.max(k, localSizeRequirements1.minimum);
/* 1041 */         localSizeRequirements2.minimum = Math.max(m, localSizeRequirements2.minimum);
/*      */ 
/* 1043 */         f2 = localView.getMaximumSpan(paramInt);
/* 1044 */         k = (int)(f1 * f2);
/* 1045 */         m = (int)(f2 - k);
/* 1046 */         localSizeRequirements1.maximum = Math.max(k, localSizeRequirements1.maximum);
/* 1047 */         localSizeRequirements2.maximum = Math.max(m, localSizeRequirements2.maximum);
/*      */       }
/*      */       else {
/* 1050 */         localSizeRequirements1.minimum = Math.max(k, localSizeRequirements1.minimum);
/* 1051 */         localSizeRequirements2.minimum = Math.max(m, localSizeRequirements2.minimum);
/* 1052 */         localSizeRequirements1.maximum = Math.max(k, localSizeRequirements1.maximum);
/* 1053 */         localSizeRequirements2.maximum = Math.max(m, localSizeRequirements2.maximum);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1060 */     paramSizeRequirements.preferred = ((int)Math.min(localSizeRequirements1.preferred + localSizeRequirements2.preferred, 2147483647L));
/*      */ 
/* 1064 */     if (paramSizeRequirements.preferred > 0) {
/* 1065 */       paramSizeRequirements.alignment = (localSizeRequirements1.preferred / paramSizeRequirements.preferred);
/*      */     }
/*      */ 
/* 1069 */     if (paramSizeRequirements.alignment == 0.0F)
/*      */     {
/* 1072 */       paramSizeRequirements.minimum = localSizeRequirements2.minimum;
/* 1073 */       paramSizeRequirements.maximum = localSizeRequirements2.maximum;
/* 1074 */     } else if (paramSizeRequirements.alignment == 1.0F)
/*      */     {
/* 1077 */       paramSizeRequirements.minimum = localSizeRequirements1.minimum;
/* 1078 */       paramSizeRequirements.maximum = localSizeRequirements1.maximum;
/*      */     }
/*      */     else
/*      */     {
/* 1083 */       paramSizeRequirements.minimum = Math.round(Math.max(localSizeRequirements1.minimum / paramSizeRequirements.alignment, localSizeRequirements2.minimum / (1.0F - paramSizeRequirements.alignment)));
/*      */ 
/* 1086 */       paramSizeRequirements.maximum = Math.round(Math.min(localSizeRequirements1.maximum / paramSizeRequirements.alignment, localSizeRequirements2.maximum / (1.0F - paramSizeRequirements.alignment)));
/*      */     }
/*      */ 
/* 1090 */     return paramSizeRequirements;
/*      */   }
/*      */ 
/*      */   protected int getOffset(int paramInt1, int paramInt2)
/*      */   {
/* 1100 */     int[] arrayOfInt = paramInt1 == this.majorAxis ? this.majorOffsets : this.minorOffsets;
/* 1101 */     return arrayOfInt[paramInt2];
/*      */   }
/*      */ 
/*      */   protected int getSpan(int paramInt1, int paramInt2)
/*      */   {
/* 1111 */     int[] arrayOfInt = paramInt1 == this.majorAxis ? this.majorSpans : this.minorSpans;
/* 1112 */     return arrayOfInt[paramInt2];
/*      */   }
/*      */ 
/*      */   protected boolean flipEastAndWestAtEnds(int paramInt, Position.Bias paramBias)
/*      */   {
/* 1146 */     if (this.majorAxis == 1) {
/* 1147 */       int i = paramBias == Position.Bias.Backward ? Math.max(0, paramInt - 1) : paramInt;
/*      */ 
/* 1149 */       int j = getViewIndexAtPosition(i);
/* 1150 */       if (j != -1) {
/* 1151 */         View localView = getView(j);
/* 1152 */         if ((localView != null) && ((localView instanceof CompositeView))) {
/* 1153 */           return ((CompositeView)localView).flipEastAndWestAtEnds(paramInt, paramBias);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1158 */     return false;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.BoxView
 * JD-Core Version:    0.6.2
 */
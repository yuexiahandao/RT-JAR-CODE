/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.util.Vector;
/*     */ import javax.swing.SizeRequirements;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentEvent.ElementChange;
/*     */ 
/*     */ public abstract class FlowView extends BoxView
/*     */ {
/*     */   protected int layoutSpan;
/*     */   protected View layoutPool;
/*     */   protected FlowStrategy strategy;
/*     */ 
/*     */   public FlowView(Element paramElement, int paramInt)
/*     */   {
/*  61 */     super(paramElement, paramInt);
/*  62 */     this.layoutSpan = 2147483647;
/*  63 */     this.strategy = new FlowStrategy();
/*     */   }
/*     */ 
/*     */   public int getFlowAxis()
/*     */   {
/*  75 */     if (getAxis() == 1) {
/*  76 */       return 0;
/*     */     }
/*  78 */     return 1;
/*     */   }
/*     */ 
/*     */   public int getFlowSpan(int paramInt)
/*     */   {
/*  95 */     return this.layoutSpan;
/*     */   }
/*     */ 
/*     */   public int getFlowStart(int paramInt)
/*     */   {
/* 110 */     return 0;
/*     */   }
/*     */ 
/*     */   protected abstract View createRow();
/*     */ 
/*     */   protected void loadChildren(ViewFactory paramViewFactory)
/*     */   {
/* 136 */     if (this.layoutPool == null) {
/* 137 */       this.layoutPool = new LogicalView(getElement());
/*     */     }
/* 139 */     this.layoutPool.setParent(this);
/*     */ 
/* 143 */     this.strategy.insertUpdate(this, null, null);
/*     */   }
/*     */ 
/*     */   protected int getViewIndexAtPosition(int paramInt)
/*     */   {
/* 155 */     if ((paramInt >= getStartOffset()) && (paramInt < getEndOffset())) {
/* 156 */       for (int i = 0; i < getViewCount(); i++) {
/* 157 */         View localView = getView(i);
/* 158 */         if ((paramInt >= localView.getStartOffset()) && (paramInt < localView.getEndOffset()))
/*     */         {
/* 160 */           return i;
/*     */         }
/*     */       }
/*     */     }
/* 164 */     return -1;
/*     */   }
/*     */ 
/*     */   protected void layout(int paramInt1, int paramInt2)
/*     */   {
/* 184 */     int i = getFlowAxis();
/*     */     int j;
/* 186 */     if (i == 0)
/* 187 */       j = paramInt1;
/*     */     else {
/* 189 */       j = paramInt2;
/*     */     }
/* 191 */     if (this.layoutSpan != j) {
/* 192 */       layoutChanged(i);
/* 193 */       layoutChanged(getAxis());
/* 194 */       this.layoutSpan = j;
/*     */     }
/*     */ 
/* 198 */     if (!isLayoutValid(i)) {
/* 199 */       int k = getAxis();
/* 200 */       int m = k == 0 ? getWidth() : getHeight();
/* 201 */       this.strategy.layout(this);
/* 202 */       int n = (int)getPreferredSpan(k);
/* 203 */       if (m != n) {
/* 204 */         View localView = getParent();
/* 205 */         if (localView != null) {
/* 206 */           localView.preferenceChanged(this, k == 0, k == 1);
/*     */         }
/*     */ 
/* 212 */         Container localContainer = getContainer();
/* 213 */         if (localContainer != null)
/*     */         {
/* 215 */           localContainer.repaint();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 220 */     super.layout(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*     */   {
/* 230 */     if (paramSizeRequirements == null) {
/* 231 */       paramSizeRequirements = new SizeRequirements();
/*     */     }
/* 233 */     float f1 = this.layoutPool.getPreferredSpan(paramInt);
/* 234 */     float f2 = this.layoutPool.getMinimumSpan(paramInt);
/*     */ 
/* 236 */     paramSizeRequirements.minimum = ((int)f2);
/* 237 */     paramSizeRequirements.preferred = Math.max(paramSizeRequirements.minimum, (int)f1);
/* 238 */     paramSizeRequirements.maximum = 2147483647;
/* 239 */     paramSizeRequirements.alignment = 0.5F;
/* 240 */     return paramSizeRequirements;
/*     */   }
/*     */ 
/*     */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 255 */     this.layoutPool.insertUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/* 256 */     this.strategy.insertUpdate(this, paramDocumentEvent, getInsideAllocation(paramShape));
/*     */   }
/*     */ 
/*     */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 269 */     this.layoutPool.removeUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/* 270 */     this.strategy.removeUpdate(this, paramDocumentEvent, getInsideAllocation(paramShape));
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 283 */     this.layoutPool.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/* 284 */     this.strategy.changedUpdate(this, paramDocumentEvent, getInsideAllocation(paramShape));
/*     */   }
/*     */ 
/*     */   public void setParent(View paramView)
/*     */   {
/* 289 */     super.setParent(paramView);
/* 290 */     if ((paramView == null) && (this.layoutPool != null))
/*     */     {
/* 292 */       this.layoutPool.setParent(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FlowStrategy
/*     */   {
/* 336 */     Position damageStart = null;
/*     */     Vector<View> viewBuffer;
/*     */ 
/*     */     void addDamage(FlowView paramFlowView, int paramInt)
/*     */     {
/* 340 */       if ((paramInt >= paramFlowView.getStartOffset()) && (paramInt < paramFlowView.getEndOffset()) && (
/* 341 */         (this.damageStart == null) || (paramInt < this.damageStart.getOffset())))
/*     */         try {
/* 343 */           this.damageStart = paramFlowView.getDocument().createPosition(paramInt);
/*     */         }
/*     */         catch (BadLocationException localBadLocationException) {
/* 346 */           if (!$assertionsDisabled) throw new AssertionError();
/*     */         }
/*     */     }
/*     */ 
/*     */     void unsetDamage()
/*     */     {
/* 353 */       this.damageStart = null;
/*     */     }
/*     */ 
/*     */     public void insertUpdate(FlowView paramFlowView, DocumentEvent paramDocumentEvent, Rectangle paramRectangle)
/*     */     {
/* 370 */       if (paramDocumentEvent != null) {
/* 371 */         addDamage(paramFlowView, paramDocumentEvent.getOffset());
/*     */       }
/*     */ 
/* 374 */       if (paramRectangle != null) {
/* 375 */         Container localContainer = paramFlowView.getContainer();
/* 376 */         if (localContainer != null)
/* 377 */           localContainer.repaint(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */       }
/*     */       else {
/* 380 */         paramFlowView.preferenceChanged(null, true, true);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void removeUpdate(FlowView paramFlowView, DocumentEvent paramDocumentEvent, Rectangle paramRectangle)
/*     */     {
/* 393 */       addDamage(paramFlowView, paramDocumentEvent.getOffset());
/* 394 */       if (paramRectangle != null) {
/* 395 */         Container localContainer = paramFlowView.getContainer();
/* 396 */         if (localContainer != null)
/* 397 */           localContainer.repaint(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */       }
/*     */       else {
/* 400 */         paramFlowView.preferenceChanged(null, true, true);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void changedUpdate(FlowView paramFlowView, DocumentEvent paramDocumentEvent, Rectangle paramRectangle)
/*     */     {
/* 415 */       addDamage(paramFlowView, paramDocumentEvent.getOffset());
/* 416 */       if (paramRectangle != null) {
/* 417 */         Container localContainer = paramFlowView.getContainer();
/* 418 */         if (localContainer != null)
/* 419 */           localContainer.repaint(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */       }
/*     */       else {
/* 422 */         paramFlowView.preferenceChanged(null, true, true);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected View getLogicalView(FlowView paramFlowView)
/*     */     {
/* 431 */       return paramFlowView.layoutPool;
/*     */     }
/*     */ 
/*     */     public void layout(FlowView paramFlowView)
/*     */     {
/* 443 */       View localView1 = getLogicalView(paramFlowView);
/*     */ 
/* 445 */       int k = paramFlowView.getEndOffset();
/*     */       int i;
/*     */       int j;
/* 447 */       if (paramFlowView.majorAllocValid) {
/* 448 */         if (this.damageStart == null) {
/* 449 */           return;
/*     */         }
/*     */ 
/* 453 */         m = this.damageStart.getOffset();
/* 454 */         while ((i = paramFlowView.getViewIndexAtPosition(m)) < 0) {
/* 455 */           m--;
/*     */         }
/* 457 */         if (i > 0) {
/* 458 */           i--;
/*     */         }
/* 460 */         j = paramFlowView.getView(i).getStartOffset();
/*     */       } else {
/* 462 */         i = 0;
/* 463 */         j = paramFlowView.getStartOffset();
/*     */       }
/* 465 */       reparentViews(localView1, j);
/*     */ 
/* 467 */       this.viewBuffer = new Vector(10, 10);
/* 468 */       int m = paramFlowView.getViewCount();
/* 469 */       while (j < k)
/*     */       {
/*     */         View localView2;
/* 471 */         if (i >= m) {
/* 472 */           localView2 = paramFlowView.createRow();
/* 473 */           paramFlowView.append(localView2);
/*     */         } else {
/* 475 */           localView2 = paramFlowView.getView(i);
/*     */         }
/* 477 */         j = layoutRow(paramFlowView, i, j);
/* 478 */         i++;
/*     */       }
/* 480 */       this.viewBuffer = null;
/*     */ 
/* 482 */       if (i < m) {
/* 483 */         paramFlowView.replace(i, m - i, null);
/*     */       }
/* 485 */       unsetDamage();
/*     */     }
/*     */ 
/*     */     protected int layoutRow(FlowView paramFlowView, int paramInt1, int paramInt2)
/*     */     {
/* 505 */       View localView1 = paramFlowView.getView(paramInt1);
/* 506 */       float f1 = paramFlowView.getFlowStart(paramInt1);
/* 507 */       float f2 = paramFlowView.getFlowSpan(paramInt1);
/* 508 */       int i = paramFlowView.getEndOffset();
/* 509 */       TabExpander localTabExpander = (paramFlowView instanceof TabExpander) ? (TabExpander)paramFlowView : null;
/* 510 */       int j = paramFlowView.getFlowAxis();
/*     */ 
/* 512 */       int k = 0;
/* 513 */       float f3 = 0.0F;
/* 514 */       float f4 = 0.0F;
/* 515 */       int m = -1;
/* 516 */       int n = 0;
/*     */ 
/* 518 */       this.viewBuffer.clear();
/* 519 */       while ((paramInt2 < i) && (f2 >= 0.0F)) {
/* 520 */         localObject = createView(paramFlowView, paramInt2, (int)f2, paramInt1);
/* 521 */         if (localObject == null)
/*     */         {
/*     */           break;
/*     */         }
/* 525 */         int i1 = ((View)localObject).getBreakWeight(j, f1, f2);
/* 526 */         if (i1 >= 3000) {
/* 527 */           View localView2 = ((View)localObject).breakView(j, paramInt2, f1, f2);
/* 528 */           if (localView2 != null) {
/* 529 */             this.viewBuffer.add(localView2); break;
/* 530 */           }if (n != 0) {
/*     */             break;
/*     */           }
/* 533 */           this.viewBuffer.add(localObject); break;
/*     */         }
/*     */ 
/* 536 */         if ((i1 >= k) && (i1 > 0)) {
/* 537 */           k = i1;
/* 538 */           f3 = f1;
/* 539 */           f4 = f2;
/* 540 */           m = n;
/*     */         }
/*     */         float f5;
/* 544 */         if ((j == 0) && ((localObject instanceof TabableView)))
/* 545 */           f5 = ((TabableView)localObject).getTabbedSpan(f1, localTabExpander);
/*     */         else {
/* 547 */           f5 = ((View)localObject).getPreferredSpan(j);
/*     */         }
/*     */ 
/* 550 */         if ((f5 > f2) && (m >= 0))
/*     */         {
/* 552 */           if (m < n) {
/* 553 */             localObject = (View)this.viewBuffer.get(m);
/*     */           }
/* 555 */           for (int i2 = n - 1; i2 >= m; i2--) {
/* 556 */             this.viewBuffer.remove(i2);
/*     */           }
/* 558 */           localObject = ((View)localObject).breakView(j, ((View)localObject).getStartOffset(), f3, f4);
/*     */         }
/*     */ 
/* 561 */         f2 -= f5;
/* 562 */         f1 += f5;
/* 563 */         this.viewBuffer.add(localObject);
/* 564 */         paramInt2 = ((View)localObject).getEndOffset();
/* 565 */         n++;
/*     */       }
/*     */ 
/* 568 */       Object localObject = new View[this.viewBuffer.size()];
/* 569 */       this.viewBuffer.toArray((Object[])localObject);
/* 570 */       localView1.replace(0, localView1.getViewCount(), (View[])localObject);
/* 571 */       return localObject.length > 0 ? localView1.getEndOffset() : paramInt2;
/*     */     }
/*     */ 
/*     */     protected void adjustRow(FlowView paramFlowView, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 587 */       int i = paramFlowView.getFlowAxis();
/* 588 */       View localView1 = paramFlowView.getView(paramInt1);
/* 589 */       int j = localView1.getViewCount();
/* 590 */       int k = 0;
/* 591 */       int m = 0;
/* 592 */       int n = 0;
/* 593 */       int i1 = -1;
/*     */ 
/* 595 */       for (int i2 = 0; i2 < j; i2++) {
/* 596 */         localView2 = localView1.getView(i2);
/* 597 */         int i3 = paramInt2 - k;
/*     */ 
/* 599 */         int i4 = localView2.getBreakWeight(i, paramInt3 + k, i3);
/* 600 */         if ((i4 >= m) && (i4 > 0)) {
/* 601 */           m = i4;
/* 602 */           i1 = i2;
/* 603 */           n = k;
/* 604 */           if (i4 >= 3000)
/*     */           {
/*     */             break;
/*     */           }
/*     */         }
/*     */ 
/* 610 */         k = (int)(k + localView2.getPreferredSpan(i));
/*     */       }
/* 612 */       if (i1 < 0)
/*     */       {
/* 615 */         return;
/*     */       }
/*     */ 
/* 619 */       i2 = paramInt2 - n;
/* 620 */       View localView2 = localView1.getView(i1);
/* 621 */       localView2 = localView2.breakView(i, localView2.getStartOffset(), paramInt3 + n, i2);
/* 622 */       View[] arrayOfView = new View[1];
/* 623 */       arrayOfView[0] = localView2;
/* 624 */       View localView3 = getLogicalView(paramFlowView);
/* 625 */       int i5 = localView1.getView(i1).getStartOffset();
/* 626 */       int i6 = localView1.getEndOffset();
/* 627 */       for (int i7 = 0; i7 < localView3.getViewCount(); i7++) {
/* 628 */         View localView4 = localView3.getView(i7);
/* 629 */         if (localView4.getEndOffset() > i6) {
/*     */           break;
/*     */         }
/* 632 */         if (localView4.getStartOffset() >= i5) {
/* 633 */           localView4.setParent(localView3);
/*     */         }
/*     */       }
/* 636 */       localView1.replace(i1, j - i1, arrayOfView);
/*     */     }
/*     */ 
/*     */     void reparentViews(View paramView, int paramInt) {
/* 640 */       int i = paramView.getViewIndex(paramInt, Position.Bias.Forward);
/* 641 */       if (i >= 0)
/* 642 */         for (int j = i; j < paramView.getViewCount(); j++)
/* 643 */           paramView.getView(j).setParent(paramView);
/*     */     }
/*     */ 
/*     */     protected View createView(FlowView paramFlowView, int paramInt1, int paramInt2, int paramInt3)
/*     */     {
/* 660 */       View localView1 = getLogicalView(paramFlowView);
/* 661 */       int i = localView1.getViewIndex(paramInt1, Position.Bias.Forward);
/* 662 */       View localView2 = localView1.getView(i);
/* 663 */       if (paramInt1 == localView2.getStartOffset())
/*     */       {
/* 665 */         return localView2;
/*     */       }
/*     */ 
/* 669 */       localView2 = localView2.createFragment(paramInt1, localView2.getEndOffset());
/* 670 */       return localView2;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class LogicalView extends CompositeView
/*     */   {
/*     */     LogicalView(Element paramElement)
/*     */     {
/* 684 */       super();
/*     */     }
/*     */ 
/*     */     protected int getViewIndexAtPosition(int paramInt) {
/* 688 */       Element localElement = getElement();
/* 689 */       if (localElement.isLeaf()) {
/* 690 */         return 0;
/*     */       }
/* 692 */       return super.getViewIndexAtPosition(paramInt);
/*     */     }
/*     */ 
/*     */     protected void loadChildren(ViewFactory paramViewFactory) {
/* 696 */       Element localElement = getElement();
/* 697 */       if (localElement.isLeaf()) {
/* 698 */         LabelView localLabelView = new LabelView(localElement);
/* 699 */         append(localLabelView);
/*     */       } else {
/* 701 */         super.loadChildren(paramViewFactory);
/*     */       }
/*     */     }
/*     */ 
/*     */     public AttributeSet getAttributes()
/*     */     {
/* 711 */       View localView = getParent();
/* 712 */       return localView != null ? localView.getAttributes() : null;
/*     */     }
/*     */ 
/*     */     public float getPreferredSpan(int paramInt)
/*     */     {
/* 727 */       float f1 = 0.0F;
/* 728 */       float f2 = 0.0F;
/* 729 */       int i = getViewCount();
/* 730 */       for (int j = 0; j < i; j++) {
/* 731 */         View localView = getView(j);
/* 732 */         f2 += localView.getPreferredSpan(paramInt);
/* 733 */         if (localView.getBreakWeight(paramInt, 0.0F, 2.147484E+009F) >= 3000) {
/* 734 */           f1 = Math.max(f1, f2);
/* 735 */           f2 = 0.0F;
/*     */         }
/*     */       }
/* 738 */       f1 = Math.max(f1, f2);
/* 739 */       return f1;
/*     */     }
/*     */ 
/*     */     public float getMinimumSpan(int paramInt)
/*     */     {
/* 755 */       float f1 = 0.0F;
/* 756 */       float f2 = 0.0F;
/* 757 */       int i = 0;
/* 758 */       int j = getViewCount();
/* 759 */       for (int k = 0; k < j; k++) {
/* 760 */         View localView = getView(k);
/* 761 */         if (localView.getBreakWeight(paramInt, 0.0F, 2.147484E+009F) == 0) {
/* 762 */           f2 += localView.getPreferredSpan(paramInt);
/* 763 */           i = 1;
/* 764 */         } else if (i != 0) {
/* 765 */           f1 = Math.max(f2, f1);
/* 766 */           i = 0;
/* 767 */           f2 = 0.0F;
/*     */         }
/* 769 */         if ((localView instanceof ComponentView)) {
/* 770 */           f1 = Math.max(f1, localView.getMinimumSpan(paramInt));
/*     */         }
/*     */       }
/* 773 */       f1 = Math.max(f1, f2);
/* 774 */       return f1;
/*     */     }
/*     */ 
/*     */     protected void forwardUpdateToView(View paramView, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */     {
/* 793 */       View localView = paramView.getParent();
/* 794 */       paramView.setParent(this);
/* 795 */       super.forwardUpdateToView(paramView, paramDocumentEvent, paramShape, paramViewFactory);
/* 796 */       paramView.setParent(localView);
/*     */     }
/*     */ 
/*     */     protected void forwardUpdate(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */     {
/* 803 */       calculateUpdateIndexes(paramDocumentEvent);
/*     */ 
/* 805 */       this.lastUpdateIndex = Math.max(getViewCount() - 1, 0);
/* 806 */       for (int i = this.firstUpdateIndex; i <= this.lastUpdateIndex; i++) {
/* 807 */         View localView = getView(i);
/* 808 */         if (localView != null) {
/* 809 */           Shape localShape = getChildAllocation(i, paramShape);
/* 810 */           forwardUpdateToView(localView, paramDocumentEvent, localShape, paramViewFactory);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics, Shape paramShape)
/*     */     {
/*     */     }
/*     */ 
/*     */     protected boolean isBefore(int paramInt1, int paramInt2, Rectangle paramRectangle)
/*     */     {
/* 841 */       return false;
/*     */     }
/*     */ 
/*     */     protected boolean isAfter(int paramInt1, int paramInt2, Rectangle paramRectangle)
/*     */     {
/* 855 */       return false;
/*     */     }
/*     */ 
/*     */     protected View getViewAtPoint(int paramInt1, int paramInt2, Rectangle paramRectangle)
/*     */     {
/* 870 */       return null;
/*     */     }
/*     */ 
/*     */     protected void childAllocation(int paramInt, Rectangle paramRectangle)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.FlowView
 * JD-Core Version:    0.6.2
 */
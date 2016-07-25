/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ 
/*     */ public abstract class CompositeView extends View
/*     */ {
/* 785 */   private static View[] ZERO = new View[0];
/*     */   private View[] children;
/*     */   private int nchildren;
/*     */   private short left;
/*     */   private short right;
/*     */   private short top;
/*     */   private short bottom;
/*     */   private Rectangle childAlloc;
/*     */ 
/*     */   public CompositeView(Element paramElement)
/*     */   {
/*  84 */     super(paramElement);
/*  85 */     this.children = new View[1];
/*  86 */     this.nchildren = 0;
/*  87 */     this.childAlloc = new Rectangle();
/*     */   }
/*     */ 
/*     */   protected void loadChildren(ViewFactory paramViewFactory)
/*     */   {
/* 102 */     if (paramViewFactory == null)
/*     */     {
/* 105 */       return;
/*     */     }
/* 107 */     Element localElement = getElement();
/* 108 */     int i = localElement.getElementCount();
/* 109 */     if (i > 0) {
/* 110 */       View[] arrayOfView = new View[i];
/* 111 */       for (int j = 0; j < i; j++) {
/* 112 */         arrayOfView[j] = paramViewFactory.create(localElement.getElement(j));
/*     */       }
/* 114 */       replace(0, 0, arrayOfView);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setParent(View paramView)
/*     */   {
/* 136 */     super.setParent(paramView);
/* 137 */     if ((paramView != null) && (this.nchildren == 0)) {
/* 138 */       ViewFactory localViewFactory = getViewFactory();
/* 139 */       loadChildren(localViewFactory);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getViewCount()
/*     */   {
/* 150 */     return this.nchildren;
/*     */   }
/*     */ 
/*     */   public View getView(int paramInt)
/*     */   {
/* 160 */     return this.children[paramInt];
/*     */   }
/*     */ 
/*     */   public void replace(int paramInt1, int paramInt2, View[] paramArrayOfView)
/*     */   {
/* 181 */     if (paramArrayOfView == null) {
/* 182 */       paramArrayOfView = ZERO;
/*     */     }
/*     */ 
/* 186 */     for (int i = paramInt1; i < paramInt1 + paramInt2; i++) {
/* 187 */       if (this.children[i].getParent() == this)
/*     */       {
/* 190 */         this.children[i].setParent(null);
/*     */       }
/* 192 */       this.children[i] = null;
/*     */     }
/*     */ 
/* 196 */     i = paramArrayOfView.length - paramInt2;
/* 197 */     int j = paramInt1 + paramInt2;
/* 198 */     int k = this.nchildren - j;
/* 199 */     int m = j + i;
/* 200 */     if (this.nchildren + i >= this.children.length)
/*     */     {
/* 202 */       n = Math.max(2 * this.children.length, this.nchildren + i);
/* 203 */       View[] arrayOfView = new View[n];
/* 204 */       System.arraycopy(this.children, 0, arrayOfView, 0, paramInt1);
/* 205 */       System.arraycopy(paramArrayOfView, 0, arrayOfView, paramInt1, paramArrayOfView.length);
/* 206 */       System.arraycopy(this.children, j, arrayOfView, m, k);
/* 207 */       this.children = arrayOfView;
/*     */     }
/*     */     else {
/* 210 */       System.arraycopy(this.children, j, this.children, m, k);
/* 211 */       System.arraycopy(paramArrayOfView, 0, this.children, paramInt1, paramArrayOfView.length);
/*     */     }
/* 213 */     this.nchildren += i;
/*     */ 
/* 216 */     for (int n = 0; n < paramArrayOfView.length; n++)
/* 217 */       paramArrayOfView[n].setParent(this);
/*     */   }
/*     */ 
/*     */   public Shape getChildAllocation(int paramInt, Shape paramShape)
/*     */   {
/* 231 */     Rectangle localRectangle = getInsideAllocation(paramShape);
/* 232 */     childAllocation(paramInt, localRectangle);
/* 233 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */     throws BadLocationException
/*     */   {
/* 250 */     int i = paramBias == Position.Bias.Backward ? 1 : 0;
/* 251 */     int j = i != 0 ? Math.max(0, paramInt - 1) : paramInt;
/* 252 */     if ((i != 0) && (j < getStartOffset())) {
/* 253 */       return null;
/*     */     }
/* 255 */     int k = getViewIndexAtPosition(j);
/* 256 */     if ((k != -1) && (k < getViewCount())) {
/* 257 */       View localView = getView(k);
/* 258 */       if ((localView != null) && (j >= localView.getStartOffset()) && (j < localView.getEndOffset()))
/*     */       {
/* 260 */         Shape localShape1 = getChildAllocation(k, paramShape);
/* 261 */         if (localShape1 == null)
/*     */         {
/* 263 */           return null;
/*     */         }
/* 265 */         Shape localShape2 = localView.modelToView(paramInt, localShape1, paramBias);
/* 266 */         if ((localShape2 == null) && (localView.getEndOffset() == paramInt)) {
/* 267 */           k++; if (k < getViewCount()) {
/* 268 */             localView = getView(k);
/* 269 */             localShape2 = localView.modelToView(paramInt, getChildAllocation(k, paramShape), paramBias);
/*     */           }
/*     */         }
/* 272 */         return localShape2;
/*     */       }
/*     */     }
/* 275 */     throw new BadLocationException("Position not represented by view", paramInt);
/*     */   }
/*     */ 
/*     */   public Shape modelToView(int paramInt1, Position.Bias paramBias1, int paramInt2, Position.Bias paramBias2, Shape paramShape)
/*     */     throws BadLocationException
/*     */   {
/* 301 */     if ((paramInt1 == getStartOffset()) && (paramInt2 == getEndOffset())) {
/* 302 */       return paramShape;
/*     */     }
/* 304 */     Rectangle localRectangle1 = getInsideAllocation(paramShape);
/* 305 */     Rectangle localRectangle2 = new Rectangle(localRectangle1);
/* 306 */     View localView1 = getViewAtPosition(paramBias1 == Position.Bias.Backward ? Math.max(0, paramInt1 - 1) : paramInt1, localRectangle2);
/*     */ 
/* 308 */     Rectangle localRectangle3 = new Rectangle(localRectangle1);
/* 309 */     View localView2 = getViewAtPosition(paramBias2 == Position.Bias.Backward ? Math.max(0, paramInt2 - 1) : paramInt2, localRectangle3);
/*     */ 
/* 311 */     if (localView1 == localView2) {
/* 312 */       if (localView1 == null) {
/* 313 */         return paramShape;
/*     */       }
/*     */ 
/* 316 */       return localView1.modelToView(paramInt1, paramBias1, paramInt2, paramBias2, localRectangle2);
/*     */     }
/*     */ 
/* 319 */     int i = getViewCount();
/* 320 */     int j = 0;
/* 321 */     while (j < i)
/*     */     {
/*     */       View localView3;
/* 326 */       if (((localView3 = getView(j)) == localView1) || (localView3 == localView2))
/*     */       {
/* 329 */         Rectangle localRectangle5 = new Rectangle();
/*     */         Rectangle localRectangle4;
/*     */         View localView4;
/* 330 */         if (localView3 == localView1) {
/* 331 */           localRectangle4 = localView1.modelToView(paramInt1, paramBias1, localView1.getEndOffset(), Position.Bias.Backward, localRectangle2).getBounds();
/*     */ 
/* 334 */           localView4 = localView2;
/*     */         }
/*     */         else {
/* 337 */           localRectangle4 = localView2.modelToView(localView2.getStartOffset(), Position.Bias.Forward, paramInt2, paramBias2, localRectangle3).getBounds();
/*     */ 
/* 340 */           localView4 = localView1;
/*     */         }
/*     */         while (true)
/*     */         {
/* 344 */           j++; if ((j >= i) || ((localView3 = getView(j)) == localView4))
/*     */             break;
/* 346 */           localRectangle5.setBounds(localRectangle1);
/* 347 */           childAllocation(j, localRectangle5);
/* 348 */           localRectangle4.add(localRectangle5);
/*     */         }
/*     */ 
/* 352 */         if (localView4 != null)
/*     */         {
/*     */           Shape localShape;
/* 354 */           if (localView4 == localView2) {
/* 355 */             localShape = localView2.modelToView(localView2.getStartOffset(), Position.Bias.Forward, paramInt2, paramBias2, localRectangle3);
/*     */           }
/*     */           else
/*     */           {
/* 360 */             localShape = localView1.modelToView(paramInt1, paramBias1, localView1.getEndOffset(), Position.Bias.Backward, localRectangle2);
/*     */           }
/*     */ 
/* 363 */           if ((localShape instanceof Rectangle)) {
/* 364 */             localRectangle4.add((Rectangle)localShape);
/*     */           }
/*     */           else {
/* 367 */             localRectangle4.add(localShape.getBounds());
/*     */           }
/*     */         }
/* 370 */         return localRectangle4;
/*     */       }
/* 372 */       j++;
/*     */     }
/* 374 */     throw new BadLocationException("Position not represented by view", paramInt1);
/*     */   }
/*     */ 
/*     */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 391 */     Rectangle localRectangle = getInsideAllocation(paramShape);
/*     */     int i;
/* 392 */     if (isBefore((int)paramFloat1, (int)paramFloat2, localRectangle))
/*     */     {
/* 394 */       i = -1;
/*     */       try
/*     */       {
/* 397 */         i = getNextVisualPositionFrom(-1, Position.Bias.Forward, paramShape, 3, paramArrayOfBias);
/*     */       } catch (BadLocationException localBadLocationException1) {
/*     */       } catch (IllegalArgumentException localIllegalArgumentException1) {
/*     */       }
/* 401 */       if (i == -1) {
/* 402 */         i = getStartOffset();
/* 403 */         paramArrayOfBias[0] = Position.Bias.Forward;
/*     */       }
/* 405 */       return i;
/* 406 */     }if (isAfter((int)paramFloat1, (int)paramFloat2, localRectangle))
/*     */     {
/* 408 */       i = -1;
/*     */       try {
/* 410 */         i = getNextVisualPositionFrom(-1, Position.Bias.Forward, paramShape, 7, paramArrayOfBias);
/*     */       } catch (BadLocationException localBadLocationException2) {
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException2) {
/*     */       }
/* 415 */       if (i == -1)
/*     */       {
/* 417 */         i = getEndOffset() - 1;
/* 418 */         paramArrayOfBias[0] = Position.Bias.Forward;
/*     */       }
/* 420 */       return i;
/*     */     }
/*     */ 
/* 423 */     View localView = getViewAtPoint((int)paramFloat1, (int)paramFloat2, localRectangle);
/* 424 */     if (localView != null) {
/* 425 */       return localView.viewToModel(paramFloat1, paramFloat2, localRectangle, paramArrayOfBias);
/*     */     }
/*     */ 
/* 428 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getNextVisualPositionFrom(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */     throws BadLocationException
/*     */   {
/* 461 */     Rectangle localRectangle = getInsideAllocation(paramShape);
/*     */ 
/* 463 */     switch (paramInt2) {
/*     */     case 1:
/* 465 */       return getNextNorthSouthVisualPositionFrom(paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*     */     case 5:
/* 468 */       return getNextNorthSouthVisualPositionFrom(paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*     */     case 3:
/* 471 */       return getNextEastWestVisualPositionFrom(paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*     */     case 7:
/* 474 */       return getNextEastWestVisualPositionFrom(paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*     */     case 2:
/*     */     case 4:
/* 477 */     case 6: } throw new IllegalArgumentException("Bad direction: " + paramInt2);
/*     */   }
/*     */ 
/*     */   public int getViewIndex(int paramInt, Position.Bias paramBias)
/*     */   {
/* 493 */     if (paramBias == Position.Bias.Backward) {
/* 494 */       paramInt--;
/*     */     }
/* 496 */     if ((paramInt >= getStartOffset()) && (paramInt < getEndOffset())) {
/* 497 */       return getViewIndexAtPosition(paramInt);
/*     */     }
/* 499 */     return -1;
/*     */   }
/*     */ 
/*     */   protected abstract boolean isBefore(int paramInt1, int paramInt2, Rectangle paramRectangle);
/*     */ 
/*     */   protected abstract boolean isAfter(int paramInt1, int paramInt2, Rectangle paramRectangle);
/*     */ 
/*     */   protected abstract View getViewAtPoint(int paramInt1, int paramInt2, Rectangle paramRectangle);
/*     */ 
/*     */   protected abstract void childAllocation(int paramInt, Rectangle paramRectangle);
/*     */ 
/*     */   protected View getViewAtPosition(int paramInt, Rectangle paramRectangle)
/*     */   {
/* 557 */     int i = getViewIndexAtPosition(paramInt);
/* 558 */     if ((i >= 0) && (i < getViewCount())) {
/* 559 */       View localView = getView(i);
/* 560 */       if (paramRectangle != null) {
/* 561 */         childAllocation(i, paramRectangle);
/*     */       }
/* 563 */       return localView;
/*     */     }
/* 565 */     return null;
/*     */   }
/*     */ 
/*     */   protected int getViewIndexAtPosition(int paramInt)
/*     */   {
/* 578 */     Element localElement = getElement();
/* 579 */     return localElement.getElementIndex(paramInt);
/*     */   }
/*     */ 
/*     */   protected Rectangle getInsideAllocation(Shape paramShape)
/*     */   {
/* 601 */     if (paramShape != null)
/*     */     {
/*     */       Rectangle localRectangle;
/* 607 */       if ((paramShape instanceof Rectangle))
/* 608 */         localRectangle = (Rectangle)paramShape;
/*     */       else {
/* 610 */         localRectangle = paramShape.getBounds();
/*     */       }
/*     */ 
/* 613 */       this.childAlloc.setBounds(localRectangle);
/* 614 */       this.childAlloc.x += getLeftInset();
/* 615 */       this.childAlloc.y += getTopInset();
/* 616 */       this.childAlloc.width -= getLeftInset() + getRightInset();
/* 617 */       this.childAlloc.height -= getTopInset() + getBottomInset();
/* 618 */       return this.childAlloc;
/*     */     }
/* 620 */     return null;
/*     */   }
/*     */ 
/*     */   protected void setParagraphInsets(AttributeSet paramAttributeSet)
/*     */   {
/* 633 */     this.top = ((short)(int)StyleConstants.getSpaceAbove(paramAttributeSet));
/* 634 */     this.left = ((short)(int)StyleConstants.getLeftIndent(paramAttributeSet));
/* 635 */     this.bottom = ((short)(int)StyleConstants.getSpaceBelow(paramAttributeSet));
/* 636 */     this.right = ((short)(int)StyleConstants.getRightIndent(paramAttributeSet));
/*     */   }
/*     */ 
/*     */   protected void setInsets(short paramShort1, short paramShort2, short paramShort3, short paramShort4)
/*     */   {
/* 648 */     this.top = paramShort1;
/* 649 */     this.left = paramShort2;
/* 650 */     this.right = paramShort4;
/* 651 */     this.bottom = paramShort3;
/*     */   }
/*     */ 
/*     */   protected short getLeftInset()
/*     */   {
/* 660 */     return this.left;
/*     */   }
/*     */ 
/*     */   protected short getRightInset()
/*     */   {
/* 669 */     return this.right;
/*     */   }
/*     */ 
/*     */   protected short getTopInset()
/*     */   {
/* 678 */     return this.top;
/*     */   }
/*     */ 
/*     */   protected short getBottomInset()
/*     */   {
/* 687 */     return this.bottom;
/*     */   }
/*     */ 
/*     */   protected int getNextNorthSouthVisualPositionFrom(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */     throws BadLocationException
/*     */   {
/* 718 */     return Utilities.getNextVisualPositionFrom(this, paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*     */   }
/*     */ 
/*     */   protected int getNextEastWestVisualPositionFrom(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */     throws BadLocationException
/*     */   {
/* 749 */     return Utilities.getNextVisualPositionFrom(this, paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*     */   }
/*     */ 
/*     */   protected boolean flipEastAndWestAtEnds(int paramInt, Position.Bias paramBias)
/*     */   {
/* 778 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.CompositeView
 * JD-Core Version:    0.6.2
 */
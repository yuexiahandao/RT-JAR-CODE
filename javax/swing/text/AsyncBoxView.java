/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.ElementChange;
/*      */ 
/*      */ public class AsyncBoxView extends View
/*      */ {
/*      */   int axis;
/*      */   List<ChildState> stats;
/*      */   float majorSpan;
/*      */   boolean estimatedMajorSpan;
/*      */   float minorSpan;
/*      */   protected ChildLocator locator;
/*      */   float topInset;
/*      */   float bottomInset;
/*      */   float leftInset;
/*      */   float rightInset;
/*      */   ChildState minRequest;
/*      */   ChildState prefRequest;
/*      */   boolean majorChanged;
/*      */   boolean minorChanged;
/*      */   Runnable flushTask;
/*      */   ChildState changing;
/*      */ 
/*      */   public AsyncBoxView(Element paramElement, int paramInt)
/*      */   {
/*   61 */     super(paramElement);
/*   62 */     this.stats = new ArrayList();
/*   63 */     this.axis = paramInt;
/*   64 */     this.locator = new ChildLocator();
/*   65 */     this.flushTask = new FlushTask();
/*   66 */     this.minorSpan = 32767.0F;
/*   67 */     this.estimatedMajorSpan = false;
/*      */   }
/*      */ 
/*      */   public int getMajorAxis()
/*      */   {
/*   76 */     return this.axis;
/*      */   }
/*      */ 
/*      */   public int getMinorAxis()
/*      */   {
/*   85 */     return this.axis == 0 ? 1 : 0;
/*      */   }
/*      */ 
/*      */   public float getTopInset()
/*      */   {
/*   92 */     return this.topInset;
/*      */   }
/*      */ 
/*      */   public void setTopInset(float paramFloat)
/*      */   {
/*  101 */     this.topInset = paramFloat;
/*      */   }
/*      */ 
/*      */   public float getBottomInset()
/*      */   {
/*  108 */     return this.bottomInset;
/*      */   }
/*      */ 
/*      */   public void setBottomInset(float paramFloat)
/*      */   {
/*  117 */     this.bottomInset = paramFloat;
/*      */   }
/*      */ 
/*      */   public float getLeftInset()
/*      */   {
/*  124 */     return this.leftInset;
/*      */   }
/*      */ 
/*      */   public void setLeftInset(float paramFloat)
/*      */   {
/*  133 */     this.leftInset = paramFloat;
/*      */   }
/*      */ 
/*      */   public float getRightInset()
/*      */   {
/*  140 */     return this.rightInset;
/*      */   }
/*      */ 
/*      */   public void setRightInset(float paramFloat)
/*      */   {
/*  149 */     this.rightInset = paramFloat;
/*      */   }
/*      */ 
/*      */   protected float getInsetSpan(int paramInt)
/*      */   {
/*  160 */     float f = paramInt == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
/*      */ 
/*  162 */     return f;
/*      */   }
/*      */ 
/*      */   protected void setEstimatedMajorSpan(boolean paramBoolean)
/*      */   {
/*  179 */     this.estimatedMajorSpan = paramBoolean;
/*      */   }
/*      */ 
/*      */   protected boolean getEstimatedMajorSpan()
/*      */   {
/*  188 */     return this.estimatedMajorSpan;
/*      */   }
/*      */ 
/*      */   protected ChildState getChildState(int paramInt)
/*      */   {
/*  199 */     synchronized (this.stats) {
/*  200 */       if ((paramInt >= 0) && (paramInt < this.stats.size())) {
/*  201 */         return (ChildState)this.stats.get(paramInt);
/*      */       }
/*  203 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected LayoutQueue getLayoutQueue()
/*      */   {
/*  211 */     return LayoutQueue.getDefaultQueue();
/*      */   }
/*      */ 
/*      */   protected ChildState createChildState(View paramView)
/*      */   {
/*  220 */     return new ChildState(paramView);
/*      */   }
/*      */ 
/*      */   protected synchronized void majorRequirementChange(ChildState paramChildState, float paramFloat)
/*      */   {
/*  242 */     if (!this.estimatedMajorSpan) {
/*  243 */       this.majorSpan += paramFloat;
/*      */     }
/*  245 */     this.majorChanged = true;
/*      */   }
/*      */ 
/*      */   protected synchronized void minorRequirementChange(ChildState paramChildState)
/*      */   {
/*  259 */     this.minorChanged = true;
/*      */   }
/*      */ 
/*      */   protected void flushRequirementChanges()
/*      */   {
/*  267 */     AbstractDocument localAbstractDocument = (AbstractDocument)getDocument();
/*      */     try {
/*  269 */       localAbstractDocument.readLock();
/*      */ 
/*  271 */       View localView = null;
/*  272 */       boolean bool1 = false;
/*  273 */       boolean bool2 = false;
/*      */ 
/*  275 */       synchronized (this)
/*      */       {
/*  278 */         synchronized (this.stats) {
/*  279 */           int i = getViewCount();
/*  280 */           if ((i > 0) && ((this.minorChanged) || (this.estimatedMajorSpan))) {
/*  281 */             LayoutQueue localLayoutQueue = getLayoutQueue();
/*  282 */             Object localObject1 = getChildState(0);
/*  283 */             Object localObject2 = getChildState(0);
/*  284 */             float f = 0.0F;
/*  285 */             for (int j = 1; j < i; j++) {
/*  286 */               ChildState localChildState = getChildState(j);
/*  287 */               if (this.minorChanged) {
/*  288 */                 if (localChildState.min > ((ChildState)localObject1).min) {
/*  289 */                   localObject1 = localChildState;
/*      */                 }
/*  291 */                 if (localChildState.pref > ((ChildState)localObject2).pref) {
/*  292 */                   localObject2 = localChildState;
/*      */                 }
/*      */               }
/*  295 */               if (this.estimatedMajorSpan) {
/*  296 */                 f += localChildState.getMajorSpan();
/*      */               }
/*      */             }
/*      */ 
/*  300 */             if (this.minorChanged) {
/*  301 */               this.minRequest = ((ChildState)localObject1);
/*  302 */               this.prefRequest = ((ChildState)localObject2);
/*      */             }
/*  304 */             if (this.estimatedMajorSpan) {
/*  305 */               this.majorSpan = f;
/*  306 */               this.estimatedMajorSpan = false;
/*  307 */               this.majorChanged = true;
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  313 */         if ((this.majorChanged) || (this.minorChanged)) {
/*  314 */           localView = getParent();
/*  315 */           if (localView != null) {
/*  316 */             if (this.axis == 0) {
/*  317 */               bool1 = this.majorChanged;
/*  318 */               bool2 = this.minorChanged;
/*      */             } else {
/*  320 */               bool2 = this.majorChanged;
/*  321 */               bool1 = this.minorChanged;
/*      */             }
/*      */           }
/*  324 */           this.majorChanged = false;
/*  325 */           this.minorChanged = false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  331 */       if (localView != null) {
/*  332 */         localView.preferenceChanged(this, bool1, bool2);
/*      */ 
/*  335 */         ??? = getContainer();
/*  336 */         if (??? != null)
/*  337 */           ((Component)???).repaint();
/*      */       }
/*      */     }
/*      */     finally {
/*  341 */       localAbstractDocument.readUnlock();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void replace(int paramInt1, int paramInt2, View[] paramArrayOfView)
/*      */   {
/*  358 */     synchronized (this.stats)
/*      */     {
/*  360 */       for (int i = 0; i < paramInt2; i++) {
/*  361 */         ChildState localChildState1 = (ChildState)this.stats.remove(paramInt1);
/*  362 */         float f = localChildState1.getMajorSpan();
/*      */ 
/*  364 */         localChildState1.getChildView().setParent(null);
/*  365 */         if (f != 0.0F) {
/*  366 */           majorRequirementChange(localChildState1, -f);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  371 */       LayoutQueue localLayoutQueue = getLayoutQueue();
/*  372 */       if (paramArrayOfView != null) {
/*  373 */         for (int j = 0; j < paramArrayOfView.length; j++) {
/*  374 */           ChildState localChildState2 = createChildState(paramArrayOfView[j]);
/*  375 */           this.stats.add(paramInt1 + j, localChildState2);
/*  376 */           localLayoutQueue.addTask(localChildState2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  381 */       localLayoutQueue.addTask(this.flushTask);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void loadChildren(ViewFactory paramViewFactory)
/*      */   {
/*  404 */     Element localElement = getElement();
/*  405 */     int i = localElement.getElementCount();
/*  406 */     if (i > 0) {
/*  407 */       View[] arrayOfView = new View[i];
/*  408 */       for (int j = 0; j < i; j++) {
/*  409 */         arrayOfView[j] = paramViewFactory.create(localElement.getElement(j));
/*      */       }
/*  411 */       replace(0, 0, arrayOfView);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected synchronized int getViewIndexAtPosition(int paramInt, Position.Bias paramBias)
/*      */   {
/*  425 */     int i = paramBias == Position.Bias.Backward ? 1 : 0;
/*  426 */     paramInt = i != 0 ? Math.max(0, paramInt - 1) : paramInt;
/*  427 */     Element localElement = getElement();
/*  428 */     return localElement.getElementIndex(paramInt);
/*      */   }
/*      */ 
/*      */   protected void updateLayout(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, Shape paramShape)
/*      */   {
/*  447 */     if (paramElementChange != null)
/*      */     {
/*  452 */       int i = Math.max(paramElementChange.getIndex() - 1, 0);
/*  453 */       ChildState localChildState = getChildState(i);
/*  454 */       this.locator.childChanged(localChildState);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setParent(View paramView)
/*      */   {
/*  476 */     super.setParent(paramView);
/*  477 */     if ((paramView != null) && (getViewCount() == 0)) {
/*  478 */       ViewFactory localViewFactory = getViewFactory();
/*  479 */       loadChildren(localViewFactory);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void preferenceChanged(View paramView, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  496 */     if (paramView == null) {
/*  497 */       getParent().preferenceChanged(this, paramBoolean1, paramBoolean2);
/*      */     } else {
/*  499 */       if (this.changing != null) {
/*  500 */         View localView = this.changing.getChildView();
/*  501 */         if (localView == paramView)
/*      */         {
/*  504 */           this.changing.preferenceChanged(paramBoolean1, paramBoolean2);
/*  505 */           return;
/*      */         }
/*      */       }
/*  508 */       int i = getViewIndex(paramView.getStartOffset(), Position.Bias.Forward);
/*      */ 
/*  510 */       ChildState localChildState = getChildState(i);
/*  511 */       localChildState.preferenceChanged(paramBoolean1, paramBoolean2);
/*  512 */       LayoutQueue localLayoutQueue = getLayoutQueue();
/*  513 */       localLayoutQueue.addTask(localChildState);
/*  514 */       localLayoutQueue.addTask(this.flushTask);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSize(float paramFloat1, float paramFloat2)
/*      */   {
/*  532 */     setSpanOnAxis(0, paramFloat1);
/*  533 */     setSpanOnAxis(1, paramFloat2);
/*      */   }
/*      */ 
/*      */   float getSpanOnAxis(int paramInt)
/*      */   {
/*  544 */     if (paramInt == getMajorAxis()) {
/*  545 */       return this.majorSpan;
/*      */     }
/*  547 */     return this.minorSpan;
/*      */   }
/*      */ 
/*      */   void setSpanOnAxis(int paramInt, float paramFloat)
/*      */   {
/*  562 */     float f1 = getInsetSpan(paramInt);
/*  563 */     if (paramInt == getMinorAxis()) {
/*  564 */       float f2 = paramFloat - f1;
/*  565 */       if (f2 != this.minorSpan) {
/*  566 */         this.minorSpan = f2;
/*      */ 
/*  570 */         int i = getViewCount();
/*  571 */         if (i != 0) {
/*  572 */           LayoutQueue localLayoutQueue = getLayoutQueue();
/*  573 */           for (int j = 0; j < i; j++) {
/*  574 */             ChildState localChildState = getChildState(j);
/*  575 */             localChildState.childSizeValid = false;
/*  576 */             localLayoutQueue.addTask(localChildState);
/*      */           }
/*  578 */           localLayoutQueue.addTask(this.flushTask);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*  585 */     else if (this.estimatedMajorSpan) {
/*  586 */       this.majorSpan = (paramFloat - f1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, Shape paramShape)
/*      */   {
/*  610 */     synchronized (this.locator) {
/*  611 */       this.locator.setAllocation(paramShape);
/*  612 */       this.locator.paintChildren(paramGraphics);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float getPreferredSpan(int paramInt)
/*      */   {
/*  628 */     float f = getInsetSpan(paramInt);
/*  629 */     if (paramInt == this.axis) {
/*  630 */       return this.majorSpan + f;
/*      */     }
/*  632 */     if (this.prefRequest != null) {
/*  633 */       View localView = this.prefRequest.getChildView();
/*  634 */       return localView.getPreferredSpan(paramInt) + f;
/*      */     }
/*      */ 
/*  638 */     return f + 30.0F;
/*      */   }
/*      */ 
/*      */   public float getMinimumSpan(int paramInt)
/*      */   {
/*  653 */     if (paramInt == this.axis) {
/*  654 */       return getPreferredSpan(paramInt);
/*      */     }
/*  656 */     if (this.minRequest != null) {
/*  657 */       View localView = this.minRequest.getChildView();
/*  658 */       return localView.getMinimumSpan(paramInt);
/*      */     }
/*      */ 
/*  662 */     if (paramInt == 0) {
/*  663 */       return getLeftInset() + getRightInset() + 5.0F;
/*      */     }
/*  665 */     return getTopInset() + getBottomInset() + 5.0F;
/*      */   }
/*      */ 
/*      */   public float getMaximumSpan(int paramInt)
/*      */   {
/*  681 */     if (paramInt == this.axis) {
/*  682 */       return getPreferredSpan(paramInt);
/*      */     }
/*  684 */     return 2.147484E+009F;
/*      */   }
/*      */ 
/*      */   public int getViewCount()
/*      */   {
/*  697 */     synchronized (this.stats) {
/*  698 */       return this.stats.size();
/*      */     }
/*      */   }
/*      */ 
/*      */   public View getView(int paramInt)
/*      */   {
/*  710 */     ChildState localChildState = getChildState(paramInt);
/*  711 */     if (localChildState != null) {
/*  712 */       return localChildState.getChildView();
/*      */     }
/*  714 */     return null;
/*      */   }
/*      */ 
/*      */   public Shape getChildAllocation(int paramInt, Shape paramShape)
/*      */   {
/*  729 */     Shape localShape = this.locator.getChildAllocation(paramInt, paramShape);
/*  730 */     return localShape;
/*      */   }
/*      */ 
/*      */   public int getViewIndex(int paramInt, Position.Bias paramBias)
/*      */   {
/*  745 */     return getViewIndexAtPosition(paramInt, paramBias);
/*      */   }
/*      */ 
/*      */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*      */     throws BadLocationException
/*      */   {
/*  764 */     int i = getViewIndex(paramInt, paramBias);
/*  765 */     Shape localShape1 = this.locator.getChildAllocation(i, paramShape);
/*      */ 
/*  770 */     ChildState localChildState = getChildState(i);
/*  771 */     synchronized (localChildState) {
/*  772 */       View localView = localChildState.getChildView();
/*  773 */       Shape localShape2 = localView.modelToView(paramInt, localShape1, paramBias);
/*  774 */       return localShape2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*      */   {
/*      */     int j;
/*      */     Shape localShape;
/*  809 */     synchronized (this.locator) {
/*  810 */       j = this.locator.getViewIndexAtPoint(paramFloat1, paramFloat2, paramShape);
/*  811 */       localShape = this.locator.getChildAllocation(j, paramShape);
/*      */     }
/*      */ 
/*  817 */     ??? = getChildState(j);
/*      */     int i;
/*  818 */     synchronized (???) {
/*  819 */       View localView = ((ChildState)???).getChildView();
/*  820 */       i = localView.viewToModel(paramFloat1, paramFloat2, localShape, paramArrayOfBias);
/*      */     }
/*  822 */     return i;
/*      */   }
/*      */ 
/*      */   public int getNextVisualPositionFrom(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*      */     throws BadLocationException
/*      */   {
/*  852 */     return Utilities.getNextVisualPositionFrom(this, paramInt1, paramBias, paramShape, paramInt2, paramArrayOfBias);
/*      */   }
/*      */ 
/*      */   public class ChildLocator
/*      */   {
/*      */     protected AsyncBoxView.ChildState lastValidOffset;
/*      */     protected Rectangle lastAlloc;
/*      */     protected Rectangle childAlloc;
/*      */ 
/*      */     public ChildLocator()
/*      */     {
/*  931 */       this.lastAlloc = new Rectangle();
/*  932 */       this.childAlloc = new Rectangle();
/*      */     }
/*      */ 
/*      */     public synchronized void childChanged(AsyncBoxView.ChildState paramChildState)
/*      */     {
/*  943 */       if (this.lastValidOffset == null)
/*  944 */         this.lastValidOffset = paramChildState;
/*  945 */       else if (paramChildState.getChildView().getStartOffset() < this.lastValidOffset.getChildView().getStartOffset())
/*      */       {
/*  947 */         this.lastValidOffset = paramChildState;
/*      */       }
/*      */     }
/*      */ 
/*      */     public synchronized void paintChildren(Graphics paramGraphics)
/*      */     {
/*  955 */       Rectangle localRectangle = paramGraphics.getClipBounds();
/*  956 */       float f1 = AsyncBoxView.this.axis == 0 ? localRectangle.x - this.lastAlloc.x : localRectangle.y - this.lastAlloc.y;
/*      */ 
/*  958 */       int i = getViewIndexAtVisualOffset(f1);
/*  959 */       int j = AsyncBoxView.this.getViewCount();
/*  960 */       float f2 = AsyncBoxView.this.getChildState(i).getMajorOffset();
/*  961 */       for (int k = i; k < j; k++) {
/*  962 */         AsyncBoxView.ChildState localChildState = AsyncBoxView.this.getChildState(k);
/*  963 */         localChildState.setMajorOffset(f2);
/*  964 */         Shape localShape = getChildAllocation(k);
/*  965 */         if (!intersectsClip(localShape, localRectangle)) break;
/*  966 */         synchronized (localChildState) {
/*  967 */           View localView = localChildState.getChildView();
/*  968 */           localView.paint(paramGraphics, localShape);
/*      */         }
/*      */ 
/*  974 */         f2 += localChildState.getMajorSpan();
/*      */       }
/*      */     }
/*      */ 
/*      */     public synchronized Shape getChildAllocation(int paramInt, Shape paramShape)
/*      */     {
/*  984 */       if (paramShape == null) {
/*  985 */         return null;
/*      */       }
/*  987 */       setAllocation(paramShape);
/*  988 */       AsyncBoxView.ChildState localChildState = AsyncBoxView.this.getChildState(paramInt);
/*  989 */       if (this.lastValidOffset == null) {
/*  990 */         this.lastValidOffset = AsyncBoxView.this.getChildState(0);
/*      */       }
/*  992 */       if (localChildState.getChildView().getStartOffset() > this.lastValidOffset.getChildView().getStartOffset())
/*      */       {
/*  995 */         updateChildOffsetsToIndex(paramInt);
/*      */       }
/*  997 */       Shape localShape = getChildAllocation(paramInt);
/*  998 */       return localShape;
/*      */     }
/*      */ 
/*      */     public int getViewIndexAtPoint(float paramFloat1, float paramFloat2, Shape paramShape)
/*      */     {
/* 1016 */       setAllocation(paramShape);
/* 1017 */       float f = AsyncBoxView.this.axis == 0 ? paramFloat1 - this.lastAlloc.x : paramFloat2 - this.lastAlloc.y;
/* 1018 */       int i = getViewIndexAtVisualOffset(f);
/* 1019 */       return i;
/*      */     }
/*      */ 
/*      */     protected Shape getChildAllocation(int paramInt)
/*      */     {
/* 1028 */       AsyncBoxView.ChildState localChildState = AsyncBoxView.this.getChildState(paramInt);
/* 1029 */       if (!localChildState.isLayoutValid()) {
/* 1030 */         localChildState.run();
/*      */       }
/* 1032 */       if (AsyncBoxView.this.axis == 0) {
/* 1033 */         this.childAlloc.x = (this.lastAlloc.x + (int)localChildState.getMajorOffset());
/* 1034 */         this.childAlloc.y = (this.lastAlloc.y + (int)localChildState.getMinorOffset());
/* 1035 */         this.childAlloc.width = ((int)localChildState.getMajorSpan());
/* 1036 */         this.childAlloc.height = ((int)localChildState.getMinorSpan());
/*      */       } else {
/* 1038 */         this.childAlloc.y = (this.lastAlloc.y + (int)localChildState.getMajorOffset());
/* 1039 */         this.childAlloc.x = (this.lastAlloc.x + (int)localChildState.getMinorOffset());
/* 1040 */         this.childAlloc.height = ((int)localChildState.getMajorSpan());
/* 1041 */         this.childAlloc.width = ((int)localChildState.getMinorSpan());
/*      */       }
/* 1043 */       this.childAlloc.x += (int)AsyncBoxView.this.getLeftInset();
/* 1044 */       this.childAlloc.y += (int)AsyncBoxView.this.getRightInset();
/* 1045 */       return this.childAlloc;
/*      */     }
/*      */ 
/*      */     protected void setAllocation(Shape paramShape)
/*      */     {
/* 1054 */       if ((paramShape instanceof Rectangle))
/* 1055 */         this.lastAlloc.setBounds((Rectangle)paramShape);
/*      */       else {
/* 1057 */         this.lastAlloc.setBounds(paramShape.getBounds());
/*      */       }
/* 1059 */       AsyncBoxView.this.setSize(this.lastAlloc.width, this.lastAlloc.height);
/*      */     }
/*      */ 
/*      */     protected int getViewIndexAtVisualOffset(float paramFloat)
/*      */     {
/* 1073 */       int i = AsyncBoxView.this.getViewCount();
/* 1074 */       if (i > 0) {
/* 1075 */         int j = this.lastValidOffset != null ? 1 : 0;
/*      */ 
/* 1077 */         if (this.lastValidOffset == null) {
/* 1078 */           this.lastValidOffset = AsyncBoxView.this.getChildState(0);
/*      */         }
/* 1080 */         if (paramFloat > AsyncBoxView.this.majorSpan)
/*      */         {
/* 1082 */           if (j == 0) {
/* 1083 */             return 0;
/*      */           }
/* 1085 */           int k = this.lastValidOffset.getChildView().getStartOffset();
/* 1086 */           m = AsyncBoxView.this.getViewIndex(k, Position.Bias.Forward);
/* 1087 */           return m;
/* 1088 */         }if (paramFloat > this.lastValidOffset.getMajorOffset())
/*      */         {
/* 1090 */           return updateChildOffsets(paramFloat);
/*      */         }
/*      */ 
/* 1094 */         float f1 = 0.0F;
/* 1095 */         for (int m = 0; m < i; m++) {
/* 1096 */           AsyncBoxView.ChildState localChildState = AsyncBoxView.this.getChildState(m);
/* 1097 */           float f2 = f1 + localChildState.getMajorSpan();
/* 1098 */           if (paramFloat < f2) {
/* 1099 */             return m;
/*      */           }
/* 1101 */           f1 = f2;
/*      */         }
/*      */       }
/*      */ 
/* 1105 */       return i - 1;
/*      */     }
/*      */ 
/*      */     int updateChildOffsets(float paramFloat)
/*      */     {
/* 1113 */       int i = AsyncBoxView.this.getViewCount();
/* 1114 */       int j = i - 1;
/* 1115 */       int k = this.lastValidOffset.getChildView().getStartOffset();
/* 1116 */       int m = AsyncBoxView.this.getViewIndex(k, Position.Bias.Forward);
/* 1117 */       float f1 = this.lastValidOffset.getMajorOffset();
/* 1118 */       float f2 = f1;
/* 1119 */       for (int n = m; n < i; n++) {
/* 1120 */         AsyncBoxView.ChildState localChildState = AsyncBoxView.this.getChildState(n);
/* 1121 */         localChildState.setMajorOffset(f2);
/* 1122 */         f2 += localChildState.getMajorSpan();
/* 1123 */         if (paramFloat < f2) {
/* 1124 */           j = n;
/* 1125 */           this.lastValidOffset = localChildState;
/* 1126 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 1130 */       return j;
/*      */     }
/*      */ 
/*      */     void updateChildOffsetsToIndex(int paramInt)
/*      */     {
/* 1138 */       int i = this.lastValidOffset.getChildView().getStartOffset();
/* 1139 */       int j = AsyncBoxView.this.getViewIndex(i, Position.Bias.Forward);
/* 1140 */       float f = this.lastValidOffset.getMajorOffset();
/* 1141 */       for (int k = j; k <= paramInt; k++) {
/* 1142 */         AsyncBoxView.ChildState localChildState = AsyncBoxView.this.getChildState(k);
/* 1143 */         localChildState.setMajorOffset(f);
/* 1144 */         f += localChildState.getMajorSpan();
/*      */       }
/*      */     }
/*      */ 
/*      */     boolean intersectsClip(Shape paramShape, Rectangle paramRectangle) {
/* 1149 */       Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*      */ 
/* 1151 */       if (localRectangle.intersects(paramRectangle))
/*      */       {
/* 1154 */         return this.lastAlloc.intersects(localRectangle);
/*      */       }
/* 1156 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public class ChildState
/*      */     implements Runnable
/*      */   {
/*      */     private float min;
/*      */     private float pref;
/*      */     private float max;
/*      */     private boolean minorValid;
/*      */     private float span;
/*      */     private float offset;
/*      */     private boolean majorValid;
/*      */     private View child;
/*      */     private boolean childSizeValid;
/*      */ 
/*      */     public ChildState(View arg2)
/*      */     {
/*      */       Object localObject;
/* 1199 */       this.child = localObject;
/* 1200 */       this.minorValid = false;
/* 1201 */       this.majorValid = false;
/* 1202 */       this.childSizeValid = false;
/* 1203 */       this.child.setParent(AsyncBoxView.this);
/*      */     }
/*      */ 
/*      */     public View getChildView()
/*      */     {
/* 1210 */       return this.child;
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/* 1235 */       AbstractDocument localAbstractDocument = (AbstractDocument)AsyncBoxView.this.getDocument();
/*      */       try {
/* 1237 */         localAbstractDocument.readLock();
/* 1238 */         if ((this.minorValid) && (this.majorValid) && (this.childSizeValid))
/*      */         {
/*      */           return;
/*      */         }
/* 1242 */         if (this.child.getParent() == AsyncBoxView.this)
/*      */         {
/* 1247 */           synchronized (AsyncBoxView.this) {
/* 1248 */             AsyncBoxView.this.changing = this;
/*      */           }
/* 1250 */           updateChild();
/* 1251 */           synchronized (AsyncBoxView.this) {
/* 1252 */             AsyncBoxView.this.changing = null;
/*      */           }
/*      */ 
/* 1258 */           updateChild();
/*      */         }
/*      */       } finally {
/* 1261 */         localAbstractDocument.readUnlock();
/*      */       }
/*      */     }
/*      */ 
/*      */     void updateChild() {
/* 1266 */       int i = 0;
/* 1267 */       synchronized (this) {
/* 1268 */         if (!this.minorValid) {
/* 1269 */           int k = AsyncBoxView.this.getMinorAxis();
/* 1270 */           this.min = this.child.getMinimumSpan(k);
/* 1271 */           this.pref = this.child.getPreferredSpan(k);
/* 1272 */           this.max = this.child.getMaximumSpan(k);
/* 1273 */           this.minorValid = true;
/* 1274 */           i = 1;
/*      */         }
/*      */       }
/* 1277 */       if (i != 0) {
/* 1278 */         AsyncBoxView.this.minorRequirementChange(this);
/*      */       }
/*      */ 
/* 1281 */       int j = 0;
/* 1282 */       float f1 = 0.0F;
/*      */       float f2;
/* 1283 */       synchronized (this) {
/* 1284 */         if (!this.majorValid) {
/* 1285 */           f2 = this.span;
/* 1286 */           this.span = this.child.getPreferredSpan(AsyncBoxView.this.axis);
/* 1287 */           f1 = this.span - f2;
/* 1288 */           this.majorValid = true;
/* 1289 */           j = 1;
/*      */         }
/*      */       }
/* 1292 */       if (j != 0) {
/* 1293 */         AsyncBoxView.this.majorRequirementChange(this, f1);
/* 1294 */         AsyncBoxView.this.locator.childChanged(this);
/*      */       }
/*      */ 
/* 1297 */       synchronized (this) {
/* 1298 */         if (!this.childSizeValid)
/*      */         {
/*      */           float f3;
/* 1301 */           if (AsyncBoxView.this.axis == 0) {
/* 1302 */             f2 = this.span;
/* 1303 */             f3 = getMinorSpan();
/*      */           } else {
/* 1305 */             f2 = getMinorSpan();
/* 1306 */             f3 = this.span;
/*      */           }
/* 1308 */           this.childSizeValid = true;
/* 1309 */           this.child.setSize(f2, f3);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public float getMinorSpan()
/*      */     {
/* 1319 */       if (this.max < AsyncBoxView.this.minorSpan) {
/* 1320 */         return this.max;
/*      */       }
/*      */ 
/* 1323 */       return Math.max(this.min, AsyncBoxView.this.minorSpan);
/*      */     }
/*      */ 
/*      */     public float getMinorOffset()
/*      */     {
/* 1330 */       if (this.max < AsyncBoxView.this.minorSpan)
/*      */       {
/* 1332 */         float f = this.child.getAlignment(AsyncBoxView.this.getMinorAxis());
/* 1333 */         return (AsyncBoxView.this.minorSpan - this.max) * f;
/*      */       }
/* 1335 */       return 0.0F;
/*      */     }
/*      */ 
/*      */     public float getMajorSpan()
/*      */     {
/* 1342 */       return this.span;
/*      */     }
/*      */ 
/*      */     public float getMajorOffset()
/*      */     {
/* 1349 */       return this.offset;
/*      */     }
/*      */ 
/*      */     public void setMajorOffset(float paramFloat)
/*      */     {
/* 1358 */       this.offset = paramFloat;
/*      */     }
/*      */ 
/*      */     public void preferenceChanged(boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/* 1369 */       if (AsyncBoxView.this.axis == 0) {
/* 1370 */         if (paramBoolean1) {
/* 1371 */           this.majorValid = false;
/*      */         }
/* 1373 */         if (paramBoolean2)
/* 1374 */           this.minorValid = false;
/*      */       }
/*      */       else {
/* 1377 */         if (paramBoolean1) {
/* 1378 */           this.minorValid = false;
/*      */         }
/* 1380 */         if (paramBoolean2) {
/* 1381 */           this.majorValid = false;
/*      */         }
/*      */       }
/* 1384 */       this.childSizeValid = false;
/*      */     }
/*      */ 
/*      */     public boolean isLayoutValid()
/*      */     {
/* 1391 */       return (this.minorValid) && (this.majorValid) && (this.childSizeValid);
/*      */     }
/*      */   }
/*      */ 
/*      */   class FlushTask
/*      */     implements Runnable
/*      */   {
/*      */     FlushTask()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/* 1415 */       AsyncBoxView.this.flushRequirementChanges();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.AsyncBoxView
 * JD-Core Version:    0.6.2
 */
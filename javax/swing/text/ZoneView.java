/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Shape;
/*     */ import java.util.Vector;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentEvent.ElementChange;
/*     */ 
/*     */ public class ZoneView extends BoxView
/*     */ {
/*  80 */   int maxZoneSize = 8192;
/*  81 */   int maxZonesLoaded = 3;
/*     */   Vector<View> loadedZones;
/*     */ 
/*     */   public ZoneView(Element paramElement, int paramInt)
/*     */   {
/*  91 */     super(paramElement, paramInt);
/*  92 */     this.loadedZones = new Vector();
/*     */   }
/*     */ 
/*     */   public int getMaximumZoneSize()
/*     */   {
/*  99 */     return this.maxZoneSize;
/*     */   }
/*     */ 
/*     */   public void setMaximumZoneSize(int paramInt)
/*     */   {
/* 114 */     this.maxZoneSize = paramInt;
/*     */   }
/*     */ 
/*     */   public int getMaxZonesLoaded()
/*     */   {
/* 122 */     return this.maxZonesLoaded;
/*     */   }
/*     */ 
/*     */   public void setMaxZonesLoaded(int paramInt)
/*     */   {
/* 136 */     if (paramInt < 1) {
/* 137 */       throw new IllegalArgumentException("ZoneView.setMaxZonesLoaded must be greater than 0.");
/*     */     }
/* 139 */     this.maxZonesLoaded = paramInt;
/* 140 */     unloadOldZones();
/*     */   }
/*     */ 
/*     */   protected void zoneWasLoaded(View paramView)
/*     */   {
/* 154 */     this.loadedZones.addElement(paramView);
/* 155 */     unloadOldZones();
/*     */   }
/*     */ 
/*     */   void unloadOldZones() {
/* 159 */     while (this.loadedZones.size() > getMaxZonesLoaded()) {
/* 160 */       View localView = (View)this.loadedZones.elementAt(0);
/* 161 */       this.loadedZones.removeElementAt(0);
/* 162 */       unloadZone(localView);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void unloadZone(View paramView)
/*     */   {
/* 178 */     paramView.removeAll();
/*     */   }
/*     */ 
/*     */   protected boolean isZoneLoaded(View paramView)
/*     */   {
/* 189 */     return paramView.getViewCount() > 0;
/*     */   }
/*     */ 
/*     */   protected View createZone(int paramInt1, int paramInt2)
/*     */   {
/* 208 */     Document localDocument = getDocument();
/*     */     Zone localZone;
/*     */     try
/*     */     {
/* 211 */       localZone = new Zone(getElement(), localDocument.createPosition(paramInt1), localDocument.createPosition(paramInt2));
/*     */     }
/*     */     catch (BadLocationException localBadLocationException)
/*     */     {
/* 216 */       throw new StateInvariantError(localBadLocationException.getMessage());
/*     */     }
/* 218 */     return localZone;
/*     */   }
/*     */ 
/*     */   protected void loadChildren(ViewFactory paramViewFactory)
/*     */   {
/* 234 */     Document localDocument = getDocument();
/* 235 */     int i = getStartOffset();
/* 236 */     int j = getEndOffset();
/* 237 */     append(createZone(i, j));
/* 238 */     handleInsert(i, j - i);
/*     */   }
/*     */ 
/*     */   protected int getViewIndexAtPosition(int paramInt)
/*     */   {
/* 252 */     int i = getViewCount();
/* 253 */     if (paramInt == getEndOffset()) {
/* 254 */       return i - 1;
/*     */     }
/* 256 */     for (int j = 0; j < i; j++) {
/* 257 */       View localView = getView(j);
/* 258 */       if ((paramInt >= localView.getStartOffset()) && (paramInt < localView.getEndOffset()))
/*     */       {
/* 260 */         return j;
/*     */       }
/*     */     }
/* 263 */     return -1;
/*     */   }
/*     */ 
/*     */   void handleInsert(int paramInt1, int paramInt2) {
/* 267 */     int i = getViewIndex(paramInt1, Position.Bias.Forward);
/* 268 */     View localView = getView(i);
/* 269 */     int j = localView.getStartOffset();
/* 270 */     int k = localView.getEndOffset();
/* 271 */     if (k - j > this.maxZoneSize)
/* 272 */       splitZone(i, j, k);
/*     */   }
/*     */ 
/*     */   void handleRemove(int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   void splitZone(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 286 */     Element localElement = getElement();
/* 287 */     Document localDocument = localElement.getDocument();
/* 288 */     Vector localVector = new Vector();
/* 289 */     int i = paramInt2;
/*     */     do {
/* 291 */       paramInt2 = i;
/* 292 */       i = Math.min(getDesiredZoneEnd(paramInt2), paramInt3);
/* 293 */       localVector.addElement(createZone(paramInt2, i));
/* 294 */     }while (i < paramInt3);
/* 295 */     View localView = getView(paramInt1);
/* 296 */     View[] arrayOfView = new View[localVector.size()];
/* 297 */     localVector.copyInto(arrayOfView);
/* 298 */     replace(paramInt1, 1, arrayOfView);
/*     */   }
/*     */ 
/*     */   int getDesiredZoneEnd(int paramInt)
/*     */   {
/* 308 */     Element localElement1 = getElement();
/* 309 */     int i = localElement1.getElementIndex(paramInt + this.maxZoneSize / 2);
/* 310 */     Element localElement2 = localElement1.getElement(i);
/* 311 */     int j = localElement2.getStartOffset();
/* 312 */     int k = localElement2.getEndOffset();
/* 313 */     if ((k - paramInt > this.maxZoneSize) && 
/* 314 */       (j > paramInt)) {
/* 315 */       return j;
/*     */     }
/*     */ 
/* 318 */     return k;
/*     */   }
/*     */ 
/*     */   protected boolean updateChildren(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, ViewFactory paramViewFactory)
/*     */   {
/* 332 */     return false;
/*     */   }
/*     */ 
/*     */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 348 */     handleInsert(paramDocumentEvent.getOffset(), paramDocumentEvent.getLength());
/* 349 */     super.insertUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*     */   }
/*     */ 
/*     */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 365 */     handleRemove(paramDocumentEvent.getOffset(), paramDocumentEvent.getLength());
/* 366 */     super.removeUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*     */   }
/*     */ 
/*     */   class Zone extends AsyncBoxView
/*     */   {
/*     */     private Position start;
/*     */     private Position end;
/*     */ 
/*     */     public Zone(Element paramPosition1, Position paramPosition2, Position arg4)
/*     */     {
/* 380 */       super(ZoneView.this.getAxis());
/* 381 */       this.start = paramPosition2;
/*     */       Object localObject;
/* 382 */       this.end = localObject;
/*     */     }
/*     */ 
/*     */     public void load()
/*     */     {
/* 393 */       if (!isLoaded()) {
/* 394 */         setEstimatedMajorSpan(true);
/* 395 */         Element localElement = getElement();
/* 396 */         ViewFactory localViewFactory = getViewFactory();
/* 397 */         int i = localElement.getElementIndex(getStartOffset());
/* 398 */         int j = localElement.getElementIndex(getEndOffset());
/* 399 */         View[] arrayOfView = new View[j - i + 1];
/* 400 */         for (int k = i; k <= j; k++) {
/* 401 */           arrayOfView[(k - i)] = localViewFactory.create(localElement.getElement(k));
/*     */         }
/* 403 */         replace(0, 0, arrayOfView);
/*     */ 
/* 405 */         ZoneView.this.zoneWasLoaded(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void unload()
/*     */     {
/* 414 */       setEstimatedMajorSpan(true);
/* 415 */       removeAll();
/*     */     }
/*     */ 
/*     */     public boolean isLoaded()
/*     */     {
/* 423 */       return getViewCount() != 0;
/*     */     }
/*     */ 
/*     */     protected void loadChildren(ViewFactory paramViewFactory)
/*     */     {
/* 437 */       setEstimatedMajorSpan(true);
/*     */ 
/* 440 */       Element localElement = getElement();
/* 441 */       int i = localElement.getElementIndex(getStartOffset());
/* 442 */       int j = localElement.getElementIndex(getEndOffset());
/* 443 */       int k = j - i;
/*     */ 
/* 448 */       View localView = paramViewFactory.create(localElement.getElement(i));
/* 449 */       localView.setParent(this);
/* 450 */       float f1 = localView.getPreferredSpan(0);
/* 451 */       float f2 = localView.getPreferredSpan(1);
/* 452 */       if (getMajorAxis() == 0)
/* 453 */         f1 *= k;
/*     */       else {
/* 455 */         f2 += k;
/*     */       }
/*     */ 
/* 458 */       setSize(f1, f2);
/*     */     }
/*     */ 
/*     */     protected void flushRequirementChanges()
/*     */     {
/* 472 */       if (isLoaded())
/* 473 */         super.flushRequirementChanges();
/*     */     }
/*     */ 
/*     */     public int getViewIndex(int paramInt, Position.Bias paramBias)
/*     */     {
/* 490 */       int i = paramBias == Position.Bias.Backward ? 1 : 0;
/* 491 */       paramInt = i != 0 ? Math.max(0, paramInt - 1) : paramInt;
/* 492 */       Element localElement = getElement();
/* 493 */       int j = localElement.getElementIndex(paramInt);
/* 494 */       int k = localElement.getElementIndex(getStartOffset());
/* 495 */       return j - k;
/*     */     }
/*     */ 
/*     */     protected boolean updateChildren(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, ViewFactory paramViewFactory)
/*     */     {
/* 501 */       Element[] arrayOfElement1 = paramElementChange.getChildrenRemoved();
/* 502 */       Element[] arrayOfElement2 = paramElementChange.getChildrenAdded();
/* 503 */       Element localElement = getElement();
/* 504 */       int i = localElement.getElementIndex(getStartOffset());
/* 505 */       int j = localElement.getElementIndex(getEndOffset() - 1);
/* 506 */       int k = paramElementChange.getIndex();
/* 507 */       if ((k >= i) && (k <= j))
/*     */       {
/* 509 */         int m = k - i;
/* 510 */         int n = Math.min(j - i + 1, arrayOfElement2.length);
/* 511 */         int i1 = Math.min(j - i + 1, arrayOfElement1.length);
/* 512 */         View[] arrayOfView = new View[n];
/* 513 */         for (int i2 = 0; i2 < n; i2++) {
/* 514 */           arrayOfView[i2] = paramViewFactory.create(arrayOfElement2[i2]);
/*     */         }
/* 516 */         replace(m, i1, arrayOfView);
/*     */       }
/* 518 */       return true;
/*     */     }
/*     */ 
/*     */     public AttributeSet getAttributes()
/*     */     {
/* 529 */       return ZoneView.this.getAttributes();
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics, Shape paramShape)
/*     */     {
/* 542 */       load();
/* 543 */       super.paint(paramGraphics, paramShape);
/*     */     }
/*     */ 
/*     */     public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */     {
/* 560 */       load();
/* 561 */       return super.viewToModel(paramFloat1, paramFloat2, paramShape, paramArrayOfBias);
/*     */     }
/*     */ 
/*     */     public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */       throws BadLocationException
/*     */     {
/* 579 */       load();
/* 580 */       return super.modelToView(paramInt, paramShape, paramBias);
/*     */     }
/*     */ 
/*     */     public int getStartOffset()
/*     */     {
/* 589 */       return this.start.getOffset();
/*     */     }
/*     */ 
/*     */     public int getEndOffset()
/*     */     {
/* 596 */       return this.end.getOffset();
/*     */     }
/*     */ 
/*     */     public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */     {
/* 611 */       if (isLoaded())
/* 612 */         super.insertUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*     */     }
/*     */ 
/*     */     public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */     {
/* 628 */       if (isLoaded())
/* 629 */         super.removeUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*     */     }
/*     */ 
/*     */     public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */     {
/* 645 */       if (isLoaded())
/* 646 */         super.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.ZoneView
 * JD-Core Version:    0.6.2
 */
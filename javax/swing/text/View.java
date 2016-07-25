/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import javax.swing.SwingConstants;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentEvent.ElementChange;
/*      */ import javax.swing.event.DocumentEvent.EventType;
/*      */ 
/*      */ public abstract class View
/*      */   implements SwingConstants
/*      */ {
/*      */   public static final int BadBreakWeight = 0;
/*      */   public static final int GoodBreakWeight = 1000;
/*      */   public static final int ExcellentBreakWeight = 2000;
/*      */   public static final int ForcedBreakWeight = 3000;
/*      */   public static final int X_AXIS = 0;
/*      */   public static final int Y_AXIS = 1;
/* 1351 */   static final Position.Bias[] sharedBiasReturn = new Position.Bias[1];
/*      */   private View parent;
/*      */   private Element elem;
/*      */   int firstUpdateIndex;
/*      */   int lastUpdateIndex;
/*      */ 
/*      */   public View(Element paramElement)
/*      */   {
/*  200 */     this.elem = paramElement;
/*      */   }
/*      */ 
/*      */   public View getParent()
/*      */   {
/*  209 */     return this.parent;
/*      */   }
/*      */ 
/*      */   public boolean isVisible()
/*      */   {
/*  220 */     return true;
/*      */   }
/*      */ 
/*      */   public abstract float getPreferredSpan(int paramInt);
/*      */ 
/*      */   public float getMinimumSpan(int paramInt)
/*      */   {
/*  248 */     int i = getResizeWeight(paramInt);
/*  249 */     if (i == 0)
/*      */     {
/*  251 */       return getPreferredSpan(paramInt);
/*      */     }
/*  253 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   public float getMaximumSpan(int paramInt)
/*      */   {
/*  266 */     int i = getResizeWeight(paramInt);
/*  267 */     if (i == 0)
/*      */     {
/*  269 */       return getPreferredSpan(paramInt);
/*      */     }
/*  271 */     return 2.147484E+009F;
/*      */   }
/*      */ 
/*      */   public void preferenceChanged(View paramView, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  287 */     View localView = getParent();
/*  288 */     if (localView != null)
/*  289 */       localView.preferenceChanged(this, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public float getAlignment(int paramInt)
/*      */   {
/*  306 */     return 0.5F;
/*      */   }
/*      */ 
/*      */   public abstract void paint(Graphics paramGraphics, Shape paramShape);
/*      */ 
/*      */   public void setParent(View paramView)
/*      */   {
/*  337 */     if (paramView == null) {
/*  338 */       for (int i = 0; i < getViewCount(); i++) {
/*  339 */         if (getView(i).getParent() == this)
/*      */         {
/*  342 */           getView(i).setParent(null);
/*      */         }
/*      */       }
/*      */     }
/*  346 */     this.parent = paramView;
/*      */   }
/*      */ 
/*      */   public int getViewCount()
/*      */   {
/*  358 */     return 0;
/*      */   }
/*      */ 
/*      */   public View getView(int paramInt)
/*      */   {
/*  369 */     return null;
/*      */   }
/*      */ 
/*      */   public void removeAll()
/*      */   {
/*  380 */     replace(0, getViewCount(), null);
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt)
/*      */   {
/*  389 */     replace(paramInt, 1, null);
/*      */   }
/*      */ 
/*      */   public void insert(int paramInt, View paramView)
/*      */   {
/*  402 */     View[] arrayOfView = new View[1];
/*  403 */     arrayOfView[0] = paramView;
/*  404 */     replace(paramInt, 0, arrayOfView);
/*      */   }
/*      */ 
/*      */   public void append(View paramView)
/*      */   {
/*  416 */     View[] arrayOfView = new View[1];
/*  417 */     arrayOfView[0] = paramView;
/*  418 */     replace(getViewCount(), 0, arrayOfView);
/*      */   }
/*      */ 
/*      */   public void replace(int paramInt1, int paramInt2, View[] paramArrayOfView)
/*      */   {
/*      */   }
/*      */ 
/*      */   public int getViewIndex(int paramInt, Position.Bias paramBias)
/*      */   {
/*  454 */     return -1;
/*      */   }
/*      */ 
/*      */   public Shape getChildAllocation(int paramInt, Shape paramShape)
/*      */   {
/*  470 */     return null;
/*      */   }
/*      */ 
/*      */   public int getNextVisualPositionFrom(int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*      */     throws BadLocationException
/*      */   {
/*  500 */     if (paramInt1 < -1)
/*      */     {
/*  502 */       throw new BadLocationException("Invalid position", paramInt1);
/*      */     }
/*      */ 
/*  505 */     paramArrayOfBias[0] = Position.Bias.Forward;
/*  506 */     switch (paramInt2)
/*      */     {
/*      */     case 1:
/*      */     case 5:
/*  510 */       if (paramInt1 == -1) {
/*  511 */         paramInt1 = paramInt2 == 1 ? Math.max(0, getEndOffset() - 1) : getStartOffset();
/*      */       }
/*      */       else
/*      */       {
/*  515 */         JTextComponent localJTextComponent = (JTextComponent)getContainer();
/*  516 */         Object localObject = localJTextComponent != null ? localJTextComponent.getCaret() : null;
/*      */         Point localPoint;
/*  520 */         if (localObject != null) {
/*  521 */           localPoint = localObject.getMagicCaretPosition();
/*      */         }
/*      */         else
/*  524 */           localPoint = null;
/*      */         int i;
/*  527 */         if (localPoint == null) {
/*  528 */           Rectangle localRectangle = localJTextComponent.modelToView(paramInt1);
/*  529 */           i = localRectangle == null ? 0 : localRectangle.x;
/*      */         }
/*      */         else {
/*  532 */           i = localPoint.x;
/*      */         }
/*  534 */         if (paramInt2 == 1) {
/*  535 */           paramInt1 = Utilities.getPositionAbove(localJTextComponent, paramInt1, i);
/*      */         }
/*      */         else {
/*  538 */           paramInt1 = Utilities.getPositionBelow(localJTextComponent, paramInt1, i);
/*      */         }
/*      */       }
/*  541 */       break;
/*      */     case 7:
/*  543 */       if (paramInt1 == -1) {
/*  544 */         paramInt1 = Math.max(0, getEndOffset() - 1);
/*      */       }
/*      */       else {
/*  547 */         paramInt1 = Math.max(0, paramInt1 - 1);
/*      */       }
/*  549 */       break;
/*      */     case 3:
/*  551 */       if (paramInt1 == -1) {
/*  552 */         paramInt1 = getStartOffset();
/*      */       }
/*      */       else {
/*  555 */         paramInt1 = Math.min(paramInt1 + 1, getDocument().getLength());
/*      */       }
/*  557 */       break;
/*      */     case 2:
/*      */     case 4:
/*      */     case 6:
/*      */     default:
/*  559 */       throw new IllegalArgumentException("Bad direction: " + paramInt2);
/*      */     }
/*  561 */     return paramInt1;
/*      */   }
/*      */ 
/*      */   public abstract Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*      */     throws BadLocationException;
/*      */ 
/*      */   public Shape modelToView(int paramInt1, Position.Bias paramBias1, int paramInt2, Position.Bias paramBias2, Shape paramShape)
/*      */     throws BadLocationException
/*      */   {
/*  619 */     Shape localShape = modelToView(paramInt1, paramShape, paramBias1);
/*      */     Object localObject;
/*  621 */     if (paramInt2 == getEndOffset()) {
/*      */       try {
/*  623 */         localObject = modelToView(paramInt2, paramShape, paramBias2);
/*      */       } catch (BadLocationException localBadLocationException) {
/*  625 */         localObject = null;
/*      */       }
/*  627 */       if (localObject == null)
/*      */       {
/*  629 */         localRectangle1 = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*      */ 
/*  631 */         localObject = new Rectangle(localRectangle1.x + localRectangle1.width - 1, localRectangle1.y, 1, localRectangle1.height);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  636 */       localObject = modelToView(paramInt2, paramShape, paramBias2);
/*      */     }
/*  638 */     Rectangle localRectangle1 = localShape.getBounds();
/*  639 */     Rectangle localRectangle2 = (localObject instanceof Rectangle) ? (Rectangle)localObject : ((Shape)localObject).getBounds();
/*      */ 
/*  641 */     if (localRectangle1.y != localRectangle2.y)
/*      */     {
/*  643 */       Rectangle localRectangle3 = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*      */ 
/*  645 */       localRectangle1.x = localRectangle3.x;
/*  646 */       localRectangle1.width = localRectangle3.width;
/*      */     }
/*  648 */     localRectangle1.add(localRectangle2);
/*  649 */     return localRectangle1;
/*      */   }
/*      */ 
/*      */   public abstract int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias);
/*      */ 
/*      */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  696 */     if (getViewCount() > 0) {
/*  697 */       Element localElement = getElement();
/*  698 */       DocumentEvent.ElementChange localElementChange = paramDocumentEvent.getChange(localElement);
/*  699 */       if ((localElementChange != null) && 
/*  700 */         (!updateChildren(localElementChange, paramDocumentEvent, paramViewFactory)))
/*      */       {
/*  703 */         localElementChange = null;
/*      */       }
/*      */ 
/*  706 */       forwardUpdate(localElementChange, paramDocumentEvent, paramShape, paramViewFactory);
/*  707 */       updateLayout(localElementChange, paramDocumentEvent, paramShape);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  737 */     if (getViewCount() > 0) {
/*  738 */       Element localElement = getElement();
/*  739 */       DocumentEvent.ElementChange localElementChange = paramDocumentEvent.getChange(localElement);
/*  740 */       if ((localElementChange != null) && 
/*  741 */         (!updateChildren(localElementChange, paramDocumentEvent, paramViewFactory)))
/*      */       {
/*  744 */         localElementChange = null;
/*      */       }
/*      */ 
/*  747 */       forwardUpdate(localElementChange, paramDocumentEvent, paramShape, paramViewFactory);
/*  748 */       updateLayout(localElementChange, paramDocumentEvent, paramShape);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  778 */     if (getViewCount() > 0) {
/*  779 */       Element localElement = getElement();
/*  780 */       DocumentEvent.ElementChange localElementChange = paramDocumentEvent.getChange(localElement);
/*  781 */       if ((localElementChange != null) && 
/*  782 */         (!updateChildren(localElementChange, paramDocumentEvent, paramViewFactory)))
/*      */       {
/*  785 */         localElementChange = null;
/*      */       }
/*      */ 
/*  788 */       forwardUpdate(localElementChange, paramDocumentEvent, paramShape, paramViewFactory);
/*  789 */       updateLayout(localElementChange, paramDocumentEvent, paramShape);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Document getDocument()
/*      */   {
/*  800 */     return this.elem.getDocument();
/*      */   }
/*      */ 
/*      */   public int getStartOffset()
/*      */   {
/*  811 */     return this.elem.getStartOffset();
/*      */   }
/*      */ 
/*      */   public int getEndOffset()
/*      */   {
/*  822 */     return this.elem.getEndOffset();
/*      */   }
/*      */ 
/*      */   public Element getElement()
/*      */   {
/*  834 */     return this.elem;
/*      */   }
/*      */ 
/*      */   public Graphics getGraphics()
/*      */   {
/*  848 */     Container localContainer = getContainer();
/*  849 */     return localContainer.getGraphics();
/*      */   }
/*      */ 
/*      */   public AttributeSet getAttributes()
/*      */   {
/*  866 */     return this.elem.getAttributes();
/*      */   }
/*      */ 
/*      */   public View breakView(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
/*      */   {
/*  902 */     return this;
/*      */   }
/*      */ 
/*      */   public View createFragment(int paramInt1, int paramInt2)
/*      */   {
/*  923 */     return this;
/*      */   }
/*      */ 
/*      */   public int getBreakWeight(int paramInt, float paramFloat1, float paramFloat2)
/*      */   {
/*  966 */     if (paramFloat2 > getPreferredSpan(paramInt)) {
/*  967 */       return 1000;
/*      */     }
/*  969 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getResizeWeight(int paramInt)
/*      */   {
/*  981 */     return 0;
/*      */   }
/*      */ 
/*      */   public void setSize(float paramFloat1, float paramFloat2)
/*      */   {
/*      */   }
/*      */ 
/*      */   public Container getContainer()
/*      */   {
/* 1004 */     View localView = getParent();
/* 1005 */     return localView != null ? localView.getContainer() : null;
/*      */   }
/*      */ 
/*      */   public ViewFactory getViewFactory()
/*      */   {
/* 1018 */     View localView = getParent();
/* 1019 */     return localView != null ? localView.getViewFactory() : null;
/*      */   }
/*      */ 
/*      */   public String getToolTipText(float paramFloat1, float paramFloat2, Shape paramShape)
/*      */   {
/* 1031 */     int i = getViewIndex(paramFloat1, paramFloat2, paramShape);
/* 1032 */     if (i >= 0) {
/* 1033 */       paramShape = getChildAllocation(i, paramShape);
/* 1034 */       Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*      */ 
/* 1036 */       if (localRectangle.contains(paramFloat1, paramFloat2)) {
/* 1037 */         return getView(i).getToolTipText(paramFloat1, paramFloat2, paramShape);
/*      */       }
/*      */     }
/* 1040 */     return null;
/*      */   }
/*      */ 
/*      */   public int getViewIndex(float paramFloat1, float paramFloat2, Shape paramShape)
/*      */   {
/* 1056 */     for (int i = getViewCount() - 1; i >= 0; i--) {
/* 1057 */       Shape localShape = getChildAllocation(i, paramShape);
/*      */ 
/* 1059 */       if (localShape != null) {
/* 1060 */         Rectangle localRectangle = (localShape instanceof Rectangle) ? (Rectangle)localShape : localShape.getBounds();
/*      */ 
/* 1063 */         if (localRectangle.contains(paramFloat1, paramFloat2)) {
/* 1064 */           return i;
/*      */         }
/*      */       }
/*      */     }
/* 1068 */     return -1;
/*      */   }
/*      */ 
/*      */   protected boolean updateChildren(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, ViewFactory paramViewFactory)
/*      */   {
/* 1102 */     Element[] arrayOfElement1 = paramElementChange.getChildrenRemoved();
/* 1103 */     Element[] arrayOfElement2 = paramElementChange.getChildrenAdded();
/* 1104 */     View[] arrayOfView = null;
/* 1105 */     if (arrayOfElement2 != null) {
/* 1106 */       arrayOfView = new View[arrayOfElement2.length];
/* 1107 */       for (i = 0; i < arrayOfElement2.length; i++) {
/* 1108 */         arrayOfView[i] = paramViewFactory.create(arrayOfElement2[i]);
/*      */       }
/*      */     }
/* 1111 */     int i = 0;
/* 1112 */     int j = paramElementChange.getIndex();
/* 1113 */     if (arrayOfElement1 != null) {
/* 1114 */       i = arrayOfElement1.length;
/*      */     }
/* 1116 */     replace(j, i, arrayOfView);
/* 1117 */     return true;
/*      */   }
/*      */ 
/*      */   protected void forwardUpdate(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/* 1140 */     calculateUpdateIndexes(paramDocumentEvent);
/*      */ 
/* 1142 */     int i = this.lastUpdateIndex + 1;
/* 1143 */     int j = i;
/* 1144 */     Object localObject = paramElementChange != null ? paramElementChange.getChildrenAdded() : null;
/* 1145 */     if ((localObject != null) && (localObject.length > 0)) {
/* 1146 */       i = paramElementChange.getIndex();
/* 1147 */       j = i + localObject.length - 1;
/*      */     }
/*      */ 
/* 1153 */     for (int k = this.firstUpdateIndex; k <= this.lastUpdateIndex; k++)
/* 1154 */       if ((k < i) || (k > j)) {
/* 1155 */         View localView = getView(k);
/* 1156 */         if (localView != null) {
/* 1157 */           Shape localShape = getChildAllocation(k, paramShape);
/* 1158 */           forwardUpdateToView(localView, paramDocumentEvent, localShape, paramViewFactory);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   void calculateUpdateIndexes(DocumentEvent paramDocumentEvent)
/*      */   {
/* 1170 */     int i = paramDocumentEvent.getOffset();
/* 1171 */     this.firstUpdateIndex = getViewIndex(i, Position.Bias.Forward);
/* 1172 */     if ((this.firstUpdateIndex == -1) && (paramDocumentEvent.getType() == DocumentEvent.EventType.REMOVE) && (i >= getEndOffset()))
/*      */     {
/* 1177 */       this.firstUpdateIndex = (getViewCount() - 1);
/*      */     }
/* 1179 */     this.lastUpdateIndex = this.firstUpdateIndex;
/* 1180 */     Object localObject = this.firstUpdateIndex >= 0 ? getView(this.firstUpdateIndex) : null;
/* 1181 */     if ((localObject != null) && 
/* 1182 */       (localObject.getStartOffset() == i) && (i > 0))
/*      */     {
/* 1185 */       this.firstUpdateIndex = Math.max(this.firstUpdateIndex - 1, 0);
/*      */     }
/*      */ 
/* 1188 */     if (paramDocumentEvent.getType() != DocumentEvent.EventType.REMOVE) {
/* 1189 */       this.lastUpdateIndex = getViewIndex(i + paramDocumentEvent.getLength(), Position.Bias.Forward);
/* 1190 */       if (this.lastUpdateIndex < 0) {
/* 1191 */         this.lastUpdateIndex = (getViewCount() - 1);
/*      */       }
/*      */     }
/* 1194 */     this.firstUpdateIndex = Math.max(this.firstUpdateIndex, 0);
/*      */   }
/*      */ 
/*      */   protected void forwardUpdateToView(View paramView, DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/* 1214 */     DocumentEvent.EventType localEventType = paramDocumentEvent.getType();
/* 1215 */     if (localEventType == DocumentEvent.EventType.INSERT)
/* 1216 */       paramView.insertUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/* 1217 */     else if (localEventType == DocumentEvent.EventType.REMOVE)
/* 1218 */       paramView.removeUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*      */     else
/* 1220 */       paramView.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*      */   }
/*      */ 
/*      */   protected void updateLayout(DocumentEvent.ElementChange paramElementChange, DocumentEvent paramDocumentEvent, Shape paramShape)
/*      */   {
/* 1241 */     if ((paramElementChange != null) && (paramShape != null))
/*      */     {
/* 1243 */       preferenceChanged(null, true, true);
/* 1244 */       Container localContainer = getContainer();
/* 1245 */       if (localContainer != null)
/* 1246 */         localContainer.repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Shape modelToView(int paramInt, Shape paramShape)
/*      */     throws BadLocationException
/*      */   {
/* 1327 */     return modelToView(paramInt, paramShape, Position.Bias.Forward);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape)
/*      */   {
/* 1345 */     sharedBiasReturn[0] = Position.Bias.Forward;
/* 1346 */     return viewToModel(paramFloat1, paramFloat2, paramShape, sharedBiasReturn);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.View
 * JD-Core Version:    0.6.2
 */
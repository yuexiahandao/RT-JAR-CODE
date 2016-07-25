/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.AlphaComposite;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Composite;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.peer.ComponentPeer;
/*      */ import java.beans.Transient;
/*      */ import java.io.Serializable;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.ViewportUI;
/*      */ 
/*      */ public class JViewport extends JComponent
/*      */   implements Accessible
/*      */ {
/*      */   private static final String uiClassID = "ViewportUI";
/*  113 */   static final Object EnableWindowBlit = "EnableWindowBlit";
/*      */ 
/*  119 */   protected boolean isViewSizeSet = false;
/*      */ 
/*  125 */   protected Point lastPaintPosition = null;
/*      */ 
/*      */   @Deprecated
/*  136 */   protected boolean backingStore = false;
/*      */ 
/*  140 */   protected transient Image backingStoreImage = null;
/*      */ 
/*  158 */   protected boolean scrollUnderway = false;
/*      */ 
/*  163 */   private ComponentListener viewListener = null;
/*      */ 
/*  170 */   private transient ChangeEvent changeEvent = null;
/*      */   public static final int BLIT_SCROLL_MODE = 1;
/*      */   public static final int BACKINGSTORE_SCROLL_MODE = 2;
/*      */   public static final int SIMPLE_SCROLL_MODE = 0;
/*  208 */   private int scrollMode = 1;
/*      */   private transient boolean repaintAll;
/*      */   private transient boolean waitingForRepaint;
/*      */   private transient Timer repaintTimer;
/*      */   private transient boolean inBlitPaint;
/*      */   private boolean hasHadValidView;
/*      */   private boolean viewChanged;
/*      */ 
/*      */   public JViewport()
/*      */   {
/*  277 */     setLayout(createLayoutManager());
/*  278 */     setOpaque(true);
/*  279 */     updateUI();
/*  280 */     setInheritsPopupMenu(true);
/*      */   }
/*      */ 
/*      */   public ViewportUI getUI()
/*      */   {
/*  292 */     return (ViewportUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(ViewportUI paramViewportUI)
/*      */   {
/*  309 */     super.setUI(paramViewportUI);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  319 */     setUI((ViewportUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  333 */     return "ViewportUI";
/*      */   }
/*      */ 
/*      */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*      */   {
/*  350 */     setView(paramComponent);
/*      */   }
/*      */ 
/*      */   public void remove(Component paramComponent)
/*      */   {
/*  360 */     paramComponent.removeComponentListener(this.viewListener);
/*  361 */     super.remove(paramComponent);
/*      */   }
/*      */ 
/*      */   public void scrollRectToVisible(Rectangle paramRectangle)
/*      */   {
/*  387 */     Component localComponent = getView();
/*      */ 
/*  389 */     if (localComponent == null) {
/*  390 */       return;
/*      */     }
/*  392 */     if (!localComponent.isValid())
/*      */     {
/*  396 */       validateView();
/*      */     }
/*      */ 
/*  400 */     int i = positionAdjustment(getWidth(), paramRectangle.width, paramRectangle.x);
/*  401 */     int j = positionAdjustment(getHeight(), paramRectangle.height, paramRectangle.y);
/*      */ 
/*  403 */     if ((i != 0) || (j != 0)) {
/*  404 */       Point localPoint = getViewPosition();
/*  405 */       Dimension localDimension1 = localComponent.getSize();
/*  406 */       int k = localPoint.x;
/*  407 */       int m = localPoint.y;
/*  408 */       Dimension localDimension2 = getExtentSize();
/*      */ 
/*  410 */       localPoint.x -= i;
/*  411 */       localPoint.y -= j;
/*      */ 
/*  416 */       if (localComponent.isValid()) {
/*  417 */         if (getParent().getComponentOrientation().isLeftToRight()) {
/*  418 */           if (localPoint.x + localDimension2.width > localDimension1.width)
/*  419 */             localPoint.x = Math.max(0, localDimension1.width - localDimension2.width);
/*  420 */           else if (localPoint.x < 0) {
/*  421 */             localPoint.x = 0;
/*      */           }
/*      */         }
/*  424 */         else if (localDimension2.width > localDimension1.width)
/*  425 */           localPoint.x = (localDimension1.width - localDimension2.width);
/*      */         else {
/*  427 */           localPoint.x = Math.max(0, Math.min(localDimension1.width - localDimension2.width, localPoint.x));
/*      */         }
/*      */ 
/*  430 */         if (localPoint.y + localDimension2.height > localDimension1.height) {
/*  431 */           localPoint.y = Math.max(0, localDimension1.height - localDimension2.height);
/*      */         }
/*  434 */         else if (localPoint.y < 0) {
/*  435 */           localPoint.y = 0;
/*      */         }
/*      */       }
/*  438 */       if ((localPoint.x != k) || (localPoint.y != m)) {
/*  439 */         setViewPosition(localPoint);
/*      */ 
/*  461 */         this.scrollUnderway = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void validateView()
/*      */   {
/*  478 */     Container localContainer = SwingUtilities.getValidateRoot(this, false);
/*      */ 
/*  480 */     if (localContainer == null) {
/*  481 */       return;
/*      */     }
/*      */ 
/*  485 */     localContainer.validate();
/*      */ 
/*  489 */     RepaintManager localRepaintManager = RepaintManager.currentManager(this);
/*      */ 
/*  491 */     if (localRepaintManager != null)
/*  492 */       localRepaintManager.removeInvalidComponent((JComponent)localContainer);
/*      */   }
/*      */ 
/*      */   private int positionAdjustment(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  506 */     if ((paramInt3 >= 0) && (paramInt2 + paramInt3 <= paramInt1)) {
/*  507 */       return 0;
/*      */     }
/*      */ 
/*  513 */     if ((paramInt3 <= 0) && (paramInt2 + paramInt3 >= paramInt1)) {
/*  514 */       return 0;
/*      */     }
/*      */ 
/*  520 */     if ((paramInt3 > 0) && (paramInt2 <= paramInt1)) {
/*  521 */       return -paramInt3 + paramInt1 - paramInt2;
/*      */     }
/*      */ 
/*  527 */     if ((paramInt3 >= 0) && (paramInt2 >= paramInt1)) {
/*  528 */       return -paramInt3;
/*      */     }
/*      */ 
/*  534 */     if ((paramInt3 <= 0) && (paramInt2 <= paramInt1)) {
/*  535 */       return -paramInt3;
/*      */     }
/*      */ 
/*  541 */     if ((paramInt3 < 0) && (paramInt2 >= paramInt1)) {
/*  542 */       return -paramInt3 + paramInt1 - paramInt2;
/*      */     }
/*      */ 
/*  545 */     return 0;
/*      */   }
/*      */ 
/*      */   public final void setBorder(Border paramBorder)
/*      */   {
/*  565 */     if (paramBorder != null)
/*  566 */       throw new IllegalArgumentException("JViewport.setBorder() not supported");
/*      */   }
/*      */ 
/*      */   public final Insets getInsets()
/*      */   {
/*  579 */     return new Insets(0, 0, 0, 0);
/*      */   }
/*      */ 
/*      */   public final Insets getInsets(Insets paramInsets)
/*      */   {
/*  595 */     paramInsets.left = (paramInsets.top = paramInsets.right = paramInsets.bottom = 0);
/*  596 */     return paramInsets;
/*      */   }
/*      */ 
/*      */   private Graphics getBackingStoreGraphics(Graphics paramGraphics)
/*      */   {
/*  601 */     Graphics localGraphics = this.backingStoreImage.getGraphics();
/*  602 */     localGraphics.setColor(paramGraphics.getColor());
/*  603 */     localGraphics.setFont(paramGraphics.getFont());
/*  604 */     localGraphics.setClip(paramGraphics.getClipBounds());
/*  605 */     return localGraphics;
/*      */   }
/*      */ 
/*      */   private void paintViaBackingStore(Graphics paramGraphics)
/*      */   {
/*  610 */     Graphics localGraphics = getBackingStoreGraphics(paramGraphics);
/*      */     try {
/*  612 */       super.paint(localGraphics);
/*  613 */       paramGraphics.drawImage(this.backingStoreImage, 0, 0, this);
/*      */     } finally {
/*  615 */       localGraphics.dispose();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintViaBackingStore(Graphics paramGraphics, Rectangle paramRectangle) {
/*  620 */     Graphics localGraphics = getBackingStoreGraphics(paramGraphics);
/*      */     try {
/*  622 */       super.paint(localGraphics);
/*  623 */       paramGraphics.setClip(paramRectangle);
/*  624 */       paramGraphics.drawImage(this.backingStoreImage, 0, 0, this);
/*      */     } finally {
/*  626 */       localGraphics.dispose();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isOptimizedDrawingEnabled()
/*      */   {
/*  642 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean isPaintingOrigin()
/*      */   {
/*  654 */     return this.scrollMode == 2;
/*      */   }
/*      */ 
/*      */   private Point getViewLocation()
/*      */   {
/*  662 */     Component localComponent = getView();
/*  663 */     if (localComponent != null) {
/*  664 */       return localComponent.getLocation();
/*      */     }
/*      */ 
/*  667 */     return new Point(0, 0);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics)
/*      */   {
/*  686 */     int i = getWidth();
/*  687 */     int j = getHeight();
/*      */ 
/*  689 */     if ((i <= 0) || (j <= 0)) {
/*  690 */       return;
/*      */     }
/*      */ 
/*  693 */     if (this.inBlitPaint)
/*      */     {
/*  695 */       super.paint(paramGraphics);
/*  696 */       return;
/*      */     }
/*      */ 
/*  699 */     if (this.repaintAll) {
/*  700 */       this.repaintAll = false;
/*  701 */       localRectangle1 = paramGraphics.getClipBounds();
/*  702 */       if ((localRectangle1.width < getWidth()) || (localRectangle1.height < getHeight()))
/*      */       {
/*  704 */         this.waitingForRepaint = true;
/*  705 */         if (this.repaintTimer == null) {
/*  706 */           this.repaintTimer = createRepaintTimer();
/*      */         }
/*  708 */         this.repaintTimer.stop();
/*  709 */         this.repaintTimer.start();
/*      */       }
/*      */       else
/*      */       {
/*  714 */         if (this.repaintTimer != null) {
/*  715 */           this.repaintTimer.stop();
/*      */         }
/*  717 */         this.waitingForRepaint = false;
/*      */       }
/*      */     }
/*  720 */     else if (this.waitingForRepaint)
/*      */     {
/*  722 */       localRectangle1 = paramGraphics.getClipBounds();
/*  723 */       if ((localRectangle1.width >= getWidth()) && (localRectangle1.height >= getHeight()))
/*      */       {
/*  725 */         this.waitingForRepaint = false;
/*  726 */         this.repaintTimer.stop();
/*      */       }
/*      */     }
/*      */ 
/*  730 */     if ((!this.backingStore) || (isBlitting()) || (getView() == null)) {
/*  731 */       super.paint(paramGraphics);
/*  732 */       this.lastPaintPosition = getViewLocation();
/*  733 */       return;
/*      */     }
/*      */ 
/*  740 */     Rectangle localRectangle1 = getView().getBounds();
/*  741 */     if (!isOpaque())
/*  742 */       paramGraphics.clipRect(0, 0, localRectangle1.width, localRectangle1.height);
/*      */     Object localObject1;
/*  745 */     if (this.backingStoreImage == null)
/*      */     {
/*  753 */       this.backingStoreImage = createImage(i, j);
/*  754 */       localObject1 = paramGraphics.getClipBounds();
/*  755 */       if ((((Rectangle)localObject1).width != i) || (((Rectangle)localObject1).height != j)) {
/*  756 */         if (!isOpaque()) {
/*  757 */           paramGraphics.setClip(0, 0, Math.min(localRectangle1.width, i), Math.min(localRectangle1.height, j));
/*      */         }
/*      */         else
/*      */         {
/*  761 */           paramGraphics.setClip(0, 0, i, j);
/*      */         }
/*  763 */         paintViaBackingStore(paramGraphics, (Rectangle)localObject1);
/*      */       }
/*      */       else {
/*  766 */         paintViaBackingStore(paramGraphics);
/*      */       }
/*      */ 
/*      */     }
/*  770 */     else if ((!this.scrollUnderway) || (this.lastPaintPosition.equals(getViewLocation())))
/*      */     {
/*  772 */       paintViaBackingStore(paramGraphics);
/*      */     }
/*      */     else {
/*  775 */       localObject1 = new Point();
/*  776 */       Point localPoint1 = new Point();
/*  777 */       Dimension localDimension = new Dimension();
/*  778 */       Rectangle localRectangle2 = new Rectangle();
/*      */ 
/*  780 */       Point localPoint2 = getViewLocation();
/*  781 */       int k = localPoint2.x - this.lastPaintPosition.x;
/*  782 */       int m = localPoint2.y - this.lastPaintPosition.y;
/*  783 */       boolean bool = computeBlit(k, m, (Point)localObject1, localPoint1, localDimension, localRectangle2);
/*  784 */       if (!bool)
/*      */       {
/*  787 */         paintViaBackingStore(paramGraphics);
/*      */       } else {
/*  789 */         int n = localPoint1.x - ((Point)localObject1).x;
/*  790 */         int i1 = localPoint1.y - ((Point)localObject1).y;
/*      */ 
/*  793 */         Rectangle localRectangle3 = paramGraphics.getClipBounds();
/*      */ 
/*  798 */         paramGraphics.setClip(0, 0, i, j);
/*  799 */         Graphics localGraphics = getBackingStoreGraphics(paramGraphics);
/*      */         try {
/*  801 */           localGraphics.copyArea(((Point)localObject1).x, ((Point)localObject1).y, localDimension.width, localDimension.height, n, i1);
/*      */ 
/*  803 */           paramGraphics.setClip(localRectangle3.x, localRectangle3.y, localRectangle3.width, localRectangle3.height);
/*      */ 
/*  805 */           Rectangle localRectangle4 = localRectangle1.intersection(localRectangle2);
/*  806 */           localGraphics.setClip(localRectangle4);
/*  807 */           super.paint(localGraphics);
/*      */ 
/*  810 */           paramGraphics.drawImage(this.backingStoreImage, 0, 0, this);
/*      */         } finally {
/*  812 */           localGraphics.dispose();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  817 */     this.lastPaintPosition = getViewLocation();
/*  818 */     this.scrollUnderway = false;
/*      */   }
/*      */ 
/*      */   public void reshape(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  834 */     int i = (getWidth() != paramInt3) || (getHeight() != paramInt4) ? 1 : 0;
/*  835 */     if (i != 0) {
/*  836 */       this.backingStoreImage = null;
/*      */     }
/*  838 */     super.reshape(paramInt1, paramInt2, paramInt3, paramInt4);
/*  839 */     if ((i != 0) || (this.viewChanged)) {
/*  840 */       this.viewChanged = false;
/*      */ 
/*  842 */       fireStateChanged();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setScrollMode(int paramInt)
/*      */   {
/*  873 */     this.scrollMode = paramInt;
/*  874 */     this.backingStore = (paramInt == 2);
/*      */   }
/*      */ 
/*      */   public int getScrollMode()
/*      */   {
/*  885 */     return this.scrollMode;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean isBackingStoreEnabled()
/*      */   {
/*  900 */     return this.scrollMode == 2;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void setBackingStoreEnabled(boolean paramBoolean)
/*      */   {
/*  918 */     if (paramBoolean)
/*  919 */       setScrollMode(2);
/*      */     else
/*  921 */       setScrollMode(1);
/*      */   }
/*      */ 
/*      */   private boolean isBlitting()
/*      */   {
/*  926 */     Component localComponent = getView();
/*  927 */     return (this.scrollMode == 1) && ((localComponent instanceof JComponent)) && (localComponent.isOpaque());
/*      */   }
/*      */ 
/*      */   public Component getView()
/*      */   {
/*  940 */     return getComponentCount() > 0 ? getComponent(0) : null;
/*      */   }
/*      */ 
/*      */   public void setView(Component paramComponent)
/*      */   {
/*  957 */     int i = getComponentCount();
/*  958 */     for (int j = i - 1; j >= 0; j--) {
/*  959 */       remove(getComponent(j));
/*      */     }
/*      */ 
/*  962 */     this.isViewSizeSet = false;
/*      */ 
/*  964 */     if (paramComponent != null) {
/*  965 */       super.addImpl(paramComponent, null, -1);
/*  966 */       this.viewListener = createViewListener();
/*  967 */       paramComponent.addComponentListener(this.viewListener);
/*      */     }
/*      */ 
/*  970 */     if (this.hasHadValidView)
/*      */     {
/*  972 */       fireStateChanged();
/*      */     }
/*  974 */     else if (paramComponent != null) {
/*  975 */       this.hasHadValidView = true;
/*      */     }
/*      */ 
/*  978 */     this.viewChanged = true;
/*      */ 
/*  980 */     revalidate();
/*  981 */     repaint();
/*      */   }
/*      */ 
/*      */   public Dimension getViewSize()
/*      */   {
/*  993 */     Component localComponent = getView();
/*      */ 
/*  995 */     if (localComponent == null) {
/*  996 */       return new Dimension(0, 0);
/*      */     }
/*  998 */     if (this.isViewSizeSet) {
/*  999 */       return localComponent.getSize();
/*      */     }
/*      */ 
/* 1002 */     return localComponent.getPreferredSize();
/*      */   }
/*      */ 
/*      */   public void setViewSize(Dimension paramDimension)
/*      */   {
/* 1014 */     Component localComponent = getView();
/* 1015 */     if (localComponent != null) {
/* 1016 */       Dimension localDimension = localComponent.getSize();
/* 1017 */       if (!paramDimension.equals(localDimension))
/*      */       {
/* 1021 */         this.scrollUnderway = false;
/* 1022 */         localComponent.setSize(paramDimension);
/* 1023 */         this.isViewSizeSet = true;
/* 1024 */         fireStateChanged();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Point getViewPosition()
/*      */   {
/* 1036 */     Component localComponent = getView();
/* 1037 */     if (localComponent != null) {
/* 1038 */       Point localPoint = localComponent.getLocation();
/* 1039 */       localPoint.x = (-localPoint.x);
/* 1040 */       localPoint.y = (-localPoint.y);
/* 1041 */       return localPoint;
/*      */     }
/*      */ 
/* 1044 */     return new Point(0, 0);
/*      */   }
/*      */ 
/*      */   public void setViewPosition(Point paramPoint)
/*      */   {
/* 1057 */     Component localComponent = getView();
/* 1058 */     if (localComponent == null) {
/* 1059 */       return;
/*      */     }
/*      */ 
/* 1062 */     int k = paramPoint.x; int m = paramPoint.y;
/*      */     Object localObject1;
/*      */     int i;
/*      */     int j;
/* 1068 */     if ((localComponent instanceof JComponent)) {
/* 1069 */       localObject1 = (JComponent)localComponent;
/* 1070 */       i = ((JComponent)localObject1).getX();
/* 1071 */       j = ((JComponent)localObject1).getY();
/*      */     }
/*      */     else {
/* 1074 */       localObject1 = localComponent.getBounds();
/* 1075 */       i = ((Rectangle)localObject1).x;
/* 1076 */       j = ((Rectangle)localObject1).y;
/*      */     }
/*      */ 
/* 1082 */     int n = -k;
/* 1083 */     int i1 = -m;
/*      */ 
/* 1085 */     if ((i != n) || (j != i1)) {
/* 1086 */       if ((!this.waitingForRepaint) && (isBlitting()) && (canUseWindowBlitter())) {
/* 1087 */         RepaintManager localRepaintManager = RepaintManager.currentManager(this);
/*      */ 
/* 1090 */         JComponent localJComponent = (JComponent)localComponent;
/* 1091 */         Rectangle localRectangle = localRepaintManager.getDirtyRegion(localJComponent);
/* 1092 */         if ((localRectangle == null) || (!localRectangle.contains(localJComponent.getVisibleRect()))) {
/* 1093 */           localRepaintManager.beginPaint();
/*      */           try {
/* 1095 */             Graphics localGraphics = JComponent.safelyGetGraphics(this);
/* 1096 */             flushViewDirtyRegion(localGraphics, localRectangle);
/* 1097 */             localComponent.setLocation(n, i1);
/* 1098 */             localGraphics.setClip(0, 0, getWidth(), Math.min(getHeight(), localJComponent.getHeight()));
/*      */ 
/* 1102 */             this.repaintAll = ((windowBlitPaint(localGraphics)) && (needsRepaintAfterBlit()));
/*      */ 
/* 1104 */             localGraphics.dispose();
/* 1105 */             localRepaintManager.markCompletelyClean((JComponent)getParent());
/* 1106 */             localRepaintManager.markCompletelyClean(this);
/* 1107 */             localRepaintManager.markCompletelyClean(localJComponent);
/*      */           } finally {
/* 1109 */             localRepaintManager.endPaint();
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1114 */           localComponent.setLocation(n, i1);
/* 1115 */           this.repaintAll = false;
/*      */         }
/*      */       }
/*      */       else {
/* 1119 */         this.scrollUnderway = true;
/*      */ 
/* 1121 */         localComponent.setLocation(n, i1);
/* 1122 */         this.repaintAll = false;
/*      */       }
/*      */ 
/* 1125 */       revalidate();
/* 1126 */       fireStateChanged();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Rectangle getViewRect()
/*      */   {
/* 1140 */     return new Rectangle(getViewPosition(), getExtentSize());
/*      */   }
/*      */ 
/*      */   protected boolean computeBlit(int paramInt1, int paramInt2, Point paramPoint1, Point paramPoint2, Dimension paramDimension, Rectangle paramRectangle)
/*      */   {
/* 1168 */     int i = Math.abs(paramInt1);
/* 1169 */     int j = Math.abs(paramInt2);
/* 1170 */     Dimension localDimension = getExtentSize();
/*      */ 
/* 1172 */     if ((paramInt1 == 0) && (paramInt2 != 0) && (j < localDimension.height)) {
/* 1173 */       if (paramInt2 < 0) {
/* 1174 */         paramPoint1.y = (-paramInt2);
/* 1175 */         paramPoint2.y = 0;
/* 1176 */         paramRectangle.y = (localDimension.height + paramInt2);
/*      */       }
/*      */       else {
/* 1179 */         paramPoint1.y = 0;
/* 1180 */         paramPoint2.y = paramInt2;
/* 1181 */         paramRectangle.y = 0;
/*      */       }
/*      */ 
/* 1184 */       paramRectangle.x = (paramPoint1.x = paramPoint2.x = 0);
/*      */ 
/* 1186 */       paramDimension.width = localDimension.width;
/* 1187 */       localDimension.height -= j;
/*      */ 
/* 1189 */       paramRectangle.width = localDimension.width;
/* 1190 */       paramRectangle.height = j;
/*      */ 
/* 1192 */       return true;
/*      */     }
/*      */ 
/* 1195 */     if ((paramInt2 == 0) && (paramInt1 != 0) && (i < localDimension.width)) {
/* 1196 */       if (paramInt1 < 0) {
/* 1197 */         paramPoint1.x = (-paramInt1);
/* 1198 */         paramPoint2.x = 0;
/* 1199 */         paramRectangle.x = (localDimension.width + paramInt1);
/*      */       }
/*      */       else {
/* 1202 */         paramPoint1.x = 0;
/* 1203 */         paramPoint2.x = paramInt1;
/* 1204 */         paramRectangle.x = 0;
/*      */       }
/*      */ 
/* 1207 */       paramRectangle.y = (paramPoint1.y = paramPoint2.y = 0);
/*      */ 
/* 1209 */       localDimension.width -= i;
/* 1210 */       paramDimension.height = localDimension.height;
/*      */ 
/* 1212 */       paramRectangle.width = i;
/* 1213 */       paramRectangle.height = localDimension.height;
/*      */ 
/* 1215 */       return true;
/*      */     }
/*      */ 
/* 1219 */     return false;
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Dimension getExtentSize()
/*      */   {
/* 1231 */     return getSize();
/*      */   }
/*      */ 
/*      */   public Dimension toViewCoordinates(Dimension paramDimension)
/*      */   {
/* 1244 */     return new Dimension(paramDimension);
/*      */   }
/*      */ 
/*      */   public Point toViewCoordinates(Point paramPoint)
/*      */   {
/* 1256 */     return new Point(paramPoint);
/*      */   }
/*      */ 
/*      */   public void setExtentSize(Dimension paramDimension)
/*      */   {
/* 1267 */     Dimension localDimension = getExtentSize();
/* 1268 */     if (!paramDimension.equals(localDimension)) {
/* 1269 */       setSize(paramDimension);
/* 1270 */       fireStateChanged();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ViewListener createViewListener()
/*      */   {
/* 1299 */     return new ViewListener();
/*      */   }
/*      */ 
/*      */   protected LayoutManager createLayoutManager()
/*      */   {
/* 1311 */     return ViewportLayout.SHARED_INSTANCE;
/*      */   }
/*      */ 
/*      */   public void addChangeListener(ChangeListener paramChangeListener)
/*      */   {
/* 1327 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public void removeChangeListener(ChangeListener paramChangeListener)
/*      */   {
/* 1339 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public ChangeListener[] getChangeListeners()
/*      */   {
/* 1351 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireStateChanged()
/*      */   {
/* 1364 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 1365 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1366 */       if (arrayOfObject[i] == ChangeListener.class) {
/* 1367 */         if (this.changeEvent == null) {
/* 1368 */           this.changeEvent = new ChangeEvent(this);
/*      */         }
/* 1370 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1387 */     Container localContainer = getParent();
/* 1388 */     if (localContainer != null)
/* 1389 */       localContainer.repaint(paramLong, paramInt1 + getX(), paramInt2 + getY(), paramInt3, paramInt4);
/*      */     else
/* 1391 */       super.repaint(paramLong, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1406 */     String str1 = this.isViewSizeSet ? "true" : "false";
/*      */ 
/* 1408 */     String str2 = this.lastPaintPosition != null ? this.lastPaintPosition.toString() : "";
/*      */ 
/* 1410 */     String str3 = this.scrollUnderway ? "true" : "false";
/*      */ 
/* 1413 */     return super.paramString() + ",isViewSizeSet=" + str1 + ",lastPaintPosition=" + str2 + ",scrollUnderway=" + str3;
/*      */   }
/*      */ 
/*      */   protected void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*      */   {
/* 1434 */     super.firePropertyChange(paramString, paramObject1, paramObject2);
/* 1435 */     if (paramString.equals(EnableWindowBlit))
/* 1436 */       if (paramObject2 != null)
/* 1437 */         setScrollMode(1);
/*      */       else
/* 1439 */         setScrollMode(0);
/*      */   }
/*      */ 
/*      */   private boolean needsRepaintAfterBlit()
/*      */   {
/* 1451 */     Container localContainer = getParent();
/*      */ 
/* 1453 */     while ((localContainer != null) && (localContainer.isLightweight())) {
/* 1454 */       localContainer = localContainer.getParent();
/*      */     }
/*      */ 
/* 1457 */     if (localContainer != null) {
/* 1458 */       ComponentPeer localComponentPeer = localContainer.getPeer();
/*      */ 
/* 1460 */       if ((localComponentPeer != null) && (localComponentPeer.canDetermineObscurity()) && (!localComponentPeer.isObscured()))
/*      */       {
/* 1468 */         return false;
/*      */       }
/*      */     }
/* 1471 */     return true;
/*      */   }
/*      */ 
/*      */   private Timer createRepaintTimer() {
/* 1475 */     Timer localTimer = new Timer(300, new ActionListener()
/*      */     {
/*      */       public void actionPerformed(ActionEvent paramAnonymousActionEvent)
/*      */       {
/* 1480 */         if (JViewport.this.waitingForRepaint)
/* 1481 */           JViewport.this.repaint();
/*      */       }
/*      */     });
/* 1485 */     localTimer.setRepeats(false);
/* 1486 */     return localTimer;
/*      */   }
/*      */ 
/*      */   private void flushViewDirtyRegion(Graphics paramGraphics, Rectangle paramRectangle)
/*      */   {
/* 1496 */     JComponent localJComponent = (JComponent)getView();
/* 1497 */     if ((paramRectangle != null) && (paramRectangle.width > 0) && (paramRectangle.height > 0)) {
/* 1498 */       paramRectangle.x += localJComponent.getX();
/* 1499 */       paramRectangle.y += localJComponent.getY();
/* 1500 */       Rectangle localRectangle = paramGraphics.getClipBounds();
/* 1501 */       if (localRectangle == null)
/*      */       {
/* 1503 */         paramGraphics.setClip(0, 0, getWidth(), getHeight());
/*      */       }
/* 1505 */       paramGraphics.clipRect(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/* 1506 */       localRectangle = paramGraphics.getClipBounds();
/*      */ 
/* 1508 */       if ((localRectangle.width > 0) && (localRectangle.height > 0))
/* 1509 */         paintView(paramGraphics);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean windowBlitPaint(Graphics paramGraphics)
/*      */   {
/* 1521 */     int i = getWidth();
/* 1522 */     int j = getHeight();
/*      */ 
/* 1524 */     if ((i == 0) || (j == 0)) {
/* 1525 */       return false;
/*      */     }
/*      */ 
/* 1529 */     RepaintManager localRepaintManager = RepaintManager.currentManager(this);
/* 1530 */     JComponent localJComponent = (JComponent)getView();
/*      */     boolean bool1;
/* 1532 */     if ((this.lastPaintPosition == null) || (this.lastPaintPosition.equals(getViewLocation())))
/*      */     {
/* 1534 */       paintView(paramGraphics);
/* 1535 */       bool1 = false;
/*      */     }
/*      */     else
/*      */     {
/* 1539 */       Point localPoint1 = new Point();
/* 1540 */       Point localPoint2 = new Point();
/* 1541 */       Dimension localDimension = new Dimension();
/* 1542 */       Rectangle localRectangle1 = new Rectangle();
/*      */ 
/* 1544 */       Point localPoint3 = getViewLocation();
/* 1545 */       int k = localPoint3.x - this.lastPaintPosition.x;
/* 1546 */       int m = localPoint3.y - this.lastPaintPosition.y;
/* 1547 */       boolean bool2 = computeBlit(k, m, localPoint1, localPoint2, localDimension, localRectangle1);
/*      */ 
/* 1549 */       if (!bool2) {
/* 1550 */         paintView(paramGraphics);
/* 1551 */         bool1 = false;
/*      */       }
/*      */       else
/*      */       {
/* 1555 */         Rectangle localRectangle2 = localJComponent.getBounds().intersection(localRectangle1);
/* 1556 */         localRectangle2.x -= localJComponent.getX();
/* 1557 */         localRectangle2.y -= localJComponent.getY();
/*      */ 
/* 1559 */         blitDoubleBuffered(localJComponent, paramGraphics, localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, localPoint1.x, localPoint1.y, localPoint2.x, localPoint2.y, localDimension.width, localDimension.height);
/*      */ 
/* 1562 */         bool1 = true;
/*      */       }
/*      */     }
/* 1565 */     this.lastPaintPosition = getViewLocation();
/* 1566 */     return bool1;
/*      */   }
/*      */ 
/*      */   private void blitDoubleBuffered(JComponent paramJComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10)
/*      */   {
/* 1585 */     RepaintManager localRepaintManager = RepaintManager.currentManager(this);
/* 1586 */     int i = paramInt7 - paramInt5;
/* 1587 */     int j = paramInt8 - paramInt6;
/*      */ 
/* 1589 */     Composite localComposite = null;
/*      */ 
/* 1591 */     if ((paramGraphics instanceof Graphics2D)) {
/* 1592 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/* 1593 */       localComposite = localGraphics2D.getComposite();
/* 1594 */       localGraphics2D.setComposite(AlphaComposite.Src);
/*      */     }
/* 1596 */     localRepaintManager.copyArea(this, paramGraphics, paramInt5, paramInt6, paramInt9, paramInt10, i, j, false);
/*      */ 
/* 1598 */     if (localComposite != null) {
/* 1599 */       ((Graphics2D)paramGraphics).setComposite(localComposite);
/*      */     }
/*      */ 
/* 1602 */     int k = paramJComponent.getX();
/* 1603 */     int m = paramJComponent.getY();
/* 1604 */     paramGraphics.translate(k, m);
/* 1605 */     paramGraphics.setClip(paramInt1, paramInt2, paramInt3, paramInt4);
/* 1606 */     paramJComponent.paintForceDoubleBuffered(paramGraphics);
/* 1607 */     paramGraphics.translate(-k, -m);
/*      */   }
/*      */ 
/*      */   private void paintView(Graphics paramGraphics)
/*      */   {
/* 1617 */     Rectangle localRectangle = paramGraphics.getClipBounds();
/* 1618 */     JComponent localJComponent = (JComponent)getView();
/*      */ 
/* 1620 */     if (localJComponent.getWidth() >= getWidth())
/*      */     {
/* 1623 */       int i = localJComponent.getX();
/* 1624 */       int j = localJComponent.getY();
/* 1625 */       paramGraphics.translate(i, j);
/* 1626 */       paramGraphics.setClip(localRectangle.x - i, localRectangle.y - j, localRectangle.width, localRectangle.height);
/* 1627 */       localJComponent.paintForceDoubleBuffered(paramGraphics);
/* 1628 */       paramGraphics.translate(-i, -j);
/* 1629 */       paramGraphics.setClip(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/* 1635 */         this.inBlitPaint = true;
/* 1636 */         paintForceDoubleBuffered(paramGraphics);
/*      */       } finally {
/* 1638 */         this.inBlitPaint = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean canUseWindowBlitter()
/*      */   {
/* 1651 */     if ((!isShowing()) || ((!(getParent() instanceof JComponent)) && (!(getView() instanceof JComponent))))
/*      */     {
/* 1653 */       return false;
/*      */     }
/* 1655 */     if (isPainting())
/*      */     {
/* 1659 */       return false;
/*      */     }
/*      */ 
/* 1662 */     Rectangle localRectangle1 = RepaintManager.currentManager(this).getDirtyRegion((JComponent)getParent());
/*      */ 
/* 1665 */     if ((localRectangle1 != null) && (localRectangle1.width > 0) && (localRectangle1.height > 0))
/*      */     {
/* 1668 */       return false;
/*      */     }
/*      */ 
/* 1671 */     Rectangle localRectangle2 = new Rectangle(0, 0, getWidth(), getHeight());
/* 1672 */     Rectangle localRectangle3 = new Rectangle();
/* 1673 */     Rectangle localRectangle4 = null;
/*      */ 
/* 1675 */     Object localObject2 = null;
/*      */ 
/* 1678 */     for (Object localObject1 = this; (localObject1 != null) && (isLightweightComponent((Component)localObject1)); localObject1 = ((Container)localObject1).getParent()) {
/* 1679 */       int i = ((Container)localObject1).getX();
/* 1680 */       int j = ((Container)localObject1).getY();
/* 1681 */       int k = ((Container)localObject1).getWidth();
/* 1682 */       int m = ((Container)localObject1).getHeight();
/*      */ 
/* 1684 */       localRectangle3.setBounds(localRectangle2);
/* 1685 */       SwingUtilities.computeIntersection(0, 0, k, m, localRectangle2);
/* 1686 */       if (!localRectangle2.equals(localRectangle3)) {
/* 1687 */         return false;
/*      */       }
/* 1689 */       if ((localObject2 != null) && ((localObject1 instanceof JComponent)) && (!((JComponent)localObject1).isOptimizedDrawingEnabled()))
/*      */       {
/* 1691 */         Component[] arrayOfComponent = ((Container)localObject1).getComponents();
/* 1692 */         int n = 0;
/*      */ 
/* 1694 */         for (int i1 = arrayOfComponent.length - 1; i1 >= 0; i1--) {
/* 1695 */           if (arrayOfComponent[i1] == localObject2) {
/* 1696 */             n = i1 - 1;
/* 1697 */             break;
/*      */           }
/*      */         }
/*      */ 
/* 1701 */         while (n >= 0) {
/* 1702 */           localRectangle4 = arrayOfComponent[n].getBounds(localRectangle4);
/*      */ 
/* 1704 */           if (localRectangle4.intersects(localRectangle2))
/* 1705 */             return false;
/* 1706 */           n--;
/*      */         }
/*      */       }
/* 1709 */       localRectangle2.x += i;
/* 1710 */       localRectangle2.y += j;
/* 1711 */       localObject2 = localObject1;
/*      */     }
/* 1713 */     if (localObject1 == null)
/*      */     {
/* 1715 */       return false;
/*      */     }
/* 1717 */     return true;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1735 */     if (this.accessibleContext == null) {
/* 1736 */       this.accessibleContext = new AccessibleJViewport();
/*      */     }
/* 1738 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJViewport extends JComponent.AccessibleJComponent
/*      */   {
/*      */     protected AccessibleJViewport()
/*      */     {
/* 1755 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1763 */       return AccessibleRole.VIEWPORT;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ViewListener extends ComponentAdapter
/*      */     implements Serializable
/*      */   {
/*      */     protected ViewListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void componentResized(ComponentEvent paramComponentEvent)
/*      */     {
/* 1289 */       JViewport.this.fireStateChanged();
/* 1290 */       JViewport.this.revalidate();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JViewport
 * JD-Core Version:    0.6.2
 */
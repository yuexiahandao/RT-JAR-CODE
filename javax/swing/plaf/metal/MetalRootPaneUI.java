/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRootPaneUI;
/*     */ 
/*     */ public class MetalRootPaneUI extends BasicRootPaneUI
/*     */ {
/*  71 */   private static final String[] borderKeys = { null, "RootPane.frameBorder", "RootPane.plainDialogBorder", "RootPane.informationDialogBorder", "RootPane.errorDialogBorder", "RootPane.colorChooserDialogBorder", "RootPane.fileChooserDialogBorder", "RootPane.questionDialogBorder", "RootPane.warningDialogBorder" };
/*     */   private static final int CORNER_DRAG_WIDTH = 16;
/*     */   private static final int BORDER_DRAG_THICKNESS = 5;
/*     */   private Window window;
/*     */   private JComponent titlePane;
/*     */   private MouseInputListener mouseInputListener;
/*     */   private LayoutManager layoutManager;
/*     */   private LayoutManager savedOldLayout;
/*     */   private JRootPane root;
/*     */   private Cursor lastCursor;
/* 663 */   private static final int[] cursorMapping = { 6, 6, 8, 7, 7, 6, 0, 0, 0, 7, 10, 0, 0, 0, 11, 4, 0, 0, 0, 5, 4, 4, 9, 5, 5 };
/*     */ 
/*     */   public MetalRootPaneUI()
/*     */   {
/* 126 */     this.lastCursor = Cursor.getPredefinedCursor(0);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 136 */     return new MetalRootPaneUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 154 */     super.installUI(paramJComponent);
/* 155 */     this.root = ((JRootPane)paramJComponent);
/* 156 */     int i = this.root.getWindowDecorationStyle();
/* 157 */     if (i != 0)
/* 158 */       installClientDecorations(this.root);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 175 */     super.uninstallUI(paramJComponent);
/* 176 */     uninstallClientDecorations(this.root);
/*     */ 
/* 178 */     this.layoutManager = null;
/* 179 */     this.mouseInputListener = null;
/* 180 */     this.root = null;
/*     */   }
/*     */ 
/*     */   void installBorder(JRootPane paramJRootPane)
/*     */   {
/* 188 */     int i = paramJRootPane.getWindowDecorationStyle();
/*     */ 
/* 190 */     if (i == 0) {
/* 191 */       LookAndFeel.uninstallBorder(paramJRootPane);
/*     */     }
/*     */     else
/* 194 */       LookAndFeel.installBorder(paramJRootPane, borderKeys[i]);
/*     */   }
/*     */ 
/*     */   private void uninstallBorder(JRootPane paramJRootPane)
/*     */   {
/* 202 */     LookAndFeel.uninstallBorder(paramJRootPane);
/*     */   }
/*     */ 
/*     */   private void installWindowListeners(JRootPane paramJRootPane, Component paramComponent)
/*     */   {
/* 216 */     if ((paramComponent instanceof Window)) {
/* 217 */       this.window = ((Window)paramComponent);
/*     */     }
/*     */     else {
/* 220 */       this.window = SwingUtilities.getWindowAncestor(paramComponent);
/*     */     }
/* 222 */     if (this.window != null) {
/* 223 */       if (this.mouseInputListener == null) {
/* 224 */         this.mouseInputListener = createWindowMouseInputListener(paramJRootPane);
/*     */       }
/* 226 */       this.window.addMouseListener(this.mouseInputListener);
/* 227 */       this.window.addMouseMotionListener(this.mouseInputListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void uninstallWindowListeners(JRootPane paramJRootPane)
/*     */   {
/* 236 */     if (this.window != null) {
/* 237 */       this.window.removeMouseListener(this.mouseInputListener);
/* 238 */       this.window.removeMouseMotionListener(this.mouseInputListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void installLayout(JRootPane paramJRootPane)
/*     */   {
/* 247 */     if (this.layoutManager == null) {
/* 248 */       this.layoutManager = createLayoutManager();
/*     */     }
/* 250 */     this.savedOldLayout = paramJRootPane.getLayout();
/* 251 */     paramJRootPane.setLayout(this.layoutManager);
/*     */   }
/*     */ 
/*     */   private void uninstallLayout(JRootPane paramJRootPane)
/*     */   {
/* 258 */     if (this.savedOldLayout != null) {
/* 259 */       paramJRootPane.setLayout(this.savedOldLayout);
/* 260 */       this.savedOldLayout = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void installClientDecorations(JRootPane paramJRootPane)
/*     */   {
/* 270 */     installBorder(paramJRootPane);
/*     */ 
/* 272 */     JComponent localJComponent = createTitlePane(paramJRootPane);
/*     */ 
/* 274 */     setTitlePane(paramJRootPane, localJComponent);
/* 275 */     installWindowListeners(paramJRootPane, paramJRootPane.getParent());
/* 276 */     installLayout(paramJRootPane);
/* 277 */     if (this.window != null) {
/* 278 */       paramJRootPane.revalidate();
/* 279 */       paramJRootPane.repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void uninstallClientDecorations(JRootPane paramJRootPane)
/*     */   {
/* 291 */     uninstallBorder(paramJRootPane);
/* 292 */     uninstallWindowListeners(paramJRootPane);
/* 293 */     setTitlePane(paramJRootPane, null);
/* 294 */     uninstallLayout(paramJRootPane);
/*     */ 
/* 300 */     int i = paramJRootPane.getWindowDecorationStyle();
/* 301 */     if (i == 0) {
/* 302 */       paramJRootPane.repaint();
/* 303 */       paramJRootPane.revalidate();
/*     */     }
/*     */ 
/* 306 */     if (this.window != null) {
/* 307 */       this.window.setCursor(Cursor.getPredefinedCursor(0));
/*     */     }
/*     */ 
/* 310 */     this.window = null;
/*     */   }
/*     */ 
/*     */   private JComponent createTitlePane(JRootPane paramJRootPane)
/*     */   {
/* 318 */     return new MetalTitlePane(paramJRootPane, this);
/*     */   }
/*     */ 
/*     */   private MouseInputListener createWindowMouseInputListener(JRootPane paramJRootPane)
/*     */   {
/* 326 */     return new MouseInputHandler(null);
/*     */   }
/*     */ 
/*     */   private LayoutManager createLayoutManager()
/*     */   {
/* 334 */     return new MetalRootLayout(null);
/*     */   }
/*     */ 
/*     */   private void setTitlePane(JRootPane paramJRootPane, JComponent paramJComponent)
/*     */   {
/* 347 */     JLayeredPane localJLayeredPane = paramJRootPane.getLayeredPane();
/* 348 */     JComponent localJComponent = getTitlePane();
/*     */ 
/* 350 */     if (localJComponent != null) {
/* 351 */       localJComponent.setVisible(false);
/* 352 */       localJLayeredPane.remove(localJComponent);
/*     */     }
/* 354 */     if (paramJComponent != null) {
/* 355 */       localJLayeredPane.add(paramJComponent, JLayeredPane.FRAME_CONTENT_LAYER);
/* 356 */       paramJComponent.setVisible(true);
/*     */     }
/* 358 */     this.titlePane = paramJComponent;
/*     */   }
/*     */ 
/*     */   private JComponent getTitlePane()
/*     */   {
/* 369 */     return this.titlePane;
/*     */   }
/*     */ 
/*     */   private JRootPane getRootPane()
/*     */   {
/* 377 */     return this.root;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 399 */     super.propertyChange(paramPropertyChangeEvent);
/*     */ 
/* 401 */     String str = paramPropertyChangeEvent.getPropertyName();
/* 402 */     if (str == null) {
/* 403 */       return;
/*     */     }
/*     */ 
/* 406 */     if (str.equals("windowDecorationStyle")) {
/* 407 */       JRootPane localJRootPane = (JRootPane)paramPropertyChangeEvent.getSource();
/* 408 */       int i = localJRootPane.getWindowDecorationStyle();
/*     */ 
/* 414 */       uninstallClientDecorations(localJRootPane);
/* 415 */       if (i != 0) {
/* 416 */         installClientDecorations(localJRootPane);
/*     */       }
/*     */     }
/* 419 */     else if (str.equals("ancestor")) {
/* 420 */       uninstallWindowListeners(this.root);
/* 421 */       if (((JRootPane)paramPropertyChangeEvent.getSource()).getWindowDecorationStyle() != 0)
/*     */       {
/* 423 */         installWindowListeners(this.root, this.root.getParent());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MetalRootLayout
/*     */     implements LayoutManager2
/*     */   {
/*     */     public Dimension preferredLayoutSize(Container paramContainer)
/*     */     {
/* 445 */       int i = 0;
/* 446 */       int j = 0;
/* 447 */       int k = 0;
/* 448 */       int m = 0;
/* 449 */       int n = 0;
/* 450 */       int i1 = 0;
/* 451 */       Insets localInsets = paramContainer.getInsets();
/* 452 */       JRootPane localJRootPane = (JRootPane)paramContainer;
/*     */       Dimension localDimension1;
/* 454 */       if (localJRootPane.getContentPane() != null)
/* 455 */         localDimension1 = localJRootPane.getContentPane().getPreferredSize();
/*     */       else {
/* 457 */         localDimension1 = localJRootPane.getSize();
/*     */       }
/* 459 */       if (localDimension1 != null) {
/* 460 */         i = localDimension1.width;
/* 461 */         j = localDimension1.height;
/*     */       }
/*     */ 
/* 464 */       if (localJRootPane.getMenuBar() != null) {
/* 465 */         Dimension localDimension2 = localJRootPane.getMenuBar().getPreferredSize();
/* 466 */         if (localDimension2 != null) {
/* 467 */           k = localDimension2.width;
/* 468 */           m = localDimension2.height;
/*     */         }
/*     */       }
/*     */ 
/* 472 */       if ((localJRootPane.getWindowDecorationStyle() != 0) && ((localJRootPane.getUI() instanceof MetalRootPaneUI)))
/*     */       {
/* 474 */         JComponent localJComponent = ((MetalRootPaneUI)localJRootPane.getUI()).getTitlePane();
/*     */ 
/* 476 */         if (localJComponent != null) {
/* 477 */           Dimension localDimension3 = localJComponent.getPreferredSize();
/* 478 */           if (localDimension3 != null) {
/* 479 */             n = localDimension3.width;
/* 480 */             i1 = localDimension3.height;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 485 */       return new Dimension(Math.max(Math.max(i, k), n) + localInsets.left + localInsets.right, j + m + n + localInsets.top + localInsets.bottom);
/*     */     }
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container paramContainer)
/*     */     {
/* 497 */       int i = 0;
/* 498 */       int j = 0;
/* 499 */       int k = 0;
/* 500 */       int m = 0;
/* 501 */       int n = 0;
/* 502 */       int i1 = 0;
/* 503 */       Insets localInsets = paramContainer.getInsets();
/* 504 */       JRootPane localJRootPane = (JRootPane)paramContainer;
/*     */       Dimension localDimension1;
/* 506 */       if (localJRootPane.getContentPane() != null)
/* 507 */         localDimension1 = localJRootPane.getContentPane().getMinimumSize();
/*     */       else {
/* 509 */         localDimension1 = localJRootPane.getSize();
/*     */       }
/* 511 */       if (localDimension1 != null) {
/* 512 */         i = localDimension1.width;
/* 513 */         j = localDimension1.height;
/*     */       }
/*     */ 
/* 516 */       if (localJRootPane.getMenuBar() != null) {
/* 517 */         Dimension localDimension2 = localJRootPane.getMenuBar().getMinimumSize();
/* 518 */         if (localDimension2 != null) {
/* 519 */           k = localDimension2.width;
/* 520 */           m = localDimension2.height;
/*     */         }
/*     */       }
/* 523 */       if ((localJRootPane.getWindowDecorationStyle() != 0) && ((localJRootPane.getUI() instanceof MetalRootPaneUI)))
/*     */       {
/* 525 */         JComponent localJComponent = ((MetalRootPaneUI)localJRootPane.getUI()).getTitlePane();
/*     */ 
/* 527 */         if (localJComponent != null) {
/* 528 */           Dimension localDimension3 = localJComponent.getMinimumSize();
/* 529 */           if (localDimension3 != null) {
/* 530 */             n = localDimension3.width;
/* 531 */             i1 = localDimension3.height;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 536 */       return new Dimension(Math.max(Math.max(i, k), n) + localInsets.left + localInsets.right, j + m + n + localInsets.top + localInsets.bottom);
/*     */     }
/*     */ 
/*     */     public Dimension maximumLayoutSize(Container paramContainer)
/*     */     {
/* 548 */       int i = 2147483647;
/* 549 */       int j = 2147483647;
/* 550 */       int k = 2147483647;
/* 551 */       int m = 2147483647;
/* 552 */       int n = 2147483647;
/* 553 */       int i1 = 2147483647;
/* 554 */       Insets localInsets = paramContainer.getInsets();
/* 555 */       JRootPane localJRootPane = (JRootPane)paramContainer;
/*     */ 
/* 557 */       if (localJRootPane.getContentPane() != null) {
/* 558 */         Dimension localDimension1 = localJRootPane.getContentPane().getMaximumSize();
/* 559 */         if (localDimension1 != null) {
/* 560 */           i = localDimension1.width;
/* 561 */           j = localDimension1.height;
/*     */         }
/*     */       }
/*     */ 
/* 565 */       if (localJRootPane.getMenuBar() != null) {
/* 566 */         Dimension localDimension2 = localJRootPane.getMenuBar().getMaximumSize();
/* 567 */         if (localDimension2 != null) {
/* 568 */           k = localDimension2.width;
/* 569 */           m = localDimension2.height;
/*     */         }
/*     */       }
/*     */ 
/* 573 */       if ((localJRootPane.getWindowDecorationStyle() != 0) && ((localJRootPane.getUI() instanceof MetalRootPaneUI)))
/*     */       {
/* 575 */         JComponent localJComponent = ((MetalRootPaneUI)localJRootPane.getUI()).getTitlePane();
/*     */ 
/* 577 */         if (localJComponent != null)
/*     */         {
/* 579 */           Dimension localDimension3 = localJComponent.getMaximumSize();
/* 580 */           if (localDimension3 != null) {
/* 581 */             n = localDimension3.width;
/* 582 */             i1 = localDimension3.height;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 587 */       int i2 = Math.max(Math.max(j, m), i1);
/*     */ 
/* 590 */       if (i2 != 2147483647) {
/* 591 */         i2 = j + m + i1 + localInsets.top + localInsets.bottom;
/*     */       }
/*     */ 
/* 594 */       int i3 = Math.max(Math.max(i, k), n);
/*     */ 
/* 596 */       if (i3 != 2147483647) {
/* 597 */         i3 += localInsets.left + localInsets.right;
/*     */       }
/*     */ 
/* 600 */       return new Dimension(i3, i2);
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer)
/*     */     {
/* 610 */       JRootPane localJRootPane = (JRootPane)paramContainer;
/* 611 */       Rectangle localRectangle = localJRootPane.getBounds();
/* 612 */       Insets localInsets = localJRootPane.getInsets();
/* 613 */       int i = 0;
/* 614 */       int j = localRectangle.width - localInsets.right - localInsets.left;
/* 615 */       int k = localRectangle.height - localInsets.top - localInsets.bottom;
/*     */ 
/* 617 */       if (localJRootPane.getLayeredPane() != null) {
/* 618 */         localJRootPane.getLayeredPane().setBounds(localInsets.left, localInsets.top, j, k);
/*     */       }
/* 620 */       if (localJRootPane.getGlassPane() != null)
/* 621 */         localJRootPane.getGlassPane().setBounds(localInsets.left, localInsets.top, j, k);
/*     */       Object localObject;
/* 625 */       if ((localJRootPane.getWindowDecorationStyle() != 0) && ((localJRootPane.getUI() instanceof MetalRootPaneUI)))
/*     */       {
/* 627 */         localObject = ((MetalRootPaneUI)localJRootPane.getUI()).getTitlePane();
/*     */ 
/* 629 */         if (localObject != null) {
/* 630 */           Dimension localDimension = ((JComponent)localObject).getPreferredSize();
/* 631 */           if (localDimension != null) {
/* 632 */             int m = localDimension.height;
/* 633 */             ((JComponent)localObject).setBounds(0, 0, j, m);
/* 634 */             i += m;
/*     */           }
/*     */         }
/*     */       }
/* 638 */       if (localJRootPane.getMenuBar() != null) {
/* 639 */         localObject = localJRootPane.getMenuBar().getPreferredSize();
/* 640 */         localJRootPane.getMenuBar().setBounds(0, i, j, ((Dimension)localObject).height);
/* 641 */         i += ((Dimension)localObject).height;
/*     */       }
/* 643 */       if (localJRootPane.getContentPane() != null) {
/* 644 */         localObject = localJRootPane.getContentPane().getPreferredSize();
/* 645 */         localJRootPane.getContentPane().setBounds(0, i, j, k < i ? 0 : k - i);
/*     */       }
/*     */     }
/*     */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*     */     }
/*     */     public void removeLayoutComponent(Component paramComponent) {
/*     */     }
/*     */     public void addLayoutComponent(Component paramComponent, Object paramObject) {  }
/*     */ 
/* 653 */     public float getLayoutAlignmentX(Container paramContainer) { return 0.0F; } 
/* 654 */     public float getLayoutAlignmentY(Container paramContainer) { return 0.0F; }
/*     */ 
/*     */ 
/*     */     public void invalidateLayout(Container paramContainer)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private class MouseInputHandler
/*     */     implements MouseInputListener
/*     */   {
/*     */     private boolean isMovingWindow;
/*     */     private int dragCursor;
/*     */     private int dragOffsetX;
/*     */     private int dragOffsetY;
/*     */     private int dragWidth;
/*     */     private int dragHeight;
/*     */ 
/*     */     private MouseInputHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/* 710 */       JRootPane localJRootPane = MetalRootPaneUI.this.getRootPane();
/*     */ 
/* 712 */       if (localJRootPane.getWindowDecorationStyle() == 0) {
/* 713 */         return;
/*     */       }
/* 715 */       Point localPoint1 = paramMouseEvent.getPoint();
/* 716 */       Window localWindow = (Window)paramMouseEvent.getSource();
/* 717 */       if (localWindow != null) {
/* 718 */         localWindow.toFront();
/*     */       }
/* 720 */       Point localPoint2 = SwingUtilities.convertPoint(localWindow, localPoint1, MetalRootPaneUI.this.getTitlePane());
/*     */ 
/* 723 */       Frame localFrame = null;
/* 724 */       Dialog localDialog = null;
/*     */ 
/* 726 */       if ((localWindow instanceof Frame))
/* 727 */         localFrame = (Frame)localWindow;
/* 728 */       else if ((localWindow instanceof Dialog)) {
/* 729 */         localDialog = (Dialog)localWindow;
/*     */       }
/*     */ 
/* 732 */       int i = localFrame != null ? localFrame.getExtendedState() : 0;
/*     */ 
/* 734 */       if ((MetalRootPaneUI.this.getTitlePane() != null) && (MetalRootPaneUI.this.getTitlePane().contains(localPoint2)))
/*     */       {
/* 736 */         if (((localFrame != null) && ((i & 0x6) == 0)) || ((localDialog != null) && (localPoint1.y >= 5) && (localPoint1.x >= 5) && (localPoint1.x < localWindow.getWidth() - 5)))
/*     */         {
/* 742 */           this.isMovingWindow = true;
/* 743 */           this.dragOffsetX = localPoint1.x;
/* 744 */           this.dragOffsetY = localPoint1.y;
/*     */         }
/*     */       }
/* 747 */       else if (((localFrame != null) && (localFrame.isResizable()) && ((i & 0x6) == 0)) || ((localDialog != null) && (localDialog.isResizable())))
/*     */       {
/* 750 */         this.dragOffsetX = localPoint1.x;
/* 751 */         this.dragOffsetY = localPoint1.y;
/* 752 */         this.dragWidth = localWindow.getWidth();
/* 753 */         this.dragHeight = localWindow.getHeight();
/* 754 */         this.dragCursor = getCursor(calculateCorner(localWindow, localPoint1.x, localPoint1.y));
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent)
/*     */     {
/* 760 */       if ((this.dragCursor != 0) && (MetalRootPaneUI.this.window != null) && (!MetalRootPaneUI.this.window.isValid()))
/*     */       {
/* 763 */         MetalRootPaneUI.this.window.validate();
/* 764 */         MetalRootPaneUI.this.getRootPane().repaint();
/*     */       }
/* 766 */       this.isMovingWindow = false;
/* 767 */       this.dragCursor = 0;
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 771 */       JRootPane localJRootPane = MetalRootPaneUI.this.getRootPane();
/*     */ 
/* 773 */       if (localJRootPane.getWindowDecorationStyle() == 0) {
/* 774 */         return;
/*     */       }
/*     */ 
/* 777 */       Window localWindow = (Window)paramMouseEvent.getSource();
/*     */ 
/* 779 */       Frame localFrame = null;
/* 780 */       Dialog localDialog = null;
/*     */ 
/* 782 */       if ((localWindow instanceof Frame))
/* 783 */         localFrame = (Frame)localWindow;
/* 784 */       else if ((localWindow instanceof Dialog)) {
/* 785 */         localDialog = (Dialog)localWindow;
/*     */       }
/*     */ 
/* 789 */       int i = getCursor(calculateCorner(localWindow, paramMouseEvent.getX(), paramMouseEvent.getY()));
/*     */ 
/* 791 */       if ((i != 0) && (((localFrame != null) && (localFrame.isResizable()) && ((localFrame.getExtendedState() & 0x6) == 0)) || ((localDialog != null) && (localDialog.isResizable()))))
/*     */       {
/* 794 */         localWindow.setCursor(Cursor.getPredefinedCursor(i));
/*     */       }
/*     */       else
/* 797 */         localWindow.setCursor(MetalRootPaneUI.this.lastCursor);
/*     */     }
/*     */ 
/*     */     private void adjust(Rectangle paramRectangle, Dimension paramDimension, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 803 */       paramRectangle.x += paramInt1;
/* 804 */       paramRectangle.y += paramInt2;
/* 805 */       paramRectangle.width += paramInt3;
/* 806 */       paramRectangle.height += paramInt4;
/* 807 */       if (paramDimension != null)
/*     */       {
/*     */         int i;
/* 808 */         if (paramRectangle.width < paramDimension.width) {
/* 809 */           i = paramDimension.width - paramRectangle.width;
/* 810 */           if (paramInt1 != 0) {
/* 811 */             paramRectangle.x -= i;
/*     */           }
/* 813 */           paramRectangle.width = paramDimension.width;
/*     */         }
/* 815 */         if (paramRectangle.height < paramDimension.height) {
/* 816 */           i = paramDimension.height - paramRectangle.height;
/* 817 */           if (paramInt2 != 0) {
/* 818 */             paramRectangle.y -= i;
/*     */           }
/* 820 */           paramRectangle.height = paramDimension.height;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 826 */       Window localWindow = (Window)paramMouseEvent.getSource();
/* 827 */       Point localPoint = paramMouseEvent.getPoint();
/*     */       Object localObject;
/* 829 */       if (this.isMovingWindow) {
/* 830 */         localObject = paramMouseEvent.getLocationOnScreen();
/* 831 */         localWindow.setLocation(((Point)localObject).x - this.dragOffsetX, ((Point)localObject).y - this.dragOffsetY);
/*     */       }
/* 834 */       else if (this.dragCursor != 0) {
/* 835 */         localObject = localWindow.getBounds();
/* 836 */         Rectangle localRectangle = new Rectangle((Rectangle)localObject);
/* 837 */         Dimension localDimension = localWindow.getMinimumSize();
/*     */ 
/* 839 */         switch (this.dragCursor) {
/*     */         case 11:
/* 841 */           adjust((Rectangle)localObject, localDimension, 0, 0, localPoint.x + (this.dragWidth - this.dragOffsetX) - ((Rectangle)localObject).width, 0);
/*     */ 
/* 843 */           break;
/*     */         case 9:
/* 845 */           adjust((Rectangle)localObject, localDimension, 0, 0, 0, localPoint.y + (this.dragHeight - this.dragOffsetY) - ((Rectangle)localObject).height);
/*     */ 
/* 847 */           break;
/*     */         case 8:
/* 849 */           adjust((Rectangle)localObject, localDimension, 0, localPoint.y - this.dragOffsetY, 0, -(localPoint.y - this.dragOffsetY));
/*     */ 
/* 851 */           break;
/*     */         case 10:
/* 853 */           adjust((Rectangle)localObject, localDimension, localPoint.x - this.dragOffsetX, 0, -(localPoint.x - this.dragOffsetX), 0);
/*     */ 
/* 855 */           break;
/*     */         case 7:
/* 857 */           adjust((Rectangle)localObject, localDimension, 0, localPoint.y - this.dragOffsetY, localPoint.x + (this.dragWidth - this.dragOffsetX) - ((Rectangle)localObject).width, -(localPoint.y - this.dragOffsetY));
/*     */ 
/* 860 */           break;
/*     */         case 5:
/* 862 */           adjust((Rectangle)localObject, localDimension, 0, 0, localPoint.x + (this.dragWidth - this.dragOffsetX) - ((Rectangle)localObject).width, localPoint.y + (this.dragHeight - this.dragOffsetY) - ((Rectangle)localObject).height);
/*     */ 
/* 866 */           break;
/*     */         case 6:
/* 868 */           adjust((Rectangle)localObject, localDimension, localPoint.x - this.dragOffsetX, localPoint.y - this.dragOffsetY, -(localPoint.x - this.dragOffsetX), -(localPoint.y - this.dragOffsetY));
/*     */ 
/* 872 */           break;
/*     */         case 4:
/* 874 */           adjust((Rectangle)localObject, localDimension, localPoint.x - this.dragOffsetX, 0, -(localPoint.x - this.dragOffsetX), localPoint.y + (this.dragHeight - this.dragOffsetY) - ((Rectangle)localObject).height);
/*     */ 
/* 877 */           break;
/*     */         }
/*     */ 
/* 881 */         if (!((Rectangle)localObject).equals(localRectangle)) {
/* 882 */           localWindow.setBounds((Rectangle)localObject);
/*     */ 
/* 885 */           if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
/* 886 */             localWindow.validate();
/* 887 */             MetalRootPaneUI.this.getRootPane().repaint();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/* 894 */       Window localWindow = (Window)paramMouseEvent.getSource();
/* 895 */       MetalRootPaneUI.this.lastCursor = localWindow.getCursor();
/* 896 */       mouseMoved(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 900 */       Window localWindow = (Window)paramMouseEvent.getSource();
/* 901 */       localWindow.setCursor(MetalRootPaneUI.this.lastCursor);
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent paramMouseEvent) {
/* 905 */       Window localWindow = (Window)paramMouseEvent.getSource();
/* 906 */       Frame localFrame = null;
/*     */ 
/* 908 */       if ((localWindow instanceof Frame))
/* 909 */         localFrame = (Frame)localWindow;
/*     */       else {
/* 911 */         return;
/*     */       }
/*     */ 
/* 914 */       Point localPoint = SwingUtilities.convertPoint(localWindow, paramMouseEvent.getPoint(), MetalRootPaneUI.this.getTitlePane());
/*     */ 
/* 917 */       int i = localFrame.getExtendedState();
/* 918 */       if ((MetalRootPaneUI.this.getTitlePane() != null) && (MetalRootPaneUI.this.getTitlePane().contains(localPoint)))
/*     */       {
/* 920 */         if ((paramMouseEvent.getClickCount() % 2 == 0) && ((paramMouseEvent.getModifiers() & 0x10) != 0))
/*     */         {
/* 922 */           if (localFrame.isResizable()) {
/* 923 */             if ((i & 0x6) != 0) {
/* 924 */               localFrame.setExtendedState(i & 0xFFFFFFF9);
/*     */             }
/*     */             else {
/* 927 */               localFrame.setExtendedState(i | 0x6);
/*     */             }
/* 929 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private int calculateCorner(Window paramWindow, int paramInt1, int paramInt2)
/*     */     {
/* 940 */       Insets localInsets = paramWindow.getInsets();
/* 941 */       int i = calculatePosition(paramInt1 - localInsets.left, paramWindow.getWidth() - localInsets.left - localInsets.right);
/*     */ 
/* 943 */       int j = calculatePosition(paramInt2 - localInsets.top, paramWindow.getHeight() - localInsets.top - localInsets.bottom);
/*     */ 
/* 946 */       if ((i == -1) || (j == -1)) {
/* 947 */         return -1;
/*     */       }
/* 949 */       return j * 5 + i;
/*     */     }
/*     */ 
/*     */     private int getCursor(int paramInt)
/*     */     {
/* 957 */       if (paramInt == -1) {
/* 958 */         return 0;
/*     */       }
/* 960 */       return MetalRootPaneUI.cursorMapping[paramInt];
/*     */     }
/*     */ 
/*     */     private int calculatePosition(int paramInt1, int paramInt2)
/*     */     {
/* 974 */       if (paramInt1 < 5) {
/* 975 */         return 0;
/*     */       }
/* 977 */       if (paramInt1 < 16) {
/* 978 */         return 1;
/*     */       }
/* 980 */       if (paramInt1 >= paramInt2 - 5) {
/* 981 */         return 4;
/*     */       }
/* 983 */       if (paramInt1 >= paramInt2 - 16) {
/* 984 */         return 3;
/*     */       }
/* 986 */       return 2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalRootPaneUI
 * JD-Core Version:    0.6.2
 */
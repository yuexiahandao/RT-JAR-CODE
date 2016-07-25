/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.GraphicsDevice;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Insets;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseMotionAdapter;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ 
/*     */ public class ToolTipManager extends MouseAdapter
/*     */   implements MouseMotionListener
/*     */ {
/*     */   Timer enterTimer;
/*     */   Timer exitTimer;
/*     */   Timer insideTimer;
/*     */   String toolTipText;
/*     */   Point preferredLocation;
/*     */   JComponent insideComponent;
/*     */   MouseEvent mouseEvent;
/*     */   boolean showImmediately;
/*  60 */   private static final Object TOOL_TIP_MANAGER_KEY = new Object();
/*     */   transient Popup tipWindow;
/*     */   private Window window;
/*     */   JToolTip tip;
/*  68 */   private Rectangle popupRect = null;
/*  69 */   private Rectangle popupFrameRect = null;
/*     */ 
/*  71 */   boolean enabled = true;
/*  72 */   private boolean tipShowing = false;
/*     */ 
/*  74 */   private FocusListener focusChangeListener = null;
/*  75 */   private MouseMotionListener moveBeforeEnterListener = null;
/*  76 */   private KeyListener accessibilityKeyListener = null;
/*     */   private KeyStroke postTip;
/*     */   private KeyStroke hideTip;
/*  82 */   protected boolean lightWeightPopupEnabled = true;
/*  83 */   protected boolean heavyWeightPopupEnabled = false;
/*     */ 
/*     */   ToolTipManager() {
/*  86 */     this.enterTimer = new Timer(750, new insideTimerAction());
/*  87 */     this.enterTimer.setRepeats(false);
/*  88 */     this.exitTimer = new Timer(500, new outsideTimerAction());
/*  89 */     this.exitTimer.setRepeats(false);
/*  90 */     this.insideTimer = new Timer(4000, new stillInsideTimerAction());
/*  91 */     this.insideTimer.setRepeats(false);
/*     */ 
/*  93 */     this.moveBeforeEnterListener = new MoveBeforeEnterListener(null);
/*  94 */     this.accessibilityKeyListener = new AccessibilityKeyListener(null);
/*     */ 
/*  96 */     this.postTip = KeyStroke.getKeyStroke(112, 2);
/*  97 */     this.hideTip = KeyStroke.getKeyStroke(27, 0);
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean)
/*     */   {
/* 106 */     this.enabled = paramBoolean;
/* 107 */     if (!paramBoolean)
/* 108 */       hideTipWindow();
/*     */   }
/*     */ 
/*     */   public boolean isEnabled()
/*     */   {
/* 118 */     return this.enabled;
/*     */   }
/*     */ 
/*     */   public void setLightWeightPopupEnabled(boolean paramBoolean)
/*     */   {
/* 132 */     this.lightWeightPopupEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isLightWeightPopupEnabled()
/*     */   {
/* 143 */     return this.lightWeightPopupEnabled;
/*     */   }
/*     */ 
/*     */   public void setInitialDelay(int paramInt)
/*     */   {
/* 156 */     this.enterTimer.setInitialDelay(paramInt);
/*     */   }
/*     */ 
/*     */   public int getInitialDelay()
/*     */   {
/* 167 */     return this.enterTimer.getInitialDelay();
/*     */   }
/*     */ 
/*     */   public void setDismissDelay(int paramInt)
/*     */   {
/* 178 */     this.insideTimer.setInitialDelay(paramInt);
/*     */   }
/*     */ 
/*     */   public int getDismissDelay()
/*     */   {
/* 189 */     return this.insideTimer.getInitialDelay();
/*     */   }
/*     */ 
/*     */   public void setReshowDelay(int paramInt)
/*     */   {
/* 207 */     this.exitTimer.setInitialDelay(paramInt);
/*     */   }
/*     */ 
/*     */   public int getReshowDelay()
/*     */   {
/* 217 */     return this.exitTimer.getInitialDelay();
/*     */   }
/*     */ 
/*     */   private GraphicsConfiguration getDrawingGC(Point paramPoint)
/*     */   {
/* 224 */     GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 225 */     GraphicsDevice[] arrayOfGraphicsDevice1 = localGraphicsEnvironment.getScreenDevices();
/* 226 */     for (GraphicsDevice localGraphicsDevice : arrayOfGraphicsDevice1) {
/* 227 */       GraphicsConfiguration[] arrayOfGraphicsConfiguration1 = localGraphicsDevice.getConfigurations();
/* 228 */       for (GraphicsConfiguration localGraphicsConfiguration : arrayOfGraphicsConfiguration1) {
/* 229 */         Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/* 230 */         if (localRectangle.contains(paramPoint)) {
/* 231 */           return localGraphicsConfiguration;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 236 */     return null;
/*     */   }
/*     */ 
/*     */   void showTipWindow() {
/* 240 */     if ((this.insideComponent == null) || (!this.insideComponent.isShowing()))
/* 241 */       return;
/* 242 */     String str = UIManager.getString("ToolTipManager.enableToolTipMode");
/*     */     Object localObject;
/* 243 */     if ("activeApplication".equals(str)) {
/* 244 */       localObject = KeyboardFocusManager.getCurrentKeyboardFocusManager();
/*     */ 
/* 246 */       if (((KeyboardFocusManager)localObject).getFocusedWindow() == null) {
/* 247 */         return;
/*     */       }
/*     */     }
/* 250 */     if (this.enabled)
/*     */     {
/* 252 */       Point localPoint1 = this.insideComponent.getLocationOnScreen();
/*     */       Point localPoint3;
/* 256 */       if (this.preferredLocation != null) {
/* 257 */         localPoint3 = new Point(localPoint1.x + this.preferredLocation.x, localPoint1.y + this.preferredLocation.y);
/*     */       }
/*     */       else {
/* 260 */         localPoint3 = this.mouseEvent.getLocationOnScreen();
/*     */       }
/*     */ 
/* 263 */       GraphicsConfiguration localGraphicsConfiguration = getDrawingGC(localPoint3);
/* 264 */       if (localGraphicsConfiguration == null) {
/* 265 */         localPoint3 = this.mouseEvent.getLocationOnScreen();
/* 266 */         localGraphicsConfiguration = getDrawingGC(localPoint3);
/* 267 */         if (localGraphicsConfiguration == null) {
/* 268 */           localGraphicsConfiguration = this.insideComponent.getGraphicsConfiguration();
/*     */         }
/*     */       }
/*     */ 
/* 272 */       Rectangle localRectangle = localGraphicsConfiguration.getBounds();
/* 273 */       Insets localInsets = Toolkit.getDefaultToolkit().getScreenInsets(localGraphicsConfiguration);
/*     */ 
/* 276 */       localRectangle.x += localInsets.left;
/* 277 */       localRectangle.y += localInsets.top;
/* 278 */       localRectangle.width -= localInsets.left + localInsets.right;
/* 279 */       localRectangle.height -= localInsets.top + localInsets.bottom;
/* 280 */       boolean bool = SwingUtilities.isLeftToRight(this.insideComponent);
/*     */ 
/* 284 */       hideTipWindow();
/*     */ 
/* 286 */       this.tip = this.insideComponent.createToolTip();
/* 287 */       this.tip.setTipText(this.toolTipText);
/* 288 */       localObject = this.tip.getPreferredSize();
/*     */       Point localPoint2;
/* 290 */       if (this.preferredLocation != null) {
/* 291 */         localPoint2 = localPoint3;
/* 292 */         if (!bool)
/* 293 */           localPoint2.x -= ((Dimension)localObject).width;
/*     */       }
/*     */       else {
/* 296 */         localPoint2 = new Point(localPoint1.x + this.mouseEvent.getX(), localPoint1.y + this.mouseEvent.getY() + 20);
/*     */ 
/* 298 */         if ((!bool) && 
/* 299 */           (localPoint2.x - ((Dimension)localObject).width >= 0)) {
/* 300 */           localPoint2.x -= ((Dimension)localObject).width;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 307 */       if (this.popupRect == null) {
/* 308 */         this.popupRect = new Rectangle();
/*     */       }
/* 310 */       this.popupRect.setBounds(localPoint2.x, localPoint2.y, ((Dimension)localObject).width, ((Dimension)localObject).height);
/*     */ 
/* 314 */       if (localPoint2.x < localRectangle.x) {
/* 315 */         localPoint2.x = localRectangle.x;
/*     */       }
/* 317 */       else if (localPoint2.x - localRectangle.x + ((Dimension)localObject).width > localRectangle.width) {
/* 318 */         localPoint2.x = (localRectangle.x + Math.max(0, localRectangle.width - ((Dimension)localObject).width));
/*     */       }
/*     */ 
/* 321 */       if (localPoint2.y < localRectangle.y) {
/* 322 */         localPoint2.y = localRectangle.y;
/*     */       }
/* 324 */       else if (localPoint2.y - localRectangle.y + ((Dimension)localObject).height > localRectangle.height) {
/* 325 */         localPoint2.y = (localRectangle.y + Math.max(0, localRectangle.height - ((Dimension)localObject).height));
/*     */       }
/*     */ 
/* 328 */       PopupFactory localPopupFactory = PopupFactory.getSharedInstance();
/*     */ 
/* 330 */       if (this.lightWeightPopupEnabled) {
/* 331 */         int i = getPopupFitHeight(this.popupRect, this.insideComponent);
/* 332 */         int j = getPopupFitWidth(this.popupRect, this.insideComponent);
/* 333 */         if ((j > 0) || (i > 0))
/* 334 */           localPopupFactory.setPopupType(1);
/*     */         else
/* 336 */           localPopupFactory.setPopupType(0);
/*     */       }
/*     */       else
/*     */       {
/* 340 */         localPopupFactory.setPopupType(1);
/*     */       }
/* 342 */       this.tipWindow = localPopupFactory.getPopup(this.insideComponent, this.tip, localPoint2.x, localPoint2.y);
/*     */ 
/* 345 */       localPopupFactory.setPopupType(0);
/*     */ 
/* 347 */       this.tipWindow.show();
/*     */ 
/* 349 */       Window localWindow = SwingUtilities.windowForComponent(this.insideComponent);
/*     */ 
/* 352 */       this.window = SwingUtilities.windowForComponent(this.tip);
/* 353 */       if ((this.window != null) && (this.window != localWindow)) {
/* 354 */         this.window.addMouseListener(this);
/*     */       }
/*     */       else {
/* 357 */         this.window = null;
/*     */       }
/*     */ 
/* 360 */       this.insideTimer.start();
/* 361 */       this.tipShowing = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   void hideTipWindow() {
/* 366 */     if (this.tipWindow != null) {
/* 367 */       if (this.window != null) {
/* 368 */         this.window.removeMouseListener(this);
/* 369 */         this.window = null;
/*     */       }
/* 371 */       this.tipWindow.hide();
/* 372 */       this.tipWindow = null;
/* 373 */       this.tipShowing = false;
/* 374 */       this.tip = null;
/* 375 */       this.insideTimer.stop();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static ToolTipManager sharedInstance()
/*     */   {
/* 385 */     Object localObject = SwingUtilities.appContextGet(TOOL_TIP_MANAGER_KEY);
/* 386 */     if ((localObject instanceof ToolTipManager)) {
/* 387 */       return (ToolTipManager)localObject;
/*     */     }
/* 389 */     ToolTipManager localToolTipManager = new ToolTipManager();
/* 390 */     SwingUtilities.appContextPut(TOOL_TIP_MANAGER_KEY, localToolTipManager);
/* 391 */     return localToolTipManager;
/*     */   }
/*     */ 
/*     */   public void registerComponent(JComponent paramJComponent)
/*     */   {
/* 408 */     paramJComponent.removeMouseListener(this);
/* 409 */     paramJComponent.addMouseListener(this);
/* 410 */     paramJComponent.removeMouseMotionListener(this.moveBeforeEnterListener);
/* 411 */     paramJComponent.addMouseMotionListener(this.moveBeforeEnterListener);
/* 412 */     paramJComponent.removeKeyListener(this.accessibilityKeyListener);
/* 413 */     paramJComponent.addKeyListener(this.accessibilityKeyListener);
/*     */   }
/*     */ 
/*     */   public void unregisterComponent(JComponent paramJComponent)
/*     */   {
/* 422 */     paramJComponent.removeMouseListener(this);
/* 423 */     paramJComponent.removeMouseMotionListener(this.moveBeforeEnterListener);
/* 424 */     paramJComponent.removeKeyListener(this.accessibilityKeyListener);
/*     */   }
/*     */ 
/*     */   public void mouseEntered(MouseEvent paramMouseEvent)
/*     */   {
/* 435 */     initiateToolTip(paramMouseEvent);
/*     */   }
/*     */ 
/*     */   private void initiateToolTip(MouseEvent paramMouseEvent) {
/* 439 */     if (paramMouseEvent.getSource() == this.window) {
/* 440 */       return;
/*     */     }
/* 442 */     JComponent localJComponent = (JComponent)paramMouseEvent.getSource();
/* 443 */     localJComponent.removeMouseMotionListener(this.moveBeforeEnterListener);
/*     */ 
/* 445 */     this.exitTimer.stop();
/*     */ 
/* 447 */     Point localPoint1 = paramMouseEvent.getPoint();
/*     */ 
/* 449 */     if ((localPoint1.x < 0) || (localPoint1.x >= localJComponent.getWidth()) || (localPoint1.y < 0) || (localPoint1.y >= localJComponent.getHeight()))
/*     */     {
/* 453 */       return;
/*     */     }
/*     */ 
/* 456 */     if (this.insideComponent != null) {
/* 457 */       this.enterTimer.stop();
/*     */     }
/*     */ 
/* 462 */     localJComponent.removeMouseMotionListener(this);
/* 463 */     localJComponent.addMouseMotionListener(this);
/*     */ 
/* 465 */     int i = this.insideComponent == localJComponent ? 1 : 0;
/*     */ 
/* 467 */     this.insideComponent = localJComponent;
/* 468 */     if (this.tipWindow != null) {
/* 469 */       this.mouseEvent = paramMouseEvent;
/* 470 */       if (this.showImmediately) {
/* 471 */         String str = localJComponent.getToolTipText(paramMouseEvent);
/* 472 */         Point localPoint2 = localJComponent.getToolTipLocation(paramMouseEvent);
/*     */ 
/* 474 */         int j = localPoint2 == null ? 1 : this.preferredLocation != null ? this.preferredLocation.equals(localPoint2) : 0;
/*     */ 
/* 478 */         if ((i == 0) || (!this.toolTipText.equals(str)) || (j == 0))
/*     */         {
/* 480 */           this.toolTipText = str;
/* 481 */           this.preferredLocation = localPoint2;
/* 482 */           showTipWindow();
/*     */         }
/*     */       } else {
/* 485 */         this.enterTimer.start();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mouseExited(MouseEvent paramMouseEvent)
/*     */   {
/* 498 */     int i = 1;
/*     */     Object localObject;
/*     */     Point localPoint1;
/* 499 */     if ((this.insideComponent != null) || (
/* 502 */       (this.window != null) && (paramMouseEvent.getSource() == this.window) && (this.insideComponent != null)))
/*     */     {
/* 505 */       localObject = this.insideComponent.getTopLevelAncestor();
/*     */ 
/* 507 */       if (localObject != null) {
/* 508 */         localPoint1 = paramMouseEvent.getPoint();
/* 509 */         SwingUtilities.convertPointToScreen(localPoint1, this.window);
/*     */ 
/* 511 */         localPoint1.x -= ((Container)localObject).getX();
/* 512 */         localPoint1.y -= ((Container)localObject).getY();
/*     */ 
/* 514 */         localPoint1 = SwingUtilities.convertPoint(null, localPoint1, this.insideComponent);
/* 515 */         if ((localPoint1.x >= 0) && (localPoint1.x < this.insideComponent.getWidth()) && (localPoint1.y >= 0) && (localPoint1.y < this.insideComponent.getHeight()))
/*     */         {
/* 517 */           i = 0;
/*     */         }
/* 519 */         else i = 1;
/*     */       }
/*     */     }
/* 522 */     else if ((paramMouseEvent.getSource() == this.insideComponent) && (this.tipWindow != null)) {
/* 523 */       localObject = SwingUtilities.getWindowAncestor(this.insideComponent);
/* 524 */       if (localObject != null) {
/* 525 */         localPoint1 = SwingUtilities.convertPoint(this.insideComponent, paramMouseEvent.getPoint(), (Component)localObject);
/*     */ 
/* 528 */         Rectangle localRectangle = this.insideComponent.getTopLevelAncestor().getBounds();
/* 529 */         localPoint1.x += localRectangle.x;
/* 530 */         localPoint1.y += localRectangle.y;
/*     */ 
/* 532 */         Point localPoint2 = new Point(0, 0);
/* 533 */         SwingUtilities.convertPointToScreen(localPoint2, this.tip);
/* 534 */         localRectangle.x = localPoint2.x;
/* 535 */         localRectangle.y = localPoint2.y;
/* 536 */         localRectangle.width = this.tip.getWidth();
/* 537 */         localRectangle.height = this.tip.getHeight();
/*     */ 
/* 539 */         if ((localPoint1.x >= localRectangle.x) && (localPoint1.x < localRectangle.x + localRectangle.width) && (localPoint1.y >= localRectangle.y) && (localPoint1.y < localRectangle.y + localRectangle.height))
/*     */         {
/* 541 */           i = 0;
/*     */         }
/* 543 */         else i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 548 */     if (i != 0) {
/* 549 */       this.enterTimer.stop();
/* 550 */       if (this.insideComponent != null) {
/* 551 */         this.insideComponent.removeMouseMotionListener(this);
/*     */       }
/* 553 */       this.insideComponent = null;
/* 554 */       this.toolTipText = null;
/* 555 */       this.mouseEvent = null;
/* 556 */       hideTipWindow();
/* 557 */       this.exitTimer.restart();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void mousePressed(MouseEvent paramMouseEvent)
/*     */   {
/* 569 */     hideTipWindow();
/* 570 */     this.enterTimer.stop();
/* 571 */     this.showImmediately = false;
/* 572 */     this.insideComponent = null;
/* 573 */     this.mouseEvent = null;
/*     */   }
/*     */ 
/*     */   public void mouseDragged(MouseEvent paramMouseEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void mouseMoved(MouseEvent paramMouseEvent)
/*     */   {
/* 594 */     if (this.tipShowing) {
/* 595 */       checkForTipChange(paramMouseEvent);
/*     */     }
/* 597 */     else if (this.showImmediately) {
/* 598 */       JComponent localJComponent = (JComponent)paramMouseEvent.getSource();
/* 599 */       this.toolTipText = localJComponent.getToolTipText(paramMouseEvent);
/* 600 */       if (this.toolTipText != null) {
/* 601 */         this.preferredLocation = localJComponent.getToolTipLocation(paramMouseEvent);
/* 602 */         this.mouseEvent = paramMouseEvent;
/* 603 */         this.insideComponent = localJComponent;
/* 604 */         this.exitTimer.stop();
/* 605 */         showTipWindow();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 610 */       this.insideComponent = ((JComponent)paramMouseEvent.getSource());
/* 611 */       this.mouseEvent = paramMouseEvent;
/* 612 */       this.toolTipText = null;
/* 613 */       this.enterTimer.restart();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkForTipChange(MouseEvent paramMouseEvent)
/*     */   {
/* 622 */     JComponent localJComponent = (JComponent)paramMouseEvent.getSource();
/* 623 */     String str = localJComponent.getToolTipText(paramMouseEvent);
/* 624 */     Point localPoint = localJComponent.getToolTipLocation(paramMouseEvent);
/*     */ 
/* 626 */     if ((str != null) || (localPoint != null)) {
/* 627 */       this.mouseEvent = paramMouseEvent;
/* 628 */       if (((str != null) && (str.equals(this.toolTipText))) || ((str == null) && (((localPoint != null) && (localPoint.equals(this.preferredLocation))) || (localPoint == null))))
/*     */       {
/* 631 */         if (this.tipWindow != null)
/* 632 */           this.insideTimer.restart();
/*     */         else
/* 634 */           this.enterTimer.restart();
/*     */       }
/*     */       else {
/* 637 */         this.toolTipText = str;
/* 638 */         this.preferredLocation = localPoint;
/* 639 */         if (this.showImmediately) {
/* 640 */           hideTipWindow();
/* 641 */           showTipWindow();
/* 642 */           this.exitTimer.stop();
/*     */         } else {
/* 644 */           this.enterTimer.restart();
/*     */         }
/*     */       }
/*     */     } else {
/* 648 */       this.toolTipText = null;
/* 649 */       this.preferredLocation = null;
/* 650 */       this.mouseEvent = null;
/* 651 */       this.insideComponent = null;
/* 652 */       hideTipWindow();
/* 653 */       this.enterTimer.stop();
/* 654 */       this.exitTimer.restart();
/*     */     }
/*     */   }
/*     */ 
/*     */   static Frame frameForComponent(Component paramComponent)
/*     */   {
/* 713 */     while (!(paramComponent instanceof Frame)) {
/* 714 */       paramComponent = paramComponent.getParent();
/*     */     }
/* 716 */     return (Frame)paramComponent;
/*     */   }
/*     */ 
/*     */   private FocusListener createFocusChangeListener() {
/* 720 */     return new FocusAdapter() {
/*     */       public void focusLost(FocusEvent paramAnonymousFocusEvent) {
/* 722 */         ToolTipManager.this.hideTipWindow();
/* 723 */         ToolTipManager.this.insideComponent = null;
/* 724 */         JComponent localJComponent = (JComponent)paramAnonymousFocusEvent.getSource();
/* 725 */         localJComponent.removeFocusListener(ToolTipManager.this.focusChangeListener);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private int getPopupFitWidth(Rectangle paramRectangle, Component paramComponent)
/*     */   {
/* 734 */     if (paramComponent != null)
/*     */     {
/* 736 */       for (Container localContainer = paramComponent.getParent(); localContainer != null; localContainer = localContainer.getParent())
/*     */       {
/* 738 */         if (((localContainer instanceof JFrame)) || ((localContainer instanceof JDialog)) || ((localContainer instanceof JWindow)))
/*     */         {
/* 740 */           return getWidthAdjust(localContainer.getBounds(), paramRectangle);
/* 741 */         }if (((localContainer instanceof JApplet)) || ((localContainer instanceof JInternalFrame))) {
/* 742 */           if (this.popupFrameRect == null) {
/* 743 */             this.popupFrameRect = new Rectangle();
/*     */           }
/* 745 */           Point localPoint = localContainer.getLocationOnScreen();
/* 746 */           this.popupFrameRect.setBounds(localPoint.x, localPoint.y, localContainer.getBounds().width, localContainer.getBounds().height);
/*     */ 
/* 749 */           return getWidthAdjust(this.popupFrameRect, paramRectangle);
/*     */         }
/*     */       }
/*     */     }
/* 753 */     return 0;
/*     */   }
/*     */ 
/*     */   private int getPopupFitHeight(Rectangle paramRectangle, Component paramComponent)
/*     */   {
/* 759 */     if (paramComponent != null)
/*     */     {
/* 761 */       for (Container localContainer = paramComponent.getParent(); localContainer != null; localContainer = localContainer.getParent()) {
/* 762 */         if (((localContainer instanceof JFrame)) || ((localContainer instanceof JDialog)) || ((localContainer instanceof JWindow)))
/*     */         {
/* 764 */           return getHeightAdjust(localContainer.getBounds(), paramRectangle);
/* 765 */         }if (((localContainer instanceof JApplet)) || ((localContainer instanceof JInternalFrame))) {
/* 766 */           if (this.popupFrameRect == null) {
/* 767 */             this.popupFrameRect = new Rectangle();
/*     */           }
/* 769 */           Point localPoint = localContainer.getLocationOnScreen();
/* 770 */           this.popupFrameRect.setBounds(localPoint.x, localPoint.y, localContainer.getBounds().width, localContainer.getBounds().height);
/*     */ 
/* 773 */           return getHeightAdjust(this.popupFrameRect, paramRectangle);
/*     */         }
/*     */       }
/*     */     }
/* 777 */     return 0;
/*     */   }
/*     */ 
/*     */   private int getHeightAdjust(Rectangle paramRectangle1, Rectangle paramRectangle2) {
/* 781 */     if ((paramRectangle2.y >= paramRectangle1.y) && (paramRectangle2.y + paramRectangle2.height <= paramRectangle1.y + paramRectangle1.height)) {
/* 782 */       return 0;
/*     */     }
/* 784 */     return paramRectangle2.y + paramRectangle2.height - (paramRectangle1.y + paramRectangle1.height) + 5;
/*     */   }
/*     */ 
/*     */   private int getWidthAdjust(Rectangle paramRectangle1, Rectangle paramRectangle2)
/*     */   {
/* 793 */     if ((paramRectangle2.x >= paramRectangle1.x) && (paramRectangle2.x + paramRectangle2.width <= paramRectangle1.x + paramRectangle1.width)) {
/* 794 */       return 0;
/*     */     }
/*     */ 
/* 797 */     return paramRectangle2.x + paramRectangle2.width - (paramRectangle1.x + paramRectangle1.width) + 5;
/*     */   }
/*     */ 
/*     */   private void show(JComponent paramJComponent)
/*     */   {
/* 806 */     if (this.tipWindow != null) {
/* 807 */       hideTipWindow();
/* 808 */       this.insideComponent = null;
/*     */     }
/*     */     else {
/* 811 */       hideTipWindow();
/* 812 */       this.enterTimer.stop();
/* 813 */       this.exitTimer.stop();
/* 814 */       this.insideTimer.stop();
/* 815 */       this.insideComponent = paramJComponent;
/* 816 */       if (this.insideComponent != null) {
/* 817 */         this.toolTipText = this.insideComponent.getToolTipText();
/* 818 */         this.preferredLocation = new Point(10, this.insideComponent.getHeight() + 10);
/*     */ 
/* 820 */         showTipWindow();
/*     */ 
/* 822 */         if (this.focusChangeListener == null) {
/* 823 */           this.focusChangeListener = createFocusChangeListener();
/*     */         }
/* 825 */         this.insideComponent.addFocusListener(this.focusChangeListener);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void hide(JComponent paramJComponent) {
/* 831 */     hideTipWindow();
/* 832 */     paramJComponent.removeFocusListener(this.focusChangeListener);
/* 833 */     this.preferredLocation = null;
/* 834 */     this.insideComponent = null;
/*     */   }
/*     */ 
/*     */   private class AccessibilityKeyListener extends KeyAdapter
/*     */   {
/*     */     private AccessibilityKeyListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void keyPressed(KeyEvent paramKeyEvent)
/*     */     {
/* 846 */       if (!paramKeyEvent.isConsumed()) {
/* 847 */         JComponent localJComponent = (JComponent)paramKeyEvent.getComponent();
/* 848 */         KeyStroke localKeyStroke = KeyStroke.getKeyStrokeForEvent(paramKeyEvent);
/* 849 */         if (ToolTipManager.this.hideTip.equals(localKeyStroke)) {
/* 850 */           if (ToolTipManager.this.tipWindow != null) {
/* 851 */             ToolTipManager.this.hide(localJComponent);
/* 852 */             paramKeyEvent.consume();
/*     */           }
/* 854 */         } else if (ToolTipManager.this.postTip.equals(localKeyStroke))
/*     */         {
/* 856 */           ToolTipManager.this.show(localJComponent);
/* 857 */           paramKeyEvent.consume();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class MoveBeforeEnterListener extends MouseMotionAdapter
/*     */   {
/*     */     private MoveBeforeEnterListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent)
/*     */     {
/* 708 */       ToolTipManager.this.initiateToolTip(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class insideTimerAction
/*     */     implements ActionListener
/*     */   {
/*     */     protected insideTimerAction()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 660 */       if ((ToolTipManager.this.insideComponent != null) && (ToolTipManager.this.insideComponent.isShowing()))
/*     */       {
/* 662 */         if ((ToolTipManager.this.toolTipText == null) && (ToolTipManager.this.mouseEvent != null)) {
/* 663 */           ToolTipManager.this.toolTipText = ToolTipManager.this.insideComponent.getToolTipText(ToolTipManager.this.mouseEvent);
/* 664 */           ToolTipManager.this.preferredLocation = ToolTipManager.this.insideComponent.getToolTipLocation(ToolTipManager.this.mouseEvent);
/*     */         }
/*     */ 
/* 667 */         if (ToolTipManager.this.toolTipText != null) {
/* 668 */           ToolTipManager.this.showImmediately = true;
/* 669 */           ToolTipManager.this.showTipWindow();
/*     */         }
/*     */         else {
/* 672 */           ToolTipManager.this.insideComponent = null;
/* 673 */           ToolTipManager.this.toolTipText = null;
/* 674 */           ToolTipManager.this.preferredLocation = null;
/* 675 */           ToolTipManager.this.mouseEvent = null;
/* 676 */           ToolTipManager.this.hideTipWindow();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class outsideTimerAction implements ActionListener { protected outsideTimerAction() {  }
/*     */ 
/*     */ 
/* 684 */     public void actionPerformed(ActionEvent paramActionEvent) { ToolTipManager.this.showImmediately = false; }  } 
/*     */   protected class stillInsideTimerAction implements ActionListener {
/*     */     protected stillInsideTimerAction() {
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 690 */       ToolTipManager.this.hideTipWindow();
/* 691 */       ToolTipManager.this.enterTimer.stop();
/* 692 */       ToolTipManager.this.showImmediately = false;
/* 693 */       ToolTipManager.this.insideComponent = null;
/* 694 */       ToolTipManager.this.mouseEvent = null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ToolTipManager
 * JD-Core Version:    0.6.2
 */
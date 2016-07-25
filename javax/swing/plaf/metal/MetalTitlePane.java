/*      */ package javax.swing.plaf.metal;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JMenuBar;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JRootPane;
/*      */ import javax.swing.JSeparator;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ class MetalTitlePane extends JComponent
/*      */ {
/*   53 */   private static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);
/*      */   private static final int IMAGE_HEIGHT = 16;
/*      */   private static final int IMAGE_WIDTH = 16;
/*      */   private PropertyChangeListener propertyChangeListener;
/*      */   private JMenuBar menuBar;
/*      */   private Action closeAction;
/*      */   private Action iconifyAction;
/*      */   private Action restoreAction;
/*      */   private Action maximizeAction;
/*      */   private JButton toggleButton;
/*      */   private JButton iconifyButton;
/*      */   private JButton closeButton;
/*      */   private Icon maximizeIcon;
/*      */   private Icon minimizeIcon;
/*      */   private Image systemIcon;
/*      */   private WindowListener windowListener;
/*      */   private Window window;
/*      */   private JRootPane rootPane;
/*      */   private int buttonsWidth;
/*      */   private int state;
/*      */   private MetalRootPaneUI rootPaneUI;
/*  150 */   private Color inactiveBackground = UIManager.getColor("inactiveCaption");
/*  151 */   private Color inactiveForeground = UIManager.getColor("inactiveCaptionText");
/*  152 */   private Color inactiveShadow = UIManager.getColor("inactiveCaptionBorder");
/*  153 */   private Color activeBumpsHighlight = MetalLookAndFeel.getPrimaryControlHighlight();
/*  154 */   private Color activeBumpsShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*  155 */   private Color activeBackground = null;
/*  156 */   private Color activeForeground = null;
/*  157 */   private Color activeShadow = null;
/*      */ 
/*  160 */   private MetalBumps activeBumps = new MetalBumps(0, 0, this.activeBumpsHighlight, this.activeBumpsShadow, MetalLookAndFeel.getPrimaryControl());
/*      */ 
/*  165 */   private MetalBumps inactiveBumps = new MetalBumps(0, 0, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), MetalLookAndFeel.getControl());
/*      */ 
/*      */   public MetalTitlePane(JRootPane paramJRootPane, MetalRootPaneUI paramMetalRootPaneUI)
/*      */   {
/*  173 */     this.rootPane = paramJRootPane;
/*  174 */     this.rootPaneUI = paramMetalRootPaneUI;
/*      */ 
/*  176 */     this.state = -1;
/*      */ 
/*  178 */     installSubcomponents();
/*  179 */     determineColors();
/*  180 */     installDefaults();
/*      */ 
/*  182 */     setLayout(createLayout());
/*      */   }
/*      */ 
/*      */   private void uninstall()
/*      */   {
/*  189 */     uninstallListeners();
/*  190 */     this.window = null;
/*  191 */     removeAll();
/*      */   }
/*      */ 
/*      */   private void installListeners()
/*      */   {
/*  198 */     if (this.window != null) {
/*  199 */       this.windowListener = createWindowListener();
/*  200 */       this.window.addWindowListener(this.windowListener);
/*  201 */       this.propertyChangeListener = createWindowPropertyChangeListener();
/*  202 */       this.window.addPropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void uninstallListeners()
/*      */   {
/*  210 */     if (this.window != null) {
/*  211 */       this.window.removeWindowListener(this.windowListener);
/*  212 */       this.window.removePropertyChangeListener(this.propertyChangeListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   private WindowListener createWindowListener()
/*      */   {
/*  221 */     return new WindowHandler(null);
/*      */   }
/*      */ 
/*      */   private PropertyChangeListener createWindowPropertyChangeListener()
/*      */   {
/*  229 */     return new PropertyChangeHandler(null);
/*      */   }
/*      */ 
/*      */   public JRootPane getRootPane()
/*      */   {
/*  236 */     return this.rootPane;
/*      */   }
/*      */ 
/*      */   private int getWindowDecorationStyle()
/*      */   {
/*  243 */     return getRootPane().getWindowDecorationStyle();
/*      */   }
/*      */ 
/*      */   public void addNotify() {
/*  247 */     super.addNotify();
/*      */ 
/*  249 */     uninstallListeners();
/*      */ 
/*  251 */     this.window = SwingUtilities.getWindowAncestor(this);
/*  252 */     if (this.window != null) {
/*  253 */       if ((this.window instanceof Frame)) {
/*  254 */         setState(((Frame)this.window).getExtendedState());
/*      */       }
/*      */       else {
/*  257 */         setState(0);
/*      */       }
/*  259 */       setActive(this.window.isActive());
/*  260 */       installListeners();
/*  261 */       updateSystemIcon();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotify() {
/*  266 */     super.removeNotify();
/*      */ 
/*  268 */     uninstallListeners();
/*  269 */     this.window = null;
/*      */   }
/*      */ 
/*      */   private void installSubcomponents()
/*      */   {
/*  276 */     int i = getWindowDecorationStyle();
/*  277 */     if (i == 1) {
/*  278 */       createActions();
/*  279 */       this.menuBar = createMenuBar();
/*  280 */       add(this.menuBar);
/*  281 */       createButtons();
/*  282 */       add(this.iconifyButton);
/*  283 */       add(this.toggleButton);
/*  284 */       add(this.closeButton);
/*  285 */     } else if ((i == 2) || (i == 3) || (i == 4) || (i == 5) || (i == 6) || (i == 7) || (i == 8))
/*      */     {
/*  292 */       createActions();
/*  293 */       createButtons();
/*  294 */       add(this.closeButton);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void determineColors()
/*      */   {
/*  302 */     switch (getWindowDecorationStyle()) {
/*      */     case 1:
/*  304 */       this.activeBackground = UIManager.getColor("activeCaption");
/*  305 */       this.activeForeground = UIManager.getColor("activeCaptionText");
/*  306 */       this.activeShadow = UIManager.getColor("activeCaptionBorder");
/*  307 */       break;
/*      */     case 4:
/*  309 */       this.activeBackground = UIManager.getColor("OptionPane.errorDialog.titlePane.background");
/*      */ 
/*  311 */       this.activeForeground = UIManager.getColor("OptionPane.errorDialog.titlePane.foreground");
/*      */ 
/*  313 */       this.activeShadow = UIManager.getColor("OptionPane.errorDialog.titlePane.shadow");
/*      */ 
/*  315 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*  319 */       this.activeBackground = UIManager.getColor("OptionPane.questionDialog.titlePane.background");
/*      */ 
/*  321 */       this.activeForeground = UIManager.getColor("OptionPane.questionDialog.titlePane.foreground");
/*      */ 
/*  323 */       this.activeShadow = UIManager.getColor("OptionPane.questionDialog.titlePane.shadow");
/*      */ 
/*  325 */       break;
/*      */     case 8:
/*  327 */       this.activeBackground = UIManager.getColor("OptionPane.warningDialog.titlePane.background");
/*      */ 
/*  329 */       this.activeForeground = UIManager.getColor("OptionPane.warningDialog.titlePane.foreground");
/*      */ 
/*  331 */       this.activeShadow = UIManager.getColor("OptionPane.warningDialog.titlePane.shadow");
/*      */ 
/*  333 */       break;
/*      */     case 2:
/*      */     case 3:
/*      */     default:
/*  337 */       this.activeBackground = UIManager.getColor("activeCaption");
/*  338 */       this.activeForeground = UIManager.getColor("activeCaptionText");
/*  339 */       this.activeShadow = UIManager.getColor("activeCaptionBorder");
/*      */     }
/*      */ 
/*  342 */     this.activeBumps.setBumpColors(this.activeBumpsHighlight, this.activeBumpsShadow, this.activeBackground);
/*      */   }
/*      */ 
/*      */   private void installDefaults()
/*      */   {
/*  350 */     setFont(UIManager.getFont("InternalFrame.titleFont", getLocale()));
/*      */   }
/*      */ 
/*      */   private void uninstallDefaults()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected JMenuBar createMenuBar()
/*      */   {
/*  364 */     this.menuBar = new SystemMenuBar(null);
/*  365 */     this.menuBar.setFocusable(false);
/*  366 */     this.menuBar.setBorderPainted(true);
/*  367 */     this.menuBar.add(createMenu());
/*  368 */     return this.menuBar;
/*      */   }
/*      */ 
/*      */   private void close()
/*      */   {
/*  375 */     Window localWindow = getWindow();
/*      */ 
/*  377 */     if (localWindow != null)
/*  378 */       localWindow.dispatchEvent(new WindowEvent(localWindow, 201));
/*      */   }
/*      */ 
/*      */   private void iconify()
/*      */   {
/*  387 */     Frame localFrame = getFrame();
/*  388 */     if (localFrame != null)
/*  389 */       localFrame.setExtendedState(this.state | 0x1);
/*      */   }
/*      */ 
/*      */   private void maximize()
/*      */   {
/*  397 */     Frame localFrame = getFrame();
/*  398 */     if (localFrame != null)
/*  399 */       localFrame.setExtendedState(this.state | 0x6);
/*      */   }
/*      */ 
/*      */   private void restore()
/*      */   {
/*  407 */     Frame localFrame = getFrame();
/*      */ 
/*  409 */     if (localFrame == null) {
/*  410 */       return;
/*      */     }
/*      */ 
/*  413 */     if ((this.state & 0x1) != 0)
/*  414 */       localFrame.setExtendedState(this.state & 0xFFFFFFFE);
/*      */     else
/*  416 */       localFrame.setExtendedState(this.state & 0xFFFFFFF9);
/*      */   }
/*      */ 
/*      */   private void createActions()
/*      */   {
/*  425 */     this.closeAction = new CloseAction();
/*  426 */     if (getWindowDecorationStyle() == 1) {
/*  427 */       this.iconifyAction = new IconifyAction();
/*  428 */       this.restoreAction = new RestoreAction();
/*  429 */       this.maximizeAction = new MaximizeAction();
/*      */     }
/*      */   }
/*      */ 
/*      */   private JMenu createMenu()
/*      */   {
/*  438 */     JMenu localJMenu = new JMenu("");
/*  439 */     if (getWindowDecorationStyle() == 1) {
/*  440 */       addMenuItems(localJMenu);
/*      */     }
/*  442 */     return localJMenu;
/*      */   }
/*      */ 
/*      */   private void addMenuItems(JMenu paramJMenu)
/*      */   {
/*  449 */     Locale localLocale = getRootPane().getLocale();
/*  450 */     JMenuItem localJMenuItem = paramJMenu.add(this.restoreAction);
/*  451 */     int i = MetalUtils.getInt("MetalTitlePane.restoreMnemonic", -1);
/*      */ 
/*  453 */     if (i != -1) {
/*  454 */       localJMenuItem.setMnemonic(i);
/*      */     }
/*      */ 
/*  457 */     localJMenuItem = paramJMenu.add(this.iconifyAction);
/*  458 */     i = MetalUtils.getInt("MetalTitlePane.iconifyMnemonic", -1);
/*  459 */     if (i != -1) {
/*  460 */       localJMenuItem.setMnemonic(i);
/*      */     }
/*      */ 
/*  463 */     if (Toolkit.getDefaultToolkit().isFrameStateSupported(6))
/*      */     {
/*  465 */       localJMenuItem = paramJMenu.add(this.maximizeAction);
/*  466 */       i = MetalUtils.getInt("MetalTitlePane.maximizeMnemonic", -1);
/*      */ 
/*  468 */       if (i != -1) {
/*  469 */         localJMenuItem.setMnemonic(i);
/*      */       }
/*      */     }
/*      */ 
/*  473 */     paramJMenu.add(new JSeparator());
/*      */ 
/*  475 */     localJMenuItem = paramJMenu.add(this.closeAction);
/*  476 */     i = MetalUtils.getInt("MetalTitlePane.closeMnemonic", -1);
/*  477 */     if (i != -1)
/*  478 */       localJMenuItem.setMnemonic(i);
/*      */   }
/*      */ 
/*      */   private JButton createTitleButton()
/*      */   {
/*  487 */     JButton localJButton = new JButton();
/*      */ 
/*  489 */     localJButton.setFocusPainted(false);
/*  490 */     localJButton.setFocusable(false);
/*  491 */     localJButton.setOpaque(true);
/*  492 */     return localJButton;
/*      */   }
/*      */ 
/*      */   private void createButtons()
/*      */   {
/*  499 */     this.closeButton = createTitleButton();
/*  500 */     this.closeButton.setAction(this.closeAction);
/*  501 */     this.closeButton.setText(null);
/*  502 */     this.closeButton.putClientProperty("paintActive", Boolean.TRUE);
/*  503 */     this.closeButton.setBorder(handyEmptyBorder);
/*  504 */     this.closeButton.putClientProperty("AccessibleName", "Close");
/*      */ 
/*  506 */     this.closeButton.setIcon(UIManager.getIcon("InternalFrame.closeIcon"));
/*      */ 
/*  508 */     if (getWindowDecorationStyle() == 1) {
/*  509 */       this.maximizeIcon = UIManager.getIcon("InternalFrame.maximizeIcon");
/*  510 */       this.minimizeIcon = UIManager.getIcon("InternalFrame.minimizeIcon");
/*      */ 
/*  512 */       this.iconifyButton = createTitleButton();
/*  513 */       this.iconifyButton.setAction(this.iconifyAction);
/*  514 */       this.iconifyButton.setText(null);
/*  515 */       this.iconifyButton.putClientProperty("paintActive", Boolean.TRUE);
/*  516 */       this.iconifyButton.setBorder(handyEmptyBorder);
/*  517 */       this.iconifyButton.putClientProperty("AccessibleName", "Iconify");
/*      */ 
/*  519 */       this.iconifyButton.setIcon(UIManager.getIcon("InternalFrame.iconifyIcon"));
/*      */ 
/*  521 */       this.toggleButton = createTitleButton();
/*  522 */       this.toggleButton.setAction(this.restoreAction);
/*  523 */       this.toggleButton.putClientProperty("paintActive", Boolean.TRUE);
/*  524 */       this.toggleButton.setBorder(handyEmptyBorder);
/*  525 */       this.toggleButton.putClientProperty("AccessibleName", "Maximize");
/*      */ 
/*  527 */       this.toggleButton.setIcon(this.maximizeIcon);
/*      */     }
/*      */   }
/*      */ 
/*      */   private LayoutManager createLayout()
/*      */   {
/*  536 */     return new TitlePaneLayout(null);
/*      */   }
/*      */ 
/*      */   private void setActive(boolean paramBoolean)
/*      */   {
/*  543 */     Boolean localBoolean = paramBoolean ? Boolean.TRUE : Boolean.FALSE;
/*      */ 
/*  545 */     this.closeButton.putClientProperty("paintActive", localBoolean);
/*  546 */     if (getWindowDecorationStyle() == 1) {
/*  547 */       this.iconifyButton.putClientProperty("paintActive", localBoolean);
/*  548 */       this.toggleButton.putClientProperty("paintActive", localBoolean);
/*      */     }
/*      */ 
/*  552 */     getRootPane().repaint();
/*      */   }
/*      */ 
/*      */   private void setState(int paramInt)
/*      */   {
/*  559 */     setState(paramInt, false);
/*      */   }
/*      */ 
/*      */   private void setState(int paramInt, boolean paramBoolean)
/*      */   {
/*  567 */     Window localWindow = getWindow();
/*      */ 
/*  569 */     if ((localWindow != null) && (getWindowDecorationStyle() == 1)) {
/*  570 */       if ((this.state == paramInt) && (!paramBoolean)) {
/*  571 */         return;
/*      */       }
/*  573 */       Frame localFrame = getFrame();
/*      */ 
/*  575 */       if (localFrame != null) {
/*  576 */         JRootPane localJRootPane = getRootPane();
/*      */ 
/*  578 */         if (((paramInt & 0x6) != 0) && ((localJRootPane.getBorder() == null) || ((localJRootPane.getBorder() instanceof UIResource))) && (localFrame.isShowing()))
/*      */         {
/*  582 */           localJRootPane.setBorder(null);
/*      */         }
/*  584 */         else if ((paramInt & 0x6) == 0)
/*      */         {
/*  587 */           this.rootPaneUI.installBorder(localJRootPane);
/*      */         }
/*  589 */         if (localFrame.isResizable()) {
/*  590 */           if ((paramInt & 0x6) != 0) {
/*  591 */             updateToggleButton(this.restoreAction, this.minimizeIcon);
/*  592 */             this.maximizeAction.setEnabled(false);
/*  593 */             this.restoreAction.setEnabled(true);
/*      */           }
/*      */           else {
/*  596 */             updateToggleButton(this.maximizeAction, this.maximizeIcon);
/*  597 */             this.maximizeAction.setEnabled(true);
/*  598 */             this.restoreAction.setEnabled(false);
/*      */           }
/*  600 */           if ((this.toggleButton.getParent() == null) || (this.iconifyButton.getParent() == null))
/*      */           {
/*  602 */             add(this.toggleButton);
/*  603 */             add(this.iconifyButton);
/*  604 */             revalidate();
/*  605 */             repaint();
/*      */           }
/*  607 */           this.toggleButton.setText(null);
/*      */         }
/*      */         else {
/*  610 */           this.maximizeAction.setEnabled(false);
/*  611 */           this.restoreAction.setEnabled(false);
/*  612 */           if (this.toggleButton.getParent() != null) {
/*  613 */             remove(this.toggleButton);
/*  614 */             revalidate();
/*  615 */             repaint();
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  621 */         this.maximizeAction.setEnabled(false);
/*  622 */         this.restoreAction.setEnabled(false);
/*  623 */         this.iconifyAction.setEnabled(false);
/*  624 */         remove(this.toggleButton);
/*  625 */         remove(this.iconifyButton);
/*  626 */         revalidate();
/*  627 */         repaint();
/*      */       }
/*  629 */       this.closeAction.setEnabled(true);
/*  630 */       this.state = paramInt;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateToggleButton(Action paramAction, Icon paramIcon)
/*      */   {
/*  639 */     this.toggleButton.setAction(paramAction);
/*  640 */     this.toggleButton.setIcon(paramIcon);
/*  641 */     this.toggleButton.setText(null);
/*      */   }
/*      */ 
/*      */   private Frame getFrame()
/*      */   {
/*  649 */     Window localWindow = getWindow();
/*      */ 
/*  651 */     if ((localWindow instanceof Frame)) {
/*  652 */       return (Frame)localWindow;
/*      */     }
/*  654 */     return null;
/*      */   }
/*      */ 
/*      */   private Window getWindow()
/*      */   {
/*  663 */     return this.window;
/*      */   }
/*      */ 
/*      */   private String getTitle()
/*      */   {
/*  670 */     Window localWindow = getWindow();
/*      */ 
/*  672 */     if ((localWindow instanceof Frame)) {
/*  673 */       return ((Frame)localWindow).getTitle();
/*      */     }
/*  675 */     if ((localWindow instanceof Dialog)) {
/*  676 */       return ((Dialog)localWindow).getTitle();
/*      */     }
/*  678 */     return null;
/*      */   }
/*      */ 
/*      */   public void paintComponent(Graphics paramGraphics)
/*      */   {
/*  687 */     if (getFrame() != null) {
/*  688 */       setState(getFrame().getExtendedState());
/*      */     }
/*  690 */     JRootPane localJRootPane = getRootPane();
/*  691 */     Window localWindow = getWindow();
/*  692 */     boolean bool1 = localWindow == null ? localJRootPane.getComponentOrientation().isLeftToRight() : localWindow.getComponentOrientation().isLeftToRight();
/*      */ 
/*  695 */     boolean bool2 = localWindow == null ? true : localWindow.isActive();
/*  696 */     int i = getWidth();
/*  697 */     int j = getHeight();
/*      */     Color localColor1;
/*      */     Color localColor2;
/*      */     Color localColor3;
/*      */     MetalBumps localMetalBumps;
/*  705 */     if (bool2) {
/*  706 */       localColor1 = this.activeBackground;
/*  707 */       localColor2 = this.activeForeground;
/*  708 */       localColor3 = this.activeShadow;
/*  709 */       localMetalBumps = this.activeBumps;
/*      */     } else {
/*  711 */       localColor1 = this.inactiveBackground;
/*  712 */       localColor2 = this.inactiveForeground;
/*  713 */       localColor3 = this.inactiveShadow;
/*  714 */       localMetalBumps = this.inactiveBumps;
/*      */     }
/*      */ 
/*  717 */     paramGraphics.setColor(localColor1);
/*  718 */     paramGraphics.fillRect(0, 0, i, j);
/*      */ 
/*  720 */     paramGraphics.setColor(localColor3);
/*  721 */     paramGraphics.drawLine(0, j - 1, i, j - 1);
/*  722 */     paramGraphics.drawLine(0, 0, 0, 0);
/*  723 */     paramGraphics.drawLine(i - 1, 0, i - 1, 0);
/*      */ 
/*  725 */     FontMetrics localFontMetrics1 = bool1 ? 5 : i - 5;
/*      */ 
/*  727 */     if (getWindowDecorationStyle() == 1) {
/*  728 */       localFontMetrics1 += (bool1 ? 21 : -21);
/*      */     }
/*      */ 
/*  731 */     String str = getTitle();
/*      */     FontMetrics localFontMetrics2;
/*      */     int m;
/*  732 */     if (str != null) {
/*  733 */       localFontMetrics2 = SwingUtilities2.getFontMetrics(localJRootPane, paramGraphics);
/*      */ 
/*  735 */       paramGraphics.setColor(localColor2);
/*      */ 
/*  737 */       m = (j - localFontMetrics2.getHeight()) / 2 + localFontMetrics2.getAscent();
/*      */ 
/*  739 */       Rectangle localRectangle = new Rectangle(0, 0, 0, 0);
/*  740 */       if ((this.iconifyButton != null) && (this.iconifyButton.getParent() != null)) {
/*  741 */         localRectangle = this.iconifyButton.getBounds();
/*      */       }
/*      */ 
/*  745 */       if (bool1) {
/*  746 */         if (localRectangle.x == 0) {
/*  747 */           localRectangle.x = (localWindow.getWidth() - localWindow.getInsets().right - 2);
/*      */         }
/*  749 */         i1 = localRectangle.x - localFontMetrics1 - 4;
/*  750 */         str = SwingUtilities2.clipStringIfNecessary(localJRootPane, localFontMetrics2, str, i1);
/*      */       }
/*      */       else {
/*  753 */         i1 = localFontMetrics1 - localRectangle.x - localRectangle.width - 4;
/*  754 */         str = SwingUtilities2.clipStringIfNecessary(localJRootPane, localFontMetrics2, str, i1);
/*      */ 
/*  756 */         localFontMetrics1 -= SwingUtilities2.stringWidth(localJRootPane, localFontMetrics2, str);
/*      */       }
/*      */ 
/*  759 */       int i2 = SwingUtilities2.stringWidth(localJRootPane, localFontMetrics2, str);
/*      */ 
/*  761 */       SwingUtilities2.drawString(localJRootPane, paramGraphics, str, localFontMetrics1, m);
/*      */ 
/*  763 */       localFontMetrics1 += (bool1 ? i2 + 5 : -5);
/*      */     }
/*      */     int k;
/*  768 */     if (bool1) {
/*  769 */       m = i - this.buttonsWidth - localFontMetrics1 - 5;
/*  770 */       localFontMetrics2 = localFontMetrics1;
/*      */     } else {
/*  772 */       m = localFontMetrics1 - this.buttonsWidth - 5;
/*  773 */       k = this.buttonsWidth + 5;
/*      */     }
/*  775 */     int n = 3;
/*  776 */     int i1 = getHeight() - 2 * n;
/*  777 */     localMetalBumps.setBumpArea(m, i1);
/*  778 */     localMetalBumps.paintIcon(this, paramGraphics, k, n);
/*      */   }
/*      */ 
/*      */   private void updateSystemIcon()
/*      */   {
/* 1006 */     Window localWindow = getWindow();
/* 1007 */     if (localWindow == null) {
/* 1008 */       this.systemIcon = null;
/* 1009 */       return;
/*      */     }
/* 1011 */     List localList = localWindow.getIconImages();
/* 1012 */     assert (localList != null);
/*      */ 
/* 1014 */     if (localList.size() == 0) {
/* 1015 */       this.systemIcon = null;
/*      */     }
/* 1017 */     else if (localList.size() == 1) {
/* 1018 */       this.systemIcon = ((Image)localList.get(0));
/*      */     }
/*      */     else
/* 1021 */       this.systemIcon = SunToolkit.getScaledIconImage(localList, 16, 16);
/*      */   }
/*      */ 
/*      */   private class CloseAction extends AbstractAction
/*      */   {
/*      */     public CloseAction()
/*      */     {
/*  786 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  791 */       MetalTitlePane.this.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class IconifyAction extends AbstractAction
/*      */   {
/*      */     public IconifyAction()
/*      */     {
/*  801 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  806 */       MetalTitlePane.this.iconify();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MaximizeAction extends AbstractAction
/*      */   {
/*      */     public MaximizeAction()
/*      */     {
/*  831 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  836 */       MetalTitlePane.this.maximize();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class PropertyChangeHandler
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     private PropertyChangeHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  974 */       String str = paramPropertyChangeEvent.getPropertyName();
/*      */ 
/*  977 */       if (("resizable".equals(str)) || ("state".equals(str))) {
/*  978 */         Frame localFrame = MetalTitlePane.this.getFrame();
/*      */ 
/*  980 */         if (localFrame != null) {
/*  981 */           MetalTitlePane.this.setState(localFrame.getExtendedState(), true);
/*      */         }
/*  983 */         if ("resizable".equals(str)) {
/*  984 */           MetalTitlePane.this.getRootPane().repaint();
/*      */         }
/*      */       }
/*  987 */       else if ("title".equals(str)) {
/*  988 */         MetalTitlePane.this.repaint();
/*      */       }
/*  990 */       else if ("componentOrientation" == str) {
/*  991 */         MetalTitlePane.this.revalidate();
/*  992 */         MetalTitlePane.this.repaint();
/*      */       }
/*  994 */       else if ("iconImage" == str) {
/*  995 */         MetalTitlePane.this.updateSystemIcon();
/*  996 */         MetalTitlePane.this.revalidate();
/*  997 */         MetalTitlePane.this.repaint();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class RestoreAction extends AbstractAction
/*      */   {
/*      */     public RestoreAction()
/*      */     {
/*  816 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*  821 */       MetalTitlePane.this.restore();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SystemMenuBar extends JMenuBar
/*      */   {
/*      */     private SystemMenuBar()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void paint(Graphics paramGraphics)
/*      */     {
/*  848 */       if (isOpaque()) {
/*  849 */         paramGraphics.setColor(getBackground());
/*  850 */         paramGraphics.fillRect(0, 0, getWidth(), getHeight());
/*      */       }
/*      */ 
/*  853 */       if (MetalTitlePane.this.systemIcon != null) {
/*  854 */         paramGraphics.drawImage(MetalTitlePane.this.systemIcon, 0, 0, 16, 16, null);
/*      */       } else {
/*  856 */         Icon localIcon = UIManager.getIcon("InternalFrame.icon");
/*      */ 
/*  858 */         if (localIcon != null)
/*  859 */           localIcon.paintIcon(this, paramGraphics, 0, 0);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Dimension getMinimumSize() {
/*  864 */       return getPreferredSize();
/*      */     }
/*      */     public Dimension getPreferredSize() {
/*  867 */       Dimension localDimension = super.getPreferredSize();
/*      */ 
/*  869 */       return new Dimension(Math.max(16, localDimension.width), Math.max(localDimension.height, 16));
/*      */     }
/*      */   }
/*      */   private class TitlePaneLayout implements LayoutManager {
/*      */     private TitlePaneLayout() {
/*      */     }
/*      */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*      */     }
/*      */     public void removeLayoutComponent(Component paramComponent) {  }
/*      */ 
/*  878 */     public Dimension preferredLayoutSize(Container paramContainer) { int i = computeHeight();
/*  879 */       return new Dimension(i, i); }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer)
/*      */     {
/*  883 */       return preferredLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     private int computeHeight() {
/*  887 */       FontMetrics localFontMetrics = MetalTitlePane.this.rootPane.getFontMetrics(MetalTitlePane.this.getFont());
/*  888 */       int i = localFontMetrics.getHeight();
/*  889 */       i += 7;
/*  890 */       int j = 0;
/*  891 */       if (MetalTitlePane.this.getWindowDecorationStyle() == 1) {
/*  892 */         j = 16;
/*      */       }
/*      */ 
/*  895 */       int k = Math.max(i, j);
/*  896 */       return k;
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer) {
/*  900 */       boolean bool = MetalTitlePane.this.window == null ? MetalTitlePane.this.getRootPane().getComponentOrientation().isLeftToRight() : MetalTitlePane.this.window.getComponentOrientation().isLeftToRight();
/*      */ 
/*  904 */       int i = MetalTitlePane.this.getWidth();
/*      */ 
/*  906 */       int k = 3;
/*      */       int n;
/*      */       int i1;
/*  911 */       if ((MetalTitlePane.this.closeButton != null) && (MetalTitlePane.this.closeButton.getIcon() != null)) {
/*  912 */         n = MetalTitlePane.this.closeButton.getIcon().getIconHeight();
/*  913 */         i1 = MetalTitlePane.this.closeButton.getIcon().getIconWidth();
/*      */       }
/*      */       else {
/*  916 */         n = 16;
/*  917 */         i1 = 16;
/*      */       }
/*      */ 
/*  923 */       int j = bool ? i : 0;
/*      */ 
/*  925 */       int m = 5;
/*  926 */       j = bool ? m : i - i1 - m;
/*  927 */       if (MetalTitlePane.this.menuBar != null) {
/*  928 */         MetalTitlePane.this.menuBar.setBounds(j, k, i1, n);
/*      */       }
/*      */ 
/*  931 */       j = bool ? i : 0;
/*  932 */       m = 4;
/*  933 */       j += (bool ? -m - i1 : m);
/*  934 */       if (MetalTitlePane.this.closeButton != null) {
/*  935 */         MetalTitlePane.this.closeButton.setBounds(j, k, i1, n);
/*      */       }
/*      */ 
/*  938 */       if (!bool) j += i1;
/*      */ 
/*  940 */       if (MetalTitlePane.this.getWindowDecorationStyle() == 1) {
/*  941 */         if (Toolkit.getDefaultToolkit().isFrameStateSupported(6))
/*      */         {
/*  943 */           if (MetalTitlePane.this.toggleButton.getParent() != null) {
/*  944 */             m = 10;
/*  945 */             j += (bool ? -m - i1 : m);
/*  946 */             MetalTitlePane.this.toggleButton.setBounds(j, k, i1, n);
/*  947 */             if (!bool) {
/*  948 */               j += i1;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  953 */         if ((MetalTitlePane.this.iconifyButton != null) && (MetalTitlePane.this.iconifyButton.getParent() != null)) {
/*  954 */           m = 2;
/*  955 */           j += (bool ? -m - i1 : m);
/*  956 */           MetalTitlePane.this.iconifyButton.setBounds(j, k, i1, n);
/*  957 */           if (!bool) {
/*  958 */             j += i1;
/*      */           }
/*      */         }
/*      */       }
/*  962 */       MetalTitlePane.this.buttonsWidth = (bool ? i - j : j);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class WindowHandler extends WindowAdapter
/*      */   {
/*      */     private WindowHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void windowActivated(WindowEvent paramWindowEvent)
/*      */     {
/* 1033 */       MetalTitlePane.this.setActive(true);
/*      */     }
/*      */ 
/*      */     public void windowDeactivated(WindowEvent paramWindowEvent) {
/* 1037 */       MetalTitlePane.this.setActive(false);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalTitlePane
 * JD-Core Version:    0.6.2
 */
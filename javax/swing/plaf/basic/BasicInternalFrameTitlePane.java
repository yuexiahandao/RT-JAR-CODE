/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.InternalFrameEvent;
/*     */ import javax.swing.plaf.ActionMapUIResource;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class BasicInternalFrameTitlePane extends JComponent
/*     */ {
/*     */   protected JMenuBar menuBar;
/*     */   protected JButton iconButton;
/*     */   protected JButton maxButton;
/*     */   protected JButton closeButton;
/*     */   protected JMenu windowMenu;
/*     */   protected JInternalFrame frame;
/*     */   protected Color selectedTitleColor;
/*     */   protected Color selectedTextColor;
/*     */   protected Color notSelectedTitleColor;
/*     */   protected Color notSelectedTextColor;
/*     */   protected Icon maxIcon;
/*     */   protected Icon minIcon;
/*     */   protected Icon iconIcon;
/*     */   protected Icon closeIcon;
/*     */   protected PropertyChangeListener propertyChangeListener;
/*     */   protected Action closeAction;
/*     */   protected Action maximizeAction;
/*     */   protected Action iconifyAction;
/*     */   protected Action restoreAction;
/*     */   protected Action moveAction;
/*     */   protected Action sizeAction;
/*  90 */   protected static final String CLOSE_CMD = UIManager.getString("InternalFrameTitlePane.closeButtonText");
/*     */ 
/*  92 */   protected static final String ICONIFY_CMD = UIManager.getString("InternalFrameTitlePane.minimizeButtonText");
/*     */ 
/*  94 */   protected static final String RESTORE_CMD = UIManager.getString("InternalFrameTitlePane.restoreButtonText");
/*     */ 
/*  96 */   protected static final String MAXIMIZE_CMD = UIManager.getString("InternalFrameTitlePane.maximizeButtonText");
/*     */ 
/*  98 */   protected static final String MOVE_CMD = UIManager.getString("InternalFrameTitlePane.moveButtonText");
/*     */ 
/* 100 */   protected static final String SIZE_CMD = UIManager.getString("InternalFrameTitlePane.sizeButtonText");
/*     */   private String closeButtonToolTip;
/*     */   private String iconButtonToolTip;
/*     */   private String restoreButtonToolTip;
/*     */   private String maxButtonToolTip;
/*     */   private Handler handler;
/*     */ 
/*     */   public BasicInternalFrameTitlePane(JInternalFrame paramJInternalFrame)
/*     */   {
/* 110 */     this.frame = paramJInternalFrame;
/* 111 */     installTitlePane();
/*     */   }
/*     */ 
/*     */   protected void installTitlePane() {
/* 115 */     installDefaults();
/* 116 */     installListeners();
/*     */ 
/* 118 */     createActions();
/* 119 */     enableActions();
/* 120 */     createActionMap();
/*     */ 
/* 122 */     setLayout(createLayout());
/*     */ 
/* 124 */     assembleSystemMenu();
/* 125 */     createButtons();
/* 126 */     addSubComponents();
/*     */   }
/*     */ 
/*     */   protected void addSubComponents()
/*     */   {
/* 131 */     add(this.menuBar);
/* 132 */     add(this.iconButton);
/* 133 */     add(this.maxButton);
/* 134 */     add(this.closeButton);
/*     */   }
/*     */ 
/*     */   protected void createActions() {
/* 138 */     this.maximizeAction = new MaximizeAction();
/* 139 */     this.iconifyAction = new IconifyAction();
/* 140 */     this.closeAction = new CloseAction();
/* 141 */     this.restoreAction = new RestoreAction();
/* 142 */     this.moveAction = new MoveAction();
/* 143 */     this.sizeAction = new SizeAction();
/*     */   }
/*     */ 
/*     */   ActionMap createActionMap() {
/* 147 */     ActionMapUIResource localActionMapUIResource = new ActionMapUIResource();
/* 148 */     localActionMapUIResource.put("showSystemMenu", new ShowSystemMenuAction(true));
/* 149 */     localActionMapUIResource.put("hideSystemMenu", new ShowSystemMenuAction(false));
/* 150 */     return localActionMapUIResource;
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/* 154 */     if (this.propertyChangeListener == null) {
/* 155 */       this.propertyChangeListener = createPropertyChangeListener();
/*     */     }
/* 157 */     this.frame.addPropertyChangeListener(this.propertyChangeListener);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/* 161 */     this.frame.removePropertyChangeListener(this.propertyChangeListener);
/* 162 */     this.handler = null;
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/* 166 */     this.maxIcon = UIManager.getIcon("InternalFrame.maximizeIcon");
/* 167 */     this.minIcon = UIManager.getIcon("InternalFrame.minimizeIcon");
/* 168 */     this.iconIcon = UIManager.getIcon("InternalFrame.iconifyIcon");
/* 169 */     this.closeIcon = UIManager.getIcon("InternalFrame.closeIcon");
/*     */ 
/* 171 */     this.selectedTitleColor = UIManager.getColor("InternalFrame.activeTitleBackground");
/* 172 */     this.selectedTextColor = UIManager.getColor("InternalFrame.activeTitleForeground");
/* 173 */     this.notSelectedTitleColor = UIManager.getColor("InternalFrame.inactiveTitleBackground");
/* 174 */     this.notSelectedTextColor = UIManager.getColor("InternalFrame.inactiveTitleForeground");
/* 175 */     setFont(UIManager.getFont("InternalFrame.titleFont"));
/* 176 */     this.closeButtonToolTip = UIManager.getString("InternalFrame.closeButtonToolTip");
/*     */ 
/* 178 */     this.iconButtonToolTip = UIManager.getString("InternalFrame.iconButtonToolTip");
/*     */ 
/* 180 */     this.restoreButtonToolTip = UIManager.getString("InternalFrame.restoreButtonToolTip");
/*     */ 
/* 182 */     this.maxButtonToolTip = UIManager.getString("InternalFrame.maxButtonToolTip");
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void createButtons()
/*     */   {
/* 191 */     this.iconButton = new NoFocusButton("InternalFrameTitlePane.iconifyButtonAccessibleName", "InternalFrameTitlePane.iconifyButtonOpacity");
/*     */ 
/* 194 */     this.iconButton.addActionListener(this.iconifyAction);
/* 195 */     if ((this.iconButtonToolTip != null) && (this.iconButtonToolTip.length() != 0)) {
/* 196 */       this.iconButton.setToolTipText(this.iconButtonToolTip);
/*     */     }
/*     */ 
/* 199 */     this.maxButton = new NoFocusButton("InternalFrameTitlePane.maximizeButtonAccessibleName", "InternalFrameTitlePane.maximizeButtonOpacity");
/*     */ 
/* 202 */     this.maxButton.addActionListener(this.maximizeAction);
/*     */ 
/* 204 */     this.closeButton = new NoFocusButton("InternalFrameTitlePane.closeButtonAccessibleName", "InternalFrameTitlePane.closeButtonOpacity");
/*     */ 
/* 207 */     this.closeButton.addActionListener(this.closeAction);
/* 208 */     if ((this.closeButtonToolTip != null) && (this.closeButtonToolTip.length() != 0)) {
/* 209 */       this.closeButton.setToolTipText(this.closeButtonToolTip);
/*     */     }
/*     */ 
/* 212 */     setButtonIcons();
/*     */   }
/*     */ 
/*     */   protected void setButtonIcons() {
/* 216 */     if (this.frame.isIcon()) {
/* 217 */       if (this.minIcon != null) {
/* 218 */         this.iconButton.setIcon(this.minIcon);
/*     */       }
/* 220 */       if ((this.restoreButtonToolTip != null) && (this.restoreButtonToolTip.length() != 0))
/*     */       {
/* 222 */         this.iconButton.setToolTipText(this.restoreButtonToolTip);
/*     */       }
/* 224 */       if (this.maxIcon != null) {
/* 225 */         this.maxButton.setIcon(this.maxIcon);
/*     */       }
/* 227 */       if ((this.maxButtonToolTip != null) && (this.maxButtonToolTip.length() != 0))
/* 228 */         this.maxButton.setToolTipText(this.maxButtonToolTip);
/*     */     }
/* 230 */     else if (this.frame.isMaximum()) {
/* 231 */       if (this.iconIcon != null) {
/* 232 */         this.iconButton.setIcon(this.iconIcon);
/*     */       }
/* 234 */       if ((this.iconButtonToolTip != null) && (this.iconButtonToolTip.length() != 0)) {
/* 235 */         this.iconButton.setToolTipText(this.iconButtonToolTip);
/*     */       }
/* 237 */       if (this.minIcon != null) {
/* 238 */         this.maxButton.setIcon(this.minIcon);
/*     */       }
/* 240 */       if ((this.restoreButtonToolTip != null) && (this.restoreButtonToolTip.length() != 0))
/*     */       {
/* 242 */         this.maxButton.setToolTipText(this.restoreButtonToolTip);
/*     */       }
/*     */     } else {
/* 245 */       if (this.iconIcon != null) {
/* 246 */         this.iconButton.setIcon(this.iconIcon);
/*     */       }
/* 248 */       if ((this.iconButtonToolTip != null) && (this.iconButtonToolTip.length() != 0)) {
/* 249 */         this.iconButton.setToolTipText(this.iconButtonToolTip);
/*     */       }
/* 251 */       if (this.maxIcon != null) {
/* 252 */         this.maxButton.setIcon(this.maxIcon);
/*     */       }
/* 254 */       if ((this.maxButtonToolTip != null) && (this.maxButtonToolTip.length() != 0)) {
/* 255 */         this.maxButton.setToolTipText(this.maxButtonToolTip);
/*     */       }
/*     */     }
/* 258 */     if (this.closeIcon != null)
/* 259 */       this.closeButton.setIcon(this.closeIcon);
/*     */   }
/*     */ 
/*     */   protected void assembleSystemMenu()
/*     */   {
/* 264 */     this.menuBar = createSystemMenuBar();
/* 265 */     this.windowMenu = createSystemMenu();
/* 266 */     this.menuBar.add(this.windowMenu);
/* 267 */     addSystemMenuItems(this.windowMenu);
/* 268 */     enableActions();
/*     */   }
/*     */ 
/*     */   protected void addSystemMenuItems(JMenu paramJMenu) {
/* 272 */     JMenuItem localJMenuItem = paramJMenu.add(this.restoreAction);
/* 273 */     localJMenuItem.setMnemonic('R');
/* 274 */     localJMenuItem = paramJMenu.add(this.moveAction);
/* 275 */     localJMenuItem.setMnemonic('M');
/* 276 */     localJMenuItem = paramJMenu.add(this.sizeAction);
/* 277 */     localJMenuItem.setMnemonic('S');
/* 278 */     localJMenuItem = paramJMenu.add(this.iconifyAction);
/* 279 */     localJMenuItem.setMnemonic('n');
/* 280 */     localJMenuItem = paramJMenu.add(this.maximizeAction);
/* 281 */     localJMenuItem.setMnemonic('x');
/* 282 */     paramJMenu.add(new JSeparator());
/* 283 */     localJMenuItem = paramJMenu.add(this.closeAction);
/* 284 */     localJMenuItem.setMnemonic('C');
/*     */   }
/*     */ 
/*     */   protected JMenu createSystemMenu() {
/* 288 */     return new JMenu("    ");
/*     */   }
/*     */ 
/*     */   protected JMenuBar createSystemMenuBar() {
/* 292 */     this.menuBar = new SystemMenuBar();
/* 293 */     this.menuBar.setBorderPainted(false);
/* 294 */     return this.menuBar;
/*     */   }
/*     */ 
/*     */   protected void showSystemMenu()
/*     */   {
/* 300 */     this.windowMenu.doClick();
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics) {
/* 304 */     paintTitleBackground(paramGraphics);
/*     */ 
/* 306 */     if (this.frame.getTitle() != null) {
/* 307 */       boolean bool = this.frame.isSelected();
/* 308 */       Font localFont = paramGraphics.getFont();
/* 309 */       paramGraphics.setFont(getFont());
/* 310 */       if (bool)
/* 311 */         paramGraphics.setColor(this.selectedTextColor);
/*     */       else {
/* 313 */         paramGraphics.setColor(this.notSelectedTextColor);
/*     */       }
/*     */ 
/* 316 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(this.frame, paramGraphics);
/* 317 */       int i = (getHeight() + localFontMetrics.getAscent() - localFontMetrics.getLeading() - localFontMetrics.getDescent()) / 2;
/*     */ 
/* 321 */       Rectangle localRectangle = new Rectangle(0, 0, 0, 0);
/* 322 */       if (this.frame.isIconifiable()) localRectangle = this.iconButton.getBounds();
/* 323 */       else if (this.frame.isMaximizable()) localRectangle = this.maxButton.getBounds();
/* 324 */       else if (this.frame.isClosable()) localRectangle = this.closeButton.getBounds();
/*     */ 
/* 327 */       String str = this.frame.getTitle();
/*     */       int j;
/* 328 */       if (BasicGraphicsUtils.isLeftToRight(this.frame)) {
/* 329 */         if (localRectangle.x == 0) localRectangle.x = (this.frame.getWidth() - this.frame.getInsets().right);
/* 330 */         j = this.menuBar.getX() + this.menuBar.getWidth() + 2;
/* 331 */         int k = localRectangle.x - j - 3;
/* 332 */         str = getTitle(this.frame.getTitle(), localFontMetrics, k);
/*     */       } else {
/* 334 */         j = this.menuBar.getX() - 2 - SwingUtilities2.stringWidth(this.frame, localFontMetrics, str);
/*     */       }
/*     */ 
/* 338 */       SwingUtilities2.drawString(this.frame, paramGraphics, str, j, i);
/* 339 */       paramGraphics.setFont(localFont);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintTitleBackground(Graphics paramGraphics)
/*     */   {
/* 351 */     boolean bool = this.frame.isSelected();
/*     */ 
/* 353 */     if (bool)
/* 354 */       paramGraphics.setColor(this.selectedTitleColor);
/*     */     else
/* 356 */       paramGraphics.setColor(this.notSelectedTitleColor);
/* 357 */     paramGraphics.fillRect(0, 0, getWidth(), getHeight());
/*     */   }
/*     */ 
/*     */   protected String getTitle(String paramString, FontMetrics paramFontMetrics, int paramInt) {
/* 361 */     return SwingUtilities2.clipStringIfNecessary(this.frame, paramFontMetrics, paramString, paramInt);
/*     */   }
/*     */ 
/*     */   protected void postClosingEvent(JInternalFrame paramJInternalFrame)
/*     */   {
/* 370 */     InternalFrameEvent localInternalFrameEvent = new InternalFrameEvent(paramJInternalFrame, 25550);
/*     */ 
/* 373 */     if (JInternalFrame.class.getClassLoader() == null)
/*     */       try {
/* 375 */         Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(localInternalFrameEvent);
/* 376 */         return;
/*     */       }
/*     */       catch (SecurityException localSecurityException)
/*     */       {
/*     */       }
/* 381 */     paramJInternalFrame.dispatchEvent(localInternalFrameEvent);
/*     */   }
/*     */ 
/*     */   protected void enableActions()
/*     */   {
/* 386 */     this.restoreAction.setEnabled((this.frame.isMaximum()) || (this.frame.isIcon()));
/* 387 */     this.maximizeAction.setEnabled(((this.frame.isMaximizable()) && (!this.frame.isMaximum()) && (!this.frame.isIcon())) || ((this.frame.isMaximizable()) && (this.frame.isIcon())));
/*     */ 
/* 390 */     this.iconifyAction.setEnabled((this.frame.isIconifiable()) && (!this.frame.isIcon()));
/* 391 */     this.closeAction.setEnabled(this.frame.isClosable());
/* 392 */     this.sizeAction.setEnabled(false);
/* 393 */     this.moveAction.setEnabled(false);
/*     */   }
/*     */ 
/*     */   private Handler getHandler() {
/* 397 */     if (this.handler == null) {
/* 398 */       this.handler = new Handler(null);
/*     */     }
/* 400 */     return this.handler;
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/* 404 */     return getHandler();
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayout() {
/* 408 */     return getHandler();
/*     */   }
/*     */ 
/*     */   public class CloseAction extends AbstractAction
/*     */   {
/*     */     public CloseAction()
/*     */     {
/* 604 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 609 */       if (BasicInternalFrameTitlePane.this.frame.isClosable())
/* 610 */         BasicInternalFrameTitlePane.this.frame.doDefaultCloseAction();
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Handler
/*     */     implements LayoutManager, PropertyChangeListener
/*     */   {
/*     */     private Handler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 417 */       String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 419 */       if (str == "selected") {
/* 420 */         BasicInternalFrameTitlePane.this.repaint();
/* 421 */         return;
/*     */       }
/*     */ 
/* 424 */       if ((str == "icon") || (str == "maximum"))
/*     */       {
/* 426 */         BasicInternalFrameTitlePane.this.setButtonIcons();
/* 427 */         BasicInternalFrameTitlePane.this.enableActions();
/* 428 */         return;
/*     */       }
/*     */ 
/* 431 */       if ("closable" == str) {
/* 432 */         if (paramPropertyChangeEvent.getNewValue() == Boolean.TRUE)
/* 433 */           BasicInternalFrameTitlePane.this.add(BasicInternalFrameTitlePane.this.closeButton);
/*     */         else
/* 435 */           BasicInternalFrameTitlePane.this.remove(BasicInternalFrameTitlePane.this.closeButton);
/*     */       }
/* 437 */       else if ("maximizable" == str) {
/* 438 */         if (paramPropertyChangeEvent.getNewValue() == Boolean.TRUE)
/* 439 */           BasicInternalFrameTitlePane.this.add(BasicInternalFrameTitlePane.this.maxButton);
/*     */         else
/* 441 */           BasicInternalFrameTitlePane.this.remove(BasicInternalFrameTitlePane.this.maxButton);
/*     */       }
/* 443 */       else if ("iconable" == str) {
/* 444 */         if (paramPropertyChangeEvent.getNewValue() == Boolean.TRUE)
/* 445 */           BasicInternalFrameTitlePane.this.add(BasicInternalFrameTitlePane.this.iconButton);
/*     */         else {
/* 447 */           BasicInternalFrameTitlePane.this.remove(BasicInternalFrameTitlePane.this.iconButton);
/*     */         }
/*     */       }
/* 450 */       BasicInternalFrameTitlePane.this.enableActions();
/*     */ 
/* 452 */       BasicInternalFrameTitlePane.this.revalidate();
/* 453 */       BasicInternalFrameTitlePane.this.repaint();
/*     */     }
/*     */ 
/*     */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*     */     }
/*     */ 
/*     */     public void removeLayoutComponent(Component paramComponent) {
/*     */     }
/*     */ 
/*     */     public Dimension preferredLayoutSize(Container paramContainer) {
/* 463 */       return minimumLayoutSize(paramContainer);
/*     */     }
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container paramContainer)
/*     */     {
/* 468 */       int i = 22;
/*     */ 
/* 470 */       if (BasicInternalFrameTitlePane.this.frame.isClosable()) {
/* 471 */         i += 19;
/*     */       }
/* 473 */       if (BasicInternalFrameTitlePane.this.frame.isMaximizable()) {
/* 474 */         i += 19;
/*     */       }
/* 476 */       if (BasicInternalFrameTitlePane.this.frame.isIconifiable()) {
/* 477 */         i += 19;
/*     */       }
/*     */ 
/* 480 */       FontMetrics localFontMetrics = BasicInternalFrameTitlePane.this.frame.getFontMetrics(BasicInternalFrameTitlePane.this.getFont());
/* 481 */       String str = BasicInternalFrameTitlePane.this.frame.getTitle();
/* 482 */       int j = str != null ? SwingUtilities2.stringWidth(BasicInternalFrameTitlePane.this.frame, localFontMetrics, str) : 0;
/*     */ 
/* 484 */       int k = str != null ? str.length() : 0;
/*     */ 
/* 487 */       if (k > 3) {
/* 488 */         int m = SwingUtilities2.stringWidth(BasicInternalFrameTitlePane.this.frame, localFontMetrics, str.substring(0, 3) + "...");
/*     */ 
/* 490 */         i += (j < m ? j : m);
/*     */       } else {
/* 492 */         i += j;
/*     */       }
/*     */ 
/* 496 */       Icon localIcon = BasicInternalFrameTitlePane.this.frame.getFrameIcon();
/* 497 */       int n = localFontMetrics.getHeight();
/* 498 */       n += 2;
/* 499 */       int i1 = 0;
/* 500 */       if (localIcon != null)
/*     */       {
/* 502 */         i1 = Math.min(localIcon.getIconHeight(), 16);
/*     */       }
/* 504 */       i1 += 2;
/*     */ 
/* 506 */       int i2 = Math.max(n, i1);
/*     */ 
/* 508 */       Dimension localDimension = new Dimension(i, i2);
/*     */ 
/* 511 */       if (BasicInternalFrameTitlePane.this.getBorder() != null) {
/* 512 */         Insets localInsets = BasicInternalFrameTitlePane.this.getBorder().getBorderInsets(paramContainer);
/* 513 */         localDimension.height += localInsets.top + localInsets.bottom;
/* 514 */         localDimension.width += localInsets.left + localInsets.right;
/*     */       }
/* 516 */       return localDimension;
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer) {
/* 520 */       boolean bool = BasicGraphicsUtils.isLeftToRight(BasicInternalFrameTitlePane.this.frame);
/*     */ 
/* 522 */       int i = BasicInternalFrameTitlePane.this.getWidth();
/* 523 */       int j = BasicInternalFrameTitlePane.this.getHeight();
/*     */ 
/* 526 */       int m = BasicInternalFrameTitlePane.this.closeButton.getIcon().getIconHeight();
/*     */ 
/* 528 */       Icon localIcon = BasicInternalFrameTitlePane.this.frame.getFrameIcon();
/* 529 */       int n = 0;
/* 530 */       if (localIcon != null) {
/* 531 */         n = localIcon.getIconHeight();
/*     */       }
/* 533 */       int k = bool ? 2 : i - 16 - 2;
/* 534 */       BasicInternalFrameTitlePane.this.menuBar.setBounds(k, (j - n) / 2, 16, 16);
/*     */ 
/* 536 */       k = bool ? i - 16 - 2 : 2;
/*     */ 
/* 538 */       if (BasicInternalFrameTitlePane.this.frame.isClosable()) {
/* 539 */         BasicInternalFrameTitlePane.this.closeButton.setBounds(k, (j - m) / 2, 16, 14);
/* 540 */         k += (bool ? -18 : 18);
/*     */       }
/*     */ 
/* 543 */       if (BasicInternalFrameTitlePane.this.frame.isMaximizable()) {
/* 544 */         BasicInternalFrameTitlePane.this.maxButton.setBounds(k, (j - m) / 2, 16, 14);
/* 545 */         k += (bool ? -18 : 18);
/*     */       }
/*     */ 
/* 548 */       if (BasicInternalFrameTitlePane.this.frame.isIconifiable())
/* 549 */         BasicInternalFrameTitlePane.this.iconButton.setBounds(k, (j - m) / 2, 16, 14);
/*     */     }
/*     */   }
/*     */ 
/*     */   public class IconifyAction extends AbstractAction
/*     */   {
/*     */     public IconifyAction()
/*     */     {
/* 650 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 655 */       if (BasicInternalFrameTitlePane.this.frame.isIconifiable())
/* 656 */         if (!BasicInternalFrameTitlePane.this.frame.isIcon()) try {
/* 657 */             BasicInternalFrameTitlePane.this.frame.setIcon(true); } catch (PropertyVetoException localPropertyVetoException1) {
/*     */           } else try {
/* 659 */             BasicInternalFrameTitlePane.this.frame.setIcon(false);
/*     */           }
/*     */           catch (PropertyVetoException localPropertyVetoException2)
/*     */           {
/*     */           }
/*     */     }
/*     */   }
/*     */ 
/*     */   public class MaximizeAction extends AbstractAction
/*     */   {
/*     */     public MaximizeAction()
/*     */     {
/* 621 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 626 */       if (BasicInternalFrameTitlePane.this.frame.isMaximizable())
/* 627 */         if ((BasicInternalFrameTitlePane.this.frame.isMaximum()) && (BasicInternalFrameTitlePane.this.frame.isIcon()))
/*     */           try {
/* 629 */             BasicInternalFrameTitlePane.this.frame.setIcon(false); } catch (PropertyVetoException localPropertyVetoException1) {
/*     */           }
/* 631 */         else if (!BasicInternalFrameTitlePane.this.frame.isMaximum())
/*     */           try {
/* 633 */             BasicInternalFrameTitlePane.this.frame.setMaximum(true);
/*     */           } catch (PropertyVetoException localPropertyVetoException2) {
/*     */           }
/*     */         else try {
/* 637 */             BasicInternalFrameTitlePane.this.frame.setMaximum(false);
/*     */           }
/*     */           catch (PropertyVetoException localPropertyVetoException3)
/*     */           {
/*     */           }
/*     */     }
/*     */   }
/*     */ 
/*     */   public class MoveAction extends AbstractAction
/*     */   {
/*     */     public MoveAction()
/*     */     {
/* 698 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private class NoFocusButton extends JButton
/*     */   {
/*     */     private String uiKey;
/*     */ 
/*     */     public NoFocusButton(String paramString1, String arg3)
/*     */     {
/* 774 */       setFocusPainted(false);
/* 775 */       setMargin(new Insets(0, 0, 0, 0));
/* 776 */       this.uiKey = paramString1;
/*     */       Object localObject1;
/* 778 */       Object localObject2 = UIManager.get(localObject1);
/* 779 */       if ((localObject2 instanceof Boolean))
/* 780 */         setOpaque(((Boolean)localObject2).booleanValue()); 
/*     */     }
/*     */ 
/* 783 */     public boolean isFocusTraversable() { return false; } 
/*     */     public void requestFocus() {
/*     */     }
/* 786 */     public AccessibleContext getAccessibleContext() { AccessibleContext localAccessibleContext = super.getAccessibleContext();
/* 787 */       if (this.uiKey != null) {
/* 788 */         localAccessibleContext.setAccessibleName(UIManager.getString(this.uiKey));
/* 789 */         this.uiKey = null;
/*     */       }
/* 791 */       return localAccessibleContext;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class PropertyChangeHandler
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     public PropertyChangeHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 564 */       BasicInternalFrameTitlePane.this.getHandler().propertyChange(paramPropertyChangeEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public class RestoreAction extends AbstractAction
/*     */   {
/*     */     public RestoreAction()
/*     */     {
/* 671 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/* 676 */       if ((BasicInternalFrameTitlePane.this.frame.isMaximizable()) && (BasicInternalFrameTitlePane.this.frame.isMaximum()) && (BasicInternalFrameTitlePane.this.frame.isIcon()))
/*     */         try {
/* 678 */           BasicInternalFrameTitlePane.this.frame.setIcon(false); } catch (PropertyVetoException localPropertyVetoException1) {
/*     */         }
/* 680 */       else if ((BasicInternalFrameTitlePane.this.frame.isMaximizable()) && (BasicInternalFrameTitlePane.this.frame.isMaximum()))
/*     */         try {
/* 682 */           BasicInternalFrameTitlePane.this.frame.setMaximum(false); } catch (PropertyVetoException localPropertyVetoException2) {
/*     */         }
/* 684 */       else if ((BasicInternalFrameTitlePane.this.frame.isIconifiable()) && (BasicInternalFrameTitlePane.this.frame.isIcon()))
/*     */         try {
/* 686 */           BasicInternalFrameTitlePane.this.frame.setIcon(false);
/*     */         }
/*     */         catch (PropertyVetoException localPropertyVetoException3)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ShowSystemMenuAction extends AbstractAction
/*     */   {
/*     */     private boolean show;
/*     */ 
/*     */     public ShowSystemMenuAction(boolean arg2)
/*     */     {
/*     */       boolean bool;
/* 714 */       this.show = bool;
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 718 */       if (this.show)
/* 719 */         BasicInternalFrameTitlePane.this.windowMenu.doClick();
/*     */       else
/* 721 */         BasicInternalFrameTitlePane.this.windowMenu.setVisible(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public class SizeAction extends AbstractAction
/*     */   {
/*     */     public SizeAction()
/*     */     {
/* 732 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public class SystemMenuBar extends JMenuBar
/*     */   {
/*     */     public SystemMenuBar()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean isFocusTraversable() {
/* 747 */       return false;
/*     */     }
/*     */     public void requestFocus() {  } 
/* 750 */     public void paint(Graphics paramGraphics) { Icon localIcon = BasicInternalFrameTitlePane.this.frame.getFrameIcon();
/* 751 */       if (localIcon == null) {
/* 752 */         localIcon = (Icon)DefaultLookup.get(BasicInternalFrameTitlePane.this.frame, BasicInternalFrameTitlePane.this.frame.getUI(), "InternalFrame.icon");
/*     */       }
/*     */ 
/* 755 */       if (localIcon != null)
/*     */       {
/* 757 */         if (((localIcon instanceof ImageIcon)) && ((localIcon.getIconWidth() > 16) || (localIcon.getIconHeight() > 16))) {
/* 758 */           Image localImage = ((ImageIcon)localIcon).getImage();
/* 759 */           ((ImageIcon)localIcon).setImage(localImage.getScaledInstance(16, 16, 4));
/*     */         }
/* 761 */         localIcon.paintIcon(this, paramGraphics, 0, 0);
/*     */       } }
/*     */ 
/*     */     public boolean isOpaque()
/*     */     {
/* 766 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class TitlePaneLayout
/*     */     implements LayoutManager
/*     */   {
/*     */     public TitlePaneLayout()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void addLayoutComponent(String paramString, Component paramComponent)
/*     */     {
/* 578 */       BasicInternalFrameTitlePane.this.getHandler().addLayoutComponent(paramString, paramComponent);
/*     */     }
/*     */ 
/*     */     public void removeLayoutComponent(Component paramComponent) {
/* 582 */       BasicInternalFrameTitlePane.this.getHandler().removeLayoutComponent(paramComponent);
/*     */     }
/*     */ 
/*     */     public Dimension preferredLayoutSize(Container paramContainer) {
/* 586 */       return BasicInternalFrameTitlePane.this.getHandler().preferredLayoutSize(paramContainer);
/*     */     }
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 590 */       return BasicInternalFrameTitlePane.this.getHandler().minimumLayoutSize(paramContainer);
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer) {
/* 594 */       BasicInternalFrameTitlePane.this.getHandler().layoutContainer(paramContainer);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicInternalFrameTitlePane
 * JD-Core Version:    0.6.2
 */
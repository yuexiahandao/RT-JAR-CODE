/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.PrintStream;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.MenuElement;
/*     */ import javax.swing.MenuSelectionManager;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.MenuDragMouseEvent;
/*     */ import javax.swing.event.MenuDragMouseListener;
/*     */ import javax.swing.event.MenuKeyListener;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.plaf.ComponentInputMapUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.MenuItemUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.View;
/*     */ import sun.swing.MenuItemCheckIconFactory;
/*     */ import sun.swing.MenuItemLayoutHelper;
/*     */ import sun.swing.MenuItemLayoutHelper.LayoutResult;
/*     */ import sun.swing.MenuItemLayoutHelper.RectSize;
/*     */ import sun.swing.SwingUtilities2;
/*     */ import sun.swing.UIAction;
/*     */ 
/*     */ public class BasicMenuItemUI extends MenuItemUI
/*     */ {
/*     */   protected JMenuItem menuItem;
/*     */   protected Color selectionBackground;
/*     */   protected Color selectionForeground;
/*     */   protected Color disabledForeground;
/*     */   protected Color acceleratorForeground;
/*     */   protected Color acceleratorSelectionForeground;
/*     */   protected String acceleratorDelimiter;
/*     */   protected int defaultTextIconGap;
/*     */   protected Font acceleratorFont;
/*     */   protected MouseInputListener mouseInputListener;
/*     */   protected MenuDragMouseListener menuDragMouseListener;
/*     */   protected MenuKeyListener menuKeyListener;
/*     */   protected PropertyChangeListener propertyChangeListener;
/*     */   Handler handler;
/*     */   protected Icon arrowIcon;
/*     */   protected Icon checkIcon;
/*     */   protected boolean oldBorderPainted;
/*     */   private static final boolean TRACE = false;
/*     */   private static final boolean VERBOSE = false;
/*     */   private static final boolean DEBUG = false;
/*     */ 
/*     */   public BasicMenuItemUI()
/*     */   {
/*  50 */     this.menuItem = null;
/*     */ 
/*  83 */     this.arrowIcon = null;
/*  84 */     this.checkIcon = null;
/*     */   }
/*     */ 
/*     */   static void loadActionMap(LazyActionMap paramLazyActionMap)
/*     */   {
/*  96 */     paramLazyActionMap.put(new Actions("doClick"));
/*  97 */     BasicLookAndFeel.installAudioActionMap(paramLazyActionMap);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/* 101 */     return new BasicMenuItemUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/* 105 */     this.menuItem = ((JMenuItem)paramJComponent);
/*     */ 
/* 107 */     installDefaults();
/* 108 */     installComponents(this.menuItem);
/* 109 */     installListeners();
/* 110 */     installKeyboardActions();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 115 */     String str = getPropertyPrefix();
/*     */ 
/* 117 */     this.acceleratorFont = UIManager.getFont("MenuItem.acceleratorFont");
/*     */ 
/* 120 */     if (this.acceleratorFont == null) {
/* 121 */       this.acceleratorFont = UIManager.getFont("MenuItem.font");
/*     */     }
/*     */ 
/* 124 */     Object localObject = UIManager.get(getPropertyPrefix() + ".opaque");
/* 125 */     if (localObject != null) {
/* 126 */       LookAndFeel.installProperty(this.menuItem, "opaque", localObject);
/*     */     }
/*     */     else {
/* 129 */       LookAndFeel.installProperty(this.menuItem, "opaque", Boolean.TRUE);
/*     */     }
/* 131 */     if ((this.menuItem.getMargin() == null) || ((this.menuItem.getMargin() instanceof UIResource)))
/*     */     {
/* 133 */       this.menuItem.setMargin(UIManager.getInsets(str + ".margin"));
/*     */     }
/*     */ 
/* 136 */     LookAndFeel.installProperty(this.menuItem, "iconTextGap", Integer.valueOf(4));
/* 137 */     this.defaultTextIconGap = this.menuItem.getIconTextGap();
/*     */ 
/* 139 */     LookAndFeel.installBorder(this.menuItem, str + ".border");
/* 140 */     this.oldBorderPainted = this.menuItem.isBorderPainted();
/* 141 */     LookAndFeel.installProperty(this.menuItem, "borderPainted", Boolean.valueOf(UIManager.getBoolean(str + ".borderPainted")));
/*     */ 
/* 143 */     LookAndFeel.installColorsAndFont(this.menuItem, str + ".background", str + ".foreground", str + ".font");
/*     */ 
/* 149 */     if ((this.selectionBackground == null) || ((this.selectionBackground instanceof UIResource)))
/*     */     {
/* 151 */       this.selectionBackground = UIManager.getColor(str + ".selectionBackground");
/*     */     }
/*     */ 
/* 154 */     if ((this.selectionForeground == null) || ((this.selectionForeground instanceof UIResource)))
/*     */     {
/* 156 */       this.selectionForeground = UIManager.getColor(str + ".selectionForeground");
/*     */     }
/*     */ 
/* 159 */     if ((this.disabledForeground == null) || ((this.disabledForeground instanceof UIResource)))
/*     */     {
/* 161 */       this.disabledForeground = UIManager.getColor(str + ".disabledForeground");
/*     */     }
/*     */ 
/* 164 */     if ((this.acceleratorForeground == null) || ((this.acceleratorForeground instanceof UIResource)))
/*     */     {
/* 166 */       this.acceleratorForeground = UIManager.getColor(str + ".acceleratorForeground");
/*     */     }
/*     */ 
/* 169 */     if ((this.acceleratorSelectionForeground == null) || ((this.acceleratorSelectionForeground instanceof UIResource)))
/*     */     {
/* 171 */       this.acceleratorSelectionForeground = UIManager.getColor(str + ".acceleratorSelectionForeground");
/*     */     }
/*     */ 
/* 175 */     this.acceleratorDelimiter = UIManager.getString("MenuItem.acceleratorDelimiter");
/*     */ 
/* 177 */     if (this.acceleratorDelimiter == null) this.acceleratorDelimiter = "+";
/*     */ 
/* 179 */     if ((this.arrowIcon == null) || ((this.arrowIcon instanceof UIResource)))
/*     */     {
/* 181 */       this.arrowIcon = UIManager.getIcon(str + ".arrowIcon");
/*     */     }
/* 183 */     if ((this.checkIcon == null) || ((this.checkIcon instanceof UIResource)))
/*     */     {
/* 185 */       this.checkIcon = UIManager.getIcon(str + ".checkIcon");
/*     */ 
/* 189 */       boolean bool = MenuItemLayoutHelper.isColumnLayout(BasicGraphicsUtils.isLeftToRight(this.menuItem), this.menuItem);
/*     */ 
/* 191 */       if (bool) {
/* 192 */         MenuItemCheckIconFactory localMenuItemCheckIconFactory = (MenuItemCheckIconFactory)UIManager.get(str + ".checkIconFactory");
/*     */ 
/* 195 */         if ((localMenuItemCheckIconFactory != null) && (MenuItemLayoutHelper.useCheckAndArrow(this.menuItem)) && (localMenuItemCheckIconFactory.isCompatible(this.checkIcon, str)))
/*     */         {
/* 198 */           this.checkIcon = localMenuItemCheckIconFactory.getIcon(this.menuItem);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void installComponents(JMenuItem paramJMenuItem)
/*     */   {
/* 208 */     BasicHTML.updateRenderer(paramJMenuItem, paramJMenuItem.getText());
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix() {
/* 212 */     return "MenuItem";
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/* 216 */     if ((this.mouseInputListener = createMouseInputListener(this.menuItem)) != null) {
/* 217 */       this.menuItem.addMouseListener(this.mouseInputListener);
/* 218 */       this.menuItem.addMouseMotionListener(this.mouseInputListener);
/*     */     }
/* 220 */     if ((this.menuDragMouseListener = createMenuDragMouseListener(this.menuItem)) != null) {
/* 221 */       this.menuItem.addMenuDragMouseListener(this.menuDragMouseListener);
/*     */     }
/* 223 */     if ((this.menuKeyListener = createMenuKeyListener(this.menuItem)) != null) {
/* 224 */       this.menuItem.addMenuKeyListener(this.menuKeyListener);
/*     */     }
/* 226 */     if ((this.propertyChangeListener = createPropertyChangeListener(this.menuItem)) != null)
/* 227 */       this.menuItem.addPropertyChangeListener(this.propertyChangeListener);
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions()
/*     */   {
/* 232 */     installLazyActionMap();
/* 233 */     updateAcceleratorBinding();
/*     */   }
/*     */ 
/*     */   void installLazyActionMap() {
/* 237 */     LazyActionMap.installLazyActionMap(this.menuItem, BasicMenuItemUI.class, getPropertyPrefix() + ".actionMap");
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 242 */     this.menuItem = ((JMenuItem)paramJComponent);
/* 243 */     uninstallDefaults();
/* 244 */     uninstallComponents(this.menuItem);
/* 245 */     uninstallListeners();
/* 246 */     uninstallKeyboardActions();
/* 247 */     MenuItemLayoutHelper.clearUsedParentClientProperties(this.menuItem);
/* 248 */     this.menuItem = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 253 */     LookAndFeel.uninstallBorder(this.menuItem);
/* 254 */     LookAndFeel.installProperty(this.menuItem, "borderPainted", Boolean.valueOf(this.oldBorderPainted));
/* 255 */     if ((this.menuItem.getMargin() instanceof UIResource))
/* 256 */       this.menuItem.setMargin(null);
/* 257 */     if ((this.arrowIcon instanceof UIResource))
/* 258 */       this.arrowIcon = null;
/* 259 */     if ((this.checkIcon instanceof UIResource))
/* 260 */       this.checkIcon = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents(JMenuItem paramJMenuItem)
/*     */   {
/* 267 */     BasicHTML.updateRenderer(paramJMenuItem, "");
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/* 271 */     if (this.mouseInputListener != null) {
/* 272 */       this.menuItem.removeMouseListener(this.mouseInputListener);
/* 273 */       this.menuItem.removeMouseMotionListener(this.mouseInputListener);
/*     */     }
/* 275 */     if (this.menuDragMouseListener != null) {
/* 276 */       this.menuItem.removeMenuDragMouseListener(this.menuDragMouseListener);
/*     */     }
/* 278 */     if (this.menuKeyListener != null) {
/* 279 */       this.menuItem.removeMenuKeyListener(this.menuKeyListener);
/*     */     }
/* 281 */     if (this.propertyChangeListener != null) {
/* 282 */       this.menuItem.removePropertyChangeListener(this.propertyChangeListener);
/*     */     }
/*     */ 
/* 285 */     this.mouseInputListener = null;
/* 286 */     this.menuDragMouseListener = null;
/* 287 */     this.menuKeyListener = null;
/* 288 */     this.propertyChangeListener = null;
/* 289 */     this.handler = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions() {
/* 293 */     SwingUtilities.replaceUIActionMap(this.menuItem, null);
/* 294 */     SwingUtilities.replaceUIInputMap(this.menuItem, 2, null);
/*     */   }
/*     */ 
/*     */   protected MouseInputListener createMouseInputListener(JComponent paramJComponent)
/*     */   {
/* 299 */     return getHandler();
/*     */   }
/*     */ 
/*     */   protected MenuDragMouseListener createMenuDragMouseListener(JComponent paramJComponent) {
/* 303 */     return getHandler();
/*     */   }
/*     */ 
/*     */   protected MenuKeyListener createMenuKeyListener(JComponent paramJComponent) {
/* 307 */     return null;
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener(JComponent paramJComponent)
/*     */   {
/* 320 */     return getHandler();
/*     */   }
/*     */ 
/*     */   Handler getHandler() {
/* 324 */     if (this.handler == null) {
/* 325 */       this.handler = new Handler();
/*     */     }
/* 327 */     return this.handler;
/*     */   }
/*     */ 
/*     */   InputMap createInputMap(int paramInt) {
/* 331 */     if (paramInt == 2) {
/* 332 */       return new ComponentInputMapUIResource(this.menuItem);
/*     */     }
/* 334 */     return null;
/*     */   }
/*     */ 
/*     */   void updateAcceleratorBinding() {
/* 338 */     KeyStroke localKeyStroke = this.menuItem.getAccelerator();
/* 339 */     InputMap localInputMap = SwingUtilities.getUIInputMap(this.menuItem, 2);
/*     */ 
/* 342 */     if (localInputMap != null) {
/* 343 */       localInputMap.clear();
/*     */     }
/* 345 */     if (localKeyStroke != null) {
/* 346 */       if (localInputMap == null) {
/* 347 */         localInputMap = createInputMap(2);
/*     */ 
/* 349 */         SwingUtilities.replaceUIInputMap(this.menuItem, 2, localInputMap);
/*     */       }
/*     */ 
/* 352 */       localInputMap.put(localKeyStroke, "doClick");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent) {
/* 357 */     Dimension localDimension = null;
/* 358 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 359 */     if (localView != null) {
/* 360 */       localDimension = getPreferredSize(paramJComponent);
/*     */       Dimension tmp23_22 = localDimension; tmp23_22.width = ((int)(tmp23_22.width - (localView.getPreferredSpan(0) - localView.getMinimumSpan(0))));
/*     */     }
/* 363 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent) {
/* 367 */     return getPreferredMenuItemSize(paramJComponent, this.checkIcon, this.arrowIcon, this.defaultTextIconGap);
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 374 */     Dimension localDimension = null;
/* 375 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 376 */     if (localView != null) {
/* 377 */       localDimension = getPreferredSize(paramJComponent);
/*     */       Dimension tmp23_22 = localDimension; tmp23_22.width = ((int)(tmp23_22.width + (localView.getMaximumSpan(0) - localView.getPreferredSpan(0))));
/*     */     }
/* 380 */     return localDimension;
/*     */   }
/*     */ 
/*     */   protected Dimension getPreferredMenuItemSize(JComponent paramJComponent, Icon paramIcon1, Icon paramIcon2, int paramInt)
/*     */   {
/* 411 */     JMenuItem localJMenuItem = (JMenuItem)paramJComponent;
/* 412 */     MenuItemLayoutHelper localMenuItemLayoutHelper = new MenuItemLayoutHelper(localJMenuItem, paramIcon1, paramIcon2, MenuItemLayoutHelper.createMaxRect(), paramInt, this.acceleratorDelimiter, BasicGraphicsUtils.isLeftToRight(localJMenuItem), localJMenuItem.getFont(), this.acceleratorFont, MenuItemLayoutHelper.useCheckAndArrow(this.menuItem), getPropertyPrefix());
/*     */ 
/* 419 */     Dimension localDimension = new Dimension();
/*     */ 
/* 422 */     localDimension.width = localMenuItemLayoutHelper.getLeadingGap();
/* 423 */     MenuItemLayoutHelper.addMaxWidth(localMenuItemLayoutHelper.getCheckSize(), localMenuItemLayoutHelper.getAfterCheckIconGap(), localDimension);
/*     */ 
/* 426 */     if ((!localMenuItemLayoutHelper.isTopLevelMenu()) && (localMenuItemLayoutHelper.getMinTextOffset() > 0) && (localDimension.width < localMenuItemLayoutHelper.getMinTextOffset()))
/*     */     {
/* 429 */       localDimension.width = localMenuItemLayoutHelper.getMinTextOffset();
/*     */     }
/* 431 */     MenuItemLayoutHelper.addMaxWidth(localMenuItemLayoutHelper.getLabelSize(), localMenuItemLayoutHelper.getGap(), localDimension);
/* 432 */     MenuItemLayoutHelper.addMaxWidth(localMenuItemLayoutHelper.getAccSize(), localMenuItemLayoutHelper.getGap(), localDimension);
/* 433 */     MenuItemLayoutHelper.addMaxWidth(localMenuItemLayoutHelper.getArrowSize(), localMenuItemLayoutHelper.getGap(), localDimension);
/*     */ 
/* 436 */     localDimension.height = MenuItemLayoutHelper.max(new int[] { localMenuItemLayoutHelper.getCheckSize().getHeight(), localMenuItemLayoutHelper.getLabelSize().getHeight(), localMenuItemLayoutHelper.getAccSize().getHeight(), localMenuItemLayoutHelper.getArrowSize().getHeight() });
/*     */ 
/* 441 */     Insets localInsets = localMenuItemLayoutHelper.getMenuItem().getInsets();
/* 442 */     if (localInsets != null) {
/* 443 */       localDimension.width += localInsets.left + localInsets.right;
/* 444 */       localDimension.height += localInsets.top + localInsets.bottom;
/*     */     }
/*     */ 
/* 449 */     if (localDimension.width % 2 == 0) {
/* 450 */       localDimension.width += 1;
/*     */     }
/*     */ 
/* 455 */     if ((localDimension.height % 2 == 0) && (Boolean.TRUE != UIManager.get(getPropertyPrefix() + ".evenHeight")))
/*     */     {
/* 458 */       localDimension.height += 1;
/*     */     }
/*     */ 
/* 461 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 471 */     paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/* 475 */     paintMenuItem(paramGraphics, paramJComponent, this.checkIcon, this.arrowIcon, this.selectionBackground, this.selectionForeground, this.defaultTextIconGap);
/*     */   }
/*     */ 
/*     */   protected void paintMenuItem(Graphics paramGraphics, JComponent paramJComponent, Icon paramIcon1, Icon paramIcon2, Color paramColor1, Color paramColor2, int paramInt)
/*     */   {
/* 485 */     Font localFont = paramGraphics.getFont();
/* 486 */     Color localColor = paramGraphics.getColor();
/*     */ 
/* 488 */     JMenuItem localJMenuItem = (JMenuItem)paramJComponent;
/* 489 */     paramGraphics.setFont(localJMenuItem.getFont());
/*     */ 
/* 491 */     Rectangle localRectangle = new Rectangle(0, 0, localJMenuItem.getWidth(), localJMenuItem.getHeight());
/* 492 */     applyInsets(localRectangle, localJMenuItem.getInsets());
/*     */ 
/* 494 */     MenuItemLayoutHelper localMenuItemLayoutHelper = new MenuItemLayoutHelper(localJMenuItem, paramIcon1, paramIcon2, localRectangle, paramInt, this.acceleratorDelimiter, BasicGraphicsUtils.isLeftToRight(localJMenuItem), localJMenuItem.getFont(), this.acceleratorFont, MenuItemLayoutHelper.useCheckAndArrow(this.menuItem), getPropertyPrefix());
/*     */ 
/* 499 */     MenuItemLayoutHelper.LayoutResult localLayoutResult = localMenuItemLayoutHelper.layoutMenuItem();
/*     */ 
/* 501 */     paintBackground(paramGraphics, localJMenuItem, paramColor1);
/* 502 */     paintCheckIcon(paramGraphics, localMenuItemLayoutHelper, localLayoutResult, localColor, paramColor2);
/* 503 */     paintIcon(paramGraphics, localMenuItemLayoutHelper, localLayoutResult, localColor);
/* 504 */     paintText(paramGraphics, localMenuItemLayoutHelper, localLayoutResult);
/* 505 */     paintAccText(paramGraphics, localMenuItemLayoutHelper, localLayoutResult);
/* 506 */     paintArrowIcon(paramGraphics, localMenuItemLayoutHelper, localLayoutResult, paramColor2);
/*     */ 
/* 509 */     paramGraphics.setColor(localColor);
/* 510 */     paramGraphics.setFont(localFont);
/*     */   }
/*     */ 
/*     */   private void paintIcon(Graphics paramGraphics, MenuItemLayoutHelper paramMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult, Color paramColor)
/*     */   {
/* 515 */     if (paramMenuItemLayoutHelper.getIcon() != null)
/*     */     {
/* 517 */       ButtonModel localButtonModel = paramMenuItemLayoutHelper.getMenuItem().getModel();
/*     */       Icon localIcon;
/* 518 */       if (!localButtonModel.isEnabled()) {
/* 519 */         localIcon = paramMenuItemLayoutHelper.getMenuItem().getDisabledIcon();
/* 520 */       } else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 521 */         localIcon = paramMenuItemLayoutHelper.getMenuItem().getPressedIcon();
/* 522 */         if (localIcon == null)
/*     */         {
/* 524 */           localIcon = paramMenuItemLayoutHelper.getMenuItem().getIcon();
/*     */         }
/*     */       } else {
/* 527 */         localIcon = paramMenuItemLayoutHelper.getMenuItem().getIcon();
/*     */       }
/*     */ 
/* 530 */       if (localIcon != null) {
/* 531 */         localIcon.paintIcon(paramMenuItemLayoutHelper.getMenuItem(), paramGraphics, paramLayoutResult.getIconRect().x, paramLayoutResult.getIconRect().y);
/*     */ 
/* 533 */         paramGraphics.setColor(paramColor);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void paintCheckIcon(Graphics paramGraphics, MenuItemLayoutHelper paramMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult, Color paramColor1, Color paramColor2)
/*     */   {
/* 541 */     if (paramMenuItemLayoutHelper.getCheckIcon() != null) {
/* 542 */       ButtonModel localButtonModel = paramMenuItemLayoutHelper.getMenuItem().getModel();
/* 543 */       if ((localButtonModel.isArmed()) || (((paramMenuItemLayoutHelper.getMenuItem() instanceof JMenu)) && (localButtonModel.isSelected())))
/*     */       {
/* 545 */         paramGraphics.setColor(paramColor2);
/*     */       }
/* 547 */       else paramGraphics.setColor(paramColor1);
/*     */ 
/* 549 */       if (paramMenuItemLayoutHelper.useCheckAndArrow()) {
/* 550 */         paramMenuItemLayoutHelper.getCheckIcon().paintIcon(paramMenuItemLayoutHelper.getMenuItem(), paramGraphics, paramLayoutResult.getCheckRect().x, paramLayoutResult.getCheckRect().y);
/*     */       }
/*     */ 
/* 553 */       paramGraphics.setColor(paramColor1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void paintAccText(Graphics paramGraphics, MenuItemLayoutHelper paramMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 559 */     if (!paramMenuItemLayoutHelper.getAccText().equals("")) {
/* 560 */       ButtonModel localButtonModel = paramMenuItemLayoutHelper.getMenuItem().getModel();
/* 561 */       paramGraphics.setFont(paramMenuItemLayoutHelper.getAccFontMetrics().getFont());
/* 562 */       if (!localButtonModel.isEnabled())
/*     */       {
/* 564 */         if (this.disabledForeground != null) {
/* 565 */           paramGraphics.setColor(this.disabledForeground);
/* 566 */           SwingUtilities2.drawString(paramMenuItemLayoutHelper.getMenuItem(), paramGraphics, paramMenuItemLayoutHelper.getAccText(), paramLayoutResult.getAccRect().x, paramLayoutResult.getAccRect().y + paramMenuItemLayoutHelper.getAccFontMetrics().getAscent());
/*     */         }
/*     */         else
/*     */         {
/* 570 */           paramGraphics.setColor(paramMenuItemLayoutHelper.getMenuItem().getBackground().brighter());
/* 571 */           SwingUtilities2.drawString(paramMenuItemLayoutHelper.getMenuItem(), paramGraphics, paramMenuItemLayoutHelper.getAccText(), paramLayoutResult.getAccRect().x, paramLayoutResult.getAccRect().y + paramMenuItemLayoutHelper.getAccFontMetrics().getAscent());
/*     */ 
/* 574 */           paramGraphics.setColor(paramMenuItemLayoutHelper.getMenuItem().getBackground().darker());
/* 575 */           SwingUtilities2.drawString(paramMenuItemLayoutHelper.getMenuItem(), paramGraphics, paramMenuItemLayoutHelper.getAccText(), paramLayoutResult.getAccRect().x - 1, paramLayoutResult.getAccRect().y + paramMenuItemLayoutHelper.getFontMetrics().getAscent() - 1);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 581 */         if ((localButtonModel.isArmed()) || (((paramMenuItemLayoutHelper.getMenuItem() instanceof JMenu)) && (localButtonModel.isSelected())))
/*     */         {
/* 584 */           paramGraphics.setColor(this.acceleratorSelectionForeground);
/*     */         }
/* 586 */         else paramGraphics.setColor(this.acceleratorForeground);
/*     */ 
/* 588 */         SwingUtilities2.drawString(paramMenuItemLayoutHelper.getMenuItem(), paramGraphics, paramMenuItemLayoutHelper.getAccText(), paramLayoutResult.getAccRect().x, paramLayoutResult.getAccRect().y + paramMenuItemLayoutHelper.getAccFontMetrics().getAscent());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void paintText(Graphics paramGraphics, MenuItemLayoutHelper paramMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 597 */     if (!paramMenuItemLayoutHelper.getText().equals(""))
/* 598 */       if (paramMenuItemLayoutHelper.getHtmlView() != null)
/*     */       {
/* 600 */         paramMenuItemLayoutHelper.getHtmlView().paint(paramGraphics, paramLayoutResult.getTextRect());
/*     */       }
/*     */       else
/* 603 */         paintText(paramGraphics, paramMenuItemLayoutHelper.getMenuItem(), paramLayoutResult.getTextRect(), paramMenuItemLayoutHelper.getText());
/*     */   }
/*     */ 
/*     */   private void paintArrowIcon(Graphics paramGraphics, MenuItemLayoutHelper paramMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult, Color paramColor)
/*     */   {
/* 611 */     if (paramMenuItemLayoutHelper.getArrowIcon() != null) {
/* 612 */       ButtonModel localButtonModel = paramMenuItemLayoutHelper.getMenuItem().getModel();
/* 613 */       if ((localButtonModel.isArmed()) || (((paramMenuItemLayoutHelper.getMenuItem() instanceof JMenu)) && (localButtonModel.isSelected())))
/*     */       {
/* 615 */         paramGraphics.setColor(paramColor);
/*     */       }
/* 617 */       if (paramMenuItemLayoutHelper.useCheckAndArrow())
/* 618 */         paramMenuItemLayoutHelper.getArrowIcon().paintIcon(paramMenuItemLayoutHelper.getMenuItem(), paramGraphics, paramLayoutResult.getArrowRect().x, paramLayoutResult.getArrowRect().y);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void applyInsets(Rectangle paramRectangle, Insets paramInsets)
/*     */   {
/* 625 */     if (paramInsets != null) {
/* 626 */       paramRectangle.x += paramInsets.left;
/* 627 */       paramRectangle.y += paramInsets.top;
/* 628 */       paramRectangle.width -= paramInsets.right + paramRectangle.x;
/* 629 */       paramRectangle.height -= paramInsets.bottom + paramRectangle.y;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics, JMenuItem paramJMenuItem, Color paramColor)
/*     */   {
/* 642 */     ButtonModel localButtonModel = paramJMenuItem.getModel();
/* 643 */     Color localColor = paramGraphics.getColor();
/* 644 */     int i = paramJMenuItem.getWidth();
/* 645 */     int j = paramJMenuItem.getHeight();
/*     */ 
/* 647 */     if (paramJMenuItem.isOpaque()) {
/* 648 */       if ((localButtonModel.isArmed()) || (((paramJMenuItem instanceof JMenu)) && (localButtonModel.isSelected()))) {
/* 649 */         paramGraphics.setColor(paramColor);
/* 650 */         paramGraphics.fillRect(0, 0, i, j);
/*     */       } else {
/* 652 */         paramGraphics.setColor(paramJMenuItem.getBackground());
/* 653 */         paramGraphics.fillRect(0, 0, i, j);
/*     */       }
/* 655 */       paramGraphics.setColor(localColor);
/*     */     }
/* 657 */     else if ((localButtonModel.isArmed()) || (((paramJMenuItem instanceof JMenu)) && (localButtonModel.isSelected())))
/*     */     {
/* 659 */       paramGraphics.setColor(paramColor);
/* 660 */       paramGraphics.fillRect(0, 0, i, j);
/* 661 */       paramGraphics.setColor(localColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, JMenuItem paramJMenuItem, Rectangle paramRectangle, String paramString)
/*     */   {
/* 675 */     ButtonModel localButtonModel = paramJMenuItem.getModel();
/* 676 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(paramJMenuItem, paramGraphics);
/* 677 */     int i = paramJMenuItem.getDisplayedMnemonicIndex();
/*     */ 
/* 679 */     if (!localButtonModel.isEnabled())
/*     */     {
/* 681 */       if ((UIManager.get("MenuItem.disabledForeground") instanceof Color)) {
/* 682 */         paramGraphics.setColor(UIManager.getColor("MenuItem.disabledForeground"));
/* 683 */         SwingUtilities2.drawStringUnderlineCharAt(paramJMenuItem, paramGraphics, paramString, i, paramRectangle.x, paramRectangle.y + localFontMetrics.getAscent());
/*     */       }
/*     */       else {
/* 686 */         paramGraphics.setColor(paramJMenuItem.getBackground().brighter());
/* 687 */         SwingUtilities2.drawStringUnderlineCharAt(paramJMenuItem, paramGraphics, paramString, i, paramRectangle.x, paramRectangle.y + localFontMetrics.getAscent());
/*     */ 
/* 689 */         paramGraphics.setColor(paramJMenuItem.getBackground().darker());
/* 690 */         SwingUtilities2.drawStringUnderlineCharAt(paramJMenuItem, paramGraphics, paramString, i, paramRectangle.x - 1, paramRectangle.y + localFontMetrics.getAscent() - 1);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 696 */       if ((localButtonModel.isArmed()) || (((paramJMenuItem instanceof JMenu)) && (localButtonModel.isSelected()))) {
/* 697 */         paramGraphics.setColor(this.selectionForeground);
/*     */       }
/* 699 */       SwingUtilities2.drawStringUnderlineCharAt(paramJMenuItem, paramGraphics, paramString, i, paramRectangle.x, paramRectangle.y + localFontMetrics.getAscent());
/*     */     }
/*     */   }
/*     */ 
/*     */   public MenuElement[] getPath()
/*     */   {
/* 705 */     MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/* 706 */     MenuElement[] arrayOfMenuElement1 = localMenuSelectionManager.getSelectedPath();
/*     */ 
/* 708 */     int i = arrayOfMenuElement1.length;
/* 709 */     if (i == 0)
/* 710 */       return new MenuElement[0];
/* 711 */     Container localContainer = this.menuItem.getParent();
/*     */     MenuElement[] arrayOfMenuElement2;
/* 712 */     if (arrayOfMenuElement1[(i - 1)].getComponent() == localContainer)
/*     */     {
/* 714 */       arrayOfMenuElement2 = new MenuElement[i + 1];
/* 715 */       System.arraycopy(arrayOfMenuElement1, 0, arrayOfMenuElement2, 0, i);
/* 716 */       arrayOfMenuElement2[i] = this.menuItem;
/*     */     }
/*     */     else
/*     */     {
/* 725 */       for (int j = arrayOfMenuElement1.length - 1; (j >= 0) && 
/* 726 */         (arrayOfMenuElement1[j].getComponent() != localContainer); j--);
/* 729 */       arrayOfMenuElement2 = new MenuElement[j + 2];
/* 730 */       System.arraycopy(arrayOfMenuElement1, 0, arrayOfMenuElement2, 0, j + 1);
/* 731 */       arrayOfMenuElement2[(j + 1)] = this.menuItem;
/*     */     }
/*     */ 
/* 740 */     return arrayOfMenuElement2;
/*     */   }
/*     */ 
/*     */   void printMenuElementArray(MenuElement[] paramArrayOfMenuElement, boolean paramBoolean) {
/* 744 */     System.out.println("Path is(");
/*     */ 
/* 746 */     int i = 0; for (int j = paramArrayOfMenuElement.length; i < j; i++) {
/* 747 */       for (int k = 0; k <= i; k++)
/* 748 */         System.out.print("  ");
/* 749 */       MenuElement localMenuElement = paramArrayOfMenuElement[i];
/* 750 */       if ((localMenuElement instanceof JMenuItem))
/* 751 */         System.out.println(((JMenuItem)localMenuElement).getText() + ", ");
/* 752 */       else if (localMenuElement == null)
/* 753 */         System.out.println("NULL , ");
/*     */       else
/* 755 */         System.out.println("" + localMenuElement + ", ");
/*     */     }
/* 757 */     System.out.println(")");
/*     */ 
/* 759 */     if (paramBoolean == true)
/* 760 */       Thread.dumpStack();
/*     */   }
/*     */ 
/*     */   protected void doClick(MenuSelectionManager paramMenuSelectionManager)
/*     */   {
/* 824 */     if (!isInternalFrameSystemMenu()) {
/* 825 */       BasicLookAndFeel.playSound(this.menuItem, getPropertyPrefix() + ".commandSound");
/*     */     }
/*     */ 
/* 829 */     if (paramMenuSelectionManager == null) {
/* 830 */       paramMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */     }
/* 832 */     paramMenuSelectionManager.clearSelectedPath();
/* 833 */     this.menuItem.doClick(0);
/*     */   }
/*     */ 
/*     */   private boolean isInternalFrameSystemMenu()
/*     */   {
/* 847 */     String str = this.menuItem.getActionCommand();
/* 848 */     if ((str == "Close") || (str == "Minimize") || (str == "Restore") || (str == "Maximize"))
/*     */     {
/* 852 */       return true;
/*     */     }
/* 854 */     return false;
/*     */   }
/*     */ 
/*     */   private static class Actions extends UIAction
/*     */   {
/*     */     private static final String CLICK = "doClick";
/*     */ 
/*     */     Actions(String paramString)
/*     */     {
/* 796 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 800 */       JMenuItem localJMenuItem = (JMenuItem)paramActionEvent.getSource();
/* 801 */       MenuSelectionManager.defaultManager().clearSelectedPath();
/* 802 */       localJMenuItem.doClick();
/*     */     }
/*     */   }
/*     */ 
/*     */   class Handler
/*     */     implements MenuDragMouseListener, MouseInputListener, PropertyChangeListener
/*     */   {
/*     */     Handler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent)
/*     */     {
/* 869 */       if (!BasicMenuItemUI.this.menuItem.isEnabled()) {
/* 870 */         return;
/*     */       }
/* 872 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 874 */       Point localPoint = paramMouseEvent.getPoint();
/* 875 */       if ((localPoint.x >= 0) && (localPoint.x < BasicMenuItemUI.this.menuItem.getWidth()) && (localPoint.y >= 0) && (localPoint.y < BasicMenuItemUI.this.menuItem.getHeight()))
/*     */       {
/* 877 */         BasicMenuItemUI.this.doClick(localMenuSelectionManager);
/*     */       }
/* 879 */       else localMenuSelectionManager.processMouseEvent(paramMouseEvent); 
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent paramMouseEvent)
/*     */     {
/* 883 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/* 884 */       int i = paramMouseEvent.getModifiers();
/*     */ 
/* 886 */       if ((i & 0x1C) != 0)
/*     */       {
/* 888 */         MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent);
/*     */       }
/* 890 */       else localMenuSelectionManager.setSelectedPath(BasicMenuItemUI.this.getPath()); 
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent paramMouseEvent)
/*     */     {
/* 894 */       MenuSelectionManager localMenuSelectionManager = MenuSelectionManager.defaultManager();
/*     */ 
/* 896 */       int i = paramMouseEvent.getModifiers();
/*     */ 
/* 898 */       if ((i & 0x1C) != 0)
/*     */       {
/* 900 */         MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent);
/*     */       }
/*     */       else {
/* 903 */         MenuElement[] arrayOfMenuElement1 = localMenuSelectionManager.getSelectedPath();
/* 904 */         if ((arrayOfMenuElement1.length > 1) && (arrayOfMenuElement1[(arrayOfMenuElement1.length - 1)] == BasicMenuItemUI.this.menuItem)) {
/* 905 */           MenuElement[] arrayOfMenuElement2 = new MenuElement[arrayOfMenuElement1.length - 1];
/*     */ 
/* 907 */           int j = 0; for (int k = arrayOfMenuElement1.length - 1; j < k; j++)
/* 908 */             arrayOfMenuElement2[j] = arrayOfMenuElement1[j];
/* 909 */           localMenuSelectionManager.setSelectedPath(arrayOfMenuElement2);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 915 */       MenuSelectionManager.defaultManager().processMouseEvent(paramMouseEvent);
/*     */     }
/*     */ 
/*     */     public void mouseMoved(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void menuDragMouseEntered(MenuDragMouseEvent paramMenuDragMouseEvent)
/*     */     {
/* 924 */       MenuSelectionManager localMenuSelectionManager = paramMenuDragMouseEvent.getMenuSelectionManager();
/* 925 */       MenuElement[] arrayOfMenuElement = paramMenuDragMouseEvent.getPath();
/* 926 */       localMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*     */     }
/*     */     public void menuDragMouseDragged(MenuDragMouseEvent paramMenuDragMouseEvent) {
/* 929 */       MenuSelectionManager localMenuSelectionManager = paramMenuDragMouseEvent.getMenuSelectionManager();
/* 930 */       MenuElement[] arrayOfMenuElement = paramMenuDragMouseEvent.getPath();
/* 931 */       localMenuSelectionManager.setSelectedPath(arrayOfMenuElement);
/*     */     }
/*     */     public void menuDragMouseExited(MenuDragMouseEvent paramMenuDragMouseEvent) {
/*     */     }
/* 935 */     public void menuDragMouseReleased(MenuDragMouseEvent paramMenuDragMouseEvent) { if (!BasicMenuItemUI.this.menuItem.isEnabled()) {
/* 936 */         return;
/*     */       }
/* 938 */       MenuSelectionManager localMenuSelectionManager = paramMenuDragMouseEvent.getMenuSelectionManager();
/* 939 */       MenuElement[] arrayOfMenuElement = paramMenuDragMouseEvent.getPath();
/* 940 */       Point localPoint = paramMenuDragMouseEvent.getPoint();
/* 941 */       if ((localPoint.x >= 0) && (localPoint.x < BasicMenuItemUI.this.menuItem.getWidth()) && (localPoint.y >= 0) && (localPoint.y < BasicMenuItemUI.this.menuItem.getHeight()))
/*     */       {
/* 943 */         BasicMenuItemUI.this.doClick(localMenuSelectionManager);
/*     */       }
/* 945 */       else localMenuSelectionManager.clearSelectedPath();
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 954 */       String str1 = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 956 */       if ((str1 == "labelFor") || (str1 == "displayedMnemonic") || (str1 == "accelerator"))
/*     */       {
/* 958 */         BasicMenuItemUI.this.updateAcceleratorBinding();
/* 959 */       } else if ((str1 == "text") || ("font" == str1) || ("foreground" == str1))
/*     */       {
/* 964 */         JMenuItem localJMenuItem = (JMenuItem)paramPropertyChangeEvent.getSource();
/* 965 */         String str2 = localJMenuItem.getText();
/* 966 */         BasicHTML.updateRenderer(localJMenuItem, str2);
/* 967 */       } else if (str1 == "iconTextGap") {
/* 968 */         BasicMenuItemUI.this.defaultTextIconGap = ((Number)paramPropertyChangeEvent.getNewValue()).intValue();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected class MouseInputHandler
/*     */     implements MouseInputListener
/*     */   {
/*     */     protected MouseInputHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent paramMouseEvent)
/*     */     {
/* 769 */       BasicMenuItemUI.this.getHandler().mouseClicked(paramMouseEvent);
/*     */     }
/*     */     public void mousePressed(MouseEvent paramMouseEvent) {
/* 772 */       BasicMenuItemUI.this.getHandler().mousePressed(paramMouseEvent);
/*     */     }
/*     */     public void mouseReleased(MouseEvent paramMouseEvent) {
/* 775 */       BasicMenuItemUI.this.getHandler().mouseReleased(paramMouseEvent);
/*     */     }
/*     */     public void mouseEntered(MouseEvent paramMouseEvent) {
/* 778 */       BasicMenuItemUI.this.getHandler().mouseEntered(paramMouseEvent);
/*     */     }
/*     */     public void mouseExited(MouseEvent paramMouseEvent) {
/* 781 */       BasicMenuItemUI.this.getHandler().mouseExited(paramMouseEvent);
/*     */     }
/*     */     public void mouseDragged(MouseEvent paramMouseEvent) {
/* 784 */       BasicMenuItemUI.this.getHandler().mouseDragged(paramMouseEvent);
/*     */     }
/*     */     public void mouseMoved(MouseEvent paramMouseEvent) {
/* 787 */       BasicMenuItemUI.this.getHandler().mouseMoved(paramMouseEvent);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicMenuItemUI
 * JD-Core Version:    0.6.2
 */
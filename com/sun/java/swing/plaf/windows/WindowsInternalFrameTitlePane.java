/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIDefaults.LazyValue;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane.PropertyChangeHandler;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class WindowsInternalFrameTitlePane extends BasicInternalFrameTitlePane
/*     */ {
/*     */   private Color selectedTitleGradientColor;
/*     */   private Color notSelectedTitleGradientColor;
/*     */   private JPopupMenu systemPopupMenu;
/*     */   private JLabel systemLabel;
/*     */   private Font titleFont;
/*     */   private int titlePaneHeight;
/*     */   private int buttonWidth;
/*     */   private int buttonHeight;
/*     */   private boolean hotTrackingOn;
/*     */ 
/*     */   public WindowsInternalFrameTitlePane(JInternalFrame paramJInternalFrame)
/*     */   {
/*  56 */     super(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   protected void addSubComponents() {
/*  60 */     add(this.systemLabel);
/*  61 */     add(this.iconButton);
/*  62 */     add(this.maxButton);
/*  63 */     add(this.closeButton);
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/*  67 */     super.installDefaults();
/*     */ 
/*  69 */     this.titlePaneHeight = UIManager.getInt("InternalFrame.titlePaneHeight");
/*  70 */     this.buttonWidth = (UIManager.getInt("InternalFrame.titleButtonWidth") - 4);
/*  71 */     this.buttonHeight = (UIManager.getInt("InternalFrame.titleButtonHeight") - 4);
/*     */ 
/*  73 */     Object localObject1 = UIManager.get("InternalFrame.titleButtonToolTipsOn");
/*  74 */     this.hotTrackingOn = ((localObject1 instanceof Boolean) ? ((Boolean)localObject1).booleanValue() : true);
/*     */     Object localObject2;
/*  77 */     if (XPStyle.getXP() != null)
/*     */     {
/*  81 */       this.buttonWidth = this.buttonHeight;
/*  82 */       localObject2 = XPStyle.getPartSize(TMSchema.Part.WP_CLOSEBUTTON, TMSchema.State.NORMAL);
/*  83 */       if ((localObject2 != null) && (((Dimension)localObject2).width != 0) && (((Dimension)localObject2).height != 0))
/*  84 */         this.buttonWidth = ((int)(this.buttonWidth * ((Dimension)localObject2).width / ((Dimension)localObject2).height));
/*     */     }
/*     */     else {
/*  87 */       this.buttonWidth += 2;
/*  88 */       localObject2 = UIManager.getColor("InternalFrame.activeBorderColor");
/*     */ 
/*  90 */       setBorder(BorderFactory.createLineBorder((Color)localObject2, 1));
/*     */     }
/*     */ 
/*  93 */     this.selectedTitleGradientColor = UIManager.getColor("InternalFrame.activeTitleGradient");
/*     */ 
/*  95 */     this.notSelectedTitleGradientColor = UIManager.getColor("InternalFrame.inactiveTitleGradient");
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 101 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   protected void createButtons() {
/* 105 */     super.createButtons();
/* 106 */     if (XPStyle.getXP() != null) {
/* 107 */       this.iconButton.setContentAreaFilled(false);
/* 108 */       this.maxButton.setContentAreaFilled(false);
/* 109 */       this.closeButton.setContentAreaFilled(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setButtonIcons() {
/* 114 */     super.setButtonIcons();
/*     */ 
/* 116 */     if (!this.hotTrackingOn) {
/* 117 */       this.iconButton.setToolTipText(null);
/* 118 */       this.maxButton.setToolTipText(null);
/* 119 */       this.closeButton.setToolTipText(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics)
/*     */   {
/* 125 */     XPStyle localXPStyle = XPStyle.getXP();
/*     */ 
/* 127 */     paintTitleBackground(paramGraphics);
/*     */ 
/* 129 */     String str1 = this.frame.getTitle();
/* 130 */     if (str1 != null) {
/* 131 */       boolean bool = this.frame.isSelected();
/* 132 */       Font localFont1 = paramGraphics.getFont();
/* 133 */       Font localFont2 = this.titleFont != null ? this.titleFont : getFont();
/* 134 */       paramGraphics.setFont(localFont2);
/*     */ 
/* 137 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(this.frame, paramGraphics, localFont2);
/* 138 */       int i = (getHeight() + localFontMetrics.getAscent() - localFontMetrics.getLeading() - localFontMetrics.getDescent()) / 2;
/*     */ 
/* 141 */       Rectangle localRectangle = new Rectangle(0, 0, 0, 0);
/* 142 */       if (this.frame.isIconifiable())
/* 143 */         localRectangle = this.iconButton.getBounds();
/* 144 */       else if (this.frame.isMaximizable())
/* 145 */         localRectangle = this.maxButton.getBounds();
/* 146 */       else if (this.frame.isClosable()) {
/* 147 */         localRectangle = this.closeButton.getBounds();
/*     */       }
/*     */ 
/* 152 */       int m = 2;
/*     */       int j;
/*     */       int k;
/* 153 */       if (WindowsGraphicsUtils.isLeftToRight(this.frame)) {
/* 154 */         if (localRectangle.x == 0) {
/* 155 */           localRectangle.x = (this.frame.getWidth() - this.frame.getInsets().right);
/*     */         }
/* 157 */         j = this.systemLabel.getX() + this.systemLabel.getWidth() + m;
/* 158 */         if (localXPStyle != null) {
/* 159 */           j += 2;
/*     */         }
/* 161 */         k = localRectangle.x - j - m;
/*     */       } else {
/* 163 */         if (localRectangle.x == 0) {
/* 164 */           localRectangle.x = this.frame.getInsets().left;
/*     */         }
/* 166 */         k = SwingUtilities2.stringWidth(this.frame, localFontMetrics, str1);
/* 167 */         int n = localRectangle.x + localRectangle.width + m;
/* 168 */         if (localXPStyle != null) {
/* 169 */           n += 2;
/*     */         }
/* 171 */         int i1 = this.systemLabel.getX() - m - n;
/* 172 */         if (i1 > k) {
/* 173 */           j = this.systemLabel.getX() - m - k;
/*     */         } else {
/* 175 */           j = n;
/* 176 */           k = i1;
/*     */         }
/*     */       }
/* 179 */       str1 = getTitle(this.frame.getTitle(), localFontMetrics, k);
/*     */ 
/* 181 */       if (localXPStyle != null) {
/* 182 */         String str2 = null;
/* 183 */         if (bool) {
/* 184 */           str2 = localXPStyle.getString(this, TMSchema.Part.WP_CAPTION, TMSchema.State.ACTIVE, TMSchema.Prop.TEXTSHADOWTYPE);
/*     */         }
/*     */ 
/* 187 */         if ("single".equalsIgnoreCase(str2)) {
/* 188 */           Point localPoint = localXPStyle.getPoint(this, TMSchema.Part.WP_WINDOW, TMSchema.State.ACTIVE, TMSchema.Prop.TEXTSHADOWOFFSET);
/*     */ 
/* 190 */           Color localColor = localXPStyle.getColor(this, TMSchema.Part.WP_WINDOW, TMSchema.State.ACTIVE, TMSchema.Prop.TEXTSHADOWCOLOR, null);
/*     */ 
/* 192 */           if ((localPoint != null) && (localColor != null)) {
/* 193 */             paramGraphics.setColor(localColor);
/* 194 */             SwingUtilities2.drawString(this.frame, paramGraphics, str1, j + localPoint.x, i + localPoint.y);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 200 */       paramGraphics.setColor(bool ? this.selectedTextColor : this.notSelectedTextColor);
/* 201 */       SwingUtilities2.drawString(this.frame, paramGraphics, str1, j, i);
/* 202 */       paramGraphics.setFont(localFont1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize() {
/* 207 */     return getMinimumSize();
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize() {
/* 211 */     Dimension localDimension = new Dimension(super.getMinimumSize());
/* 212 */     localDimension.height = (this.titlePaneHeight + 2);
/*     */ 
/* 214 */     XPStyle localXPStyle = XPStyle.getXP();
/* 215 */     if (localXPStyle != null)
/*     */     {
/* 218 */       if (this.frame.isMaximum())
/* 219 */         localDimension.height -= 1;
/*     */       else {
/* 221 */         localDimension.height += 3;
/*     */       }
/*     */     }
/* 224 */     return localDimension;
/*     */   }
/*     */ 
/*     */   protected void paintTitleBackground(Graphics paramGraphics) {
/* 228 */     XPStyle localXPStyle = XPStyle.getXP();
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 229 */     if (localXPStyle != null) {
/* 230 */       localObject1 = this.frame.isMaximum() ? TMSchema.Part.WP_MAXCAPTION : this.frame.isIcon() ? TMSchema.Part.WP_MINCAPTION : TMSchema.Part.WP_CAPTION;
/*     */ 
/* 233 */       localObject2 = this.frame.isSelected() ? TMSchema.State.ACTIVE : TMSchema.State.INACTIVE;
/* 234 */       localObject3 = localXPStyle.getSkin(this, (TMSchema.Part)localObject1);
/* 235 */       ((XPStyle.Skin)localObject3).paintSkin(paramGraphics, 0, 0, getWidth(), getHeight(), (TMSchema.State)localObject2);
/*     */     } else {
/* 237 */       localObject1 = (Boolean)LookAndFeel.getDesktopPropertyValue("win.frame.captionGradientsOn", Boolean.valueOf(false));
/*     */ 
/* 239 */       if ((((Boolean)localObject1).booleanValue()) && ((paramGraphics instanceof Graphics2D))) {
/* 240 */         localObject2 = (Graphics2D)paramGraphics;
/* 241 */         localObject3 = ((Graphics2D)localObject2).getPaint();
/*     */ 
/* 243 */         boolean bool = this.frame.isSelected();
/* 244 */         int i = getWidth();
/*     */         GradientPaint localGradientPaint;
/* 246 */         if (bool) {
/* 247 */           localGradientPaint = new GradientPaint(0.0F, 0.0F, this.selectedTitleColor, (int)(i * 0.75D), 0.0F, this.selectedTitleGradientColor);
/*     */ 
/* 251 */           ((Graphics2D)localObject2).setPaint(localGradientPaint);
/*     */         } else {
/* 253 */           localGradientPaint = new GradientPaint(0.0F, 0.0F, this.notSelectedTitleColor, (int)(i * 0.75D), 0.0F, this.notSelectedTitleGradientColor);
/*     */ 
/* 257 */           ((Graphics2D)localObject2).setPaint(localGradientPaint);
/*     */         }
/* 259 */         ((Graphics2D)localObject2).fillRect(0, 0, getWidth(), getHeight());
/* 260 */         ((Graphics2D)localObject2).setPaint((Paint)localObject3);
/*     */       } else {
/* 262 */         super.paintTitleBackground(paramGraphics);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void assembleSystemMenu() {
/* 268 */     this.systemPopupMenu = new JPopupMenu();
/* 269 */     addSystemMenuItems(this.systemPopupMenu);
/* 270 */     enableActions();
/* 271 */     this.systemLabel = new JLabel(this.frame.getFrameIcon()) {
/*     */       protected void paintComponent(Graphics paramAnonymousGraphics) {
/* 273 */         int i = 0;
/* 274 */         int j = 0;
/* 275 */         int k = getWidth();
/* 276 */         int m = getHeight();
/* 277 */         paramAnonymousGraphics = paramAnonymousGraphics.create();
/* 278 */         if (isOpaque()) {
/* 279 */           paramAnonymousGraphics.setColor(getBackground());
/* 280 */           paramAnonymousGraphics.fillRect(0, 0, k, m);
/*     */         }
/* 282 */         Icon localIcon = getIcon();
/*     */         int n;
/*     */         int i1;
/* 285 */         if ((localIcon != null) && ((n = localIcon.getIconWidth()) > 0) && ((i1 = localIcon.getIconHeight()) > 0))
/*     */         {
/*     */           double d;
/* 291 */           if (n > i1)
/*     */           {
/* 293 */             j = (m - k * i1 / n) / 2;
/* 294 */             d = k / n;
/*     */           }
/*     */           else {
/* 297 */             i = (k - m * n / i1) / 2;
/* 298 */             d = m / i1;
/*     */           }
/* 300 */           ((Graphics2D)paramAnonymousGraphics).translate(i, j);
/* 301 */           ((Graphics2D)paramAnonymousGraphics).scale(d, d);
/* 302 */           localIcon.paintIcon(this, paramAnonymousGraphics, 0, 0);
/*     */         }
/* 304 */         paramAnonymousGraphics.dispose();
/*     */       }
/*     */     };
/* 307 */     this.systemLabel.addMouseListener(new MouseAdapter() {
/*     */       public void mouseClicked(MouseEvent paramAnonymousMouseEvent) {
/* 309 */         if ((paramAnonymousMouseEvent.getClickCount() == 2) && (WindowsInternalFrameTitlePane.this.frame.isClosable()) && (!WindowsInternalFrameTitlePane.this.frame.isIcon()))
/*     */         {
/* 311 */           WindowsInternalFrameTitlePane.this.systemPopupMenu.setVisible(false);
/* 312 */           WindowsInternalFrameTitlePane.this.frame.doDefaultCloseAction();
/*     */         }
/*     */         else {
/* 315 */           super.mouseClicked(paramAnonymousMouseEvent);
/*     */         }
/*     */       }
/*     */ 
/*     */       public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/*     */         try { WindowsInternalFrameTitlePane.this.frame.setSelected(true);
/*     */         } catch (PropertyVetoException localPropertyVetoException) {
/*     */         }
/* 323 */         WindowsInternalFrameTitlePane.this.showSystemPopupMenu(paramAnonymousMouseEvent.getComponent());
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected void addSystemMenuItems(JPopupMenu paramJPopupMenu) {
/* 329 */     JMenuItem localJMenuItem = paramJPopupMenu.add(this.restoreAction);
/* 330 */     localJMenuItem.setMnemonic('R');
/* 331 */     localJMenuItem = paramJPopupMenu.add(this.moveAction);
/* 332 */     localJMenuItem.setMnemonic('M');
/* 333 */     localJMenuItem = paramJPopupMenu.add(this.sizeAction);
/* 334 */     localJMenuItem.setMnemonic('S');
/* 335 */     localJMenuItem = paramJPopupMenu.add(this.iconifyAction);
/* 336 */     localJMenuItem.setMnemonic('n');
/* 337 */     localJMenuItem = paramJPopupMenu.add(this.maximizeAction);
/* 338 */     localJMenuItem.setMnemonic('x');
/* 339 */     this.systemPopupMenu.add(new JSeparator());
/* 340 */     localJMenuItem = paramJPopupMenu.add(this.closeAction);
/* 341 */     localJMenuItem.setMnemonic('C');
/*     */   }
/*     */ 
/*     */   protected void showSystemMenu() {
/* 345 */     showSystemPopupMenu(this.systemLabel);
/*     */   }
/*     */ 
/*     */   private void showSystemPopupMenu(Component paramComponent) {
/* 349 */     Dimension localDimension = new Dimension();
/* 350 */     Border localBorder = this.frame.getBorder();
/* 351 */     if (localBorder != null) {
/* 352 */       localDimension.width += localBorder.getBorderInsets(this.frame).left + localBorder.getBorderInsets(this.frame).right;
/*     */ 
/* 354 */       localDimension.height += localBorder.getBorderInsets(this.frame).bottom + localBorder.getBorderInsets(this.frame).top;
/*     */     }
/*     */ 
/* 357 */     if (!this.frame.isIcon()) {
/* 358 */       this.systemPopupMenu.show(paramComponent, getX() - localDimension.width, getY() + getHeight() - localDimension.height);
/*     */     }
/*     */     else
/*     */     {
/* 362 */       this.systemPopupMenu.show(paramComponent, getX() - localDimension.width, getY() - this.systemPopupMenu.getPreferredSize().height - localDimension.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener()
/*     */   {
/* 370 */     return new WindowsPropertyChangeHandler();
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayout() {
/* 374 */     return new WindowsTitlePaneLayout();
/*     */   }
/*     */ 
/*     */   public static class ScalableIconUIResource
/*     */     implements Icon, UIResource
/*     */   {
/*     */     private static final int SIZE = 16;
/*     */     private Icon[] icons;
/*     */ 
/*     */     public ScalableIconUIResource(Object[] paramArrayOfObject)
/*     */     {
/* 499 */       this.icons = new UIResource[paramArrayOfObject.length];
/*     */ 
/* 501 */       for (int i = 0; i < paramArrayOfObject.length; i++)
/* 502 */         if ((paramArrayOfObject[i] instanceof UIDefaults.LazyValue))
/* 503 */           this.icons[i] = ((UIResource)((UIDefaults.LazyValue)paramArrayOfObject[i]).createValue(null));
/*     */         else
/* 505 */           this.icons[i] = ((UIResource)paramArrayOfObject[i]);
/*     */     }
/*     */ 
/*     */     protected Icon getBestIcon(int paramInt)
/*     */     {
/* 514 */       if ((this.icons != null) && (this.icons.length > 0)) {
/* 515 */         int i = 0;
/* 516 */         int j = 2147483647;
/* 517 */         for (int k = 0; k < this.icons.length; k++) {
/* 518 */           Icon localIcon = this.icons[k];
/*     */           int m;
/* 520 */           if ((localIcon != null) && ((m = localIcon.getIconWidth()) > 0)) {
/* 521 */             int n = Math.abs(m - paramInt);
/* 522 */             if (n < j) {
/* 523 */               j = n;
/* 524 */               i = k;
/*     */             }
/*     */           }
/*     */         }
/* 528 */         return this.icons[i];
/*     */       }
/* 530 */       return null;
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/* 535 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
/*     */ 
/* 538 */       int i = getIconWidth();
/* 539 */       double d1 = localGraphics2D.getTransform().getScaleX();
/* 540 */       Icon localIcon = getBestIcon((int)(i * d1));
/*     */       int j;
/* 542 */       if ((localIcon != null) && ((j = localIcon.getIconWidth()) > 0))
/*     */       {
/* 544 */         double d2 = i / j;
/* 545 */         localGraphics2D.translate(paramInt1, paramInt2);
/* 546 */         localGraphics2D.scale(d2, d2);
/* 547 */         localIcon.paintIcon(paramComponent, localGraphics2D, 0, 0);
/*     */       }
/* 549 */       localGraphics2D.dispose();
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 553 */       return 16;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 557 */       return 16;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class WindowsPropertyChangeHandler extends BasicInternalFrameTitlePane.PropertyChangeHandler
/*     */   {
/*     */     public WindowsPropertyChangeHandler()
/*     */     {
/* 464 */       super();
/*     */     }
/* 466 */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) { String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 469 */       if (("frameIcon".equals(str)) && (WindowsInternalFrameTitlePane.this.systemLabel != null))
/*     */       {
/* 471 */         WindowsInternalFrameTitlePane.this.systemLabel.setIcon(WindowsInternalFrameTitlePane.this.frame.getFrameIcon());
/*     */       }
/*     */ 
/* 474 */       super.propertyChange(paramPropertyChangeEvent);
/*     */     }
/*     */   }
/*     */ 
/*     */   public class WindowsTitlePaneLayout extends BasicInternalFrameTitlePane.TitlePaneLayout
/*     */   {
/* 378 */     private Insets captionMargin = null;
/* 379 */     private Insets contentMargin = null;
/* 380 */     private XPStyle xp = XPStyle.getXP();
/*     */ 
/* 382 */     WindowsTitlePaneLayout() { super();
/* 383 */       if (this.xp != null) {
/* 384 */         WindowsInternalFrameTitlePane localWindowsInternalFrameTitlePane = WindowsInternalFrameTitlePane.this;
/* 385 */         this.captionMargin = this.xp.getMargin(localWindowsInternalFrameTitlePane, TMSchema.Part.WP_CAPTION, null, TMSchema.Prop.CAPTIONMARGINS);
/* 386 */         this.contentMargin = this.xp.getMargin(localWindowsInternalFrameTitlePane, TMSchema.Part.WP_CAPTION, null, TMSchema.Prop.CONTENTMARGINS);
/*     */       }
/* 388 */       if (this.captionMargin == null) {
/* 389 */         this.captionMargin = new Insets(0, 2, 0, 2);
/*     */       }
/* 391 */       if (this.contentMargin == null)
/* 392 */         this.contentMargin = new Insets(0, 0, 0, 0);
/*     */     }
/*     */ 
/*     */     private int layoutButton(JComponent paramJComponent, TMSchema.Part paramPart, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
/*     */     {
/* 399 */       if (!paramBoolean) {
/* 400 */         paramInt1 -= paramInt3;
/*     */       }
/* 402 */       paramJComponent.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/* 403 */       if (paramBoolean)
/* 404 */         paramInt1 += paramInt3 + 2;
/*     */       else {
/* 406 */         paramInt1 -= 2;
/*     */       }
/* 408 */       return paramInt1;
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer) {
/* 412 */       boolean bool = WindowsGraphicsUtils.isLeftToRight(WindowsInternalFrameTitlePane.this.frame);
/*     */ 
/* 414 */       int k = WindowsInternalFrameTitlePane.this.getWidth();
/* 415 */       int m = WindowsInternalFrameTitlePane.this.getHeight();
/*     */ 
/* 419 */       int n = this.xp != null ? (m - 2) * 6 / 10 : m - 4;
/*     */       int i;
/* 420 */       if (this.xp != null)
/* 421 */         i = bool ? this.captionMargin.left + 2 : k - this.captionMargin.right - 2;
/*     */       else {
/* 423 */         i = bool ? this.captionMargin.left : k - this.captionMargin.right;
/*     */       }
/* 425 */       int j = (m - n) / 2;
/* 426 */       layoutButton(WindowsInternalFrameTitlePane.this.systemLabel, TMSchema.Part.WP_SYSBUTTON, i, j, n, n, 0, bool);
/*     */ 
/* 431 */       if (this.xp != null) {
/* 432 */         i = bool ? k - this.captionMargin.right - 2 : this.captionMargin.left + 2;
/* 433 */         j = 1;
/* 434 */         if (WindowsInternalFrameTitlePane.this.frame.isMaximum())
/* 435 */           j++;
/*     */         else
/* 437 */           j += 5;
/*     */       }
/*     */       else {
/* 440 */         i = bool ? k - this.captionMargin.right : this.captionMargin.left;
/* 441 */         j = (m - WindowsInternalFrameTitlePane.this.buttonHeight) / 2;
/*     */       }
/*     */ 
/* 444 */       if (WindowsInternalFrameTitlePane.this.frame.isClosable()) {
/* 445 */         i = layoutButton(WindowsInternalFrameTitlePane.this.closeButton, TMSchema.Part.WP_CLOSEBUTTON, i, j, WindowsInternalFrameTitlePane.this.buttonWidth, WindowsInternalFrameTitlePane.this.buttonHeight, 2, !bool);
/*     */       }
/*     */ 
/* 450 */       if (WindowsInternalFrameTitlePane.this.frame.isMaximizable()) {
/* 451 */         i = layoutButton(WindowsInternalFrameTitlePane.this.maxButton, TMSchema.Part.WP_MAXBUTTON, i, j, WindowsInternalFrameTitlePane.this.buttonWidth, WindowsInternalFrameTitlePane.this.buttonHeight, this.xp != null ? 2 : 0, !bool);
/*     */       }
/*     */ 
/* 456 */       if (WindowsInternalFrameTitlePane.this.frame.isIconifiable())
/* 457 */         layoutButton(WindowsInternalFrameTitlePane.this.iconButton, TMSchema.Part.WP_MINBUTTON, i, j, WindowsInternalFrameTitlePane.this.buttonWidth, WindowsInternalFrameTitlePane.this.buttonHeight, 0, !bool);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane
 * JD-Core Version:    0.6.2
 */
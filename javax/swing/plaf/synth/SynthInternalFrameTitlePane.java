/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ class SynthInternalFrameTitlePane extends BasicInternalFrameTitlePane
/*     */   implements SynthUI, PropertyChangeListener
/*     */ {
/*     */   protected JPopupMenu systemPopupMenu;
/*     */   protected JButton menuButton;
/*     */   private SynthStyle style;
/*     */   private int titleSpacing;
/*     */   private int buttonSpacing;
/*     */   private int titleAlignment;
/*     */ 
/*     */   public SynthInternalFrameTitlePane(JInternalFrame paramJInternalFrame)
/*     */   {
/*  58 */     super(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   public String getUIClassID() {
/*  62 */     return "InternalFrameTitlePaneUI";
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent) {
/*  66 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent, int paramInt) {
/*  70 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private Region getRegion(JComponent paramJComponent)
/*     */   {
/*  75 */     return SynthLookAndFeel.getRegion(paramJComponent);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent) {
/*  79 */     if ((this.frame != null) && 
/*  80 */       (this.frame.isSelected())) {
/*  81 */       return 512;
/*     */     }
/*     */ 
/*  84 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void addSubComponents() {
/*  88 */     this.menuButton.setName("InternalFrameTitlePane.menuButton");
/*  89 */     this.iconButton.setName("InternalFrameTitlePane.iconifyButton");
/*  90 */     this.maxButton.setName("InternalFrameTitlePane.maximizeButton");
/*  91 */     this.closeButton.setName("InternalFrameTitlePane.closeButton");
/*     */ 
/*  93 */     add(this.menuButton);
/*  94 */     add(this.iconButton);
/*  95 */     add(this.maxButton);
/*  96 */     add(this.closeButton);
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/* 100 */     super.installListeners();
/* 101 */     this.frame.addPropertyChangeListener(this);
/* 102 */     addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners() {
/* 106 */     this.frame.removePropertyChangeListener(this);
/* 107 */     removePropertyChangeListener(this);
/* 108 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/* 112 */     SynthContext localSynthContext = getContext(this, 1);
/* 113 */     SynthStyle localSynthStyle = this.style;
/* 114 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 115 */     if (this.style != localSynthStyle) {
/* 116 */       this.maxIcon = this.style.getIcon(localSynthContext, "InternalFrameTitlePane.maximizeIcon");
/*     */ 
/* 118 */       this.minIcon = this.style.getIcon(localSynthContext, "InternalFrameTitlePane.minimizeIcon");
/*     */ 
/* 120 */       this.iconIcon = this.style.getIcon(localSynthContext, "InternalFrameTitlePane.iconifyIcon");
/*     */ 
/* 122 */       this.closeIcon = this.style.getIcon(localSynthContext, "InternalFrameTitlePane.closeIcon");
/*     */ 
/* 124 */       this.titleSpacing = this.style.getInt(localSynthContext, "InternalFrameTitlePane.titleSpacing", 2);
/*     */ 
/* 126 */       this.buttonSpacing = this.style.getInt(localSynthContext, "InternalFrameTitlePane.buttonSpacing", 2);
/*     */ 
/* 128 */       String str = (String)this.style.get(localSynthContext, "InternalFrameTitlePane.titleAlignment");
/*     */ 
/* 130 */       this.titleAlignment = 10;
/* 131 */       if (str != null) {
/* 132 */         str = str.toUpperCase();
/* 133 */         if (str.equals("TRAILING")) {
/* 134 */           this.titleAlignment = 11;
/*     */         }
/* 136 */         else if (str.equals("CENTER")) {
/* 137 */           this.titleAlignment = 0;
/*     */         }
/*     */       }
/*     */     }
/* 141 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/* 145 */     super.installDefaults();
/* 146 */     updateStyle(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults() {
/* 150 */     SynthContext localSynthContext = getContext(this, 1);
/* 151 */     this.style.uninstallDefaults(localSynthContext);
/* 152 */     localSynthContext.dispose();
/* 153 */     this.style = null;
/* 154 */     JInternalFrame.JDesktopIcon localJDesktopIcon = this.frame.getDesktopIcon();
/* 155 */     if ((localJDesktopIcon != null) && (localJDesktopIcon.getComponentPopupMenu() == this.systemPopupMenu))
/*     */     {
/* 157 */       localJDesktopIcon.setComponentPopupMenu(null);
/*     */     }
/* 159 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   protected void assembleSystemMenu()
/*     */   {
/* 166 */     this.systemPopupMenu = new JPopupMenuUIResource(null);
/* 167 */     addSystemMenuItems(this.systemPopupMenu);
/* 168 */     enableActions();
/* 169 */     this.menuButton = createNoFocusButton();
/* 170 */     updateMenuIcon();
/* 171 */     this.menuButton.addMouseListener(new MouseAdapter() {
/*     */       public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/*     */         try {
/* 174 */           SynthInternalFrameTitlePane.this.frame.setSelected(true);
/*     */         } catch (PropertyVetoException localPropertyVetoException) {
/*     */         }
/* 177 */         SynthInternalFrameTitlePane.this.showSystemMenu();
/*     */       }
/*     */     });
/* 180 */     JPopupMenu localJPopupMenu = this.frame.getComponentPopupMenu();
/* 181 */     if ((localJPopupMenu == null) || ((localJPopupMenu instanceof UIResource))) {
/* 182 */       this.frame.setComponentPopupMenu(this.systemPopupMenu);
/*     */     }
/* 184 */     if (this.frame.getDesktopIcon() != null) {
/* 185 */       localJPopupMenu = this.frame.getDesktopIcon().getComponentPopupMenu();
/* 186 */       if ((localJPopupMenu == null) || ((localJPopupMenu instanceof UIResource))) {
/* 187 */         this.frame.getDesktopIcon().setComponentPopupMenu(this.systemPopupMenu);
/*     */       }
/*     */     }
/* 190 */     setInheritsPopupMenu(true);
/*     */   }
/*     */ 
/*     */   protected void addSystemMenuItems(JPopupMenu paramJPopupMenu)
/*     */   {
/* 195 */     JMenuItem localJMenuItem = paramJPopupMenu.add(this.restoreAction);
/* 196 */     localJMenuItem.setMnemonic('R');
/* 197 */     localJMenuItem = paramJPopupMenu.add(this.moveAction);
/* 198 */     localJMenuItem.setMnemonic('M');
/* 199 */     localJMenuItem = paramJPopupMenu.add(this.sizeAction);
/* 200 */     localJMenuItem.setMnemonic('S');
/* 201 */     localJMenuItem = paramJPopupMenu.add(this.iconifyAction);
/* 202 */     localJMenuItem.setMnemonic('n');
/* 203 */     localJMenuItem = paramJPopupMenu.add(this.maximizeAction);
/* 204 */     localJMenuItem.setMnemonic('x');
/* 205 */     paramJPopupMenu.add(new JSeparator());
/* 206 */     localJMenuItem = paramJPopupMenu.add(this.closeAction);
/* 207 */     localJMenuItem.setMnemonic('C');
/*     */   }
/*     */ 
/*     */   protected void showSystemMenu() {
/* 211 */     Insets localInsets = this.frame.getInsets();
/* 212 */     if (!this.frame.isIcon())
/* 213 */       this.systemPopupMenu.show(this.frame, this.menuButton.getX(), getY() + getHeight());
/*     */     else
/* 215 */       this.systemPopupMenu.show(this.menuButton, getX() - localInsets.left - localInsets.right, getY() - this.systemPopupMenu.getPreferredSize().height - localInsets.bottom - localInsets.top);
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics)
/*     */   {
/* 224 */     SynthContext localSynthContext = getContext(this);
/* 225 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 226 */     localSynthContext.getPainter().paintInternalFrameTitlePaneBackground(localSynthContext, paramGraphics, 0, 0, getWidth(), getHeight());
/*     */ 
/* 228 */     paint(localSynthContext, paramGraphics);
/* 229 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics) {
/* 233 */     String str1 = this.frame.getTitle();
/*     */ 
/* 235 */     if (str1 != null) {
/* 236 */       SynthStyle localSynthStyle = paramSynthContext.getStyle();
/*     */ 
/* 238 */       paramGraphics.setColor(localSynthStyle.getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/* 239 */       paramGraphics.setFont(localSynthStyle.getFont(paramSynthContext));
/*     */ 
/* 242 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(this.frame, paramGraphics);
/* 243 */       int i = (getHeight() + localFontMetrics.getAscent() - localFontMetrics.getLeading() - localFontMetrics.getDescent()) / 2;
/*     */ 
/* 245 */       JButton localJButton = null;
/* 246 */       if (this.frame.isIconifiable()) {
/* 247 */         localJButton = this.iconButton;
/*     */       }
/* 249 */       else if (this.frame.isMaximizable()) {
/* 250 */         localJButton = this.maxButton;
/*     */       }
/* 252 */       else if (this.frame.isClosable()) {
/* 253 */         localJButton = this.closeButton;
/*     */       }
/*     */ 
/* 257 */       boolean bool = SynthLookAndFeel.isLeftToRight(this.frame);
/* 258 */       int m = this.titleAlignment;
/*     */       int j;
/*     */       int k;
/* 259 */       if (bool) {
/* 260 */         if (localJButton != null) {
/* 261 */           j = localJButton.getX() - this.titleSpacing;
/*     */         }
/*     */         else {
/* 264 */           j = this.frame.getWidth() - this.frame.getInsets().right - this.titleSpacing;
/*     */         }
/*     */ 
/* 267 */         k = this.menuButton.getX() + this.menuButton.getWidth() + this.titleSpacing;
/*     */       }
/*     */       else
/*     */       {
/* 271 */         if (localJButton != null) {
/* 272 */           k = localJButton.getX() + localJButton.getWidth() + this.titleSpacing;
/*     */         }
/*     */         else
/*     */         {
/* 276 */           k = this.frame.getInsets().left + this.titleSpacing;
/*     */         }
/* 278 */         j = this.menuButton.getX() - this.titleSpacing;
/* 279 */         if (m == 10) {
/* 280 */           m = 11;
/*     */         }
/* 282 */         else if (m == 11) {
/* 283 */           m = 10;
/*     */         }
/*     */       }
/* 286 */       String str2 = getTitle(str1, localFontMetrics, j - k);
/* 287 */       if (str2 == str1)
/*     */       {
/* 289 */         if (m == 11) {
/* 290 */           k = j - localSynthStyle.getGraphicsUtils(paramSynthContext).computeStringWidth(paramSynthContext, paramGraphics.getFont(), localFontMetrics, str1);
/*     */         }
/* 293 */         else if (m == 0) {
/* 294 */           int n = localSynthStyle.getGraphicsUtils(paramSynthContext).computeStringWidth(paramSynthContext, paramGraphics.getFont(), localFontMetrics, str1);
/*     */ 
/* 296 */           k = Math.max(k, (getWidth() - n) / 2);
/* 297 */           k = Math.min(j - n, k);
/*     */         }
/*     */       }
/* 300 */       localSynthStyle.getGraphicsUtils(paramSynthContext).paintText(paramSynthContext, paramGraphics, str2, k, i - localFontMetrics.getAscent(), -1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 307 */     paramSynthContext.getPainter().paintInternalFrameTitlePaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayout()
/*     */   {
/* 312 */     SynthContext localSynthContext = getContext(this);
/* 313 */     LayoutManager localLayoutManager = (LayoutManager)this.style.get(localSynthContext, "InternalFrameTitlePane.titlePaneLayout");
/*     */ 
/* 315 */     localSynthContext.dispose();
/* 316 */     return localLayoutManager != null ? localLayoutManager : new SynthTitlePaneLayout();
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 320 */     if (paramPropertyChangeEvent.getSource() == this) {
/* 321 */       if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 322 */         updateStyle(this);
/*     */       }
/*     */ 
/*     */     }
/* 327 */     else if (paramPropertyChangeEvent.getPropertyName() == "frameIcon")
/* 328 */       updateMenuIcon();
/*     */   }
/*     */ 
/*     */   private void updateMenuIcon()
/*     */   {
/* 337 */     Object localObject = this.frame.getFrameIcon();
/* 338 */     SynthContext localSynthContext = getContext(this);
/* 339 */     if (localObject != null) {
/* 340 */       Dimension localDimension = (Dimension)localSynthContext.getStyle().get(localSynthContext, "InternalFrameTitlePane.maxFrameIconSize");
/*     */ 
/* 342 */       int i = 16;
/* 343 */       int j = 16;
/* 344 */       if (localDimension != null) {
/* 345 */         i = localDimension.width;
/* 346 */         j = localDimension.height;
/*     */       }
/* 348 */       if (((((Icon)localObject).getIconWidth() > i) || (((Icon)localObject).getIconHeight() > j)) && ((localObject instanceof ImageIcon)))
/*     */       {
/* 351 */         localObject = new ImageIcon(((ImageIcon)localObject).getImage().getScaledInstance(i, j, 4));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 356 */     localSynthContext.dispose();
/* 357 */     this.menuButton.setIcon((Icon)localObject);
/*     */   }
/*     */ 
/*     */   private JButton createNoFocusButton()
/*     */   {
/* 484 */     JButton localJButton = new JButton();
/* 485 */     localJButton.setFocusable(false);
/* 486 */     localJButton.setMargin(new Insets(0, 0, 0, 0));
/* 487 */     return localJButton;
/*     */   }
/*     */ 
/*     */   private static class JPopupMenuUIResource extends JPopupMenu
/*     */     implements UIResource
/*     */   {
/*     */   }
/*     */ 
/*     */   class SynthTitlePaneLayout
/*     */     implements LayoutManager
/*     */   {
/*     */     SynthTitlePaneLayout()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void addLayoutComponent(String paramString, Component paramComponent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void removeLayoutComponent(Component paramComponent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public Dimension preferredLayoutSize(Container paramContainer)
/*     */     {
/* 365 */       return minimumLayoutSize(paramContainer);
/*     */     }
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 369 */       SynthContext localSynthContext = SynthInternalFrameTitlePane.this.getContext(SynthInternalFrameTitlePane.this);
/*     */ 
/* 371 */       int i = 0;
/* 372 */       int j = 0;
/*     */ 
/* 374 */       int k = 0;
/*     */ 
/* 377 */       if (SynthInternalFrameTitlePane.this.frame.isClosable()) {
/* 378 */         localDimension = SynthInternalFrameTitlePane.this.closeButton.getPreferredSize();
/* 379 */         i += localDimension.width;
/* 380 */         j = Math.max(localDimension.height, j);
/* 381 */         k++;
/*     */       }
/* 383 */       if (SynthInternalFrameTitlePane.this.frame.isMaximizable()) {
/* 384 */         localDimension = SynthInternalFrameTitlePane.this.maxButton.getPreferredSize();
/* 385 */         i += localDimension.width;
/* 386 */         j = Math.max(localDimension.height, j);
/* 387 */         k++;
/*     */       }
/* 389 */       if (SynthInternalFrameTitlePane.this.frame.isIconifiable()) {
/* 390 */         localDimension = SynthInternalFrameTitlePane.this.iconButton.getPreferredSize();
/* 391 */         i += localDimension.width;
/* 392 */         j = Math.max(localDimension.height, j);
/* 393 */         k++;
/*     */       }
/* 395 */       Dimension localDimension = SynthInternalFrameTitlePane.this.menuButton.getPreferredSize();
/* 396 */       i += localDimension.width;
/* 397 */       j = Math.max(localDimension.height, j);
/*     */ 
/* 399 */       i += Math.max(0, (k - 1) * SynthInternalFrameTitlePane.this.buttonSpacing);
/*     */ 
/* 401 */       FontMetrics localFontMetrics = SynthInternalFrameTitlePane.this.getFontMetrics(SynthInternalFrameTitlePane.this.getFont());
/*     */ 
/* 403 */       SynthGraphicsUtils localSynthGraphicsUtils = localSynthContext.getStyle().getGraphicsUtils(localSynthContext);
/*     */ 
/* 405 */       String str = SynthInternalFrameTitlePane.this.frame.getTitle();
/* 406 */       int m = str != null ? localSynthGraphicsUtils.computeStringWidth(localSynthContext, localFontMetrics.getFont(), localFontMetrics, str) : 0;
/*     */ 
/* 409 */       int n = str != null ? str.length() : 0;
/*     */ 
/* 412 */       if (n > 3) {
/* 413 */         int i1 = localSynthGraphicsUtils.computeStringWidth(localSynthContext, localFontMetrics.getFont(), localFontMetrics, str.substring(0, 3) + "...");
/*     */ 
/* 415 */         i += (m < i1 ? m : i1);
/*     */       } else {
/* 417 */         i += m;
/*     */       }
/*     */ 
/* 420 */       j = Math.max(localFontMetrics.getHeight() + 2, j);
/*     */ 
/* 422 */       i += SynthInternalFrameTitlePane.this.titleSpacing + SynthInternalFrameTitlePane.this.titleSpacing;
/*     */ 
/* 424 */       Insets localInsets = SynthInternalFrameTitlePane.this.getInsets();
/* 425 */       j += localInsets.top + localInsets.bottom;
/* 426 */       i += localInsets.left + localInsets.right;
/* 427 */       localSynthContext.dispose();
/* 428 */       return new Dimension(i, j);
/*     */     }
/*     */ 
/*     */     private int center(Component paramComponent, Insets paramInsets, int paramInt, boolean paramBoolean)
/*     */     {
/* 433 */       Dimension localDimension = paramComponent.getPreferredSize();
/* 434 */       if (paramBoolean) {
/* 435 */         paramInt -= localDimension.width;
/*     */       }
/* 437 */       paramComponent.setBounds(paramInt, paramInsets.top + (SynthInternalFrameTitlePane.this.getHeight() - paramInsets.top - paramInsets.bottom - localDimension.height) / 2, localDimension.width, localDimension.height);
/*     */ 
/* 440 */       if (localDimension.width > 0) {
/* 441 */         if (paramBoolean) {
/* 442 */           return paramInt - SynthInternalFrameTitlePane.this.buttonSpacing;
/*     */         }
/* 444 */         return paramInt + localDimension.width + SynthInternalFrameTitlePane.this.buttonSpacing;
/*     */       }
/* 446 */       return paramInt;
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer) {
/* 450 */       Insets localInsets = paramContainer.getInsets();
/*     */       int i;
/* 453 */       if (SynthLookAndFeel.isLeftToRight(SynthInternalFrameTitlePane.this.frame)) {
/* 454 */         center(SynthInternalFrameTitlePane.this.menuButton, localInsets, localInsets.left, false);
/* 455 */         i = SynthInternalFrameTitlePane.this.getWidth() - localInsets.right;
/* 456 */         if (SynthInternalFrameTitlePane.this.frame.isClosable()) {
/* 457 */           i = center(SynthInternalFrameTitlePane.this.closeButton, localInsets, i, true);
/*     */         }
/* 459 */         if (SynthInternalFrameTitlePane.this.frame.isMaximizable()) {
/* 460 */           i = center(SynthInternalFrameTitlePane.this.maxButton, localInsets, i, true);
/*     */         }
/* 462 */         if (SynthInternalFrameTitlePane.this.frame.isIconifiable())
/* 463 */           i = center(SynthInternalFrameTitlePane.this.iconButton, localInsets, i, true);
/*     */       }
/*     */       else
/*     */       {
/* 467 */         center(SynthInternalFrameTitlePane.this.menuButton, localInsets, SynthInternalFrameTitlePane.this.getWidth() - localInsets.right, true);
/*     */ 
/* 469 */         i = localInsets.left;
/* 470 */         if (SynthInternalFrameTitlePane.this.frame.isClosable()) {
/* 471 */           i = center(SynthInternalFrameTitlePane.this.closeButton, localInsets, i, false);
/*     */         }
/* 473 */         if (SynthInternalFrameTitlePane.this.frame.isMaximizable()) {
/* 474 */           i = center(SynthInternalFrameTitlePane.this.maxButton, localInsets, i, false);
/*     */         }
/* 476 */         if (SynthInternalFrameTitlePane.this.frame.isIconifiable())
/* 477 */           i = center(SynthInternalFrameTitlePane.this.iconButton, localInsets, i, false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthInternalFrameTitlePane
 * JD-Core Version:    0.6.2
 */
/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane.PropertyChangeHandler;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MetalInternalFrameTitlePane extends BasicInternalFrameTitlePane
/*     */ {
/*  49 */   protected boolean isPalette = false;
/*     */   protected Icon paletteCloseIcon;
/*     */   protected int paletteTitleHeight;
/*  53 */   private static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);
/*     */   private String selectedBackgroundKey;
/*     */   private String selectedForegroundKey;
/*     */   private String selectedShadowKey;
/*     */   private boolean wasClosable;
/*  76 */   int buttonsWidth = 0;
/*     */ 
/*  78 */   MetalBumps activeBumps = new MetalBumps(0, 0, MetalLookAndFeel.getPrimaryControlHighlight(), MetalLookAndFeel.getPrimaryControlDarkShadow(), UIManager.get("InternalFrame.activeTitleGradient") != null ? null : MetalLookAndFeel.getPrimaryControl());
/*     */ 
/*  84 */   MetalBumps inactiveBumps = new MetalBumps(0, 0, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), UIManager.get("InternalFrame.inactiveTitleGradient") != null ? null : MetalLookAndFeel.getControl());
/*     */   MetalBumps paletteBumps;
/*  92 */   private Color activeBumpsHighlight = MetalLookAndFeel.getPrimaryControlHighlight();
/*     */ 
/*  94 */   private Color activeBumpsShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*     */ 
/*     */   public MetalInternalFrameTitlePane(JInternalFrame paramJInternalFrame)
/*     */   {
/*  98 */     super(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   public void addNotify() {
/* 102 */     super.addNotify();
/*     */ 
/* 107 */     updateOptionPaneState();
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/* 111 */     super.installDefaults();
/* 112 */     setFont(UIManager.getFont("InternalFrame.titleFont"));
/* 113 */     this.paletteTitleHeight = UIManager.getInt("InternalFrame.paletteTitleHeight");
/*     */ 
/* 115 */     this.paletteCloseIcon = UIManager.getIcon("InternalFrame.paletteCloseIcon");
/* 116 */     this.wasClosable = this.frame.isClosable();
/* 117 */     this.selectedForegroundKey = (this.selectedBackgroundKey = null);
/* 118 */     if (MetalLookAndFeel.usingOcean())
/* 119 */       setOpaque(true);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 124 */     super.uninstallDefaults();
/* 125 */     if (this.wasClosable != this.frame.isClosable())
/* 126 */       this.frame.setClosable(this.wasClosable);
/*     */   }
/*     */ 
/*     */   protected void createButtons()
/*     */   {
/* 131 */     super.createButtons();
/*     */ 
/* 133 */     Boolean localBoolean = this.frame.isSelected() ? Boolean.TRUE : Boolean.FALSE;
/* 134 */     this.iconButton.putClientProperty("paintActive", localBoolean);
/* 135 */     this.iconButton.setBorder(handyEmptyBorder);
/*     */ 
/* 137 */     this.maxButton.putClientProperty("paintActive", localBoolean);
/* 138 */     this.maxButton.setBorder(handyEmptyBorder);
/*     */ 
/* 140 */     this.closeButton.putClientProperty("paintActive", localBoolean);
/* 141 */     this.closeButton.setBorder(handyEmptyBorder);
/*     */ 
/* 145 */     this.closeButton.setBackground(MetalLookAndFeel.getPrimaryControlShadow());
/*     */ 
/* 147 */     if (MetalLookAndFeel.usingOcean()) {
/* 148 */       this.iconButton.setContentAreaFilled(false);
/* 149 */       this.maxButton.setContentAreaFilled(false);
/* 150 */       this.closeButton.setContentAreaFilled(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void assembleSystemMenu()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addSystemMenuItems(JMenu paramJMenu)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void showSystemMenu()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void addSubComponents()
/*     */   {
/* 177 */     add(this.iconButton);
/* 178 */     add(this.maxButton);
/* 179 */     add(this.closeButton);
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/* 183 */     return new MetalPropertyChangeHandler();
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayout() {
/* 187 */     return new MetalTitlePaneLayout();
/*     */   }
/*     */ 
/*     */   public void paintPalette(Graphics paramGraphics)
/*     */   {
/* 311 */     boolean bool = MetalUtils.isLeftToRight(this.frame);
/*     */ 
/* 313 */     int i = getWidth();
/* 314 */     int j = getHeight();
/*     */ 
/* 316 */     if (this.paletteBumps == null) {
/* 317 */       this.paletteBumps = new MetalBumps(0, 0, MetalLookAndFeel.getPrimaryControlHighlight(), MetalLookAndFeel.getPrimaryControlInfo(), MetalLookAndFeel.getPrimaryControlShadow());
/*     */     }
/*     */ 
/* 324 */     ColorUIResource localColorUIResource1 = MetalLookAndFeel.getPrimaryControlShadow();
/* 325 */     ColorUIResource localColorUIResource2 = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*     */ 
/* 327 */     paramGraphics.setColor(localColorUIResource1);
/* 328 */     paramGraphics.fillRect(0, 0, i, j);
/*     */ 
/* 330 */     paramGraphics.setColor(localColorUIResource2);
/* 331 */     paramGraphics.drawLine(0, j - 1, i, j - 1);
/*     */ 
/* 333 */     int k = bool ? 4 : this.buttonsWidth + 4;
/* 334 */     int m = i - this.buttonsWidth - 8;
/* 335 */     int n = getHeight() - 4;
/* 336 */     this.paletteBumps.setBumpArea(m, n);
/* 337 */     this.paletteBumps.paintIcon(this, paramGraphics, k, 2);
/*     */   }
/*     */ 
/*     */   public void paintComponent(Graphics paramGraphics) {
/* 341 */     if (this.isPalette) {
/* 342 */       paintPalette(paramGraphics);
/* 343 */       return;
/*     */     }
/*     */ 
/* 346 */     boolean bool1 = MetalUtils.isLeftToRight(this.frame);
/* 347 */     boolean bool2 = this.frame.isSelected();
/*     */ 
/* 349 */     int i = getWidth();
/* 350 */     int j = getHeight();
/*     */ 
/* 352 */     Object localObject1 = null;
/* 353 */     Object localObject2 = null;
/* 354 */     Object localObject3 = null;
/*     */     MetalBumps localMetalBumps;
/*     */     String str1;
/* 359 */     if (bool2) {
/* 360 */       if (!MetalLookAndFeel.usingOcean()) {
/* 361 */         this.closeButton.setContentAreaFilled(true);
/* 362 */         this.maxButton.setContentAreaFilled(true);
/* 363 */         this.iconButton.setContentAreaFilled(true);
/*     */       }
/* 365 */       if (this.selectedBackgroundKey != null) {
/* 366 */         localObject1 = UIManager.getColor(this.selectedBackgroundKey);
/*     */       }
/* 368 */       if (localObject1 == null) {
/* 369 */         localObject1 = MetalLookAndFeel.getWindowTitleBackground();
/*     */       }
/* 371 */       if (this.selectedForegroundKey != null) {
/* 372 */         localObject2 = UIManager.getColor(this.selectedForegroundKey);
/*     */       }
/* 374 */       if (this.selectedShadowKey != null) {
/* 375 */         localObject3 = UIManager.getColor(this.selectedShadowKey);
/*     */       }
/* 377 */       if (localObject3 == null) {
/* 378 */         localObject3 = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*     */       }
/* 380 */       if (localObject2 == null) {
/* 381 */         localObject2 = MetalLookAndFeel.getWindowTitleForeground();
/*     */       }
/* 383 */       this.activeBumps.setBumpColors(this.activeBumpsHighlight, this.activeBumpsShadow, UIManager.get("InternalFrame.activeTitleGradient") != null ? null : (Color)localObject1);
/*     */ 
/* 386 */       localMetalBumps = this.activeBumps;
/* 387 */       str1 = "InternalFrame.activeTitleGradient";
/*     */     } else {
/* 389 */       if (!MetalLookAndFeel.usingOcean()) {
/* 390 */         this.closeButton.setContentAreaFilled(false);
/* 391 */         this.maxButton.setContentAreaFilled(false);
/* 392 */         this.iconButton.setContentAreaFilled(false);
/*     */       }
/* 394 */       localObject1 = MetalLookAndFeel.getWindowTitleInactiveBackground();
/* 395 */       localObject2 = MetalLookAndFeel.getWindowTitleInactiveForeground();
/* 396 */       localObject3 = MetalLookAndFeel.getControlDarkShadow();
/* 397 */       localMetalBumps = this.inactiveBumps;
/* 398 */       str1 = "InternalFrame.inactiveTitleGradient";
/*     */     }
/*     */ 
/* 401 */     if (!MetalUtils.drawGradient(this, paramGraphics, str1, 0, 0, i, j, true))
/*     */     {
/* 403 */       paramGraphics.setColor((Color)localObject1);
/* 404 */       paramGraphics.fillRect(0, 0, i, j);
/*     */     }
/*     */ 
/* 407 */     paramGraphics.setColor((Color)localObject3);
/* 408 */     paramGraphics.drawLine(0, j - 1, i, j - 1);
/* 409 */     paramGraphics.drawLine(0, 0, 0, 0);
/* 410 */     paramGraphics.drawLine(i - 1, 0, i - 1, 0);
/*     */ 
/* 414 */     Font localFont1 = bool1 ? 5 : i - 5;
/* 415 */     String str2 = this.frame.getTitle();
/*     */ 
/* 417 */     Icon localIcon = this.frame.getFrameIcon();
/* 418 */     if (localIcon != null) {
/* 419 */       if (!bool1)
/* 420 */         localFont1 -= localIcon.getIconWidth();
/* 421 */       int m = j / 2 - localIcon.getIconHeight() / 2;
/* 422 */       localIcon.paintIcon(this.frame, paramGraphics, localFont1, m);
/* 423 */       localFont1 += (bool1 ? localIcon.getIconWidth() + 5 : -5);
/*     */     }
/*     */     Font localFont2;
/* 426 */     if (str2 != null) {
/* 427 */       localFont2 = getFont();
/* 428 */       paramGraphics.setFont(localFont2);
/* 429 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(this.frame, paramGraphics, localFont2);
/* 430 */       i2 = localFontMetrics.getHeight();
/*     */ 
/* 432 */       paramGraphics.setColor((Color)localObject2);
/*     */ 
/* 434 */       i3 = (j - localFontMetrics.getHeight()) / 2 + localFontMetrics.getAscent();
/*     */ 
/* 436 */       Rectangle localRectangle = new Rectangle(0, 0, 0, 0);
/* 437 */       if (this.frame.isIconifiable()) localRectangle = this.iconButton.getBounds();
/* 438 */       else if (this.frame.isMaximizable()) localRectangle = this.maxButton.getBounds();
/* 439 */       else if (this.frame.isClosable()) localRectangle = this.closeButton.getBounds();
/*     */       int i4;
/* 442 */       if (bool1) {
/* 443 */         if (localRectangle.x == 0) {
/* 444 */           localRectangle.x = (this.frame.getWidth() - this.frame.getInsets().right - 2);
/*     */         }
/* 446 */         i4 = localRectangle.x - localFont1 - 4;
/* 447 */         str2 = getTitle(str2, localFontMetrics, i4);
/*     */       } else {
/* 449 */         i4 = localFont1 - localRectangle.x - localRectangle.width - 4;
/* 450 */         str2 = getTitle(str2, localFontMetrics, i4);
/* 451 */         localFont1 -= SwingUtilities2.stringWidth(this.frame, localFontMetrics, str2);
/*     */       }
/*     */ 
/* 454 */       int k = SwingUtilities2.stringWidth(this.frame, localFontMetrics, str2);
/* 455 */       SwingUtilities2.drawString(this.frame, paramGraphics, str2, localFont1, i3);
/* 456 */       localFont1 += (bool1 ? k + 5 : -5);
/*     */     }
/*     */     int i1;
/*     */     int n;
/* 461 */     if (bool1) {
/* 462 */       i1 = i - this.buttonsWidth - localFont1 - 5;
/* 463 */       localFont2 = localFont1;
/*     */     } else {
/* 465 */       i1 = localFont1 - this.buttonsWidth - 5;
/* 466 */       n = this.buttonsWidth + 5;
/*     */     }
/* 468 */     int i2 = 3;
/* 469 */     int i3 = getHeight() - 2 * i2;
/* 470 */     localMetalBumps.setBumpArea(i1, i3);
/* 471 */     localMetalBumps.paintIcon(this, paramGraphics, n, i2);
/*     */   }
/*     */ 
/*     */   public void setPalette(boolean paramBoolean) {
/* 475 */     this.isPalette = paramBoolean;
/*     */ 
/* 477 */     if (this.isPalette) {
/* 478 */       this.closeButton.setIcon(this.paletteCloseIcon);
/* 479 */       if (this.frame.isMaximizable())
/* 480 */         remove(this.maxButton);
/* 481 */       if (this.frame.isIconifiable())
/* 482 */         remove(this.iconButton);
/*     */     } else {
/* 484 */       this.closeButton.setIcon(this.closeIcon);
/* 485 */       if (this.frame.isMaximizable())
/* 486 */         add(this.maxButton);
/* 487 */       if (this.frame.isIconifiable())
/* 488 */         add(this.iconButton);
/*     */     }
/* 490 */     revalidate();
/* 491 */     repaint();
/*     */   }
/*     */ 
/*     */   private void updateOptionPaneState()
/*     */   {
/* 499 */     int i = -2;
/* 500 */     boolean bool = this.wasClosable;
/* 501 */     Object localObject = this.frame.getClientProperty("JInternalFrame.messageType");
/*     */ 
/* 503 */     if (localObject == null)
/*     */     {
/* 505 */       return;
/*     */     }
/* 507 */     if ((localObject instanceof Integer)) {
/* 508 */       i = ((Integer)localObject).intValue();
/*     */     }
/* 510 */     switch (i) {
/*     */     case 0:
/* 512 */       this.selectedBackgroundKey = "OptionPane.errorDialog.titlePane.background";
/*     */ 
/* 514 */       this.selectedForegroundKey = "OptionPane.errorDialog.titlePane.foreground";
/*     */ 
/* 516 */       this.selectedShadowKey = "OptionPane.errorDialog.titlePane.shadow";
/* 517 */       bool = false;
/* 518 */       break;
/*     */     case 3:
/* 520 */       this.selectedBackgroundKey = "OptionPane.questionDialog.titlePane.background";
/*     */ 
/* 522 */       this.selectedForegroundKey = "OptionPane.questionDialog.titlePane.foreground";
/*     */ 
/* 524 */       this.selectedShadowKey = "OptionPane.questionDialog.titlePane.shadow";
/*     */ 
/* 526 */       bool = false;
/* 527 */       break;
/*     */     case 2:
/* 529 */       this.selectedBackgroundKey = "OptionPane.warningDialog.titlePane.background";
/*     */ 
/* 531 */       this.selectedForegroundKey = "OptionPane.warningDialog.titlePane.foreground";
/*     */ 
/* 533 */       this.selectedShadowKey = "OptionPane.warningDialog.titlePane.shadow";
/* 534 */       bool = false;
/* 535 */       break;
/*     */     case -1:
/*     */     case 1:
/* 538 */       this.selectedBackgroundKey = (this.selectedForegroundKey = this.selectedShadowKey = null);
/*     */ 
/* 540 */       bool = false;
/* 541 */       break;
/*     */     default:
/* 543 */       this.selectedBackgroundKey = (this.selectedForegroundKey = this.selectedShadowKey = null);
/*     */     }
/*     */ 
/* 547 */     if (bool != this.frame.isClosable())
/* 548 */       this.frame.setClosable(bool);
/*     */   }
/*     */ 
/*     */   class MetalPropertyChangeHandler extends BasicInternalFrameTitlePane.PropertyChangeHandler
/*     */   {
/*     */     MetalPropertyChangeHandler()
/*     */     {
/* 190 */       super();
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 194 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 195 */       if (str.equals("selected")) {
/* 196 */         Boolean localBoolean = (Boolean)paramPropertyChangeEvent.getNewValue();
/* 197 */         MetalInternalFrameTitlePane.this.iconButton.putClientProperty("paintActive", localBoolean);
/* 198 */         MetalInternalFrameTitlePane.this.closeButton.putClientProperty("paintActive", localBoolean);
/* 199 */         MetalInternalFrameTitlePane.this.maxButton.putClientProperty("paintActive", localBoolean);
/*     */       }
/* 201 */       else if ("JInternalFrame.messageType".equals(str)) {
/* 202 */         MetalInternalFrameTitlePane.this.updateOptionPaneState();
/* 203 */         MetalInternalFrameTitlePane.this.frame.repaint();
/*     */       }
/* 205 */       super.propertyChange(paramPropertyChangeEvent);
/*     */     }
/*     */   }
/*     */   class MetalTitlePaneLayout extends BasicInternalFrameTitlePane.TitlePaneLayout {
/* 209 */     MetalTitlePaneLayout() { super(); } 
/*     */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*     */     }
/*     */     public void removeLayoutComponent(Component paramComponent) {  } 
/* 213 */     public Dimension preferredLayoutSize(Container paramContainer) { return minimumLayoutSize(paramContainer); }
/*     */ 
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container paramContainer)
/*     */     {
/* 218 */       int i = 30;
/* 219 */       if (MetalInternalFrameTitlePane.this.frame.isClosable()) {
/* 220 */         i += 21;
/*     */       }
/* 222 */       if (MetalInternalFrameTitlePane.this.frame.isMaximizable()) {
/* 223 */         i += 16 + (MetalInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4);
/*     */       }
/* 225 */       if (MetalInternalFrameTitlePane.this.frame.isIconifiable()) {
/* 226 */         i += 16 + (MetalInternalFrameTitlePane.this.frame.isClosable() ? 10 : MetalInternalFrameTitlePane.this.frame.isMaximizable() ? 2 : 4);
/*     */       }
/*     */ 
/* 229 */       FontMetrics localFontMetrics = MetalInternalFrameTitlePane.this.frame.getFontMetrics(MetalInternalFrameTitlePane.this.getFont());
/* 230 */       String str = MetalInternalFrameTitlePane.this.frame.getTitle();
/* 231 */       int j = str != null ? SwingUtilities2.stringWidth(MetalInternalFrameTitlePane.this.frame, localFontMetrics, str) : 0;
/*     */ 
/* 233 */       int k = str != null ? str.length() : 0;
/*     */       int m;
/* 235 */       if (k > 2) {
/* 236 */         m = SwingUtilities2.stringWidth(MetalInternalFrameTitlePane.this.frame, localFontMetrics, MetalInternalFrameTitlePane.this.frame.getTitle().substring(0, 2) + "...");
/*     */ 
/* 238 */         i += (j < m ? j : m);
/*     */       }
/*     */       else {
/* 241 */         i += j;
/*     */       }
/*     */ 
/* 246 */       if (MetalInternalFrameTitlePane.this.isPalette) {
/* 247 */         m = MetalInternalFrameTitlePane.this.paletteTitleHeight;
/*     */       } else {
/* 249 */         int n = localFontMetrics.getHeight();
/* 250 */         n += 7;
/* 251 */         Icon localIcon = MetalInternalFrameTitlePane.this.frame.getFrameIcon();
/* 252 */         int i1 = 0;
/* 253 */         if (localIcon != null)
/*     */         {
/* 255 */           i1 = Math.min(localIcon.getIconHeight(), 16);
/*     */         }
/* 257 */         i1 += 5;
/* 258 */         m = Math.max(n, i1);
/*     */       }
/*     */ 
/* 261 */       return new Dimension(i, m);
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer) {
/* 265 */       boolean bool = MetalUtils.isLeftToRight(MetalInternalFrameTitlePane.this.frame);
/*     */ 
/* 267 */       int i = MetalInternalFrameTitlePane.this.getWidth();
/* 268 */       int j = bool ? i : 0;
/* 269 */       int k = 2;
/*     */ 
/* 274 */       int n = MetalInternalFrameTitlePane.this.closeButton.getIcon().getIconHeight();
/* 275 */       int i1 = MetalInternalFrameTitlePane.this.closeButton.getIcon().getIconWidth();
/*     */       int m;
/* 277 */       if (MetalInternalFrameTitlePane.this.frame.isClosable()) {
/* 278 */         if (MetalInternalFrameTitlePane.this.isPalette) {
/* 279 */           m = 3;
/* 280 */           j += (bool ? -m - (i1 + 2) : m);
/* 281 */           MetalInternalFrameTitlePane.this.closeButton.setBounds(j, k, i1 + 2, MetalInternalFrameTitlePane.this.getHeight() - 4);
/* 282 */           if (!bool) j += i1 + 2; 
/*     */         }
/* 284 */         else { m = 4;
/* 285 */           j += (bool ? -m - i1 : m);
/* 286 */           MetalInternalFrameTitlePane.this.closeButton.setBounds(j, k, i1, n);
/* 287 */           if (!bool) j += i1;
/*     */         }
/*     */       }
/*     */ 
/* 291 */       if ((MetalInternalFrameTitlePane.this.frame.isMaximizable()) && (!MetalInternalFrameTitlePane.this.isPalette)) {
/* 292 */         m = MetalInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4;
/* 293 */         j += (bool ? -m - i1 : m);
/* 294 */         MetalInternalFrameTitlePane.this.maxButton.setBounds(j, k, i1, n);
/* 295 */         if (!bool) j += i1;
/*     */       }
/*     */ 
/* 298 */       if ((MetalInternalFrameTitlePane.this.frame.isIconifiable()) && (!MetalInternalFrameTitlePane.this.isPalette)) {
/* 299 */         m = MetalInternalFrameTitlePane.this.frame.isClosable() ? 10 : MetalInternalFrameTitlePane.this.frame.isMaximizable() ? 2 : 4;
/*     */ 
/* 301 */         j += (bool ? -m - i1 : m);
/* 302 */         MetalInternalFrameTitlePane.this.iconButton.setBounds(j, k, i1, n);
/* 303 */         if (!bool) j += i1;
/*     */       }
/*     */ 
/* 306 */       MetalInternalFrameTitlePane.this.buttonsWidth = (bool ? i - j : j);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalInternalFrameTitlePane
 * JD-Core Version:    0.6.2
 */
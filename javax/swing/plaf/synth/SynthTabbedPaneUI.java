/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTabbedPaneUI;
/*     */ import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;
/*     */ import javax.swing.text.View;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class SynthTabbedPaneUI extends BasicTabbedPaneUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private int tabOverlap;
/*     */   private boolean extendTabsToBase;
/*     */   private SynthContext tabAreaContext;
/*     */   private SynthContext tabContext;
/*     */   private SynthContext tabContentContext;
/*     */   private SynthStyle style;
/*     */   private SynthStyle tabStyle;
/*     */   private SynthStyle tabAreaStyle;
/*     */   private SynthStyle tabContentStyle;
/*     */   private Rectangle textRect;
/*     */   private Rectangle iconRect;
/*     */   private Rectangle tabAreaBounds;
/*     */   private boolean tabAreaStatesMatchSelectedTab;
/*     */   private boolean nudgeSelectedLabel;
/*     */   private boolean selectedTabIsPressed;
/*     */ 
/*     */   public SynthTabbedPaneUI()
/*     */   {
/*  73 */     this.tabOverlap = 0;
/*     */ 
/*  84 */     this.extendTabsToBase = false;
/*     */ 
/*  95 */     this.textRect = new Rectangle();
/*  96 */     this.iconRect = new Rectangle();
/*     */ 
/*  98 */     this.tabAreaBounds = new Rectangle();
/*     */ 
/* 102 */     this.tabAreaStatesMatchSelectedTab = false;
/*     */ 
/* 104 */     this.nudgeSelectedLabel = true;
/*     */ 
/* 106 */     this.selectedTabIsPressed = false;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 115 */     return new SynthTabbedPaneUI();
/*     */   }
/*     */ 
/*     */   private boolean scrollableTabLayoutEnabled() {
/* 119 */     return this.tabPane.getTabLayoutPolicy() == 1;
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 127 */     updateStyle(this.tabPane);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JTabbedPane paramJTabbedPane) {
/* 131 */     SynthContext localSynthContext = getContext(paramJTabbedPane, 1);
/* 132 */     SynthStyle localSynthStyle = this.style;
/* 133 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*     */ 
/* 136 */     if (this.style != localSynthStyle) {
/* 137 */       this.tabRunOverlay = this.style.getInt(localSynthContext, "TabbedPane.tabRunOverlay", 0);
/*     */ 
/* 139 */       this.tabOverlap = this.style.getInt(localSynthContext, "TabbedPane.tabOverlap", 0);
/* 140 */       this.extendTabsToBase = this.style.getBoolean(localSynthContext, "TabbedPane.extendTabsToBase", false);
/*     */ 
/* 142 */       this.textIconGap = this.style.getInt(localSynthContext, "TabbedPane.textIconGap", 0);
/* 143 */       this.selectedTabPadInsets = ((Insets)this.style.get(localSynthContext, "TabbedPane.selectedTabPadInsets"));
/*     */ 
/* 145 */       if (this.selectedTabPadInsets == null) {
/* 146 */         this.selectedTabPadInsets = new Insets(0, 0, 0, 0);
/*     */       }
/* 148 */       this.tabAreaStatesMatchSelectedTab = this.style.getBoolean(localSynthContext, "TabbedPane.tabAreaStatesMatchSelectedTab", false);
/*     */ 
/* 150 */       this.nudgeSelectedLabel = this.style.getBoolean(localSynthContext, "TabbedPane.nudgeSelectedLabel", true);
/*     */ 
/* 152 */       if (localSynthStyle != null) {
/* 153 */         uninstallKeyboardActions();
/* 154 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 157 */     localSynthContext.dispose();
/*     */ 
/* 159 */     if (this.tabContext != null) {
/* 160 */       this.tabContext.dispose();
/*     */     }
/* 162 */     this.tabContext = getContext(paramJTabbedPane, Region.TABBED_PANE_TAB, 1);
/* 163 */     this.tabStyle = SynthLookAndFeel.updateStyle(this.tabContext, this);
/* 164 */     this.tabInsets = this.tabStyle.getInsets(this.tabContext, null);
/*     */ 
/* 167 */     if (this.tabAreaContext != null) {
/* 168 */       this.tabAreaContext.dispose();
/*     */     }
/* 170 */     this.tabAreaContext = getContext(paramJTabbedPane, Region.TABBED_PANE_TAB_AREA, 1);
/* 171 */     this.tabAreaStyle = SynthLookAndFeel.updateStyle(this.tabAreaContext, this);
/* 172 */     this.tabAreaInsets = this.tabAreaStyle.getInsets(this.tabAreaContext, null);
/*     */ 
/* 175 */     if (this.tabContentContext != null) {
/* 176 */       this.tabContentContext.dispose();
/*     */     }
/* 178 */     this.tabContentContext = getContext(paramJTabbedPane, Region.TABBED_PANE_CONTENT, 1);
/* 179 */     this.tabContentStyle = SynthLookAndFeel.updateStyle(this.tabContentContext, this);
/*     */ 
/* 181 */     this.contentBorderInsets = this.tabContentStyle.getInsets(this.tabContentContext, null);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 190 */     super.installListeners();
/* 191 */     this.tabPane.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 199 */     super.uninstallListeners();
/* 200 */     this.tabPane.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 208 */     SynthContext localSynthContext = getContext(this.tabPane, 1);
/* 209 */     this.style.uninstallDefaults(localSynthContext);
/* 210 */     localSynthContext.dispose();
/* 211 */     this.style = null;
/*     */ 
/* 213 */     this.tabStyle.uninstallDefaults(this.tabContext);
/* 214 */     this.tabContext.dispose();
/* 215 */     this.tabContext = null;
/* 216 */     this.tabStyle = null;
/*     */ 
/* 218 */     this.tabAreaStyle.uninstallDefaults(this.tabAreaContext);
/* 219 */     this.tabAreaContext.dispose();
/* 220 */     this.tabAreaContext = null;
/* 221 */     this.tabAreaStyle = null;
/*     */ 
/* 223 */     this.tabContentStyle.uninstallDefaults(this.tabContentContext);
/* 224 */     this.tabContentContext.dispose();
/* 225 */     this.tabContentContext = null;
/* 226 */     this.tabContentStyle = null;
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 234 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 238 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, int paramInt)
/*     */   {
/* 243 */     SynthStyle localSynthStyle = null;
/* 244 */     SynthContext localSynthContext = SynthContext.class;
/*     */ 
/* 246 */     if (paramRegion == Region.TABBED_PANE_TAB) {
/* 247 */       localSynthStyle = this.tabStyle;
/*     */     }
/* 249 */     else if (paramRegion == Region.TABBED_PANE_TAB_AREA) {
/* 250 */       localSynthStyle = this.tabAreaStyle;
/*     */     }
/* 252 */     else if (paramRegion == Region.TABBED_PANE_CONTENT) {
/* 253 */       localSynthStyle = this.tabContentStyle;
/*     */     }
/* 255 */     return SynthContext.getContext(localSynthContext, paramJComponent, paramRegion, localSynthStyle, paramInt);
/*     */   }
/*     */ 
/*     */   protected JButton createScrollButton(int paramInt)
/*     */   {
/* 266 */     if (UIManager.getBoolean("TabbedPane.useBasicArrows")) {
/* 267 */       JButton localJButton = super.createScrollButton(paramInt);
/* 268 */       localJButton.setBorder(BorderFactory.createEmptyBorder());
/* 269 */       return localJButton;
/*     */     }
/* 271 */     return new SynthScrollableTabButton(paramInt);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 279 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 280 */       updateStyle(this.tabPane);
/*     */   }
/*     */ 
/*     */   protected MouseListener createMouseListener()
/*     */   {
/* 291 */     final MouseListener localMouseListener = super.createMouseListener();
/* 292 */     final MouseMotionListener localMouseMotionListener = (MouseMotionListener)localMouseListener;
/* 293 */     return new MouseListener() {
/* 294 */       public void mouseClicked(MouseEvent paramAnonymousMouseEvent) { localMouseListener.mouseClicked(paramAnonymousMouseEvent); } 
/* 295 */       public void mouseEntered(MouseEvent paramAnonymousMouseEvent) { localMouseListener.mouseEntered(paramAnonymousMouseEvent); } 
/* 296 */       public void mouseExited(MouseEvent paramAnonymousMouseEvent) { localMouseListener.mouseExited(paramAnonymousMouseEvent); }
/*     */ 
/*     */       public void mousePressed(MouseEvent paramAnonymousMouseEvent) {
/* 299 */         if (!SynthTabbedPaneUI.this.tabPane.isEnabled()) {
/* 300 */           return;
/*     */         }
/*     */ 
/* 303 */         int i = SynthTabbedPaneUI.this.tabForCoordinate(SynthTabbedPaneUI.this.tabPane, paramAnonymousMouseEvent.getX(), paramAnonymousMouseEvent.getY());
/* 304 */         if ((i >= 0) && (SynthTabbedPaneUI.this.tabPane.isEnabledAt(i)) && 
/* 305 */           (i == SynthTabbedPaneUI.this.tabPane.getSelectedIndex()))
/*     */         {
/* 307 */           SynthTabbedPaneUI.this.selectedTabIsPressed = true;
/*     */ 
/* 309 */           SynthTabbedPaneUI.this.tabPane.repaint();
/*     */         }
/*     */ 
/* 314 */         localMouseListener.mousePressed(paramAnonymousMouseEvent);
/*     */       }
/*     */ 
/*     */       public void mouseReleased(MouseEvent paramAnonymousMouseEvent) {
/* 318 */         if (SynthTabbedPaneUI.this.selectedTabIsPressed) {
/* 319 */           SynthTabbedPaneUI.this.selectedTabIsPressed = false;
/*     */ 
/* 321 */           SynthTabbedPaneUI.this.tabPane.repaint();
/*     */         }
/*     */ 
/* 324 */         localMouseListener.mouseReleased(paramAnonymousMouseEvent);
/*     */ 
/* 330 */         localMouseMotionListener.mouseMoved(paramAnonymousMouseEvent);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected int getTabLabelShiftX(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 340 */     if (this.nudgeSelectedLabel) {
/* 341 */       return super.getTabLabelShiftX(paramInt1, paramInt2, paramBoolean);
/*     */     }
/* 343 */     return 0;
/*     */   }
/*     */ 
/*     */   protected int getTabLabelShiftY(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 352 */     if (this.nudgeSelectedLabel) {
/* 353 */       return super.getTabLabelShiftY(paramInt1, paramInt2, paramBoolean);
/*     */     }
/* 355 */     return 0;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 373 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 375 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 376 */     localSynthContext.getPainter().paintTabbedPaneBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 378 */     paint(localSynthContext, paramGraphics);
/* 379 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected int getBaseline(int paramInt)
/*     */   {
/* 387 */     if ((this.tabPane.getTabComponentAt(paramInt) != null) || (getTextViewForTab(paramInt) != null))
/*     */     {
/* 389 */       return super.getBaseline(paramInt);
/*     */     }
/* 391 */     String str = this.tabPane.getTitleAt(paramInt);
/* 392 */     Font localFont = this.tabContext.getStyle().getFont(this.tabContext);
/* 393 */     FontMetrics localFontMetrics = getFontMetrics(localFont);
/* 394 */     Icon localIcon = getIconForTab(paramInt);
/* 395 */     this.textRect.setBounds(0, 0, 0, 0);
/* 396 */     this.iconRect.setBounds(0, 0, 0, 0);
/* 397 */     this.calcRect.setBounds(0, 0, 32767, this.maxTabHeight);
/* 398 */     this.tabContext.getStyle().getGraphicsUtils(this.tabContext).layoutText(this.tabContext, localFontMetrics, str, localIcon, 0, 0, 10, 0, this.calcRect, this.iconRect, this.textRect, this.textIconGap);
/*     */ 
/* 403 */     return this.textRect.y + localFontMetrics.getAscent() + getBaselineOffset();
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 412 */     paramSynthContext.getPainter().paintTabbedPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 426 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 428 */     paint(localSynthContext, paramGraphics);
/* 429 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 440 */     int i = this.tabPane.getSelectedIndex();
/* 441 */     int j = this.tabPane.getTabPlacement();
/*     */ 
/* 443 */     ensureCurrentLayout();
/*     */ 
/* 449 */     if (!scrollableTabLayoutEnabled()) {
/* 450 */       Insets localInsets = this.tabPane.getInsets();
/* 451 */       int k = localInsets.left;
/* 452 */       int m = localInsets.top;
/* 453 */       int n = this.tabPane.getWidth() - localInsets.left - localInsets.right;
/* 454 */       int i1 = this.tabPane.getHeight() - localInsets.top - localInsets.bottom;
/*     */       int i2;
/* 456 */       switch (j) {
/*     */       case 2:
/* 458 */         n = calculateTabAreaWidth(j, this.runCount, this.maxTabWidth);
/*     */ 
/* 460 */         break;
/*     */       case 4:
/* 462 */         i2 = calculateTabAreaWidth(j, this.runCount, this.maxTabWidth);
/*     */ 
/* 464 */         k = k + n - i2;
/* 465 */         n = i2;
/* 466 */         break;
/*     */       case 3:
/* 468 */         i2 = calculateTabAreaHeight(j, this.runCount, this.maxTabHeight);
/*     */ 
/* 470 */         m = m + i1 - i2;
/* 471 */         i1 = i2;
/* 472 */         break;
/*     */       case 1:
/*     */       default:
/* 475 */         i1 = calculateTabAreaHeight(j, this.runCount, this.maxTabHeight);
/*     */       }
/*     */ 
/* 479 */       this.tabAreaBounds.setBounds(k, m, n, i1);
/*     */ 
/* 481 */       if (paramGraphics.getClipBounds().intersects(this.tabAreaBounds)) {
/* 482 */         paintTabArea(this.tabAreaContext, paramGraphics, j, i, this.tabAreaBounds);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 488 */     paintContentBorder(this.tabContentContext, paramGraphics, j, i);
/*     */   }
/*     */ 
/*     */   protected void paintTabArea(Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/* 494 */     Insets localInsets = this.tabPane.getInsets();
/* 495 */     int i = localInsets.left;
/* 496 */     int j = localInsets.top;
/* 497 */     int k = this.tabPane.getWidth() - localInsets.left - localInsets.right;
/* 498 */     int m = this.tabPane.getHeight() - localInsets.top - localInsets.bottom;
/*     */ 
/* 500 */     paintTabArea(this.tabAreaContext, paramGraphics, paramInt1, paramInt2, new Rectangle(i, j, k, m));
/*     */   }
/*     */ 
/*     */   private void paintTabArea(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, Rectangle paramRectangle)
/*     */   {
/* 507 */     Rectangle localRectangle = paramGraphics.getClipBounds();
/*     */ 
/* 514 */     if ((this.tabAreaStatesMatchSelectedTab) && (paramInt2 >= 0)) {
/* 515 */       updateTabContext(paramInt2, true, this.selectedTabIsPressed, getRolloverTab() == paramInt2, getFocusIndex() == paramInt2);
/*     */ 
/* 518 */       paramSynthContext.setComponentState(this.tabContext.getComponentState());
/*     */     } else {
/* 520 */       paramSynthContext.setComponentState(1);
/*     */     }
/*     */ 
/* 524 */     SynthLookAndFeel.updateSubregion(paramSynthContext, paramGraphics, paramRectangle);
/* 525 */     paramSynthContext.getPainter().paintTabbedPaneTabAreaBackground(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, paramInt1);
/*     */ 
/* 528 */     paramSynthContext.getPainter().paintTabbedPaneTabAreaBorder(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, paramInt1);
/*     */ 
/* 532 */     int i = this.tabPane.getTabCount();
/*     */ 
/* 534 */     this.iconRect.setBounds(0, 0, 0, 0);
/* 535 */     this.textRect.setBounds(0, 0, 0, 0);
/*     */ 
/* 538 */     for (int j = this.runCount - 1; j >= 0; j--) {
/* 539 */       int k = this.tabRuns[j];
/* 540 */       int m = this.tabRuns[(j + 1)];
/* 541 */       int n = m != 0 ? m - 1 : i - 1;
/* 542 */       for (int i1 = k; i1 <= n; i1++) {
/* 543 */         if ((this.rects[i1].intersects(localRectangle)) && (paramInt2 != i1)) {
/* 544 */           paintTab(this.tabContext, paramGraphics, paramInt1, this.rects, i1, this.iconRect, this.textRect);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 550 */     if ((paramInt2 >= 0) && 
/* 551 */       (this.rects[paramInt2].intersects(localRectangle)))
/* 552 */       paintTab(this.tabContext, paramGraphics, paramInt1, this.rects, paramInt2, this.iconRect, this.textRect);
/*     */   }
/*     */ 
/*     */   protected void setRolloverTab(int paramInt)
/*     */   {
/* 563 */     int i = getRolloverTab();
/* 564 */     super.setRolloverTab(paramInt);
/*     */ 
/* 566 */     Rectangle localRectangle = null;
/*     */ 
/* 568 */     if ((i != paramInt) && (this.tabAreaStatesMatchSelectedTab))
/*     */     {
/* 570 */       this.tabPane.repaint();
/*     */     } else {
/* 572 */       if ((i >= 0) && (i < this.tabPane.getTabCount())) {
/* 573 */         localRectangle = getTabBounds(this.tabPane, i);
/* 574 */         if (localRectangle != null) {
/* 575 */           this.tabPane.repaint(localRectangle);
/*     */         }
/*     */       }
/*     */ 
/* 579 */       if (paramInt >= 0) {
/* 580 */         localRectangle = getTabBounds(this.tabPane, paramInt);
/* 581 */         if (localRectangle != null)
/* 582 */           this.tabPane.repaint(localRectangle);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void paintTab(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, Rectangle[] paramArrayOfRectangle, int paramInt2, Rectangle paramRectangle1, Rectangle paramRectangle2)
/*     */   {
/* 591 */     Rectangle localRectangle = paramArrayOfRectangle[paramInt2];
/* 592 */     int i = this.tabPane.getSelectedIndex();
/* 593 */     boolean bool = i == paramInt2;
/* 594 */     updateTabContext(paramInt2, bool, (bool) && (this.selectedTabIsPressed), getRolloverTab() == paramInt2, getFocusIndex() == paramInt2);
/*     */ 
/* 598 */     SynthLookAndFeel.updateSubregion(paramSynthContext, paramGraphics, localRectangle);
/* 599 */     int j = localRectangle.x;
/* 600 */     int k = localRectangle.y;
/* 601 */     int m = localRectangle.height;
/* 602 */     int n = localRectangle.width;
/* 603 */     int i1 = this.tabPane.getTabPlacement();
/*     */     Object localObject;
/* 604 */     if ((this.extendTabsToBase) && (this.runCount > 1))
/*     */     {
/* 611 */       if (i >= 0) {
/* 612 */         localObject = paramArrayOfRectangle[i];
/* 613 */         switch (i1) {
/*     */         case 1:
/* 615 */           int i2 = ((Rectangle)localObject).y + ((Rectangle)localObject).height;
/* 616 */           m = i2 - localRectangle.y;
/* 617 */           break;
/*     */         case 2:
/* 619 */           int i3 = ((Rectangle)localObject).x + ((Rectangle)localObject).width;
/* 620 */           n = i3 - localRectangle.x;
/* 621 */           break;
/*     */         case 3:
/* 623 */           int i4 = ((Rectangle)localObject).y;
/* 624 */           m = localRectangle.y + localRectangle.height - i4;
/* 625 */           k = i4;
/* 626 */           break;
/*     */         case 4:
/* 628 */           int i5 = ((Rectangle)localObject).x;
/* 629 */           n = localRectangle.x + localRectangle.width - i5;
/* 630 */           j = i5;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 635 */     this.tabContext.getPainter().paintTabbedPaneTabBackground(this.tabContext, paramGraphics, j, k, n, m, paramInt2, i1);
/*     */ 
/* 637 */     this.tabContext.getPainter().paintTabbedPaneTabBorder(this.tabContext, paramGraphics, j, k, n, m, paramInt2, i1);
/*     */ 
/* 640 */     if (this.tabPane.getTabComponentAt(paramInt2) == null) {
/* 641 */       localObject = this.tabPane.getTitleAt(paramInt2);
/* 642 */       Font localFont = paramSynthContext.getStyle().getFont(paramSynthContext);
/* 643 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(this.tabPane, paramGraphics, localFont);
/* 644 */       Icon localIcon = getIconForTab(paramInt2);
/*     */ 
/* 646 */       layoutLabel(paramSynthContext, paramInt1, localFontMetrics, paramInt2, (String)localObject, localIcon, localRectangle, paramRectangle1, paramRectangle2, bool);
/*     */ 
/* 649 */       paintText(paramSynthContext, paramGraphics, paramInt1, localFont, localFontMetrics, paramInt2, (String)localObject, paramRectangle2, bool);
/*     */ 
/* 652 */       paintIcon(paramGraphics, paramInt1, paramInt2, localIcon, paramRectangle1, bool);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void layoutLabel(SynthContext paramSynthContext, int paramInt1, FontMetrics paramFontMetrics, int paramInt2, String paramString, Icon paramIcon, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3, boolean paramBoolean)
/*     */   {
/* 661 */     View localView = getTextViewForTab(paramInt2);
/* 662 */     if (localView != null) {
/* 663 */       this.tabPane.putClientProperty("html", localView);
/*     */     }
/*     */ 
/* 666 */     paramRectangle3.x = (paramRectangle3.y = paramRectangle2.x = paramRectangle2.y = 0);
/*     */ 
/* 668 */     paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext).layoutText(paramSynthContext, paramFontMetrics, paramString, paramIcon, 0, 0, 10, 0, paramRectangle1, paramRectangle2, paramRectangle3, this.textIconGap);
/*     */ 
/* 673 */     this.tabPane.putClientProperty("html", null);
/*     */ 
/* 675 */     int i = getTabLabelShiftX(paramInt1, paramInt2, paramBoolean);
/* 676 */     int j = getTabLabelShiftY(paramInt1, paramInt2, paramBoolean);
/* 677 */     paramRectangle2.x += i;
/* 678 */     paramRectangle2.y += j;
/* 679 */     paramRectangle3.x += i;
/* 680 */     paramRectangle3.y += j;
/*     */   }
/*     */ 
/*     */   private void paintText(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, Font paramFont, FontMetrics paramFontMetrics, int paramInt2, String paramString, Rectangle paramRectangle, boolean paramBoolean)
/*     */   {
/* 688 */     paramGraphics.setFont(paramFont);
/*     */ 
/* 690 */     View localView = getTextViewForTab(paramInt2);
/* 691 */     if (localView != null)
/*     */     {
/* 693 */       localView.paint(paramGraphics, paramRectangle);
/*     */     }
/*     */     else {
/* 696 */       int i = this.tabPane.getDisplayedMnemonicIndexAt(paramInt2);
/*     */ 
/* 698 */       paramGraphics.setColor(paramSynthContext.getStyle().getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/* 699 */       paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext).paintText(paramSynthContext, paramGraphics, paramString, paramRectangle, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void paintContentBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/* 707 */     int i = this.tabPane.getWidth();
/* 708 */     int j = this.tabPane.getHeight();
/* 709 */     Insets localInsets = this.tabPane.getInsets();
/*     */ 
/* 711 */     int k = localInsets.left;
/* 712 */     int m = localInsets.top;
/* 713 */     int n = i - localInsets.right - localInsets.left;
/* 714 */     int i1 = j - localInsets.top - localInsets.bottom;
/*     */ 
/* 716 */     switch (paramInt1) {
/*     */     case 2:
/* 718 */       k += calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth);
/* 719 */       n -= k - localInsets.left;
/* 720 */       break;
/*     */     case 4:
/* 722 */       n -= calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth);
/* 723 */       break;
/*     */     case 3:
/* 725 */       i1 -= calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight);
/* 726 */       break;
/*     */     case 1:
/*     */     default:
/* 729 */       m += calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight);
/* 730 */       i1 -= m - localInsets.top;
/*     */     }
/* 732 */     SynthLookAndFeel.updateSubregion(paramSynthContext, paramGraphics, new Rectangle(k, m, n, i1));
/* 733 */     paramSynthContext.getPainter().paintTabbedPaneContentBackground(paramSynthContext, paramGraphics, k, m, n, i1);
/*     */ 
/* 735 */     paramSynthContext.getPainter().paintTabbedPaneContentBorder(paramSynthContext, paramGraphics, k, m, n, i1);
/*     */   }
/*     */ 
/*     */   private void ensureCurrentLayout() {
/* 739 */     if (!this.tabPane.isValid()) {
/* 740 */       this.tabPane.validate();
/*     */     }
/*     */ 
/* 746 */     if (!this.tabPane.isValid()) {
/* 747 */       BasicTabbedPaneUI.TabbedPaneLayout localTabbedPaneLayout = (BasicTabbedPaneUI.TabbedPaneLayout)this.tabPane.getLayout();
/* 748 */       localTabbedPaneLayout.calculateLayoutInfo();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int calculateMaxTabHeight(int paramInt)
/*     */   {
/* 757 */     FontMetrics localFontMetrics = getFontMetrics(this.tabContext.getStyle().getFont(this.tabContext));
/*     */ 
/* 759 */     int i = this.tabPane.getTabCount();
/* 760 */     int j = 0;
/* 761 */     int k = localFontMetrics.getHeight();
/* 762 */     for (int m = 0; m < i; m++) {
/* 763 */       j = Math.max(calculateTabHeight(paramInt, m, k), j);
/*     */     }
/* 765 */     return j;
/*     */   }
/*     */ 
/*     */   protected int calculateTabWidth(int paramInt1, int paramInt2, FontMetrics paramFontMetrics)
/*     */   {
/* 774 */     Icon localIcon = getIconForTab(paramInt2);
/* 775 */     Insets localInsets = getTabInsets(paramInt1, paramInt2);
/* 776 */     int i = localInsets.left + localInsets.right;
/* 777 */     Component localComponent = this.tabPane.getTabComponentAt(paramInt2);
/* 778 */     if (localComponent != null) {
/* 779 */       i += localComponent.getPreferredSize().width;
/*     */     } else {
/* 781 */       if (localIcon != null) {
/* 782 */         i += localIcon.getIconWidth() + this.textIconGap;
/*     */       }
/* 784 */       View localView = getTextViewForTab(paramInt2);
/* 785 */       if (localView != null)
/*     */       {
/* 787 */         i += (int)localView.getPreferredSpan(0);
/*     */       }
/*     */       else {
/* 790 */         String str = this.tabPane.getTitleAt(paramInt2);
/* 791 */         i += this.tabContext.getStyle().getGraphicsUtils(this.tabContext).computeStringWidth(this.tabContext, paramFontMetrics.getFont(), paramFontMetrics, str);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 796 */     return i;
/*     */   }
/*     */ 
/*     */   protected int calculateMaxTabWidth(int paramInt)
/*     */   {
/* 804 */     FontMetrics localFontMetrics = getFontMetrics(this.tabContext.getStyle().getFont(this.tabContext));
/*     */ 
/* 806 */     int i = this.tabPane.getTabCount();
/* 807 */     int j = 0;
/* 808 */     for (int k = 0; k < i; k++) {
/* 809 */       j = Math.max(calculateTabWidth(paramInt, k, localFontMetrics), j);
/*     */     }
/*     */ 
/* 812 */     return j;
/*     */   }
/*     */ 
/*     */   protected Insets getTabInsets(int paramInt1, int paramInt2)
/*     */   {
/* 820 */     updateTabContext(paramInt2, false, false, false, getFocusIndex() == paramInt2);
/*     */ 
/* 822 */     return this.tabInsets;
/*     */   }
/*     */ 
/*     */   protected FontMetrics getFontMetrics()
/*     */   {
/* 830 */     return getFontMetrics(this.tabContext.getStyle().getFont(this.tabContext));
/*     */   }
/*     */ 
/*     */   private FontMetrics getFontMetrics(Font paramFont) {
/* 834 */     return this.tabPane.getFontMetrics(paramFont);
/*     */   }
/*     */ 
/*     */   private void updateTabContext(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
/*     */   {
/* 839 */     int i = 0;
/* 840 */     if ((!this.tabPane.isEnabled()) || (!this.tabPane.isEnabledAt(paramInt))) {
/* 841 */       i |= 8;
/* 842 */       if (paramBoolean1) {
/* 843 */         i |= 512;
/*     */       }
/*     */     }
/* 846 */     else if (paramBoolean1) {
/* 847 */       i |= 513;
/* 848 */       if ((paramBoolean3) && (UIManager.getBoolean("TabbedPane.isTabRollover"))) {
/* 849 */         i |= 2;
/*     */       }
/*     */     }
/* 852 */     else if (paramBoolean3) {
/* 853 */       i |= 3;
/*     */     }
/*     */     else {
/* 856 */       i = SynthLookAndFeel.getComponentState(this.tabPane);
/* 857 */       i &= -257;
/*     */     }
/* 859 */     if ((paramBoolean4) && (this.tabPane.hasFocus())) {
/* 860 */       i |= 256;
/*     */     }
/* 862 */     if (paramBoolean2) {
/* 863 */       i |= 4;
/*     */     }
/*     */ 
/* 866 */     this.tabContext.setComponentState(i);
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayoutManager()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 533	javax/swing/plaf/synth/SynthTabbedPaneUI:tabPane	Ljavax/swing/JTabbedPane;
/*     */     //   4: invokevirtual 561	javax/swing/JTabbedPane:getTabLayoutPolicy	()I
/*     */     //   7: iconst_1
/*     */     //   8: if_icmpne +8 -> 16
/*     */     //   11: aload_0
/*     */     //   12: invokespecial 588	javax/swing/plaf/basic/BasicTabbedPaneUI:createLayoutManager	()Ljava/awt/LayoutManager;
/*     */     //   15: areturn
/*     */     //   16: new 266	javax/swing/plaf/synth/SynthTabbedPaneUI$2
/*     */     //   19: dup
/*     */     //   20: aload_0
/*     */     //   21: invokespecial 655	javax/swing/plaf/synth/SynthTabbedPaneUI$2:<init>	(Ljavax/swing/plaf/synth/SynthTabbedPaneUI;)V
/*     */     //   24: areturn
/*     */   }
/*     */ 
/*     */   private class SynthScrollableTabButton extends SynthArrowButton
/*     */     implements UIResource
/*     */   {
/*     */     public SynthScrollableTabButton(int arg2)
/*     */     {
/* 928 */       super();
/* 929 */       setName("TabbedPane.button");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthTabbedPaneUI
 * JD-Core Version:    0.6.2
 */
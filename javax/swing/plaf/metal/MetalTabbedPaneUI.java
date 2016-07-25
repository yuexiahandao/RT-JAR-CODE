/*      */ package javax.swing.plaf.metal;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Rectangle;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JTabbedPane;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.basic.BasicTabbedPaneUI;
/*      */ import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;
/*      */ 
/*      */ public class MetalTabbedPaneUI extends BasicTabbedPaneUI
/*      */ {
/*      */   protected int minTabWidth;
/*      */   private Color unselectedBackground;
/*      */   protected Color tabAreaBackground;
/*      */   protected Color selectColor;
/*      */   protected Color selectHighlight;
/*      */   private boolean tabsOpaque;
/*      */   private boolean ocean;
/*      */   private Color oceanSelectedBorderColor;
/*      */ 
/*      */   public MetalTabbedPaneUI()
/*      */   {
/*   53 */     this.minTabWidth = 40;
/*      */ 
/*   60 */     this.tabsOpaque = true;
/*      */   }
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*   69 */     return new MetalTabbedPaneUI();
/*      */   }
/*      */ 
/*      */   protected LayoutManager createLayoutManager() {
/*   73 */     if (this.tabPane.getTabLayoutPolicy() == 1) {
/*   74 */       return super.createLayoutManager();
/*      */     }
/*   76 */     return new TabbedPaneLayout();
/*      */   }
/*      */ 
/*      */   protected void installDefaults() {
/*   80 */     super.installDefaults();
/*      */ 
/*   82 */     this.tabAreaBackground = UIManager.getColor("TabbedPane.tabAreaBackground");
/*   83 */     this.selectColor = UIManager.getColor("TabbedPane.selected");
/*   84 */     this.selectHighlight = UIManager.getColor("TabbedPane.selectHighlight");
/*   85 */     this.tabsOpaque = UIManager.getBoolean("TabbedPane.tabsOpaque");
/*   86 */     this.unselectedBackground = UIManager.getColor("TabbedPane.unselectedBackground");
/*      */ 
/*   88 */     this.ocean = MetalLookAndFeel.usingOcean();
/*   89 */     if (this.ocean)
/*   90 */       this.oceanSelectedBorderColor = UIManager.getColor("TabbedPane.borderHightlightColor");
/*      */   }
/*      */ 
/*      */   protected void paintTabBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*      */   {
/*   99 */     int i = paramInt4 + (paramInt6 - 1);
/*  100 */     int j = paramInt3 + (paramInt5 - 1);
/*      */ 
/*  102 */     switch (paramInt1) {
/*      */     case 2:
/*  104 */       paintLeftTabBorder(paramInt2, paramGraphics, paramInt3, paramInt4, paramInt5, paramInt6, i, j, paramBoolean);
/*  105 */       break;
/*      */     case 3:
/*  107 */       paintBottomTabBorder(paramInt2, paramGraphics, paramInt3, paramInt4, paramInt5, paramInt6, i, j, paramBoolean);
/*  108 */       break;
/*      */     case 4:
/*  110 */       paintRightTabBorder(paramInt2, paramGraphics, paramInt3, paramInt4, paramInt5, paramInt6, i, j, paramBoolean);
/*  111 */       break;
/*      */     case 1:
/*      */     default:
/*  114 */       paintTopTabBorder(paramInt2, paramGraphics, paramInt3, paramInt4, paramInt5, paramInt6, i, j, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintTopTabBorder(int paramInt1, Graphics paramGraphics, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean)
/*      */   {
/*  123 */     int i = getRunForTab(this.tabPane.getTabCount(), paramInt1);
/*  124 */     int j = lastTabInRun(this.tabPane.getTabCount(), i);
/*  125 */     int k = this.tabRuns[i];
/*  126 */     boolean bool = MetalUtils.isLeftToRight(this.tabPane);
/*  127 */     int m = this.tabPane.getSelectedIndex();
/*  128 */     int n = paramInt5 - 1;
/*  129 */     int i1 = paramInt4 - 1;
/*      */ 
/*  135 */     if (shouldFillGap(i, paramInt1, paramInt2, paramInt3)) {
/*  136 */       paramGraphics.translate(paramInt2, paramInt3);
/*      */ 
/*  138 */       if (bool) {
/*  139 */         paramGraphics.setColor(getColorForGap(i, paramInt2, paramInt3 + 1));
/*  140 */         paramGraphics.fillRect(1, 0, 5, 3);
/*  141 */         paramGraphics.fillRect(1, 3, 2, 2);
/*      */       } else {
/*  143 */         paramGraphics.setColor(getColorForGap(i, paramInt2 + paramInt4 - 1, paramInt3 + 1));
/*  144 */         paramGraphics.fillRect(i1 - 5, 0, 5, 3);
/*  145 */         paramGraphics.fillRect(i1 - 2, 3, 2, 2);
/*      */       }
/*      */ 
/*  148 */       paramGraphics.translate(-paramInt2, -paramInt3);
/*      */     }
/*      */ 
/*  151 */     paramGraphics.translate(paramInt2, paramInt3);
/*      */ 
/*  157 */     if ((this.ocean) && (paramBoolean)) {
/*  158 */       paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */     }
/*      */     else {
/*  161 */       paramGraphics.setColor(this.darkShadow);
/*      */     }
/*      */ 
/*  164 */     if (bool)
/*      */     {
/*  167 */       paramGraphics.drawLine(1, 5, 6, 0);
/*      */ 
/*  170 */       paramGraphics.drawLine(6, 0, i1, 0);
/*      */ 
/*  173 */       if (paramInt1 == j)
/*      */       {
/*  175 */         paramGraphics.drawLine(i1, 1, i1, n);
/*      */       }
/*      */ 
/*  178 */       if ((this.ocean) && (paramInt1 - 1 == m) && (i == getRunForTab(this.tabPane.getTabCount(), m)))
/*      */       {
/*  181 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */       }
/*      */ 
/*  185 */       if (paramInt1 != this.tabRuns[(this.runCount - 1)])
/*      */       {
/*  187 */         if ((this.ocean) && (paramBoolean)) {
/*  188 */           paramGraphics.drawLine(0, 6, 0, n);
/*  189 */           paramGraphics.setColor(this.darkShadow);
/*  190 */           paramGraphics.drawLine(0, 0, 0, 5);
/*      */         }
/*      */         else {
/*  193 */           paramGraphics.drawLine(0, 0, 0, n);
/*      */         }
/*      */       }
/*      */       else {
/*  197 */         paramGraphics.drawLine(0, 6, 0, n);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  202 */       paramGraphics.drawLine(i1 - 1, 5, i1 - 6, 0);
/*      */ 
/*  205 */       paramGraphics.drawLine(i1 - 6, 0, 0, 0);
/*      */ 
/*  208 */       if (paramInt1 == j)
/*      */       {
/*  210 */         paramGraphics.drawLine(0, 1, 0, n);
/*      */       }
/*      */ 
/*  214 */       if ((this.ocean) && (paramInt1 - 1 == m) && (i == getRunForTab(this.tabPane.getTabCount(), m)))
/*      */       {
/*  217 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*  218 */         paramGraphics.drawLine(i1, 0, i1, n);
/*      */       }
/*  220 */       else if ((this.ocean) && (paramBoolean)) {
/*  221 */         paramGraphics.drawLine(i1, 6, i1, n);
/*  222 */         if (paramInt1 != 0) {
/*  223 */           paramGraphics.setColor(this.darkShadow);
/*  224 */           paramGraphics.drawLine(i1, 0, i1, 5);
/*      */         }
/*      */ 
/*      */       }
/*  228 */       else if (paramInt1 != this.tabRuns[(this.runCount - 1)])
/*      */       {
/*  230 */         paramGraphics.drawLine(i1, 0, i1, n);
/*      */       }
/*      */       else {
/*  233 */         paramGraphics.drawLine(i1, 6, i1, n);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  242 */     paramGraphics.setColor(paramBoolean ? this.selectHighlight : this.highlight);
/*      */ 
/*  244 */     if (bool)
/*      */     {
/*  247 */       paramGraphics.drawLine(1, 6, 6, 1);
/*      */ 
/*  250 */       paramGraphics.drawLine(6, 1, paramInt1 == j ? i1 - 1 : i1, 1);
/*      */ 
/*  253 */       paramGraphics.drawLine(1, 6, 1, n);
/*      */ 
/*  257 */       if ((paramInt1 == k) && (paramInt1 != this.tabRuns[(this.runCount - 1)]))
/*      */       {
/*  259 */         if (this.tabPane.getSelectedIndex() == this.tabRuns[(i + 1)])
/*      */         {
/*  261 */           paramGraphics.setColor(this.selectHighlight);
/*      */         }
/*      */         else
/*      */         {
/*  265 */           paramGraphics.setColor(this.highlight);
/*      */         }
/*  267 */         paramGraphics.drawLine(1, 0, 1, 4);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  272 */       paramGraphics.drawLine(i1 - 1, 6, i1 - 6, 1);
/*      */ 
/*  275 */       paramGraphics.drawLine(i1 - 6, 1, 1, 1);
/*      */ 
/*  278 */       if (paramInt1 == j)
/*      */       {
/*  280 */         paramGraphics.drawLine(1, 1, 1, n);
/*      */       }
/*  282 */       else paramGraphics.drawLine(0, 1, 0, n);
/*      */ 
/*      */     }
/*      */ 
/*  286 */     paramGraphics.translate(-paramInt2, -paramInt3);
/*      */   }
/*      */ 
/*      */   protected boolean shouldFillGap(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  290 */     boolean bool = false;
/*      */ 
/*  292 */     if (!this.tabsOpaque) {
/*  293 */       return false;
/*      */     }
/*      */ 
/*  296 */     if (paramInt1 == this.runCount - 2) {
/*  297 */       Rectangle localRectangle1 = getTabBounds(this.tabPane, this.tabPane.getTabCount() - 1);
/*  298 */       Rectangle localRectangle2 = getTabBounds(this.tabPane, paramInt2);
/*      */       int i;
/*  299 */       if (MetalUtils.isLeftToRight(this.tabPane)) {
/*  300 */         i = localRectangle1.x + localRectangle1.width - 1;
/*      */ 
/*  304 */         if (i > localRectangle2.x + 2)
/*  305 */           return true;
/*      */       }
/*      */       else {
/*  308 */         i = localRectangle1.x;
/*  309 */         int j = localRectangle2.x + localRectangle2.width - 1;
/*      */ 
/*  313 */         if (i < j - 2)
/*  314 */           return true;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  319 */       bool = paramInt1 != this.runCount - 1;
/*      */     }
/*      */ 
/*  322 */     return bool;
/*      */   }
/*      */ 
/*      */   protected Color getColorForGap(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  327 */     int i = this.tabPane.getSelectedIndex();
/*  328 */     int j = this.tabRuns[(paramInt1 + 1)];
/*  329 */     int k = lastTabInRun(this.tabPane.getTabCount(), paramInt1 + 1);
/*  330 */     int m = -1;
/*      */ 
/*  332 */     for (int n = j; n <= k; n++) {
/*  333 */       Rectangle localRectangle = getTabBounds(this.tabPane, n);
/*  334 */       int i1 = localRectangle.x;
/*  335 */       int i2 = localRectangle.x + localRectangle.width - 1;
/*      */ 
/*  337 */       if (MetalUtils.isLeftToRight(this.tabPane)) {
/*  338 */         if ((i1 <= paramInt2) && (i2 - 4 > paramInt2)) {
/*  339 */           return i == n ? this.selectColor : getUnselectedBackgroundAt(n);
/*      */         }
/*      */ 
/*      */       }
/*  343 */       else if ((i1 + 4 < paramInt2) && (i2 >= paramInt2)) {
/*  344 */         return i == n ? this.selectColor : getUnselectedBackgroundAt(n);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  349 */     return this.tabPane.getBackground();
/*      */   }
/*      */ 
/*      */   protected void paintLeftTabBorder(int paramInt1, Graphics paramGraphics, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean)
/*      */   {
/*  356 */     int i = this.tabPane.getTabCount();
/*  357 */     int j = getRunForTab(i, paramInt1);
/*  358 */     int k = lastTabInRun(i, j);
/*  359 */     int m = this.tabRuns[j];
/*      */ 
/*  361 */     paramGraphics.translate(paramInt2, paramInt3);
/*      */ 
/*  363 */     int n = paramInt5 - 1;
/*  364 */     int i1 = paramInt4 - 1;
/*      */ 
/*  370 */     if ((paramInt1 != m) && (this.tabsOpaque)) {
/*  371 */       paramGraphics.setColor(this.tabPane.getSelectedIndex() == paramInt1 - 1 ? this.selectColor : getUnselectedBackgroundAt(paramInt1 - 1));
/*      */ 
/*  374 */       paramGraphics.fillRect(2, 0, 4, 3);
/*  375 */       paramGraphics.drawLine(2, 3, 2, 3);
/*      */     }
/*      */ 
/*  383 */     if (this.ocean) {
/*  384 */       paramGraphics.setColor(paramBoolean ? this.selectHighlight : MetalLookAndFeel.getWhite());
/*      */     }
/*      */     else
/*      */     {
/*  388 */       paramGraphics.setColor(paramBoolean ? this.selectHighlight : this.highlight);
/*      */     }
/*      */ 
/*  392 */     paramGraphics.drawLine(1, 6, 6, 1);
/*      */ 
/*  395 */     paramGraphics.drawLine(1, 6, 1, n);
/*      */ 
/*  398 */     paramGraphics.drawLine(6, 1, i1, 1);
/*      */ 
/*  400 */     if (paramInt1 != m) {
/*  401 */       if (this.tabPane.getSelectedIndex() == paramInt1 - 1)
/*  402 */         paramGraphics.setColor(this.selectHighlight);
/*      */       else {
/*  404 */         paramGraphics.setColor(this.ocean ? MetalLookAndFeel.getWhite() : this.highlight);
/*      */       }
/*      */ 
/*  407 */       paramGraphics.drawLine(1, 0, 1, 4);
/*      */     }
/*      */ 
/*  414 */     if (this.ocean) {
/*  415 */       if (paramBoolean) {
/*  416 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */       }
/*      */       else {
/*  419 */         paramGraphics.setColor(this.darkShadow);
/*      */       }
/*      */     }
/*      */     else {
/*  423 */       paramGraphics.setColor(this.darkShadow);
/*      */     }
/*      */ 
/*  427 */     paramGraphics.drawLine(1, 5, 6, 0);
/*      */ 
/*  430 */     paramGraphics.drawLine(6, 0, i1, 0);
/*      */ 
/*  433 */     if (paramInt1 == k) {
/*  434 */       paramGraphics.drawLine(0, n, i1, n);
/*      */     }
/*      */ 
/*  438 */     if (this.ocean) {
/*  439 */       if (this.tabPane.getSelectedIndex() == paramInt1 - 1) {
/*  440 */         paramGraphics.drawLine(0, 5, 0, n);
/*  441 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*  442 */         paramGraphics.drawLine(0, 0, 0, 5);
/*      */       }
/*  444 */       else if (paramBoolean) {
/*  445 */         paramGraphics.drawLine(0, 6, 0, n);
/*  446 */         if (paramInt1 != 0) {
/*  447 */           paramGraphics.setColor(this.darkShadow);
/*  448 */           paramGraphics.drawLine(0, 0, 0, 5);
/*      */         }
/*      */       }
/*  451 */       else if (paramInt1 != m) {
/*  452 */         paramGraphics.drawLine(0, 0, 0, n);
/*      */       } else {
/*  454 */         paramGraphics.drawLine(0, 6, 0, n);
/*      */       }
/*      */ 
/*      */     }
/*  458 */     else if (paramInt1 != m)
/*  459 */       paramGraphics.drawLine(0, 0, 0, n);
/*      */     else {
/*  461 */       paramGraphics.drawLine(0, 6, 0, n);
/*      */     }
/*      */ 
/*  465 */     paramGraphics.translate(-paramInt2, -paramInt3);
/*      */   }
/*      */ 
/*      */   protected void paintBottomTabBorder(int paramInt1, Graphics paramGraphics, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean)
/*      */   {
/*  473 */     int i = this.tabPane.getTabCount();
/*  474 */     int j = getRunForTab(i, paramInt1);
/*  475 */     int k = lastTabInRun(i, j);
/*  476 */     int m = this.tabRuns[j];
/*  477 */     boolean bool = MetalUtils.isLeftToRight(this.tabPane);
/*      */ 
/*  479 */     int n = paramInt5 - 1;
/*  480 */     int i1 = paramInt4 - 1;
/*      */ 
/*  486 */     if (shouldFillGap(j, paramInt1, paramInt2, paramInt3)) {
/*  487 */       paramGraphics.translate(paramInt2, paramInt3);
/*      */ 
/*  489 */       if (bool) {
/*  490 */         paramGraphics.setColor(getColorForGap(j, paramInt2, paramInt3));
/*  491 */         paramGraphics.fillRect(1, n - 4, 3, 5);
/*  492 */         paramGraphics.fillRect(4, n - 1, 2, 2);
/*      */       } else {
/*  494 */         paramGraphics.setColor(getColorForGap(j, paramInt2 + paramInt4 - 1, paramInt3));
/*  495 */         paramGraphics.fillRect(i1 - 3, n - 3, 3, 4);
/*  496 */         paramGraphics.fillRect(i1 - 5, n - 1, 2, 2);
/*  497 */         paramGraphics.drawLine(i1 - 1, n - 4, i1 - 1, n - 4);
/*      */       }
/*      */ 
/*  500 */       paramGraphics.translate(-paramInt2, -paramInt3);
/*      */     }
/*      */ 
/*  503 */     paramGraphics.translate(paramInt2, paramInt3);
/*      */ 
/*  510 */     if ((this.ocean) && (paramBoolean)) {
/*  511 */       paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */     }
/*      */     else {
/*  514 */       paramGraphics.setColor(this.darkShadow);
/*      */     }
/*      */ 
/*  517 */     if (bool)
/*      */     {
/*  520 */       paramGraphics.drawLine(1, n - 5, 6, n);
/*      */ 
/*  523 */       paramGraphics.drawLine(6, n, i1, n);
/*      */ 
/*  526 */       if (paramInt1 == k) {
/*  527 */         paramGraphics.drawLine(i1, 0, i1, n);
/*      */       }
/*      */ 
/*  531 */       if ((this.ocean) && (paramBoolean)) {
/*  532 */         paramGraphics.drawLine(0, 0, 0, n - 6);
/*  533 */         if (((j == 0) && (paramInt1 != 0)) || ((j > 0) && (paramInt1 != this.tabRuns[(j - 1)])))
/*      */         {
/*  535 */           paramGraphics.setColor(this.darkShadow);
/*  536 */           paramGraphics.drawLine(0, n - 5, 0, n);
/*      */         }
/*      */       }
/*      */       else {
/*  540 */         if ((this.ocean) && (paramInt1 == this.tabPane.getSelectedIndex() + 1)) {
/*  541 */           paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */         }
/*  543 */         if (paramInt1 != this.tabRuns[(this.runCount - 1)])
/*  544 */           paramGraphics.drawLine(0, 0, 0, n);
/*      */         else {
/*  546 */           paramGraphics.drawLine(0, 0, 0, n - 6);
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  552 */       paramGraphics.drawLine(i1 - 1, n - 5, i1 - 6, n);
/*      */ 
/*  555 */       paramGraphics.drawLine(i1 - 6, n, 0, n);
/*      */ 
/*  558 */       if (paramInt1 == k)
/*      */       {
/*  560 */         paramGraphics.drawLine(0, 0, 0, n);
/*      */       }
/*      */ 
/*  564 */       if ((this.ocean) && (paramInt1 == this.tabPane.getSelectedIndex() + 1)) {
/*  565 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*  566 */         paramGraphics.drawLine(i1, 0, i1, n);
/*      */       }
/*  568 */       else if ((this.ocean) && (paramBoolean)) {
/*  569 */         paramGraphics.drawLine(i1, 0, i1, n - 6);
/*  570 */         if (paramInt1 != m) {
/*  571 */           paramGraphics.setColor(this.darkShadow);
/*  572 */           paramGraphics.drawLine(i1, n - 5, i1, n);
/*      */         }
/*      */       }
/*  575 */       else if (paramInt1 != this.tabRuns[(this.runCount - 1)])
/*      */       {
/*  577 */         paramGraphics.drawLine(i1, 0, i1, n);
/*      */       }
/*      */       else {
/*  580 */         paramGraphics.drawLine(i1, 0, i1, n - 6);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  588 */     paramGraphics.setColor(paramBoolean ? this.selectHighlight : this.highlight);
/*      */ 
/*  590 */     if (bool)
/*      */     {
/*  593 */       paramGraphics.drawLine(1, n - 6, 6, n - 1);
/*      */ 
/*  596 */       paramGraphics.drawLine(1, 0, 1, n - 6);
/*      */ 
/*  600 */       if ((paramInt1 == m) && (paramInt1 != this.tabRuns[(this.runCount - 1)]))
/*      */       {
/*  602 */         if (this.tabPane.getSelectedIndex() == this.tabRuns[(j + 1)])
/*      */         {
/*  604 */           paramGraphics.setColor(this.selectHighlight);
/*      */         }
/*      */         else
/*      */         {
/*  608 */           paramGraphics.setColor(this.highlight);
/*      */         }
/*  610 */         paramGraphics.drawLine(1, n - 4, 1, n);
/*      */       }
/*      */ 
/*      */     }
/*  615 */     else if (paramInt1 == k)
/*      */     {
/*  617 */       paramGraphics.drawLine(1, 0, 1, n - 1);
/*      */     } else {
/*  619 */       paramGraphics.drawLine(0, 0, 0, n - 1);
/*      */     }
/*      */ 
/*  623 */     paramGraphics.translate(-paramInt2, -paramInt3);
/*      */   }
/*      */ 
/*      */   protected void paintRightTabBorder(int paramInt1, Graphics paramGraphics, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean)
/*      */   {
/*  630 */     int i = this.tabPane.getTabCount();
/*  631 */     int j = getRunForTab(i, paramInt1);
/*  632 */     int k = lastTabInRun(i, j);
/*  633 */     int m = this.tabRuns[j];
/*      */ 
/*  635 */     paramGraphics.translate(paramInt2, paramInt3);
/*      */ 
/*  637 */     int n = paramInt5 - 1;
/*  638 */     int i1 = paramInt4 - 1;
/*      */ 
/*  644 */     if ((paramInt1 != m) && (this.tabsOpaque)) {
/*  645 */       paramGraphics.setColor(this.tabPane.getSelectedIndex() == paramInt1 - 1 ? this.selectColor : getUnselectedBackgroundAt(paramInt1 - 1));
/*      */ 
/*  648 */       paramGraphics.fillRect(i1 - 5, 0, 5, 3);
/*  649 */       paramGraphics.fillRect(i1 - 2, 3, 2, 2);
/*      */     }
/*      */ 
/*  657 */     paramGraphics.setColor(paramBoolean ? this.selectHighlight : this.highlight);
/*      */ 
/*  660 */     paramGraphics.drawLine(i1 - 6, 1, i1 - 1, 6);
/*      */ 
/*  663 */     paramGraphics.drawLine(0, 1, i1 - 6, 1);
/*      */ 
/*  666 */     if (!paramBoolean) {
/*  667 */       paramGraphics.drawLine(0, 1, 0, n);
/*      */     }
/*      */ 
/*  675 */     if ((this.ocean) && (paramBoolean)) {
/*  676 */       paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */     }
/*      */     else {
/*  679 */       paramGraphics.setColor(this.darkShadow);
/*      */     }
/*      */ 
/*  683 */     if (paramInt1 == k) {
/*  684 */       paramGraphics.drawLine(0, n, i1, n);
/*      */     }
/*      */ 
/*  688 */     if ((this.ocean) && (this.tabPane.getSelectedIndex() == paramInt1 - 1)) {
/*  689 */       paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */     }
/*  691 */     paramGraphics.drawLine(i1 - 6, 0, i1, 6);
/*      */ 
/*  694 */     paramGraphics.drawLine(0, 0, i1 - 6, 0);
/*      */ 
/*  697 */     if ((this.ocean) && (paramBoolean)) {
/*  698 */       paramGraphics.drawLine(i1, 6, i1, n);
/*  699 */       if (paramInt1 != m) {
/*  700 */         paramGraphics.setColor(this.darkShadow);
/*  701 */         paramGraphics.drawLine(i1, 0, i1, 5);
/*      */       }
/*      */     }
/*  704 */     else if ((this.ocean) && (this.tabPane.getSelectedIndex() == paramInt1 - 1)) {
/*  705 */       paramGraphics.setColor(this.oceanSelectedBorderColor);
/*  706 */       paramGraphics.drawLine(i1, 0, i1, 6);
/*  707 */       paramGraphics.setColor(this.darkShadow);
/*  708 */       paramGraphics.drawLine(i1, 6, i1, n);
/*      */     }
/*  710 */     else if (paramInt1 != m) {
/*  711 */       paramGraphics.drawLine(i1, 0, i1, n);
/*      */     } else {
/*  713 */       paramGraphics.drawLine(i1, 6, i1, n);
/*      */     }
/*      */ 
/*  716 */     paramGraphics.translate(-paramInt2, -paramInt3);
/*      */   }
/*      */ 
/*      */   public void update(Graphics paramGraphics, JComponent paramJComponent) {
/*  720 */     if (paramJComponent.isOpaque()) {
/*  721 */       paramGraphics.setColor(this.tabAreaBackground);
/*  722 */       paramGraphics.fillRect(0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*      */     }
/*  724 */     paint(paramGraphics, paramJComponent);
/*      */   }
/*      */ 
/*      */   protected void paintTabBackground(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*      */   {
/*  729 */     int i = paramInt6 / 2;
/*  730 */     if (paramBoolean)
/*  731 */       paramGraphics.setColor(this.selectColor);
/*      */     else {
/*  733 */       paramGraphics.setColor(getUnselectedBackgroundAt(paramInt2));
/*      */     }
/*      */ 
/*  736 */     if (MetalUtils.isLeftToRight(this.tabPane))
/*  737 */       switch (paramInt1) {
/*      */       case 2:
/*  739 */         paramGraphics.fillRect(paramInt3 + 5, paramInt4 + 1, paramInt5 - 5, paramInt6 - 1);
/*  740 */         paramGraphics.fillRect(paramInt3 + 2, paramInt4 + 4, 3, paramInt6 - 4);
/*  741 */         break;
/*      */       case 3:
/*  743 */         paramGraphics.fillRect(paramInt3 + 2, paramInt4, paramInt5 - 2, paramInt6 - 4);
/*  744 */         paramGraphics.fillRect(paramInt3 + 5, paramInt4 + (paramInt6 - 1) - 3, paramInt5 - 5, 3);
/*  745 */         break;
/*      */       case 4:
/*  747 */         paramGraphics.fillRect(paramInt3, paramInt4 + 2, paramInt5 - 4, paramInt6 - 2);
/*  748 */         paramGraphics.fillRect(paramInt3 + (paramInt5 - 1) - 3, paramInt4 + 5, 3, paramInt6 - 5);
/*  749 */         break;
/*      */       case 1:
/*      */       default:
/*  752 */         paramGraphics.fillRect(paramInt3 + 4, paramInt4 + 2, paramInt5 - 1 - 3, paramInt6 - 1 - 1);
/*  753 */         paramGraphics.fillRect(paramInt3 + 2, paramInt4 + 5, 2, paramInt6 - 5); break;
/*      */       }
/*      */     else
/*  756 */       switch (paramInt1) {
/*      */       case 2:
/*  758 */         paramGraphics.fillRect(paramInt3 + 5, paramInt4 + 1, paramInt5 - 5, paramInt6 - 1);
/*  759 */         paramGraphics.fillRect(paramInt3 + 2, paramInt4 + 4, 3, paramInt6 - 4);
/*  760 */         break;
/*      */       case 3:
/*  762 */         paramGraphics.fillRect(paramInt3, paramInt4, paramInt5 - 5, paramInt6 - 1);
/*  763 */         paramGraphics.fillRect(paramInt3 + (paramInt5 - 1) - 4, paramInt4, 4, paramInt6 - 5);
/*  764 */         paramGraphics.fillRect(paramInt3 + (paramInt5 - 1) - 4, paramInt4 + (paramInt6 - 1) - 4, 2, 2);
/*  765 */         break;
/*      */       case 4:
/*  767 */         paramGraphics.fillRect(paramInt3 + 1, paramInt4 + 1, paramInt5 - 5, paramInt6 - 1);
/*  768 */         paramGraphics.fillRect(paramInt3 + (paramInt5 - 1) - 3, paramInt4 + 5, 3, paramInt6 - 5);
/*  769 */         break;
/*      */       case 1:
/*      */       default:
/*  772 */         paramGraphics.fillRect(paramInt3, paramInt4 + 2, paramInt5 - 1 - 3, paramInt6 - 1 - 1);
/*  773 */         paramGraphics.fillRect(paramInt3 + (paramInt5 - 1) - 3, paramInt4 + 5, 3, paramInt6 - 3);
/*      */       }
/*      */   }
/*      */ 
/*      */   protected int getTabLabelShiftX(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/*  782 */     return 0;
/*      */   }
/*      */ 
/*      */   protected int getTabLabelShiftY(int paramInt1, int paramInt2, boolean paramBoolean)
/*      */   {
/*  790 */     return 0;
/*      */   }
/*      */ 
/*      */   protected int getBaselineOffset()
/*      */   {
/*  799 */     return 0;
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/*  803 */     int i = this.tabPane.getTabPlacement();
/*      */ 
/*  805 */     Insets localInsets = paramJComponent.getInsets(); Dimension localDimension = paramJComponent.getSize();
/*      */ 
/*  808 */     if (this.tabPane.isOpaque()) {
/*  809 */       Color localColor = UIManager.getColor("TabbedPane.tabAreaBackground");
/*  810 */       if (localColor != null) {
/*  811 */         paramGraphics.setColor(localColor);
/*      */       }
/*      */       else {
/*  814 */         paramGraphics.setColor(paramJComponent.getBackground());
/*      */       }
/*  816 */       switch (i) {
/*      */       case 2:
/*  818 */         paramGraphics.fillRect(localInsets.left, localInsets.top, calculateTabAreaWidth(i, this.runCount, this.maxTabWidth), localDimension.height - localInsets.bottom - localInsets.top);
/*      */ 
/*  821 */         break;
/*      */       case 3:
/*  823 */         int j = calculateTabAreaHeight(i, this.runCount, this.maxTabHeight);
/*  824 */         paramGraphics.fillRect(localInsets.left, localDimension.height - localInsets.bottom - j, localDimension.width - localInsets.left - localInsets.right, j);
/*      */ 
/*  827 */         break;
/*      */       case 4:
/*  829 */         int k = calculateTabAreaWidth(i, this.runCount, this.maxTabWidth);
/*  830 */         paramGraphics.fillRect(localDimension.width - localInsets.right - k, localInsets.top, k, localDimension.height - localInsets.top - localInsets.bottom);
/*      */ 
/*  833 */         break;
/*      */       case 1:
/*      */       default:
/*  836 */         paramGraphics.fillRect(localInsets.left, localInsets.top, localDimension.width - localInsets.right - localInsets.left, calculateTabAreaHeight(i, this.runCount, this.maxTabHeight));
/*      */ 
/*  839 */         paintHighlightBelowTab();
/*      */       }
/*      */     }
/*      */ 
/*  843 */     super.paint(paramGraphics, paramJComponent);
/*      */   }
/*      */ 
/*      */   protected void paintHighlightBelowTab()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void paintFocusIndicator(Graphics paramGraphics, int paramInt1, Rectangle[] paramArrayOfRectangle, int paramInt2, Rectangle paramRectangle1, Rectangle paramRectangle2, boolean paramBoolean)
/*      */   {
/*  855 */     if ((this.tabPane.hasFocus()) && (paramBoolean)) {
/*  856 */       Rectangle localRectangle = paramArrayOfRectangle[paramInt2];
/*  857 */       boolean bool1 = isLastInRun(paramInt2);
/*  858 */       paramGraphics.setColor(this.focus);
/*  859 */       paramGraphics.translate(localRectangle.x, localRectangle.y);
/*  860 */       int i = localRectangle.width - 1;
/*  861 */       int j = localRectangle.height - 1;
/*  862 */       boolean bool2 = MetalUtils.isLeftToRight(this.tabPane);
/*  863 */       switch (paramInt1) {
/*      */       case 4:
/*  865 */         paramGraphics.drawLine(i - 6, 2, i - 2, 6);
/*  866 */         paramGraphics.drawLine(1, 2, i - 6, 2);
/*  867 */         paramGraphics.drawLine(i - 2, 6, i - 2, j);
/*  868 */         paramGraphics.drawLine(1, 2, 1, j);
/*  869 */         paramGraphics.drawLine(1, j, i - 2, j);
/*  870 */         break;
/*      */       case 3:
/*  872 */         if (bool2) {
/*  873 */           paramGraphics.drawLine(2, j - 6, 6, j - 2);
/*  874 */           paramGraphics.drawLine(6, j - 2, i, j - 2);
/*      */ 
/*  876 */           paramGraphics.drawLine(2, 0, 2, j - 6);
/*  877 */           paramGraphics.drawLine(2, 0, i, 0);
/*  878 */           paramGraphics.drawLine(i, 0, i, j - 2);
/*      */         } else {
/*  880 */           paramGraphics.drawLine(i - 2, j - 6, i - 6, j - 2);
/*      */ 
/*  882 */           paramGraphics.drawLine(i - 2, 0, i - 2, j - 6);
/*      */ 
/*  884 */           if (bool1)
/*      */           {
/*  886 */             paramGraphics.drawLine(2, j - 2, i - 6, j - 2);
/*      */ 
/*  888 */             paramGraphics.drawLine(2, 0, i - 2, 0);
/*  889 */             paramGraphics.drawLine(2, 0, 2, j - 2);
/*      */           } else {
/*  891 */             paramGraphics.drawLine(1, j - 2, i - 6, j - 2);
/*      */ 
/*  893 */             paramGraphics.drawLine(1, 0, i - 2, 0);
/*  894 */             paramGraphics.drawLine(1, 0, 1, j - 2);
/*      */           }
/*      */         }
/*  897 */         break;
/*      */       case 2:
/*  899 */         paramGraphics.drawLine(2, 6, 6, 2);
/*  900 */         paramGraphics.drawLine(2, 6, 2, j - 1);
/*  901 */         paramGraphics.drawLine(6, 2, i, 2);
/*  902 */         paramGraphics.drawLine(i, 2, i, j - 1);
/*  903 */         paramGraphics.drawLine(2, j - 1, i, j - 1);
/*      */ 
/*  905 */         break;
/*      */       case 1:
/*      */       default:
/*  908 */         if (bool2) {
/*  909 */           paramGraphics.drawLine(2, 6, 6, 2);
/*  910 */           paramGraphics.drawLine(2, 6, 2, j - 1);
/*  911 */           paramGraphics.drawLine(6, 2, i, 2);
/*  912 */           paramGraphics.drawLine(i, 2, i, j - 1);
/*  913 */           paramGraphics.drawLine(2, j - 1, i, j - 1);
/*      */         }
/*      */         else
/*      */         {
/*  917 */           paramGraphics.drawLine(i - 2, 6, i - 6, 2);
/*  918 */           paramGraphics.drawLine(i - 2, 6, i - 2, j - 1);
/*      */ 
/*  920 */           if (bool1)
/*      */           {
/*  922 */             paramGraphics.drawLine(i - 6, 2, 2, 2);
/*  923 */             paramGraphics.drawLine(2, 2, 2, j - 1);
/*  924 */             paramGraphics.drawLine(i - 2, j - 1, 2, j - 1);
/*      */           }
/*      */           else
/*      */           {
/*  928 */             paramGraphics.drawLine(i - 6, 2, 1, 2);
/*  929 */             paramGraphics.drawLine(1, 2, 1, j - 1);
/*  930 */             paramGraphics.drawLine(i - 2, j - 1, 1, j - 1);
/*      */           }
/*      */         }
/*      */         break;
/*      */       }
/*  935 */       paramGraphics.translate(-localRectangle.x, -localRectangle.y);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintContentBorderTopEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  942 */     boolean bool1 = MetalUtils.isLeftToRight(this.tabPane);
/*  943 */     int i = paramInt3 + paramInt5 - 1;
/*  944 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*      */ 
/*  946 */     if (this.ocean) {
/*  947 */       paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */     }
/*      */     else {
/*  950 */       paramGraphics.setColor(this.selectHighlight);
/*      */     }
/*      */ 
/*  957 */     if ((paramInt1 != 1) || (paramInt2 < 0) || (localRectangle.y + localRectangle.height + 1 < paramInt4) || (localRectangle.x < paramInt3) || (localRectangle.x > paramInt3 + paramInt5))
/*      */     {
/*  960 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3 + paramInt5 - 2, paramInt4);
/*  961 */       if ((this.ocean) && (paramInt1 == 1)) {
/*  962 */         paramGraphics.setColor(MetalLookAndFeel.getWhite());
/*  963 */         paramGraphics.drawLine(paramInt3, paramInt4 + 1, paramInt3 + paramInt5 - 2, paramInt4 + 1);
/*      */       }
/*      */     }
/*      */     else {
/*  967 */       boolean bool2 = isLastInRun(paramInt2);
/*      */ 
/*  969 */       if ((bool1) || (bool2))
/*  970 */         paramGraphics.drawLine(paramInt3, paramInt4, localRectangle.x + 1, paramInt4);
/*      */       else {
/*  972 */         paramGraphics.drawLine(paramInt3, paramInt4, localRectangle.x, paramInt4);
/*      */       }
/*      */ 
/*  975 */       if (localRectangle.x + localRectangle.width < i - 1) {
/*  976 */         if ((bool1) && (!bool2))
/*  977 */           paramGraphics.drawLine(localRectangle.x + localRectangle.width, paramInt4, i - 1, paramInt4);
/*      */         else
/*  979 */           paramGraphics.drawLine(localRectangle.x + localRectangle.width - 1, paramInt4, i - 1, paramInt4);
/*      */       }
/*      */       else {
/*  982 */         paramGraphics.setColor(this.shadow);
/*  983 */         paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4, paramInt3 + paramInt5 - 2, paramInt4);
/*      */       }
/*      */ 
/*  986 */       if (this.ocean) {
/*  987 */         paramGraphics.setColor(MetalLookAndFeel.getWhite());
/*      */ 
/*  989 */         if ((bool1) || (bool2))
/*  990 */           paramGraphics.drawLine(paramInt3, paramInt4 + 1, localRectangle.x + 1, paramInt4 + 1);
/*      */         else {
/*  992 */           paramGraphics.drawLine(paramInt3, paramInt4 + 1, localRectangle.x, paramInt4 + 1);
/*      */         }
/*      */ 
/*  995 */         if (localRectangle.x + localRectangle.width < i - 1) {
/*  996 */           if ((bool1) && (!bool2)) {
/*  997 */             paramGraphics.drawLine(localRectangle.x + localRectangle.width, paramInt4 + 1, i - 1, paramInt4 + 1);
/*      */           }
/*      */           else
/* 1000 */             paramGraphics.drawLine(localRectangle.x + localRectangle.width - 1, paramInt4 + 1, i - 1, paramInt4 + 1);
/*      */         }
/*      */         else
/*      */         {
/* 1004 */           paramGraphics.setColor(this.shadow);
/* 1005 */           paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 1, paramInt3 + paramInt5 - 2, paramInt4 + 1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintContentBorderBottomEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1014 */     boolean bool1 = MetalUtils.isLeftToRight(this.tabPane);
/* 1015 */     int i = paramInt4 + paramInt6 - 1;
/* 1016 */     int j = paramInt3 + paramInt5 - 1;
/* 1017 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*      */ 
/* 1020 */     paramGraphics.setColor(this.darkShadow);
/*      */ 
/* 1026 */     if ((paramInt1 != 3) || (paramInt2 < 0) || (localRectangle.y - 1 > paramInt6) || (localRectangle.x < paramInt3) || (localRectangle.x > paramInt3 + paramInt5))
/*      */     {
/* 1029 */       if ((this.ocean) && (paramInt1 == 3)) {
/* 1030 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */       }
/* 1032 */       paramGraphics.drawLine(paramInt3, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/*      */     }
/*      */     else {
/* 1035 */       boolean bool2 = isLastInRun(paramInt2);
/*      */ 
/* 1037 */       if (this.ocean) {
/* 1038 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */       }
/*      */ 
/* 1041 */       if ((bool1) || (bool2))
/* 1042 */         paramGraphics.drawLine(paramInt3, i, localRectangle.x, i);
/*      */       else {
/* 1044 */         paramGraphics.drawLine(paramInt3, i, localRectangle.x - 1, i);
/*      */       }
/*      */ 
/* 1047 */       if (localRectangle.x + localRectangle.width < paramInt3 + paramInt5 - 2)
/* 1048 */         if ((bool1) && (!bool2)) {
/* 1049 */           paramGraphics.drawLine(localRectangle.x + localRectangle.width, i, j, i);
/*      */         }
/*      */         else
/* 1052 */           paramGraphics.drawLine(localRectangle.x + localRectangle.width - 1, i, j, i);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintContentBorderLeftEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1062 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*      */ 
/* 1064 */     if (this.ocean) {
/* 1065 */       paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */     }
/*      */     else {
/* 1068 */       paramGraphics.setColor(this.selectHighlight);
/*      */     }
/*      */ 
/* 1075 */     if ((paramInt1 != 2) || (paramInt2 < 0) || (localRectangle.x + localRectangle.width + 1 < paramInt3) || (localRectangle.y < paramInt4) || (localRectangle.y > paramInt4 + paramInt6))
/*      */     {
/* 1078 */       paramGraphics.drawLine(paramInt3, paramInt4 + 1, paramInt3, paramInt4 + paramInt6 - 2);
/* 1079 */       if ((this.ocean) && (paramInt1 == 2)) {
/* 1080 */         paramGraphics.setColor(MetalLookAndFeel.getWhite());
/* 1081 */         paramGraphics.drawLine(paramInt3 + 1, paramInt4, paramInt3 + 1, paramInt4 + paramInt6 - 2);
/*      */       }
/*      */     }
/*      */     else {
/* 1085 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3, localRectangle.y + 1);
/* 1086 */       if (localRectangle.y + localRectangle.height < paramInt4 + paramInt6 - 2) {
/* 1087 */         paramGraphics.drawLine(paramInt3, localRectangle.y + localRectangle.height + 1, paramInt3, paramInt4 + paramInt6 + 2);
/*      */       }
/*      */ 
/* 1090 */       if (this.ocean) {
/* 1091 */         paramGraphics.setColor(MetalLookAndFeel.getWhite());
/* 1092 */         paramGraphics.drawLine(paramInt3 + 1, paramInt4 + 1, paramInt3 + 1, localRectangle.y + 1);
/* 1093 */         if (localRectangle.y + localRectangle.height < paramInt4 + paramInt6 - 2)
/* 1094 */           paramGraphics.drawLine(paramInt3 + 1, localRectangle.y + localRectangle.height + 1, paramInt3 + 1, paramInt4 + paramInt6 + 2);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void paintContentBorderRightEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1104 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*      */ 
/* 1107 */     paramGraphics.setColor(this.darkShadow);
/*      */ 
/* 1112 */     if ((paramInt1 != 4) || (paramInt2 < 0) || (localRectangle.x - 1 > paramInt5) || (localRectangle.y < paramInt4) || (localRectangle.y > paramInt4 + paramInt6))
/*      */     {
/* 1115 */       if ((this.ocean) && (paramInt1 == 4)) {
/* 1116 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */       }
/* 1118 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/*      */     }
/*      */     else {
/* 1121 */       if (this.ocean) {
/* 1122 */         paramGraphics.setColor(this.oceanSelectedBorderColor);
/*      */       }
/* 1124 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4, paramInt3 + paramInt5 - 1, localRectangle.y);
/*      */ 
/* 1126 */       if (localRectangle.y + localRectangle.height < paramInt4 + paramInt6 - 2)
/* 1127 */         paramGraphics.drawLine(paramInt3 + paramInt5 - 1, localRectangle.y + localRectangle.height, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 2);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected int calculateMaxTabHeight(int paramInt)
/*      */   {
/* 1134 */     FontMetrics localFontMetrics = getFontMetrics();
/* 1135 */     int i = localFontMetrics.getHeight();
/* 1136 */     int j = 0;
/*      */ 
/* 1138 */     for (int k = 0; k < this.tabPane.getTabCount(); k++) {
/* 1139 */       Icon localIcon = this.tabPane.getIconAt(k);
/* 1140 */       if ((localIcon != null) && 
/* 1141 */         (localIcon.getIconHeight() > i)) {
/* 1142 */         j = 1;
/* 1143 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1147 */     return super.calculateMaxTabHeight(paramInt) - (j != 0 ? this.tabInsets.top + this.tabInsets.bottom : 0);
/*      */   }
/*      */ 
/*      */   protected int getTabRunOverlay(int paramInt)
/*      */   {
/* 1155 */     if ((paramInt == 2) || (paramInt == 4)) {
/* 1156 */       int i = calculateMaxTabHeight(paramInt);
/* 1157 */       return i / 2;
/*      */     }
/* 1159 */     return 0;
/*      */   }
/*      */ 
/*      */   protected boolean shouldRotateTabRuns(int paramInt1, int paramInt2)
/*      */   {
/* 1164 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean shouldPadTabRun(int paramInt1, int paramInt2)
/*      */   {
/* 1169 */     return (this.runCount > 1) && (paramInt2 < this.runCount - 1);
/*      */   }
/*      */ 
/*      */   private boolean isLastInRun(int paramInt) {
/* 1173 */     int i = getRunForTab(this.tabPane.getTabCount(), paramInt);
/* 1174 */     int j = lastTabInRun(this.tabPane.getTabCount(), i);
/* 1175 */     return paramInt == j;
/*      */   }
/*      */ 
/*      */   private Color getUnselectedBackgroundAt(int paramInt)
/*      */   {
/* 1182 */     Color localColor = this.tabPane.getBackgroundAt(paramInt);
/* 1183 */     if (((localColor instanceof UIResource)) && 
/* 1184 */       (this.unselectedBackground != null)) {
/* 1185 */       return this.unselectedBackground;
/*      */     }
/*      */ 
/* 1188 */     return localColor;
/*      */   }
/*      */ 
/*      */   int getRolloverTabIndex()
/*      */   {
/* 1195 */     return getRolloverTab();
/*      */   }
/*      */ 
/*      */   public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout
/*      */   {
/*      */     public TabbedPaneLayout()
/*      */     {
/* 1205 */       super();
/*      */     }
/*      */ 
/*      */     protected void normalizeTabRuns(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */     {
/* 1215 */       if ((paramInt1 == 1) || (paramInt1 == 3))
/* 1216 */         super.normalizeTabRuns(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     protected void rotateTabRuns(int paramInt1, int paramInt2)
/*      */     {
/*      */     }
/*      */ 
/*      */     protected void padSelectedTab(int paramInt1, int paramInt2)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalTabbedPaneUI
 * JD-Core Version:    0.6.2
 */
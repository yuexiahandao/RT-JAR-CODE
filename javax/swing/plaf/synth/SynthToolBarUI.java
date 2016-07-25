/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Box.Filler;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicToolBarUI;
/*     */ import javax.swing.plaf.basic.BasicToolBarUI.DragWindow;
/*     */ import sun.swing.plaf.synth.SynthIcon;
/*     */ 
/*     */ public class SynthToolBarUI extends BasicToolBarUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private Icon handleIcon;
/*     */   private Rectangle contentRect;
/*     */   private SynthStyle style;
/*     */   private SynthStyle contentStyle;
/*     */   private SynthStyle dragWindowStyle;
/*     */ 
/*     */   public SynthToolBarUI()
/*     */   {
/*  54 */     this.handleIcon = null;
/*  55 */     this.contentRect = new Rectangle();
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  68 */     return new SynthToolBarUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  76 */     this.toolBar.setLayout(createLayout());
/*  77 */     updateStyle(this.toolBar);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  85 */     super.installListeners();
/*  86 */     this.toolBar.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/*  94 */     super.uninstallListeners();
/*  95 */     this.toolBar.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JToolBar paramJToolBar) {
/*  99 */     SynthContext localSynthContext = getContext(paramJToolBar, Region.TOOL_BAR_CONTENT, null, 1);
/*     */ 
/* 101 */     this.contentStyle = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 102 */     localSynthContext.dispose();
/*     */ 
/* 104 */     localSynthContext = getContext(paramJToolBar, Region.TOOL_BAR_DRAG_WINDOW, null, 1);
/* 105 */     this.dragWindowStyle = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 106 */     localSynthContext.dispose();
/*     */ 
/* 108 */     localSynthContext = getContext(paramJToolBar, 1);
/* 109 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/* 111 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 112 */     if (localSynthStyle != this.style) {
/* 113 */       this.handleIcon = this.style.getIcon(localSynthContext, "ToolBar.handleIcon");
/*     */ 
/* 115 */       if (localSynthStyle != null) {
/* 116 */         uninstallKeyboardActions();
/* 117 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 120 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 128 */     SynthContext localSynthContext = getContext(this.toolBar, 1);
/*     */ 
/* 130 */     this.style.uninstallDefaults(localSynthContext);
/* 131 */     localSynthContext.dispose();
/* 132 */     this.style = null;
/*     */ 
/* 134 */     this.handleIcon = null;
/*     */ 
/* 136 */     localSynthContext = getContext(this.toolBar, Region.TOOL_BAR_CONTENT, this.contentStyle, 1);
/*     */ 
/* 138 */     this.contentStyle.uninstallDefaults(localSynthContext);
/* 139 */     localSynthContext.dispose();
/* 140 */     this.contentStyle = null;
/*     */ 
/* 142 */     localSynthContext = getContext(this.toolBar, Region.TOOL_BAR_DRAG_WINDOW, this.dragWindowStyle, 1);
/*     */ 
/* 144 */     this.dragWindowStyle.uninstallDefaults(localSynthContext);
/* 145 */     localSynthContext.dispose();
/* 146 */     this.dragWindowStyle = null;
/*     */ 
/* 148 */     this.toolBar.setLayout(null);
/*     */   }
/*     */ 
/*     */   protected void installComponents()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayout()
/*     */   {
/* 169 */     return new SynthToolBarLayoutManager();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 177 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 181 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, SynthStyle paramSynthStyle)
/*     */   {
/* 186 */     return SynthContext.getContext(SynthContext.class, paramJComponent, paramRegion, paramSynthStyle, getComponentState(paramJComponent, paramRegion));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, SynthStyle paramSynthStyle, int paramInt)
/*     */   {
/* 192 */     return SynthContext.getContext(SynthContext.class, paramJComponent, paramRegion, paramSynthStyle, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 197 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 214 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 216 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 217 */     localSynthContext.getPainter().paintToolBarBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), this.toolBar.getOrientation());
/*     */ 
/* 220 */     paint(localSynthContext, paramGraphics);
/* 221 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 235 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 237 */     paint(localSynthContext, paramGraphics);
/* 238 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 247 */     paramSynthContext.getPainter().paintToolBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.toolBar.getOrientation());
/*     */   }
/*     */ 
/*     */   protected void setBorderToNonRollover(Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void setBorderToRollover(Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void setBorderToNormal(Component paramComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 283 */     if ((this.handleIcon != null) && (this.toolBar.isFloatable())) {
/* 284 */       int i = this.toolBar.getComponentOrientation().isLeftToRight() ? 0 : this.toolBar.getWidth() - SynthIcon.getIconWidth(this.handleIcon, paramSynthContext);
/*     */ 
/* 287 */       SynthIcon.paintIcon(this.handleIcon, paramSynthContext, paramGraphics, i, 0, SynthIcon.getIconWidth(this.handleIcon, paramSynthContext), SynthIcon.getIconHeight(this.handleIcon, paramSynthContext));
/*     */     }
/*     */ 
/* 292 */     SynthContext localSynthContext = getContext(this.toolBar, Region.TOOL_BAR_CONTENT, this.contentStyle);
/*     */ 
/* 294 */     paintContent(localSynthContext, paramGraphics, this.contentRect);
/* 295 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paintContent(SynthContext paramSynthContext, Graphics paramGraphics, Rectangle paramRectangle)
/*     */   {
/* 307 */     SynthLookAndFeel.updateSubregion(paramSynthContext, paramGraphics, paramRectangle);
/* 308 */     paramSynthContext.getPainter().paintToolBarContentBackground(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, this.toolBar.getOrientation());
/*     */ 
/* 311 */     paramSynthContext.getPainter().paintToolBarContentBorder(paramSynthContext, paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, this.toolBar.getOrientation());
/*     */   }
/*     */ 
/*     */   protected void paintDragWindow(Graphics paramGraphics)
/*     */   {
/* 321 */     int i = this.dragWindow.getWidth();
/* 322 */     int j = this.dragWindow.getHeight();
/* 323 */     SynthContext localSynthContext = getContext(this.toolBar, Region.TOOL_BAR_DRAG_WINDOW, this.dragWindowStyle);
/*     */ 
/* 325 */     SynthLookAndFeel.updateSubregion(localSynthContext, paramGraphics, new Rectangle(0, 0, i, j));
/*     */ 
/* 327 */     localSynthContext.getPainter().paintToolBarDragWindowBackground(localSynthContext, paramGraphics, 0, 0, i, j, this.dragWindow.getOrientation());
/*     */ 
/* 330 */     localSynthContext.getPainter().paintToolBarDragWindowBorder(localSynthContext, paramGraphics, 0, 0, i, j, this.dragWindow.getOrientation());
/*     */ 
/* 332 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 344 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 345 */       updateStyle((JToolBar)paramPropertyChangeEvent.getSource()); 
/*     */   }
/*     */   class SynthToolBarLayoutManager implements LayoutManager {
/*     */     SynthToolBarLayoutManager() {
/*     */     }
/*     */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*     */     }
/*     */ 
/*     */     public void removeLayoutComponent(Component paramComponent) {
/*     */     }
/*     */ 
/* 356 */     public Dimension minimumLayoutSize(Container paramContainer) { JToolBar localJToolBar = (JToolBar)paramContainer;
/* 357 */       Insets localInsets = localJToolBar.getInsets();
/* 358 */       Dimension localDimension1 = new Dimension();
/* 359 */       SynthContext localSynthContext = SynthToolBarUI.this.getContext(localJToolBar);
/*     */       int i;
/*     */       Component localComponent;
/*     */       Dimension localDimension2;
/* 361 */       if (localJToolBar.getOrientation() == 0) {
/* 362 */         localDimension1.width = (localJToolBar.isFloatable() ? SynthIcon.getIconWidth(SynthToolBarUI.this.handleIcon, localSynthContext) : 0);
/*     */ 
/* 365 */         for (i = 0; i < localJToolBar.getComponentCount(); i++) {
/* 366 */           localComponent = localJToolBar.getComponent(i);
/* 367 */           if (localComponent.isVisible()) {
/* 368 */             localDimension2 = localComponent.getMinimumSize();
/* 369 */             localDimension1.width += localDimension2.width;
/* 370 */             localDimension1.height = Math.max(localDimension1.height, localDimension2.height);
/*     */           }
/*     */         }
/*     */       } else {
/* 374 */         localDimension1.height = (localJToolBar.isFloatable() ? SynthIcon.getIconHeight(SynthToolBarUI.this.handleIcon, localSynthContext) : 0);
/*     */ 
/* 377 */         for (i = 0; i < localJToolBar.getComponentCount(); i++) {
/* 378 */           localComponent = localJToolBar.getComponent(i);
/* 379 */           if (localComponent.isVisible()) {
/* 380 */             localDimension2 = localComponent.getMinimumSize();
/* 381 */             localDimension1.width = Math.max(localDimension1.width, localDimension2.width);
/* 382 */             localDimension1.height += localDimension2.height;
/*     */           }
/*     */         }
/*     */       }
/* 386 */       localDimension1.width += localInsets.left + localInsets.right;
/* 387 */       localDimension1.height += localInsets.top + localInsets.bottom;
/*     */ 
/* 389 */       localSynthContext.dispose();
/* 390 */       return localDimension1; }
/*     */ 
/*     */     public Dimension preferredLayoutSize(Container paramContainer)
/*     */     {
/* 394 */       JToolBar localJToolBar = (JToolBar)paramContainer;
/* 395 */       Insets localInsets = localJToolBar.getInsets();
/* 396 */       Dimension localDimension1 = new Dimension();
/* 397 */       SynthContext localSynthContext = SynthToolBarUI.this.getContext(localJToolBar);
/*     */       int i;
/*     */       Component localComponent;
/*     */       Dimension localDimension2;
/* 399 */       if (localJToolBar.getOrientation() == 0) {
/* 400 */         localDimension1.width = (localJToolBar.isFloatable() ? SynthIcon.getIconWidth(SynthToolBarUI.this.handleIcon, localSynthContext) : 0);
/*     */ 
/* 403 */         for (i = 0; i < localJToolBar.getComponentCount(); i++) {
/* 404 */           localComponent = localJToolBar.getComponent(i);
/* 405 */           if (localComponent.isVisible()) {
/* 406 */             localDimension2 = localComponent.getPreferredSize();
/* 407 */             localDimension1.width += localDimension2.width;
/* 408 */             localDimension1.height = Math.max(localDimension1.height, localDimension2.height);
/*     */           }
/*     */         }
/*     */       } else {
/* 412 */         localDimension1.height = (localJToolBar.isFloatable() ? SynthIcon.getIconHeight(SynthToolBarUI.this.handleIcon, localSynthContext) : 0);
/*     */ 
/* 415 */         for (i = 0; i < localJToolBar.getComponentCount(); i++) {
/* 416 */           localComponent = localJToolBar.getComponent(i);
/* 417 */           if (localComponent.isVisible()) {
/* 418 */             localDimension2 = localComponent.getPreferredSize();
/* 419 */             localDimension1.width = Math.max(localDimension1.width, localDimension2.width);
/* 420 */             localDimension1.height += localDimension2.height;
/*     */           }
/*     */         }
/*     */       }
/* 424 */       localDimension1.width += localInsets.left + localInsets.right;
/* 425 */       localDimension1.height += localInsets.top + localInsets.bottom;
/*     */ 
/* 427 */       localSynthContext.dispose();
/* 428 */       return localDimension1;
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer) {
/* 432 */       JToolBar localJToolBar = (JToolBar)paramContainer;
/* 433 */       Insets localInsets = localJToolBar.getInsets();
/* 434 */       boolean bool = localJToolBar.getComponentOrientation().isLeftToRight();
/* 435 */       SynthContext localSynthContext = SynthToolBarUI.this.getContext(localJToolBar);
/*     */ 
/* 447 */       int i = 0;
/* 448 */       for (int j = 0; j < localJToolBar.getComponentCount(); j++)
/* 449 */         if (isGlue(localJToolBar.getComponent(j))) i++;
/*     */       int k;
/*     */       int m;
/*     */       int n;
/*     */       int i1;
/*     */       int i2;
/*     */       Component localComponent;
/*     */       Dimension localDimension;
/*     */       int i3;
/*     */       int i4;
/* 452 */       if (localJToolBar.getOrientation() == 0) {
/* 453 */         j = localJToolBar.isFloatable() ? SynthIcon.getIconWidth(SynthToolBarUI.this.handleIcon, localSynthContext) : 0;
/*     */ 
/* 459 */         SynthToolBarUI.this.contentRect.x = (bool ? j : 0);
/* 460 */         SynthToolBarUI.this.contentRect.y = 0;
/* 461 */         SynthToolBarUI.this.contentRect.width = (localJToolBar.getWidth() - j);
/* 462 */         SynthToolBarUI.this.contentRect.height = localJToolBar.getHeight();
/*     */ 
/* 466 */         k = bool ? j + localInsets.left : localJToolBar.getWidth() - j - localInsets.right;
/*     */ 
/* 469 */         m = localInsets.top;
/* 470 */         n = localJToolBar.getHeight() - localInsets.top - localInsets.bottom;
/*     */ 
/* 475 */         i1 = 0;
/* 476 */         if (i > 0) {
/* 477 */           i2 = minimumLayoutSize(paramContainer).width;
/* 478 */           i1 = (localJToolBar.getWidth() - i2) / i;
/* 479 */           if (i1 < 0) i1 = 0;
/*     */         }
/*     */ 
/* 482 */         for (i2 = 0; i2 < localJToolBar.getComponentCount(); i2++) {
/* 483 */           localComponent = localJToolBar.getComponent(i2);
/* 484 */           if (localComponent.isVisible()) {
/* 485 */             localDimension = localComponent.getPreferredSize();
/*     */ 
/* 487 */             if ((localDimension.height >= n) || ((localComponent instanceof JSeparator)))
/*     */             {
/* 489 */               i3 = m;
/* 490 */               i4 = n;
/*     */             }
/*     */             else {
/* 493 */               i3 = m + n / 2 - localDimension.height / 2;
/* 494 */               i4 = localDimension.height;
/*     */             }
/*     */ 
/* 498 */             if (isGlue(localComponent)) localDimension.width += i1;
/* 499 */             localComponent.setBounds(bool ? k : k - localDimension.width, i3, localDimension.width, i4);
/* 500 */             k = bool ? k + localDimension.width : k - localDimension.width;
/*     */           }
/*     */         }
/*     */       } else {
/* 504 */         j = localJToolBar.isFloatable() ? SynthIcon.getIconHeight(SynthToolBarUI.this.handleIcon, localSynthContext) : 0;
/*     */ 
/* 508 */         SynthToolBarUI.this.contentRect.x = 0;
/* 509 */         SynthToolBarUI.this.contentRect.y = j;
/* 510 */         SynthToolBarUI.this.contentRect.width = localJToolBar.getWidth();
/* 511 */         SynthToolBarUI.this.contentRect.height = (localJToolBar.getHeight() - j);
/*     */ 
/* 513 */         k = localInsets.left;
/* 514 */         m = localJToolBar.getWidth() - localInsets.left - localInsets.right;
/* 515 */         n = j + localInsets.top;
/*     */ 
/* 520 */         i1 = 0;
/* 521 */         if (i > 0) {
/* 522 */           i2 = minimumLayoutSize(paramContainer).height;
/* 523 */           i1 = (localJToolBar.getHeight() - i2) / i;
/* 524 */           if (i1 < 0) i1 = 0;
/*     */         }
/*     */ 
/* 527 */         for (i2 = 0; i2 < localJToolBar.getComponentCount(); i2++) {
/* 528 */           localComponent = localJToolBar.getComponent(i2);
/* 529 */           if (localComponent.isVisible()) {
/* 530 */             localDimension = localComponent.getPreferredSize();
/*     */ 
/* 532 */             if ((localDimension.width >= m) || ((localComponent instanceof JSeparator)))
/*     */             {
/* 534 */               i3 = k;
/* 535 */               i4 = m;
/*     */             }
/*     */             else {
/* 538 */               i3 = k + m / 2 - localDimension.width / 2;
/* 539 */               i4 = localDimension.width;
/*     */             }
/*     */ 
/* 543 */             if (isGlue(localComponent)) localDimension.height += i1;
/* 544 */             localComponent.setBounds(i3, n, i4, localDimension.height);
/* 545 */             n += localDimension.height;
/*     */           }
/*     */         }
/*     */       }
/* 549 */       localSynthContext.dispose();
/*     */     }
/*     */ 
/*     */     private boolean isGlue(Component paramComponent) {
/* 553 */       if ((paramComponent.isVisible()) && ((paramComponent instanceof Box.Filler))) {
/* 554 */         Box.Filler localFiller = (Box.Filler)paramComponent;
/* 555 */         Dimension localDimension1 = localFiller.getMinimumSize();
/* 556 */         Dimension localDimension2 = localFiller.getPreferredSize();
/* 557 */         return (localDimension1.width == 0) && (localDimension1.height == 0) && (localDimension2.width == 0) && (localDimension2.height == 0);
/*     */       }
/*     */ 
/* 560 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthToolBarUI
 * JD-Core Version:    0.6.2
 */
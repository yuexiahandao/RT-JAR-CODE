/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MotifBorders
/*     */ {
/*     */   public static void drawBezel(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2, Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */   {
/* 572 */     Color localColor = paramGraphics.getColor();
/* 573 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 575 */     if (paramBoolean1) {
/* 576 */       if (paramBoolean2) {
/* 577 */         paramGraphics.setColor(paramColor4);
/* 578 */         paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/*     */       }
/* 580 */       paramGraphics.setColor(paramColor1);
/* 581 */       paramGraphics.drawRect(1, 1, paramInt3 - 3, paramInt4 - 3);
/*     */ 
/* 583 */       paramGraphics.setColor(paramColor2);
/* 584 */       paramGraphics.drawLine(2, paramInt4 - 3, paramInt3 - 3, paramInt4 - 3);
/* 585 */       paramGraphics.drawLine(paramInt3 - 3, 2, paramInt3 - 3, paramInt4 - 4);
/*     */     }
/*     */     else {
/* 588 */       if (paramBoolean2) {
/* 589 */         paramGraphics.setColor(paramColor4);
/* 590 */         paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/* 592 */         paramGraphics.setColor(paramColor2);
/* 593 */         paramGraphics.drawLine(1, 1, 1, paramInt4 - 3);
/* 594 */         paramGraphics.drawLine(2, 1, paramInt3 - 4, 1);
/*     */ 
/* 596 */         paramGraphics.setColor(paramColor1);
/* 597 */         paramGraphics.drawLine(2, paramInt4 - 3, paramInt3 - 3, paramInt4 - 3);
/* 598 */         paramGraphics.drawLine(paramInt3 - 3, 1, paramInt3 - 3, paramInt4 - 4);
/*     */ 
/* 600 */         paramGraphics.setColor(paramColor3);
/* 601 */         paramGraphics.drawLine(1, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/* 602 */         paramGraphics.drawLine(paramInt3 - 2, paramInt4 - 2, paramInt3 - 2, 1);
/*     */       } else {
/* 604 */         paramGraphics.setColor(paramColor2);
/* 605 */         paramGraphics.drawLine(1, 1, 1, paramInt4 - 3);
/* 606 */         paramGraphics.drawLine(2, 1, paramInt3 - 4, 1);
/* 607 */         paramGraphics.setColor(paramColor1);
/* 608 */         paramGraphics.drawLine(2, paramInt4 - 3, paramInt3 - 3, paramInt4 - 3);
/* 609 */         paramGraphics.drawLine(paramInt3 - 3, 1, paramInt3 - 3, paramInt4 - 4);
/*     */ 
/* 611 */         paramGraphics.setColor(paramColor3);
/* 612 */         paramGraphics.drawLine(1, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/* 613 */         paramGraphics.drawLine(paramInt3 - 2, paramInt4 - 2, paramInt3 - 2, 0);
/*     */       }
/*     */ 
/* 616 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/* 618 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   public static class BevelBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*  58 */     private Color darkShadow = UIManager.getColor("controlShadow");
/*  59 */     private Color lightShadow = UIManager.getColor("controlLtHighlight");
/*     */     private boolean isRaised;
/*     */ 
/*     */     public BevelBorder(boolean paramBoolean, Color paramColor1, Color paramColor2)
/*     */     {
/*  63 */       this.isRaised = paramBoolean;
/*  64 */       this.darkShadow = paramColor1;
/*  65 */       this.lightShadow = paramColor2;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  69 */       paramGraphics.setColor(this.isRaised ? this.lightShadow : this.darkShadow);
/*  70 */       paramGraphics.drawLine(paramInt1, paramInt2, paramInt1 + paramInt3 - 1, paramInt2);
/*  71 */       paramGraphics.drawLine(paramInt1, paramInt2 + paramInt4 - 1, paramInt1, paramInt2 + 1);
/*     */ 
/*  73 */       paramGraphics.setColor(this.isRaised ? this.darkShadow : this.lightShadow);
/*  74 */       paramGraphics.drawLine(paramInt1 + 1, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 1);
/*  75 */       paramGraphics.drawLine(paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - 1, paramInt2 + 1);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/*  79 */       paramInsets.set(1, 1, 1, 1);
/*  80 */       return paramInsets;
/*     */     }
/*     */ 
/*     */     public boolean isOpaque(Component paramComponent) {
/*  84 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ButtonBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/* 117 */     protected Color focus = UIManager.getColor("activeCaptionBorder");
/* 118 */     protected Color shadow = UIManager.getColor("Button.shadow");
/* 119 */     protected Color highlight = UIManager.getColor("Button.light");
/*     */     protected Color darkShadow;
/*     */ 
/*     */     public ButtonBorder(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 123 */       this.shadow = paramColor1;
/* 124 */       this.highlight = paramColor2;
/* 125 */       this.darkShadow = paramColor3;
/* 126 */       this.focus = paramColor4;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 130 */       int i = 0;
/* 131 */       int j = 0;
/* 132 */       boolean bool1 = false;
/* 133 */       boolean bool2 = false;
/*     */ 
/* 135 */       if ((paramComponent instanceof AbstractButton)) {
/* 136 */         AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 137 */         ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 139 */         i = (localButtonModel.isArmed()) && (localButtonModel.isPressed()) ? 1 : 0;
/* 140 */         j = ((localButtonModel.isArmed()) && (i != 0)) || ((localAbstractButton.isFocusPainted()) && (localAbstractButton.hasFocus())) ? 1 : 0;
/*     */ 
/* 142 */         if ((localAbstractButton instanceof JButton)) {
/* 143 */           bool1 = ((JButton)localAbstractButton).isDefaultCapable();
/* 144 */           bool2 = ((JButton)localAbstractButton).isDefaultButton();
/*     */         }
/*     */       }
/* 147 */       int k = paramInt1 + 1;
/* 148 */       int m = paramInt2 + 1;
/* 149 */       int n = paramInt1 + paramInt3 - 2;
/* 150 */       int i1 = paramInt2 + paramInt4 - 2;
/*     */ 
/* 152 */       if (bool1) {
/* 153 */         if (bool2) {
/* 154 */           paramGraphics.setColor(this.shadow);
/* 155 */           paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 3, paramInt1 + 3, paramInt2 + paramInt4 - 4);
/* 156 */           paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 3, paramInt1 + paramInt3 - 4, paramInt2 + 3);
/*     */ 
/* 158 */           paramGraphics.setColor(this.highlight);
/* 159 */           paramGraphics.drawLine(paramInt1 + 4, paramInt2 + paramInt4 - 4, paramInt1 + paramInt3 - 4, paramInt2 + paramInt4 - 4);
/* 160 */           paramGraphics.drawLine(paramInt1 + paramInt3 - 4, paramInt2 + 3, paramInt1 + paramInt3 - 4, paramInt2 + paramInt4 - 4);
/*     */         }
/* 162 */         k += 6;
/* 163 */         m += 6;
/* 164 */         n -= 6;
/* 165 */         i1 -= 6;
/*     */       }
/*     */ 
/* 168 */       if (j != 0) {
/* 169 */         paramGraphics.setColor(this.focus);
/* 170 */         if (bool2)
/* 171 */           paramGraphics.drawRect(paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */         else {
/* 173 */           paramGraphics.drawRect(k - 1, m - 1, n - k + 2, i1 - m + 2);
/*     */         }
/*     */       }
/*     */ 
/* 177 */       paramGraphics.setColor(i != 0 ? this.shadow : this.highlight);
/* 178 */       paramGraphics.drawLine(k, m, n, m);
/* 179 */       paramGraphics.drawLine(k, m, k, i1);
/*     */ 
/* 181 */       paramGraphics.setColor(i != 0 ? this.highlight : this.shadow);
/* 182 */       paramGraphics.drawLine(n, m + 1, n, i1);
/* 183 */       paramGraphics.drawLine(k + 1, i1, n, i1);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 187 */       int i = ((paramComponent instanceof JButton)) && (((JButton)paramComponent).isDefaultCapable()) ? 8 : 2;
/* 188 */       paramInsets.set(i, i, i, i);
/* 189 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FocusBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     private Color focus;
/*     */     private Color control;
/*     */ 
/*     */     public FocusBorder(Color paramColor1, Color paramColor2)
/*     */     {
/*  95 */       this.control = paramColor1;
/*  96 */       this.focus = paramColor2;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 100 */       if (paramComponent.hasFocus()) {
/* 101 */         paramGraphics.setColor(this.focus);
/* 102 */         paramGraphics.drawRect(paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */       } else {
/* 104 */         paramGraphics.setColor(this.control);
/* 105 */         paramGraphics.drawRect(paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 110 */       paramInsets.set(1, 1, 1, 1);
/* 111 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FrameBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     JComponent jcomp;
/*     */     Color frameHighlight;
/*     */     Color frameColor;
/*     */     Color frameShadow;
/*     */     public static final int BORDER_SIZE = 5;
/*     */ 
/*     */     public FrameBorder(JComponent paramJComponent)
/*     */     {
/* 265 */       this.jcomp = paramJComponent;
/*     */     }
/*     */ 
/*     */     public void setComponent(JComponent paramJComponent)
/*     */     {
/* 271 */       this.jcomp = paramJComponent;
/*     */     }
/*     */ 
/*     */     public JComponent component()
/*     */     {
/* 278 */       return this.jcomp;
/*     */     }
/*     */ 
/*     */     protected Color getFrameHighlight() {
/* 282 */       return this.frameHighlight;
/*     */     }
/*     */ 
/*     */     protected Color getFrameColor() {
/* 286 */       return this.frameColor;
/*     */     }
/*     */ 
/*     */     protected Color getFrameShadow() {
/* 290 */       return this.frameShadow;
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 294 */       paramInsets.set(5, 5, 5, 5);
/* 295 */       return paramInsets;
/*     */     }
/*     */ 
/*     */     protected boolean drawTopBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 302 */       Rectangle localRectangle = new Rectangle(paramInt1, paramInt2, paramInt3, 5);
/* 303 */       if (!paramGraphics.getClipBounds().intersects(localRectangle)) {
/* 304 */         return false;
/*     */       }
/*     */ 
/* 307 */       int i = paramInt3 - 1;
/* 308 */       int j = 4;
/*     */ 
/* 311 */       paramGraphics.setColor(this.frameColor);
/* 312 */       paramGraphics.drawLine(paramInt1, paramInt2 + 2, i - 2, paramInt2 + 2);
/* 313 */       paramGraphics.drawLine(paramInt1, paramInt2 + 3, i - 2, paramInt2 + 3);
/* 314 */       paramGraphics.drawLine(paramInt1, paramInt2 + 4, i - 2, paramInt2 + 4);
/*     */ 
/* 317 */       paramGraphics.setColor(this.frameHighlight);
/* 318 */       paramGraphics.drawLine(paramInt1, paramInt2, i, paramInt2);
/* 319 */       paramGraphics.drawLine(paramInt1, paramInt2 + 1, i, paramInt2 + 1);
/* 320 */       paramGraphics.drawLine(paramInt1, paramInt2 + 2, paramInt1, paramInt2 + 4);
/* 321 */       paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 2, paramInt1 + 1, paramInt2 + 4);
/*     */ 
/* 324 */       paramGraphics.setColor(this.frameShadow);
/* 325 */       paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 4, i - 4, paramInt2 + 4);
/* 326 */       paramGraphics.drawLine(i, paramInt2 + 1, i, j);
/* 327 */       paramGraphics.drawLine(i - 1, paramInt2 + 2, i - 1, j);
/*     */ 
/* 329 */       return true;
/*     */     }
/*     */ 
/*     */     protected boolean drawLeftBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 336 */       Rectangle localRectangle = new Rectangle(0, 0, getBorderInsets(paramComponent).left, paramInt4);
/*     */ 
/* 338 */       if (!paramGraphics.getClipBounds().intersects(localRectangle)) {
/* 339 */         return false;
/*     */       }
/*     */ 
/* 342 */       int i = 5;
/*     */ 
/* 344 */       paramGraphics.setColor(this.frameHighlight);
/* 345 */       paramGraphics.drawLine(paramInt1, i, paramInt1, paramInt4 - 1);
/* 346 */       paramGraphics.drawLine(paramInt1 + 1, i, paramInt1 + 1, paramInt4 - 2);
/*     */ 
/* 348 */       paramGraphics.setColor(this.frameColor);
/* 349 */       paramGraphics.fillRect(paramInt1 + 2, i, paramInt1 + 2, paramInt4 - 3);
/*     */ 
/* 351 */       paramGraphics.setColor(this.frameShadow);
/* 352 */       paramGraphics.drawLine(paramInt1 + 4, i, paramInt1 + 4, paramInt4 - 5);
/*     */ 
/* 354 */       return true;
/*     */     }
/*     */ 
/*     */     protected boolean drawRightBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 361 */       Rectangle localRectangle = new Rectangle(paramInt3 - getBorderInsets(paramComponent).right, 0, getBorderInsets(paramComponent).right, paramInt4);
/*     */ 
/* 364 */       if (!paramGraphics.getClipBounds().intersects(localRectangle)) {
/* 365 */         return false;
/*     */       }
/*     */ 
/* 368 */       int i = paramInt3 - getBorderInsets(paramComponent).right;
/* 369 */       int j = 5;
/*     */ 
/* 371 */       paramGraphics.setColor(this.frameColor);
/* 372 */       paramGraphics.fillRect(i + 1, j, 2, paramInt4 - 1);
/*     */ 
/* 374 */       paramGraphics.setColor(this.frameShadow);
/* 375 */       paramGraphics.fillRect(i + 3, j, 2, paramInt4 - 1);
/*     */ 
/* 377 */       paramGraphics.setColor(this.frameHighlight);
/* 378 */       paramGraphics.drawLine(i, j, i, paramInt4 - 1);
/*     */ 
/* 380 */       return true;
/*     */     }
/*     */ 
/*     */     protected boolean drawBottomBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 390 */       Rectangle localRectangle = new Rectangle(0, paramInt4 - getBorderInsets(paramComponent).bottom, paramInt3, getBorderInsets(paramComponent).bottom);
/*     */ 
/* 392 */       if (!paramGraphics.getClipBounds().intersects(localRectangle)) {
/* 393 */         return false;
/*     */       }
/*     */ 
/* 396 */       int i = paramInt4 - getBorderInsets(paramComponent).bottom;
/*     */ 
/* 398 */       paramGraphics.setColor(this.frameShadow);
/* 399 */       paramGraphics.drawLine(paramInt1 + 1, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 400 */       paramGraphics.drawLine(paramInt1 + 2, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/*     */ 
/* 402 */       paramGraphics.setColor(this.frameColor);
/* 403 */       paramGraphics.fillRect(paramInt1 + 2, i + 1, paramInt3 - 4, 2);
/*     */ 
/* 405 */       paramGraphics.setColor(this.frameHighlight);
/* 406 */       paramGraphics.drawLine(paramInt1 + 5, i, paramInt3 - 5, i);
/*     */ 
/* 408 */       return true;
/*     */     }
/*     */ 
/*     */     protected boolean isActiveFrame()
/*     */     {
/* 413 */       return this.jcomp.hasFocus();
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 422 */       if (isActiveFrame())
/* 423 */         this.frameColor = UIManager.getColor("activeCaptionBorder");
/*     */       else {
/* 425 */         this.frameColor = UIManager.getColor("inactiveCaptionBorder");
/*     */       }
/* 427 */       this.frameHighlight = this.frameColor.brighter();
/* 428 */       this.frameShadow = this.frameColor.darker().darker();
/*     */ 
/* 430 */       drawTopBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/* 431 */       drawLeftBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/* 432 */       drawRightBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/* 433 */       drawBottomBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class InternalFrameBorder extends MotifBorders.FrameBorder
/*     */   {
/*     */     JInternalFrame frame;
/*     */     public static final int CORNER_SIZE = 24;
/*     */ 
/*     */     public InternalFrameBorder(JInternalFrame paramJInternalFrame)
/*     */     {
/* 448 */       super();
/* 449 */       this.frame = paramJInternalFrame;
/*     */     }
/*     */ 
/*     */     public void setFrame(JInternalFrame paramJInternalFrame)
/*     */     {
/* 455 */       this.frame = paramJInternalFrame;
/*     */     }
/*     */ 
/*     */     public JInternalFrame frame()
/*     */     {
/* 462 */       return this.frame;
/*     */     }
/*     */ 
/*     */     public int resizePartWidth()
/*     */     {
/* 474 */       if (!this.frame.isResizable()) {
/* 475 */         return 0;
/*     */       }
/* 477 */       return 5;
/*     */     }
/*     */ 
/*     */     protected boolean drawTopBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 484 */       if ((super.drawTopBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4)) && (this.frame.isResizable()))
/*     */       {
/* 486 */         paramGraphics.setColor(getFrameShadow());
/* 487 */         paramGraphics.drawLine(23, paramInt2 + 1, 23, paramInt2 + 4);
/* 488 */         paramGraphics.drawLine(paramInt3 - 24 - 1, paramInt2 + 1, paramInt3 - 24 - 1, paramInt2 + 4);
/*     */ 
/* 491 */         paramGraphics.setColor(getFrameHighlight());
/* 492 */         paramGraphics.drawLine(24, paramInt2, 24, paramInt2 + 4);
/* 493 */         paramGraphics.drawLine(paramInt3 - 24, paramInt2, paramInt3 - 24, paramInt2 + 4);
/* 494 */         return true;
/*     */       }
/* 496 */       return false;
/*     */     }
/*     */ 
/*     */     protected boolean drawLeftBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 503 */       if ((super.drawLeftBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4)) && (this.frame.isResizable()))
/*     */       {
/* 505 */         paramGraphics.setColor(getFrameHighlight());
/* 506 */         int i = paramInt2 + 24;
/* 507 */         paramGraphics.drawLine(paramInt1, i, paramInt1 + 4, i);
/* 508 */         int j = paramInt4 - 24;
/* 509 */         paramGraphics.drawLine(paramInt1 + 1, j, paramInt1 + 5, j);
/* 510 */         paramGraphics.setColor(getFrameShadow());
/* 511 */         paramGraphics.drawLine(paramInt1 + 1, i - 1, paramInt1 + 5, i - 1);
/* 512 */         paramGraphics.drawLine(paramInt1 + 1, j - 1, paramInt1 + 5, j - 1);
/* 513 */         return true;
/*     */       }
/* 515 */       return false;
/*     */     }
/*     */ 
/*     */     protected boolean drawRightBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 522 */       if ((super.drawRightBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4)) && (this.frame.isResizable()))
/*     */       {
/* 524 */         int i = paramInt3 - getBorderInsets(paramComponent).right;
/* 525 */         paramGraphics.setColor(getFrameHighlight());
/* 526 */         int j = paramInt2 + 24;
/* 527 */         paramGraphics.drawLine(i, j, paramInt3 - 2, j);
/* 528 */         int k = paramInt4 - 24;
/* 529 */         paramGraphics.drawLine(i + 1, k, i + 3, k);
/* 530 */         paramGraphics.setColor(getFrameShadow());
/* 531 */         paramGraphics.drawLine(i + 1, j - 1, paramInt3 - 2, j - 1);
/* 532 */         paramGraphics.drawLine(i + 1, k - 1, i + 3, k - 1);
/* 533 */         return true;
/*     */       }
/* 535 */       return false;
/*     */     }
/*     */ 
/*     */     protected boolean drawBottomBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 542 */       if ((super.drawBottomBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4)) && (this.frame.isResizable()))
/*     */       {
/* 544 */         int i = paramInt4 - getBorderInsets(paramComponent).bottom;
/*     */ 
/* 546 */         paramGraphics.setColor(getFrameShadow());
/* 547 */         paramGraphics.drawLine(23, i + 1, 23, paramInt4 - 1);
/*     */ 
/* 549 */         paramGraphics.drawLine(paramInt3 - 24, i + 1, paramInt3 - 24, paramInt4 - 1);
/*     */ 
/* 552 */         paramGraphics.setColor(getFrameHighlight());
/* 553 */         paramGraphics.drawLine(24, i, 24, paramInt4 - 2);
/* 554 */         paramGraphics.drawLine(paramInt3 - 24 + 1, i, paramInt3 - 24 + 1, paramInt4 - 2);
/*     */ 
/* 556 */         return true;
/*     */       }
/* 558 */       return false;
/*     */     }
/*     */ 
/*     */     protected boolean isActiveFrame()
/*     */     {
/* 563 */       return this.frame.isSelected();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MenuBarBorder extends MotifBorders.ButtonBorder
/*     */   {
/*     */     public MenuBarBorder(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 230 */       super(paramColor2, paramColor3, paramColor4);
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 234 */       if (!(paramComponent instanceof JMenuBar)) {
/* 235 */         return;
/*     */       }
/* 237 */       JMenuBar localJMenuBar = (JMenuBar)paramComponent;
/* 238 */       if (localJMenuBar.isBorderPainted() == true)
/*     */       {
/* 240 */         Dimension localDimension = localJMenuBar.getSize();
/* 241 */         MotifBorders.drawBezel(paramGraphics, paramInt1, paramInt2, localDimension.width, localDimension.height, false, false, this.shadow, this.highlight, this.darkShadow, this.focus);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 247 */       paramInsets.set(6, 6, 6, 6);
/* 248 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MotifPopupMenuBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     protected Font font;
/*     */     protected Color background;
/*     */     protected Color foreground;
/*     */     protected Color shadowColor;
/*     */     protected Color highlightColor;
/*     */     protected static final int TEXT_SPACING = 2;
/*     */     protected static final int GROOVE_HEIGHT = 2;
/*     */ 
/*     */     public MotifPopupMenuBorder(Font paramFont, Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 644 */       this.font = paramFont;
/* 645 */       this.background = paramColor1;
/* 646 */       this.foreground = paramColor2;
/* 647 */       this.shadowColor = paramColor3;
/* 648 */       this.highlightColor = paramColor4;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 662 */       if (!(paramComponent instanceof JPopupMenu)) {
/* 663 */         return;
/*     */       }
/*     */ 
/* 666 */       Font localFont = paramGraphics.getFont();
/* 667 */       Color localColor = paramGraphics.getColor();
/* 668 */       JPopupMenu localJPopupMenu = (JPopupMenu)paramComponent;
/*     */ 
/* 670 */       String str = localJPopupMenu.getLabel();
/* 671 */       if (str == null) {
/* 672 */         return;
/*     */       }
/*     */ 
/* 675 */       paramGraphics.setFont(this.font);
/*     */ 
/* 677 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(localJPopupMenu, paramGraphics, this.font);
/* 678 */       int i = localFontMetrics.getHeight();
/* 679 */       int j = localFontMetrics.getDescent();
/* 680 */       int k = localFontMetrics.getAscent();
/* 681 */       Point localPoint = new Point();
/* 682 */       int m = SwingUtilities2.stringWidth(localJPopupMenu, localFontMetrics, str);
/*     */ 
/* 685 */       localPoint.y = (paramInt2 + k + 2);
/* 686 */       localPoint.x = (paramInt1 + (paramInt3 - m) / 2);
/*     */ 
/* 688 */       paramGraphics.setColor(this.background);
/* 689 */       paramGraphics.fillRect(localPoint.x - 2, localPoint.y - (i - j), m + 4, i - j);
/*     */ 
/* 691 */       paramGraphics.setColor(this.foreground);
/* 692 */       SwingUtilities2.drawString(localJPopupMenu, paramGraphics, str, localPoint.x, localPoint.y);
/*     */ 
/* 694 */       MotifGraphicsUtils.drawGroove(paramGraphics, paramInt1, localPoint.y + 2, paramInt3, 2, this.shadowColor, this.highlightColor);
/*     */ 
/* 698 */       paramGraphics.setFont(localFont);
/* 699 */       paramGraphics.setColor(localColor);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 708 */       if (!(paramComponent instanceof JPopupMenu)) {
/* 709 */         return paramInsets;
/*     */       }
/*     */ 
/* 712 */       int i = 0;
/* 713 */       int j = 16;
/*     */ 
/* 715 */       String str = ((JPopupMenu)paramComponent).getLabel();
/* 716 */       if (str == null) {
/* 717 */         paramInsets.left = (paramInsets.top = paramInsets.right = paramInsets.bottom = 0);
/* 718 */         return paramInsets;
/*     */       }
/*     */ 
/* 721 */       FontMetrics localFontMetrics = paramComponent.getFontMetrics(this.font);
/*     */ 
/* 723 */       if (localFontMetrics != null) {
/* 724 */         i = localFontMetrics.getDescent();
/* 725 */         j = localFontMetrics.getAscent();
/*     */       }
/*     */ 
/* 728 */       paramInsets.top += j + i + 2 + 2;
/* 729 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ToggleButtonBorder extends MotifBorders.ButtonBorder
/*     */   {
/*     */     public ToggleButtonBorder(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 197 */       super(paramColor2, paramColor3, paramColor4);
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 202 */       if ((paramComponent instanceof AbstractButton)) {
/* 203 */         AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 204 */         ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 206 */         if (((localButtonModel.isArmed()) && (localButtonModel.isPressed())) || (localButtonModel.isSelected())) {
/* 207 */           MotifBorders.drawBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, (localButtonModel.isPressed()) || (localButtonModel.isSelected()), (localAbstractButton.isFocusPainted()) && (localAbstractButton.hasFocus()), this.shadow, this.highlight, this.darkShadow, this.focus);
/*     */         }
/*     */         else
/*     */         {
/* 211 */           MotifBorders.drawBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, false, (localAbstractButton.isFocusPainted()) && (localAbstractButton.hasFocus()), this.shadow, this.highlight, this.darkShadow, this.focus);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 216 */         MotifBorders.drawBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, false, false, this.shadow, this.highlight, this.darkShadow, this.focus);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 222 */       paramInsets.set(2, 2, 3, 3);
/* 223 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifBorders
 * JD-Core Version:    0.6.2
 */
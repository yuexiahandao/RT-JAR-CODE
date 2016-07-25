/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.View;
/*     */ import sun.swing.MenuItemLayoutHelper;
/*     */ import sun.swing.MenuItemLayoutHelper.LayoutResult;
/*     */ import sun.swing.MenuItemLayoutHelper.RectSize;
/*     */ import sun.swing.SwingUtilities2;
/*     */ import sun.swing.plaf.synth.SynthIcon;
/*     */ 
/*     */ public class SynthGraphicsUtils
/*     */ {
/*  45 */   private Rectangle paintIconR = new Rectangle();
/*  46 */   private Rectangle paintTextR = new Rectangle();
/*  47 */   private Rectangle paintViewR = new Rectangle();
/*  48 */   private Insets paintInsets = new Insets(0, 0, 0, 0);
/*     */ 
/*  52 */   private Rectangle iconR = new Rectangle();
/*  53 */   private Rectangle textR = new Rectangle();
/*  54 */   private Rectangle viewR = new Rectangle();
/*  55 */   private Insets viewSizingInsets = new Insets(0, 0, 0, 0);
/*     */ 
/*     */   public void drawLine(SynthContext paramSynthContext, Object paramObject, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  77 */     paramGraphics.drawLine(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void drawLine(SynthContext paramSynthContext, Object paramObject1, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Object paramObject2)
/*     */   {
/* 102 */     if ("dashed".equals(paramObject2))
/*     */     {
/*     */       int i;
/* 104 */       if (paramInt1 == paramInt3) {
/* 105 */         paramInt2 += paramInt2 % 2;
/*     */ 
/* 107 */         for (i = paramInt2; i <= paramInt4; i += 2) {
/* 108 */           paramGraphics.drawLine(paramInt1, i, paramInt3, i);
/*     */         }
/*     */       }
/* 111 */       else if (paramInt2 == paramInt4) {
/* 112 */         paramInt1 += paramInt1 % 2;
/*     */ 
/* 114 */         for (i = paramInt1; i <= paramInt3; i += 2)
/* 115 */           paramGraphics.drawLine(i, paramInt2, i, paramInt4);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 120 */       drawLine(paramSynthContext, paramObject1, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String layoutText(SynthContext paramSynthContext, FontMetrics paramFontMetrics, String paramString, Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3, int paramInt5)
/*     */   {
/* 146 */     if ((paramIcon instanceof SynthIcon)) {
/* 147 */       SynthIconWrapper localSynthIconWrapper = SynthIconWrapper.get((SynthIcon)paramIcon, paramSynthContext);
/*     */ 
/* 149 */       String str = SwingUtilities.layoutCompoundLabel(paramSynthContext.getComponent(), paramFontMetrics, paramString, localSynthIconWrapper, paramInt2, paramInt1, paramInt4, paramInt3, paramRectangle1, paramRectangle2, paramRectangle3, paramInt5);
/*     */ 
/* 153 */       SynthIconWrapper.release(localSynthIconWrapper);
/* 154 */       return str;
/*     */     }
/* 156 */     return SwingUtilities.layoutCompoundLabel(paramSynthContext.getComponent(), paramFontMetrics, paramString, paramIcon, paramInt2, paramInt1, paramInt4, paramInt3, paramRectangle1, paramRectangle2, paramRectangle3, paramInt5);
/*     */   }
/*     */ 
/*     */   public int computeStringWidth(SynthContext paramSynthContext, Font paramFont, FontMetrics paramFontMetrics, String paramString)
/*     */   {
/* 172 */     return SwingUtilities2.stringWidth(paramSynthContext.getComponent(), paramFontMetrics, paramString);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(SynthContext paramSynthContext, Font paramFont, String paramString, Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 194 */     JComponent localJComponent = paramSynthContext.getComponent();
/* 195 */     Dimension localDimension = getPreferredSize(paramSynthContext, paramFont, paramString, paramIcon, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 198 */     View localView = (View)localJComponent.getClientProperty("html");
/*     */ 
/* 200 */     if (localView != null)
/*     */     {
/*     */       Dimension tmp48_46 = localDimension; tmp48_46.width = ((int)(tmp48_46.width - (localView.getPreferredSpan(0) - localView.getMinimumSpan(0))));
/*     */     }
/*     */ 
/* 204 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(SynthContext paramSynthContext, Font paramFont, String paramString, Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 225 */     JComponent localJComponent = paramSynthContext.getComponent();
/* 226 */     Dimension localDimension = getPreferredSize(paramSynthContext, paramFont, paramString, paramIcon, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 229 */     View localView = (View)localJComponent.getClientProperty("html");
/*     */ 
/* 231 */     if (localView != null)
/*     */     {
/*     */       Dimension tmp48_46 = localDimension; tmp48_46.width = ((int)(tmp48_46.width + (localView.getMaximumSpan(0) - localView.getPreferredSpan(0))));
/*     */     }
/*     */ 
/* 235 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getMaximumCharHeight(SynthContext paramSynthContext)
/*     */   {
/* 247 */     FontMetrics localFontMetrics = paramSynthContext.getComponent().getFontMetrics(paramSynthContext.getStyle().getFont(paramSynthContext));
/*     */ 
/* 249 */     return localFontMetrics.getAscent() + localFontMetrics.getDescent();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(SynthContext paramSynthContext, Font paramFont, String paramString, Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 270 */     JComponent localJComponent = paramSynthContext.getComponent();
/* 271 */     Insets localInsets = localJComponent.getInsets(this.viewSizingInsets);
/* 272 */     int i = localInsets.left + localInsets.right;
/* 273 */     int j = localInsets.top + localInsets.bottom;
/*     */ 
/* 275 */     if ((paramIcon == null) && ((paramString == null) || (paramFont == null))) {
/* 276 */       return new Dimension(i, j);
/*     */     }
/* 278 */     if ((paramString == null) || ((paramIcon != null) && (paramFont == null))) {
/* 279 */       return new Dimension(SynthIcon.getIconWidth(paramIcon, paramSynthContext) + i, SynthIcon.getIconHeight(paramIcon, paramSynthContext) + j);
/*     */     }
/*     */ 
/* 283 */     FontMetrics localFontMetrics = localJComponent.getFontMetrics(paramFont);
/*     */ 
/* 285 */     this.iconR.x = (this.iconR.y = this.iconR.width = this.iconR.height = 0);
/* 286 */     this.textR.x = (this.textR.y = this.textR.width = this.textR.height = 0);
/* 287 */     this.viewR.x = i;
/* 288 */     this.viewR.y = j;
/* 289 */     this.viewR.width = (this.viewR.height = 32767);
/*     */ 
/* 291 */     layoutText(paramSynthContext, localFontMetrics, paramString, paramIcon, paramInt1, paramInt2, paramInt3, paramInt4, this.viewR, this.iconR, this.textR, paramInt5);
/*     */ 
/* 294 */     int k = Math.min(this.iconR.x, this.textR.x);
/* 295 */     int m = Math.max(this.iconR.x + this.iconR.width, this.textR.x + this.textR.width);
/* 296 */     int n = Math.min(this.iconR.y, this.textR.y);
/* 297 */     int i1 = Math.max(this.iconR.y + this.iconR.height, this.textR.y + this.textR.height);
/* 298 */     Dimension localDimension = new Dimension(m - k, i1 - n);
/*     */ 
/* 300 */     localDimension.width += i;
/* 301 */     localDimension.height += j;
/* 302 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void paintText(SynthContext paramSynthContext, Graphics paramGraphics, String paramString, Rectangle paramRectangle, int paramInt)
/*     */   {
/* 319 */     paintText(paramSynthContext, paramGraphics, paramString, paramRectangle.x, paramRectangle.y, paramInt);
/*     */   }
/*     */ 
/*     */   public void paintText(SynthContext paramSynthContext, Graphics paramGraphics, String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 336 */     if (paramString != null) {
/* 337 */       JComponent localJComponent = paramSynthContext.getComponent();
/* 338 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(localJComponent, paramGraphics);
/* 339 */       paramInt2 += localFontMetrics.getAscent();
/* 340 */       SwingUtilities2.drawStringUnderlineCharAt(localJComponent, paramGraphics, paramString, paramInt3, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintText(SynthContext paramSynthContext, Graphics paramGraphics, String paramString, Icon paramIcon, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
/*     */   {
/* 366 */     if ((paramIcon == null) && (paramString == null)) {
/* 367 */       return;
/*     */     }
/* 369 */     JComponent localJComponent = paramSynthContext.getComponent();
/* 370 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(localJComponent, paramGraphics);
/* 371 */     Insets localInsets = SynthLookAndFeel.getPaintingInsets(paramSynthContext, this.paintInsets);
/*     */ 
/* 373 */     this.paintViewR.x = localInsets.left;
/* 374 */     this.paintViewR.y = localInsets.top;
/* 375 */     this.paintViewR.width = (localJComponent.getWidth() - (localInsets.left + localInsets.right));
/* 376 */     this.paintViewR.height = (localJComponent.getHeight() - (localInsets.top + localInsets.bottom));
/*     */ 
/* 378 */     this.paintIconR.x = (this.paintIconR.y = this.paintIconR.width = this.paintIconR.height = 0);
/* 379 */     this.paintTextR.x = (this.paintTextR.y = this.paintTextR.width = this.paintTextR.height = 0);
/*     */ 
/* 381 */     String str = layoutText(paramSynthContext, localFontMetrics, paramString, paramIcon, paramInt1, paramInt2, paramInt3, paramInt4, this.paintViewR, this.paintIconR, this.paintTextR, paramInt5);
/*     */     Object localObject;
/* 386 */     if (paramIcon != null) {
/* 387 */       localObject = paramGraphics.getColor();
/*     */ 
/* 389 */       if ((paramSynthContext.getStyle().getBoolean(paramSynthContext, "TableHeader.alignSorterArrow", false)) && ("TableHeader.renderer".equals(localJComponent.getName())))
/*     */       {
/* 391 */         this.paintIconR.x = (this.paintViewR.width - this.paintIconR.width);
/*     */       }
/* 393 */       else this.paintIconR.x += paramInt7;
/*     */ 
/* 395 */       this.paintIconR.y += paramInt7;
/* 396 */       SynthIcon.paintIcon(paramIcon, paramSynthContext, paramGraphics, this.paintIconR.x, this.paintIconR.y, this.paintIconR.width, this.paintIconR.height);
/*     */ 
/* 398 */       paramGraphics.setColor((Color)localObject);
/*     */     }
/*     */ 
/* 401 */     if (paramString != null) {
/* 402 */       localObject = (View)localJComponent.getClientProperty("html");
/*     */ 
/* 404 */       if (localObject != null) {
/* 405 */         ((View)localObject).paint(paramGraphics, this.paintTextR);
/*     */       } else {
/* 407 */         this.paintTextR.x += paramInt7;
/* 408 */         this.paintTextR.y += paramInt7;
/*     */ 
/* 410 */         paintText(paramSynthContext, paramGraphics, str, this.paintTextR, paramInt6);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static Dimension getPreferredMenuItemSize(SynthContext paramSynthContext1, SynthContext paramSynthContext2, JComponent paramJComponent, Icon paramIcon1, Icon paramIcon2, int paramInt, String paramString1, boolean paramBoolean, String paramString2)
/*     */   {
/* 433 */     JMenuItem localJMenuItem = (JMenuItem)paramJComponent;
/* 434 */     SynthMenuItemLayoutHelper localSynthMenuItemLayoutHelper = new SynthMenuItemLayoutHelper(paramSynthContext1, paramSynthContext2, localJMenuItem, paramIcon1, paramIcon2, MenuItemLayoutHelper.createMaxRect(), paramInt, paramString1, SynthLookAndFeel.isLeftToRight(localJMenuItem), paramBoolean, paramString2);
/*     */ 
/* 440 */     Dimension localDimension = new Dimension();
/*     */ 
/* 443 */     int i = localSynthMenuItemLayoutHelper.getGap();
/* 444 */     localDimension.width = 0;
/* 445 */     MenuItemLayoutHelper.addMaxWidth(localSynthMenuItemLayoutHelper.getCheckSize(), i, localDimension);
/* 446 */     MenuItemLayoutHelper.addMaxWidth(localSynthMenuItemLayoutHelper.getLabelSize(), i, localDimension);
/* 447 */     MenuItemLayoutHelper.addWidth(localSynthMenuItemLayoutHelper.getMaxAccOrArrowWidth(), 5 * i, localDimension);
/*     */ 
/* 449 */     localDimension.width -= i;
/*     */ 
/* 452 */     localDimension.height = MenuItemLayoutHelper.max(new int[] { localSynthMenuItemLayoutHelper.getCheckSize().getHeight(), localSynthMenuItemLayoutHelper.getLabelSize().getHeight(), localSynthMenuItemLayoutHelper.getAccSize().getHeight(), localSynthMenuItemLayoutHelper.getArrowSize().getHeight() });
/*     */ 
/* 457 */     Insets localInsets = localSynthMenuItemLayoutHelper.getMenuItem().getInsets();
/* 458 */     if (localInsets != null) {
/* 459 */       localDimension.width += localInsets.left + localInsets.right;
/* 460 */       localDimension.height += localInsets.top + localInsets.bottom;
/*     */     }
/*     */ 
/* 465 */     if (localDimension.width % 2 == 0) {
/* 466 */       localDimension.width += 1;
/*     */     }
/*     */ 
/* 471 */     if (localDimension.height % 2 == 0) {
/* 472 */       localDimension.height += 1;
/*     */     }
/*     */ 
/* 475 */     return localDimension;
/*     */   }
/*     */ 
/*     */   static void applyInsets(Rectangle paramRectangle, Insets paramInsets, boolean paramBoolean) {
/* 479 */     if (paramInsets != null) {
/* 480 */       paramRectangle.x += (paramBoolean ? paramInsets.left : paramInsets.right);
/* 481 */       paramRectangle.y += paramInsets.top;
/* 482 */       paramRectangle.width -= (paramBoolean ? paramInsets.right : paramInsets.left) + paramRectangle.x;
/* 483 */       paramRectangle.height -= paramInsets.bottom + paramRectangle.y;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void paint(SynthContext paramSynthContext1, SynthContext paramSynthContext2, Graphics paramGraphics, Icon paramIcon1, Icon paramIcon2, String paramString1, int paramInt, String paramString2)
/*     */   {
/* 490 */     JMenuItem localJMenuItem = (JMenuItem)paramSynthContext1.getComponent();
/* 491 */     SynthStyle localSynthStyle = paramSynthContext1.getStyle();
/* 492 */     paramGraphics.setFont(localSynthStyle.getFont(paramSynthContext1));
/*     */ 
/* 494 */     Rectangle localRectangle = new Rectangle(0, 0, localJMenuItem.getWidth(), localJMenuItem.getHeight());
/* 495 */     boolean bool = SynthLookAndFeel.isLeftToRight(localJMenuItem);
/* 496 */     applyInsets(localRectangle, localJMenuItem.getInsets(), bool);
/*     */ 
/* 498 */     SynthMenuItemLayoutHelper localSynthMenuItemLayoutHelper = new SynthMenuItemLayoutHelper(paramSynthContext1, paramSynthContext2, localJMenuItem, paramIcon1, paramIcon2, localRectangle, paramInt, paramString1, bool, MenuItemLayoutHelper.useCheckAndArrow(localJMenuItem), paramString2);
/*     */ 
/* 502 */     MenuItemLayoutHelper.LayoutResult localLayoutResult = localSynthMenuItemLayoutHelper.layoutMenuItem();
/*     */ 
/* 504 */     paintMenuItem(paramGraphics, localSynthMenuItemLayoutHelper, localLayoutResult);
/*     */   }
/*     */ 
/*     */   static void paintMenuItem(Graphics paramGraphics, SynthMenuItemLayoutHelper paramSynthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 510 */     Font localFont = paramGraphics.getFont();
/* 511 */     Color localColor = paramGraphics.getColor();
/*     */ 
/* 513 */     paintCheckIcon(paramGraphics, paramSynthMenuItemLayoutHelper, paramLayoutResult);
/* 514 */     paintIcon(paramGraphics, paramSynthMenuItemLayoutHelper, paramLayoutResult);
/* 515 */     paintText(paramGraphics, paramSynthMenuItemLayoutHelper, paramLayoutResult);
/* 516 */     paintAccText(paramGraphics, paramSynthMenuItemLayoutHelper, paramLayoutResult);
/* 517 */     paintArrowIcon(paramGraphics, paramSynthMenuItemLayoutHelper, paramLayoutResult);
/*     */ 
/* 520 */     paramGraphics.setColor(localColor);
/* 521 */     paramGraphics.setFont(localFont);
/*     */   }
/*     */ 
/*     */   static void paintBackground(Graphics paramGraphics, SynthMenuItemLayoutHelper paramSynthMenuItemLayoutHelper) {
/* 525 */     paintBackground(paramSynthMenuItemLayoutHelper.getContext(), paramGraphics, paramSynthMenuItemLayoutHelper.getMenuItem());
/*     */   }
/*     */ 
/*     */   static void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent) {
/* 529 */     paramSynthContext.getPainter().paintMenuItemBackground(paramSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */   }
/*     */ 
/*     */   static void paintIcon(Graphics paramGraphics, SynthMenuItemLayoutHelper paramSynthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 535 */     if (paramSynthMenuItemLayoutHelper.getIcon() != null)
/*     */     {
/* 537 */       JMenuItem localJMenuItem = paramSynthMenuItemLayoutHelper.getMenuItem();
/* 538 */       ButtonModel localButtonModel = localJMenuItem.getModel();
/*     */       Icon localIcon;
/* 539 */       if (!localButtonModel.isEnabled()) {
/* 540 */         localIcon = localJMenuItem.getDisabledIcon();
/* 541 */       } else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 542 */         localIcon = localJMenuItem.getPressedIcon();
/* 543 */         if (localIcon == null)
/*     */         {
/* 545 */           localIcon = localJMenuItem.getIcon();
/*     */         }
/*     */       } else {
/* 548 */         localIcon = localJMenuItem.getIcon();
/*     */       }
/*     */ 
/* 551 */       if (localIcon != null) {
/* 552 */         Rectangle localRectangle = paramLayoutResult.getIconRect();
/* 553 */         SynthIcon.paintIcon(localIcon, paramSynthMenuItemLayoutHelper.getContext(), paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void paintCheckIcon(Graphics paramGraphics, SynthMenuItemLayoutHelper paramSynthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 561 */     if (paramSynthMenuItemLayoutHelper.getCheckIcon() != null) {
/* 562 */       Rectangle localRectangle = paramLayoutResult.getCheckRect();
/* 563 */       SynthIcon.paintIcon(paramSynthMenuItemLayoutHelper.getCheckIcon(), paramSynthMenuItemLayoutHelper.getContext(), paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void paintAccText(Graphics paramGraphics, SynthMenuItemLayoutHelper paramSynthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 570 */     String str = paramSynthMenuItemLayoutHelper.getAccText();
/* 571 */     if ((str != null) && (!str.equals(""))) {
/* 572 */       paramGraphics.setColor(paramSynthMenuItemLayoutHelper.getAccStyle().getColor(paramSynthMenuItemLayoutHelper.getAccContext(), ColorType.TEXT_FOREGROUND));
/*     */ 
/* 574 */       paramGraphics.setFont(paramSynthMenuItemLayoutHelper.getAccStyle().getFont(paramSynthMenuItemLayoutHelper.getAccContext()));
/* 575 */       paramSynthMenuItemLayoutHelper.getAccGraphicsUtils().paintText(paramSynthMenuItemLayoutHelper.getAccContext(), paramGraphics, str, paramLayoutResult.getAccRect().x, paramLayoutResult.getAccRect().y, -1);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void paintText(Graphics paramGraphics, SynthMenuItemLayoutHelper paramSynthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 582 */     if (!paramSynthMenuItemLayoutHelper.getText().equals(""))
/* 583 */       if (paramSynthMenuItemLayoutHelper.getHtmlView() != null)
/*     */       {
/* 585 */         paramSynthMenuItemLayoutHelper.getHtmlView().paint(paramGraphics, paramLayoutResult.getTextRect());
/*     */       }
/*     */       else {
/* 588 */         paramGraphics.setColor(paramSynthMenuItemLayoutHelper.getStyle().getColor(paramSynthMenuItemLayoutHelper.getContext(), ColorType.TEXT_FOREGROUND));
/*     */ 
/* 590 */         paramGraphics.setFont(paramSynthMenuItemLayoutHelper.getStyle().getFont(paramSynthMenuItemLayoutHelper.getContext()));
/* 591 */         paramSynthMenuItemLayoutHelper.getGraphicsUtils().paintText(paramSynthMenuItemLayoutHelper.getContext(), paramGraphics, paramSynthMenuItemLayoutHelper.getText(), paramLayoutResult.getTextRect().x, paramLayoutResult.getTextRect().y, paramSynthMenuItemLayoutHelper.getMenuItem().getDisplayedMnemonicIndex());
/*     */       }
/*     */   }
/*     */ 
/*     */   static void paintArrowIcon(Graphics paramGraphics, SynthMenuItemLayoutHelper paramSynthMenuItemLayoutHelper, MenuItemLayoutHelper.LayoutResult paramLayoutResult)
/*     */   {
/* 600 */     if (paramSynthMenuItemLayoutHelper.getArrowIcon() != null) {
/* 601 */       Rectangle localRectangle = paramLayoutResult.getArrowRect();
/* 602 */       SynthIcon.paintIcon(paramSynthMenuItemLayoutHelper.getArrowIcon(), paramSynthMenuItemLayoutHelper.getContext(), paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SynthIconWrapper
/*     */     implements Icon
/*     */   {
/* 612 */     private static final List<SynthIconWrapper> CACHE = new ArrayList(1);
/*     */     private SynthIcon synthIcon;
/*     */     private SynthContext context;
/*     */ 
/*     */     static SynthIconWrapper get(SynthIcon paramSynthIcon, SynthContext paramSynthContext)
/*     */     {
/* 618 */       synchronized (CACHE) {
/* 619 */         int i = CACHE.size();
/* 620 */         if (i > 0) {
/* 621 */           SynthIconWrapper localSynthIconWrapper = (SynthIconWrapper)CACHE.remove(i - 1);
/* 622 */           localSynthIconWrapper.reset(paramSynthIcon, paramSynthContext);
/* 623 */           return localSynthIconWrapper;
/*     */         }
/*     */       }
/* 626 */       return new SynthIconWrapper(paramSynthIcon, paramSynthContext);
/*     */     }
/*     */ 
/*     */     static void release(SynthIconWrapper paramSynthIconWrapper) {
/* 630 */       paramSynthIconWrapper.reset(null, null);
/* 631 */       synchronized (CACHE) {
/* 632 */         CACHE.add(paramSynthIconWrapper);
/*     */       }
/*     */     }
/*     */ 
/*     */     SynthIconWrapper(SynthIcon paramSynthIcon, SynthContext paramSynthContext) {
/* 637 */       reset(paramSynthIcon, paramSynthContext);
/*     */     }
/*     */ 
/*     */     void reset(SynthIcon paramSynthIcon, SynthContext paramSynthContext) {
/* 641 */       this.synthIcon = paramSynthIcon;
/* 642 */       this.context = paramSynthContext;
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 650 */       return this.synthIcon.getIconWidth(this.context);
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 654 */       return this.synthIcon.getIconHeight(this.context);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthGraphicsUtils
 * JD-Core Version:    0.6.2
 */
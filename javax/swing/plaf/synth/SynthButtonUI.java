/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicButtonUI;
/*     */ import javax.swing.plaf.basic.BasicHTML;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public class SynthButtonUI extends BasicButtonUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  54 */     return new SynthButtonUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  62 */     updateStyle(paramAbstractButton);
/*     */ 
/*  64 */     LookAndFeel.installProperty(paramAbstractButton, "rolloverEnabled", Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   protected void installListeners(AbstractButton paramAbstractButton)
/*     */   {
/*  72 */     super.installListeners(paramAbstractButton);
/*  73 */     paramAbstractButton.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   void updateStyle(AbstractButton paramAbstractButton) {
/*  77 */     SynthContext localSynthContext = getContext(paramAbstractButton, 1);
/*  78 */     SynthStyle localSynthStyle = this.style;
/*  79 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  80 */     if (this.style != localSynthStyle) {
/*  81 */       if ((paramAbstractButton.getMargin() == null) || ((paramAbstractButton.getMargin() instanceof UIResource)))
/*     */       {
/*  83 */         localObject = (Insets)this.style.get(localSynthContext, getPropertyPrefix() + "margin");
/*     */ 
/*  86 */         if (localObject == null)
/*     */         {
/*  88 */           localObject = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
/*     */         }
/*  90 */         paramAbstractButton.setMargin((Insets)localObject);
/*     */       }
/*     */ 
/*  93 */       Object localObject = this.style.get(localSynthContext, getPropertyPrefix() + "iconTextGap");
/*  94 */       if (localObject != null) {
/*  95 */         LookAndFeel.installProperty(paramAbstractButton, "iconTextGap", localObject);
/*     */       }
/*     */ 
/*  98 */       localObject = this.style.get(localSynthContext, getPropertyPrefix() + "contentAreaFilled");
/*  99 */       LookAndFeel.installProperty(paramAbstractButton, "contentAreaFilled", localObject != null ? localObject : Boolean.TRUE);
/*     */ 
/* 102 */       if (localSynthStyle != null) {
/* 103 */         uninstallKeyboardActions(paramAbstractButton);
/* 104 */         installKeyboardActions(paramAbstractButton);
/*     */       }
/*     */     }
/*     */ 
/* 108 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(AbstractButton paramAbstractButton)
/*     */   {
/* 116 */     super.uninstallListeners(paramAbstractButton);
/* 117 */     paramAbstractButton.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton)
/*     */   {
/* 125 */     SynthContext localSynthContext = getContext(paramAbstractButton, 1);
/*     */ 
/* 127 */     this.style.uninstallDefaults(localSynthContext);
/* 128 */     localSynthContext.dispose();
/* 129 */     this.style = null;
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 137 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 141 */     Region localRegion = SynthLookAndFeel.getRegion(paramJComponent);
/* 142 */     return SynthContext.getContext(SynthContext.class, paramJComponent, localRegion, this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 150 */     int i = 1;
/*     */ 
/* 152 */     if (!paramJComponent.isEnabled()) {
/* 153 */       i = 8;
/*     */     }
/* 155 */     if (SynthLookAndFeel.getSelectedUI() == this) {
/* 156 */       return SynthLookAndFeel.getSelectedUIState() | 0x1;
/*     */     }
/* 158 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 159 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 161 */     if (localButtonModel.isPressed()) {
/* 162 */       if (localButtonModel.isArmed()) {
/* 163 */         i = 4;
/*     */       }
/*     */       else {
/* 166 */         i = 2;
/*     */       }
/*     */     }
/* 169 */     if (localButtonModel.isRollover()) {
/* 170 */       i |= 2;
/*     */     }
/* 172 */     if (localButtonModel.isSelected()) {
/* 173 */       i |= 512;
/*     */     }
/* 175 */     if ((paramJComponent.isFocusOwner()) && (localAbstractButton.isFocusPainted())) {
/* 176 */       i |= 256;
/*     */     }
/* 178 */     if (((paramJComponent instanceof JButton)) && (((JButton)paramJComponent).isDefaultButton())) {
/* 179 */       i |= 1024;
/*     */     }
/* 181 */     return i;
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 189 */     if (paramJComponent == null) {
/* 190 */       throw new NullPointerException("Component must be non-null");
/*     */     }
/* 192 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/* 193 */       throw new IllegalArgumentException("Width and height must be >= 0");
/*     */     }
/*     */ 
/* 196 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 197 */     String str = localAbstractButton.getText();
/* 198 */     if ((str == null) || ("".equals(str))) {
/* 199 */       return -1;
/*     */     }
/* 201 */     Insets localInsets = localAbstractButton.getInsets();
/* 202 */     Rectangle localRectangle1 = new Rectangle();
/* 203 */     Rectangle localRectangle2 = new Rectangle();
/* 204 */     Rectangle localRectangle3 = new Rectangle();
/* 205 */     localRectangle1.x = localInsets.left;
/* 206 */     localRectangle1.y = localInsets.top;
/* 207 */     localRectangle1.width = (paramInt1 - (localInsets.right + localRectangle1.x));
/* 208 */     localRectangle1.height = (paramInt2 - (localInsets.bottom + localRectangle1.y));
/*     */ 
/* 211 */     SynthContext localSynthContext = getContext(localAbstractButton);
/* 212 */     FontMetrics localFontMetrics = localSynthContext.getComponent().getFontMetrics(localSynthContext.getStyle().getFont(localSynthContext));
/*     */ 
/* 214 */     localSynthContext.getStyle().getGraphicsUtils(localSynthContext).layoutText(localSynthContext, localFontMetrics, localAbstractButton.getText(), localAbstractButton.getIcon(), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalTextPosition(), localAbstractButton.getVerticalTextPosition(), localRectangle1, localRectangle3, localRectangle2, localAbstractButton.getIconTextGap());
/*     */ 
/* 219 */     View localView = (View)localAbstractButton.getClientProperty("html");
/*     */     int i;
/* 221 */     if (localView != null) {
/* 222 */       i = BasicHTML.getHTMLBaseline(localView, localRectangle2.width, localRectangle2.height);
/*     */ 
/* 224 */       if (i >= 0)
/* 225 */         i += localRectangle2.y;
/*     */     }
/*     */     else
/*     */     {
/* 229 */       i = localRectangle2.y + localFontMetrics.getAscent();
/*     */     }
/* 231 */     localSynthContext.dispose();
/* 232 */     return i;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 253 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 255 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 256 */     paintBackground(localSynthContext, paramGraphics, paramJComponent);
/* 257 */     paint(localSynthContext, paramGraphics);
/* 258 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 272 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 274 */     paint(localSynthContext, paramGraphics);
/* 275 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 286 */     AbstractButton localAbstractButton = (AbstractButton)paramSynthContext.getComponent();
/*     */ 
/* 288 */     paramGraphics.setColor(paramSynthContext.getStyle().getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/*     */ 
/* 290 */     paramGraphics.setFont(this.style.getFont(paramSynthContext));
/* 291 */     paramSynthContext.getStyle().getGraphicsUtils(paramSynthContext).paintText(paramSynthContext, paramGraphics, localAbstractButton.getText(), getIcon(localAbstractButton), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalTextPosition(), localAbstractButton.getVerticalTextPosition(), localAbstractButton.getIconTextGap(), localAbstractButton.getDisplayedMnemonicIndex(), getTextShiftOffset(paramSynthContext));
/*     */   }
/*     */ 
/*     */   void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 300 */     if (((AbstractButton)paramJComponent).isContentAreaFilled())
/* 301 */       paramSynthContext.getPainter().paintButtonBackground(paramSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 313 */     paramSynthContext.getPainter().paintButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected Icon getDefaultIcon(AbstractButton paramAbstractButton)
/*     */   {
/* 324 */     SynthContext localSynthContext = getContext(paramAbstractButton);
/* 325 */     Icon localIcon = localSynthContext.getStyle().getIcon(localSynthContext, getPropertyPrefix() + "icon");
/* 326 */     localSynthContext.dispose();
/* 327 */     return localIcon;
/*     */   }
/*     */ 
/*     */   protected Icon getIcon(AbstractButton paramAbstractButton)
/*     */   {
/* 338 */     Icon localIcon = paramAbstractButton.getIcon();
/* 339 */     ButtonModel localButtonModel = paramAbstractButton.getModel();
/*     */ 
/* 341 */     if (!localButtonModel.isEnabled())
/* 342 */       localIcon = getSynthDisabledIcon(paramAbstractButton, localIcon);
/* 343 */     else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed()))
/* 344 */       localIcon = getPressedIcon(paramAbstractButton, getSelectedIcon(paramAbstractButton, localIcon));
/* 345 */     else if ((paramAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover()))
/* 346 */       localIcon = getRolloverIcon(paramAbstractButton, getSelectedIcon(paramAbstractButton, localIcon));
/* 347 */     else if (localButtonModel.isSelected())
/* 348 */       localIcon = getSelectedIcon(paramAbstractButton, localIcon);
/*     */     else {
/* 350 */       localIcon = getEnabledIcon(paramAbstractButton, localIcon);
/*     */     }
/* 352 */     if (localIcon == null) {
/* 353 */       return getDefaultIcon(paramAbstractButton);
/*     */     }
/* 355 */     return localIcon;
/*     */   }
/*     */ 
/*     */   private Icon getIcon(AbstractButton paramAbstractButton, Icon paramIcon1, Icon paramIcon2, int paramInt)
/*     */   {
/* 371 */     Icon localIcon = paramIcon1;
/* 372 */     if (localIcon == null) {
/* 373 */       if ((paramIcon2 instanceof UIResource)) {
/* 374 */         localIcon = getSynthIcon(paramAbstractButton, paramInt);
/* 375 */         if (localIcon == null)
/* 376 */           localIcon = paramIcon2;
/*     */       }
/*     */       else {
/* 379 */         localIcon = paramIcon2;
/*     */       }
/*     */     }
/* 382 */     return localIcon;
/*     */   }
/*     */ 
/*     */   private Icon getSynthIcon(AbstractButton paramAbstractButton, int paramInt) {
/* 386 */     return this.style.getIcon(getContext(paramAbstractButton, paramInt), getPropertyPrefix() + "icon");
/*     */   }
/*     */ 
/*     */   private Icon getEnabledIcon(AbstractButton paramAbstractButton, Icon paramIcon) {
/* 390 */     if (paramIcon == null) {
/* 391 */       paramIcon = getSynthIcon(paramAbstractButton, 1);
/*     */     }
/* 393 */     return paramIcon;
/*     */   }
/*     */ 
/*     */   private Icon getSelectedIcon(AbstractButton paramAbstractButton, Icon paramIcon) {
/* 397 */     return getIcon(paramAbstractButton, paramAbstractButton.getSelectedIcon(), paramIcon, 512);
/*     */   }
/*     */ 
/*     */   private Icon getRolloverIcon(AbstractButton paramAbstractButton, Icon paramIcon)
/*     */   {
/* 402 */     ButtonModel localButtonModel = paramAbstractButton.getModel();
/*     */     Icon localIcon;
/* 404 */     if (localButtonModel.isSelected()) {
/* 405 */       localIcon = getIcon(paramAbstractButton, paramAbstractButton.getRolloverSelectedIcon(), paramIcon, 514);
/*     */     }
/*     */     else {
/* 408 */       localIcon = getIcon(paramAbstractButton, paramAbstractButton.getRolloverIcon(), paramIcon, 2);
/*     */     }
/*     */ 
/* 411 */     return localIcon;
/*     */   }
/*     */ 
/*     */   private Icon getPressedIcon(AbstractButton paramAbstractButton, Icon paramIcon) {
/* 415 */     return getIcon(paramAbstractButton, paramAbstractButton.getPressedIcon(), paramIcon, 4);
/*     */   }
/*     */ 
/*     */   private Icon getSynthDisabledIcon(AbstractButton paramAbstractButton, Icon paramIcon)
/*     */   {
/* 420 */     ButtonModel localButtonModel = paramAbstractButton.getModel();
/*     */     Icon localIcon;
/* 422 */     if (localButtonModel.isSelected()) {
/* 423 */       localIcon = getIcon(paramAbstractButton, paramAbstractButton.getDisabledSelectedIcon(), paramIcon, 520);
/*     */     }
/*     */     else {
/* 426 */       localIcon = getIcon(paramAbstractButton, paramAbstractButton.getDisabledIcon(), paramIcon, 8);
/*     */     }
/*     */ 
/* 429 */     return localIcon;
/*     */   }
/*     */ 
/*     */   private int getTextShiftOffset(SynthContext paramSynthContext)
/*     */   {
/* 436 */     AbstractButton localAbstractButton = (AbstractButton)paramSynthContext.getComponent();
/* 437 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 439 */     if ((localButtonModel.isArmed()) && (localButtonModel.isPressed()) && (localAbstractButton.getPressedIcon() == null))
/*     */     {
/* 441 */       return paramSynthContext.getStyle().getInt(paramSynthContext, getPropertyPrefix() + "textShiftOffset", 0);
/*     */     }
/*     */ 
/* 444 */     return 0;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 456 */     if ((paramJComponent.getComponentCount() > 0) && (paramJComponent.getLayout() != null)) {
/* 457 */       return null;
/*     */     }
/* 459 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 460 */     SynthContext localSynthContext = getContext(paramJComponent);
/* 461 */     Dimension localDimension = localSynthContext.getStyle().getGraphicsUtils(localSynthContext).getMinimumSize(localSynthContext, localSynthContext.getStyle().getFont(localSynthContext), localAbstractButton.getText(), getSizingIcon(localAbstractButton), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalTextPosition(), localAbstractButton.getVerticalTextPosition(), localAbstractButton.getIconTextGap(), localAbstractButton.getDisplayedMnemonicIndex());
/*     */ 
/* 468 */     localSynthContext.dispose();
/* 469 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 477 */     if ((paramJComponent.getComponentCount() > 0) && (paramJComponent.getLayout() != null)) {
/* 478 */       return null;
/*     */     }
/* 480 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 481 */     SynthContext localSynthContext = getContext(paramJComponent);
/* 482 */     Dimension localDimension = localSynthContext.getStyle().getGraphicsUtils(localSynthContext).getPreferredSize(localSynthContext, localSynthContext.getStyle().getFont(localSynthContext), localAbstractButton.getText(), getSizingIcon(localAbstractButton), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalTextPosition(), localAbstractButton.getVerticalTextPosition(), localAbstractButton.getIconTextGap(), localAbstractButton.getDisplayedMnemonicIndex());
/*     */ 
/* 489 */     localSynthContext.dispose();
/* 490 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 498 */     if ((paramJComponent.getComponentCount() > 0) && (paramJComponent.getLayout() != null)) {
/* 499 */       return null;
/*     */     }
/*     */ 
/* 502 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 503 */     SynthContext localSynthContext = getContext(paramJComponent);
/* 504 */     Dimension localDimension = localSynthContext.getStyle().getGraphicsUtils(localSynthContext).getMaximumSize(localSynthContext, localSynthContext.getStyle().getFont(localSynthContext), localAbstractButton.getText(), getSizingIcon(localAbstractButton), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalTextPosition(), localAbstractButton.getVerticalTextPosition(), localAbstractButton.getIconTextGap(), localAbstractButton.getDisplayedMnemonicIndex());
/*     */ 
/* 511 */     localSynthContext.dispose();
/* 512 */     return localDimension;
/*     */   }
/*     */ 
/*     */   protected Icon getSizingIcon(AbstractButton paramAbstractButton)
/*     */   {
/* 520 */     Icon localIcon = getEnabledIcon(paramAbstractButton, paramAbstractButton.getIcon());
/* 521 */     if (localIcon == null) {
/* 522 */       localIcon = getDefaultIcon(paramAbstractButton);
/*     */     }
/* 524 */     return localIcon;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 532 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 533 */       updateStyle((AbstractButton)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthButtonUI
 * JD-Core Version:    0.6.2
 */
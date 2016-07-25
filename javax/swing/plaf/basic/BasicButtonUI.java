/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component.BaselineResizeBehavior;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ButtonUI;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.View;
/*     */ import sun.awt.AppContext;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class BasicButtonUI extends ButtonUI
/*     */ {
/*     */   protected int defaultTextIconGap;
/*  56 */   private int shiftOffset = 0;
/*     */   protected int defaultTextShiftOffset;
/*     */   private static final String propertyPrefix = "Button.";
/*  63 */   private static final Object BASIC_BUTTON_UI_KEY = new Object();
/*     */ 
/* 191 */   private static Rectangle viewRect = new Rectangle();
/* 192 */   private static Rectangle textRect = new Rectangle();
/* 193 */   private static Rectangle iconRect = new Rectangle();
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  69 */     AppContext localAppContext = AppContext.getAppContext();
/*  70 */     BasicButtonUI localBasicButtonUI = (BasicButtonUI)localAppContext.get(BASIC_BUTTON_UI_KEY);
/*     */ 
/*  72 */     if (localBasicButtonUI == null) {
/*  73 */       localBasicButtonUI = new BasicButtonUI();
/*  74 */       localAppContext.put(BASIC_BUTTON_UI_KEY, localBasicButtonUI);
/*     */     }
/*  76 */     return localBasicButtonUI;
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix() {
/*  80 */     return "Button.";
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  88 */     installDefaults((AbstractButton)paramJComponent);
/*  89 */     installListeners((AbstractButton)paramJComponent);
/*  90 */     installKeyboardActions((AbstractButton)paramJComponent);
/*  91 */     BasicHTML.updateRenderer(paramJComponent, ((AbstractButton)paramJComponent).getText());
/*     */   }
/*     */ 
/*     */   protected void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  96 */     String str = getPropertyPrefix();
/*     */ 
/*  98 */     this.defaultTextShiftOffset = UIManager.getInt(str + "textShiftOffset");
/*     */ 
/* 101 */     if (paramAbstractButton.isContentAreaFilled())
/* 102 */       LookAndFeel.installProperty(paramAbstractButton, "opaque", Boolean.TRUE);
/*     */     else {
/* 104 */       LookAndFeel.installProperty(paramAbstractButton, "opaque", Boolean.FALSE);
/*     */     }
/*     */ 
/* 107 */     if ((paramAbstractButton.getMargin() == null) || ((paramAbstractButton.getMargin() instanceof UIResource))) {
/* 108 */       paramAbstractButton.setMargin(UIManager.getInsets(str + "margin"));
/*     */     }
/*     */ 
/* 111 */     LookAndFeel.installColorsAndFont(paramAbstractButton, str + "background", str + "foreground", str + "font");
/*     */ 
/* 113 */     LookAndFeel.installBorder(paramAbstractButton, str + "border");
/*     */ 
/* 115 */     Object localObject = UIManager.get(str + "rollover");
/* 116 */     if (localObject != null) {
/* 117 */       LookAndFeel.installProperty(paramAbstractButton, "rolloverEnabled", localObject);
/*     */     }
/*     */ 
/* 120 */     LookAndFeel.installProperty(paramAbstractButton, "iconTextGap", Integer.valueOf(4));
/*     */   }
/*     */ 
/*     */   protected void installListeners(AbstractButton paramAbstractButton) {
/* 124 */     BasicButtonListener localBasicButtonListener = createButtonListener(paramAbstractButton);
/* 125 */     if (localBasicButtonListener != null) {
/* 126 */       paramAbstractButton.addMouseListener(localBasicButtonListener);
/* 127 */       paramAbstractButton.addMouseMotionListener(localBasicButtonListener);
/* 128 */       paramAbstractButton.addFocusListener(localBasicButtonListener);
/* 129 */       paramAbstractButton.addPropertyChangeListener(localBasicButtonListener);
/* 130 */       paramAbstractButton.addChangeListener(localBasicButtonListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions(AbstractButton paramAbstractButton) {
/* 135 */     BasicButtonListener localBasicButtonListener = getButtonListener(paramAbstractButton);
/*     */ 
/* 137 */     if (localBasicButtonListener != null)
/* 138 */       localBasicButtonListener.installKeyboardActions(paramAbstractButton);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 147 */     uninstallKeyboardActions((AbstractButton)paramJComponent);
/* 148 */     uninstallListeners((AbstractButton)paramJComponent);
/* 149 */     uninstallDefaults((AbstractButton)paramJComponent);
/* 150 */     BasicHTML.updateRenderer(paramJComponent, "");
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions(AbstractButton paramAbstractButton) {
/* 154 */     BasicButtonListener localBasicButtonListener = getButtonListener(paramAbstractButton);
/* 155 */     if (localBasicButtonListener != null)
/* 156 */       localBasicButtonListener.uninstallKeyboardActions(paramAbstractButton);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(AbstractButton paramAbstractButton)
/*     */   {
/* 161 */     BasicButtonListener localBasicButtonListener = getButtonListener(paramAbstractButton);
/* 162 */     if (localBasicButtonListener != null) {
/* 163 */       paramAbstractButton.removeMouseListener(localBasicButtonListener);
/* 164 */       paramAbstractButton.removeMouseMotionListener(localBasicButtonListener);
/* 165 */       paramAbstractButton.removeFocusListener(localBasicButtonListener);
/* 166 */       paramAbstractButton.removeChangeListener(localBasicButtonListener);
/* 167 */       paramAbstractButton.removePropertyChangeListener(localBasicButtonListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/* 172 */     LookAndFeel.uninstallBorder(paramAbstractButton);
/*     */   }
/*     */ 
/*     */   protected BasicButtonListener createButtonListener(AbstractButton paramAbstractButton)
/*     */   {
/* 179 */     return new BasicButtonListener(paramAbstractButton);
/*     */   }
/*     */ 
/*     */   public int getDefaultTextIconGap(AbstractButton paramAbstractButton) {
/* 183 */     return this.defaultTextIconGap;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 201 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 202 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 204 */     String str = layout(localAbstractButton, SwingUtilities2.getFontMetrics(localAbstractButton, paramGraphics), localAbstractButton.getWidth(), localAbstractButton.getHeight());
/*     */ 
/* 207 */     clearTextShiftOffset();
/*     */ 
/* 210 */     if ((localButtonModel.isArmed()) && (localButtonModel.isPressed())) {
/* 211 */       paintButtonPressed(paramGraphics, localAbstractButton);
/*     */     }
/*     */ 
/* 215 */     if (localAbstractButton.getIcon() != null) {
/* 216 */       paintIcon(paramGraphics, paramJComponent, iconRect);
/*     */     }
/*     */ 
/* 219 */     if ((str != null) && (!str.equals(""))) {
/* 220 */       View localView = (View)paramJComponent.getClientProperty("html");
/* 221 */       if (localView != null)
/* 222 */         localView.paint(paramGraphics, textRect);
/*     */       else {
/* 224 */         paintText(paramGraphics, localAbstractButton, textRect, str);
/*     */       }
/*     */     }
/*     */ 
/* 228 */     if ((localAbstractButton.isFocusPainted()) && (localAbstractButton.hasFocus()))
/*     */     {
/* 230 */       paintFocus(paramGraphics, localAbstractButton, viewRect, textRect, iconRect);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintIcon(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle) {
/* 235 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 236 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/* 237 */     Object localObject1 = localAbstractButton.getIcon();
/* 238 */     Object localObject2 = null;
/*     */ 
/* 240 */     if (localObject1 == null) {
/* 241 */       return;
/*     */     }
/*     */ 
/* 244 */     Icon localIcon = null;
/*     */ 
/* 247 */     if (localButtonModel.isSelected()) {
/* 248 */       localIcon = localAbstractButton.getSelectedIcon();
/* 249 */       if (localIcon != null) {
/* 250 */         localObject1 = localIcon;
/*     */       }
/*     */     }
/*     */ 
/* 254 */     if (!localButtonModel.isEnabled()) {
/* 255 */       if (localButtonModel.isSelected()) {
/* 256 */         localObject2 = localAbstractButton.getDisabledSelectedIcon();
/* 257 */         if (localObject2 == null) {
/* 258 */           localObject2 = localIcon;
/*     */         }
/*     */       }
/*     */ 
/* 262 */       if (localObject2 == null)
/* 263 */         localObject2 = localAbstractButton.getDisabledIcon();
/*     */     }
/* 265 */     else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 266 */       localObject2 = localAbstractButton.getPressedIcon();
/* 267 */       if (localObject2 != null)
/*     */       {
/* 269 */         clearTextShiftOffset();
/*     */       }
/* 271 */     } else if ((localAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover())) {
/* 272 */       if (localButtonModel.isSelected()) {
/* 273 */         localObject2 = localAbstractButton.getRolloverSelectedIcon();
/* 274 */         if (localObject2 == null) {
/* 275 */           localObject2 = localIcon;
/*     */         }
/*     */       }
/*     */ 
/* 279 */       if (localObject2 == null) {
/* 280 */         localObject2 = localAbstractButton.getRolloverIcon();
/*     */       }
/*     */     }
/*     */ 
/* 284 */     if (localObject2 != null) {
/* 285 */       localObject1 = localObject2;
/*     */     }
/*     */ 
/* 288 */     if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 289 */       ((Icon)localObject1).paintIcon(paramJComponent, paramGraphics, paramRectangle.x + getTextShiftOffset(), paramRectangle.y + getTextShiftOffset());
/*     */     }
/*     */     else
/* 292 */       ((Icon)localObject1).paintIcon(paramJComponent, paramGraphics, paramRectangle.x, paramRectangle.y);
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle, String paramString)
/*     */   {
/* 302 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 303 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/* 304 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics);
/* 305 */     int i = localAbstractButton.getDisplayedMnemonicIndex();
/*     */ 
/* 308 */     if (localButtonModel.isEnabled())
/*     */     {
/* 310 */       paramGraphics.setColor(localAbstractButton.getForeground());
/* 311 */       SwingUtilities2.drawStringUnderlineCharAt(paramJComponent, paramGraphics, paramString, i, paramRectangle.x + getTextShiftOffset(), paramRectangle.y + localFontMetrics.getAscent() + getTextShiftOffset());
/*     */     }
/*     */     else
/*     */     {
/* 317 */       paramGraphics.setColor(localAbstractButton.getBackground().brighter());
/* 318 */       SwingUtilities2.drawStringUnderlineCharAt(paramJComponent, paramGraphics, paramString, i, paramRectangle.x, paramRectangle.y + localFontMetrics.getAscent());
/*     */ 
/* 320 */       paramGraphics.setColor(localAbstractButton.getBackground().darker());
/* 321 */       SwingUtilities2.drawStringUnderlineCharAt(paramJComponent, paramGraphics, paramString, i, paramRectangle.x - 1, paramRectangle.y + localFontMetrics.getAscent() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle, String paramString)
/*     */   {
/* 336 */     paintText(paramGraphics, paramAbstractButton, paramRectangle, paramString);
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void paintButtonPressed(Graphics paramGraphics, AbstractButton paramAbstractButton)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void clearTextShiftOffset()
/*     */   {
/* 351 */     this.shiftOffset = 0;
/*     */   }
/*     */ 
/*     */   protected void setTextShiftOffset() {
/* 355 */     this.shiftOffset = this.defaultTextShiftOffset;
/*     */   }
/*     */ 
/*     */   protected int getTextShiftOffset() {
/* 359 */     return this.shiftOffset;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 366 */     Dimension localDimension = getPreferredSize(paramJComponent);
/* 367 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 368 */     if (localView != null)
/*     */     {
/*     */       Dimension tmp21_20 = localDimension; tmp21_20.width = ((int)(tmp21_20.width - (localView.getPreferredSpan(0) - localView.getMinimumSpan(0))));
/*     */     }
/* 371 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent) {
/* 375 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 376 */     return BasicGraphicsUtils.getPreferredButtonSize(localAbstractButton, localAbstractButton.getIconTextGap());
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent) {
/* 380 */     Dimension localDimension = getPreferredSize(paramJComponent);
/* 381 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 382 */     if (localView != null)
/*     */     {
/*     */       Dimension tmp21_20 = localDimension; tmp21_20.width = ((int)(tmp21_20.width + (localView.getMaximumSpan(0) - localView.getPreferredSpan(0))));
/*     */     }
/* 385 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 397 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 398 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 399 */     String str = localAbstractButton.getText();
/* 400 */     if ((str == null) || ("".equals(str))) {
/* 401 */       return -1;
/*     */     }
/* 403 */     FontMetrics localFontMetrics = localAbstractButton.getFontMetrics(localAbstractButton.getFont());
/* 404 */     layout(localAbstractButton, localFontMetrics, paramInt1, paramInt2);
/* 405 */     return BasicHTML.getBaseline(localAbstractButton, textRect.y, localFontMetrics.getAscent(), textRect.width, textRect.height);
/*     */   }
/*     */ 
/*     */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*     */   {
/* 419 */     super.getBaselineResizeBehavior(paramJComponent);
/* 420 */     if (paramJComponent.getClientProperty("html") != null) {
/* 421 */       return Component.BaselineResizeBehavior.OTHER;
/*     */     }
/* 423 */     switch (((AbstractButton)paramJComponent).getVerticalAlignment()) {
/*     */     case 1:
/* 425 */       return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*     */     case 3:
/* 427 */       return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
/*     */     case 0:
/* 429 */       return Component.BaselineResizeBehavior.CENTER_OFFSET;
/*     */     case 2:
/* 431 */     }return Component.BaselineResizeBehavior.OTHER;
/*     */   }
/*     */ 
/*     */   private String layout(AbstractButton paramAbstractButton, FontMetrics paramFontMetrics, int paramInt1, int paramInt2)
/*     */   {
/* 436 */     Insets localInsets = paramAbstractButton.getInsets();
/* 437 */     viewRect.x = localInsets.left;
/* 438 */     viewRect.y = localInsets.top;
/* 439 */     viewRect.width = (paramInt1 - (localInsets.right + viewRect.x));
/* 440 */     viewRect.height = (paramInt2 - (localInsets.bottom + viewRect.y));
/*     */ 
/* 442 */     textRect.x = (textRect.y = textRect.width = textRect.height = 0);
/* 443 */     iconRect.x = (iconRect.y = iconRect.width = iconRect.height = 0);
/*     */ 
/* 446 */     return SwingUtilities.layoutCompoundLabel(paramAbstractButton, paramFontMetrics, paramAbstractButton.getText(), paramAbstractButton.getIcon(), paramAbstractButton.getVerticalAlignment(), paramAbstractButton.getHorizontalAlignment(), paramAbstractButton.getVerticalTextPosition(), paramAbstractButton.getHorizontalTextPosition(), viewRect, iconRect, textRect, paramAbstractButton.getText() == null ? 0 : paramAbstractButton.getIconTextGap());
/*     */   }
/*     */ 
/*     */   private BasicButtonListener getButtonListener(AbstractButton paramAbstractButton)
/*     */   {
/* 459 */     MouseMotionListener[] arrayOfMouseMotionListener1 = paramAbstractButton.getMouseMotionListeners();
/*     */ 
/* 461 */     if (arrayOfMouseMotionListener1 != null) {
/* 462 */       for (MouseMotionListener localMouseMotionListener : arrayOfMouseMotionListener1) {
/* 463 */         if ((localMouseMotionListener instanceof BasicButtonListener)) {
/* 464 */           return (BasicButtonListener)localMouseMotionListener;
/*     */         }
/*     */       }
/*     */     }
/* 468 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicButtonUI
 * JD-Core Version:    0.6.2
 */
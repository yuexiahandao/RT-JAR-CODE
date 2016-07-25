/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Component.BaselineResizeBehavior;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentInputMapUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.InputMapUIResource;
/*     */ import javax.swing.plaf.LabelUI;
/*     */ import javax.swing.text.View;
/*     */ import sun.awt.AppContext;
/*     */ import sun.swing.SwingUtilities2;
/*     */ import sun.swing.UIAction;
/*     */ 
/*     */ public class BasicLabelUI extends LabelUI
/*     */   implements PropertyChangeListener
/*     */ {
/*  67 */   protected static BasicLabelUI labelUI = new BasicLabelUI();
/*  68 */   private static final Object BASIC_LABEL_UI_KEY = new Object();
/*     */   private Rectangle paintIconR;
/*     */   private Rectangle paintTextR;
/*     */ 
/*     */   public BasicLabelUI()
/*     */   {
/*  70 */     this.paintIconR = new Rectangle();
/*  71 */     this.paintTextR = new Rectangle();
/*     */   }
/*     */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/*  74 */     paramLazyActionMap.put(new Actions("press"));
/*  75 */     paramLazyActionMap.put(new Actions("release"));
/*     */   }
/*     */ 
/*     */   protected String layoutCL(JLabel paramJLabel, FontMetrics paramFontMetrics, String paramString, Icon paramIcon, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3)
/*     */   {
/*  94 */     return SwingUtilities.layoutCompoundLabel(paramJLabel, paramFontMetrics, paramString, paramIcon, paramJLabel.getVerticalAlignment(), paramJLabel.getHorizontalAlignment(), paramJLabel.getVerticalTextPosition(), paramJLabel.getHorizontalTextPosition(), paramRectangle1, paramRectangle2, paramRectangle3, paramJLabel.getIconTextGap());
/*     */   }
/*     */ 
/*     */   protected void paintEnabledText(JLabel paramJLabel, Graphics paramGraphics, String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 117 */     int i = paramJLabel.getDisplayedMnemonicIndex();
/* 118 */     paramGraphics.setColor(paramJLabel.getForeground());
/* 119 */     SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void paintDisabledText(JLabel paramJLabel, Graphics paramGraphics, String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 133 */     int i = paramJLabel.getDisplayedMnemonicIndex();
/* 134 */     Color localColor = paramJLabel.getBackground();
/* 135 */     paramGraphics.setColor(localColor.brighter());
/* 136 */     SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1 + 1, paramInt2 + 1);
/*     */ 
/* 138 */     paramGraphics.setColor(localColor.darker());
/* 139 */     SwingUtilities2.drawStringUnderlineCharAt(paramJLabel, paramGraphics, paramString, i, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 155 */     JLabel localJLabel = (JLabel)paramJComponent;
/* 156 */     String str1 = localJLabel.getText();
/* 157 */     Icon localIcon = localJLabel.isEnabled() ? localJLabel.getIcon() : localJLabel.getDisabledIcon();
/*     */ 
/* 159 */     if ((localIcon == null) && (str1 == null)) {
/* 160 */       return;
/*     */     }
/*     */ 
/* 163 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(localJLabel, paramGraphics);
/* 164 */     String str2 = layout(localJLabel, localFontMetrics, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 166 */     if (localIcon != null) {
/* 167 */       localIcon.paintIcon(paramJComponent, paramGraphics, this.paintIconR.x, this.paintIconR.y);
/*     */     }
/*     */ 
/* 170 */     if (str1 != null) {
/* 171 */       View localView = (View)paramJComponent.getClientProperty("html");
/* 172 */       if (localView != null) {
/* 173 */         localView.paint(paramGraphics, this.paintTextR);
/*     */       } else {
/* 175 */         int i = this.paintTextR.x;
/* 176 */         int j = this.paintTextR.y + localFontMetrics.getAscent();
/*     */ 
/* 178 */         if (localJLabel.isEnabled()) {
/* 179 */           paintEnabledText(localJLabel, paramGraphics, str2, i, j);
/*     */         }
/*     */         else
/* 182 */           paintDisabledText(localJLabel, paramGraphics, str2, i, j);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String layout(JLabel paramJLabel, FontMetrics paramFontMetrics, int paramInt1, int paramInt2)
/*     */   {
/* 190 */     Insets localInsets = paramJLabel.getInsets(null);
/* 191 */     String str = paramJLabel.getText();
/* 192 */     Icon localIcon = paramJLabel.isEnabled() ? paramJLabel.getIcon() : paramJLabel.getDisabledIcon();
/*     */ 
/* 194 */     Rectangle localRectangle = new Rectangle();
/* 195 */     localRectangle.x = localInsets.left;
/* 196 */     localRectangle.y = localInsets.top;
/* 197 */     localRectangle.width = (paramInt1 - (localInsets.left + localInsets.right));
/* 198 */     localRectangle.height = (paramInt2 - (localInsets.top + localInsets.bottom));
/* 199 */     this.paintIconR.x = (this.paintIconR.y = this.paintIconR.width = this.paintIconR.height = 0);
/* 200 */     this.paintTextR.x = (this.paintTextR.y = this.paintTextR.width = this.paintTextR.height = 0);
/* 201 */     return layoutCL(paramJLabel, paramFontMetrics, str, localIcon, localRectangle, this.paintIconR, this.paintTextR);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 207 */     JLabel localJLabel = (JLabel)paramJComponent;
/* 208 */     String str = localJLabel.getText();
/* 209 */     Icon localIcon = localJLabel.isEnabled() ? localJLabel.getIcon() : localJLabel.getDisabledIcon();
/*     */ 
/* 211 */     Insets localInsets = localJLabel.getInsets(null);
/* 212 */     Font localFont = localJLabel.getFont();
/*     */ 
/* 214 */     int i = localInsets.left + localInsets.right;
/* 215 */     int j = localInsets.top + localInsets.bottom;
/*     */ 
/* 217 */     if ((localIcon == null) && ((str == null) || ((str != null) && (localFont == null))))
/*     */     {
/* 220 */       return new Dimension(i, j);
/*     */     }
/* 222 */     if ((str == null) || ((localIcon != null) && (localFont == null))) {
/* 223 */       return new Dimension(localIcon.getIconWidth() + i, localIcon.getIconHeight() + j);
/*     */     }
/*     */ 
/* 227 */     FontMetrics localFontMetrics = localJLabel.getFontMetrics(localFont);
/* 228 */     Rectangle localRectangle1 = new Rectangle();
/* 229 */     Rectangle localRectangle2 = new Rectangle();
/* 230 */     Rectangle localRectangle3 = new Rectangle();
/*     */ 
/* 232 */     localRectangle1.x = (localRectangle1.y = localRectangle1.width = localRectangle1.height = 0);
/* 233 */     localRectangle2.x = (localRectangle2.y = localRectangle2.width = localRectangle2.height = 0);
/* 234 */     localRectangle3.x = i;
/* 235 */     localRectangle3.y = j;
/* 236 */     localRectangle3.width = (localRectangle3.height = 32767);
/*     */ 
/* 238 */     layoutCL(localJLabel, localFontMetrics, str, localIcon, localRectangle3, localRectangle1, localRectangle2);
/* 239 */     int k = Math.min(localRectangle1.x, localRectangle2.x);
/* 240 */     int m = Math.max(localRectangle1.x + localRectangle1.width, localRectangle2.x + localRectangle2.width);
/* 241 */     int n = Math.min(localRectangle1.y, localRectangle2.y);
/* 242 */     int i1 = Math.max(localRectangle1.y + localRectangle1.height, localRectangle2.y + localRectangle2.height);
/* 243 */     Dimension localDimension = new Dimension(m - k, i1 - n);
/*     */ 
/* 245 */     localDimension.width += i;
/* 246 */     localDimension.height += j;
/* 247 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 256 */     Dimension localDimension = getPreferredSize(paramJComponent);
/* 257 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 258 */     if (localView != null)
/*     */     {
/*     */       Dimension tmp21_20 = localDimension; tmp21_20.width = ((int)(tmp21_20.width - (localView.getPreferredSpan(0) - localView.getMinimumSpan(0))));
/*     */     }
/* 261 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 268 */     Dimension localDimension = getPreferredSize(paramJComponent);
/* 269 */     View localView = (View)paramJComponent.getClientProperty("html");
/* 270 */     if (localView != null)
/*     */     {
/*     */       Dimension tmp21_20 = localDimension; tmp21_20.width = ((int)(tmp21_20.width + (localView.getMaximumSpan(0) - localView.getPreferredSpan(0))));
/*     */     }
/* 273 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 285 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 286 */     JLabel localJLabel = (JLabel)paramJComponent;
/* 287 */     String str = localJLabel.getText();
/* 288 */     if ((str == null) || ("".equals(str)) || (localJLabel.getFont() == null)) {
/* 289 */       return -1;
/*     */     }
/* 291 */     FontMetrics localFontMetrics = localJLabel.getFontMetrics(localJLabel.getFont());
/* 292 */     layout(localJLabel, localFontMetrics, paramInt1, paramInt2);
/* 293 */     return BasicHTML.getBaseline(localJLabel, this.paintTextR.y, localFontMetrics.getAscent(), this.paintTextR.width, this.paintTextR.height);
/*     */   }
/*     */ 
/*     */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*     */   {
/* 307 */     super.getBaselineResizeBehavior(paramJComponent);
/* 308 */     if (paramJComponent.getClientProperty("html") != null) {
/* 309 */       return Component.BaselineResizeBehavior.OTHER;
/*     */     }
/* 311 */     switch (((JLabel)paramJComponent).getVerticalAlignment()) {
/*     */     case 1:
/* 313 */       return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*     */     case 3:
/* 315 */       return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
/*     */     case 0:
/* 317 */       return Component.BaselineResizeBehavior.CENTER_OFFSET;
/*     */     case 2:
/* 319 */     }return Component.BaselineResizeBehavior.OTHER;
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 324 */     installDefaults((JLabel)paramJComponent);
/* 325 */     installComponents((JLabel)paramJComponent);
/* 326 */     installListeners((JLabel)paramJComponent);
/* 327 */     installKeyboardActions((JLabel)paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 332 */     uninstallDefaults((JLabel)paramJComponent);
/* 333 */     uninstallComponents((JLabel)paramJComponent);
/* 334 */     uninstallListeners((JLabel)paramJComponent);
/* 335 */     uninstallKeyboardActions((JLabel)paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JLabel paramJLabel) {
/* 339 */     LookAndFeel.installColorsAndFont(paramJLabel, "Label.background", "Label.foreground", "Label.font");
/* 340 */     LookAndFeel.installProperty(paramJLabel, "opaque", Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   protected void installListeners(JLabel paramJLabel) {
/* 344 */     paramJLabel.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void installComponents(JLabel paramJLabel) {
/* 348 */     BasicHTML.updateRenderer(paramJLabel, paramJLabel.getText());
/* 349 */     paramJLabel.setInheritsPopupMenu(true);
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions(JLabel paramJLabel) {
/* 353 */     int i = paramJLabel.getDisplayedMnemonic();
/* 354 */     Component localComponent = paramJLabel.getLabelFor();
/*     */     Object localObject;
/* 355 */     if ((i != 0) && (localComponent != null)) {
/* 356 */       LazyActionMap.installLazyActionMap(paramJLabel, BasicLabelUI.class, "Label.actionMap");
/*     */ 
/* 358 */       localObject = SwingUtilities.getUIInputMap(paramJLabel, 2);
/*     */ 
/* 360 */       if (localObject == null) {
/* 361 */         localObject = new ComponentInputMapUIResource(paramJLabel);
/* 362 */         SwingUtilities.replaceUIInputMap(paramJLabel, 2, (InputMap)localObject);
/*     */       }
/*     */ 
/* 365 */       ((InputMap)localObject).clear();
/* 366 */       ((InputMap)localObject).put(KeyStroke.getKeyStroke(i, BasicLookAndFeel.getFocusAcceleratorKeyMask(), false), "press");
/*     */     }
/*     */     else {
/* 369 */       localObject = SwingUtilities.getUIInputMap(paramJLabel, 2);
/*     */ 
/* 371 */       if (localObject != null)
/* 372 */         ((InputMap)localObject).clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JLabel paramJLabel)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JLabel paramJLabel) {
/* 381 */     paramJLabel.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents(JLabel paramJLabel) {
/* 385 */     BasicHTML.updateRenderer(paramJLabel, "");
/*     */   }
/*     */ 
/*     */   protected void uninstallKeyboardActions(JLabel paramJLabel) {
/* 389 */     SwingUtilities.replaceUIInputMap(paramJLabel, 0, null);
/* 390 */     SwingUtilities.replaceUIInputMap(paramJLabel, 2, null);
/*     */ 
/* 392 */     SwingUtilities.replaceUIActionMap(paramJLabel, null);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/* 396 */     if (System.getSecurityManager() != null) {
/* 397 */       AppContext localAppContext = AppContext.getAppContext();
/* 398 */       BasicLabelUI localBasicLabelUI = (BasicLabelUI)localAppContext.get(BASIC_LABEL_UI_KEY);
/*     */ 
/* 400 */       if (localBasicLabelUI == null) {
/* 401 */         localBasicLabelUI = new BasicLabelUI();
/* 402 */         localAppContext.put(BASIC_LABEL_UI_KEY, localBasicLabelUI);
/*     */       }
/* 404 */       return localBasicLabelUI;
/*     */     }
/* 406 */     return labelUI;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 410 */     String str1 = paramPropertyChangeEvent.getPropertyName();
/* 411 */     if ((str1 == "text") || ("font" == str1) || ("foreground" == str1))
/*     */     {
/* 415 */       JLabel localJLabel = (JLabel)paramPropertyChangeEvent.getSource();
/* 416 */       String str2 = localJLabel.getText();
/* 417 */       BasicHTML.updateRenderer(localJLabel, str2);
/*     */     }
/* 419 */     else if ((str1 == "labelFor") || (str1 == "displayedMnemonic")) {
/* 420 */       installKeyboardActions((JLabel)paramPropertyChangeEvent.getSource());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Actions extends UIAction
/*     */   {
/*     */     private static final String PRESS = "press";
/*     */     private static final String RELEASE = "release";
/*     */ 
/*     */     Actions(String paramString)
/*     */     {
/* 433 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 437 */       JLabel localJLabel = (JLabel)paramActionEvent.getSource();
/* 438 */       String str = getName();
/* 439 */       if (str == "press") {
/* 440 */         doPress(localJLabel);
/*     */       }
/* 442 */       else if (str == "release")
/* 443 */         doRelease(localJLabel);
/*     */     }
/*     */ 
/*     */     private void doPress(JLabel paramJLabel)
/*     */     {
/* 448 */       Component localComponent = paramJLabel.getLabelFor();
/* 449 */       if ((localComponent != null) && (localComponent.isEnabled())) {
/* 450 */         Object localObject = SwingUtilities.getUIInputMap(paramJLabel, 0);
/* 451 */         if (localObject == null) {
/* 452 */           localObject = new InputMapUIResource();
/* 453 */           SwingUtilities.replaceUIInputMap(paramJLabel, 0, (InputMap)localObject);
/*     */         }
/* 455 */         int i = paramJLabel.getDisplayedMnemonic();
/* 456 */         ((InputMap)localObject).put(KeyStroke.getKeyStroke(i, BasicLookAndFeel.getFocusAcceleratorKeyMask(), true), "release");
/*     */ 
/* 458 */         ((InputMap)localObject).put(KeyStroke.getKeyStroke(i, 0, true), "release");
/*     */ 
/* 460 */         ((InputMap)localObject).put(KeyStroke.getKeyStroke(18, 0, true), "release");
/* 461 */         paramJLabel.requestFocus();
/*     */       }
/*     */     }
/*     */ 
/*     */     private void doRelease(JLabel paramJLabel) {
/* 466 */       Component localComponent = paramJLabel.getLabelFor();
/* 467 */       if ((localComponent != null) && (localComponent.isEnabled())) {
/* 468 */         InputMap localInputMap = SwingUtilities.getUIInputMap(paramJLabel, 0);
/* 469 */         if (localInputMap != null)
/*     */         {
/* 471 */           int i = paramJLabel.getDisplayedMnemonic();
/* 472 */           localInputMap.remove(KeyStroke.getKeyStroke(i, BasicLookAndFeel.getFocusAcceleratorKeyMask(), true));
/* 473 */           localInputMap.remove(KeyStroke.getKeyStroke(i, 0, true));
/* 474 */           localInputMap.remove(KeyStroke.getKeyStroke(18, 0, true));
/*     */         }
/* 476 */         if (((localComponent instanceof Container)) && (((Container)localComponent).isFocusCycleRoot()))
/*     */         {
/* 478 */           localComponent.requestFocus();
/*     */         }
/* 480 */         else SwingUtilities2.compositeRequestFocus(localComponent);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicLabelUI
 * JD-Core Version:    0.6.2
 */
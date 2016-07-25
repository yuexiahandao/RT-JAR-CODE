/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.View;
/*     */ import sun.awt.AppContext;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class BasicRadioButtonUI extends BasicToggleButtonUI
/*     */ {
/*  45 */   private static final Object BASIC_RADIO_BUTTON_UI_KEY = new Object();
/*     */   protected Icon icon;
/*  49 */   private boolean defaults_initialized = false;
/*     */   private static final String propertyPrefix = "RadioButton.";
/* 101 */   private static Dimension size = new Dimension();
/* 102 */   private static Rectangle viewRect = new Rectangle();
/* 103 */   private static Rectangle iconRect = new Rectangle();
/* 104 */   private static Rectangle textRect = new Rectangle();
/*     */ 
/* 208 */   private static Rectangle prefViewRect = new Rectangle();
/* 209 */   private static Rectangle prefIconRect = new Rectangle();
/* 210 */   private static Rectangle prefTextRect = new Rectangle();
/* 211 */   private static Insets prefInsets = new Insets(0, 0, 0, 0);
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     AppContext localAppContext = AppContext.getAppContext();
/*  58 */     BasicRadioButtonUI localBasicRadioButtonUI = (BasicRadioButtonUI)localAppContext.get(BASIC_RADIO_BUTTON_UI_KEY);
/*     */ 
/*  60 */     if (localBasicRadioButtonUI == null) {
/*  61 */       localBasicRadioButtonUI = new BasicRadioButtonUI();
/*  62 */       localAppContext.put(BASIC_RADIO_BUTTON_UI_KEY, localBasicRadioButtonUI);
/*     */     }
/*  64 */     return localBasicRadioButtonUI;
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix() {
/*  68 */     return "RadioButton.";
/*     */   }
/*     */ 
/*     */   protected void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  75 */     super.installDefaults(paramAbstractButton);
/*  76 */     if (!this.defaults_initialized) {
/*  77 */       this.icon = UIManager.getIcon(getPropertyPrefix() + "icon");
/*  78 */       this.defaults_initialized = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  86 */     super.uninstallDefaults(paramAbstractButton);
/*  87 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   public Icon getDefaultIcon() {
/*  91 */     return this.icon;
/*     */   }
/*     */ 
/*     */   public synchronized void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 110 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 111 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 113 */     Font localFont = paramJComponent.getFont();
/* 114 */     paramGraphics.setFont(localFont);
/* 115 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics, localFont);
/*     */ 
/* 117 */     Insets localInsets = paramJComponent.getInsets();
/* 118 */     size = localAbstractButton.getSize(size);
/* 119 */     viewRect.x = localInsets.left;
/* 120 */     viewRect.y = localInsets.top;
/* 121 */     viewRect.width = (size.width - (localInsets.right + viewRect.x));
/* 122 */     viewRect.height = (size.height - (localInsets.bottom + viewRect.y));
/* 123 */     iconRect.x = (iconRect.y = iconRect.width = iconRect.height = 0);
/* 124 */     textRect.x = (textRect.y = textRect.width = textRect.height = 0);
/*     */ 
/* 126 */     Icon localIcon = localAbstractButton.getIcon();
/* 127 */     Object localObject1 = null;
/* 128 */     Object localObject2 = null;
/*     */ 
/* 130 */     String str = SwingUtilities.layoutCompoundLabel(paramJComponent, localFontMetrics, localAbstractButton.getText(), localIcon != null ? localIcon : getDefaultIcon(), localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalTextPosition(), localAbstractButton.getHorizontalTextPosition(), viewRect, iconRect, textRect, localAbstractButton.getText() == null ? 0 : localAbstractButton.getIconTextGap());
/*     */ 
/* 138 */     if (paramJComponent.isOpaque()) {
/* 139 */       paramGraphics.setColor(localAbstractButton.getBackground());
/* 140 */       paramGraphics.fillRect(0, 0, size.width, size.height);
/*     */     }
/*     */ 
/* 145 */     if (localIcon != null)
/*     */     {
/* 147 */       if (!localButtonModel.isEnabled()) {
/* 148 */         if (localButtonModel.isSelected())
/* 149 */           localIcon = localAbstractButton.getDisabledSelectedIcon();
/*     */         else
/* 151 */           localIcon = localAbstractButton.getDisabledIcon();
/*     */       }
/* 153 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 154 */         localIcon = localAbstractButton.getPressedIcon();
/* 155 */         if (localIcon == null)
/*     */         {
/* 157 */           localIcon = localAbstractButton.getSelectedIcon();
/*     */         }
/* 159 */       } else if (localButtonModel.isSelected()) {
/* 160 */         if ((localAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover())) {
/* 161 */           localIcon = localAbstractButton.getRolloverSelectedIcon();
/* 162 */           if (localIcon == null)
/* 163 */             localIcon = localAbstractButton.getSelectedIcon();
/*     */         }
/*     */         else {
/* 166 */           localIcon = localAbstractButton.getSelectedIcon();
/*     */         }
/* 168 */       } else if ((localAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover())) {
/* 169 */         localIcon = localAbstractButton.getRolloverIcon();
/*     */       }
/*     */ 
/* 172 */       if (localIcon == null) {
/* 173 */         localIcon = localAbstractButton.getIcon();
/*     */       }
/*     */ 
/* 176 */       localIcon.paintIcon(paramJComponent, paramGraphics, iconRect.x, iconRect.y);
/*     */     }
/*     */     else {
/* 179 */       getDefaultIcon().paintIcon(paramJComponent, paramGraphics, iconRect.x, iconRect.y);
/*     */     }
/*     */ 
/* 184 */     if (str != null) {
/* 185 */       View localView = (View)paramJComponent.getClientProperty("html");
/* 186 */       if (localView != null)
/* 187 */         localView.paint(paramGraphics, textRect);
/*     */       else {
/* 189 */         paintText(paramGraphics, localAbstractButton, textRect, str);
/*     */       }
/* 191 */       if ((localAbstractButton.hasFocus()) && (localAbstractButton.isFocusPainted()) && (textRect.width > 0) && (textRect.height > 0))
/*     */       {
/* 193 */         paintFocus(paramGraphics, textRect, size);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, Rectangle paramRectangle, Dimension paramDimension)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 217 */     if (paramJComponent.getComponentCount() > 0) {
/* 218 */       return null;
/*     */     }
/*     */ 
/* 221 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/*     */ 
/* 223 */     String str = localAbstractButton.getText();
/*     */ 
/* 225 */     Icon localIcon = localAbstractButton.getIcon();
/* 226 */     if (localIcon == null) {
/* 227 */       localIcon = getDefaultIcon();
/*     */     }
/*     */ 
/* 230 */     Font localFont = localAbstractButton.getFont();
/* 231 */     FontMetrics localFontMetrics = localAbstractButton.getFontMetrics(localFont);
/*     */ 
/* 233 */     prefViewRect.x = (prefViewRect.y = 0);
/* 234 */     prefViewRect.width = 32767;
/* 235 */     prefViewRect.height = 32767;
/* 236 */     prefIconRect.x = (prefIconRect.y = prefIconRect.width = prefIconRect.height = 0);
/* 237 */     prefTextRect.x = (prefTextRect.y = prefTextRect.width = prefTextRect.height = 0);
/*     */ 
/* 239 */     SwingUtilities.layoutCompoundLabel(paramJComponent, localFontMetrics, str, localIcon, localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalTextPosition(), localAbstractButton.getHorizontalTextPosition(), prefViewRect, prefIconRect, prefTextRect, str == null ? 0 : localAbstractButton.getIconTextGap());
/*     */ 
/* 247 */     int i = Math.min(prefIconRect.x, prefTextRect.x);
/* 248 */     int j = Math.max(prefIconRect.x + prefIconRect.width, prefTextRect.x + prefTextRect.width);
/*     */ 
/* 250 */     int k = Math.min(prefIconRect.y, prefTextRect.y);
/* 251 */     int m = Math.max(prefIconRect.y + prefIconRect.height, prefTextRect.y + prefTextRect.height);
/*     */ 
/* 253 */     int n = j - i;
/* 254 */     int i1 = m - k;
/*     */ 
/* 256 */     prefInsets = localAbstractButton.getInsets(prefInsets);
/* 257 */     n += prefInsets.left + prefInsets.right;
/* 258 */     i1 += prefInsets.top + prefInsets.bottom;
/* 259 */     return new Dimension(n, i1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicRadioButtonUI
 * JD-Core Version:    0.6.2
 */
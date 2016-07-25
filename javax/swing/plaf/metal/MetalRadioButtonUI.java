/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
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
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRadioButtonUI;
/*     */ import javax.swing.text.View;
/*     */ import sun.awt.AppContext;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MetalRadioButtonUI extends BasicRadioButtonUI
/*     */ {
/*  58 */   private static final Object METAL_RADIO_BUTTON_UI_KEY = new Object();
/*     */   protected Color focusColor;
/*     */   protected Color selectColor;
/*     */   protected Color disabledTextColor;
/*  64 */   private boolean defaults_initialized = false;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  70 */     AppContext localAppContext = AppContext.getAppContext();
/*  71 */     MetalRadioButtonUI localMetalRadioButtonUI = (MetalRadioButtonUI)localAppContext.get(METAL_RADIO_BUTTON_UI_KEY);
/*     */ 
/*  73 */     if (localMetalRadioButtonUI == null) {
/*  74 */       localMetalRadioButtonUI = new MetalRadioButtonUI();
/*  75 */       localAppContext.put(METAL_RADIO_BUTTON_UI_KEY, localMetalRadioButtonUI);
/*     */     }
/*  77 */     return localMetalRadioButtonUI;
/*     */   }
/*     */ 
/*     */   public void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  84 */     super.installDefaults(paramAbstractButton);
/*  85 */     if (!this.defaults_initialized) {
/*  86 */       this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
/*  87 */       this.selectColor = UIManager.getColor(getPropertyPrefix() + "select");
/*  88 */       this.disabledTextColor = UIManager.getColor(getPropertyPrefix() + "disabledText");
/*  89 */       this.defaults_initialized = true;
/*     */     }
/*  91 */     LookAndFeel.installProperty(paramAbstractButton, "opaque", Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/*  95 */     super.uninstallDefaults(paramAbstractButton);
/*  96 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   protected Color getSelectColor()
/*     */   {
/* 103 */     return this.selectColor;
/*     */   }
/*     */ 
/*     */   protected Color getDisabledTextColor() {
/* 107 */     return this.disabledTextColor;
/*     */   }
/*     */ 
/*     */   protected Color getFocusColor() {
/* 111 */     return this.focusColor;
/*     */   }
/*     */ 
/*     */   public synchronized void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 120 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 121 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 123 */     Dimension localDimension = paramJComponent.getSize();
/*     */ 
/* 125 */     int i = localDimension.width;
/* 126 */     int j = localDimension.height;
/*     */ 
/* 128 */     Font localFont = paramJComponent.getFont();
/* 129 */     paramGraphics.setFont(localFont);
/* 130 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics, localFont);
/*     */ 
/* 132 */     Rectangle localRectangle1 = new Rectangle(localDimension);
/* 133 */     Rectangle localRectangle2 = new Rectangle();
/* 134 */     Rectangle localRectangle3 = new Rectangle();
/*     */ 
/* 136 */     Insets localInsets = paramJComponent.getInsets();
/* 137 */     localRectangle1.x += localInsets.left;
/* 138 */     localRectangle1.y += localInsets.top;
/* 139 */     localRectangle1.width -= localInsets.right + localRectangle1.x;
/* 140 */     localRectangle1.height -= localInsets.bottom + localRectangle1.y;
/*     */ 
/* 142 */     Icon localIcon = localAbstractButton.getIcon();
/* 143 */     Object localObject1 = null;
/* 144 */     Object localObject2 = null;
/*     */ 
/* 146 */     String str = SwingUtilities.layoutCompoundLabel(paramJComponent, localFontMetrics, localAbstractButton.getText(), localIcon != null ? localIcon : getDefaultIcon(), localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalTextPosition(), localAbstractButton.getHorizontalTextPosition(), localRectangle1, localRectangle2, localRectangle3, localAbstractButton.getIconTextGap());
/*     */ 
/* 153 */     if (paramJComponent.isOpaque()) {
/* 154 */       paramGraphics.setColor(localAbstractButton.getBackground());
/* 155 */       paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);
/*     */     }
/*     */ 
/* 160 */     if (localIcon != null)
/*     */     {
/* 162 */       if (!localButtonModel.isEnabled()) {
/* 163 */         if (localButtonModel.isSelected())
/* 164 */           localIcon = localAbstractButton.getDisabledSelectedIcon();
/*     */         else
/* 166 */           localIcon = localAbstractButton.getDisabledIcon();
/*     */       }
/* 168 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 169 */         localIcon = localAbstractButton.getPressedIcon();
/* 170 */         if (localIcon == null)
/*     */         {
/* 172 */           localIcon = localAbstractButton.getSelectedIcon();
/*     */         }
/* 174 */       } else if (localButtonModel.isSelected()) {
/* 175 */         if ((localAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover())) {
/* 176 */           localIcon = localAbstractButton.getRolloverSelectedIcon();
/* 177 */           if (localIcon == null)
/* 178 */             localIcon = localAbstractButton.getSelectedIcon();
/*     */         }
/*     */         else {
/* 181 */           localIcon = localAbstractButton.getSelectedIcon();
/*     */         }
/* 183 */       } else if ((localAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover())) {
/* 184 */         localIcon = localAbstractButton.getRolloverIcon();
/*     */       }
/*     */ 
/* 187 */       if (localIcon == null) {
/* 188 */         localIcon = localAbstractButton.getIcon();
/*     */       }
/*     */ 
/* 191 */       localIcon.paintIcon(paramJComponent, paramGraphics, localRectangle2.x, localRectangle2.y);
/*     */     }
/*     */     else {
/* 194 */       getDefaultIcon().paintIcon(paramJComponent, paramGraphics, localRectangle2.x, localRectangle2.y);
/*     */     }
/*     */ 
/* 199 */     if (str != null) {
/* 200 */       View localView = (View)paramJComponent.getClientProperty("html");
/* 201 */       if (localView != null) {
/* 202 */         localView.paint(paramGraphics, localRectangle3);
/*     */       } else {
/* 204 */         int k = localAbstractButton.getDisplayedMnemonicIndex();
/* 205 */         if (localButtonModel.isEnabled())
/*     */         {
/* 207 */           paramGraphics.setColor(localAbstractButton.getForeground());
/*     */         }
/*     */         else {
/* 210 */           paramGraphics.setColor(getDisabledTextColor());
/*     */         }
/* 212 */         SwingUtilities2.drawStringUnderlineCharAt(paramJComponent, paramGraphics, str, k, localRectangle3.x, localRectangle3.y + localFontMetrics.getAscent());
/*     */       }
/*     */ 
/* 215 */       if ((localAbstractButton.hasFocus()) && (localAbstractButton.isFocusPainted()) && (localRectangle3.width > 0) && (localRectangle3.height > 0))
/*     */       {
/* 217 */         paintFocus(paramGraphics, localRectangle3, localDimension);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, Rectangle paramRectangle, Dimension paramDimension) {
/* 223 */     paramGraphics.setColor(getFocusColor());
/* 224 */     paramGraphics.drawRect(paramRectangle.x - 1, paramRectangle.y - 1, paramRectangle.width + 1, paramRectangle.height + 1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalRadioButtonUI
 * JD-Core Version:    0.6.2
 */
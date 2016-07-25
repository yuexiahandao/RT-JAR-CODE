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
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.View;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class BasicToggleButtonUI extends BasicButtonUI
/*     */ {
/*  48 */   private static final Object BASIC_TOGGLE_BUTTON_UI_KEY = new Object();
/*     */   private static final String propertyPrefix = "ToggleButton.";
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  56 */     AppContext localAppContext = AppContext.getAppContext();
/*  57 */     BasicToggleButtonUI localBasicToggleButtonUI = (BasicToggleButtonUI)localAppContext.get(BASIC_TOGGLE_BUTTON_UI_KEY);
/*     */ 
/*  59 */     if (localBasicToggleButtonUI == null) {
/*  60 */       localBasicToggleButtonUI = new BasicToggleButtonUI();
/*  61 */       localAppContext.put(BASIC_TOGGLE_BUTTON_UI_KEY, localBasicToggleButtonUI);
/*     */     }
/*  63 */     return localBasicToggleButtonUI;
/*     */   }
/*     */ 
/*     */   protected String getPropertyPrefix() {
/*  67 */     return "ToggleButton.";
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*  75 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/*  76 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/*  78 */     Dimension localDimension = localAbstractButton.getSize();
/*  79 */     FontMetrics localFontMetrics = paramGraphics.getFontMetrics();
/*     */ 
/*  81 */     Insets localInsets = paramJComponent.getInsets();
/*     */ 
/*  83 */     Rectangle localRectangle1 = new Rectangle(localDimension);
/*     */ 
/*  85 */     localRectangle1.x += localInsets.left;
/*  86 */     localRectangle1.y += localInsets.top;
/*  87 */     localRectangle1.width -= localInsets.right + localRectangle1.x;
/*  88 */     localRectangle1.height -= localInsets.bottom + localRectangle1.y;
/*     */ 
/*  90 */     Rectangle localRectangle2 = new Rectangle();
/*  91 */     Rectangle localRectangle3 = new Rectangle();
/*     */ 
/*  93 */     Font localFont = paramJComponent.getFont();
/*  94 */     paramGraphics.setFont(localFont);
/*     */ 
/*  97 */     String str = SwingUtilities.layoutCompoundLabel(paramJComponent, localFontMetrics, localAbstractButton.getText(), localAbstractButton.getIcon(), localAbstractButton.getVerticalAlignment(), localAbstractButton.getHorizontalAlignment(), localAbstractButton.getVerticalTextPosition(), localAbstractButton.getHorizontalTextPosition(), localRectangle1, localRectangle2, localRectangle3, localAbstractButton.getText() == null ? 0 : localAbstractButton.getIconTextGap());
/*     */ 
/* 104 */     paramGraphics.setColor(localAbstractButton.getBackground());
/*     */ 
/* 106 */     if (((localButtonModel.isArmed()) && (localButtonModel.isPressed())) || (localButtonModel.isSelected())) {
/* 107 */       paintButtonPressed(paramGraphics, localAbstractButton);
/*     */     }
/*     */ 
/* 111 */     if (localAbstractButton.getIcon() != null) {
/* 112 */       paintIcon(paramGraphics, localAbstractButton, localRectangle2);
/*     */     }
/*     */ 
/* 116 */     if ((str != null) && (!str.equals(""))) {
/* 117 */       View localView = (View)paramJComponent.getClientProperty("html");
/* 118 */       if (localView != null)
/* 119 */         localView.paint(paramGraphics, localRectangle3);
/*     */       else {
/* 121 */         paintText(paramGraphics, localAbstractButton, localRectangle3, str);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 126 */     if ((localAbstractButton.isFocusPainted()) && (localAbstractButton.hasFocus()))
/* 127 */       paintFocus(paramGraphics, localAbstractButton, localRectangle1, localRectangle3, localRectangle2);
/*     */   }
/*     */ 
/*     */   protected void paintIcon(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle)
/*     */   {
/* 132 */     ButtonModel localButtonModel = paramAbstractButton.getModel();
/* 133 */     Icon localIcon = null;
/*     */ 
/* 135 */     if (!localButtonModel.isEnabled()) {
/* 136 */       if (localButtonModel.isSelected())
/* 137 */         localIcon = paramAbstractButton.getDisabledSelectedIcon();
/*     */       else
/* 139 */         localIcon = paramAbstractButton.getDisabledIcon();
/*     */     }
/* 141 */     else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 142 */       localIcon = paramAbstractButton.getPressedIcon();
/* 143 */       if (localIcon == null)
/*     */       {
/* 145 */         localIcon = paramAbstractButton.getSelectedIcon();
/*     */       }
/* 147 */     } else if (localButtonModel.isSelected()) {
/* 148 */       if ((paramAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover())) {
/* 149 */         localIcon = paramAbstractButton.getRolloverSelectedIcon();
/* 150 */         if (localIcon == null)
/* 151 */           localIcon = paramAbstractButton.getSelectedIcon();
/*     */       }
/*     */       else {
/* 154 */         localIcon = paramAbstractButton.getSelectedIcon();
/*     */       }
/* 156 */     } else if ((paramAbstractButton.isRolloverEnabled()) && (localButtonModel.isRollover())) {
/* 157 */       localIcon = paramAbstractButton.getRolloverIcon();
/*     */     }
/*     */ 
/* 160 */     if (localIcon == null) {
/* 161 */       localIcon = paramAbstractButton.getIcon();
/*     */     }
/*     */ 
/* 164 */     localIcon.paintIcon(paramAbstractButton, paramGraphics, paramRectangle.x, paramRectangle.y);
/*     */   }
/*     */ 
/*     */   protected int getTextShiftOffset()
/*     */   {
/* 172 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicToggleButtonUI
 * JD-Core Version:    0.6.2
 */
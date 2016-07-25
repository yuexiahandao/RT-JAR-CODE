/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicToggleButtonUI;
/*     */ import sun.awt.AppContext;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MetalToggleButtonUI extends BasicToggleButtonUI
/*     */ {
/*  60 */   private static final Object METAL_TOGGLE_BUTTON_UI_KEY = new Object();
/*     */   protected Color focusColor;
/*     */   protected Color selectColor;
/*     */   protected Color disabledTextColor;
/*  66 */   private boolean defaults_initialized = false;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  72 */     AppContext localAppContext = AppContext.getAppContext();
/*  73 */     MetalToggleButtonUI localMetalToggleButtonUI = (MetalToggleButtonUI)localAppContext.get(METAL_TOGGLE_BUTTON_UI_KEY);
/*     */ 
/*  75 */     if (localMetalToggleButtonUI == null) {
/*  76 */       localMetalToggleButtonUI = new MetalToggleButtonUI();
/*  77 */       localAppContext.put(METAL_TOGGLE_BUTTON_UI_KEY, localMetalToggleButtonUI);
/*     */     }
/*  79 */     return localMetalToggleButtonUI;
/*     */   }
/*     */ 
/*     */   public void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  86 */     super.installDefaults(paramAbstractButton);
/*  87 */     if (!this.defaults_initialized) {
/*  88 */       this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
/*  89 */       this.selectColor = UIManager.getColor(getPropertyPrefix() + "select");
/*  90 */       this.disabledTextColor = UIManager.getColor(getPropertyPrefix() + "disabledText");
/*  91 */       this.defaults_initialized = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/*  96 */     super.uninstallDefaults(paramAbstractButton);
/*  97 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   protected Color getSelectColor()
/*     */   {
/* 104 */     return this.selectColor;
/*     */   }
/*     */ 
/*     */   protected Color getDisabledTextColor() {
/* 108 */     return this.disabledTextColor;
/*     */   }
/*     */ 
/*     */   protected Color getFocusColor() {
/* 112 */     return this.focusColor;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 132 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 133 */     if (((paramJComponent.getBackground() instanceof UIResource)) && (localAbstractButton.isContentAreaFilled()) && (paramJComponent.isEnabled()))
/*     */     {
/* 135 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 136 */       if (!MetalUtils.isToolBarButton(paramJComponent)) {
/* 137 */         if ((!localButtonModel.isArmed()) && (!localButtonModel.isPressed()) && (MetalUtils.drawGradient(paramJComponent, paramGraphics, "ToggleButton.gradient", 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), true)))
/*     */         {
/* 141 */           paint(paramGraphics, paramJComponent);
/*     */         }
/*     */ 
/*     */       }
/* 145 */       else if (((localButtonModel.isRollover()) || (localButtonModel.isSelected())) && (MetalUtils.drawGradient(paramJComponent, paramGraphics, "ToggleButton.gradient", 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), true)))
/*     */       {
/* 148 */         paint(paramGraphics, paramJComponent);
/* 149 */         return;
/*     */       }
/*     */     }
/* 152 */     super.update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void paintButtonPressed(Graphics paramGraphics, AbstractButton paramAbstractButton) {
/* 156 */     if (paramAbstractButton.isContentAreaFilled()) {
/* 157 */       paramGraphics.setColor(getSelectColor());
/* 158 */       paramGraphics.fillRect(0, 0, paramAbstractButton.getWidth(), paramAbstractButton.getHeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle, String paramString) {
/* 163 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 164 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/* 165 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(localAbstractButton, paramGraphics);
/* 166 */     int i = localAbstractButton.getDisplayedMnemonicIndex();
/*     */ 
/* 169 */     if (localButtonModel.isEnabled())
/*     */     {
/* 171 */       paramGraphics.setColor(localAbstractButton.getForeground());
/*     */     }
/* 175 */     else if (localButtonModel.isSelected())
/* 176 */       paramGraphics.setColor(paramJComponent.getBackground());
/*     */     else {
/* 178 */       paramGraphics.setColor(getDisabledTextColor());
/*     */     }
/*     */ 
/* 181 */     SwingUtilities2.drawStringUnderlineCharAt(paramJComponent, paramGraphics, paramString, i, paramRectangle.x, paramRectangle.y + localFontMetrics.getAscent());
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3)
/*     */   {
/* 188 */     Rectangle localRectangle = new Rectangle();
/* 189 */     String str = paramAbstractButton.getText();
/* 190 */     int i = paramAbstractButton.getIcon() != null ? 1 : 0;
/*     */ 
/* 193 */     if ((str != null) && (!str.equals(""))) {
/* 194 */       if (i == 0) {
/* 195 */         localRectangle.setBounds(paramRectangle2);
/*     */       }
/*     */       else {
/* 198 */         localRectangle.setBounds(paramRectangle3.union(paramRectangle2));
/*     */       }
/*     */ 
/*     */     }
/* 202 */     else if (i != 0) {
/* 203 */       localRectangle.setBounds(paramRectangle3);
/*     */     }
/*     */ 
/* 206 */     paramGraphics.setColor(getFocusColor());
/* 207 */     paramGraphics.drawRect(localRectangle.x - 1, localRectangle.y - 1, localRectangle.width + 1, localRectangle.height + 1);
/*     */   }
/*     */ 
/*     */   protected void paintIcon(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle)
/*     */   {
/* 223 */     super.paintIcon(paramGraphics, paramAbstractButton, paramRectangle);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalToggleButtonUI
 * JD-Core Version:    0.6.2
 */
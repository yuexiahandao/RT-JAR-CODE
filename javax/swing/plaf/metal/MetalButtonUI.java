/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicButtonListener;
/*     */ import javax.swing.plaf.basic.BasicButtonUI;
/*     */ import sun.awt.AppContext;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class MetalButtonUI extends BasicButtonUI
/*     */ {
/*     */   protected Color focusColor;
/*     */   protected Color selectColor;
/*     */   protected Color disabledTextColor;
/*  60 */   private static final Object METAL_BUTTON_UI_KEY = new Object();
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  66 */     AppContext localAppContext = AppContext.getAppContext();
/*  67 */     MetalButtonUI localMetalButtonUI = (MetalButtonUI)localAppContext.get(METAL_BUTTON_UI_KEY);
/*     */ 
/*  69 */     if (localMetalButtonUI == null) {
/*  70 */       localMetalButtonUI = new MetalButtonUI();
/*  71 */       localAppContext.put(METAL_BUTTON_UI_KEY, localMetalButtonUI);
/*     */     }
/*  73 */     return localMetalButtonUI;
/*     */   }
/*     */ 
/*     */   public void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  80 */     super.installDefaults(paramAbstractButton);
/*     */   }
/*     */ 
/*     */   public void uninstallDefaults(AbstractButton paramAbstractButton) {
/*  84 */     super.uninstallDefaults(paramAbstractButton);
/*     */   }
/*     */ 
/*     */   protected BasicButtonListener createButtonListener(AbstractButton paramAbstractButton)
/*     */   {
/*  91 */     return super.createButtonListener(paramAbstractButton);
/*     */   }
/*     */ 
/*     */   protected Color getSelectColor()
/*     */   {
/*  99 */     this.selectColor = UIManager.getColor(getPropertyPrefix() + "select");
/* 100 */     return this.selectColor;
/*     */   }
/*     */ 
/*     */   protected Color getDisabledTextColor() {
/* 104 */     this.disabledTextColor = UIManager.getColor(getPropertyPrefix() + "disabledText");
/*     */ 
/* 106 */     return this.disabledTextColor;
/*     */   }
/*     */ 
/*     */   protected Color getFocusColor() {
/* 110 */     this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
/* 111 */     return this.focusColor;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 130 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 131 */     if (((paramJComponent.getBackground() instanceof UIResource)) && (localAbstractButton.isContentAreaFilled()) && (paramJComponent.isEnabled()))
/*     */     {
/* 133 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 134 */       if (!MetalUtils.isToolBarButton(paramJComponent)) {
/* 135 */         if ((!localButtonModel.isArmed()) && (!localButtonModel.isPressed()) && (MetalUtils.drawGradient(paramJComponent, paramGraphics, "Button.gradient", 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), true)))
/*     */         {
/* 139 */           paint(paramGraphics, paramJComponent);
/*     */         }
/*     */ 
/*     */       }
/* 143 */       else if ((localButtonModel.isRollover()) && (MetalUtils.drawGradient(paramJComponent, paramGraphics, "Button.gradient", 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), true)))
/*     */       {
/* 146 */         paint(paramGraphics, paramJComponent);
/* 147 */         return;
/*     */       }
/*     */     }
/* 150 */     super.update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void paintButtonPressed(Graphics paramGraphics, AbstractButton paramAbstractButton) {
/* 154 */     if (paramAbstractButton.isContentAreaFilled()) {
/* 155 */       Dimension localDimension = paramAbstractButton.getSize();
/* 156 */       paramGraphics.setColor(getSelectColor());
/* 157 */       paramGraphics.fillRect(0, 0, localDimension.width, localDimension.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3)
/*     */   {
/* 164 */     Rectangle localRectangle = new Rectangle();
/* 165 */     String str = paramAbstractButton.getText();
/* 166 */     int i = paramAbstractButton.getIcon() != null ? 1 : 0;
/*     */ 
/* 169 */     if ((str != null) && (!str.equals(""))) {
/* 170 */       if (i == 0) {
/* 171 */         localRectangle.setBounds(paramRectangle2);
/*     */       }
/*     */       else {
/* 174 */         localRectangle.setBounds(paramRectangle3.union(paramRectangle2));
/*     */       }
/*     */ 
/*     */     }
/* 178 */     else if (i != 0) {
/* 179 */       localRectangle.setBounds(paramRectangle3);
/*     */     }
/*     */ 
/* 182 */     paramGraphics.setColor(getFocusColor());
/* 183 */     paramGraphics.drawRect(localRectangle.x - 1, localRectangle.y - 1, localRectangle.width + 1, localRectangle.height + 1);
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle, String paramString)
/*     */   {
/* 190 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 191 */     ButtonModel localButtonModel = localAbstractButton.getModel();
/* 192 */     FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(paramJComponent, paramGraphics);
/* 193 */     int i = localAbstractButton.getDisplayedMnemonicIndex();
/*     */ 
/* 196 */     if (localButtonModel.isEnabled())
/*     */     {
/* 198 */       paramGraphics.setColor(localAbstractButton.getForeground());
/*     */     }
/*     */     else
/*     */     {
/* 202 */       paramGraphics.setColor(getDisabledTextColor());
/*     */     }
/* 204 */     SwingUtilities2.drawStringUnderlineCharAt(paramJComponent, paramGraphics, paramString, i, paramRectangle.x, paramRectangle.y + localFontMetrics.getAscent());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalButtonUI
 * JD-Core Version:    0.6.2
 */
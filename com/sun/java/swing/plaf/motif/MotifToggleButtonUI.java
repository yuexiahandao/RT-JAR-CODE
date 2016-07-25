/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicToggleButtonUI;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class MotifToggleButtonUI extends BasicToggleButtonUI
/*     */ {
/*  53 */   private static final Object MOTIF_TOGGLE_BUTTON_UI_KEY = new Object();
/*     */   protected Color selectColor;
/*  57 */   private boolean defaults_initialized = false;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  63 */     AppContext localAppContext = AppContext.getAppContext();
/*  64 */     MotifToggleButtonUI localMotifToggleButtonUI = (MotifToggleButtonUI)localAppContext.get(MOTIF_TOGGLE_BUTTON_UI_KEY);
/*     */ 
/*  66 */     if (localMotifToggleButtonUI == null) {
/*  67 */       localMotifToggleButtonUI = new MotifToggleButtonUI();
/*  68 */       localAppContext.put(MOTIF_TOGGLE_BUTTON_UI_KEY, localMotifToggleButtonUI);
/*     */     }
/*  70 */     return localMotifToggleButtonUI;
/*     */   }
/*     */ 
/*     */   public void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  77 */     super.installDefaults(paramAbstractButton);
/*  78 */     if (!this.defaults_initialized) {
/*  79 */       this.selectColor = UIManager.getColor(getPropertyPrefix() + "select");
/*  80 */       this.defaults_initialized = true;
/*     */     }
/*  82 */     LookAndFeel.installProperty(paramAbstractButton, "opaque", Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/*  86 */     super.uninstallDefaults(paramAbstractButton);
/*  87 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   protected Color getSelectColor()
/*     */   {
/*  95 */     return this.selectColor;
/*     */   }
/*     */ 
/*     */   protected void paintButtonPressed(Graphics paramGraphics, AbstractButton paramAbstractButton)
/*     */   {
/* 102 */     if (paramAbstractButton.isContentAreaFilled()) {
/* 103 */       Color localColor = paramGraphics.getColor();
/* 104 */       Dimension localDimension = paramAbstractButton.getSize();
/* 105 */       Insets localInsets1 = paramAbstractButton.getInsets();
/* 106 */       Insets localInsets2 = paramAbstractButton.getMargin();
/*     */ 
/* 108 */       if ((paramAbstractButton.getBackground() instanceof UIResource)) {
/* 109 */         paramGraphics.setColor(getSelectColor());
/*     */       }
/* 111 */       paramGraphics.fillRect(localInsets1.left - localInsets2.left, localInsets1.top - localInsets2.top, localDimension.width - (localInsets1.left - localInsets2.left) - (localInsets1.right - localInsets2.right), localDimension.height - (localInsets1.top - localInsets2.top) - (localInsets1.bottom - localInsets2.bottom));
/*     */ 
/* 115 */       paramGraphics.setColor(localColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Insets getInsets(JComponent paramJComponent) {
/* 120 */     Border localBorder = paramJComponent.getBorder();
/* 121 */     Insets localInsets = localBorder != null ? localBorder.getBorderInsets(paramJComponent) : new Insets(0, 0, 0, 0);
/* 122 */     return localInsets;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifToggleButtonUI
 * JD-Core Version:    0.6.2
 */
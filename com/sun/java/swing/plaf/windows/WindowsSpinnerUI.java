/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicSpinnerUI;
/*     */ 
/*     */ public class WindowsSpinnerUI extends BasicSpinnerUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  42 */     return new WindowsSpinnerUI();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*  50 */     if (XPStyle.getXP() != null) {
/*  51 */       paintXPBackground(paramGraphics, paramJComponent);
/*     */     }
/*  53 */     super.paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   private TMSchema.State getXPState(JComponent paramJComponent) {
/*  57 */     TMSchema.State localState = TMSchema.State.NORMAL;
/*  58 */     if (!paramJComponent.isEnabled()) {
/*  59 */       localState = TMSchema.State.DISABLED;
/*     */     }
/*  61 */     return localState;
/*     */   }
/*     */ 
/*     */   private void paintXPBackground(Graphics paramGraphics, JComponent paramJComponent) {
/*  65 */     XPStyle localXPStyle = XPStyle.getXP();
/*  66 */     if (localXPStyle == null) {
/*  67 */       return;
/*     */     }
/*  69 */     XPStyle.Skin localSkin = localXPStyle.getSkin(paramJComponent, TMSchema.Part.EP_EDIT);
/*  70 */     TMSchema.State localState = getXPState(paramJComponent);
/*  71 */     localSkin.paintSkin(paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), localState);
/*     */   }
/*     */ 
/*     */   protected Component createPreviousButton() {
/*  75 */     if (XPStyle.getXP() != null) {
/*  76 */       XPStyle.GlyphButton localGlyphButton = new XPStyle.GlyphButton(this.spinner, TMSchema.Part.SPNP_DOWN);
/*  77 */       Dimension localDimension = UIManager.getDimension("Spinner.arrowButtonSize");
/*  78 */       localGlyphButton.setPreferredSize(localDimension);
/*  79 */       localGlyphButton.setRequestFocusEnabled(false);
/*  80 */       installPreviousButtonListeners(localGlyphButton);
/*  81 */       return localGlyphButton;
/*     */     }
/*  83 */     return super.createPreviousButton();
/*     */   }
/*     */ 
/*     */   protected Component createNextButton() {
/*  87 */     if (XPStyle.getXP() != null) {
/*  88 */       XPStyle.GlyphButton localGlyphButton = new XPStyle.GlyphButton(this.spinner, TMSchema.Part.SPNP_UP);
/*  89 */       Dimension localDimension = UIManager.getDimension("Spinner.arrowButtonSize");
/*  90 */       localGlyphButton.setPreferredSize(localDimension);
/*  91 */       localGlyphButton.setRequestFocusEnabled(false);
/*  92 */       installNextButtonListeners(localGlyphButton);
/*  93 */       return localGlyphButton;
/*     */     }
/*  95 */     return super.createNextButton();
/*     */   }
/*     */ 
/*     */   private UIResource getUIResource(Object[] paramArrayOfObject) {
/*  99 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 100 */       if ((paramArrayOfObject[i] instanceof UIResource)) {
/* 101 */         return (UIResource)paramArrayOfObject[i];
/*     */       }
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsSpinnerUI
 * JD-Core Version:    0.6.2
 */
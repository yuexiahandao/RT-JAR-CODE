/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRadioButtonUI;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class MotifRadioButtonUI extends BasicRadioButtonUI
/*     */ {
/*  52 */   private static final Object MOTIF_RADIO_BUTTON_UI_KEY = new Object();
/*     */   protected Color focusColor;
/*  56 */   private boolean defaults_initialized = false;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  62 */     AppContext localAppContext = AppContext.getAppContext();
/*  63 */     MotifRadioButtonUI localMotifRadioButtonUI = (MotifRadioButtonUI)localAppContext.get(MOTIF_RADIO_BUTTON_UI_KEY);
/*     */ 
/*  65 */     if (localMotifRadioButtonUI == null) {
/*  66 */       localMotifRadioButtonUI = new MotifRadioButtonUI();
/*  67 */       localAppContext.put(MOTIF_RADIO_BUTTON_UI_KEY, localMotifRadioButtonUI);
/*     */     }
/*  69 */     return localMotifRadioButtonUI;
/*     */   }
/*     */ 
/*     */   public void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  76 */     super.installDefaults(paramAbstractButton);
/*  77 */     if (!this.defaults_initialized) {
/*  78 */       this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
/*  79 */       this.defaults_initialized = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/*  84 */     super.uninstallDefaults(paramAbstractButton);
/*  85 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   protected Color getFocusColor()
/*     */   {
/*  93 */     return this.focusColor;
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, Rectangle paramRectangle, Dimension paramDimension)
/*     */   {
/* 100 */     paramGraphics.setColor(getFocusColor());
/* 101 */     paramGraphics.drawRect(0, 0, paramDimension.width - 1, paramDimension.height - 1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifRadioButtonUI
 * JD-Core Version:    0.6.2
 */
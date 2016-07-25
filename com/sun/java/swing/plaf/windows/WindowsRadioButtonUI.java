/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ import javax.swing.plaf.basic.BasicRadioButtonUI;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class WindowsRadioButtonUI extends BasicRadioButtonUI
/*     */ {
/*  49 */   private static final Object WINDOWS_RADIO_BUTTON_UI_KEY = new Object();
/*     */   protected int dashedRectGapX;
/*     */   protected int dashedRectGapY;
/*     */   protected int dashedRectGapWidth;
/*     */   protected int dashedRectGapHeight;
/*     */   protected Color focusColor;
/*  58 */   private boolean initialized = false;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  64 */     AppContext localAppContext = AppContext.getAppContext();
/*  65 */     WindowsRadioButtonUI localWindowsRadioButtonUI = (WindowsRadioButtonUI)localAppContext.get(WINDOWS_RADIO_BUTTON_UI_KEY);
/*     */ 
/*  67 */     if (localWindowsRadioButtonUI == null) {
/*  68 */       localWindowsRadioButtonUI = new WindowsRadioButtonUI();
/*  69 */       localAppContext.put(WINDOWS_RADIO_BUTTON_UI_KEY, localWindowsRadioButtonUI);
/*     */     }
/*  71 */     return localWindowsRadioButtonUI;
/*     */   }
/*     */ 
/*     */   public void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  78 */     super.installDefaults(paramAbstractButton);
/*  79 */     if (!this.initialized) {
/*  80 */       this.dashedRectGapX = ((Integer)UIManager.get("Button.dashedRectGapX")).intValue();
/*  81 */       this.dashedRectGapY = ((Integer)UIManager.get("Button.dashedRectGapY")).intValue();
/*  82 */       this.dashedRectGapWidth = ((Integer)UIManager.get("Button.dashedRectGapWidth")).intValue();
/*  83 */       this.dashedRectGapHeight = ((Integer)UIManager.get("Button.dashedRectGapHeight")).intValue();
/*  84 */       this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
/*  85 */       this.initialized = true;
/*     */     }
/*  87 */     if (XPStyle.getXP() != null)
/*  88 */       LookAndFeel.installProperty(paramAbstractButton, "rolloverEnabled", Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  93 */     super.uninstallDefaults(paramAbstractButton);
/*  94 */     this.initialized = false;
/*     */   }
/*     */ 
/*     */   protected Color getFocusColor() {
/*  98 */     return this.focusColor;
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle, String paramString)
/*     */   {
/* 109 */     WindowsGraphicsUtils.paintText(paramGraphics, paramAbstractButton, paramRectangle, paramString, getTextShiftOffset());
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, Rectangle paramRectangle, Dimension paramDimension)
/*     */   {
/* 114 */     paramGraphics.setColor(getFocusColor());
/* 115 */     BasicGraphicsUtils.drawDashedRect(paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 122 */     Dimension localDimension = super.getPreferredSize(paramJComponent);
/*     */ 
/* 127 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 128 */     if ((localDimension != null) && (localAbstractButton.isFocusPainted())) {
/* 129 */       if (localDimension.width % 2 == 0) localDimension.width += 1;
/* 130 */       if (localDimension.height % 2 == 0) localDimension.height += 1;
/*     */     }
/* 132 */     return localDimension;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsRadioButtonUI
 * JD-Core Version:    0.6.2
 */
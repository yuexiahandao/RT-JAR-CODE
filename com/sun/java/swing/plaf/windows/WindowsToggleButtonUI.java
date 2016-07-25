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
/*     */ import javax.swing.plaf.basic.BasicToggleButtonUI;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class WindowsToggleButtonUI extends BasicToggleButtonUI
/*     */ {
/*     */   protected int dashedRectGapX;
/*     */   protected int dashedRectGapY;
/*     */   protected int dashedRectGapWidth;
/*     */   protected int dashedRectGapHeight;
/*     */   protected Color focusColor;
/*  61 */   private static final Object WINDOWS_TOGGLE_BUTTON_UI_KEY = new Object();
/*     */ 
/*  63 */   private boolean defaults_initialized = false;
/*     */ 
/* 115 */   private transient Color cachedSelectedColor = null;
/* 116 */   private transient Color cachedBackgroundColor = null;
/* 117 */   private transient Color cachedHighlightColor = null;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  66 */     AppContext localAppContext = AppContext.getAppContext();
/*  67 */     WindowsToggleButtonUI localWindowsToggleButtonUI = (WindowsToggleButtonUI)localAppContext.get(WINDOWS_TOGGLE_BUTTON_UI_KEY);
/*     */ 
/*  69 */     if (localWindowsToggleButtonUI == null) {
/*  70 */       localWindowsToggleButtonUI = new WindowsToggleButtonUI();
/*  71 */       localAppContext.put(WINDOWS_TOGGLE_BUTTON_UI_KEY, localWindowsToggleButtonUI);
/*     */     }
/*  73 */     return localWindowsToggleButtonUI;
/*     */   }
/*     */ 
/*     */   protected void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  81 */     super.installDefaults(paramAbstractButton);
/*  82 */     if (!this.defaults_initialized) {
/*  83 */       localObject = getPropertyPrefix();
/*  84 */       this.dashedRectGapX = ((Integer)UIManager.get("Button.dashedRectGapX")).intValue();
/*  85 */       this.dashedRectGapY = ((Integer)UIManager.get("Button.dashedRectGapY")).intValue();
/*  86 */       this.dashedRectGapWidth = ((Integer)UIManager.get("Button.dashedRectGapWidth")).intValue();
/*  87 */       this.dashedRectGapHeight = ((Integer)UIManager.get("Button.dashedRectGapHeight")).intValue();
/*  88 */       this.focusColor = UIManager.getColor((String)localObject + "focus");
/*  89 */       this.defaults_initialized = true;
/*     */     }
/*     */ 
/*  92 */     Object localObject = XPStyle.getXP();
/*  93 */     if (localObject != null) {
/*  94 */       paramAbstractButton.setBorder(((XPStyle)localObject).getBorder(paramAbstractButton, WindowsButtonUI.getXPButtonType(paramAbstractButton)));
/*  95 */       LookAndFeel.installProperty(paramAbstractButton, "opaque", Boolean.FALSE);
/*  96 */       LookAndFeel.installProperty(paramAbstractButton, "rolloverEnabled", Boolean.TRUE);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/* 101 */     super.uninstallDefaults(paramAbstractButton);
/* 102 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   protected Color getFocusColor()
/*     */   {
/* 107 */     return this.focusColor;
/*     */   }
/*     */ 
/*     */   protected void paintButtonPressed(Graphics paramGraphics, AbstractButton paramAbstractButton)
/*     */   {
/* 120 */     if ((XPStyle.getXP() == null) && (paramAbstractButton.isContentAreaFilled())) {
/* 121 */       Color localColor1 = paramGraphics.getColor();
/* 122 */       Color localColor2 = paramAbstractButton.getBackground();
/* 123 */       Color localColor3 = UIManager.getColor("ToggleButton.highlight");
/* 124 */       if ((localColor2 != this.cachedBackgroundColor) || (localColor3 != this.cachedHighlightColor)) {
/* 125 */         int i = localColor2.getRed(); int j = localColor3.getRed();
/* 126 */         int k = localColor2.getGreen(); int m = localColor3.getGreen();
/* 127 */         int n = localColor2.getBlue(); int i1 = localColor3.getBlue();
/* 128 */         this.cachedSelectedColor = new Color(Math.min(i, j) + Math.abs(i - j) / 2, Math.min(k, m) + Math.abs(k - m) / 2, Math.min(n, i1) + Math.abs(n - i1) / 2);
/*     */ 
/* 133 */         this.cachedBackgroundColor = localColor2;
/* 134 */         this.cachedHighlightColor = localColor3;
/*     */       }
/* 136 */       paramGraphics.setColor(this.cachedSelectedColor);
/* 137 */       paramGraphics.fillRect(0, 0, paramAbstractButton.getWidth(), paramAbstractButton.getHeight());
/* 138 */       paramGraphics.setColor(localColor1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/* 143 */     if (XPStyle.getXP() != null) {
/* 144 */       WindowsButtonUI.paintXPButtonBackground(paramGraphics, paramJComponent);
/*     */     }
/* 146 */     super.paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle, String paramString)
/*     */   {
/* 154 */     WindowsGraphicsUtils.paintText(paramGraphics, paramAbstractButton, paramRectangle, paramString, getTextShiftOffset());
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3)
/*     */   {
/* 159 */     paramGraphics.setColor(getFocusColor());
/* 160 */     BasicGraphicsUtils.drawDashedRect(paramGraphics, this.dashedRectGapX, this.dashedRectGapY, paramAbstractButton.getWidth() - this.dashedRectGapWidth, paramAbstractButton.getHeight() - this.dashedRectGapHeight);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 169 */     Dimension localDimension = super.getPreferredSize(paramJComponent);
/*     */ 
/* 174 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 175 */     if ((localDimension != null) && (localAbstractButton.isFocusPainted())) {
/* 176 */       if (localDimension.width % 2 == 0) localDimension.width += 1;
/* 177 */       if (localDimension.height % 2 == 0) localDimension.height += 1;
/*     */     }
/* 179 */     return localDimension;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsToggleButtonUI
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicButtonUI;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class WindowsButtonUI extends BasicButtonUI
/*     */ {
/*     */   protected int dashedRectGapX;
/*     */   protected int dashedRectGapY;
/*     */   protected int dashedRectGapWidth;
/*     */   protected int dashedRectGapHeight;
/*     */   protected Color focusColor;
/*  63 */   private boolean defaults_initialized = false;
/*     */ 
/*  65 */   private static final Object WINDOWS_BUTTON_UI_KEY = new Object();
/*     */ 
/* 161 */   private Rectangle viewRect = new Rectangle();
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  71 */     AppContext localAppContext = AppContext.getAppContext();
/*  72 */     WindowsButtonUI localWindowsButtonUI = (WindowsButtonUI)localAppContext.get(WINDOWS_BUTTON_UI_KEY);
/*     */ 
/*  74 */     if (localWindowsButtonUI == null) {
/*  75 */       localWindowsButtonUI = new WindowsButtonUI();
/*  76 */       localAppContext.put(WINDOWS_BUTTON_UI_KEY, localWindowsButtonUI);
/*     */     }
/*  78 */     return localWindowsButtonUI;
/*     */   }
/*     */ 
/*     */   protected void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  86 */     super.installDefaults(paramAbstractButton);
/*  87 */     if (!this.defaults_initialized) {
/*  88 */       localObject = getPropertyPrefix();
/*  89 */       this.dashedRectGapX = UIManager.getInt((String)localObject + "dashedRectGapX");
/*  90 */       this.dashedRectGapY = UIManager.getInt((String)localObject + "dashedRectGapY");
/*  91 */       this.dashedRectGapWidth = UIManager.getInt((String)localObject + "dashedRectGapWidth");
/*  92 */       this.dashedRectGapHeight = UIManager.getInt((String)localObject + "dashedRectGapHeight");
/*  93 */       this.focusColor = UIManager.getColor((String)localObject + "focus");
/*  94 */       this.defaults_initialized = true;
/*     */     }
/*     */ 
/*  97 */     Object localObject = XPStyle.getXP();
/*  98 */     if (localObject != null) {
/*  99 */       paramAbstractButton.setBorder(((XPStyle)localObject).getBorder(paramAbstractButton, getXPButtonType(paramAbstractButton)));
/* 100 */       LookAndFeel.installProperty(paramAbstractButton, "rolloverEnabled", Boolean.TRUE);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/* 105 */     super.uninstallDefaults(paramAbstractButton);
/* 106 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   protected Color getFocusColor() {
/* 110 */     return this.focusColor;
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle, String paramString)
/*     */   {
/* 121 */     WindowsGraphicsUtils.paintText(paramGraphics, paramAbstractButton, paramRectangle, paramString, getTextShiftOffset());
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3)
/*     */   {
/* 127 */     int i = paramAbstractButton.getWidth();
/* 128 */     int j = paramAbstractButton.getHeight();
/* 129 */     paramGraphics.setColor(getFocusColor());
/* 130 */     BasicGraphicsUtils.drawDashedRect(paramGraphics, this.dashedRectGapX, this.dashedRectGapY, i - this.dashedRectGapWidth, j - this.dashedRectGapHeight);
/*     */   }
/*     */ 
/*     */   protected void paintButtonPressed(Graphics paramGraphics, AbstractButton paramAbstractButton)
/*     */   {
/* 135 */     setTextShiftOffset();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 142 */     Dimension localDimension = super.getPreferredSize(paramJComponent);
/*     */ 
/* 147 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/* 148 */     if ((localDimension != null) && (localAbstractButton.isFocusPainted())) {
/* 149 */       if (localDimension.width % 2 == 0) localDimension.width += 1;
/* 150 */       if (localDimension.height % 2 == 0) localDimension.height += 1;
/*     */     }
/* 152 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 164 */     if (XPStyle.getXP() != null) {
/* 165 */       paintXPButtonBackground(paramGraphics, paramJComponent);
/*     */     }
/* 167 */     super.paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   static TMSchema.Part getXPButtonType(AbstractButton paramAbstractButton) {
/* 171 */     if ((paramAbstractButton instanceof JCheckBox)) {
/* 172 */       return TMSchema.Part.BP_CHECKBOX;
/*     */     }
/* 174 */     if ((paramAbstractButton instanceof JRadioButton)) {
/* 175 */       return TMSchema.Part.BP_RADIOBUTTON;
/*     */     }
/* 177 */     boolean bool = paramAbstractButton.getParent() instanceof JToolBar;
/* 178 */     return bool ? TMSchema.Part.TP_BUTTON : TMSchema.Part.BP_PUSHBUTTON;
/*     */   }
/*     */ 
/*     */   static TMSchema.State getXPButtonState(AbstractButton paramAbstractButton) {
/* 182 */     TMSchema.Part localPart = getXPButtonType(paramAbstractButton);
/* 183 */     ButtonModel localButtonModel = paramAbstractButton.getModel();
/* 184 */     TMSchema.State localState = TMSchema.State.NORMAL;
/* 185 */     switch (1.$SwitchMap$com$sun$java$swing$plaf$windows$TMSchema$Part[localPart.ordinal()])
/*     */     {
/*     */     case 1:
/*     */     case 2:
/* 189 */       if (!localButtonModel.isEnabled()) {
/* 190 */         localState = localButtonModel.isSelected() ? TMSchema.State.CHECKEDDISABLED : TMSchema.State.UNCHECKEDDISABLED;
/*     */       }
/* 192 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/* 193 */         localState = localButtonModel.isSelected() ? TMSchema.State.CHECKEDPRESSED : TMSchema.State.UNCHECKEDPRESSED;
/*     */       }
/* 195 */       else if (localButtonModel.isRollover()) {
/* 196 */         localState = localButtonModel.isSelected() ? TMSchema.State.CHECKEDHOT : TMSchema.State.UNCHECKEDHOT;
/*     */       }
/*     */       else {
/* 199 */         localState = localButtonModel.isSelected() ? TMSchema.State.CHECKEDNORMAL : TMSchema.State.UNCHECKEDNORMAL;
/*     */       }
/*     */ 
/* 202 */       break;
/*     */     case 3:
/*     */     case 4:
/* 206 */       boolean bool = paramAbstractButton.getParent() instanceof JToolBar;
/* 207 */       if (bool) {
/* 208 */         if ((localButtonModel.isArmed()) && (localButtonModel.isPressed()))
/* 209 */           localState = TMSchema.State.PRESSED;
/* 210 */         else if (!localButtonModel.isEnabled())
/* 211 */           localState = TMSchema.State.DISABLED;
/* 212 */         else if ((localButtonModel.isSelected()) && (localButtonModel.isRollover()))
/* 213 */           localState = TMSchema.State.HOTCHECKED;
/* 214 */         else if (localButtonModel.isSelected())
/* 215 */           localState = TMSchema.State.CHECKED;
/* 216 */         else if (localButtonModel.isRollover())
/* 217 */           localState = TMSchema.State.HOT;
/* 218 */         else if (paramAbstractButton.hasFocus()) {
/* 219 */           localState = TMSchema.State.HOT;
/*     */         }
/*     */       }
/* 222 */       else if (((localButtonModel.isArmed()) && (localButtonModel.isPressed())) || (localButtonModel.isSelected()))
/*     */       {
/* 224 */         localState = TMSchema.State.PRESSED;
/* 225 */       } else if (!localButtonModel.isEnabled())
/* 226 */         localState = TMSchema.State.DISABLED;
/* 227 */       else if ((localButtonModel.isRollover()) || (localButtonModel.isPressed()))
/* 228 */         localState = TMSchema.State.HOT;
/* 229 */       else if (((paramAbstractButton instanceof JButton)) && (((JButton)paramAbstractButton).isDefaultButton()))
/*     */       {
/* 231 */         localState = TMSchema.State.DEFAULTED;
/* 232 */       } else if (paramAbstractButton.hasFocus())
/* 233 */         localState = TMSchema.State.HOT; break;
/*     */     default:
/* 238 */       localState = TMSchema.State.NORMAL;
/*     */     }
/*     */ 
/* 241 */     return localState;
/*     */   }
/*     */ 
/*     */   static void paintXPButtonBackground(Graphics paramGraphics, JComponent paramJComponent) {
/* 245 */     AbstractButton localAbstractButton = (AbstractButton)paramJComponent;
/*     */ 
/* 247 */     XPStyle localXPStyle = XPStyle.getXP();
/*     */ 
/* 249 */     TMSchema.Part localPart = getXPButtonType(localAbstractButton);
/*     */ 
/* 251 */     if ((localAbstractButton.isContentAreaFilled()) && (localXPStyle != null))
/*     */     {
/* 253 */       XPStyle.Skin localSkin = localXPStyle.getSkin(localAbstractButton, localPart);
/*     */ 
/* 255 */       TMSchema.State localState = getXPButtonState(localAbstractButton);
/* 256 */       Dimension localDimension = paramJComponent.getSize();
/* 257 */       int i = 0;
/* 258 */       int j = 0;
/* 259 */       int k = localDimension.width;
/* 260 */       int m = localDimension.height;
/*     */ 
/* 262 */       Border localBorder = paramJComponent.getBorder();
/*     */       Insets localInsets;
/* 264 */       if (localBorder != null)
/*     */       {
/* 270 */         localInsets = getOpaqueInsets(localBorder, paramJComponent);
/*     */       }
/* 272 */       else localInsets = paramJComponent.getInsets();
/*     */ 
/* 274 */       if (localInsets != null) {
/* 275 */         i += localInsets.left;
/* 276 */         j += localInsets.top;
/* 277 */         k -= localInsets.left + localInsets.right;
/* 278 */         m -= localInsets.top + localInsets.bottom;
/*     */       }
/* 280 */       localSkin.paintSkin(paramGraphics, i, j, k, m, localState);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Insets getOpaqueInsets(Border paramBorder, Component paramComponent)
/*     */   {
/* 291 */     if (paramBorder == null) {
/* 292 */       return null;
/*     */     }
/* 294 */     if (paramBorder.isBorderOpaque())
/* 295 */       return paramBorder.getBorderInsets(paramComponent);
/* 296 */     if ((paramBorder instanceof CompoundBorder)) {
/* 297 */       CompoundBorder localCompoundBorder = (CompoundBorder)paramBorder;
/* 298 */       Insets localInsets1 = getOpaqueInsets(localCompoundBorder.getOutsideBorder(), paramComponent);
/* 299 */       if ((localInsets1 != null) && (localInsets1.equals(localCompoundBorder.getOutsideBorder().getBorderInsets(paramComponent))))
/*     */       {
/* 301 */         Insets localInsets2 = getOpaqueInsets(localCompoundBorder.getInsideBorder(), paramComponent);
/* 302 */         if (localInsets2 == null)
/*     */         {
/* 304 */           return localInsets1;
/*     */         }
/*     */ 
/* 308 */         return new Insets(localInsets1.top + localInsets2.top, localInsets1.left + localInsets2.left, localInsets1.bottom + localInsets2.bottom, localInsets1.right + localInsets2.right);
/*     */       }
/*     */ 
/* 314 */       return localInsets1;
/*     */     }
/*     */ 
/* 317 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsButtonUI
 * JD-Core Version:    0.6.2
 */
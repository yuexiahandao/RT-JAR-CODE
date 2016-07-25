/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicMenuItemUI;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class WindowsMenuItemUI extends BasicMenuItemUI
/*     */ {
/*  52 */   final WindowsMenuItemUIAccessor accessor = new WindowsMenuItemUIAccessor()
/*     */   {
/*     */     public JMenuItem getMenuItem()
/*     */     {
/*  56 */       return WindowsMenuItemUI.this.menuItem;
/*     */     }
/*     */ 
/*     */     public TMSchema.State getState(JMenuItem paramAnonymousJMenuItem) {
/*  60 */       return WindowsMenuItemUI.getState(this, paramAnonymousJMenuItem);
/*     */     }
/*     */ 
/*     */     public TMSchema.Part getPart(JMenuItem paramAnonymousJMenuItem) {
/*  64 */       return WindowsMenuItemUI.getPart(this, paramAnonymousJMenuItem);
/*     */     }
/*  52 */   };
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  68 */     return new WindowsMenuItemUI();
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, JMenuItem paramJMenuItem, Rectangle paramRectangle, String paramString)
/*     */   {
/*  81 */     if (isVistaPainting()) {
/*  82 */       paintText(this.accessor, paramGraphics, paramJMenuItem, paramRectangle, paramString);
/*  83 */       return;
/*     */     }
/*  85 */     ButtonModel localButtonModel = paramJMenuItem.getModel();
/*  86 */     Color localColor = paramGraphics.getColor();
/*     */ 
/*  88 */     if ((localButtonModel.isEnabled()) && ((localButtonModel.isArmed()) || (((paramJMenuItem instanceof JMenu)) && (localButtonModel.isSelected()))))
/*     */     {
/*  91 */       paramGraphics.setColor(this.selectionForeground);
/*     */     }
/*     */ 
/*  94 */     WindowsGraphicsUtils.paintText(paramGraphics, paramJMenuItem, paramRectangle, paramString, 0);
/*     */ 
/*  96 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics, JMenuItem paramJMenuItem, Color paramColor)
/*     */   {
/* 102 */     if (isVistaPainting()) {
/* 103 */       paintBackground(this.accessor, paramGraphics, paramJMenuItem, paramColor);
/* 104 */       return;
/*     */     }
/* 106 */     super.paintBackground(paramGraphics, paramJMenuItem, paramColor);
/*     */   }
/*     */ 
/*     */   static void paintBackground(WindowsMenuItemUIAccessor paramWindowsMenuItemUIAccessor, Graphics paramGraphics, JMenuItem paramJMenuItem, Color paramColor)
/*     */   {
/* 111 */     XPStyle localXPStyle = XPStyle.getXP();
/* 112 */     assert (isVistaPainting(localXPStyle));
/* 113 */     if (isVistaPainting(localXPStyle)) {
/* 114 */       int i = paramJMenuItem.getWidth();
/* 115 */       int j = paramJMenuItem.getHeight();
/* 116 */       if (paramJMenuItem.isOpaque()) {
/* 117 */         localObject = paramGraphics.getColor();
/* 118 */         paramGraphics.setColor(paramJMenuItem.getBackground());
/* 119 */         paramGraphics.fillRect(0, 0, i, j);
/* 120 */         paramGraphics.setColor((Color)localObject);
/*     */       }
/* 122 */       Object localObject = paramWindowsMenuItemUIAccessor.getPart(paramJMenuItem);
/* 123 */       XPStyle.Skin localSkin = localXPStyle.getSkin(paramJMenuItem, (TMSchema.Part)localObject);
/* 124 */       localSkin.paintSkin(paramGraphics, 0, 0, i, j, paramWindowsMenuItemUIAccessor.getState(paramJMenuItem));
/*     */     }
/*     */   }
/*     */ 
/*     */   static void paintText(WindowsMenuItemUIAccessor paramWindowsMenuItemUIAccessor, Graphics paramGraphics, JMenuItem paramJMenuItem, Rectangle paramRectangle, String paramString)
/*     */   {
/* 134 */     assert (isVistaPainting());
/* 135 */     if (isVistaPainting()) {
/* 136 */       TMSchema.State localState = paramWindowsMenuItemUIAccessor.getState(paramJMenuItem);
/*     */ 
/* 139 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(paramJMenuItem, paramGraphics);
/* 140 */       int i = paramJMenuItem.getDisplayedMnemonicIndex();
/*     */ 
/* 142 */       if (WindowsLookAndFeel.isMnemonicHidden() == true) {
/* 143 */         i = -1;
/*     */       }
/* 145 */       WindowsGraphicsUtils.paintXPText(paramJMenuItem, paramWindowsMenuItemUIAccessor.getPart(paramJMenuItem), localState, paramGraphics, paramRectangle.x, paramRectangle.y + localFontMetrics.getAscent(), paramString, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   static TMSchema.State getState(WindowsMenuItemUIAccessor paramWindowsMenuItemUIAccessor, JMenuItem paramJMenuItem)
/*     */   {
/* 155 */     ButtonModel localButtonModel = paramJMenuItem.getModel();
/*     */     TMSchema.State localState;
/* 156 */     if (localButtonModel.isArmed())
/* 157 */       localState = localButtonModel.isEnabled() ? TMSchema.State.HOT : TMSchema.State.DISABLEDHOT;
/*     */     else {
/* 159 */       localState = localButtonModel.isEnabled() ? TMSchema.State.NORMAL : TMSchema.State.DISABLED;
/*     */     }
/* 161 */     return localState;
/*     */   }
/*     */ 
/*     */   static TMSchema.Part getPart(WindowsMenuItemUIAccessor paramWindowsMenuItemUIAccessor, JMenuItem paramJMenuItem) {
/* 165 */     return TMSchema.Part.MP_POPUPITEM;
/*     */   }
/*     */ 
/*     */   static boolean isVistaPainting(XPStyle paramXPStyle)
/*     */   {
/* 174 */     return (paramXPStyle != null) && (paramXPStyle.isSkinDefined(null, TMSchema.Part.MP_POPUPITEM));
/*     */   }
/*     */ 
/*     */   static boolean isVistaPainting() {
/* 178 */     return isVistaPainting(XPStyle.getXP());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsMenuItemUI
 * JD-Core Version:    0.6.2
 */
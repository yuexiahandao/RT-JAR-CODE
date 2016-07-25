/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRadioButtonMenuItemUI;
/*     */ 
/*     */ public class WindowsRadioButtonMenuItemUI extends BasicRadioButtonMenuItemUI
/*     */ {
/*  48 */   final WindowsMenuItemUIAccessor accessor = new WindowsMenuItemUIAccessor()
/*     */   {
/*     */     public JMenuItem getMenuItem()
/*     */     {
/*  52 */       return WindowsRadioButtonMenuItemUI.this.menuItem;
/*     */     }
/*     */ 
/*     */     public TMSchema.State getState(JMenuItem paramAnonymousJMenuItem) {
/*  56 */       return WindowsMenuItemUI.getState(this, paramAnonymousJMenuItem);
/*     */     }
/*     */ 
/*     */     public TMSchema.Part getPart(JMenuItem paramAnonymousJMenuItem) {
/*  60 */       return WindowsMenuItemUI.getPart(this, paramAnonymousJMenuItem);
/*     */     }
/*  48 */   };
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  64 */     return new WindowsRadioButtonMenuItemUI();
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics, JMenuItem paramJMenuItem, Color paramColor)
/*     */   {
/*  70 */     if (WindowsMenuItemUI.isVistaPainting()) {
/*  71 */       WindowsMenuItemUI.paintBackground(this.accessor, paramGraphics, paramJMenuItem, paramColor);
/*  72 */       return;
/*     */     }
/*  74 */     super.paintBackground(paramGraphics, paramJMenuItem, paramColor);
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, JMenuItem paramJMenuItem, Rectangle paramRectangle, String paramString)
/*     */   {
/*  88 */     if (WindowsMenuItemUI.isVistaPainting()) {
/*  89 */       WindowsMenuItemUI.paintText(this.accessor, paramGraphics, paramJMenuItem, paramRectangle, paramString);
/*  90 */       return;
/*     */     }
/*  92 */     ButtonModel localButtonModel = paramJMenuItem.getModel();
/*  93 */     Color localColor = paramGraphics.getColor();
/*     */ 
/*  95 */     if ((localButtonModel.isEnabled()) && (localButtonModel.isArmed())) {
/*  96 */       paramGraphics.setColor(this.selectionForeground);
/*     */     }
/*     */ 
/*  99 */     WindowsGraphicsUtils.paintText(paramGraphics, paramJMenuItem, paramRectangle, paramString, 0);
/*     */ 
/* 101 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsRadioButtonMenuItemUI
 * JD-Core Version:    0.6.2
 */
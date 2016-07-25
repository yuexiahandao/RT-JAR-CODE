/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;
/*     */ 
/*     */ public class WindowsCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI
/*     */ {
/*  49 */   final WindowsMenuItemUIAccessor accessor = new WindowsMenuItemUIAccessor()
/*     */   {
/*     */     public JMenuItem getMenuItem()
/*     */     {
/*  53 */       return WindowsCheckBoxMenuItemUI.this.menuItem;
/*     */     }
/*     */ 
/*     */     public TMSchema.State getState(JMenuItem paramAnonymousJMenuItem) {
/*  57 */       return WindowsMenuItemUI.getState(this, paramAnonymousJMenuItem);
/*     */     }
/*     */ 
/*     */     public TMSchema.Part getPart(JMenuItem paramAnonymousJMenuItem) {
/*  61 */       return WindowsMenuItemUI.getPart(this, paramAnonymousJMenuItem);
/*     */     }
/*  49 */   };
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  65 */     return new WindowsCheckBoxMenuItemUI();
/*     */   }
/*     */ 
/*     */   protected void paintBackground(Graphics paramGraphics, JMenuItem paramJMenuItem, Color paramColor)
/*     */   {
/*  71 */     if (WindowsMenuItemUI.isVistaPainting()) {
/*  72 */       WindowsMenuItemUI.paintBackground(this.accessor, paramGraphics, paramJMenuItem, paramColor);
/*  73 */       return;
/*     */     }
/*  75 */     super.paintBackground(paramGraphics, paramJMenuItem, paramColor);
/*     */   }
/*     */ 
/*     */   protected void paintText(Graphics paramGraphics, JMenuItem paramJMenuItem, Rectangle paramRectangle, String paramString)
/*     */   {
/*  88 */     if (WindowsMenuItemUI.isVistaPainting()) {
/*  89 */       WindowsMenuItemUI.paintText(this.accessor, paramGraphics, paramJMenuItem, paramRectangle, paramString);
/*     */ 
/*  91 */       return;
/*     */     }
/*  93 */     ButtonModel localButtonModel = paramJMenuItem.getModel();
/*  94 */     Color localColor = paramGraphics.getColor();
/*     */ 
/*  96 */     if ((localButtonModel.isEnabled()) && (localButtonModel.isArmed())) {
/*  97 */       paramGraphics.setColor(this.selectionForeground);
/*     */     }
/*     */ 
/* 100 */     WindowsGraphicsUtils.paintText(paramGraphics, paramJMenuItem, paramRectangle, paramString, 0);
/*     */ 
/* 102 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsCheckBoxMenuItemUI
 * JD-Core Version:    0.6.2
 */
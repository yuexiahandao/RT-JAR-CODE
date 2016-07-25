/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.DesktopManager;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameUI;
/*     */ 
/*     */ public class WindowsInternalFrameUI extends BasicInternalFrameUI
/*     */ {
/*  50 */   XPStyle xp = XPStyle.getXP();
/*     */ 
/*     */   public void installDefaults() {
/*  53 */     super.installDefaults();
/*     */ 
/*  55 */     if (this.xp != null)
/*  56 */       this.frame.setBorder(new XPBorder(null));
/*     */     else
/*  58 */       this.frame.setBorder(UIManager.getBorder("InternalFrame.border"));
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  63 */     super.installUI(paramJComponent);
/*     */ 
/*  65 */     LookAndFeel.installProperty(paramJComponent, "opaque", this.xp == null ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   public void uninstallDefaults()
/*     */   {
/*  70 */     this.frame.setBorder(null);
/*  71 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  75 */     return new WindowsInternalFrameUI((JInternalFrame)paramJComponent);
/*     */   }
/*     */ 
/*     */   public WindowsInternalFrameUI(JInternalFrame paramJInternalFrame) {
/*  79 */     super(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   protected DesktopManager createDesktopManager() {
/*  83 */     return new WindowsDesktopManager();
/*     */   }
/*     */ 
/*     */   protected JComponent createNorthPane(JInternalFrame paramJInternalFrame) {
/*  87 */     this.titlePane = new WindowsInternalFrameTitlePane(paramJInternalFrame);
/*  88 */     return this.titlePane;
/*     */   }
/*     */ 
/*     */   private class XPBorder extends AbstractBorder {
/*  92 */     private XPStyle.Skin leftSkin = WindowsInternalFrameUI.this.xp.getSkin(WindowsInternalFrameUI.this.frame, TMSchema.Part.WP_FRAMELEFT);
/*  93 */     private XPStyle.Skin rightSkin = WindowsInternalFrameUI.this.xp.getSkin(WindowsInternalFrameUI.this.frame, TMSchema.Part.WP_FRAMERIGHT);
/*  94 */     private XPStyle.Skin bottomSkin = WindowsInternalFrameUI.this.xp.getSkin(WindowsInternalFrameUI.this.frame, TMSchema.Part.WP_FRAMEBOTTOM);
/*     */ 
/*     */     private XPBorder()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 103 */       TMSchema.State localState = ((JInternalFrame)paramComponent).isSelected() ? TMSchema.State.ACTIVE : TMSchema.State.INACTIVE;
/* 104 */       int i = WindowsInternalFrameUI.this.titlePane != null ? WindowsInternalFrameUI.this.titlePane.getSize().height : 0;
/*     */ 
/* 106 */       this.bottomSkin.paintSkin(paramGraphics, 0, paramInt4 - this.bottomSkin.getHeight(), paramInt3, this.bottomSkin.getHeight(), localState);
/*     */ 
/* 110 */       this.leftSkin.paintSkin(paramGraphics, 0, i - 1, this.leftSkin.getWidth(), paramInt4 - i - this.bottomSkin.getHeight() + 2, localState);
/*     */ 
/* 114 */       this.rightSkin.paintSkin(paramGraphics, paramInt3 - this.rightSkin.getWidth(), i - 1, this.rightSkin.getWidth(), paramInt4 - i - this.bottomSkin.getHeight() + 2, localState);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 121 */       paramInsets.top = 4;
/* 122 */       paramInsets.left = this.leftSkin.getWidth();
/* 123 */       paramInsets.right = this.rightSkin.getWidth();
/* 124 */       paramInsets.bottom = this.bottomSkin.getHeight();
/*     */ 
/* 126 */       return paramInsets;
/*     */     }
/*     */ 
/*     */     public boolean isBorderOpaque() {
/* 130 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsInternalFrameUI
 * JD-Core Version:    0.6.2
 */
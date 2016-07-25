/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicButtonListener;
/*     */ import javax.swing.plaf.basic.BasicButtonUI;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class MotifButtonUI extends BasicButtonUI
/*     */ {
/*     */   protected Color selectColor;
/*  53 */   private boolean defaults_initialized = false;
/*     */ 
/*  55 */   private static final Object MOTIF_BUTTON_UI_KEY = new Object();
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  61 */     AppContext localAppContext = AppContext.getAppContext();
/*  62 */     MotifButtonUI localMotifButtonUI = (MotifButtonUI)localAppContext.get(MOTIF_BUTTON_UI_KEY);
/*     */ 
/*  64 */     if (localMotifButtonUI == null) {
/*  65 */       localMotifButtonUI = new MotifButtonUI();
/*  66 */       localAppContext.put(MOTIF_BUTTON_UI_KEY, localMotifButtonUI);
/*     */     }
/*  68 */     return localMotifButtonUI;
/*     */   }
/*     */ 
/*     */   protected BasicButtonListener createButtonListener(AbstractButton paramAbstractButton)
/*     */   {
/*  75 */     return new MotifButtonListener(paramAbstractButton);
/*     */   }
/*     */ 
/*     */   public void installDefaults(AbstractButton paramAbstractButton)
/*     */   {
/*  82 */     super.installDefaults(paramAbstractButton);
/*  83 */     if (!this.defaults_initialized) {
/*  84 */       this.selectColor = UIManager.getColor(getPropertyPrefix() + "select");
/*  85 */       this.defaults_initialized = true;
/*     */     }
/*  87 */     LookAndFeel.installProperty(paramAbstractButton, "opaque", Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(AbstractButton paramAbstractButton) {
/*  91 */     super.uninstallDefaults(paramAbstractButton);
/*  92 */     this.defaults_initialized = false;
/*     */   }
/*     */ 
/*     */   protected Color getSelectColor()
/*     */   {
/* 100 */     return this.selectColor;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 107 */     fillContentArea(paramGraphics, (AbstractButton)paramJComponent, paramJComponent.getBackground());
/* 108 */     super.paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void paintIcon(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*     */   {
/* 113 */     Shape localShape = paramGraphics.getClip();
/* 114 */     Rectangle localRectangle1 = AbstractBorder.getInteriorRectangle(paramJComponent, paramJComponent.getBorder(), 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 118 */     Rectangle localRectangle2 = localShape.getBounds();
/* 119 */     localRectangle1 = SwingUtilities.computeIntersection(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height, localRectangle1);
/*     */ 
/* 122 */     paramGraphics.setClip(localRectangle1);
/* 123 */     super.paintIcon(paramGraphics, paramJComponent, paramRectangle);
/* 124 */     paramGraphics.setClip(localShape);
/*     */   }
/*     */ 
/*     */   protected void paintFocus(Graphics paramGraphics, AbstractButton paramAbstractButton, Rectangle paramRectangle1, Rectangle paramRectangle2, Rectangle paramRectangle3)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void paintButtonPressed(Graphics paramGraphics, AbstractButton paramAbstractButton)
/*     */   {
/* 133 */     fillContentArea(paramGraphics, paramAbstractButton, this.selectColor);
/*     */   }
/*     */ 
/*     */   protected void fillContentArea(Graphics paramGraphics, AbstractButton paramAbstractButton, Color paramColor)
/*     */   {
/* 139 */     if (paramAbstractButton.isContentAreaFilled()) {
/* 140 */       Insets localInsets1 = paramAbstractButton.getMargin();
/* 141 */       Insets localInsets2 = paramAbstractButton.getInsets();
/* 142 */       Dimension localDimension = paramAbstractButton.getSize();
/* 143 */       paramGraphics.setColor(paramColor);
/* 144 */       paramGraphics.fillRect(localInsets2.left - localInsets1.left, localInsets2.top - localInsets1.top, localDimension.width - (localInsets2.left - localInsets1.left) - (localInsets2.right - localInsets1.right), localDimension.height - (localInsets2.top - localInsets1.top) - (localInsets2.bottom - localInsets1.bottom));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifButtonUI
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI;
/*     */ 
/*     */ public class MotifScrollBarUI extends BasicScrollBarUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     return new MotifScrollBarUI();
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent) {
/*  61 */     Insets localInsets = paramJComponent.getInsets();
/*  62 */     int i = localInsets.left + localInsets.right;
/*  63 */     int j = localInsets.top + localInsets.bottom;
/*  64 */     return this.scrollbar.getOrientation() == 1 ? new Dimension(i + 11, j + 33) : new Dimension(i + 33, j + 11);
/*     */   }
/*     */ 
/*     */   protected JButton createDecreaseButton(int paramInt)
/*     */   {
/*  70 */     return new MotifScrollBarButton(paramInt);
/*     */   }
/*     */ 
/*     */   protected JButton createIncreaseButton(int paramInt) {
/*  74 */     return new MotifScrollBarButton(paramInt);
/*     */   }
/*     */ 
/*     */   public void paintTrack(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*     */   {
/*  79 */     paramGraphics.setColor(this.trackColor);
/*  80 */     paramGraphics.fillRect(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */   }
/*     */ 
/*     */   public void paintThumb(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*     */   {
/*  87 */     if ((paramRectangle.isEmpty()) || (!this.scrollbar.isEnabled())) {
/*  88 */       return;
/*     */     }
/*     */ 
/*  91 */     int i = paramRectangle.width;
/*  92 */     int j = paramRectangle.height;
/*     */ 
/*  94 */     paramGraphics.translate(paramRectangle.x, paramRectangle.y);
/*  95 */     paramGraphics.setColor(this.thumbColor);
/*  96 */     paramGraphics.fillRect(0, 0, i - 1, j - 1);
/*     */ 
/*  98 */     paramGraphics.setColor(this.thumbHighlightColor);
/*  99 */     paramGraphics.drawLine(0, 0, 0, j - 1);
/* 100 */     paramGraphics.drawLine(1, 0, i - 1, 0);
/*     */ 
/* 102 */     paramGraphics.setColor(this.thumbLightShadowColor);
/* 103 */     paramGraphics.drawLine(1, j - 1, i - 1, j - 1);
/* 104 */     paramGraphics.drawLine(i - 1, 1, i - 1, j - 2);
/*     */ 
/* 106 */     paramGraphics.translate(-paramRectangle.x, -paramRectangle.y);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifScrollBarUI
 * JD-Core Version:    0.6.2
 */
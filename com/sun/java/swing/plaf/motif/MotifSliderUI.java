/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSliderUI;
/*     */ 
/*     */ public class MotifSliderUI extends BasicSliderUI
/*     */ {
/*  51 */   static final Dimension PREFERRED_HORIZONTAL_SIZE = new Dimension(164, 15);
/*  52 */   static final Dimension PREFERRED_VERTICAL_SIZE = new Dimension(15, 164);
/*     */ 
/*  54 */   static final Dimension MINIMUM_HORIZONTAL_SIZE = new Dimension(43, 15);
/*  55 */   static final Dimension MINIMUM_VERTICAL_SIZE = new Dimension(15, 43);
/*     */ 
/*     */   public MotifSliderUI(JSlider paramJSlider)
/*     */   {
/*  61 */     super(paramJSlider);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  68 */     return new MotifSliderUI((JSlider)paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredHorizontalSize() {
/*  72 */     return PREFERRED_HORIZONTAL_SIZE;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredVerticalSize() {
/*  76 */     return PREFERRED_VERTICAL_SIZE;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumHorizontalSize() {
/*  80 */     return MINIMUM_HORIZONTAL_SIZE;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumVerticalSize() {
/*  84 */     return MINIMUM_VERTICAL_SIZE;
/*     */   }
/*     */ 
/*     */   protected Dimension getThumbSize() {
/*  88 */     if (this.slider.getOrientation() == 0) {
/*  89 */       return new Dimension(30, 15);
/*     */     }
/*     */ 
/*  92 */     return new Dimension(15, 30);
/*     */   }
/*     */ 
/*     */   public void paintFocus(Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintTrack(Graphics paramGraphics) {
/*     */   }
/*     */ 
/*     */   public void paintThumb(Graphics paramGraphics) {
/* 103 */     Rectangle localRectangle = this.thumbRect;
/*     */ 
/* 105 */     int i = localRectangle.x;
/* 106 */     int j = localRectangle.y;
/* 107 */     int k = localRectangle.width;
/* 108 */     int m = localRectangle.height;
/*     */ 
/* 110 */     if (this.slider.isEnabled()) {
/* 111 */       paramGraphics.setColor(this.slider.getForeground());
/*     */     }
/*     */     else
/*     */     {
/* 115 */       paramGraphics.setColor(this.slider.getForeground().darker());
/*     */     }
/*     */ 
/* 118 */     if (this.slider.getOrientation() == 0) {
/* 119 */       paramGraphics.translate(i, localRectangle.y - 1);
/*     */ 
/* 122 */       paramGraphics.fillRect(0, 1, k, m - 1);
/*     */ 
/* 125 */       paramGraphics.setColor(getHighlightColor());
/* 126 */       paramGraphics.drawLine(0, 1, k - 1, 1);
/* 127 */       paramGraphics.drawLine(0, 1, 0, m);
/* 128 */       paramGraphics.drawLine(k / 2, 2, k / 2, m - 1);
/*     */ 
/* 131 */       paramGraphics.setColor(getShadowColor());
/* 132 */       paramGraphics.drawLine(0, m, k - 1, m);
/* 133 */       paramGraphics.drawLine(k - 1, 1, k - 1, m);
/* 134 */       paramGraphics.drawLine(k / 2 - 1, 2, k / 2 - 1, m);
/*     */ 
/* 136 */       paramGraphics.translate(-i, -(localRectangle.y - 1));
/*     */     }
/*     */     else {
/* 139 */       paramGraphics.translate(localRectangle.x - 1, 0);
/*     */ 
/* 142 */       paramGraphics.fillRect(1, j, k - 1, m);
/*     */ 
/* 145 */       paramGraphics.setColor(getHighlightColor());
/* 146 */       paramGraphics.drawLine(1, j, k, j);
/* 147 */       paramGraphics.drawLine(1, j + 1, 1, j + m - 1);
/* 148 */       paramGraphics.drawLine(2, j + m / 2, k - 1, j + m / 2);
/*     */ 
/* 151 */       paramGraphics.setColor(getShadowColor());
/* 152 */       paramGraphics.drawLine(2, j + m - 1, k, j + m - 1);
/* 153 */       paramGraphics.drawLine(k, j + m - 1, k, j);
/* 154 */       paramGraphics.drawLine(2, j + m / 2 - 1, k - 1, j + m / 2 - 1);
/*     */ 
/* 156 */       paramGraphics.translate(-(localRectangle.x - 1), 0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifSliderUI
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.basic.BasicArrowButton;
/*     */ 
/*     */ public class MotifScrollBarButton extends BasicArrowButton
/*     */ {
/*  48 */   private Color darkShadow = UIManager.getColor("controlShadow");
/*  49 */   private Color lightShadow = UIManager.getColor("controlLtHighlight");
/*     */ 
/*     */   public MotifScrollBarButton(int paramInt)
/*     */   {
/*  54 */     super(paramInt);
/*     */ 
/*  56 */     switch (paramInt) {
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/*     */     case 7:
/*  61 */       this.direction = paramInt;
/*  62 */       break;
/*     */     case 2:
/*     */     case 4:
/*     */     case 6:
/*     */     default:
/*  64 */       throw new IllegalArgumentException("invalid direction");
/*     */     }
/*     */ 
/*  67 */     setRequestFocusEnabled(false);
/*  68 */     setOpaque(true);
/*  69 */     setBackground(UIManager.getColor("ScrollBar.background"));
/*  70 */     setForeground(UIManager.getColor("ScrollBar.foreground"));
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/*  75 */     switch (this.direction) {
/*     */     case 1:
/*     */     case 5:
/*  78 */       return new Dimension(11, 12);
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 6:
/*  82 */     case 7: } return new Dimension(12, 11);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/*  87 */     return getPreferredSize();
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize() {
/*  91 */     return getPreferredSize();
/*     */   }
/*     */ 
/*     */   public boolean isFocusTraversable() {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/* 100 */     int i = getWidth();
/* 101 */     int j = getHeight();
/*     */ 
/* 103 */     if (isOpaque()) {
/* 104 */       paramGraphics.setColor(getBackground());
/* 105 */       paramGraphics.fillRect(0, 0, i, j);
/*     */     }
/*     */ 
/* 108 */     boolean bool = getModel().isPressed();
/* 109 */     Color localColor1 = bool ? this.darkShadow : this.lightShadow;
/* 110 */     Color localColor2 = bool ? this.lightShadow : this.darkShadow;
/* 111 */     Color localColor3 = getBackground();
/*     */ 
/* 113 */     int k = i / 2;
/* 114 */     int m = j / 2;
/* 115 */     int n = Math.min(i, j);
/*     */     int i1;
/*     */     int i2;
/*     */     int i3;
/* 117 */     switch (this.direction) {
/*     */     case 1:
/* 119 */       paramGraphics.setColor(localColor1);
/* 120 */       paramGraphics.drawLine(k, 0, k, 0);
/* 121 */       i1 = k - 1; i2 = 1; for (i3 = 1; i2 <= n - 2; i2 += 2) {
/* 122 */         paramGraphics.setColor(localColor1);
/* 123 */         paramGraphics.drawLine(i1, i2, i1, i2);
/* 124 */         if (i2 >= n - 2) {
/* 125 */           paramGraphics.drawLine(i1, i2 + 1, i1, i2 + 1);
/*     */         }
/* 127 */         paramGraphics.setColor(localColor3);
/* 128 */         paramGraphics.drawLine(i1 + 1, i2, i1 + i3, i2);
/* 129 */         if (i2 < n - 2) {
/* 130 */           paramGraphics.drawLine(i1, i2 + 1, i1 + i3 + 1, i2 + 1);
/*     */         }
/* 132 */         paramGraphics.setColor(localColor2);
/* 133 */         paramGraphics.drawLine(i1 + i3 + 1, i2, i1 + i3 + 1, i2);
/* 134 */         if (i2 >= n - 2) {
/* 135 */           paramGraphics.drawLine(i1 + 1, i2 + 1, i1 + i3 + 1, i2 + 1);
/*     */         }
/* 137 */         i3 += 2;
/* 138 */         i1--;
/*     */       }
/* 140 */       break;
/*     */     case 5:
/* 143 */       paramGraphics.setColor(localColor2);
/* 144 */       paramGraphics.drawLine(k, n, k, n);
/* 145 */       i1 = k - 1; i2 = n - 1; for (i3 = 1; i2 >= 1; i2 -= 2) {
/* 146 */         paramGraphics.setColor(localColor1);
/* 147 */         paramGraphics.drawLine(i1, i2, i1, i2);
/* 148 */         if (i2 <= 2) {
/* 149 */           paramGraphics.drawLine(i1, i2 - 1, i1 + i3 + 1, i2 - 1);
/*     */         }
/* 151 */         paramGraphics.setColor(localColor3);
/* 152 */         paramGraphics.drawLine(i1 + 1, i2, i1 + i3, i2);
/* 153 */         if (i2 > 2) {
/* 154 */           paramGraphics.drawLine(i1, i2 - 1, i1 + i3 + 1, i2 - 1);
/*     */         }
/* 156 */         paramGraphics.setColor(localColor2);
/* 157 */         paramGraphics.drawLine(i1 + i3 + 1, i2, i1 + i3 + 1, i2);
/*     */ 
/* 159 */         i3 += 2;
/* 160 */         i1--;
/*     */       }
/* 162 */       break;
/*     */     case 3:
/* 165 */       paramGraphics.setColor(localColor1);
/* 166 */       paramGraphics.drawLine(n, m, n, m);
/* 167 */       i1 = m - 1; i2 = n - 1; for (i3 = 1; i2 >= 1; i2 -= 2) {
/* 168 */         paramGraphics.setColor(localColor1);
/* 169 */         paramGraphics.drawLine(i2, i1, i2, i1);
/* 170 */         if (i2 <= 2) {
/* 171 */           paramGraphics.drawLine(i2 - 1, i1, i2 - 1, i1 + i3 + 1);
/*     */         }
/* 173 */         paramGraphics.setColor(localColor3);
/* 174 */         paramGraphics.drawLine(i2, i1 + 1, i2, i1 + i3);
/* 175 */         if (i2 > 2) {
/* 176 */           paramGraphics.drawLine(i2 - 1, i1, i2 - 1, i1 + i3 + 1);
/*     */         }
/* 178 */         paramGraphics.setColor(localColor2);
/* 179 */         paramGraphics.drawLine(i2, i1 + i3 + 1, i2, i1 + i3 + 1);
/*     */ 
/* 181 */         i3 += 2;
/* 182 */         i1--;
/*     */       }
/* 184 */       break;
/*     */     case 7:
/* 187 */       paramGraphics.setColor(localColor2);
/* 188 */       paramGraphics.drawLine(0, m, 0, m);
/* 189 */       i1 = m - 1; i2 = 1; for (i3 = 1; i2 <= n - 2; i2 += 2) {
/* 190 */         paramGraphics.setColor(localColor1);
/* 191 */         paramGraphics.drawLine(i2, i1, i2, i1);
/* 192 */         if (i2 >= n - 2) {
/* 193 */           paramGraphics.drawLine(i2 + 1, i1, i2 + 1, i1);
/*     */         }
/* 195 */         paramGraphics.setColor(localColor3);
/* 196 */         paramGraphics.drawLine(i2, i1 + 1, i2, i1 + i3);
/* 197 */         if (i2 < n - 2) {
/* 198 */           paramGraphics.drawLine(i2 + 1, i1, i2 + 1, i1 + i3 + 1);
/*     */         }
/* 200 */         paramGraphics.setColor(localColor2);
/* 201 */         paramGraphics.drawLine(i2, i1 + i3 + 1, i2, i1 + i3 + 1);
/* 202 */         if (i2 >= n - 2) {
/* 203 */           paramGraphics.drawLine(i2 + 1, i1 + 1, i2 + 1, i1 + i3 + 1);
/*     */         }
/* 205 */         i3 += 2;
/* 206 */         i1--;
/*     */       }
/*     */     case 2:
/*     */     case 4:
/*     */     case 6:
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifScrollBarButton
 * JD-Core Version:    0.6.2
 */
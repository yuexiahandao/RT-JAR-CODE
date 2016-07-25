/*     */ package javax.swing.border;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.beans.ConstructorProperties;
/*     */ 
/*     */ public class SoftBevelBorder extends BevelBorder
/*     */ {
/*     */   public SoftBevelBorder(int paramInt)
/*     */   {
/*  60 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public SoftBevelBorder(int paramInt, Color paramColor1, Color paramColor2)
/*     */   {
/*  71 */     super(paramInt, paramColor1, paramColor2);
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"bevelType", "highlightOuterColor", "highlightInnerColor", "shadowOuterColor", "shadowInnerColor"})
/*     */   public SoftBevelBorder(int paramInt, Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */   {
/*  87 */     super(paramInt, paramColor1, paramColor2, paramColor3, paramColor4);
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 102 */     Color localColor = paramGraphics.getColor();
/* 103 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 105 */     if (this.bevelType == 0) {
/* 106 */       paramGraphics.setColor(getHighlightOuterColor(paramComponent));
/* 107 */       paramGraphics.drawLine(0, 0, paramInt3 - 2, 0);
/* 108 */       paramGraphics.drawLine(0, 0, 0, paramInt4 - 2);
/* 109 */       paramGraphics.drawLine(1, 1, 1, 1);
/*     */ 
/* 111 */       paramGraphics.setColor(getHighlightInnerColor(paramComponent));
/* 112 */       paramGraphics.drawLine(2, 1, paramInt3 - 2, 1);
/* 113 */       paramGraphics.drawLine(1, 2, 1, paramInt4 - 2);
/* 114 */       paramGraphics.drawLine(2, 2, 2, 2);
/* 115 */       paramGraphics.drawLine(0, paramInt4 - 1, 0, paramInt4 - 2);
/* 116 */       paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 1, 0);
/*     */ 
/* 118 */       paramGraphics.setColor(getShadowOuterColor(paramComponent));
/* 119 */       paramGraphics.drawLine(2, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 120 */       paramGraphics.drawLine(paramInt3 - 1, 2, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/* 122 */       paramGraphics.setColor(getShadowInnerColor(paramComponent));
/* 123 */       paramGraphics.drawLine(paramInt3 - 2, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/*     */     }
/* 126 */     else if (this.bevelType == 1) {
/* 127 */       paramGraphics.setColor(getShadowOuterColor(paramComponent));
/* 128 */       paramGraphics.drawLine(0, 0, paramInt3 - 2, 0);
/* 129 */       paramGraphics.drawLine(0, 0, 0, paramInt4 - 2);
/* 130 */       paramGraphics.drawLine(1, 1, 1, 1);
/*     */ 
/* 132 */       paramGraphics.setColor(getShadowInnerColor(paramComponent));
/* 133 */       paramGraphics.drawLine(2, 1, paramInt3 - 2, 1);
/* 134 */       paramGraphics.drawLine(1, 2, 1, paramInt4 - 2);
/* 135 */       paramGraphics.drawLine(2, 2, 2, 2);
/* 136 */       paramGraphics.drawLine(0, paramInt4 - 1, 0, paramInt4 - 2);
/* 137 */       paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 1, 0);
/*     */ 
/* 139 */       paramGraphics.setColor(getHighlightOuterColor(paramComponent));
/* 140 */       paramGraphics.drawLine(2, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 141 */       paramGraphics.drawLine(paramInt3 - 1, 2, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/* 143 */       paramGraphics.setColor(getHighlightInnerColor(paramComponent));
/* 144 */       paramGraphics.drawLine(paramInt3 - 2, paramInt4 - 2, paramInt3 - 2, paramInt4 - 2);
/*     */     }
/* 146 */     paramGraphics.translate(-paramInt1, -paramInt2);
/* 147 */     paramGraphics.setColor(localColor);
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/* 156 */     paramInsets.set(3, 3, 3, 3);
/* 157 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 163 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.SoftBevelBorder
 * JD-Core Version:    0.6.2
 */
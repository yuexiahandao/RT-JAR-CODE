/*     */ package javax.swing.border;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.beans.ConstructorProperties;
/*     */ 
/*     */ public class CompoundBorder extends AbstractBorder
/*     */ {
/*     */   protected Border outsideBorder;
/*     */   protected Border insideBorder;
/*     */ 
/*     */   public CompoundBorder()
/*     */   {
/*  65 */     this.outsideBorder = null;
/*  66 */     this.insideBorder = null;
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"outsideBorder", "insideBorder"})
/*     */   public CompoundBorder(Border paramBorder1, Border paramBorder2)
/*     */   {
/*  77 */     this.outsideBorder = paramBorder1;
/*  78 */     this.insideBorder = paramBorder2;
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/*  90 */     return ((this.outsideBorder == null) || (this.outsideBorder.isBorderOpaque())) && ((this.insideBorder == null) || (this.insideBorder.isBorderOpaque()));
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 110 */     int i = paramInt1;
/* 111 */     int j = paramInt2;
/* 112 */     int k = paramInt3;
/* 113 */     int m = paramInt4;
/*     */ 
/* 115 */     if (this.outsideBorder != null) {
/* 116 */       this.outsideBorder.paintBorder(paramComponent, paramGraphics, i, j, k, m);
/*     */ 
/* 118 */       Insets localInsets = this.outsideBorder.getBorderInsets(paramComponent);
/* 119 */       i += localInsets.left;
/* 120 */       j += localInsets.top;
/* 121 */       k = k - localInsets.right - localInsets.left;
/* 122 */       m = m - localInsets.bottom - localInsets.top;
/*     */     }
/* 124 */     if (this.insideBorder != null)
/* 125 */       this.insideBorder.paintBorder(paramComponent, paramGraphics, i, j, k, m);
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/* 137 */     paramInsets.top = (paramInsets.left = paramInsets.right = paramInsets.bottom = 0);
/*     */     Insets localInsets;
/* 138 */     if (this.outsideBorder != null) {
/* 139 */       localInsets = this.outsideBorder.getBorderInsets(paramComponent);
/* 140 */       paramInsets.top += localInsets.top;
/* 141 */       paramInsets.left += localInsets.left;
/* 142 */       paramInsets.right += localInsets.right;
/* 143 */       paramInsets.bottom += localInsets.bottom;
/*     */     }
/* 145 */     if (this.insideBorder != null) {
/* 146 */       localInsets = this.insideBorder.getBorderInsets(paramComponent);
/* 147 */       paramInsets.top += localInsets.top;
/* 148 */       paramInsets.left += localInsets.left;
/* 149 */       paramInsets.right += localInsets.right;
/* 150 */       paramInsets.bottom += localInsets.bottom;
/*     */     }
/* 152 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public Border getOutsideBorder()
/*     */   {
/* 159 */     return this.outsideBorder;
/*     */   }
/*     */ 
/*     */   public Border getInsideBorder()
/*     */   {
/* 166 */     return this.insideBorder;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.CompoundBorder
 * JD-Core Version:    0.6.2
 */
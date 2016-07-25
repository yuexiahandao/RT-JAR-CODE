/*     */ package javax.swing.border;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Component.BaselineResizeBehavior;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class AbstractBorder
/*     */   implements Border, Serializable
/*     */ {
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent)
/*     */   {
/*  74 */     return getBorderInsets(paramComponent, new Insets(0, 0, 0, 0));
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/*  84 */     paramInsets.left = (paramInsets.top = paramInsets.right = paramInsets.bottom = 0);
/*  85 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   public Rectangle getInteriorRectangle(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 104 */     return getInteriorRectangle(paramComponent, this, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public static Rectangle getInteriorRectangle(Component paramComponent, Border paramBorder, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */     Insets localInsets;
/* 121 */     if (paramBorder != null)
/* 122 */       localInsets = paramBorder.getBorderInsets(paramComponent);
/*     */     else
/* 124 */       localInsets = new Insets(0, 0, 0, 0);
/* 125 */     return new Rectangle(paramInt1 + localInsets.left, paramInt2 + localInsets.top, paramInt3 - localInsets.right - localInsets.left, paramInt4 - localInsets.top - localInsets.bottom);
/*     */   }
/*     */ 
/*     */   public int getBaseline(Component paramComponent, int paramInt1, int paramInt2)
/*     */   {
/* 152 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/* 153 */       throw new IllegalArgumentException("Width and height must be >= 0");
/*     */     }
/*     */ 
/* 156 */     return -1;
/*     */   }
/*     */ 
/*     */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(Component paramComponent)
/*     */   {
/* 184 */     if (paramComponent == null) {
/* 185 */       throw new NullPointerException("Component must be non-null");
/*     */     }
/* 187 */     return Component.BaselineResizeBehavior.OTHER;
/*     */   }
/*     */ 
/*     */   static boolean isLeftToRight(Component paramComponent)
/*     */   {
/* 195 */     return paramComponent.getComponentOrientation().isLeftToRight();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.AbstractBorder
 * JD-Core Version:    0.6.2
 */
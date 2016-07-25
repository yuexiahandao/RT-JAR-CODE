/*     */ package javax.swing.border;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class EmptyBorder extends AbstractBorder
/*     */   implements Serializable
/*     */ {
/*     */   protected int left;
/*     */   protected int right;
/*     */   protected int top;
/*     */   protected int bottom;
/*     */ 
/*     */   public EmptyBorder(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  61 */     this.top = paramInt1;
/*  62 */     this.right = paramInt4;
/*  63 */     this.bottom = paramInt3;
/*  64 */     this.left = paramInt2;
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"borderInsets"})
/*     */   public EmptyBorder(Insets paramInsets)
/*     */   {
/*  73 */     this.top = paramInsets.top;
/*  74 */     this.right = paramInsets.right;
/*  75 */     this.bottom = paramInsets.bottom;
/*  76 */     this.left = paramInsets.left;
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/*  91 */     paramInsets.left = this.left;
/*  92 */     paramInsets.top = this.top;
/*  93 */     paramInsets.right = this.right;
/*  94 */     paramInsets.bottom = this.bottom;
/*  95 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets()
/*     */   {
/* 103 */     return new Insets(this.top, this.left, this.bottom, this.right);
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 110 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.EmptyBorder
 * JD-Core Version:    0.6.2
 */
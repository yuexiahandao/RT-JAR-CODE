/*     */ package javax.swing.border;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.beans.ConstructorProperties;
/*     */ 
/*     */ public class EtchedBorder extends AbstractBorder
/*     */ {
/*     */   public static final int RAISED = 0;
/*     */   public static final int LOWERED = 1;
/*     */   protected int etchType;
/*     */   protected Color highlight;
/*     */   protected Color shadow;
/*     */ 
/*     */   public EtchedBorder()
/*     */   {
/*  71 */     this(1);
/*     */   }
/*     */ 
/*     */   public EtchedBorder(int paramInt)
/*     */   {
/*  82 */     this(paramInt, null, null);
/*     */   }
/*     */ 
/*     */   public EtchedBorder(Color paramColor1, Color paramColor2)
/*     */   {
/*  92 */     this(1, paramColor1, paramColor2);
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"etchType", "highlightColor", "shadowColor"})
/*     */   public EtchedBorder(int paramInt, Color paramColor1, Color paramColor2)
/*     */   {
/* 104 */     this.etchType = paramInt;
/* 105 */     this.highlight = paramColor1;
/* 106 */     this.shadow = paramColor2;
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 120 */     int i = paramInt3;
/* 121 */     int j = paramInt4;
/*     */ 
/* 123 */     paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 125 */     paramGraphics.setColor(this.etchType == 1 ? getShadowColor(paramComponent) : getHighlightColor(paramComponent));
/* 126 */     paramGraphics.drawRect(0, 0, i - 2, j - 2);
/*     */ 
/* 128 */     paramGraphics.setColor(this.etchType == 1 ? getHighlightColor(paramComponent) : getShadowColor(paramComponent));
/* 129 */     paramGraphics.drawLine(1, j - 3, 1, 1);
/* 130 */     paramGraphics.drawLine(1, 1, i - 3, 1);
/*     */ 
/* 132 */     paramGraphics.drawLine(0, j - 1, i - 1, j - 1);
/* 133 */     paramGraphics.drawLine(i - 1, j - 1, i - 1, 0);
/*     */ 
/* 135 */     paramGraphics.translate(-paramInt1, -paramInt2);
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/* 144 */     paramInsets.set(2, 2, 2, 2);
/* 145 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 151 */     return true;
/*     */   }
/*     */ 
/*     */   public int getEtchType()
/*     */   {
/* 157 */     return this.etchType;
/*     */   }
/*     */ 
/*     */   public Color getHighlightColor(Component paramComponent)
/*     */   {
/* 169 */     return this.highlight != null ? this.highlight : paramComponent.getBackground().brighter();
/*     */   }
/*     */ 
/*     */   public Color getHighlightColor()
/*     */   {
/* 180 */     return this.highlight;
/*     */   }
/*     */ 
/*     */   public Color getShadowColor(Component paramComponent)
/*     */   {
/* 192 */     return this.shadow != null ? this.shadow : paramComponent.getBackground().darker();
/*     */   }
/*     */ 
/*     */   public Color getShadowColor()
/*     */   {
/* 202 */     return this.shadow;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.EtchedBorder
 * JD-Core Version:    0.6.2
 */
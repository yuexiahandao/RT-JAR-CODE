/*     */ package javax.swing.border;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Path2D;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.awt.geom.RoundRectangle2D.Float;
/*     */ import java.beans.ConstructorProperties;
/*     */ 
/*     */ public class LineBorder extends AbstractBorder
/*     */ {
/*     */   private static Border blackLine;
/*     */   private static Border grayLine;
/*     */   protected int thickness;
/*     */   protected Color lineColor;
/*     */   protected boolean roundedCorners;
/*     */ 
/*     */   public static Border createBlackLineBorder()
/*     */   {
/*  65 */     if (blackLine == null) {
/*  66 */       blackLine = new LineBorder(Color.black, 1);
/*     */     }
/*  68 */     return blackLine;
/*     */   }
/*     */ 
/*     */   public static Border createGrayLineBorder()
/*     */   {
/*  74 */     if (grayLine == null) {
/*  75 */       grayLine = new LineBorder(Color.gray, 1);
/*     */     }
/*  77 */     return grayLine;
/*     */   }
/*     */ 
/*     */   public LineBorder(Color paramColor)
/*     */   {
/*  86 */     this(paramColor, 1, false);
/*     */   }
/*     */ 
/*     */   public LineBorder(Color paramColor, int paramInt)
/*     */   {
/*  95 */     this(paramColor, paramInt, false);
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"lineColor", "thickness", "roundedCorners"})
/*     */   public LineBorder(Color paramColor, int paramInt, boolean paramBoolean)
/*     */   {
/* 108 */     this.lineColor = paramColor;
/* 109 */     this.thickness = paramInt;
/* 110 */     this.roundedCorners = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 124 */     if ((this.thickness > 0) && ((paramGraphics instanceof Graphics2D))) {
/* 125 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/*     */ 
/* 127 */       Color localColor = localGraphics2D.getColor();
/* 128 */       localGraphics2D.setColor(this.lineColor);
/*     */ 
/* 133 */       int i = this.thickness;
/* 134 */       int j = i + i;
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 135 */       if (this.roundedCorners) {
/* 136 */         int k = i + j;
/* 137 */         localObject1 = new RoundRectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4, k, k);
/* 138 */         localObject2 = new RoundRectangle2D.Float(paramInt1 + i, paramInt2 + i, paramInt3 - j, paramInt4 - j, k, k);
/*     */       }
/*     */       else {
/* 141 */         localObject1 = new Rectangle2D.Float(paramInt1, paramInt2, paramInt3, paramInt4);
/* 142 */         localObject2 = new Rectangle2D.Float(paramInt1 + i, paramInt2 + i, paramInt3 - j, paramInt4 - j);
/*     */       }
/* 144 */       Path2D.Float localFloat = new Path2D.Float(0);
/* 145 */       localFloat.append((Shape)localObject1, false);
/* 146 */       localFloat.append((Shape)localObject2, false);
/* 147 */       localGraphics2D.fill(localFloat);
/* 148 */       localGraphics2D.setColor(localColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/* 158 */     paramInsets.set(this.thickness, this.thickness, this.thickness, this.thickness);
/* 159 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public Color getLineColor()
/*     */   {
/* 166 */     return this.lineColor;
/*     */   }
/*     */ 
/*     */   public int getThickness()
/*     */   {
/* 173 */     return this.thickness;
/*     */   }
/*     */ 
/*     */   public boolean getRoundedCorners()
/*     */   {
/* 181 */     return this.roundedCorners;
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 188 */     return !this.roundedCorners;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.LineBorder
 * JD-Core Version:    0.6.2
 */
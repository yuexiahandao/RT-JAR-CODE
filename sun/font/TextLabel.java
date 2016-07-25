/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ 
/*     */ public abstract class TextLabel
/*     */ {
/*     */   public abstract Rectangle2D getVisualBounds(float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public abstract Rectangle2D getLogicalBounds(float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public abstract Rectangle2D getAlignBounds(float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public abstract Rectangle2D getItalicBounds(float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public abstract Shape getOutline(float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public abstract void draw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public Rectangle2D getVisualBounds()
/*     */   {
/*  87 */     return getVisualBounds(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getLogicalBounds()
/*     */   {
/*  94 */     return getLogicalBounds(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getAlignBounds()
/*     */   {
/* 101 */     return getAlignBounds(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getItalicBounds()
/*     */   {
/* 108 */     return getItalicBounds(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   public Shape getOutline()
/*     */   {
/* 115 */     return getOutline(0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   public void draw(Graphics2D paramGraphics2D)
/*     */   {
/* 122 */     draw(paramGraphics2D, 0.0F, 0.0F);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.TextLabel
 * JD-Core Version:    0.6.2
 */
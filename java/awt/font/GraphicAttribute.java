/*     */ package java.awt.font;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ 
/*     */ public abstract class GraphicAttribute
/*     */ {
/*     */   private int fAlignment;
/*     */   public static final int TOP_ALIGNMENT = -1;
/*     */   public static final int BOTTOM_ALIGNMENT = -2;
/*     */   public static final int ROMAN_BASELINE = 0;
/*     */   public static final int CENTER_BASELINE = 1;
/*     */   public static final int HANGING_BASELINE = 2;
/*     */ 
/*     */   protected GraphicAttribute(int paramInt)
/*     */   {
/* 102 */     if ((paramInt < -2) || (paramInt > 2)) {
/* 103 */       throw new IllegalArgumentException("bad alignment");
/*     */     }
/* 105 */     this.fAlignment = paramInt;
/*     */   }
/*     */ 
/*     */   public abstract float getAscent();
/*     */ 
/*     */   public abstract float getDescent();
/*     */ 
/*     */   public abstract float getAdvance();
/*     */ 
/*     */   public Rectangle2D getBounds()
/*     */   {
/* 148 */     float f = getAscent();
/* 149 */     return new Rectangle2D.Float(0.0F, -f, getAdvance(), f + getDescent());
/*     */   }
/*     */ 
/*     */   public Shape getOutline(AffineTransform paramAffineTransform)
/*     */   {
/* 169 */     Object localObject = getBounds();
/* 170 */     if (paramAffineTransform != null) {
/* 171 */       localObject = paramAffineTransform.createTransformedShape((Shape)localObject);
/*     */     }
/* 173 */     return localObject;
/*     */   }
/*     */ 
/*     */   public abstract void draw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2);
/*     */ 
/*     */   public final int getAlignment()
/*     */   {
/* 194 */     return this.fAlignment;
/*     */   }
/*     */ 
/*     */   public GlyphJustificationInfo getJustificationInfo()
/*     */   {
/* 208 */     float f = getAdvance();
/*     */ 
/* 210 */     return new GlyphJustificationInfo(f, false, 2, f / 3.0F, f / 3.0F, false, 1, 0.0F, 0.0F);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.GraphicAttribute
 * JD-Core Version:    0.6.2
 */
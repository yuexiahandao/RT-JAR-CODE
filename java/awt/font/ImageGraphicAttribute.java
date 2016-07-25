/*     */ package java.awt.font;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Image;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ 
/*     */ public final class ImageGraphicAttribute extends GraphicAttribute
/*     */ {
/*     */   private Image fImage;
/*     */   private float fImageWidth;
/*     */   private float fImageHeight;
/*     */   private float fOriginX;
/*     */   private float fOriginY;
/*     */ 
/*     */   public ImageGraphicAttribute(Image paramImage, int paramInt)
/*     */   {
/*  71 */     this(paramImage, paramInt, 0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   public ImageGraphicAttribute(Image paramImage, int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/*  97 */     super(paramInt);
/*     */ 
/* 101 */     this.fImage = paramImage;
/*     */ 
/* 103 */     this.fImageWidth = paramImage.getWidth(null);
/* 104 */     this.fImageHeight = paramImage.getHeight(null);
/*     */ 
/* 107 */     this.fOriginX = paramFloat1;
/* 108 */     this.fOriginY = paramFloat2;
/*     */   }
/*     */ 
/*     */   public float getAscent()
/*     */   {
/* 119 */     return Math.max(0.0F, this.fOriginY);
/*     */   }
/*     */ 
/*     */   public float getDescent()
/*     */   {
/* 130 */     return Math.max(0.0F, this.fImageHeight - this.fOriginY);
/*     */   }
/*     */ 
/*     */   public float getAdvance()
/*     */   {
/* 141 */     return Math.max(0.0F, this.fImageWidth - this.fOriginX);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getBounds()
/*     */   {
/* 155 */     return new Rectangle2D.Float(-this.fOriginX, -this.fOriginY, this.fImageWidth, this.fImageHeight);
/*     */   }
/*     */ 
/*     */   public void draw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2)
/*     */   {
/* 164 */     paramGraphics2D.drawImage(this.fImage, (int)(paramFloat1 - this.fOriginX), (int)(paramFloat2 - this.fOriginY), null);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 173 */     return this.fImage.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 187 */       return equals((ImageGraphicAttribute)paramObject);
/*     */     } catch (ClassCastException localClassCastException) {
/*     */     }
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(ImageGraphicAttribute paramImageGraphicAttribute)
/*     */   {
/* 205 */     if (paramImageGraphicAttribute == null) {
/* 206 */       return false;
/*     */     }
/*     */ 
/* 209 */     if (this == paramImageGraphicAttribute) {
/* 210 */       return true;
/*     */     }
/*     */ 
/* 213 */     if ((this.fOriginX != paramImageGraphicAttribute.fOriginX) || (this.fOriginY != paramImageGraphicAttribute.fOriginY)) {
/* 214 */       return false;
/*     */     }
/*     */ 
/* 217 */     if (getAlignment() != paramImageGraphicAttribute.getAlignment()) {
/* 218 */       return false;
/*     */     }
/*     */ 
/* 221 */     if (!this.fImage.equals(paramImageGraphicAttribute.fImage)) {
/* 222 */       return false;
/*     */     }
/*     */ 
/* 225 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.ImageGraphicAttribute
 * JD-Core Version:    0.6.2
 */
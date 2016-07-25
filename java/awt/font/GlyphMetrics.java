/*     */ package java.awt.font;
/*     */ 
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ 
/*     */ public final class GlyphMetrics
/*     */ {
/*     */   private boolean horizontal;
/*     */   private float advanceX;
/*     */   private float advanceY;
/*     */   private Rectangle2D.Float bounds;
/*     */   private byte glyphType;
/*     */   public static final byte STANDARD = 0;
/*     */   public static final byte LIGATURE = 1;
/*     */   public static final byte COMBINING = 2;
/*     */   public static final byte COMPONENT = 3;
/*     */   public static final byte WHITESPACE = 4;
/*     */ 
/*     */   public GlyphMetrics(float paramFloat, Rectangle2D paramRectangle2D, byte paramByte)
/*     */   {
/* 180 */     this.horizontal = true;
/* 181 */     this.advanceX = paramFloat;
/* 182 */     this.advanceY = 0.0F;
/* 183 */     this.bounds = new Rectangle2D.Float();
/* 184 */     this.bounds.setRect(paramRectangle2D);
/* 185 */     this.glyphType = paramByte;
/*     */   }
/*     */ 
/*     */   public GlyphMetrics(boolean paramBoolean, float paramFloat1, float paramFloat2, Rectangle2D paramRectangle2D, byte paramByte)
/*     */   {
/* 201 */     this.horizontal = paramBoolean;
/* 202 */     this.advanceX = paramFloat1;
/* 203 */     this.advanceY = paramFloat2;
/* 204 */     this.bounds = new Rectangle2D.Float();
/* 205 */     this.bounds.setRect(paramRectangle2D);
/* 206 */     this.glyphType = paramByte;
/*     */   }
/*     */ 
/*     */   public float getAdvance()
/*     */   {
/* 215 */     return this.horizontal ? this.advanceX : this.advanceY;
/*     */   }
/*     */ 
/*     */   public float getAdvanceX()
/*     */   {
/* 224 */     return this.advanceX;
/*     */   }
/*     */ 
/*     */   public float getAdvanceY()
/*     */   {
/* 233 */     return this.advanceY;
/*     */   }
/*     */ 
/*     */   public Rectangle2D getBounds2D()
/*     */   {
/* 243 */     return new Rectangle2D.Float(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height);
/*     */   }
/*     */ 
/*     */   public float getLSB()
/*     */   {
/* 255 */     return this.horizontal ? this.bounds.x : this.bounds.y;
/*     */   }
/*     */ 
/*     */   public float getRSB()
/*     */   {
/* 267 */     return this.horizontal ? this.advanceX - this.bounds.x - this.bounds.width : this.advanceY - this.bounds.y - this.bounds.height;
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 277 */     return this.glyphType;
/*     */   }
/*     */ 
/*     */   public boolean isStandard()
/*     */   {
/* 286 */     return (this.glyphType & 0x3) == 0;
/*     */   }
/*     */ 
/*     */   public boolean isLigature()
/*     */   {
/* 295 */     return (this.glyphType & 0x3) == 1;
/*     */   }
/*     */ 
/*     */   public boolean isCombining()
/*     */   {
/* 304 */     return (this.glyphType & 0x3) == 2;
/*     */   }
/*     */ 
/*     */   public boolean isComponent()
/*     */   {
/* 313 */     return (this.glyphType & 0x3) == 3;
/*     */   }
/*     */ 
/*     */   public boolean isWhitespace()
/*     */   {
/* 322 */     return (this.glyphType & 0x4) == 4;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.GlyphMetrics
 * JD-Core Version:    0.6.2
 */
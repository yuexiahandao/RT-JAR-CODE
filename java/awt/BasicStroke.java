/*     */ package java.awt;
/*     */ 
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.util.Arrays;
/*     */ import sun.java2d.pipe.RenderingEngine;
/*     */ 
/*     */ public class BasicStroke
/*     */   implements Stroke
/*     */ {
/*     */   public static final int JOIN_MITER = 0;
/*     */   public static final int JOIN_ROUND = 1;
/*     */   public static final int JOIN_BEVEL = 2;
/*     */   public static final int CAP_BUTT = 0;
/*     */   public static final int CAP_ROUND = 1;
/*     */   public static final int CAP_SQUARE = 2;
/*     */   float width;
/*     */   int join;
/*     */   int cap;
/*     */   float miterlimit;
/*     */   float[] dash;
/*     */   float dash_phase;
/*     */ 
/*     */   @ConstructorProperties({"lineWidth", "endCap", "lineJoin", "miterLimit", "dashArray", "dashPhase"})
/*     */   public BasicStroke(float paramFloat1, int paramInt1, int paramInt2, float paramFloat2, float[] paramArrayOfFloat, float paramFloat3)
/*     */   {
/* 191 */     if (paramFloat1 < 0.0F) {
/* 192 */       throw new IllegalArgumentException("negative width");
/*     */     }
/* 194 */     if ((paramInt1 != 0) && (paramInt1 != 1) && (paramInt1 != 2)) {
/* 195 */       throw new IllegalArgumentException("illegal end cap value");
/*     */     }
/* 197 */     if (paramInt2 == 0) {
/* 198 */       if (paramFloat2 < 1.0F)
/* 199 */         throw new IllegalArgumentException("miter limit < 1");
/*     */     }
/* 201 */     else if ((paramInt2 != 1) && (paramInt2 != 2)) {
/* 202 */       throw new IllegalArgumentException("illegal line join value");
/*     */     }
/* 204 */     if (paramArrayOfFloat != null) {
/* 205 */       if (paramFloat3 < 0.0F) {
/* 206 */         throw new IllegalArgumentException("negative dash phase");
/*     */       }
/* 208 */       int i = 1;
/* 209 */       for (int j = 0; j < paramArrayOfFloat.length; j++) {
/* 210 */         float f = paramArrayOfFloat[j];
/* 211 */         if (f > 0.0D)
/* 212 */           i = 0;
/* 213 */         else if (f < 0.0D) {
/* 214 */           throw new IllegalArgumentException("negative dash length");
/*     */         }
/*     */       }
/* 217 */       if (i != 0) {
/* 218 */         throw new IllegalArgumentException("dash lengths all zero");
/*     */       }
/*     */     }
/* 221 */     this.width = paramFloat1;
/* 222 */     this.cap = paramInt1;
/* 223 */     this.join = paramInt2;
/* 224 */     this.miterlimit = paramFloat2;
/* 225 */     if (paramArrayOfFloat != null) {
/* 226 */       this.dash = ((float[])paramArrayOfFloat.clone());
/*     */     }
/* 228 */     this.dash_phase = paramFloat3;
/*     */   }
/*     */ 
/*     */   public BasicStroke(float paramFloat1, int paramInt1, int paramInt2, float paramFloat2)
/*     */   {
/* 247 */     this(paramFloat1, paramInt1, paramInt2, paramFloat2, null, 0.0F);
/*     */   }
/*     */ 
/*     */   public BasicStroke(float paramFloat, int paramInt1, int paramInt2)
/*     */   {
/* 265 */     this(paramFloat, paramInt1, paramInt2, 10.0F, null, 0.0F);
/*     */   }
/*     */ 
/*     */   public BasicStroke(float paramFloat)
/*     */   {
/* 276 */     this(paramFloat, 2, 0, 10.0F, null, 0.0F);
/*     */   }
/*     */ 
/*     */   public BasicStroke()
/*     */   {
/* 286 */     this(1.0F, 2, 0, 10.0F, null, 0.0F);
/*     */   }
/*     */ 
/*     */   public Shape createStrokedShape(Shape paramShape)
/*     */   {
/* 297 */     RenderingEngine localRenderingEngine = RenderingEngine.getInstance();
/*     */ 
/* 299 */     return localRenderingEngine.createStrokedShape(paramShape, this.width, this.cap, this.join, this.miterlimit, this.dash, this.dash_phase);
/*     */   }
/*     */ 
/*     */   public float getLineWidth()
/*     */   {
/* 312 */     return this.width;
/*     */   }
/*     */ 
/*     */   public int getEndCap()
/*     */   {
/* 322 */     return this.cap;
/*     */   }
/*     */ 
/*     */   public int getLineJoin()
/*     */   {
/* 332 */     return this.join;
/*     */   }
/*     */ 
/*     */   public float getMiterLimit()
/*     */   {
/* 340 */     return this.miterlimit;
/*     */   }
/*     */ 
/*     */   public float[] getDashArray()
/*     */   {
/* 356 */     if (this.dash == null) {
/* 357 */       return null;
/*     */     }
/*     */ 
/* 360 */     return (float[])this.dash.clone();
/*     */   }
/*     */ 
/*     */   public float getDashPhase()
/*     */   {
/* 372 */     return this.dash_phase;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 380 */     int i = Float.floatToIntBits(this.width);
/* 381 */     i = i * 31 + this.join;
/* 382 */     i = i * 31 + this.cap;
/* 383 */     i = i * 31 + Float.floatToIntBits(this.miterlimit);
/* 384 */     if (this.dash != null) {
/* 385 */       i = i * 31 + Float.floatToIntBits(this.dash_phase);
/* 386 */       for (int j = 0; j < this.dash.length; j++) {
/* 387 */         i = i * 31 + Float.floatToIntBits(this.dash[j]);
/*     */       }
/*     */     }
/* 390 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 409 */     if (!(paramObject instanceof BasicStroke)) {
/* 410 */       return false;
/*     */     }
/*     */ 
/* 413 */     BasicStroke localBasicStroke = (BasicStroke)paramObject;
/* 414 */     if (this.width != localBasicStroke.width) {
/* 415 */       return false;
/*     */     }
/*     */ 
/* 418 */     if (this.join != localBasicStroke.join) {
/* 419 */       return false;
/*     */     }
/*     */ 
/* 422 */     if (this.cap != localBasicStroke.cap) {
/* 423 */       return false;
/*     */     }
/*     */ 
/* 426 */     if (this.miterlimit != localBasicStroke.miterlimit) {
/* 427 */       return false;
/*     */     }
/*     */ 
/* 430 */     if (this.dash != null) {
/* 431 */       if (this.dash_phase != localBasicStroke.dash_phase) {
/* 432 */         return false;
/*     */       }
/*     */ 
/* 435 */       if (!Arrays.equals(this.dash, localBasicStroke.dash)) {
/* 436 */         return false;
/*     */       }
/*     */     }
/* 439 */     else if (localBasicStroke.dash != null) {
/* 440 */       return false;
/*     */     }
/*     */ 
/* 443 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.BasicStroke
 * JD-Core Version:    0.6.2
 */
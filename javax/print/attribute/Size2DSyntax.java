/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class Size2DSyntax
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 5584439964938660530L;
/*     */   private int x;
/*     */   private int y;
/*     */   public static final int INCH = 25400;
/*     */   public static final int MM = 1000;
/*     */ 
/*     */   protected Size2DSyntax(float paramFloat1, float paramFloat2, int paramInt)
/*     */   {
/* 128 */     if (paramFloat1 < 0.0F) {
/* 129 */       throw new IllegalArgumentException("x < 0");
/*     */     }
/* 131 */     if (paramFloat2 < 0.0F) {
/* 132 */       throw new IllegalArgumentException("y < 0");
/*     */     }
/* 134 */     if (paramInt < 1) {
/* 135 */       throw new IllegalArgumentException("units < 1");
/*     */     }
/* 137 */     this.x = ((int)(paramFloat1 * paramInt + 0.5F));
/* 138 */     this.y = ((int)(paramFloat2 * paramInt + 0.5F));
/*     */   }
/*     */ 
/*     */   protected Size2DSyntax(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 156 */     if (paramInt1 < 0) {
/* 157 */       throw new IllegalArgumentException("x < 0");
/*     */     }
/* 159 */     if (paramInt2 < 0) {
/* 160 */       throw new IllegalArgumentException("y < 0");
/*     */     }
/* 162 */     if (paramInt3 < 1) {
/* 163 */       throw new IllegalArgumentException("units < 1");
/*     */     }
/* 165 */     this.x = (paramInt1 * paramInt3);
/* 166 */     this.y = (paramInt2 * paramInt3);
/*     */   }
/*     */ 
/*     */   private static float convertFromMicrometers(int paramInt1, int paramInt2)
/*     */   {
/* 185 */     if (paramInt2 < 1) {
/* 186 */       throw new IllegalArgumentException("units is < 1");
/*     */     }
/* 188 */     return paramInt1 / paramInt2;
/*     */   }
/*     */ 
/*     */   public float[] getSize(int paramInt)
/*     */   {
/* 206 */     return new float[] { getX(paramInt), getY(paramInt) };
/*     */   }
/*     */ 
/*     */   public float getX(int paramInt)
/*     */   {
/* 223 */     return convertFromMicrometers(this.x, paramInt);
/*     */   }
/*     */ 
/*     */   public float getY(int paramInt)
/*     */   {
/* 240 */     return convertFromMicrometers(this.y, paramInt);
/*     */   }
/*     */ 
/*     */   public String toString(int paramInt, String paramString)
/*     */   {
/* 263 */     StringBuffer localStringBuffer = new StringBuffer();
/* 264 */     localStringBuffer.append(getX(paramInt));
/* 265 */     localStringBuffer.append('x');
/* 266 */     localStringBuffer.append(getY(paramInt));
/* 267 */     if (paramString != null) {
/* 268 */       localStringBuffer.append(' ');
/* 269 */       localStringBuffer.append(paramString);
/*     */     }
/* 271 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 297 */     return (paramObject != null) && ((paramObject instanceof Size2DSyntax)) && (this.x == ((Size2DSyntax)paramObject).x) && (this.y == ((Size2DSyntax)paramObject).y);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 307 */     return this.x & 0xFFFF | (this.y & 0xFFFF) << 16;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 318 */     StringBuffer localStringBuffer = new StringBuffer();
/* 319 */     localStringBuffer.append(this.x);
/* 320 */     localStringBuffer.append('x');
/* 321 */     localStringBuffer.append(this.y);
/* 322 */     localStringBuffer.append(" um");
/* 323 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   protected int getXMicrometers()
/*     */   {
/* 333 */     return this.x;
/*     */   }
/*     */ 
/*     */   protected int getYMicrometers()
/*     */   {
/* 343 */     return this.y;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.Size2DSyntax
 * JD-Core Version:    0.6.2
 */
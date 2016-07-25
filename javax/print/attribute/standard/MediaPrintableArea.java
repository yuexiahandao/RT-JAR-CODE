/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.DocAttribute;
/*     */ import javax.print.attribute.PrintJobAttribute;
/*     */ import javax.print.attribute.PrintRequestAttribute;
/*     */ 
/*     */ public final class MediaPrintableArea
/*     */   implements DocAttribute, PrintRequestAttribute, PrintJobAttribute
/*     */ {
/*     */   private int x;
/*     */   private int y;
/*     */   private int w;
/*     */   private int h;
/*     */   private int units;
/*     */   private static final long serialVersionUID = -1597171464050795793L;
/*     */   public static final int INCH = 25400;
/*     */   public static final int MM = 1000;
/*     */ 
/*     */   public MediaPrintableArea(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt)
/*     */   {
/* 115 */     if ((paramFloat1 < 0.0D) || (paramFloat2 < 0.0D) || (paramFloat3 <= 0.0D) || (paramFloat4 <= 0.0D) || (paramInt < 1))
/*     */     {
/* 117 */       throw new IllegalArgumentException("0 or negative value argument");
/*     */     }
/*     */ 
/* 120 */     this.x = ((int)(paramFloat1 * paramInt + 0.5F));
/* 121 */     this.y = ((int)(paramFloat2 * paramInt + 0.5F));
/* 122 */     this.w = ((int)(paramFloat3 * paramInt + 0.5F));
/* 123 */     this.h = ((int)(paramFloat4 * paramInt + 0.5F));
/*     */   }
/*     */ 
/*     */   public MediaPrintableArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 141 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt3 <= 0) || (paramInt4 <= 0) || (paramInt5 < 1))
/*     */     {
/* 143 */       throw new IllegalArgumentException("0 or negative value argument");
/*     */     }
/* 145 */     this.x = (paramInt1 * paramInt5);
/* 146 */     this.y = (paramInt2 * paramInt5);
/* 147 */     this.w = (paramInt3 * paramInt5);
/* 148 */     this.h = (paramInt4 * paramInt5);
/*     */   }
/*     */ 
/*     */   public float[] getPrintableArea(int paramInt)
/*     */   {
/* 165 */     return new float[] { getX(paramInt), getY(paramInt), getWidth(paramInt), getHeight(paramInt) };
/*     */   }
/*     */ 
/*     */   public float getX(int paramInt)
/*     */   {
/* 183 */     return convertFromMicrometers(this.x, paramInt);
/*     */   }
/*     */ 
/*     */   public float getY(int paramInt)
/*     */   {
/* 200 */     return convertFromMicrometers(this.y, paramInt);
/*     */   }
/*     */ 
/*     */   public float getWidth(int paramInt)
/*     */   {
/* 215 */     return convertFromMicrometers(this.w, paramInt);
/*     */   }
/*     */ 
/*     */   public float getHeight(int paramInt)
/*     */   {
/* 230 */     return convertFromMicrometers(this.h, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 252 */     boolean bool = false;
/* 253 */     if ((paramObject instanceof MediaPrintableArea)) {
/* 254 */       MediaPrintableArea localMediaPrintableArea = (MediaPrintableArea)paramObject;
/* 255 */       if ((this.x == localMediaPrintableArea.x) && (this.y == localMediaPrintableArea.y) && (this.w == localMediaPrintableArea.w) && (this.h == localMediaPrintableArea.h)) {
/* 256 */         bool = true;
/*     */       }
/*     */     }
/* 259 */     return bool;
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 273 */     return MediaPrintableArea.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 287 */     return "media-printable-area";
/*     */   }
/*     */ 
/*     */   public String toString(int paramInt, String paramString)
/*     */   {
/* 307 */     if (paramString == null) {
/* 308 */       paramString = "";
/*     */     }
/* 310 */     float[] arrayOfFloat = getPrintableArea(paramInt);
/* 311 */     String str = "(" + arrayOfFloat[0] + "," + arrayOfFloat[1] + ")->(" + arrayOfFloat[2] + "," + arrayOfFloat[3] + ")";
/* 312 */     return str + paramString;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 319 */     return toString(1000, "mm");
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 326 */     return this.x + 37 * this.y + 43 * this.w + 47 * this.h;
/*     */   }
/*     */ 
/*     */   private static float convertFromMicrometers(int paramInt1, int paramInt2) {
/* 330 */     if (paramInt2 < 1) {
/* 331 */       throw new IllegalArgumentException("units is < 1");
/*     */     }
/* 333 */     return paramInt1 / paramInt2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.MediaPrintableArea
 * JD-Core Version:    0.6.2
 */
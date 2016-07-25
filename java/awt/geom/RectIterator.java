/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class RectIterator
/*     */   implements PathIterator
/*     */ {
/*     */   double x;
/*     */   double y;
/*     */   double w;
/*     */   double h;
/*     */   AffineTransform affine;
/*     */   int index;
/*     */ 
/*     */   RectIterator(Rectangle2D paramRectangle2D, AffineTransform paramAffineTransform)
/*     */   {
/*  42 */     this.x = paramRectangle2D.getX();
/*  43 */     this.y = paramRectangle2D.getY();
/*  44 */     this.w = paramRectangle2D.getWidth();
/*  45 */     this.h = paramRectangle2D.getHeight();
/*  46 */     this.affine = paramAffineTransform;
/*  47 */     if ((this.w < 0.0D) || (this.h < 0.0D))
/*  48 */       this.index = 6;
/*     */   }
/*     */ 
/*     */   public int getWindingRule()
/*     */   {
/*  59 */     return 1;
/*     */   }
/*     */ 
/*     */   public boolean isDone()
/*     */   {
/*  67 */     return this.index > 5;
/*     */   }
/*     */ 
/*     */   public void next()
/*     */   {
/*  76 */     this.index += 1;
/*     */   }
/*     */ 
/*     */   public int currentSegment(float[] paramArrayOfFloat)
/*     */   {
/*  98 */     if (isDone()) {
/*  99 */       throw new NoSuchElementException("rect iterator out of bounds");
/*     */     }
/* 101 */     if (this.index == 5) {
/* 102 */       return 4;
/*     */     }
/* 104 */     paramArrayOfFloat[0] = ((float)this.x);
/* 105 */     paramArrayOfFloat[1] = ((float)this.y);
/* 106 */     if ((this.index == 1) || (this.index == 2)) {
/* 107 */       paramArrayOfFloat[0] += (float)this.w;
/*     */     }
/* 109 */     if ((this.index == 2) || (this.index == 3)) {
/* 110 */       paramArrayOfFloat[1] += (float)this.h;
/*     */     }
/* 112 */     if (this.affine != null) {
/* 113 */       this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, 1);
/*     */     }
/* 115 */     return this.index == 0 ? 0 : 1;
/*     */   }
/*     */ 
/*     */   public int currentSegment(double[] paramArrayOfDouble)
/*     */   {
/* 137 */     if (isDone()) {
/* 138 */       throw new NoSuchElementException("rect iterator out of bounds");
/*     */     }
/* 140 */     if (this.index == 5) {
/* 141 */       return 4;
/*     */     }
/* 143 */     paramArrayOfDouble[0] = this.x;
/* 144 */     paramArrayOfDouble[1] = this.y;
/* 145 */     if ((this.index == 1) || (this.index == 2)) {
/* 146 */       paramArrayOfDouble[0] += this.w;
/*     */     }
/* 148 */     if ((this.index == 2) || (this.index == 3)) {
/* 149 */       paramArrayOfDouble[1] += this.h;
/*     */     }
/* 151 */     if (this.affine != null) {
/* 152 */       this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, 1);
/*     */     }
/* 154 */     return this.index == 0 ? 0 : 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.RectIterator
 * JD-Core Version:    0.6.2
 */
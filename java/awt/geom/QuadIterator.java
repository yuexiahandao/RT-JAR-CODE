/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class QuadIterator
/*     */   implements PathIterator
/*     */ {
/*     */   QuadCurve2D quad;
/*     */   AffineTransform affine;
/*     */   int index;
/*     */ 
/*     */   QuadIterator(QuadCurve2D paramQuadCurve2D, AffineTransform paramAffineTransform)
/*     */   {
/*  42 */     this.quad = paramQuadCurve2D;
/*  43 */     this.affine = paramAffineTransform;
/*     */   }
/*     */ 
/*     */   public int getWindingRule()
/*     */   {
/*  53 */     return 1;
/*     */   }
/*     */ 
/*     */   public boolean isDone()
/*     */   {
/*  61 */     return this.index > 1;
/*     */   }
/*     */ 
/*     */   public void next()
/*     */   {
/*  70 */     this.index += 1;
/*     */   }
/*     */ 
/*     */   public int currentSegment(float[] paramArrayOfFloat)
/*     */   {
/*  92 */     if (isDone())
/*  93 */       throw new NoSuchElementException("quad iterator iterator out of bounds");
/*     */     int i;
/*  96 */     if (this.index == 0) {
/*  97 */       paramArrayOfFloat[0] = ((float)this.quad.getX1());
/*  98 */       paramArrayOfFloat[1] = ((float)this.quad.getY1());
/*  99 */       i = 0;
/*     */     } else {
/* 101 */       paramArrayOfFloat[0] = ((float)this.quad.getCtrlX());
/* 102 */       paramArrayOfFloat[1] = ((float)this.quad.getCtrlY());
/* 103 */       paramArrayOfFloat[2] = ((float)this.quad.getX2());
/* 104 */       paramArrayOfFloat[3] = ((float)this.quad.getY2());
/* 105 */       i = 2;
/*     */     }
/* 107 */     if (this.affine != null) {
/* 108 */       this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, this.index == 0 ? 1 : 2);
/*     */     }
/* 110 */     return i;
/*     */   }
/*     */ 
/*     */   public int currentSegment(double[] paramArrayOfDouble)
/*     */   {
/* 132 */     if (isDone())
/* 133 */       throw new NoSuchElementException("quad iterator iterator out of bounds");
/*     */     int i;
/* 136 */     if (this.index == 0) {
/* 137 */       paramArrayOfDouble[0] = this.quad.getX1();
/* 138 */       paramArrayOfDouble[1] = this.quad.getY1();
/* 139 */       i = 0;
/*     */     } else {
/* 141 */       paramArrayOfDouble[0] = this.quad.getCtrlX();
/* 142 */       paramArrayOfDouble[1] = this.quad.getCtrlY();
/* 143 */       paramArrayOfDouble[2] = this.quad.getX2();
/* 144 */       paramArrayOfDouble[3] = this.quad.getY2();
/* 145 */       i = 2;
/*     */     }
/* 147 */     if (this.affine != null) {
/* 148 */       this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, this.index == 0 ? 1 : 2);
/*     */     }
/* 150 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.QuadIterator
 * JD-Core Version:    0.6.2
 */
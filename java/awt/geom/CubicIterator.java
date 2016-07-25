/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class CubicIterator
/*     */   implements PathIterator
/*     */ {
/*     */   CubicCurve2D cubic;
/*     */   AffineTransform affine;
/*     */   int index;
/*     */ 
/*     */   CubicIterator(CubicCurve2D paramCubicCurve2D, AffineTransform paramAffineTransform)
/*     */   {
/*  42 */     this.cubic = paramCubicCurve2D;
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
/*  93 */       throw new NoSuchElementException("cubic iterator iterator out of bounds");
/*     */     int i;
/*  96 */     if (this.index == 0) {
/*  97 */       paramArrayOfFloat[0] = ((float)this.cubic.getX1());
/*  98 */       paramArrayOfFloat[1] = ((float)this.cubic.getY1());
/*  99 */       i = 0;
/*     */     } else {
/* 101 */       paramArrayOfFloat[0] = ((float)this.cubic.getCtrlX1());
/* 102 */       paramArrayOfFloat[1] = ((float)this.cubic.getCtrlY1());
/* 103 */       paramArrayOfFloat[2] = ((float)this.cubic.getCtrlX2());
/* 104 */       paramArrayOfFloat[3] = ((float)this.cubic.getCtrlY2());
/* 105 */       paramArrayOfFloat[4] = ((float)this.cubic.getX2());
/* 106 */       paramArrayOfFloat[5] = ((float)this.cubic.getY2());
/* 107 */       i = 3;
/*     */     }
/* 109 */     if (this.affine != null) {
/* 110 */       this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, this.index == 0 ? 1 : 3);
/*     */     }
/* 112 */     return i;
/*     */   }
/*     */ 
/*     */   public int currentSegment(double[] paramArrayOfDouble)
/*     */   {
/* 134 */     if (isDone())
/* 135 */       throw new NoSuchElementException("cubic iterator iterator out of bounds");
/*     */     int i;
/* 138 */     if (this.index == 0) {
/* 139 */       paramArrayOfDouble[0] = this.cubic.getX1();
/* 140 */       paramArrayOfDouble[1] = this.cubic.getY1();
/* 141 */       i = 0;
/*     */     } else {
/* 143 */       paramArrayOfDouble[0] = this.cubic.getCtrlX1();
/* 144 */       paramArrayOfDouble[1] = this.cubic.getCtrlY1();
/* 145 */       paramArrayOfDouble[2] = this.cubic.getCtrlX2();
/* 146 */       paramArrayOfDouble[3] = this.cubic.getCtrlY2();
/* 147 */       paramArrayOfDouble[4] = this.cubic.getX2();
/* 148 */       paramArrayOfDouble[5] = this.cubic.getY2();
/* 149 */       i = 3;
/*     */     }
/* 151 */     if (this.affine != null) {
/* 152 */       this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, this.index == 0 ? 1 : 3);
/*     */     }
/* 154 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.CubicIterator
 * JD-Core Version:    0.6.2
 */
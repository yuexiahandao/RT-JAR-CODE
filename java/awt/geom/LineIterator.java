/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class LineIterator
/*     */   implements PathIterator
/*     */ {
/*     */   Line2D line;
/*     */   AffineTransform affine;
/*     */   int index;
/*     */ 
/*     */   LineIterator(Line2D paramLine2D, AffineTransform paramAffineTransform)
/*     */   {
/*  42 */     this.line = paramLine2D;
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
/*  93 */       throw new NoSuchElementException("line iterator out of bounds");
/*     */     int i;
/*  96 */     if (this.index == 0) {
/*  97 */       paramArrayOfFloat[0] = ((float)this.line.getX1());
/*  98 */       paramArrayOfFloat[1] = ((float)this.line.getY1());
/*  99 */       i = 0;
/*     */     } else {
/* 101 */       paramArrayOfFloat[0] = ((float)this.line.getX2());
/* 102 */       paramArrayOfFloat[1] = ((float)this.line.getY2());
/* 103 */       i = 1;
/*     */     }
/* 105 */     if (this.affine != null) {
/* 106 */       this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, 1);
/*     */     }
/* 108 */     return i;
/*     */   }
/*     */ 
/*     */   public int currentSegment(double[] paramArrayOfDouble)
/*     */   {
/* 130 */     if (isDone())
/* 131 */       throw new NoSuchElementException("line iterator out of bounds");
/*     */     int i;
/* 134 */     if (this.index == 0) {
/* 135 */       paramArrayOfDouble[0] = this.line.getX1();
/* 136 */       paramArrayOfDouble[1] = this.line.getY1();
/* 137 */       i = 0;
/*     */     } else {
/* 139 */       paramArrayOfDouble[0] = this.line.getX2();
/* 140 */       paramArrayOfDouble[1] = this.line.getY2();
/* 141 */       i = 1;
/*     */     }
/* 143 */     if (this.affine != null) {
/* 144 */       this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, 1);
/*     */     }
/* 146 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.LineIterator
 * JD-Core Version:    0.6.2
 */
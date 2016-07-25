/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class EllipseIterator
/*     */   implements PathIterator
/*     */ {
/*     */   double x;
/*     */   double y;
/*     */   double w;
/*     */   double h;
/*     */   AffineTransform affine;
/*     */   int index;
/*     */   public static final double CtrlVal = 0.5522847498307933D;
/*     */   private static final double pcv = 0.7761423749153966D;
/*     */   private static final double ncv = 0.2238576250846033D;
/*  89 */   private static double[][] ctrlpts = { { 1.0D, 0.7761423749153966D, 0.7761423749153966D, 1.0D, 0.5D, 1.0D }, { 0.2238576250846033D, 1.0D, 0.0D, 0.7761423749153966D, 0.0D, 0.5D }, { 0.0D, 0.2238576250846033D, 0.2238576250846033D, 0.0D, 0.5D, 0.0D }, { 0.7761423749153966D, 0.0D, 1.0D, 0.2238576250846033D, 1.0D, 0.5D } };
/*     */ 
/*     */   EllipseIterator(Ellipse2D paramEllipse2D, AffineTransform paramAffineTransform)
/*     */   {
/*  42 */     this.x = paramEllipse2D.getX();
/*  43 */     this.y = paramEllipse2D.getY();
/*  44 */     this.w = paramEllipse2D.getWidth();
/*  45 */     this.h = paramEllipse2D.getHeight();
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
/* 115 */     if (isDone()) {
/* 116 */       throw new NoSuchElementException("ellipse iterator out of bounds");
/*     */     }
/* 118 */     if (this.index == 5) {
/* 119 */       return 4;
/*     */     }
/* 121 */     if (this.index == 0) {
/* 122 */       arrayOfDouble = ctrlpts[3];
/* 123 */       paramArrayOfFloat[0] = ((float)(this.x + arrayOfDouble[4] * this.w));
/* 124 */       paramArrayOfFloat[1] = ((float)(this.y + arrayOfDouble[5] * this.h));
/* 125 */       if (this.affine != null) {
/* 126 */         this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, 1);
/*     */       }
/* 128 */       return 0;
/*     */     }
/* 130 */     double[] arrayOfDouble = ctrlpts[(this.index - 1)];
/* 131 */     paramArrayOfFloat[0] = ((float)(this.x + arrayOfDouble[0] * this.w));
/* 132 */     paramArrayOfFloat[1] = ((float)(this.y + arrayOfDouble[1] * this.h));
/* 133 */     paramArrayOfFloat[2] = ((float)(this.x + arrayOfDouble[2] * this.w));
/* 134 */     paramArrayOfFloat[3] = ((float)(this.y + arrayOfDouble[3] * this.h));
/* 135 */     paramArrayOfFloat[4] = ((float)(this.x + arrayOfDouble[4] * this.w));
/* 136 */     paramArrayOfFloat[5] = ((float)(this.y + arrayOfDouble[5] * this.h));
/* 137 */     if (this.affine != null) {
/* 138 */       this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, 3);
/*     */     }
/* 140 */     return 3;
/*     */   }
/*     */ 
/*     */   public int currentSegment(double[] paramArrayOfDouble)
/*     */   {
/* 162 */     if (isDone()) {
/* 163 */       throw new NoSuchElementException("ellipse iterator out of bounds");
/*     */     }
/* 165 */     if (this.index == 5) {
/* 166 */       return 4;
/*     */     }
/* 168 */     if (this.index == 0) {
/* 169 */       arrayOfDouble = ctrlpts[3];
/* 170 */       paramArrayOfDouble[0] = (this.x + arrayOfDouble[4] * this.w);
/* 171 */       paramArrayOfDouble[1] = (this.y + arrayOfDouble[5] * this.h);
/* 172 */       if (this.affine != null) {
/* 173 */         this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, 1);
/*     */       }
/* 175 */       return 0;
/*     */     }
/* 177 */     double[] arrayOfDouble = ctrlpts[(this.index - 1)];
/* 178 */     paramArrayOfDouble[0] = (this.x + arrayOfDouble[0] * this.w);
/* 179 */     paramArrayOfDouble[1] = (this.y + arrayOfDouble[1] * this.h);
/* 180 */     paramArrayOfDouble[2] = (this.x + arrayOfDouble[2] * this.w);
/* 181 */     paramArrayOfDouble[3] = (this.y + arrayOfDouble[3] * this.h);
/* 182 */     paramArrayOfDouble[4] = (this.x + arrayOfDouble[4] * this.w);
/* 183 */     paramArrayOfDouble[5] = (this.y + arrayOfDouble[5] * this.h);
/* 184 */     if (this.affine != null) {
/* 185 */       this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, 3);
/*     */     }
/* 187 */     return 3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.EllipseIterator
 * JD-Core Version:    0.6.2
 */
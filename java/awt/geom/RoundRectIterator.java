/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class RoundRectIterator
/*     */   implements PathIterator
/*     */ {
/*     */   double x;
/*     */   double y;
/*     */   double w;
/*     */   double h;
/*     */   double aw;
/*     */   double ah;
/*     */   AffineTransform affine;
/*     */   int index;
/*     */   private static final double angle = 0.7853981633974483D;
/*  83 */   private static final double a = 1.0D - Math.cos(0.7853981633974483D);
/*  84 */   private static final double b = Math.tan(0.7853981633974483D);
/*  85 */   private static final double c = Math.sqrt(1.0D + b * b) - 1.0D + a;
/*  86 */   private static final double cv = 1.333333333333333D * a * b / c;
/*  87 */   private static final double acv = (1.0D - cv) / 2.0D;
/*     */ 
/*  93 */   private static double[][] ctrlpts = { { 0.0D, 0.0D, 0.0D, 0.5D }, { 0.0D, 0.0D, 1.0D, -0.5D }, { 0.0D, 0.0D, 1.0D, -acv, 0.0D, acv, 1.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.0D }, { 1.0D, -0.5D, 1.0D, 0.0D }, { 1.0D, -acv, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D, -acv, 1.0D, 0.0D, 1.0D, -0.5D }, { 1.0D, 0.0D, 0.0D, 0.5D }, { 1.0D, 0.0D, 0.0D, acv, 1.0D, -acv, 0.0D, 0.0D, 1.0D, -0.5D, 0.0D, 0.0D }, { 0.0D, 0.5D, 0.0D, 0.0D }, { 0.0D, acv, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, acv, 0.0D, 0.0D, 0.0D, 0.5D }, new double[0] };
/*     */ 
/* 113 */   private static int[] types = { 0, 1, 3, 1, 3, 1, 3, 1, 3, 4 };
/*     */ 
/*     */   RoundRectIterator(RoundRectangle2D paramRoundRectangle2D, AffineTransform paramAffineTransform)
/*     */   {
/*  42 */     this.x = paramRoundRectangle2D.getX();
/*  43 */     this.y = paramRoundRectangle2D.getY();
/*  44 */     this.w = paramRoundRectangle2D.getWidth();
/*  45 */     this.h = paramRoundRectangle2D.getHeight();
/*  46 */     this.aw = Math.min(this.w, Math.abs(paramRoundRectangle2D.getArcWidth()));
/*  47 */     this.ah = Math.min(this.h, Math.abs(paramRoundRectangle2D.getArcHeight()));
/*  48 */     this.affine = paramAffineTransform;
/*  49 */     if ((this.aw < 0.0D) || (this.ah < 0.0D))
/*     */     {
/*  51 */       this.index = ctrlpts.length;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getWindingRule()
/*     */   {
/*  62 */     return 1;
/*     */   }
/*     */ 
/*     */   public boolean isDone()
/*     */   {
/*  70 */     return this.index >= ctrlpts.length;
/*     */   }
/*     */ 
/*     */   public void next()
/*     */   {
/*  79 */     this.index += 1;
/*     */   }
/*     */ 
/*     */   public int currentSegment(float[] paramArrayOfFloat)
/*     */   {
/* 141 */     if (isDone()) {
/* 142 */       throw new NoSuchElementException("roundrect iterator out of bounds");
/*     */     }
/* 144 */     double[] arrayOfDouble = ctrlpts[this.index];
/* 145 */     int i = 0;
/* 146 */     for (int j = 0; j < arrayOfDouble.length; j += 4) {
/* 147 */       paramArrayOfFloat[(i++)] = ((float)(this.x + arrayOfDouble[(j + 0)] * this.w + arrayOfDouble[(j + 1)] * this.aw));
/* 148 */       paramArrayOfFloat[(i++)] = ((float)(this.y + arrayOfDouble[(j + 2)] * this.h + arrayOfDouble[(j + 3)] * this.ah));
/*     */     }
/* 150 */     if (this.affine != null) {
/* 151 */       this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, i / 2);
/*     */     }
/* 153 */     return types[this.index];
/*     */   }
/*     */ 
/*     */   public int currentSegment(double[] paramArrayOfDouble)
/*     */   {
/* 175 */     if (isDone()) {
/* 176 */       throw new NoSuchElementException("roundrect iterator out of bounds");
/*     */     }
/* 178 */     double[] arrayOfDouble = ctrlpts[this.index];
/* 179 */     int i = 0;
/* 180 */     for (int j = 0; j < arrayOfDouble.length; j += 4) {
/* 181 */       paramArrayOfDouble[(i++)] = (this.x + arrayOfDouble[(j + 0)] * this.w + arrayOfDouble[(j + 1)] * this.aw);
/* 182 */       paramArrayOfDouble[(i++)] = (this.y + arrayOfDouble[(j + 2)] * this.h + arrayOfDouble[(j + 3)] * this.ah);
/*     */     }
/* 184 */     if (this.affine != null) {
/* 185 */       this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, i / 2);
/*     */     }
/* 187 */     return types[this.index];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.RoundRectIterator
 * JD-Core Version:    0.6.2
 */
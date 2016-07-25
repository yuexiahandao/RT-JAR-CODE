/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class ArcIterator
/*     */   implements PathIterator
/*     */ {
/*     */   double x;
/*     */   double y;
/*     */   double w;
/*     */   double h;
/*     */   double angStRad;
/*     */   double increment;
/*     */   double cv;
/*     */   AffineTransform affine;
/*     */   int index;
/*     */   int arcSegs;
/*     */   int lineSegs;
/*     */ 
/*     */   ArcIterator(Arc2D paramArc2D, AffineTransform paramAffineTransform)
/*     */   {
/*  44 */     this.w = (paramArc2D.getWidth() / 2.0D);
/*  45 */     this.h = (paramArc2D.getHeight() / 2.0D);
/*  46 */     this.x = (paramArc2D.getX() + this.w);
/*  47 */     this.y = (paramArc2D.getY() + this.h);
/*  48 */     this.angStRad = (-Math.toRadians(paramArc2D.getAngleStart()));
/*  49 */     this.affine = paramAffineTransform;
/*  50 */     double d = -paramArc2D.getAngleExtent();
/*  51 */     if ((d >= 360.0D) || (d <= -360.0D)) {
/*  52 */       this.arcSegs = 4;
/*  53 */       this.increment = 1.570796326794897D;
/*     */ 
/*  55 */       this.cv = 0.5522847498307933D;
/*  56 */       if (d < 0.0D) {
/*  57 */         this.increment = (-this.increment);
/*  58 */         this.cv = (-this.cv);
/*     */       }
/*     */     } else {
/*  61 */       this.arcSegs = ((int)Math.ceil(Math.abs(d) / 90.0D));
/*  62 */       this.increment = Math.toRadians(d / this.arcSegs);
/*  63 */       this.cv = btan(this.increment);
/*  64 */       if (this.cv == 0.0D) {
/*  65 */         this.arcSegs = 0;
/*     */       }
/*     */     }
/*  68 */     switch (paramArc2D.getArcType()) {
/*     */     case 0:
/*  70 */       this.lineSegs = 0;
/*  71 */       break;
/*     */     case 1:
/*  73 */       this.lineSegs = 1;
/*  74 */       break;
/*     */     case 2:
/*  76 */       this.lineSegs = 2;
/*     */     }
/*     */ 
/*  79 */     if ((this.w < 0.0D) || (this.h < 0.0D))
/*  80 */       this.arcSegs = (this.lineSegs = -1);
/*     */   }
/*     */ 
/*     */   public int getWindingRule()
/*     */   {
/*  91 */     return 1;
/*     */   }
/*     */ 
/*     */   public boolean isDone()
/*     */   {
/*  99 */     return this.index > this.arcSegs + this.lineSegs;
/*     */   }
/*     */ 
/*     */   public void next()
/*     */   {
/* 108 */     this.index += 1;
/*     */   }
/*     */ 
/*     */   private static double btan(double paramDouble)
/*     */   {
/* 189 */     paramDouble /= 2.0D;
/* 190 */     return 1.333333333333333D * Math.sin(paramDouble) / (1.0D + Math.cos(paramDouble));
/*     */   }
/*     */ 
/*     */   public int currentSegment(float[] paramArrayOfFloat)
/*     */   {
/* 212 */     if (isDone()) {
/* 213 */       throw new NoSuchElementException("arc iterator out of bounds");
/*     */     }
/* 215 */     double d1 = this.angStRad;
/* 216 */     if (this.index == 0) {
/* 217 */       paramArrayOfFloat[0] = ((float)(this.x + Math.cos(d1) * this.w));
/* 218 */       paramArrayOfFloat[1] = ((float)(this.y + Math.sin(d1) * this.h));
/* 219 */       if (this.affine != null) {
/* 220 */         this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, 1);
/*     */       }
/* 222 */       return 0;
/*     */     }
/* 224 */     if (this.index > this.arcSegs) {
/* 225 */       if (this.index == this.arcSegs + this.lineSegs) {
/* 226 */         return 4;
/*     */       }
/* 228 */       paramArrayOfFloat[0] = ((float)this.x);
/* 229 */       paramArrayOfFloat[1] = ((float)this.y);
/* 230 */       if (this.affine != null) {
/* 231 */         this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, 1);
/*     */       }
/* 233 */       return 1;
/*     */     }
/* 235 */     d1 += this.increment * (this.index - 1);
/* 236 */     double d2 = Math.cos(d1);
/* 237 */     double d3 = Math.sin(d1);
/* 238 */     paramArrayOfFloat[0] = ((float)(this.x + (d2 - this.cv * d3) * this.w));
/* 239 */     paramArrayOfFloat[1] = ((float)(this.y + (d3 + this.cv * d2) * this.h));
/* 240 */     d1 += this.increment;
/* 241 */     d2 = Math.cos(d1);
/* 242 */     d3 = Math.sin(d1);
/* 243 */     paramArrayOfFloat[2] = ((float)(this.x + (d2 + this.cv * d3) * this.w));
/* 244 */     paramArrayOfFloat[3] = ((float)(this.y + (d3 - this.cv * d2) * this.h));
/* 245 */     paramArrayOfFloat[4] = ((float)(this.x + d2 * this.w));
/* 246 */     paramArrayOfFloat[5] = ((float)(this.y + d3 * this.h));
/* 247 */     if (this.affine != null) {
/* 248 */       this.affine.transform(paramArrayOfFloat, 0, paramArrayOfFloat, 0, 3);
/*     */     }
/* 250 */     return 3;
/*     */   }
/*     */ 
/*     */   public int currentSegment(double[] paramArrayOfDouble)
/*     */   {
/* 272 */     if (isDone()) {
/* 273 */       throw new NoSuchElementException("arc iterator out of bounds");
/*     */     }
/* 275 */     double d1 = this.angStRad;
/* 276 */     if (this.index == 0) {
/* 277 */       paramArrayOfDouble[0] = (this.x + Math.cos(d1) * this.w);
/* 278 */       paramArrayOfDouble[1] = (this.y + Math.sin(d1) * this.h);
/* 279 */       if (this.affine != null) {
/* 280 */         this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, 1);
/*     */       }
/* 282 */       return 0;
/*     */     }
/* 284 */     if (this.index > this.arcSegs) {
/* 285 */       if (this.index == this.arcSegs + this.lineSegs) {
/* 286 */         return 4;
/*     */       }
/* 288 */       paramArrayOfDouble[0] = this.x;
/* 289 */       paramArrayOfDouble[1] = this.y;
/* 290 */       if (this.affine != null) {
/* 291 */         this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, 1);
/*     */       }
/* 293 */       return 1;
/*     */     }
/* 295 */     d1 += this.increment * (this.index - 1);
/* 296 */     double d2 = Math.cos(d1);
/* 297 */     double d3 = Math.sin(d1);
/* 298 */     paramArrayOfDouble[0] = (this.x + (d2 - this.cv * d3) * this.w);
/* 299 */     paramArrayOfDouble[1] = (this.y + (d3 + this.cv * d2) * this.h);
/* 300 */     d1 += this.increment;
/* 301 */     d2 = Math.cos(d1);
/* 302 */     d3 = Math.sin(d1);
/* 303 */     paramArrayOfDouble[2] = (this.x + (d2 + this.cv * d3) * this.w);
/* 304 */     paramArrayOfDouble[3] = (this.y + (d3 - this.cv * d2) * this.h);
/* 305 */     paramArrayOfDouble[4] = (this.x + d2 * this.w);
/* 306 */     paramArrayOfDouble[5] = (this.y + d3 * this.h);
/* 307 */     if (this.affine != null) {
/* 308 */       this.affine.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, 3);
/*     */     }
/* 310 */     return 3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.ArcIterator
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.awt.geom;
/*     */ 
/*     */ import java.awt.geom.PathIterator;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public abstract class Crossings
/*     */ {
/*     */   public static final boolean debug = false;
/*  35 */   int limit = 0;
/*  36 */   double[] yranges = new double[10];
/*     */   double xlo;
/*     */   double ylo;
/*     */   double xhi;
/*     */   double yhi;
/* 240 */   private Vector tmp = new Vector();
/*     */ 
/*     */   public Crossings(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/*  41 */     this.xlo = paramDouble1;
/*  42 */     this.ylo = paramDouble2;
/*  43 */     this.xhi = paramDouble3;
/*  44 */     this.yhi = paramDouble4;
/*     */   }
/*     */ 
/*     */   public final double getXLo() {
/*  48 */     return this.xlo;
/*     */   }
/*     */ 
/*     */   public final double getYLo() {
/*  52 */     return this.ylo;
/*     */   }
/*     */ 
/*     */   public final double getXHi() {
/*  56 */     return this.xhi;
/*     */   }
/*     */ 
/*     */   public final double getYHi() {
/*  60 */     return this.yhi;
/*     */   }
/*     */ 
/*     */   public abstract void record(double paramDouble1, double paramDouble2, int paramInt);
/*     */ 
/*     */   public void print() {
/*  66 */     System.out.println("Crossings [");
/*  67 */     System.out.println("  bounds = [" + this.ylo + ", " + this.yhi + "]");
/*  68 */     for (int i = 0; i < this.limit; i += 2) {
/*  69 */       System.out.println("  [" + this.yranges[i] + ", " + this.yranges[(i + 1)] + "]");
/*     */     }
/*  71 */     System.out.println("]");
/*     */   }
/*     */ 
/*     */   public final boolean isEmpty() {
/*  75 */     return this.limit == 0;
/*     */   }
/*     */ 
/*     */   public abstract boolean covers(double paramDouble1, double paramDouble2);
/*     */ 
/*     */   public static Crossings findCrossings(Vector paramVector, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/*  84 */     EvenOdd localEvenOdd = new EvenOdd(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*  85 */     Enumeration localEnumeration = paramVector.elements();
/*  86 */     while (localEnumeration.hasMoreElements()) {
/*  87 */       Curve localCurve = (Curve)localEnumeration.nextElement();
/*  88 */       if (localCurve.accumulateCrossings(localEvenOdd)) {
/*  89 */         return null;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  95 */     return localEvenOdd;
/*     */   }
/*     */ 
/*     */   public static Crossings findCrossings(PathIterator paramPathIterator, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/*     */     Object localObject;
/* 103 */     if (paramPathIterator.getWindingRule() == 0)
/* 104 */       localObject = new EvenOdd(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */     else {
/* 106 */       localObject = new NonZero(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */     }
/*     */ 
/* 122 */     double[] arrayOfDouble = new double[23];
/* 123 */     double d1 = 0.0D;
/* 124 */     double d2 = 0.0D;
/* 125 */     double d3 = 0.0D;
/* 126 */     double d4 = 0.0D;
/*     */ 
/* 128 */     while (!paramPathIterator.isDone()) {
/* 129 */       int i = paramPathIterator.currentSegment(arrayOfDouble);
/*     */       double d5;
/*     */       double d6;
/* 130 */       switch (i) {
/*     */       case 0:
/* 132 */         if ((d2 != d4) && (((Crossings)localObject).accumulateLine(d3, d4, d1, d2)))
/*     */         {
/* 135 */           return null;
/*     */         }
/* 137 */         d1 = d3 = arrayOfDouble[0];
/* 138 */         d2 = d4 = arrayOfDouble[1];
/* 139 */         break;
/*     */       case 1:
/* 141 */         d5 = arrayOfDouble[0];
/* 142 */         d6 = arrayOfDouble[1];
/* 143 */         if (((Crossings)localObject).accumulateLine(d3, d4, d5, d6)) {
/* 144 */           return null;
/*     */         }
/* 146 */         d3 = d5;
/* 147 */         d4 = d6;
/* 148 */         break;
/*     */       case 2:
/* 150 */         d5 = arrayOfDouble[2];
/* 151 */         d6 = arrayOfDouble[3];
/* 152 */         if (((Crossings)localObject).accumulateQuad(d3, d4, arrayOfDouble)) {
/* 153 */           return null;
/*     */         }
/* 155 */         d3 = d5;
/* 156 */         d4 = d6;
/* 157 */         break;
/*     */       case 3:
/* 159 */         d5 = arrayOfDouble[4];
/* 160 */         d6 = arrayOfDouble[5];
/* 161 */         if (((Crossings)localObject).accumulateCubic(d3, d4, arrayOfDouble)) {
/* 162 */           return null;
/*     */         }
/* 164 */         d3 = d5;
/* 165 */         d4 = d6;
/* 166 */         break;
/*     */       case 4:
/* 168 */         if ((d2 != d4) && (((Crossings)localObject).accumulateLine(d3, d4, d1, d2)))
/*     */         {
/* 171 */           return null;
/*     */         }
/* 173 */         d3 = d1;
/* 174 */         d4 = d2;
/*     */       }
/*     */ 
/* 177 */       paramPathIterator.next();
/*     */     }
/* 179 */     if ((d2 != d4) && 
/* 180 */       (((Crossings)localObject).accumulateLine(d3, d4, d1, d2))) {
/* 181 */       return null;
/*     */     }
/*     */ 
/* 187 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean accumulateLine(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 193 */     if (paramDouble2 <= paramDouble4) {
/* 194 */       return accumulateLine(paramDouble1, paramDouble2, paramDouble3, paramDouble4, 1);
/*     */     }
/* 196 */     return accumulateLine(paramDouble3, paramDouble4, paramDouble1, paramDouble2, -1);
/*     */   }
/*     */ 
/*     */   public boolean accumulateLine(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt)
/*     */   {
/* 204 */     if ((this.yhi <= paramDouble2) || (this.ylo >= paramDouble4)) {
/* 205 */       return false;
/*     */     }
/* 207 */     if ((paramDouble1 >= this.xhi) && (paramDouble3 >= this.xhi)) {
/* 208 */       return false;
/*     */     }
/* 210 */     if (paramDouble2 == paramDouble4) {
/* 211 */       return (paramDouble1 >= this.xlo) || (paramDouble3 >= this.xlo);
/*     */     }
/*     */ 
/* 214 */     double d5 = paramDouble3 - paramDouble1;
/* 215 */     double d6 = paramDouble4 - paramDouble2;
/*     */     double d1;
/*     */     double d2;
/* 216 */     if (paramDouble2 < this.ylo) {
/* 217 */       d1 = paramDouble1 + (this.ylo - paramDouble2) * d5 / d6;
/* 218 */       d2 = this.ylo;
/*     */     } else {
/* 220 */       d1 = paramDouble1;
/* 221 */       d2 = paramDouble2;
/*     */     }
/*     */     double d3;
/*     */     double d4;
/* 223 */     if (this.yhi < paramDouble4) {
/* 224 */       d3 = paramDouble1 + (this.yhi - paramDouble2) * d5 / d6;
/* 225 */       d4 = this.yhi;
/*     */     } else {
/* 227 */       d3 = paramDouble3;
/* 228 */       d4 = paramDouble4;
/*     */     }
/* 230 */     if ((d1 >= this.xhi) && (d3 >= this.xhi)) {
/* 231 */       return false;
/*     */     }
/* 233 */     if ((d1 > this.xlo) || (d3 > this.xlo)) {
/* 234 */       return true;
/*     */     }
/* 236 */     record(d2, d4, paramInt);
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean accumulateQuad(double paramDouble1, double paramDouble2, double[] paramArrayOfDouble)
/*     */   {
/* 243 */     if ((paramDouble2 < this.ylo) && (paramArrayOfDouble[1] < this.ylo) && (paramArrayOfDouble[3] < this.ylo)) {
/* 244 */       return false;
/*     */     }
/* 246 */     if ((paramDouble2 > this.yhi) && (paramArrayOfDouble[1] > this.yhi) && (paramArrayOfDouble[3] > this.yhi)) {
/* 247 */       return false;
/*     */     }
/* 249 */     if ((paramDouble1 > this.xhi) && (paramArrayOfDouble[0] > this.xhi) && (paramArrayOfDouble[2] > this.xhi)) {
/* 250 */       return false;
/*     */     }
/* 252 */     if ((paramDouble1 < this.xlo) && (paramArrayOfDouble[0] < this.xlo) && (paramArrayOfDouble[2] < this.xlo)) {
/* 253 */       if (paramDouble2 < paramArrayOfDouble[3])
/* 254 */         record(Math.max(paramDouble2, this.ylo), Math.min(paramArrayOfDouble[3], this.yhi), 1);
/* 255 */       else if (paramDouble2 > paramArrayOfDouble[3]) {
/* 256 */         record(Math.max(paramArrayOfDouble[3], this.ylo), Math.min(paramDouble2, this.yhi), -1);
/*     */       }
/* 258 */       return false;
/*     */     }
/* 260 */     Curve.insertQuad(this.tmp, paramDouble1, paramDouble2, paramArrayOfDouble);
/* 261 */     Enumeration localEnumeration = this.tmp.elements();
/* 262 */     while (localEnumeration.hasMoreElements()) {
/* 263 */       Curve localCurve = (Curve)localEnumeration.nextElement();
/* 264 */       if (localCurve.accumulateCrossings(this)) {
/* 265 */         return true;
/*     */       }
/*     */     }
/* 268 */     this.tmp.clear();
/* 269 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean accumulateCubic(double paramDouble1, double paramDouble2, double[] paramArrayOfDouble) {
/* 273 */     if ((paramDouble2 < this.ylo) && (paramArrayOfDouble[1] < this.ylo) && (paramArrayOfDouble[3] < this.ylo) && (paramArrayOfDouble[5] < this.ylo))
/*     */     {
/* 276 */       return false;
/*     */     }
/* 278 */     if ((paramDouble2 > this.yhi) && (paramArrayOfDouble[1] > this.yhi) && (paramArrayOfDouble[3] > this.yhi) && (paramArrayOfDouble[5] > this.yhi))
/*     */     {
/* 281 */       return false;
/*     */     }
/* 283 */     if ((paramDouble1 > this.xhi) && (paramArrayOfDouble[0] > this.xhi) && (paramArrayOfDouble[2] > this.xhi) && (paramArrayOfDouble[4] > this.xhi))
/*     */     {
/* 286 */       return false;
/*     */     }
/* 288 */     if ((paramDouble1 < this.xlo) && (paramArrayOfDouble[0] < this.xlo) && (paramArrayOfDouble[2] < this.xlo) && (paramArrayOfDouble[4] < this.xlo))
/*     */     {
/* 291 */       if (paramDouble2 <= paramArrayOfDouble[5])
/* 292 */         record(Math.max(paramDouble2, this.ylo), Math.min(paramArrayOfDouble[5], this.yhi), 1);
/*     */       else {
/* 294 */         record(Math.max(paramArrayOfDouble[5], this.ylo), Math.min(paramDouble2, this.yhi), -1);
/*     */       }
/* 296 */       return false;
/*     */     }
/* 298 */     Curve.insertCubic(this.tmp, paramDouble1, paramDouble2, paramArrayOfDouble);
/* 299 */     Enumeration localEnumeration = this.tmp.elements();
/* 300 */     while (localEnumeration.hasMoreElements()) {
/* 301 */       Curve localCurve = (Curve)localEnumeration.nextElement();
/* 302 */       if (localCurve.accumulateCrossings(this)) {
/* 303 */         return true;
/*     */       }
/*     */     }
/* 306 */     this.tmp.clear();
/* 307 */     return false;
/*     */   }
/*     */ 
/*     */   public static final class EvenOdd extends Crossings {
/*     */     public EvenOdd(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 312 */       super(paramDouble2, paramDouble3, paramDouble4);
/*     */     }
/*     */ 
/*     */     public final boolean covers(double paramDouble1, double paramDouble2) {
/* 316 */       return (this.limit == 2) && (this.yranges[0] <= paramDouble1) && (this.yranges[1] >= paramDouble2);
/*     */     }
/*     */ 
/*     */     public void record(double paramDouble1, double paramDouble2, int paramInt) {
/* 320 */       if (paramDouble1 >= paramDouble2) {
/* 321 */         return;
/*     */       }
/* 323 */       int i = 0;
/*     */ 
/* 325 */       while ((i < this.limit) && (paramDouble1 > this.yranges[(i + 1)])) {
/* 326 */         i += 2;
/*     */       }
/* 328 */       int j = i;
/* 329 */       while (i < this.limit) {
/* 330 */         double d1 = this.yranges[(i++)];
/* 331 */         double d2 = this.yranges[(i++)];
/* 332 */         if (paramDouble2 < d1)
/*     */         {
/* 334 */           this.yranges[(j++)] = paramDouble1;
/* 335 */           this.yranges[(j++)] = paramDouble2;
/* 336 */           paramDouble1 = d1;
/* 337 */           paramDouble2 = d2;
/*     */         }
/*     */         else
/*     */         {
/*     */           double d3;
/*     */           double d4;
/* 342 */           if (paramDouble1 < d1) {
/* 343 */             d3 = paramDouble1;
/* 344 */             d4 = d1;
/*     */           } else {
/* 346 */             d3 = d1;
/* 347 */             d4 = paramDouble1;
/*     */           }
/*     */           double d5;
/*     */           double d6;
/* 349 */           if (paramDouble2 < d2) {
/* 350 */             d5 = paramDouble2;
/* 351 */             d6 = d2;
/*     */           } else {
/* 353 */             d5 = d2;
/* 354 */             d6 = paramDouble2;
/*     */           }
/* 356 */           if (d4 == d5) {
/* 357 */             paramDouble1 = d3;
/* 358 */             paramDouble2 = d6;
/*     */           } else {
/* 360 */             if (d4 > d5) {
/* 361 */               paramDouble1 = d5;
/* 362 */               d5 = d4;
/* 363 */               d4 = paramDouble1;
/*     */             }
/* 365 */             if (d3 != d4) {
/* 366 */               this.yranges[(j++)] = d3;
/* 367 */               this.yranges[(j++)] = d4;
/*     */             }
/* 369 */             paramDouble1 = d5;
/* 370 */             paramDouble2 = d6;
/*     */           }
/* 372 */           if (paramDouble1 >= paramDouble2)
/*     */             break;
/*     */         }
/*     */       }
/* 376 */       if ((j < i) && (i < this.limit)) {
/* 377 */         System.arraycopy(this.yranges, i, this.yranges, j, this.limit - i);
/*     */       }
/* 379 */       j += this.limit - i;
/* 380 */       if (paramDouble1 < paramDouble2) {
/* 381 */         if (j >= this.yranges.length) {
/* 382 */           double[] arrayOfDouble = new double[j + 10];
/* 383 */           System.arraycopy(this.yranges, 0, arrayOfDouble, 0, j);
/* 384 */           this.yranges = arrayOfDouble;
/*     */         }
/* 386 */         this.yranges[(j++)] = paramDouble1;
/* 387 */         this.yranges[(j++)] = paramDouble2;
/*     */       }
/* 389 */       this.limit = j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class NonZero extends Crossings {
/*     */     private int[] crosscounts;
/*     */ 
/*     */     public NonZero(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
/* 397 */       super(paramDouble2, paramDouble3, paramDouble4);
/* 398 */       this.crosscounts = new int[this.yranges.length / 2];
/*     */     }
/*     */ 
/*     */     public final boolean covers(double paramDouble1, double paramDouble2) {
/* 402 */       int i = 0;
/* 403 */       while (i < this.limit) {
/* 404 */         double d1 = this.yranges[(i++)];
/* 405 */         double d2 = this.yranges[(i++)];
/* 406 */         if (paramDouble1 < d2)
/*     */         {
/* 409 */           if (paramDouble1 < d1) {
/* 410 */             return false;
/*     */           }
/* 412 */           if (paramDouble2 <= d2) {
/* 413 */             return true;
/*     */           }
/* 415 */           paramDouble1 = d2;
/*     */         }
/*     */       }
/* 417 */       return paramDouble1 >= paramDouble2;
/*     */     }
/*     */ 
/*     */     public void remove(int paramInt) {
/* 421 */       this.limit -= 2;
/* 422 */       int i = this.limit - paramInt;
/* 423 */       if (i > 0) {
/* 424 */         System.arraycopy(this.yranges, paramInt + 2, this.yranges, paramInt, i);
/* 425 */         System.arraycopy(this.crosscounts, paramInt / 2 + 1, this.crosscounts, paramInt / 2, i / 2);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void insert(int paramInt1, double paramDouble1, double paramDouble2, int paramInt2)
/*     */     {
/* 432 */       int i = this.limit - paramInt1;
/* 433 */       double[] arrayOfDouble = this.yranges;
/* 434 */       int[] arrayOfInt = this.crosscounts;
/* 435 */       if (this.limit >= this.yranges.length) {
/* 436 */         this.yranges = new double[this.limit + 10];
/* 437 */         System.arraycopy(arrayOfDouble, 0, this.yranges, 0, paramInt1);
/* 438 */         this.crosscounts = new int[(this.limit + 10) / 2];
/* 439 */         System.arraycopy(arrayOfInt, 0, this.crosscounts, 0, paramInt1 / 2);
/*     */       }
/* 441 */       if (i > 0) {
/* 442 */         System.arraycopy(arrayOfDouble, paramInt1, this.yranges, paramInt1 + 2, i);
/* 443 */         System.arraycopy(arrayOfInt, paramInt1 / 2, this.crosscounts, paramInt1 / 2 + 1, i / 2);
/*     */       }
/*     */ 
/* 447 */       this.yranges[(paramInt1 + 0)] = paramDouble1;
/* 448 */       this.yranges[(paramInt1 + 1)] = paramDouble2;
/* 449 */       this.crosscounts[(paramInt1 / 2)] = paramInt2;
/* 450 */       this.limit += 2;
/*     */     }
/*     */ 
/*     */     public void record(double paramDouble1, double paramDouble2, int paramInt) {
/* 454 */       if (paramDouble1 >= paramDouble2) {
/* 455 */         return;
/*     */       }
/* 457 */       int i = 0;
/*     */ 
/* 459 */       while ((i < this.limit) && (paramDouble1 > this.yranges[(i + 1)])) {
/* 460 */         i += 2;
/*     */       }
/* 462 */       if (i < this.limit) {
/* 463 */         int j = this.crosscounts[(i / 2)];
/* 464 */         double d1 = this.yranges[(i + 0)];
/* 465 */         double d2 = this.yranges[(i + 1)];
/* 466 */         if ((d2 == paramDouble1) && (j == paramInt))
/*     */         {
/* 472 */           if (i + 2 == this.limit) {
/* 473 */             this.yranges[(i + 1)] = paramDouble2;
/* 474 */             return;
/*     */           }
/* 476 */           remove(i);
/* 477 */           paramDouble1 = d1;
/* 478 */           j = this.crosscounts[(i / 2)];
/* 479 */           d1 = this.yranges[(i + 0)];
/* 480 */           d2 = this.yranges[(i + 1)];
/*     */         }
/* 482 */         if (paramDouble2 < d1)
/*     */         {
/* 484 */           insert(i, paramDouble1, paramDouble2, paramInt);
/* 485 */           return;
/*     */         }
/* 487 */         if ((paramDouble2 == d1) && (j == paramInt))
/*     */         {
/* 489 */           this.yranges[i] = paramDouble1;
/* 490 */           return;
/*     */         }
/*     */ 
/* 493 */         if (paramDouble1 < d1) {
/* 494 */           insert(i, paramDouble1, d1, paramInt);
/* 495 */           i += 2;
/* 496 */           paramDouble1 = d1;
/* 497 */         } else if (d1 < paramDouble1) {
/* 498 */           insert(i, d1, paramDouble1, j);
/* 499 */           i += 2;
/* 500 */           d1 = paramDouble1;
/*     */         }
/*     */ 
/* 503 */         int k = j + paramInt;
/* 504 */         double d3 = Math.min(paramDouble2, d2);
/* 505 */         if (k == 0) {
/* 506 */           remove(i);
/*     */         } else {
/* 508 */           this.crosscounts[(i / 2)] = k;
/* 509 */           this.yranges[(i++)] = paramDouble1;
/* 510 */           this.yranges[(i++)] = d3;
/*     */         }
/* 512 */         paramDouble1 = d1 = d3;
/* 513 */         if (d1 < d2) {
/* 514 */           insert(i, d1, d2, j);
/*     */         }
/*     */       }
/* 517 */       if (paramDouble1 < paramDouble2)
/* 518 */         insert(i, paramDouble1, paramDouble2, paramInt);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.Crossings
 * JD-Core Version:    0.6.2
 */
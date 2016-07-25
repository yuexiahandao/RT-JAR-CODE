/*     */ package sun.awt.geom;
/*     */ 
/*     */ import java.awt.geom.Rectangle2D;
/*     */ 
/*     */ final class Order1 extends Curve
/*     */ {
/*     */   private double x0;
/*     */   private double y0;
/*     */   private double x1;
/*     */   private double y1;
/*     */   private double xmin;
/*     */   private double xmax;
/*     */ 
/*     */   public Order1(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt)
/*     */   {
/*  44 */     super(paramInt);
/*  45 */     this.x0 = paramDouble1;
/*  46 */     this.y0 = paramDouble2;
/*  47 */     this.x1 = paramDouble3;
/*  48 */     this.y1 = paramDouble4;
/*  49 */     if (paramDouble1 < paramDouble3) {
/*  50 */       this.xmin = paramDouble1;
/*  51 */       this.xmax = paramDouble3;
/*     */     } else {
/*  53 */       this.xmin = paramDouble3;
/*  54 */       this.xmax = paramDouble1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getOrder() {
/*  59 */     return 1;
/*     */   }
/*     */ 
/*     */   public double getXTop() {
/*  63 */     return this.x0;
/*     */   }
/*     */ 
/*     */   public double getYTop() {
/*  67 */     return this.y0;
/*     */   }
/*     */ 
/*     */   public double getXBot() {
/*  71 */     return this.x1;
/*     */   }
/*     */ 
/*     */   public double getYBot() {
/*  75 */     return this.y1;
/*     */   }
/*     */ 
/*     */   public double getXMin() {
/*  79 */     return this.xmin;
/*     */   }
/*     */ 
/*     */   public double getXMax() {
/*  83 */     return this.xmax;
/*     */   }
/*     */ 
/*     */   public double getX0() {
/*  87 */     return this.direction == 1 ? this.x0 : this.x1;
/*     */   }
/*     */ 
/*     */   public double getY0() {
/*  91 */     return this.direction == 1 ? this.y0 : this.y1;
/*     */   }
/*     */ 
/*     */   public double getX1() {
/*  95 */     return this.direction == -1 ? this.x0 : this.x1;
/*     */   }
/*     */ 
/*     */   public double getY1() {
/*  99 */     return this.direction == -1 ? this.y0 : this.y1;
/*     */   }
/*     */ 
/*     */   public double XforY(double paramDouble) {
/* 103 */     if ((this.x0 == this.x1) || (paramDouble <= this.y0)) {
/* 104 */       return this.x0;
/*     */     }
/* 106 */     if (paramDouble >= this.y1) {
/* 107 */       return this.x1;
/*     */     }
/*     */ 
/* 110 */     return this.x0 + (paramDouble - this.y0) * (this.x1 - this.x0) / (this.y1 - this.y0);
/*     */   }
/*     */ 
/*     */   public double TforY(double paramDouble) {
/* 114 */     if (paramDouble <= this.y0) {
/* 115 */       return 0.0D;
/*     */     }
/* 117 */     if (paramDouble >= this.y1) {
/* 118 */       return 1.0D;
/*     */     }
/* 120 */     return (paramDouble - this.y0) / (this.y1 - this.y0);
/*     */   }
/*     */ 
/*     */   public double XforT(double paramDouble) {
/* 124 */     return this.x0 + paramDouble * (this.x1 - this.x0);
/*     */   }
/*     */ 
/*     */   public double YforT(double paramDouble) {
/* 128 */     return this.y0 + paramDouble * (this.y1 - this.y0);
/*     */   }
/*     */ 
/*     */   public double dXforT(double paramDouble, int paramInt) {
/* 132 */     switch (paramInt) {
/*     */     case 0:
/* 134 */       return this.x0 + paramDouble * (this.x1 - this.x0);
/*     */     case 1:
/* 136 */       return this.x1 - this.x0;
/*     */     }
/* 138 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double dYforT(double paramDouble, int paramInt)
/*     */   {
/* 143 */     switch (paramInt) {
/*     */     case 0:
/* 145 */       return this.y0 + paramDouble * (this.y1 - this.y0);
/*     */     case 1:
/* 147 */       return this.y1 - this.y0;
/*     */     }
/* 149 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double nextVertical(double paramDouble1, double paramDouble2)
/*     */   {
/* 154 */     return paramDouble2;
/*     */   }
/*     */ 
/*     */   public boolean accumulateCrossings(Crossings paramCrossings) {
/* 158 */     double d1 = paramCrossings.getXLo();
/* 159 */     double d2 = paramCrossings.getYLo();
/* 160 */     double d3 = paramCrossings.getXHi();
/* 161 */     double d4 = paramCrossings.getYHi();
/* 162 */     if (this.xmin >= d3)
/* 163 */       return false;
/*     */     double d6;
/*     */     double d5;
/* 166 */     if (this.y0 < d2) {
/* 167 */       if (this.y1 <= d2) {
/* 168 */         return false;
/*     */       }
/* 170 */       d6 = d2;
/* 171 */       d5 = XforY(d2);
/*     */     } else {
/* 173 */       if (this.y0 >= d4) {
/* 174 */         return false;
/*     */       }
/* 176 */       d6 = this.y0;
/* 177 */       d5 = this.x0;
/*     */     }
/*     */     double d8;
/*     */     double d7;
/* 179 */     if (this.y1 > d4) {
/* 180 */       d8 = d4;
/* 181 */       d7 = XforY(d4);
/*     */     } else {
/* 183 */       d8 = this.y1;
/* 184 */       d7 = this.x1;
/*     */     }
/* 186 */     if ((d5 >= d3) && (d7 >= d3)) {
/* 187 */       return false;
/*     */     }
/* 189 */     if ((d5 > d1) || (d7 > d1)) {
/* 190 */       return true;
/*     */     }
/* 192 */     paramCrossings.record(d6, d8, this.direction);
/* 193 */     return false;
/*     */   }
/*     */ 
/*     */   public void enlarge(Rectangle2D paramRectangle2D) {
/* 197 */     paramRectangle2D.add(this.x0, this.y0);
/* 198 */     paramRectangle2D.add(this.x1, this.y1);
/*     */   }
/*     */ 
/*     */   public Curve getSubCurve(double paramDouble1, double paramDouble2, int paramInt) {
/* 202 */     if ((paramDouble1 == this.y0) && (paramDouble2 == this.y1)) {
/* 203 */       return getWithDirection(paramInt);
/*     */     }
/* 205 */     if (this.x0 == this.x1) {
/* 206 */       return new Order1(this.x0, paramDouble1, this.x1, paramDouble2, paramInt);
/*     */     }
/* 208 */     double d1 = this.x0 - this.x1;
/* 209 */     double d2 = this.y0 - this.y1;
/* 210 */     double d3 = this.x0 + (paramDouble1 - this.y0) * d1 / d2;
/* 211 */     double d4 = this.x0 + (paramDouble2 - this.y0) * d1 / d2;
/* 212 */     return new Order1(d3, paramDouble1, d4, paramDouble2, paramInt);
/*     */   }
/*     */ 
/*     */   public Curve getReversedCurve() {
/* 216 */     return new Order1(this.x0, this.y0, this.x1, this.y1, -this.direction);
/*     */   }
/*     */ 
/*     */   public int compareTo(Curve paramCurve, double[] paramArrayOfDouble) {
/* 220 */     if (!(paramCurve instanceof Order1)) {
/* 221 */       return super.compareTo(paramCurve, paramArrayOfDouble);
/*     */     }
/* 223 */     Order1 localOrder1 = (Order1)paramCurve;
/* 224 */     if (paramArrayOfDouble[1] <= paramArrayOfDouble[0]) {
/* 225 */       throw new InternalError("yrange already screwed up...");
/*     */     }
/* 227 */     paramArrayOfDouble[1] = Math.min(Math.min(paramArrayOfDouble[1], this.y1), localOrder1.y1);
/* 228 */     if (paramArrayOfDouble[1] <= paramArrayOfDouble[0]) {
/* 229 */       throw new InternalError("backstepping from " + paramArrayOfDouble[0] + " to " + paramArrayOfDouble[1]);
/*     */     }
/* 231 */     if (this.xmax <= localOrder1.xmin) {
/* 232 */       return this.xmin == localOrder1.xmax ? 0 : -1;
/*     */     }
/* 234 */     if (this.xmin >= localOrder1.xmax) {
/* 235 */       return 1;
/*     */     }
/*     */ 
/* 269 */     double d1 = this.x1 - this.x0;
/* 270 */     double d2 = this.y1 - this.y0;
/* 271 */     double d3 = localOrder1.x1 - localOrder1.x0;
/* 272 */     double d4 = localOrder1.y1 - localOrder1.y0;
/* 273 */     double d5 = d3 * d2 - d1 * d4;
/*     */     double d6;
/* 275 */     if (d5 != 0.0D) {
/* 276 */       double d7 = (this.x0 - localOrder1.x0) * d2 * d4 - this.y0 * d1 * d4 + localOrder1.y0 * d3 * d2;
/*     */ 
/* 279 */       d6 = d7 / d5;
/* 280 */       if (d6 <= paramArrayOfDouble[0])
/*     */       {
/* 283 */         d6 = Math.min(this.y1, localOrder1.y1);
/*     */       }
/*     */       else {
/* 286 */         if (d6 < paramArrayOfDouble[1])
/*     */         {
/* 288 */           paramArrayOfDouble[1] = d6;
/*     */         }
/*     */ 
/* 291 */         d6 = Math.max(this.y0, localOrder1.y0);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 297 */       d6 = Math.max(this.y0, localOrder1.y0);
/*     */     }
/* 299 */     return orderof(XforY(d6), localOrder1.XforY(d6));
/*     */   }
/*     */ 
/*     */   public int getSegment(double[] paramArrayOfDouble) {
/* 303 */     if (this.direction == 1) {
/* 304 */       paramArrayOfDouble[0] = this.x1;
/* 305 */       paramArrayOfDouble[1] = this.y1;
/*     */     } else {
/* 307 */       paramArrayOfDouble[0] = this.x0;
/* 308 */       paramArrayOfDouble[1] = this.y0;
/*     */     }
/* 310 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.Order1
 * JD-Core Version:    0.6.2
 */
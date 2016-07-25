/*     */ package sun.awt.geom;
/*     */ 
/*     */ import java.awt.geom.Rectangle2D;
/*     */ 
/*     */ final class Order0 extends Curve
/*     */ {
/*     */   private double x;
/*     */   private double y;
/*     */ 
/*     */   public Order0(double paramDouble1, double paramDouble2)
/*     */   {
/*  37 */     super(1);
/*  38 */     this.x = paramDouble1;
/*  39 */     this.y = paramDouble2;
/*     */   }
/*     */ 
/*     */   public int getOrder() {
/*  43 */     return 0;
/*     */   }
/*     */ 
/*     */   public double getXTop() {
/*  47 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double getYTop() {
/*  51 */     return this.y;
/*     */   }
/*     */ 
/*     */   public double getXBot() {
/*  55 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double getYBot() {
/*  59 */     return this.y;
/*     */   }
/*     */ 
/*     */   public double getXMin() {
/*  63 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double getXMax() {
/*  67 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double getX0() {
/*  71 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double getY0() {
/*  75 */     return this.y;
/*     */   }
/*     */ 
/*     */   public double getX1() {
/*  79 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double getY1() {
/*  83 */     return this.y;
/*     */   }
/*     */ 
/*     */   public double XforY(double paramDouble) {
/*  87 */     return paramDouble;
/*     */   }
/*     */ 
/*     */   public double TforY(double paramDouble) {
/*  91 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double XforT(double paramDouble) {
/*  95 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double YforT(double paramDouble) {
/*  99 */     return this.y;
/*     */   }
/*     */ 
/*     */   public double dXforT(double paramDouble, int paramInt) {
/* 103 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double dYforT(double paramDouble, int paramInt) {
/* 107 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double nextVertical(double paramDouble1, double paramDouble2) {
/* 111 */     return paramDouble2;
/*     */   }
/*     */ 
/*     */   public int crossingsFor(double paramDouble1, double paramDouble2) {
/* 115 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean accumulateCrossings(Crossings paramCrossings) {
/* 119 */     return (this.x > paramCrossings.getXLo()) && (this.x < paramCrossings.getXHi()) && (this.y > paramCrossings.getYLo()) && (this.y < paramCrossings.getYHi());
/*     */   }
/*     */ 
/*     */   public void enlarge(Rectangle2D paramRectangle2D)
/*     */   {
/* 126 */     paramRectangle2D.add(this.x, this.y);
/*     */   }
/*     */ 
/*     */   public Curve getSubCurve(double paramDouble1, double paramDouble2, int paramInt) {
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   public Curve getReversedCurve() {
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */   public int getSegment(double[] paramArrayOfDouble) {
/* 138 */     paramArrayOfDouble[0] = this.x;
/* 139 */     paramArrayOfDouble[1] = this.y;
/* 140 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.Order0
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.awt.geom;
/*     */ 
/*     */ final class Edge
/*     */ {
/*     */   static final int INIT_PARTS = 4;
/*     */   static final int GROW_PARTS = 10;
/*     */   Curve curve;
/*     */   int ctag;
/*     */   int etag;
/*     */   double activey;
/*     */   int equivalence;
/*     */   private Edge lastEdge;
/*     */   private int lastResult;
/*     */   private double lastLimit;
/*     */ 
/*     */   public Edge(Curve paramCurve, int paramInt)
/*     */   {
/*  39 */     this(paramCurve, paramInt, 0);
/*     */   }
/*     */ 
/*     */   public Edge(Curve paramCurve, int paramInt1, int paramInt2) {
/*  43 */     this.curve = paramCurve;
/*  44 */     this.ctag = paramInt1;
/*  45 */     this.etag = paramInt2;
/*     */   }
/*     */ 
/*     */   public Curve getCurve() {
/*  49 */     return this.curve;
/*     */   }
/*     */ 
/*     */   public int getCurveTag() {
/*  53 */     return this.ctag;
/*     */   }
/*     */ 
/*     */   public int getEdgeTag() {
/*  57 */     return this.etag;
/*     */   }
/*     */ 
/*     */   public void setEdgeTag(int paramInt) {
/*  61 */     this.etag = paramInt;
/*     */   }
/*     */ 
/*     */   public int getEquivalence() {
/*  65 */     return this.equivalence;
/*     */   }
/*     */ 
/*     */   public void setEquivalence(int paramInt) {
/*  69 */     this.equivalence = paramInt;
/*     */   }
/*     */ 
/*     */   public int compareTo(Edge paramEdge, double[] paramArrayOfDouble)
/*     */   {
/*  77 */     if ((paramEdge == this.lastEdge) && (paramArrayOfDouble[0] < this.lastLimit)) {
/*  78 */       if (paramArrayOfDouble[1] > this.lastLimit) {
/*  79 */         paramArrayOfDouble[1] = this.lastLimit;
/*     */       }
/*  81 */       return this.lastResult;
/*     */     }
/*  83 */     if ((this == paramEdge.lastEdge) && (paramArrayOfDouble[0] < paramEdge.lastLimit)) {
/*  84 */       if (paramArrayOfDouble[1] > paramEdge.lastLimit) {
/*  85 */         paramArrayOfDouble[1] = paramEdge.lastLimit;
/*     */       }
/*  87 */       return 0 - paramEdge.lastResult;
/*     */     }
/*     */ 
/*  90 */     int i = this.curve.compareTo(paramEdge.curve, paramArrayOfDouble);
/*     */ 
/* 101 */     this.lastEdge = paramEdge;
/* 102 */     this.lastLimit = paramArrayOfDouble[1];
/* 103 */     this.lastResult = i;
/* 104 */     return i;
/*     */   }
/*     */ 
/*     */   public void record(double paramDouble, int paramInt) {
/* 108 */     this.activey = paramDouble;
/* 109 */     this.etag = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean isActiveFor(double paramDouble, int paramInt) {
/* 113 */     return (this.etag == paramInt) && (this.activey >= paramDouble);
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 117 */     return "Edge[" + this.curve + ", " + (this.ctag == 0 ? "L" : "R") + ", " + (this.etag == -1 ? "O" : this.etag == 1 ? "I" : "N") + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.Edge
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.awt.geom;
/*     */ 
/*     */ final class CurveLink
/*     */ {
/*     */   Curve curve;
/*     */   double ytop;
/*     */   double ybot;
/*     */   int etag;
/*     */   CurveLink next;
/*     */ 
/*     */   public CurveLink(Curve paramCurve, double paramDouble1, double paramDouble2, int paramInt)
/*     */   {
/*  37 */     this.curve = paramCurve;
/*  38 */     this.ytop = paramDouble1;
/*  39 */     this.ybot = paramDouble2;
/*  40 */     this.etag = paramInt;
/*  41 */     if ((this.ytop < paramCurve.getYTop()) || (this.ybot > paramCurve.getYBot()))
/*  42 */       throw new InternalError("bad curvelink [" + this.ytop + "=>" + this.ybot + "] for " + paramCurve);
/*     */   }
/*     */ 
/*     */   public boolean absorb(CurveLink paramCurveLink)
/*     */   {
/*  47 */     return absorb(paramCurveLink.curve, paramCurveLink.ytop, paramCurveLink.ybot, paramCurveLink.etag);
/*     */   }
/*     */ 
/*     */   public boolean absorb(Curve paramCurve, double paramDouble1, double paramDouble2, int paramInt) {
/*  51 */     if ((this.curve != paramCurve) || (this.etag != paramInt) || (this.ybot < paramDouble1) || (this.ytop > paramDouble2))
/*     */     {
/*  54 */       return false;
/*     */     }
/*  56 */     if ((paramDouble1 < paramCurve.getYTop()) || (paramDouble2 > paramCurve.getYBot())) {
/*  57 */       throw new InternalError("bad curvelink [" + paramDouble1 + "=>" + paramDouble2 + "] for " + paramCurve);
/*     */     }
/*  59 */     this.ytop = Math.min(this.ytop, paramDouble1);
/*  60 */     this.ybot = Math.max(this.ybot, paramDouble2);
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/*  65 */     return this.ytop == this.ybot;
/*     */   }
/*     */ 
/*     */   public Curve getCurve() {
/*  69 */     return this.curve;
/*     */   }
/*     */ 
/*     */   public Curve getSubCurve() {
/*  73 */     if ((this.ytop == this.curve.getYTop()) && (this.ybot == this.curve.getYBot())) {
/*  74 */       return this.curve.getWithDirection(this.etag);
/*     */     }
/*  76 */     return this.curve.getSubCurve(this.ytop, this.ybot, this.etag);
/*     */   }
/*     */ 
/*     */   public Curve getMoveto() {
/*  80 */     return new Order0(getXTop(), getYTop());
/*     */   }
/*     */ 
/*     */   public double getXTop() {
/*  84 */     return this.curve.XforY(this.ytop);
/*     */   }
/*     */ 
/*     */   public double getYTop() {
/*  88 */     return this.ytop;
/*     */   }
/*     */ 
/*     */   public double getXBot() {
/*  92 */     return this.curve.XforY(this.ybot);
/*     */   }
/*     */ 
/*     */   public double getYBot() {
/*  96 */     return this.ybot;
/*     */   }
/*     */ 
/*     */   public double getX() {
/* 100 */     return this.curve.XforY(this.ytop);
/*     */   }
/*     */ 
/*     */   public int getEdgeTag() {
/* 104 */     return this.etag;
/*     */   }
/*     */ 
/*     */   public void setNext(CurveLink paramCurveLink) {
/* 108 */     this.next = paramCurveLink;
/*     */   }
/*     */ 
/*     */   public CurveLink getNext() {
/* 112 */     return this.next;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.CurveLink
 * JD-Core Version:    0.6.2
 */
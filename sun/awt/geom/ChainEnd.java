/*     */ package sun.awt.geom;
/*     */ 
/*     */ final class ChainEnd
/*     */ {
/*     */   CurveLink head;
/*     */   CurveLink tail;
/*     */   ChainEnd partner;
/*     */   int etag;
/*     */ 
/*     */   public ChainEnd(CurveLink paramCurveLink, ChainEnd paramChainEnd)
/*     */   {
/*  35 */     this.head = paramCurveLink;
/*  36 */     this.tail = paramCurveLink;
/*  37 */     this.partner = paramChainEnd;
/*  38 */     this.etag = paramCurveLink.getEdgeTag();
/*     */   }
/*     */ 
/*     */   public CurveLink getChain() {
/*  42 */     return this.head;
/*     */   }
/*     */ 
/*     */   public void setOtherEnd(ChainEnd paramChainEnd) {
/*  46 */     this.partner = paramChainEnd;
/*     */   }
/*     */ 
/*     */   public ChainEnd getPartner() {
/*  50 */     return this.partner;
/*     */   }
/*     */ 
/*     */   public CurveLink linkTo(ChainEnd paramChainEnd)
/*     */   {
/*  58 */     if ((this.etag == 0) || (paramChainEnd.etag == 0))
/*     */     {
/*  61 */       throw new InternalError("ChainEnd linked more than once!");
/*     */     }
/*  63 */     if (this.etag == paramChainEnd.etag)
/*  64 */       throw new InternalError("Linking chains of the same type!");
/*     */     ChainEnd localChainEnd1;
/*     */     ChainEnd localChainEnd2;
/*  68 */     if (this.etag == 1) {
/*  69 */       localChainEnd1 = this;
/*  70 */       localChainEnd2 = paramChainEnd;
/*     */     } else {
/*  72 */       localChainEnd1 = paramChainEnd;
/*  73 */       localChainEnd2 = this;
/*     */     }
/*     */ 
/*  76 */     this.etag = 0;
/*  77 */     paramChainEnd.etag = 0;
/*     */ 
/*  79 */     localChainEnd1.tail.setNext(localChainEnd2.head);
/*  80 */     localChainEnd1.tail = localChainEnd2.tail;
/*  81 */     if (this.partner == paramChainEnd)
/*     */     {
/*  83 */       return localChainEnd1.head;
/*     */     }
/*     */ 
/*  86 */     ChainEnd localChainEnd3 = localChainEnd2.partner;
/*  87 */     ChainEnd localChainEnd4 = localChainEnd1.partner;
/*  88 */     localChainEnd3.partner = localChainEnd4;
/*  89 */     localChainEnd4.partner = localChainEnd3;
/*  90 */     if (localChainEnd1.head.getYTop() < localChainEnd3.head.getYTop()) {
/*  91 */       localChainEnd1.tail.setNext(localChainEnd3.head);
/*  92 */       localChainEnd3.head = localChainEnd1.head;
/*     */     } else {
/*  94 */       localChainEnd4.tail.setNext(localChainEnd1.head);
/*  95 */       localChainEnd4.tail = localChainEnd1.tail;
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   public void addLink(CurveLink paramCurveLink) {
/* 101 */     if (this.etag == 1) {
/* 102 */       this.tail.setNext(paramCurveLink);
/* 103 */       this.tail = paramCurveLink;
/*     */     } else {
/* 105 */       paramCurveLink.setNext(this.head);
/* 106 */       this.head = paramCurveLink;
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getX() {
/* 111 */     if (this.etag == 1) {
/* 112 */       return this.tail.getXBot();
/*     */     }
/* 114 */     return this.head.getXBot();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.geom.ChainEnd
 * JD-Core Version:    0.6.2
 */
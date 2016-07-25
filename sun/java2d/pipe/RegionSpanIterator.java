/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ public class RegionSpanIterator
/*     */   implements SpanIterator
/*     */ {
/*     */   RegionIterator ri;
/*     */   int lox;
/*     */   int loy;
/*     */   int hix;
/*     */   int hiy;
/*     */   int curloy;
/*     */   int curhiy;
/*  45 */   boolean done = false;
/*     */   boolean isrect;
/*     */ 
/*     */   public RegionSpanIterator(Region paramRegion)
/*     */   {
/*  65 */     int[] arrayOfInt = new int[4];
/*     */ 
/*  67 */     paramRegion.getBounds(arrayOfInt);
/*  68 */     this.lox = arrayOfInt[0];
/*  69 */     this.loy = arrayOfInt[1];
/*  70 */     this.hix = arrayOfInt[2];
/*  71 */     this.hiy = arrayOfInt[3];
/*  72 */     this.isrect = paramRegion.isRectangular();
/*     */ 
/*  74 */     this.ri = paramRegion.getIterator();
/*     */   }
/*     */ 
/*     */   public void getPathBox(int[] paramArrayOfInt)
/*     */   {
/*  81 */     paramArrayOfInt[0] = this.lox;
/*  82 */     paramArrayOfInt[1] = this.loy;
/*  83 */     paramArrayOfInt[2] = this.hix;
/*  84 */     paramArrayOfInt[3] = this.hiy;
/*     */   }
/*     */ 
/*     */   public void intersectClipBox(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  92 */     if (paramInt1 > this.lox) {
/*  93 */       this.lox = paramInt1;
/*     */     }
/*  95 */     if (paramInt2 > this.loy) {
/*  96 */       this.loy = paramInt2;
/*     */     }
/*  98 */     if (paramInt3 < this.hix) {
/*  99 */       this.hix = paramInt3;
/*     */     }
/* 101 */     if (paramInt4 < this.hiy) {
/* 102 */       this.hiy = paramInt4;
/*     */     }
/* 104 */     this.done = ((this.lox >= this.hix) || (this.loy >= this.hiy));
/*     */   }
/*     */ 
/*     */   public boolean nextSpan(int[] paramArrayOfInt)
/*     */   {
/* 114 */     if (this.done) {
/* 115 */       return false;
/*     */     }
/*     */ 
/* 122 */     if (this.isrect) {
/* 123 */       getPathBox(paramArrayOfInt);
/* 124 */       this.done = true;
/* 125 */       return true;
/* 130 */     }
/*     */ int k = this.curloy;
/* 131 */     int m = this.curhiy;
/*     */     int i;
/*     */     int j;
/*     */     while (true) if (!this.ri.nextXBand(paramArrayOfInt)) {
/* 135 */         if (!this.ri.nextYRange(paramArrayOfInt)) {
/* 136 */           this.done = true;
/* 137 */           return false;
/*     */         }
/*     */ 
/* 140 */         k = paramArrayOfInt[1];
/* 141 */         m = paramArrayOfInt[3];
/* 142 */         if (k < this.loy) {
/* 143 */           k = this.loy;
/*     */         }
/* 145 */         if (m > this.hiy) {
/* 146 */           m = this.hiy;
/*     */         }
/*     */ 
/* 149 */         if (k >= this.hiy) {
/* 150 */           this.done = true;
/* 151 */           return false;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 156 */         i = paramArrayOfInt[0];
/* 157 */         j = paramArrayOfInt[2];
/* 158 */         if (i < this.lox) {
/* 159 */           i = this.lox;
/*     */         }
/* 161 */         if (j > this.hix) {
/* 162 */           j = this.hix;
/*     */         }
/*     */ 
/* 165 */         if ((i < j) && (k < m)) {
/* 166 */           break;
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/* 171 */     paramArrayOfInt[0] = i;
/*     */     int tmp183_181 = k; this.curloy = tmp183_181; paramArrayOfInt[1] = tmp183_181;
/* 173 */     paramArrayOfInt[2] = j;
/*     */     int tmp197_195 = m; this.curhiy = tmp197_195; paramArrayOfInt[3] = tmp197_195;
/* 175 */     return true;
/*     */   }
/*     */ 
/*     */   public void skipDownTo(int paramInt)
/*     */   {
/* 183 */     this.loy = paramInt;
/*     */   }
/*     */ 
/*     */   public long getNativeIterator()
/*     */   {
/* 198 */     return 0L;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.RegionSpanIterator
 * JD-Core Version:    0.6.2
 */
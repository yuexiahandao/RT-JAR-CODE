/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ public class RegionClipSpanIterator
/*     */   implements SpanIterator
/*     */ {
/*     */   Region rgn;
/*     */   SpanIterator spanIter;
/*     */   RegionIterator resetState;
/*     */   RegionIterator lwm;
/*     */   RegionIterator row;
/*     */   RegionIterator box;
/*     */   int spanlox;
/*     */   int spanhix;
/*     */   int spanloy;
/*     */   int spanhiy;
/*     */   int lwmloy;
/*     */   int lwmhiy;
/*     */   int rgnlox;
/*     */   int rgnloy;
/*     */   int rgnhix;
/*     */   int rgnhiy;
/*     */   int rgnbndslox;
/*     */   int rgnbndsloy;
/*     */   int rgnbndshix;
/*     */   int rgnbndshiy;
/*  84 */   int[] rgnbox = new int[4];
/*     */ 
/*  87 */   int[] spanbox = new int[4];
/*     */   boolean doNextSpan;
/*     */   boolean doNextBox;
/*  98 */   boolean done = false;
/*     */ 
/*     */   public RegionClipSpanIterator(Region paramRegion, SpanIterator paramSpanIterator)
/*     */   {
/* 106 */     this.spanIter = paramSpanIterator;
/*     */ 
/* 108 */     this.resetState = paramRegion.getIterator();
/* 109 */     this.lwm = this.resetState.createCopy();
/*     */ 
/* 111 */     if (!this.lwm.nextYRange(this.rgnbox)) {
/* 112 */       this.done = true;
/* 113 */       return;
/*     */     }
/*     */ 
/* 116 */     this.rgnloy = (this.lwmloy = this.rgnbox[1]);
/* 117 */     this.rgnhiy = (this.lwmhiy = this.rgnbox[3]);
/*     */ 
/* 119 */     paramRegion.getBounds(this.rgnbox);
/* 120 */     this.rgnbndslox = this.rgnbox[0];
/* 121 */     this.rgnbndsloy = this.rgnbox[1];
/* 122 */     this.rgnbndshix = this.rgnbox[2];
/* 123 */     this.rgnbndshiy = this.rgnbox[3];
/* 124 */     if ((this.rgnbndslox >= this.rgnbndshix) || (this.rgnbndsloy >= this.rgnbndshiy))
/*     */     {
/* 126 */       this.done = true;
/* 127 */       return;
/*     */     }
/*     */ 
/* 130 */     this.rgn = paramRegion;
/*     */ 
/* 133 */     this.row = this.lwm.createCopy();
/* 134 */     this.box = this.row.createCopy();
/* 135 */     this.doNextSpan = true;
/* 136 */     this.doNextBox = false;
/*     */   }
/*     */ 
/*     */   public void getPathBox(int[] paramArrayOfInt)
/*     */   {
/* 144 */     int[] arrayOfInt = new int[4];
/* 145 */     this.rgn.getBounds(arrayOfInt);
/* 146 */     this.spanIter.getPathBox(paramArrayOfInt);
/*     */ 
/* 148 */     if (paramArrayOfInt[0] < arrayOfInt[0]) {
/* 149 */       paramArrayOfInt[0] = arrayOfInt[0];
/*     */     }
/*     */ 
/* 152 */     if (paramArrayOfInt[1] < arrayOfInt[1]) {
/* 153 */       paramArrayOfInt[1] = arrayOfInt[1];
/*     */     }
/*     */ 
/* 156 */     if (paramArrayOfInt[2] > arrayOfInt[2]) {
/* 157 */       paramArrayOfInt[2] = arrayOfInt[2];
/*     */     }
/*     */ 
/* 160 */     if (paramArrayOfInt[3] > arrayOfInt[3])
/* 161 */       paramArrayOfInt[3] = arrayOfInt[3];
/*     */   }
/*     */ 
/*     */   public void intersectClipBox(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 171 */     this.spanIter.intersectClipBox(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public boolean nextSpan(int[] paramArrayOfInt)
/*     */   {
/* 180 */     if (this.done) {
/* 181 */       return false; } 
/*     */ int n = 0;
/*     */     int i;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/*     */     do { while (true) if (this.doNextSpan) {
/* 192 */           if (!this.spanIter.nextSpan(this.spanbox)) {
/* 193 */             this.done = true;
/* 194 */             return false;
/*     */           }
/* 196 */           this.spanlox = this.spanbox[0];
/*     */ 
/* 198 */           if (this.spanlox < this.rgnbndshix)
/*     */           {
/* 202 */             this.spanloy = this.spanbox[1];
/* 203 */             if (this.spanloy < this.rgnbndshiy)
/*     */             {
/* 207 */               this.spanhix = this.spanbox[2];
/* 208 */               if (this.spanhix > this.rgnbndslox)
/*     */               {
/* 212 */                 this.spanhiy = this.spanbox[3];
/* 213 */                 if (this.spanhiy > this.rgnbndsloy)
/*     */                 {
/* 220 */                   if (this.lwmloy > this.spanloy) {
/* 221 */                     this.lwm.copyStateFrom(this.resetState);
/* 222 */                     this.lwm.nextYRange(this.rgnbox);
/* 223 */                     this.lwmloy = this.rgnbox[1];
/* 224 */                     this.lwmhiy = this.rgnbox[3];
/*     */                   }
/*     */ 
/* 231 */                   while ((this.lwmhiy <= this.spanloy) && 
/* 232 */                     (this.lwm.nextYRange(this.rgnbox)))
/*     */                   {
/* 234 */                     this.lwmloy = this.rgnbox[1];
/* 235 */                     this.lwmhiy = this.rgnbox[3];
/*     */                   }
/*     */ 
/* 239 */                   if ((this.lwmhiy > this.spanloy) && (this.lwmloy < this.spanhiy))
/*     */                   {
/* 242 */                     if (this.rgnloy != this.lwmloy) {
/* 243 */                       this.row.copyStateFrom(this.lwm);
/* 244 */                       this.rgnloy = this.lwmloy;
/* 245 */                       this.rgnhiy = this.lwmhiy;
/*     */                     }
/* 247 */                     this.box.copyStateFrom(this.row);
/* 248 */                     this.doNextBox = true;
/* 249 */                     this.doNextSpan = false;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           boolean bool;
/* 255 */           if (n != 0)
/*     */           {
/* 257 */             n = 0;
/*     */ 
/* 259 */             bool = this.row.nextYRange(this.rgnbox);
/*     */ 
/* 261 */             if (bool) {
/* 262 */               this.rgnloy = this.rgnbox[1];
/* 263 */               this.rgnhiy = this.rgnbox[3];
/*     */             }
/* 265 */             if ((!bool) || (this.rgnloy >= this.spanhiy))
/*     */             {
/* 268 */               this.doNextSpan = true;
/*     */             }
/*     */             else
/*     */             {
/* 272 */               this.box.copyStateFrom(this.row);
/* 273 */               this.doNextBox = true;
/*     */             }
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 279 */             if (!this.doNextBox) break;
/* 280 */             bool = this.box.nextXBand(this.rgnbox);
/* 281 */             if (bool) {
/* 282 */               this.rgnlox = this.rgnbox[0];
/* 283 */               this.rgnhix = this.rgnbox[2];
/*     */             }
/* 285 */             if ((!bool) || (this.rgnlox >= this.spanhix))
/*     */             {
/* 288 */               this.doNextBox = false;
/* 289 */               if (this.rgnhiy >= this.spanhiy)
/*     */               {
/* 292 */                 this.doNextSpan = true;
/*     */               }
/*     */               else {
/* 295 */                 n = 1;
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 300 */               this.doNextBox = (this.rgnhix <= this.spanlox);
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */ 
/* 307 */       this.doNextBox = true;
/*     */ 
/* 310 */       if (this.spanlox > this.rgnlox) {
/* 311 */         i = this.spanlox;
/*     */       }
/*     */       else {
/* 314 */         i = this.rgnlox;
/*     */       }
/*     */ 
/* 317 */       if (this.spanloy > this.rgnloy) {
/* 318 */         j = this.spanloy;
/*     */       }
/*     */       else {
/* 321 */         j = this.rgnloy;
/*     */       }
/*     */ 
/* 324 */       if (this.spanhix < this.rgnhix) {
/* 325 */         k = this.spanhix;
/*     */       }
/*     */       else {
/* 328 */         k = this.rgnhix;
/*     */       }
/*     */ 
/* 331 */       if (this.spanhiy < this.rgnhiy) {
/* 332 */         m = this.spanhiy;
/*     */       }
/*     */       else {
/* 335 */         m = this.rgnhiy;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 342 */     while ((i >= k) || (j >= m));
/*     */ 
/* 351 */     paramArrayOfInt[0] = i;
/* 352 */     paramArrayOfInt[1] = j;
/* 353 */     paramArrayOfInt[2] = k;
/* 354 */     paramArrayOfInt[3] = m;
/* 355 */     return true;
/*     */   }
/*     */ 
/*     */   public void skipDownTo(int paramInt)
/*     */   {
/* 365 */     this.spanIter.skipDownTo(paramInt);
/*     */   }
/*     */ 
/*     */   public long getNativeIterator()
/*     */   {
/* 380 */     return 0L;
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.RegionClipSpanIterator
 * JD-Core Version:    0.6.2
 */
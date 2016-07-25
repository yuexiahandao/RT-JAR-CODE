/*     */ package java.awt.font;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ class TextJustifier
/*     */ {
/*     */   private GlyphJustificationInfo[] info;
/*     */   private int start;
/*     */   private int limit;
/*  55 */   static boolean DEBUG = false;
/*     */   public static final int MAX_PRIORITY = 3;
/*     */ 
/*     */   TextJustifier(GlyphJustificationInfo[] paramArrayOfGlyphJustificationInfo, int paramInt1, int paramInt2)
/*     */   {
/*  62 */     this.info = paramArrayOfGlyphJustificationInfo;
/*  63 */     this.start = paramInt1;
/*  64 */     this.limit = paramInt2;
/*     */ 
/*  66 */     if (DEBUG) {
/*  67 */       System.out.println("start: " + paramInt1 + ", limit: " + paramInt2);
/*  68 */       for (int i = paramInt1; i < paramInt2; i++) {
/*  69 */         GlyphJustificationInfo localGlyphJustificationInfo = paramArrayOfGlyphJustificationInfo[i];
/*  70 */         System.out.println("w: " + localGlyphJustificationInfo.weight + ", gp: " + localGlyphJustificationInfo.growPriority + ", gll: " + localGlyphJustificationInfo.growLeftLimit + ", grl: " + localGlyphJustificationInfo.growRightLimit);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public float[] justify(float paramFloat)
/*     */   {
/*  88 */     float[] arrayOfFloat = new float[this.info.length * 2];
/*     */ 
/*  90 */     int i = paramFloat > 0.0F ? 1 : 0;
/*     */ 
/*  92 */     if (DEBUG) {
/*  93 */       System.out.println("delta: " + paramFloat);
/*     */     }
/*     */ 
/*  97 */     int j = -1;
/*     */     int m;
/*  98 */     for (int k = 0; paramFloat != 0.0F; k++)
/*     */     {
/* 103 */       m = k > 3 ? 1 : 0;
/* 104 */       if (m != 0) {
/* 105 */         k = j;
/*     */       }
/*     */ 
/* 108 */       float f2 = 0.0F;
/* 109 */       float f3 = 0.0F;
/* 110 */       float f4 = 0.0F;
/* 111 */       for (int n = this.start; n < this.limit; n++) {
/* 112 */         GlyphJustificationInfo localGlyphJustificationInfo1 = this.info[n];
/* 113 */         if ((i != 0 ? localGlyphJustificationInfo1.growPriority : localGlyphJustificationInfo1.shrinkPriority) == k) {
/* 114 */           if (j == -1) {
/* 115 */             j = k;
/*     */           }
/*     */ 
/* 118 */           if (n != this.start) {
/* 119 */             f2 += localGlyphJustificationInfo1.weight;
/* 120 */             if (i != 0) {
/* 121 */               f3 += localGlyphJustificationInfo1.growLeftLimit;
/* 122 */               if (localGlyphJustificationInfo1.growAbsorb)
/* 123 */                 f4 += localGlyphJustificationInfo1.weight;
/*     */             }
/*     */             else {
/* 126 */               f3 += localGlyphJustificationInfo1.shrinkLeftLimit;
/* 127 */               if (localGlyphJustificationInfo1.shrinkAbsorb) {
/* 128 */                 f4 += localGlyphJustificationInfo1.weight;
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 133 */           if (n + 1 != this.limit) {
/* 134 */             f2 += localGlyphJustificationInfo1.weight;
/* 135 */             if (i != 0) {
/* 136 */               f3 += localGlyphJustificationInfo1.growRightLimit;
/* 137 */               if (localGlyphJustificationInfo1.growAbsorb)
/* 138 */                 f4 += localGlyphJustificationInfo1.weight;
/*     */             }
/*     */             else {
/* 141 */               f3 += localGlyphJustificationInfo1.shrinkRightLimit;
/* 142 */               if (localGlyphJustificationInfo1.shrinkAbsorb) {
/* 143 */                 f4 += localGlyphJustificationInfo1.weight;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 151 */       if (i == 0) {
/* 152 */         f3 = -f3;
/*     */       }
/* 154 */       if (f2 != 0.0F) if (m != 0) break label375; label375: n = (paramFloat < 0.0F ? 1 : 0) == (paramFloat < f3 ? 1 : 0) ? 1 : 0;
/* 155 */       int i1 = (n != 0) && (f4 > 0.0F) ? 1 : 0;
/*     */ 
/* 158 */       float f5 = paramFloat / f2;
/*     */ 
/* 160 */       float f6 = 0.0F;
/* 161 */       if ((n != 0) && (f4 > 0.0F)) {
/* 162 */         f6 = (paramFloat - f3) / f4;
/*     */       }
/*     */ 
/* 165 */       if (DEBUG) {
/* 166 */         System.out.println("pass: " + k + ", d: " + paramFloat + ", l: " + f3 + ", w: " + f2 + ", aw: " + f4 + ", wd: " + f5 + ", wa: " + f6 + ", hit: " + (n != 0 ? "y" : "n"));
/*     */       }
/*     */ 
/* 177 */       int i2 = this.start * 2;
/* 178 */       for (int i3 = this.start; i3 < this.limit; i3++) {
/* 179 */         GlyphJustificationInfo localGlyphJustificationInfo2 = this.info[i3];
/* 180 */         if ((i != 0 ? localGlyphJustificationInfo2.growPriority : localGlyphJustificationInfo2.shrinkPriority) == k)
/*     */         {
/*     */           float f7;
/* 181 */           if (i3 != this.start)
/*     */           {
/* 183 */             if (n != 0)
/*     */             {
/* 185 */               f7 = i != 0 ? localGlyphJustificationInfo2.growLeftLimit : -localGlyphJustificationInfo2.shrinkLeftLimit;
/* 186 */               if (i1 != 0)
/*     */               {
/* 188 */                 f7 += localGlyphJustificationInfo2.weight * f6;
/*     */               }
/*     */             }
/*     */             else {
/* 192 */               f7 = localGlyphJustificationInfo2.weight * f5;
/*     */             }
/*     */ 
/* 195 */             arrayOfFloat[i2] += f7;
/*     */           }
/* 197 */           i2++;
/*     */ 
/* 199 */           if (i3 + 1 != this.limit)
/*     */           {
/* 201 */             if (n != 0) {
/* 202 */               f7 = i != 0 ? localGlyphJustificationInfo2.growRightLimit : -localGlyphJustificationInfo2.shrinkRightLimit;
/* 203 */               if (i1 != 0)
/* 204 */                 f7 += localGlyphJustificationInfo2.weight * f6;
/*     */             }
/*     */             else {
/* 207 */               f7 = localGlyphJustificationInfo2.weight * f5;
/*     */             }
/*     */ 
/* 210 */             arrayOfFloat[i2] += f7;
/*     */           }
/* 212 */           i2++;
/*     */         } else {
/* 214 */           i2 += 2;
/*     */         }
/*     */       }
/*     */ 
/* 218 */       if ((m == 0) && (n != 0) && (i1 == 0))
/* 219 */         paramFloat -= f3;
/*     */       else {
/* 221 */         paramFloat = 0.0F;
/*     */       }
/*     */     }
/*     */ 
/* 225 */     if (DEBUG) {
/* 226 */       float f1 = 0.0F;
/* 227 */       for (m = 0; m < arrayOfFloat.length; m++) {
/* 228 */         f1 += arrayOfFloat[m];
/* 229 */         System.out.print(arrayOfFloat[m] + ", ");
/* 230 */         if (m % 20 == 9) {
/* 231 */           System.out.println();
/*     */         }
/*     */       }
/* 234 */       System.out.println("\ntotal: " + f1);
/* 235 */       System.out.println();
/*     */     }
/*     */ 
/* 238 */     return arrayOfFloat;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.TextJustifier
 * JD-Core Version:    0.6.2
 */
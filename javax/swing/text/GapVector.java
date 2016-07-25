/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ abstract class GapVector
/*     */   implements Serializable
/*     */ {
/*     */   private Object array;
/*     */   private int g0;
/*     */   private int g1;
/*     */ 
/*     */   public GapVector()
/*     */   {
/*  51 */     this(10);
/*     */   }
/*     */ 
/*     */   public GapVector(int paramInt)
/*     */   {
/*  61 */     this.array = allocateArray(paramInt);
/*  62 */     this.g0 = 0;
/*  63 */     this.g1 = paramInt;
/*     */   }
/*     */ 
/*     */   protected abstract Object allocateArray(int paramInt);
/*     */ 
/*     */   protected abstract int getArrayLength();
/*     */ 
/*     */   protected final Object getArray()
/*     */   {
/*  82 */     return this.array;
/*     */   }
/*     */ 
/*     */   protected final int getGapStart()
/*     */   {
/*  89 */     return this.g0;
/*     */   }
/*     */ 
/*     */   protected final int getGapEnd()
/*     */   {
/*  96 */     return this.g1;
/*     */   }
/*     */ 
/*     */   protected void replace(int paramInt1, int paramInt2, Object paramObject, int paramInt3)
/*     */   {
/* 132 */     int i = 0;
/* 133 */     if (paramInt3 == 0) {
/* 134 */       close(paramInt1, paramInt2);
/* 135 */       return;
/* 136 */     }if (paramInt2 > paramInt3)
/*     */     {
/* 138 */       close(paramInt1 + paramInt3, paramInt2 - paramInt3);
/*     */     }
/*     */     else {
/* 141 */       int j = paramInt3 - paramInt2;
/* 142 */       int k = open(paramInt1 + paramInt2, j);
/* 143 */       System.arraycopy(paramObject, paramInt2, this.array, k, j);
/* 144 */       paramInt3 = paramInt2;
/*     */     }
/* 146 */     System.arraycopy(paramObject, i, this.array, paramInt1, paramInt3);
/*     */   }
/*     */ 
/*     */   void close(int paramInt1, int paramInt2)
/*     */   {
/* 157 */     if (paramInt2 == 0) return;
/*     */ 
/* 159 */     int i = paramInt1 + paramInt2;
/* 160 */     int j = this.g1 - this.g0 + paramInt2;
/* 161 */     if (i <= this.g0)
/*     */     {
/* 163 */       if (this.g0 != i) {
/* 164 */         shiftGap(i);
/*     */       }
/*     */ 
/* 167 */       shiftGapStartDown(this.g0 - paramInt2);
/* 168 */     } else if (paramInt1 >= this.g0)
/*     */     {
/* 170 */       if (this.g0 != paramInt1) {
/* 171 */         shiftGap(paramInt1);
/*     */       }
/*     */ 
/* 174 */       shiftGapEndUp(this.g0 + j);
/*     */     }
/*     */     else
/*     */     {
/* 178 */       shiftGapStartDown(paramInt1);
/* 179 */       shiftGapEndUp(this.g0 + j);
/*     */     }
/*     */   }
/*     */ 
/*     */   int open(int paramInt1, int paramInt2)
/*     */   {
/* 190 */     int i = this.g1 - this.g0;
/* 191 */     if (paramInt2 == 0) {
/* 192 */       if (paramInt1 > this.g0)
/* 193 */         paramInt1 += i;
/* 194 */       return paramInt1;
/*     */     }
/*     */ 
/* 198 */     shiftGap(paramInt1);
/* 199 */     if (paramInt2 >= i)
/*     */     {
/* 201 */       shiftEnd(getArrayLength() - i + paramInt2);
/* 202 */       i = this.g1 - this.g0;
/*     */     }
/*     */ 
/* 205 */     this.g0 += paramInt2;
/* 206 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   void resize(int paramInt)
/*     */   {
/* 214 */     Object localObject = allocateArray(paramInt);
/* 215 */     System.arraycopy(this.array, 0, localObject, 0, Math.min(paramInt, getArrayLength()));
/* 216 */     this.array = localObject;
/*     */   }
/*     */ 
/*     */   protected void shiftEnd(int paramInt)
/*     */   {
/* 224 */     int i = getArrayLength();
/* 225 */     int j = this.g1;
/* 226 */     int k = i - j;
/* 227 */     int m = getNewArraySize(paramInt);
/* 228 */     int n = m - k;
/* 229 */     resize(m);
/* 230 */     this.g1 = n;
/*     */ 
/* 232 */     if (k != 0)
/*     */     {
/* 234 */       System.arraycopy(this.array, j, this.array, n, k);
/*     */     }
/*     */   }
/*     */ 
/*     */   int getNewArraySize(int paramInt)
/*     */   {
/* 245 */     return (paramInt + 1) * 2;
/*     */   }
/*     */ 
/*     */   protected void shiftGap(int paramInt)
/*     */   {
/* 255 */     if (paramInt == this.g0) {
/* 256 */       return;
/*     */     }
/* 258 */     int i = this.g0;
/* 259 */     int j = paramInt - i;
/* 260 */     int k = this.g1;
/* 261 */     int m = k + j;
/* 262 */     int n = k - i;
/*     */ 
/* 264 */     this.g0 = paramInt;
/* 265 */     this.g1 = m;
/* 266 */     if (j > 0)
/*     */     {
/* 268 */       System.arraycopy(this.array, k, this.array, i, j);
/* 269 */     } else if (j < 0)
/*     */     {
/* 271 */       System.arraycopy(this.array, paramInt, this.array, m, -j);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void shiftGapStartDown(int paramInt)
/*     */   {
/* 284 */     this.g0 = paramInt;
/*     */   }
/*     */ 
/*     */   protected void shiftGapEndUp(int paramInt)
/*     */   {
/* 296 */     this.g1 = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.GapVector
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.text;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public final class IntHashtable
/*     */ {
/* 144 */   private int defaultValue = 0;
/*     */   private int primeIndex;
/*     */   private static final float HIGH_WATER_FACTOR = 0.4F;
/*     */   private int highWaterMark;
/*     */   private static final float LOW_WATER_FACTOR = 0.0F;
/*     */   private int lowWaterMark;
/*     */   private int count;
/*     */   private int[] values;
/*     */   private int[] keyList;
/*     */   private static final int EMPTY = -2147483648;
/*     */   private static final int DELETED = -2147483647;
/*     */   private static final int MAX_UNUSED = -2147483647;
/* 265 */   private static final int[] PRIMES = { 17, 37, 67, 131, 257, 521, 1031, 2053, 4099, 8209, 16411, 32771, 65537, 131101, 262147, 524309, 1048583, 2097169, 4194319, 8388617, 16777259, 33554467, 67108879, 134217757, 268435459, 536870923, 1073741827, 2147483647 };
/*     */ 
/*     */   public IntHashtable()
/*     */   {
/*  42 */     initialize(3);
/*     */   }
/*     */ 
/*     */   public IntHashtable(int paramInt) {
/*  46 */     initialize(leastGreaterPrimeIndex((int)(paramInt / 0.4F)));
/*     */   }
/*     */ 
/*     */   public int size() {
/*  50 */     return this.count;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/*  54 */     return this.count == 0;
/*     */   }
/*     */ 
/*     */   public void put(int paramInt1, int paramInt2) {
/*  58 */     if (this.count > this.highWaterMark) {
/*  59 */       rehash();
/*     */     }
/*  61 */     int i = find(paramInt1);
/*  62 */     if (this.keyList[i] <= -2147483647) {
/*  63 */       this.keyList[i] = paramInt1;
/*  64 */       this.count += 1;
/*     */     }
/*  66 */     this.values[i] = paramInt2;
/*     */   }
/*     */ 
/*     */   public int get(int paramInt) {
/*  70 */     return this.values[find(paramInt)];
/*     */   }
/*     */ 
/*     */   public void remove(int paramInt) {
/*  74 */     int i = find(paramInt);
/*  75 */     if (this.keyList[i] > -2147483647) {
/*  76 */       this.keyList[i] = -2147483647;
/*  77 */       this.values[i] = this.defaultValue;
/*  78 */       this.count -= 1;
/*  79 */       if (this.count < this.lowWaterMark)
/*  80 */         rehash();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getDefaultValue()
/*     */   {
/*  86 */     return this.defaultValue;
/*     */   }
/*     */ 
/*     */   public void setDefaultValue(int paramInt) {
/*  90 */     this.defaultValue = paramInt;
/*  91 */     rehash();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  95 */     if (paramObject.getClass() != getClass()) return false;
/*     */ 
/*  97 */     IntHashtable localIntHashtable = (IntHashtable)paramObject;
/*  98 */     if ((localIntHashtable.size() != this.count) || (localIntHashtable.defaultValue != this.defaultValue)) {
/*  99 */       return false;
/*     */     }
/* 101 */     for (int i = 0; i < this.keyList.length; i++) {
/* 102 */       int j = this.keyList[i];
/* 103 */       if ((j > -2147483647) && (localIntHashtable.get(j) != this.values[i]))
/* 104 */         return false;
/*     */     }
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 119 */     int i = 465;
/* 120 */     int j = 1362796821;
/* 121 */     for (int k = 0; k < this.keyList.length; k++)
/*     */     {
/* 125 */       i = i * j + 1;
/* 126 */       i += this.keyList[k];
/*     */     }
/* 128 */     for (k = 0; k < this.values.length; k++) {
/* 129 */       i = i * j + 1;
/* 130 */       i += this.values[k];
/*     */     }
/* 132 */     return i;
/*     */   }
/*     */ 
/*     */   public Object clone() throws CloneNotSupportedException
/*     */   {
/* 137 */     IntHashtable localIntHashtable = (IntHashtable)super.clone();
/* 138 */     this.values = ((int[])this.values.clone());
/* 139 */     this.keyList = ((int[])this.keyList.clone());
/* 140 */     return localIntHashtable;
/*     */   }
/*     */ 
/*     */   private void initialize(int paramInt)
/*     */   {
/* 171 */     if (paramInt < 0) {
/* 172 */       paramInt = 0;
/* 173 */     } else if (paramInt >= PRIMES.length) {
/* 174 */       System.out.println("TOO BIG");
/* 175 */       paramInt = PRIMES.length - 1;
/*     */     }
/*     */ 
/* 178 */     this.primeIndex = paramInt;
/* 179 */     int i = PRIMES[paramInt];
/* 180 */     this.values = new int[i];
/* 181 */     this.keyList = new int[i];
/* 182 */     for (int j = 0; j < i; j++) {
/* 183 */       this.keyList[j] = -2147483648;
/* 184 */       this.values[j] = this.defaultValue;
/*     */     }
/* 186 */     this.count = 0;
/* 187 */     this.lowWaterMark = ((int)(i * 0.0F));
/* 188 */     this.highWaterMark = ((int)(i * 0.4F));
/*     */   }
/*     */ 
/*     */   private void rehash() {
/* 192 */     int[] arrayOfInt1 = this.values;
/* 193 */     int[] arrayOfInt2 = this.keyList;
/* 194 */     int i = this.primeIndex;
/* 195 */     if (this.count > this.highWaterMark)
/* 196 */       i++;
/* 197 */     else if (this.count < this.lowWaterMark) {
/* 198 */       i -= 2;
/*     */     }
/* 200 */     initialize(i);
/* 201 */     for (int j = arrayOfInt1.length - 1; j >= 0; j--) {
/* 202 */       int k = arrayOfInt2[j];
/* 203 */       if (k > -2147483647)
/* 204 */         putInternal(k, arrayOfInt1[j]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putInternal(int paramInt1, int paramInt2)
/*     */   {
/* 210 */     int i = find(paramInt1);
/* 211 */     if (this.keyList[i] < -2147483647) {
/* 212 */       this.keyList[i] = paramInt1;
/* 213 */       this.count += 1;
/*     */     }
/* 215 */     this.values[i] = paramInt2;
/*     */   }
/*     */ 
/*     */   private int find(int paramInt) {
/* 219 */     if (paramInt <= -2147483647)
/* 220 */       throw new IllegalArgumentException("key can't be less than 0xFFFFFFFE");
/* 221 */     int i = -1;
/* 222 */     int j = (paramInt ^ 0x4000000) % this.keyList.length;
/* 223 */     if (j < 0) j = -j;
/* 224 */     int k = 0;
/*     */     while (true) {
/* 226 */       int m = this.keyList[j];
/* 227 */       if (m == paramInt)
/* 228 */         return j;
/* 229 */       if (m <= -2147483647)
/*     */       {
/* 231 */         if (m == -2147483648) {
/* 232 */           if (i >= 0) {
/* 233 */             j = i;
/*     */           }
/* 235 */           return j;
/* 236 */         }if (i < 0)
/* 237 */           i = j;
/*     */       }
/* 239 */       if (k == 0) {
/* 240 */         k = paramInt % (this.keyList.length - 1);
/* 241 */         if (k < 0) k = -k;
/* 242 */         k++;
/*     */       }
/*     */ 
/* 245 */       j = (j + k) % this.keyList.length;
/* 246 */       if (j == i)
/*     */       {
/* 248 */         return j;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int leastGreaterPrimeIndex(int paramInt)
/*     */   {
/* 255 */     for (int i = 0; (i < PRIMES.length) && 
/* 256 */       (paramInt >= PRIMES[i]); i++);
/* 260 */     return i == 0 ? 0 : i - 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.IntHashtable
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class UintMap
/*     */ {
/*     */   private static final int A = -1640531527;
/*     */   private static final int EMPTY = -1;
/*     */   private static final int DELETED = -2;
/*     */   private transient int[] keys;
/*     */   private transient Object[] values;
/*     */   private int power;
/*     */   private int keyCount;
/*     */   private transient int occupiedCount;
/*     */   private transient int ivaluesShift;
/*     */   private static final boolean check = false;
/*     */ 
/*     */   public UintMap()
/*     */   {
/*  57 */     this(4);
/*     */   }
/*     */ 
/*     */   public UintMap(int paramInt) {
/*  61 */     if (paramInt < 0) Kit.codeBug();
/*     */ 
/*  63 */     int i = paramInt * 4 / 3;
/*     */ 
/*  65 */     for (int j = 2; 1 << j < i; j++);
/*  66 */     this.power = j;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  71 */     return this.keyCount == 0;
/*     */   }
/*     */ 
/*     */   public int size() {
/*  75 */     return this.keyCount;
/*     */   }
/*     */ 
/*     */   public boolean has(int paramInt) {
/*  79 */     if (paramInt < 0) Kit.codeBug();
/*  80 */     return 0 <= findIndex(paramInt);
/*     */   }
/*     */ 
/*     */   public Object getObject(int paramInt)
/*     */   {
/*  88 */     if (paramInt < 0) Kit.codeBug();
/*  89 */     if (this.values != null) {
/*  90 */       int i = findIndex(paramInt);
/*  91 */       if (0 <= i) {
/*  92 */         return this.values[i];
/*     */       }
/*     */     }
/*  95 */     return null;
/*     */   }
/*     */ 
/*     */   public int getInt(int paramInt1, int paramInt2)
/*     */   {
/* 103 */     if (paramInt1 < 0) Kit.codeBug();
/* 104 */     int i = findIndex(paramInt1);
/* 105 */     if (0 <= i) {
/* 106 */       if (this.ivaluesShift != 0) {
/* 107 */         return this.keys[(this.ivaluesShift + i)];
/*     */       }
/* 109 */       return 0;
/*     */     }
/* 111 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   public int getExistingInt(int paramInt)
/*     */   {
/* 121 */     if (paramInt < 0) Kit.codeBug();
/* 122 */     int i = findIndex(paramInt);
/* 123 */     if (0 <= i) {
/* 124 */       if (this.ivaluesShift != 0) {
/* 125 */         return this.keys[(this.ivaluesShift + i)];
/*     */       }
/* 127 */       return 0;
/*     */     }
/*     */ 
/* 130 */     Kit.codeBug();
/* 131 */     return 0;
/*     */   }
/*     */ 
/*     */   public void put(int paramInt, Object paramObject)
/*     */   {
/* 139 */     if (paramInt < 0) Kit.codeBug();
/* 140 */     int i = ensureIndex(paramInt, false);
/* 141 */     if (this.values == null) {
/* 142 */       this.values = new Object[1 << this.power];
/*     */     }
/* 144 */     this.values[i] = paramObject;
/*     */   }
/*     */ 
/*     */   public void put(int paramInt1, int paramInt2)
/*     */   {
/* 152 */     if (paramInt1 < 0) Kit.codeBug();
/* 153 */     int i = ensureIndex(paramInt1, true);
/* 154 */     if (this.ivaluesShift == 0) {
/* 155 */       int j = 1 << this.power;
/*     */ 
/* 157 */       if (this.keys.length != j * 2) {
/* 158 */         int[] arrayOfInt = new int[j * 2];
/* 159 */         System.arraycopy(this.keys, 0, arrayOfInt, 0, j);
/* 160 */         this.keys = arrayOfInt;
/*     */       }
/* 162 */       this.ivaluesShift = j;
/*     */     }
/* 164 */     this.keys[(this.ivaluesShift + i)] = paramInt2;
/*     */   }
/*     */ 
/*     */   public void remove(int paramInt) {
/* 168 */     if (paramInt < 0) Kit.codeBug();
/* 169 */     int i = findIndex(paramInt);
/* 170 */     if (0 <= i) {
/* 171 */       this.keys[i] = -2;
/* 172 */       this.keyCount -= 1;
/*     */ 
/* 175 */       if (this.values != null) this.values[i] = null;
/* 176 */       if (this.ivaluesShift != 0) this.keys[(this.ivaluesShift + i)] = 0; 
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 181 */     int i = 1 << this.power;
/* 182 */     if (this.keys != null) {
/* 183 */       for (int j = 0; j != i; j++) {
/* 184 */         this.keys[j] = -1;
/*     */       }
/* 186 */       if (this.values != null) {
/* 187 */         for (j = 0; j != i; j++) {
/* 188 */           this.values[j] = null;
/*     */         }
/*     */       }
/*     */     }
/* 192 */     this.ivaluesShift = 0;
/* 193 */     this.keyCount = 0;
/* 194 */     this.occupiedCount = 0;
/*     */   }
/*     */ 
/*     */   public int[] getKeys()
/*     */   {
/* 199 */     int[] arrayOfInt1 = this.keys;
/* 200 */     int i = this.keyCount;
/* 201 */     int[] arrayOfInt2 = new int[i];
/* 202 */     for (int j = 0; i != 0; j++) {
/* 203 */       int k = arrayOfInt1[j];
/* 204 */       if ((k != -1) && (k != -2)) {
/* 205 */         arrayOfInt2[(--i)] = k;
/*     */       }
/*     */     }
/* 208 */     return arrayOfInt2;
/*     */   }
/*     */ 
/*     */   private static int tableLookupStep(int paramInt1, int paramInt2, int paramInt3) {
/* 212 */     int i = 32 - 2 * paramInt3;
/* 213 */     if (i >= 0) {
/* 214 */       return paramInt1 >>> i & paramInt2 | 0x1;
/*     */     }
/*     */ 
/* 217 */     return paramInt1 & paramInt2 >>> -i | 0x1;
/*     */   }
/*     */ 
/*     */   private int findIndex(int paramInt)
/*     */   {
/* 222 */     int[] arrayOfInt = this.keys;
/* 223 */     if (arrayOfInt != null) {
/* 224 */       int i = paramInt * -1640531527;
/* 225 */       int j = i >>> 32 - this.power;
/* 226 */       int k = arrayOfInt[j];
/* 227 */       if (k == paramInt) return j;
/* 228 */       if (k != -1)
/*     */       {
/* 230 */         int m = (1 << this.power) - 1;
/* 231 */         int n = tableLookupStep(i, m, this.power);
/* 232 */         int i1 = 0;
/*     */         do
/*     */         {
/* 238 */           j = j + n & m;
/* 239 */           k = arrayOfInt[j];
/* 240 */           if (k == paramInt) return j; 
/*     */         }
/* 241 */         while (k != -1);
/*     */       }
/*     */     }
/* 244 */     return -1;
/*     */   }
/*     */ 
/*     */   private int insertNewKey(int paramInt)
/*     */   {
/* 252 */     int[] arrayOfInt = this.keys;
/* 253 */     int i = paramInt * -1640531527;
/* 254 */     int j = i >>> 32 - this.power;
/* 255 */     if (arrayOfInt[j] != -1) {
/* 256 */       int k = (1 << this.power) - 1;
/* 257 */       int m = tableLookupStep(i, k, this.power);
/* 258 */       int n = j;
/*     */       do
/*     */       {
/* 261 */         j = j + m & k;
/*     */       }
/* 263 */       while (arrayOfInt[j] != -1);
/*     */     }
/* 265 */     arrayOfInt[j] = paramInt;
/* 266 */     this.occupiedCount += 1;
/* 267 */     this.keyCount += 1;
/* 268 */     return j;
/*     */   }
/*     */ 
/*     */   private void rehashTable(boolean paramBoolean) {
/* 272 */     if (this.keys != null)
/*     */     {
/* 274 */       if (this.keyCount * 2 >= this.occupiedCount)
/*     */       {
/* 276 */         this.power += 1;
/*     */       }
/*     */     }
/* 279 */     int i = 1 << this.power;
/* 280 */     int[] arrayOfInt = this.keys;
/* 281 */     int j = this.ivaluesShift;
/* 282 */     if ((j == 0) && (!paramBoolean)) {
/* 283 */       this.keys = new int[i];
/*     */     }
/*     */     else {
/* 286 */       this.ivaluesShift = i; this.keys = new int[i * 2];
/*     */     }
/* 288 */     for (int k = 0; k != i; k++) this.keys[k] = -1;
/*     */ 
/* 290 */     Object[] arrayOfObject = this.values;
/* 291 */     if (arrayOfObject != null) this.values = new Object[i];
/*     */ 
/* 293 */     int m = this.keyCount;
/* 294 */     this.occupiedCount = 0;
/* 295 */     if (m != 0) {
/* 296 */       this.keyCount = 0;
/* 297 */       int n = 0; for (int i1 = m; i1 != 0; n++) {
/* 298 */         int i2 = arrayOfInt[n];
/* 299 */         if ((i2 != -1) && (i2 != -2)) {
/* 300 */           int i3 = insertNewKey(i2);
/* 301 */           if (arrayOfObject != null) {
/* 302 */             this.values[i3] = arrayOfObject[n];
/*     */           }
/* 304 */           if (j != 0) {
/* 305 */             this.keys[(this.ivaluesShift + i3)] = arrayOfInt[(j + n)];
/*     */           }
/* 307 */           i1--;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int ensureIndex(int paramInt, boolean paramBoolean)
/*     */   {
/* 315 */     int i = -1;
/* 316 */     int j = -1;
/* 317 */     int[] arrayOfInt = this.keys;
/* 318 */     if (arrayOfInt != null) {
/* 319 */       int k = paramInt * -1640531527;
/* 320 */       i = k >>> 32 - this.power;
/* 321 */       int m = arrayOfInt[i];
/* 322 */       if (m == paramInt) return i;
/* 323 */       if (m != -1) {
/* 324 */         if (m == -2) j = i;
/*     */ 
/* 326 */         int n = (1 << this.power) - 1;
/* 327 */         int i1 = tableLookupStep(k, n, this.power);
/* 328 */         int i2 = 0;
/*     */         do
/*     */         {
/* 334 */           i = i + i1 & n;
/* 335 */           m = arrayOfInt[i];
/* 336 */           if (m == paramInt) return i;
/* 337 */           if ((m == -2) && (j < 0))
/* 338 */             j = i;
/*     */         }
/* 340 */         while (m != -1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 346 */     if (j >= 0) {
/* 347 */       i = j;
/*     */     }
/*     */     else
/*     */     {
/* 351 */       if ((arrayOfInt == null) || (this.occupiedCount * 4 >= (1 << this.power) * 3))
/*     */       {
/* 353 */         rehashTable(paramBoolean);
/* 354 */         return insertNewKey(paramInt);
/*     */       }
/* 356 */       this.occupiedCount += 1;
/*     */     }
/* 358 */     arrayOfInt[i] = paramInt;
/* 359 */     this.keyCount += 1;
/* 360 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.UintMap
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class ObjToIntMap
/*     */ {
/*     */   private static final int A = -1640531527;
/* 423 */   private static final Object DELETED = new Object();
/*     */   private transient Object[] keys;
/*     */   private transient int[] values;
/*     */   private int power;
/*     */   private int keyCount;
/*     */   private transient int occupiedCount;
/*     */   private static final boolean check = false;
/*     */ 
/*     */   public ObjToIntMap()
/*     */   {
/* 118 */     this(4);
/*     */   }
/*     */ 
/*     */   public ObjToIntMap(int paramInt) {
/* 122 */     if (paramInt < 0) Kit.codeBug();
/*     */ 
/* 124 */     int i = paramInt * 4 / 3;
/*     */ 
/* 126 */     for (int j = 2; 1 << j < i; j++);
/* 127 */     this.power = j;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 132 */     return this.keyCount == 0;
/*     */   }
/*     */ 
/*     */   public int size() {
/* 136 */     return this.keyCount;
/*     */   }
/*     */ 
/*     */   public boolean has(Object paramObject) {
/* 140 */     if (paramObject == null) paramObject = UniqueTag.NULL_VALUE;
/* 141 */     return 0 <= findIndex(paramObject);
/*     */   }
/*     */ 
/*     */   public int get(Object paramObject, int paramInt)
/*     */   {
/* 149 */     if (paramObject == null) paramObject = UniqueTag.NULL_VALUE;
/* 150 */     int i = findIndex(paramObject);
/* 151 */     if (0 <= i) {
/* 152 */       return this.values[i];
/*     */     }
/* 154 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public int getExisting(Object paramObject)
/*     */   {
/* 163 */     if (paramObject == null) paramObject = UniqueTag.NULL_VALUE;
/* 164 */     int i = findIndex(paramObject);
/* 165 */     if (0 <= i) {
/* 166 */       return this.values[i];
/*     */     }
/*     */ 
/* 169 */     Kit.codeBug();
/* 170 */     return 0;
/*     */   }
/*     */ 
/*     */   public void put(Object paramObject, int paramInt) {
/* 174 */     if (paramObject == null) paramObject = UniqueTag.NULL_VALUE;
/* 175 */     int i = ensureIndex(paramObject);
/* 176 */     this.values[i] = paramInt;
/*     */   }
/*     */ 
/*     */   public Object intern(Object paramObject)
/*     */   {
/* 185 */     int i = 0;
/* 186 */     if (paramObject == null) {
/* 187 */       i = 1;
/* 188 */       paramObject = UniqueTag.NULL_VALUE;
/*     */     }
/* 190 */     int j = ensureIndex(paramObject);
/* 191 */     this.values[j] = 0;
/* 192 */     return i != 0 ? null : this.keys[j];
/*     */   }
/*     */ 
/*     */   public void remove(Object paramObject) {
/* 196 */     if (paramObject == null) paramObject = UniqueTag.NULL_VALUE;
/* 197 */     int i = findIndex(paramObject);
/* 198 */     if (0 <= i) {
/* 199 */       this.keys[i] = DELETED;
/* 200 */       this.keyCount -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 205 */     int i = this.keys.length;
/* 206 */     while (i != 0) {
/* 207 */       this.keys[(--i)] = null;
/*     */     }
/* 209 */     this.keyCount = 0;
/* 210 */     this.occupiedCount = 0;
/*     */   }
/*     */ 
/*     */   public Iterator newIterator() {
/* 214 */     return new Iterator(this);
/*     */   }
/*     */ 
/*     */   final void initIterator(Iterator paramIterator)
/*     */   {
/* 221 */     paramIterator.init(this.keys, this.values, this.keyCount);
/*     */   }
/*     */ 
/*     */   public Object[] getKeys()
/*     */   {
/* 226 */     Object[] arrayOfObject = new Object[this.keyCount];
/* 227 */     getKeys(arrayOfObject, 0);
/* 228 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public void getKeys(Object[] paramArrayOfObject, int paramInt) {
/* 232 */     int i = this.keyCount;
/* 233 */     for (int j = 0; i != 0; j++) {
/* 234 */       Object localObject = this.keys[j];
/* 235 */       if ((localObject != null) && (localObject != DELETED)) {
/* 236 */         if (localObject == UniqueTag.NULL_VALUE) localObject = null;
/* 237 */         paramArrayOfObject[paramInt] = localObject;
/* 238 */         paramInt++;
/* 239 */         i--;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int tableLookupStep(int paramInt1, int paramInt2, int paramInt3) {
/* 245 */     int i = 32 - 2 * paramInt3;
/* 246 */     if (i >= 0) {
/* 247 */       return paramInt1 >>> i & paramInt2 | 0x1;
/*     */     }
/*     */ 
/* 250 */     return paramInt1 & paramInt2 >>> -i | 0x1;
/*     */   }
/*     */ 
/*     */   private int findIndex(Object paramObject)
/*     */   {
/* 255 */     if (this.keys != null) {
/* 256 */       int i = paramObject.hashCode();
/* 257 */       int j = i * -1640531527;
/* 258 */       int k = j >>> 32 - this.power;
/* 259 */       Object localObject = this.keys[k];
/* 260 */       if (localObject != null) {
/* 261 */         int m = 1 << this.power;
/* 262 */         if ((localObject == paramObject) || ((this.values[(m + k)] == i) && (localObject.equals(paramObject))))
/*     */         {
/* 265 */           return k;
/*     */         }
/*     */ 
/* 268 */         int n = m - 1;
/* 269 */         int i1 = tableLookupStep(j, n, this.power);
/* 270 */         int i2 = 0;
/*     */         do
/*     */         {
/* 276 */           k = k + i1 & n;
/* 277 */           localObject = this.keys[k];
/* 278 */           if (localObject == null)
/*     */             break;
/*     */         }
/* 281 */         while ((localObject != paramObject) && ((this.values[(m + k)] != i) || (!localObject.equals(paramObject))));
/*     */ 
/* 284 */         return k;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 289 */     return -1;
/*     */   }
/*     */ 
/*     */   private int insertNewKey(Object paramObject, int paramInt)
/*     */   {
/* 297 */     int i = paramInt * -1640531527;
/* 298 */     int j = i >>> 32 - this.power;
/* 299 */     int k = 1 << this.power;
/* 300 */     if (this.keys[j] != null) {
/* 301 */       int m = k - 1;
/* 302 */       int n = tableLookupStep(i, m, this.power);
/* 303 */       int i1 = j;
/*     */       do
/*     */       {
/* 306 */         j = j + n & m;
/*     */       }
/* 308 */       while (this.keys[j] != null);
/*     */     }
/* 310 */     this.keys[j] = paramObject;
/* 311 */     this.values[(k + j)] = paramInt;
/* 312 */     this.occupiedCount += 1;
/* 313 */     this.keyCount += 1;
/*     */ 
/* 315 */     return j;
/*     */   }
/*     */ 
/*     */   private void rehashTable()
/*     */   {
/*     */     int i;
/* 319 */     if (this.keys == null)
/*     */     {
/* 322 */       i = 1 << this.power;
/* 323 */       this.keys = new Object[i];
/* 324 */       this.values = new int[2 * i];
/*     */     }
/*     */     else
/*     */     {
/* 328 */       if (this.keyCount * 2 >= this.occupiedCount)
/*     */       {
/* 330 */         this.power += 1;
/*     */       }
/* 332 */       i = 1 << this.power;
/* 333 */       Object[] arrayOfObject = this.keys;
/* 334 */       int[] arrayOfInt = this.values;
/* 335 */       int j = arrayOfObject.length;
/* 336 */       this.keys = new Object[i];
/* 337 */       this.values = new int[2 * i];
/*     */ 
/* 339 */       int k = this.keyCount;
/* 340 */       this.occupiedCount = (this.keyCount = 0);
/* 341 */       for (int m = 0; k != 0; m++) {
/* 342 */         Object localObject = arrayOfObject[m];
/* 343 */         if ((localObject != null) && (localObject != DELETED)) {
/* 344 */           int n = arrayOfInt[(j + m)];
/* 345 */           int i1 = insertNewKey(localObject, n);
/* 346 */           this.values[i1] = arrayOfInt[m];
/* 347 */           k--;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int ensureIndex(Object paramObject)
/*     */   {
/* 355 */     int i = paramObject.hashCode();
/* 356 */     int j = -1;
/* 357 */     int k = -1;
/* 358 */     if (this.keys != null) {
/* 359 */       int m = i * -1640531527;
/* 360 */       j = m >>> 32 - this.power;
/* 361 */       Object localObject = this.keys[j];
/* 362 */       if (localObject != null) {
/* 363 */         int n = 1 << this.power;
/* 364 */         if ((localObject == paramObject) || ((this.values[(n + j)] == i) && (localObject.equals(paramObject))))
/*     */         {
/* 367 */           return j;
/*     */         }
/* 369 */         if (localObject == DELETED) {
/* 370 */           k = j;
/*     */         }
/*     */ 
/* 374 */         int i1 = n - 1;
/* 375 */         int i2 = tableLookupStep(m, i1, this.power);
/* 376 */         int i3 = 0;
/*     */         while (true)
/*     */         {
/* 382 */           j = j + i2 & i1;
/* 383 */           localObject = this.keys[j];
/* 384 */           if (localObject == null) {
/*     */             break;
/*     */           }
/* 387 */           if ((localObject == paramObject) || ((this.values[(n + j)] == i) && (localObject.equals(paramObject))))
/*     */           {
/* 390 */             return j;
/*     */           }
/* 392 */           if ((localObject == DELETED) && (k < 0)) {
/* 393 */             k = j;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 401 */     if (k >= 0) {
/* 402 */       j = k;
/*     */     }
/*     */     else
/*     */     {
/* 406 */       if ((this.keys == null) || (this.occupiedCount * 4 >= (1 << this.power) * 3))
/*     */       {
/* 408 */         rehashTable();
/* 409 */         return insertNewKey(paramObject, i);
/*     */       }
/* 411 */       this.occupiedCount += 1;
/*     */     }
/* 413 */     this.keys[j] = paramObject;
/* 414 */     this.values[((1 << this.power) + j)] = i;
/* 415 */     this.keyCount += 1;
/* 416 */     return j;
/*     */   }
/*     */ 
/*     */   public static class Iterator
/*     */   {
/*     */     ObjToIntMap master;
/*     */     private int cursor;
/*     */     private int remaining;
/*     */     private Object[] keys;
/*     */     private int[] values;
/*     */ 
/*     */     Iterator(ObjToIntMap paramObjToIntMap)
/*     */     {
/*  61 */       this.master = paramObjToIntMap;
/*     */     }
/*     */ 
/*     */     final void init(Object[] paramArrayOfObject, int[] paramArrayOfInt, int paramInt) {
/*  65 */       this.keys = paramArrayOfObject;
/*  66 */       this.values = paramArrayOfInt;
/*  67 */       this.cursor = -1;
/*  68 */       this.remaining = paramInt;
/*     */     }
/*     */ 
/*     */     public void start() {
/*  72 */       this.master.initIterator(this);
/*  73 */       next();
/*     */     }
/*     */ 
/*     */     public boolean done() {
/*  77 */       return this.remaining < 0;
/*     */     }
/*     */ 
/*     */     public void next() {
/*  81 */       if (this.remaining == -1) Kit.codeBug();
/*  82 */       if (this.remaining == 0) {
/*  83 */         this.remaining = -1;
/*  84 */         this.cursor = -1;
/*     */       } else {
/*  86 */         for (this.cursor += 1; ; this.cursor += 1) {
/*  87 */           Object localObject = this.keys[this.cursor];
/*  88 */           if ((localObject != null) && (localObject != ObjToIntMap.DELETED)) {
/*  89 */             this.remaining -= 1;
/*  90 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object getKey() {
/*  97 */       Object localObject = this.keys[this.cursor];
/*  98 */       if (localObject == UniqueTag.NULL_VALUE) localObject = null;
/*  99 */       return localObject;
/*     */     }
/*     */ 
/*     */     public int getValue() {
/* 103 */       return this.values[this.cursor];
/*     */     }
/*     */ 
/*     */     public void setValue(int paramInt) {
/* 107 */       this.values[this.cursor] = paramInt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ObjToIntMap
 * JD-Core Version:    0.6.2
 */
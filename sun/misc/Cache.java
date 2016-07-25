/*     */ package sun.misc;
/*     */ 
/*     */ import java.util.Dictionary;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class Cache extends Dictionary
/*     */ {
/*     */   private CacheEntry[] table;
/*     */   private int count;
/*     */   private int threshold;
/*     */   private float loadFactor;
/*     */ 
/*     */   private void init(int paramInt, float paramFloat)
/*     */   {
/* 100 */     if ((paramInt <= 0) || (paramFloat <= 0.0D)) {
/* 101 */       throw new IllegalArgumentException();
/*     */     }
/* 103 */     this.loadFactor = paramFloat;
/* 104 */     this.table = new CacheEntry[paramInt];
/* 105 */     this.threshold = ((int)(paramInt * paramFloat));
/*     */   }
/*     */ 
/*     */   public Cache(int paramInt, float paramFloat)
/*     */   {
/* 121 */     init(paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   public Cache(int paramInt)
/*     */   {
/* 130 */     init(paramInt, 0.75F);
/*     */   }
/*     */ 
/*     */   public Cache()
/*     */   {
/*     */     try
/*     */     {
/* 140 */       init(101, 0.75F);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException) {
/* 143 */       throw new Error("panic");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 151 */     return this.count;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 158 */     return this.count == 0;
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration keys()
/*     */   {
/* 167 */     return new CacheEnumerator(this.table, true);
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration elements()
/*     */   {
/* 177 */     return new CacheEnumerator(this.table, false);
/*     */   }
/*     */ 
/*     */   public synchronized Object get(Object paramObject)
/*     */   {
/* 188 */     CacheEntry[] arrayOfCacheEntry = this.table;
/* 189 */     int i = paramObject.hashCode();
/* 190 */     int j = (i & 0x7FFFFFFF) % arrayOfCacheEntry.length;
/* 191 */     for (CacheEntry localCacheEntry = arrayOfCacheEntry[j]; localCacheEntry != null; localCacheEntry = localCacheEntry.next) {
/* 192 */       if ((localCacheEntry.hash == i) && (localCacheEntry.key.equals(paramObject))) {
/* 193 */         return localCacheEntry.check();
/*     */       }
/*     */     }
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */   protected void rehash()
/*     */   {
/* 205 */     int i = this.table.length;
/* 206 */     CacheEntry[] arrayOfCacheEntry1 = this.table;
/*     */ 
/* 208 */     int j = i * 2 + 1;
/* 209 */     CacheEntry[] arrayOfCacheEntry2 = new CacheEntry[j];
/*     */ 
/* 211 */     this.threshold = ((int)(j * this.loadFactor));
/* 212 */     this.table = arrayOfCacheEntry2;
/*     */ 
/* 217 */     for (int k = i; k-- > 0; )
/* 218 */       for (localCacheEntry1 = arrayOfCacheEntry1[k]; localCacheEntry1 != null; ) {
/* 219 */         CacheEntry localCacheEntry2 = localCacheEntry1;
/* 220 */         localCacheEntry1 = localCacheEntry1.next;
/* 221 */         if (localCacheEntry2.check() != null) {
/* 222 */           int m = (localCacheEntry2.hash & 0x7FFFFFFF) % j;
/* 223 */           localCacheEntry2.next = arrayOfCacheEntry2[m];
/* 224 */           arrayOfCacheEntry2[m] = localCacheEntry2;
/*     */         } else {
/* 226 */           this.count -= 1;
/*     */         }
/*     */       }
/*     */     CacheEntry localCacheEntry1;
/*     */   }
/*     */ 
/*     */   public synchronized Object put(Object paramObject1, Object paramObject2)
/*     */   {
/* 244 */     if (paramObject2 == null) {
/* 245 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 248 */     CacheEntry[] arrayOfCacheEntry = this.table;
/* 249 */     int i = paramObject1.hashCode();
/* 250 */     int j = (i & 0x7FFFFFFF) % arrayOfCacheEntry.length;
/* 251 */     Object localObject1 = null;
/* 252 */     for (CacheEntry localCacheEntry = arrayOfCacheEntry[j]; localCacheEntry != null; localCacheEntry = localCacheEntry.next) {
/* 253 */       if ((localCacheEntry.hash == i) && (localCacheEntry.key.equals(paramObject1))) {
/* 254 */         Object localObject2 = localCacheEntry.check();
/* 255 */         localCacheEntry.setThing(paramObject2);
/* 256 */         return localObject2;
/* 257 */       }if (localCacheEntry.check() == null) {
/* 258 */         localObject1 = localCacheEntry;
/*     */       }
/*     */     }
/* 261 */     if (this.count >= this.threshold)
/*     */     {
/* 263 */       rehash();
/* 264 */       return put(paramObject1, paramObject2);
/*     */     }
/*     */ 
/* 267 */     if (localObject1 == null) {
/* 268 */       localObject1 = new CacheEntry();
/* 269 */       ((CacheEntry)localObject1).next = arrayOfCacheEntry[j];
/* 270 */       arrayOfCacheEntry[j] = localObject1;
/* 271 */       this.count += 1;
/*     */     }
/* 273 */     ((CacheEntry)localObject1).hash = i;
/* 274 */     ((CacheEntry)localObject1).key = paramObject1;
/* 275 */     ((CacheEntry)localObject1).setThing(paramObject2);
/* 276 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized Object remove(Object paramObject)
/*     */   {
/* 286 */     CacheEntry[] arrayOfCacheEntry = this.table;
/* 287 */     int i = paramObject.hashCode();
/* 288 */     int j = (i & 0x7FFFFFFF) % arrayOfCacheEntry.length;
/* 289 */     CacheEntry localCacheEntry1 = arrayOfCacheEntry[j]; for (CacheEntry localCacheEntry2 = null; localCacheEntry1 != null; localCacheEntry1 = localCacheEntry1.next) {
/* 290 */       if ((localCacheEntry1.hash == i) && (localCacheEntry1.key.equals(paramObject))) {
/* 291 */         if (localCacheEntry2 != null)
/* 292 */           localCacheEntry2.next = localCacheEntry1.next;
/*     */         else {
/* 294 */           arrayOfCacheEntry[j] = localCacheEntry1.next;
/*     */         }
/* 296 */         this.count -= 1;
/* 297 */         return localCacheEntry1.check();
/*     */       }
/* 289 */       localCacheEntry2 = localCacheEntry1;
/*     */     }
/*     */ 
/* 300 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Cache
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.misc;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ class CacheEnumerator
/*     */   implements Enumeration
/*     */ {
/*     */   boolean keys;
/*     */   int index;
/*     */   CacheEntry[] table;
/*     */   CacheEntry entry;
/*     */ 
/*     */   CacheEnumerator(CacheEntry[] paramArrayOfCacheEntry, boolean paramBoolean)
/*     */   {
/* 315 */     this.table = paramArrayOfCacheEntry;
/* 316 */     this.keys = paramBoolean;
/* 317 */     this.index = paramArrayOfCacheEntry.length;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements() {
/* 321 */     if (this.index >= 0) {
/* 322 */       while (this.entry != null) {
/* 323 */         if (this.entry.check() != null) {
/* 324 */           return true;
/*     */         }
/* 326 */         this.entry = this.entry.next;
/* 327 */       }while ((--this.index >= 0) && ((this.entry = this.table[this.index]) == null));
/*     */     }
/* 329 */     return false;
/*     */   }
/*     */ 
/*     */   public Object nextElement() {
/* 333 */     while (this.index >= 0) {
/* 334 */       while ((this.entry == null) && 
/* 335 */         (--this.index >= 0) && ((this.entry = this.table[this.index]) == null));
/* 336 */       if (this.entry != null) {
/* 337 */         CacheEntry localCacheEntry = this.entry;
/* 338 */         this.entry = localCacheEntry.next;
/* 339 */         if (localCacheEntry.check() != null)
/* 340 */           return this.keys ? localCacheEntry.key : localCacheEntry.check();
/*     */       }
/*     */     }
/* 343 */     throw new NoSuchElementException("CacheEnumerator");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.CacheEnumerator
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.security.util;
/*     */ 
/*     */ class NullCache extends Cache
/*     */ {
/* 204 */   static final Cache INSTANCE = new NullCache();
/*     */ 
/*     */   public int size()
/*     */   {
/* 211 */     return 0;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void put(Object paramObject1, Object paramObject2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject) {
/* 223 */     return null;
/*     */   }
/*     */ 
/*     */   public void remove(Object paramObject)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setCapacity(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setTimeout(int paramInt)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void accept(Cache.CacheVisitor paramCacheVisitor)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.NullCache
 * JD-Core Version:    0.6.2
 */
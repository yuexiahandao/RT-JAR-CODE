/*     */ package sun.security.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class Cache
/*     */ {
/*     */   public abstract int size();
/*     */ 
/*     */   public abstract void clear();
/*     */ 
/*     */   public abstract void put(Object paramObject1, Object paramObject2);
/*     */ 
/*     */   public abstract Object get(Object paramObject);
/*     */ 
/*     */   public abstract void remove(Object paramObject);
/*     */ 
/*     */   public abstract void setCapacity(int paramInt);
/*     */ 
/*     */   public abstract void setTimeout(int paramInt);
/*     */ 
/*     */   public abstract void accept(CacheVisitor paramCacheVisitor);
/*     */ 
/*     */   public static Cache newSoftMemoryCache(int paramInt)
/*     */   {
/* 123 */     return new MemoryCache(true, paramInt);
/*     */   }
/*     */ 
/*     */   public static Cache newSoftMemoryCache(int paramInt1, int paramInt2)
/*     */   {
/* 132 */     return new MemoryCache(true, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public static Cache newHardMemoryCache(int paramInt)
/*     */   {
/* 140 */     return new MemoryCache(false, paramInt);
/*     */   }
/*     */ 
/*     */   public static Cache newNullCache()
/*     */   {
/* 147 */     return NullCache.INSTANCE;
/*     */   }
/*     */ 
/*     */   public static Cache newHardMemoryCache(int paramInt1, int paramInt2)
/*     */   {
/* 156 */     return new MemoryCache(false, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public static abstract interface CacheVisitor {
/*     */     public abstract void visit(Map<Object, Object> paramMap);
/*     */   }
/*     */ 
/*     */   public static class EqualByteArray {
/*     */     private final byte[] b;
/*     */     private volatile int hash;
/*     */ 
/*     */     public EqualByteArray(byte[] paramArrayOfByte) {
/* 169 */       this.b = paramArrayOfByte;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 173 */       int i = this.hash;
/* 174 */       if (i == 0) {
/* 175 */         i = this.b.length + 1;
/* 176 */         for (int j = 0; j < this.b.length; j++) {
/* 177 */           i += (this.b[j] & 0xFF) * 37;
/*     */         }
/* 179 */         this.hash = i;
/*     */       }
/* 181 */       return i;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 185 */       if (this == paramObject) {
/* 186 */         return true;
/*     */       }
/* 188 */       if (!(paramObject instanceof EqualByteArray)) {
/* 189 */         return false;
/*     */       }
/* 191 */       EqualByteArray localEqualByteArray = (EqualByteArray)paramObject;
/* 192 */       return Arrays.equals(this.b, localEqualByteArray.b);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.Cache
 * JD-Core Version:    0.6.2
 */
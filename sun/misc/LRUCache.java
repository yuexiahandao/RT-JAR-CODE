/*    */ package sun.misc;
/*    */ 
/*    */ public abstract class LRUCache<N, V>
/*    */ {
/* 35 */   private V[] oa = null;
/*    */   private final int size;
/*    */ 
/*    */   public LRUCache(int paramInt)
/*    */   {
/* 39 */     this.size = paramInt;
/*    */   }
/*    */ 
/*    */   protected abstract V create(N paramN);
/*    */ 
/*    */   protected abstract boolean hasName(V paramV, N paramN);
/*    */ 
/*    */   public static void moveToFront(Object[] paramArrayOfObject, int paramInt) {
/* 47 */     Object localObject = paramArrayOfObject[paramInt];
/* 48 */     for (int i = paramInt; i > 0; i--)
/* 49 */       paramArrayOfObject[i] = paramArrayOfObject[(i - 1)];
/* 50 */     paramArrayOfObject[0] = localObject;
/*    */   }
/*    */ 
/*    */   public V forName(N paramN) {
/* 54 */     if (this.oa == null)
/* 55 */       this.oa = ((Object[])new Object[this.size]);
/*    */     else {
/* 57 */       for (int i = 0; i < this.oa.length; i++) {
/* 58 */         Object localObject2 = this.oa[i];
/* 59 */         if (localObject2 != null)
/*    */         {
/* 61 */           if (hasName(localObject2, paramN)) {
/* 62 */             if (i > 0)
/* 63 */               moveToFront(this.oa, i);
/* 64 */             return localObject2;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 70 */     Object localObject1 = create(paramN);
/* 71 */     this.oa[(this.oa.length - 1)] = localObject1;
/* 72 */     moveToFront(this.oa, this.oa.length - 1);
/* 73 */     return localObject1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.LRUCache
 * JD-Core Version:    0.6.2
 */
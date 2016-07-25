/*    */ package sun.rmi.server;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
/*    */ 
/*    */ public abstract class WeakClassHashMap<V>
/*    */ {
/* 49 */   private Map<Class<?>, ValueCell<V>> internalMap = new WeakHashMap();
/*    */ 
/*    */   public V get(Class<?> paramClass)
/*    */   {
/*    */     ValueCell localValueCell;
/* 61 */     synchronized (this.internalMap) {
/* 62 */       localValueCell = (ValueCell)this.internalMap.get(paramClass);
/* 63 */       if (localValueCell == null) {
/* 64 */         localValueCell = new ValueCell();
/* 65 */         this.internalMap.put(paramClass, localValueCell);
/*    */       }
/*    */     }
/* 68 */     synchronized (localValueCell) {
/* 69 */       Object localObject2 = null;
/* 70 */       if (localValueCell.ref != null) {
/* 71 */         localObject2 = localValueCell.ref.get();
/*    */       }
/* 73 */       if (localObject2 == null) {
/* 74 */         localObject2 = computeValue(paramClass);
/* 75 */         localValueCell.ref = new SoftReference(localObject2);
/*    */       }
/* 77 */       return localObject2;
/*    */     }
/*    */   }
/*    */ 
/*    */   protected abstract V computeValue(Class<?> paramClass);
/*    */ 
/*    */   private static class ValueCell<T> {
/* 84 */     Reference<T> ref = null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.WeakClassHashMap
 * JD-Core Version:    0.6.2
 */
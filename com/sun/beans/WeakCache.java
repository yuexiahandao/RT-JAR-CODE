/*    */ package com.sun.beans;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
/*    */ 
/*    */ public final class WeakCache<K, V>
/*    */ {
/* 45 */   private final Map<K, Reference<V>> map = new WeakHashMap();
/*    */ 
/*    */   public V get(K paramK)
/*    */   {
/* 55 */     Reference localReference = (Reference)this.map.get(paramK);
/* 56 */     if (localReference == null) {
/* 57 */       return null;
/*    */     }
/* 59 */     Object localObject = localReference.get();
/* 60 */     if (localObject == null) {
/* 61 */       this.map.remove(paramK);
/*    */     }
/* 63 */     return localObject;
/*    */   }
/*    */ 
/*    */   public void put(K paramK, V paramV)
/*    */   {
/* 77 */     if (paramV != null) {
/* 78 */       this.map.put(paramK, new WeakReference(paramV));
/*    */     }
/*    */     else
/* 81 */       this.map.remove(paramK);
/*    */   }
/*    */ 
/*    */   public void clear()
/*    */   {
/* 89 */     this.map.clear();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.WeakCache
 * JD-Core Version:    0.6.2
 */
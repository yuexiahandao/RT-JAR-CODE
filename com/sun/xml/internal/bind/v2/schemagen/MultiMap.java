/*    */ package com.sun.xml.internal.bind.v2.schemagen;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ final class MultiMap<K extends Comparable<K>, V> extends TreeMap<K, V>
/*    */ {
/*    */   private final V many;
/*    */ 
/*    */   public MultiMap(V many)
/*    */   {
/* 45 */     this.many = many;
/*    */   }
/*    */ 
/*    */   public V put(K key, V value)
/*    */   {
/* 50 */     Object old = super.put(key, value);
/* 51 */     if ((old != null) && (!old.equals(value)))
/*    */     {
/* 53 */       super.put(key, this.many);
/*    */     }
/* 55 */     return old;
/*    */   }
/*    */ 
/*    */   public void putAll(Map<? extends K, ? extends V> map)
/*    */   {
/* 60 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.MultiMap
 * JD-Core Version:    0.6.2
 */
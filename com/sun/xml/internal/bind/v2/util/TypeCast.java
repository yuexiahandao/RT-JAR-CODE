/*    */ package com.sun.xml.internal.bind.v2.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class TypeCast
/*    */ {
/*    */   public static <K, V> Map<K, V> checkedCast(Map<?, ?> m, Class<K> keyType, Class<V> valueType)
/*    */   {
/* 38 */     if (m == null)
/* 39 */       return null;
/* 40 */     for (Map.Entry e : m.entrySet()) {
/* 41 */       if (!keyType.isInstance(e.getKey()))
/* 42 */         throw new ClassCastException(e.getKey().getClass().toString());
/* 43 */       if (!valueType.isInstance(e.getValue()))
/* 44 */         throw new ClassCastException(e.getValue().getClass().toString());
/*    */     }
/* 46 */     return m;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.util.TypeCast
 * JD-Core Version:    0.6.2
 */
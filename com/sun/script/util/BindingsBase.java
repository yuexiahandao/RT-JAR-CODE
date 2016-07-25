/*    */ package com.sun.script.util;
/*    */ 
/*    */ import java.util.AbstractMap;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import javax.script.Bindings;
/*    */ 
/*    */ public abstract class BindingsBase extends AbstractMap<String, Object>
/*    */   implements Bindings
/*    */ {
/*    */   public Object get(Object paramObject)
/*    */   {
/* 43 */     checkKey(paramObject);
/* 44 */     return getImpl((String)paramObject);
/*    */   }
/*    */ 
/*    */   public Object remove(Object paramObject) {
/* 48 */     checkKey(paramObject);
/* 49 */     return removeImpl((String)paramObject);
/*    */   }
/*    */ 
/*    */   public Object put(String paramString, Object paramObject) {
/* 53 */     checkKey(paramString);
/* 54 */     return putImpl(paramString, paramObject);
/*    */   }
/*    */ 
/*    */   public void putAll(Map<? extends String, ? extends Object> paramMap) {
/* 58 */     for (Map.Entry localEntry : paramMap.entrySet()) {
/* 59 */       String str = (String)localEntry.getKey();
/* 60 */       checkKey(str);
/* 61 */       putImpl((String)localEntry.getKey(), localEntry.getValue());
/*    */     }
/*    */   }
/*    */   public abstract Object putImpl(String paramString, Object paramObject);
/*    */ 
/*    */   public abstract Object getImpl(String paramString);
/*    */ 
/*    */   public abstract Object removeImpl(String paramString);
/*    */ 
/*    */   public abstract String[] getNames();
/*    */ 
/* 72 */   protected void checkKey(Object paramObject) { if (paramObject == null) {
/* 73 */       throw new NullPointerException("key can not be null");
/*    */     }
/* 75 */     if (!(paramObject instanceof String)) {
/* 76 */       throw new ClassCastException("key should be String");
/*    */     }
/* 78 */     if (paramObject.equals(""))
/* 79 */       throw new IllegalArgumentException("key can not be empty");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.util.BindingsBase
 * JD-Core Version:    0.6.2
 */
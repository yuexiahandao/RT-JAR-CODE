/*     */ package javax.script;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class SimpleBindings
/*     */   implements Bindings
/*     */ {
/*     */   private Map<String, Object> map;
/*     */ 
/*     */   public SimpleBindings(Map<String, Object> paramMap)
/*     */   {
/*  53 */     if (paramMap == null) {
/*  54 */       throw new NullPointerException();
/*     */     }
/*  56 */     this.map = paramMap;
/*     */   }
/*     */ 
/*     */   public SimpleBindings()
/*     */   {
/*  63 */     this(new HashMap());
/*     */   }
/*     */ 
/*     */   public Object put(String paramString, Object paramObject)
/*     */   {
/*  79 */     checkKey(paramString);
/*  80 */     return this.map.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void putAll(Map<? extends String, ? extends Object> paramMap)
/*     */   {
/*  94 */     if (paramMap == null) {
/*  95 */       throw new NullPointerException("toMerge map is null");
/*     */     }
/*  97 */     for (Map.Entry localEntry : paramMap.entrySet()) {
/*  98 */       String str = (String)localEntry.getKey();
/*  99 */       checkKey(str);
/* 100 */       put(str, localEntry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 106 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 125 */     checkKey(paramObject);
/* 126 */     return this.map.containsKey(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject)
/*     */   {
/* 131 */     return this.map.containsValue(paramObject);
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<String, Object>> entrySet()
/*     */   {
/* 136 */     return this.map.entrySet();
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/* 161 */     checkKey(paramObject);
/* 162 */     return this.map.get(paramObject);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 167 */     return this.map.isEmpty();
/*     */   }
/*     */ 
/*     */   public Set<String> keySet()
/*     */   {
/* 172 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 198 */     checkKey(paramObject);
/* 199 */     return this.map.remove(paramObject);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 204 */     return this.map.size();
/*     */   }
/*     */ 
/*     */   public Collection<Object> values()
/*     */   {
/* 209 */     return this.map.values();
/*     */   }
/*     */ 
/*     */   private void checkKey(Object paramObject) {
/* 213 */     if (paramObject == null) {
/* 214 */       throw new NullPointerException("key can not be null");
/*     */     }
/* 216 */     if (!(paramObject instanceof String)) {
/* 217 */       throw new ClassCastException("key should be a String");
/*     */     }
/* 219 */     if (paramObject.equals(""))
/* 220 */       throw new IllegalArgumentException("key can not be empty");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.script.SimpleBindings
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.net.httpserver;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class Headers
/*     */   implements Map<String, List<String>>
/*     */ {
/*  65 */   HashMap<String, List<String>> map = new HashMap(32);
/*     */ 
/*     */   private String normalize(String paramString)
/*     */   {
/*  72 */     if (paramString == null) {
/*  73 */       return null;
/*     */     }
/*  75 */     int i = paramString.length();
/*  76 */     if (i == 0) {
/*  77 */       return paramString;
/*     */     }
/*  79 */     char[] arrayOfChar = paramString.toCharArray();
/*  80 */     if ((arrayOfChar[0] >= 'a') && (arrayOfChar[0] <= 'z')) {
/*  81 */       arrayOfChar[0] = ((char)(arrayOfChar[0] - ' '));
/*     */     }
/*  83 */     for (int j = 1; j < i; j++) {
/*  84 */       if ((arrayOfChar[j] >= 'A') && (arrayOfChar[j] <= 'Z')) {
/*  85 */         arrayOfChar[j] = ((char)(arrayOfChar[j] + ' '));
/*     */       }
/*     */     }
/*  88 */     return new String(arrayOfChar);
/*     */   }
/*     */   public int size() {
/*  91 */     return this.map.size();
/*     */   }
/*  93 */   public boolean isEmpty() { return this.map.isEmpty(); }
/*     */ 
/*     */   public boolean containsKey(Object paramObject) {
/*  96 */     if (paramObject == null) {
/*  97 */       return false;
/*     */     }
/*  99 */     if (!(paramObject instanceof String)) {
/* 100 */       return false;
/*     */     }
/* 102 */     return this.map.containsKey(normalize((String)paramObject));
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject) {
/* 106 */     return this.map.containsValue(paramObject);
/*     */   }
/*     */ 
/*     */   public List<String> get(Object paramObject) {
/* 110 */     return (List)this.map.get(normalize((String)paramObject));
/*     */   }
/*     */ 
/*     */   public String getFirst(String paramString)
/*     */   {
/* 120 */     List localList = (List)this.map.get(normalize(paramString));
/* 121 */     if (localList == null) {
/* 122 */       return null;
/*     */     }
/* 124 */     return (String)localList.get(0);
/*     */   }
/*     */ 
/*     */   public List<String> put(String paramString, List<String> paramList) {
/* 128 */     return (List)this.map.put(normalize(paramString), paramList);
/*     */   }
/*     */ 
/*     */   public void add(String paramString1, String paramString2)
/*     */   {
/* 139 */     String str = normalize(paramString1);
/* 140 */     Object localObject = (List)this.map.get(str);
/* 141 */     if (localObject == null) {
/* 142 */       localObject = new LinkedList();
/* 143 */       this.map.put(str, localObject);
/*     */     }
/* 145 */     ((List)localObject).add(paramString2);
/*     */   }
/*     */ 
/*     */   public void set(String paramString1, String paramString2)
/*     */   {
/* 156 */     LinkedList localLinkedList = new LinkedList();
/* 157 */     localLinkedList.add(paramString2);
/* 158 */     put(paramString1, localLinkedList);
/*     */   }
/*     */ 
/*     */   public List<String> remove(Object paramObject)
/*     */   {
/* 163 */     return (List)this.map.remove(normalize((String)paramObject));
/*     */   }
/*     */ 
/*     */   public void putAll(Map<? extends String, ? extends List<String>> paramMap) {
/* 167 */     this.map.putAll(paramMap);
/*     */   }
/*     */   public void clear() {
/* 170 */     this.map.clear();
/*     */   }
/* 172 */   public Set<String> keySet() { return this.map.keySet(); } 
/*     */   public Collection<List<String>> values() {
/* 174 */     return this.map.values();
/*     */   }
/*     */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 177 */     return this.map.entrySet();
/*     */   }
/*     */   public boolean equals(Object paramObject) {
/* 180 */     return this.map.equals(paramObject);
/*     */   }
/* 182 */   public int hashCode() { return this.map.hashCode(); }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.Headers
 * JD-Core Version:    0.6.2
 */
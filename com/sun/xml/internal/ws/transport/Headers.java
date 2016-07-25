/*     */ package com.sun.xml.internal.ws.transport;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class Headers extends TreeMap<String, List<String>>
/*     */ {
/*  74 */   private static final InsensitiveComparator INSTANCE = new InsensitiveComparator(null);
/*     */ 
/*     */   public Headers()
/*     */   {
/*  71 */     super(INSTANCE);
/*     */   }
/*     */ 
/*     */   public void add(String key, String value)
/*     */   {
/*  97 */     List list = (List)get(key);
/*  98 */     if (list == null) {
/*  99 */       list = new LinkedList();
/* 100 */       put(key, list);
/*     */     }
/* 102 */     list.add(value);
/*     */   }
/*     */ 
/*     */   public String getFirst(String key)
/*     */   {
/* 113 */     List l = (List)get(key);
/* 114 */     return l == null ? null : (String)l.get(0);
/*     */   }
/*     */ 
/*     */   public void set(String key, String value)
/*     */   {
/* 125 */     LinkedList l = new LinkedList();
/* 126 */     l.add(value);
/* 127 */     put(key, l);
/*     */   }
/*     */ 
/*     */   private static final class InsensitiveComparator
/*     */     implements Comparator<String>
/*     */   {
/*     */     public int compare(String o1, String o2)
/*     */     {
/*  79 */       if ((o1 == null) && (o2 == null))
/*  80 */         return 0;
/*  81 */       if (o1 == null)
/*  82 */         return -1;
/*  83 */       if (o2 == null)
/*  84 */         return 1;
/*  85 */       return o1.compareToIgnoreCase(o2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.Headers
 * JD-Core Version:    0.6.2
 */
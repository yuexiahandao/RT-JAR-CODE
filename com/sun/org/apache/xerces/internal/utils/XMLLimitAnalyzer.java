/*     */ package com.sun.org.apache.xerces.internal.utils;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Formatter;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class XMLLimitAnalyzer
/*     */ {
/*     */   private final int[] values;
/*     */   private final String[] names;
/*     */   private final int[] totalValue;
/*     */   private final Map[] caches;
/*     */   private String entityStart;
/*     */   private String entityEnd;
/*     */ 
/*     */   public XMLLimitAnalyzer()
/*     */   {
/* 104 */     this.values = new int[XMLSecurityManager.Limit.values().length];
/* 105 */     this.totalValue = new int[XMLSecurityManager.Limit.values().length];
/* 106 */     this.names = new String[XMLSecurityManager.Limit.values().length];
/* 107 */     this.caches = new Map[XMLSecurityManager.Limit.values().length];
/*     */   }
/*     */ 
/*     */   public void addValue(XMLSecurityManager.Limit limit, String entityName, int value)
/*     */   {
/* 119 */     addValue(limit.ordinal(), entityName, value);
/*     */   }
/*     */ 
/*     */   public void addValue(int index, String entityName, int value)
/*     */   {
/* 129 */     if ((index == XMLSecurityManager.Limit.ENTITY_EXPANSION_LIMIT.ordinal()) || (index == XMLSecurityManager.Limit.MAX_OCCUR_NODE_LIMIT.ordinal()) || (index == XMLSecurityManager.Limit.ELEMENT_ATTRIBUTE_LIMIT.ordinal()))
/*     */     {
/* 132 */       this.totalValue[index] += value;
/* 133 */       return;
/*     */     }
/* 135 */     if (index == XMLSecurityManager.Limit.MAX_ELEMENT_DEPTH_LIMIT.ordinal()) {
/* 136 */       this.totalValue[index] = value;
/*     */       return;
/*     */     }
/*     */     Map cache;
/* 141 */     if (this.caches[index] == null) {
/* 142 */       Map cache = new HashMap(10);
/* 143 */       this.caches[index] = cache;
/*     */     } else {
/* 145 */       cache = this.caches[index];
/*     */     }
/*     */ 
/* 148 */     int accumulatedValue = value;
/* 149 */     if (cache.containsKey(entityName)) {
/* 150 */       accumulatedValue += ((Integer)cache.get(entityName)).intValue();
/* 151 */       cache.put(entityName, Integer.valueOf(accumulatedValue));
/*     */     } else {
/* 153 */       cache.put(entityName, Integer.valueOf(value));
/*     */     }
/*     */ 
/* 156 */     if (accumulatedValue > this.values[index]) {
/* 157 */       this.values[index] = accumulatedValue;
/* 158 */       this.names[index] = entityName;
/*     */     }
/*     */ 
/* 162 */     if ((index == XMLSecurityManager.Limit.GENEAL_ENTITY_SIZE_LIMIT.ordinal()) || (index == XMLSecurityManager.Limit.PARAMETER_ENTITY_SIZE_LIMIT.ordinal()))
/*     */     {
/* 164 */       this.totalValue[XMLSecurityManager.Limit.TOTAL_ENTITY_SIZE_LIMIT.ordinal()] += value;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getValue(XMLSecurityManager.Limit limit)
/*     */   {
/* 175 */     return this.values[limit.ordinal()];
/*     */   }
/*     */ 
/*     */   public int getValue(int index) {
/* 179 */     return this.values[index];
/*     */   }
/*     */ 
/*     */   public int getTotalValue(XMLSecurityManager.Limit limit)
/*     */   {
/* 188 */     return this.totalValue[limit.ordinal()];
/*     */   }
/*     */ 
/*     */   public int getTotalValue(int index) {
/* 192 */     return this.totalValue[index];
/*     */   }
/*     */ 
/*     */   public int getValueByIndex(int index)
/*     */   {
/* 200 */     return this.values[index];
/*     */   }
/*     */ 
/*     */   public void startEntity(String name) {
/* 204 */     this.entityStart = name;
/*     */   }
/*     */ 
/*     */   public boolean isTracking(String name) {
/* 208 */     if (this.entityStart == null) {
/* 209 */       return false;
/*     */     }
/* 211 */     return this.entityStart.equals(name);
/*     */   }
/*     */ 
/*     */   public void endEntity(XMLSecurityManager.Limit limit, String name)
/*     */   {
/* 219 */     this.entityStart = "";
/* 220 */     Map cache = this.caches[limit.ordinal()];
/* 221 */     if (cache != null)
/* 222 */       cache.remove(name);
/*     */   }
/*     */ 
/*     */   public void debugPrint(XMLSecurityManager securityManager)
/*     */   {
/* 227 */     Formatter formatter = new Formatter();
/* 228 */     System.out.println(formatter.format("%30s %15s %15s %15s %30s", new Object[] { "Property", "Limit", "Total size", "Size", "Entity Name" }));
/*     */ 
/* 231 */     for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
/* 232 */       formatter = new Formatter();
/* 233 */       System.out.println(formatter.format("%30s %15d %15d %15d %30s", new Object[] { limit.name(), Integer.valueOf(securityManager.getLimit(limit)), Integer.valueOf(this.totalValue[limit.ordinal()]), Integer.valueOf(this.values[limit.ordinal()]), this.names[limit.ordinal()] }));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum NameMap
/*     */   {
/*  60 */     ENTITY_EXPANSION_LIMIT("jdk.xml.entityExpansionLimit", "entityExpansionLimit"), 
/*  61 */     MAX_OCCUR_NODE_LIMIT("jdk.xml.maxOccurLimit", "maxOccurLimit"), 
/*  62 */     ELEMENT_ATTRIBUTE_LIMIT("jdk.xml.elementAttributeLimit", "elementAttributeLimit");
/*     */ 
/*     */     final String newName;
/*     */     final String oldName;
/*     */ 
/*  68 */     private NameMap(String newName, String oldName) { this.newName = newName;
/*  69 */       this.oldName = oldName; }
/*     */ 
/*     */     String getOldName(String newName)
/*     */     {
/*  73 */       if (newName.equals(this.newName)) {
/*  74 */         return this.oldName;
/*     */       }
/*  76 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.utils.XMLLimitAnalyzer
 * JD-Core Version:    0.6.2
 */
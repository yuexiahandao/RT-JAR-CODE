/*     */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*     */ 
/*     */ public class CaseInsensitiveMap
/*     */ {
/*  30 */   private static int CHUNK_SHIFT = 10;
/*  31 */   private static int CHUNK_SIZE = 1 << CHUNK_SHIFT;
/*  32 */   private static int CHUNK_MASK = CHUNK_SIZE - 1;
/*  33 */   private static int INITIAL_CHUNK_COUNT = 64;
/*     */   private static int[][][] caseInsensitiveMap;
/*  36 */   private static Boolean mapBuilt = Boolean.FALSE;
/*     */ 
/*  38 */   private static int LOWER_CASE_MATCH = 1;
/*  39 */   private static int UPPER_CASE_MATCH = 2;
/*     */ 
/*     */   public static int[] get(int codePoint)
/*     */   {
/*  46 */     if (mapBuilt == Boolean.FALSE) {
/*  47 */       synchronized (mapBuilt) {
/*  48 */         if (mapBuilt == Boolean.FALSE) {
/*  49 */           buildCaseInsensitiveMap();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*  54 */     return codePoint < 65536 ? getMapping(codePoint) : null;
/*     */   }
/*     */ 
/*     */   private static int[] getMapping(int codePoint) {
/*  58 */     int chunk = codePoint >>> CHUNK_SHIFT;
/*  59 */     int offset = codePoint & CHUNK_MASK;
/*     */ 
/*  61 */     return caseInsensitiveMap[chunk][offset];
/*     */   }
/*     */ 
/*     */   private static void buildCaseInsensitiveMap() {
/*  65 */     caseInsensitiveMap = new int[INITIAL_CHUNK_COUNT][][];
/*  66 */     for (int i = 0; i < INITIAL_CHUNK_COUNT; i++) {
/*  67 */       caseInsensitiveMap[i] = new int[CHUNK_SIZE][];
/*     */     }
/*     */ 
/*  71 */     for (int i = 0; i < 65536; i++) {
/*  72 */       int lc = Character.toLowerCase(i);
/*  73 */       int uc = Character.toUpperCase(i);
/*     */ 
/*  76 */       if ((lc != uc) || (lc != i)) {
/*  77 */         int[] map = new int[2];
/*  78 */         int index = 0;
/*     */ 
/*  80 */         if (lc != i) {
/*  81 */           map[(index++)] = lc;
/*  82 */           map[(index++)] = LOWER_CASE_MATCH;
/*  83 */           int[] lcMap = getMapping(lc);
/*  84 */           if (lcMap != null) {
/*  85 */             map = updateMap(i, map, lc, lcMap, LOWER_CASE_MATCH);
/*     */           }
/*     */         }
/*     */ 
/*  89 */         if (uc != i) {
/*  90 */           if (index == map.length) {
/*  91 */             map = expandMap(map, 2);
/*     */           }
/*  93 */           map[(index++)] = uc;
/*  94 */           map[(index++)] = UPPER_CASE_MATCH;
/*  95 */           int[] ucMap = getMapping(uc);
/*  96 */           if (ucMap != null) {
/*  97 */             map = updateMap(i, map, uc, ucMap, UPPER_CASE_MATCH);
/*     */           }
/*     */         }
/*     */ 
/* 101 */         set(i, map);
/*     */       }
/*     */     }
/*     */ 
/* 105 */     mapBuilt = Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   private static int[] expandMap(int[] srcMap, int expandBy) {
/* 109 */     int oldLen = srcMap.length;
/* 110 */     int[] newMap = new int[oldLen + expandBy];
/*     */ 
/* 112 */     System.arraycopy(srcMap, 0, newMap, 0, oldLen);
/* 113 */     return newMap;
/*     */   }
/*     */ 
/*     */   private static void set(int codePoint, int[] map) {
/* 117 */     int chunk = codePoint >>> CHUNK_SHIFT;
/* 118 */     int offset = codePoint & CHUNK_MASK;
/*     */ 
/* 120 */     caseInsensitiveMap[chunk][offset] = map;
/*     */   }
/*     */ 
/*     */   private static int[] updateMap(int codePoint, int[] codePointMap, int ciCodePoint, int[] ciCodePointMap, int matchType)
/*     */   {
/* 125 */     for (int i = 0; i < ciCodePointMap.length; i += 2) {
/* 126 */       int c = ciCodePointMap[i];
/* 127 */       int[] cMap = getMapping(c);
/* 128 */       if ((cMap != null) && 
/* 129 */         (contains(cMap, ciCodePoint, matchType))) {
/* 130 */         if (!contains(cMap, codePoint)) {
/* 131 */           cMap = expandAndAdd(cMap, codePoint, matchType);
/* 132 */           set(c, cMap);
/*     */         }
/* 134 */         if (!contains(codePointMap, c)) {
/* 135 */           codePointMap = expandAndAdd(codePointMap, c, matchType);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     if (!contains(ciCodePointMap, codePoint)) {
/* 142 */       ciCodePointMap = expandAndAdd(ciCodePointMap, codePoint, matchType);
/* 143 */       set(ciCodePoint, ciCodePointMap);
/*     */     }
/*     */ 
/* 146 */     return codePointMap;
/*     */   }
/*     */ 
/*     */   private static boolean contains(int[] map, int codePoint) {
/* 150 */     for (int i = 0; i < map.length; i += 2) {
/* 151 */       if (map[i] == codePoint) {
/* 152 */         return true;
/*     */       }
/*     */     }
/* 155 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean contains(int[] map, int codePoint, int matchType) {
/* 159 */     for (int i = 0; i < map.length; i += 2) {
/* 160 */       if ((map[i] == codePoint) && (map[(i + 1)] == matchType)) {
/* 161 */         return true;
/*     */       }
/*     */     }
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */   private static int[] expandAndAdd(int[] srcMap, int codePoint, int matchType) {
/* 168 */     int oldLen = srcMap.length;
/* 169 */     int[] newMap = new int[oldLen + 2];
/*     */ 
/* 171 */     System.arraycopy(srcMap, 0, newMap, 0, oldLen);
/* 172 */     newMap[oldLen] = codePoint;
/* 173 */     newMap[(oldLen + 1)] = matchType;
/* 174 */     return newMap;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.CaseInsensitiveMap
 * JD-Core Version:    0.6.2
 */
/*     */ package java.io;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ class ExpiringCache
/*     */ {
/*     */   private long millisUntilExpiration;
/*     */   private Map map;
/*     */   private int queryCount;
/*  41 */   private int queryOverflow = 300;
/*  42 */   private int MAX_ENTRIES = 200;
/*     */ 
/*     */   ExpiringCache()
/*     */   {
/*  61 */     this(30000L);
/*     */   }
/*     */ 
/*     */   ExpiringCache(long paramLong) {
/*  65 */     this.millisUntilExpiration = paramLong;
/*  66 */     this.map = new LinkedHashMap() {
/*     */       protected boolean removeEldestEntry(Map.Entry paramAnonymousEntry) {
/*  68 */         return size() > ExpiringCache.this.MAX_ENTRIES;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   synchronized String get(String paramString) {
/*  74 */     if (++this.queryCount >= this.queryOverflow) {
/*  75 */       cleanup();
/*     */     }
/*  77 */     Entry localEntry = entryFor(paramString);
/*  78 */     if (localEntry != null) {
/*  79 */       return localEntry.val();
/*     */     }
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   synchronized void put(String paramString1, String paramString2) {
/*  85 */     if (++this.queryCount >= this.queryOverflow) {
/*  86 */       cleanup();
/*     */     }
/*  88 */     Entry localEntry = entryFor(paramString1);
/*  89 */     if (localEntry != null) {
/*  90 */       localEntry.setTimestamp(System.currentTimeMillis());
/*  91 */       localEntry.setVal(paramString2);
/*     */     } else {
/*  93 */       this.map.put(paramString1, new Entry(System.currentTimeMillis(), paramString2));
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void clear() {
/*  98 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   private Entry entryFor(String paramString) {
/* 102 */     Entry localEntry = (Entry)this.map.get(paramString);
/* 103 */     if (localEntry != null) {
/* 104 */       long l = System.currentTimeMillis() - localEntry.timestamp();
/* 105 */       if ((l < 0L) || (l >= this.millisUntilExpiration)) {
/* 106 */         this.map.remove(paramString);
/* 107 */         localEntry = null;
/*     */       }
/*     */     }
/* 110 */     return localEntry;
/*     */   }
/*     */ 
/*     */   private void cleanup() {
/* 114 */     Set localSet = this.map.keySet();
/*     */ 
/* 116 */     String[] arrayOfString = new String[localSet.size()];
/* 117 */     int i = 0;
/* 118 */     for (Iterator localIterator = localSet.iterator(); localIterator.hasNext(); ) {
/* 119 */       String str = (String)localIterator.next();
/* 120 */       arrayOfString[(i++)] = str;
/*     */     }
/* 122 */     for (int j = 0; j < arrayOfString.length; j++) {
/* 123 */       entryFor(arrayOfString[j]);
/*     */     }
/* 125 */     this.queryCount = 0;
/*     */   }
/*     */ 
/*     */   static class Entry
/*     */   {
/*     */     private long timestamp;
/*     */     private String val;
/*     */ 
/*     */     Entry(long paramLong, String paramString)
/*     */     {
/*  49 */       this.timestamp = paramLong;
/*  50 */       this.val = paramString;
/*     */     }
/*     */     long timestamp() {
/*  53 */       return this.timestamp; } 
/*  54 */     void setTimestamp(long paramLong) { this.timestamp = paramLong; } 
/*     */     String val() {
/*  56 */       return this.val; } 
/*  57 */     void setVal(String paramString) { this.val = paramString; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ExpiringCache
 * JD-Core Version:    0.6.2
 */
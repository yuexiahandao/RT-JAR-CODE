/*     */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ class SymbMap
/*     */   implements Cloneable
/*     */ {
/* 321 */   int free = 23;
/*     */   NameSpaceSymbEntry[] entries;
/*     */   String[] keys;
/*     */ 
/*     */   SymbMap()
/*     */   {
/* 325 */     this.entries = new NameSpaceSymbEntry[this.free];
/* 326 */     this.keys = new String[this.free];
/*     */   }
/*     */   void put(String paramString, NameSpaceSymbEntry paramNameSpaceSymbEntry) {
/* 329 */     int i = index(paramString);
/* 330 */     String str = this.keys[i];
/* 331 */     this.keys[i] = paramString;
/* 332 */     this.entries[i] = paramNameSpaceSymbEntry;
/* 333 */     if (((str == null) || (!str.equals(paramString))) && 
/* 334 */       (--this.free == 0)) {
/* 335 */       this.free = this.entries.length;
/* 336 */       int j = this.free << 2;
/* 337 */       rehash(j);
/*     */     }
/*     */   }
/*     */ 
/*     */   List entrySet()
/*     */   {
/* 343 */     ArrayList localArrayList = new ArrayList();
/* 344 */     for (int i = 0; i < this.entries.length; i++) {
/* 345 */       if ((this.entries[i] != null) && (!"".equals(this.entries[i].uri))) {
/* 346 */         localArrayList.add(this.entries[i]);
/*     */       }
/*     */     }
/* 349 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   protected int index(Object paramObject) {
/* 353 */     String[] arrayOfString = this.keys;
/* 354 */     int i = arrayOfString.length;
/*     */ 
/* 356 */     int j = (paramObject.hashCode() & 0x7FFFFFFF) % i;
/* 357 */     String str = arrayOfString[j];
/*     */ 
/* 359 */     if ((str == null) || (str.equals(paramObject))) {
/* 360 */       return j;
/*     */     }
/* 362 */     i -= 1;
/*     */     do {
/* 364 */       j++; j = j == i ? 0 : j;
/* 365 */       str = arrayOfString[j];
/* 366 */     }while ((str != null) && (!str.equals(paramObject)));
/* 367 */     return j;
/*     */   }
/*     */ 
/*     */   protected void rehash(int paramInt)
/*     */   {
/* 376 */     int i = this.keys.length;
/* 377 */     String[] arrayOfString = this.keys;
/* 378 */     NameSpaceSymbEntry[] arrayOfNameSpaceSymbEntry = this.entries;
/*     */ 
/* 380 */     this.keys = new String[paramInt];
/* 381 */     this.entries = new NameSpaceSymbEntry[paramInt];
/*     */ 
/* 383 */     for (int j = i; j-- > 0; )
/* 384 */       if (arrayOfString[j] != null) {
/* 385 */         String str = arrayOfString[j];
/* 386 */         int k = index(str);
/* 387 */         this.keys[k] = str;
/* 388 */         this.entries[k] = arrayOfNameSpaceSymbEntry[j];
/*     */       }
/*     */   }
/*     */ 
/*     */   NameSpaceSymbEntry get(String paramString)
/*     */   {
/* 394 */     return this.entries[index(paramString)];
/*     */   }
/*     */ 
/*     */   protected Object clone() {
/*     */     try {
/* 399 */       SymbMap localSymbMap = (SymbMap)super.clone();
/* 400 */       localSymbMap.entries = new NameSpaceSymbEntry[this.entries.length];
/* 401 */       System.arraycopy(this.entries, 0, localSymbMap.entries, 0, this.entries.length);
/* 402 */       localSymbMap.keys = new String[this.keys.length];
/* 403 */       System.arraycopy(this.keys, 0, localSymbMap.keys, 0, this.keys.length);
/*     */ 
/* 405 */       return localSymbMap;
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 408 */       localCloneNotSupportedException.printStackTrace();
/*     */     }
/* 410 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.SymbMap
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.corba.se.impl.util;
/*     */ 
/*     */ import java.util.Dictionary;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public final class IdentityHashtable extends Dictionary
/*     */ {
/*     */   private transient IdentityHashtableEntry[] table;
/*     */   private transient int count;
/*     */   private int threshold;
/*     */   private float loadFactor;
/*     */ 
/*     */   public IdentityHashtable(int paramInt, float paramFloat)
/*     */   {
/*  88 */     if ((paramInt <= 0) || (paramFloat <= 0.0D)) {
/*  89 */       throw new IllegalArgumentException();
/*     */     }
/*  91 */     this.loadFactor = paramFloat;
/*  92 */     this.table = new IdentityHashtableEntry[paramInt];
/*  93 */     this.threshold = ((int)(paramInt * paramFloat));
/*     */   }
/*     */ 
/*     */   public IdentityHashtable(int paramInt)
/*     */   {
/* 104 */     this(paramInt, 0.75F);
/*     */   }
/*     */ 
/*     */   public IdentityHashtable()
/*     */   {
/* 114 */     this(101, 0.75F);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 124 */     return this.count;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 135 */     return this.count == 0;
/*     */   }
/*     */ 
/*     */   public Enumeration keys()
/*     */   {
/* 147 */     return new IdentityHashtableEnumerator(this.table, true);
/*     */   }
/*     */ 
/*     */   public Enumeration elements()
/*     */   {
/* 161 */     return new IdentityHashtableEnumerator(this.table, false);
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/* 178 */     if (paramObject == null) {
/* 179 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 182 */     IdentityHashtableEntry[] arrayOfIdentityHashtableEntry = this.table;
/* 183 */     for (int i = arrayOfIdentityHashtableEntry.length; i-- > 0; ) {
/* 184 */       for (IdentityHashtableEntry localIdentityHashtableEntry = arrayOfIdentityHashtableEntry[i]; localIdentityHashtableEntry != null; localIdentityHashtableEntry = localIdentityHashtableEntry.next) {
/* 185 */         if (localIdentityHashtableEntry.value == paramObject) {
/* 186 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/* 203 */     IdentityHashtableEntry[] arrayOfIdentityHashtableEntry = this.table;
/* 204 */     int i = System.identityHashCode(paramObject);
/* 205 */     int j = (i & 0x7FFFFFFF) % arrayOfIdentityHashtableEntry.length;
/* 206 */     for (IdentityHashtableEntry localIdentityHashtableEntry = arrayOfIdentityHashtableEntry[j]; localIdentityHashtableEntry != null; localIdentityHashtableEntry = localIdentityHashtableEntry.next) {
/* 207 */       if ((localIdentityHashtableEntry.hash == i) && (localIdentityHashtableEntry.key == paramObject)) {
/* 208 */         return true;
/*     */       }
/*     */     }
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/* 225 */     IdentityHashtableEntry[] arrayOfIdentityHashtableEntry = this.table;
/* 226 */     int i = System.identityHashCode(paramObject);
/* 227 */     int j = (i & 0x7FFFFFFF) % arrayOfIdentityHashtableEntry.length;
/* 228 */     for (IdentityHashtableEntry localIdentityHashtableEntry = arrayOfIdentityHashtableEntry[j]; localIdentityHashtableEntry != null; localIdentityHashtableEntry = localIdentityHashtableEntry.next) {
/* 229 */       if ((localIdentityHashtableEntry.hash == i) && (localIdentityHashtableEntry.key == paramObject)) {
/* 230 */         return localIdentityHashtableEntry.value;
/*     */       }
/*     */     }
/* 233 */     return null;
/*     */   }
/*     */ 
/*     */   protected void rehash()
/*     */   {
/* 245 */     int i = this.table.length;
/* 246 */     IdentityHashtableEntry[] arrayOfIdentityHashtableEntry1 = this.table;
/*     */ 
/* 248 */     int j = i * 2 + 1;
/* 249 */     IdentityHashtableEntry[] arrayOfIdentityHashtableEntry2 = new IdentityHashtableEntry[j];
/*     */ 
/* 251 */     this.threshold = ((int)(j * this.loadFactor));
/* 252 */     this.table = arrayOfIdentityHashtableEntry2;
/*     */ 
/* 256 */     for (int k = i; k-- > 0; )
/* 257 */       for (localIdentityHashtableEntry1 = arrayOfIdentityHashtableEntry1[k]; localIdentityHashtableEntry1 != null; ) {
/* 258 */         IdentityHashtableEntry localIdentityHashtableEntry2 = localIdentityHashtableEntry1;
/* 259 */         localIdentityHashtableEntry1 = localIdentityHashtableEntry1.next;
/*     */ 
/* 261 */         int m = (localIdentityHashtableEntry2.hash & 0x7FFFFFFF) % j;
/* 262 */         localIdentityHashtableEntry2.next = arrayOfIdentityHashtableEntry2[m];
/* 263 */         arrayOfIdentityHashtableEntry2[m] = localIdentityHashtableEntry2;
/*     */       }
/*     */     IdentityHashtableEntry localIdentityHashtableEntry1;
/*     */   }
/*     */ 
/*     */   public Object put(Object paramObject1, Object paramObject2)
/*     */   {
/* 287 */     if (paramObject2 == null) {
/* 288 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 292 */     IdentityHashtableEntry[] arrayOfIdentityHashtableEntry = this.table;
/* 293 */     int i = System.identityHashCode(paramObject1);
/* 294 */     int j = (i & 0x7FFFFFFF) % arrayOfIdentityHashtableEntry.length;
/* 295 */     for (IdentityHashtableEntry localIdentityHashtableEntry = arrayOfIdentityHashtableEntry[j]; localIdentityHashtableEntry != null; localIdentityHashtableEntry = localIdentityHashtableEntry.next) {
/* 296 */       if ((localIdentityHashtableEntry.hash == i) && (localIdentityHashtableEntry.key == paramObject1)) {
/* 297 */         Object localObject = localIdentityHashtableEntry.value;
/* 298 */         localIdentityHashtableEntry.value = paramObject2;
/* 299 */         return localObject;
/*     */       }
/*     */     }
/*     */ 
/* 303 */     if (this.count >= this.threshold)
/*     */     {
/* 305 */       rehash();
/* 306 */       return put(paramObject1, paramObject2);
/*     */     }
/*     */ 
/* 310 */     localIdentityHashtableEntry = new IdentityHashtableEntry();
/* 311 */     localIdentityHashtableEntry.hash = i;
/* 312 */     localIdentityHashtableEntry.key = paramObject1;
/* 313 */     localIdentityHashtableEntry.value = paramObject2;
/* 314 */     localIdentityHashtableEntry.next = arrayOfIdentityHashtableEntry[j];
/* 315 */     arrayOfIdentityHashtableEntry[j] = localIdentityHashtableEntry;
/* 316 */     this.count += 1;
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 330 */     IdentityHashtableEntry[] arrayOfIdentityHashtableEntry = this.table;
/* 331 */     int i = System.identityHashCode(paramObject);
/* 332 */     int j = (i & 0x7FFFFFFF) % arrayOfIdentityHashtableEntry.length;
/* 333 */     IdentityHashtableEntry localIdentityHashtableEntry1 = arrayOfIdentityHashtableEntry[j]; for (IdentityHashtableEntry localIdentityHashtableEntry2 = null; localIdentityHashtableEntry1 != null; localIdentityHashtableEntry1 = localIdentityHashtableEntry1.next) {
/* 334 */       if ((localIdentityHashtableEntry1.hash == i) && (localIdentityHashtableEntry1.key == paramObject)) {
/* 335 */         if (localIdentityHashtableEntry2 != null)
/* 336 */           localIdentityHashtableEntry2.next = localIdentityHashtableEntry1.next;
/*     */         else {
/* 338 */           arrayOfIdentityHashtableEntry[j] = localIdentityHashtableEntry1.next;
/*     */         }
/* 340 */         this.count -= 1;
/* 341 */         return localIdentityHashtableEntry1.value;
/*     */       }
/* 333 */       localIdentityHashtableEntry2 = localIdentityHashtableEntry1;
/*     */     }
/*     */ 
/* 344 */     return null;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 353 */     IdentityHashtableEntry[] arrayOfIdentityHashtableEntry = this.table;
/* 354 */     int i = arrayOfIdentityHashtableEntry.length;
/*     */     while (true) { i--; if (i < 0) break;
/* 355 */       arrayOfIdentityHashtableEntry[i] = null; }
/* 356 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 366 */     int i = size() - 1;
/* 367 */     StringBuffer localStringBuffer = new StringBuffer();
/* 368 */     Enumeration localEnumeration1 = keys();
/* 369 */     Enumeration localEnumeration2 = elements();
/* 370 */     localStringBuffer.append("{");
/*     */ 
/* 372 */     for (int j = 0; j <= i; j++) {
/* 373 */       String str1 = localEnumeration1.nextElement().toString();
/* 374 */       String str2 = localEnumeration2.nextElement().toString();
/* 375 */       localStringBuffer.append(str1 + "=" + str2);
/* 376 */       if (j < i) {
/* 377 */         localStringBuffer.append(", ");
/*     */       }
/*     */     }
/* 380 */     localStringBuffer.append("}");
/* 381 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.IdentityHashtable
 * JD-Core Version:    0.6.2
 */
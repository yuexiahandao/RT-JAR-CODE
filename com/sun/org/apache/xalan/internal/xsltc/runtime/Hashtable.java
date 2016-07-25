/*     */ package com.sun.org.apache.xalan.internal.xsltc.runtime;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class Hashtable
/*     */ {
/*     */   private transient HashtableEntry[] table;
/*     */   private transient int count;
/*     */   private int threshold;
/*     */   private float loadFactor;
/*     */ 
/*     */   public Hashtable(int initialCapacity, float loadFactor)
/*     */   {
/*  70 */     if (initialCapacity <= 0) initialCapacity = 11;
/*  71 */     if (loadFactor <= 0.0D) loadFactor = 0.75F;
/*  72 */     this.loadFactor = loadFactor;
/*  73 */     this.table = new HashtableEntry[initialCapacity];
/*  74 */     this.threshold = ((int)(initialCapacity * loadFactor));
/*     */   }
/*     */ 
/*     */   public Hashtable(int initialCapacity)
/*     */   {
/*  82 */     this(initialCapacity, 0.75F);
/*     */   }
/*     */ 
/*     */   public Hashtable()
/*     */   {
/*  90 */     this(101, 0.75F);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  97 */     return this.count;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 104 */     return this.count == 0;
/*     */   }
/*     */ 
/*     */   public Enumeration keys()
/*     */   {
/* 111 */     return new HashtableEnumerator(this.table, true);
/*     */   }
/*     */ 
/*     */   public Enumeration elements()
/*     */   {
/* 120 */     return new HashtableEnumerator(this.table, false);
/*     */   }
/*     */ 
/*     */   public boolean contains(Object value)
/*     */   {
/* 130 */     if (value == null) throw new NullPointerException();
/*     */ 
/* 134 */     HashtableEntry[] tab = this.table;
/*     */ 
/* 136 */     for (int i = tab.length; i-- > 0; ) {
/* 137 */       for (HashtableEntry e = tab[i]; e != null; e = e.next) {
/* 138 */         if (e.value.equals(value)) {
/* 139 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object key)
/*     */   {
/* 151 */     HashtableEntry[] tab = this.table;
/* 152 */     int hash = key.hashCode();
/* 153 */     int index = (hash & 0x7FFFFFFF) % tab.length;
/*     */ 
/* 155 */     for (HashtableEntry e = tab[index]; e != null; e = e.next) {
/* 156 */       if ((e.hash == hash) && (e.key.equals(key)))
/* 157 */         return true;
/*     */     }
/* 159 */     return false;
/*     */   }
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 167 */     HashtableEntry[] tab = this.table;
/* 168 */     int hash = key.hashCode();
/* 169 */     int index = (hash & 0x7FFFFFFF) % tab.length;
/*     */ 
/* 171 */     for (HashtableEntry e = tab[index]; e != null; e = e.next) {
/* 172 */       if ((e.hash == hash) && (e.key.equals(key)))
/* 173 */         return e.value;
/*     */     }
/* 175 */     return null;
/*     */   }
/*     */ 
/*     */   protected void rehash()
/*     */   {
/* 187 */     int oldCapacity = this.table.length;
/* 188 */     HashtableEntry[] oldTable = this.table;
/*     */ 
/* 190 */     int newCapacity = oldCapacity * 2 + 1;
/* 191 */     HashtableEntry[] newTable = new HashtableEntry[newCapacity];
/*     */ 
/* 193 */     this.threshold = ((int)(newCapacity * this.loadFactor));
/* 194 */     this.table = newTable;
/*     */ 
/* 196 */     for (int i = oldCapacity; i-- > 0; )
/* 197 */       for (old = oldTable[i]; old != null; ) {
/* 198 */         HashtableEntry e = old;
/* 199 */         old = old.next;
/* 200 */         int index = (e.hash & 0x7FFFFFFF) % newCapacity;
/* 201 */         e.next = newTable[index];
/* 202 */         newTable[index] = e;
/*     */       }
/*     */     HashtableEntry old;
/*     */   }
/*     */ 
/*     */   public Object put(Object key, Object value)
/*     */   {
/* 217 */     if (value == null) throw new NullPointerException();
/*     */ 
/* 221 */     HashtableEntry[] tab = this.table;
/* 222 */     int hash = key.hashCode();
/* 223 */     int index = (hash & 0x7FFFFFFF) % tab.length;
/*     */ 
/* 225 */     for (HashtableEntry e = tab[index]; e != null; e = e.next) {
/* 226 */       if ((e.hash == hash) && (e.key.equals(key))) {
/* 227 */         Object old = e.value;
/* 228 */         e.value = value;
/* 229 */         return old;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 234 */     if (this.count >= this.threshold) {
/* 235 */       rehash();
/* 236 */       return put(key, value);
/*     */     }
/*     */ 
/* 240 */     e = new HashtableEntry();
/* 241 */     e.hash = hash;
/* 242 */     e.key = key;
/* 243 */     e.value = value;
/* 244 */     e.next = tab[index];
/* 245 */     tab[index] = e;
/* 246 */     this.count += 1;
/* 247 */     return null;
/*     */   }
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 256 */     HashtableEntry[] tab = this.table;
/* 257 */     int hash = key.hashCode();
/* 258 */     int index = (hash & 0x7FFFFFFF) % tab.length;
/* 259 */     HashtableEntry e = tab[index]; for (HashtableEntry prev = null; e != null; e = e.next) {
/* 260 */       if ((e.hash == hash) && (e.key.equals(key))) {
/* 261 */         if (prev != null)
/* 262 */           prev.next = e.next;
/*     */         else
/* 264 */           tab[index] = e.next;
/* 265 */         this.count -= 1;
/* 266 */         return e.value;
/*     */       }
/* 259 */       prev = e;
/*     */     }
/*     */ 
/* 269 */     return null;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 276 */     HashtableEntry[] tab = this.table;
/* 277 */     int index = tab.length;
/*     */     while (true) { index--; if (index < 0) break;
/* 278 */       tab[index] = null; }
/* 279 */     this.count = 0;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 288 */     int max = size() - 1;
/* 289 */     StringBuffer buf = new StringBuffer();
/* 290 */     Enumeration k = keys();
/* 291 */     Enumeration e = elements();
/* 292 */     buf.append("{");
/*     */ 
/* 294 */     for (int i = 0; i <= max; i++) {
/* 295 */       String s1 = k.nextElement().toString();
/* 296 */       String s2 = e.nextElement().toString();
/* 297 */       buf.append(s1).append('=').append(s2);
/* 298 */       if (i < max) buf.append(", ");
/*     */     }
/* 300 */     buf.append("}");
/* 301 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   class HashtableEnumerator implements Enumeration
/*     */   {
/*     */     boolean keys;
/*     */     int index;
/*     */     HashtableEntry[] table;
/*     */     HashtableEntry entry;
/*     */ 
/*     */     HashtableEnumerator(HashtableEntry[] table, boolean keys) {
/* 315 */       this.table = table;
/* 316 */       this.keys = keys;
/* 317 */       this.index = table.length;
/*     */     }
/*     */ 
/*     */     public boolean hasMoreElements() {
/* 321 */       if (this.entry != null) {
/* 322 */         return true;
/*     */       }
/* 324 */       while (this.index-- > 0) {
/* 325 */         if ((this.entry = this.table[this.index]) != null) {
/* 326 */           return true;
/*     */         }
/*     */       }
/* 329 */       return false;
/*     */     }
/*     */ 
/*     */     public Object nextElement() {
/* 333 */       while ((this.entry == null) && 
/* 334 */         (this.index-- > 0) && ((this.entry = this.table[this.index]) == null));
/* 336 */       if (this.entry != null) {
/* 337 */         HashtableEntry e = this.entry;
/* 338 */         this.entry = e.next;
/* 339 */         return this.keys ? e.key : e.value;
/*     */       }
/* 341 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable
 * JD-Core Version:    0.6.2
 */
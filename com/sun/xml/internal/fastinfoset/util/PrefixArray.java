/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class PrefixArray extends ValueArray
/*     */ {
/*     */   public static final int PREFIX_MAP_SIZE = 64;
/*     */   private int _initialCapacity;
/*     */   public String[] _array;
/*     */   private PrefixArray _readOnlyArray;
/*  50 */   private PrefixEntry[] _prefixMap = new PrefixEntry[64];
/*     */   private PrefixEntry _prefixPool;
/*     */   private NamespaceEntry _namespacePool;
/*     */   private NamespaceEntry[] _inScopeNamespaces;
/*     */   public int[] _currentInScope;
/*     */   public int _declarationId;
/*     */ 
/*     */   public PrefixArray(int initialCapacity, int maximumCapacity)
/*     */   {
/*  73 */     this._initialCapacity = initialCapacity;
/*  74 */     this._maximumCapacity = maximumCapacity;
/*     */ 
/*  76 */     this._array = new String[initialCapacity];
/*     */ 
/*  80 */     this._inScopeNamespaces = new NamespaceEntry[initialCapacity + 2];
/*  81 */     this._currentInScope = new int[initialCapacity + 2];
/*     */ 
/*  83 */     increaseNamespacePool(initialCapacity);
/*  84 */     increasePrefixPool(initialCapacity);
/*     */ 
/*  86 */     initializeEntries();
/*     */   }
/*     */ 
/*     */   public PrefixArray() {
/*  90 */     this(10, 2147483647);
/*     */   }
/*     */ 
/*     */   private final void initializeEntries() {
/*  94 */     this._inScopeNamespaces[0] = this._namespacePool;
/*  95 */     this._namespacePool = this._namespacePool.next;
/*  96 */     this._inScopeNamespaces[0].next = null;
/*  97 */     this._inScopeNamespaces[0].prefix = "";
/*  98 */     this._inScopeNamespaces[0].namespaceName = "";
/*  99 */     this._inScopeNamespaces[0].namespaceIndex = (this._currentInScope[0] = 0);
/*     */ 
/* 101 */     int index = KeyIntMap.indexFor(KeyIntMap.hashHash(this._inScopeNamespaces[0].prefix.hashCode()), this._prefixMap.length);
/* 102 */     this._prefixMap[index] = this._prefixPool;
/* 103 */     this._prefixPool = this._prefixPool.next;
/* 104 */     this._prefixMap[index].next = null;
/* 105 */     this._prefixMap[index].prefixId = 0;
/*     */ 
/* 108 */     this._inScopeNamespaces[1] = this._namespacePool;
/* 109 */     this._namespacePool = this._namespacePool.next;
/* 110 */     this._inScopeNamespaces[1].next = null;
/* 111 */     this._inScopeNamespaces[1].prefix = "xml";
/* 112 */     this._inScopeNamespaces[1].namespaceName = "http://www.w3.org/XML/1998/namespace";
/* 113 */     this._inScopeNamespaces[1].namespaceIndex = (this._currentInScope[1] = 1);
/*     */ 
/* 115 */     index = KeyIntMap.indexFor(KeyIntMap.hashHash(this._inScopeNamespaces[1].prefix.hashCode()), this._prefixMap.length);
/* 116 */     if (this._prefixMap[index] == null) {
/* 117 */       this._prefixMap[index] = this._prefixPool;
/* 118 */       this._prefixPool = this._prefixPool.next;
/* 119 */       this._prefixMap[index].next = null;
/*     */     } else {
/* 121 */       PrefixEntry e = this._prefixMap[index];
/* 122 */       this._prefixMap[index] = this._prefixPool;
/* 123 */       this._prefixPool = this._prefixPool.next;
/* 124 */       this._prefixMap[index].next = e;
/*     */     }
/* 126 */     this._prefixMap[index].prefixId = 1;
/*     */   }
/*     */ 
/*     */   private final void increaseNamespacePool(int capacity) {
/* 130 */     if (this._namespacePool == null) {
/* 131 */       this._namespacePool = new NamespaceEntry(null);
/*     */     }
/*     */ 
/* 134 */     for (int i = 0; i < capacity; i++) {
/* 135 */       NamespaceEntry ne = new NamespaceEntry(null);
/* 136 */       ne.next = this._namespacePool;
/* 137 */       this._namespacePool = ne;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void increasePrefixPool(int capacity) {
/* 142 */     if (this._prefixPool == null) {
/* 143 */       this._prefixPool = new PrefixEntry(null);
/*     */     }
/*     */ 
/* 146 */     for (int i = 0; i < capacity; i++) {
/* 147 */       PrefixEntry pe = new PrefixEntry(null);
/* 148 */       pe.next = this._prefixPool;
/* 149 */       this._prefixPool = pe;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int countNamespacePool() {
/* 154 */     int i = 0;
/* 155 */     NamespaceEntry e = this._namespacePool;
/* 156 */     while (e != null) {
/* 157 */       i++;
/* 158 */       e = e.next;
/*     */     }
/* 160 */     return i;
/*     */   }
/*     */ 
/*     */   public int countPrefixPool() {
/* 164 */     int i = 0;
/* 165 */     PrefixEntry e = this._prefixPool;
/* 166 */     while (e != null) {
/* 167 */       i++;
/* 168 */       e = e.next;
/*     */     }
/* 170 */     return i;
/*     */   }
/*     */ 
/*     */   public final void clear() {
/* 174 */     for (int i = this._readOnlyArraySize; i < this._size; i++) {
/* 175 */       this._array[i] = null;
/*     */     }
/* 177 */     this._size = this._readOnlyArraySize;
/*     */   }
/*     */ 
/*     */   public final void clearCompletely() {
/* 181 */     this._prefixPool = null;
/* 182 */     this._namespacePool = null;
/*     */ 
/* 184 */     for (int i = 0; i < this._size + 2; i++) {
/* 185 */       this._currentInScope[i] = 0;
/* 186 */       this._inScopeNamespaces[i] = null;
/*     */     }
/*     */ 
/* 189 */     for (int i = 0; i < this._prefixMap.length; i++) {
/* 190 */       this._prefixMap[i] = null;
/*     */     }
/*     */ 
/* 193 */     increaseNamespacePool(this._initialCapacity);
/* 194 */     increasePrefixPool(this._initialCapacity);
/*     */ 
/* 196 */     initializeEntries();
/*     */ 
/* 198 */     this._declarationId = 0;
/*     */ 
/* 200 */     clear();
/*     */   }
/*     */ 
/*     */   public final String[] getArray() {
/* 204 */     return this._array;
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
/* 208 */     if (!(readOnlyArray instanceof PrefixArray)) {
/* 209 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[] { readOnlyArray }));
/*     */     }
/*     */ 
/* 213 */     setReadOnlyArray((PrefixArray)readOnlyArray, clear);
/*     */   }
/*     */ 
/*     */   public final void setReadOnlyArray(PrefixArray readOnlyArray, boolean clear) {
/* 217 */     if (readOnlyArray != null) {
/* 218 */       this._readOnlyArray = readOnlyArray;
/* 219 */       this._readOnlyArraySize = readOnlyArray.getSize();
/*     */ 
/* 221 */       clearCompletely();
/*     */ 
/* 224 */       this._inScopeNamespaces = new NamespaceEntry[this._readOnlyArraySize + this._inScopeNamespaces.length];
/* 225 */       this._currentInScope = new int[this._readOnlyArraySize + this._currentInScope.length];
/*     */ 
/* 227 */       initializeEntries();
/*     */ 
/* 229 */       if (clear) {
/* 230 */         clear();
/*     */       }
/*     */ 
/* 233 */       this._array = getCompleteArray();
/* 234 */       this._size = this._readOnlyArraySize;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String[] getCompleteArray() {
/* 239 */     if (this._readOnlyArray == null) {
/* 240 */       return this._array;
/*     */     }
/* 242 */     String[] ra = this._readOnlyArray.getCompleteArray();
/* 243 */     String[] a = new String[this._readOnlyArraySize + this._array.length];
/* 244 */     System.arraycopy(ra, 0, a, 0, this._readOnlyArraySize);
/* 245 */     return a;
/*     */   }
/*     */ 
/*     */   public final String get(int i)
/*     */   {
/* 250 */     return this._array[i];
/*     */   }
/*     */ 
/*     */   public final int add(String s) {
/* 254 */     if (this._size == this._array.length) {
/* 255 */       resize();
/*     */     }
/*     */ 
/* 258 */     this._array[(this._size++)] = s;
/* 259 */     return this._size;
/*     */   }
/*     */ 
/*     */   protected final void resize() {
/* 263 */     if (this._size == this._maximumCapacity) {
/* 264 */       throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
/*     */     }
/*     */ 
/* 267 */     int newSize = this._size * 3 / 2 + 1;
/* 268 */     if (newSize > this._maximumCapacity) {
/* 269 */       newSize = this._maximumCapacity;
/*     */     }
/*     */ 
/* 272 */     String[] newArray = new String[newSize];
/* 273 */     System.arraycopy(this._array, 0, newArray, 0, this._size);
/* 274 */     this._array = newArray;
/*     */ 
/* 276 */     newSize += 2;
/* 277 */     NamespaceEntry[] newInScopeNamespaces = new NamespaceEntry[newSize];
/* 278 */     System.arraycopy(this._inScopeNamespaces, 0, newInScopeNamespaces, 0, this._inScopeNamespaces.length);
/* 279 */     this._inScopeNamespaces = newInScopeNamespaces;
/*     */ 
/* 281 */     int[] newCurrentInScope = new int[newSize];
/* 282 */     System.arraycopy(this._currentInScope, 0, newCurrentInScope, 0, this._currentInScope.length);
/* 283 */     this._currentInScope = newCurrentInScope;
/*     */   }
/*     */ 
/*     */   public final void clearDeclarationIds() {
/* 287 */     for (int i = 0; i < this._size; i++) {
/* 288 */       NamespaceEntry e = this._inScopeNamespaces[i];
/* 289 */       if (e != null) {
/* 290 */         e.declarationId = 0;
/*     */       }
/*     */     }
/*     */ 
/* 294 */     this._declarationId = 1;
/*     */   }
/*     */ 
/*     */   public final void pushScope(int prefixIndex, int namespaceIndex) throws FastInfosetException {
/* 298 */     if (this._namespacePool == null) {
/* 299 */       increaseNamespacePool(16);
/*     */     }
/*     */ 
/* 302 */     NamespaceEntry e = this._namespacePool;
/* 303 */     this._namespacePool = e.next;
/*     */ 
/* 305 */     NamespaceEntry current = this._inScopeNamespaces[(++prefixIndex)];
/* 306 */     if (current == null) {
/* 307 */       e.declarationId = this._declarationId;
/* 308 */       e.namespaceIndex = (this._currentInScope[prefixIndex] = ++namespaceIndex);
/* 309 */       e.next = null;
/*     */ 
/* 311 */       this._inScopeNamespaces[prefixIndex] = e;
/* 312 */     } else if (current.declarationId < this._declarationId) {
/* 313 */       e.declarationId = this._declarationId;
/* 314 */       e.namespaceIndex = (this._currentInScope[prefixIndex] = ++namespaceIndex);
/* 315 */       e.next = current;
/*     */ 
/* 317 */       current.declarationId = 0;
/* 318 */       this._inScopeNamespaces[prefixIndex] = e;
/*     */     } else {
/* 320 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.duplicateNamespaceAttribute"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void pushScopeWithPrefixEntry(String prefix, String namespaceName, int prefixIndex, int namespaceIndex) throws FastInfosetException
/*     */   {
/* 326 */     if (this._namespacePool == null) {
/* 327 */       increaseNamespacePool(16);
/*     */     }
/* 329 */     if (this._prefixPool == null) {
/* 330 */       increasePrefixPool(16);
/*     */     }
/*     */ 
/* 333 */     NamespaceEntry e = this._namespacePool;
/* 334 */     this._namespacePool = e.next;
/*     */ 
/* 336 */     NamespaceEntry current = this._inScopeNamespaces[(++prefixIndex)];
/* 337 */     if (current == null) {
/* 338 */       e.declarationId = this._declarationId;
/* 339 */       e.namespaceIndex = (this._currentInScope[prefixIndex] = ++namespaceIndex);
/* 340 */       e.next = null;
/*     */ 
/* 342 */       this._inScopeNamespaces[prefixIndex] = e;
/* 343 */     } else if (current.declarationId < this._declarationId) {
/* 344 */       e.declarationId = this._declarationId;
/* 345 */       e.namespaceIndex = (this._currentInScope[prefixIndex] = ++namespaceIndex);
/* 346 */       e.next = current;
/*     */ 
/* 348 */       current.declarationId = 0;
/* 349 */       this._inScopeNamespaces[prefixIndex] = e;
/*     */     } else {
/* 351 */       throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.duplicateNamespaceAttribute"));
/*     */     }
/*     */ 
/* 354 */     PrefixEntry p = this._prefixPool;
/* 355 */     this._prefixPool = this._prefixPool.next;
/* 356 */     p.prefixId = prefixIndex;
/*     */ 
/* 358 */     e.prefix = prefix;
/* 359 */     e.namespaceName = namespaceName;
/* 360 */     e.prefixEntryIndex = KeyIntMap.indexFor(KeyIntMap.hashHash(prefix.hashCode()), this._prefixMap.length);
/*     */ 
/* 362 */     PrefixEntry pCurrent = this._prefixMap[e.prefixEntryIndex];
/* 363 */     p.next = pCurrent;
/* 364 */     this._prefixMap[e.prefixEntryIndex] = p;
/*     */   }
/*     */ 
/*     */   public final void popScope(int prefixIndex) {
/* 368 */     NamespaceEntry e = this._inScopeNamespaces[(++prefixIndex)];
/* 369 */     this._inScopeNamespaces[prefixIndex] = e.next;
/* 370 */     this._currentInScope[prefixIndex] = (e.next != null ? NamespaceEntry.access$000(e).namespaceIndex : 0);
/*     */ 
/* 372 */     e.next = this._namespacePool;
/* 373 */     this._namespacePool = e;
/*     */   }
/*     */ 
/*     */   public final void popScopeWithPrefixEntry(int prefixIndex) {
/* 377 */     NamespaceEntry e = this._inScopeNamespaces[(++prefixIndex)];
/*     */ 
/* 379 */     this._inScopeNamespaces[prefixIndex] = e.next;
/* 380 */     this._currentInScope[prefixIndex] = (e.next != null ? NamespaceEntry.access$000(e).namespaceIndex : 0);
/*     */ 
/* 382 */     e.prefix = NamespaceEntry.access$202(e, null);
/* 383 */     e.next = this._namespacePool;
/* 384 */     this._namespacePool = e;
/*     */ 
/* 386 */     PrefixEntry current = this._prefixMap[e.prefixEntryIndex];
/* 387 */     if (current.prefixId == prefixIndex) {
/* 388 */       this._prefixMap[e.prefixEntryIndex] = current.next;
/* 389 */       current.next = this._prefixPool;
/* 390 */       this._prefixPool = current;
/*     */     } else {
/* 392 */       PrefixEntry prev = current;
/* 393 */       current = current.next;
/* 394 */       while (current != null) {
/* 395 */         if (current.prefixId == prefixIndex) {
/* 396 */           prev.next = current.next;
/* 397 */           current.next = this._prefixPool;
/* 398 */           this._prefixPool = current;
/* 399 */           break;
/*     */         }
/* 401 */         prev = current;
/* 402 */         current = current.next;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String getNamespaceFromPrefix(String prefix) {
/* 408 */     int index = KeyIntMap.indexFor(KeyIntMap.hashHash(prefix.hashCode()), this._prefixMap.length);
/* 409 */     PrefixEntry pe = this._prefixMap[index];
/* 410 */     while (pe != null) {
/* 411 */       NamespaceEntry ne = this._inScopeNamespaces[pe.prefixId];
/* 412 */       if ((prefix == ne.prefix) || (prefix.equals(ne.prefix))) {
/* 413 */         return ne.namespaceName;
/*     */       }
/* 415 */       pe = pe.next;
/*     */     }
/*     */ 
/* 418 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getPrefixFromNamespace(String namespaceName) {
/* 422 */     int position = 0;
/*     */     while (true) { position++; if (position >= this._size + 2) break;
/* 424 */       NamespaceEntry ne = this._inScopeNamespaces[position];
/* 425 */       if ((ne != null) && (namespaceName.equals(ne.namespaceName))) {
/* 426 */         return ne.prefix;
/*     */       }
/*     */     }
/*     */ 
/* 430 */     return null;
/*     */   }
/*     */ 
/*     */   public final Iterator getPrefixes() {
/* 434 */     return new Iterator() {
/* 435 */       int _position = 1;
/* 436 */       PrefixArray.NamespaceEntry _ne = PrefixArray.this._inScopeNamespaces[this._position];
/*     */ 
/*     */       public boolean hasNext() {
/* 439 */         return this._ne != null;
/*     */       }
/*     */ 
/*     */       public Object next() {
/* 443 */         if (this._position == PrefixArray.this._size + 2) {
/* 444 */           throw new NoSuchElementException();
/*     */         }
/*     */ 
/* 447 */         String prefix = PrefixArray.NamespaceEntry.access$100(this._ne);
/* 448 */         moveToNext();
/* 449 */         return prefix;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 453 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */       private final void moveToNext() {
/* 457 */         while (++this._position < PrefixArray.this._size + 2) {
/* 458 */           this._ne = PrefixArray.this._inScopeNamespaces[this._position];
/* 459 */           if (this._ne != null) {
/* 460 */             return;
/*     */           }
/*     */         }
/* 463 */         this._ne = null;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public final Iterator getPrefixesFromNamespace(final String namespaceName)
/*     */   {
/* 470 */     return new Iterator()
/*     */     {
/*     */       String _namespaceName;
/*     */       int _position;
/*     */       PrefixArray.NamespaceEntry _ne;
/*     */ 
/*     */       public boolean hasNext()
/*     */       {
/* 480 */         return this._ne != null;
/*     */       }
/*     */ 
/*     */       public Object next() {
/* 484 */         if (this._position == PrefixArray.this._size + 2) {
/* 485 */           throw new NoSuchElementException();
/*     */         }
/*     */ 
/* 488 */         String prefix = PrefixArray.NamespaceEntry.access$100(this._ne);
/* 489 */         moveToNext();
/* 490 */         return prefix;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 494 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */       private final void moveToNext() {
/* 498 */         while (++this._position < PrefixArray.this._size + 2) {
/* 499 */           this._ne = PrefixArray.this._inScopeNamespaces[this._position];
/* 500 */           if ((this._ne != null) && (this._namespaceName.equals(PrefixArray.NamespaceEntry.access$200(this._ne)))) {
/* 501 */             return;
/*     */           }
/*     */         }
/* 504 */         this._ne = null;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private static class NamespaceEntry
/*     */   {
/*     */     private NamespaceEntry next;
/*     */     private int declarationId;
/*     */     private int namespaceIndex;
/*     */     private String prefix;
/*     */     private String namespaceName;
/*     */     private int prefixEntryIndex;
/*     */   }
/*     */ 
/*     */   private static class PrefixEntry
/*     */   {
/*     */     private PrefixEntry next;
/*     */     private int prefixId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.PrefixArray
 * JD-Core Version:    0.6.2
 */
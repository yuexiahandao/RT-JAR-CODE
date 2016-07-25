/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ final class Fixups extends AbstractCollection
/*     */ {
/*     */   byte[] bytes;
/*     */   int head;
/*     */   int tail;
/*     */   int size;
/*     */   ConstantPool.Entry[] entries;
/*     */   int[] bigDescs;
/*     */   private static final int MINBIGSIZE = 1;
/*  80 */   private static int[] noBigDescs = { 1 };
/*     */   static final int LOC_SHIFT = 1;
/*     */   static final int FMT_MASK = 1;
/*     */   static final byte UNUSED_BYTE = 0;
/*     */   static final byte OVERFLOW_BYTE = -1;
/*     */   static final int BIGSIZE = 0;
/*     */   public static final int U2_FORMAT = 0;
/*     */   public static final int U1_FORMAT = 1;
/*     */   private static final int SPECIAL_LOC = 0;
/*     */   private static final int SPECIAL_FMT = 0;
/*     */ 
/*     */   Fixups(byte[] paramArrayOfByte)
/*     */   {
/*  61 */     this.bytes = paramArrayOfByte;
/*  62 */     this.entries = new ConstantPool.Entry[3];
/*  63 */     this.bigDescs = noBigDescs;
/*     */   }
/*     */ 
/*     */   Fixups() {
/*  67 */     this((byte[])null);
/*     */   }
/*     */   Fixups(byte[] paramArrayOfByte, Collection paramCollection) {
/*  70 */     this(paramArrayOfByte);
/*  71 */     addAll(paramCollection);
/*     */   }
/*     */   Fixups(Collection paramCollection) {
/*  74 */     this((byte[])null);
/*  75 */     addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  83 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void trimToSize() {
/*  87 */     if (this.size != this.entries.length) {
/*  88 */       ConstantPool.Entry[] arrayOfEntry = this.entries;
/*  89 */       this.entries = new ConstantPool.Entry[this.size];
/*  90 */       System.arraycopy(arrayOfEntry, 0, this.entries, 0, this.size);
/*     */     }
/*  92 */     int i = this.bigDescs[0];
/*  93 */     if (i == 1) {
/*  94 */       this.bigDescs = noBigDescs;
/*  95 */     } else if (i != this.bigDescs.length) {
/*  96 */       int[] arrayOfInt = this.bigDescs;
/*  97 */       this.bigDescs = new int[i];
/*  98 */       System.arraycopy(arrayOfInt, 0, this.bigDescs, 0, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void visitRefs(Collection<ConstantPool.Entry> paramCollection) {
/* 103 */     for (int i = 0; i < this.size; i++)
/* 104 */       paramCollection.add(this.entries[i]);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/*     */     Iterator localIterator;
/* 109 */     if (this.bytes != null)
/*     */     {
/* 111 */       for (localIterator = iterator(); localIterator.hasNext(); ) {
/* 112 */         Fixup localFixup = (Fixup)localIterator.next();
/*     */ 
/* 114 */         storeIndex(localFixup.location(), localFixup.format(), 0);
/*     */       }
/*     */     }
/* 117 */     this.size = 0;
/* 118 */     if (this.bigDescs != noBigDescs)
/* 119 */       this.bigDescs[0] = 1;
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 124 */     return this.bytes;
/*     */   }
/*     */ 
/*     */   public void setBytes(byte[] paramArrayOfByte)
/*     */   {
/* 129 */     if (this.bytes == paramArrayOfByte) return;
/* 130 */     ArrayList localArrayList1 = null;
/* 131 */     assert ((localArrayList1 = new ArrayList(this)) != null);
/* 132 */     if ((this.bytes == null) || (paramArrayOfByte == null))
/*     */     {
/* 135 */       ArrayList localArrayList2 = new ArrayList(this);
/* 136 */       clear();
/* 137 */       this.bytes = paramArrayOfByte;
/* 138 */       addAll(localArrayList2);
/*     */     }
/*     */     else {
/* 141 */       this.bytes = paramArrayOfByte;
/*     */     }
/* 143 */     assert (localArrayList1.equals(new ArrayList(this)));
/*     */   }
/*     */ 
/*     */   static int fmtLen(int paramInt)
/*     */   {
/* 161 */     return 1 + (paramInt - 1) / -1; } 
/* 162 */   static int descLoc(int paramInt) { return paramInt >>> 1; } 
/* 163 */   static int descFmt(int paramInt) { return paramInt & 0x1; } 
/* 164 */   static int descEnd(int paramInt) { return descLoc(paramInt) + fmtLen(descFmt(paramInt)); } 
/*     */   static int makeDesc(int paramInt1, int paramInt2) {
/* 166 */     int i = paramInt1 << 1 | paramInt2;
/* 167 */     assert (descLoc(i) == paramInt1);
/* 168 */     assert (descFmt(i) == paramInt2);
/* 169 */     return i;
/*     */   }
/*     */   int fetchDesc(int paramInt1, int paramInt2) {
/* 172 */     int i = this.bytes[paramInt1];
/* 173 */     assert (i != -1);
/*     */     int j;
/* 175 */     if (paramInt2 == 0) {
/* 176 */       int k = this.bytes[(paramInt1 + 1)];
/* 177 */       j = ((i & 0xFF) << 8) + (k & 0xFF);
/*     */     } else {
/* 179 */       j = i & 0xFF;
/*     */     }
/*     */ 
/* 182 */     return j + (paramInt1 << 1);
/*     */   }
/*     */   boolean storeDesc(int paramInt1, int paramInt2, int paramInt3) {
/* 185 */     if (this.bytes == null)
/* 186 */       return false;
/* 187 */     int i = paramInt3 - (paramInt1 << 1);
/*     */     int j;
/* 189 */     switch (paramInt2) {
/*     */     case 0:
/* 191 */       assert (this.bytes[(paramInt1 + 0)] == 0);
/* 192 */       assert (this.bytes[(paramInt1 + 1)] == 0);
/* 193 */       j = (byte)(i >> 8);
/* 194 */       int k = (byte)(i >> 0);
/* 195 */       if ((i == (i & 0xFFFF)) && (j != -1)) {
/* 196 */         this.bytes[(paramInt1 + 0)] = j;
/* 197 */         this.bytes[(paramInt1 + 1)] = k;
/* 198 */         assert (fetchDesc(paramInt1, paramInt2) == paramInt3);
/* 199 */         return true;
/*     */       }
/*     */       break;
/*     */     case 1:
/* 203 */       assert (this.bytes[paramInt1] == 0);
/* 204 */       j = (byte)i;
/* 205 */       if ((i == (i & 0xFF)) && (j != -1)) {
/* 206 */         this.bytes[paramInt1] = j;
/* 207 */         assert (fetchDesc(paramInt1, paramInt2) == paramInt3);
/* 208 */         return true;
/*     */       }break;
/*     */     default:
/* 211 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */       break;
/*     */     }
/* 214 */     this.bytes[paramInt1] = -1;
/* 215 */     assert ((paramInt2 == 1) || ((this.bytes[(paramInt1 + 1)] = (byte)this.bigDescs[0]) != 999));
/* 216 */     return false;
/*     */   }
/*     */   void storeIndex(int paramInt1, int paramInt2, int paramInt3) {
/* 219 */     storeIndex(this.bytes, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   static void storeIndex(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3) {
/* 223 */     switch (paramInt2) {
/*     */     case 0:
/* 225 */       assert (paramInt3 == (paramInt3 & 0xFFFF)) : paramInt3;
/* 226 */       paramArrayOfByte[(paramInt1 + 0)] = ((byte)(paramInt3 >> 8));
/* 227 */       paramArrayOfByte[(paramInt1 + 1)] = ((byte)(paramInt3 >> 0));
/* 228 */       break;
/*     */     case 1:
/* 230 */       assert (paramInt3 == (paramInt3 & 0xFF)) : paramInt3;
/* 231 */       paramArrayOfByte[paramInt1] = ((byte)paramInt3);
/* 232 */       break;
/*     */     default:
/* 233 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Iterator iterator()
/*     */   {
/* 302 */     return new Itr(null);
/*     */   }
/*     */   public void add(int paramInt1, int paramInt2, ConstantPool.Entry paramEntry) {
/* 305 */     addDesc(makeDesc(paramInt1, paramInt2), paramEntry);
/*     */   }
/*     */   public boolean add(Fixup paramFixup) {
/* 308 */     addDesc(paramFixup.desc, paramFixup.entry);
/* 309 */     return true;
/*     */   }
/*     */   public boolean add(Object paramObject) {
/* 312 */     return add((Fixup)paramObject);
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection paramCollection) {
/* 316 */     if ((paramCollection instanceof Fixups))
/*     */     {
/* 318 */       Fixups localFixups = (Fixups)paramCollection;
/* 319 */       if (localFixups.size == 0) return false;
/* 320 */       if ((this.size == 0) && (this.entries.length < localFixups.size))
/* 321 */         growEntries(localFixups.size);
/* 322 */       ConstantPool.Entry[] arrayOfEntry = localFixups.entries;
/*     */       Fixups tmp58_57 = localFixups; tmp58_57.getClass(); for (Itr localItr = new Itr(null); localItr.hasNext(); ) {
/* 324 */         int i = localItr.index;
/* 325 */         addDesc(localItr.nextDesc(), arrayOfEntry[i]);
/*     */       }
/* 327 */       return true;
/*     */     }
/* 329 */     return super.addAll(paramCollection);
/*     */   }
/*     */ 
/*     */   private void addDesc(int paramInt, ConstantPool.Entry paramEntry)
/*     */   {
/* 334 */     if (this.entries.length == this.size)
/* 335 */       growEntries(this.size * 2);
/* 336 */     this.entries[this.size] = paramEntry;
/* 337 */     if (this.size == 0) {
/* 338 */       this.head = (this.tail = paramInt);
/*     */     } else {
/* 340 */       int i = this.tail;
/*     */ 
/* 342 */       int j = descLoc(i);
/* 343 */       int k = descFmt(i);
/* 344 */       int m = fmtLen(k);
/* 345 */       int n = descLoc(paramInt);
/*     */ 
/* 347 */       if (n < j + m)
/* 348 */         badOverlap(n);
/* 349 */       this.tail = paramInt;
/* 350 */       if (!storeDesc(j, k, paramInt))
/*     */       {
/* 352 */         int i1 = this.bigDescs[0];
/* 353 */         if (this.bigDescs.length == i1) {
/* 354 */           growBigDescs();
/*     */         }
/* 356 */         this.bigDescs[(i1++)] = paramInt;
/* 357 */         this.bigDescs[0] = i1;
/*     */       }
/*     */     }
/* 360 */     this.size += 1;
/*     */   }
/*     */   private void badOverlap(int paramInt) {
/* 363 */     throw new IllegalArgumentException("locs must be ascending and must not overlap:  " + paramInt + " >> " + this);
/*     */   }
/*     */ 
/*     */   private void growEntries(int paramInt) {
/* 367 */     ConstantPool.Entry[] arrayOfEntry = this.entries;
/* 368 */     this.entries = new ConstantPool.Entry[Math.max(3, paramInt)];
/* 369 */     System.arraycopy(arrayOfEntry, 0, this.entries, 0, arrayOfEntry.length);
/*     */   }
/*     */   private void growBigDescs() {
/* 372 */     int[] arrayOfInt = this.bigDescs;
/* 373 */     this.bigDescs = new int[arrayOfInt.length * 2];
/* 374 */     System.arraycopy(arrayOfInt, 0, this.bigDescs, 0, arrayOfInt.length);
/*     */   }
/*     */ 
/*     */   public static Object add(Object paramObject, byte[] paramArrayOfByte, int paramInt1, int paramInt2, ConstantPool.Entry paramEntry)
/*     */   {
/*     */     Fixups localFixups;
/* 383 */     if (paramObject == null) {
/* 384 */       if ((paramInt1 == 0) && (paramInt2 == 0))
/*     */       {
/* 388 */         return paramEntry;
/*     */       }
/* 390 */       localFixups = new Fixups(paramArrayOfByte);
/* 391 */     } else if (!(paramObject instanceof Fixups))
/*     */     {
/* 393 */       ConstantPool.Entry localEntry = (ConstantPool.Entry)paramObject;
/* 394 */       localFixups = new Fixups(paramArrayOfByte);
/* 395 */       localFixups.add(0, 0, localEntry);
/*     */     } else {
/* 397 */       localFixups = (Fixups)paramObject;
/* 398 */       assert (localFixups.bytes == paramArrayOfByte);
/*     */     }
/* 400 */     localFixups.add(paramInt1, paramInt2, paramEntry);
/* 401 */     return localFixups;
/*     */   }
/*     */ 
/*     */   public static void setBytes(Object paramObject, byte[] paramArrayOfByte)
/*     */   {
/* 406 */     if ((paramObject instanceof Fixups)) {
/* 407 */       Fixups localFixups = (Fixups)paramObject;
/* 408 */       localFixups.setBytes(paramArrayOfByte);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object trimToSize(Object paramObject)
/*     */   {
/* 414 */     if ((paramObject instanceof Fixups)) {
/* 415 */       Fixups localFixups = (Fixups)paramObject;
/* 416 */       localFixups.trimToSize();
/* 417 */       if (localFixups.size() == 0)
/* 418 */         paramObject = null;
/*     */     }
/* 420 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public static void visitRefs(Object paramObject, Collection<ConstantPool.Entry> paramCollection)
/*     */   {
/* 426 */     if (paramObject != null)
/* 427 */       if (!(paramObject instanceof Fixups))
/*     */       {
/* 429 */         paramCollection.add((ConstantPool.Entry)paramObject);
/*     */       } else {
/* 431 */         Fixups localFixups = (Fixups)paramObject;
/* 432 */         localFixups.visitRefs(paramCollection);
/*     */       }
/*     */   }
/*     */ 
/*     */   public static void finishRefs(Object paramObject, byte[] paramArrayOfByte, ConstantPool.Index paramIndex)
/*     */   {
/* 440 */     if (paramObject == null)
/* 441 */       return;
/* 442 */     if (!(paramObject instanceof Fixups))
/*     */     {
/* 444 */       int i = paramIndex.indexOf((ConstantPool.Entry)paramObject);
/* 445 */       storeIndex(paramArrayOfByte, 0, 0, i);
/* 446 */       return;
/*     */     }
/* 448 */     Fixups localFixups = (Fixups)paramObject;
/* 449 */     assert (localFixups.bytes == paramArrayOfByte);
/* 450 */     localFixups.finishRefs(paramIndex);
/*     */   }
/*     */ 
/*     */   void finishRefs(ConstantPool.Index paramIndex) {
/* 454 */     if (isEmpty())
/* 455 */       return;
/* 456 */     for (Iterator localIterator = iterator(); localIterator.hasNext(); ) {
/* 457 */       Fixup localFixup = (Fixup)localIterator.next();
/* 458 */       int i = paramIndex.indexOf(localFixup.entry);
/*     */ 
/* 462 */       storeIndex(localFixup.location(), localFixup.format(), i);
/*     */     }
/*     */ 
/* 465 */     this.bytes = null;
/* 466 */     clear();
/*     */   }
/*     */ 
/*     */   public static class Fixup
/*     */     implements Comparable
/*     */   {
/*     */     int desc;
/*     */     ConstantPool.Entry entry;
/*     */ 
/*     */     Fixup(int paramInt, ConstantPool.Entry paramEntry)
/*     */     {
/* 243 */       this.desc = paramInt;
/* 244 */       this.entry = paramEntry;
/*     */     }
/*     */     public Fixup(int paramInt1, int paramInt2, ConstantPool.Entry paramEntry) {
/* 247 */       this.desc = Fixups.makeDesc(paramInt1, paramInt2);
/* 248 */       this.entry = paramEntry;
/*     */     }
/* 250 */     public int location() { return Fixups.descLoc(this.desc); } 
/* 251 */     public int format() { return Fixups.descFmt(this.desc); } 
/* 252 */     public ConstantPool.Entry entry() { return this.entry; }
/*     */ 
/*     */     public int compareTo(Fixup paramFixup) {
/* 255 */       return location() - paramFixup.location();
/*     */     }
/*     */     public int compareTo(Object paramObject) {
/* 258 */       return compareTo((Fixup)paramObject);
/*     */     }
/*     */     public boolean equals(Object paramObject) {
/* 261 */       if (!(paramObject instanceof Fixup)) return false;
/* 262 */       Fixup localFixup = (Fixup)paramObject;
/* 263 */       return (this.desc == localFixup.desc) && (this.entry == localFixup.entry);
/*     */     }
/*     */     public String toString() {
/* 266 */       return "@" + location() + (format() == 1 ? ".1" : "") + "=" + this.entry;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class Itr implements Iterator
/*     */   {
/* 272 */     int index = 0;
/* 273 */     int bigIndex = 1;
/* 274 */     int next = Fixups.this.head;
/*     */ 
/*     */     private Itr() {  } 
/* 275 */     public boolean hasNext() { return this.index < Fixups.this.size; } 
/* 276 */     public void remove() { throw new UnsupportedOperationException(); } 
/*     */     public Object next() {
/* 278 */       int i = this.index;
/* 279 */       return new Fixups.Fixup(nextDesc(), Fixups.this.entries[i]);
/*     */     }
/*     */     int nextDesc() {
/* 282 */       this.index += 1;
/* 283 */       int i = this.next;
/* 284 */       if (this.index < Fixups.this.size)
/*     */       {
/* 286 */         int j = Fixups.descLoc(i);
/* 287 */         int k = Fixups.descFmt(i);
/* 288 */         if ((Fixups.this.bytes != null) && (Fixups.this.bytes[j] != -1)) {
/* 289 */           this.next = Fixups.this.fetchDesc(j, k);
/*     */         }
/*     */         else
/*     */         {
/* 293 */           assert ((k == 1) || (Fixups.this.bytes == null) || (Fixups.this.bytes[(j + 1)] == (byte)this.bigIndex));
/* 294 */           this.next = Fixups.this.bigDescs[(this.bigIndex++)];
/*     */         }
/*     */       }
/* 297 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Fixups
 * JD-Core Version:    0.6.2
 */
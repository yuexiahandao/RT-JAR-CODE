/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class SuballocatedIntVector
/*     */ {
/*     */   protected int m_blocksize;
/*     */   protected int m_SHIFT;
/*     */   protected int m_MASK;
/*     */   protected static final int NUMBLOCKS_DEFAULT = 32;
/*  57 */   protected int m_numblocks = 32;
/*     */   protected int[][] m_map;
/*  63 */   protected int m_firstFree = 0;
/*     */   protected int[] m_map0;
/*     */   protected int[] m_buildCache;
/*     */   protected int m_buildCacheStartIndex;
/*     */ 
/*     */   public SuballocatedIntVector()
/*     */   {
/*  83 */     this(2048);
/*     */   }
/*     */ 
/*     */   public SuballocatedIntVector(int blocksize, int numblocks)
/*     */   {
/*  97 */     for (this.m_SHIFT = 0; 0 != blocksize >>>= 1; this.m_SHIFT += 1);
/*  99 */     this.m_blocksize = (1 << this.m_SHIFT);
/* 100 */     this.m_MASK = (this.m_blocksize - 1);
/* 101 */     this.m_numblocks = numblocks;
/*     */ 
/* 103 */     this.m_map0 = new int[this.m_blocksize];
/* 104 */     this.m_map = new int[numblocks][];
/* 105 */     this.m_map[0] = this.m_map0;
/* 106 */     this.m_buildCache = this.m_map0;
/* 107 */     this.m_buildCacheStartIndex = 0;
/*     */   }
/*     */ 
/*     */   public SuballocatedIntVector(int blocksize)
/*     */   {
/* 117 */     this(blocksize, 32);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 127 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public void setSize(int sz)
/*     */   {
/* 138 */     if (this.m_firstFree > sz)
/* 139 */       this.m_firstFree = sz;
/*     */   }
/*     */ 
/*     */   public void addElement(int value)
/*     */   {
/* 149 */     int indexRelativeToCache = this.m_firstFree - this.m_buildCacheStartIndex;
/*     */ 
/* 152 */     if ((indexRelativeToCache >= 0) && (indexRelativeToCache < this.m_blocksize)) {
/* 153 */       this.m_buildCache[indexRelativeToCache] = value;
/* 154 */       this.m_firstFree += 1;
/*     */     }
/*     */     else
/*     */     {
/* 163 */       int index = this.m_firstFree >>> this.m_SHIFT;
/* 164 */       int offset = this.m_firstFree & this.m_MASK;
/*     */ 
/* 166 */       if (index >= this.m_map.length)
/*     */       {
/* 168 */         int newsize = index + this.m_numblocks;
/* 169 */         int[][] newMap = new int[newsize][];
/* 170 */         System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
/* 171 */         this.m_map = newMap;
/*     */       }
/* 173 */       int[] block = this.m_map[index];
/* 174 */       if (null == block)
/* 175 */         block = this.m_map[index] =  = new int[this.m_blocksize];
/* 176 */       block[offset] = value;
/*     */ 
/* 180 */       this.m_buildCache = block;
/* 181 */       this.m_buildCacheStartIndex = (this.m_firstFree - offset);
/*     */ 
/* 183 */       this.m_firstFree += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addElements(int value, int numberOfElements)
/*     */   {
/* 194 */     if (this.m_firstFree + numberOfElements < this.m_blocksize) {
/* 195 */       for (int i = 0; i < numberOfElements; i++)
/*     */       {
/* 197 */         this.m_map0[(this.m_firstFree++)] = value;
/*     */       }
/*     */     }
/*     */     else {
/* 201 */       int index = this.m_firstFree >>> this.m_SHIFT;
/* 202 */       int offset = this.m_firstFree & this.m_MASK;
/* 203 */       this.m_firstFree += numberOfElements;
/*     */ 
/* 222 */       for (; numberOfElements > 0; 
/* 222 */         offset = 0)
/*     */       {
/* 206 */         if (index >= this.m_map.length)
/*     */         {
/* 208 */           int newsize = index + this.m_numblocks;
/* 209 */           int[][] newMap = new int[newsize][];
/* 210 */           System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
/* 211 */           this.m_map = newMap;
/*     */         }
/* 213 */         int[] block = this.m_map[index];
/* 214 */         if (null == block)
/* 215 */           block = this.m_map[index] =  = new int[this.m_blocksize];
/* 216 */         int copied = this.m_blocksize - offset < numberOfElements ? this.m_blocksize - offset : numberOfElements;
/*     */ 
/* 218 */         numberOfElements -= copied;
/* 219 */         while (copied-- > 0) {
/* 220 */           block[(offset++)] = value;
/*     */         }
/* 222 */         index++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addElements(int numberOfElements)
/*     */   {
/* 235 */     int newlen = this.m_firstFree + numberOfElements;
/* 236 */     if (newlen > this.m_blocksize)
/*     */     {
/* 238 */       int index = this.m_firstFree >>> this.m_SHIFT;
/* 239 */       int newindex = this.m_firstFree + numberOfElements >>> this.m_SHIFT;
/* 240 */       for (int i = index + 1; i <= newindex; i++)
/* 241 */         this.m_map[i] = new int[this.m_blocksize];
/*     */     }
/* 243 */     this.m_firstFree = newlen;
/*     */   }
/*     */ 
/*     */   private void insertElementAt(int value, int at)
/*     */   {
/* 259 */     if (at == this.m_firstFree) {
/* 260 */       addElement(value);
/* 261 */     } else if (at > this.m_firstFree)
/*     */     {
/* 263 */       int index = at >>> this.m_SHIFT;
/* 264 */       if (index >= this.m_map.length)
/*     */       {
/* 266 */         int newsize = index + this.m_numblocks;
/* 267 */         int[][] newMap = new int[newsize][];
/* 268 */         System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
/* 269 */         this.m_map = newMap;
/*     */       }
/* 271 */       int[] block = this.m_map[index];
/* 272 */       if (null == block)
/* 273 */         block = this.m_map[index] =  = new int[this.m_blocksize];
/* 274 */       int offset = at & this.m_MASK;
/* 275 */       block[offset] = value;
/* 276 */       this.m_firstFree = (offset + 1);
/*     */     }
/*     */     else
/*     */     {
/* 280 */       int index = at >>> this.m_SHIFT;
/* 281 */       int maxindex = this.m_firstFree >>> this.m_SHIFT;
/* 282 */       this.m_firstFree += 1;
/* 283 */       int offset = at & this.m_MASK;
/*     */ 
/* 287 */       while (index <= maxindex)
/*     */       {
/* 289 */         int copylen = this.m_blocksize - offset - 1;
/* 290 */         int[] block = this.m_map[index];
/*     */         int push;
/* 291 */         if (null == block)
/*     */         {
/* 293 */           int push = 0;
/* 294 */           block = this.m_map[index] =  = new int[this.m_blocksize];
/*     */         }
/*     */         else
/*     */         {
/* 298 */           push = block[(this.m_blocksize - 1)];
/* 299 */           System.arraycopy(block, offset, block, offset + 1, copylen);
/*     */         }
/* 301 */         block[offset] = value;
/* 302 */         value = push;
/* 303 */         offset = 0;
/* 304 */         index++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAllElements()
/*     */   {
/* 314 */     this.m_firstFree = 0;
/* 315 */     this.m_buildCache = this.m_map0;
/* 316 */     this.m_buildCacheStartIndex = 0;
/*     */   }
/*     */ 
/*     */   private boolean removeElement(int s)
/*     */   {
/* 332 */     int at = indexOf(s, 0);
/* 333 */     if (at < 0)
/* 334 */       return false;
/* 335 */     removeElementAt(at);
/* 336 */     return true;
/*     */   }
/*     */ 
/*     */   private void removeElementAt(int at)
/*     */   {
/* 350 */     if (at < this.m_firstFree)
/*     */     {
/* 352 */       int index = at >>> this.m_SHIFT;
/* 353 */       int maxindex = this.m_firstFree >>> this.m_SHIFT;
/* 354 */       int offset = at & this.m_MASK;
/*     */ 
/* 356 */       while (index <= maxindex)
/*     */       {
/* 358 */         int copylen = this.m_blocksize - offset - 1;
/* 359 */         int[] block = this.m_map[index];
/* 360 */         if (null == block)
/* 361 */           block = this.m_map[index] =  = new int[this.m_blocksize];
/*     */         else
/* 363 */           System.arraycopy(block, offset + 1, block, offset, copylen);
/* 364 */         if (index < maxindex)
/*     */         {
/* 366 */           int[] next = this.m_map[(index + 1)];
/* 367 */           if (next != null)
/* 368 */             block[(this.m_blocksize - 1)] = (next != null ? next[0] : 0);
/*     */         }
/*     */         else {
/* 371 */           block[(this.m_blocksize - 1)] = 0;
/* 372 */         }offset = 0;
/* 373 */         index++;
/*     */       }
/*     */     }
/* 376 */     this.m_firstFree -= 1;
/*     */   }
/*     */ 
/*     */   public void setElementAt(int value, int at)
/*     */   {
/* 391 */     if (at < this.m_blocksize) {
/* 392 */       this.m_map0[at] = value;
/*     */     }
/*     */     else {
/* 395 */       int index = at >>> this.m_SHIFT;
/* 396 */       int offset = at & this.m_MASK;
/*     */ 
/* 398 */       if (index >= this.m_map.length)
/*     */       {
/* 400 */         int newsize = index + this.m_numblocks;
/* 401 */         int[][] newMap = new int[newsize][];
/* 402 */         System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
/* 403 */         this.m_map = newMap;
/*     */       }
/*     */ 
/* 406 */       int[] block = this.m_map[index];
/* 407 */       if (null == block)
/* 408 */         block = this.m_map[index] =  = new int[this.m_blocksize];
/* 409 */       block[offset] = value;
/*     */     }
/*     */ 
/* 412 */     if (at >= this.m_firstFree)
/* 413 */       this.m_firstFree = (at + 1);
/*     */   }
/*     */ 
/*     */   public int elementAt(int i)
/*     */   {
/* 441 */     if (i < this.m_blocksize) {
/* 442 */       return this.m_map0[i];
/*     */     }
/* 444 */     return this.m_map[(i >>> this.m_SHIFT)][(i & this.m_MASK)];
/*     */   }
/*     */ 
/*     */   private boolean contains(int s)
/*     */   {
/* 456 */     return indexOf(s, 0) >= 0;
/*     */   }
/*     */ 
/*     */   public int indexOf(int elem, int index)
/*     */   {
/* 472 */     if (index >= this.m_firstFree) {
/* 473 */       return -1;
/*     */     }
/* 475 */     int bindex = index >>> this.m_SHIFT;
/* 476 */     int boffset = index & this.m_MASK;
/* 477 */     int maxindex = this.m_firstFree >>> this.m_SHIFT;
/*     */ 
/* 480 */     for (; bindex < maxindex; bindex++)
/*     */     {
/* 482 */       int[] block = this.m_map[bindex];
/* 483 */       if (block != null)
/* 484 */         for (int offset = boffset; offset < this.m_blocksize; offset++)
/* 485 */           if (block[offset] == elem)
/* 486 */             return offset + bindex * this.m_blocksize;
/* 487 */       boffset = 0;
/*     */     }
/*     */ 
/* 490 */     int maxoffset = this.m_firstFree & this.m_MASK;
/* 491 */     int[] block = this.m_map[maxindex];
/* 492 */     for (int offset = boffset; offset < maxoffset; offset++) {
/* 493 */       if (block[offset] == elem)
/* 494 */         return offset + maxindex * this.m_blocksize;
/*     */     }
/* 496 */     return -1;
/*     */   }
/*     */ 
/*     */   public int indexOf(int elem)
/*     */   {
/* 511 */     return indexOf(elem, 0);
/*     */   }
/*     */ 
/*     */   private int lastIndexOf(int elem)
/*     */   {
/* 526 */     int boffset = this.m_firstFree & this.m_MASK;
/* 527 */     for (int index = this.m_firstFree >>> this.m_SHIFT; 
/* 528 */       index >= 0; 
/* 529 */       index--)
/*     */     {
/* 531 */       int[] block = this.m_map[index];
/* 532 */       if (block != null)
/* 533 */         for (int offset = boffset; offset >= 0; offset--)
/* 534 */           if (block[offset] == elem)
/* 535 */             return offset + index * this.m_blocksize;
/* 536 */       boffset = 0;
/*     */     }
/* 538 */     return -1;
/*     */   }
/*     */ 
/*     */   public final int[] getMap0()
/*     */   {
/* 547 */     return this.m_map0;
/*     */   }
/*     */ 
/*     */   public final int[][] getMap()
/*     */   {
/* 556 */     return this.m_map;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.SuballocatedIntVector
 * JD-Core Version:    0.6.2
 */
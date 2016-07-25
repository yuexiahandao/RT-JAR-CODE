/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class SuballocatedByteVector
/*     */ {
/*     */   protected int m_blocksize;
/*  52 */   protected int m_numblocks = 32;
/*     */   protected byte[][] m_map;
/*  58 */   protected int m_firstFree = 0;
/*     */   protected byte[] m_map0;
/*     */ 
/*     */   public SuballocatedByteVector()
/*     */   {
/*  69 */     this(2048);
/*     */   }
/*     */ 
/*     */   public SuballocatedByteVector(int blocksize)
/*     */   {
/*  79 */     this.m_blocksize = blocksize;
/*  80 */     this.m_map0 = new byte[blocksize];
/*  81 */     this.m_map = new byte[this.m_numblocks][];
/*  82 */     this.m_map[0] = this.m_map0;
/*     */   }
/*     */ 
/*     */   public SuballocatedByteVector(int blocksize, int increaseSize)
/*     */   {
/*  93 */     this(blocksize);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 104 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   private void setSize(int sz)
/*     */   {
/* 114 */     if (this.m_firstFree < sz)
/* 115 */       this.m_firstFree = sz;
/*     */   }
/*     */ 
/*     */   public void addElement(byte value)
/*     */   {
/* 125 */     if (this.m_firstFree < this.m_blocksize) {
/* 126 */       this.m_map0[(this.m_firstFree++)] = value;
/*     */     }
/*     */     else {
/* 129 */       int index = this.m_firstFree / this.m_blocksize;
/* 130 */       int offset = this.m_firstFree % this.m_blocksize;
/* 131 */       this.m_firstFree += 1;
/*     */ 
/* 133 */       if (index >= this.m_map.length)
/*     */       {
/* 135 */         int newsize = index + this.m_numblocks;
/* 136 */         byte[][] newMap = new byte[newsize][];
/* 137 */         System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
/* 138 */         this.m_map = newMap;
/*     */       }
/* 140 */       byte[] block = this.m_map[index];
/* 141 */       if (null == block)
/* 142 */         block = this.m_map[index] =  = new byte[this.m_blocksize];
/* 143 */       block[offset] = value;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addElements(byte value, int numberOfElements)
/*     */   {
/* 154 */     if (this.m_firstFree + numberOfElements < this.m_blocksize) {
/* 155 */       for (int i = 0; i < numberOfElements; i++)
/*     */       {
/* 157 */         this.m_map0[(this.m_firstFree++)] = value;
/*     */       }
/*     */     }
/*     */     else {
/* 161 */       int index = this.m_firstFree / this.m_blocksize;
/* 162 */       int offset = this.m_firstFree % this.m_blocksize;
/* 163 */       this.m_firstFree += numberOfElements;
/*     */ 
/* 182 */       for (; numberOfElements > 0; 
/* 182 */         offset = 0)
/*     */       {
/* 166 */         if (index >= this.m_map.length)
/*     */         {
/* 168 */           int newsize = index + this.m_numblocks;
/* 169 */           byte[][] newMap = new byte[newsize][];
/* 170 */           System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
/* 171 */           this.m_map = newMap;
/*     */         }
/* 173 */         byte[] block = this.m_map[index];
/* 174 */         if (null == block)
/* 175 */           block = this.m_map[index] =  = new byte[this.m_blocksize];
/* 176 */         int copied = this.m_blocksize - offset < numberOfElements ? this.m_blocksize - offset : numberOfElements;
/*     */ 
/* 178 */         numberOfElements -= copied;
/* 179 */         while (copied-- > 0) {
/* 180 */           block[(offset++)] = value;
/*     */         }
/* 182 */         index++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addElements(int numberOfElements)
/*     */   {
/* 195 */     int newlen = this.m_firstFree + numberOfElements;
/* 196 */     if (newlen > this.m_blocksize)
/*     */     {
/* 198 */       int index = this.m_firstFree % this.m_blocksize;
/* 199 */       int newindex = (this.m_firstFree + numberOfElements) % this.m_blocksize;
/* 200 */       for (int i = index + 1; i <= newindex; i++)
/* 201 */         this.m_map[i] = new byte[this.m_blocksize];
/*     */     }
/* 203 */     this.m_firstFree = newlen;
/*     */   }
/*     */ 
/*     */   private void insertElementAt(byte value, int at)
/*     */   {
/* 219 */     if (at == this.m_firstFree) {
/* 220 */       addElement(value);
/* 221 */     } else if (at > this.m_firstFree)
/*     */     {
/* 223 */       int index = at / this.m_blocksize;
/* 224 */       if (index >= this.m_map.length)
/*     */       {
/* 226 */         int newsize = index + this.m_numblocks;
/* 227 */         byte[][] newMap = new byte[newsize][];
/* 228 */         System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
/* 229 */         this.m_map = newMap;
/*     */       }
/* 231 */       byte[] block = this.m_map[index];
/* 232 */       if (null == block)
/* 233 */         block = this.m_map[index] =  = new byte[this.m_blocksize];
/* 234 */       int offset = at % this.m_blocksize;
/* 235 */       block[offset] = value;
/* 236 */       this.m_firstFree = (offset + 1);
/*     */     }
/*     */     else
/*     */     {
/* 240 */       int index = at / this.m_blocksize;
/* 241 */       int maxindex = this.m_firstFree + 1 / this.m_blocksize;
/* 242 */       this.m_firstFree += 1;
/* 243 */       int offset = at % this.m_blocksize;
/*     */ 
/* 247 */       while (index <= maxindex)
/*     */       {
/* 249 */         int copylen = this.m_blocksize - offset - 1;
/* 250 */         byte[] block = this.m_map[index];
/*     */         byte push;
/* 251 */         if (null == block)
/*     */         {
/* 253 */           byte push = 0;
/* 254 */           block = this.m_map[index] =  = new byte[this.m_blocksize];
/*     */         }
/*     */         else
/*     */         {
/* 258 */           push = block[(this.m_blocksize - 1)];
/* 259 */           System.arraycopy(block, offset, block, offset + 1, copylen);
/*     */         }
/* 261 */         block[offset] = value;
/* 262 */         value = push;
/* 263 */         offset = 0;
/* 264 */         index++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeAllElements()
/*     */   {
/* 274 */     this.m_firstFree = 0;
/*     */   }
/*     */ 
/*     */   private boolean removeElement(byte s)
/*     */   {
/* 290 */     int at = indexOf(s, 0);
/* 291 */     if (at < 0)
/* 292 */       return false;
/* 293 */     removeElementAt(at);
/* 294 */     return true;
/*     */   }
/*     */ 
/*     */   private void removeElementAt(int at)
/*     */   {
/* 308 */     if (at < this.m_firstFree)
/*     */     {
/* 310 */       int index = at / this.m_blocksize;
/* 311 */       int maxindex = this.m_firstFree / this.m_blocksize;
/* 312 */       int offset = at % this.m_blocksize;
/*     */ 
/* 314 */       while (index <= maxindex)
/*     */       {
/* 316 */         int copylen = this.m_blocksize - offset - 1;
/* 317 */         byte[] block = this.m_map[index];
/* 318 */         if (null == block)
/* 319 */           block = this.m_map[index] =  = new byte[this.m_blocksize];
/*     */         else
/* 321 */           System.arraycopy(block, offset + 1, block, offset, copylen);
/* 322 */         if (index < maxindex)
/*     */         {
/* 324 */           byte[] next = this.m_map[(index + 1)];
/* 325 */           if (next != null)
/* 326 */             block[(this.m_blocksize - 1)] = (next != null ? next[0] : 0);
/*     */         }
/*     */         else {
/* 329 */           block[(this.m_blocksize - 1)] = 0;
/* 330 */         }offset = 0;
/* 331 */         index++;
/*     */       }
/*     */     }
/* 334 */     this.m_firstFree -= 1;
/*     */   }
/*     */ 
/*     */   public void setElementAt(byte value, int at)
/*     */   {
/* 349 */     if (at < this.m_blocksize)
/*     */     {
/* 351 */       this.m_map0[at] = value;
/* 352 */       return;
/*     */     }
/*     */ 
/* 355 */     int index = at / this.m_blocksize;
/* 356 */     int offset = at % this.m_blocksize;
/*     */ 
/* 358 */     if (index >= this.m_map.length)
/*     */     {
/* 360 */       int newsize = index + this.m_numblocks;
/* 361 */       byte[][] newMap = new byte[newsize][];
/* 362 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_map.length);
/* 363 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 366 */     byte[] block = this.m_map[index];
/* 367 */     if (null == block)
/* 368 */       block = this.m_map[index] =  = new byte[this.m_blocksize];
/* 369 */     block[offset] = value;
/*     */ 
/* 371 */     if (at >= this.m_firstFree)
/* 372 */       this.m_firstFree = (at + 1);
/*     */   }
/*     */ 
/*     */   public byte elementAt(int i)
/*     */   {
/* 400 */     if (i < this.m_blocksize) {
/* 401 */       return this.m_map0[i];
/*     */     }
/* 403 */     return this.m_map[(i / this.m_blocksize)][(i % this.m_blocksize)];
/*     */   }
/*     */ 
/*     */   private boolean contains(byte s)
/*     */   {
/* 415 */     return indexOf(s, 0) >= 0;
/*     */   }
/*     */ 
/*     */   public int indexOf(byte elem, int index)
/*     */   {
/* 431 */     if (index >= this.m_firstFree) {
/* 432 */       return -1;
/*     */     }
/* 434 */     int bindex = index / this.m_blocksize;
/* 435 */     int boffset = index % this.m_blocksize;
/* 436 */     int maxindex = this.m_firstFree / this.m_blocksize;
/*     */ 
/* 439 */     for (; bindex < maxindex; bindex++)
/*     */     {
/* 441 */       byte[] block = this.m_map[bindex];
/* 442 */       if (block != null)
/* 443 */         for (int offset = boffset; offset < this.m_blocksize; offset++)
/* 444 */           if (block[offset] == elem)
/* 445 */             return offset + bindex * this.m_blocksize;
/* 446 */       boffset = 0;
/*     */     }
/*     */ 
/* 449 */     int maxoffset = this.m_firstFree % this.m_blocksize;
/* 450 */     byte[] block = this.m_map[maxindex];
/* 451 */     for (int offset = boffset; offset < maxoffset; offset++) {
/* 452 */       if (block[offset] == elem)
/* 453 */         return offset + maxindex * this.m_blocksize;
/*     */     }
/* 455 */     return -1;
/*     */   }
/*     */ 
/*     */   public int indexOf(byte elem)
/*     */   {
/* 470 */     return indexOf(elem, 0);
/*     */   }
/*     */ 
/*     */   private int lastIndexOf(byte elem)
/*     */   {
/* 485 */     int boffset = this.m_firstFree % this.m_blocksize;
/* 486 */     for (int index = this.m_firstFree / this.m_blocksize; 
/* 487 */       index >= 0; 
/* 488 */       index--)
/*     */     {
/* 490 */       byte[] block = this.m_map[index];
/* 491 */       if (block != null)
/* 492 */         for (int offset = boffset; offset >= 0; offset--)
/* 493 */           if (block[offset] == elem)
/* 494 */             return offset + index * this.m_blocksize;
/* 495 */       boffset = 0;
/*     */     }
/* 497 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.SuballocatedByteVector
 * JD-Core Version:    0.6.2
 */
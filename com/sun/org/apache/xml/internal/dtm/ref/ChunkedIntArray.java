/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ final class ChunkedIntArray
/*     */ {
/*  44 */   final int slotsize = 4;
/*     */   static final int lowbits = 10;
/*     */   static final int chunkalloc = 1024;
/*     */   static final int lowmask = 1023;
/*  51 */   ChunksVector chunks = new ChunksVector();
/*  52 */   final int[] fastArray = new int[1024];
/*  53 */   int lastUsed = 0;
/*     */ 
/*     */   ChunkedIntArray(int slotsize)
/*     */   {
/*  61 */     getClass(); if (4 < slotsize)
/*  62 */       throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_CHUNKEDINTARRAY_NOT_SUPPORTED", new Object[] { Integer.toString(slotsize) }));
/*  63 */     getClass(); if (4 > slotsize) {
/*  64 */       getClass(); System.out.println("*****WARNING: ChunkedIntArray(" + slotsize + ") wasting " + (4 - slotsize) + " words per slot");
/*  65 */     }this.chunks.addElement(this.fastArray);
/*     */   }
/*     */ 
/*     */   int appendSlot(int w0, int w1, int w2, int w3)
/*     */   {
/*  88 */     int slotsize = 4;
/*  89 */     int newoffset = (this.lastUsed + 1) * 4;
/*  90 */     int chunkpos = newoffset >> 10;
/*  91 */     int slotpos = newoffset & 0x3FF;
/*     */ 
/*  94 */     if (chunkpos > this.chunks.size() - 1)
/*  95 */       this.chunks.addElement(new int[1024]);
/*  96 */     int[] chunk = this.chunks.elementAt(chunkpos);
/*  97 */     chunk[slotpos] = w0;
/*  98 */     chunk[(slotpos + 1)] = w1;
/*  99 */     chunk[(slotpos + 2)] = w2;
/* 100 */     chunk[(slotpos + 3)] = w3;
/*     */ 
/* 102 */     return ++this.lastUsed;
/*     */   }
/*     */ 
/*     */   int readEntry(int position, int offset)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 123 */     if (offset >= 4)
/* 124 */       throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_OFFSET_BIGGER_THAN_SLOT", null));
/* 125 */     position *= 4;
/* 126 */     int chunkpos = position >> 10;
/* 127 */     int slotpos = position & 0x3FF;
/* 128 */     int[] chunk = this.chunks.elementAt(chunkpos);
/* 129 */     return chunk[(slotpos + offset)];
/*     */   }
/*     */ 
/*     */   int specialFind(int startPos, int position)
/*     */   {
/* 143 */     int ancestor = startPos;
/* 144 */     while (ancestor > 0)
/*     */     {
/* 147 */       ancestor *= 4;
/* 148 */       int chunkpos = ancestor >> 10;
/* 149 */       int slotpos = ancestor & 0x3FF;
/* 150 */       int[] chunk = this.chunks.elementAt(chunkpos);
/*     */ 
/* 155 */       ancestor = chunk[(slotpos + 1)];
/*     */ 
/* 157 */       if (ancestor == position) {
/*     */         break;
/*     */       }
/*     */     }
/* 161 */     if (ancestor <= 0)
/*     */     {
/* 163 */       return position;
/*     */     }
/* 165 */     return -1;
/*     */   }
/*     */ 
/*     */   int slotsUsed()
/*     */   {
/* 173 */     return this.lastUsed;
/*     */   }
/*     */ 
/*     */   void discardLast()
/*     */   {
/* 183 */     this.lastUsed -= 1;
/*     */   }
/*     */ 
/*     */   void writeEntry(int position, int offset, int value)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 204 */     if (offset >= 4)
/* 205 */       throw new ArrayIndexOutOfBoundsException(XMLMessages.createXMLMessage("ER_OFFSET_BIGGER_THAN_SLOT", null));
/* 206 */     position *= 4;
/* 207 */     int chunkpos = position >> 10;
/* 208 */     int slotpos = position & 0x3FF;
/* 209 */     int[] chunk = this.chunks.elementAt(chunkpos);
/* 210 */     chunk[(slotpos + offset)] = value;
/*     */   }
/*     */ 
/*     */   void writeSlot(int position, int w0, int w1, int w2, int w3)
/*     */   {
/* 225 */     position *= 4;
/* 226 */     int chunkpos = position >> 10;
/* 227 */     int slotpos = position & 0x3FF;
/*     */ 
/* 230 */     if (chunkpos > this.chunks.size() - 1)
/* 231 */       this.chunks.addElement(new int[1024]);
/* 232 */     int[] chunk = this.chunks.elementAt(chunkpos);
/* 233 */     chunk[slotpos] = w0;
/* 234 */     chunk[(slotpos + 1)] = w1;
/* 235 */     chunk[(slotpos + 2)] = w2;
/* 236 */     chunk[(slotpos + 3)] = w3;
/*     */   }
/*     */ 
/*     */   void readSlot(int position, int[] buffer)
/*     */   {
/* 258 */     position *= 4;
/* 259 */     int chunkpos = position >> 10;
/* 260 */     int slotpos = position & 0x3FF;
/*     */ 
/* 263 */     if (chunkpos > this.chunks.size() - 1)
/* 264 */       this.chunks.addElement(new int[1024]);
/* 265 */     int[] chunk = this.chunks.elementAt(chunkpos);
/* 266 */     System.arraycopy(chunk, slotpos, buffer, 0, 4);
/*     */   }
/*     */ 
/*     */   class ChunksVector
/*     */   {
/* 272 */     final int BLOCKSIZE = 64;
/* 273 */     int[][] m_map = new int[64][];
/* 274 */     int m_mapSize = 64;
/* 275 */     int pos = 0;
/*     */ 
/*     */     ChunksVector()
/*     */     {
/*     */     }
/*     */ 
/*     */     final int size()
/*     */     {
/* 283 */       return this.pos;
/*     */     }
/*     */ 
/*     */     void addElement(int[] value)
/*     */     {
/* 288 */       if (this.pos >= this.m_mapSize)
/*     */       {
/* 290 */         int orgMapSize = this.m_mapSize;
/* 291 */         while (this.pos >= this.m_mapSize)
/* 292 */           this.m_mapSize += 64;
/* 293 */         int[][] newMap = new int[this.m_mapSize][];
/* 294 */         System.arraycopy(this.m_map, 0, newMap, 0, orgMapSize);
/* 295 */         this.m_map = newMap;
/*     */       }
/*     */ 
/* 299 */       this.m_map[this.pos] = value;
/* 300 */       this.pos += 1;
/*     */     }
/*     */ 
/*     */     final int[] elementAt(int pos)
/*     */     {
/* 305 */       return this.m_map[pos];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.ChunkedIntArray
 * JD-Core Version:    0.6.2
 */
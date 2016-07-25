/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class IntVector
/*     */   implements Cloneable
/*     */ {
/*     */   protected int m_blocksize;
/*     */   protected int[] m_map;
/*  45 */   protected int m_firstFree = 0;
/*     */   protected int m_mapSize;
/*     */ 
/*     */   public IntVector()
/*     */   {
/*  57 */     this.m_blocksize = 32;
/*  58 */     this.m_mapSize = this.m_blocksize;
/*  59 */     this.m_map = new int[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public IntVector(int blocksize)
/*     */   {
/*  70 */     this.m_blocksize = blocksize;
/*  71 */     this.m_mapSize = blocksize;
/*  72 */     this.m_map = new int[blocksize];
/*     */   }
/*     */ 
/*     */   public IntVector(int blocksize, int increaseSize)
/*     */   {
/*  83 */     this.m_blocksize = increaseSize;
/*  84 */     this.m_mapSize = blocksize;
/*  85 */     this.m_map = new int[blocksize];
/*     */   }
/*     */ 
/*     */   public IntVector(IntVector v)
/*     */   {
/*  95 */     this.m_map = new int[v.m_mapSize];
/*  96 */     this.m_mapSize = v.m_mapSize;
/*  97 */     this.m_firstFree = v.m_firstFree;
/*  98 */     this.m_blocksize = v.m_blocksize;
/*  99 */     System.arraycopy(v.m_map, 0, this.m_map, 0, this.m_firstFree);
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/* 109 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public final void setSize(int sz)
/*     */   {
/* 119 */     this.m_firstFree = sz;
/*     */   }
/*     */ 
/*     */   public final void addElement(int value)
/*     */   {
/* 131 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 133 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 135 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 137 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 139 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 142 */     this.m_map[this.m_firstFree] = value;
/*     */ 
/* 144 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final void addElements(int value, int numberOfElements)
/*     */   {
/* 155 */     if (this.m_firstFree + numberOfElements >= this.m_mapSize)
/*     */     {
/* 157 */       this.m_mapSize += this.m_blocksize + numberOfElements;
/*     */ 
/* 159 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 161 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 163 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 166 */     for (int i = 0; i < numberOfElements; i++)
/*     */     {
/* 168 */       this.m_map[this.m_firstFree] = value;
/* 169 */       this.m_firstFree += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void addElements(int numberOfElements)
/*     */   {
/* 181 */     if (this.m_firstFree + numberOfElements >= this.m_mapSize)
/*     */     {
/* 183 */       this.m_mapSize += this.m_blocksize + numberOfElements;
/*     */ 
/* 185 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 187 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 189 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 192 */     this.m_firstFree += numberOfElements;
/*     */   }
/*     */ 
/*     */   public final void insertElementAt(int value, int at)
/*     */   {
/* 208 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 210 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 212 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 214 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 216 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 219 */     if (at <= this.m_firstFree - 1)
/*     */     {
/* 221 */       System.arraycopy(this.m_map, at, this.m_map, at + 1, this.m_firstFree - at);
/*     */     }
/*     */ 
/* 224 */     this.m_map[at] = value;
/*     */ 
/* 226 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final void removeAllElements()
/*     */   {
/* 238 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 240 */       this.m_map[i] = -2147483648;
/*     */     }
/*     */ 
/* 243 */     this.m_firstFree = 0;
/*     */   }
/*     */ 
/*     */   public final boolean removeElement(int s)
/*     */   {
/* 260 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 262 */       if (this.m_map[i] == s)
/*     */       {
/* 264 */         if (i + 1 < this.m_firstFree)
/* 265 */           System.arraycopy(this.m_map, i + 1, this.m_map, i - 1, this.m_firstFree - i);
/*     */         else {
/* 267 */           this.m_map[i] = -2147483648;
/*     */         }
/* 269 */         this.m_firstFree -= 1;
/*     */ 
/* 271 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 275 */     return false;
/*     */   }
/*     */ 
/*     */   public final void removeElementAt(int i)
/*     */   {
/* 289 */     if (i > this.m_firstFree)
/* 290 */       System.arraycopy(this.m_map, i + 1, this.m_map, i, this.m_firstFree);
/*     */     else {
/* 292 */       this.m_map[i] = -2147483648;
/*     */     }
/* 294 */     this.m_firstFree -= 1;
/*     */   }
/*     */ 
/*     */   public final void setElementAt(int value, int index)
/*     */   {
/* 309 */     this.m_map[index] = value;
/*     */   }
/*     */ 
/*     */   public final int elementAt(int i)
/*     */   {
/* 321 */     return this.m_map[i];
/*     */   }
/*     */ 
/*     */   public final boolean contains(int s)
/*     */   {
/* 334 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 336 */       if (this.m_map[i] == s) {
/* 337 */         return true;
/*     */       }
/*     */     }
/* 340 */     return false;
/*     */   }
/*     */ 
/*     */   public final int indexOf(int elem, int index)
/*     */   {
/* 357 */     for (int i = index; i < this.m_firstFree; i++)
/*     */     {
/* 359 */       if (this.m_map[i] == elem) {
/* 360 */         return i;
/*     */       }
/*     */     }
/* 363 */     return -2147483648;
/*     */   }
/*     */ 
/*     */   public final int indexOf(int elem)
/*     */   {
/* 379 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 381 */       if (this.m_map[i] == elem) {
/* 382 */         return i;
/*     */       }
/*     */     }
/* 385 */     return -2147483648;
/*     */   }
/*     */ 
/*     */   public final int lastIndexOf(int elem)
/*     */   {
/* 401 */     for (int i = this.m_firstFree - 1; i >= 0; i--)
/*     */     {
/* 403 */       if (this.m_map[i] == elem) {
/* 404 */         return i;
/*     */       }
/*     */     }
/* 407 */     return -2147483648;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 418 */     return new IntVector(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.IntVector
 * JD-Core Version:    0.6.2
 */
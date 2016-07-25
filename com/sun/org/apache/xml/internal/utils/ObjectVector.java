/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class ObjectVector
/*     */   implements Cloneable
/*     */ {
/*     */   protected int m_blocksize;
/*     */   protected Object[] m_map;
/*  45 */   protected int m_firstFree = 0;
/*     */   protected int m_mapSize;
/*     */ 
/*     */   public ObjectVector()
/*     */   {
/*  57 */     this.m_blocksize = 32;
/*  58 */     this.m_mapSize = this.m_blocksize;
/*  59 */     this.m_map = new Object[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public ObjectVector(int blocksize)
/*     */   {
/*  70 */     this.m_blocksize = blocksize;
/*  71 */     this.m_mapSize = blocksize;
/*  72 */     this.m_map = new Object[blocksize];
/*     */   }
/*     */ 
/*     */   public ObjectVector(int blocksize, int increaseSize)
/*     */   {
/*  83 */     this.m_blocksize = increaseSize;
/*  84 */     this.m_mapSize = blocksize;
/*  85 */     this.m_map = new Object[blocksize];
/*     */   }
/*     */ 
/*     */   public ObjectVector(ObjectVector v)
/*     */   {
/*  95 */     this.m_map = new Object[v.m_mapSize];
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
/*     */   public final void addElement(Object value)
/*     */   {
/* 131 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 133 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 135 */       Object[] newMap = new Object[this.m_mapSize];
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
/*     */   public final void addElements(Object value, int numberOfElements)
/*     */   {
/* 155 */     if (this.m_firstFree + numberOfElements >= this.m_mapSize)
/*     */     {
/* 157 */       this.m_mapSize += this.m_blocksize + numberOfElements;
/*     */ 
/* 159 */       Object[] newMap = new Object[this.m_mapSize];
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
/* 185 */       Object[] newMap = new Object[this.m_mapSize];
/*     */ 
/* 187 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 189 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 192 */     this.m_firstFree += numberOfElements;
/*     */   }
/*     */ 
/*     */   public final void insertElementAt(Object value, int at)
/*     */   {
/* 208 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 210 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 212 */       Object[] newMap = new Object[this.m_mapSize];
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
/* 235 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 237 */       this.m_map[i] = null;
/*     */     }
/*     */ 
/* 240 */     this.m_firstFree = 0;
/*     */   }
/*     */ 
/*     */   public final boolean removeElement(Object s)
/*     */   {
/* 257 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 259 */       if (this.m_map[i] == s)
/*     */       {
/* 261 */         if (i + 1 < this.m_firstFree)
/* 262 */           System.arraycopy(this.m_map, i + 1, this.m_map, i - 1, this.m_firstFree - i);
/*     */         else {
/* 264 */           this.m_map[i] = null;
/*     */         }
/* 266 */         this.m_firstFree -= 1;
/*     */ 
/* 268 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */   public final void removeElementAt(int i)
/*     */   {
/* 286 */     if (i > this.m_firstFree)
/* 287 */       System.arraycopy(this.m_map, i + 1, this.m_map, i, this.m_firstFree);
/*     */     else {
/* 289 */       this.m_map[i] = null;
/*     */     }
/* 291 */     this.m_firstFree -= 1;
/*     */   }
/*     */ 
/*     */   public final void setElementAt(Object value, int index)
/*     */   {
/* 306 */     this.m_map[index] = value;
/*     */   }
/*     */ 
/*     */   public final Object elementAt(int i)
/*     */   {
/* 318 */     return this.m_map[i];
/*     */   }
/*     */ 
/*     */   public final boolean contains(Object s)
/*     */   {
/* 331 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 333 */       if (this.m_map[i] == s) {
/* 334 */         return true;
/*     */       }
/*     */     }
/* 337 */     return false;
/*     */   }
/*     */ 
/*     */   public final int indexOf(Object elem, int index)
/*     */   {
/* 354 */     for (int i = index; i < this.m_firstFree; i++)
/*     */     {
/* 356 */       if (this.m_map[i] == elem) {
/* 357 */         return i;
/*     */       }
/*     */     }
/* 360 */     return -2147483648;
/*     */   }
/*     */ 
/*     */   public final int indexOf(Object elem)
/*     */   {
/* 376 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 378 */       if (this.m_map[i] == elem) {
/* 379 */         return i;
/*     */       }
/*     */     }
/* 382 */     return -2147483648;
/*     */   }
/*     */ 
/*     */   public final int lastIndexOf(Object elem)
/*     */   {
/* 398 */     for (int i = this.m_firstFree - 1; i >= 0; i--)
/*     */     {
/* 400 */       if (this.m_map[i] == elem) {
/* 401 */         return i;
/*     */       }
/*     */     }
/* 404 */     return -2147483648;
/*     */   }
/*     */ 
/*     */   public final void setToSize(int size)
/*     */   {
/* 414 */     Object[] newMap = new Object[size];
/*     */ 
/* 416 */     System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree);
/* 417 */     this.m_mapSize = size;
/*     */ 
/* 419 */     this.m_map = newMap;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 431 */     return new ObjectVector(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.ObjectVector
 * JD-Core Version:    0.6.2
 */
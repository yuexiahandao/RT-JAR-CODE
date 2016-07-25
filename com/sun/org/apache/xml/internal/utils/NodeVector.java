/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class NodeVector
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   static final long serialVersionUID = -713473092200731870L;
/*     */   private int m_blocksize;
/*     */   private int[] m_map;
/*  53 */   protected int m_firstFree = 0;
/*     */   private int m_mapSize;
/*     */ 
/*     */   public NodeVector()
/*     */   {
/*  66 */     this.m_blocksize = 32;
/*  67 */     this.m_mapSize = 0;
/*     */   }
/*     */ 
/*     */   public NodeVector(int blocksize)
/*     */   {
/*  77 */     this.m_blocksize = blocksize;
/*  78 */     this.m_mapSize = 0;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/*  91 */     NodeVector clone = (NodeVector)super.clone();
/*     */ 
/*  93 */     if ((null != this.m_map) && (this.m_map == clone.m_map))
/*     */     {
/*  95 */       clone.m_map = new int[this.m_map.length];
/*     */ 
/*  97 */       System.arraycopy(this.m_map, 0, clone.m_map, 0, this.m_map.length);
/*     */     }
/*     */ 
/* 100 */     return clone;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 110 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public void addElement(int value)
/*     */   {
/* 121 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 123 */       if (null == this.m_map)
/*     */       {
/* 125 */         this.m_map = new int[this.m_blocksize];
/* 126 */         this.m_mapSize = this.m_blocksize;
/*     */       }
/*     */       else
/*     */       {
/* 130 */         this.m_mapSize += this.m_blocksize;
/*     */ 
/* 132 */         int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 134 */         System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 136 */         this.m_map = newMap;
/*     */       }
/*     */     }
/*     */ 
/* 140 */     this.m_map[this.m_firstFree] = value;
/*     */ 
/* 142 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final void push(int value)
/*     */   {
/* 153 */     int ff = this.m_firstFree;
/*     */ 
/* 155 */     if (ff + 1 >= this.m_mapSize)
/*     */     {
/* 157 */       if (null == this.m_map)
/*     */       {
/* 159 */         this.m_map = new int[this.m_blocksize];
/* 160 */         this.m_mapSize = this.m_blocksize;
/*     */       }
/*     */       else
/*     */       {
/* 164 */         this.m_mapSize += this.m_blocksize;
/*     */ 
/* 166 */         int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 168 */         System.arraycopy(this.m_map, 0, newMap, 0, ff + 1);
/*     */ 
/* 170 */         this.m_map = newMap;
/*     */       }
/*     */     }
/*     */ 
/* 174 */     this.m_map[ff] = value;
/*     */ 
/* 176 */     ff++;
/*     */ 
/* 178 */     this.m_firstFree = ff;
/*     */   }
/*     */ 
/*     */   public final int pop()
/*     */   {
/* 189 */     this.m_firstFree -= 1;
/*     */ 
/* 191 */     int n = this.m_map[this.m_firstFree];
/*     */ 
/* 193 */     this.m_map[this.m_firstFree] = -1;
/*     */ 
/* 195 */     return n;
/*     */   }
/*     */ 
/*     */   public final int popAndTop()
/*     */   {
/* 207 */     this.m_firstFree -= 1;
/*     */ 
/* 209 */     this.m_map[this.m_firstFree] = -1;
/*     */ 
/* 211 */     return this.m_firstFree == 0 ? -1 : this.m_map[(this.m_firstFree - 1)];
/*     */   }
/*     */ 
/*     */   public final void popQuick()
/*     */   {
/* 220 */     this.m_firstFree -= 1;
/*     */ 
/* 222 */     this.m_map[this.m_firstFree] = -1;
/*     */   }
/*     */ 
/*     */   public final int peepOrNull()
/*     */   {
/* 234 */     return (null != this.m_map) && (this.m_firstFree > 0) ? this.m_map[(this.m_firstFree - 1)] : -1;
/*     */   }
/*     */ 
/*     */   public final void pushPair(int v1, int v2)
/*     */   {
/* 249 */     if (null == this.m_map)
/*     */     {
/* 251 */       this.m_map = new int[this.m_blocksize];
/* 252 */       this.m_mapSize = this.m_blocksize;
/*     */     }
/* 256 */     else if (this.m_firstFree + 2 >= this.m_mapSize)
/*     */     {
/* 258 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 260 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 262 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree);
/*     */ 
/* 264 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 268 */     this.m_map[this.m_firstFree] = v1;
/* 269 */     this.m_map[(this.m_firstFree + 1)] = v2;
/* 270 */     this.m_firstFree += 2;
/*     */   }
/*     */ 
/*     */   public final void popPair()
/*     */   {
/* 281 */     this.m_firstFree -= 2;
/* 282 */     this.m_map[this.m_firstFree] = -1;
/* 283 */     this.m_map[(this.m_firstFree + 1)] = -1;
/*     */   }
/*     */ 
/*     */   public final void setTail(int n)
/*     */   {
/* 295 */     this.m_map[(this.m_firstFree - 1)] = n;
/*     */   }
/*     */ 
/*     */   public final void setTailSub1(int n)
/*     */   {
/* 307 */     this.m_map[(this.m_firstFree - 2)] = n;
/*     */   }
/*     */ 
/*     */   public final int peepTail()
/*     */   {
/* 319 */     return this.m_map[(this.m_firstFree - 1)];
/*     */   }
/*     */ 
/*     */   public final int peepTailSub1()
/*     */   {
/* 331 */     return this.m_map[(this.m_firstFree - 2)];
/*     */   }
/*     */ 
/*     */   public void insertInOrder(int value)
/*     */   {
/* 342 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 344 */       if (value < this.m_map[i])
/*     */       {
/* 346 */         insertElementAt(value, i);
/*     */ 
/* 348 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 352 */     addElement(value);
/*     */   }
/*     */ 
/*     */   public void insertElementAt(int value, int at)
/*     */   {
/* 367 */     if (null == this.m_map)
/*     */     {
/* 369 */       this.m_map = new int[this.m_blocksize];
/* 370 */       this.m_mapSize = this.m_blocksize;
/*     */     }
/* 372 */     else if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 374 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 376 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 378 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 380 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 383 */     if (at <= this.m_firstFree - 1)
/*     */     {
/* 385 */       System.arraycopy(this.m_map, at, this.m_map, at + 1, this.m_firstFree - at);
/*     */     }
/*     */ 
/* 388 */     this.m_map[at] = value;
/*     */ 
/* 390 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public void appendNodes(NodeVector nodes)
/*     */   {
/* 401 */     int nNodes = nodes.size();
/*     */ 
/* 403 */     if (null == this.m_map)
/*     */     {
/* 405 */       this.m_mapSize = (nNodes + this.m_blocksize);
/* 406 */       this.m_map = new int[this.m_mapSize];
/*     */     }
/* 408 */     else if (this.m_firstFree + nNodes >= this.m_mapSize)
/*     */     {
/* 410 */       this.m_mapSize += nNodes + this.m_blocksize;
/*     */ 
/* 412 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/* 414 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + nNodes);
/*     */ 
/* 416 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 419 */     System.arraycopy(nodes.m_map, 0, this.m_map, this.m_firstFree, nNodes);
/*     */ 
/* 421 */     this.m_firstFree += nNodes;
/*     */   }
/*     */ 
/*     */   public void removeAllElements()
/*     */   {
/* 433 */     if (null == this.m_map) {
/* 434 */       return;
/*     */     }
/* 436 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 438 */       this.m_map[i] = -1;
/*     */     }
/*     */ 
/* 441 */     this.m_firstFree = 0;
/*     */   }
/*     */ 
/*     */   public void RemoveAllNoClear()
/*     */   {
/* 450 */     if (null == this.m_map) {
/* 451 */       return;
/*     */     }
/* 453 */     this.m_firstFree = 0;
/*     */   }
/*     */ 
/*     */   public boolean removeElement(int s)
/*     */   {
/* 470 */     if (null == this.m_map) {
/* 471 */       return false;
/*     */     }
/* 473 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 475 */       int node = this.m_map[i];
/*     */ 
/* 477 */       if (node == s)
/*     */       {
/* 479 */         if (i > this.m_firstFree)
/* 480 */           System.arraycopy(this.m_map, i + 1, this.m_map, i - 1, this.m_firstFree - i);
/*     */         else {
/* 482 */           this.m_map[i] = -1;
/*     */         }
/* 484 */         this.m_firstFree -= 1;
/*     */ 
/* 486 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 490 */     return false;
/*     */   }
/*     */ 
/*     */   public void removeElementAt(int i)
/*     */   {
/* 504 */     if (null == this.m_map) {
/* 505 */       return;
/*     */     }
/* 507 */     if (i > this.m_firstFree)
/* 508 */       System.arraycopy(this.m_map, i + 1, this.m_map, i - 1, this.m_firstFree - i);
/*     */     else
/* 510 */       this.m_map[i] = -1;
/*     */   }
/*     */ 
/*     */   public void setElementAt(int node, int index)
/*     */   {
/* 526 */     if (null == this.m_map)
/*     */     {
/* 528 */       this.m_map = new int[this.m_blocksize];
/* 529 */       this.m_mapSize = this.m_blocksize;
/*     */     }
/*     */ 
/* 532 */     if (index == -1) {
/* 533 */       addElement(node);
/*     */     }
/* 535 */     this.m_map[index] = node;
/*     */   }
/*     */ 
/*     */   public int elementAt(int i)
/*     */   {
/* 548 */     if (null == this.m_map) {
/* 549 */       return -1;
/*     */     }
/* 551 */     return this.m_map[i];
/*     */   }
/*     */ 
/*     */   public boolean contains(int s)
/*     */   {
/* 564 */     if (null == this.m_map) {
/* 565 */       return false;
/*     */     }
/* 567 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 569 */       int node = this.m_map[i];
/*     */ 
/* 571 */       if (node == s) {
/* 572 */         return true;
/*     */       }
/*     */     }
/* 575 */     return false;
/*     */   }
/*     */ 
/*     */   public int indexOf(int elem, int index)
/*     */   {
/* 592 */     if (null == this.m_map) {
/* 593 */       return -1;
/*     */     }
/* 595 */     for (int i = index; i < this.m_firstFree; i++)
/*     */     {
/* 597 */       int node = this.m_map[i];
/*     */ 
/* 599 */       if (node == elem) {
/* 600 */         return i;
/*     */       }
/*     */     }
/* 603 */     return -1;
/*     */   }
/*     */ 
/*     */   public int indexOf(int elem)
/*     */   {
/* 619 */     if (null == this.m_map) {
/* 620 */       return -1;
/*     */     }
/* 622 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 624 */       int node = this.m_map[i];
/*     */ 
/* 626 */       if (node == elem) {
/* 627 */         return i;
/*     */       }
/*     */     }
/* 630 */     return -1;
/*     */   }
/*     */ 
/*     */   public void sort(int[] a, int lo0, int hi0)
/*     */     throws Exception
/*     */   {
/* 645 */     int lo = lo0;
/* 646 */     int hi = hi0;
/*     */ 
/* 649 */     if (lo >= hi)
/*     */     {
/* 651 */       return;
/*     */     }
/* 653 */     if (lo == hi - 1)
/*     */     {
/* 659 */       if (a[lo] > a[hi])
/*     */       {
/* 661 */         int T = a[lo];
/*     */ 
/* 663 */         a[lo] = a[hi];
/* 664 */         a[hi] = T;
/*     */       }
/*     */ 
/* 667 */       return;
/*     */     }
/*     */ 
/* 673 */     int pivot = a[((lo + hi) / 2)];
/*     */ 
/* 675 */     a[((lo + hi) / 2)] = a[hi];
/* 676 */     a[hi] = pivot;
/*     */ 
/* 678 */     while (lo < hi)
/*     */     {
/* 685 */       while ((a[lo] <= pivot) && (lo < hi))
/*     */       {
/* 687 */         lo++;
/*     */       }
/*     */ 
/* 694 */       while ((pivot <= a[hi]) && (lo < hi))
/*     */       {
/* 696 */         hi--;
/*     */       }
/*     */ 
/* 702 */       if (lo < hi)
/*     */       {
/* 704 */         int T = a[lo];
/*     */ 
/* 706 */         a[lo] = a[hi];
/* 707 */         a[hi] = T;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 720 */     a[hi0] = a[hi];
/* 721 */     a[hi] = pivot;
/*     */ 
/* 728 */     sort(a, lo0, lo - 1);
/* 729 */     sort(a, hi + 1, hi0);
/*     */   }
/*     */ 
/*     */   public void sort()
/*     */     throws Exception
/*     */   {
/* 739 */     sort(this.m_map, 0, this.m_firstFree - 1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.NodeVector
 * JD-Core Version:    0.6.2
 */
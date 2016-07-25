/*     */ package com.sun.org.apache.xpath.internal.compiler;
/*     */ 
/*     */ public class OpMapVector
/*     */ {
/*     */   protected int m_blocksize;
/*     */   protected int[] m_map;
/*  42 */   protected int m_lengthPos = 0;
/*     */   protected int m_mapSize;
/*     */ 
/*     */   public OpMapVector(int blocksize, int increaseSize, int lengthPos)
/*     */   {
/*  55 */     this.m_blocksize = increaseSize;
/*  56 */     this.m_mapSize = blocksize;
/*  57 */     this.m_lengthPos = lengthPos;
/*  58 */     this.m_map = new int[blocksize];
/*     */   }
/*     */ 
/*     */   public final int elementAt(int i)
/*     */   {
/*  70 */     return this.m_map[i];
/*     */   }
/*     */ 
/*     */   public final void setElementAt(int value, int index)
/*     */   {
/*  85 */     if (index >= this.m_mapSize)
/*     */     {
/*  87 */       int oldSize = this.m_mapSize;
/*     */ 
/*  89 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/*  91 */       int[] newMap = new int[this.m_mapSize];
/*     */ 
/*  93 */       System.arraycopy(this.m_map, 0, newMap, 0, oldSize);
/*     */ 
/*  95 */       this.m_map = newMap;
/*     */     }
/*     */ 
/*  98 */     this.m_map[index] = value;
/*     */   }
/*     */ 
/*     */   public final void setToSize(int size)
/*     */   {
/* 109 */     int[] newMap = new int[size];
/*     */ 
/* 111 */     System.arraycopy(this.m_map, 0, newMap, 0, this.m_map[this.m_lengthPos]);
/*     */ 
/* 113 */     this.m_mapSize = size;
/* 114 */     this.m_map = newMap;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.compiler.OpMapVector
 * JD-Core Version:    0.6.2
 */
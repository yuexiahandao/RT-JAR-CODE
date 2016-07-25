/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class StringToStringTableVector
/*     */ {
/*     */   private int m_blocksize;
/*     */   private StringToStringTable[] m_map;
/*  40 */   private int m_firstFree = 0;
/*     */   private int m_mapSize;
/*     */ 
/*     */   public StringToStringTableVector()
/*     */   {
/*  52 */     this.m_blocksize = 8;
/*  53 */     this.m_mapSize = this.m_blocksize;
/*  54 */     this.m_map = new StringToStringTable[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public StringToStringTableVector(int blocksize)
/*     */   {
/*  65 */     this.m_blocksize = blocksize;
/*  66 */     this.m_mapSize = blocksize;
/*  67 */     this.m_map = new StringToStringTable[blocksize];
/*     */   }
/*     */ 
/*     */   public final int getLength()
/*     */   {
/*  77 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/*  87 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public final void addElement(StringToStringTable value)
/*     */   {
/*  98 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 100 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 102 */       StringToStringTable[] newMap = new StringToStringTable[this.m_mapSize];
/*     */ 
/* 104 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 106 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 109 */     this.m_map[this.m_firstFree] = value;
/*     */ 
/* 111 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final String get(String key)
/*     */   {
/* 126 */     for (int i = this.m_firstFree - 1; i >= 0; i--)
/*     */     {
/* 128 */       String nsuri = this.m_map[i].get(key);
/*     */ 
/* 130 */       if (nsuri != null) {
/* 131 */         return nsuri;
/*     */       }
/*     */     }
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   public final boolean containsKey(String key)
/*     */   {
/* 148 */     for (int i = this.m_firstFree - 1; i >= 0; i--)
/*     */     {
/* 150 */       if (this.m_map[i].get(key) != null) {
/* 151 */         return true;
/*     */       }
/*     */     }
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   public final void removeLastElem()
/*     */   {
/* 163 */     if (this.m_firstFree > 0)
/*     */     {
/* 165 */       this.m_map[this.m_firstFree] = null;
/*     */ 
/* 167 */       this.m_firstFree -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final StringToStringTable elementAt(int i)
/*     */   {
/* 180 */     return this.m_map[i];
/*     */   }
/*     */ 
/*     */   public final boolean contains(StringToStringTable s)
/*     */   {
/* 193 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 195 */       if (this.m_map[i].equals(s)) {
/* 196 */         return true;
/*     */       }
/*     */     }
/* 199 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.StringToStringTableVector
 * JD-Core Version:    0.6.2
 */
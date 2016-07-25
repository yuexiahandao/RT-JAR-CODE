/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class StringVector
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 4995234972032919748L;
/*     */   protected int m_blocksize;
/*     */   protected String[] m_map;
/*  41 */   protected int m_firstFree = 0;
/*     */   protected int m_mapSize;
/*     */ 
/*     */   public StringVector()
/*     */   {
/*  53 */     this.m_blocksize = 8;
/*  54 */     this.m_mapSize = this.m_blocksize;
/*  55 */     this.m_map = new String[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public StringVector(int blocksize)
/*     */   {
/*  66 */     this.m_blocksize = blocksize;
/*  67 */     this.m_mapSize = blocksize;
/*  68 */     this.m_map = new String[blocksize];
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  78 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/*  88 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public final void addElement(String value)
/*     */   {
/*  99 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 101 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 103 */       String[] newMap = new String[this.m_mapSize];
/*     */ 
/* 105 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 107 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 110 */     this.m_map[this.m_firstFree] = value;
/*     */ 
/* 112 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final String elementAt(int i)
/*     */   {
/* 124 */     return this.m_map[i];
/*     */   }
/*     */ 
/*     */   public final boolean contains(String s)
/*     */   {
/* 137 */     if (null == s) {
/* 138 */       return false;
/*     */     }
/* 140 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 142 */       if (this.m_map[i].equals(s)) {
/* 143 */         return true;
/*     */       }
/*     */     }
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean containsIgnoreCase(String s)
/*     */   {
/* 159 */     if (null == s) {
/* 160 */       return false;
/*     */     }
/* 162 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 164 */       if (this.m_map[i].equalsIgnoreCase(s)) {
/* 165 */         return true;
/*     */       }
/*     */     }
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   public final void push(String s)
/*     */   {
/* 179 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 181 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 183 */       String[] newMap = new String[this.m_mapSize];
/*     */ 
/* 185 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 187 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 190 */     this.m_map[this.m_firstFree] = s;
/*     */ 
/* 192 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final String pop()
/*     */   {
/* 204 */     if (this.m_firstFree <= 0) {
/* 205 */       return null;
/*     */     }
/* 207 */     this.m_firstFree -= 1;
/*     */ 
/* 209 */     String s = this.m_map[this.m_firstFree];
/*     */ 
/* 211 */     this.m_map[this.m_firstFree] = null;
/*     */ 
/* 213 */     return s;
/*     */   }
/*     */ 
/*     */   public final String peek()
/*     */   {
/* 223 */     return this.m_firstFree <= 0 ? null : this.m_map[(this.m_firstFree - 1)];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.StringVector
 * JD-Core Version:    0.6.2
 */
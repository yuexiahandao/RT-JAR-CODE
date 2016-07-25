/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class StringToIntTable
/*     */ {
/*     */   public static final int INVALID_KEY = -10000;
/*     */   private int m_blocksize;
/*     */   private String[] m_map;
/*     */   private int[] m_values;
/*  47 */   private int m_firstFree = 0;
/*     */   private int m_mapSize;
/*     */ 
/*     */   public StringToIntTable()
/*     */   {
/*  59 */     this.m_blocksize = 8;
/*  60 */     this.m_mapSize = this.m_blocksize;
/*  61 */     this.m_map = new String[this.m_blocksize];
/*  62 */     this.m_values = new int[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public StringToIntTable(int blocksize)
/*     */   {
/*  73 */     this.m_blocksize = blocksize;
/*  74 */     this.m_mapSize = blocksize;
/*  75 */     this.m_map = new String[blocksize];
/*  76 */     this.m_values = new int[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public final int getLength()
/*     */   {
/*  86 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public final void put(String key, int value)
/*     */   {
/*  98 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 100 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 102 */       String[] newMap = new String[this.m_mapSize];
/*     */ 
/* 104 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 106 */       this.m_map = newMap;
/*     */ 
/* 108 */       int[] newValues = new int[this.m_mapSize];
/*     */ 
/* 110 */       System.arraycopy(this.m_values, 0, newValues, 0, this.m_firstFree + 1);
/*     */ 
/* 112 */       this.m_values = newValues;
/*     */     }
/*     */ 
/* 115 */     this.m_map[this.m_firstFree] = key;
/* 116 */     this.m_values[this.m_firstFree] = value;
/*     */ 
/* 118 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final int get(String key)
/*     */   {
/* 132 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 134 */       if (this.m_map[i].equals(key)) {
/* 135 */         return this.m_values[i];
/*     */       }
/*     */     }
/* 138 */     return -10000;
/*     */   }
/*     */ 
/*     */   public final int getIgnoreCase(String key)
/*     */   {
/* 151 */     if (null == key) {
/* 152 */       return -10000;
/*     */     }
/* 154 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 156 */       if (this.m_map[i].equalsIgnoreCase(key)) {
/* 157 */         return this.m_values[i];
/*     */       }
/*     */     }
/* 160 */     return -10000;
/*     */   }
/*     */ 
/*     */   public final boolean contains(String key)
/*     */   {
/* 173 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 175 */       if (this.m_map[i].equals(key)) {
/* 176 */         return true;
/*     */       }
/*     */     }
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */   public final String[] keys()
/*     */   {
/* 189 */     String[] keysArr = new String[this.m_firstFree];
/*     */ 
/* 191 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 193 */       keysArr[i] = this.m_map[i];
/*     */     }
/*     */ 
/* 196 */     return keysArr;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.StringToIntTable
 * JD-Core Version:    0.6.2
 */
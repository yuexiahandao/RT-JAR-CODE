/*     */ package com.sun.org.apache.xml.internal.serializer.utils;
/*     */ 
/*     */ public final class StringToIntTable
/*     */ {
/*     */   public static final int INVALID_KEY = -10000;
/*     */   private int m_blocksize;
/*     */   private String[] m_map;
/*     */   private int[] m_values;
/*  54 */   private int m_firstFree = 0;
/*     */   private int m_mapSize;
/*     */ 
/*     */   public StringToIntTable()
/*     */   {
/*  66 */     this.m_blocksize = 8;
/*  67 */     this.m_mapSize = this.m_blocksize;
/*  68 */     this.m_map = new String[this.m_blocksize];
/*  69 */     this.m_values = new int[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public StringToIntTable(int blocksize)
/*     */   {
/*  80 */     this.m_blocksize = blocksize;
/*  81 */     this.m_mapSize = blocksize;
/*  82 */     this.m_map = new String[blocksize];
/*  83 */     this.m_values = new int[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public final int getLength()
/*     */   {
/*  93 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public final void put(String key, int value)
/*     */   {
/* 105 */     if (this.m_firstFree + 1 >= this.m_mapSize)
/*     */     {
/* 107 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/* 109 */       String[] newMap = new String[this.m_mapSize];
/*     */ 
/* 111 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/* 113 */       this.m_map = newMap;
/*     */ 
/* 115 */       int[] newValues = new int[this.m_mapSize];
/*     */ 
/* 117 */       System.arraycopy(this.m_values, 0, newValues, 0, this.m_firstFree + 1);
/*     */ 
/* 119 */       this.m_values = newValues;
/*     */     }
/*     */ 
/* 122 */     this.m_map[this.m_firstFree] = key;
/* 123 */     this.m_values[this.m_firstFree] = value;
/*     */ 
/* 125 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final int get(String key)
/*     */   {
/* 139 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 141 */       if (this.m_map[i].equals(key)) {
/* 142 */         return this.m_values[i];
/*     */       }
/*     */     }
/* 145 */     return -10000;
/*     */   }
/*     */ 
/*     */   public final int getIgnoreCase(String key)
/*     */   {
/* 158 */     if (null == key) {
/* 159 */       return -10000;
/*     */     }
/* 161 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 163 */       if (this.m_map[i].equalsIgnoreCase(key)) {
/* 164 */         return this.m_values[i];
/*     */       }
/*     */     }
/* 167 */     return -10000;
/*     */   }
/*     */ 
/*     */   public final boolean contains(String key)
/*     */   {
/* 180 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 182 */       if (this.m_map[i].equals(key)) {
/* 183 */         return true;
/*     */       }
/*     */     }
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */   public final String[] keys()
/*     */   {
/* 196 */     String[] keysArr = new String[this.m_firstFree];
/*     */ 
/* 198 */     for (int i = 0; i < this.m_firstFree; i++)
/*     */     {
/* 200 */       keysArr[i] = this.m_map[i];
/*     */     }
/*     */ 
/* 203 */     return keysArr;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.utils.StringToIntTable
 * JD-Core Version:    0.6.2
 */
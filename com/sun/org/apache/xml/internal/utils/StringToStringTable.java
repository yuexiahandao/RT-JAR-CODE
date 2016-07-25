/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ public class StringToStringTable
/*     */ {
/*     */   private int m_blocksize;
/*     */   private String[] m_map;
/*  40 */   private int m_firstFree = 0;
/*     */   private int m_mapSize;
/*     */ 
/*     */   public StringToStringTable()
/*     */   {
/*  52 */     this.m_blocksize = 16;
/*  53 */     this.m_mapSize = this.m_blocksize;
/*  54 */     this.m_map = new String[this.m_blocksize];
/*     */   }
/*     */ 
/*     */   public StringToStringTable(int blocksize)
/*     */   {
/*  65 */     this.m_blocksize = blocksize;
/*  66 */     this.m_mapSize = blocksize;
/*  67 */     this.m_map = new String[blocksize];
/*     */   }
/*     */ 
/*     */   public final int getLength()
/*     */   {
/*  77 */     return this.m_firstFree;
/*     */   }
/*     */ 
/*     */   public final void put(String key, String value)
/*     */   {
/*  91 */     if (this.m_firstFree + 2 >= this.m_mapSize)
/*     */     {
/*  93 */       this.m_mapSize += this.m_blocksize;
/*     */ 
/*  95 */       String[] newMap = new String[this.m_mapSize];
/*     */ 
/*  97 */       System.arraycopy(this.m_map, 0, newMap, 0, this.m_firstFree + 1);
/*     */ 
/*  99 */       this.m_map = newMap;
/*     */     }
/*     */ 
/* 102 */     this.m_map[this.m_firstFree] = key;
/*     */ 
/* 104 */     this.m_firstFree += 1;
/*     */ 
/* 106 */     this.m_map[this.m_firstFree] = value;
/*     */ 
/* 108 */     this.m_firstFree += 1;
/*     */   }
/*     */ 
/*     */   public final String get(String key)
/*     */   {
/* 121 */     for (int i = 0; i < this.m_firstFree; i += 2)
/*     */     {
/* 123 */       if (this.m_map[i].equals(key)) {
/* 124 */         return this.m_map[(i + 1)];
/*     */       }
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   public final void remove(String key)
/*     */   {
/* 138 */     for (int i = 0; i < this.m_firstFree; i += 2)
/*     */     {
/* 140 */       if (this.m_map[i].equals(key))
/*     */       {
/* 142 */         if (i + 2 < this.m_firstFree) {
/* 143 */           System.arraycopy(this.m_map, i + 2, this.m_map, i, this.m_firstFree - (i + 2));
/*     */         }
/* 145 */         this.m_firstFree -= 2;
/* 146 */         this.m_map[this.m_firstFree] = null;
/* 147 */         this.m_map[(this.m_firstFree + 1)] = null;
/*     */ 
/* 149 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String getIgnoreCase(String key)
/*     */   {
/* 164 */     if (null == key) {
/* 165 */       return null;
/*     */     }
/* 167 */     for (int i = 0; i < this.m_firstFree; i += 2)
/*     */     {
/* 169 */       if (this.m_map[i].equalsIgnoreCase(key)) {
/* 170 */         return this.m_map[(i + 1)];
/*     */       }
/*     */     }
/* 173 */     return null;
/*     */   }
/*     */ 
/*     */   public final String getByValue(String val)
/*     */   {
/* 186 */     for (int i = 1; i < this.m_firstFree; i += 2)
/*     */     {
/* 188 */       if (this.m_map[i].equals(val)) {
/* 189 */         return this.m_map[(i - 1)];
/*     */       }
/*     */     }
/* 192 */     return null;
/*     */   }
/*     */ 
/*     */   public final String elementAt(int i)
/*     */   {
/* 204 */     return this.m_map[i];
/*     */   }
/*     */ 
/*     */   public final boolean contains(String key)
/*     */   {
/* 217 */     for (int i = 0; i < this.m_firstFree; i += 2)
/*     */     {
/* 219 */       if (this.m_map[i].equals(key)) {
/* 220 */         return true;
/*     */       }
/*     */     }
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean containsValue(String val)
/*     */   {
/* 236 */     for (int i = 1; i < this.m_firstFree; i += 2)
/*     */     {
/* 238 */       if (this.m_map[i].equals(val)) {
/* 239 */         return true;
/*     */       }
/*     */     }
/* 242 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.StringToStringTable
 * JD-Core Version:    0.6.2
 */